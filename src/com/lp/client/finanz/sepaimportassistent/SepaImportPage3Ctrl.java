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

import java.math.BigDecimal;

import javax.swing.table.TableModel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.AssistentPageController;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;

public class SepaImportPage3Ctrl extends AssistentPageController {

	private SepaImportModel model;
	private Throwable throwable;
	
	public SepaImportPage3Ctrl(SepaImportModel model) {
		this.model = model;
	}

	@Override
	public boolean isNextAllowed() {
		return model.getAuszugsnummer() != null;
	}

	@Override
	public boolean isPrevAllowed() {
		return false;
	}

	@Override
	public boolean isCancelAllowed() {
		return true;
	}

	@Override
	public void activateByNext() throws Throwable {
//		berechneSaldoBankkontoHV();
		fireDataUpdateEvent();
	}

	@Override
	public void activateByPrev() throws Throwable {
		fireDataUpdateEvent();
	}

	@Override
	public boolean nextPageIfPossible() throws ExceptionLP, Throwable {
		return true;
	}

	@Override
	public boolean prevPageIfPossible() throws ExceptionLP, Throwable {
		return true;
	}

	@Override
	public boolean cancelIfPossible() throws ExceptionLP, Throwable {
		return true;
	}

	public String getSelectedBankverbindungName() {
		return model.getPartnerDtoByBankIId(model.getSelectedBankverbindung().getBankIId())
				.getCName1nachnamefirmazeile1();
	}
	
	public String getStatementNumber() {
//		if (model.getCurrentStatement().getAuszugsnr() != null) {
//			return model.getCurrentStatement().getAuszugsnr().toString();
//		}
		return model.getAuszugsnummer() != null ? model.getAuszugsnummer().toString() : "";
	}

	public String getCreationDate() {
		return model.getDateFormat().format(model.getCurrentStatement().getErstellungsDatum());
	}
	
	public Boolean hasSalden() {
		return model.hasStatementSalden();
	}
	
	public BigDecimal getAnfangsSaldo() {
		return model.getAnfangsSaldoBetrag();
	}

	public BigDecimal getEndSaldo() {
		return model.getEndSaldoBetrag();
	}
	
	public TableModel getOverviewTableModel() {
		return model.getOverviewTableModel();
	}
	
	public void viewDataChanged() {
		fireDataUpdateEvent();
		fireNavigationUpdateEvent();
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public ISepakonto getSepakonto() {
		if (model.getSepakonto() == null) {
			berechneSaldoBankkontoHV();
		}
		return model.getSepakonto();
	}
	
	private void berechneSaldoBankkontoHV() {
		try {
			Integer auszugsnummer = DelegateFactory.getInstance().getBuchenDelegate()
					.getNaechstniedrigereAuszugsnummer(model.getSelectedBankverbindung().getKontoIId(), 
							model.getNiedrigsteAuszugsnummer(), LPMain.getTheClient().getGeschaeftsJahr());
			
			if (auszugsnummer != null) {
				model.setSepakonto(new SepakontoAuszug(model.getSelectedBankverbindung().getKontoIId(), auszugsnummer));
			} else {
				model.setSepakonto(new Sepakonto(model.getSelectedBankverbindung().getKontoIId()));
			}
		} catch (Throwable t) {
			LpLogger.getLogger(this.getClass()).error(t);
			throwable = t;
			fireDataUpdateEvent();
		}
	}

	public boolean isAnfangssaldoOk() {
		if (!model.hasFibu() || getSepakonto() == null) return true;
		
		return getAnfangsSaldo().compareTo(getSepakonto().getSaldo()) == 0;
	}

	public String getWaehrung() {
		return getSepakonto().getWaehrung();
	}

}
