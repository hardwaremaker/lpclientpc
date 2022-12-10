package com.lp.service.findchips;

import java.io.Serializable;

public class FindchipsRequest implements Serializable {
	private static final long serialVersionUID = 5786174189749632468L;

	private String apiKey ;
	private String part ;
	private int limit ;
	private boolean exactMatch ;
	private int softWaitTime ;
	private int hardWaitTime ;
	private boolean useHardWait ;
	private boolean hostedOnly ;
	private boolean authorizedOnly ;
	private String country ;
	private boolean startWith;
	
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getPart() {
		return part;
	}
	public void setPart(String part) {
		this.part = part;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public boolean isExactMatch() {
		return exactMatch;
	}
	public void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}
	public int getSoftWaitTime() {
		return softWaitTime;
	}
	public void setSoftWaitTime(int softWaitTime) {
		this.softWaitTime = softWaitTime;
	}
	public int getHardWaitTime() {
		return hardWaitTime;
	}
	public void setHardWaitTime(int hardWaitTime) {
		this.hardWaitTime = hardWaitTime;
	}
	public boolean isUseHardWait() {
		return useHardWait;
	}
	public void setUseHardWait(boolean useHardWait) {
		this.useHardWait = useHardWait;
	}
	public boolean isHostedOnly() {
		return hostedOnly;
	}
	public void setHostedOnly(boolean hostedOnly) {
		this.hostedOnly = hostedOnly;
	}
	public boolean isAuthorizedOnly() {
		return authorizedOnly;
	}
	public void setAuthorizedOnly(boolean authorizedOnly) {
		this.authorizedOnly = authorizedOnly;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public boolean isStartWith() {
		return startWith;
	}
	public void setStartWith(boolean startWith) {
		this.startWith = startWith;
	}
}
