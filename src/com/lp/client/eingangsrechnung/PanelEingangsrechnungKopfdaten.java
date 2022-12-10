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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.finanz.PanelSelectReversechargeart;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.DialogReversechargeGeaendertER;
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
import com.lp.client.frame.component.WrapperGotoLieferantMapButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperLabelOverlay;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.EingangsrechnungDelegate;
import com.lp.client.frame.delegate.FinanzDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.IPartnerDto;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDto;
import com.lp.server.finanz.service.ExportdatenDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.UvaartDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.CoinRoundingResult;
import com.lp.server.system.service.BelegDatumClient;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.GotoHelper;
import com.lp.util.Helper;

/*
 * <p>Diese Klasse kuemmert sich um die Kopfdaten der Eingangsrechnung</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.42 $
 */
public class PanelEingangsrechnungKopfdaten extends PanelBasis implements PropertyChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String MEHRFACH = "mehrfach";
	private BestellungDto bestellungDto = null;
	private KostenstelleDto kostenstelleDto = null;
	private ZahlungszielDto zahlungszielDto = null;
	private KontoDto kontoDto = null;
	private PanelQueryFLR panelQueryFLRWaehrung = null;
	private boolean bFibuInstalliert = false;
	private int iStellenLieferantenrechnungsnummer = 0;
	private boolean bReversechargeVerwenden = true;
	private boolean bIstModulKostenstelleInstalliert = false;

	public final static String ACTION_SPECIAL_ART = "action_special_er_art";
	public final static String ACTION_SPECIAL_MWST = "action_special_er_mwst";
	public final static String ACTION_SPECIAL_WAEHRUNG = "action_special_er_waehrung";
	public final static String ACTION_SPECIAL_BESTELLUNG = "action_special_er_bestellung";
	public final static String ACTION_SPECIAL_LIEFERANT = "action_special_er_lieferant";
	public final static String ACTION_SPECIAL_KONTO = "action_special_er_konto";
	public final static String ACTION_SPECIAL_KOSTENSTELLE = "action_special_er_kostenstelle";
	public final static String ACTION_SPECIAL_ZAHLUNGSZIEL = "action_special_er_zahlungsziel";
	public final static String ACTION_SPECIAL_MEHRFACH = "action_special_er_mehrfach";
	public final static String ACTION_SPECIAL_HAT_POSITIONEN = "action_special_hat_positionen";
	public final static String ACTION_SPECIAL_IGERWERB = "action_special_igerwerb";
	public final static String ACTION_SPECIAL_REVERSECHARGE = "action_special_reversecharge";

	public final static String MY_OWN_NEW_TOGGLE_WIEDERHOLEND_ERLEDIGT = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_WIEDERHOLEND_ERLEDIGT";

	public final static String MY_OWN_NEW_TOGGLE_ZOLLIMPORTPAPIER_ERHALTEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_ZOLLIMPORTPAPIER_ERHALTEN";

	public final static String MY_OWN_NEW_TOGGLE_PRUEFEN = PanelBasis.ACTION_MY_OWN_NEW + "MY_OWN_NEW_PRUEFEN";

	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "action_kundelieferadresse_from_liste";

	private PanelQueryFLR panelQueryFLRLieferant = null;
	private PanelQueryFLR panelQueryFLRBestellung = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;
	private PanelQueryFLR panelQueryFLRZahlungsziel = null;
	private PanelQueryFLR panelQueryFLRKonto = null;

	// private WrapperCheckBox wcbReversecharge = null;
	private WrapperCheckBox wcbIGErwerb = null;
	private TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung = null;
	private JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private WrapperGotoButton wbuLieferant = null;
	private WrapperTextField wtfLieferant = new WrapperTextField();
	private WrapperButton wbuKonto = new WrapperButton();
	private WrapperTextField wtfKontoNummer = new WrapperTextField();
	private WrapperLabel wlaArt = new WrapperLabel();
	private WrapperComboBox wcoArt = new WrapperComboBox();
	private WrapperLabel wlaBelegdatum = new WrapperLabel();
	private WrapperLabel wlaFreigabedatum = new WrapperLabel();
	private WrapperDateField wdfBelegdatum = null;
	private WrapperDateField wdfFreigabedatum = new WrapperDateField();
	private WrapperLabel wlaText = new WrapperLabel();
	private WrapperLabel wlaWEArtikel = new WrapperLabel();

	private WrapperLabel wlaFibuExportDatum = new WrapperLabel();

	private WrapperLabel wlaKreditorennummer = new WrapperLabel();

	private JLabel wlaGeprueft = new JLabel();

	private JTextField wtnfLieferantenrechnungsnummer = null;
	private WrapperTextField wtfText = new WrapperTextField();
	private WrapperTextField wtfWEArtikel = new WrapperTextField();
	private WrapperLabel wlaBetrag = new WrapperLabel();
	private WrapperNumberField wnfBetrag = new WrapperNumberField();
	private WrapperButton wbuWaehrung = new WrapperButton();
	public WrapperLabel wlaWaehrung1 = new WrapperLabel();
	public WrapperLabel wlaWaehrung2 = new WrapperLabel();
	private WrapperTextField wtfWaehrung = new WrapperTextField();
	private WrapperLabel wlaKurs = new WrapperLabel();
	private WrapperNumberField wnfKurs = new WrapperNumberField();
	private WrapperLabel wlaMwst = new WrapperLabel();
	private WrapperComboBox wcoMwst = new WrapperComboBox();

	private WrapperNumberField wnfMwst = new WrapperNumberField();
	private WrapperButton wbuBestellung = new WrapperButton();
	private WrapperTextField wtfBestellung = new WrapperTextField();
	private WrapperButton wbuKostenstelle = new WrapperButton();
	private WrapperTextField wtfKostenstelleNummer = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
	private WrapperButton wbuZahlungsziel = new WrapperButton();
	private WrapperTextField wtfZahlungsziel = new WrapperTextField();
	private JLabel wlaZollimportpapiere = new JLabel();
	private WrapperLabel wlaWiederholendErledigt = new WrapperLabel();

	private WrapperTextField wtfKostenstelleBezeichnung = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
	private WrapperCheckBox wcbMehrfachkontierung = new WrapperCheckBox();
	private WrapperCheckBox wcbHatPositionen = new WrapperCheckBox();

	private WrapperLabel wlaLieferantenrechnungsnummer = null;
	private WrapperTextField wtfAdresse = new WrapperTextField();
	private WrapperTextField wtfAbteilung = new WrapperTextField();
	private WrapperLabel wlaAbteilung = new WrapperLabel();
	private WrapperTextField wtfKundendaten = new WrapperTextField();
	private WrapperLabel wlaKundendaten = new WrapperLabelOverlay();

	private WrapperLabel wlaWiederholungsintervall = null;
	private WrapperComboBox wcoWiederholungsintervall = null;
	private WrapperLabel wlaNachfolger = new WrapperLabel();
	private WrapperTextField wtfNachfolger = new WrapperTextField();

	private WrapperLabel wlaAnzahlungen;
	private WrapperTextField wtfAnzahlungen;

	private WrapperGotoButton wbuKunde = null;
	private PanelQueryFLR panelQueryFLRKundenauswahl = null;

	private WrapperLabel wlaNochNichtKontiert = new WrapperLabel();

	private boolean bMapSetAktiv = false;

	private boolean bAusgangsgutschriftAnKunde = false;
	private PanelSelectReversechargeart panelReversechargeart;
	private IBetragER betragER;
	private BigDecimal originalNettoBetrag;
	private BigDecimal originalUstBetrag;

	private WrapperGotoButton wbuPersonalAbwBankverbindung = new WrapperGotoButton(GotoHelper.GOTO_PERSONAL_AUSWAHL);
	private WrapperTextField wtfPersonalAbwBankverbindung = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRPersonal = null;
	private final static String ACTION_SPECIAL_PERSONAL_AUSWAHL = "action_special_personal_auswahl";

	public PanelEingangsrechnungKopfdaten(InternalFrame internalFrame, String add2TitleI, Object key,
			TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneEingangsrechnung = tabbedPaneEingangsrechnung;
		jbInit();
		initPanel();
		initComponents();
		initListeners();
	}

	private TabbedPaneEingangsrechnung getTabbedPane() {
		return tabbedPaneEingangsrechnung;
	}

	private void initListeners() {
		wcoArt.addActionListener(this);
		wcoMwst.addActionListener(this);
	}

	private void dialogQueryPersonal() throws Throwable {
		panelQueryFLRPersonal = PersonalFilterFactory.getInstance().createPanelFLRPersonal(getInternalFrame(), false,
				true);
		if (getTabbedPane().getEingangsrechnungDto() != null) {
			panelQueryFLRPersonal
					.setSelectedId(getTabbedPane().getEingangsrechnungDto().getPersonalIIdAbwBankverbindung());
		}
		new DialogQuery(panelQueryFLRPersonal);
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		wlaLieferantenrechnungsnummer = new WrapperLabel();

		panelReversechargeart = new PanelSelectReversechargeart(getInternalFrame(), "");

		// Texte
		wbuLieferant = new WrapperGotoLieferantMapButton(new IPartnerDto() {
			public PartnerDto getPartnerDto() {
				return getTabbedPane().getLieferantDto() == null ? null
						: getTabbedPane().getLieferantDto().getPartnerDto();
			}
		});
		wbuLieferant.setText(LPMain.getTextRespectUISPr("button.lieferant"));
		wbuLieferant.setToolTipText(LPMain.getTextRespectUISPr("button.lieferant.tooltip"));
		wbuBestellung.setText(LPMain.getTextRespectUISPr("button.bestellung"));
		wbuBestellung.setToolTipText(LPMain.getTextRespectUISPr("button.bestellung.tooltip"));
		wbuKostenstelle.setText(LPMain.getTextRespectUISPr("button.kostenstelle"));
		wbuKostenstelle.setToolTipText(LPMain.getTextRespectUISPr("button.kostenstelle.tooltip"));
		wbuZahlungsziel.setText(LPMain.getTextRespectUISPr("button.zahlungsziel"));
		wbuZahlungsziel.setToolTipText(LPMain.getTextRespectUISPr("button.zahlungsziel.tooltip"));
		wbuKonto.setText(LPMain.getTextRespectUISPr("button.konto"));
		wbuKonto.setToolTipText(LPMain.getTextRespectUISPr("button.konto.tooltip"));
		wlaAbteilung.setText(LPMain.getTextRespectUISPr("lp.abteilung"));
		wlaArt.setText(LPMain.getTextRespectUISPr("label.art"));
		// Setzt somit (beabsichtigt) die Breite der ersten Spalte
		HelperClient.setMinimumAndPreferredSize(wlaArt, HelperClient.getSizeFactoredDimension(140));

		wlaKreditorennummer.setText(LPMain.getTextRespectUISPr("er.kreditorennr") + ":");

		wlaBelegdatum.setText(LPMain.getTextRespectUISPr("label.belegdatum"));
		wlaFreigabedatum.setText(LPMain.getTextRespectUISPr("label.freigabedatum"));
		wlaText.setText(LPMain.getTextRespectUISPr("label.text"));
		wtfWEArtikel.setColumnsMax(80);
		wlaWEArtikel.setText(LPMain.getTextRespectUISPr("er.weartikel"));
		wbuWaehrung.setText(LPMain.getTextRespectUISPr("label.waehrung"));
		wlaKurs.setText(LPMain.getTextRespectUISPr("label.kurs"));
		wlaBetrag.setText(LPMain.getTextRespectUISPr("label.bruttobetrag"));
		wlaMwst.setText(LPMain.getTextRespectUISPr("label.mwst"));
		wlaNochNichtKontiert.setText(LPMain.getTextRespectUISPr("er.nochnichtvollstaendigkontiert"));
		wlaNochNichtKontiert.setForeground(Color.RED);
		wlaLieferantenrechnungsnummer.setText(LPMain.getTextRespectUISPr("label.lieferantenrechnungsnummer"));

		wcbMehrfachkontierung.setText(LPMain.getTextRespectUISPr("er.checkbox.mehrfach"));
		wcbMehrfachkontierung.setToolTipText(LPMain.getTextRespectUISPr("er.checkbox.mehrfach.tooltip"));
		wcbHatPositionen.setText(LPMain.getTextRespectUISPr("er.mitpositionen"));

		wbuPersonalAbwBankverbindung.setText(LPMain.getTextRespectUISPr("er.kopfdaten.abwbankverbindung"));

		// Setzt somit (beabsichtigt) auch die Breite der dritten Spalte
		HelperClient.setMinimumAndPreferredSize(wcbHatPositionen, HelperClient.getSizeFactoredDimension(150));
		// sizes
		// wcbReversecharge = new WrapperCheckBox();
		// wcbReversecharge
		// .setText(LPMain.getTextRespectUISPr("lp.reversecharge"));
		// wcbReversecharge.setActionCommand(ACTION_SPECIAL_REVERSECHARGE);
		// wcbReversecharge.addActionListener(this);

		panelReversechargeart.getWcoReversechargeart().setActionCommand(ACTION_SPECIAL_REVERSECHARGE);
		panelReversechargeart.getWcoReversechargeart().addActionListener(this);

		ParametermandantDto parametermandantRCDto = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_REVERSE_CHARGE_VERWENDEN);
		bReversechargeVerwenden = (Boolean) parametermandantRCDto.getCWertAsObject();

		parametermandantRCDto = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
				ParameterFac.PARAMETER_AUSGANGSGUTSCHRIFT_AN_KUNDE);
		bAusgangsgutschriftAnKunde = (Boolean) parametermandantRCDto.getCWertAsObject();

		wcbIGErwerb = new WrapperCheckBox();
		wcbIGErwerb.setText(LPMain.getTextRespectUISPr("lp.igerwerb"));
		wcbIGErwerb.setActionCommand(ACTION_SPECIAL_IGERWERB);
		wcbIGErwerb.addActionListener(this);

		wlaWiederholungsintervall = new WrapperLabel(LPMain.getTextRespectUISPr("lp.wiederholungsintervall"));
		wcoWiederholungsintervall = new WrapperComboBox();

		wlaKundendaten.setText(LPMain.getTextRespectUISPr("er.kundendaten"));
		wlaKundendaten.setHorizontalAlignment(SwingConstants.RIGHT);

		// beschraenkungen

		wnfKurs.setFractionDigits(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS);
		wnfKurs.setMaximumIntegerDigits(LocaleFac.ANZAHL_VORKOMMASTELLEN_WECHSELKURS);
		wlaWaehrung1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
		// activatable
		wtfLieferant.setActivatable(false);
		wtfBestellung.setActivatable(false);
		wnfKurs.setActivatable(false);
		wnfMwst.setActivatable(false);
		wtfAbteilung.setActivatable(false);
		wtfAdresse.setActivatable(false);
		wtfKostenstelleBezeichnung.setActivatable(false);
		wtfKostenstelleNummer.setActivatable(false);

		wtfKontoNummer.setActivatable(false);
		wtfZahlungsziel.setActivatable(false);
		wtfWaehrung.setActivatable(false);
		// dependence
		wnfBetrag.setDependenceField(true);
		wnfMwst.setDependenceField(true);
		// mandatory
		wcoArt.setMandatoryFieldDB(true);
		wcoMwst.setMandatoryFieldDB(true);
		wtfWaehrung.setMandatoryFieldDB(true);
		wtfLieferant.setMandatoryFieldDB(true);

		wdfBelegdatum = new WrapperBelegDateField(
				new BelegDatumClient(LPMain.getTheClient().getMandant(), new GregorianCalendar(), true));
		wdfBelegdatum.setMandatoryFieldDB(true);
		wdfBelegdatum.addPropertyChangeListener(this);

		wlaNachfolger.setText(LPMain.getTextRespectUISPr("er.nachfolger"));
		wtfNachfolger.setActivatable(false);

		wdfFreigabedatum.setMandatoryFieldDB(true);
		wtfKostenstelleNummer.setMandatoryField(true);
		wtfKontoNummer.setMandatoryField(true);
		wtfKontoNummer.setColumnsMax(15 + 40);
		wnfBetrag.setMandatoryFieldDB(true);
		// Commands
		wcoArt.setActionCommand(ACTION_SPECIAL_ART);
		wcoMwst.setActionCommand(ACTION_SPECIAL_MWST);
		wbuWaehrung.setActionCommand(ACTION_SPECIAL_WAEHRUNG);
		wbuWaehrung.addActionListener(this);
		wbuBestellung.setActionCommand(ACTION_SPECIAL_BESTELLUNG);
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
		wbuKonto.setActionCommand(ACTION_SPECIAL_KONTO);
		wbuLieferant.setActionCommand(ACTION_SPECIAL_LIEFERANT);
		wbuZahlungsziel.setActionCommand(ACTION_SPECIAL_ZAHLUNGSZIEL);
		wcbMehrfachkontierung.setActionCommand(ACTION_SPECIAL_MEHRFACH);
		wcbHatPositionen.setActionCommand(ACTION_SPECIAL_HAT_POSITIONEN);
		// max
		wtfLieferant.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfAdresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		// Listener
