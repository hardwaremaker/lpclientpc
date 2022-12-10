package com.lp.service.findsteel.schema;

import java.io.Serializable;

public class DocumentEntry implements Serializable {
	private static final long serialVersionUID = 7940669603830183728L;

	private String name;
	private String size;
	private String path;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
