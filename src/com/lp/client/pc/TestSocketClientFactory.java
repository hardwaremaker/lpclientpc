package com.lp.client.pc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jboss.remoting.InvokerLocator;
import org.jboss.remoting.transport.ClientInvoker;
import org.jboss.remoting.transport.socket.SocketClientInvoker;
import org.jboss.remoting.transport.socket.TransportClientFactory;

public class TestSocketClientFactory extends TransportClientFactory {
	public ClientInvoker createClientInvoker(InvokerLocator locator, Map config) throws IOException {
		if(config == null) {
			config = new HashMap() ;
		}
		config.put(SocketClientInvoker.SO_TIMEOUT_DEFAULT, "10000") ;
		return super.createClientInvoker(locator, config) ;
	}
}
