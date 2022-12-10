package com.lp.service.sepa;

import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.LPMain;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.sepa.errors.ISepaMandantException;

public class SepaErrorMandant implements ISepaErrorReAction {

	private ISepaMandantException exception;
	
	public SepaErrorMandant(ISepaMandantException exception) {
		setException(exception);
	}

	public void setException(ISepaMandantException exception) {
		this.exception = exception;
	}
	
	public ISepaMandantException getException() {
		return exception;
	}
	
	@Override
	public boolean hasRechnungDto() {
		return false;
	}

	@Override
	public RechnungDto getRechnungDto() {
		return null;
	}

	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr("rechnung.lastschriftvorschlag.sepaexport.error." 
				+ getException().getCode().getText(), getException().getMandantDto().getCNr());
	}

	@Override
	public WrapperGotoButton getGotoButton() {
		return null;
	}

}
