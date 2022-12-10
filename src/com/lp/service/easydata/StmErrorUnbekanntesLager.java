package com.lp.service.easydata;

import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.errors.StmUnbekanntesLagerExc;

public class StmErrorUnbekanntesLager implements IEasydataErrorAction {

	private StmUnbekanntesLagerExc exception;
	
	public StmErrorUnbekanntesLager(StmUnbekanntesLagerExc exception) {
		this.exception = exception;
	}

	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr(
				"ls.import.easydata.lagerbewegung.error." + exception.getExcCode().getText(), 
				String.valueOf(exception.getLager()));
	}

	public WrapperGotoButton getGoto() {
		return null;
	}
}
