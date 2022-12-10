package com.lp.service.findchips;

import java.io.Serializable;

public class FindchipsDistributor implements Serializable {
	private static final long serialVersionUID = -2397753531597775819L;

	private String id ;
	private String name ;
	private boolean authorized ;
	private String logoUrl ;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isAuthorized() {
		return authorized;
	}
	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
}
