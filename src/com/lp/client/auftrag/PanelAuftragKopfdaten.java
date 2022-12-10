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
package com.lp.client.auftrag;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogGeaenderteKonditionenVK;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperBelegDateField;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperGotoKundeMapButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.partner.IPartnerDto;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.rechnung.InternalFrameRechnung;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragdokumentDto;
import com.lp.server.auftrag.service.AuftragkostenstelleDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.BelegDatumClient;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.GotoHelper;
import com.lp.util.Helper;

import net.miginfocom.swing.MigLayout;

/*
 * <p>In diesem Detailfenster des Auftrags werden Kopfdaten erfasst bzw.
 * geaendert.</p> <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2004-08-03</p> <p> </p>
 *
 * @author Uli Walch
 *
 * @version $Revision: 1.51 $
 */
public class PanelAuftragKopfdaten extends PanelBasis implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;

	/** Cache for convenience. */
	private InternalFrameAuftrag intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAuftrag tpAuftrag = null;

	// dtos in diesem panel
	private AuftragDto rahmenauftragDto = null;
	private AnsprechpartnerDto ansprechpartnerDto = null;
	private AnsprechpartnerDto ansprechpartnerDtoLieferadresse = null;
	private AnsprechpartnerDto ansprechpartnerDtoRechnungsadresse = null;
	private PersonalDto vertreterDto = null;
	private KundeDto kundeLieferadresseDto = null;
	private KundeDto kundeRechnungsadresseDto = null;
	private WaehrungDto waehrungDto = null;
	private KostenstelleDto kostenstelleDto = null;

	private PanelQueryFLR panelQueryFLRAbbuchungsLager = null;
	private PanelQueryFLR panelQueryFLRArtikelFuerSchnellanlage = null;

	private PanelDialogAuftragArtikelSchnellanlage panelDialogAuftragArtikelSchnellanlage = null;

	// aenderewaehrung: 0 die urspruengliche Belegwaehrung hinterlegen
	private WaehrungDto waehrungOriDto = null;

	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;
	private WrapperButton jbuSetNull = null;

	private final static String ACTION_SPECIAL_AUFTRAGART_CHANGED = "action_special_auftragart_changed";
	private final static String ACTION_SPECIAL_RAHMENAUSWAHL = "action_special_rahmenauswahl";
	private static final String ACTION_SPECIAL_KUNDE_AUFTRAG = "action_special_kunde_auftrag";
	private static final String ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE = "action_special_ansprechpartner_kunde";
	private static final String ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERADRESSE = "action_special_ansprechpartner_lieferadresse";
	private static final String ACTION_SPECIAL_ANSPRECHPARTNER_RECHNUNGSADRESSE = "action_special_ansprechpartner_rechungsadresse";
	private static final String ACTION_SPECIAL_VERTRETER_KUNDE = "action_special_vertreter_kunde";
	private static final String ACTION_SPECIAL_LIEFERADRESSE_AUFTRAG = "action_special_lieferadresse_auftrag";
	private static final String ACTION_SPECIAL_RECHNUNGSADRESSE_AUFTRAG = "action_special_rechnungsadresse_auftrag";
	private static final String ACTION_SPECIAL_WAEHRUNG = "action_special_waehrung";
	private static final String ACTION_SPECIAL_KOSTENSTELLE = "action_special_kostenstelle";
	// private static final String ACTION_SPECIAL_LIEFERTERMIN =
	// "action_special_liefertermin";
	static final public String ACTION_SPECIAL_ABBUCHUNGSLAGER_FROM_LISTE = "action_abbuchungslager_from_liste";

	public final static String MY_OWN_NEW_TOGGLE_FREIGABE = PanelBasis.ACTION_MY_OWN_NEW + "MY_OWN_NEW_FREIGABE";
	public final static String MY_OWN_NEW_ANZAHLUNGSRECHNUNG_AUS_AUFTRAG = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_ANZAHLUNGSRECHNUNG_AUS_AUFTRAG";

	private static final String ACTION_SPECIAL_VERSTECKEN = "action_special_verstecken";
	private static final String ACTION_SPECIAL_ORDERRESPONSE = "action_special_orderresponse";
	private final static String ACTION_SPECIAL_LAENDERART_CHANGED = "action_special_laenderart_changed";
	static final public String ACTION_SPECIAL_VERTRETER2 = "action_special_vertreter2";

	private WrapperButton wbuAbbuchungslager = null;
	private WrapperTextField wtfAbbuchungslager = null;

	private WrapperLabel wlaAuftragsart = null;
	private WrapperComboBox wcoAuftragsart = null;
	private WrapperLabel jLabelBelegdatum = null;
	private WrapperDateField wdfBelegdatum = null;

	private WrapperGotoButton jButtonKunde = null;
	private PanelQueryFLR panelQueryFLRKunde = null;

	private WrapperButton wbuAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;

	private WrapperButton wbuAnsprechpartnerRechungsadresse = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner_Rechungsadresse = null;
	private WrapperTextField wtfAnsprechpartnerRechungsadresse = null;

	private WrapperButton wbuAnsprechpartnerLieferadresse = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner_Lieferadresse = null;

	private WrapperButton jButtonVertreter = null;
	private PanelQueryFLR panelQueryFLRVertreter = null;

	private WrapperButton jButtonLieferadresse = null;
	private PanelQueryFLR panelQueryFLRLieferadresse = null;

	private WrapperButton jButtonRechnungsadresse = null;
	private PanelQueryFLR panelQueryFLRRechnungsadresse = null;

	private WrapperButton wbuKostenstelle = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;

	private WrapperLabel wlaAdresse = null;
	private WrapperTextField wtfKundeAuftrag = null;
	private WrapperTextField wtfKundeAuftragAdresse = null;
	private WrapperLabel wlaKundeAuftragAbteilung = null;
	private WrapperTextField wtfKundeAuftragAbteilung = null;
	private WrapperTextField wtfAnsprechpartner = null;
	private WrapperTextField wtfAnsprechpartnerLieferadresse = null;
	private WrapperTextField wtfVertreter = null;
	private WrapperTextField wtfKundeLieferadresse = null;
	private WrapperTextField wtfKundeRechnungsadresse = null;

	private WrapperButton wbuVertreter2 = null;
	private WrapperTextField wtfVertreter2 = null;
	private PanelQueryFLR panelQueryFLRVertreter2 = null;

	private Integer kundeIIdLetzterSchnellauftrag = null;

	private WrapperLabel wlaVersteckt = new WrapperLabel();

	private WrapperButton wbuWaehrung = null;
	private PanelQueryFLR panelQueryFLRWaehrung = null;
	private WrapperTextField wtfWaehrung = null;

	private WrapperLabel wlaKurs = null;
	private WrapperNumberField wnfKurs = null;

	private WrapperLabel jLabelProjekt = null;
	private WrapperLabel jLabelBestellnummer = null;
	private WrapperTextField wtfBestellnummer = null;
	private WrapperTextField wtfProjekt = null;
	private WrapperLabel jLabelSonderrabatt = null;
	private WrapperTextField jTextFieldRabattsatz = null;
	// private WrapperComboBox jComboBoxSonderrabatt = null;
	private WrapperLabel wlaLiefertermin = null;
	private WrapperLabel wlaWunschtermin = null;
	private WrapperDateField wdfWunschtermin = null;
	private WrapperLabel jLabelFinaltermin = null;
	private WrapperCheckBox wcbTeillieferung = null;
	private WrapperCheckBox wcbUnverbindlich = null;
	private WrapperCheckBox wcbPoenale = null;
	private WrapperCheckBox wcbRoHs = null;
	private WrapperLabel wlaVerrechenbar = new WrapperLabel();
	private WrapperTextField wtfKostenstelle = null;
	private WrapperTextField wtfKostenstelleBezeichnung = null;

	private WrapperLabel wlaBestelldatum = null;
	private WrapperDateField wdfBestelldatum = null;
	private WrapperLabel jLabelLeihtage = null;
	private WrapperNumberField wnfLeihtage = null;

	private WrapperLabel wlaAngebot = null;
	private WrapperGotoButton wbuAngebot = null;
	private WrapperTextField wtfAngebot = null;

	private WrapperGotoButton wbuRahmenauswahl = null;
	private PanelQueryFLR panelQueryFLRRahmenauswahl = null;

	private WrapperTextField wtfRahmencnr = null;
	private WrapperTextField wtfRahmenbez = null;

	private WrapperLabel wlaAbrufe = null;
	private WrapperTextArea wtaAbrufe = null;

	private WrapperLabel labelKommission = null;
	private WrapperTextField wtfKommission = null;

	private WrapperLabel wlaWiederholungsintervall = null;
	private WrapperComboBox wcoWiederholungsintervall = null;

	private WrapperLabel wlaLauf = null;
	private WrapperDateField wdfLauf = null;

	private WrapperLabel wlaLaufBis = null;
	private WrapperDateField wdfLaufBis = null;

	private GregorianCalendar calendar = null;

	private WrapperLabel wlaErfuellungsgrad = null;
	private WrapperNumberField wnfErfuellungsgrad = null;

	private static final String ACTION_SPECIAL_ANGEBOT_LEEREN = "action_special_angebot_leeren";
	private ImageIcon imageIconLeeren = null;

	private WrapperNumberField wnfDivisor = null;
	private WrapperRadioButton wcbDivisor = null;
	private WrapperRadioButton wcbRest = null;
	private WrapperRadioButton wcbManuellAbruf = null;
	private ButtonGroup jbgAbrufBes = null;
	private WrapperLabel wlaLeer = new WrapperLabel();
	private WrapperLabel wlaLeerAbruf = null;
	private boolean bAbrufbesUndNeu = false;
	private JPanel jPanelAbrufBes = null;

	private WrapperSelectField wsfProjekt = new WrapperSelectField(WrapperSelectField.PROJEKT, getInternalFrame(),
			true);

	private JLabel wlaFreigabe = new JLabel();

	private WrapperLabel wlaVersion;
	private WrapperNumberField wnfVersion;
	private JPanel jPanelVersionBelegdatum;

	private boolean bZusatzfunktionVersandweg = false;
	private static final ImageIcon RESPONSE_ICON = HelperClient.createImageIcon("data_out.png");
	private static final ImageIcon RESPONSE_ICON_DONE = HelperClient.createImageIcon("data_out_green.png");

	private WrapperLabel wlaLaenderart;
	private WrapperComboBox wcoLaenderart;
	private WrapperTimestampField wtsfFinaltermin;
	private WrapperTimestampField wtsfLiefertermin;

	private boolean bSchnellanlage = false;

	public boolean isSchnellanlage() {
		return bSchnellanlage;
	}

	public void setSchnellanlage(boolean bSchnellanlage) {
		this.bSchnellanlage = bSchnellanlage;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI    der default Titel des Panels
	 * @param key           PK des Auftrags
	 * @throws java.lang.Throwable Ausnahme
	 */
	public PanelAuftragKopfdaten(InternalFrame internalFrame, String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameAuftrag) internalFrame;
		tpAuftrag = intFrame.getTabbedPaneAuftrag();

		bZusatzfunktionVersandweg = LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_VERSANDWEG);

		jbInit();
		initPanel();
		initComponents();
	}

	public PanelDialogAuftragArtikelSchnellanlage getAuftragArtikelSchnellanlage(Integer artikelIId,
			AuftragDto auftragDto) throws Throwable {
		panelDialogAuftragArtikelSchnellanlage = new PanelDialogAuftragArtikelSchnellanlage(getInternalFrame(),
				artikelIId);
		return panelDialogAuftragArtikelSchnellanlage;
	}

	private void dialogQueryVertreter2(ActionEvent e) throws Throwable {
		panelQueryFLRVertreter2 = PersonalFilterFactory.getInstance().createPanelFLRPersonal(intFrame, true, false,
				tpAuftrag.getAuftragDto().getPersonalIIdVertreter2());

		new DialogQuery(panelQueryFLRVertreter2);
	}

	private String getIconNameLieferaviso() {
		String iconName = "/com/lp/client/res/data_out.png";
		if (tpAuftrag.getAuftragDto() != null && tpAuftrag.getAuftragDto().getTResponse() != null) {
			iconName = "/com/lp/client/res/data_out_green.png";
		}
		return iconName;
	}

	void jbInit() throws Throwable {

		ParametermandantDto parameterMandant = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
				ParameterFac.PARAMETER_AUFTRAEGE_KOENNEN_VERSTECKT_WERDEN);

		boolean b = (Boolean) parameterMandant.getCWertAsObject();
		if (b == true) {
			createAndSaveAndShowButton("/com/lp/client/res/element_preferences.png",
					LPMain.getTextRespectUISPr("auftrag.verstecken"), ACTION_SPECIAL_VERSTECKEN,
					RechteFac.RECHT_AUFT_AUFTRAG_CUD);
		}

		if (bZusatzfunktionVersandweg) {
			createAndSaveAndShowButton(getIconNameLieferaviso(), LPMain.getTextRespectUISPr("auftrag.bestaetigung"),
					ACTION_SPECIAL_ORDERRESPONSE, KeyStroke.getKeyStroke('L', java.awt.event.InputEvent.CTRL_MASK),
					RechteFac.RECHT_AUFT_AKTIVIEREN);
		}

		jbuSetNull = new WrapperButton();
		jbuSetNull.setActionCommand(ACTION_SPECIAL_ANGEBOT_LEEREN);
		jbuSetNull.addActionListener(this);
		imageIconLeeren = new ImageIcon(getClass().getResource("/com/lp/client/res/leeren.png"));

		jbuSetNull.setIcon(imageIconLeeren);
		jbuSetNull.setMaximumSize(
				new Dimension(Defaults.getInstance().getControlHeight(), Defaults.getInstance().getControlHeight()));
		jbuSetNull.setMinimumSize(
				new Dimension(Defaults.getInstance().getControlHeight(), Defaults.getInstance().getControlHeight()));
		jbuSetNull.setPreferredSize(
				new Dimension(Defaults.getInstance().getControlHeight(), Defaults.getInstance().getControlHeight()));

		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = null;
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF)) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
					PanelBasis.ACTION_STORNIEREN, PanelBasis.ACTION_DISCARD, // btndiscard: 0 den Button am
																				// Panel
					// anbringen
					PanelBasis.ACTION_PRINT, ACTION_SPECIAL_VERSTECKEN, ACTION_SPECIAL_ORDERRESPONSE };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
					PanelBasis.ACTION_STORNIEREN, PanelBasis.ACTION_DISCARD, ACTION_SPECIAL_VERSTECKEN,
					ACTION_SPECIAL_ORDERRESPONSE };
		}

		this.enableToolsPanelButtons(aWhichButtonIUse);

		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);
		Timestamp datCurrentDate = new Timestamp(System.currentTimeMillis());

		wlaAuftragsart = new WrapperLabel(LPMain.getTextRespectUISPr("detail.label.auftragart"));
		HelperClient.setDefaultsToComponent(wlaAuftragsart, 115);

		wcoAuftragsart = new WrapperComboBox();
		wcoAuftragsart.setMandatoryFieldDB(true);
		wcoAuftragsart.setActionCommand(ACTION_SPECIAL_AUFTRAGART_CHANGED);
		wcoAuftragsart.addActionListener(this);

		jLabelBelegdatum = new WrapperLabel(LPMain.getTextRespectUISPr("label.belegdatum"));
		HelperClient.setDefaultsToComponent(jLabelBelegdatum, 90);

		wdfBelegdatum = new WrapperBelegDateField(
				new BelegDatumClient(LPMain.getTheClient().getMandant(), new GregorianCalendar(), false));
		wdfBelegdatum.setDate(datCurrentDate);
		wdfBelegdatum.setMandatoryField(true);
		wdfBelegdatum.addPropertyChangeListener("date", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				wdfLauf.setMinimumValue(wdfBelegdatum.getDate());
			}
		});

		wlaAngebot = new WrapperLabel(LPMain.getTextRespectUISPr("angb.angebot"));

		wlaVersion = new WrapperLabel();
		wlaVersion.setText(LPMain.getTextRespectUISPr("lp.beleg.version"));
		wlaVersion.setHorizontalAlignment(SwingConstants.RIGHT);
		wlaVersion.setVisible(false);
		wnfVersion = new WrapperNumberField();
		wnfVersion.setFractionDigits(0);
		wnfVersion.setActivatable(false);
		wnfVersion.setVisible(false);

		wbuAbbuchungslager = new WrapperButton(LPMain.getTextRespectUISPr("button.abbuchungslager"));
		wbuAbbuchungslager.setToolTipText(LPMain.getTextRespectUISPr("button..hint.abbuchungslager"));
		wbuAbbuchungslager.setActionCommand(ACTION_SPECIAL_ABBUCHUNGSLAGER_FROM_LISTE);
		wbuAbbuchungslager.addActionListener(this);
		wtfAbbuchungslager = new WrapperTextField();
		wtfAbbuchungslager.setActivatable(false);
		wtfAbbuchungslager.setMandatoryField(true);

		wbuRahmenauswahl = new WrapperGotoButton(LPMain.getTextRespectUISPr("button.rahmen"),
				com.lp.util.GotoHelper.GOTO_AUFTRAG_AUSWAHL);
		wbuRahmenauswahl.setActionCommand(ACTION_SPECIAL_RAHMENAUSWAHL);
		wbuRahmenauswahl.addActionListener(this);

		wtfRahmencnr = new WrapperTextField();
		wtfRahmencnr.setActivatable(false);
		wtfRahmenbez = new WrapperTextField();
		wtfRahmenbez.setActivatable(false);

		jButtonKunde = new WrapperGotoKundeMapButton(new IPartnerDto() {
			public PartnerDto getPartnerDto() {
				return tpAuftrag.getKundeAuftragDto() == null ? null : tpAuftrag.getKundeAuftragDto().getPartnerDto();

			}
		});
		jButtonKunde.setActionCommand(ACTION_SPECIAL_KUNDE_AUFTRAG);
		jButtonKunde.setText(LPMain.getTextRespectUISPr("button.kunde"));
		jButtonKunde.addActionListener(this);

		wlaAdresse = new WrapperLabel();
		wlaAdresse.setText(LPMain.getTextRespectUISPr("lp.adresse.kbez"));
		wtfKundeAuftrag = new WrapperTextField();
		wtfKundeAuftrag.setMandatoryField(true);
		wtfKundeAuftrag.setActivatable(false);
		wtfKundeAuftrag.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfKundeAuftragAdresse = new WrapperTextField();
		wtfKundeAuftragAdresse.setActivatable(false);
		wtfKundeAuftragAdresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wlaKundeAuftragAbteilung = new WrapperLabel(LPMain.getTextRespectUISPr("lp.abteilung"));

		wtfKundeAuftragAbteilung = new WrapperTextField();
		wtfKundeAuftragAbteilung.setActivatable(false);
		wtfKundeAuftragAbteilung.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuAnsprechpartner = new WrapperButton();
		wbuAnsprechpartner.setText(LPMain.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartner.setActionCommand(PanelAuftragKopfdaten.ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE);
		wbuAnsprechpartner.addActionListener(this);

		wbuAnsprechpartnerLieferadresse = new WrapperButton();
		wbuAnsprechpartnerLieferadresse.setText(LPMain.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartnerLieferadresse
				.setActionCommand(PanelAuftragKopfdaten.ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERADRESSE);
		wbuAnsprechpartnerLieferadresse.addActionListener(this);

		wtfAnsprechpartner = new WrapperTextField();
		wtfAnsprechpartner.setActivatable(false);
		wtfAnsprechpartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfAnsprechpartnerLieferadresse = new WrapperTextField();
		wtfAnsprechpartnerLieferadresse.setActivatable(false);
		wtfAnsprechpartnerLieferadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuAnsprechpartnerRechungsadresse = new WrapperButton();
		wbuAnsprechpartnerRechungsadresse.setText(LPMain.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartnerRechungsadresse
				.setActionCommand(PanelAuftragKopfdaten.ACTION_SPECIAL_ANSPRECHPARTNER_RECHNUNGSADRESSE);
		wbuAnsprechpartnerRechungsadresse.addActionListener(this);

		wtfAnsprechpartnerRechungsadresse = new WrapperTextField();
		wtfAnsprechpartnerRechungsadresse.setActivatable(false);
		wtfAnsprechpartnerRechungsadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		jButtonVertreter = new WrapperButton();
		jButtonVertreter.setText(LPMain.getTextRespectUISPr("button.vertreter"));
		jButtonVertreter.setActionCommand(ACTION_SPECIAL_VERTRETER_KUNDE);
		jButtonVertreter.addActionListener(this);

		wtfVertreter = new WrapperTextField();
		wtfVertreter.setMandatoryField(true);
		wtfVertreter.setActivatable(false);
		wtfVertreter.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuVertreter2 = new WrapperButton();
		wbuVertreter2.setText(LPMain.getTextRespectUISPr("button.vertreter2"));
		wbuVertreter2.setActionCommand(ACTION_SPECIAL_VERTRETER2);
		wbuVertreter2.addActionListener(this);

		wtfVertreter2 = new WrapperTextField();
		wtfVertreter2.setActivatable(false);
		wtfVertreter2.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		jButtonLieferadresse = new WrapperButton();
		jButtonLieferadresse.setText(LPMain.getTextRespectUISPr("button.lieferadresse"));
		jButtonLieferadresse.setActionCommand(ACTION_SPECIAL_LIEFERADRESSE_AUFTRAG);
		jButtonLieferadresse.addActionListener(this);

		wtfKundeLieferadresse = new WrapperTextField();
		wtfKundeLieferadresse.setMandatoryField(true);
		wtfKundeLieferadresse.setActivatable(false);
		wtfKundeLieferadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		jButtonRechnungsadresse = new WrapperButton();
		jButtonRechnungsadresse.setText(LPMain.getTextRespectUISPr("button.rechnungsadresse"));
		jButtonRechnungsadresse.setActionCommand(ACTION_SPECIAL_RECHNUNGSADRESSE_AUFTRAG);
		jButtonRechnungsadresse.addActionListener(this);

		wtfKundeRechnungsadresse = new WrapperTextField();
		wtfKundeRechnungsadresse.setMandatoryField(true);
		wtfKundeRechnungsadresse.setActivatable(false);
		wtfKundeRechnungsadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuWaehrung = new WrapperButton();
		wbuWaehrung.setActionCommand(ACTION_SPECIAL_WAEHRUNG);
		wbuWaehrung.setText(LPMain.getTextRespectUISPr("button.waehrung"));
		wbuWaehrung.addActionListener(this);

		wtfWaehrung = new WrapperTextField();
		wtfWaehrung.setActivatable(false);
		wtfWaehrung.setMandatoryField(true);
		wtfWaehrung.addPropertyChangeListener(this);

		wlaKurs = new WrapperLabel(LPMain.getTextRespectUISPr("label.kurs"));

		wnfKurs = new WrapperNumberField();
		wnfKurs.setActivatable(false);
		wnfKurs.setMandatoryField(true);
		wnfKurs.setMaximumIntegerDigits(LocaleFac.ANZAHL_VORKOMMASTELLEN_WECHSELKURS);
		wnfKurs.setFractionDigits(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS);

		jLabelProjekt = new WrapperLabel();
		jLabelProjekt.setText(LPMain.getTextRespectUISPr("label.projekt"));

		jLabelBestellnummer = new WrapperLabel();
		jLabelBestellnummer.setText(LPMain.getTextRespectUISPr("label.bestellnummer"));

		wtfBestellnummer = new WrapperTextField(
		/* AuftragFac.MAX_AUFT_AUFTRAG_BESTELLNUMMER */);

		wtfProjekt = new WrapperTextField(AuftragFac.MAX_AUFT_AUFTRAG_PROJEKTBEZEICHNUNG);

		jLabelSonderrabatt = new WrapperLabel();
		jLabelSonderrabatt.setText(LPMain.getTextRespectUISPr("detail.label.sonderrabatt"));

		jTextFieldRabattsatz = new WrapperTextField();
		jTextFieldRabattsatz.setHorizontalAlignment(SwingConstants.RIGHT);

		// jComboBoxSonderrabatt = new WrapperComboBox();

		wlaLiefertermin = new WrapperLabel();
		wlaLiefertermin.setText(LPMain.getTextRespectUISPr("label.liefertermin"));

		jLabelFinaltermin = new WrapperLabel();
		jLabelFinaltermin.setText(LPMain.getTextRespectUISPr("label.finaltermin"));

		wlaWunschtermin = new WrapperLabel();
		wlaWunschtermin.setText(LPMain.getTextRespectUISPr("label.wunschtermin"));
		wdfWunschtermin = new WrapperDateField();

		wtsfFinaltermin = new WrapperTimestampField();
		wtsfFinaltermin.setMandatoryField(true);
		wtsfFinaltermin.setTimestamp(datCurrentDate);

		wtsfLiefertermin = new WrapperTimestampField();
		wtsfLiefertermin.setMandatoryField(true);
		wtsfLiefertermin.setTimestamp(datCurrentDate);
		wtsfLiefertermin.getWdfDatum().getDateEditor().addPropertyChangeListener("date", this);

		wcbTeillieferung = new WrapperCheckBox();
		wcbTeillieferung.setText(LPMain.getTextRespectUISPr("label.teillieferung"));

		wcbUnverbindlich = new WrapperCheckBox();
		wcbUnverbindlich.setText(LPMain.getTextRespectUISPr("detail.label.unverbindlich"));

		wcbPoenale = new WrapperCheckBox();
		wcbPoenale.setText(LPMain.getTextRespectUISPr("detail.label.poenale"));
		wcbRoHs = new WrapperCheckBox();
		wcbRoHs.setText(LPMain.getTextRespectUISPr("detail.label.rohs"));

		wnfLeihtage = new WrapperNumberField(new Long(AuftragFac.MIN_I_LEIHTAGE).intValue(),
				new Long(AuftragFac.MAX_I_LEIHTAGE).intValue());
		wnfLeihtage.setFractionDigits(0);

		wbuKostenstelle = new WrapperButton(LPMain.getTextRespectUISPr("button.kostenstelle"));
		wbuKostenstelle.setMandatoryField(true);
		wbuKostenstelle.addActionListener(this);
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);

		wtfKostenstelle = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
		wtfKostenstelle.setActivatable(false);
		wtfKostenstelle.setMandatoryField(true);
		wtfKostenstelleBezeichnung = new WrapperTextField();
		wtfKostenstelleBezeichnung.setActivatable(false);

		wlaErfuellungsgrad = new WrapperLabel(LPMain.getTextRespectUISPr("auft.kopfdaten.erfuellungsgrad"));
		wnfErfuellungsgrad = new WrapperNumberField();
		wnfErfuellungsgrad.setActivatable(false);
		wnfErfuellungsgrad.setMandatoryField(false);
		wnfErfuellungsgrad.setFractionDigits(0);

		wlaBestelldatum = new WrapperLabel(LPMain.getTextRespectUISPr("lp.bestelldatum"));
		wdfBestelldatum = new WrapperDateField();
		wdfBestelldatum.setDate(datCurrentDate);

		jLabelLeihtage = new WrapperLabel();
		jLabelLeihtage.setText(LPMain.getTextRespectUISPr("detail.label.leihtage"));

		wbuAngebot = new WrapperGotoButton(LPMain.getTextRespectUISPr("anf.angebotnummer"),
				GotoHelper.GOTO_ANGEBOT_AUSWAHL);
		wtfAngebot = new WrapperTextField();
		wtfAngebot.setActivatable(false);

		wlaAbrufe = new WrapperLabel(LPMain.getTextRespectUISPr("bes.abrufe"));
		wtaAbrufe = new WrapperTextArea();
		wtaAbrufe.setActivatable(false);
		wtaAbrufe.setRows(4);

		wlaWiederholungsintervall = new WrapperLabel(LPMain.getTextRespectUISPr("lp.wiederholungsintervall"));
		wlaWiederholungsintervall.setVisible(false);
		wcoWiederholungsintervall = new WrapperComboBox();
		wcoWiederholungsintervall.setVisible(false);
		wcoWiederholungsintervall.setMandatoryFieldDB(true);

		wlaLauf = new WrapperLabel(LPMain.getTextRespectUISPr("lp.ab"));
		wlaLauf.setVisible(false);
		wdfLauf = new WrapperDateField();
		wdfLauf.setVisible(false);

		wlaLaufBis = new WrapperLabel(LPMain.getTextRespectUISPr("lp.bis"));
		wlaLaufBis.setVisible(false);
		wdfLaufBis = new WrapperDateField();
		wdfLaufBis.setVisible(false);

		wcoLaenderart = new WrapperComboBox();
		wcoLaenderart.setMandatoryFieldDB(false);
		wcoLaenderart.setActionCommand(ACTION_SPECIAL_LAENDERART_CHANGED);
		wcoLaenderart.addActionListener(this);
		wlaLaenderart = new WrapperLabel(LPMain.getTextRespectUISPr("label.laenderart"));

		labelKommission = new WrapperLabel();
		labelKommission.setText(LPMain.getTextRespectUISPr("lp.kommission"));
		wtfKommission = new WrapperTextField();

		createAbrufPanel();

		// Workingpanel
		jPanelWorkingOn = new JPanel(
				new MigLayout("wrap 6, hidemode 3", "[fill,20%][fill,15%][fill,15%][fill,15%][fill,20%][fill,15%]"));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jPanelVersionBelegdatum = new JPanel();
		HvLayout layoutVersionBelegdatum = HvLayoutFactory.create(jPanelVersionBelegdatum, "ins 0",
				"push[fill,grow|fill|fill|fill]", "");
		layoutVersionBelegdatum.add(wlaVersion, "push, right, wmin 80").add(wnfVersion, "wmin 40")
				.add(jLabelBelegdatum, "growx").add(wdfBelegdatum);

		// Zeile
		jPanelWorkingOn.add(wlaAuftragsart, "right");

		jPanelWorkingOn.add(wcoAuftragsart, "span 3");

		jPanelWorkingOn.add(jPanelVersionBelegdatum, "gapleft 10, span 2, growx");
		// jPanelWorkingOn.add(jLabelBelegdatum, "right");
		// jPanelWorkingOn.add(wdfBelegdatum, "wrap");

		jPanelWorkingOn.add(jPanelAbrufBes, "span");

		jPanelWorkingOn.add(wbuRahmenauswahl);
		jPanelWorkingOn.add(wtfRahmencnr, "span 3");
		jPanelWorkingOn.add(wtfRahmenbez, "span");

		jPanelWorkingOn.add(jButtonKunde);
		jPanelWorkingOn.add(wtfKundeAuftrag, "span");

		jPanelWorkingOn.add(wlaAdresse);
		jPanelWorkingOn.add(wtfKundeAuftragAdresse, "span");

		jPanelWorkingOn.add(wlaKundeAuftragAbteilung);
		jPanelWorkingOn.add(wtfKundeAuftragAbteilung, "span");

		jPanelWorkingOn.add(wbuAnsprechpartner);
		jPanelWorkingOn.add(wtfAnsprechpartner, "span");

		// Zeile

		ParametermandantDto parametermandantDtoZW = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.PARAMETER_ZWEITER_VERTRETER);
		boolean bZweiterVertreter = ((Boolean) parametermandantDtoZW.getCWertAsObject());

		if (bZweiterVertreter) {
			jPanelWorkingOn.add(jButtonVertreter);
			jPanelWorkingOn.add(wtfVertreter, "span 3");

			jPanelWorkingOn.add(wbuVertreter2);
			jPanelWorkingOn.add(wtfVertreter2, "span");

			wtfVertreter2.setMandatoryField(true);

		} else {
			jPanelWorkingOn.add(jButtonVertreter);
			jPanelWorkingOn.add(wtfVertreter, "span");
		}

		// Zeile
		jPanelWorkingOn.add(jButtonLieferadresse);
		jPanelWorkingOn.add(wtfKundeLieferadresse, "span 3");
		jPanelWorkingOn.add(wbuAnsprechpartnerLieferadresse);
		jPanelWorkingOn.add(wtfAnsprechpartnerLieferadresse);

		// Zeile
		jPanelWorkingOn.add(jButtonRechnungsadresse);
		jPanelWorkingOn.add(wtfKundeRechnungsadresse, "span 3");
		jPanelWorkingOn.add(wbuAnsprechpartnerRechungsadresse);
		jPanelWorkingOn.add(wtfAnsprechpartnerRechungsadresse);
		// Zeile
				jPanelWorkingOn.add(jLabelBestellnummer);
				jPanelWorkingOn.add(wtfBestellnummer, "span 3");

				jPanelWorkingOn.add(wlaAngebot, "split 3, w 330");
				jPanelWorkingOn.add(wbuAngebot.getWrapperButtonGoTo(), "w 250");
				jPanelWorkingOn.add(jbuSetNull);
				jPanelWorkingOn.add(wtfAngebot);
		// Zeile
		jPanelWorkingOn.add(jLabelProjekt);

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {

			ParametermandantDto parametermandantDto = DelegateFactory.getInstance().getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_PROJEKT_IST_PFLICHTFELD);
			boolean bProjektIstPflichtfeld = ((Boolean) parametermandantDto.getCWertAsObject());
			if (bProjektIstPflichtfeld) {
				wsfProjekt.setMandatoryField(true);
			}

			jPanelWorkingOn.add(wtfProjekt, "span 3");
			jPanelWorkingOn.add(wsfProjekt.getWrapperGotoButton());
			jPanelWorkingOn.add(wsfProjekt.getWrapperTextField(), "span");
		} else {
			jPanelWorkingOn.add(wtfProjekt, "span");
		}
		

		// Zeile
		jPanelWorkingOn.add(labelKommission);
		jPanelWorkingOn.add(wtfKommission, "span");

		// Zeile
		jPanelWorkingOn.add(wbuWaehrung);
		jPanelWorkingOn.add(wtfWaehrung, "span 3");
		jPanelWorkingOn.add(wlaKurs);
		jPanelWorkingOn.add(wnfKurs);

		// Zeile
		jPanelWorkingOn.add(wlaBestelldatum);
		jPanelWorkingOn.add(wdfBestelldatum);
		jPanelWorkingOn.add(wbuAbbuchungslager);
		jPanelWorkingOn.add(wtfAbbuchungslager);
		jPanelWorkingOn.add(jLabelLeihtage);
		jPanelWorkingOn.add(wnfLeihtage);

		// Zeile Liefertermin
		JPanel panelLiefertermin = new JPanel();
		panelLiefertermin.setLayout(new MigLayout("ins 0", "fill,220", ""));
		panelLiefertermin.add(wtsfLiefertermin);

		jPanelWorkingOn.add(wlaLiefertermin);
		jPanelWorkingOn.add(panelLiefertermin, "span 2");
		wtsfLiefertermin.getWtfZeit().setVisible(tpAuftrag.getBAuftragterminstudenminuten());
		jPanelWorkingOn.add(wlaErfuellungsgrad, "skip");
		jPanelWorkingOn.add(wnfErfuellungsgrad);

		// Zeile Finaltermin
		JPanel panelFinaltermin = new JPanel();
		panelFinaltermin.setLayout(new MigLayout("ins 0", "fill,220", ""));
		panelFinaltermin.add(wtsfFinaltermin);

		jPanelWorkingOn.add(jLabelFinaltermin);
		jPanelWorkingOn.add(panelFinaltermin, "span 2");
		wtsfFinaltermin.getWtfZeit().setVisible(tpAuftrag.getBAuftragterminstudenminuten());
		jPanelWorkingOn.add(wlaLaenderart, "skip");
		jPanelWorkingOn.add(wcoLaenderart);

		// Zeile Wunschtermin
		jPanelWorkingOn.add(wlaWunschtermin);
		jPanelWorkingOn.add(wdfWunschtermin, "span 2");

		jPanelWorkingOn.add(wcbUnverbindlich, "skip");
		jPanelWorkingOn.add(wcbTeillieferung);

		// Zeile
		jPanelWorkingOn.add(wbuKostenstelle);
		jPanelWorkingOn.add(wtfKostenstelle, "span 2");
		jPanelWorkingOn.add(wlaVersteckt);

		jPanelWorkingOn.add(wcbPoenale);

		jPanelWorkingOn.add(wcbRoHs);

		// Zeile

		jPanelWorkingOn.add(wlaAbrufe);
		jPanelWorkingOn.add(wtaAbrufe, "span");

		jPanelWorkingOn.add(wlaWiederholungsintervall);
		jPanelWorkingOn.add(wcoWiederholungsintervall, "span 2");
		jPanelWorkingOn.add(wlaLauf);
		jPanelWorkingOn.add(wdfLauf, "split 2");
		jPanelWorkingOn.add(wlaLaufBis);
		jPanelWorkingOn.add(wdfLaufBis);

		ParametermandantDto parametermandantDtoVerrechenbar = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.PARAMETER_ZUSATZSTATUS_VERRECHENBAR);
		boolean bVerrechenbar = ((Boolean) parametermandantDtoVerrechenbar.getCWertAsObject());
		if (bVerrechenbar) {
			jPanelWorkingOn.add(wlaVerrechenbar, "skip, span");
		}

		if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ZEITERFASSUNG)) {
			boolean hatRecht = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD);
			if (hatRecht) {

				ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate()
						.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_PERSONAL,
								ParameterFac.PARAMETER_VON_BIS_ERFASSUNG);
				boolean bVonBisErfassung = (java.lang.Boolean) parameter.getCWertAsObject();

				// SP2352
				if (bVonBisErfassung == false) {

					getToolBar().addButtonRight("/com/lp/client/res/gear_run.png",
							LPMain.getTextRespectUISPr("proj.startzeit"), Desktop.MY_OWN_NEW_ZEIT_START, null, null);
					getToolBar().addButtonRight("/com/lp/client/res/gear_stop.png",
							LPMain.getTextRespectUISPr("proj.stopzeit"), Desktop.MY_OWN_NEW_ZEIT_STOP, null, null);

					enableToolsPanelButtons(true, Desktop.MY_OWN_NEW_ZEIT_START, Desktop.MY_OWN_NEW_ZEIT_STOP);
				}

			}
		}

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_AUFTRAGSFREIGABE, ParameterFac.KATEGORIE_AUFTRAG,
						LPMain.getTheClient().getMandant());

		if ((Boolean) parameter.getCWertAsObject() && DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_AUFT_DARF_AUFTRAEGE_FREIGEBEN)) {

			getToolBar().addButtonCenter("/com/lp/client/res/check2.png", LPMain.getTextRespectUISPr("auft.freigeben"),
					MY_OWN_NEW_TOGGLE_FREIGABE, null, RechteFac.RECHT_AUFT_DARF_AUFTRAEGE_FREIGEBEN);

			getToolBar().getToolsPanelCenter().add(wlaFreigabe);
		}

		if ((Boolean) parameter.getCWertAsObject()) {
			getToolBar().addButtonCenter("/com/lp/client/res/wallet_open.png",
					LPMain.getTextRespectUISPr("auft.anzahlungsrechungausauftrag"),
					MY_OWN_NEW_ANZAHLUNGSRECHNUNG_AUS_AUFTRAG, null, RechteFac.RECHT_RECH_RECHNUNG_CUD);

		}

	}

	private void initPanel() throws Throwable {
		wcoAuftragsart.setMap(DelegateFactory.getInstance().getAuftragServiceDelegate()
				.getAuftragart(LPMain.getInstance().getUISprLocale()));
		wcoWiederholungsintervall.setMap(DelegateFactory.getInstance().getAuftragServiceDelegate()
				.getAuftragwiederholungsintervall(LPMain.getInstance().getUISprLocale()));

		wcoLaenderart.setEmptyEntry(LPMain.getTextRespectUISPr("lp.laenderart.automatisch"));
		wcoLaenderart
				.setMap(DelegateFactory.getInstance().getFinanzServiceDelegate().getAllLaenderartenMitUebersetzung());
	}

	private Timestamp getDefaultTimestampLiefertermin() throws Throwable {
		ParametermandantDto parametermandantDto = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.PARAMETER_DEFAULT_LIEFERZEIT_AUFTRAG);
		int defaultLieferzeitAuftrag = ((Integer) parametermandantDto.getCWertAsObject()).intValue();

		calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, defaultLieferzeitAuftrag);
		Timestamp defaultTimestamp = Helper.cutTimestamp(new Timestamp(calendar.getTimeInMillis()));
		return defaultTimestamp;
	}

	public void setDefaults() throws Throwable {
		rahmenauftragDto = new AuftragDto();
		ansprechpartnerDto = new AnsprechpartnerDto();
		ansprechpartnerDtoLieferadresse = new AnsprechpartnerDto();
		ansprechpartnerDtoRechnungsadresse = new AnsprechpartnerDto();
		vertreterDto = new PersonalDto();
		kundeLieferadresseDto = new KundeDto();
		kundeRechnungsadresseDto = new KundeDto();
		waehrungDto = new WaehrungDto();
		kostenstelleDto = new KostenstelleDto();
		// aenderewaehrung: 1 die eventuell hinterlegte urspruengliche Waehrung
		// zuruecksetzen
		waehrungOriDto = new WaehrungDto();

		// alle anzeigefelder zuruecksetzen
		leereAlleFelder(this);

		wlaVersteckt.setText("");
		// alle vorbelegten werte setzen
		wcoAuftragsart.setKeyOfSelectedItem(AuftragServiceFac.AUFTRAGART_FREI);
		Date currentDate = new Date(System.currentTimeMillis());
		// Locale locDefault = LPMain.getInstance().getUISprLocale();

		wdfBelegdatum.setDate(currentDate);
		wdfBestelldatum.setDate(currentDate);

		Timestamp defaultTimestampLiefertermin = getDefaultTimestampLiefertermin();
		wtsfLiefertermin.setTimestamp(defaultTimestampLiefertermin);
		wtsfFinaltermin.setTimestamp(defaultTimestampLiefertermin);
		calendar.add(Calendar.MONTH, 1);
		java.sql.Date laufDate = new java.sql.Date(calendar.getTimeInMillis());
		wdfLauf.setDate(laufDate);
		wdfLauf.setMinimumValue(new java.sql.Date(System.currentTimeMillis()));
		wcoWiederholungsintervall.setKeyOfSelectedItem(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH);

		// default waehrung kommt vom Mandanten
		waehrungDto = DelegateFactory.getInstance().getLocaleDelegate()
				.waehrungFindByPrimaryKey(DelegateFactory.getInstance().getMandantDelegate()
						.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant()).getWaehrungCNr());
		wtfWaehrung.setText(waehrungDto.getCNr());
		setzeWechselkurs();
		wnfLeihtage.setInteger(new Integer(0));
		wcbTeillieferung.setSelected(true);
		// Vertreter aus kunde
		ParametermandantDto parametermandantDto = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_VERTRETER_VORSCHLAG_AUS_KUNDE);
		if (parametermandantDto.getCWert().equals("0")) {
			if (tpAuftrag.getAuftragDto().getIId() == null) {
				vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
						.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());
				if (vertreterDto != null && vertreterDto.getIId() != null) {
					wtfVertreter.setText(vertreterDto.getPartnerDto().formatTitelAnrede());
				}
			}
		}
		wnfDivisor.setInteger(1);

		parametermandantDto = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
				ParameterFac.PARAMETER_DEFAULT_UNVERBINDLICH);
		if (((Boolean) parametermandantDto.getCWertAsObject()) == true) {
			wcbUnverbindlich.setSelected(true);
		}

		wtfAnsprechpartner.resetColorAndTooltip();
		wtfAnsprechpartnerLieferadresse.resetColorAndTooltip();
		wtfAnsprechpartnerRechungsadresse.resetColorAndTooltip();

	}

	public void setDefaultsAusProjekt(Integer projektIId) throws Throwable {
		ProjektDto projektDto = DelegateFactory.getInstance().getProjektDelegate().projektFindByPrimaryKey(projektIId);

		wtfProjekt.setText(projektDto.getCTitel());
		wsfProjekt.setKey(projektDto.getIId());

		vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
				.personalFindByPrimaryKey(projektDto.getPersonalIIdZugewiesener());
		if (vertreterDto != null && vertreterDto.getIId() != null) {
			wtfVertreter.setText(vertreterDto.getPartnerDto().formatTitelAnrede());
		}

		KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByiIdPartnercNrMandantOhneExc(projektDto.getPartnerIId(), LPMain.getTheClient().getMandant());
		if (kundeDto == null) {
			// Dann Kunde zuerst anlegen

			PartnerDto pDto = DelegateFactory.getInstance().getPartnerDelegate()
					.partnerFindByPrimaryKey(projektDto.getPartnerIId());

			boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kunde.auspartner.anlegen.teil1") + pDto.formatFixTitelName1Name2()
							+ LPMain.getTextRespectUISPr("lp.kunde.auspartner.anlegen.teil2"));

			if (b == true) {
				Integer kundeIId = DelegateFactory.getInstance().getKundeDelegate()
						.createKundeAusPartner(projektDto.getPartnerIId());
				kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(kundeIId);

			} else {
				return;
			}

		} else {
			kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(kundeDto.getIId());
		}

		tpAuftrag.setKundeAuftragDto(kundeDto);

		kundenDatenVorbesetzen(kundeDto.getIId());
		if (projektDto.getAnsprechpartnerIId() != null) {
			ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(projektDto.getAnsprechpartnerIId());

			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatTitelAnrede());
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// Source ist mein Kundeauswahldialog; wenn ein neuer Auftragskunde
			// gewaehlt wurde, werden die abhaengigen Felder angepasst
			if (e.getSource() == panelQueryFLRKunde) {
				Integer iIdKunde = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				if (iIdKunde != null) {

					kundenDatenVorbesetzen(iIdKunde);
					if (bSchnellanlage) {
						kundeIIdLetzterSchnellauftrag = iIdKunde;
					}

				}
			} else if (e.getSource() == panelQueryFLRAbbuchungsLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate()
						.lagerFindByPrimaryKey((Integer) key);
				wtfAbbuchungslager.setText(lagerDto.getCNr());
				tpAuftrag.getAuftragDto().setLagerIIdAbbuchungslager(lagerDto.getIId());
			} else if (e.getSource() == panelQueryFLRArtikelFuerSchnellanlage) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getInternalFrame()
						.showPanelDialog(getAuftragArtikelSchnellanlage((Integer) key, tpAuftrag.getAuftragDto()));

			} else

			// Source ist der Ansprechpartnerauswahldialog

			// Source ist der Ansprechpartnerauswahldialog
			if (e.getSource() == panelQueryFLRAnsprechpartner_Lieferadresse) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				bestimmeUndZeigeAnsprechpartnerLieferadresse(iIdAnsprechpartner, kundeLieferadresseDto.getPartnerIId());
			} else

			// Source ist der Ansprechpartnerauswahldialog

			// Source ist der Ansprechpartnerauswahldialog
			if (e.getSource() == panelQueryFLRAnsprechpartner_Rechungsadresse) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				bestimmeUndZeigeAnsprechpartnerRechnungsadresse(iIdAnsprechpartner,
						kundeRechnungsadresseDto.getPartnerIId());
			}

			else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

				wtfAnsprechpartner.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());

				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
						.getParameterDelegate()
						.getParametermandant(ParameterFac.PARAMETER_AUFTRAG_LIEFERADRESSE_ANSP_VORBESETZEN,
								ParameterFac.KATEGORIE_AUFTRAG, LPMain.getTheClient().getMandant());

				int iOption = (Integer) parameter.getCWertAsObject();
				if (iOption == 2) {
					if (kundeLieferadresseDto != null && kundeLieferadresseDto.getIId() != null
							&& tpAuftrag.getKundeAuftragDto() != null) {
						if (kundeLieferadresseDto.getIId().equals(tpAuftrag.getKundeAuftragDto().getIId())) {
							bestimmeUndZeigeAnsprechpartnerLieferadresse(iIdAnsprechpartner,
									kundeLieferadresseDto.getPartnerIId());

						}
					}

				}

				parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
						.getParametermandant(ParameterFac.PARAMETER_AUFTRAG_RECHNUNGSADRESSE_ANSP_VORBESETZEN,
								ParameterFac.KATEGORIE_AUFTRAG, LPMain.getTheClient().getMandant());

				iOption = (Integer) parameter.getCWertAsObject();
				if (iOption == 2) {
					if (kundeRechnungsadresseDto != null && kundeRechnungsadresseDto.getIId() != null
							&& tpAuftrag.getKundeAuftragDto() != null) {
						if (kundeRechnungsadresseDto.getIId().equals(tpAuftrag.getKundeAuftragDto().getIId())) {
							bestimmeUndZeigeAnsprechpartnerRechnungsadresse(iIdAnsprechpartner,
									kundeRechnungsadresseDto.getPartnerIId());

						}
					}

				}

			} else

			// source ist MEIN Vertreterauswahldialog
			if (e.getSource() == panelQueryFLRVertreter) {
				Integer pkPersonal = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				vertreterDto = DelegateFactory.getInstance().getPersonalDelegate().personalFindByPrimaryKey(pkPersonal);
				vertreterDto2components();
			} else if (e.getSource() == panelQueryFLRVertreter2) {
				Integer pkPersonal = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				PersonalDto vertreterDto2 = DelegateFactory.getInstance().getPersonalDelegate()
						.personalFindByPrimaryKey(pkPersonal);

				if (vertreterDto2 != null && vertreterDto2.getIId() != null) {
					wtfVertreter2.setText(vertreterDto2.getPartnerDto().formatTitelAnrede());
				}
				tpAuftrag.getAuftragDto().setPersonalIIdVertreter2(pkPersonal);
			} else

			// source ist MEINE auswahl der lieferadresse
			if (e.getSource() == panelQueryFLRLieferadresse) {
				Integer pkKunde = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				kundeLieferadresseDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(pkKunde);

				kundeLieferadresseDto2components();

				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
						.getParameterDelegate()
						.getParametermandant(ParameterFac.PARAMETER_AUFTRAG_LIEFERADRESSE_ANSP_VORBESETZEN,
								ParameterFac.KATEGORIE_AUFTRAG, LPMain.getTheClient().getMandant());

				int iOption = (Integer) parameter.getCWertAsObject();

				AnsprechpartnerDto anspDtoTemp = null;

				if (iOption == 1 || iOption == 2) {
					anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerFindErstenEinesPartnersOhneExc(
									kundeLieferadresseDto.getPartnerDto().getIId());

				}

				if (anspDtoTemp != null) {
					ansprechpartnerDtoLieferadresse = anspDtoTemp;

					wtfAnsprechpartnerLieferadresse
							.setText(ansprechpartnerDtoLieferadresse.getPartnerDto().formatTitelAnrede());
				} else {
					wtfAnsprechpartnerLieferadresse.setText("");
					ansprechpartnerDtoLieferadresse = new AnsprechpartnerDto();
				}

			} else

			// source ist MEINE auswahl der rechnungsadresse
			if (e.getSource() == panelQueryFLRRechnungsadresse) {
				Integer pkKunde = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				kundeRechnungsadresseDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(pkKunde);

				kundeRechnungsadresseDto2components();

				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
						.getParameterDelegate()
						.getParametermandant(ParameterFac.PARAMETER_AUFTRAG_RECHNUNGSADRESSE_ANSP_VORBESETZEN,
								ParameterFac.KATEGORIE_AUFTRAG, LPMain.getTheClient().getMandant());

				int iOption = (Integer) parameter.getCWertAsObject();

				AnsprechpartnerDto anspDtoTemp = null;

				if (iOption == 1 || iOption == 2) {
					if (iOption == 2 && ansprechpartnerDto != null && ansprechpartnerDto.getIId() != null) {
						anspDtoTemp = ansprechpartnerDto;
					} else {
						anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
								.ansprechpartnerFindErstenEinesPartnersOhneExc(
										kundeRechnungsadresseDto.getPartnerDto().getIId());
					}
				}

				if (anspDtoTemp != null) {
					ansprechpartnerDtoRechnungsadresse = anspDtoTemp;

					wtfAnsprechpartnerRechungsadresse
							.setText(ansprechpartnerDtoRechnungsadresse.getPartnerDto().formatTitelAnrede());
				} else {
					wtfAnsprechpartnerRechungsadresse.setText("");
					ansprechpartnerDtoRechnungsadresse = new AnsprechpartnerDto();
				}

			} else if (e.getSource() == panelQueryFLRWaehrung) {
				Object cNrWaehrung = ((ISourceEvent) e.getSource()).getIdSelected();

				if (cNrWaehrung != null) {
					waehrungDto = DelegateFactory.getInstance().getLocaleDelegate()
							.waehrungFindByPrimaryKey((String) cNrWaehrung);

					wtfWaehrung.setText(waehrungDto.getCNr());

					setzeWechselkurs();
				}
			} else if (e.getSource() == panelQueryFLRKostenstelle) {
				Integer iIdKostenstelle = (Integer) (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
						.kostenstelleFindByPrimaryKey(iIdKostenstelle);
				wtfKostenstelle.setText(kostenstelleDto.formatKostenstellenbezeichnung());
				wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
			} else if (e.getSource() == panelQueryFLRRahmenauswahl) {
				Integer auftragIIdRahmenauftrag = (Integer) (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				if (auftragIIdRahmenauftrag != null) {
					rahmenauftragDto = DelegateFactory.getInstance().getAuftragDelegate()
							.auftragFindByPrimaryKey(auftragIIdRahmenauftrag);
					Integer IId = null;
					if (tpAuftrag.getAuftragDto() != null)
						IId = tpAuftrag.getAuftragDto().getIId();
					tpAuftrag.setAuftragDto(rahmenauftragDto);

					wtfRahmencnr.setText(rahmenauftragDto.getCNr());
					wbuRahmenauswahl.setOKey(rahmenauftragDto.getIId());
					wtfRahmenbez.setText(rahmenauftragDto.getCBezProjektbezeichnung());

					erzeugeAbrufauftrag(IId);
				}
			}
		} else

		// dqeingabeleeren: 1 das Event vom leeren Button behandeln
		if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			// source ist MEIN Ansprechpartnerdialog
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				ansprechpartnerDto = new AnsprechpartnerDto();
				wtfAnsprechpartner.setText("");
			}
			if (e.getSource() == panelQueryFLRAnsprechpartner_Lieferadresse) {
				ansprechpartnerDtoLieferadresse = new AnsprechpartnerDto();
				wtfAnsprechpartnerLieferadresse.setText("");
			}
			if (e.getSource() == panelQueryFLRAnsprechpartner_Rechungsadresse) {
				ansprechpartnerDtoRechnungsadresse = new AnsprechpartnerDto();
				wtfAnsprechpartnerRechungsadresse.setText("");
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == panelDialogAuftragArtikelSchnellanlage) {

				if (tpAuftrag.getAuftragDto().getIId() == null) {
					Integer auftragIId = DelegateFactory.getInstance().getAuftragDelegate()
							.createAuftrag(tpAuftrag.getAuftragDto());

					tpAuftrag.setAuftragDto(
							DelegateFactory.getInstance().getAuftragDelegate().auftragFindByPrimaryKey(auftragIId));

				}

				// ANLEGEN
				Integer auftragIId = DelegateFactory.getInstance().getAuftragDelegate()
						.erzeugeAuftragpositionUeberSchnellanlage(tpAuftrag.getAuftragDto().getIId(),
								panelDialogAuftragArtikelSchnellanlage.getArtikelDto(),
								panelDialogAuftragArtikelSchnellanlage.getPaneldatenDtos());
				tpAuftrag.setAuftragDto(
						DelegateFactory.getInstance().getAuftragDelegate().auftragFindByPrimaryKey(auftragIId));
				setKeyWhenDetailPanel(auftragIId);
				super.eventActionSave(null, true);
				eventYouAreSelected(false);

				if (panelDialogAuftragArtikelSchnellanlage.getPacklisteDrucken()) {

					KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByPrimaryKey(tpAuftrag.getAuftragDto().getKundeIIdAuftragsadresse());

					PanelReportKriterien krit = null;

					// DruckPanel instantiieren
					krit = new PanelReportKriterien(getInternalFrame(),
							new ReportAuftragPackliste(getInternalFrame(), auftragIId, ""), "", kdDto.getPartnerDto(),
							tpAuftrag.getAuftragDto().getAnsprechparnterIId(), false, false, false);
					// jetzt das tatsaechliche Drucken
					krit.druckeArchiviereUndSetzeVersandstatusEinesBelegs();

				}

				bSchnellanlage = false;
			}
		}

	}

	protected void kundenDatenVorbesetzen(Integer iIdKunde) throws ExceptionLP, Throwable {
		DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(iIdKunde, LocaleFac.BELEGART_AUFTRAG,
				getInternalFrame());

		// aenderewaehrung: 2 die urspruengliche Waehrung
		// hinterlegen, wenn es eine gibt
		if (tpAuftrag.getAuftragDto().getCAuftragswaehrung() != null) {
			waehrungOriDto = DelegateFactory.getInstance().getLocaleDelegate()
					.waehrungFindByPrimaryKey(tpAuftrag.getAuftragDto().getCAuftragswaehrung());
		}

		// aenderewaehrung: 3 den gewaehlten Kunden uebernehmen
		tpAuftrag.setKundeAuftragDto(DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(iIdKunde));

		LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate()
				.lagerFindByPrimaryKey(tpAuftrag.getKundeAuftragDto().getLagerIIdAbbuchungslager());
		wtfAbbuchungslager.setText(lagerDto.getCNr());
		tpAuftrag.getAuftragDto().setLagerIIdAbbuchungslager(lagerDto.getIId());

		tpAuftrag.getAuftragDto().setBMindermengenzuschlag(tpAuftrag.getKundeAuftragDto().getBMindermengenzuschlag());

		kundeAuftragDto2Components();

		// aenderewaehrung: 4 den Benutzer fragen, ob er die
		// urspruengliche Waehrung beibehalten moechte
		if (waehrungOriDto.getCNr() != null && !waehrungOriDto.getCNr().equals(wtfWaehrung.getText())) {
			int indexWaehrungOriCNr = 0;
			int indexWaehrungNeuCNr = 1;
			int iAnzahlOptionen = 2;

			Object[] aOptionen = new Object[iAnzahlOptionen];
			aOptionen[indexWaehrungOriCNr] = waehrungOriDto.getCNr();
			aOptionen[indexWaehrungNeuCNr] = wtfWaehrung.getText();

			int iAuswahl = DialogFactory.showModalDialog(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.waehrungunterschiedlich"), LPMain.getTextRespectUISPr("lp.frage"),
					aOptionen, aOptionen[0]);

			if (iAuswahl == indexWaehrungOriCNr) {
				// die Belegwaehrung wird beibehalten -> Waehrung
				// und Wechselkurs zuruecksetzen
				waehrungDto = waehrungOriDto;
				wtfWaehrung.setText(waehrungDto.getCNr());
				setzeWechselkurs();
				waehrungOriDto = null;
			}
		}

		// den Ansprechpartner beim Kunden zuruecksetzen
		ansprechpartnerDto = new AnsprechpartnerDto();

		AnsprechpartnerDto anspDtoTemp = null;
		// Ansprechpartner vorbesetzen?
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_AUFTRAG_ANSP_VORBESETZEN, ParameterFac.KATEGORIE_AUFTRAG,
						LPMain.getTheClient().getMandant());
		if ((Boolean) parameter.getCWertAsObject()) {
			anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindErstenEinesPartnersOhneExc(tpAuftrag.getKundeAuftragDto().getPartnerIId());
		}
		if (anspDtoTemp != null) {
			ansprechpartnerDto = anspDtoTemp;

			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatTitelAnrede());
		} else {
			wtfAnsprechpartner.setText("");
		}

		// Vertreter aus kunde
		ParametermandantDto parametermandantDto = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_VERTRETER_VORSCHLAG_AUS_KUNDE);
		if (parametermandantDto.getCWert().equals("1")) {
			// es kann einen default vertreter beim kunden geben
			Integer iIdPersonal = tpAuftrag.getKundeAuftragDto().getPersonaliIdProvisionsempfaenger();
			if (iIdPersonal != null) {

				// PJ20668
				boolean b = DelegateFactory.getInstance().getPersonalDelegate()
						.istPersonalVerstecktOderAusgetreten(iIdPersonal);
				if (b == true) {
					vertreterDto = new PersonalDto();
					wtfVertreter.setText("");
					;
				} else {
					vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey(iIdPersonal);
					vertreterDto2components();
				}

			} else {
				vertreterDto = new PersonalDto();
				wtfVertreter.setText("");
			}
		}

		Integer partnerIid = null;

		ParametermandantDto parametermandantAdressvorbelegungDto = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
						ParameterFac.PARAMETER_ADRESSVORBELEGUNG);

		int iAdressvorbelegung = (Integer) parametermandantAdressvorbelegungDto.getCWertAsObject();

		if (iAdressvorbelegung == 0) {

			// PJ 17644 Rechnungsadresse aus Haeufigkeit holen
			ArrayList<KundeDto> listKundenRechnungsadresse = DelegateFactory.getInstance().getAuftragDelegate()
					.getRechnungsadressenEinerAuftragsadresseSortiertNachHaeufigkeit(
							tpAuftrag.getKundeAuftragDto().getIId());
			if (listKundenRechnungsadresse.size() == 0) {
				partnerIid = tpAuftrag.getKundeAuftragDto().getPartnerIIdRechnungsadresse();
			} else if (listKundenRechnungsadresse.size() == 1) {
				partnerIid = listKundenRechnungsadresse.get(0).getPartnerIId();
			} else if (listKundenRechnungsadresse.size() > 1) {
				// Dialog anzeigen
				DialogRechnungsLieferadresse d = new DialogRechnungsLieferadresse(listKundenRechnungsadresse,
						LPMain.getTextRespectUISPr("auft.rechnungsadresse.haeufigkeit.auswaehlen"));
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);

				d.setVisible(true);
				if (d.kundeDto != null) {
					partnerIid = d.kundeDto.getPartnerIId();
				}
			}

			if (partnerIid == null) {
				partnerIid = tpAuftrag.getKundeAuftragDto().getPartnerIIdRechnungsadresse();
			}
		} else {
			partnerIid = tpAuftrag.getKundeAuftragDto().getPartnerIIdRechnungsadresse();
		}

		// Der Kunde hat eine andere Rechnungadresse

		if (partnerIid != null) {
			kundeRechnungsadresseDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByiIdPartnercNrMandantOhneExc(partnerIid, LPMain.getTheClient().getMandant());
			if (kundeRechnungsadresseDto != null) {
				kundeRechnungsadresseDto.setPartnerDto(
						DelegateFactory.getInstance().getPartnerDelegate().partnerFindByPrimaryKey(partnerIid));
			} else {
				kundeRechnungsadresseDto = tpAuftrag.getKundeAuftragDto();
			}

		} else {
			kundeRechnungsadresseDto = tpAuftrag.getKundeAuftragDto();
		}

		kundeRechnungsadresseDto2components();

		// PJ17869
		ansprechpartnerDtoRechnungsadresse = new AnsprechpartnerDto();

		anspDtoTemp = null;
		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_AUFTRAG_RECHNUNGSADRESSE_ANSP_VORBESETZEN, ParameterFac.KATEGORIE_AUFTRAG,
				LPMain.getTheClient().getMandant());

		int iOption = (Integer) parameter.getCWertAsObject();
		if (iOption == 1) {
			anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindErstenEinesPartnersOhneExc(kundeRechnungsadresseDto.getPartnerDto().getIId());
		} else if (iOption == 2) {

			// Wenn Auftrags/Rechnungsadresse ungleich, dann ersten
			// vorbesetzen
			if (kundeRechnungsadresseDto.getIId().equals(tpAuftrag.getKundeAuftragDto().getIId())) {
				anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindErstenEinesPartnersOhneExc(tpAuftrag.getKundeAuftragDto().getPartnerIId());
			} else {
				anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindErstenEinesPartnersOhneExc(
								kundeRechnungsadresseDto.getPartnerDto().getIId());
			}

		}
		if (anspDtoTemp != null) {
			ansprechpartnerDtoRechnungsadresse = anspDtoTemp;

			wtfAnsprechpartnerRechungsadresse
					.setText(ansprechpartnerDtoRechnungsadresse.getPartnerDto().formatTitelAnrede());
		} else {
			wtfAnsprechpartnerRechungsadresse.setText("");
		}

		// Lieferadresse

		if (iAdressvorbelegung == 0 || iAdressvorbelegung == 2) {
			// PJ 17644 Rechnungsadresse aus Haeufigkeit holen
			ArrayList<KundeDto> listKundenLieferadresse = DelegateFactory.getInstance().getAuftragDelegate()
					.getLieferadressenEinerRechnungsadresseSortiertNachHaeufigkeit(kundeRechnungsadresseDto.getIId());

			kundeLieferadresseDto = tpAuftrag.getKundeAuftragDto();
			if (listKundenLieferadresse.size() == 1) {
				kundeLieferadresseDto = listKundenLieferadresse.get(0);
			} else if (listKundenLieferadresse.size() > 1) {
				// Dialog anzeigen
				DialogRechnungsLieferadresse d = new DialogRechnungsLieferadresse(listKundenLieferadresse,
						LPMain.getTextRespectUISPr("auft.lieferadresse.haeufigkeit.auswaehlen"));
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);

				d.setVisible(true);
				if (d.kundeDto != null) {
					kundeLieferadresseDto = d.kundeDto;
				}

			}
		} else {
			kundeLieferadresseDto = tpAuftrag.getKundeAuftragDto();
		}
		if (kundeLieferadresseDto == null) {
			kundeLieferadresseDto = tpAuftrag.getKundeAuftragDto();
		}
		kundeLieferadresseDto2components();

		// den Ansprechpartner beim Kunden zuruecksetzen
		ansprechpartnerDtoLieferadresse = new AnsprechpartnerDto();

		anspDtoTemp = null;

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_AUFTRAG_LIEFERADRESSE_ANSP_VORBESETZEN, ParameterFac.KATEGORIE_AUFTRAG,
				LPMain.getTheClient().getMandant());

		iOption = (Integer) parameter.getCWertAsObject();
		if (iOption == 1) {
			anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindErstenEinesPartnersOhneExc(kundeLieferadresseDto.getPartnerDto().getIId());
		} else if (iOption == 2) {

			// Wenn Auftrags/Lieferadresse ungleich, dann ersten
			// vorbesetzen
			if (kundeLieferadresseDto.getIId().equals(tpAuftrag.getKundeAuftragDto().getIId())) {
				anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindErstenEinesPartnersOhneExc(tpAuftrag.getKundeAuftragDto().getPartnerIId());
			} else {
				anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindErstenEinesPartnersOhneExc(kundeLieferadresseDto.getPartnerDto().getIId());
			}

		}
		if (anspDtoTemp != null) {
			ansprechpartnerDtoLieferadresse = anspDtoTemp;

			wtfAnsprechpartnerLieferadresse
					.setText(ansprechpartnerDtoLieferadresse.getPartnerDto().formatTitelAnrede());
		} else {
			wtfAnsprechpartnerLieferadresse.setText("");
		}
	}

	/**
	 * Einen Abrufauftrag als Clone eines Rahmenauftrags erzeugen und anzeigen.
	 * 
	 * @throws Throwable Ausnahme
	 */
	private void erzeugeAbrufauftrag(Integer iId) throws Throwable {
		rahmenauftragDto = tpAuftrag.getAuftragDto();

		AuftragDto abrufauftragDto = (AuftragDto) rahmenauftragDto.clone();
		abrufauftragDto.setIId(iId);
		abrufauftragDto.setAuftragartCNr(AuftragServiceFac.AUFTRAGART_ABRUF);
		abrufauftragDto.setAuftragIIdRahmenauftrag(rahmenauftragDto.getIId());
		abrufauftragDto.setPersonalIIdAnlegen(LPMain.getTheClient().getIDPersonal());
		abrufauftragDto.setPersonalIIdAendern(LPMain.getTheClient().getIDPersonal());
		abrufauftragDto.setTAendern(new Timestamp(System.currentTimeMillis()));
		abrufauftragDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
		abrufauftragDto.setDLiefertermin(getDefaultTimestampLiefertermin());

		Date currentDate = new Date(System.currentTimeMillis());
		abrufauftragDto.setDBestelldatum(new Timestamp(currentDate.getTime()));

		tpAuftrag.setAuftragDto(abrufauftragDto);
		tpAuftrag.setKundeAuftragDto(DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(abrufauftragDto.getKundeIIdAuftragsadresse()));

		// den Clone anzeigen; Vorsicht: dieses Dto ist vorerst temporaer
		dto2Components();

		// es muss der aktuelle Wechselkurs von heute verwendet werden
		setzeWechselkurs();
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		// hier landen wir, wenn aus einem Rahmenauftrag ein Abrufauftrag
		// erzeugt werden soll
		if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			erzeugeAbrufauftrag(null);

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					LPMain.getTextRespectUISPr("auft.title.neuerauftrag"));
		} else {
			tpAuftrag.resetDtos();
			tpAuftrag.setTitleAuftrag(LPMain.getTextRespectUISPr("auft.title.panel.kopfdaten"));
			setDefaults();
		}

		super.eventActionNew(eventObject, true, false);

		clearStatusbar();

		enableComponentsInAbhaengigkeitZuStatusUndAnzahlMengenbehafteterPositionen();

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		if (tpAuftrag.aktualisiereAuftragstatus()) {
			super.eventActionUpdate(aE, false);

			enableComponentsInAbhaengigkeitZuStatusUndAnzahlMengenbehafteterPositionen();

			// SP2202 Wenn Auftrag aus Angebot, dann Projekt hier nicht mehr
			// auswaehlbar
			if (tpAuftrag.getAuftragDto().getAngebotIId() != null) {
				wsfProjekt.getWrapperGotoButton().setEnabled(false);

			}

			// PJ20384
			LieferscheinDto[] lsDtos = DelegateFactory.getInstance().getLsDelegate()
					.lieferscheinFindByAuftrag(tpAuftrag.getAuftragDto().getIId());
			if (lsDtos != null && lsDtos.length > 0) {
				wtsfLiefertermin.setEnabled(false);
			}

			jbuSetNull.setEnabled(false);
			wbuAngebot.getWrapperButtonGoTo().setEnabled(false);

		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_AUFTRAG;
	}

	/**
	 * Einen Auftrag auf storniert setzen. <br>
	 * Auftraege koennen nicht physikalisch geloescht werden.
	 * 
	 * @param e                     ActionEvent
	 * @param bAdministrateLockKeyI boolean
	 * @param bNeedNoDeleteI        boolean
	 * @throws Throwable Ausnahme
	 */
	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (!isLockedDlg()) {
			if (tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
					|| tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {

			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("auft.storno.statusnichtoffen"));
				return;
			}

			DelegateFactory.getInstance().getAuftragDelegate().storniereAuftrag(tpAuftrag.getAuftragDto());
			super.eventActionDelete(e, false, false); // der auftrag existiert
			// weiterhin!
			eventYouAreSelected(false);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		boolean bUpdate = false;
		if (allMandatoryFieldsSetDlg()) {

			// PJ20174

			if (tpAuftrag.getAuftragDto().getIId() == null && LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {

				ParametermandantDto parameterMandant = DelegateFactory.getInstance().getParameterDelegate()
						.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
								ParameterFac.PARAMETER_WARNUNG_WENN_KEIN_PROJEKT_ANGEGEBEN);

				Boolean bWarnung = (Boolean) parameterMandant.getCWertAsObject();

				if (bWarnung == true && wsfProjekt.getIKey() == null) {
					boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getTextRespectUISPr("auft.warnung.keinprojektzugeordnet"));

					if (b == false) {
						return;
					}
				}
			}

			components2auftragDto();

			if (Helper.cutTimestamp(tpAuftrag.getAuftragDto().getDFinaltermin())
					.before(Helper.cutTimestamp(tpAuftrag.getAuftragDto().getTBelegdatum()))) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"),
						LPMain.getTextRespectUISPr("auft.hint.finalterminvorbelegtermin"));
			}
			if (Helper.cutTimestamp(tpAuftrag.getAuftragDto().getDLiefertermin())
					.before(Helper.cutTimestamp(tpAuftrag.getAuftragDto().getTBelegdatum()))) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"),
						LPMain.getTextRespectUISPr("auft.hint.lieferterminvorbelegtermin"));
			}

			// pruefen, obs schon AB's mit dieser Bestellnummer gibt
			if (tpAuftrag.getAuftragDto().getCBestellnummer() != null
					&& tpAuftrag.getAuftragDto().getCBestellnummer().trim().length() > 0) {
				AuftragDto[] dtos = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByMandantCNrKundeIIdBestellnummerOhneExc(
								tpAuftrag.getAuftragDto().getKundeIIdAuftragsadresse(),
								tpAuftrag.getAuftragDto().getMandantCNr(),
								tpAuftrag.getAuftragDto().getCBestellnummer());
				if (dtos != null && dtos.length > 0) {
					// wenn es die AB ist, die ich grad bearbeite
					if (tpAuftrag.getAuftragDto().getIId() != null && dtos.length == 1
							&& dtos[0].getIId().equals(tpAuftrag.getAuftragDto().getIId())) {
						// nothing here
					} else {
						StringBuffer sb = new StringBuffer();
						sb.append(LPMain.getTextRespectUISPr("auft.error.bestellnummer_doppelt"));
						for (int i = 0; i < dtos.length && i < 20; i++) {
							// auch hier die aktuelle nicht anzeigen
							if (tpAuftrag.getAuftragDto().getIId() != null
									&& dtos[i].getIId().equals(tpAuftrag.getAuftragDto().getIId())) {
								// nothing here
							} else {
								sb.append("\n");
								sb.append(dtos[i].getCNr() + "   " + dtos[i].getCBestellnummer());
							}
						}
						sb.append("\n");
						sb.append(LPMain.getTextRespectUISPr("lp.frage.trotzdemspeichern"));
						boolean answer = (DialogFactory.showMeldung(sb.toString(),
								LPMain.getTextRespectUISPr("lp.frage"),
								javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
						if (!answer) {
							return;
						}
					}
				}
			}

			DelegateFactory.getInstance().getKundeDelegate()
					.pruefeKreditlimit(tpAuftrag.getAuftragDto().getKundeIIdRechnungsadresse(), getInternalFrame());

			AuftragdokumentDto[] alleAuftragdokumentDtos = DelegateFactory.getInstance().getAuftragServiceDelegate()
					.auftragdokumentFindByBVersteckt();

			ArrayList<AuftragdokumentDto> alAuftragsdokumenteZuSpeichern = new ArrayList<AuftragdokumentDto>();

			if (alleAuftragdokumentDtos != null && alleAuftragdokumentDtos.length > 0) {

				Integer auftragIIdDokumente = tpAuftrag.getAuftragDto().getIId();

				if (wcoAuftragsart.getKeyOfSelectedItem().equals(AuftragServiceFac.AUFTRAGART_ABRUF)
						&& tpAuftrag.getAuftragDto().getIId() == null) {
					auftragIIdDokumente = tpAuftrag.getAuftragDto().getAuftragIIdRahmenauftrag();
				}

				DialogAuftragdokumente d = new DialogAuftragdokumente(auftragIIdDokumente);
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);

				if (d.getAuftragdokumentDto() == null) {
					return;
				}

				alAuftragsdokumenteZuSpeichern = d.getAuftragdokumentDto();

			}

			// PJ19449
			ParametermandantDto parameterMandant = DelegateFactory.getInstance().getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_ETIKETTENDRUCK_NACH_SCHNELLANLAGE);

			int iSchnellanlage = (Integer) parameterMandant.getCWertAsObject();

			if (tpAuftrag.getAuftragDto().getIId() == null) {

				// vorschlagswerte aus dem kunden fuer konditionen vorbelegen,
				// wenn es sich um keinen Abrufauftrag handelt
				if (!((String) wcoAuftragsart.getKeyOfSelectedItem()).equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
					initKonditionen();
				}
				// belegartkonditionen: 0 Kopf- und Fusstext werden nicht
				// vorbelegt, damit
				// man spaeter erkennen kann, ob die Konditionen abgespeichert
				// wurden

				if (bSchnellanlage == true && (iSchnellanlage == 0 || iSchnellanlage == 2)) {

					// PJ18897
					if (wtfProjekt.getText() == null && wtfBestellnummer.getText() == null) {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
								LPMain.getTextRespectUISPr("auft.schnellanlage.pflichtfelder.ausfuellen"));
						return;
					}

					// Speichern

					Integer iIdAuftrag = DelegateFactory.getInstance().getAuftragDelegate()
							.createAuftrag(tpAuftrag.getAuftragDto());
					tpAuftrag.setAuftragDto(
							DelegateFactory.getInstance().getAuftragDelegate().auftragFindByPrimaryKey(iIdAuftrag));

					if (iSchnellanlage == 2) {
						// Etikett Drucken

						KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
								.kundeFindByPrimaryKey(tpAuftrag.getAuftragDto().getKundeIIdAuftragsadresse());

						PanelReportKriterien krit = new PanelReportKriterien(getInternalFrame(),
								new ReportAuftragsetikett(getInternalFrame(), tpAuftrag.getAuftragDto().getIId(), ""),
								"", kdDto.getPartnerDto(), tpAuftrag.getAuftragDto().getAnsprechparnterIId(), false,
								false, false); // jetzt das
												// tatsaechliche
												// Drucken
						krit.druckeArchiviereUndSetzeVersandstatusEinesBelegs();
					}

					// Nun noch Musterartikel abfragen

					String[] aWhichButtonIUse = null;

					FilterKriterium[] kriterien = new FilterKriterium[3];

					if (LPMain.getInstance().getDesktop()
							.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {
						kriterien[0] = new FilterKriterium("artikelliste.mandant_c_nr", true,
								"'" + DelegateFactory.getInstance().getSystemDelegate().getHauptmandant() + "'",
								FilterKriterium.OPERATOR_EQUAL, false);
					} else {
						kriterien[0] = new FilterKriterium("artikelliste.mandant_c_nr", true,
								"'" + LPMain.getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL, false);

					}

					ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
							.getParameterDelegate()
							.getParametermandant(ParameterFac.PARAMETER_TRENNZEICHEN_ARTIKELGRUPPE_AUFTRAGSNUMMER,
									ParameterFac.KATEGORIE_AUFTRAG, LPMain.getTheClient().getMandant());

					String trennzeichen = (String) parameter.getCWertAsObject();

					kriterien[1] = new FilterKriterium("artikelliste.c_nr", true, "'%" + trennzeichen + "MUSTER" + "'",
							FilterKriterium.OPERATOR_LIKE, true, false);
					kriterien[2] = new FilterKriterium("ag.i_id", true, "",
							FilterKriterium.OPERATOR_IS + " " + FilterKriterium.OPERATOR_NOT_NULL, true, false);

					panelQueryFLRArtikelFuerSchnellanlage = new PanelQueryFLR(null, kriterien,
							QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse, getInternalFrame(),
							LPMain.getTextRespectUISPr("title.artikelauswahlliste"),
							ArtikelFilterFactory.getInstance().createFKVArtikel(), null);

					panelQueryFLRArtikelFuerSchnellanlage.befuellePanelFilterkriterienDirekt(
							ArtikelFilterFactory.getInstance().createFKDArtikelnummer(getInternalFrame()),
							ArtikelFilterFactory.getInstance().createFKDVolltextsuche());
					panelQueryFLRArtikelFuerSchnellanlage
							.addDirektFilter(ArtikelFilterFactory.getInstance().createFKDLieferantennrBezeichnung());

					panelQueryFLRArtikelFuerSchnellanlage.setFilterComboBox(
							DelegateFactory.getInstance().getArtikelDelegate().getAllSprArtgru(),
							new FilterKriterium("ag.i_id", true, "" + "", FilterKriterium.OPERATOR_IN, false), false,
							LPMain.getTextRespectUISPr("lp.alle"), false);

					panelQueryFLRArtikelFuerSchnellanlage
							.addDirektFilter(ArtikelFilterFactory.getInstance().createFKDAKAG());

					new DialogQuery(panelQueryFLRArtikelFuerSchnellanlage);

					return;

				} else {

					Integer iIdAuftrag = DelegateFactory.getInstance().getAuftragDelegate()
							.createAuftrag(tpAuftrag.getAuftragDto());
					tpAuftrag.setAuftragDto(
							DelegateFactory.getInstance().getAuftragDelegate().auftragFindByPrimaryKey(iIdAuftrag));
					setKeyWhenDetailPanel(iIdAuftrag);
				}

			} else {
				bUpdate = true;

				if (bUpdate) {

					// SP1141
					AuftragDto aDtoVorhanden = DelegateFactory.getInstance().getAuftragDelegate()
							.auftragFindByPrimaryKey(tpAuftrag.getAuftragDto().getIId());
					if (!aDtoVorhanden.getKundeIIdAuftragsadresse()
							.equals(tpAuftrag.getAuftragDto().getKundeIIdAuftragsadresse())) {

						DialogGeaenderteKonditionenVK dialog = new DialogGeaenderteKonditionenVK(
								tpAuftrag.getAuftragDto(), tpAuftrag.getAuftragDto().getKundeIIdAuftragsadresse(),
								tpAuftrag.getAuftragDto().getKundeIIdLieferadresse(), getInternalFrame());
						LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(dialog);

						if (dialog.bKonditionenUnterschiedlich == true) {
							dialog.setVisible(true);

							if (dialog.bAbgebrochen == false) {
								tpAuftrag.setAuftragDto((AuftragDto) dialog.getBelegVerkaufDto());
							} else {
								bUpdate = false;
								return;
							}
						}

					}

					if (bUpdate == true) {
						// vorschlagswerte aus dem kunden fuer konditionen
						// vorbelegen, wenn es sich um keinen Abrufauftrag
						// handelt

						// Wegen PJ 856 auskommentiert
						// if (!((String) wcoAuftragsart.getKeyOfSelectedItem())
						// .equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
						// initKonditionen();
						// }
						// aenderewaehrung: 5 Wenn die Waehrung geaendert wurde,
						// muessen die Belegwerte neu berechnet werden

						// PJ18737

						if (bSchnellanlage == true) {

							// PJ18897
							if (wtfProjekt.getText() == null && wtfBestellnummer.getText() == null) {
								DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
										LPMain.getTextRespectUISPr("auft.schnellanlage.pflichtfelder.ausfuellen"));
								return;
							}

							// Projekt/Bestelnummer speichern
							DelegateFactory.getInstance().getAuftragDelegate().updateAuftrag(tpAuftrag.getAuftragDto(),
									null);

							// Nun noch Musterartikel abfragen

							String[] aWhichButtonIUse = null;

							FilterKriterium[] kriterien = new FilterKriterium[3];

							if (LPMain.getInstance().getDesktop().darfAnwenderAufZusatzfunktionZugreifen(
									MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {
								kriterien[0] = new FilterKriterium("artikelliste.mandant_c_nr", true,
										"'" + DelegateFactory.getInstance().getSystemDelegate().getHauptmandant() + "'",
										FilterKriterium.OPERATOR_EQUAL, false);
							} else {
								kriterien[0] = new FilterKriterium("artikelliste.mandant_c_nr", true,
										"'" + LPMain.getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL,
										false);

							}

							ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
									.getParameterDelegate().getParametermandant(
											ParameterFac.PARAMETER_TRENNZEICHEN_ARTIKELGRUPPE_AUFTRAGSNUMMER,
											ParameterFac.KATEGORIE_AUFTRAG, LPMain.getTheClient().getMandant());

							String trennzeichen = (String) parameter.getCWertAsObject();

							kriterien[1] = new FilterKriterium("artikelliste.c_nr", true,
									"'%" + trennzeichen + "MUSTER" + "'", FilterKriterium.OPERATOR_LIKE, true, false);
							kriterien[2] = new FilterKriterium("ag.i_id", true, "",
									FilterKriterium.OPERATOR_IS + " " + FilterKriterium.OPERATOR_NOT_NULL, true, false);

							panelQueryFLRArtikelFuerSchnellanlage = new PanelQueryFLR(null, kriterien,
									QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse, getInternalFrame(),
									LPMain.getTextRespectUISPr("title.artikelauswahlliste"),
									ArtikelFilterFactory.getInstance().createFKVArtikel(), null);

							panelQueryFLRArtikelFuerSchnellanlage.befuellePanelFilterkriterienDirekt(
									ArtikelFilterFactory.getInstance().createFKDArtikelnummer(getInternalFrame()),
									ArtikelFilterFactory.getInstance().createFKDVolltextsuche());
							panelQueryFLRArtikelFuerSchnellanlage.addDirektFilter(
									ArtikelFilterFactory.getInstance().createFKDLieferantennrBezeichnung());

							panelQueryFLRArtikelFuerSchnellanlage.setFilterComboBox(
									DelegateFactory.getInstance().getArtikelDelegate().getAllSprArtgru(),
									new FilterKriterium("ag.i_id", true, "" + "", FilterKriterium.OPERATOR_IN, false),
									false, LPMain.getTextRespectUISPr("lp.alle"), false);

							panelQueryFLRArtikelFuerSchnellanlage
									.addDirektFilter(ArtikelFilterFactory.getInstance().createFKDAKAG());

							new DialogQuery(panelQueryFLRArtikelFuerSchnellanlage);

							return;

						}

						boolean bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = DelegateFactory.getInstance()
								.getAuftragDelegate().updateAuftrag(tpAuftrag.getAuftragDto(),
										waehrungOriDto == null ? null : waehrungOriDto.getCNr());

						if (bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben == true) {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("lp.error.mwstvonnullgeaendertundhandeingaben"));

						}

						setKeyWhenDetailPanel(tpAuftrag.getAuftragDto().getIId());
					}
				}
			}

			DelegateFactory.getInstance().getAuftragServiceDelegate()
					.updateAuftragdokumente(tpAuftrag.getAuftragDto().getIId(), alAuftragsdokumenteZuSpeichern);

			super.eventActionSave(e, true);
			// Wenn eine neue Abrufauftrag angelegt wurde, schalten wir auf die
			// "Sicht Rahmen" um.
			if (!bUpdate) {
				if (wcoAuftragsart.getKeyOfSelectedItem().equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
					if (wcbDivisor.isSelected() || wcbRest.isSelected()) {
						if (wcbRest.isSelected()) {
							// rest ist wie divisor mit 1
							wnfDivisor.setInteger(1);
						}
						int iDivisor = this.wnfDivisor.getInteger();
						DelegateFactory.getInstance().getAuftragRahmenAbrufDelegate()
								.erzeugeAbrufpositionen(tpAuftrag.getAuftragDto().getIId(), iDivisor);
						tpAuftrag.gotoSichtRahmen();
					} else if (wcbManuellAbruf.isSelected()) {
						// alle bespos mit restmenge in sicht rahmen anzeigen
						tpAuftrag.gotoSichtRahmen();
					}
				}
			}
			eventYouAreSelected(false);
		}
	}

	public void initKonditionen() throws Throwable {
		if (tpAuftrag.getKundeAuftragDto().getFRabattsatz() != null) {
			tpAuftrag.getAuftragDto().setFAllgemeinerRabattsatz(
					new Double(tpAuftrag.getKundeAuftragDto().getFRabattsatz().doubleValue()));
		}
		if (tpAuftrag.getKundeAuftragDto() != null) {
			tpAuftrag.getAuftragDto().setIGarantie(tpAuftrag.getKundeAuftragDto().getIGarantieinmonaten());
		}
		if (tpAuftrag.getKundeAuftragDto().getLieferartIId() != null) {
			tpAuftrag.getAuftragDto().setLieferartIId(tpAuftrag.getKundeAuftragDto().getLieferartIId());
		}
		if (tpAuftrag.getKundeAuftragDto().getZahlungszielIId() != null) {
			tpAuftrag.getAuftragDto().setZahlungszielIId(tpAuftrag.getKundeAuftragDto().getZahlungszielIId());
		}

		if (tpAuftrag.getKundeAuftragDto().getVerrechnungsmodellIId() != null) {
			tpAuftrag.getAuftragDto()
					.setVerrechnungsmodellIId(tpAuftrag.getKundeAuftragDto().getVerrechnungsmodellIId());
		}

		if (tpAuftrag.getKundeAuftragDto().getSpediteurIId() != null) {
			tpAuftrag.getAuftragDto().setSpediteurIId(tpAuftrag.getKundeAuftragDto().getSpediteurIId());
		}

		if (tpAuftrag.getAuftragDto().getBMitzusammenfassung() == null) {
			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_DEFAULT_MIT_ZUSAMMENFASSUNG,
							ParameterFac.KATEGORIE_ANGEBOT, LPMain.getTheClient().getMandant());

			boolean bMitZusammenfassung = (Boolean) parameter.getCWertAsObject();

			tpAuftrag.getAuftragDto().setBMitzusammenfassung(Helper.boolean2Short(bMitZusammenfassung));
		}

	}

	/**
	 * Drucke Auftragbestaetigung.
	 * 
	 * @param e Ereignis
	 * @throws Throwable
	 */
	protected void eventActionPrint(ActionEvent e) throws Throwable {
		tpAuftrag.printAuftragbestaetigung();
		// eventYouAreSelected(false);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAGART_CHANGED)) {
			if (wcoAuftragsart.getKeyOfSelectedItem().equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
				wlaLiefertermin.setText(LPMain.getTextRespectUISPr("lp.rahmentermin"));
				setVisibleAbrufe(true, false);
			} else if (wcoAuftragsart.getKeyOfSelectedItem().equals(AuftragServiceFac.AUFTRAGART_FREI)
					&& LPMain.getInstance().getDesktop().darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_AUFTRAGUNTERKOSTENSTELLEN)) {

				setVisibleAbrufe(true, true);
			} else {
				wlaLiefertermin.setText(LPMain.getTextRespectUISPr("label.liefertermin"));
				setVisibleAbrufe(false, false);
			}
			bAbrufbesUndNeu = false;
			if (wcoAuftragsart.getKeyOfSelectedItem().equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
				setVisisbleAbrufausrahmen(true);
				wtfRahmencnr.setMandatoryField(true);

				bAbrufbesUndNeu = (tpAuftrag != null) && (tpAuftrag.getAuftragDto() != null);
				// && (tpAuftrag.getAuftragDto().getIId() == null);

				wlaLeer.setVisible(!bAbrufbesUndNeu);
				wnfDivisor.setMandatoryField(bAbrufbesUndNeu);
				jPanelAbrufBes.setVisible(bAbrufbesUndNeu);
				wnfDivisor.getText();
			} else {
				rahmenauftragDto = new AuftragDto();

				wtfRahmencnr.setText("");
				wtfRahmencnr.setMandatoryField(false);
				wtfRahmenbez.setText("");

				setVisisbleAbrufausrahmen(false);
				wnfDivisor.setMandatoryField(false);
				jPanelAbrufBes.setVisible(false);

			}
			if (wcoAuftragsart.getKeyOfSelectedItem().equals(AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
				wlaWiederholungsintervall.setVisible(true);
				wcoWiederholungsintervall.setVisible(true);
				wlaLauf.setVisible(true);
				wdfLauf.setVisible(true);
				wdfLauf.setMandatoryField(true);
				wlaLaufBis.setVisible(true);
				wdfLaufBis.setVisible(true);

			} else {
				wlaWiederholungsintervall.setVisible(false);
				wcoWiederholungsintervall.setVisible(false);
				wlaLauf.setVisible(false);
				wdfLauf.setVisible(false);
				wdfLauf.setMandatoryField(false);
				wlaLaufBis.setVisible(false);
				wdfLaufBis.setVisible(false);

			}
		} else if (e.getActionCommand().equals(Desktop.MY_OWN_NEW_ZEIT_START)
				|| e.getActionCommand().equals(Desktop.MY_OWN_NEW_ZEIT_STOP)) {

			LPMain.getInstance().getDesktop().zeitbuchungAufBeleg(e.getActionCommand(), LocaleFac.BELEGART_AUFTRAG,
					tpAuftrag.getAuftragDto().getIId());

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_RAHMENAUSWAHL)) {
			dialogQueryRahmenauftragFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_AUFTRAG)) {
			dialogQueryKunde(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER2)) {
			dialogQueryVertreter2(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANGEBOT_LEEREN)) {

			if (tpAuftrag.getAuftragDto().getAngebotIId() != null) {

				Integer angebotIId = tpAuftrag.getAuftragDto().getAngebotIId();
				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("auft.angebot.entkoppeln.warning"));

				if (b == true) {
					tpAuftrag.getAuftragDto().setAngebotIId(null);
					DelegateFactory.getInstance().getAuftragDelegate()
							.updateAuftragOhneWeitereAktion(tpAuftrag.getAuftragDto());

					AngebotDto agDto = DelegateFactory.getInstance().getAngebotDelegate()
							.angebotFindByPrimaryKey(angebotIId);
					agDto.setAngeboterledigungsgrundCNr(null);
					agDto.setStatusCNr(AngebotServiceFac.ANGEBOTSTATUS_OFFEN);
					DelegateFactory.getInstance().getAngebotDelegate().updateAngebotOhneWeitereAktion(agDto);

				}

				eventYouAreSelected(false);
			}

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VERSTECKEN)) {
			DelegateFactory.getInstance().getAuftragDelegate()
					.updateAuftragVersteckt(tpAuftrag.getAuftragDto().getIId());

			eventYouAreSelected(false);

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE)) {
			dialogQueryAnsprechpartner(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERADRESSE)) {
			dialogQueryAnsprechpartnerLieferadresse(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER_RECHNUNGSADRESSE)) {
			dialogQueryAnsprechpartnerRechnungsadresse(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ABBUCHUNGSLAGER_FROM_LISTE)) {
			dialogQueryAbbuchungsLagerFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER_KUNDE)) {
			dialogQueryVertreter(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERADRESSE_AUFTRAG)) {
			dialogQueryLieferadresse(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_RECHNUNGSADRESSE_AUFTRAG)) {
			dialogQueryRechnungsadresse(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_WAEHRUNG)) {
			dialogQueryWaehrung(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ORDERRESPONSE)) {
			if (tpAuftrag.getAuftragDto().getTResponse() == null) {
				dialogOrderResponseErzeugen();
			} else {
				boolean yes = DialogFactory.showModalJaNeinDialog(tpAuftrag.getInternalFrame(),
						LPMain.getTextRespectUISPr("auftrag.bereitsbestaetigt"));
				if (yes) {
					dialogOrderResponseErzeugen();
				}
			}
		} else if (e.getActionCommand().equals(MY_OWN_NEW_ANZAHLUNGSRECHNUNG_AUS_AUFTRAG)) {

			if (tpAuftrag.getAuftragDto().getTAuftragsfreigabe() == null) {
				// ERROR
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
						LPMain.getTextRespectUISPr("auft.anzahlungsre.aus.ab.nichtfreigegeben"));
			} else {
				Date tBelegdatum = null;

				InternalFrameRechnung intFrameRE = (InternalFrameRechnung) LPMain.getInstance().getDesktop()
						.getLPModul(LocaleFac.BELEGART_RECHNUNG);

				if (intFrameRE != null) {
					tBelegdatum = intFrameRE.neuDatum;
				}

				Integer rechnungIIdNeu = DelegateFactory.getInstance().getRechnungDelegate()
						.createAnzahlungsrechnungAusAuftrag(tpAuftrag.getAuftragDto().getIId(), tBelegdatum,
								getInternalFrame());
				WrapperGotoButton wbuGoto = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_RECHNUNG_AUSWAHL);
				wbuGoto.setOKey(rechnungIIdNeu);
				wbuGoto.actionPerformed(new ActionEvent(wbuGoto, 0, WrapperGotoButton.ACTION_GOTO));

			}

		} else if (e.getActionCommand().equals(MY_OWN_NEW_TOGGLE_FREIGABE)) {
			// PJ 17558
			if (tpAuftrag.getAuftragDto().getIId() != null
					&& tpAuftrag.getAuftragDto().getStatusCNr().equals(LocaleFac.STATUS_ANGELEGT)) {

				if (tpAuftrag.getAuftragDto().getTAuftragsfreigabe() != null) {
					if (DialogFactory.showMeldung(

							LPMain.getTextRespectUISPr("auft.freigeben.ruecknahme.frage"),
							LPMain.getTextRespectUISPr("lp.frage"),
							javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {

					} else {
						return;
					}

				}

				DelegateFactory.getInstance().getAuftragDelegate()
						.toggleAuftragsfreigabe(tpAuftrag.getAuftragDto().getIId());
				tpAuftrag.setAuftragDto(DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey(tpAuftrag.getAuftragDto().getIId()));

				eventYouAreSelected(false);
			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
						LPMain.getTextRespectUISPr("auft.freigeben.error"));
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LAENDERART_CHANGED)) {
		} else if (e.getSource() != null && (e.getSource().equals(wcbDivisor) || e.getSource().equals(wcbManuellAbruf)
				|| e.getSource().equals(wcbRest))) {

			abrufCheckBoxSetzen();

		}
	}

	private Throwable resultException = null;

	private void dialogOrderResponseErzeugen() {
		JTextArea msgLabel;
		JProgressBar progressBar;
		final int MAXIMUM = 100;
		JPanel panel;

		progressBar = new JProgressBar(0, MAXIMUM);
		progressBar.setIndeterminate(true);
		msgLabel = new JTextArea(LPMain.getTextRespectUISPr("lp.versandweg.durchfuehren"));
		msgLabel.setEditable(false);

		panel = new JPanel(new BorderLayout(5, 5));
		panel.add(msgLabel, BorderLayout.PAGE_START);
		panel.add(progressBar, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(11, 11, 11, 11));

		final JDialog dialog = new JDialog();
		dialog.getContentPane().add(panel);
		dialog.setResizable(false);
		dialog.pack();
		dialog.setSize(500, dialog.getHeight());
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setAlwaysOnTop(false);
		dialog.setVisible(true);
		msgLabel.setBackground(panel.getBackground());

		SwingWorker<String, Object> worker = new SwingWorker<String, Object>() {
			@Override
			protected void done() {
				dialog.dispose();

				if (resultException != null) {
					handleException(resultException, true);
				}
			}

			@Override
			protected String doInBackground() throws Exception {
				String result = "";
				resultException = null;
				try {
					result = DelegateFactory.getInstance().getAuftragDelegate()
							.createOrderResponsePost(tpAuftrag.getAuftragDto());
					publish(result);
					setProgress(100);

//					tpAuftrag.initializeDtos(tpAuftrag.getAuftragDto().getIId());
//					updateOrderResponseButton();
				} catch (Throwable t) {
					resultException = t;
				}

				return result;
			}

			@Override
			protected void process(List<Object> chunks) {
				for (Object result : chunks) {
					if (result instanceof String) {
						try {
							tpAuftrag.initializeDtos(tpAuftrag.getAuftragDto().getIId());
							updateOrderResponseButton();
						} catch (Throwable t) {
							resultException = t;
						}
					}
				}
			}
		};

		worker.execute();
	}

	private void dialogQueryRahmenauftragFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRRahmenauswahl = AuftragFilterFactory.getInstance().createPanelFLRRahmenauftrag(intFrame, true,
				false, rahmenauftragDto.getIId());

		new DialogQuery(panelQueryFLRRahmenauswahl);
	}

	private void dialogQueryWaehrung(ActionEvent e) throws Throwable {
		panelQueryFLRWaehrung = FinanzFilterFactory.getInstance().createPanelFLRWaehrung(getInternalFrame(),
				waehrungDto.getCNr());
		new DialogQuery(panelQueryFLRWaehrung);
	}

	public void dialogQueryKunde(ActionEvent e) throws Throwable {

		if (bSchnellanlage) {
			panelQueryFLRKunde = PartnerFilterFactory.getInstance().createPanelFLRKunde(intFrame, true, false,
					kundeIIdLetzterSchnellauftrag);

		} else {
			panelQueryFLRKunde = PartnerFilterFactory.getInstance().createPanelFLRKunde(intFrame, true, false,
					tpAuftrag.getKundeAuftragDto().getIId());

		}

		new DialogQuery(panelQueryFLRKunde);
	}

	private void dialogQueryAnsprechpartner(ActionEvent e) throws Throwable {
		if (tpAuftrag.getKundeAuftragDto().getIId() == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
		} else {
			panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance().createPanelFLRAnsprechpartner(intFrame,
					tpAuftrag.getKundeAuftragDto().getPartnerDto().getIId(), ansprechpartnerDto.getIId(), true, true);

			new DialogQuery(panelQueryFLRAnsprechpartner);
		}
	}

	private void dialogQueryAnsprechpartnerLieferadresse(ActionEvent e) throws Throwable {
		if (kundeLieferadresseDto == null || kundeLieferadresseDto.getIId() == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
		} else {

			panelQueryFLRAnsprechpartner_Lieferadresse = PartnerFilterFactory.getInstance()
					.createPanelFLRAnsprechpartner(intFrame, kundeLieferadresseDto.getPartnerIId(),
							ansprechpartnerDtoLieferadresse.getIId(), true, true);

			new DialogQuery(panelQueryFLRAnsprechpartner_Lieferadresse);
		}
	}

	private void dialogQueryAnsprechpartnerRechnungsadresse(ActionEvent e) throws Throwable {
		if (kundeRechnungsadresseDto == null || kundeRechnungsadresseDto.getIId() == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
		} else {

			panelQueryFLRAnsprechpartner_Rechungsadresse = PartnerFilterFactory.getInstance()
					.createPanelFLRAnsprechpartner(intFrame, kundeRechnungsadresseDto.getPartnerIId(),
							ansprechpartnerDtoRechnungsadresse.getIId(), true, true);

			new DialogQuery(panelQueryFLRAnsprechpartner_Rechungsadresse);
		}
	}

	private void dialogQueryVertreter(ActionEvent e) throws Throwable {
		panelQueryFLRVertreter = PersonalFilterFactory.getInstance().createPanelFLRPersonal(intFrame, true, false,
				vertreterDto.getIId());

		new DialogQuery(panelQueryFLRVertreter);
	}

	void dialogQueryLieferadresse(ActionEvent e) throws Throwable {
		panelQueryFLRLieferadresse = PartnerFilterFactory.getInstance().createPanelFLRKunde(intFrame, true, false,
				kundeLieferadresseDto.getIId());

		new DialogQuery(panelQueryFLRLieferadresse);
	}

	private void dialogQueryRechnungsadresse(ActionEvent e) throws Throwable {
		panelQueryFLRRechnungsadresse = PartnerFilterFactory.getInstance().createPanelFLRKunde(intFrame, true, false,
				kundeRechnungsadresseDto.getIId());

		new DialogQuery(panelQueryFLRRechnungsadresse);
	}

	private void dialogQueryKostenstelle(ActionEvent e) throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance().createPanelFLRKostenstelle(getInternalFrame(),
				false, false, kostenstelleDto.getIId());

		new DialogQuery(panelQueryFLRKostenstelle);
	}

	private void dto2Components() throws Throwable {
		if (tpAuftrag.getAuftragDto().getAngebotIId() != null) {
			AngebotDto angebotDto = DelegateFactory.getInstance().getAngebotDelegate()
					.angebotFindByPrimaryKey(tpAuftrag.getAuftragDto().getAngebotIId());
			wtfAngebot.setText(angebotDto.getCNr());
			wbuAngebot.setOKey(angebotDto.getIId());
			wbuAngebot.getWrapperButtonGoTo().setEnabled(true);
		} else {
			wbuAngebot.setOKey(null);
		}

		if (Helper.short2boolean(tpAuftrag.getAuftragDto().getBVersteckt())) {
			wlaVersteckt.setText(LPMain.getTextRespectUISPr("lp.versteckt"));
		} else {
			wlaVersteckt.setText("");
		}

		wcoAuftragsart.setKeyOfSelectedItem(tpAuftrag.getAuftragDto().getAuftragartCNr());

		// SP2202
		if (tpAuftrag.getAuftragDto().getAngebotIId() != null) {
			AngebotDto agDto = DelegateFactory.getInstance().getAngebotDelegate()
					.angebotFindByPrimaryKey(tpAuftrag.getAuftragDto().getAngebotIId());
			wsfProjekt.setKey(agDto.getProjektIId());
		} else {
			wsfProjekt.setKey(tpAuftrag.getAuftragDto().getProjektIId());
		}

		Integer auftragIIdRahmenauftrag = tpAuftrag.getAuftragDto().getAuftragIIdRahmenauftrag();

		if (tpAuftrag.getAuftragDto().getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {

			if (tpAuftrag.getAuftragDto().getWiederholungsintervallCNr() != null) {
				wcoWiederholungsintervall
						.setKeyOfSelectedItem(tpAuftrag.getAuftragDto().getWiederholungsintervallCNr());
			}

			wdfLauf.setTimestamp(tpAuftrag.getAuftragDto().getTLauftermin());
			wdfLaufBis.setTimestamp(tpAuftrag.getAuftragDto().getTLaufterminBis());
		}

		if (auftragIIdRahmenauftrag != null) {
			rahmenauftragDto = DelegateFactory.getInstance().getAuftragDelegate()
					.auftragFindByPrimaryKey(auftragIIdRahmenauftrag);

			wtfRahmencnr.setText(rahmenauftragDto.getCNr());
			wbuRahmenauswahl.setOKey(rahmenauftragDto.getIId());
			wtfRahmenbez.setText(rahmenauftragDto.getCBezProjektbezeichnung());
		}

		setVersionComponents(tpAuftrag.getAuftragDto().getIVersion());
		wdfBelegdatum.setDate(tpAuftrag.getAuftragDto().getTBelegdatum());
		wdfBestelldatum.setDate(tpAuftrag.getAuftragDto().getDBestelldatum());
		kundeAuftragDto2Components();

		// Ansprechpartner bestimmen
		Integer iIdAnsprechpartner = tpAuftrag.getAuftragDto().getAnsprechparnterIId();
		if (iIdAnsprechpartner != null) {
			ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

			wtfAnsprechpartner.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
		} else {
			wtfAnsprechpartner.setText("");
		}

		HelperClient.pruefeAnsprechpartner(tpAuftrag.getKundeAuftragDto().getPartnerIId(), iIdAnsprechpartner,
				wtfAnsprechpartner);

		LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate()
				.lagerFindByPrimaryKey(tpAuftrag.getAuftragDto().getLagerIIdAbbuchungslager());
		wtfAbbuchungslager.setText(lagerDto.getCNr());

		// vertreter bestimmen, kann null sein
		Integer pkVertreter = tpAuftrag.getAuftragDto().getPersonalIIdVertreter();
		if (pkVertreter != null) {
			vertreterDto = DelegateFactory.getInstance().getPersonalDelegate().personalFindByPrimaryKey(pkVertreter);
			vertreterDto2components();
		}

		if (tpAuftrag.getAuftragDto().getPersonalIIdVertreter2() != null) {
			PersonalDto vertreterDto2 = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(tpAuftrag.getAuftragDto().getPersonalIIdVertreter2());

			wtfVertreter2.setText(vertreterDto2.getPartnerDto().formatTitelAnrede());
		} else {
			wtfVertreter2.setText(null);
		}

		// Lieferadresse bestimmen
		Integer pkKundeLiefer = tpAuftrag.getAuftragDto().getKundeIIdLieferadresse();
		kundeLieferadresseDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(pkKundeLiefer);
		kundeLieferadresseDto2components();

		// Rechnungsadresse bestimmen
		Integer pkKundeRechnung = tpAuftrag.getAuftragDto().getKundeIIdRechnungsadresse();
		kundeRechnungsadresseDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(pkKundeRechnung);
		kundeRechnungsadresseDto2components();

		// Ansprechpartner bestimmen
		Integer iIdAnsprechpartnerLieferadresse = tpAuftrag.getAuftragDto().getAnsprechpartnerIIdLieferadresse();
		bestimmeUndZeigeAnsprechpartnerLieferadresse(iIdAnsprechpartnerLieferadresse,
				kundeLieferadresseDto.getPartnerIId());

		Integer iIdAnsprechpartnerRechnungsadresse = tpAuftrag.getAuftragDto().getAnsprechpartnerIIdRechnungsadresse();
		bestimmeUndZeigeAnsprechpartnerRechnungsadresse(iIdAnsprechpartnerRechnungsadresse,
				kundeRechnungsadresseDto.getPartnerIId());

		kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
				.kostenstelleFindByPrimaryKey(tpAuftrag.getAuftragDto().getKostIId());
		wtfKostenstelle.setText(kostenstelleDto.formatKostenstellenbezeichnung());
		wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());

		this.wtfProjekt.setText(tpAuftrag.getAuftragDto().getCBezProjektbezeichnung());
		this.wtfBestellnummer.setText(tpAuftrag.getAuftragDto().getCBestellnummer());
		wtfKommission.setText(tpAuftrag.getAuftragDto().getCKommission());

		// Waehrung und Wechselkurs setzen
		wtfWaehrung.setText(tpAuftrag.getAuftragDto().getCAuftragswaehrung());
		wnfKurs.setDouble(tpAuftrag.getAuftragDto().getFWechselkursmandantwaehrungzubelegwaehrung());
		wnfErfuellungsgrad.setDouble(tpAuftrag.getAuftragDto().getFErfuellungsgrad());
		// @todo Sonderrabatt PJ 4799
		java.sql.Timestamp liefertermin = tpAuftrag.getAuftragDto().getDLiefertermin();
		if (liefertermin != null) {
			wtsfLiefertermin.setTimestamp(liefertermin);
			wtsfLiefertermin.getWtfZeit().setVisible(tpAuftrag.getBAuftragterminstudenminuten());
		}
		java.sql.Timestamp finaltermin = tpAuftrag.getAuftragDto().getDFinaltermin();

		if (finaltermin != null) {
			wtsfFinaltermin.setTimestamp(finaltermin);
			wtsfFinaltermin.getWtfZeit().setVisible(tpAuftrag.getBAuftragterminstudenminuten());
		}

		wdfWunschtermin.setDate(tpAuftrag.getAuftragDto().getTWunschtermin());

		wcbUnverbindlich.setSelected(Helper.short2boolean(tpAuftrag.getAuftragDto().getBLieferterminUnverbindlich()));
		wcbTeillieferung.setSelected(Helper.short2boolean(tpAuftrag.getAuftragDto().getBTeillieferungMoeglich()));
		wcbPoenale.setSelected(Helper.short2boolean(tpAuftrag.getAuftragDto().getBPoenale()));
		wcbRoHs.setSelected(Helper.short2boolean(tpAuftrag.getAuftragDto().getBRoHs()));

		if (tpAuftrag.getAuftragDto().getPersonalIIdVerrechenbar() != null) {
			PersonalDto personalDtoVerrechnen = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(tpAuftrag.getAuftragDto().getPersonalIIdVerrechenbar());

			wlaVerrechenbar.setText(LPMain.getTextRespectUISPr("iv.verrechenbargesetzt") + " "
					+ Helper.formatDatum(tpAuftrag.getAuftragDto().getTVerrechenbar(), LPMain.getTheClient().getLocUi())
					+ ", " + personalDtoVerrechnen.formatAnrede());

		} else {
			wlaVerrechenbar.setText("");
		}

		String text = "";

		if (tpAuftrag.getAuftragDto().getTAuftragsfreigabe() != null) {
			text = LPMain.getTextRespectUISPr("auft.freigegebenam") + " " + Helper.formatDatumZeit(
					tpAuftrag.getAuftragDto().getTAuftragsfreigabe(), LPMain.getTheClient().getLocUi());
		}
		if (tpAuftrag.getAuftragDto().getPersonalIIdAuftragsfreigabe() != null) {
			text += "(" + DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(tpAuftrag.getAuftragDto().getPersonalIIdAuftragsfreigabe())
					.getCKurzzeichen() + ")";
		}

		wlaFreigabe.setText(text);

		wnfLeihtage.setInteger(tpAuftrag.getAuftragDto().getILeihtage());

		if (tpAuftrag.getAuftragDto().getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
			// alle Abrufe anzeigen
			AuftragDto[] aAbrufauftragDto = DelegateFactory.getInstance().getAuftragDelegate()
					.abrufauftragFindByAuftragIIdRahmenauftrag(tpAuftrag.getAuftragDto().getIId());

			wtaAbrufe.setText(formatAuftraege(aAbrufauftragDto));
		} else if (tpAuftrag.getAuftragDto().getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_FREI)) {
			// alle Abrufe anzeigen
			AuftragkostenstelleDto[] auftragkostenstelleDto = DelegateFactory.getInstance().getAuftragServiceDelegate()
					.auftragkostenstellefindByAuftrag(tpAuftrag.getAuftragDto().getIId());

			wtaAbrufe.setText(formatKostenstellen(auftragkostenstelleDto));
		}

		wcoLaenderart.setKeyOfSelectedItem(tpAuftrag.getAuftragDto().getLaenderartCnr());
		aktualisiereStatusbar();
	}

	private void setVersionComponents(Integer iVersion) {
		wnfVersion.setInteger(iVersion);
		wnfVersion.setVisible(iVersion != null);
		wlaVersion.setVisible(iVersion != null);
	}

	private void updateOrderResponseButton() throws Throwable, ExceptionLP {
		if (bZusatzfunktionVersandweg) {
			AuftragDto auftragDto = tpAuftrag.getAuftragDto();
			boolean hasOrderResponse = auftragDto != null && auftragDto.getTResponse() != null;
			JButton button = getHmOfButtons().get(ACTION_SPECIAL_ORDERRESPONSE).getButton();
			button.setIcon(hasOrderResponse ? RESPONSE_ICON_DONE : RESPONSE_ICON);

			boolean enable = auftragDto != null && auftragDto.getIId() != null && auftragDto.getTResponse() == null
					&& LocaleFac.STATUS_OFFEN.equals(auftragDto.getStatusCNr())
					&& DelegateFactory.getInstance().getAuftragDelegate().hatAuftragVersandweg(auftragDto);
			if (hasOrderResponse) {
				enable = true;
			}
			enableToolsPanelButtons(enable, ACTION_SPECIAL_ORDERRESPONSE);
		}
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAuftrag.getAuftragDto().getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAuftrag.getAuftragDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAuftrag.getAuftragDto().getPersonalIIdAendern());
		setStatusbarTAendern(tpAuftrag.getAuftragDto().getTAendern());

		String add2Status = "";
		Timestamp tErledigt = null;
		String kurzzeichen = null;

		if (tpAuftrag.getAuftragDto().getTErledigt() != null) {
			tErledigt = tpAuftrag.getAuftragDto().getTErledigt();
		} else {
			if (tpAuftrag.getAuftragDto().getTManuellerledigt() != null) {
				tErledigt = tpAuftrag.getAuftragDto().getTManuellerledigt();
			}
		}
		if (tpAuftrag.getAuftragDto().getPersonalIIdErledigt() != null) {

			kurzzeichen = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(tpAuftrag.getAuftragDto().getPersonalIIdErledigt()).getCKurzzeichen();
		} else {
			if (tpAuftrag.getAuftragDto().getPersonalIIdManuellerledigt() != null) {
				kurzzeichen = DelegateFactory.getInstance().getPersonalDelegate()
						.personalFindByPrimaryKey(tpAuftrag.getAuftragDto().getPersonalIIdManuellerledigt())
						.getCKurzzeichen();
			}
		}

		if (tErledigt != null) {
			add2Status += "/" + Helper.formatDatum(tErledigt, LPMain.getTheClient().getLocUi());
		}
		if (kurzzeichen != null) {
			add2Status += "/" + kurzzeichen;
		}

		setStatusbarStatusCNr(tpAuftrag.getAuftragStatus());
		getPanelStatusbar().addToSpalteStatus(add2Status);
		String status = DelegateFactory.getInstance().getVersandDelegate().getVersandstatus(LocaleFac.BELEGART_AUFTRAG,
				tpAuftrag.getAuftragDto().getIId());
		if (status != null) {
			status = LPMain.getTextRespectUISPr("lp.versandstatus") + ": " + status;
		}
		setStatusbarSpalte5(status);
	}

	private void dialogQueryAbbuchungsLagerFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRAbbuchungsLager = ArtikelFilterFactory.getInstance().createPanelFLRLager(getInternalFrame(),
				tpAuftrag.getAuftragDto().getLagerIIdAbbuchungslager(), true, false);

		new DialogQuery(panelQueryFLRAbbuchungsLager);
	}

	/**
	 * Die Eigenschaften des Vertreters beim Kunden zur Anzeige bringen.
	 * 
	 * @throws java.lang.Throwable Ausnahme
	 */
	private void vertreterDto2components() throws Throwable {
		if (vertreterDto != null && vertreterDto.getIId() != null) {
			wtfVertreter.setText(vertreterDto.getPartnerDto().formatTitelAnrede());
		}
	}

	/**
	 * Die Eigenschaften des Auftragskunden zur Anzeige bringen.
	 * 
	 * @throws java.lang.Throwable Ausnahme
	 */
	private void kundeAuftragDto2Components() throws Throwable {
		jButtonKunde.setOKey(tpAuftrag.getKundeAuftragDto().getIId());
		wtfKundeAuftrag.setText(tpAuftrag.getKundeAuftragDto().getPartnerDto().formatTitelAnrede());
		String sAdresse = tpAuftrag.getKundeAuftragDto().getPartnerDto().formatAdresse();
		if (tpAuftrag.getKundeAuftragDto().getPartnerDto().getCKbez() != null)
			;
		sAdresse = sAdresse + "  /  " + tpAuftrag.getKundeAuftragDto().getPartnerDto().getCKbez();
		wtfKundeAuftragAdresse.setText(sAdresse);
		wtfKundeAuftragAbteilung.setText(tpAuftrag.getKundeAuftragDto().getPartnerDto().getCName3vorname2abteilung());

		// default waehrung des auftrags kommt aus dem kunden
		waehrungDto = DelegateFactory.getInstance().getLocaleDelegate()
				.waehrungFindByPrimaryKey(tpAuftrag.getKundeAuftragDto().getCWaehrung());
		wtfWaehrung.setText(waehrungDto.getCNr());
		setzeWechselkurs();

		// default kostenstelle des auftrags kommt aus dem kunden

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_KOSTENSTELLE_IN_VK_BELEGEN_VORBESETZT,
						ParameterFac.KATEGORIE_ALLGEMEIN, LPMain.getTheClient().getMandant());
		boolean bKostenstelleVorbesetzten = ((Boolean) parameter.getCWertAsObject());

		if (tpAuftrag.getKundeAuftragDto().getKostenstelleIId() != null && bKostenstelleVorbesetzten == true) {
			kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
					.kostenstelleFindByPrimaryKey(tpAuftrag.getKundeAuftragDto().getKostenstelleIId());
			wtfKostenstelle.setText(kostenstelleDto.formatKostenstellenbezeichnung());
			wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
		}

		// default fuer teillieferung erlaubt im auftrag kommt aus dem kunden
		wcbTeillieferung
				.setSelected(Helper.short2boolean(tpAuftrag.getKundeAuftragDto().getBAkzeptiertteillieferung()));

		wcoLaenderart.setKeyOfSelectedItem(tpAuftrag.getKundeAuftragDto().getLaenderartCnr());

		// @todo in diesem Moment muessen sich alle sprachabh Felder des
		// Auftrags aendern? PJ 4800
	}

	/**
	 * Die Eigenschaften des Kunden der Lieferadresse zur Ansicht bringen.
	 * 
	 * @throws java.lang.Throwable Ausnahme
	 */
	private void kundeLieferadresseDto2components() throws Throwable {
		String sAdresse = kundeLieferadresseDto.getPartnerDto().formatTitelAnrede();

		if (kundeLieferadresseDto.getPartnerDto().formatAdresse() != null
				&& !kundeLieferadresseDto.getPartnerDto().formatAdresse().equals("")) {
			sAdresse = sAdresse + ", " + kundeLieferadresseDto.getPartnerDto().formatAdresse();
		}

		if (kundeLieferadresseDto.getPartnerDto().getCKbez() != null)
			;
		sAdresse = sAdresse + "  /  " + kundeLieferadresseDto.getPartnerDto().getCKbez();
		wtfKundeLieferadresse.setText(sAdresse);
	}

	/**
	 * Die Eigenschaften des Kunden der Rechnungsadresse zur Anzeige bringen.
	 * 
	 * @throws java.lang.Throwable Ausnahme
	 */
	private void kundeRechnungsadresseDto2components() throws Throwable {
		String sAdresse = kundeRechnungsadresseDto.getPartnerDto().formatTitelAnrede();

		if (kundeRechnungsadresseDto.getPartnerDto().formatAdresse() != null
				&& !kundeRechnungsadresseDto.getPartnerDto().formatAdresse().equals("")) {
			sAdresse = sAdresse + ", " + kundeRechnungsadresseDto.getPartnerDto().formatAdresse();
		}

		if (kundeRechnungsadresseDto.getPartnerDto().getCKbez() != null)
			;
		sAdresse = sAdresse + "  /  " + kundeRechnungsadresseDto.getPartnerDto().getCKbez();
		wtfKundeRechnungsadresse.setText(sAdresse);
	}

	/**
	 * Alle Auftragdaten aus dem Panel sammeln.
	 * 
	 * @throws Throwable
	 */
	protected void components2auftragDto() throws Throwable {
		if (tpAuftrag.getAuftragDto() == null || tpAuftrag.getAuftragDto().getIId() == null) {
			// tpAuftrag.setAuftragDto(new AuftragDto());
			tpAuftrag.getAuftragDto().setMandantCNr(LPMain.getTheClient().getMandant());
			tpAuftrag.getAuftragDto().setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
		}

		tpAuftrag.getAuftragDto().setAuftragartCNr((String) wcoAuftragsart.getKeyOfSelectedItem());
		tpAuftrag.getAuftragDto().setProjektIId(wsfProjekt.getIKey());
		if (rahmenauftragDto != null
				&& wcoAuftragsart.getKeyOfSelectedItem().equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
			tpAuftrag.getAuftragDto().setAuftragIIdRahmenauftrag(rahmenauftragDto.getIId());

		}

		if (wcoAuftragsart.getKeyOfSelectedItem().equals(AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {

			if (tpAuftrag.getAuftragDto().getIId() != null
					&& tpAuftrag.getAuftragDto().getWiederholungsintervallCNr() != null
					&& !tpAuftrag.getAuftragDto().getWiederholungsintervallCNr()
							.equals((String) wcoWiederholungsintervall.getKeyOfSelectedItem())) {
				// Art wurde geaendert, Datum berechnen
				java.sql.Date d = DelegateFactory.getInstance().getAuftragDelegate()
						.getAuftragWiederholungsstart(tpAuftrag.getAuftragDto().getIId());
				if (d != null)
					wdfLauf.setTimestamp(new Timestamp(d.getTime()));

			}
			tpAuftrag.getAuftragDto()
					.setWiederholungsintervallCNr((String) wcoWiederholungsintervall.getKeyOfSelectedItem());
			tpAuftrag.getAuftragDto().setTLauftermin(wdfLauf.getTimestamp());
			tpAuftrag.getAuftragDto().setTLaufterminBis(wdfLaufBis.getTimestamp());
		}

		tpAuftrag.getAuftragDto().setTBelegdatum(wdfBelegdatum.getTimestamp());
		tpAuftrag.getAuftragDto().setDBestelldatum(wdfBestelldatum.getTimestamp());
		tpAuftrag.getAuftragDto().setKundeIIdAuftragsadresse(tpAuftrag.getKundeAuftragDto().getIId());
		tpAuftrag.getAuftragDto().setAnsprechpartnerIId(ansprechpartnerDto.getIId());
		tpAuftrag.getAuftragDto().setAnsprechpartnerIIdLieferadresse(ansprechpartnerDtoLieferadresse.getIId());
		tpAuftrag.getAuftragDto().setAnsprechpartnerIIdRechnungsadresse(ansprechpartnerDtoRechnungsadresse.getIId());
		tpAuftrag.getAuftragDto().setPersonalIIdVertreter(vertreterDto.getIId());
		tpAuftrag.getAuftragDto().setKundeIIdLieferadresse(kundeLieferadresseDto.getIId());
		tpAuftrag.getAuftragDto().setKundeIIdRechnungsadresse(kundeRechnungsadresseDto.getIId());
		tpAuftrag.getAuftragDto().setCBezProjektbezeichnung(wtfProjekt.getText());
		tpAuftrag.getAuftragDto().setCBestellnummer(wtfBestellnummer.getText());
		tpAuftrag.getAuftragDto().setCKommission(wtfKommission.getText());
		tpAuftrag.getAuftragDto().setCAuftragswaehrung(wtfWaehrung.getText());
		tpAuftrag.getAuftragDto().setFWechselkursmandantwaehrungzubelegwaehrung(this.wnfKurs.getDouble());
		tpAuftrag.getAuftragDto().setBLieferterminUnverbindlich(Helper.boolean2Short(wcbUnverbindlich.isSelected()));

		tpAuftrag.getAuftragDto().setTWunschtermin(wdfWunschtermin.getTimestamp());

		if (tpAuftrag.getBAuftragterminstudenminuten()) {
			tpAuftrag.getAuftragDto().setDLiefertermin(wtsfLiefertermin.getTimestamp());
			tpAuftrag.getAuftragDto().setDFinaltermin(wtsfFinaltermin.getTimestamp());
		} else {
			tpAuftrag.getAuftragDto().setDLiefertermin(Helper.cutTimestamp(wtsfLiefertermin.getTimestamp()));
			tpAuftrag.getAuftragDto().setDFinaltermin(Helper.cutTimestamp(wtsfFinaltermin.getTimestamp()));
		}
		tpAuftrag.getAuftragDto().setBRoHs(Helper.boolean2Short(wcbRoHs.isSelected()));
		tpAuftrag.getAuftragDto().setKostIId(kostenstelleDto.getIId());
		tpAuftrag.getAuftragDto().setBTeillieferungMoeglich(Helper.boolean2Short(wcbTeillieferung.isSelected()));
		tpAuftrag.getAuftragDto().setBPoenale(Helper.boolean2Short(wcbPoenale.isSelected()));
		tpAuftrag.getAuftragDto().setBRoHs(Helper.boolean2Short(wcbRoHs.isSelected()));
		tpAuftrag.getAuftragDto().setILeihtage(wnfLeihtage.getInteger());
		tpAuftrag.getAuftragDto().setLaenderartCnr((String) wcoLaenderart.getKeyOfSelectedItem());
	}

	void jComboBoxAuftragsart_actionPerformed(ActionEvent e) {

	}

	/**
	 * Die Waehrung ist aenderbar, wenn ... ... ein neuer Auftrag angelegt wird ...
	 * der Auftrag den Status ('Angelegt' oder 'Offen') hat und keine
	 * mengenbehafteten Positionen hat und der Datensatz gelockt ist. <br>
	 * Wenn die Waehrung nicht geaendert werden darf, darf auch der Kunde nicht
	 * geaendert werden, da dieser seine Waehrung mitbringt.
	 * 
	 * @throws java.lang.Throwable Ausnahme
	 */
	/*
	 * private void setzeWaehrungAenderbar() throws Throwable { if
	 * (tpAuftrag.getAuftragDto() != null) { if (tpAuftrag.getAuftragDto().getIId()
	 * == null || ((tpAuftrag.getAuftragDto()
	 * .getAuftragstatusCNr().equals(AuftragServiceFac. AUFTRAGSTATUS_ANGELEGT) ||
	 * tpAuftrag.getAuftragDto().getAuftragstatusCNr().equals(AuftragServiceFac.
	 * AUFTRAGSTATUS_OFFEN)) &&
	 * DelegateFactory.getInstance().getAuftragpositionDelegate
	 * ().getAnzahlMengenbehafteteAuftragpositionen(
	 * tpAuftrag.getAuftragDto().getIId()) == 0) && isLocked()) {
	 * wbuWaehrung.setEnabled(true); jButtonKunde.setEnabled(true); } else {
	 * wbuWaehrung.setEnabled(false); jButtonKunde.setEnabled(false); } } }
	 */

	/**
	 * Einige Eingaben koennen nur unter bestimmten Voraussetzungen gemacht werden.
	 * <br>
	 * Betrifft:
	 * <ul>
	 * <li>Auftragart
	 * <li>Waehrung
	 * </ul>
	 * 
	 * @throws java.lang.Throwable Ausnahme
	 */
	private void enableComponentsInAbhaengigkeitZuStatusUndAnzahlMengenbehafteterPositionen() throws Throwable {
		boolean bEnableComponents = false;

		// Schritt 1: Ein bestehender Auftrag muss durch den aktuellen Benutzer
		// gelockt sein
		if (tpAuftrag.getAuftragDto().getIId() != null && isLockedByMe()) {
			// Schritt 1a: Der Auftrag befindet sich im Status Angelegt oder
			// Offen
			if (tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)
					|| tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)) {
				// Schritt 1b: Der Auftrag hat keine mengenbehafteten Positionen
				if (DelegateFactory.getInstance().getAuftragpositionDelegate()
						.getAnzahlMengenbehafteteAuftragpositionen(tpAuftrag.getAuftragDto().getIId()) == 0) {
					bEnableComponents = true;
				}
				if (tpAuftrag.getAuftragDto().getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
					boolean bEnable = DelegateFactory.getInstance().getAuftragDelegate()
							.darfWiederholungsTerminAendern(tpAuftrag.getAuftragDto().getIId());
					wdfLauf.setEnabled(bEnable);
					wdfLaufBis.setEnabled(bEnable);
				}
			}
		}
		// Schritt 2: Es handelt sich um einen neuen Auftrag
		else if (tpAuftrag.getAuftragDto().getIId() == null) {
			bEnableComponents = true;
		}

		wcoAuftragsart.setEnabled(bEnableComponents);
		// jButtonKunde.setEnabled(bEnableComponents); UW 24.03.06 Der Kunde
		// darf geaendert werden
		wbuWaehrung.setEnabled(bEnableComponents);
		wcoLaenderart.setEnabled(bEnableComponents);

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(com.lp.server.benutzer.service.RechteFac.RECHT_AUFT_AUFTRAG_CUD)) {

			if (tpAuftrag.getAuftragDto() != null && tpAuftrag.getAuftragDto().getIId() != null
					&& getHmOfButtons().get(ACTION_SPECIAL_VERSTECKEN) != null) {

				((LPButtonAction) getHmOfButtons().get(ACTION_SPECIAL_VERSTECKEN)).getButton().setEnabled(!isLockedByAnyone());
			}
		}

		// PJ18129
		if (tpAuftrag.getAuftragDto().getAngebotIId() != null) {
			AngebotDto angebotDto = DelegateFactory.getInstance().getAngebotDelegate()
					.angebotFindByPrimaryKey(tpAuftrag.getAuftragDto().getAngebotIId());
			if (angebotDto.getProjektIId() != null) {
				wsfProjekt.setEnabled(false);
				wsfProjekt.getWrapperGotoButton().setEnabled(false);
			}

		}

	}

	@Override
	public void eventActionLock(ActionEvent e) throws Throwable {
		// ist public wegen Termin verschieben aus TabbedPaneAuftrag
		super.eventActionLock(e);
	}

	@Override
	public void eventActionUnlock(ActionEvent e) throws Throwable {
		// ist public wegen Termin verschieben aus TabbedPaneAuftrag
		super.eventActionUnlock(e);
	}

	public void setzeWechselkurs() throws Throwable {
		if (waehrungDto != null && waehrungDto.getCNr() != null) {
			try {
				wnfKurs.setBigDecimal(DelegateFactory.getInstance().getLocaleDelegate()
						.getWechselkurs2(LPMain.getTheClient().getSMandantenwaehrung(), waehrungDto.getCNr()));
			} catch (Throwable t) {
				handleException(t, true); // UW: muss bleiben
				wnfKurs.setBigDecimal(null); // wnfKurs ist mandatory!
				eventActionUnlock(null);
				getInternalFrame().enableAllPanelsExcept(true);
				tpAuftrag.gotoAuswahl();
			}
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false); // Stati aller Components
		// aktualisieren

		// neu einlesen, ausloeser war ev. ein refresh oder discard
		Object oKey = tpAuftrag.getAuftragDto().getIId();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		// einen bestehenden Auftrag anzeigen
		if (oKey != null) {
			tpAuftrag.setAuftragDto(
					DelegateFactory.getInstance().getAuftragDelegate().auftragFindByPrimaryKey((Integer) oKey));
			tpAuftrag.setKundeAuftragDto(DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(tpAuftrag.getAuftragDto().getKundeIIdAuftragsadresse()));

			dto2Components();

			tpAuftrag.setTitleAuftrag(LPMain.getTextRespectUISPr("auft.title.panel.kopfdaten"));
		}

		// JButton button =
		// getHmOfButtons().get(ACTION_SPECIAL_ORDERRESPONSE).getButton();

		tpAuftrag.getAuftragKopfdaten().updateButtons(getLockedstateDetailMainKey());

		LPButtonAction toggleFreigabe = getHmOfButtons().get(MY_OWN_NEW_TOGGLE_FREIGABE);
		// Wenn vorhanden
		if (toggleFreigabe != null) {
			toggleFreigabe.getButton().setEnabled(true);
		}

		LPButtonAction anzahlungsrechnung = getHmOfButtons().get(MY_OWN_NEW_ANZAHLUNGSRECHNUNG_AUS_AUFTRAG);
		// Wenn vorhanden
		if (anzahlungsrechnung != null) {
			anzahlungsrechnung.getButton().setEnabled(true);
		}

		enableComponentsInAbhaengigkeitZuStatusUndAnzahlMengenbehafteterPositionen(); // unbedingt

		// tpAuftrag.enablePanelsNachBitmuster(getLockedstateDetailMainKey());

		if (tpAuftrag.getAuftragDto() != null && tpAuftrag.getAuftragDto().getIId() != null) {
			if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
				this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START).getButton().setEnabled(true);
			}
			if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
				this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_STOP).getButton().setEnabled(true);
			}
		}
		if (tpAuftrag.getAuftragDto() != null && tpAuftrag.getAuftragDto().getStatusCNr() != null
				&& tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
			if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
				this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START).getButton().setEnabled(false);
			}
			if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
				this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_STOP).getButton().setEnabled(false);
			}
		}
		jbuSetNull.setEnabled(true);
		if (tpAuftrag.getAuftragDto() != null && tpAuftrag.getAuftragDto().getAngebotIId() != null) {
			wbuAngebot.getWrapperButtonGoTo().setEnabled(true);
		}

		updateOrderResponseButton();
	}

	private void bestimmeUndZeigeAnsprechpartnerLieferadresse(Integer iIdAnsprechpartnerI, Integer partnerIId)
			throws Throwable {
		if (iIdAnsprechpartnerI != null) {
			ansprechpartnerDtoLieferadresse = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerI);

			wtfAnsprechpartnerLieferadresse
					.setText(ansprechpartnerDtoLieferadresse.formatFixTitelVornameNachnameNTitel());
		} else {
			wtfAnsprechpartnerLieferadresse.setText("");
		}

		HelperClient.pruefeAnsprechpartner(partnerIId, iIdAnsprechpartnerI, wtfAnsprechpartnerLieferadresse);

	}

	private void bestimmeUndZeigeAnsprechpartnerRechnungsadresse(Integer iIdAnsprechpartnerI, Integer partnerIId)
			throws Throwable {
		if (iIdAnsprechpartnerI != null) {
			ansprechpartnerDtoRechnungsadresse = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerI);

			wtfAnsprechpartnerRechungsadresse
					.setText(ansprechpartnerDtoRechnungsadresse.formatFixTitelVornameNachnameNTitel());
		} else {
			wtfAnsprechpartnerRechungsadresse.setText("");
		}

		HelperClient.pruefeAnsprechpartner(partnerIId, iIdAnsprechpartnerI, wtfAnsprechpartnerRechungsadresse);

	}

	/**
	 * Wenn in Liefertermin oder Finaltermin ein neues Datum gewaehlt wurde, dann
	 * landet man hier.
	 * 
	 * @param evt Ereignis
	 */
	public void propertyChange(PropertyChangeEvent evt) {

		try {
			if (evt.getSource() == wtsfLiefertermin.getWdfDatum().getDisplay()
					&& evt.getPropertyName().equals("date")) {
				wtsfFinaltermin.getWdfDatum().setMinimumValue(wtsfLiefertermin.getTimestamp());

				if (wtsfFinaltermin.getTimestamp() != null && wtsfLiefertermin.getTimestamp() != null
						&& wtsfFinaltermin.getTimestamp().before(wtsfLiefertermin.getTimestamp())) {
					wtsfFinaltermin.setTimestamp(wtsfLiefertermin.getTimestamp());
				}

				// if (wdfFinaltermin.getDate() != null
				// && wdfLiefertermin.getDate() != null) {
				// if (wdfFinaltermin.getDate().before(wdfLiefertermin.getDate())) {
				// wdfFinaltermin.setDate(wdfLiefertermin.getDate());
				// }
				// }
			} else if (evt.getSource() == wtfWaehrung && evt.getPropertyName().equals("value")) {
				setzeWechselkurs();
			}
		} catch (Throwable t) {
			myLogger.error(t.getLocalizedMessage(), t); // @todo sanieren
			// wie bei den
			// anderen Panels...
			// PJ 4809
		}
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;

		switch (exfc.getICode()) {
		case EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT:
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("auft.mandant.hauptlager_fehlt"));
			break;

		case EJBExceptionLP.FEHLER_KEIN_PARTNER_GEWAEHLT:
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
			break;

		case EJBExceptionLP.FEHLER_BELEG_WURDE_NICHT_MANUELL_ERLEDIGT:
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.manuellerledigen"));
			break;

		default:
			bErrorErkannt = false;
			break;
		}

		return bErrorErkannt;
	}

	private String formatAuftraege(AuftragDto[] aAuftragDtoI) {
		String cFormat = "";

		// nach jeweils 4 Abrufen einen Zeilenumbruch einfuegen
		int iAnzahl = 0;

		if (aAuftragDtoI != null && aAuftragDtoI.length > 0) {
			for (int i = 0; i < aAuftragDtoI.length; i++) {

				if (aAuftragDtoI[i].getStatusCNr().equals(LocaleFac.STATUS_STORNIERT)) {
					cFormat += "(" + aAuftragDtoI[i].getCNr() + ")";
				} else {
					cFormat += aAuftragDtoI[i].getCNr();
				}

				iAnzahl++;

				if (iAnzahl == 5) {
					cFormat += "\n";
					iAnzahl = 0;
				} else {
					cFormat += " | ";
				}
			}
		}

		if (cFormat.length() > 3 && iAnzahl != 0) {
			cFormat = cFormat.substring(0, cFormat.length() - 3);
		}

		return cFormat;
	}

	private String formatKostenstellen(AuftragkostenstelleDto[] auftragkostenstelleDto) throws Throwable {
		String cFormat = "";

		// nach jeweils 4 Abrufen einen Zeilenumbruch einfuegen
		int iAnzahl = 0;

		if (auftragkostenstelleDto != null && auftragkostenstelleDto.length > 0) {
			for (int i = 0; i < auftragkostenstelleDto.length; i++) {

				KostenstelleDto kDto = DelegateFactory.getInstance().getSystemDelegate()
						.kostenstelleFindByPrimaryKey(auftragkostenstelleDto[i].getKostenstelleIId());
				cFormat += kDto.formatKostenstellenbezeichnung();

				iAnzahl++;

				if (iAnzahl == 5) {
					cFormat += "\n";
					iAnzahl = 0;
				} else {
					cFormat += " | ";
				}
			}
		}

		if (cFormat.length() > 3 && iAnzahl != 0) {
			cFormat = cFormat.substring(0, cFormat.length() - 3);
		}

		return cFormat;
	}

	private void setVisisbleAbrufausrahmen(boolean bVisible) {
		wbuRahmenauswahl.setVisible(bVisible);
		wtfRahmencnr.setVisible(bVisible);
		wtfRahmenbez.setVisible(bVisible);
	}

	private void setVisibleAbrufe(boolean bVisible, boolean bUnterkostenstellen) {
		wlaAbrufe.setVisible(bVisible);
		wtaAbrufe.setVisible(bVisible);

		if (bUnterkostenstellen) {
			wlaAbrufe.setText(LPMain.getTextRespectUISPr("auft.kostenstellen"));
		} else {
			wlaAbrufe.setText(LPMain.getTextRespectUISPr("bes.abrufe"));
		}

	}

	private void abrufCheckBoxSetzen() {
		wnfDivisor.setVisible(true);
		wnfDivisor.setMandatoryField(true);

		if (wcbManuellAbruf.isSelected() || wcbRest.isSelected()) {
			wnfDivisor.setVisible(false);
			wnfDivisor.setMandatoryField(false);
		}
		jPanelAbrufBes.updateUI();
	}

	private void createAbrufPanel() throws ExceptionLP {

		wnfDivisor = new WrapperNumberField();
		wnfDivisor.setMaximumIntegerDigits(3);
		wnfDivisor.setFractionDigits(0);
		wnfDivisor.setMaximumValue(999);
		wnfDivisor.setMinimumValue(1);
		wnfDivisor.setHorizontalAlignment(SwingConstants.LEFT);

		wcbDivisor = new WrapperRadioButton(LPMain.getTextRespectUISPr("bes.abruf_bes.divisor"));
		wcbRest = new WrapperRadioButton(LPMain.getTextRespectUISPr("bes.abruf_bes.rest"));
		wcbManuellAbruf = new WrapperRadioButton(LPMain.getTextRespectUISPr("bes.abruf_bes.manuel"));

		wcbDivisor.addActionListener(this);
		wcbRest.addActionListener(this);
		wcbManuellAbruf.addActionListener(this);

		jbgAbrufBes = new ButtonGroup();
		jbgAbrufBes.add(wcbDivisor);
		jbgAbrufBes.add(wcbRest);
		jbgAbrufBes.add(wcbManuellAbruf);
		ParametermandantDto mandantParam = null;
		try {
			mandantParam = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_ABRUFE_DEFAULT_ABRUFART_AUFTRAG);
		} catch (Throwable e) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"),
					LPMain.getTextRespectUISPr("lp.abrufemenge.parameter.inkorrekt"));
		}
		if (mandantParam != null) {
			String sParamValue = mandantParam.getCWert().toUpperCase();
			if (sParamValue.equals("REST")) {
				wcbRest.setSelected(true);
			} else if (sParamValue.equals("MANUELL")) {
				wcbManuellAbruf.setSelected(true);
			} else if (sParamValue.equals("DIVISOR")) {
				wcbDivisor.setSelected(true);
			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"),
						LPMain.getTextRespectUISPr("lp.abrufemenge.parameter.inkorrekt"));
			}
		}

		wlaLeerAbruf = new WrapperLabel(LPMain.getTextRespectUISPr("bes.abruf_bes"));
		wlaLeerAbruf.setHorizontalAlignment(SwingConstants.LEFT);

		jPanelAbrufBes = new JPanel(new GridBagLayout());

		jPanelAbrufBes.add(wlaLeerAbruf, new GridBagConstraints(0, 0, 1, 1, 0.12, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jPanelAbrufBes.add(wcbDivisor, new GridBagConstraints(1, 0, 1, 1, 0.09, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jPanelAbrufBes.add(wnfDivisor, new GridBagConstraints(2, 0, 1, 1, 0.005, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jPanelAbrufBes.add(new WrapperLabel(), new GridBagConstraints(3, 0, 1, 1, 0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jPanelAbrufBes.add(wcbManuellAbruf, new GridBagConstraints(4, 0, 1, 1, 0.07, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jPanelAbrufBes.add(wcbRest, new GridBagConstraints(5, 0, 1, 1, 0.07, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jPanelAbrufBes.add(new WrapperLabel(), new GridBagConstraints(6, 0, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		abrufCheckBoxSetzen();

	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (tpAuftrag.getAuftragDto().getIId() != null && lockStateValue.getIState() != PanelBasis.LOCK_FOR_NEW
				&& lockStateValue.getIState() != PanelBasis.LOCK_IS_LOCKED_BY_ME
				&& lockStateValue.getIState() != PanelBasis.LOCK_IS_LOCKED_BY_OTHER_USER) {
			if (tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
				// Drucken kann man nur, wenn die Konditionen und
				// mengenbehaftete Positionen erfasst wurden
				// if (tpAuftrag.getAuftragDto().getAuftragIIdKopftext() != null
				// && tpAuftrag.getAuftragDto().getAuftragIIdFusstext() != null
				// && DelegateFactory
				// .getInstance()
				// .getAuftragpositionDelegate()
				// .getAnzahlMengenbehafteteAuftragpositionen(
				// tpAuftrag.getAuftragDto().getIId()) > 0) {
				if (DelegateFactory.getInstance().getAuftragpositionDelegate()
						.getAnzahlMengenbehafteteAuftragpositionen(tpAuftrag.getAuftragDto().getIId()) > 0) {
					lockStateValue = new LockStateValue(PanelBasis.LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY);
				}
			} else if (tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)
					|| tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)
					|| tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
				// Button Stornieren ist nicht verfuegbar, in diesen Faellen
				// wird ein echtes Update ausgeloest
				lockStateValue = new LockStateValue(PanelBasis.LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY);
			}
		}

		return lockStateValue;
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI) throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

		if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
			this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START).getButton().setEnabled(true);
		}
		if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
			this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_STOP).getButton().setEnabled(true);
		}
		if (tpAuftrag.getAuftragDto() != null && tpAuftrag.getAuftragDto().getStatusCNr() != null
				&& tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
			if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
				this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START).getButton().setEnabled(false);
			}
			if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
				this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_STOP).getButton().setEnabled(false);
			}
		}
		jbuSetNull.setEnabled(true);
		if (tpAuftrag.getAuftragDto() != null && tpAuftrag.getAuftragDto().getAngebotIId() != null) {
			wbuAngebot.getWrapperButtonGoTo().setEnabled(true);
		}

		updateOrderResponseButton();
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {

		if (bSchnellanlage == true && tpAuftrag.getAuftragDto().getIId() != null) {
			DelegateFactory.getInstance().getAuftragDelegate().storniereAuftrag(tpAuftrag.getAuftragDto());
			super.eventActionDiscard(e);
			eventYouAreSelected(false);
		} else {
			super.eventActionDiscard(e);
		}

		bSchnellanlage = false;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		if (tpAuftrag.getAuftragDto().getIId() == null) {
			return jButtonKunde;
		} else {
			return wtfProjekt;
		}
	}
}
