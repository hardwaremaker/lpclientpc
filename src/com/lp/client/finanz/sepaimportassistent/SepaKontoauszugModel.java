package com.lp.client.finanz.sepaimportassistent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.lp.server.finanz.service.Iso20022StandardEnum;
import com.lp.server.finanz.service.SepaBuchung;
import com.lp.server.finanz.service.SepaKontoauszug;
import com.lp.server.finanz.service.SepaZahlung;
import com.lp.server.finanz.service.SepakontoauszugDto;

public class SepaKontoauszugModel {

	private SepakontoauszugDto kontoauszugDto;
	private List<SepaZahlung> payments;
	private Map<SepaZahlung, SepaBuchung> paymentBookingMap;
	private Map<SepaBuchung, SepaKontoauszug> bookingStatementMap;
	private Iso20022StandardEnum iso20022Standard;

	public SepaKontoauszugModel(SepakontoauszugDto kontoauszugDto, Iso20022StandardEnum iso20022Standard) {
		this.kontoauszugDto = kontoauszugDto;
		this.iso20022Standard = iso20022Standard;
		initMaps();
	}
	
	private void initMaps() {
		payments = new ArrayList<SepaZahlung>();
		paymentBookingMap = new LinkedHashMap<SepaZahlung, SepaBuchung>();
		bookingStatementMap = new HashMap<SepaBuchung, SepaKontoauszug>();
		
		for (SepaBuchung book : getStatement().getBuchungen()) {
			bookingStatementMap.put(book, getStatement());
			
			for (SepaZahlung paym : book.getZahlungen()) {
				payments.add(paym);
				paymentBookingMap.put(paym, book);
			}
		}
	}
	
	public SepakontoauszugDto getKontoauszugDto() {
		return kontoauszugDto;
	}

	public SepaKontoauszug getStatement() {
		return kontoauszugDto.getKontoauszug();
	}
	
	public Integer getAuszugsnummer() {
		return kontoauszugDto.getIAuszug();
//		if (getStatement().getKontoInfo().getIban().startsWith("AT")) {
//			return getStatement().getAuszugsnr().intValue();
//		} else {
//			return getStatement().getElektronischeAuszugsnr().intValue();
//		}
	}
	
	public List<SepaZahlung> getPayments() {
		return payments;
	}
	
	public SepaBuchung getBuchungByPayment(SepaZahlung payment) {
		return paymentBookingMap.get(payment);
	}
	
	public Iso20022StandardEnum getIso20022Standard() {
		return iso20022Standard;
	}

}
