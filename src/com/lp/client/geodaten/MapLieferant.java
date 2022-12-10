package com.lp.client.geodaten;

import com.lp.server.partner.service.LieferantDto;

public class MapLieferant extends MapPartner {

	public MapLieferant(LieferantDto lieferantDto) {
		super(lieferantDto.getPartnerDto(), lieferantDto.getIId());
	}

}
