package com.lp.client.frame.component;

import com.lp.client.finanz.InternalFrameFinanz;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;

public class WrapperGotoFibuBankverbindung extends WrapperGotoButton implements WrapperGotoConstants {
	private static final long serialVersionUID = 2103790854946363049L;
	
	public WrapperGotoFibuBankverbindung() {
		super(GOTO_FIBUBANKVERBINDUNG);
	}
	
	public WrapperGotoFibuBankverbindung(String text) {
		super(text, GOTO_FIBUBANKVERBINDUNG);
	}
	
	@Override
	protected void geheZuImpl(Integer where, Object okey) throws Throwable {
		if(where != GOTO_FIBUBANKVERBINDUNG) {
			throw new IllegalArgumentException("Where: " + where) ;
		}
		
		if (!LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
			return;
		}
		
		InternalFrameFinanz ifF = (InternalFrameFinanz) LPMain
				.getInstance().getDesktop()
				.holeModul(LocaleFac.BELEGART_FINANZBUCHHALTUNG);
		ifF.geheZu(
				ifF.IDX_TABBED_PANE_BANKVERBINDUNG,
				ifF.getTabbedPaneBankverbindung().IDX_KOPFDATEN,
				okey,
				getDetailKey(),
				null);
	}
	
}
