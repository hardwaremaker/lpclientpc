package com.lp.service.findchips;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FindchipsResponseList implements Serializable {
	private static final long serialVersionUID = 6074163163980779232L;
	private List<FindchipsResponse> responses ;
	
	public FindchipsResponseList() {
		setResponses(new ArrayList<FindchipsResponse>()) ;
	}

	public List<FindchipsResponse> getResponses() {
		return responses;
	}

	public void setResponses(List<FindchipsResponse> responses) {
		this.responses = responses;
	}
}
