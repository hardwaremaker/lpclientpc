package com.lp.client.frame.report;

import com.lp.server.rechnung.service.ZugferdResult;

public interface IZugferdBeleg {

	ZugferdResult createZugferdResult() throws Throwable;
	
	boolean isZugferdPartner() throws Throwable;
	
}
