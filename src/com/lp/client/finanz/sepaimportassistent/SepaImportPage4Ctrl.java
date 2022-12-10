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

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.swing.table.TableModel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.AssistentPageController;
import com.lp.client.frame.assistent.IAssistentCanceler;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAdapter;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.finanz.service.BuchungKompakt;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.ISepaImportResult;
import com.lp.server.finanz.service.Iso20022StandardEnum;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.SepaKontoauszug;
import com.lp.server.util.BelegAdapter;
import com.lp.server.util.BelegZahlungAdapter;
import com.lp.server.util.KontoId;
import com.lp.util.Helper;

public class SepaImportPage4Ctrl extends AssistentPageController implements IManuelleAuswahlController, ISepaCellEditorController {

	public static final BelegAdapter MANUELLE_AUSWAHL = getManuelleAuswahlItem();
	
	private SepaImportModel model;
	private SwingWorker<List<ISepaImportResult>, Void> worker;
	private Boolean isBusyImporting;
	private IManuelleAuswahlListener manuelleAuswahlListener;
	private Boolean isPageNavigationAllowed;
	private WaitingSepaImportResult resultWaitingForManuelleAuswahl;
	private Throwable importException;
	private IAssistentCanceler assistentCanceler;
	
	public SepaImportPage4Ctrl(SepaImportModel model, IAssistentCanceler assistentCanceler) {
		this.model = model;
		isBusyImporting = true;
		isPageNavigationAllowed = true;
		this.assistentCanceler = assistentCanceler;
	}

	private static BelegAdapter getManuelleAuswahlItem() {
		EingangsrechnungDto erDto = new EingangsrechnungDto();
		erDto.setCNr(LPMain.getTextRespectUISPr("fb.sepa.import.manuelleauswahl"));
		BelegAdapter belegAdapter = new EingangsrechnungAdapter(erDto);
		return belegAdapter;
	}
	
	@Override
	public boolean isNextAllowed() {
		if (isBusyImporting || model == null || model.getResults() == null) {
			return false;
		}
		if (!areBuchungenKomplett()
				|| getImportException() != null) {
			return false;
		}
		
		return isPageNavigationAllowed;
	}

	@Override
	public boolean isPrevAllowed() {
		return isPageNavigationAllowed;
	}

	@Override
	public boolean isCancelAllowed() {
		return isPageNavigationAllowed;
	}

	@Override
	public void activateByNext() throws Throwable {
		isBusyImporting = true;
		cancelWorker();
		model.setResults(new ArrayList<ISepaImportResult>());

		worker = new SepaSearchWorker(model);
		worker.execute();
	}

	/**
	 * Beende den Worker, falls existent
	 */
	public void cancelWorker() {
		if (worker != null) {
			worker.cancel(true);
		}
	}

	public void setImportException(Throwable importException) {
		this.importException = importException;
	}
	
	public Throwable getImportException() {
		return importException;
	}
	
	@Override
	public void activateByPrev() throws Throwable {
	}

	@Override
	public boolean nextPageIfPossible() throws ExceptionLP, Throwable {
		return true;
	}

	@Override
	public boolean prevPageIfPossible() throws ExceptionLP, Throwable {
		cancelWorker();
		return true;
	}

	@Override
	public boolean cancelIfPossible() throws ExceptionLP, Throwable {
		cancelWorker();
		return true;
	}
	
	private boolean areBuchungenKomplett() {
		for (ISepaImportResult result : model.getResults()) {
			if (model.hasFibu()) {
				if (!result.applyPayment())
					return false;
			} else {
				if (!result.applyPayment() 
						&& !result.ignorePayment()) 
					return false;
			}
		}
		return true;
	}
	
	public TableModel getSearchResultsTableModel() {
		return model.getSearchResultsTableModel(this);
	}
	
	public Boolean isBusyImporting() {
		return isBusyImporting;
	}
	
