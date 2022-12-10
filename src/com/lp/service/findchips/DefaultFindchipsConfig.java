package com.lp.service.findchips;

import com.lp.client.frame.delegate.DelegateFactory;

public class DefaultFindchipsConfig extends FindchipsConfig {
	public DefaultFindchipsConfig() throws Throwable {
		setApiKey(DelegateFactory.getInstance().getParameterDelegate().getFindChipsApiKey());
		setProxyConfig(DelegateFactory.getInstance().getParameterDelegate().getHttpProxy());
		setMaxThreads(5) ;
	}
}
