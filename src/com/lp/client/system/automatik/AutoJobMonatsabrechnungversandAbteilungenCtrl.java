

package com.lp.client.system.automatik;

import javax.persistence.NoResultException;

import com.lp.client.frame.delegate.AutoMonatsabrechnungversandDelegate;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.AutoMonatsabrechnungversandDto;

public class AutoJobMonatsabrechnungversandAbteilungenCtrl implements IAutoJobPanelCtrl<AutoMonatsabrechnungversandDto> {

	private AutoMonatsabrechnungversandDto jobDetailsDto;
	
	public AutoJobMonatsabrechnungversandAbteilungenCtrl() {
	}

	@Override
	public AutoMonatsabrechnungversandDto getJobDetailsDto() throws Throwable {
		if (jobDetailsDto == null) {
			try {
				jobDetailsDto = delegateAutojob()
						.autoMonatsabrechnungversandAbteilungenfindByMandantCNr(LPMain.getTheClient().getMandant());
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

	private AutoMonatsabrechnungversandDto createJobDetailsDto() throws Throwable {
		String mandant = LPMain.getTheClient().getMandant();
		jobDetailsDto = new AutoMonatsabrechnungversandDto();
		jobDetailsDto.setMandantCNr(mandant);
		delegateAutojob().createAutoMonatsabrechnungversandAbteilungen(jobDetailsDto);
		jobDetailsDto = delegateAutojob().autoMonatsabrechnungversandAbteilungenfindByMandantCNr(mandant);
		
		return jobDetailsDto;
	}
	
	@Override
	public void save() throws Throwable {
		delegateAutojob().updateAutoMonatsabrechnungversandAbteilungen(getJobDetailsDto());
	}

	private AutoMonatsabrechnungversandDelegate delegateAutojob() throws Throwable {
		return DelegateFactory.getInstance()
				.getAutoMonatsabrechnungversandDelegate();
	}
}
