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

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ExtralisteRueckgabeTabelleDto;
import com.lp.server.system.service.MergePrintTypeParams;
import com.lp.server.system.service.SystemReportFac;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JasperPrint;

public class SystemReportDelegate extends Delegate {
	private Context context;
	private SystemReportFac systemReportFac;

	public SystemReportDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			systemReportFac = lookupFac(context, SystemReportFac.class);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public JasperPrintLP printUseCaseHandler(String uuid, QueryParameters q, int iAnzahlZeilen, String ueberschrift,
			int[] columnHeaderWidthsFromClientPerspective) throws ExceptionLP {
		try {

			return systemReportFac.printUseCaseHandler(uuid, q, iAnzahlZeilen, ueberschrift,
					columnHeaderWidthsFromClientPerspective, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printDashboard() throws ExceptionLP {
		try {

			return systemReportFac.printDashboard(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printStatistik(DatumsfilterVonBis vonBis, Integer iOption, String sOption) throws ExceptionLP {
		try {

			return systemReportFac.printStatistik(vonBis, iOption, sOption, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printEntitylog(String filterKey, String filterId, String cDatensatz) throws ExceptionLP {
		try {
			return systemReportFac.printEntitylog(filterKey, filterId, cDatensatz, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printModulberechtigungen() throws ExceptionLP {
		try {
			return systemReportFac.printModulberechtigungen(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP printExtraliste(ExtralisteRueckgabeTabelleDto extralisteRueckgabeTabelleDto,
			Integer extralisteIId) throws ExceptionLP {
		try {
			return systemReportFac.printExtraliste(extralisteRueckgabeTabelleDto, extralisteIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrintLP[] printVersandAuftrag(VersandauftragDto versandauftragDto, Integer iAnzahlKopien)
			throws ExceptionLP {
		myLogger.entry();
		JasperPrintLP[] print = null;
		try {
			print = systemReportFac.printVersandAuftrag(versandauftragDto, iAnzahlKopien, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
		return print;
	}

	public List<JRPrintElement> getReportCopy(JasperPrint originalPrint, String modul) throws ExceptionLP {
		try {
			return systemReportFac.getReportCopy(originalPrint, modul, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public JasperPrint mergeWithPrintTypePrint(JasperPrint originalPrint, String druckType) throws ExceptionLP {
		try {
			return systemReportFac.mergeWithPrintTypePrint(new MergePrintTypeParams(originalPrint, druckType),
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}
}
