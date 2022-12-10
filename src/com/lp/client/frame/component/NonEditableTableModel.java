package com.lp.client.frame.component;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author Alexander Daum
 *
 */
public class NonEditableTableModel extends DefaultTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4753603552216512982L;


	
	public NonEditableTableModel() {
		super();
	}



	public NonEditableTableModel(int rowCount, int columnCount) {
		super(rowCount, columnCount);
	}



	public NonEditableTableModel(Object[] columnNames, int rowCount) {
		super(columnNames, rowCount);
	}



	public NonEditableTableModel(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
	}



	public NonEditableTableModel(Vector<?> columnNames, int rowCount) {
		super(columnNames, rowCount);
	}



	public NonEditableTableModel(Vector<? extends Vector<?>> data, Vector<?> columnNames) {
		super(data, columnNames);
	}



	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
}
