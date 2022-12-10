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

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import com.lp.client.finanz.sepaimportassistent.SepaImportModel.SearchResultTableModel;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.ISepaImportResult;
import com.lp.server.util.BelegAdapter;
import com.lp.util.Helper;

public class SearchResultTableCellRenderer extends JLabel implements
		TableCellRenderer {

	private static final long serialVersionUID = 1L;
	
	private static final Color COLOR_GREEN = new Color(150, 255, 150);
	private static final Color COLOR_ORANGE = new Color(255, 140, 0);
	private static final Color COLOR_RED = new Color(238, 64, 0);
	private static final Color COLOR_EDITED = new Color(64, 224, 208);
	
	private Border paddingBorder = BorderFactory.createEmptyBorder(0, 2, 0, 2);
	private SepaImportPage4Ctrl controller;

	public SearchResultTableCellRenderer(SepaImportPage4Ctrl controller) {
		this.controller = controller;
	}

	protected static Color getBackgroundColor(JTable table, int row) {
		
		if (table.getModel() instanceof SearchResultTableModel) {
			ISepaImportResult result = ((SearchResultTableModel) table.getModel()).getResultAtRow(row);
			
			if (result.isEditedByUser()) {
				return COLOR_EDITED;
			}
			
			int size = result.getFoundItems().size();
			if (size == 2 && result.getTotalMatch()) { // ein Treffer plus MANUELLE_AUSWAHL
				return COLOR_GREEN;
			} else if (size > 1) {
				return COLOR_ORANGE;
			}
			return COLOR_RED;
		}
		return Color.WHITE;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Color bgColor = getBackgroundColor(table, row);
		
		if (SearchResultTableModel.IDX_ZAHLUNG == column) {
			WrapperTextArea wtaZahlung = new WrapperTextArea();
			wtaZahlung.setText(getValue(value));
			wtaZahlung.setEditable(false);
			wtaZahlung.setBackground(bgColor);
			wtaZahlung.setForeground(HelperClient.getContrastYIQ(bgColor));
			int fontHeight = wtaZahlung.getFontMetrics(wtaZahlung.getFont()).getHeight();
			table.setRowHeight(fontHeight * 2 + 5);
			return wtaZahlung;
		}
		setOpaque(true);
		setBackground(bgColor);
		setForeground(HelperClient.getContrastYIQ(bgColor));
		setText(getValue(value));
		setHorizontalAlignment(getAlignment(value));
		
		setBorder(paddingBorder);
		
		return this;
	}

	private String getValue(Object value) {
		if (value == null) return null;
		
		if (value instanceof ISepaImportResult) {
			ISepaImportResult result = (ISepaImportResult) value;
			
			if(result.getSelectedIndex() == null || result.getSelectedIndex() == -1) {
				return null;
			}
			
			BelegAdapter beleg = result.getFoundItems().get(result.getSelectedIndex());
			if (SepaImportPage4Ctrl.MANUELLE_AUSWAHL == beleg) {
				return LPMain.getTextRespectUISPr("fb.sepa.import.manuelleauswahl");
			}
			return SepaImportPage4Ctrl.getTextForBelegAdapterItem(beleg);
		}
		
		if (value instanceof BigDecimal) {
			return Helper.formatZahl(Helper.rundeKaufmaennisch((BigDecimal) value, 2),
					2, LPMain.getInstance().getUISprLocale()) + " " + controller.getWaehrung();		
		}
		
		if (value instanceof MultilineText) {
			return ((MultilineText)value).asString();
		}
		
		return value.toString();
	}

	private int getAlignment(Object value) {
		if (value instanceof BigDecimal) {
			return JLabel.RIGHT;
		}
		return JLabel.LEFT;
	}

}
