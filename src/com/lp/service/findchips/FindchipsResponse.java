package com.lp.service.findchips;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lp.client.angebotstkl.webabfrage.INormalizationResponse;

public class FindchipsResponse implements Serializable, INormalizationResponse {
	private static final long serialVersionUID = -7796945054813982547L;
	private FindchipsDistributor distributor ;
//	private FindchipsPartlist parts ;
	private List<FindchipsPart> parts ;
	
	public FindchipsResponse() {
		setDistributor(new FindchipsDistributor());
//		setParts(new FindchipsPartlist());
		setParts(new ArrayList<FindchipsPart>()) ;
	}
	
	public FindchipsDistributor getDistributor() {
		return distributor;
	}
	public void setDistributor(FindchipsDistributor distributor) {
		this.distributor = distributor;
	}

//	public FindchipsPartlist getParts() {
	public List<FindchipsPart> getParts() {
		return parts;
	}

//	public void setParts(FindchipsPartlist parts) {
	public void setParts(List<FindchipsPart> parts) {
		this.parts = parts;
	}
}
