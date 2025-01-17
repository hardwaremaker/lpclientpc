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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikellagerDto;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.BelegInfos;
import com.lp.server.artikel.service.GeraetesnrDto;
import com.lp.server.artikel.service.HandlagerbewegungDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.LagerumbuchungDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class LagerDelegate extends Delegate {
	private Context context;
	private LagerFac lagerFac;

	public LagerDelegate() throws Exception {
		context = new InitialContext();
		lagerFac = lookupFac(context, LagerFac.class);
	}

	public Integer createLager(LagerDto lagerDto) throws ExceptionLP {
		try {
			lagerDto.setMandantCNr(LPMain.getInstance().getTheClient().getMandant());
			return lagerFac.createLager(lagerDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	public BigDecimal[] getVerfuegbarkeitUndGestehungspreis(Integer artikelIId) throws ExceptionLP {
		try {
			return lagerFac.getVerfuegbarkeitUndGestehungspreis(artikelIId,LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	public Integer createLagerplatz(LagerplatzDto lagerplatzDto) throws ExceptionLP {
		try {

			return lagerFac.createLagerplatz(lagerplatzDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal sofortverbrauchAllerArtikelAbbuchen() throws ExceptionLP {
		try {

			return lagerFac.sofortverbrauchAllerArtikelAbbuchen(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LagerDto getLagerDesErstenArtikellagerplatzes(Integer artikelIId) throws ExceptionLP {
		try {

			return lagerFac.getLagerDesErstenArtikellagerplatzes(artikelIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeLager(LagerDto lagerDto) throws ExceptionLP {
		try {
			lagerDto.setMandantCNr(LPMain.getInstance().getTheClient().getMandant());
			lagerFac.removeLager(lagerDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeLagerplatz(LagerplatzDto lagerplatzDto) throws ExceptionLP {
		try {
			lagerFac.removeLagerplatz(lagerplatzDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer createArtikellagerplaetze(ArtikellagerplaetzeDto artikellagerplaetzeDto) throws ExceptionLP {
		try {
			return lagerFac.createArtikellagerplaetze(artikellagerplaetzeDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer artikelsnrchnrIIdFindByArtikelIIdCSeriennrchargennr(Integer artikelIId, String cSerienchargennr)
			throws ExceptionLP {
		try {
			return lagerFac.artikelsnrchnrIIdFindByArtikelIIdCSeriennrchargennr(artikelIId, cSerienchargennr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer updateArtikelsnrchnr(Integer artikelIId, String cSerienchargennr) throws ExceptionLP {
		try {
			return lagerFac.updateArtikelsnrchnr(artikelIId, cSerienchargennr, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public boolean pruefeObSnrChnrAufBelegGebuchtWurde(String belegartCNr, Integer belegartIId, Integer artikelIId,
			String cSnrChnr) throws ExceptionLP {
		return lagerFac.pruefeObSnrChnrAufBelegGebuchtWurde(belegartCNr, belegartIId, artikelIId, cSnrChnr);

	}

	public void removeArtikellagerplaetze(ArtikellagerplaetzeDto artikellagerplaetzeDto) throws ExceptionLP {
		try {
			lagerFac.removeArtikellagerplaetze(artikellagerplaetzeDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateArtikellagerplaetze(ArtikellagerplaetzeDto artikellagerplaetzeDto) throws ExceptionLP {
		try {
			lagerFac.updateArtikellagerplaetze(artikellagerplaetzeDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateGestpreisArtikellager(ArtikellagerDto artikellagerDto) throws ExceptionLP {
		try {
			lagerFac.updateGestpreisArtikellager(artikellagerDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public int aendereEinzelneSerienChargennummerEinesArtikel(Integer artikelIId, String snrChnr_Alt,
			String snrChnr_Neu, String version_Alt, String version_Neu) throws ExceptionLP {
		try {
			return lagerFac.aendereEinzelneSerienChargennummerEinesArtikel(artikelIId, snrChnr_Alt, snrChnr_Neu,
					version_Alt, version_Neu, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return 0;
		}
	}

	public ArtikellagerplaetzeDto artikellagerplaetzeFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return lagerFac.artikellagerplaetzeFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikellagerplaetzeDto artikellagerplaetzeFindByArtikelIIdLagerIId(Integer artikelIId, Integer lagerIId)
			throws ExceptionLP {
		try {
			return lagerFac.artikellagerplaetzeFindByArtikelIIdLagerIId(artikelIId, lagerIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getLagerplaezteEinesArtikels(Integer artikelIId, Integer lagerIId) throws ExceptionLP {
		try {
			return lagerFac.getLagerplaezteEinesArtikels(artikelIId, lagerIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikellagerplaetzeDto artikellagerplaetzeFindByArtikelIIdLagerplatzIId(Integer artikelIId,
			Integer lagerplatzIId) throws ExceptionLP {
		try {
			return lagerFac.artikellagerplaetzeFindByArtikelIIdLagerplatzIId(artikelIId, lagerplatzIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikellagerDto artikellagerFindByPrimaryKey(Integer artikelIId, Integer lagerIId) throws ExceptionLP {
		try {
			return lagerFac.artikellagerFindByPrimaryKey(artikelIId, lagerIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void artikellagerplatzCRUD(Integer artikelIId, Integer lagerIId, Integer lagerplatzIId) throws ExceptionLP {
		try {
			lagerFac.artikellagerplatzCRUD(artikelIId, lagerIId, lagerplatzIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public LagerplatzDto lagerplatzFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return lagerFac.lagerplatzFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateLager(LagerDto lagerDto) throws ExceptionLP {
		try {
			lagerDto.setMandantCNr(LPMain.getInstance().getTheClient().getMandant());
			lagerFac.updateLager(lagerDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateLagerplatz(LagerplatzDto lagerplatzDto) throws ExceptionLP {
		try {
			lagerFac.updateLagerplatz(lagerplatzDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void vertauscheArtikellagerplaetze(Integer iId1, Integer iId2) throws ExceptionLP {
		try {
			lagerFac.vertauscheArtikellagerplaetze(iId1, iId2);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void vertauscheLager(Integer iId1, Integer iId2) throws ExceptionLP {
		try {
			lagerFac.vertauscheLager(iId1, iId2);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void konstruiereLagergewegungenLSREAusBelegen() throws ExceptionLP {
		try {
			lagerFac.konstruiereLagergewegungenLSREAusBelegen(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void konstruiereLagergewegungenBESTAusBelegen() throws ExceptionLP {
		try {
			lagerFac.konstruiereLagergewegungenBESTAusBelegen(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void konstruiereLagergewegungenLOSAusBelegen() throws ExceptionLP {
		try {
			lagerFac.konstruiereLagergewegungenLOSAusBelegen(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void kopiereChargeneigenschaften(Integer i_id_buchungVon, Integer i_id_buchungNach) throws ExceptionLP {
		try {
			lagerFac.kopiereChargeneigenschaften(i_id_buchungVon, i_id_buchungNach,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void konstruiereLagergewegungenHAND() throws ExceptionLP {
		try {
			lagerFac.konstruiereLagergewegungenHAND(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public LagerDto lagerFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return lagerFac.lagerFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LagerDto lagerFindByPrimaryKeyOhneExc(Integer iId) throws ExceptionLP {
		try {
			return lagerFac.lagerFindByPrimaryKeyOhneExc(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public boolean sindBereitsLagerbewegungenVorhanden(Integer artikelIId) throws ExceptionLP {
		try {
			return lagerFac.sindBereitsLagerbewegungenVorhanden(artikelIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return true;
		}
	}

	public LagerDto lagerFindByCNrByMandantCNr(String cNr) throws ExceptionLP {
		try {
			return lagerFac.lagerFindByCNrByMandantCNr(cNr, LPMain.getInstance().getTheClient().getMandant());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LagerDto[] lagerFindByPartnerIIdStandortMandantCNr(Integer partnerIIdStandort, boolean bMitVersteckten)
			throws ExceptionLP {
		try {
			return lagerFac.lagerFindByPartnerIIdStandortMandantCNr(partnerIIdStandort,
					LPMain.getInstance().getTheClient().getMandant(), bMitVersteckten);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LagerDto[] lagerFindByMandantCNr(String sMandantI) throws ExceptionLP {
		try {
			return lagerFac.lagerFindByMandantCNr(sMandantI);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LagerDto lagerFindByMandantCNrLagerartCNrOhneExc(String sMandantI, String sLagerartI) throws ExceptionLP {
		LagerDto oLagerDtoO = null;

		try {
			oLagerDtoO = lagerFac.lagerFindByMandantCNrLagerartCNrOhneExc(sMandantI, sLagerartI);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oLagerDtoO;
	}

	public BigDecimal getGemittelterGestehungspreisEinesLagers(Integer artikelIId, Integer lagerIId)
			throws ExceptionLP {
		try {
			return lagerFac.getGemittelterGestehungspreisEinesLagers(artikelIId, lagerIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getGemittelterGestehungspreisAllerLaegerEinesMandanten(Integer artikelIId) throws ExceptionLP {
		try {
			return lagerFac.getGemittelterGestehungspreisAllerLaegerEinesMandanten(artikelIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String istLagerplatzBereitsDurchAnderenArtikelBelegt(Integer artikelIId, Integer lagerplatzIId)
			throws ExceptionLP {
		try {
			return lagerFac.istLagerplatzBereitsDurchAnderenArtikelBelegt(artikelIId, lagerplatzIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public GeraetesnrDto[] getGeraeteseriennummerEinerLagerbewegung(String belegartCnr, Integer belegartpositionIId,
			String cSnr) {
		return lagerFac.getGeraeteseriennummerEinerLagerbewegung(belegartCnr, belegartpositionIId, cSnr);
	}

	public BigDecimal getGemittelterGestehungspreisDesHauptlagers(Integer artikelIId) throws ExceptionLP {
		try {
			return lagerFac.getGemittelterGestehungspreisDesHauptlagers(artikelIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getLagerstand(Integer artikelIId, Integer lagerIId) throws ExceptionLP {
		try {
			return lagerFac.getLagerstand(artikelIId, lagerIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getPaternosterLagerstand(Integer artikelIId) throws ExceptionLP {
		try {
			return lagerFac.getPaternosterLagerstand(artikelIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getLagerstandZumZeitpunkt(Integer artikelIId, Integer lagerIId, java.sql.Timestamp tsZeitpunkt)
			throws ExceptionLP {
		try {
			return lagerFac.getLagerstandZumZeitpunkt(artikelIId, lagerIId, tsZeitpunkt,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getLagerstandsVeraenderungOhneInventurbuchungen(Integer artikelIId, Integer lagerIId,
			java.sql.Timestamp tVon, java.sql.Timestamp tBis, String cSnrChnr) throws ExceptionLP {
		try {
			return lagerFac.getLagerstandsVeraenderungOhneInventurbuchungen(artikelIId, lagerIId, tVon, tBis, cSnrChnr,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getLagerstandAllerLagerEinesMandanten(Integer artikelIId) throws ExceptionLP {
		try {
			return lagerFac.getLagerstandAllerLagerEinesMandanten(artikelIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getLagerstandAllerLagerAllerMandanten(Integer artikelIId, boolean bMitKonsignationslager)
			throws ExceptionLP {
		try {
			return lagerFac.getLagerstandAllerLagerAllerMandanten(artikelIId, bMitKonsignationslager,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getLagerstandAllerLagerEinesMandanten(Integer artikelIId, boolean bMitKonsignationslager)
			throws ExceptionLP {
		try {
			return lagerFac.getLagerstandAllerLagerEinesMandanten(artikelIId, bMitKonsignationslager,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<String[]> getAlleWarenzugaengeFuerProFirst() throws ExceptionLP {
		try {
			return lagerFac.getAlleWarenzugaengeFuerProFirst(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getMindestverkaufspreis(Integer artikelIId, Integer lagerIId, BigDecimal nMenge)
			throws ExceptionLP {
		try {
			return lagerFac.getMindestverkaufspreis(artikelIId, lagerIId, nMenge, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal[] getSummeLagermindesUndLagerSollstandEinesStandorts(Integer artikelIId,
			Integer partnerIIdStandort) throws ExceptionLP {
		try {
			return lagerFac.getSummeLagermindesUndLagerSollstandEinesStandorts(artikelIId, partnerIIdStandort,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllSprLagerarten() throws ExceptionLP {
		try {
			return lagerFac.getAllSprLagerArten(LPMain.getInstance().getTheClient().getLocUiAsString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer getPartnerIIdStandortEinesLagers(Integer lagerIId) throws ExceptionLP {
		try {
			return lagerFac.getPartnerIIdStandortEinesLagers(lagerIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllLager() throws ExceptionLP {
		try {
			return lagerFac.getAllLager(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PaneldatenDto[] getLetzteChargeninfosEinesArtikels(Integer artikelIId, String belegartCNr,
			Integer belegartIId, Integer belegartpositionIId, String cSerienchargennummer) throws ExceptionLP {
		try {
			return lagerFac.getLetzteChargeninfosEinesArtikels(artikelIId, belegartCNr, belegartIId,
					belegartpositionIId, cSerienchargennummer, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAlleStandorte() throws ExceptionLP {
		try {
			return lagerFac.getAlleStandorte(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer getArtikelIIdUeberSeriennummer(String snr) throws ExceptionLP {
		try {
			return lagerFac.getArtikelIIdUeberSeriennummer(snr, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer getArtikelIIdUeberSeriennummerAbgang(String snr) throws ExceptionLP {
		try {
			return lagerFac.getArtikelIIdUeberSeriennummerAbgang(snr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLager(Integer artikelIId, Integer lagerIId)
			throws ExceptionLP {
		try {
			return lagerFac.getAllSerienChargennrAufLagerInfoDtos(artikelIId, lagerIId, false, null,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BelegInfos wurdeSeriennummerSchonEinmalVerwendet(Integer artikelIId, String snr) throws ExceptionLP {
		try {
			return lagerFac.wurdeSeriennummerSchonEinmalVerwendet(artikelIId, snr, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BelegInfos wurdeChargennummerSchonEinmalVerwendet(Integer artikelIId, String chargennummer)
			throws ExceptionLP {
		try {
			return lagerFac.wurdeChargennummerSchonEinmalVerwendet(artikelIId, chargennummer,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLager(Integer artikelIId, Integer lagerIId,
			boolean bSortiertNachSerienChargennummer) throws ExceptionLP {
		try {
			return lagerFac.getAllSerienChargennrAufLagerInfoDtos(artikelIId, lagerIId,
					bSortiertNachSerienChargennummer, null, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtosMitBereitsVerbrauchten(
			Integer artikelIId, Integer lagerIId, boolean bSortiertNachSerienChargennummer) throws ExceptionLP {
		try {
			return lagerFac.getAllSerienChargennrAufLagerInfoDtosMitBereitsVerbrauchten(artikelIId, lagerIId,
					bSortiertNachSerienChargennummer, null, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getAllSerienChargennrAufLagerInfo(Integer artikelIId, Integer lagerIId) throws ExceptionLP {
		try {
			return lagerFac.getAllSerienChargennrAufLagerInfo(artikelIId, lagerIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtos(Integer artikelIId, Integer lagerIId)
			throws ExceptionLP {
		try {

			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_AELTESTE_CHARGENNUMMER_VORSCHLAGEN);

			if ((Boolean) parameter.getCWertAsObject()) {
				return lagerFac.getAllSerienChargennrAufLagerInfoDtos(artikelIId, lagerIId, false, null,
						LPMain.getInstance().getTheClient());
			} else {
				return lagerFac.getAllSerienChargennrAufLagerInfoDtos(artikelIId, lagerIId, true, null,
						LPMain.getInstance().getTheClient());
			}

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public SeriennrChargennrAufLagerDto[] getAllSerienChargennrAufLagerInfoDtos(Integer artikelIId, Integer lagerIId,
			String cSeriennrChargennr, boolean bSortiertNachSerienChargennummer, java.sql.Timestamp tStichtag)
			throws ExceptionLP {
		try {

			return lagerFac.getAllSerienChargennrAufLagerInfoDtos(artikelIId, lagerIId, cSeriennrChargennr, true, null,
					LPMain.getInstance().getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public List<SeriennrChargennrMitMengeDto> getAllSeriennrchargennrEinerBelegartposition(String belegartCNr,
			Integer belegartpositionIId) throws ExceptionLP {
		try {
			return lagerFac.getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(belegartCNr,
					belegartpositionIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getMengeAufLager(Integer artikelIId, Integer lagerIId, String cSeriennrchargennr)
			throws ExceptionLP {
		try {
			return lagerFac.getMengeAufLager(artikelIId, lagerIId, cSeriennrchargennr,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Object[] getMengeMehrererSeriennummernChargennummernAufLager(Integer artikelIId, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> cMehrereSeriennrchargennr) throws ExceptionLP {
		try {
			return lagerFac.getMengeMehrererSeriennummernChargennummernAufLager(artikelIId, lagerIId,
					cMehrereSeriennrchargennr, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printSeriennummern(Integer lagerIId, Integer artikelIId, String[] cSeriennrs,
			boolean bSortIdent, boolean bMitGeraeteseriennummern, String snrWildcard, String versionWildcard)
			throws ExceptionLP {
		try {
			return lagerFac.printSeriennummern(lagerIId, artikelIId, cSeriennrs, snrWildcard, new Boolean(bSortIdent),
					bMitGeraeteseriennummern, versionWildcard, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String pruefeQuickLagerstandGegenEchtenLagerstand(String artikelnummerInput, boolean bFehlerKorrigieren)
			throws ExceptionLP {
		try {
			return lagerFac.pruefeQuickLagerstandGegenEchtenLagerstand(artikelnummerInput, bFehlerKorrigieren,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String fehlendeAbbuchungenNachtragen(Timestamp tAb) throws ExceptionLP {
		try {
			return lagerFac.fehlendeAbbuchungenNachtragen(tAb, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String pruefeIIdBuchungen(Integer artikelIIdInput) throws ExceptionLP {
		try {
			return lagerFac.pruefeIIdBuchungen(artikelIIdInput, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String pruefeLagerabgangurpsrung() throws ExceptionLP {
		try {
			return lagerFac.pruefeLagerabgangurpsrung(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String pruefeVollstaendigVerbraucht(Integer artikelIId, boolean bKorrigieren) throws ExceptionLP {
		try {
			return lagerFac.pruefeVollstaendigVerbraucht(artikelIId, bKorrigieren, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String pruefeBelegeMitLagerbewegungen() throws ExceptionLP {
		try {
			return lagerFac.pruefeBelegeMitLagerbewegungen(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void pruefeSeriennummernMitVersion() throws ExceptionLP {
		try {
			lagerFac.pruefeSeriennummernMitVersion(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public String pruefeVerbrauchteMenge() throws ExceptionLP {
		try {
			return lagerFac.pruefeVerbrauchteMenge(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String pruefeLagerbewegungenMitBelege() throws ExceptionLP {
		try {
			return lagerFac.pruefeLagerbewegungenMitBelege(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String wirdLagermindeststandUnterschritten(java.sql.Date tPositionsdatum, BigDecimal bdPositionsmenge,
			Integer artikelIId, Integer partnerIIdStandort) throws ExceptionLP {
		try {
			return lagerFac.wirdLagermindeststandUnterschritten(tPositionsdatum, bdPositionsmenge, artikelIId,
					LPMain.getInstance().getTheClient(), partnerIIdStandort);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public HandlagerbewegungDto handlagerbewegungFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return lagerFac.handlagerbewegungFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LagerbewegungDto[] lagerbewegungFindByBelegartCNrBelegartPositionIIdCSeriennrchargennr(String belegartCNr,
			Integer belegartpositionIId, String cSnrChnr) throws ExceptionLP {
		try {
			return lagerFac.lagerbewegungFindByBelegartCNrBelegartPositionIIdCSeriennrchargennr(belegartCNr,
					belegartpositionIId, cSnrChnr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LagerbewegungDto[] lagerbewegungFindByBelegartCNrBelegartPositionIId(String belegartCNr,
			Integer belegartpositionIId) throws ExceptionLP {
		try {
			return lagerFac.lagerbewegungFindByBelegartCNrBelegartPositionIId(belegartCNr, belegartpositionIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LagerbewegungDto getLetzteintrag(String belegartCNr, Integer belegartpositionIId, String cSeriennrchargennr)
			throws ExceptionLP {
		try {
			return lagerFac.getLetzteintrag(belegartCNr, belegartpositionIId, cSeriennrchargennr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LagerbewegungDto[] lagerbewegungFindByArtikelIIdCSeriennrChargennr(Integer artikelIId, String cSnrChnr)
			throws ExceptionLP {
		try {
			return lagerFac.lagerbewegungFindByArtikelIIdCSeriennrChargennr(artikelIId, cSnrChnr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LagerbewegungDto lagerbewegungFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return lagerFac.lagerbewegungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LagerbewegungDto[] lagerbewegungFindByIIdBuchung(Integer iIdBuchung) throws ExceptionLP {
		try {
			return lagerFac.lagerbewegungFindByIIdBuchung(iIdBuchung);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LagerumbuchungDto[] lagerumbuchungFindByIdAbbuchung(Integer iIdBuchungAbbuchung) throws ExceptionLP {
		try {
			return lagerFac.lagerumbuchungFindByIdAbbuchung(iIdBuchungAbbuchung);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LagerumbuchungDto[] lagerumbuchungFindByIdZubuchung(Integer iIdBuchungZubuchung) throws ExceptionLP {
		try {
			return lagerFac.lagerumbuchungFindByIdZubuchung(iIdBuchungZubuchung);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateHandlagerbewegung(HandlagerbewegungDto handlagerbewegungDto) throws ExceptionLP {
		try {
			lagerFac.updateHandlagerbewegung(handlagerbewegungDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void versionInLagerbewegungUpdaten(String belegartCNr, Integer belegartpositionIId, String cVersion)
			throws ExceptionLP {
		try {
			lagerFac.versionInLagerbewegungUpdaten(belegartCNr, belegartpositionIId, cVersion);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeHandlagerbewegung(HandlagerbewegungDto handlagerbewegungDto) throws ExceptionLP {
		try {
			lagerFac.removeHandlagerbewegung(handlagerbewegungDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public java.sql.Timestamp getDatumLetzterZugangsOderAbgangsbuchung(Integer artikelIId, boolean bAbgang)
			throws ExceptionLP {
		try {
			return lagerFac.getDatumLetzterZugangsOderAbgangsbuchung(artikelIId, bAbgang);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer getLetzteWEP_IID(Integer artikelIId) throws ExceptionLP {
		try {
			return lagerFac.getLetzteWEP_IID(artikelIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createHandlagerbewegung(HandlagerbewegungDto handlagerbewegungDto) throws ExceptionLP {
		try {
			return lagerFac.createHandlagerbewegung(handlagerbewegungDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer bucheUm(Integer artikelIId_Quelle, Integer lagerIId_Quelle, Integer artikelIId_Ziel,
			Integer lagerIId_Ziel, BigDecimal fMengeUmzubuchen, List<SeriennrChargennrMitMengeDto> cSeriennrChargennr,
			String sKommentar, BigDecimal vkpreis) throws ExceptionLP {
		try {
			return lagerFac.bucheUm(artikelIId_Quelle, lagerIId_Quelle, artikelIId_Ziel, lagerIId_Ziel,
					fMengeUmzubuchen, cSeriennrChargennr, sKommentar, vkpreis, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void gestehungspreiseImportieren(ArrayList<Object[]> alDaten, Integer lagerIId) throws ExceptionLP {
		try {
			lagerFac.gestehungspreiseImportieren(alDaten, lagerIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public List<LosDto> chargennummerWegwerfen(Integer artikelIId, String chargennummer, List<LosDto> losDtos)
			throws ExceptionLP {
		try {
			return lagerFac.chargennummerWegwerfen(artikelIId, chargennummer, losDtos, true,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public List<LosDto> getAlleBetroffenenLoseEinerArtikelIIdUndCharge(Integer artikelIId, String chargennummer)
			throws ExceptionLP {
		try {
			return lagerFac.getAlleBetroffenenLoseEinerArtikelIIdUndCharge(artikelIId, chargennummer);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Ein Lager ueber seine Lagerart bestimmen.
	 * 
	 * @param sMandantI  String
	 * @param sLagerartI Lagerart
	 * @throws ExceptionLP
	 * @throws Throwable
	 * @return LagerDto
	 */
	public LagerDto lagerFindByMandantCNrLagerartCNr(String sMandantI, String sLagerartI)
			throws ExceptionLP, Throwable {
		try {
			return lagerFac.lagerFindByMandantCNrLagerartCNr(sMandantI, sLagerartI);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	/**
	 * Das Hauptlager des Mandanten des aktuellen Benutzers bestimmen.
	 * 
	 * @return LagerDto das Hauptlager
	 * @throws ExceptionLP Ausnahme
	 */
	public LagerDto getHauptlagerDesMandanten() throws ExceptionLP {
		LagerDto oHauptlagerO = null;

		try {
			oHauptlagerO = lagerFac.getHauptlagerDesMandanten(LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oHauptlagerO;
	}

	public LagerDto getLagerWertgutschriftDesMandanten() throws ExceptionLP {
		LagerDto oHauptlagerO = null;

		try {
			oHauptlagerO = lagerFac.getLagerWertgutschriftDesMandanten(LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return oHauptlagerO;
	}

	public LagerDto getLagerDtoWennNurAufEinemTypLagerstandVorhandenIst(Integer artikelIId) throws ExceptionLP {
		try {
			return lagerFac.getLagerDtoWennNurAufEinemTypLagerstandVorhandenIst(artikelIId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public HandlagerbewegungDto getZugehoerigeUmbuchung(Integer handlagerbewegungIId) throws ExceptionLP {
		try {
			return lagerFac.getZugehoerigeUmbuchung(handlagerbewegungIId, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public String getNaechsteSeriennummer(Integer artikelIId, String uebersteuerteSeriennummer, String beginntMit)
			throws ExceptionLP {
		try {
			return lagerFac.getNaechsteSeriennummer(artikelIId, uebersteuerteSeriennummer, beginntMit,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public String getNaechsteChargennummer(Integer artikelIId, String letzteChargennummerVomClient) throws ExceptionLP {
		try {
			return lagerFac.getNaechsteChargennummer(artikelIId, letzteChargennummerVomClient,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public String getNaechsteTafelnummer(Integer artikelIId, String letzteTafelnummerVomClient) throws ExceptionLP {
		try {
			return lagerFac.getNaechsteTafelnummer(artikelIId, letzteTafelnummerVomClient,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public HashMap<String, BigDecimal> getLagerstaendeAllerLagerartenOhneKeinLager(Integer artikelIId)
			throws ExceptionLP {
		try {
			return lagerFac.getLagerstaendeAllerLagerartenOhneKeinLager(artikelIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public Set<String> pruefeObSeriennummernExistieren(ArtikelId artikelIId, Collection<String> snrs) throws ExceptionLP {
		try {
			return lagerFac.pruefeObSeriennummernExistieren(artikelIId, snrs, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return Collections.emptySet();
		}
	}

}
