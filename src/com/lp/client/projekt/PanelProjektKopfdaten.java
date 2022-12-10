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
package com.lp.client.projekt;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import com.lp.client.angebot.InternalFrameAngebot;
import com.lp.client.angebotstkl.InternalFrameAngebotstkl;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.auftrag.InternalFrameAuftrag;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.ButtonFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDokumentenablage;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperBlockEditorField;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldTexteingabe;
import com.lp.client.frame.component.WrapperEmailField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperGotoPartnerMapButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperMediaControl;
import com.lp.client.frame.component.WrapperMediaControlTexteingabe;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTelefonField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.MediaDelegate;
import com.lp.client.frame.delegate.ProjektDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.IPartnerDto;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.reklamation.InternalFrameReklamation;
import com.lp.client.util.DokumentenlinkController;
import com.lp.client.util.DokumentenlinkProjektAction;
import com.lp.client.util.EmailParser;
import com.lp.client.util.EmailParser.TextContentType;
import com.lp.client.util.feature.HvFeatures;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.system.jcr.service.JCRRepoInfo;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.DokumentenlinkDto;
import com.lp.server.system.service.EditorContentDto;
import com.lp.server.system.service.EditorImageBlockDto;
import com.lp.server.system.service.EditorTextBlockDto;
import com.lp.server.system.service.HvImageDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.EditorContentIId;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

import net.miginfocom.swing.MigLayout;

/**
 * <p>
 * Diese Klasse kuemmert sich ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/01/31 14:24:31 $
 */
