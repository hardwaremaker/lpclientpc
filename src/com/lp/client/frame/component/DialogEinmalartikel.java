package com.lp.client.frame.component;

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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogEinmalartikel extends JDialog implements ActionListener,
		FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperLabel wlaZusatzbezeichnung = new WrapperLabel(LPMain
			.getInstance().getTextRespectUISPr("lp.zusatzbezeichnung"));
	private WrapperTextField wtfZusatzbezeichnung = new WrapperTextField(
			Facade.MAX_UNBESCHRAENKT);

	private WrapperLabel wlaBezeichnung = new WrapperLabel(LPMain.getInstance()
			.getTextRespectUISPr("lp.bezeichnung"));
	private WrapperTextField wtfBezeichnung = new WrapperTextField(
			Facade.MAX_UNBESCHRAENKT);

	public WrapperTextField getWtfBezeichnung() {
		return wtfBezeichnung;
	}

	private WrapperLabel wlaEinheit = new WrapperLabel(LPMain.getInstance()
			.getTextRespectUISPr("lp.einheit"));
	private WrapperComboBox wcoEinheit = new WrapperComboBox();

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();

	private WrapperSelectField wsfArtikelgruppe = null;

	private InternalFrame iFrame = null;

	private ButtonGroup bg = new ButtonGroup();
	private WrapperRadioButton wrbEinmalartikel = new WrapperRadioButton();
	private WrapperRadioButton wrbHandartikel = new WrapperRadioButton();

	private ArtikelDto artikelDtoNeu = null;

	public ArtikelDto getArtikelDtoNeu() {
		return artikelDtoNeu;
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

	public DialogEinmalartikel(InternalFrame iFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.iFrame = iFrame;

		try {

			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(500, 180);

		setTitle(LPMain.getInstance().getTextRespectUISPr(
				"artikel.einmalartikel.neu"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}

			public void windowOpened(WindowEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						wtfBezeichnung.requestFocusInWindow();
					}
				});
			}

		});

		this.addEscapeListener(this);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK),
				"test");
		getRootPane().getActionMap().put("test", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FocusEvent fe = new FocusEvent(wtfBezeichnung, -1);
				focusLost(fe);
				setVisible(false);
			}
		});
		wtfBezeichnung.transferFocus();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuSpeichern)) {

			try {

				if (wtfBezeichnung.getText() == null
						|| (wsfArtikelgruppe.getWrapperTextField().isVisible()
								&& wsfArtikelgruppe.isMandatoryField() && wsfArtikelgruppe
								.getIKey() == null)) {

					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));

					return;
				}

				// Neuen Artikel anlegen

				ArtikelDto artikelDto = new ArtikelDto();

				artikelDto.setArtikelsprDto(new ArtikelsprDto());
				artikelDto.getArtikelsprDto().setCBez(wtfBezeichnung.getText());
				artikelDto.getArtikelsprDto().setCZbez(
						wtfZusatzbezeichnung.getText());
				artikelDto.setEinheitCNr((String) wcoEinheit
						.getKeyOfSelectedItem());
				
				Integer artikelIIdNeu;
				if(wrbHandartikel.isSelected()){
					artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);
					artikelIIdNeu= DelegateFactory.getInstance()
							.getArtikelDelegate().createArtikel(artikelDto);
				} else {
					artikelDto.setArtgruIId(wsfArtikelgruppe.getIKey());
					artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);
					 artikelIIdNeu = DelegateFactory.getInstance()
							.getArtikelDelegate().createArtikel(artikelDto, true);
				}
				

				
				artikelDtoNeu = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(artikelIIdNeu);

				setVisible(false);

			} catch (Throwable e1) {
				iFrame.handleException(e1, true);
				return;
			}

			this.setVisible(false);

		} else if (e.getSource().equals(wbuAbbrechen)) {
			artikelDtoNeu = null;
			this.setVisible(false);
		} else if (e.getSource().equals(wrbEinmalartikel)) {
			wsfArtikelgruppe.getWrapperTextField().setVisible(true);
			wsfArtikelgruppe.getWrapperButton().setVisible(true);

		} else if (e.getSource().equals(wrbHandartikel)) {

			wsfArtikelgruppe.getWrapperTextField().setVisible(false);
			wsfArtikelgruppe.getWrapperButton().setVisible(false);
		}

	}

	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);
		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.report.save"));
		wbuAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abbrechen"));

		wtfBezeichnung.setMandatoryField(true);

		wcoEinheit.setMandatoryField(true);
		wcoEinheit.setMap(DelegateFactory.getInstance().getSystemDelegate()
				.getAllEinheiten());

		wcoEinheit.setKeyOfSelectedItem(SystemFac.EINHEIT_STUECK);

		wsfArtikelgruppe = new WrapperSelectField(
				WrapperSelectField.ARTIKELGRUPPE, iFrame, true);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARTIKELGRUPPE_IST_PFLICHTFELD,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		wsfArtikelgruppe.setMandatoryField((Boolean) parameter
				.getCWertAsObject());

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);
		bg.add(wrbEinmalartikel);
		bg.add(wrbHandartikel);

		
		wrbEinmalartikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.einmalartikel"));
		wrbHandartikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.einmalartikel.handartikel"));

		wrbHandartikel.addActionListener(this);
		wrbEinmalartikel.addActionListener(this);


		
		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_EINMALARTIKEL_DEFAULT_IST_HANDARTIKEL,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		boolean bHandartikelIstDefault=(Boolean) parameter
				.getCWertAsObject();
		if(bHandartikelIstDefault){
			wrbHandartikel.setSelected(true);	
			wsfArtikelgruppe.getWrapperTextField().setVisible(false);
			wsfArtikelgruppe.getWrapperButton().setVisible(false);
		} else {
			wrbEinmalartikel.setSelected(true);	
		}
		

		add(panel1);

		int iZeile = 0;
		panel1.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 2, 1, 1.0,
				0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(wlaZusatzbezeichnung, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wtfZusatzbezeichnung, new GridBagConstraints(1, iZeile, 2,
				1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(wlaEinheit, new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 150, 0));
		panel1.add(wcoEinheit, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 150, 0));
		iZeile++;
		panel1.add(wsfArtikelgruppe.getWrapperButton(), new GridBagConstraints(
				0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wsfArtikelgruppe.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 2, 1, 1.0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		iZeile++;
		panel1.add(wrbEinmalartikel, new GridBagConstraints(1, iZeile, 1, 1,
				1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wrbHandartikel, new GridBagConstraints(2, iZeile, 1, 1, 1,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		//

		panel1.add(wbuSpeichern, new GridBagConstraints(0, iZeile, 2, 1, 0,
				1.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(wbuAbbrechen, new GridBagConstraints(2, iZeile, 1, 1, 1,
				1.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 190, 0));

		setContentPane(panel1);

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
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {

	}

}
