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
package com.lp.client.artikel;

import javax.swing.JMenu;
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
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PanelArtikelzulage;
import com.lp.client.system.PanelDetailTheJudge;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version 1.0
 */
public class TabbedPaneWerkzeuge extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryWerkzeug = null;

	public PanelQuery getPanelQueryWerkzeug() {
		return panelQueryWerkzeug;
	}

	private PanelBasis panelSplitWerkzeug = null;
	private PanelBasis panelBottomWerkzeug = null;

	private PanelQuery panelQueryVerschleissteil = null;
	private PanelBasis panelSplitVerschleissteil = null;
	private PanelBasis panelBottomVerschleissteil = null;

	private PanelQuery panelQueryVerschleissteilwerkzeug = null;
	private PanelBasis panelSplitVerschleissteilwerkzeug = null;
	private PanelBasis panelBottomVerschleissteilwerkzeug = null;

	private WrapperMenuBar wrapperMenuBar = null;

	private  static int IDX_PANEL_WERKZEUG = 0;
	private  static int IDX_PANEL_VERSCHLEISSTEIL = 1;
	private  static int IDX_PANEL_VERSCHLEISSTEILWERKZEUG = 2;

	private final String MENUE_JOURNAL_VERSCHLEISSTEILWERKZEUG = "MENUE_JOURNAL_VERSCHLEISSTEILWERKZEUG";

	private static final String ACTION_SPECIAL_XLSIMPORT_VERSCHLEISSTEIL = "ACTION_SPECIAL_XLSIMPORT_VERSCHLEISSTEIL";
	private final String BUTTON_XLSIMPORT_VERSCHLEISSTEIL = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_XLSIMPORT_VERSCHLEISSTEIL;

	public TabbedPaneWerkzeuge(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"artikel.werkzeug"));

		jbInit();
		initComponents();
	}

	public InternalFrameArtikel getInternalFrameArtikel() {
		return (InternalFrameArtikel) getInternalFrame();
	}

	private void jbInit() throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

		panelQueryWerkzeug = new PanelQuery(null, null,
				QueryParameters.UC_ID_WERKZEUG, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"artikel.werkzeug"), true);
		panelQueryWerkzeug.eventYouAreSelected(false);

		panelBottomWerkzeug = new PanelWerkzeug(getInternalFrame(), LPMain
				.getInstance().getTextRespectUISPr("artikel.werkzeug"), null);
		panelSplitWerkzeug = new PanelSplit(getInternalFrame(),
				panelBottomWerkzeug, panelQueryWerkzeug, 180);
		IDX_PANEL_WERKZEUG=reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("artikel.werkzeug"),
				panelSplitWerkzeug);

		IDX_PANEL_VERSCHLEISSTEIL=reiterHinzufuegen(
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.werkzeug.verschleissteil"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.werkzeug.verschleissteil"));

		IDX_PANEL_VERSCHLEISSTEILWERKZEUG=reiterHinzufuegen(
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.werkzeug.verschleissteilwerkzeug"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.werkzeug.verschleissteilwerkzeug")
				);

		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		this.addChangeListener(this);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryWerkzeug) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomWerkzeug.setKeyWhenDetailPanel(key);
				panelBottomWerkzeug.eventYouAreSelected(false);
				panelQueryWerkzeug.updateButtons();

				if (panelQueryWerkzeug.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, 0,
							false);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, 0,
							true);
				}
			}
			if (e.getSource() == panelQueryVerschleissteil) {
				Integer iId = (Integer) panelQueryVerschleissteil
						.getSelectedId();
				panelBottomVerschleissteil.setKeyWhenDetailPanel(iId);
				panelBottomVerschleissteil.eventYouAreSelected(false);
				panelQueryVerschleissteil.updateButtons();
			}
			if (e.getSource() == panelQueryVerschleissteilwerkzeug) {
				Integer iId = (Integer) panelQueryVerschleissteilwerkzeug
						.getSelectedId();
				panelBottomVerschleissteilwerkzeug.setKeyWhenDetailPanel(iId);
				panelBottomVerschleissteilwerkzeug.eventYouAreSelected(false);
				panelQueryVerschleissteilwerkzeug.updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			panelSplitWerkzeug.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryWerkzeug) {
				panelBottomWerkzeug.eventActionNew(e, true, false);
				panelBottomWerkzeug.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryVerschleissteil) {
				panelBottomVerschleissteil.eventActionNew(e, true, false);
				panelBottomVerschleissteil.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitVerschleissteil);
			} else if (e.getSource() == panelQueryVerschleissteilwerkzeug) {
				panelBottomVerschleissteilwerkzeug.eventActionNew(e, true,
						false);
				panelBottomVerschleissteilwerkzeug.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitVerschleissteilwerkzeug);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomWerkzeug) {
				panelSplitWerkzeug.eventYouAreSelected(false);
			}
			if (e.getSource() == panelBottomVerschleissteil) {
				panelBottomVerschleissteil.eventYouAreSelected(false);
			}
			if (e.getSource() == panelBottomVerschleissteilwerkzeug) {
				panelBottomVerschleissteilwerkzeug.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomWerkzeug) {
				panelQueryWerkzeug.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
			if (e.getSource() == panelBottomVerschleissteil) {
				panelQueryVerschleissteil.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));

			}
			if (e.getSource() == panelBottomVerschleissteilwerkzeug) {
				panelQueryVerschleissteilwerkzeug
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomWerkzeug) {
				Object oKey = panelBottomWerkzeug.getKeyWhenDetailPanel();
				panelQueryWerkzeug.eventYouAreSelected(false);
				panelQueryWerkzeug.setSelectedId(oKey);
				panelSplitWerkzeug.eventYouAreSelected(false);

			}
			if (e.getSource() == panelBottomVerschleissteil) {
				Object oKey = panelBottomVerschleissteil
						.getKeyWhenDetailPanel();
				panelQueryVerschleissteil.eventYouAreSelected(false);
				panelQueryVerschleissteil.setSelectedId(oKey);
				panelSplitVerschleissteil.eventYouAreSelected(false);

			}
			if (e.getSource() == panelBottomVerschleissteilwerkzeug) {
				Object oKey = panelBottomVerschleissteilwerkzeug
						.getKeyWhenDetailPanel();
				panelQueryVerschleissteilwerkzeug.eventYouAreSelected(false);
				panelQueryVerschleissteilwerkzeug.setSelectedId(oKey);
				panelSplitVerschleissteilwerkzeug.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomWerkzeug) {
				setKeyWasForLockMe();
				if (panelBottomWerkzeug.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryWerkzeug
							.getId2SelectAfterDelete();
					panelQueryWerkzeug.setSelectedId(oNaechster);
				}
				panelSplitWerkzeug.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomVerschleissteil) {
				setKeyWasForLockMe();
				if (panelBottomVerschleissteil.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryVerschleissteil
							.getId2SelectAfterDelete();
					panelQueryVerschleissteil.setSelectedId(oNaechster);
				}
				panelSplitVerschleissteil.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomVerschleissteilwerkzeug) {
				setKeyWasForLockMe();
				if (panelBottomVerschleissteilwerkzeug.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryVerschleissteilwerkzeug
							.getId2SelectAfterDelete();
					panelQueryVerschleissteilwerkzeug.setSelectedId(oNaechster);
				}
				panelSplitVerschleissteilwerkzeug.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (sAspectInfo.endsWith(ACTION_SPECIAL_XLSIMPORT_VERSCHLEISSTEIL)) {
				DialogVerschleissteilImportXLS d = new DialogVerschleissteilImportXLS(
						this);
			}
		}

	}

	private void createVerschleissteil(Integer key) throws Throwable {

		if (panelQueryVerschleissteil == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryVerschleissteil = new PanelQuery(null, null,
					QueryParameters.UC_ID_VERSCHLEISSTEIL, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"artikel.werkzeug.verschleissteil"), true);

			panelBottomVerschleissteil = new PanelVerschleissteil(
					getInternalFrame(), this, LPMain.getInstance()
							.getTextRespectUISPr(
									"artikel.werkzeug.verschleissteil"), null);

			panelQueryVerschleissteil.createAndSaveAndShowButton(
					"/com/lp/client/res/document_into.png",
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.verschleissteile.xlsimport"),
					BUTTON_XLSIMPORT_VERSCHLEISSTEIL, null);

			panelSplitVerschleissteil = new PanelSplit(getInternalFrame(),
					panelBottomVerschleissteil, panelQueryVerschleissteil, 350);

			setComponentAt(IDX_PANEL_VERSCHLEISSTEIL, panelSplitVerschleissteil);
		}
	}

	private void createVerschleissteilwerkzeug() throws Throwable {

		if (panelQueryVerschleissteilwerkzeug == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryVerschleissteilwerkzeug = new PanelQuery(null, null,
					QueryParameters.UC_ID_VERSCHLEISSTEILWERKZEUG,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.werkzeug.verschleissteilwerkzeug"), true);

			panelBottomVerschleissteilwerkzeug = new PanelVerschleissteilwerkzeug(
					getInternalFrame(), this,
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.werkzeug.verschleissteilwerkzeug"), null);

			panelSplitVerschleissteilwerkzeug = new PanelSplit(
					getInternalFrame(), panelBottomVerschleissteilwerkzeug,
					panelQueryVerschleissteilwerkzeug, 350);

			setComponentAt(IDX_PANEL_VERSCHLEISSTEILWERKZEUG,
					panelSplitVerschleissteilwerkzeug);
		}
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryWerkzeug.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("artikel.werkzeug"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		getInternalFrame().setLpTitle(3, "");
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_WERKZEUG) {
			panelSplitWerkzeug.eventYouAreSelected(false);
			panelQueryWerkzeug.updateButtons();

		} else if (selectedIndex == IDX_PANEL_VERSCHLEISSTEIL) {
			createVerschleissteil((Integer) panelQueryWerkzeug.getSelectedId());
			panelSplitVerschleissteil.eventYouAreSelected(false);
			panelQueryVerschleissteil.updateButtons();
		} else if (selectedIndex == IDX_PANEL_VERSCHLEISSTEILWERKZEUG) {
			createVerschleissteilwerkzeug();
			panelSplitVerschleissteilwerkzeug.eventYouAreSelected(false);
			panelQueryVerschleissteilwerkzeug.updateButtons();
		}

	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_JOURNAL_VERSCHLEISSTEILWERKZEUG)) {

			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"artikel.werkzeug.verschleissteilwerkzeug");
			if (getInternalFrameArtikel().getArtikelDto() != null) {
				getInternalFrame().showReportKriterien(
						new ReportVerschleissteilWerkzeug(
								getInternalFrameArtikel(), add2Title));
			}

		}
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);

			JMenu journal = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_JOURNAL);
			WrapperMenuItem menuItemVW = new WrapperMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"artikel.werkzeug.verschleissteilwerkzeug"), null);
			menuItemVW.addActionListener(this);
			menuItemVW.setActionCommand(MENUE_JOURNAL_VERSCHLEISSTEILWERKZEUG);
			journal.add(menuItemVW);

		}
		return wrapperMenuBar;
	}

}
