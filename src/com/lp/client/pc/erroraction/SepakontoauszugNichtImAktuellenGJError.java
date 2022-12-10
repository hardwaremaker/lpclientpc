package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.util.Helper;
import com.lp.util.SepakontoauszugNichtImAktuellenGJException;

public class SepakontoauszugNichtImAktuellenGJError implements IErrorAction {

	public SepakontoauszugNichtImAktuellenGJError() {
	}

	@Override
	public String getMsg(ExceptionLP exception) {
		if (exception.getExceptionData() instanceof SepakontoauszugNichtImAktuellenGJException) {
			SepakontoauszugNichtImAktuellenGJException data = (SepakontoauszugNichtImAktuellenGJException)exception.getExceptionData();
			return LPMain.getMessageTextRespectUISPr("finanz.error.sepakontoauszugnichtimaktuellengeschaeftsjahr", 
					String.valueOf(data.getSepakontoauszugDto().getIAuszug()), 
					Helper.formatDatum(data.getSepakontoauszugDto().getTAuszug(), LPMain.getInstance().getUISprLocale()),
					String.valueOf(data.getGeschaeftsjahr()));
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel, ExceptionLP exception) {
		return false;
	}

}
