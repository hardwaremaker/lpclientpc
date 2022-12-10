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
package com.lp.client.frame.report;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lp.client.bestellung.ReportAbholauftrag;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.IEditableTextEditor;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelHtmlTexteingabe;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelTexteingabe;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.lieferschein.ReportLieferschein;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.ReportUrlaubsantrag;
import com.lp.client.rechnung.IOpenTransBeleg;
import com.lp.client.util.ExcFactory;
import com.lp.client.util.IconFactory;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.system.service.VersandFac;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.util.Helper;

import net.miginfocom.swing.MigLayout;

/*
 * <p><I>Diese Klasse kuemmert sich ...</I> </p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>dd.mm.05</I></p>
 * 
 * <p> </p>
 * 
 * @author not attributable
 * 
 * @version $Revision: 1.18 $
 */
public class PanelVersandEmail extends PanelVersand {
	private static final long serialVersionUID = 3798593491477582229L;

	private WrapperLabel wlaCCEmpfaenger = null;
	private WrapperTextField wtfCCEmpfaenger = null;
	private WrapperLabel wlaAbsender = null;
	private WrapperTextField wtfAbsender = null;
	public WrapperTextField getWtfAbsender() {
		return wtfAbsender;
	}

	private WrapperLabel wlaText = null;
	// private WrapperEditorField wefText = null;
	private MailtextDto mailtextDto = null;
	private WrapperCheckBox wcbEmpfangsbestaetigung = null;
	private WrapperCheckBox wcbDokumenteAnhaengen = null;
	private PanelReportIfJRDS jpaPanelReportIf = null;
	private WrapperButton wbuEmpfaengerCC = null;
	private WrapperButton wbuEmpfaengerAnspCC = null;
	private JTextField jtfAnhaenge = null;
	private WrapperButton wbuAnhangWaehlen = null;
	private WrapperButton wbuAnhangLoeschen = null;
	private WrapperLabel wlaAnhaenge = null;
	private PanelReportKriterien panelReportKriterien = null;

	public static final String ACTION_SPECIAL_ATTACHMENT = "ACTION_SPECIAL_ATTACHMENT";
	public static final String ACTION_SPECIAL_REMOVE_ATTACHMENT = "ACTION_SPECIAL_REMOVE_ATTACHMENT";
	protected static final String ACTION_SPECIAL_PARTNERCC = "action_special_partnercc";
	protected static final String ACTION_SPECIAL_ANSPRECHPARTNERCC = "action_special_ansprechpartnercc";
	public static final String ACTION_SPECIAL_SENDEN_OHNE_KOPIEN = "action_special_sendenohnekopien";
	public static final String ACTION_SPECIAL_SENDEN_ZUGFERD = "action_special_sendenzugferd";
	public static final String ACTION_SPECIAL_SENDEN_OPENTRANS = "action_special_opentrans";

	public final static String MY_OWN_NEW_EXTRA_ACTION_SPECIAL_PLUS = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_EXTRA_ACTION_SPECIAL_PLUS";

	protected PanelQueryFLR panelQueryFLRPartnerCC = null;
	protected PanelQueryFLR panelQueryFLRAnsprechpartnerCC = null;
	protected PartnerDto partnerDtoEmpfaengerCC = null;
	protected Integer ansprechpartnerIIdCC = null;
	public boolean bUebersteuerterEmpaengerVorschlagGesetzt = false;
	public boolean bUebersteuerterEmpaengerVorschlagCCGesetzt = false;

	private WrapperButton wbuSendenOhneKopien = null;
	private WrapperButton wbuSendenZugferd = null;
	private WrapperButton wbuSendenOpenTrans = null;

	private WrapperLabel wlaBccEmpfaenger = null;
	private WrapperTextField wtfBccEmpfaenger = null;
	private JPanel panelBcc;

	private PanelTexteingabe editor;
	private PanelHtmlTexteingabe editorHtml;
	private IEditableTextEditor activeEditor;
	private JComponent editorComponent;
	private int editorComponentRow;

	public PanelVersandEmail(InternalFrame internalFrame, MailtextDto mailtextDto, String belegartCNr, Integer belegIId,
			PanelReportIfJRDS jpaPanelReportIf, PanelReportKriterien panelReportKriterien,
			PartnerDto partnerDtoEmpfaenger) throws Throwable {
		super(internalFrame, belegartCNr, belegIId, partnerDtoEmpfaenger);
		this.mailtextDto = mailtextDto;
		this.jpaPanelReportIf = jpaPanelReportIf;
		this.panelReportKriterien = panelReportKriterien;
		jbInitPanel();
		setDefaultsPanel();
	}

	protected void setDefaultText() throws Throwable {
		String sDefaulttext = DelegateFactory.getInstance().getVersandDelegate()
				.getDefaultTextForBelegEmail(mailtextDto);
//		wefText.setText(sDefaulttext);
//		wefText.setDefaultText(sDefaulttext);
//		setupEditorProperties();
		switchEditor(sDefaulttext);
		activeEditor.setText(sDefaulttext);
		activeEditor.setDefaultText(sDefaulttext);
	}

