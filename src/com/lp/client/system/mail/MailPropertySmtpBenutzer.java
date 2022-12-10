package com.lp.client.system.mail;

import com.lp.server.system.mail.service.MailPropertyEnum;

public class MailPropertySmtpBenutzer extends DefaultMailPropertyString {
	
	public MailPropertySmtpBenutzer() {
		super(MailPropertyEnum.MailSmtpUser);
	}
	
	@Override
	public void setDataUI(MailPropertiesUI data) {
		data.setSmtpBenutzer(valueHolder().get());
	}

	@Override
	public void getDataUI(MailPropertiesUI data) {
		valueHolder().set(data.getSmtpBenutzer());
	}

}
