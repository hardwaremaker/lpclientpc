package com.lp.client.pc.erroraction;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperGotoStueckliste;
import com.lp.client.frame.component.WrapperGotoStuecklistePosition;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;

public class ScriptInstanzierungNichtMoeglichError implements IErrorAction {
	public ScriptInstanzierungNichtMoeglichError() {	
	}
	
	@Override
	public String getMsg(ExceptionLP ex) {
		List<?> allInfos = ex.getAlInfoForTheClient();
		return LPMain.getMessageTextRespectUISPr("lp.script.nichtinstanzierbar", allInfos.toArray());
	}
	
	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel, ExceptionLP ex) {
		if(!LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_STUECKLISTE)) {
			return false;
		}
		try {
			HvLayout layout = HvLayoutFactory.create(panel, 
					"width 300:300:1024, height 30:30:400, gap 10 10 10 10") ;
			WrapperGotoButton btnStkl = new WrapperGotoStueckliste(
					LPMain.getTextRespectUISPr("stkl.stueckliste")) ;
			btnStkl.closeDialogOnGoto(dialog);
			btnStkl.setOKey((Integer)ex.getAlInfoForTheClient().get(0));
			btnStkl.setEnabled(false);
			
			layout
				.add(btnStkl, "split 2, w 50%");
			
			return true ;			
		} catch(Throwable t) {
			return false;
		}
		
	}
}
