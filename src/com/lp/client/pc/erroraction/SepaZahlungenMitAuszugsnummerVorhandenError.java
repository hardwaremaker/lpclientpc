package com.lp.client.pc.erroraction;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.util.SepaZahlungenMitAuszugsnummerVorhandenException;

public class SepaZahlungenMitAuszugsnummerVorhandenError implements IErrorAction {

	public SepaZahlungenMitAuszugsnummerVorhandenError() {
	}

	@Override
	public String getMsg(ExceptionLP exception) {
		if (exception.getExceptionData() instanceof SepaZahlungenMitAuszugsnummerVorhandenException) {
			SepaZahlungenMitAuszugsnummerVorhandenException excData = (SepaZahlungenMitAuszugsnummerVorhandenException) exception.getExceptionData();
			
			String msg = LPMain.getMessageTextRespectUISPr("finanz.error.zahlungenmitauszugsnummervorhanden.sepaverbuchung", 
					String.valueOf(excData.getSepakontoauszugDto().getIAuszug()));
			if (!excData.getRechnungen().isEmpty()) {
				msg += "\n" + LPMain.getTextRespectUISPr("fb.export.rechnungen") 
					+ ": " + StringUtils.join(reCnrs(excData.getRechnungen()).iterator(), ", ");;
			}
			if (!excData.getEingangsrechnungen().isEmpty()) {
				msg += "\n" + LPMain.getTextRespectUISPr("fb.export.eingangsrechnungen") 
					+ ": " + StringUtils.join(erCnrs(excData.getEingangsrechnungen()).iterator(), ", ");
			}
			return msg;
		}
		return null;
	}
	
	private List<String> erCnrs(List<EingangsrechnungDto> eingangsrechnungen) {
		List<String> cnrs = new ArrayList<String>();
		for (EingangsrechnungDto eingangsrechnung : eingangsrechnungen) {
			cnrs.add(eingangsrechnung.getCNr());
		}
		return cnrs;
	}
	
	private List<String> reCnrs(List<RechnungDto> rechnungen) {
		List<String> cnrs = new ArrayList<String>();
		for (RechnungDto rechnung : rechnungen) {
			cnrs.add(rechnung.getCNr());
		}
		return cnrs;
	}
	
	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel, ExceptionLP exception) {
		if (exception.getExceptionData() instanceof SepaZahlungenMitAuszugsnummerVorhandenException) {
			SepaZahlungenMitAuszugsnummerVorhandenException excData = (SepaZahlungenMitAuszugsnummerVorhandenException) exception.getExceptionData();
			HvLayout layout = HvLayoutFactory.create(panel, "width 300:300:1024, height 30:30:400, gap 10 10 10 10");
			if (!excData.getRechnungen().isEmpty()) {
				WrapperLabel labelRe = new WrapperLabel("Rechnungen:");
				WrapperLabel labelReList = new WrapperLabel();
				String reText = "";
				for (RechnungDto rechnungDto : excData.getRechnungen()) {
					reText += rechnungDto.getCNr() + " ";
				}
				labelReList.setText(reText);
				layout.add(labelRe, "50").add(labelReList).wrap();
			}
			
			if (!excData.getEingangsrechnungen().isEmpty()) {
				WrapperLabel labelRe = new WrapperLabel("Rechnungen:");
				WrapperLabel labelReList = new WrapperLabel();
				String reText = "";
				for (EingangsrechnungDto eingangsrechnungDto : excData.getEingangsrechnungen()) {
					reText += eingangsrechnungDto.getCNr() + " ";
				}
				labelReList.setText(reText);
				layout.add(labelRe, "50").add(labelReList).wrap();
			}
			return true;
		}
		return false;
	}

}
