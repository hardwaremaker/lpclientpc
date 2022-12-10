package com.lp.client.remote;

import com.lp.server.system.service.PayloadDto;

public interface IPayloadPublisher {

	void publishPayload(PayloadDto payload);
}
