package com.lp.client.system.mail;

import com.lp.server.system.mail.service.MailPropertyEnum;

public class MailPropertyImapPort extends DefaultMailPropertyInteger {

	public MailPropertyImapPort() {
		super(MailPropertyEnum.MailImapPort);
	}
	
	@Override
	public void setDataUI(MailPropertiesUI data) {
		data.setImapPort(valueHolder().get());
	}

	@Override
	public void getDataUI(MailPropertiesUI data) {
		valueHolder().set(data.getImapPort());
	}

}
