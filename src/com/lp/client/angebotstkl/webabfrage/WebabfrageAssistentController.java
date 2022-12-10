package com.lp.client.angebotstkl.webabfrage;

import java.util.List;

import com.lp.client.angebotstkl.InternalFrameAngebotstkl;
import com.lp.client.frame.assistent.AssistentController;
import com.lp.server.angebotstkl.service.WebabfragepositionDto;

public class WebabfrageAssistentController extends AssistentController {

	public WebabfrageAssistentController(InternalFrameAngebotstkl iFrame, List<WebabfragepositionDto> positionen) throws Throwable {
		WebabfrageBaseModel model = new WebabfrageBaseModel(iFrame.getEinkaufsangebotDto(), positionen);
		
		//Auswahl des Webabfrage-Typs
		WebabfragePage1Ctrl ctrl1 = new WebabfragePage1Ctrl(model, this);
		registerPage(new WebabfragePage1View(ctrl1, iFrame));
		
		//Webabfrage selbst
		WebabfragePage2Ctrl ctrl2 = new WebabfragePage2Ctrl(model);
		registerPage(new WebabfragePage2View(ctrl2, iFrame));
		
//		//Import
		WebabfragePage3Ctrl ctrl3 = new WebabfragePage3Ctrl(model);
		registerPage(new WebabfragePage3View(ctrl3, iFrame));
		
		//for now: zurzeit existiert nur WebabfrageTyp FindChips
		gotoNextPage();
	}

}
