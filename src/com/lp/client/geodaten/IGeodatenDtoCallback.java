package com.lp.client.geodaten;

import com.lp.server.partner.service.GeodatenDto;

public interface IGeodatenDtoCallback {

	void onResult(GeodatenDto geodatenDto);
}
