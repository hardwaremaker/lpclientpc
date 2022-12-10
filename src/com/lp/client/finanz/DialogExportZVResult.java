package com.lp.client.finanz;

import java.awt.Frame;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.lp.client.pc.LPMain;

public abstract class DialogExportZVResult extends DialogSepaExportResult {
	private static final long serialVersionUID = 3671988363563827032L;

	public DialogExportZVResult(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	@Override
	protected TableModel getTableModel() {
		return new SepaExportMessagesTableModel();
	}
	
	@Override
	protected void setColumnWidths(JTable table) {
		TableColumn tableColumn = table.getColumn(
				table.getColumnName(SepaExportMessagesTableModel.IDX_FEHLERCODE));
		tableColumn.setMaxWidth(60);
	}
	
	protected abstract String getExceptionMessage(int rowIndex);
	
	protected abstract Integer getExceptionCode(int rowIndex);
	
	protected abstract Integer getMessagesCount();
	
	public class SepaExportMessagesTableModel implements TableModel {
		
		protected static final int IDX_FEHLERCODE = 0;
		protected static final int IDX_TEXT = 1;
		
		private String[] columnNames;

		public SepaExportMessagesTableModel() {
			columnNames = new String[2];
			
			columnNames[IDX_FEHLERCODE] = LPMain.getTextRespectUISPr("er.zahlungsvorschlag.sepaexport.head.fehlercode");
			columnNames[IDX_TEXT] = LPMain.getTextRespectUISPr("er.zahlungsvorschlag.sepaexport.head.text");
		}
		
		@Override
		public void addTableModelListener(TableModelListener l) {
			
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
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
			return getMessagesCount();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {

			switch (columnIndex) {
				case IDX_FEHLERCODE:
					return getExceptionCode(rowIndex);
				case IDX_TEXT:
					return getExceptionMessage(rowIndex);
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
}
