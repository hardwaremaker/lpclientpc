package com.lp.service.google.geocoding.schema;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeocodeGeometryEntry implements Serializable {
	private static final long serialVersionUID = -2958332999456108785L;

	private GeocodeCoordinatesEntry location;
	@JsonProperty("location_type")
	private GeocodeLocationTypeEntry locationType;
	private GeocodeBoundsEntry viewport;
	private GeocodeBoundsEntry bounds;
	
	public GeocodeGeometryEntry() {
	}

	public GeocodeCoordinatesEntry getLocation() {
		return location;
	}
	
	public void setLocation(GeocodeCoordinatesEntry location) {
		this.location = location;
	}
	
	public GeocodeLocationTypeEntry getLocationType() {
		return locationType;
	}
	
	public void setLocationType(GeocodeLocationTypeEntry locationType) {
		this.locationType = locationType;
	}
	
	public GeocodeBoundsEntry getViewport() {
		return viewport;
	}
	
	public void setViewport(GeocodeBoundsEntry viewport) {
		this.viewport = viewport;
	}
	
	public GeocodeBoundsEntry getBounds() {
		return bounds;
	}
	
	public void setBounds(GeocodeBoundsEntry bounds) {
		this.bounds = bounds;
	}
}
