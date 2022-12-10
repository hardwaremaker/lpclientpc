package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.util.Helper;
import com.lp.util.LinienabrufProduktionKeinLosGefundenException;

public class LinienabrufProduktionKeinLosGefundenError implements IErrorAction {

	@Override
	public String getMsg(ExceptionLP exception) {
		if (exception.getExceptionData() instanceof LinienabrufProduktionKeinLosGefundenException) {
			LinienabrufProduktionKeinLosGefundenException data = 
					(LinienabrufProduktionKeinLosGefundenException)exception.getExceptionData();
			return LPMain.getMessageTextRespectUISPr(
					"linienabruf.produktion.keinloszuartikelnummergefunden", data.getArtikelCnr(),
					data.getKundeKBezLieferadresse(), Helper.formatZahl(data.getForecastpositionIId(), LPMain.getInstance().getUISprLocale()));
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel,
			ExceptionLP exception) {
		return false;
	}

}
