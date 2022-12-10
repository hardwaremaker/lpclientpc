package com.lp.client.system.mail;

import com.lp.server.system.mail.service.MailPropertyEnum;

public class MailPropertyImapKennwort extends DefaultMailPropertyString {

	public MailPropertyImapKennwort() {
		super(MailPropertyEnum.HvImapPwd);
	}

	@Override
	public void setDataUI(MailPropertiesUI data) {
//		data.setImapPasswort(valueHolder().get() == null ? null : MailPropertiesController.PwdMask);
		data.setImapPasswort(valueHolder().get());
	}

	@Override
	public void getDataUI(MailPropertiesUI data) {
		String pwd = data.getImapPasswort();
		valueHolder().set(
				pwd == null || MailPropertiesController.PwdMask.equals(pwd)
					? null : pwd);
	}

//	@Override
//	public boolean hasChanged() {
//		return false;
//	}
}
