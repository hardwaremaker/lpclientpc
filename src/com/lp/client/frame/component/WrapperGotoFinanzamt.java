package com.lp.client.frame.component;

import com.lp.client.finanz.InternalFrameFinanz;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;

public class WrapperGotoFinanzamt extends WrapperGotoButton implements WrapperGotoConstants {
	private static final long serialVersionUID = -7153118167835800387L;
	
	public WrapperGotoFinanzamt() {
		super(GOTO_FINANZAMT); 
	}

	public WrapperGotoFinanzamt(String text) {
		super(text, GOTO_FINANZAMT) ;
	}
	
	@Override
	protected void geheZuImpl(Integer where, Object okey) throws Throwable {
		if(where != GOTO_FINANZAMT) {
			throw new IllegalArgumentException("Where: " + where) ;
		}
		
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
			InternalFrameFinanz ifF = (InternalFrameFinanz) LPMain
					.getInstance().getDesktop()
					.holeModul(LocaleFac.BELEGART_FINANZBUCHHALTUNG);
			ifF.geheZu(
					ifF.IDX_TABBED_PANE_FINANZAMT,
					ifF.getTabbedPaneFinanzamt().IDX_FINANZAEMTER,
					okey,
					null,
					null);
		}		
	}
}
