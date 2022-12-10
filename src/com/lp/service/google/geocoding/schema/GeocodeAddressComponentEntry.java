package com.lp.service.google.geocoding.schema;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeocodeAddressComponentEntry implements Serializable {
	private static final long serialVersionUID = -1921060970156715386L;

	@JsonProperty(value="long_name")
	private String longName;
	@JsonProperty(value="short_name")
	private String shortName;
	private List<GeocodeAddressComponentTypeEntry> types; 
	
	public GeocodeAddressComponentEntry() {
	}

	public String getLongName() {
		return longName;
	}
	
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public List<GeocodeAddressComponentTypeEntry> getTypes() {
		return types;
	}
	
	public void setTypes(List<GeocodeAddressComponentTypeEntry> types) {
		this.types = types;
	}
}
