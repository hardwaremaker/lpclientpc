package com.lp.client.finanz.sepaimportassistent;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.finanz.service.BuchungKompakt;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.Iso20022BuchungdetailDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.util.BelegZahlungAdapter;

public class UebersichtEntryNormalizer {
	
	private String waehrungCnr;
	
	public UebersichtEntryNormalizer(String waehrung) {
		this.waehrungCnr = waehrung;
	}

	public ManuelleAuswahlUebersichtEntry normalize(BelegZahlungAdapter belegZahlung) {
		ManuelleAuswahlUebersichtEntry entry = new ManuelleAuswahlUebersichtEntry();
		
		entry.setBetrag(belegZahlung.getNBetragfw());
		entry.setName(belegZahlung.getBelegAdapter().getPartnerKurzbezeichnung());
		entry.setWaehrungCNr(waehrungCnr);
		
		if (belegZahlung.isEingangsrechnungzahlungAdapter()) {
			entry.setErNr(belegZahlung.getBelegAdapter().getCNr());
			EingangsrechnungDto erDto = (EingangsrechnungDto) belegZahlung.getBelegAdapter().getRawBelegDto();
			entry.setInfo((erDto.getCLieferantenrechnungsnummer() != null ? erDto.getCLieferantenrechnungsnummer() + " " : "") 
					+ erDto.getCText() != null ? erDto.getCText() : "");
		} else if (belegZahlung.isRechnungzahlungAdapter()) {
			entry.setArNr(belegZahlung.getBelegAdapter().getCNr());
			RechnungDto reDto = (RechnungDto) belegZahlung.getBelegAdapter().getRawBelegDto();
			entry.setInfo((reDto.getCBez() != null ? reDto.getCBez() + " " : "") 
					+ (reDto.getCBestellnummer() != null ? reDto.getCBestellnummer() + " " : ""));
		}
		
		return entry;
	}
	
	public ManuelleAuswahlUebersichtEntry normalize(BuchungKompakt buchung) {
		ManuelleAuswahlUebersichtEntry entry = new ManuelleAuswahlUebersichtEntry();
		BuchungdetailDto detailGegenkonto = buchung.getBuchungdetailList().get(1);

		try {
			KontoDtoSmall kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKeySmall(detailGegenkonto.getKontoIId());
			entry.setKontoNr(kontoDto.getCNr());
			entry.setName(kontoDto.getCBez());
			entry.setBetrag(buchung.getBuchungdetailList().get(0).getNBetrag());
			entry.setWaehrungCNr(waehrungCnr);
		} catch (ExceptionLP e) {
			LpLogger.getInstance(this.getClass()).error("Error during kontoFindByPrimaryKeySmall", e);
		}
		
		return entry;
	}

	public ManuelleAuswahlUebersichtEntry normalize(Iso20022BuchungdetailDto buchungdetailDto) {
		ManuelleAuswahlUebersichtEntry entry = new ManuelleAuswahlUebersichtEntry();

		try {
			KontoDtoSmall kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKeySmall(buchungdetailDto.getKontoIId());
			entry.setKontoNr("(" + buchungdetailDto.getBuchungdetailartCNr().substring(0,1) + ") " + kontoDto.getCNr());
			entry.setName(kontoDto.getCBez());
			entry.setBetrag(buchungdetailDto.getNBetragKontowaehrung());
			entry.setWaehrungCNr(waehrungCnr);
		} catch (ExceptionLP e) {
			LpLogger.getInstance(this.getClass()).error("Error during kontoFindByPrimaryKeySmall", e);
		}
		
		return entry;
	}
}
