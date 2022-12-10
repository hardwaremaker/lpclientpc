package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperGotoDebitorenKonto;
import com.lp.client.frame.component.WrapperGotoKreditorenKonto;
import com.lp.client.frame.component.WrapperGotoSachKonto;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.SteuerkategorieDefinitionFehltException;

public class SteuerkategorieDefinitionError implements IErrorAction {

	@Override
	public String getMsg(ExceptionLP ex) {
		try {
			if(ex.getExceptionData() instanceof SteuerkategorieDefinitionFehltException) {
				return LPMain.getMessageTextRespectUISPr("finanz.error.keinesteuerkategoriedefinition",
						ex.getAlInfoForTheClient().toArray()) ;
			}			
		} catch(Throwable t) {
		}
		
		return null ;
	}

	@Override
	public boolean shouldBeShown(final JDialog dialog, JPanel panel, ExceptionLP ex) {
		if (!LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
			return false ;
		}

		try {
			if (ex.getExceptionData() instanceof SteuerkategorieDefinitionFehltException) {
				SteuerkategorieDefinitionFehltException e = (SteuerkategorieDefinitionFehltException) ex.getExceptionData() ;
				
				HvLayout layout = HvLayoutFactory.create(panel, 
						"width 300:300:1024, height 30:30:400, gap 10 10 10 10") ;
				WrapperGotoButton btnKonto = createBtn(e.getKontoDto()) ;
				btnKonto.closeDialogOnGoto(dialog);
				btnKonto.setOKey(e.getKontoDto().getIId());
				btnKonto.setEnabled(false);
				
				layout
					.add(btnKonto, "split 2, w 50%") ;
				
				return true ;
			}
		} catch (Throwable t) {
		}
		return false;
	}
	
	private WrapperGotoButton createBtn(KontoDto kontoDto) {
		String s = LPMain.getMessageTextRespectUISPr("lp.error.dialog.konto", kontoDto.getCNr()) ;
		if(FinanzServiceFac.KONTOTYP_SACHKONTO.equals(kontoDto.getKontotypCNr())) {
			return new WrapperGotoSachKonto(s) ;
		}
		if(FinanzServiceFac.KONTOTYP_DEBITOR.equals(kontoDto.getKontotypCNr())) {
			return new WrapperGotoDebitorenKonto(s) ;
		}
		
		return new WrapperGotoKreditorenKonto(s) ;
	}
}