	protected void setDefaultBetreff() throws Throwable {
		String betreff = null;
		if (partnerDtoEmpfaenger != null && partnerDtoEmpfaenger.getLocaleCNrKommunikation() != null) {
			betreff = DelegateFactory.getInstance().getVersandDelegate().getDefaultBetreffForBelegEmail(mailtextDto,
					getbelegartCNr(), getbelegIId(), partnerDtoEmpfaenger.getLocaleCNrKommunikation());
		} else {
			betreff = DelegateFactory.getInstance().getVersandDelegate().getDefaultBetreffForBelegEmail(mailtextDto,
					getbelegartCNr(), getbelegIId());
		}

		wtfBetreff.setText(betreff);
	}

	protected void setUebersteuertenAbsender() throws Throwable {
		String absender = DelegateFactory.getInstance().getVersandDelegate().getUebersteuertenAbsender(mailtextDto);

		if (absender != null) {
			wtfAbsender.setText(absender);
		}
	}

	protected void setDefaultsPanel() throws Throwable {
		PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
				.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());

		if (personalDto.getCEmail() != null) {
			wtfAbsender.setText(personalDto.getCEmail());
		}

		// PJ19864
		setUebersteuertenAbsender();

		setDefaultText();
		setDefaultBetreff();
		// setupEditorProperties();

		// PJ18083
		if (jpaPanelReportIf.getReportname().equals(BestellungReportFac.REPORT_BESTELLUNG)) {

			boolean bEmpfangsbestaetigung = (Boolean) DelegateFactory
					.getInstance().getParameterDelegate().getMandantparameter(LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_DEFAULT_EMPFANGSBESTAETIGUNG)
					.getCWertAsObject();
			if (bEmpfangsbestaetigung) {
				wcbEmpfangsbestaetigung.setSelected(true);
			}
		}

		// PJ18083
		if (jpaPanelReportIf.getReportname().equals(RechnungReportFac.REPORT_RECHNUNG)) {

			boolean bEmpfangsbestaetigung = (Boolean) DelegateFactory
					.getInstance().getParameterDelegate().getMandantparameter(LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_DEFAULT_EMPFANGSBESTAETIGUNG)
					.getCWertAsObject();
			if (bEmpfangsbestaetigung) {
				wcbEmpfangsbestaetigung.setSelected(true);
			}
		}

		setBccEmpfaenger(personalDto, false);

		// PJ20471 zusaetzlich
		if (jpaPanelReportIf.getReportname().equals(RechnungReportFac.REPORT_RECHNUNG)) {

			String bccAempfaenger = (String) DelegateFactory.getInstance().getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_RECHNUNG,
							ParameterFac.PARAMETER_BCC_RECHNUNG)
					.getCWertAsObject();

			if (bccAempfaenger != null && bccAempfaenger.trim().length() > 0) {

				wlaBccEmpfaenger.setVisible(true);
				wtfBccEmpfaenger.setVisible(true);

				if (wtfBccEmpfaenger.getText() != null && wtfBccEmpfaenger.getText().trim().length() > 0) {
					wtfBccEmpfaenger.setText(wtfBccEmpfaenger.getText() + ";" + bccAempfaenger);

				} else {
					wtfBccEmpfaenger.setText(bccAempfaenger);
				}

			}

		}

		// PJ22380 zusaetzlich
		if (jpaPanelReportIf.getReportname().equals(RechnungReportFac.REPORT_GUTSCHRIFT)) {

			String bccAempfaenger = (String) DelegateFactory.getInstance().getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_RECHNUNG,
							ParameterFac.PARAMETER_BCC_GUTSCHRIFT)
					.getCWertAsObject();

			if (bccAempfaenger != null && bccAempfaenger.trim().length() > 0) {

				wlaBccEmpfaenger.setVisible(true);
				wtfBccEmpfaenger.setVisible(true);

				if (wtfBccEmpfaenger.getText() != null && wtfBccEmpfaenger.getText().trim().length() > 0) {
					wtfBccEmpfaenger.setText(wtfBccEmpfaenger.getText() + ";" + bccAempfaenger);

				} else {
					wtfBccEmpfaenger.setText(bccAempfaenger);
				}

			}

		}

	}

	private void setBccEmpfaenger(PersonalDto personalDto, boolean bAbsenderadresse) {

		if (bAbsenderadresse) {
			boolean hasBcc = !Helper.isStringEmpty(personalDto.getCEmail());
			wlaBccEmpfaenger.setVisible(hasBcc);
			wtfBccEmpfaenger.setVisible(hasBcc);
			if (hasBcc)
				wtfBccEmpfaenger.setText(personalDto.getCEmail());
		} else {
			boolean hasBcc = !Helper.isStringEmpty(personalDto.getCBccempfaenger());
			wlaBccEmpfaenger.setVisible(hasBcc);
			wtfBccEmpfaenger.setVisible(hasBcc);
			if (hasBcc)
				wtfBccEmpfaenger.setText(personalDto.getCBccempfaenger());
		}

	}

