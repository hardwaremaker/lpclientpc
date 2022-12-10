
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
package com.lp.client.zeiterfassung;

import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

/**
 * <p>
 * &UUml;berschrift:
 * </p>
 * <p>
 * Beschreibung:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Organisation:
 * </p>
 * 
 * @author Christian Kollmann
 * @version $Revision: 1.2 $
 */

public class TabbedPaneAnwesenheitsbestaetigung extends TabbedPane {
	private PanelQuery panelQueryAnwesenheitsbestaetigung = null;
	private PanelBasis panelSplitAnwesenheitsbestaetigung = null;
	private PanelBasis panelBottomAnwesenheitsbestaetigung = null;

	private WrapperMenuBar wrapperMenuBar = null;

	private final static int IDX_PANEL_AUSWAHL = 0;

	public TabbedPaneAnwesenheitsbestaetigung(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("pers.title.tab.anwesenheitsbestaetigung"));

		jbInit();
		initComponents();
	}

	public InternalFrameZeiterfassung getInternalFramePersonal() {
		return (InternalFrameZeiterfassung) getInternalFrame();
	}

	private void jbInit() throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("flrpersonal.mandant_c_nr", true,
				"'" + LPMain.getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL, false);

		panelQueryAnwesenheitsbestaetigung = new PanelQuery(null, kriterien,
				QueryParameters.UC_ID_ANWESENHEITSBESTAETIGUNG, aWhichButtonIUse, getInternalFrame(),
				LPMain.getTextRespectUISPr("pers.title.tab.anwesenheitsbestaetigung"), true);
		panelQueryAnwesenheitsbestaetigung.eventYouAreSelected(false);

		panelBottomAnwesenheitsbestaetigung = new PanelAnwesenheitsbestaetigung(getInternalFrame(),
				LPMain.getTextRespectUISPr("pers.title.tab.anwesenheitsbestaetigung"), null);
		panelSplitAnwesenheitsbestaetigung = new PanelSplit(getInternalFrame(), panelBottomAnwesenheitsbestaetigung,
				panelQueryAnwesenheitsbestaetigung, 150);
		addTab(LPMain.getTextRespectUISPr("pers.title.tab.anwesenheitsbestaetigung"),
				panelSplitAnwesenheitsbestaetigung);

		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		this.addChangeListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryAnwesenheitsbestaetigung) {
				if (panelQueryAnwesenheitsbestaetigung.getSelectedId() != null) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();

					Integer iId = (Integer) panelQueryAnwesenheitsbestaetigung.getSelectedId();
					panelBottomAnwesenheitsbestaetigung.setKeyWhenDetailPanel(iId);
					panelBottomAnwesenheitsbestaetigung.eventYouAreSelected(false);
					getInternalFramePersonal().setKeyWasForLockMe(key + "");

					String sBezeichnung = "";
					getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sBezeichnung);

					if (panelQueryAnwesenheitsbestaetigung.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
					} else {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, true);
					}

				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryAnwesenheitsbestaetigung) {
				panelBottomAnwesenheitsbestaetigung.eventActionNew(e, true, false);
				panelBottomAnwesenheitsbestaetigung.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomAnwesenheitsbestaetigung) {
				panelSplitAnwesenheitsbestaetigung.eventYouAreSelected(false);
			}

		}

		else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomAnwesenheitsbestaetigung) {
				panelQueryAnwesenheitsbestaetigung.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}

		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomAnwesenheitsbestaetigung) {
				Object oKey = panelBottomAnwesenheitsbestaetigung.getKeyWhenDetailPanel();
				panelQueryAnwesenheitsbestaetigung.eventYouAreSelected(false);
				panelQueryAnwesenheitsbestaetigung.setSelectedId(oKey);
				panelSplitAnwesenheitsbestaetigung.eventYouAreSelected(false);
				panelQueryAnwesenheitsbestaetigung.updateButtons();

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomAnwesenheitsbestaetigung) {
				getInternalFrame().enableAllPanelsExcept(true);

				setKeyWasForLockMe();
				panelQueryAnwesenheitsbestaetigung.eventYouAreSelected(false);
			}

		}

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryAnwesenheitsbestaetigung.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getTextRespectUISPr("pers.zeiterfassung.diaeten"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		if (getInternalFramePersonal().getDiaetenDto() != null) {
			getInternalFrame().setLpTitle(3, getInternalFramePersonal().getDiaetenDto().getCBez());
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();
		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			panelQueryAnwesenheitsbestaetigung.eventYouAreSelected(false);
			if (panelQueryAnwesenheitsbestaetigung.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
			}

			panelQueryAnwesenheitsbestaetigung.updateButtons();
			panelSplitAnwesenheitsbestaetigung.eventYouAreSelected(false);
		}

		refreshTitle();
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {

	}

	protected JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);
		}
		return wrapperMenuBar;

	}

}
