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
package com.lp.client.eingangsrechnung;

import java.awt.event.ActionEvent;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.stammdatencrud.PanelStammdatenCRUD;
import com.lp.client.pc.LPMain;
import com.lp.client.rechnung.RechnungFilterFactory;
import com.lp.server.eingangsrechnung.service.EingangsrechnungServiceFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungtextDto;
import com.lp.server.rechnung.service.RechnungServiceFac;
import com.lp.server.rechnung.service.RechnungtextDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich um die ER-Grunddaten.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 03.05.05</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 07:50:58 $
 */
public class TabbedPaneGrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static int IDX_EINGANGSRECHNUNGART = 0;
	private final static int IDX_EINGANGSRECHNUNGSSTATUS = 1;
	private final static int IDX_EINGANGSRECHNUNGSTEXT = 2;

	private PanelQuery panelEingangsrechnungsartQP1 = null;
	private PanelStammdatenCRUD panelEingangsrechnungsartBottomD1 = null;
	private PanelSplit panelEingangsrechnungsartSP1 = null;
	private PanelQuery panelEingangsrechnungsstatusQP2 = null;
	private PanelStammdatenCRUD panelEingangsrechnungsstatusBottomD2 = null;
	private PanelSplit panelEingangsrechnungsstatusSP2 = null;

	private PanelQuery panelQueryEingangsrechnungtext;
	private PanelStammdatenCRUD panelBottomEingangsrechnungtext;
	private PanelSplit panelSplitEingangsrechnungtext;

	public TabbedPaneGrunddaten(InternalFrame internalFrameI) throws Throwable {

		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"pers.title.tab.grunddaten"));

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {

		// Tab 1: Eingangsrechnungsart
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"er.tab.oben.eingangsrechnungsart"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"er.tab.oben.eingangsrechnungsart"),
				IDX_EINGANGSRECHNUNGART);

		// Tab 2: Eingangsrechnungsstatus
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"er.tab.oben.eingangsrechnungsstatus"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"er.tab.oben.eingangsrechnungsstatus"),
				IDX_EINGANGSRECHNUNGSSTATUS);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"lp.eingangsrechnungtext"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"lp.eingangsrechnungtext"), IDX_EINGANGSRECHNUNGSTEXT);

		// Default
		refreshEingangsrechnungsart();

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	/**
	 * Behandle ActionEvent; zB Menue-Klick oben.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 * @todo Implement this com.lp.client.frame.component.TabbedPane method PJ
	 *       5238
	 */
	protected void lPActionEvent(ActionEvent e) throws Throwable {
		// nothing here
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelEingangsrechnungsartQP1) {
				String cNr = (String) panelEingangsrechnungsartQP1
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr);
				panelEingangsrechnungsartBottomD1.setKeyWhenDetailPanel(cNr);
				panelEingangsrechnungsartBottomD1.eventYouAreSelected(false);
				panelEingangsrechnungsartQP1.updateButtons();
			} else if (e.getSource() == panelEingangsrechnungsstatusQP2) {
				String cNr = (String) panelEingangsrechnungsstatusQP2
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr);
				panelEingangsrechnungsstatusBottomD2.setKeyWhenDetailPanel(cNr);
				panelEingangsrechnungsstatusBottomD2.eventYouAreSelected(false);
				panelEingangsrechnungsstatusQP2.updateButtons();
			} else if (eI.getSource() == panelQueryEingangsrechnungtext) {
				Integer iId = (Integer) panelQueryEingangsrechnungtext
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelBottomEingangsrechnungtext.setKeyWhenDetailPanel(iId);
				panelBottomEingangsrechnungtext.eventYouAreSelected(false);
				panelQueryEingangsrechnungtext.updateButtons();
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelEingangsrechnungsartBottomD1) {
				panelEingangsrechnungsartBottomD1.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomEingangsrechnungtext) {
				panelSplitEingangsrechnungtext.eventYouAreSelected(false);
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelEingangsrechnungsartBottomD1) {
				Object oKey = panelEingangsrechnungsartBottomD1
						.getKeyWhenDetailPanel();
				panelEingangsrechnungsartQP1.eventYouAreSelected(false);
				panelEingangsrechnungsartQP1.setSelectedId(oKey);
				panelEingangsrechnungsartSP1.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomEingangsrechnungtext) {
				Object oKey = panelBottomEingangsrechnungtext
						.getKeyWhenDetailPanel();
				panelQueryEingangsrechnungtext.eventYouAreSelected(false);
				panelQueryEingangsrechnungtext.setSelectedId(oKey);
				panelSplitEingangsrechnungtext.eventYouAreSelected(false);
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelEingangsrechnungsartBottomD1) {
				panelEingangsrechnungsartSP1.eventYouAreSelected(false); // refresh
																			// auf
																			// das
																			// gesamte
																			// 1:n
																			// panel
			} else if (eI.getSource() == panelBottomEingangsrechnungtext) {
				panelSplitEingangsrechnungtext.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelEingangsrechnungsartQP1) {
				panelEingangsrechnungsartBottomD1
						.eventActionNew(e, true, false);
				panelEingangsrechnungsartBottomD1.eventYouAreSelected(false);
				setSelectedComponent(panelEingangsrechnungsartSP1);
			} else if (eI.getSource() == panelQueryEingangsrechnungtext) {
				if (panelQueryEingangsrechnungtext.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBottomEingangsrechnungtext.eventActionNew(eI, true, false);
				panelBottomEingangsrechnungtext.eventYouAreSelected(false);
				setSelectedComponent(panelSplitEingangsrechnungtext);
			}
		}
	}

	/**
	 * Behandle ChangeEvent; zB Tabwechsel oben.
	 * 
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 * @todo Implement this com.lp.client.frame.component.TabbedPane method PJ
	 *       5238
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_EINGANGSRECHNUNGART:
			refreshEingangsrechnungsart();
			panelEingangsrechnungsartSP1.eventYouAreSelected(false);
			break;
		case IDX_EINGANGSRECHNUNGSSTATUS:
			refreshEingangsrechnungsstatus();
			panelEingangsrechnungsstatusSP2.eventYouAreSelected(false);
			break;
		case IDX_EINGANGSRECHNUNGSTEXT:
			refreshPanelRechnungtext();
			panelQueryEingangsrechnungtext
					.setDefaultFilter(RechnungFilterFactory.getInstance()
							.createFKRechnungtext());
			panelQueryEingangsrechnungtext.eventYouAreSelected(false);
			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelQueryEingangsrechnungtext.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_EINGANGSRECHNUNGSTEXT, false);
			}
			break;

		}
	}

	private void refreshEingangsrechnungsart() throws Throwable {

		if (panelEingangsrechnungsartSP1 == null) {

			panelEingangsrechnungsartQP1 = new PanelQuery(null, null,
					QueryParameters.UC_ID_EINGANGSRECHNUNGART, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"er.tab.oben.eingangsrechnungsart"), true); // liste
																				// refresh
																				// wenn
																				// lasche
																				// geklickt
																				// wurde

			panelEingangsrechnungsartBottomD1 = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"er.tab.oben.eingangsrechnungsart"), null,
					HelperClient.SCRUD_EINGANGSRECHNUNGSART_FILE,
					(InternalFrameEingangsrechnung) getInternalFrame(),
					HelperClient.LOCKME_EINGANGSRECHNUNGSART);

			panelEingangsrechnungsartSP1 = new PanelSplit(getInternalFrame(),
					panelEingangsrechnungsartBottomD1,
					panelEingangsrechnungsartQP1, 200);
			setComponentAt(IDX_EINGANGSRECHNUNGART,
					panelEingangsrechnungsartSP1);

			// liste soll sofort angezeigt werden
			panelEingangsrechnungsartQP1.eventYouAreSelected(true);
		}
	}

	private void refreshPanelRechnungtext() throws Throwable {
		if (panelSplitEingangsrechnungtext == null) {

			// der Kopftext muss hinterlegt sein oder angelegt werden
			EingangsrechnungtextDto reTextDtoKopf = DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.eingangsrechnungtextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_KOPFTEXT);
			if (reTextDtoKopf == null) {
				DelegateFactory
						.getInstance()
						.getEingangsrechnungDelegate()
						.createDefaultEingangsrechnungtext(
								MediaFac.MEDIAART_KOPFTEXT,
								EingangsrechnungServiceFac.ER_DEFAULT_KOPFTEXT,
								LPMain.getInstance().getTheClient()
										.getLocUiAsString());
			}
			// der Fusstext muss hinterlegt sein oder angelegt werden
			EingangsrechnungtextDto reTextDtoFuss = DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.eingangsrechnungtextFindByMandantLocaleCNr(
							LPMain.getInstance().getTheClient()
									.getLocUiAsString(),
							MediaFac.MEDIAART_FUSSTEXT);
			if (reTextDtoFuss == null) {
				DelegateFactory
						.getInstance()
						.getEingangsrechnungDelegate()
						.createDefaultEingangsrechnungtext(
								MediaFac.MEDIAART_FUSSTEXT,
								EingangsrechnungServiceFac.ER_DEFAULT_FUSSTEXT,
								LPMain.getInstance().getTheClient()
										.getLocUiAsString());
			}
			String[] aWhichStandardButtonIUse = null;
			panelQueryEingangsrechnungtext = new PanelQuery(null, null,
					QueryParameters.UC_ID_EINGANGSRECHNUNGTEXT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"lp.eingangsrechnungtext"), true);
			panelBottomEingangsrechnungtext = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.eingangsrechnungtext"),
					null, HelperClient.SCRUD_EINGANGSRECHNUNGTEXT_FILE,
					getInternalFrame(),
					HelperClient.LOCKME_EINGANGSRECHNUNGTEXT);
			panelSplitEingangsrechnungtext = new PanelSplit(
					getInternalFrame(),
					panelBottomEingangsrechnungtext,
					panelQueryEingangsrechnungtext,
					400 - ((PanelStammdatenCRUD) panelBottomEingangsrechnungtext)
							.getAnzahlControls() * 30);
			setComponentAt(IDX_EINGANGSRECHNUNGSTEXT,
					panelSplitEingangsrechnungtext);
		}
	}

	private void refreshEingangsrechnungsstatus() throws Throwable {

		if (panelEingangsrechnungsstatusSP2 == null) {

			panelEingangsrechnungsstatusQP2 = new PanelQuery(null, null,
					QueryParameters.UC_ID_EINGANGSRECHNUNGSSTATUS, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"er.tab.oben.eingangsrechnungsstatus"),
					true); // liste refresh wenn lasche geklickt wurde

			panelEingangsrechnungsstatusBottomD2 = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"er.tab.oben.eingangsrechnungsstatus"),
					null, HelperClient.SCRUD_EINGANGSRECHNUNGSSTATUS_FILE,
					(InternalFrameEingangsrechnung) getInternalFrame(),
					HelperClient.LOCKME_EINGANGSRECHNUNGSSTATUS);

			panelEingangsrechnungsstatusSP2 = new PanelSplit(
					getInternalFrame(), panelEingangsrechnungsstatusBottomD2,
					panelEingangsrechnungsstatusQP2, 200);
			setComponentAt(IDX_EINGANGSRECHNUNGSSTATUS,
					panelEingangsrechnungsstatusSP2);

			// liste soll sofort angezeigt werden
			panelEingangsrechnungsstatusQP2.eventYouAreSelected(true);
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

}
