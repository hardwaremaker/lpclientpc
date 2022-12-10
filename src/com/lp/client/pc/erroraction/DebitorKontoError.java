package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundeDto;
import com.lp.util.DebitorKontoException;

public class DebitorKontoError implements IErrorAction {

	@Override
	public String getMsg(ExceptionLP exception) {
		try {
			if(exception.getExceptionData() instanceof DebitorKontoException) {
				DebitorKontoException e = (DebitorKontoException) exception.getExceptionData() ;
				return LPMain.getMessageTextRespectUISPr("kunde.debitorkontofehlt", 
						e.getKundeDto().getPartnerDto().getCName1nachnamefirmazeile1());
			}
		} catch(Throwable t) {			
		}
		
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel,
			ExceptionLP exception) {
		try {
			if(exception.getExceptionData() instanceof DebitorKontoException) {
				DebitorKontoException e = (DebitorKontoException) exception.getExceptionData() ;
				
				HvLayout layout = HvLayoutFactory.create(panel, 
						"width 300:300:1024, height 30:30:400, gap 10 10 10 10") ;
				WrapperGotoButton btnKonto = createBtn(e.getKundeDto()) ;
				btnKonto.closeDialogOnGoto(dialog);
				btnKonto.setOKey(e.getKundeDto().getIId());
				btnKonto.setEnabled(false);
				
				layout
					.add(btnKonto, "split 2, w 50%") ;
				
				return true ;				
			}			
		} catch(Throwable t) {
		}
		return false;
	}
	
	private WrapperGotoButton createBtn(KundeDto kundeDto) {
		String s = LPMain.getTextRespectUISPr("lp.kunde") ;
		return new WrapperGotoButton(s, com.lp.util.GotoHelper.GOTO_KUNDE_KONDITIONEN) ; 
	}
}
