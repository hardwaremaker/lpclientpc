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
package com.lp.client.bestellung;

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
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.ButtonAbstractAction;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.GebindeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogGebindeUmrechnung extends JDialog implements ActionListener,
		FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	WrapperKeyValueField wkvLieferant = new WrapperKeyValueField(Defaults
			.getInstance().bySizeFactor(100));
	WrapperKeyValueField wkvArtikel = new WrapperKeyValueField(Defaults
			.getInstance().bySizeFactor(100));
	WrapperKeyValueField wkvPosmengeVorhanden = new WrapperKeyValueField(
			Defaults.getInstance().bySizeFactor(100));

	WrapperNumberField wnfAnzahlGebinde = new WrapperNumberField();
	WrapperComboBox wcbGebinde = new WrapperComboBox();
	WrapperNumberField wnfPosMengeBerechnet = new WrapperNumberField();

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuKeinGebinde = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();
	BigDecimal bdPosmenge = null;

	private Integer artikelIId = null;
	private Integer lieferantIId = null;
	private Integer gebindeIId_Vorhanden = null;

	private boolean bKeinGebinde = false;

	public boolean isKeinGebinde() {
		return bKeinGebinde;
	}

	ArrayList<GebindeDto> alGebinde = null;

	private GebindeDto gebindeDtoSelektiert = null;
	private BigDecimal bdNeuePositionsmenge = null;
	private BigDecimal bdAnzahlGebinde = null;

	public BigDecimal getBdAnzahlGebinde() {
		return bdAnzahlGebinde;
	}

	public GebindeDto getGebindeDtoSelektiert() {
		return gebindeDtoSelektiert;
	}

	public BigDecimal getBdNeuePositionsmenge() {
		return bdNeuePositionsmenge;
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

	public DialogGebindeUmrechnung(Integer artikelIId, Integer lieferantIId,
			ArrayList<GebindeDto> alGebinde, BigDecimal bdPosmenge,
			Integer gebindeIId_Vorhanden) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.bdPosmenge = bdPosmenge;
		this.artikelIId = artikelIId;
		this.lieferantIId = lieferantIId;
		this.alGebinde = alGebinde;
		this.gebindeIId_Vorhanden = gebindeIId_Vorhanden;
		try {

			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(400, 250);

		setTitle(LPMain.getInstance().getTextRespectUISPr("artikel.gebinde"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}

			public void windowOpened(WindowEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						wnfAnzahlGebinde.requestFocusInWindow();
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
				FocusEvent fe = new FocusEvent(wnfAnzahlGebinde, -1);
				focusLost(fe);
				saveAndExit();
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuSpeichern)) {
			saveAndExit();

		} else if (e.getSource().equals(wbuAbbrechen)) {
			this.setVisible(false);
		} else if (e.getSource().equals(wbuKeinGebinde)) {
			bKeinGebinde = true;
			this.setVisible(false);
		}

		if (e.getSource().equals(wcbGebinde)) {
			Integer gebindeIIdSelektiert = (Integer) wcbGebinde
					.getKeyOfSelectedItem();
			BigDecimal bdGebindemenge = BigDecimal.ONE;

			for (int i = 0; i < alGebinde.size(); i++) {

				GebindeDto gDto = alGebinde.get(i);
				if (gebindeIIdSelektiert.equals(gDto.getIId())) {
					bdGebindemenge = gDto.getNMenge();
					break;
				}
			}

			try {

				if (bdGebindemenge.doubleValue() != 0) {
					BigDecimal bdGebindeAnzahl = bdPosmenge.divide(
							bdGebindemenge, 10, BigDecimal.ROUND_HALF_EVEN);

					bdGebindeAnzahl = new BigDecimal(Math.ceil(bdGebindeAnzahl
							.doubleValue()));
					wnfAnzahlGebinde.setBigDecimal(bdGebindeAnzahl);
					wnfPosMengeBerechnet.setBigDecimal(bdGebindeAnzahl
							.multiply(bdGebindemenge));
				}

			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	public void saveAndExit() {
		try {
			if (wnfPosMengeBerechnet.getBigDecimal() != null) {

				bdNeuePositionsmenge = wnfPosMengeBerechnet.getBigDecimal();
				bdAnzahlGebinde = wnfAnzahlGebinde.getBigDecimal();
				Integer gebindeIId = (Integer) wcbGebinde
						.getKeyOfSelectedItem();

				for (int i = 0; i < alGebinde.size(); i++) {

					GebindeDto gDto = alGebinde.get(i);
					if (gebindeIId.equals(gDto.getIId())) {
						gebindeDtoSelektiert = gDto;
						break;
					}
				}

				this.setVisible(false);

			}

		} catch (ExceptionLP e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);
		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.report.save"));
		wbuAbbrechen
				.setText(LPMain.getInstance().getTextRespectUISPr("Cancel"));

		wbuKeinGebinde.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.gebinde.keingebinde"));

		wkvArtikel.setKey(LPMain.getInstance().getTextRespectUISPr(
				"artikel.gebindedialog.artikel"));

		ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(artikelIId);
		wkvArtikel.setValue(aDto.formatArtikelbezeichnung());

		LieferantDto lieferantDto = DelegateFactory.getInstance()
				.getLieferantDelegate().lieferantFindByPrimaryKey(lieferantIId);

		wkvLieferant.setKey(LPMain.getInstance().getTextRespectUISPr(
				"artikel.gebindedialog.lieferant"));
		wkvLieferant.setValue(lieferantDto.getPartnerDto().formatAnrede());

		WrapperLabel wlaEinheit = new WrapperLabel(aDto.getEinheitCNr().trim());
		wlaEinheit.setHorizontalAlignment(SwingConstants.LEFT);

		wkvPosmengeVorhanden.setKey(LPMain.getInstance().getTextRespectUISPr(
				"artikel.gebindedialog.posmenge"));
		if (bdPosmenge != null) {
			wkvPosmengeVorhanden.setValue(Helper.formatZahl(bdPosmenge,
					Defaults.getInstance().getIUINachkommastellenMenge(),
					LPMain.getInstance().getUISprLocale()));
		}
		wcbGebinde.setMandatoryField(true);
		wnfAnzahlGebinde.setMandatoryField(true);
		wnfPosMengeBerechnet.setMandatoryField(true);
		wnfPosMengeBerechnet.setActivatable(false);

		Map m = new LinkedHashMap();

		for (int i = 0; i < alGebinde.size(); i++) {
			GebindeDto gebindeDto = alGebinde.get(i);

			String gebinde = gebindeDto.getCBez()
					+ " ("
					+ Helper.formatZahl(gebindeDto.getNMenge(), Defaults
							.getInstance().getIUINachkommastellenMenge(),
							LPMain.getInstance().getUISprLocale()) + ")";

			m.put(gebindeDto.getIId(), gebinde);
		}

		wcbGebinde.setMap(m);

		if (gebindeIId_Vorhanden != null) {
			wcbGebinde.setKeyOfSelectedItem(gebindeIId_Vorhanden);
		}

		Integer gebindeIIdSelektiert = (Integer) wcbGebinde
				.getKeyOfSelectedItem();
		BigDecimal bdGebindemenge = BigDecimal.ONE;

		for (int i = 0; i < alGebinde.size(); i++) {

			GebindeDto gDto = alGebinde.get(i);
			if (gebindeIIdSelektiert.equals(gDto.getIId())) {
				bdGebindemenge = gDto.getNMenge();
				break;
			}
		}

		if (bdGebindemenge.doubleValue() != 0) {
			BigDecimal bdGebindeAnzahl = bdPosmenge.divide(bdGebindemenge, 10,
					BigDecimal.ROUND_HALF_EVEN);

			bdGebindeAnzahl = new BigDecimal(Math.ceil(bdGebindeAnzahl
					.doubleValue()));
			wnfAnzahlGebinde.setBigDecimal(bdGebindeAnzahl);
			wnfPosMengeBerechnet.setBigDecimal(bdGebindeAnzahl
					.multiply(bdGebindemenge));
		}

		wcbGebinde.addActionListener(this);
		wnfAnzahlGebinde.addFocusListener(this);

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);
		wbuKeinGebinde.addActionListener(this);

		add(panel1);
		panel1.add(wkvArtikel, new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panel1.add(wkvLieferant, new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		panel1.add(wkvPosmengeVorhanden, new GridBagConstraints(0, 2, 3, 1,
				1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		//
		panel1.add(wnfAnzahlGebinde, new GridBagConstraints(0, 3, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 2, 0, 2), -50, 0));
		panel1.add(wcbGebinde, new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 2, 0, 2), 100, 0));
		panel1.add(wnfPosMengeBerechnet, new GridBagConstraints(2, 3, 1, 1, 0,
				0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 2, 0, 2), -40, 0));
		panel1.add(wlaEinheit, new GridBagConstraints(3, 3, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 2, 0, 2), 20, 0));

		//

		panel1.add(wbuSpeichern, new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(wbuKeinGebinde, new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(wbuAbbrechen, new GridBagConstraints(2, 5, 2, 1, .5, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

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
		if (e.getSource().equals(wnfAnzahlGebinde)) {

			Integer gebindeIId = (Integer) wcbGebinde.getKeyOfSelectedItem();

			for (int i = 0; i < alGebinde.size(); i++) {

				GebindeDto gDto = alGebinde.get(i);
				if (gebindeIId.equals(gDto.getIId())) {

					alGebinde.get(i).getNMenge();
					try {
						wnfPosMengeBerechnet.setBigDecimal(gDto.getNMenge()
								.multiply(wnfAnzahlGebinde.getBigDecimal()));
					} catch (ExceptionLP e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				}

			}

		}

	}

}
