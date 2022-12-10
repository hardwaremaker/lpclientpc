package com.lp.client.system.mail;

import com.lp.server.system.mail.service.MailPropertyEnum;

public class MailPropertyImapServer extends DefaultMailPropertyString {

	public MailPropertyImapServer() {
		super(MailPropertyEnum.MailImapHost);
	}

	@Override
	public void setDataUI(MailPropertiesUI data) {
		data.setImapServer(valueHolder().get());
	}

	@Override
	public void getDataUI(MailPropertiesUI data) {
		valueHolder().set(data.getImapServer());
	}

}
