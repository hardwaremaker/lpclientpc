package com.lp.service.findsteel.schema;

import java.io.Serializable;

public class FilterEntry implements Serializable {
	private static final long serialVersionUID = -7336787931252558142L;

	private String name;
	private String align;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
}
