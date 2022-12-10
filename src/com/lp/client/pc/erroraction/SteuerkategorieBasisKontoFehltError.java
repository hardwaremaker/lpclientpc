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
import com.lp.util.SteuerkategorieBasiskontoException;

public class SteuerkategorieBasisKontoFehltError implements IErrorAction {
	private String msgToken ;
	
	public SteuerkategorieBasisKontoFehltError(String messageToken) {
		msgToken = messageToken ;
	}
	
	@Override
	public String getMsg(ExceptionLP ex) {
		try {
			if(ex.getExceptionData() instanceof SteuerkategorieBasiskontoException) {
				SteuerkategorieBasiskontoException e = (SteuerkategorieBasiskontoException) ex.getExceptionData() ;

				FinanzamtDto finanzamtDto = DelegateFactory.getInstance()
						.getFinanzDelegate().finanzamtFindByPrimaryKey(
								e.getSteuerkategorieDto().getFinanzamtIId(), LPMain.getTheClient().getMandant()) ;
				
				e.getSteuerkategorieDto().getFinanzamtIId();
				ReversechargeartDto rcartDto = DelegateFactory.getInstance()
						.getFinanzServiceDelegate().reversechargeartFindByPrimaryKey(
								e.getSteuerkategorieDto().getReversechargeartId()) ;
				List<Object> allInfos = new ArrayList<Object>() ;
				allInfos.add(finanzamtDto.getPartnerDto().getCName1nachnamefirmazeile1()) ;
				allInfos.add(rcartDto.getCNr()) ;
				allInfos.addAll(ex.getAlInfoForTheClient()) ;
				return LPMain.getMessageTextRespectUISPr(msgToken, allInfos.toArray());
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
