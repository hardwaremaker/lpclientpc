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

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.ejb.SerienbriefselektionPK;
import com.lp.server.partner.ejb.SerienbriefselektionnegativPK;
import com.lp.server.partner.service.BankFac;
import com.lp.server.partner.service.BeauskunftungDto;
import com.lp.server.partner.service.DsgvokategorieDto;
import com.lp.server.partner.service.DsgvotextDto;
import com.lp.server.partner.service.IdentifikationDto;
import com.lp.server.partner.service.KommunikationsartDto;
import com.lp.server.partner.service.KontaktartDto;
import com.lp.server.partner.service.KundeSelectCriteriaDto;
import com.lp.server.partner.service.NewslettergrundDto;
import com.lp.server.partner.service.PartnerServicesFac;
import com.lp.server.partner.service.PartnerkommentarDto;
import com.lp.server.partner.service.PartnerkommentarartDto;
import com.lp.server.partner.service.SelektionDto;
import com.lp.server.partner.service.SelektionsprDto;
import com.lp.server.partner.service.SerienbriefDto;
import com.lp.server.partner.service.SerienbriefselektionDto;
import com.lp.server.partner.service.SerienbriefselektionnegativDto;

public class PartnerServicesDelegate extends Delegate {
	private Context context;
	private PartnerServicesFac partnerServicesFac;

	public PartnerServicesDelegate() throws Exception {
		context = new InitialContext();
		partnerServicesFac = lookupFac(context, PartnerServicesFac.class);
	}

