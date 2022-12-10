package com.lp.client.system.automatik;

import javax.persistence.NoResultException;

import com.lp.client.frame.delegate.AutoMahnenDelegate;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.AutoMahnenDto;

public class AutoJobMahnenCtrl implements IAutoJobPanelCtrl<AutoMahnenDto> {

	private AutoMahnenDto jobDetailsDto;
	
	public AutoJobMahnenCtrl() {
	}

	@Override
	public AutoMahnenDto getJobDetailsDto() throws Throwable {
		if (jobDetailsDto == null) {
			try {
				jobDetailsDto = delegateAutojob().autoMahnenFindByMandantCNr(
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

	private AutoMahnenDto createJobDetailsDto() throws Throwable {
		String mandant = LPMain.getTheClient().getMandant();
		jobDetailsDto = new AutoMahnenDto();
		jobDetailsDto.setMandantCNr(mandant);
		delegateAutojob().createAutoMahnen(jobDetailsDto);
		jobDetailsDto = delegateAutojob().autoMahnenFindByMandantCNr(mandant);
		
		return jobDetailsDto;
	}

	@Override
	public void save() throws Throwable {
		delegateAutojob().updateAutoMahnen(getJobDetailsDto());
	}

	private AutoMahnenDelegate delegateAutojob() throws Throwable {
		return DelegateFactory.getInstance().getAutoMahnenDelegate();
	}

}
