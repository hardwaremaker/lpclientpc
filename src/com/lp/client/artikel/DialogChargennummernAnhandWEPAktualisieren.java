package com.lp.client.artikel;

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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.GeaenderteChargennummernDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.Facade;

@SuppressWarnings("static-access")
public class DialogChargennummernAnhandWEPAktualisieren extends JDialog
		implements ActionListener, ItemChangedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private InternalFrame intFrame = null;

	static final public String ACTION_SPECIAL_ARTIKELVON_FROM_LISTE = "ACTION_SPECIAL_ARTIKELVON_FROM_LISTE";
	static final public String ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE = "ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE";

	private Integer artikelIId_Von = null;
	private Integer artikelIId_Bis = null;
	private PanelQueryFLR panelQueryFLRArtikel_Von = null;
	private PanelQueryFLR panelQueryFLRArtikel_Bis = null;

	private WrapperSelectField wsfProjekt = null;

	private AuftragDto auftragDto = null;

	private WrapperButton wbuArtikelnrVon = new WrapperButton();
	private WrapperButton wbuArtikelnrBis = new WrapperButton();
	private WrapperTextField wtfArtikelnrVon = new WrapperTextField();
	private WrapperTextField wtfArtikelnrBis = new WrapperTextField();

	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();

	private WrapperLabel wrbArtikelnummer = new WrapperLabel();
	private WrapperLabel wrbZeitraum = new WrapperLabel();
	private ButtonGroup buttonGroup = new ButtonGroup();

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();

	ArrayList<GeaenderteChargennummernDto> geaenderteChnrs = null;

	public ArrayList<GeaenderteChargennummernDto> getGeaenderteChnrs() {
		return geaenderteChnrs;
	}

	public DialogChargennummernAnhandWEPAktualisieren(InternalFrame intFrame) {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.intFrame = intFrame;
		try {
			jbInit();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		this.setSize(700, 260);

		setTitle(LPMain.getInstance().getTextRespectUISPr(
				"artikel.pflege.chargennummern.anhandwepaktualisieren"));

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

				if (e.getSource() == panelQueryFLRArtikel_Von) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();
					ArtikelDto artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey((Integer) key);
					artikelIId_Von = artikelDto.getIId();
					wtfArtikelnrVon.setText(artikelDto.getCNr());
				} else if (e.getSource() == panelQueryFLRArtikel_Bis) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();
					ArtikelDto artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey((Integer) key);
					artikelIId_Bis = artikelDto.getIId();
					wtfArtikelnrBis.setText(artikelDto.getCNr());
				}
			} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
				if (e.getSource() == panelQueryFLRArtikel_Von) {
					wtfArtikelnrVon.setText(null);
					artikelIId_Von = null;
				} else if (e.getSource() == panelQueryFLRArtikel_Bis) {
					wtfArtikelnrBis.setText(null);
					artikelIId_Bis = null;
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

		wbuArtikelnrVon.setText(LPMain
				.getTextRespectUISPr("artikel.artikelnummer")
				+ " "
				+ LPMain.getTextRespectUISPr("lp.von"));
		wbuArtikelnrBis.setText(LPMain
				.getTextRespectUISPr("artikel.artikelnummer")
				+ " "
				+ LPMain.getTextRespectUISPr("lp.bis"));

		wbuArtikelnrVon.setActionCommand(ACTION_SPECIAL_ARTIKELVON_FROM_LISTE);
		wbuArtikelnrVon.addActionListener(this);

		wbuArtikelnrBis.setActionCommand(ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE);
		wbuArtikelnrBis.addActionListener(this);

		intFrame.addItemChangedListener(this);

		wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));

		wrbArtikelnummer.setText(LPMain
				.getTextRespectUISPr("artikel.artikelnummer"));

		wrbZeitraum
				.setText(LPMain
						.getTextRespectUISPr("artikel.pflege.chargennummern.anhandwepaktualisieren.zeitraumwep"));


		wrbArtikelnummer.setHorizontalAlignment(SwingConstants.LEFT);
		wrbZeitraum.setHorizontalAlignment(SwingConstants.LEFT);

	
		
	

		int iZeile = 0;
		panel1.add(new JLabel(LPMain
				.getTextRespectUISPr("artikel.pflege.chargennummern.anhandwepaktualisieren.ohne.ex")) , new GridBagConstraints(0, iZeile, 2, 1,
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(wrbArtikelnummer, new GridBagConstraints(0, iZeile, 1, 1,
				.3, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(wbuArtikelnrVon, new GridBagConstraints(0, iZeile, 1, 1, .5,
				0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wtfArtikelnrVon, new GridBagConstraints(1, iZeile, 3, 1,
				1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		panel1.add(wbuArtikelnrBis, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wtfArtikelnrBis, new GridBagConstraints(1, iZeile, 3, 1, 0,
				0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(wrbZeitraum, new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(wlaVon, new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));

		panel1.add(wdfVon, new GridBagConstraints(1, iZeile, 3, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		iZeile++;
		panel1.add(wlaBis, new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));

		panel1.add(wdfBis, new GridBagConstraints(1, iZeile, 3, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(wbuSpeichern, new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 100, 0));

		panel1.add(wbuAbbrechen, new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 100, 0));

		getContentPane().add(panel1);

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

	void dialogQueryArtikelFromListe_Von(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel_Von = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikel(intFrame, artikelIId_Von, true);

		new DialogQuery(panelQueryFLRArtikel_Von);
	}

	void dialogQueryArtikelFromListe_Bis(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel_Bis = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikel(intFrame, artikelIId_Bis, true);

		new DialogQuery(panelQueryFLRArtikel_Bis);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {

		

			if (e.getActionCommand().equals(
					ACTION_SPECIAL_ARTIKELVON_FROM_LISTE)) {
				dialogQueryArtikelFromListe_Von(e);
			} else if (e.getActionCommand().equals(
					ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE)) {
				dialogQueryArtikelFromListe_Bis(e);
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

		

		geaenderteChnrs = DelegateFactory
				.getInstance()
				.getPflegeDelegate()
				.automatischeChargennummernAusWEPsNachtragen(
						wtfArtikelnrVon.getText(),
						wtfArtikelnrBis.getText(),
						 new DatumsfilterVonBis(
								wdfVon.getTimestamp(), wdfBis.getTimestamp())
								);
		this.setVisible(false);
	}
}
