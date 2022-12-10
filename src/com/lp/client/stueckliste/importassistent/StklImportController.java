/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
 package com.lp.client.stueckliste.importassistent;

import com.lp.client.frame.assistent.AssistentController;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.service.StklImportSpezifikation;

public class StklImportController extends AssistentController{
	
	private StklImportModel model;
	
	public StklImportController(int stklIId, int stklTyp, InternalFrame iFrame) throws Throwable {
		initModel(stklIId, stklTyp);
		
		String pageTitle = getTitle(stklTyp);
		StklImportPage1Ctrl ctrl1 = new StklImportPage1Ctrl(model);
		StklImportPage1View view1 = new StklImportPage1View(ctrl1, iFrame);
		view1.setTitle(pageTitle);
		registerPage(view1);
		
		StklImportPage2Ctrl ctrl2 = new StklImportPage2Ctrl(model);
		StklImportPage2View view2 = new StklImportPage2View(ctrl2, iFrame);
		view2.setTitle(pageTitle);
		registerPage(view2);
		
		StklImportPage3View view3;
		if(model.getSelectedSpezifikation().isStuecklisteMitBezugVerkauf()) {
			StklImportPage3CtrlVerkauf ctrl3 = new StklImportPage3CtrlVerkauf(model);
			view3 = new StklImportPage3ViewVerkauf(ctrl3, iFrame);
		} else {
			StklImportPage3CtrlEinkauf ctrl3 = new StklImportPage3CtrlEinkauf(model);
			view3 = new StklImportPage3ViewEinkauf(ctrl3, iFrame);
		}
		view3.setTitle(pageTitle);
		registerPage(view3);
		
		StklImportPage4Ctrl ctrl4 = new StklImportPage4Ctrl(model);
		StklImportPage4View view4 = new StklImportPage4View(ctrl4, iFrame);
		view4.setTitle(pageTitle);
		registerPage(view4);
	}
	
	private void initModel(int stklIId, int stklTyp) throws Throwable {
		Integer bezugsobjektIId = null;
		FileChooserConfigToken configToken = null;
		
		if(stklTyp == StklImportSpezifikation.SpezifikationsTyp.ANGEBOTSSTKL_SPEZ) {
			AgstklDto agstklDto = DelegateFactory.getInstance().getAngebotstklDelegate()
					.agstklFindByPrimaryKey(stklIId);
			bezugsobjektIId = agstklDto.getKundeIId();
			configToken = FileChooserConfigToken.IntelligenterImportAgstkl;
		
		} else if(stklTyp == StklImportSpezifikation.SpezifikationsTyp.EINKAUFSANGEBOTSSTKL_SPEZ) {
			EinkaufsangebotDto einkaufsAgDto = DelegateFactory.getInstance().getAngebotstklDelegate()
					.einkaufsangebotFindByPrimaryKey(stklIId);
			bezugsobjektIId = einkaufsAgDto.getKundeIId();
			configToken = FileChooserConfigToken.IntelligenterImportEinkaufsangebot;
		
		} else if(stklTyp == StklImportSpezifikation.SpezifikationsTyp.FERTIGUNGSSTKL_SPEZ) {
			StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
					.stuecklisteFindByPrimaryKey(stklIId);
			bezugsobjektIId = stklDto.getPartnerIId();
			configToken = FileChooserConfigToken.IntelligenterImportStkl;

		} else if(stklTyp == StklImportSpezifikation.SpezifikationsTyp.BESTELLUNGSTKL_SPEZ) {
			BestellungDto bestDto = DelegateFactory.getInstance().getBestellungDelegate()
					.bestellungFindByPrimaryKey(stklIId);
			bezugsobjektIId = bestDto.getLieferantIIdBestelladresse();
			configToken = FileChooserConfigToken.IntelligenterImportBestellung;
		}
		
		model = new StklImportModel(stklTyp, stklIId, bezugsobjektIId);
		model.setChooserConfigToken(configToken);
	}

	public String getTitle(int stklTyp) {
		if (StklImportSpezifikation.SpezifikationsTyp.ANGEBOTSSTKL_SPEZ == stklTyp)
			return LPMain.getTextRespectUISPr("agstkl.intelligenteragstklimport");

		if (StklImportSpezifikation.SpezifikationsTyp.BESTELLUNGSTKL_SPEZ == stklTyp)
			return LPMain.getTextRespectUISPr("bs.intelligenterbestellungenimport");

		if (StklImportSpezifikation.SpezifikationsTyp.EINKAUFSANGEBOTSSTKL_SPEZ == stklTyp)
			return LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.intelligentereinkaufsangebotimport");

		return LPMain.getTextRespectUISPr("stkl.intelligenterstklimport");
	}
	
}
