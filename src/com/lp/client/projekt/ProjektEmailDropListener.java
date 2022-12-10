package com.lp.client.projekt;

import java.nio.file.Path;

import javax.swing.JButton;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.util.EmailDropHandler;
import com.lp.client.util.EmailParser;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class ProjektEmailDropListener extends EmailDropHandler {
	private final TabbedPaneProjekt tabbedPane;
	private final PanelBasis panelQuery;

	public ProjektEmailDropListener(TabbedPaneProjekt tabbedPane, PanelBasis projektAuswahl) {
		this.tabbedPane = tabbedPane;
		this.panelQuery = projektAuswahl;
	}


	@Override
	protected void emailDropped(EmailParser email, Path pathToMail) {
		JButton btn = panelQuery.getToolBar().getButton(PanelBasis.ACTION_NEW);
		if (btn.isEnabled()) {
			btn.doClick();
		}
		PanelBasis panel = (PanelBasis) tabbedPane.getSelectedComponent();
		if (panel instanceof PanelProjektKopfdaten) {
			PanelProjektKopfdaten projPanel = (PanelProjektKopfdaten) panel;
			projPanel.setDefaultsAusEmail(email);
			projPanel.doOnSave(() -> doAddMailToDokAbl(tabbedPane.getProjektDto(), pathToMail, email));
		}

	}

	private void doAddMailToDokAbl(ProjektDto projekt, Path mailFile, EmailParser parsedMail) {
		try {
			super.addMailToDokumentenablage(projekt.getIId(), QueryParameters.UC_ID_PROJEKT, projekt.getIId().toString(), mailFile);
		} catch (Throwable e) {
			myLogger.error("Fehler beim Hochladen in die Dokumentenablage", e);
		}
	}
}
