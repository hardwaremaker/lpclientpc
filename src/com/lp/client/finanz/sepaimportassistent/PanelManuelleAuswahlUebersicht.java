package com.lp.client.finanz.sepaimportassistent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.assistent.view.IDataUpdateListener;
import com.lp.client.frame.component.ButtonFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.finanz.service.BuchungKompakt;
import com.lp.server.util.BelegZahlungAdapter;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

public class PanelManuelleAuswahlUebersicht extends JPanel implements ActionListener, IUebersichtDataUpdateListener, IDataUpdateListener {

	private static final long serialVersionUID = -435971851633937428L;
	
	private static final String ACTION_SPECIAL_LOESCHEN = "action_special_loeschen";
	private static final String ACTION_SPECIAL_EDITIEREN = "action_special_editieren";

	private JPanel panelButtons;
	private JButton wrbDelete;
	private JButton wrbEdit;
	private JTable tableUebersicht;
	
	private List<ManuelleAuswahlUebersichtEntry> entries;
	private Map<ManuelleAuswahlUebersichtEntry, BelegZahlungAdapter> belegZahlungen;
	private Map<ManuelleAuswahlUebersichtEntry, BuchungKompakt> buchungen;
	private IManuelleAuswahlUebersichtController controller;
	private UebersichtEntryNormalizer normalizer;
	
	public PanelManuelleAuswahlUebersicht(IManuelleAuswahlUebersichtController controller) {
		this.controller = controller;
		this.entries = new ArrayList<ManuelleAuswahlUebersichtEntry>();
		this.normalizer = new UebersichtEntryNormalizer(controller.getWaehrung());
		this.belegZahlungen = new HashMap<ManuelleAuswahlUebersichtEntry, BelegZahlungAdapter>();
		this.buchungen = new HashMap<ManuelleAuswahlUebersichtEntry, BuchungKompakt>();

		jbInit();
	}
	
	private void jbInit() {
		wrbDelete = ButtonFactory.createJButton(IconFactory.getDelete(), 
				LPMain.getTextRespectUISPr("lp.loeschen"), ACTION_SPECIAL_LOESCHEN);
		wrbDelete.addActionListener(this);
		wrbDelete.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		wrbDelete.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		wrbDelete.setMaximumSize(HelperClient.getToolsPanelButtonDimension());
		
		wrbEdit = ButtonFactory.createJButton(IconFactory.getEdit(), 
				LPMain.getTextRespectUISPr("fb.sepa.import.button.editieren"), ACTION_SPECIAL_EDITIEREN);
		wrbEdit.addActionListener(this);
		wrbEdit.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		wrbEdit.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		wrbEdit.setMaximumSize(HelperClient.getToolsPanelButtonDimension());

		panelButtons = new JPanel();
		panelButtons.setLayout(new MigLayout("", ""));
		panelButtons.add(wrbDelete, "align left");
		panelButtons.add(wrbEdit, "align left");
		
		initTable();
		
		setLayout(new MigLayout("wrap 1", "[fill,grow]", "[fill|fill,grow]"));
		add(panelButtons);
		
		add(new JScrollPane(tableUebersicht));
	}

	private void initTable() {
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		
		tableUebersicht = new JTable();
		tableUebersicht.setModel(new UebersichtTableModel());
		tableUebersicht.getColumnModel().getColumn(UebersichtTableModel.IDX_BETRAG).setCellRenderer(rightRenderer);
		
		Map<Integer, Integer> columnWidths = new HashMap<Integer, Integer>();
		columnWidths.put(UebersichtTableModel.IDX_AR_CNR, QueryParameters.FLR_BREITE_M);
		columnWidths.put(UebersichtTableModel.IDX_ER_CNR, QueryParameters.FLR_BREITE_M);
		columnWidths.put(UebersichtTableModel.IDX_KONTO_CNR, QueryParameters.FLR_BREITE_M);
		columnWidths.put(UebersichtTableModel.IDX_NAME, QueryParameters.FLR_BREITE_XXL);
		columnWidths.put(UebersichtTableModel.IDX_INFO, QueryParameters.FLR_BREITE_XL);
		columnWidths.put(UebersichtTableModel.IDX_BETRAG, QueryParameters.FLR_BREITE_PREIS);
		columnWidths.put(UebersichtTableModel.IDX_WAEHRUNG, QueryParameters.FLR_BREITE_WAEHRUNG);
		setTableColumnWidths(columnWidths);
	}

	private void setTableColumnWidths(Map<Integer, Integer> columnWidths) {
		
		for (Map.Entry<Integer, Integer> entry : columnWidths.entrySet()) {
			TableColumn tColumn = tableUebersicht.getColumnModel().getColumn(entry.getKey());
			tColumn.setPreferredWidth(Helper.getBreiteInPixel(Math.abs(entry.getValue())));
			tColumn.setWidth(Helper.getBreiteInPixel(Math.abs(entry.getValue())));
			tColumn.setMinWidth(Helper.getBreiteInPixel(QueryParameters.FLR_BREITE_MINIMUM));
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event == null) return;
		
		if (ACTION_SPECIAL_LOESCHEN.equals(event.getActionCommand())) {
			actionLoeschen();
		} else if (ACTION_SPECIAL_EDITIEREN.equals(event.getActionCommand())) {
			actionEditieren();
		}
	}
	
