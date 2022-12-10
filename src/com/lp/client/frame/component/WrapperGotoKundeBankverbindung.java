package com.lp.client.frame.component;

import com.lp.client.partner.InternalFrameKunde;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.partner.TabbedPaneKunde;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;

public class WrapperGotoKundeBankverbindung extends WrapperGotoButton implements WrapperGotoConstants {
	private static final long serialVersionUID = -3350417524736693824L;

	public WrapperGotoKundeBankverbindung() {
		super(GOTO_KUNDE_BANKVERBINDUNG);
	}

	public WrapperGotoKundeBankverbindung(String text) {
		super(text, GOTO_KUNDE_BANKVERBINDUNG);
	}

	@Override
	protected void geheZuImpl(Integer where, Object okey) throws Throwable {
		if(where != GOTO_KUNDE_BANKVERBINDUNG) {
			throw new IllegalArgumentException("Where: " + where) ;
		}
		
		if (!LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_KUNDE)) {
			return;
		}
		
		InternalFrameKunde ifKunde = (InternalFrameKunde) LPMain
				.getInstance().getDesktop()
				.holeModul(LocaleFac.BELEGART_KUNDE);
		ifKunde.getTpKunde().getOnlyPanelKundeQP1()
				.clearDefaultFilters();
		ifKunde.geheZu(InternalFrameKunde.IDX_PANE_KUNDE,
				TabbedPaneKunde.IDX_PANE_BANKVERBINDUNG, okey,
				null, PartnerFilterFactory.getInstance()
						.createFKPartnerKey((Integer) okey));
		ifKunde.getTpKunde().getOnlyPanelKundeQP1()
				.restoreDefaultFilters();
	}
}
