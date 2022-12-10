package com.lp.client.fertigung;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

import com.lp.client.eingangsrechnung.IVendidataImportController;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.pc.LPMain;
import com.lp.client.remote.PayloadWorker;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.eingangsrechnung.service.VendidataImportStats;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.VendidataArticleConsumptionImportResult;
import com.lp.server.system.service.ImportProgressDto;
import com.lp.server.system.service.PayloadDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBVendidataArticleExceptionLP;

public class DialogVendidataArticleConsumptionResult extends JDialog implements ActionListener {

	private static final long serialVersionUID = -7414437746467580682L;
	
	private IVendidataImportController<VendidataArticleConsumptionImportResult> importController;
	private VendidataArticleConsumptionImportResult importResult;
	
	private WrapperButton wbuImport;
	private WrapperButton wbuCancel;
	private WrapperButton wbuOkay;
	
	private WrapperLabel wlaVerify;
	private WrapperLabel wlaErrorLines;
	private WrapperLabel wlaTotalImports;
	private WrapperLabel wlaGoodImports;

	private JPanel panelImport;
	private JTable tableResults;
	private JScrollPane panelTableResults;
	private JProgressBar progressBar;

	public DialogVendidataArticleConsumptionResult(Frame owner, String title, boolean modal, 
			IVendidataImportController<VendidataArticleConsumptionImportResult> controller) {
		super(owner, title, modal);
		if (null == controller) throw new IllegalArgumentException("controller == null");
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		importController = controller;
		
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
		wlaVerify = new WrapperLabel(LPMain.getTextRespectUISPr("er.vendidata.import.pruefen"));
		wlaVerify.setHorizontalAlignment(SwingConstants.LEFT);
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setVisible(false);
		
		initTableResults();
		panelImport = new JPanel();
		panelImport.setLayout(new MigLayout("wrap 1, hidemode 0", "[fill, grow]", "[fill|fill|fill,grow]"));
		panelImport.add(wlaVerify);
		panelImport.add(progressBar);
		panelImport.add(panelTableResults);
		
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(panelImport,
               new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
                		GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		wlaTotalImports = new WrapperLabel();
		wlaTotalImports.setHorizontalAlignment(SwingConstants.LEFT);
		wlaErrorLines = new WrapperLabel();
		wlaErrorLines.setHorizontalAlignment(SwingConstants.LEFT);
		wlaGoodImports = new WrapperLabel();
		wlaGoodImports.setHorizontalAlignment(SwingConstants.LEFT);

		JPanel panelInfo = new JPanel();
		panelInfo.setLayout(new GridBagLayout());
		
		panelInfo.add(wlaTotalImports, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelInfo.add(wlaErrorLines, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
        		GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelInfo.add(wlaGoodImports, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, 
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
		Integer count = getImportStats().getGoodImportsAusgangsgutschriften();
		if (count == 1) {
			wlaTotalImports.setText(
					LPMain.getMessageTextRespectUISPr("fert.vendidata.import.singletotalimports", 
							new Object[]{getFilename()}));
		} else {
			wlaTotalImports.setText(
					LPMain.getMessageTextRespectUISPr("fert.vendidata.import.multipletotalimports", 
							new Object[]{getFilename(), count}));
		}
		
		count = getImportStats().getErrorCounts();
		String errorLinesMessage;
		if(count > 0) {
			errorLinesMessage = LPMain.getMessageTextRespectUISPr("fert.vendidata.import.errors", new Object[]{count});
			wlaErrorLines.setForeground(Color.RED) ;
		} else {
			errorLinesMessage = LPMain.getTextRespectUISPr("fert.vendidata.import.noerrors");
			wlaErrorLines.setForeground(new Color(0, 204, 102)) ;
		}
		count = getImportStats().getWarningCounts();
		if (count == 1) {
			errorLinesMessage += " " + LPMain.getTextRespectUISPr("fert.vendidata.import.singlewarnings");
			wlaErrorLines.setForeground(Color.RED) ;
		} else if (count > 1) {
			errorLinesMessage += " " + LPMain.getMessageTextRespectUISPr("fert.vendidata.import.multiplewarnings", new Object[]{count});
			wlaErrorLines.setForeground(Color.RED) ;
		}
		wlaErrorLines.setText(errorLinesMessage);
		
		count = getImportStats().getGoodImportsAusgangsgutschriften();
		String goodImportsMessage;
		if (imported) {
			if (count == 1) {
				goodImportsMessage = LPMain.getTextRespectUISPr("fert.vendidata.import.singlesaved");
			} else  {
				goodImportsMessage = LPMain.getMessageTextRespectUISPr("fert.vendidata.import.mulitplesaved", new Object[]{count});
			}
			wlaGoodImports.setText(goodImportsMessage + " "
					+ LPMain.getMessageTextRespectUISPr("fert.vendidata.import.affectedlose", new Object[]{getAffectedLose()}) + ". "
					+ LPMain.getMessageTextRespectUISPr("fert.vendidata.import.affectedtouren", new Object[]{getAffectedTouren()}) + ". ");
		} else {
			wlaGoodImports.setText("");
		}
	}

	private String getAffectedLose() {
		StringBuilder builder = new StringBuilder();
		Iterator<Entry<Integer, LosDto>> iter = getImportResult().getUsedLosDtos().entrySet().iterator();
		while (iter.hasNext()) {
			builder.append(iter.next().getValue().getCNr());
			if (iter.hasNext()) {
				builder.append(", ");
			}
		}
		
		return builder.toString();
	}
	
	private String getAffectedTouren() {
		StringBuilder builder = new StringBuilder();
		Iterator<Integer> iter = getImportResult().getIncludedTours().iterator();
		while (iter.hasNext()) {
			builder.append(iter.next());
			if (iter.hasNext()) {
				builder.append(", ");
			}
		}
		
		return builder.toString();
	}

	private String getFilename() {
		File file = importController.getImportFile();
		return file == null ? "" : file.getName();
	}

	private void initTableResults() {
		tableResults = new JTable(new ImportResultsTableModel());
		//tableResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setPreferredColumnWidth(tableResults, new Integer[]{10, 60, 60, 180, 60, 410, 420});
		
		panelTableResults = new JScrollPane(tableResults);
		panelTableResults.setPreferredSize(new Dimension(1200, 400));
	}
	
	private void setPreferredColumnWidth(JTable table, Integer[] preferredSizes) {
		for (int i = 0; i < preferredSizes.length; i++) {
			TableColumn col = table.getColumnModel().getColumn(i);
			col.setPreferredWidth(preferredSizes[i]);
		}
	}

	protected void actionVerify() {
		VerifyTask verifyTask = new VerifyTask();
		verifyTask.execute();
	}

	protected void actionImport() {
		ImportTask importTask = new ImportTask();
		importTask.execute();
	}
	
	private void setImportResult(VendidataArticleConsumptionImportResult result) {
		this.importResult = result;
		((AbstractTableModel)tableResults.getModel()).fireTableDataChanged(); 
	}
	
	private VendidataArticleConsumptionImportResult getImportResult() {
		return importResult == null ? new VendidataArticleConsumptionImportResult() : importResult;
	}
	
	private List<EJBVendidataArticleExceptionLP> getImportErrors() {
		return getImportResult().getImportErrors();
	}
	
	private VendidataImportStats getImportStats() {
		return getImportResult().getStats();
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
	
	/**
	 * Startet den Import in einem eigenen Thread und steuert die Progressbar
	 * 
	 * @author andi
	 *
	 */
	protected class ImportTask extends PayloadWorker<VendidataArticleConsumptionImportResult> {

		@Override
		protected VendidataArticleConsumptionImportResult doInBackground() throws Exception {
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			wlaVerify.setText(LPMain.getTextRespectUISPr("er.vendidata.import.importieren"));
			wbuImport.setEnabled(false);
			wbuCancel.setEnabled(false);
			progressBar.setValue(0);
			progressBar.setVisible(true);
			return importController.doImport(this);
		}
		
		@Override
		public void publishPayload(PayloadDto payload) {
			publish(payload);
		}

		@Override
		protected void done() {
			try {
				progressBar.setVisible(false);
				setImportResult(get());
				wlaVerify.setText(LPMain.getTextRespectUISPr("er.vendidata.import.importiert"));
				setFieldsForStats(true);
				wbuImport.setVisible(false);
				wbuCancel.setVisible(false);
				wbuOkay.setVisible(true);
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			} catch (InterruptedException e) {
				LpLogger.getInstance(this.getClass()).error("InterruptedException during 4Vending ImportTask", e);
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			}
		}

		@Override
		protected void process(List<PayloadDto> payloads) {
			PayloadDto payload = payloads.get(payloads.size()-1);
			ImportProgressDto importProgress = (ImportProgressDto) payload.getPayload();
			progressBar.setValue(importProgress.getProgress());
		}

	}
	
	/**
	 * Startet den Ueberpruefung des Imports in einem eigenen Thread und steuert die Progressbar
	 * 
	 * @author andi
	 *
	 */
	protected class VerifyTask extends PayloadWorker<VendidataArticleConsumptionImportResult> {
		
		@Override
		protected VendidataArticleConsumptionImportResult doInBackground() throws Exception {
			wlaVerify.setText(LPMain.getTextRespectUISPr("er.vendidata.import.pruefen"));
			wbuImport.setEnabled(false);
			progressBar.setVisible(true);

			return importController.checkImport(this);
		}
		
		@Override
		public void publishPayload(PayloadDto payload) {
			publish(payload);
		}

		@Override
		protected void done() {
			try {
				progressBar.setVisible(false);
				setImportResult(get());
				wlaVerify.setText(LPMain.getTextRespectUISPr("er.vendidata.import.geprueft")) ;
				setFieldsForStats(false);
				wbuImport.setEnabled(getImportErrors().isEmpty());
			} catch (InterruptedException e) {
				LpLogger.getInstance(this.getClass()).error("InterruptedException during 4Vending VerifyTask", e);
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void process(List<PayloadDto> payloads) {
			PayloadDto payload = payloads.get(payloads.size()-1);
			ImportProgressDto importProgress = (ImportProgressDto) payload.getPayload();
			System.out.println("Process new payloads! Maximum = " + importProgress.getMaximum() + ", Current = " + importProgress.getCurrent());
			progressBar.setValue(importProgress.getProgress());
		}
	}

	protected class ImportResultsTableModel extends AbstractTableModel {
		
		private static final long serialVersionUID = 3580086499855216968L;

		private String[] columnNames;
		private Class[] columnClasses;

		public ImportResultsTableModel() {
			columnNames = new String[] {
					"",
					LPMain.getTextRespectUISPr("fert.vendidata.import.head.fehlernummer"),
					LPMain.getTextRespectUISPr("fert.vendidata.import.head.fourvendingid"),
					LPMain.getTextRespectUISPr("fert.vendidata.import.head.fourvendingname"),
					LPMain.getTextRespectUISPr("fert.vendidata.import.head.tournummer"),
					LPMain.getTextRespectUISPr("fert.vendidata.import.head.fehlerbezeichnung"),
					LPMain.getTextRespectUISPr("fert.vendidata.import.head.detail")
			};
			columnClasses = new Class[] {String.class, Integer.class, Integer.class, String.class, Integer.class, String.class, String.class};
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
			return getImportErrors().size();
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex >= getImportErrors().size()) return null;
			
			EJBVendidataArticleExceptionLP ex = getImportErrors().get(rowIndex);
			
			if (0 == columnIndex) return getSeverityText(ex);
			if (1 == columnIndex) return ex.getCode();
			if (2 == columnIndex) return ex.getFourVendingId();
			if (3 == columnIndex) return ex.getFourVendingName();
			if (4 == columnIndex) return ex.getTourNr();
			if (5 == columnIndex) return getFehlerMessage(ex);
			if (6 == columnIndex) {
				Throwable t = ex.getCause();
				return t != null ? t.getMessage() : null;
			}
			
			return null;
		}

		private String getFehlerMessage(EJBVendidataArticleExceptionLP ex) {
			if (EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_4VENDING_ID_NICHT_VORHANDEN == ex.getCode() 
					|| EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_BASEARTIKELID_NICHT_VORHANDEN == ex.getCode() 
					|| EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_TOURLAGER_NICHT_VORHANDEN == ex.getCode() 
					|| EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_KEINE_FERTIGUNGSGRUPPE_VORHANDEN == ex.getCode() 
					|| EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_KEINE_MONTAGEART_VORHANDEN == ex.getCode() 
					|| EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_KEINE_KOSTENSTELLE_DEFINIERT == ex.getCode() 
					|| EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_LOS_ANLEGEN_FEHLGESCHLAGEN == ex.getCode() 
					|| EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_LOSSOLLMATERIAL_ANLEGEN_FEHLGESCHLAGEN == ex.getCode() 
					|| EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_MATERIALAUSGABE_FEHLGESCHLAGEN == ex.getCode() 
					|| EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_UNMARSHAL_FEHLGESCHLAGEN == ex.getCode()) {
				return LPMain.getTextRespectUISPr("fert.vendidata.import.error." + ex.getCode());
			} else if (EJBExceptionLP.FEHLER_FERTIGUNG_IMPORT_LOSSTATUS == ex.getCode()
					|| EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER == ex.getCode()) {
				List<Object> clientInfo = ex.getAlInfoForTheClient();
				return LPMain.getMessageTextRespectUISPr("fert.vendidata.import.error." + ex.getCode(), clientInfo.toArray());
			}
			
			return ex.getMessage();
		}

		private String getSeverityText(EJBVendidataArticleExceptionLP ex) {
			if (EJBVendidataArticleExceptionLP.SEVERITY_ERROR.equals(ex.getSeverity())) {
				return "E";
			} else if (EJBVendidataArticleExceptionLP.SEVERITY_INFO.equals(ex.getSeverity())) {
				return "I";
			} else if (EJBVendidataArticleExceptionLP.SEVERITY_WARNING.equals(ex.getSeverity())) {
				return "W";
			}
			return "";
		}
	}
}
