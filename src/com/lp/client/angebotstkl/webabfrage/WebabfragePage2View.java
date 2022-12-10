package com.lp.client.angebotstkl.webabfrage;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.assistent.AssistentPageController;
import com.lp.client.frame.assistent.view.AssistentPageView;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.pc.LPMain;

public class WebabfragePage2View extends AssistentPageView {

	private static final long serialVersionUID = 6020432963223254394L;
	private WebabfragePage2Ctrl controller;

	private PanelWebabfrageEinkaufsangebot panelWebabfrage;
	
	public WebabfragePage2View(AssistentPageController controller, InternalFrame iFrame) {
		super(controller, iFrame);
		this.controller = (WebabfragePage2Ctrl) controller;
	}

	@Override
	public void dataUpdated() {

	}

	@Override
	public WebabfragePage2Ctrl getController() {
		return controller;
	}

	@Override
	public String getTitle() {
		return LPMain.getTextRespectUISPr("agstkl.webabfrage.title.webabfrage");
	}

	@Override
	protected void initViewImpl() {
		try {
			panelWebabfrage = new PanelWebabfrageEinkaufsangebot(
					getInternalFrame(),
					getController().getBekannteLieferantenController(getInternalFrame()),
					getController().getNeueLieferantenController(getInternalFrame()),
					getController().getWebabfrageViewController(),
					getController().getFoundpartsViewController());
		} catch (Throwable t) {
			getInternalFrame().handleException(t, true);
		}
		setLayout(new MigLayout("", "[fill,grow]", "[fill,grow]"));
		add(panelWebabfrage);
	}

}
