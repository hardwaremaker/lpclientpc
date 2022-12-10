package com.lp.client.frame.component.phone;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;

public class BrowserDialer extends PhoneDialer {

	@Override
	public void dial() throws ExceptionLP {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dial(String uri) throws ExceptionLP {
		
		try {
			URI dialUri = new URI(uri) ;
			java.awt.Desktop.getDesktop().browse(dialUri);
		} catch (URISyntaxException ex1) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),LPMain.getTextRespectUISPr("lp.fehlerhafteurl"));
		} catch (IOException ex1) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),ex1.getMessage());
		}
		
		
		
	}

}
