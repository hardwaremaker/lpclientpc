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
package com.lp.client.lieferschein;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.EventObject;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.HvActionEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionen2;
import com.lp.client.frame.component.PanelPositionenArtikelVerkauf;
import com.lp.client.frame.component.PanelPositionenArtikelVerkaufSNR;
import com.lp.client.frame.component.PositionNumberHelperLieferschein;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.ArtikelMengenDialogRueckgabe;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.pc.PasteBuffer;
import com.lp.client.system.DialogEingabeBetrag;
import com.lp.client.util.HelperTimestamp;
import com.lp.client.util.IconFactory;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

/*
 * <p>In diesem Detailfenster der Lieferscheinposition werden Daten erfasst bzw.
 * geaendert.</p> <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2004-10-22</p> <p> </p>
 *
 * @author Uli Walch
 *
 * @version $Revision: 1.32 $
 */
public class PanelLieferscheinPositionen extends PanelPositionen2 {
	private static final long serialVersionUID = 9146937758145057284L;

	private final InternalFrameLieferschein intFrame;
	private final TabbedPaneLieferschein tpLieferschein;
	private LieferscheinpositionDto positionDto = null;

	WrapperLabel wlaAuftrag = null;
	private WrapperTextField wtfAuftragNummer = null;
	private WrapperTextField wtfAuftragProjekt = null;
	private WrapperTextField wtfAuftragBestellnummer = null;

	static final private String ACTION_SPECIAL_ETIKETTDRUCKEN = "action_special_etikettdrucken";

	public final static String MY_OWN_NEW_INT_ZWS_UEBERSTEUERN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_INT_ZWS_UEBERSTEUERN";
	
	public PanelLieferscheinPositionen(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key,
				PanelPositionen2.TYP_PANELPREISEINGABE_ARTIKELVERKAUFSNR); // VKPF

		intFrame = (InternalFrameLieferschein) internalFrame;
		tpLieferschein = intFrame.getTabbedPaneLieferschein();

		zwController
				.setPositionNumberHelper(new PositionNumberHelperLieferschein());
		zwController.setBelegDto(tpLieferschein.getLieferscheinDto());

