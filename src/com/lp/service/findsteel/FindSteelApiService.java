package com.lp.service.findsteel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lp.client.util.logger.LpLogger;
import com.lp.service.findsteel.schema.FindSteelApiSearchResponse;


public class FindSteelApiService implements IFindSteelApiService {
	private final static LpLogger myLogger = (LpLogger) LpLogger.getInstance(FindSteelApiService.class);

	private CloseableHttpClient cachedClient = null;
	private boolean closeClient = false;
	private ObjectMapper jsonMapper ;

	/**
	 * Der interne HttpClient wird nach jedem API Aufruf geschlossen
	 */
	public void beOneShotClient() {
		closeClient = true;
	}
	
	/**
	 * Der interne HttpClient wird nicht geschlossen, das muss der
	 * Aufrufer durch {@link #close()} explizit veranlassen
	 */
	public void beAliveClient() {
		closeClient = false;
	}
	
	/**
	 * Der interne HttpClient wird explizit geschlossen
	 */
	public void close() {
		forceCloseClient();
	}
	
	public FindSteelSessionInfo logon(String username, String password) throws UnsupportedEncodingException, IOException {
/*
	curl -c login.cookie -H "Content-Type: application/json" -H "Accept: application/json"
	   -X POST -d '{ "username": "username@tld", "password": "yourpassword"}' https://test.thesteel.com:4443/thesteel/rest/user/login/

 */
		FindSteelSessionInfo sessionInfo = null;
		try {
			HttpPost post = new HttpPost(getBaseUrl() + "/user/login/");
	        StringEntity stringEntity = new StringEntity(
	        		"{\"username\":\"" + username + "\"," +
	        		"\"password\":\"" + password + "\"}");
	        post.setEntity(stringEntity);
	        post.setHeader("Content-type", "application/json");
	        post.setHeader("Accept", "application/json");
	           
	        CloseableHttpResponse response = getHttpClient().execute(post);

	        try {
	        	if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
		        	HttpEntity responseEntity = response.getEntity();
		        	
		        	ByteArrayOutputStream os = new ByteArrayOutputStream();
		        	responseEntity.writeTo(os);
		        	String contentString = new String(os.toByteArray());
		        	sessionInfo = new FindSteelSessionInfo(response);
//		        	sessionInfo.setSessionId(extractSessionId(response));
//		        	sessionInfo.setXsfrToken(extractXsfrToken(response)); 	        		
		        	EntityUtils.consume(responseEntity);
	        	}
	        } finally {
	        	response.close();
	        }	         			
		} finally {
			closeClientImpl();
		}
		
		return sessionInfo;
	}
	
	public boolean logout(FindSteelSessionInfo sessionInfo) throws UnsupportedEncodingException, IOException  {
		boolean success = false;

		try {
			HttpPost post = new HttpPost(getBaseUrl() + "/user/logout/");
	        StringEntity stringEntity = new StringEntity("{}");
	        post.setEntity(stringEntity);
	        post.setHeader("Content-type", "application/json");
	        sessionInfo.apply(post);
//	        post.setHeader("Cookie", sessionInfo.getSessionId());
//	        post.setHeader("X-XSRF-TOKEN", sessionInfo.getXsfrToken());
	           
	        CloseableHttpResponse response = getHttpClient().execute(post);

	        try {
	        	if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	        		success = true;
		        	HttpEntity responseEntity = response.getEntity();
		        	EntityUtils.consume(responseEntity);
	        	}
	        } finally {
	        	response.close();
	        }	         			
		} finally {
			closeClientImpl();
		}
		
		return success;
	}
	
	public FindSteelApiSearchResponse search(FindSteelSessionInfo sessionInfo, String searchValue, 
			Integer pageNumber, Integer pageSize) throws UnsupportedEncodingException, IOException {
		FindSteelApiSearchResponse apiResponse = new FindSteelApiSearchResponse();
		try {
			HttpGet get= new HttpGet(getBaseUrl() + "/product/search"
				+ "?type=1" 
				+ "&query=" + URLEncoder.encode(searchValue, "UTF-8")
				+ "&page=" + pageNumber.toString() 
				+ "&pageSize=" + pageSize.toString()
				+ "&filter=");
	        get.setHeader("Accept", "application/json");
	        sessionInfo.apply(get);
//	        get.setHeader("Cookie", sessionInfo.getSessionId());
//	        get.setHeader("X-XSRF-TOKEN", sessionInfo.getXsfrToken());
	        CloseableHttpResponse response = getHttpClient().execute(get);

	        try {
	        	if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
		        	HttpEntity responseEntity = response.getEntity();
		        	
					apiResponse = getJsonMapper().readValue(
							responseEntity.getContent(), FindSteelApiSearchResponse.class) ;
		        	
//		        	ByteArrayOutputStream os = new ByteArrayOutputStream();
//		        	responseEntity.writeTo(os);
//		        	content = new String(os.toByteArray());
		        	EntityUtils.consume(responseEntity);
	        	}
	        } catch(JsonMappingException e) {	        	
	        	myLogger.error("JsonMappingException", e);
	        } catch(JsonParseException e){
	        	myLogger.error("JsonParseException", e);
	        } finally {
	        	response.close();
	        }	         			
		} finally {
			closeClientImpl();
		}
		
		return apiResponse;
	}
	
	protected CloseableHttpClient getHttpClient() {
		if(cachedClient == null) {
		 cachedClient = HttpClients.createDefault();
		}
		return cachedClient;
	}

	protected void closeClientImpl() {
		if(closeClient) {
			forceCloseClient();
		}
	}

	private void forceCloseClient() {
		if(cachedClient != null) {
			try {
				cachedClient.close();
			} catch(IOException e) {
			}
			cachedClient = null;
		}		
	}
	
	private ObjectMapper getJsonMapper() {
		if(jsonMapper == null) {
			jsonMapper = new ObjectMapper() ;
			jsonMapper
				.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
				.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
//				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		return jsonMapper ;
	}		
	
	protected String getBaseUrl() {
		return "https://test.thesteel.com:4443/thesteel/rest";
	}
	
//	private String extractSessionId(CloseableHttpResponse response) {
//    	Header[] h = response.getHeaders("Set-Cookie");
//    	if(h != null && h.length > 0 && h[0].getValue().startsWith("JSESSIONID=")) {
//    		return h[0].getValue();
//    	}
//
//    	logHeaders("Couldn't find Set-Cookie Line", response.getAllHeaders());
//		return null;
//	}

//	private String extractXsfrToken(CloseableHttpResponse response) {
//    	Header[] h = response.getHeaders("X-XSRF-TOKEN");
//    	if(h != null && h.length > 0) {
//    		return h[0].getValue();
//    	}
//
//    	logHeaders("Couldn't find X-XSRF-TOKEN Line", response.getAllHeaders());
//		return null;
//	}
//	

//	private void logHeaders(String message, Header[] headers) {
//		if(headers == null) {
//			myLogger.warn(message + ", there are no Headers received!");
//		} else {
//			myLogger.warn(message + ", dump of headers follows {");
//			for (Header header : headers) {
//				myLogger.warn("Header: " + header.getName() + "=" + header.getValue());
//			}
//			myLogger.warn("} dump of headers done.");
//		}
//	}
}
