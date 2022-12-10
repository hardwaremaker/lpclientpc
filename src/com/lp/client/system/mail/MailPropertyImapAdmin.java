package com.lp.client.system.mail;

import com.lp.server.system.mail.service.MailPropertyEnum;

public class MailPropertyImapAdmin extends DefaultMailPropertyString {

	public MailPropertyImapAdmin() {
		super(MailPropertyEnum.HvImapAdmin);
	}

	@Override
	public void setDataUI(MailPropertiesUI data) {
		data.setImapAdmin(valueHolder().get());
	}

	@Override
	public void getDataUI(MailPropertiesUI data) {
		valueHolder().set(data.getImapAdmin());
	}

}
