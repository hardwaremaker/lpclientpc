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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.forecast.service.CallOffXlsImporterResult;
import com.lp.server.forecast.service.EdiFileInfo;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastImportFehlerDto;
import com.lp.server.forecast.service.ForecastImportZeissDto;
import com.lp.server.forecast.service.ForecastartDto;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.forecast.service.ImportdefDto;
import com.lp.server.forecast.service.KommdruckerDto;
import com.lp.server.forecast.service.LinienabrufDto;
import com.lp.server.system.service.ForecastimportFac;
import com.lp.server.system.service.IForecastImportFile;

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
public class ForecastDelegate extends Delegate {
	private Context context;
	private ForecastFac forecastFac;
	private ForecastimportFac forecastimportFac;

	public ForecastDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			forecastFac = lookupFac(context, ForecastFac.class);
			forecastimportFac = lookupFac(context, ForecastimportFac.class);
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public Integer createForecast(ForecastDto dto) throws ExceptionLP {

		try {
			return forecastFac.createForecast(dto);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public Integer createFclieferadresse(FclieferadresseDto dto) throws ExceptionLP {

		try {
			return forecastFac.createFclieferadresse(dto);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public boolean sindBereitsPositionenVorhanden(Integer forecastauftragIId) throws ExceptionLP {

		try {
			return forecastFac.sindBereitsPositionenVorhanden(forecastauftragIId);
		} catch (Throwable t) {
			handleThrowable(t);
			return true;
		}

	}

	public BigDecimal getSummeLinienabrufe(Integer forecastpositionIId) throws ExceptionLP {

		try {
			return forecastFac.getSummeLinienabrufe(forecastpositionIId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public boolean sindBereitsLieferscheinpositionenVorhanden(Integer forecastpositionIId) throws ExceptionLP {

		try {
			return forecastFac.sindBereitsLieferscheinpositionenVorhanden(forecastpositionIId);
		} catch (Throwable t) {
			handleThrowable(t);
			return true;
		}

	}

	public Integer createForecastposition(ForecastpositionDto dto) throws ExceptionLP {

		try {
			return forecastFac.createForecastposition(dto);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public Integer getAktuellFreigegebenenForecastauftragEinerLieferadresse(Integer kundeIIdLieferadresse)
			throws ExceptionLP {

		try {
			return forecastFac.getAktuellFreigegebenenForecastauftragEinerLieferadresse(kundeIIdLieferadresse);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public boolean gibtEsDenArtikelIneinemOffenenOderFreigegebenenForecastauftrag(Integer artikelIId,
			Integer kundeIIdLieferadresse) throws ExceptionLP {

		try {
			return forecastFac.gibtEsDenArtikelIneinemOffenenOderFreigegebenenForecastauftrag(artikelIId,
					kundeIIdLieferadresse);
		} catch (Throwable t) {
			handleThrowable(t);
			return true;
		}

	}

	public Integer createLinienabruf(LinienabrufDto dto) throws ExceptionLP {

		try {
			return forecastFac.createLinienabruf(dto);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public Integer createKommdrucker(KommdruckerDto dto) throws ExceptionLP {

		try {
			return forecastFac.createKommdrucker(dto);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public Integer createForecastauftrag(ForecastauftragDto dto) throws ExceptionLP {

		try {
			return forecastFac.createForecastauftrag(dto);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public Map getAllForecastart() throws ExceptionLP {

		try {
			return forecastFac.getAllForecastart(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public Map getAllKommdrucker() throws ExceptionLP {

		try {
			return forecastFac.getAllKommdrucker(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public Map getAllArtikelEinesForecastAuftrags(Integer forecastauftragIId) throws ExceptionLP {

		try {
			return forecastFac.getAllArtikelEinesForecastAuftrags(forecastauftragIId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public Map getAllImportdef() throws ExceptionLP {

		try {
			return forecastFac.getAllImportdef(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public String getForecastartEienrForecastposition(Integer forecastpositionIId) throws ExceptionLP {

		try {
			return forecastFac.getForecastartEienrForecastposition(forecastpositionIId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public void updateForecastauftrag(ForecastauftragDto dto) throws ExceptionLP {

		try {
			forecastFac.updateForecastauftrag(dto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public TreeMap<String, BigDecimal> toggleFreigabe(Integer forecastauftragIId) throws ExceptionLP {

		try {
			return forecastFac.toggleFreigabe(forecastauftragIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;

		}

	}

	public void toggleForecastErledigt(Integer forecastIId) throws ExceptionLP {

		try {
			forecastFac.toggleForecastErledigt(forecastIId);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void updateForecast(ForecastDto dto) throws ExceptionLP {

		try {
			forecastFac.updateForecast(dto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void updateKommdrucker(KommdruckerDto dto) throws ExceptionLP {

		try {
			forecastFac.updateKommdrucker(dto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void updateImportdef(ImportdefDto dto) throws ExceptionLP {

		try {
			forecastFac.updateImportdef(dto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void updateLinienabruf(LinienabrufDto dto) throws ExceptionLP {

		try {
			forecastFac.updateLinienabruf(dto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void updateForecastart(ForecastartDto forecastartDto) throws ExceptionLP {

		try {
			forecastFac.updateForecastart(forecastartDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void updateForecastposition(ForecastpositionDto dto) throws ExceptionLP {

		try {
			forecastFac.updateForecastposition(dto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void updateFclieferadresse(FclieferadresseDto dto) throws ExceptionLP {

		try {
			forecastFac.updateFclieferadresse(dto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public ForecastauftragDto forecastauftragFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return forecastFac.forecastauftragFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public ForecastDto forecastFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return forecastFac.forecastFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public KommdruckerDto kommdruckerFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return forecastFac.kommdruckerFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public LinienabrufDto linienabrufFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return forecastFac.linienabrufFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public ImportdefDto importdefFindByPrimaryKey(String cNr) throws ExceptionLP {

		try {
			return forecastFac.importdefFindByPrimaryKey(cNr, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public ForecastartDto forecastartFindByPrimaryKey(String cNrI) throws ExceptionLP {

		try {
			return forecastFac.forecastartFindByPrimaryKey(cNrI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public ForecastpositionDto forecastpositionFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return forecastFac.forecastpositionFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public FclieferadresseDto fclieferadresseFindByPrimaryKey(Integer iId) throws ExceptionLP {

		try {
			return forecastFac.fclieferadresseFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public void removeForecastauftrag(ForecastauftragDto dto) throws ExceptionLP {

		try {
			forecastFac.removeForecastauftrag(dto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void removeForecastposition(ForecastpositionDto dto) throws ExceptionLP {

		try {
			forecastFac.removeForecastposition(dto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void removeFclieferadresse(FclieferadresseDto dto) throws ExceptionLP {

		try {
			forecastFac.removeFclieferadresse(dto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void removeForecast(ForecastDto dto) throws ExceptionLP {

		try {
			forecastFac.removeForecast(dto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void removeLinienabruf(LinienabrufDto dto) throws ExceptionLP {

		try {
			forecastFac.removeLinienabruf(dto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public void removeKommdrucker(KommdruckerDto dto) throws ExceptionLP {

		try {
			forecastFac.removeKommdrucker(dto);
		} catch (Throwable t) {
			handleThrowable(t);

		}

	}

	public String pruefeUndImportiereForecastpositionXLS(byte[] xlsDatei, Integer forecastIId,
			boolean bImportierenWennKeinFehler) throws ExceptionLP {

		try {
			return forecastimportFac.pruefeUndImportiereForecastpositionXLS(xlsDatei, forecastIId,
					bImportierenWennKeinFehler, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	
	public String pruefeUndImportiereForecastpositionXLS_VAT(byte[] xlsDatei, Integer forecastIId,
			boolean bImportierenWennKeinFehler) throws ExceptionLP {

		try {
			return forecastimportFac.pruefeUndImportiereForecastpositionXLS_VAT(xlsDatei, forecastIId,
					bImportierenWennKeinFehler, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}
	public int importiereVMI_XLS(byte[] xlsDatei) throws ExceptionLP {

		try {

			int i = forecastimportFac.importiereVMI_XLS(xlsDatei, LPMain.getTheClient());
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
					LPMain.getMessageTextRespectUISPr("fc.import.anzahl.importiert.alle", i));

			return i;
		} catch (Throwable t) {
			handleThrowable(t);
			return -1;
		}

	}
	
	public int importiereVMI_CC_XLS(byte[] xlsDatei, Integer forecastauftragIId) throws ExceptionLP {

		try {

			int i = forecastimportFac.importiereVMI_CC_XLS(xlsDatei, forecastauftragIId, LPMain.getTheClient());
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
					LPMain.getMessageTextRespectUISPr("fc.import.anzahl.importiert.alle", i));

			return i;
		} catch (Throwable t) {
			handleThrowable(t);
			return -1;
		}

	}
	
	public ForecastImportFehlerDto importiereRollierendePlanung_XLS(byte[] xlsDatei, Integer forecastauftragIId, boolean referenznummer, boolean bImportiereWennKeinFehler) throws ExceptionLP {
		try {
			return forecastimportFac.importiereRollierendePlanung_XLS(xlsDatei, forecastauftragIId, referenznummer,bImportiereWennKeinFehler, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
			
		}
	}

	public int importiereEpsilon_XLS(byte[] xlsDatei,Integer forecastauftragIId) throws ExceptionLP {

		try {
			int i = forecastimportFac.importiereEpsilon_XLS(xlsDatei,forecastauftragIId, LPMain.getTheClient());
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
					LPMain.getMessageTextRespectUISPr("fc.import.anzahl.importiert", i));
			return i;
		} catch (Throwable t) {
			handleThrowable(t);
			return -1;
		}

	}
	
	public void importiereForecastpositionZEISS_CSV(ArrayList<ForecastImportZeissDto> alZeilenCsv,Integer forecastauftragIId) throws ExceptionLP {

		try {
			forecastimportFac.importiereForecastpositionZEISS_CSV(alZeilenCsv, forecastauftragIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			
		}

	}
	

	public CallOffXlsImporterResult importCallOffDailyXls(byte[] xlsDatei, Integer forecastIId, boolean checkOnly,
			Integer startRow) throws ExceptionLP {

		try {
			return forecastimportFac.importCallOffDailyXls(forecastIId, xlsDatei, checkOnly, startRow,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}

	}

	public CallOffXlsImporterResult importCallOffWeeklyXls(byte[] xlsDatei, Integer fclieferadresseIId,
			boolean checkOnly, Integer startRow) throws ExceptionLP {

		try {
			return forecastimportFac.importCallOffWeeklyXls(fclieferadresseIId, xlsDatei, checkOnly, startRow,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public CallOffXlsImporterResult importLinienabrufEluxDe(List<String> inputLines, Integer fclieferadresseIId,
			Date deliveryDate, boolean checkOnly) throws ExceptionLP {

		try {
			return forecastimportFac.importLinienabrufEluxDeTxt(fclieferadresseIId, inputLines, checkOnly, deliveryDate,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public CallOffXlsImporterResult importDelforEdifact(Integer forecastId, List<EdiFileInfo> ediContent,
			boolean checkOnly) throws ExceptionLP {
		try {
			return forecastimportFac.importDelforEdifact(forecastId, ediContent, checkOnly, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public CallOffXlsImporterResult importFiles(Integer fclieferadresseId, List<IForecastImportFile<?>> files,
			boolean checkOnly) throws ExceptionLP {
		try {
			return forecastimportFac.importFiles(files, fclieferadresseId, checkOnly, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public List<String> getStatiEinerForecastposition() throws ExceptionLP {
		try {
			return forecastFac.getStatiEinerForecastposition();
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}
}
