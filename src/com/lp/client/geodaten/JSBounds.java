package com.lp.client.geodaten;

public class JSBounds {

	private JSLatLng center;
	private JSLatLng northEast;
	private JSLatLng southWest;
	
	public JSBounds() {
	}
	
	public JSLatLng getCenter() {
		return center;
	}
	
	public void setCenter(JSLatLng center) {
		this.center = center;
	}
	
	public JSLatLng getNorthEast() {
		return northEast;
	}
	
	public void setNorthEast(JSLatLng northEast) {
		this.northEast = northEast;
	}
	
	public JSLatLng getSouthWest() {
		return southWest;
	}
	
	public void setSouthWest(JSLatLng southWest) {
		this.southWest = southWest;
	}
}
