package com.lp.service.findchips;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FindchipsPartRohs implements Serializable {
	private static final long serialVersionUID = -1583185462117165962L;
//	private String DEFAULT ;
	@JsonProperty("DEFAULT")
	public String rohsDefault ;
	
//	public String getDefault() {
//		return rohsDefault ;
//	}
//
//	public void setDefault(String value) {
//		rohsDefault = value;
//	}
	
	
	@JsonIgnore
	public boolean isCompliant() {
		return "Compliant".equals(rohsDefault);
	}
}
