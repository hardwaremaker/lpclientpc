package com.lp.service.google.geocoding;

public class GeocodeRequestException extends GeocodeException {
	private static final long serialVersionUID = -7926933401023157988L;

	public GeocodeRequestException(String address, Exception e) {
		super("Fehler bei Durchfuehrung des Request fuer Adresse '" + address + "'", e);
		setAddress(address);
	}
}