//		wcoArt.addActionListener(this);
//		wcoMwst.addActionListener(this);
		wtfWaehrung.addActionListener(this);
		wbuBestellung.addActionListener(this);
		wbuKostenstelle.addActionListener(this);
		wbuKonto.addActionListener(this);
		wbuLieferant.addActionListener(this);
		wbuZahlungsziel.addActionListener(this);
		wcbMehrfachkontierung.addActionListener(this);
		wcbHatPositionen.addActionListener(this);
		wnfBetrag.addFocusListener(new PanelEingangsrechnungKopfdaten_wnfBetrag_focusAdapter(this));

		bFibuInstalliert = DelegateFactory.getInstance().getMandantDelegate()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG);

		wcbIGErwerb.setActivatable(!bFibuInstalliert); // bei Fibu nicht
														// aenderbar, da aus
														// Kreditorenkonto
		wbuKunde = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_KUNDE_AUSWAHL);
		wbuKunde.setText(LPMain.getTextRespectUISPr("button.kunde"));
		wbuKunde.setActionCommand(ACTION_SPECIAL_KUNDE_FROM_LISTE);
		wbuKunde.addActionListener(this);
		wbuKunde.setVisible(false);

		// PJ18303

		wlaAnzahlungen = new WrapperLabel(LPMain.getTextRespectUISPr("rech.zahlung.anzahlungen"));
		wtfAnzahlungen = new WrapperTextField(9999);
		wtfAnzahlungen.setActivatable(false);

		wbuPersonalAbwBankverbindung.setText(LPMain.getTextRespectUISPr("er.kopfdaten.abwbankverbindung"));
		wbuPersonalAbwBankverbindung.addActionListener(this);
		wbuPersonalAbwBankverbindung.setActionCommand(ACTION_SPECIAL_PERSONAL_AUSWAHL);

		wtfPersonalAbwBankverbindung.setActivatable(false);

		prepareLieferantenRechnungsnummerField();
		/*
		 * ParametermandantDto parametermandantDto =
		 * DelegateFactory.getInstance().getParameterDelegate()
		 * .getMandantparameter(LPMain.getTheClient().getMandant(),
		 * ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
		 * ParameterFac.PARAMETER_EINGANGSRECHNUNG_LIEFERANTENRECHNUNGSNUMMER_LAENGE);
		 * iStellenLieferantenrechnungsnummer =
		 * Integer.parseInt(parametermandantDto.getCWert());
		 * 
		 * if (iStellenLieferantenrechnungsnummer == -1) {
		 * wtnfLieferantenrechnungsnummer = new WrapperTextField(20); } else {
		 * wtnfLieferantenrechnungsnummer = new WrapperTextNumberField(); }
		 * 
		 * if (wtnfLieferantenrechnungsnummer instanceof WrapperTextNumberField) {
		 * ((WrapperTextNumberField)
		 * wtnfLieferantenrechnungsnummer).setMandatoryField(true); } else if
		 * (wtnfLieferantenrechnungsnummer instanceof WrapperTextField) {
		 * ((WrapperTextField) wtnfLieferantenrechnungsnummer).setMandatoryField(true);
		 * }
		 * 
		 * if (bFibuInstalliert) {
		 * 
		 * if (wtnfLieferantenrechnungsnummer instanceof WrapperTextNumberField) {
		 * ((WrapperTextNumberField)
		 * wtnfLieferantenrechnungsnummer).setMaximumDigits(20); } else if
		 * (wtnfLieferantenrechnungsnummer instanceof WrapperTextField) {
		 * ((WrapperTextField) wtnfLieferantenrechnungsnummer).setColumnsMax(20); }
		 * 
		 * } else { if (iStellenLieferantenrechnungsnummer != -1) { if
		 * (wtnfLieferantenrechnungsnummer instanceof WrapperTextNumberField) {
		 * ((WrapperTextNumberField) wtnfLieferantenrechnungsnummer)
		 * .setMaximumDigits(iStellenLieferantenrechnungsnummer); } else if
		 * (wtnfLieferantenrechnungsnummer instanceof WrapperTextField) {
		 * ((WrapperTextField) wtnfLieferantenrechnungsnummer)
		 * .setColumnsMax(iStellenLieferantenrechnungsnummer);
		 * 
		 * } }
		 * 
		 * }
		 */
		// Actionpanel
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_STORNIEREN, ACTION_DISCARD, ACTION_PRINT };
		enableToolsPanelButtons(aWhichButtonIUse);
		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		getToolBar().getToolsPanelRight().add(wlaZollimportpapiere);
		// jpaWorkingOn.add(wlaZollimportpapiere, new GridBagConstraints(2,
		// iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
		// GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaArt, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoArt, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaWiederholendErledigt, new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		if (!tabbedPaneEingangsrechnung.isBZusatzkosten()) {
			iZeile++;
			jpaWorkingOn.add(wbuBestellung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wtfBestellung, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}
		iZeile++;
		jpaWorkingOn.add(wbuLieferant, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuKunde, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLieferant, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wtfAdresse, new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaAbteilung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAbteilung, new GridBagConstraints(1, iZeile, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuKostenstelle, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleNummer, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleBezeichnung, new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbMehrfachkontierung, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		if (!tabbedPaneEingangsrechnung.isBZusatzkosten()) {

			jpaWorkingOn.add(wcbHatPositionen, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}
		jpaWorkingOn.add(wlaNochNichtKontiert, new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuKonto, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKontoNummer, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKreditorennummer, new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuZahlungsziel, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfZahlungsziel, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuPersonalAbwBankverbindung, new GridBagConstraints(2, iZeile, 1, 1, 0.5, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPersonalAbwBankverbindung, new GridBagConstraints(3, iZeile, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBelegdatum, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBelegdatum, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFreigabedatum, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfFreigabedatum, new GridBagConstraints(3, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaLieferantenrechnungsnummer, new GridBagConstraints(0, iZeile, 1, 1, 0.5, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtnfLieferantenrechnungsnummer, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKundendaten, new GridBagConstraints(2, iZeile, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKundendaten, new GridBagConstraints(3, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaText, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfText, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaWEArtikel, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfWEArtikel, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBetrag, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetrag, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung1, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaFibuExportDatum, new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuWaehrung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfWaehrung, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKurs, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKurs, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaMwst, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoMwst, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		if (bReversechargeVerwenden == true) {
			jpaWorkingOn.add(panelReversechargeart.getWlaReversechargeart(), new GridBagConstraints(2, iZeile, 1, 1,
					0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(panelReversechargeart.getWcoReversechargeart(), new GridBagConstraints(3, iZeile, 1, 1,
					0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			// jpaWorkingOn.add(wcbReversecharge, new GridBagConstraints(3,
			// iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
			// GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wcbIGErwerb, new GridBagConstraints(3, iZeile + 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		iZeile++;
		jpaWorkingOn.add(wnfMwst, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung2, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		if (tabbedPaneEingangsrechnung.isBZusatzkosten()) {
			iZeile++;
			jpaWorkingOn.add(wlaWiederholungsintervall, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wcoWiederholungsintervall, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			iZeile++;
			jpaWorkingOn.add(wlaNachfolger, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wtfNachfolger, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}
		iZeile++;
		jpaWorkingOn.add(wlaAnzahlungen, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAnzahlungen, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		if (tabbedPaneEingangsrechnung.isBZusatzkosten()) {
			getToolBar().addButtonCenter("/com/lp/client/res/check2.png",
					LPMain.getTextRespectUISPr("er.zusatzkosten.wiederholungerledigen"),
					MY_OWN_NEW_TOGGLE_WIEDERHOLEND_ERLEDIGT, null, null);
		} else {
			getToolBar().addButtonCenter("/com/lp/client/res/document_preferences.png",
					LPMain.getTextRespectUISPr("er.zollimportpapiere.erhalten"),
					MY_OWN_NEW_TOGGLE_ZOLLIMPORTPAPIER_ERHALTEN, null, null);
		}

		if (!tabbedPaneEingangsrechnung.isBZusatzkosten() && getTabbedPane().iERPruefen > 0 && DelegateFactory
				.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_ER_DARF_EINGANGSRECHNUNGEN_PRUEFEN)) {

			getToolBar().addButtonLeft("/com/lp/client/res/check2.png",
					LPMain.getTextRespectUISPr("er.alsgeprueft.markieren"), MY_OWN_NEW_TOGGLE_PRUEFEN, null,
					RechteFac.RECHT_ER_DARF_EINGANGSRECHNUNGEN_PRUEFEN);

		}

		getToolBar().getToolsPanelLeft().add(wlaGeprueft);

	}

	private void prepareLieferantenRechnungsnummerField() throws Throwable {
		ParametermandantDto parametermandantDto = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
						ParameterFac.PARAMETER_EINGANGSRECHNUNG_LIEFERANTENRECHNUNGSNUMMER_LAENGE);
		iStellenLieferantenrechnungsnummer = Integer.parseInt(parametermandantDto.getCWert());

		if (iStellenLieferantenrechnungsnummer == -1) {
			prepareLRNrTextField(iStellenLieferantenrechnungsnummer);
		} else {
			prepareLRNrNumberField(iStellenLieferantenrechnungsnummer);
		}
	}

	private void prepareLRNrTextField(int maxStellen) {
		WrapperTextField tf = new WrapperTextField(20);
		tf.setMandatoryField(true);
		if (bFibuInstalliert) {
			tf.setColumnsMax(20);
		}
		wtnfLieferantenrechnungsnummer = tf;
	}

	private void prepareLRNrNumberField(int maxStellen) {
		WrapperTextNumberField tf = new WrapperTextNumberField();
		tf.setMandatoryField(true);
		tf.setMaximumDigits(bFibuInstalliert ? 20 : maxStellen);
		wtnfLieferantenrechnungsnummer = tf;
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_EINGANGSRECHNUNG;
	}

	/**
	 * Neue ER.
	 * 
	 * @param eventObject EventObject
	 * @param bLockMeI    boolean
	 * @param bNeedNoNewI boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, bLockMeI, bNeedNoNewI);
		getTabbedPane().setEingangsrechnungDto(null);
		getTabbedPane().setLieferantDto(null);
		this.bestellungDto = null;
		this.kostenstelleDto = null;
		this.zahlungszielDto = null;
		this.kontoDto = null;
		this.leereAlleFelder(this);
		wlaKreditorennummer.setText("");
		setDefaults();
		panelReversechargeart.reload();
		updateMehrfach();
		// noch nicht vollstaendig kontiert ausblenden
		wlaNochNichtKontiert.setVisible(false);

		this.clearStatusbar();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);

		// PJ18167
		LPButtonAction lpbaZoll = getHmOfButtons().get(MY_OWN_NEW_TOGGLE_ZOLLIMPORTPAPIER_ERHALTEN);
		if (lpbaZoll != null) {
			lpbaZoll.getButton().setVisible(false);
		}

		if (!bNeedNoYouAreSelectedI) {
			EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
			if (erDto != null) {
				getTabbedPane().setEingangsrechnungDto(DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.eingangsrechnungFindByPrimaryKey(erDto.getIId()));
				dto2Components();
				getTabbedPane().enablePanels();

				LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(erDto.getLieferantIId());
				if (lfDto != null && Helper.short2boolean(lfDto.getBZollimportpapier())) {
					if (lpbaZoll != null) {
						lpbaZoll.getButton().setVisible(true);
					}
				}

				// SP9433

				if (!getTabbedPane().isBDarfKontieren() && getTabbedPane().iERPruefen > 0
						&& DelegateFactory.getInstance().getTheJudgeDelegate()
								.hatRecht(RechteFac.RECHT_ER_DARF_EINGANGSRECHNUNGEN_PRUEFEN)) {
					if (getHmOfButtons().containsKey(MY_OWN_NEW_TOGGLE_PRUEFEN)) {
						LPButtonAction lpba = getHmOfButtons().get(MY_OWN_NEW_TOGGLE_PRUEFEN);
						lpba.getButton().setEnabled(true);
					}
				}

			} else {
				// NEU
				if (!getTabbedPane().isBDarfKontieren()) {
					wcbMehrfachkontierung.setSelected(true);
					updateMehrfach();
					wcbMehrfachkontierung.setEnabled(false);
				}
			}
		}
	}

	private void dialogQueryWaehrung(ActionEvent e) throws Throwable {
		String selectetWaehrung = null;
		if (getTabbedPane().getEingangsrechnungDto() != null) {
			selectetWaehrung = getTabbedPane().getEingangsrechnungDto().getWaehrungCNr();
		}

		panelQueryFLRWaehrung = FinanzFilterFactory.getInstance().createPanelFLRWaehrung(getInternalFrame(),
				selectetWaehrung);
		new DialogQuery(panelQueryFLRWaehrung);
	}

	private void dialogQueryKundeFromListe() throws Throwable {

		Integer kundeIId = null;
		// wenn schon ein Kunde gewaehlt wurde, dann den lieferanten dazu suchen
		if (getTabbedPane().getLieferantDto() != null && getTabbedPane().getLieferantDto().getPartnerIId() != null) {
			KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByiIdPartnercNrMandantOhneExc(
					getTabbedPane().getLieferantDto().getPartnerIId(), LPMain.getTheClient().getMandant());
			if (kundeDto != null) {
				kundeIId = kundeDto.getIId();
			}
		}

		panelQueryFLRKundenauswahl = PartnerFilterFactory.getInstance().createPanelFLRKunde(getInternalFrame(), true,
				false, kundeIId);

		new DialogQuery(panelQueryFLRKundenauswahl);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_BESTELLUNG)) {
			dialogQueryBestellung();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERANT)) {
			dialogQueryLieferant(e);
			setIGErwerbReverseCharge();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ZAHLUNGSZIEL)) {
			dialogQueryZahlungsziel(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_FROM_LISTE)) {
			dialogQueryKundeFromListe();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_WAEHRUNG)) {
			dialogQueryWaehrung(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_MWST)) {
			wnfBetrag_focusLost();

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_HAT_POSITIONEN)) {

			if (getTabbedPane().getEingangsrechnungDto() != null
					&& getTabbedPane().getEingangsrechnungDto().getIId() != null) {

				EingangsrechnungKontierungDto[] erDtos = DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.eingangsrechnungKontierungFindByEingangsrechnungIId(
								getTabbedPane().getEingangsrechnungDto().getIId());

				if (erDtos != null && erDtos.length > 0) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("er.mitpositionen.error"));

					wcbHatPositionen.setSelected(!wcbHatPositionen.isSelected());
					return;
				}
			}

			if (wcbHatPositionen.isSelected()) {
				wcoArt.setKeyOfSelectedItem(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG);
				wcoArt.setEnabled(false);
				wcbMehrfachkontierung.setSelected(true);
				wcbMehrfachkontierung.setEnabled(false);
				eventActionSpecial(new ActionEvent(this, 1, ACTION_SPECIAL_MEHRFACH));
				wnfBetrag.setBigDecimal(BigDecimal.ZERO);
				wnfBetrag.setEditable(false);
				wtnfLieferantenrechnungsnummer.setText("0");

				if (bAusgangsgutschriftAnKunde == true) {
					wbuKunde.setVisible(true);
					wbuKunde.setOKey(null);
					wtfLieferant.setText(null);
					wtfAdresse.setText(null);
					wtfAbteilung.setText(null);
					wbuLieferant.setVisible(false);
				} else {
					wbuKunde.setVisible(false);
					wbuLieferant.setVisible(true);
				}

			} else {
				wbuKunde.setVisible(false);
				wbuLieferant.setVisible(true);
				wcoArt.setEnabled(true);
				if (getTabbedPane().isBDarfKontieren()) {
					wcbMehrfachkontierung.setEnabled(true);
				}
				wnfBetrag.setEditable(true);

			}

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_MEHRFACH)) {
			updateMehrfach();
			// falls NICHT mehrfach, dann kst des lieferanten vorbesetzen
			if (wcbMehrfachkontierung.isSelected() == false) {
				// falls er eine hat
				if (getTabbedPane().getLieferantDto() != null
						&& getTabbedPane().getLieferantDto().getIIdKostenstelle() != null) {
					holeKostenstelle(getTabbedPane().getLieferantDto().getIIdKostenstelle());
				}

				prepareAnzahlung(wcoArt.getKeyOfSelectedItem().toString());
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ART)) {
			String sArt = wcoArt.getKeyOfSelectedItem().toString();

			wcbMehrfachkontierung.setEnabled(true);

			prepareAnzahlung(sArt);

			setVisibleBestellung(sArt);
			boolean schlusszahlung = sArt.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG);
			wlaAnzahlungen.setVisible(schlusszahlung);
			wtfAnzahlungen.setVisible(schlusszahlung);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO)) {
			dialogQueryKonto(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_IGERWERB)) {
			actionDoIGErwerbModified();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_REVERSECHARGE)) {
			actionDoReversechargeModified();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PERSONAL_AUSWAHL)) {
			dialogQueryPersonal();
		} else if (e.getActionCommand().equals(MY_OWN_NEW_TOGGLE_WIEDERHOLEND_ERLEDIGT)) {
			// PJ 17584
			if (getTabbedPane().getEingangsrechnungDto() != null
					&& getTabbedPane().getEingangsrechnungDto().getIId() != null) {
				DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.toggleWiederholdendErledigt(getTabbedPane().getEingangsrechnungDto().getIId());
				eventYouAreSelected(false);
			}
		} else if (e.getActionCommand().equals(MY_OWN_NEW_TOGGLE_ZOLLIMPORTPAPIER_ERHALTEN)) {
			// PJ 17696
			if (getTabbedPane().getEingangsrechnungDto() != null
					&& getTabbedPane().getEingangsrechnungDto().getIId() != null) {

				// SP1811

				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("er.exportbeleg.aendern.stufe1"));
				if (b == false) {
					return;
				}

				if (getTabbedPane().getEingangsrechnungDto().getTFibuuebernahme() != null) {

					boolean bChefbuchhalter = DelegateFactory.getInstance().getTheJudgeDelegate()
							.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER);

					if (bChefbuchhalter) {
						b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("er.exportbeleg.aendern.stufe2"));
						if (b == false) {
							return;
						}
					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("er.exportbeleg.aendern.keineberechtigung"));
						return;
					}

				}

				if (getTabbedPane().getEingangsrechnungDto().getTZollimportpapier() == null) {

					DialogZollbeleg d = new DialogZollbeleg(getTabbedPane().getEingangsrechnungDto().getIId(),
							getTabbedPane());
					LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);
				} else {
					DelegateFactory.getInstance().getEingangsrechnungDelegate().toggleZollimportpapiereErhalten(
							getTabbedPane().getEingangsrechnungDto().getIId(), null, null);
				}

				eventYouAreSelected(false);
			}
		} else if (e.getActionCommand().equals(MY_OWN_NEW_TOGGLE_PRUEFEN)) {
			// PJ 17558
			if (getTabbedPane().getEingangsrechnungDto().getIId() != null) {

				if (getTabbedPane().getEingangsrechnungDto().getTGeprueft() != null) {
					if (DialogFactory.showMeldung(

							LPMain.getTextRespectUISPr("er.geprueft.ruecknahme.frage"),
							LPMain.getTextRespectUISPr("lp.frage"),
							javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {

					} else {
						return;
					}

				}

				DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.toggleEingangsrechnungGeprueft(getTabbedPane().getEingangsrechnungDto().getIId());
				getTabbedPane().setEingangsrechnungDto(DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.eingangsrechnungFindByPrimaryKey(getTabbedPane().getEingangsrechnungDto().getIId()));

				eventYouAreSelected(false);
			}
		}
	}

	private void setVisibleBestellung(String sArt) {
		if (sArt.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG)
				|| sArt.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT)) {
			wbuBestellung.setVisible(false);
			wtfBestellung.setVisible(false);
			wtfBestellung.setMandatoryField(false);
		} else {
			wbuBestellung.setVisible(true);
			wtfBestellung.setVisible(true);
			wtfBestellung.setMandatoryField(true);
		}
	}

	/**
	 * PJ21120 Bei einer Anzahlungsrechnung soll für: a) Kontierungsrecht fehlt:
	 * Splitbuchung default gesetzt werden, ausserdem darf das Häkchen für
	 * Splitbuchung nicht geändert werden b) Kontierungsrecht vorhanden (also
	 * Buchhalter): Eine Anzahlungseingangsrechnung muss immer auf das Anzahlungs
	 * Gegeben Konto gehen. Daher wird eine Kontobuchung auf das AnzahlungsGegeben
	 * Konto vorbesetzt. Die Nicht-Splittbuchung darf nicht(!) geaendert werden. Das
	 * Konto schon. c) Da Splittbuchung Häkchen automatisch geaendert wird, mus
	 * auch entsprechende Kostenstellenbehandlung gemacht werden.
	 * 
	 * @param artCnr
	 * @throws Throwable
	 */
	private void prepareAnzahlung(String artCnr) throws Throwable {
		boolean oldMehrfachkontierung = wcbMehrfachkontierung.isSelected();
		// PJ21120
		if (artCnr.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)) {
			myLogger.warn("Eingangsrechnungsart ANZAHLUNG");
//			wcbMehrfachkontierung.setEnabled(false);

			if (getTabbedPane().getLieferantDto() == null
					|| getTabbedPane().getLieferantDto().getPartnerDto() == null) {
				myLogger.warn("Skip Eingangsrechnungsart ANZAHLUNG, Lieferant/Partner null");
				return;
			}

			if (getTabbedPane().isBDarfKontieren()) {
				final FinanzDelegate delegate = DelegateFactory.getInstance().getFinanzDelegate();
				FinanzamtDto finanzamtDto = delegate.findFinanzamtForLieferant(getTabbedPane().getLieferantDto());
				Integer kontoId = finanzamtDto.getKontoIIdAnzahlungGegebenVerr();
				if (kontoId != null) {
					kontoDto = delegate.kontoFindByPrimaryKey(kontoId);
					wtfKontoNummer.setText(kontoDto.getCNr());
					wcbMehrfachkontierung.setSelected(false);
					wcbMehrfachkontierung.setEnabled(false);

					wtfKostenstelleNummer.setMandatoryField(bIstModulKostenstelleInstalliert);
					wbuKostenstelle.setEnabled(bIstModulKostenstelleInstalliert);
					if (bIstModulKostenstelleInstalliert
							&& getTabbedPane().getLieferantDto().getIIdKostenstelle() != null) {
						holeKostenstelle(getTabbedPane().getLieferantDto().getIIdKostenstelle());
					}
					wlaNochNichtKontiert.setVisible(false);
				} else {
					wcbMehrfachkontierung.setSelected(true);
					wtfKostenstelleNummer.setMandatoryField(false);
					wlaNochNichtKontiert.setVisible(true);
				}
			} else {
				wcbMehrfachkontierung.setSelected(true);
				wcbMehrfachkontierung.setEnabled(false);
				wtfKostenstelleNummer.setMandatoryField(false);
				wtfKostenstelleNummer.setText("");
				wtfKontoNummer.setText("");
				wlaNochNichtKontiert.setVisible(true);
			}

//			if (!getTabbedPane().isBDarfKontieren()) {
//				wcbMehrfachkontierung.setSelected(true);
//			}
		} else {
			if (!getTabbedPane().isBDarfKontieren()) {
				wcbMehrfachkontierung.setSelected(true);
				wcbMehrfachkontierung.setEnabled(false);
			} else {
				wcbMehrfachkontierung.setEnabled(true);
			}
		}

		if (oldMehrfachkontierung != wcbMehrfachkontierung.isSelected()) {
			updateMehrfach();
		}
	}

	private void actionDoIGErwerbModified() throws ExceptionLP, Throwable {
		if (wcbIGErwerb.isSelected()) {
			panelReversechargeart.setOhneAsSelected();
		}
		setIGErwerbReverseCharge();
	}

	private void actionDoReversechargeModified() throws ExceptionLP, Throwable {
		if (panelReversechargeart.hatReversecharge() && wcbIGErwerb.isSelected()) {
			wcbIGErwerb.setSelected(false);
		} else {
			if (bFibuInstalliert) {
				if (panelReversechargeart.isOhneSelected()) {
					LieferantDto lieferantDto = getTabbedPane().getLieferantDto();
					if (lieferantDto != null) {
						boolean istIgErwerb = DelegateFactory.getInstance().getFinanzServiceDelegate()
								.istIgErwerb(getTabbedPane().getLieferantDto().getKontoIIdKreditorenkonto());
						wcbIGErwerb.setSelected(istIgErwerb);
					}
				}
			}
		}

		setIGErwerbReverseCharge();
	}

	private void setIGErwerbReverseCharge() throws ExceptionLP, Throwable {
		myLogger.warn("setIGErwerb: betragBrutto:" + getBetragER().isBrutto() + ", ER brutto:"
				+ isEingangsrechnungBrutto() + ", original: " + originalNettoBetrag);
		if (getBetragER().isBrutto() != isEingangsrechnungBrutto()) {
			createBetragER();
			if (originalNettoBetrag != null) {
				if (!isEingangsrechnungBrutto()) {
					myLogger.warn("Setting explicit netto " + originalNettoBetrag.toPlainString());
					getBetragER().setBetrag(originalNettoBetrag);
				} else {
					myLogger.warn("the other way around");
					getBetragER().setBetrag(originalNettoBetrag.add(originalUstBetrag));
				}
			}
		}
		updateMwst();
	}

	// private void setIGErwerbReverseCharge() throws ExceptionLP, Throwable {
	// boolean selected = wcbIGErwerb.isSelected()
	// || panelReversechargeart.hatReversecharge();
	// if (selected) {
	// wlaBetrag.setText(LPMain
	// .getTextRespectUISPr("label.bruttobetrag.ig"));
	// wlaMwst.setText(LPMain.getTextRespectUISPr("label.mwst.ig"));
	// } else {
	// wlaBetrag.setText(LPMain.getTextRespectUISPr("label.bruttobetrag"));
	// wlaMwst.setText(LPMain.getTextRespectUISPr("label.mwst"));
	// }
	//
	// updateMwst();
	// }

	/**
	 * updateMehrfach.
	 * 
	 * @throws Throwable
	 */
	private void updateMehrfach() throws Throwable {
		if (wcbMehrfachkontierung.isSelected()) {
			wbuKostenstelle.setEnabled(false);
			wtfKostenstelleNummer.setMandatoryField(false);
			wbuKonto.setEnabled(false);
			wtfKontoNummer.setMandatoryField(false);
			kostenstelleDto = null;
			kontoDto = null;
			dto2ComponentsKostenstelle();
			dto2ComponentsKonto();
			wcoMwst.setMap(getMapSieheKontierung());
			wcoMwst.setEnabled(false);

			panelReversechargeart.setReversechargeKontierung();
			panelReversechargeart.setReversechargeEnabled(false);
		} else {
			panelReversechargeart.reload();
			// buttons nur aktiveren wenn die anderen auch aktiviert sind
			if (wbuLieferant.isEnabled()) {
				LockStateValue lockstateValue = super.getLockedstateDetailMainKey();
				if (!(lockstateValue.getIState() == LOCK_IS_NOT_LOCKED)) {

					wbuKostenstelle.setEnabled(true);
					wbuKonto.setEnabled(true);

					// panelReversechargeart.reload();
					panelReversechargeart.setReversechargeEnabled(true);
					if (getTabbedPane().getLieferantDto() == null) {
						panelReversechargeart.setOhneAsSelected();
					} else {
						panelReversechargeart
								.setReversechargeartId(getTabbedPane().getLieferantDto().getReversechargeartId());
					}
				}
			}
			wtfKostenstelleNummer.setMandatoryField(true && bIstModulKostenstelleInstalliert);
			wtfKontoNummer.setMandatoryField(true);
			bMapSetAktiv = true; // Pruefung auf keineUst uebergehen
			if (getTabbedPane().getEingangsrechnungDto() != null) {
				wcoMwst.setMap(DelegateFactory.getInstance().getMandantDelegate().getAllMwstsatz(
						LPMain.getTheClient().getMandant(),
						new Timestamp(getTabbedPane().getEingangsrechnungDto().getDBelegdatum().getTime())));
			} else {
				wcoMwst.setMap(DelegateFactory.getInstance().getMandantDelegate()
						.getAllMwstsatz(LPMain.getTheClient().getMandant(), new Timestamp(System.currentTimeMillis())));
			}
			bMapSetAktiv = false;
			if (getTabbedPane().getEingangsrechnungDto() != null) {
				wcoMwst.setKeyOfSelectedItem(getTabbedPane().getEingangsrechnungDto().getMwstsatzIId());
			}
			LockStateValue lsv = this.getLockedstateDetailMainKey();
			if (lsv.getIState() != PanelBasis.LOCK_IS_NOT_LOCKED) {
				wcoMwst.setEnabled(true);
			}
		}
	}

	/**
	 * Speichere ER.
	 * 
	 * @param e            ActionEvent
	 * @param bNeedNoSaveI boolean
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		// noetigenfalls den Betrag merken
		BigDecimal bdAlterWert = null;
		if (getTabbedPane().getEingangsrechnungDto() != null) {
			bdAlterWert = getTabbedPane().getEingangsrechnungDto().getNBetragfw();
		}

		// SP3255
		if (wnfBetrag.hasFocus()) {
			updateMwst();
		}

		try {
			if (allMandatoryFieldsSetDlg()) {

				components2Dto();

				if (wcoMwst.isEnabled() && !bMapSetAktiv) {
					if (kontoDto != null && Helper.short2boolean(kontoDto.getBOhneUst())) {
						if (wcoMwst.getSelectedItem() != null) {
							if (!wcoMwst.getKeyOfSelectedItem().equals(MEHRFACH)) {
								MwstsatzDto mwst = getMwstsatzForSelected();
								if (mwst != null && mwst.getFMwstsatz() != 0.0) {
									DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
											LPMain.getTextRespectUISPr("er.hint.keinevst"));
								}
							}
						}
					}
				}

				// Betrag prufen
				if (wcbHatPositionen.isSelected() == false
						&& BigDecimal.ZERO.compareTo(wnfBetrag.getBigDecimal()) == 0) {

					// PJ20410
					if (!DelegateFactory.getInstance().getMandantDelegate()
							.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG)
							&& tabbedPaneEingangsrechnung.isBZusatzkosten()) {

						int indexOK = 0;
						int indexTrotzdemanlegen = 1;
						int iAnzahlOptionen = 2;

						Object[] aOptionen = new Object[iAnzahlOptionen];
						aOptionen[indexOK] = LPMain.getTextRespectUISPr("button.ok");
						aOptionen[indexTrotzdemanlegen] = LPMain.getTextRespectUISPr("er.ermitnull.trotzdemanlegen");
						int iAuswahl = DialogFactory.showModalDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("er.betragnullnichtmoeglich"),
								LPMain.getTextRespectUISPr("lp.frage"), aOptionen, aOptionen[0]);

						if (iAuswahl == indexTrotzdemanlegen) {

						} else {
							return;
						}

					} else {

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("er.betragnullnichtmoeglich"));

						return;
					}

				}
				// keine Lieferantenrechnungsnummer eingegeben
				if (wtnfLieferantenrechnungsnummer.getText() == null) {
					boolean answer = (DialogFactory.showMeldung(
							LPMain.getTextRespectUISPr("er.ohnelieferantenrechnungsnummer"),
							LPMain.getTextRespectUISPr("lp.frage"),
							javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
					if (!answer) {
						return;
					}
				}
				// pruefen, obs schon ER's mit dieser Lieferantenrechnungsnummer
				// gibt
				else {
					EingangsrechnungDto[] dtos = DelegateFactory.getInstance().getEingangsrechnungDelegate()
							.eingangsrechnungFindByLieferantIIdLieferantenrechnungsnummerOhneExc(
									getTabbedPane().getEingangsrechnungDto().getLieferantIId(),
									getTabbedPane().getEingangsrechnungDto().getCLieferantenrechnungsnummer());
					if (dtos != null && dtos.length > 0) {
						// wenn es die ER ist, die ich grad bearbeite
						if (getTabbedPane().getEingangsrechnungDto().getIId() != null && dtos.length == 1
								&& dtos[0].getIId().equals(getTabbedPane().getEingangsrechnungDto().getIId())) {
							// nothing here
						} else {
							StringBuffer sb = new StringBuffer();
							sb.append(LPMain.getTextRespectUISPr("er.error.lieferantenrechnungsnummer_doppelt"));
							for (int i = 0; i < dtos.length; i++) {
								// auch hier die aktuelle nicht anzeigen
								if (getTabbedPane().getEingangsrechnungDto().getIId() != null
										&& dtos[i].getIId().equals(getTabbedPane().getEingangsrechnungDto().getIId())) {
									// nothing here
								} else {
									sb.append("\n");
									sb.append(dtos[i].getCNr());
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
				/**
				 * @todo Lieferantenrechnungsnummer auf fibutauglichkeit pruefen (10 stellen,
				 *       numerisch) PJ 4959
				 */
				boolean bIsNewER = getTabbedPane().getEingangsrechnungDto().getIId() == null;
				EingangsrechnungDto erDto = DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.updateEingangsrechnung(getTabbedPane().getEingangsrechnungDto());
				this.setKeyWhenDetailPanel(erDto.getIId());
				getTabbedPane().setEingangsrechnungDto(erDto);
				if (getTabbedPane().getWareneingangDto() != null) {
					getTabbedPane().getWareneingangDto().setEingangsrechnungIId(erDto.getIId());
					DelegateFactory.getInstance().getWareneingangDelegate()
							.updateWareneingang(getTabbedPane().getWareneingangDto());
					getTabbedPane().setWareneingangDto(null);
				}

				if (getTabbedPane().getInseratIIds() != null && getTabbedPane().getInseratIIds().length > 0) {
					for (int i = 0; i < getTabbedPane().getInseratIIds().length; i++) {
						DelegateFactory.getInstance().getInseratDelegate().eingangsrechnungZuordnen(
								(Integer) getTabbedPane().getInseratIIds()[i], erDto.getIId(),
								erDto.getNBetrag().subtract(erDto.getNUstBetrag()));
					}
					getTabbedPane().setInseratIIds(null);
				}

				super.eventActionSave(e, true);
				eventYouAreSelected(false);
				if (bIsNewER) {
					getTabbedPane().setSelectedEingangsrechnungIId();
				}
			}
		} catch (ExceptionLP t) {
			switch (t.getICode()) {
			case EJBExceptionLP.FEHLER_LIEFERANTENRECHNUNGSNUMMER_DOPPELT: {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("er.error.lieferantenrechnungsnummer_doppelt"));
			}
				break;
			case EJBExceptionLP.FEHLER_WERT_UNTER_AUFTRAGSZUORDNUNG: {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("er.error.wert_unter_auftragszuordnung"));
				getBetragER().setBetrag(bdAlterWert);
				wnfBetrag_focusLost();
			}
				break;
			default: {
				/**
				 * @todo PJ 4960
				 */
				throw t;
			}
			}
		}
	}

	/**
	 * Stornieren einer ER.
	 * 
	 * @param e                     ActionEvent
	 * @param bAdministrateLockKeyI boolean
	 * @param bNeedNoDeleteI        boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		if (erDto.getStatusCNr().equalsIgnoreCase(EingangsrechnungFac.STATUS_STORNIERT)) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
					"Die Eingangsrechnung ist bereits storniert");
			return;
		}
		if (erDto.getStatusCNr().equalsIgnoreCase(EingangsrechnungFac.STATUS_ERLEDIGT)) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
					"Die Eingangsrechnung ist bereits erledigt\nSie kann daher nicht storniert werden");
			return;
		}
		if (erDto.getStatusCNr().equalsIgnoreCase(EingangsrechnungFac.STATUS_TEILBEZAHLT)) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
					"Die Eingangsrechnung ist bereits teilweise bezahlt\nSie kann daher nicht storniert werden");
			return;
		}
		if (erDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_ANGELEGT)) {
			boolean answer = (DialogFactory.showMeldung("Eingangsrechnung " + erDto.getCNr() + " stornieren?",
					LPMain.getTextRespectUISPr("lp.frage"),
					javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
			if (!answer) {
				return;
			} else {
				DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.storniereEingangsrechnung(getTabbedPane().getEingangsrechnungDto().getIId());
			}
		}
		super.eventActionDelete(e, false, false);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRBestellung) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeBestellung((Integer) key);
				if (getTabbedPane().getLieferantDto() == null) {
					holeLieferant(bestellungDto.getLieferantIIdBestelladresse());

					DelegateFactory.getInstance().getLieferantDelegate().pruefeLieferant(
							bestellungDto.getLieferantIIdBestelladresse(), LocaleFac.BELEGART_EINGANGSRECHNUNG,
							getInternalFrame());
					prepareAnzahlung(wcoArt.getKeyOfSelectedItem().toString());
				}
			} else
			// Lieferantauswahldialog;
			if (e.getSource() == panelQueryFLRKundenauswahl) {
				Integer iIdKunde = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(iIdKunde,
						LocaleFac.BELEGART_EINGANGSRECHNUNG, getInternalFrame());

				Integer iIdLieferant = DelegateFactory.getInstance().getLieferantDelegate()
						.createVerstecktenLieferantAusKunden(iIdKunde);

				holeLieferant(iIdLieferant);
			} else if (e.getSource() == panelQueryFLRWaehrung) {
				String cNrWaehrung = (String) ((ISourceEvent) e.getSource()).getIdSelected();
				wtfWaehrung.setText(cNrWaehrung);
				wlaWaehrung1.setText(cNrWaehrung);
				wlaWaehrung2.setText(cNrWaehrung);
				setzeWechselkurs(cNrWaehrung);

			} else if (e.getSource() == panelQueryFLRLieferant) {

				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				DelegateFactory.getInstance().getLieferantDelegate().pruefeLieferant(key,
						LocaleFac.BELEGART_EINGANGSRECHNUNG, getInternalFrame());

				holeLieferant(key);
				if (wuerdeReversechargeDurchLieferantGeaendert()) {
					DialogReversechargeGeaendertER dialog = new DialogReversechargeGeaendertER(
							panelReversechargeart.getReversechargeartId(),
							getTabbedPane().getLieferantDto().getReversechargeartId());
					LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(dialog);
					dialog.setVisible(true);
					if (dialog.isModified()) {
						panelReversechargeart
								.setReversechargeartId(getTabbedPane().getLieferantDto().getReversechargeartId());
					}
				}
				prepareAnzahlung(wcoArt.getKeyOfSelectedItem().toString());
			} else if (e.getSource() == panelQueryFLRKostenstelle) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeKostenstelle((Integer) key);
			} else if (e.getSource() == panelQueryFLRZahlungsziel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeZahlungsziel((Integer) key);
			} else if (e.getSource() == panelQueryFLRKonto) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeKonto((Integer) key);
			} else if (e.getSource() == panelQueryFLRPersonal) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);
					wtfPersonalAbwBankverbindung.setText(personalDto.formatFixName1Name2());
					getTabbedPane().getEingangsrechnungDto().setPersonalIIdAbwBankverbindung(personalDto.getIId());
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRPersonal) {
				getTabbedPane().getEingangsrechnungDto().setPersonalIIdAbwBankverbindung(null);
				wtfPersonalAbwBankverbindung.setText(null);
			}
		}
	}

	private boolean wuerdeReversechargeDurchLieferantGeaendert() {
		LieferantDto lieferantDto = getTabbedPane().getLieferantDto();
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();

		if (lieferantDto == null || lieferantDto.getIId() == null)
			return false;
		if (erDto == null)
			return false;
		// if(lieferantDto.getIId().equals(erDto.getLieferantIId())) return
		// false ;

		return !panelReversechargeart.getReversechargeartId().equals(lieferantDto.getReversechargeartId());
	}

	private BigDecimal getWechselkurs(String cNrWaehrung) throws ExceptionLP, Throwable {
		String sMandantWaehrung = DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant()).getWaehrungCNr();
		if (sMandantWaehrung == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					"Beim Mandanten ist keine Standard-Waehrung hinterlegt");
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
		String sWaehrung = cNrWaehrung;
		if (sWaehrung.equals(sMandantWaehrung)) {
			return new BigDecimal(1);
		} else {
			BigDecimal bdKurs = DelegateFactory.getInstance().getLocaleDelegate().getWechselkurs2(sWaehrung,
					sMandantWaehrung);
			if (bdKurs != null) {
				return bdKurs.setScale(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS, BigDecimal.ROUND_HALF_EVEN);
			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), "Zwischen " + sMandantWaehrung
						+ " und " + sWaehrung + " ist kein Kurs hinterlegt\nBitte tragen Sie diesen nach");
				return null;
			}
		}
	}

	private void setzeWechselkurs(String cNrWaehrung) throws ExceptionLP, Throwable {
		wnfKurs.setForeground(Color.BLACK);
		String sMandantWaehrung = DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant()).getWaehrungCNr();
		if (sMandantWaehrung == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					"Beim Mandanten ist keine Standard-Waehrung hinterlegt");
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
		String sWaehrung = cNrWaehrung;
		if (sWaehrung.equals(sMandantWaehrung)) {
			wnfKurs.setBigDecimal(new BigDecimal(1));
		} else {

			BigDecimal bdKurs = DelegateFactory.getInstance().getLocaleDelegate().getWechselkurs2(sWaehrung,
					sMandantWaehrung);

			WechselkursDto wDto = DelegateFactory.getInstance().getLocaleDelegate().getKursZuDatum(sWaehrung,
					sMandantWaehrung, wdfBelegdatum.getDate());

			if (wDto != null) {
				bdKurs = wDto.getNKurs();
			}

			if (bdKurs != null) {
				wnfKurs.setBigDecimal(bdKurs);
			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), "Zwischen " + sMandantWaehrung
						+ " und " + sWaehrung + " ist kein Kurs hinterlegt\nBitte tragen Sie diesen nach");
				// LPMain.getInstance().exitFrame(getInternalFrame());
			}
		}
	}

	private void holeKostenstelle(Integer key) throws Throwable {
		if (key != null) {
			kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate().kostenstelleFindByPrimaryKey(key);
		} else {
			kostenstelleDto = null;
		}
		dto2ComponentsKostenstelle();
	}

	private void setKreditorennummerLabel(LieferantDto lieferantDto) throws Throwable {
		String msgToken = "er.kreditorennrkonto";
		Integer kontoId = lieferantDto.getKontoIIdKreditorenkonto();
		if (bAusgangsgutschriftAnKunde && wcbHatPositionen.isSelected()) {
			msgToken = "er.debitorennr";

			KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByiIdPartnercNrMandantOhneExc(
					lieferantDto.getPartnerIId(), LPMain.getTheClient().getMandant());
			kontoId = kdDto != null ? kdDto.getIidDebitorenkonto() : null;
		}

		String kontoCnr = "";
		if (kontoId != null) {
			KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKey(kontoId);
			kontoCnr = kontoDto.getCNr();
		}
		wlaKreditorennummer.setText(LPMain.getMessageTextRespectUISPr(msgToken, kontoCnr));
	}

	private void updateMwstCombobox(Integer mwstsatzBezId, Timestamp belegDatum) throws Throwable {
		Timestamp tBeleg = belegDatum;
		if (tBeleg == null) {
			tBeleg = new Timestamp(System.currentTimeMillis());
		}
		MwstsatzDto mwstsatzDtoPassend = DelegateFactory.getInstance().getMandantDelegate()
				.mwstsatzFindZuDatum(mwstsatzBezId, tBeleg);
		wcoMwst.setKeyOfSelectedItem(mwstsatzDtoPassend.getIId());
	}

	private void holeLieferant(Integer key) throws Throwable {
		if (key != null) {
			LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
					.lieferantFindByPrimaryKey(key);
			getTabbedPane().setLieferantDto(lieferantDto);

			setKreditorennummerLabel(getTabbedPane().getLieferantDto());
			setzeKundendaten();

			// fuer eine neue ER die Lieferantendaten uebernehmen
			if (getTabbedPane().getEingangsrechnungDto() == null) {
				if (zahlungszielDto == null) {
					holeZahlungsziel(getTabbedPane().getLieferantDto().getZahlungszielIId());
				}
				if (getTabbedPane().getLieferantDto().getWaehrungCNr() != null) {
					String waehrungCnr = getTabbedPane().getLieferantDto().getWaehrungCNr();
					wtfWaehrung.setText(waehrungCnr);
					wlaWaehrung1.setText(waehrungCnr);
					wlaWaehrung2.setText(waehrungCnr);
					setzeWechselkurs(waehrungCnr);
				}

				if (getTabbedPane().getLieferantDto().getMwstsatzbezIId() != null) {
					updateMwstCombobox(getTabbedPane().getLieferantDto().getMwstsatzbezIId(),
							wdfBelegdatum.getTimestamp());
					/*
					 * Timestamp tBeleg = wdfBelegdatum.getTimestamp(); Integer mwstsatzBezId =
					 * getTabbedPane().getLieferantDto().getMwstsatzbezIId(); if(tBeleg == null) {
					 * tBeleg = new Timestamp(System.currentTimeMillis()); } MwstsatzDto
					 * mwstsatzDtoPassend = DelegateFactory.getInstance()
					 * .getMandantDelegate().mwstsatzFindZuDatum(mwstsatzBezId, tBeleg);
					 * wcoMwst.setKeyOfSelectedItem(mwstsatzDtoPassend.getIId());
					 */
					/*
					 * // Auf den aktuellen MWST-Satz uebersetzen. MwstsatzDto mwstsatzDtoAktuell =
					 * DelegateFactory.getInstance().getMandantDelegate()
					 * .mwstsatzFindByMwstsatzbezIIdAktuellster(
					 * getTabbedPane().getLieferantDto().getMwstsatzbezIId());
					 * wcoMwst.setKeyOfSelectedItem(mwstsatzDtoAktuell.getIId());
					 */
				}
				if (!wcbMehrfachkontierung.isSelected()) {
					if (getTabbedPane().getLieferantDto().getKontoIIdWarenkonto() != null) {
						holeKonto(getTabbedPane().getLieferantDto().getKontoIIdWarenkonto());
					}
					if (kostenstelleDto == null) {
						holeKostenstelle(getTabbedPane().getLieferantDto().getIIdKostenstelle());
					}

					panelReversechargeart
							.setReversechargeartId(getTabbedPane().getLieferantDto().getReversechargeartId());
				} else {
					panelReversechargeart.setReversechargeKontierung();
				}

				wcbIGErwerb.setShort(getTabbedPane().getLieferantDto().getBIgErwerb());

			}

			actionDoReversechargeModified();
			actionDoIGErwerbModified();

		} else {
			getTabbedPane().setLieferantDto(null);
		}

		dto2ComponentsLieferant();
	}

	/**
	 * holeZahlungsziel
	 * 
	 * @param key Integer
	 * @throws Throwable
	 */
	private void holeZahlungsziel(Integer key) throws Throwable {
		if (key != null) {
			zahlungszielDto = DelegateFactory.getInstance().getMandantDelegate().zahlungszielFindByPrimaryKey(key);
		} else {
			zahlungszielDto = null;
		}
		dto2ComponentsZahlungsziel();
	}

	/**
	 * dto2ComponentsZahlungsziel
	 */
	private void dto2ComponentsZahlungsziel() {
		if (zahlungszielDto != null) {
			if (zahlungszielDto.getZahlungszielsprDto() != null) {
				wtfZahlungsziel.setText(zahlungszielDto.getZahlungszielsprDto().getCBezeichnung());
			} else {
				wtfZahlungsziel.setText(zahlungszielDto.getCBez());
			}
		} else {
			wtfZahlungsziel.setText(null);
		}
	}

	private void holeBestellung(Integer key) throws Throwable {
		if (key != null) {
			bestellungDto = DelegateFactory.getInstance().getBestellungDelegate().bestellungFindByPrimaryKey(key);
		} else {
			bestellungDto = null;
		}
		dto2ComponentsBestellung();
	}

	private void dialogQueryKostenstelle(ActionEvent e) throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance().createPanelFLRKostenstelle(getInternalFrame(),
				false, false);
		if (kostenstelleDto != null) {
			panelQueryFLRKostenstelle.setSelectedId(kostenstelleDto.getIId());
		}
		new DialogQuery(panelQueryFLRKostenstelle);
	}

	/**
	 * Dialogfenster zur Lieferantenauswahl.
	 * 
	 * @param e ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryLieferant(ActionEvent e) throws Throwable {
		Integer lieferantIId = null;
		if (getTabbedPane().getLieferantDto() != null) {
			lieferantIId = getTabbedPane().getLieferantDto().getIId();
		}
		panelQueryFLRLieferant = PartnerFilterFactory.getInstance().createPanelFLRLieferant(getInternalFrame(),
				lieferantIId, true, false);
		new DialogQuery(panelQueryFLRLieferant);
	}

	/**
	 * Dialogfenster zur Zahlungszieleauswahl.
	 * 
	 * @param e ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryZahlungsziel(ActionEvent e) throws Throwable {
		Integer zahlungszielIId = null;
		if (zahlungszielDto != null) {
			zahlungszielIId = zahlungszielDto.getIId();
		}
		panelQueryFLRZahlungsziel = SystemFilterFactory.getInstance().createPanelFLRZahlungsziel(getInternalFrame(),
				zahlungszielIId);
		new DialogQuery(panelQueryFLRZahlungsziel);
	}

	/**
	 * Dialogfenster zur Bestellungauswahl.
	 * 
	 * @throws Throwable
	 */
	private void dialogQueryBestellung() throws Throwable {
		Integer besIId = null;
		if (bestellungDto != null) {
			besIId = bestellungDto.getIId();
		}
		FilterKriterium[] fk;
		if (getTabbedPane().getLieferantDto() != null) {
			fk = BestellungFilterFactory.getInstance()
					.getFKBestellungenEinesLieferanten(getTabbedPane().getLieferantDto().getIId());
		} else {
			fk = SystemFilterFactory.getInstance().createFKMandantCNr();
		}
		panelQueryFLRBestellung = BestellungFilterFactory.getInstance().createPanelFLRBestellung(getInternalFrame(),
				false, false, fk, besIId);
		new DialogQuery(panelQueryFLRBestellung);
	}

	/**
	 * initPanel
	 * 
	 * @throws Throwable
	 */
	private void initPanel() throws Throwable {

		if (tabbedPaneEingangsrechnung.isBZusatzkosten()) {

			wcoWiederholungsintervall.setMap(DelegateFactory.getInstance().getAuftragServiceDelegate()
					.getAuftragwiederholungsintervall(LPMain.getInstance().getUISprLocale()));
			// wcoWiederholungsintervall
			// .setKeyOfSelectedItem(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH);

			if (!wcoArt.isMapSet()) {
				wcoArt.setMap(DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.getSprEingangsrechnungartNurZusatzkosten());
			}
		} else {
			if (!wcoArt.isMapSet()) {
				wcoArt.setMap(
						DelegateFactory.getInstance().getEingangsrechnungDelegate().getAllSprEingangsrechnungarten());
			}
		}

		if (!wcoMwst.isMapSet()) {
			bMapSetAktiv = true; // Pruefung auf keineUst uebergehen
			if (getTabbedPane().getEingangsrechnungDto() != null) {
				wcoMwst.setMap(DelegateFactory.getInstance().getMandantDelegate().getAllMwstsatz(
						LPMain.getTheClient().getMandant(),
						new Timestamp(getTabbedPane().getEingangsrechnungDto().getDBelegdatum().getTime())));
			} else {
				wcoMwst.setMap(DelegateFactory.getInstance().getMandantDelegate()
						.getAllMwstsatz(LPMain.getTheClient().getMandant(), new Timestamp(System.currentTimeMillis())));
			}
			bMapSetAktiv = false;
		}

		// rechte
		bIstModulKostenstelleInstalliert = true;
		wbuKostenstelle.setVisible(bIstModulKostenstelleInstalliert);
		wtfKostenstelleBezeichnung.setVisible(bIstModulKostenstelleInstalliert);
		wtfKostenstelleNummer.setVisible(bIstModulKostenstelleInstalliert);
		wtfKostenstelleNummer.setMandatoryField(bIstModulKostenstelleInstalliert);
	}

	private void setDefaults() throws Throwable {
		wcbMehrfachkontierung.setSelected(false);
		wcbHatPositionen.setSelected(false);
		wbuLieferant.setVisible(true);
		wbuKunde.setVisible(false);
		wcoArt.setKeyOfSelectedItem(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG);
		wdfBelegdatum.setDate(new java.sql.Date(System.currentTimeMillis()));
		wdfFreigabedatum.setDate(DelegateFactory.getInstance().getEingangsrechnungDelegate().getDefaultFreigabeDatum());
		// mit der Mandantenwaehrung beginnen, damit ist auch der kurs definiert
		wtfWaehrung.setText(DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant()).getWaehrungCNr());
		wlaWaehrung1.setText(wtfWaehrung.getText());
		wlaWaehrung2.setText(wtfWaehrung.getText());
		setzeWechselkurs(wtfWaehrung.getText());

		wcbIGErwerb.setShort(Helper.getShortFalse());
		panelReversechargeart.setOhneAsSelected();
		// panelReversechargeart.getWcoReversechargeart().setSelectedIndex(0);
		setIGErwerbReverseCharge();
	}

	private void components2Dto() throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		if (erDto == null) {
			// eine neue wirds
			erDto = new EingangsrechnungDto();
			erDto.setMandantCNr(LPMain.getTheClient().getMandant());
		}
		if (tabbedPaneEingangsrechnung.isBZusatzkosten()) {
			erDto.setWiederholungsintervallCNr((String) wcoWiederholungsintervall.getKeyOfSelectedItem());
		}
		if (bReversechargeVerwenden == true) {
			erDto.setReversechargeartId(panelReversechargeart.getReversechargeartId());
			// erDto.setBReversecharge(wcbReversecharge.getShort());
		} else {
			erDto.setReversechargeartId(panelReversechargeart.getReversechargeartId());
			// erDto.setBReversecharge(Helper.boolean2Short(false));
		}
		erDto.setBIgErwerb(wcbIGErwerb.getShort());
		erDto.setBMitpositionen(wcbHatPositionen.getShort());

		// Zuordnung zu Bestellung nur bei An-/Schlusszahlung
		if (wcoArt.getKeyOfSelectedItem().equals(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG)
				|| wcoArt.getKeyOfSelectedItem().equals(EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT)
				|| wcoArt.getKeyOfSelectedItem().equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)) {
			erDto.setBestellungIId(null);
		} else {
			erDto.setBestellungIId(bestellungDto.getIId());
		}
		erDto.setCLieferantenrechnungsnummer(wtnfLieferantenrechnungsnummer.getText());
		erDto.setCText(wtfText.getText());
		erDto.setCWeartikel(wtfWEArtikel.getText());
		erDto.setDBelegdatum(wdfBelegdatum.getDate());
		erDto.setDFreigabedatum(wdfFreigabedatum.getDate());
		erDto.setEingangsrechnungartCNr((String) wcoArt.getKeyOfSelectedItem());

		if (!wcoMwst.getKeyOfSelectedItem().equals(MEHRFACH)) {
			Integer pkMwstsatz = (Integer) wcoMwst.getKeyOfSelectedItem();
			if (pkMwstsatz != null) {
				erDto.setMwstsatzIId(pkMwstsatz);
			}
		}
		if (kostenstelleDto != null) {
			erDto.setKostenstelleIId(kostenstelleDto.getIId());
		} else {
			erDto.setKostenstelleIId(null);
		}
		if (getTabbedPane().getLieferantDto() != null) {
			erDto.setLieferantIId(getTabbedPane().getLieferantDto().getIId());
		} else {
			erDto.setLieferantIId(null);
		}
		if (kontoDto != null) {
			erDto.setKontoIId(kontoDto.getIId());
		} else {
			erDto.setKontoIId(null);
		}
		String sMandantWaehrung = DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant()).getWaehrungCNr();
		if (sMandantWaehrung == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					"Beim Mandanten ist keine Standard-Waehrung hinterlegt");
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
		String sWaehrung = (String) wtfWaehrung.getText();
		WechselkursDto kursDto = DelegateFactory.getInstance().getLocaleDelegate().getKursZuDatum(sWaehrung,
				sMandantWaehrung, erDto.getDBelegdatum());

		if (kursDto == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getMessageTextRespectUISPr("lp.error.keinkurshinterlegt", sMandantWaehrung, sWaehrung,
							Helper.formatDatum(erDto.getDBelegdatum(), LPMain.getTheClient().getLocUi())));
			return;
		}

		BigDecimal bdKurs = kursDto.getNKurs().setScale(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
				BigDecimal.ROUND_HALF_EVEN);

		erDto.setNBetrag(
				Helper.rundeKaufmaennisch(getBetragER().getBetrag().multiply(bdKurs), FinanzFac.NACHKOMMASTELLEN));
		erDto.setNBetragfw(getBetragER().getBetrag());
		erDto.setNKurs(bdKurs);
		if (wnfMwst.getBigDecimal() != null) {
			erDto.setNUstBetragfw(wnfMwst.getBigDecimal());
			erDto.setNUstBetrag(
					Helper.rundeKaufmaennisch(wnfMwst.getBigDecimal().multiply(bdKurs), FinanzFac.NACHKOMMASTELLEN));
		} else {
			erDto.setNUstBetrag(new BigDecimal(0));
			erDto.setNUstBetragfw(new BigDecimal(0));
		}
		erDto.setWaehrungCNr(sWaehrung);
		if (zahlungszielDto != null) {
			erDto.setZahlungszielIId(zahlungszielDto.getIId());
		} else {
			erDto.setZahlungszielIId(null);
		}
		erDto.setCKundendaten(wtfKundendaten.getText());

		getTabbedPane().setEingangsrechnungDto(erDto);
	}

	private void dto2Components() throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();

		wcoArt.removeActionListener(this);
		wcoMwst.removeActionListener(this);

		wcbHatPositionen.setShort(erDto.getBMitpositionen());
		holeBestellung(erDto.getBestellungIId());
		holeKostenstelle(erDto.getKostenstelleIId());
		holeLieferant(erDto.getLieferantIId());
		holeZahlungsziel(erDto.getZahlungszielIId());
		holeKonto(erDto.getKontoIId());

		dto2ComponentsZollimportpapier(erDto);
		dto2WiederholendErledigt(erDto);

		if (bFibuInstalliert == false) {
			wlaFibuExportDatum.setText("");

			try {
				ExportdatenDto exportDto = DelegateFactory.getInstance().getFibuExportDelegate()
						.exportdatenFindByBelegartCNrBelegiid(LocaleFac.BELEGART_EINGANGSRECHNUNG, erDto.getIId());

				wlaFibuExportDatum
						.setText(
								LPMain.getTextRespectUISPr("rech.fibuexportdatum")
										+ " " + Helper
												.formatTimestamp(
														DelegateFactory.getInstance().getFibuExportDelegate()
																.exportlaufFindByPrimaryKey(
																		exportDto.getExportlaufIId())
																.getTAendern(),
														LPMain.getTheClient().getLocUi()));

			} catch (Exception e) {
				// Kein Exoprt vorhanden
			}
		}

		boolean bMehrfach = erDto.getKontoIId() == null || erDto.getKostenstelleIId() == null;
		wcbMehrfachkontierung.setSelected(bMehrfach);
		updateMehrfach();

		// vollstaendig kontiert?
		if (bMehrfach) {
			BigDecimal bdNochNichtKontiert = DelegateFactory.getInstance().getEingangsrechnungDelegate()
					.getWertNochNichtKontiert(erDto.getIId());
			wlaNochNichtKontiert.setVisible(bdNochNichtKontiert.signum() != 0);
		} else {
			wlaNochNichtKontiert.setVisible(false);
		}

		// if (!bFibuInstalliert)
		// // Betrags/Ust Labels anpassen
		// setIGErwerbReverseCharge();

		if (erDto.getEingangsrechnungIIdNachfolger() != null) {
			EingangsrechnungDto erDtoNachfolger = DelegateFactory.getInstance().getEingangsrechnungDelegate()
					.eingangsrechnungFindByPrimaryKey(erDto.getEingangsrechnungIIdNachfolger());
			wtfNachfolger.setText(erDtoNachfolger.getCNr());
		} else {
			wtfNachfolger.setText("");
		}

		wcoWiederholungsintervall.setKeyOfSelectedItem(erDto.getWiederholungsintervallCNr());

		wtnfLieferantenrechnungsnummer.setText(erDto.getCLieferantenrechnungsnummer());
		wtfText.setText(erDto.getCText());
		wtfWEArtikel.setText(erDto.getCWeartikel());

		wdfBelegdatum.setDate(erDto.getDBelegdatum());
		wdfFreigabedatum.setDate(erDto.getDFreigabedatum());

		wcoMwst.setKeyOfSelectedItem(erDto.getMwstsatzIId());
		wnfKurs.setBigDecimal(erDto.getNKurs());

		wbuPersonalAbwBankverbindung.setOKey(erDto.getPersonalIIdAbwBankverbindung());
		if (erDto.getPersonalIIdAbwBankverbindung() != null) {
			PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey((Integer) erDto.getPersonalIIdAbwBankverbindung());

			wtfPersonalAbwBankverbindung.setText(personalDto.formatFixName1Name2());

		} else {
			wtfPersonalAbwBankverbindung.setText(null);
		}

		wtfWaehrung.setText(erDto.getWaehrungCNr());

		wnfKurs.setForeground(Color.BLACK);

		BigDecimal aktuelleKurs = getWechselkurs(erDto.getWaehrungCNr());

		WechselkursDto wDto = DelegateFactory.getInstance().getLocaleDelegate().getKursZuDatum(erDto.getWaehrungCNr(),
				LPMain.getTheClient().getSMandantenwaehrung(), wdfBelegdatum.getDate());

		if (wDto != null) {
			aktuelleKurs = wDto.getNKurs().setScale(LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
					BigDecimal.ROUND_HALF_EVEN);
		}

		if (aktuelleKurs != null) {
			if (wnfKurs.getBigDecimal().doubleValue() != aktuelleKurs.doubleValue()) {
				wnfKurs.setForeground(Color.RED);
			}
		}

		wlaWaehrung1.setText(erDto.getWaehrungCNr());
		wlaWaehrung2.setText(erDto.getWaehrungCNr());
		wcoArt.setKeyOfSelectedItem(erDto.getEingangsrechnungartCNr());
		wtfKundendaten.setText(erDto.getCKundendaten());

		panelReversechargeart.setReversechargeartId(erDto.getReversechargeartId());
		wcbIGErwerb.setShort(erDto.getBIgErwerb());

		wnfMwst.setBigDecimal(erDto.getNUstBetragfw());
		createBetragER(erDto);
		// wnfBetrag.setBigDecimal(erDto.getNBetragfw());
		// setIGErwerbReverseCharge();

		dto2Statusbar(erDto);

		wcoArt.addActionListener(this);
		wcoMwst.addActionListener(this);

		String text = "";

		if (getTabbedPane().getEingangsrechnungDto().getTGeprueft() != null) {
			text = LPMain.getTextRespectUISPr("er.geprueftam") + " " + Helper.formatDatumZeit(
					getTabbedPane().getEingangsrechnungDto().getTGeprueft(), LPMain.getTheClient().getLocUi());
		}
		if (getTabbedPane().getEingangsrechnungDto().getPersonalIIdGeprueft() != null) {
			text += "(" + DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(getTabbedPane().getEingangsrechnungDto().getPersonalIIdGeprueft())
					.getCKurzzeichen() + ")";
		}

		wlaGeprueft.setText(text);

		setVisibleBestellung(erDto.getEingangsrechnungartCNr());

	}

	private void dto2Statusbar(EingangsrechnungDto erDto) throws Throwable {
		setStatusbarModification(erDto);
		this.setStatusbarStatusCNr(erDto.getStatusCNr());
		String status = DelegateFactory.getInstance().getVersandDelegate()
				.getVersandstatus(LocaleFac.BELEGART_EINGANGSRECHNUNG, erDto.getIId());
		if (status != null) {
			status = LPMain.getTextRespectUISPr("lp.versandstatus") + ": " + status;
		}

		setStatusbarSpalte5(status);
	}

	private void dto2ComponentsZollimportpapier(EingangsrechnungDto erDto) throws Throwable {
		String text = "";

		if (tabbedPaneEingangsrechnung.isBZusatzkosten()) {
			if (erDto.getTWiederholenderledigt() != null) {
				text = LPMain.getTextRespectUISPr("er.zusatzkosten.wiederholungerledigt") + " "
						+ Helper.formatDatumZeit(erDto.getTWiederholenderledigt(), LPMain.getTheClient().getLocUi());
			}
			if (erDto.getPersonalIIdWiederholenderledigt() != null) {
				text += "("
						+ DelegateFactory.getInstance().getPersonalDelegate()
								.personalFindByPrimaryKey(erDto.getPersonalIIdWiederholenderledigt()).getCKurzzeichen()
						+ ")";
			}
		} else {
			if (erDto.getTZollimportpapier() != null) {
				text = LPMain.getTextRespectUISPr("er.zollimportpapiere.erhalten.persondatum") + " "
						+ Helper.formatDatumZeit(erDto.getTZollimportpapier(), LPMain.getTheClient().getLocUi());
			}
			if (erDto.getPersonalIIdZollimportpapier() != null) {
				text += "("
						+ DelegateFactory.getInstance().getPersonalDelegate()
								.personalFindByPrimaryKey(erDto.getPersonalIIdZollimportpapier()).getCKurzzeichen()
						+ ")";
			}
		}

		wlaZollimportpapiere.setText(text);
	}

	private void dto2WiederholendErledigt(EingangsrechnungDto erDto) throws Throwable {
		String text2 = "";

		if (!tabbedPaneEingangsrechnung.isBZusatzkosten()) {
			if (erDto.getCZollimportpapier() != null) {
				text2 = LPMain.getTextRespectUISPr("lp.zollbelegnummer") + " " + erDto.getCZollimportpapier();
			}

			if (erDto.getEingangsrechnungIdZollimport() != null) {
				EingangsrechnungDto erDtoZollImport = DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.eingangsrechnungFindByPrimaryKey(erDto.getEingangsrechnungIdZollimport());
				text2 += " | " + LPMain.getTextRespectUISPr("er.modulname.kurz") + " " + erDtoZollImport.getCNr();
			}
		}

		wlaWiederholendErledigt.setText(text2);
	}

	private void dto2ComponentsLieferant() throws ExceptionLP, Throwable {
		if (getTabbedPane().getLieferantDto() != null) {
			LieferantDto lieferant = getTabbedPane().getLieferantDto();

			if (bAusgangsgutschriftAnKunde && wcbHatPositionen.isSelected()) {

				KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByiIdPartnercNrMandantOhneExc(lieferant.getPartnerIId(),
								LPMain.getTheClient().getMandant());

				if (kdDto != null) {
					wbuKunde.setOKey(kdDto.getIId());
				}

				wbuKunde.setVisible(true);
				wbuLieferant.setVisible(false);
			} else {
				wbuKunde.setVisible(false);
				wbuLieferant.setVisible(true);
			}

			wbuLieferant.setOKey(lieferant.getIId());
			wtfLieferant.setText(lieferant.getPartnerDto().formatFixTitelName1Name2());
			wtfAdresse.setText(lieferant.getPartnerDto().formatAdresse());
			wtfAbteilung.setText(lieferant.getPartnerDto().getCName3vorname2abteilung());
			// panelReversechargeart.reload();
			// panelReversechargeart.setReversechargeartId(
			// getTabbedPane().getLieferantDto().getReversechargeartId());
			// wcbIGErwerb.setShort(getTabbedPane().getLieferantDto()
			// .getBIgErwerb());
			// actionDoReversechargeModified();
		} else {
			wbuLieferant.setOKey(null);
			wtfLieferant.setText(null);
			wtfAdresse.setText(null);
			wtfAbteilung.setText(null);
			if (bFibuInstalliert)
				wcbIGErwerb.setSelected(false);
		}
	}

	private void dto2ComponentsBestellung() throws ExceptionLP, Throwable {
		if (bestellungDto != null) {
			wtfBestellung.setText(bestellungDto.getCNr());
			String art = getTabbedPane().getEingangsrechnungDto() == null ? wcoArt.getSelectedItem().toString()
					: getTabbedPane().getEingangsrechnungDto().getEingangsrechnungartCNr();
			if (EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG.trim().equals(art.trim())) {
				StringBuffer sb = new StringBuffer();
				wlaAnzahlungen.setVisible(true);
				wtfAnzahlungen.setVisible(true);
				EingangsrechnungDto[] dtos = DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.findByBestellungIId(bestellungDto.getIId());
				for (EingangsrechnungDto dto : dtos) {
					if (!dto.getEingangsrechnungartCNr().equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG))
						continue;
					if (dto.getStatusCNr().equals(EingangsrechnungFac.STATUS_STORNIERT))
						continue;
					sb.append(dto.getCNr() + ", ");
				}
				wtfAnzahlungen.setText(sb.toString());
			} else {
				wlaAnzahlungen.setVisible(false);
				wtfAnzahlungen.setVisible(false);
			}
		} else {
			wtfBestellung.setText(null);
			wlaAnzahlungen.setVisible(false);
			wtfAnzahlungen.setVisible(false);
		}
	}

	private void dto2ComponentsKostenstelle() {
		if (kostenstelleDto != null) {
			wtfKostenstelleNummer.setText(kostenstelleDto.getCNr());
			wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
		} else {
			wtfKostenstelleNummer.setText(null);
			wtfKostenstelleBezeichnung.setText(null);
		}
	}

	/**
	 * wnfBetrag_focusLost
	 * 
	 * @throws Throwable
	 */
	void wnfBetrag_focusLost() throws Throwable {
		myLogger.warn("wnfBetrag_focuslost");
		// Wenn der Anwender explizit einen Betrag eingibt, dann haben wir wieder
		// das "alte" Verhalten
		originalNettoBetrag = null;
		updateMwst();
	}

	private void updateMwst() throws ExceptionLP, Throwable {
		if (wcoMwst.getKeyOfSelectedItem() == null || wcoMwst.getKeyOfSelectedItem().equals(MEHRFACH)
				|| wnfBetrag.getBigDecimal() == null) {
			wnfMwst.setBigDecimal(null);
		} else {
			calculateRoundedER();
		}
	}

	// private void updateMwst() throws ExceptionLP, Throwable {
	// if (wcoMwst.getKeyOfSelectedItem() == null
	// || wcoMwst.getKeyOfSelectedItem().equals(MEHRFACH)
	// || wnfBetrag.getBigDecimal() == null) {
	// wnfMwst.setBigDecimal(null);
	// } else {
	// if (wcbIGErwerb.isSelected()
	// || panelReversechargeart.hatReversecharge()) {
	// if (nettoBetrag != null) {
	// wnfBetrag.setBigDecimal(nettoBetrag);
	// }
	// // IG Erwerb und Reverse Charge rechnen netto
	// // wnfMwst.setBigDecimal(Helper.getProzentWert(
	// // wnfBetrag.getBigDecimal(),
	// // new BigDecimal(mwst.getFMwstsatz()),
	// // FinanzFac.NACHKOMMASTELLEN));
	// calculateRoundedER(false);
	// } else {
	// if (nettoBetrag != null) {
	// wnfBetrag.setBigDecimal(nettoBetrag);
	// }
	// calculateRoundedER(nettoBetrag == null);
	// /*
	// * if (nettoBetrag != null) { // wenn nettoBetrag gesetzt ist,
	// * rechnen wir immer // von diesem ausgehend. Netto bleibt also
	// * immer gleich. //
	// * wnfBetrag.setBigDecimal(nettoBetrag.add(Helper //
	// * .getProzentWert(nettoBetrag, new BigDecimal( //
	// * mwst.getFMwstsatz()), // FinanzFac.NACHKOMMASTELLEN)));
	// * wnfBetrag.setBigDecimal(nettoBetrag);
	// * calculateRoundedER(false); } else {
	// *
	// * // wnfMwst.setBigDecimal(Helper.getMehrwertsteuerBetrag( //
	// * wnfBetrag.getBigDecimal(), mwst.getFMwstsatz() //
	// * .doubleValue()));
	// *
	// * calculateRoundedER(true); }
	// */}
	// }
	// }

	private void calculateRoundedER() throws Throwable {
		getBetragER().calculateRoundedER();
	}

	// private void calculateRoundedER(boolean brutto) throws Throwable {
	// EingangsrechnungDto calcDto = new EingangsrechnungDto();
	// if (getTabbedPane().getLieferantDto() != null) {
	// calcDto.setLieferantIId(getTabbedPane().getLieferantDto().getIId());
	// }
	// calcDto.setWaehrungCNr(wtfWaehrung.getText());
	// calcDto.setNBetrag(wnfBetrag.getBigDecimal());
	// calcDto.setMwstsatzIId((Integer) wcoMwst.getKeyOfSelectedItem());
	// EingangsrechnungDelegate delegate = DelegateFactory.getInstance()
	// .getEingangsrechnungDelegate();
	// CoinRoundingResult result = brutto ? delegate
	// .calcMwstBetragFromBrutto(calcDto) : delegate
	// .calcMwstBetragFromNetto(calcDto);
	// // CoinRoundingResult result =
	// //
	// DelegateFactory.getInstance().getEingangsrechnungDelegate().calcMwstBetragFromNetto(calcDto);
	// wnfMwst.setBigDecimal(result.getTaxAmount());
	// if (!brutto) {
	// // wir hatten einen Nettobetrag im wnfBetrag der jetzt wieder Brutto
	// // dargestellt wird
	// wnfBetrag.setBigDecimal(result.getBruttoAmount());
	// }
	// wnfBetrag.setForeground(result.isValidBruttoAmount() ? Color.black
	// : Color.red);
	// }

	private MwstsatzDto getMwstsatzForSelected() throws ExceptionLP, Throwable {
		MwstsatzDto mwst = DelegateFactory.getInstance().getMandantDelegate()
				.mwstsatzFindByPrimaryKey((Integer) wcoMwst.getKeyOfSelectedItem());
		return mwst;
	}

	private void holeKonto(Integer key) throws Throwable {
		if (key != null) {
			this.kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKey(key);

			// SP6110
			if (kontoDto.getUvaartIId() != null) {
				UvaartDto uvaartDto = DelegateFactory.getInstance().getFinanzServiceDelegate()
						.uvaartFindByPrimaryKey(kontoDto.getUvaartIId());

				if (Helper.short2boolean(uvaartDto.getBKeineAuswahlBeiEr())) {

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getMessageTextRespectUISPr("er.konto.auswahl.error", kontoDto.getCNr()));
					this.kontoDto = null;
				}
			}

		} else {
			kontoDto = null;
		}
		dto2ComponentsKonto();
	}

	/**
	 * dto2Components
	 */
	private void dto2ComponentsKonto() {
		if (kontoDto != null) {
			wtfKontoNummer.setText(kontoDto.getKontonrBezeichnung());
		} else {
			wtfKontoNummer.setText(null);
		}
	}

	public void propertyChange(PropertyChangeEvent e) {
		if (e.getSource() == wdfBelegdatum && e.getNewValue() instanceof Date && e.getPropertyName().equals("date")
				&& wdfBelegdatum.getTimestamp() != null && !wcbMehrfachkontierung.isSelected()) {
			// SP5714
			try {
				wcoMwst.setMap(DelegateFactory.getInstance().getMandantDelegate().getAllMwstsatz(
						LPMain.getTheClient().getMandant(), new Timestamp(wdfBelegdatum.getTimestamp().getTime())));

				if (getTabbedPane().getLieferantDto() != null
						&& getTabbedPane().getLieferantDto().getMwstsatzbezIId() != null) {
					updateMwstCombobox(getTabbedPane().getLieferantDto().getMwstsatzbezIId(),
							wdfBelegdatum.getTimestamp());
					/*
					 * Timestamp tBeleg = wdfBelegdatum.getTimestamp(); Integer mwstsatzBezId =
					 * getTabbedPane().getLieferantDto().getMwstsatzbezIId(); MwstsatzDto
					 * mwstsatzDtoPassend = DelegateFactory.getInstance()
					 * .getMandantDelegate().mwstsatzFindZuDatum(mwstsatzBezId, tBeleg);
					 * wcoMwst.setKeyOfSelectedItem(mwstsatzDtoPassend.getIId());
					 */
					/*
					 * // Auf den aktuellen MWST-Satz uebersetzen. MwstsatzDto mwstsatzDtoAktuell =
					 * DelegateFactory.getInstance().getMandantDelegate()
					 * .mwstsatzFindByMwstsatzbezIIdAktuellster(
					 * getTabbedPane().getLieferantDto().getMwstsatzbezIId());
					 * wcoMwst.setKeyOfSelectedItem(mwstsatzDtoAktuell.getIId());
					 */
				}
			} catch (Throwable ex) {
				// brauche ich
				handleException(ex, false);
			}
		}
	}

	private void dialogQueryKonto(ActionEvent e) throws Throwable {
		wbuKonto.setEnabled(false);
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, };
		QueryType[] qt = null;
		// nur Sachkonten dieses Mandanten
		FilterKriterium[] filters = FinanzFilterFactory.getInstance().createFKSachkontenFuerER();
		panelQueryFLRKonto = new PanelQueryFLR(qt, filters, QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr("finanz.liste.sachkonten"));
		FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().createFKDKontonummer();
		FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().createFKDKontobezeichnung();
		FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance().createFKVKonto();
		panelQueryFLRKonto.befuellePanelFilterkriterienDirektUndVersteckte(fkDirekt1, fkDirekt2, fkVersteckt);
		if (kontoDto != null) {
			panelQueryFLRKonto.setSelectedId(kontoDto.getIId());
		}
		new DialogQuery(panelQueryFLRKonto);
		wbuKonto.setEnabled(true);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();

		if (!tabbedPaneEingangsrechnung.isBZusatzkosten() && getTabbedPane().iERPruefen > 0) {

			if (getTabbedPane().iERPruefen > 0 && erDto.getTGeprueft() != null) {
				// Wenn die ER bereits geprueft ist und keine
				// Berechtigung vorhanden

				boolean b = DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_ER_DARF_EINGANGSRECHNUNGEN_PRUEFEN);

				if (b == false) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("er.geprueft.ruecknahme.error"));
					return;
				} else {
					if (DialogFactory.showMeldung(

							LPMain.getTextRespectUISPr("er.geprueft.ruecknahme.frage"),
							LPMain.getTextRespectUISPr("lp.frage"),
							javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
						DelegateFactory.getInstance().getEingangsrechnungDelegate()
								.toggleEingangsrechnungGeprueft(erDto.getIId());
						getTabbedPane().setEingangsrechnungDto(DelegateFactory.getInstance()
								.getEingangsrechnungDelegate().eingangsrechnungFindByPrimaryKey(erDto.getIId()));
					} else {
						return;
					}

				}

			}

		}

		if (EingangsrechnungFac.STATUS_STORNIERT.equals(erDto.getStatusCNr())) {
			boolean answer = (DialogFactory.showMeldung(
					"Die Eingangsrechnung ist storniert\nSoll sie wieder verwendet werden?",
					LPMain.getTextRespectUISPr("lp.frage"),
					javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
			if (!answer) {
				return;
			}
			DelegateFactory.getInstance().getEingangsrechnungDelegate()
					.storniereEingangsrechnungRueckgaengig(erDto.getIId());
			this.eventYouAreSelected(false);
		} else if (erDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_TEILBEZAHLT)) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
					"Es sind bereits Zahlungen auf diese Eingangsrechnung eingetragen\nNehmen Sie zuerst die Zahlungen zur\u00FCck");
			return;
		} else if (erDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_ERLEDIGT)) {
			if (DialogFactory.showMeldung(LPMain.getTextRespectUISPr("er.eingangsrechnungistbereitserledigt"),
					LPMain.getTextRespectUISPr("lp.hint"),
					javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
				DelegateFactory.getInstance().getEingangsrechnungDelegate().erledigungAufheben(erDto.getIId());
				getTabbedPane().reloadEingangsrechnungDto();
				eventYouAreSelected(false);
			}
			return;
		}

		if (erDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_ANGELEGT)
				&& getTabbedPane().getEingangsrechnungDto().getTGedruckt() != null) {
			boolean bFrage = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					LPMain.getTextRespectUISPr("er.gedruckt.zuruecksetzen"));
			if (bFrage == true) {
				DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.eingangsrechnungAufAngelegtZuruecksetzen(getTabbedPane().getEingangsrechnungDto().getIId());
				getTabbedPane().setEingangsrechnungDto(DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.eingangsrechnungFindByPrimaryKey(getTabbedPane().getEingangsrechnungDto().getIId()));
			} else {
				return;
			}
		}

		super.eventActionUpdate(aE, false);
		setzeWechselkurs(wtfWaehrung.getText());
		updateMehrfach();

		if (!getTabbedPane().isBDarfKontieren()) {
			wcbMehrfachkontierung.setEnabled(false);
		}

		panelReversechargeart.setReversechargeartId(erDto.getReversechargeartId());

		if (wcbHatPositionen.isSelected()) {
			wnfBetrag.setEditable(false);
			wcbMehrfachkontierung.setEnabled(false);
			wcoArt.setEnabled(false);

			if (bAusgangsgutschriftAnKunde) {
				wbuKunde.setVisible(true);
				wbuLieferant.setVisible(false);
			} else {
				wbuKunde.setVisible(false);
				wbuLieferant.setVisible(true);
			}

		} else {
			wbuKunde.setVisible(false);
			wbuLieferant.setVisible(true);
		}

		String sArt = wcoArt.getKeyOfSelectedItem().toString();
		if (sArt.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)) {
			wcbMehrfachkontierung.setEnabled(false);

		}

	}

	public Map<String, String> getMapSieheKontierung() {
		TreeMap<String, String> tm = new TreeMap<String, String>();
		tm.put(MEHRFACH, LPMain.getTextRespectUISPr("er.siehekontierung"));
		return tm;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuLieferant;
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI) throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);
		getTabbedPane().enablePanels();
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		getTabbedPane().enablePanels();
		getTabbedPane().setWareneingangDto(null);
		getTabbedPane().setInseratIIds(null);
	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		getTabbedPane().print();
		eventYouAreSelected(false);
	}

	public void setMyComponents(EingangsrechnungDto eingangsrechnungDto) throws Throwable {
		holeBestellung(eingangsrechnungDto.getBestellungIId());
		holeKostenstelle(eingangsrechnungDto.getKostenstelleIId());
		holeLieferant(eingangsrechnungDto.getLieferantIId());
		holeZahlungsziel(eingangsrechnungDto.getZahlungszielIId());
		holeKonto(eingangsrechnungDto.getKontoIId());

		wtnfLieferantenrechnungsnummer.setText(eingangsrechnungDto.getCLieferantenrechnungsnummer());
		wtfText.setText(eingangsrechnungDto.getCText());
		wdfBelegdatum.setDate(eingangsrechnungDto.getDBelegdatum());
		wdfFreigabedatum.setDate(eingangsrechnungDto.getDFreigabedatum());
		// egal ob "normale" ER oder Gutschrift, hier steht immer der absolute
		// betrag
		wnfBetrag.setBigDecimal(eingangsrechnungDto.getNBetragfw().abs());

		wcoMwst.setKeyOfSelectedItem(eingangsrechnungDto.getMwstsatzIId());
		wnfKurs.setBigDecimal(eingangsrechnungDto.getNKurs());
		// egal ob "normale" ER oder Gutschrift, hier steht immer der absolute
		// betrag
		wnfMwst.setBigDecimal(eingangsrechnungDto.getNUstBetragfw().abs());
		wtfWaehrung.setText(eingangsrechnungDto.getWaehrungCNr());
		wcoArt.setKeyOfSelectedItem(eingangsrechnungDto.getEingangsrechnungartCNr());
		//Aufgrund SP9476 auskommentiert
		//wtfKundendaten.setText(eingangsrechnungDto.getCKundendaten());

		updateMehrfach();
		// wcoMwst wird von updateMehrfach() ueberschrieben
		wcoMwst.setKeyOfSelectedItem(eingangsrechnungDto.getMwstsatzIId());
		panelReversechargeart.setReversechargeartId(eingangsrechnungDto.getReversechargeartId());

		dto2Statusbar(eingangsrechnungDto);
	}

	public void setNettoBetrag(BigDecimal nettoBetrag, Integer mwstsatzIId) throws ExceptionLP, Throwable {
		createBetragER();

		if (getBetragER().isBrutto()) {
			CoinRoundingResult result = null;
			if (!getTabbedPane().isBDarfKontieren() && mwstsatzIId != null) {
				result = calcMwstBetrag(nettoBetrag, false, mwstsatzIId);
			} else {
				result = calcMwstBetrag(nettoBetrag, false);
			}

			getBetragER().setBetrag(result.getBruttoAmount());
		} else {
			getBetragER().setBetrag(nettoBetrag);
		}

		updateMwst();
	}

	/**
	 * WIP: Der Ausgangsbeleg hat einen Nettobetrag. Unsere ER ist eigentlich
	 * Brutto. Wenn innerhalb der ER (im Kontext dessen, dass diese ER aus einer BS
	 * erstellt wird(! gerade jetzt) bekannt ist, dass es sich eigentlich um einen
	 * Nettobetrag handelt, dann diesen vorhalten.
	 * 
	 * Das Thema ist, dass wir im Zuge der ER Kopfdaten eigentlich nicht wissen, wie
	 * der eingegebene Betrag zu bewerten ist. Der Anwender kann zuerst den Betrag
	 * eingeben, und erst dann sagen, dass er IG/RC haben will. So wie es jetzt
	 * umgesetzt ist, aendern wir den Betrag nicht, sondern nehmen ihn jetzt mit
	 * diesen Einstellungen zur Kenntnis.
	 * 
	 * Im Kontext ER wird durch Wareneingang erstellt, wissen wir, dass der Betrag
	 * eigentlich netto gemeint ist und der von uns ermittelte Bruttobetrag nur der
	 * Default-Behandlung geschuldet ist.
	 * 
	 * @param nettoBetrag
	 */
	public void setOriginalNettoBetrag(BigDecimal nettoBetrag, BigDecimal ustBetrag) {
		this.originalNettoBetrag = nettoBetrag;
		this.originalUstBetrag = ustBetrag;
		myLogger.warn("setOriginalNettoBetrag: " + nettoBetrag + ", ust: " + ustBetrag);
	}

	/*
	 * public void setNettoBetrag(BigDecimal nettoBetrag) throws ExceptionLP,
	 * Throwable { createBetragER();
	 * 
	 * boolean bMehrfachKontierung = false;
	 * 
	 * if (wcoMwst.getKeyOfSelectedItem() != null &&
	 * wcoMwst.getKeyOfSelectedItem().equals(MEHRFACH)) { bMehrfachKontierung =
	 * true; }
	 * 
	 * if (getBetragER().isBrutto() && bMehrfachKontierung == false) {
	 * CoinRoundingResult result = calcMwstBetrag(nettoBetrag, false);
	 * getBetragER().setBetrag(result.getBruttoAmount()); } else {
	 * getBetragER().setBetrag(nettoBetrag); }
	 * 
	 * updateMwst(); }
	 */

	private void setzeKundendaten() throws Throwable {
		if (getTabbedPane().getLieferantDto() != null && getTabbedPane().getLieferantDto().getCKundennr() != null
				&& kundendatenVorbesetzen()) {
			String cKnr = Helper.cutString(getTabbedPane().getLieferantDto().getCKundennr(),
					EingangsrechnungFac.FieldLength.KUNDENDATEN);
			wtfKundendaten.setText(cKnr);
		} else {
			wtfKundendaten.setText(null);
		}
	}

	private boolean kundendatenVorbesetzen() throws Throwable {
		return DelegateFactory.getInstance().getParameterDelegate().getEingangsrechnungKundendatenVorbesetzen();
	}

	private boolean isEingangsrechnungBrutto() {
		return !(wcbIGErwerb.isSelected() || panelReversechargeart.hatReversecharge());
	}

	private void createBetragER() {
		betragER = isEingangsrechnungBrutto() ? new BruttoBetragER() : new NettoBetragER();
	}

	private void createBetragER(EingangsrechnungDto erDto) throws ExceptionLP {
		createBetragER();
		getBetragER().setBetrag(erDto);
	}

	private IBetragER getBetragER() {
		if (betragER == null) {
			createBetragER();
		}
		return betragER;
	}

	private CoinRoundingResult calcMwstBetrag(BigDecimal betrag, boolean fromBrutto) throws Throwable {
		return calcMwstBetrag(betrag, fromBrutto, null);
	}

	private CoinRoundingResult calcMwstBetrag(BigDecimal betrag, boolean fromBrutto, Integer mwstsatzIIdUebersteuert)
			throws Throwable {
		EingangsrechnungDto calcDto = new EingangsrechnungDto();
		if (getTabbedPane().getLieferantDto() != null) {
			calcDto.setLieferantIId(getTabbedPane().getLieferantDto().getIId());
		}
		calcDto.setWaehrungCNr(wtfWaehrung.getText());
		calcDto.setNBetrag(betrag);
		if (mwstsatzIIdUebersteuert != null) {
			calcDto.setMwstsatzIId(mwstsatzIIdUebersteuert);
		} else {
			calcDto.setMwstsatzIId((Integer) wcoMwst.getKeyOfSelectedItem());
		}

		EingangsrechnungDelegate delegate = DelegateFactory.getInstance().getEingangsrechnungDelegate();
		return fromBrutto ? delegate.calcMwstBetragFromBrutto(calcDto) : delegate.calcMwstBetragFromNetto(calcDto);
	}

	private interface IBetragER {
		public void setBetrag(BigDecimal betrag) throws ExceptionLP;

		public void setBetrag(EingangsrechnungDto erDto) throws ExceptionLP;

		public BigDecimal getBetrag() throws ExceptionLP;

		public boolean isBrutto();

		public void calculateRoundedER() throws Throwable;
	}

	private class BruttoBetragER implements IBetragER {

		public BruttoBetragER() {
			setFieldsToBrutto();
		}

		@Override
		public void setBetrag(BigDecimal bruttoBetrag) throws ExceptionLP {
			myLogger.warn("setBetrag (brutto) from betrag " + bruttoBetrag.toPlainString());
			wnfBetrag.setBigDecimal(bruttoBetrag);
		}

		@Override
		public void setBetrag(EingangsrechnungDto erDto) throws ExceptionLP {
			myLogger.warn("setBetrag (brutto) from " + erDto.getNBetragfw().toPlainString());
			wnfBetrag.setBigDecimal(erDto.getNBetragfw());
		}

		private void setFieldsToBrutto() {
			wlaBetrag.setText(LPMain.getTextRespectUISPr("label.bruttobetrag"));
			wlaMwst.setText(LPMain.getTextRespectUISPr("label.mwst"));
		}

		public BigDecimal getBetrag() throws ExceptionLP {
			return wnfBetrag.getBigDecimal();
		}

		@Override
		public boolean isBrutto() {
			return true;
		}

		public void calculateRoundedER() throws Throwable {
			CoinRoundingResult result = calcMwstBetrag(wnfBetrag.getBigDecimal(), true);
			wnfMwst.setBigDecimal(result.getTaxAmount());
			wnfBetrag.setForeground(result.isValidBruttoAmount() ? Color.black : Color.red);
		}
	}

	private class NettoBetragER implements IBetragER {
		private BigDecimal bruttoBetrag;

		public NettoBetragER() {
			setFieldsToNetto();
		}

		@Override
		public void setBetrag(BigDecimal nettoBetrag) throws ExceptionLP {
			myLogger.warn("setBetrag (netto) from betrag " + nettoBetrag.toPlainString());
			wnfBetrag.setBigDecimal(nettoBetrag);
		}

		@Override
		public void setBetrag(EingangsrechnungDto erDto) throws ExceptionLP {
			myLogger.warn("setBetrag (netto) from er-Betrag " + erDto.getNBetragfw().toPlainString());
			wnfBetrag.setBigDecimal(erDto.getNBetragfw());
		}

		private void setBrutto(BigDecimal bruttoBetrag) {
			this.bruttoBetrag = bruttoBetrag;
		}

		private void setFieldsToNetto() {
			wlaBetrag.setText(LPMain.getTextRespectUISPr("label.nettobetrag"));
			wlaMwst.setText(LPMain.getTextRespectUISPr("label.mwst.ig"));
		}

		@Override
		public BigDecimal getBetrag() throws ExceptionLP {
			return wnfBetrag.getBigDecimal();
		}

		@Override
		public boolean isBrutto() {
			return false;
		}

		@Override
		public void calculateRoundedER() throws Throwable {
			CoinRoundingResult result = calcMwstBetrag(wnfBetrag.getBigDecimal(), false);
			wnfMwst.setBigDecimal(result.getTaxAmount());
			setBrutto(result.getBruttoAmount());
			wnfBetrag.setForeground(result.isValidBruttoAmount() ? Color.black : Color.red);
		}
	}
}

class PanelEingangsrechnungKopfdaten_wnfBetrag_focusAdapter implements java.awt.event.FocusListener {
	private PanelEingangsrechnungKopfdaten adaptee;

	PanelEingangsrechnungKopfdaten_wnfBetrag_focusAdapter(PanelEingangsrechnungKopfdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfBetrag_focusLost();
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}

	public void focusGained(FocusEvent e) {
	}
}
