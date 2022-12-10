package com.lp.client.remote;

import java.util.HashMap;
import java.util.Map;

import com.lp.server.system.service.PayloadDto;

/**
 * 
 * @author andi
 *
 */
public class Router {
	
	private static Router instance;
	private Map<String, IPayloadPublisher> references = new HashMap<String, IPayloadPublisher>();
	
	private Router() {}
	
	public static Router getInstance() {
		if (Router.instance == null) {
			Router.instance = new Router();
		}
		return Router.instance;
	}

	public void register(String workerReference, IPayloadPublisher publisher) {
		references.put(workerReference, publisher);
	}

	public void unregister(String workerReference) {
		references.remove(workerReference);
	}
	
	public void delegate(PayloadDto payload) {
		IPayloadPublisher publisher = references.get(payload.getReference());
		if (publisher != null) {
			publisher.publishPayload(payload);
		}
	}
}
