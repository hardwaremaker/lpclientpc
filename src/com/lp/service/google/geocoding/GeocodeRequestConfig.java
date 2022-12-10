package com.lp.service.google.geocoding;

public class GeocodeRequestConfig {

	private String apiKey;
	private int maxThreads;
	
	public GeocodeRequestConfig() {
	}

	public String getApiKey() {
		return apiKey;
	}
	
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	public int getMaxThreads() {
		return maxThreads;
	}
	
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}
}
