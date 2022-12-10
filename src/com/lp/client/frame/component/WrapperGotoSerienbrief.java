package com.lp.client.frame.component;

import com.lp.client.partner.InternalFramePartner;
import com.lp.client.partner.TabbedPaneSerienbrief;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;

public class WrapperGotoSerienbrief extends WrapperGotoButton implements WrapperGotoConstants {
	private static final long serialVersionUID = -5179059783130001765L;
	
	public WrapperGotoSerienbrief() {
		super(GOTO_SERIENBRIEF);
	}
	
	public WrapperGotoSerienbrief(String text) {
		super(text, GOTO_SERIENBRIEF);
	}
	
	@Override
	protected void geheZuImpl(Integer where, Object okey) throws Throwable {
		if (where != GOTO_SERIENBRIEF) {
			throw new IllegalArgumentException("Where: " + where) ;
		}
		
		InternalFramePartner internalFramePartner = (InternalFramePartner) LPMain.getInstance()
				.getDesktop().holeModul(LocaleFac.BELEGART_PARTNER);
		internalFramePartner.geheZu(InternalFramePartner.IDX_PANE_SERIENBRIEF, 
				TabbedPaneSerienbrief.IDX_PANEL_SERIENBRIEF_KOPFDATEN_D2, 
				okey, null, null);
	}
}
