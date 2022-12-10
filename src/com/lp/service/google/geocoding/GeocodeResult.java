package com.lp.service.google.geocoding;

import com.lp.server.partner.service.GeodatenDto;
import com.lp.service.google.geocoding.schema.GeocodeResultStatusEntry;

public class GeocodeResult {
	
	private GeocodeResultStatusEntry status;
	private GeodatenDto geodatenDto;
	private Integer id;
	private boolean successful = false;
	private String errorMessage;
	
	public GeocodeResult(GeocodeResultStatusEntry status, GeodatenDto geodatenDto) {
		setStatus(status);
		setGeodatenDto(geodatenDto);
		setSuccessful(true);
	}
	
	public GeocodeResult() {
	}
	
	public GeocodeResultStatusEntry getStatus() {
		return status;
	}
	
	public void setStatus(GeocodeResultStatusEntry status) {
		this.status = status;
	}
	
	public GeodatenDto getGeodatenDto() {
		return geodatenDto;
	}
	
	public void setGeodatenDto(GeodatenDto geodatenDto) {
		this.geodatenDto = geodatenDto;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
	
	public boolean isSuccessful() {
		return successful;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
