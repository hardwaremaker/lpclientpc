package com.lp.client.projekt;

import java.nio.file.Path;

import javax.swing.JButton;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.util.EmailDropHandler;
import com.lp.client.util.EmailParser;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class ProjektDetailEmailDropListener extends EmailDropHandler {
	private final PanelQuery panelQueryHistory;
	private final PanelProjektHistory panelDetails;

	public ProjektDetailEmailDropListener(PanelQuery panelQueryHistory, PanelProjektHistory panelHistory) {
		this.panelQueryHistory = panelQueryHistory;
		this.panelDetails = panelHistory;
	}

	private void doAddMailToDokAbl(Object key, Path mailFile, EmailParser parsedMail) {
		try {
			super.addMailToDokumentenablage(key, QueryParameters.UC_ID_HISTORY, key.toString(), mailFile);
		} catch (Throwable e) {
			myLogger.error("Fehler beim Hochladen in die Dokumentenablage", e);
		}
	}

	@Override
	protected void emailDropped(EmailParser email, Path pathToMail) {
		JButton btn = panelQueryHistory.getToolBar().getButton(PanelBasis.ACTION_NEW);
		if (btn.isEnabled()) {
			btn.doClick();
			panelDetails.setFromEmail(email);
			panelDetails.doOnSave(() -> doAddMailToDokAbl(panelQueryHistory.getIdSelected(), pathToMail, email));
		}
	}

}
