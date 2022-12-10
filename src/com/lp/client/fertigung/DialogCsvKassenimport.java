package com.lp.client.fertigung;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.IImportResultController;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.VerbrauchsartikelImportResult;
import com.lp.server.fertigung.service.VerbrauchsartikelImportStats;
import com.lp.server.fertigung.service.errors.ImportException;
import com.lp.service.csvverbrauch.CsvErrorFactory;
import com.lp.service.csvverbrauch.ICsvErrorAction;

public class DialogCsvKassenimport extends JDialog implements TableModel, ActionListener {
	private static final long serialVersionUID = 3729968635971045222L;

	private WrapperLabel wlaCurrentFile = new WrapperLabel();
	private WrapperTextField wtfCurrentFile = new WrapperTextField(100);
	private WrapperLabel wlaNextFile = new WrapperLabel();
	private WrapperLabel wlaImportMessages = new WrapperLabel();
	private JTable tableResults;
	private JScrollPane panelTable;
	
	private WrapperLabel wlaFileImportStatus = new WrapperLabel();
	private WrapperLabel wlaErrorLines = new WrapperLabel();
	private WrapperLabel wlaImportLines = new WrapperLabel();
	
	private WrapperButton wbuVerify = new WrapperButton();
	private WrapperButton wbuImport = new WrapperButton();
	private WrapperButton wbuCancel = new WrapperButton();

	private IImportResultController<VerbrauchsartikelImportResult> controller;
	private String[] columnNames = new String[] {
			LPMain.getTextRespectUISPr("fert.verbrauchsartikel.import.zeile"),
			LPMain.getTextRespectUISPr("fert.verbrauchsartikel.import.fehlerbez")
	};
	private Class[] columnClasses = new Class[] {Integer.class, String.class};
	private CsvErrorFactory errorFactory = new CsvErrorFactory();
	private List<ICsvErrorAction> errorActions;
	private VerbrauchsartikelImportStats stats;
	
	public DialogCsvKassenimport(Frame owner, String title, boolean modal, 
			IImportResultController<VerbrauchsartikelImportResult> controller) {
		super(owner, title, modal);
		this.controller = controller;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//		setResizable(false);
		//setPreferredSize(new Dimension(800, 500));
		
		jbInit();
		pack();
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(this);
	}

	private void jbInit() {
		wlaCurrentFile.setText(LPMain.getTextRespectUISPr("fert.verbrauchsartikel.import.aktuelledatei"));
		wtfCurrentFile.setEditable(false);
		wlaImportMessages.setText(null);
		wlaImportMessages.setHorizontalAlignment(SwingConstants.LEFT);
		wlaErrorLines.setHorizontalAlignment(SwingConstants.LEFT);
		wlaImportLines.setHorizontalAlignment(SwingConstants.LEFT);
		wlaFileImportStatus.setHorizontalAlignment(SwingConstants.LEFT);
		wbuCancel.setText(LPMain.getTextRespectUISPr("lp.abbrechen"));
		wbuCancel.addActionListener(this);
		wbuVerify.setText(LPMain.getTextRespectUISPr("fert.verbrauchsartikel.import.pruefen"));
		wbuVerify.addActionListener(this);
		wbuImport.setText(LPMain.getTextRespectUISPr("fert.verbrauchsartikel.import.importieren"));
		wbuImport.addActionListener(this);
		updateFilenames();
		
		tableResults = new JTable(this);
		tableResults.setAutoCreateRowSorter(true) ;
		tableResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
		panelTable = new JScrollPane(tableResults);
		panelTable.setPreferredSize(new Dimension(1000, 250));
		
		setPreferredColumnWidth(new Integer[]{50, 940});
		
		JPanel panelButtons = new JPanel();
		HvLayout buttonsLayout = HvLayoutFactory.create(panelButtons, "ins 0", "5%[30%,fill]20[30%,fill]20[30%,fill]5%", "");
		buttonsLayout.add(wbuVerify).add(wbuImport).add(wbuCancel);
		
		JPanel panelDialog = new JPanel();
		HvLayout dialogLayout = HvLayoutFactory.create(panelDialog, "wrap 2", "[30%,fill|70%,fill,grow]", "[fill|fill|fill|fill,grow|fill|fill|fill|fill]");
		dialogLayout.add(wlaCurrentFile).add(wtfCurrentFile)
			.add(wlaNextFile).span(2)
			.add(wlaImportMessages).span(2)
			.add(panelTable).span(2)
			.add(wlaFileImportStatus).span(2)
			.add(wlaErrorLines).span(2)
			.add(wlaImportLines).span(2)
			.add(panelButtons).span(2);
		
		add(panelDialog);
	}

