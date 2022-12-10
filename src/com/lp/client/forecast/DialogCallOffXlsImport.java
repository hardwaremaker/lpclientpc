package com.lp.client.forecast;

import java.awt.Frame;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.lp.client.frame.DialogError;
import com.lp.client.frame.DialogImportBase;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.forecast.service.CallOffXlsImporterResult;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBLineNumberExceptionLP;

public class DialogCallOffXlsImport extends DialogImportBase implements TableModel {

	private static final long serialVersionUID = -4318602273952788741L;
	private ICallOffXlsImportController importController;
	private List<EJBLineNumberExceptionLP> importErrors;
	private String[] columnNames;
	private Class[] columnClasses;
	private String[] severityMap = new String[] {"-", "D", "I", "W", "F"};
	private JPanel rowPanel;
	private WrapperLabel rowLabel;
	private WrapperNumberField rowField;
	private boolean isGeprueft = false;
	private WrapperLabel lieferadresseLabel;
	private WrapperTextField lieferadresseField;

	public DialogCallOffXlsImport(Frame owner, boolean modal, ICallOffXlsImportController importController) {
		super(owner, importController.getDialogTitle(), modal, importController);
		this.importController = importController;
		setPreferredColumnWidth(new Integer[] {20, 20, 40, 100, 100, 600});
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
//				showStats(false);
				initStartRowPanel();
				setTextButtonImprtExportButton(LPMain.getTextRespectUISPr("dialog.import.pruefen"));
				wbuExport.setEnabled(true);
				wlaVerify.setVisible(false);
//				actionVerify();
				invalidate();
			}
		});
	}
	
	private void initStartRowPanel() {
//		try {
//			rowLabel = new WrapperLabel(LPMain.getTextRespectUISPr("dialog.import.linienabruf.startzeile"));
//			rowField = new WrapperNumberField();
//			rowField.setInteger(importController.getStartRow());
//			rowField.setFractionDigits(0);
			rowPanel = new JPanel();
			
			lieferadresseLabel = new WrapperLabel();
			lieferadresseLabel.setText(LPMain.getTextRespectUISPr("ls.ausliefervorschlag.lieferadresse"));
			lieferadresseField = new WrapperTextField();
			lieferadresseField.setText(importController.getFclieferadresseAsText());
			lieferadresseField.setEditable(false);

			HvLayout hvLayoutOptional = HvLayoutFactory.create(rowPanel, "", "[200,fill|200,fill|fill,grow]", "fill,grow");
			hvLayoutOptional
				.add(lieferadresseLabel)
				.add(lieferadresseField, "al left, wrap");
			
			updateOptionalPanel(rowPanel);
//		} catch (ExceptionLP e) {
//			this.setVisible(false);
//			new DialogError(LPMain.getInstance().getDesktop(), e,
//					DialogError.TYPE_INFORMATION);
//		}
	}
	
	public List<EJBLineNumberExceptionLP> getImportErrors() {
		if (importErrors == null) {
			importErrors = new ArrayList<EJBLineNumberExceptionLP>();
		}
		return importErrors;
	}

	protected void initTableModel() {
		setColumnNames(new String[] {
				LPMain.getTextRespectUISPr("dialog.import.head.zeile"), 
				LPMain.getTextRespectUISPr("dialog.import.head.klasse"), 
				LPMain.getTextRespectUISPr("dialog.import.head.fehlernr"), 
				LPMain.getTextRespectUISPr("dialog.import.head.wert"), 
				LPMain.getTextRespectUISPr("dialog.import.head.erwartet"),
				LPMain.getTextRespectUISPr("dialog.import.head.fehlerbez")});
		setColumnClasses(new Class[] {Integer.class, String.class, Integer.class, String.class, String.class, String.class});
	}

	private void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	
	public void setColumnClasses(Class[] columnClasses) {
		this.columnClasses = columnClasses;
	}

	@Override
	protected TableModel getTableModel() {
		initTableModel();
		return this;
	}

	@Override
	protected void actionExport() {
		if (!isGeprueft) {
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					wlaVerify.setVisible(true);
					wlaVerify.revalidate();
					setTextButtonImprtExportButton(getTextImportExportButton());
					actionVerify();
				}
			});
			return;
		}

		actionUIExport();
		try {
			setImportResult(importController.doImport());
			actionUIExportDone(hasErrors());
			invalidate();
		} catch (Exception e) {
			this.setVisible(false);
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_INFORMATION);
			actionUIExportDone(false);
		}
	}

	/**
	 * @return
	 */
	private boolean hasErrors() {
		if (importErrors == null || importErrors.isEmpty()) return true;
		
		for (EJBLineNumberExceptionLP le : importErrors) {
			if (EJBLineNumberExceptionLP.SEVERITY_ERROR.equals(le.getSeverity())) {
				return false;
			}
		}
		return true;
	}

	private void setImportResult(CallOffXlsImporterResult result) {
		if (result == null) return;
		importErrors = result.getMessages();
		setTotalCounts(result.getStats().getTotalExports());
		setErrorCounts(result.getStats().getErrorCounts());
	}

	@Override
	protected void actionVerify() {
//		try {
//			if (rowField.getInteger() == null) return;
//		} catch (ExceptionLP e1) {
//			this.setVisible(false);
//			new DialogError(LPMain.getInstance().getDesktop(), e1,
//					DialogError.TYPE_INFORMATION);
//		}
//		
		actionUIVerify();
		try {
//			importController.setStartRow(rowField.getInteger());
			setImportResult(importController.checkImport());
			isGeprueft = true;
			actionUIVerificationDone(hasErrors());
		} catch (Exception e) {
			this.setVisible(false);
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_INFORMATION);
			actionUIExportDone(false);
		}
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
		return columnClasses.length;
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
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= getImportErrors().size()) return null;
		
		EJBLineNumberExceptionLP lne = getImportErrors().get(rowIndex);
		String wert = lne.getAlInfoForTheClient() != null && !lne.getAlInfoForTheClient().isEmpty() && lne.getAlInfoForTheClient().size() > 0 ?
				(String)lne.getAlInfoForTheClient().get(0) : null;
		String erwartet = lne.getAlInfoForTheClient() != null && !lne.getAlInfoForTheClient().isEmpty() && lne.getAlInfoForTheClient().size() > 1 ?
				(String)lne.getAlInfoForTheClient().get(1) : null;
				
		if (0 == columnIndex) return lne.getLinenumber() != null ? lne.getLinenumber().intValue() + 1 : null;
		if (1 == columnIndex) return severityMap[lne.getSeverity()];
		if (2 == columnIndex) return lne.getCode();
		if (3 == columnIndex) return wert;
		if (4 == columnIndex) return erwartet;
		if (5 == columnIndex) return getMessage(lne);

		return null;
	}

	private String getMessage(EJBLineNumberExceptionLP lne) {
		if (EJBExceptionLP.FEHLER_FORECAST_IMPORT_ARTIKEL_NICHT_GEFUNDEN == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.xls.callof.error.artikelnichtgefunden");
		} else if (EJBExceptionLP.FEHLER_FORECAST_IMPORT_BESTELLNUMMER_LEER == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.xls.callof.error.bestellnummerleer");
		} else if (EJBExceptionLP.FEHLER_FORECAST_IMPORT_DATUM_LEER == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.xls.callof.error.datumleer");
		} else if (EJBExceptionLP.FEHLER_FORECAST_IMPORT_MEHR_ALS_EINEN_ARTIKEL_GEFUNDEN == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.xls.callof.error.mehralseinartikel");
		} else if (EJBExceptionLP.FEHLER_FORECAST_IMPORT_MENGE_LEER == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.xls.callof.error.mengeleer");
		} else if (EJBExceptionLP.FEHLER_FORECAST_IMPORT_UNGUELTIGE_ZAHL == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.xls.callof.error.ungueltigezahl");
		} else if (EJBExceptionLP.FEHLER_FORECAST_IMPORT_UNGUELTIGES_DATUM == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.xls.callof.error.ungueltigesdatum");
		} else if (EJBExceptionLP.FEHLER_FORECAST_IMPORT_DATEI_NICHT_LESBAR == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.xls.callof.error.dateinichtlesbar");
		} else if (EJBExceptionLP.FEHLER_FORECAST_IMPORT_AUFTRAG_MIT_STATUS_ANGELEGT == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.xls.callof.error.forecastauftragimstatusangelegt");
		} else if (EJBExceptionLP.FEHLER_FORECAST_IMPORT_KUNDE_NICHT_GEFUNDEN == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.xls.callof.error.kundenichtgefunden");			
		} else if (EJBExceptionLP.FEHLER_FORECAST_IMPORT_LIEFERKUNDE_NICHT_GEFUNDEN == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.xls.callof.error.lieferkundenichtgefunden");			
		} else if (EJBExceptionLP.FEHLER_FORECAST_IMPORT_UNBEKANNTE_FORECAST_ART == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.xls.callof.error.unbekannteforecastart");			
		} else if(EJBExceptionLP.FEHLER_EDIFACT_ARTIKEL_BESTELLNUMMER_MEHRFACH_VORHANDEN == lne.getCode()) {
			List<Object> info = lne.getAlInfoForTheClient();
			return LPMain.getMessageTextRespectUISPr("forecast.import.edi.artikelbestellnummermehrfach", info.get(1), info.get(2), info.get(3), info.get(4));
		} else if(EJBExceptionLP.FEHLER_FORECAST_IMPORT_LMFZ_UNGUELTIGES_DATUM == lne.getCode()) {
			try {
				List<Object> info = lne.getAlInfoForTheClient();
				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKeySmallOhneExc((Integer)info.get(2));
				Date d = new Date((Long)info.get(3));
				BigDecimal amount = (BigDecimal) info.get(4);
				return LPMain.getMessageTextRespectUISPr(
						"forecast.import.xls.callof.error.ungueltigeslmfzdatum", 
						artikelDto.getCNr(), d, amount);
			} catch(Throwable t) {
				String msg = t != null ? t.getMessage() : "";
				return LPMain.getMessageTextRespectUISPr(
						"forecast.import.xls.callof.error.ungueltigeslmfzdatum.fail", msg);				
			}
		} 
		
		this.setVisible(false);
		new DialogError(LPMain.getInstance().getDesktop(), lne,
				DialogError.TYPE_INFORMATION);
		return null;
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

}