		jbInitPanel();
		initComponents();
		initPanel();
	}

	private void jbInitPanel() throws Throwable {
		// braucht nur refresh, save und aendern
		resetToolsPanel();

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT, ACTION_TEXT, ACTION_MEDIA };

		enableToolsPanelButtons(aWhichButtonIUse);

		// zusaetzliche Felder fuer den Auftrag beim Artikel
		iZeile++;
		wlaAuftrag = new WrapperLabel(
				LPMain.getTextRespectUISPr("auft.auftrag"));
		wlaAuftrag.setMinimumSize(new Dimension(iSpaltenbreiteArtikelMitGoto,
				Defaults.getInstance().getControlHeight()));
		wlaAuftrag.setPreferredSize(new Dimension(iSpaltenbreiteArtikelMitGoto,
				Defaults.getInstance().getControlHeight()));
		wtfAuftragNummer = new WrapperTextField();
		wtfAuftragNummer.setActivatable(false);
		wtfAuftragNummer.setMinimumSize(new Dimension(80, Defaults
				.getInstance().getControlHeight()));
		wtfAuftragNummer.setPreferredSize(new Dimension(80, Defaults
				.getInstance().getControlHeight()));
		wtfAuftragProjekt = new WrapperTextField();
		wtfAuftragProjekt.setActivatable(false);
		wtfAuftragBestellnummer = new WrapperTextField();
		wtfAuftragBestellnummer.setActivatable(false);
		JPanel jpaAuftrag = new JPanel(new GridBagLayout());
		jpaAuftrag.add(wlaAuftrag, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		jpaAuftrag.add(wtfAuftragNummer, new GridBagConstraints(1, 0, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaAuftrag.add(wtfAuftragProjekt, new GridBagConstraints(2, 0, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaAuftrag.add(wtfAuftragBestellnummer, new GridBagConstraints(3, 0, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		add(jpaAuftrag, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		this.createAndSaveAndShowButton(
				"/com/lp/client/res/printer216x16.png",
				LPMain.getTextRespectUISPr("artikel.report.etikett.shortcut"),
				ACTION_SPECIAL_ETIKETTDRUCKEN,
				KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK
						| InputEvent.SHIFT_DOWN_MASK),
				RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);

		// Statusbar an den unteren Rand des Panels haengen
		iZeile++;
		add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		panelArtikel.wlaMenge.setText(LPMain
				.getTextRespectUISPr("ls.label.mengels"));
		panelArtikel.wlaMenge.setMaximumSize(new Dimension(
				iSpaltenbreiteArtikelMitGoto, Defaults.getInstance()
						.getControlHeight()));
		panelArtikel.wlaMenge.setMinimumSize(new Dimension(
				iSpaltenbreiteArtikelMitGoto, Defaults.getInstance()
						.getControlHeight()));
		panelArtikel.wlaMenge.setPreferredSize(new Dimension(
				iSpaltenbreiteArtikelMitGoto, Defaults.getInstance()
						.getControlHeight()));
		panelHandeingabe.setVisibleZeileLieferterminposition(false);
		
		getToolBar().addButtonCenter("/com/lp/client/res/numeric_keypad.png",
				LPMain.getTextRespectUISPr("angb.positionen.intzws.uebersteuern"), MY_OWN_NEW_INT_ZWS_UEBERSTEUERN,
				null, RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);
		
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ETIKETTDRUCKEN)) {
			if (positionDto != null && positionDto.getIId() != null) {
				tpLieferschein.printLieferscheinwaetikett(positionDto.getIId());
			}
		}else 	if (e.getActionCommand().equals(MY_OWN_NEW_INT_ZWS_UEBERSTEUERN)) {

			DialogEingabeBetrag d = new DialogEingabeBetrag(Defaults.getInstance().getIUINachkommastellenPreiseVK(),
					tpLieferschein.getLieferscheinDto().getWaehrungCNr(), getInternalFrame());
			d.setTitle(LPMain.getTextRespectUISPr("angb.positionen.intzws.uebersteuern.zielpreis"));
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);

			d.setVisible(true);

			if (d.bdBetrag == null) {
				return;
			}
			DelegateFactory.getInstance().getLsDelegate()
					.uebersteuereIntelligenteZwischensumme(positionDto.getIId(), d.bdBetrag);

			tpLieferschein.getPanelPositionen().eventYouAreSelected(false);

		}
	}

	private void initPanel() throws Throwable {
		// combobox Positionen in der UI Sprache des Benutzers fuellen
		setPositionsarten(DelegateFactory.getInstance()
				.getLieferscheinServiceDelegate()
				.getLieferscheinpositionart(LPMain.getTheClient().getLocUi()));
	}

	protected void setDefaults() throws Throwable {
		positionDto = new LieferscheinpositionDto();
		positionDto.setBNettopreisuebersteuert(new Short((short) 0));

		leereAlleFelder(this);

		// default positionsart ist ident
		wcoPositionsart
				.setKeyOfSelectedItem(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT);

		super.setDefaults();

		// der Vorschlagswert fuer eine frei erfasste Position ist 1
		//panelArtikel.wnfMenge.setDouble(new Double(1));

		panelArtikel.setUebersteuertesLagerIId(null);

		// dem panelArtikel muss das Lager und der Kunde gesetzt werden
		// PJ 14751

		KundeDto rechnungsadresse = DelegateFactory
				.getInstance()
				.getKundeDelegate()
				.kundeFindByPrimaryKey(
						tpLieferschein.getLieferscheinDto()
								.getKundeIIdRechnungsadresse());

		((PanelPositionenArtikelVerkaufSNR) panelArtikel)
				.setKundeDto(rechnungsadresse);
		((PanelPositionenArtikelVerkaufSNR) panelArtikel)
				.setIIdLager(tpLieferschein.getLieferscheinDto().getLagerIId());

		if (tpLieferschein.getLieferscheinDto() != null
				&& tpLieferschein.getLieferscheinDto().getIId() != null) {
			// alle lieferscheinabhaengigen Defaults in den verschiedenen
			// PanelPositionen setzen
			panelHandeingabe.setWaehrungCNr(tpLieferschein.getLieferscheinDto()
					.getWaehrungCNr());
			panelArtikel.setWaehrungCNr(tpLieferschein.getLieferscheinDto()
					.getWaehrungCNr());

			// im PanelArtikel alles fuer die VKPF vorbereiten
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setKundeDto(rechnungsadresse);
		
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setCNrWaehrung(tpLieferschein.getLieferscheinDto()
							.getWaehrungCNr());
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setWechselkurs(tpLieferschein.getLieferscheinDto()
							.getFWechselkursmandantwaehrungzubelegwaehrung());
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setGueltigkeitsdatumArtikeleinzelverkaufspreis(new java.sql.Date(
							tpLieferschein.getLieferscheinDto()
									.getTBelegdatum().getTime()));
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setIIdLager(tpLieferschein.getLieferscheinDto()
							.getLagerIId());

			// dem panelArtikel muss die momentan eingetragene Menge gesetzt
			// werden -> fuer Chargennummern
			((PanelPositionenArtikelVerkaufSNR) panelArtikel)
					.setDDBisherigeMenge(new Double(0));
		}

		// Wenn der Vorschlagswert fuer den Mwstsatz aus dem Belegkunden kommt
		if (!panelHandeingabe.getBDefaultMwstsatzAusArtikel()) {
			if (tpLieferschein.getKundeLieferadresseDto() != null
					&& tpLieferschein.getKundeLieferadresseDto().getIId() != null) {
				// Aktuellen MWST-Satz uebersetzen.
				
				Timestamp belegDatum = panelHandeingabe.getTBelegdatumMwstsatz();
				if(belegDatum == null) {
					belegDatum = HelperTimestamp.cut();
				}
				MwstsatzDto mwstsatzDtoAktuell = DelegateFactory.getInstance().getMandantDelegate()
						.mwstsatzFindZuDatum(tpLieferschein.getKundeLieferadresseDto()
									.getMwstsatzbezIId(), belegDatum);
/*				
				MwstsatzDto mwstsatzDtoAktuell = null;
				if (panelHandeingabe.getTBelegdatumMwstsatz() != null) {
					mwstsatzDtoAktuell = DelegateFactory
							.getInstance()
							.getMandantDelegate()
							.mwstsatzFindZuDatum(
									tpLieferschein.getKundeLieferadresseDto()
									.getMwstsatzbezIId(),
									panelHandeingabe
											.getTBelegdatumMwstsatz());
				} else {

					mwstsatzDtoAktuell = DelegateFactory
							.getInstance()
							.getMandantDelegate()
							.mwstsatzFindByMwstsatzbezIIdAktuellster(
									tpLieferschein.getKundeLieferadresseDto()
									.getMwstsatzbezIId());
				}
*/				
				// TODO SP8308: Ueberpruefung auf null mwstsatzDtoAktuell!
				
				panelHandeingabe.wcoMwstsatz
						.setKeyOfSelectedItem(mwstsatzDtoAktuell.getIId());
			}
		}
		panelArtikel.wtfZusatzbezeichnung.setActivatable(true);
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		setTBelegdatumMwstsatz(tpLieferschein.getLieferscheinDto()
				.getTBelegdatum());
		// den gesamten Inhalt zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();
		// position neu einlesen, ausloeser war ev. ein refresh
		Object pkPosition = getKeyWhenDetailPanel();

		// wenn es eine Position anzuzeigen gibt, dann anzeigen
		if (pkPosition != null && !pkPosition.equals(LPMain.getLockMeForNew())) {
			// Position neu einlesen.
			positionDto = DelegateFactory.getInstance()
					.getLieferscheinpositionDelegate()
					.lieferscheinpositionFindByPrimaryKey((Integer) pkPosition);
			Integer lagerIId = tpLieferschein.getLieferscheinDto()
					.getLagerIId();
			if (positionDto.getLagerIId() != null) {
				lagerIId = positionDto.getLagerIId();
			}

			((PanelPositionenArtikelVerkaufSNR) panelArtikel)
					.setIIdLager(lagerIId);

			dto2Components();

			// dem panelArtikel muss die momentan eingetragene Menge gesetzt
			// werden -> fuer Chargennummern
			if (positionDto.getNMenge() != null) {
				((PanelPositionenArtikelVerkaufSNR) panelArtikel)
						.setDDBisherigeMenge(new Double(positionDto.getNMenge()
								.doubleValue()));
			}

			LPButtonAction itemEtikket = (LPButtonAction) getHmOfButtons().get(
					ACTION_SPECIAL_ETIKETTDRUCKEN);
			itemEtikket.getButton().setEnabled(true);

		} else {
			panelArtikel.setLetzteArtikelCNr();
		}

		
		if (panelArtikel instanceof PanelPositionenArtikelVerkauf) {
			((PanelPositionenArtikelVerkauf) panelArtikel)
					.setBelegpositionVerkaufDto(positionDto);
		}
		
		// wenn der Lieferschein gerade gelockt ist, die Eingabefelder
		// freischalten
		if (getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_FOR_NEW) {
			panelArtikel.setArtikelEingabefelderEditable(true);
			((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
					.setEnabled(true);
			((PanelPositionenArtikelVerkaufSNR) panelArtikel)
					.zeigeSerienchargennummer(true, false, true);
		} else {
			panelArtikel.setArtikelEingabefelderEditable(false);
			((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
					.setEnabled(false);
			((PanelPositionenArtikelVerkaufSNR) panelArtikel)
					.zeigeSerienchargennummer(false, false, false);
		}

		setzePositionsartAenderbar(positionDto);
		panelArtikel.setzeEinheitAenderbar();

		aktualisiereStatusbar();
		tpLieferschein.enablePanels(tpLieferschein.getLieferscheinDto(), true);

		tpLieferschein.setTitleLieferschein(LPMain
				.getTextRespectUISPr("ls.title.panel.positionen"));
		LPButtonAction item = null;
		if (positionDto.getLieferscheinpositionartCNr() != null) {
			boolean bEnableVerschieben = !positionDto
					.getLieferscheinpositionartCNr().equals(
							LocaleFac.POSITIONSART_POSITION);
			item = (LPButtonAction) tpLieferschein
					.getLieferscheinPositionenTop().getHmOfButtons()
					.get(PanelBasis.ACTION_POSITION_VONNNACHNMINUS1);
			item.getButton().setVisible(bEnableVerschieben);
			item = (LPButtonAction) tpLieferschein
					.getLieferscheinPositionenTop().getHmOfButtons()
					.get(PanelBasis.ACTION_POSITION_VONNNACHNPLUS1);
			item.getButton().setVisible(bEnableVerschieben);
		}

		setEditorButtonColor();

		refreshMyComponents();
		
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

		panelArtikel.setArtikelEingabefelderEditable(false);
		((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
				.setEnabled(false);
	}

	/**
	 * Alle Positionsdaten aus einem dto ins Panel setzen.
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.dto2Components(positionDto, tpLieferschein
				.getKundeLieferadresseDto().getPartnerDto()
				.getLocaleCNrKommunikation());
		// 2. Weiter mit den anderen.
		String positionsart = positionDto.getPositionsartCNr();

		if (positionsart.equalsIgnoreCase(LocaleFac.POSITIONSART_IDENT)) {
			// Serien/Chargennummern.
			((PanelPositionenArtikelVerkaufSNR) panelArtikel)
					.zeigeSerienchargennummer(true, false, false);
			((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
					.setSeriennummern(positionDto
							.getSeriennrChargennrMitMenge(), positionDto
							.getArtikelIId(), tpLieferschein
							.getLieferscheinDto().getLagerIId());
			// } else if (positionsart
			// .equalsIgnoreCase(LocaleFac.POSITIONSART_HANDEINGABE)) {
			// // zzt. nicht spezifisches.
		}
		// Auftrag anzeigen

		wlaAuftrag.setText(LPMain.getTextRespectUISPr("auft.auftrag"));

		if (positionDto.getAuftragpositionIId() != null) {
			AuftragpositionDto abPos = DelegateFactory
					.getInstance()
					.getAuftragpositionDelegate()
					.auftragpositionFindByPrimaryKey(
							positionDto.getAuftragpositionIId());
			AuftragDto auftragDto = DelegateFactory.getInstance()
					.getAuftragDelegate()
					.auftragFindByPrimaryKey(abPos.getBelegIId());
			wtfAuftragNummer.setText(auftragDto.getCNr());
			wtfAuftragProjekt.setText(auftragDto.getCBezProjektbezeichnung());
			wtfAuftragBestellnummer.setText(auftragDto.getCBestellnummer());
		} else if (positionDto.getForecastpositionIId() != null) {

			setzeForecastdaten(positionDto.getForecastpositionIId());

		} else {
			wtfAuftragNummer.setText(null);
			wtfAuftragProjekt.setText(null);
			wtfAuftragBestellnummer.setText(null);
		}
	}

	private void setzeForecastdaten(Integer forecastpositionIId)
			throws ExceptionLP, Throwable {
		wlaAuftrag.setText(LPMain.getTextRespectUISPr("fc.forecast"));

		ForecastpositionDto forecastpositionDto = DelegateFactory.getInstance()
				.getForecastDelegate()
				.forecastpositionFindByPrimaryKey(forecastpositionIId);
		ForecastauftragDto forecastauftragDto = DelegateFactory
				.getInstance()
				.getForecastDelegate()
				.forecastauftragFindByPrimaryKey(
						forecastpositionDto.getForecastauftragIId());
		FclieferadresseDto fclDto = DelegateFactory
				.getInstance()
				.getForecastDelegate()
				.fclieferadresseFindByPrimaryKey(
						forecastauftragDto.getFclieferadresseIId());
		ForecastDto forecastDto = DelegateFactory.getInstance()
				.getForecastDelegate()
				.forecastFindByPrimaryKey(fclDto.getForecastIId());

		wtfAuftragNummer.setText(forecastDto.getCNr());
		wtfAuftragProjekt.setText(forecastDto.getCProjekt());
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		resetEditorButton();

		if (tpLieferschein.istAktualisierenLieferscheinErlaubt()) {

			if (tpLieferschein.getLieferscheinDto().getStatusCNr()
					.equals(LieferscheinFac.LSSTATUS_OFFEN)) {
				boolean b = DialogFactory.showModalJaNeinDialog(
						getInternalFrame(),
						LPMain.getTextRespectUISPr("ls.bereitsoffen"),
						LPMain.getTextRespectUISPr("lp.hint"));
				if (b == false) {
					return;
				}

			} else if (tpLieferschein.getLieferscheinDto().getStatusCNr()
					.equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
				boolean b = DialogFactory.showModalJaNeinDialog(
						getInternalFrame(),
						LPMain.getTextRespectUISPr("ls.bereitsgeliefert"),
						LPMain.getTextRespectUISPr("lp.hint"));
				if (b == false) {
					return;
				}
			}
			setDefaults();

			super.eventActionNew(eventObject, true, false); // LockMeForNew
			// setzen

			wcoPositionsart.setEnabled(true);

			if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
				// Dieses Flag gibt an, ob die neue Position vor der aktuellen
				// eingefuegt werden soll
				bFuegeNeuePositionVorDerSelektiertenEin = true;
			}
		} else {
			tpLieferschein.getLieferscheinPositionenTop().updateButtons(
					tpLieferschein.getLieferscheinPositionenBottom()
							.getLockedstateDetailMainKey());
		}
	}

	private void refreshMyComponents() throws Throwable {

	// PJ21633
		if (wcoPositionsart.getKeyOfSelectedItem()
				.equals(AngebotServiceFac.ANGEBOTPOSITIONART_INTELLIGENTE_ZWISCHENSUMME)
				&& tpLieferschein.getLieferscheinDto().getStatusCNr().equals(LieferscheinFac.LSSTATUS_ANGELEGT)) {

			getHmOfButtons().get(MY_OWN_NEW_INT_ZWS_UEBERSTEUERN).getButton().setVisible(true);
		} else {
			getHmOfButtons().get(MY_OWN_NEW_INT_ZWS_UEBERSTEUERN).getButton().setVisible(false);
		}

	}
	
	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		
		refreshMyComponents();
		
		if (tpLieferschein.istAktualisierenLieferscheinErlaubt()) {

			// if (tpLieferschein.getLieferscheinDto().getStatusCNr()
			// .equals(LieferscheinFac.LSSTATUS_OFFEN)) {
			// boolean b = DialogFactory.showModalJaNeinDialog(
			// getInternalFrame(),
			// LPMain.getTextRespectUISPr("ls.bereitsoffen"),
			// LPMain.getTextRespectUISPr("lp.hint"));
			// if (b == false) {
			// return;
			// }
			//
			// } else if (tpLieferschein.getLieferscheinDto().getStatusCNr()
			// .equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
			// boolean b = DialogFactory.showModalJaNeinDialog(
			// getInternalFrame(),
			// LPMain.getTextRespectUISPr("ls.bereitsgeliefert"),
			// LPMain.getTextRespectUISPr("lp.hint"));
			// if (b == false) {
			// return;
			// }
			// }

			super.eventActionUpdate(aE, false);

			((PanelPositionenArtikelVerkaufSNR) panelArtikel)
					.zeigeSerienchargennummer(true, false, false);

			// PJ 14648 Setartikel
			if (panelArtikel.getArtikelDto().getIId() != null
					&& positionDto.getPositioniIdArtikelset() != null) {

				if (Helper.short2boolean(panelArtikel.getArtikelDto()
						.getBSeriennrtragend())
						|| Helper.short2boolean(panelArtikel.getArtikelDto()
								.getBChargennrtragend())) {

				} else {
					// Es kann nur das Artikel-Set geaendert werden
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("ls.warning.setartikelnichtaenderbar"));
					return;
				}
			}

			panelArtikel.setArtikelEingabefelderEditable(true);
			

			if (getInternalFrame().bRechtDarfPreiseAendernVerkauf == true) {
				((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
						.setEnabled(true);
			} else {
				((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
						.setEnabled(false);
			}

			setzePositionsartAenderbar(positionDto);
			panelArtikel.setzeEinheitAenderbar();
			panelArtikel.setzeMengeAenderbar(positionDto);
			panelArtikel.wbuLager.setEnabled(false);
			// sobald ich es erlaube, den Artikel der Ident Position umzudrehen,
			// muss ich
			// Logik ueberdenken -> ev. kein Bezug zum Auftrag mehr etc.
			panelArtikel.getWtfArtikel().setEditable(false); // UW 27.04.06

			// PJ20146
			if (positionDto.getWareneingangspositionIIdAndererMandant() != null) {
				panelArtikel.wnfMenge.setEditable(false);

				WareneingangspositionDto weposDto = DelegateFactory
						.getInstance()
						.getWareneingangDelegate()
						.wareneingangspositionFindByPrimaryKey(
								positionDto
										.getWareneingangspositionIIdAndererMandant());

				BestellungDto bsDto = DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.bestellungFindByPrimaryKey(
								DelegateFactory
										.getInstance()
										.getBestellungDelegate()
										.bestellpositionFindByPrimaryKey(
												weposDto.getBestellpositionIId())
										.getBestellungIId());

				DialogFactory.showModalDialog(LPMain
						.getTextRespectUISPr("lp.hint"), LPMain
						.getMessageTextRespectUISPr(
								"ls.lspos.bereitsmitweposverknuepft",
								bsDto.getCNr(), bsDto.getMandantCNr()));

			}

		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		boolean bPositionFuerZugehoerigenArtikelAnlegen = false;
		if (wirdPreisvorschlagsdialogGeradeAngezeit()) {
			return;
		}
		try {
			zwController.setBelegDto(tpLieferschein.getLieferscheinDto());
			zwController.setBelegPositionDto(positionDto);
			zwController
					.setSelectedPositionIId(bFuegeNeuePositionVorDerSelektiertenEin ? tpLieferschein
							.getSelectedIdPositionen() : null);

			calculateFields();

			if (allMandatoryFieldsSetDlg()) {
				boolean bDiePositionSpeichern = true;

				// die folgenden beiden Felder koennten durch eine Berechnung zu
				// gross fuer die DB sein
				bDiePositionSpeichern = HelperClient
						.checkNumberFormat(panelArtikel.wnfNettopreis
								.getBigDecimal());

				if (bDiePositionSpeichern) {
					bDiePositionSpeichern = HelperClient
							.checkNumberFormat(panelArtikel.wnfBruttopreis
									.getBigDecimal());
				}

				if (bDiePositionSpeichern) {
					if (getPositionsartCNr()
							.equalsIgnoreCase(
									LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
						// auf Unterpreisigkeit pruefen

						// PJ 17580
						boolean bAufUnterpreisigkeitPruefen = true;
						if (panelArtikel.getArtikelDto().getArtgruIId() != null) {
							ArtgruDto agDto = DelegateFactory
									.getInstance()
									.getArtikelDelegate()
									.artgruFindByPrimaryKey(
											panelArtikel.getArtikelDto()
													.getArtgruIId());

							if (Helper.short2boolean(agDto
									.getBKeinevkwarnmeldungimls()) == true) {
								bAufUnterpreisigkeitPruefen = false;
							}

						}

						// PJ 17837
						if (!tpLieferschein.getLieferscheinDto()
								.getLieferscheinartCNr()
								.equals(LieferscheinFac.LSART_LIEFERANT)) {
							if (bAufUnterpreisigkeitPruefen == true) {
								// auf Unterpreisigkeit und Verpackungsmittelmenge pruefen
								ArtikelMengenDialogRueckgabe bPositionSpeichernUndNMenge = DialogFactory
										.pruefeUnterpreisigkeitUndMindestVKMengeDlg(getInternalFrame(),
												panelArtikel.getArtikelDto().getIId(),
												tpLieferschein.getLieferscheinDto().getLagerIId(),
												panelArtikel.wnfNettopreis.getBigDecimal(),
												tpLieferschein.getLieferscheinDto()
														.getFWechselkursmandantwaehrungzubelegwaehrung(),
												panelArtikel.wnfMenge.getBigDecimal());

								bDiePositionSpeichern = bPositionSpeichernUndNMenge.isStore();

								if (bPositionSpeichernUndNMenge.isChanged()) {
									panelArtikel.wnfMenge.setBigDecimal(bPositionSpeichernUndNMenge.getAmount());
								}
							}
						}

					}
				}

				if (bDiePositionSpeichern) {
					

					// Menge auf Lager pruefen
					if (getPositionsartCNr().equals(
							LocaleFac.POSITIONSART_IDENT)
							&& Helper.short2boolean(panelArtikel
									.getArtikelDto().getBLagerbewirtschaftet())) {

						BigDecimal bdAktuelleMengeImLieferschein = panelArtikel.wnfMenge
								.getBigDecimal();
						BigDecimal bdBisherigeMengeImLieferschein = positionDto
								.getNMenge();
						BigDecimal bdWievielBraucheIchVomLager; // wenn die beiden
						// Mengen gleich
						// sind oder die
						// neue Menge unter
						// der alten Menge
						// liegt

						if (bdBisherigeMengeImLieferschein != null
								&& bdBisherigeMengeImLieferschein.doubleValue() > 0) {
							bdWievielBraucheIchVomLager = bdAktuelleMengeImLieferschein
									.subtract(bdBisherigeMengeImLieferschein);
						} else {
							bdWievielBraucheIchVomLager = bdAktuelleMengeImLieferschein;
						}
						
						if (!Helper.short2boolean(panelArtikel.getArtikelDto()
								.getBSeriennrtragend())
								&& !Helper
										.short2boolean(panelArtikel
												.getArtikelDto()
												.getBChargennrtragend())) {
							if (bdWievielBraucheIchVomLager.doubleValue() > 0) {

								Integer lagerIId = tpLieferschein
										.getLieferscheinDto().getLagerIId();
								if (panelArtikel.selectedlagerIId != null) {
									lagerIId = panelArtikel.selectedlagerIId;
								}

								bDiePositionSpeichern = tpLieferschein
										.istMengeAufLager(panelArtikel
												.getArtikelDto().getIId(),
												lagerIId, null,
												bdWievielBraucheIchVomLager);

								if (!bDiePositionSpeichern) {

									// bei einer neuen Position als
									// Vorschlagswert die moegliche Menge setzen
									if (positionDto.getIId() == null) {
										panelArtikel.wnfMenge
												.setBigDecimal(DelegateFactory
														.getInstance()
														.getLagerDelegate()
														.getMengeAufLager(
																panelArtikel
																		.getArtikelDto()
																		.getIId(),
																tpLieferschein
																		.getLieferscheinDto()
																		.getLagerIId(),
																null));
									}

									// bei einer bestehenden Position auf den
									// urspruenglichen Wert zuruecksetzen
									else {
										panelArtikel.wnfMenge
												.setBigDecimal(positionDto
														.getNMenge());
									}
								}
							}
						}
					}

					if (bDiePositionSpeichern) {
						// Wenn ein guenstigerer VK-Preis moeglich waere und der
						// Mandantparameter == true,
						// dann den Benutzer darauf hinweisen
						// PJ 17837
						if (!tpLieferschein.getLieferscheinDto()
								.getLieferscheinartCNr()
								.equals(LieferscheinFac.LSART_LIEFERANT)) {
							((PanelPositionenArtikelVerkauf) panelArtikel)
									.checkVkp();
						}

						components2Dto();

						if (positionDto.getIId() == null) {
							if (getPositionsartCNr().equals(
									LocaleFac.POSITIONSART_IDENT)
									|| getPositionsartCNr().equals(
											LocaleFac.POSITIONSART_HANDEINGABE)) {
								boolean isLSArtAuftrag = LieferscheinFac.LSART_AUFTRAG
										.equals(tpLieferschein.getLieferscheinDto().getLieferscheinartCNr());						
								boolean hatFCVerteilung = LPMain.getInstance().getDesktopController()
										.hatZusatzForecastVerteilen();
								
								if(isLSArtAuftrag && !hatFCVerteilung) {
									// einmalige Warnung aussprechen
									if (!intFrame.bWarnungAusgesprochen) {
										if (DialogFactory
												.showMeldung(
														LPMain.getTextRespectUISPr("ls.warning.reduziertnichtdieoffenemengeimauftrag"),
														LPMain.getTextRespectUISPr("lp.warning"),
														javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.NO_OPTION) {
											bDiePositionSpeichern = false;
										}

										intFrame.bWarnungAusgesprochen = true;
									}
								}

								if (getPositionsartCNr().equals(
										LocaleFac.POSITIONSART_IDENT)) {
									// Soll eine Position mit einem eventuellen
									// zugehoerigen Artikel angelegt werden?
									if (positionDto.getIId() == null) {
										bPositionFuerZugehoerigenArtikelAnlegen = DialogFactory
												.pruefeZugehoerigenArtikelAnlegenDlg(
														panelArtikel
																.getArtikelDto(),positionDto.getNMenge(),
														false, this);
									}

									// PJ19985
									if (!hatFCVerteilung && DelegateFactory
											.getInstance()
											.getForecastDelegate()
											.gibtEsDenArtikelIneinemOffenenOderFreigegebenenForecastauftrag(
													positionDto.getArtikelIId(),
													tpLieferschein
															.getLieferscheinDto()
															.getKundeIIdLieferadresse())) {
										if (DialogFactory
												.showMeldung(
														LPMain.getTextRespectUISPr("ls.warning.fcpositionvorhanden"),
														LPMain.getTextRespectUISPr("lp.warning"),
														javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.NO_OPTION) {
											bDiePositionSpeichern = false;
										}
									}
								}
							}

							if (bDiePositionSpeichern) {
								bDiePositionSpeichern = getArtikelsetViewController()
										.validateArtikelsetConstraints(
												positionDto);
							}

							if (bDiePositionSpeichern) {
								// Soll die neue Position vor der aktuell
								// selektierten stehen?
								if (bFuegeNeuePositionVorDerSelektiertenEin) {
									Integer iIdAktuellePosition = (Integer) tpLieferschein
											.getLieferscheinPositionenTop()
											.getSelectedId();

									// die erste Position steht an der Stelle 1
									Integer iSortAktuellePosition = new Integer(
											1);

									if (iIdAktuellePosition != null) {
										iSortAktuellePosition = DelegateFactory
												.getInstance()
												.getLieferscheinpositionDelegate()
												.lieferscheinpositionFindByPrimaryKey(
														iIdAktuellePosition)
												.getISort();

										// Die bestehenden Positionen muessen
										// Platz fuer die neue schaffen
										DelegateFactory
												.getInstance()
												.getLieferscheinpositionDelegate()
												.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
														tpLieferschein
																.getLieferscheinDto()
																.getIId(),
														iSortAktuellePosition
																.intValue());
									}

									// Die neue Position wird an frei gemachte
									// Position gesetzt
									positionDto.setISort(iSortAktuellePosition);
								}

								positionDto
										.setPositioniIdArtikelset(getArtikelsetViewController()
												.getArtikelSetIIdFuerNeuePosition());

								List<SeriennrChargennrMitMengeDto> snrs = getArtikelsetViewController()
										.handleArtikelsetSeriennummern(
												tpLieferschein
														.getLieferscheinDto()
														.getLagerIId(),
												positionDto);
								if (!getArtikelsetViewController()
										.isArtikelsetWithSnrsStoreable(
												positionDto, snrs)) {
									bDiePositionSpeichern = false;
								}

								if (bDiePositionSpeichern) {
									Integer pkPosition = DelegateFactory
											.getInstance()
											.getLieferscheinpositionDelegate()
											.createLieferscheinpositionVerteilen(
													positionDto, snrs, true);
									positionDto.setIId(pkPosition);
									setKeyWhenDetailPanel(pkPosition);
								}
							}
						} else {
							List<SeriennrChargennrMitMengeDto> bekannteSnrs = DelegateFactory
									.getInstance()
									.getLieferscheinpositionDelegate()
									.getSeriennrchargennrForArtikelsetPosition(
											positionDto.getIId());

							List<SeriennrChargennrMitMengeDto> snrs = getArtikelsetViewController()
									.handleArtikelsetSeriennummern(
											tpLieferschein.getLieferscheinDto()
													.getLagerIId(),
											positionDto, bekannteSnrs);

							if (!getArtikelsetViewController()
									.isArtikelsetWithSnrsStoreable(positionDto,
											snrs)) {
								bDiePositionSpeichern = false;
							}

							if (bDiePositionSpeichern) {
								Integer pkPosition = DelegateFactory
										.getInstance()
										.getLieferscheinpositionDelegate()
										.updateLieferscheinposition(
												positionDto, snrs);
								if (pkPosition == null) {
									pkPosition = positionDto.getIId();
								}
								setKeyWhenDetailPanel(pkPosition);
							}

							// Integer pkPosition =
							// DelegateFactory.getInstance()
							// .getLieferscheinpositionDelegate()
							// .updateLieferscheinposition(positionDto);
						}

						if (bDiePositionSpeichern) {
							// buttons schalten
							super.eventActionSave(e, false);

							eventYouAreSelected(false);
						}
					}
				}

				if (bDiePositionSpeichern) {
					bFuegeNeuePositionVorDerSelektiertenEin = false;
					getArtikelsetViewController()
							.resetArtikelSetIIdFuerNeuePosition();
					setModified(true);
				}
			}
		} finally {
			// per Default wird eine neue Position ans Ende der Liste gesetzt
			// bFuegeNeuePositionVorDerSelektiertenEin = false;
		}

		// wenn eine Position fuer einen zugehoerigen Artikel angelegt werden
		// soll,
		// dann muss die Eingabe fuer den zugehoerigen Artikel geoeffnet werden
		if (bPositionFuerZugehoerigenArtikelAnlegen) {
			ArtikelDto artikelDtoZugehoerig = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							panelArtikel.getArtikelDto()
									.getArtikelIIdZugehoerig());
			
			LieferscheinpositionDto lieferscheinpositionDto_Vorgaenger = DelegateFactory
					.getInstance().getLieferscheinpositionDelegate()
					.lieferscheinpositionFindByPrimaryKey(positionDto.getIId());

			
			BigDecimal nMengeZugehoerig = lieferscheinpositionDto_Vorgaenger.getNMenge();
			BigDecimal preisZugehoerig =panelArtikel.getArtikelDto().getNPreisZugehoerigerartikel();
			// PJ19312/PJ21370
						nMengeZugehoerig = multiplikatorZugehoerigerArtikel(nMengeZugehoerig);

			ItemChangedEvent ice = new ItemChangedEvent(this,
					ItemChangedEvent.ACTION_NEW);
			tpLieferschein.getLieferscheinPositionenBottom().eventActionNew(
					ice, true, false);
			tpLieferschein.getLieferscheinPositionenBottom()
					.eventYouAreSelected(false);

			tpLieferschein.getLieferscheinPositionenTop().updateButtons(
					tpLieferschein.getLieferscheinPositionenBottom()
							.getLockedstateDetailMainKey());

			panelArtikel.setArtikelDto(artikelDtoZugehoerig);
			panelArtikel.artikelDto2components();
			panelArtikel.wnfMenge.setBigDecimal(nMengeZugehoerig);

			//PJ21823
			panelArtikel.preisUebersteuern(preisZugehoerig);
			
			panelArtikel.setArtikelEingabefelderEditable(true);
			((PanelPositionenArtikelVerkauf) panelArtikel).wbuPreisauswahl
					.setEnabled(true);

			//SP3912
			wcoPositionsart.setEnabled(false);
			
			positionDto.setPositionIIdZugehoerig(lieferscheinpositionDto_Vorgaenger.getIId());

			int iSortNeu = lieferscheinpositionDto_Vorgaenger.getISort() + 1;

			DelegateFactory
					.getInstance()
					.getLieferscheinpositionDelegate()
					.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
							tpLieferschein.getLieferscheinDto().getIId(), iSortNeu);

			positionDto.setISort(iSortNeu);
			
			panelArtikel.setzeEinheitAenderbar();
			
			boolean bOhneRueckfrage = panelArtikel
					.zugehoerigenArtikelOhneRueckfrageAnlegen();

			if (bOhneRueckfrage) {
				tpLieferschein.getLieferscheinPositionenBottom().eventActionSave(e,
						bNeedNoSaveI);
			}
			
			
		}

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_MINDESTSTANDSWARNUNG,
						ParameterFac.KATEGORIE_LIEFERSCHEIN,
						LPMain.getTheClient().getMandant());

		if ((Boolean) parameter.getCWertAsObject()) {

			wirdLagermindeststandUnterschritten(
					tpLieferschein.getLieferscheinDto().getTBelegdatum(),
					positionDto.getNMenge(),
					positionDto.getArtikelIId(),
					DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getPartnerIIdStandortEinesLagers(
									tpLieferschein.getLieferscheinDto()
											.getLagerIId()));
		}

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (tpLieferschein.istAktualisierenLieferscheinErlaubt()) {

			if (tpLieferschein.getLieferscheinDto().getStatusCNr()
					.equals(LieferscheinFac.LSSTATUS_OFFEN)) {
				boolean b = DialogFactory.showModalJaNeinDialog(
						getInternalFrame(),
						LPMain.getTextRespectUISPr("ls.bereitsoffen"),
						LPMain.getTextRespectUISPr("lp.hint"));
				if (b == false) {
					return;
				}

			} else if (tpLieferschein.getLieferscheinDto().getStatusCNr()
					.equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
				boolean b = DialogFactory.showModalJaNeinDialog(
						getInternalFrame(),
						LPMain.getTextRespectUISPr("ls.bereitsgeliefert"),
						LPMain.getTextRespectUISPr("lp.hint"));
				if (b == false) {
					return;
				}
			}
			Object[] o = tpLieferschein.getLieferscheinPositionenTop()
					.getSelectedIds();
			if (o.length > 1) {
				DelegateFactory.getInstance().getLieferscheinpositionDelegate()
						.removeLieferscheinpositionen(o);
			} else {

				if (positionDto.getWareneingangspositionIIdAndererMandant() != null) {
					panelArtikel.wnfMenge.setEditable(false);

					WareneingangspositionDto weposDto = DelegateFactory
							.getInstance()
							.getWareneingangDelegate()
							.wareneingangspositionFindByPrimaryKey(
									positionDto
											.getWareneingangspositionIIdAndererMandant());

					BestellungDto bsDto = DelegateFactory
							.getInstance()
							.getBestellungDelegate()
							.bestellungFindByPrimaryKey(
									DelegateFactory
											.getInstance()
											.getBestellungDelegate()
											.bestellpositionFindByPrimaryKey(
													weposDto.getBestellpositionIId())
											.getBestellungIId());

					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getMessageTextRespectUISPr(
											"ls.lspos.remove.bereitsmitweposverknuepft",
											bsDto.getCNr(),
											bsDto.getMandantCNr()));
					return;
				}

				DelegateFactory.getInstance().getLieferscheinpositionDelegate()
						.removeLieferscheinposition(positionDto);

			}
			this.setKeyWhenDetailPanel(null);
			super.eventActionDelete(e, false, false); // keyWasForLockMe nicht
			// ueberschreiben
		}

	}

	protected void components2Dto() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.components2Dto(positionDto, tpLieferschein
				.getKundeLieferadresseDto().getPartnerDto()
				.getLocaleCNrKommunikation(), tpLieferschein
				.getLieferscheinDto().getIId());
		// 2. Weiter mit den anderen.

		String positionsart = getPositionsartCNr();

		if (positionsart.equalsIgnoreCase(LocaleFac.POSITIONSART_IDENT)) {
			// Serien/Chargennummern.
			// Wenn Menge 0, dann Seriennummer nicht setzen
			if (positionDto.getNMenge().compareTo(new BigDecimal(0)) != 0) {

				if (Helper.short2boolean(panelArtikel.getArtikelDto()
						.getBSeriennrtragend())
						|| Helper.short2boolean(panelArtikel.getArtikelDto()
								.getBChargennrtragend())) {
					positionDto
							.setSeriennrChargennrMitMenge(((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
									.getSeriennummern());
				} else {
					positionDto.setSeriennrChargennrMitMenge(null);
				}

			} else {
				positionDto.setSeriennrChargennrMitMenge(null);
			}
			// hier zusaetzlich gespeicherte Betraege.
			positionDto.setNRabattbetrag(panelArtikel.wnfRabattsumme
					.getBigDecimal());
			positionDto.setNMwstbetrag(panelArtikel.wnfMwstsumme
					.getBigDecimal());
		} else if (positionsart
				.equalsIgnoreCase(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {
			// hier zusaetzlich gespeicherte Betraege.
			positionDto.setNRabattbetrag(panelHandeingabe.wnfRabattsumme
					.getBigDecimal());
			positionDto.setNMwstbetrag(panelHandeingabe.wnfMwstsumme
					.getBigDecimal());
		} else if (positionsart
				.equalsIgnoreCase(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_INTELLIGENTE_ZWISCHENSUMME)) {

			Integer lastZwsPositionIId = panelIntZwischensumme.components2Dto(
					tpLieferschein.getLieferscheinDto(), positionDto);
			LieferscheinpositionDto lastPosition = DelegateFactory
					.getInstance().getLieferscheinpositionDelegate()
					.lieferscheinpositionFindByPrimaryKey(lastZwsPositionIId);
			positionDto.setMwstsatzIId(lastPosition.getMwstsatzIId());
		}
	}

	public void setModified(boolean changed) {
		zwController.setChanged(changed);
	}

	private void aktualisiereStatusbar() throws Throwable {
		// der Lieferschein koennte von aussen geaendert worden sein
		tpLieferschein.setLieferscheinDto(DelegateFactory
				.getInstance()
				.getLsDelegate()
				.lieferscheinFindByPrimaryKey(
						tpLieferschein.getLieferscheinDto().getIId()));

		setStatusbarPersonalIIdAnlegen(tpLieferschein.getLieferscheinDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpLieferschein.getLieferscheinDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpLieferschein.getLieferscheinDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpLieferschein.getLieferscheinDto().getTAendern());
		setStatusbarStatusCNr(tpLieferschein.getLieferscheinStatus());

		setStatusbarSpalte5(this
				.getLagerstandFuerStatusbarSpalte5(tpLieferschein
						.getLieferscheinDto().getLagerIId()));

		Integer iaktuelleLager = null;
		if (tpLieferschein.getLieferscheinDto().getZiellagerIId() != null) {
			iaktuelleLager = tpLieferschein.getLieferscheinDto()
					.getZiellagerIId();
		} else {
			iaktuelleLager = tpLieferschein.getLieferscheinDto().getLagerIId();
		}
		((PanelPositionenArtikelVerkaufSNR) panelArtikel).wtfSerienchargennummer
				.getText();
		/*
		 * String serienchargennummer = null;
		 * 
		 * if (((PanelPositionenArtikelVerkaufSNR)
		 * panelArtikel).wtfSerienchargennummer .getText() != null &&
		 * ((PanelPositionenArtikelVerkaufSNR)
		 * panelArtikel).wtfSerienchargennummer .getText().length() > 0) {
		 * serienchargennummer = ((PanelPositionenArtikelVerkaufSNR)
		 * panelArtikel).wtfSerienchargennummer .getText(); }
		 * 
		 * if (panelArtikel.getArtikelDto().getIId() != null &&
		 * panelArtikel.getArtikelDto().getBLagerbewirtschaftet()
		 * .equals(Helper.boolean2Short(true))) { // das Lager des Lieferscheins
		 * und Lagerstand des aktuellen Artikel String lagerinfo =
		 * DelegateFactory.getInstance().getLagerDelegate()
		 * .lagerFindByPrimaryKey(iaktuelleLager).getCNr(); if
		 * (panelArtikel.wnfMenge.getDouble().doubleValue() > 0) { BigDecimal
		 * ddMenge = DelegateFactory.getInstance()
		 * .getLagerDelegate().getMengeAufLager(
		 * panelArtikel.getArtikelDto().getIId(), iaktuelleLager,
		 * serienchargennummer);
		 * 
		 * lagerinfo += ": "; lagerinfo += ddMenge; }
		 * setStatusbarSpalte5(lagerinfo); } else { setStatusbarSpalte5(""); }
		 */
	}

	protected void eventActionPrint(HvActionEvent e) throws Throwable {

		if (e.isMouseEvent() && e.isRightButtonPressed()) {

			boolean bStatusAngelegt = tpLieferschein.getLieferscheinDto()
					.getStatusCNr()
					.equals(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT);
			boolean bKonditionen = tpLieferschein.pruefeKonditionen();
			boolean bRecht = (getCachedRights()
					.getValueOfKey(RechteFac.RECHT_LS_LIEFERSCHEIN_CUD));

			if (bStatusAngelegt && bKonditionen && bRecht) {
				DelegateFactory
						.getInstance()
						.getLsDelegate()
						.berechneAktiviereBelegControlled(
								tpLieferschein.getLieferscheinDto().getIId());
				eventActionRefresh(e, false);
			} else if (!bStatusAngelegt) {
				DialogFactory.showModalDialog("Status", LPMain
						.getMessageTextRespectUISPr("status.zustand",
								LPMain.getTextRespectUISPr("ls.lieferschein"),
								tpLieferschein.getLieferscheinStatus().trim()));
			}

		} else {
			tpLieferschein.printLieferschein();
		}

		eventYouAreSelected(false);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LIEFERSCHEIN;
	}

	public void positionMitForecastdatenVorbesetzen(Integer forecastpositionIId)
			throws Throwable {
		if (positionDto != null && positionDto.getIId() == null) {

			ForecastpositionDto fpDto = DelegateFactory.getInstance()
					.getForecastDelegate()
					.forecastpositionFindByPrimaryKey(forecastpositionIId);

			ArtikelDto aDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(fpDto.getArtikelIId());

			wcoPositionsart.setKeyOfSelectedItem(LocaleFac.POSITIONSART_IDENT);
			positionDto.setForecastpositionIId(forecastpositionIId);

			panelArtikel.setArtikelDto(aDto);
			panelArtikel.wnfMenge.setBigDecimal(fpDto.getNMenge());

			if (panelArtikel instanceof PanelPositionenArtikelVerkauf) {
				((PanelPositionenArtikelVerkauf) panelArtikel)
						.berechneVerkaufspreis(false);
			}

			setzeForecastdaten(forecastpositionIId);
		}
	}

	/**
	 * Eigene ExceptionLP's verarbeiten. myexception: 2 methode ueberschreiben
	 * 
	 * @return boolean
	 * @param exfc
	 *            ExceptionLP
	 * @throws Throwable
	 */
	public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
		/*
		 * if (exfc.getICode() ==
		 * EJBExceptionLP.FEHLER_MENGENREDUZIERUNG_NICHT_MOEGLICH) {
		 * DialogFactory.showModalDialog(LPMain
		 * .getTextRespectUISPr("lp.error"), LPMain
		 * .getTextRespectUISPr("bes.mengenreduzierung")); return true; } else {
		 * // myexception: 4 konnte nicht verarbeitet werden return false; }
		 */
		return false;
	}

	public void wnfPauschalposition_focusLost(FocusEvent e) {
		super.wnfPauschalposition_focusLost(e);
		try {
			DelegateFactory
					.getInstance()
					.getLieferscheinpositionDelegate()
					.berechnePauschalposition(
							wnfPauschalpositionpreis.getBigDecimal(),
							positionDto.getIId(),
							positionDto.getLieferscheinIId());
		} catch (ExceptionLP e1) {
		} catch (Throwable e1) {
		}
	}

	private void setEditorButtonColor() {
		getHmOfButtons()
				.get(ACTION_TEXT)
				.getButton()
				.setIcon(
						positionDto.getXTextinhalt() != null
								&& positionDto.getXTextinhalt().length() > 0 ? IconFactory
								.getCommentExist() : IconFactory
								.getEditorEdit());
	}

	private void resetEditorButton() {
		getHmOfButtons().get(ACTION_TEXT).getButton()
				.setIcon(IconFactory.getEditorEdit());
	}

}
