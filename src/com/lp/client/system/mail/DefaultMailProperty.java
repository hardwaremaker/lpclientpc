package com.lp.client.system.mail;

import java.util.Map;

import com.lp.server.system.mail.service.MailPropertyEnum;

public abstract class DefaultMailProperty<T> implements IMailPropertyCtrl {

	private ValueHolder<T> valueHolder;
	private MailPropertyEnum propertyEnum;
	
	public DefaultMailProperty(MailPropertyEnum propertyEnum) {
		this.propertyEnum = propertyEnum;
	}

	protected ValueHolder<T> valueHolder() {
		if (valueHolder == null) {
			valueHolder = new ValueHolder<T>(null);
		}
		return valueHolder;
	}
	
	protected void initValueHolder(T value) {
		valueHolder = new ValueHolder<T>(value);
	}
	protected MailPropertyEnum property() {
		return propertyEnum;
	}
	
	public void saveProperty(Map<MailPropertyEnum, String> store) {
		store.put(property(), valueHolder().asString());
	}
	
	public boolean hasChanged() {
		return valueHolder().hasChanged();
	}
}
