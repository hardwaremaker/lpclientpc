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
package com.lp.client.angebotstkl;

import java.awt.Component;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.IWebpartnerDto;
import com.lp.server.angebotstkl.service.WebFindChipsDto;

public class PanelWebFindChips extends PanelWebpartner {

	private static final long serialVersionUID = -1555245287846518271L;
	
	private WrapperLabel wlDistributorID;
	private WrapperTextField wtfDistributorID;
	private WrapperLabel wlDistributorName;
	private WrapperTextField wtfDistributorName;

	public PanelWebFindChips(InternalFrame internalFrameI, String addTitleI,
			Object keyWhenDetailPanelI) throws Throwable {
		super(internalFrameI, addTitleI, keyWhenDetailPanelI);
	}

	@Override
	protected IWebpartnerDto getNewWebpartnerDto() {
		return new WebFindChipsDto();
	}

	@Override
	protected String getLockMeWerImpl() {
		return HelperClient.LOCKME_WEBFINDCHIPS;
	}

	@Override
	protected void dto2Components() throws Throwable {
		super.dto2Components();
		WebFindChipsDto dto = (WebFindChipsDto)getWebpartnerDto();
		wtfDistributorID.setText(dto.getcDistributor());
		wtfDistributorName.setText(dto.getcName());
	}

	@Override
	protected Component[][] getDetailComponents() {
		wlDistributorID = new WrapperLabel(LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.distributorid"));
		wtfDistributorID = new WrapperTextField();
		wtfDistributorID.setActivatable(false);
		wlDistributorName = new WrapperLabel(LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.distributorname"));
		wtfDistributorName = new WrapperTextField();
		wtfDistributorName.setActivatable(false);
		
		Component[][] components = new Component[2][2];
		components[0][0] = wlDistributorID;
		components[0][1] = wtfDistributorID;
		components[1][0] = wlDistributorName;
		components[1][1] = wtfDistributorName;
		
		return components;
	}
	
}
