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
package com.lp.client.finanz.sepaimportassistent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;

import org.apache.commons.lang.StringUtils;

import com.lp.client.finanz.SepakontoauszugImportController;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.AssistentPageController;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.remote.PayloadWorker;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.finanz.service.ISepaImportResult;
import com.lp.server.finanz.service.SepaImportInfoDto;
import com.lp.server.system.service.PayloadDto;
import com.lp.server.util.BelegZahlungAdapter;

public class SepaImportPage5Ctrl extends AssistentPageController {
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());
	private SepaImportModel model;
	private ImportTask importWorker;
	private Boolean isImporting = true;
	private Integer numberOfCompletedPayments;
	private Throwable importThrowable = null;
	private List<String> clientInfo;
	private BoundedRangeModel boundedRangeModel;
	private List<ISepaImportResult> resultsToImport;
	private SepaBuchungProtokoll protokoll;
	private boolean goBackAllowed = false;
	
	public SepaImportPage5Ctrl(SepaImportModel model) {
		this.model = model;
		protokoll = new SepaBuchungProtokoll();
	}

	@Override
	public boolean isNextAllowed() {
		if (isImporting) return false;
		
		return true;
	}

	@Override
	public boolean isPrevAllowed() {
		return goBackAllowed;
	}

	@Override
	public boolean isCancelAllowed() {
		return false;
	}

	@Override
	public void activateByNext() throws Throwable {
		importThrowable = null;
		goBackAllowed = false;
		protokoll.reset();
		initResultsToImport();
		isImporting = true;
		clientInfo = new ArrayList<String>();
		clientInfo.add(LPMain.getMessageTextRespectUISPr(
				"fb.sepa.import.importierekontoauszug", 
				new Object[] {resultsToImport.size()}));
		fireDataUpdateEvent();
		
		for (ISepaImportResult result : resultsToImport) {
			if (result.getSelectedBeleg() != null 
//					&& result.getSelectedBeleg() != SepaImportPage4Ctrl.MANUELLE_AUSWAHL
					&& result.applyPayment()) {
				setBelegZahlungen(result);
				
			}
		}

		if (importWorker != null) {
			importWorker.cancel(true);
		}
		importWorker = new ImportTask();
		
		importWorker.execute();
		
//		DelegateFactory.getInstance().getFinanzDelegate().archiviereSepaKontoauszug(
//				model.getXmlContents().get(0), model.getImportFiles().get(0).getName(), 
//				model.getSelectedBankverbindung().getIId(), 1);

	}
	
	public List<String> getClientInfo() {
		return clientInfo;
	}

	@Override
	public void activateByPrev() throws Throwable {

	}

	@Override
	public boolean nextPageIfPossible() throws ExceptionLP, Throwable {
		return false;
	}

	@Override
	public boolean prevPageIfPossible() throws ExceptionLP, Throwable {
		return true;
	}

	@Override
	public boolean cancelIfPossible() throws ExceptionLP, Throwable {
		return false;
	}

	public void setImporting(Boolean isImporting) {
		this.isImporting = isImporting;
	}
	
	public Boolean isBusyImporting() {
		return isImporting;
	}

	public void setBelegZahlungen(ISepaImportResult result) throws ExceptionLP, Throwable {
		if (result.getSelectedBeleg() == null) return;
		
		model.prepareBelegZahlungen(result);
		
//		List<BelegZahlungAdapter> belegZahlungen = new ArrayList<BelegZahlungAdapter>();
//		belegZahlungen.add(belegZahlung);
//		result.setSelectedPayments(belegZahlungen);
	}

	
	public Integer getNumberOfCompletedPayments() {
		return numberOfCompletedPayments;
	}

	public void setNumberOfCompletedPayments(Integer numberOfCompletedPayments) {
		this.numberOfCompletedPayments = numberOfCompletedPayments;
	}

	public void setImportThrowable(Throwable e) {
		importThrowable = e;
	}
	
	public Throwable getImportThrowable() {
		return importThrowable;
	}

	public void initResultsToImport() {
		resultsToImport = new ArrayList<ISepaImportResult>();
		if (model.getResults() == null || model.getResults().isEmpty()) {
			return;
		}
		
		for (ISepaImportResult result : model.getResults()) {
			if (result.applyPayment()) {
				resultsToImport.add(result);
				protokoll.addApplied(result);
			} else if (result.ignorePayment()) {
				protokoll.addIgnored(result);
			}
		}
	}
	
	public BoundedRangeModel getProgressModel() {
		if (boundedRangeModel == null) {
			boundedRangeModel = new DefaultBoundedRangeModel(0, 1, 0, 100);
		}
		return boundedRangeModel;
	}
	
	protected void setProgressValue(int progress) {
		getProgressModel().setValue(progress);
		fireDataUpdateEvent();
	}

	protected class ImportTask extends PayloadWorker<Integer> {

		@Override
		protected Integer doInBackground() throws Exception {
			try {
				return DelegateFactory.getInstance().getFinanzDelegate().importSepaImportResults(
						model.getSepakontoauszugDto(), resultsToImport, this);
			} catch (Throwable t) {
				myLogger.error("Error waehrend des Verbuchens der Buchungszeilen", t);
				setImportThrowable(t);
				return null;
			}
		}
		
		@Override
		public void publishPayload(PayloadDto payload) {
			publish(payload);
		}

		@Override
		protected void process(List<PayloadDto> payloads) {
			for (PayloadDto payload : payloads) {
				SepaImportInfoDto infoDto = (SepaImportInfoDto) payload.getPayload();
				setProgressValue(infoDto.getProgressDto().getProgress());
//				clientInfo.add("Zahlung " + infoDto.getProgressDto().getCurrent() 
//						+ " von " + infoDto.getProgressDto().getMaximum() + " importiert");
//				fireDataUpdateEvent();
			}
		}

		@Override
		protected void done() {
			try {
				Integer completedPayments = get();
				if (completedPayments == null) {
					onErrorOccured();
				} else {
					clientInfo.add(LPMain.getTextRespectUISPr("fb.sepa.import.importabgeschlossen"));
					protokoll.archiveToSepaDirectory();
				}
				setNumberOfCompletedPayments(completedPayments);
				
			} catch (InterruptedException e) {
				onErrorOccured(e);
			} catch (ExecutionException e) {
				onErrorOccured(e);
			} catch (Throwable t) {
				onErrorOccured(t);
			}
			setImporting(false);
			
			fireDataUpdateEvent();
			fireNavigationUpdateEvent();
		}

	}

	private void onErrorOccured() {
		setNumberOfCompletedPayments(null);
		clientInfo.add(LPMain.getTextRespectUISPr("fb.sepa.import.importabgebrochen"));
		goBackAllowed = true;
	}

	private void onErrorOccured(Throwable t) {
		setImportThrowable(t);
		onErrorOccured();
	}

	protected class SepaBuchungProtokoll {
		private List<ISepaImportResult> resultsApplied;
		private List<ISepaImportResult> resultsIgnored;
		
		public void reset() {
			getResultsApplied().clear();
			getResultsIgnored().clear();
		}
		
		public List<ISepaImportResult> getResultsApplied() {
			if (resultsApplied == null) {
				resultsApplied = new ArrayList<ISepaImportResult>();
			}
			return resultsApplied;
		}
		
		public List<ISepaImportResult> getResultsIgnored() {
			if (resultsIgnored == null) {
				resultsIgnored = new ArrayList<ISepaImportResult>();
			}
			return resultsIgnored;
		}
		
		public void addApplied(ISepaImportResult result) {
			getResultsApplied().add(result);
		}
		
		public void addIgnored(ISepaImportResult result) {
			getResultsIgnored().add(result);
		}
		
		public void archiveToSepaDirectory() {
			if (model.hasFibu()) 
				return;
			
			File parent = new File(model.getSelectedBankverbindung().getCSepaVerzeichnis());
			if (!parent.isDirectory()) return;
			
			File archiveFolder = new File(parent, SepakontoauszugImportController.ARCHIVE_DIRECTORY);
			if (!archiveFolder.exists()) {
				archiveFolder.mkdir();
			}
			File logFile = new File(archiveFolder, "auszug" + model.getAuszugsnummer() + ".log");
			
			FileOutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(logFile);
				String protokoll = createProtokoll();
			    byte[] strToBytes = protokoll.getBytes("UTF-8");
			    outputStream.write(strToBytes);
			} catch (IOException e) {
				myLogger.error("IOException writing SepaImport protocol to file '" + logFile.getAbsolutePath() + "'", e);
			} catch (Exception e) {
				myLogger.error("Error during creating SepaImport protocol", e);
			} finally {
				try {
					if (outputStream != null) 
						outputStream.close();
				} catch (IOException e) {
					myLogger.error("IOException closing outputstream of file '" + logFile.getAbsolutePath() + "'", e);
				}
			}
		}
		
		public String createProtokoll() {
			StringBuilder builder = new StringBuilder();
			builder.append("Kontoauszug: ").append(model.getAuszugsnummer());
			builder.append("\nVerbuchte Buchungszeilen:");
			for (ISepaImportResult applied : getResultsApplied()) {
				builder.append("\n- ")
					.append(model.getInfoBuchungszeile(applied));
			}
			
			builder.append("\nIgnorierte Buchungszeilen:");
			for (ISepaImportResult ignored : getResultsIgnored()) {
				builder.append("\n- ")
					.append(model.getInfoBuchungszeile(ignored));
			}
			return builder.toString();
		}

		private String getBelege(ISepaImportResult applied) {
			Set<String> cnrs = new HashSet<String>();
			for (BelegZahlungAdapter zahlung : applied.getManualPayments()) {
				cnrs.add(zahlung.getBelegAdapter().getArtAndCNr());
			}
			return StringUtils.join(cnrs.iterator(), ", ");
		}
	}
}
