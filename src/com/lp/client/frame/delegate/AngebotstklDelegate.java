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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.ejb.Agstkl;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AgstklarbeitsplanDto;
import com.lp.server.angebotstkl.service.AgstklmaterialDto;
import com.lp.server.angebotstkl.service.AgstklmengenstaffelDto;
import com.lp.server.angebotstkl.service.AgstklmengenstaffelSchnellerfassungDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.AngebotstklreportFac;
import com.lp.server.angebotstkl.service.AufschlagDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.angebotstkl.service.EkagLieferantoptimierenDto;
import com.lp.server.angebotstkl.service.EkaglieferantDto;
import com.lp.server.angebotstkl.service.EkgruppeDto;
import com.lp.server.angebotstkl.service.EkgruppelieferantDto;
import com.lp.server.angebotstkl.service.EkweblieferantDto;
import com.lp.server.angebotstkl.service.IWebpartnerDto;
import com.lp.server.angebotstkl.service.ImportLumiQuoteXlsxDto;
import com.lp.server.angebotstkl.service.PositionlieferantDto;
import com.lp.server.angebotstkl.service.WebFindChipsDto;
import com.lp.server.angebotstkl.service.WebabfragepositionDto;
import com.lp.server.angebotstkl.service.WeblieferantDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;

public class AngebotstklDelegate extends Delegate {
	private Context context;
	private AngebotstklFac stuecklisteFac;
	private AngebotstklreportFac stuecklistereportFac;

	public AngebotstklDelegate() throws Exception {
		context = new InitialContext();
		stuecklisteFac = lookupFac(context, AngebotstklFac.class);
		stuecklistereportFac = lookupFac(context, AngebotstklreportFac.class);
	}

	public boolean sindMengenstaffelnvorhandenUndPreiseFixiert(Integer iIdAngebotstkl) throws ExceptionLP {
		try {
			return stuecklisteFac.sindMengenstaffelnvorhandenUndPreiseFixiert(iIdAngebotstkl);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return true;
		}
	}

