package com.lp.client.rechnung;

import com.lp.server.bestellung.service.OpenTransXmlReportResult;

public interface IOpenTransBeleg {

	boolean isOpenTransPartner();
	
	OpenTransXmlReportResult createOpenTransResult() throws Throwable;
}
