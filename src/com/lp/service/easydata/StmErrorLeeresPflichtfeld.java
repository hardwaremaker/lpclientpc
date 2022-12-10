package com.lp.service.easydata;

import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.errors.StmPflichtfeldLeerExc;

public class StmErrorLeeresPflichtfeld implements IEasydataErrorAction {
	
	private StmPflichtfeldLeerExc exception;
	
	public StmErrorLeeresPflichtfeld(StmPflichtfeldLeerExc exception) {
		this.exception = exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr(
				"ls.import.easydata.lagerbewegung.error." + exception.getExcCode().getText(), 
				exception.getXmlField(), 
				String.valueOf(exception.getSerialnumber()), 
				String.valueOf(exception.getAutoindex()));
	}

	@Override
	public WrapperGotoButton getGoto() {
		return null;
	}

}
