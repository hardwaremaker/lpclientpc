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

import java.util.List;

import com.lp.client.frame.component.WrapperTristateCheckbox;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.service.StklImportSpezifikation;

public class ResultTableModelEinkauf extends ResultTableModel {

	protected static final int IDX_ARTIKELLIEFERANTUPDATE_NO_PRICE_CB = 2;
	private boolean bHasArtikelbestellpreis = false;
	
	public ResultTableModelEinkauf(StklImportPage3Ctrl controller,
			List<IStklImportResult> list, List<String> columnTypes) {
		super(controller, list, columnTypes);
		
		if(this.columnTypes.contains(StklImportSpezifikation.BESTELLPREIS)) {
			this.columnTypes.remove(StklImportSpezifikation.BESTELLPREIS);
			bHasArtikelbestellpreis = true;
		}
	}

	@Override
	public Class<?> getColumnClass(int i) {
		if(i == getIndexOfAfterMengeColumn(IDX_ARTIKELCHOOSER)) {
			return IStklImportResult.class;
		}
		if(bHasArtikelbestellpreis) {
			if(i == getIndexOfAfterMengeColumn(IDX_ARTIKELLIEFERANTUPDATE_CB) 
				|| i == getIndexOfAfterMengeColumn(IDX_PREISUEBERNAHME_CB)) {
				return Boolean.class;
			}
		} else {
			if(i == getIndexOfAfterMengeColumn(IDX_ARTIKELLIEFERANTUPDATE_NO_PRICE_CB))
				return Boolean.class;
		}
		return String.class;
	}

