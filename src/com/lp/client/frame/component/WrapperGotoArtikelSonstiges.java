package com.lp.client.frame.component;

import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;

public class WrapperGotoArtikelSonstiges extends WrapperGotoButton implements WrapperGotoConstants {
	private static final long serialVersionUID = 4177927897433451188L;

	public WrapperGotoArtikelSonstiges() {
		super(GOTO_ARTIKEL_SONSTIGES);
	}
	
	public WrapperGotoArtikelSonstiges(String text) { 
		super(text, GOTO_ARTIKEL_SONSTIGES);
	}
	
	@Override
	protected void geheZuImpl(Integer where, Object okey) throws Throwable {
		if(where != GOTO_ARTIKEL_SONSTIGES) {
			throw new IllegalArgumentException("Where: " + where) ;
		}
		
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ARTIKEL)) {

			InternalFrameArtikel ifArtikel = (InternalFrameArtikel) LPMain
					.getInstance().getDesktop()
					.holeModul(LocaleFac.BELEGART_ARTIKEL);
			ifArtikel.geheZu(InternalFrameArtikel.IDX_TABBED_PANE_ARTIKEL,
					ifArtikel.getTabbedPaneArtikel().IDX_PANEL_SONSTIGES,
					okey,
					getDetailKey(),
					null);
		}
	}
}
