package com.lp.client.finanz.sepaimportassistent;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.lp.client.pc.LPMain;

public class StatementOverviewTableModel implements TableModel {

	public class Columns {
		public static final int NAME = 0;
		public static final int IBAN = 1;
		public static final int ZAHLDATUM = 2;
		public static final int BETRAG = 3;
		public static final int REFERENZTEXT = 4;
	}
	
	private String[] columnNames;
	private List<StatementOverviewTableEntry> entries;
	
	public StatementOverviewTableModel() {
		columnNames = new String[5];
		columnNames[Columns.NAME] = LPMain.getTextRespectUISPr("fb.sepa.import.head.name");
		columnNames[Columns.IBAN] = LPMain.getTextRespectUISPr("fb.sepa.import.head.iban");
		columnNames[Columns.ZAHLDATUM] = LPMain.getTextRespectUISPr("fb.sepa.import.head.zahldatum");
		columnNames[Columns.BETRAG] = LPMain.getTextRespectUISPr("fb.sepa.import.head.betrag");
		columnNames[Columns.REFERENZTEXT] = LPMain.getTextRespectUISPr("fb.sepa.import.head.referenztext");
	}
	
	public void setEntries(List<StatementOverviewTableEntry> entries) {
		this.entries = entries;
	}

	private List<StatementOverviewTableEntry> getItems() {
		if (entries == null) {
			entries = new ArrayList<StatementOverviewTableEntry>();
		}
		return entries;
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (Columns.REFERENZTEXT == columnIndex) 
			return MultilineText.class;
		
		return String.class;
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
		return getItems().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		StatementOverviewTableEntry item = getItems().get(rowIndex);
		
		switch (columnIndex) {
			case Columns.NAME: 
				return item.getName();
			case Columns.IBAN: 
				return item.getIban();
			case Columns.ZAHLDATUM:
				return item.getZahldatum();
			case Columns.BETRAG:
				return item.getBetrag();
			case Columns.REFERENZTEXT:
				return item.getReferenztext();
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
