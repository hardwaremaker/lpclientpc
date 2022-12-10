
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
package com.lp.client.frame.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;

@SuppressWarnings("static-access")
public class DialogQRCodeLesen extends JDialog {

	private String barcode = "";

	private static final long serialVersionUID = 1L;
	protected String ACTION_DELETE = "action_delete";
	protected String ACTION_ADD_FROM_HAND = "ACTION_ADD_FROM_HAND";

	JPanel panel1 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();

	JButton jButtonUebernehmen = new JButton();

	public List<SeriennrChargennrMitMengeDto> alSeriennummern = null;

	protected InternalFrame internalFrame = null;

	public DialogQRCodeLesen(InternalFrame internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance().getTextRespectUISPr("er.erausqr.scan"), true);

		this.internalFrame = internalFrame;

		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(250, 100);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addEscapeListener(this);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		jButtonUebernehmen.grabFocus();
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

	public void dispose() {
		// Beim expliziten Close die moeglicherweise erfassten Artikel verwerfen
		alSeriennummern.clear();
		super.dispose();
	}

	public void actionPerformed(ActionEvent e) {
		try {

		} catch (Throwable e1) {
			internalFrame.handleException(e1, true);
		}

	}

	public String getBarCode() {
		return barcode;
	}

	protected void setBarCode(String s) {
		barcode = s;
	}

	public int iAnzahlEnter = 0;
	public int iAnzahlGroesser = 0;

	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);
		jButtonUebernehmen.setText(LPMain.getInstance().getTextRespectUISPr("er.erausqr.beenden"));

		add(panel1);

		jButtonUebernehmen.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				// if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				// your code is scanned and you can access it using frame.getBarCode()
				// now clean the bar code so the next one can be read
				// setBarCode(new String());
				// setVisible(false);

				// } else {
				// some character has been read, append it to your "barcode cache"

				System.out.println(e.getKeyChar() + " " + e.getKeyCode());

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					iAnzahlEnter++;
				}

				int ascii = (int) e.getKeyChar();
				if (ascii < 256) {
					setBarCode(getBarCode() + e.getKeyChar());
				}
				if (iAnzahlEnter > 29) {
					setVisible(false);
				}

				if (e.getKeyCode() == KeyEvent.VK_LESS) {
					iAnzahlGroesser++;
					if (iAnzahlGroesser > 1) {
						setVisible(false);
					}
				}

			}
			// }

		});

		jButtonUebernehmen.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				setVisible(false);

			}

		});

		panel1.add(jButtonUebernehmen, new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	}

	public void jButtonUebernehmen_actionPerformed(ActionEvent e) {

		setVisible(false);
	}

}

class DialogQRCodeLesen_jButtonUebernehmen_actionAdapter implements ActionListener {
	private DialogQRCodeLesen adaptee;

	DialogQRCodeLesen_jButtonUebernehmen_actionAdapter(DialogQRCodeLesen adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonUebernehmen_actionPerformed(e);
	}
}
