package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.util.GTINGenerierungAlleArtikelnummernVergebenException;

public class GTINGenerierungAlleArtikelnummernVergebenError implements IErrorAction {

	@Override
	public String getMsg(ExceptionLP exception) {
		if (exception.getExceptionData() instanceof GTINGenerierungAlleArtikelnummernVergebenException) {
			GTINGenerierungAlleArtikelnummernVergebenException ex = 
					(GTINGenerierungAlleArtikelnummernVergebenException) exception.getExceptionData();
			
			return LPMain.getMessageTextRespectUISPr("artikel.gtingenerierung.allenummernvergeben", 
					ex.getHighestItemReference(), ex.getCompanyPrefix());
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel,
			ExceptionLP exception) {
		return false;
	}

}
