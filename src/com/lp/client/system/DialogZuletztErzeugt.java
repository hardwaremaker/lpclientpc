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
package com.lp.client.system;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.MultipleImageViewer;
import com.lp.client.frame.component.WrapperBildField;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSNRField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogZuletztErzeugt extends JDialog implements ActionListener,
		KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private InternalFrame internalFrame = null;
	private String cGruppeKeyValue = null;

	private WrapperButton wbuOK = new WrapperButton();

	public DialogZuletztErzeugt(String cGruppeKeyValue,
			InternalFrame internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);

		
		
		this.cGruppeKeyValue = cGruppeKeyValue;
		this.internalFrame = internalFrame;
		try {
			jbInit();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		setTitle(LPMain.getInstance().getTextRespectUISPr("lp.zuletzt.erzeugt"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuOK)) {

			setVisible(false);

		}

	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
		if (e.getSource() instanceof WrapperSNRField) {

		}

	}

	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);

		wbuOK.setText(LPMain.getInstance().getTextRespectUISPr("button.ok"));

		wbuOK.addActionListener(this);

		add(panel1);

		int iZeile = 0;

		int height=80;
		
		KeyvalueDto[] kvDtos = DelegateFactory.getInstance()
				.getSystemDelegate().keyvalueFindyByCGruppe(cGruppeKeyValue);

		for (int i = 0; i < kvDtos.length; i++) {
			KeyvalueDto kvDto = kvDtos[i];

			iZeile++;

			panel1.add(new WrapperLabel(kvDto.getCKey()),
					new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(0, 0, 0, 0), 150, 0));
			panel1.add(new WrapperLabel(kvDto.getCValue()),
					new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(0, 0, 0, 10), 150, 0));
			height+=22;
		}

		iZeile++;
		panel1.add(wbuOK, new GridBagConstraints(0, iZeile, 2, 1, 1.0, 1,
				GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(
						20, 0, 10, 0), 100, 0));

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

		this.setSize(600, height);

	}

	public void clearAndHide() {
		setVisible(false);
	}

}
