package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.util.BuchungenMitAuszugsnummerVorhandenException;

public class BuchungenMitAuszugsnummerVorhandenError implements IErrorAction {

	private String msgToken;
	
	public BuchungenMitAuszugsnummerVorhandenError(String msgToken) {
		this.msgToken = msgToken;
	}
	
	@Override
	public String getMsg(ExceptionLP exception) {
		if (exception.getExceptionData() instanceof BuchungenMitAuszugsnummerVorhandenException) {
			BuchungenMitAuszugsnummerVorhandenException e = (BuchungenMitAuszugsnummerVorhandenException) exception.getExceptionData();
			
			return LPMain.getMessageTextRespectUISPr(msgToken, String.valueOf(e.getSepakontoauszugDto().getIAuszug()));
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel, ExceptionLP exception) {
		return false;
	}
}