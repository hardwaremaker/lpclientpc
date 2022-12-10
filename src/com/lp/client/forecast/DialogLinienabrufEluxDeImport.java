package com.lp.client.forecast;

import java.awt.Frame;
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
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.forecast.service.CallOffXlsImporterResult;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBLineNumberExceptionLP;
import com.lp.util.Helper;

public class DialogLinienabrufEluxDeImport extends DialogImportBase implements TableModel {
	private static final long serialVersionUID = -2174356852406958455L;
	
	private ILinienabrufEluxDeImportController importController;
	private List<EJBLineNumberExceptionLP> importErrors;
	private String[] columnNames;
	private Class[] columnClasses;
	private String[] severityMap = new String[] {"-", "D", "I", "W", "F"};
	private JPanel datePanel;
	private WrapperLabel dateLabel;
	private WrapperDateField dateField;
	private WrapperLabel lieferadresseLabel;
	private WrapperTextField lieferadresseField;
	private boolean isGeprueft = false;
	
	public DialogLinienabrufEluxDeImport(Frame owner, String title,
			boolean modal, ILinienabrufEluxDeImportController importController) {
		super(owner, title, modal, importController);
		this.importController = importController;
		setPreferredColumnWidth(new Integer[] {20, 20, 40, 100, 100, 600});
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
//				showStats(false);
				initDatePanel();
				setTextButtonImprtExportButton(LPMain.getTextRespectUISPr("dialog.import.pruefen"));
				wbuExport.setEnabled(true);
				wlaVerify.setVisible(false);
//				actionVerify();
				invalidate();
			}
		});
	}
	
	private void initDatePanel() {
		dateLabel = new WrapperLabel();
		dateLabel.setText(LPMain.getTextRespectUISPr("dialog.import.linienabruf.liefertermin"));
		dateField = new WrapperDateField();
		dateField.setShowRubber(false);
		Date date = Helper.addiereTageZuDatum(new Date(System.currentTimeMillis()), 2);
		dateField.setDate(date);
		
		lieferadresseLabel = new WrapperLabel();
		lieferadresseLabel.setText(LPMain.getTextRespectUISPr("ls.ausliefervorschlag.lieferadresse"));
		lieferadresseField = new WrapperTextField();
		lieferadresseField.setText(importController.getFclieferadresseAsText());
		lieferadresseField.setEditable(false);
		
		datePanel = new JPanel();
		HvLayout hvLayoutOptional = HvLayoutFactory.create(datePanel, "", "[200,fill|200,fill|fill]", "fill,grow");
		hvLayoutOptional
			.add(lieferadresseLabel)
			.add(lieferadresseField, "al left, wrap")
			.add(dateLabel)
			.add(dateField, "al left");
		
		updateOptionalPanel(datePanel);
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
			wlaVerify.setVisible(true);
			setTextButtonImprtExportButton(getTextImportExportButton());
			actionVerify();
			return;
		}
		
		actionUIExport();
		try {
			importController.setDeliveryDate(dateField.getDate());
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
		actionUIVerify();
		try {
			importController.setDeliveryDate(dateField.getDate());
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
	public void addTableModelListener(TableModelListener arg0) {
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
		if (EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_ZUWENIG_ZEILEN == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.txt.linienabruf.error.zuwenigzeilen");
		} else if (EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_ZEILENLAENGE_ZU_KLEIN == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.txt.linienabruf.error.zeilenlaengezuklein");
		} else if (EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_MENGE_NICHT_NUMERISCH == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.txt.linienabruf.error.mengenichtnumerisch");
		} else if (EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_MEHR_ALS_EINEN_ARTIKEL_GEFUNDEN == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.txt.linienabruf.error.mehralseinartikel");
		} else if (EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_ARTIKEL_NICHT_IM_FC_AUFTRAG == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.txt.linienabruf.error.artikelnichtinfcauftrag");
		} else if (EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_UNGUELTIGES_DATUM == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.txt.linienabruf.error.ungueltigesdatum");
		} else if (EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_ARTIKEL_NICHT_GEFUNDEN == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.txt.linienabruf.error.artikelnichtgefunden");
		} else if (EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_LIEFERTERMIN_AUSSERHALB_POSITIONSTERMINE == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.txt.linienabruf.error.lieferterminausserhalb");
		} else if (EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_NUR_CALLOFF_DAILY_ERLAUBT == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.txt.linienabruf.error.nurcalloffdaily");
		}	else if (EJBExceptionLP.FEHLER_LINIENABRUF_IMPORT_KEINE_AUFTRAEGE_VORHANDEN == lne.getCode()) {
			return LPMain.getTextRespectUISPr("forecast.import.txt.linienabruf.error.keineforecastauftraege");
		}
		
		
		this.setVisible(false);
		new DialogError(LPMain.getInstance().getDesktop(), lne,
				DialogError.TYPE_INFORMATION);
		return null;
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
	}

}
