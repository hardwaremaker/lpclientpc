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
package com.lp.client.anfrage;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.Map;

import com.lp.client.artikel.ReportArtikelstatistik;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.HvActionEvent;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionen2;
import com.lp.client.frame.component.PanelPositionenArtikelEinkauf;
import com.lp.client.frame.component.PanelPositionenFindSteelSuche;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ErsatztypenDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Panel fuer Anfragepositionen.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 14.06.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.7 $
 */
public class PanelAnfragePositionen extends PanelPositionen2 {

	private static final long serialVersionUID = -3626326220949472858L;
	private InternalFrameAnfrage intFrame = null;
	private TabbedPaneAnfrage tpAnfrage = null;
	private AnfragepositionDto anfragepositionDto = null;
	private PanelPositionenFindSteelSuche panelFindSteel = null;

	private WrapperButton wbuLossollarbeitsplan = new WrapperButton();
	private WrapperTextField wtfLossollarbeitsplan = new WrapperTextField();

	static final private String ACTION_SPECIAL_MONATSSTATISTIK = "action_special_monatsstatikstik";

	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "action_los_from_liste";

	private PanelQueryFLR panelQueryFLRLos = null;
	private PanelQueryFLR panelQueryFLRLossollmaterial = null;

	public PanelAnfragePositionen(InternalFrame internalFrame, String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key, PanelPositionen2.TYP_PANELPREISEINGABE_ARTIKELANFRAGE);
		intFrame = (InternalFrameAnfrage) internalFrame;
		tpAnfrage = intFrame.getTabbedPaneAnfrage();

