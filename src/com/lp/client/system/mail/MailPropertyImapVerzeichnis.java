package com.lp.client.system.mail;

import com.lp.server.system.mail.service.MailPropertyEnum;

public class MailPropertyImapVerzeichnis extends DefaultMailPropertyString {

	public MailPropertyImapVerzeichnis() {
		super(MailPropertyEnum.HvImapFolder);
	}

	@Override
	public void setDataUI(MailPropertiesUI data) {
		data.setImapSentFolder(valueHolder().get());
	}

	@Override
	public void getDataUI(MailPropertiesUI data) {
		valueHolder().set(data.getImapSentFolder());
	}

}
