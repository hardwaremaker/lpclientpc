package com.lp.client.frame.component;

import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;

public class WrapperGotoArtikel extends WrapperGotoButton implements WrapperGotoConstants {
	private static final long serialVersionUID = 1907823648318565245L;

	public WrapperGotoArtikel() {
		super(GOTO_ARTIKEL_DETAIL);
	}

	public WrapperGotoArtikel(String text) {
		super(text, GOTO_ARTIKEL_DETAIL);
	}

	@Override
	protected void geheZuImpl(Integer where, Object okey) throws Throwable {
		if(where != GOTO_ARTIKEL_DETAIL) {
			throw new IllegalArgumentException("Where: " + where) ;
		}
		
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ARTIKEL)) {

			InternalFrameArtikel ifArtikel = (InternalFrameArtikel) LPMain
					.getInstance().getDesktop()
					.holeModul(LocaleFac.BELEGART_ARTIKEL);
			ifArtikel.geheZu(InternalFrameArtikel.IDX_TABBED_PANE_ARTIKEL,
					ifArtikel.getTabbedPaneArtikel().IDX_PANEL_DETAIL,
					okey,
					getDetailKey(),
					null);
		}
	}
}
