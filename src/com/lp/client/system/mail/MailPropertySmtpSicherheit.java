package com.lp.client.system.mail;

import java.util.Map;

import com.lp.server.system.mail.service.MailPropertyDto;
import com.lp.server.system.mail.service.MailPropertyEnum;

public class MailPropertySmtpSicherheit implements IMailPropertyCtrl {

	private ValueHolder<MailSecurityEnum> securityType;
	
	@Override
	public void loadProperty(Map<MailPropertyEnum, MailPropertyDto> store) {
		if (isSsl(store)) {
			securityType = new ValueHolder<MailSecurityEnum>(MailSecurityEnum.Ssl);
		} else if (isStarttls(store)) {
			securityType = new ValueHolder<MailSecurityEnum>(MailSecurityEnum.Starttls);
		} else {
			securityType = new ValueHolder<MailSecurityEnum>(MailSecurityEnum.None);
		}
	}
	
	private boolean isSsl(Map<MailPropertyEnum, MailPropertyDto> store) {
		MailPropertyDto dto = store.get(MailPropertyEnum.MailSmtpSslEnable);
		return dto != null && Boolean.TRUE.equals(Boolean.valueOf(dto.getCWert()));
	}

	
	private boolean isStarttls(Map<MailPropertyEnum, MailPropertyDto> store) {
		MailPropertyDto dto = store.get(MailPropertyEnum.MailSmtpStarttlsEnable);
		return dto != null && Boolean.TRUE.equals(Boolean.valueOf(dto.getCWert()));
	}

	@Override
	public void saveProperty(Map<MailPropertyEnum, String> store) {
		if (MailSecurityEnum.Ssl.equals(securityTypeHolder().get())) {
			setSslProperties(store);
		} else if (MailSecurityEnum.Starttls.equals(securityTypeHolder().get())) {
			setStarttlsProperties(store);
		} else {
			setNoneProperties(store);
		}
	}
	
	private void setNoneProperties(Map<MailPropertyEnum, String> store) {
		enableSmtpStarttls(store, false);
		enableSmtpSsl(store, false);
		enableSmtpSocketFactoryClass(store, false);
	}

	private void setStarttlsProperties(Map<MailPropertyEnum, String> store) {
		enableSmtpStarttls(store, true);
		
		enableSmtpSsl(store, false);
		enableSmtpSocketFactoryClass(store, false);
	}

	private void setSslProperties(Map<MailPropertyEnum, String> store) {
		enableSmtpSsl(store, true);
		enableSmtpSocketFactoryClass(store, true);

		enableSmtpStarttls(store, false);
	}
	
	private void enableSmtpSsl(Map<MailPropertyEnum, String> store, boolean enabled) {
		store.put(MailPropertyEnum.MailSmtpSslEnable, enabled ? "true" : null);
	}
	
	private void enableSmtpStarttls(Map<MailPropertyEnum, String> store, boolean enabled) {
		store.put(MailPropertyEnum.MailSmtpStarttlsEnable, enabled ? "true" : null);
	}
	
	private void enableSmtpSocketFactoryClass(Map<MailPropertyEnum, String> store, boolean enabled) {
		store.put(MailPropertyEnum.MailSmtpSocketFactoryClass, enabled ? "javax.net.ssl.SSLSocketFactory" : null);
	}

	@Override
	public boolean hasChanged() {
		return securityTypeHolder().hasChanged();
	}

	private ValueHolder<MailSecurityEnum> securityTypeHolder() {
		if (securityType == null) {
			securityType = new ValueHolder<MailSecurityEnum>(MailSecurityEnum.None);
		}
		return securityType;
	}

	@Override
	public void setDataUI(MailPropertiesUI data) {
		data.setSmtpSicherheitType(securityTypeHolder().get());
	}

	@Override
	public void getDataUI(MailPropertiesUI data) {
		securityTypeHolder().set(data.getSmtpSicherheitType());
	}

}
