package com.lp.service.google.geocoding.schema;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lp.util.Helper;

public class GeocodeApiResponse implements Serializable {
	private static final long serialVersionUID = 7161653783825153150L;

	private GeocodeResultStatusEntry status;
	private List<GeocodeResultEntry> results;
	@JsonProperty("error_message")
	private String errorMessage;
	
	public GeocodeApiResponse() {
	}

	public GeocodeResultStatusEntry getStatus() {
		return status;
	}
	
	public void setStatus(GeocodeResultStatusEntry status) {
		this.status = status;
	}
	
	public List<GeocodeResultEntry> getResults() {
		return results;
	}
	
	public void setResults(List<GeocodeResultEntry> results) {
		this.results = results;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public boolean isSuccessful() {
		return GeocodeResultStatusEntry.OK.equals(getStatus()) 
				|| GeocodeResultStatusEntry.ZERO_RESULTS.equals(getStatus());
	}
	
	public boolean hasResult() {
		return GeocodeResultStatusEntry.OK.equals(getStatus());
	}

	public String asString() {
		return "GeocodeApiResponse [status = '" + getStatus() + "', number of results = " + getResults().size()  
				+ (Helper.isStringEmpty(getErrorMessage()) ? "" : ", error message = '" + getErrorMessage() + "'")
				+ "]";
	}
}
