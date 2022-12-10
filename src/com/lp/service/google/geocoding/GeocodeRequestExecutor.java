package com.lp.service.google.geocoding;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lp.client.util.logger.LpLogger;
import com.lp.service.google.geocoding.schema.GeocodeApiResponse;

public class GeocodeRequestExecutor {
	private final static LpLogger myLogger = (LpLogger) LpLogger.getInstance(GeocodeRequestExecutor.class);

	private ExecutorService executorService;
	private ObjectMapper jsonMapper;
	private GeocodeRequestConfig config;
	private List<Future<GeocodeApiResponse>> answers;
	
	public GeocodeRequestExecutor() throws Throwable {
		this(new DefaultGeocodeRequestConfig());
	}
	
	public GeocodeRequestExecutor(GeocodeRequestConfig config) {
		this.config = config;
		executorService = Executors.newFixedThreadPool(config.getMaxThreads());
	}
	
	public GeocodeApiResponse executeOne(GeocodeApiRequest request, IGeocodeResponseCallback callback) throws InterruptedException, ExecutionException {
		GeocodeApiResponse response = executorService.submit(new ApiRequest(request, callback)).get();
		executorService.shutdown();
		return response;
	}
	
	public void executeAll(List<GeocodeApiRequest> requests, IGeocodeResponseCallback callback) throws InterruptedException {
		List<ApiRequest> apiRequests = new ArrayList<ApiRequest>();
		for (GeocodeApiRequest request : requests) {
			apiRequests.add(new ApiRequest(request, callback));
		}
		answers = executorService.invokeAll(apiRequests);
	}

	public void executeAllSync(List<GeocodeApiRequest> requests, long timeout, IGeocodeResponseCallback callback) throws InterruptedException, ExecutionException {
		for (GeocodeApiRequest request : requests) {
			executorService.submit(new ApiRequest(request, callback)).get();
			Thread.sleep(timeout);
		}
		executorService.shutdown();
	}
	
	public void awaitTermination() {
		try {
			for (Future<GeocodeApiResponse> answer : answers) {
				if (!answer.isDone()) {
						answer.get();
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			myLogger.info("awaitingTermination failed", e);
		}
		myLogger.info("awaitTermination done, shutting down executorService");
		executorService.shutdown();
	}
	
	public void cancel() {
		for (Future<GeocodeApiResponse> answer : answers) {
			if (!answer.isDone()) {
					answer.cancel(true);
			}
		}
		executorService.shutdown();
	}

	public void cancelSync() {
		executorService.shutdown();
		try {
		    if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
		        executorService.shutdownNow();
		    } 
		} catch (InterruptedException e) {
		    executorService.shutdownNow();
		}	
	}

	private ObjectMapper getJsonMapper() {
		if (jsonMapper == null) {
			jsonMapper = new ObjectMapper();
			jsonMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		return jsonMapper;
	}
	
	private class ApiRequest implements Callable<GeocodeApiResponse> {
		private GeocodeApiRequest request;
		private IGeocodeResponseCallback callback;
		
		public ApiRequest(GeocodeApiRequest request, IGeocodeResponseCallback callback) {
			this.request = request;
			this.callback = callback;
		}

		@Override
		public GeocodeApiResponse call() throws Exception {
			HttpClient httpClient = new HttpClient();
			GetMethod getMethod = new GetMethod(buildUrl());
			GeocodeApiResponse apiResponse = new GeocodeApiResponse();
			
			try {
				myLogger.info("Calling Geocoding-API for address '" + request.getAddress() + "'...");
				int status = httpClient.executeMethod(getMethod);
				myLogger.info("Called Geocoding-API for address '" + request.getAddress() + "' -> HTTP-Status: <" + status + ">");
				
				if (status != HttpStatus.SC_OK) {
					myLogger.error("Geocode-API request '" + request.asString() + "' responded with status code '" + status + "'");
					callback.onError(request);
					return apiResponse;
				}
				
				myLogger.info("Done requesting address '" + request.getAddress() + "'.");
				apiResponse = getJsonMapper().readValue(getMethod.getResponseBodyAsStream(), GeocodeApiResponse.class);
				callback.onSuccess(request, apiResponse);

			} catch(HttpException e) {
				myLogger.info("Got HttpException requesting '" + request.getAddress() + "'", e) ;
				callback.onException(request, e);;
			} catch(JsonMappingException e) {
				myLogger.warn("JsonMapping problem requesting '" + request.getAddress() + "'", e);
				callback.onException(request, e);;
			} catch (JsonParseException e) {
				myLogger.warn("JsonParsing problem requesting '" + request.getAddress() +  "'", e);
				callback.onException(request, e);;
			} catch(IOException e) {
				myLogger.debug("Got IOException requesting '" + request.getAddress() + "'", e) ;
				callback.onException(request, e);;
			} finally {
				getMethod.releaseConnection() ;
			}
			return apiResponse;
		}
		

		public String buildUrl() throws UnsupportedEncodingException {
			return "https://maps.googleapis.com/maps/api/geocode/json?key=" + config.getApiKey() 
					+ "&address=" + URLEncoder.encode(request.getAddress(), "UTF-8");
		}
	}
}
