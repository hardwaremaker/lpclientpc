package com.lp.client.finanz.sepaimportassistent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.SepaBuchung;
import com.lp.server.finanz.service.SepaZahlung;
import com.lp.util.Helper;

public class StatementOverviewTableEntryNormalizer {

	private SepaKontoauszugModel kontoauszugModel;
	public SimpleDateFormat dateFormat = new SimpleDateFormat(SepaImportModel.DATE_FORMAT);
	private static final Integer MAX_TEXT_LINE_LENGTH = 140;
	
	public StatementOverviewTableEntryNormalizer(SepaKontoauszugModel kontoauszugModel) {
		this.kontoauszugModel = kontoauszugModel;
	}

	public List<StatementOverviewTableEntry> getOverviewTableEntries() {
		List<StatementOverviewTableEntry> entries = new ArrayList<StatementOverviewTableEntry>();
		
		entries.addAll(getKontoauszuginfos());
		entries.addAll(getBuchungsinfosOhneZahlung());
		entries.addAll(getZahlungen());
		
		return entries;
	}

	private List<StatementOverviewTableEntry> getZahlungen() {
		List<StatementOverviewTableEntry> entries = new ArrayList<StatementOverviewTableEntry>();
		
		for (SepaZahlung zahlung : kontoauszugModel.getPayments()) {
			entries.add(mapEntry(zahlung, kontoauszugModel.getBuchungByPayment(zahlung)));
		}
		return entries;
	}

	private StatementOverviewTableEntry mapEntry(SepaZahlung zahlung, SepaBuchung buchung) {
		StatementOverviewTableEntry entry = new StatementOverviewTableEntry();
		if (zahlung.getBeteiligter() != null) {
			entry.setName(zahlung.getBeteiligter().getName());
			entry.setIban(zahlung.getBeteiligter().getIban());
		}
		entry.setZahldatum(dateFormat.format(buchung.getBuchungsdatum()));
		entry.setBetrag(Helper.formatZahl(Helper.rundeKaufmaennisch(zahlung.getBetrag().getPlusMinusWert(), 2),
							FinanzFac.NACHKOMMASTELLEN, LPMain.getInstance().getUISprLocale()) + " " + buchung.getWaehrung());
		entry.setReferenztext(getReferenztext(zahlung, buchung));
		
		return entry;
	}

	private MultilineText getReferenztext(SepaZahlung zahlung, SepaBuchung buchung) {
		MultilineText referenztext = new MultilineText();
		if (zahlung.isSammler()) {
			referenztext.addLine(LPMain.getTextRespectUISPr("fb.sepa.import.sammlerbuchung") 
					+ "("+ String.valueOf(zahlung.getEnthalteneBuchungen()) + ")");
			
		} else if (!Helper.isStringEmpty(zahlung.getZahlungsreferenz())) {
			referenztext.addLine(zahlung.getZahlungsreferenz());
			
		} else if (!Helper.isStringEmpty(zahlung.getVerwendungszweck())) {
			referenztext.addLine(zahlung.getVerwendungszweck());
		}

		if (!Helper.isStringEmpty(zahlung.getZusatzinformation())) {
			splitInParts(referenztext, zahlung.getZusatzinformation(), MAX_TEXT_LINE_LENGTH);
		}
		
		if (!Helper.isStringEmpty(buchung.getZusatzinformation())) {
			splitInParts(referenztext, buchung.getZusatzinformation(), MAX_TEXT_LINE_LENGTH);
		}
		
		return referenztext;
	}

	private List<StatementOverviewTableEntry> getBuchungsinfosOhneZahlung() {
		List<StatementOverviewTableEntry> entries = new ArrayList<StatementOverviewTableEntry>();
		
		for (SepaBuchung buchung : kontoauszugModel.getStatement().getBuchungen()) {
			if (buchung.getZahlungen() == null || buchung.getZahlungen().isEmpty()) {
				entries.add(mapEntry(buchung));
			}
		}
		return entries;
	}

	private StatementOverviewTableEntry mapEntry(SepaBuchung buchung) {
		StatementOverviewTableEntry entry = new StatementOverviewTableEntry();
//		entry.setZahldatum(dateFormat.format(buchung.getBuchungsdatum()));
		entry.setReferenztext(getReferenztext(buchung.getZusatzinformation()));
		return entry;
	}

	private MultilineText getReferenztext(String text) {
		MultilineText referenztext = new MultilineText();
		if (!Helper.isStringEmpty(text)) {
			splitInParts(referenztext, text, MAX_TEXT_LINE_LENGTH);
			return referenztext;
		}
		
		referenztext.addLine("Keine Zahlung und keine Zusatzinformation");
		return referenztext;
	}
	
	private void splitInParts(MultilineText referenztext, String value, Integer size) {
		for (int i = 0; i < value.length(); i += size) {
			referenztext.addLine(value.substring(i, Math.min(i + size, value.length())));
		}
	}

	private List<StatementOverviewTableEntry> getKontoauszuginfos() {
		List<StatementOverviewTableEntry> entries = new ArrayList<StatementOverviewTableEntry>();

		if (Helper.isStringEmpty(kontoauszugModel.getStatement().getZusatzinformation()))
			return entries;
		
		StatementOverviewTableEntry entry = new StatementOverviewTableEntry();
		entry.setReferenztext(getReferenztext(kontoauszugModel.getStatement().getZusatzinformation()));
		entries.add(entry);
		return entries;
	}

}
