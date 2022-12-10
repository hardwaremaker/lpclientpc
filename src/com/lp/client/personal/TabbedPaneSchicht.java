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
package com.lp.client.personal;

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
import com.lp.server.personal.service.SchichtDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class TabbedPaneSchicht extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQuerySchicht = null;
	private PanelBasis panelDetailSchicht = null;

	private PanelQuery panelQuerySchichtzeit = null;
	private PanelBasis panelBottomSchichtzeit = null;
	private PanelSplit panelSplitSchichtzeit = null;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_DETAIL = 1;
	private final static int IDX_PANEL_ZEITEN = 2;

	private SchichtDto schichtDto = null;

	public SchichtDto getSchichtDto() {
		return schichtDto;
	}

	public void setSchichtDto(SchichtDto schichtDto) {
		this.schichtDto = schichtDto;
	}

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneSchicht(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"pers.schicht"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryBereitschaft() {
		return panelQuerySchicht;
	}

	private void jbInit() throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'"
				+ LPMain.getInstance().getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
		panelQuerySchicht = new PanelQuery(null, kriterien,
				QueryParameters.UC_ID_SCHICHT, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"auft.title.panel.auswahl"), true);

		panelQuerySchicht.befuellePanelFilterkriterienDirekt(
				PersonalFilterFactory.getInstance()
						.createFKDBereitschaftartBezeichnung(), null);

		addTab(LPMain.getInstance().getTextRespectUISPr(
				"auft.title.panel.auswahl"), panelQuerySchicht);
		panelQuerySchicht.eventYouAreSelected(false);

		if ((Integer) panelQuerySchicht.getSelectedId() != null) {
			setSchichtDto(DelegateFactory
					.getInstance()
					.getSchichtDelegate()
					.schichtFindByPrimaryKey(
							(Integer) panelQuerySchicht.getSelectedId()));
		}

		// Detail
		panelDetailSchicht = new PanelSchicht(getInternalFrame(), LPMain
				.getInstance().getTextRespectUISPr("lp.detail"),
				panelQuerySchicht.getSelectedId());
		addTab(LPMain.getInstance().getTextRespectUISPr("lp.detail"),
				panelDetailSchicht);

		// Schichtzeit
		panelQuerySchichtzeit = new PanelQuery(null, PersonalFilterFactory
				.getInstance().createFKSchichtzeiten(
						(Integer) panelQuerySchicht.getSelectedId()),
				QueryParameters.UC_ID_SCHICHTZEITEN, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"pers.schichtzeiten"), true);
		panelBottomSchichtzeit = new PanelSchichtzeiten(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("pers.schichtzeiten"),
				null);

		panelSplitSchichtzeit = new PanelSplit(getInternalFrame(),
				panelBottomSchichtzeit, panelQuerySchichtzeit, 185);
		addTab(LPMain.getInstance().getTextRespectUISPr("pers.schichtzeiten"),
				panelSplitSchichtzeit);

		panelQuerySchicht.eventYouAreSelected(false);

		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		refreshTitle();
		this.addChangeListener(this);

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQuerySchicht.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("pers.schicht"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		if (getSchichtDto() != null) {
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getSchichtDto()
							.getCBez());
		}

	}

	public InternalFramePersonal getInternalFramePersonal() {
		return (InternalFramePersonal) getInternalFrame();
	}

	protected void lPActionEvent(ActionEvent e) {
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQuerySchicht) {
				Integer iId = (Integer) panelQuerySchicht.getSelectedId();
				if (iId != null) {
					setSelectedComponent(panelDetailSchicht);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomSchichtzeit) {
				panelSplitSchichtzeit.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomSchichtzeit) {
				panelQuerySchichtzeit.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}

		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

			if (e.getSource() == panelDetailSchicht) {
				panelQuerySchicht.clearDirektFilter();
				Object oKey = panelDetailSchicht.getKeyWhenDetailPanel();

				panelQuerySchicht.setSelectedId(oKey);
			}

			else if (e.getSource() == panelBottomSchichtzeit) {

				panelQuerySchichtzeit.setDefaultFilter(PersonalFilterFactory
						.getInstance().createFKSchichtzeiten(
								getInternalFramePersonal()
										.getTabbedPaneSchicht().getSchichtDto().getIId()));
				Object oKey = panelBottomSchichtzeit.getKeyWhenDetailPanel();

				panelQuerySchichtzeit.eventYouAreSelected(false);
				panelQuerySchichtzeit.setSelectedId(oKey);
				panelSplitSchichtzeit.eventYouAreSelected(false);

			}

		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQuerySchicht) {
				if (panelQuerySchicht.getSelectedId() != null) {
					getInternalFramePersonal().setKeyWasForLockMe(
							panelQuerySchicht.getSelectedId() + "");

					// Dto-setzen

					setSchichtDto(DelegateFactory
							.getInstance()
							.getSchichtDelegate()
							.schichtFindByPrimaryKey(
									(Integer) panelQuerySchicht.getSelectedId()));
					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE,
							getSchichtDto().getCBez());

					// Default- Filter vorbesetzten
					panelQuerySchichtzeit
							.setDefaultFilter(PersonalFilterFactory
									.getInstance().createFKSchichtzeiten(
											getInternalFramePersonal()
													.getTabbedPaneSchicht()
													.getSchichtDto().getIId()));
					if (panelQuerySchicht.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_AUSWAHL, false);
					} else {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_AUSWAHL, true);
					}

				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}

			} else if (e.getSource() == panelQuerySchichtzeit) {

				Integer iId = (Integer) panelQuerySchichtzeit.getSelectedId();
				panelBottomSchichtzeit.setKeyWhenDetailPanel(iId);
				panelBottomSchichtzeit.eventYouAreSelected(false);
				panelQuerySchichtzeit.updateButtons();
			}

		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailSchicht) {
				this.setSelectedComponent(panelQuerySchicht);
				setKeyWasForLockMe();
				panelQuerySchicht.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSchichtzeit) {
				setKeyWasForLockMe();
				if (panelBottomSchichtzeit.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQuerySchichtzeit
							.getId2SelectAfterDelete();
					panelQuerySchichtzeit.setSelectedId(oNaechster);
				}
				panelSplitSchichtzeit.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			super.lPEventItemChanged(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQuerySchicht) {
				if (panelQuerySchicht.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailSchicht.eventActionNew(null, true, false);
				setSelectedComponent(panelDetailSchicht);
			} else if (e.getSource() == panelQuerySchichtzeit) {
				panelBottomSchichtzeit.eventActionNew(e, true, false);
				panelBottomSchichtzeit.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitSchichtzeit);

			}

		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			panelQuerySchicht.eventYouAreSelected(false);
			if (panelQuerySchicht.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}
			panelQuerySchicht.updateButtons();
		} else if (selectedIndex == IDX_PANEL_DETAIL) {
			panelDetailSchicht.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_ZEITEN) {
			if (getInternalFramePersonal().getZeitmodellDto() != null) {
				panelQuerySchichtzeit.setDefaultFilter(PersonalFilterFactory
						.getInstance().createFKSchichtzeiten(
								getInternalFramePersonal()
										.getTabbedPaneSchicht().getSchichtDto()
										.getIId()));
			}

			panelSplitSchichtzeit.eventYouAreSelected(false);
			panelQuerySchichtzeit.updateButtons();
		}

		refreshTitle();

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);
		}
		return wrapperMenuBar;
	}

}
