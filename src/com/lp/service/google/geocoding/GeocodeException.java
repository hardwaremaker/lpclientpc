package com.lp.service.google.geocoding;

public class GeocodeException extends Exception {
	private static final long serialVersionUID = 7626737850156453769L;

	private String address;
	
	public GeocodeException(String message) {
		super(message);
	}
	
	public GeocodeException(String message, Exception e) {
		super(message, e);
	}
	
	protected void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
}
