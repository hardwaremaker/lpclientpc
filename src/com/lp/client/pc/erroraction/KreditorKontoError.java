package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.LieferantDto;
import com.lp.util.GotoHelper;
import com.lp.util.KreditorKontoException;

public class KreditorKontoError implements IErrorAction {


	@Override
	public String getMsg(ExceptionLP exception) {
		try {
			if (exception.getExceptionData() instanceof KreditorKontoException) {
				KreditorKontoException ex = (KreditorKontoException)exception.getExceptionData();
				return LPMain.getMessageTextRespectUISPr("lieferant.kreditorkontofehlt", 
						ex.getLieferantDto().getPartnerDto().getCName1nachnamefirmazeile1());
			}
		} catch (Throwable t) {
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel,
			ExceptionLP exception) {
		try {
			if (exception.getExceptionData() instanceof KreditorKontoException) {
				KreditorKontoException ex = (KreditorKontoException)exception.getExceptionData();
				
				HvLayout layout = HvLayoutFactory.create(panel, 
						"width 300:300:1024, height 30:30:400, gap 10 10 10 10") ;
				WrapperGotoButton btnKonto = createBtn(ex.getLieferantDto()) ;
				btnKonto.closeDialogOnGoto(dialog);
				btnKonto.setOKey(ex.getLieferantDto().getIId());
				btnKonto.setEnabled(false);
				
				layout
					.add(btnKonto, "split 2, w 50%") ;
				
				return true ;				
			}
		} catch (Throwable t) {
		}
		return false;
	}

	private WrapperGotoButton createBtn(LieferantDto lieferantDto) {
		return new WrapperGotoButton(LPMain.getTextRespectUISPr("lp.lieferant"),
				GotoHelper.GOTO_LIEFERANT_KONDITIONEN);
	}

}
