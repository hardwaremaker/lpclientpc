package com.lp.client.system.mail;

import com.lp.server.system.mail.service.MailPropertyEnum;

public class MailPropertySmtpPort extends DefaultMailPropertyInteger {

	public MailPropertySmtpPort() {
		super(MailPropertyEnum.MailSmtpPort);
	}

	public void setDataUI(MailPropertiesUI data) {
		data.setSmtpPort(valueHolder().get());
	}

	public void getDataUI(MailPropertiesUI data) {
		valueHolder().set(data.getSmtpPort());
	}

}