	private void actionEditieren() {
		if (tableUebersicht.getSelectedRow() < 0) return;
		
		ManuelleAuswahlUebersichtEntry entry = entries.get(tableUebersicht.getSelectedRow());
		BelegZahlungAdapter belegZahlung = belegZahlungen.get(entry);
		if (belegZahlung != null) {
			controller.actionEditBelegZahlung(belegZahlung);
			return;
		}
		
		BuchungKompakt buchung = buchungen.get(entry);
		if (buchung != null) {
			controller.actionEditBuchung(buchung);
			buchungen.remove(entry);
			entries.remove(entry);
			((AbstractTableModel)tableUebersicht.getModel()).fireTableDataChanged();
		}
	}

	private void actionLoeschen() {
		if (tableUebersicht.getSelectedRow() < 0) return;
		
		ManuelleAuswahlUebersichtEntry entry = entries.get(tableUebersicht.getSelectedRow());
		BelegZahlungAdapter belegZahlung = belegZahlungen.get(entry);
		if (belegZahlung != null) {
			controller.actionDeleteBelegZahlung(belegZahlung);
			belegZahlungen.remove(entry);
			entries.remove(entry);
			((AbstractTableModel)tableUebersicht.getModel()).fireTableDataChanged();
			return;
		}
		
		BuchungKompakt buchung = buchungen.get(entry);
		if (buchung != null) {
			controller.actionDeleteBuchung(buchung);
			buchungen.remove(entry);
			entries.remove(entry);
			((AbstractTableModel)tableUebersicht.getModel()).fireTableDataChanged();
		}
	}

	@Override
	public void dataUpdated() {
		entries.clear();
		belegZahlungen.clear();
		buchungen.clear();
		
		List<BelegZahlungAdapter> belegZahlungenList = controller.getBelegZahlungen();
		for (BelegZahlungAdapter zahlung : belegZahlungenList) {
			ManuelleAuswahlUebersichtEntry entry = normalizer.normalize(zahlung);
			entries.add(entry);
			belegZahlungen.put(entry, zahlung);
		}

		List<BuchungKompakt> buchungenList = controller.getManuelleBuchungen();
		Integer bankkontoIId = controller.getBankkontoIId();
		for (BuchungKompakt buchung : buchungenList) {
			int idxBuchungdetail = 
					buchung.getBuchungdetailList().get(0).getKontoIId().equals(bankkontoIId) ? 1 : 0;
			ManuelleAuswahlUebersichtEntry entry = 
					normalizer.normalize(buchung.getBuchungdetailList().get(idxBuchungdetail));
			entries.add(entry);
			buchungen.put(entry, buchung);
		}
		
		((AbstractTableModel)tableUebersicht.getModel()).fireTableDataChanged();
	}

	@Override
	public void dataUpdated(BelegZahlungAdapter belegZahlung) {
		entries.add(normalizer.normalize(belegZahlung));
		((AbstractTableModel)tableUebersicht.getModel()).fireTableDataChanged();
	}

	@Override
	public void dataUpdated(BuchungKompakt buchung) {
		entries.add(normalizer.normalize(buchung));
		((AbstractTableModel)tableUebersicht.getModel()).fireTableDataChanged();
	}
	
	public class UebersichtTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -219414149931337799L;

		protected static final int IDX_AR_CNR = 0;
		protected static final int IDX_ER_CNR = 1;
		protected static final int IDX_KONTO_CNR = 2;
		protected static final int IDX_NAME = 3;
		protected static final int IDX_INFO = 4;
		protected static final int IDX_BETRAG = 5;
		protected static final int IDX_WAEHRUNG = 6;
		
		private Class[] columnClasses = new Class[7];
		private String[] columnNames = new String[7];
		
		public UebersichtTableModel() {
			columnClasses[IDX_AR_CNR] = String.class;
			columnClasses[IDX_ER_CNR] = String.class;
			columnClasses[IDX_KONTO_CNR] = String.class;
			columnClasses[IDX_BETRAG] = BigDecimal.class;
			columnClasses[IDX_NAME] = String.class;
			columnClasses[IDX_INFO] = String.class;
			columnClasses[IDX_WAEHRUNG] = String.class;

			columnNames[IDX_AR_CNR] = LPMain.getTextRespectUISPr("fb.sepa.import.tab.head.rechnungnummer");
			columnNames[IDX_ER_CNR] = LPMain.getTextRespectUISPr("fb.sepa.import.tab.head.eingangsrechnungnummer");
			columnNames[IDX_KONTO_CNR] = LPMain.getTextRespectUISPr("fb.sepa.import.tab.head.kontonummer");
			columnNames[IDX_BETRAG] = LPMain.getTextRespectUISPr("fb.sepa.import.tab.head.betrag");
			columnNames[IDX_NAME] = LPMain.getTextRespectUISPr("fb.sepa.import.tab.head.name");
			columnNames[IDX_INFO] = LPMain.getTextRespectUISPr("fb.sepa.import.tab.head.info");
			columnNames[IDX_WAEHRUNG] = LPMain.getTextRespectUISPr("fb.sepa.import.tab.head.waehrung");
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
			return entries.size();
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (entries == null || entries.size() == 0) return null;
			
			ManuelleAuswahlUebersichtEntry entry = entries.get(row);
			
			if (IDX_AR_CNR == column) {
				return entry.getArNr();
			} else if (IDX_ER_CNR == column) {
				return entry.getErNr();
			} else if (IDX_KONTO_CNR == column) {
				return entry.getKontoNr();
			} else if (IDX_BETRAG == column) {
				return Helper.formatZahl(Helper.rundeKaufmaennisch(entry.getBetrag(), 2),
					2, LPMain.getInstance().getUISprLocale());
			} else if (IDX_NAME == column) {
				return entry.getName();
			} else if (IDX_INFO == column) {
				return entry.getInfo();
			} else if (IDX_WAEHRUNG == column) {
				return entry.getWaehrungCNr();
			}
			
			return null;
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}

	}


}
