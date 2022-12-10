package com.lp.service.findsteel;

import java.io.Serializable;

public class FindSteelApiBaseResponse implements Serializable {
	private static final long serialVersionUID = 7537492463149896450L;

	private boolean success;
	private String messageType;
	private String message;
	private String meta;
	
	public boolean getSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMeta() {
		return meta;
	}
	public void setMeta(String meta) {
		this.meta = meta;
	}	
}
