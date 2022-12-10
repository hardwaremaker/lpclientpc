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
 package com.lp.client.stueckliste.importassistent;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import com.lp.client.frame.HelperClient;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.Helper;

public class ResultTableCellRenderer extends JLabel implements TableCellRenderer{
	
	private static final long serialVersionUID = 2439960570760252531L;
	
	private static final Color PRICE_COLOR_WITHIN_10_PERCENT = new Color(220, 140, 0);
	private static final Color PRICE_COLOR_MORE_THAN_10_PERCENT = new Color(230, 0, 0);
	
	private NumberFormat format;
	private Border paddingBorder = BorderFactory.createEmptyBorder(0, 2, 0, 2);
	private StklImportPage3Ctrl ctrl;
	
	public ResultTableCellRenderer(StklImportPage3Ctrl ctrl) {
		super();
		this.ctrl = ctrl;
	}

	protected Color getBackgroundColor(JTable table, int row) {
		if(table.getModel() instanceof ResultTableModel) {
			IStklImportResult result = ((ResultTableModel)table.getModel()).getResultAtRow(row);
			if(result.isTotalMatch())
				return new Color(150, 255, 150);
			if(result.getSelectedIndex() != null && result.getSelectedIndex() != -1)
				return Color.white;
			if(result.getFoundItems().size() > (ctrl.isDemo() ? 3 : 2))
				return new Color(255, 255, 125);
			return new Color(255, 150, 150);
		}
		return Color.white;
	}
	
	protected String getValue(Object value) {
		if(value == null) return null;
		if(value instanceof BigDecimal) {
			BigDecimal bd = (BigDecimal) value;
			return getFormat().format(bd.doubleValue());
		}
		return value.toString();
	}
	
	private NumberFormat getFormat() {
		if(format == null) {
			try {
				format = DecimalFormat.getInstance(LPMain.getTheClient().getLocUi());
			} catch (Throwable e) {
				format = DecimalFormat.getInstance();
			}
		}
		return format;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Color bg = getBackgroundColor(table, row);
		setOpaque(true);
		setBackground(bg);
		setForeground(HelperClient.getContrastYIQ(bg));
		setText(getValue(value));
		setHorizontalAlignment(JLabel.LEFT);
		setPreiseTextColorAndAlignment(table, row, column);
		
		if(table.getEditingRow() == row)
			setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.black), paddingBorder));
		else
			setBorder(paddingBorder);
		return this;
	}

	private void setPreiseTextColorAndAlignment(JTable table, int row, int column) {
		if(table.getModel() instanceof ResultTableModelEinkauf) {
			ResultTableModelEinkauf tableModel = ((ResultTableModelEinkauf)table.getModel());
			IStklImportResult result = tableModel.getResultAtRow(row);	
			
			if(result.getValues().get(StklImportSpezifikation.BESTELLPREIS) != null 
					&& result.getValues().get(StklImportSpezifikation.LIEFPREIS) != null) {
				if(tableModel.getIndexOfAfterMengeColumn(ResultTableModelEinkauf.IDX_LIEFPREIS) == column) {

					BigDecimal bestellpreis = Helper.toBigDecimal(result.getValues().get(StklImportSpezifikation.BESTELLPREIS));
					BigDecimal liefpreis = Helper.toBigDecimal(result.getValues().get(StklImportSpezifikation.LIEFPREIS));
	
					if(liefpreis.compareTo(bestellpreis) < 0) {
						if(Helper.calculateRatioInDecimal(bestellpreis, liefpreis).compareTo(new BigDecimal(10)) < 0) {
							setForeground(PRICE_COLOR_WITHIN_10_PERCENT);
						} else {
							setForeground(PRICE_COLOR_MORE_THAN_10_PERCENT);
						}
					}
				}
			}
			if(result.getValues().get(StklImportSpezifikation.BESTELLPREIS) != null 
					&& tableModel.getIndexOfAfterMengeColumn(ResultTableModelEinkauf.IDX_ARTIKELBESTELLPREIS) == column
				|| result.getValues().get(StklImportSpezifikation.LIEFPREIS) != null 
					&& tableModel.getIndexOfAfterMengeColumn(ResultTableModelEinkauf.IDX_LIEFPREIS) == column) {
				setHorizontalAlignment(JLabel.RIGHT);
			}
		}
		
	}

}
