package com.lp.client.angebotstkl.webabfrage;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class ButtonCellRenderer extends DefaultTableCellRenderer  {
	private static final long serialVersionUID = 1L;

	public ButtonCellRenderer() {
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if(null == value) return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column) ; 

		if(value instanceof JButton) {
			JButton button = ((JButton) value);
			if(isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());					
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(UIManager.getColor("Button.background"));
			}
		}
		
		return value instanceof Component ?
				(Component) value : 
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column) ;
	}
}
