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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>
 * &UUml;berschrift:
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
 * @author Christian Kollmann
 * @version $Revision: 1.5 $
 */
public class TabbedPaneMehrereLoseErledigenStornieren extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryMehrereLoseErledigen = null;

	private final static int IDX_PANEL_AUSWAHL = 0;

	private final static String EXTRA_GOTO_AG = "goto_ag";

	private final String STORNIEREN = PanelBasis.ACTION_MY_OWN_NEW + "stornieren";

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneMehrereLoseErledigenStornieren(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr("fert.los.mehrereerledigen"));
		jbInit();
		initComponents();

	}

	public PanelQuery getPanelQueryWiederholende() {
		return panelQueryMehrereLoseErledigen;
	}

	private void jbInit() throws Throwable {

		insertTab(LPMain.getInstance().getTextRespectUISPr("fert.los.mehrereerledigen"), null, null,
				LPMain.getInstance().getTextRespectUISPr("fert.los.mehrereerledigen"), IDX_PANEL_AUSWAHL);

		createAuswahl();

		panelQueryMehrereLoseErledigen.eventYouAreSelected(false);

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryMehrereLoseErledigen, ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	private void createAuswahl() throws Throwable {

		if (panelQueryMehrereLoseErledigen == null) {

			String[] aWhichButtonIUse = null;

			FilterKriterium[] kriterien = null;

			kriterien = new FilterKriterium[2];

			kriterien[0] = new FilterKriterium("flrlos.mandant_c_nr", true,
					"'" + LPMain.getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL, false);

			kriterien[1] = new FilterKriterium("flrlos." + FertigungFac.FLR_LOS_STATUS_C_NR, true,
					"('" + FertigungFac.STATUS_ERLEDIGT + "','" + FertigungFac.STATUS_STORNIERT + "')",
					FilterKriterium.OPERATOR_NOT_IN, false);

			panelQueryMehrereLoseErledigen = new PanelQueryFLR(null, kriterien, QueryParameters.UC_ID_LOSE_ERLEDIGEN,
					aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("lp.auswahl"));

			FilterKriteriumDirekt fkDirekt1 = FertigungFilterFactory.getInstance().createFKDLosnummer();
			FilterKriteriumDirekt fkDirekt2 = FertigungFilterFactory.getInstance().createFKDVolltextsucheArtikel();

			// PJ17681

			Map mEingeschraenkteFertigungsgruppen = DelegateFactory.getInstance().getStuecklisteDelegate()
					.getEingeschraenkteFertigungsgruppen();

			if (mEingeschraenkteFertigungsgruppen != null) {
				panelQueryMehrereLoseErledigen.setFilterComboBox(mEingeschraenkteFertigungsgruppen,
						new FilterKriterium("flrlos.fertigungsgruppe_i_id", true, "" + "",
								FilterKriterium.OPERATOR_EQUAL, false),
						true, null, false);
				panelQueryMehrereLoseErledigen.eventActionRefresh(null, true);
			} else {

				panelQueryMehrereLoseErledigen.setFilterComboBox(
						DelegateFactory.getInstance().getStuecklisteDelegate().getAllFertigungsgrupe(),
						new FilterKriterium("flrlos.fertigungsgruppe_i_id", true, "" + "",
								FilterKriterium.OPERATOR_EQUAL, false),
						false, LPMain.getTextRespectUISPr("lp.alle"), false);
			}

			panelQueryMehrereLoseErledigen.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);

			panelQueryMehrereLoseErledigen
					.addDirektFilter(FertigungFilterFactory.getInstance().createFKDLosAuftagsnummer());

			panelQueryMehrereLoseErledigen.addDirektFilter(FertigungFilterFactory.getInstance().createFKDLosKunde());

			panelQueryMehrereLoseErledigen
					.addDirektFilter(FertigungFilterFactory.getInstance().createFKDLosBestellnummerAusAuftrag());
			panelQueryMehrereLoseErledigen
					.addDirektFilter(FertigungFilterFactory.getInstance().createFKDLosProjektbezeichnung());

			panelQueryMehrereLoseErledigen.setMultipleRowSelectionEnabled(true);
			panelQueryMehrereLoseErledigen.addButtonAuswahlBestaetigen(null,LPMain.getInstance().getTextRespectUISPr("fert.loseerledigen.ausgewaehlte.lose.erledigen"));

			panelQueryMehrereLoseErledigen.createAndSaveAndShowButton("/com/lp/client/res/document_delete.png",
					LPMain.getInstance().getTextRespectUISPr("fert.lose.mehrere.stornieren"),
					STORNIEREN, KeyStroke.getKeyStroke('D', java.awt.event.InputEvent.CTRL_MASK),null);

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryMehrereLoseErledigen);

		}
	}

	public InternalFrameFertigung getInternalFrameFertigung() {
		return (InternalFrameFertigung) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryMehrereLoseErledigen.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryMehrereLoseErledigen) {
				if (panelQueryMehrereLoseErledigen.getSelectedId() != null) {
					getInternalFrameFertigung().setKeyWasForLockMe(panelQueryMehrereLoseErledigen.getSelectedId() + "");

					if (panelQueryMehrereLoseErledigen.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
					} else {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, true);
					}
					if (panelQueryMehrereLoseErledigen.getHmOfButtons()
							.containsKey(PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_AG)) {
						panelQueryMehrereLoseErledigen.getHmOfButtons()
								.get(PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_AG).getButton().setEnabled(true);
					}

				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, true);
				}

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

		} else if ((e.getID() == ItemChangedEvent.ACTION_NEW) || (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW)) {
			// btnnew: einen neuen machen.

			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

			if (e.getSource() == panelQueryMehrereLoseErledigen) {
				// goto AG

				if (sAspectInfo.endsWith(PanelQuery.MY_OWN_NEW_EXTRA_ACTION_SPECIAL_OK)) {

					if (panelQueryMehrereLoseErledigen.getSelectedId() != null) {

						ArrayList<Integer> losIIds = panelQueryMehrereLoseErledigen.getSelectedIdsAsInteger();

						boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
								LPMain.getMessageTextRespectUISPr("fert.mehrerelose.erledigen.warning"));

						if (b == true) {

							TreeMap tmLose = new TreeMap();
							for (int i = 0; i < losIIds.size(); i++) {

								LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
										.losFindByPrimaryKey(losIIds.get(i));

								tmLose.put(losIIds.get(i), losDto);
							}

							Map mEingeschraenkteFertigungsgruppen = DelegateFactory.getInstance()
									.getStuecklisteDelegate().getEingeschraenkteFertigungsgruppen();

							Iterator it = tmLose.descendingKeySet().iterator();
							while (it.hasNext()) {

								LosDto losDto = (LosDto) tmLose.get(it.next());

								boolean bMaterialbuchungBeiAblieferung = false;
								if (losDto.getStuecklisteIId() != null) {

									StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
											.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId());

									bMaterialbuchungBeiAblieferung = Helper
											.short2Boolean(stklDto.getBMaterialbuchungbeiablieferung());
									// Geht derzeit nicht mit SNR/CHNR-Artikel
									if (stklDto.getArtikelDto().istArtikelSnrOderchargentragend()) {
										continue;
									}

								}

								// Nur bei Fertigungsgruppen, welche der
								// Benutzer auch
								// darf
								if (mEingeschraenkteFertigungsgruppen == null || mEingeschraenkteFertigungsgruppen
										.containsKey(losDto.getFertigungsgruppeIId())) {

									if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)
											|| losDto.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)
											|| losDto.getStatusCNr().equals(FertigungFac.STATUS_IN_PRODUKTION)
											|| losDto.getStatusCNr().equals(FertigungFac.STATUS_TEILERLEDIGT)
											|| losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {

										if (losDto.getStatusCNr().equals(FertigungFac.STATUS_ANGELEGT)) {
											DelegateFactory.getInstance().getFertigungDelegate().gebeLosAus(
													losDto.getIId(), false, false,
													getInternalFrameFertigung().getTabbedPaneLos()
															.getAbzubuchendeSeriennrChargen(losDto.getIId(),
																	losDto.getNLosgroesse(), false));
										}

										if (losDto.getStatusCNr().equals(FertigungFac.STATUS_GESTOPPT)) {
											DelegateFactory.getInstance().getFertigungDelegate()
													.stoppeProduktionRueckgaengig(losDto.getIId());
										}

										losDto = DelegateFactory.getInstance().getFertigungDelegate()
												.losFindByPrimaryKey(losDto.getIId());

										if (losDto.getStatusCNr().equals(FertigungFac.STATUS_AUSGEGEBEN)
												|| losDto.getStatusCNr().equals(FertigungFac.STATUS_IN_PRODUKTION)
												|| losDto.getStatusCNr().equals(FertigungFac.STATUS_TEILERLEDIGT)) {

											BigDecimal bdErledigt = DelegateFactory.getInstance().getFertigungDelegate()
													.getErledigteMenge(losDto.getIId());

											BigDecimal bdOffen = losDto.getNLosgroesse().subtract(bdErledigt);

											if (bdOffen.doubleValue() > 0) {

												if (bMaterialbuchungBeiAblieferung) {
													DelegateFactory.getInstance().getFertigungDelegate()
															.bucheMaterialAufLos(losDto, bdOffen, false, false, true,
																	getInternalFrameFertigung().getTabbedPaneLos()
																			.getAbzubuchendeSeriennrChargen(
																					losDto.getIId(), bdOffen, true));
												}

												LosablieferungDto laDto = new LosablieferungDto();

												laDto.setLosIId(losDto.getIId());
												laDto.setNMenge(bdOffen);

												DelegateFactory.getInstance().getFertigungDelegate()
														.createLosablieferung(laDto, true);

											} else {
												DelegateFactory.getInstance().getFertigungDelegate()
														.manuellErledigen(losDto.getIId(), false);

											}

										}

									}

								}

							}

							panelQueryMehrereLoseErledigen.eventActionRefresh(null, true);
						}
					}
				} else if (sAspectInfo.equals(STORNIEREN)) {
					if (panelQueryMehrereLoseErledigen.getSelectedId() != null) {

						ArrayList<Integer> losIIds = panelQueryMehrereLoseErledigen.getSelectedIdsAsInteger();

						boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
								LPMain.getMessageTextRespectUISPr("fert.lose.mehrere.stornieren.warning"));

						if (b == true) {

							for (int i = 0; i < losIIds.size(); i++) {

								Integer losIId = losIIds.get(i);

								LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
										.losFindByPrimaryKey(losIId);

								if (losDto.getStatusCNr().equals(LocaleFac.STATUS_ANGELEGT)) {

									if (!DelegateFactory.getInstance().getZeiterfassungDelegate()
											.sindBelegzeitenVorhanden(LocaleFac.BELEGART_LOS, losIId)) {
										DelegateFactory.getInstance().getFertigungDelegate()
												.storniereLos(losDto.getIId(), true);
									}
								}
							}

						}
					}
					panelQueryMehrereLoseErledigen.eventActionRefresh(null, true);
				}

			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_TABLE_SELECTION_CHANGED) {

		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1
				|| e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {

		}

	}

	private void refreshTitle() {
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("fert.los.mehrereerledigen"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryMehrereLoseErledigen.eventYouAreSelected(false);

			panelQueryMehrereLoseErledigen.updateButtons();
		}

		refreshTitle();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		setName("TabbedPaneMehrereLoseErledigen");
		if (wrapperMenuBar == null) {

			wrapperMenuBar = new WrapperMenuBar(this);

		}

		return wrapperMenuBar;

	}

}
