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
package com.lp.client.finanz;

import java.math.BigDecimal;
import java.sql.Date;

import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.util.Helper;

public class FinanzBankbuchungModel {
	protected static final int BELEGNUMMER_HAND = 1;
	protected static final int BELEGNUMMER_AUTO = 2;
	private KontoDto bankkontoDto;
	private KontoDto gegenkontoDto;
	private KostenstelleDto kostenstelleDto;
	private MwstsatzDto mwstsatzDto;
	private WechselkursDto kursDto;
	
	private Date buchungsdatum;
	private String buchungsart;
	private String kontotypGegenkonto;
	private Integer auszugnummer;
	private Integer auszugnummerGegenkonto;
	private boolean isGegenkontoBankkonto;
	private BigDecimal betrag;
	private BigDecimal mwstBetrag;
	private String waehrung;
	private String text;
	private String belegnummer;
	private String kommentar;
	private int belegnummerModi;
	
	public FinanzBankbuchungModel() {
		this(null, null);
	}
	
	public FinanzBankbuchungModel(KontoDto bankkontoDto, Integer auszugnummer) {
		this.bankkontoDto = bankkontoDto;
		this.auszugnummer = auszugnummer;
		setDefaults();
	}
	
	public void setDefaults() {
		setGegenkontoDto(null);
		setKostenstelleDto(null);
		setMwstsatzDto(null);
		setKursDto(null);
		setBuchungsart(BuchenFac.SollBuchung);
		setKontotypGegenkonto(FinanzServiceFac.KONTOTYP_SACHKONTO);
		setBetrag(new BigDecimal(0));
		setMwstBetrag(new BigDecimal(0));
		setText(null);
		setBelegnummer(null);
		setKommentar(null);
		setBelegnummerModi(BELEGNUMMER_AUTO);
		setGegenkontoBankkonto(false);
	}

	public KontoDto getBankkontoDto() {
		return bankkontoDto;
	}

	public void setBankkontoDto(KontoDto bankkontoDto) {
		this.bankkontoDto = bankkontoDto;
	}

	public KontoDto getGegenkontoDto() {
		return gegenkontoDto;
	}

	public void setGegenkontoDto(KontoDto gegenkontoDto) {
		this.gegenkontoDto = gegenkontoDto;
	}

	public KostenstelleDto getKostenstelleDto() {
		return kostenstelleDto;
	}

	public void setKostenstelleDto(KostenstelleDto kostenstelleDto) {
		this.kostenstelleDto = kostenstelleDto;
	}

	public MwstsatzDto getMwstsatzDto() {
		return mwstsatzDto;
	}

	public void setMwstsatzDto(MwstsatzDto mwstsatzDto) {
		this.mwstsatzDto = mwstsatzDto;
	}

	public WechselkursDto getKursDto() {
		return kursDto;
	}

	public void setKursDto(WechselkursDto kursDto) {
		this.kursDto = kursDto;
	}

	public Date getBuchungsdatum() {
		return buchungsdatum;
	}

	public void setBuchungsdatum(Date buchungsdatum) {
		this.buchungsdatum = buchungsdatum;
	}

	public String getBuchungsart() {
		return buchungsart;
	}

	public void setBuchungsart(String buchungsart) {
		this.buchungsart = buchungsart;
	}

	public String getKontotypGegenkonto() {
		return kontotypGegenkonto;
	}

	public void setKontotypGegenkonto(String kontotypGegenkonto) {
		this.kontotypGegenkonto = kontotypGegenkonto;
	}

	public Integer getAuszugnummer() {
		return auszugnummer;
	}

	public void setAuszugnummer(Integer auszugnummer) {
		this.auszugnummer = auszugnummer;
	}

	public BigDecimal getBetrag() {
		return betrag;
	}
	
	public BigDecimal getBetragNetto() {
		return getBetrag().subtract(getMwstBetrag());
	}

	public void setBetrag(BigDecimal betrag) {
		this.betrag = betrag;
	}

	public BigDecimal getMwstBetrag() {
		return mwstBetrag;
	}

	public void setMwstBetrag(BigDecimal mwstBetrag) {
		this.mwstBetrag = mwstBetrag;
	}

	public String getWaehrung() {
		return waehrung;
	}

	public void setWaehrung(String waehrung) {
		this.waehrung = waehrung;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getBelegnummer() {
		return belegnummer;
	}

	public void setBelegnummer(String belegnummer) {
		this.belegnummer = belegnummer;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public BigDecimal getBetragBruttoMandantenWaehrung() throws Throwable {
		if (!isKontoInMandantenWaehrung() && getKursDto() != null) {
			return Helper.rundeKaufmaennisch(getBetrag(), FinanzFac.NACHKOMMASTELLEN).multiply(getKursDto().getNKurs());
		} else {
			return getBetrag();
		}
	}

	public BigDecimal getBetragNettoMandantenWaehrung() throws Throwable {
		if (!isKontoInMandantenWaehrung() && getKursDto() != null) {
			return Helper.rundeKaufmaennisch(getBetragNetto(), FinanzFac.NACHKOMMASTELLEN).multiply(getKursDto().getNKurs());
		} else {
			return getBetrag().subtract(getMwstBetrag());
		}
	}
	
	public BigDecimal getBetragMwstMandantenWaehrung() throws Throwable {
		if (!isKontoInMandantenWaehrung() && getKursDto() != null) {
			return Helper.rundeKaufmaennisch(getMwstBetrag(), FinanzFac.NACHKOMMASTELLEN).multiply(getKursDto().getNKurs());
		} else {
			return getMwstBetrag();
		}
	}

	public boolean isKontoInMandantenWaehrung() throws Throwable {
		return LPMain.getTheClient().getSMandantenwaehrung().equals(getWaehrung());
	}
	
	public int getBelegnummerModi() {
		return belegnummerModi;
	}

	public void setBelegnummerModi(int belegnummerModi) {
		this.belegnummerModi = belegnummerModi;
	}

	public Integer getAuszugnummerGegenkonto() {
		return auszugnummerGegenkonto;
	}

	public void setAuszugnummerGegenkonto(Integer auszugnummerGegenkonto) {
		this.auszugnummerGegenkonto = auszugnummerGegenkonto;
	}

	public boolean isGegenkontoBankkonto() {
		return isGegenkontoBankkonto;
	}

	public void setGegenkontoBankkonto(boolean isGegenkontoBankkonto) {
		this.isGegenkontoBankkonto = isGegenkontoBankkonto;
		setAuszugnummerGegenkonto(isGegenkontoBankkonto ? getAuszugnummerGegenkonto() : null);
	}
	
	public boolean hasMwstBetrag() {
		return getMwstBetrag() != null && getMwstBetrag().signum() != 0;
	}
}

