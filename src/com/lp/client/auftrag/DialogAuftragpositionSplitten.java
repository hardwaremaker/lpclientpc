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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.util.Facade;

@SuppressWarnings("static-access")
public class DialogAuftragpositionSplitten extends JDialog implements ActionListener,
		FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	WrapperLabel wlaArtikel = new WrapperLabel("Artikel");
	WrapperTextField wtfArtikel = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);

	WrapperLabel wlaBezeichnung = new WrapperLabel("Bezeichnung");
	WrapperTextField wtfBezeichnung = new WrapperTextField(
			Facade.MAX_UNBESCHRAENKT);

	WrapperLabel wlaMenge = new WrapperLabel("Menge");
	WrapperNumberField wnfMenge = new WrapperNumberField();

	WrapperLabel wlaLiefertermin = new WrapperLabel("");

	WrapperDateField wdfLiefertermin = new WrapperDateField();

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();
	BigDecimal bdPosmenge = null;

	private InternalFrame iFrame = null;

	private AuftragpositionDto abposDto = null;

	private BigDecimal bdNeuePositionsmenge = null;
	

	public BigDecimal getBdNeuePositionsmenge() {
		return bdNeuePositionsmenge;
	}

	private Integer auftragpositionIIdNeu = null;

	public Integer getAuftragpositionIIdNeu() {
		return auftragpositionIIdNeu;
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

	public DialogAuftragpositionSplitten(Integer bestellpositionIId,
			InternalFrame iFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.iFrame = iFrame;

		abposDto = DelegateFactory.getInstance().getAuftragpositionDelegate()
				.auftragpositionFindByPrimaryKey(bestellpositionIId);

		try {

			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(500, 250);

		setTitle(LPMain.getInstance().getTextRespectUISPr(
				"auft.auftragposition.split"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}

			public void windowOpened(WindowEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						wnfMenge.requestFocusInWindow();
					}
				});
			}

		});

		this.addEscapeListener(this);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK),
				"test");
		getRootPane().getActionMap().put("test", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FocusEvent fe = new FocusEvent(wnfMenge, -1);
				focusLost(fe);
				setVisible(false);
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuSpeichern)) {

			try {
				if (wnfMenge.getBigDecimal() == null
						|| wdfLiefertermin.getDate() == null) {

					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
					return;
				}

				// Neuen Artikel anlegen

				auftragpositionIIdNeu = DelegateFactory
						.getInstance()
						.getAuftragpositionDelegate()
						.auftragpositionSplitten(abposDto.getIId(),
								wnfMenge.getBigDecimal(),
								wdfLiefertermin.getTimestamp());

				setVisible(false);

			} catch (Throwable e1) {
				iFrame.handleException(e1, false);
				return;
			}

			this.setVisible(false);

		} else if (e.getSource().equals(wbuAbbrechen)) {
			this.setVisible(false);
		}

	}

	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);
		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.report.save"));
		wbuAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abbrechen"));

		ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(abposDto.getArtikelIId());

		wtfArtikel.setActivatable(false);
		wtfArtikel.setText(aDto.getCNr());

		wtfBezeichnung.setActivatable(false);
		wtfBezeichnung.setText(aDto.formatBezeichnung());

		wnfMenge.setMandatoryField(true);
		wnfMenge.setFractionDigits(0);

		wlaLiefertermin.setText(LPMain
				.getTextRespectUISPr("bes.bestellpositiontermin"));

		wnfMenge.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());

		wnfMenge.setBigDecimal(abposDto.getNMenge());
		wdfLiefertermin.setTimestamp(abposDto.getTUebersteuerbarerLiefertermin());

		wnfMenge.setMinimumValue(0);

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		add(panel1);

		int iZeile = 0;
		panel1.add(wlaArtikel, new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wtfArtikel, new GridBagConstraints(1, iZeile, 2, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 10, 2), 0, 0));
		panel1.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 2, 1, 1.0,
				0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 10, 2), 0, 0));

		iZeile++;
		panel1.add(wlaMenge, new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 150, 0));
		panel1.add(wnfMenge, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(new JLabel(aDto.getEinheitCNr().trim()),
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						20, 0));
		iZeile++;
		wdfLiefertermin.setMandatoryField(true);
		panel1.add(wlaLiefertermin, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wdfLiefertermin, new GridBagConstraints(1, iZeile, 1, 1,
				1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		//

		panel1.add(wbuSpeichern, new GridBagConstraints(0, iZeile, 2, 1, 0,
				1.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(wbuAbbrechen, new GridBagConstraints(2, iZeile, 1, 1, 1,
				1.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 190, 0));

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

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {

	}

}
