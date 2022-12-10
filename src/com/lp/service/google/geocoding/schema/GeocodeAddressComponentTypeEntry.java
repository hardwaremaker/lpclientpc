package com.lp.service.google.geocoding.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lp.client.util.logger.LpLogger;

public enum GeocodeAddressComponentTypeEntry {
	STREET_ADDRESS("street_address"),
	ROUTE("route"),
	INTERSECTION("intersection"),
	POLITICAL("political"),
	COUNTRY("country"),
    ADMINISTRATIVE_AREA_LEVEL_1("administrative_area_level_1"),
    ADMINISTRATIVE_AREA_LEVEL_2("administrative_area_level_2"),
    ADMINISTRATIVE_AREA_LEVEL_3("administrative_area_level_3"),
    ADMINISTRATIVE_AREA_LEVEL_4("administrative_area_level_4"),
    ADMINISTRATIVE_AREA_LEVEL_5("administrative_area_level_5"),
    COLLOQUIAL_AREA("colloquial_area"),
    LOCALITY("locality"), 
    SUBLOCALITY("sublocality"),
    NEIGHBORHOOD("neighborhodd"),
    PREMISE("premise"), 
    SUBPREMISE("subpremise"), 
    POSTAL_CODE("postal_code"), 
    NATURAL_FEATURE("natural_feature"),
    AIRPORT("airport"),
    PARK("park"),
    POINT_OF_INTEREST("point_of_interest"),
    FLOOR("floor"),
    ESTABLISHMENT("establishment"),
    PARKING("parking"),
    POST_BOX("post_box"),
    POSTAL_TOWN("postal_town"),
    ROOM("room"),
    STREET_NUMBER("street_number"),
    BUS_STATION("bus_station"),
    TRAIN_STATION("train_station"),
    TRANSIT_STATION("transit_station"),
    UNKNOWN("unknown");
	
	private String value;
	protected final static LpLogger myLogger = (LpLogger) LpLogger.getInstance(GeocodeAddressComponentTypeEntry.class);
	
	private GeocodeAddressComponentTypeEntry(String value) {
		this.value = value;
	}
	
	public String getText() {
		return value;
	}
	
	@JsonCreator
	public static GeocodeAddressComponentTypeEntry fromString(String text) {
		if (text != null) {
			for (GeocodeAddressComponentTypeEntry type : GeocodeAddressComponentTypeEntry.values()) {
				if (text.equalsIgnoreCase(type.getText())) {
					return type;
				}
			}
		}
		myLogger.warn("Unknown GeocodeAddressComponentTypeEntry enum value '" + text + "'. Return enum '" + UNKNOWN + "'.");
		return UNKNOWN;
	}
}
