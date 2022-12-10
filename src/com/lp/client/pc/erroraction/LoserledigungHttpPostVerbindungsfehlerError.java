package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.util.VerbindungsfehlerBeiHttpPostLosExc;

public class LoserledigungHttpPostVerbindungsfehlerError implements IErrorAction {

	@Override
	public String getMsg(ExceptionLP exception) {
		if (exception.getExceptionData() instanceof VerbindungsfehlerBeiHttpPostLosExc) {
			VerbindungsfehlerBeiHttpPostLosExc excData = (VerbindungsfehlerBeiHttpPostLosExc) exception.getExceptionData();
			return LPMain.getMessageTextRespectUISPr("fert.loserledigung.error.http.verbindungsfehler", 
					excData.getHttpProxyConfig().asString(), excData.getLosDto().getCNr());
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel,
			ExceptionLP exception) {
		return false;
	}

}
