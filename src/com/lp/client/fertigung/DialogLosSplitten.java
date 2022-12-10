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
package com.lp.client.fertigung;

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
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.BucheSerienChnrAufLosDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogLosSplitten extends JDialog implements FocusListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();
	private TabbedPaneLos tpLos = null;

	JLabel losBestehend = new JLabel();
	JLabel losNeu = new JLabel();

	JLabel losgroesse = new JLabel();
	JLabel beginntermin = new JLabel();

	private WrapperNumberField wnfMengeBestehend = new WrapperNumberField();
	private WrapperNumberField wnfMengeNeu = new WrapperNumberField();

	private WrapperDateField wdfBeginnBestehend = new WrapperDateField();
	private WrapperDateField wdfBeginnNeu = new WrapperDateField();

	public DialogLosSplitten(TabbedPaneLos tpLos) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.tpLos = tpLos;
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		this.setSize(Defaults.sizeFactor(300), Defaults.sizeFactor(140));
		
		//this.setSize(300, 140);

		setTitle(LPMain.getInstance().getTextRespectUISPr("fert.menu.lossplitten") + " ("+tpLos.getLosDto().getCNr()+")");

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});
		this.addEscapeListener(this);

		wnfMengeNeu.requestFocusInWindow();

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

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuSpeichern)) {
			try {

				if (wnfMengeBestehend.getBigDecimal() == null || wnfMengeNeu.getBigDecimal() == null
						|| wdfBeginnBestehend.getDate() == null || wdfBeginnNeu.getDate() == null) {

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
					return;
				}

				DelegateFactory.getInstance().getFertigungDelegate().losSplitten(tpLos.getLosDto().getIId(),
						wnfMengeNeu.getBigDecimal(), wdfBeginnNeu.getDate());

				setVisible(false);

			} catch (Throwable e1) {
				tpLos.handleException(e1, true);
			}
		} else if (e.getSource().equals(wbuAbbrechen)) {
			this.setVisible(false);
		}

	}

	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);

		losBestehend.setText(LPMain.getInstance().getTextRespectUISPr("fert.menu.lossplitten.bestehendeslos"));
		losNeu.setText(LPMain.getInstance().getTextRespectUISPr("fert.menu.lossplitten.neueslos"));
		losgroesse.setText(LPMain.getInstance().getTextRespectUISPr("fert.menu.lossplitten.losgroesse"));
		beginntermin.setText(LPMain.getInstance().getTextRespectUISPr("fert.menu.lossplitten.beginn"));

		wnfMengeBestehend.setMandatoryField(true);
		wnfMengeNeu.setMandatoryField(true);

		wnfMengeBestehend.setMinimumValue(
				new BigDecimal(1).movePointLeft(Defaults.getInstance().getIUINachkommastellenLosgroesse()));
		wnfMengeBestehend.setFractionDigits(Defaults.getInstance().getIUINachkommastellenLosgroesse());

		wnfMengeNeu.setMinimumValue(
				new BigDecimal(1).movePointLeft(Defaults.getInstance().getIUINachkommastellenLosgroesse()));
		wnfMengeNeu.setFractionDigits(Defaults.getInstance().getIUINachkommastellenLosgroesse());
		wnfMengeNeu.setMaximumValue(tpLos.getLosDto().getNLosgroesse().subtract(BigDecimal.ONE));

		wdfBeginnBestehend.setMandatoryField(true);
		wdfBeginnNeu.setMandatoryField(true);

		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr("lp.report.save"));
		wbuAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr("Cancel"));

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		wnfMengeBestehend.setBigDecimal(tpLos.getLosDto().getNLosgroesse());

		wnfMengeBestehend.setEnabled(false);
		wdfBeginnBestehend.setEnabled(false);

		// wnfMengeNeu.setBigDecimal(BigDecimal.ZERO);
		wdfBeginnBestehend.setDate(tpLos.getLosDto().getTProduktionsbeginn());
		wdfBeginnNeu.setDate(tpLos.getLosDto().getTProduktionsbeginn());

		wnfMengeNeu.addFocusListener(this);
		
		
		add(panel1);
		int iZeile = 0;

		panel1.add(new JLabel(), new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(losgroesse, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(beginntermin, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(losBestehend, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wnfMengeBestehend, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wdfBeginnBestehend, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(losNeu, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wnfMengeNeu, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wdfBeginnNeu, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(wbuSpeichern, new GridBagConstraints(0, iZeile, 2, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wbuAbbrechen, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		setContentPane(panel1);

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

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		try {
			if(wnfMengeNeu.getBigDecimal()!=null) {
				wnfMengeBestehend.setBigDecimal(tpLos.getLosDto().getNLosgroesse().subtract(wnfMengeNeu.getBigDecimal()));
			}
		} catch (ExceptionLP e1) {
			tpLos.handleException(e1, true);
		}
		
	}

}
