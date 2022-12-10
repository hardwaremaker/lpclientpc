package com.lp.client.stueckliste;

import java.math.BigDecimal;
import java.util.EventObject;
import java.util.List;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.util.LPScriptEngine;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;

public class StuecklisteCreateScript extends LPScriptEngine {
	private ScriptStuecklisteServiceDto serviceDto ;
	private String scriptName ;
	
	public StuecklisteCreateScript(ScriptStuecklisteServiceDto dto, String scriptName) {
		this.serviceDto = dto ;
		this.scriptName = scriptName ;
	}
	
	public BigDecimal getValue() {
		put("@serviceDto", serviceDto) ;

		try {
			Object receiver = runEmbeddedLPScript() ;			
			List<StuecklistepositionDto> positionDtos  = 
					(List<StuecklistepositionDto>) getEngine().callMethod(receiver, "doAction", List.class) ;
			if(serviceDto.isStore()) {
				createOrUpdate();
			}
		} catch(Throwable t) {
			t.printStackTrace(); 
		}

		return null ;
	}

	private void createOrUpdate() throws ExceptionLP, Throwable {
		StuecklistepositionDto[] positionDtos = new StuecklistepositionDto[0];
		StuecklistearbeitsplanDto[] arbeitsplanDtos = new StuecklistearbeitsplanDto[0] ;
		
		MontageartDto[] montageartDtos = DelegateFactory.getInstance()
				.getStuecklisteDelegate().montageartFindByMandantCNr() ;
		
		if(serviceDto.getStuecklistepositionenOut() != null) {
			positionDtos = serviceDto.getStuecklistepositionenOut().toArray(new StuecklistepositionDto[0]) ;
			for (StuecklistepositionDto positionDto : positionDtos) {
				positionDto.setStuecklisteIId(serviceDto.getStuecklisteId());
				positionDto.setMontageartIId(montageartDtos[0].getIId());
			}
		}

		if(serviceDto.getStuecklistearbeitsplanOut() != null) {
			arbeitsplanDtos = serviceDto.getStuecklistearbeitsplanOut().toArray(new StuecklistearbeitsplanDto[0]) ;
			for (StuecklistearbeitsplanDto stuecklistearbeitsplanDto : arbeitsplanDtos) {
				stuecklistearbeitsplanDto.setStuecklisteIId(serviceDto.getStuecklisteId());
			}
		}
		
		DelegateFactory.getInstance().getStuecklisteDelegate()
			.createOrUpdatePositionsArbeitsplans(
				serviceDto.getStuecklisteId(), positionDtos, arbeitsplanDtos);		
	}
	
//	private ScriptStuecklisteServiceDto createServiceDto() throws Throwable {
//		ScriptStuecklisteServiceDto dto = new ScriptStuecklisteServiceDto() ;
//		dto.setInternalFrame(getInternalFrame());			
//		dto.setPanelBasisScript(new PanelStuecklistepositionenScript(getInternalFrame(), "panelbasis", null));
//		
//		StuecklistepositionDto[] positionDtos = DelegateFactory.getInstance().getStuecklisteDelegate()
//			.stuecklistepositionFindByStuecklisteIIdAllData(internalFrameStueckliste.getStuecklisteDto().getIId());
//		dto.setStuecklistepositionenIn(positionDtos);
//		
//		StuecklistearbeitsplanDto[] arbeitDtos = DelegateFactory.getInstance().getStuecklisteDelegate()
//			.stuecklistearbeitsplanFindByStuecklisteIId(internalFrameStueckliste.getStuecklisteDto().getIId()) ;
//		dto.setStuecklistearbeitsplanIn(arbeitDtos) ;
//		return dto ;
//	}
//		
	
	@Override
	protected String getModule() {
		return StuecklisteReportFac.REPORT_MODUL ;
	}
	
	@Override
	protected String getScriptName() {
		return scriptName ;
	}
	
	public void eventItemchanged(EventObject eI) throws Throwable {
		getEngine().callMethod(null, "eventItemChanged", eI) ;
	}
}
