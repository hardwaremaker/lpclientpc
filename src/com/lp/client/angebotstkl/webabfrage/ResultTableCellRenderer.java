package com.lp.client.angebotstkl.webabfrage;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.lp.client.frame.HelperClient;
import com.lp.client.pc.LPMain;
import com.lp.util.Helper;

public class ResultTableCellRenderer extends JLabel implements
		TableCellRenderer {

	private static final long serialVersionUID = -4783150422782916197L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Color bg = Color.white;
		if (!isSelected && table.getModel() instanceof WebabfrageResultTableModel) {
			IWebabfrageResult result = ((WebabfrageResultTableModel)table.getModel()).getResultAtRow(row);
			bg = result.getState().getBackgroundColor();
		}
		
		setOpaque(true);
		setBackground(bg);
		setForeground(HelperClient.getContrastYIQ(bg));
		setText(getValue(value));
		
		return this;
	}

	private String getValue(Object value) {
		if (value == null) return null;
		
		if (value instanceof BigDecimal) {
			return Helper.formatZahl(Helper.rundeKaufmaennisch((BigDecimal) value, 2),
					2, LPMain.getInstance().getUISprLocale());
		}
		
		return value.toString();
	}	
}
