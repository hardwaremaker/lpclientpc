
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access")
public class DialogWiederholendeAuftraegeAktualsisieren extends JDialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();

	private WrapperLabel wlaTage = new WrapperLabel();

	private WrapperDateField wnfDatum = new WrapperDateField();
	
	private WrapperLabel wlaMaxAbweichung = new WrapperLabel();

	private WrapperNumberField wnfMaxAbweichung = new WrapperNumberField();

	
	public WrapperDateField getWnfDatum() {
		return wnfDatum;
	}
	
	public WrapperNumberField getWnfMaxAbweichung() {
		return wnfMaxAbweichung;
	}

	public java.sql.Date datum=null; 
	
	public DialogWiederholendeAuftraegeAktualsisieren()
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);

		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(300, 110);

		setTitle(LPMain.getInstance().getTextRespectUISPr(
				"lp.datum"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				datum=null;
				setVisible(false);
				dispose();
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuSpeichern)) {
			try {
				if (wnfDatum.getDate() == null || wnfMaxAbweichung.getBigDecimal() == null) {

					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain
											.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
					return;
				}

				datum=wnfDatum.getDate();

				setVisible(false);

			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(wbuAbbrechen)) {
			datum=null;
			this.setVisible(false);
		}

	}

	private void jbInit() throws Exception {
		panel1.setLayout(gridBagLayout1);

		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.ok"));
		wbuAbbrechen
				.setText(LPMain.getInstance().getTextRespectUISPr("Cancel"));


		
		wnfDatum.setMandatoryField(true);
		wnfMaxAbweichung.setMandatoryField(true);
		wnfMaxAbweichung.setDouble(20D);

		
		wlaTage.setText(LPMain.getInstance().getTextRespectUISPr("auft.pflege.wiederholende.zurpreisgueltigkeit.datum"));
		wlaMaxAbweichung.setText(LPMain.getInstance().getTextRespectUISPr("auft.pflege.wiederholende.zurpreisgueltigkeit.maxabweichung"));

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		add(panel1);

		
		panel1.add(wlaTage, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2,2,2,2), 150, 0));
		panel1.add(wnfDatum, new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2,2,2,2), 0, 0));
		
		panel1.add(wlaMaxAbweichung, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2,2,2,2), 150, 0));
		panel1.add(wnfMaxAbweichung, new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2,2,2,2), 0, 0));

		panel1.add(wbuSpeichern, new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2,2,2,2), 0, 0));
		panel1.add(wbuAbbrechen, new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2,2,2,2), 0, 0));

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