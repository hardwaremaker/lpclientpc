package com.lp.client.pc.erroraction;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.util.SteuerkategoriekontoDefinitionFehltException;

public class SteuerkategorieKontoDefinitionFehltError implements IErrorAction {

	@Override
	public String getMsg(ExceptionLP exception) {
		try {
			if(exception.getExceptionData() instanceof SteuerkategoriekontoDefinitionFehltException) {
				SteuerkategoriekontoDefinitionFehltException e = (SteuerkategoriekontoDefinitionFehltException) exception.getExceptionData() ;
				SteuerkategorieDto stkDto = e.getSteuerkategorieDto() ;
				FinanzamtDto finanzamtDto = DelegateFactory.getInstance()
						.getFinanzDelegate().finanzamtFindByPrimaryKey(stkDto.getFinanzamtIId(), LPMain.getTheClient().getMandant()) ;
				ReversechargeartDto rcartDto = DelegateFactory.getInstance()
						.getFinanzServiceDelegate().reversechargeartFindByPrimaryKey(stkDto.getReversechargeartId()) ;
				MwstsatzbezDto mwstsatzbezDto = DelegateFactory.getInstance()
						.getMandantDelegate().mwstsatzbezFindByPrimaryKey(e.getMwstsatzbezId()) ;
				List<Object> allInfos = new ArrayList<Object>() ;
				allInfos.add(finanzamtDto.getPartnerDto().getCName1nachnamefirmazeile1()) ;
 				allInfos.add(rcartDto.getCNr()) ;
 				allInfos.add(stkDto.getCBez()) ;
 				allInfos.add(mwstsatzbezDto.getCBezeichnung());
				allInfos.addAll(exception.getAlInfoForTheClient()) ; 				
				return LPMain.getMessageTextRespectUISPr(
						"finanz.error.keinesteuerkategoriekontodefinition", allInfos.toArray()) ;
			}
		} catch(ExceptionLP e) {			
		} catch(Throwable t) {
			
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(final JDialog dialog, JPanel panel, ExceptionLP ex) {
		return false;
	}
}
