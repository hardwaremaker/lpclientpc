package com.lp.service.sepa;

import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperGotoKundeBankverbindung;
import com.lp.client.pc.LPMain;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.sepa.errors.ISepaReKundeException;
import com.lp.server.system.service.LocaleFac;

public class SepaErrorRechnungKunde implements ISepaErrorReAction {
	private ISepaReKundeException exception;
	private WrapperGotoButton gotoButton;
	
	public SepaErrorRechnungKunde(ISepaReKundeException exc) {
		setException(exc);
		initGotoButton();
	}

	public void setException(ISepaReKundeException exception) {
		this.exception = exception;
	}
	
	public ISepaReKundeException getException() {
		return exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr("rechnung.lastschriftvorschlag.sepaexport.error." 
				+ getException().getCode().getText(), 
				getException().getKundeDto().getPartnerDto().getCName1nachnamefirmazeile1());
	}

	@Override
	public boolean hasRechnungDto() {
		return getException().getRechnungDto() != null;
	}

	@Override
	public RechnungDto getRechnungDto() {
		return getException().getRechnungDto();
	}

	public void initGotoButton() {
		if (!LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_KUNDE)) {
			gotoButton = null;
			return;
		}

		gotoButton = new WrapperGotoKundeBankverbindung();
		gotoButton.setOKey(getException().getKundeDto().getIId());
		gotoButton.setEnabled(false);
		gotoButton.getWrapperButton().setVisible(false);
	}
	
	@Override
	public WrapperGotoButton getGotoButton() {
		return gotoButton;
	}
}
