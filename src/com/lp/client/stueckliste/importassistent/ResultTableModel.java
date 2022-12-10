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

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.service.StklImportSpezifikation;

public abstract class ResultTableModel implements TableModel {
	
	protected List<IStklImportResult> list;
	protected List<String> columnTypes;
	
	private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	protected StklImportPage3Ctrl controller;
	
	/**
	 * Reihenfolge der Spalten nach der Spalte "Menge"
	 */
	protected static final int IDX_ARTIKELCHOOSER = 1;
	protected static final int IDX_ARTIKELBESTELLPREIS = 2;
	protected static final int IDX_LIEFPREIS = 3;
	protected static final int IDX_PREISUEBERNAHME_CB = 4;
	protected static final int IDX_ARTIKELLIEFERANTUPDATE_CB = 5;
	
	protected static final int IDX_SOKOUPDATE_CB = 2;

	/**
	 * Erzeugt das Model f&uuml;r die R&uuml;ckgabedaten der Artikelsuche beim Intelligenten Stkl. Import.
	 * Die drei letzten (rechtesten) Spalten zeigen immer die Menge, die Artikelauswahl und das Soko Update an.
	 * @param controller der Controller
	 * @param list die Liste der vom Server gelieferten <code>StklImportResult</code>s
	 * @param columnTypes die Spalten die angezeigt werden sollen.
	 */
	public ResultTableModel(StklImportPage3Ctrl controller, List<IStklImportResult> list, List<String> columnTypes) {
		if(controller == null) throw new NullPointerException("controller == null");
		if(list == null) throw new NullPointerException("list == null");
		if(columnTypes == null) throw new NullPointerException("columnTypes == null");
		this.controller = controller;
		this.columnTypes = new ArrayList<String>(columnTypes);
		this.columnTypes.remove(StklImportSpezifikation.MENGE);
		while(this.columnTypes.contains(StklImportSpezifikation.UNDEFINED))
			this.columnTypes.remove(StklImportSpezifikation.UNDEFINED);
		this.list = list;
	}

	@Override
	public void addTableModelListener(TableModelListener listener) {
		if(!listeners.contains(listener))
			listeners.add(listener);
	}

	abstract public int getBezugsobjektArtikelnummerIndex();
	
	protected int getMengeIndex() {
		return columnTypes.size();
	}

	protected int getIndexOfAfterMengeColumn(int which) {
		return getMengeIndex() + which;
	}

	@Override
	public String getColumnName(int i) {
		if(i < columnTypes.size())
			return columnTypes.get(i);
		if(i == getMengeIndex())
			return LPMain.getTextRespectUISPr("lp.menge");
		if(i == getIndexOfAfterMengeColumn(IDX_ARTIKELCHOOSER))
			return LPMain.getTextRespectUISPr("lp.artikel");
		return "";
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public void removeTableModelListener(TableModelListener listener) {
		listeners.remove(listener);
	}

	public IStklImportResult getResultAtRow(int row) {
		return list.get(row);
	}
	
	/**
	 * Gibt an, ob die Spalte f&uuml;r das Mapping (Kundeartikelnummer
	 * oder Lieferantenartikelnummer) in der Tabelle vorhanden ist.
	 * 
	 * @return true, wenn die Kundenartikelnummer-Spalte vorhanden ist
	 */
	public boolean bezugsobjektArtikelnummerColumnTypeExists() {
		return getBezugsobjektArtikelnummerIndex() < 0 ? false : true;
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if(value  instanceof IStklImportResult) {
			IStklImportResult result = (IStklImportResult) value;
			if(result.getSelectedArtikelDto() == StklImportPage3Ctrl.FLR_LISTE
					|| result.getSelectedArtikelDto() == StklImportPage3Ctrl.ZUVIELE_ARTIKEL_FLR_LISTE) {
				list.get(rowIndex).setSelectedIndex(-1);
				controller.setResultWaitingForArtikelIId(result);
			} else {
				list.get(rowIndex).setSelectedIndex(result.getSelectedIndex());
				controller.tableModelValueChanged();
			}
		}
	}

}
