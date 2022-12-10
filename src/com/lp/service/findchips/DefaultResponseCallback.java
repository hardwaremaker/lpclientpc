package com.lp.service.findchips;

import com.lp.client.util.logger.LpLogger;

public class DefaultResponseCallback implements IFindchipsResponseCallback {
	private final static LpLogger myLogger = (LpLogger) LpLogger.getInstance(DefaultResponseCallback.class);

	@Override
	public void onError(FindchipsApiRequest request) {			
		myLogger.error("Defaultresponse.onError for '" + request.getPartnr() + "'.");
	}
	
	@Override
	public void onSuccess(FindchipsApiRequest request,
			FindchipsApiResponse response) {			
		myLogger.info("Defaultresponse.onSuccess for '" + request.getPartnr() + "'.");
	}
	
	@Override
	public void onException(FindchipsApiRequest request, Exception e) {
		myLogger.error("Defaultresponse.onException for '" + request.getPartnr() + "'.", e);			
	}
}