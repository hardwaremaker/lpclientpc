package com.lp.service.sepa;

import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.LPMain;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.sepa.errors.ISepaException;

public class SepaErrorUnbekannt implements ISepaErrorReAction {

	private ISepaException exception;
	
	public SepaErrorUnbekannt(ISepaException exception) {
		setException(exception);
	}

	public void setException(ISepaException exception) {
		this.exception = exception;
	}
	
	public ISepaException getException() {
		return exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr("rechnung.lastschriftvorschlag.sepaexport.error.unbekannt", 
				getException().getMessage());
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
	public WrapperGotoButton getGotoButton() {
		return null;
	}

}
