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
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.rechnung.service.AbrechnungsvorschlagDto;
import com.lp.server.rechnung.service.AbrechnungsvorschlagFac;
import com.lp.server.rechnung.service.AbrechnungsvorschlagUeberleitenDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.rechnung.service.RechnungkontierungDto;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.rechnung.service.ReportRechnungJournalKriterienDto;
import com.lp.server.rechnung.service.VerrechnungsmodellDto;
import com.lp.server.rechnung.service.VerrechnungsmodelltagDto;
import com.lp.server.rechnung.service.VorkassepositionDto;
import com.lp.server.rechnung.service.ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto;
import com.lp.server.rechnung.service.ZeitinfoTransferDto;
import com.lp.server.rechnung.service.ZugferdResult;
import com.lp.server.system.service.BelegPruefungDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.service.Artikelset;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * <I>BusinessDelegate zum Rechnungsmodul</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.55 $
 */
public class RechnungDelegate extends Delegate {
	private Context context;
	private RechnungFac rechnungFac;
	private RechnungReportFac rechnungReportFac;
	private AbrechnungsvorschlagFac abrechnungsvorschlagFac;

	public RechnungDelegate() throws ExceptionLP {
		try {
			// @Todo: Facades nur bei gebrauch wenn null instanzieren anstatt im
			// Konstruktor
			context = new InitialContext();
			rechnungFac = lookupFac(context, RechnungFac.class);
			rechnungReportFac = lookupFac(context, RechnungReportFac.class);
			abrechnungsvorschlagFac = lookupFac(context, AbrechnungsvorschlagFac.class);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Speichern einer Rechnung.
	 * 
	 * @param rechnungDto die zu speichernde Rechnung
	 * @throws ExceptionLP
	 * @return RechnungDto
	 */
	// delegateexc: 0 nur ExceptionLP werfen!!!
	public RechnungDto updateRechnung(RechnungDto rechnungDto) throws ExceptionLP {
		RechnungDto rechnungDto2Return = null;
		try {
			if (rechnungDto.getIId() != null) {
				rechnungDto.setTAendern(new Timestamp(System.currentTimeMillis()));
				rechnungDto2Return = rechnungFac.updateRechnung(rechnungDto, LPMain.getTheClient());
			} else {
				rechnungDto2Return = rechnungFac.createRechnung(rechnungDto, LPMain.getTheClient());
			}
		} catch (Throwable ex) {
			// delegateexc: 1 so fange ich Exceptions vom server und verarbeite
			// sie
			handleThrowable(ex);
		}
		return rechnungDto2Return;
	}

	public void updateRechnungStatus(Integer rechnungIId, String statusNeu, java.sql.Date bezahltdatum)
			throws ExceptionLP {
		try {
			rechnungFac.updateRechnungStatus(rechnungIId, statusNeu, bezahltdatum);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateRechnungVertreter(Integer rechnungIId, Integer personalIIdVertreter_Neu) throws ExceptionLP {
		try {
			rechnungFac.updateRechnungVertreter(rechnungIId, personalIIdVertreter_Neu, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateRechnungStatistikadresse(Integer rechnungIId, Integer kundeIIdStatistikadresseNeu)
			throws ExceptionLP {
		try {
			rechnungFac.updateRechnungStatistikadresse(rechnungIId, kundeIIdStatistikadresseNeu, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateRechnungZahlungsplan(Integer rechnungIId, BigDecimal bdZahlbetrag, Integer iZahltag)
			throws ExceptionLP {
		try {
			rechnungFac.updateRechnungZahlungsplan(rechnungIId, bdZahlbetrag, iZahltag);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Finden einer Rechnung anhand Primaerschluessel.
	 * 
	 * @param iId Integer
	 * @throws ExceptionLP
	 * @return RechnungDto
	 */
	public RechnungDto rechnungFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return rechnungFac.rechnungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungDto[] rechnungFindByAuftragIId(Integer auftragId) throws ExceptionLP {
		try {
			return rechnungFac.rechnungFindByAuftragIId(auftragId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungDto istLieferscheinBereitsInProformarechnung(Integer lieferscheinIId) throws ExceptionLP {
		try {
			return rechnungFac.istLieferscheinBereitsInProformarechnung(lieferscheinIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungDto findRechnungOderGutschriftByCNr(String cNr, String rechnungsart) throws ExceptionLP {
		try {
			RechnungDto[] array = rechnungFac.rechnungFindByCNrMandantCNrOhneExc(cNr,
					LPMain.getTheClient().getMandant());
			if (array == null)
				return null;
			for (RechnungDto dto : array) {
				if (dto.getRechnungartCNr().equals(rechnungsart))
					return dto;
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public RechnungDto findGytschriftByRechnungTypCNr(String cNr, String rechnungstyp) throws ExceptionLP {
		try {
			RechnungDto[] array = rechnungFac.rechnungFindByCNrMandantCNrOhneExc(cNr,
					LPMain.getTheClient().getMandant());
			if (array == null)
				return null;
			for (RechnungDto dto : array) {
				String typ = DelegateFactory.getInstance().getRechnungServiceDelegate()
						.rechnungartFindByPrimaryKey(dto.getRechnungartCNr()).getRechnungtypCNr();

				if (typ.equals(rechnungstyp))
					return dto;
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public int getAnzahlMengenbehafteteRechnungpositionen(Integer rechnungIId) throws ExceptionLP {
		try {
			return rechnungFac.getAnzahlMengenbehafteteRechnungpositionen(rechnungIId, LPMain.getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return 0;
	}

	public RechnungDto rechnungFindByCNr(String cNr) throws ExceptionLP {
		return findRechnungOderGutschriftByCNr(cNr, RechnungFac.RECHNUNGTYP_RECHNUNG);
	}

	public RechnungDto gutschriftFindByCNr(String cNr) throws ExceptionLP {
		return findRechnungOderGutschriftByCNr(cNr, RechnungFac.RECHNUNGTYP_GUTSCHRIFT);
	}

	public void removeRechnungPosition(RechnungPositionDto rechnungPositionDto) throws ExceptionLP {
		try {
			rechnungFac.removeRechnungPosition(rechnungPositionDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeVerrechnungsmodell(VerrechnungsmodellDto dto) throws ExceptionLP {
		try {
			abrechnungsvorschlagFac.removeVerrechnungsmodell(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeVerrechnungsmodelltag(VerrechnungsmodelltagDto dto) throws ExceptionLP {
		try {
			abrechnungsvorschlagFac.removeVerrechnungsmodelltag(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public RechnungPositionDto updateRechnungPosition(RechnungPositionDto rechnungPositionDto, Integer lagerIId)
			throws ExceptionLP {
		try {
			if (rechnungPositionDto.getIId() != null) {
				return rechnungFac.updateRechnungPosition(rechnungPositionDto, LPMain.getTheClient());
			} else {
				return rechnungFac.createRechnungPosition(rechnungPositionDto, lagerIId, LPMain.getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungPositionDto updateRechnungPosition(RechnungPositionDto rechnungPositionDto, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> identities) throws ExceptionLP {
		try {
			if (rechnungPositionDto.getIId() != null) {
				return rechnungFac.updateRechnungPosition(rechnungPositionDto, identities, LPMain.getTheClient());
			} else {
				return rechnungFac.createRechnungPosition(rechnungPositionDto, lagerIId, identities,
						LPMain.getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungPositionDto updateRechnungPosition(RechnungPositionDto rechnungPositionDto, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> identities, Artikelset artikelset) throws ExceptionLP {
		try {
			if (rechnungPositionDto.getIId() != null) {
				return rechnungFac.updateRechnungPosition(rechnungPositionDto, identities, artikelset,
						LPMain.getTheClient());
			} else {
				return rechnungFac.createRechnungPosition(rechnungPositionDto, lagerIId, identities,
						LPMain.getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungPositionDto createRechnungposition(RechnungPositionDto pDto, Integer lagerIId,
			boolean bNichtMengenbehafteteAuftragspositionErledigen) throws ExceptionLP {
		try {
			return rechnungFac.createRechnungPosition(pDto, lagerIId, bNichtMengenbehafteteAuftragspositionErledigen,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public RechnungPositionDto createRechnungposition(RechnungPositionDto pDto, Integer lagerIId) throws ExceptionLP {
		try {
			return rechnungFac.createRechnungPosition(pDto, lagerIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public Integer createVerrechnungsmodell(VerrechnungsmodellDto dto) throws ExceptionLP {
		try {
			return abrechnungsvorschlagFac.createVerrechnungsmodell(dto);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public Integer createVerrechnungsmodelltag(VerrechnungsmodelltagDto dto) throws ExceptionLP {
		try {
			return abrechnungsvorschlagFac.createVerrechnungsmodelltag(dto);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public RechnungPositionDto rechnungPositionFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return rechnungFac.rechnungPositionFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungPositionDto rechnungPositionFindByLieferscheinIId(Integer lieferscheinIId) throws ExceptionLP {
		try {
			return rechnungFac.rechnungPositionFindByLieferscheinIId(lieferscheinIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungPositionDto[] rechnungPositionFindByRechnungIId(Integer rechnungIId) throws ExceptionLP {
		try {
			return rechnungFac.rechnungPositionFindByRechnungIId(rechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Rechnung drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param pKey          Integer
	 * @param druckLocale   Locale
	 * @param bMitLogo      Boolean
	 * @param iAnzahlKopien Integer
	 */
	public JasperPrintLP[] printRechnung(Integer pKey, Locale druckLocale, Boolean bMitLogo, Integer iAnzahlKopien,
			String sDrucktype) throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP[] print = null;
		try {
			print = this.rechnungReportFac.printRechnung(pKey, druckLocale, bMitLogo, iAnzahlKopien, sDrucktype,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	public JasperPrintLP printRechnungAlsMahnung(Integer pKey, Integer iMahnstufe, Locale druckLocale, Boolean bMitLogo,
			String sDrucktype) throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP print = null;
		try {
			print = this.rechnungReportFac.printRechnungAlsMahnung(pKey, druckLocale, bMitLogo, iMahnstufe, sDrucktype,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Proformarechnung drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param pKey          Integer
	 * @param druckLocale   Locale
	 * @param bMitLogo      Boolean
	 * @param iAnzahlKopien Integer
	 */
	public JasperPrintLP[] printProformarechnung(Integer pKey, Locale druckLocale, Boolean bMitLogo,
			Integer iAnzahlKopien, String sDrucktype) throws ExceptionLP {
		JasperPrintLP[] print = null;
		try {
			print = this.rechnungReportFac.printProformarechnung(pKey, druckLocale, bMitLogo, iAnzahlKopien, sDrucktype,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Umsatz aller Rechnungen drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param iGeschaeftsjahr  Integer
	 * @param bMitGutschriften Boolean
	 */
	public JasperPrintLP printRechnungenUmsatz(Integer iGeschaeftsjahr, Boolean bMitGutschriften,
			boolean bMitDeckungsbeitrag, boolean bOhneAndereMandanten, boolean bMitOffenen) throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP print = null;
		try {
			print = this.rechnungReportFac.printRechnungenUmsatz(LPMain.getTheClient(), iGeschaeftsjahr,
					bMitGutschriften, bMitDeckungsbeitrag, bOhneAndereMandanten, bMitOffenen);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	public JasperPrintLP printNichtabgerechneteAnzahlungen() throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP print = null;
		try {
			print = this.rechnungReportFac.printNichtabgerechneteAnzahlungen(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Offene Rechnungen drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param krit Date
	 */
	public JasperPrintLP printRechnungenOffene(ReportRechnungJournalKriterienDto krit) throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP print = null;
		try {
			print = this.rechnungReportFac.printRechnungenOffene(krit, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Gutschrift drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param pKey          Integer
	 * @param druckLocale   Locale
	 * @param bMitLogo      Boolean
	 * @param iAnzahlKopien Integer
	 */
	public JasperPrintLP[] printGutschrift(Integer pKey, Locale druckLocale, Boolean bMitLogo, Integer iAnzahlKopien,
			String sDrucktype) throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP[] print = null;
		try {
			print = this.rechnungReportFac.printGutschrift(pKey, druckLocale, bMitLogo, iAnzahlKopien, sDrucktype,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Alle Gutschriften drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param krit Date
	 */
	public JasperPrintLP printGutschriftenAlle(ReportRechnungJournalKriterienDto krit) throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP print = null;
		try {
			print = this.rechnungReportFac.printGutschriftenAlle(krit, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Alle Rechnungen drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param krit Date
	 */
	public JasperPrintLP printRechnungenAlle(ReportRechnungJournalKriterienDto krit) throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP print = null;
		try {
			print = this.rechnungReportFac.printRechnungenAlle(krit, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	public JasperPrintLP printZusammenfassendeMeldung(java.sql.Date dVon, java.sql.Date dBis,
			Integer partnerIIdFinanzamt) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = this.rechnungReportFac.printZusammenfassendeMeldung(dVon, dBis, partnerIIdFinanzamt,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	public JasperPrintLP printWarenausgangsjournal(ReportRechnungJournalKriterienDto krit) throws ExceptionLP {
		JasperPrintLP print = null;
		myLogger.entry();
		try {
			print = this.rechnungReportFac.printWarenausgangsjournal(krit, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Alle Proformarechnungen drucken.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param krit Date
	 */
	public JasperPrintLP printProformarechnungenAlle(ReportRechnungJournalKriterienDto krit) throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP print = null;
		try {
			print = this.rechnungReportFac.printProformarechnungenAlle(krit, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	/**
	 * Status einer bestehenden Rechnung aendern.
	 * 
	 * @param rechnungDto RechnungDto
	 * @param pStatus     String
	 * @throws ExceptionLP
	 */
	public void setRechnungstatus(RechnungDto rechnungDto, String pStatus) throws ExceptionLP {
		myLogger.entry();
		rechnungDto.setStatusCNr(pStatus);

		try {
			this.updateRechnung(rechnungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public RechnungzahlungDto zahlungFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return rechnungFac.zahlungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	// public RechnungzahlungDto zahlungMitFibuKommentarByPrimaryKey(Integer
	// zahlungId, TheClientDto theClientDto) throws ExceptionLP {
	// try {
	// return rechnungFac.zahlungMitFibuKommentarByPrimaryKey(zahlungId,
	// theClientDto);
	// } catch (Throwable ex) {
	// handleThrowable(ex);
	// return null;
	// }
	// }

	public void createMonatsrechnungen() throws ExceptionLP {
		try {
			rechnungFac.createMonatsrechnungen(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public void erstelleAbrechnungsvorschlag(java.sql.Date dStichtag, boolean bAltenLoeschen, boolean bMitZeitdaten,
			boolean bMitTelefonzeiten, boolean bMitReisezeiten, boolean bMitEingangsrechnungen,
			boolean bMitMaschinenzeiten) throws ExceptionLP {
		try {
			abrechnungsvorschlagFac.erstelleAbrechnungsvorschlag(dStichtag, bAltenLoeschen, bMitZeitdaten,
					bMitTelefonzeiten, bMitReisezeiten, bMitEingangsrechnungen, bMitMaschinenzeiten,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public VerrechnungsmodellDto verrechnungsmodellFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return abrechnungsvorschlagFac.verrechnungsmodellFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public VerrechnungsmodelltagDto verrechnungsmodelltagFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return abrechnungsvorschlagFac.verrechnungsmodelltagFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public AbrechnungsvorschlagDto abrechnungsvorschlagFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return abrechnungsvorschlagFac.abrechnungsvorschlagFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getSelektiertenBetrag(ArrayList<Integer> iids) throws ExceptionLP {
		try {
			return abrechnungsvorschlagFac.getSelektiertenBetrag(iids);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getSelektierteSpesen(ArrayList<Integer> iids) throws ExceptionLP {
		try {
			return abrechnungsvorschlagFac.getSelektierteSpesen(iids);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getSelektierteKilometer(ArrayList<Integer> iids) throws ExceptionLP {
		try {
			return abrechnungsvorschlagFac.getSelektierteKilometer(iids);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getSelektierteStunden(ArrayList<Integer> iids) throws ExceptionLP {
		try {
			return abrechnungsvorschlagFac.getSelektierteStunden(iids);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public AbrechnungsvorschlagUeberleitenDto erzeugeUeberleitungsvorschlag(String art,
			ArrayList<Integer> selektierteIIds) throws ExceptionLP {
		try {
			return abrechnungsvorschlagFac.erzeugeUeberleitungsvorschlag(art, selektierteIIds, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void selektiertePositionenManuellErledigen(ArrayList<Integer> iids) throws ExceptionLP {
		try {
			abrechnungsvorschlagFac.selektiertePositionenManuellErledigen(iids, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void selektiertenPositionenKundeZuordnen(ArrayList<Integer> iids, Integer kundeIId) throws ExceptionLP {
		try {
			abrechnungsvorschlagFac.selektiertenPositionenKundeZuordnen(iids, kundeIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void aendereVerrechenbarkeit(ArrayList<Integer> abrechnugnsvorschlagIIds, Double fProzent)
			throws ExceptionLP {
		try {
			abrechnungsvorschlagFac.aendereVerrechenbarkeit(abrechnugnsvorschlagIIds, fProzent, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void abrechnungsvorschlagUeberleitenReise(ArrayList<Integer> selektierteIIds, Integer artikelIId,
			BigDecimal bdBetragInMandantenwaehrung, String kommentar, Integer artikelIIdKilometer,
			BigDecimal bdKilometer, String kommentarKilometer, BigDecimal bdSpesen, String kommentarSpesen,
			Integer artikelIIdSpesen, boolean bErledigt, java.sql.Date tBelegdatum) throws ExceptionLP {
		try {
			abrechnungsvorschlagFac.abrechnungsvorschlagUeberleitenReise(selektierteIIds, artikelIId,
					bdBetragInMandantenwaehrung, kommentar, artikelIIdKilometer, bdKilometer, kommentarKilometer,
					bdSpesen, kommentarSpesen, artikelIIdSpesen, bErledigt, tBelegdatum, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void abrechnungsvorschlagUeberleitenZeitdaten(
			Map<String, ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto> zeilen,
			boolean bErledigt, java.sql.Date tBelegdatum) throws ExceptionLP {
		try {
			abrechnungsvorschlagFac.abrechnungsvorschlagUeberleitenZeitdaten(zeilen, bErledigt, tBelegdatum,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void abrechnungsvorschlagUeberleitenER(ArrayList<Integer> seketierteIIds, Integer artikelIId,
			BigDecimal bdBetrag, BigDecimal bdBetragInMandantenwaehrungInclAufschlag, String kommentar,
			boolean bErledigt, java.sql.Date tBelegdatum) throws ExceptionLP {
		try {
			abrechnungsvorschlagFac.abrechnungsvorschlagUeberleitenER(seketierteIIds, artikelIId, bdBetrag,
					bdBetragInMandantenwaehrungInclAufschlag, kommentar, bErledigt, tBelegdatum, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer kopiereVerrechnungsmodelltag(Integer verrechnungsmodellmodelltagIId) throws ExceptionLP {
		try {
			return abrechnungsvorschlagFac.kopiereVerrechnungsmodelltag(verrechnungsmodellmodelltagIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Eine Rechnung aus einem Lieferschein erstellen.
	 * 
	 * @return Id der neuen Rechnung
	 * @param lieferscheinIId Integer
	 * @param rechnungDto     RechnungDto
	 * @throws ExceptionLP
	 */
	public Integer createRechnungAusLieferschein(Integer lieferscheinIId, RechnungDto rechnungDto,
			String rechnungstypCNr, java.sql.Date neuDatum, InternalFrame internalFrame) throws ExceptionLP {
		try {
			if (lieferscheinIId != null) {

				HashMap<Integer, Integer> hmKunden = new HashMap<Integer, Integer>();

				LieferscheinDto lsDto = DelegateFactory.getInstance().getLsDelegate()
						.lieferscheinFindByPrimaryKey(lieferscheinIId);

				hmKunden.put(lsDto.getKundeIIdLieferadresse(), lsDto.getKundeIIdLieferadresse());

				if (!hmKunden.containsKey(lsDto.getKundeIIdRechnungsadresse())) {
					hmKunden.put(lsDto.getKundeIIdRechnungsadresse(), lsDto.getKundeIIdRechnungsadresse());
				}

				Iterator<Integer> it = hmKunden.keySet().iterator();

				while (it.hasNext()) {
					DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(it.next(), LocaleFac.BELEGART_RECHNUNG,
							internalFrame);
				}

			}

			Integer rechnungIIdNeu = rechnungFac.createRechnungAusLieferschein(lieferscheinIId, rechnungDto,
					rechnungstypCNr, neuDatum, LPMain.getTheClient());

			RechnungDto reDto = rechnungFindByPrimaryKey(rechnungIIdNeu);
			// SP6638
			DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.pruefeObAnsprechpartnerVersteckt(reDto.getAnsprechpartnerIId());

			return rechnungIIdNeu;

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createRechnungAusRechnung(Integer rechnungIId, java.sql.Date neuDatum,
			boolean bUebernimmKonditionenDesKunden, boolean bZahlungsplanUebernehmen, InternalFrame internalFrame)
			throws ExceptionLP {

		boolean kopiereAuftragsbezugPositionen = false;

		try {
			if (rechnungIId != null) {
				RechnungDto reDto = rechnungFindByPrimaryKey(rechnungIId);

				DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(reDto.getKundeIId(),
						LocaleFac.BELEGART_RECHNUNG, internalFrame);

				if (hatRechnungPositionenMitAuftragsbezug(rechnungIId)) {
					int answer = DialogFactory.showMeldung(
							LPMain.getMessageTextRespectUISPr("rech.ausrechnung.auftragsbezugpositionen",
									reDto.getCNr()),
							LPMain.getTextRespectUISPr("rech.ausrechnung.auftragsbezugpositionen.titel"),
							JOptionPane.YES_NO_CANCEL_OPTION);

					if (answer == JOptionPane.CANCEL_OPTION)
						return null;
					kopiereAuftragsbezugPositionen = JOptionPane.YES_OPTION == answer;
				}
			}

			Integer rechnungIIdNeu = rechnungFac.createRechnungAusRechnung(rechnungIId, neuDatum,
					bUebernimmKonditionenDesKunden, kopiereAuftragsbezugPositionen, LPMain.getTheClient(),
					bZahlungsplanUebernehmen);

			RechnungDto reDto = rechnungFindByPrimaryKey(rechnungIIdNeu);
			// SP6638
			DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.pruefeObAnsprechpartnerVersteckt(reDto.getAnsprechpartnerIId());

			return rechnungIIdNeu;

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createRechnungAusAngebot(Integer angebotIId, java.sql.Date neuDatum, InternalFrame internalFrame)
			throws ExceptionLP {
		try {
			if (angebotIId != null) {
				AngebotDto agDto = DelegateFactory.getInstance().getAngebotDelegate()
						.angebotFindByPrimaryKey(angebotIId);

				DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(agDto.getKundeIIdAngebotsadresse(),
						LocaleFac.BELEGART_RECHNUNG, internalFrame);

			}

			Integer rechnungIIdNeu = rechnungFac.createRechnungAusAngebot(angebotIId, neuDatum, LPMain.getTheClient());
			RechnungDto reDto = rechnungFindByPrimaryKey(rechnungIIdNeu);
			// SP6638
			DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.pruefeObAnsprechpartnerVersteckt(reDto.getAnsprechpartnerIId());

			return rechnungIIdNeu;

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Eine Rechnung aus mehrere Lieferschein gleiche Rechnungsadresse erstellen.
	 * 
	 * @return Id der neuen Rechnung
	 * @param lieferscheinIId Integer
	 * @param rechnungDto     RechnungDto
	 * @throws ExceptionLP
	 */
	public Integer createRechnungAusMehrereLieferscheine(Object[] keys, RechnungDto rechnungDto, String rechnungstypCNr,
			java.sql.Date neuDatum, InternalFrame internalFrame) throws ExceptionLP {
		try {
			HashMap<Integer, Integer> hmKunden = new HashMap<Integer, Integer>();
			for (int i = 0; i < keys.length; i++) {
				if (keys[i] != null) {

					LieferscheinDto lsDto = DelegateFactory.getInstance().getLsDelegate()
							.lieferscheinFindByPrimaryKey((Integer) keys[i]);
					if (!hmKunden.containsKey(lsDto.getKundeIIdLieferadresse())) {
						hmKunden.put(lsDto.getKundeIIdLieferadresse(), lsDto.getKundeIIdLieferadresse());
					}

					if (!hmKunden.containsKey(lsDto.getKundeIIdRechnungsadresse())) {
						hmKunden.put(lsDto.getKundeIIdRechnungsadresse(), lsDto.getKundeIIdRechnungsadresse());
					}

				}
			}

			Iterator<Integer> it = hmKunden.keySet().iterator();

			while (it.hasNext()) {
				DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(it.next(), LocaleFac.BELEGART_RECHNUNG,
						internalFrame);
			}

			return rechnungFac.createRechnungAusMehrereLieferscheine(keys, rechnungDto, rechnungstypCNr, neuDatum,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Eine Rechnung aus einem Auftrag erstellen.
	 * 
	 * @return Id der neuen Rechnung
	 * @param auftragIId Integer
	 * @throws ExceptionLP
	 */
	public Integer createRechnungAusAuftrag(Integer auftragIId, java.sql.Date neuDatum,boolean bSchlussrechnung, InternalFrame internalFrame)
			throws ExceptionLP {
		try {

			if (auftragIId != null) {

				HashMap<Integer, Integer> hmKunden = new HashMap<Integer, Integer>();

				AuftragDto abDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey(auftragIId);

				hmKunden.put(abDto.getKundeIIdAuftragsadresse(), abDto.getKundeIIdAuftragsadresse());
				if (!hmKunden.containsKey(abDto.getKundeIIdRechnungsadresse())) {
					hmKunden.put(abDto.getKundeIIdRechnungsadresse(), abDto.getKundeIIdRechnungsadresse());
				}

				if (!hmKunden.containsKey(abDto.getKundeIIdLieferadresse())) {
					hmKunden.put(abDto.getKundeIIdLieferadresse(), abDto.getKundeIIdLieferadresse());
				}

				Iterator<Integer> it = hmKunden.keySet().iterator();

				while (it.hasNext()) {
					DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(it.next(), LocaleFac.BELEGART_RECHNUNG,
							internalFrame);
				}

			}

			Integer rechnungIIdNeu = rechnungFac.createRechnungAusAuftrag(auftragIId, neuDatum,
					rabattAusRechnungsadresse(auftragIId), bSchlussrechnung, LPMain.getTheClient());
			RechnungDto reDto = rechnungFindByPrimaryKey(rechnungIIdNeu);
			// SP6638
			DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.pruefeObAnsprechpartnerVersteckt(reDto.getAnsprechpartnerIId());

			return rechnungIIdNeu;
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createAnzahlungsrechnungAusAuftrag(Integer auftragIId, java.sql.Date tBelegdatum,  InternalFrame internalFrame)
			throws ExceptionLP {
		try {

			if (auftragIId != null) {

				HashMap<Integer, Integer> hmKunden = new HashMap<Integer, Integer>();

				AuftragDto abDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey(auftragIId);

				hmKunden.put(abDto.getKundeIIdAuftragsadresse(), abDto.getKundeIIdAuftragsadresse());
				if (!hmKunden.containsKey(abDto.getKundeIIdRechnungsadresse())) {
					hmKunden.put(abDto.getKundeIIdRechnungsadresse(), abDto.getKundeIIdRechnungsadresse());
				}

				if (!hmKunden.containsKey(abDto.getKundeIIdLieferadresse())) {
					hmKunden.put(abDto.getKundeIIdLieferadresse(), abDto.getKundeIIdLieferadresse());
				}

				Iterator<Integer> it = hmKunden.keySet().iterator();

				while (it.hasNext()) {
					DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(it.next(), LocaleFac.BELEGART_RECHNUNG,
							internalFrame);
				}

			}

			Integer rechnungIIdNeu = rechnungFac.createAnzahlungsrechnungAusAuftrag(auftragIId, tBelegdatum, LPMain.getTheClient());

			return rechnungIIdNeu;
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Eine GS aus einer RE erstellen.
	 * 
	 * @return Id der neuen Rechnung
	 * @param rechnungIId Integer
	 * @throws ExceptionLP
	 */
	public Integer createGutschriftAusRechnung(Integer rechnungIId, java.sql.Date dBelegdatum) throws ExceptionLP {
		try {
			return rechnungFac.createGutschriftAusRechnung(rechnungIId, dBelegdatum, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Eine GS aus einer RE erstellen.
	 * 
	 * @return Id der neuen Rechnung
	 * @param rechnungIId Integer
	 * @throws ExceptionLP
	 */
	public Integer createGutschriftAusRechnung(Integer rechnungIId, java.sql.Date dBelegdatum, String gutschriftartCnr)
			throws ExceptionLP {
		try {
			return rechnungFac.createGutschriftAusRechnung(rechnungIId, dBelegdatum, gutschriftartCnr,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Den Status einer Rechnung auf "Angelegt" setzen.
	 * 
	 * @param rechnungIId Integer
	 * @throws ExceptionLP
	 */
	public void setRechnungStatusAufAngelegt(Integer rechnungIId) throws ExceptionLP {
		try {
			rechnungFac.setRechnungStatusAufAngelegt(rechnungIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Die summe aller Zahlungen auf eine Rechnung holen.
	 * 
	 * @param rechnungIId           Integer
	 * @param zahlungIIdAusgenommen Integer
	 * @throws ExceptionLP
	 * @return BigDecimal
	 */
	public BigDecimal getBereitsBezahltWertVonRechnungFw(Integer rechnungIId, Integer zahlungIIdAusgenommen)
			throws ExceptionLP {
		try {
			return rechnungFac.getBereitsBezahltWertVonRechnungFw(rechnungIId, zahlungIIdAusgenommen);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getAnzahlungenZuSchlussrechnungFw(Integer rechnungIId) throws ExceptionLP {
		try {
			return rechnungFac.getAnzahlungenZuSchlussrechnungFw(rechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getAnzahlungenZuSchlussrechnungUstFw(Integer rechnungIId) throws ExceptionLP {
		try {
			return rechnungFac.getAnzahlungenZuSchlussrechnungUstFw(rechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Die summe aller Zahlungen auf eine Rechnung holen.
	 * 
	 * @param rechnungIId           Integer
	 * @param zahlungIIdAusgenommen Integer
	 * @throws ExceptionLP
	 * @return BigDecimal
	 */
	public BigDecimal getBereitsBezahltWertBruttoVonRechnungFwOhneFSkonto(Integer rechnungIId,
			Integer zahlungIIdAusgenommen) throws ExceptionLP {
		try {
			return rechnungFac.getBereitsBezahltWertBruttoVonRechnungFwOhneFSkonto(rechnungIId, zahlungIIdAusgenommen);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Die summe aller Zahlungen auf eine Rechnung holen.
	 * 
	 * @param rechnungIId           Integer
	 * @param zahlungIIdAusgenommen Integer
	 * @throws ExceptionLP
	 * @return BigDecimal
	 */
	public BigDecimal getBereitsBezahltWertVonRechnung(Integer rechnungIId, Integer zahlungIIdAusgenommen)
			throws ExceptionLP {
		try {
			return rechnungFac.getBereitsBezahltWertVonRechnung(rechnungIId, zahlungIIdAusgenommen);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Die summe aller Zahlungen auf eine Rechnung holen.
	 * 
	 * @param rechnungIId           Integer
	 * @param zahlungIIdAusgenommen Integer
	 * @throws ExceptionLP
	 * @return BigDecimal
	 */
	public BigDecimal getBereitsBezahltWertVonRechnungUstFw(Integer rechnungIId, Integer zahlungIIdAusgenommen)
			throws ExceptionLP {
		try {
			return rechnungFac.getBereitsBezahltWertVonRechnungUstFw(rechnungIId, zahlungIIdAusgenommen);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Eine Zahlung anlegen.
	 * 
	 * @param zahlungDto Integer
	 * @param bErledigt  boolean
	 * @throws ExceptionLP
	 * @return BigDecimal
	 */
	public RechnungzahlungDto createZahlung(RechnungzahlungDto zahlungDto, boolean bErledigt) throws ExceptionLP {
		try {
			return rechnungFac.createZahlung(zahlungDto, bErledigt, LPMain.getTheClient());
		} catch (Throwable ex) {
			if (ex instanceof EJBExceptionLP)
				if (((EJBExceptionLP) ex).getCode() == EJBExceptionLP.FEHLER_RECHNUNG_BEREITS_BEZAHLT) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("rechnung.zahlung.rechnungistbereitsbezahlt"));
					return null;
				}
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Eine Zahlung anlegen.
	 * 
	 * @param zahlungDto Integer
	 * @param bErledigt  boolean
	 * @throws ExceptionLP
	 */
	public void updateZahlung(RechnungzahlungDto zahlungDto, boolean bErledigt) throws ExceptionLP {
		try {
			rechnungFac.updateZahlung(zahlungDto, bErledigt, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateVerrechnungsmodelltag(VerrechnungsmodelltagDto dto) throws ExceptionLP {
		try {
			abrechnungsvorschlagFac.updateVerrechnungsmodelltag(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateVerrechnungsmodell(VerrechnungsmodellDto dto) throws ExceptionLP {
		try {
			abrechnungsvorschlagFac.updateVerrechnungsmodell(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Eine Zahlung loeschen.
	 * 
	 * @param zahlungDto Integer
	 * @throws ExceptionLP
	 */
	public void removeZahlung(RechnungzahlungDto zahlungDto) throws ExceptionLP {
		try {
			rechnungFac.removeZahlung(zahlungDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void mahneRechnung(Integer rechnungIId, Integer mahnstufeIId, java.sql.Date dMahndatum) throws ExceptionLP {
		try {
			rechnungFac.mahneRechnung(rechnungIId, mahnstufeIId, dMahndatum, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public boolean setzeMahnstufeZurueck(Integer rechnungIId) throws ExceptionLP {
		try {
			return rechnungFac.setzeMahnstufeZurueck(rechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return false;
		}
	}

	public JasperPrintLP printZahlungsjournal(int iSortierung, java.sql.Date dVon, java.sql.Date dBis,
			Integer bankverbindungIId, boolean bSortierungNachKostenstelle) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = rechnungReportFac.printZahlungsjournal(LPMain.getTheClient(), iSortierung, dVon, dBis,
					bankverbindungIId, bSortierungNachKostenstelle);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printMaterialeinsatz(Integer iRechnungIId, boolean nachkalkulation) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = rechnungReportFac.printMaterialeinsatz(iRechnungIId, nachkalkulation, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public ArrayList<ZeitinfoTransferDto> printZeitnachweis(ArrayList<Integer> projektzeitenIIds, java.sql.Date dateVon,
			java.sql.Date dateBis) throws ExceptionLP {
		ArrayList<ZeitinfoTransferDto> prints = null;
		try {
			prints = rechnungReportFac.printZeitnachweis(projektzeitenIIds, dateVon, dateBis, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return prints;
	}

	public JasperPrintLP printAbrechnungsvorschlag() throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = abrechnungsvorschlagFac.printAbrechnungsvorschlag(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printVerrechnungsmodell(Integer verrechnungsmodellIId) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = abrechnungsvorschlagFac.printVerrechnungsmodell(verrechnungsmodellIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printManuellErledigteEnterledigen(Integer personalIId, Integer kundeIId, Integer auftragIId,
			DatumsfilterVonBis dVonBis) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = abrechnungsvorschlagFac.printManuellErledigteEnterledigen(personalIId, kundeIId, auftragIId,
					dVonBis, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public void vertauscheRechnungspositionMinus(Integer rowIndex, TableModel tableModel) throws ExceptionLP {
		try {
			// int pageCount = 0 ;

			Integer baseIId = (Integer) tableModel.getValueAt(rowIndex, 0);
			List<Integer> iidList = new ArrayList<Integer>();

			while (--rowIndex >= 0) {
				iidList.add((Integer) tableModel.getValueAt(rowIndex, 0));
			}

			rechnungFac.vertauscheRechnungspositionenMinus(baseIId, iidList, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheRechnungspositionPlus(Integer rowIndex, TableModel tableModel) throws ExceptionLP {
		try {
			Integer baseIId = (Integer) tableModel.getValueAt(rowIndex, 0);
			List<Integer> iidList = new ArrayList<Integer>();

			int maxRowCount = tableModel.getRowCount();
			while (++rowIndex < maxRowCount) {
				iidList.add((Integer) tableModel.getValueAt(rowIndex, 0));
			}

			rechnungFac.vertauscheRechnungspositionenPlus(baseIId, iidList, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public Boolean hatRechnungPositionen(Integer rechnungIId) throws ExceptionLP {
		try {
			return rechnungFac.hatRechnungPositionen(rechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return Boolean.FALSE;
		}
	}

	/**
	 * Warenausgangsjournal exportieren.
	 * 
	 * @return JasperPrint
	 * @throws ExceptionLP
	 * @param kundeIId    Integer
	 * @param von         Date
	 * @param bis         Date
	 * @param iSortierung Integer
	 */
	public String exportWAJournal(Integer kundeIId, java.sql.Date von, java.sql.Date bis, Integer iSortierung)
			throws ExceptionLP {
		myLogger.entry();
		String sText = null;
		try {
			sText = this.rechnungReportFac.exportWAJournal(LPMain.getTheClient(), kundeIId, von, bis,
					System.getProperty("line.separator"), iSortierung);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return sText;
	}

	public boolean gibtEsBereitsEineSchlussrechnungZuEinemAuftrag(Integer auftragIId) throws ExceptionLP {

		try {
			return this.rechnungFac.gibtEsBereitsEineSchlussrechnungZuEinemAuftrag(auftragIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return true;
		}

	}

	public void erstelleEinzelexport(Integer rechnungIId, String pfad, boolean bSortiertNachArtikelnummer)
			throws ExceptionLP {
		try {
			this.rechnungReportFac.erstelleEinzelexport(rechnungIId, pfad, bSortiertNachArtikelnummer,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Eine Rechnung manuell auf 'Erledigt' setzen.
	 * 
	 * @param iIdRechnungI PK der Rechnung
	 * @throws ExceptionLP Ausnahme
	 */
	public void manuellErledigen(Integer iIdRechnungI) throws ExceptionLP {
		try {
			rechnungFac.manuellErledigen(iIdRechnungI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void setzeMahnsperre(Integer rechnungIId, java.sql.Date tMahnsperre, String cMahnungsanmerkung)
			throws ExceptionLP {
		try {
			rechnungFac.setzeMahnsperre(rechnungIId, tMahnsperre, cMahnungsanmerkung, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void sortiereNachLieferscheinAnsprechpartner(Integer rechnungIId) throws ExceptionLP {
		try {
			rechnungFac.sortiereNachLieferscheinAnsprechpartner(rechnungIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void sortiereNachLieferscheinNummer(Integer rechnungIId) throws ExceptionLP {
		try {
			rechnungFac.sortiereNachLieferscheinNummer(rechnungIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer getISortUmLieferscheinNachAnsprechpartnerEinzusortieren(Integer rechnungIId, Integer lieferscheinIId)
			throws ExceptionLP {
		try {
			return rechnungFac.getISortUmLieferscheinNachAnsprechpartnerEinzusortieren(rechnungIId, lieferscheinIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getSummeVorkasseposition(Integer rechnungIId, Integer auftragpositonIId) throws ExceptionLP {
		try {
			return rechnungFac.getSummeVorkasseposition(rechnungIId, auftragpositonIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Den Status einer AR von 'Erledigt' auf 'Erfasst' setzen. <br>
	 * Diese Aktion ist nur moeglich, wenn der 'Erledigt' Status manuell gesetzt
	 * wurde.
	 * 
	 * @param rechnungIId PK der AR
	 * @throws ExceptionLP
	 */
	public void erledigungAufheben(Integer rechnungIId) throws ExceptionLP {
		try {
			rechnungFac.erledigungAufheben(rechnungIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void aktiviereBelegControlled(Integer iid, Timestamp t) throws ExceptionLP {
		try {
			BelegPruefungDto pruefungDto = rechnungFac.aktiviereBelegControlled(iid, t, LPMain.getTheClient());
			dialogBelegpruefung(pruefungDto);
			// // SP1881
			// DelegateFactory
			// .getInstance()
			// .getSystemDelegate()
			// .enthaeltEinVKBelegUmsatzsteuerObwohlKundeSteuerfrei(
			// LocaleFac.BELEGART_RECHNUNG, iid);
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
	}

	public Timestamp berechneBelegControlled(Integer iid) throws ExceptionLP {
		try {
			BelegPruefungDto pruefungDto = rechnungFac.berechneBelegControlled(iid, LPMain.getTheClient());
			dialogBelegpruefung(pruefungDto);
			return pruefungDto.getBerechnungsZeitpunkt();

			// SP1881
			// DelegateFactory
			// .getInstance()
			// .getSystemDelegate()
			// .enthaeltEinVKBelegUmsatzsteuerObwohlKundeSteuerfrei(
			// LocaleFac.BELEGART_RECHNUNG, iid);
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
		return null;
	}

	public void prufeSEPAMandatsreferenz(RechnungDto rechnungDto) throws Throwable {
		if (rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_ANGELEGT)) {

			int i = DelegateFactory.getInstance().getMandantDelegate().istMandatsreferenzAbgelaufen(
					rechnungDto.getZahlungszielIId(), rechnungDto.getKundeIId(), rechnungDto.getTBelegdatum());

			if (i == MandantFac.MANDATSREFERENZ_ABGELAUFEN) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.achtung"),
						LPMain.getTextRespectUISPr("lp.zahlungsziel.lastschrift.abgelaufen"));
			} else if (i == MandantFac.MANDATSREFERENZ_KEIN_BANKVERBINDUNG) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.achtung"),
						LPMain.getTextRespectUISPr("lp.zahlungsziel.lastschrift.keine.bankverbindung"));
			} else if (i == MandantFac.MANDATSREFERENZ_KEIN_GUELTIGKEITSDATUM) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.achtung"), LPMain.getTextRespectUISPr(
						"lp.zahlungsziel.lastschrift.bankverbindung.keinegueltigkeitmandatsnummer"));
			} else if (i == MandantFac.MANDATSREFERENZ_KEINE_GLAEUBIGERNUMMER) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.achtung"),
						LPMain.getTextRespectUISPr("lp.zahlungsziel.lastschrift.bankverbindung.keineglaeubigerid"));
			} else if (i == MandantFac.MANDATSREFERENZ_KEINE_MANDATSREFERENZNUMMER) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.achtung"),
						LPMain.getTextRespectUISPr("lp.zahlungsziel.lastschrift.bankverbindung.keinemandatsnummer"));
			}
		}
	}

	public Timestamp berechneAktiviereBelegControlled(Integer iid) throws ExceptionLP {
		try {
			BelegPruefungDto pruefungDto = rechnungFac.berechneAktiviereBelegControlled(iid, LPMain.getTheClient());
			dialogBelegpruefung(pruefungDto);
			return pruefungDto.getBerechnungsZeitpunkt();
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
		return null;
	}

	public void storniereRechnung(Integer rechnungIId) throws ExceptionLP {
		try {
			rechnungFac.storniereRechnung(rechnungIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void storniereRechnungRueckgaengig(Integer rechnungIId) throws ExceptionLP {
		try {
			rechnungFac.storniereRechnungRueckgaengig(rechnungIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public BigDecimal getUmsatzVomKundenImZeitraum(Integer kundeIId, java.sql.Date dVon, java.sql.Date dBis,
			boolean bStatistikadresse) throws ExceptionLP {
		BigDecimal bdUmsatz = null;
		try {
			bdUmsatz = rechnungFac.getUmsatzVomKundenImZeitraum(LPMain.getTheClient(), kundeIId, dVon, dBis,
					bStatistikadresse);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return bdUmsatz;
	}

	public BigDecimal getUmsatzVomKundenHeuer(Integer kundeIId, boolean bStatistikadresse, boolean geschaeftsjahr)
			throws ExceptionLP {
		BigDecimal bdUmsatz = null;
		try {
			bdUmsatz = rechnungFac.getUmsatzVomKundenHeuer(LPMain.getTheClient(), kundeIId, bStatistikadresse,
					geschaeftsjahr);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return bdUmsatz;
	}

	public BigDecimal getUmsatzVomKundenVorjahr(Integer kundeIId, boolean bStatistikadresse, boolean geschaeftsjahr)
			throws ExceptionLP {
		BigDecimal bdUmsatz = null;
		try {
			bdUmsatz = rechnungFac.getUmsatzVomKundenVorjahr(LPMain.getTheClient(), kundeIId, bStatistikadresse,
					geschaeftsjahr);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return bdUmsatz;
	}

	public Integer getAnzahlDerRechnungenVomKundenImZeitraum(Integer kundeIId, java.sql.Date dVon, java.sql.Date dBis,
			boolean bStatistikadresse) throws ExceptionLP {
		Integer iAnzahl = null;
		try {
			iAnzahl = rechnungFac.getAnzahlDerRechnungenVomKundenImZeitraum(LPMain.getTheClient(), kundeIId, dVon, dBis,
					bStatistikadresse);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iAnzahl;
	}

	public Integer getAnzahlDerRechnungenVomKundenHeuer(Integer kundeIId, boolean bStatistikadresse,
			boolean geschaeftsjahr) throws ExceptionLP {
		Integer iAnzahl = null;
		try {
			iAnzahl = rechnungFac.getAnzahlDerRechnungenVomKundenHeuer(LPMain.getTheClient(), kundeIId,
					bStatistikadresse, geschaeftsjahr);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iAnzahl;
	}

	public Integer getZahlungsmoraleinesKunden(Integer kundeIId, boolean bStatistikadresse) throws ExceptionLP {
		Integer iAnzahl = null;
		try {
			iAnzahl = rechnungFac.getZahlungsmoraleinesKunden(kundeIId, bStatistikadresse, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iAnzahl;
	}

	public Integer getAnzahlDerRechnungenVomKundenVorjahr(Integer kundeIId, boolean bStatistikadresse,
			boolean geschaeftsjahr) throws ExceptionLP {
		Integer iAnzahl = null;
		try {
			iAnzahl = rechnungFac.getAnzahlDerRechnungenVomKundenVorjahr(LPMain.getTheClient(), kundeIId,
					bStatistikadresse, geschaeftsjahr);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iAnzahl;
	}

	public void removeRechnungkontierung(RechnungkontierungDto rechnungkontierungDto) throws ExceptionLP {
		try {
			rechnungFac.removeRechnungkontierung(rechnungkontierungDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void prozentsatzZuOffeneAnzahlungspositionenAbrechnen(Integer rechnungIId, BigDecimal bdProzentsatz)
			throws ExceptionLP {
		try {
			rechnungFac.prozentsatzZuOffeneAnzahlungspositionenAbrechnen(rechnungIId, bdProzentsatz,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public RechnungkontierungDto updateRechnungkontierung(RechnungkontierungDto rechnungkontierungDto)
			throws ExceptionLP {
		try {
			if (rechnungkontierungDto.getIId() == null) {
				return rechnungFac.createRechnungkontierung(rechnungkontierungDto, LPMain.getTheClient());
			} else {
				return rechnungFac.updateRechnungkontierung(rechnungkontierungDto, LPMain.getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechnungkontierungDto rechnungkontierungFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return rechnungFac.rechnungkontierungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Berechnen, wieviele Prozent einer AR bereits kontiert sind
	 * 
	 * @param rechnungIId Integer
	 * @return BigDecimal
	 * @throws ExceptionLP
	 */
	public BigDecimal getProzentsatzKontiert(Integer rechnungIId) throws ExceptionLP {
		try {
			return rechnungFac.getProzentsatzKontiert(rechnungIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createRechnungenAusWiederholungsauftrag(Integer auftragIId, java.sql.Date dNeuDatum)
			throws ExceptionLP {
		try {
			return rechnungFac.createRechnungenAusWiederholungsauftrag(auftragIId, dNeuDatum, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<Integer> getAngelegteRechnungen() throws ExceptionLP {
		try {
			return rechnungFac.getAngelegteRechnungen(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public VerrechnungsmodellDto holeVerrechnungsmodellFuerERUeberleitungAnhandSelektierterZeilen(
			ArrayList<Integer> selektierteIIds) throws ExceptionLP {
		try {
			return abrechnungsvorschlagFac.holeVerrechnungsmodellFuerERUeberleitungAnhandSelektierterZeilen(
					selektierteIIds, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public VerrechnungsmodellDto holeVerrechnungsmodellFuerReiseUeberleitungAnhandSelektierterZeilen(
			ArrayList<Integer> selektierteIIds) throws ExceptionLP {
		try {
			return abrechnungsvorschlagFac.holeVerrechnungsmodellFuerReiseUeberleitungAnhandSelektierterZeilen(
					selektierteIIds, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Bei einer auftragbezogenen Rechnung ist es moeglich, all jene offenen oder
	 * teilerledigten Auftragpositionen innerhalb einer Transaktion zu uebernehmen,
	 * die keine Benutzerinteraktion benoetigen. <br>
	 * Es gilt:
	 * <ul>
	 * <li>Handeingabepositionen werden uebernommen.
	 * <li>Nicht Serien- oder Chargennummertragende Artikelpositionen werden mit
	 * jener Menge uebernommen, die auf Lager liegt.
	 * <li>Artikelpositionen mit Seriennummer werden nicht uebernommen.
	 * <li>Artikelpositionen mit Chargennummer werden mit jener Menge uebernommen,
	 * die auf Lager liegt, wenn es genau eine Charge gibt.
	 * </ul>
	 * Die restlichen Positionen koennen nicht automatisch uebernommen werden.
	 * 
	 * @param iIdRechnungI PK der Rechnung
	 * @param iIdAuftragI  Integer
	 * @param artikelsets
	 * @throws ExceptionLP Ausnahme
	 */
	public ArrayList<RechnungPositionDto> uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktionNew(
			Integer iIdRechnungI, Integer iIdAuftrag, List<Artikelset> artikelsets, List<Integer> auftragspositionIIds)
			throws ExceptionLP {
		try {
			return rechnungFac.uebernimmAlleOffenenAuftragpositionenOhneBenutzerinteraktionNew(iIdRechnungI, iIdAuftrag,
					artikelsets, auftragspositionIIds, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	/**
	 * Aus einer Rechnung jene Position heraussuchen, die zu einer bestimmten
	 * Auftragposition gehoert.
	 * 
	 * @param iIdRechnungI        Integer
	 * @param iIdAuftragpositionI Integer
	 * @throws ExceptionLP
	 * @return LieferscheinpositionDto
	 */
	public RechnungPositionDto getRechnungPositionByRechnungAuftragposition(Integer iIdRechnungI,
			Integer iIdAuftragpositionI) throws ExceptionLP {
		RechnungPositionDto oRechnungpositionDto = null;
		try {
			oRechnungpositionDto = rechnungFac.getRechnungPositionByRechnungAuftragposition(iIdRechnungI,
					iIdAuftragpositionI);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oRechnungpositionDto;
	}

	public boolean pruefePositionenAufSortierungNachAuftrag(Integer rechnungIId) throws ExceptionLP {
		boolean bOk = false;
		try {
			bOk = rechnungFac.pruefePositionenAufSortierungNachAuftrag(rechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return bOk;
	}

	public String pruefeRechnungswert() throws ExceptionLP {
		try {
			return rechnungFac.pruefeRechnungswert(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void sortierePositionenNachAuftrag(Integer rechnungIId) throws ExceptionLP {
		try {
			rechnungFac.sortierePositionenNachAuftrag(rechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void sortiereNachAuftragsnummer(Integer rechnungIId) throws ExceptionLP {
		try {
			rechnungFac.sortiereNachAuftragsnummer(rechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void sortiereNachArtikelnummer(Integer rechnungIId) throws ExceptionLP {
		try {
			rechnungFac.sortiereNachArtikelnummer(rechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public String getGutschriftenEinerRechnung(Integer rechnungIId) throws ExceptionLP {
		String text = null;
		try {
			text = rechnungFac.getGutschriftenEinerRechnung(rechnungIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return text;
	}

	public void berechnePauschalposition(BigDecimal wert, Integer positionIId, Integer belegIId) throws ExceptionLP {
		try {
			rechnungFac.berechnePauschalposition(wert, positionIId, belegIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void enterledigen(HashMap<String, ArrayList<Integer>> hmEnterledigen) throws ExceptionLP {
		try {
			abrechnungsvorschlagFac.enterledigen(hmEnterledigen);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public BigDecimal berechneSummeOffenNetto(Integer kundeIId, boolean bStatistikadresse) throws ExceptionLP {
		GregorianCalendar gcVon = new GregorianCalendar(1900, 01, 01);

		GregorianCalendar gcBis = new GregorianCalendar(2099, 01, 01);
		try {
			return rechnungFac.berechneSummeOffenNetto(LPMain.getTheClient().getMandant(),
					RechnungFac.KRIT_MIT_GUTSCHRIFTEN, gcVon, gcBis, kundeIId, bStatistikadresse,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public ArrayList<RechnungDto> sindAnzahlungenVorhanden(Integer auftragIId) throws ExceptionLP {
		
		try {
			return rechnungFac.sindAnzahlungenVorhanden(auftragIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	
	public JasperPrintLP[] printRechnungZahlschein(Integer iRechnungIId, String sReportname, Integer iKopien)
			throws ExceptionLP {
		try {
			return rechnungReportFac.printRechnungZahlschein(iRechnungIId, sReportname, iKopien, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public void setzeVersandzeitpunktAufJetzt(Integer iRechnungIId, String sDruckart) throws ExceptionLP {
		try {
			rechnungFac.setzeVersandzeitpunktAufJetzt(iRechnungIId, sDruckart);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void verbucheRechnungNeu(Integer iRechnungIId) throws ExceptionLP {
		try {
			rechnungFac.verbucheRechnungNeu(iRechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void verbucheGutschriftNeu(Integer iRechnungIId) throws ExceptionLP {
		try {
			rechnungFac.verbucheGutschriftNeu(iRechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Liefert die IId der Position fuer die angegebene Positionsnummer zurueck
	 * 
	 * @param rechnungIId
	 * @param position    die Positionsnummer f&uuml;r die die IId ermittelt werden
	 *                    soll
	 * @return null wenn es position nicht gibt, ansonsten die IId
	 */
	public Integer getPositionIIdFromPositionNummer(Integer rechnungIId, Integer position) throws ExceptionLP {
		try {
			return rechnungFac.getPositionIIdFromPositionNummer(rechnungIId, position);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	public Integer getPositionNummer(Integer reposIId) throws ExceptionLP {
		try {
			return rechnungFac.getPositionNummer(reposIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	/**
	 * Liefert die Positionsnummer der angegebenen Position-IId. Sollte die Position
	 * selbst keine Nummer haben, wird die unmittelbar vor dieser Position liegende
	 * letztg&uuml;ltige Nummer geliefert.
	 * 
	 * @param reposIId
	 * @return Die Positionsnummer (1 - n), oder null wenn die Position nicht
	 *         vorkommt.
	 */
	public Integer getLastPositionNummer(Integer reposIId) throws ExceptionLP {
		try {
			return rechnungFac.getLastPositionNummer(reposIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	/**
	 * Die hoechste/letzte in einer Rechnung bestehende Positionsnummer ermitteln
	 * 
	 * @param rechnungIId die RechnungsIId fuer die die hoechste Pos.Nummer
	 *                    ermittelt werden soll.
	 * 
	 * @return 0 ... n
	 */
	public Integer getHighestPositionNumber(Integer rechnungIId) throws ExceptionLP {
		try {
			return rechnungFac.getHighestPositionNumber(rechnungIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return 0;
	}

	/**
	 * Prueft, ob fuer alle Rechnungspositionen zwischen den beiden angegebenen
	 * Positionsnummern der gleiche Mehrwertsteuersatz definiert ist.
	 * 
	 * @param rechnungIId
	 * @param vonPositionNumber
	 * @param bisPositionNumber
	 * @return true wenn alle Positionen den gleichen Mehrwertsteuersatz haben.
	 * @throws EJBExceptionLP
	 */
	public boolean pruefeAufGleichenMwstSatz(Integer rechnungIId, Integer vonPositionNumber, Integer bisPositionNumber)
			throws ExceptionLP {
		try {
			return rechnungFac.pruefeAufGleichenMwstSatz(rechnungIId, vonPositionNumber, bisPositionNumber);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return false;
	}

	public RechnungDto[] pruefeObChronologieDesBlegdatumsDerRechnungStimmt(Integer rechnungIId) throws ExceptionLP {
		try {
			return rechnungFac.pruefeObChronologieDesBelegdatumsDerRechnungStimmt(rechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	public void toggleZollpapiereErhalten(Integer eingangsrechnungIId, String cZollpapier) throws ExceptionLP {
		try {
			rechnungFac.toggleZollpapiereErhalten(eingangsrechnungIId, cZollpapier, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public RechnungPositionDto[] getArtikelsetForIId(Integer kopfartikelIId) throws ExceptionLP {
		try {
			return rechnungFac.getArtikelsetForIId(kopfartikelIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	public List<SeriennrChargennrMitMengeDto> getSeriennrchargennrForArtikelsetPosition(Integer rechnungposIId)
			throws ExceptionLP {
		try {
			return rechnungFac.getSeriennrchargennrForArtikelsetPosition(rechnungposIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return new ArrayList<SeriennrChargennrMitMengeDto>();
	}

	public void repairRechnungZws2276(Integer rechnungId) throws ExceptionLP {
		try {
			rechnungFac.repairRechnungZws2276(rechnungId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public List<Integer> repairRechnungZws2276GetList() throws ExceptionLP {
		try {
			return rechnungFac.repairRechnungZws2276GetList(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return new ArrayList<Integer>();
	}

	public RechnungDto[] findByAuftragIId(int auftragIId) throws ExceptionLP {
		try {
			return rechnungFac.rechnungFindByAuftragIId(auftragIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	public RechnungzahlungDto createUpdateZahlung(RechnungzahlungDto zahlungDto, boolean rechnungErledigt,
			RechnungzahlungDto gutschriftZahlungDto, boolean gutschriftErledigt) throws ExceptionLP {
		try {
			return rechnungFac.createUpdateZahlung(zahlungDto, rechnungErledigt, gutschriftZahlungDto,
					gutschriftErledigt, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	public void removeLockDesAbrechungsvorschlagesWennIchIhnSperre() throws ExceptionLP {
		try {
			abrechnungsvorschlagFac.removeLockDesAbrechungsvorschlagesWennIchIhnSperre(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void pruefeBearbeitenDesAbrechungsvorschlagesErlaubt() throws ExceptionLP {
		try {
			abrechnungsvorschlagFac.pruefeBearbeitenDesAbrechungsvorschlagesErlaubt(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public Boolean hatRechnungPositionenMitAuftragsbezug(Integer rechnungId) throws ExceptionLP {
		try {
			return rechnungFac.hatRechnungPositionenMitAuftragsbezug(rechnungId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return Boolean.FALSE;
	}

	public boolean hatRechnungVersandweg(RechnungDto rechnungDto) throws ExceptionLP {
		try {
			return rechnungFac.hatRechnungVersandweg(rechnungDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return false;
	}

	public String createRechnungElektronischPost(Integer rechnungIId) throws ExceptionLP {
		try {
			return rechnungFac.createRechnungElektronischPost(rechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return "";
	}

	public void resetRechnungElektronisch(Integer rechnungIId) throws ExceptionLP {
		try {
			rechnungFac.resetRechnungElektronisch(rechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public boolean pruefeKonditionenLieferscheinzuRechnung(Integer rechnungIId, Integer lieferscheinIId)
			throws ExceptionLP {
		try {
			return rechnungFac.pruefeKonditionenLieferscheinzuRechnung(rechnungIId, lieferscheinIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return false;
	}

	public void uebernehmeKonditionen(Integer rechnungIId, Integer lieferscheinIId) throws ExceptionLP {
		try {
			rechnungFac.uebernehmeKonditionenAusLieferschein(rechnungIId, lieferscheinIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public KundeDto getKundeFuerRechnungAusLieferschein(Integer lieferscheinIId, String rechnungstypCNr)
			throws ExceptionLP {
		try {
			return rechnungFac.getKundeFuerRechnungAusLieferschein(lieferscheinIId, rechnungstypCNr,
					LPMain.getTheClient());
		} catch (Throwable e) {
			handleThrowable(e);
		}
		return null;
	}

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer iIdRechnungI,
			int iSortierungNeuePositionI) throws ExceptionLP {
		try {
			rechnungFac.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(iIdRechnungI, iSortierungNeuePositionI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public List<Integer> repairRechnungSP6402GetList() throws ExceptionLP {
		try {
			return rechnungFac.repairRechnungSP6402GetList(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return new ArrayList<Integer>();
	}

	public void repairRechnungSP6402(Integer rechnungId) throws ExceptionLP {
		try {
			rechnungFac.repairRechnungSP6402(rechnungId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public ZugferdResult createZugferdRechnung(Integer rechnungId, Locale druckLocale, Boolean bMitLogo,
			String drucktype) throws ExceptionLP {
		try {
			return rechnungReportFac.createZugferdRechnung(rechnungId, druckLocale, bMitLogo, drucktype,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return new ZugferdResult();
	}

	public void archiviereZugferdResult(ZugferdResult zugferdResult) throws ExceptionLP {
		try {
			rechnungFac.archiviereZugferdResult(zugferdResult, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public ZugferdResult createZugferdGutschrift(Integer rechnungId, Locale druckLocale, Boolean bMitLogo,
			String drucktype) throws ExceptionLP {
		try {
			return rechnungReportFac.createZugferdGutschrift(rechnungId, druckLocale, bMitLogo, drucktype,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return new ZugferdResult();
	}

	public boolean isZugferdPartner(Integer rechnungId) throws ExceptionLP {
		try {
			return rechnungReportFac.isZugferdPartner(rechnungId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return false;
		}
	}

	public List<Integer> repairLieferscheinSP6999GetList() throws ExceptionLP {
		try {
			return rechnungFac.repairLieferscheinSP6999GetList(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return new ArrayList<Integer>();
	}

	public VorkassepositionDto vorkassepositionFindByRechnungIIdAuftragspositionIId(Integer rechnungIId,
			Integer auftragpositonIId) throws ExceptionLP {
		try {
			return rechnungFac.vorkassepositionFindByRechnungIIdAuftragspositionIId(rechnungIId, auftragpositonIId,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public void removeVorkassepositionDto(VorkassepositionDto vorkassepositionDto) throws ExceptionLP {
		try {
			rechnungFac.removeVorkassepositionDto(vorkassepositionDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public Integer updateVorkasseposition(VorkassepositionDto vorkassepositionDto) throws ExceptionLP {
		try {
			return rechnungFac.updateVorkasseposition(vorkassepositionDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;

	}

	public void uebersteuereIntelligenteZwischensumme(Integer rechnungpositionIId,
			BigDecimal bdBetragInBelegwaehrungUebersteuert) throws ExceptionLP {
		try {
			rechnungFac.uebersteuereIntelligenteZwischensumme(rechnungpositionIId, bdBetragInBelegwaehrungUebersteuert,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);

		}
	}

	public ArrayList<Integer> getKundeIIdsRechnungsadresseOffenerLierferscheine(java.sql.Date dStichtag)
			throws ExceptionLP {
		try {
			return rechnungFac.getKundeIIdsRechnungsadresseOffenerLierferscheine(dStichtag, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;

	}

	public void offeneLieferscheineMitABStaffelpreisenVerrechnen(Integer kundeIId_rechnungsadresse,
			java.sql.Date dStichtag, java.sql.Date neuDatum) throws ExceptionLP {
		try {
			rechnungFac.offeneLieferscheineMitABStaffelpreisenVerrechnen(kundeIId_rechnungsadresse, dStichtag, neuDatum,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

}
