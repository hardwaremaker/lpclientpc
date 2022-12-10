package com.lp.client.eingangsrechnung;

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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.VendidataImportStats;
import com.lp.server.eingangsrechnung.service.VendidataImporterResult;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBVendidataImportExceptionLP;
import com.lp.util.Helper;

public class DialogVendidataResult extends JDialog implements ActionListener {

	private static final long serialVersionUID = 5976574609926183759L;
	
	private IVendidataImportController<VendidataImporterResult> importController;
	private List<EJBVendidataImportExceptionLP> importErrors;
	private VendidataImportStats importStats;
	
	private WrapperButton wbuImport;
	private WrapperButton wbuCancel;
	private WrapperButton wbuOkay;
	
	private WrapperLabel wlbVerify;
	private WrapperLabel wlbErrorLines;
	private WrapperLabel wlbGoodImports;
	private WrapperLabel wlbErrorImports;
	private WrapperLabel wlbTotalImports;
	
	private JPanel panelImport;
	private JTable tableResults;
	private ImportResultsTableModel tableModel;
	private JScrollPane panelTableResults;

	public DialogVendidataResult(Frame owner, String title, boolean modal, IVendidataImportController<VendidataImporterResult> controller) 
			throws Throwable {
		super(owner, title, modal);
		
		if (null == controller) throw new IllegalArgumentException("controller == null");

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		importController = controller;
		importErrors = new ArrayList<EJBVendidataImportExceptionLP>();
		importStats = new VendidataImportStats();
		
		jbInit();
		pack();
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(this);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				actionVerify();
			}
		});
	}

	private void jbInit() {
		wlbVerify = new WrapperLabel(LPMain.getTextRespectUISPr("er.vendidata.import.pruefen"));
		wlbVerify.setHorizontalAlignment(SwingConstants.LEFT);
		
		initTableResults();
		
		panelImport = new JPanel();
		panelImport.setLayout(new GridBagLayout());
		panelImport.add(wlbVerify,
               new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
                		GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelImport.add(panelTableResults,
				new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
		        		GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(panelImport,
               new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
                		GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		wlbTotalImports = new WrapperLabel();
		wlbTotalImports.setHorizontalAlignment(SwingConstants.LEFT);
		wlbErrorLines = new WrapperLabel();
		wlbErrorLines.setHorizontalAlignment(SwingConstants.LEFT);
		wlbErrorImports = new WrapperLabel();
		wlbErrorImports.setHorizontalAlignment(SwingConstants.LEFT);
		wlbGoodImports = new WrapperLabel();
		wlbGoodImports.setHorizontalAlignment(SwingConstants.LEFT);
		
		JPanel panelInfo = new JPanel();
		panelInfo.setLayout(new GridBagLayout());
		
		panelInfo.add(wlbTotalImports, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelInfo.add(wlbErrorLines, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelInfo.add(wlbGoodImports, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		getContentPane().add(panelInfo,
                new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
                		GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		
		wbuImport = new WrapperButton(LPMain.getTextRespectUISPr("er.vendidata.import.button.importieren"));
		wbuImport.addActionListener(this);
		wbuImport.setVisible(true);
		wbuImport.setEnabled(false);
		
		wbuOkay = new WrapperButton(LPMain.getTextRespectUISPr("button.ok"));
		wbuOkay.addActionListener(this);
		wbuOkay.setVisible(false);
		
		wbuCancel = new WrapperButton(LPMain.getTextRespectUISPr("lp.abbrechen"));
		wbuCancel.addActionListener(this);
		wbuCancel.setVisible(true);
		
		panelButton.add(wbuImport, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
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
		int count = importStats.getTotalImportsAusgangsgutschriften();
		String totalImports = "";
		if (count == 1) {
			totalImports += LPMain.getMessageTextRespectUISPr("er.vendidata.import.singletotalimports", 
							new Object[]{getFilename()});
		} else {
			totalImports += LPMain.getMessageTextRespectUISPr("er.vendidata.import.multipletotalimports", 
							new Object[]{getFilename(), count});
		}
		
		count = importStats.getTotalImportsRechnungen();
		if (count == 1) {
			totalImports += ", " + LPMain.getTextRespectUISPr("rech.vendidata.import.singletotalimports");
		} else {
			totalImports += ", " + LPMain.getMessageTextRespectUISPr("rech.vendidata.import.multipletotalimports", count);
		}
		wlbTotalImports.setText(totalImports);
		
		count = importStats.getErrorCounts();
		if(count > 0) {
			wlbErrorLines.setText(
					LPMain.getMessageTextRespectUISPr("er.vendidata.import.errors", new Object[]{count})) ;
			wlbErrorLines.setForeground(Color.RED) ;
			imported = false;
		} else {
			wlbErrorLines.setText(LPMain.getTextRespectUISPr("er.vendidata.import.noerrors")) ;
			wlbErrorLines.setForeground(new Color(0, 204, 102)) ;
		}
		
		String goodImports = "";
		if (imported) {
			count = importStats.getGoodImportsAusgangsgutschriften();
			if (count == 1) {
				goodImports += LPMain.getTextRespectUISPr("er.vendidata.import.singlesaved");
			} else  {
				goodImports += LPMain.getMessageTextRespectUISPr("er.vendidata.import.mulitplesaved", new Object[]{count});
			}

			count = importStats.getGoodImportsRechnungen();
			if (count == 1) {
				goodImports += ", " + LPMain.getTextRespectUISPr("rech.vendidata.import.singlesaved");
			} else  {
				goodImports += ", " + LPMain.getMessageTextRespectUISPr("rech.vendidata.import.mulitplesaved", new Object[]{count});
			}
		}
		
		wlbGoodImports.setText(goodImports);
	}

	private String getFilename() {
		File file = importController.getImportFile();
		return file == null ? "" : file.getName();
	}

	private void initTableResults() {
		tableModel = new ImportResultsTableModel();
		tableResults = new JTable(tableModel);
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
	
	protected void actionVerify() {
		wlbVerify.setText(LPMain.getTextRespectUISPr("er.vendidata.import.pruefen"));
		invalidate();
		
		try {
			wbuImport.setEnabled(false);
			setImportResult(importController.checkImport());
			wlbVerify.setText(LPMain.getTextRespectUISPr("er.vendidata.import.geprueft")) ;
			setFieldsForStats(false);
			tableModel.fireTableDataChanged();
		} catch (IOException e) {
		} finally {
			wbuImport.setEnabled(importErrors.isEmpty());
		}
	}

	protected void actionImport() {
		wlbVerify.setText(LPMain.getTextRespectUISPr("er.vendidata.import.importieren"));
		invalidate();
		
		try {
			wbuImport.setEnabled(false);
			setImportResult(importController.doImport());
			wlbVerify.setText(LPMain.getTextRespectUISPr("er.vendidata.import.importiert")) ;
			setFieldsForStats(true);
			tableModel.fireTableDataChanged();
		} catch (IOException e) {
		} finally {
			wbuImport.setVisible(false) ;
			wbuCancel.setVisible(false) ;
			wbuOkay.setVisible(true) ;
		}
	}
	
	private void setImportResult(VendidataImporterResult result) {
		if (result == null) return;
		
		importErrors = result.getImportErrors();
		importStats = result.getImportStats();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == wbuImport) {
			actionImport();
		}
		if (source == wbuCancel || source == wbuOkay) {
			setVisible(false);
		}
		
	}

	protected class ImportResultsTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -8577438030256325566L;

		private String[] columnNames;
		private Class[] columnClasses;

		public ImportResultsTableModel() {
			columnNames = new String[] {
					LPMain.getTextRespectUISPr("er.vendidata.import.head.fehlernummer"),
					LPMain.getTextRespectUISPr("er.vendidata.import.head.4vendingid"),
					LPMain.getTextRespectUISPr("er.vendidata.import.head.bezeichnungkunde"),
					LPMain.getTextRespectUISPr("er.vendidata.import.head.fehlerbezeichnung"),
					LPMain.getTextRespectUISPr("er.vendidata.import.head.detail")
			};
			columnClasses = new Class[] {Integer.class, String.class, String.class, String.class, String.class};
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
			return importErrors.size();
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}


		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex >= importErrors.size()) return null;
			
			EJBVendidataImportExceptionLP ex = importErrors.get(rowIndex);
			
			if (0 == columnIndex) return ex.getCode();
			if (1 == columnIndex) return ex.getFourvendingid();
			if (2 == columnIndex) return ex.getBezKunde();
			if (3 == columnIndex) return getFehlerMessage(ex);
			if (4 == columnIndex) {
				Throwable t = ex.getCause();
				return t != null ? t.getMessage() : null;
			}
			
			return null;
		}

		private String getFehlerMessage(EJBVendidataImportExceptionLP ex) {
			if (EJBExceptionLP.FEHLER_ER_IMPORT_FELD_LEER == ex.getCode()) {
				List<Object> msgs = ex.getAlInfoForTheClient();
				if (msgs != null) {
					return "Feld " + msgs.get(0) + " ist in der XML-Datei nicht besetzt."; 
				}
			}
			if (EJBExceptionLP.FEHLER_ER_IMPORT_FREMDSYSTEMNUMMER_NICHT_GEFUNDEN == ex.getCode() 
					|| EJBExceptionLP.FEHLER_ER_IMPORT_FREMDSYSTEMNUMMER_NICHT_EINDEUTIG == ex.getCode()) {
				return LPMain.getMessageTextRespectUISPr("er.vendidata.import.error." + ex.getCode(), 
						LPMain.getTextRespectUISPr("part.kund.fremdsystemnr"));
			}
			
			if (EJBExceptionLP.FEHLER_ER_IMPORT_UNMARSHAL_FEHLGESCHLAGEN == ex.getCode() 
					|| EJBExceptionLP.FEHLER_ER_IMPORT_WARENKONTO_NICHT_DEFINIERT == ex.getCode() 
					|| EJBExceptionLP.FEHLER_ER_IMPORT_KOSTENSTELLE_NICHT_DEFINIERT == ex.getCode()
					|| EJBExceptionLP.FEHLER_ER_IMPORT_MWSTSATZ_NICHT_DEFINIERT == ex.getCode()) {
				return LPMain.getTextRespectUISPr("er.vendidata.import.error." + ex.getCode());
			}
			
			if (EJBExceptionLP.FEHLER_ER_IMPORT_UEBERSTEUERTER_MWSTSATZ_NICHT_GEFUNDEN == ex.getCode()) {
				return LPMain.getMessageTextRespectUISPr("er.vendidata.import.error.uebersteuertermwstsatznichtgefunden", 
						Helper.formatZahl(new BigDecimal(ex.getUebersteuerterSteuersatz()), LPMain.getInstance().getUISprLocale()));
			}
			return null;
		}

		
	}
}
