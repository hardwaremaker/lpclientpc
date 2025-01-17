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

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.service.EinstandspreiseEinesWareneingangsDto;
import com.lp.server.bestellung.service.RueckgabeWEPMitReelIDDto;
import com.lp.server.bestellung.service.WEPBuchenReturnDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.system.service.TheClientDto;

public class WareneingangDelegate extends Delegate {
	private Context context;
	private WareneingangFac wareneingangFac;

	public WareneingangDelegate() throws Throwable {
		context = new InitialContext();
		wareneingangFac = lookupFac(context, WareneingangFac.class);
	}

	public Integer createWareneingang(WareneingangDto wareneingangDto)
			throws ExceptionLP {
		Integer iIdWareneingang = null;
		try {
			iIdWareneingang = wareneingangFac.createWareneingang(
					wareneingangDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iIdWareneingang;
	}

	public void removeWareneingang(WareneingangDto wareneingangDto)
			throws ExceptionLP {
		try {
			wareneingangFac.removeWareneingang(wareneingangDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void geliefertPreiseAllerWEPRueckpflegen(java.sql.Date dVon,
			java.sql.Date dBis) throws ExceptionLP {
		try {
			wareneingangFac.geliefertPreiseAllerWEPRueckpflegen(dVon, dBis,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateWareneingang(WareneingangDto wareneingangDto)
			throws ExceptionLP {
		try {
			wareneingangFac.updateWareneingang(wareneingangDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public WareneingangDto wareneingangFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		WareneingangDto wareneingangDto = null;
		try {
			wareneingangDto = wareneingangFac.wareneingangFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return wareneingangDto;
	}

	public WareneingangDto wareneingangFindByPrimaryKeyOhneExc(Integer iId)
			throws ExceptionLP {
		WareneingangDto wareneingangDto = null;
		try {
			wareneingangDto = wareneingangFac
					.wareneingangFindByPrimaryKeyOhneExc(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return wareneingangDto;
	}

	public WareneingangDto[] wareneingangFindByBestellungIId(
			Integer iIdBestellungI) throws ExceptionLP {
		WareneingangDto[] aWareneingangDto = null;
		try {
			aWareneingangDto = wareneingangFac
					.wareneingangFindByBestellungIId(iIdBestellungI);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return aWareneingangDto;
	}

	public WareneingangDto[] wareneingangFindByEingangsrechnungIId(
			Integer eingangsrechnungIId) throws ExceptionLP {
		WareneingangDto[] aWareneingangDto = null;
		try {
			aWareneingangDto = wareneingangFac
					.wareneingangFindByEingangsrechnungIId(eingangsrechnungIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return aWareneingangDto;
	}

	/**
	 * Zwei bestehende Bestellpositionen in Bezug auf ihr iSort umreihen.
	 * 
	 * @param iIdPosition1I
	 *            PK der ersten Position
	 * @param iIdPosition2I
	 *            PK der zweiten Position
	 * @throws ExceptionLP
	 *             Ausnahme
	 */
	public void vertauscheWareneingang(Integer iIdPosition1I,
			Integer iIdPosition2I) throws ExceptionLP {
		try {
			wareneingangFac
					.vertauscheWareneingang(iIdPosition1I, iIdPosition2I);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void wareneingangspositionAlsVerraeumtKennzeichnen(
			Integer wareneingangspositionIId) throws ExceptionLP {
		try {
			wareneingangFac
					.wareneingangspositionAlsVerraeumtKennzeichnen(wareneingangspositionIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public RueckgabeWEPMitReelIDDto wareneingangspositionMitReelIDBuchen(
			Integer wareneingangIId, Integer bestellpositionIId,
			BigDecimal nMenge, String datecode, String batch1)
			throws ExceptionLP {
		RueckgabeWEPMitReelIDDto weposIId = null;
		try {
			weposIId = wareneingangFac.wareneingangspositionMitReelIDBuchen(
					wareneingangIId, bestellpositionIId, nMenge, datecode,
					batch1, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return weposIId;
	}

	public Integer createWareneingangsposition(
			WareneingangspositionDto wareneingangspositionenDto)
			throws ExceptionLP {
		Integer weposIId = null;
		try {
			weposIId = wareneingangFac.createWareneingangsposition(
					wareneingangspositionenDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return weposIId;
	}

	public EinstandspreiseEinesWareneingangsDto getBerechnetenEinstandspreisEinerWareneingangsposition(
			Integer wareneingangIId) throws ExceptionLP {

		try {
			return wareneingangFac
					.getBerechnetenEinstandspreisEinerWareneingangsposition(
							wareneingangIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public BigDecimal getWareneingangWertsumme(WareneingangDto wareneingangDto)
			throws ExceptionLP {

		try {
			return wareneingangFac.getWareneingangWertsumme(wareneingangDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}
	public String generiereAutomatischeChargennummerAnhandBestellnummerWEPNrPosnr(
			Integer bsposIId, Integer wareneingangIId)
			throws ExceptionLP {

		try {
			return wareneingangFac.generiereAutomatischeChargennummerAnhandBestellnummerWEPNrPosnr(bsposIId,wareneingangIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}
	public void removeWareneingangsposition(
			WareneingangspositionDto wareneingangspositionenDto)
			throws ExceptionLP {
		try {
			wareneingangFac.removeWareneingangsposition(
					wareneingangspositionenDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public Integer getLieferscheinIIdAusWareUnterwegs(String wareunterwegsCNr)
			throws ExceptionLP {

		try {
			return wareneingangFac.getLieferscheinIIdAusWareUnterwegs(
					wareunterwegsCNr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}

	}

	public void wareUnterwegsZubuchen(Integer lieferscheinIId,ArrayList<String> wareunterwegsCNr)
			throws ExceptionLP {
		try {
			wareneingangFac.wareUnterwegsZubuchen(
					lieferscheinIId,wareunterwegsCNr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateWareneingangsposition(
			WareneingangspositionDto wareneingangspositionenDto)
			throws ExceptionLP {
		try {
			wareneingangFac.updateWareneingangsposition(
					wareneingangspositionenDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * updated eine WEP ohne weitere Aktionen zb. Statusaenderung erfolgt hier
	 * nicht
	 * 
	 * @param wareneingangspositionenDto
	 *            BestellungDto
	 * @throws ExceptionLP
	 */
	public void updateWareneingangspositionInternenKommentar(
			WareneingangspositionDto wareneingangspositionenDto)
			throws ExceptionLP {
		try {
			wareneingangFac.updateWareneingangspositionInternenKommentar(
					wareneingangspositionenDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public WareneingangspositionDto wareneingangspositionFindByPrimaryKey(
			Integer iId) throws ExceptionLP {
		WareneingangspositionDto wareneingangspositionDto = null;
		try {
			wareneingangspositionDto = wareneingangFac
					.wareneingangspositionFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return wareneingangspositionDto;
	}
	public WareneingangspositionDto wareneingangspositionFindByPrimaryKeyOhneExc(
			Integer iId) throws ExceptionLP {
		WareneingangspositionDto wareneingangspositionDto = null;
		try {
			wareneingangspositionDto = wareneingangFac
					.wareneingangspositionFindByPrimaryKeyOhneExc(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return wareneingangspositionDto;
	}
	public WareneingangspositionDto[] wareneingangspositionFindByWareneingangIId(
			Integer iIdWareneingangI) throws ExceptionLP {
		WareneingangspositionDto[] wareneingangspositionDto = null;
		try {
			wareneingangspositionDto = wareneingangFac
					.wareneingangspositionFindByWareneingangIId(iIdWareneingangI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return wareneingangspositionDto;
	}

	public Integer getAnzahlWE(Integer iIdBestellungI) throws ExceptionLP {

		try {
			return wareneingangFac.getAnzahlWE(iIdBestellungI);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer getAnzahlWEP(Integer iIdWareneingangI) throws ExceptionLP {

		try {
			return wareneingangFac.getAnzahlWEP(iIdWareneingangI);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public WEPBuchenReturnDto uebernimmAlleWepsOhneBenutzerinteraktion(
			Integer iIdWareneingangI, Integer iIdBestellung, ArrayList<Integer> bestellpositionIIds_selektiert) throws ExceptionLP {
		WEPBuchenReturnDto wEPBuchenReturnDto = null;
		try {
			wEPBuchenReturnDto = wareneingangFac.uebernimmAlleWepsOhneBenutzerinteraktion(
					iIdWareneingangI, iIdBestellung, bestellpositionIIds_selektiert, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return wEPBuchenReturnDto;
	}

	public Integer[] erfasseAllePreiseOhneBenutzerinteraktion(
			Integer iIdWareneingangI) throws ExceptionLP {
		Integer[] iToReturn = null;
		try {
			iToReturn = wareneingangFac
					.erfasseAllePreiseOhneBenutzerinteraktion(iIdWareneingangI,
							LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iToReturn;
	}

	public BigDecimal berechneWertDesWareneingangsInBestellungswaehrung(
			Integer iIdWareneingangI) throws ExceptionLP {
		BigDecimal iToReturn = null;
		try {
			iToReturn = wareneingangFac
					.berechneWertDesWareneingangsInBestellungswaehrung(
							iIdWareneingangI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iToReturn;
	}

	public WareneingangspositionDto[] wareneingangspositionFindByBestellpositionIId(
			Integer iIdBestellpositionI) throws ExceptionLP {

		WareneingangspositionDto[] wepDtos = null;
		try {
			wepDtos = wareneingangFac
					.wareneingangspositionFindByBestellpositionIId(iIdBestellpositionI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return wepDtos;
	}

	public boolean allePreiseFuerWareneingangErfasst(Integer iWareneingangIId)
			throws ExceptionLP {
		try {
			return wareneingangFac
					.allePreiseFuerWareneingangErfasst(iWareneingangIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return false;
	}

	public Integer createWareneingangsposition(
			WareneingangspositionDto wareneingangspositionenDto,
			boolean setartikelAufloesen) throws ExceptionLP {
		Integer weposIId = null;
		try {
			weposIId = wareneingangFac.createWareneingangsposition(
					wareneingangspositionenDto, setartikelAufloesen,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

		return weposIId;
	}

	public void updateWareneingangsposition(WareneingangspositionDto weposDtoI,
			boolean setartikelAufloesen) throws ExceptionLP {
		try {
			wareneingangFac.updateWareneingangsposition(weposDtoI,
					setartikelAufloesen, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeWareneingangsposition(
			WareneingangspositionDto positionDto, boolean setartikelAufloesen)
			throws ExceptionLP {
		try {
			wareneingangFac.removeWareneingangsposition(positionDto,
					setartikelAufloesen, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
}
