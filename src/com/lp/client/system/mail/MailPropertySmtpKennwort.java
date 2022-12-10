package com.lp.client.system.mail;

import com.lp.server.system.mail.service.MailPropertyEnum;

public class MailPropertySmtpKennwort extends DefaultMailPropertyString {

	public MailPropertySmtpKennwort() {
		super(MailPropertyEnum.HvSmtpPwd);
	}

	@Override
	public void setDataUI(MailPropertiesUI data) {
//		data.setSmtpPasswort(valueHolder().get() == null ? null : MailPropertiesController.PwdMask);
		data.setSmtpPasswort(valueHolder().get());
	}

	@Override
	public void getDataUI(MailPropertiesUI data) {
		String pwd = data.getSmtpPasswort();
		valueHolder().set(
				pwd == null || MailPropertiesController.PwdMask.equals(pwd)
					? null : pwd);
	}

//	@Override
//	public boolean hasChanged() {
//		return false;
//	}
}
