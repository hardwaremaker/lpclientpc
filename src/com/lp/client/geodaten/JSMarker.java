package com.lp.client.geodaten;

public class JSMarker implements JSVariable {

	private String name;
	private JSMarkerOptions markerOptions;
	
	public JSMarker(JSMarkerOptions markerOptions) {
		setMarkerOptions(markerOptions);
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setMarkerOptions(JSMarkerOptions markerOptions) {
		this.markerOptions = markerOptions;
	}
	
	public JSMarkerOptions getMarkerOptions() {
		return markerOptions;
	}
}
