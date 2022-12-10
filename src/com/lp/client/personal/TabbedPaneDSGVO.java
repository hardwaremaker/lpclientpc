package com.lp.client.personal;

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

import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;

import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.fertigung.InternalFrameFertigung;
import com.lp.client.fertigung.ReportMaschineUndMaterial;
import com.lp.client.fertigung.ReportTaetigkeitAGBeginn;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMapButton;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.5 $
 */
public class TabbedPaneDSGVO extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelPartnerQP = null;

	private PanelQuery panelQueryBeauskunftung = null;
	private PanelBasis panelBottomBeauskunftung = null;
	private PanelSplit panelSplitBeauskunftung = null;

	private final static int IDX_PANEL_PARTNER = 0;
	private final static int IDX_PANEL_BEAUSKUNFTUNG = 1;

	private WrapperMenuBar wrapperMenuBar = null;

	private static final String MENUE_ACTION_MODULBERECHTIGUNG = "MENUE_ACTION_MODULBERECHTIGUNG";
	private static final String MENUE_ACTION_BEAUSKUNFTUNG = "MENUE_ACTION_BEAUSKUNFTUNG";

	public TabbedPaneDSGVO(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"pers.dsgvo"));
		jbInit();
		initComponents();

	}

	public PanelQuery getPanelQueryPArtner() {
		return panelPartnerQP;
	}

	private void jbInit() throws Throwable {

		insertTab(LPMain.getInstance().getTextRespectUISPr("part.partner"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("part.partner"),
				IDX_PANEL_PARTNER);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"pers.dsgvo.beauskunftung"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.dsgvo.beauskunftung"), IDX_PANEL_BEAUSKUNFTUNG);

		createAuswahl();

		panelPartnerQP.eventYouAreSelected(false);

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelPartnerQP,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	private void createAuswahl() throws Throwable {
		if (panelPartnerQP == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_FILTER };

			panelPartnerQP = new PanelQuery(PartnerFilterFactory.getInstance()
					.createQTPartnerart(), null, QueryParameters.UC_ID_PARTNER,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.auswahl"), true,
					PartnerFilterFactory.getInstance().createFKVPartner(), null);

			panelPartnerQP.befuellePanelFilterkriterienDirekt(
					PartnerFilterFactory.getInstance().createFKDPartnerName(),
					PartnerFilterFactory.getInstance()
					// .createFKDPartnerLandPLZOrt());
							.createFKDLandPLZOrt());

			panelPartnerQP.addDirektFilter(PartnerFilterFactory.getInstance()
					.createFKDPartnerErweiterteSuche());

			panelPartnerQP.addDirektFilter(PartnerFilterFactory.getInstance()
					.createFKDPartnersucheNachTelefonnummer());

			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
			boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter
					.getCWertAsObject();

			if (!bSuchenInklusiveKbez) {
				panelPartnerQP.addDirektFilter(PartnerFilterFactory
						.getInstance().createFKDPartnerKurzbezeichnung());
			}

			setComponentAt(IDX_PANEL_PARTNER, panelPartnerQP);
		}
	}

	private void createBeauskunftung(Integer partnerIId) throws Throwable {

		if (panelQueryBeauskunftung == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKBeauskunftung(partnerIId);

			panelQueryBeauskunftung = new PanelQuery(null, filters,
					QueryParameters.UC_ID_BEAUSKUNFTUNG, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.dsgvo.beauskunftung"),
					true);

			panelBottomBeauskunftung = new PanelBeauskunftung(
					getInternalFrame(), this, LPMain.getInstance()
							.getTextRespectUISPr("pers.dsgvo.beauskunftung"),
					null);

			panelSplitBeauskunftung = new PanelSplit(getInternalFrame(),
					panelBottomBeauskunftung, panelQueryBeauskunftung, 250);

			setComponentAt(IDX_PANEL_BEAUSKUNFTUNG, panelSplitBeauskunftung);
		} else {
			// filter refreshen.
			panelQueryBeauskunftung.setDefaultFilter(PartnerFilterFactory
					.getInstance().createFKBeauskunftung(partnerIId));
		}
	}

	public InternalFramePersonal getInternalFramePersonal() {
		return (InternalFramePersonal) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelPartnerQP.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelPartnerQP) {
				if (panelPartnerQP.getSelectedId() != null) {
					getInternalFramePersonal().setKeyWasForLockMe(
							panelPartnerQP.getSelectedId() + "");

					if (panelPartnerQP.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_PARTNER, false);
					} else {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_PARTNER, true);
					}
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_PARTNER, true);
				}

			} else if (e.getSource() == panelQueryBeauskunftung) {
				Integer iId = (Integer) panelQueryBeauskunftung.getSelectedId();
				panelBottomBeauskunftung.setKeyWhenDetailPanel(iId);
				panelBottomBeauskunftung.eventYouAreSelected(false);
				panelQueryBeauskunftung.updateButtons();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			// btnnew: einen neuen machen.

			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryBeauskunftung) {

				createBeauskunftung((Integer) panelPartnerQP
						.getSelectedId());
				panelBottomBeauskunftung.eventActionNew(e, true, false);
				panelBottomBeauskunftung.eventYouAreSelected(false);

			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_TABLE_SELECTION_CHANGED) {

		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomBeauskunftung) {
				panelBottomBeauskunftung.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomBeauskunftung) {
				panelQueryBeauskunftung.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

			if (e.getSource() == panelBottomBeauskunftung) {
				Object oKey = panelBottomBeauskunftung.getKeyWhenDetailPanel();
				panelQueryBeauskunftung.eventYouAreSelected(false);
				panelQueryBeauskunftung.setSelectedId(oKey);
				panelSplitBeauskunftung.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelBottomBeauskunftung) {
				setKeyWasForLockMe();
				if (panelBottomBeauskunftung.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryBeauskunftung
							.getId2SelectAfterDelete();
					panelQueryBeauskunftung.setSelectedId(oNaechster);
				}
				panelSplitBeauskunftung.eventYouAreSelected(false);
			}
		}
	}

	private boolean vergleicheSortReihung(ArrayList<SortierKriterium> al1,
			ArrayList<SortierKriterium> al2) {

		if (al1.size() == al2.size()) {

			for (int i = 0; i < al1.size(); i++) {

				if (!al1.get(i).kritName.equals(al2.get(i).kritName)) {
					return false;
				}

			}
			return true;

		} else {
			return false;
		}

	}

	private void refreshTitle() {
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("pers.dsgvo"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_PARTNER) {
			createAuswahl();
			panelPartnerQP.eventYouAreSelected(false);

			panelPartnerQP.updateButtons();
		} else if (selectedIndex == IDX_PANEL_BEAUSKUNFTUNG) {
			createBeauskunftung((Integer) panelPartnerQP.getSelectedId());
			panelSplitBeauskunftung.eventYouAreSelected(false);
			panelQueryBeauskunftung.updateButtons();
		}

		refreshTitle();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_ACTION_MODULBERECHTIGUNG)) {
			String add2Title = "";
			getInternalFrame().showReportKriterien(
					new ReportModulberechtigungen(getInternalFramePersonal(),
							add2Title));

		} else if (e.getActionCommand().equals(MENUE_ACTION_BEAUSKUNFTUNG)) {
			String add2Title = "";
			getInternalFrame().showReportKriterien(
					new ReportBeauskunftung(this, null, add2Title));

		}
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		setName("TabbedPaneDSGVO");
		if (wrapperMenuBar == null) {

			wrapperMenuBar = new WrapperMenuBar(this);

			JMenuItem menuItemModulberechtigungen = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"pers.dsgvo.report.modulberechtigung"));

			menuItemModulberechtigungen.addActionListener(this);

			menuItemModulberechtigungen
					.setActionCommand(MENUE_ACTION_MODULBERECHTIGUNG);
			JMenu jmJournal = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_JOURNAL);
			jmJournal.add(menuItemModulberechtigungen);

			JMenu menuInfo = new WrapperMenu("lp.info", this);

			JMenuItem menuItemBeauskunftung = new JMenuItem(LPMain
					.getInstance().getTextRespectUISPr(
							"pers.dsgvo.beauskunftung"));

			menuItemBeauskunftung.addActionListener(this);

			menuItemBeauskunftung.setActionCommand(MENUE_ACTION_BEAUSKUNFTUNG);

			menuInfo.add(menuItemBeauskunftung);

			wrapperMenuBar.addJMenuItem(menuInfo);

		}

		return wrapperMenuBar;

	}

}
