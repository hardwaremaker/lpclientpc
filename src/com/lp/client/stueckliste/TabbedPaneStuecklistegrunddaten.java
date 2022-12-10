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
package com.lp.client.stueckliste;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.5 $
 */
public class TabbedPaneStuecklistegrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryMontageart = null;
	private PanelBasis panelSplitMontageart = null;
	private PanelBasis panelBottomMontageart = null;

	private PanelQuery panelQueryStuecklisteeigenschaftart = null;
	private PanelBasis panelSplitStuecklisteeigenschaftart = null;
	private PanelBasis panelBottomStuecklisteeigenschaftart = null;

	private PanelQuery panelQueryFertigungsgruppe = null;
	private PanelBasis panelSplitFertigungsgruppe = null;
	private PanelBasis panelBottomFertigungsgruppe = null;

	private PanelQuery panelQueryKommentarimport = null;
	private PanelBasis panelSplitKommentarimport = null;
	private PanelKommentarimport panelBottomKommentarimport = null;

	private static int IDX_PANEL_MONTAGEART = 0;
	private static int IDX_PANEL_STUECKLISTEEIGENSCHAFTART = 1;
	private static int IDX_PANEL_FERTIGUNGSGRUPPE = 2;
	private static int IDX_PANEL_KOMMENTARIMPORT = 3;
	private static int IDX_PANEL_SCRIPTART = 4;
	private static int IDX_PANEL_APKOMMENTAR = 5;
	private static int IDX_PANEL_PRUEFART = 6;
	private static int IDX_PANEL_PRUEFKOMBINATION = 7;

	private WrapperMenuBar wrapperMenuBar = null;

	private PanelQuery panelQueryScriptart = null;
	private PanelBasis panelSplitScriptart = null;
	private PanelBasis panelBottomScriptart = null;

	private PanelQuery panelQueryApkommentar = null;
	private PanelBasis panelSplitApkommentar = null;
	private PanelBasis panelBottomApkommentar = null;

	private PanelQuery panelQueryPruefart = null;
	private PanelBasis panelSplitPruefart = null;
	private PanelBasis panelBottomPruefart = null;

	private PanelQuery panelQueryPruefkombination = null;
	private PanelBasis panelSplitPruefkombination = null;
	private PanelBasis panelBottomPruefkombination = null;

	private static final String ACTION_SPECIAL_XLSIMPORT_PRUEFKOMBINATION = "ACTION_SPECIAL_XLSIMPORT_PRUEFKOMBINATION";
	private final String BUTTON_XLSIMPORT_PRUEFKOMBINATION = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_XLSIMPORT_PRUEFKOMBINATION;

	public TabbedPaneStuecklistegrunddaten(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"pers.title.tab.grunddaten"));

		jbInit();
		initComponents();
	}

	public InternalFrameStueckliste getInternalFrameStueckliste() {
		return (InternalFrameStueckliste) getInternalFrame();
	}

	private void createMontageart() throws Throwable {
		if (panelSplitMontageart == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };
			panelQueryMontageart = new PanelQuery(StuecklisteFilterFactory
					.getInstance().createQTMontageart(),
					com.lp.client.system.SystemFilterFactory.getInstance()
							.createFKMandantCNr(),
					QueryParameters.UC_ID_STUECKLISTEMONTAGEART,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("stkl.montageart"), true);

			panelBottomMontageart = new PanelMontageart(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("stkl.montageart"),
					null);
			panelSplitMontageart = new PanelSplit(getInternalFrame(),
					panelBottomMontageart, panelQueryMontageart, 325);

			setComponentAt(IDX_PANEL_MONTAGEART, panelSplitMontageart);
		}
	}

	private void createFertigungsgruppe() throws Throwable {
		if (panelSplitFertigungsgruppe == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };
			panelQueryFertigungsgruppe = new PanelQuery(
					StuecklisteFilterFactory.getInstance()
							.createQTFertigungsgruppe(),
					com.lp.client.system.SystemFilterFactory.getInstance()
							.createFKMandantCNr(),
					QueryParameters.UC_ID_FERTIGUNGSGRUPPE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("stkl.fertigungsgruppe"), true);

			panelBottomFertigungsgruppe = new PanelFertigungsgruppe(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("stkl.fertigungsgruppe"), null);
			panelSplitFertigungsgruppe = new PanelSplit(getInternalFrame(),
					panelBottomFertigungsgruppe, panelQueryFertigungsgruppe,
					350);

			setComponentAt(IDX_PANEL_FERTIGUNGSGRUPPE,
					panelSplitFertigungsgruppe);
		}
	}

	private void createKommentartimport() throws Throwable {
		if (panelSplitKommentarimport == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					};
			panelQueryKommentarimport = new PanelQuery(null, null,
					QueryParameters.UC_ID_KOMMENTARIMPORT, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("stk.kommentarimport"), true);

			panelBottomKommentarimport = new PanelKommentarimport(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("stk.kommentarimport"), null);
			panelSplitKommentarimport = new PanelSplit(getInternalFrame(),
					panelBottomKommentarimport, panelQueryKommentarimport, 350);

			setComponentAt(IDX_PANEL_KOMMENTARIMPORT, panelSplitKommentarimport);
		}
	}

	private void createPruefkombination() throws Throwable {
		if (panelSplitPruefkombination == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_PRINT };
			panelQueryPruefkombination = new PanelQuery(null, null,
					QueryParameters.UC_ID_PRUEFKOMBINATION, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("stk.pruefkombination"), true);
			panelQueryPruefkombination.setMultipleRowSelectionEnabled(true);
			panelBottomPruefkombination = new PanelPruefkombination(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("stk.pruefkombination"), null,
					panelQueryPruefkombination);
			panelSplitPruefkombination = new PanelSplit(getInternalFrame(),
					panelBottomPruefkombination, panelQueryPruefkombination,
					190);

			panelQueryPruefkombination.createAndSaveAndShowButton(
					"/com/lp/client/res/document_into.png",
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.verschleissteile.xlsimport"),
					BUTTON_XLSIMPORT_PRUEFKOMBINATION, null);

			panelQueryPruefkombination.befuellePanelFilterkriterienDirekt(
					StuecklisteFilterFactory.getInstance()
							.createFKDArtikelnummerKontakt(),
					StuecklisteFilterFactory.getInstance().createFKDWerkzeug());

			panelQueryPruefkombination.addDirektFilter(StuecklisteFilterFactory
					.getInstance().createFKDArtikelnummerLitze());

			panelQueryPruefkombination.addDirektFilter(StuecklisteFilterFactory
					.getInstance().createFKDVolltextLitze());

			setComponentAt(IDX_PANEL_PRUEFKOMBINATION,
					panelSplitPruefkombination);
		}
	}

	private void createStuecklisteeigenschaftart() throws Throwable {
		if (panelSplitStuecklisteeigenschaftart == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			panelQueryStuecklisteeigenschaftart = new PanelQuery(
					StuecklisteFilterFactory.getInstance()
							.createQTStuekliseeigenschaftart(), null,
					QueryParameters.UC_ID_STUECKLISTEEIGENSCHAFTART,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"stkl.stuecklisteeigenschaftart"), true);
			panelBottomStuecklisteeigenschaftart = new PanelStuecklisteeigenschaftart(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"stkl.stuecklisteeigenschaftart"), null);

			panelSplitStuecklisteeigenschaftart = new PanelSplit(
					getInternalFrame(), panelBottomStuecklisteeigenschaftart,
					panelQueryStuecklisteeigenschaftart, 400);

			setComponentAt(IDX_PANEL_STUECKLISTEEIGENSCHAFTART,
					panelSplitStuecklisteeigenschaftart);
		}
	}

	private void createScriptart() throws Throwable {
		if (panelSplitScriptart == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };
			panelQueryScriptart = new PanelQuery(StuecklisteFilterFactory
					.getInstance().createQTScriptart(),
					com.lp.client.system.SystemFilterFactory.getInstance()
							.createFKMandantCNr(),
					QueryParameters.UC_ID_STUECKLISTESCRIPTART,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("stkl.scriptart"), true);

			panelBottomScriptart = new PanelStuecklisteScriptart(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("stkl.scriptart"), null);
			panelSplitScriptart = new PanelSplit(getInternalFrame(),
					panelBottomScriptart, panelQueryScriptart, 350);

			setComponentAt(IDX_PANEL_SCRIPTART, panelSplitScriptart);
		}
	}

	private void createApkommentar() throws Throwable {
		if (panelSplitApkommentar == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryApkommentar = new PanelQuery(null,
					com.lp.client.system.SystemFilterFactory.getInstance()
							.createFKMandantCNr(),
					QueryParameters.UC_ID_APKOMMENTAR, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("stkl.arbeitsplankommentar"),
					true);

			panelBottomApkommentar = new PanelApkommentar(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"stkl.arbeitsplankommentar"), null);

			panelSplitApkommentar = new PanelSplit(getInternalFrame(),
					panelBottomApkommentar, panelQueryApkommentar, 350);

			setComponentAt(IDX_PANEL_APKOMMENTAR, panelSplitApkommentar);
		}
	}

	private void createPruefart() throws Throwable {
		if (panelSplitPruefart == null) {
			String[] aWhichButtonIUse = {};
			panelQueryPruefart = new PanelQuery(null, null,
					QueryParameters.UC_ID_PRUEFART, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("stkl.pruefart"), true);

			panelBottomPruefart = new PanelPruefart(getInternalFrame(), LPMain
					.getInstance().getTextRespectUISPr("stkl.pruefart"), null);

			panelSplitPruefart = new PanelSplit(getInternalFrame(),
					panelBottomPruefart, panelQueryPruefart, 350);

			setComponentAt(IDX_PANEL_PRUEFART, panelSplitPruefart);
		}
	}

	private void jbInit() throws Throwable {
		// Kollektiv

		// 1 tab oben: QP1 PartnerFLR; lazy loading
		insertTab(LPMain.getInstance().getTextRespectUISPr("stkl.montageart"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("stkl.montageart"),
				IDX_PANEL_MONTAGEART);

		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"stkl.stuecklisteeigenschaftart"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"stkl.stuecklisteeigenschaftart"),
				IDX_PANEL_STUECKLISTEEIGENSCHAFTART);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"stkl.fertigungsgruppe"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"stkl.fertigungsgruppe"), IDX_PANEL_FERTIGUNGSGRUPPE);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr("stk.kommentarimport"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr("stk.kommentarimport"),
				IDX_PANEL_KOMMENTARIMPORT);

		insertTab(LPMain.getInstance().getTextRespectUISPr("stkl.scriptart"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("stkl.scriptart"),
				IDX_PANEL_SCRIPTART);
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"stkl.arbeitsplankommentar"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"stkl.arbeitsplankommentar"), IDX_PANEL_APKOMMENTAR);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_PRUEFPLAN1)) {

			insertTab(
					LPMain.getInstance().getTextRespectUISPr("stkl.pruefart"),
					null, null,
					LPMain.getInstance().getTextRespectUISPr("stkl.pruefart"),
					IDX_PANEL_PRUEFART);
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"stk.pruefkombination"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"stk.pruefkombination"), IDX_PANEL_PRUEFKOMBINATION);
		}

		createMontageart();
		createScriptart();

		// Itemevents an MEIN Detailpanel senden kann.
		refreshTitle();
		this.addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryMontageart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomMontageart.setKeyWhenDetailPanel(key);
				panelBottomMontageart.eventYouAreSelected(false);
				panelQueryMontageart.updateButtons();
			} else if (e.getSource() == panelQueryStuecklisteeigenschaftart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomStuecklisteeigenschaftart.setKeyWhenDetailPanel(key);
				panelBottomStuecklisteeigenschaftart.eventYouAreSelected(false);
				panelQueryStuecklisteeigenschaftart.updateButtons();
			} else if (e.getSource() == panelQueryFertigungsgruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomFertigungsgruppe.setKeyWhenDetailPanel(key);
				panelBottomFertigungsgruppe.eventYouAreSelected(false);
				panelQueryFertigungsgruppe.updateButtons();
			} else if (e.getSource() == panelQueryKommentarimport) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomKommentarimport.setKeyWhenDetailPanel(key);
				panelBottomKommentarimport.eventYouAreSelected(false);
				panelQueryKommentarimport.updateButtons();
			} else if (e.getSource() == panelQueryScriptart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomScriptart.setKeyWhenDetailPanel(key);
				panelBottomScriptart.eventYouAreSelected(false);
				panelQueryScriptart.updateButtons();
			} else if (e.getSource() == panelQueryApkommentar) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomApkommentar.setKeyWhenDetailPanel(key);
				panelBottomApkommentar.eventYouAreSelected(false);
				panelQueryApkommentar.updateButtons();
			} else if (e.getSource() == panelQueryPruefkombination) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomPruefkombination.setKeyWhenDetailPanel(key);
				panelBottomPruefkombination.eventYouAreSelected(false);
				panelQueryPruefkombination.updateButtons();
			} else if (e.getSource() == panelQueryPruefart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomPruefart.setKeyWhenDetailPanel(key);
				panelBottomPruefart.eventYouAreSelected(false);
				panelQueryPruefart.updateButtons();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			panelSplitMontageart.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomMontageart) {
				panelQueryMontageart.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomStuecklisteeigenschaftart) {
				panelQueryStuecklisteeigenschaftart
						.updateButtons(new LockStateValue(
								PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomFertigungsgruppe) {
				panelQueryFertigungsgruppe.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomKommentarimport) {
				panelQueryKommentarimport.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomScriptart) {
				panelQueryScriptart.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomApkommentar) {
				panelQueryApkommentar.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomPruefkombination) {
				panelQueryPruefkombination.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomPruefart) {
				panelQueryPruefart.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == panelQueryMontageart) {
				int iPos = panelQueryMontageart.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryMontageart
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryMontageart
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.vertauscheMontageart(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryMontageart.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryStuecklisteeigenschaftart) {
				int iPos = panelQueryStuecklisteeigenschaftart.getTable()
						.getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryStuecklisteeigenschaftart
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryStuecklisteeigenschaftart
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.vertauscheStuecklisteeigenschaftart(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryStuecklisteeigenschaftart
							.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryScriptart) {
				int iPos = panelQueryScriptart.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryScriptart
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryScriptart
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.vertauscheStuecklisteScriptart(iIdPosition,
									iIdPositionMinus1);
					panelQueryScriptart.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == panelQueryMontageart) {
				int iPos = panelQueryMontageart.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryMontageart.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryMontageart
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryMontageart
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.vertauscheMontageart(iIdPosition, iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryMontageart.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryStuecklisteeigenschaftart) {
				int iPos = panelQueryStuecklisteeigenschaftart.getTable()
						.getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryStuecklisteeigenschaftart.getTable()
						.getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryStuecklisteeigenschaftart
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryStuecklisteeigenschaftart
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.vertauscheStuecklisteeigenschaftart(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryStuecklisteeigenschaftart
							.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryScriptart) {
				int iPos = panelQueryScriptart.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryScriptart.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryScriptart
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryScriptart
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.vertauscheStuecklisteScriptart(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryScriptart.setSelectedId(iIdPosition);
				}
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (e.getSource() == panelQueryMontageart) {
				panelBottomMontageart.eventActionNew(e, true, false);
				panelBottomMontageart.eventYouAreSelected(false); // Buttons
																	// schalten
			} else if (e.getSource() == panelQueryStuecklisteeigenschaftart) {
				panelBottomStuecklisteeigenschaftart.eventActionNew(e, true,
						false);
				panelBottomStuecklisteeigenschaftart.eventYouAreSelected(false); // Buttons
																					// schalten
			} else if (e.getSource() == panelQueryScriptart) {
				panelBottomScriptart.eventActionNew(e, true, false);
				panelBottomScriptart.eventYouAreSelected(false); // Buttons
			} else if (e.getSource() == panelQueryApkommentar) {
				panelBottomApkommentar.eventActionNew(e, true, false);
				panelBottomApkommentar.eventYouAreSelected(false); // Buttons
			} else if (e.getSource() == panelQueryPruefkombination) {
				panelBottomPruefkombination.eventActionNew(e, true, false);
				panelBottomPruefkombination.eventYouAreSelected(false); // Buttons
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryMontageart) {
				panelBottomMontageart.eventActionNew(e, true, false);
				panelBottomMontageart.eventYouAreSelected(false);
			}

			else if (e.getSource() == panelQueryStuecklisteeigenschaftart) {
				panelBottomStuecklisteeigenschaftart.eventActionNew(e, true,
						false);
				panelBottomStuecklisteeigenschaftart.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryFertigungsgruppe) {
				panelBottomFertigungsgruppe.eventActionNew(e, true, false);
				panelBottomFertigungsgruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryKommentarimport) {
				panelBottomKommentarimport.eventActionNew(e, true, false);
				panelBottomKommentarimport.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryScriptart) {
				panelBottomScriptart.eventActionNew(e, true, false);
				panelBottomScriptart.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryApkommentar) {
				panelBottomApkommentar.eventActionNew(e, true, false);
				panelBottomApkommentar.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryPruefkombination) {
				panelBottomPruefkombination.eventActionNew(e, true, false);
				panelBottomPruefkombination.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomMontageart) {
				panelSplitMontageart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomStuecklisteeigenschaftart) {
				panelSplitStuecklisteeigenschaftart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomFertigungsgruppe) {
				panelSplitFertigungsgruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomKommentarimport) {
				panelSplitKommentarimport.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomScriptart) {
				panelSplitScriptart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomApkommentar) {
				panelSplitApkommentar.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPruefkombination) {
				panelSplitPruefkombination.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPruefart) {
				panelSplitPruefart.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomMontageart) {
				Object oKey = panelBottomMontageart.getKeyWhenDetailPanel();
				panelQueryMontageart.eventYouAreSelected(false);
				panelQueryMontageart.setSelectedId(oKey);
				panelSplitMontageart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomStuecklisteeigenschaftart) {
				Object oKey = panelBottomStuecklisteeigenschaftart
						.getKeyWhenDetailPanel();
				panelQueryStuecklisteeigenschaftart.eventYouAreSelected(false);
				panelQueryStuecklisteeigenschaftart.setSelectedId(oKey);
				panelSplitStuecklisteeigenschaftart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomFertigungsgruppe) {
				Object oKey = panelBottomFertigungsgruppe
						.getKeyWhenDetailPanel();
				panelQueryFertigungsgruppe.eventYouAreSelected(false);
				panelQueryFertigungsgruppe.setSelectedId(oKey);
				panelSplitFertigungsgruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomKommentarimport) {
				Object oKey = panelBottomKommentarimport
						.getKeyWhenDetailPanel();
				panelQueryKommentarimport.eventYouAreSelected(false);
				panelQueryKommentarimport.setSelectedId(oKey);
				panelSplitKommentarimport.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomScriptart) {
				Object oKey = panelBottomScriptart.getKeyWhenDetailPanel();
				panelQueryScriptart.eventYouAreSelected(false);
				panelQueryScriptart.setSelectedId(oKey);
				panelQueryScriptart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomApkommentar) {
				Object oKey = panelBottomApkommentar.getKeyWhenDetailPanel();
				panelQueryApkommentar.eventYouAreSelected(false);
				panelQueryApkommentar.setSelectedId(oKey);
				panelQueryApkommentar.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPruefkombination) {
				Object oKey = panelBottomPruefkombination
						.getKeyWhenDetailPanel();
				panelQueryPruefkombination.eventYouAreSelected(false);
				panelQueryPruefkombination.setSelectedId(oKey);
				panelQueryPruefkombination.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPruefart) {
				Object oKey = panelBottomPruefart.getKeyWhenDetailPanel();
				panelQueryPruefart.eventYouAreSelected(false);
				panelQueryPruefart.setSelectedId(oKey);
				panelQueryPruefart.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomMontageart) {
				Object oKey = panelQueryMontageart.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomMontageart.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryMontageart
							.getId2SelectAfterDelete();
					panelQueryMontageart.setSelectedId(oNaechster);
				}
				panelSplitMontageart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomStuecklisteeigenschaftart) {
				Object oKey = panelQueryStuecklisteeigenschaftart
						.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomStuecklisteeigenschaftart
						.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryStuecklisteeigenschaftart
							.getId2SelectAfterDelete();
					panelQueryStuecklisteeigenschaftart
							.setSelectedId(oNaechster);
				}

				panelSplitStuecklisteeigenschaftart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomFertigungsgruppe) {
				Object oKey = panelQueryFertigungsgruppe.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomFertigungsgruppe.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryFertigungsgruppe
							.getId2SelectAfterDelete();
					panelQueryFertigungsgruppe.setSelectedId(oNaechster);
				}

				panelSplitFertigungsgruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomKommentarimport) {
				Object oKey = panelQueryKommentarimport.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomKommentarimport.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryKommentarimport
							.getId2SelectAfterDelete();
					panelQueryKommentarimport.setSelectedId(oNaechster);
				}

				panelSplitKommentarimport.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomScriptart) {
				Object oKey = panelQueryScriptart.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomScriptart.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryScriptart
							.getId2SelectAfterDelete();
					panelQueryScriptart.setSelectedId(oNaechster);
				}
				panelSplitScriptart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomApkommentar) {
				Object oKey = panelQueryApkommentar.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomApkommentar.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryApkommentar
							.getId2SelectAfterDelete();
					panelQueryApkommentar.setSelectedId(oNaechster);
				}
				panelSplitApkommentar.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPruefart) {
				Object oKey = panelQueryPruefart.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomPruefart.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPruefart
							.getId2SelectAfterDelete();
					panelQueryPruefart.setSelectedId(oNaechster);
				}
				panelSplitPruefart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPruefkombination) {
				Object oKey = panelQueryPruefkombination.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomPruefkombination.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPruefkombination
							.getId2SelectAfterDelete();
					panelQueryPruefkombination.setSelectedId(oNaechster);
				}
				panelSplitPruefkombination.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_PRINT) {
			if (e.getSource() == panelQueryPruefkombination) {
				String add2Title = LPMain.getInstance().getTextRespectUISPr(
						"stk.pruefkombination");
				getInternalFrame().showReportKriterien(
						new ReportPruefkombination(
								getInternalFrameStueckliste(), add2Title));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (sAspectInfo.endsWith(ACTION_SPECIAL_XLSIMPORT_PRUEFKOMBINATION)) {
				DialogPruefkombinationImportXLS d = new DialogPruefkombinationImportXLS(
						this);
			}
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(
				InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.title.tab.grunddaten"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		getInternalFrame().setLpTitle(3, "");
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		DelegateFactory.getInstance().getStuecklisteDelegate()
				.removeLockDerPruefkombinationWennIchIhnSperre();

		if (selectedIndex == IDX_PANEL_MONTAGEART) {
			createMontageart();
			panelSplitMontageart.eventYouAreSelected(false);
			panelQueryMontageart.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_STUECKLISTEEIGENSCHAFTART) {
			createStuecklisteeigenschaftart();
			panelSplitStuecklisteeigenschaftart.eventYouAreSelected(false);
			panelQueryStuecklisteeigenschaftart.updateButtons();
		} else if (selectedIndex == IDX_PANEL_FERTIGUNGSGRUPPE) {
			createFertigungsgruppe();
			panelSplitFertigungsgruppe.eventYouAreSelected(false);
			panelQueryFertigungsgruppe.updateButtons();
		} else if (selectedIndex == IDX_PANEL_KOMMENTARIMPORT) {
			createKommentartimport();
			panelSplitKommentarimport.eventYouAreSelected(false);
			panelQueryKommentarimport.updateButtons();
		} else if (selectedIndex == IDX_PANEL_SCRIPTART) {
			createScriptart();
			panelSplitScriptart.eventYouAreSelected(false);
			panelQueryScriptart.updateButtons();
		} else if (selectedIndex == IDX_PANEL_APKOMMENTAR) {
			createApkommentar();
			panelSplitApkommentar.eventYouAreSelected(false);
			panelQueryApkommentar.updateButtons();
		} else if (selectedIndex == IDX_PANEL_PRUEFART) {
			createPruefart();
			panelSplitPruefart.eventYouAreSelected(false);
			panelQueryPruefart.updateButtons();
		} else if (selectedIndex == IDX_PANEL_PRUEFKOMBINATION) {

			try {
				DelegateFactory.getInstance().getStuecklisteDelegate()
						.pruefeBearbeitenDerPruefkombinationErlaubt();
			} catch (ExceptionLP ex) {
				if (ex.getICode() == EJBExceptionLP.FEHLER_PRUEFKOMBINATION_IST_GESPERRT) {

					String cWer = "";
					if (ex.getAlInfoForTheClient() != null
							&& ex.getAlInfoForTheClient().size() > 0) {
						cWer = (String) ex.getAlInfoForTheClient().get(0);
					}

					DialogFactory.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr(
									"lp.warning"),
							LPMain.getInstance().getTextRespectUISPr(
									"stk.pruefkombinationgesperrt")
									+ "\n" + cWer);
					setSelectedIndex(0);
					return;
				} else {
					handleException(ex, false);
				}

			}

			createPruefkombination();
			panelSplitPruefkombination.eventYouAreSelected(false);
			panelQueryPruefkombination.updateButtons();
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);
		}
		return wrapperMenuBar;
	}
}
