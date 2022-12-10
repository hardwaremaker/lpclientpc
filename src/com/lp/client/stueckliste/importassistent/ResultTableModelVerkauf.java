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

public class ResultTableModelVerkauf extends ResultTableModel {

	public ResultTableModelVerkauf(StklImportPage3Ctrl controller,
			List<IStklImportResult> list, List<String> columnTypes) {
		super(controller, list, columnTypes);
	}

	@Override
	public int getBezugsobjektArtikelnummerIndex() {
		return columnTypes.indexOf(StklImportSpezifikation.KUNDENARTIKELNUMMER);
	}
	
	@Override
	/**
	 * Gibt an, ob eine Zelle in der Tabelle editierbar ist.
	 * Dies ist der Fall, wenn es sich um eine Auswahl der Artikel
	 * oder wenn es sich um eine Zelle f&uuml;r die Selektierung des 
	 * Soko Updates handelt, aber nur im Falle, dass das aktuelle Result 
	 * kein TotalMatch ist, f&uuml;r dieses Result kein Handartikel ausgew&auml;hlt
	 * wurde, oder die Kunden- oder Lieferantenartikelnummer nicht vorhanden ist.
	 * 
	 * @param rowIndex, Zeilennummer
	 * @param columnIndex, Spaltennummer
	 * @return true, wenn die Zelle editierbar ist
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		IStklImportResult artikel = 
				(IStklImportResult) getValueAt(rowIndex, getIndexOfAfterMengeColumn(IDX_ARTIKELCHOOSER));
		String boArtikelnummer = bezugsobjektArtikelnummerColumnTypeExists() ? 
				(String) getValueAt(rowIndex, getBezugsobjektArtikelnummerIndex()) : null;
		
		return columnIndex == getIndexOfAfterMengeColumn(IDX_ARTIKELCHOOSER) 
				|| (columnIndex == getIndexOfAfterMengeColumn(IDX_SOKOUPDATE_CB) 
					&& !(artikel.getSelectedArtikelDto() == StklImportPage3Ctrl.HANDARTIKEL
						|| boArtikelnummer == null || boArtikelnummer.isEmpty()));
	}

	@Override
	/**
	 * Liefert die Anzahl der Spalten der Tabelle
	 * H&auml;ngt davon ab, ob die SokoUpdate Spalte angezeigt werden soll. Existiert
	 * keine Spalte f&uuml;r das Mapping (Kundenartikelnummer oder 
	 * Lieferantenartikelnummer) oder sind alle Eintr&auml;ge TotalMatches oder
	 * Handartikel (SokoUpdateCheckbox ist in diesem Fall auf <code>DISABLE</code>)
	 * dann ist die letzte (rechte) Spalte jene des ArtikelChoosers.
	 * 
	 * @return Anzahl der Spalten der Tabelle
	 */
	public int getColumnCount() {
		return controller.getMappingUpdateStatus() != WrapperTristateCheckbox.DISABLE
				? getIndexOfAfterMengeColumn(IDX_SOKOUPDATE_CB) + 1 
						: getIndexOfAfterMengeColumn(IDX_ARTIKELCHOOSER) + 1;
	}

	@Override
	public String getColumnName(int i) {
		if(i == getIndexOfAfterMengeColumn(IDX_SOKOUPDATE_CB))
			return LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.sokoupdate.spaltenkopf");
		
		return super.getColumnName(i);
	}

	@Override
	public Class<?> getColumnClass(int i) {
		if(i == getIndexOfAfterMengeColumn(IDX_ARTIKELCHOOSER))
			return IStklImportResult.class;
		if(i == getIndexOfAfterMengeColumn(IDX_SOKOUPDATE_CB))
			return Boolean.class;
		return String.class;
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
		if(columnIndex == getIndexOfAfterMengeColumn(IDX_SOKOUPDATE_CB))
			return result.isUpdateArtikelnummerMapping();
		return null;
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if(columnIndex != getIndexOfAfterMengeColumn(IDX_ARTIKELCHOOSER) 
				&& columnIndex != getIndexOfAfterMengeColumn(IDX_SOKOUPDATE_CB)) {
			return;
		}
		
		if (value instanceof Boolean) {
			list.get(rowIndex).setUpdateArtikelnummerMapping((Boolean)value);
			controller.tableModelValueChanged();
		}
		
		super.setValueAt(value, rowIndex, columnIndex);
	}
	
}
