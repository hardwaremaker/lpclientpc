package com.lp.service.google.geocoding.schema;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeocodeCoordinatesEntry implements Serializable {
	private static final long serialVersionUID = -846188998811036641L;

	@JsonProperty(value="lat")
	private String latitude;
	@JsonProperty(value="lng")
	private String longitude;
	
	public GeocodeCoordinatesEntry() {
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
