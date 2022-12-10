package com.lp.service.findchips;

public class FindchipsApiRequest {
	private Integer id ;
	private String partnr ;
	
	protected FindchipsApiRequest() {
	}
	
	public FindchipsApiRequest(String partnr) {
		this.partnr = partnr ;
	}

	public FindchipsApiRequest(Integer id, String partnr) {
		this.id = id ;
		this.partnr = partnr ;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPartnr() {
		return partnr;
	}
	public void setPartnr(String partnr) {
		this.partnr = partnr;
	}	
}