	public AufschlagDto[] aufschlagFindByBMaterial(Integer agstklIId, Short bMaterial) throws ExceptionLP {
		try {
			return stuecklisteFac.aufschlagFindByBMaterial(agstklIId, bMaterial, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LinkedHashMap<Integer, byte[]> erzeugeXLSFuerLieferanten(Integer einkaufsangebotIId, Integer lieferantIId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.erzeugeXLSFuerLieferanten(einkaufsangebotIId, lieferantIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void leseXLSEinesLieferantenEin(Integer ekaglieferantIId, byte[] xlsDatei) throws ExceptionLP {
		try {
			stuecklisteFac.leseXLSEinesLieferantenEin(ekaglieferantIId, xlsDatei, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public ArrayList<String> leseLumiquoteXLSEin(Integer einkaufsangebotIId, TreeMap<BigDecimal,LinkedHashMap<String,ArrayList<ImportLumiQuoteXlsxDto>>> hmNachMengenUndLieferantenGruppiert) throws ExceptionLP {
		try {
			return stuecklisteFac.leseLumiquoteXLSEin(einkaufsangebotIId, hmNachMengenUndLieferantenGruppiert, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public EkagLieferantoptimierenDto getEkagLieferantoptimierenDto(Integer einkaufsangebotIId, Integer iLieferzeitInKW,
			int iMenge, boolean bMindestbestellmengeBeruecksichtigen, boolean bVerpackungseinheitBeruecksichtigen,
			int sortierung, String filterArtikelnummer, String filterArtikelbezeichnung) throws ExceptionLP {
		try {
			return stuecklisteFac.getEkagLieferantoptimierenDto(einkaufsangebotIId, iLieferzeitInKW, iMenge,
					bMindestbestellmengeBeruecksichtigen, bVerpackungseinheitBeruecksichtigen, sortierung,
					filterArtikelnummer, filterArtikelbezeichnung, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createAgstkl(AgstklDto agstklDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createAgstkl(agstklDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createEkgruppe(EkgruppeDto ekgruppeDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createEkgruppe(ekgruppeDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createEkgruppelieferant(EkgruppelieferantDto ekgruppelieferantDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createEkgruppelieferant(ekgruppelieferantDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createEkaglieferant(EkaglieferantDto ekaglieferantDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createEkaglieferant(ekaglieferantDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createPositionlieferant(PositionlieferantDto positionlieferantDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createPositionlieferant(positionlieferantDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer erzeugeStuecklisteAusAgstkl(Integer agstklIId, String artikelnummerNeu) throws ExceptionLP {
		try {
			return stuecklisteFac.erzeugeStuecklisteAusAgstkl(agstklIId, artikelnummerNeu, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createWeblieferant(WeblieferantDto dto) throws ExceptionLP {
		try {
			return stuecklisteFac.createWeblieferant(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createEkweblieferant(EkweblieferantDto dto) throws ExceptionLP {
		try {
			return stuecklisteFac.createEkweblieferant(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createWebpartner(IWebpartnerDto dto) throws ExceptionLP {
		try {
			return stuecklisteFac.createWebpartner(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void lieferantenOptimierenbestellen(EkagLieferantoptimierenDto ekagLieferantoptimierenDto, int iMenge,
			boolean bZuruecknehmen) throws ExceptionLP {
		try {
			stuecklisteFac.lieferantenOptimierenbestellen(ekagLieferantoptimierenDto, iMenge, bZuruecknehmen,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public void lieferantenOptimierenArtikelnummerLFSpeichern(Integer einkaufsangebotIId) throws ExceptionLP {
		try {
			stuecklisteFac.lieferantenOptimierenArtikelnummerLFSpeichern(einkaufsangebotIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public Integer createAgstklarbeitsplan(AgstklarbeitsplanDto agstklarbeitsplanDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createAgstklarbeitsplan(agstklarbeitsplanDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createEinkaufsangebot(einkaufsangebotDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createAufschlag(AufschlagDto aufschlagDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createAufschlag(aufschlagDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createAgstklmengenstaffel(AgstklmengenstaffelDto agstklmengenstaffelDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createAgstklmengenstaffel(agstklmengenstaffelDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}
	public Integer createAgstklmengenstaffelSchnellerfassung(AgstklmengenstaffelSchnellerfassungDto agstklmengenstaffelSchnellerfassungDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createAgstklmengenstaffelSchnellerfassung(agstklmengenstaffelSchnellerfassungDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}
	public Integer createEinkaufsangebotposition(EinkaufsangebotpositionDto einkaufsangebotpositionDto)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createEinkaufsangebotposition(einkaufsangebotpositionDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createEinkaufsangebotpositions(EinkaufsangebotpositionDto[] einkaufsangebotpositionDto)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createEinkaufsangebotpositions(einkaufsangebotpositionDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	/**
	 * Eine Angebotst&uuml;ckliste drucken.
	 * 
	 * @param iIdAngebotstklI PK der Angebotstkl
	 * @return JasperPrint der Druck
	 * @throws ExceptionLP Ausnahme
	 */
	public JasperPrintLP printAngebotstkl(Integer iIdAngebotstklI) throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = stuecklistereportFac.printAngebotstkl(iIdAngebotstklI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrint;
	}

	public JasperPrintLP printAngebotstklmenenstaffel(Integer iIdAngebotstklI, Timestamp tVergleichsdatum)
			throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = stuecklistereportFac.printAngebotstklmenenstaffel(iIdAngebotstklI, tVergleichsdatum,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrint;
	}

	public JasperPrintLP printEinkaufsangebot(Integer einkaufsangebotIId, int iSortierung,
			Timestamp tGeplanterFertigungstermin) throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = stuecklistereportFac.printEinkaufsangebot(einkaufsangebotIId, iSortierung,
					tGeplanterFertigungstermin, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrint;
	}

	public JasperPrintLP printVergleich(Integer einkaufsangebotIId, int iSortierung) throws ExceptionLP {
		JasperPrintLP oPrint = null;

		try {
			oPrint = stuecklistereportFac.printVergleich(einkaufsangebotIId, iSortierung, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return oPrint;
	}

	public void removeAgstkl(AgstklDto agstklDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeAgstkl(agstklDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeWeblieferant(Integer weblieferantIId) throws ExceptionLP {
		try {
			stuecklisteFac.removeWeblieferant(weblieferantIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeWebpartner(Integer iId) throws ExceptionLP {
		try {
			stuecklisteFac.removeWebpartner(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeEkweblieferant(Integer ekweblieferantIId) throws ExceptionLP {
		try {
			stuecklisteFac.removeEkweblieferant(ekweblieferantIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeAgstklmengenstaffel(Integer agstklmengenstaffelIId) throws ExceptionLP {
		try {
			stuecklisteFac.removeAgstklmengenstaffel(agstklmengenstaffelIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}
	public void removeAgstklmengenstaffelSchnellerfassung(Integer agstklmengenstaffelSchnellerfassungIId) throws ExceptionLP {
		try {
			stuecklisteFac.removeAgstklmengenstaffelSchnellerfassung(agstklmengenstaffelSchnellerfassungIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}
	public void kopiereEkgruppeInEkaglieferant(Integer einkaufangebotId, Integer ekgruppeIId) throws ExceptionLP {
		try {
			stuecklisteFac.kopiereEkgruppeInEkaglieferant(einkaufangebotId, ekgruppeIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeAgstklarbeitsplan(AgstklarbeitsplanDto agstklarbeitsplanDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeAgstklarbeitsplan(agstklarbeitsplanDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeAufschlag(AufschlagDto aufschlagDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeAufschlag(aufschlagDto.getIId());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeEkgruppe(EkgruppeDto ekgruppeDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeEkgruppe(ekgruppeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeEkgruppelieferant(EkgruppelieferantDto ekgruppelieferantDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeEkgruppelieferant(ekgruppelieferantDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeEkaglieferant(EkaglieferantDto ekaglieferantDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeEkaglieferant(ekaglieferantDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removePositionlieferant(PositionlieferantDto positionlieferantDto) throws ExceptionLP {
		try {
			stuecklisteFac.removePositionlieferant(positionlieferantDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void preiseDerMengenstaffelnNeuKalkulieren(Integer angebotstklIId) throws ExceptionLP {
		try {
			stuecklisteFac.preiseDerMengenstaffelnNeuKalkulieren(angebotstklIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void alteVersionInDokumentenablageKopierenUndAgstklAufGeaendertStellen(Integer angebotstklIId)
			throws ExceptionLP {
		try {
			stuecklistereportFac.alteVersionInDokumentenablageKopierenUndAgstklAufGeaendertStellen(angebotstklIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeEinkaufsangebotposition(EinkaufsangebotpositionDto einkaufsangebotpositionDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.removeEinkaufsangebotposition(einkaufsangebotpositionDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateAgstkl(AgstklDto agstklDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateAgstkl(agstklDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateEkgruppe(EkgruppeDto ekgruppeDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateEkgruppe(ekgruppeDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateEkgruppelieferant(EkgruppelieferantDto ekgruppelieferantDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateEkgruppelieferant(ekgruppelieferantDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateEkaglieferant(EkaglieferantDto ekaglieferantDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateEkaglieferant(ekaglieferantDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updatePositionlieferant(PositionlieferantDto positionlieferantDto) throws ExceptionLP {
		try {
			stuecklisteFac.updatePositionlieferant(positionlieferantDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateWeblieferant(WeblieferantDto dto) throws ExceptionLP {
		try {
			stuecklisteFac.updateWeblieferant(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateEkweblieferant(EkweblieferantDto dto) throws ExceptionLP {
		try {
			stuecklisteFac.updateEkweblieferant(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateWebpartner(IWebpartnerDto dto) throws ExceptionLP {
		try {
			stuecklisteFac.updateWebpartner(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateAgstklarbeitsplan(AgstklarbeitsplanDto agstklarbeitsplanDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateAgstklarbeitsplan(agstklarbeitsplanDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateAufschlag(AufschlagDto aufschlagDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateAufschlag(aufschlagDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateAgstklmengenstaffel(AgstklmengenstaffelDto agstklmengenstaffelDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateAgstklmengenstaffel(agstklmengenstaffelDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}
	public void updateAgstklmengenstaffelSchnellerfassung(AgstklmengenstaffelSchnellerfassungDto agstklmengenstaffelSchnellerfassungDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateAgstklmengenstaffelSchnellerfassung(agstklmengenstaffelSchnellerfassungDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}
	public void kopierePositionenAusStueckliste(Integer stuecklisteIId, Integer agstklIId) throws ExceptionLP {
		try {
			stuecklisteFac.kopierePositionenAusStueckliste(stuecklisteIId, agstklIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void kopiereArbeitsplanAusStuecklisteInArbeitsplan(Integer stuecklisteIId, Integer agstklIId)
			throws ExceptionLP {
		try {
			stuecklisteFac.kopiereArbeitsplanAusStuecklisteInArbeitsplan(stuecklisteIId, agstklIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void kopiereArbeitsplanAusStuecklisteInPositionen(Integer stuecklisteIId, Integer agstklIId)
			throws ExceptionLP {
		try {
			stuecklisteFac.kopiereArbeitsplanAusStuecklisteInPositionen(stuecklisteIId, agstklIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateAgstklaufschlag(Integer agstklIId, AufschlagDto[] aufschlagDtos) throws ExceptionLP {
		try {
			stuecklisteFac.updateAgstklaufschlag(agstklIId, aufschlagDtos, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateEinkaufsangebot(einkaufsangebotDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateEinkaufsangebotposition(EinkaufsangebotpositionDto einkaufsangebotpositionDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.updateEinkaufsangebotposition(einkaufsangebotpositionDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void vertauscheEinkausangebotpositionen(Integer iIdPosition1I, Integer iIdPosition2I) throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheEinkausangebotpositionen(iIdPosition1I, iIdPosition2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheWeblieferant(Integer iIdPosition1I, Integer iIdPosition2I) throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheWeblieferant(iIdPosition1I, iIdPosition2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void verdichteEinkaufsangebotPositionen(Integer einkaufsangebotIId) throws ExceptionLP {
		try {
			stuecklisteFac.verdichteEinkaufsangebotPositionen(einkaufsangebotIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheEkweblieferant(Integer iIdPosition1I, Integer iIdPosition2I) throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheEkweblieferant(iIdPosition1I, iIdPosition2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer einkaufsangebotIId,
			int iSortierungNeuePositionI) throws ExceptionLP {
		try {
			stuecklisteFac.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(einkaufsangebotIId,
					iSortierungNeuePositionI);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AgstklDto agstklFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.agstklFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public EkgruppeDto ekgruppeFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.ekgruppeFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public EkgruppelieferantDto ekgruppelieferantFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.ekgruppelieferantFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public EkaglieferantDto ekaglieferantFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.ekaglieferantFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public EkaglieferantDto[] ekaglieferantFindByEinkaufsangebotIId(Integer einkaufsangebotIId) throws ExceptionLP {
		try {
			return stuecklisteFac.ekaglieferantFindByEinkaufsangebotIId(einkaufsangebotIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public PositionlieferantDto positionlieferantFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.positionlieferantFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public PositionlieferantDto positionlieferantFindByPrimaryKeyInZielWaehrung(Integer iId, String zielwaehrungCNr)
			throws ExceptionLP {
		try {
			return stuecklisteFac.positionlieferantFindByPrimaryKeyInZielWaehrung(iId, zielwaehrungCNr,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public AgstklDto agstklFindByCNrMandantCNrOhneExc(String cNr) throws ExceptionLP {
		try {
			return stuecklisteFac.agstklFindByCNrMandantCNrOhneExc(cNr, LPMain.getTheClient().getMandant());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public AufschlagDto aufschlagFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.aufschlagFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public WeblieferantDto weblieferantFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.weblieferantFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public List<WeblieferantDto> weblieferantFindByWebabfrageTyp(Integer webabfrageITyp, Boolean loadWithDtos)
			throws ExceptionLP {
		try {
			return stuecklisteFac.weblieferantFindByWebabfrageTyp(webabfrageITyp, loadWithDtos, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public List<WeblieferantDto> weblieferantFindByWebabfrageTyp(Integer webabfrageITyp) throws ExceptionLP {
		try {
			return stuecklisteFac.weblieferantFindByWebabfrageTyp(webabfrageITyp, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public EkweblieferantDto ekweblieferantFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.ekweblieferantFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public List<EkweblieferantDto> ekweblieferantFindByPrimaryKey(Integer iId, Integer webabfrageITyp,
			Boolean loadWithDtos) throws ExceptionLP {
		try {
			return stuecklisteFac.ekweblieferantFindByEinkaufsangebotIIdWebabfrageTyp(iId, webabfrageITyp, loadWithDtos,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public List<EkweblieferantDto> ekweblieferantFindByEinkaufsangebotIIdWebabfrageTyp(Integer einkaufsangebotIId,
			Integer webabfrageITyp, Boolean loadWithDtos) throws ExceptionLP {
		try {
			return stuecklisteFac.ekweblieferantFindByEinkaufsangebotIIdWebabfrageTyp(einkaufsangebotIId,
					webabfrageITyp, loadWithDtos, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public List<EkweblieferantDto> ekweblieferantFindByEinkaufsangebotIIdWebabfrageTyp(Integer einkaufsangebotIId,
			Integer webabfrageITyp) throws ExceptionLP {
		try {
			return stuecklisteFac.ekweblieferantFindByEinkaufsangebotIIdWebabfrageTyp(einkaufsangebotIId,
					webabfrageITyp, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public IWebpartnerDto webpartnerFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.webpartnerFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public IWebpartnerDto webpartnerFindByPrimaryKey(Integer iId, Boolean loadWithDtos) throws ExceptionLP {
		try {
			return stuecklisteFac.webpartnerFindByPrimaryKey(iId, loadWithDtos, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public List<WebFindChipsDto> webfindchipsFindByMandantCNr() throws ExceptionLP {
		try {
			return stuecklisteFac.webfindchipsFindByMandantCNr(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public List<WebFindChipsDto> webfindchipsFindByMandantCNr(Boolean loadWithDtos) throws ExceptionLP {
		try {
			return stuecklisteFac.webfindchipsFindByMandantCNr(loadWithDtos, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public List<WebFindChipsDto> webfindchipsFindByMandantCNrWithNullLieferanten() throws ExceptionLP {
		try {
			return stuecklisteFac.webfindchipsFindByMandantCNrWithNullLieferanten(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public List<WebFindChipsDto> webfindchipsFindByMandantCNrWithNullLieferanten(Boolean loadWithDtos)
			throws ExceptionLP {
		try {
			return stuecklisteFac.webfindchipsFindByMandantCNrWithNullLieferanten(loadWithDtos, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public WebFindChipsDto webfindchipsFindByDistributorId(String distributorId) throws ExceptionLP {
		try {
			return stuecklisteFac.webfindchipsFindByDistributorId(distributorId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public AgstklmengenstaffelDto agstklmengenstaffelFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.agstklmengenstaffelFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}
	public AgstklmengenstaffelSchnellerfassungDto agstklmengenstaffelSchnellerfassungFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.agstklmengenstaffelSchnellerfassungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public AgstklmengenstaffelDto[] agstklmengenstaffelFindByAgstklIId(Integer agstklIId) throws ExceptionLP {
		try {
			return stuecklisteFac.agstklmengenstaffelFindByAgstklIId(agstklIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public AgstklmengenstaffelSchnellerfassungDto[] agstklmengenstaffelSchnellerfassungFindByAgstklIId(Integer agstklIId) throws ExceptionLP {
		try {
			return stuecklisteFac.agstklmengenstaffelSchnellerfassungFindByAgstklIId(agstklIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createAngebotstuecklisteAusSchnellerfassungUndErzeugeAngebotsposition(AgstklDto agstklDto,
			ArrayList<AgstklarbeitsplanDto> agstklarbeitsplanDtos, ArrayList<AgstklpositionDto> agstklpositionDtos,
			ArrayList<AgstklmengenstaffelSchnellerfassungDto> agstklmengenstaffelDtos, ArrayList<AgstklmaterialDto> agstklmaterialDtos,
			Integer angebotIId, BigDecimal bdMenge, BigDecimal bdPreis, int iDialoghoehe,
			AngebotpositionDto angebotpositionDtoVorhanden) throws ExceptionLP {
		try {
			return stuecklisteFac.createAngebotstuecklisteAusSchnellerfassungUndErzeugeAngebotsposition(agstklDto,
					agstklarbeitsplanDtos, agstklpositionDtos, agstklmengenstaffelDtos, agstklmaterialDtos, angebotIId,
					bdMenge, bdPreis, iDialoghoehe, angebotpositionDtoVorhanden, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public AgstklarbeitsplanDto agstklarbeitsplanFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.agstklarbeitsplanFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public AgstklarbeitsplanDto[] agstklarbeitsplanFindByAgstklIId(Integer iIdAgstklI) throws ExceptionLP {
		try {
			return stuecklisteFac.agstklarbeitsplanFindByAgstklIId(iIdAgstklI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public EinkaufsangebotDto einkaufsangebotFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.einkaufsangebotFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public EinkaufsangebotpositionDto einkaufsangebotpositionFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.einkaufsangebotpositionFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public List<EinkaufsangebotpositionDto> einkaufsangebotpositionenFindByPrimaryKeys(Integer[] iIds)
			throws ExceptionLP {
		try {
			return stuecklisteFac.einkaufsangebotpositionenFindByPrimaryKeys(iIds);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}
	
	public List<Integer> einkaufsangebotpositionenIIdFindByEinkaufsangebotIId(Integer einkaufsangebotIId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.einkaufsangebotpositionenIIdFindByEinkaufsangebotIId(einkaufsangebotIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}


	public String getAngeboteDieBestimmteAngebotsstuecklisteVerwenden(Integer agstklId) throws ExceptionLP {
		try {
			return stuecklisteFac.getAngeboteDieBestimmteAngebotsstuecklisteVerwenden(agstklId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<String, String> getAllAgstklpositionsart() throws ExceptionLP {
		try {
			return stuecklisteFac.getAllAgstklpositionsart();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Liefert den kalkulatorischen Wert einer Angebotsstueckliste.
	 * 
	 * @param iIdAgstklI   PK der Agstkl
	 * @param cNrWaehrungI die gewuenschte Waehrung
	 * @return BigDecimal der kalkulatorische Wert
	 * @throws ExceptionLP
	 */
	public BigDecimal berechneKalkulatorischenAgstklwert(Integer iIdAgstklI, BigDecimal nMengenstaffel,
			String cNrWaehrungI) throws ExceptionLP {
		BigDecimal nWert = new BigDecimal(0);

		try {
			nWert = stuecklisteFac.berechneKalkulatorischenAgstklwert(iIdAgstklI, nMengenstaffel, cNrWaehrungI,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return nWert;
	}

	public BigDecimal[] berechneAgstklMaterialwertUndArbeitszeitwert(Integer iIdAgstklI) throws ExceptionLP {
		BigDecimal[] nWerte = null;

		try {
			nWerte = stuecklisteFac.berechneAgstklMaterialwertUndArbeitszeitwert(iIdAgstklI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return nWerte;
	}

	public Integer getNextArbeitsgang(Integer agstklIId) throws ExceptionLP {
		try {
			return stuecklisteFac.getNextArbeitsgang(agstklIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getWareneinsatzLief1(BigDecimal bdMenge, Integer agstklIId) throws ExceptionLP {
		try {
			return stuecklisteFac.getWareneinsatzLief1(bdMenge, agstklIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getAZeinsatzLief1(BigDecimal bdMenge, Integer agstklIId) throws ExceptionLP {
		try {
			return stuecklisteFac.getAZeinsatzLief1(bdMenge, agstklIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal[] getVKPreis(BigDecimal bdMenge, Integer agstklIId) throws ExceptionLP {
		try {
			return stuecklisteFac.getVKPreis(bdMenge, agstklIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal getVKPreisGewaehlt(BigDecimal bdMenge, Integer agstklIId) throws ExceptionLP {
		try {
			return stuecklisteFac.getVKPreisGewaehlt(bdMenge, agstklIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void kopiereAgstklArbeitsplan(Integer agstklIId_Quelle, Integer agstklIId_Ziel) throws ExceptionLP {
		try {
			stuecklisteFac.kopiereAgstklArbeitsplan(agstklIId_Quelle, agstklIId_Ziel, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public List<WebabfragepositionDto> getWebabfragepositionenByEinkaufsangebot(Integer einkaufsangebotIId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.getWebabfragepositionenByEinkaufsangebot(einkaufsangebotIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public List<WebabfragepositionDto> getWebabfragepositionenByEinkaufsangebotpositionen(List<Integer> iIds)
			throws ExceptionLP {
		try {
			return stuecklisteFac.getWebabfragepositionenByEinkaufsangebotpositionen(iIds, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public Integer einkaufsangebotWandleHandartikelUmUndFasseZusammen(List<Integer> positionIIds, String artikelCNr)
			throws ExceptionLP {
		try {
			return stuecklisteFac.einkaufsangebotWandleHandartikelUmUndFasseZusammen(positionIIds, artikelCNr,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public Integer einkaufsangebotWandleHandartikelUmUndFasseZusammen(Integer positionIId, String artikelCNr)
			throws ExceptionLP {
		try {
			return stuecklisteFac.einkaufsangebotWandleHandartikelUmUndFasseZusammen(positionIId, artikelCNr,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public ArtikelDto findeArtikelZuEinkaufsangebotpositionHandeingabe(Integer positionIId) throws ExceptionLP {
		try {
			return stuecklisteFac.findeArtikelZuEinkaufsangebotpositionHandeingabe(positionIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public PositionlieferantDto getGuenstigstenLieferant(Integer einkaufsangebotpositionIId, int iMenge)
			throws ExceptionLP {
		try {
			return stuecklisteFac.getGuenstigstenLieferant(einkaufsangebotpositionIId, iMenge, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public PositionlieferantDto getSchnellstenLieferant(Integer einkaufsangebotpositionIId) throws ExceptionLP {
		try {
			return stuecklisteFac.getSchnellstenLieferant(einkaufsangebotpositionIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public PositionlieferantDto getBestelltLieferant(Integer positionliferantIId, int iMenge) throws ExceptionLP {
		try {
			return stuecklisteFac.getBestelltLieferant(positionliferantIId, iMenge, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

}
