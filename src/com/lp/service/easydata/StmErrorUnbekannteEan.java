package com.lp.service.easydata;

import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.errors.StmUnbekannteEanExc;

public class StmErrorUnbekannteEan implements IEasydataErrorAction {

	private StmUnbekannteEanExc exception;
	
	public StmErrorUnbekannteEan(StmUnbekannteEanExc exception) {
		this.exception = exception;
	}

	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr(
				"ls.import.easydata.lagerbewegung.error." + exception.getExcCode().getText(), 
				String.valueOf(exception.getEan()));
	}

	@Override
	public WrapperGotoButton getGoto() {
		return null;
	}

}
