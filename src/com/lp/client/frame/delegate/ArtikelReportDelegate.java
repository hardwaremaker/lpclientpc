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
import java.util.ArrayList;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.anfrage.service.ReportAnfragestatistikKriterienDto;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.artikel.service.ArtikelReportFac.ReportKundensonderkoditionenSortierung;
import com.lp.server.system.service.GeaenderteChargennummernDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

public class ArtikelReportDelegate extends Delegate {
	private Context context;
	private ArtikelReportFac artikelReportFac;

	public ArtikelReportDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			artikelReportFac = lookupFac(context, ArtikelReportFac.class);
		} catch (Throwable t) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t);
		}

	}

	public JasperPrintLP printFehlmengen(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelReportFac.printFehlmengen(artikelIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printKundensokos(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelReportFac.printKundensokos(artikelIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printKundensonderkonditionen(Integer artikelgruppeIId, Integer artikelklasseIId,
			String artikelnrVon, String artikelnrBis, boolean bMitVersteckten,
			ReportKundensonderkoditionenSortierung sort) throws ExceptionLP {
		try {
			return artikelReportFac.printKundensonderkonditionen(artikelgruppeIId, artikelklasseIId, artikelnrVon, artikelnrBis, bMitVersteckten, sort, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printNaechsteWartungen() throws ExceptionLP {
		try {
			return artikelReportFac.printNaechsteWartungen(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printLieferantenpreis(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelReportFac.printLieferantenpreis(artikelIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printLieferantenpreisvergleich(Integer artikelIId, java.sql.Date dStichtag)
			throws ExceptionLP {
		try {
			return artikelReportFac.printLieferantenpreisvergleich(artikelIId, dStichtag, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printMakeOrBuy(Integer artikelgruppeIId, Integer artikelklasseIId, String artiklenrVon,
			String artiklenrBis, java.sql.Date dStichtagEK, java.sql.Date dZeitraumVonAblieferung,
			java.sql.Date dZeitraumBisAblieferung, boolean bMitVersteckten) throws ExceptionLP {
		try {
			return artikelReportFac.printMakeOrBuy(artikelgruppeIId, artikelklasseIId, artiklenrVon, artiklenrBis,
					dStichtagEK, dZeitraumVonAblieferung, dZeitraumBisAblieferung, bMitVersteckten,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printAlergene(String artikelNrVon, String artikelNrBis) throws ExceptionLP {
		try {
			return artikelReportFac.printAllergene(artikelNrVon, artikelNrBis, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printGeaenderteChargen(String artikelNrVon, String artikelNrBis,
			ArrayList<GeaenderteChargennummernDto> alGeaenderteChargen) throws ExceptionLP {
		try {
			return artikelReportFac.printGeaenderteChargen(artikelNrVon, artikelNrBis, alGeaenderteChargen,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printAenderungen(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelReportFac.printAenderungen(artikelIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printMindestlagerstaende() throws ExceptionLP {
		try {
			return artikelReportFac.printMindestlagerstaende(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printAuftragsseriennummern(Integer artikelIId, java.sql.Date dVon, java.sql.Date dBis)
			throws ExceptionLP {
		try {
			return artikelReportFac.printAuftragsseriennummern(artikelIId, dVon, dBis, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printVkPreisentwicklung(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelReportFac.printVkPreisentwicklung(artikelIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printVkPreisliste(Integer preislisteIId, Integer artikelgruppeIId, Integer artikelklasseIId,
			Integer shopgruppeIId, boolean bMitInaktiven, String artikelnrVon, String artikelnrBis,
			boolean bMitVersteckten, java.sql.Date datGueltikeitsdatumI, String waehrungCNr) throws ExceptionLP {
		try {
			return artikelReportFac.printVkPreisliste(preislisteIId, artikelgruppeIId, artikelklasseIId, shopgruppeIId,
					bMitInaktiven, artikelnrVon, artikelnrBis, bMitVersteckten, datGueltikeitsdatumI, waehrungCNr,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printAuftragswerte(String artikelNrVon, String artikelNrBis, boolean bMitKonsignationslager)

			throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = artikelReportFac.printAuftragswerte(artikelNrVon, artikelNrBis, bMitKonsignationslager,
					LPMain.getTheClient());

		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oPrint;
	}

	public JasperPrintLP printAufgeloesteFehlmengen(Integer fasessionIId, boolean bNachdruck) throws ExceptionLP {
		try {
			return artikelReportFac.printAufgeloesteFehlmengen(fasessionIId, bNachdruck, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printLosstatus(Integer artikelIId) throws Throwable {
		try {
			return artikelReportFac.printLosstatus(artikelIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printLagercockpitMaterialVerteilungsvorschlag(Integer iOption) throws Throwable {
		try {
			return artikelReportFac.printLagercockpitMaterialVerteilungsvorschlag(iOption, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printLagercockpitWELagerVerteilungsvorschlag() throws Throwable {
		try {
			return artikelReportFac.printLagercockpitWELagerVerteilungsvorschlag(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printVerwendungsnachweis(Integer artikelIId, boolean bMitVerbrauchtenMengen,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis, boolean bMitVersteckten, boolean bMitHierarchie,
			boolean bMandantenuebergreifend, boolean bVerdichtet) throws ExceptionLP {
		try {
			return artikelReportFac.printVerwendungsnachweis(artikelIId, bMitVerbrauchtenMengen, tVon, tBis,
					bMitVersteckten, bMitHierarchie, bMandantenuebergreifend, bVerdichtet, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printArtikelOhneStklVerwendung(boolean bMitVersteckten) throws ExceptionLP {
		try {
			return artikelReportFac.printArtikelOhneStklVerwendung(bMitVersteckten, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printFreiInFertigung(Integer artikelIId) throws Throwable {
		try {
			return artikelReportFac.printFreiInFertigung(artikelIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printMehrereArtikeletiketten(String artikelgruppeFilt, Integer artikelklasseIId,
			Integer artikelVonId, Integer artikelBisId, Integer lagerIId, Integer lagerplatzVon, Integer lagerplatzBis,
			Integer shopgruppeIId, ArtikelReportFac.ReportMehrereArtikeletikettenSortierung sortierung,
			boolean orderAscDesc, boolean etikettProLagerplatz) throws ExceptionLP {
		try {
			return artikelReportFac.printMehrereArtikeletiketten(artikelgruppeFilt, artikelklasseIId, artikelVonId,
					artikelBisId, lagerIId, lagerplatzVon, lagerplatzBis, shopgruppeIId, sortierung, orderAscDesc,
					etikettProLagerplatz, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printArtikeletikett(Integer artikelIId, String sKommentar, BigDecimal bdMenge,
			Integer iExemplare, String[] cSnrChnr, Integer lagerIIdfuerLagerstandDerCharge, String lfdnr,
			String trennzeichenLfdNr) throws Throwable {
		try {
			return artikelReportFac.printArtikeletikett(artikelIId, sKommentar, bdMenge, iExemplare, cSnrChnr,
					lagerIIdfuerLagerstandDerCharge, lfdnr, trennzeichenLfdNr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printSeriennummernStammblatt(Integer lagerIId, Integer artikelIId, String[] cSeriennrs,
			boolean bSortIdent, String snrWildcard, String versionWildcard, boolean bNurSeriennummern,
			boolean nurObersteEbene) throws ExceptionLP {
		try {
			return artikelReportFac.printSeriennummernStammblatt(lagerIId, artikelIId, cSeriennrs, snrWildcard,
					new Boolean(bSortIdent), versionWildcard, bNurSeriennummern, nurObersteEbene,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printLagerplatzetikett(Integer lagerplatzIId) throws Throwable {
		try {
			return artikelReportFac.printLagerplatzetikett(lagerplatzIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printArtikelstammblatt(Integer artikelIId) throws Throwable {
		try {
			return artikelReportFac.printArtikelstammblatt(artikelIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printArtikelbestellt(Integer artikelIId, java.sql.Date dVon, java.sql.Date dBis)
			throws ExceptionLP {
		try {
			return artikelReportFac.printArtikelbestellt(artikelIId, dVon, dBis, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printRahmenbestellungsliste(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelReportFac.printRahmenbestellungsliste(artikelIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printWerkzeugVerschleissteil(Integer werkzeugIId, Integer verschleisteilIId)
			throws ExceptionLP {
		try {
			return artikelReportFac.printWerkzeugVerschleissteil(werkzeugIId, verschleisteilIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public JasperPrintLP printBewegungsvorschau(Integer artikelIId, boolean bInterneBestellungMiteibeziehen,
			Integer partnerIIdStandort, boolean bMitRahmen, boolean bUeberAlleMandanten) throws Throwable {
		try {
			return artikelReportFac.printBewegungsvorschau(artikelIId, bInterneBestellungMiteibeziehen,
					partnerIIdStandort, bMitRahmen, bUeberAlleMandanten, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printBewegungsvorschauAlle(Integer artikelgruppeIId, Integer artikelklasseIId, String artiklenrVon, String artiklenrBis,
			Integer partnerIIdStandort, boolean bMitRahmen, boolean bArtikelOhneBewegungsvorschauAusblenden, boolean bMitVersteckten) throws Throwable {
		try {
			return artikelReportFac.printBewegungsvorschauAlle(artikelgruppeIId, artikelklasseIId, artiklenrVon, artiklenrBis,
					partnerIIdStandort, bMitRahmen, bArtikelOhneBewegungsvorschauAusblenden, bMitVersteckten, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printArtikelstatistik(Integer artikelIId, java.sql.Date dVon, java.sql.Date dBis,
			Integer iOption, boolean bMonatsstatistik, boolean bEingeschraenkt, boolean bMitHandlagerbewegungen,
			boolean bMitBewegungsvorschau, boolean bMitNichtFreigegebenenAuftraegen, boolean bMitHistory,
			boolean bMitVorgaengern) throws ExceptionLP {
		try {
			return artikelReportFac.printArtikelstatistik(artikelIId, dVon, dBis, iOption, bMonatsstatistik,
					bEingeschraenkt, bMitHandlagerbewegungen, bMitBewegungsvorschau, bMitNichtFreigegebenenAuftraegen,
					bMitHistory, bMitVorgaengern, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Fuer einen bestimmten Artikel die Rahmenreservierung des Auftrags drucken.
	 * 
	 * @param kritDtoI die Auswertungskriterien des Benutzers
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP Ausnahme
	 */
	public JasperPrintLP printRahmenreservierungsliste(ReportAnfragestatistikKriterienDto kritDtoI) throws ExceptionLP {
		JasperPrintLP jasperPrint = null;

		try {
			jasperPrint = artikelReportFac.printRahmenreservierungsliste(kritDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return jasperPrint;
	}

}
