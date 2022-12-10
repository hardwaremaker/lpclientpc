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
package com.lp.client.reklamation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EventObject;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.WrapperSNRFieldLimLength;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSNRField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTelefonField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.rechnung.RechnungFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.zeiterfassung.ZeiterfassungFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerkommunikationDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.reklamation.service.AufnahmeartDto;
import com.lp.server.reklamation.service.BehandlungDto;
import com.lp.server.reklamation.service.FehlerangabeDto;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.GotoHelper;
import com.lp.util.Helper;

public class PanelReklamation extends PanelBasis {

	private static final long serialVersionUID = 1L;

	private ReklamationDto reklamationDto = null;
	private InternalFrameReklamation internalFrameReklamation = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private ButtonGroup buttonKundeUnterart = new ButtonGroup();
	private WrapperRadioButton wrbKundeunterartFertigung = new WrapperRadioButton();
	private WrapperRadioButton wrbKundeunterartLieferant = new WrapperRadioButton();

	private PanelQueryFLR panelQueryFLRKostenstelle = null;
	private PanelQueryFLR panelQueryFLRPersonal = null;
	private PanelQueryFLR panelQueryFLRAufnahmeart = null;
	private PanelQueryFLR panelQueryFLRFehlerangabe = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner_Lieferant = null;
	private PanelQueryFLR panelQueryFLRKunde = null;
	private PanelQueryFLR panelQueryFLRLieferschein = null;
	private PanelQueryFLR panelQueryFLRLieferant = null;
	private PanelQueryFLR panelQueryFLRBestellung = null;
	private PanelQueryFLR panelQueryFLRWareneingang = null;
	private PanelQueryFLR panelQueryFLRRechnung = null;
	private PanelQueryFLR panelQueryFLRLos = null;
	private PanelQueryFLR panelQueryFLRMaschine = null;
	private PanelQueryFLR panelQueryFLRVerursacher = null;
	private PanelQueryFLR panelQueryFLRArbeitsgang = null;
	private PanelQueryFLR panelQueryFLRSnrChnrAuswahl = null;

	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();

	private WrapperLabel wlaGrund = new WrapperLabel();
	private WrapperTextField wtfGrund = new WrapperTextField();

	private WrapperLabel wlaKdReklaNr = new WrapperLabel();
	private WrapperTextField wtfKdReklaNr = new WrapperTextField();
	private WrapperLabel wlaKdLsNr = new WrapperLabel();
	private WrapperTextField wtfKdLsNr = new WrapperTextField();

