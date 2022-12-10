
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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;

public class DialogEingabeBetrag extends JDialog implements ActionListener {

	private static final long serialVersionUID = -5704551591990693731L;

	// private JPanel panel = new JPanel();
	// private GridBagLayout gridBagLayout = new GridBagLayout();
	// private GridBagLayout gridBagLayout = null;
	// private GridBagConstraints gridBagConstraints = null;
	// private WrapperButton wbuSpeichern = new WrapperButton();
	// private WrapperButton wbuAbbrechen = new WrapperButton();
	// private WrapperLabel wlaDatumVon = new WrapperLabel();
	// private WrapperDateField wnfDatumVon = new WrapperDateField();
	// private WrapperLabel wlaDatumBis = new WrapperLabel();
	// private WrapperDateField wnfDatumBis = new WrapperDateField();
	// private WrapperDateRangeController wdrBereich = null;

	private JPanel panel = null;
	private WrapperButton wbuSpeichern = null;
	private WrapperButton wbuAbbrechen = null;

	private WrapperLabel wlaPreis = new WrapperLabel();
	private WrapperNumberField wnfPreis = new WrapperNumberField();
	private String waehrung=null;
	
	public BigDecimal bdBetrag=null;

	private HvLayout hvLayout = null;

	private InternalFrame intFrame = null;
	
	private int iNachkommastellen=2;

	public DialogEingabeBetrag(int iNachkommastellen,String waehrung, InternalFrame intFrame)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);
		
		this.waehrung = waehrung;
		this.iNachkommastellen=iNachkommastellen;
		this.intFrame = intFrame;
		jbInit();
		setContentPane(panel);

		Dimension d = new Dimension(Defaults.sizeFactor(350),
				Defaults.sizeFactor(100));
		setSize(d.width, d.height);

		setTitle(LPMain.getTextRespectUISPr("angb.positionen.intzws.uebersteuern.betrag"));

		addEscapeListener(this);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		
		

	}

	public static void addEscapeListener(final JDialog dialog) {
		ActionListener escListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		};

		dialog.getRootPane().registerKeyboardAction(escListener,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuSpeichern)) {
			try {
				if (wnfPreis.getBigDecimal() == null) {

					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
					return;
				}

				bdBetrag = wnfPreis.getBigDecimal();

				setVisible(false);

			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(wbuAbbrechen)) {
			bdBetrag = null;
			setVisible(false);
		}

	}

	private void jbInit() throws Throwable {

		panel = new JPanel();

		wbuSpeichern = new WrapperButton();
		wbuAbbrechen = new WrapperButton();

		wbuSpeichern.setText(LPMain.getTextRespectUISPr("button.ok"));
		wbuAbbrechen.setText(LPMain.getTextRespectUISPr("Cancel"));

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		wlaPreis.setText(LPMain.getTextRespectUISPr("angb.positionen.intzws.uebersteuern.betrag")
				+ " (" + waehrung + ")");
	
		
		
		
		wnfPreis.setFractionDigits(iNachkommastellen);
		
		
		wnfPreis.setMandatoryField(true);

		
		
	
		
		add(panel);

		hvLayout = new HvLayout(panel, null, "[right, grow]", null);
		hvLayout.add(wlaPreis, 150).add(wnfPreis, 150).wrap()
		.add(wbuSpeichern,"center", 90).add(wbuAbbrechen, 90).wrap();

	}


	

}
