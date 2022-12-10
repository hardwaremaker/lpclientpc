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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.filechooser.open.XlsFileOpener;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access")
public abstract class DialogImportAllgemeinXLS extends JDialog implements
		ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panel = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();

	JButton wbuAbbrechen = new JButton();

	JButton wbuImportieren = new JButton();

	private byte[] xlsDatei = null;

	private JScrollPane jspScrollPane = new JScrollPane();
	private WrapperTextArea wtaFehler = new WrapperTextArea();
	private TabbedPane tp = null;
	
	public TabbedPane getTp() {
		return tp;
	}

	private Integer belegIId=null;
	protected String panelCNr=null;
	
	

	public DialogImportAllgemeinXLS(TabbedPane tp, String title, Integer belegIId, String panelCNr)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), title, true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		this.tp = tp;
		this.belegIId = belegIId;
		this.panelCNr = panelCNr;

		XlsFileOpener xlsOpener = getXlsFileOpener();
		xlsOpener.doOpenDialog();
		
		if (!xlsOpener.hasFileChosen()) {
			dispose();
			return;
		}
		
		xlsDatei = xlsOpener.getFile().getBytes();

		jbInit();
		pack();
		this.setSize(500, 500);
		LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(this);
		setVisible(true);
	}
	
	protected abstract XlsFileOpener getXlsFileOpener();

	private void jbInit() throws Throwable {
		panel.setLayout(gridBagLayout1);

		wbuImportieren.setText("Importieren");

		wtaFehler.setText(pruefe());

		wbuAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abbrechen"));

		wbuImportieren.addActionListener(this);

		if (wtaFehler.getText() != null && wtaFehler.getText().length() > 0) {
			wbuImportieren.setEnabled(false);
		} else {
			wtaFehler.setText("Keine Fehler gefunden");
		}

		setSize(500, 500);

		wbuAbbrechen.addActionListener(this);

		this.getContentPane().setLayout(gridBagLayout2);

		this.getContentPane().add(
				panel,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 250, 50));

		panel.add(jspScrollPane, new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 2), 0, 0));
		jspScrollPane.getViewport().add(wtaFehler, null);

		panel.add(wbuImportieren, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						2, 2, 2, 2), 100, 0));
		panel.add(wbuAbbrechen, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						2, 2, 2, 2), 100, 0));

	}

	public byte[] getXLSDatei() {
		return xlsDatei;
	}

	public Integer getBelegId() {
		return belegIId;
	}
	
	public abstract String pruefe() throws Throwable;

	public abstract void importiere() throws Throwable;

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(wbuImportieren)) {
			try {
				importiere();

				this.setVisible(false);

			} catch (Throwable e2) {
				tp.handleException(e2, false);
			}

		} else if (e.getSource().equals(wbuAbbrechen)) {
			this.setVisible(false);

		}
	}

}
