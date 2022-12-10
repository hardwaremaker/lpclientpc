package com.lp.service.sepa;

import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.server.rechnung.service.RechnungDto;

public interface ISepaErrorReAction extends ISepaErrorAction {

	boolean hasRechnungDto();
	
	RechnungDto getRechnungDto();
	
	WrapperGotoButton getGotoButton();
}
