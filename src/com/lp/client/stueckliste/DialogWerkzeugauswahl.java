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
package com.lp.client.stueckliste;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.LinkedHashMap;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.WerkzeugDto;
import com.lp.server.stueckliste.service.PruefkombinationDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogWerkzeugauswahl extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();

	private WrapperLabel wlaWerkzeug = new WrapperLabel();

	private WrapperLabel wlaVerschleissteil = new WrapperLabel();
	private WrapperComboBox wcbVerschleissteil = new WrapperComboBox();;

	private JLabel wlaKontakt = new JLabel();
	private JLabel wlaLitze = new JLabel();

	private WrapperComboBox wcbWerkzeug = null;

	public Integer artikelIIdKontakt = null;
	public Integer artikelIIdLitze = null;
	public Integer werkzeugIId = null;
	public Integer verschleissteilIId = null;

	public DialogWerkzeugauswahl(Integer artikelIIdKontakt,
			Integer artikelIIdLitze) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.artikelIIdKontakt = artikelIIdKontakt;
		this.artikelIIdLitze = artikelIIdLitze;
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(350, 150);

		setTitle(LPMain.getInstance().getTextRespectUISPr("artikel.werkzeug"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuSpeichern)) {
			try {
				if (wcbWerkzeug.getKeyOfSelectedItem() == null
						|| wcbVerschleissteil.getKeyOfSelectedItem() == null) {

					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
					return;
				}

				werkzeugIId = (Integer) wcbWerkzeug.getKeyOfSelectedItem();
				verschleissteilIId = (Integer) wcbVerschleissteil
						.getKeyOfSelectedItem();

				setVisible(false);

			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(wbuAbbrechen)) {
			werkzeugIId = null;
			verschleissteilIId = null;
			this.setVisible(false);
		} else if (e.getSource().equals(wcbWerkzeug)) {
			try {
				wcbVerschleissteil.setMap(DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.getAllVerschleissteile(
								(Integer) wcbWerkzeug.getKeyOfSelectedItem()));
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}

	}

	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);

		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.ok"));
		wbuAbbrechen
				.setText(LPMain.getInstance().getTextRespectUISPr("Cancel"));

		wcbWerkzeug = new WrapperComboBox();
		wcbWerkzeug.setMandatoryField(true);
		wcbWerkzeug.addActionListener(this);

		wcbVerschleissteil.setMandatoryField(true);

		PruefkombinationDto[] kombiDtos = DelegateFactory
				.getInstance()
				.getStuecklisteDelegate()
				.pruefkombinationFindByArtikelIIdKontaktArtikelIIdLitze(
						artikelIIdKontakt, artikelIIdLitze);

		LinkedHashMap<Integer, String> tmArten = new LinkedHashMap<Integer, String>();

		PruefkombinationDto ersterStandard = null;
		for (PruefkombinationDto kombiDto : kombiDtos) {

		/*	WerkzeugDto wDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.werkzeugFindByPrimaryKey(kombiDto.getWerkzeugIId());
			String werkzeug = wDto.getBezeichnung();

			if (Helper.short2boolean(kombiDto.getBStandard())) {
				werkzeug += " (Standard)";

				if (ersterStandard == null) {
					ersterStandard = kombiDto;
				}
			}
			tmArten.put(kombiDto.getWerkzeugIId(), werkzeug);*/

		}

		if(ersterStandard==null){
			ersterStandard=kombiDtos[0];
		}
		
		
		wcbWerkzeug.setMap(tmArten, true);

	//	wcbWerkzeug.setKeyOfSelectedItem(ersterStandard.getWerkzeugIId());

		wlaVerschleissteil.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.werkzeug.verschleissteil"));
	//	wcbVerschleissteil.setMap(DelegateFactory.getInstance()
		//		.getArtikelDelegate()
		//		.getAllVerschleissteile(ersterStandard.getWerkzeugIId()));

		wlaWerkzeug.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.werkzeug"));

		ArtikelDto aDtoKontakt = DelegateFactory.getInstance()
				.getArtikelDelegate()
				.artikelFindByPrimaryKey(artikelIIdKontakt);
		wlaKontakt.setText(LPMain.getInstance().getTextRespectUISPr(
				"stk.pruefkombination.kontakt")
				+ ":  " + aDtoKontakt.formatArtikelbezeichnung());

		ArtikelDto aDtoLitze = DelegateFactory.getInstance()
				.getArtikelDelegate().artikelFindByPrimaryKey(artikelIIdLitze);
		wlaLitze.setText(LPMain.getInstance().getTextRespectUISPr(
				"stk.pruefkombination.litze")
				+ ":  " + aDtoLitze.formatArtikelbezeichnung());

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		add(panel1);

		panel1.add(wlaKontakt, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		panel1.add(wlaLitze, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		panel1.add(wlaWerkzeug, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 50, 0));
		panel1.add(wcbWerkzeug, new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 150, 0));
		panel1.add(wlaVerschleissteil, new GridBagConstraints(0, 4, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		panel1.add(wcbVerschleissteil, new GridBagConstraints(1, 4, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(wbuSpeichern, new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		panel1.add(wbuAbbrechen, new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

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

}
