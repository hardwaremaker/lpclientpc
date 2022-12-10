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
package com.lp.client.frame.component;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.service.BelegVerkaufDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogGeaenderteKonditionenZwischenVKBelegen extends JDialog
		implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JButton btnQuellbeleg = new JButton();
	private JButton btnZielbeleg = new JButton();

	public boolean bAbgebrochen = true;
	public boolean bKonditionenUnterschiedlich = false;

	AuftragDto auftragDto = null;
	BelegVerkaufDto belegVerkaufDtoZiel = null;

	public boolean bQuellbelegSelektiert = false;

	InternalFrame internalFrame = null;

	public DialogGeaenderteKonditionenZwischenVKBelegen(AuftragDto auftragDto,
			BelegVerkaufDto belegVerkaufDtoZiel, InternalFrame internalFrame)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("ls.title.panel.konditionen"), true);

		this.internalFrame = internalFrame;
		this.belegVerkaufDtoZiel = belegVerkaufDtoZiel;
		this.auftragDto = auftragDto;

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(Defaults.getInstance().bySizeFactor(700, 290));

	}

	public BelegVerkaufDto getBelegVerkaufDtoZiel() {
		return belegVerkaufDtoZiel;
	}

	private Double berechneRabattAnhandRabattUndProjektierungsrabatt() {
		double dFaktor = 100.0;
		if (auftragDto.getFAllgemeinerRabattsatz() != null) {
			dFaktor = dFaktor - auftragDto.getFAllgemeinerRabattsatz();
		}
		if (auftragDto.getFProjektierungsrabattsatz() != null) {
			double dFaktor2 = 100.0 - auftragDto.getFProjektierungsrabattsatz();
			dFaktor = dFaktor * dFaktor2 / 100.0;
		}
		return new Double(100.0 - dFaktor);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnQuellbeleg)) {
			bQuellbelegSelektiert = true;
			belegVerkaufDtoZiel.setSpediteurIId(auftragDto.getSpediteurIId());

			belegVerkaufDtoZiel.setZahlungszielIId(auftragDto
					.getZahlungszielIId());
			belegVerkaufDtoZiel.setLieferartIId(auftragDto.getLieferartIId());

			belegVerkaufDtoZiel.setFVersteckterAufschlag(auftragDto
					.getFVersteckterAufschlag());

			belegVerkaufDtoZiel
					.setFAllgemeinerRabattsatz(berechneRabattAnhandRabattUndProjektierungsrabatt());

			bAbgebrochen = false;
			this.setVisible(false);
		} else if (e.getSource().equals(btnZielbeleg)) {
			bAbgebrochen = false;
			this.setVisible(false);
		}

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		String zielbeleg=LPMain.getInstance().getTextRespectUISPr(
				"lp.zielbeleg");
		
		if(belegVerkaufDtoZiel.getBelegartCNr()!=null){
			zielbeleg=belegVerkaufDtoZiel.getBelegartCNr().trim();
		} else {
			if(belegVerkaufDtoZiel instanceof RechnungDto){
				zielbeleg=LocaleFac.BELEGART_RECHNUNG.trim();
			}
		}
		
		btnQuellbeleg.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.auftrag"));
		btnZielbeleg.setText(zielbeleg);
		btnQuellbeleg.addActionListener(this);
		btnZielbeleg.addActionListener(this);

		this.getContentPane().setLayout(gridBagLayout2);

		String lieferartZielbelegString = DelegateFactory
				.getInstance()
				.getLocaleDelegate()
				.lieferartFindByPrimaryKey(
						belegVerkaufDtoZiel.getLieferartIId()).formatBez();

		String zahlungszielZielbelegString = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.zahlungszielFindByPrimaryKey(
						belegVerkaufDtoZiel.getZahlungszielIId()).getCBez();

		String lieferartAuftragString = DelegateFactory.getInstance()
				.getLocaleDelegate()
				.lieferartFindByPrimaryKey(auftragDto.getLieferartIId())
				.formatBez();
		String spediteurAuftragString = DelegateFactory.getInstance()
				.getMandantDelegate()
				.spediteurFindByPrimaryKey(auftragDto.getSpediteurIId())
				.getCNamedesspediteurs();
		String zahlungszielBelegString = DelegateFactory.getInstance()
				.getMandantDelegate()
				.zahlungszielFindByPrimaryKey(auftragDto.getZahlungszielIId())
				.getCBez();

		int iZeile = 0;

		WrapperLabel wla = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr(
						"lp.konditionen.auftrag.zielbeleg.weichenab"));
		wla.setHorizontalAlignment(SwingConstants.LEFT);
		this.getContentPane().add(
				wla,
				new GridBagConstraints(0, iZeile, 4, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;
		this.getContentPane().add(
				new WrapperLabel(""),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;
		this.getContentPane().add(
				new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
						"auft.auftrag")),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(zielbeleg),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;

		WrapperLabel wlaLieferart = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.lieferart"));

		if (!auftragDto.getLieferartIId().equals(
				belegVerkaufDtoZiel.getLieferartIId())) {
			wlaLieferart.setForeground(Color.RED);
			bKonditionenUnterschiedlich = true;
		}

		this.getContentPane().add(
				wlaLieferart,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						200, 0));

		this.getContentPane().add(
				new WrapperLabel(lieferartAuftragString),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						250, 0));
		this.getContentPane().add(
				new WrapperLabel(lieferartZielbelegString),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						250, 0));
		this.getContentPane().add(
				new WrapperLabel(""),
				new GridBagConstraints(3, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						100, 0));

		iZeile++;

		WrapperLabel wlaZahlungsziel = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.zahlungsziel"));

		if (!auftragDto.getZahlungszielIId().equals(
				belegVerkaufDtoZiel.getZahlungszielIId())) {
			wlaZahlungsziel.setForeground(Color.RED);
			bKonditionenUnterschiedlich = true;
		}

		this.getContentPane().add(
				wlaZahlungsziel,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(zahlungszielBelegString),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(zahlungszielZielbelegString),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;
		WrapperLabel wlaSpediteur = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.spediteur"));

		if (!auftragDto.getSpediteurIId().equals(
				belegVerkaufDtoZiel.getSpediteurIId())) {
			wlaSpediteur.setForeground(Color.RED);
			bKonditionenUnterschiedlich = true;
		}

		this.getContentPane().add(
				wlaSpediteur,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(spediteurAuftragString),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		String spediteurKundeString = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.spediteurFindByPrimaryKey(
						belegVerkaufDtoZiel.getSpediteurIId())
				.getCNamedesspediteurs();
		this.getContentPane().add(
				new WrapperLabel(spediteurKundeString),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		iZeile++;

		WrapperLabel wlaVersteckterAufschlag = new WrapperLabel(LPMain
				.getInstance()
				.getTextRespectUISPr("label.versteckteraufschlag"));

		if (auftragDto.getFVersteckterAufschlag().doubleValue() != belegVerkaufDtoZiel
				.getFVersteckterAufschlag().doubleValue()) {
			wlaVersteckterAufschlag.setForeground(Color.RED);
			bKonditionenUnterschiedlich = true;
		}

		this.getContentPane().add(
				wlaVersteckterAufschlag,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(Helper.formatZahl(auftragDto
						.getFVersteckterAufschlag(), 2, LPMain.getTheClient()
						.getLocUi())),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(Helper.formatZahl(
						belegVerkaufDtoZiel.getFVersteckterAufschlag(), 2,
						LPMain.getTheClient().getLocUi())),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;

		WrapperLabel wlaAllgRabatt = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.allgemeinerrabatt"));

		//SP9714
		if(belegVerkaufDtoZiel.getFAllgemeinerRabattsatz()==null) {
			belegVerkaufDtoZiel.setFAllgemeinerRabattsatz(0D);
		}
		
		if (berechneRabattAnhandRabattUndProjektierungsrabatt().doubleValue() != belegVerkaufDtoZiel
				.getFAllgemeinerRabattsatz().doubleValue()) {
			wlaAllgRabatt.setForeground(Color.RED);
			bKonditionenUnterschiedlich = true;
		}

		this.getContentPane().add(
				wlaAllgRabatt,
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(Helper.formatZahl(
						berechneRabattAnhandRabattUndProjektierungsrabatt(), 2,
						LPMain.getTheClient().getLocUi())),
				new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		this.getContentPane().add(
				new WrapperLabel(Helper.formatZahl(
						belegVerkaufDtoZiel.getFAllgemeinerRabattsatz(), 2,
						LPMain.getTheClient().getLocUi())),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));
		iZeile++;

		WrapperLabel wlaWelche = new WrapperLabel(
				LPMain.getInstance()
						.getTextRespectUISPr(
								"ls.sammellieferschein.konditionenweichenab.welche.uebernehmen"));
		wlaWelche.setHorizontalAlignment(SwingConstants.LEFT);

		this.getContentPane().add(
				wlaWelche,
				new GridBagConstraints(0, iZeile, 3, 1, 1, 0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 5),
						0, 0));

		// --------------------------

		iZeile++;

		this.getContentPane().add(
				btnQuellbeleg,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));
		this.getContentPane().add(
				btnZielbeleg,
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));

	}

}
