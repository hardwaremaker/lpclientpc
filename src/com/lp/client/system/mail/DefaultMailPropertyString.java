package com.lp.client.system.mail;

import java.util.Map;

import com.lp.server.system.mail.service.MailPropertyDto;
import com.lp.server.system.mail.service.MailPropertyEnum;

public abstract class DefaultMailPropertyString extends DefaultMailProperty<String> {

	public DefaultMailPropertyString(MailPropertyEnum propertyEnum) {
		super(propertyEnum);
	}

	public void loadProperty(Map<MailPropertyEnum, MailPropertyDto> stored) {
		MailPropertyDto dto = stored.get(property());
		initValueHolder(dto != null ? dto.getCWert() : null);
	}
}