public class PanelProjektKopfdaten extends PanelBasis implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;

	private static final ImageIcon DOKUMENTE = HelperClient.createImageIcon("document_attachment_green16x16.png");
	private static final ImageIcon KEINE_DOKUMENTE = HelperClient.createImageIcon("document_attachment16x16.png");
	private JButton jbDokumente;

	private boolean bAufInternErledigteBuchen = true;

	private boolean bProjektMitUmsatz = false;

	private Integer angebotIId_KommeVon = null;
	private Integer auftragIId_KommeVon = null;

	private InternalFrameProjekt intFrame = null;
	private TabbedPaneProjekt tpProjekt = null;

	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;

	private WrapperLabel wlfKategorie;
	private WrapperComboBox wcoKategorie;
	private WrapperComboBox wcoBereich;
	private WrapperComboBox wcoVKFortschritt;

	private WrapperTextField wtfTitel;
	private WrapperLabel wlfTitel;

	private WrapperButton wbuPersonalZugewiesener = null;
	private WrapperTextField wtfPersonalZugewiesener = null;
	private PanelQueryFLR panelQueryFLRPersonalZugewiesener = null;

	private WrapperButton wbuPersonalErzeuger = null;
	private WrapperTextField wtfPersonalErzeuger = null;
	private PanelQueryFLR panelQueryFLRPersonalErzeuger = null;

	private PanelQueryFLR panelQueryFLRArtikel = null;

	private WrapperGotoButton wbuPartner = null;
	private WrapperTextField wtfPartner = null;
	private PanelQueryFLR panelQueryFLRPartner = null;
	protected WrapperEmailField wtfEmail = null;

	private WrapperButton wbuAnsprechpartner = null;
	private WrapperTextField wtfAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;
	private static final String ACTION_SPECIAL_PERSONAL_ZUGEWIESENER = "action_special_personal_zugewiesener";
	private static final String ACTION_SPECIAL_PERSONAL_ERZEUGER = "action_special_personal_erzeuger";
	private static final String ACTION_SPECIAL_PARTNER = "action_special_partner";
	private static final String ACTION_SPECIAL_ANSPRECHPARTNER = "action_special_ansprechpartner";

	public final static String _ANZAHL_TECHNIKER = PanelBasis.LEAVEALONE + "_ANZAHL_TECHNIKER";

	private WrapperButton wbuBetreiberAnsprechpartner = null;
	private WrapperTextField wtfBetreiberAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRBetreiberAnsprechpartner = null;
	private static final String ACTION_SPECIAL_BETREIBERANSPRECHPARTNER = "action_special_betreiberansprechpartner";

	static final public String ACTION_SPECIAL_ARTIKEL_FROM_LISTE = "action_artikel_from_liste";

	public final static String MY_OWN_NEW_DOKUMENTENABLAGE = PanelBasis.LEAVEALONE + "DOKUMENTENABLAGE";

	public final static String MY_OWN_NEW_TOGGLE_INTERN_ERLEDIGT = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_INTERN_ERLEDIGT";

	public final static String MY_OWN_NEW_ANGEBOT = PanelBasis.ACTION_MY_OWN_NEW + "MY_OWN_NEW_ANGEBOT";
	public final static String MY_OWN_NEW_AUFTRAG = PanelBasis.ACTION_MY_OWN_NEW + "MY_OWN_NEW_AUFTRAG";

	public final static String MY_OWN_NEW_AGSTKL = PanelBasis.ACTION_MY_OWN_NEW + "MY_OWN_NEW_AGSTKL";

	public final static String MY_OWN_NEW_REKLAMATION = PanelBasis.ACTION_MY_OWN_NEW + "MY_OWN_NEW_REKLAMATION";

	private WrapperLabel wlfTyp;
	private WrapperComboBox wcoTyp;
	private WrapperLabel wlfPrio;
	private WrapperComboBox wcoPrio;
	private WrapperLabel wlfStatus;
	private WrapperComboBox wcoStatus;

	private WrapperLabel wlaLkzPlzOrt = new WrapperLabel();
	private JLabel wlaInternErledigt = new JLabel();

	private WrapperDateField wdfLiefertermin = null;
	private WrapperLabel wlaLiefertermin = null;

	private WrapperDateField wdfRealisierung = null;
	private WrapperLabel wlaRealisierung = null;

	private WrapperDateField wdfErledigt = null;
	private WrapperLabel wlaErledigt = null;
	private WrapperLabel wlaErledigungsgrund = new WrapperLabel();

	private WrapperLabel wlaText = new WrapperLabel();
	private WrapperEditorField wefText = null;

	private WrapperTelefonField wtfDurchwahl = null;
	private WrapperTelefonField wtfBetreiberDurchwahl = null;
	private WrapperTelefonField wtfHandy = null;
	private WrapperTelefonField wtfBetreiberHandy = null;

	// private WrapperLabel wlaBild = new WrapperLabel();
	private WrapperMediaControl wmcBild = null;

	private WrapperComboBox wcbVerrechenbar = null;
	private WrapperCheckBox wcbFreigegeben = null;

	private WrapperLabel wlaDauer = null;
	private WrapperNumberField wnfDauer = null;

	private WrapperLabel wlaWahrscheinlichkeit = null;
	private WrapperNumberField wnfWahrscheinlichkeit = null;

	private WrapperLabel wlaUmsatzGeplant = null;
	private WrapperNumberField wnfUmsatzGeplant = null;

	private WrapperTimeField wtfZeit = null;

	private AnsprechpartnerDto ansprechpartnerDtoVorbesetzen = null;
	private PersonalDto personalDtoZugewiesenerVorbesetzen = null;
	private PartnerDto partnerDtoVorbesetzen = null;

	private WrapperTextField wtfBuildNumber;
	private WrapperTextField wtfDeployNumber;

	private WrapperSelectField wsfNachfolger = new WrapperSelectField(WrapperSelectField.PROJEKT, getInternalFrame(),
			true);
	private WrapperSelectField wsfPartnerBetreiber = new WrapperSelectField(WrapperSelectField.PARTNER,
			getInternalFrame(), true);

	private WrapperGotoButton wbuArtikel = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_ARTIKEL_AUSWAHL);
	private WrapperTextField wtfArtikel = new WrapperTextField();

	private JButton buttonAnzahlTechniker;

	private EditorContentDto editorContentDto;
	private WrapperBlockEditorField wbeKopf;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI    der default Titel des Panels
	 * @param key           PK des Projektes
	 * @throws java.lang.Throwable Ausnahme
	 */
	public PanelProjektKopfdaten(InternalFrame internalFrame, String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);
		intFrame = (InternalFrameProjekt) internalFrame;
		tpProjekt = intFrame.getTabbedPaneProjekt();

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_PROJEKT,
				ParameterFac.PARAMETER_INTERN_ERLEDIGT_BEBUCHBAR);
		bAufInternErledigteBuchen = ((Boolean) parameter.getCWertAsObject());

		parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_PROJEKT,
				ParameterFac.PARAMETER_PROJEKT_MIT_UMSATZ);
		bProjektMitUmsatz = (Boolean) parameter.getCWertAsObject();

		jbInit();
		initPanel();
		initComponents();
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		// zusaetzliche buttons
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_STORNIEREN,
				PanelBasis.ACTION_DISCARD, PanelBasis.ACTION_PRINT };
		enableToolsPanelButtons(aWhichButtonIUse);

		// Workingpanel
		getInternalFrame().addItemChangedListener(this);

		wlfKategorie = new WrapperLabel(LPMain.getTextRespectUISPr("proj.projekt.label.kategorie"));
		wcoKategorie = new WrapperComboBox();
		wcoKategorie.setMandatoryField(true);

		wcoVKFortschritt = new WrapperComboBox();
		wcoVKFortschritt.setToolTipText(LPMain.getTextRespectUISPr("proj.vkfortschritt"));

		wcoBereich = new WrapperComboBox();
		wcoBereich.setMandatoryField(true);
		wcoBereich.addActionListener(this);

		wlfTitel = new WrapperLabel(LPMain.getTextRespectUISPr("proj.projekt.label.titel"));
		wtfTitel = new WrapperTextField();
		wtfTitel.setMandatoryField(true);
		wtfTitel.setColumnsMax(120);

		wbuArtikel.setText(LPMain.getTextRespectUISPr("button.artikel"));

		wbuArtikel.setActionCommand(ACTION_SPECIAL_ARTIKEL_FROM_LISTE);
		wbuArtikel.addActionListener(this);
		wtfArtikel.setActivatable(false);
		wtfArtikel.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfHandy = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);
		wtfHandy.setActivatable(false);
		wtfBetreiberHandy = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);
		wtfBetreiberHandy.setActivatable(false);
		wtfDurchwahl = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);
		wtfDurchwahl.setActivatable(false);

		wtfBetreiberDurchwahl = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);
		wtfBetreiberDurchwahl.setActivatable(false);

		wbuPersonalZugewiesener = new WrapperButton();
		wbuPersonalZugewiesener.setText(LPMain.getTextRespectUISPr("button.mitarbeiter"));
		wbuPersonalZugewiesener.setToolTipText(LPMain.getTextRespectUISPr("button.mitarbeiter.tooltip"));
		wbuPersonalZugewiesener.setActionCommand(ACTION_SPECIAL_PERSONAL_ZUGEWIESENER);
		wbuPersonalZugewiesener.addActionListener(this);

		wtfEmail = new WrapperEmailField();

		wtfBuildNumber = new WrapperTextField(10);
		wtfDeployNumber = new WrapperTextField(10);

		wtfPersonalZugewiesener = new WrapperTextField();
		wtfPersonalZugewiesener.setMandatoryField(true);
		wtfPersonalZugewiesener.setActivatable(false);

		wbuPersonalErzeuger = new WrapperButton();
		wbuPersonalErzeuger.setText(LPMain.getTextRespectUISPr("button.erzeuger"));
		wbuPersonalErzeuger.setToolTipText(LPMain.getTextRespectUISPr("button.erzeuger.tooltip"));
		wbuPersonalErzeuger.setActionCommand(ACTION_SPECIAL_PERSONAL_ERZEUGER);
		wbuPersonalErzeuger.addActionListener(this);

		wtfPersonalErzeuger = new WrapperTextField();
		wtfPersonalErzeuger.setMandatoryField(true);
		wtfPersonalErzeuger.setActivatable(false);

		wbuPartner = new WrapperGotoPartnerMapButton(new IPartnerDto() {
			public PartnerDto getPartnerDto() {
				return tpProjekt.getPartnerDto();
			}
		});

		wbuPartner.setText(LPMain.getTextRespectUISPr("button.partner"));
		wbuPartner.setToolTipText(LPMain.getTextRespectUISPr("button.partner.tooltip"));

		wbuPartner.setActionCommand(ACTION_SPECIAL_PARTNER);
		wbuPartner.addActionListener(this);

		wtfPartner = new WrapperTextField();
		wtfPartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfPartner.setMandatoryField(true);
		wtfPartner.setActivatable(false);

		wbuAnsprechpartner = new WrapperButton();

		wbuAnsprechpartner.setText(LPMain.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartner.setToolTipText(LPMain.getTextRespectUISPr("button.ansprechpartner.tooltip"));

		wbuAnsprechpartner.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER);
		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner = new WrapperTextField();
		wtfAnsprechpartner.setActivatable(false);

		wbuBetreiberAnsprechpartner = new WrapperButton();

		wbuBetreiberAnsprechpartner.setText(LPMain.getTextRespectUISPr("button.ansprechpartner"));
		wbuBetreiberAnsprechpartner.setToolTipText(LPMain.getTextRespectUISPr("button.ansprechpartner.tooltip"));

		wbuBetreiberAnsprechpartner.setActionCommand(ACTION_SPECIAL_BETREIBERANSPRECHPARTNER);
		wbuBetreiberAnsprechpartner.addActionListener(this);

		wtfBetreiberAnsprechpartner = new WrapperTextField();
		wtfBetreiberAnsprechpartner.setActivatable(false);

		wlfTyp = new WrapperLabel(LPMain.getTextRespectUISPr("proj.projekt.label.typ"));
		wcoTyp = new WrapperComboBox();
		wcoTyp.setMandatoryField(true);
		wlfPrio = new WrapperLabel(LPMain.getTextRespectUISPr("proj.projekt.label.prio"));
		wcoPrio = new WrapperComboBox();
		wcoPrio.setMandatoryField(true);
		LinkedHashMap<Integer, Integer> hm = new LinkedHashMap<Integer, Integer>();
		hm.put(1, 1);
		hm.put(2, 2);
		hm.put(3, 3);
		hm.put(4, 4);
		hm.put(5, 5);
		wcoPrio.setMap(hm);

		wlfStatus = new WrapperLabel(LPMain.getTextRespectUISPr("proj.status"));
		wcoStatus = new WrapperComboBox();
		wcoStatus.setMandatoryField(true);

		wlaLkzPlzOrt.setHorizontalAlignment(SwingConstants.LEFT);

		Date datCurrentDate = new Date(System.currentTimeMillis());
		wlaLiefertermin = new WrapperLabel();
		wlaLiefertermin.setText(LPMain.getTextRespectUISPr("proj.zeiltermin"));

		wlaRealisierung = new WrapperLabel();

		wlaRealisierung.setText(LPMain.getTextRespectUISPr("proj.realisierungstermin"));
		wdfRealisierung = new WrapperDateField();

		wlaDauer = new WrapperLabel();
		wlaDauer.setText(LPMain.getTextRespectUISPr("proj.schaetzung"));
		wnfDauer = new WrapperNumberField();
		wnfDauer.setFractionDigits(2);

		wlaWahrscheinlichkeit = new WrapperLabel();
		wlaWahrscheinlichkeit.setText(LPMain.getTextRespectUISPr("lp.wahrscheinlichkeit") + " (%)");
		wnfWahrscheinlichkeit = new WrapperNumberField();
		wnfWahrscheinlichkeit.setFractionDigits(0);
		wnfWahrscheinlichkeit.setMaximumValue(100);
		wnfWahrscheinlichkeit.setMinimumValue(0);

		wlaUmsatzGeplant = new WrapperLabel();
		wlaUmsatzGeplant.setText(LPMain.getTextRespectUISPr("proj.umsatzgeplant"));
		wnfUmsatzGeplant = new WrapperNumberField();
		wnfUmsatzGeplant.setFractionDigits(2);

		wlaDauer = new WrapperLabel();
		wlaDauer.setText(LPMain.getTextRespectUISPr("proj.schaetzung"));
		wnfDauer = new WrapperNumberField();
		wnfDauer.setFractionDigits(2);

		wtfZeit = new WrapperTimeField();

		wdfLiefertermin = new WrapperDateField();
		wdfLiefertermin.setMandatoryField(true);

		wdfLiefertermin.setDate(datCurrentDate);
		wdfLiefertermin.setMinimumValue(datCurrentDate);
		wdfLiefertermin.getDisplay().addPropertyChangeListener(this);

		wefText = new WrapperEditorFieldTexteingabe(getInternalFrame(), LPMain.getTextRespectUISPr("label.text"));
		wefText.getLpEditor().getTextBlockAttributes(-1).capacity = SystemFac.MAX_LAENGE_EDITORTEXT_WENN_NTEXT;

		wefText.setMandatoryField(true); // Projekt 3330
		wlaText.setText(LPMain.getTextRespectUISPr("label.text"));

		wtfEmail.setActivatable(false);

		wmcBild = new WrapperMediaControlTexteingabe(getInternalFrame(), "", true);
		wmcBild.getWcoArt().setMap(wmcBild.getSelectableMimeTypes(true));

		wcbVerrechenbar = new WrapperComboBox();

		wcbVerrechenbar.setMandatoryField(true);

		wcbVerrechenbar.setMap(DelegateFactory.getInstance().getProjektServiceDelegate().getAllVerrechenbar(), false);

		wcbFreigegeben = new WrapperCheckBox(LPMain.getTextRespectUISPr("proj.label.freigegeben"));

		wdfErledigt = new WrapperDateField();
		wdfErledigt.setActivatable(false);
		wdfErledigt.getCalendarButton().setVisible(false);
		wlaErledigt = new WrapperLabel(LPMain.getTextRespectUISPr("label.erledigt"));

		jPanelWorkingOn = new JPanel(new MigLayout("wrap 7, hidemode 2",
				"[15%,fill|15%,fill|10%,fill|10%,fill|15%,fill|20%,fill|15%,fill]"));

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jPanelWorkingOn.add(wlfKategorie);
		jPanelWorkingOn.add(wcoKategorie, "span 3");
		jPanelWorkingOn.add(wcoBereich);
		jPanelWorkingOn.add(wcbVerrechenbar);
		jPanelWorkingOn.add(wcbFreigegeben, "wrap");

		jPanelWorkingOn.add(wlfTitel);
		jPanelWorkingOn.add(wtfTitel, "span");

		wsfNachfolger.setText(LPMain.getTextRespectUISPr("proj.nachfolgeprojekt") + "...");
		jPanelWorkingOn.add(wlfTyp);
		jPanelWorkingOn.add(wcoTyp, "span 3");
		jPanelWorkingOn.add(wsfNachfolger.getWrapperGotoButton());
		jPanelWorkingOn.add(wsfNachfolger.getWrapperTextField(), "span");

		jPanelWorkingOn.add(wbuPersonalErzeuger);
		jPanelWorkingOn.add(wtfPersonalErzeuger, "span 3");
		jPanelWorkingOn.add(wbuPersonalZugewiesener);
		jPanelWorkingOn.add(wtfPersonalZugewiesener, "span");

		jPanelWorkingOn.add(wbuPartner);
		jPanelWorkingOn.add(wtfPartner, "span 3");
		jPanelWorkingOn.add(wtfDurchwahl);
		jPanelWorkingOn.add(wlaUmsatzGeplant);
		jPanelWorkingOn.add(wnfUmsatzGeplant, "wrap");

		jPanelWorkingOn.add(wbuAnsprechpartner);
		jPanelWorkingOn.add(wtfAnsprechpartner, "span 3");
		jPanelWorkingOn.add(wtfHandy);
		jPanelWorkingOn.add(wlaWahrscheinlichkeit);
		jPanelWorkingOn.add(wnfWahrscheinlichkeit, "wrap");

		jPanelWorkingOn.add(wlaLkzPlzOrt, "skip, span 3");
		jPanelWorkingOn.add(wtfEmail, "span");

		wsfPartnerBetreiber.setMandatoryField(false);
		wsfPartnerBetreiber.getWrapperGotoButton().setText(LPMain.getTextRespectUISPr("proj.betreiber") + "...");
		jPanelWorkingOn.add(wsfPartnerBetreiber.getWrapperGotoButton());
		jPanelWorkingOn.add(wsfPartnerBetreiber.getWrapperTextField(), "span 3");

		jPanelWorkingOn.add(wtfBetreiberDurchwahl, "wrap");

		jPanelWorkingOn.add(wbuBetreiberAnsprechpartner);
		jPanelWorkingOn.add(wtfBetreiberAnsprechpartner, "span 3");
		jPanelWorkingOn.add(wtfBetreiberHandy, "wrap");

		jPanelWorkingOn.add(wbuArtikel);
		jPanelWorkingOn.add(wtfArtikel, "span 3, wrap");

		jPanelWorkingOn.add(wlfPrio);
		jPanelWorkingOn.add(wcoPrio);
		jPanelWorkingOn.add(wcoVKFortschritt, "growx, w 115:500:500");
		jPanelWorkingOn.add(wlfStatus);
		jPanelWorkingOn.add(wcoStatus);

		// PJ19812
		if (bProjektMitUmsatz == true) {

			jPanelWorkingOn.add(wlaRealisierung);
			jPanelWorkingOn.add(wdfRealisierung, "wrap");
		} else {
			jPanelWorkingOn.add(wlaDauer);
			jPanelWorkingOn.add(wnfDauer, "wrap");
		}

		jPanelWorkingOn.add(wlaLiefertermin);
		jPanelWorkingOn.add(wdfLiefertermin);
		jPanelWorkingOn.add(wtfZeit, "split 2, span 2, growx");
		jPanelWorkingOn.add(wlaErledigt, "growx 50");
		jPanelWorkingOn.add(wdfErledigt);

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_PROJEKT,
				ParameterFac.PARAMETER_BUILD_ANZEIGEN);
		boolean bBuildAnzeigen = ((Boolean) parameter.getCWertAsObject());

		if (bBuildAnzeigen) {
			jPanelWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("proj.deploy")), "split, span");
			jPanelWorkingOn.add(wtfDeployNumber);
			jPanelWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("proj.build")));
			jPanelWorkingOn.add(wtfBuildNumber);
		} else {
			jPanelWorkingOn.add(wlaErledigungsgrund, "span");

		}

		if (HvFeatures.BlockEditor.isActive()) {
			wbeKopf = new WrapperBlockEditorField(intFrame, "Weiß nicht, was das ist");
			wbeKopf.setEditable(false);
			jPanelWorkingOn.add(wbeKopf, "newline, span, w 100:800:2000");
		} else {
			JSplitPane sp = new JSplitPane();
			sp.setDividerSize(3);
			sp.setDividerLocation(80);
			sp.setOrientation(JSplitPane.VERTICAL_SPLIT);

			sp.setTopComponent(wefText);

			sp.setBottomComponent(wmcBild);

			jPanelWorkingOn.add(sp, "newline, span, w 100:800:2000");
		}

		/*
		 * jPanelWorkingOn.add(wefText, "newline, span, pushy");
		 * 
		 * jPanelWorkingOn.add(wmcBild, "span, w :0:");
		 */

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {

			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANGEBOT)) {
				getToolBar().addButtonLeft("/com/lp/client/res/presentation_chart16x16.png",
						LPMain.getTextRespectUISPr("proj.neues.angebot"), MY_OWN_NEW_ANGEBOT, null, null);
			}

			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AUFTRAG)) {
				getToolBar().addButtonLeft("/com/lp/client/res/auftrag16x16.png",
						LPMain.getTextRespectUISPr("proj.neues.auftrag"), MY_OWN_NEW_AUFTRAG, null, null);
			}

			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AGSTUECKLISTE)) {
				getToolBar().addButtonLeft("/com/lp/client/res/note_add16x16.png",
						LPMain.getTextRespectUISPr("proj.neue.agstkl"), MY_OWN_NEW_AGSTKL, null, null);
			}

			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_REKLAMATION)) {

				if (DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_REKLA_REKLAMATION_CUD)) {
					getToolBar().addButtonLeft("/com/lp/client/res/exchange16x16.png",
							LPMain.getTextRespectUISPr("proj.neue.reklamation"), MY_OWN_NEW_REKLAMATION, null, null);
				}
			}
		}

		buttonAnzahlTechniker = ButtonFactory.createJButton();
		buttonAnzahlTechniker.setEnabled(false);
		buttonAnzahlTechniker.setForeground(new Color(0, 153, 51));

		getToolBar().getToolsPanelRight().add(buttonAnzahlTechniker);

		if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ZEITERFASSUNG)
				&& LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTZEITERFASSUNG)) {
			boolean hatRecht = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD);
			if (hatRecht) {
				getToolBar().addButtonCenter("/com/lp/client/res/check2.png",
						LPMain.getTextRespectUISPr("proj.internerledigen"), MY_OWN_NEW_TOGGLE_INTERN_ERLEDIGT, null,
						null);
				getToolBar().getToolsPanelCenter().add(wlaInternErledigt);

				ParametermandantDto parameterVB = DelegateFactory.getInstance().getParameterDelegate()
						.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_PERSONAL,
								ParameterFac.PARAMETER_VON_BIS_ERFASSUNG);
				boolean bVonBisErfassung = (java.lang.Boolean) parameterVB.getCWertAsObject();

				// SP2352
				if (bVonBisErfassung == false) {

					getToolBar().addButtonRight("/com/lp/client/res/gear_run.png",
							LPMain.getTextRespectUISPr("proj.startzeit"), Desktop.MY_OWN_NEW_ZEIT_START, null, null);
					getToolBar().addButtonRight("/com/lp/client/res/gear_stop.png",
							LPMain.getTextRespectUISPr("proj.stopzeit"), Desktop.MY_OWN_NEW_ZEIT_STOP, null, null);
				}

				enableToolsPanelButtons(true, Desktop.MY_OWN_NEW_ZEIT_START, Desktop.MY_OWN_NEW_ZEIT_STOP);

			}
		}
		getToolBar().addButtonRight("/com/lp/client/res/document_attachment16x16.png",
				LPMain.getTextRespectUISPr("lp.dokumentablage"), MY_OWN_NEW_DOKUMENTENABLAGE, null, null);
		jbDokumente = getHmOfButtons().get(MY_OWN_NEW_DOKUMENTENABLAGE).getButton();

	}

	private PanelQueryFLR panelQueryFLRProjekterledigungsgrund = null;

	public void dialogQueryErledigungsgrundFromListe() throws Throwable {
		panelQueryFLRProjekterledigungsgrund = ProjektFilterFactory.getInstance()
				.createPanelFLRProjekterledigungsgrund(getInternalFrame(), false, false);
		new DialogQuery(panelQueryFLRProjekterledigungsgrund);
	}

	public void propertyChange(PropertyChangeEvent evt) {
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_PROJEKT;
	}

	public void setDefaultsAusAngebot(Integer angebotIId) throws Throwable {

		AngebotDto angebotDto = DelegateFactory.getInstance().getAngebotDelegate().angebotFindByPrimaryKey(angebotIId);

		wtfTitel.setText(angebotDto.getCBez());

		KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(angebotDto.getKundeIIdAngebotsadresse());

		PersonalDto vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
				.personalFindByPrimaryKey(angebotDto.getPersonalIIdVertreter());
		if (vertreterDto != null && vertreterDto.getIId() != null) {
			wtfPersonalZugewiesener.setText(vertreterDto.getPartnerDto().formatTitelAnrede());
			tpProjekt.setPersonalZugewiesenerDto(vertreterDto);
		}

		partnerdatenVorbesetzen(kundeDto.getPartnerIId());

		if (angebotDto.getAnsprechpartnerIIdKunde() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(angebotDto.getAnsprechpartnerIIdKunde());

			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatTitelAnrede());
			tpProjekt.setAnsprechpartnerDto(ansprechpartnerDto);
		}
		angebotIId_KommeVon = angebotIId;
	}

	public void setDefaultsAusAuftrag(Integer auftragIId) throws Throwable {

		AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate().auftragFindByPrimaryKey(auftragIId);

		wtfTitel.setText(auftragDto.getCBezProjektbezeichnung());

		KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse());

		PersonalDto vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
				.personalFindByPrimaryKey(auftragDto.getPersonalIIdVertreter());
		if (vertreterDto != null && vertreterDto.getIId() != null) {
			wtfPersonalZugewiesener.setText(vertreterDto.getPartnerDto().formatTitelAnrede());
			tpProjekt.setPersonalZugewiesenerDto(vertreterDto);
		}

		partnerdatenVorbesetzen(kundeDto.getPartnerIId());

		if (auftragDto.getAnsprechparnterIId() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(auftragDto.getAnsprechparnterIId());

			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatTitelAnrede());
			tpProjekt.setAnsprechpartnerDto(ansprechpartnerDto);
		}
		auftragIId_KommeVon = auftragIId;
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		angebotIId_KommeVon = null;
		auftragIId_KommeVon = null;
		tpProjekt.refreshMenuAnsicht(true);
	}

	public void setDefaults() throws Throwable {

		// alle anzeigefelder zuruecksetzen
		leereAlleFelder(this);
		// alle vorbelegten werte setzen
		// alle vorbelegten werte setzen
		wcoPrio.setKeyOfSelectedItem(3);
		wmcBild.setOMediaImage(null);
		wmcBild.getWcoArt().setKeyOfSelectedItem(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG);

		// den Vorschlagswert fuer die Verdichtungstage bestimmen
		ParametermandantDto parameterZielTermin = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_PROJEKT,
						ParameterFac.PARAMETER_PROJEKT_OFFSET_ZIELTERMIN);
		int defaultZielTermin = ((Integer) parameterZielTermin.getCWertAsObject()).intValue();

		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, defaultZielTermin);
		java.sql.Date currentDateZielTermin = new java.sql.Date(calendar.getTimeInMillis());

		wdfLiefertermin.setDate(currentDateZielTermin);
		wcbVerrechenbar.setKeyOfSelectedItem(ProjektServiceFac.PROJEKT_VERRECHENBAR_NICHT_DEFINIERT);
		wcbFreigegeben.setShort(Helper.boolean2Short(false));
		tpProjekt.refreshMenuAnsicht(false);
		// Status auf angelegt setzen
		wcoStatus.setKeyOfSelectedItem(ProjektServiceFac.PROJEKT_STATUS_ANGELEGT);

		wlaErledigungsgrund.setText("");
		wlaErledigt.setToolTipText("");

		if (tpProjekt.getBereichIId() != null) {
			BereichDto bereichDto = DelegateFactory.getInstance().getProjektServiceDelegate()
					.bereichFindByPrimaryKey(tpProjekt.getBereichIId());

			wefText.setMandatoryField(Helper.short2boolean(bereichDto.getBDetailtextIstPflichtfeld()));

			if (Helper.short2boolean(bereichDto.getBProjektMitBetreiber())) {
				wsfPartnerBetreiber.setMandatoryField(true);
				wsfPartnerBetreiber.getWrapperGotoButton().setVisible(true);
				wsfPartnerBetreiber.getWrapperTextField().setVisible(true);

				wbuBetreiberAnsprechpartner.setVisible(true);
				wtfBetreiberAnsprechpartner.setVisible(true);
				wtfBetreiberHandy.setVisible(true);
				wtfBetreiberDurchwahl.setVisible(true);

			} else {
				wsfPartnerBetreiber.setMandatoryField(false);
				wsfPartnerBetreiber.getWrapperGotoButton().setVisible(false);
				wsfPartnerBetreiber.getWrapperTextField().setVisible(false);

				wbuBetreiberAnsprechpartner.setVisible(false);
				wtfBetreiberAnsprechpartner.setVisible(false);
				wtfBetreiberHandy.setVisible(false);
				wtfBetreiberDurchwahl.setVisible(false);

			}

			wbuArtikel.setMandatoryField(false);

			if (Helper.short2boolean(bereichDto.getBProjektMitArtikel())) {

				wbuArtikel.setVisible(true);
				wtfArtikel.setVisible(true);
				if (Helper.short2boolean(bereichDto.getBProjektArtikelPflichtfeld())) {
					wtfArtikel.setMandatoryField(true);
				}

			} else {
				wbuArtikel.setVisible(false);
				wtfArtikel.setVisible(false);
			}

		}

	}

	private void dto2ComponentsErzeuger(PersonalDto personalErzeugerDto) {
		wtfPersonalErzeuger.setText(personalErzeugerDto.getPartnerDto().formatTitelAnrede());
	}

	private void initPanel() throws Throwable {
		wcoKategorie.setMap(DelegateFactory.getInstance().getProjektServiceDelegate().getKategorie());
		wcoBereich.setMap(DelegateFactory.getInstance().getProjektServiceDelegate().getAllBereich());
		wcoVKFortschritt.setMap(DelegateFactory.getInstance().getProjektServiceDelegate().getAllSprVkfortschritt());
		wcoTyp.setMap(DelegateFactory.getInstance().getProjektServiceDelegate().getTyp());
		wcoStatus.setMap(DelegateFactory.getInstance().getProjektServiceDelegate()
				.getProjektStatus(LPMain.getInstance().getUISprLocale()));
	}

	private void dto2Components() throws Throwable {

		if (tpProjekt.getProjektDto().getProjekterledigungsgrundIId() != null) {
			String grund = DelegateFactory.getInstance().getProjektServiceDelegate()
					.projekterledigungsgrundFindByPrimaryKey(tpProjekt.getProjektDto().getProjekterledigungsgrundIId())
					.getCBez();

			if (tpProjekt.getProjektDto().getPersonalIIdErlediger() != null) {
				PersonalDto pDto = DelegateFactory.getInstance().getPersonalDelegate()
						.personalFindByPrimaryKey(tpProjekt.getProjektDto().getPersonalIIdErlediger());

				grund += " (" + pDto.getCKurzzeichen() + ")";

			}

			wlaErledigungsgrund.setText(LPMain.getTextRespectUISPr("proj.erledigungsgrund") + ": " + grund);
			wlaErledigt.setToolTipText(LPMain.getTextRespectUISPr("proj.erledigungsgrund") + ": " + grund);

			wdfErledigt.setToolTipText(LPMain.getTextRespectUISPr("proj.erledigungsgrund") + ": " + grund);

		} else {
			wlaErledigungsgrund.setText("");
			wlaErledigt.setToolTipText("");
			wdfErledigt.setToolTipText("");
		}

		if (tpProjekt.getProjektDto().getArtikelIId() != null) {
			ArtikelDto artikelTempDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(tpProjekt.getProjektDto().getArtikelIId());

			wtfArtikel.setText(artikelTempDto.formatArtikelbezeichnung());
			wbuArtikel.setOKey(tpProjekt.getProjektDto().getArtikelIId());
		} else {
			wtfArtikel.setText(null);
		}

		wcoKategorie.setKeyOfSelectedItem(tpProjekt.getProjektDto().getKategorieCNr());
		wcoTyp.setKeyOfSelectedItem(tpProjekt.getProjektDto().getProjekttypCNr());
		wcoBereich.setKeyOfSelectedItem(tpProjekt.getProjektDto().getBereichIId());
		wcoVKFortschritt.setKeyOfSelectedItem(tpProjekt.getProjektDto().getVkfortschrittIId());
		wcoStatus.setKeyOfSelectedItem(tpProjekt.getProjektDto().getStatusCNr());
		wcoPrio.setKeyOfSelectedItem(tpProjekt.getProjektDto().getIPrio());
		wtfTitel.setText(tpProjekt.getProjektDto().getCTitel());
		dto2ComponentsZugewiesener(tpProjekt.getPersonalZugewiesenerDto());
		dto2ComponentsErzeuger(tpProjekt.getPersonalErzeugerDto());
		dto2ComponentsPartner(tpProjekt.getPartnerDto());
		dto2ComponentsAnsprechpartner(tpProjekt.getAnsprechpartnerDto());
		wtfEmail.setEmail(null, null);
		wtfEmail.setMailLocale(tpProjekt.getPartnerDto() != null
				? Helper.string2Locale(tpProjekt.getPartnerDto().getLocaleCNrKommunikation())
				: null);

		AnsprechpartnerDto anspDto = null;
		if (tpProjekt.getAnsprechpartnerDto() != null && tpProjekt.getAnsprechpartnerDto().getIId() != null) {
			anspDto = tpProjekt.getAnsprechpartnerDto();
		}

		if (tpProjekt.getAnsprechpartnerDto() != null && tpProjekt.getAnsprechpartnerDto().getCEmail() != null) {
			wtfEmail.setEmail(tpProjekt.getAnsprechpartnerDto().getCEmail(), anspDto);
		} else {
			if (tpProjekt.getPartnerDto() != null && tpProjekt.getPartnerDto().getCEmail() != null) {
				wtfEmail.setEmail(tpProjekt.getPartnerDto().getCEmail(), anspDto);
			}

		}

		String text = "";

		if (tpProjekt.getProjektDto().getTInternerledigt() != null) {
			text = LPMain.getTextRespectUISPr("proj.internerledigt") + " " + Helper
					.formatDatumZeit(tpProjekt.getProjektDto().getTInternerledigt(), LPMain.getTheClient().getLocUi());
		}
		if (tpProjekt.getProjektDto().getPersonalIIdInternerledigt() != null) {
			text += "(" + DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(tpProjekt.getProjektDto().getPersonalIIdInternerledigt())
					.getCKurzzeichen() + ")";
		}

		wlaInternErledigt.setText(text);
		// wlaInternErledigt.setCutOffEnd(false);

		dto2ComponentsTextBild(tpProjekt.getProjektDto());

		wdfLiefertermin.setTimestamp(tpProjekt.getProjektDto().getTZielwunschdatum());
		wdfRealisierung.setTimestamp(tpProjekt.getProjektDto().getTRealisierung());
		wdfErledigt.setTimestamp(tpProjekt.getProjektDto().getTErledigt());

		wcbVerrechenbar.setKeyOfSelectedItem(tpProjekt.getProjektDto().getIVerrechenbar());
		wcbFreigegeben.setShort(tpProjekt.getProjektDto().getBFreigegeben());

		wsfNachfolger.setKey(tpProjekt.getProjektDto().getProjektIIdNachfolger());
		wsfPartnerBetreiber.setKey(tpProjekt.getProjektDto().getPartnerIIdBetreiber());

		dto2ComponentsBetreiberAnsprechpartner(tpProjekt.getProjektDto().getPartnerIIdBetreiber(),
				tpProjekt.getProjektDto().getAnsprechpartnerIIdBetreiber());

		ArrayList<String> s = DelegateFactory.getInstance().getProjektDelegate()
				.getVorgaengerProjekte(tpProjekt.getProjektDto().getIId());

		StringBuffer str = new StringBuffer("Vorg\u00E4nger: ");
		for (int i = 0; i < s.size(); i++) {
			str.append(s.get(i));
			str.append(", ");
		}

		wsfNachfolger.setToolTipText(str.toString());

		wnfWahrscheinlichkeit.setInteger(tpProjekt.getProjektDto().getIWahrscheinlichkeit());
		wnfUmsatzGeplant.setBigDecimal(tpProjekt.getProjektDto().getNUmsatzgeplant());

		//
		if (tpProjekt.getProjektDto().getDDauer() != null) {
			wnfDauer.setDouble(tpProjekt.getProjektDto().getDDauer());
		}
		//
		if (tpProjekt.getProjektDto().getTZeit() != null) {
			java.sql.Time time = new Time(tpProjekt.getProjektDto().getTZeit().getTime());
			wtfZeit.setTime(time);
		}
		wtfDeployNumber.setText(tpProjekt.getProjektDto().getDeployNumber());
		wtfBuildNumber.setText(tpProjekt.getProjektDto().getBuildNumber());
		PrintInfoDto values = DelegateFactory.getInstance().getJCRDocDelegate()
				.getPathAndPartnerAndTable(tpProjekt.getProjektDto().getIId(), QueryParameters.UC_ID_PROJEKT);

		int i = DelegateFactory.getInstance().getProjektServiceDelegate()
				.getAnzahlTechniker(tpProjekt.getProjektDto().getIId());

		buttonAnzahlTechniker.setToolTipText(LPMain.getMessageTextRespectUISPr("proj.anzahl.techniker", i + ""));
		buttonAnzahlTechniker.setText(i + "");

		JCRRepoInfo repoInfo = new JCRRepoInfo();
		// boolean hasFiles = false;
		if (values != null && values.getDocPath() != null) {
			repoInfo = DelegateFactory.getInstance().getJCRDocDelegate().checkIfNodeExists(values.getDocPath());
			enableToolsPanelButtons(repoInfo.isOnline(), MY_OWN_NEW_DOKUMENTENABLAGE);
			// boolean online = DelegateFactory.getInstance()
			// .getJCRDocDelegate().isOnline();
			// enableToolsPanelButtons(online, MY_OWN_NEW_DOKUMENTENABLAGE);
			// if (online) {
			// hasFiles = DelegateFactory.getInstance()
			// .getJCRDocDelegate()
			// .checkIfNodeExists(values.getDocPath());
			// }
			// }
		}
		jbDokumente.setIcon(repoInfo.isExists() ? DOKUMENTE : KEINE_DOKUMENTE);

	}

	private void dto2ComponentsTextBild(ProjektDto projektDto) throws Throwable {
		if (HvFeatures.BlockEditor.isActive()) {
			dto2ComponentsBlockEditor(projektDto);
		} else {
			dto2ComponentsTextBildEditor(projektDto);
		}
	}

	private void dto2ComponentsBlockEditor(ProjektDto projektDto) throws Throwable {
		if (projektDto.getContentId() != null && projektDto.getContentId().isValid()) {
			HvOptional<EditorContentDto> editorContent = mediaDelegate()
					.editorContentFindByPrimaryKey(projektDto.getContentId());
			editorContentDto = editorContent.orElse(new EditorContentDto());
			wbeKopf.setContent(editorContentDto);
		} else {
			editorContentDto = new EditorContentDto();
			int row = 0;
			if (projektDto.getXFreetext() != null) {
				EditorTextBlockDto textBlock = new EditorTextBlockDto(row++, 0, projektDto.getXFreetext());
				editorContentDto.addBlockDto(textBlock);
			}

			if (projektDto.getOAttachments() != null) {
				HvImageDto imageDto = new HvImageDto(projektDto.getOAttachments(), projektDto.getCAttachmentsType());
				EditorImageBlockDto imageBlock = new EditorImageBlockDto(row++, 0, imageDto);
				editorContentDto.addBlockDto(imageBlock);
			}

			wbeKopf.setContent(editorContentDto);
		}
	}

	private void dto2ComponentsTextBildEditor(ProjektDto projektDto) throws Throwable {
		if (projektDto.getXFreetext() != null) {
			wefText.setText(projektDto.getXFreetext());
		}

		if (projektDto.getOAttachments() != null) {
			wmcBild.setMimeType(projektDto.getCAttachmentsType());
			wmcBild.setDateiname(projektDto.getCDateiname());
			wmcBild.setOMediaImage(projektDto.getOAttachments());
		} else {
			wmcBild.setOMediaImage(null);
		}
	}

	private void dto2ComponentsPartner(PartnerDto partnerDto) {
		if (partnerDto != null) {
			wtfPartner.setText(partnerDto.formatFixTitelName1Name2());
			wbuPartner.setOKey(partnerDto.getIId());
			LandplzortDto landplzortDto = partnerDto.getLandplzortDto();
			String lKZ = null;
			if (landplzortDto != null) {
				lKZ = landplzortDto.formatLandPlzOrt();
			}
			wlaLkzPlzOrt.setText(lKZ);
		} else {
			wtfPartner.setText(null);
			wlaLkzPlzOrt.setText(null);

			wbuPartner.setOKey(null);
		}
	}

	private void dto2ComponentsZugewiesener(PersonalDto personalDto) {
		if (personalDto != null) {
			wtfPersonalZugewiesener.setText(personalDto.getPartnerDto().formatTitelAnrede());
		} else {
			wtfPersonalZugewiesener.setText(null);
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		tpProjekt.resetDtos();
		setDefaults();
		clearStatusbar();
		buttonAnzahlTechniker.setText("0");
		buttonAnzahlTechniker.setToolTipText("");

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (tpProjekt.getProjektDto().getIId() == null) {
				// neues Projekt
				saveOrUpdateEditor();
				Integer projektIId = DelegateFactory.getInstance().getProjektDelegate()
						.createProjekt(tpProjekt.getProjektDto());

				// PJ20454
				if (angebotIId_KommeVon != null) {
					AngebotDto agDto = DelegateFactory.getInstance().getAngebotDelegate()
							.angebotFindByPrimaryKey(angebotIId_KommeVon);

					agDto.setProjektIId(projektIId);

					DelegateFactory.getInstance().getAngebotDelegate().updateAngebotOhneWeitereAktion(agDto);
					angebotIId_KommeVon = null;
				}
				if (auftragIId_KommeVon != null) {
					AuftragDto abDto = DelegateFactory.getInstance().getAuftragDelegate()
							.auftragFindByPrimaryKey(auftragIId_KommeVon);

					abDto.setProjektIId(projektIId);

					DelegateFactory.getInstance().getAuftragDelegate().updateAuftragOhneWeitereAktion(abDto);
					auftragIId_KommeVon = null;
				}

				tpProjekt.setProjektDto(
						DelegateFactory.getInstance().getProjektDelegate().projektFindByPrimaryKey(projektIId));
				setKeyWhenDetailPanel(projektIId);
				// Filter loeschen und refreshen, damit ich nacher richtig
				// stehe.
				// String sOldKey = getInternalFrame().getKeyWasForLockMe();
				tpProjekt.getProjektAuswahl().clearDirektFilter();
				tpProjekt.getProjektAuswahl().refreshMe(e, projektIId);

				// tpProjekt.getProjektAuswahl().eventYouAreSelected(false);
				// TODO: Wird das noch benoetigt?
				// tpProjekt.getProjektAuswahl().setSelectedId(projektIId);
				// getInternalFrame().setKeyWasForLockMe(sOldKey);
				// Projekt 2839: Mitarbeiter und Partner aus dem letzten
				// erfassten Projekt vorbesetzen.
				// das merk ich mir hier.
				ansprechpartnerDtoVorbesetzen = tpProjekt.getAnsprechpartnerDto();
				personalDtoZugewiesenerVorbesetzen = tpProjekt.getPersonalZugewiesenerDto();
				partnerDtoVorbesetzen = tpProjekt.getPartnerDto();
			} else {
				// das aktuelle Projekt wird veraendert
				actionUpdateProjekt();
			}
			super.eventActionSave(e, true);
			eventYouAreSelected(false);
			tpProjekt.refreshMenuAnsicht(true);
		}
	}

	private ProjektDelegate projektDelegate() throws Throwable {
		return DelegateFactory.getInstance().getProjektDelegate();
	}

	private MediaDelegate mediaDelegate() throws Throwable {
		return DelegateFactory.getInstance().getMediaDelegate();
	}

	private void saveOrUpdateEditor() throws Throwable {
		if (HvFeatures.BlockEditor.isActive()) {
			EditorContentDto contentDto;

			if (tpProjekt.getProjektDto().getContentId().isValid()) {
				contentDto = mediaDelegate().updateEditorContent(wbeKopf.getContent());
			} else {
				contentDto = mediaDelegate().createEditorContent(wbeKopf.getContent());
			}
			tpProjekt.getProjektDto().setContentId(contentDto.getId());
		}
	}

	private void actionUpdateProjekt() throws ExceptionLP, Throwable {
		ProjektDto originalProjekt = projektDelegate().projektFindByPrimaryKey(tpProjekt.getProjektDto().getIId());
		boolean bereichChanged = !originalProjekt.getBereichIId().equals(tpProjekt.getProjektDto().getBereichIId());

		saveOrUpdateEditor();
		projektDelegate().updateProjekt(tpProjekt.getProjektDto());

		if (bereichChanged) {
			DokumentenlinkController doklinkCtrl = new DokumentenlinkController(tpProjekt, LocaleFac.BELEGART_PROJEKT);
			if (!doklinkCtrl.hasDokumentenlinks()) {
				return;
			}

			ProjektDto updatedProjekt = projektDelegate().projektFindByPrimaryKey(originalProjekt.getIId());
			for (DokumentenlinkDto doklink : doklinkCtrl.getDokumentenlinkDtos()) {
				DokumentenlinkProjektAction doklinkProjekt = new DokumentenlinkProjektAction(doklink, originalProjekt);
				doklinkProjekt.processBereichsaenderung(updatedProjekt);
			}
		}
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		ProjektDto dto = tpProjekt.getProjektDto();
		DelegateFactory.getInstance().getProjektDelegate().removeProjekt(dto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, bAdministrateLockKeyI, false);
	}

	/**
	 * components2Dto
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {

		if (DelegateFactory.getInstance().getProjektServiceDelegate().sindErledigugnsgruendeVorhanden()) {

			// SP5286
			boolean bErldigtstatusVorhanden = false;

			if (tpProjekt.getProjektDto().getStatusCNr() != null) {
				bErldigtstatusVorhanden = Helper.short2boolean(DelegateFactory.getInstance().getProjektServiceDelegate()
						.projektstatusFindByPrimaryKey(tpProjekt.getProjektDto().getStatusCNr()).getBErledigt());
			}

			boolean bErldigtstatusNeu = Helper.short2boolean(DelegateFactory.getInstance().getProjektServiceDelegate()
					.projektstatusFindByPrimaryKey((String) wcoStatus.getKeyOfSelectedItem()).getBErledigt());

			if (bErldigtstatusNeu == true && bErldigtstatusVorhanden == false) {

				dialogQueryErledigungsgrundFromListe();

				if (tpProjekt.getProjektDto().getProjekterledigungsgrundIId() == null) {
					return;
				}

			}
		}
		tpProjekt.getProjektDto().setMandantCNr(LPMain.getTheClient().getMandant());
		tpProjekt.getProjektDto().setBereichIId((Integer) wcoBereich.getKeyOfSelectedItem());

		tpProjekt.getProjektDto().setVkfortschrittIId((Integer) wcoVKFortschritt.getKeyOfSelectedItem());

		tpProjekt.getProjektDto().setPartnerIIdBetreiber(wsfPartnerBetreiber.getIKey());

		tpProjekt.getProjektDto().setKategorieCNr((String) wcoKategorie.getKeyOfSelectedItem());

		String statusCNr = (String) wcoStatus.getKeyOfSelectedItem();
		tpProjekt.getProjektDto().setStatusCNr(statusCNr);

		tpProjekt.getProjektDto().setCTitel(wtfTitel.getText());

		tpProjekt.getProjektDto().setIWahrscheinlichkeit(wnfWahrscheinlichkeit.getInteger());
		tpProjekt.getProjektDto().setNUmsatzgeplant(wnfUmsatzGeplant.getBigDecimal());

		tpProjekt.getProjektDto().setProjektIIdNachfolger(wsfNachfolger.getIKey());

		tpProjekt.getProjektDto().setProjekttypCNr((String) wcoTyp.getKeyOfSelectedItem());
		tpProjekt.getProjektDto().setPersonalIIdErzeuger(tpProjekt.getPersonalErzeugerDto().getIId());
		if (tpProjekt.getPersonalZugewiesenerDto() != null) {
			tpProjekt.getProjektDto().setPersonalIIdZugewiesener(tpProjekt.getPersonalZugewiesenerDto().getIId());
		}
		tpProjekt.getProjektDto().setIPrio((Integer) wcoPrio.getKeyOfSelectedItem());
		tpProjekt.getProjektDto().setPartnerIId(tpProjekt.getPartnerDto().getIId());
		tpProjekt.getProjektDto().setTZielwunschdatum(wdfLiefertermin.getTimestamp());
		tpProjekt.getProjektDto().setTRealisierung(wdfRealisierung.getTimestamp());
		tpProjekt.getProjektDto().setXFreetext(wefText.getText());

		tpProjekt.getProjektDto().setDeployNumber(wtfDeployNumber.getText());
		tpProjekt.getProjektDto().setBuildNumber(wtfBuildNumber.getText());
		if (wmcBild.getOMediaImage() != null) {
			tpProjekt.getProjektDto().setCAttachmentsType(wmcBild.getMimeType());
			tpProjekt.getProjektDto().setCDateiname(wmcBild.getDateiname());
			tpProjekt.getProjektDto().setOAttachments(wmcBild.getOMediaImage());
		} else {
			tpProjekt.getProjektDto().setCAttachmentsType(null);
			tpProjekt.getProjektDto().setOAttachments(null);
		}
		tpProjekt.getProjektDto().setIVerrechenbar((Integer) wcbVerrechenbar.getKeyOfSelectedItem());
		tpProjekt.getProjektDto().setBFreigegeben(wcbFreigegeben.getShort());
		//
		java.sql.Timestamp tsZeit = wdfLiefertermin.getTimestamp();
		tsZeit = Helper.cutTimestamp(tsZeit);
		tsZeit.setTime(tsZeit.getTime() + wtfZeit.getTime().getTime() + 3600000);
		tpProjekt.getProjektDto().setTZeit(tsZeit);
		if (wnfDauer.getDouble() != null) {
			tpProjekt.getProjektDto().setDDauer(wnfDauer.getDouble());
		} else {
			tpProjekt.getProjektDto().setDDauer(0.0);
		}
		if (tpProjekt.getAnsprechpartnerDto() != null) {
			tpProjekt.getProjektDto().setAnsprechpartnerIId(tpProjekt.getAnsprechpartnerDto().getIId());
		}

		if (HvFeatures.BlockEditor.isActive()) {
			tpProjekt.getProjektDto().setContentId(wbeKopf.getContent().getId());
		}
	}

	/**
	 * Das Projekt drucken.
	 * 
	 * @param e Ereignis
	 * @throws Throwable
	 */
	protected void eventActionPrint(ActionEvent e) throws Throwable {
		tpProjekt.printProjekt();
		// wg. SP 8200 auskommentiert
		// eventYouAreSelected(false);
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		Object oKey = tpProjekt.getProjektDto().getIId();
		setDefaults();
		if (oKey == null) {
			// Neues Projekt anlegen.
			dto2ComponentsPartner(partnerDtoVorbesetzen);
			if (partnerDtoVorbesetzen != null) {
				tpProjekt.setPartnerDto(partnerDtoVorbesetzen);

				//SP9512
				if (ansprechpartnerDtoVorbesetzen != null && ansprechpartnerDtoVorbesetzen.getPartnerIId() !=null
						&& ansprechpartnerDtoVorbesetzen.getPartnerIId().equals(partnerDtoVorbesetzen.getIId())) {
					dto2ComponentsAnsprechpartner(ansprechpartnerDtoVorbesetzen);
					tpProjekt.setAnsprechpartnerDto(ansprechpartnerDtoVorbesetzen);
				}else {
					ansprechpartnerDtoVorbesetzen=null;
				}

			}

			dto2ComponentsZugewiesener(personalDtoZugewiesenerVorbesetzen);
			if (personalDtoZugewiesenerVorbesetzen != null) {
				tpProjekt.setPersonalZugewiesenerDto(personalDtoZugewiesenerVorbesetzen);
			}

			// Erzeuger ist per Default der Benutzer.
			PersonalDto personalErzeugerDto = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());
			dto2ComponentsErzeuger(personalErzeugerDto);
			tpProjekt.setPersonalErzeugerDto(personalErzeugerDto);

			wcoBereich.setKeyOfSelectedItem(
					(Integer) intFrame.getTabbedPaneProjekt().getProjektAuswahl().getKeyOfFilterComboBox());

		} else {
			tpProjekt.setProjektDto(
					DelegateFactory.getInstance().getProjektDelegate().projektFindByPrimaryKey((Integer) oKey));

			dto2Components();
			tpProjekt.setTitleProjekt(LPMain.getTextRespectUISPr("lp.kopfdaten"));
		}
		tpProjekt.getProjektKopfdaten().updateButtons(getLockedstateDetailMainKey());
		if (oKey == null) {
			wcoBereich.setEnabled(true);
		}

		aktualisiereStatusbar();
		zeitbuchungDeaktivierenWennNoetig();
	}

	private void zeitbuchungDeaktivierenWennNoetig() {
		if ((tpProjekt.getProjektDto() != null && tpProjekt.getProjektDto().getStatusCNr() != null
				&& tpProjekt.getProjektDto().getStatusCNr().equals(ProjektServiceFac.PROJEKT_STATUS_STORNIERT))
				|| (tpProjekt.getProjektDto() != null && tpProjekt.getProjektDto().getTInternerledigt() != null
						&& bAufInternErledigteBuchen == false)) {
			if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
				this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START).getButton().setEnabled(false);
			}
			if (this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_START) != null) {
				this.getHmOfButtons().get(Desktop.MY_OWN_NEW_ZEIT_STOP).getButton().setEnabled(false);
			}
		}

	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI) throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

		zeitbuchungDeaktivierenWennNoetig();

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// koennte mein lokaler FLR
			if (e.getSource() == panelQueryFLRPersonalZugewiesener) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);
					dto2ComponentsZugewiesener(personalDto);
					tpProjekt.setPersonalZugewiesenerDto(personalDto);
				}
			} else if (e.getSource() == panelQueryFLRProjekterledigungsgrund) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				tpProjekt.getProjektDto().setProjekterledigungsgrundIId((Integer) key);
			} else if (e.getSource() == panelQueryFLRPersonalErzeuger) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);
					dto2ComponentsErzeuger(personalDto);
					tpProjekt.setPersonalErzeugerDto(personalDto);
				}
			} else if (e.getSource() == panelQueryFLRPartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					partnerdatenVorbesetzen((Integer) key);
				}
			} else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPrimaryKey((Integer) key);
					wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatTitelAnrede());
					tpProjekt.setAnsprechpartnerDto(ansprechpartnerDto);
				}
			} else if (e.getSource() == panelQueryFLRBetreiberAnsprechpartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPrimaryKey((Integer) key);
					wtfBetreiberAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatAnrede());
					tpProjekt.getProjektDto().setAnsprechpartnerIIdBetreiber(ansprechpartnerDto.getIId());
					dto2ComponentsBetreiberAnsprechpartner(wsfPartnerBetreiber.getIKey(), ansprechpartnerDto.getIId());
				}
			} else if (e.getSource() == wsfPartnerBetreiber.getPanelQueryFLR()) {
				tpProjekt.getProjektDto().setAnsprechpartnerIIdBetreiber(null);
				wtfBetreiberAnsprechpartner.setText(null);
				dto2ComponentsBetreiberAnsprechpartner(wsfPartnerBetreiber.getIKey(), null);
			} else if (e.getSource() == panelQueryFLRArtikel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelTempDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);

				if (tpProjekt.getBereichIId() != null) {
					BereichDto bereichDto = DelegateFactory.getInstance().getProjektServiceDelegate()
							.bereichFindByPrimaryKey(tpProjekt.getBereichIId());

					if (Helper.short2boolean(bereichDto.getBProjektMitArtikel())
							&& Helper.short2boolean(bereichDto.getBProjektArtikeleindeutig())) {

						// Pruefen, ob schon verwendet
						ProjektDto pjDto = DelegateFactory.getInstance().getProjektDelegate()
								.projektfindByBereichIIdArtikelIId(bereichDto.getIId(), artikelTempDto.getIId());

						if (pjDto != null && !pjDto.getIId().equals(tpProjekt.getProjektDto().getIId())) {

							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"), LPMain
									.getMessageTextRespectUISPr("proj.artikel.bereits.zugeordnet", pjDto.getCNr()));

							return;

						}

					}
				}

				wtfArtikel.setText(artikelTempDto.formatArtikelbezeichnung());
				tpProjekt.getProjektDto().setArtikelIId(artikelTempDto.getIId());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				tpProjekt.setAnsprechpartnerDto(new AnsprechpartnerDto());
				wtfAnsprechpartner.setText(null);
			}
			if (e.getSource() == panelQueryFLRBetreiberAnsprechpartner) {
				tpProjekt.getProjektDto().setAnsprechpartnerIIdBetreiber(null);
				wtfBetreiberAnsprechpartner.setText(null);
			}
			if (e.getSource() == wsfPartnerBetreiber.getPanelQueryFLR()) {
				tpProjekt.getProjektDto().setAnsprechpartnerIIdBetreiber(null);
				wtfBetreiberAnsprechpartner.setText(null);
			}
			if (e.getSource() == panelQueryFLRArtikel) {
				tpProjekt.getProjektDto().setArtikelIId(null);
				wtfArtikel.setText(null);
			}
		}
	}

	private void partnerdatenVorbesetzen(Integer partnerIId) throws ExceptionLP, Throwable {
		PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
				.partnerFindByPrimaryKey((Integer) partnerIId);

		String gesperrterMandant = DelegateFactory.getInstance().getProjektDelegate()
				.istPartnerBeiEinemMandantenGesperrt(partnerDto.getIId());

		if (gesperrterMandant != null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
					LPMain.getTextRespectUISPr("projekt.partnerauswahl.kundegesperrt") + " " + gesperrterMandant);
		}

		KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByiIdPartnercNrMandantOhneExc(partnerDto.getIId(), LPMain.getTheClient().getMandant());

		// PJ18400
		if (kdDto != null) {
			DelegateFactory.getInstance().getKundeDelegate().pruefeKreditlimit(kdDto.getIId(), getInternalFrame());

			DelegateFactory.getInstance().getKundeDelegate().pruefeMahnstufe(kdDto.getIId());

		}

		tpProjekt.setPartnerDto(partnerDto);
		dto2ComponentsPartner(partnerDto);

		// den Ansprechpartner beim Partner zuruecksetzen

		AnsprechpartnerDto anspDtoTemp = null;

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_PROJEKT_ANSPRECHPARTNER_VORBESETZEN,
						ParameterFac.KATEGORIE_PROJEKT, LPMain.getTheClient().getMandant());
		if ((Boolean) parameter.getCWertAsObject()) {
			anspDtoTemp = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindErstenEinesPartnersOhneExc(tpProjekt.getPartnerDto().getIId());
		}

		if (anspDtoTemp != null) {

			tpProjekt.setAnsprechpartnerDto(anspDtoTemp);
		} else {
			tpProjekt.setAnsprechpartnerDto(new AnsprechpartnerDto());
		}
		dto2ComponentsAnsprechpartner(tpProjekt.getAnsprechpartnerDto());
	}

	private void dto2ComponentsAnsprechpartner(AnsprechpartnerDto ansprechpartnerDto) throws Throwable {

		partnerDtoVorbesetzen = tpProjekt.getPartnerDto();

		PartnerDto dto = partnerDtoVorbesetzen;

		if (ansprechpartnerDto != null) {
			if (ansprechpartnerDto.getCTelefon() != null) {

				wtfDurchwahl.setPartnerKommunikationDto(tpProjekt.getPartnerDto(), ansprechpartnerDto.getCTelefon());

				wtfDurchwahl.setTextDurchwahl(DelegateFactory.getInstance().getPartnerDelegate().enrichNumber(
						dto.getIId(), PartnerFac.KOMMUNIKATIONSART_TELEFON, ansprechpartnerDto.getCTelefon(), false));

				if (Helper.short2boolean(ansprechpartnerDto.getBDurchwahl())) {
					wtfDurchwahl.setIstAnsprechpartner(true);
				} else {
					wtfDurchwahl.setIstAnsprechpartner(false);
					wtfDurchwahl.setPartnerKommunikationDto(tpProjekt.getPartnerDto(),
							ansprechpartnerDto.getCTelefon());
				}

			} else {
				wtfDurchwahl.setPartnerKommunikationDto(tpProjekt.getPartnerDto(),
						tpProjekt.getPartnerDto().getCTelefon());
			}
			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatTitelAnrede());

			if (ansprechpartnerDto.getCHandy() != null) {
				wtfHandy.setPartnerKommunikationDto(ansprechpartnerDto.getPartnerDto(), ansprechpartnerDto.getCHandy());
				// wtfDurchwahl.setIstAnsprechpartner(true);
			} else {
				wtfHandy.setPartnerKommunikationDto(null, null);
			}

		} else {
			wtfAnsprechpartner.setText(null);

		}
	}

	private void dto2ComponentsBetreiberAnsprechpartner(Integer partnerIIdBetreiber,
			Integer ansprechpartnerIIdBetreiber) throws Throwable {

		AnsprechpartnerDto ansprechpartnerDtoBetreiber = null;

		if (ansprechpartnerIIdBetreiber == null) {
			ansprechpartnerDtoBetreiber = new AnsprechpartnerDto();
		} else {
			ansprechpartnerDtoBetreiber = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(ansprechpartnerIIdBetreiber);
		}

		if (ansprechpartnerDtoBetreiber != null && partnerIIdBetreiber != null) {

			PartnerDto partnerDtoBetreiber = DelegateFactory.getInstance().getPartnerDelegate()
					.partnerFindByPrimaryKey(partnerIIdBetreiber);

			if (ansprechpartnerDtoBetreiber.getCTelefon() != null) {

				wtfBetreiberDurchwahl.setPartnerKommunikationDto(partnerDtoBetreiber,
						ansprechpartnerDtoBetreiber.getCTelefon());

				wtfBetreiberDurchwahl.setTextDurchwahl(DelegateFactory.getInstance().getPartnerDelegate().enrichNumber(
						partnerIIdBetreiber, PartnerFac.KOMMUNIKATIONSART_TELEFON,
						ansprechpartnerDtoBetreiber.getCTelefon(), false));

				if (Helper.short2boolean(ansprechpartnerDtoBetreiber.getBDurchwahl())) {
					wtfBetreiberDurchwahl.setIstAnsprechpartner(true);
				} else {
					wtfBetreiberDurchwahl.setIstAnsprechpartner(false);
					wtfBetreiberDurchwahl.setPartnerKommunikationDto(partnerDtoBetreiber,
							ansprechpartnerDtoBetreiber.getCTelefon());
				}

			} else {
				wtfBetreiberDurchwahl.setPartnerKommunikationDto(partnerDtoBetreiber,
						partnerDtoBetreiber.getCTelefon());
			}
			wtfBetreiberAnsprechpartner.setText(ansprechpartnerDtoBetreiber.getPartnerDto().formatTitelAnrede());

			if (ansprechpartnerDtoBetreiber.getCHandy() != null) {
				wtfBetreiberHandy.setPartnerKommunikationDto(ansprechpartnerDtoBetreiber.getPartnerDto(),
						ansprechpartnerDtoBetreiber.getCHandy());
				// wtfDurchwahl.setIstAnsprechpartner(true);
			} else {
				wtfBetreiberHandy.setPartnerKommunikationDto(null, null);
			}

		} else {
			wtfBetreiberAnsprechpartner.setText(null);

		}
	}

	void dialogQueryArtikelFromListe(ActionEvent e) throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_LEEREN };
		panelQueryFLRArtikel = new PanelQueryFLR(null, ArtikelFilterFactory.getInstance().createFKArtikelliste(),
				com.lp.server.util.fastlanereader.service.query.QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("title.artikelauswahlliste"),
				ArtikelFilterFactory.getInstance().createFKVArtikel(), null);
		panelQueryFLRArtikel.setFilterComboBox(DelegateFactory.getInstance().getArtikelDelegate().getAllSprArtgru(),
				new FilterKriterium("ag.i_id", true, "" + "", FilterKriterium.OPERATOR_IN, false), false,
				LPMain.getTextRespectUISPr("lp.alle"), false);
		panelQueryFLRArtikel.befuellePanelFilterkriterienDirekt(
				ArtikelFilterFactory.getInstance().createFKDArtikelnummer(getInternalFrame()),
				ArtikelFilterFactory.getInstance().createFKDVolltextsuche());
		panelQueryFLRArtikel.setSelectedId(tpProjekt.getProjektDto().getArtikelIId());
		new DialogQuery(panelQueryFLRArtikel);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_PERSONAL_ZUGEWIESENER)) {
			panelQueryFLRPersonalZugewiesener = PersonalFilterFactory.getInstance().createPanelFLRPersonal(
					getInternalFrame(), true, false, tpProjekt.getPersonalZugewiesenerDto().getIId());

			new DialogQuery(panelQueryFLRPersonalZugewiesener);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PERSONAL_ERZEUGER)) {
			panelQueryFLRPersonalErzeuger = PersonalFilterFactory.getInstance().createPanelFLRPersonal(
					getInternalFrame(), true, false, tpProjekt.getPersonalErzeugerDto().getIId());
			new DialogQuery(panelQueryFLRPersonalErzeuger);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PARTNER)) {
			panelQueryFLRPartner = PartnerFilterFactory.getInstance().createPanelFLRPartner(getInternalFrame(),
					tpProjekt.getPartnerDto().getIId(), false);
			new DialogQuery(panelQueryFLRPartner);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER)) {
			if (tpProjekt.getPartnerDto().getIId() != null) {
				Integer selectedAnsprechpartner = null;
				if (tpProjekt.getAnsprechpartnerDto() != null) {
					selectedAnsprechpartner = tpProjekt.getAnsprechpartnerDto().getIId();
				}
				panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance().createPanelFLRAnsprechpartner(
						getInternalFrame(), tpProjekt.getPartnerDto().getIId(), selectedAnsprechpartner, true, true);
				new DialogQuery(panelQueryFLRAnsprechpartner);
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_BETREIBERANSPRECHPARTNER)) {
			if (wsfPartnerBetreiber.getIKey() != null) {

				panelQueryFLRBetreiberAnsprechpartner = PartnerFilterFactory.getInstance()
						.createPanelFLRAnsprechpartner(getInternalFrame(), wsfPartnerBetreiber.getIKey(),
								tpProjekt.getProjektDto().getAnsprechpartnerIIdBetreiber(), true, true);
				new DialogQuery(panelQueryFLRBetreiberAnsprechpartner);
			}
		} else if (e.getActionCommand().equals(MY_OWN_NEW_TOGGLE_INTERN_ERLEDIGT)) {
			// PJ 17558
			if (tpProjekt.getProjektDto().getIId() != null) {
				DelegateFactory.getInstance().getProjektDelegate()
						.toggleInternErledigt(tpProjekt.getProjektDto().getIId());
				eventYouAreSelected(false);
			}
		} else if (e.getActionCommand().equals(Desktop.MY_OWN_NEW_ZEIT_START)
				|| e.getActionCommand().equals(Desktop.MY_OWN_NEW_ZEIT_STOP)) {

			LPMain.getInstance().getDesktop().zeitbuchungAufBeleg(e.getActionCommand(), LocaleFac.BELEGART_PROJEKT,
					tpProjekt.getProjektDto().getIId());

		} else if (e.getActionCommand().equals(MY_OWN_NEW_DOKUMENTENABLAGE)) {
			PrintInfoDto values = DelegateFactory.getInstance().getJCRDocDelegate()
					.getPathAndPartnerAndTable(tpProjekt.getProjektDto().getIId(), QueryParameters.UC_ID_PROJEKT);

			DocPath docPath = values.getDocPath();
			Integer iPartnerIId = values.getiId();
			String sTable = values.getTable();
			if (docPath != null) {
				PanelDokumentenablage panelDokumentenverwaltung = new PanelDokumentenablage(getInternalFrame(),
						tpProjekt.getProjektDto().getIId().toString(), docPath, sTable,
						tpProjekt.getProjektDto().getIId().toString(), true, iPartnerIId);
				getInternalFrame().showPanelDialog(panelDokumentenverwaltung);
				getInternalFrame().addItemChangedListener(panelDokumentenverwaltung);
			} else {
				// Show Dialog
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("jcr.hinweis.keinpfad"));
			}
		} else if (e.getActionCommand().equals(MY_OWN_NEW_ANGEBOT)) {
			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANGEBOT)) {
				InternalFrameAngebot ifAB = (InternalFrameAngebot) LPMain.getInstance().getDesktop()
						.holeModul(LocaleFac.BELEGART_ANGEBOT);
				ifAB.getTabbedPaneAngebot().erstelleAngebotAusProjekt(tpProjekt.getProjektDto().getIId());
			}
		} else if (e.getActionCommand().equals(MY_OWN_NEW_AUFTRAG)) {

			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AUFTRAG)) {
				InternalFrameAuftrag ifAB = (InternalFrameAuftrag) LPMain.getInstance().getDesktop()
						.holeModul(LocaleFac.BELEGART_AUFTRAG);
				ifAB.getTabbedPaneAuftrag().erstelleAuftragAusProjekt(tpProjekt.getProjektDto().getIId());
			}

		} else if (e.getActionCommand().equals(MY_OWN_NEW_AGSTKL)) {

			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AGSTUECKLISTE)) {
				InternalFrameAngebotstkl ifAS = (InternalFrameAngebotstkl) LPMain.getInstance().getDesktop()
						.holeModul(LocaleFac.BELEGART_AGSTUECKLISTE);
				ifAS.getTabbedPaneAngebotstkl().erstelleAgstklAusProjekt(tpProjekt.getProjektDto().getIId());
			}

		} else if (e.getActionCommand().equals(MY_OWN_NEW_REKLAMATION)) {

			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_REKLAMATION)) {
				InternalFrameReklamation ifRK = (InternalFrameReklamation) LPMain.getInstance().getDesktop()
						.holeModul(LocaleFac.BELEGART_REKLAMATION);
				ifRK.getTabbedPaneReklamation().erstelleReklamationAusProjekt(tpProjekt.getProjektDto().getIId());
			}

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL_FROM_LISTE)) {
			dialogQueryArtikelFromListe(e);

		} else if (e.getSource().equals(wcoBereich)) {
			BereichDto bereichDto = DelegateFactory.getInstance().getProjektServiceDelegate()
					.bereichFindByPrimaryKey((Integer) wcoBereich.getKeyOfSelectedItem());

			wefText.setMandatoryField(Helper.short2boolean(bereichDto.getBDetailtextIstPflichtfeld()));
		}
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpProjekt.getProjektDto().getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpProjekt.getProjektDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpProjekt.getProjektDto().getPersonalIIdAendern());
		setStatusbarTAendern(tpProjekt.getProjektDto().getTAendern());
		setStatusbarStatusCNr(tpProjekt.getProjektDto().getStatusCNr());
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcoKategorie;
	}

	public void setDefaultsAusEmail(EmailParser parser) {
		if (parser.getContentType() != TextContentType.PLAIN) {
			// Nur Plain Text!
			return;
		}
		String betreff = parser.getSubject();
		String absender = parser.getFrom();
		String content = parser.getContent();

		wtfTitel.setText(betreff);
		wefText.setText(content);
		try {
			// Bei Email rein ziehen ist Benutzer Zugewiesener
			PersonalDto personalDtoBenutzer = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());
			dto2ComponentsZugewiesener(personalDtoBenutzer);
			tpProjekt.setPersonalZugewiesenerDto(personalDtoBenutzer);
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
		// absender sollte email addresse sein
		try {
			InternetAddress emailAddr = new InternetAddress(absender);
			try {
				String mailAddr = emailAddr.getAddress();
				AnsprechpartnerDto[] asps = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByEmail(mailAddr);
				if (asps.length > 0) {
					AnsprechpartnerDto ansprechpartnerDto = asps[0];
					PartnerDto partner = DelegateFactory.getInstance().getPartnerDelegate()
							.partnerFindByPrimaryKey(ansprechpartnerDto.getPartnerIId());
					PartnerDto anspPartnerPartnerDto = DelegateFactory.getInstance().getPartnerDelegate()
							.partnerFindByPrimaryKey(ansprechpartnerDto.getPartnerIIdAnsprechpartner());
					ansprechpartnerDto.setPartnerDto(anspPartnerPartnerDto);
					tpProjekt.setPartnerDto(partner);
					tpProjekt.setAnsprechpartnerDto(ansprechpartnerDto);
					dto2ComponentsPartner(partner);
					dto2ComponentsAnsprechpartner(ansprechpartnerDto);
					wtfEmail.setText(mailAddr);
				}
			} catch (ExceptionLP e) {
				e.printStackTrace();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} catch (AddressException e1) {
			// Ungueltige Adresse
		}
	}

}
