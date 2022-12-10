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
package com.lp.client.partner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.auftrag.DialogRechnungsadresseBestellnummerProjekt;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
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
import com.lp.client.frame.component.WrapperHtmlField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.pc.Texts;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.editor.util.TextBlockOverflowException;
import com.lp.server.partner.service.AnsprechpartnerfunktionDto;
import com.lp.server.partner.service.MaileSerienbriefResponse;
import com.lp.server.partner.service.SerienbriefDto;
import com.lp.server.partner.service.SerienbriefEmpfaengerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse kuemmert sich um die Sereienbriefkopfdaten.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Josef Ornetsmueller; 18.11.05</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2012/08/29 14:29:29 $
 */
public class PanelSerienbriefKopfdaten extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneSerienbrief tpSerienbrief = null;

	private JPanel jpaWorking = null;
	private GridBagLayout gblWorkingPanel = null;
	private GridBagLayout gblAussen = null;

	private WrapperTextField wtfBezeichnung = null;
	private WrapperLabel wlaBezeichnung = null;

	private WrapperTextField wtfPLZ = null;
	private WrapperLabel wlaPLZ = null;

	private WrapperButton wbuLKZ = null;
	private WrapperTextField wtfLKZ = null;

	private WrapperCheckBox wcbKD = null;
	private WrapperCheckBox wcbInteressent = null;
	private WrapperCheckBox wcbLF = null;
	private WrapperCheckBox wcbMoeglicheLF = null;
	private WrapperCheckBox wcbVersteckte = null;
	private WrapperCheckBox wcbPartner = null;
	private WrapperCheckBox wcbMitzugeordnetenfirmen = null;
	private WrapperCheckBox wcbNewsletter = null;

	private WrapperCheckBox wcbWennKeinAnspMitFktDannErster = new WrapperCheckBox();;

	protected WrapperLabel wlaLocale = new WrapperLabel();
	protected WrapperComboBox wcoLocale = new WrapperComboBox();

	private WrapperRadioButton wrbLogischesODER = new WrapperRadioButton();
	private WrapperRadioButton wrbLogischesUND = new WrapperRadioButton();
	private ButtonGroup bgVerknuepfung = new ButtonGroup();

	private WrapperDateField wdfZeitraumVon = new WrapperDateField();
	private WrapperDateField wdfZeitraumBis = new WrapperDateField();

	private WrapperLabel wlaFuerKunden = new WrapperLabel();
	private WrapperLabel wlaBisUmsatz = new WrapperLabel();

	private WrapperNumberField wnfAbUmsatz = new WrapperNumberField();
	private WrapperNumberField wnfBisUmsatz = new WrapperNumberField();

	private WrapperButton wbuAnsprechpartnerfunktion = null;
	private WrapperTextField wtfAnsprechpartnerfunktion = null;
	private WrapperCheckBox wcbAnsprechpartnerfunktionAuchOhne = null;

	private WrapperLabel wlaBetreff = null;
	private WrapperTextField wtfBetreff = new WrapperTextField();

	private WrapperEditorField wefText = null;

	private WrapperSelectField wsfBranche = null;
	private WrapperSelectField wsfPartnerklasse = null;

	private WrapperHtmlField whtmlText = null;

	private PanelQueryFLR panelQueryFLRAnsprechpartnerfunktion = null;
	static final public String ACTION_SPECIAL_FLR_ANSPRECHPARTNER_FUNKTION = "action_special_flr_ansprechpartner_funktion";

	private PanelQueryFLR panelQueryFLRLand = null;
	static final public String ACTION_SPECIAL_FLR_LAND = "action_special_flr_land";

	private String EXTRA_NEU_SERIENMAIL = "extra_neu_serienmail";
	private String MY_OWN_NEW_SERIENMAIL = PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_SERIENMAIL;

	private String EXTRA_NEU_SERIENFAX = "extra_neu_serienfax";
	private String MY_OWN_NEW_SERIENFAX = PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_SERIENFAX;

	private String EXTRA_NEU_SERIENPRINT = "extra_neu_serienprint";
	private String MY_OWN_NEW_SERIENPRINT = PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_SERIENPRINT;
	private String MY_OWN_NEW_RAWHTML_IMPORT = PanelBasis.LEAVEALONE + "RAWHTML_IMPORT";

	private String EXTRA_NEU_WIEDERVORLAGE = "extra_neu_wiedervorlage";
	private String MY_OWN_NEW_WIEDERVORLAGE = PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_WIEDERVORLAGE;

	public PanelSerienbriefKopfdaten(InternalFrame internalFrame, String add2TitleI, Object keyI,
			TabbedPaneSerienbrief tpSerienbrief) throws Throwable {

		super(internalFrame, add2TitleI, keyI);
		this.tpSerienbrief = tpSerienbrief;
		jbInit();
		initComponents();
		initPanel();
	}

	private SerienbriefDto getSerienbriefDto() {
		return getTabbedPaneSerienbrief().getSerienbriefDto();
	}

	public void beHtml() {
		getSerienbriefDto().setBHtml(Helper.boolean2Short(true));
		setHmButtonVisible(MY_OWN_NEW_SERIENFAX, false);
		setHmButtonVisible(MY_OWN_NEW_SERIENPRINT, false);
	}

	public void bePlain() {
		getSerienbriefDto().setBHtml(Helper.boolean2Short(false));
		setHmButtonVisible(MY_OWN_NEW_SERIENFAX, true);
		setHmButtonVisible(MY_OWN_NEW_SERIENPRINT, true);
	}

	public void enableHtml(boolean enable) {
		getSerienbriefDto().setBHtml(Helper.boolean2Short(enable));
	}

	public boolean isHtmlEnabled() {
		if (getSerienbriefDto() == null)
			return false;
		return Helper.short2boolean(getSerienbriefDto().getBHtml());
	}

	public void beEditMode(boolean htmlMode) throws TextBlockOverflowException {
		if (htmlMode && isHtmlEnabled()) {
			switchTextPanels(htmlMode);
			getSerienbriefDto().setBHtml(Helper.boolean2Short(htmlMode));

			String c = whtmlText.getPlainText();
			if (!(c.startsWith("<html") || c.startsWith("<!DOCTYPE"))) {
				whtmlText.startEditing();
			}
		}
	}

	private void initPanel() throws Throwable {
	}

	private void jbInit() throws Throwable {

		getInternalFrame().addItemChangedListener(this);

		// buttons.
		resetToolsPanel();

		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		createAndSaveAndShowButton("/com/lp/client/res/mail.png",
				LPMain.getInstance().getTextRespectUISPr("lp.drucken.alsemailversenden"), MY_OWN_NEW_SERIENMAIL, null);

		createAndSaveAndShowButton("/com/lp/client/res/fax.png",
				LPMain.getInstance().getTextRespectUISPr("lp.drucken.alsfaxversenden"), MY_OWN_NEW_SERIENFAX, null);

		createAndSaveAndShowButton("/com/lp/client/res/printer.png",
				LPMain.getInstance().getTextRespectUISPr("lp.drucken.seriendruck"), MY_OWN_NEW_SERIENPRINT, null);

		createAndSaveAndShowButton("/com/lp/client/res/download.png",
				LPMain.getInstance().getTextRespectUISPr("lp.serienbrief.import.html"), MY_OWN_NEW_RAWHTML_IMPORT,
				null);

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_KONTAKTMANAGMENT)) {
			createAndSaveAndShowButton("/com/lp/client/res/index_down.png",
					LPMain.getInstance().getTextRespectUISPr("part.serienbrief.wiedervorlage"),
					MY_OWN_NEW_WIEDERVORLAGE, null);
		}

		wsfBranche = new WrapperSelectField(WrapperSelectField.BRANCHE, getInternalFrame(), true);
		wsfPartnerklasse = new WrapperSelectField(WrapperSelectField.PARTNERKLASSE, getInternalFrame(), true);

		wrbLogischesODER.setText(LPMain.getInstance().getTextRespectUISPr("part.serienbrief.selektionen.oder"));
		wrbLogischesUND.setText(LPMain.getInstance().getTextRespectUISPr("part.serienbrief.selektionen.und"));

		wcbWennKeinAnspMitFktDannErster
				.setText(LPMain.getInstance().getTextRespectUISPr("part.serienbrief.wennkeinanspmitfktdannerster"));

		bgVerknuepfung.add(wrbLogischesODER);
		bgVerknuepfung.add(wrbLogischesUND);

		// von hier ...
		// das aussenpanel hat immer das gridbaglayout.
		gblAussen = new GridBagLayout();
		setLayout(gblAussen);

		// actionpanel von Oberklasse holen und anhaengen.
		add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// Workingpanel
		jpaWorking = new JPanel();
		gblWorkingPanel = new GridBagLayout();
		jpaWorking.setLayout(gblWorkingPanel);
		add(jpaWorking, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		wlaBezeichnung = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));
		wtfBezeichnung = new WrapperTextField();
		wtfBezeichnung.setMandatoryField(true);

		wlaFuerKunden.setText(LPMain.getInstance().getTextRespectUISPr("part.serienbrief.fuerkunden"));
		wlaBisUmsatz.setText(LPMain.getInstance().getTextRespectUISPr("part.serienbrief.bisumsatz"));

		wlaLocale.setText(LPMain.getInstance().getTextRespectUISPr("part.sprache.kommunikation"));

		wcoLocale.setMandatoryField(true);

		wcoLocale.setMap(
				DelegateFactory.getInstance().getLocaleDelegate().getAllLocales(LPMain.getTheClient().getLocUi()));

		wlaPLZ = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.plz.a"));
		wtfPLZ = new WrapperTextField(SystemFac.MAX_PLZ);

		wbuLKZ = new WrapperButton(LPMain.getInstance().getTextRespectUISPr("lp.land.flr"));
		wtfLKZ = new WrapperTextField(SystemFac.MAX_LKZ);
		wbuLKZ.addActionListener(this);
		wbuLKZ.setActionCommand(ACTION_SPECIAL_FLR_LAND);

		wlaBetreff = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("label.betreff"));
		wefText = new WrapperEditorField(getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("lp.bemerkung"));
		wcbKD = new WrapperCheckBox(LPMain.getInstance().getTextRespectUISPr("part.kunden_alle"));
		wcbInteressent = new WrapperCheckBox(LPMain.getInstance().getTextRespectUISPr("part.interessenten_alle"));
		wcbVersteckte = new WrapperCheckBox(LPMain.getInstance().getTextRespectUISPr("lp.versteckte"));

		wcbLF = new WrapperCheckBox(LPMain.getInstance().getTextRespectUISPr("part.lieferanten_alle"));
		wcbMoeglicheLF = new WrapperCheckBox(LPMain.getInstance().getTextRespectUISPr("part.lieferanten_moegliche"));

		wcbPartner = new WrapperCheckBox(LPMain.getInstance().getTextRespectUISPr("part.partner_alle"));
		wcbMitzugeordnetenfirmen = new WrapperCheckBox(
				LPMain.getInstance().getTextRespectUISPr("part.serienbrief.mitzugeordnetenfirmen"));

		wcbMitzugeordnetenfirmen.addActionListener(this);
		wcbPartner.addActionListener(this);

		wcbWennKeinAnspMitFktDannErster.addActionListener(this);

		wbuAnsprechpartnerfunktion = new WrapperButton(
				LPMain.getInstance().getTextRespectUISPr("part.ansprechpartner_funktion"));
		wbuAnsprechpartnerfunktion.setActionCommand(ACTION_SPECIAL_FLR_ANSPRECHPARTNER_FUNKTION);
		wbuAnsprechpartnerfunktion.addActionListener(this);
		wtfAnsprechpartnerfunktion = new WrapperTextField();
		wtfAnsprechpartnerfunktion.setActivatable(false);
		wcbAnsprechpartnerfunktionAuchOhne = new WrapperCheckBox(
				LPMain.getInstance().getTextRespectUISPr("part.ansprechpartnerfkt_auch_ohne"));
		wcbAnsprechpartnerfunktionAuchOhne.addActionListener(this);
		wcbNewsletter = new WrapperCheckBox(LPMain.getInstance().getTextRespectUISPr("part.serienbrief.newsletter"));
		wcbNewsletter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					refreshComponentEnablingNewsletter();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		wtfBetreff.setColumnsMax(80);

		// Zeile
		iZeile++;
		jpaWorking.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorking.add(wbuAnsprechpartnerfunktion, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wtfAnsprechpartnerfunktion, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wcbAnsprechpartnerfunktionAuchOhne, new GridBagConstraints(2, iZeile, 2, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorking.add(wlaPLZ, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wtfPLZ, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wcbWennKeinAnspMitFktDannErster, new GridBagConstraints(2, iZeile, 2, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorking.add(wbuLKZ, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wtfLKZ, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wlaLocale, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorking.add(wcoLocale, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 00, 0));

		iZeile++;
		jpaWorking.add(wsfPartnerklasse.getWrapperButton(), new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 150, 0));
		jpaWorking.add(wsfPartnerklasse.getWrapperTextField(), new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorking.add(wsfBranche.getWrapperButton(), new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wsfBranche.getWrapperTextField(), new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 200, 0));

		// Zeile
		iZeile++;
		jpaWorking.add(wcbKD, new GridBagConstraints(0, iZeile, 1, 1, 0.37, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wcbLF, new GridBagConstraints(1, iZeile, 1, 1, 0.45, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorking.add(wcbVersteckte, new GridBagConstraints(2, iZeile, 1, 1, 0.75, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorking.add(wcbNewsletter, new GridBagConstraints(3, iZeile, 2, 1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorking.add(wcbInteressent, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wcbMoeglicheLF, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wcbPartner, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wcbMitzugeordnetenfirmen, new GridBagConstraints(3, iZeile, 2, 1, 0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorking.add(wlaFuerKunden, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 70, 0));
		jpaWorking.add(wnfAbUmsatz, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 120), 0, 0));

		jpaWorking.add(wlaBisUmsatz, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 50, 2, 2), 0, 0));
		jpaWorking.add(wnfBisUmsatz, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));

		iZeile++;
		jpaWorking.add(new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.imzeitraum")),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		jpaWorking.add(wdfZeitraumVon, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 80), 0, 0));

		jpaWorking.add(wdfZeitraumBis, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 80), 0, 0));
		jpaWorking.add(wrbLogischesODER, new GridBagConstraints(3, iZeile, 2, 1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorking.add(wlaBetreff, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorking.add(wtfBetreff, new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));

		jpaWorking.add(wrbLogischesUND, new GridBagConstraints(3, iZeile, 2, 1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorking.add(wefText, new GridBagConstraints(0, iZeile, 4, 1, 0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		whtmlText = new WrapperHtmlField(getInternalFrame(), LPMain.getTextRespectUISPr("lp.bemerkung"), true);
		jpaWorking.add(whtmlText, new GridBagConstraints(0, iZeile, 4, 1, 0.0, 0.5, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		setHmButtonVisible(MY_OWN_NEW_RAWHTML_IMPORT, false);
		setHmButtonEnabled(MY_OWN_NEW_RAWHTML_IMPORT, true);
	}

	private void switchTextPanels(boolean isHtml) {
		if (isHtml) {
			whtmlText.setEnabled(true);
			whtmlText.setVisible(true);
			wefText.setEnabled(false);
			wefText.setVisible(false);
		} else {
			whtmlText.setEnabled(false);
			whtmlText.setVisible(false);
			wefText.setEnabled(true);
			wefText.setVisible(true);
		}
	}

	private void refreshComponentEnablingNewsletter() throws Throwable {
		boolean enable = !wcbNewsletter.isSelected();
		wcbMitzugeordnetenfirmen.setActivatable(enable);
		if (!enable)
			wcbMitzugeordnetenfirmen.setSelected(false);
		updateButtons(getLockedstateDetailMainKey());
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_SERIENBRIEF;
	}

	protected void eventActionSpecial(ActionEvent eI) throws Throwable {

		if (eI.getSource().equals(wcbAnsprechpartnerfunktionAuchOhne)) {
			if (wcbAnsprechpartnerfunktionAuchOhne.isSelected()) {
				wcbWennKeinAnspMitFktDannErster.setSelected(false);
			}
		}
		if (eI.getSource().equals(wcbWennKeinAnspMitFktDannErster)) {
			if (wcbAnsprechpartnerfunktionAuchOhne.isSelected()) {
				wcbAnsprechpartnerfunktionAuchOhne.setSelected(false);
			}
		}

		if (eI.getSource().equals(wcbMitzugeordnetenfirmen)) {
			if (wcbMitzugeordnetenfirmen.isSelected()) {
				// Das alles geht nicht, wenn wcbMitzugeordnetenfirmen
				// ausgewaehlt
				wcbPartner.setSelected(true);
				getSerienbriefDto().setAnsprechpartnerfunktionIId(null);
				wtfAnsprechpartnerfunktion.setText(null);
				wcbAnsprechpartnerfunktionAuchOhne.setSelected(false);
			}
		}
		if (eI.getSource().equals(wcbPartner)) {
			if (wcbPartner.isSelected() == false) {
				wcbMitzugeordnetenfirmen.setSelected(false);
			}
		}

		if (eI.getActionCommand().equals(ACTION_SPECIAL_FLR_ANSPRECHPARTNER_FUNKTION)) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_LEEREN, };
			QueryType[] querytypes = null;
			FilterKriterium[] filters = null;
			panelQueryFLRAnsprechpartnerfunktion = new PanelQueryFLR(querytypes, filters,
					QueryParameters.UC_ID_ANSPRECHPARTNERFUNKTION, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("part.ansprechpartnerfkt"));
			new DialogQuery(panelQueryFLRAnsprechpartnerfunktion);

		}

		else if (eI.getActionCommand().equals(ACTION_SPECIAL_FLR_LAND)) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_LEEREN, };
			QueryType[] querytypes = null;
			FilterKriterium[] filters = null;
			panelQueryFLRLand = new PanelQueryFLR(querytypes, filters, QueryParameters.UC_ID_LAND, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("lp.land"));
			new DialogQuery(panelQueryFLRLand);
		}

		else if (eI.getActionCommand().equals(MY_OWN_NEW_SERIENMAIL)) {

			if (isBetreffEmptyDlg()) {
				// nothing here
			} else if (isEMailAbsenderEmptyDlg()) {
				// nothing here
			} else if (sendAnEmptyTextDlg()) {
				// text kann leer sein; nur info

				boolean bMailtext = false;
				if (getSerienbriefDto().isPlain() && getSerienbriefDto().getXMailtext() == null) {
					bMailtext = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getInstance().getTextRespectUISPr("part.serienbrief.keinmailtext"));
				} else {
					bMailtext = true;
				}

				if (bMailtext == true) {

					MaileSerienbriefResponse response = DelegateFactory.getInstance().getKundeDelegate()
							.maileSerienbrief(getSerienbriefDto().getIId(), getEMailAbsender());
					DialogFactory.showModalDialog("", Texts.msgMaileSerienbrief(response));

				}
			}
		}

		else if (eI.getActionCommand().equals(MY_OWN_NEW_SERIENFAX)) {

			if (isBetreffEmptyDlg()) {
				// nothing here
			} else if (sendAnEmptyTextDlg()) {

				Integer iAnz = DelegateFactory.getInstance().getKundeDelegate()
						.faxeSerienbrief(getSerienbriefDto().getIId(), null);
				if (iAnz != null && iAnz > 0) {
					DialogFactory.showModalDialog("",
							iAnz + " " + LPMain.getInstance().getTextRespectUISPr("kund.sent.fax"));
				}

			}
		} else if (eI.getActionCommand().equals(MY_OWN_NEW_WIEDERVORLAGE)) {

			DialogWiedervorlageAusSerienbrief d = new DialogWiedervorlageAusSerienbrief(getInternalFrame(),
					getSerienbriefDto().getIId());
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);

		} else if (eI.getActionCommand().equals(MY_OWN_NEW_SERIENPRINT)) {
			if (isBetreffEmptyDlg()) {
				// nothing here
			} else if (sendAnEmptyTextDlg()) {
				String sTitel = getSerienbriefDto().getCBez();

				getInternalFrame().showReportKriterien(
						new ReportSerienbrief(getInternalFrame(), getSerienbriefDto().getIId(), sTitel), null, null,
						true, false);
			}
		} else if (eI.getActionCommand().equals(MY_OWN_NEW_RAWHTML_IMPORT)) {
			int modifiers = eI.getModifiers();
			handleClipboard((eI.getModifiers() & ActionEvent.CTRL_MASK) > 0);
		}
	}

	private int htmlRawStartIndex(String s) {
		int index = s.indexOf("<!DOCTYPE");
		if (index != 0)
			return index;

		index = s.indexOf("<html");
		if (index != 0)
			return index;

		index = s.indexOf("<HTML");
		if (index != 0) {
			// s.replaceFirst("<HTML", "<html");
			return index;
		}

		return -1;
	}

	private void handleClipboard(boolean ctrlPressed) throws UnsupportedFlavorException, IOException {
		Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable transferData = systemClipboard.getContents(null);
		if (transferData != null && transferData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			String s = (String) transferData.getTransferData(DataFlavor.stringFlavor);
			myLogger.info("clipboard-content:<" + s + ">.");

			int htmlStart = htmlRawStartIndex(s);
			if (htmlStart > -1) {
				if (htmlStart > 0) {
					s = s.substring(htmlStart);
				}
				int count = Math.min(100, s.length());
				String htmlSample = s.substring(0, count - 1) + " (" + s.length() + "): ";

				boolean replace = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getMessageTextRespectUISPr("part.serienbrief_ersetzen", htmlSample));
				if (replace) {
					whtmlText.setText(s);
				}
			} else {
				int count = Math.min(100, s.length());
				String htmlSample = s.substring(0, count - 1) + " (" + s.length() + "): ";
				boolean replace = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getMessageTextRespectUISPr("part.serienbrief_textersetzen", htmlSample));
				if (replace) {
					whtmlText.setText(s);
					whtmlText.startEditing();
				}
			}
		}
	}

	private boolean isBetreffEmptyDlg() {

		boolean bRet = false;

		if (getSerienbriefDto().getSBetreff() == null || getSerienbriefDto().getSBetreff().equals("")) {
			DialogFactory.showModalDialog("", LPMain.getInstance().getTextRespectUISPr("part.no_betreff"));
			bRet = true;
		}

		return bRet;
	}

	private boolean isEMailAbsenderEmptyDlg() throws Throwable {

		boolean bRet = false;

		if (getEMailAbsender() == null) {
			DialogFactory.showModalDialog("", LPMain.getInstance().getTextRespectUISPr("kund.no_absender"));
			bRet = true;
		}

		return bRet;
	}

	private boolean sendAnEmptyTextDlg() {

		boolean bRet = true;

		if (getSerienbriefDto().getSXText() == null || getSerienbriefDto().getSXText().equals("")) {
			bRet = (DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("part.no_text"), ""));
		}

		return bRet;
	}

	private String getEMailAbsender() throws Throwable {

		Integer iIdPersonal = LPMain.getInstance().getTheClient().getIDPersonal();
		PersonalDto p = DelegateFactory.getInstance().getPersonalDelegate().personalFindByPrimaryKey(iIdPersonal);

		return p.getCEmail();

	}

	protected void eventItemchanged(EventObject eI) throws ExceptionLP, Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRAnsprechpartnerfunktion) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				getSerienbriefDto().setAnsprechpartnerfunktionIId(iId);

				AnsprechpartnerfunktionDto ansprechpartnerfunktionDto = null;
				if (iId != null) {
					ansprechpartnerfunktionDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerfunktionFindByPrimaryKey(iId);
					wtfAnsprechpartnerfunktion
							.setText(ansprechpartnerfunktionDto != null ? ansprechpartnerfunktionDto.getCNr() : null);

					wcbMitzugeordnetenfirmen.setSelected(false);

				}
			} else if (e.getSource() == panelQueryFLRLand) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				getSerienbriefDto().setLandIId(iId);

				String sLand = null;
				if (iId != null) {
					LandDto l = null;
					l = DelegateFactory.getInstance().getSystemDelegate().landFindByPrimaryKey(iId);
					sLand = l.getCLkz();
				}
				wtfLKZ.setText(sLand);
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAnsprechpartnerfunktion) {
				getSerienbriefDto().setAnsprechpartnerfunktionIId(null);
				wtfAnsprechpartnerfunktion.setText("");
			} else if (e.getSource() == panelQueryFLRLand) {
				getSerienbriefDto().setLandIId(null);
				wtfLKZ.setText("");
			}
		}
	}

	private TabbedPaneSerienbrief getTabbedPaneSerienbrief() {
		return tpSerienbrief;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);
		boolean htmlModeRequired = isHtmlEnabled();
		switchTextPanels(htmlModeRequired);
		if (!bNeedNoNewI) {
			getTabbedPaneSerienbrief().setSerienbriefDto(new SerienbriefDto());
			getSerienbriefDto().setBHtml(Helper.boolean2Short(htmlModeRequired));
			setDefaults();
		}

	}

	private void setDefaults() throws Throwable {
		getHmOfButtons().get(MY_OWN_NEW_RAWHTML_IMPORT).getButton().setVisible(false);
		SerienbriefDto serienbriefDto = getSerienbriefDto();
		serienbriefDto.setBAnsprechpartnerfunktionAuchOhne(Helper.boolean2Short(false));
		serienbriefDto.setBGehtAnInteressenten(Helper.boolean2Short(false));
		serienbriefDto.setBGehtAnKunden(Helper.boolean2Short(true));
		serienbriefDto.setBGehtAnInteressenten(Helper.boolean2Short(false));
		serienbriefDto.setBVersteckteDabei(Helper.boolean2Short(false));

		wcoLocale.setKeyOfSelectedItem(LPMain.getTheClient().getLocMandantAsString());

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getSerienbriefDto().getIId();

		if (key == null) {
			leereAlleFelder(this);
			clearStatusbar();
			switchTextPanels(false);
		} else {
			// Update.
			getTabbedPaneSerienbrief().setSerienbriefDto(DelegateFactory.getInstance().getPartnerServicesDelegate()
					.serienbriefFindByPrimaryKey((Integer) key));
			dto2Components();

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getTabbedPaneSerienbrief().getSerienbriefDto().getCBez());

			setHmButtonVisible(MY_OWN_NEW_SERIENFAX, !isHtmlEnabled());
			setHmButtonVisible(MY_OWN_NEW_SERIENPRINT, !isHtmlEnabled());

			switchTextPanels(isHtmlEnabled());
		}
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI) throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);
	}

	private void dto2Components() throws ExceptionLP, Throwable {
		SerienbriefDto serienbriefDto = getSerienbriefDto();
		wcbAnsprechpartnerfunktionAuchOhne.setShort(serienbriefDto.getBAnsprechpartnerfunktionAuchOhne());
		wcbKD.setShort(serienbriefDto.getBGehtAnKunden());
		wcbInteressent.setShort(serienbriefDto.getBGehtAnInteressenten());
		wcbPartner.setShort(serienbriefDto.getBGehtanpartner());
		wcbMitzugeordnetenfirmen.setShort(serienbriefDto.getBMitzugeordnetenfirmen());

		if (Helper.short2boolean(serienbriefDto.getBSelektionenLogischesOder())) {
			wrbLogischesODER.setSelected(true);
		} else {
			wrbLogischesUND.setSelected(true);
		}

		wcbLF.setShort(serienbriefDto.getBGehtanlieferanten());
		wcbMoeglicheLF.setShort(serienbriefDto.getBGehtanmoeglichelieferanten());

		wtfBetreff.setText(serienbriefDto.getSBetreff());

		if (isHtmlEnabled()) {
			whtmlText.setText(serienbriefDto.getSXText());
		} else {
			wefText.setText(serienbriefDto.getSXText());
		}

		String locKommunikationAsString = serienbriefDto.getLocaleCNr();
		wcoLocale.setKeyOfSelectedItem(locKommunikationAsString);

		// wefText.setText(serienbriefDto.getSXText());

		wsfBranche.setKey(serienbriefDto.getBrancheIId());
		wsfPartnerklasse.setKey(serienbriefDto.getPartnerklasseIId());

		wcbVersteckte.setShort(serienbriefDto.getBVersteckteDabei());

		wcbWennKeinAnspMitFktDannErster.setShort(serienbriefDto.getBWennkeinanspmitfktDannersteransp());

		wtfBezeichnung.setText(serienbriefDto.getCBez());
		wtfPLZ.setText(serienbriefDto.getCPlz());

		Integer iIdLand = serienbriefDto.getLandIId();
		String sLand = null;
		if (iIdLand != null) {
			LandDto l = null;
			l = DelegateFactory.getInstance().getSystemDelegate().landFindByPrimaryKey(iIdLand);
			sLand = l.getCLkz();
		}
		wtfLKZ.setText(sLand);

		AnsprechpartnerfunktionDto ansprechpartnerfunktionDto = null;
		if (serienbriefDto.getAnsprechpartnerfunktionIId() != null) {
			ansprechpartnerfunktionDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerfunktionFindByPrimaryKey(serienbriefDto.getAnsprechpartnerfunktionIId());
		}
		wtfAnsprechpartnerfunktion
				.setText(ansprechpartnerfunktionDto != null ? ansprechpartnerfunktionDto.getCNr() : null);

		wnfAbUmsatz.setBigDecimal(serienbriefDto.getNAbumsatz());
		wnfBisUmsatz.setBigDecimal(serienbriefDto.getNBisumsatz());

		wdfZeitraumVon.setTimestamp(serienbriefDto.getTUmsatzab());
		wdfZeitraumBis.setTimestamp(serienbriefDto.getTUmsatzbis());
		wcbNewsletter.setSelected(serienbriefDto.isNewsletter());
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		setHmButtonVisible(MY_OWN_NEW_RAWHTML_IMPORT, false);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {

		super.eventActionUpdate(aE, bNeedNoUpdateI);
		beEditMode(isHtmlEnabled());
		setHmButtonVisible(MY_OWN_NEW_RAWHTML_IMPORT, isHtmlEnabled());
		// setHmButtonEnabled(MY_OWN_NEW_RAWHTML_IMPORT, isHtmlEnabled());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			checkLockedDlg();

			if (wnfAbUmsatz.getBigDecimal() != null && wnfBisUmsatz.getBigDecimal() != null) {
				if (wnfAbUmsatz.getBigDecimal().doubleValue() > wnfBisUmsatz.getBigDecimal().doubleValue()) {
					DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr("part.serienbrief.error.umsatz"));

					return;
				}
			}

			if (wdfZeitraumVon.getTimestamp() != null && wdfZeitraumBis.getTimestamp() != null) {
				if (wdfZeitraumVon.getTimestamp().after(wdfZeitraumBis.getTimestamp())) {
					DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr("part.serienbrief.error.zeitraum"));

					return;
				}
			}

			components2Dto();
			if (getSerienbriefDto().getIId() == null) {
				// Create.
				Integer iId = DelegateFactory.getInstance().getPartnerServicesDelegate()
						.createSerienbrief(getSerienbriefDto());
				setKeyWhenDetailPanel(iId);
				getTabbedPaneSerienbrief().getSerienbriefDto().setIId(iId);
			} else {
				// Update.
				DelegateFactory.getInstance().getPartnerServicesDelegate().updateSerienbrief(getSerienbriefDto());
			}
			super.eventActionSave(e, false);
			eventYouAreSelected(false);

			setHmButtonVisible(MY_OWN_NEW_RAWHTML_IMPORT, false);
			// setHmButtonEnabled(MY_OWN_NEW_RAWHTML_IMPORT, false);
		}
	}

	/**
	 * components2Dto
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {
		SerienbriefDto serienbriefDto = getTabbedPaneSerienbrief().getSerienbriefDto();
		serienbriefDto.setBAnsprechpartnerfunktionAuchOhne(wcbAnsprechpartnerfunktionAuchOhne.getShort());
		serienbriefDto.setBGehtAnInteressenten(wcbAnsprechpartnerfunktionAuchOhne.getShort());
		serienbriefDto.setBGehtAnKunden(wcbKD.getShort());
		serienbriefDto.setBGehtAnInteressenten(wcbInteressent.getShort());
		serienbriefDto.setBGehtanlieferanten(wcbLF.getShort());
		serienbriefDto.setBGehtanpartner(wcbPartner.getShort());

		serienbriefDto.setPartnerklasseIId(wsfPartnerklasse.getIKey());
		serienbriefDto.setBrancheIId(wsfBranche.getIKey());

		serienbriefDto.setLocaleCNr((String) wcoLocale.getKeyOfSelectedItem());

		serienbriefDto.setBMitzugeordnetenfirmen(wcbMitzugeordnetenfirmen.getShort());

		serienbriefDto.setBGehtanmoeglichelieferanten(wcbMoeglicheLF.getShort());

		serienbriefDto.setSBetreff(wtfBetreff.getText());

		if (Helper.short2boolean(getSerienbriefDto().getBHtml())) {
			String theText = whtmlText.getText();
			serienbriefDto.setSXText(theText);
		} else {
			serienbriefDto.setSXText(wefText.getText());
		}

		// serienbriefDto.setSXText(wefText.getText());

		serienbriefDto.setBVersteckteDabei(wcbVersteckte.getShort());
		serienbriefDto.setCBez(wtfBezeichnung.getText());
		serienbriefDto.setCPlz(wtfPLZ.getText());
		serienbriefDto.setMandantCNr(LPMain.getInstance().getTheClient().getMandant());

		if (wnfAbUmsatz.getBigDecimal() == null && wnfBisUmsatz.getBigDecimal() == null) {
			wdfZeitraumVon.setTimestamp(null);
			wdfZeitraumBis.setTimestamp(null);
		}

		serienbriefDto.setNAbumsatz(wnfAbUmsatz.getBigDecimal());
		serienbriefDto.setNBisumsatz(wnfBisUmsatz.getBigDecimal());

		serienbriefDto.setTUmsatzab(Helper.cutTimestamp(wdfZeitraumVon.getTimestamp()));
		serienbriefDto.setTUmsatzbis(Helper.cutTimestamp(wdfZeitraumBis.getTimestamp()));
		serienbriefDto.setNewsletter(wcbNewsletter.isSelected());
		serienbriefDto.setBSelektionenLogischesOder(wrbLogischesODER.getShort());
		serienbriefDto.setBWennkeinanspmitfktDannersteransp(wcbWennKeinAnspMitFktDannErster.getShort());
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (!bNeedNoDeleteI) {
			if (!isLockedDlg()) {
				DelegateFactory.getInstance().getPartnerServicesDelegate()
						.removeSerienbrief(getSerienbriefDto().getIId());
				getTabbedPaneSerienbrief().setSerienbriefDto(new SerienbriefDto());
			}
		}
		super.eventActionDelete(e, false, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}
}
