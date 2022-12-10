package com.lp.service.google.geocoding;

public class GeocodeErrorException extends GeocodeException {
	private static final long serialVersionUID = 9156435987066015245L;

	private GeocodeResult result;
	
	public GeocodeErrorException(String address, GeocodeResult geocodeResult) {
		super("Die Geodaten-Abfrage fuer Adresse '" + address + "' lieferte kein Ergebnis. Status-Code der Antwort. " 
				+ (geocodeResult.getStatus() != null ? geocodeResult.getStatus().name() : "unknown"));
		setAddress(address);
		setResult(geocodeResult);
	}
	
	public GeocodeResult getResult() {
		return result;
	}
	public void setResult(GeocodeResult result) {
		this.result = result;
	}

}
