package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.util.LsAlsRePosWaehrungenUnterschiedlichException;

public class LsAlsRePosWaehrungenUnterschiedlichError implements IErrorAction {

	@Override
	public String getMsg(ExceptionLP exception) {
		if (exception.getExceptionData() instanceof LsAlsRePosWaehrungenUnterschiedlichException) {
			LsAlsRePosWaehrungenUnterschiedlichException exceptionData = 
					(LsAlsRePosWaehrungenUnterschiedlichException) exception.getExceptionData();
			
			return LPMain.getMessageTextRespectUISPr("rechnung.error.lieferscheinundrechnungwaehrungunterschiedlich", 
					exceptionData.getLieferscheinDto().getCNr(), exceptionData.getLieferscheinDto().getWaehrungCNr(), 
					exceptionData.getRechungDto().getCNr(), exceptionData.getRechungDto().getWaehrungCNr());
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel,
			ExceptionLP exception) {
		return false;
	}

}
