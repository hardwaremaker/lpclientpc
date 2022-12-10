
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.rechnung.ReportRechnung;

import net.sf.jasperreports.engine.JRException;

@SuppressWarnings("static-access")
public class DialogLosSonderetikett extends JDialog implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panel = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	public JCheckBox cbVorschau = new JCheckBox();

	JButton wbuFertig = new JButton();

	TabbedPaneLos tabbedpaneLos = null;

	private String barcode = "";

	public String getBarCode() {
		return barcode;
	}

	protected void setBarCode(String s) {
		barcode = s;
	}

	public DialogLosSonderetikett(TabbedPaneLos tabbedpaneLos) throws Throwable {
		super(LPMain.getInstance().getDesktop(),
				LPMain.getInstance().getTextRespectUISPr("fert.report.sonderetikett.dialog.titel"), true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.tabbedpaneLos = tabbedpaneLos;
		jbInit();
		pack();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addEscapeListener(this);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		wbuFertig.addKeyListener(this);
		cbVorschau.addKeyListener(this);

		wbuFertig.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				setVisible(false);

			}

		});

		wbuFertig.grabFocus();
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

	private void jbInit() throws Throwable {

		panel.setLayout(gridBagLayout1);

		cbVorschau.setText(LPMain.getInstance().getTextRespectUISPr("fert.report.sonderetikett.dialog.vorschau"));

		wbuFertig.setText(LPMain.getInstance().getTextRespectUISPr("lp.beenden"));
		
		this.getContentPane().setLayout(gridBagLayout2);

		this.getContentPane().add(panel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 250, 0));

		panel.add(cbVorschau, new GridBagConstraints(2, 2, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panel.add(wbuFertig, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public void wbuFertig_actionPerformed(ActionEvent e) {

		this.dispose();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource() == wbuFertig || e.getSource() == cbVorschau) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				// your code is scanned and you can access it using frame.getBarCode()
				// now clean the bar code so the next one can be read
				// setBarCode(new String());

				if (getBarCode().length() > 0) {
					try {
						if (cbVorschau.isSelected()) {

							setVisible(false);
							String add2Title = LPMain.getTextRespectUISPr("fert.report.sonderetikett");
							tabbedpaneLos.getInternalFrame().showReportKriterien(new ReportLosSonderetikett(
									tabbedpaneLos.getInternalFrameFertigung(), getBarCode(), add2Title));

						} else {

							PanelReportKriterien krit = null;
							try {
								// DruckPanel instantiieren
								krit = new PanelReportKriterien(tabbedpaneLos.getInternalFrame(),
										new ReportLosSonderetikett(tabbedpaneLos.getInternalFrameFertigung(),
												getBarCode(), ""),
										"", null, null, false, false, false);
								// jetzt das tatsaechliche Drucken

								krit.druckeArchiviereUndSetzeVersandstatusEinesBelegs();
							} catch (JRException ex) {
								int iChoice = JOptionPane.YES_OPTION;

								String sMsg = LPMain.getTextRespectUISPr("lp.drucken.reportkonntenichtgedrucktwerden");
								Object[] options = { LPMain.getTextRespectUISPr("lp.druckerror.wiederholen"),
										LPMain.getTextRespectUISPr("lp.abbrechen") };
								iChoice = DialogFactory.showModalDialog(tabbedpaneLos.getInternalFrame(), sMsg,
										LPMain.getTextRespectUISPr("lp.error"), options, options[0]);
								if (iChoice == 0) {
									Thread.sleep(5000);
									krit.druckeArchiviereUndSetzeVersandstatusEinesBelegs();
								}
							}

						}

						setBarCode("");
						wbuFertig.requestFocus();

					} catch (Throwable ex) {
						tabbedpaneLos.handleException(ex, true);
					}

				}

			} else {
			// some character has been read, append it to your "barcode cache"

			int ascii = (int) e.getKeyChar();
			if (ascii < 256) {
				setBarCode(getBarCode() + e.getKeyChar());
			}

		}

		}

	}

}