	protected void setPreferredColumnWidth(Integer[] preferredSizes) {
		for (int i = 0; i < preferredSizes.length; i++) {
			TableColumn col = tableResults.getColumnModel().getColumn(i);
			col.setPreferredWidth(preferredSizes[i]);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (wbuVerify == event.getSource()) {
			actionVerify();
		} else if (wbuImport == event.getSource()) {
			actionImport();
		} else if (wbuCancel == event.getSource()) {
			actionCancel();
		}
	}

	private void actionVerify() {
		wlaFileImportStatus.setText(LPMain.getTextRespectUISPr("fert.verbrauchsartikel.import.pruefe"));
		invalidate();
		setImportResult(controller.checkImport());

		updateStats();
		wlaFileImportStatus.setText(LPMain.getMessageTextRespectUISPr(
				"fert.verbrauchsartikel.import.dateigeprueft", controller.getCurrentFilename()));
	}

	private void actionImport() {
		wlaFileImportStatus.setText(LPMain.getTextRespectUISPr("fert.verbrauchsartikel.import.importiere"));
		revalidate();
		
		String filename = controller.getCurrentFilename();
		boolean wasLastImport = !controller.hasNext();
		setImportResult(controller.doImport());

		updateStats();
		if (getErrorActions().size() < 1) {
			updateFilenames();
			wlaFileImportStatus.setText(LPMain.getMessageTextRespectUISPr(
					"fert.verbrauchsartikel.import.dateiimportiert", filename));
			if (wasLastImport) {
				wbuVerify.setEnabled(false);
				wbuImport.setEnabled(false);
				wbuCancel.setText(LPMain.getTextRespectUISPr("button.ok"));
			}
		} else {
			wlaFileImportStatus.setText(LPMain.getMessageTextRespectUISPr(
					"fert.verbrauchsartikel.import.importfehlgeschlagen", filename));
		}
	}

	private void updateStats() {
		wlaErrorLines.setText(LPMain.getMessageTextRespectUISPr("fert.verbrauchsartikel.import.anzahlfehler", 
				getStats().getErrorCounts()));
//		wlaImportLines.setText(LPMain.getMessageTextRespectUISPr("fert.verbrauchsartikel.import.anzahlimports", 
//				getStats().getPossibleImports()));
	}
	
	private void updateFilenames() {
		wtfCurrentFile.setText(controller.getCurrentFilename());
		if (controller.getPosition() > controller.getNumberOfFiles()) {
			wlaCurrentFile.setText(LPMain.getMessageTextRespectUISPr("fert.verbrauchsartikel.import.aktuelledatei",
					controller.getNumberOfFiles(), controller.getNumberOfFiles()));
		} else {
			wlaCurrentFile.setText(LPMain.getMessageTextRespectUISPr("fert.verbrauchsartikel.import.aktuelledatei",
					controller.getPosition(), controller.getNumberOfFiles()));
		}

		if (controller.hasNext()) {
			wlaNextFile.setText(LPMain.getMessageTextRespectUISPr("fert.verbrauchsartikel.import.naechstedatei", 
					controller.getNextFilename()));
		} else {
			wlaNextFile.setText(null);
		}
	}

	private void actionCancel() {
		dispose();
	}

	private void setImportResult(VerbrauchsartikelImportResult result) {
		if (result == null) actionCancel();
		
		errorActions = new ArrayList<ICsvErrorAction>();
		for (ImportException ex : result.getImportErrors()) {
			errorActions.add(errorFactory.getCsvErrorAction(ex));
		}
		stats = result.getStats();
		tableResults.revalidate();
	}

	public List<ICsvErrorAction> getErrorActions() {
		if (errorActions == null) {
			errorActions = new ArrayList<ICsvErrorAction>();
		}
		return errorActions;
	}
	
	public VerbrauchsartikelImportStats getStats() {
		if (stats == null) {
			stats = new VerbrauchsartikelImportStats();
		}
		return stats;
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
		return getErrorActions().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return getErrorActions().get(rowIndex).getLinenumber();
		} else if (columnIndex == 1) {
			return getErrorActions().get(rowIndex).getMessage();
		}
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
