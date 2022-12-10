package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.util.FremdsystemnrNichtNummerischException;

public class FremdsystemnummerNichtNummerischError implements IErrorAction {

	@Override
	public String getMsg(ExceptionLP exception) {
		if (exception.getExceptionData() instanceof FremdsystemnrNichtNummerischException) {
			FremdsystemnrNichtNummerischException ex = 
					(FremdsystemnrNichtNummerischException) exception.getExceptionData();
			
			if (ex.getLieferantDto() != null) {
				return LPMain.getMessageTextRespectUISPr("lieferant.fremdsystemnrnichtnummerisch", 
						ex.getLieferantDto().getPartnerDto().getCName1nachnamefirmazeile1());
			}
			
			if (ex.getKundeDto() != null) {
				return LPMain.getMessageTextRespectUISPr("kunde.fremdsystemnrnichtnummerisch", 
						LPMain.getTextRespectUISPr("part.kund.fremdsystemnr"),
						ex.getKundeDto().getPartnerDto().getCName1nachnamefirmazeile1());
			}
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel,
			ExceptionLP exception) {
		// TODO Auto-generated method stub
		return false;
	}

}
