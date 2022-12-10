package com.lp.client.angebotstkl.webabfrage;

import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.assistent.view.AssistentPageView;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;

public class WebabfragePage3View extends AssistentPageView {

	private static final long serialVersionUID = 3444923550142433227L;
	private WebabfragePage3Ctrl controller;
	private JProgressBar progressBar;
	private WrapperTextField importiertePositionen;

	public WebabfragePage3View(WebabfragePage3Ctrl controller, InternalFrame iFrame) {
		super(controller, iFrame);
		this.controller = controller;
	}

	@Override
	public void dataUpdated() {
		if (getController().getImportThrowable() != null) {
			getInternalFrame().handleException(getController().getImportThrowable(), true);
		}
		progressBar.setVisible(getController().isImporting());
		if (!getController().isImporting()) {
			importiertePositionen.setText(getController().getAnzahlImportierterPositionen() == null 
						? "?" : getController().getAnzahlImportierterPositionen().toString());
		}
	}

	@Override
	public WebabfragePage3Ctrl getController() {
		return controller;
	}

	@Override
	public String getTitle() {
		return LPMain.getTextRespectUISPr("agstkl.webabfrage.title.webabfrage");
	}

	@Override
	protected void initViewImpl() {
		progressBar = new JProgressBar(getController().getProgressModel());
		progressBar.setStringPainted(true);
		importiertePositionen = new WrapperTextField();
		importiertePositionen.setEditable(false);
		
		setLayout(new MigLayout("wrap 2, fill, hidemode 2", "[fill,30%|fill,70%]", "[30%|70%,fill, align top]"));
		
		add(progressBar, "span");
		add(new WrapperLabel(LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.importiertepositionen")));
		add(importiertePositionen);
	}

}
