
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
package com.lp.client.lieferschein;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.lp.client.frame.HelperClient;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access")
public class DialogAusliefervorschlagUeberleitenError extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JLabel wlaMeldung = null;
	private JButton btnOK = new JButton();

	JScrollPane jScrollPane = new JScrollPane();
	JTextPane textPanelLog = new JTextPane();
	String zeilen = null;

	public DialogAusliefervorschlagUeberleitenError(String zeilen) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);

		this.zeilen = zeilen;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(700, 400);

	}

	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		btnOK.setText(LPMain.getInstance().getTextRespectUISPr("button.ok"));

		btnOK.addActionListener(this);

		wlaMeldung = new JLabel(LPMain.getTextRespectUISPr("ls.ausliefervorschlag.ausgelassenweilerledigt"));
		textPanelLog.setContentType("text/html");

		textPanelLog.setText(zeilen);
		textPanelLog.setFont(Font.decode("Courier bold 12"));
		jScrollPane.getViewport().add(textPanelLog);
		this.getContentPane().setLayout(gridBagLayout2);

		int iZeile = 0;

		this.getContentPane().add(wlaMeldung, new GridBagConstraints(0, iZeile, 3, 1, 0, 0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		iZeile++;

		this.getContentPane().add(jScrollPane, new GridBagConstraints(0, iZeile, 3, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

		iZeile++;

		this.getContentPane().add(btnOK, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 250, 5, 5), 0, 0));

	}

}
