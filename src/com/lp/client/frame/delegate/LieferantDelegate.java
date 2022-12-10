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
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.IWebpartnerDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.ejb.LflfliefergruppePK;
import com.lp.server.partner.service.LflfliefergruppeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.LieferantReportFac;
import com.lp.server.partner.service.LieferantbeurteilungDto;
import com.lp.server.partner.service.PartnerServicesFac;
import com.lp.server.partner.service.PartnerkommentarDto;
import com.lp.server.partner.service.PartnerkommentarartDto;
import com.lp.server.partner.service.StatistikParamDto;
import com.lp.server.partner.service.VendidataPartnerExportResult;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class LieferantDelegate extends Delegate {
	private Context context;
	private LieferantFac lieferantFac;
	private LieferantReportFac lieferantReportFac;

	public LieferantDelegate() throws Exception {

		context = new InitialContext();

		lieferantFac = lookupFac(context, LieferantFac.class);
		lieferantReportFac = lookupFac(context, LieferantReportFac.class);
	}

	public JasperPrintLP printLieferantenstatistik(StatistikParamDto statistikParamDtoI, boolean bVerdichtetNachArtikel,
			boolean bEingeschraenkt) throws ExceptionLP {

		JasperPrintLP print = null;
		try {
			print = lieferantReportFac.printLieferantenStatistik(statistikParamDtoI, bVerdichtetNachArtikel,
					bEingeschraenkt, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printLieferantenliste(boolean bMitVersteckten, boolean bMitInteressenten,
			boolean bMitAnsprechpartner, boolean bNurFreigegebeneLieferanten, Integer lieferantIIdSelektiert,
			String cPlz, Integer landIId, Integer brancheIId, Integer partnerklasseIId, Integer liefergruppeIId)
			throws ExceptionLP {

		try {
			return lieferantReportFac.printLieferantenliste(LPMain.getTheClient(), bMitVersteckten, bMitInteressenten,
					bMitAnsprechpartner, bNurFreigegebeneLieferanten, lieferantIIdSelektiert, cPlz, landIId, brancheIId,
					partnerklasseIId, liefergruppeIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printLieferantenstammblatt(Integer lieferantIId) throws ExceptionLP {

		try {
			return lieferantReportFac.printLieferantenstammblatt(lieferantIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	
	public boolean pruefeObPreiseBeiWaehrungsaenderungVorhanden(Integer lieferantIId, String waehrungCNr) throws ExceptionLP {

		try {
			return lieferantFac.pruefeObPreiseBeiWaehrungsaenderungVorhanden(lieferantIId, waehrungCNr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return true;
		}
	}

	public LieferantDto[] lieferantfindByKontoIIdKreditorenkonto(Integer kontoIId) throws ExceptionLP {

		LieferantDto[] k = null;
		try {
			k = lieferantFac.lieferantfindByKontoIIdKreditorenkonto(kontoIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public JasperPrintLP printArtikeldesLieferanten(Integer lieferantIId, boolean bSortiertNachBezeichnung,
			boolean bMitVersteckten, boolean bSortiertNachLieferant, boolean bNurLagerbewirtschaftete,
			java.sql.Timestamp tStichtag, boolean bMitStaffelpreisen) throws ExceptionLP {

		JasperPrintLP print = null;
		try {
			print = lieferantReportFac.printArtikeldesLieferanten(lieferantIId, bSortiertNachBezeichnung,
					bMitVersteckten, bSortiertNachLieferant, bNurLagerbewirtschaftete, tStichtag, bMitStaffelpreisen, 
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public Integer createLieferant(LieferantDto lieferantDto) throws ExceptionLP {
		Integer ret = null;
		try {
			ret = lieferantFac.createLieferant(lieferantDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return ret;
	}

	public Integer createLieferantFuerEingangsrechnungAusQRCode(String name, String land, String plz, String ort,
			String strasse, String waehrung, String iban) throws ExceptionLP {
		Integer ret = null;
		try {
			ret = lieferantFac.createLieferantFuerEingangsrechnungAusQRCode(name, land, plz, ort, strasse, waehrung,
					iban, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return ret;
	}

	public Integer createLieferantbeurteilung(LieferantbeurteilungDto lieferantbeurteilungDto) throws ExceptionLP {
		Integer ret = null;
		try {
			ret = lieferantFac.createLieferantbeurteilung(lieferantbeurteilungDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return ret;
	}

	public void removeLieferant(LieferantDto lieferantDtoI) throws ExceptionLP {
		try {
			lieferantFac.removeLieferant(lieferantDtoI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeLieferantbeurteilung(LieferantbeurteilungDto lieferantbeurteilungDto) throws ExceptionLP {
		try {
			lieferantFac.removeLieferantbeurteilung(lieferantbeurteilungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeLieferantPartnerRechnungsadresse(LieferantDto lieferantDtoI) throws ExceptionLP {
		try {
			lieferantFac.removeLieferantPartnerRechnungsadresse(lieferantDtoI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateLieferant(LieferantDto lieferantDto) throws ExceptionLP {
		try {
			lieferantFac.updateLieferant(lieferantDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateLieferantbeurteilung(LieferantbeurteilungDto lieferantbeurteilungDto) throws ExceptionLP {
		try {
			lieferantFac.updateLieferantbeurteilung(lieferantbeurteilungDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateLieferantRechnungsadresse(LieferantDto lieferantDto) throws ExceptionLP {
		try {
			lieferantFac.updateLieferantRechnungsadresse(lieferantDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public LieferantDto lieferantFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return lieferantFac.lieferantFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LieferantDto lieferantFindByPrimaryKeyOhneExc(Integer iId) throws ExceptionLP {
		try {
			return lieferantFac.lieferantFindByPrimaryKeyOhneExc(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LieferantbeurteilungDto lieferantbeurteilungFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return lieferantFac.lieferantbeurteilungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<Integer> lieferantFindByIBAN(String iban) throws ExceptionLP {
		try {
			return lieferantFac.lieferantFindByIBAN(iban, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String lieferantbeurteilungfindByLetzteBeurteilung(Integer lieferantIId) throws ExceptionLP {
		try {

			LieferantbeurteilungDto[] dtos = lieferantFac.lieferantbeurteilungfindByLetzteBeurteilungByLieferantIId(
					lieferantIId, new Timestamp(System.currentTimeMillis()));

			if (dtos != null && dtos.length > 0) {
				return " " + dtos[0].getIPunkte() + " (" + dtos[0].getCKlasse() + ")";
			}

			return " ?";
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void zusammenfuehrenLieferant(LieferantDto lieferantZielDto, int lieferantQuellDtoIid,
			Integer iLieferantPartnerIId) throws ExceptionLP {
		try {
			lieferantFac.zusammenfuehrenLieferant(lieferantZielDto, lieferantQuellDtoIid, iLieferantPartnerIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public LieferantDto lieferantFindByiIdPartnercNrMandantOhneExc(Integer iIdPartnerI, String cNrMandantI)
			throws ExceptionLP {
		try {
			return lieferantFac.lieferantFindByiIdPartnercNrMandantOhneExc(iIdPartnerI, cNrMandantI,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LieferantDto lieferantFindByCKundennrcNrMandant(String cKundennr, String cNrMandantI) throws ExceptionLP {
		try {
			return lieferantFac.lieferantFindByCKundennrcNrMandant(cKundennr, cNrMandantI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createVerstecktenLieferantAusKunden(Integer kundeIId) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = lieferantFac.createVerstecktenLieferantAusKunden(kundeIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	// ******************************************************************************
	public LflfliefergruppePK createLflfliefergruppe(LflfliefergruppeDto lflfliefergruppeDto) throws Exception {
		LflfliefergruppePK pk = null;
		try {
			pk = lieferantFac.createLflfliefergruppe(lflfliefergruppeDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return pk;
	}

	public void removeLflfliefergruppe(Integer lieferantIId, Integer lfliefergruppeIId) throws Exception {
		try {
			lieferantFac.removeLflfliefergruppe(lieferantIId, lfliefergruppeIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public LflfliefergruppeDto lflfliefergruppeFindByPrimaryKey(Integer lieferantIId, Integer lfliefergruppeIId)
			throws Exception {

		LflfliefergruppeDto lflfliefergruppeDto = null;
		try {
			lflfliefergruppeDto = lieferantFac.lflfliefergruppeFindByPrimaryKey(lieferantIId, lfliefergruppeIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return lflfliefergruppeDto;
	}

	public String createKreditorenkontoNummerZuLieferantenAutomatisch(Integer lieferantIId, String kontonummerVorgabe)
			throws ExceptionLP {
		KontoDto k = null;
		try {
			k = lieferantFac.createKreditorenkontoZuLieferantenAutomatisch(lieferantIId, false, kontonummerVorgabe,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		if (k == null)
			return null;
		else
			return k.getCNr();
	}

	public KontoDto createKreditorenkontoZuLieferantenAutomatisch(Integer lieferantIId, String kontonummerVorgabe)
			throws ExceptionLP {
		KontoDto k = null;
		try {
			k = lieferantFac.createKreditorenkontoZuLieferantenAutomatisch(lieferantIId, true, kontonummerVorgabe,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public void pruefeLieferant(Integer LieferantIId, String belegartCNr, InternalFrame internalFrame)
			throws ExceptionLP {
		try {
			LieferantDto lieferantDto = lieferantFac.lieferantFindByPrimaryKey(LieferantIId, LPMain.getTheClient());
			if (lieferantDto.getTBestellsperream() != null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis.lieferant"),
						LPMain.getTextRespectUISPr("lieferant.gesperrt"));
			}
			if (lieferantDto.getCHinweisextern() != null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis.lieferant"),
						LPMain.getTextRespectUISPr("lief.hinweisextern") + " " + lieferantDto.getCHinweisextern());
			}
			if (lieferantDto.getCHinweisintern() != null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis.lieferant"),
						LPMain.getTextRespectUISPr("lief.hinweisintern") + " " + lieferantDto.getCHinweisintern());
			}
			if (belegartCNr != null) {
				// Partnerhinweise anzeigen
				ArrayList<PartnerkommentarDto> alHinweise = DelegateFactory.getInstance().getPartnerServicesDelegate()
						.getPartnerhinweise(lieferantDto.getPartnerIId(), false, belegartCNr);
				for (int i = 0; i < alHinweise.size(); i++) {

					PartnerkommentarartDto pkDto = DelegateFactory.getInstance().getPartnerServicesDelegate()
							.partnerkommentarartFindByPrimaryKey(alHinweise.get(i).getPartnerkommentarartIId());

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis") + ": " + pkDto.getCBez(),
							Helper.strippHTML(alHinweise.get(i).getXKommentar()));
				}

				ArrayList<byte[]> bilder = DelegateFactory.getInstance().getPartnerServicesDelegate()
						.getPartnerkommentarBilderUndPDFAlsBilderUmgewandelt(lieferantDto.getPartnerIId(), false,
								belegartCNr, PartnerServicesFac.PARTNERKOMMENTARART_HINWEIS);

				if (bilder != null && bilder.size() > 0) {
					DialogFactory.showArtikelHinweisBild(bilder, internalFrame);
				}
			}

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public String importiereArtikellieferant(Integer lieferantIId, List<String[]> daten, boolean bImportieren,
			TheClientDto theClientDto) {
		return lieferantFac.importiereArtikellieferant(lieferantIId, daten, bImportieren, theClientDto);
	}

	public VendidataPartnerExportResult exportiere4VendingLieferanten(boolean checkOnly) throws ExceptionLP {
		try {
			return lieferantFac.exportiere4VendingLieferanten(checkOnly, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public IWebpartnerDto weblieferantFindByLieferantIIdOhneExc(Integer lieferantIId) throws ExceptionLP {
		try {
			return lieferantFac.weblieferantFindByLieferantIIdOhneExc(lieferantIId, LPMain.getTheClient());
		} catch (Throwable e) {
			handleThrowable(e);
			return null;
		}
	}

	public boolean isWeblieferant(Integer lieferantIId) throws ExceptionLP {
		try {
			IWebpartnerDto webpartnerDto = lieferantFac.weblieferantFindByLieferantIIdOhneExc(lieferantIId,
					LPMain.getTheClient());
			return webpartnerDto != null;
		} catch (Throwable e) {
			handleThrowable(e);
			return false;
		}
	}

	public Integer createWeblieferant(IWebpartnerDto webpartnerDto) throws ExceptionLP {
		try {
			return lieferantFac.createWeblieferant(webpartnerDto, LPMain.getTheClient());
		} catch (Throwable e) {
			handleThrowable(e);
			return null;
		}
	}

	public void updateWeblieferant(IWebpartnerDto webpartnerDto) throws ExceptionLP {
		try {
			lieferantFac.updateWeblieferant(webpartnerDto);
		} catch (Throwable e) {
			handleThrowable(e);
		}
	}

	public void removeWeblieferant(Integer webpartnerIId) throws ExceptionLP {
		try {
			lieferantFac.removeWeblieferant(webpartnerIId);
		} catch (Throwable e) {
			handleThrowable(e);
		}
	}
	public void pflegeEKpreise(Integer lieferantIId, Integer artikelgruppeIId, java.sql.Date tGueltigab,
			BigDecimal nProzent) throws ExceptionLP {
		try {
			lieferantFac.pflegeEKpreise(lieferantIId, artikelgruppeIId,tGueltigab,nProzent,LPMain.getTheClient());
		} catch (Throwable e) {
			handleThrowable(e);
		}
	}
	
	
}
