package com.lp.client.finanz;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagExportException;
import com.lp.util.EJBExceptionLP;

public class DialogCsvExportZVResult extends DialogExportZVResult {
	private static final long serialVersionUID = -3061371354624811863L;

	private List<ZahlungsvorschlagExportException> messages;
	
	public DialogCsvExportZVResult(Frame owner, List<ZahlungsvorschlagExportException> messages) {
		super(owner, LPMain.getTextRespectUISPr("er.zahlungsvorschlag.csvexport.titel"), false);
		setMessages(messages);
	}

	public List<ZahlungsvorschlagExportException> getMessages() {
		if (messages == null) {
			messages = new ArrayList<ZahlungsvorschlagExportException>();
		}
		return messages;
	}
	
	public void setMessages(List<ZahlungsvorschlagExportException> messages) {
		this.messages = messages;
	}
	
	@Override
	protected String getExceptionMessage(int rowIndex) {
		StringBuilder msgBuilder = new StringBuilder();
		ZahlungsvorschlagExportException error = getMessages().get(rowIndex);
		
		if (ZahlungsvorschlagExportException.SEVERITY_WARNING.equals(error.getSeverity())) {
			msgBuilder.append(LPMain.getTextRespectUISPr("lp.warning"))
				.append(": ");
		}
		
		if (EJBExceptionLP.FEHLER_FINANZ_ZVEXPORT_LF_HAT_KEINE_BANKVERBINDUNG == error.getCode()){
			msgBuilder.append(LPMain.getMessageTextRespectUISPr("er.zahlungsvorschlag.sepaexport.lieferanthatkeinebankverbindung", 
					error.getErDto().getCNr(), error.getPartnerDto().getCName1nachnamefirmazeile1()));
		} else if (EJBExceptionLP.FEHLER_FINANZ_ZVEXPORT_BANK_HAT_KEINEN_ORT == error.getCode()) {
			msgBuilder.append(LPMain.getMessageTextRespectUISPr("er.zahlungsvorschlag.sepaexport.bankhatkeinenort", 
					error.getErDto().getCNr(), error.getPartnerDto().getCName1nachnamefirmazeile1()));
		} else if (EJBExceptionLP.FEHLER_FINANZ_ZVEXPORT_BANK_AUS_NICHT_SEPA_LAND == error.getCode()){
			msgBuilder.append(LPMain.getMessageTextRespectUISPr("er.zahlungsvorschlag.csvexport.bankausnichtsepaland", 
					error.getErDto().getCNr(), error.getPartnerDto().getCName1nachnamefirmazeile1()));
		} else if (EJBExceptionLP.FEHLER_FINANZ_ZVEXPORT_KEINE_BELEGE_ZU_EXPORTIEREN == error.getCode()){
			msgBuilder.append(LPMain.getTextRespectUISPr("er.zahlungsvorschlag.csvexport.keinebelege"));
		} else if (EJBExceptionLP.FEHLER_FINANZ_ZVEXPORT_ER_MIT_ABWEICHENDER_BANKVERBINDUNG_HAT_KEINE == error.getCode()) {
			msgBuilder.append(LPMain.getMessageTextRespectUISPr("er.zahlungsvorschlag.sepaexport.keineabweichendebankverbindung", 
					error.getErDto().getCNr(), error.getPartnerDto().getCName1nachnamefirmazeile1()));
		} else {
			msgBuilder.append(LPMain.getTextRespectUISPr("lp.unbekannt"));
		}
		
		return msgBuilder.toString();
	}

	@Override
	protected Integer getExceptionCode(int rowIndex) {
		ZahlungsvorschlagExportException error = getMessages().get(rowIndex);
		
		return error.getCode();
	}

	@Override
	protected Integer getMessagesCount() {
		return getMessages().size();
	}
}
