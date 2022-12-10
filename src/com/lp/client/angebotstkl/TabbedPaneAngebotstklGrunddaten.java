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
package com.lp.client.angebotstkl;

import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;
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
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.05</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 09:44:45 $
 */
public class TabbedPaneAngebotstklGrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int IDX_AUFSCHLAG = -1;
	public int IDX_WEBLIEFERANT = -11;
	public int IDX_WEBFINDCHIPS = -12;

	private PanelQuery panelAufschlagQP1 = null;
	private PanelAufschlag panelAufschlagBottomD1 = null;
	private PanelSplit panelAufschlagSP1 = null;

	private PanelQuery panelWeblieferantQP1 = null;
	private PanelWeblieferant panelWeblieferantBottomD1 = null;
	private PanelSplit panelWeblieferantSP1 = null;
	
	private PanelQuery panelWebFindChipsQP1 = null;
	private PanelWebFindChips panelWebFindChipsBottomD1 = null;
	private PanelSplit panelWebFindChipsSP1 = null;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneAngebotstklGrunddaten(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("lp.grunddaten"));
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
						ParameterFac.PARAMETER_KALKULATIONSART);
		int iKalkulationsart = (java.lang.Integer) parameter.getCWertAsObject();

		int tabIndex = -1;

		if (iKalkulationsart == 2) {
			tabIndex++;

			IDX_AUFSCHLAG = tabIndex;
			insertTab(LPMain.getInstance().getTextRespectUISPr("as.aufschlag"),
					null, null,
					LPMain.getInstance().getTextRespectUISPr("as.aufschlag"),
					IDX_AUFSCHLAG);

			// Default
			refreshAufschlag();

		}

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_WEB_BAUTEIL_ANFRAGE)) {
			tabIndex++;

			IDX_WEBLIEFERANT = tabIndex;
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"agstkl.weblieferant"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"agstkl.weblieferant"), IDX_WEBLIEFERANT);

			// Default
			refreshWeblieferant();
			
			tabIndex++;
			IDX_WEBFINDCHIPS = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("agstkl.webfindchips"), null, null, 
					LPMain.getTextRespectUISPr("agstkl.webfindchips"), IDX_WEBFINDCHIPS);
			refreshWebFindChips();

		}

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("lp.grunddaten"));
	}

	protected JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);
		}
		return wrapperMenuBar;
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {

	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {
		if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelAufschlagQP1) {
				Integer iId = (Integer) panelAufschlagQP1.getSelectedId();
				panelAufschlagBottomD1.setKeyWhenDetailPanel(iId);
				panelAufschlagBottomD1.eventYouAreSelected(false);
				panelAufschlagQP1.updateButtons();
			} else if (eI.getSource() == panelWeblieferantQP1) {
				Integer iId = (Integer) panelWeblieferantQP1.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId == null ? null : iId.toString());
				panelWeblieferantBottomD1.setKeyWhenDetailPanel(iId);
				panelWeblieferantBottomD1.eventYouAreSelected(false);
				panelWeblieferantQP1.updateButtons();
			} else if (eI.getSource() == panelWebFindChipsQP1) {
				Integer iId = (Integer) panelWebFindChipsQP1.getSelectedId();
				panelWebFindChipsBottomD1.setKeyWhenDetailPanel(iId);
				panelWebFindChipsBottomD1.eventYouAreSelected(false);
				panelWebFindChipsQP1.updateButtons();
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelAufschlagBottomD1) {
				// btnstate: 2 im QP die Buttons in den Zustand neu setzen.
				panelAufschlagQP1.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelWeblieferantBottomD1) {
				// btnstate: 2 im QP die Buttons in den Zustand neu setzen.
				panelWeblieferantQP1.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelWebFindChipsBottomD1) {
				panelWebFindChipsQP1.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelAufschlagQP1) {
				panelAufschlagBottomD1.eventActionNew(eI, true, false);
				panelAufschlagBottomD1.eventYouAreSelected(false);
				setSelectedComponent(panelAufschlagSP1);
			} else if (eI.getSource() == panelWeblieferantQP1) {
				panelWeblieferantBottomD1.eventActionNew(eI, true, false);
				panelWeblieferantBottomD1.eventYouAreSelected(false);
				setSelectedComponent(panelWeblieferantSP1);
			} else if (eI.getSource() == panelWebFindChipsQP1) {
				panelWebFindChipsBottomD1.eventActionNew(eI, true, false);
				panelWebFindChipsBottomD1.eventYouAreSelected(false);
				setSelectedComponent(panelWebFindChipsSP1);
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == panelAufschlagBottomD1) {
				if (panelAufschlagBottomD1.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelAufschlagQP1
							.getId2SelectAfterDelete();
					panelAufschlagQP1.setSelectedId(oNaechster);
				}
				panelAufschlagSP1.eventYouAreSelected(false);
			} else if (eI.getSource() == panelWeblieferantBottomD1) {
				if (panelWeblieferantBottomD1.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelWeblieferantQP1
							.getId2SelectAfterDelete();
					panelWeblieferantQP1.setSelectedId(oNaechster);
				}
				panelWeblieferantSP1.eventYouAreSelected(false);
			} else if (eI.getSource() == panelWebFindChipsBottomD1) {
				if (panelWebFindChipsBottomD1.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelWebFindChipsQP1
							.getId2SelectAfterDelete();
					panelWebFindChipsQP1.setSelectedId(oNaechster);
				}
				panelWebFindChipsSP1.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelAufschlagBottomD1) {
				panelAufschlagSP1.eventYouAreSelected(false);
			} else if (eI.getSource() == panelWeblieferantBottomD1) {
				panelWeblieferantSP1.eventYouAreSelected(false);
			} else if (eI.getSource() == panelWebFindChipsBottomD1) {
				panelWebFindChipsSP1.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelAufschlagBottomD1) {
				Object oKey = panelAufschlagBottomD1.getKeyWhenDetailPanel();
				panelAufschlagQP1.eventYouAreSelected(false);
				panelAufschlagQP1.setSelectedId(oKey);
				panelAufschlagSP1.eventYouAreSelected(false);
			} else if (eI.getSource() == panelWeblieferantBottomD1) {
				Object oKey = panelWeblieferantBottomD1.getKeyWhenDetailPanel();
				panelWeblieferantQP1.eventYouAreSelected(false);
				panelWeblieferantQP1.setSelectedId(oKey);
				panelWeblieferantSP1.eventYouAreSelected(false);
			} else if (eI.getSource() == panelWebFindChipsBottomD1) {
				Object oKey = panelWebFindChipsBottomD1.getKeyWhenDetailPanel();
				panelWebFindChipsQP1.eventYouAreSelected(false);
				panelWebFindChipsQP1.setSelectedId(oKey);
				panelWebFindChipsSP1.eventYouAreSelected(false);
			}
		}
		if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (eI.getSource() == panelWeblieferantQP1) {
				int iPos = panelWeblieferantQP1.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelWeblieferantQP1
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelWeblieferantQP1
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getAngebotstklDelegate()
							.vertauscheWeblieferant(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelWeblieferantQP1.setSelectedId(iIdPosition);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (eI.getSource() == panelWeblieferantQP1) {
				int iPos = panelWeblieferantQP1.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelWeblieferantQP1.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelWeblieferantQP1
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelWeblieferantQP1
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getAngebotstklDelegate()
							.vertauscheWeblieferant(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelWeblieferantQP1.setSelectedId(iIdPosition);
				}
			}
		}
	}
	
	private void refreshWebFindChips() throws Throwable {
		if (panelWebFindChipsSP1 == null) {
			String[] aButton = {};
			
			panelWebFindChipsQP1 = new PanelQuery(null, 
					new FilterKriterium[] {AngebotstklFilterFactory.getInstance().createFKWebpartner()}, 
					QueryParameters.UC_ID_WEBFINDCHIPS, aButton, getInternalFrame(),
					LPMain.getTextRespectUISPr("agstkl.webfindchips"), true);
			
			panelWebFindChipsBottomD1 = new PanelWebFindChips(getInternalFrame(), 
					LPMain.getTextRespectUISPr("agstkl.webfindchips"), null);
			
			panelWebFindChipsSP1 = new PanelSplit(getInternalFrame(), 
					panelWebFindChipsBottomD1, panelWebFindChipsQP1, 200);
			setComponentAt(IDX_WEBFINDCHIPS, panelWebFindChipsSP1);
			
			panelWebFindChipsQP1.eventYouAreSelected(true);
		}
	}

	private void refreshWeblieferant() throws Throwable {

		if (panelWeblieferantSP1 == null) {

			// Buttons.
			String[] aButton = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1, };

			FilterKriterium[] kriterien = new FilterKriterium[1];
			kriterien[0] = new FilterKriterium("flrlieferant.mandant_c_nr",
					true, "'"
							+ LPMain.getInstance().getTheClient().getMandant()
							+ "'", FilterKriterium.OPERATOR_EQUAL, false);

			panelWeblieferantQP1 = new PanelQuery(null, kriterien,
					QueryParameters.UC_ID_WEBLIEFERANT, aButton,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("agstkl.weblieferant"), true); // liste
			// refresh
			// wenn
			// lasche
			// geklickt
			// wurde

			panelWeblieferantBottomD1 = new PanelWeblieferant(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("agstkl.weblieferant"), null);

			panelWeblieferantSP1 = new PanelSplit(getInternalFrame(),
					panelWeblieferantBottomD1, panelWeblieferantQP1, 200);
			setComponentAt(IDX_WEBLIEFERANT, panelWeblieferantSP1);

			// liste soll sofort angezeigt werden
			panelWeblieferantQP1.eventYouAreSelected(true);
		}
	}

	private void refreshAufschlag() throws Throwable {

		if (panelAufschlagSP1 == null) {

			// Buttons.
			String[] aButton = { PanelBasis.ACTION_NEW };
			FilterKriterium[] f = SystemFilterFactory.getInstance()
					.createFKMandantCNr();
			panelAufschlagQP1 = new PanelQuery(null, f,
					QueryParameters.UC_ID_AUFSCHLAG, aButton,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("as.aufschlag"), true); // liste
																			// refresh
																			// wenn
																			// lasche
																			// geklickt
																			// wurde

			panelAufschlagBottomD1 = new PanelAufschlag(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("as.aufschlag"),
					null);

			panelAufschlagSP1 = new PanelSplit(getInternalFrame(),
					panelAufschlagBottomD1, panelAufschlagQP1, 200);
			setComponentAt(IDX_AUFSCHLAG, panelAufschlagSP1);

			// liste soll sofort angezeigt werden
			panelAufschlagQP1.eventYouAreSelected(true);
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");

		if (selectedIndex == IDX_AUFSCHLAG) {
			refreshAufschlag();
			panelAufschlagSP1.eventYouAreSelected(false);
			panelAufschlagQP1.updateButtons();
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_OHRWASCHLOBEN, "");

		} else if (selectedIndex == IDX_WEBLIEFERANT) {
			refreshWeblieferant();
			panelWeblieferantSP1.eventYouAreSelected(false);
			panelWeblieferantQP1.updateButtons();
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_OHRWASCHLOBEN, "");

		} else if (selectedIndex == IDX_WEBFINDCHIPS) {
			refreshWebFindChips();
			panelWebFindChipsSP1.eventYouAreSelected(false);
			panelWebFindChipsQP1.updateButtons();
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_OHRWASCHLOBEN, "");

		}
	}

}