//	private void setupEditorProperties() {
//		LpEditor wefEditor = wefText.getLpEditor();
//		wefEditor.disableStyledText();
//
//		wefEditor.showMenu(false);
//		wefEditor.showTableItems(false);
//		wefEditor.showTabRuler(false);
//	}

	public WrapperCheckBox getWcbDokumenteAnhaengen() {
		return wcbDokumenteAnhaengen;
	}

	public MailtextDto getMailtextDto() {
		if (mailtextDto == null) {
			mailtextDto = new MailtextDto();
		}
		return mailtextDto;
	}

	public void setMailtextDto(MailtextDto mailtextDto) {
		if (mailtextDto != null) {
			this.mailtextDto = mailtextDto;
		}
	}

	private void installWbuSendenOhneKopien() throws ExceptionLP {
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/com/lp/client/res/mail.png"));
		getWbuSenden().setIcon(imageIcon);

		if (!(jpaPanelReportIf instanceof ReportBeleg))
			return;

		wbuSendenOhneKopien = new WrapperButton(LPMain.getTextRespectUISPr("lp.sendenohnekopien"));
		wbuSendenOhneKopien.setActionCommand(ACTION_SPECIAL_SENDEN_OHNE_KOPIEN);
		wbuSendenOhneKopien.setIcon(imageIcon);

		((ReportBeleg) jpaPanelReportIf).addFocusListenerKopien(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					wbuSendenOhneKopien.setVisible(hasBelegKopien());
				} catch (ExceptionLP ex) {
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		JPanel p = getSendenActionPanel();
		MigLayout m = (MigLayout) p.getLayout();
		m.setLayoutConstraints("wrap 2, insets 0");
		p.remove(getWbuSenden());
		// p.setLayout(new MigLayout(
		// "wrap 2, insets 0", ""));
		p.add(getWbuSenden(), "w 50%");
		p.add(wbuSendenOhneKopien, "w 50%");
		if (!hasBelegKopien()) {
			wbuSendenOhneKopien.setVisible(false);
		}
	}

	private void installWbuSendenZugferd() throws Throwable {
		if (!LPMain.getInstance().getDesktop().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZUGFERD)
				|| !(jpaPanelReportIf instanceof IZugferdBeleg)) {
			return;
		}

		wbuSendenZugferd = new WrapperButton(LPMain.getTextRespectUISPr("rech.zugferd.mail.button"));
		wbuSendenZugferd.setActionCommand(ACTION_SPECIAL_SENDEN_ZUGFERD);
		wbuSendenZugferd.setIcon(IconFactory.getMail());

		IZugferdBeleg zugferdBeleg = (IZugferdBeleg) jpaPanelReportIf;
		boolean buttonEnabled = zugferdBeleg.isZugferdPartner();
		wbuSendenZugferd.setEnabled(buttonEnabled);

		JPanel p = getSendenActionPanel();
		MigLayout m = (MigLayout) p.getLayout();
		if (p.getComponents().length > 1) {
			m.setLayoutConstraints("wrap 3, insets 0");
			p.removeAll();
			p.add(getWbuSenden(), "w 33%");
			p.add(wbuSendenZugferd, "w 33%");
			p.add(wbuSendenOhneKopien, "w 33%");
		} else {
			m.setLayoutConstraints("wrap 2, insets 0");
			p.removeAll();
			p.add(getWbuSenden(), "w 50%");
			p.add(wbuSendenZugferd, "w 50%");
		}
	}

	private void installWbuSendenOpenTrans() throws Throwable {
		if (!LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_WEB_BAUTEIL_ANFRAGE)
				|| !(jpaPanelReportIf instanceof IOpenTransBeleg)) {
			return;
		}

		wbuSendenOpenTrans = new WrapperButton(LPMain.getTextRespectUISPr("bes.opentrans.mail.button"));

		IOpenTransBeleg otBeleg = (IOpenTransBeleg) jpaPanelReportIf;
		boolean buttonEnabled = otBeleg.isOpenTransPartner();
		if (!buttonEnabled) {
			return;
		}

		wbuSendenOpenTrans.setVisible(buttonEnabled);
		wbuSendenOpenTrans.setActionCommand(ACTION_SPECIAL_SENDEN_OPENTRANS);
		wbuSendenOpenTrans.setIcon(IconFactory.getMail());

		JPanel p = getSendenActionPanel();
		MigLayout m = (MigLayout) p.getLayout();
		if (p.getComponents().length > 1) {
			m.setLayoutConstraints("wrap 3, insets 0");
			p.removeAll();
			p.add(getWbuSenden(), "w 33%");
			p.add(wbuSendenOpenTrans, "w 33%");
			p.add(wbuSendenOhneKopien, "w 33%");
		} else {
			m.setLayoutConstraints("wrap 2, insets 0");
			p.removeAll();
			p.add(getWbuSenden(), "w 50%");
			p.add(wbuSendenOpenTrans, "w 50%");
		}
	}

	private boolean hasBelegKopien() throws ExceptionLP {
		Integer wnfKopien = ((ReportBeleg) jpaPanelReportIf).getKopien();
		return (wnfKopien != null && wnfKopien.intValue() > 0);
	}

	private void jbInitPanel() throws Throwable {
		wlaCCEmpfaenger = new WrapperLabel(LPMain.getTextRespectUISPr("label.cc"));
		wtfCCEmpfaenger = new WrapperTextField(VersandFac.MAX_CCEMPFAENGER);
		wlaAbsender = new WrapperLabel(LPMain.getTextRespectUISPr("label.absender"));
		wtfAbsender = new WrapperTextField(VersandFac.MAX_ABSENDER);
		wlaText = new WrapperLabel(LPMain.getTextRespectUISPr("label.text"));
//		wefText = new WrapperEditorField(getInternalFrame(),
//				LPMain.getTextRespectUISPr("lp.emailtext"));
		editorHtml = new PanelHtmlTexteingabe(getInternalFrame(), LPMain.getTextRespectUISPr("lp.emailtext"));
		editor = new PanelTexteingabe(getInternalFrame(), LPMain.getTextRespectUISPr("lp.emailtext"));
		editorComponent = editor;
		activeEditor = editor;

		wcbEmpfangsbestaetigung = new WrapperCheckBox(LPMain.getTextRespectUISPr("lp.empfangsbestaetigung"));

		wcbDokumenteAnhaengen = new WrapperCheckBox(LPMain.getTextRespectUISPr("lp.dokumenteanhaengen"));

		wbuEmpfaengerCC = new WrapperButton(LPMain.getTextRespectUISPr("lp.versand.partner"));
		wbuEmpfaengerCC.setActionCommand(ACTION_SPECIAL_PARTNERCC);
		wbuEmpfaengerCC.addActionListener(this);

		wbuEmpfaengerAnspCC = new WrapperButton(LPMain.getTextRespectUISPr("lp.versand.ansprechpartner"));
		wbuEmpfaengerAnspCC.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNERCC);
		wbuEmpfaengerAnspCC.addActionListener(this);

		wtfAbsender.setMandatoryField(true);
		wlaAnhaenge = new WrapperLabel(LPMain.getTextRespectUISPr("label.anhang"));
		jtfAnhaenge = new JTextField();
		jtfAnhaenge.setText("");
		jtfAnhaenge.setEditable(false);
		wbuAnhangWaehlen = new WrapperButton(LPMain.getTextRespectUISPr("label.hinzufuegen"));
		wbuAnhangWaehlen.setActionCommand(ACTION_SPECIAL_ATTACHMENT);
		wbuAnhangLoeschen = new WrapperButton("");
		wbuAnhangLoeschen.setActionCommand(ACTION_SPECIAL_REMOVE_ATTACHMENT);
		wbuAnhangLoeschen.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/leeren.png")));
		wbuAnhangLoeschen.setMinimumSize(
				new Dimension(Defaults.getInstance().getControlHeight(), Defaults.getInstance().getControlHeight()));
		wbuAnhangLoeschen.setPreferredSize(
				new Dimension(Defaults.getInstance().getControlHeight(), Defaults.getInstance().getControlHeight()));

		installWbuSendenOhneKopien();
		installWbuSendenZugferd();
		installWbuSendenOpenTrans();

		wlaBccEmpfaenger = new WrapperLabel(LPMain.getTextRespectUISPr("label.bcc"));
		wlaBccEmpfaenger.setVisible(false);
		wtfBccEmpfaenger = new WrapperTextField(VersandFac.MAX_BCCEMPFAENGER);
		wtfBccEmpfaenger.setEditable(false);
		wtfBccEmpfaenger.setVisible(false);
		initPanelBcc();

		getInternalFrame().addItemChangedListener(this);
		// ImageIcon imageIcon = new ImageIcon(getClass().getResource(
		// "/com/lp/client/res/mail.png"));
		// wbuSenden.setIcon(imageIcon);

		jpaWorkingOn.add(wbuEmpfaengerCC, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 70, 0));

		jpaWorkingOn.add(wbuEmpfaengerAnspCC, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 85, 2, 2), 70, 0));
		updateAnspCCButton();

		jpaWorkingOn.add(wlaCCEmpfaenger, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(2, 150, 2, 2), 30, 0));

		jpaWorkingOn.add(wtfCCEmpfaenger, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// jpaWorkingOn.add(wcbEmpfangsbestaetigung, new GridBagConstraints(2,
		// iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
		// GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(panelBcc, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 30, 0));
		iZeile++;
		jpaWorkingOn.add(wlaAbsender, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAbsender, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		if (getbelegartCNr() != null && getbelegartCNr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_DEFAULT_DOKUMENTE_ANHAENGEN,
							ParameterFac.KATEGORIE_ALLGEMEIN, LPMain.getTheClient().getMandant());

			if ((Boolean) parameter.getCWertAsObject()) {
				wcbDokumenteAnhaengen.setSelected(true);
			}

			jpaWorkingOn.add(wcbDokumenteAnhaengen, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}
		iZeile++;
		jpaWorkingOn.add(wlaBetreff, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBetreff, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		JPanel attachPanel = new JPanel(new GridBagLayout());
		attachPanel.add(wlaAnhaenge, new GridBagConstraints(0, 0, 2, 1, 2.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		attachPanel.add(jtfAnhaenge, new GridBagConstraints(2, 0, 2, 1, 5.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		attachPanel.add(wbuAnhangLoeschen, new GridBagConstraints(4, 0, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 0, 2, 0), 0, 0));
		attachPanel.add(wbuAnhangWaehlen, new GridBagConstraints(5, 0, 2, 1, 2.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(attachPanel, new GridBagConstraints(2, iZeile, 3, 1, 5.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaText, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		editorComponentRow = iZeile;
		addEditorToGrid();
		iZeile++;
	}

	private void initPanelBcc() {
		panelBcc = new JPanel();
		HvLayout layoutBcc = HvLayoutFactory.create(panelBcc, "insets 0", "[fill]push[fill|]", "");
		layoutBcc.add(wcbEmpfangsbestaetigung, "wmin 150").add(wlaBccEmpfaenger, "wmin 60, pushx").add(wtfBccEmpfaenger,
				"w 150:220:, pushx");
	}

	public VersandauftragDto getVersandauftragDto() throws Throwable {
		VersandauftragDto dto = super.getVersandauftragDto();
		dto.setCEmpfaenger(Helper.entferneWhitespaceVonEmailadresse(wtfEmpfaenger.getText()));
		dto.setCCcempfaenger(Helper.entferneWhitespaceVonEmailadresse(wtfCCEmpfaenger.getText()));
		dto.setCAbsenderadresse(wtfAbsender.getText());
//		dto.setCText(wefText.getPlainText());
		dto.setCText(activeEditor.getText());
		dto.setBEmpfangsbestaetigung(Helper.boolean2Short(wcbEmpfangsbestaetigung.isSelected()));
		dto.setCBccempfaenger(Helper.entferneWhitespaceVonEmailadresse(wtfBccEmpfaenger.getText()));

		validateEmailadresse(wlaEmpfaenger.getText(), wtfEmpfaenger.getText());
		validateEmailadresse(wlaAbsender.getText(), wtfAbsender.getText());
		
		if (!Helper.isStringEmpty(wtfCCEmpfaenger.getText()))
			validateEmailadresse(wlaCCEmpfaenger.getText(), wtfCCEmpfaenger.getText());
		if (!Helper.isStringEmpty(wtfBccEmpfaenger.getText()))
			validateEmailadresse(wlaBccEmpfaenger.getText(), wtfBccEmpfaenger.getText());

		
		
		return dto;
	}

	private void validateEmailadresse(String feldname, String mailadresse) throws ExceptionLP {
	
		if (!Helper.validateEmailadresse(mailadresse))
			throw ExcFactory.ungueltigeEmailadresse(mailadresse);
	}

	/**
	 * Vorschlagswert fuer den Empfaenger setzen.
	 * 
	 * @throws Throwable
	 */
	protected void setVorschlag() throws Throwable {
		if (partnerDtoEmpfaenger != null) {

			String report = jpaPanelReportIf.getReportname();

			if (panelReportKriterien.getPanelStandardDrucker().getVariante() != null) {
				ReportvarianteDto varDto = DelegateFactory.getInstance().getDruckerDelegate()
						.reportvarianteFindByPrimaryKey(panelReportKriterien.getPanelStandardDrucker().getVariante());
				report = varDto.getCReportnamevariante();
			}

			String ubersteuerterEmpfaenger = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.getUebersteuerteEmpfaenger(partnerDtoEmpfaenger, report, true);
			if (bUebersteuerterEmpaengerVorschlagGesetzt == false && ubersteuerterEmpfaenger != null
					&& ubersteuerterEmpfaenger.length() > 0) {
				setEmpfaenger(ubersteuerterEmpfaenger);
				bUebersteuerterEmpaengerVorschlagGesetzt = true;
			} else {

				String eMailAusSpediteur = null;

				if (jpaPanelReportIf instanceof ReportLieferschein) {
					LieferscheinDto lsDto = ((ReportLieferschein) jpaPanelReportIf).getLieferscheinDto();
					if (lsDto != null && lsDto.getSpediteurIId() != null) {
						eMailAusSpediteur = DelegateFactory.getInstance().getMandantDelegate()
								.spediteurFindByPrimaryKey(lsDto.getSpediteurIId()).getCEmail();
					}
				}

				else if (jpaPanelReportIf instanceof ReportAbholauftrag) {

					BestellungDto bestellungDto = DelegateFactory.getInstance().getBestellungDelegate()
							.bestellungFindByPrimaryKey(((ReportAbholauftrag) jpaPanelReportIf).getBestellungIId());

					if (bestellungDto != null && bestellungDto.getSpediteurIId() != null) {
						eMailAusSpediteur = DelegateFactory.getInstance().getMandantDelegate()
								.spediteurFindByPrimaryKey(bestellungDto.getSpediteurIId()).getCEmail();
					}
				} else if (jpaPanelReportIf instanceof ReportUrlaubsantrag) {

					PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey(((ReportUrlaubsantrag) jpaPanelReportIf).getPersonalIId());

					if (personalDto != null && personalDto.getCEmail() != null) {
						eMailAusSpediteur = personalDto.getCEmail();
					}
				}

				if (bUebersteuerterEmpaengerVorschlagGesetzt == false && eMailAusSpediteur != null) {
					setEmpfaenger(eMailAusSpediteur);
					bUebersteuerterEmpaengerVorschlagGesetzt = true;
				} else {
					String p = null;
					if (ansprechpartnerIId != null) {

						AnsprechpartnerDto anspDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
								.ansprechpartnerFindByPrimaryKey(ansprechpartnerIId);

						if (anspDto.getCEmail() != null && anspDto.getCEmail().length() > 0) {
							p = anspDto.getCEmail();
						}

					}

					if (p == null && partnerDtoEmpfaenger.getIId() != null) {
						p = DelegateFactory.getInstance().getPartnerDelegate()
								.partnerFindByPrimaryKey(partnerDtoEmpfaenger.getIId()).getCEmail();
					}

					if (p != null) {
						setEmpfaenger(p);
					}

				}
			}

			// PJ20471
			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_BCC_VERTRETER, ParameterFac.KATEGORIE_ALLGEMEIN,
							LPMain.getTheClient().getMandant());

			if ((Boolean) parameter.getCWertAsObject()) {

				if (getbelegartCNr() != null && (getbelegartCNr().equals(LocaleFac.BELEGART_ANGEBOT)
						|| getbelegartCNr().equals(LocaleFac.BELEGART_ANGEBOT)
						|| getbelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)
						|| getbelegartCNr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)
						|| getbelegartCNr().equals(LocaleFac.BELEGART_RECHNUNG)
						|| getbelegartCNr().equals(LocaleFac.BELEGART_GUTSCHRIFT))) {

					KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByiIdPartnercNrMandantOhneExc(partnerDtoEmpfaenger.getIId(),
									LPMain.getTheClient().getMandant());
					if (kdDto.getPersonaliIdProvisionsempfaenger() != null) {
						PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
								.personalFindByPrimaryKey(kdDto.getPersonaliIdProvisionsempfaenger());
						if (personalDto.getCEmail() != null) {

							setBccEmpfaenger(personalDto, true);

						}

					}

				}
			}

		}
	}

	protected void setVorschlagCC() throws Throwable {
		if (partnerDtoEmpfaengerCC != null) {

			String report = jpaPanelReportIf.getReportname();

			if (panelReportKriterien.getPanelStandardDrucker().getVariante() != null) {
				ReportvarianteDto varDto = DelegateFactory.getInstance().getDruckerDelegate()
						.reportvarianteFindByPrimaryKey(panelReportKriterien.getPanelStandardDrucker().getVariante());
				report = varDto.getCReportnamevariante();
			}

			String ubersteuerterEmpfaenger = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.getUebersteuerteEmpfaenger(partnerDtoEmpfaengerCC, report, true);

			if (bUebersteuerterEmpaengerVorschlagCCGesetzt == false && ubersteuerterEmpfaenger != null
					&& ubersteuerterEmpfaenger.length() > 0) {
				setEmpfaengerCC(ubersteuerterEmpfaenger);
				bUebersteuerterEmpaengerVorschlagCCGesetzt = true;
			} else {
				String p = null;
				if (ansprechpartnerIIdCC != null) {

					AnsprechpartnerDto anspDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPrimaryKey(ansprechpartnerIIdCC);

					if (anspDto.getCEmail() != null && anspDto.getCEmail().length() > 0) {
						p = anspDto.getCEmail();
					}

				}

				if (p == null) {
					p = DelegateFactory.getInstance().getPartnerDelegate()
							.partnerFindByPrimaryKey(partnerDtoEmpfaengerCC.getIId()).getCEmail();
				}

				if (p != null) {
					setEmpfaengerCC(p);
				}

			}

		}
	}

	public void setEmpfaengerCC(String sEmpfaenger) {
		wtfCCEmpfaenger.setText(sEmpfaenger);
	}

	public void setBetreff(String betreff) {
		wtfBetreff.setText(betreff);
	}

	public void setEditorFieldVisible(boolean bVisible) {
//		wefText.setVisible(bVisible);
		editorComponent.setVisible(bVisible);
		wlaText.setVisible(bVisible);
	}

	protected WrapperButton getwbuAnhangWaehlen() {
		return wbuAnhangWaehlen;
	}

	protected WrapperButton getwbuAnhangLoeschen() {
		return wbuAnhangLoeschen;
	}

	protected WrapperTextField getwtfCCEmpfaenger() {
		return wtfCCEmpfaenger;
	}

	public void setjtfAnhaengeText(String text) {
		jtfAnhaenge.setText(text);
	}

	public String getjtfAnhaengeText() {
		return jtfAnhaenge.getText();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(PanelVersand.ACTION_SPECIAL_PARTNER)) {
			try {
				panelQueryFLRPartner = PartnerFilterFactory.getInstance().createPanelFLRPartner(getInternalFrame());
				new DialogQuery(panelQueryFLRPartner);
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
		if (e.getActionCommand().equals(ACTION_SPECIAL_PARTNERCC)) {
			try {
				panelQueryFLRPartnerCC = PartnerFilterFactory.getInstance().createPanelFLRPartner(getInternalFrame());
				new DialogQuery(panelQueryFLRPartnerCC);
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals(PanelVersand.ACTION_SPECIAL_ANSPRECHPARTNER)) {
			try {
				if (partnerDtoEmpfaenger != null) {
					ansprechpartnerIId = null;

					panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance().createPanelFLRAnsprechpartner(
							getInternalFrame(), partnerDtoEmpfaenger.getIId(), null, false, true);
					panelQueryFLRAnsprechpartner.setMultipleRowSelectionEnabled(true);
					panelQueryFLRAnsprechpartner.addButtonAuswahlBestaetigen(null);

					panelQueryFLRAnsprechpartner.createAndSaveAndShowButton("/com/lp/client/res/plus_sign.png",
							LPMain.getTextRespectUISPr("lp.tooltip.emailergaenzen"),
							MY_OWN_NEW_EXTRA_ACTION_SPECIAL_PLUS, null);

					new DialogQuery(panelQueryFLRAnsprechpartner);

				}
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNERCC)) {
			try {
				if (partnerDtoEmpfaengerCC != null) {
					ansprechpartnerIIdCC = null;

					panelQueryFLRAnsprechpartnerCC = PartnerFilterFactory.getInstance().createPanelFLRAnsprechpartner(
							getInternalFrame(), partnerDtoEmpfaengerCC.getIId(), null, false, true);
					panelQueryFLRAnsprechpartnerCC.setMultipleRowSelectionEnabled(true);
					panelQueryFLRAnsprechpartnerCC.addButtonAuswahlBestaetigen(null);

					panelQueryFLRAnsprechpartnerCC.createAndSaveAndShowButton("/com/lp/client/res/plus_sign.png",
							LPMain.getTextRespectUISPr("lp.tooltip.emailergaenzen"),
							MY_OWN_NEW_EXTRA_ACTION_SPECIAL_PLUS, null);

					new DialogQuery(panelQueryFLRAnsprechpartnerCC);

				} else {
					if (partnerDtoEmpfaenger != null) {
						panelQueryFLRAnsprechpartnerCC = PartnerFilterFactory.getInstance()
								.createPanelFLRAnsprechpartner(getInternalFrame(), partnerDtoEmpfaenger.getIId(), null,
										false, true);
						panelQueryFLRAnsprechpartnerCC.setMultipleRowSelectionEnabled(true);
						panelQueryFLRAnsprechpartnerCC.addButtonAuswahlBestaetigen(null);

						new DialogQuery(panelQueryFLRAnsprechpartnerCC);
					}
				}

			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}

	}

	@Override
	public void changed(EventObject eI) {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		try {
			if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

				if (e.getSource() == panelQueryFLRPartner) {
					wtfEmpfaenger.setText(null);
					ansprechpartnerIId = null;
					Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

					partnerDtoEmpfaenger = DelegateFactory.getInstance().getPartnerDelegate()
							.partnerFindByPrimaryKey(key);

					AnsprechpartnerDto[] dtos = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPartnerIIdOhneExc(key);

					if (dtos != null && dtos.length > 0) {
						panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance()
								.createPanelFLRAnsprechpartner(getInternalFrame(), key, null, false, true);
						panelQueryFLRAnsprechpartner.setMultipleRowSelectionEnabled(true);
						panelQueryFLRAnsprechpartner.addButtonAuswahlBestaetigen(null);
						new DialogQuery(panelQueryFLRAnsprechpartner);

					} else {
						setVorschlag();
					}

					showEmpfaengerAnspButton(partnerDtoEmpfaenger != null);

				} else if (e.getSource() == panelQueryFLRPartnerCC) {
					wtfCCEmpfaenger.setText(null);
					ansprechpartnerIIdCC = null;
					Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

					partnerDtoEmpfaengerCC = DelegateFactory.getInstance().getPartnerDelegate()
							.partnerFindByPrimaryKey(key);

					AnsprechpartnerDto[] dtos = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPartnerIIdOhneExc(key);

					if (dtos != null && dtos.length > 0) {
						panelQueryFLRAnsprechpartnerCC = PartnerFilterFactory.getInstance()
								.createPanelFLRAnsprechpartner(getInternalFrame(), key, null, false, true);

						panelQueryFLRAnsprechpartnerCC.setMultipleRowSelectionEnabled(true);
						panelQueryFLRAnsprechpartnerCC.addButtonAuswahlBestaetigen(null);

						new DialogQuery(panelQueryFLRAnsprechpartnerCC);

					} else {
						setVorschlagCC();
					}

					updateAnspCCButton();

				} else if (e.getSource() == panelQueryFLRAnsprechpartner) {

					Object[] selctedIds = panelQueryFLRAnsprechpartner.getSelectedIds();

					wtfEmpfaenger.setText(null);
					Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
					ansprechpartnerIId = key;

					if (panelQueryFLRAnsprechpartner.getDialog() != null) {
						panelQueryFLRAnsprechpartner.getDialog().setVisible(false);
					}

					if (selctedIds.length > 1) {
						String s = "";
						for (int i = 0; i < selctedIds.length; i++) {
							AnsprechpartnerDto anspDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
									.ansprechpartnerFindByPrimaryKey((Integer) selctedIds[i]);

							if (anspDto.getCEmail() != null && anspDto.getCEmail().length() > 0) {
								s += anspDto.getCEmail() + ";";
							}
						}

						if (s.endsWith(";")) {
							s = s.substring(0, s.length() - 1);
						}

						setEmpfaenger(s);
					} else {
						setVorschlag();
					}

				} else if (e.getSource() == panelQueryFLRAnsprechpartnerCC) {
					wtfCCEmpfaenger.setText(null);
					Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
					ansprechpartnerIIdCC = key;

					partnerDtoEmpfaengerCC = DelegateFactory.getInstance().getPartnerDelegate()
							.partnerFindByPrimaryKey(DelegateFactory.getInstance().getAnsprechpartnerDelegate()
									.ansprechpartnerFindByPrimaryKey(ansprechpartnerIIdCC).getPartnerIId());

					if (panelQueryFLRAnsprechpartnerCC.getDialog() != null) {
						panelQueryFLRAnsprechpartnerCC.getDialog().setVisible(false);
					}

					Object[] selctedIds = panelQueryFLRAnsprechpartnerCC.getSelectedIds();

					if (selctedIds.length > 1) {
						String s = "";
						for (int i = 0; i < selctedIds.length; i++) {
							AnsprechpartnerDto anspDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
									.ansprechpartnerFindByPrimaryKey((Integer) selctedIds[i]);

							if (anspDto.getCEmail() != null && anspDto.getCEmail().length() > 0) {
								s += anspDto.getCEmail() + ";";
							}
						}

						if (s.endsWith(";")) {
							s = s.substring(0, s.length() - 1);
						}

						setEmpfaengerCC(s);
					} else {
						setVorschlagCC();
					}

				}

			}

			else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
				if (e.getSource() == panelQueryFLRAnsprechpartner) {
					setVorschlag();
				}
				if (e.getSource() == panelQueryFLRAnsprechpartnerCC) {
					setVorschlagCC();
				}
			} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {

				String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();

				if (sAspectInfo.equals(MY_OWN_NEW_EXTRA_ACTION_SPECIAL_PLUS)) {

					if (e.getSource() == panelQueryFLRAnsprechpartner) {

						Object[] selctedIds = panelQueryFLRAnsprechpartner.getSelectedIds();

						if (panelQueryFLRAnsprechpartner.getDialog() != null) {
							panelQueryFLRAnsprechpartner.getDialog().setVisible(false);
						}

						if (selctedIds.length > 0) {
							String s = "";
							if (wtfEmpfaenger.getText() != null) {
								s = wtfEmpfaenger.getText();
								if (!s.endsWith(";")) {
									s += ";";
								}
							}

							for (int i = 0; i < selctedIds.length; i++) {
								AnsprechpartnerDto anspDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
										.ansprechpartnerFindByPrimaryKey((Integer) selctedIds[i]);

								if (anspDto.getCEmail() != null && anspDto.getCEmail().length() > 0) {
									s += anspDto.getCEmail() + ";";
								}
							}

							if (s.endsWith(";")) {
								s = s.substring(0, s.length() - 1);
							}

							setEmpfaenger(s);
						}
					} else if (e.getSource() == panelQueryFLRAnsprechpartnerCC) {

						Object[] selctedIds = panelQueryFLRAnsprechpartnerCC.getSelectedIds();

						if (panelQueryFLRAnsprechpartnerCC.getDialog() != null) {
							panelQueryFLRAnsprechpartnerCC.getDialog().setVisible(false);
						}

						if (selctedIds.length > 0) {
							String s = "";
							if (wtfCCEmpfaenger.getText() != null) {
								s = wtfCCEmpfaenger.getText();
								if (!s.endsWith(";")) {
									s += ";";
								}
							}

							for (int i = 0; i < selctedIds.length; i++) {
								AnsprechpartnerDto anspDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
										.ansprechpartnerFindByPrimaryKey((Integer) selctedIds[i]);

								if (anspDto.getCEmail() != null && anspDto.getCEmail().length() > 0) {
									s += anspDto.getCEmail() + ";";
								}
							}

							if (s.endsWith(";")) {
								s = s.substring(0, s.length() - 1);
							}

							setEmpfaengerCC(s);
						}
					}
				}
			}
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
	}

	public void installActionListeners(ActionListener l) {
		getWbuSenden().addActionListener(l);
		getwbuAnhangWaehlen().addActionListener(l);
		getwbuAnhangLoeschen().addActionListener(l);

		if (wbuSendenOhneKopien != null) {
			wbuSendenOhneKopien.addActionListener(l);
		}
		if (wbuSendenZugferd != null) {
			wbuSendenZugferd.addActionListener(l);
		}
		if (wbuSendenOpenTrans != null) {
			wbuSendenOpenTrans.addActionListener(l);
		}
	}

	private void switchEditor(String text) throws Throwable {
		boolean isHtml = isHtml(text);
		if (editorComponent != null) {
			if (isHtml && editorComponent.equals(editorHtml))
				return;
			if (!isHtml && editorComponent.equals(editor))
				return;

			jpaWorkingOn.remove(editorComponent);
		}

		activeEditor = isHtml ? editorHtml : editor;
		editorComponent = isHtml ? editorHtml : editor;

		addEditorToGrid();
	}

	private void addEditorToGrid() {
		if (editorComponent == null)
			return;

		jpaWorkingOn.add(editorComponent, new GridBagConstraints(1, editorComponentRow, 4, 1, 0.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	private boolean isHtml(String text) {
		return text != null && (text.startsWith("<html") || text.startsWith("<HTML") || text.startsWith("<!DOCTYPE"));
	}

	protected void updateAnspCCButton() {
		boolean visible = partnerDtoEmpfaenger != null || partnerDtoEmpfaengerCC != null;
		wbuEmpfaengerAnspCC.setVisible(visible);
	}
}