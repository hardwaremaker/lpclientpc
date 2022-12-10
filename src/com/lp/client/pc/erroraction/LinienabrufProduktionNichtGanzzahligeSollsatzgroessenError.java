package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.util.LinienabrufProduktionNichtGanzzahligeSollsatzgroessenExc;

public class LinienabrufProduktionNichtGanzzahligeSollsatzgroessenError
		implements IErrorAction {

	@Override
	public String getMsg(ExceptionLP exception) {
		if (exception.getExceptionData() instanceof LinienabrufProduktionNichtGanzzahligeSollsatzgroessenExc) {
			LinienabrufProduktionNichtGanzzahligeSollsatzgroessenExc data = 
					(LinienabrufProduktionNichtGanzzahligeSollsatzgroessenExc)exception.getExceptionData();
			return LPMain.getMessageTextRespectUISPr(
					"linienabruf.produktion.sollsatzmengenichtganzzahlig", 
					data.getLosDto().getCNr(), data.getArtikelCnr());
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel,
			ExceptionLP exception) {
		return false;
	}

}
