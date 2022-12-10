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
package com.lp.client.angebot;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.DialogGeaenderteKonditionenVK;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperBelegDateField;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperGotoKundeMapButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.IPartnerDto;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotFac;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngeboterledigungsgrundDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.benutzer.service.RechteFac;
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
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.GotoHelper;
import com.lp.util.Helper;

/*
 * <p>In diesem Detailfenster des Angebots werden Kopfdaten erfasst bzw.
 * geaendert.</p> <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 04.07.05</p> <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.21 $
 */
public class PanelAngebotKopfdaten extends PanelBasis implements PropertyChangeListener, ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAngebot intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAngebot tpAngebot = null;

	// dtos in diesem panel
	private AnsprechpartnerDto ansprechpartnerDto = null;
	private PersonalDto vertreterDto = null;
	private WaehrungDto waehrungDto = null;
	private KostenstelleDto kostenstelleDto = null;
	private WaehrungDto waehrungOriDto = null;

	private JPanel jpaWorkingOn = null;
	private Border innerBorder = null;

	static final public String ACTION_SPECIAL_ANGEBOTART_CHANGED = "action_special_angebotart_changed";
	static final public String ACTION_SPECIAL_KUNDE = "action_special_kunde";
	static final public String ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE = "action_special_ansprechpartner_kunde";
	static final public String ACTION_SPECIAL_VERTRETER_KUNDE = "action_special_vertreter_kunde";
	static final public String ACTION_SPECIAL_VERTRETER2 = "action_special_vertreter2";
	static final public String ACTION_SPECIAL_WAEHRUNG = "action_special_waehrung";
	static final public String ACTION_SPECIAL_KOSTENSTELLE = "action_special_kostenstelle";
	static final public String ACTION_SPECIAL_ANGEBOTEINHEIT_CHANGED = "action_special_angeboteinheit_changed";

	private static final String ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERADRESSE = "action_special_ansprechpartner_lieferadresse";
	private static final String ACTION_SPECIAL_ANSPRECHPARTNER_RECHNUNGSADRESSE = "action_special_ansprechpartner_rechungsadresse";
	private static final String ACTION_SPECIAL_LIEFERADRESSE_AUFTRAG = "action_special_lieferadresse_auftrag";
	private static final String ACTION_SPECIAL_RECHNUNGSADRESSE_AUFTRAG = "action_special_rechnungsadresse_auftrag";

	private WrapperLabel wlaAngebotart = null;
	private WrapperComboBox wcoAngebotart = null;
	private WrapperLabel wlaBelegdatum = null;
	private WrapperDateField wdfBelegdatum = null;

	private WrapperGotoButton jpaKunde = null;
	private PanelQueryFLR panelQueryFLRKunde = null;

	private WrapperButton wbuAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;

	private WrapperButton wbuVertreter = null;
	private PanelQueryFLR panelQueryFLRVertreter = null;

	private WrapperButton wbuVertreter2 = null;
	private WrapperTextField wtfVertreter2 = null;
	private PanelQueryFLR panelQueryFLRVertreter2 = null;

	private WrapperButton wbuWaehrung = null;
	private PanelQueryFLR panelQueryFLRWaehrung = null;
	private WrapperTextField wtfWaehrung = null;

	private WrapperLabel wlaKurs = null;
	private WrapperNumberField wnfKurs = null;

	private WrapperButton wbuKostenstelle = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;

	private WrapperLabel wlaAdresse = null;
	private WrapperTextField wtfKunde = null;
	private WrapperTextField wtfKundeAdresse = null;
	private WrapperLabel wlaKundeAbteilung = null;
	private WrapperTextField wtfKundeAbteilung = null;
	private WrapperTextField wtfAnsprechpartner = null;
	private WrapperTextField wtfVertreter = null;

	private WrapperButton jButtonLieferadresse = null;
	private PanelQueryFLR panelQueryFLRLieferadresse = null;

	private WrapperButton jButtonRechnungsadresse = null;
	private PanelQueryFLR panelQueryFLRRechnungsadresse = null;

	private WrapperTextField wtfKundeLieferadresse = null;
	private WrapperTextField wtfKundeRechnungsadresse = null;

	private WrapperButton wbuAnsprechpartnerRechungsadresse = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner_Rechungsadresse = null;
	private WrapperTextField wtfAnsprechpartnerRechungsadresse = null;

	private WrapperButton wbuAnsprechpartnerLieferadresse = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner_Lieferadresse = null;
	private WrapperTextField wtfAnsprechpartnerLieferadresse = null;

	private WrapperLabel wlaProjektbezeichnung = null;
	private WrapperTextField wtfProjektbezeichnung = null;
	private WrapperLabel wlaKundenanfrage = null;
	private WrapperTextField wtfKundenanfrage = null;

	private WrapperLabel wlaAnfragedatum = null;
	private WrapperDateField wdfAnfragedatum = null;
	private WrapperLabel wlaAngebotsgueltigkeitbis = null;
	private WrapperDateField wdfAngebotsgueltigkeitbis = null;

	private WrapperLabel wlaLieferzeitinstunden = null;
	private WrapperNumberField wnfLieferzeitinstunden = null;
	private WrapperLabel wlaAngeboteinheit = null;
	private WrapperComboBox wcoAngeboteinheit = null;

	private WrapperTextField wtfKostenstelle = null;

	private WrapperLabel wlaAngeboterledigungsgrund = null;
	private WrapperTextField wtfAngeboterledigungsgrund = null;

	private WrapperLabel labelKommission = null;
	private WrapperTextField wtfKommission = null;
	
	private WrapperGotoButton wbuErfassteauftrage = null;
	JList list = null;
	Map<?, ?> mErfassteAuftraege = new TreeMap<Object, Object>();

	private WrapperSelectField wsfProjekt = new WrapperSelectField(WrapperSelectField.PROJEKT, getInternalFrame(),
			true);

	private WrapperLabel wlaVersion;
	private WrapperNumberField wnfVersion;
	private JPanel jPanelVersionBelegdatum;

	/**
	 * Parametermandant PARAMETER_DEFAULT_ANGEBOT_GUELTIGKEITSART legt fest, ob die
	 * Gueltigkeitsdauer eines Angebots ueber einen Parametermandant definiert wird.
	 */
	private int iAngebotGueltigkeitsart = AngebotFac.ANGEBOTSGUELTIGKEIT_PARAMETERMANDANT;

	/**
	 * Parametermandant PARAMETER_DEFAULT_ANGEBOT_GUELTIGKEIT legt fest, wie lange
	 * ein Angebot per default gueltig ist.
	 */
	private int iAngebotGueltigkeitsdauer = -1;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI    der default Titel des Panels
	 * @param key           PK des Angebots
	 * @throws java.lang.Throwable Ausnahme
	 */
	public PanelAngebotKopfdaten(InternalFrame internalFrame, String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameAngebot) internalFrame;
		tpAngebot = intFrame.getTabbedPaneAngebot();

		jbInit();
		initComponents();
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {

			if (list.getSelectedIndex() == -1) {
				wbuErfassteauftrage.setOKey(null);

			} else {
				Iterator<?> it = mErfassteAuftraege.keySet().iterator();
				int i = 0;
				while (it.hasNext()) {

					Integer key = (Integer) it.next();

					if (i == list.getSelectedIndex()) {
						wbuErfassteauftrage.setOKey(key);
						break;
					}
					i++;
				}

			}
		}
	}

	void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		setLayout(new GridBagLayout());

		innerBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		setBorder(innerBorder);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche Buttons auf das Actionpanel setzen
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_STORNIEREN,
				PanelBasis.ACTION_DISCARD, PanelBasis.ACTION_PRINT };
		enableToolsPanelButtons(aWhichButtonIUse);

		// Workingpanel
		jpaWorkingOn = new JPanel();
		jpaWorkingOn.setLayout(new GridBagLayout());
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);

		wlaAngebotart = new WrapperLabel(LPMain.getTextRespectUISPr("angb.angebotart"));
		HelperClient.setDefaultsToComponent(wlaAngebotart, 115);

		wcoAngebotart = new WrapperComboBox();
		wcoAngebotart.setMandatoryFieldDB(true);
		wcoAngebotart.setActionCommand(ACTION_SPECIAL_ANGEBOTART_CHANGED);

		wlaVersion = new WrapperLabel();
		wlaVersion.setText(LPMain.getTextRespectUISPr("lp.beleg.version"));
		wlaVersion.setHorizontalAlignment(SwingConstants.RIGHT);
		wlaVersion.setVisible(false);
		wnfVersion = new WrapperNumberField();
		wnfVersion.setFractionDigits(0);
		wnfVersion.setActivatable(false);
		wnfVersion.setVisible(false);

		wbuAnsprechpartnerLieferadresse = new WrapperButton();
		wbuAnsprechpartnerLieferadresse.setText(LPMain.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartnerLieferadresse.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERADRESSE);
		wbuAnsprechpartnerLieferadresse.addActionListener(this);

		wtfAnsprechpartnerLieferadresse = new WrapperTextField();
		wtfAnsprechpartnerLieferadresse.setActivatable(false);
		wtfAnsprechpartnerLieferadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuAnsprechpartnerRechungsadresse = new WrapperButton();
		wbuAnsprechpartnerRechungsadresse.setText(LPMain.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartnerRechungsadresse.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_RECHNUNGSADRESSE);
		wbuAnsprechpartnerRechungsadresse.addActionListener(this);

		wtfAnsprechpartnerRechungsadresse = new WrapperTextField();
		wtfAnsprechpartnerRechungsadresse.setActivatable(false);
		wtfAnsprechpartnerRechungsadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		jButtonRechnungsadresse = new WrapperButton();
		jButtonRechnungsadresse.setText(LPMain.getTextRespectUISPr("button.rechnungsadresse"));
		jButtonRechnungsadresse.setActionCommand(ACTION_SPECIAL_RECHNUNGSADRESSE_AUFTRAG);
		jButtonRechnungsadresse.addActionListener(this);

		wtfKundeRechnungsadresse = new WrapperTextField();
		wtfKundeRechnungsadresse.setMandatoryField(true);
		wtfKundeRechnungsadresse.setActivatable(false);
		wtfKundeRechnungsadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		jButtonLieferadresse = new WrapperButton();
		jButtonLieferadresse.setText(LPMain.getTextRespectUISPr("button.lieferadresse"));
		jButtonLieferadresse.setActionCommand(ACTION_SPECIAL_LIEFERADRESSE_AUFTRAG);
		jButtonLieferadresse.addActionListener(this);

		wtfKundeLieferadresse = new WrapperTextField();
		wtfKundeLieferadresse.setMandatoryField(true);
		wtfKundeLieferadresse.setActivatable(false);
		wtfKundeLieferadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wlaBelegdatum = new WrapperLabel(LPMain.getTextRespectUISPr("label.belegdatum"));
		HelperClient.setDefaultsToComponent(wlaBelegdatum, 90);

		wdfBelegdatum = new WrapperBelegDateField(
				new BelegDatumClient(LPMain.getTheClient().getMandant(), new GregorianCalendar(), true));
		wdfBelegdatum.setMandatoryField(true);

		jpaKunde = new WrapperGotoKundeMapButton(new IPartnerDto() {
			public PartnerDto getPartnerDto() {
				return tpAngebot.getKundeDto() == null ? null : tpAngebot.getKundeDto().getPartnerDto();
			}
		});
		jpaKunde.setActionCommand(ACTION_SPECIAL_KUNDE);
		jpaKunde.setText(LPMain.getTextRespectUISPr("button.kunde"));
		jpaKunde.addActionListener(this);

		wtfKunde = new WrapperTextField();
		wtfKunde.setMandatoryField(true);
		wtfKunde.setActivatable(false);

		wtfKunde.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wlaAdresse = new WrapperLabel();
		wlaAdresse.setText(LPMain.getTextRespectUISPr("lp.adresse.kbez"));

		wtfKundeAdresse = new WrapperTextField();
		wtfKundeAdresse.setActivatable(false);
		wtfKundeAdresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wlaKundeAbteilung = new WrapperLabel(LPMain.getTextRespectUISPr("lp.abteilung"));

		wtfKundeAbteilung = new WrapperTextField();
		wtfKundeAbteilung.setActivatable(false);
		wtfKundeAbteilung.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuAnsprechpartner = new WrapperButton();
		wbuAnsprechpartner.setText(LPMain.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartner.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE);
		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner = new WrapperTextField();
		wtfAnsprechpartner.setActivatable(false);
		wtfAnsprechpartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuVertreter = new WrapperButton();
		wbuVertreter.setText(LPMain.getTextRespectUISPr("button.vertreter"));
		wbuVertreter.setActionCommand(ACTION_SPECIAL_VERTRETER_KUNDE);
		wbuVertreter.addActionListener(this);

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

		wlaProjektbezeichnung = new WrapperLabel(LPMain.getTextRespectUISPr("label.projekt"));
		wtfProjektbezeichnung = new WrapperTextField(80);

		wlaKundenanfrage = new WrapperLabel(LPMain.getTextRespectUISPr("anbg.kundenanfrage"));
		wtfKundenanfrage = new WrapperTextField();

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

		wlaAnfragedatum = new WrapperLabel(LPMain.getTextRespectUISPr("angb.anfragedatum"));
		wdfAnfragedatum = new WrapperDateField();

		wdfAnfragedatum.setMandatoryField(true);
		wlaAngebotsgueltigkeitbis = new WrapperLabel(
				LPMain.getTextRespectUISPr("angb.angebotgueltigbis"));
		wdfAngebotsgueltigkeitbis = new WrapperDateField();
		wdfAngebotsgueltigkeitbis.setMandatoryField(true);

		wlaLieferzeitinstunden = new WrapperLabel(LPMain.getTextRespectUISPr("angb.lieferzeit"));
		wnfLieferzeitinstunden = new WrapperNumberField();
		wnfLieferzeitinstunden.setMandatoryField(true);
		wnfLieferzeitinstunden.setMaximumIntegerDigits(3);
		wnfLieferzeitinstunden.setFractionDigits(0);

		wlaAngeboteinheit = new WrapperLabel(LPMain.getTextRespectUISPr("angb.lieferzeiteinheit"));
		wcoAngeboteinheit = new WrapperComboBox();
		wcoAngeboteinheit.setMandatoryFieldDB(true);
		wcoAngeboteinheit.setActionCommand(ACTION_SPECIAL_ANGEBOTEINHEIT_CHANGED);

		wbuKostenstelle = new WrapperButton();
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
		wbuKostenstelle.setText(LPMain.getTextRespectUISPr("button.kostenstelle"));
		wbuKostenstelle.addActionListener(this);

		wtfKostenstelle = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
		wtfKostenstelle.setActivatable(false);
		wtfKostenstelle.setMandatoryField(true);

		wlaAngeboterledigungsgrund = new WrapperLabel(
				LPMain.getTextRespectUISPr("angb.angeboterledigungsgrund"));
		wtfAngeboterledigungsgrund = new WrapperTextField();
		wtfAngeboterledigungsgrund.setActivatable(false);
		wtfAngeboterledigungsgrund.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuErfassteauftrage = new WrapperGotoButton(LPMain.getTextRespectUISPr("angb.erfasstauftraege"),
				GotoHelper.GOTO_AUFTRAG_AUSWAHL);

		
		labelKommission = new WrapperLabel();
		labelKommission.setText(LPMain.getTextRespectUISPr("lp.kommission"));
		wtfKommission = new WrapperTextField();
		
		list = new JList();
		// list.setFont(new java.awt.Font("monospaced", 0, 12));
		list.setFont(new java.awt.Font("monospaced", 0, 11));
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		// listScroller.setMinimumSize(new Dimension(250, 80));
		// listScroller.setPreferredSize(new Dimension(300, 80));
		list.addListSelectionListener(this);

		jPanelVersionBelegdatum = new JPanel();
		HvLayout layoutVersionBelegdatum = HvLayoutFactory.create(jPanelVersionBelegdatum, "ins 0",
				"push[fill,grow|fill|fill|fill]", "");
		layoutVersionBelegdatum.add(wlaVersion, "push, right, wmin 80").add(wnfVersion, "wmin 40")
				.add(wlaBelegdatum, "growx").add(wdfBelegdatum);
		// erste Zeile
		jpaWorkingOn.add(wlaAngebotart, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoAngebotart, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(jPanelVersionBelegdatum, new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// jpaWorkingOn.add(wlaBelegdatum, new GridBagConstraints(2, iZeile, 1,
		// 1,
		// 0.05, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		// new Insets(2, 2, 2, 2), 0, 0));
		// jpaWorkingOn.add(wdfBelegdatum, new GridBagConstraints(3, iZeile, 1,
		// 1,
		// 0.05,
		// 0.0 // workaround
		// , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		// new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(jpaKunde, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaAdresse, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKundeAdresse, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaKundeAbteilung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKundeAbteilung, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		ParametermandantDto parametermandantDtoZW = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ANGEBOT,
						ParameterFac.PARAMETER_ZWEITER_VERTRETER);
		boolean bZweiterVertreter = ((Boolean) parametermandantDtoZW.getCWertAsObject());

		if (bZweiterVertreter) {
			jpaWorkingOn.add(wbuVertreter, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wtfVertreter, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			wtfVertreter2.setMandatoryField(true);

			jpaWorkingOn.add(wbuVertreter2, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wtfVertreter2, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		} else {
			jpaWorkingOn.add(wbuVertreter, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wtfVertreter, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		iZeile++;
		jpaWorkingOn.add(jButtonLieferadresse, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKundeLieferadresse, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 250, 0));

		jpaWorkingOn.add(wbuAnsprechpartnerLieferadresse, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAnsprechpartnerLieferadresse, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(jButtonRechnungsadresse, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKundeRechnungsadresse, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuAnsprechpartnerRechungsadresse, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAnsprechpartnerRechungsadresse, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaKundenanfrage, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKundenanfrage, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaProjektbezeichnung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {
			ParametermandantDto parametermandantDto = DelegateFactory.getInstance().getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PROJEKT_IST_PFLICHTFELD);
			boolean bProjektIstPflichtfeld = ((Boolean) parametermandantDto.getCWertAsObject());
			if (bProjektIstPflichtfeld) {
				wsfProjekt.setMandatoryField(true);
			}

			jpaWorkingOn.add(wtfProjektbezeichnung, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wsfProjekt.getWrapperGotoButton(), new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 70, 0));
			jpaWorkingOn.add(wsfProjekt.getWrapperTextField(), new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		} else {
			jpaWorkingOn.add(wtfProjektbezeichnung, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		iZeile++;
		iZeile++;
		jpaWorkingOn.add(labelKommission, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKommission, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuWaehrung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfWaehrung, new GridBagConstraints(1, iZeile, 1, 1, 0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKurs, new GridBagConstraints(2, iZeile, 1, 1, 0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKurs, new GridBagConstraints(3, iZeile, 1, 1, 0.04, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaAnfragedatum, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfAnfragedatum, new GridBagConstraints(1, iZeile, 1, 1, 0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaAngebotsgueltigkeitbis, new GridBagConstraints(2, iZeile, 1, 1, 0.05, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfAngebotsgueltigkeitbis, new GridBagConstraints(3, iZeile, 1, 1, 0.04, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaLieferzeitinstunden, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfLieferzeitinstunden, new GridBagConstraints(1, iZeile, 1, 1, 0.05, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaAngeboteinheit, new GridBagConstraints(2, iZeile, 1, 1, 0.05, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoAngeboteinheit, new GridBagConstraints(3, iZeile, 1, 1, 0.04, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuKostenstelle, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelle, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaAngeboterledigungsgrund, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAngeboterledigungsgrund, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wbuErfassteauftrage, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(listScroller, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANGEBOT)
				&& LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ANGEBOTSZEITERFASSUNG)) {
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
							LPMain.getTextRespectUISPr("angb.startzeit"), Desktop.MY_OWN_NEW_ZEIT_START, null, null);
					getToolBar().addButtonRight("/com/lp/client/res/gear_stop.png",
							LPMain.getTextRespectUISPr("angb.stopzeit"), Desktop.MY_OWN_NEW_ZEIT_STOP, null, null);

					enableToolsPanelButtons(true, Desktop.MY_OWN_NEW_ZEIT_START, Desktop.MY_OWN_NEW_ZEIT_STOP);
				}

			}
		}

	}

	public void setDefaultsAusProjekt(Integer projektIId) throws Throwable {
		ProjektDto projektDto = DelegateFactory.getInstance().getProjektDelegate().projektFindByPrimaryKey(projektIId);

		wtfProjektbezeichnung.setText(projektDto.getCTitel());
		wsfProjekt.setKey(projektDto.getIId());

		vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
				.personalFindByPrimaryKey(projektDto.getPersonalIIdZugewiesener());
		if (vertreterDto != null && vertreterDto.getIId() != null) {
			wtfVertreter.setText(vertreterDto.getPartnerDto().formatTitelAnrede());
		}

		KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByiIdPartnercNrMandantOhneExc(
				projektDto.getPartnerIId(), LPMain.getTheClient().getMandant());
		if (kundeDto == null) {
			// Dann Kunde zuerst anlegen

			PartnerDto pDto = DelegateFactory.getInstance().getPartnerDelegate()
					.partnerFindByPrimaryKey(projektDto.getPartnerIId());

			boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kunde.auspartner.anlegen.teil1")
							+ pDto.formatFixTitelName1Name2()
							+ LPMain.getTextRespectUISPr("lp.kunde.auspartner.anlegen.teil2"));

			if (b == true) {
				Integer kundeIId = DelegateFactory.getInstance().getKundeDelegate()
						.createKundeAusPartner(projektDto.getPartnerIId());
				kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(kundeIId);

			} else {
				return;
			}

		}

		kundendatenVorbesetzen(kundeDto.getIId());

		if (projektDto.getAnsprechpartnerIId() != null) {
			ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(projektDto.getAnsprechpartnerIId());

			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatTitelAnrede());
		}

	}

	public void setDefaults() throws Throwable {
		ansprechpartnerDto = new AnsprechpartnerDto();
		vertreterDto = new PersonalDto();
		waehrungDto = new WaehrungDto();
		kostenstelleDto = new KostenstelleDto();
		waehrungOriDto = new WaehrungDto();

		// alle anzeigefelder zuruecksetzen
		leereAlleFelder(this);

		wcoAngebotart.setMap(DelegateFactory.getInstance().getAngebotServiceDelegate()
				.getAngebotarten(LPMain.getInstance().getUISprLocale()));

		Date currentDate = new Date(System.currentTimeMillis());

		wdfBelegdatum.setDate(currentDate);

		ParametermandantDto parametermandantDto = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ANGEBOT,
						ParameterFac.PARAMETER_ANFRAGEDATUM_VORBESETZEN);
		boolean bAnfragedatumVorbesetzen = ((Boolean) parametermandantDto.getCWertAsObject());

		if (bAnfragedatumVorbesetzen == true) {
			wdfAnfragedatum.setDate(currentDate);
		}

		// den Vorschlagswert fuer die Angebotgueltigkeitsdauer bestimmen
		parametermandantDto = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ANGEBOT,
				ParameterFac.PARAMETER_DEFAULT_ANGEBOT_GUELTIGKEITSART);

		iAngebotGueltigkeitsart = ((Integer) parametermandantDto.getCWertAsObject()).intValue();

		parametermandantDto = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ANGEBOT,
				ParameterFac.PARAMETER_DEFAULT_ANGEBOT_GUELTIGKEIT);

		iAngebotGueltigkeitsdauer = ((Integer) parametermandantDto.getCWertAsObject()).intValue();

		if (iAngebotGueltigkeitsart == AngebotFac.ANGEBOTSGUELTIGKEIT_PARAMETERMANDANT) {
			// die angegebene Anzahl von Tagen zum heutigen Tag dazuzaehlen
			GregorianCalendar gc = new GregorianCalendar(LPMain.getTheClient().getLocUi());
			gc.add(Calendar.DATE, iAngebotGueltigkeitsdauer);
			wdfAngebotsgueltigkeitbis.setDate(new Date(gc.getTimeInMillis()));
		} else if (iAngebotGueltigkeitsart == AngebotFac.ANGEBOTSGUELTIGKEIT_ENDEGESCHAEFTSJAHR) {
			parametermandantDto = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_GESCHAEFTSJAHRBEGINNMONAT);

			int iGeschaeftsjahrbeginnmonat = ((Integer) parametermandantDto.getCWertAsObject()).intValue();
			iGeschaeftsjahrbeginnmonat = iGeschaeftsjahrbeginnmonat - 1;

			GregorianCalendar gcReferenz = new GregorianCalendar(LPMain.getTheClient().getLocUi());
			// der erste Tag des naechsten Geschaeftsjahres
			GregorianCalendar gc = new GregorianCalendar(gcReferenz.get(Calendar.YEAR) + 1, iGeschaeftsjahrbeginnmonat,
					1);
			gc.add(Calendar.DATE, -1);
			gc.set(Calendar.YEAR, gcReferenz.get(Calendar.YEAR));
			wdfAngebotsgueltigkeitbis.setDate(new Date(gc.getTimeInMillis()));
		}

		myLogger.debug("Angebot gueltig bis: " + Helper.formatDatum(wdfAngebotsgueltigkeitbis.getDate(),
				LPMain.getTheClient().getLocUi()));

		// default waehrung kommt vom Mandanten
		waehrungDto = DelegateFactory.getInstance().getLocaleDelegate()
				.waehrungFindByPrimaryKey(DelegateFactory.getInstance().getMandantDelegate()
						.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant()).getWaehrungCNr());
		wtfWaehrung.setText(waehrungDto.getCNr());
		setzeWechselkurs();

		parametermandantDto = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ANGEBOT,
				ParameterFac.PARAMETER_DEFAULT_LIEFERZEIT_ANGEBOT);
		int defaultLieferzeitAngebot = ((Integer) parametermandantDto.getCWertAsObject()).intValue();

		wnfLieferzeitinstunden.setInteger(defaultLieferzeitAngebot);

		wcoAngeboteinheit.setMap(DelegateFactory.getInstance().getAngebotServiceDelegate().getAngeboteinheiten());
		wcoAngeboteinheit.setKeyOfSelectedItem(SystemFac.EINHEIT_WOCHE);

		// Vertreter aus kunde
		parametermandantDto = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_VERTRETER_VORSCHLAG_AUS_KUNDE);
		if (parametermandantDto.getCWert().equals("0")) {
			if (tpAngebot.getAngebotDto().getIId() == null) {
				vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
						.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());
				if (vertreterDto != null && vertreterDto.getIId() != null) {
					wtfVertreter.setText(vertreterDto.getPartnerDto().formatTitelAnrede());
				}
			}
		}
		
		wtfAnsprechpartner.resetColorAndTooltip();
		wtfAnsprechpartnerLieferadresse.resetColorAndTooltip();
		wtfAnsprechpartnerRechungsadresse.resetColorAndTooltip();

		
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			// der Kunde bestimmt eine Reihe von Vorbelegungen, u.a. die Sprache
			// der Drucke
			if (e.getSource() == panelQueryFLRKunde) {
				Integer iIdKunde = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				if (iIdKunde != null) {

					kundendatenVorbesetzen(iIdKunde);
				}
			} else if (e.getSource() == panelQueryFLRLieferadresse) {
				Integer pkKunde = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				KundeDto kundeLieferadresseDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(pkKunde);

				wtfKundeLieferadresse.setText(formatKundenadresse(kundeLieferadresseDto));

				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
						.getParameterDelegate()
						.getParametermandant(ParameterFac.PARAMETER_ANGEBOT_LIEFERADRESSE_ANSP_VORBESETZEN,
								ParameterFac.KATEGORIE_ANGEBOT, LPMain.getTheClient().getMandant());

				int iOption = (Integer) parameter.getCWertAsObject();
				AnsprechpartnerDto anspDtoTemp = null;

				if (iOption == 1 || iOption == 2) {
					anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerFindErstenEinesPartnersOhneExc(
									kundeLieferadresseDto.getPartnerDto().getIId());

				}

				if (anspDtoTemp != null) {
					wtfAnsprechpartnerLieferadresse.setText(anspDtoTemp.getPartnerDto().formatFixTitelVornameNachnameNTitel());
					tpAngebot.getAngebotDto().setAnsprechpartnerIIdLieferadresse(anspDtoTemp.getIId());
				} else {
					wtfAnsprechpartnerLieferadresse.setText("");
					tpAngebot.getAngebotDto().setAnsprechpartnerIIdLieferadresse(null);

				}

				tpAngebot.getAngebotDto().setKundeIIdLieferadresse(pkKunde);

			} else if (e.getSource() == panelQueryFLRRechnungsadresse) {
				Integer pkKunde = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(pkKunde);



				tpAngebot.getAngebotDto().setKundeIIdRechnungsadresse(pkKunde);
				
				wtfKundeRechnungsadresse.setText(formatKundenadresse(kundeDto));

				wtfAnsprechpartnerRechungsadresse.setText(null);
				
				
				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
						.getParameterDelegate()
						.getParametermandant(ParameterFac.PARAMETER_ANGEBOT_RECHNUNGSADRESSE_ANSP_VORBESETZEN,
								ParameterFac.KATEGORIE_ANGEBOT, LPMain.getTheClient().getMandant());

				int iOption = (Integer) parameter.getCWertAsObject();
				
				AnsprechpartnerDto anspDtoTemp = null;
				if (iOption == 1 || iOption == 2) {
					
					
					
					if (iOption == 2 && ansprechpartnerDto != null && ansprechpartnerDto.getIId() != null) {
						anspDtoTemp = ansprechpartnerDto;
					} else {
						anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
								.ansprechpartnerFindErstenEinesPartnersOhneExc(
										kundeDto.getPartnerDto().getIId());
					}
					
					
				}
				if (anspDtoTemp != null) {
					wtfAnsprechpartnerRechungsadresse.setText(anspDtoTemp.getPartnerDto().formatFixTitelVornameNachnameNTitel());
					tpAngebot.getAngebotDto().setAnsprechpartnerIIdRechnungsadresse(anspDtoTemp.getIId());
				} else {
					wtfAnsprechpartnerRechungsadresse.setText("");
					tpAngebot.getAngebotDto().setAnsprechpartnerIIdRechnungsadresse(null);

				}
				
				

			} else

			// Source ist der Ansprechpartnerauswahldialog
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				ansprechpartnerDto2Components(iIdAnsprechpartner);

				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
						.getParameterDelegate()
						.getParametermandant(ParameterFac.PARAMETER_ANGEBOT_LIEFERADRESSE_ANSP_VORBESETZEN,
								ParameterFac.KATEGORIE_ANGEBOT, LPMain.getTheClient().getMandant());

 				int iOption = (Integer) parameter.getCWertAsObject();
				if (iOption == 2) {
					if (tpAngebot.getAngebotDto().getKundeIIdLieferadresse() != null
							&& tpAngebot.getKundeDto().getIId() != null) {
						if (tpAngebot.getAngebotDto().getKundeIIdLieferadresse()
								.equals(tpAngebot.getKundeDto().getIId())) {
							AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance()
									.getAnsprechpartnerDelegate().ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

							wtfAnsprechpartnerLieferadresse
									.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
							tpAngebot.getAngebotDto().setAnsprechpartnerIIdLieferadresse(iIdAnsprechpartner);

						}
					}

				}

				parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
						.getParametermandant(ParameterFac.PARAMETER_ANGEBOT_RECHNUNGSADRESSE_ANSP_VORBESETZEN,
								ParameterFac.KATEGORIE_ANGEBOT, LPMain.getTheClient().getMandant());

				iOption = (Integer) parameter.getCWertAsObject();
				if (iOption == 2) {
					if (tpAngebot.getAngebotDto().getKundeIIdRechnungsadresse() != null
							&& tpAngebot.getKundeDto().getIId() != null) {
						if (tpAngebot.getAngebotDto().getKundeIIdRechnungsadresse()
								.equals(tpAngebot.getKundeDto().getIId())) {
							AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance()
									.getAnsprechpartnerDelegate().ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

							wtfAnsprechpartnerRechungsadresse
									.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
							tpAngebot.getAngebotDto().setAnsprechpartnerIIdRechnungsadresse(iIdAnsprechpartner);

						}
					}

				}

			} else if (e.getSource() == panelQueryFLRAnsprechpartner_Lieferadresse) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

				wtfAnsprechpartnerLieferadresse.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
				tpAngebot.getAngebotDto().setAnsprechpartnerIIdLieferadresse(iIdAnsprechpartner);
			} else

			// Source ist der Ansprechpartnerauswahldialog
			if (e.getSource() == panelQueryFLRAnsprechpartner_Rechungsadresse) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);

				wtfAnsprechpartnerRechungsadresse.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
				tpAngebot.getAngebotDto().setAnsprechpartnerIIdRechnungsadresse(iIdAnsprechpartner);
			} else

			// source ist MEIN Vertreterauswahldialog
			if (e.getSource() == panelQueryFLRVertreter) {
				Integer pkPersonal = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				vertreterDto = DelegateFactory.getInstance().getPersonalDelegate().personalFindByPrimaryKey(pkPersonal);

				if (vertreterDto != null && vertreterDto.getIId() != null) {
					wtfVertreter.setText(vertreterDto.getPartnerDto().formatTitelAnrede());
				}
			} else

			// source ist MEIN Vertreterauswahldialog
			if (e.getSource() == panelQueryFLRVertreter2) {
				Integer pkPersonal = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				PersonalDto vertreterDto2 = DelegateFactory.getInstance().getPersonalDelegate()
						.personalFindByPrimaryKey(pkPersonal);

				if (vertreterDto2 != null && vertreterDto2.getIId() != null) {
					wtfVertreter2.setText(vertreterDto2.getPartnerDto().formatTitelAnrede());
				}
				tpAngebot.getAngebotDto().setPersonalIIdVertreter2(pkPersonal);
			} else if (e.getSource() == panelQueryFLRWaehrung) {
				Object cNrWaehrung = ((ISourceEvent) e.getSource()).getIdSelected();

				if (cNrWaehrung != null) {
					waehrungDto = DelegateFactory.getInstance().getLocaleDelegate()
							.waehrungFindByPrimaryKey((String) cNrWaehrung);

					wtfWaehrung.setText(waehrungDto.getCNr());

					setzeWechselkurs();
				}
			} else if (e.getSource() == panelQueryFLRKostenstelle) {
				Object iIdKostenstelle = ((ISourceEvent) e.getSource()).getIdSelected();

				if (iIdKostenstelle != null) {
					kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
							.kostenstelleFindByPrimaryKey((Integer) iIdKostenstelle);

					wtfKostenstelle.setText(kostenstelleDto.getCBez());
				}
			}
		} else

		// das Event vom leeren Button behandeln
		if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			// source ist MEIN Ansprechpartnerdialog
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				ansprechpartnerDto = new AnsprechpartnerDto();
				wtfAnsprechpartner.setText("");
			} else if (e.getSource() == panelQueryFLRAnsprechpartner_Rechungsadresse) {
				tpAngebot.getAngebotDto().setAnsprechpartnerIIdRechnungsadresse(null);
				wtfAnsprechpartnerRechungsadresse.setText("");
			} else if (e.getSource() == panelQueryFLRAnsprechpartner_Lieferadresse) {
				tpAngebot.getAngebotDto().setAnsprechpartnerIIdLieferadresse(null);
				wtfAnsprechpartnerLieferadresse.setText("");
			}
		}
	}

	private void kundendatenVorbesetzen(Integer iIdKunde) throws ExceptionLP, Throwable {
		DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(iIdKunde, LocaleFac.BELEGART_ANGEBOT,
				getInternalFrame());

		if (tpAngebot.getAngebotDto().getWaehrungCNr() != null) {
			waehrungOriDto = DelegateFactory.getInstance().getLocaleDelegate()
					.waehrungFindByPrimaryKey(tpAngebot.getAngebotDto().getWaehrungCNr());
		}

		tpAngebot.setKundeDto(DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(iIdKunde));
		kundeDto2Components();

		AnsprechpartnerDto anspDtoTemp = null;
		// Ansprechpartner vorbesetzen?
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_ANGEBOT_ANSP_VORBESETZEN, ParameterFac.KATEGORIE_ANGEBOT,
						LPMain.getTheClient().getMandant());
		if ((Boolean) parameter.getCWertAsObject()) {
			anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindErstenEinesPartnersOhneExc(tpAngebot.getKundeDto().getPartnerIId());
		}
		if (anspDtoTemp != null) {
			ansprechpartnerDto2Components(anspDtoTemp.getIId());
		} else {
			ansprechpartnerDto2Components(null);
		}
		// den Benutzer fragen, ob er die urspruengliche Waehrung
		// beibehalten moechte
		if (waehrungOriDto.getCNr() != null && !waehrungOriDto.getCNr().equals(wtfWaehrung.getText())) {
			int indexWaehrungOriCNr = 0;
			int indexWaehrungNeuCNr = 1;
			int iAnzahlOptionen = 2;

			Object[] aOptionen = new Object[iAnzahlOptionen];
			aOptionen[indexWaehrungOriCNr] = waehrungOriDto.getCNr();
			aOptionen[indexWaehrungNeuCNr] = wtfWaehrung.getText();

			int iAuswahl = DialogFactory.showModalDialog(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.waehrungunterschiedlich"),
					LPMain.getTextRespectUISPr("lp.frage"), aOptionen, aOptionen[0]);

			if (iAuswahl == indexWaehrungOriCNr) {
				// die Belegwaehrung wird beibehalten -> Waehrung
				// und Wechselkurs zuruecksetzen
				waehrungDto = waehrungOriDto;
				wtfWaehrung.setText(waehrungDto.getCNr());
				setzeWechselkurs();
				waehrungOriDto = null;
			}
		}

		KundeDto kundeRechnungsadresseDto = null;
		if (tpAngebot.getKundeDto().getPartnerIIdRechnungsadresse() != null) {
			kundeRechnungsadresseDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByiIdPartnercNrMandantOhneExc(tpAngebot.getKundeDto().getPartnerIIdRechnungsadresse(),
							LPMain.getTheClient().getMandant());
		}

		if (kundeRechnungsadresseDto == null) {
			kundeRechnungsadresseDto = tpAngebot.getKundeDto();
		}

		wtfKundeRechnungsadresse.setText(formatKundenadresse(kundeRechnungsadresseDto));
		tpAngebot.getAngebotDto().setKundeIIdRechnungsadresse(kundeRechnungsadresseDto.getIId());

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_ANGEBOT_RECHNUNGSADRESSE_ANSP_VORBESETZEN, ParameterFac.KATEGORIE_ANGEBOT,
				LPMain.getTheClient().getMandant());

		anspDtoTemp = null;
		int iOption = (Integer) parameter.getCWertAsObject();
		if (iOption == 1) {
			anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindErstenEinesPartnersOhneExc(kundeRechnungsadresseDto.getPartnerDto().getIId());
		} else if (iOption == 2) {
			// Wenn Angebot/Rechnungsadresse ungleich, dann ersten
			// vorbesetzen
			if (kundeRechnungsadresseDto.getIId().equals(tpAngebot.getKundeDto().getIId())) {
				anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindErstenEinesPartnersOhneExc(tpAngebot.getKundeDto().getPartnerIId());
			} else {
				anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindErstenEinesPartnersOhneExc(
								kundeRechnungsadresseDto.getPartnerDto().getIId());
			}
		}

		if (anspDtoTemp != null) {
			wtfAnsprechpartnerRechungsadresse.setText(anspDtoTemp.getPartnerDto().formatFixTitelVornameNachnameNTitel());
			tpAngebot.getAngebotDto().setAnsprechpartnerIIdRechnungsadresse(anspDtoTemp.getIId());
		} else {
			wtfAnsprechpartnerRechungsadresse.setText("");
			tpAngebot.getAngebotDto().setAnsprechpartnerIIdRechnungsadresse(null);
		}

		wtfKundeLieferadresse.setText(formatKundenadresse(tpAngebot.getKundeDto()));
		tpAngebot.getAngebotDto().setKundeIIdLieferadresse(tpAngebot.getKundeDto().getIId());
		tpAngebot.getAngebotDto().setBMindermengenzuschlag(tpAngebot.getKundeDto().getBMindermengenzuschlag());
		
		
		
		wtfAnsprechpartnerLieferadresse.setText("");
		tpAngebot.getAngebotDto().setAnsprechpartnerIIdLieferadresse(null);

		if (iOption == 1 || iOption == 2) {

			if (anspDtoTemp != null) {
				wtfAnsprechpartnerLieferadresse.setText(anspDtoTemp.getPartnerDto().formatTitelAnrede());
				tpAngebot.getAngebotDto().setAnsprechpartnerIIdLieferadresse(anspDtoTemp.getIId());
			}
		}

	}

	/**
	 * Der Kunde bestimmt eine Reihe von Vorbelegungen.
	 * 
	 * @throws Throwable Ausnahme
	 */
	private void kundeDto2Components() throws Throwable {
		jpaKunde.setOKey(tpAngebot.getKundeDto().getIId());
		wtfKunde.setText(tpAngebot.getKundeDto().getPartnerDto().formatTitelAnrede());
		String sAdresse = tpAngebot.getKundeDto().getPartnerDto().formatAdresse();
		if (tpAngebot.getKundeDto().getPartnerDto().getCKbez() != null)
			;
		sAdresse = sAdresse + "  /  " + tpAngebot.getKundeDto().getPartnerDto().getCKbez();
		wtfKundeAdresse.setText(sAdresse);
		wtfKundeAbteilung.setText(tpAngebot.getKundeDto().getPartnerDto().getCName3vorname2abteilung());

		// den Ansprechpartner beim Kunden zuruecksetzen
		ansprechpartnerDto = new AnsprechpartnerDto();
		wtfAnsprechpartner.setText("");

		// Vertreter aus kunde
		ParametermandantDto parametermandantDto = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_VERTRETER_VORSCHLAG_AUS_KUNDE);
		if (parametermandantDto.getCWert().equals("1")) {
			// den Default Vertreter des Kunden setzen
			Integer iIdPersonal = tpAngebot.getKundeDto().getPersonaliIdProvisionsempfaenger();

			if (iIdPersonal != null) {

				boolean b = DelegateFactory.getInstance().getPersonalDelegate()
						.istPersonalVerstecktOderAusgetreten(iIdPersonal);
				if (b == true) {
					vertreterDto = new PersonalDto();
					wtfVertreter.setText("");
					;
				} else {

					vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey(iIdPersonal);

					if (vertreterDto != null && vertreterDto.getIId() != null) {
						wtfVertreter.setText(vertreterDto.getPartnerDto().formatTitelAnrede());
					}
				}
			} else {
				vertreterDto = new PersonalDto();
				wtfVertreter.setText("");
			}
		}
		// default waehrung des auftrags kommt aus dem kunden
		waehrungDto = DelegateFactory.getInstance().getLocaleDelegate()
				.waehrungFindByPrimaryKey(tpAngebot.getKundeDto().getCWaehrung());
		wtfWaehrung.setText(waehrungDto.getCNr());
		setzeWechselkurs();

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_KOSTENSTELLE_IN_VK_BELEGEN_VORBESETZT,
						ParameterFac.KATEGORIE_ALLGEMEIN, LPMain.getTheClient().getMandant());
		boolean bKostenstelleVorbesetzten = ((Boolean) parameter.getCWertAsObject());

		if (tpAngebot.getKundeDto().getKostenstelleIId() != null && bKostenstelleVorbesetzten == true) {
			kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
					.kostenstelleFindByPrimaryKey(tpAngebot.getKundeDto().getKostenstelleIId());

			wtfKostenstelle.setText(kostenstelleDto.getCBez());
		} else {
			wtfKostenstelle.setText("");
		}
	}

	/**
	 * Einen Ansprechpartner anzeigen. <br>
	 * Der Ansprechpartner kann null sein.
	 * 
	 * @param iIdAnsprechpartnerI PK des Ansprechpartners
	 * @throws java.lang.Throwable Ausnahme
	 */
	private void ansprechpartnerDto2Components(Integer iIdAnsprechpartnerI) throws Throwable {
		if (iIdAnsprechpartnerI != null) {
			ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerI);

			wtfAnsprechpartner.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
		} else {
			wtfAnsprechpartner.setText("");
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		// dtos aus dem tabbed pane zuruecksetzen
		tpAngebot.resetDtos();
		tpAngebot.setTitleAngebot(LPMain.getTextRespectUISPr("angb.panel.kopfdaten"));

		// diese panel zuruecksetzen
		setDefaults();

		clearStatusbar();

		setzeWaehrungAenderbar();

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ANGEBOT;
	}

	void dialogQueryLieferadresse(ActionEvent e) throws Throwable {
		panelQueryFLRLieferadresse = PartnerFilterFactory.getInstance().createPanelFLRKunde(intFrame, true, false,
				tpAngebot.getAngebotDto().getKundeIIdLieferadresse());

		new DialogQuery(panelQueryFLRLieferadresse);
	}

	private void dialogQueryRechnungsadresse(ActionEvent e) throws Throwable {
		panelQueryFLRRechnungsadresse = PartnerFilterFactory.getInstance().createPanelFLRKunde(intFrame, true, false,
				tpAngebot.getAngebotDto().getKundeIIdRechnungsadresse());

		new DialogQuery(panelQueryFLRRechnungsadresse);
	}

	private void dialogQueryAnsprechpartnerLieferadresse(ActionEvent e) throws Throwable {
		if (tpAngebot.getAngebotDto().getKundeIIdLieferadresse() == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
		} else {

			KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(tpAngebot.getAngebotDto().getKundeIIdLieferadresse());
			panelQueryFLRAnsprechpartner_Lieferadresse = PartnerFilterFactory.getInstance()
					.createPanelFLRAnsprechpartner(intFrame, kdDto.getPartnerIId(),
							tpAngebot.getAngebotDto().getAnsprechpartnerIIdLieferadresse(), true, true);

			new DialogQuery(panelQueryFLRAnsprechpartner_Lieferadresse);
		}
	}

	private void dialogQueryAnsprechpartnerRechnungsadresse(ActionEvent e) throws Throwable {
		if (tpAngebot.getAngebotDto().getKundeIIdRechnungsadresse() == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
		} else {
			KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(tpAngebot.getAngebotDto().getKundeIIdRechnungsadresse());
			panelQueryFLRAnsprechpartner_Rechungsadresse = PartnerFilterFactory.getInstance()
					.createPanelFLRAnsprechpartner(intFrame, kdDto.getPartnerIId(),
							tpAngebot.getAngebotDto().getAnsprechpartnerIIdRechnungsadresse(), true, true);

			new DialogQuery(panelQueryFLRAnsprechpartner_Rechungsadresse);
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE)) {
			dialogQueryKunde(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE)) {
			dialogQueryAnsprechpartner(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER_KUNDE)) {
			dialogQueryVertreter(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER2)) {
			dialogQueryVertreter2(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_WAEHRUNG)) {
			dialogQueryWaehrung(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle(e);
		} else if (e.getActionCommand().equals(Desktop.MY_OWN_NEW_ZEIT_START)
				|| e.getActionCommand().equals(Desktop.MY_OWN_NEW_ZEIT_STOP)) {

			LPMain.getInstance().getDesktop().zeitbuchungAufBeleg(e.getActionCommand(), LocaleFac.BELEGART_ANGEBOT,
					tpAngebot.getAngebotDto().getIId());

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERADRESSE_AUFTRAG)) {
			dialogQueryLieferadresse(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_RECHNUNGSADRESSE_AUFTRAG)) {
			dialogQueryRechnungsadresse(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERADRESSE)) {
			dialogQueryAnsprechpartnerLieferadresse(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER_RECHNUNGSADRESSE)) {
			dialogQueryAnsprechpartnerRechnungsadresse(e);
		}
	}

	void dialogQueryKunde(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance().createPanelFLRKunde(intFrame, true, false,
				tpAngebot.getKundeDto().getIId());
		new DialogQuery(panelQueryFLRKunde);
	}

	void dialogQueryAnsprechpartner(ActionEvent e) throws Throwable {
		if (tpAngebot.getKundeDto().getIId() == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
		} else {

			panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance().createPanelFLRAnsprechpartner(intFrame,
					tpAngebot.getKundeDto().getPartnerDto().getIId(), ansprechpartnerDto.getIId(), true, true);
			new DialogQuery(panelQueryFLRAnsprechpartner);
		}
	}

	private void dialogQueryVertreter(ActionEvent e) throws Throwable {
		panelQueryFLRVertreter = PersonalFilterFactory.getInstance().createPanelFLRPersonal(intFrame, true, false,
				vertreterDto.getIId());

		new DialogQuery(panelQueryFLRVertreter);
	}

	private void dialogQueryVertreter2(ActionEvent e) throws Throwable {
		panelQueryFLRVertreter2 = PersonalFilterFactory.getInstance().createPanelFLRPersonal(intFrame, true, false,
				tpAngebot.getAngebotDto().getPersonalIIdVertreter2());

		new DialogQuery(panelQueryFLRVertreter2);
	}

	private void dialogQueryWaehrung(ActionEvent e) throws Throwable {
		panelQueryFLRWaehrung = FinanzFilterFactory.getInstance().createPanelFLRWaehrung(getInternalFrame(),
				waehrungDto.getCNr());
		new DialogQuery(panelQueryFLRWaehrung);
	}

	private void dialogQueryKostenstelle(ActionEvent e) throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance().createPanelFLRKostenstelle(getInternalFrame(),
				false, false, kostenstelleDto.getIId());

		new DialogQuery(panelQueryFLRKostenstelle);
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAngebot.getAngebotDto().getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAngebot.getAngebotDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAngebot.getAngebotDto().getPersonalIIdAendern());
		setStatusbarTAendern(tpAngebot.getAngebotDto().getTAendern());
		setStatusbarStatusCNr(tpAngebot.getAngebotStatus());
		String status = DelegateFactory.getInstance().getVersandDelegate().getVersandstatus(LocaleFac.BELEGART_ANGEBOT,
				tpAngebot.getAngebotDto().getIId());
		if (status != null) {
			status = LPMain.getTextRespectUISPr("lp.versandstatus") + ": " + status;
		}
		setStatusbarSpalte5(status);
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false); // Stati aller Components
		// aktualisieren

		// neu einlesen, ausloeser war ev. ein refresh oder discard
		Integer oKey = tpAngebot.getAngebotDto().getIId();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		if (oKey != null) {
			tpAngebot.setAngebotDto(
					DelegateFactory.getInstance().getAngebotDelegate().angebotFindByPrimaryKey((Integer) oKey));

			tpAngebot.setTitleAngebot(LPMain.getTextRespectUISPr("angb.panel.kopfdaten"));

			dto2Components();
		}

		setzeWaehrungAenderbar();
	}

	private String formatKundenadresse(KundeDto kundeDto) throws Throwable {
		String sAdresse = kundeDto.getPartnerDto().formatTitelAnrede();

		if (kundeDto.getPartnerDto().formatAdresse() != null && !kundeDto.getPartnerDto().formatAdresse().equals("")) {
			sAdresse = sAdresse + ", " + kundeDto.getPartnerDto().formatAdresse();
		}

		if (kundeDto.getPartnerDto().getCKbez() != null)
			;
		sAdresse = sAdresse + "  /  " + kundeDto.getPartnerDto().getCKbez();
		return sAdresse;
	}

	private void dto2Components() throws Throwable {
		if (tpAngebot.pruefeAktuellesAngebot()) {
			wcoAngebotart.setKeyOfSelectedItem(tpAngebot.getAngebotDto().getArtCNr());
			setVersionComponents(tpAngebot.getAngebotDto().getIVersion());
			wdfBelegdatum.setTimestamp(tpAngebot.getAngebotDto().getTBelegdatum());
			wdfAnfragedatum.setTimestamp(tpAngebot.getAngebotDto().getTAnfragedatum());
			wdfAngebotsgueltigkeitbis.setTimestamp(tpAngebot.getAngebotDto().getTAngebotsgueltigkeitbis());
			jpaKunde.setOKey(tpAngebot.getKundeDto().getIId());
			wtfKunde.setText(tpAngebot.getKundeDto().getPartnerDto().formatTitelAnrede());
			String sAdresse = tpAngebot.getKundeDto().getPartnerDto().formatAdresse();
			if (tpAngebot.getKundeDto().getPartnerDto().getCKbez() != null)
				;
			sAdresse = sAdresse + "  /  " + tpAngebot.getKundeDto().getPartnerDto().getCKbez();
			wtfKundeAdresse.setText(sAdresse);
			wtfKundeAbteilung.setText(tpAngebot.getKundeDto().getPartnerDto().getCName3vorname2abteilung());

			Integer ansprechpartnerIId = tpAngebot.getAngebotDto().getAnsprechpartnerIIdKunde();

			if (ansprechpartnerIId != null) {
				ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(ansprechpartnerIId);

				wtfAnsprechpartner.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
			}
			
			HelperClient.pruefeAnsprechpartner(tpAngebot.getKundeDto().getPartnerIId(), ansprechpartnerIId, wtfAnsprechpartner);
			

			Integer vertreterIId = tpAngebot.getAngebotDto().getPersonalIIdVertreter();

			if (vertreterIId != null) {
				vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
						.personalFindByPrimaryKey(vertreterIId);

				wtfVertreter.setText(vertreterDto.getPartnerDto().formatTitelAnrede());
			}

			if (tpAngebot.getAngebotDto().getPersonalIIdVertreter2() != null) {
				PersonalDto vertreterDto2 = DelegateFactory.getInstance().getPersonalDelegate()
						.personalFindByPrimaryKey(tpAngebot.getAngebotDto().getPersonalIIdVertreter2());

				wtfVertreter2.setText(vertreterDto2.getPartnerDto().formatTitelAnrede());
			} else {
				wtfVertreter2.setText(null);
			}

			// Lieferadresse bestimmen
			Integer pkKundeLiefer = tpAngebot.getAngebotDto().getKundeIIdLieferadresse();
			KundeDto kundeLieferadresseDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(pkKundeLiefer);
			wtfKundeLieferadresse.setText(formatKundenadresse(kundeLieferadresseDto));

			// Rechnungsadresse bestimmen
			Integer pkKundeRechnung = tpAngebot.getAngebotDto().getKundeIIdRechnungsadresse();
			KundeDto kundeRechnungsadresseDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(pkKundeRechnung);

			wtfKundeRechnungsadresse.setText(formatKundenadresse(kundeRechnungsadresseDto));

			Integer iIdAnsprechpartnerLieferadresse = tpAngebot.getAngebotDto().getAnsprechpartnerIIdLieferadresse();
			if (iIdAnsprechpartnerLieferadresse != null) {
				AnsprechpartnerDto ansprechpartnerDtoLieferadresse = DelegateFactory.getInstance()
						.getAnsprechpartnerDelegate().ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerLieferadresse);
				wtfAnsprechpartnerLieferadresse
						.setText(ansprechpartnerDtoLieferadresse.formatFixTitelVornameNachnameNTitel());
			} else {
				wtfAnsprechpartnerLieferadresse.setText(null);
			}

			
			HelperClient.pruefeAnsprechpartner(kundeLieferadresseDto.getPartnerIId(), iIdAnsprechpartnerLieferadresse, wtfAnsprechpartnerLieferadresse);
			
			Integer iIdAnsprechpartnerRechnungsadresse = tpAngebot.getAngebotDto()
					.getAnsprechpartnerIIdRechnungsadresse();
			if (iIdAnsprechpartnerRechnungsadresse != null) {
				AnsprechpartnerDto ansprechpartnerDtoRechnungsadresse = DelegateFactory.getInstance()
						.getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerRechnungsadresse);
				wtfAnsprechpartnerRechungsadresse
						.setText(ansprechpartnerDtoRechnungsadresse.formatFixTitelVornameNachnameNTitel());
			} else {
				wtfAnsprechpartnerRechungsadresse.setText(null);
			}
			
			
			HelperClient.pruefeAnsprechpartner(kundeRechnungsadresseDto.getPartnerIId(), iIdAnsprechpartnerRechnungsadresse, wtfAnsprechpartnerRechungsadresse);


			wtfProjektbezeichnung.setText(tpAngebot.getAngebotDto().getCBez());
			wtfKundenanfrage.setText(tpAngebot.getAngebotDto().getCKundenanfrage());
			wtfKommission.setText(tpAngebot.getAngebotDto().getCKommission());
			wsfProjekt.setKey(tpAngebot.getAngebotDto().getProjektIId());
			wtfWaehrung.setText(tpAngebot.getAngebotDto().getWaehrungCNr());
			wnfKurs.setDouble(tpAngebot.getAngebotDto().getFWechselkursmandantwaehrungzubelegwaehrung());

			String angeboteinheitCNr = tpAngebot.getAngebotDto().getAngeboteinheitCNr();
			double dLieferzeit = tpAngebot.getAngebotDto().getILieferzeitinstunden().doubleValue();

			if (angeboteinheitCNr.equals(AngebotServiceFac.ANGEBOTEINHEIT_TAG)) {
				dLieferzeit /= 24;
			} else if (angeboteinheitCNr.equals(AngebotServiceFac.ANGEBOTEINHEIT_WOCHE)) {
				dLieferzeit /= 24 * 7;
			}

			wnfLieferzeitinstunden.setInteger(new Integer(new Double(dLieferzeit).intValue()));
			wcoAngeboteinheit.setKeyOfSelectedItem(angeboteinheitCNr);

			Integer kostenstelleIId = tpAngebot.getAngebotDto().getKostenstelleIId();

			if (kostenstelleIId != null) {
				kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
						.kostenstelleFindByPrimaryKey(kostenstelleIId);

				wtfKostenstelle.setText(kostenstelleDto.getCBez());
			}

			if (tpAngebot.getAngebotDto().getAngeboterledigungsgrundCNr() != null) {

				AngeboterledigungsgrundDto grundDto = DelegateFactory.getInstance().getAngebotServiceDelegate()
						.angeboterledigungsgrundFindByPrimaryKey(
								tpAngebot.getAngebotDto().getAngeboterledigungsgrundCNr());

				wtfAngeboterledigungsgrund.setText(grundDto.getBezeichnung());
			} else {
				wtfAngeboterledigungsgrund.setText(null);
			}

			mErfassteAuftraege = DelegateFactory.getInstance().getAuftragDelegate()
					.getListeDerErfasstenAuftraege(tpAngebot.getAngebotDto().getIId());

			list.removeAll();
			wbuErfassteauftrage.setOKey(null);
			// Iterator it = mGegenkonten.keySet().iterator();
			// String[] zeilen = new String[mGegenkonten.size()];
			// int i = 0;
			// while (it.hasNext()) {
			// zeilen[i] = (String) mGegenkonten.get(it.next());
			// i++;
			// }
			// list.setListData(zeilen);

			Object[] tempZeilen = mErfassteAuftraege.values().toArray();
			list.setListData(tempZeilen);

			aktualisiereStatusbar();
		}
	}

	private void setVersionComponents(Integer iVersion) {
		wnfVersion.setInteger(iVersion);
		wnfVersion.setVisible(iVersion != null);
		wlaVersion.setVisible(iVersion != null);
	}

	private String formatAuftraege(AuftragDto[] aAuftragDtoI) {
		String cFormat = "";

		if (aAuftragDtoI != null && aAuftragDtoI.length > 0) {
			for (int i = 0; i < aAuftragDtoI.length; i++) {
				cFormat += aAuftragDtoI[i].getCNr();
				cFormat += " | ";
			}
		}

		if (cFormat.length() > 3) {
			cFormat = cFormat.substring(0, cFormat.length() - 3);
		}

		return cFormat;
	}

	private void initKonditionen() throws Throwable {

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_DEFAULT_NACHFASSTERMIN, ParameterFac.KATEGORIE_ANGEBOT,
						LPMain.getTheClient().getMandant());

		int defaultNachfasstermin = (Integer) parameter.getCWertAsObject();

		tpAngebot.getAngebotDto().setTNachfasstermin(Helper
				.cutTimestamp(Helper.addiereTageZuTimestamp(wdfBelegdatum.getTimestamp(), defaultNachfasstermin)));
		tpAngebot.getAngebotDto().setFAuftragswahrscheinlichkeit(new Double(0));
		tpAngebot.getAngebotDto().setFAllgemeinerRabattsatz(new Double(0));
		tpAngebot.getAngebotDto().setFVersteckterAufschlag(new Double(0));
		tpAngebot.getAngebotDto().setFProjektierungsrabattsatz(new Double(0));

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_DEFAULT_MIT_ZUSAMMENFASSUNG, ParameterFac.KATEGORIE_ANGEBOT,
				LPMain.getTheClient().getMandant());

		boolean bMitZusammenfassung = (Boolean) parameter.getCWertAsObject();

		tpAngebot.getAngebotDto().setBMitzusammenfassung(Helper.boolean2Short(bMitZusammenfassung));

		Double dRabattsatz = new Double(0);

		if (tpAngebot.getKundeDto().getFRabattsatz() != null) {
			dRabattsatz = new Double(tpAngebot.getKundeDto().getFRabattsatz().doubleValue());
		}

		tpAngebot.getAngebotDto().setFAllgemeinerRabattsatz(dRabattsatz);

		if (tpAngebot.getKundeDto().getLieferartIId() != null) {
			tpAngebot.getAngebotDto().setLieferartIId(tpAngebot.getKundeDto().getLieferartIId());
		}

		if (tpAngebot.getKundeDto().getZahlungszielIId() != null) {
			tpAngebot.getAngebotDto().setZahlungszielIId(tpAngebot.getKundeDto().getZahlungszielIId());
		}

		if (tpAngebot.getKundeDto().getSpediteurIId() != null) {
			tpAngebot.getAngebotDto().setSpediteurIId(tpAngebot.getKundeDto().getSpediteurIId());
		}

		Integer iGarantie = new Integer(0);

		if (tpAngebot.getKundeDto().getIGarantieinmonaten() != null) {
			iGarantie = tpAngebot.getKundeDto().getIGarantieinmonaten();
		}

		tpAngebot.getAngebotDto().setIGarantie(iGarantie);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			DelegateFactory.getInstance().getKundeDelegate()
					.pruefeKreditlimit(tpAngebot.getAngebotDto().getKundeIIdAngebotsadresse(), getInternalFrame());

			if (tpAngebot.getAngebotDto().getIId() == null) {
				Integer iIdAngebot = DelegateFactory.getInstance().getAngebotDelegate()
						.createAngebot(tpAngebot.getAngebotDto());

				tpAngebot.setAngebotDto(
						DelegateFactory.getInstance().getAngebotDelegate().angebotFindByPrimaryKey(iIdAngebot));

				setKeyWhenDetailPanel(iIdAngebot);

			} else {
				boolean bUpdate = true;

				if (tpAngebot.getAngebotDto().getStatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_OFFEN)) {
					if (DialogFactory.showMeldung(LPMain.getTextRespectUISPr("lp.hint.offennachangelegt"),
							LPMain.getTextRespectUISPr("lp.hint"),
							javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.NO_OPTION) {
						bUpdate = false;
					}
				}

				// SP1141
				AngebotDto aDtoVorhanden = DelegateFactory.getInstance().getAngebotDelegate()
						.angebotFindByPrimaryKey(tpAngebot.getAngebotDto().getIId());
				if (!aDtoVorhanden.getKundeIIdAngebotsadresse()
						.equals(tpAngebot.getAngebotDto().getKundeIIdAngebotsadresse())) {

					DialogGeaenderteKonditionenVK dialog = new DialogGeaenderteKonditionenVK(tpAngebot.getAngebotDto(),
							tpAngebot.getAngebotDto().getKundeIIdAngebotsadresse(), getInternalFrame());
					LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(dialog);

					if (dialog.bKonditionenUnterschiedlich == true) {
						dialog.setVisible(true);

						if (dialog.bAbgebrochen == false) {
							tpAngebot.setAngebotDto((AngebotDto) dialog.getBelegVerkaufDto());
						} else {
							bUpdate = false;
							return;
						}
					}

				}

				if (bUpdate) {
					boolean bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben = DelegateFactory.getInstance()
							.getAngebotDelegate().updateAngebot(tpAngebot.getAngebotDto(),
									waehrungOriDto == null ? null : waehrungOriDto.getCNr());

					if (bMwstSatzWurdeVonNullGeaendertUndEsGibtHandeingaben == true) {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"), LPMain
								.getTextRespectUISPr("lp.error.mwstvonnullgeaendertundhandeingaben"));

					}

				}
			}

			super.eventActionSave(e, true);

			eventYouAreSelected(false);
		}
	}

	private void components2Dto() throws Throwable {
		// es wird ein neues Angebot angelegt
		if (tpAngebot.getAngebotDto() == null || tpAngebot.getAngebotDto().getIId() == null) {
			if (tpAngebot.getAngebotDto() == null) {
				tpAngebot.setAngebotDto(new AngebotDto());
			}
			tpAngebot.getAngebotDto().setMandantCNr(LPMain.getTheClient().getMandant());
			tpAngebot.getAngebotDto().setArtCNr((String) wcoAngebotart.getKeyOfSelectedItem());
			tpAngebot.getAngebotDto().setStatusCNr(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT);
			tpAngebot.getAngebotDto().setBelegartCNr(LocaleFac.BELEGART_ANGEBOT);

			initKonditionen();
		}

		tpAngebot.getAngebotDto().setTBelegdatum(wdfBelegdatum.getTimestamp());
		tpAngebot.getAngebotDto().setTAnfragedatum(wdfAnfragedatum.getTimestamp());
		tpAngebot.getAngebotDto().setTAngebotsgueltigkeitbis(wdfAngebotsgueltigkeitbis.getTimestamp());
		tpAngebot.getAngebotDto().setKundeIIdAngebotsadresse(tpAngebot.getKundeDto().getIId());
		tpAngebot.getAngebotDto().setAnsprechpartnerIIdKunde(ansprechpartnerDto.getIId());
		tpAngebot.getAngebotDto().setPersonalIIdVertreter(vertreterDto.getIId());

		tpAngebot.getAngebotDto().setProjektIId(wsfProjekt.getIKey());

		tpAngebot.getAngebotDto().setCBez(wtfProjektbezeichnung.getText());
		tpAngebot.getAngebotDto().setCKundenanfrage(wtfKundenanfrage.getText());
		tpAngebot.getAngebotDto().setCKommission(wtfKommission.getText());
		tpAngebot.getAngebotDto().setWaehrungCNr(wtfWaehrung.getText());
		tpAngebot.getAngebotDto().setFWechselkursmandantwaehrungzubelegwaehrung(wnfKurs.getDouble());

		String angeboteinheitCNr = (String) wcoAngeboteinheit.getKeyOfSelectedItem();
		double dLieferzeit = wnfLieferzeitinstunden.getInteger().doubleValue();

		if (angeboteinheitCNr.equals(AngebotServiceFac.ANGEBOTEINHEIT_TAG)) {
			dLieferzeit *= 24;
		} else if (angeboteinheitCNr.equals(AngebotServiceFac.ANGEBOTEINHEIT_WOCHE)) {
			dLieferzeit *= 24 * 7;
		}

		tpAngebot.getAngebotDto().setILieferzeitinstunden(new Integer(new Double(dLieferzeit).intValue()));
		tpAngebot.getAngebotDto().setAngeboteinheitCNr(angeboteinheitCNr);
		tpAngebot.getAngebotDto().setKostenstelleIId(kostenstelleDto.getIId());
	}

	/**
	 * Eine andere Waehrung wurde ausgewaehlt.
	 * 
	 * @param evt Ereignis
	 * @throws java.lang.Throwable Ausnhame
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == wtfWaehrung && evt.getPropertyName().equals("value")) {
			try {
				setzeWechselkurs();
			} catch (Throwable t) {
				LPMain.getInstance().exitFrame(getInternalFrame(), t);
			}
		}
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;

		switch (exfc.getICode()) {
		case EJBExceptionLP.FEHLER_KEIN_PARTNER_GEWAEHLT:
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
			break;

		// manuellerledigen: 6 Fehler abfangen
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

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		if (tpAngebot.aktualisiereAngebotstatusDurchButtonUpdate()) {
			super.eventActionUpdate(aE, false);

			setzeWaehrungAenderbar();
		}
	}

	/**
	 * Eine Angebot auf 'Storniert' setzen. <br>
	 * Angebote koennen nicht physikalisch geloescht werden.
	 * 
	 * @param e                     Ereignis
	 * @param bAdministrateLockKeyI boolean
	 * @param bNeedNoDeleteI        boolean
	 * @throws Throwable Ausnahme
	 */
	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (!isLockedDlg()) {
			if (tpAngebot.getAngebotDto().getStatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_OFFEN)
					|| tpAngebot.getAngebotDto().getStatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)) {
				DelegateFactory.getInstance().getAngebotDelegate().storniereAngebot(tpAngebot.getAngebotDto());
				super.eventActionDelete(e, false, false); // das Angebot
				// existiert
				// weiterhin!
				eventYouAreSelected(false);
			} else {
				tpAngebot.showStatusMessage("lp.hint", "angb.warning.angebotkannnichtstorniertwerden");
			}
		}
	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		tpAngebot.printAngebot();
		eventYouAreSelected(false);
	}

	/**
	 * Die Waehrung ist aenderbar, wenn ... ... ein neues Angebot angelegt wird ...
	 * das Angebot den Status ('Angelegt' oder 'Offen') hat und keine
	 * mengenbehafteten Positionen hat und der Datensatz gelockt ist. <br>
	 * Wenn die Waehrung nicht geaendert werden darf, darf auch der Kunde nicht
	 * geaendert werden, da dieser seine Waehrung mitbringt.
	 * 
	 * @throws java.lang.Throwable Ausnahme
	 */
	private void setzeWaehrungAenderbar() throws Throwable {
		if (tpAngebot.getAngebotDto() != null) {
			if (tpAngebot.getAngebotDto().getIId() == null
					|| ((tpAngebot.getAngebotDto().getStatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT)
							|| tpAngebot.getAngebotDto().getStatusCNr().equals(AngebotServiceFac.ANGEBOTSTATUS_OFFEN))
							&& DelegateFactory.getInstance().getAngebotpositionDelegate()
									.getAnzahlMengenbehafteteAngebotpositionen(tpAngebot.getAngebotDto().getIId()) == 0)
							&& isLockedByMeOrForNew()) {
				wbuWaehrung.setEnabled(true);
				jpaKunde.setEnabled(true);
				jpaKunde.setOKey(null);
			} else {
				wbuWaehrung.setEnabled(false);
				// jButtonKunde.setEnabled(false);
			}
		}
	}

	public void setzeWechselkurs() throws Throwable {
		if (waehrungDto != null && waehrungDto.getCNr() != null) {
			try {
				wnfKurs.setBigDecimal(DelegateFactory.getInstance().getLocaleDelegate()
						.getWechselkurs2(DelegateFactory.getInstance().getMandantDelegate()
								.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant())
								.getWaehrungCNr(), waehrungDto.getCNr()));
			} catch (Throwable t) {
				handleException(t, true); // UW: muss bleiben
				wnfKurs.setBigDecimal(null); // wnfKurs ist mandatory!
				eventActionUnlock(null);
				getInternalFrame().enableAllPanelsExcept(true);
				tpAngebot.gotoAuswahl();
			}
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return jpaKunde;
	}

}
