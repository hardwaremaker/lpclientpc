package com.lp.client.angebotstkl;
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

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.InternalFramePersonal;
import com.lp.client.personal.PanelFahrzeug;
import com.lp.client.personal.PanelFahrzeugkosten;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.zeiterfassung.ReportZeiterfassungZeitdaten;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class TabbedPaneEkgruppe extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryEkgruppe = null;
	public PanelQuery getPanelQueryEkgruppe() {
		return panelQueryEkgruppe;
	}

	private PanelBasis panelDetailEkgruppe = null;

	private PanelQuery panelQueryLieferant = null;
	private PanelBasis panelSplitLieferant = null;
	private PanelBasis panelDetailLieferant = null;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_DETAIL = 1;
	private final static int IDX_PANEL_LIEFERANT = 2;
	

	public TabbedPaneEkgruppe(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("agstkl.ekgruppe"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryMaterial() {
		return panelQueryEkgruppe;
	}

	private void createAuswahl() throws Throwable {
		if (panelQueryEkgruppe == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryEkgruppe = new PanelQuery(null,SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_EKGRUPPE, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.auswahl"), true);

			panelQueryEkgruppe.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryEkgruppe);

		}
	}

	private void createDetail(Integer key) throws Throwable {
		if (panelDetailEkgruppe == null) {
			panelDetailEkgruppe = new PanelEkgruppe(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.detail"), key);
			setComponentAt(IDX_PANEL_DETAIL, panelDetailEkgruppe);
		}
	}

	private void createLieferant(Integer key) throws Throwable {

		if (panelQueryLieferant == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = AngebotstklFilterFactory
					.getInstance().createFKEkgruppe(key);

			panelQueryLieferant = new PanelQuery(null, filters,
					QueryParameters.UC_ID_EKGRUPPELIEFERANT, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("agstkl.ekgruppelieferant"), true);

			panelDetailLieferant = new PanelEkgruppelieferant(getInternalFrame(),
					LPMain.getTextRespectUISPr("agstkl.ekgruppelieferant"), null);

			panelSplitLieferant = new PanelSplit(getInternalFrame(),
					panelDetailLieferant, panelQueryLieferant, 350);

			setComponentAt(IDX_PANEL_LIEFERANT, panelSplitLieferant);
		} else {
			// filter refreshen.
			panelQueryLieferant.setDefaultFilter(AngebotstklFilterFactory
					.getInstance().createFKEkgruppe(key));
		}
	}

	private void jbInit() throws Throwable {

		insertTab(LPMain.getTextRespectUISPr("lp.auswahl"), null, null,
				LPMain.getTextRespectUISPr("lp.auswahl"), IDX_PANEL_AUSWAHL);

		insertTab(LPMain.getTextRespectUISPr("lp.detail"), null, null,
				LPMain.getTextRespectUISPr("lp.detail"), IDX_PANEL_DETAIL);
		insertTab(LPMain.getTextRespectUISPr("agstkl.ekgruppelieferant"), null,
				null, LPMain.getTextRespectUISPr("agstkl.ekgruppelieferant"),
				IDX_PANEL_LIEFERANT);

		createAuswahl();

		panelQueryEkgruppe.eventYouAreSelected(false);
		if ((Integer) panelQueryEkgruppe.getSelectedId() != null) {
			getInternalFrameAngebotstkl().setEkgruppeDto(
					DelegateFactory
							.getInstance()
							.getAngebotstklDelegate()
							.ekgruppeFindByPrimaryKey(
									(Integer) panelQueryEkgruppe
											.getSelectedId()));
		}
		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryEkgruppe,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryEkgruppe) {
				Integer iId = (Integer) panelQueryEkgruppe.getSelectedId();
				if (iId != null) {
					setSelectedComponent(panelDetailEkgruppe);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelDetailLieferant) {
				panelDetailLieferant.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailEkgruppe) {
				panelDetailEkgruppe.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelDetailLieferant) {
				panelQueryLieferant.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

			if (e.getSource() == panelDetailEkgruppe) {
				panelQueryEkgruppe.clearDirektFilter();
				Object oKey = panelDetailEkgruppe.getKeyWhenDetailPanel();
				panelQueryEkgruppe.setSelectedId(oKey);
			}

			if (e.getSource() == panelDetailLieferant) {
				Object oKey = panelDetailLieferant.getKeyWhenDetailPanel();
				panelQueryLieferant.eventYouAreSelected(false);
				panelQueryLieferant.setSelectedId(oKey);
				panelSplitLieferant.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryLieferant) {
				Integer iId = (Integer) panelQueryLieferant.getSelectedId();
				panelDetailLieferant.setKeyWhenDetailPanel(iId);
				panelDetailLieferant.eventYouAreSelected(false);
				panelQueryLieferant.updateButtons();
			} else if (e.getSource() == panelQueryEkgruppe) {
				if (panelQueryEkgruppe.getSelectedId() != null) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();

					getInternalFrameAngebotstkl().setKeyWasForLockMe(key + "");

					getInternalFrameAngebotstkl().setEkgruppeDto(
							DelegateFactory.getInstance().getAngebotstklDelegate()
									.ekgruppeFindByPrimaryKey((Integer) key));

					if (panelQueryEkgruppe.getSelectedId() == null) {
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
				refreshTitle();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailEkgruppe) {
				getInternalFrame().enableAllPanelsExcept(true);
				this.setSelectedComponent(panelQueryEkgruppe);
				setKeyWasForLockMe();
				panelQueryEkgruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailLieferant) {
				setKeyWasForLockMe();
				if (panelDetailLieferant.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLieferant
							.getId2SelectAfterDelete();
					panelQueryLieferant.setSelectedId(oNaechster);
				}
				panelSplitLieferant.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryEkgruppe) {

				createDetail((Integer) panelQueryEkgruppe.getSelectedId());
				if (panelQueryEkgruppe.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailEkgruppe.eventActionNew(null, true, false);
				setSelectedComponent(panelDetailEkgruppe);
			} else if (e.getSource() == panelQueryLieferant) {
				panelDetailLieferant.eventActionNew(e, true, false);
				panelDetailLieferant.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitLieferant);
			}

		}

	}

	public InternalFrameAngebotstkl getInternalFrameAngebotstkl() {
		return (InternalFrameAngebotstkl) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryEkgruppe.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getTextRespectUISPr("agstkl.ekgruppe"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

		if (getInternalFrameAngebotstkl().getEkgruppeDto() != null
				&& getInternalFrameAngebotstkl().getEkgruppeDto().getCBez() != null) {
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					LPMain.getTextRespectUISPr("agstkl.ekgruppe")
							+ ": "
							+ getInternalFrameAngebotstkl().getEkgruppeDto()
									.getCBez());
		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryEkgruppe.eventYouAreSelected(false);
			if (panelQueryEkgruppe.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}

			panelQueryEkgruppe.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_DETAIL) {
			Integer key = null;
			if (getInternalFrameAngebotstkl().getEkgruppeDto() != null) {
				key = getInternalFrameAngebotstkl().getEkgruppeDto().getIId();
			}
			createDetail(key);
			panelDetailEkgruppe.eventYouAreSelected(false);

		} else if (selectedIndex == IDX_PANEL_LIEFERANT) {
			createLieferant(getInternalFrameAngebotstkl().getEkgruppeDto().getIId());
			panelSplitLieferant.eventYouAreSelected(false);
			panelQueryLieferant.updateButtons();
		}

		refreshTitle();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable  {
		
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar menuBarFahrzeug = new WrapperMenuBar(this);
		JMenu menuInfo = new WrapperMenu("lp.info", this);
		
		
	

		menuBarFahrzeug.add(menuInfo);
		return menuBarFahrzeug;
	}

}
