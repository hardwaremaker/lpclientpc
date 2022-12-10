
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
package com.lp.client.system;

import java.awt.event.ActionEvent;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.fastlanereader.generated.service.FLRHvmaparameterPK;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

/**
 * <p>
 * Diese Klasse kuemmert sich um Parameter.
 * </p>
 *
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 *
 * <p>
 * Erstellungsdatum <I>01. 06. 05</I>
 * </p>
 *
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class TabbedPaneHVMAParameter extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelSplit panelSplitHVMAParameter = null;
	private PanelQuery panelQueryHVMAParameter = null;

	public PanelQuery getPanelQueryParametermandant() {
		return panelQueryHVMAParameter;
	}

	private PanelHVMAParameter panelBottomHVMAParameter = null;

	// Reihenfolge der Panels
	public static final int IDX_PANEL_HVMAPARAMETER = 0;

	public TabbedPaneHVMAParameter(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("lp.hvmaparameter"));
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		insertTab(LPMain.getTextRespectUISPr("lp.hvmaparameter"), null, null,
				LPMain.getTextRespectUISPr("lp.hvmaparameter"), IDX_PANEL_HVMAPARAMETER);

		refreshPanelHVMAParameter();
		setSelectedComponent(panelSplitHVMAParameter);
		panelQueryHVMAParameter.eventYouAreSelected(false);

		// wenn es keine Parametermandant gibt, die fix verdrahteten Werte fuer
		// den aktuellen Mandanten anlegen
		if (panelQueryHVMAParameter.getSelectedId() == null) {
			DelegateFactory.getInstance().getParameterDelegate().createFixverdrahteteParametermandant();

			panelQueryHVMAParameter.eventYouAreSelected(false);
		}

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	protected void lPActionEvent(ActionEvent e) {
		// nothing here.
	}

	/**
	 * changed: hier wird alles durchlaufen und abgefragt zb. save event, discard
	 * event, wenn ein panel refresht werden soll...
	 * 
	 * @param eI ItemChangedEvent
	 * @throws Throwable
	 */
	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryHVMAParameter) {
				FLRHvmaparameterPK pk = (FLRHvmaparameterPK) panelQueryHVMAParameter.getSelectedId();

				if (pk != null) {
					getInternalFrame().setKeyWasForLockMe(pk.getC_nr() + pk.getC_kategorie());
				}

				panelBottomHVMAParameter.setKeyWhenDetailPanel(pk);
				panelBottomHVMAParameter.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				this.panelQueryHVMAParameter.updateButtons(panelBottomHVMAParameter.getLockedstateDetailMainKey());

			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == this.panelBottomHVMAParameter) {
				// im QP die Buttons in den Zustand neu setzen.
				this.panelQueryHVMAParameter.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			// wir landen hier bei der Abfolge Button Aendern -> xx -> Button
			// Discard
			if (e.getSource() == panelBottomHVMAParameter) {
				panelSplitHVMAParameter.eventYouAreSelected(false);
			}
		}

		// selektiert nach save: 0 Wir landen hier nach Button Save
		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomHVMAParameter) {
				Object oKey = panelBottomHVMAParameter.getKeyWhenDetailPanel();
				panelQueryHVMAParameter.eventYouAreSelected(false);
				panelQueryHVMAParameter.setSelectedId(oKey);
				panelSplitHVMAParameter.eventYouAreSelected(false);
			}
		}

		// wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomHVMAParameter) {
				panelSplitHVMAParameter.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelQueryHVMAParameter) {
				if (panelQueryHVMAParameter.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBottomHVMAParameter.eventActionNew(eI, true, false);
				panelBottomHVMAParameter.eventYouAreSelected(false);
				setSelectedComponent(panelSplitHVMAParameter);
			}
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_HVMAPARAMETER:
			refreshPanelHVMAParameter();
			panelQueryHVMAParameter.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelQueryHVMAParameter.updateButtons();

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelQueryHVMAParameter.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_HVMAPARAMETER, false);
			}
			break;
		}

	}

	private void refreshPanelHVMAParameter() throws Throwable {

		if (panelSplitHVMAParameter == null) {
			String[] aWhichStandardButtonIUse = null;

			
			
			
			panelQueryHVMAParameter = new PanelQuery(null, null,
					QueryParameters.UC_ID_HVMAPARAMETER, aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.hvmaparameter"), true);

			panelQueryHVMAParameter.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDIdCompCNr(),
					SystemFilterFactory.getInstance().createFKDIdCompCKategorie());

			panelBottomHVMAParameter = new PanelHVMAParameter(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.hvmaparameter"), null);

			panelSplitHVMAParameter = new PanelSplit(getInternalFrame(), panelBottomHVMAParameter,
					panelQueryHVMAParameter, 260);

			setComponentAt(IDX_PANEL_HVMAPARAMETER, panelSplitHVMAParameter);
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

}