	public static String getTextForBelegAdapterItem(BelegAdapter beleg) {
		return beleg.getArtAndCNr() + ", " + Helper.formatZahl(beleg.getOffenerBetrag(), 
				FinanzFac.NACHKOMMASTELLEN, LPMain.getInstance().getUISprLocale()) 
				+ " " + beleg.getWaehrungCNr()
				+ ", " + beleg.getPartnerKurzbezeichnung();
	}
	
	public void actionManuelleAuswahlDialog() {
		ISepaImportResult result = resultWaitingForManuelleAuswahl.get();
		if (getManuelleAuswahlListener() != null) {
			if (!resultWaitingForManuelleAuswahl.wasManuelleAuswahlSelected()) {
//				if (!manuelleAuswahlSelectedResults.contains(getResultWaitingForManuelleAuswahl())) {
				result.setManualPayments(new ArrayList<BelegZahlungAdapter>());
			}
			
			getManuelleAuswahlListener().waehleBelegzahlungManuell();
		}
	}
	
	public void setResultWaitingForManuelleAuswahl(WaitingSepaImportResult result) {
		resultWaitingForManuelleAuswahl = result;
	}

	public ISepaImportResult getResultWaitingForManuelleAuswahl() {
		return resultWaitingForManuelleAuswahl.get();
	}
	
	public IManuelleAuswahlListener getManuelleAuswahlListener() {
		return manuelleAuswahlListener;
	}

	public void setManuelleAuswahlListener(
			IManuelleAuswahlListener manuelleAuswahlListener) {
		this.manuelleAuswahlListener = manuelleAuswahlListener;
	}

	public void tableModelValueChanged() {
		setPaymentCompleted();
		fireNavigationUpdateEvent();
		fireDataUpdateEvent();
	}

	private void setPaymentCompleted() {
		for (ISepaImportResult result : model.getResults()) {
			if (result.getRemainingAmount() != null 
					&& BigDecimal.ZERO.compareTo(result.getRemainingAmount()) == 0) {
				result.setCompletedForSelectedBeleg(true);
			}
		}
	}

	public void actionManuelleAuswahlUebernehmen() {
		getResultWaitingForManuelleAuswahl().setEditedByUser(true);
	}
	
	public void actionManuelleAuswahlZurueck() {
		// wenn die manuelle Auswahl abgebrochen wurde und nur ein Suchergebnis
		// vorhanden ist, dann die Selektion wieder darauf setzen
		resultWaitingForManuelleAuswahl.restore();
		
		if (!resultWaitingForManuelleAuswahl.wasManuelleAuswahlSelected()) {
			if (getResultWaitingForManuelleAuswahl().getFoundItems().size() == 2) {
				getResultWaitingForManuelleAuswahl().setSelectedIndex(0);
			} else {
				getResultWaitingForManuelleAuswahl().setSelectedIndex(-1);
				getResultWaitingForManuelleAuswahl().setEditedByUser(false);
			}
		}
//		if (getResultWaitingForManuelleAuswahl().getManualPayments().isEmpty()) {
//			getResultWaitingForManuelleAuswahl().setEditedByUser(false);
//			
//			if (getResultWaitingForManuelleAuswahl().getFoundItems().size() == 2) {
//				getResultWaitingForManuelleAuswahl().setSelectedIndex(0);
//				model.belegChangedUpdateBelegZahlung(getResultWaitingForManuelleAuswahl());
//				removeFromManuelleAuswahlSelectedResults(getResultWaitingForManuelleAuswahl());
//			} else {
//				getResultWaitingForManuelleAuswahl().setSelectedIndex(-1);
//			}
//		}
	}
	
	public void setPageNavigationAllowed(Boolean isAllowed) {
		isPageNavigationAllowed = isAllowed;
		fireNavigationUpdateEvent();
	}

	public Integer getAuszugsnummer() {
		return model.getAuszugsnummer();
	}

	public BigDecimal getEndSaldoBankKtoauszug() {
		return model.getEndSaldoBetrag();
	}
	
