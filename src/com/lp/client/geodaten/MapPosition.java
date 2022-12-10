package com.lp.client.geodaten;

import com.lp.server.partner.service.GeodatenDto;

public class MapPosition {
	
	private GeodatenDto geodatenDto;
	private String title;
	
	public MapPosition(GeodatenDto geodatenDto, String title) {
		setGeodatenDto(geodatenDto);
		setTitle(title);
	}

	public GeodatenDto getGeodatenDto() {
		return geodatenDto;
	}
	
	public void setGeodatenDto(GeodatenDto geodatenDto) {
		this.geodatenDto = geodatenDto;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String asString() {
		return this.getClass() + " [partnerIId = " + getGeodatenDto().getPartnerIId() + ", title = " + getTitle() + "]";
	}
}
