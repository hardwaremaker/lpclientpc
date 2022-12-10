package com.lp.client.frame.component;

import com.lp.client.partner.InternalFramePartner;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.partner.TabbedPaneBank;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;

public class WrapperGotoBankDetail extends WrapperGotoButton {
	private static final long serialVersionUID = -4655078068717917566L;
	
	private static final int GOTO_BANK_DETAIL = 5005;

	public WrapperGotoBankDetail() {
		super(GOTO_BANK_DETAIL);
	}

	public WrapperGotoBankDetail(String text) {
		super(text, GOTO_BANK_DETAIL);
	}

	@Override
	protected void geheZuImpl(Integer where, Object okey) throws Throwable {
		if(where != GOTO_BANK_DETAIL) {
			throw new IllegalArgumentException("Where: " + where) ;
		}

		if (!LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PARTNER)) {
			return;
		}
		
		InternalFramePartner ifPartner = (InternalFramePartner) LPMain
				.getInstance().getDesktop()
				.holeModul(LocaleFac.BELEGART_PARTNER);
		ifPartner.geheZu(
				InternalFramePartner.IDX_PANE_BANK,
				TabbedPaneBank.IDX_PANE_BANK_D2, okey,
				null, PartnerFilterFactory.getInstance()
						.createFKPartner((Integer) okey));
	}
}
