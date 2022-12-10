package com.lp.service.google.geocoding;

import com.lp.service.google.geocoding.schema.GeocodeApiResponse;

public interface IGeocodeResponseCallback {

	void onSuccess(GeocodeApiRequest request, GeocodeApiResponse response);
	
	void onError(GeocodeApiRequest request);
	
	void onException(GeocodeApiRequest request, Exception e);
}
