package com.lp.client.personal;

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

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.UrlaubsabrechnungDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogUrlaubsanspruchZumEintrittsdatum extends JDialog implements
		KeyListener, PropertyChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panelUrlaubsanspruch = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	WrapperLabel wlaAnspruchAlt = new WrapperLabel();
	WrapperLabel wlaAnspruchAliquot = new WrapperLabel();
	WrapperLabel wrapperLabel4 = new WrapperLabel();
	WrapperLabel wlaAnspruch = new WrapperLabel();
	WrapperNumberField wnfAnspruchAltTage = null;
	WrapperNumberField wnfAnspruchAliqotTage = null;
	WrapperNumberField wnfAnspruchAktuellVerbrauchtTage = null;
	WrapperLabel wlaStrich = new WrapperLabel();
	WrapperLabel wlaTage = new WrapperLabel();
	WrapperLabel wlaStunden = new WrapperLabel();
	WrapperNumberField wnfAnspruchAltStunden = null;
	WrapperNumberField wnfAnspruchAliqoutStunden = null;
	WrapperNumberField wnfAnspruchAktuellVerbrauchtStunden = null;
	WrapperLabel wlaTitel = new WrapperLabel();

	WrapperNumberField wnfUrlaubGeplantTage = null;
	WrapperNumberField wnfUrlaubGeplantStunden = null;
	private WrapperNumberField wnfNochNichtVerbrauchtTage = null;
	private WrapperNumberField wnfNochNichtVerbrauchtStunden = null;

	private WrapperLabel wlaNochNichtVerbraucht = new WrapperLabel();
	private WrapperLabel wlaUrlaubGeplant = new WrapperLabel();
	private WrapperDateField wdfAbrechnungszeitpunkt = new WrapperDateField();

	private Integer personalIId = null;

	GridBagLayout gridBagLayout2 = new GridBagLayout();

	public DialogUrlaubsanspruchZumEintrittsdatum() {

	}

	private void aktualisiereUrlaubsanspruch() throws Throwable {

		UrlaubsabrechnungDto urlaubsabrechnungDto = DelegateFactory
				.getInstance()
				.getZeiterfassungDelegate()
				.berechneUrlaubsAnspruch(personalIId,
						wdfAbrechnungszeitpunkt.getDate());

		wnfAnspruchAliqoutStunden.setBigDecimal(urlaubsabrechnungDto
				.getNAliquoterAnspruchStunden());
		wnfAnspruchAliqotTage.setBigDecimal(urlaubsabrechnungDto
				.getNAliquoterAnspruchTage());
		wnfAnspruchAktuellVerbrauchtStunden.setBigDecimal(urlaubsabrechnungDto
				.getNAktuellerUrlaubVerbrauchtStunden());
		wnfAnspruchAktuellVerbrauchtTage.setBigDecimal(urlaubsabrechnungDto
				.getNAktuellerUrlaubVerbrauchtTage());
		wnfAnspruchAltStunden.setBigDecimal(urlaubsabrechnungDto
				.getNAlterUrlaubsanspruchStunden());
		wnfAnspruchAltTage.setBigDecimal(urlaubsabrechnungDto
				.getNAlterUrlaubsanspruchTage());

		wnfNochNichtVerbrauchtTage.setBigDecimal(urlaubsabrechnungDto
				.getNAlterUrlaubsanspruchTage()
				.add(urlaubsabrechnungDto.getNAliquoterAnspruchTage())
				.subtract(
						urlaubsabrechnungDto
								.getNAktuellerUrlaubVerbrauchtTage()));
		wnfNochNichtVerbrauchtStunden.setBigDecimal(urlaubsabrechnungDto
				.getNAlterUrlaubsanspruchStunden()
				.add(urlaubsabrechnungDto.getNAliquoterAnspruchStunden())
				.subtract(
						urlaubsabrechnungDto
								.getNAktuellerUrlaubVerbrauchtStunden()));

		wnfUrlaubGeplantTage.setBigDecimal(urlaubsabrechnungDto
				.getNGeplanterUrlaubTage());
		wnfUrlaubGeplantStunden.setBigDecimal(urlaubsabrechnungDto
				.getNGeplanterUrlaubStunden());

		wrapperLabel4
				.setText("- "
						+ LPMain.getInstance()
								.getMessageTextRespectUISPr(
										"pers.urlaubsanspruch.anspruchaktuellverbrauchtbiszum",
										Helper.formatDatum(
												wdfAbrechnungszeitpunkt
														.getDate(), LPMain
														.getInstance()
														.getTheClient()
														.getLocUi())) + ":");
		
		wlaUrlaubGeplant
		.setText(LPMain.getInstance()
				.getMessageTextRespectUISPr(
						"pers.urlaubsanspruch.urlaubgeplantnach",
						Helper.formatDatum(
								wdfAbrechnungszeitpunkt.getDate(),
								LPMain.getInstance().getTheClient()
										.getLocUi())));

	}

	public DialogUrlaubsanspruchZumEintrittsdatum(Integer personalIId)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("pers.urlaubsanspruch.urlaubsabrechnung"),
				true);

		this.personalIId = personalIId;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		pack();

		wdfAbrechnungszeitpunkt.setMinimumValue(new Date(System
				.currentTimeMillis()));

		Calendar c = Calendar.getInstance();

		c.set(Calendar.MONTH, Calendar.DECEMBER);
		c.set(Calendar.DAY_OF_MONTH, 31);

		wdfAbrechnungszeitpunkt.setDate(c.getTime());
		wdfAbrechnungszeitpunkt.setMandatoryField(true);
		wdfAbrechnungszeitpunkt.addPropertyChangeListener(this);

		wlaTitel.setText(wlaTitel.getText() + " ");
		aktualisiereUrlaubsanspruch();

	}

	public void propertyChange(PropertyChangeEvent e) {
		if (e.getSource() == wdfAbrechnungszeitpunkt
				&& e.getNewValue() instanceof Date
				&& e.getPropertyName().equals("date")) {
			wdfAbrechnungszeitpunkt.setDate((Date) e.getNewValue());

			try {
				aktualisiereUrlaubsanspruch();
			} catch (Throwable ex) {
				// brauche ich

			}
		}
	}

	private void jbInit() throws Throwable {

		wnfUrlaubGeplantTage = new WrapperNumberField();
		wnfUrlaubGeplantStunden = new WrapperNumberField();

		wnfAnspruchAltTage = new WrapperNumberField();
		wnfAnspruchAliqotTage = new WrapperNumberField();
		wnfAnspruchAktuellVerbrauchtTage = new WrapperNumberField();
		wnfAnspruchAltStunden = new WrapperNumberField();
		wnfAnspruchAliqoutStunden = new WrapperNumberField();
		wnfAnspruchAktuellVerbrauchtStunden = new WrapperNumberField();

		wnfNochNichtVerbrauchtTage = new WrapperNumberField();
		wnfNochNichtVerbrauchtStunden = new WrapperNumberField();

		panelUrlaubsanspruch.setLayout(gridBagLayout1);
		wlaAnspruchAlt.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.urlaubsanspruch.anspruchalt")
				+ ":");
		wlaUrlaubGeplant
				.setText(LPMain.getInstance()
						.getMessageTextRespectUISPr(
								"pers.urlaubsanspruch.urlaubgeplantnach",
								Helper.formatDatum(
										wdfAbrechnungszeitpunkt.getDate(),
										LPMain.getInstance().getTheClient()
												.getLocUi()))
						+ ":");
		wlaAnspruchAliquot.setText("+ "
				+ LPMain.getInstance().getMessageTextRespectUISPr(
						"pers.urlaubsanspruch.anspruchaliquot") + ":");
		wrapperLabel4
				.setText("- "
						+ LPMain.getInstance()
								.getMessageTextRespectUISPr(
										"pers.urlaubsanspruch.anspruchaktuellverbrauchtbiszum",
										Helper.formatDatum(
												wdfAbrechnungszeitpunkt
														.getDate(), LPMain
														.getInstance()
														.getTheClient()
														.getLocUi())) + ":");
		wlaAnspruch.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.urlaubsanspruch.betrachtungsart")
				+ ":");
		wlaStrich
				.setText("----------------------------------------------------------------------------------------------");
		wlaTage.setHorizontalAlignment(SwingConstants.CENTER);
		wlaTage.setText(LPMain.getInstance().getTextRespectUISPr("lp.tage"));
		wlaStunden.setHorizontalAlignment(SwingConstants.CENTER);
		wlaStunden.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.stunden"));
		wlaTitel.setFont(new java.awt.Font("Dialog", Font.BOLD, 11));
		wlaTitel.setHorizontalAlignment(SwingConstants.RIGHT);
		wlaTitel.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.urlaubsanspruch.anspruchzum"));

		wlaNochNichtVerbraucht
				.setText(LPMain.getInstance().getTextRespectUISPr(
						"pers.urlaubsanspruch.nochnichtverbraucht")
						+ ":");

		wnfAnspruchAltStunden.setEditable(false);
		wnfAnspruchAliqoutStunden.setEditable(false);
		wnfAnspruchAktuellVerbrauchtStunden.setEditable(false);

		wnfAnspruchAltTage.setEditable(false);
		wnfAnspruchAliqotTage.setEditable(false);
		wnfAnspruchAktuellVerbrauchtTage.setEditable(false);

		wnfNochNichtVerbrauchtTage.setEditable(false);
		wnfNochNichtVerbrauchtStunden.setEditable(false);

		wnfUrlaubGeplantStunden.setEditable(false);
		wnfUrlaubGeplantTage.setEditable(false);

		this.getContentPane().setLayout(gridBagLayout2);
		this.getContentPane().add(
				panelUrlaubsanspruch,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 200, 150));
		panelUrlaubsanspruch.add(wnfAnspruchAltStunden, new GridBagConstraints(
				2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wrapperLabel4, new GridBagConstraints(0, 7, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wnfAnspruchAktuellVerbrauchtTage,
				new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wnfAnspruchAktuellVerbrauchtStunden,
				new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wlaStrich, new GridBagConstraints(0, 8, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wlaTage, new GridBagConstraints(1, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaStunden, new GridBagConstraints(2, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wnfAnspruchAltTage, new GridBagConstraints(1,
				2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaAnspruch, new GridBagConstraints(0, 1, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaTitel, new GridBagConstraints(0, 0, 1, 1,
				0.0, 0.0, GridBagConstraints.SOUTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wdfAbrechnungszeitpunkt,
				new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.SOUTH,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		panelUrlaubsanspruch.add(wlaAnspruchAlt, new GridBagConstraints(0, 2,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));

		panelUrlaubsanspruch.add(wlaAnspruchAliquot, new GridBagConstraints(0,
				6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wnfAnspruchAliqotTage, new GridBagConstraints(
				1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wnfAnspruchAliqoutStunden,
				new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wlaNochNichtVerbraucht,
				new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		panelUrlaubsanspruch.add(wnfNochNichtVerbrauchtTage,
				new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wnfNochNichtVerbrauchtStunden,
				new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(new WrapperLabel(), new GridBagConstraints(0,
				10, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wlaUrlaubGeplant, new GridBagConstraints(0,
				11, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wnfUrlaubGeplantTage, new GridBagConstraints(
				1, 11, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wnfUrlaubGeplantStunden,
				new GridBagConstraints(2, 11, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		Component[] comps = panelUrlaubsanspruch.getComponents();

		for (int i = 0; i < comps.length; i++) {
			comps[i].addKeyListener(this);
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.dispose();
		}

	}

	public void keyReleased(KeyEvent e) {
	}
}
