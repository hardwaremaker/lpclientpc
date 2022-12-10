package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.util.GTINBasisnummerLaengeUngueltigException;

public class GTINBasisnummerLaengeUngueltigError implements IErrorAction {

	@Override
	public String getMsg(ExceptionLP exception) {
		if (exception.getExceptionData() instanceof GTINBasisnummerLaengeUngueltigException) {
			GTINBasisnummerLaengeUngueltigException ex = 
					(GTINBasisnummerLaengeUngueltigException) exception.getExceptionData();
			
			return LPMain.getMessageTextRespectUISPr("artikel.gtingenerierung.basisnummerlaengeungueltig", 
					ex.getValue(), ex.getParametername());
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel,
			ExceptionLP exception) {
		return false;
	}

}