		jbInit();
		initComponents();
		initPanel();
		//SP9838
		if (tpAnfrage.getAnfrageDto() != null && tpAnfrage.getAnfrageDto().getTBelegdatum() != null) {
			setTBelegdatumMwstsatz(tpAnfrage.getAnfrageDto().getTBelegdatum());
		}
		setDefaults();
	}

	private boolean hasFindSteel() {
		return LPMain.getInstance().getDesktopController()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_DEBUGMODUS);
	}

	private void initializeFindSteel() throws Throwable {
		wcoPositionsart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String currentArt = (String) wcoPositionsart.getKeyOfSelectedItem();
				if ("FINDSTEEL_SUCHTEXT".equals(currentArt)) {
					panelTexteingabe.setVisible(false);
					panelTextbaustein.setVisible(false);
					panelLeerzeile.setVisible(false);
					panelEndsumme.setVisible(false);
					panelUrsprung.setVisible(false);
					panelSeitenumbruch.setVisible(false);
					panelBetreff.setVisible(false);
					panelHandeingabe.setVisible(false);
					panelArtikel.setVisible(false);
					panelIntZwischensumme.setVisible(false);
					panelLieferschein.setVisible(false);
					panelAGStueckliste.setVisible(false);

					panelFindSteel.setVisible(true);
				} else {
					panelFindSteel.setVisible(false);
				}
			}
		});

		iZeile++;
		panelFindSteel = new PanelPositionenFindSteelSuche(getInternalFrame(), "", "FINDSTEEL_SUCHTEXT",
				this.getLockMeWer(), iSpaltenbreiteArtikelMitGoto, getInternalFrame().bRechtDarfPreiseSehenEinkauf,
				getInternalFrame().bRechtDarfPreiseAendernEinkauf);
		add(panelFindSteel, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		panelFindSteel.setVisible(false);
	}

	private void jbInit() throws Throwable {
		resetToolsPanel();

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT,
				ACTION_TEXT, ACTION_MEDIA };

		enableToolsPanelButtons(aWhichButtonIUse);

		panelHandeingabe.setVisibleZeileRabattsumme(false);
		panelHandeingabe.setVisibleZeileZusatzrabattsumme(false);
		panelHandeingabe.setVisibleZeileNettogesamtpreis(false);
		panelHandeingabe.setVisibleZeileMwstsumme(false);
		panelHandeingabe.setVisibleZeileBruttogesamtpreis(false);
		panelHandeingabe.setVisibleZeileLieferterminposition(false);
		panelHandeingabe.wlaEinzelpreis.setText(LPMain.getTextRespectUISPr("anf.richtpreis"));

		wtfLossollarbeitsplan.setActivatable(false);
		wbuLossollarbeitsplan.setText(LPMain.getTextRespectUISPr("fert.tab.unten.los.title") + "...");
		wbuLossollarbeitsplan.setActionCommand(ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLossollarbeitsplan.addActionListener(this);

		panelArtikel.setVisibleZeileRabattsumme(false);
		// panelArtikel.setVisibleZeileZusatzrabattsumme(false);
		panelArtikel.setVisibleZeileNettogesamtpreis(false);

		((PanelPositionenArtikelEinkauf) panelArtikel).wlaRabattsatz.setVisible(false);
		panelArtikel.wlaEinzelpreis.setText(LPMain.getTextRespectUISPr("anf.richtpreis"));

		panelHandeingabe.wnfEinzelpreis.setDependenceField(false);

		panelArtikel.wnfEinzelpreis.setDependenceField(false);

		// PJ 18506
		iZeile++;
		panelArtikel.add(wbuLossollarbeitsplan, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panelArtikel.add(wtfLossollarbeitsplan, new GridBagConstraints(1, iZeile, 6, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		if (hasFindSteel()) {
			initializeFindSteel();
		}

		getInternalFrame().addItemChangedListener(this);
		// Statusbar an den unteren Rand des Panels haengen
		iZeile++;
		add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		this.createAndSaveAndShowButton("/com/lp/client/res/chart.png",
				LPMain.getTextRespectUISPr("lp.statistik.monate"), ACTION_SPECIAL_MONATSSTATISTIK, null);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_MONATSSTATISTIK)) {
			if (anfragepositionDto != null && anfragepositionDto.getArtikelIId() != null) {
				ReportArtikelstatistik reportEtikett = new ReportArtikelstatistik(getInternalFrame(),
						anfragepositionDto.getArtikelIId(), true, "");
				getInternalFrame().showReportKriterien(reportEtikett, false);
			}
		}

		if (e.getActionCommand().equals(ACTION_SPECIAL_LOS_FROM_LISTE)) {
			dialogQueryLosFromListe(e);
		}

	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRLos = FertigungFilterFactory.getInstance().createPanelFLRBebuchbareLose(getInternalFrame(), true,
				true, true, null, true);
		new DialogQuery(panelQueryFLRLos);

	}

	void dialogQueryLossollmaterialFromListe(Integer selectedLosIId) throws Throwable {
		if (selectedLosIId != null) {
			panelQueryFLRLossollmaterial = FertigungFilterFactory.getInstance().createPanelFLRLossollmaterial(
					getInternalFrame(), selectedLosIId, anfragepositionDto.getLossollmaterialIId(), true);
			new DialogQuery(panelQueryFLRLossollmaterial);
		}

	}

	private void initPanel() throws Throwable {
		// combobox Positionen in der UI Sprache des Benutzers fuellen
		Map<String, String> map = DelegateFactory.getInstance().getAnfrageServiceDelegate()
				.getAnfragepositionart(LPMain.getTheClient().getLocUi());
		if (hasFindSteel()) {
			map.put("FINDSTEEL_SUCHTEXT", "FindSteel Suche");
		}
		setPositionsarten(map);

		// setPositionsarten(DelegateFactory.getInstance()
		// .getAnfrageServiceDelegate()
		// .getAnfragepositionart(LPMain.getTheClient().getLocUi()));
	}

	protected void setDefaults() throws Throwable {
		anfragepositionDto = new AnfragepositionDto();
		anfragepositionDto.setBNettopreisuebersteuert(new Short((short) 0));
		leereAlleFelder(this);

		// default positionsart ist ident
		wcoPositionsart.setKeyOfSelectedItem(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT);

		super.setDefaults();
		if (hasFindSteel()) {
			panelFindSteel.setDefaults();
		}

		if (tpAnfrage.getAnfrageDto() != null && tpAnfrage.getAnfrageDto().getIId() != null) {
			// alle auftragabhaengigen Defaults in den verschiedenen
			// PanelPositionen setzen
			panelHandeingabe.setWaehrungCNr(tpAnfrage.getAnfrageDto().getWaehrungCNr());
			panelArtikel.setWaehrungCNr(tpAnfrage.getAnfrageDto().getWaehrungCNr());

			((PanelPositionenArtikelEinkauf) panelArtikel).setLieferantDto(tpAnfrage.getLieferantDto());
			((PanelPositionenArtikelEinkauf) panelArtikel).setBdWechselkurs(new BigDecimal(
					tpAnfrage.getAnfrageDto().getFWechselkursmandantwaehrungzubelegwaehrung().doubleValue()));
		}

		panelArtikel.wtfZusatzbezeichnung.setActivatable(true);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {

		resetEditorButton();

		if (tpAnfrage.istAktualisierenAnfrageErlaubt()) {

			if (tpAnfrage.getAnfrageDto().getStatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN)) {
				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("af.bereitsoffen"), LPMain.getTextRespectUISPr("lp.hint"));
				if (b == false) {
					tpAnfrage.getPanelPositionen().eventYouAreSelected(false);
					return;
				}

			}

			super.eventActionNew(eventObject, true, false); // LockMeForNew
															// setzen

			setDefaults();

			// die neue Position soll vor der aktuell selektierten eingefuegt
			// werden
			if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
				// Dieses Flag gibt an, ob die neue Position vor der aktuellen
				// eingefuegt werden soll
				bFuegeNeuePositionVorDerSelektiertenEin = true;
			}
		} else {
			tpAnfrage.getAnfragePositionenTop()
					.updateButtons(tpAnfrage.getAnfragePositionenBottom().getLockedstateDetailMainKey());
		}
	}

	@Override
	public void discard() throws Throwable {
		super.discard();
		setDefaults();
		panelArtikel.setArtikelEingabefelderEditable(false);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		if (tpAnfrage.istAktualisierenAnfrageErlaubt()) {

			if (tpAnfrage.getAnfrageDto().getStatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN)) {
				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("af.bereitsoffen"), LPMain.getTextRespectUISPr("lp.hint"));
				if (b == false) {
					return;
				}

			}

			super.eventActionUpdate(aE, false);

			panelArtikel.setArtikelEingabefelderEditable(true);

			setzePositionsartAenderbar(anfragepositionDto);
			panelArtikel.setzeEinheitAenderbar();
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		setTBelegdatumMwstsatz(tpAnfrage.getAnfrageDto().getTBelegdatum());
		// den gesamten Inhalt zuruecksetzen, ausloeser war ev. ein discard
		// setDefaults(); //AxD direkt im discard machen
		// position neu einlesen, ausloeser war ev. ein refresh
		Object pkPosition = getKeyWhenDetailPanel();

		if (pkPosition != null && !pkPosition.equals(LPMain.getLockMeForNew())) {
			// Position neu einlesen.
			anfragepositionDto = DelegateFactory.getInstance().getAnfragepositionDelegate()
					.anfragepositionFindByPrimaryKey((Integer) pkPosition);
			// die Anfrage fuer die Statusbar neu einlesen
			tpAnfrage.setAnfrageDto(DelegateFactory.getInstance().getAnfrageDelegate()
					.anfrageFindByPrimaryKey(anfragepositionDto.getBelegIId()));
			dto2Components();
		} else {
			panelArtikel.setLetzteArtikelCNr();
		}

		/**
		 * @todo nicht hier, sondern in der TP
		 */
		tpAnfrage.setTitleAnfrage(LPMain.getTextRespectUISPr("anf.panel.positionen"));

		// wenn die Anfrage gerade von mir gelockt ist, die Artikeleingabefelder
		// freischalten
		LockStateValue lockedstateDetailMainKey = getLockedstateDetailMainKey();
		if (lockedstateDetailMainKey.getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			panelArtikel.setArtikelEingabefelderEditable(true);
		}

		setzePositionsartAenderbar(anfragepositionDto);
		panelArtikel.setzeEinheitAenderbar();

		aktualisiereStatusbar();

		setEditorButtonColor();

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		try {
			calculateFields();

			if (allMandatoryFieldsSetDlg()) {

				components2Dto();

				boolean bDiePositionSpeichern = true;

				bDiePositionSpeichern = HelperClient.checkNumberFormat(panelArtikel.wnfNettopreis.getBigDecimal());

				if (bDiePositionSpeichern) {

					// PJ 16966

					boolean bZertifiziert = pruefeObZertifiziert(panelArtikel.getArtikelDto(),
							tpAnfrage.getLieferantDto());

					if (bZertifiziert == false) {
						boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("part.lieferant.nichtzertifiziert.trotzdem"));

						if (b == false) {
							return;
						}
					}

					if (anfragepositionDto.getIId() == null) {

						// Soll die neue Position vor der aktuell selektierten
						// stehen?
						if (bFuegeNeuePositionVorDerSelektiertenEin) {
							Integer iIdAktuellePosition = (Integer) tpAnfrage.getAnfragePositionenTop().getSelectedId();

							// die erste Position steht an der Stelle 1
							Integer iSortAktuellePosition = new Integer(1);

							if (iIdAktuellePosition != null) {
								iSortAktuellePosition = DelegateFactory.getInstance().getAnfragepositionDelegate()
										.anfragepositionFindByPrimaryKey(iIdAktuellePosition).getISort();

								// Die bestehenden Positionen muessen Platz fuer
								// die neue schaffen
								DelegateFactory.getInstance().getAnfragepositionDelegate()
										.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
												tpAnfrage.getAnfrageDto().getIId(), iSortAktuellePosition.intValue());
							}

							// Die neue Position wird an frei gemachte Position
							// gesetzt
							anfragepositionDto.setISort(iSortAktuellePosition);
						}

						// PJ19954

						boolean bErsatztypenAnlegen = false;

						if (anfragepositionDto.getArtikelIId() != null) {
							ErsatztypenDto[] etDtos = DelegateFactory.getInstance().getArtikelDelegate()
									.ersatztypenFindByArtikelIId(anfragepositionDto.getArtikelIId());
							if (etDtos != null && etDtos.length > 0) {
								bErsatztypenAnlegen = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
										LPMain.getTextRespectUISPr("anf.positionen.ersatztypenanlegen"));
							}
						}

						// wir legen eine neue Position an
						Integer pkPosition = DelegateFactory.getInstance().getAnfragepositionDelegate()
								.createAnfrageposition(anfragepositionDto, bErsatztypenAnlegen);

						// AxD: hier nicht notwendig, passiert im EventYouAreSelected wenn neue Zeile
						// fokussiert wird
//						anfragepositionDto = DelegateFactory.getInstance()
//								.getAnfragepositionDelegate()
//								.anfragepositionFindByPrimaryKey(pkPosition);

						setKeyWhenDetailPanel(pkPosition);
					} else {
						DelegateFactory.getInstance().getAnfragepositionDelegate()
								.updateAnfrageposition(anfragepositionDto);
					}
				}

				// buttons schalten
				super.eventActionSave(e, false);

				// AxD: nicht notwendig, das speichern loest sowieso ein Datenupdate aus
