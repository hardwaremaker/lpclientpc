package com.lp.client.finanz.sepaimportassistent;

import java.math.BigDecimal;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.system.service.MandantDto;
import com.lp.util.Helper;

public class Sepakonto implements ISepakonto {

	private KontoDto kontoDto;
	private BigDecimal saldo;
	private String waehrungCnr;
	
	public Sepakonto(Integer kontoIId) throws Throwable {
		findKonto(kontoIId);
		initWaehrung();
	}

	private void findKonto(Integer kontoIId) throws ExceptionLP {
		kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKey(kontoIId);
		refresh();
	}

	@Override
	public String getTextUebersicht() {
		return LPMain.getMessageTextRespectUISPr("fb.sepa.import.anfangssaldo.aktuell.error", new Object[]{getKonto().getCNr(), 
				Helper.formatZahl(getSaldo(), FinanzFac.NACHKOMMASTELLEN, LPMain.getInstance().getUISprLocale())});
	}

	@Override
	public String getTextSuchergebnis() {
		return LPMain.getMessageTextRespectUISPr("fb.sepa.import.bankkontosaldo.aktuell", new Object[]{getKonto().getCNr()});
	}
	
	@Override
	public BigDecimal getSaldo() {
		return saldo;
	}

	@Override
	public KontoDto getKonto() {
		return kontoDto;
	}

	@Override
	public void refresh() {
		if (kontoDto == null) return;
		
		try {
			saldo = DelegateFactory.getInstance().getBuchenDelegate().getSaldoVonKontoMitEB(
					getKonto().getIId(), LPMain.getTheClient().getGeschaeftsJahr(), -1);
		} catch (Throwable e) {
			LpLogger.getLogger(this.getClass()).error(e);
		}
	}

	private String initWaehrung() throws ExceptionLP, Throwable {
		if (waehrungCnr != null) {
			return waehrungCnr;
		}
		
		waehrungCnr = getKonto().getWaehrungCNrDruck();
		if (waehrungCnr != null) {
			return waehrungCnr;
		}
		
		MandantDto mandantDto = DelegateFactory.getInstance().getMandantDelegate().mandantFindByPrimaryKey(LPMain.getTheClient().getMandant());
		waehrungCnr = mandantDto.getWaehrungCNr();
		
		return waehrungCnr;
	}
	
	public String getWaehrung() {
		return waehrungCnr;
	}
}
