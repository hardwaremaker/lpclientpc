package com.lp.client.util;

import java.util.ArrayList;
import java.util.List;

import com.lp.client.frame.ExceptionLP;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.util.EJBExceptionLP;

public class ExcFactory {

	public static ExceptionLP ungueltigeEmailadresse(String mailadresse) {
		List<String> clientInfo = new ArrayList<String>();
		clientInfo.add(mailadresse);
		ExceptionLP excLP = new ExceptionLP(EJBExceptionLP.FEHLER_UNGUELTIGE_EMAILADRESSE_EXTENDED, 
				"Ungueltige Mailadresse '" + mailadresse,
				clientInfo, 
				new IllegalArgumentException(mailadresse));
		return excLP;
	}
	
	public static ExceptionLP artikelBenoetigtGewicht(ArtikelDto artikelDto) {
		return new ExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_INTRASTAT_ARTIKEL_BENOETIGT_GEWICHT,
				"Artikel " + artikelDto.getCNr() + 
				" (" + artikelDto.getIId() + ") benoetigt Gewicht",
				null, artikelDto.getIId().toString(), artikelDto.getCNr());
	}

	public static ExceptionLP uidBenoetigt(PartnerDto partnerDto) {
		return new ExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_INTRASTAT_UID_BENOETIGT, 
				"UID fehlt " + partnerDto.getCKbez() + ", " + partnerDto.getCName1nachnamefirmazeile1() + 
				" (" + partnerDto.getIId() + ")",
				null, partnerDto.getIId().toString(), partnerDto.getCKbez(), partnerDto.getCName1nachnamefirmazeile1());				
	}

}
