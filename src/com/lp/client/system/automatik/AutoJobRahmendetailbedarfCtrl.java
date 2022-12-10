package com.lp.client.system.automatik;

import javax.persistence.NoResultException;

import com.lp.client.frame.delegate.AutoRahmendetailbedarfdruckDelegate;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.AutoRahmendetailbedarfdruckDto;

public class AutoJobRahmendetailbedarfCtrl implements IAutoJobPanelCtrl<AutoRahmendetailbedarfdruckDto> {

	private AutoRahmendetailbedarfdruckDto jobDetailsDto;
	
	public AutoJobRahmendetailbedarfCtrl() {
	}

	@Override
	public AutoRahmendetailbedarfdruckDto getJobDetailsDto() throws Throwable {
		if (jobDetailsDto == null) {
			try{
				jobDetailsDto = delegate().autoAutoRahmendetailbedarfdruckFindByMandantCNr(
						LPMain.getTheClient().getMandant());
			} catch (Exception e){
				if(e.getCause() instanceof NoResultException){
					return createJobDetailsDto();
				} else {
					throw e;
				}
			}
		}
		return jobDetailsDto;
	}

	private AutoRahmendetailbedarfdruckDto createJobDetailsDto() throws Throwable {
		String mandant = LPMain.getTheClient().getMandant();
		jobDetailsDto = new AutoRahmendetailbedarfdruckDto();
		jobDetailsDto.setMandantCNr(mandant);
		delegate().createAutoRahmendetailbedarfdruck(jobDetailsDto);
		jobDetailsDto = delegate().autoAutoRahmendetailbedarfdruckFindByMandantCNr(mandant);
		
		return jobDetailsDto;
	}

	@Override
	public void save() throws Throwable {
		delegate().updateAutoRahmendetailbedarfdruck(getJobDetailsDto());
	}

	private AutoRahmendetailbedarfdruckDelegate delegate() throws Throwable {
		return DelegateFactory.getInstance().getautoRahmendetailbedarfdruckDelegate();
	}
}
