package com.lp.client.pc.erroraction;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.WrapperGotoArtikelSonstiges;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;

public class ArtikelUrsprungslandFehltError implements IErrorAction {

	private Integer artikelId;
	private String artikelCnr;
	
	private boolean prepare(ExceptionLP ex) {
		if(artikelId != null) return true;
		
		List<?> infos = ex.getAlInfoForTheClient();
		if(infos != null && infos.size() == 2) {
			artikelCnr = (String) infos.get(0);
			artikelId = (Integer) infos.get(1);
			return true;
		}
		
		return false;
	}

	@Override
	public String getMsg(ExceptionLP exception) {
		if(prepare(exception)) {
			return LPMain.getMessageTextRespectUISPr(
					"artikel.error.ursprungslandfehlt", artikelCnr);
		}
		
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel, ExceptionLP exception) {
		if (!LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_ARTIKEL)) {
			return false ;
		}
		
		if(prepare(exception)) {
			HvLayout layout = HvLayoutFactory.create(panel, 
					"width 300:300:1024, height 30:30:400, gap 10 10 10 10") ;
			WrapperGotoButton btnArtikel = new WrapperGotoArtikelSonstiges(
					LPMain.getMessageTextRespectUISPr("lp.error.artikelvalue", 
							artikelCnr)) ;
			btnArtikel.closeDialogOnGoto(dialog);
			btnArtikel.setOKey(artikelId);
			btnArtikel.setEnabled(false);
			
			layout.add(btnArtikel, "split 2, w 50%");
			
			return true ;
		}

		return false;
	}
}
