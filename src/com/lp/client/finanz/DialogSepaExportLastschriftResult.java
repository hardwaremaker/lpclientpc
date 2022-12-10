package com.lp.client.finanz;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.LPMain;
import com.lp.server.rechnung.service.sepa.errors.SepaException;
import com.lp.service.sepa.ISepaErrorReAction;
import com.lp.service.sepa.SepaErrorFactory;

public class DialogSepaExportLastschriftResult extends DialogSepaExportResult {
	private static final long serialVersionUID = 7172956281013109870L;
	
	private List<ISepaErrorReAction> errorActions;

	public DialogSepaExportLastschriftResult(Frame owner, List<SepaException> errors) {
		super(owner, LPMain.getTextRespectUISPr("er.zahlungsvorschlag.sepaexportfehlgeschlagen"), false);
		setErrors(errors);
		setPreferredSize(new Dimension(1000, 400));
		pack();
	}

	public void setErrors(List<SepaException> errors) {
		SepaErrorFactory errorFactory = new SepaErrorFactory();
		for (SepaException error : errors) {
			getErrors().add(errorFactory.getSepaErrorRechnungAction(error));
		}
	}
	
	public List<ISepaErrorReAction> getErrors() {
		if (errorActions == null) {
			errorActions = new ArrayList<ISepaErrorReAction>();
		}
		return errorActions;
	}
	
	@Override
	protected TableModel getTableModel() {
		return new SepaExportMessagesTableModel();
	}

	@Override
	protected void setColumnWidths(JTable table) {
		TableColumn tableColumn = table.getColumn(
				table.getColumnName(SepaExportMessagesTableModel.IDX_RE_NR));
		tableColumn.setMaxWidth(100);
		tableColumn = table.getColumn(
				table.getColumnName(SepaExportMessagesTableModel.IDX_GOTO));
		tableColumn.setMaxWidth(30);
		tableColumn.setCellRenderer(new ButtonCellRenderer());
		table.addMouseListener(new JTableButtonMouseListener(table));
	}

	public class SepaExportMessagesTableModel implements TableModel {

		protected final static int IDX_RE_NR = 0;
		protected final static int IDX_TEXT = 1;
		protected final static int IDX_GOTO = 2;
		
		private String[] columnNames;
		
		public String[] getColumnNames() {
			if (columnNames == null) {
				columnNames = new String[3];
				columnNames[IDX_RE_NR] = LPMain.getTextRespectUISPr(
						"rechnung.lastschriftvorschlag.sepaexport.head.rechnungnr");
				columnNames[IDX_TEXT] = LPMain.getTextRespectUISPr(
						"rechnung.lastschriftvorschlag.sepaexport.head.text");
				columnNames[IDX_GOTO] = LPMain.getTextRespectUISPr(
						"rechnung.lastschriftvorschlag.sepaexport.head.goto");
			}
			return columnNames;
		}
		
		@Override
		public void addTableModelListener(TableModelListener l) {
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (IDX_GOTO == columnIndex) return JPanel.class;
			
			return String.class;
		}

		@Override
		public int getColumnCount() {
			return getColumnNames().length;
		}

		@Override
		public String getColumnName(int column) {
			return getColumnNames()[column];
		}

		@Override
		public int getRowCount() {
			return getErrors().size();
		}

		@Override
		public Object getValueAt(int row, int column) {
			switch (column) {
				case IDX_RE_NR:
					return getRechnungsnummer(row);
				case IDX_TEXT:
					return getText(row);
				case IDX_GOTO:
					return getGoto(row);
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

	public String getRechnungsnummer(int row) {
		ISepaErrorReAction errorAction = getErrors().get(row);
		return errorAction.hasRechnungDto() ? errorAction.getRechnungDto().getCNr() : "";
	}

	public WrapperGotoButton getGoto(int row) {
		return getErrors().get(row).getGotoButton();
	}

	public String getText(int row) {
		return getErrors().get(row).getMessage();
	}

	public class ButtonCellRenderer extends DefaultTableCellRenderer  {
		private static final long serialVersionUID = 1L;

		public ButtonCellRenderer() {
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if(null == value) return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column) ; 

			if(value instanceof WrapperGotoButton) {
				WrapperGotoButton button = (WrapperGotoButton) value;
				if(isSelected) {
					button.setForeground(table.getSelectionForeground());
					button.setBackground(table.getSelectionBackground());					
				} else {
					button.setForeground(table.getForeground());
					button.setBackground(table.getBackground());
				}
			}
			
			return value instanceof Component ?
					(Component) value : 
					super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column) ;
		}
	}


	public class JTableButtonMouseListener extends MouseAdapter {
		private final JTable table;

		public JTableButtonMouseListener(JTable table) {
			this.table = table;
		}

		@Override public void mouseClicked(MouseEvent e) {
			int column = table.getColumnModel().getColumnIndexAtX(e.getX());
			int row    = e.getY()/table.getRowHeight(); 

			if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
				Object value = table.getValueAt(row, column);
				if (value instanceof WrapperGotoButton) {
					ActionEvent action  = new ActionEvent(this, 0, "ACTION_GOTO") ;
					((WrapperGotoButton)value).actionPerformed(action);
				}
			}
		}		
	}
}
