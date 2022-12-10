package com.lp.client.geodaten;

import com.lp.server.partner.service.KundeDto;

public class MapInteressent extends MapPartner {

	public MapInteressent(KundeDto kundeDto) {
		super(kundeDto.getPartnerDto(), kundeDto.getIId());
	}

}
