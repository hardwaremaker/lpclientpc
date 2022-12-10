
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
package com.lp.client.rechnung;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;

import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.finanz.ReportMahnlauf;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelDialogKriterienBestellvorschlag2;
import com.lp.client.frame.component.PanelDialogKriterienBestellvorschlagUeberleitung2;
import com.lp.client.frame.component.PanelPositionenBestellvorschlag;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.DialogZuletztErzeugt;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.bestellung.service.BestellvorschlagUeberleitungKriterienDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.rechnung.service.AbrechnungsvorschlagDto;
import com.lp.server.rechnung.service.AbrechnungsvorschlagFac;
import com.lp.server.rechnung.service.AbrechnungsvorschlagUeberleitenDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.VerrechnungsmodellDto;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Pair;

public class TabbedPaneAbrechnungsvorschlag extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelAbrechnungsvorschlagTopQP1 = null;
	private PanelSplit panelAbrechnungsvorschlagSP1 = null;
	private PanelAbrechnungsvorschlag panelAbrechnungsvorschlagBottom = null;

	private PanelQueryFLR panelQueryFLRKunde = null;

	private PanelDialogKriterienAbrechnungsvorschlag pdKritAbrechnungsvorschlag = null;

	private PanelDialogAbrechnungsvorschlagUeberleitenReise pdKritUeberleitungReise = null;
	private PanelDialogAbrechnungsvorschlagUeberleitenER pdKritUeberleitungER = null;
	private PanelDialogAbrechnungsvorschlagUeberleitenPersonalUndMaschinenzeiten pdKritUeberleitungZeitdaten = null;

	private java.util.Date dBisStichtagAbrechnungsvorschlag = null;

	private static final int IDX_PANEL_ABRECHNUNGSVORSCHLAG = 0;

	public java.util.Date getDBisStichtagAbrechnungsvorschlag() {
		return dBisStichtagAbrechnungsvorschlag;
	}

	public void setDBisStichtagAbrechnungsvorschlag(java.util.Date dBisStichtagAbrechnungsvorschlag) {
		this.dBisStichtagAbrechnungsvorschlag = dBisStichtagAbrechnungsvorschlag;
	}

	private static final String ACTION_SPECIAL_NEUER_ABRECHNUNGSVORSCHLAG = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_neuer_abrechnungsvorschlag";

	private static final String ACTION_SPECIAL_VERRECHENBAR_AENDERN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_verrechenbar_aendern";

	private static final String ACTION_SPECIAL_RECHNUNG_AUS_ABRECHNUNGSVORSCHLAG = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_rechnung_aus_abrechnungsvorschlag";

	private static final String ACTION_SPECIAL_MANUELL_ERLEDIGEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_manuell_erledigen";

	private static final String ACTION_SPECIAL_KUNDE_ZUORDNEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_kunde_zuordnen";

	private static final String MENU_ACTION_PRINT_ABRECHNUNGSVORSCHLAG = "MENU_ACTION_PRINT_ABRECHNUNGSVORSCHLAG";

	private static final String MENU_ACTION_BEARBEITEN_ENTERLEDIGEN = "MENU_ACTION_BEARBEITEN_ENTERLEDIGEN";

	private static final String MENU_INFO_ZULETZT_ERZEUGT = "MENU_INFO_ZULETZT_ERZEUGT";

	private AbrechnungsvorschlagDto abrechnungsvorschlagDto = null;

	public AbrechnungsvorschlagDto getAbrechnungsvorschlagDto() {
		return abrechnungsvorschlagDto;
	}

	public void setAbrechnungsvorschlagDto(AbrechnungsvorschlagDto abrechnungsvorschlagDto) throws Throwable {
		this.abrechnungsvorschlagDto = abrechnungsvorschlagDto;

	}

	public TabbedPaneAbrechnungsvorschlag(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag"));
		jbInit();
		initComponents();

		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.add(Calendar.WEEK_OF_YEAR, -1);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		setDBisStichtagAbrechnungsvorschlag(c.getTime());
	}

	public InternalFrameRechnung getInternalFrameBestellung() {
		return (InternalFrameRechnung) getInternalFrame();
	}

	public PanelSplit getPanelBestellungVorschlagSP1() {
		return panelAbrechnungsvorschlagSP1;
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		insertTab(LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag"), null, null,
				LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag"), IDX_PANEL_ABRECHNUNGSVORSCHLAG);

		refreshPanelAbrechnungsvorschlag();

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {
		if (pdKritAbrechnungsvorschlag != null) {
			pdKritAbrechnungsvorschlag.eventItemchanged(eI);
		}
		if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelQueryFLRKunde) {
				Integer iId = (Integer) panelQueryFLRKunde.getSelectedId();
				DelegateFactory.getInstance().getRechnungDelegate().selektiertenPositionenKundeZuordnen(
						panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger(), iId);
				panelAbrechnungsvorschlagSP1.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelAbrechnungsvorschlagTopQP1) {

				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();

				if (key != null) {
					getInternalFrameBestellung()
							.setKeyWasForLockMe(panelAbrechnungsvorschlagTopQP1.getSelectedId() + "");
				}

				panelAbrechnungsvorschlagBottom.setKeyWhenDetailPanel(key);
				panelAbrechnungsvorschlagBottom.eventYouAreSelected(false);
				panelAbrechnungsvorschlagTopQP1.updateButtons();

				if (key != null) {
					AbrechnungsvorschlagDto dto = DelegateFactory.getInstance().getRechnungDelegate()
							.abrechnungsvorschlagFindByPrimaryKey((Integer) key);
					setAbrechnungsvorschlagDto(dto);

				}

				panelAbrechnungsvorschlagTopQP1.enableToolsPanelButtons(
						panelAbrechnungsvorschlagTopQP1.getSelectedIds() != null
								&& panelAbrechnungsvorschlagTopQP1.getSelectedIds().length > 0,
						PanelBasis.ACTION_DELETE);

				LPButtonAction item = (LPButtonAction) panelAbrechnungsvorschlagTopQP1.getHmOfButtons()
						.get(ACTION_SPECIAL_RECHNUNG_AUS_ABRECHNUNGSVORSCHLAG);
				item.getButton().setEnabled(true);
				LPButtonAction item2 = (LPButtonAction) panelAbrechnungsvorschlagTopQP1.getHmOfButtons()
						.get(ACTION_SPECIAL_MANUELL_ERLEDIGEN);
				item2.getButton().setEnabled(true);

				LPButtonAction item3 = (LPButtonAction) panelAbrechnungsvorschlagTopQP1.getHmOfButtons()
						.get(ACTION_SPECIAL_KUNDE_ZUORDNEN);
				item3.getButton().setEnabled(true);

				LPButtonAction item4 = (LPButtonAction) panelAbrechnungsvorschlagTopQP1.getHmOfButtons()
						.get(ACTION_SPECIAL_VERRECHENBAR_AENDERN);
				item4.getButton().setEnabled(true);

			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelAbrechnungsvorschlagBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				panelAbrechnungsvorschlagTopQP1.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));

			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelAbrechnungsvorschlagBottom) {
				panelAbrechnungsvorschlagSP1.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelAbrechnungsvorschlagBottom) {
				Object oKey = panelAbrechnungsvorschlagBottom.getKeyWhenDetailPanel();
				panelAbrechnungsvorschlagTopQP1.eventYouAreSelected(false);
				panelAbrechnungsvorschlagTopQP1.setSelectedId(oKey);
				panelAbrechnungsvorschlagSP1.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == panelAbrechnungsvorschlagBottom) {
				Object oKey = panelAbrechnungsvorschlagTopQP1.getSelectedId();

				// holt sich alten key wieder
				getInternalFrame().setKeyWasForLockMe((oKey == null) ? null : oKey + "");

				if (panelAbrechnungsvorschlagBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelAbrechnungsvorschlagTopQP1.getId2SelectAfterDelete();
					panelAbrechnungsvorschlagTopQP1.setSelectedId(oNaechster);
				}

				panelAbrechnungsvorschlagSP1.eventYouAreSelected(false); // refresh
																			// auf
																			// das
																			// gesamte
																			// 1:n
																			// panel

				// wenn es bisher keinen eintrag gibt, die restlichen
				// panels deaktivieren
				if (panelAbrechnungsvorschlagTopQP1.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_ABRECHNUNGSVORSCHLAG, false);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelAbrechnungsvorschlagTopQP1) {

				/** @todo JO->JE wenn beans nach CW-FAQ dann test! PJ 5219 */
				panelAbrechnungsvorschlagBottom.eventActionNew(eI, true, false);
				panelAbrechnungsvorschlagBottom.eventYouAreSelected(false);
				setSelectedComponent(panelAbrechnungsvorschlagSP1);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (eI.getSource() == pdKritAbrechnungsvorschlag) {

				setDBisStichtagAbrechnungsvorschlag(pdKritAbrechnungsvorschlag.getStichtag());

				DelegateFactory.getInstance().getRechnungDelegate().erstelleAbrechnungsvorschlag(
						pdKritAbrechnungsvorschlag.getStichtag(), pdKritAbrechnungsvorschlag.isAltenLoeschen(),
						pdKritAbrechnungsvorschlag.isMitZeidaten(), pdKritAbrechnungsvorschlag.isMitTelefonzeiten(),
						pdKritAbrechnungsvorschlag.isMitReisezeiten(),
						pdKritAbrechnungsvorschlag.isMitEingangsrechnungen(),
						pdKritAbrechnungsvorschlag.isMitMaschinenzeitdaten());

				panelAbrechnungsvorschlagSP1.eventYouAreSelected(false);
				pdKritAbrechnungsvorschlag.eventYouAreSelected(false);

			} else if (eI.getSource() == pdKritUeberleitungER) {

				DelegateFactory.getInstance().getRechnungDelegate().abrechnungsvorschlagUeberleitenER(
						panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger(), pdKritUeberleitungER.getArtikelIId(),
						pdKritUeberleitungER.getBetragZuVerrechnen(),
						pdKritUeberleitungER.getBetragInclAufschlagZuVerrechnen(), pdKritUeberleitungER.getKommentar(),
						pdKritUeberleitungER.isErledigt(), getInternalFrameBestellung().neuDatum);

				panelAbrechnungsvorschlagSP1.eventYouAreSelected(false);

			} else if (eI.getSource() == pdKritUeberleitungReise) {

				DelegateFactory.getInstance().getRechnungDelegate().abrechnungsvorschlagUeberleitenReise(
						panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger(),
						pdKritUeberleitungReise.getArtikelIIdReisespesen(),
						pdKritUeberleitungReise.getBetragZuVerrechnen(), pdKritUeberleitungReise.getKommentar(),
						pdKritUeberleitungReise.getArtikelIIdKilometer(),
						pdKritUeberleitungReise.getKilometerZuVerrechnen(),
						pdKritUeberleitungReise.getKommentarKilometer(),
						pdKritUeberleitungReise.getSpesenZuVerrechnen(), pdKritUeberleitungReise.getKommentarSpesen(),
						pdKritUeberleitungReise.getArtikelIIdSpesen(), pdKritUeberleitungReise.isErledigt(),
						getInternalFrameBestellung().neuDatum);

				panelAbrechnungsvorschlagSP1.eventYouAreSelected(false);

			} else if (eI.getSource() == pdKritUeberleitungZeitdaten) {

				panelAbrechnungsvorschlagSP1.eventYouAreSelected(false);

			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();

		} else if (eI.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			if (eI.getSource() == panelAbrechnungsvorschlagTopQP1) {

			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();

			if (eI.getSource() == panelAbrechnungsvorschlagTopQP1) {

				if (sAspectInfo.equals(ACTION_SPECIAL_NEUER_ABRECHNUNGSVORSCHLAG)) {

					refreshPdKriterienAbrechnunsvorschlag();
					getInternalFrame().showPanelDialog(pdKritAbrechnungsvorschlag);
				} else if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {

				} else if (sAspectInfo.equals(ACTION_SPECIAL_RECHNUNG_AUS_ABRECHNUNGSVORSCHLAG)) {
					if (panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger() != null
							&& panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger().size() > 0) {
						refreshPdKriterienAbrechnungsvorschlagUeberleiten();
					}

				} else if (sAspectInfo.equals(ACTION_SPECIAL_MANUELL_ERLEDIGEN)) {
					if (panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger() != null
							&& panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger().size() > 0) {
						DelegateFactory.getInstance().getRechnungDelegate().selektiertePositionenManuellErledigen(
								panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger());
						panelAbrechnungsvorschlagSP1.eventYouAreSelected(false);
					}
				} else if (sAspectInfo.equals(ACTION_SPECIAL_KUNDE_ZUORDNEN)) {
					if (panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger() != null
							&& panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger().size() > 0) {

						// Geht nur, wenn kein AB/LO/PJ zugeordnet
						for (int i = 0; i < panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger().size(); i++) {
							Integer avIId = panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger().get(i);
							AbrechnungsvorschlagDto avDto = DelegateFactory.getInstance().getRechnungDelegate()
									.abrechnungsvorschlagFindByPrimaryKey(avIId);

							if (avDto.getKundeIId() != null && (avDto.getProjektIId() != null
									|| avDto.getAuftragIId() != null || avDto.getLosIId() != null)) {
								DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag.kunde.zuordnen.error"));
								return;
							}
						}
						panelQueryFLRKunde = PartnerFilterFactory.getInstance().createPanelFLRKunde(getInternalFrame(),
								false, true);
						new DialogQuery(panelQueryFLRKunde);
					}
				} else if (sAspectInfo.equals(ACTION_SPECIAL_VERRECHENBAR_AENDERN)) {
					if (panelAbrechnungsvorschlagTopQP1.getSelectedId() != null) {
						DialogProzentAendern d = new DialogProzentAendern(
								panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger(), this);
						LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
						d.setVisible(true);
						panelAbrechnungsvorschlagTopQP1.eventYouAreSelected(false);
					}
				}

			}
		}
	}

	public void deleteAuswahl() throws ExceptionLP, Throwable {
		if (panelAbrechnungsvorschlagTopQP1.getSelectedIds() == null)
			return;
		for (Object id : panelAbrechnungsvorschlagTopQP1.getSelectedIds()) {
			DelegateFactory.getInstance().getBestellvorschlagDelegate().removeBestellvorschlag((Integer) id);
		}
		panelAbrechnungsvorschlagTopQP1.eventYouAreSelected(false);
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
		case IDX_PANEL_ABRECHNUNGSVORSCHLAG:
			refreshPanelAbrechnungsvorschlag();
			this.panelAbrechnungsvorschlagSP1.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			this.panelAbrechnungsvorschlagTopQP1.updateButtons();

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelAbrechnungsvorschlagTopQP1.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_ABRECHNUNGSVORSCHLAG, false);
			}
			panelAbrechnungsvorschlagTopQP1
					.updateButtons(panelAbrechnungsvorschlagBottom.getLockedstateDetailMainKey());
			break;
		}

	}

	/**
	 * Diese Methode setzt den aktuellen Bestellvorschlag aus der Auswahlliste als
	 * die zu lockende Bestellung.
	 * 
	 * @throws Throwable
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = panelAbrechnungsvorschlagTopQP1.getSelectedId();
		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_ACTION_PRINT_ABRECHNUNGSVORSCHLAG)) {

			getInternalFrame().showReportKriterien(new ReportAbrechnungsvorschlag(getInternalFrameBestellung(),
					LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag.report.abrechnungsvorschlag")));

		} else if (e.getActionCommand().equals(MENU_ACTION_BEARBEITEN_ENTERLEDIGEN)) {

			getInternalFrame().showReportKriterien(new ReportManuellErledigteEnterledigen(getInternalFrameBestellung(),
					LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag.manuellerledigte.enterledigen")));

		} else if (e.getActionCommand().equals(MENU_INFO_ZULETZT_ERZEUGT)) {
			// Dialog

			DialogZuletztErzeugt d = new DialogZuletztErzeugt(
					SystemServicesFac.KEYVALUE_EINSTELLUNGEN_LETZTER_ABRECHUNGSVORSCHLAG
							+ LPMain.getTheClient().getMandant(),
					getInternalFrame());
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);

		}
	}

	private PanelSplit refreshPanelAbrechnungsvorschlag() throws Throwable {
		FilterKriterium[] fkBestellvorschlag = SystemFilterFactory.getInstance().createFKMandantCNr();

		if (panelAbrechnungsvorschlagSP1 == null) {
			panelAbrechnungsvorschlagBottom = new PanelAbrechnungsvorschlag(getInternalFrame(),
					LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag"), null);

			String[] aWhichButtonIUse = {};

			panelAbrechnungsvorschlagTopQP1 = new PanelQuery(null, fkBestellvorschlag,
					QueryParameters.UC_ID_ABRECHNUNGSVORSCHLAG, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag"), true, null, null); // flag,
																								// damit
																								// flr
																								// erst
																								// beim
																								// aufruf
																								// des
																								// panels

			panelAbrechnungsvorschlagTopQP1
					.addDirektFilter(new FilterKriteriumDirekt("t_von", "", FilterKriterium.OPERATOR_GTE,
							LPMain.getTextRespectUISPr("lp.von"), FilterKriteriumDirekt.PROZENT_NONE, true, // wrapWithSingleQuotes
							false, Facade.MAX_UNBESCHRAENKT, FilterKriteriumDirekt.TYP_DATE));

			panelAbrechnungsvorschlagTopQP1
					.addDirektFilter(new FilterKriteriumDirekt("t_von", "", FilterKriterium.OPERATOR_LT,
							LPMain.getTextRespectUISPr("lp.bis"), FilterKriteriumDirekt.PROZENT_NONE, true, // wrapWithSingleQuotes
							false, Facade.MAX_UNBESCHRAENKT, FilterKriteriumDirekt.TYP_DATE));
			panelAbrechnungsvorschlagTopQP1.addDirektFilter(RechnungFilterFactory.getInstance().createFKDKundename());

			panelAbrechnungsvorschlagTopQP1.addDirektFilter(
					new FilterKriteriumDirekt("flrpersonal.c_kurzzeichen", "", FilterKriterium.OPERATOR_LIKE,
							LPMain.getTextRespectUISPr("pers.personal.kurzzeichen"), FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung
																															// als
																															// 'XX%'
							true, // wrapWithSingleQuotes
							true, Facade.MAX_UNBESCHRAENKT));

			panelAbrechnungsvorschlagTopQP1
					.addDirektFilter(new FilterKriteriumDirekt("flrauftrag.c_nr", "", FilterKriterium.OPERATOR_LIKE,
							LPMain.getTextRespectUISPr("label.auftragnummer"), FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung
																													// als
																													// '%XX'
							true, // wrapWithSingleQuotes
							true, Facade.MAX_UNBESCHRAENKT));

			Map m = new LinkedHashMap();
			m.put(AbrechnungsvorschlagFac.ART_ZEITDATEN, AbrechnungsvorschlagFac.ART_ZEITDATEN);
			m.put(AbrechnungsvorschlagFac.ART_TELEFON, AbrechnungsvorschlagFac.ART_TELEFON);
			m.put(AbrechnungsvorschlagFac.ART_ER, AbrechnungsvorschlagFac.ART_ER);
			m.put(AbrechnungsvorschlagFac.ART_REISE, AbrechnungsvorschlagFac.ART_REISE);
			m.put(AbrechnungsvorschlagFac.ART_MASCHINE, AbrechnungsvorschlagFac.ART_MASCHINE);

			panelAbrechnungsvorschlagTopQP1.setFilterComboBox(m,
					new FilterKriterium("ART", true, "" + "", FilterKriterium.OPERATOR_IN, false), false,
					LPMain.getTextRespectUISPr("lp.alle"), false);

			panelAbrechnungsvorschlagTopQP1.befuelleFilterkriteriumSchnellansicht(new FilterKriterium[] {
					new FilterKriterium("b_verrechnet", true, "0", FilterKriterium.OPERATOR_EQUAL, false) });

			panelAbrechnungsvorschlagTopQP1.setMultipleRowSelectionEnabled(true);

			panelAbrechnungsvorschlagTopQP1.createAndSaveAndShowButton("/com/lp/client/res/clipboard.png",
					LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag.erstellen"),
					ACTION_SPECIAL_NEUER_ABRECHNUNGSVORSCHLAG, RechteFac.RECHT_RECH_RECHNUNG_CUD);
			panelAbrechnungsvorschlagTopQP1.createAndSaveAndShowButton("/com/lp/client/res/percent.png",
					LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag.prozent.aendern"),
					ACTION_SPECIAL_VERRECHENBAR_AENDERN, RechteFac.RECHT_RECH_RECHNUNG_CUD);
			panelAbrechnungsvorschlagTopQP1.createAndSaveAndShowButton("/com/lp/client/res/handshake16x16.png",
					LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag.selektiertezeilen.kundezuordnen"),
					ACTION_SPECIAL_KUNDE_ZUORDNEN, RechteFac.RECHT_RECH_RECHNUNG_CUD);
			panelAbrechnungsvorschlagTopQP1.createAndSaveAndShowButton("/com/lp/client/res/clipboard_next.png",
					LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag.selektiertezeilen.abrechnen"),
					ACTION_SPECIAL_RECHNUNG_AUS_ABRECHNUNGSVORSCHLAG, RechteFac.RECHT_RECH_RECHNUNG_CUD);

			panelAbrechnungsvorschlagTopQP1.createAndSaveAndShowButton("/com/lp/client/res/check2.png",
					LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag.selektiertezeilen.erledigen"),
					ACTION_SPECIAL_MANUELL_ERLEDIGEN, RechteFac.RECHT_RECH_RECHNUNG_CUD);

			panelAbrechnungsvorschlagSP1 = new PanelSplit(getInternalFrame(), panelAbrechnungsvorschlagBottom,
					panelAbrechnungsvorschlagTopQP1, 360);

			setComponentAt(IDX_PANEL_ABRECHNUNGSVORSCHLAG, panelAbrechnungsvorschlagSP1);
		}
		return panelAbrechnungsvorschlagSP1;
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {

		WrapperMenuBar wrapperMenuBar = new WrapperMenuBar(this);

		JMenu journal = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_JOURNAL);
		journal.add(new JSeparator(), 0);
		WrapperMenuItem menuItemAbrechnungsvorschlag = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag.report.abrechnungsvorschlag"), null);
		menuItemAbrechnungsvorschlag.addActionListener(this);
		menuItemAbrechnungsvorschlag.setActionCommand(MENU_ACTION_PRINT_ABRECHNUNGSVORSCHLAG);
		journal.add(menuItemAbrechnungsvorschlag, 0);

		JMenu menuInfo = new WrapperMenu("lp.info", this);

		JMenuItem menuItemZuletztErzeugt = new JMenuItem(LPMain.getTextRespectUISPr("lp.zuletzt.erzeugt"));
		menuItemZuletztErzeugt.addActionListener(this);
		menuItemZuletztErzeugt.setActionCommand(MENU_INFO_ZULETZT_ERZEUGT);

		menuInfo.add(menuItemZuletztErzeugt);
		wrapperMenuBar.add(menuInfo);

		JMenu jmBearbeiten = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

		WrapperMenuItem menueItemEnterledigen = null;
		menueItemEnterledigen = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag.manuellerledigte.enterledigen"),
				RechteFac.RECHT_RECH_RECHNUNG_CUD);
		menueItemEnterledigen.addActionListener(this);
		menueItemEnterledigen.setActionCommand(MENU_ACTION_BEARBEITEN_ENTERLEDIGEN);
		jmBearbeiten.add(menueItemEnterledigen);

		return wrapperMenuBar;
	}

	private PanelDialogKriterien refreshPdKriterienAbrechnunsvorschlag() throws Throwable {
		pdKritAbrechnungsvorschlag = new PanelDialogKriterienAbrechnungsvorschlag(getInternalFrame(),
				LPMain.getTextRespectUISPr("bes.title.panelbestellvorschlagerzeugen"),
				getDBisStichtagAbrechnungsvorschlag());
		return pdKritAbrechnungsvorschlag;
	}

	private void refreshPdKriterienAbrechnungsvorschlagUeberleiten() throws Throwable {

		HashMap hmArten = new HashMap();

		HashMap hmKunden = new HashMap();

		HashMap hmWaehrungen = new HashMap();

		String waehrung = null;

		for (int i = 0; i < panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger().size(); i++) {
			Integer avIId = panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger().get(i);
			AbrechnungsvorschlagDto avDto = DelegateFactory.getInstance().getRechnungDelegate()
					.abrechnungsvorschlagFindByPrimaryKey(avIId);

			if (avDto.getZeitdatenIId() != null) {
				hmArten.put(AbrechnungsvorschlagFac.ART_ZEITDATEN, "");
			}
			if (avDto.getMaschinenzeitdatenIId() != null) {
				hmArten.put(AbrechnungsvorschlagFac.ART_MASCHINE, "");
			}
			if (avDto.getTelefonzeitenIId() != null) {
				hmArten.put(AbrechnungsvorschlagFac.ART_TELEFON, "");
			}
			if (avDto.getReiseIId() != null) {
				hmArten.put(AbrechnungsvorschlagFac.ART_REISE, "");
			}
			if (avDto.getAuftragszuordnungIId() != null) {
				hmArten.put(AbrechnungsvorschlagFac.ART_ER, "");
			}

			hmWaehrungen.put(avDto.getWaehrungCNrRechnung(), "");

			waehrung = avDto.getWaehrungCNrRechnung();

			if (avDto.getKundeIId() == null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						"Es sind Zeilen ohne Kunde enthalten!");
				return;
			}

			hmKunden.put(avDto.getKundeIId(), "");

		}

		if (hmKunden.size() > 1) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					"Es sind Zeilen mit mehreren Kunden enthalten!");
			return;
		}

		if (hmArten.size() > 1) {
			// FEHLER

			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					"Es kann nur eine Art abgerechnet werden");
			return;
		}

		if (hmWaehrungen.size() > 1) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					"Es kann nur eine W\u00e4hrung abgerechnet werden!");
			return;
		}

		Integer kundeIId = (Integer) hmKunden.keySet().iterator().next();

		if (hmArten.size() == 1) {

			if (hmArten.containsKey(AbrechnungsvorschlagFac.ART_ZEITDATEN)
					|| hmArten.containsKey(AbrechnungsvorschlagFac.ART_MASCHINE)
					|| hmArten.containsKey(AbrechnungsvorschlagFac.ART_TELEFON)) {
				AbrechnungsvorschlagUeberleitenDto ueberleituungsvorschlagDto = DelegateFactory.getInstance()
						.getRechnungDelegate()
						.erzeugeUeberleitungsvorschlag((String) hmArten.keySet().iterator().next(),
								panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger());

				pdKritUeberleitungZeitdaten = new PanelDialogAbrechnungsvorschlagUeberleitenPersonalUndMaschinenzeiten(
						kundeIId, ueberleituungsvorschlagDto, getInternalFrame(),
						LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag.selektiertezeilen.abrechnen"), waehrung);
				getInternalFrame().showPanelDialog(pdKritUeberleitungZeitdaten);

			} else if (hmArten.containsKey(AbrechnungsvorschlagFac.ART_ER)) {

				VerrechnungsmodellDto vmDto = DelegateFactory.getInstance().getRechnungDelegate()
						.holeVerrechnungsmodellFuerERUeberleitungAnhandSelektierterZeilen(
								panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger());

				AbrechnungsvorschlagDto avDto = DelegateFactory.getInstance().getRechnungDelegate()
						.abrechnungsvorschlagFindByPrimaryKey(
								(Integer) panelAbrechnungsvorschlagTopQP1.getSelectedId());
				BigDecimal bdBetrag = null;
				if (avDto.getNBetragOffen() != null) {
					bdBetrag = DelegateFactory.getInstance().getRechnungDelegate()
							.getSelektiertenBetrag(panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger());
				}

				pdKritUeberleitungER = new PanelDialogAbrechnungsvorschlagUeberleitenER(avDto.getKundeIId(), vmDto,
						bdBetrag, waehrung, getInternalFrame(),
						LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag.selektiertezeilen.abrechnen"));
				getInternalFrame().showPanelDialog(pdKritUeberleitungER);

			} else if (hmArten.containsKey(AbrechnungsvorschlagFac.ART_REISE)) {
				VerrechnungsmodellDto vmDto = DelegateFactory.getInstance().getRechnungDelegate()
						.holeVerrechnungsmodellFuerReiseUeberleitungAnhandSelektierterZeilen(
								panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger());

				AbrechnungsvorschlagDto avDto = DelegateFactory.getInstance().getRechnungDelegate()
						.abrechnungsvorschlagFindByPrimaryKey(
								(Integer) panelAbrechnungsvorschlagTopQP1.getSelectedId());

				BigDecimal bdBetrag = null;
				if (avDto.getNBetragOffen() != null) {
					bdBetrag = DelegateFactory.getInstance().getRechnungDelegate()
							.getSelektiertenBetrag(panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger());
				}

				BigDecimal bdKilometer = null;
				if (avDto.getNKilometerOffen() != null) {
					bdKilometer = DelegateFactory.getInstance().getRechnungDelegate()
							.getSelektierteKilometer(panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger());
				}

				BigDecimal bdSpesen = null;
				if (avDto.getNSpesenOffen() != null) {
					bdSpesen = DelegateFactory.getInstance().getRechnungDelegate()
							.getSelektierteSpesen(panelAbrechnungsvorschlagTopQP1.getSelectedIdsAsInteger());
				}

				pdKritUeberleitungReise = new PanelDialogAbrechnungsvorschlagUeberleitenReise(avDto.getKundeIId(),
						vmDto, bdBetrag, bdKilometer, bdSpesen, waehrung, getInternalFrame(),
						LPMain.getTextRespectUISPr("rech.abrechnungsvorschlag.selektiertezeilen.abrechnen"));
				getInternalFrame().showPanelDialog(pdKritUeberleitungReise);
			}
		}

	}

	public Object getDto() {
		return abrechnungsvorschlagDto;
	}
}
