package com.lp.service.findchips;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FindchipsPartlist implements Serializable {
	private static final long serialVersionUID = -982406927079407747L;
	private List<FindchipsPart> parts ;
	
	public FindchipsPartlist() {
		setParts(new ArrayList<FindchipsPart>()) ;
	}

	public List<FindchipsPart> getParts() {
		return parts;
	}

	public void setParts(List<FindchipsPart> parts) {
		this.parts = parts;
	}
	
}
