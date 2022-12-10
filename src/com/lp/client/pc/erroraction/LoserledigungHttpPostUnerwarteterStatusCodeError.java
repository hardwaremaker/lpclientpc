package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.util.UnerwarteterStatusCodeBeiHttpLosExc;

public class LoserledigungHttpPostUnerwarteterStatusCodeError implements IErrorAction {

	@Override
	public String getMsg(ExceptionLP exception) {
		if (exception.getExceptionData() instanceof UnerwarteterStatusCodeBeiHttpLosExc) {
			UnerwarteterStatusCodeBeiHttpLosExc excData = (UnerwarteterStatusCodeBeiHttpLosExc)exception.getExceptionData();
			return LPMain.getMessageTextRespectUISPr("fert.loserledigung.error.http.statuscode", 
					excData.getLosDto().getCNr(), excData.getHttpRequestConfig().getHostConfig().asString(), 
					excData.getReasonPhrase(), excData.getStatusCode());
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel,
			ExceptionLP exception) {
		return false;
	}

}
