package com.lp.client.system.automatik;

import javax.swing.JPanel;

public interface IPanelAutoJobDetails {

	public Integer installComponents(JPanel jPanelWorkingOn, Integer iZeile) throws Throwable;

	void loadDetails() throws Throwable;

	void saveDetails() throws Throwable;
	
}
