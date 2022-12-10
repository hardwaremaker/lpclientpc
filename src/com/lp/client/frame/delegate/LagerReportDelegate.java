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
package com.lp.client.frame.delegate;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.artikel.service.LagerReportFac;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

public class LagerReportDelegate extends Delegate {
	private Context context;
	private LagerReportFac lagerReportFac;

	public LagerReportDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();

			lagerReportFac = lookupFac(context, LagerReportFac.class);

		} catch (Throwable t) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t);
		}

	}

	public JasperPrintLP printWarenbewegungsjournal(Integer artikelIId, Integer lagerIId, Timestamp tVon,
			Timestamp tBis) throws Throwable {
		try {
			return lagerReportFac.printWarenbewegungsjournal(artikelIId, lagerIId, tVon, tBis, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printZaehlliste(Integer lagerIId, BigDecimal abLagerwert, BigDecimal abGestpreis, Boolean b1,
			Boolean b2, int iSortierung, boolean bMitVersteckten, Integer artikelgruppeIId, Integer artikelklasseIId,
			String lagerplatzVon, String lagerplatzBis, int iArtikelarten, String artikelNrVon, String artikelNrBis,
			boolean bNurLagerbewerteteArtikel) throws ExceptionLP {
		try {
			return lagerReportFac.printZaehlliste(lagerIId, abLagerwert, abGestpreis, b1, b2, iSortierung,
					bMitVersteckten, artikelgruppeIId, artikelklasseIId, lagerplatzVon, lagerplatzBis, iArtikelarten,
					artikelNrVon, artikelNrBis, bNurLagerbewerteteArtikel, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printWarenentnahmestatistik(Timestamp tVon, Timestamp tBis, Integer lagerIId,
			boolean bMitVersteckten, String artikelNrVon, String artikelNrBis, Integer artikelgruppeIId,
			Integer artikelklasseIId, Integer iSortierung, boolean bMitNichtLagerbewirtschaftetenArtikeln,
			boolean bDetailliert) throws Throwable {
		try {
			return lagerReportFac.printWarenentnahmestatistik(tVon, tBis, lagerIId, bMitVersteckten, artikelNrVon,
					artikelNrBis, artikelgruppeIId, artikelklasseIId, iSortierung,
					bMitNichtLagerbewirtschaftetenArtikeln, bDetailliert, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printLagerstandliste(Integer lagerIId, java.sql.Timestamp tDatum, boolean bMitAZArtikel,
			String artikelNrVon, String artikelNrBis, Integer artikelgruppeIId, Integer artikelklasseIId,
			Integer vkPreislisteIId, int iOptionSortierung, int iArtikelarten, boolean bNurLagerbewertete,
			boolean bMitAbgewertetemGestpreis, boolean bMitArtikelOhneLagerstand, Integer lagerplatzIId,
			boolean bMitVersteckten, Integer shopgruppeIId, boolean bMitNichtLagerbewirtschaftetenArtikeln,
			boolean bDetailliert, boolean bLagerstandMitBelegen) throws Throwable {
		try {
			return lagerReportFac.printLagerstandliste(lagerIId, tDatum, new Boolean(bMitAZArtikel),
					new Boolean(bNurLagerbewertete), artikelNrVon, artikelNrBis, artikelgruppeIId, artikelklasseIId,
					vkPreislisteIId, iOptionSortierung, iArtikelarten, bMitAbgewertetemGestpreis,
					bMitArtikelOhneLagerstand, lagerplatzIId, bMitVersteckten, shopgruppeIId,
					bMitNichtLagerbewirtschaftetenArtikeln, bDetailliert, bLagerstandMitBelegen, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printGestpreisUnterMinVK(Integer lagerIId, Integer vkPreislisteIId, boolean bMitVersteckten,
			boolean bVergleichMitMinVKPReis, boolean bMitStuecklisten) throws Throwable {
		try {
			return lagerReportFac.printGestpreisUeberMinVK(lagerIId, vkPreislisteIId, bMitVersteckten,
					bVergleichMitMinVKPReis, bMitStuecklisten, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printLadenhueter(Timestamp tVon, Timestamp tBis, Integer iOptionSortierung, String filterVon,
			String filterBis, Integer lagerIId, boolean bMitHandlagerbewegungen, boolean bMitFertigung,
			boolean bMitVersteckten, Integer artikelgruppeIId, boolean bZugaengeBeruecksichtigen) throws Throwable {
		try {
			return lagerReportFac.printLadenhueter(tVon, tBis, iOptionSortierung, filterVon, filterBis, lagerIId,
					bMitHandlagerbewegungen, bMitFertigung, bMitVersteckten, artikelgruppeIId,
					bZugaengeBeruecksichtigen, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printUmbuchungsbeleg(Integer lagerbewegungIIdBuchungZubuchung,
			Integer lagerbewegungIIdBuchungabbuchung) throws ExceptionLP {
		try {
			return lagerReportFac.printUmbuchungsbeleg(lagerbewegungIIdBuchungZubuchung,
					lagerbewegungIIdBuchungabbuchung, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printLagerbuchungsbeleg(Integer iExemplare, Integer lagerbewegungIIdBuchung)
			throws ExceptionLP {
		try {
			return lagerReportFac.printLagerbuchungsbeleg(iExemplare, lagerbewegungIIdBuchung, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printHandlagerbewegungen(Timestamp dVon, Timestamp dBis, Integer artikelIId,
			Integer artikelgruppeIId) throws ExceptionLP {
		try {
			return lagerReportFac.printHandlagerbewegungen(dVon, dBis, artikelIId, artikelgruppeIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printChargeneigenschaften(Integer lagerbewegungIIdBuchung) throws ExceptionLP {
		try {
			return lagerReportFac.printChargeneigenschaften(lagerbewegungIIdBuchung, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printHitliste(Timestamp tVon, Timestamp tBis, Integer iOptionSortierung, String filterVon,
			String filterBis, boolean bMitHandlagerbewegungen, boolean bMitFertigung, boolean bMitVersteckten,
			boolean bNurVerrechnetePositionen) throws Throwable {
		try {
			return lagerReportFac.printHitliste(tVon, tBis, null, iOptionSortierung, filterVon, filterBis,
					bMitHandlagerbewegungen, bMitFertigung, bMitVersteckten, bNurVerrechnetePositionen,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printArtikelgruppen(Timestamp tVon, Timestamp tBis,

			boolean bMitHandlagerbewegungen, boolean bMitFertigung, boolean bMitVersteckten,
			boolean bNurVerrechnetePositionen) throws Throwable {
		try {
			return lagerReportFac.printArtikelgruppen(tVon, tBis, bMitHandlagerbewegungen, bMitFertigung,
					bMitVersteckten, bNurVerrechnetePositionen, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printMaterialbedarfsvorschau(Timestamp tVergangenheit_Von, Timestamp tVergangenheit_Bis,
			Timestamp tAngebotRealisierungstermin, Timestamp tOffeneRahmenAuftraegeBis, Timestamp tAngelegteAuftraege,
			Integer kundeIId, Integer artikelgruppeIId, boolean bNurVerrechnetePositionen) throws Throwable {
		try {
			return lagerReportFac.printMaterialbedarfsvorschau(tVergangenheit_Von, tVergangenheit_Bis,
					tAngebotRealisierungstermin, tOffeneRahmenAuftraegeBis, tAngelegteAuftraege, kundeIId,
					artikelgruppeIId, bNurVerrechnetePositionen, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printShopgruppen(Timestamp tVon, Timestamp tBis,

			boolean bMitHandlagerbewegungen, boolean bMitFertigung, boolean bMitVersteckten,
			boolean bNurVerrechnetePositionen) throws Throwable {
		try {
			return lagerReportFac.printShopgruppen(tVon, tBis, bMitHandlagerbewegungen, bMitFertigung, bMitVersteckten,
					bNurVerrechnetePositionen, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printMindesthaltbarkeit(String artikelnrVon, String artikelnrBis,
			boolean bSortiertNachChargennr, Integer mhalter) throws Throwable {
		try {
			return lagerReportFac.printMindesthaltbarkeit(artikelnrVon, artikelnrBis, bSortiertNachChargennr, mhalter,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printIndirekterWareneinsatz(DatumsfilterVonBis datumsfilter, Integer kundeIId, int iSortierung)
			throws Throwable {
		try {
			return lagerReportFac.printIndirekterWareneinsatz(datumsfilter, kundeIId, iSortierung,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printKundeUmsatzstatistik(Timestamp tVon, Timestamp tBis, Integer iOptionKundengruppierung,
			boolean bUmsatz, Integer iOptionGruppierung, boolean bNurHauptgruppeKlasse, Integer iOptionSortierung,
			Integer iSortierbasisJahre, boolean bVerwendeAuftragsadresse, boolean bVerwendeStatistikadresse,
			boolean bMitNichtLagerbewertetenArtikeln, boolean ohneDBBetrachtung,
			boolean bSortiertNachProvisionsempfaenger) throws Throwable {
		try {
			return lagerReportFac.printKundeumsatzstatistik(tVon, tBis, iOptionKundengruppierung, bUmsatz,
					iOptionGruppierung, bNurHauptgruppeKlasse, iOptionSortierung, iSortierbasisJahre,
					bVerwendeAuftragsadresse, bVerwendeStatistikadresse, bMitNichtLagerbewertetenArtikeln,
					ohneDBBetrachtung, bSortiertNachProvisionsempfaenger, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printLieferantUmsatzstatistik(Timestamp tVon, Timestamp tBis,
			Integer iOptionLieferantgruppierung, boolean bWareneingangspoitionen, Integer iOptionGruppierung,
			Integer iOptionSortierung, boolean bNurHauptgruppeKlasse) throws Throwable {
		try {
			return lagerReportFac.printLieferantumsatzstatistik(tVon, tBis, bWareneingangspoitionen,
					iOptionLieferantgruppierung, iOptionGruppierung, iOptionSortierung, bNurHauptgruppeKlasse,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

}
