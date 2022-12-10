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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ErsatztypenDto;
import com.lp.server.stueckliste.service.PosersatzDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;

@SuppressWarnings("static-access")
public class DialogErsatztypen extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();

	private WrapperRadioButton wrbHauptartikel = new WrapperRadioButton();

	private ArrayList<WrapperRadioButton> alWrbErsatzartikel = new ArrayList<WrapperRadioButton>();
	private ArrayList<WrapperCheckBox> alWcbErsatzartikel = new ArrayList<WrapperCheckBox>();
	private ArrayList<WrapperLabel> alWlaErsatzartikelNummer = new ArrayList<WrapperLabel>();
	private ArrayList<WrapperLabel> alWlaErsatzartikelBezeichnung = new ArrayList<WrapperLabel>();
	private ArrayList<Integer> alArtikelIIdErsatzartikel = new ArrayList<Integer>();

	private ButtonGroup bg = new ButtonGroup();

	private WrapperLabel wlaHauptartikel = new WrapperLabel();
	private WrapperLabel wlaHauptartikelBezeichnung = new WrapperLabel();

	ArtikelDto hauptartikelDto = null;

	StuecklistepositionDto stuecklistepositionDto = null;

	PanelStuecklistepositionen panelStuecklistepositionen = null;

	public boolean bOk = false;
	boolean bErstaufruf=false;

	public DialogErsatztypen(
			PanelStuecklistepositionen panelStuecklistepositionen,
			StuecklistepositionDto stuecklistepositionDto, boolean bErstaufruf) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.stuecklistepositionDto = stuecklistepositionDto;
		this.panelStuecklistepositionen = panelStuecklistepositionen;
		this.bErstaufruf=bErstaufruf;
		try {

			ErsatztypenDto[] ersatztypenDtos = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.ersatztypenfindByArtikelIIdErsatz(
							stuecklistepositionDto.getArtikelIId());

			if (ersatztypenDtos.length == 0) {
				hauptartikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								stuecklistepositionDto.getArtikelIId());
			} else {
				hauptartikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								ersatztypenDtos[0].getArtikelIId());
			}

			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(650, 310);

		setTitle(LPMain.getInstance().getTextRespectUISPr(
				"artikel.tab.ersatztypen"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuSpeichern)) {
			try {
				bOk = true;

				// Hauptartikel setzen

				if (wrbHauptartikel.isSelected()) {
					stuecklistepositionDto.setArtikelDto(hauptartikelDto);
					stuecklistepositionDto.setArtikelIId(hauptartikelDto
							.getIId());
				} else {
					boolean bEsWurdeZumindestEinArtikelSelektiert=false;
					
					for (int i = 0; i < alWrbErsatzartikel.size(); i++) {
						if (alWrbErsatzartikel.get(i).isSelected()) {
							
							bEsWurdeZumindestEinArtikelSelektiert=true;
							
							// Hauptartikel
							stuecklistepositionDto
									.setArtikelDto(DelegateFactory
											.getInstance()
											.getArtikelDelegate()
											.artikelFindByPrimaryKey(
													alArtikelIIdErsatzartikel
															.get(i)));

							stuecklistepositionDto
									.setArtikelIId(alArtikelIIdErsatzartikel
											.get(i));

						}
					}
					//PJ20135
					if(bEsWurdeZumindestEinArtikelSelektiert==false){
						DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
								"lp.info"), LPMain.getInstance().getTextRespectUISPr(
										"artikel.ersatztypen.nochkeinhauptartikel"));
						return;
					}
					
				}

				//SP5272
				panelStuecklistepositionen.getWcbEinheit()
						.setKeyOfSelectedItem(
								stuecklistepositionDto.getArtikelDto()
										.getEinheitCNr());

				// Ersatztypen setzen
				ArrayList<PosersatzDto> posErsatzDtos = new ArrayList<PosersatzDto>();

				for (int i = 0; i < alWcbErsatzartikel.size(); i++) {

					if (alWcbErsatzartikel.get(i).isEnabled()
							&& alWcbErsatzartikel.get(i).isSelected()) {

						Integer artikelIIdErsatzartikel = alArtikelIIdErsatzartikel
								.get(i);
						PosersatzDto posersatzDto = new PosersatzDto();
						posersatzDto
								.setStuecklistepositionIId(stuecklistepositionDto
										.getIId());
						posersatzDto
								.setArtikelIIdErsatz(artikelIIdErsatzartikel);
						posersatzDto.setISort(i);

						posErsatzDtos.add(posersatzDto);
					}

				}

				stuecklistepositionDto.setPosersatzDtos(posErsatzDtos);

				setVisible(false);

				panelStuecklistepositionen.dto2ComponentsArtikelDto();

			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(wbuAbbrechen)) {
			bOk = false;
			this.setVisible(false);
		}

		else if (alWrbErsatzartikel.contains(e.getSource())) {

			for (int i = 0; i < alWrbErsatzartikel.size(); i++) {
				if (alWrbErsatzartikel.get(i).equals(e.getSource())) {

					for (int j = 0; j < alWcbErsatzartikel.size(); j++) {
						alWcbErsatzartikel.get(j).setEnabled(true);

						if (i == j) {
							alWcbErsatzartikel.get(j).setEnabled(false);
						}

					}

				}
			}

		} else if (e.getSource().equals(wrbHauptartikel)
				&& wrbHauptartikel.isSelected()) {

			for (int j = 0; j < alWcbErsatzartikel.size(); j++) {
				alWcbErsatzartikel.get(j).setEnabled(false);

			}

		}

	}

	private void jbInit() throws Throwable {

		ErsatztypenDto[] ersatztypenDtos = DelegateFactory.getInstance()
				.getArtikelDelegate()
				.ersatztypenFindByArtikelIId(hauptartikelDto.getIId());

		ArrayList<PosersatzDto> alVorhanden = null;
		ArrayList<PosersatzDto> alSelektiert = null;

		// Mit vorhandenen mergen
		ArrayList<PosersatzDto> alGemerged = stuecklistepositionDto
				.getPosersatzDtos();

		if (stuecklistepositionDto.getPosersatzDtos() != null) {
			alVorhanden = (ArrayList<PosersatzDto>) stuecklistepositionDto
					.getPosersatzDtos().clone();

			alSelektiert = (ArrayList<PosersatzDto>) stuecklistepositionDto
					.getPosersatzDtos().clone();
			alGemerged = (ArrayList<PosersatzDto>) stuecklistepositionDto
					.getPosersatzDtos().clone();
		}

		if (alVorhanden == null) {
			alVorhanden = new ArrayList<PosersatzDto>();
		}

		if (alSelektiert == null) {
			alSelektiert = new ArrayList<PosersatzDto>();
		}

		if (alGemerged == null) {
			alGemerged = new ArrayList<PosersatzDto>();
		}

		for (int i = 0; i < ersatztypenDtos.length; i++) {

			boolean bVorhanden = false;
			for (int j = 0; j < alVorhanden.size(); j++) {

				PosersatzDto posersatzDtoVorhanden = alVorhanden.get(j);

				if (posersatzDtoVorhanden.getArtikelIIdErsatz().equals(
						ersatztypenDtos[i].getArtikelIIdErsatz())) {
					bVorhanden = true;
				}
			}
			if (bVorhanden == false) {
				PosersatzDto posersatzDto = new PosersatzDto();

				posersatzDto.setStuecklistepositionIId(stuecklistepositionDto
						.getIId());
				posersatzDto.setArtikelIIdErsatz(ersatztypenDtos[i]
						.getArtikelIIdErsatz());
				posersatzDto.setISort(i);

				alGemerged.add(posersatzDto);
			}
		}

		JPanel panel1 = new JPanel(new GridBagLayout());

		JPanel panelHauptartikel = new JPanel(new GridBagLayout());

		Border border = panelHauptartikel.getBorder();
		Border margin = new LineBorder(Color.gray, 1);
		panelHauptartikel.setBorder(new CompoundBorder(border, margin));

		wlaHauptartikel.setText(hauptartikelDto.getCNr());
		wlaHauptartikel.setHorizontalAlignment(SwingConstants.LEFT);

		wlaHauptartikelBezeichnung.setText(hauptartikelDto.formatBezeichnung());

		bg.add(wrbHauptartikel);

		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.ok"));
		wbuAbbrechen
				.setText(LPMain.getInstance().getTextRespectUISPr("Cancel"));

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);
		wrbHauptartikel.addActionListener(this);

		add(panel1);

		int iZeile = 0;

		panel1.add(wrbHauptartikel, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 20, 0));

		panel1.add(wlaHauptartikel, new GridBagConstraints(2, iZeile, 1, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 100, 0));
		panel1.add(wlaHauptartikelBezeichnung, new GridBagConstraints(3,
				iZeile, 1, 1, 1.0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		panel1.add(panelHauptartikel, new GridBagConstraints(0, iZeile, 5, 1,
				1.0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		// Ersatztypen holen
		if(bErstaufruf==false){
			wrbHauptartikel.setSelected(true);
		}
		

		for (int i = 0; i < alGemerged.size(); i++) {

			ArtikelDto aDtoErsatz = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							alGemerged.get(i).getArtikelIIdErsatz());

			WrapperRadioButton wrbErsatzartikel = new WrapperRadioButton();

			wrbErsatzartikel.addActionListener(this);
			WrapperCheckBox wcbErsatzartikel = new WrapperCheckBox();
			WrapperLabel wlaErsatzartikelNummer = new WrapperLabel();
			WrapperLabel wlaErsatzartikelBezeichnung = new WrapperLabel();

			wlaErsatzartikelNummer.setText(aDtoErsatz.getCNr());

			wlaErsatzartikelNummer.setHorizontalAlignment(SwingConstants.LEFT);

			wlaErsatzartikelBezeichnung.setText(aDtoErsatz.formatBezeichnung());

			alWrbErsatzartikel.add(wrbErsatzartikel);

			alWcbErsatzartikel.add(wcbErsatzartikel);
			alWlaErsatzartikelNummer.add(wlaErsatzartikelNummer);
			alWlaErsatzartikelBezeichnung.add(wlaErsatzartikelBezeichnung);

			alArtikelIIdErsatzartikel.add(aDtoErsatz.getIId());

			bg.add(wrbErsatzartikel);

			if (aDtoErsatz.getIId().equals(
					stuecklistepositionDto.getArtikelIId())) {
				wrbErsatzartikel.setSelected(true);
				wcbErsatzartikel.setEnabled(false);
			}

			for (int j = 0; j < alVorhanden.size(); j++) {

				PosersatzDto posersatzDtoVorhanden = alVorhanden.get(j);

				if (posersatzDtoVorhanden.getArtikelIIdErsatz().equals(
						alGemerged.get(i).getArtikelIIdErsatz())) {
					wcbErsatzartikel.setSelected(true);

				}
			}

			panel1.add(wrbErsatzartikel, new GridBagConstraints(0, iZeile, 1,
					1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2), 20, 0));
			panel1.add(wcbErsatzartikel, new GridBagConstraints(1, iZeile, 1,
					1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2), 20, 0));
			panel1.add(wlaErsatzartikelNummer, new GridBagConstraints(2,
					iZeile, 1, 1, 0, 0, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			panel1.add(wlaErsatzartikelBezeichnung, new GridBagConstraints(3,
					iZeile, 1, 1, 1.0, 0, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			iZeile++;
		}

		panel1.add(new WrapperLabel(), new GridBagConstraints(3, iZeile, 1, 1,
				1.0, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		JPanel panelOKAbbrechen = new JPanel(new GridBagLayout());

		panelOKAbbrechen.add(wbuSpeichern, new GridBagConstraints(0, 0, 1, 1,
				0, 0, GridBagConstraints.SOUTH, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 100, 0));
		panelOKAbbrechen.add(wbuAbbrechen, new GridBagConstraints(1, 0, 1, 1,
				0, 0, GridBagConstraints.SOUTH, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 100, 0));

		panel1.add(panelOKAbbrechen, new GridBagConstraints(0, iZeile, 5, 1,
				1.0, 0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 0), 0, 0));

		JScrollPane scrollPane = new JScrollPane(panel1);

		setContentPane(scrollPane);

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

		if (wrbHauptartikel.isSelected()) {

			for (int j = 0; j < alWcbErsatzartikel.size(); j++) {
				alWcbErsatzartikel.get(j).setEnabled(false);
			}

		}

	}

	public void clearAndHide() {
		setVisible(false);
	}

}
