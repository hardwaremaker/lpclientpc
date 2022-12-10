package com.lp.service.findsteel;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.lp.client.util.logger.LpLogger;

public class FindSteelSessionInfo {
	private final static LpLogger myLogger = (LpLogger) LpLogger.getInstance(FindSteelSessionInfo.class);

	private String sessionId;
	private String xsfrToken;
	
	public FindSteelSessionInfo() {
	}
	
	public FindSteelSessionInfo(CloseableHttpResponse response) {
		extract(response);
	}
	
	public void extract(CloseableHttpResponse response) {
		setSessionId(extractSessionId(response));
		setXsfrToken(extractXsfrToken(response));
	}
	
	private String extractSessionId(CloseableHttpResponse response) {
    	Header[] h = response.getHeaders("Set-Cookie");
    	if(h != null && h.length > 0) {
    		for (Header header : h) {
        		if(header.getValue().startsWith("JSESSIONID=")) {
        			return header.getValue();
        		}
			}
    	}

    	logHeaders("Couldn't find Set-Cookie Line", response.getAllHeaders());
		return null;
	}
	
	private String extractXsfrToken(CloseableHttpResponse response) {
    	Header[] h = response.getHeaders("Set-Cookie");
    	if(h != null && h.length > 0) {
    		for (Header header : h) {
        		if(header.getValue().startsWith("XSRF-TOKEN=")) {
        			String s = header.getValue().substring(11);
        			String[] t = s.split(";");
        			if(t.length == 2) {
            			return t[0];        				
        			}
        		}
			}
    	}

    	logHeaders("Couldn't find Set-Cookie XSRF-TOKEN Line", response.getAllHeaders());
		return null;
	}
	
	public void apply(HttpRequestBase httpMethod) {
		httpMethod.setHeader("Cookie", getSessionId());
		httpMethod.setHeader("X-XSRF-TOKEN", getXsfrToken());		
	}
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getXsfrToken() {
		return xsfrToken;
	}
	public void setXsfrToken(String xsfrToken) {
		this.xsfrToken = xsfrToken;
	}

	private void logHeaders(String message, Header[] headers) {
		if(headers == null) {
			myLogger.warn(message + ", there are no Headers received!");
		} else {
			myLogger.warn(message + ", dump of headers follows {");
			for (Header header : headers) {
				myLogger.warn("Header: " + header.getName() + "=" + header.getValue());
			}
			myLogger.warn("} dump of headers done.");
		}
	}
}
