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
package com.lp.client.fertigung;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;

import com.lp.client.auftrag.AuftragFilterFactory;
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
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.InternebestellungDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um die Interne Bestellung </I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>03.12.05</I>
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.8 $
 */
public class TabbedPaneBedarfsuebernahme extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryBedarfsuebernahme = null;
	private PanelSplit panelSplitBedarfsuebernahme = null;
	private PanelBedarfsuebernahme panelDetailBedarfsuebernahme = null;

	private final String MENUE_ACTION_SYNCHRONISATION = "MENUE_ACTION_SYNCHRONISATION";
	private final String MENUE_ACTION_BUCHUNGSLISTE = "MENUE_ACTION_BUCHUNGSLISTE";
	private final String MENUE_ACTION_OFFENE = "MENUE_ACTION_OFFENE";

	private InternebestellungDto internebestellungDto = null;
	private StuecklisteDto stuecklisteDto = null;

	private static final int IDX_PANEL_BEDARFSUEBERNHAME = 0;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneBedarfsuebernahme(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain
				.getTextRespectUISPr("fert.bedarfsuebernahme"));

		jbInit();
		initComponents();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		insertTab(LPMain.getTextRespectUISPr("fert.bedarfsuebernahme"), null,
				null, LPMain.getTextRespectUISPr("fert.bedarfsuebernahme"),
				IDX_PANEL_BEDARFSUEBERNHAME);

		getPanelSplitBedarfsuebernahme(true);

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	protected InternebestellungDto getInternebestellungDto() {
		return internebestellungDto;
	}

	protected void setInternebestellungDto(
			InternebestellungDto internebestellungDto) throws Throwable {
		this.internebestellungDto = internebestellungDto;
		if (internebestellungDto != null) {

			// Stueckliste nur dann neu laden, wenn sie nicht eh schon da ist.
			if (this.getStuecklisteDto() == null
					|| !this.getStuecklisteDto().getIId()
							.equals(internebestellungDto.getStuecklisteIId())) {
				this.setStuecklisteDto(DelegateFactory
						.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(
								internebestellungDto.getStuecklisteIId()));
			}
		}
	}

	protected void setStuecklisteDto(StuecklisteDto stuecklisteDto) {
		this.stuecklisteDto = stuecklisteDto;
	}

	protected StuecklisteDto getStuecklisteDto() {
		return stuecklisteDto;
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == getPanelQueryBedarfsuebernahme(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_BEDARFSUEBERNHAME, false);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_BEDARFSUEBERNHAME, true);
				}

				getPanelDetailBedarfsuebernahme(true)
						.setKeyWhenDetailPanel(key);

				getPanelDetailBedarfsuebernahme(true)
						.eventYouAreSelected(false);
				// Buttons richtig schalten
				getPanelQueryBedarfsuebernahme(true).updateButtons();

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (e.getSource() == getPanelDetailBedarfsuebernahme(false)) {
				// im QP die Buttons in den Zustand neu setzen.
				getPanelQueryBedarfsuebernahme(false).updateButtons(
						new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == getPanelDetailBedarfsuebernahme(false)) {
				getPanelSplitBedarfsuebernahme(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == getPanelDetailBedarfsuebernahme(false)) {
				Object oKey = getPanelDetailBedarfsuebernahme(true)
						.getKeyWhenDetailPanel();
				getPanelQueryBedarfsuebernahme(true).eventYouAreSelected(false);
				getPanelQueryBedarfsuebernahme(true).setSelectedId(oKey);
				getPanelSplitBedarfsuebernahme(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == getPanelDetailBedarfsuebernahme(false)) {
				setKeyWasForLockMe();

				if (getPanelDetailBedarfsuebernahme(true)
						.getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryBedarfsuebernahme(true)
							.getId2SelectAfterDelete();
					getPanelQueryBedarfsuebernahme(true).setSelectedId(
							oNaechster);
				}

				getPanelSplitBedarfsuebernahme(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == getPanelQueryBedarfsuebernahme(true)) {
				getPanelDetailBedarfsuebernahme(true).eventActionNew(e, true,
						false);
				getPanelDetailBedarfsuebernahme(true)
						.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

		}
	}

	/**
	 * eventStateChanged
	 * 
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		boolean hatRecht = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_FERT_LOS_CUD);
		if (!hatRecht) {
			getInternalFrame()
					.setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
		}

		switch (selectedIndex) {
		case IDX_PANEL_BEDARFSUEBERNHAME: {
			getPanelSplitBedarfsuebernahme(true).eventYouAreSelected(false);
		}

			break;
		}

	}

	/**
	 * Diese Methode setzt die aktuelle Interne Bestellung aus der Auswahlliste
	 * als die zu lockende Bestellung.
	 * 
	 * @throws Throwable
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = getPanelQueryBedarfsuebernahme(true).getSelectedId();
		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_ACTION_SYNCHRONISATION)) {

			String add2Title = LPMain
					.getTextRespectUISPr("fert.bedarfsuebernahme.report.synchronisieren");
			getInternalFrame().showReportKriterien(
					new ReportBedarfsuebernahmeSynchronisation(
							getInternalFrame(), add2Title, null));

		} else if (e.getActionCommand().equals(MENUE_ACTION_BUCHUNGSLISTE)) {

			String add2Title = LPMain
					.getTextRespectUISPr("fert.bedarfsuebernahme.report.buchungsliste");
			getInternalFrame().showReportKriterien(
					new ReportBedarfsuebernahmeBuchungsliste(
							getInternalFrame(), add2Title, null));

		} else if (e.getActionCommand().equals(MENUE_ACTION_OFFENE)) {

			String add2Title = LPMain
					.getTextRespectUISPr("fert.bedarfsuebernahme.report.offene");
			getInternalFrame().showReportKriterien(
					new ReportBedarfsuebernahmeOffene(getInternalFrame(),
							add2Title, null));

		}
	}

	private PanelBedarfsuebernahme getPanelDetailBedarfsuebernahme(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailBedarfsuebernahme == null && bNeedInstantiationIfNull) {
			panelDetailBedarfsuebernahme = new PanelBedarfsuebernahme(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.bedarfsuebernahme"), null, // eventuell
																				// gibt
																				// es
																				// noch
																				// keine
																				// Position
					this);
		}
		return panelDetailBedarfsuebernahme;
	}

	public PanelQuery getPanelQueryBedarfsuebernahme(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryBedarfsuebernahme == null && bNeedInstantiationIfNull) {

			FilterKriterium[] kriterien = new FilterKriterium[1];
			kriterien[0] = new FilterKriterium("flrpersonal_anlegen.mandant_c_nr", true,
					"'" + LPMain.getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);

			String[] aWhichButtonIUse = null;

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_DEBUGMODUS)) {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW };
			}

			panelQueryBedarfsuebernahme = new PanelQuery(null, kriterien,
					QueryParameters.UC_ID_BEDARFSUEBERNAHME, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.bedarfsuebernahme"), true); // flag,
																					// damit
																					// flr
																					// erst
																					// beim
																					// aufruf
																					// des
																					// panels
																					// loslaeuft

			FilterKriteriumDirekt fkDirekt1 = new FilterKriteriumDirekt(
					"flrartikel.c_nr", "", FilterKriterium.OPERATOR_LIKE,
					LPMain.getTextRespectUISPr("artikel.artikelnummer"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
															// 'XX%'
					true, // wrapWithSingleQuotes
					true, 99);

			FilterKriteriumDirekt fkDirekt2 = new FilterKriteriumDirekt(
					"flrpersonal_anlegen.c_kurzzeichen", "",
					FilterKriterium.OPERATOR_LIKE,
					LPMain.getTextRespectUISPr("pers.personal.kurzzeichen"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
															// 'XX%'
					true, // wrapWithSingleQuotes
					true, 99);

			panelQueryBedarfsuebernahme.befuellePanelFilterkriterienDirekt(
					fkDirekt1, fkDirekt2);

			panelQueryBedarfsuebernahme
					.addDirektFilter(new FilterKriteriumDirekt(
							"flrbedarfsuebernahme.flrlos.c_nr", "",
							FilterKriterium.OPERATOR_LIKE, LPMain
									.getTextRespectUISPr("label.losnummer"),
							FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung
																	// als '%XX'
							true, // wrapWithSingleQuotes
							true, Facade.MAX_UNBESCHRAENKT));

			FilterKriterium[] filters = new FilterKriterium[1];
			filters[0] = new FilterKriterium("t_verbucht_gedruckt", true, "",
					FilterKriterium.OPERATOR_IS + " "
							+ FilterKriterium.OPERATOR_NULL, false);

			panelQueryBedarfsuebernahme
					.befuelleFilterkriteriumSchnellansicht(filters);

		}
		return panelQueryBedarfsuebernahme;
	}

	private PanelSplit getPanelSplitBedarfsuebernahme(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitBedarfsuebernahme == null && bNeedInstantiationIfNull) {
			panelSplitBedarfsuebernahme = new PanelSplit(getInternalFrame(),
					getPanelDetailBedarfsuebernahme(true),
					getPanelQueryBedarfsuebernahme(true), 180);
			setComponentAt(IDX_PANEL_BEDARFSUEBERNHAME,
					panelSplitBedarfsuebernahme);
		}
		return panelSplitBedarfsuebernahme;
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		setName("TabbedPaneInternebestellung");
		if (wrapperMenuBar == null) {

			wrapperMenuBar = new WrapperMenuBar(this);

			JMenu menuInfo = new WrapperMenu("lp.info", this);

			JMenuItem menuItemBuchungsliste = new JMenuItem(
					LPMain.getTextRespectUISPr("fert.bedarfsuebernahme.report.buchungsliste"));
			menuItemBuchungsliste.addActionListener(this);
			menuItemBuchungsliste.setActionCommand(MENUE_ACTION_BUCHUNGSLISTE);
			menuInfo.add(menuItemBuchungsliste);

			wrapperMenuBar.add(menuInfo);

			JMenu modul = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_MODUL);
			modul.add(new JSeparator(), 0);
			JMenuItem menuItemSynchronisation = new JMenuItem(
					LPMain.getTextRespectUISPr("fert.bedarfsuebernahme.report.synchronisieren"));
			menuItemSynchronisation.addActionListener(this);
			menuItemSynchronisation
					.setActionCommand(MENUE_ACTION_SYNCHRONISATION);

			modul.add(menuItemSynchronisation, 0);

			JMenu modulJournal = (JMenu) wrapperMenuBar
					.getComponent(WrapperMenuBar.MENU_JOURNAL);

			JMenuItem menuItemOffene = new JMenuItem(
					LPMain.getTextRespectUISPr("fert.bedarfsuebernahme.report.offene"));
			menuItemOffene.addActionListener(this);
			menuItemOffene.setActionCommand(MENUE_ACTION_OFFENE);

			modulJournal.add(menuItemOffene);

		}

		return wrapperMenuBar;

	}

	public Integer getSelectedIIdInternebestellung() throws Throwable {
		return (Integer) getPanelQueryBedarfsuebernahme(true).getSelectedId();
	}

	/**
	 * hole EingangsrechnungDto.
	 * 
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void holeInternebestellungDto(Object key) throws Throwable {
		if (key != null) {
			InternebestellungDto ibDto = DelegateFactory.getInstance()
					.getFertigungDelegate()
					.internebestellungFindByPrimaryKey((Integer) key);
			setInternebestellungDto(ibDto);
			getInternalFrame().setKeyWasForLockMe(key.toString());
			if (getPanelDetailBedarfsuebernahme(false) != null) {
				getPanelDetailBedarfsuebernahme(true)
						.setKeyWhenDetailPanel(key);
			}
		} else {
			setInternebestellungDto(null);
		}
	}

}
