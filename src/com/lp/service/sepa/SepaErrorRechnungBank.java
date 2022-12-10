package com.lp.service.sepa;

import com.lp.client.frame.component.WrapperGotoBankDetail;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.LPMain;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.sepa.errors.ISepaReBankException;
import com.lp.server.system.service.LocaleFac;

public class SepaErrorRechnungBank implements ISepaErrorReAction {

	private ISepaReBankException exception;
	private WrapperGotoButton gotoButton;
	
	public SepaErrorRechnungBank(ISepaReBankException exception) {
		setException(exception);
		initGotoButton();
	}

	public void setException(ISepaReBankException exception) {
		this.exception = exception;
	}
	
	public ISepaReBankException getException() {
		return exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr("rechnung.lastschriftvorschlag.sepaexport.error." 
				+ getException().getCode().getText(), 
				getException().getBankDto().getPartnerDto().getCName1nachnamefirmazeile1());
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
				LocaleFac.BELEGART_PARTNER)) {
			gotoButton = null;
			return;
		}
		
		gotoButton = new WrapperGotoBankDetail();
		gotoButton.setOKey(getException().getBankDto().getPartnerIId());
		gotoButton.setEnabled(false);
		gotoButton.getWrapperButton().setVisible(false);
	}
	
	@Override
	public WrapperGotoButton getGotoButton() {
		return gotoButton;
	}

}
