package com.lp.service.google.geocoding.schema;

import java.io.Serializable;

public class GeocodeBoundsEntry implements Serializable {
	private static final long serialVersionUID = 554879070314943473L;

	private GeocodeCoordinatesEntry northeast;
	private GeocodeCoordinatesEntry southwest;
	
	public GeocodeBoundsEntry() {
	}

	public GeocodeCoordinatesEntry getNortheast() {
		return northeast;
	}
	
	public void setNortheast(GeocodeCoordinatesEntry northeast) {
		this.northeast = northeast;
	}
	
	public GeocodeCoordinatesEntry getSouthwest() {
		return southwest;
	}
	
	public void setSouthwest(GeocodeCoordinatesEntry southwest) {
		this.southwest = southwest;
	}
}
