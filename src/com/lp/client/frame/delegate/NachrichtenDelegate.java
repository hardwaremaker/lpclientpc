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
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.forecast.service.CallOffXlsImporterResult;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastartDto;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.forecast.service.ImportdefDto;
import com.lp.server.forecast.service.LinienabrufDto;
import com.lp.server.personal.service.NachrichtenDto;
import com.lp.server.personal.service.NachrichtenFac;
import com.lp.server.personal.service.NachrichtenaboDto;
import com.lp.server.personal.service.NachrichtenartDto;
import com.lp.server.personal.service.NachrichtenempfaengerDto;
import com.lp.server.personal.service.NachrichtengruppeDto;
import com.lp.server.personal.service.NachrichtengruppeteilnehmerDto;
import com.lp.server.system.service.ForecastimportFac;
import com.lp.server.system.service.IForecastImportFile;
import com.lp.server.system.service.VersandFac;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um die Auftrag Reports.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>27.06.05</I>
 * </p>
 * 
 * @author $Author: christian $
 * @version $Revision: 1.28 $
 */
public class NachrichtenDelegate extends Delegate {
	private Context context;
	private NachrichtenFac nachrichtenFac;

	public NachrichtenDelegate() throws Exception {

		context = new InitialContext();
		nachrichtenFac = lookupFac(context, NachrichtenFac.class);
	}

	public void updateNachrichtenart(NachrichtenartDto nachrichtenartDto) throws ExceptionLP {

		try {
			nachrichtenFac.updateNachrichtenart(nachrichtenartDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void updateNachrichtengruppe(NachrichtengruppeDto nachrichtengruppeDto) throws ExceptionLP {

		try {
			nachrichtenFac.updateNachrichtengruppe(nachrichtengruppeDto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void updateNachrichtengruppeteilnehmer(NachrichtengruppeteilnehmerDto dto) throws ExceptionLP {

		try {
			nachrichtenFac.updateNachrichtengruppeteilnehmer(dto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void updateNachrichtenabo(NachrichtenaboDto dto) throws ExceptionLP {

		try {
			nachrichtenFac.updateNachrichtenabo(dto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void removeNachrichtengruppe(NachrichtengruppeDto nachrichtengruppeDto) throws ExceptionLP {

		try {
			nachrichtenFac.removeNachrichtengruppe(nachrichtengruppeDto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void nachrichtAnEinzelpersonErstellen(String cBetreff, String xText, Integer personalIId)
			throws ExceptionLP {

		try {
			nachrichtenFac.nachrichtAnEinzelpersonErstellen(cBetreff, xText, personalIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void nachrichtenempfaengerAlsGelesenMarkieren(Integer nachrichtenempaengerIId, boolean bGelesen) throws ExceptionLP {

		try {
			nachrichtenFac.nachrichtenempfaengerAlsGelesenMarkieren(nachrichtenempaengerIId, bGelesen, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void removeNachrichtengruppeteilnehmer(NachrichtengruppeteilnehmerDto dto) throws ExceptionLP {

		try {
			nachrichtenFac.removeNachrichtengruppeteilnehmer(dto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void removeNachrichtenabo(NachrichtenaboDto dto) throws ExceptionLP {

		try {
			nachrichtenFac.removeNachrichtenabo(dto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public NachrichtenartDto nachrichtenartFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return nachrichtenFac.nachrichtenartFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public Map getMoeglicheNachrichtenempfaenger() throws ExceptionLP {

		try {
			return nachrichtenFac.getMoeglicheNachrichtenempfaenger(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public NachrichtenaboDto nachrichtenaboFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return nachrichtenFac.nachrichtenaboFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public NachrichtenempfaengerDto nachrichtenempfaengerFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return nachrichtenFac.nachrichtenempfaengerFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public NachrichtenDto nachrichtenFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return nachrichtenFac.nachrichtenFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public Timestamp getAnlageDatumDerNachricht(Integer nachrichtenempfaengeriId) throws ExceptionLP {

		try {
			return nachrichtenFac.getAnlageDatumDerNachricht(nachrichtenempfaengeriId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public Integer createNachrichtengruppe(NachrichtengruppeDto nachrichtengruppeDto) throws ExceptionLP {

		try {
			return nachrichtenFac.createNachrichtengruppe(nachrichtengruppeDto);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public ArrayList<Map<Integer, String>> getListeDerNachrichtenFuerEinePerson(Integer personalIId,
			Integer personalIIdAbsender, String suche, boolean bNurEmpfangene) throws ExceptionLP {

		try {
			return nachrichtenFac.getListeDerNachrichtenFuerEinePerson(personalIId, personalIIdAbsender, suche, bNurEmpfangene,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public String getBetreff(Integer nachrichtenempaengerIId) throws ExceptionLP {

		try {
			return nachrichtenFac.getBetreff(nachrichtenempaengerIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public ArrayList<Map<Integer, String>> getListeDerNachrichtenFuerEinePerson(Integer personalIId, int iAnzahl,
			Integer personalIIdAbsender, String suche, boolean bNurEmpfangene) throws ExceptionLP {

		try {
			return nachrichtenFac.getListeDerNachrichtenFuerEinePerson(personalIId, iAnzahl, false, personalIIdAbsender,
					suche, bNurEmpfangene,LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public ArrayList<Map<Integer, String>> getListeDerNachrichtenFuerEinePerson(Integer personalIId,
			boolean nurNugelesene, int iAnzahl, Integer personalIIdAbsender, String suche, boolean bNurEmpfangene) throws ExceptionLP {

		try {
			return nachrichtenFac.getListeDerNachrichtenFuerEinePerson(personalIId, iAnzahl, nurNugelesene,
					personalIIdAbsender, suche,bNurEmpfangene,LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public Integer createNachrichtengruppeteilnehmer(NachrichtengruppeteilnehmerDto dto) throws ExceptionLP {

		try {
			return nachrichtenFac.createNachrichtengruppeteilnehmer(dto);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public Integer createNachrichtenabo(NachrichtenaboDto nachrichtenaboDto) throws ExceptionLP {

		try {
			return nachrichtenFac.createNachrichtenabo(nachrichtenaboDto);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public NachrichtengruppeDto nachrichtengruppeFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return nachrichtenFac.nachrichtengruppeFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public NachrichtengruppeteilnehmerDto nachrichtengrupperteilnehmerFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return nachrichtenFac.nachrichtengrupperteilnehmerFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}
}
