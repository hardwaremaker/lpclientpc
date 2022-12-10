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
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.system.service.MandantDto;
import com.lp.util.EJBExceptionLP;

public class ZwsMinusRabatteKorrigieren5524 extends PflegefunktionSupportsProgressBar {
	
	private PanelConsole console;
	private boolean startable = false;
	private boolean isRunning = false;
	private boolean cancelled = false;
	private JPanel panel;
	
	private JLabel lblTotalCount ;
	private JLabel lblActualInvoice ;

	@Override
	public String getKategorie() {
		return KATEGORIE_AUFTRAG;
	}

	@Override
	public String getName() {
		return "Int. Zwischensumme 5524";
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
				"In Angebots- und Auftragpositionen die die Positionsart 'Intelligente Zwischensumme' enthalten, " +
				"die Statistikfelder um die Rabatte korrigieren." +
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

	private String pluralize(int count, String singular, String plural) {
		if (count == 0) return "keine " + plural;
		if (count == 1) return "1 " + singular;
		
		return "" + count + " " + plural;
	}
	
	@Override
	public void run() {
		cancelled = false;
		startable = false ;
	
		try {
			isRunning = true ;
			
			lblTotalCount.setText("ermitteln...");
			List<Integer> allOffers = DelegateFactory.getInstance().getAngebotDelegate().repairAngebotZws5524GetList() ;
			List<Integer> allOrders = DelegateFactory.getInstance().getAuftragDelegate().repairAuftragZws5524GetList() ;
			ZwsMinusRabatteKorrigieren5524.this.setProgress(new DefaultBoundedRangeModel(0, 0, 0, allOffers.size() + allOrders.size()));
			
			String s = pluralize(allOffers.size(), "Angebot", "Angebote") + " und " + pluralize(allOrders.size(), "Auftrag", "Auftr\u00e4ge") ;
			lblTotalCount.setText("<html><b>" + s + "</b></html>");			

			if(allOffers.size() > 0 || allOrders.size() > 0) {
				lblActualInvoice.setText("berechnen...") ;
				SwingWorker<Void, Workerdata> worker = new Worker(allOffers, allOrders);
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
		private List<Integer> offerIds ;
		private List<Integer> orderIds ;
		private String lastBelegtyp ;
		
		public Worker(List<Integer> offerIds, List<Integer> orderIds) {
			this.offerIds = offerIds ;
			this.orderIds = orderIds ;
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			isRunning = true ;

			try {
				int count = 0 ;	
				lastBelegtyp = "" ;
				
				for (Integer offerId : offerIds) {
					setProgress(++count);

					AngebotDto angebotDto = DelegateFactory.getInstance().getAngebotDelegate().angebotFindByPrimaryKey(offerId);
					DelegateFactory.getInstance().getAngebotDelegate().repairAngebotZws5524(offerId);

					publish(new Workerdata("Angebot", offerIds.size(), angebotDto.getCNr(), angebotDto.getIId()));
					if(cancelled) return null ;
				}

				for(Integer orderId : orderIds) {
					setProgress(++count);

					AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate().auftragFindByPrimaryKey(orderId) ;
					DelegateFactory.getInstance().getAuftragDelegate().repairAuftragZws5524(orderId) ;
					publish(new Workerdata("Auftrag", orderIds.size(), auftragDto.getCNr(), auftragDto.getIId())) ;					
				}
				setProgress(100);
			} catch(Throwable t) {
				setProgress(100);
				console.setVisible(true) ;
				t.printStackTrace(console.getOutputWriter());				
				throw new Exception(t);
			}
			
			return null;
		}	

		@Override
		protected void process(List<Workerdata> chunks) {
			int count = ZwsMinusRabatteKorrigieren5524.this.getProgress().getValue() ;
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
