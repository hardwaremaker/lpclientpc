package com.lp.service.findsteel.schema;

import java.io.Serializable;

public class PropertyMetaEntry implements Serializable {
	private static final long serialVersionUID = -3796197583182048663L;

	private String defaultValue;
	private PropertyPossibleValue[] possibleValues;
	private Integer maxLength;
	
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public PropertyPossibleValue[] getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(PropertyPossibleValue[] possibleValues) {
		this.possibleValues = possibleValues;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
}
