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

public class SepakontoAuszug implements ISepakonto {

	private KontoDto kontoDto;
	private BigDecimal saldo;
	private Integer auszugsnummer;
	private String waehrungCnr;
	
	public SepakontoAuszug(Integer kontoIId, Integer auszugsnummer) throws Throwable {
		this.auszugsnummer = auszugsnummer;
		findKonto(kontoIId);
		initWaehrung();
	}

	private void findKonto(Integer kontoIId) throws ExceptionLP {	
		
		kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKey(kontoIId);
		refresh();
	}

	@Override
	public BigDecimal getSaldo() {
		return saldo;
	}
	
	public Integer getAuszugsnummer() {
		return auszugsnummer;
	}
	
	public void setAuszugsnummer(Integer auszugsnummer) {
		this.auszugsnummer = auszugsnummer;
	}

	@Override
	public KontoDto getKonto() {
		return kontoDto;
	}

	@Override
	public String getTextUebersicht() {
		if (kontoDto == null) return "";
		
		return LPMain.getMessageTextRespectUISPr("fb.sepa.import.anfangssaldo.error", String.valueOf(getAuszugsnummer()), 
				Helper.formatZahl(getSaldo(), FinanzFac.NACHKOMMASTELLEN, LPMain.getInstance().getUISprLocale()),
				getWaehrung());
	}

	@Override
	public String getTextSuchergebnis() {
		return LPMain.getMessageTextRespectUISPr("fb.sepa.import.bankkontosaldo", new Object[]{getKonto().getCNr()}) 
				+ " " + getAuszugsnummer();
	}
	
	@Override
	public void refresh() {
		if (kontoDto == null) return;
		
		try {
			saldo = DelegateFactory.getInstance().getFinanzDelegate().getSaldoVonBankverbindungByAuszug(
					kontoDto.getIId(), auszugsnummer);
		} catch (ExceptionLP e) {
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
