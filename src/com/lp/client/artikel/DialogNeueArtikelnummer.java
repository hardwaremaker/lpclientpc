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
package com.lp.client.artikel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

@SuppressWarnings("static-access")
public class DialogNeueArtikelnummer extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private WrapperLabel lNeueArtikelnummer = new WrapperLabel();
	private WrapperTextField neueArtikelnummer = new WrapperTextField();
	private JButton btnOK = new JButton();
	private JButton btnAbbrechen = new JButton();
	private WrapperLabel lArtikelnummerVorschlag = new WrapperLabel();
	private WrapperLabel lVorschlag = new WrapperLabel();

	private WrapperButton wbuGeneriereArtikelnummer = new WrapperButton();

	public DialogNeueArtikelnummer(InternalFrame internalFrame) throws Throwable {
		super(
				LPMain.getInstance().getDesktop(),
				LPMain.getInstance()
						.getTextRespectUISPr("angebot.bearbeiten.handartikelumwandeln.neueartikelnummer"),
				true);

		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(Defaults.getInstance().bySizeFactor(500, 150));
		neueArtikelnummer.grabFocus();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuGeneriereArtikelnummer)) {
			try {
				neueArtikelnummer
						.setText(DelegateFactory
								.getInstance()
								.getArtikelDelegate()
								.generiereNeueArtikelnummer(
										neueArtikelnummer.getText()));
			} catch (Throwable ex) {
				LPMain.getInstance().exitFrame(null, ex);
			}
		} else {
			if (e.getSource().equals(btnAbbrechen)) {
				neueArtikelnummer.setText(null);
				this.setVisible(false);
			} else {
				this.setVisible(false);
			}

		}

	}

	public String getArtikelnummerNeu() throws Throwable {
		String artikelnummerNeu = neueArtikelnummer.getText();
		return artikelnummerNeu;
	}
	
	public void setArtikelVorschlag(ArtikelDto artikelDto) {
		if (artikelDto == null) return;
		neueArtikelnummer.setText(artikelDto.getCNr());
		lVorschlag.setText(LPMain.getTextRespectUISPr("dialog.neueartikelnummer.vorschlag"));
		lVorschlag.setForeground(new Color(150, 150, 150));
		lArtikelnummerVorschlag.setText("[" + artikelDto.getCNr() + "] " + artikelDto.getArtikelsprDto().getCBez());
		lArtikelnummerVorschlag.setForeground(new Color(150, 150, 150));
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			neueArtikelnummer.setText(null);
			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getInstance().getTheClient().getMandant());

		if (parameter.getCWertAsObject() != null) {
			neueArtikelnummer.setColumnsMax(((Integer) parameter
					.getCWertAsObject()).intValue());
		}
		lNeueArtikelnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelkopieren.neueartikelnummer"));
		lArtikelnummerVorschlag.setHorizontalAlignment(SwingConstants.LEFT);

		btnOK.setText(LPMain.getInstance().getTextRespectUISPr("button.ok"));
		btnAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abbrechen"));
		neueArtikelnummer.setUppercaseField(true);
		btnOK.addActionListener(this);
		btnAbbrechen.addActionListener(this);

		wbuGeneriereArtikelnummer.setText("G");
		wbuGeneriereArtikelnummer.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("artikel.generiereartikelnummer"));
		wbuGeneriereArtikelnummer.addActionListener(this);

		this.getContentPane().setLayout(new MigLayout("wrap 3, hidemode 3", "[fill, grow 30||fill,grow 70]"));
		this.getContentPane().add(lNeueArtikelnummer);
		this.getContentPane().add(wbuGeneriereArtikelnummer, "width 50");
		this.getContentPane().add(neueArtikelnummer);
		this.getContentPane().add(lVorschlag, "span 2");
		this.getContentPane().add(lArtikelnummerVorschlag, "align left");
		JPanel okPanel = new JPanel();
		okPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		okPanel.add(btnOK);

		JPanel abbrechenPanel = new JPanel();
		abbrechenPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		abbrechenPanel.add(btnAbbrechen);
		this.getContentPane().add(okPanel, "span 2, align right");
		this.getContentPane().add(abbrechenPanel);
		
//		this.getContentPane().setLayout(gridBagLayout2);
//
//		int iZeile = 0;
//
//		this.getContentPane().add(
//				lNeueArtikelnummer,
//				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
//						GridBagConstraints.CENTER,
//						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
//						100, 0));
//
//		this.getContentPane().add(
//				wbuGeneriereArtikelnummer,
//				new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
//						GridBagConstraints.EAST, GridBagConstraints.NONE,
//						new Insets(5, 5, 5, 5), 20, 0));
//		this.getContentPane().add(
//				neueArtikelnummer,
//				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
//						GridBagConstraints.CENTER,
//						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
//						200, 0));
//
//		iZeile++;
//		this.getContentPane().add(
//				lVorschlag,
//				new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
//						GridBagConstraints.CENTER,
//						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
//						200, 0));
//		this.getContentPane().add(
//				lArtikelnummerVorschlag,
//				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
//						GridBagConstraints.CENTER,
//						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
//						200, 0));
//
//		iZeile++;
//
//		this.getContentPane().add(
//				btnOK,
//				new GridBagConstraints(0, iZeile, 2, 1, 0, 0,
//						GridBagConstraints.EAST, GridBagConstraints.NONE,
//						new Insets(5, 5, 5, 5), 0, 0));
//		this.getContentPane().add(
//				btnAbbrechen,
//				new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
//						GridBagConstraints.WEST, GridBagConstraints.NONE,
//						new Insets(5, 5, 5, 5), 0, 0));
//	
	}

}
