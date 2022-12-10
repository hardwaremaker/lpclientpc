package com.lp.client.frame.component;

import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.InternalFrameStueckliste;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.client.stueckliste.TabbedPaneStueckliste;
import com.lp.server.system.service.LocaleFac;

public class WrapperGotoStueckliste extends WrapperGotoButton implements WrapperGotoConstants {
	private static final long serialVersionUID = -5725639955249216517L;
	
	public WrapperGotoStueckliste() {
		super(GOTO_STUECKLISTE) ;
	}
	
	public WrapperGotoStueckliste(String text) {
		super(text, GOTO_STUECKLISTE) ;
	}
	
	@Override
	protected void geheZuImpl(Integer where, Object oKey) throws Throwable {
		if(where != GOTO_STUECKLISTE) {
			throw new IllegalArgumentException("Where: " + where) ;
		}
		
		
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_STUECKLISTE)) {
			InternalFrameStueckliste ifStueckliste = (InternalFrameStueckliste) LPMain
					.getInstance().getDesktop()
					.holeModul(LocaleFac.BELEGART_STUECKLISTE);
			ifStueckliste
					.geheZu(InternalFrameStueckliste.IDX_TABBED_PANE_STUECKLISTE,
							TabbedPaneStueckliste.IDX_PANEL_AUSWAHL,
							oKey,
							null,
							StuecklisteFilterFactory
									.getInstance()
									.createFKStuecklisteKey(
											(Integer) oKey));
		}
	}
}
