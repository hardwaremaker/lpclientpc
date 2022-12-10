package com.lp.client.finanz.sepaimportassistent;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.lp.client.frame.component.WrapperTextArea;

public class MultilineTextTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 6758783374052706369L;
	private int lineLength;

	public MultilineTextTableCellRenderer() {
		this(-1);
	}
	
	public MultilineTextTableCellRenderer(int lineLength) {
		setLineLength(lineLength);
	}

	public void setLineLength(int lineLength) {
		this.lineLength = lineLength;
	}
	
	public int getLineLength() {
		return lineLength;
	}
	
	private String getMultilineText(MultilineText text) {
		if (getLineLength() < 0) {
			return text.asString();
		}
		return text.asString(getLineLength());
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		MultilineText multilineText = (MultilineText)value;
		
		WrapperTextArea wtaZahlung = new WrapperTextArea();
		wtaZahlung.setText(getMultilineText(multilineText));
		wtaZahlung.setEditable(false);
		wtaZahlung.setBackground(table.isRowSelected(row) ? table.getSelectionBackground() : Color.WHITE);
		int fontHeight = wtaZahlung.getFontMetrics(wtaZahlung.getFont()).getHeight();
		if (multilineText.getLines().size() > 0) {
			table.setRowHeight(fontHeight * 2 + 5);
		}
		
		return wtaZahlung;
	}
}
