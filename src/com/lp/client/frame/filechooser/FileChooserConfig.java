package com.lp.client.frame.filechooser;

import java.io.Serializable;

public class FileChooserConfig implements Serializable {
	private static final long serialVersionUID = 2865477717527067010L;

	private String token;
	private String path;
	private String fileFilter;
	
	public FileChooserConfig() {
	}
	
	public FileChooserConfig(String token) {
		setToken(token);
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getFileFilter() {
		return fileFilter;
	}
	
	public void setFileFilter(String fileFilter) {
		this.fileFilter = fileFilter;
	}
}
