package com.lp.service.easydata;

import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.errors.StmTransformXmlExc;

public class StmErrorTransformXml implements IEasydataErrorAction {

	private StmTransformXmlExc exception;
	
	public StmErrorTransformXml(StmTransformXmlExc exception) {
		this.exception = exception;
	}

	@Override
	public String getMessage() {
		return LPMain.getTextRespectUISPr("ls.import.easydata.lagerbewegung.error." + exception.getExcCode().getText());
	}

	@Override
	public WrapperGotoButton getGoto() {
		return null;
	}

}
