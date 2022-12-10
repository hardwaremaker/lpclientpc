package com.lp.client.pc.erroraction;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperGotoFinanzamt;
import com.lp.client.frame.component.WrapperGotoFinanzamtReversecharge;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.SteuerkategorieFehltException;

public class SteuerkategorieFehltError implements IErrorAction {
	private FinanzamtDto finanzamtDto ;
	private ReversechargeartDto reversechargeartDto ;

	protected boolean prepare(ExceptionLP ex) throws Throwable {
		if(finanzamtDto != null && reversechargeartDto != null) {
			return true ;
		}
		
		if(ex.getExceptionData() instanceof SteuerkategorieFehltException) {
			SteuerkategorieFehltException e = (SteuerkategorieFehltException) ex.getExceptionData() ;
			
			finanzamtDto = DelegateFactory.getInstance()
					.getFinanzDelegate().finanzamtFindByPrimaryKey(e.getFinanzamtId(), LPMain.getTheClient().getMandant()) ;
			reversechargeartDto = DelegateFactory.getInstance()
					.getFinanzServiceDelegate().reversechargeartFindByPrimaryKey(e.getReversechargeartId()) ;
			
			return true ;
		}	
		
		return false ;
	}
	
	@Override
	public String getMsg(ExceptionLP ex) {
		try {
			if(prepare(ex)) {
				List<Object> allInfos = new ArrayList<Object>() ;
				allInfos.add(finanzamtDto.getPartnerDto().getCName1nachnamefirmazeile1()) ;
 				allInfos.add(reversechargeartDto.getCNr()) ;
				allInfos.addAll(ex.getAlInfoForTheClient()) ;
				return LPMain.getMessageTextRespectUISPr("finanz.error.keinesteuerkategoriegefunden", allInfos.toArray()) ;				
			}
		} catch(ExceptionLP e) {
		} catch(Throwable t) {
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(final JDialog dialog, JPanel panel, ExceptionLP ex) {
		if (!LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
			return false ;
		}

		try {
			if(prepare(ex)) {
				HvLayout layout = HvLayoutFactory.create(panel, 
						"width 300:300:1024, height 30:30:400, gap 10 10 10 10") ;
				WrapperGotoButton btnFinanzamt = new WrapperGotoFinanzamt(
						LPMain.getMessageTextRespectUISPr("lp.error.finanzamtvalue", 
								finanzamtDto.getPartnerDto().getCName1nachnamefirmazeile1())) ;
				btnFinanzamt.closeDialogOnGoto(dialog);
				btnFinanzamt.setOKey(finanzamtDto.getPartnerIId());
				btnFinanzamt.setEnabled(false);

				WrapperGotoButton btnReversecharge = new WrapperGotoFinanzamtReversecharge(
						LPMain.getMessageTextRespectUISPr("lp.error.reversechargeartvalue", 
								reversechargeartDto.getCNr())) ;
				btnReversecharge.closeDialogOnGoto(dialog);
				btnReversecharge.setOKey(finanzamtDto.getPartnerIId());
				btnReversecharge.setDetailKey(reversechargeartDto.getIId());
				btnReversecharge.setEnabled(false);
				
				layout
					.add(btnFinanzamt, "split 2, w 50%")
					.add(btnReversecharge, "w 50%" );
				
				return true ;
			}			
		} catch(ExceptionLP e) {
		} catch(Throwable t) {
		}
		
		return false ;
	}
}
