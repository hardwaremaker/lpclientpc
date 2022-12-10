package com.lp.client.system.automatik;

import javax.persistence.NoResultException;

import com.lp.client.frame.delegate.AutoFehlmengenDruckDelegate;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.AutoFehlmengendruckDto;

public class AutoJobFehlmengendruckCtrl implements IAutoJobPanelCtrl<AutoFehlmengendruckDto> {

	private AutoFehlmengendruckDto jobDetailsDto;
	
	public AutoJobFehlmengendruckCtrl() {
	}

	@Override
	public AutoFehlmengendruckDto getJobDetailsDto() throws Throwable {
		if (jobDetailsDto == null) {
			try {
				jobDetailsDto = delegateAutojob().autoFehlmengendruckFindByMandantCNr(
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

	private AutoFehlmengendruckDto createJobDetailsDto() throws Throwable {
		String mandant = LPMain.getTheClient().getMandant();
		jobDetailsDto = new AutoFehlmengendruckDto();
		jobDetailsDto.setMandantCNr(mandant);
		delegateAutojob().createAutoFehlmengendruck(jobDetailsDto);
		jobDetailsDto = delegateAutojob().autoFehlmengendruckFindByMandantCNr(mandant);
		
		return jobDetailsDto;
	}

	@Override
	public void save() throws Throwable {
		delegateAutojob().updateAutoFehlmengendruck(getJobDetailsDto());
	}

	private AutoFehlmengenDruckDelegate delegateAutojob() throws Throwable {
		return DelegateFactory.getInstance().getAutoFehlmengenDruckDelegate();
	}

}
