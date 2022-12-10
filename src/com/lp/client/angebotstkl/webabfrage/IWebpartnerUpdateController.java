package com.lp.client.angebotstkl.webabfrage;

import java.util.List;

import com.lp.server.angebotstkl.service.EkweblieferantDto;
import com.lp.server.angebotstkl.service.IWebpartnerDto;

public interface IWebpartnerUpdateController {

	public Integer actionUpdateWebpartner(IWebpartnerDto webpartnerDto);
	
	public List<EkweblieferantDto> getEkweblieferanten();
}
