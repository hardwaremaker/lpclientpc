
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

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechtDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.benutzer.service.RollerechtDto;
import com.lp.server.forecast.service.ForecastReportFac;
import com.lp.server.personal.ejb.HvmaparameterPK;
import com.lp.server.personal.service.HvmaFac;
import com.lp.server.personal.service.HvmaLizenzEnum;
import com.lp.server.personal.service.HvmabenutzerDto;
import com.lp.server.personal.service.HvmabenutzerParameterDto;
import com.lp.server.personal.service.HvmalizenzDto;
import com.lp.server.personal.service.HvmaparameterDto;
import com.lp.server.personal.service.HvmarechtDto;
import com.lp.server.personal.service.HvmarolleDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.report.JasperPrintLP;

public class HvmaDelegate extends Delegate {
	private Context context;
	private HvmaFac hvmaFac;

	public HvmaDelegate() throws Exception {
		context = new InitialContext();
		hvmaFac = lookupFac(context, HvmaFac.class);
	}

	public JasperPrintLP printLetzteSynchronisation(DatumsfilterVonBis datumsfilter) throws Throwable {
		try {
			return hvmaFac.printLetzteSynchronisation(datumsfilter, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createHvmabenutzer(HvmabenutzerDto dto) throws ExceptionLP {
		try {
			return hvmaFac.createHvmabenutzer(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createHvmarolle(HvmarolleDto dto) throws ExceptionLP {
		try {
			return hvmaFac.createHvmarolle(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	public void removeHvmabenutzer(HvmabenutzerDto dto) throws ExceptionLP {

		try {
			hvmaFac.removeHvmabenutzer(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}
	public void removeHvmabenutzerparameter(HvmabenutzerParameterDto dto) throws ExceptionLP {

		try {
			hvmaFac.removeHvmabenutzerparameter(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}
	public void removeHvmarolle(HvmarolleDto dto) throws ExceptionLP {

		try {
			hvmaFac.removeHvmarolle(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateHvmabenutzer(HvmabenutzerDto dto) throws ExceptionLP {

		try {
			hvmaFac.updateHvmabenutzer(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateHvmarolle(HvmarolleDto dto) throws ExceptionLP {

		try {
			hvmaFac.updateHvmarolle(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public HvmabenutzerDto hvmabenutzerFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return hvmaFac.hvmabenutzerFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<HvmarechtDto> getHvmaRechte(String hvmalizenzCNr) throws ExceptionLP {

		try {
			return hvmaFac.getHvmaRechte(hvmalizenzCNr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public HvmalizenzDto hvmalizenzFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return hvmaFac.hvmalizenzFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public HvmalizenzDto hvmalizenzFindByEnum(HvmaLizenzEnum licence) throws ExceptionLP {

		try {
			return hvmaFac.hvmalizenzFindByEnum(licence);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public HvmarechtDto hvmarechtFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return hvmaFac.hvmarechtFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	
	public HvmabenutzerParameterDto hvmabenutzerparameterFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return hvmaFac.hvmabenutzerparameterFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	
	public void updateHvmabenutzerparameter(HvmabenutzerParameterDto dto) throws ExceptionLP {

		try {
			hvmaFac.updateHvmabenutzerparameter(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}
	
	public Integer createHvmabenutzerparameter(HvmabenutzerParameterDto dto) throws ExceptionLP {

		try {
			return hvmaFac.createHvmabenutzerparameter(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public HvmaparameterDto parameterFindByPrimaryKey(HvmaparameterPK pk) throws ExceptionLP {

		try {
			return hvmaFac.parameterFindByPrimaryKey(pk);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void updateParameter(HvmaparameterDto dto) throws ExceptionLP {

		try {
			hvmaFac.updateParameter(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public HvmarolleDto hvmarolleFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return hvmaFac.hvmarolleFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

}
