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
import java.math.BigDecimal;
import java.util.HashSet;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.PruefkombinationDto;

@SuppressWarnings("static-access")
public class DialogCrimpwerteAendern extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();

	private WrapperLabel wlaCrimphoeheDraht = new WrapperLabel();
	private WrapperLabel wlaCrimphoeheInsolation = new WrapperLabel();
	private WrapperLabel wlaCrimpBreiteDraht = new WrapperLabel();
	private WrapperLabel wlaCrimpBreiteInsolation = new WrapperLabel();

	public WrapperNumberField wnfCrimphoeheDraht = new WrapperNumberField();
	public WrapperNumberField wnfCrimphoeheInsolation = new WrapperNumberField();
	public WrapperNumberField wnfCrimpBreiteDraht = new WrapperNumberField();
	public WrapperNumberField wnfCrimpBreiteInsolation = new WrapperNumberField();

	public DialogCrimpwerteAendern(Object[] selectedIds) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);

		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(550, 110);

		setTitle(LPMain.getInstance().getTextRespectUISPr(
				"stk.pruefkombination.crimpwerteaendern")
				+ "  ("
				+ selectedIds.length
				+ " "
				+ LPMain.getInstance().getTextRespectUISPr(
						"stk.pruefkombination.crimpwerteaendern.datensaetze")
				+ ")");

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		// Wenn alle Werte gleich, dann vorbesetzen

		HashSet<BigDecimal> hsCrimphoeheDraht = new HashSet<BigDecimal>();
		HashSet<BigDecimal> hsCrimphoeheIsolation = new HashSet<BigDecimal>();
		HashSet<BigDecimal> hsCrimpBreiteDraht = new HashSet<BigDecimal>();
		HashSet<BigDecimal> hsCrimpBreiteIsolation = new HashSet<BigDecimal>();

		for (int i = 0; i < selectedIds.length; i++) {

			PruefkombinationDto pkDto = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.pruefkombinationFindByPrimaryKey((Integer) selectedIds[i]);

			hsCrimphoeheDraht.add(pkDto.getNCrimphoeheDraht());
			hsCrimphoeheIsolation.add(pkDto.getNCrimphoeheIsolation());
			hsCrimpBreiteDraht.add(pkDto.getNCrimpbreitDraht());
			hsCrimpBreiteIsolation.add(pkDto.getNCrimpbreiteIsolation());

		}

		if (hsCrimphoeheDraht.size() == 1) {
			wnfCrimphoeheDraht.setBigDecimal(hsCrimphoeheDraht.iterator()
					.next());
		}
		if (hsCrimphoeheIsolation.size() == 1) {
			wnfCrimphoeheInsolation.setBigDecimal(hsCrimphoeheIsolation
					.iterator().next());
		}
		if (hsCrimpBreiteDraht.size() == 1) {
			wnfCrimpBreiteDraht.setBigDecimal(hsCrimpBreiteDraht.iterator()
					.next());
		}
		if (hsCrimpBreiteIsolation.size() == 1) {
			wnfCrimpBreiteInsolation.setBigDecimal(hsCrimpBreiteIsolation
					.iterator().next());
		}

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuSpeichern)) {
			try {
				if (wnfCrimphoeheDraht.getBigDecimal() == null
						|| wnfCrimphoeheInsolation.getBigDecimal() == null
						|| wnfCrimpBreiteDraht.getBigDecimal() == null
						|| wnfCrimpBreiteInsolation.getBigDecimal() == null) {

					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
					return;
				}

				setVisible(false);

			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(wbuAbbrechen)) {
			try {
				wnfCrimphoeheDraht.setBigDecimal(null);
				wnfCrimphoeheInsolation.setBigDecimal(null);
				wnfCrimpBreiteDraht.setBigDecimal(null);
				wnfCrimpBreiteInsolation.setBigDecimal(null);
			} catch (ExceptionLP e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.setVisible(false);
		}

	}

	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);

		wlaCrimpBreiteDraht.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.crimpbreitedraht"));
		wlaCrimphoeheDraht.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.crimphoehedraht"));
		wlaCrimphoeheInsolation
				.setText(LPMain
						.getTextRespectUISPr("stk.pruefkombination.crimphoeheisolation"));
		wlaCrimpBreiteInsolation
				.setText(LPMain
						.getTextRespectUISPr("stk.pruefkombination.crimpbreiteisolation"));

		wnfCrimpBreiteDraht.setMandatoryField(true);
		wnfCrimphoeheDraht.setMandatoryField(true);
		wnfCrimphoeheInsolation.setMandatoryField(true);
		wnfCrimpBreiteInsolation.setMandatoryField(true);

		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.ok"));
		wbuAbbrechen
				.setText(LPMain.getInstance().getTextRespectUISPr("Cancel"));

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		add(panel1);

		panel1.add(wlaCrimphoeheDraht, new GridBagConstraints(0, 0, 1, 1, 1.0,
				1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 0), 100, 0));
		panel1.add(wnfCrimphoeheDraht, new GridBagConstraints(1, 0, 1, 1, 1.0,
				1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 0), 0, 0));

		panel1.add(wlaCrimphoeheInsolation, new GridBagConstraints(2, 0, 1, 1,
				1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 0), 100, 0));
		panel1.add(wnfCrimphoeheInsolation, new GridBagConstraints(3, 0, 1, 1,
				1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 0), 0, 0));

		panel1.add(wlaCrimpBreiteDraht, new GridBagConstraints(0, 1, 1, 1, 1.0,
				1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 0), 0, 0));
		panel1.add(wnfCrimpBreiteDraht, new GridBagConstraints(1, 1, 1, 1, 1.0,
				1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 0), 0, 0));

		panel1.add(wlaCrimpBreiteInsolation, new GridBagConstraints(2, 1, 1, 1,
				1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 0), 0, 0));
		panel1.add(wnfCrimpBreiteInsolation, new GridBagConstraints(3, 1, 1, 1,
				1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 0), 0, 0));

		panel1.add(wbuSpeichern, new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		panel1.add(wbuAbbrechen, new GridBagConstraints(2, 5, 1, 1, 1.0, 1.0,
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
