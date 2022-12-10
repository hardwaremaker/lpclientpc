package com.lp.client.pc.erroraction;

import java.util.Collection;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.util.Helper;

public class UnbekannteSerienChargennummer implements IErrorAction {

	public UnbekannteSerienChargennummer() {
	}

	@Override
	public String getMsg(ExceptionLP exception) {
		ArtikelDto artikelDto = (ArtikelDto)exception.getAlInfoForTheClient().get(0);
		Collection<String> snrChnrs = (Collection<String>)exception.getAlInfoForTheClient().get(1);
		
		return LPMain.getMessageTextRespectUISPr("lp.error.unbekannteserienchargennummern", 
				artikelDto.getCNr(), Helper.erzeugeStringAusStringArray(snrChnrs.toArray(new String[] {})));
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel, ExceptionLP exception) {
		return false;
	}

}
