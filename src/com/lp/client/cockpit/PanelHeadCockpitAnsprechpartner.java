package com.lp.client.cockpit;
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.IPartnerDtoService;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.MandantFac;
import com.lp.util.Helper;

/*
 * <p><I>Diese Klasse kuemmert sich um das Panel Partner.</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>20.10.04</I></p>
 *
 * @author $Author: christian $
 *
 * @version $Revision: 1.22 $
 *
 * Date $Date: 2012/11/09 08:01:49 $
 */
public class PanelHeadCockpitAnsprechpartner extends PanelBasis {

	private static final long serialVersionUID = 8590199699076800120L;

	protected JPanel jpaWorkingOn = null;

	protected JPanel jpaButtonAction = null;
	protected WrapperGotoButton wcoAnrede = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_PARTNER_AUSWAHL);;
	protected Border border = null;
	protected WrapperLabel wla1 = null;
	protected WrapperLabel wlaLocaleKommunikation = null;
	protected WrapperLabel wcoLocaleKommunikation = null;
	protected Map<?, ?> tmLocales = null;

	protected GridBagLayout gblAussen = null;
	protected GridBagLayout gblPartner = null;
	protected WrapperTextField wtfName2 = null;
	protected WrapperLabel wlaVorname = null;
	protected WrapperTextField wtfName1 = null;

	protected WrapperLabel wbuPostfach = null;
	protected WrapperTextField wtfOrtPostfach = null;

	protected WrapperTextField wtfStrasse = null;
	protected WrapperLabel wlaStrasse = null;
	protected WrapperLabel wlaAbteilung = null;
	protected WrapperTextField wtfName3 = null;
	protected WrapperLabel wbuOrt = null;
	protected WrapperTextField wtfLandPLZOrt = null;
	private WrapperLabel wlaGmtVersatz = null;

	private IPartnerDtoService partnerDtoService;

	protected JPanel panelInfoKunde = new JPanel(new GridBagLayout());

	private boolean bUmsatzGeschaeftsjahr = false;
	private WrapperKeyValueField wkvUmsatzLfdJahr = null;
	private WrapperKeyValueField wkvUmsatzVorjahr = null;
	private WrapperKeyValueField wkvGelegteRechnungenLfdJahr = null;
	private WrapperKeyValueField wkvGelegteRechnungenVorjahr = null;

	private WrapperKeyValueField wkvOffenerRechnungswert = null;
	private WrapperKeyValueField wkvOffenerLSwert = null;

	private WrapperKeyValueField wkvZahlungsmoral = null;

	private ButtonGroup buttonGroupUmsatz = new ButtonGroup();
	private JRadioButton wrbKundenadresse = new JRadioButton();
	private JRadioButton wrbStatistikadresse = new JRadioButton();

	private KundeDto kundeDto = null;

	public PanelHeadCockpitAnsprechpartner(InternalFrame internalFrame, String add2TitleI, Object key,
			IPartnerDtoService partnerDtoService) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.partnerDtoService = partnerDtoService;

		jbInit();

		initPanel();
	}

	protected PartnerDto getPartnerDto() {
		return partnerDtoService.getServicePartnerDto();
	}

	protected void setPartnerDto(PartnerDto partnerDto) {
		partnerDtoService.setServicePartnerDto(partnerDto);
	}

	public Integer getPartnerIId() {
		if (partnerDtoService.getServicePartnerDto() == null)
			return null;
		return partnerDtoService.getServicePartnerDto().getIId();
	}

	/**
	 * Fuelle die Helpercontrols, jene aus denen ausgewaehlt werden kann.
	 * 
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	private void initPanel() throws Throwable {

	}

	private void jbInit() throws Throwable {

		// Buttons
		resetToolsPanel();

		// das aussenpanel hat immer das gridbaglayout.
		gblAussen = new GridBagLayout();
		setLayout(gblAussen);

		jpaWorkingOn = new JPanel();

		wtfName3 = new WrapperTextField(PartnerFac.MAX_NAME);
		wtfName3.setText("");

		wtfName3.setMandatoryFieldDB(false);

		wlaStrasse = new WrapperLabel(LPMain.getTextRespectUISPr("lp.strasse"));

		wtfStrasse = new WrapperTextField(PartnerFac.MAX_STRASSE);
		wtfStrasse.setText(LPMain.getTextRespectUISPr("lp.uid"));

		wtfStrasse.setMandatoryFieldDB(false);

		wbuOrt = new WrapperLabel(LPMain.getTextRespectUISPr("part.button.ort"));

		wlaGmtVersatz = new WrapperLabel(LPMain.getTextRespectUISPr("lp.land.gmtversatz"));

		// flrlokal: damit der event via internalframe direkt hierherkommt,
		// nicht ueber tabbedpanes.
		getInternalFrame().addItemChangedListener(this);

		wtfLandPLZOrt = new WrapperTextField();
		wtfLandPLZOrt.setActivatable(false);

		wtfLandPLZOrt.setText("");

		// Workingpanel
		gblPartner = new GridBagLayout();
		jpaWorkingOn.setLayout(gblPartner);

		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		wla1 = new WrapperLabel();
		wla1.setText("");

		wlaAbteilung = new WrapperLabel(LPMain.getTextRespectUISPr("lp.zusatzbezeichnung2"));

		wtfName2 = new WrapperTextField(PartnerFac.MAX_NAME);

		wtfName1 = new WrapperTextField(PartnerFac.MAX_NAME);
		wtfName1.setMandatoryFieldDB(true);

		wtfName1.setDependenceField(true);

		wlaVorname = new WrapperLabel(LPMain.getTextRespectUISPr("lp.zusatzbezeichnung"));

		wcoLocaleKommunikation = new WrapperLabel();

		wlaLocaleKommunikation = new WrapperLabel(LPMain.getTextRespectUISPr("part.sprache.kommunikation"));

		wtfOrtPostfach = new WrapperTextField();
		wtfOrtPostfach.setActivatable(false);

		wbuPostfach = new WrapperLabel(LPMain.getTextRespectUISPr("button.postfach"));

		jpaWorkingOn.add(wcoAnrede, new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 110, 0));

		jpaWorkingOn.add(wtfName1, new GridBagConstraints(1, iZeile, 3, 1, 0.4, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(panelInfoKunde, new GridBagConstraints(5, iZeile, 1, 7, 1.5, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaVorname, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfName2, new GridBagConstraints(1, iZeile, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaAbteilung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfName3, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaStrasse, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfStrasse, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 40), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wbuOrt, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfLandPLZOrt, new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wbuPostfach, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfOrtPostfach, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaLocaleKommunikation, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcoLocaleKommunikation, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 120, 0));

		jpaWorkingOn.add(wlaGmtVersatz, new GridBagConstraints(2, iZeile, 2, 1, 0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 110, 0));

		// PANELKUNDE
		final int iBreiteSpalte1 = Defaults.getInstance().bySizeFactor(90);
		final int iBreiteSpalte2 = Defaults.getInstance().bySizeFactor(50);
		wkvUmsatzLfdJahr = new WrapperKeyValueField(iBreiteSpalte1);
		if (bUmsatzGeschaeftsjahr) {
			wkvUmsatzLfdJahr.setKey(LPMain.getInstance().getTextRespectUISPr("part.umsatz.lfd.gf_jahr"));
		} else {
			wkvUmsatzLfdJahr.setKey(LPMain.getInstance().getTextRespectUISPr("part.umsatz.lfd_jahr"));
		}
		wkvUmsatzVorjahr = new WrapperKeyValueField(iBreiteSpalte2);

		wkvGelegteRechnungenLfdJahr = new WrapperKeyValueField(iBreiteSpalte1);
		wkvGelegteRechnungenLfdJahr.setKey(LPMain.getInstance().getTextRespectUISPr("part.gelegterechnung.heuer"));

		wkvUmsatzVorjahr.setKey(LPMain.getInstance().getTextRespectUISPr("lp.vorjahr"));
		wkvGelegteRechnungenVorjahr = new WrapperKeyValueField(iBreiteSpalte2);

		wkvOffenerRechnungswert = new WrapperKeyValueField(iBreiteSpalte1);
		wkvOffenerRechnungswert.setKey(LPMain.getInstance().getTextRespectUISPr("kund.offenre"));
		wkvOffenerLSwert = new WrapperKeyValueField(iBreiteSpalte2);
		wkvOffenerLSwert.setKey(LPMain.getInstance().getTextRespectUISPr("kund.offenls"));

		// Zahlungsmoral
		wkvZahlungsmoral = new WrapperKeyValueField(iBreiteSpalte1);
		wkvZahlungsmoral.setKey(LPMain.getInstance().getTextRespectUISPr("lp.zahlungsmoral.kurz"));

		boolean bDarfUmsaetzeSehen = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PART_KUNDE_UMSAETZE_R);
		if (bDarfUmsaetzeSehen == false) {
			wkvGelegteRechnungenLfdJahr.setVisible(false);
			wkvGelegteRechnungenVorjahr.setVisible(false);
			wkvOffenerLSwert.setVisible(false);
			wkvOffenerRechnungswert.setVisible(false);
			wkvUmsatzLfdJahr.setVisible(false);
			wkvUmsatzVorjahr.setVisible(false);
			wkvZahlungsmoral.setVisible(false);

		}

		// Statistikadresse
		int iZeileKundeInfo = 0;
		wrbKundenadresse.setText(LPMain.getTextRespectUISPr("part.kunde.kundenliste.kundenadresse"));
		wrbStatistikadresse.setText(LPMain.getTextRespectUISPr("part.kunde.kundenliste.statistikadresse"));
		wrbKundenadresse.setSelected(true);

		wrbKundenadresse.addActionListener(this);
		wrbStatistikadresse.addActionListener(this);

		wrbKundenadresse.setHorizontalAlignment(SwingConstants.RIGHT);

		buttonGroupUmsatz.add(wrbKundenadresse);
		buttonGroupUmsatz.add(wrbStatistikadresse);

		panelInfoKunde.add(wkvUmsatzLfdJahr, new GridBagConstraints(0, iZeileKundeInfo, 1, 1, 1, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 160, 0));
		panelInfoKunde.add(wkvUmsatzVorjahr, new GridBagConstraints(1, iZeileKundeInfo, 1, 1, 0.5, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 140, 0));

		iZeileKundeInfo++;
		panelInfoKunde.add(wkvGelegteRechnungenLfdJahr, new GridBagConstraints(0, iZeileKundeInfo, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelInfoKunde.add(wkvGelegteRechnungenVorjahr, new GridBagConstraints(1, iZeileKundeInfo, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeileKundeInfo++;
		panelInfoKunde.add(wkvOffenerRechnungswert, new GridBagConstraints(0, iZeileKundeInfo, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelInfoKunde.add(wkvOffenerLSwert, new GridBagConstraints(1, iZeileKundeInfo, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeileKundeInfo++;
		panelInfoKunde.add(wkvZahlungsmoral, new GridBagConstraints(0, iZeileKundeInfo, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panelInfoKunde.add(new JLabel(LPMain.getTextRespectUISPr("lp.zahlungsmoral.tage")),
				new GridBagConstraints(0, iZeileKundeInfo, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeileKundeInfo++;

		panelInfoKunde.add(wrbKundenadresse, new GridBagConstraints(0, iZeileKundeInfo, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panelInfoKunde.add(wrbStatistikadresse, new GridBagConstraints(1, iZeileKundeInfo, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);

		kundeDto = null;

		panelInfoKunde.setVisible(false);

		if (!bNeedNoYouAreSelectedI) {
			Object key = getPartnerDto().getIId();
			if (key == null) {
				leereAlleFelder(this);
				clearStatusbar();
			} else {
				setPartnerDto(
						DelegateFactory.getInstance().getPartnerDelegate().partnerFindByPrimaryKey((Integer) key));

				kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByiIdPartnercNrMandantOhneExc(
						getPartnerDto().getIId(), LPMain.getTheClient().getMandant());

				getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
						getPartnerDto().formatFixTitelName1Name2());
				setStatusbar();

				if (kundeDto != null) {
					panelInfoKunde.setVisible(true);
				}

			}
			initPanel();
			// updateComponents();
			dto2Components();
		}
	}

	protected void setDefaults() throws Throwable {

		if (getPartnerDto() == null) {
			setPartnerDto(new PartnerDto());
		}

		getPartnerDto().setBVersteckt(Helper.boolean2Short(false));

		getPartnerDto().setLocaleCNrKommunikation(LPMain.getTheClient().getLocMandantAsString());

	}

	protected void setStatusbar() throws Throwable {
		setStatusbarModification(getPartnerDto());
	}

	/**
	 * Fuelle alle Panelfelder.
	 * 
	 * @throws Throwable Ausnahme
	 */
	protected void dto2Components() throws Throwable {

		PartnerDto partnerDto = getPartnerDto();

		wtfName1.setText(partnerDto.getCName1nachnamefirmazeile1());
		wtfName2.setText(partnerDto.getCName2vornamefirmazeile2());
		wtfName3.setText(partnerDto.getCName3vorname2abteilung());
		wtfStrasse.setText(partnerDto.getCStrasse());

		wcoAnrede.setText(partnerDto.getAnredeCNr());
		wcoAnrede.setOKey(partnerDto.getIId());
		String locKommunikationAsString = partnerDto.getLocaleCNrKommunikation();
		wcoLocaleKommunikation.setText(locKommunikationAsString);

		LandplzortDto landplzortDto = partnerDto.getLandplzortDto();
		String lKZ = null;
		if (landplzortDto != null) {
			lKZ = landplzortDto.formatLandPlzOrt();
		}
		wtfLandPLZOrt.setText(lKZ);

		landplzortDto = partnerDto.getLandplzortDto_Postfach();
		lKZ = null;
		if (landplzortDto != null) {
			lKZ = landplzortDto.formatLandPlzOrt();
		}
		wtfOrtPostfach.setText(lKZ);
		if (partnerDto.getFGmtversatz() != null) {
			wlaGmtVersatz.setText(LPMain.getTextRespectUISPr("lp.land.gmtversatz") + " "
					+ Helper.formatZahl(partnerDto.getFGmtversatz(), 2, LPMain.getTheClient().getLocUi()));
		} else {
			wlaGmtVersatz.setText(LPMain.getTextRespectUISPr("lp.land.gmtversatz"));
		}

		umsaetzeBerechnen();

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getSource().equals(wrbKundenadresse) || e.getSource().equals(wrbStatistikadresse)) {
			umsaetzeBerechnen();
		}

	}

	private void umsaetzeBerechnen() throws ExceptionLP, Throwable {

		if (kundeDto != null) {

			if (wrbKundenadresse.isSelected()) {

				wkvUmsatzLfdJahr
						.setValue(Helper.formatZahl(
								DelegateFactory.getInstance().getRechnungDelegate()
										.getUmsatzVomKundenHeuer(kundeDto.getIId(), false, bUmsatzGeschaeftsjahr),
								2, LPMain.getTheClient().getLocUi()));
				wkvUmsatzVorjahr
						.setValue(Helper.formatZahl(
								DelegateFactory.getInstance().getRechnungDelegate()
										.getUmsatzVomKundenVorjahr(kundeDto.getIId(), false, bUmsatzGeschaeftsjahr),
								2, LPMain.getTheClient().getLocUi()));
				wkvGelegteRechnungenLfdJahr
						.setValue(Helper.formatZahl(DelegateFactory.getInstance().getRechnungDelegate()
								.getAnzahlDerRechnungenVomKundenHeuer(kundeDto.getIId(), false, bUmsatzGeschaeftsjahr),
								0, LPMain.getTheClient().getLocUi()));

				wkvGelegteRechnungenVorjahr.setValue(Helper.formatZahl(
						DelegateFactory.getInstance().getRechnungDelegate().getAnzahlDerRechnungenVomKundenVorjahr(
								kundeDto.getIId(), false, bUmsatzGeschaeftsjahr),
						0, LPMain.getTheClient().getLocUi()));
				wkvZahlungsmoral.setValue(Helper.formatZahl(DelegateFactory.getInstance().getRechnungDelegate()
						.getZahlungsmoraleinesKunden(kundeDto.getIId(), false), 0, LPMain.getTheClient().getLocUi()));

				wkvOffenerRechnungswert.setValue(Helper.formatZahl(DelegateFactory.getInstance().getRechnungDelegate()
						.berechneSummeOffenNetto(kundeDto.getIId(), false), 2, LPMain.getTheClient().getLocUi()));

			} else {

				wkvUmsatzLfdJahr
						.setValue(Helper.formatZahl(
								DelegateFactory.getInstance().getRechnungDelegate()
										.getUmsatzVomKundenHeuer(kundeDto.getIId(), true, bUmsatzGeschaeftsjahr),
								2, LPMain.getTheClient().getLocUi()));
				wkvUmsatzVorjahr
						.setValue(Helper.formatZahl(
								DelegateFactory.getInstance().getRechnungDelegate()
										.getUmsatzVomKundenVorjahr(kundeDto.getIId(), true, bUmsatzGeschaeftsjahr),
								2, LPMain.getTheClient().getLocUi()));
				wkvGelegteRechnungenLfdJahr
						.setValue(Helper.formatZahl(DelegateFactory.getInstance().getRechnungDelegate()
								.getAnzahlDerRechnungenVomKundenHeuer(kundeDto.getIId(), true, bUmsatzGeschaeftsjahr),
								0, LPMain.getTheClient().getLocUi()));

				wkvGelegteRechnungenVorjahr
						.setValue(Helper.formatZahl(DelegateFactory.getInstance().getRechnungDelegate()
								.getAnzahlDerRechnungenVomKundenVorjahr(kundeDto.getIId(), true, bUmsatzGeschaeftsjahr),
								0, LPMain.getTheClient().getLocUi()));
				wkvZahlungsmoral.setValue(Helper.formatZahl(DelegateFactory.getInstance().getRechnungDelegate()
						.getZahlungsmoraleinesKunden(kundeDto.getIId(), true), 0, LPMain.getTheClient().getLocUi()));

				wkvOffenerRechnungswert.setValue(Helper.formatZahl(DelegateFactory.getInstance().getRechnungDelegate()
						.berechneSummeOffenNetto(kundeDto.getIId(), true), 2, LPMain.getTheClient().getLocUi()));

			}
		} else {

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.ACTION_NEW) {

		} else if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PARTNER;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfName1;
	}

}