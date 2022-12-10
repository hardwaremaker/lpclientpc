package com.lp.client.system.automatik;

import javax.persistence.NoResultException;

import com.lp.client.frame.delegate.AutoMahnungsversandDelegate;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.AutoMahnungsversandDto;

public class AutoJobMahnungsversandCtrl implements IAutoJobPanelCtrl<AutoMahnungsversandDto> {

	private AutoMahnungsversandDto jobDetailsDto;
	
	public AutoJobMahnungsversandCtrl() {
	}

	@Override
	public AutoMahnungsversandDto getJobDetailsDto() throws Throwable {
		if (jobDetailsDto == null) {
			try {
				jobDetailsDto = delegateAutojob()
						.autoMahnungsversandFindByMandantCNr(LPMain.getTheClient().getMandant());
			} catch (Exception ex) {
				if(ex.getCause() instanceof NoResultException){
					return createJobDetailsDto();
				} else {
					throw ex;
				}
			}
		}
		return jobDetailsDto;
	}

	private AutoMahnungsversandDto createJobDetailsDto() throws Throwable {
		String mandant = LPMain.getTheClient().getMandant();
		jobDetailsDto = new AutoMahnungsversandDto();
		jobDetailsDto.setMandantCNr(mandant);
		delegateAutojob().createAutoMahnungsversand(jobDetailsDto);
		jobDetailsDto = delegateAutojob().autoMahnungsversandFindByMandantCNr(mandant);
		
		return jobDetailsDto;
	}
	
	@Override
	public void save() throws Throwable {
		delegateAutojob().updateAutoMahnungsversand(getJobDetailsDto());
	}

	private AutoMahnungsversandDelegate delegateAutojob() throws Throwable {
		return DelegateFactory.getInstance()
				.getAutoMahnungsversandDelegate();
	}
}
