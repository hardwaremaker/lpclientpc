package com.lp.client.geodaten;


public class JSLatLng {
	
	private String lat;
	private String lng;
	
	public JSLatLng() {
	}
	
	public JSLatLng(String lat, String lng) {
		setLat(lat);
		setLng(lng);
	}

	public void setLat(String lat) {
		this.lat = lat;
	}
	
	public String getLat() {
		return lat;
	}
	
	public void setLng(String lng) {
		this.lng = lng;
	}
	
	public String getLng() {
		return lng;
	}
}
