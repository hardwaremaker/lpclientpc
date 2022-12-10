package com.lp.client.geodaten;

import com.lp.server.partner.service.KundeDto;

public class MapKunde extends MapPartner {

	public MapKunde(KundeDto kundeDto) {
		super(kundeDto.getPartnerDto(), kundeDto.getIId());
	}

}
