package com.lp.service.sepa;

import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.LPMain;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.sepa.errors.ISepaReException;

public class SepaErrorRechnung implements ISepaErrorReAction {

	public ISepaReException exception;
	
	public SepaErrorRechnung(ISepaReException exception) {
		setException(exception);
	}

	public void setException(ISepaReException exception) {
		this.exception = exception;
	}
	
	public ISepaReException getException() {
		return exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr("rechnung.lastschriftvorschlag.sepaexport.error." 
				+ getException().getCode().getText(), getException().getRechnungDto().getCNr());
	}

	@Override
	public boolean hasRechnungDto() {
		return getException().getRechnungDto() != null;
	}

	@Override
	public RechnungDto getRechnungDto() {
		return getException().getRechnungDto();
	}

	@Override
	public WrapperGotoButton getGotoButton() {
		return null;
	}

}
