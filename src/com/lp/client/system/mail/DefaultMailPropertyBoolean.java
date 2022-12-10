package com.lp.client.system.mail;

import java.util.Map;

import com.lp.server.system.mail.service.MailPropertyDto;
import com.lp.server.system.mail.service.MailPropertyEnum;

public abstract class DefaultMailPropertyBoolean extends DefaultMailProperty<Boolean> {

	public DefaultMailPropertyBoolean(MailPropertyEnum propertyEnum) {
		super(propertyEnum);
	}

	@Override
	public void loadProperty(Map<MailPropertyEnum, MailPropertyDto> stored) {
		MailPropertyDto dto = stored.get(property());
		initValueHolder(dto != null && dto.getCWert() != null
				? Boolean.valueOf(dto.getCWert())
				: Boolean.FALSE);
	}

}
