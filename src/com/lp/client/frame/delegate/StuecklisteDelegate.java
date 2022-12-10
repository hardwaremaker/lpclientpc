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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.stueckliste.service.AlternativmaschineDto;
import com.lp.server.stueckliste.service.ApkommentarDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.stueckliste.service.KommentarimportDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.PosersatzDto;
import com.lp.server.stueckliste.service.PruefartDto;
import com.lp.server.stueckliste.service.PruefkombinationDto;
import com.lp.server.stueckliste.service.StklagerentnahmeDto;
import com.lp.server.stueckliste.service.StklparameterDto;
import com.lp.server.stueckliste.service.StklpruefplanDto;
import com.lp.server.stueckliste.service.StrukturierterImportDto;
import com.lp.server.stueckliste.service.StrukturierterImportSiemensNXDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteScriptartDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftDto;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftartDto;
import com.lp.server.stueckliste.service.StuecklisteimportFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.ImportErroInfo;
import com.lp.server.system.service.IntelligenterStklImportFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.service.StklImportSpezifikation;

@SuppressWarnings("static-access")
public class StuecklisteDelegate extends Delegate {
	private Context context;
	private StuecklisteFac stuecklisteFac;
	private StuecklisteimportFac stuecklisteimportFac;
	private IntelligenterStklImportFac iStklImportFac;

	public StuecklisteDelegate() throws Exception {
		context = new InitialContext();

		stuecklisteFac = lookupFac(context, StuecklisteFac.class);
		stuecklisteimportFac = lookupFac(context, StuecklisteimportFac.class);
		iStklImportFac = lookupFac(context, IntelligenterStklImportFac.class);
	}

