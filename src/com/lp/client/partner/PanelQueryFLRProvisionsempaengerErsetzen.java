package com.lp.client.partner;

/******************************************************************************
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

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import com.lp.client.frame.component.*;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

public class PanelQueryFLRProvisionsempaengerErsetzen extends PanelQueryFLR {

	private String ERSETZEN = "ERSETZEN";

	private PanelQueryFLR panelQueryFLRPersonal;

	public PanelQueryFLR getPanelQueryFLRPersonal() {
		return panelQueryFLRPersonal;
	}

	private static final long serialVersionUID = 1L;

	public PanelQueryFLRProvisionsempaengerErsetzen(QueryType[] typesI,
			FilterKriterium[] filtersI, int idUsecaseI,
			String[] aWhichButtonIUseI, InternalFrame internalFrameI,
			String add2TitleI) throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI,
				add2TitleI);

		jbInit();
	}

	private void jbInit() throws Exception {
		createAndSaveAndShowButton(
				"/com/lp/client/res/worker16x16.png",
				LPMain.getTextRespectUISPr("kunde.pflege.provisionsempfaenger.ersetzen"),
				ERSETZEN, null);
		getInternalFrame().addItemChangedListener(this);
	}

	@Override
	public void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {

		super.eventActionRefresh(e, bNeedNoRefreshI);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRPersonal) {
				int i = 0;
			}
		}

	}

	@Override
	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		super.eventActionSpecial(e);

		panelQueryFLRPersonal = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonal(getInternalFrame(), true, true);
		panelQueryFLRPersonal.setFireItemChangedEventAfterChange(true);

		getInternalFrame().addItemChangedListener(panelQueryFLRPersonal);

		new DialogQuery(panelQueryFLRPersonal);
	}

	public void ersetzen(Integer personalIIdNeu) throws Throwable {
		
		
		DelegateFactory.getInstance().getKundeDelegate()
				.provisionsempfaengerErsetzen(getSelectedIdsAsInteger(), personalIIdNeu);
		
		eventActionRefresh(null, true);

	}

}
