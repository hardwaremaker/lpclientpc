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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;

@SuppressWarnings("static-access")
public class DialogRechnungsadresseBestellnummerProjekt extends JDialog implements ActionListener, ItemChangedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private InternalFrame intFrame = null;

	private WrapperLabel jLabelProjekt = null;
	private WrapperLabel jLabelBestellnummer = null;
	private WrapperTextField wtfBestellnummer = null;
	private WrapperTextField wtfProjekt = null;
	private WrapperTextField wtfVertreter = null;
	private WrapperButton jButtonVertreter = null;
	private PanelQueryFLR panelQueryFLRVertreter = null;

	private WrapperLabel wlaLaufBis = null;
	private WrapperDateField wdfLaufBis = null;

	protected WrapperSelectField wsfVerrechnungsmodell = null;

	private static final String ACTION_SPECIAL_ANSPRECHPARTNER_RECHNUNGSADRESSE = "action_special_ansprechpartner_rechungsadresse";

	private static final String ACTION_SPECIAL_RECHNUNGSADRESSE_AUFTRAG = "action_special_rechnungsadresse_auftrag";
	private static final String ACTION_SPECIAL_VERTRETER_KUNDE = "action_special_vertreter_kunde";

	private WrapperButton jButtonRechnungsadresse = null;
	private PanelQueryFLR panelQueryFLRRechnungsadresse = null;
	private WrapperTextField wtfKundeRechnungsadresse = null;

	private WrapperButton wbuAnsprechpartnerRechungsadresse = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner_Rechungsadresse = null;
	private WrapperTextField wtfAnsprechpartnerRechungsadresse = null;

	private WrapperSelectField wsfProjekt = null;

	private AuftragDto auftragDto = null;

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();

	public DialogRechnungsadresseBestellnummerProjekt(InternalFrame intFrame, AuftragDto auftragDto) {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.intFrame = intFrame;
		this.auftragDto = auftragDto;
		try {
			jbInit();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		this.setSize(700, 300);

		setTitle(LPMain.getInstance().getTextRespectUISPr("auft.menu.bearbeiten.rechnungsadressebestellnummerprojekt"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		this.addEscapeListener(this);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK), "test");
		getRootPane().getActionMap().put("test", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					saveAndExit();
				} catch (Throwable e1) {
					e1.printStackTrace();
				}

			}
		});

	}

	public static void addEscapeListener(final JDialog dialog) {
		ActionListener escListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		};

		dialog.getRootPane().registerKeyboardAction(escListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

	}

	public void changed(EventObject eI) {
		try {
			ItemChangedEvent e = (ItemChangedEvent) eI;
			if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
				if (e.getSource() == panelQueryFLRRechnungsadresse) {

					Integer pkKunde = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
					KundeDto kundeRechnungsadresseDto = DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByPrimaryKey(pkKunde);

					DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(pkKunde, LocaleFac.BELEGART_AUFTRAG,
							intFrame);

					auftragDto.setKundeIIdRechnungsadresse(kundeRechnungsadresseDto.getIId());

					String sAdresse = kundeRechnungsadresseDto.getPartnerDto().formatTitelAnrede();
					if (kundeRechnungsadresseDto.getPartnerDto().getCKbez() != null)
						;
					sAdresse = sAdresse + "  /  " + kundeRechnungsadresseDto.getPartnerDto().getCKbez();
					wtfKundeRechnungsadresse.setText(sAdresse);

					auftragDto.setAnsprechpartnerIIdRechnungsadresse(null);
					wtfAnsprechpartnerRechungsadresse.setText(null);

				} else if (e.getSource() == panelQueryFLRAnsprechpartner_Rechungsadresse) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();

					AnsprechpartnerDto ansprechpartnerDtoRechnungsadresse = DelegateFactory.getInstance()
							.getAnsprechpartnerDelegate().ansprechpartnerFindByPrimaryKey((Integer) key);

					auftragDto.setAnsprechpartnerIIdRechnungsadresse(ansprechpartnerDtoRechnungsadresse.getIId());

					wtfAnsprechpartnerRechungsadresse
							.setText(ansprechpartnerDtoRechnungsadresse.getPartnerDto().formatTitelAnrede());
				} else if (e.getSource() == panelQueryFLRVertreter) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();

					PersonalDto vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);

					wtfVertreter.setText(vertreterDto.getPartnerDto().formatTitelAnrede());

					auftragDto.setPersonalIIdVertreter(vertreterDto.getIId());

				}

			}
		} catch (Throwable e1) {
			intFrame.handleException(e1, false);
		}
	}

	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);

		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr("lp.report.save"));
		wbuAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr("Cancel"));

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		jButtonRechnungsadresse = new WrapperButton();
		jButtonRechnungsadresse.setText(LPMain.getTextRespectUISPr("button.rechnungsadresse"));
		jButtonRechnungsadresse.setActionCommand(ACTION_SPECIAL_RECHNUNGSADRESSE_AUFTRAG);
		jButtonRechnungsadresse.addActionListener(this);

		wbuAnsprechpartnerRechungsadresse = new WrapperButton();
		wbuAnsprechpartnerRechungsadresse.setText(LPMain.getTextRespectUISPr("button.ansprechpartner"));
		wbuAnsprechpartnerRechungsadresse.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_RECHNUNGSADRESSE);
		wbuAnsprechpartnerRechungsadresse.addActionListener(this);

		wtfKundeRechnungsadresse = new WrapperTextField();
		wtfKundeRechnungsadresse.setMandatoryField(true);
		wtfKundeRechnungsadresse.setActivatable(false);
		wtfKundeRechnungsadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfAnsprechpartnerRechungsadresse = new WrapperTextField();
		wtfAnsprechpartnerRechungsadresse.setActivatable(false);
		wtfAnsprechpartnerRechungsadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wlaLaufBis = new WrapperLabel(LPMain.getTextRespectUISPr("auft.bearbeiten.laufterminbis"));
		wdfLaufBis = new WrapperDateField();

		wtfVertreter = new WrapperTextField();
		wtfVertreter.setMandatoryField(true);
		wtfVertreter.setActivatable(false);
		wtfVertreter.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		jButtonVertreter = new WrapperButton();
		jButtonVertreter.setText(LPMain.getTextRespectUISPr("button.vertreter"));
		jButtonVertreter.setActionCommand(ACTION_SPECIAL_VERTRETER_KUNDE);
		jButtonVertreter.addActionListener(this);

		intFrame.addItemChangedListener(this);

		jLabelProjekt = new WrapperLabel();
		jLabelProjekt.setText(LPMain.getTextRespectUISPr("label.projekt"));

		jLabelBestellnummer = new WrapperLabel();
		jLabelBestellnummer.setText(LPMain.getTextRespectUISPr("label.bestellnummer"));

		wtfBestellnummer = new WrapperTextField(
		/* AuftragFac.MAX_AUFT_AUFTRAG_BESTELLNUMMER */);

		wtfProjekt = new WrapperTextField(AuftragFac.MAX_AUFT_AUFTRAG_PROJEKTBEZEICHNUNG);

		int iZeile = 0;

		panel1.add(jButtonRechnungsadresse, new GridBagConstraints(0, iZeile, 1, 1, .5, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wtfKundeRechnungsadresse, new GridBagConstraints(1, iZeile, 3, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		panel1.add(wbuAnsprechpartnerRechungsadresse, new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wtfAnsprechpartnerRechungsadresse, new GridBagConstraints(1, iZeile, 3, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(jButtonVertreter, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wtfVertreter, new GridBagConstraints(1, iZeile, 3, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		panel1.add(jLabelProjekt, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wtfProjekt, new GridBagConstraints(1, iZeile, 3, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {

			wsfProjekt = new WrapperSelectField(WrapperSelectField.PROJEKT, intFrame, true);

			ParametermandantDto parametermandantDto = DelegateFactory.getInstance().getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_PROJEKT_IST_PFLICHTFELD);
			boolean bProjektIstPflichtfeld = ((Boolean) parametermandantDto.getCWertAsObject());
			if (bProjektIstPflichtfeld) {
				wsfProjekt.setMandatoryField(true);
			}

			panel1.add(wsfProjekt.getWrapperGotoButton(), new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			panel1.add(wsfProjekt.getWrapperTextField(), new GridBagConstraints(1, iZeile, 3, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		}
		iZeile++;
		panel1.add(jLabelBestellnummer, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wtfBestellnummer, new GridBagConstraints(1, iZeile, 3, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ABRECHNUNGSVORSCHLAG)) {
			iZeile++;
			wsfVerrechnungsmodell = new WrapperSelectField(WrapperSelectField.VERRECHNUNGSMODELL, intFrame, false);
			wsfVerrechnungsmodell.setMandatoryField(true);
			panel1.add(wsfVerrechnungsmodell.getWrapperButton(), new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			panel1.add(wsfVerrechnungsmodell.getWrapperTextField(), new GridBagConstraints(1, iZeile, 3, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		if (auftragDto.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
			iZeile++;
			wsfVerrechnungsmodell = new WrapperSelectField(WrapperSelectField.VERRECHNUNGSMODELL, intFrame, false);
			wsfVerrechnungsmodell.setMandatoryField(true);
			panel1.add(wlaLaufBis, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			panel1.add(wdfLaufBis, new GridBagConstraints(1, iZeile, 3, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		iZeile++;
		panel1.add(wbuSpeichern, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));

		panel1.add(wbuAbbrechen, new GridBagConstraints(2, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));

		getContentPane().add(panel1);

		KundeDto kundeRechnungsadresseDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(auftragDto.getKundeIIdRechnungsadresse());

		String sAdresse = kundeRechnungsadresseDto.getPartnerDto().formatTitelAnrede();
		if (kundeRechnungsadresseDto.getPartnerDto().getCKbez() != null)
			;
		sAdresse = sAdresse + "  /  " + kundeRechnungsadresseDto.getPartnerDto().getCKbez();
		wtfKundeRechnungsadresse.setText(sAdresse);

		if (auftragDto.getAnsprechpartnerIIdRechnungsadresse() != null) {
			AnsprechpartnerDto ansprechpartnerDtoRechnungsadresse = DelegateFactory.getInstance()
					.getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(auftragDto.getAnsprechpartnerIIdRechnungsadresse());

			wtfAnsprechpartnerRechungsadresse
					.setText(ansprechpartnerDtoRechnungsadresse.getPartnerDto().formatTitelAnrede());
		}

		if (auftragDto.getPersonalIIdVertreter() != null) {
			PersonalDto vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(auftragDto.getPersonalIIdVertreter());

			wtfVertreter.setText(vertreterDto.getPartnerDto().formatTitelAnrede());

		}

		if (wsfProjekt != null) {
			// SP2202 Wenn Auftrag aus Angebot, dann Projekt hier nicht mehr
			// auswaehlbar
			if (auftragDto.getAngebotIId() != null) {
				wsfProjekt.getWrapperGotoButton().setEnabled(false);

			}

			if (auftragDto.getAngebotIId() != null) {
				AngebotDto agDto = DelegateFactory.getInstance().getAngebotDelegate()
						.angebotFindByPrimaryKey(auftragDto.getAngebotIId());
				wsfProjekt.setKey(agDto.getProjektIId());
			} else {
				wsfProjekt.setKey(auftragDto.getProjektIId());
			}

		}

		wtfProjekt.setText(auftragDto.getCBezProjektbezeichnung());
		wtfBestellnummer.setText(auftragDto.getCBestellnummer());

		wdfLaufBis.setTimestamp(auftragDto.getTLaufterminBis());

		// Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				/*
				 * Instead of directly closing the window, we're going to change the
				 * JOptionPane's value property.
				 */
				setVisible(false);
			}
		});

	}

	public void clearAndHide() {
		setVisible(false);
	}

	private void dialogQueryAnsprechpartnerRechnungsadresse(ActionEvent e) throws Throwable {
		if (auftragDto.getKundeIIdRechnungsadresse() == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.kundenichtgewaehlt"));
		} else {

			KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(auftragDto.getKundeIIdRechnungsadresse());
			panelQueryFLRAnsprechpartner_Rechungsadresse = PartnerFilterFactory.getInstance()
					.createPanelFLRAnsprechpartner(intFrame, kdDto.getPartnerIId(),
							auftragDto.getAnsprechpartnerIIdRechnungsadresse(), true, true);

			new DialogQuery(panelQueryFLRAnsprechpartner_Rechungsadresse);
		}
	}

	private void dialogQueryRechnungsadresse(ActionEvent e) throws Throwable {
		panelQueryFLRRechnungsadresse = PartnerFilterFactory.getInstance().createPanelFLRKunde(intFrame, true, false,
				auftragDto.getKundeIIdRechnungsadresse());

		new DialogQuery(panelQueryFLRRechnungsadresse);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals(ACTION_SPECIAL_RECHNUNGSADRESSE_AUFTRAG)) {
				dialogQueryRechnungsadresse(e);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER_RECHNUNGSADRESSE)) {
				dialogQueryAnsprechpartnerRechnungsadresse(e);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER_KUNDE)) {
				panelQueryFLRVertreter = PersonalFilterFactory.getInstance().createPanelFLRPersonal(intFrame, true,
						false, auftragDto.getPersonalIIdVertreter());

				new DialogQuery(panelQueryFLRVertreter);
			}

			else if (e.getSource().equals(wbuSpeichern)) {
				saveAndExit();
			} else if (e.getSource().equals(wbuAbbrechen)) {
				this.setVisible(false);
			}

		} catch (Throwable e1) {
			intFrame.handleException(e1, false);
		}

	}

	public void saveAndExit() throws Throwable {
		auftragDto.setCBezProjektbezeichnung(wtfProjekt.getText());
		auftragDto.setCBestellnummer(wtfBestellnummer.getText());
		
		auftragDto.setTLaufterminBis(wdfLaufBis.getTimestamp());

		if (wsfProjekt != null) {
			auftragDto.setProjektIId(wsfProjekt.getIKey());
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ABRECHNUNGSVORSCHLAG)) {
			if (wsfVerrechnungsmodell != null) {
				auftragDto.setVerrechnungsmodellIId(wsfVerrechnungsmodell.getIKey());
			}
		}

		DelegateFactory.getInstance().getAuftragDelegate().aendereRechnungsadresseProjeknummerBestellnummer(auftragDto);
		this.setVisible(false);
	}
}