	@Override
	public int getColumnCount() {
		int cbUpdateIndex = bHasArtikelbestellpreis 
				? IDX_ARTIKELLIEFERANTUPDATE_CB : IDX_ARTIKELLIEFERANTUPDATE_NO_PRICE_CB;
		
		if(controller.getMappingUpdateStatus() != WrapperTristateCheckbox.DISABLE)
			return getIndexOfAfterMengeColumn(cbUpdateIndex) + 1;
		else 
			return getIndexOfAfterMengeColumn(cbUpdateIndex);
		
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		IStklImportResult result = list.get(rowIndex);
		
		if(result.getValues() == null) return null;
		if(columnTypes.size() > columnIndex)
			return result.getValues().get(columnTypes.get(columnIndex));
		if(columnIndex == getMengeIndex())
			return result.getValues().get(StklImportSpezifikation.MENGE);
		if(columnIndex == getIndexOfAfterMengeColumn(IDX_ARTIKELCHOOSER))
			return result;
		
		if(bHasArtikelbestellpreis) {
			if(columnIndex == getIndexOfAfterMengeColumn(IDX_ARTIKELBESTELLPREIS)) 
				return result.getValues().get(StklImportSpezifikation.BESTELLPREIS);
			if(columnIndex == getIndexOfAfterMengeColumn(IDX_LIEFPREIS)) {
				return result.getValues().get(StklImportSpezifikation.LIEFPREIS);
			}
			if(columnIndex == getIndexOfAfterMengeColumn(IDX_PREISUEBERNAHME_CB)) {
				if(result.getValues().get(StklImportSpezifikation.LIEFPREIS) != null) {
					return list.get(rowIndex).uebernehmeLiefPreisInBestellung();
				}
				return false;
			}
			if(columnIndex == getIndexOfAfterMengeColumn(IDX_ARTIKELLIEFERANTUPDATE_CB))
				return result.isUpdateArtikelnummerMapping();
		} else {
			if(columnIndex == getIndexOfAfterMengeColumn(IDX_ARTIKELLIEFERANTUPDATE_NO_PRICE_CB))
				return result.isUpdateArtikelnummerMapping();
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		IStklImportResult artikel = 
				(IStklImportResult) getValueAt(rowIndex, getIndexOfAfterMengeColumn(IDX_ARTIKELCHOOSER));
		String boArtikelnummer = bezugsobjektArtikelnummerColumnTypeExists() ? 
				(String) getValueAt(rowIndex, getBezugsobjektArtikelnummerIndex()) : null;
		int cbUpdateIndex = bHasArtikelbestellpreis 
				? IDX_ARTIKELLIEFERANTUPDATE_CB : IDX_ARTIKELLIEFERANTUPDATE_NO_PRICE_CB;
		
		if(bHasArtikelbestellpreis 
				&& columnIndex == getIndexOfAfterMengeColumn(IDX_PREISUEBERNAHME_CB)) {
			return artikel.getValues().containsKey(StklImportSpezifikation.LIEFPREIS)
					&& artikel.getValues().get(StklImportSpezifikation.LIEFPREIS) == null ? false : true;
		}
		
		return columnIndex == getIndexOfAfterMengeColumn(IDX_ARTIKELCHOOSER) 
				|| (columnIndex == getIndexOfAfterMengeColumn(cbUpdateIndex) 
					&& !(artikel.getSelectedArtikelDto() == StklImportPage3Ctrl.HANDARTIKEL
						|| boArtikelnummer == null || boArtikelnummer.isEmpty()));
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if(columnIndex != getIndexOfAfterMengeColumn(IDX_ARTIKELCHOOSER) 
				&& !bHasArtikelbestellpreis 
				&& columnIndex != getIndexOfAfterMengeColumn(IDX_ARTIKELLIEFERANTUPDATE_NO_PRICE_CB)) {
			return;
		}
		
		if(columnIndex != getIndexOfAfterMengeColumn(IDX_ARTIKELCHOOSER) 
				&& bHasArtikelbestellpreis && columnIndex != getIndexOfAfterMengeColumn(IDX_PREISUEBERNAHME_CB)
				&& columnIndex != getIndexOfAfterMengeColumn(IDX_ARTIKELLIEFERANTUPDATE_CB)) {
			return;
		}
		
		if (value instanceof Boolean) {
			int cbUpdateIndex = bHasArtikelbestellpreis 
					? getIndexOfAfterMengeColumn(IDX_ARTIKELLIEFERANTUPDATE_CB) 
					: getIndexOfAfterMengeColumn(IDX_ARTIKELLIEFERANTUPDATE_NO_PRICE_CB);
			if(columnIndex == cbUpdateIndex) {
				list.get(rowIndex).setUpdateArtikelnummerMapping((Boolean)value);
				controller.tableModelValueChanged();
			} else if(bHasArtikelbestellpreis && columnIndex == getIndexOfAfterMengeColumn(IDX_PREISUEBERNAHME_CB)) {
				list.get(rowIndex).setUebernehmeLiefPreisInBestellung((Boolean)value);
				controller.tableModelValueChanged();
			}
		}
		
		super.setValueAt(value, rowIndex, columnIndex);
	}

	@Override
	public int getBezugsobjektArtikelnummerIndex() {
		return columnTypes.indexOf(StklImportSpezifikation.LIEFERANTENARTIKELNUMMER);
	}
	
	@Override
	public String getColumnName(int i) {
		if(bHasArtikelbestellpreis) {
			if(i == getIndexOfAfterMengeColumn(IDX_ARTIKELBESTELLPREIS))
				return LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.importpreis");
			
			if(i == getIndexOfAfterMengeColumn(IDX_LIEFPREIS))
				return LPMain.getTextRespectUISPr("artikel.report.lieferantenpreis");
			
			if(i == getIndexOfAfterMengeColumn(IDX_PREISUEBERNAHME_CB))
				return LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.liefpreisuebernehmen");
				
			if(i == getIndexOfAfterMengeColumn(IDX_ARTIKELLIEFERANTUPDATE_CB))
				return LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.artikellieferantupdate.spaltenkopf");
		} else {
			if(i == getIndexOfAfterMengeColumn(IDX_ARTIKELLIEFERANTUPDATE_NO_PRICE_CB))
				return LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.artikellieferantupdate.spaltenkopf");
		}
		
		return super.getColumnName(i);
	}
	
	public boolean hasArtikelBestellpreis() {
		return bHasArtikelbestellpreis;
	}

}
