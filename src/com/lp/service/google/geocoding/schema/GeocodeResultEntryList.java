package com.lp.service.google.geocoding.schema;

import java.io.Serializable;
import java.util.List;

public class GeocodeResultEntryList implements Serializable {
	private static final long serialVersionUID = -6170098075235366363L;

	private List<GeocodeResultEntry> results;
	
	public GeocodeResultEntryList() {
	}

	public List<GeocodeResultEntry> getResults() {
		return results;
	}
	
	public void setResults(List<GeocodeResultEntry> results) {
		this.results = results;
	}
}
