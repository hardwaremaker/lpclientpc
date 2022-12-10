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

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import com.lp.client.finanz.sepaimportassistent.SepaImportModel.SearchResultTableModel;

public class SearchResultTableCellRendererCheckbox implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
	private JCheckBox cbUebernahme;
	private JCheckBox cbIgnorieren;
	
	public SearchResultTableCellRendererCheckbox() {
		cbUebernahme = new JCheckBox();
		cbUebernahme.setHorizontalAlignment(SwingConstants.CENTER);
		cbIgnorieren = new JCheckBox();
		cbIgnorieren.setHorizontalAlignment(SwingConstants.CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Color bgColor = SearchResultTableCellRenderer.getBackgroundColor(table, row);
		JComponent retComponent;
		
		if (table.isCellEditable(row, column)) {
			JCheckBox current = column == SearchResultTableModel.IDX_IGNORIEREN
					? cbIgnorieren : cbUebernahme;
			current.setSelected((Boolean) value);
			retComponent = current;
		} else {
			retComponent = new JPanel();
		}
		
		retComponent.setOpaque(true);
		retComponent.setBackground(bgColor);
		
		return retComponent;
	}

}
