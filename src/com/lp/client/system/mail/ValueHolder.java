package com.lp.client.system.mail;

public class ValueHolder<T> {

	private T initValue;
	private T value;
	
	public ValueHolder(T initValue) {
		this.initValue = initValue;
		this.value = initValue;
	}
	
	public T get() {
		return value;
	}
	
	public void set(T newValue) {
		value = newValue;
	}
	
	public boolean hasChanged() {
		return initValue == null ? value != null : !initValue.equals(value);
	}
	
	public String asString() {
		return value != null ? value.toString() : null;
	}
}
