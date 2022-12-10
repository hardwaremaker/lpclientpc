package com.lp.client.system.automatik;

import javax.persistence.NoResultException;

import com.lp.client.frame.delegate.AutoBestellvorschlagDelegate;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.AutoBestellvorschlagDto;

public class AutoJobBestellvorschlagCtrl implements IAutoJobPanelCtrl<AutoBestellvorschlagDto> {

	private AutoBestellvorschlagDto jobDetailsDto;
	
	public AutoJobBestellvorschlagCtrl() {
	}

	@Override
	public AutoBestellvorschlagDto getJobDetailsDto() throws Throwable {
		if (jobDetailsDto == null) {
			try {
				jobDetailsDto = delegateAutojob().autoBestellvorschlagFindByMandantCNr(
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

	private AutoBestellvorschlagDto createJobDetailsDto() throws Throwable {
		String mandant = LPMain.getTheClient().getMandant();
		jobDetailsDto = new AutoBestellvorschlagDto();
		jobDetailsDto.setMandantCNr(mandant);
		delegateAutojob().createAutoBestellvorschlag(jobDetailsDto);
		jobDetailsDto = delegateAutojob().autoBestellvorschlagFindByMandantCNr(mandant);
		
		return jobDetailsDto;
	}

	@Override
	public void save() throws Throwable {
		delegateAutojob().updateAutoBestellvorschlag(getJobDetailsDto());
	}

	private AutoBestellvorschlagDelegate delegateAutojob() throws Throwable {
		return DelegateFactory.getInstance()
				.getautoBestellvorschlagDelegate();
	}
}
