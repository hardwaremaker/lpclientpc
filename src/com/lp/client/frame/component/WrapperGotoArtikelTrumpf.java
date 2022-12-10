package com.lp.client.frame.component;

import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;

public class WrapperGotoArtikelTrumpf extends WrapperGotoButton implements WrapperGotoConstants {
	private static final long serialVersionUID = -5607237051665535321L;

	public WrapperGotoArtikelTrumpf() {
		super(GOTO_ARTIKEL_TRUMPF);
	}

	public WrapperGotoArtikelTrumpf(String sText) {
		super(sText, GOTO_ARTIKEL_TRUMPF);
	}

	@Override
	protected void geheZuImpl(Integer where, Object okey) throws Throwable {
		if(where != GOTO_ARTIKEL_TRUMPF) {
			throw new IllegalArgumentException("Where: " + where) ;
		}
		
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ARTIKEL)
			&& LPMain.getInstance().getDesktop()
			.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_TRUTOPS_BOOST)) {

			InternalFrameArtikel ifArtikel = (InternalFrameArtikel) LPMain
					.getInstance().getDesktop()
					.holeModul(LocaleFac.BELEGART_ARTIKEL);
			ifArtikel.geheZu(InternalFrameArtikel.IDX_TABBED_PANE_ARTIKEL,
					ifArtikel.getTabbedPaneArtikel().IDX_PANEL_TRUMPF,
					okey,
					getDetailKey(),
					null);
		}
	}
}
