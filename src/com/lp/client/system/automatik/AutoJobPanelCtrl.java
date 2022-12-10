package com.lp.client.system.automatik;

import com.lp.client.frame.delegate.AutoJobDelegate;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.JobDetailsDto;
import com.lp.server.system.service.JobDetailsFac;

public class AutoJobPanelCtrl<T extends JobDetailsDto, I extends JobDetailsFac<T>> implements IAutoJobDetailsPanelCtrl<T> {

	private AutoJobDelegate<T, I> delegate;
	private T jobDetailsDto;
	private Class<T> jobDetailsClass;
	
	public AutoJobPanelCtrl(AutoJobDelegate<T, I> delegate, Class<T> jobDetailsClass) {
		this.delegate = delegate;
		this.jobDetailsClass = jobDetailsClass;
	}

	public T getJobDetailsDto() throws Throwable {
		if (jobDetailsDto == null) {
			jobDetailsDto = delegateAutojob().findByMandantCNrNoEx(LPMain.getTheClient().getMandant());
			if (jobDetailsDto != null) {
				return jobDetailsDto;
			}
			jobDetailsDto = createJobDetailsDto();
		}
		return jobDetailsDto;
	}
	
	private T createJobDetailsDto() throws Throwable {
		T dto = jobDetailsClass.newInstance();
		dto.setMandantCNr(LPMain.getTheClient().getMandant());
		Integer iId = delegateAutojob().create(dto);
		dto.setiId(iId);
		return dto;
	}
	
	protected AutoJobDelegate<T, I> delegateAutojob() {
		return delegate;
	}

	public void save() throws Throwable {
		delegateAutojob().update(getJobDetailsDto());
	}
}
