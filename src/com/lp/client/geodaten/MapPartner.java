package com.lp.client.geodaten;

import com.lp.server.partner.service.PartnerDto;

public class MapPartner extends MapPosition {
	
	private PartnerDto partnerDto;
	private Integer id;
	
	public MapPartner(PartnerDto partnerDto) {
		this(partnerDto, partnerDto.getIId());
	}
	
	public MapPartner(PartnerDto partnerDto, Integer id) {
		super(partnerDto.getGeodatenDto(), partnerDto.formatFixName1Name2());
		setPartnerDto(partnerDto);
		setId(id);
		setTitle(createTitle());
	}

	private String createTitle() {
		return partnerDto.formatFixName1Name2() + "\n" + partnerDto.formatAdresse();
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public PartnerDto getPartnerDto() {
		return partnerDto;
	}
	
	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}
	
	@Override
	public String asString() {
		return this.getClass() + " [id = " + getId() + ", partnerIId = " + getGeodatenDto().getPartnerIId() + ", title = " + getTitle() + ", adresse = " + getPartnerDto().formatAdresse() + ""
				+ ", geodaten = " + getGeodatenDto().getBreitengrad() + ", " + getGeodatenDto().getLaengengrad() + "]";
	}
}
