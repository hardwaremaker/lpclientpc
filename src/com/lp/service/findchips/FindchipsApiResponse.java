package com.lp.service.findchips;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

 
public class FindchipsApiResponse implements Serializable {
	private static final long serialVersionUID = -7472802822060339415L;

	private FindchipsMetadata metadata ;
//	private FindchipsResponseList response ;
	private List<FindchipsResponse> response ;
	
	@JsonIgnore
	private boolean successfull ;
	
	public FindchipsApiResponse() {
//		setResponse(new FindchipsResponseList());
		setResponse(new ArrayList<FindchipsResponse>()) ;
	}

	public FindchipsMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(FindchipsMetadata metadata) {
		this.metadata = metadata;
	}

//	public FindchipsResponseList getResponse() {
	public List<FindchipsResponse> getResponse() {
		return response;
	}

//	public void setResponse(FindchipsResponseList response) {
	public void setResponse(List<FindchipsResponse> response) {
		this.response = response;
	}	

	public boolean isSuccessfull() {
		return successfull;
	}

	public void setSuccessfull(boolean successfull) {
		this.successfull = successfull;
	}
}
