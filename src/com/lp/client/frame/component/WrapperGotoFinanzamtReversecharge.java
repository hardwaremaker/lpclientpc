package com.lp.client.frame.component;

import com.lp.client.finanz.InternalFrameFinanz;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;

public class WrapperGotoFinanzamtReversecharge extends WrapperGotoButton implements WrapperGotoConstants {
	private static final long serialVersionUID = 772029016710750126L;
	
	public WrapperGotoFinanzamtReversecharge() {
		super(GOTO_REVERSECHARGE); 
	}

	public WrapperGotoFinanzamtReversecharge(String text) {
		super(text, GOTO_REVERSECHARGE) ;
	}

	@Override
	protected void geheZuImpl(Integer where, Object okey) throws Throwable {
		if(where != GOTO_REVERSECHARGE) {
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
					ifF.getTabbedPaneFinanzamt().IDX_REVERSECHARGEART,
					okey,
					getDetailKey(),
					null);
		}		
	}
}
