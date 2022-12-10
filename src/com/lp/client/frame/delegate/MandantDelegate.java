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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.client.util.HelperTimestamp;
import com.lp.client.util.MwstsatzChangedInfo;
import com.lp.server.system.service.DokumentenlinkDto;
import com.lp.server.system.service.DokumentenlinkbelegDto;
import com.lp.server.system.service.KostentraegerDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ModulberechtigungDto;
import com.lp.server.system.service.MwstsatzCodeDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.system.service.ZusatzfunktionberechtigungDto;
import com.lp.server.util.MwstsatzId;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich um den Mandantdelegate.</I> </p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum Martin; 10.01.05</p>
 * 
 * <p>@author $Author: adi $</p>
 * 
 * @version $Revision: 1.17 $ Date $Date: 2012/07/19 15:24:55 $
 */
public class MandantDelegate extends Delegate {
	private Context context;
	private MandantFac mandantFac;

	/**
	 * Cache for performance.
	 */
	private Map<?, ?> hmMWSTSaetze = null;

	public MandantDelegate() throws Exception {
		context = new InitialContext();
		mandantFac = lookupFac(context, MandantFac.class);
	}

	public Integer createMwstsatz(MwstsatzDto mwstsatzDto) throws ExceptionLP {

		Integer iId = null;
		try {
			/*
			 * mwstsatzDto.getMwstsatzbezDto().setMandantCNr(
			 * LPMain.getInstance().getTheClient().getMandant());
			 */
			iId = mandantFac.createMwstsatz(mwstsatzDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			// mwstsatzDto.getMwstsatzbezDto().setMandantCNr(null);
			handleThrowable(ex);
		}
		// Cache aktualisieren
		reloadMwstMap();
		return iId;
	}

	public Integer createMwstsatzbez(MwstsatzbezDto mwstsatzbezDto) throws ExceptionLP {
		Integer iId = null;
		try {
			mwstsatzbezDto.setMandantCNr(LPMain.getInstance().getTheClient().getMandant());
			iId = mandantFac.createMwstsatzbez(mwstsatzbezDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public void removeMwstsatz(MwstsatzDto mwstsatzDto) throws ExceptionLP {
		try {
			mandantFac.removeMwstsatz(mwstsatzDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		// Cache aktualisieren
		reloadMwstMap();
	}

	public void removeDokumentenlink(DokumentenlinkDto dokumentenlinkDto) throws ExceptionLP {
		try {
			mandantFac.removeDokumentenlink(dokumentenlinkDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeKostentraeger(KostentraegerDto dto) throws ExceptionLP {
		try {
			mandantFac.removeKostentraeger(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeMwstsatzbez(MwstsatzbezDto mwstsatzbezDto) throws ExceptionLP {
		try {
			mandantFac.removeMwstsatzbez(mwstsatzbezDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateMwstsatz(MwstsatzDto mwstsatzDto) throws ExceptionLP {
		try {
			mandantFac.updateMwstsatz(mwstsatzDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		// Cache aktualisieren
		reloadMwstMap();
	}

	public void updateMwstsatzbez(MwstsatzbezDto mwstsatzbezDto) throws ExceptionLP {
		try {
			mandantFac.updateMwstsatzbez(mwstsatzbezDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		// Cache aktualisieren
		reloadMwstMap();
	}

	/**
	 * Mehrwertsteuersatz finden. ACHTUNG: das loest normalerweise keinen
	 * Serverzugriff aus, da die MWST-Saetze aus Performancegruenden am Client
	 * gecacht werden.
	 * 
	 * @param iId Integer
	 * @return MwstsatzDto
	 * @throws ExceptionLP
	 */
	public MwstsatzDto mwstsatzFindByPrimaryKey(Integer iId) throws ExceptionLP {
		// beim ersten Zugriff laden
		if (hmMWSTSaetze == null) {
			reloadMwstMap();
		}
		MwstsatzDto mwstsatzDto = (MwstsatzDto) hmMWSTSaetze.get(iId);
		// wenn der in der Liste fehlt
		if (mwstsatzDto == null) {
			// kann passieren, falls in der Zwischenzeit von einem anderen
			// Benutzer ein neuer angelegt wurde.
			// dann neu laden und nochmals suchen.
			reloadMwstMap();
			mwstsatzDto = (MwstsatzDto) hmMWSTSaetze.get(iId);
		}
		return mwstsatzDto;
	}

	public MwstsatzChangedInfo pruefeObMwstsatzNochAktuell(
			BelegpositionVerkaufDto vkDto,
			Timestamp belegDatum) throws Throwable {
		if(vkDto == null || vkDto.getMwstsatzIId() == null) return new MwstsatzChangedInfo();
	
		if(belegDatum == null) {
			belegDatum = HelperTimestamp.cut();
		}
	
		MwstsatzDto satzDto = mandantFac.mwstsatzZuDatumEvaluate(
				new MwstsatzId(vkDto.getMwstsatzIId()), belegDatum, LPMain.getTheClient());
		return new MwstsatzChangedInfo(vkDto.getMwstsatzIId(), satzDto);
	}
	
	public boolean pruefeObMwstsatzNochAktuell0(BelegpositionVerkaufDto vkDto, Timestamp tDatum) throws ExceptionLP {

		if (vkDto != null && vkDto.getMwstsatzIId() != null) {
			Integer satzVorgher = vkDto.getMwstsatzIId();
			MwstsatzDto satzDto = mwstsatzFindByPrimaryKey(vkDto.getMwstsatzIId());
			if(tDatum == null) {
				tDatum = HelperTimestamp.cut();
			}
			MwstsatzDto satzAktuell = mandantFac
					.mwstsatzFindZuDatum(satzDto.getIIMwstsatzbezId(), tDatum);
/*
			MwstsatzDto satzAktuell = null;
			if (tDatum == null) {
				satzAktuell = mwstsatzFindByMwstsatzbezIIdAktuellster(satzDto.getIIMwstsatzbezId());
			} else {
				satzAktuell = mandantFac.mwstsatzFindZuDatum(satzDto.getIIMwstsatzbezId(), tDatum);
			}
*/			
			vkDto.setMwstsatzIId(satzAktuell.getIId());

			if (!satzAktuell.getIId().equals(satzVorgher)) {
				return false;
			}

		}
		return true;
	}

	public MwstsatzbezDto mwstsatzbezFindByPrimaryKey(Integer iId) throws ExceptionLP {
		MwstsatzbezDto mwstsatzbezDto = null;
		try {
			mwstsatzbezDto = mandantFac.mwstsatzbezFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return mwstsatzbezDto;
	}

	public Map<?, ?> getAllMwstsatzbez(String mandantI) throws ExceptionLP {
		Map<?, ?> mwstSaetze = null;
		try {
			mwstSaetze = mandantFac.mwstsatzbezFindAllByMandant(mandantI, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return mwstSaetze;
	}

	public Map<?, ?> getAllMwstsatz(String mandantI, Timestamp tBelegdatum) throws ExceptionLP {
		Map<?, ?> mwstSaetze = null;
		try {
			if (tBelegdatum != null) {
				mwstSaetze = mandantFac.mwstsatzFindAllByMandant(mandantI, tBelegdatum, false,
						LPMain.getInstance().getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return mwstSaetze;
	}

	public DokumentenlinkbelegDto[] getDokumentenlinkbelegs(String belegartCNr, Integer belegartIId)
			throws ExceptionLP {
		try {
			return mandantFac.getDokumentenlinkbelegs(belegartCNr, belegartIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllMwstsatz(String mandantI, Timestamp tBelegdatum, boolean bInklHandeingabe)
			throws ExceptionLP {
		Map<?, ?> mwstSaetze = null;
		try {
			if (tBelegdatum != null) {
				mwstSaetze = mandantFac.mwstsatzFindAllByMandant(mandantI, tBelegdatum, bInklHandeingabe,
						LPMain.getInstance().getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return mwstSaetze;
	}

	public ModulberechtigungDto[] modulberechtigungFindByMandantCNr() throws ExceptionLP {
		ModulberechtigungDto[] berechtigungen = null;
		try {
			berechtigungen = mandantFac
					.modulberechtigungFindByMandantCNr(LPMain.getInstance().getTheClient().getMandant());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return berechtigungen;
	}

	public ZusatzfunktionberechtigungDto[] zusatzfunktionberechtigungFindByMandantCNr() throws ExceptionLP {
		ZusatzfunktionberechtigungDto[] zusatzberechtigungen = null;
		try {
			zusatzberechtigungen = mandantFac
					.zusatzfunktionberechtigungFindByMandantCNr(LPMain.getInstance().getTheClient().getMandant());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return zusatzberechtigungen;
	}

	public ZusatzfunktionberechtigungDto[] zusatzfunktionberechtigungFindByMandantCNr(String mandantCNr)
			throws ExceptionLP {
		ZusatzfunktionberechtigungDto[] zusatzberechtigungen = null;
		try {
			zusatzberechtigungen = mandantFac.zusatzfunktionberechtigungFindByMandantCNr(mandantCNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return zusatzberechtigungen;
	}

	// *** Spediteur
	// ****************************************************************
	public SpediteurDto spediteurFindByPrimaryKey(Integer iIdSpediteurI) throws ExceptionLP {
		SpediteurDto spediteurDto = null;
		try {
			spediteurDto = mandantFac.spediteurFindByPrimaryKey(iIdSpediteurI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return spediteurDto;
	}

	public Integer createSpediteur(SpediteurDto spediteurDtoI) throws ExceptionLP {
		Integer iId = null;

		// Precondition
		if (spediteurDtoI.getMandantCNr() != null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, "spediteurDtoI.getMandantCNr() != null", null);
		}

		try {
			spediteurDtoI.setMandantCNr(LPMain.getInstance().getTheClient().getMandant());
			iId = mandantFac.createSpediteur(spediteurDtoI, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public void updateSpediteur(SpediteurDto spediteurDtoI) throws ExceptionLP {
		try {
			mandantFac.updateSpediteur(spediteurDtoI, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeSpediteur(SpediteurDto spediteurDtoI) throws ExceptionLP {
		try {
			mandantFac.removeSpediteur(spediteurDtoI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	// *** Zahlungsziel
	// *************************************************************
	/**
	 * Ein bestimmtes Zahlungsziel holen.
	 * 
	 * @param iiZahlungszielI Integer
	 * @throws ExceptionLP
	 * @throws Throwable
	 * @return ZahlungszielDto
	 */
	public ZahlungszielDto zahlungszielFindByPrimaryKey(Integer iiZahlungszielI) throws ExceptionLP {
		ZahlungszielDto zahlungszielDto = null;
		try {
			zahlungszielDto = mandantFac.zahlungszielFindByPrimaryKey(iiZahlungszielI,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return zahlungszielDto;
	}

	public void updateZahlungsziel(ZahlungszielDto zahlungszielDto) throws ExceptionLP {

		try {
			mandantFac.updateZahlungsziel(zahlungszielDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer createZahlungsziel(ZahlungszielDto zahlungszielDto) throws ExceptionLP {

		// Precondition
		if (zahlungszielDto.getMandantCNr() != null) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, "zahlungszielDto.getMandantCNr() != null", null);
		}

		Integer iId = null;
		try {
			zahlungszielDto.setMandantCNr(LPMain.getInstance().getTheClient().getMandant());
			iId = mandantFac.createZahlungsziel(zahlungszielDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			zahlungszielDto.setMandantCNr(null);
			handleThrowable(ex);
		}
		return iId;
	}

	public Integer createDokumentenlink(DokumentenlinkDto dokumentenlinkDto) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = mandantFac.createDokumentenlink(dokumentenlinkDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public Integer createKostentraeger(KostentraegerDto kostentraegerDto) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = mandantFac.createKostentraeger(kostentraegerDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public byte[] getAGBs_PDF(Locale loc) throws ExceptionLP {
		byte[] pdf = null;
		try {
			pdf = mandantFac.getAGBs_PDF(loc, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return pdf;
	}

	public void updateAGBs_PDF(byte[] oPdf) throws ExceptionLP {
		try {
			mandantFac.updateAGBs_PDF(oPdf, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void removeZahlungsziel(ZahlungszielDto zahlungszielDto) throws ExceptionLP {

		try {
			mandantFac.removeZahlungsziel(zahlungszielDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	// *** Mandant
	// ******************************************************************
	public String createMandant(MandantDto mandantDto) throws ExceptionLP {
		String mandant = null;
		try {
			mandant = mandantFac.createMandant(mandantDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}
		return mandant;
	}

	public void removeMandant(String cNr) throws ExceptionLP {
		try {
			mandantFac.removeMandant(cNr);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}

	}

	public void removeMandant(MandantDto mandantDto) throws ExceptionLP {
		try {
			mandantFac.removeMandant(mandantDto);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}

	}

	public void updateMandant(MandantDto mandantDto) throws ExceptionLP {
		try {
			mandantFac.updateMandant(mandantDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}

	}

	public void updateDokumentenlink(DokumentenlinkDto dokumentenlinkDto) throws ExceptionLP {
		try {
			mandantFac.updateDokumentenlink(dokumentenlinkDto);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}

	}

	public void updateKostentraeger(KostentraegerDto dto) throws ExceptionLP {
		try {
			mandantFac.updateKostentraeger(dto);
		} catch (Throwable ex) {
			// so fange ich Exceptions vom server und verarbeite sie
			handleThrowable(ex);
		}

	}

	public void updateDokumentenlinkbeleg(String belegartCNr, Integer iBelegartId,
			DokumentenlinkbelegDto[] dokumentenlinkbelegDtos) throws ExceptionLP {
		try {
			mandantFac.updateDokumentenlinkbeleg(belegartCNr, iBelegartId, dokumentenlinkbelegDtos,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public MandantDto mandantFindByPrimaryKey(String cNr) throws ExceptionLP {
		try {
			return mandantFac.mandantFindByPrimaryKey(cNr, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public KostentraegerDto kostentraegerFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return mandantFac.kostentraegerFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MandantDto[] mandantFindAll() throws ExceptionLP {
		try {
			return mandantFac.mandantFindAll(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public DokumentenlinkDto dokumentenlinkFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return mandantFac.dokumentenlinkFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public DokumentenlinkDto[] dokumentenlinkFindByBelegartCNrMandantCNr(String belegartCNr) throws ExceptionLP {
		try {
			return mandantFac.dokumentenlinkFindByBelegartCNrMandantCNr(belegartCNr,
					LPMain.getInstance().getTheClient().getMandant());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public DokumentenlinkDto[] dokumentenlinkFindByBelegartCNrMandantCNrBPfadabsolut(String belegartCNr,
			boolean bPfadAbsolut) throws ExceptionLP {
		try {
			return mandantFac.dokumentenlinkFindByBelegartCNrMandantCNrBPfadabsolut(belegartCNr,
					LPMain.getInstance().getTheClient().getMandant(), bPfadAbsolut);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MandantDto[] mandantFindByPartnerIId(Integer partnerIId) throws ExceptionLP {
		try {
			return mandantFac.mandantFindByPartnerIIdOhneExc(partnerIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public java.sql.Date berechneZielDatumFuerBelegdatum(java.sql.Date dBelegdatum, Integer zahlungszielIId)
			throws ExceptionLP {
		try {
			return mandantFac.berechneZielDatumFuerBelegdatum(dBelegdatum, zahlungszielIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public java.sql.Date berechneSkontoTage1FuerBelegdatum(java.sql.Date dBelegdatum, Integer zahlungszielIId)
			throws ExceptionLP {
		try {
			return mandantFac.berechneSkontoTage1FuerBelegdatum(dBelegdatum, zahlungszielIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public java.sql.Date berechneSkontoTage2Belegdatum(java.sql.Date dBelegdatum, Integer zahlungszielIId)
			throws ExceptionLP {
		try {
			return mandantFac.berechneSkontoTage2Belegdatum(dBelegdatum, zahlungszielIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	private void reloadMwstMap() throws ExceptionLP {
		try {
			hmMWSTSaetze = mandantFac.mwstsatzFindAll(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Den Aktuellen MWST-Satz zu einer MWST-Bezeichnung finden.
	 * 
	 * @param mwstsatzbezIId String
	 * @return MwstsatzDto[]
	 * @throws ExceptionLP
	 */
	public MwstsatzDto mwstsatzFindByMwstsatzbezIIdAktuellster(Integer mwstsatzbezIId) throws ExceptionLP {
		MwstsatzDto mwstsatzDto = null;
		try {
			mwstsatzDto = mandantFac.mwstsatzFindByMwstsatzbezIIdAktuellster(mwstsatzbezIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return mwstsatzDto;
	}

	public boolean darfAnwenderAufModulZugreifen(String belegartFinanzbuchhaltung) {
		try {
			return mandantFac.darfAnwenderAufModulZugreifen(belegartFinanzbuchhaltung, LPMain.getTheClient());
		} catch (Throwable ex) {
			return false;
		}

	}

	public boolean darfAnwenderAufZusatzfunktionZugreifen(String zusatzfunktionCNr, String mandantCNr) {
		try {
			return mandantFac.darfAnwenderAufZusatzfunktionZugreifen(zusatzfunktionCNr, mandantCNr);
		} catch (Throwable ex) {
			return false;
		}

	}

	public MwstsatzDto mwstsatzFindZuDatum(Integer mwstsatzbezIId, Timestamp tDatum) throws ExceptionLP {
		MwstsatzDto mwstsatzDto = null;
		try {
//			mwstsatzDto = mandantFac.mwstsatzFindZuDatum(mwstsatzbezIId, tDatum);
			mwstsatzDto = mandantFac.mwstsatzZuDatumClient(
					mwstsatzbezIId, tDatum, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return mwstsatzDto;
	}

	public Map<Integer, String>  mwstsatzFindAllByMandant() throws ExceptionLP {
		try {

			return mandantFac.mwstsatzFindAllByMandant(LPMain.getTheClient().getMandant(),  LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}
	
	public MwstsatzbezDto getMwstsatzbezSteuerfrei() throws ExceptionLP {
		MwstsatzbezDto mwstsatzbezDto = null;
		try {
			mwstsatzbezDto = mandantFac.getMwstsatzbezSteuerfrei(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return mwstsatzbezDto;
	}

	public MwstsatzDto getMwstSatzVonBruttoBetragUndUst(Timestamp tBelegDatum, BigDecimal bruttoBetrag,
			BigDecimal mwstBetrag) throws ExceptionLP {
		try {
			return mandantFac.getMwstSatzVonBruttoBetragUndUst(LPMain.getTheClient().getMandant(), tBelegDatum,
					bruttoBetrag, mwstBetrag);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return null;
	}

	public MwstsatzDto getMwstSatzVonNettoBetragUndUst(Timestamp tBelegDatum, BigDecimal nettoBetrag,
			BigDecimal mwstBetrag) throws ExceptionLP {
		try {
			return mandantFac.getMwstSatzVonNettoBetragUndUst(LPMain.getTheClient().getMandant(), tBelegDatum,
					nettoBetrag, mwstBetrag);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return null;
	}

	public int istMandatsreferenzAbgelaufen(Integer zahlungszielIId, Integer kundeIId, Timestamp tBelegdatum)
			throws ExceptionLP {
		try {
			return mandantFac.istMandatsreferenzAbgelaufen(zahlungszielIId, kundeIId, tBelegdatum,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return -1;
	}

	public List<DokumentenlinkDto> getSichtbareDokumentenlinks(String belegartCnr, boolean bPfadAbsolut)
			throws ExceptionLP {
		try {
			return mandantFac.getSichtbareDokumentenlinks(belegartCnr, bPfadAbsolut, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return new ArrayList<DokumentenlinkDto>();
	}
	
	public List<MwstsatzCodeDto> getAllReversechargeartMwstsatzCodeByMwstsatzId(MwstsatzId mwstsatzId) throws ExceptionLP {
		try {
			return mandantFac.getAllReversechargeartMwstsatzCodeByMwstsatzId(mwstsatzId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return new ArrayList<MwstsatzCodeDto>();
		}
	}
	
	public void updateOrCreateMwstsatzCodes(List<MwstsatzCodeDto> mwstsatzCodeDtos) throws ExceptionLP {
		try {
			mandantFac.updateOrCreateMwstsatzCodes(mwstsatzCodeDtos, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}
	
	public boolean hatZusatzfunktionIntrastat() throws ExceptionLP {
		try {
			return mandantFac.hatZusatzfunktionIntrastat(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		
		return false;
	}
}
