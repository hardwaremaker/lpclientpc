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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.lp.client.frame.component.WrapperTristateCheckbox;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.stueckliste.service.CondensedResultList;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.Helper;

public class StklImportPage3CtrlEinkauf extends StklImportPage3Ctrl {

	private int cbPreisuebernahme = WrapperTristateCheckbox.DESELECTED;

	public StklImportPage3CtrlEinkauf(StklImportModel model) {
		super(model);
	}

	@Override
	public TableModel getResultTableModel() {
		if(model.getResults() != null) {
			List<IStklImportResult> results = zusammengefasst ?
					new CondensedResultList(model.getResults()) : model.getResults();
			return new ResultTableModelEinkauf(
					this, results, model.getSelectedSpezifikation().getColumnTypes());
		}
		return new DefaultTableModel();
	}

	/**
	 * Berechnet den Status der Tristate Checkbox der Preis&uuml;bernahme von
	 * Lieferantenpreisen
	 * 
	 * @return Status der &Uuml;bernahme von Liefpreisen in der Tabelle:
	 * 	SELECTED, wenn alle auf true sind;
	 * 	DESELECTED, wenn alle auf false sind;
	 * 	HALFSELECTED, wenn sich welche unterscheiden;
	 * 	DISABLE, wenn keine Imports geladen oder von keinem Result Liefpreise
	 * vorhanden sind.
	 */
	public void setPreisuebernahmeStatusAusStklImportResults() {
		TableModel resultTableModel = getResultTableModel();
		if(resultTableModel instanceof ResultTableModelEinkauf
				&& ((ResultTableModelEinkauf) resultTableModel).hasArtikelBestellpreis()
				&& model.getResults() != null) {
			IStklImportResult lastStklImportResult = null;
			Iterator<IStklImportResult> iter = model.getResults().iterator();

			while(iter.hasNext()) {
				IStklImportResult res = iter.next();
				if((res.getSelectedArtikelDto() != null 
						&& ArtikelFac.ARTIKELART_HANDARTIKEL.equals(res.getSelectedArtikelDto().getArtikelartCNr()))
						|| res.getValues().get(StklImportSpezifikation.LIEFPREIS) == null
						|| res.getValues().get(StklImportSpezifikation.LIEFPREIS).isEmpty()) {
					continue;
				}
				if(lastStklImportResult != null) {
					if(res.uebernehmeLiefPreisInBestellung() != lastStklImportResult.uebernehmeLiefPreisInBestellung()) {
						cbPreisuebernahme = WrapperTristateCheckbox.HALFSELECTED;
						return;
					}
				}
				lastStklImportResult = res;
			}
			
			if(lastStklImportResult != null) {
				cbPreisuebernahme = lastStklImportResult.uebernehmeLiefPreisInBestellung() 
						? WrapperTristateCheckbox.SELECTED : WrapperTristateCheckbox.DESELECTED;
				return;
			}
		}
		cbPreisuebernahme = WrapperTristateCheckbox.DISABLE;
	}
	
	@Override
	public void setMappingUpdateStatusAusStklImportResults() {
		if(!model.getSelectedSpezifikation().getColumnTypes()
				.contains(StklImportSpezifikation.BESTELLPREIS)) {
			cbMappingUpdateState = WrapperTristateCheckbox.DISABLE;
			return;
		}
		super.setMappingUpdateStatusAusStklImportResults();
	}

	public int getPreisuebernahmeStatus() {
		return cbPreisuebernahme;
	}

	/**
	 * Selektiert die Liefpreis&uuml;bernahmen Flags aller importierten Stklresults
	 * in der Tabelle. Sind diese unterschiedlich gesetzt (HALFSELECTED) werden alle 
	 * auf true gesetzt.
	 * 
	 * @param status gibt den Status der Tristate Checkbox an, nach dem alle
	 * Preis&uuml;bernahme Flags gesetzt werden
	 */
	public void selektierePreisuebernahme(int status) {
		for(IStklImportResult res : model.getResults()) {
			if(status == WrapperTristateCheckbox.SELECTED)
				res.setUebernehmeLiefPreisInBestellung(false);
			else
				res.setUebernehmeLiefPreisInBestellung(true);
		}
		setPreisuebernahmeStatusAusStklImportResults();
		fireDataUpdateEvent();
	}

	@Override
	protected void updateCheckboxStatus() {
		super.updateCheckboxStatus();
		setPreisuebernahmeStatusAusStklImportResults();
	}
	
	/**
	 * Holt sich den Einkaufspreis eines Artikels des Lieferanten der Bestellung, falls
	 * vorhanden. Ansonsten wird die Spalte des Lieferantenpreises mit null besetzt.
	 * Wird keine IId eines Artikel &uuml;bergeben, so wird die Spalte ebenfalls
	 * zur&uuml;ckgesetzt.
	 *  
	 * @param artikelIId IId des Artikels zu dem der Einkaufspreis gesucht wird
	 * @param result ImportResult, in dessen Feld des Lieferantenpreises, das Ergebnis 
	 * gespeichert wird
	 */
	public void fetchLiefPreisIfExist(Integer artikelIId, IStklImportResult result) {
		if(!result.getValues().containsKey(StklImportSpezifikation.LIEFPREIS))
			return;
		
		ArtikellieferantDto artliefDto = null;
		String liefpreis = null;
		
		if(artikelIId != null) {
			try {
				artliefDto = DelegateFactory.getInstance().getArtikelDelegate()
						.getArtikelEinkaufspreisEinesLieferantenEinerBestellung(artikelIId, model.getSelectedSpezifikation().getStklIId(), 
								new BigDecimal(result.getValues().get(StklImportSpezifikation.MENGE)));
				if(artliefDto != null && artliefDto.getNEinzelpreis() != null) {
					liefpreis = Helper.formatZahl(artliefDto.getNEinzelpreis(), 2, LPMain.getTheClient().getLocUi());
				}
			} catch (Throwable e) {
			}
		}
		
		Map<String, String> values = new HashMap<String, String>(result.getValues());
		values.put(StklImportSpezifikation.LIEFPREIS, liefpreis);
		result.setValues(values);
	}

	@Override
	protected void doSomethingWithJustChosenArtikel(Integer iid) {
		fetchLiefPreisIfExist(iid, getResultWaitingForArtikelIId());
	}

	@Override
	public String getMappingUpdateTristateCheckboxText() {
		return LPMain.getTextRespectUISPr(
				"stkl.intelligenterstklimport.artikellieferantupdate.tristatecheckbox");	}

}
