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
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.BuchungsjournalReportParameter;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.LiquititaetsvorschauImportDto;
import com.lp.server.finanz.service.PrintKontoblaetterModel;
import com.lp.server.finanz.service.ReportErfolgsrechnungKriterienDto;
import com.lp.server.finanz.service.ReportSaldenlisteKriterienDto;
import com.lp.server.finanz.service.ReportUvaKriterienDto;
import com.lp.server.finanz.service.SepaImportFac;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p>
 * <I>Business-Delegate fuer das Finanzmodul</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>13. 05. 2005</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.25 $
 */
public class FinanzReportDelegate extends Delegate {
	private Context context;
	private FinanzReportFac finanzReportFac;

	public FinanzReportDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			finanzReportFac = lookupFac(context, FinanzReportFac.class);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Alle Konten drucken.
	 * 
	 * @param kontotypCNr
	 *            kontotyp
	 * @param bMitVersteckten
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printAlleKonten(String kontotypCNr,
			boolean bMitVersteckten) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printAlleKonten(LPMain.getTheClient(),
					kontotypCNr, bMitVersteckten);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Buchungen auf einem Konto drucken.
	 * 
	 * @param kontoIId
	 *            Integer
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printBuchungenAufKonto(Integer kontoIId)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printBuchungenAufKonto(
					LPMain.getTheClient(), kontoIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Buchungen int einem Buchungsjournal drucken.
	 * 
	 * @param buchungsjournalIId
	 *            Integer
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printBuchungenInBuchungsjournal(
			Integer buchungsjournalIId) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printBuchungenInBuchungsjournal(
					LPMain.getTheClient(), buchungsjournalIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Buchungsjournal drucken.
	 * 
	 * @param buchungsjournalIId
	 *            Integer
	 * @param dVon
	 * @param dBis
	 * @param storniert
	 * @param bDatumsfilterIstBuchungsdatum
	 * @param text
	 * @param belegnummer
	 * @param betrag
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	// public JasperPrintLP printBuchungsjournal(Integer buchungsjournalIId,
	// Date dVon, Date dBis, boolean storniert,
	// boolean bDatumsfilterIstBuchungsdatum, String text,
	// String belegnummer, BigDecimal betrag, String kontonummer) throws
	// ExceptionLP {
	// JasperPrintLP print = null;
	// try {
	// print = this.finanzReportFac.printBuchungsjournal(
	// LPMain.getTheClient(), buchungsjournalIId, dVon, dBis,
	// storniert, bDatumsfilterIstBuchungsdatum, text,
	// belegnummer, betrag, kontonummer);
	// } catch (Throwable ex) {
	// handleThrowable(ex);
	// }
	// return print;
	// }

	/**
	 * Das detailierte Buchungsjournal drucken
	 * 
	 * @param params
	 * @return den Ausdruck
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printBuchungsjournal(
			BuchungsjournalReportParameter params) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printBuchungsjournal(
					LPMain.getTheClient(), params);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * RA-Schreiben drucken.
	 * 
	 * @param mahnungIId
	 *            Integer
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printRASchreiben(Integer mahnungIId)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printRASchreiben(
					LPMain.getTheClient(), mahnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * RA-Schreiben drucken.
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printRASchreibenFuerRechnung(Integer rechnungIId)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printRASchreibenFuerRechnung(
					LPMain.getTheClient(), rechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Mahnung drucken.
	 * 
	 * @param mahnungIId
	 *            Integer
	 * @param bMitLogo
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printMahnungAusMahnlauf(Integer mahnungIId,
			boolean bMitLogo) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printMahnungAusMahnlauf(
					LPMain.getTheClient(), mahnungIId, bMitLogo);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printEinfacheErfolgsrechnung(Integer iGeschaeftsjahr,
			boolean bMitLagerwert, boolean bMitHalbfertiglager, boolean bMitPersonalstunden,boolean bMitMaschinenstunden)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printEinfacheErfolgsrechnung(
					LPMain.getTheClient(), iGeschaeftsjahr, bMitLagerwert,
					bMitHalbfertiglager,bMitPersonalstunden, bMitMaschinenstunden);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printKassenjournal(Integer kassabuchIId,
			Integer iGeschaeftsjahr) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printKassenjournal(kassabuchIId,
					iGeschaeftsjahr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	
	public JasperPrintLP printUeberweisungsliste(Integer zahlungsvorschlaglaufIId) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printUeberweisungsliste(zahlungsvorschlaglaufIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Kontobl&auml;tter ausdrucken
	 * 
	 * @param kbModel
	 * @return JasperPrintLP
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printKontoblaetter(PrintKontoblaetterModel kbModel)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printKontoblaetter(kbModel,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return print;
	}

	/**
	 * Buchungsbeleg ausdrucken
	 * 
	 * @param buchungIId
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printBuchungsbeleg(Integer buchungIId)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = finanzReportFac.printBuchungsbeleg(buchungIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return print;
	}

	public JasperPrintLP printKassabuch(PrintKontoblaetterModel kbModel)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printKassabuch(kbModel,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return print;
	}

	public JasperPrintLP printOffenPosten(String kontotypCNr,
			Integer geschaeftsjahr, Integer kontoIId,
			java.sql.Timestamp tStichtag, boolean sortAlphabethisch)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printOffenePosten(kontotypCNr,
					geschaeftsjahr, kontoIId, tStichtag, LPMain.getTheClient(),
					sortAlphabethisch);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printLiquiditaetsvorschau(BigDecimal kontostand,
			BigDecimal kreditlimit, Integer kalenderwochen,
			boolean bTerminNachZahlungsmoral, boolean bMitPlankosten,
			ArrayList<LiquititaetsvorschauImportDto> alPlankosten,
			boolean bMitOffenenAngeboten, boolean bMitOffenenBestellungen,
			boolean bMitOffenenAuftraegen, boolean bMitAbgaben,
			boolean bERTerminNachFreigabedatum,
			boolean bMitABZahlungsUndZeitplan, boolean bOhneAndereMandanten) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printLiquiditaetsvorschau(kontostand,
					kreditlimit, kalenderwochen, bTerminNachZahlungsmoral,
					bMitPlankosten, alPlankosten, bMitOffenenAngeboten,
					bMitOffenenBestellungen, bMitOffenenAuftraegen,
					bMitAbgaben, bERTerminNachFreigabedatum,
					bMitABZahlungsUndZeitplan, bOhneAndereMandanten, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Mahnung drucken.
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param mahnstufeIId
	 *            Integer
	 * @param bMitLogo
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printMahnungFuerRechnung(Integer rechnungIId,
			Integer mahnstufeIId, boolean bMitLogo) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = finanzReportFac.printMahnungFuerRechnung(
					LPMain.getTheClient(), rechnungIId, mahnstufeIId, bMitLogo);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Alle Mahnungen eines Mahnlaufes drucken.
	 * 
	 * @param mahnlaufIId
	 *            Integer
	 * @param bMitLogo
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	public JasperPrintLP[] printSammelMahnungen(Integer mahnlaufIId,
			Boolean bMitLogo) throws ExceptionLP {
		JasperPrintLP[] print = null;
		try {
			print = this.finanzReportFac.printSammelmahnung(mahnlaufIId,
					bMitLogo, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Saldenliste drucken.
	 * 
	 * @param krit
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printSaldenliste(ReportSaldenlisteKriterienDto krit)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printSaldenliste(LPMain.getTheClient()
					.getMandant(), krit, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printAenderungenKonto(Integer kontoIId)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printAenderungenKonto(kontoIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * UVA drucken
	 * 
	 * @param krit
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * 
	 */
	public JasperPrintLP printUva(ReportUvaKriterienDto krit)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printUva(LPMain.getTheClient()
					.getMandant(), krit, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Mahnlauf drucken.
	 * 
	 * @param reportJournalKriterienDtoI
	 *            ReportJournalKriterienDto
	 * @param mahnlaufIId
	 *            Integer
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printMahnlauf(
			ReportJournalKriterienDto reportJournalKriterienDtoI,
			Integer mahnlaufIId) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = finanzReportFac.printMahnlauf(reportJournalKriterienDtoI,
					mahnlaufIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printSteuerkategorien() throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = finanzReportFac
					.printSteuerkategorien(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Intrastat-Vorschau drucken.
	 * 
	 * @param sVerfahren
	 *            ReportJournalKriterienDto
	 * @param dVon
	 *            Integer
	 * @param dBis
	 *            Date
	 * @param bdTransportkosten
	 *            BigDecimal
	 * @return JasperPrint
	 * @throws ExceptionLP
	 */
	public JasperPrintLP printIntrastatVorschau(String sVerfahren, Date dVon,
			Date dBis, BigDecimal bdTransportkosten) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = finanzReportFac.printIntrastatVorschau(sVerfahren, dVon,
					dBis, bdTransportkosten, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printErfolgsrechnung(
			ReportErfolgsrechnungKriterienDto kriterien, boolean bBilanz,
			boolean bDetails, boolean bEroeffnungsbilanz) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.finanzReportFac.printErfolgsrechnung(LPMain
					.getTheClient().getMandant(), kriterien, bBilanz, bDetails,
					bEroeffnungsbilanz, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printUstVerprobung(ReportUvaKriterienDto krit)
			throws ExceptionLP {
		try {
			return finanzReportFac.printUstVerprobung(krit,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}
}