	public BigDecimal getAnfangsSaldoBankKtoauszug() {
		return model.getAnfangsSaldoBetrag();
	}

	public BigDecimal getErwarteterEndSaldo() {
		BigDecimal erwarteterSaldo = model.hasFibu() ? getSepakonto().getSaldo() : getAnfangsSaldoBankKtoauszug();
		
		for (ISepaImportResult result : model.getResults()) {
			if (result.applyPayment()
					|| result.ignorePayment()) {
				erwarteterSaldo = erwarteterSaldo.add(result.getPayment().getBetrag().getPlusMinusWert());
			}
		}
		
		return erwarteterSaldo;
	}
	
	public BigDecimal getBankkontoSaldo() {
		return hasFibu() ? getSepakonto().getSaldo() : BigDecimal.ZERO;
	}
	
	public Integer getBankkontoIId() {
		return model.getSelectedBankverbindung().getKontoIId();
	}
	
	public String getKontonummerBank() {
		try {
			KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
					.kontoFindByPrimaryKey(getBankkontoIId());
			return kontoDto.getCNr();
		} catch (ExceptionLP e) {
			LpLogger.getLogger(this.getClass()).error(e);
		}
		return "";
	}

	public String getZahlungsinfoManuelleAuswahl() {
		return model.createZahlungsInfo(getResultWaitingForManuelleAuswahl());
	}

	public Date getZahldatum(ISepaImportResult result) {
		return model.getZahldatum(model.getResults().indexOf(result));
	}
	
	public Boolean hasSalden() {
		return model.hasStatementSalden();
	}

	public ISepakonto getSepakonto() {
		return model.getSepakonto();
	}
	
	protected void berechneSaldoBankkontoHV() {
		getSepakonto().refresh();
	}

	protected class SepaSearchWorker extends SwingWorker<List<ISepaImportResult>, Void> {

		private SepaImportModel model;
		
		public SepaSearchWorker(SepaImportModel model) {
			this.model = model;
		}
		
		@Override
		protected List<ISepaImportResult> doInBackground() throws Exception {
			List<SepaKontoauszug> list = new ArrayList<SepaKontoauszug>();
			list.add(model.getCurrentStatement());
			try {
				KontoId kontoId = new KontoId(model.getSelectedBankverbindung().getKontoIId());
				List<ISepaImportResult> results = DelegateFactory.getInstance()
						.getFinanzDelegate().searchForImportMatches(kontoId, list);
				
				for (ISepaImportResult result : results) {
					result.getFoundItems().add(MANUELLE_AUSWAHL);
				}
				
				return results;
			} catch (Throwable t) {
				setImportException(t);
				return new ArrayList<ISepaImportResult>();
			}
		}

		@Override
		protected void done() {
			try {
				model.setResults(get());
				
				for (ISepaImportResult result : model.getResults()) {
					//wenn Suchergebnisse vorhanden sind
					if (result.getFoundItems() != null && result.getFoundItems().size() > 1) {
						model.belegChangedUpdateBelegZahlung(result);
					}
				}
			} catch (InterruptedException e) {
				LpLogger.getLogger(this.getClass()).error(e);
			} catch (ExecutionException e) {
				LpLogger.getLogger(this.getClass()).error(e);
			}
			
			isBusyImporting = false;
			tableModelValueChanged();
		}
	
	}
	
	protected class WaitingSepaImportResult {
		private List<BelegZahlungAdapter> savedBelegZahlungen;
		private List<BuchungKompakt> savedBuchungen;
		private ISepaImportResult result;
		private int previousSelection = -1;

		public WaitingSepaImportResult (ISepaImportResult result) {
			this.result = result;
			savedBelegZahlungen = new ArrayList<BelegZahlungAdapter>();
			savedBelegZahlungen.addAll(result.getManualPayments());
			savedBuchungen = new ArrayList<BuchungKompakt>();
			savedBuchungen.addAll(result.getManualBookings());
		}

		public ISepaImportResult get() {
			return result;
		}
		
