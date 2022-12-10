package com.lp.service.findchips;

import java.io.Serializable;

public class FindchipsMetadata implements Serializable {
	private static final long serialVersionUID = 9203677072490580619L;

	private String queryTime ;
	private String timeStamp ;
	private String message ;
	private FindchipsRequest requestData ;
	
	public String getQueryTime() {
		return queryTime;
	}
	public void setQueryTime(String queryTime) {
		this.queryTime = queryTime;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timestamp) {
		this.timeStamp = timestamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public FindchipsRequest getRequestData() {
		return requestData;
	}
	public void setRequestData(FindchipsRequest requestData) {
		this.requestData = requestData;
	}	
}
