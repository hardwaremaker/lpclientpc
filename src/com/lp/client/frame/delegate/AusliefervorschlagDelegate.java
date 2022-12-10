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

import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.lieferschein.service.AusliefervorschlagDto;
import com.lp.server.lieferschein.service.AusliefervorschlagFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;

public class AusliefervorschlagDelegate extends Delegate {
	private Context context;
	private AusliefervorschlagFac ausliefervorschlagFac;

	public AusliefervorschlagDelegate() throws Throwable {
		context = new InitialContext();
		ausliefervorschlagFac = lookupFac(context, AusliefervorschlagFac.class);

	}

	public Integer createAusliefervorschlag(
			AusliefervorschlagDto ausliefervorschlagDto) throws ExceptionLP {
		Integer ausliefervorschlagIId = null;
		try {
			ausliefervorschlagIId = ausliefervorschlagFac
					.createAusliefervorschlag(ausliefervorschlagDto,
							LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return ausliefervorschlagIId;
	}

	public JasperPrintLP printAusliefervorschlag(Integer kundeIId,
			Integer kundeIId_Lieferadresse, int iSortierung) throws ExceptionLP {
		try {
			return ausliefervorschlagFac.printAusliefervorschlag(kundeIId,
					kundeIId_Lieferadresse, iSortierung, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public String ausliefervorschlagUeberleiten(Set<Integer> iids,
			Integer kundeIId, Integer kundeIIdLieferadresse) throws ExceptionLP {

		try {
			return ausliefervorschlagFac.ausliefervorschlagUeberleiten(iids, kundeIId,
					kundeIIdLieferadresse, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void erstelleAusliefervorschlag(java.sql.Date dAusliefertermin)
			throws ExceptionLP {
		try {
			ausliefervorschlagFac.erstelleAusliefervorschlag(dAusliefertermin,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void verfuegbareMengeBerechnen() throws ExceptionLP {
		try {
			ausliefervorschlagFac.verfuegbareMengeBerechnen(LPMain
					.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeAusliefervorschlag(
			AusliefervorschlagDto ausliefervorschlagDto) throws ExceptionLP {
		try {
			ausliefervorschlagFac
					.removeAusliefervorschlag(ausliefervorschlagDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateAusliefervorschlag(
			AusliefervorschlagDto ausliefervorschlagDto) throws ExceptionLP {
		try {
			ausliefervorschlagFac.updateAusliefervorschlag(
					ausliefervorschlagDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public AusliefervorschlagDto ausliefervorschlagFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		AusliefervorschlagDto dto = null;
		try {
			dto = ausliefervorschlagFac.ausliefervorschlagFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return dto;
	}

	public Set<Integer> getKundenIIsEinesAusliefervorschlages(
			Set<Integer> iids, boolean bLieferadresse) throws ExceptionLP {

		try {
			return ausliefervorschlagFac.getKundenIIsEinesAusliefervorschlages(
					iids, bLieferadresse, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}
	
	public void removeLockDesAusliefervorschlagesWennIchIhnSperre()
			throws ExceptionLP {
		try {
			ausliefervorschlagFac
					.removeLockDesAusliefervorschlagesWennIchIhnSperre(LPMain
							.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void pruefeBearbeitenDesAusliefervorschlagsErlaubt()
			throws ExceptionLP {
		try {
			ausliefervorschlagFac
					.pruefeBearbeitenDesAusliefervorschlagsErlaubt(LPMain
							.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	

}