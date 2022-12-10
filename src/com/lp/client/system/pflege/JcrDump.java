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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.List;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.system.jcr.service.JcrDumpResult;
import com.lp.server.system.jcr.service.JcrScanResult;

public class JcrDump extends PflegefunktionSupportsProgressBar {
	
	private PanelConsole console;
	private boolean startable = false;
	private boolean isRunning = false;
	private boolean cancelled = false;
	private JPanel panel;
	
	private JLabel lblTotalCount ;
	private JLabel lblActualInvoice ;

	private BoundedRangeModel brmProgress;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private int retryCount;
	private int totalCount;
	private long totalBytes;
	
	@Override
	public String getKategorie() {
		return KATEGORIE_DOKUMENTENABLAGE ;
	}

	@Override
	public String getName() {
		return "Dokumentenablage Dump";
	}

	@Override
	public String getBeschreibung() {
		return "<html>" +
				"Die Dokumentendatenbank auslesen und in ein neues Ziel schreiben" +
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
			
			lblTotalCount.setText("Dokumentenablage scannen...");
			brmProgress = new DefaultBoundedRangeModel(0, 0, 0, 100);
			JcrDump.this.setProgress(brmProgress);
			
			SwingWorker<Void, JcrDumpResultEx> worker = new Worker();
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

	private class Worker extends SwingWorker<Void, JcrDumpResultEx> {		
		public Worker() {
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			isRunning = true ;

			try {
/*
 2018-03-20 11:10:45,795 WARN  [com.lp.server.system.jcr.ejbfac.JcrDmsDumpVisitorFacBean] |1|  S-Nodeinfo: [type=3, sPath=/HELIUMV/002/Lieferschein/Lieferschein/33237, timestamp=-1, filename=(nofilename), mime=(nomime), data=false].

				
 */
	
//				JcrDumpResult dr =
//						DelegateFactory.getInstance()
//							.getJcrDumpDelegate().dumpPath("HELIUMV/001/Eingangsrechng/Eingangsrechng/37073/ER/");
//				if(dr != null) {
//					publish(dr);
//				}				

//				JcrDumpResult dr = DelegateFactory.getInstance()
//							.getJcrDumpDelegate().dumpPath("HELIUMV/001/Eingangsrechng/Eingangsrechng/47986/ER/");
//				if(dr != null) {
//					publish(dr);
//				}				
				
				setProgress(2);
				JcrScanResult scanResult = DelegateFactory.getInstance().getJcrDumpDelegate().scan();
				if(scanResult.getRootPaths() == null) {
					setProgress(100);
				} else {
					brmProgress.setMaximum(scanResult.getDataNodeCount());
					totalCount = scanResult.getDataNodeCount();
					lblTotalCount.setText(new Integer(totalCount).toString());
					retryCount = 0;
					totalBytes = 0l;
					
					for (String rootPath : scanResult.getRootPaths()) {
						if(cancelled) {
							console.getOutputWriter().println(sdf.format(new Date()) +
									" Verarbeitung durch Anwender abgebrochen.");
							break;
						}
						
//						lblActualInvoice.setText(rootPath);
						JcrDumpResult dumpProgress = new JcrDumpResult(rootPath);
						dumpProgress.setDataByteCount(-1l);
						publish(new JcrDumpResultEx(dumpProgress));

//						String retryText = retryCount == 0 ? "" 
//								: (" (" + new Integer(retryCount).toString() + " Wiederholungen)");
//						lblActualInvoice.setText(rootPath);
//						lblTotalCount.setText(
//								new Integer(brmProgress.getValue()).toString() + " von " +
//								new Integer(scanResult.getDataNodeCount()).toString() +
//								retryText);
						try {
							JcrDumpResult dumpResult =
									DelegateFactory.getInstance()
										.getJcrDumpDelegate().dumpPath(rootPath);
							if(dumpResult != null) {
								publish(new JcrDumpResultEx(dumpResult));
							}			
						} catch(Exception e) {
							JcrDumpResult dumpResult =
									DelegateFactory.getInstance()
										.getJcrDumpDelegate().dumpPath(rootPath);
							if(dumpResult != null) {
								publish(new JcrDumpResultEx(dumpResult, true));
							}										
						}
					}
					
					publish(new JcrDumpResultEx(brmProgress.getMaximum(), brmProgress.getValue()));

//					console.getOutputWriter().println(sdf.format(new Date()) +
//							" Erwartet: " + 
//							brmProgress.getMaximum() + ", verarbeitet: " +
//							brmProgress.getValue() + ".");
//					console.getOutputWriter().println("------------------------------------------");
					setProgress(100);
				}
			} catch(Throwable t) {
				console.setVisible(true) ;
				t.printStackTrace(console.getOutputWriter());
			}
			
			return null;
		}	

		@Override
		protected void process(List<JcrDumpResultEx> chunks) {
			for (JcrDumpResultEx workerdata : chunks) {
				if(workerdata.isDump()) {
					if(workerdata.getDataByteCount() < 0l) {
						lblActualInvoice.setText(workerdata.getPath());
						continue;
					}
					
					setProgressValue(brmProgress.getValue() + workerdata.getDataNodeCount());
					String suffix = ".";
					if(workerdata.isError()) {
						suffix = " -> FEHLERHAFT!";
					}
					if(workerdata.hasRetry()) {
						++retryCount;
					}
					String retryText = retryCount == 0 ? "" 
							: (" (" + new Integer(retryCount).toString() + " Wiederholungen)");
					lblActualInvoice.setText(workerdata.getPath());
					
					totalBytes += workerdata.getDataByteCount();
					String totalBytesInfo = dataBytesInfo(totalBytes);
					
					lblTotalCount.setText(
							new Integer(brmProgress.getValue()).toString() + " von " +
							new Integer(totalCount).toString() +
							totalBytesInfo + 
							retryText);
					
					String sizeInfo = dataBytesInfo(workerdata.getDataByteCount());
					console.getOutputWriter().println(
							sdf.format(new Date()) +
							" " + workerdata.getPath() + 
							", Dokumente: " + workerdata.getDataNodeCount() + 
							sizeInfo + 
							", Pfade: " + workerdata.getPathNodeCount() + 
							", Fehler: " + workerdata.getErrorNodeCount() +
							suffix) ;
				} else {
					console.getOutputWriter().println("------------------------------------------");
					console.getOutputWriter().println(sdf.format(new Date()) +
							" Erwartet: " + workerdata.getMaximum() +
							", verarbeitet: " + workerdata.getValue() + ".");
					console.getOutputWriter().println("------------------------------------------");
				}
			}
		}
		
		private String dataBytesInfo(long databytes) {
			if(databytes == 0l) return "";
			
			if(databytes > (1024l*1024l*1024l)) {
				return " (" + (databytes / (1024l*1024l*1024l)) + "GB)";
			}

			if(databytes > (1024l*1024l)) {
				return " (" + (databytes / (1024l*1024l)) + "MB)";
			}
			return " (" + (databytes / 1024l) + "kB)";
		}
		
		@Override
		protected void done() {
			isRunning = false;
			startable = true ;

			fireDoneEvent(new EventObject(this)) ;
		}
	}
	
	private class Worker0 extends SwingWorker<Void, JcrDumpResult> {		
		public Worker0() {
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			isRunning = true ;

			try {
				setProgress(20);
				publish(new JcrDumpResult()) ;
				JcrDumpResult result = DelegateFactory.getInstance().getJcrDumpDelegate().dump() ;
				publish(result) ;
				setProgress(100);
				
			} catch(Throwable t) {
				console.setVisible(true) ;
				console.getOutputWriter().write(t.getMessage());
			}
			
			return null;
		}	

		@Override
		protected void process(List<JcrDumpResult> chunks) {
			int count = JcrDump.this.getProgress().getValue() ;
			for (JcrDumpResult workerdata : chunks) {
				setProgressValue(++count) ;
				if(workerdata.getPathNodeCount() == 0) {
					console.getOutputWriter().println("Bearbeitung laeuft...") ;
					continue ;
				}
				
				console.getOutputWriter().println(
						"Pfade: " + workerdata.getPathNodeCount() + 
						", Dokumente: " + workerdata.getDataNodeCount() + 
						", Fehler: " + workerdata.getErrorNodeCount()) ;
//				for (String nodePath : workerdata.getDefectNodes()) {
//					console.getOutputWriter().println("- " + nodePath);
//				}
				console.getOutputWriter().println("------------------------------------------");
			}
		}
		
		@Override
		protected void done() {
			isRunning = false;
			startable = true ;

			fireDoneEvent(new EventObject(this)) ;
		}
	}
	
	private class JcrDumpResultEx extends JcrDumpResult {
		private boolean retry;
		private JcrDumpResult result;
		private int maximum;
		private int value;
		private boolean isDump;
		
		public JcrDumpResultEx(JcrDumpResult result) {
			this(result, false);
		}
		
		public JcrDumpResultEx(JcrDumpResult result, boolean retry) {
			this.result = result;
			this.retry = retry;
			this.isDump = true;
		}
		
		public JcrDumpResultEx(int maximum, int value) {
			this.maximum = maximum;
			this.value = value;
			this.isDump = false;
		}
		
		public boolean isDump() {
			return isDump;
		}
		
		public int getMaximum() {
			return maximum;
		}
		
		public int getValue() {
			return value;
		}
		
		public boolean hasRetry() {
			return retry;
		}

		@Override
		public int getPathNodeCount() {
			return result.getPathNodeCount();
		}

		@Override
		public int getDataNodeCount() {
			return result.getDataNodeCount();
		}

		@Override
		public int getErrorNodeCount() {
			return result.getErrorNodeCount();
		}

		@Override
		public long getDataByteCount() {
			return result.getDataByteCount();
		}
		
		@Override
		public String getPath() {
			return result.getPath();
		}

		@Override
		public boolean isError() {
			return result.isError();
		}
	}
}