	public String pruefeUndImportiereArbeitsplanXLS(byte[] xlsDatei, String einheitStueckRuestZeit,
			boolean bImportierenWennKeinFehler, boolean bVorhandenePositionenLoeschen) throws ExceptionLP {
		try {
			return stuecklisteimportFac.pruefeUndImportiereArbeitsplanXLS(xlsDatei, einheitStueckRuestZeit,
					bImportierenWennKeinFehler, bVorhandenePositionenLoeschen, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ImportErroInfo pruefeUndImportiereCreoXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			boolean bVorhandenePositionenLoeschen) throws ExceptionLP {
		try {
			return stuecklisteimportFac.pruefeUndImportiereCreoXLS(xlsDatei, bImportierenWennKeinFehler,
					bVorhandenePositionenLoeschen, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String pruefeUndImportiereProFirst(Integer stuecklisteIId, String kundeKbez) throws ExceptionLP {
		try {
			return stuecklisteimportFac.pruefeUndImportiereProFirst(stuecklisteIId, kundeKbez,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String pruefeUndImportiereProFirst(Integer stuecklisteIId, String kundeKbez, boolean bNurVKPreisUpdaten)
			throws ExceptionLP {
		try {
			return stuecklisteimportFac.pruefeUndImportiereProFirst(stuecklisteIId, kundeKbez, bNurVKPreisUpdaten,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ImportErroInfo pruefeUndImportiereMaterialXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			boolean bVorhandenePositionenLoeschen, String einheitStueckRuestZeit) throws ExceptionLP {
		try {
			return stuecklisteimportFac.pruefeUndImportiereMaterialXLS(xlsDatei, bImportierenWennKeinFehler,
					bVorhandenePositionenLoeschen, einheitStueckRuestZeit, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String pruefeUndImportierePruefkombinationXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler)
			throws ExceptionLP {
		try {
			return stuecklisteimportFac.pruefeUndImportierePruefkombinationXLS(xlsDatei, bImportierenWennKeinFehler,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String pruefeUndImportierePruefplanXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler,
			boolean bVorhandenePositionenLoeschen) throws ExceptionLP {
		try {
			return stuecklisteimportFac.pruefeUndImportierePruefplanXLS(xlsDatei, bImportierenWennKeinFehler,
					bVorhandenePositionenLoeschen, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void ignoriereKundeBeiProfirstImport(String kbez) throws ExceptionLP {
		try {
			stuecklisteimportFac.ignoriereKundeBeiProfirstImport(kbez);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer createMontageart(MontageartDto montageartDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createMontageart(montageartDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createApkommentar(ApkommentarDto dto) throws ExceptionLP {
		try {
			return stuecklisteFac.createApkommentar(dto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createPruefkombination(PruefkombinationDto dto) throws ExceptionLP {
		try {
			return stuecklisteFac.createPruefkombination(dto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createStklpruefplan(StklpruefplanDto dto) throws ExceptionLP {
		try {
			return stuecklisteFac.createStklpruefplan(dto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createStklparameter(StklparameterDto dto) throws ExceptionLP {
		try {
			return stuecklisteFac.createStklparameter(dto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createProduktstuecklisteAnhandFormelstueckliste(Integer stuecklisteIId_Formelstueckliste,
			BigDecimal losgroesse, Map<String, Object> konfigurationsWerte, Integer kundeIId) throws ExceptionLP {
		try {
			return stuecklisteFac.createProduktstuecklisteAnhandFormelstueckliste(stuecklisteIId_Formelstueckliste,
					losgroesse, konfigurationsWerte, kundeIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createProduktstuecklisteAnhandFormelstuecklisteUndErzeugeAngebot(Integer angebotIId,
			Integer stuecklisteIId_Formelstueckliste, BigDecimal losgroesse, Integer kundeIId,
			Map<String, Object> konfigurationsWerte) throws ExceptionLP {
		try {
			return stuecklisteFac.createProduktstuecklisteAnhandFormelstuecklisteUndErzeugeAngebot(angebotIId,
					stuecklisteIId_Formelstueckliste, losgroesse, kundeIId, konfigurationsWerte,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createPosersatz(PosersatzDto posersatzDtio) throws ExceptionLP {
		try {
			return stuecklisteFac.createPosersatz(posersatzDtio, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createAlternativmaschine(AlternativmaschineDto alternativmaschineDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createAlternativmaschine(alternativmaschineDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createKommentarimport(KommentarimportDto kommentarimportDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createKommentarimport(kommentarimportDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateKommentarimport(KommentarimportDto kommentarimportDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateKommentarimport(kommentarimportDto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public Integer createFertigungsgruppe(FertigungsgruppeDto fertigungsgruppe) throws ExceptionLP {
		try {
			return stuecklisteFac.createFertigungsgruppe(fertigungsgruppe, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createStueckliste(StuecklisteDto stuecklisteDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createStueckliste(stuecklisteDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void removeMontageart(MontageartDto montageartDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeMontageart(montageartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removePosersatz(PosersatzDto posersatzDto) throws ExceptionLP {
		try {
			stuecklisteFac.removePosersatz(posersatzDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeAlternativmaschine(AlternativmaschineDto dto) throws ExceptionLP {
		try {
			stuecklisteFac.removeAlternativmaschine(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeKommentarimport(KommentarimportDto dto) throws ExceptionLP {
		try {
			stuecklisteFac.removeKommentarimport(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeFertigungsgruppe(FertigungsgruppeDto fertigungsgruppeDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeFertigungsgruppe(fertigungsgruppeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer stueckliste,
			int iSortierungNeuePositionI) throws ExceptionLP {
		try {
			stuecklisteFac.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(stueckliste,
					iSortierungNeuePositionI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void sortiereNachArtikelnummer(Integer stuecklisteIId) throws ExceptionLP {
		try {
			stuecklisteFac.sortiereNachArtikelnummer(stuecklisteIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void arbeitsgaengeNeuNummerieren(Integer stuecklisteIId) throws ExceptionLP {
		try {
			stuecklisteFac.arbeitsgaengeNeuNummerieren(stuecklisteIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public ArrayList<?> importiereStuecklistenstruktur(ArrayList<StrukturierterImportDto> struktur,
			boolean bAnfragevorschlagErzeugen, java.sql.Timestamp tLieferterminfuerAnfrageVorschlag)
			throws ExceptionLP {
		try {
			return stuecklisteFac.importiereStuecklistenstruktur(struktur, null, LPMain.getInstance().getTheClient(),
					bAnfragevorschlagErzeugen, tLieferterminfuerAnfrageVorschlag);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public TreeMap<String, Integer> holeAlleWurzelstuecklisten(boolean bMitVersteckten) throws ExceptionLP {
		try {
			return stuecklisteFac.holeAlleWurzelstuecklisten(bMitVersteckten, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public TreeMap<String, Integer> holeNaechsteEbene(Integer stuecklisteIId, boolean bMitVersteckten)
			throws ExceptionLP {
		try {
			return stuecklisteFac.holeNaechsteEbene(stuecklisteIId, bMitVersteckten,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<ArtikelDto> importiereStuecklistenstrukturSiemensNX(
			ArrayList<StrukturierterImportSiemensNXDto> stueckliste,
			ArrayList<StrukturierterImportSiemensNXDto> listeFlach, Integer stuecklisteIIdKopf) throws ExceptionLP {
		try {
			return stuecklisteFac.importiereStuecklistenstrukturSiemensNX(stueckliste, listeFlach, stuecklisteIIdKopf,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void kopiereStuecklistenPositionen(Integer stuecklisteIId_Quelle, Integer stuecklisteIId_Ziel)
			throws ExceptionLP {
		try {
			stuecklisteFac.kopiereStuecklistenPositionen(stuecklisteIId_Quelle, stuecklisteIId_Ziel,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void kopiereStuecklisteArbeitsplan(Integer stuecklisteIId_Quelle, Integer stuecklisteIId_Ziel)
			throws ExceptionLP {
		try {
			stuecklisteFac.kopiereStuecklisteArbeitsplan(stuecklisteIId_Quelle, stuecklisteIId_Ziel,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void kopiereAusAgstkl(Integer agstklIId, Integer stuecklisteIId) throws ExceptionLP {
		try {
			stuecklisteFac.kopiereAusAgstkl(agstklIId, stuecklisteIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateMontageart(MontageartDto montageartDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateMontageart(montageartDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updatePosersatz(PosersatzDto posersatzDto) throws ExceptionLP {
		try {
			stuecklisteFac.updatePosersatz(posersatzDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateAlternativmaschine(AlternativmaschineDto alternativmaschineDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateAlternativmaschine(alternativmaschineDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateFertigungsgruppe(FertigungsgruppeDto fertigungsgruppeDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateFertigungsgruppe(fertigungsgruppeDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void vertauscheStuecklisteposition(Integer iIdPosition1I, Integer iIdPosition2I) throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheStuecklisteposition(iIdPosition1I, iIdPosition2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheAlternativmaschine(Integer iIdPosition1I, Integer iIdPosition2I) throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheAlternativmaschine(iIdPosition1I, iIdPosition2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheMontageart(Integer iIdMontageart1I, Integer iIdMontageart2I) throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheMontageart(iIdMontageart1I, iIdMontageart2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheStklpruefplan(Integer iId1, Integer iId2) throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheStklpruefplan(iId1, iId2);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauschePosersatz(Integer iIdPosersatz1I, Integer iIdPosersatz2I) throws ExceptionLP {
		try {
			stuecklisteFac.vertauschePosersatz(iIdPosersatz1I, iIdPosersatz2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheStklparameter(Integer iId1I, Integer iId2I) throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheStklparameter(iId1I, iId2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheStuecklisteeigenschaftart(Integer iIdStuecklisteeigenschaftart1I,
			Integer iIdStuecklisteeigenschaftart2I) throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheStuecklisteeigenschaftart(iIdStuecklisteeigenschaftart1I,
					iIdStuecklisteeigenschaftart2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public MontageartDto montageartFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.montageartFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public PosersatzDto posersatzFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.posersatzFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public PosersatzDto[] posersatzFindByStuecklistepositionIId(Integer stuecklistepositionIId) throws ExceptionLP {
		try {
			return stuecklisteFac.posersatzFindByStuecklistepositionIId(stuecklistepositionIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public AlternativmaschineDto alternativmaschineFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.alternativmaschineFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public FertigungsgruppeDto fertigungsgruppeFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.fertigungsgruppeFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public FertigungsgruppeDto fertigungsgruppeFindByMandantCNrCBezOhneExc(String mandantCNr, String cBez)
			throws ExceptionLP {
		try {
			return stuecklisteFac.fertigungsgruppeFindByMandantCNrCBezOhneExc(mandantCNr, cBez);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public KommentarimportDto kommentarimportFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.kommentarimportFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public ApkommentarDto apkommentarFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.apkommentarFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public FertigungsgruppeDto[] fertigungsgruppeFindByMandantCNr() throws ExceptionLP {
		try {
			return stuecklisteFac.fertigungsgruppeFindByMandantCNr(LPMain.getInstance().getTheClient().getMandant(),
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StuecklisteDto stuecklisteFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklisteFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StuecklisteDto stuecklisteFindByPrimaryKeyOhneExc(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklisteFindByPrimaryKeyOhneExc(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StklparameterDto stklparameterFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.stklparameterFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StklparameterDto[] stklparameterFindByStuecklisteIId(Integer stuecklisteId) throws ExceptionLP {
		try {
			return stuecklisteFac.stklparameterFindByStuecklisteIId(stuecklisteId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StuecklisteDto[] stuecklisteFindByArtikelIId(Integer artikelIId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklisteFindByArtikelIId(artikelIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StuecklisteDto[] unterstuecklistenFindByStuecklisteIId(Integer stuecklisteIId) throws ExceptionLP {
		try {
			return stuecklisteFac.unterstuecklistenFindByStuecklisteIId(stuecklisteIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StuecklistepositionDto stuecklistepositionFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklistepositionFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}
	public StuecklistepositionDto[] stuecklistepositionFindByArtikelIId(Integer artikelIId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklistepositionFindByArtikelIId(artikelIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}
	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIId(Integer stuecklisteIId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklistepositionFindByStuecklisteIId(stuecklisteIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIIdAllData(Integer stuecklisteIId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklistepositionFindByStuecklisteIIdAllData(stuecklisteIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public StuecklistearbeitsplanDto[] stuecklistearbeitsplanFindByStuecklisteIId(Integer stuecklisteIId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklistearbeitsplanFindByStuecklisteIId(stuecklisteIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public MontageartDto montageartFindByMandantCNrCBez(String cBez) throws Throwable {
		return stuecklisteFac.montageartFindByMandantCNrCBez(cBez, LPMain.getInstance().getTheClient());
	}

	public MontageartDto[] montageartFindByMandantCNr() throws Throwable {
		return stuecklisteFac.montageartFindByMandantCNr(LPMain.getInstance().getTheClient());
	}

	public Integer createStuecklistearbeitsplan(StuecklistearbeitsplanDto stuecklistearbeitsplanDto)
			throws ExceptionLP {

		try {
			return stuecklisteFac.createStuecklistearbeitsplan(stuecklistearbeitsplanDto,
					LPMain.getInstance().getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createStuecklistearbeitsplans(StuecklistearbeitsplanDto[] stuecklistearbeitsplanDtos)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createStuecklistearbeitsplans(stuecklistearbeitsplanDtos,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal berechneZielmenge(Integer stuecklistepositionIId, BigDecimal nLosgroesse) throws ExceptionLP {
		try {
			return stuecklisteFac.berechneZielmenge(stuecklistepositionIId, LPMain.getInstance().getTheClient(),
					nLosgroesse);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BigDecimal berechneZielmenge(Integer stuecklistepositionIId) throws ExceptionLP {

		try {
			return stuecklisteFac.berechneZielmenge(stuecklistepositionIId, LPMain.getInstance().getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void removeStuecklistearbeitsplan(StuecklistearbeitsplanDto stuecklistearbeitsplanDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeStuecklistearbeitsplan(stuecklistearbeitsplanDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateStuecklistearbeitsplan(StuecklistearbeitsplanDto stuecklistearbeitsplanDto) throws ExceptionLP {

		try {
			stuecklisteFac.updateStuecklistearbeitsplan(stuecklistearbeitsplanDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateStueckliste(StuecklisteDto stuecklisteDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateStueckliste(stuecklisteDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateStklparameter(StklparameterDto stuecklisteDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateStklparameter(stuecklisteDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updatePruefkombination(PruefkombinationDto dto) throws ExceptionLP {
		try {
			stuecklisteFac.updatePruefkombination(dto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateStklpruefplan(StklpruefplanDto dto) throws ExceptionLP {
		try {
			stuecklisteFac.updateStklpruefplan(dto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updatePruefartspr(PruefartDto dto) throws ExceptionLP {
		try {
			stuecklisteFac.updatePruefartspr(dto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateApkommentar(ApkommentarDto dto) throws ExceptionLP {
		try {
			stuecklisteFac.updateApkommentar(dto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void removeApkommentar(ApkommentarDto dto) throws ExceptionLP {
		try {
			stuecklisteFac.removeApkommentar(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void removePruefkombination(Integer iId) throws ExceptionLP {
		try {
			stuecklisteFac.removePruefkombination(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void removeStklpruefplan(Integer iId) throws ExceptionLP {
		try {
			stuecklisteFac.removeStklpruefplan(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateStuecklisteKommentar(StuecklisteDto stuecklisteDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateStuecklisteKommentar(stuecklisteDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public StuecklistearbeitsplanDto stuecklistearbeitsplanFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {

			return stuecklisteFac.stuecklistearbeitsplanFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer getNextArbeitsgang(Integer stuecklisteId) throws ExceptionLP {
		try {
			return stuecklisteFac.getNextArbeitsgang(stuecklisteId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void artikelErsetzen(Integer artikelIIdVon, Integer artikelIIdDurch) throws ExceptionLP {
		try {
			stuecklisteFac.artikelErsetzen(artikelIIdVon, artikelIIdDurch, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void bevorzugtenArtikelEintragen(Integer artikelIId_Bevorzugt) throws ExceptionLP {
		try {
			stuecklisteFac.bevorzugtenArtikelEintragen(artikelIId_Bevorzugt, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void maschineErsetzen(Integer maschineIIdVon, Integer maschineIIdDurch) throws ExceptionLP {
		try {
			stuecklisteFac.maschineErsetzen(maschineIIdVon, maschineIIdDurch, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public Integer getNextFertigungsgruppe() throws ExceptionLP {
		try {
			return stuecklisteFac.getNextFertigungsgruppe(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void sollzeitenAnhandLosistzeitenAktualisieren(Integer stuecklisteIId,java.sql.Date tVon, java.sql.Date tBis) throws ExceptionLP {
		try {
			stuecklisteFac.sollzeitenAnhandLosistzeitenAktualisieren(stuecklisteIId,tVon,tBis,LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}
	
	
	public Map<?, ?> getAllStuecklisteart() throws ExceptionLP {
		try {
			return stuecklisteFac.getAllStuecklisteart(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllPruefart() throws ExceptionLP {
		try {
			return stuecklisteFac.getAllPruefart(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public boolean wirdKundeVonProFirstIgnoriert(String kbez) throws ExceptionLP {
		try {
			return stuecklisteimportFac.wirdKundeVonProFirstIgnoriert(kbez);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return false;
		}
	}

	public Integer wirdArtikelInFreigegebenerStuecklisteVerwendet(Integer artikelIId,
			boolean freigabeDerStuecklistenZuruecknehmen) throws ExceptionLP {
		try {
			return stuecklisteFac.wirdArtikelInFreigegebenerStuecklisteVerwendet(artikelIId,
					freigabeDerStuecklistenZuruecknehmen, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeProFirstIgnore(String kbez) throws ExceptionLP {
		try {
			stuecklisteimportFac.removeProFirstIgnore(kbez);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public HashMap<Integer, String> getAlleStuecklistenIIdsFuerVerwendungsnachweis(Integer artikelIId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.getAlleStuecklistenIIdsFuerVerwendungsnachweis(artikelIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllFertigungsgrupe() throws ExceptionLP {
		try {
			return stuecklisteFac.getAllFertigungsgrupe(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getEingeschraenkteFertigungsgruppen() throws ExceptionLP {
		try {
			return stuecklisteFac.getEingeschraenkteFertigungsgruppen(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createStuecklisteposition(StuecklistepositionDto stuecklistepositionDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createStuecklisteposition(stuecklistepositionDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createStuecklistepositions(StuecklistepositionDto[] stuecklistepositionDtos) throws ExceptionLP {
		try {
			return stuecklisteFac.createStuecklistepositions(stuecklistepositionDtos,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void removeStuecklisteposition(StuecklistepositionDto stuecklistepositionDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeStuecklisteposition(stuecklistepositionDto, LPMain.getInstance().getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void kopiereParameterEinerStueckliste(Integer stuecklisteIIdQuelle, Integer stuecklisteIIdZiel)
			throws ExceptionLP {

		try {
			stuecklisteFac.kopiereParameterEinerStueckliste(stuecklisteIIdQuelle, stuecklisteIIdZiel, null,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void wechsleMandantEinerSteckliste(Integer stklIId, String mandantCNrNeu) throws ExceptionLP {
		try {
			stuecklisteFac.wechsleMandantEinerSteckliste(stklIId, mandantCNrNeu, LPMain.getInstance().getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void removeAlleStuecklistenpositionen(Integer stuecklisteIId) throws ExceptionLP {
		try {
			stuecklisteFac.removeAlleStuecklistenpositionen(stuecklisteIId);

		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void removeStueckliste(StuecklisteDto stuecklisteDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeStueckliste(stuecklisteDto, LPMain.getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void removeStklparameter(Integer stklparameterIId) throws ExceptionLP {
		try {
			stuecklisteFac.removeStklparameter(stklparameterIId);

		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void updateStuecklisteposition(StuecklistepositionDto stuecklistepositionDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateStuecklisteposition(stuecklistepositionDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public Integer createStuecklisteeigenschaft(StuecklisteeigenschaftDto stuecklisteeigenschaftDto)
			throws ExceptionLP {

		try {
			return stuecklisteFac.createStuecklisteeigenschaft(stuecklisteeigenschaftDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void removeStuecklisteeigenschaft(StuecklisteeigenschaftDto stuecklisteeigenschaftDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeStuecklisteeigenschaft(stuecklisteeigenschaftDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeLockDerPruefkombinationWennIchIhnSperre() throws ExceptionLP {
		try {
			stuecklisteFac.removeLockDerPruefkombinationWennIchIhnSperre(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void pruefeBearbeitenDerPruefkombinationErlaubt() throws ExceptionLP {
		try {
			stuecklisteFac.pruefeBearbeitenDerPruefkombinationErlaubt(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateStuecklisteeigenschaft(StuecklisteeigenschaftDto stuecklisteeigenschaftDto) throws Throwable {
		stuecklisteFac.updateStuecklisteeigenschaft(stuecklisteeigenschaftDto, LPMain.getInstance().getTheClient());
	}

	public StuecklisteeigenschaftDto stuecklisteeigenschaftFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklisteeigenschaftFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public StuecklisteeigenschaftDto stuecklisteeigenschaftFindByStuecklisteIIdStuecklisteeigenschaftartIId(
			Integer stuecklisteIId, Integer stuecklisteeigenschaftartIId) throws Exception {
		return stuecklisteFac.stuecklisteeigenschaftFindByStuecklisteIIdStuecklisteeigenschaftartIId(stuecklisteIId,
				stuecklisteeigenschaftartIId);
	}

	public Object[] kopiereStueckliste(Integer stuecklisteIId, String artikelnummerNeu, java.util.HashMap zuKopieren,
			Integer herstellerIIdNeu, Integer stuecklistepositionIId) throws ExceptionLP {
		try {
			return stuecklisteFac.kopiereStueckliste(stuecklisteIId, artikelnummerNeu, zuKopieren, herstellerIIdNeu,
					stuecklistepositionIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public Integer createStuecklisteeigenschaftart(StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto)
			throws ExceptionLP {
		try {
			return stuecklisteFac.createStuecklisteeigenschaftart(stuecklisteeigenschaftartDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public List<Integer> getMoeglicheMaschinen(Integer lossollarbeitsplanIId) throws ExceptionLP {
		try {
			return stuecklisteFac.getMoeglicheMaschinen(lossollarbeitsplanIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeStuecklisteeigenschaftart(StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.removeStuecklisteeigenschaftart(stuecklisteeigenschaftartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateStuecklisteeigenschaftart(StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto)
			throws ExceptionLP {
		try {
			stuecklisteFac.updateStuecklisteeigenschaftart(stuecklisteeigenschaftartDto,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public StuecklisteeigenschaftartDto stuecklisteeigenschaftartFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklisteeigenschaftartFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PruefkombinationDto pruefkombinationFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.pruefkombinationFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PruefartDto pruefartFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.pruefartFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PruefartDto pruefartFindByCNr(String cNr) throws ExceptionLP {
		try {
			return stuecklisteFac.pruefartFindByCNr(cNr, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public StklpruefplanDto stklpruefplanFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.stklpruefplanFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public StuecklisteeigenschaftartDto stuecklisteeigenschaftartFindByCBez(String cBez) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklisteeigenschaftartFindByCBez(cBez);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public StuecklisteDto stuecklisteFindByMandantCNrArtikelIIdOhneExc(Integer artikelIId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Saemtliche ArtikelIIds einer Stueckliste zurueckliefern, die entweder Serien-
	 * oder Chargennummerntragend sind. Eine ArtikelIId kann mehrfach vorkommen wenn
	 * der gleiche Artikel mehrfach in der Stueckliste aufgefuehrt ist.
	 * 
	 * @param stuecklisteIId ist die Stueckliste des Artikelsets
	 * @param nmenge         ist die zu erfuellende Menge des Artikelsets
	 * @return eine (leere) Liste von ArtikelIIds die serien- oder
	 *         chargennummerntragend sind.
	 * @throws ExceptionLP
	 */
	public List<Integer> getSeriennrChargennrArtikelIIdsFromArtikelset(Integer stuecklisteIId, BigDecimal nmenge)
			throws ExceptionLP {

		try {
			return stuecklisteFac.getSeriennrChargennrArtikelIIdsFromStueckliste(stuecklisteIId, nmenge,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return new ArrayList<Integer>();
		}
	}

	/**
	 * Sucht nach Artikeln f&uuml;r den intelligenten Stkl. Import
	 * 
	 * @param spez        die Importspezifikation
	 * @param importLines die Zeilen der Importdatei als Rohdaten, also nicht
	 *                    umformatiert
	 * @param rowIndex    = die Nummer der Zeile in der Datei, welche
	 *                    <code>importLines.get(0)</code> entspricht.
	 * @return eine Liste von {@link IStklImportResult}
	 * @throws ExceptionLP
	 */
	public List<IStklImportResult> searchForImportMatches(StklImportSpezifikation spez, List<String> importLines,
			int rowIndex) throws ExceptionLP {
		try {
			return iStklImportFac.searchForImportMatches(spez, importLines, rowIndex, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return new ArrayList<IStklImportResult>();
		}
	}

	/**
	 * Importiert die selektierten Artikel in den <code>results</code> in die
	 * Stueckliste.<br>
	 * Hat ein {@link IStklImportResult} keinen Artikel gesetzt (
	 * <code>{@link IStklImportResult#getSelectedArtikelDto()} == null</code>), wird
	 * ein Handartikel angelegt.
	 * 
	 * @param spez          die Importspezifikation (<code>spez.getStklIId()</code>
	 *                      darf nicht null sein!)
	 * @param results       Liste der Ergebnisse der clientseitigen Artikelzuordnung
	 * @param updateArtikel true, wenn der Artikelstamm aktualisiert werden soll
	 * @return die Anzahl der neu angelegten Positionen
	 * @throws ExceptionLP
	 */
	public int importiereStklImportResults(StklImportSpezifikation spez, List<IStklImportResult> results,
			Boolean updateArtikel) throws ExceptionLP {
		try {
			return iStklImportFac.importiereImportResultsAlsBelegpositionen(spez, results, updateArtikel,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return 0;
	}

	public void createStklImportSpez(StklImportSpezifikation spez) throws ExceptionLP {
		try {
			iStklImportFac.createStklImportSpezifikation(spez);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateStklImportSpez(StklImportSpezifikation spez) throws ExceptionLP {
		try {
			iStklImportFac.updateStklImportSpezifikation(spez);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeStklImportSpez(StklImportSpezifikation spez) throws ExceptionLP {
		try {
			iStklImportFac.removeStklImportSpezifikation(spez);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public Map<String, StklImportSpezifikation> stklImportSpezFindAll(int stklTyp, TheClientDto theClientDto)
			throws ExceptionLP {
		try {
			return iStklImportFac.stklImportSpezifikationenFindAll(stklTyp, theClientDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public void importiereStuecklistenINFRA(HashMap<String, HashMap<String, byte[]>> dateien) throws ExceptionLP {
		try {
			stuecklisteFac.importiereStuecklistenINFRA(dateien, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeStklagerentnahme(StklagerentnahmeDto stklagerentnahmeDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeStklagerentnahme(stklagerentnahmeDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public StklagerentnahmeDto updateStklagerentnahme(StklagerentnahmeDto stklagerentnahmeDto) throws ExceptionLP {
		try {
			if (stklagerentnahmeDto.getIId() == null) {
				return stuecklisteFac.createStklagerentnahme(stklagerentnahmeDto, LPMain.getTheClient());
			} else {
				return stuecklisteFac.updateStklagerentnahme(stklagerentnahmeDto, LPMain.getTheClient());
			}
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public StklagerentnahmeDto stklagerentnahmeFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.stklagerentnahmeFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void vertauscheStklagerentnahme(Integer iiDLagerentnahme1, Integer iIdLagerentnahme2) throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheStklagerentnahme(iiDLagerentnahme1, iIdLagerentnahme2);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void toggleFreigabe(Integer stuecklisteIId) throws ExceptionLP {
		try {
			stuecklisteFac.toggleFreigabe(stuecklisteIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public StuecklisteScriptartDto stuecklisteScriptartFindByMandantCNrCBez(String cBez) throws Throwable {
		try {
			return stuecklisteFac.stuecklisteScriptartFindByMandantCNrCBez(cBez, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	public BigDecimal getGesamtgewichtEinerStuecklisteInKg(Integer stuecklisteIId, BigDecimal nLosgroesse)
			throws Throwable {
		try {
			return stuecklisteFac.getGesamtgewichtEinerStuecklisteInKg(stuecklisteIId, nLosgroesse,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	public StuecklisteScriptartDto[] stuecklisteScriptartFindByMandantCNr() throws Throwable {
		try {
			return stuecklisteFac.stuecklisteScriptartFindByMandantCNr(LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return null;
	}

	public StuecklisteScriptartDto stuecklisteScriptartFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return stuecklisteFac.stuecklisteScriptartFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return null;
	}

	public Integer createStuecklisteScriptart(StuecklisteScriptartDto scriptartDto) throws ExceptionLP {
		try {
			return stuecklisteFac.createStuecklisteScriptart(scriptartDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateStuecklisteScriptart(StuecklisteScriptartDto scriptartDto) throws ExceptionLP {
		try {
			stuecklisteFac.updateStuecklisteScriptart(scriptartDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeStuecklisteScriptart(StuecklisteScriptartDto scriptartDto) throws ExceptionLP {
		try {
			stuecklisteFac.removeStuecklisteScriptart(scriptartDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void vertauscheStuecklisteScriptart(Integer iIdStuecklisteScriptart1I, Integer iIdStuecklisteScriptart2I)
			throws ExceptionLP {
		try {
			stuecklisteFac.vertauscheStuecklisteScriptart(iIdStuecklisteScriptart1I, iIdStuecklisteScriptart2I);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void createOrUpdatePositionsArbeitsplans(Integer stuecklisteId, StuecklistepositionDto[] positionDtos,
			StuecklistearbeitsplanDto[] arbeitsPlanDtos) throws ExceptionLP {
		try {
			stuecklisteFac.createOrUpdatePositionsArbeitsplans(stuecklisteId, positionDtos, arbeitsPlanDtos,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public StklpruefplanDto[] stklpruefplanFindByStuecklisteIId(Integer stuecklisteIId) throws ExceptionLP {
		try {
			return stuecklisteFac.stklpruefplanFindByStuecklisteIId(stuecklisteIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PruefkombinationDto[] pruefkombinationFindByArtikelIIdKontaktArtikelIIdLitze(Integer artikelIIdKontakt,
			Integer artikelIIdLitze) throws ExceptionLP {
		try {
			return stuecklisteFac.pruefkombinationFindByArtikelIIdKontaktArtikelIIdLitze(artikelIIdKontakt,
					artikelIIdLitze, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public PruefkombinationDto pruefkombinationFindByPruefartIIdArtikelIIdKontaktArtikelIIdLitzeVerschleissteilIId(
			Integer pruefartIId, Integer artikelIIdKontakt, Integer artikelIIdLitze, Integer verschleissteilIId)
			throws ExceptionLP {
		try {
			return stuecklisteFac.pruefkombinationFindByPruefartIIdArtikelIIdKontaktArtikelIIdLitzeVerschleissteilIId(
					pruefartIId, artikelIIdKontakt, artikelIIdLitze, verschleissteilIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer pruefeObPruefplanInPruefkombinationVorhanden(Integer stuecklisteIId, Integer pruefartIId,
			Integer artikelIIdKontakt, Integer artikelIIdLitze, Integer artikelIIdLitze2, Integer verschleissteilIId,
			Integer pruefkombinationIId) throws ExceptionLP {
		try {
			return stuecklisteFac.pruefeObPruefplanInPruefkombinationVorhanden(stuecklisteIId, pruefartIId,
					artikelIIdKontakt, artikelIIdLitze, artikelIIdLitze2, verschleissteilIId, pruefkombinationIId,
					false, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<Integer> getVorgeschlageneVerschleissteile(Integer artikelIIdKontakt, Integer artikelIIdLitze,
			Integer artikelIIdLitze2, boolean bDoppelanschlag) throws ExceptionLP {
		try {
			return stuecklisteFac.getVorgeschlageneVerschleissteile(artikelIIdKontakt, artikelIIdLitze,
					artikelIIdLitze2, bDoppelanschlag);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<String, String> getAllArbeitsgangarten() throws ExceptionLP {
		try {
			return stuecklisteFac.getAllArbeitsgangarten();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<Integer, String> getAllPruefartenFuerPruefkombinationen() throws ExceptionLP {
		try {
			return stuecklisteFac.getAllPruefartenFuerPruefkombinationen(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	
	public VerkaufspreisDto getKalkuliertenVerkaufspreisAusGesamtkalkulation(Integer artikelIId, BigDecimal bdMenge,java.sql.Date belegdatum,
			String waehrungCNr)  throws ExceptionLP{
		try {
			return stuecklisteFac.getKalkuliertenVerkaufspreisAusGesamtkalkulation(artikelIId,bdMenge,belegdatum,waehrungCNr,LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	
	
}
