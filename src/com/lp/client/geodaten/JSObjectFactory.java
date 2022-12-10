package com.lp.client.geodaten;

import java.math.BigDecimal;

public class JSObjectFactory {

	public JSObjectFactory() {
	}

	public JSLatLng createLatLng(String latitude, String longitude) {
		return new JSLatLng(latitude, longitude);
	}
	
	public JSLatLng createLatLng(BigDecimal latitude, BigDecimal longitude) {
		return createLatLng(latitude.toPlainString(), longitude.toPlainString());
	}

	public JSMapOptions createMapOptionsWithDisabledDefaultUI(JSLatLng center, Integer zoomLevel) {
		JSMapOptions mapOptions = new JSMapOptions(center, zoomLevel);
		mapOptions.setDisableDefaultUI(Boolean.TRUE);
		return mapOptions;
	}
	
	public JSMap createMap(String mapDiv, JSMapOptions mapOptions) {
		return new JSMap(mapDiv, mapOptions);
	}
	
	public JSMap createMapWithDisabledDefaultUI(String mapDiv, JSLatLng center, Integer zoomLevel) {
		return createMap(mapDiv, createMapOptionsWithDisabledDefaultUI(center, zoomLevel));
	}
	
	public JSMarkerOptions createMarkerOptions(JSVariable map, JSLatLng position) {
		return new JSMarkerOptions(map, position);
	}

	public JSMarkerOptions createMarkerOptions(JSVariable map, JSLatLng position, String title) {
		JSMarkerOptions markerOptions = new JSMarkerOptions(map, position);
		markerOptions.setTitle(title);
		return markerOptions;
	}
	
	public JSMarker createMarker(JSVariable map, JSLatLng position) {
		return new JSMarker(createMarkerOptions(map, position));
	}
	
	public JSMarker createMarker(JSVariable map, JSLatLng position, String title) {
		return new JSMarker(createMarkerOptions(map, position, title));
	}
}
