package com.lp.service.findsteel.schema;

import java.io.Serializable;

public class PropertyPossibleValue implements Serializable {
	private static final long serialVersionUID = 3653208468080082546L;

	private String displayField;
	private String valueField;
	private String minAngle1;
	private String minAngle2;
	private String maxAngle1;
	private String maxAngle2;
		
	public String getDisplayField() {
		return displayField;
	}
	public void setDisplayField(String displayField) {
		this.displayField = displayField;
	}
	public String getValueField() {
		return valueField;
	}
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}
	public String getMinAngle1() {
		return minAngle1;
	}
	public void setMinAngle1(String minAngle1) {
		this.minAngle1 = minAngle1;
	}
	public String getMinAngle2() {
		return minAngle2;
	}
	public void setMinAngle2(String minAngle2) {
		this.minAngle2 = minAngle2;
	}
	public String getMaxAngle1() {
		return maxAngle1;
	}
	public void setMaxAngle1(String maxAngle1) {
		this.maxAngle1 = maxAngle1;
	}
	public String getMaxAngle2() {
		return maxAngle2;
	}
	public void setMaxAngle2(String maxAngle2) {
		this.maxAngle2 = maxAngle2;
	}	
}
