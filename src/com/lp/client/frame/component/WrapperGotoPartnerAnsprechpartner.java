package com.lp.client.frame.component;

import com.lp.client.partner.InternalFramePartner;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.partner.TabbedPanePartner;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;

public class WrapperGotoPartnerAnsprechpartner extends WrapperGotoButton implements WrapperGotoConstants {
	private static final long serialVersionUID = -3701020018770039840L;

	public WrapperGotoPartnerAnsprechpartner() {
		super(GOTO_PARTNER_ANSPRECHPARTNER);
	}

	public WrapperGotoPartnerAnsprechpartner(String text) {
		super(text, GOTO_PARTNER_ANSPRECHPARTNER);
	}

	@Override
	protected void geheZuImpl(Integer where, Object okey) throws Throwable {
		if (where != GOTO_PARTNER_ANSPRECHPARTNER) {
			throw new IllegalArgumentException("Where: " + where) ;
		}


		if (!LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PARTNER)) {
			return;
		}

		InternalFramePartner ifPartner = (InternalFramePartner) LPMain
				.getInstance().getDesktop().holeModul(LocaleFac.BELEGART_PARTNER);
		ifPartner.geheZu(
				InternalFramePartner.IDX_PANE_PARTNER,
				TabbedPanePartner.IDX_PANE_ANSPRECHPARTNER,
				okey,
				getDetailKey(),
				PartnerFilterFactory.getInstance().createFKPartnerKey((Integer) okey));
	}
}
