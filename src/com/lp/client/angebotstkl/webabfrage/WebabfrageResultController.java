package com.lp.client.angebotstkl.webabfrage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;

public class WebabfrageResultController implements IWebabfrageViewController, IWebabfrageResultModelController, IWebabfrageCallback {
	private final static LpLogger myLogger = (LpLogger) LpLogger.getInstance(WebabfrageResultController.class);
	
	public final static int WARNING_NUMBER_OF_REQUESTS_LIMIT = 10;
	
	private WebabfrageBaseModel baseModel;
	private Boolean bWebabfrageLaeuft = false;
	private IWebabfrageApi apiController;
	private IWebabfrageControlListener webabfrageControlListener;
	private IWebabfrageResultTableUpdateListener tableUpdateListener;
	private WebabfrageResultTableModel resultTableModel;
	private IWebpartnerUpdateController webpartnerUpdateController;
	private Finder finder;
	private WebabfrageManager webabfrageManager;

	public WebabfrageResultController(WebabfrageBaseModel baseModel, IWebpartnerUpdateController webpartnerUpdateController) throws Throwable {
		this.baseModel = baseModel;
		this.webpartnerUpdateController = webpartnerUpdateController;
//		apiController = new WebabfrageApiMOCK(this);
		apiController = new WebabfrageApiController(this, new NormalizationFactory(baseModel));
		resultTableModel = new WebabfrageResultTableModel(this, baseModel.getResults());
		webabfrageManager = new WebabfrageManager();
		finder = new Finder();
		
		notifyResultTableUpdateListener();
	}
	
	private Boolean isWebabfrageRunning() {
		return bWebabfrageLaeuft;
	}
	
	private void setWebabfrageRunning(Boolean running) {
		bWebabfrageLaeuft = running;
	}

	private void generateRequestsAndStart(List<IWebabfrageResult> positionen) {
		try {
			setWebabfrageRunning(true);
			apiController.find(webabfrageManager.getSuchstringsMap(positionen), baseModel.getSelectedWebabfrageTyp());
		} catch (ExceptionLP e) {
			handleThrowable(e);
		} catch (Throwable e) {
			handleThrowable(e);
		}
	}

	private void handleThrowable(Throwable t) {
		handleThrowable(t, "");
	}

	private void handleThrowable(Throwable t, String message) {
		myLogger.debug(message, t);
	}

	
	/* 
	 * IWebabfrageViewController Methoden
	*/

	@Override
	public void actionMengenstaffelSelected(Integer selectedMenge) {
		baseModel.setSelectedMengeIndex(selectedMenge);
		notifyResultTableUpdateListener();
	}

	@Override
	public void actionStartWebabfrageAlle() {
		myLogger.debug("actionStartWebabfrageAlle");
		if (!isWebabfrageRunning()) {
			notifyAbfrageStarted();
			generateRequestsAndStart(baseModel.getResults());
		}
	}

	@Override
	public void actionStartWebabfrageSelektierte(int[] rows) {
		myLogger.debug("actionStartWebabfrageAlle");
		if (!isWebabfrageRunning()) {
			notifyAbfrageStarted();
			List<IWebabfrageResult> positionen = new ArrayList<IWebabfrageResult>();
			for (int row : rows) {
				IWebabfrageResult pos = getWebabfrageResultTableModel().getResultAtRow(row);
				if (pos != null) {
					positionen.add(pos);
				}
			}
			generateRequestsAndStart(positionen);
		}
	}

	@Override
	public void actionCancelWebabfrage() {
		if (isWebabfrageRunning()) {
			notifyAbfrageCompleted();
			setWebabfrageRunning(false);
			apiController.cancelRequest(null);
		} 
	}

	@Override
	public void actionRefreshResultTable() {
		notifyResultTableUpdateListener();
	}

	@Override
	public void registerWebabfrageControlListener(IWebabfrageControlListener listener) {
		webabfrageControlListener = listener;
	}
	
	private void notifyAbfrageStarted() {
		if (webabfrageControlListener != null) {
			webabfrageControlListener.updateAbfrageStarted();
		}
	}

	private void notifyAbfrageCompleted() {
		if (webabfrageControlListener != null) {
			webabfrageControlListener.updateAbfrageCompleted();
		}
	}

	@Override
	public WebabfrageResultTableModel getWebabfrageResultTableModel() {
		return resultTableModel;
	}

	/* 
	 * IWebabfrageResultModelController Methoden
	*/

	@Override
	public void registerDataUpdateListener(IWebabfrageResultTableUpdateListener listener) {
		tableUpdateListener = listener;
	}
	
	@Override
	public List<IWebabfrageResult> getWebabfragePositionen() {
		return baseModel.getResults();
	}

	@Override
	public BigDecimal getSelectedEinkaufsangebotMenge() {
		return baseModel.getSelectedMenge();
	}

	private void notifyResultTableUpdateListener() {
		tableUpdateListener.dataUpdated();
	}

	/* 
	 * IWebabfrageCallback Methoden
	*/
	
	@Override
	public void updateNumberOfRequests(Integer numberOfRequests) {
		if (numberOfRequests == null) return;
		
		EinkaufsangebotDto ekDto = baseModel.getEinkaufsangebotDto();
		ekDto.setIAnzahlwebabfragen(ekDto.getIAnzahlwebabfragen() == null ? numberOfRequests : ekDto.getIAnzahlwebabfragen() + numberOfRequests);
		try {
			DelegateFactory.getInstance().getAngebotstklDelegate().updateEinkaufsangebot(ekDto);
		} catch (Throwable e) {
			handleThrowable(e, "Could not update number of requests of Einkaufsangebot with iId=" + ekDto.getIId());
		}
	}

	@Override
	public void processParts(List<NormalizedWebabfragePart> parts) {
		myLogger.debug("process " + parts.size() + " parts");
		for (NormalizedWebabfragePart part : parts) {
			IWebabfrageResult result = baseModel.getWebabfrageResultByEinkaufsangebotpositionIId(part.getSearchId());
//			part.setPriceByQuantity(result.getMenge());
			part.getDistributor().setIId(webpartnerUpdateController.actionUpdateWebpartner(part.getDistributor()));
			result.addPartBySearchType(webabfrageManager.getSearchTypeForSearchId(part.getSearchId()), part);
		}
		
		finder.setEkweblieferanten(webpartnerUpdateController.getEkweblieferanten());
		finder.rateParts(null, parts);
		notifyResultTableUpdateListener();
	}

	@Override
	public void processNoResultFound(Integer searchId) {
		IWebabfrageResult result = baseModel.getWebabfrageResultByEinkaufsangebotpositionIId(searchId);
		result.setState(new ColorNotFoundState());
	}

	@Override
	public void done(List<WebabfrageError> errors, List<WebabfrageException> exceptions) {
		myLogger.debug("done, got " + errors.size() + " errors and " + exceptions.size() + " exceptions");
		setWebabfrageRunning(false);
		notifyAbfrageCompleted();
	}

	@Override
	public Map<Integer, BigDecimal> getEinkaufsangebotMengenstaffel() {
		return baseModel.getEkMengen();
	}

}
