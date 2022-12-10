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
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.BucheBelegPeriodeInfoDto;
import com.lp.server.finanz.service.FibuFehlerDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoartDto;
import com.lp.server.finanz.service.KontoartsprDto;
import com.lp.server.finanz.service.KontotypDto;
import com.lp.server.finanz.service.KontotypsprDto;
import com.lp.server.finanz.service.LaenderartDto;
import com.lp.server.finanz.service.MahnspesenDto;
import com.lp.server.finanz.service.MahntextDto;
import com.lp.server.finanz.service.ReportUvaKriterienDto;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.finanz.service.SteuerkategoriekontoDto;
import com.lp.server.finanz.service.UvaartDto;
import com.lp.server.finanz.service.UvaartsprDto;
import com.lp.server.finanz.service.UvaverprobungDto;
import com.lp.server.finanz.service.WarenverkehrsnummerDto;
import com.lp.server.util.SteuerkategoriekontoId;
import com.lp.util.EJBExceptionLP;

public class FinanzServiceDelegate extends Delegate {
	private Context context;
	private FinanzServiceFac finanzServiceFac;

	public FinanzServiceDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			finanzServiceFac = lookupFac(context, FinanzServiceFac.class);

		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public Integer createMahntext(MahntextDto mahntextDto) throws ExceptionLP {
		try {
			return finanzServiceFac.createMahntext(mahntextDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createMahnspesen(MahnspesenDto dto) throws ExceptionLP {
		try {
			return finanzServiceFac.createMahnspesen(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createSteuerkategorie(SteuerkategorieDto steuerkategorieDto)
			throws ExceptionLP {
		try {
			return finanzServiceFac.createSteuerkategorie(steuerkategorieDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateSteuerkategorie(SteuerkategorieDto steuerkategorieDto)
			throws ExceptionLP {
		try {
			finanzServiceFac.updateSteuerkategorie(steuerkategorieDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateMahnspesen(MahnspesenDto dto) throws ExceptionLP {
		try {
			finanzServiceFac.updateMahnspesen(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}
	public SteuerkategoriekontoId createSteuerkategoriekonto(
			SteuerkategoriekontoDto steuerkategoriekontoDto) throws ExceptionLP {
		try {
			return finanzServiceFac.createSteuerkategoriekonto(steuerkategoriekontoDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	public void updateSteuerkategoriekonto(
			SteuerkategoriekontoDto steuerkategoriekontoDto) throws ExceptionLP {
		try {
			finanzServiceFac.updateSteuerkategoriekonto(
					steuerkategoriekontoDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeMahntext(MahntextDto mahntextDto) throws ExceptionLP {
		try {
			finanzServiceFac.removeMahntext(mahntextDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeMahnspesen(MahnspesenDto dto) throws ExceptionLP {
		try {
			finanzServiceFac.removeMahnspesen(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeSteuerkategorie(Integer iId) throws ExceptionLP {
		try {
			finanzServiceFac.removeSteuerkategorie(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}
	public void removeSteuerkategoriekonto(Integer iId) throws ExceptionLP {
		try {
			finanzServiceFac.removeSteuerkategoriekonto(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateMahntext(MahntextDto mahntextDto) throws ExceptionLP {
		try {
			finanzServiceFac.updateMahntext(mahntextDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public MahntextDto mahntextFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return finanzServiceFac.mahntextFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MahntextDto mahntextFindByMandantLocaleCNr(String pSprache,
			Integer mahnstufeIId) throws ExceptionLP {
		try {
			return finanzServiceFac.mahntextFindByMandantLocaleCNr(LPMain
					.getTheClient().getMandant(), pSprache, mahnstufeIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MahntextDto createDefaultMahntext(Integer mahnstufeIId,
			String sTextinhaltI) throws ExceptionLP {
		MahntextDto oMahntextDto = null;

		try {
			oMahntextDto = finanzServiceFac.createDefaultMahntext(mahnstufeIId,
					sTextinhaltI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return oMahntextDto;
	}

	public KontotypsprDto kontotypsprFindByPrimaryKey(String kontotypCNr,
			String localeCNr) throws ExceptionLP {
		try {
			return finanzServiceFac.kontotypsprFindByPrimaryKey(kontotypCNr,
					localeCNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public KontotypDto kontotypFindByPrimaryKey(String cNr) throws ExceptionLP {
		try {
			return finanzServiceFac.kontotypFindByPrimaryKey(cNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public UvaartDto uvaartFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return finanzServiceFac.uvaartFindByPrimaryKey(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public UvaartDto uvaartFindByCnr(String cnr) throws ExceptionLP {
		try {
			return finanzServiceFac.uvaartFindByCnrMandant(cnr,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public UvaartsprDto uvaartsprFindByPrimaryKey(Integer uvaartIId,String localeCNr) throws ExceptionLP {
		try {
			return finanzServiceFac.uvaartsprFindByPrimaryKey(uvaartIId,localeCNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public SteuerkategorieDto steuerkategorieFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return finanzServiceFac.steuerkategorieFindByPrimaryKey(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public SteuerkategoriekontoDto[] steuerkategoriekontoFindAll(
			Integer steuerkategorieIId) throws ExceptionLP {
		try {
			return finanzServiceFac
					.steuerkategoriekontoFindAll(steuerkategorieIId);
		} catch (Throwable ex) {
			// handleThrowable(ex);
			return null;
		}
	}

	public KontoartsprDto kontoartsprFindByPrimaryKey() throws ExceptionLP {
		try {
			return finanzServiceFac.kontoartsprFindByPrimaryKey();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateKontoart(KontoartDto kontoartDto) throws ExceptionLP {
		try {
			finanzServiceFac.updateKontoart(kontoartDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateLaenderart(LaenderartDto laenderartDto)
			throws ExceptionLP {
		try {
			finanzServiceFac.updateLaenderart(laenderartDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateUvaart(UvaartDto uvaartDto) throws ExceptionLP {
		try {
			finanzServiceFac.updateUvaart(uvaartDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public KontoartDto kontoartFindByPrimaryKey(String cNr) throws ExceptionLP {
		try {
			return finanzServiceFac.kontoartFindByPrimaryKey(cNr,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MahnspesenDto mahnspesenFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return finanzServiceFac.mahnspesenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public KontoartDto[] kontoartFindAll() throws ExceptionLP {
		try {
			return finanzServiceFac.kontoartFindAll();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String uebersetzeKontotypOptimal(String cNr) throws ExceptionLP {

		try {
			Locale locale1 = LPMain.getTheClient().getLocUi();
			Locale locale2 = LPMain.getTheClient().getLocMandant();
			return finanzServiceFac.uebersetzeKontotypOptimal(cNr, locale1,
					locale2);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String uebersetzeBuchungsartOptimal(String cNr) throws ExceptionLP {
		try {
			Locale locale1 = LPMain.getTheClient().getLocUi();
			Locale locale2 = LPMain.getTheClient().getLocMandant();
			return finanzServiceFac.uebersetzeBuchungsartOptimal(cNr, locale1,
					locale2);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String uebersetzeKontoartOptimal(String cNr) throws ExceptionLP {

		try {
			Locale locale1 = LPMain.getTheClient().getLocUi();
			Locale locale2 = LPMain.getTheClient().getLocMandant();
			return finanzServiceFac.uebersetzeKontoartOptimal(cNr, locale1,
					locale2);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String uebersetzeUvaartOptimal(Integer iId) throws ExceptionLP {
		try {
			Locale locale1 = LPMain.getTheClient().getLocUi();
			Locale locale2 = LPMain.getTheClient().getLocMandant();
			return finanzServiceFac.uebersetzeUvaartOptimal(iId, locale1,
					locale2);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LaenderartDto laenderartFindByPrimaryKey(String cNr)
			throws ExceptionLP {
		try {
			return finanzServiceFac.laenderartFindByPrimaryKey(cNr,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public HashMap<String, String> getAllLaenderartenMitUebersetzung() throws ExceptionLP {
		try {
			return finanzServiceFac.getAllLaenderartenMitUebersetzung(LPMain
					.getTheClient().getLocUi(), LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Alle Buchungsarten holen.
	 * 
	 * @throws ExceptionLP
	 * @return TreeMap
	 */
	public TreeMap<?, ?> getAllBuchungsarten() throws ExceptionLP {
		myLogger.entry();
		try {
			Locale locale1 = LPMain.getTheClient().getLocUi();
			Locale locale2 = LPMain.getTheClient().getLocMandant();
			TreeMap<?, ?> arten = finanzServiceFac.getAllBuchungsarten(
					locale1, locale2, LPMain.getTheClient().getMandant());
			
			// SP9354 Keine "Buchung" mehr fuer den Client
			arten.remove(FinanzFac.BUCHUNGSART_BUCHUNG);
			return arten;
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getLaenderartZuPartner(Integer partnerIId, Timestamp gueltigZum) throws ExceptionLP {
		try {
			return finanzServiceFac.getLaenderartZuPartner(
					partnerIId, gueltigZum, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public WarenverkehrsnummerDto warenverkehrsnummerFindByPrimaryKey(String cNr)
			throws ExceptionLP {
		try {
			return finanzServiceFac.warenverkehrsnummerFindByPrimaryKey(cNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void vertauscheSteuerkategorie(Integer iiD1, Integer iId2)
			throws ExceptionLP {
		try {
			finanzServiceFac.vertauscheSteuerkategorie(iiD1, iId2);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public WarenverkehrsnummerDto warenverkehrsnummerFindByPrimaryKeyOhneExc(
			String cNr) throws ExceptionLP {
		try {
			return finanzServiceFac
					.warenverkehrsnummerFindByPrimaryKeyOhneExc(cNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void createWarenverkehrsnummernUndLoescheAlte(
			WarenverkehrsnummerDto[] dtos) throws ExceptionLP {
		try {
			finanzServiceFac.createWarenverkehrsnummernUndLoescheAlte(dtos,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer getUstKontoFuerSteuerkategorie(
			Integer steuerkategorieIId, Integer mwstsatzbezId, Timestamp gueltigAm) throws ExceptionLP {
		try {
			return finanzServiceFac.getUstKontoFuerSteuerkategorie(
					steuerkategorieIId, mwstsatzbezId, gueltigAm);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer getVstKontoFuerSteuerkategorie(
			Integer steuerkategorieIId, Integer mwstsatzbezId, Timestamp gueltigAm) throws ExceptionLP {
		try {
			return finanzServiceFac.getVstKontoFuerSteuerkategorie(
					steuerkategorieIId, mwstsatzbezId, gueltigAm);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public List<BucheBelegPeriodeInfoDto> verbucheBelege(
			Integer geschaeftsjahr, 
			int periode, boolean alleNeu) throws ExceptionLP {
		try {
			return finanzServiceFac.verbucheBelegePeriode(geschaeftsjahr, periode,
					alleNeu, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return new ArrayList<BucheBelegPeriodeInfoDto>();
		}
	}

	public ArrayList<FibuFehlerDto> pruefeBelege(Integer geschaeftsjahr,
			int periode, boolean pruefeBelegInFibu) throws ExceptionLP {
		try {
			return finanzServiceFac.pruefeBelegePeriode(geschaeftsjahr,
					periode, pruefeBelegInFibu, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public Integer uvaVerprobung(ReportUvaKriterienDto krit) throws ExceptionLP {
		try {
			return finanzServiceFac.uvaVerprobung(krit, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public UvaverprobungDto letzteVerprobung(Integer partnerIIdFinanzamt)
			throws ExceptionLP {
		try {
			return finanzServiceFac.getLetzteVerprobung(partnerIIdFinanzamt,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public SteuerkategoriekontoDto[] createDefaultSteuerkategoriekonto(
			Integer steuerkategorieIId) throws ExceptionLP {
		try {
			finanzServiceFac.createDefaultSteuerkategoriekonto(
					steuerkategorieIId, LPMain.getTheClient());
			return finanzServiceFac
					.steuerkategoriekontoFindAll(steuerkategorieIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

// TODO: Veraltet, ReversechargeartId ist fuer eindeutiges Ergebnis notwendig
//	public SteuerkategorieDto steuerkategorieFindByCNrFinanzamtIId(String nr,
//			Integer finanzamtIId) throws ExceptionLP {
//		try {
//			return finanzServiceFac.steuerkategorieFindByCNrFinanzamtIId(nr,
//					finanzamtIId, LPMain.getTheClient());
//		} catch (Throwable ex) {
//			handleThrowable(ex);
//		}
//		return null;
//	}

	/**
	 * Alle im System bekannten Steuerkategorien f&uuml;r das angegebene Finanzamt
	 * 
	 * @param finanzamtIId
	 *            die zu betrachtende ID des Finanzamts
	 * @throws ExceptionLP
	 * @return null wenn es keine gibt, ansonsten die bekannten
	 *         SteuerkategorieDtos
	 */
	public SteuerkategorieDto[] steuerkategorieFindByFinanzamtIId(
			Integer finanzamtIId) throws ExceptionLP {
		try {
			return finanzServiceFac.steuerkategorieFindByFinanzamtIId(
					finanzamtIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	/**
	 * Die Standardsteuerkategorien fuer das angegebene Finanzamt anlegen.
	 * 
	 * Die Standardsteuerkategorien werden nur angelegt, wenn es noch keine
	 * Kategorien fuer dieses Finanzamt gibt.
	 * 
	 * @param finanzamtIId
	 * @throws ExceptionLP 
	 */
	public void createIfNeededSteuerkategorieForFinanzamtIId(
			Integer finanzamtIId) throws ExceptionLP {
		try {
			finanzServiceFac.createIfNeededSteuerkategorieForFinanzamtIId(
					finanzamtIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Fuer alle diesem Finanzamt zugehoerigen Steuerkategorien die
	 * Steuerkategoriekonten anlegen sofern nicht vorhanden
	 * 
	 * Sollte es noch keine Steuerkategorien geben fuer dieses Finanzamt werden
	 * auch diese angelegt.
	 * 
	 * @param finanzamtIId
	 * @throws ExceptionLP
	 */
	public void createIfNeededSteuerkategoriekontoForFinanzamtIId(
			Integer finanzamtIId) throws ExceptionLP {
		try {
			finanzServiceFac.createIfNeededSteuerkategoriekontoForFinanzamtIId(
					finanzamtIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeUvaverprobung(UvaverprobungDto uvapDto)
			throws ExceptionLP {
		try {
			finanzServiceFac.removeUvaverprobung(uvapDto.getIId());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public ArrayList<FibuFehlerDto> pruefeBelegeKurs(Integer geschaeftsjahr,
			boolean nurPruefen) throws ExceptionLP {
		try {
			return finanzServiceFac.pruefeBelegeKurse(geschaeftsjahr,
					nurPruefen, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public Integer createAuszifferung(
			Integer geschaeftsjahrVerursacher, Integer[] ids) throws ExceptionLP {
		try {
			return finanzServiceFac.createAuszifferung(
					geschaeftsjahrVerursacher, ids, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public Integer updateAuszifferung(Integer geschaeftsjahrVerursacher, 
			Integer auszifferKennzeichen, Integer[] ids) throws ExceptionLP {
		try {
			return finanzServiceFac.updateAuszifferung(
					geschaeftsjahrVerursacher, auszifferKennzeichen, ids, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public void removeAuszifferung(Integer[] ids) throws ExceptionLP {
		try {
			finanzServiceFac.removeAuszifferung(ids, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

//	public boolean istIgErwerb(Integer kreditorenKontoIId) {
//		KontoDto kreditorenKontoDto = null;
//		SteuerkategorieDto steuerkategorieDto = null;
//		try {
//			kreditorenKontoDto = DelegateFactory.getInstance()
//					.getFinanzDelegate()
//					.kontoFindByPrimaryKey(kreditorenKontoIId);
//			steuerkategorieDto = steuerkategorieFindByPrimaryKey(kreditorenKontoDto
//					.getSteuerkategorieIId());
//		} catch (Throwable e) {
//			//
//		}
//		if (kreditorenKontoDto == null || steuerkategorieDto == null) {
//			return false;
//		} else {
//			if (steuerkategorieDto.getCNr().equals(
//					FinanzServiceFac.STEUERKATEGORIE_AUSLANDEU_UID)) {
//				return true;
//			} else {
//				return false;
//			}
//		}
//	}

	public boolean istIgErwerb(Integer kreditorenKontoIId) {
		KontoDto kreditorenKontoDto = null;
		try {
			kreditorenKontoDto = DelegateFactory.getInstance()
					.getFinanzDelegate()
					.kontoFindByPrimaryKey(kreditorenKontoIId);
			return FinanzServiceFac.STEUERKATEGORIE_AUSLANDEU_UID.equals(kreditorenKontoDto.getSteuerkategorieCnr()) ;
		} catch (Throwable e) {
		}
		
		return false ;
	}
	
	public void createFinanzamtsbuchungen(Integer geschaeftsjahr, int periode) throws ExceptionLP {
		try {
			finanzServiceFac.createFinanzamtsbuchungen(geschaeftsjahr, periode, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}	
	}
	
	public BigDecimal getLiquiditaetsKontostand(Integer geschaeftsjahrIId) throws ExceptionLP {
		try {
			return finanzServiceFac.getLiquiditaetsKontostand(geschaeftsjahrIId, LPMain.getTheClient());
		} catch(Throwable t) {
			handleThrowable(t);
		}
		return null;
	}
	
	public void pruefeSteuerkonten(Integer finanzamtId) throws ExceptionLP {
		try {
			finanzServiceFac.pruefeSteuerkonten(finanzamtId, LPMain.getTheClient());
		} catch(Throwable t)  {
			handleThrowable(t) ;
		}
	}
	
	public ReversechargeartDto reversechargeartFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return finanzServiceFac.reversechargeartFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}	
	
	
	public ReversechargeartDto reversechargeartFindOhne() throws ExceptionLP {
		try {
			return finanzServiceFac.reversechargeartFindOhne(LPMain.getTheClient()) ;
		} catch(Throwable t) {
			handleThrowable(t);
			return null ;
		}
	}
	
	public Integer createReversechargeart(ReversechargeartDto reversechargeartDto) throws ExceptionLP {
		try {
			return finanzServiceFac.createReversechargeart(reversechargeartDto, LPMain.getTheClient()) ;
		} catch(Throwable t) {
			handleThrowable(t);
		}
		return null ;
	}

	public void updateReversechargeart(ReversechargeartDto reversechargeartDto) throws ExceptionLP {
		try {
			finanzServiceFac.updateReversechargeart(reversechargeartDto, LPMain.getTheClient()) ;
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeReversechargeart(Integer reversechargeartId) throws ExceptionLP {
		try {
			finanzServiceFac.removeReversechargeart(reversechargeartId);
		} catch(Throwable t) {
			handleThrowable(t) ;
		}
	}
	
    public Map<Integer, String> getAllReversechargeArt() throws ExceptionLP {
    	try {
    		return finanzServiceFac.getAllReversechargeArt(LPMain.getTheClient()) ;
    	} catch(Throwable t) {
    		handleThrowable(t) ;
    	}
    	return null ;
    } 
    
    public Map<Integer, String> getAllReversechargeartAllowed() throws ExceptionLP {
    	try {
    		return finanzServiceFac.getAllReversechargeartAllowed(LPMain.getTheClient()) ;
    	} catch(Throwable t) {
    		handleThrowable(t) ;
    	}
    	return null ;    	
    }
    
	public void createIfNeededSteuerkategorieForFinanzamtIId(
			Integer finanzamtIId, Integer reversechargeartId) throws ExceptionLP {
		try {
			finanzServiceFac.createIfNeededSteuerkategorieForFinanzamtIId(
					finanzamtIId, reversechargeartId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}
	
	public SteuerkategorieDto steuerkategorieFindByCNrFinanzamtIId(
			String nr, Integer reversechargeartId, Integer finanzamtIId) throws ExceptionLP {
		try {
			return finanzServiceFac.steuerkategorieFindByCNrFinanzamtIId(nr,
					reversechargeartId, finanzamtIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	
	public Map<String, String> getAllSteuerkategorieCnr() throws ExceptionLP {
		try {
			return finanzServiceFac.getAllSteuerkategorieCnr(LPMain.getTheClient()) ;
		} catch(Throwable t) {
			handleThrowable(t) ;
			return null ;
		}
	}
	
	public Integer getUstKontoFuerSteuerkategorie(String steuerkategorieCnr, 
			Integer finanzamtId, Integer mwstsatzbezId, Timestamp gueltigAm) throws ExceptionLP {
		try {
			return finanzServiceFac.getUstKontoFuerSteuerkategorie(
					steuerkategorieCnr,finanzamtId, mwstsatzbezId,
					gueltigAm, LPMain.getTheClient()) ; 
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer getVstKontoFuerSteuerkategorie(String steuerkategorieCnr, 
			Integer finanzamtId, Integer mwstsatzbezId, Timestamp gueltigAm) throws ExceptionLP {
		try {
			return finanzServiceFac.getVstKontoFuerSteuerkategorie(
					steuerkategorieCnr, finanzamtId, mwstsatzbezId, 
					gueltigAm, LPMain.getTheClient()) ; 
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}	
	
	public void vertauscheReversechargeart(Integer id1, Integer id2)
			throws ExceptionLP {
		try {
			finanzServiceFac.vertauscheReversechargeart(id1, id2); 
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Map<Integer, ReversechargeartDto> reversechargeartFindByPrimaryKeys(Integer... keys)
			throws ExceptionLP {
		try {
			return finanzServiceFac.reversechargeartFindByPrimaryKeys(LPMain.getTheClient(), keys);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void createGewinnbuchungen(int geschaeftsjahr, int periode,
			BigDecimal betrag) throws EJBExceptionLP, RemoteException, Throwable {
		finanzServiceFac.createGewinnbuchungen(geschaeftsjahr, periode, betrag, LPMain.getTheClient());
	}	
	
	public SteuerkategoriekontoDto[] steuerkategoriekontoAllZuDatum(
			Integer steuerkategorieId, Timestamp gueltigZum) throws ExceptionLP {
		try {
			return finanzServiceFac.steuerkategoriekontoAllZuDatum(
					steuerkategorieId, gueltigZum);
		} catch (Throwable ex) {
			return new SteuerkategoriekontoDto[0];
		}
	}
	
	public void pflegeUvaFormulare() throws ExceptionLP {
		try {
			finanzServiceFac.pflegeUvaFormulare(LPMain.getTheClient());
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
    
    public Map<Integer, String> getAllReversechargeartAllowedMitVersteckten() throws ExceptionLP {
    	try {
    		return finanzServiceFac.getAllReversechargeartAllowed(Boolean.TRUE, LPMain.getTheClient()) ;
    	} catch(Throwable t) {
    		handleThrowable(t) ;
    	}
    	return null ;    	
    }
}
