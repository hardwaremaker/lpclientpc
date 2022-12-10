package com.lp.client.system.automatik;


import javax.persistence.NoResultException;

import com.lp.client.frame.delegate.AutoLoseerledigenDelegate;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.AutoLoseerledigenDto;

public class AutoJobLoseerledigenCtrl implements IAutoJobPanelCtrl<AutoLoseerledigenDto> {

	private AutoLoseerledigenDto jobDetailsDto;
	
	public AutoJobLoseerledigenCtrl() {
	}

	@Override
	public AutoLoseerledigenDto getJobDetailsDto() throws Throwable {
		if (jobDetailsDto == null) {
			try {
				jobDetailsDto = delegateAutojob().autoLoseerledigenFindByMandantCNr(
						LPMain.getTheClient().getMandant());
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

	private AutoLoseerledigenDto createJobDetailsDto() throws Throwable {
		String mandant = LPMain.getTheClient().getMandant();
		jobDetailsDto = new AutoLoseerledigenDto();
		jobDetailsDto.setMandantCNr(mandant);
		delegateAutojob().createAutoLoseerledigen(jobDetailsDto);
		jobDetailsDto = delegateAutojob().autoLoseerledigenFindByMandantCNr(mandant);
		
		return jobDetailsDto;
	}

	@Override
	public void save() throws Throwable {
		delegateAutojob().updateAutoLoseerledigen(getJobDetailsDto());
	}

	private AutoLoseerledigenDelegate delegateAutojob() throws Throwable {
		return DelegateFactory.getInstance()
				.getAutoLoseerledigenDelegate();
	}
}
