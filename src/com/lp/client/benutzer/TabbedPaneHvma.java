
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
package com.lp.client.benutzer;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.ReportWarenentnahmestatistik;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.SystemrolleDto;
import com.lp.server.personal.service.HvmaFac;
import com.lp.server.personal.service.HvmalizenzDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p>&UUml;berschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.9 $
 */
public class TabbedPaneHvma extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryHvmalizenz = null;
	private PanelQuery panelQueryHvmabenutzer = null;
	public PanelQuery getPanelQueryHvmabenutzer() {
		return panelQueryHvmabenutzer;
	}

	private PanelBasis panelSplitHvmabenutzer = null;
	private PanelBasis panelDetailHvmabenutzer = null;

	private PanelQuery panelQueryHvmabenutzerparameter = null;
	private PanelBasis panelSplitHvmabenutzerparameter = null;
	private PanelBasis panelDetailHvmabenutzerparameter = null;

	private WrapperMenuBar wrapperMenuBar = null;

	private static int IDX_PANEL_AUSWAHL = -1;
	private static int IDX_PANEL_BENUTZER = -1;
	private static int IDX_PANEL_PARAMETER = -1;

	public TabbedPaneHvma(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr("pers.hvma"));

		jbInit();
		initComponents();
	}

	private HvmalizenzDto hvmalizenzDto = null;

	public HvmalizenzDto getHvmalizenzDto() {
		return hvmalizenzDto;
	}

	private void jbInit() throws Throwable {

		int tabIndex = 0;
		IDX_PANEL_AUSWAHL = tabIndex;

		// 1 Materialauswahlliste
		String[] aWhichButtonIUse = {};
		panelQueryHvmalizenz = new PanelQuery(null, null, QueryParameters.UC_ID_HVMALIZENZ, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("pers.hvmalizenz"), true);

		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.hvmalizenz"), null, panelQueryHvmalizenz,
				LPMain.getInstance().getTextRespectUISPr("pers.hvmalizenz"), IDX_PANEL_AUSWAHL);
		panelQueryHvmalizenz.eventYouAreSelected(false);

		if ((Integer) panelQueryHvmalizenz.getSelectedId() != null) {

			hvmalizenzDto = DelegateFactory.getInstance().getHvmaDelegate()
					.hvmalizenzFindByPrimaryKey((Integer) panelQueryHvmalizenz.getSelectedId());

		}

		String[] aWhichButtonIUse2 = { PanelBasis.ACTION_NEW };
		// 3 Rechte
		panelQueryHvmabenutzer = new PanelQuery(null, null, QueryParameters.UC_ID_HVMABENUTZER, aWhichButtonIUse2,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("pers.hvmabenutzer"), true);

		panelQueryHvmabenutzer
				.befuellePanelFilterkriterienDirekt(new FilterKriteriumDirekt("flrbenutzer.c_benutzerkennung", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance().getTextRespectUISPr("pers.hvmabenutzer"),
						FilterKriteriumDirekt.PROZENT_TRAILING, true, true, Facade.MAX_UNBESCHRAENKT), null);

		panelDetailHvmabenutzer = new PanelHvmabenutzer(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("pers.hvmabenutzer"), null);

		panelSplitHvmabenutzer = new PanelSplit(getInternalFrame(), panelDetailHvmabenutzer, panelQueryHvmabenutzer,
				300);

		tabIndex++;
		IDX_PANEL_BENUTZER = tabIndex;

		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.hvmabenutzer"), null, panelSplitHvmabenutzer,
				LPMain.getInstance().getTextRespectUISPr("pers.hvmabenutzer"), IDX_PANEL_BENUTZER);

		// Parameter
		panelQueryHvmabenutzerparameter = new PanelQuery(null, null, QueryParameters.UC_ID_HVMABENUTZERPARAMETER,
				aWhichButtonIUse2, getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("lp.parameter"), true);

		panelDetailHvmabenutzerparameter = new PanelHvmabenutzerParameter(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("lp.parameter"), null);

		panelSplitHvmabenutzerparameter = new PanelSplit(getInternalFrame(), panelDetailHvmabenutzerparameter,
				panelQueryHvmabenutzerparameter, 270);

		tabIndex++;
		IDX_PANEL_PARAMETER = tabIndex;

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.parameter"), null, panelSplitHvmabenutzerparameter,
				LPMain.getInstance().getTextRespectUISPr("lp.parameter"), IDX_PANEL_PARAMETER);

		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		refreshTitle();
		this.addChangeListener(this);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryHvmalizenz) {
				setSelectedComponent(panelSplitHvmabenutzer);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelDetailHvmabenutzer) {
				panelDetailHvmabenutzer.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailHvmabenutzerparameter) {
				panelDetailHvmabenutzerparameter.eventYouAreSelected(false);
			}

		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelDetailHvmabenutzer) {
				Object oKey = panelDetailHvmabenutzer.getKeyWhenDetailPanel();
				panelQueryHvmabenutzer.eventYouAreSelected(false);
				panelQueryHvmabenutzer.setSelectedId(oKey);
				panelSplitHvmabenutzer.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailHvmabenutzerparameter) {
				Object oKey = panelDetailHvmabenutzerparameter.getKeyWhenDetailPanel();
				panelQueryHvmabenutzerparameter.eventYouAreSelected(false);
				panelQueryHvmabenutzerparameter.setSelectedId(oKey);
				panelSplitHvmabenutzerparameter.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelDetailHvmabenutzer) {
				panelQueryHvmabenutzer.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelDetailHvmabenutzerparameter) {
				panelQueryHvmabenutzerparameter.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryHvmabenutzer) {
				Integer iId = (Integer) panelQueryHvmabenutzer.getSelectedId();
				panelDetailHvmabenutzer.setKeyWhenDetailPanel(iId);
				panelDetailHvmabenutzer.eventYouAreSelected(false);
				panelQueryHvmabenutzer.updateButtons();
			} else if (e.getSource() == panelQueryHvmalizenz) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				getInternalFrameBenutzer().setKeyWasForLockMe(panelQueryHvmalizenz.getSelectedId() + "");

				hvmalizenzDto = DelegateFactory.getInstance().getHvmaDelegate()
						.hvmalizenzFindByPrimaryKey((Integer) key);

				getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
						LPMain.getInstance().getTextRespectUISPr("pers.hvma") + ": " + hvmalizenzDto.getCNr());
				
				
				panelQueryHvmabenutzer.setDefaultFilter(BenutzerFilterFactory.getInstance()
						.createFKHvmabenutzer((Integer) panelQueryHvmalizenz.getSelectedId()));

				panelSplitHvmabenutzer.eventYouAreSelected(false);
				
			} else if (e.getSource() == panelQueryHvmabenutzerparameter) {
				Integer iId = (Integer) panelQueryHvmabenutzerparameter.getSelectedId();
				panelDetailHvmabenutzerparameter.setKeyWhenDetailPanel(iId);
				panelDetailHvmabenutzerparameter.eventYouAreSelected(false);
				panelQueryHvmabenutzerparameter.updateButtons();
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			super.lPEventItemChanged(e);
			refreshTitle();
		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailHvmabenutzer) {
				setKeyWasForLockMe();
				if (panelDetailHvmabenutzer.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryHvmabenutzer.getId2SelectAfterDelete();
					panelQueryHvmabenutzer.setSelectedId(oNaechster);
				}

				panelSplitHvmabenutzer.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailHvmabenutzerparameter) {
				setKeyWasForLockMe();
				if (panelDetailHvmabenutzerparameter.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryHvmabenutzerparameter.getId2SelectAfterDelete();
					panelQueryHvmabenutzerparameter.setSelectedId(oNaechster);
				}

				panelSplitHvmabenutzerparameter.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryHvmabenutzer) {
				panelDetailHvmabenutzer.eventActionNew(e, true, false);
				panelDetailHvmabenutzer.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitHvmabenutzer);
			} else if (e.getSource() == panelQueryHvmabenutzerparameter) {
				panelDetailHvmabenutzerparameter.eventActionNew(e, true, false);
				panelDetailHvmabenutzerparameter.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitHvmabenutzerparameter);
			}
		}

	}

	public InternalFrameBenutzer getInternalFrameBenutzer() {
		return (InternalFrameBenutzer) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryHvmalizenz.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("lp.systemrolle"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		if (getInternalFrameBenutzer().getSystemrolleDto() != null) {

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameBenutzer().getSystemrolleDto().getCBez());
		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			panelQueryHvmalizenz.eventYouAreSelected(false);
			panelQueryHvmalizenz.updateButtons();
		} else if (selectedIndex == IDX_PANEL_BENUTZER) {
			if (panelQueryHvmalizenz.getSelectedId() != null) {
				panelQueryHvmabenutzer.setDefaultFilter(BenutzerFilterFactory.getInstance()
						.createFKHvmabenutzer((Integer) panelQueryHvmalizenz.getSelectedId()));

				panelSplitHvmabenutzer.eventYouAreSelected(false);
				panelQueryHvmabenutzer.updateButtons();
			}
		} else if (selectedIndex == IDX_PANEL_PARAMETER) {
			if (panelQueryHvmabenutzer.getSelectedId() != null) {
				panelQueryHvmabenutzerparameter.setDefaultFilter(BenutzerFilterFactory.getInstance()
						.createFKHvmabenutzerparameter((Integer) panelQueryHvmabenutzer.getSelectedId()));

				panelSplitHvmabenutzerparameter.eventYouAreSelected(false);
				panelQueryHvmabenutzerparameter.updateButtons();
			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
						LPMain.getTextRespectUISPr("lp.hvmaparameter.benutzerauswaehlen"));

				setSelectedIndex(IDX_PANEL_BENUTZER);
				lPEventObjectChanged(e);
			}
		}

		refreshTitle();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);

			JMenu journal = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_JOURNAL);

		}
		return wrapperMenuBar;
	}

}
