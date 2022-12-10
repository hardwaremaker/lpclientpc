package com.lp.service.google.geocoding;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.lp.client.util.logger.LpLogger;
import com.lp.server.partner.service.GeodatenDto;
import com.lp.service.google.geocoding.schema.GeocodeApiResponse;

public class GeodatenFinder {
	private final LpLogger log = (LpLogger) LpLogger.getInstance(GeodatenFinder.class);
	
	private GeocodeRequestConfig geocodeConfig;
	private IGeocodeApi geocodeApiController;
	
	public GeodatenFinder() throws Throwable {
		geocodeConfig = new DefaultGeocodeRequestConfig();
	}
	
	private IGeocodeApi getGeocodeApiController() {
		if (geocodeApiController == null) {
			geocodeApiController = new GeocodeApiController(geocodeConfig);
		}
		return geocodeApiController;
	}
	
	public void find(List<GeocodeApiRequest> requests, IGeodatenabfrageCallback resultCallback) {
		GeocodeResponseCallback responseCallback = new GeocodeResponseCallback(resultCallback);
		try {
			getGeocodeApiController().findByAddress(requests, 500, responseCallback);
			resultCallback.done();
		} catch (InterruptedException e) {
			log.error("InterruptedException", e);
			resultCallback.doneByError(e);
		} catch (ExecutionException e) {
			log.error("ExecutionException", e);
			resultCallback.doneByError(e);
		}
	}
	
	public void cancel() {
		getGeocodeApiController().cancel();
	}
	
	public GeocodeResult find(GeocodeApiRequest request) throws InterruptedException, ExecutionException {
		GeocodeApiResponse response = getGeocodeApiController().findByAddress(request);
		if (response == null) return null;
		
		return new GeocodeResult(response.getStatus(), transformResponse(request, response));
	}
	
	public void findAsync(List<GeocodeApiRequest> requests, IGeodatenabfrageCallback resultCallback) {
		GeodatenWorker worker = new GeodatenWorker(requests, resultCallback);
		worker.execute();
	}
	
	public GeodatenDto find(String address) {
		try {
			GeocodeApiResponse response = getGeocodeApiController().findByAddress(address);
			return transformResponse(response);
		} catch (InterruptedException e) {
			log.error("InterruptedException", e);
		} catch (ExecutionException e) {
			log.error("ExecutionException", e);
		}
		return null;
	}
	
	public GeodatenDto findExc(String address) throws GeocodeException {
		try {
			GeocodeApiResponse response = getGeocodeApiController().findByAddress(address);
			if (!response.hasResult()) {
				GeocodeResult result = new GeocodeResult(response.getStatus(), transformResponse(response));
				GeocodeErrorException exc = new GeocodeErrorException(address, result);
				throw exc;
			}
			return transformResponse(response);
		} catch (InterruptedException e) {
			log.error("InterruptedException", e);
			throw new GeocodeRequestException(address, e);
		} catch (ExecutionException e) {
			log.error("ExecutionException", e);
			throw new GeocodeRequestException(address, e);
		}
	}

	private class GeocodeResponseCallback implements IGeocodeResponseCallback {
		private final LpLogger workerLogger = (LpLogger) LpLogger.getInstance(GeocodeResponseCallback.class);
		private IGeodatenabfrageCallback resultCallback;

		public GeocodeResponseCallback(IGeodatenabfrageCallback resultCallback) {
			this.resultCallback = resultCallback;
		}
		@Override
		public void onSuccess(GeocodeApiRequest request, GeocodeApiResponse response) {
			GeocodeResult result = new GeocodeResult(response.getStatus(), transformResponse(request, response));
			result.setId(request.getId());
			result.setErrorMessage(response.getErrorMessage());
			resultCallback.processResult(result);
		}

		@Override
		public void onError(GeocodeApiRequest request) {
			workerLogger.info("Request '" + request.asString() + "' ended not successful");
			resultCallback.processResult(createErrorResult(request));
		}

		@Override
		public void onException(GeocodeApiRequest request, Exception e) {
			workerLogger.info("Request '" + request.asString() + "' ended not successful", e);
			resultCallback.processResult(createErrorResult(request));
		}
		
	}

	private class GeodatenWorker extends SwingWorker<Void, GeocodeResult> implements IGeocodeResponseCallback {
		private final LpLogger workerLogger = (LpLogger) LpLogger.getInstance(GeodatenWorker.class);
		
		private List<GeocodeApiRequest> requests;
		private GeocodeRequestExecutor executor;
		private IGeodatenabfrageCallback resultCallback;
		
		public GeodatenWorker(List<GeocodeApiRequest> requests, IGeodatenabfrageCallback resultCallback) {
			this.requests = requests;
			this.resultCallback = resultCallback;
		}

		@Override
		protected Void doInBackground() throws Exception {
			if (requests == null) {
				workerLogger.info("Requests == null, won't start");
				return null;
			}
			
			workerLogger.info("Start requesting geocodes for " + requests.size() + " requests");
			executor = getGeocodeApiController().findByAddressAsynch(requests, this);
			getGeocodeApiController().awaitTermination(executor);
			
			return null;
		}
		
		@Override
		public void onSuccess(GeocodeApiRequest request, GeocodeApiResponse response) {
			workerLogger.info("Got response '" + response.asString() + "' for request '" + request.asString() + "'");
			
			if (!response.isSuccessful()) {
				// TODO create an error?
				return;
			}

			GeocodeResult result = new GeocodeResult(response.getStatus(), transformResponse(request, response));
			result.setErrorMessage(response.getErrorMessage());
			publish(result);
		}

		@Override
		public void onError(GeocodeApiRequest request) {
			workerLogger.info("Request '" + request.asString() + "' ended not successful");
		}

		@Override
		public void onException(GeocodeApiRequest request, Exception e) {
			workerLogger.info("Request '" + request.asString() + "' ended not successful", e);
		}
		
		@Override
		protected void process(List<GeocodeResult> results) {
			workerLogger.info("Processing " + results.size() + " results");
			for (GeocodeResult result : results) {
				resultCallback.processResult(result);
			}
		}
		
		@Override
		protected void done() {
			workerLogger.info("GeodatenWorker is done");
			resultCallback.done();
		}
	}
	
	protected GeodatenDto transformResponse(GeocodeApiResponse response) {
		if (response.getResults().size() < 1) {
			return null;
		}
		GeodatenDto geodatenDto = new GeodatenDto();
		geodatenDto.setLaengengrad(new BigDecimal(response.getResults().get(0).getGeometry().getLocation().getLongitude()));
		geodatenDto.setBreitengrad(new BigDecimal(response.getResults().get(0).getGeometry().getLocation().getLatitude()));
		return geodatenDto;
	}

	protected GeodatenDto transformResponse(GeocodeApiRequest request, GeocodeApiResponse response) {
		GeodatenDto geodatenDto = new GeodatenDto();
		geodatenDto.setPartnerIId(request != null ? request.getId() : null);
		if (response.getResults().size() < 1) {
			return geodatenDto;
		}
		
		geodatenDto.setLaengengrad(new BigDecimal(response.getResults().get(0).getGeometry().getLocation().getLongitude()));
		geodatenDto.setBreitengrad(new BigDecimal(response.getResults().get(0).getGeometry().getLocation().getLatitude()));
		return geodatenDto;
	}

	private GeocodeResult createErrorResult(GeocodeApiRequest request) {
		GeocodeResult result = new GeocodeResult();
		result.setId(request.getId());
		result.setSuccessful(false);
		return result;
	}
	
}
