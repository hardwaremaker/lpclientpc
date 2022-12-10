package com.lp.client.frame.filechooser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FileChooserConfigList implements Serializable {
	private static final long serialVersionUID = -2068439567474429603L;

	private List<FileChooserConfig> configs;
	
	public FileChooserConfigList() {
	}
	
	public void setConfigs(List<FileChooserConfig> configs) {
		this.configs = configs;
	}
	
	public List<FileChooserConfig> getConfigs() {
		if (configs == null) {
			configs = new ArrayList<FileChooserConfig>();
		}
		return configs;
	}
	
	@JsonIgnore
	public FileChooserConfig findByToken(String token) {
		if (token == null) return null;
		
		for (FileChooserConfig config : getConfigs()) {
			if (token.equals(config.getToken()))
				return config;
		}
		return null;
	}
}
