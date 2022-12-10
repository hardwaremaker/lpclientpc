package com.lp.client.system.automatik;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.system.service.AutomatikjobDto;
import com.lp.server.system.service.AutomatikjobtypeDto;

public class AutoJobHeadCtrl implements IAutoJobHeadPanelCtrl {

	private AutomatikjobDto automatikJobDto;
	private AutomatikjobtypeDto automatikJobTypeDto;
	
	public AutoJobHeadCtrl(AutomatikjobDto automatikJobDto, AutomatikjobtypeDto automatikJobTypeDto) {
		this.automatikJobDto = automatikJobDto;
		this.automatikJobTypeDto = automatikJobTypeDto;
	}

	@Override
	public AutomatikjobDto getJobDto() throws Throwable {
		return automatikJobDto;
	}

	@Override
	public AutomatikjobtypeDto getJobTypeDto() throws Throwable {
		return automatikJobTypeDto;
	}

	@Override
	public void save() throws Throwable {
		DelegateFactory.getInstance().getAutomatikDelegate().updateAutomatikjob(getJobDto());
	}

}
