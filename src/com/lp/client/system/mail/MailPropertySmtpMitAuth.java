package com.lp.client.system.mail;

import com.lp.server.system.mail.service.MailPropertyEnum;

public class MailPropertySmtpMitAuth extends DefaultMailPropertyBoolean {

	public MailPropertySmtpMitAuth() {
		super(MailPropertyEnum.MailSmtpAuth);
	}

	@Override
	public void setDataUI(MailPropertiesUI data) {
		data.setSmtpMitAuth(valueHolder().get());
	}
	
	@Override
	public void getDataUI(MailPropertiesUI data) {
		valueHolder().set(data.getSmtpMitAuth());
	}
}
