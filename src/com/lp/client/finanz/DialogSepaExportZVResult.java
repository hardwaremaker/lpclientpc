package com.lp.client.finanz;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import com.lp.client.pc.LPMain;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBSepaExportExceptionLP;

public class DialogSepaExportZVResult extends DialogExportZVResult {
	private static final long serialVersionUID = -5277313943297318822L;

	private List<EJBSepaExportExceptionLP> messages;

	public DialogSepaExportZVResult(Frame owner) {
		super(owner, LPMain.getTextRespectUISPr("er.zahlungsvorschlag.sepaexportfehlgeschlagen"), false);
	}
	
	public DialogSepaExportZVResult(Frame owner, List<EJBSepaExportExceptionLP> messages) {
		this(owner);
		setMessages(messages);
	}
	
	public void setMessages(List<EJBSepaExportExceptionLP> messages) {
		this.messages = messages;
	}
	
	public List<EJBSepaExportExceptionLP> getMessages() {
		if (messages == null) {
			messages = new ArrayList<EJBSepaExportExceptionLP>();
		}
		return messages;
	}
	
	protected String getExceptionMessage(int rowIndex) {
		StringBuilder msgBuilder = new StringBuilder();
		EJBSepaExportExceptionLP sepaError = getMessages().get(rowIndex);
		
		switch (sepaError.getCode()) {
			case EJBExceptionLP.FEHLER_SEPAEXPORT_BANK_AUS_NICHT_SEPA_LAND: {
				msgBuilder.append(
						LPMain.getMessageTextRespectUISPr("er.zahlungsvorschlag.sepaexportbankausnichtsepaland", 
						sepaError.getErDto().getCNr()));
				break;
			}
			
			case EJBExceptionLP.FEHLER_SEPAEXPORT_BANK_HAT_KEINEN_ORT: {
				msgBuilder.append(
						LPMain.getMessageTextRespectUISPr("er.zahlungsvorschlag.sepaexport.bankhatkeinenort", 
						sepaError.getErDto().getCNr(), sepaError.getPartnerDto().getCName1nachnamefirmazeile1()));
				break;
			}
			
			case EJBExceptionLP.FEHLER_SEPAEXPORT_LF_HAT_KEINE_BANKVERBINDUNG: {
				msgBuilder.append(
						LPMain.getMessageTextRespectUISPr("er.zahlungsvorschlag.sepaexport.lieferanthatkeinebankverbindung", 
						sepaError.getErDto().getCNr(), sepaError.getPartnerDto().getCName1nachnamefirmazeile1()));
				break;
			}
			
			case EJBExceptionLP.FEHLER_SEPAEXPORT_KEINE_IBAN_VORHANDEN: {
				msgBuilder.append(
					LPMain.getMessageTextRespectUISPr("er.zahlungsvorschlag.sepaexport.keineiban", 
					sepaError.getErDto().getCNr(), sepaError.getPartnerDto().getCName1nachnamefirmazeile1()));
				break;
			}
			
			case EJBExceptionLP.FEHLER_SEPAEXPORT_KEINE_BIC_VORHANDEN: {
				msgBuilder.append(
					LPMain.getMessageTextRespectUISPr("er.zahlungsvorschlag.sepaexport.keinebic", 
					sepaError.getErDto().getCNr(), sepaError.getPartnerDto().getCName1nachnamefirmazeile1()));
				break;
			}
			
			case EJBExceptionLP.FEHLER_PARAMETER_IS_NULL: {
				msgBuilder.append(sepaError.getMessage() + " ");
				msgBuilder.append(LPMain.getTextRespectUISPr("er.zahlungsvorschlag.sepaexportparameternull"));
				msgBuilder.append(": ER" + sepaError.getErDto().getCNr() + ", "
						+ "Lieferant " + sepaError.getPartnerDto().getCName1nachnamefirmazeile1());
				break;
			}
			
			case EJBExceptionLP.FEHLER_SEPAEXPORT_BIC_UNGUELTIG: {
				msgBuilder.append(
						LPMain.getMessageTextRespectUISPr("er.zahlungsvorschlag.sepaexport.bicungueltig", 
						sepaError.getErDto().getCNr(), sepaError.getPartnerDto().getCName1nachnamefirmazeile1()));
				break;
			}

			case EJBExceptionLP.FEHLER_SEPAEXPORT_ER_MIT_ABWEICHENDER_BANKVERBINDUNG_HAT_KEINE: {
				msgBuilder.append(
						LPMain.getMessageTextRespectUISPr("er.zahlungsvorschlag.sepaexport.keineabweichendebankverbindung", 
						sepaError.getErDto().getCNr(), sepaError.getPartnerDto().getCName1nachnamefirmazeile1()));
				break;
			}
}
		
		return msgBuilder.toString();
	}
	
	protected Integer getExceptionCode(int rowIndex) {
		EJBSepaExportExceptionLP sepaError = getMessages().get(rowIndex);
		
		return sepaError != null ? sepaError.getCode() : null;
	}	

	@Override
	protected Integer getMessagesCount() {
		return getMessages().size();
	}
}
