package com.lp.client.geodaten;

public class JSMap implements JSVariable {

	private String name;
	private String mapDiv;
	private JSMapOptions mapOptions;
	
	public JSMap(String mapDiv, JSMapOptions mapOptions) {
		setMapDiv(mapDiv);
		setMapOptions(mapOptions);
	}

	public void setMapDiv(String mapDiv) {
		this.mapDiv = mapDiv;
	}
	
	public String getMapDiv() {
		return mapDiv;
	}
	
	public void setMapOptions(JSMapOptions mapOptions) {
		this.mapOptions = mapOptions;
	}
	
	public JSMapOptions getMapOptions() {
		return mapOptions;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
