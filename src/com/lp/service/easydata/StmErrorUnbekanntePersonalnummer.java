package com.lp.service.easydata;

import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.errors.StmUnbekanntePersonalnummerExc;

public class StmErrorUnbekanntePersonalnummer implements IEasydataErrorAction {

	private StmUnbekanntePersonalnummerExc exception;
	
	public StmErrorUnbekanntePersonalnummer(StmUnbekanntePersonalnummerExc exception) {
		this.exception = exception;
	}

	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr(
				"ls.import.easydata.lagerbewegung.error." + exception.getExcCode().getText(), 
				exception.getPersonalnummer());
	}
	
	@Override
	public WrapperGotoButton getGoto() {
		return null;
	}

}
