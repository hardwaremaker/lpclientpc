/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.system.pflege;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;
import java.util.List;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.system.service.MandantDto;
import com.lp.util.EJBExceptionLP;

public class LieferscheinpositionenKorrigieren6999 extends PflegefunktionSupportsProgressBar {
	
	private PanelConsole console;
	private boolean startable = false;
	private boolean isRunning = false;
	private boolean cancelled = false;
	private JPanel panel;
	
	private JLabel lblTotalCount ;
	private JLabel lblActualInvoice ;

	@Override
	public String getKategorie() {
		return KATEGORIE_LIEFERSCHEIN;
	}

	@Override
	public String getName() {
		return "Artikelset Lieferscheinpositionen SP6999";
	}

	@Override
	public String getBeschreibung() {
		String clients = "" ;
		int clientCount = 0 ;
		try {
			MandantDto[] mandantDtos = DelegateFactory.getInstance().getMandantDelegate().mandantFindAll() ;
			for (MandantDto mandantDto : mandantDtos) {
				if(clients.length() > 0) {
					clients += ", " ;
				}

				clients += mandantDto.getCNr() ;
			}
			clientCount = mandantDtos.length ;
		} catch(EJBExceptionLP e) {
		} catch(ExceptionLP e) {			
		} catch(Throwable t) {
		}
		
		return "<html>" +
				"In Lieferscheinpositionen die zu einem Artikelset geh&ouml;ren " +
				"die Mengen und Preisstatistikfelder ab Gesch&auml;ftsjahr 2018 korrigieren." +
				(clientCount > 1
					? ("<p><b>Diese Funktion mu&szlig; f&uuml;r jeden Mandanten (" + clients + ") explizit aufgerufen werden!</b></p>") 
					: "") +
				"</html>";
	}

	@Override
	public JPanel getPanel() {
		if(panel != null) {
			startable = true ;
			fireEnabledStartableEvent(new EventObject(this));
			return panel;
		}

		console = new PanelConsole();

		
		lblTotalCount = new JLabel("unbekannt") ;
		lblActualInvoice = new JLabel("unbekannt") ;
		
//		console.setVisible(false) ;
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		Insets inset0 = new Insets(0, 0, 0, 0);
		panel.add(new JLabel("<html><b>Anzahl Belege:</b></html>"),
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, inset0, 0, 0));
		panel.add(lblTotalCount,
				new GridBagConstraints(1, 0, 2, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, inset0, 0, 0));
		panel.add(new JLabel("<html><b>Aktueller Beleg:</b></html>"),
				new GridBagConstraints(0, 1, 1, 1, 0.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, inset0, 0, 0));
		panel.add(lblActualInvoice,
				new GridBagConstraints(1, 1, 1, 1, 0.1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, inset0, 0, 0));
		panel.add(new JScrollPane(console),
				new GridBagConstraints(0, 2, 3, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, inset0, 0, 0));
		
		startable = true ;
		fireEnabledStartableEvent(new EventObject(this));

		return panel;
	}
		
	@Override
	public boolean isStartable() {
		return !isRunning && startable;
	}

	private String pluralize(int count, String singular, String suffix) {
		if (count == 0) return "keine " + singular + suffix ;
		if (count == 1) return "1 " + singular ;
		
		return "" + count + " " + singular + suffix ;
	}
	
	@Override
	public void run() {
		cancelled = false;
		startable = false ;

		
		try {
			isRunning = true ;
			
			lblTotalCount.setText("ermitteln...");
			List<Integer> allInvoices = DelegateFactory.getInstance().getRechnungDelegate().repairLieferscheinSP6999GetList() ;
			LieferscheinpositionenKorrigieren6999.this.setProgress(new DefaultBoundedRangeModel(0, 0, 0, allInvoices.size()));
			
			String s = pluralize(allInvoices.size(), "Lieferschein", "e");
			lblTotalCount.setText("<html><b>" + s + "</b></html>");			

			if(allInvoices.size() > 0) {
				lblActualInvoice.setText("berechnen...") ;
				SwingWorker<Void, Workerdata> worker = new Worker(allInvoices);
				worker.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if ("progress".equals(evt.getPropertyName())) {
							setProgressValue((Integer) evt.getNewValue());
							fireProgressEvent(null);
						}
					}
				});
				fireStartedEvent(new EventObject(this));
				worker.execute();				
			} else {
				lblTotalCount.setText("<html><b>Es gab nichts zu tun</b></html>");
				lblActualInvoice.setText("") ;
				setProgress(new DefaultBoundedRangeModel(100, 0, 0, 100)) ;
				isRunning = false ;
			}
		} catch(Throwable t) {
			isRunning = false ;
		}
	}
	
	
	@Override
	public void cancel() {
		synchronized (this) {
			cancelled = true;			
		}
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public void init() {
	}

	@Override
	public void eventYouAreSelected() {
	}
	
	public class Workerdata {
		private int totalSize ;
		private String cnr ;
		private Integer id ;
		private String belegtyp ;
		
		public Workerdata(int totalsize) {
			this.totalSize =  totalsize ;
		}
		
		public Workerdata(String belegtyp, int totalsize, String cnr, Integer id) {
			this.belegtyp = belegtyp ;
			this.totalSize = totalsize ;
			this.cnr = cnr ;
			this.id = id ;
		}

		public int getTotalSize() {
			return totalSize;
		}

		public String getCnr() {
			return cnr;
		}

		public Integer getId() {
			return id ;
		}
		
		public String getBelegtyp() {
			return belegtyp ; 
		}
	}

	private class Worker extends SwingWorker<Void, Workerdata> {
		private List<Integer> deliveryIds;
		private String lastBelegtyp ;
		
		public Worker(List<Integer> invoiceIds) {
			this.deliveryIds = invoiceIds ;
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			isRunning = true ;

			console.clearContent();
			try {
				lastBelegtyp = "" ;
				
				for (Integer deliveryId : deliveryIds) {
//					setProgress(++count);

					LieferscheinDto lieferscheinDto = DelegateFactory.getInstance().getLsDelegate().lieferscheinFindByPrimaryKey(deliveryId) ;
					DelegateFactory.getInstance().getLsDelegate().repairLieferscheinSP6999(deliveryId);

					publish(new Workerdata("Lieferschein", deliveryIds.size(), lieferscheinDto.getCNr(), lieferscheinDto.getIId())) ;
					if(cancelled) return null ;
				}

				setProgress(100);
			} catch(Throwable t) {
				console.setVisible(true) ;
				console.getOutputWriter().write(t.getMessage());
			}
			
			return null;
		}	

		@Override
		protected void process(List<Workerdata> chunks) {
			int count = LieferscheinpositionenKorrigieren6999.this.getProgress().getValue() ;
			for (Workerdata workerdata : chunks) {
				setProgressValue(++count) ;
				lblActualInvoice.setText("<html><b>" + workerdata.getCnr() + "</b> (id: " + workerdata.getId() + ") </html>");
				
				if(!lastBelegtyp.equals(workerdata.getBelegtyp())) {
					console.getOutputWriter().println();
					console.getOutputWriter().println(workerdata.getBelegtyp() + ":") ;
					console.getOutputWriter().println("---------------------------------------------") ;
					lastBelegtyp = workerdata.getBelegtyp() ;
				}
				
				console.getOutputWriter().println(workerdata.getCnr()) ;
			}
		}
		
		@Override
		protected void done() {
			isRunning = false;
			startable = true ;

			fireDoneEvent(new EventObject(this)) ;
		}
	}
}
