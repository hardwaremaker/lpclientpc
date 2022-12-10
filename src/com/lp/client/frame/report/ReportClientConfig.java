package com.lp.client.frame.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReportClientConfig {

	private List<ReportClientConfigData> configData;
	
	public ReportClientConfig() {
	}

	public List<ReportClientConfigData> getConfigData() {
		if (configData == null) {
			configData = new ArrayList<ReportClientConfigData>();
		}
		return configData;
	}
	
	public void setConfigData(List<ReportClientConfigData> configData) {
		this.configData = configData;
	}
	
	public <T extends ReportClientConfigData> T findByClass(Class<T> clazz) {
		for (ReportClientConfigData element : getConfigData()) {
			if (clazz.isInstance(element)) {
				return (T) element;
			}
		}
		return null;
	}
	
	public <T extends ReportClientConfigData> void removeByClass(Class<T> clazz) {
		Iterator<ReportClientConfigData> iter = getConfigData().iterator();
		while (iter.hasNext()) {
			if (clazz.isInstance(iter.next())) {
				iter.remove();
			}
		}
	}
}
