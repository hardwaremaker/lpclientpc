package com.lp.service.google.geocoding.schema;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeocodeResultEntry implements Serializable {
	private static final long serialVersionUID = 1051816805998838398L;

	@JsonProperty("formatted_address")
	private String formattedAddress;
	@JsonProperty("address_components")
	private List<GeocodeAddressComponentEntry> adressComponents;
	private GeocodeGeometryEntry geometry;
	@JsonProperty("place_id")
	private String placeId;
	private List<GeocodeAddressComponentTypeEntry> types;
	@JsonProperty("partial_match")
	private Boolean partialMatch;
	
	public GeocodeResultEntry() {
	}

	public String getFormattedAddress() {
		return formattedAddress;
	}
	
	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}
	
	public List<GeocodeAddressComponentEntry> getAdressComponents() {
		return adressComponents;
	}
	
	public void setAdressComponents(
			List<GeocodeAddressComponentEntry> adressComponents) {
		this.adressComponents = adressComponents;
	}
	
	public GeocodeGeometryEntry getGeometry() {
		return geometry;
	}
	
	public void setGeometry(GeocodeGeometryEntry geometry) {
		this.geometry = geometry;
	}
	
	public String getPlaceId() {
		return placeId;
	}
	
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	
	public List<GeocodeAddressComponentTypeEntry> getTypes() {
		return types;
	}
	
	public void setTypes(List<GeocodeAddressComponentTypeEntry> types) {
		this.types = types;
	}
	
	public Boolean getPartialMatch() {
		return partialMatch;
	}
	
	public void setPartialMatch(Boolean partialMatch) {
		this.partialMatch = partialMatch;
	}
}
