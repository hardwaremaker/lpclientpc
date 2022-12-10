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
package com.lp.client.bestellung;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access")
public class DialogStandortAuswaehlen extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();

	private WrapperLabel wlaStandort = new WrapperLabel();

	private WrapperComboBox wcbStandort = new WrapperComboBox();

	public Integer getPartnerIIdStandort() {
		return (Integer) wcbStandort.getKeyOfSelectedItem();
	}

	public Integer iJahr = null;

	public boolean bCancel=true;
	
	public DialogStandortAuswaehlen() throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);

		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(400, 140);

		setTitle(LPMain.getInstance().getTextRespectUISPr("system.standort"));

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
				bCancel=false;
				setVisible(false);

			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(wbuAbbrechen)) {
			iJahr = null;
			this.setVisible(false);
		}

	}

	private void jbInit() throws Throwable {

		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.ok"));
		wbuAbbrechen
				.setText(LPMain.getInstance().getTextRespectUISPr("Cancel"));
		wcbStandort.setEmptyEntry(LPMain.getInstance().getTextRespectUISPr("lp.alle"));
		wcbStandort.setMap(DelegateFactory.getInstance().getLagerDelegate()
				.getAlleStandorte());
		wlaStandort.setText(LPMain.getInstance().getTextRespectUISPr(
				"system.standort"));

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		JPanel panelAuswahl = new JPanel();
		HvLayout layoutAuswahl = HvLayoutFactory.create(panelAuswahl, "insets 0", "[120,fill]10[fill,grow]", "");
		layoutAuswahl.add(wlaStandort, "pushx").add(wcbStandort);
		
		HvLayout layout = HvLayoutFactory.create(panel1, "al center center", "10[fill,grow]10[fill,grow]10", "[]15[]");
		layout.add(panelAuswahl).spanAndWrap(2)
			.add(wbuSpeichern).add(wbuAbbrechen);

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
