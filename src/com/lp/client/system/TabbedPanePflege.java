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

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;

import com.lp.client.artikel.ReportArtikelreservierungen;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.pc.LPMain;
import com.lp.client.system.pflege.PanelPflegeNew;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>
 * Ueberschrift:
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
 * @author unbekannt
 * @version $Revision: 1.3 $
 */
public class TabbedPanePflege extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PanelQuery panelQueryTheJudge = null;

	private static int IDX_PFLEGE = -1;
	private static int IDX_PFLEGENEW = -1;
	private static int IDX_PANELSPERREN = -1;
	private static int IDX_PFLEGE_INTERN = -1;

	private PanelPflegeNew panelPflegeNew = null;
	private PanelPflege panelPflege = null;
	private PanelPflegeIntern panelPflegeIntern = null;

	private PanelQuery panelQuerySperren = null;
	private PanelBasis panelBottomSperren = null;
	private PanelSplit panelSplitSperren = null;

	private WrapperMenuBar wrapperMenuBar = null;

	private final String MENUE_ACTION_STATISTIK = "MENUE_ACTION_STATISTIK";

	public TabbedPanePflege(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr("lp.pflege"));
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// Tab 1: Rechte
		IDX_PFLEGE = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.pflege"), null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.pflege"));
		IDX_PFLEGENEW = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.pflege.neu"), null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.pflege.neu"));

		if (LPMain.getInstance().isLPAdmin()) {
			IDX_PFLEGE_INTERN = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.pflege.intern"), null,
					null, LPMain.getInstance().getTextRespectUISPr("lp.pflege.intern"));
		}

		IDX_PANELSPERREN = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.pflege.panelsperren"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.pflege.panelsperren"));

		setSelectedComponent(getPanelQueryTheJudge());
		// refresh
		getPanelQueryTheJudge().eventYouAreSelected(false);
		// damit gleich eine selektiert ist
		ItemChangedEvent it = new ItemChangedEvent(getPanelQueryTheJudge(), ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);
		// Listener
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	private PanelQuery getPanelQueryTheJudge() throws Throwable {
		if (panelQueryTheJudge == null) {
			String[] aWhichButtonIUseQPTheJudge = { PanelBasis.ACTION_REFRESH };

			QueryType[] qtTheJudge = null;
			FilterKriterium[] filtersTheJudge = null;

			panelQueryTheJudge = new PanelQuery(qtTheJudge, filtersTheJudge, QueryParameters.UC_ID_THEJUDGE,
					aWhichButtonIUseQPTheJudge, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.benutzer.gesperrt"), true);
			this.setComponentAt(IDX_PFLEGE, panelQueryTheJudge);
		}
		return panelQueryTheJudge;
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {
		if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelQuerySperren) {
				Integer iId = (Integer) panelQuerySperren.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomSperren.setKeyWhenDetailPanel(iId);
				panelBottomSperren.eventYouAreSelected(false);

				panelQuerySperren.updateButtons(panelBottomSperren.getLockedstateDetailMainKey());

			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (eI.getSource() == panelBottomSperren) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQuerySperren.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));

			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == panelBottomSperren) {
				panelSplitSperren.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelQuerySperren) {
				panelBottomSperren.eventActionNew(eI, true, false);
				panelBottomSperren.eventYouAreSelected(false);
				setSelectedComponent(panelSplitSperren);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelBottomSperren) {
				Object oKey = panelBottomSperren.getKeyWhenDetailPanel();
				panelQuerySperren.eventYouAreSelected(false);
				panelQuerySperren.setSelectedId(oKey);
				panelSplitSperren.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelBottomSperren) {
				panelSplitSperren.eventYouAreSelected(false);
			}
		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PFLEGENEW) {
			refreshPanelPflegeNew();
			panelPflegeNew.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelPflegeNew.updateButtons();
		} else if (selectedIndex == IDX_PFLEGE) {
			refreshPanelPflege();
			panelPflege.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelPflege.updateButtons();
		} else if (selectedIndex == IDX_PFLEGE_INTERN) {
			refreshPanelPflegeIntern();
			panelPflegeIntern.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelPflegeIntern.updateButtons();
		} else if (selectedIndex == IDX_PANELSPERREN) {
			refreshPanelsperren();
			panelQuerySperren.eventYouAreSelected(false);

			panelQuerySperren.updateButtons(panelQuerySperren.getLockedstateDetailMainKey());

		}

	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_ACTION_STATISTIK)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("system.report.statistik");
			getInternalFrame().showReportKriterien(new ReportStatistik(getInternalFrame(), add2Title));

		}
	}

	private void refreshPanelPflege() throws Throwable {
		if (panelPflege == null) {
			panelPflege = new PanelPflege(getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("lp.pflege"));
			setComponentAt(IDX_PFLEGE, panelPflege);

			// liste soll sofort angezeigt werden
			panelPflege.eventYouAreSelected(true);
		}
	}

	private void refreshPanelPflegeNew() throws Throwable {
		if (panelPflegeNew == null) {
			panelPflegeNew = new PanelPflegeNew(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.pflege.neu"));
			setComponentAt(IDX_PFLEGENEW, panelPflegeNew);

			// liste soll sofort angezeigt werden
			panelPflegeNew.eventYouAreSelected(true);
		}
	}

	private void refreshPanelsperren() throws Throwable {
		if (panelSplitSperren == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			panelQuerySperren = new PanelQuery(null, null, QueryParameters.UC_ID_PANELSPERREN, aWhichStandardButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("lp.pflege.panelsperren"), true);

			panelBottomSperren = new PanelPanelsperren(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.pflege.panelsperren"), null);

			panelSplitSperren = new PanelSplit(getInternalFrame(), panelBottomSperren, panelQuerySperren, 180);
			setComponentAt(IDX_PANELSPERREN, panelSplitSperren);
		}
		// filter refreshen
		panelQuerySperren.setDefaultFilter(
				SystemFilterFactory.getInstance().createFKMandantCNr(LPMain.getTheClient().getMandant()));

		// liste soll sofort angezeigt werden
		panelQuerySperren.eventYouAreSelected(true);
	}

	private void refreshPanelPflegeIntern() throws Throwable {
		if (panelPflegeIntern == null) {
			panelPflegeIntern = new PanelPflegeIntern(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.pflege.intern"));
			setComponentAt(IDX_PFLEGE_INTERN, panelPflegeIntern);

			// liste soll sofort angezeigt werden
			panelPflegeIntern.eventYouAreSelected(true);
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {

		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);

			JMenu menuInfo = new WrapperMenu("lp.info", this);

			JMenuItem menuItemStatistik = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("system.report.statistik"));
			menuItemStatistik.addActionListener(this);
			menuItemStatistik.setActionCommand(MENUE_ACTION_STATISTIK);
			menuInfo.add(menuItemStatistik);

			wrapperMenuBar.addJMenuItem(menuInfo);
		}

		return wrapperMenuBar;
	}
}
