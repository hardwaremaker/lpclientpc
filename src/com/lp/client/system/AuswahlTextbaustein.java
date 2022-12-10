package com.lp.client.system;

import java.util.Arrays;
import java.util.EventObject;

import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.editor.DialogTextbausteine;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MediastandardDto;

public class AuswahlTextbaustein implements ItemChangedListener {

	private MediastandardDto chosenTextbaustein;
	private InternalFrame internalFrame;
	private PanelQueryFLR panelQueryFLRTextbaustein;
	
	public AuswahlTextbaustein(InternalFrame internalFrame) {
		this.internalFrame = internalFrame;
	}
	
	public void choose() throws Throwable {
		
		if (internalFrame != null) {
			internalFrame.addItemChangedListener(this);
			openFLRDialog();
			return;
		}
		
		// Fallback auf bisherigen Dialog, falls internalFrame nicht gesetzt
		DialogTextbausteine dialog = new DialogTextbausteine();
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(dialog);
		dialog.setVisible(true);
		
		Integer textbausteinMediaStdId = dialog.mediastandardIId;
		dialog.dispose();
		
		setChosenTextbaustein(textbausteinMediaStdId);
	}

	private void setChosenTextbaustein(Integer mediastandardIId) {
		chosenTextbaustein = null;
		if (mediastandardIId == null) {
			return;
		}
		
		try {
			chosenTextbaustein = loadTextbaustein(mediastandardIId);
		} catch (Throwable e) {
			new DialogError(LPMain.getInstance().getDesktop(), e, DialogError.TYPE_ERROR);
		}
		
	}
	private MediastandardDto loadTextbaustein(Integer mediastandardIId) throws ExceptionLP, Throwable {
		MediastandardDto mediastandardDto = DelegateFactory.getInstance()
				.getMediaDelegate().mediastandardFindByPrimaryKey(mediastandardIId);
		return mediastandardDto;
	}
	
	public boolean hasValidTextbaustein() {
		return getChosenTextbaustein() != null;
	}
	
	public MediastandardDto getChosenTextbaustein() {
		return chosenTextbaustein;
	}
	
	public String getTextbausteinText() {
		if (!hasValidTextbaustein()) return null;
		
		return getChosenTextbaustein().getOMediaText();
	}
	
	private void openFLRDialog() throws Throwable {
		if (panelQueryFLRTextbaustein == null) {
			panelQueryFLRTextbaustein = SystemFilterFactory.getInstance()
					.createPanelFLRMediastandard(internalFrame, 
							Arrays.asList(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML));
			panelQueryFLRTextbaustein.setAdd2Title(LPMain.getTextRespectUISPr("lp.mediastandard.auswahl.text"));
		}
		new DialogQuery(panelQueryFLRTextbaustein);
	}

	@Override
	public void changed(EventObject eI) {
	    ItemChangedEvent e = (ItemChangedEvent) eI;

	    if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL
	    		&& e.getSource() == panelQueryFLRTextbaustein) {
	        Object oKey = ( (ISourceEvent) e.getSource()).getIdSelected();
	        if (oKey != null) {
	        	setChosenTextbaustein((Integer)oKey);
	        }
	    }
	}
}
