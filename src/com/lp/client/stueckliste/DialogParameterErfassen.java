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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.ItemId;
import com.lp.server.stueckliste.service.StklparameterDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.util.KundeId;
import com.lp.util.Helper;

public class DialogParameterErfassen extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private Map<String, Object> parameterMap = null;

	private boolean bProduktstuecklisteErzeugen = false;

	public boolean isbProduktstuecklisteErzeugen() {
		return bProduktstuecklisteErzeugen;
	}

	private JButton wbuAbbrechen = new JButton();

	private JButton wbuStklErzeugen = new JButton();

	private JButton wbuOK = new JButton();

	private JScrollPane jspScrollPane = new JScrollPane();

	private StklparameterDto[] stklparameterDto = null;
	private Component[] eingabefelder;

	private WrapperIdentField[] artikelauswahlen;
	private WrapperSelectField[] kundeauswehlen;

	private TabbedPaneStueckliste tpStkl = null;

	public DialogParameterErfassen(TabbedPaneStueckliste tpStkl) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "Konfigurieren", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.tpStkl = tpStkl;

		jbInit();
		pack();
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(this);

	}

	private void jbInit() throws Throwable {
		panel.setLayout(gridBagLayout1);

		
		
		
		wbuOK.setText(LPMain.getTextRespectUISPr("stkl.produktsklerzeugen.vorkalkulation"));

		wbuAbbrechen.setText(LPMain.getTextRespectUISPr("lp.abbrechen"));

		wbuStklErzeugen.setText(LPMain.getTextRespectUISPr("stkl.produktsklerzeugen"));

		wbuStklErzeugen.addActionListener(this);
		wbuStklErzeugen.setMnemonic('p');

		wbuOK.addActionListener(this);
		wbuOK.setMnemonic('o');

		setSize(500, 500);

		wbuAbbrechen.addActionListener(this);

		this.getContentPane().setLayout(gridBagLayout2);

		this.getContentPane().add(panel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 250, 50));

		panel.add(jspScrollPane, new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));

		stklparameterDto = DelegateFactory.getInstance().getStuecklisteDelegate()
				.stklparameterFindByStuecklisteIId(tpStkl.getInternalFrameStueckliste().getStuecklisteDto().getIId());

		JPanel panelWerte = new JPanel(new GridBagLayout());

		eingabefelder = new Component[stklparameterDto.length];
		artikelauswahlen = new WrapperIdentField[stklparameterDto.length];
		kundeauswehlen = new WrapperSelectField[stklparameterDto.length];

		for (int i = 0; i < stklparameterDto.length; i++) {

			stklparameterDto[i] = DelegateFactory.getInstance().getStuecklisteDelegate()
					.stklparameterFindByPrimaryKey(stklparameterDto[i].getIId());

			Component c = null;
			WrapperIdentField artikelauswahl = null;
			WrapperSelectField kundenauswahl = null;

			if (stklparameterDto[i].getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_STRING)) {

				if (Helper.short2boolean(stklparameterDto[i].getBCombobox())) {
					WrapperComboBox wcb = new WrapperComboBox();
					wcb.setMandatoryField(Helper.short2boolean(stklparameterDto[i].getBMandatory()));
					if (stklparameterDto[i].getCBereich() != null) {
						Map<String, String> m = new LinkedHashMap<String, String>();
						StringTokenizer token = new StringTokenizer(stklparameterDto[i].getCBereich(), "|");
						while (token.hasMoreTokens()) {
							String wert = token.nextToken();
							m.put(wert, wert);
						}
						wcb.setMap(m);
					}

					c = wcb;

				} else {

					WrapperTextField wtf = new WrapperTextField();
					wtf.setMandatoryField(Helper.short2boolean(stklparameterDto[i].getBMandatory()));
					c = wtf;
				}

			} else if (stklparameterDto[i].getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_INTEGER)) {

				WrapperNumberField wnf = new WrapperNumberField();
				wnf.setMinimumValue(stklparameterDto[i].getNMin());
				wnf.setMaximumValue(stklparameterDto[i].getNMax());
				wnf.setFractionDigits(0);
				wnf.setMandatoryField(Helper.short2boolean(stklparameterDto[i].getBMandatory()));

				c = wnf;
			} else if (stklparameterDto[i].getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_DOUBLE)
					|| stklparameterDto[i].getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_BIGDECIMAL)) {
				WrapperNumberField wnf = new WrapperNumberField();
				wnf.setMinimumValue(stklparameterDto[i].getNMin());
				wnf.setMaximumValue(stklparameterDto[i].getNMax());
				wnf.setFractionDigits(2);
				wnf.setMandatoryField(Helper.short2boolean(stklparameterDto[i].getBMandatory()));
				c = wnf;
			} else if (stklparameterDto[i].getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_BOOLEAN)) {
				c = new WrapperCheckBox();
			} else if (stklparameterDto[i].getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_ITEM_ID)) {
				artikelauswahl = new WrapperIdentField(tpStkl.getInternalFrame(), null);
				artikelauswahl.getWtfIdent()
						.setMandatoryField(Helper.short2boolean(stklparameterDto[i].getBMandatory()));
			} else if (stklparameterDto[i].getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_KUNDE_ID)) {
				kundenauswahl = new WrapperSelectField(WrapperSelectField.KUNDE, tpStkl.getInternalFrame(),
						Helper.short2boolean(stklparameterDto[i].getBMandatory()));
				
				kundenauswahl.setMandatoryField(Helper.short2boolean(stklparameterDto[i].getBMandatory()));

			}

			if (artikelauswahl != null) {
				artikelauswahl.getWbuArtikel().setText(stklparameterDto[i].getCNr());

				panelWerte.add(artikelauswahl.getWbuArtikel(), new GridBagConstraints(0, i, 1, 1, .5, 0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 0, 2), 0, 0));

			} else if (kundenauswahl != null) {
				kundenauswahl.getWrapperButton().setText(stklparameterDto[i].getCNr());

				panelWerte.add(kundenauswahl.getWrapperButton(), new GridBagConstraints(0, i, 1, 1, .5, 0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 0, 2), 0, 0));

			} else {
				JLabel label = new JLabel(stklparameterDto[i].getCNr());

				panelWerte.add(label, new GridBagConstraints(0, i, 1, 1, .5, 0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 2, 0, 2), 0, 0));
			}

			String bezeichnung = "";

			if (stklparameterDto[i].getStklparametersprDto() != null
					&& stklparameterDto[i].getStklparametersprDto().getCBez() != null) {
				bezeichnung = stklparameterDto[i].getStklparametersprDto().getCBez();
			}
			JLabel labelBez = new JLabel(bezeichnung);

			panelWerte.add(labelBez, new GridBagConstraints(1, i, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 2, 0, 2), 0, 0));

			if (c != null) {

				eingabefelder[i] = c;

				panelWerte.add(c, new GridBagConstraints(2, i, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			}

			if (artikelauswahl != null) {

				artikelauswahlen[i] = artikelauswahl;

				artikelauswahl.getWtfIdent().setEditable(false);
				panelWerte.add(artikelauswahl.getWtfIdent(), new GridBagConstraints(2, i, 1, 1, 1.0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			} else if (kundenauswahl != null) {

				kundeauswehlen[i] = kundenauswahl;

				kundenauswahl.getWrapperTextField().setEditable(false);
				panelWerte.add(kundenauswahl.getWrapperTextField(), new GridBagConstraints(2, i, 1, 1, 1.0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			}

		}

		jspScrollPane.getViewport().add(panelWerte, null);

		panel.add(wbuOK, new GridBagConstraints(0, 4, 1, 1, 1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 80, 0));
		panel.add(wbuAbbrechen, new GridBagConstraints(1, 4, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 30, 0));

		panel.add(wbuStklErzeugen, new GridBagConstraints(2, 4, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

	}

	public Map<String, Object> getParameter() {
		return parameterMap;
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(wbuOK) || e.getSource().equals(wbuStklErzeugen)) {
			try {

				if (e.getSource().equals(wbuStklErzeugen)) {
					bProduktstuecklisteErzeugen = true;
				} else {
					bProduktstuecklisteErzeugen = false;
				}

				LinkedHashMap<String, Object> parameterMapLocal = new LinkedHashMap<String, Object>();
				for (int i = 0; i < stklparameterDto.length; i++) {

					if (stklparameterDto[i].getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_STRING)) {

						if (Helper.short2boolean(stklparameterDto[i].getBCombobox())) {
							parameterMapLocal.put(stklparameterDto[i].getCNr(),
									((WrapperComboBox) eingabefelder[i]).getKeyOfSelectedItem());
						} else {
							parameterMapLocal.put(stklparameterDto[i].getCNr(),
									((WrapperTextField) eingabefelder[i]).getText());
						}

					} else if (stklparameterDto[i].getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_INTEGER)) {

						parameterMapLocal.put(stklparameterDto[i].getCNr(),
								((WrapperNumberField) eingabefelder[i]).getInteger());
					} else if (stklparameterDto[i].getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_DOUBLE)) {
						parameterMapLocal.put(stklparameterDto[i].getCNr(),
								((WrapperNumberField) eingabefelder[i]).getDouble());
					} else if (stklparameterDto[i].getCTyp().trim()
							.equals(StuecklisteFac.STKLPARAMETER_TYP_BIGDECIMAL)) {
						parameterMapLocal.put(stklparameterDto[i].getCNr(),
								((WrapperNumberField) eingabefelder[i]).getBigDecimal());
					} else if (stklparameterDto[i].getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_BOOLEAN)) {
						parameterMapLocal.put(stklparameterDto[i].getCNr(),
								Helper.short2Boolean(((WrapperCheckBox) eingabefelder[i]).getShort()));
					} else if (stklparameterDto[i].getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_ITEM_ID)) {

						ItemId itemid = null;

						if (artikelauswahlen[i].getArtikelDto() != null) {
							itemid = new ItemId(artikelauswahlen[i].getArtikelDto().getIId());
						}

						parameterMapLocal.put(stklparameterDto[i].getCNr(), itemid);
					} else if (stklparameterDto[i].getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_KUNDE_ID)) {

						KundeId kundeId = null;

						if (kundeauswehlen[i].getIKey() != null) {
							kundeId =new KundeId( kundeauswehlen[i].getIKey());
						}

						parameterMapLocal.put(stklparameterDto[i].getCNr(), kundeId);
					}

					if (parameterMapLocal.get(stklparameterDto[i].getCNr()) == null
							&& Helper.short2boolean(stklparameterDto[i].getBMandatory()) == true) {

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
						return;

					}

				}

				parameterMap = parameterMapLocal;

				this.setVisible(false);

			} catch (Throwable e2) {
				tpStkl.getPanelQueryPersonal().handleException(e2, true);
			}

		} else if (e.getSource().equals(wbuAbbrechen)) {
			parameterMap = null;
			this.setVisible(false);

		}
	}

}