		public void restore() {
			List<BelegZahlungAdapter> payments = new ArrayList<BelegZahlungAdapter>();
			payments.addAll(savedBelegZahlungen);
			result.setManualPayments(payments);
			
			List<BuchungKompakt> bookings = new ArrayList<BuchungKompakt>();
			bookings.addAll(savedBuchungen);
			result.setManualBooking(bookings);
		}

		public int getPreviousSelection() {
			return previousSelection;
		}

		public void setPreviousSelection(Integer previousSelection) {
			if (previousSelection == null) previousSelection = -1;
			this.previousSelection = previousSelection;
		}
		
		public boolean wasManuelleAuswahlSelected() {
			if (previousSelection < 0 || previousSelection > result.getFoundItems().size()-1) {
				return false;
			}
			
			if (MANUELLE_AUSWAHL == result.getFoundItems().get(previousSelection)) {
				return true;
			}
			return false;
		}
	}

	public String getToolTipText(int row) {
		ISepaImportResult result = model.getSearchResultsTableModel(this).getResultAtRow(row);
		if (result.getSelectedIndex() != null && result.getFoundItems().indexOf(MANUELLE_AUSWAHL) == result.getSelectedIndex()) {
			StringBuilder builder = new StringBuilder();
			builder.append("<html>Manuelle Zahlungen");
			UebersichtEntryNormalizer normalizer = new UebersichtEntryNormalizer(getWaehrung());
			for (BelegZahlungAdapter belegZahlung : result.getManualPayments()) {
				ManuelleAuswahlUebersichtEntry entry = normalizer.normalize(belegZahlung);
				builder.append("<br />");
				builder.append(entry.toString());
			}
			for (BuchungKompakt buchung : result.getManualBookings()) {
				int idxBuchungdetail = 
						buchung.getBuchungdetailList().get(0).getKontoIId().equals(model.getSelectedBankverbindung().getKontoIId()) ? 1 : 0;
				ManuelleAuswahlUebersichtEntry entry = 
						normalizer.normalize(buchung.getBuchungdetailList().get(idxBuchungdetail));
				builder.append("<br />");
				builder.append(entry.toString());
			}
			builder.append("</html>");
			return builder.toString();
		}
		
		return null;
	}

	@Override
	public Date getZahldatum() {
		return getZahldatum(getResultWaitingForManuelleAuswahl());
	}

	@Override
	public void dataUpdated() {
		tableModelValueChanged();
	}

	@Override
	public void actionNewSelection(ISepaImportResult result, Integer previousSelection) {
		WaitingSepaImportResult waitingSepaResult = new WaitingSepaImportResult(result);
		waitingSepaResult.setPreviousSelection(previousSelection);
		setResultWaitingForManuelleAuswahl(waitingSepaResult);
		if (result.getSelectedBeleg() == SepaImportPage4Ctrl.MANUELLE_AUSWAHL) {
			actionManuelleAuswahlDialog();
//			controller.addToManuelleAuswahlSelectedResults(result);
//			result.setSelectedIndex(-1);
		} else {
			model.belegChangedUpdateBelegZahlung(result);
			tableModelValueChanged();
			//controller.setBelegZahlungen(rowIndex, result.completeSelectedBeleg());
		}
	}

	public boolean hasResultWaitingForManuelleAuswahlChanges() {
		ISepaImportResult result = getResultWaitingForManuelleAuswahl();
		return result.hasManualPaymentsOrBookings();
	}
	
	public boolean hasFibu() {
		return model.hasFibu();
	}
	
	@Override
	public String getInfoBuchungszeile(ISepaImportResult result) {
		return model.getInfoBuchungszeile(result);
	}

	public String getWaehrung() {
		return model.getSepakonto().getWaehrung();
	}
	
	@Override
	public Iso20022StandardEnum getIso20022Standard() {
		return model.getIso20022Standard();
	}
	
	public void cancelAssistent() throws ExceptionLP, Throwable {
		assistentCanceler.cancel();
	}
}
