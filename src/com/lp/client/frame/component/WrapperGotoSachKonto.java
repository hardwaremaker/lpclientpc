package com.lp.client.frame.component;

import com.lp.client.finanz.InternalFrameFinanz;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;

public class WrapperGotoSachKonto extends WrapperGotoButton implements WrapperGotoConstants {
	private static final long serialVersionUID = -6603684874016621939L;
	
	public WrapperGotoSachKonto() {
		super(GOTO_KONTO) ;
	}
	
	public WrapperGotoSachKonto(String text) {
		super(text, GOTO_KONTO) ;
	}
	
	@Override
	protected void geheZuImpl(Integer where, Object okey) throws Throwable {
		if(where != GOTO_KONTO) {
			throw new IllegalArgumentException("Where: " + where) ;
		}
		
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
			InternalFrameFinanz ifF = (InternalFrameFinanz) LPMain
					.getInstance().getDesktop()
					.holeModul(LocaleFac.BELEGART_FINANZBUCHHALTUNG);
			ifF.geheZu(
					ifF.IDX_TABBED_PANE_SACHKONTEN,
					ifF.getTabbedPaneFinanzamt().IDX_KOPFDATEN,
					okey,
					null,
					null);
		}		
	}
}