	private WrapperLabel wlaTelAnsp = new WrapperLabel();
	private WrapperTelefonField wtfTelAnsp = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);

	private WrapperLabel wlaTelAnspLieferant = new WrapperLabel();
	private WrapperTelefonField wtfTelAnspLieferant = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);

	private WrapperLabel wlaProjekt = new WrapperLabel();
	private WrapperTextField wtfProjekt = new WrapperTextField();

	private ButtonGroup buttonGroup1 = new ButtonGroup();

	private WrapperLabel wlaReklamationart = new WrapperLabel();
	private WrapperComboBox wcoReklamationart = new WrapperComboBox();

	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperDateField wdfDatum = new WrapperDateField();

	private WrapperLabel wlaWareErhalten = new WrapperLabel();
	private WrapperDateField wdfWareErhalten = new WrapperDateField();

	private WrapperTextField wtfHandartikel = new WrapperTextField();
	private WrapperButton wbuPersonal = new WrapperButton();
	private WrapperTextField wtfPersonal = new WrapperTextField();

	private WrapperButton wbuVerursacher = new WrapperButton();
	private WrapperTextField wtfVerursacher = new WrapperTextField();

	private WrapperButton wbuArbeitsgang = new WrapperButton();
	private WrapperTextField wtfArbeitsgang = new WrapperTextField();

	private WrapperIdentField wifArtikel = null;

	private WrapperRadioButton wrbArtikel = new WrapperRadioButton();
	private WrapperRadioButton wrbHandeingabe = new WrapperRadioButton();
	private WrapperTextField wtfKostenstelle = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
	private WrapperButton wbuKostenstelle = new WrapperButton();

	private WrapperGotoButton wbuKunde = new WrapperGotoButton(GotoHelper.GOTO_KUNDE_AUSWAHL);
	private WrapperTextField wtfKunde = new WrapperTextField();

	private WrapperGotoButton wbuLieferant = new WrapperGotoButton(GotoHelper.GOTO_LIEFERANT_AUSWAHL);
	private WrapperTextField wtfLieferant = new WrapperTextField();
	private WrapperGotoButton wbuBestellung = new WrapperGotoButton(GotoHelper.GOTO_BESTELLUNG_AUSWAHL);
	private WrapperButton wbuWareneingang = new WrapperButton();
	private WrapperTextField wtfBestellung = new WrapperTextField();
	private WrapperTextField wtfWareneingang = new WrapperTextField();
	private WrapperGotoButton wbuRechnung = new WrapperGotoButton(GotoHelper.GOTO_RECHNUNG_AUSWAHL);
	private WrapperTextField wtfRechnung = new WrapperTextField();
	private WrapperGotoButton wbuLieferschein = new WrapperGotoButton(GotoHelper.GOTO_LIEFERSCHEIN_AUSWAHL);
	private WrapperTextField wtfLieferschein = new WrapperTextField();
	private WrapperButton wbuAnsprechpartner = new WrapperButton();
	private WrapperTextField wtfAnsprechpartner = new WrapperTextField();
	private WrapperButton wbuAnsprechpartnerLieferant = new WrapperButton();
	private WrapperTextField wtfAnsprechpartnerLieferant = new WrapperTextField();
	private WrapperGotoButton wbuLos = new WrapperGotoButton(GotoHelper.GOTO_FERTIGUNG_AUSWAHL);
	private WrapperTextField wtfLos = new WrapperTextField();

	private WrapperLabel wlaKundeLos = new WrapperLabel();
	private WrapperLabel wlaMaschinengruppe = new WrapperLabel();

	private WrapperButton wbuMaschine = new WrapperButton();
	private WrapperTextField wtfMaschine = new WrapperTextField();

	private WrapperLabel wlaBeurteilung = new WrapperLabel();

	private WrapperButton wbuFehlerangabe = new WrapperButton();
	private WrapperTextField wtfFehlerangabe = new WrapperTextField();
	private WrapperButton wbuAufnahmeart = new WrapperButton();
	private WrapperTextField wtfAufnahmeart = new WrapperTextField();

	private WrapperCheckBox wcbFremdprodukt = new WrapperCheckBox();

	private WrapperLabel wlaGrundKommentar = new WrapperLabel();
	private WrapperEditorField wefGrundKommentar = new WrapperEditorFieldKommentar(getInternalFrame(),
			LPMain.getTextRespectUISPr("lp.bemerkung"));

	private WrapperSelectField wsfProjekt = new WrapperSelectField(WrapperSelectField.PROJEKT, getInternalFrame(),
			true);

	private WrapperButton wbuSnrChnr = new WrapperButton();
	private WrapperSNRField wtfSnrChnr = new WrapperSNRFieldLimLength(80);
	static final public String ACTION_SPECIAL_SNRCHNR_FROM_LISTE = "ACTION_SPECIAL_SNRCHNR_FROM_LISTE";

	static final public String ACTION_SPECIAL_KOSTENSTELLE_ABTEILUNG_FROM_LISTE = "action_kostenstelle_from_liste";
	static final public String ACTION_SPECIAL_PERSONAL_FROM_LISTE = "action_personal_from_liste";
	static final public String ACTION_SPECIAL_LOSSOLLARBEITSPLAN_FROM_LISTE = "action_lossollarbeitsplan_from_liste";
	static final public String ACTION_SPECIAL_VERURSACHER_FROM_LISTE = "action_verursacher_from_liste";

	static final public String ACTION_SPECIAL_AUFNAHMEART_FROM_LISTE = "ACTION_SPECIAL_AUFNAHMEART_FROM_LISTE";
	static final public String ACTION_SPECIAL_FEHLERANGABE_FROM_LISTE = "ACTION_SPECIAL_FEHLERANGABE_FROM_LISTE";

	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "ACTION_SPECIAL_LOS_FROM_LISTE";
	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "ACTION_SPECIAL_KUNDE_FROM_LISTE";
	static final public String ACTION_SPECIAL_LIEFERANT_FROM_LISTE = "ACTION_SPECIAL_LIEFERANT_FROM_LISTE";
	static final public String ACTION_SPECIAL_BESTELLUNG_FROM_LISTE = "ACTION_SPECIAL_BESTELLUNG_FROM_LISTE";
	static final public String ACTION_SPECIAL_WARENEINGANG_FROM_LISTE = "ACTION_SPECIAL_WARENEINGANG_FROM_LISTE";
	static final public String ACTION_SPECIAL_RECHNUNG_FROM_LISTE = "ACTION_SPECIAL_RECHNUNG_FROM_LISTE";
	static final public String ACTION_SPECIAL_LIEFERSCHEIN_FROM_LISTE = "ACTION_SPECIAL_LIEFERSCHEIN_FROM_LISTE";
	static final public String ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE = "ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE";
	static final public String ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT_FROM_LISTE = "ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT_FROM_LISTE";

	static final public String ACTION_SPECIAL_MASCHINE_FROM_LISTE = "ACTION_SPECIAL_MASCHINE_FROM_LISTE";

	private boolean bBestellungWEManuellEingeben = false;

	public InternalFrameReklamation getInternalFrameReklamation() {
		return internalFrameReklamation;
	}

	public PanelReklamation(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameReklamation = (InternalFrameReklamation) internalFrame;

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_LIEFERANTENREKLAMATION_BESTELLUNG_MANUELL,
						ParameterFac.KATEGORIE_REKLAMATION, LPMain.getTheClient().getMandant());

		bBestellungWEManuellEingeben = (Boolean) parameter.getCWertAsObject();

		jbInit();
		setDefaults();
		initComponents();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	protected void prepareNewDetailsPanel() throws Throwable {
		leereAlleFelder(this);

		try {
			// ParametermandantDto parameter = (ParametermandantDto)
			// DelegateFactory
			// .getInstance()
			// .getParameterDelegate()
			// .getParametermandant(
			// ParameterFac.PARAMETER_DEFAULT_KOSTENSTELLE,
			// ParameterFac.KATEGORIE_ALLGEMEIN,
			// LPMain.getInstance().getTheClient().getMandant());
			// KostenstelleDto dto = DelegateFactory
			// .getInstance()
			// .getSystemDelegate()
			// .kostenstelleFindByNummerMandant(parameter.getCWert(),
			// LPMain.getInstance().getTheClient().getMandant());
			//
			// reklamationDto.setKostenstelleIId(dto.getIId());
			// wtfKostenstelle.setText(dto.getCNr());

			MandantDto mandantDto = DelegateFactory.getInstance().getMandantDelegate()
					.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant());

			reklamationDto.setKostenstelleIId(mandantDto.getIIdKostenstelle());

			KostenstelleDto kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
					.kostenstelleFindByPrimaryKey(mandantDto.getIIdKostenstelle());

			wtfKostenstelle.setText(kostenstelleDto.getCNr());

			wdfDatum.setDate(new Date(System.currentTimeMillis()));

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_KUNDENREKLAMATION_DEFAULT,
							ParameterFac.KATEGORIE_REKLAMATION, LPMain.getTheClient().getMandant());
			Integer i = (Integer) parameter.getCWertAsObject();

			if (i.intValue() == ReklamationFac.REKLAMATION_KUNDEUNTERART_FERTIGUNG) {
				wrbKundeunterartFertigung.setSelected(true);
			} else {
				wrbKundeunterartLieferant.setSelected(true);
			}

		} catch (Throwable ex) {
			// Kein Mandantparameter vorhanden;
		}
		reklamationDto.setPersonalIIdAufnehmer(LPMain.getTheClient().getIDPersonal());

		PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
				.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());
		wtfPersonal.setText(personalDto.formatAnrede());
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);

		Integer key = getInternalFrameReklamation().getReklamationDto().getIId();
		if (key != null) {
			reklamationDto = DelegateFactory.getInstance().getReklamationDelegate()
					.reklamationFindByPrimaryKey(getInternalFrameReklamation().getReklamationDto().getIId());

			dto2Components();
			refreshDetailsGrid();

			getInternalFrameReklamation().getTabbedPaneReklamation().refreshTitle();
		} else {
			prepareNewDetailsPanel();
		}
	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	protected void dto2Components() throws Throwable {

		wcoReklamationart.setKeyOfSelectedItem(reklamationDto.getReklamationartCNr());

		if (reklamationDto.getReklamationartCNr().equals(ReklamationFac.REKLAMATIONART_KUNDE)) {

			if (reklamationDto.getIKundeunterart() != null) {
				if (reklamationDto.getIKundeunterart()
						.intValue() == ReklamationFac.REKLAMATION_KUNDEUNTERART_FERTIGUNG) {
					wrbKundeunterartFertigung.setSelected(true);
				} else {
					wrbKundeunterartLieferant.setSelected(true);
				}
			}

		} else {
			reklamationDto.setIKundeunterart(null);
		}

		wdfDatum.setTimestamp(reklamationDto.getTBelegdatum());
		wdfWareErhalten.setTimestamp(reklamationDto.getTWareErhalten());
		if (Helper.short2boolean(reklamationDto.getBArtikel())) {
			wrbArtikel.setSelected(true);

			ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(reklamationDto.getArtikelIId());
			wifArtikel.setArtikelDto(artikelDto);

			wrbHandeingabe.setSelected(false);
			wtfHandartikel.setActivatable(false);
			wtfHandartikel.setText("");
			wtfHandartikel.setEditable(false);
		} else {
			wrbHandeingabe.setSelected(true);
			wtfHandartikel.setText(reklamationDto.getCHandartikel());

			wrbArtikel.setSelected(false);
			wtfHandartikel.setActivatable(true);
		}
		wcbFremdprodukt.setShort(reklamationDto.getBFremdprodukt());
		wnfMenge.setBigDecimal(reklamationDto.getNMenge());
		wtfGrund.setText(reklamationDto.getCGrund());
		/*
		 * wtfTelAnsp.setText(reklamationDto.getCTelansprechpartner());
		 * wtfTelAnspLieferant.setText(reklamationDto
		 * .getCTelansprechpartnerLieferant());
		 */
		wtfProjekt.setText(reklamationDto.getCProjekt());

		wsfProjekt.setKey(reklamationDto.getProjektIId());

		wtfSnrChnr.setText(reklamationDto.getCSeriennrchargennr());

		wlaMaschinengruppe.setText("");

		if (reklamationDto.getMaschineIId() != null) {
			MaschineDto maschineDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.maschineFindByPrimaryKey(reklamationDto.getMaschineIId());
			wtfMaschine.setText(maschineDto.getBezeichnung());
			if (maschineDto.getMaschinengruppeIId() != null) {
				wlaMaschinengruppe.setText(LPMain.getTextRespectUISPr("pers.maschinengruppe") + ": "
						+ DelegateFactory.getInstance().getZeiterfassungDelegate()
								.maschinengruppeFindByPrimaryKey(maschineDto.getMaschinengruppeIId()).getCBez());
			}

		} else {
			wtfMaschine.setText("");
		}

		wefGrundKommentar.setText(reklamationDto.getXGrundLang());

		KostenstelleDto kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
				.kostenstelleFindByPrimaryKey(reklamationDto.getKostenstelleIId());
		wtfKostenstelle.setText(kostenstelleDto.formatKostenstellenbezeichnung());

		PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
				.personalFindByPrimaryKey(reklamationDto.getPersonalIIdAufnehmer());
		wtfPersonal.setText(personalDto.formatAnrede());

		if (reklamationDto.getPersonalIIdVerursacher() != null) {

			PersonalDto personalDtoVerursacher = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(reklamationDto.getPersonalIIdVerursacher());
			wtfVerursacher.setText(personalDtoVerursacher.formatAnrede());
		} else {
			wtfVerursacher.setText("");
		}

		AufnahmeartDto aufnahmeartDto = DelegateFactory.getInstance().getReklamationDelegate()
				.aufnahmeartFindByPrimaryKey(reklamationDto.getAufnahmeartIId());
		wtfAufnahmeart.setText(aufnahmeartDto.getCBez());

		FehlerangabeDto fehlerangabeDto = DelegateFactory.getInstance().getReklamationDelegate()
				.fehlerangabeFindByPrimaryKey(reklamationDto.getFehlerangabeIId());
		wtfFehlerangabe.setText(fehlerangabeDto.getCBez());

		if (reklamationDto.getAnsprechpartnerIId() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(reklamationDto.getAnsprechpartnerIId());

			wtfAnsprechpartner.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
		} else {
			wtfAnsprechpartner.setText(null);
		}

		if (reklamationDto.getAnsprechpartnerIIdLieferant() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(reklamationDto.getAnsprechpartnerIIdLieferant());

			wtfAnsprechpartnerLieferant.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
		} else {
			wtfAnsprechpartnerLieferant.setText(null);
		}

		if (reklamationDto.getBestellungIId() != null) {
			BestellungDto bestellungDto = DelegateFactory.getInstance().getBestellungDelegate()
					.bestellungFindByPrimaryKey(reklamationDto.getBestellungIId());
			wtfBestellung.setText(bestellungDto.getCNr());

			wbuBestellung.setOKey(bestellungDto.getIId());

		} else {
			wbuBestellung.setOKey(null);
			if (bBestellungWEManuellEingeben == true) {
				wtfBestellung.setText(reklamationDto.getCBestellnummer());

			} else {
				wtfBestellung.setText(null);
			}

		}

		if (reklamationDto.getLieferscheinIId() != null) {
			LieferscheinDto lieferscheinDto = DelegateFactory.getInstance().getLsDelegate()
					.lieferscheinFindByPrimaryKey(reklamationDto.getLieferscheinIId());
			wtfLieferschein.setText(lieferscheinDto.getCNr());
			wbuLieferschein.setOKey(lieferscheinDto.getIId());

		} else {
			wtfLieferschein.setText(null);
			wbuLieferschein.setOKey(null);
		}
		wlaKundeLos.setText("");

		wtfArbeitsgang.setText("");

		if (reklamationDto.getLosIId() != null) {
			LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
					.losFindByPrimaryKey(reklamationDto.getLosIId());
			wtfLos.setText(losDto.getCNr());

			wbuLos.setOKey(losDto.getIId());

			if (losDto.getAuftragIId() != null || losDto.getKundeIId() != null) {

				KundeDto kundeDto = null;
				if (losDto.getAuftragIId() != null) {
					kundeDto = DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByPrimaryKey(DelegateFactory.getInstance().getAuftragDelegate()
									.auftragFindByPrimaryKey(losDto.getAuftragIId()).getKundeIIdAuftragsadresse());
				} else {
					kundeDto = DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByPrimaryKey(losDto.getKundeIId());
				}

				wlaKundeLos.setText(LPMain.getTextRespectUISPr("label.kunde") + ": "
						+ kundeDto.getPartnerDto().formatFixTitelName1Name2());
			}

			if (reklamationDto.getLossollarbeitsplanIId() != null) {
				LossollarbeitsplanDto sollarbeitsplanDto = DelegateFactory.getInstance().getFertigungDelegate()
						.lossollarbeitsplanFindByPrimaryKey(reklamationDto.getLossollarbeitsplanIId());

				String sollarb = "AG:" + sollarbeitsplanDto.getIArbeitsgangnummer();
				if (sollarbeitsplanDto.getIUnterarbeitsgang() != null) {
					sollarb += " UAG:" + sollarbeitsplanDto.getIUnterarbeitsgang();
				}
				wtfArbeitsgang.setText(sollarb);
			}

		} else {
			wtfLos.setText(null);
			wbuLos.setOKey(null);
		}
		if (reklamationDto.getWareneingangIId() != null) {
			WareneingangDto wareneingangDto = DelegateFactory.getInstance().getWareneingangDelegate()
					.wareneingangFindByPrimaryKey(reklamationDto.getWareneingangIId());
			wtfWareneingang.setText(wareneingangDto.getCLieferscheinnr());

		} else {
			if (bBestellungWEManuellEingeben == true) {
				wtfWareneingang.setText(reklamationDto.getCWareneingang());

			} else {
				wtfWareneingang.setText(null);
			}

		}

		if (reklamationDto.getBehandlungIId() != null) {
			BehandlungDto bDto = DelegateFactory.getInstance().getReklamationDelegate()
					.behandlungFindByPrimaryKey(reklamationDto.getBehandlungIId());

			String s = LPMain.getTextRespectUISPr("lp.beurteilung") + ": " + bDto.getCNr();
			if (reklamationDto.getTErledigt() != null) {
				s += ", " + LPMain.getTextRespectUISPr("fert.erledigtam") + " "
						+ Helper.formatDatum(reklamationDto.getTErledigt(), LPMain.getTheClient().getLocUi());

			}
			wlaBeurteilung.setText(s);
		} else {
			wlaBeurteilung.setText("");
		}

		if (reklamationDto.getLieferantIId() != null) {
			LieferantDto lieferantDtoNew = DelegateFactory.getInstance().getLieferantDelegate()
					.lieferantFindByPrimaryKey(reklamationDto.getLieferantIId());
			wtfLieferant.setText(lieferantDtoNew.getPartnerDto().formatFixTitelName1Name2());

			wbuLieferant.setOKey(lieferantDtoNew.getIId());

		} else {
			wtfLieferant.setText(null);
			wbuLieferant.setOKey(null);
		}

		if (reklamationDto.getKundeIId() != null) {
			KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(reklamationDto.getKundeIId());
			wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());

			wbuKunde.setOKey(kundeDto.getIId());

		} else {
			wtfKunde.setText(null);
			wbuKunde.setOKey(null);
		}
		if (reklamationDto.getRechnungIId() != null) {
			RechnungDto rechnungDto = DelegateFactory.getInstance().getRechnungDelegate()
					.rechnungFindByPrimaryKey(reklamationDto.getRechnungIId());
			wtfRechnung.setText(rechnungDto.getCNr());

			wbuRechnung.setOKey(rechnungDto.getIId());

		} else {
			wtfRechnung.setText(null);
			wbuRechnung.setOKey(null);
		}

		if (reklamationDto.getReklamationartCNr().equals(ReklamationFac.REKLAMATIONART_LIEFERANT)) {
			wtfKdLsNr.setText(reklamationDto.getCLflsnr());
			wtfKdReklaNr.setText(reklamationDto.getCLfreklanr());
		} else {
			wtfKdLsNr.setText(reklamationDto.getCKdlsnr());
			wtfKdReklaNr.setText(reklamationDto.getCKdreklanr());
		}

		refreshTelAnsprechpartner();

		this.setStatusbarPersonalIIdAendern(reklamationDto.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(reklamationDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(reklamationDto.getTAnlegen());
		this.setStatusbarTAendern(reklamationDto.getTAendern());
		this.setStatusbarSpalte5(reklamationDto.getStatusCNr());
	}

	private void dialogQueryMaschineFromListe() throws Throwable {
		panelQueryFLRMaschine = ZeiterfassungFilterFactory.getInstance().createPanelFLRMaschinen(getInternalFrame(),
				reklamationDto.getMaschineIId());
		new DialogQuery(panelQueryFLRMaschine);

	}

	private void dialogQueryLieferschein(ActionEvent e) throws Throwable {

		FilterKriterium[] filters = null;

		if (reklamationDto.getKundeIId() != null) {
			filters = new FilterKriterium[2];
			filters[0] = new FilterKriterium(LieferscheinFac.FLR_LIEFERSCHEIN_KUNDE_I_ID_RECHNUNGSADRESSE, true,
					reklamationDto.getKundeIId() + "", FilterKriterium.OPERATOR_EQUAL, false);
			filters[1] = new FilterKriterium(LieferscheinFac.FLR_LIEFERSCHEIN_LIEFERSCHEINSTATUS_STATUS_C_NR, true,
					"('" + LieferscheinFac.LSSTATUS_STORNIERT + "','" + LieferscheinFac.LSSTATUS_ANGELEGT + "')",
					FilterKriterium.OPERATOR_NOT_IN, false);

			if (wifArtikel.getArtikelDto() != null && wifArtikel.getArtikelDto().getIId() != null) {
				// todo

			}
		}

		String sTitle = LPMain.getTextRespectUISPr("ls.title.tooltip.auswahl");
		panelQueryFLRLieferschein = LieferscheinFilterFactory.getInstance()
				.createPanelQueryFLRLieferschein(getInternalFrame(), filters, sTitle, null, true);
		new DialogQuery(panelQueryFLRLieferschein);
	}

	void dialogQueryPersonalFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRPersonal = PersonalFilterFactory.getInstance().createPanelFLRPersonal(getInternalFrame(), true,
				false, reklamationDto.getPersonalIIdAufnehmer());
		new DialogQuery(panelQueryFLRPersonal);
	}

	void dialogQueryVerursacherFromListe(Integer lossollarbeitsplanIId) throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance().createButtonArray(false, true);

		FilterKriterium filter[] = new FilterKriterium[2];
		filter[0] = new FilterKriterium("mandant_c_nr", true, "'" + LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		filter[1] = new FilterKriterium(PersonalFac.FLR_PERSONAL_B_VERSTECKT, true, "(1)",
				FilterKriterium.OPERATOR_NOT_IN, false);

		panelQueryFLRVerursacher = new PanelQueryFLR(PersonalFilterFactory.getInstance().createQTPersonal(),
				SystemFilterFactory.getInstance().createFKMandantCNr(LPMain.getTheClient().getMandant()),
				QueryParameters.UC_ID_PERSONAL, aWhichButtonIUse, getInternalFrame(),
				LPMain.getTextRespectUISPr("title.personalauswahlliste"));

		if (lossollarbeitsplanIId != null) {
			ArrayList<Integer> alPersonalIIds = DelegateFactory.getInstance().getFertigungDelegate()
					.getAllPersonalIIdEinesSollarbeitsplansUeberLogGutSchlecht(lossollarbeitsplanIId);

			if (alPersonalIIds.size() > 0) {

				String krit = "(";

				for (int i = 0; i < alPersonalIIds.size(); i++) {
					krit += alPersonalIIds.get(i);

					if (i < alPersonalIIds.size() - 1) {
						krit += ",";
					}
				}
				krit += ")";
				FilterKriterium[] fkAlle = new FilterKriterium[1];
				fkAlle[0] = new FilterKriterium("i_id", true, krit, FilterKriterium.OPERATOR_IN, false);

				panelQueryFLRVerursacher.befuelleFilterkriteriumSchnellansicht(fkAlle);
				panelQueryFLRVerursacher.getCbSchnellansicht().setText("Nur Personen des Arbeitsgangs");

			}
		}
		panelQueryFLRVerursacher.befuellePanelFilterkriterienDirektUndVersteckte(
				PersonalFilterFactory.getInstance().createFKDPersonalname(),
				PersonalFilterFactory.getInstance().createFKDPersonalnummer(),
				PersonalFilterFactory.getInstance().createFKVPersonal());
		panelQueryFLRVerursacher.addDirektFilter(PersonalFilterFactory.getInstance().createFKDAusweis());
		if (reklamationDto.getPersonalIIdVerursacher() != null) {
			panelQueryFLRVerursacher.setSelectedId(reklamationDto.getPersonalIIdVerursacher());
		}

		new DialogQuery(panelQueryFLRVerursacher);
	}

	void dialogQuerySnrChnrFromListe() throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance().createButtonArray(false, true);

		FilterKriterium[] filtersI = new FilterKriterium[2];

		String key = (String) wcoReklamationart.getKeyOfSelectedItem();
		if (key.equals(ReklamationFac.REKLAMATIONART_FERTIGUNG)) {
			filtersI[0] = new FilterKriterium("" + LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR, true,
					"('" + LocaleFac.BELEGART_LOS + "','" + LocaleFac.BELEGART_LOSABLIEFERUNG + "')",
					FilterKriterium.OPERATOR_IN, false);
		} else if (key.equals(ReklamationFac.REKLAMATIONART_KUNDE)) {
			filtersI[0] = new FilterKriterium("" + LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR, true,
					"('" + LocaleFac.BELEGART_LIEFERSCHEIN + "','" + LocaleFac.BELEGART_RECHNUNG + "')",
					FilterKriterium.OPERATOR_IN, false);
		} else if (key.equals(ReklamationFac.REKLAMATIONART_LIEFERANT)) {
			filtersI[0] = new FilterKriterium("" + LagerFac.FLR_LAGERBEWEGUNG_C_BELEGARTNR, true,
					"('" + LocaleFac.BELEGART_BESTELLUNG + "')", FilterKriterium.OPERATOR_IN, false);
		}

		filtersI[1] = new FilterKriterium("" + LagerFac.FLR_LAGERBEWEGUNG_N_MENGE, true, "0",
				FilterKriterium.OPERATOR_GT, false);

		panelQueryFLRSnrChnrAuswahl = new PanelQueryFLR(null, filtersI, QueryParameters.UC_ID_SNRCHNRFUERREKLAMATION,
				aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("rekla.snchrnauswahl"));

		FilterKriteriumDirekt fkdSeriennummer = ArtikelFilterFactory.getInstance().createFKDSnrChnrReklamation();

		if (wtfSnrChnr.getText() != null) {
			fkdSeriennummer.value = wtfSnrChnr.getText();
		}

		FilterKriteriumDirekt fkdArtikelnummer = ArtikelFilterFactory.getInstance()
				.createFKDArtikelnummerSnrChnrReklamation(getInternalFrame());
		if (wifArtikel.getArtikelDto() != null) {
			fkdArtikelnummer.value = wifArtikel.getArtikelDto().getCNr();
		}

		panelQueryFLRSnrChnrAuswahl.befuellePanelFilterkriterienDirekt(fkdArtikelnummer, fkdSeriennummer);

		if (reklamationDto.getPersonalIIdVerursacher() != null) {
			panelQueryFLRSnrChnrAuswahl.setSelectedId(reklamationDto.getPersonalIIdVerursacher());
		}

		if ((fkdSeriennummer.value != null && !fkdSeriennummer.value.trim().equals(""))
				|| fkdArtikelnummer.value != null && !fkdArtikelnummer.value.trim().equals("")) {
			panelQueryFLRSnrChnrAuswahl.eventActionRefresh(null, false);
		}

		panelQueryFLRSnrChnrAuswahl.setSize(800, panelQueryFLRSnrChnrAuswahl.getHeight());

		new DialogQuery(panelQueryFLRSnrChnrAuswahl);
	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRLos = FertigungFilterFactory.getInstance().createPanelFLRLose(getInternalFrame(),
				reklamationDto.getArtikelIId(), !wtfLos.isMandatoryField());
		panelQueryFLRLos.setSelectedId(reklamationDto.getLosIId());

		new DialogQuery(panelQueryFLRLos);

	}

	void dialogQueryLossollarbeitsplanFromListe(Integer selectedLosIId) throws Throwable {
		if (reklamationDto.getLosIId() != null) {
			panelQueryFLRArbeitsgang = FertigungFilterFactory.getInstance().createPanelFLRLossollarbeitsplan(
					getInternalFrame(), selectedLosIId, reklamationDto.getLossollarbeitsplanIId());
			new DialogQuery(panelQueryFLRArbeitsgang);
		}

	}

	void dialogQueryKostenstelleFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance().createPanelFLRKostenstelle(getInternalFrame(),
				false, false, reklamationDto.getKostenstelleIId());
		new DialogQuery(panelQueryFLRKostenstelle);
	}

	private void dialogQueryRechnung() throws Throwable {
		panelQueryFLRRechnung = RechnungFilterFactory.getInstance().createPanelFLRRechnungenEinesKunden(
				getInternalFrame(), reklamationDto.getKundeIId(), reklamationDto.getRechnungIId());

		new DialogQuery(panelQueryFLRRechnung);
	}

	void dialogQueryFehlerangabeFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		panelQueryFLRFehlerangabe = new PanelQueryFLR(null, null, QueryParameters.UC_ID_FEHLERANGABE, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr("rekla.fehlerangabe"));
		panelQueryFLRFehlerangabe.befuellePanelFilterkriterienDirekt(
				ReklamationFilterFactory.getInstance().createFKDBezeichnungMitAlias("fehlerangabe"), null);
		panelQueryFLRFehlerangabe.setSelectedId(reklamationDto.getFehlerangabeIId());

		new DialogQuery(panelQueryFLRFehlerangabe);
	}

	public void setDefaultsAusProjekt(Integer projektIId) throws Throwable {
		ProjektDto projektDto = DelegateFactory.getInstance().getProjektDelegate().projektFindByPrimaryKey(projektIId);

		wtfProjekt.setText(projektDto.getCTitel());
		wsfProjekt.setKey(projektDto.getIId());

		KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByiIdPartnercNrMandantOhneExc(projektDto.getPartnerIId(), LPMain.getTheClient().getMandant());
		if (kundeDto == null) {

			wcoReklamationart.setKeyOfSelectedItem(ReklamationFac.REKLAMATIONART_LIEFERANT);
			return;
		} else {

			wcoReklamationart.setKeyOfSelectedItem(ReklamationFac.REKLAMATIONART_KUNDE);

			kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(kundeDto.getIId());

			DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(kundeDto.getIId(), null, getInternalFrame());

			wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());

			wbuKunde.setOKey(kundeDto.getIId());

			reklamationDto.setKundeIId(kundeDto.getIId());

			if (projektDto.getAnsprechpartnerIId() != null) {
				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(projektDto.getAnsprechpartnerIId());

				reklamationDto.setAnsprechpartnerIId(ansprechpartnerDto.getIId());

				wtfAnsprechpartner.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
			}
		}

	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		getInternalFrameReklamation().getTabbedPaneReklamation().printReklamation();
	}

	private void dialogQueryAnsprechpartner(ActionEvent e) throws Throwable {
		// String key = (String) wcoReklamationart.getKeyOfSelectedItem();

		if (reklamationDto.getKundeIId() == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
		} else {
			KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(reklamationDto.getKundeIId());
			panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance().createPanelFLRAnsprechpartner(
					getInternalFrame(), kundeDto.getPartnerIId(), reklamationDto.getAnsprechpartnerIId(), true, true);
			new DialogQuery(panelQueryFLRAnsprechpartner);
		}

	}

	private void dialogQueryAnsprechpartnerLieferant(ActionEvent e) throws Throwable {
		// String key = (String) wcoReklamationart.getKeyOfSelectedItem();

		if (reklamationDto.getLieferantIId() == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.lieferantnichtgewaehlt"));
		} else {
			LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
					.lieferantFindByPrimaryKey(reklamationDto.getLieferantIId());
			panelQueryFLRAnsprechpartner_Lieferant = PartnerFilterFactory.getInstance().createPanelFLRAnsprechpartner(
					getInternalFrame(), lfDto.getPartnerIId(), reklamationDto.getAnsprechpartnerIId(), true, true);
			new DialogQuery(panelQueryFLRAnsprechpartner_Lieferant);
		}

	}

	void dialogQueryAufnahmeartFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		panelQueryFLRAufnahmeart = new PanelQueryFLR(null, null, QueryParameters.UC_ID_AUFNAHMEART, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr("rekla.aufnahmeart"));
		panelQueryFLRAufnahmeart.befuellePanelFilterkriterienDirekt(
				ReklamationFilterFactory.getInstance().createFKDBezeichnungMitAlias("aufnahmeart"), null);
		panelQueryFLRAufnahmeart.setSelectedId(reklamationDto.getAufnahmeartIId());

		new DialogQuery(panelQueryFLRAufnahmeart);
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		wrbArtikel.setSelected(true);
		wrbArtikel.setText(LPMain.getTextRespectUISPr("auftrag.ident"));

		buttonKundeUnterart.add(wrbKundeunterartFertigung);
		buttonKundeUnterart.add(wrbKundeunterartLieferant);

		wrbKundeunterartLieferant.setText(LPMain.getTextRespectUISPr("label.lieferant"));
		wrbKundeunterartFertigung.setText(LPMain.getTextRespectUISPr("fert.modulname"));

		wrbKundeunterartFertigung.addActionListener(this);
		wrbKundeunterartLieferant.addActionListener(this);
		wrbKundeunterartFertigung.setSelected(true);

		wtfTelAnsp.setActivatable(false);
		wtfTelAnspLieferant.setActivatable(false);
		wtfTelAnsp.setEditable(false);
		wtfTelAnspLieferant.setEditable(false);

		wrbArtikel.addActionListener(this);
		wrbHandeingabe.addActionListener(this);

		wbuSnrChnr.setText(LPMain.getTextRespectUISPr("rekla.snchrnauswahl"));

		wrbHandeingabe.setText(LPMain.getTextRespectUISPr("lp.handeingabe"));
		wcbFremdprodukt.setText(LPMain.getTextRespectUISPr("rekla.fremdprodukt"));

		wlaGrundKommentar.setText(LPMain.getTextRespectUISPr("rekla.grundkommentar"));

		wifArtikel = new WrapperIdentField(getInternalFrame(), this);
		wifArtikel.setBMitLeerenButton(true);

		wlaReklamationart.setText(LPMain.getTextRespectUISPr("rekla.reklamationart"));

		wlaKdReklaNr.setText(LPMain.getTextRespectUISPr("rekla.kdreklanr"));
		wlaKdLsNr.setText(LPMain.getTextRespectUISPr("rekla.kdlsnr"));
		wlaDatum.setText(LPMain.getTextRespectUISPr("label.belegdatum"));
		wlaWareErhalten.setText(LPMain.getTextRespectUISPr("rekla.wareerhalten"));
		wcoReklamationart.setMandatoryFieldDB(true);
		wcoReklamationart.setMandatoryField(true);
		wtfKostenstelle.setActivatable(false);
		wtfKostenstelle.setText("");
		wtfKostenstelle.setColumnsMax(80);
		wtfKostenstelle.setMandatoryField(true);

		wbuMaschine.setText(LPMain.getTextRespectUISPr("lp.maschine") + "...");
		wbuMaschine.setActionCommand(ACTION_SPECIAL_MASCHINE_FROM_LISTE);
		wbuMaschine.addActionListener(this);
		wtfMaschine.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wtfMaschine.setActivatable(false);

		wtfTelAnsp.setActivatable(false);
		wtfTelAnspLieferant.setActivatable(false);

		wlaBeurteilung.setHorizontalAlignment(SwingConstants.LEFT);

		wtfGrund.setColumnsMax(80);
		wtfHandartikel.setColumnsMax(80);
		wtfProjekt.setColumnsMax(80);

		wbuKunde.setText(LPMain.getTextRespectUISPr("button.kunde"));
		wbuKunde.setActionCommand(ACTION_SPECIAL_KUNDE_FROM_LISTE);
		wbuKunde.addActionListener(this);

		wdfDatum.setMandatoryField(true);
		wnfMenge.setMandatoryField(true);

		wtfKunde.setMandatoryField(true);

		if (bBestellungWEManuellEingeben == false) {
			wtfBestellung.setActivatable(false);
			wtfWareneingang.setActivatable(false);
		}

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_BESTELLUNG_UND_WARENEINGANG_SIND_PFLICHTFELDER,
						ParameterFac.KATEGORIE_REKLAMATION, LPMain.getTheClient().getMandant());
		boolean b = (Boolean) parameter.getCWertAsObject();

		if (b == true) {
			wtfBestellung.setMandatoryField(true);
			wtfWareneingang.setMandatoryField(true);
		}

		wbuLieferant.setText(LPMain.getTextRespectUISPr("button.lieferant"));
		wbuLieferant.setActionCommand(ACTION_SPECIAL_LIEFERANT_FROM_LISTE);
		wbuLieferant.addActionListener(this);
		wtfLieferant.setMandatoryField(true);
		wtfLieferant.setActivatable(false);
		wbuBestellung.setText(LPMain.getTextRespectUISPr("button.bestellung"));
		wbuBestellung.setActionCommand(ACTION_SPECIAL_BESTELLUNG_FROM_LISTE);
		wbuBestellung.addActionListener(this);

		wbuWareneingang.setText(LPMain.getTextRespectUISPr("menueentry.wareneingang") + "...");
		wbuWareneingang.setActionCommand(ACTION_SPECIAL_WARENEINGANG_FROM_LISTE);
		wbuWareneingang.addActionListener(this);

		wbuRechnung.setText(LPMain.getTextRespectUISPr("button.rechnung"));
		wbuRechnung.setActionCommand(ACTION_SPECIAL_RECHNUNG_FROM_LISTE);
		wbuRechnung.addActionListener(this);
		wbuLieferschein.setText(LPMain.getTextRespectUISPr("button.lieferschein"));
		wbuLieferschein.setActionCommand(ACTION_SPECIAL_LIEFERSCHEIN_FROM_LISTE);
		wbuLieferschein.addActionListener(this);
		wbuAnsprechpartner.setText(LPMain.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartner.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE);
		wbuAnsprechpartner.addActionListener(this);

		wbuAnsprechpartnerLieferant.setText(LPMain.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartnerLieferant.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT_FROM_LISTE);
		wbuAnsprechpartnerLieferant.addActionListener(this);

		wbuLos.setText(LPMain.getTextRespectUISPr("fert.tab.unten.los.title") + "...");
		wbuLos.setActionCommand(ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLos.addActionListener(this);
		wtfLos.setMandatoryField(true);
		wtfLos.setActivatable(false);
		wbuPersonal.setActionCommand(ACTION_SPECIAL_PERSONAL_FROM_LISTE);
		wbuPersonal.addActionListener(this);

		wbuPersonal.setText(LPMain.getTextRespectUISPr("rekla.aufnehmer"));
		wtfPersonal.setActivatable(false);
		wtfPersonal.setMandatoryField(true);

		wbuVerursacher.setActionCommand(ACTION_SPECIAL_VERURSACHER_FROM_LISTE);
		wbuVerursacher.addActionListener(this);

		wbuVerursacher.setText(LPMain.getTextRespectUISPr("rekla.verursacher"));
		wtfVerursacher.setActivatable(false);

		wtfLieferschein.setActivatable(false);
		wtfRechnung.setActivatable(false);
		wtfKunde.setActivatable(false);
		wtfAnsprechpartner.setActivatable(false);
		wtfAnsprechpartnerLieferant.setActivatable(false);

		wbuArbeitsgang.setActionCommand(ACTION_SPECIAL_LOSSOLLARBEITSPLAN_FROM_LISTE);
		wbuArbeitsgang.addActionListener(this);

		wbuArbeitsgang.setText(LPMain.getTextRespectUISPr("stkl.arbeitsplan.arbeitsgang"));
		wtfArbeitsgang.setActivatable(false);

		wbuAufnahmeart.setText(LPMain.getTextRespectUISPr("rekla.aufnahmeart") + "...");
		wbuAufnahmeart.setActionCommand(ACTION_SPECIAL_AUFNAHMEART_FROM_LISTE);
		wbuAufnahmeart.addActionListener(this);

		wbuFehlerangabe.setText(LPMain.getTextRespectUISPr("rekla.fehlerangabe") + "...");
		wbuFehlerangabe.setActionCommand(ACTION_SPECIAL_FEHLERANGABE_FROM_LISTE);
		wbuFehlerangabe.addActionListener(this);

		wbuSnrChnr.setActionCommand(ACTION_SPECIAL_SNRCHNR_FROM_LISTE);
		wbuSnrChnr.addActionListener(this);

		wcoReklamationart.addActionListener(this);

		wtfAufnahmeart.setActivatable(false);
		wtfFehlerangabe.setActivatable(false);
		wtfAufnahmeart.setMandatoryField(true);
		wtfFehlerangabe.setMandatoryField(true);

		wlaMaschinengruppe.setHorizontalAlignment(SwingConstants.LEFT);
		wlaKundeLos.setHorizontalAlignment(SwingConstants.LEFT);
		wbuKostenstelle.setActionCommand(PanelReklamation.ACTION_SPECIAL_KOSTENSTELLE_ABTEILUNG_FROM_LISTE);
		wbuKostenstelle.setText(LPMain.getTextRespectUISPr("button.kostenstelle"));
		wbuKostenstelle.addActionListener(this);

		wlaMenge.setText(LPMain.getTextRespectUISPr("lp.menge"));
		wlaGrund.setText(LPMain.getTextRespectUISPr("rekla.grund"));
		wlaProjekt.setText(LPMain.getTextRespectUISPr("label.projekt"));
		wlaTelAnsp.setText(LPMain.getTextRespectUISPr("rekla.telansprechpartner"));
		wlaTelAnspLieferant.setText(LPMain.getTextRespectUISPr("rekla.telansprechpartner"));

		wtfSnrChnr.addFocusListener(new SnrFocusListener());

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		buttonGroup1.add(wrbHandeingabe);
		buttonGroup1.add(wrbArtikel);

		jpaWorkingOn.add(wlaReklamationart, new GridBagConstraints(0, 0, 1, 1, 0.25, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wcoReklamationart, new GridBagConstraints(1, 0, 1, 1, 0.6, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		jpaWorkingOn.add(wlaDatum, new GridBagConstraints(2, 0, 1, 1, 0.4, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wdfDatum, new GridBagConstraints(3, 0, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWareErhalten, new GridBagConstraints(4, 0, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 60, 0));

		jpaWorkingOn.add(wdfWareErhalten, new GridBagConstraints(5, 0, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -20, 0));

		iZeile = 10;

		jpaWorkingOn.add(wbuPersonal, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wtfPersonal, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		jpaWorkingOn.add(wbuKostenstelle, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		jpaWorkingOn.add(wtfKostenstelle, new GridBagConstraints(3, iZeile, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuAufnahmeart, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wtfAufnahmeart, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		jpaWorkingOn.add(wbuFehlerangabe, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		jpaWorkingOn.add(wtfFehlerangabe, new GridBagConstraints(3, iZeile, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		iZeile = 14;

		jpaWorkingOn.add(wrbArtikel, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// jpaWorkingOn.add(wifArtikel.getWbuArtikel(), new
		// GridBagConstraints(1,
		// iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
		// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		// jpaWorkingOn.repaint();
		// jpaWorkingOn.add(wifArtikel.getWtfIdent(), new GridBagConstraints(2,
		// iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
		// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		// jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(),
		// new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
		// GridBagConstraints.CENTER,
		// GridBagConstraints.HORIZONTAL, new Insets(2, 0, 2, 2),
		// 0, 0));

		iZeile = 15;

		jpaWorkingOn.add(wrbHandeingabe, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		// jpaWorkingOn.add(wtfHandartikel, new GridBagConstraints(1, iZeile, 3,
		// 1, 0.0, 0.0, GridBagConstraints.CENTER,
		// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		iZeile++;

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_SERIENNUMMERN)
				|| LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_CHARGENNUMMERN)) {
			jpaWorkingOn.add(wbuSnrChnr, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
			jpaWorkingOn.add(wtfSnrChnr, new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

			iZeile++;

		}

		jpaWorkingOn.add(wlaMenge, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wnfMenge, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		jpaWorkingOn.add(wcbFremdprodukt, new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaGrund, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wtfGrund, new GridBagConstraints(1, iZeile, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaGrundKommentar, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wefGrundKommentar, new GridBagConstraints(1, iZeile, 5, 1, 0.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 0, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaProjekt, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wtfProjekt, new GridBagConstraints(1, iZeile, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {
			iZeile++;
			jpaWorkingOn.add(wsfProjekt.getWrapperGotoButton(), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
			jpaWorkingOn.add(wsfProjekt.getWrapperTextField(), new GridBagConstraints(1, iZeile, 5, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		}

		iZeile++;
		jpaWorkingOn.add(wlaBeurteilung, new GridBagConstraints(1, iZeile, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DISCARD, PanelBasis.ACTION_PRINT };

		enableToolsPanelButtons(aWhichButtonIUse);
	}

	public void reklamationAusWEPVorbesetzten(Integer bestellpositionIId, Integer wareneingangIId)
			throws ExceptionLP, Throwable {

		wcoReklamationart.setKeyOfSelectedItem(ReklamationFac.REKLAMATIONART_LIEFERANT);

		WareneingangDto weDto = DelegateFactory.getInstance().getWareneingangDelegate()
				.wareneingangFindByPrimaryKey(wareneingangIId);

		BestellungDto bestellungDto = DelegateFactory.getInstance().getBestellungDelegate()
				.bestellungFindByPrimaryKey(weDto.getBestellungIId());

		wtfBestellung.setText(bestellungDto.getCNr());
		reklamationDto.setBestellungIId(bestellungDto.getIId());

		WareneingangDto wareneingangDto = DelegateFactory.getInstance().getWareneingangDelegate()
				.wareneingangFindByPrimaryKey(wareneingangIId);
		wtfWareneingang.setText(wareneingangDto.getCLieferscheinnr());
		reklamationDto.setWareneingangIId(wareneingangDto.getIId());

		LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
				.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse());
		wtfLieferant.setText(lieferantDto.getPartnerDto().formatFixTitelName1Name2());
		reklamationDto.setLieferantIId(bestellungDto.getLieferantIIdBestelladresse());

		BestellpositionDto bestellpositionDto = DelegateFactory.getInstance().getBestellungDelegate()
				.bestellpositionFindByPrimaryKey(bestellpositionIId);
		if (bestellpositionDto.getArtikelIId() != null) {

			wifArtikel.setArtikelDto(DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(bestellpositionDto.getArtikelIId()));
		}

		if (bestellungDto.getAnsprechpartnerIId() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(bestellungDto.getAnsprechpartnerIId());

			wtfAnsprechpartnerLieferant.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());

			reklamationDto.setAnsprechpartnerIIdLieferant(ansprechpartnerDto.getIId());
		}

		// WEP suchen
		WareneingangspositionDto[] aWEPOSDto = DelegateFactory.getInstance().getWareneingangDelegate()
				.wareneingangspositionFindByBestellpositionIId(bestellpositionDto.getIId());

		if (aWEPOSDto.length > 0) {
			Integer iIdWE = weDto.getIId();

			for (int i = 0; i < aWEPOSDto.length; i++) {

				if (aWEPOSDto[i].getWareneingangIId().equals(wareneingangDto.getIId())) {

					wnfMenge.setBigDecimal(aWEPOSDto[i].getNGeliefertemenge());

				}

			}
		}

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		reklamationDto = new ReklamationDto();
		reklamationDto.setBBerechtigt(Helper.boolean2Short(false));
		reklamationDto.setBBetrifftgelieferte(Helper.boolean2Short(false));
		reklamationDto.setBBetrifftlagerstand(Helper.boolean2Short(false));
		getInternalFrameReklamation().setReklamationDto(reklamationDto);

		prepareNewDetailsPanel();
		refreshDetailsGrid();
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		boolean b = internalFrameReklamation.getTabbedPaneReklamation().pruefeObReklamationAenderbar();
		if (b == true) {
			super.eventActionUpdate(aE, false); // Buttons schalten
		} else {
			return;
		}
	}

	protected void changeToArtikelFieldsInGrid() {
		jpaWorkingOn.remove(wtfHandartikel);

		// jpaWorkingOn.add(wrbArtikel,
		// new GridBagConstraints(0, 14, 1, 1, 0.0, 0.0,
		// GridBagConstraints.CENTER,
		// GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
		// 0), 0, 0));

		jpaWorkingOn.add(wifArtikel.getWbuArtikel(), new GridBagConstraints(1, 14, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		jpaWorkingOn.add(wifArtikel.getWtfIdent(), new GridBagConstraints(2, 14, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
		jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(), new GridBagConstraints(3, 14, 3, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
	}

	protected void changeToHandartikelInGrid() {
		jpaWorkingOn.remove(wifArtikel.getWbuArtikel());
		jpaWorkingOn.remove(wifArtikel.getWtfIdent());
		jpaWorkingOn.remove(wifArtikel.getWtfBezeichnung());

		jpaWorkingOn.add(wtfHandartikel, new GridBagConstraints(1, 15, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource().equals(wrbArtikel)) {
			wtfHandartikel.setActivatable(false);
			wtfHandartikel.setEditable(false);

			wifArtikel.getWbuArtikel().setEnabled(true);
			changeToArtikelFieldsInGrid();
			revalidate();

			// jpaWorkingOn.remove(wtfHandartikel);
			// jpaWorkingOn.add(wrbArtikel,
			// new GridBagConstraints(0, 14, 1, 1, 0.0, 0.0,
			// GridBagConstraints.CENTER,
			// GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
			// 0), 0, 0));
			//
			// jpaWorkingOn.add(wifArtikel.getWbuArtikel(),
			// new GridBagConstraints(1, 14, 1, 1, 0, 0.0,
			// GridBagConstraints.CENTER,
			// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0,
			// 2), 0, 0));
			// jpaWorkingOn.repaint();
			// jpaWorkingOn.add(wifArtikel.getWtfIdent(),
			// new GridBagConstraints(2, 14, 1, 1, 0, 0.0,
			// GridBagConstraints.CENTER,
			// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0,
			// 2), 0, 0));
			// jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(),
			// new GridBagConstraints(3, 14, 1, 1, 0, 0.0,
			// GridBagConstraints.CENTER,
			// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0,
			// 2), 0, 0));
			// jpaWorkingOn.repaint();
		} else if (e.getSource().equals(wrbHandeingabe)) {
			wtfHandartikel.setActivatable(true);
			wtfHandartikel.setEditable(true);

			changeToHandartikelInGrid();
			revalidate();

			// jpaWorkingOn.remove(wifArtikel.getWbuArtikel());
			// jpaWorkingOn.remove(wifArtikel.getWtfIdent());
			// jpaWorkingOn.remove(wifArtikel.getWtfBezeichnung());
			//
			// jpaWorkingOn.add(wrbHandeingabe,
			// new GridBagConstraints(0, 15, 1, 1, 0.0, 0.0,
			// GridBagConstraints.CENTER,
			// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0,
			// 2), 0, 0));
			// jpaWorkingOn.add(wtfHandartikel,
			// new GridBagConstraints(1, 15, 3, 1, 0.0, 0.0,
			// GridBagConstraints.CENTER,
			// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0,
			// 2), 0, 0));
			// jpaWorkingOn.repaint();
		} else if (e.getSource().equals(wcoReklamationart) || e.getSource().equals(wrbKundeunterartFertigung)
				|| e.getSource().equals(wrbKundeunterartLieferant)) {

			refreshDetailsGrid();
		} else {

			if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE_ABTEILUNG_FROM_LISTE)) {
				dialogQueryKostenstelleFromListe(e);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_PERSONAL_FROM_LISTE)) {
				dialogQueryPersonalFromListe(e);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_VERURSACHER_FROM_LISTE)) {
				dialogQueryVerursacherFromListe(reklamationDto.getLossollarbeitsplanIId());
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_AUFNAHMEART_FROM_LISTE)) {
				dialogQueryAufnahmeartFromListe(e);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_FEHLERANGABE_FROM_LISTE)) {
				dialogQueryFehlerangabeFromListe(e);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_FROM_LISTE)) {
				dialogQueryKunde(e);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER_FROM_LISTE)) {
				dialogQueryAnsprechpartner(e);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT_FROM_LISTE)) {
				dialogQueryAnsprechpartnerLieferant(e);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERSCHEIN_FROM_LISTE)) {
				dialogQueryLieferschein(e);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERANT_FROM_LISTE)) {
				dialogQueryLieferant();
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_LOSSOLLARBEITSPLAN_FROM_LISTE)) {
				dialogQueryLossollarbeitsplanFromListe(reklamationDto.getLosIId());
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_LOS_FROM_LISTE)) {
				dialogQueryLosFromListe(null);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_RECHNUNG_FROM_LISTE)) {
				if (reklamationDto.getKundeIId() != null) {
					dialogQueryRechnung();
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));

				}

			} else if (e.getActionCommand().equals(ACTION_SPECIAL_BESTELLUNG_FROM_LISTE)) {
				if (reklamationDto.getLieferantIId() != null) {
					dialogQueryBestellung();
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.error.lieferantnichtgewaehlt"));

				}
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_WARENEINGANG_FROM_LISTE)) {
				if (reklamationDto.getBestellungIId() != null) {
					dialogQueryWareneingang();
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.error.bestellungnichtgewaehlt"));

				}
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_MASCHINE_FROM_LISTE)) {
				dialogQueryMaschineFromListe();
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_SNRCHNR_FROM_LISTE)) {
				dialogQuerySnrChnrFromListe();
				setMengeFeldFromSeriennummer();
			}
		}
	}

	protected void refreshItemFields() {
		if (wrbArtikel.isSelected()) {
			wtfHandartikel.setActivatable(false);
			wtfHandartikel.setEditable(false);

			changeToArtikelFieldsInGrid();
		} else {
			wtfHandartikel.setActivatable(true);
			// wtfHandartikel.setEditable(true) ;
			changeToHandartikelInGrid();
		}
	}

	protected void beEnabled(boolean editable) {
		wbuLos.setEnabled(editable);

		wbuLieferant.setEnabled(editable);
		wbuBestellung.setEnabled(editable);
		wbuWareneingang.setEnabled(editable);

		wbuKunde.setEnabled(editable);
		wbuLieferschein.setEnabled(editable);
		wbuRechnung.setEnabled(editable);
		wbuMaschine.setEnabled(editable);
		wbuArbeitsgang.setEnabled(editable);
		wbuVerursacher.setEnabled(editable);
		wrbKundeunterartFertigung.setEnabled(editable);
		wrbKundeunterartLieferant.setEnabled(editable);
		wbuAnsprechpartner.setEnabled(editable);
		wbuAnsprechpartnerLieferant.setEnabled(editable);
	}

	protected void refreshDetailsGrid() throws Throwable {
		// Zuerst alles entfernen
		jpaWorkingOn.remove(wbuLos);
		jpaWorkingOn.remove(wtfLos);
		jpaWorkingOn.remove(wbuKunde);
		jpaWorkingOn.remove(wtfKunde);
		jpaWorkingOn.remove(wbuLieferant);
		jpaWorkingOn.remove(wtfLieferant);
		jpaWorkingOn.remove(wbuBestellung);
		jpaWorkingOn.remove(wtfBestellung);
		jpaWorkingOn.remove(wtfWareneingang);
		jpaWorkingOn.remove(wbuWareneingang);
		jpaWorkingOn.remove(wbuRechnung);
		jpaWorkingOn.remove(wtfRechnung);
		jpaWorkingOn.remove(wbuLieferschein);
		jpaWorkingOn.remove(wtfLieferschein);
		jpaWorkingOn.remove(wbuAnsprechpartnerLieferant);
		jpaWorkingOn.remove(wtfAnsprechpartnerLieferant);
		jpaWorkingOn.remove(wbuAnsprechpartner);
		jpaWorkingOn.remove(wtfAnsprechpartner);
		jpaWorkingOn.remove(wtfTelAnsp);
		jpaWorkingOn.remove(wlaTelAnsp);
		jpaWorkingOn.remove(wtfTelAnspLieferant);
		jpaWorkingOn.remove(wlaTelAnspLieferant);
		jpaWorkingOn.remove(wtfKdLsNr);
		jpaWorkingOn.remove(wlaKdLsNr);
		jpaWorkingOn.remove(wtfKdReklaNr);
		jpaWorkingOn.remove(wlaKdReklaNr);
		jpaWorkingOn.remove(wbuMaschine);
		jpaWorkingOn.remove(wtfMaschine);
		jpaWorkingOn.remove(wbuVerursacher);
		jpaWorkingOn.remove(wtfVerursacher);
		jpaWorkingOn.remove(wbuArbeitsgang);
		jpaWorkingOn.remove(wtfArbeitsgang);
		jpaWorkingOn.remove(wlaKundeLos);
		jpaWorkingOn.remove(wlaMaschinengruppe);
		jpaWorkingOn.remove(wrbKundeunterartFertigung);
		jpaWorkingOn.remove(wrbKundeunterartLieferant);
		jpaWorkingOn.remove(wtfHandartikel);

		revalidate();
		repaint();

		wlaKdReklaNr.setText(LPMain.getTextRespectUISPr("rekla.kdreklanr"));
		wlaKdLsNr.setText(LPMain.getTextRespectUISPr("rekla.kdlsnr"));

		if (getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME
				|| getLockedstateDetailMainKey().getIState() == LOCK_FOR_NEW) {
			beEnabled(true);
		} else {
			beEnabled(false);
		}

		refreshItemFields();

		String key = (String) wcoReklamationart.getKeyOfSelectedItem();
		if (key.equals(ReklamationFac.REKLAMATIONART_FERTIGUNG)) {
			if (reklamationDto != null) {
				reklamationDto.setAnsprechpartnerIId(null);

			}
			wtfLos.setMandatoryField(true);
			jpaWorkingOn.add(wbuLos, new GridBagConstraints(0, 1, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfLos, new GridBagConstraints(1, 1, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
			jpaWorkingOn.add(wlaKundeLos, new GridBagConstraints(2, 1, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

			jpaWorkingOn.add(wbuArbeitsgang, new GridBagConstraints(0, 2, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfArbeitsgang, new GridBagConstraints(1, 2, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
			jpaWorkingOn.add(wbuMaschine, new GridBagConstraints(0, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

			jpaWorkingOn.add(wtfMaschine, new GridBagConstraints(1, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
			jpaWorkingOn.add(wlaMaschinengruppe, new GridBagConstraints(2, 3, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
			jpaWorkingOn.add(wbuVerursacher, new GridBagConstraints(0, 4, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfVerursacher, new GridBagConstraints(1, 4, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		} else if (key.equals(ReklamationFac.REKLAMATIONART_LIEFERANT)) {
			if (reklamationDto != null) {
				reklamationDto.setLosIId(null);
				reklamationDto.setKundeIId(null);
				reklamationDto.setRechnungIId(null);
				reklamationDto.setLieferscheinIId(null);
				reklamationDto.setPersonalIIdVerursacher(null);
			}

			jpaWorkingOn.add(wbuLieferant, new GridBagConstraints(0, 1, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfLieferant, new GridBagConstraints(1, 1, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

			jpaWorkingOn.add(wbuAnsprechpartnerLieferant, new GridBagConstraints(2, 1, 1, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfAnsprechpartnerLieferant, new GridBagConstraints(3, 1, 3, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

			wlaKdReklaNr.setText(LPMain.getTextRespectUISPr("rekla.lfreklanr"));
			wlaKdLsNr.setText(LPMain.getTextRespectUISPr("rekla.lflsnr"));

			jpaWorkingOn.add(wlaKdReklaNr, new GridBagConstraints(2, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfKdReklaNr, new GridBagConstraints(3, 3, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

			jpaWorkingOn.add(wlaKdLsNr, new GridBagConstraints(2, 4, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfKdLsNr, new GridBagConstraints(3, 4, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

			jpaWorkingOn.add(wbuBestellung, new GridBagConstraints(0, 2, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

			jpaWorkingOn.add(wtfBestellung, new GridBagConstraints(1, 2, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

			jpaWorkingOn.add(wlaTelAnspLieferant, new GridBagConstraints(2, 2, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfTelAnspLieferant, new GridBagConstraints(3, 2, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

			jpaWorkingOn.add(wbuWareneingang, new GridBagConstraints(0, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfWareneingang, new GridBagConstraints(1, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

		} else if (key.equals(ReklamationFac.REKLAMATIONART_KUNDE)) {

			wtfLos.setMandatoryField(false);
			jpaWorkingOn.add(wbuKunde, new GridBagConstraints(0, 1, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, 1, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

			jpaWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(2, 1, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(3, 1, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

			jpaWorkingOn.add(wlaTelAnsp, new GridBagConstraints(2, 2, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfTelAnsp, new GridBagConstraints(3, 2, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

			jpaWorkingOn.add(wbuLieferschein, new GridBagConstraints(0, 2, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfLieferschein, new GridBagConstraints(1, 2, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

			jpaWorkingOn.add(wbuRechnung, new GridBagConstraints(0, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfRechnung, new GridBagConstraints(1, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
			jpaWorkingOn.add(wlaKdReklaNr, new GridBagConstraints(2, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfKdReklaNr, new GridBagConstraints(3, 3, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

			jpaWorkingOn.add(wrbKundeunterartFertigung, new GridBagConstraints(0, 4, 1, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wrbKundeunterartLieferant, new GridBagConstraints(1, 4, 1, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

			jpaWorkingOn.add(wlaKdLsNr, new GridBagConstraints(2, 4, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jpaWorkingOn.add(wtfKdLsNr, new GridBagConstraints(3, 4, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

			if (wrbKundeunterartFertigung.isSelected()) {

				jpaWorkingOn.add(wbuLos, new GridBagConstraints(0, 5, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jpaWorkingOn.add(wtfLos, new GridBagConstraints(1, 5, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

				jpaWorkingOn.add(wlaKundeLos, new GridBagConstraints(2, 5, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

				jpaWorkingOn.add(wbuArbeitsgang, new GridBagConstraints(0, 6, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jpaWorkingOn.add(wtfArbeitsgang, new GridBagConstraints(1, 6, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
				jpaWorkingOn.add(wbuMaschine, new GridBagConstraints(0, 7, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jpaWorkingOn.add(wtfMaschine, new GridBagConstraints(1, 7, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
				jpaWorkingOn.add(wlaMaschinengruppe, new GridBagConstraints(2, 7, 2, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
				jpaWorkingOn.add(wbuVerursacher, new GridBagConstraints(0, 8, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jpaWorkingOn.add(wtfVerursacher, new GridBagConstraints(1, 8, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
			} else {
				jpaWorkingOn.add(wbuLieferant, new GridBagConstraints(0, 5, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jpaWorkingOn.add(wtfLieferant, new GridBagConstraints(1, 5, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

				jpaWorkingOn.add(wbuAnsprechpartnerLieferant, new GridBagConstraints(2, 5, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jpaWorkingOn.add(wtfAnsprechpartnerLieferant, new GridBagConstraints(3, 5, 3, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

				jpaWorkingOn.add(wbuBestellung, new GridBagConstraints(0, 6, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

				jpaWorkingOn.add(wtfBestellung, new GridBagConstraints(1, 6, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

				jpaWorkingOn.add(wlaTelAnspLieferant, new GridBagConstraints(2, 6, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jpaWorkingOn.add(wtfTelAnspLieferant, new GridBagConstraints(3, 6, 3, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

				jpaWorkingOn.add(wbuWareneingang, new GridBagConstraints(0, 7, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jpaWorkingOn.add(wtfWareneingang, new GridBagConstraints(1, 7, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));
				jpaWorkingOn.add(new WrapperLabel(""), new GridBagConstraints(0, 8, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			}
		}

		revalidate();
	}

	private void dialogQueryBestellung() throws Throwable {
		FilterKriterium[] fk = BestellungFilterFactory.getInstance()
				.getFKBestellungenEinesLieferanten(reklamationDto.getLieferantIId());

		panelQueryFLRBestellung = BestellungFilterFactory.getInstance().createPanelFLRBestellung(getInternalFrame(),
				false, false, fk, reklamationDto.getBestellungIId());
		new DialogQuery(panelQueryFLRBestellung);
	}

	private void dialogQueryWareneingang() throws Throwable {

		panelQueryFLRWareneingang = BestellungFilterFactory.getInstance().createPanelFLRWareneingang(getInternalFrame(),
				reklamationDto.getBestellungIId());
		new DialogQuery(panelQueryFLRWareneingang);
	}

	private void dialogQueryLieferant() throws Throwable {
		panelQueryFLRLieferant = PartnerFilterFactory.getInstance().createPanelFLRLieferant(getInternalFrame(),
				reklamationDto.getLieferantIId(), true, false);
		new DialogQuery(panelQueryFLRLieferant);
	}

	private void dialogQueryKunde(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance().createPanelFLRKunde(getInternalFrame(), true, true,
				reklamationDto.getKundeIId());

		new DialogQuery(panelQueryFLRKunde);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_REKLAMATION;
	}

	protected void setDefaults() throws Throwable {
		wcoReklamationart.setMap(DelegateFactory.getInstance().getReklamationDelegate().getAllReklamationart());

	}

	protected void components2Dto() throws Throwable {
		reklamationDto.setReklamationartCNr((String) wcoReklamationart.getKeyOfSelectedItem());

		if (wcoReklamationart.getKeyOfSelectedItem().equals(ReklamationFac.REKLAMATIONART_KUNDE)) {
			if (wrbKundeunterartFertigung.isSelected()) {
				reklamationDto.setIKundeunterart(ReklamationFac.REKLAMATION_KUNDEUNTERART_FERTIGUNG);
			} else {
				reklamationDto.setIKundeunterart(ReklamationFac.REKLAMATION_KUNDEUNTERART_LIEFERANT);
			}
			reklamationDto.setCKdlsnr(wtfKdLsNr.getText());
			reklamationDto.setCKdreklanr(wtfKdReklaNr.getText());
		} else {
			reklamationDto.setIKundeunterart(null);
			if (wcoReklamationart.getKeyOfSelectedItem().equals(ReklamationFac.REKLAMATIONART_LIEFERANT)) {
				reklamationDto.setCLflsnr(wtfKdLsNr.getText());
				reklamationDto.setCLfreklanr(wtfKdReklaNr.getText());
			}

		}

		if (bBestellungWEManuellEingeben == true) {
			reklamationDto.setCBestellnummer(wtfBestellung.getText());
			reklamationDto.setCWareneingang(wtfWareneingang.getText());

			reklamationDto.setBestellungIId(null);
			reklamationDto.setWareneingangIId(null);
		}

		reklamationDto.setProjektIId(wsfProjekt.getIKey());

		reklamationDto.setTBelegdatum(wdfDatum.getTimestamp());
		reklamationDto.setTWareErhalten(wdfWareErhalten.getTimestamp());
		reklamationDto.setBArtikel(wrbArtikel.getShort());
		if (wrbArtikel.isSelected()) {
			reklamationDto.setArtikelIId(wifArtikel.getArtikelDto().getIId());
		} else {
			reklamationDto.setCHandartikel(wtfHandartikel.getText());
		}
		reklamationDto.setBFremdprodukt(wcbFremdprodukt.getShort());
		reklamationDto.setNMenge(wnfMenge.getBigDecimal());
		reklamationDto.setCGrund(wtfGrund.getText());

		reklamationDto.setCProjekt(wtfProjekt.getText());

		reklamationDto.setXGrundLang(wefGrundKommentar.getText());

		if (wtfSnrChnr.getText() != null) {
			reklamationDto.setCSeriennrchargennr(wtfSnrChnr.getText().replaceAll(" ", ""));
		} else {
			reklamationDto.setCSeriennrchargennr(null);
		}

	}

	private boolean validateSeriennummer() throws ExceptionLP, Throwable {
		String snrs = wtfSnrChnr.getText();
		String[] seriennumern = Helper.erzeugeSeriennummernArray(snrs, BigDecimal.ZERO, false);
		ArtikelId artikelIId = new ArtikelId(wifArtikel.getArtikelDto().getIId());
		Set<String> existierende = DelegateFactory.getInstance().getLagerDelegate()
				.pruefeObSeriennummernExistieren(artikelIId, Arrays.asList(seriennumern));
		if (seriennumern.length != existierende.size()) {
			String notExisting = Arrays.stream(seriennumern).filter(s -> !existierende.contains(s)).reduce("",
					(s1, s2) -> s1 + (s1.length() == 0 ? "" : ",") + s2);
			boolean trotzdemSpeichern = DialogFactory.showModalJaNeinDialog(internalFrameReklamation,
					LPMain.getMessageTextRespectUISPr("rekla.seriennummer.notexist", notExisting));
			return trotzdemSpeichern;
		}
		return true;
	}

	private void setMengeFeldFromSeriennummer() throws ExceptionLP {
		String snrs = wtfSnrChnr.getText();
		String[] seriennumern = Helper.erzeugeSeriennummernArray(snrs, BigDecimal.ZERO, false);
		if (seriennumern != null) {
			wnfMenge.setBigDecimal(BigDecimal.valueOf(seriennumern.length));
		}
	}

	public static PartnerkommunikationDto erzeugePartnerkommunikationDto(
			PartnerkommunikationDto partnerkommunikationDto, String sPartnerkommBez, String sPartnerkommInhalt,
			Integer iIdPartner) throws Throwable {

		if (partnerkommunikationDto == null) {
			partnerkommunikationDto = new PartnerkommunikationDto();
			partnerkommunikationDto.setCNrMandant(LPMain.getTheClient().getMandant());
		}
		partnerkommunikationDto.setKommunikationsartCNr(sPartnerkommBez);
		partnerkommunikationDto.setCInhalt(sPartnerkommInhalt);
		partnerkommunikationDto.setPartnerIId(iIdPartner);
		partnerkommunikationDto.setCBez(sPartnerkommBez);
		return partnerkommunikationDto;
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			wifArtikel.validate();
			components2Dto();
			if (wtfSnrChnr.getText() != null) {
				boolean bSpeichern = validateSeriennummer();
				if (!bSpeichern) {
					return;
				}
			}
			if (reklamationDto.getIId() == null) {
				reklamationDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
				reklamationDto.setIId(
						DelegateFactory.getInstance().getReklamationDelegate().createReklamation(reklamationDto));
				setKeyWhenDetailPanel(reklamationDto.getIId());

				reklamationDto = DelegateFactory.getInstance().getReklamationDelegate()
						.reklamationFindByPrimaryKey(reklamationDto.getIId());

				internalFrameReklamation.setReklamationDto(reklamationDto);
				if (ReklamationFac.REKLAMATIONART_LIEFERANT.equals(reklamationDto.getReklamationartCNr())) {
					DelegateFactory.getInstance().getLieferantDelegate().pruefeLieferant(
							reklamationDto.getLieferantIId(), LocaleFac.BELEGART_REKLAMATION, getInternalFrame());
				}

			} else {

				DelegateFactory.getInstance().getReklamationDelegate().updateReklamation(reklamationDto);
			}

			super.eventActionSave(e, true);

			wtfHandartikel.setActivatable(false);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(reklamationDto.getIId().toString());
			}
			eventYouAreSelected(false);
			reklamationDto = DelegateFactory.getInstance().getReklamationDelegate()
					.reklamationFindByPrimaryKey(reklamationDto.getIId());
			getInternalFrameReklamation().setReklamationDto(reklamationDto);
		}
	}

	public void refreshTelAnsprechpartner() throws Throwable {

		if (reklamationDto != null) {

			if (reklamationDto.getKundeIId() != null) {
				KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(reklamationDto.getKundeIId());
				Integer partnerIId = kdDto.getPartnerIId();
				if (reklamationDto.getAnsprechpartnerIId() != null) {

					AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPrimaryKey(reklamationDto.getAnsprechpartnerIId());

					if (ansprechpartnerDto.getCTelefon() != null) {

						wtfTelAnsp.setPartnerKommunikationDto(kdDto.getPartnerDto(), ansprechpartnerDto.getCTelefon());

						wtfTelAnsp.setTextDurchwahl(
								DelegateFactory.getInstance().getPartnerDelegate().enrichNumber(partnerIId,
										PartnerFac.KOMMUNIKATIONSART_TELEFON, ansprechpartnerDto.getCTelefon(), false));

						if (Helper.short2boolean(ansprechpartnerDto.getBDurchwahl())) {
							wtfTelAnsp.setIstAnsprechpartner(true);
						} else {
							wtfTelAnsp.setIstAnsprechpartner(false);
							wtfTelAnsp.setPartnerKommunikationDto(kdDto.getPartnerDto(),
									ansprechpartnerDto.getCTelefon());
						}
					} else {

						wtfTelAnsp.setPartnerKommunikationDto(kdDto.getPartnerDto(),
								kdDto.getPartnerDto().getCTelefon());

					}

				} else {
					wtfTelAnsp.setText(null);
				}

			}

			if (reklamationDto.getLieferantIId() != null) {

				LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(reklamationDto.getLieferantIId());

				Integer partnerIId = lfDto.getPartnerIId();

				if (reklamationDto.getAnsprechpartnerIIdLieferant() != null) {

					AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPrimaryKey(reklamationDto.getAnsprechpartnerIIdLieferant());
					if (ansprechpartnerDto.getCTelefon() != null) {
						wtfTelAnspLieferant.setPartnerKommunikationDto(lfDto.getPartnerDto(),
								ansprechpartnerDto.getCTelefon());

						wtfTelAnspLieferant.setTextDurchwahl(
								DelegateFactory.getInstance().getPartnerDelegate().enrichNumber(partnerIId,
										PartnerFac.KOMMUNIKATIONSART_TELEFON, ansprechpartnerDto.getCTelefon(), false));
					} else {
						wtfTelAnspLieferant.setPartnerKommunikationDto(lfDto.getPartnerDto(),
								lfDto.getPartnerDto().getCTelefon());
					}

					if (Helper.short2boolean(ansprechpartnerDto.getBDurchwahl())) {
						wtfTelAnspLieferant.setIstAnsprechpartner(true);
					} else {
						wtfTelAnspLieferant.setIstAnsprechpartner(false);
						wtfTelAnspLieferant.setPartnerKommunikationDto(lfDto.getPartnerDto(),
								ansprechpartnerDto.getCTelefon());
					}

				} else {
					wtfTelAnspLieferant.setText(null);
				}
			}
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKostenstelle) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				reklamationDto.setKostenstelleIId(key);

				KostenstelleDto kostenstelleDto = null;
				kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate().kostenstelleFindByPrimaryKey(key);
				wtfKostenstelle.setText(kostenstelleDto.formatKostenstellenbezeichnung());
			} else if (e.getSource() == panelQueryFLRPersonal) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);
					wtfPersonal.setText(personalDto.formatAnrede());
					reklamationDto.setPersonalIIdAufnehmer((Integer) key);
				}

			} else if (e.getSource() == panelQueryFLRSnrChnrAuswahl) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {

					LagerbewegungDto lDto = DelegateFactory.getInstance().getLagerDelegate()
							.lagerbewegungFindByPrimaryKey((Integer) key);

					String cSeriennrchargennr = lDto.getCSeriennrchargennr();
					// Wenn die Seriennummer einen Bindestrich enthaelt, dann muss sie in "" gesetzt
					// werden, sonst wirkt Bindestrich als Bereichsangabe
					if (cSeriennrchargennr.contains("-")) {
						wtfSnrChnr.setText(String.format("\"%s\"", cSeriennrchargennr));
					} else {
						wtfSnrChnr.setText(cSeriennrchargennr);
					}

					ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByPrimaryKey(lDto.getArtikelIId());

					wifArtikel.setArtikelDto(artikelDto);
					reklamationDto.setArtikelIId(artikelDto.getIId());
					wnfMenge.setBigDecimal(lDto.getNMenge());
					if (lDto.getCBelegartnr().equals(LocaleFac.BELEGART_LOS)
							|| lDto.getCBelegartnr().equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {

						LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
								.losFindByPrimaryKey(lDto.getIBelegartid());

						reklamationDto.setLosIId(losDto.getIId());

						reklamationDto.setProjektIId(losDto.getProjektIId());
						wsfProjekt.setKey(losDto.getProjektIId());

						wtfLos.setText(losDto.getCNr());

						if (losDto.getAuftragIId() != null || losDto.getKundeIId() != null) {

							KundeDto kundeDto = null;
							if (losDto.getAuftragIId() != null) {
								kundeDto = DelegateFactory.getInstance().getKundeDelegate()
										.kundeFindByPrimaryKey(DelegateFactory.getInstance().getAuftragDelegate()
												.auftragFindByPrimaryKey(losDto.getAuftragIId())
												.getKundeIIdAuftragsadresse());
							} else {
								kundeDto = DelegateFactory.getInstance().getKundeDelegate()
										.kundeFindByPrimaryKey(losDto.getKundeIId());
							}

							wlaKundeLos.setText(LPMain.getTextRespectUISPr("label.kunde") + ": "
									+ kundeDto.getPartnerDto().formatFixTitelName1Name2());
						}

						if (losDto.getStuecklisteIId() != null) {
							wrbArtikel.setSelected(true);
							wifArtikel.setArtikelDto(artikelDto);
						} else {
							wrbHandeingabe.setSelected(true);
							wtfHandartikel.setText(losDto.getCProjekt());
						}

					} else if (lDto.getCBelegartnr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
						LieferscheinDto lieferscheinDto = DelegateFactory.getInstance().getLsDelegate()
								.lieferscheinFindByPrimaryKey(lDto.getIBelegartid());
						wtfLieferschein.setText(lieferscheinDto.getCNr());
						reklamationDto.setLieferscheinIId(lieferscheinDto.getIId());
						reklamationDto.setProjektIId(lieferscheinDto.getProjektIId());
						wsfProjekt.setKey(lieferscheinDto.getProjektIId());

						if (reklamationDto.getKundeIId() == null) {

							KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
									.kundeFindByPrimaryKey(lieferscheinDto.getKundeIIdLieferadresse());

							DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(
									lieferscheinDto.getKundeIIdLieferadresse(), LocaleFac.BELEGART_REKLAMATION,
									getInternalFrame());

							wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());

							reklamationDto.setKundeIId(lieferscheinDto.getKundeIIdLieferadresse());

							wtfAnsprechpartner.setText(null);
							reklamationDto.setAnsprechpartnerIId(null);

							if (lieferscheinDto.getAnsprechpartnerIId() != null) {
								Integer iIdAnsprechpartner = lieferscheinDto.getAnsprechpartnerIId();
								AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance()
										.getAnsprechpartnerDelegate()
										.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

								wtfAnsprechpartner.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
								reklamationDto.setAnsprechpartnerIId(iIdAnsprechpartner);
							}

						}

						if (lieferscheinDto.getRechnungIId() != null) {
							RechnungDto rechnungDto = DelegateFactory.getInstance().getRechnungDelegate()
									.rechnungFindByPrimaryKey(lieferscheinDto.getRechnungIId());
							wtfRechnung.setText(rechnungDto.getCNr());
							reklamationDto.setRechnungIId(lieferscheinDto.getRechnungIId());

						} else {
							wtfRechnung.setText(null);
							reklamationDto.setRechnungIId(null);
						}

					} else if (lDto.getCBelegartnr().equals(LocaleFac.BELEGART_RECHNUNG)) {

						RechnungDto rechnungDto = DelegateFactory.getInstance().getRechnungDelegate()
								.rechnungFindByPrimaryKey(lDto.getIBelegartid());
						wtfRechnung.setText(rechnungDto.getCNr());
						reklamationDto.setRechnungIId((Integer) key);
						reklamationDto.setProjektIId(rechnungDto.getProjektIId());
						wsfProjekt.setKey(rechnungDto.getProjektIId());

						if (reklamationDto.getKundeIId() == null) {

							KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
									.kundeFindByPrimaryKey(rechnungDto.getKundeIId());

							DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(rechnungDto.getKundeIId(),
									LocaleFac.BELEGART_REKLAMATION, getInternalFrame());

							wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());

							reklamationDto.setKundeIId(rechnungDto.getKundeIId());

							wtfAnsprechpartner.setText(null);
							reklamationDto.setAnsprechpartnerIId(null);

							if (rechnungDto.getAnsprechpartnerIId() != null) {
								Integer iIdAnsprechpartner = rechnungDto.getAnsprechpartnerIId();
								AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance()
										.getAnsprechpartnerDelegate()
										.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

								wtfAnsprechpartner.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
								reklamationDto.setAnsprechpartnerIId(iIdAnsprechpartner);
							}

						}

					} else if (lDto.getCBelegartnr().equals(LocaleFac.BELEGART_BESTELLUNG)) {

						WareneingangspositionDto wareneingangpositionDto = DelegateFactory.getInstance()
								.getWareneingangDelegate()
								.wareneingangspositionFindByPrimaryKey(lDto.getIBelegartpositionid());
						WareneingangDto wareneingangDto = DelegateFactory.getInstance().getWareneingangDelegate()
								.wareneingangFindByPrimaryKey(wareneingangpositionDto.getWareneingangIId());
						wtfWareneingang.setText(wareneingangDto.getCLieferscheinnr());
						reklamationDto.setWareneingangIId(wareneingangDto.getIId());

						BestellungDto bestellungDto = DelegateFactory.getInstance().getBestellungDelegate()
								.bestellungFindByPrimaryKey(wareneingangDto.getBestellungIId());
						wtfBestellung.setText(bestellungDto.getCNr());
						reklamationDto.setBestellungIId(wareneingangDto.getBestellungIId());

						reklamationDto.setProjektIId(bestellungDto.getProjektIId());
						wsfProjekt.setKey(bestellungDto.getProjektIId());

						LieferantDto lieferantDtoNew = DelegateFactory.getInstance().getLieferantDelegate()
								.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse());
						wtfLieferant.setText(lieferantDtoNew.getPartnerDto().formatFixTitelName1Name2());
						reklamationDto.setLieferantIId(bestellungDto.getLieferantIIdBestelladresse());

					}

				}

			} else if (e.getSource() == wifArtikel.getPanelQueryFLRArtikel()) {
				reklamationDto.setArtikelIId((Integer) wifArtikel.getPanelQueryFLRArtikel().getSelectedId());
			} else if (e.getSource() == panelQueryFLRVerursacher) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);
					wtfVerursacher.setText(personalDto.formatAnrede());
					reklamationDto.setPersonalIIdVerursacher((Integer) key);
				}
			} else if (e.getSource() == panelQueryFLRAufnahmeart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				AufnahmeartDto aufnahmeartDto = DelegateFactory.getInstance().getReklamationDelegate()
						.aufnahmeartFindByPrimaryKey((Integer) key);
				wtfAufnahmeart.setText(aufnahmeartDto.getCBez());
				reklamationDto.setAufnahmeartIId((Integer) key);

			} else if (e.getSource() == panelQueryFLRMaschine) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				MaschineDto maschineDto = null;
				maschineDto = DelegateFactory.getInstance().getZeiterfassungDelegate().maschineFindByPrimaryKey(key);
				reklamationDto.setMaschineIId(maschineDto.getIId());

				wtfMaschine.setText(maschineDto.getBezeichnung());

				if (maschineDto.getMaschinengruppeIId() != null) {
					wlaMaschinengruppe.setText(LPMain.getTextRespectUISPr("pers.maschinengruppe") + ": "
							+ DelegateFactory.getInstance().getZeiterfassungDelegate()
									.maschinengruppeFindByPrimaryKey(maschineDto.getMaschinengruppeIId()).getCBez());
				}

			} else if (e.getSource() == panelQueryFLRLieferant) {
				Integer keyLieferant = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				LieferantDto lieferantDtoNew = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(keyLieferant);
				wtfLieferant.setText(lieferantDtoNew.getPartnerDto().formatFixTitelName1Name2());
				reklamationDto.setLieferantIId(keyLieferant);

				wtfAnsprechpartnerLieferant.setText(null);
				reklamationDto.setAnsprechpartnerIIdLieferant(null);

			} else if (e.getSource() == panelQueryFLRLos) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate().losFindByPrimaryKey(key);
				wtfLos.setText(losDto.getCNr());
				reklamationDto.setLosIId(key);
				reklamationDto.setProjektIId(losDto.getProjektIId());
				wsfProjekt.setKey(losDto.getProjektIId());

				wtfArbeitsgang.setText("");
				reklamationDto.setLossollarbeitsplanIId(null);

				if (losDto.getAuftragIId() != null || losDto.getKundeIId() != null) {

					KundeDto kundeDto = null;
					if (losDto.getAuftragIId() != null) {
						kundeDto = DelegateFactory.getInstance().getKundeDelegate()
								.kundeFindByPrimaryKey(DelegateFactory.getInstance().getAuftragDelegate()
										.auftragFindByPrimaryKey(losDto.getAuftragIId()).getKundeIIdAuftragsadresse());
					} else {
						kundeDto = DelegateFactory.getInstance().getKundeDelegate()
								.kundeFindByPrimaryKey(losDto.getKundeIId());
					}

					wlaKundeLos.setText(LPMain.getTextRespectUISPr("label.kunde") + ": "
							+ kundeDto.getPartnerDto().formatFixTitelName1Name2());
				}

				if (losDto.getStuecklisteIId() != null) {
					wrbArtikel.setSelected(true);
					ArtikelDto artikelDto = DelegateFactory.getInstance().getStuecklisteDelegate()
							.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId()).getArtikelDto();

					wifArtikel.setArtikelDto(artikelDto);
				} else {
					wrbHandeingabe.setSelected(true);
					wtfHandartikel.setText(losDto.getCProjekt());
				}

			} else if (e.getSource() == panelQueryFLRArbeitsgang) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				LossollarbeitsplanDto sollarbeitsplanDto = DelegateFactory.getInstance().getFertigungDelegate()
						.lossollarbeitsplanFindByPrimaryKey(key);

				reklamationDto.setLossollarbeitsplanIId(key);

				String sollarb = "AG:" + sollarbeitsplanDto.getIArbeitsgangnummer();
				if (sollarbeitsplanDto.getIUnterarbeitsgang() != null) {
					sollarb += " UAG:" + sollarbeitsplanDto.getIUnterarbeitsgang();
				}
				wtfArbeitsgang.setText(sollarb);
				if (reklamationDto.getMaschineIId() == null && sollarbeitsplanDto.getMaschineIId() != null) {
					MaschineDto maschineDto = null;
					maschineDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
							.maschineFindByPrimaryKey(sollarbeitsplanDto.getMaschineIId());
					reklamationDto.setMaschineIId(maschineDto.getIId());

					wtfMaschine.setText(maschineDto.getBezeichnung());

					if (maschineDto.getMaschinengruppeIId() != null) {
						wlaMaschinengruppe.setText(LPMain.getTextRespectUISPr("pers.maschinengruppe") + ": "
								+ DelegateFactory.getInstance().getZeiterfassungDelegate()
										.maschinengruppeFindByPrimaryKey(maschineDto.getMaschinengruppeIId())
										.getCBez());
					}

				}

			}

			else if (e.getSource() == panelQueryFLRFehlerangabe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				FehlerangabeDto fehlerangabeDto = DelegateFactory.getInstance().getReklamationDelegate()
						.fehlerangabeFindByPrimaryKey((Integer) key);
				wtfFehlerangabe.setText(fehlerangabeDto.getCBez());
				reklamationDto.setFehlerangabeIId((Integer) key);

			} else if (e.getSource() == panelQueryFLRKunde) {
				Integer iIdKunde = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(iIdKunde);

				DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(iIdKunde, LocaleFac.BELEGART_REKLAMATION,
						getInternalFrame());

				wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());

				reklamationDto.setKundeIId(iIdKunde);

				wtfAnsprechpartner.setText(null);
				reklamationDto.setAnsprechpartnerIId(null);
			} else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

				wtfAnsprechpartner.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());

				reklamationDto.setAnsprechpartnerIId(iIdAnsprechpartner);

				refreshTelAnsprechpartner();
			} else if (e.getSource() == panelQueryFLRAnsprechpartner_Lieferant) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

				wtfAnsprechpartnerLieferant.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
				wtfTelAnspLieferant.setText(ansprechpartnerDto.getCTelefon());
				reklamationDto.setAnsprechpartnerIIdLieferant(iIdAnsprechpartner);
			} else if (e.getSource() == panelQueryFLRLieferschein) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				LieferscheinDto lieferscheinDto = DelegateFactory.getInstance().getLsDelegate()
						.lieferscheinFindByPrimaryKey((Integer) key);
				wtfLieferschein.setText(lieferscheinDto.getCNr());
				reklamationDto.setLieferscheinIId(key);

				reklamationDto.setProjektIId(lieferscheinDto.getProjektIId());
				wsfProjekt.setKey(lieferscheinDto.getProjektIId());

				if (reklamationDto.getKundeIId() == null) {

					KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByPrimaryKey(lieferscheinDto.getKundeIIdLieferadresse());

					DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(
							lieferscheinDto.getKundeIIdLieferadresse(), LocaleFac.BELEGART_REKLAMATION,
							getInternalFrame());

					wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());

					reklamationDto.setKundeIId(lieferscheinDto.getKundeIIdLieferadresse());

					wtfAnsprechpartner.setText(null);
					reklamationDto.setAnsprechpartnerIId(null);

					if (lieferscheinDto.getAnsprechpartnerIId() != null) {
						Integer iIdAnsprechpartner = lieferscheinDto.getAnsprechpartnerIId();
						AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance()
								.getAnsprechpartnerDelegate().ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

						wtfAnsprechpartner.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
						reklamationDto.setAnsprechpartnerIId(iIdAnsprechpartner);
					}

				}

				if (lieferscheinDto.getRechnungIId() != null) {
					RechnungDto rechnungDto = DelegateFactory.getInstance().getRechnungDelegate()
							.rechnungFindByPrimaryKey(lieferscheinDto.getRechnungIId());
					wtfRechnung.setText(rechnungDto.getCNr());
					reklamationDto.setRechnungIId(lieferscheinDto.getRechnungIId());
				} else {
					wtfRechnung.setText(null);
					reklamationDto.setRechnungIId(null);
				}
			} else if (e.getSource() == panelQueryFLRRechnung) {

				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				RechnungDto rechnungDto = DelegateFactory.getInstance().getRechnungDelegate()
						.rechnungFindByPrimaryKey(key);
				wtfRechnung.setText(rechnungDto.getCNr());
				reklamationDto.setRechnungIId(key);

				reklamationDto.setProjektIId(rechnungDto.getProjektIId());
				wsfProjekt.setKey(rechnungDto.getProjektIId());

			} else if (e.getSource() == panelQueryFLRWareneingang) {

				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				WareneingangDto wareneingangDto = DelegateFactory.getInstance().getWareneingangDelegate()
						.wareneingangFindByPrimaryKey(key);
				wtfWareneingang.setText(wareneingangDto.getCLieferscheinnr());
				reklamationDto.setWareneingangIId(key);
			}

			else if (e.getSource() == panelQueryFLRBestellung) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				BestellungDto bestellungDto = DelegateFactory.getInstance().getBestellungDelegate()
						.bestellungFindByPrimaryKey((Integer) key);
				wtfBestellung.setText(bestellungDto.getCNr());
				reklamationDto.setBestellungIId((Integer) key);

				reklamationDto.setProjektIId(bestellungDto.getProjektIId());
				wsfProjekt.setKey(bestellungDto.getProjektIId());

			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				wtfAnsprechpartner.setText(null);
				reklamationDto.setAnsprechpartnerIId(null);
			} else if (e.getSource() == panelQueryFLRVerursacher) {
				wtfVerursacher.setText(null);
				reklamationDto.setPersonalIIdVerursacher(null);

			} else if (e.getSource() == panelQueryFLRAnsprechpartner_Lieferant) {
				wtfAnsprechpartnerLieferant.setText(null);
				reklamationDto.setAnsprechpartnerIIdLieferant(null);
			} else if (e.getSource() == panelQueryFLRKunde) {
				wtfKunde.setText(null);
				reklamationDto.setKundeIId(null);

			} else if (e.getSource() == panelQueryFLRLos) {
				wtfLos.setText(null);
				reklamationDto.setLosIId(null);
				wtfArbeitsgang.setText(null);
				reklamationDto.setLossollarbeitsplanIId(null);
				wlaKundeLos.setText("");
			} else if (e.getSource() == panelQueryFLRMaschine) {
				wtfMaschine.setText(null);
				reklamationDto.setMaschineIId(null);
				wlaMaschinengruppe.setText("");
			} else if (e.getSource() == panelQueryFLRLieferschein) {
				wtfLieferschein.setText(null);
				reklamationDto.setLieferscheinIId(null);
			} else if (e.getSource() == wifArtikel.getPanelQueryFLRArtikel()) {
				reklamationDto.setArtikelIId(null);
			}
		}

		refreshTelAnsprechpartner();
	}

	private class SnrFocusListener extends FocusAdapter {
		@Override
		public void focusLost(FocusEvent e) {
			try {
				setMengeFeldFromSeriennummer();
			} catch (ExceptionLP e1) {
				myLogger.warn("Exception beim automatischen Setzen der Menge aus Seriennummern", e1);
			}
		}
	}
}
