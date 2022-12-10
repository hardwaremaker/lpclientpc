package com.lp.client.angebotstkl.webabfrage;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.AssistentPageController;
import com.lp.client.frame.component.InternalFrame;

public class WebabfragePage2Ctrl extends AssistentPageController {

	private WebabfrageBaseModel model;
	private WeblieferantenController weblieferantenController;
	private WebabfrageResultController webabfrageResultController;
	private FoundPartsController foundPartsController;
	
	public WebabfragePage2Ctrl(WebabfrageBaseModel model) {
		this.model = model;
	}

	@Override
	public boolean isNextAllowed() {
		return true;
	}

	@Override
	public boolean isPrevAllowed() {
		return false;
	}

	@Override
	public boolean isCancelAllowed() {
		return true;
	}

	@Override
	public void activateByNext() throws Throwable {
	}

	@Override
	public void activateByPrev() throws Throwable {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean nextPageIfPossible() throws ExceptionLP, Throwable {
		return true;
	}

	@Override
	public boolean prevPageIfPossible() throws ExceptionLP, Throwable {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelIfPossible() throws ExceptionLP, Throwable {
		return true;
	}

	protected WebabfrageBaseModel getModel() {
		return model;
	}

	public INeueLieferantenViewController getNeueLieferantenController(InternalFrame internalframe) {
		return getWeblieferantenController(internalframe);
	}
	
	public IBekannteLieferantenViewController getBekannteLieferantenController(InternalFrame internalframe) {
		return getWeblieferantenController(internalframe);
	}
	
	public IWebabfrageViewController getWebabfrageViewController() throws Throwable {
		return getWebabfrageResultController();
	}
	
	private WeblieferantenController getWeblieferantenController(InternalFrame internalframe) {
		try {
			if (weblieferantenController == null) {
				weblieferantenController = new WeblieferantenController(internalframe, model);
			}
			
			return weblieferantenController;
		} catch (ExceptionLP e) {
			internalframe.handleException(e, true);
		} catch (Throwable e) {
			internalframe.handleException(e, true);
		}
		return null;
	}
	
	private WebabfrageResultController getWebabfrageResultController() throws Throwable {
		if (webabfrageResultController == null) {
			webabfrageResultController = new WebabfrageResultController(model, getWeblieferantenController(null));
		}
		
		return webabfrageResultController;
	}

	public IFoundPartsViewController getFoundpartsViewController() {
		if (foundPartsController == null) {
			foundPartsController = new FoundPartsController(model);
		}
		
		return foundPartsController;
	}
	
}
