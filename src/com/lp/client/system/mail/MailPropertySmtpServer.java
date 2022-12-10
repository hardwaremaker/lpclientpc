package com.lp.client.system.mail;

import com.lp.server.system.mail.service.MailPropertyEnum;

public class MailPropertySmtpServer extends DefaultMailPropertyString {
	
	public MailPropertySmtpServer() {
		super(MailPropertyEnum.MailSmtpHost);
	}

	public void setDataUI(MailPropertiesUI data) {
		data.setSmtpServer(valueHolder().get());
	}
	
	public void getDataUI(MailPropertiesUI data) {
		valueHolder().set(data.getSmtpServer());
	}
	
}