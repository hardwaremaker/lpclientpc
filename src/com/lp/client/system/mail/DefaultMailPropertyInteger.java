package com.lp.client.system.mail;

import java.util.Map;

import com.lp.server.system.mail.service.MailPropertyDto;
import com.lp.server.system.mail.service.MailPropertyEnum;

public abstract class DefaultMailPropertyInteger extends DefaultMailProperty<Integer> {

	public DefaultMailPropertyInteger(MailPropertyEnum propertyEnum) {
		super(propertyEnum);
	}

	public void loadProperty(Map<MailPropertyEnum, MailPropertyDto> stored) {
		MailPropertyDto dto = stored.get(property());
		
		Integer intValue = null;
		try {
			intValue = dto != null ? Integer.parseInt(dto.getCWert()) : null;
		} catch (NumberFormatException ex) {
			// TODO log
		}
		
		initValueHolder(intValue);
	}

}
