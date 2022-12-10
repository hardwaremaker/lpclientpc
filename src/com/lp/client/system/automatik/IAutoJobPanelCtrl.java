package com.lp.client.system.automatik;

public interface IAutoJobPanelCtrl<T> {

	public T getJobDetailsDto() throws Throwable;
	
	public void save() throws Throwable;
}
