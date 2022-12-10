package com.lp.client.system.automatik;

import com.lp.server.system.service.AutomatikjobDto;
import com.lp.server.system.service.AutomatikjobtypeDto;

public interface IAutoJobHeadPanelCtrl {

	public AutomatikjobDto getJobDto() throws Throwable;
	public AutomatikjobtypeDto getJobTypeDto() throws Throwable; 
	
	public void save() throws Throwable;
}
