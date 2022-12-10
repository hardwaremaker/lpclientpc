package com.lp.service.easydata;

import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperGotoPartnerAnsprechpartner;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.errors.StmAnsprechpartnerNichtEingetragenExc;
import com.lp.server.system.service.LocaleFac;

public class StmErrorAnsprechpartnerNichtEingetragen implements IEasydataErrorAction {

	private StmAnsprechpartnerNichtEingetragenExc exception;
	private WrapperGotoButton gotoButton;
	
	public StmErrorAnsprechpartnerNichtEingetragen(StmAnsprechpartnerNichtEingetragenExc exception) {
		this.exception = exception;
		initGotoButton();
	}

	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr(
				"ls.import.easydata.lagerbewegung.error." + exception.getExcCode().getText(), 
				exception.getPersonalDto().getPartnerDto().formatFixName1Name2(),
				exception.getMandantDto().getPartnerDto().formatFixName1Name2());
	}

	public void initGotoButton() {
		if (!LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_PARTNER)) {
			gotoButton = null;
			return;
		}
		
		gotoButton = new WrapperGotoPartnerAnsprechpartner();
		gotoButton.setOKey(exception.getMandantDto().getPartnerIId());
		gotoButton.setEnabled(false);
		gotoButton.getWrapperButton().setVisible(false);
	}

	@Override
	public WrapperGotoButton getGoto() {
		return gotoButton;
	}

}
