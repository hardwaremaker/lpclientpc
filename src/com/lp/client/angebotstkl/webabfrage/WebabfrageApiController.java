package com.lp.client.angebotstkl.webabfrage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.service.findchips.DefaultFindchipsConfig;
import com.lp.service.findchips.FindRequestExecutor;
import com.lp.service.findchips.FindchipsApiController;
import com.lp.service.findchips.FindchipsApiRequest;
import com.lp.service.findchips.FindchipsApiResponse;
import com.lp.service.findchips.FindchipsConfig;
import com.lp.service.findchips.FindchipsResponse;
import com.lp.service.findchips.IFindchipsApi;
import com.lp.service.findchips.IFindchipsResponseCallback;

public class WebabfrageApiController implements IWebabfrageApi {
	private final static LpLogger controllerLogger = (LpLogger) LpLogger.getInstance(WebabfrageApiController.class);
	
	private IFindchipsApi findchipsController;
	private FindchipsConfig findchipsConfig;
	private FindchipsWorker findchipsWorker;
	
	private IWebabfrageCallback viewController;
	private NormalizationFactory normalizationFactory;
	private ErrorFactory errorFactory;
	private List<WebabfrageError> errors;
	private List<WebabfrageException> exceptions;

	public WebabfrageApiController(IWebabfrageCallback callback, NormalizationFactory normalizationFactory) throws Throwable {
		this.viewController = callback;
		findchipsConfig = new DefaultFindchipsConfig();
		this.normalizationFactory = normalizationFactory;
		errorFactory = new ErrorFactory();
		errors = Collections.synchronizedList(new ArrayList<WebabfrageError>());
		exceptions = Collections.synchronizedList(new ArrayList<WebabfrageException>());
	}
	
	public List<WebabfrageError> getErrors() {
		return errors;
	}
	
	public void addError(WebabfrageError error) {
		getErrors().add(error);
	}

	public List<WebabfrageException> getExceptions() {
		return exceptions;
	}
	
	public synchronized void addException(WebabfrageException exception) {
		getExceptions().add(exception);
	}

	private IFindchipsApi getFindchipsController() {
		if (findchipsController == null) {
			findchipsController = new FindchipsApiController(findchipsConfig);
		}
		
		return findchipsController;
	}

	@Override
	public void find(Map<Integer, List<String>> idSearchStringMap, Integer requestType) throws ExceptionLP {
		if (AngebotstklFac.WebabfrageTyp.FINDCHIPS.equals(requestType)) {
			try {
				findByFindChipsApi(idSearchStringMap);
			} catch (InterruptedException e) {
				controllerLogger.error("InterruptedException", e);
			} catch (ExecutionException e) {
				controllerLogger.error("ExecutionException", e);
			} catch (Throwable e) {
				controllerLogger.error("Throwable", e);
			}
		}
	}

	@Override
	public void cancelRequest(Integer requestType) {
		if (AngebotstklFac.WebabfrageTyp.FINDCHIPS.equals(requestType) && findchipsWorker != null) {
			findchipsWorker.cancelWorker();
		}
	}

	private void findByFindChipsApi(Map<Integer, List<String>> idSearchStringMap) throws InterruptedException, ExecutionException, Throwable {
		findchipsWorker = new FindchipsWorker(createFindchipsRequests(idSearchStringMap));
		
		findchipsWorker.execute();
	}

	private List<FindchipsApiRequest> createFindchipsRequests(Map<Integer, List<String>> idSearchStringMap) {
		List<FindchipsApiRequest> requests = new ArrayList<FindchipsApiRequest>();
		
		for (Integer key : idSearchStringMap.keySet()) {
			for (String searchString : idSearchStringMap.get(key)) {
				FindchipsApiRequest apiRequest = new FindchipsApiRequest(key, searchString);
				requests.add(apiRequest);
			}
		}
		
		viewController.updateNumberOfRequests(requests.size());
		return requests;
	}

	private class FindchipsWorker extends SwingWorker<Void, NormalizedWebabfragePart> implements IFindchipsResponseCallback{
		private final LpLogger workerLogger = (LpLogger) LpLogger.getInstance(FindchipsWorker.class);

		private List<FindchipsApiRequest> requests;
		private FindRequestExecutor executor;
		
		public FindchipsWorker(List<FindchipsApiRequest> list) {
			requests = list;
		}
		
		public void cancelWorker() {
			if (executor != null) {
				workerLogger.debug("Cancel FindchipsWorker");
				getFindchipsController().cancel(executor);
				cancel(true);
			}
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			if (requests == null) {
				workerLogger.debug("Requests = null, FindchipsWorker canceled");
			} else {
				workerLogger.debug("Start FindchipsWorker, send " + requests.size() + " Requests");				
			}
			
			executor = getFindchipsController().findByPartnrAsynch(requests, this);
			getFindchipsController().awaitTermination(executor);

			return null;
		}

		@Override
		public void onSuccess(FindchipsApiRequest request, FindchipsApiResponse response) {
			workerLogger.debug("Got response for request with id=" + request.getId());
			if (response == null) return;
			
			if (!response.isSuccessfull()) {
				viewController.processNoResultFound(request.getId());
			}
			
			for (FindchipsResponse resp : response.getResponse()) {
				List<NormalizedWebabfragePart> normalizedParts = normalizationFactory.create(request.getId(), resp);
				for (NormalizedWebabfragePart part : normalizedParts) {
					publish(part);
				}
				
			}
		}

		@Override
		public void onError(FindchipsApiRequest request) {
			WebabfrageError we = errorFactory.createError(request);
			workerLogger.debug("onError: " + we == null ? "we=null" : we.toString());
			addError(we);
		}

		@Override
		public void onException(FindchipsApiRequest request, Exception e) {
			WebabfrageException we = errorFactory.createException(request, e);
			workerLogger.debug("onException: " + we == null ? "we=null" : we.toString());
			addException(we);
		}
		
		@Override
		protected void process(List<NormalizedWebabfragePart> parts) {
			workerLogger.debug("process " + parts.size() + " parts");
			viewController.processParts(parts);
		}

		@Override
		protected void done() {
			workerLogger.debug("FindchipsWorker is done");

			viewController.done(getErrors(), getExceptions());
		}
		
	}
}
