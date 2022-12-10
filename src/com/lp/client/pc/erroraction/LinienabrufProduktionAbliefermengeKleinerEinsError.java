package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.util.LinienabrufProduktionAbliefermengeKleinerEinsExc;

public class LinienabrufProduktionAbliefermengeKleinerEinsError implements
		IErrorAction {

	@Override
	public String getMsg(ExceptionLP exception) {
		if (exception.getExceptionData() instanceof LinienabrufProduktionAbliefermengeKleinerEinsExc) {
			LinienabrufProduktionAbliefermengeKleinerEinsExc data =
					(LinienabrufProduktionAbliefermengeKleinerEinsExc)exception.getExceptionData();
			return LPMain.getMessageTextRespectUISPr(
					"linienabruf.produktion.berechneteabliefermengekleinereins", data.getLosDto().getCNr());
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel,
			ExceptionLP exception) {
		return false;
	}

}
