package com.lp.client.partner;

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
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KontaktartDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.SerienbriefEmpfaengerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogWiedervorlageAusSerienbrief extends JDialog implements
		ActionListener, ItemChangedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private InternalFrame intFrame = null;

	private WrapperLabel wlaKontaktVon = null;
	private WrapperLabel wlaWiedervorlage = null;
	private WrapperTimestampField wtfWiedervorlage = null;
	private WrapperTimestampField wtfKontaktVon = null;
	private WrapperTextField wtfKontaktart = null;
	private WrapperButton wbuKontaktart = null;
	private PanelQueryFLR panelQueryFLRKontaktart = null;

	private WrapperSelectField wsfPersonalzugewiesener = null;

	private KontaktartDto kontaktartDto = null;

	static final public String ACTION_SPECIAL_FLR_KONTAKTART = "action_special_flr_kontaktart";

	private WrapperLabel wlaTitel = new WrapperLabel();
	private WrapperTextField wtfTitel = null;

	private Integer serienbriefIId = null;

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();

	public DialogWiedervorlageAusSerienbrief(InternalFrame intFrame,
			Integer serienbriefIId) {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.intFrame = intFrame;
		this.serienbriefIId = serienbriefIId;

		try {
			jbInit();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		this.setSize(500, 200);

		setTitle(LPMain.getInstance().getTextRespectUISPr(
				"part.serienbrief.wiedervorlage"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		this.addEscapeListener(this);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK),
				"test");
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

		dialog.getRootPane().registerKeyboardAction(escListener,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

	}

	public void changed(EventObject eI) {
		try {
			ItemChangedEvent e = (ItemChangedEvent) eI;
			if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
				if (e.getSource() == panelQueryFLRKontaktart) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();

					this.kontaktartDto = DelegateFactory.getInstance()
							.getPartnerServicesDelegate()
							.kontaktartFindByPrimaryKey((Integer) key);
					wtfKontaktart.setText(kontaktartDto.getCBez());

				}

			}
		} catch (Throwable e1) {
			intFrame.handleException(e1, false);
		}
	}

	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);

		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.report.save"));
		wbuAbbrechen
				.setText(LPMain.getInstance().getTextRespectUISPr("Cancel"));

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		wlaTitel.setText(LPMain.getInstance().getTextRespectUISPr("lp.titel"));

		wtfTitel = new WrapperTextField();
		wtfTitel.setMandatoryField(true);

		wtfKontaktart = new WrapperTextField();
		wtfKontaktart.setMandatoryField(true);
		wtfKontaktart.setActivatable(false);
		wtfKontaktart.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuKontaktart = new WrapperButton();
		wbuKontaktart.setText(LPMain
				.getTextRespectUISPr("proj.projekt.label.kontaktart") + "...");
		wbuKontaktart.setActionCommand(ACTION_SPECIAL_FLR_KONTAKTART);
		wbuKontaktart.addActionListener(this);

		intFrame.addItemChangedListener(this);

		wlaKontaktVon = new WrapperLabel();
		wlaKontaktVon.setText(LPMain.getTextRespectUISPr("lp.kontaktvon"));

		wlaWiedervorlage = new WrapperLabel();
		wlaWiedervorlage
				.setText(LPMain.getTextRespectUISPr("lp.wiedervorlage"));

		wtfWiedervorlage = new WrapperTimestampField();
		wtfWiedervorlage.setMandatoryField(true);

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 14);
		wtfWiedervorlage.setTimestamp(new java.sql.Timestamp(c
				.getTimeInMillis()));

		wtfKontaktVon = new WrapperTimestampField();
		wtfKontaktVon.setMandatoryField(true);

		wtfKontaktVon.setTimestamp(new java.sql.Timestamp(System
				.currentTimeMillis()));

		wsfPersonalzugewiesener = new WrapperSelectField(
				WrapperSelectField.PERSONAL, intFrame, false);
		wsfPersonalzugewiesener.setKey(LPMain.getTheClient().getIDPersonal());
		wsfPersonalzugewiesener.getWrapperButton().setText(
				LPMain.getTextRespectUISPr("proj.personal.zugewiesener")
						+ "...");

		int iZeile = 0;

		panel1.add(wlaTitel, new GridBagConstraints(0, iZeile, 1, 1, .5, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));

		panel1.add(wtfTitel, new GridBagConstraints(1, iZeile, 3, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(wlaKontaktVon, new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));

		panel1.add(wtfKontaktVon, new GridBagConstraints(1, iZeile, 3, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,
						2, 2, 2), 200, 0));
		iZeile++;

		panel1.add(wlaWiedervorlage, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wtfWiedervorlage, new GridBagConstraints(1, iZeile, 3, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 200, 0));

		iZeile++;
		panel1.add(wbuKontaktart, new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));

		panel1.add(wtfKontaktart, new GridBagConstraints(1, iZeile, 3, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		iZeile++;
		panel1.add(wsfPersonalzugewiesener.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wsfPersonalzugewiesener.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 3, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(wbuSpeichern, new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						20, 2, 2, 2), 100, 0));

		panel1.add(wbuAbbrechen, new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						20, 2, 2, 2), 100, 0));

		getContentPane().add(panel1);

		KontaktartDto kDto = DelegateFactory.getInstance()
				.getPartnerServicesDelegate()
				.getVorschlagFuerWiedervorlageAusSerienbrief();
		if (kDto != null) {
			kontaktartDto = kDto;

			wtfKontaktart.setText(kDto.getCBez());
		}

		// Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				/*
				 * Instead of directly closing the window, we're going to change
				 * the JOptionPane's value property.
				 */
				setVisible(false);
			}
		});

	}

	public void clearAndHide() {
		setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_KONTAKTART)) {
				panelQueryFLRKontaktart = new PanelQueryFLR(null, null,
						QueryParameters.UC_ID_KONTAKTART, null, intFrame,
						LPMain.getInstance().getTextRespectUISPr(
								"proj.projekt.label.kontaktart"));

				new DialogQuery(panelQueryFLRKontaktart);
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

		if (wtfTitel.getText() == null || kontaktartDto == null
				|| wtfKontaktVon.getTimestamp() == null
				|| wtfWiedervorlage.getTimestamp() == null) {

			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
			return;

		}

		SerienbriefEmpfaengerDto[] serienbriefEmpfaengerDto = DelegateFactory
				.getInstance().getKundeDelegate()
				.getSerienbriefEmpfaenger(serienbriefIId);

		boolean b = DialogFactory.showModalJaNeinDialog(
				intFrame,
				LPMain.getInstance().getMessageTextRespectUISPr(
						"part.serienbrief.wiedervorlage.frage",
						serienbriefEmpfaengerDto.length));

		if (b == true) {
			DelegateFactory
					.getInstance()
					.getPartnerDelegate()
					.empfaengerlisteAlsKontakteBeiPartnernEintragen(
							wtfTitel.getText(), serienbriefEmpfaengerDto,
							wtfKontaktVon.getTimestamp(),
							wtfWiedervorlage.getTimestamp(),
							kontaktartDto.getIId(),
							wsfPersonalzugewiesener.getIKey());
		}
		this.setVisible(false);
	}
}
