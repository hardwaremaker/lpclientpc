package com.lp.service.easydata;

import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.errors.StmException;

public class StmErrorDefault implements IEasydataErrorAction {

	private StmException exception;
	
	public StmErrorDefault(StmException exception) {
		this.exception = exception;
	}

	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr(
				"ls.import.easydata.lagerbewegung.error.unbekannterfehler", 
				exception.getMessage());
	}

	@Override
	public WrapperGotoButton getGoto() {
		return null;
	}

}
