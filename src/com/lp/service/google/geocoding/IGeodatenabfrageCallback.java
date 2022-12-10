package com.lp.service.google.geocoding;


public interface IGeodatenabfrageCallback {

	void processResult(GeocodeResult result);
	
	void done();

	void doneByError(Throwable e);
}
