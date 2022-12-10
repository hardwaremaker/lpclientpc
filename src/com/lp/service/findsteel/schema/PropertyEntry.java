package com.lp.service.findsteel.schema;

import java.io.Serializable;

public class PropertyEntry implements Serializable {
	private static final long serialVersionUID = -1842951208234729217L;

	private String key;
	private String value;
	private PropertyMetaEntry meta;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public PropertyMetaEntry getMeta() {
		return meta;
	}
	public void setMeta(PropertyMetaEntry meta) {
		this.meta = meta;
	}
	
}
