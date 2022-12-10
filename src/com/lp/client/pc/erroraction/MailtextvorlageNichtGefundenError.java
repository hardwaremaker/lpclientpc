package com.lp.client.pc.erroraction;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class MailtextvorlageNichtGefundenError implements IErrorAction {

	@Override
	public String getMsg(ExceptionLP exc) {
		List<?> params = exc.getAlInfoForTheClient();
		String reportname = params.size() > 3 ? (String)params.get(3) : null;
		
		return !Helper.isStringEmpty(reportname) 
				? LPMain.getMessageTextRespectUISPr(msgToken(exc.getICode()), "")
				: LPMain.getMessageTextRespectUISPr(msgToken(exc.getICode()), "(" + reportname + ")");
	}
	
	private String msgToken(int ejbCode) {
		if (EJBExceptionLP.FEHLER_DRUCKEN_MAILTEXTVORLAGE_HTML_NICHT_GEFUNDEN == ejbCode) {
			return "lp.drucken.mailtextvorlagefehlt.html";
		}
		
		if (EJBExceptionLP.FEHLER_DRUCKEN_MAILTEXTVORLAGE_MIT_SIGNATUR_NICHT_GEFUNDEN == ejbCode) {
			return "lp.drucken.mailtextvorlagefehlt.mitsignatur";
		}
		
		return "lp.drucken.mailtextvorlagefehlt";
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel, ExceptionLP exception) {
		return false;
	}

}
