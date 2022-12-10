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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;

import com.lp.client.bestellung.DialogStandortAuswaehlen;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.DialogZuletztErzeugt;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.InternebestellungDto;
import com.lp.server.fertigung.service.InternebestellungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

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
public class TabbedPaneInternebestellung extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryInternebestellung = null;
	private PanelSplit panelSplitInternebestellung = null;
	private PanelInternebestellung panelDetailInternebestellung = null;

	private PanelTabelleBewegungsvorschau panelTabelleBewegungsvorschau = null;

	private InternebestellungDto internebestellungDto = null;
	private StuecklisteDto stuecklisteDto = null;

	private PanelDialogKriterienInternebestellung panelDialogKriterienInternebestellung = null;

	private static final int IDX_PANEL_INTERNEBESTELLUNGBESTELLUNG = 0;
	private static final int IDX_PANEL_BEWEGUNGSVORSCHAU = 1;

	private static final String ACTION_SPECIAL_NEUE_INTERNEBESTELLUNG = PanelBasis.ALWAYSENABLED
			+ "action_special_neue_internebestellung";
	private static final String ACTION_SPECIAL_LOSE_ANLEGEN = PanelBasis.ALWAYSENABLED + "action_special_lose_anlegen";
	private static final String ACTION_SPECIAL_VERDICHTEN = PanelBasis.ALWAYSENABLED + "action_special_verdichten";

	private final static String MENUE_ACTION_BEARBEITEN_BEGINN_ENDE_AENDERN = "menu_action_bearbeiten_beginn_ende_aendern";

	private static final String ACTION_SPECIAL_UEBERPRODUKTION_REDUZIEREN = PanelBasis.ALWAYSENABLED
			+ "action_special_ueberproduktion_reduzieren";

	private static final String MENU_INFO_ZULETZT_ERZEUGT = "MENU_INFO_ZULETZT_ERZEUGT";

	private static final String MENUE_ACTION_BEDARFSZUSAMMENSCHAU = "MENUE_ACTION_BEDARFSZUSAMMENSCHAU";

	private final static String MENUE_ACTION_BEARBEITEN_LOESCHE_ANGELEGTE_UND_STORNIERTE_LOSE = "menu_action_bearbeiten_loesche_angelegte_und_stornierte_lose";

	private boolean bInterneBestellungVerdichtenMitRahmenpruefung = false;

	private WrapperMenuBar wrapperMenuBar = null;

	private Map mEingeschraenkteFertigungsgruppen = DelegateFactory.getInstance().getStuecklisteDelegate()
			.getEingeschraenkteFertigungsgruppen();

	public TabbedPaneInternebestellung(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("fert.tab.oben.internebestellung.title"));

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_INT_BEST_VERDICHTEN_RAHMENPRUEFUNG);
		bInterneBestellungVerdichtenMitRahmenpruefung = (java.lang.Boolean) parameter.getCWertAsObject();

		jbInit();
		initComponents();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		insertTab(LPMain.getTextRespectUISPr("fert.tab.oben.internebestellung.title"), null, null,
				LPMain.getTextRespectUISPr("fert.tab.oben.internebestellung.tooltip"),
				IDX_PANEL_INTERNEBESTELLUNGBESTELLUNG);

		insertTab(LPMain.getTextRespectUISPr("fert.tab.oben.bewegungsvorschau.title"), null, null,
				LPMain.getTextRespectUISPr("fert.tab.oben.bewegungsvorschau.tooltip"), IDX_PANEL_BEWEGUNGSVORSCHAU);

		getPanelSplitInternebestellung(true);

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	protected InternebestellungDto getInternebestellungDto() {
		return internebestellungDto;
	}

	protected void setInternebestellungDto(InternebestellungDto internebestellungDto) throws Throwable {
		this.internebestellungDto = internebestellungDto;
		if (internebestellungDto != null) {
			refreshFilterBewegungsvorschau();
			// Stueckliste nur dann neu laden, wenn sie nicht eh schon da ist.
			if (this.getStuecklisteDto() == null
					|| !this.getStuecklisteDto().getIId().equals(internebestellungDto.getStuecklisteIId())) {
				this.setStuecklisteDto(DelegateFactory.getInstance().getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(internebestellungDto.getStuecklisteIId()));
			}
		}
	}

	private void refreshFilterBewegungsvorschau() throws Throwable {
		if (getInternebestellungDto() != null) {
			if (getPanelTabelleBewegungsvorschau(false) != null) {
				FilterKriterium[] krit = FertigungFilterFactory.getInstance()
						.createFKBewegungsvorschau(getStuecklisteDto().getArtikelIId(), true, false, null);
				getPanelTabelleBewegungsvorschau(true).setDefaultFilter(krit);
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
		if (panelDialogKriterienInternebestellung != null) {
			panelDialogKriterienInternebestellung.eventItemchanged(e);
		}

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == getPanelQueryInternebestellung(false)) {
				// bei Doppelklick auf die Bewegungsvorschau wechseln
				setSelectedComponent(getPanelTabelleBewegungsvorschau(true));
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == getPanelQueryInternebestellung(false)) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_INTERNEBESTELLUNGBESTELLUNG, false);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_INTERNEBESTELLUNGBESTELLUNG, true);
				}

				// Anzahl
				int count = panelQueryInternebestellung.getTable().getRowCount();
				panelDetailInternebestellung.setAnzahlEintraege(
						LPMain.getTextRespectUISPr("fert.internebestellung.anzahl.eintraege") + " " + count);

				//
				holeInternebestellungDto(key);
				getPanelDetailInternebestellung(true).eventYouAreSelected(false);
				// Buttons richtig schalten
				getPanelQueryInternebestellung(true).updateButtons();

				// Ueberleitung nur aktiviert wenn nicht leer
				LPButtonAction item = (LPButtonAction) getPanelQueryInternebestellung(true).getHmOfButtons()
						.get(ACTION_SPECIAL_LOSE_ANLEGEN);
				if (getPanelQueryInternebestellung(true).getTable().getRowCount() > 0) {
					item.getButton().setEnabled(true);
				} else {
					item.getButton().setEnabled(false);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (e.getSource() == getPanelDetailInternebestellung(false)) {
				// im QP die Buttons in den Zustand neu setzen.
				getPanelQueryInternebestellung(false).updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == getPanelDetailInternebestellung(false)) {
				getPanelSplitInternebestellung(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == getPanelDetailInternebestellung(false)) {
				Object oKey = getPanelDetailInternebestellung(true).getKeyWhenDetailPanel();
				getPanelQueryInternebestellung(true).eventYouAreSelected(false);
				getPanelQueryInternebestellung(true).setSelectedId(oKey);
				getPanelSplitInternebestellung(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == getPanelDetailInternebestellung(false)) {
				setKeyWasForLockMe();

				if (getPanelDetailInternebestellung(true).getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryInternebestellung(true).getId2SelectAfterDelete();
					getPanelQueryInternebestellung(true).setSelectedId(oNaechster);
				}

				getPanelSplitInternebestellung(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == getPanelQueryInternebestellung(true)) {
				getPanelDetailInternebestellung(true).eventActionNew(e, true, false);
				getPanelDetailInternebestellung(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == getPanelDialogKriterienIB()) {
				DelegateFactory.getInstance().getFertigungDelegate().erzeugeInterneBestellung(
						getPanelDialogKriterienIB().getVorhandeneLoeschen(),
						getPanelDialogKriterienIB().getAuftragsvorlaufzeit(),
						getPanelDialogKriterienIB().getVorlaufzeitUnterlose(),
						getPanelDialogKriterienIB().getToleranz(),
						getPanelDialogKriterienIB().getLieferterminFuerArtikelOhneReservierung(),
						getPanelDialogKriterienIB().getBVerdichten(), getPanelDialogKriterienIB().getVerdichtungstage(),
						getPanelDialogKriterienIB().getLosIIds(),
						getPanelDialogKriterienIB().getBMitNichtFreigegebeneAuftraegen(),
						getPanelDialogKriterienIB().getAuftragIIds(),
						getPanelDialogKriterienIB().getFertigungsgruppeIId(),
						getPanelDialogKriterienIB().getBExakterAuftragsbezug());
				getPanelQueryInternebestellung(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (sAspectInfo.equals(ACTION_SPECIAL_NEUE_INTERNEBESTELLUNG)) {
				getInternalFrame().showPanelDialog(getPanelDialogKriterienIB());
			} else if (sAspectInfo.equals(ACTION_SPECIAL_UEBERPRODUKTION_REDUZIEREN)) {

				// Warnung
				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("fert.internebestellung.ueberproduktion.reduzieren.warnung"));
				if (b == true) {
					Object[] aOptionen = new Object[2];
					aOptionen[0] = LPMain.getTextRespectUISPr("fert.internebestellung.ueberproduktion.reduzieren.nein");
					aOptionen[1] = LPMain.getTextRespectUISPr("fert.internebestellung.ueberproduktion.reduzieren.ja");

					int iAuswahl = DialogFactory.showModalDialog(getInternalFrame(),
							LPMain.getTextRespectUISPr("fert.internebestellung.ueberproduktion.reduzieren.warnung2"),
							LPMain.getTextRespectUISPr("lp.warning"), aOptionen, aOptionen[0]);

					if (iAuswahl == 1) {
						DelegateFactory.getInstance().getFertigungDelegate().ueberproduktionZuruecknehmen();
					}
				}

			} else if (sAspectInfo.equals(ACTION_SPECIAL_LOSE_ANLEGEN)) {

				// SP7446
				ParametermandantDto parameterRF = (ParametermandantDto) DelegateFactory.getInstance()
						.getParameterDelegate().getParametermandant(ParameterFac.PARAMETER_REIHENFOLGENPLANUNG,
								ParameterFac.KATEGORIE_FERTIGUNG, LPMain.getTheClient().getMandant());
				boolean bReihenfolgenplanung = (Boolean) parameterRF.getCWertAsObject();
				if (bReihenfolgenplanung) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("fert.internebestellung.ueberleiten.reihenfolgeplanung.error"));

					return;
				}

				// PJ18367

				Set<Integer> setIIds = null;

				int indexAlle = 0;
				int indexMarkierte = 1;
				int iAnzahlOptionen = 2;

				Object[] aOptionenVerdichten = new Object[iAnzahlOptionen];

				aOptionenVerdichten[indexAlle] = LPMain.getTextRespectUISPr("fert.internebestellung.loseanlegen.alle");
				aOptionenVerdichten[indexMarkierte] = LPMain
						.getTextRespectUISPr("fert.internebestellung.loseanlegen.markierte");

				JRadioButton cbMitAuftragsbezug = new JRadioButton(
						LPMain.getTextRespectUISPr("fert.internebestellung.loseanlegen.mitauftragsbezug"));

				JRadioButton cbNurKundeUndKostenstelle = new JRadioButton(
						LPMain.getTextRespectUISPr("fert.internebestellung.loseanlegen.nurkundeundkostenstelle"));

				JRadioButton cbOhneAuftragsbezug = new JRadioButton(
						LPMain.getTextRespectUISPr("fert.internebestellung.loseanlegen.ohneauftragsbezug"));

				ButtonGroup bg = new ButtonGroup();
				bg.add(cbMitAuftragsbezug);
				bg.add(cbNurKundeUndKostenstelle);
				bg.add(cbOhneAuftragsbezug);

				cbMitAuftragsbezug.setSelected(true);

				Object[] params = { LPMain.getTextRespectUISPr("fert.internebestellung.loseanlegen.frage"),
						cbMitAuftragsbezug, cbNurKundeUndKostenstelle, cbOhneAuftragsbezug };

				int iAuswahl = JOptionPane.showOptionDialog(getInternalFrame(), params,
						LPMain.getTextRespectUISPr("lp.frage"), JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, // Icon
						aOptionenVerdichten, aOptionenVerdichten[0]);

				if (iAuswahl == indexAlle) {
					setIIds = DelegateFactory.getInstance().getFertigungDelegate()
							.getInternebestellungIIdsEinesMandanten();
				} else if (iAuswahl == indexMarkierte) {
					setIIds = new HashSet<Integer>();

					Object[] ids = getPanelQueryInternebestellung(true).getSelectedIds();
					for (int i = 0; i < ids.length; i++) {
						setIIds.add((Integer) ids[i]);
					}

				} else {
					return;
				}

				// PJ222425
				int iTyp = -1;

				if (cbMitAuftragsbezug.isSelected()) {
					iTyp = InternebestellungFac.UEBERLEITEN_MIT_AUFTRAGSBEZUG;
				} else if (cbNurKundeUndKostenstelle.isSelected()) {
					iTyp = InternebestellungFac.UEBERLEITEN_NUR_KUNDE_UND_KOSTENSTELLE;
				} else if (cbOhneAuftragsbezug.isSelected()) {
					iTyp = InternebestellungFac.UEBERLEITEN_OHNE_AUFTRAGSBEZUG;
				}

				// PJ18849
				Integer partnerIIdStandort = null;
				ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate()
						.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);
				boolean lagerminjelager = (Boolean) parameter.getCWertAsObject();
				if (lagerminjelager) {

					DialogStandortAuswaehlen dialog = new DialogStandortAuswaehlen();
					LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(dialog);
					dialog.setVisible(true);

					if (dialog.bCancel == true) {
						return;
					} else {
						partnerIIdStandort = dialog.getPartnerIIdStandort();
					}

				}

				if (setIIds != null) {

					String sMeldung = "";
					HashSet hsBereitsAngezeigt = new HashSet();

					for (Iterator<?> iter = setIIds.iterator(); iter.hasNext();) {
						Integer ibIId = (Integer) iter.next();

						InternebestellungDto ibDto = DelegateFactory.getInstance().getFertigungDelegate()
								.internebestellungFindByPrimaryKey(ibIId);

						Integer losIId = DelegateFactory.getInstance().getFertigungDelegate()
								.interneBestellungUeberleiten(ibIId, partnerIIdStandort, iTyp);
						if (losIId != null) {

							if (ibDto.getBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)
									&& ibDto.getIBelegpositionIId() != null) {
								AuftragpositionDto apDto = DelegateFactory.getInstance().getAuftragpositionDelegate()
										.auftragpositionFindByPrimaryKeyOhneExc(ibDto.getIBelegpositionIId());
								AuftragDto aDto = DelegateFactory.getInstance().getAuftragDelegate()
										.auftragFindByPrimaryKey(apDto.getBelegIId());

								if (apDto != null && !hsBereitsAngezeigt.contains(apDto.getIId())) {
									LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
											.losFindByPrimaryKey(losIId);

									sMeldung += LPMain.getMessageTextRespectUISPr("fert.internebestellung.losvorhanden",
											new Object[] { aDto.getCNr(), apDto.getISort() + "", losDto.getCNr() })
											+ "\r\n";

									hsBereitsAngezeigt.add(apDto.getIId());

								}

							}

						}

					}

					if (sMeldung.length() > 0) {
						sMeldung += "\r\n" + LPMain.getTextRespectUISPr("fert.internebestellung.losvorhanden.zusatz");

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"), sMeldung);
					}

				}
				setInternebestellungDto(null);
				getPanelQueryInternebestellung(true).eventYouAreSelected(false);
			} else if (sAspectInfo.equals(ACTION_SPECIAL_VERDICHTEN)) {
				if (e.getSource() == panelQueryInternebestellung) {
					// PJ18367
					int indexAlle = 0;
					int indexMarkierte = 1;
					int iAnzahlOptionen = 2;

					Object[] aOptionenVerdichten = new Object[iAnzahlOptionen];

					aOptionenVerdichten[indexAlle] = LPMain
							.getTextRespectUISPr("fert.internebestellung.verdichten.alle");
					aOptionenVerdichten[indexMarkierte] = LPMain
							.getTextRespectUISPr("fert.internebestellung.verdichten.markierte");

					int iAuswahl = DialogFactory.showModalDialog(getInternalFrame(),
							LPMain.getTextRespectUISPr("fert.internebestellung.verdichten.frage"),
							LPMain.getTextRespectUISPr("lp.frage"), aOptionenVerdichten, aOptionenVerdichten[0]);

					if (iAuswahl == indexAlle) {
						DelegateFactory.getInstance().getFertigungDelegate().verdichteInterneBestellung((Integer) null);

						if (bInterneBestellungVerdichtenMitRahmenpruefung == true) {
							ArrayList<?> al = DelegateFactory.getInstance().getFertigungDelegate()
									.pruefeOffeneRahmenmengen();

							Object[] aOptionen = new Object[3];
							aOptionen[0] = "Trotzdem \u00FCbernehmen";
							aOptionen[1] = "Restrahmen \u00FCbernehmen";
							aOptionen[2] = "Nicht \u00FCbernehmen";
							String nachricht = "";
							for (int i = 0; i < al.size(); i++) {
								Object[] oTemp = (Object[]) al.get(i);

								nachricht += "Bei " + (String) oTemp[0] + " ist " + oTemp[4] + " von "
										+ Helper.rundeKaufmaennisch((BigDecimal) oTemp[1],
												3)
										+ "eingetragen. Es besteht aber " + /* kein / */"ein zu kleiner Bedarf"/*
																												 * von "
																												 * +
																												 * Helper
																												 * .
																												 * rundeKaufmaennisch
																												 * ( (
																												 * BigDecimal
																												 * )
																												 * oTemp
																												 * [ 2 ]
																												 * , 3 )
																												 */
										+ " daf\u00FCr.\n";

							}
							if (!nachricht.equals("")) {
								DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"), nachricht);
							}
						}

					} else if (iAuswahl == indexMarkierte) {
						HashSet<Integer> set = new HashSet<Integer>();
						Object[] ids = getPanelQueryInternebestellung(true).getSelectedIds();
						for (int i = 0; i < ids.length; i++) {

							InternebestellungDto ibDto = DelegateFactory.getInstance().getFertigungDelegate()
									.internebestellungFindByPrimaryKey((Integer) ids[i]);

							set.add(ibDto.getStuecklisteIId());

						}

						DelegateFactory.getInstance().getFertigungDelegate().verdichteInterneBestellung(set);
					}

					getPanelQueryInternebestellung(true).eventYouAreSelected(false);
				}
			}
		}
	}

	/**
	 * eventStateChanged
	 * 
	 * @param e ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_INTERNEBESTELLUNGBESTELLUNG: {
			getPanelSplitInternebestellung(true).eventYouAreSelected(false);
		}
			break;
		case IDX_PANEL_BEWEGUNGSVORSCHAU: {
			getPanelTabelleBewegungsvorschau(true).eventYouAreSelected(false);
			getPanelTabelleBewegungsvorschau(true).updateButtons(new LockStateValue(PanelBasis.LOCK_IS_NOT_LOCKED));
			getPanelTabelleBewegungsvorschau(true).aktiviereStandort();
		}
			break;
		}

	}

	/**
	 * Diese Methode setzt die aktuelle Interne Bestellung aus der Auswahlliste als
	 * die zu lockende Bestellung.
	 * 
	 * @throws Throwable
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = getPanelQueryInternebestellung(true).getSelectedId();
		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_ACTION_BEDARFSZUSAMMENSCHAU)) {
			String add2Title = "Bedarfszusammenschau";
			getInternalFrame().showReportKriterien(new ReportBedarfszusammenschau(getInternalFrame(), add2Title));

		} else if (e.getActionCommand().equals(MENU_INFO_ZULETZT_ERZEUGT)) {
			// Dialog

			DialogZuletztErzeugt d = new DialogZuletztErzeugt(
					SystemServicesFac.KEYVALUE_EINSTELLUNGEN_LETZTE_INTERNE_BESTELLUNG, getInternalFrame());
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);

		} else if (e.getActionCommand().equals(MENUE_ACTION_BEARBEITEN_BEGINN_ENDE_AENDERN)) {
			DialogBeginnEndeAendern d = new DialogBeginnEndeAendern(this);
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);

			getPanelQueryInternebestellung(true).eventYouAreSelected(false);

		} else if (e.getActionCommand().equals(MENUE_ACTION_BEARBEITEN_LOESCHE_ANGELEGTE_UND_STORNIERTE_LOSE)) {
			DelegateFactory.getInstance().getFertigungDelegate()
					.loescheAngelegteUndStornierteLoseUndAufEinmal(null);

		}
	}

	private PanelInternebestellung getPanelDetailInternebestellung(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailInternebestellung == null && bNeedInstantiationIfNull) {
			panelDetailInternebestellung = new PanelInternebestellung(getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.tab.unten.internebestellung.title"), null, // eventuell gibt es
																								// noch keine Position
					this);
		}
		return panelDetailInternebestellung;
	}

	public PanelQuery getPanelQueryInternebestellung(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryInternebestellung == null && bNeedInstantiationIfNull) {
			FilterKriterium[] fkInternebestellung = SystemFilterFactory.getInstance().createFKMandantCNr();
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryInternebestellung = new PanelQuery(null, fkInternebestellung,
					QueryParameters.UC_ID_INTERNEBESTELLUNG, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.title.panel.internebestellung"), true); // flag, damit flr erst
																								// beim aufruf des
																								// panels
																								// loslaeuft

			if (mEingeschraenkteFertigungsgruppen != null) {
				panelQueryInternebestellung.setFilterComboBox(mEingeschraenkteFertigungsgruppen,
						new FilterKriterium("flrstueckliste.fertigungsgruppe_i_id", true, "" + "",
								FilterKriterium.OPERATOR_EQUAL, false),
						true, null, false);
			} else {
				panelQueryInternebestellung.setFilterComboBox(
						DelegateFactory.getInstance().getStuecklisteDelegate().getAllFertigungsgrupe(),
						new FilterKriterium("flrstueckliste.fertigungsgruppe_i_id", true, "" + "",
								FilterKriterium.OPERATOR_EQUAL, false),
						false, LPMain.getTextRespectUISPr("lp.alle"), false);
			}

			panelQueryInternebestellung.createAndSaveAndShowButton("/com/lp/client/res/clipboard.png",
					LPMain.getTextRespectUISPr("fert.internebestellungdurchfuehren"),
					ACTION_SPECIAL_NEUE_INTERNEBESTELLUNG, RechteFac.RECHT_FERT_LOS_CUD);

			String textVerdichten = LPMain.getTextRespectUISPr("fert.internebestellungverdichten");
			if (bInterneBestellungVerdichtenMitRahmenpruefung == true) {
				textVerdichten += " + " + LPMain.getTextRespectUISPr("fert.pruefeoffenerahmenmengen");
			}

			panelQueryInternebestellung.createAndSaveAndShowButton("/com/lp/client/res/branch.png", textVerdichten,
					ACTION_SPECIAL_VERDICHTEN, RechteFac.RECHT_FERT_LOS_CUD);

			panelQueryInternebestellung.createAndSaveAndShowButton("/com/lp/client/res/clipboard_next.png",
					LPMain.getTextRespectUISPr("fert.internebestellungloseanglegen"), ACTION_SPECIAL_LOSE_ANLEGEN,
					RechteFac.RECHT_FERT_LOS_CUD);
			panelQueryInternebestellung.createAndSaveAndShowButton("/com/lp/client/res/data_down.png",
					LPMain.getTextRespectUISPr("fert.internebestellung.ueberproduktion.reduzieren"),
					ACTION_SPECIAL_UEBERPRODUKTION_REDUZIEREN, RechteFac.RECHT_FERT_LOS_CUD);

			FilterKriteriumDirekt fkDirekt1 = FertigungFilterFactory.getInstance()
					.createFKDArtikelnummerInterneBestellung();

			panelQueryInternebestellung.befuellePanelFilterkriterienDirekt(fkDirekt1, null);
			panelQueryInternebestellung.setMultipleRowSelectionEnabled(true);

			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);
			boolean lagerminjelager = (Boolean) parameter.getCWertAsObject();
			if (lagerminjelager) {
				panelQueryInternebestellung
						.setFilterComboBox(DelegateFactory.getInstance().getLagerDelegate().getAlleStandorte(),
								new FilterKriterium("flrpartner_standort.i_id", true, "" + "",
										FilterKriterium.OPERATOR_EQUAL, false),
								false, LPMain.getTextRespectUISPr("lp.alle"), false);
			}

			parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_REFERENZNUMMER_IN_INTERNER_BESTELLUNG);
			boolean bReferenznummer = (Boolean) parameter.getCWertAsObject();
			if (bReferenznummer) {
				panelQueryInternebestellung.addDirektFilter(FertigungFilterFactory.getInstance().createFKDReferenznr());
			}

		}
		return panelQueryInternebestellung;
	}

	private PanelSplit getPanelSplitInternebestellung(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitInternebestellung == null && bNeedInstantiationIfNull) {
			panelSplitInternebestellung = new PanelSplit(getInternalFrame(), getPanelDetailInternebestellung(true),
					getPanelQueryInternebestellung(true), 210);
			setComponentAt(IDX_PANEL_INTERNEBESTELLUNGBESTELLUNG, panelSplitInternebestellung);
		}
		return panelSplitInternebestellung;
	}

	private PanelTabelleBewegungsvorschau getPanelTabelleBewegungsvorschau(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelTabelleBewegungsvorschau == null && bNeedInstantiationIfNull) {
			panelTabelleBewegungsvorschau = new PanelTabelleBewegungsvorschau(QueryParameters.UC_ID_BEWEGUNGSVORSCHAU2,
					LPMain.getTextRespectUISPr("fert.tab.oben.bewegungsvorschau.title"), getInternalFrame());

			setComponentAt(IDX_PANEL_BEWEGUNGSVORSCHAU, panelTabelleBewegungsvorschau);
			if (getStuecklisteDto() != null) {
				panelTabelleBewegungsvorschau.setDefaultFilter(FertigungFilterFactory.getInstance()
						.createFKBewegungsvorschau(getStuecklisteDto().getArtikelIId(), true, false, null));
			}
		}
		return panelTabelleBewegungsvorschau;
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		setName("TabbedPaneInternebestellung");
		if (wrapperMenuBar == null) {

			wrapperMenuBar = new WrapperMenuBar(this);

			JMenuItem menuItemBedarfszusmmenschau = new JMenuItem(
					LPMain.getTextRespectUISPr("fert.report.bedarfszusammenschau"));

			menuItemBedarfszusmmenschau.addActionListener(this);

			menuItemBedarfszusmmenschau.setActionCommand(MENUE_ACTION_BEDARFSZUSAMMENSCHAU);
			JMenu jmJournal = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_JOURNAL);
			jmJournal.add(menuItemBedarfszusmmenschau);

			JMenu menuInfo = new WrapperMenu("lp.info", this);

			JMenuItem menuItemZuletztErzeugt = new JMenuItem(LPMain.getTextRespectUISPr("lp.zuletzt.erzeugt"));
			menuItemZuletztErzeugt.addActionListener(this);
			menuItemZuletztErzeugt.setActionCommand(MENU_INFO_ZULETZT_ERZEUGT);

			menuInfo.add(menuItemZuletztErzeugt);

			wrapperMenuBar.add(menuInfo);

			// Menu Bearbeiten
			JMenu jmBearbeiten = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_BEARBEITEN);
			WrapperMenuItem menuItemBearbeitenBeginnEndeAendern = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("fert.internebestellung.bearbeiten"), RechteFac.RECHT_FERT_LOS_CUD);
			menuItemBearbeitenBeginnEndeAendern.addActionListener(this);
			menuItemBearbeitenBeginnEndeAendern.setActionCommand(MENUE_ACTION_BEARBEITEN_BEGINN_ENDE_AENDERN);

			jmBearbeiten.add(menuItemBearbeitenBeginnEndeAendern);

			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_INTERNE_BESTELLUNG_ANGELEGTE_ENTFERNEN);
			boolean bAngelegteEntfernen = (java.lang.Boolean) parameter.getCWertAsObject();

			if (bAngelegteEntfernen) {
				WrapperMenuItem menuItemBearbeitenLoescheAngelegteUndStornierteLose = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("fert.internebestellung.loesche.angelegtestornierte"),
						RechteFac.RECHT_FERT_LOS_CUD);
				menuItemBearbeitenLoescheAngelegteUndStornierteLose.addActionListener(this);
				menuItemBearbeitenLoescheAngelegteUndStornierteLose
						.setActionCommand(MENUE_ACTION_BEARBEITEN_LOESCHE_ANGELEGTE_UND_STORNIERTE_LOSE);

				jmBearbeiten.add(menuItemBearbeitenLoescheAngelegteUndStornierteLose);
			}

		}

		return wrapperMenuBar;

	}

	public Integer getSelectedIIdInternebestellung() throws Throwable {
		return (Integer) getPanelQueryInternebestellung(true).getSelectedId();
	}

	/**
	 * hole EingangsrechnungDto.
	 * 
	 * @param key Object
	 * @throws Throwable
	 */
	private void holeInternebestellungDto(Object key) throws Throwable {
		if (key != null) {
			InternebestellungDto ibDto = DelegateFactory.getInstance().getFertigungDelegate()
					.internebestellungFindByPrimaryKey((Integer) key);
			setInternebestellungDto(ibDto);
			getInternalFrame().setKeyWasForLockMe(key.toString());
			if (getPanelDetailInternebestellung(false) != null) {
				getPanelDetailInternebestellung(true).setKeyWhenDetailPanel(key);
			}
		} else {
			setInternebestellungDto(null);
		}
	}

	private PanelDialogKriterienInternebestellung getPanelDialogKriterienIB() throws Throwable {
		if (panelDialogKriterienInternebestellung == null) {
			panelDialogKriterienInternebestellung = new PanelDialogKriterienInternebestellung(getInternalFrame(), "");
		}
		return panelDialogKriterienInternebestellung;
	}

	public Object getDto() {
		return internebestellungDto;
	}
}
