package com.lp.service.easydata;

import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.errors.StmMandantKeinKundeExc;

public class StmErrorMandantKeinKunde implements IEasydataErrorAction {

	private StmMandantKeinKundeExc exception;
	
	public StmErrorMandantKeinKunde(StmMandantKeinKundeExc exception) {
		this.exception = exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr(
				"ls.import.easydata.lagerbewegung.error." + exception.getExcCode().getText(), 
				exception.getMandantDto().getPartnerDto().formatFixName1Name2());
	}

	@Override
	public WrapperGotoButton getGoto() {
		return null;
	}

}
