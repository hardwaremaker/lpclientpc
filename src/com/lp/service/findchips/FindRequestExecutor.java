package com.lp.service.findchips;

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

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
// import org.apache.http.conn.params.ConnRoutePNames;
// import org.apache.http.conn.params.ConnRoutePNames;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lp.client.util.logger.LpLogger;

public class FindRequestExecutor {
	private final static LpLogger myLogger = (LpLogger) LpLogger.getInstance(FindRequestExecutor.class);
	
	private ExecutorService executorService ;
	private ObjectMapper jsonMapper ;
	private List<Future<FindchipsApiResponse>> answers ;
	private FindchipsConfig config ;
	
	public FindRequestExecutor() throws Throwable {
		this(new DefaultFindchipsConfig()) ;
	}
		
	public FindRequestExecutor(FindchipsConfig config) {
		this.config = config ;
		executorService = Executors.newFixedThreadPool(config.getMaxThreads()) ;
	}
		
	public void executeAll(List<FindchipsApiRequest> requests, 
			IFindchipsResponseCallback responseCallback) throws InterruptedException {		
		List<TheRequest> apiRequests = new ArrayList<FindRequestExecutor.TheRequest>() ;
		for (FindchipsApiRequest theRequest : requests) {
			apiRequests.add(new TheRequest(theRequest, responseCallback)) ;
		}
		
		answers = executorService.invokeAll(apiRequests) ;
	}
	
	public FindchipsApiResponse executeOne(FindchipsApiRequest request) throws InterruptedException, ExecutionException {
		return executeOne(request, new DefaultResponseCallback()) ; 
	}
	
	public FindchipsApiResponse executeOne(FindchipsApiRequest request, 
			IFindchipsResponseCallback callback) throws InterruptedException, ExecutionException {
		FindchipsApiResponse response = 
				executorService.submit(new TheRequest(request, callback)).get() ;
		executorService.shutdown(); 
		return response ;
	}
	
	public void awaitTermination() {
		try {
			for (Future<FindchipsApiResponse> answer : answers) {
				if(!answer.isDone()) {
						answer.get() ;
				}
			}
		} catch (InterruptedException e) {
			myLogger.debug("awaitingTermination failed '", e) ;
		} catch (ExecutionException e) {
			myLogger.debug("awaitingTermination failed '", e) ;
		}
		executorService.shutdown(); 
	}
	
	public void cancel() {
		for (Future<FindchipsApiResponse> answer : answers) {
			if(!answer.isDone()) {
				answer.cancel(true) ;
			}
		}
		
		executorService.shutdown(); 
	}
	
	private ObjectMapper getJsonMapper() {
		if(jsonMapper == null) {
			jsonMapper = new ObjectMapper() ;
			jsonMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true) ;
		}
		return jsonMapper ;
	}		

	private class TheRequest implements Callable<FindchipsApiResponse> {
		private FindchipsApiRequest request ;
		private IFindchipsResponseCallback callback ;
		
		public TheRequest(FindchipsApiRequest request, IFindchipsResponseCallback callback) {
			this.request = request ;
			this.callback = callback ;
		}
		
		@Override
		public FindchipsApiResponse call() throws Exception {
			HttpClient client = new HttpClient();
			if(config.getProxyConfig().isDefined()) {
				HostConfiguration hostConfiguration = client.getHostConfiguration() ;
				hostConfiguration.setProxy(config.getProxyConfig().getHost(), config.getProxyConfig().getPort());
//				
//				HttpHost proxyHost = new HttpHost(config.getProxyConfig().getHost(), config.getProxyConfig().getPort()) ;
//				client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost);
			}
			GetMethod getMethod = new GetMethod(buildUrl()) ;
			FindchipsApiResponse apiResponse = new FindchipsApiResponse() ;
			
			try {
				myLogger.debug("Calling API for partnr '" + request.getPartnr() +  "'...") ;
				int status = client.executeMethod(getMethod) ;

				myLogger.info("Called API for partnr '" + request.getPartnr() +  "' -> HTTP Status: <" + status + ">") ;
				if(status == HttpStatus.SC_OK) {
//					String s = getMethod.getResponseBodyAsString();
//					myLogger.debug("Done requesting partnr '" + request.getPartnr() + "<" + s + ">") ;
					myLogger.debug("Done requesting partnr '" + request.getPartnr() + "'.");
					apiResponse = getJsonMapper().readValue(getMethod.getResponseBodyAsStream(), FindchipsApiResponse.class) ;
					if(callback != null) {
						callback.onSuccess(request, apiResponse);
					}
				}
			} catch(HttpException e) {
				myLogger.debug("Got HttpException requesting '" + request.getPartnr() + "'", e) ;
				callback.onError(request);
			} catch(JsonMappingException e) {
				myLogger.warn("JsonMapping problem requesting '" + request.getPartnr() + "'", e);
				callback.onError(request);
			} catch (JsonParseException e) {
				myLogger.warn("JsonParsing problem requesting '" + request.getPartnr() +  "'", e);
				callback.onError(request);
			} catch(IOException e) {
				myLogger.debug("Got IOException requesting '" + request.getPartnr() + "'", e) ;
				callback.onError(request);
			} finally {
				getMethod.releaseConnection() ;
			}

			return apiResponse ;
		}
		
		public String buildUrl() throws UnsupportedEncodingException {
			return "http://api.findchips.com/v1/search?apiKey=" + config.getApiKey() 
					+ "&part=" + URLEncoder.encode(request.getPartnr(), "UTF-8") ;
		}		
	}
}
