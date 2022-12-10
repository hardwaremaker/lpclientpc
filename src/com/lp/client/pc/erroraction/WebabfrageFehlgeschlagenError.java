package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.PartSearchInvalidParamExc;
import com.lp.server.artikel.service.PartSearchUnexpectedResponseExc;
import com.lp.server.partner.service.LieferantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.WebabfrageArtikellieferantException;

public class WebabfrageFehlgeschlagenError implements IErrorAction {

	public WebabfrageFehlgeschlagenError() {
	}

	@Override
	public String getMsg(ExceptionLP exc) {
		try {
			if (!(exc.getExceptionData() instanceof WebabfrageArtikellieferantException)) {
				return null;
			}
			WebabfrageArtikellieferantException excData = (WebabfrageArtikellieferantException)exc.getExceptionData();
	
			// TODO 403 Forbidden
			if (exc.getICode() == EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_RESPONSE_FORBIDDEN) {
				PartSearchUnexpectedResponseExc partSearchExc = (PartSearchUnexpectedResponseExc)exc.getAlInfoForTheClient().get(2);
				ArtikellieferantDto artliefDto = (ArtikellieferantDto)exc.getAlInfoForTheClient().get(1);
				LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate().lieferantFindByPrimaryKey(artliefDto.getLieferantIId());
				String msgToken = partSearchExc.getApiError() != null ? 
						"artikel.webabfrage.error.responseforbidden.apierrormsg" : "artikel.webabfrage.error.responseforbidden";
				return LPMain.getMessageTextRespectUISPr(msgToken, 
						excData.getArtikelDto().getCNr(), partSearchExc.getStatusLine(), lieferantDto.getPartnerDto().formatName(), partSearchExc.getApiError());
			}
			
			if (exc.getICode() == EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_UNERWARTETE_RESPONSE) {
				PartSearchUnexpectedResponseExc partSearchExc = (PartSearchUnexpectedResponseExc)exc.getAlInfoForTheClient().get(2);
				if (partSearchExc instanceof PartSearchInvalidParamExc) {
					PartSearchInvalidParamExc invalidParamExc = (PartSearchInvalidParamExc)partSearchExc;
					return LPMain.getMessageTextRespectUISPr("artikel.webabfrage.error.parameterungueltig", excData.getArtikelDto().getCNr(), invalidParamExc.getErrorMsg());
				}
				String apiError = partSearchExc.getApiError() != null ? LPMain.getMessageTextRespectUISPr("artikel.webabfrage.error.apierrormsg", partSearchExc.getApiError()) : "";
				return LPMain.getMessageTextRespectUISPr("artikel.webabfrage.error.unerwarteresponse", excData.getArtikelDto().getCNr(), partSearchExc.getStatusLine(), apiError);
	//			if (partSearchExc instanceof PartSearchPartNotFoundExc) {
	//				PartSearchPartNotFoundExc partNotFoundExc = (PartSearchPartNotFoundExc)partSearchExc;
	//				return "Artikel nicht gefunden";
	//			}
			}
	
			String liefartnr = excData.getResultLiefNr().wasExecuted() 
					? "\n" + LPMain.getMessageTextRespectUISPr("artikel.webabfrage.error.suchparameter.liefartnr", 
							excData.getResultLiefNr().getSearchValue())
					: "";
			String hstnr = excData.getResultHstNr().wasExecuted() 
					? "\n" + LPMain.getMessageTextRespectUISPr("artikel.webabfrage.error.suchparameter.hstnr", 
							excData.getResultHstNr().getSearchValue())
					: "";
			
			if (exc.getICode() == EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_KEIN_ERGEBNIS) {
				return LPMain.getMessageTextRespectUISPr("artikel.webabfrage.error.keinergebnis", 
						excData.getArtikelDto().getCNr(), liefartnr, hstnr);
			}
			
			if (exc.getICode() == EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_MEHRFACHE_ERGEBNISSE) {
				return LPMain.getMessageTextRespectUISPr("artikel.webabfrage.error.mehrfacheergebnisse", 
						excData.getArtikelDto().getCNr(), liefartnr, hstnr);
			}
		} catch(ExceptionLP e) {			
		} catch(Throwable t) {
			
		}
		
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel, ExceptionLP exception) {
		return false;
	}

}
