
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
import com.lp.server.system.service.BelegartmediaDto;
import com.lp.server.system.service.BelegartmediaFac;
import com.lp.server.system.service.TheClientDto;

@SuppressWarnings("static-access")
/*
 * <p>BusinessDelegate fuer die Druckereinstellungen</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Martin Bluehweis; 25.04.06</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2010/02/26 09:37:37 $
 */
public class BelegartmediaDelegate extends Delegate {
	private Context context;
	private BelegartmediaFac belegartmediaFac;

	public BelegartmediaDelegate() throws Exception {
		context = new InitialContext();
		belegartmediaFac = lookupFac(context, BelegartmediaFac.class);

	}

	public ArrayList<BelegartmediaDto> getBelegartMediaDtos(Integer usecaseId, Integer iKey) throws ExceptionLP {
		try {
			return belegartmediaFac
					.getBelegartMediaDtos(usecaseId, iKey, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	
	public boolean sindMedienVorhanden(Integer usecaseId, Integer iKey) throws ExceptionLP {
		try {
			return belegartmediaFac
					.sindMedienVorhanden(usecaseId, iKey, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return true;
		}
	}
	
	public ArrayList<Integer> syncBelegartmediaDtos(Integer usecaseId, Integer iKey, ArrayList<BelegartmediaDto> belegartmediaDtos) throws ExceptionLP {
		try {
			return belegartmediaFac
					.syncBelegartmediaDtos(usecaseId, iKey, belegartmediaDtos, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	public void removeBelegartmedia(Integer usecaseId, Integer iKey) throws ExceptionLP {
		try {
			belegartmediaFac
					.removeBelegartmedia(usecaseId, iKey, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			
		}
	}

}
