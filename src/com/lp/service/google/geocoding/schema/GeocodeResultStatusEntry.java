package com.lp.service.google.geocoding.schema;

public enum GeocodeResultStatusEntry {
	OK,
	ZERO_RESULTS,
	OVER_QUERY_LIMIT,
	REQUEST_DENIED,
	INVALID_REQUEST,
	UNKNOWN_ERROR;
}
