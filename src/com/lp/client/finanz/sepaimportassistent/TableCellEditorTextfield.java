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

import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.util.Helper;
import com.lp.util.HelperReport;

public class TableCellEditorTextfield extends AbstractCellEditor implements
		TableCellEditor {

	private static final long serialVersionUID = 5287955022235952969L;

	private JTextField textField;
	
	public TableCellEditorTextfield() {
		super();
		textField = new JTextField();
	}
	
	@Override
	public Object getCellEditorValue() {
		if (textField.getText() != null && !textField.getText().isEmpty()) {
			BigDecimal value = HelperReport.toBigDecimal(textField.getText(), LPMain.getInstance().getUISprLocale());
			textField.setText(Helper.formatZahl(Helper.rundeKaufmaennisch((BigDecimal)value, FinanzFac.NACHKOMMASTELLEN), 
					FinanzFac.NACHKOMMASTELLEN, LPMain.getInstance().getUISprLocale()));
		}
		
		return textField.getText();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		String text = null;
		if (value != null && value instanceof BigDecimal) {
			text = Helper.formatZahl(Helper.rundeKaufmaennisch((BigDecimal)value, FinanzFac.NACHKOMMASTELLEN), 
					FinanzFac.NACHKOMMASTELLEN, LPMain.getInstance().getUISprLocale());
		}
		textField.setText(text);
		
		return textField;
	}

}
