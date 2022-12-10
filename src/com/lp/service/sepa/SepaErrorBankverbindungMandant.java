package com.lp.service.sepa;

import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperGotoFibuBankverbindung;
import com.lp.client.pc.LPMain;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.sepa.errors.ISepaBankverbindungException;
import com.lp.server.system.service.LocaleFac;

public class SepaErrorBankverbindungMandant implements ISepaErrorReAction {

	private ISepaBankverbindungException exception;
	private WrapperGotoFibuBankverbindung gotoButton;
	
	public SepaErrorBankverbindungMandant(ISepaBankverbindungException exception) {
		setException(exception);
		initGotoButton();
	}

	public void setException(ISepaBankverbindungException exception) {
		this.exception = exception;
	}
	
	public ISepaBankverbindungException getException() {
		return exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr("rechnung.lastschriftvorschlag.sepaexport.error." 
				+ getException().getCode().getText(), getException().getKontoDto().getCNr());
	}

	@Override
	public boolean hasRechnungDto() {
		return false;
	}

	@Override
	public RechnungDto getRechnungDto() {
		return null;
	}

	private void initGotoButton() {
		if (!LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
			gotoButton = null;
			return;
		}
		gotoButton = new WrapperGotoFibuBankverbindung();
		gotoButton.setOKey(getException().getBankverbindungDto().getIId());
		gotoButton.setEnabled(false);
		gotoButton.getWrapperButton().setVisible(false);
	}

	@Override
	public WrapperGotoButton getGotoButton() {
		return gotoButton;
	}

}
