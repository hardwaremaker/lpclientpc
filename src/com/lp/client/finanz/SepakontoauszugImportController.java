/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2016 HELIUM V IT-Solutions GmbH
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
package com.lp.client.finanz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.view.IDataUpdateListener;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.ISepaCamtFormat;
import com.lp.server.finanz.service.SepaCamtFormat052;
import com.lp.server.finanz.service.SepaCamtFormat053;
import com.lp.server.finanz.service.SepaImportProperties;
import com.lp.server.finanz.service.SepaImportSourceData;
import com.lp.server.finanz.service.SepaImportTransformResult;
import com.lp.server.partner.service.PartnerDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBSepaImportExceptionLP;

public class SepakontoauszugImportController implements	ISepakontoauszugImportController {
	public static final String ARCHIVE_DIRECTORY = "old";

	private SepaImportProperties importProperties;
	private PartnerDto bvPartnerDto;
	private List<ISepaCamtFormat> camtFormate;
	private Integer camtSelection;
	private IDataUpdateListener listener;
	private List<EJBSepaImportExceptionLP> messages;
	private boolean bViewShouldClose = false;
	private Map<Integer, String> severityMap;
	private SepaImportTransformResult importResult;
	private List<File> filesForImport;

	public SepakontoauszugImportController(InternalFrame internalFrame, BankverbindungDto bankverbindungDto) throws ExceptionLP {
		importProperties = new SepaImportProperties();
		setBankverbindungData(bankverbindungDto);
		messages = new ArrayList<EJBSepaImportExceptionLP>();
		camtFormate = new ArrayList<ISepaCamtFormat>();
		camtFormate.add(new SepaCamtFormat052());
		camtFormate.add(new SepaCamtFormat053());
		initCamtSelection();

		severityMap = new HashMap<Integer, String>();	
		severityMap.put(EJBSepaImportExceptionLP.SEVERITY_INFO, "INFO");
		severityMap.put(EJBSepaImportExceptionLP.SEVERITY_WARNING, "WARN");
		severityMap.put(EJBSepaImportExceptionLP.SEVERITY_ERROR, "ERR ");
	}

	private void initCamtSelection() {
		camtSelection = 1;
	}

	private void setBankverbindungData(BankverbindungDto bankverbindungDto) throws ExceptionLP {
		importProperties.setBankverbindungDto(bankverbindungDto);
		bvPartnerDto = DelegateFactory.getInstance().getPartnerDelegate()
				.partnerFindByPrimaryKey(bankverbindungDto.getBankIId());
	}
	
	private BankverbindungDto getBankverbindungDto() {
		return importProperties.getBankverbindungDto();
	}
	
	public void registerDataUpdateListener(IDataUpdateListener listener) {
		this.listener = listener;
	}
	
	private void notifyDataUpdateListener() {
		if (listener != null) listener.dataUpdated();
	}

	@Override
	public String getBankverbindungName() {
		return bvPartnerDto.getCName1nachnamefirmazeile1() + ", " + importProperties.getBankverbindungDto().getCIban();
	}

	@Override
	public String[] getCamtFormate() {
		List<String> camtFormate = new ArrayList<String>();
		camtFormate.add(LPMain.getTextRespectUISPr("fb.sepa.import.camtformat052"));
		camtFormate.add(LPMain.getTextRespectUISPr("fb.sepa.import.camtformat053"));
		
		return camtFormate.toArray(new String[camtFormate.size()]);
	}

	@Override
	public void setAuszugsnummer(Integer iAuszug) {
		importProperties.setAuszugsnummer(iAuszug);
		notifyDataUpdateListener();
	}

	private ISepaCamtFormat getSelectedCamtFormat() {
		return camtFormate.get(camtSelection);
	}

	@Override
	public int getCamtFormatSelection() {
		return camtSelection;
	}

	@Override
	public boolean isImportPossible() {
		return true;
	}

	@Override
	public void setSelectedCamtFormat(int selectedIndex) {
		this.camtSelection = selectedIndex;
		notifyDataUpdateListener();
	}

	@Override
	public boolean shouldShowAuszugsnummer() {
		return getSelectedCamtFormat() instanceof SepaCamtFormat052;
	}
	
	public void handleException(Throwable throwable) {
		setBViewShouldClose(true);
		notifyDataUpdateListener();
		new DialogError(LPMain.getInstance().getDesktop(), throwable, DialogError.TYPE_INFORMATION);
	}
	
	public void actionImport() {
		loadFiles();
	}

	protected void loadFiles() {
		
		readFiles();
		if (!messages.isEmpty()) {
			notifyDataUpdateListener();
			return;
		}
		
		if (!importProperties.getSources().isEmpty()) {
			try {
				SepaImportTransformResult result = DelegateFactory.getInstance().getFinanzDelegate()
						.readAndTransformSepaKontoauszug(importProperties);
				setImportResult(result);
				if (result.wasImportErfolgreich()) {
					moveImportFilesToArchive();
				}
				notifyDataUpdateListener();
			} catch (EJBSepaImportExceptionLP ex) {
				messages.add(ex);
			} catch (ExceptionLP ex) {
				handleException(ex);
			} catch (IOException ex) {
				handleException(ex);
			}
		}
		
	}
	
