package com.lp.service.findchips;

import com.lp.server.system.service.HttpProxyConfig;

public class FindchipsConfig {
	private String apiKey ;
	private HttpProxyConfig proxyConfig ;
	private int maxThreads ;
	
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public HttpProxyConfig getProxyConfig() {
		return proxyConfig;
	}
	public void setProxyConfig(HttpProxyConfig proxyConfig) {
		this.proxyConfig = proxyConfig;
	}
	public int getMaxThreads() {
		return maxThreads;
	}
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}	
}
