package com.lp.client.angebotstkl.webabfrage;

public class WebabfrageError {

	private Integer id;
	private String search;
	private Integer abfrageType;
	
	public WebabfrageError(Integer id, String search, Integer abfrageType) {
		super();
		this.id = id;
		this.search = search;
		this.abfrageType = abfrageType;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public Integer getAbfrageType() {
		return abfrageType;
	}
	public void setAbfrageType(Integer abfrageType) {
		this.abfrageType = abfrageType;
	}

	@Override
	public String toString() {
		return "WebabfrageError: " + errorToString();
	}
	
	protected String errorToString() {
		StringBuilder builder = new StringBuilder();
		builder.append("id=");
		builder.append(id);
		builder.append(", search=");
		builder.append(search);
		builder.append(", abfrageType=");
		builder.append(abfrageType);
		
		return builder.toString();
	}
}
