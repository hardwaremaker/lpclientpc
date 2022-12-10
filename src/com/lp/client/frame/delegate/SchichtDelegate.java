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

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.server.personal.service.SchichtDto;
import com.lp.server.personal.service.SchichtFac;
import com.lp.server.personal.service.SchichtzeitDto;
import com.lp.server.personal.service.SchichtzuschlagDto;
import com.lp.util.EJBExceptionLP;

public class SchichtDelegate extends Delegate {
	private Context context;
	private SchichtFac schichtFac;

	public SchichtDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			schichtFac = lookupFac(context, SchichtFac.class);
		} catch (Throwable t) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t);
		}
	}

	public Integer createSchicht(SchichtDto dto) throws ExceptionLP {
		try {
			return schichtFac.createSchicht(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeSchicht(SchichtDto dto) throws ExceptionLP {
		try {
			schichtFac.removeSchicht(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSchicht(SchichtDto dto) throws ExceptionLP {
		try {
			schichtFac.updateSchicht(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public SchichtDto schichtFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return schichtFac.schichtFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createSchichtzuschlag(SchichtzuschlagDto dto)
			throws ExceptionLP {
		try {
			return schichtFac.createSchichtzuschlag(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeSchichtzuschlag(SchichtzuschlagDto dto)
			throws ExceptionLP {
		try {
			schichtFac.removeSchichtzuschlag(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSchichtzuschlag(SchichtzuschlagDto dto)
			throws ExceptionLP {
		try {
			schichtFac.updateSchichtzuschlag(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public SchichtzuschlagDto schichtzuschlagFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return schichtFac.schichtzuschlagFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createSchichtzeit(SchichtzeitDto dto) throws ExceptionLP {
		try {
			return schichtFac.createSchichtzeit(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeSchichtzeit(SchichtzeitDto dto) throws ExceptionLP {
		try {
			schichtFac.removeSchichtzeit(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSchichtzeit(SchichtzeitDto dto) throws ExceptionLP {
		try {
			schichtFac.updateSchichtzeit(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public SchichtzeitDto schichtzeitFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return schichtFac.schichtzeitFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
}
