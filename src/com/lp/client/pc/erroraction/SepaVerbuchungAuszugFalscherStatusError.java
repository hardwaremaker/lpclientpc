package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.util.SepaVerbuchungAuszugFalscherStatusException;

public class SepaVerbuchungAuszugFalscherStatusError implements IErrorAction {

	public SepaVerbuchungAuszugFalscherStatusError() {
	}

	@Override
	public String getMsg(ExceptionLP exception) {
		if (exception.getExceptionData() instanceof SepaVerbuchungAuszugFalscherStatusException) {
			SepaVerbuchungAuszugFalscherStatusException data = (SepaVerbuchungAuszugFalscherStatusException)exception.getExceptionData();
			return LPMain.getMessageTextRespectUISPr("finanz.error.sepaverbuchungauszugmitfalschenstatus", 
					String.valueOf(data.getSepakontoauszugDto().getIAuszug()), data.getVerbuchungsstatus());
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel, ExceptionLP exception) {
		return false;
	}

}