	private void setImportResult(SepaImportTransformResult result) {
		this.importResult = result;
		messages = result.getWarnings();
	}

	private void setBViewShouldClose(boolean bValue) {
		bViewShouldClose = bValue;
	}

	/**
	 * List ein XML-File aus dem uebergebenen Verzeichnis aus.
	 * 
	 * @param filename
	 */
	private void readFiles() {
		File folder = new File(getBankverbindungDto().getCSepaVerzeichnis());
		
		if (folder.listFiles() == null) {
			messages.add(new EJBSepaImportExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_DIRECTORY_NOT_FOUND, 
					EJBSepaImportExceptionLP.SEVERITY_ERROR, 
					LPMain.getMessageTextRespectUISPr("fb.sepa.import.error.sepaverzeichnisnichtgefunden", new Object[]{folder.getAbsolutePath()})));
			return;
		}

		List<File> filesInFolder = Arrays.asList(folder.listFiles());
		filesForImport = new ArrayList<File>();
		for (File file : filesInFolder) {
			if (file.isFile() && file.getName().toLowerCase().endsWith(".xml")) {
				filesForImport.add(file);
			}
		}
		
		if (filesForImport.size() == 0) {
			messages.add(new EJBSepaImportExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_KEINE_XML_DATEI_VORHANDEN, 
					EJBSepaImportExceptionLP.SEVERITY_ERROR, 
					LPMain.getMessageTextRespectUISPr("fb.sepa.import.error.keinexmldatei", new Object[]{folder.getAbsolutePath()})));
			return;
		}
		
		BufferedReader reader = null;
		for (File importFile : filesForImport) {
			try {
				FileInputStream fInputStream = new FileInputStream(importFile);
				InputStreamReader inputStreamReader = new InputStreamReader(fInputStream, "UTF-8");
				reader = new BufferedReader(inputStreamReader);
				StringBuilder builder = new StringBuilder();
				String line = reader.readLine();
				
				while (line != null) {
					builder.append(line);
					line = reader.readLine();
				}
				
				importProperties.getSources().add(new SepaImportSourceData(importFile.getName(), builder.toString()));
			} catch (FileNotFoundException e) {
				messages.add(new EJBSepaImportExceptionLP(
						EJBSepaImportExceptionLP.SEVERITY_ERROR, new EJBExceptionLP(e)));
			} catch (IOException e) {
				messages.add(new EJBSepaImportExceptionLP(
						EJBSepaImportExceptionLP.SEVERITY_ERROR, new EJBExceptionLP(e)));
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						messages.add(new EJBSepaImportExceptionLP(EJBSepaImportExceptionLP.SEVERITY_ERROR, new EJBExceptionLP(e)));
					}
				}
			}
		}
	}

	@Override
	public String getImportMessages() {
		StringBuilder builder = new StringBuilder();
		builder.append(getSourceDataString());
		for (EJBSepaImportExceptionLP warning : messages) {
			String message = getMessage(warning);
			builder.append(severityMap.get(warning.getSeverity()));
			builder.append(": ");
			builder.append(message != null ? message : warning.getCause().getMessage());
			builder.append("\n");
		}
		if (importResult != null && importResult.wasImportErfolgreich()) {
			builder.append(LPMain.getTextRespectUISPr("finanz.sepakontoauszug.import.importerfolgreich"));
		}

		return builder.toString();
	}
	
	private String getSourceDataString() {
		if (importResult == null) return "";
		
		StringBuilder builder = new StringBuilder();
		for (SepaImportSourceData source : importResult.getSources()) {
			int stmtCount = source.getStmtNumbers().size();
			String msgToken = stmtCount > 1
					? "finanz.sepakontoauszug.import.datei.auszuege"
					: "finanz.sepakontoauszug.import.datei.auszug";
			String auszuege = StringUtils.join(source.getStmtNumbers().toArray(), ", ");
			builder.append(LPMain.getMessageTextRespectUISPr(msgToken, source.getFilename(), auszuege)).append("\n");
		}
		builder.append("- - - - - - - -\n");
		return builder.toString();
	}

	private String getMessage(EJBSepaImportExceptionLP warning) {
		List<Object> objects = warning.getAlInfoForTheClient();
		switch (warning.getCode()) {
			case EJBExceptionLP.FEHLER_SEPAIMPORT_KTOINFO_NICHT_ALS_BANKVERBINDUNG_BEKANNT: {
				if (objects != null && objects.size() == 2) {
					return LPMain.getMessageTextRespectUISPr(
							"fb.sepa.import.error.empfaengeribanstimmtnichtueberein", objects.toArray());
				}
				break;
			}
			case EJBExceptionLP.FEHLER_SEPAIMPORT_CAMT053_VERSION_NICHT_UNTERSTUETZT: {
				return LPMain.getMessageTextRespectUISPr(
						"fb.sepa.import.error.camtversionnichtunterstuetzt", 
						new Object[] {warning.getCamt053Version()});
			}
			case EJBExceptionLP.FEHLER_SEPAIMPORT_AUSGEWAEHLTES_CAMT_FORMAT_STIMMT_NICHT_UEBEREIN: {
				return LPMain.getMessageTextRespectUISPr(
						"fb.sepa.import.error.camtformat", 
						new Object[] {warning.getCamt053Version()});
			}
			case EJBExceptionLP.FEHLER_SEPAIMPORT_CAMT052_WIRD_NICHT_UNTERSTUETZT: {
				return LPMain.getTextRespectUISPr("fb.sepa.import.error.dateiistcamt052");
			}
			case EJBExceptionLP.FEHLER_SEPAIMPORT_UNBEKANNTE_XML_DATEI: {
				return LPMain.getTextRespectUISPr("fb.sepa.import.error.unbekanntexmldatei");
			}
			case EJBExceptionLP.FEHLER_SEPAIMPORT_KTOAUSZUG_HAT_KEINE_SALDEN: {
				return LPMain.getTextRespectUISPr("fb.sepa.import.error.kontoauszughatkeinesalden");
			}
			case EJBExceptionLP.FEHLER_SEPAIMPORT_ENDE_DES_KONTOAUSZUGS_NICHT_ERKANNT: {
				return LPMain.getMessageTextRespectUISPr(
						"fb.sepa.import.error.endedeskontoauszugnichterkannt", 
						objects.toArray());
			}
			case EJBExceptionLP.FEHLER_SEPAIMPORT_SEITENNUMMERN_INKONSISTENT: {
				return LPMain.getMessageTextRespectUISPr(
						"fb.sepa.import.error.seitennummerninkonsistent", 
						objects.toArray());
			}
			case EJBExceptionLP.FEHLER_SEPAIMPORT_SALDENBETRAEGE_FEHLERHAFT: {
				return LPMain.getMessageTextRespectUISPr(
						"fb.sepa.import.error.saldenbetraegefehlerhaft", 
						objects.toArray());
			}
			case EJBExceptionLP.FEHLER_SEPAIMPORT_BUCHUNGSBETRAEGE_FEHLERHAFT: {
				return LPMain.getMessageTextRespectUISPr(
						"fb.sepa.import.error.buchungsbetraegefehlerhaft", 
						objects.toArray());
			}
			case EJBExceptionLP.FEHLER_SEPAIMPORT_KEINE_FORTLAUFENDE_ELEKTRONISCHE_AUSZUGSNUMMER: {
				return LPMain.getMessageTextRespectUISPr(
						"fb.sepa.import.error.keinefortlaufendeelektronischeauszugsnr", 
						objects.toArray());
			}
			case EJBExceptionLP.FEHLER_SEPAIMPORT_FELD_NULL_ODER_LEER: {
				return LPMain.getMessageTextRespectUISPr(
						"fb.sepa.import.error.feldnulloderleer", 
						objects.toArray());
			}
		}
			
		return null;
	}

	@Override
	public boolean shouldCloseDialog() {
		return bViewShouldClose;
	}

	@Override
	public String getSepaVerzeichnis() {
		return getBankverbindungDto().getCSepaVerzeichnis();
	}
	
	private void moveImportFilesToArchive() throws IOException {
		File parent = new File(getBankverbindungDto().getCSepaVerzeichnis());
		if (!parent.isDirectory()) return;
		
		File archiveFolder = new File(parent, ARCHIVE_DIRECTORY);
		if (!archiveFolder.exists()) {
			archiveFolder.mkdir();
		}
		if (!archiveFolder.canWrite()) {
			LpLogger.getInstance(this.getClass()).error("Keine Schreibrechte f\u00fcr Ordner '" + archiveFolder.getAbsolutePath() + "' zum Ablegen der importierten Sepa XML-Dateien");
		}
		
		for (File importFile : filesForImport) { 
			File archive = new File(archiveFolder, importFile.getName());
			
			FileChannel inChannel = null;
			FileChannel outChannel = null;
			FileInputStream fisIn = null;
			FileOutputStream fisOut = null;
			
			try {
				fisIn = new FileInputStream(importFile);
				fisOut = new FileOutputStream(archive);
				inChannel = fisIn.getChannel();
				outChannel = fisOut.getChannel();
				inChannel.transferTo(0, inChannel.size(), outChannel);
			} catch (FileNotFoundException e) {
				throw e;
			} catch (IOException e) {
				LpLogger.getInstance(this.getClass()).error("Kopieren von '" + importFile.getName() + "' ist fehlgeschlagen");
				throw e;
			} finally {
				try {
					if (inChannel != null) {
						inChannel.close();
					}
					if (outChannel != null) {
						outChannel.close();
					}
					if (fisIn != null) {
						fisIn.close();
					}
					if (fisOut != null) {
						fisOut.close();
					}
				} catch (IOException e) {
				}
			}
			
			if (archive != null && archive.exists()) {
				archive.setLastModified(importFile.lastModified());
				importFile.delete();
			}
		}
	}

	@Override
	public String getTextLetzteBuchung() {
		return "TODO: Letzte Buchung";
	}

}