	public String createKommunikationsart(KommunikationsartDto kommunikationsartDto) throws ExceptionLP {

		String k = null;
		try {
			k = partnerServicesFac.createKommunikationsart(kommunikationsartDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return k;
	}

	public void removeKommunikationsart(String cNr) throws ExceptionLP {

		try {
			partnerServicesFac.removeKommunikationsart(cNr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeKommunikationsart(KommunikationsartDto kommunikationsartDto) throws ExceptionLP {
		try {
			partnerServicesFac.removeKommunikationsart(kommunikationsartDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateKommunikationsart(KommunikationsartDto kommunikationsartDto) throws ExceptionLP {
		try {
			partnerServicesFac.updateKommunikationsart(kommunikationsartDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateBeauskunftung(BeauskunftungDto dto) throws ExceptionLP {
		try {
			partnerServicesFac.updateBeauskunftung(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateDsgvotext(DsgvotextDto dto) throws ExceptionLP {
		try {
			partnerServicesFac.updateDsgvotext(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public KommunikationsartDto kommunikationsartFindByPrimaryKey(String cNr) throws ExceptionLP {
		KommunikationsartDto k = null;
		try {
			k = partnerServicesFac.kommunikationsartFindByPrimaryKey(cNr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return k;
	}

	public KommunikationsartDto[] kommunikationsartFindAll() throws ExceptionLP {

		KommunikationsartDto[] ak = null;
		try {
			ak = partnerServicesFac.kommunikationsartFindAll();
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return ak;
	}

	public KundeSelectCriteriaDto getSerienbriefSelektionsKriterien(Integer serienbriefIId) throws ExceptionLP {

		KundeSelectCriteriaDto m = null;
		try {
			m = partnerServicesFac.getSerienbriefSelektionsKriterien(serienbriefIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return m;
	}

	public Map<?, ?> getAllKommunikationsArten(String spracheCNr) throws ExceptionLP {

		Map<?, ?> m = null;
		try {
			m = partnerServicesFac.getAllKommunikationsArten(spracheCNr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return m;
	}

	public Map<?, ?> getAllNewslettergrund() throws ExceptionLP {

		Map<?, ?> m = null;
		try {
			m = partnerServicesFac.getAllNewslettergrund(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return m;
	}
	public Map<?, ?> getAllKontaktart() throws ExceptionLP {

		Map<?, ?> m = null;
		try {
			m = partnerServicesFac.getAllKontaktart(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return m;
	}
	public Map<?, ?> getAllDSGVOKategorie() throws ExceptionLP {

		Map<?, ?> m = null;
		try {
			m = partnerServicesFac.getAllDSGVOKategorie(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return m;
	}

	public String getBriefanredeFuerBeleg(Integer ansprechpartnerIId, Integer partnerIId) throws Exception {
		try {
			return getBriefanredeFuerBeleg(ansprechpartnerIId, partnerIId, LPMain.getTheClient().getLocUi());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getBriefanredeFuerBeleg(Integer ansprechpartnerIId, Integer partnerIId, Locale locale)
			throws Exception {
		try {
			if (locale == null)
				locale = LPMain.getTheClient().getLocUi();
			return partnerServicesFac.getBriefanredeFuerBeleg(ansprechpartnerIId, partnerIId, locale,
					LPMain.getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	// **************************************************************************
	// ****
	public Integer createSelektion(SelektionDto selektionDto) throws ExceptionLP {
		Integer iId = null;
		try {
			selektionDto.setMandantCNr(LPMain.getTheClient().getMandant());
			iId = partnerServicesFac.createSelektion(selektionDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public Integer createIdentifikation(IdentifikationDto dto) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = partnerServicesFac.createIdentifikation(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public Integer createKontaktart(KontaktartDto kontaktartDto) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = partnerServicesFac.createKontaktart(kontaktartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public void removeSelektion(Integer iId) throws ExceptionLP {
		try {
			partnerServicesFac.removeSelektion(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeKontaktart(KontaktartDto kontaktartDto) throws ExceptionLP {
		try {
			partnerServicesFac.removeKontaktart(kontaktartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeIdentifikation(IdentifikationDto dto) throws ExceptionLP {
		try {
			partnerServicesFac.removeIdentifikation(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeBeauskunftung(BeauskunftungDto dto) throws ExceptionLP {
		try {
			partnerServicesFac.removeBeauskunftung(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeDsgvotext(DsgvotextDto dto) throws ExceptionLP {
		try {
			partnerServicesFac.removeDsgvotext(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeSelektion(SelektionDto selektionDtoI) throws ExceptionLP {
		try {
			partnerServicesFac.removeSelektion(selektionDtoI.getIId(), LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeDsgvokategorie(DsgvokategorieDto dto) throws ExceptionLP {
		try {
			partnerServicesFac.removeDsgvokategorie(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateKontaktart(KontaktartDto kontaktartDto) throws ExceptionLP {
		try {
			partnerServicesFac.updateKontaktart(kontaktartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePartnerbild(Integer partnerIId, byte[] bild) throws ExceptionLP {
		try {
			partnerServicesFac.updatePartnerbild(partnerIId, bild);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateIdentifikation(IdentifikationDto dto) throws ExceptionLP {
		try {
			partnerServicesFac.updateIdentifikation(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSelektion(SelektionDto selektionDto) throws ExceptionLP {
		try {
			partnerServicesFac.updateSelektion(selektionDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void vertauscheDsgvotext(Integer id1, Integer id2) throws ExceptionLP {
		try {
			partnerServicesFac.vertauscheDsgvotext(id1, id2);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateDsgvokategorie(DsgvokategorieDto dto) throws ExceptionLP {
		try {
			partnerServicesFac.updateDsgvokategorie(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSerienbriefMailtext(SerienbriefDto serienbriefDto) throws ExceptionLP {
		try {
			partnerServicesFac.updateSerienbriefMailtext(serienbriefDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public SelektionDto selektionFindByPrimaryKey(Integer iId) throws ExceptionLP {
		SelektionDto selektionDto = null;
		try {
			selektionDto = partnerServicesFac.selektionFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return selektionDto;
	}

	public KontaktartDto kontaktartFindByPrimaryKey(Integer iId) throws ExceptionLP {
		KontaktartDto kontaktartDto = null;
		try {
			kontaktartDto = partnerServicesFac.kontaktartFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return kontaktartDto;
	}

	public DsgvokategorieDto dsgvokategorieFindByPrimaryKey(Integer iId) throws ExceptionLP {
		DsgvokategorieDto dto = null;
		try {
			dto = partnerServicesFac.dsgvokategorieFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return dto;
	}

	public KontaktartDto getVorschlagFuerWiedervorlageAusSerienbrief() throws ExceptionLP {
		KontaktartDto kontaktartDto = null;
		try {
			kontaktartDto = partnerServicesFac.getVorschlagFuerWiedervorlageAusSerienbrief();
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return kontaktartDto;
	}

	public BeauskunftungDto beauskunftungFindByPrimaryKey(Integer iId) throws ExceptionLP {
		BeauskunftungDto dto = null;
		try {
			dto = partnerServicesFac.beauskunftungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return dto;
	}

	public DsgvotextDto dsgvotextFindByPrimaryKey(Integer iId) throws ExceptionLP {
		DsgvotextDto dto = null;
		try {
			dto = partnerServicesFac.dsgvotextFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return dto;
	}

	public IdentifikationDto identifikationFindByPrimaryKey(Integer iId) throws ExceptionLP {
		IdentifikationDto dto = null;
		try {
			dto = partnerServicesFac.identifikationFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return dto;
	}

	// **************************************************************************
	// ****
	public void createSelektionspr(SelektionsprDto selektionsprDto) throws ExceptionLP {
		try {
			partnerServicesFac.createSelektionspr(selektionsprDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeSelektionspr(Integer selektionIId, String localeCNr) throws ExceptionLP {
		try {
			partnerServicesFac.removeSelektionspr(selektionIId, localeCNr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSelektionspr(SelektionsprDto selektionsprDto) throws ExceptionLP {
		try {
			partnerServicesFac.updateSelektionspr(selektionsprDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public SelektionsprDto selektionsprFindByPrimaryKey(Integer selektionIId, String localeCNr) throws ExceptionLP {
		SelektionsprDto selektionsprDto = null;
		try {
			selektionsprDto = partnerServicesFac.selektionsprFindByPrimaryKey(selektionIId, localeCNr,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return selektionsprDto;
	}

	// **************************************************************************
	// ****
	public Integer createSerienbrief(SerienbriefDto serienbriefDto) throws Exception {
		Integer iId = null;
		try {
			iId = partnerServicesFac.createSerienbrief(serienbriefDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public Integer createNewslettergrund(NewslettergrundDto dto) throws Exception {
		Integer iId = null;
		try {
			iId = partnerServicesFac.createNewslettergrund(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public Integer createBeauskunftung(BeauskunftungDto dto) throws Exception {
		Integer iId = null;
		try {
			iId = partnerServicesFac.createBeauskunftung(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public Integer createDsgvotext(DsgvotextDto dto) throws Exception {
		Integer iId = null;
		try {
			iId = partnerServicesFac.createDsgvotext(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public Integer createPartnerkommentarart(PartnerkommentarartDto artDto) throws Exception {
		Integer iId = null;
		try {
			iId = partnerServicesFac.createPartnerkommentarart(artDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public Integer createPartnerkommentar(PartnerkommentarDto dto) throws Exception {
		Integer iId = null;
		try {
			iId = partnerServicesFac.createPartnerkommentar(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public void removeSerienbrief(Integer iId) throws Exception {
		try {
			partnerServicesFac.removeSerienbrief(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeNewslettergrund(NewslettergrundDto dto) throws Exception {
		try {
			partnerServicesFac.removeNewslettergrund(dto.getIId(), LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removePartnerkommentarart(PartnerkommentarartDto artDto) throws Exception {
		try {
			partnerServicesFac.removePartnerkommentarart(artDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removePartnerkommentar(PartnerkommentarDto dto) throws Exception {
		try {
			partnerServicesFac.removePartnerkommentar(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSerienbrief(SerienbriefDto serienbriefDto) throws Exception {
		try {
			partnerServicesFac.updateSerienbrief(serienbriefDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateNewslettergrund(NewslettergrundDto dtoI) throws Exception {
		try {
			partnerServicesFac.updateNewslettergrund(dtoI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePartnerkommentarart(PartnerkommentarartDto artDto) throws Exception {
		try {
			partnerServicesFac.updatePartnerkommentarart(artDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updatePartnerkommentar(PartnerkommentarDto dto) throws Exception {
		try {
			partnerServicesFac.updatePartnerkommentar(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public SerienbriefDto serienbriefFindByPrimaryKey(Integer iId) throws Exception {
		SerienbriefDto serienbriefDto = null;
		try {
			serienbriefDto = partnerServicesFac.serienbriefFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return serienbriefDto;
	}

	public NewslettergrundDto newslettergrundFindByPrimaryKey(Integer iId) throws Exception {
		NewslettergrundDto newslettergrundDto = null;
		try {
			newslettergrundDto = partnerServicesFac.newslettergrundFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return newslettergrundDto;
	}

	public PartnerkommentarartDto partnerkommentarartFindByPrimaryKey(Integer iId) throws Exception {
		PartnerkommentarartDto partnerkommentarartDto = null;
		try {
			partnerkommentarartDto = partnerServicesFac.partnerkommentarartFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return partnerkommentarartDto;
	}

	public PartnerkommentarDto partnerkommentarFindByPrimaryKey(Integer iId) throws Exception {
		PartnerkommentarDto partnerkommentarDto = null;
		try {
			partnerkommentarDto = partnerServicesFac.partnerkommentarFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return partnerkommentarDto;
	}

	public byte[] partnerbilFindByPrimaryKey(Integer partneriId) throws Exception {
		byte[] bild = null;
		try {
			bild = partnerServicesFac.partnerbildFindByPartnerIId(partneriId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return bild;
	}

	// **************************************************************************
	// ****
	public SerienbriefselektionPK createSerienbriefselektion(SerienbriefselektionDto serienbriefselektionDto)
			throws Exception {

		SerienbriefselektionPK serienbriefselektionPK = null;
		try {
			serienbriefselektionPK = partnerServicesFac.createSerienbriefselektion(serienbriefselektionDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return serienbriefselektionPK;
	}

	public void removeSerienbriefselektion(Integer serienbriefIId, Integer selektionIId) throws Exception {
		try {
			partnerServicesFac.removeSerienbriefselektion(serienbriefIId, selektionIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSerienbriefselektion(SerienbriefselektionDto serienbriefselektionDto) throws Exception {
		try {
			partnerServicesFac.updateSerienbriefselektion(serienbriefselektionDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public SerienbriefselektionDto serienbriefselektionFindByPrimaryKey(Integer serienbriefIId, Integer selektionIId)
			throws Exception {
		SerienbriefselektionDto serienbriefselektionDto = null;
		try {
			serienbriefselektionDto = partnerServicesFac.serienbriefselektionFindByPrimaryKey(serienbriefIId,
					selektionIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return serienbriefselektionDto;
	}

	public SerienbriefselektionnegativPK createSerienbriefselektionnegativ(
			SerienbriefselektionnegativDto serienbriefselektionnegativDto) throws Exception {

		SerienbriefselektionnegativPK serienbriefselektionnegativPK = null;
		try {
			serienbriefselektionnegativPK = partnerServicesFac
					.createSerienbriefselektionnegativ(serienbriefselektionnegativDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return serienbriefselektionnegativPK;
	}

	public void removeSerienbriefselektionnegativ(Integer serienbriefIId, Integer selektionIId) throws Exception {
		try {
			partnerServicesFac.removeSerienbriefselektionnegativ(serienbriefIId, selektionIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSerienbriefselektionnegativ(SerienbriefselektionnegativDto serienbriefselektionnegativDto)
			throws Exception {
		try {
			partnerServicesFac.updateSerienbriefselektionnegativ(serienbriefselektionnegativDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public SerienbriefselektionnegativDto serienbriefselektionnegativFindByPrimaryKey(Integer serienbriefIId,
			Integer selektionIId) throws Exception {
		SerienbriefselektionnegativDto serienbriefselektionnegativDto = null;
		try {
			serienbriefselektionnegativDto = partnerServicesFac
					.serienbriefselektionnegativFindByPrimaryKey(serienbriefIId, selektionIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return serienbriefselektionnegativDto;
	}

	public ArrayList<PartnerkommentarDto> getPartnerhinweise(Integer partnerIId, boolean bKunde, String belegartCNr)
			throws Exception {
		ArrayList<PartnerkommentarDto> al = null;
		try {
			al = partnerServicesFac.getPartnerhinweise(partnerIId, bKunde, belegartCNr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return al;
	}

	public ArrayList<byte[]> getPartnerkommentarBilderUndPDFAlsBilderUmgewandelt(Integer partnerIId, boolean bKunde,
			String belegartCNr, Integer iArt) throws Exception {
		ArrayList<byte[]> s = null;
		try {
			s = partnerServicesFac.getPartnerkommentarBilderUndPDFAlsBilderUmgewandelt(partnerIId, bKunde, belegartCNr,
					iArt, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return s;
	}

}
