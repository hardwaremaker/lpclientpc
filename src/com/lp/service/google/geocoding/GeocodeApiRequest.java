package com.lp.service.google.geocoding;

public class GeocodeApiRequest {
	
	private Integer id;
	private String address;

	public GeocodeApiRequest(Integer id, String address) {
		this(address);
		setId(id);
	}
	
	public GeocodeApiRequest(String address) {
		setAddress(address);
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String asString() {
		return "GeocodeApiRequest [id = " + id + ", address = '" + address + "']";
	}
}