//				eventYouAreSelected(false);
			}
		} finally {
			// per Default wird eine neue Position ans Ende der Liste gesetzt
			bFuegeNeuePositionVorDerSelektiertenEin = false;
		}
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (tpAnfrage.istAktualisierenAnfrageErlaubt()) {

			if (tpAnfrage.getAnfrageDto().getStatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN)) {
				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("af.bereitsoffen"), LPMain.getTextRespectUISPr("lp.hint"));
				if (b == false) {
					return;
				}

			}

			DelegateFactory.getInstance().getAnfragepositionDelegate().removeAnfrageposition(anfragepositionDto);
			this.setKeyWhenDetailPanel(null);
			super.eventActionDelete(e, false, false); // keyWasForLockMe nicht
														// ueberschreiben
		}
	}

	private void dto2Components() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.dto2Components(anfragepositionDto,
				tpAnfrage.getLieferantDto().getPartnerDto().getLocaleCNrKommunikation());
		// 2. Weiter mit den anderen.
		String positionsart = anfragepositionDto.getPositionsartCNr();

		if (positionsart.equalsIgnoreCase(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)) {
			panelArtikel.wnfEinzelpreis.setBigDecimal(anfragepositionDto.getNRichtpreis());
			// AxD: PanelPositionen2::dto2Components holt schon ArtikelDto
//			ArtikelDto oArtikelDto = DelegateFactory
//					.getInstance()
//					.getArtikelDelegate()
//					.artikelFindByPrimaryKey(anfragepositionDto.getArtikelIId());
//			panelArtikel.setArtikelDto(oArtikelDto);
			ArtikelDto oArtikelDto = panelArtikel.getArtikelDto();

			ArtikellieferantDto artliefDto = DelegateFactory.getInstance().getArtikelDelegate().getArtikelEinkaufspreis(
					oArtikelDto.getIId(), tpAnfrage.getAnfrageDto().getLieferantIIdAnfrageadresse(), new BigDecimal(1),
					tpAnfrage.getAnfrageDto().getWaehrungCNr(),
					new java.sql.Date(tpAnfrage.getAnfrageDto().getTBelegdatum().getTime()));

			if (artliefDto != null) {

				if (artliefDto.getNVerpackungseinheit() != null) {
					panelArtikel.pa.setVerpackungsmenge(artliefDto.getNVerpackungseinheit().doubleValue());
				}

			}

			if (anfragepositionDto.getLossollmaterialIId() != null) {
				LossollmaterialDto sollmaterialDto = DelegateFactory.getInstance().getFertigungDelegate()
						.lossollmaterialFindByPrimaryKey(anfragepositionDto.getLossollmaterialIId());

				LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
						.losFindByPrimaryKey(sollmaterialDto.getLosIId());
				String s = losDto.getCNr();
				if (losDto.getCProjekt() != null) {
					s += ", " + losDto.getCProjekt();
				}

				wtfLossollarbeitsplan.setText(s);
			} else {
				wtfLossollarbeitsplan.setText(null);
			}

		} else if (positionsart.equalsIgnoreCase(AnfrageServiceFac.ANFRAGEPOSITIONART_HANDEINGABE)) {
			panelHandeingabe.wnfEinzelpreis.setBigDecimal(anfragepositionDto.getNRichtpreis());
		}
	}

	/**
	 * Alle Positionsdaten aus dem Panel sammeln.
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.components2Dto(anfragepositionDto,
				tpAnfrage.getLieferantDto().getPartnerDto().getLocaleCNrKommunikation(),
				tpAnfrage.getAnfrageDto().getIId());
		// 2. Weiter mit den anderen.

		String positionsart = getPositionsartCNr();

		if (positionsart.equalsIgnoreCase(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)) {
			anfragepositionDto.setNRichtpreis(panelArtikel.wnfEinzelpreis.getBigDecimal());
		} else if (positionsart.equalsIgnoreCase(AnfrageServiceFac.ANFRAGEPOSITIONART_HANDEINGABE)) {
			anfragepositionDto.setNRichtpreis(panelHandeingabe.wnfEinzelpreis.getBigDecimal());
		}
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAnfrage.getAnfrageDto().getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAnfrage.getAnfrageDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAnfrage.getAnfrageDto().getPersonalIIdAendern());
		setStatusbarTAendern(tpAnfrage.getAnfrageDto().getTAendern());
		setStatusbarStatusCNr(tpAnfrage.getAnfrageStatus());
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ANFRAGE;
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;

		if (exfc.getICode() == EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("auft.mandant.hauptlager_fehlt"));
		} else {
			bErrorErkannt = false;
		}

		return bErrorErkannt;
	}

	/**
	 * Drucke Anfrage.
	 * 
	 * @param e Ereignis
	 * @throws Throwable
	 */
	protected void eventActionPrint(HvActionEvent e) throws Throwable {

		if (e.isMouseEvent() && e.isRightButtonPressed()) {

			if (tpAnfrage.getAnfrageDto().getStatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT)) {
				DelegateFactory.getInstance().getAnfrageDelegate()
						.berechneAktiviereBelegControlled(tpAnfrage.getAnfrageDto().getIId());
				eventActionRefresh(e, false);
			} else {
				DialogFactory.showModalDialog("Status", LPMain.getMessageTextRespectUISPr("status.zustand",
						LPMain.getTextRespectUISPr("anf.anfrage"), tpAnfrage.getAnfrageStatus().trim()));
			}

		} else {
			tpAnfrage.printAnfrage();
		}

		eventYouAreSelected(false);
	}

	// statuspositionen: 0 getLockedstateDetailMainKey ueberschreiben
	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue lsv = super.getLockedstateDetailMainKey();

		if (tpAnfrage.getAnfrageDto().getIId() != null) {
			if (tpAnfrage.getAnfrageDto().getStatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_STORNIERT)
					|| tpAnfrage.getAnfrageDto().getStatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_ERFASST)
					|| tpAnfrage.getAnfrageDto().getStatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT)
					|| (tpAnfrage.getAnfrageDto().getStatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN)
							&& tpAnfrage.getAnfrageDto().getCAngebotnummer() != null)) {
				lsv = new LockStateValue(PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lsv;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelArtikel.wifArtikelauswahl) {
				if (panelArtikel.wifArtikelauswahl.getArtikelDto() != null
						&& tpAnfrage.getAnfrageDto().getLieferantIIdAnfrageadresse() != null) {
					// PJ18879 Standarmenge vorschlagen
					ArtikellieferantDto artliefDto = DelegateFactory.getInstance().getArtikelDelegate()
							.getArtikelEinkaufspreis(panelArtikel.wifArtikelauswahl.getArtikelDto().getIId(),
									tpAnfrage.getAnfrageDto().getLieferantIIdAnfrageadresse(), new BigDecimal(1),
									tpAnfrage.getAnfrageDto().getWaehrungCNr(),
									new java.sql.Date(tpAnfrage.getAnfrageDto().getTBelegdatum().getTime()));

					if (artliefDto != null) {

						if (artliefDto.getFStandardmenge() != null) {
							panelArtikel.wnfMenge.setDouble(artliefDto.getFStandardmenge());
						} else if (artliefDto.getFMindestbestelmenge() != null) {
							panelArtikel.wnfMenge.setDouble(artliefDto.getFMindestbestelmenge());
						}

						if (artliefDto.getNVerpackungseinheit() != null) {
							panelArtikel.pa.setVerpackungsmenge(artliefDto.getNVerpackungseinheit().doubleValue());
						} else {
							panelArtikel.pa.setVerpackungsmenge(null);
						}

						// SP4058

						ParametermandantDto parameterDto = DelegateFactory.getInstance().getParameterDelegate()
								.getMandantparameter(LPMain.getTheClient().getMandant(),
										ParameterFac.KATEGORIE_ALLGEMEIN,
										ParameterFac.PARAMETER_VERPACKUNGSMENGEN_EINGABE);
						int bVerpackungsmengeneingabe = (Integer) parameterDto.getCWertAsObject();

						if (bVerpackungsmengeneingabe > 0) {

							if (panelArtikel.pa != null) {
								panelArtikel.pa.clearMengenUndSetzeFocusAufKarton();
								panelArtikel.wnfMenge.setDouble(0D);
							}
						}

					}

				}

			} else if (e.getSource() == panelQueryFLRLos) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					dialogQueryLossollmaterialFromListe(key);
				}

			} else if (e.getSource() == panelQueryFLRLossollmaterial) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				LossollmaterialDto sollmaterialDto = DelegateFactory.getInstance().getFertigungDelegate()
						.lossollmaterialFindByPrimaryKey(key);

				LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
						.losFindByPrimaryKey(sollmaterialDto.getLosIId());
				anfragepositionDto.setLossollmaterialIId(key);

				String s = losDto.getCNr();
				if (losDto.getCProjekt() != null) {
					s += ", " + losDto.getCProjekt();
				}

				wtfLossollarbeitsplan.setText(s);

				// PJ 16484
				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(sollmaterialDto.getArtikelIId());
				getPanelArtikel().wifArtikelauswahl.setArtikelDto(artikelDto);
				getPanelArtikel().setArtikelDto(artikelDto);
				getPanelArtikel().wnfMenge.setBigDecimal(sollmaterialDto.getNMenge());
				getPanelArtikel().wifArtikelauswahl.getWtfIdent().requestFocus();
				getPanelArtikel().artikelDto2components();

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRLossollmaterial || e.getSource() == panelQueryFLRLos) {

				wtfLossollarbeitsplan.setText(null);
				anfragepositionDto.setLossollmaterialIId(null);
			}
		}

	}

	private void setEditorButtonColor() {
		getHmOfButtons().get(ACTION_TEXT).getButton()
				.setIcon(anfragepositionDto.getXTextinhalt() != null && anfragepositionDto.getXTextinhalt().length() > 0
						? IconFactory.getCommentExist()
						: IconFactory.getEditorEdit());
	}

	private void resetEditorButton() {
		getHmOfButtons().get(ACTION_TEXT).getButton().setIcon(IconFactory.getEditorEdit());
	}

	@Override
	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

//		tpAnfrage.checkDisableTabs();
	}
}
