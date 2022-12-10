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
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.pc.LPMain;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.report.JasperPrintLP;

public class StuecklisteReportDelegate extends Delegate {
	private Context context;
	private StuecklisteReportFac stuecklisteReportFac;

	public StuecklisteReportDelegate() throws Exception {
		context = new InitialContext();
		stuecklisteReportFac = lookupFac(context, StuecklisteReportFac.class);
	}

	public JasperPrintLP printStuecklisteAllgemein(Integer stuecklisteIId, boolean bMtiPositionskommentar,
			boolean bMitStuecklistenkommentar, boolean bUnterstuecklistenEinbinden,
			boolean bGleichePositionenZusammenfassen, int iOptionSortierungUnterstuecklisten,
			boolean bUnterstklstrukurBelassen, Integer iOptionSortierungStuecklisteGesamt1,
			Integer iOptionSortierungStuecklisteGesamt2, Integer iOptionSortierungStuecklisteGesamt3,
			String[] labelSortierungen, boolean fremdfertigungAufloesen) throws Throwable {
		try {
			return stuecklisteReportFac.printStuecklisteAllgemein(stuecklisteIId, new Boolean(bMtiPositionskommentar),
					new Boolean(bMitStuecklistenkommentar), new Boolean(bUnterstuecklistenEinbinden),
					new Boolean(bGleichePositionenZusammenfassen), new Integer(iOptionSortierungUnterstuecklisten),
					bUnterstklstrukurBelassen, LPMain.getTheClient(), iOptionSortierungStuecklisteGesamt1,
					iOptionSortierungStuecklisteGesamt2, iOptionSortierungStuecklisteGesamt3, labelSortierungen,
					fremdfertigungAufloesen);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printWaffenregister(Integer stuecklisteIId) throws Throwable {
		try {
			return stuecklisteReportFac.printWaffenregister(stuecklisteIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printReichweite(Integer stuecklisteIId, DatumsfilterVonBis vonBis, boolean bVerdichtet)
			throws Throwable {
		try {
			return stuecklisteReportFac.printReichweite(stuecklisteIId, vonBis, bVerdichtet, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printVergleichMitAndererStueckliste(Integer stuecklisteIId, Integer stuecklisteIId2,
			boolean bSortiertNachArtikelnummer, boolean bVerdichtetNachArtikelnummer, boolean bHerstellerunabhaengig,
			boolean bNurUnterschiede) throws Throwable {
		try {
			return stuecklisteReportFac.printVergleichMitAndererStueckliste(stuecklisteIId, stuecklisteIId2,
					bSortiertNachArtikelnummer, bVerdichtetNachArtikelnummer, bHerstellerunabhaengig, bNurUnterschiede,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printPruefkombinationen(Integer artikelIIdKontakt, Integer artikelIIdLitze,
			String bezeichnungLitze) throws Throwable {
		try {
			return stuecklisteReportFac.printPruefkombinationen(artikelIIdKontakt, artikelIIdLitze, bezeichnungLitze,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printPruefplan(Integer stuecklisteIId) throws Throwable {
		try {
			return stuecklisteReportFac.printPruefplan(stuecklisteIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printFreigabe(Integer stuecklisteIId) throws Throwable {
		try {
			return stuecklisteReportFac.printFreigabe(stuecklisteIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printVerschleissteilverwendung(Integer werkzeugIId) throws Throwable {
		try {
			return stuecklisteReportFac.printVerschleissteilverwendung(werkzeugIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printStuecklisteAllgemeinMitPreis(Integer stuecklisteIId, int iOptionPreis,
			boolean bMtiPositionskommentar, boolean bMitStuecklistenkommentar, boolean bUnterstuecklistenEinbinden,
			boolean bGleichePositionenZusammenfassen, int iOptionSortierungUnterstuecklisten,
			boolean bUnterstklstrukurBelassen, Integer iOptionSortierungStuecklisteGesamt1,
			Integer iOptionSortierungStuecklisteGesamt2, Integer iOptionSortierungStuecklisteGesamt3,
			boolean fremdfertigungAufloesen) throws Throwable {
		try {
			return stuecklisteReportFac.printStuecklisteAllgemeinMitPreis(stuecklisteIId, new Integer(iOptionPreis),
					new Boolean(bMtiPositionskommentar), new Boolean(bMitStuecklistenkommentar),
					new Boolean(bUnterstuecklistenEinbinden), new Boolean(bGleichePositionenZusammenfassen),
					new Integer(iOptionSortierungUnterstuecklisten), bUnterstklstrukurBelassen, LPMain.getTheClient(),
					iOptionSortierungStuecklisteGesamt1, iOptionSortierungStuecklisteGesamt2,
					iOptionSortierungStuecklisteGesamt3, fremdfertigungAufloesen);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printAusgabestueckliste(Integer stuecklisteIId[], Integer lagerIId,
			boolean bMitStuecklistenkommentar, boolean bUnterstuecklistenEinbinden,
			boolean bGleichePositionenZusammenfassen, BigDecimal nLossgroesse, boolean bSortiertNachArtikelbezeichnung,
			int iOptionLager, boolean fremdfertigungAufloesen) throws Throwable {
		try {
			return stuecklisteReportFac.printAusgabestueckliste(stuecklisteIId, lagerIId,
					new Boolean(bMitStuecklistenkommentar), new Boolean(bUnterstuecklistenEinbinden),
					new Boolean(bGleichePositionenZusammenfassen), nLossgroesse, bSortiertNachArtikelbezeichnung,
					iOptionLager, fremdfertigungAufloesen, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printMinderverfuegbarkeit(Integer stuecklisteIId, boolean inFertigungBeruecksichtigen)
			throws Throwable {
		try {
			return stuecklisteReportFac.printMinderverfuegbarkeit(stuecklisteIId, inFertigungBeruecksichtigen,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printArbeitsplan(Integer stuecklisteIId, BigDecimal nLossgroesse) throws Throwable {
		try {
			return stuecklisteReportFac.printArbeitsplan(stuecklisteIId, nLossgroesse, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printLoseAktualisiert(TreeMap<String, Object[]> tmAufgeloesteFehlmengen,
			Integer stuecklisteIId, boolean bInclAusgegebenUndInProduktion) throws Throwable {
		try {
			return stuecklisteReportFac.printLoseAktualisiert(tmAufgeloesteFehlmengen, stuecklisteIId,
					bInclAusgegebenUndInProduktion, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printGesamtkalkulation(Integer stuecklisteIId, BigDecimal nLosgroesse,
			boolean lief1PreisInKalkpreisUebernehmen, boolean bMitPreisenDerLetzten2Jahre,
			boolean unterstuecklistenVerdichten, boolean bUeberAlleMandanten, boolean nachArtikelcnrsortieren,
			boolean bFremdfertigungAufloesen, boolean minBSMengeUndVPEBeruecksichtigen,
			Double dMaterialgemeinkostenfaktor, Double dArbeitszeitgemeinkostenfaktor,
			Double dFertigungsgemeinkostenfaktor, boolean gesamtmengeMaterialBeruecksichtigen, Date preisGueltig) throws Throwable {
		try {
			return stuecklisteReportFac.printGesamtkalkulation(stuecklisteIId, nLosgroesse,
					lief1PreisInKalkpreisUebernehmen, bMitPreisenDerLetzten2Jahre, unterstuecklistenVerdichten,
					bUeberAlleMandanten, nachArtikelcnrsortieren, bFremdfertigungAufloesen,
					minBSMengeUndVPEBeruecksichtigen, dMaterialgemeinkostenfaktor, dArbeitszeitgemeinkostenfaktor,
					dFertigungsgemeinkostenfaktor, gesamtmengeMaterialBeruecksichtigen, preisGueltig, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printGesamtkalkulationKonfigurator(Integer stuecklisteIId, BigDecimal nLosgroesse,
			boolean lief1PreisInKalkpreisUebernehmen, boolean bMitPreisenDerLetzten2Jahre,
			boolean unterstuecklistenVerdichten, boolean bUeberAlleMandanten, boolean bFremdfertigungAufloesen,

			Map<String, Object> konfigurationsWerte, Integer kundeIIdUebersteuert,
			boolean minBSMengeUndVPEBeruecksichtigen, Double dMaterialgemeinkostenfaktor,
			Double dArbeitszeitgemeinkostenfaktor, Double dFertigungsgemeinkostenfaktor, Date preisGueltig) throws Throwable {
		try {
			return stuecklisteReportFac.printGesamtkalkulationKonfigurator(stuecklisteIId, nLosgroesse,
					lief1PreisInKalkpreisUebernehmen, bMitPreisenDerLetzten2Jahre, unterstuecklistenVerdichten,
					bUeberAlleMandanten, bFremdfertigungAufloesen, konfigurationsWerte, kundeIIdUebersteuert,
					minBSMengeUndVPEBeruecksichtigen, dMaterialgemeinkostenfaktor, dArbeitszeitgemeinkostenfaktor,
					dFertigungsgemeinkostenfaktor, preisGueltig, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

}
