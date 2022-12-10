package com.lp.client.artikel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.open.WrapperFile;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.VendidataExportStats;
import com.lp.server.fertigung.service.VendidataArticleExportResult;
import com.lp.server.util.HvOptional;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBVendidataArticleExceptionLP;

public class Dialog4VendingArtikelExport extends JDialog implements ActionListener {

	private static final long serialVersionUID = 19248605512345968L;
	
	private IVendidataExportController<VendidataArticleExportResult> controller;
	private List<EJBVendidataArticleExceptionLP> exportErrors;
	private VendidataExportStats exportStats;
	
	private WrapperButton wbuExport;
	private WrapperButton wbuCancel;
	private WrapperButton wbuOkay;
	
	private WrapperLabel wlaVerify;
	private WrapperLabel wlaTotalExports;
	private WrapperLabel wlaErrorCounts;
	private WrapperLabel wlaGoodImports;

	private JPanel panelExport;
	private JTable tableResults;
	private JScrollPane panelTableResults;

	public Dialog4VendingArtikelExport(Frame owner, String title, boolean modal, 
			IVendidataExportController<VendidataArticleExportResult> controller) throws Throwable {
		super(owner, title, modal);
		if (null == controller) throw new IllegalArgumentException("controller == null");
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.controller = controller;
		this.exportErrors = new ArrayList<EJBVendidataArticleExceptionLP>();
		this.exportStats = new VendidataExportStats();

		jbInit();
		pack();
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(this);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				actionVerify();
				invalidate();
			}
		});
	}

	private void jbInit() {
		wlaVerify = new WrapperLabel(LPMain.getTextRespectUISPr("er.vendidata.import.pruefen"));
		wlaVerify.setHorizontalAlignment(SwingConstants.LEFT);
		
		initTableResults();
		panelExport = new JPanel();
		panelExport.setLayout(new GridBagLayout());
		panelExport.add(wlaVerify,
               new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
                		GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelExport.add(panelTableResults,
				new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
		        		GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(panelExport,
               new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
                		GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		wlaTotalExports = new WrapperLabel();
		wlaTotalExports.setHorizontalAlignment(SwingConstants.LEFT);
		wlaErrorCounts = new WrapperLabel();
		wlaErrorCounts.setHorizontalAlignment(SwingConstants.LEFT);
		wlaGoodImports = new WrapperLabel();
		wlaGoodImports.setHorizontalAlignment(SwingConstants.LEFT);
		
		JPanel panelInfo = new JPanel();
		panelInfo.setLayout(new GridBagLayout());
		
		panelInfo.add(wlaTotalExports, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelInfo.add(wlaErrorCounts, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelInfo.add(wlaGoodImports, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		getContentPane().add(panelInfo,
                new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
                		GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		
		wbuExport = new WrapperButton(LPMain.getTextRespectUISPr("artikel.vendidata.export.exportieren"));
		wbuExport.addActionListener(this);
		wbuExport.setVisible(true);
		wbuExport.setEnabled(false);
		
		wbuOkay = new WrapperButton(LPMain.getTextRespectUISPr("button.ok"));
		wbuOkay.addActionListener(this);
		wbuOkay.setVisible(false);
		
		wbuCancel = new WrapperButton(LPMain.getTextRespectUISPr("lp.abbrechen"));
		wbuCancel.addActionListener(this);
		wbuCancel.setVisible(true);
		
		panelButton.add(wbuExport, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		panelButton.add(wbuOkay, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		panelButton.add(wbuCancel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		
		getContentPane().add(panelButton,
                new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
                		GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	private void setFieldsForStats(boolean imported) {
		Integer count = exportStats.getTotalExports();
		if (count == 1) {
			wlaTotalExports.setText(
					LPMain.getMessageTextRespectUISPr("artikel.vendidata.export.singletotalimports", 
							new Object[]{getFilename()}));
		} else {
			wlaTotalExports.setText(
					LPMain.getMessageTextRespectUISPr("artikel.vendidata.export.multipletotalimports", 
							new Object[]{getFilename(), count}));
		}
		
		count = exportStats.getErrorCounts();
		if(count > 0) {
			wlaErrorCounts.setText(
					LPMain.getMessageTextRespectUISPr("artikel.vendidata.export.errors", new Object[]{count})) ;
			wlaErrorCounts.setForeground(Color.RED) ;
		} else {
			wlaErrorCounts.setText(LPMain.getTextRespectUISPr("artikel.vendidata.export.noerrors")) ;
			wlaErrorCounts.setForeground(new Color(0, 204, 102)) ;
		}
		
		count = exportStats.getTotalExports();
		if (imported) {
			if (count == 1) {
				wlaGoodImports.setText(LPMain.getTextRespectUISPr("artikel.vendidata.export.singlesaved"));
			} else  {
				wlaGoodImports.setText(LPMain.getMessageTextRespectUISPr("artikel.vendidata.export.mulitplesaved", new Object[]{count}));
			}
		} else {
			wlaGoodImports.setText("");
		}
	}

	private String getFilename() {
		File file = controller.getFile();
		return file == null ? "" : file.getName();
	}

	private void initTableResults() {
		tableResults = new JTable(new ExportResultsTableModel());
		//tableResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setPreferredColumnWidth(tableResults, new Integer[]{100, 100, 350, 250, 200});
		
		panelTableResults = new JScrollPane(tableResults);
		panelTableResults.setPreferredSize(new Dimension(1000, 250));
	}

	private void setPreferredColumnWidth(JTable table, Integer[] preferredSizes) {
		for (int i = 0; i < preferredSizes.length; i++) {
			TableColumn col = table.getColumnModel().getColumn(i);
			col.setPreferredWidth(preferredSizes[i]);
		}
	}

	private void actionVerify() {
		wlaVerify = new WrapperLabel(LPMain.getTextRespectUISPr("er.vendidata.import.pruefen"));
		invalidate();
		
		wbuExport.setEnabled(false);
		setExportResult(controller.checkExport());
		wlaVerify.setText(LPMain.getTextRespectUISPr("er.vendidata.import.geprueft")) ;
		setFieldsForStats(false);

		wbuExport.setEnabled(exportErrors.isEmpty());
	}
	
	private void actionExport() {
		chooseFile();
		wlaVerify = new WrapperLabel(LPMain.getTextRespectUISPr("artikel.vendidata.export.exportlaeuft"));
		invalidate();

		try {
			wbuExport.setEnabled(false);
			setExportResult(controller.doExport());
			wlaVerify.setText(LPMain.getTextRespectUISPr("artikel.vendidata.export.exportiert")) ;
			setFieldsForStats(true);
		} catch (IOException e) {
		} finally {
			wbuExport.setVisible(false);
			wbuCancel.setVisible(false);
			wbuOkay.setVisible(true);
		}
		
	}

	private void setExportResult(VendidataArticleExportResult result) {
		if (result == null) return;
		
		exportErrors = result.getExportErrors();
		exportStats = result.getStats();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == wbuExport) {
			actionExport();
		}
		if (source == wbuCancel || source == wbuOkay) {
			setVisible(false);
		}
	}
	
	private void chooseFile() {
		try {
			HvOptional<WrapperFile> wf = HelperClient.showSaveDialog(
					this, FileChooserConfigToken.Export4Vending,
					new File("artikel_4vd.xml"));
			if (wf.isPresent()) {
				controller.setFile(wf.get().getFile());				
			}
		} catch (Exception e) {
			// ignored (ghp, 19.11.2020)
		}
		
		// TODO controller crashed mit nullpointer wenn keine Datei gewaehlt wurde
	}

	protected class ExportResultsTableModel implements TableModel {
		
		private String[] columnNames;
		private Class[] columnClasses;

		public ExportResultsTableModel() {
			columnNames = new String[] {
					LPMain.getTextRespectUISPr("artikel.vendidata.export.head.fehlernummer"),
					LPMain.getTextRespectUISPr("artikel.vendidata.export.head.fourvendingid"),
					LPMain.getTextRespectUISPr("artikel.vendidata.export.head.artikelcnr"),
					LPMain.getTextRespectUISPr("artikel.vendidata.export.head.fehlerbezeichnung"),
					LPMain.getTextRespectUISPr("artikel.vendidata.export.head.detail")
			};
			columnClasses = new Class[] {Integer.class, Integer.class, String.class,String.class, String.class};
		}
		
		@Override
		public void addTableModelListener(TableModelListener l) {
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return columnClasses[columnIndex];
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return columnNames[columnIndex];
		}

		@Override
		public int getRowCount() {
			return exportErrors.size();
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex >= exportErrors.size()) return null;
			
			EJBVendidataArticleExceptionLP ex = exportErrors.get(rowIndex);
			
			if (0 == columnIndex) return ex.getCode();
			if (1 == columnIndex) return ex.getFourVendingId();
			if (2 == columnIndex) return ex.getFourVendingName();
			if (3 == columnIndex) return getFehlerMessage(ex);
			if (4 == columnIndex) {
				Throwable t = ex.getCause();
				return t != null ? t.getMessage() : null;
			}
			
			return null;
		}

		private String getFehlerMessage(EJBVendidataArticleExceptionLP ex) {
//			if (EJBExceptionLP.FEHLER_ER_IMPORT_FELD_LEER == ex.getCode()) {
//				ArrayList<Object> msgs = ex.getAlInfoForTheClient();
//				if (msgs != null) {
//					return "Feld " + msgs.get(0) + " ist in der XML-Datei nicht besetzt."; 
//				}
//			}
			if (EJBExceptionLP.FEHLER_EXPORT_4VENDING_KEINE_AKTIVE_PREISLISTE_VORHANDEN == ex.getCode() 
					|| EJBExceptionLP.FEHLER_EXPORT_4VENDING_KEIN_AKTUELL_GUELTIGER_VKPREIS_VORHANDEN == ex.getCode() 
					|| EJBExceptionLP.FEHLER_EXPORT_4VENDING_KEIN_ARTIKEL_MWST_SATZ_DEFINIERT == ex.getCode() 
					|| EJBExceptionLP.FEHLER_EXPORT_4VENDING_KEIN_LIEF1PREIS_VORHANDEN == ex.getCode() 
					|| EJBExceptionLP.FEHLER_EXPORT_4VENDING_ARTIKELGRUPPE_NICHT_GEFUNDEN == ex.getCode()) {
				return LPMain.getTextRespectUISPr("artikel.vendidata.export.error." + ex.getCode());
			}
			
			return ex.getMessage();
		}

		
	}
}
