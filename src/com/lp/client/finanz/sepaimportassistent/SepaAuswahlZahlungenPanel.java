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
package com.lp.client.finanz.sepaimportassistent;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableStringConverter;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.assistent.view.IDataUpdateListener;
import com.lp.client.frame.component.ButtonFactory;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.finanz.service.ISepaImportResult;
import com.lp.server.finanz.service.SepaBetrag;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.util.BelegAdapter;
import com.lp.server.util.BelegZahlungAdapter;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;
import com.lp.util.HelperReport;

import net.miginfocom.swing.MigLayout;

public abstract class SepaAuswahlZahlungenPanel extends JPanel implements ActionListener, ListSelectionListener, IDataUpdateListener {

	private static final long serialVersionUID = 1L;
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());
	
	private static final String ACTION_SPECIAL_REFRESH = "action_special_refresh";
	private List<BelegAdapter> belege;
	private Map<Integer, PartnerDto> belegePartnerMap;
	
	private TableModelBelegZahlung tableModel;
	private JTable tableBelege;
	private WrapperLabel wlPartner;
	private JButton wrbRefresh;
	private JPanel panelButtons;
	private WrapperTextField wtFilterPartner;
	private WrapperLabel wlNummer;
	private WrapperTextField wtFilterNummer;
	
	private TabbedPaneBelegZahlung tabbedPane;
	private TableRowSorter<TableModel> rowSorter;
	private Map<Integer, RowFilter<Object, Object>> columnFilter;
	
	protected Comparator<BelegAdapter> belegAdapterComparator = new Comparator<BelegAdapter>() {
		@Override
		public int compare(BelegAdapter beleg1, BelegAdapter beleg2) {
			// absteigend
			return beleg2.getCNr().compareTo(beleg1.getCNr());
		}
	};
	
	public SepaAuswahlZahlungenPanel(TabbedPaneBelegZahlung tb) {
		belege = new ArrayList<BelegAdapter>();
		belegePartnerMap = new HashMap<Integer, PartnerDto>();
		tabbedPane = tb;
		columnFilter = new HashMap<>();
		jbInit();
	}
	
	abstract protected void updateTableData() throws ExceptionLP;
	
	abstract protected String getActionCommandAddOn();
	
	abstract protected String getPartnerTableHead();
	
	abstract protected BelegZahlungAdapter getBelegZahlung(Integer iId, 
			List<BelegZahlungAdapter> belegZahlungen);

	abstract protected BelegZahlungAdapter getNewBelegZahlungAdapter();
	
	abstract protected SepaBetrag getBetragAsSepaBetrag(BigDecimal bdBetrag);
	
	abstract protected String getTextNummer();
	
	protected void setSelection(BelegZahlungAdapter belegZahlungAdapter) {
		System.out.println(tableBelege.getRowCount());
		for (int row=0; row<tableBelege.getRowCount(); row++) {
			if (tableModel.getValueAt(tableBelege.convertRowIndexToModel(row), TableModelBelegZahlung.IDX_CNR)
					.equals(belegZahlungAdapter.getBelegAdapter().getCNr())) {
				tableBelege.setRowSelectionInterval(row, row);
				return;
			}
		}
	}
	
	protected TableModelBelegZahlung getTableModel() {
		return tableModel;
	}
	
	private void jbInit() {
		wrbRefresh = ButtonFactory.createJButton(IconFactory.getRefresh(), 
				LPMain.getTextRespectUISPr("fb.sepa.import.button.editieren"), ACTION_SPECIAL_REFRESH);
		wrbRefresh.addActionListener(this);
		wrbRefresh.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		wrbRefresh.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		wrbRefresh.setMaximumSize(HelperClient.getToolsPanelButtonDimension());

		panelButtons = new JPanel();
		panelButtons.setLayout(new MigLayout("", ""));
		panelButtons.add(wrbRefresh, "align left");

		wlPartner = new WrapperLabel();
		wlPartner.setText(getPartnerTableHead() + " (Aa|A*) = ");
		wtFilterPartner = new WrapperTextField();
		wtFilterPartner.addActionListener(this);
		
		wlNummer = new WrapperLabel();
		wlNummer.setText(getTextNummer() + " (*A)");
		wtFilterNummer = new WrapperTextField();
		wtFilterNummer.addActionListener(this);
		
		initTable();

		setLayout(new MigLayout("wrap 4, hidemode 2, fill", "[30%,fill|20%,fill|30%,fill|20%,fill]", "[fill|fill|fill, grow]"));
		add(panelButtons, "span, wrap");
		add(wlNummer);
		add(wtFilterNummer);
		add(wlPartner);
		add(wtFilterPartner);
		add(new JScrollPane(tableBelege), "newline, span 4");
	}

	/**
	 * 
	 */
	private void initTable() {
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		
		tableBelege = new JTable();
		tableModel = new TableModelBelegZahlung();
		tableBelege.setModel(tableModel);
		//tableBelege.getModel().addTableModelListener(this);
		tableBelege.getSelectionModel().addListSelectionListener(this);
		tableBelege.getColumnModel().getColumn(TableModelBelegZahlung.IDX_OFFENERBETRAG)
			.setCellRenderer(rightRenderer);
		tableBelege.setAutoCreateRowSorter(true);
		rowSorter = new TableRowSorter<TableModel>(tableBelege.getModel());
		tableBelege.setRowSorter(rowSorter);
		
		tableBelege.setDefaultRenderer(Boolean.class, new TableCellRendererCheckbox());
		tableBelege.setDefaultEditor(BigDecimal.class, new TableCellEditorTextfield());
		tableBelege.setDefaultRenderer(BigDecimal.class, new TableCellRendererJTextField());
		
		tableBelege.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				Point point = event.getPoint();
				int row = tableBelege.rowAtPoint(point);
				row = tableBelege.convertRowIndexToModel(row);
				if (row >= 0 && event.getClickCount() == 2) {
					BelegAdapter beleg = getSelectedBeleg();
					actionZahlbetragChanged(beleg.getOffenerBetrag(), row);
				}
			}
			
		});
		
		Map<Integer, Integer> columnWidths = new HashMap<Integer, Integer>();
		columnWidths.put(TableModelBelegZahlung.IDX_RECHNUNGSART, QueryParameters.FLR_BREITE_XS);
		columnWidths.put(TableModelBelegZahlung.IDX_CNR, QueryParameters.FLR_BREITE_M);
		columnWidths.put(TableModelBelegZahlung.IDX_PARTNER, QueryParameters.FLR_BREITE_XXL);
		columnWidths.put(TableModelBelegZahlung.IDX_ORT, QueryParameters.FLR_BREITE_XXL);
		columnWidths.put(TableModelBelegZahlung.IDX_BELEGDATUM, QueryParameters.FLR_BREITE_M);
		columnWidths.put(TableModelBelegZahlung.IDX_OFFENERBETRAG, QueryParameters.FLR_BREITE_PREIS);
		columnWidths.put(TableModelBelegZahlung.IDX_WAEHRUNG, QueryParameters.FLR_BREITE_WAEHRUNG);
		columnWidths.put(TableModelBelegZahlung.IDX_BETRAG, QueryParameters.FLR_BREITE_PREIS);
		
		setTableColumnWidths(columnWidths);
		strechShareWithRestColumns(columnWidths);
	}
	
	private void setTableColumnWidths(Map<Integer, Integer> columnWidths) {
		
		for (Map.Entry<Integer, Integer> entry : columnWidths.entrySet()) {
			TableColumn tColumn = tableBelege.getColumnModel().getColumn(entry.getKey());
			tColumn.setPreferredWidth(Helper.getBreiteInPixel(Math.abs(entry.getValue())));
			tColumn.setWidth(Helper.getBreiteInPixel(Math.abs(entry.getValue())));
			tColumn.setMinWidth(Helper.getBreiteInPixel(QueryParameters.FLR_BREITE_MINIMUM));
		}
	}

	private void strechShareWithRestColumns(Map<Integer, Integer> columnWidths) {
		if (columnWidths == null)
			return;
		List<TableColumn> columnsToStrech = new ArrayList<TableColumn>();
		int alreadyDefinedSize = 0;
		for (Map.Entry<Integer, Integer> entry : columnWidths.entrySet()) {
			TableColumn tc = tableBelege.getColumnModel().getColumn(entry.getKey());
			if (entry.getValue() == -1 && tc.getMaxWidth() > 0)
				columnsToStrech.add(tc);
			else
				alreadyDefinedSize += tc.getPreferredWidth();
		}

		if (columnsToStrech.size() == 0)
			return;

		int sharedTotal = tableBelege.getColumnModel().getTotalColumnWidth()
				- alreadyDefinedSize;
		for (TableColumn tc : columnsToStrech) {
			int newSize = sharedTotal / columnsToStrech.size();
			tc.setPreferredWidth(newSize);
			tc.setWidth(newSize);
		}

	}

	protected TabbedPaneBelegZahlung getTabbedPane() {
		return tabbedPane;
	}

	protected List<BelegAdapter> getBelege() {
		return belege;
	}

	protected void setBelege(List<BelegAdapter> belege) {
		this.belege = belege;
	}

	protected Map<Integer, PartnerDto> getBelegePartnerMap() {
		return belegePartnerMap;
	}

	protected void setBelegePartnerMap(Map<Integer, PartnerDto> belegePartnerMap) {
		this.belegePartnerMap = belegePartnerMap;
	}

	public BelegAdapter getSelectedBeleg() {
		if (tableBelege.getSelectedRow() < 0) return null;
		
		return belege.get(tableBelege.convertRowIndexToModel(tableBelege.getSelectedRow()));
	}
	
	@Override
	public void dataUpdated() {
		tableBelege.clearSelection();
//		clearRowFilter();
	}

	private ISepaImportResult getResultWaitingForAuswahl() {
		return getTabbedPane().getController().getResultWaitingForManuelleAuswahl();
	}
	
	protected void setBErledigtOfZahlung(Integer rowIndex, Boolean bErledigt) {
		BelegAdapter beleg = getBelege().get(rowIndex);
		List<BelegZahlungAdapter> belegZahlungen = 
				getResultWaitingForAuswahl().getManualPayments();

		BelegZahlungAdapter belegZahlung = getBelegZahlung(beleg.getIId(), belegZahlungen);
		if (belegZahlung != null) {
			belegZahlungen.remove(belegZahlung);
			belegZahlung.setBErledigt(bErledigt);
			belegZahlungen.add(belegZahlung);
			getResultWaitingForAuswahl().setManualPayments(belegZahlungen);
		}
	}

	protected void setZahlbetragOfZahlung(Integer rowIndex, BigDecimal bdZahlbetrag) {
		BelegAdapter beleg = getBelege().get(rowIndex);
		List<BelegZahlungAdapter> belegZahlungen = getResultWaitingForAuswahl().getManualPayments();
		
		BelegZahlungAdapter belegZahlung = getBelegZahlung(beleg.getIId(), belegZahlungen);
		SepaBetrag sepaZahlbetrag = getBetragAsSepaBetrag(bdZahlbetrag);
		
		if (belegZahlung == null) {
			belegZahlung = getNewBelegZahlungAdapter();
			belegZahlung.setRechnungIId(beleg.getIId());
			belegZahlung.setBelegAdapter(beleg);
		} else {
			belegZahlungen.remove(belegZahlung);
		}
		
		SepaBetrag sepaVerfuegbar = getTabbedPane().getNochVerfuegbarerZahlbetrag2();
		if (bdZahlbetrag != null) {
			SepaBetrag newSepaVerfuegbar = sepaVerfuegbar.subtract(sepaZahlbetrag);
			if (sepaVerfuegbar.getPlusMinusWert().signum() != newSepaVerfuegbar.getPlusMinusWert().signum()) {
				bdZahlbetrag = sepaVerfuegbar.getWert().multiply(new BigDecimal(bdZahlbetrag.signum()));
				if (BigDecimal.ZERO.compareTo(bdZahlbetrag) == 0) return;
			}

			if (bdZahlbetrag.compareTo(beleg.getOffenerBetrag()) != -1) {
				belegZahlung.setBErledigt(true);
			}
			belegZahlung.setNBetragfw(bdZahlbetrag);
			belegZahlungen.add(belegZahlung);
		}
		getResultWaitingForAuswahl().setManualPayments(belegZahlungen);
		getTabbedPane().notifyManuelleAuswahlUpdateListener();
	}
	
	private void addRowFilter(Integer columnIndex, String text) {
		String filterText = text.toLowerCase();
		if (TableModelBelegZahlung.IDX_CNR == columnIndex) {
			columnFilter.put(columnIndex, RowFilter.regexFilter("(?i)" + filterText + "$", columnIndex));
		} else {
			columnFilter.put(columnIndex, RowFilter.regexFilter("^(?i)" + filterText, columnIndex));
		}
	}
	
	private void removeRowFilter(Integer columnIndex) {
		columnFilter.remove(columnIndex);
	}
	
	protected void clearRowFilter() {
		wtFilterNummer.setText(null);
		wtFilterPartner.setText(null);
		columnFilter.clear();
		actionUpdateRowSorter();
	}
	
	private void actionUpdateRowSorter() {
		List<RowFilter<Object, Object>> rowFilter = new ArrayList<RowFilter<Object, Object>>();
		for (Entry<Integer, RowFilter<Object, Object>> entry : columnFilter.entrySet()) {
			rowFilter.add(entry.getValue());
		}
		rowSorter.setStringConverter(new TableStringConverter() {
		    @Override
		    public String toString(TableModel model, int row, int column) {
		        return model.getValueAt(row, column).toString().toLowerCase();
		    }
		});
		rowSorter.setRowFilter(RowFilter.andFilter(rowFilter));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (wtFilterPartner == e.getSource()) {
			actionFilter(TableModelBelegZahlung.IDX_PARTNER, wtFilterPartner.getText());

		} else if (wtFilterNummer == e.getSource()) {
			actionFilter(TableModelBelegZahlung.IDX_CNR, wtFilterNummer.getText());
		
		} else if (wrbRefresh == e.getSource()) {
			actionRefreshTable();
		}
	}

	private void actionRefreshTable() {
		try {
			updateTableData();
			tableModel.fireTableDataChanged();
			tableBelege.updateUI();
			
		} catch (ExceptionLP e1) {
			// TODO Auto-generated catch block
			myLogger.error("ExceptionLP", e1);
		}
	}
	
	private void actionFilter(Integer columnIndex, String text) {
		if (text == null || text.length() == 0) {
			removeRowFilter(columnIndex);
		} else {
			addRowFilter(columnIndex, text);
		}
		actionUpdateRowSorter();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		getTabbedPane().updateButtonUebernehmen();
		// Hack just for now
		int row = tableBelege.getSelectedRow();
		if ((row > -1)) tableBelege.setRowSelectionInterval(row, row);
	}

	public class TableModelBelegZahlung extends DefaultTableModel {
		
		private static final long serialVersionUID = -4282645252877540324L;

		protected static final int IDX_RECHNUNGSART = 0;
		protected static final int IDX_CNR = 1;
		protected static final int IDX_PARTNER = 2;
		protected static final int IDX_ORT = 3;
		protected static final int IDX_BELEGDATUM = 4;
		protected static final int IDX_OFFENERBETRAG = 5;
		protected static final int IDX_WAEHRUNG = 6;
		protected static final int IDX_BETRAG = 7;
		protected static final int IDX_ERLEDIGT = 8;
		
		private String[] columnNames;
		private Class<?>[] columnClasses;
		
		private SimpleDateFormat dateFormat;

		public TableModelBelegZahlung() {
			columnNames = new String[9];
			columnClasses = new Class<?>[9];
			
			columnNames[IDX_RECHNUNGSART] = LPMain.getTextRespectUISPr("fb.sepa.import.tab.head.art");
			columnNames[IDX_CNR] = LPMain.getTextRespectUISPr("fb.sepa.import.tab.head.nummer");
			columnNames[IDX_PARTNER] = getPartnerTableHead();
			columnNames[IDX_ORT] = LPMain.getTextRespectUISPr("fb.sepa.import.tab.head.ort");
			columnNames[IDX_BELEGDATUM] = LPMain.getTextRespectUISPr("fb.sepa.import.tab.head.belegdatum");
			columnNames[IDX_OFFENERBETRAG] = LPMain.getTextRespectUISPr("fb.sepa.import.tab.head.offenerbetrag");
			columnNames[IDX_WAEHRUNG] = LPMain.getTextRespectUISPr("fb.sepa.import.tab.head.waehrung");
			columnNames[IDX_BETRAG] = LPMain.getTextRespectUISPr("fb.sepa.import.tab.head.betrag");
			columnNames[IDX_ERLEDIGT] = LPMain.getTextRespectUISPr("fb.sepa.import.head.erledigt");

			columnClasses[IDX_RECHNUNGSART] = String.class;
			columnClasses[IDX_CNR] = String.class;
			columnClasses[IDX_PARTNER] = String.class;
			columnClasses[IDX_ORT] = String.class;
			columnClasses[IDX_BELEGDATUM] = String.class;
			columnClasses[IDX_OFFENERBETRAG] = String.class;
			columnClasses[IDX_WAEHRUNG] = String.class;
			columnClasses[IDX_BETRAG] = BigDecimal.class;
			columnClasses[IDX_ERLEDIGT] = Boolean.class;
			
			dateFormat = new SimpleDateFormat("dd.MM.YYYY");
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
			return belege.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			BelegAdapter beleg = belege.get(rowIndex);
			ISepaImportResult result = getResultWaitingForAuswahl();
			
			if (beleg == null || result == null) return null;

			BelegZahlungAdapter belegZahlung = getBelegZahlung(beleg.getIId(), 
					result.getManualPayments());

			switch (columnIndex) {
				case IDX_RECHNUNGSART:
					return beleg.getRechnungsartCNr() != null ? 
							beleg.getRechnungsartCNr().substring(0, 1) : null;
				case IDX_CNR:
					return beleg.getCNr();
				case IDX_PARTNER:
					return belegePartnerMap.get(beleg.getIId()).getCName1nachnamefirmazeile1();
				case IDX_ORT:
					return belegePartnerMap.get(beleg.getIId()).getLandplzortDto().getOrtDto().getCName();
				case IDX_BELEGDATUM:
					return dateFormat.format(beleg.getDBelegdatum());
				case IDX_OFFENERBETRAG:
					if (beleg.getOffenerBetrag() == null) return null;
					return Helper.formatZahl(Helper.rundeKaufmaennisch(beleg.getOffenerBetrag(), 2),
							2, LPMain.getInstance().getUISprLocale());
				case IDX_WAEHRUNG:
					return beleg.getWaehrungCNr();
				case IDX_BETRAG:
					if (belegZahlung != null) {
						return belegZahlung.getNBetragfw();
					}
				case IDX_ERLEDIGT:
					if (belegZahlung != null) {
						return belegZahlung.isBErledigt();
					}
			}
			return null;
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (String.class == columnClasses[columnIndex]) return false;
			
			if (IDX_ERLEDIGT == columnIndex 
					&& getValueAt(rowIndex, IDX_BETRAG) == null) {
				return false;
			}
			
			return true;
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			if (IDX_BETRAG == columnIndex && value instanceof String) {
				BigDecimal bdValue = value == null || ((String)value).isEmpty() ? null :
					HelperReport.toBigDecimal((String)value, LPMain.getInstance().getUISprLocale());
				actionZahlbetragChanged(bdValue, rowIndex);
			} else if (IDX_ERLEDIGT == columnIndex && value instanceof Boolean) {
				setBErledigtOfZahlung(rowIndex, (Boolean) value);
			}
		}
		
	}
	
	private void actionZahlbetragChanged(BigDecimal bdZahlbetrag, int row) {
		if (bdZahlbetrag == null) setZahlbetragOfZahlung(row, null);
		
		setZahlbetragOfZahlung(row, bdZahlbetrag);
		tabbedPane.updateNochVerfuegbarerZahlbetrag();
	}

}
