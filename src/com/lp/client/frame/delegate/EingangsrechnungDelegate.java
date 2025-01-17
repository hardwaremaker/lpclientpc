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

/**
 * <p><I>Diese Klasse kuemmert sich um die Eingangsrechnungen</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>11.02.05</I></p>
 *
 * <p> </p>
 *
 * @author  Martin Bluehweis
 * @version $Revision: 1.25 $
 */
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungOffeneKriterienDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungServiceFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungartDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungartsprDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungstatusDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungtextDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.eingangsrechnung.service.ErZahlungsempfaenger;
import com.lp.server.eingangsrechnung.service.VendidataImporterResult;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagDto;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagExportResult;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagFac;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagkriterienDto;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlaglaufDto;
import com.lp.server.eingangsrechnung.service.ZusatzkostenAnlegenResult;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.rechnung.service.CoinRoundingResult;
import com.lp.server.system.service.JxlImportErgebnis;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.EingangsrechnungId;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class EingangsrechnungDelegate extends Delegate {
	private Context context;
	private EingangsrechnungFac eingangsrechnungFac;
	private EingangsrechnungReportFac eingangsrechnungReportFac;
	private EingangsrechnungServiceFac eingangsrechnungServiceFac;
	private ZahlungsvorschlagFac zahlungsvorschlagFac;

	public EingangsrechnungDelegate() throws ExceptionLP {
		// delegateexc: 2 auch der konstruktor
		try {
			context = new InitialContext();

			eingangsrechnungFac = lookupFac(context, EingangsrechnungFac.class);

			eingangsrechnungReportFac = lookupFac(context, EingangsrechnungReportFac.class);

			eingangsrechnungServiceFac = lookupFac(context, EingangsrechnungServiceFac.class);

			zahlungsvorschlagFac = lookupFac(context, ZahlungsvorschlagFac.class);

		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void storniereEingangsrechnung(Integer eingangsrechnungIId) throws ExceptionLP {
		try {
			eingangsrechnungFac.storniereEingangsrechnung(eingangsrechnungIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void storniereEingangsrechnungRueckgaengig(Integer eingangsrechnungIId) throws ExceptionLP {
		try {
			eingangsrechnungFac.storniereEingangsrechnungRueckgaengig(eingangsrechnungIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public java.sql.Date getDefaultFreigabeDatum() throws ExceptionLP {
		try {
			return eingangsrechnungFac.getDefaultFreigabeDatum();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public EingangsrechnungDto updateEingangsrechnung(EingangsrechnungDto eingangsrechnungDto) throws ExceptionLP {
		try {
			if (eingangsrechnungDto.getIId() == null) {
				return eingangsrechnungFac.createEingangsrechnung(eingangsrechnungDto,
						LPMain.getInstance().getTheClient());
			} else {
				return eingangsrechnungFac.updateEingangsrechnung(eingangsrechnungDto,
						LPMain.getInstance().getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateEingangsrechnungFreigabedatum(Integer eingangsrechnungIId, java.sql.Date dFreigabedatum)
			throws ExceptionLP {
		try {
			eingangsrechnungFac.updateEingangsrechnungFreigabedatum(eingangsrechnungIId, dFreigabedatum,
					LPMain.getInstance().getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public void updateEingangsrechnungMahndaten(Integer eingangsrechnungrechnungIId, Integer mahnstufeIId,
			Timestamp tMahndatum) throws ExceptionLP {
		try {
			eingangsrechnungFac.updateEingangsrechnungMahndaten(eingangsrechnungrechnungIId, mahnstufeIId, tMahndatum);

		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public void updateKopfFusstextUebersteuert(Integer eingangsrechnungIId, String cKopftext, String cFusstext)
			throws ExceptionLP {
		try {
			eingangsrechnungFac.updateKopfFusstextUebersteuert(eingangsrechnungIId, cKopftext, cFusstext,
					LPMain.getInstance().getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public void updateEingangsrechnungGedruckt(Integer eingangsrechnungIId) throws ExceptionLP {
		try {

			eingangsrechnungFac.updateEingangsrechnungGedruckt(LPMain.getInstance().getTheClient(), eingangsrechnungIId,
					new java.sql.Timestamp(System.currentTimeMillis()));

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public EingangsrechnungDto eingangsrechnungFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return eingangsrechnungFac.eingangsrechnungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public EingangsrechnungDto eingangsrechnungFindByCNrMandantCNr(String cNr, boolean bZusatzkosten)
			throws ExceptionLP {
		try {
			return eingangsrechnungFac.eingangsrechnungFindByCNrMandantCNr(cNr,
					LPMain.getInstance().getTheClient().getMandant(), bZusatzkosten);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public EingangsrechnungDto[] eingangsrechnungFindByBestellungIId(Integer bestellungIId) throws ExceptionLP {
		try {
			return eingangsrechnungFac.eingangsrechnungFindByBestellungIId(bestellungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public EingangsrechnungartDto eingangsrechnungartFindByPrimaryKey(String cNr) throws ExceptionLP {
		try {
			return eingangsrechnungServiceFac.eingangsrechnungartFindByPrimaryKey(cNr,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateEingangsrechnungart(EingangsrechnungartDto eingangsrechnungartDto) throws ExceptionLP {
		try {
			eingangsrechnungServiceFac.updateEingangsrechnungart(eingangsrechnungartDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public EingangsrechnungartDto[] eingangsrechnungartFindAll() throws ExceptionLP {
		try {
			return eingangsrechnungServiceFac.eingangsrechnungartFindAll();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public EingangsrechnungartsprDto eingangsrechnungartsprFindByPrimaryKey(String eingangsrechnungartCNr,
			String localeCNr) throws ExceptionLP {
		try {
			return eingangsrechnungServiceFac.eingangsrechnungartsprFindByPrimaryKey(eingangsrechnungartCNr, localeCNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllSprEingangsrechnungarten() throws ExceptionLP {
		try {
			return eingangsrechnungServiceFac
					.getAllSprEingangsrechnungarten(Helper.locale2String(LPMain.getInstance().getUISprLocale()));
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getSprEingangsrechnungartNurZusatzkosten() throws ExceptionLP {
		try {
			return eingangsrechnungServiceFac.getSprEingangsrechnungartNurZusatzkosten(
					Helper.locale2String(LPMain.getInstance().getUISprLocale()));
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeEingangsrechnungAuftragszuordnung(
			EingangsrechnungAuftragszuordnungDto eingangsrechnungAuftragszuordnungDto) throws ExceptionLP {
		try {
			eingangsrechnungFac.removeEingangsrechnungAuftragszuordnung(eingangsrechnungAuftragszuordnungDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public EingangsrechnungAuftragszuordnungDto updateEingangsrechnungAuftragszuordnung(
			EingangsrechnungAuftragszuordnungDto eingangsrechnungAuftragszuordnungDto) throws ExceptionLP {
		try {
			if (eingangsrechnungAuftragszuordnungDto.getIId() != null) {
				return eingangsrechnungFac.updateEingangsrechnungAuftragszuordnung(eingangsrechnungAuftragszuordnungDto,
						LPMain.getInstance().getTheClient());
			} else {
				return eingangsrechnungFac.createEingangsrechnungAuftragszuordnung(eingangsrechnungAuftragszuordnungDto,
						LPMain.getInstance().getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public EingangsrechnungAuftragszuordnungDto eingangsrechnungAuftragszuordnungFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return eingangsrechnungFac.eingangsrechnungAuftragszuordnungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public EingangsrechnungAuftragszuordnungDto[] eingangsrechnungAuftragszuordnungFindByEingangsrechnungIId(
			Integer eingangsrechnungIId) throws ExceptionLP {
		try {
			return eingangsrechnungFac.eingangsrechnungAuftragszuordnungFindByEingangsrechnungIId(eingangsrechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getWertNochNichtZuAuftraegenZugeordnet(Integer eingangsrechnungIId) throws ExceptionLP {
		try {
			return eingangsrechnungFac.getWertNochNichtZuAuftraegenZugeordnet(eingangsrechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getWertNochNichtKontiert(Integer eingangsrechnungIId) throws ExceptionLP {
		try {
			return eingangsrechnungFac.getWertNochNichtKontiert(eingangsrechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void toggleWiederholdendErledigt(Integer eingangsrechnungIId) throws ExceptionLP {
		try {
			eingangsrechnungFac.toggleWiederholendErledigt(eingangsrechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	public void toggleEingangsrechnungGeprueft(Integer eingangsrechnungIId) throws ExceptionLP {
		try {
			eingangsrechnungFac.toggleEingangsrechnungGeprueft(eingangsrechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void toggleZollimportpapiereErhalten(Integer eingangsrechnungIId, String cZollimportpapier,
			Integer eingangsrechnungIId_Zollimport) throws ExceptionLP {
		try {
			eingangsrechnungFac.toggleZollimportpapiereErhalten(eingangsrechnungIId, cZollimportpapier,
					eingangsrechnungIId_Zollimport, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeEingangsrechnungKontierung(EingangsrechnungKontierungDto eingangsrechnungKontierungDto)
			throws ExceptionLP {
		try {
			eingangsrechnungFac.removeEingangsrechnungKontierung(eingangsrechnungKontierungDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public EingangsrechnungKontierungDto updateEingangsrechnungKontierung(
			EingangsrechnungKontierungDto eingangsrechnungKontierungDto) throws ExceptionLP {
		EingangsrechnungKontierungDto dto = null;
		try {
			if (eingangsrechnungKontierungDto.getIId() == null) {
				dto = eingangsrechnungFac.createEingangsrechnungKontierung(eingangsrechnungKontierungDto,
						LPMain.getInstance().getTheClient());
			} else {
				dto = eingangsrechnungFac.updateEingangsrechnungKontierung(eingangsrechnungKontierungDto,
						LPMain.getInstance().getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return dto;
	}

	public EingangsrechnungKontierungDto eingangsrechnungKontierungFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return eingangsrechnungFac.eingangsrechnungKontierungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZusatzkostenAnlegenResult wiederholendeZusatzkostenAnlegen() throws ExceptionLP {
		ZusatzkostenAnlegenResult result = new ZusatzkostenAnlegenResult();

		try {
			result = eingangsrechnungFac.wiederholendeZusatzkostenAnlegen(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return result;
	}

	public EingangsrechnungKontierungDto[] eingangsrechnungKontierungFindByEingangsrechnungIId(
			Integer eingangsrechnungIId) throws ExceptionLP {
		EingangsrechnungKontierungDto[] dtos = null;
		try {
			dtos = eingangsrechnungFac.eingangsrechnungKontierungFindByEingangsrechnungIId(eingangsrechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return dtos;
	}

	public EingangsrechnungzahlungDto updateEingangsrechnungzahlung(
			EingangsrechnungzahlungDto eingangsrechnungzahlungDto, boolean bErledigt) throws ExceptionLP {
		EingangsrechnungzahlungDto erzDto = null;
		try {
			if (eingangsrechnungzahlungDto.getIId() == null) {
				erzDto = eingangsrechnungFac.createEingangsrechnungzahlung(eingangsrechnungzahlungDto, bErledigt,
						LPMain.getInstance().getTheClient());
			} else {
				erzDto = eingangsrechnungFac.updateEingangsrechnungzahlung(eingangsrechnungzahlungDto, bErledigt,
						LPMain.getInstance().getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return erzDto;
	}

	public void removeEingangsrechnungzahlung(EingangsrechnungzahlungDto eingangsrechnungzahlungDto)
			throws ExceptionLP {
		try {
			eingangsrechnungFac.removeEingangsrechnungzahlung(eingangsrechnungzahlungDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public EingangsrechnungzahlungDto eingangsrechnungzahlungFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return eingangsrechnungFac.eingangsrechnungzahlungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public EingangsrechnungzahlungDto eingangsrechnungzahlungFindByRechnungzahlungIId(Integer rechnungzahlungIId)
			throws ExceptionLP {
		try {
			return eingangsrechnungFac.eingangsrechnungzahlungFindByRechnungzahlungIId(rechnungzahlungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public EingangsrechnungzahlungDto[] eingangsrechnungzahlungFindByEingangsrechnungzahlungIId(
			Integer rechnungzahlungIId) throws ExceptionLP {
		try {
			return eingangsrechnungFac.eingangsrechnungzahlungFindByEingangsrechnungIId(rechnungzahlungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Die summe aller Zahlungen auf eine Eingangsrechnung holen.
	 * 
	 * @param erIId                 Integer
	 * @param zahlungIIdAusgenommen Integer
	 * @throws ExceptionLP
	 * @return BigDecimal
	 */
	public BigDecimal getBezahltBetragFw(Integer erIId, Integer zahlungIIdAusgenommen) throws ExceptionLP {
		try {
			return eingangsrechnungFac.getBezahltBetragFw(erIId, zahlungIIdAusgenommen);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getBezahltBetrag(Integer erIId, Integer zahlungIIdAusgenommen) throws ExceptionLP {
		try {
			return eingangsrechnungFac.getBezahltBetrag(erIId, zahlungIIdAusgenommen);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Die summe aller Zahlungen auf eine Eingangsrechnung holen.
	 * 
	 * @param erIId                 Integer
	 * @param zahlungIIdAusgenommen Integer
	 * @throws ExceptionLP
	 * @return BigDecimal
	 */
	public BigDecimal getBezahltBetragUstFw(Integer erIId, Integer zahlungIIdAusgenommen) throws ExceptionLP {
		try {
			return eingangsrechnungFac.getBezahltBetragUstFw(erIId, zahlungIIdAusgenommen);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printZahlungsjournal(int iSortierung, Date dVon, Date dBis, boolean bZusatzkosten)
			throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = eingangsrechnungReportFac.printZahlungsjournal(LPMain.getInstance().getTheClient(), iSortierung,
					dVon, dBis, bZusatzkosten);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printOffene(EingangsrechnungOffeneKriterienDto krit) throws ExceptionLP {
		try {
			return eingangsrechnungReportFac.printOffene(krit, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		
		return null;
	}
	
//	public JasperPrintLP printOffene(int iSort, Integer lieferantIId, Date dStichtag, boolean bStichtagIstFreigabeDatum,
//			boolean bZusatzkosten, boolean mitNichtZugeordnetenBelegen) throws ExceptionLP {
//		JasperPrintLP print = null;
//		try {
//			print = eingangsrechnungReportFac.printOffene(LPMain.getInstance().getTheClient(), iSort, lieferantIId,
//					dStichtag, bStichtagIstFreigabeDatum, bZusatzkosten, mitNichtZugeordnetenBelegen);
//		} catch (Throwable ex) {
//			handleThrowable(ex);
//		}
//		return print;
//	}

	public JasperPrintLP printFehlendeZollpapiere() throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = eingangsrechnungReportFac.printFehlendeZollpapiere(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printNichtabgerechneteAnzahlungen(java.sql.Date dStichtag) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = eingangsrechnungReportFac.printNichtabgerechneteAnzahlungen(dStichtag,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printErfassteZollpapiere(Date dVon, Date dBis) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = eingangsrechnungReportFac.printErfassteZollpapiere(dVon, dBis, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printKontierungsjournal(int iFilterER, Integer kostenstelleIId, int iKritDatum, Date dVon,
			Date dBis, boolean bZusatzkosten) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = eingangsrechnungReportFac.printKontierungsjournal(LPMain.getInstance().getTheClient(), iFilterER,
					kostenstelleIId, iKritDatum, dVon, dBis, bZusatzkosten);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public BankverbindungDto getZuletztVerwendeteBankverbindung(Integer eingangsrechnungIId) throws ExceptionLP {
		BankverbindungDto print = null;
		try {
			print = eingangsrechnungFac.getZuletztVerwendeteBankverbindung(eingangsrechnungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	// *** Eingangsrechnungsstatus
	// **************************************************
	public EingangsrechnungstatusDto eingangsrechnungsstatusFindByPrimaryKey(String cNrI) throws ExceptionLP {
		try {
			return eingangsrechnungServiceFac.eingangsrechnungstatusFindByPrimaryKey(cNrI);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Eine Rechnung manuell auf 'Erledigt' setzen.
	 * 
	 * @param iIdEingangsrechnungI PK der Rechnung
	 * @throws ExceptionLP Ausnahme
	 */
	public void manuellErledigen(Integer iIdEingangsrechnungI) throws ExceptionLP {
		try {
			eingangsrechnungFac.manuellErledigen(iIdEingangsrechnungI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public JasperPrintLP printAlle(ReportJournalKriterienDto krit, boolean bZusatzkosten,
			boolean bDatumIstFreigabedatum) throws ExceptionLP {
		JasperPrintLP print = null;
		try {
			print = eingangsrechnungReportFac.printAlle(krit, LPMain.getInstance().getTheClient(), bZusatzkosten,
					bDatumIstFreigabedatum);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	/**
	 * Den Status einer ER von 'Erledigt' auf 'Erfasst' setzen. <br>
	 * Diese Aktion ist nur moeglich, wenn der 'Erledigt' Status manuell gesetzt
	 * wurde.
	 * 
	 * @param eingangsrechnungIId PK der ER
	 * @throws ExceptionLP
	 */
	public void erledigungAufheben(Integer eingangsrechnungIId) throws ExceptionLP {
		try {
			eingangsrechnungFac.erledigungAufheben(eingangsrechnungIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public EingangsrechnungDto[] eingangsrechnungFindByLieferantIIdLieferantenrechnungsnummerOhneExc(
			Integer lieferantIId, String sLieferantenrechnungsnummer) throws ExceptionLP {
		try {
			return eingangsrechnungFac.eingangsrechnungFindByLieferantIIdLieferantenrechnungsnummerOhneExc(lieferantIId,
					sLieferantenrechnungsnummer);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<EingangsrechnungDto> eingangsrechnungFindOffeneByLieferantIId(Integer lieferantIId)
			throws ExceptionLP {
		try {
			return eingangsrechnungFac.eingangsrechnungFindOffeneByLieferantIId(lieferantIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createZahlungsvorschlag(ZahlungsvorschlagkriterienDto krit) throws ExceptionLP {
		try {
			return zahlungsvorschlagFac.createZahlungsvorschlag(krit, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeZahlungsvorschlaglauf(Integer zahlungsvorschlaglaufIId) throws ExceptionLP {
		try {
			zahlungsvorschlagFac.removeZahlungsvorschlaglauf(zahlungsvorschlaglaufIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void eingangsrechnungAufAngelegtZuruecksetzen(Integer eingangangsrechnungIId) throws ExceptionLP {
		try {
			eingangsrechnungFac.eingangsrechnungAufAngelegtZuruecksetzen(eingangangsrechnungIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void aktiviereBeleg(Integer eingangangsrechnungIId) throws ExceptionLP {
		try {
			eingangsrechnungFac.aktiviereBeleg(eingangangsrechnungIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public ZahlungsvorschlaglaufDto zahlungsvorschlaglaufFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return zahlungsvorschlagFac.zahlungsvorschlaglaufFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZahlungsvorschlagDto zahlungsvorschlagFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return zahlungsvorschlagFac.zahlungsvorschlagFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZahlungsvorschlagDto[] zahlungsvorschlagFindByZahlungsvorschlaglaufIId(Integer zahlungsvorschlaglaufIId)
			throws ExceptionLP {
		try {
			return zahlungsvorschlagFac.zahlungsvorschlagFindByZahlungsvorschlaglaufIId(zahlungsvorschlaglaufIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public boolean sindNegativeZuExportierendeZahlungenVorhanden(Integer zahlungsvorschlagleufIId) throws ExceptionLP {
		try {
			return zahlungsvorschlagFac.sindNegativeZuExportierendeZahlungenVorhanden(zahlungsvorschlagleufIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return false;
		}
	}

	public void updateZahlungsvorschlagBBezahlenMultiSelect(List<Integer> zahlungsvorschlagIId) throws ExceptionLP {
		try {
			zahlungsvorschlagFac.updateZahlungsvorschlagBBezahlenMultiSelect(zahlungsvorschlagIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public BigDecimal getGesamtwertEinesZahlungsvorschlaglaufsInMandantenwaehrung(Integer zahlungsvorschlaglaufIId)
			throws ExceptionLP {
		try {
			return zahlungsvorschlagFac.getGesamtwertEinesZahlungsvorschlaglaufsInMandantenwaehrung(
					zahlungsvorschlaglaufIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZahlungsvorschlagExportResult exportiereZahlungsvorschlaglauf(Integer zahlungsvorschlaglaufIId,
			Integer iExportArt) throws ExceptionLP {
		try {
			return zahlungsvorschlagFac.exportiereZahlungsvorschlaglauf(zahlungsvorschlaglaufIId, iExportArt,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZahlungsvorschlagDto updateZahlungsvorschlag(ZahlungsvorschlagDto zahlungsvorschlagDto) throws ExceptionLP {
		try {
			return zahlungsvorschlagFac.updateZahlungsvorschlag(zahlungsvorschlagDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getZahlungsvorschlagSepaExportFilename(Integer bankverbindungIId) throws ExceptionLP {
		try {
			return zahlungsvorschlagFac.getZahlungsvorschlagSepaExportFilename(bankverbindungIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void archiviereSepaZahlungsvorschlag(Integer zahlungsvorschlaglaufIId, String xmlZahlungsvorschlag)
			throws ExceptionLP {
		try {
			zahlungsvorschlagFac.archiviereSepaZahlungsvorschlag(xmlZahlungsvorschlag, zahlungsvorschlaglaufIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void eingangsrechnungAuftragszuordnungAnteilsmaessigKopieren(
			EingangsrechnungAuftragszuordnungDto[] eingangsrechnungAuftragszuordnungDto, Integer eingangangsrechnungIId)
			throws ExceptionLP {
		try {
			eingangsrechnungFac.eingangsrechnungAuftragszuordnungAnteilsmaessigKopieren(
					eingangsrechnungAuftragszuordnungDto, eingangangsrechnungIId, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void eingangsrechnungAuftragszuordnungExaktKopieren(
			EingangsrechnungAuftragszuordnungDto[] eingangsrechnungAuftragszuordnungDto, Integer eingangangsrechnungIId)
			throws ExceptionLP {
		try {
			eingangsrechnungFac.eingangsrechnungAuftragszuordnungExaktKopieren(eingangsrechnungAuftragszuordnungDto,
					eingangangsrechnungIId, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void pruefePreise() throws ExceptionLP {
		try {
			eingangsrechnungFac.pruefePreise(LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public JasperPrintLP[] printEingangsrechnung(Integer iEingangsrechnungIId, String sReportName, Integer iKopien,
			BigDecimal bdBetrag, String sZusatztext, Integer schecknummer) throws ExceptionLP {
		try {
			return eingangsrechnungReportFac.printEingangsrechnung(iEingangsrechnungIId, sReportName, iKopien, bdBetrag,
					sZusatztext, schecknummer, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public JasperPrintLP printDokumente(Integer eingangsrechnungIId) throws ExceptionLP {
		try {
			return eingangsrechnungReportFac.printDokumente(eingangsrechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public JasperPrintLP[] printEingangsrechnungMitPositionen(Integer iEingangsrechnungIId, Locale druckLocale,
			boolean bMitLogo, Integer iAnzahlKopien) throws ExceptionLP {
		try {
			return eingangsrechnungReportFac.printEingangsrechnungMitPositionen(iEingangsrechnungIId, druckLocale,
					bMitLogo, iAnzahlKopien, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public void verbucheEingangsrechnungNeu(Integer iRechnungIId) throws ExceptionLP {
		try {
			eingangsrechnungFac.verbucheEingangsrechnungNeu(iRechnungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public BigDecimal getAnzahlungenZuSchlussrechnungFw(Integer erIId) throws ExceptionLP {
		try {
			return eingangsrechnungFac.getAnzahlungenGestelltZuSchlussrechnungFw(erIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getAnzahlungenBezahltZuSchlussrechnungFw(Integer erIId) throws ExceptionLP {
		try {
			return eingangsrechnungFac.getAnzahlungenBezahltZuSchlussrechnungFw(erIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getAnzahlungenZuSchlussrechnungUstFw(Integer erIId) throws ExceptionLP {
		try {
			return eingangsrechnungFac.getAnzahlungenGestelltZuSchlussrechnungUstFw(erIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public EingangsrechnungDto[] findByBestellungIId(Integer bsIId) throws ExceptionLP {
		try {
			return eingangsrechnungFac.eingangsrechnungFindByBestellungIId(bsIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateEingangsrechnungtext(EingangsrechnungtextDto textDto) throws ExceptionLP {
		try {
			eingangsrechnungServiceFac.updateEingangsrechnungtext(textDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer createEingangsrechnungtext(EingangsrechnungtextDto textDto) throws ExceptionLP {
		try {
			textDto.setMandantCNr(LPMain.getInstance().getTheClient().getMandant());
			textDto.setLocaleCNr(LPMain.getInstance().getTheClient().getLocUiAsString());
			return eingangsrechnungServiceFac.createEingangsrechnungtext(textDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public EingangsrechnungtextDto createDefaultEingangsrechnungtext(String sMediaartI, String sTextinhaltI,
			String localeCNr) throws ExceptionLP {
		EingangsrechnungtextDto textDto = null;

		try {
			textDto = eingangsrechnungServiceFac.createDefaultEingangsRechnungtext(sMediaartI, sTextinhaltI, localeCNr,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return textDto;
	}

	public void removeEingangsrechnungtext(EingangsrechnungtextDto eingangsrechnungtextDto) throws ExceptionLP {
		try {
			eingangsrechnungServiceFac.removeEingangsrechnungtext(eingangsrechnungtextDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public EingangsrechnungtextDto eingangsrechnungtextFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return eingangsrechnungServiceFac.eingangsrechnungtextFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public EingangsrechnungtextDto eingangsrechnungtextFindByMandantLocaleCNr(String pSprache, String pText)
			throws ExceptionLP {
		try {
			return eingangsrechnungServiceFac.eingangsrechnungtextFindByMandantLocaleCNr(
					LPMain.getInstance().getTheClient().getMandant(), pSprache, pText);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public VendidataImporterResult importXml(String xmlContent, boolean checkOnly) throws ExceptionLP {
		try {
			return eingangsrechnungFac.importXML(xmlContent, checkOnly, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JxlImportErgebnis importiereEingangsrechnungXLS(byte[] xlsFile) throws ExceptionLP {
		try {
			return eingangsrechnungFac.importiereEingangsrechnungXLS(xlsFile, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public boolean darfNeuerZahlungsvorschlaglaufErstelltWerden() throws ExceptionLP {
		try {
			return zahlungsvorschlagFac.darfNeuerZahlungsvorschlaglaufErstelltWerden(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return false;
		}
	}

	public CoinRoundingResult calcMwstBetragFromBrutto(EingangsrechnungDto erDto) throws ExceptionLP {
		try {
			return eingangsrechnungFac.calcMwstBetragFromBrutto(erDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public CoinRoundingResult calcMwstBetragFromNetto(EingangsrechnungDto erDto) throws ExceptionLP {
		try {
			return eingangsrechnungFac.calcMwstBetragFromNetto(erDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public List<EingangsrechnungDto> eingangsrechnungFindByBelegdatumVonBis(Date von, Date bis) throws ExceptionLP {
		try {
			return eingangsrechnungFac.eingangsrechnungFindByBelegdatumVonBis(von, bis, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}
	
	public ErZahlungsempfaenger getEingangsrechnungBankverbindung(EingangsrechnungId eingangsrechnungId) throws ExceptionLP {
		try {
			return eingangsrechnungFac.getErZahlungsempfaenger(eingangsrechnungId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}
}
