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
import javax.naming.NamingException;

import com.lp.server.system.service.JobDetailsDto;
import com.lp.server.system.service.JobDetailsFac;
import com.lp.util.EJBExceptionLP;


/**
 * Generische Delegate-Klasse fuer Automatikjobs mit generischer Standardfunktionalitaet
 * von Datenbankabfragen
 * 
 * @author andi
 *
 * @param <T>
 * @param <I>
 */
public class AutoJobDelegate<T extends JobDetailsDto, I extends JobDetailsFac<T>> extends Delegate {
	
	private Context context;
	private JobDetailsFac<T> jobDetailsFac;
	private Class<I> facName;
	
	private AutoJobDelegate() {
	}

	protected AutoJobDelegate(Class<I> facName) {
		this.facName = facName;
	}

	protected Context getContext() throws NamingException {
		if (context == null) {
			context = new InitialContext();
		}
		
		return context;
	}
	
	protected JobDetailsFac<T> getFac() {
		try {
			if (jobDetailsFac == null) {
				jobDetailsFac = lookupFac(getContext(), facName);
			}
			return jobDetailsFac;
		} catch (NamingException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NAMING_EXCEPTION, e);
		}
	}

	public T findByMandantCNr(String mandantCNr) {
		return getFac().findByMandantCNr(mandantCNr);
	}
	
	public T findByPrimaryKey(Integer iId) {
		return getFac().findByPrimaryKey(iId);
	}
	
	public Integer create(T dto) {
		return getFac().create(dto);
	}
	
	public void remove(Integer iId) {
		getFac().remove(iId);
	}
	
	public void update(T dto) {
		getFac().update(dto);
	}
	
	public T findByMandantCNrNoEx(String mandantCNr) {
		return getFac().findByMandantCNrNoEx(mandantCNr);
	}
}
