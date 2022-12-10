package com.lp.client.angebotstkl.webabfrage;

import com.lp.client.frame.assistent.AssistentPageController;
import com.lp.client.frame.assistent.view.AssistentPageView;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.pc.LPMain;

public class WebabfragePage1View extends AssistentPageView {

	private static final long serialVersionUID = -4056117266529325472L;
	private WebabfragePage1Ctrl controller;

	public WebabfragePage1View(AssistentPageController controller, InternalFrame iFrame) {
		super(controller, iFrame);
		this.controller = (WebabfragePage1Ctrl) controller;
	}

	@Override
	public void dataUpdated() {

	}

	@Override
	public WebabfragePage1Ctrl getController() {
		return controller;
	}

	@Override
	public String getTitle() {
		return LPMain.getTextRespectUISPr("agstkl.webabfrage.title.webabfragetypauswahl");
	}

	@Override
	protected void initViewImpl() {
		// TODO Auto-generated method stub

	}

}
