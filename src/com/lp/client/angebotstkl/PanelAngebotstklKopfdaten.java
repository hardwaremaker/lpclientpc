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
package com.lp.client.angebotstkl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.DialogNeueArtikelnummer;
import com.lp.client.bestellung.PanelBestellungWEP;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperBelegDateField;
import com.lp.client.frame.component.WrapperBildField;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperGotoKundeMapButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperMediaControl;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.IPartnerDto;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.BelegDatumClient;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.util.Facade;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelAngebotstklKopfdaten extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AgstklDto agstklDto = null;
	private KundeDto kundeDto = null;
	private InternalFrameAngebotstkl internalFrameAngebotstkl = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private PanelQueryFLR panelQueryFLRKunde = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;

	private WrapperLabel wlaEkpreisbasis = new WrapperLabel();
	private ButtonGroup bgEkpreisbasis = new ButtonGroup();

	private WrapperRadioButton wrbEkpreisbasisLief1Preis = new WrapperRadioButton();
	private WrapperRadioButton wrbEkpreisbasisNettopreis = new WrapperRadioButton();

	private WrapperGotoButton wbuArtikel = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_ARTIKEL_AUSWAHL);

	private WrapperTextArea wtaAngebot = new WrapperTextArea();

	private WrapperSelectField wsfProjekt = new WrapperSelectField(WrapperSelectField.PROJEKT, getInternalFrame(),
			true);

	private WrapperSelectField wsfStueckliste = new WrapperSelectField(WrapperSelectField.STUECKLISTE,
			getInternalFrame(), true);

	private ButtonGroup buttonGroup1 = new ButtonGroup();
	private WrapperGotoButton jpaKunde = null;
	private WrapperTextField wtfKunde = new WrapperTextField();

	private WrapperButton wbuAnsprechpartner = new WrapperButton();
	private WrapperTextField wtfAnsprechpartner = new WrapperTextField();

	private WrapperLabel wlaProjekt = new WrapperLabel();
	private WrapperTextField wtfProjekt = new WrapperTextField();
	
	private WrapperLabel wlaZeichnungsnummer = new WrapperLabel();
	private WrapperTextField wtfZeichnungsnummer = new WrapperTextField(30);

	private WrapperLabel wlaWaehrung = new WrapperLabel();
	private WrapperComboBox wcoWaehrung = new WrapperComboBox();

	private WrapperLabel wlaInitialkosten = new WrapperLabel();
	private WrapperNumberField wnfInitialkosten = new WrapperNumberField();
	
	private JLabel datenGeaendert = new JLabel("");

	private WrapperLabel wlaAngebote = new WrapperLabel();

	private WrapperCheckBox wcbVorlage = new WrapperCheckBox();

	private WrapperLabel wlaBelegdatum = new WrapperLabel();
	private WrapperDateField wdfBelegdatum = new WrapperDateField();

	private WrapperNumberField wnfKurs = new WrapperNumberField();

	private WrapperLabel wlaKurs = new WrapperLabel();
	
	private WrapperMediaControl wmcMedia = null;

	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "action_kunde_from_liste";
	public static final String ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE = "action_special_ansprechpartner_kunde";
	Integer partnerIId;

	public final static String MY_OWN_NEW_STKL = PanelBasis.ACTION_MY_OWN_NEW + "STKL";

	public InternalFrameAngebotstkl getInternalFrameAngebotstkl() {
		return internalFrameAngebotstkl;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return jpaKunde;
	}

	public PanelAngebotstklKopfdaten(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameAngebotstkl = (InternalFrameAngebotstkl) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);
		datenGeaendert.setText("");
		Integer key = getInternalFrameAngebotstkl().getAgstklDto().getIId();

		if (key != null) {

			agstklDto = DelegateFactory.getInstance().getAngebotstklDelegate()
					.agstklFindByPrimaryKey(getInternalFrameAngebotstkl().getAgstklDto().getIId());

			dto2Components();

			String cBez = "";

			if (getInternalFrameAngebotstkl().getAgstklDto().getKundeIId() != null) {

				KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(getInternalFrameAngebotstkl().getAgstklDto().getKundeIId());

				cBez = kundeDto.getPartnerDto().formatFixTitelName1Name2();
			}

			if (getInternalFrameAngebotstkl().getAgstklDto() != null) {
				if (getInternalFrameAngebotstkl().getAgstklDto().getCBez() != null) {
					cBez += " " + getInternalFrameAngebotstkl().getAgstklDto().getCBez();
				}
			}
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameAngebotstkl().getAgstklDto().getCNr() + ", " + cBez);

		} else {
			leereAlleFelder(this);
			wdfBelegdatum.setDate(new java.sql.Date(System.currentTimeMillis()));
			wcoWaehrung.setKeyOfSelectedItem(LPMain.getInstance().getTheClient().getSMandantenwaehrung());

			ParametermandantDto parametermandantDtoTagen = DelegateFactory.getInstance().getParameterDelegate()
					.getMandantparameter(LPMain.getInstance().getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE, ParameterFac.PARAMETER_EK_PREISBASIS);
			int iEkpreisbasis = ((Integer) parametermandantDtoTagen.getCWertAsObject()).intValue();

			if (iEkpreisbasis == AngebotstklFac.EK_PREISBASIS_LIEF1PREIS) {
				wrbEkpreisbasisLief1Preis.setSelected(true);
			} else {
				wrbEkpreisbasisNettopreis.setSelected(true);
			}

		}

	}

	private void setVerwendeteAngebote() throws Throwable {
		String s = DelegateFactory.getInstance().getAngebotstklDelegate()
				.getAngeboteDieBestimmteAngebotsstuecklisteVerwenden(agstklDto.getIId());
		wtaAngebot.setText(s);
	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	protected void dto2Components() throws Throwable {
		wsfStueckliste.setKey(agstklDto.getStuecklisteIId());

		if (agstklDto.getStuecklisteIId() != null) {
			StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
					.stuecklisteFindByPrimaryKey(agstklDto.getStuecklisteIId());
			wbuArtikel.setOKey(stklDto.getArtikelIId());
		} else {
			wbuArtikel.setOKey(null);
		}

		wmcMedia.setMimeType(agstklDto.getDatenformatCNr());
		wmcMedia.setOMedia(agstklDto.getOMedia());

		wsfProjekt.setKey(agstklDto.getProjektIId());
		wtfProjekt.setText(agstklDto.getCBez());
		
		wtfZeichnungsnummer.setText(agstklDto.getCZeichnungsnummer());
		
		wcoWaehrung.setKeyOfSelectedItem(agstklDto.getWaehrungCNr());
		wnfKurs.setDouble(agstklDto.getFWechselkursmandantwaehrungzuagstklwaehrung());
		
		wnfInitialkosten.setBigDecimal(agstklDto.getNInitialkosten());
		
		wcbVorlage.setShort(agstklDto.getBVorlage());

		kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(agstklDto.getKundeIId());
		// Goto Kunde Ziel setzen
		jpaKunde.setOKey(kundeDto.getIId());
		wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());
		partnerIId = kundeDto.getPartnerDto().getIId();
		if (agstklDto.getAnsprechpartnerIIdKunde() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(agstklDto.getAnsprechpartnerIIdKunde());
			wtfAnsprechpartner.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
		} else {
			wtfAnsprechpartner.setText(null);
		}

		setVerwendeteAngebote();

		wdfBelegdatum.setTimestamp(agstklDto.getTBelegdatum());

		if (agstklDto.getIEkpreisbasis().intValue() == AngebotstklFac.EK_PREISBASIS_LIEF1PREIS) {
			wrbEkpreisbasisLief1Preis.setSelected(true);
		} else {
			wrbEkpreisbasisNettopreis.setSelected(true);
		}

		if (Helper.short2boolean(agstklDto.getBDatengeaendert()) == true) {
			datenGeaendert.setForeground(Color.RED);
			datenGeaendert.setText(LPMain.getTextRespectUISPr("agstkl.preisefixiert.meldung.panel"));
		}

		this.setStatusbarPersonalIIdAendern(agstklDto.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(agstklDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(agstklDto.getTAnlegen());
		this.setStatusbarTAendern(agstklDto.getTAendern());

	}

	void dialogQueryKundeFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance().createPanelFLRKunde(getInternalFrame(), true, false,
				agstklDto.getKundeIId());
		new DialogQuery(panelQueryFLRKunde);
	}

	void dialogQueryAnsprechartnerFromListe(ActionEvent e) throws Throwable {

		if (partnerIId != null) {
			panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance().createPanelFLRAnsprechpartner(
					getInternalFrame(), partnerIId, agstklDto.getAnsprechpartnerIIdKunde(), true, true);
			new DialogQuery(panelQueryFLRAnsprechpartner);
		} else {
			DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
					LPMain.getInstance().getTextRespectUISPr("lp.error.kundenichtgewaehlt")); // UW->
			// CK
			// Konstante
		}
	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		setBorder(border);
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
		jpaKunde = new WrapperGotoKundeMapButton(new IPartnerDto() {
			public PartnerDto getPartnerDto() {
				return kundeDto == null ? new PartnerDto() : kundeDto.getPartnerDto();
			}
		});
		jpaKunde.setText(LPMain.getInstance().getTextRespectUISPr("button.kunde"));

		wtfKunde.setMandatoryField(true);
		wtfKunde.setColumnsMax(PartnerFac.MAX_NAME);
		wtfKunde.setActivatable(false);
		wtfKunde.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wlaBelegdatum = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("label.belegdatum"));

		wcbVorlage.setText(LPMain.getInstance().getTextRespectUISPr("agstkl.vorlage"));

		wnfKurs.setFractionDigits(6);
		wnfKurs.setActivatable(false);
		wnfKurs.setMandatoryField(true);

		wlaProjekt.setText(LPMain.getInstance().getTextRespectUISPr("agstkl.projekt.bezeichnung"));
		
		wlaZeichnungsnummer.setText(LPMain.getInstance().getTextRespectUISPr("agstkl.projekt.zeichnungsnummer"));

		wlaEkpreisbasis.setText(LPMain.getInstance().getTextRespectUISPr("as.agstkl.mengenstaffel.ekpreisbasis"));

		wrbEkpreisbasisLief1Preis
				.setText(LPMain.getInstance().getTextRespectUISPr("as.agstkl.mengenstaffel.ekpreisbasis.lief1preis"));
		wrbEkpreisbasisNettopreis
				.setText(LPMain.getInstance().getTextRespectUISPr("as.agstkl.mengenstaffel.ekpreisbasis.nettopreis"));

		wlaAngebote.setText(LPMain.getInstance().getTextRespectUISPr("as.agstkl.verwendeteangebote"));
		wlaAngebote.setHorizontalAlignment(SwingConstants.LEFT);

		jpaKunde.setActionCommand(this.ACTION_SPECIAL_KUNDE_FROM_LISTE);
		jpaKunde.addActionListener(this);

		wbuAnsprechpartner.setText(LPMain.getInstance().getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartner.setActionCommand(this.ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE);
		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner.setActivatable(false);

		wlaWaehrung.setText(LPMain.getInstance().getTextRespectUISPr("label.waehrung"));
		wcoWaehrung.setMandatoryField(true);
		wcoWaehrung.addActionListener(new PanelAngebotstklKopfdaten_wcoWaehrung_actionAdapter(this));

		bgEkpreisbasis.add(wrbEkpreisbasisLief1Preis);
		bgEkpreisbasis.add(wrbEkpreisbasisNettopreis);

		
		wlaInitialkosten.setText(LPMain.getInstance().getTextRespectUISPr("stkl.positionen.initialkosten"));
		
		wmcMedia = new WrapperMediaControl(getInternalFrame(), "");

		wmcMedia.setPreferredSize(new Dimension(90, Defaults.getInstance().getControlHeight()));
		wmcMedia.setMaximumSize(new Dimension(90, Defaults.getInstance().getControlHeight()));

		wdfBelegdatum = new WrapperBelegDateField(
				new BelegDatumClient(LPMain.getInstance().getTheClient().getMandant(), new GregorianCalendar(), false));
		wdfBelegdatum.setMandatoryField(true);

		wtaAngebot.setActivatable(false);

		wlaKurs.setText(LPMain.getInstance().getTextRespectUISPr("label.kurs"));

		wbuArtikel.getWrapperButtonGoTo().setToolTipText(LPMain.getTextRespectUISPr("fert.los.kopfdaten.gotoartikel"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaBelegdatum, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBelegdatum, new GridBagConstraints(1, iZeile, 1, 1, 0.35, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbVorlage, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		iZeile++;

		jpaWorkingOn.add(jpaKunde, new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, iZeile, 3, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		
		
		jpaWorkingOn.add(wlaZeichnungsnummer, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wtfZeichnungsnummer, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++;

		jpaWorkingOn.add(wlaProjekt, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {

			ParametermandantDto parametermandantDto = DelegateFactory.getInstance().getParameterDelegate()
					.getMandantparameter(LPMain.getInstance().getTheClient().getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PROJEKT_IST_PFLICHTFELD);
			boolean bProjektIstPflichtfeld = ((Boolean) parametermandantDto.getCWertAsObject());
			if (bProjektIstPflichtfeld) {
				wsfProjekt.setMandatoryField(true);
			}

			jpaWorkingOn.add(wtfProjekt, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

			jpaWorkingOn.add(wsfProjekt.getWrapperGotoButton(), new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
			jpaWorkingOn.add(wsfProjekt.getWrapperTextField(), new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		} else {

			jpaWorkingOn.add(wtfProjekt, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			
			
			
		}

		iZeile++;

		jpaWorkingOn.add(wlaWaehrung, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoWaehrung, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaKurs, new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKurs, new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		// PJ18725
		jpaWorkingOn.add(wlaEkpreisbasis, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbEkpreisbasisLief1Preis, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wlaInitialkosten, new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfInitialkosten, new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wrbEkpreisbasisNettopreis, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		wsfStueckliste.setActivatable(false);
		wsfStueckliste.getWrapperGotoButton().setActivatable(false);

		jpaWorkingOn.add(wsfStueckliste.getWrapperGotoButton(), new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 22), 0, 0));

		jpaWorkingOn.add(wbuArtikel.getWrapperButtonGoTo(), new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 10, 0));

		jpaWorkingOn.add(wsfStueckliste.getWrapperTextField(), new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		/*
		 * jpaWorkingOn.add(wbuMaterial, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
		 * GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
		 * 2), 0, 0)); jpaWorkingOn.add(wtfMaterial, new GridBagConstraints(1, iZeile,
		 * 1, 1, 0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new
		 * Insets(2, 2, 2, 2), 0, 0));
		 */

		iZeile++;
		jpaWorkingOn.add(wmcMedia, new GridBagConstraints(0, iZeile, 4, 1, 1, 10, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 100));
		iZeile++;

		jpaWorkingOn.add(datenGeaendert, new GridBagConstraints(0, iZeile, 4, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaAngebote, new GridBagConstraints(0, iZeile, 4, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		JScrollPane scrollPane = new JScrollPane(wtaAngebot);

		jpaWorkingOn.add(scrollPane, new GridBagConstraints(0, iZeile, 4, 1, 0.1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

		boolean hatRecht = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_STK_STUECKLISTE_CUD);
		if (hatRecht) {
			getToolBar().addButtonCenter("/com/lp/client/res/text_code_colored16x16.png",
					LPMain.getTextRespectUISPr("agstkl.instklumwandel"), MY_OWN_NEW_STKL, null,
					RechteFac.RECHT_STK_STUECKLISTE_CUD);

		}

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		agstklDto = new AgstklDto();
		kundeDto = new KundeDto();
		partnerIId = null;
		getInternalFrameAngebotstkl().setAgstklDto(agstklDto);
		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_FROM_LISTE)) {
			dialogQueryKundeFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER_KUNDE)) {
			dialogQueryAnsprechartnerFromListe(e);
		} else if (MY_OWN_NEW_STKL.equals(e.getActionCommand())) {
			DialogNeueArtikelnummer d = new DialogNeueArtikelnummer(getInternalFrame());
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);
			if (d.getArtikelnummerNeu() != null) {
				Integer stuecklisteIId = DelegateFactory.getInstance().getAngebotstklDelegate()
						.erzeugeStuecklisteAusAgstkl(agstklDto.getIId(), d.getArtikelnummerNeu());
				wsfStueckliste.setKey(stuecklisteIId);
			}
		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_AGSTKL;
	}

	protected void setDefaults() throws Throwable {
		wcoWaehrung.setMap(DelegateFactory.getInstance().getLocaleDelegate().getAllWaehrungen());

	}

	protected void components2Dto() throws Throwable {
		agstklDto.setCBez(wtfProjekt.getText());
		agstklDto.setCZeichnungsnummer(wtfZeichnungsnummer.getText());
		agstklDto.setTBelegdatum(wdfBelegdatum.getTimestamp());
		agstklDto.setWaehrungCNr((String) wcoWaehrung.getSelectedItem());
		agstklDto.setProjektIId(wsfProjekt.getIKey());
		agstklDto.setBVorlage(wcbVorlage.getShort());
		agstklDto.setNInitialkosten(wnfInitialkosten.getBigDecimal());

		if (wmcMedia.getMimeType().equals(Defaults.getInstance().getSComboboxEmptyEntry())) {
			agstklDto.setDatenformatCNr(null);
			agstklDto.setOMedia(null);
			agstklDto.setCDateiname(null);
		} else {
			agstklDto.setDatenformatCNr(wmcMedia.getMimeType());
			agstklDto.setOMedia(wmcMedia.getOMediaImage());
			agstklDto.setCDateiname(wmcMedia.getDateiname());
		}

		if (wrbEkpreisbasisLief1Preis.isSelected()) {
			agstklDto.setIEkpreisbasis(AngebotstklFac.EK_PREISBASIS_LIEF1PREIS);
		} else {
			agstklDto.setIEkpreisbasis(AngebotstklFac.EK_PREISBASIS_NETTOPREIS);
		}

	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getAngebotstklDelegate().removeAgstkl(agstklDto);
		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (agstklDto.getIId() == null) {
				agstklDto.setIId(DelegateFactory.getInstance().getAngebotstklDelegate().createAgstkl(agstklDto));
				setKeyWhenDetailPanel(agstklDto.getIId());
				agstklDto = DelegateFactory.getInstance().getAngebotstklDelegate()
						.agstklFindByPrimaryKey(agstklDto.getIId());
				kundeDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(agstklDto.getKundeIId());
				internalFrameAngebotstkl.setAgstklDto(agstklDto);
			} else {
				DelegateFactory.getInstance().getAngebotstklDelegate().updateAgstkl(agstklDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(agstklDto.getIId().toString());
			}

			eventYouAreSelected(false);

			agstklDto = DelegateFactory.getInstance().getAngebotstklDelegate()
					.agstklFindByPrimaryKey(agstklDto.getIId());
			internalFrameAngebotstkl.setAgstklDto(agstklDto);
		}
	}

	public void setDefaultsAusProjekt(Integer projektIId) throws Throwable {
		ProjektDto projektDto = DelegateFactory.getInstance().getProjektDelegate().projektFindByPrimaryKey(projektIId);

		wtfProjekt.setText(projektDto.getCTitel());
		wsfProjekt.setKey(projektDto.getIId());

		KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByiIdPartnercNrMandantOhneExc(
				projektDto.getPartnerIId(), LPMain.getInstance().getTheClient().getMandant());
		if (kundeDto == null) {
			// Dann Kunde zuerst anlegen

			PartnerDto pDto = DelegateFactory.getInstance().getPartnerDelegate()
					.partnerFindByPrimaryKey(projektDto.getPartnerIId());

			boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.kunde.auspartner.anlegen.teil1")
							+ pDto.formatFixTitelName1Name2()
							+ LPMain.getInstance().getTextRespectUISPr("lp.kunde.auspartner.anlegen.teil2"));

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

		DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(kundeDto.getIId(), null, getInternalFrame());

		wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());

		agstklDto.setKundeIId(kundeDto.getIId());

		if (projektDto.getAnsprechpartnerIId() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(projektDto.getAnsprechpartnerIId());

			agstklDto.setAnsprechpartnerIIdKunde(ansprechpartnerDto.getIId());

			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatTitelAnrede());
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKunde) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey((Integer) key);

				AnsprechpartnerDto ansprechpartnerDto = null;
				// Ansprechpartner vorbesetzen?
				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
						.getParameterDelegate().getParametermandant(ParameterFac.PARAMETER_ANGEBOTSTKL_ANSP_VORBESETZEN,
								ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
								LPMain.getInstance().getTheClient().getMandant());
				if ((Boolean) parameter.getCWertAsObject()) {
					ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerFindErstenEinesPartnersOhneExc(kundeDto.getPartnerIId());
				}
				if (ansprechpartnerDto != null) {
					wtfAnsprechpartner.setText(ansprechpartnerDto.formatFixTitelVornameNachnameNTitel());
					agstklDto.setAnsprechpartnerIIdKunde(ansprechpartnerDto.getIId());
				} else {
					wtfAnsprechpartner.setText("");
					agstklDto.setAnsprechpartnerIIdKunde(null);
				}
				agstklDto.setWaehrungCNr(kundeDto.getCWaehrung());
				wcoWaehrung.setKeyOfSelectedItem(kundeDto.getCWaehrung());

				jpaKunde.setOKey(kundeDto.getIId());
				wtfKunde.setText(kundeDto.getPartnerDto().formatTitelAnrede());
				partnerIId = kundeDto.getPartnerDto().getIId();

				agstklDto.setKundeIId(kundeDto.getIId());
				if (kundeDto.getCWaehrung() != null) {
					BigDecimal fKurs = DelegateFactory.getInstance().getLocaleDelegate()
							.getWechselkurs2(DelegateFactory.getInstance().getMandantDelegate()
									.mandantFindByPrimaryKey(LPMain.getInstance().getTheClient().getMandant())
									.getWaehrungCNr(), kundeDto.getCWaehrung());

					wnfKurs.setBigDecimal(fKurs);
					if (fKurs != null) {
						agstklDto.setFWechselkursmandantwaehrungzuagstklwaehrung(new Double(fKurs.doubleValue()));
					} else {
						agstklDto.setFWechselkursmandantwaehrungzuagstklwaehrung(null);

					}
				}
			} else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey((Integer) key);

				wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatTitelAnrede());
				agstklDto.setAnsprechpartnerIIdKunde(ansprechpartnerDto.getIId());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				wtfAnsprechpartner.setText(null);
				agstklDto.setAnsprechpartnerIIdKunde(null);
			}
		}
	}

	public void wcoWaehrung_actionPerformed(ActionEvent e) {
		try {
			if (agstklDto != null) {
				WaehrungDto waehrungDto = DelegateFactory.getInstance().getLocaleDelegate()
						.waehrungFindByPrimaryKey((String) wcoWaehrung.getSelectedItem());

				BigDecimal fKurs = DelegateFactory.getInstance().getLocaleDelegate()
						.getWechselkurs2(DelegateFactory.getInstance().getMandantDelegate()
								.mandantFindByPrimaryKey(LPMain.getInstance().getTheClient().getMandant())
								.getWaehrungCNr(), waehrungDto.getCNr());

				wnfKurs.setBigDecimal(fKurs);
				if (fKurs != null) {
					agstklDto.setFWechselkursmandantwaehrungzuagstklwaehrung(new Double(fKurs.doubleValue()));
				} else {
					agstklDto.setFWechselkursmandantwaehrungzuagstklwaehrung(null);

				}
			}
		} catch (Throwable ex) {
			handleException(ex, false);
		}
	}

}

class PanelAngebotstklKopfdaten_wcoWaehrung_actionAdapter implements ActionListener {
	private PanelAngebotstklKopfdaten adaptee;

	PanelAngebotstklKopfdaten_wcoWaehrung_actionAdapter(PanelAngebotstklKopfdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoWaehrung_actionPerformed(e);
	}
}
