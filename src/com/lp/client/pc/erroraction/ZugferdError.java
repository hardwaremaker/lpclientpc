package com.lp.client.pc.erroraction;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundeDto;
import com.lp.util.EJBExceptionLP;

public class ZugferdError implements IErrorAction {

	public ZugferdError() {
	}

	@Override
	public String getMsg(ExceptionLP exception) {
		if (EJBExceptionLP.FEHLER_ZUGFERD_LIEFERANTENNUMMER_FEHLT == exception.getICode()) {
			KundeDto kundeDto = (KundeDto) exception.getAlInfoForTheClient().get(1); 
			return LPMain.getMessageTextRespectUISPr("rech.zugferd.error.lieferantennummerfehlt", kundeDto.getPartnerDto().formatFixName1Name2());
		}
		
		if (EJBExceptionLP.FEHLER_ZUGFERD_ADRESSINFO_FEHLT == exception.getICode()) {
			KundeDto kundeDto = (KundeDto) exception.getAlInfoForTheClient().get(0); 
			return LPMain.getMessageTextRespectUISPr("rech.zugferd.error.adressinfofehlt", kundeDto.getPartnerDto().formatFixName1Name2());
		}
		
		if (EJBExceptionLP.FEHLER_ZUGFERD_EINHEIT_MAPPING_NICHT_GEFUNDEN == exception.getICode()) {
			List<?> clientInfo = exception.getAlInfoForTheClient(); 
			return LPMain.getMessageTextRespectUISPr("rech.zugferd.error.einheitmappingnichtgefunden", 
					clientInfo.get(0), clientInfo.get(1));
			
		}
		return "Unbekannter ZUGFeRD Fehler";
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel, ExceptionLP exception) {
		return false;
	}

}
