package com.lp.service.google.geocoding;

import com.lp.client.frame.delegate.DelegateFactory;

public class DefaultGeocodeRequestConfig extends GeocodeRequestConfig {

	public DefaultGeocodeRequestConfig() throws Throwable {
		setApiKey(DelegateFactory.getInstance().getParameterDelegate().getGoogleApiKey());
		setMaxThreads(5);
	}

}
