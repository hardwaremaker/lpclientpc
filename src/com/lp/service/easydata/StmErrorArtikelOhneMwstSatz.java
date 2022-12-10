package com.lp.service.easydata;

import com.lp.client.frame.component.WrapperGotoArtikel;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.errors.StmArtikelMwstSatzExc;
import com.lp.server.system.service.LocaleFac;

public class StmErrorArtikelOhneMwstSatz implements IEasydataErrorAction {

	private StmArtikelMwstSatzExc exception;
	private WrapperGotoArtikel gotoButton;
	
	public StmErrorArtikelOhneMwstSatz(StmArtikelMwstSatzExc exception) {
		this.exception = exception;
		initGotoButton();
	}
	
	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr(
				"ls.import.easydata.lagerbewegung.error." + exception.getExcCode().getText(), 
				exception.getArtikelDto().getCNr());
	}

	private void initGotoButton() {
		if (!LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_PARTNER)) {
			gotoButton = null;
			return;
		}
		
		gotoButton = new WrapperGotoArtikel();
		gotoButton.setOKey(exception.getArtikelDto().getIId());
		gotoButton.setEnabled(false);
		gotoButton.getWrapperButton().setVisible(false);
	}

	@Override
	public WrapperGotoButton getGoto() {
		return gotoButton;
	}

}
