package com.lp.client.angebot;

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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReport;
import com.lp.client.frame.report.ReportViewer;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.ItemId;
import com.lp.server.stueckliste.service.StklparameterDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.util.KundeId;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class PanelDialogAngebotAusFormelstueckliste extends com.lp.client.frame.component.PanelDialog
		implements ActionListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSplitPane jSplitPane1 = new JSplitPane();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private Map<String, Object> parameterMap = null;

	private WrapperLabel wlaLosgroesse = new WrapperLabel();
	private WrapperNumberField wnfLosgroesse = new WrapperNumberField();

	private WrapperSelectField wsfKunde = null;

	private PanelReport panelReport = null;

	private JButton wbuAbbrechen = new JButton();

	private JButton wbuAngebotErstellen = new JButton();

	private JButton wbuBerechnen = new JButton();

	private JScrollPane jspScrollPane = new JScrollPane();

	private StklparameterDto[] stklparameterDto = null;
	private Component[] eingabefelder;

	private WrapperIdentField[] artikelauswahlen;
	private WrapperSelectField[] kundeauswehlen;

	private TabbedPaneAngebot tpAG = null;
	private Integer stuecklisteIId_formelstueckliste = null;

	private Integer angebotIId = null;
	
	private boolean bNurPositionHinzufuegen=false;

	public Integer getAngebotIId() {
		return angebotIId;
	}

	public PanelDialogAngebotAusFormelstueckliste(TabbedPaneAngebot tpAG, Integer stuecklisteIId_formelstueckliste, boolean bNurPositionHinzufuegen)
			throws Throwable {
		super(tpAG.getInternalFrame(), "Konfigurieren", false);
		this.tpAG = tpAG;
		this.stuecklisteIId_formelstueckliste = stuecklisteIId_formelstueckliste;
		this.bNurPositionHinzufuegen=bNurPositionHinzufuegen;
		
		jbInit();
		
		
		if(bNurPositionHinzufuegen) {
			wsfKunde.getWrapperButton().setEnabled(false);
			wsfKunde.setKey(tpAG.getAngebotDto().getKundeIIdAngebotsadresse());
		}
		

	}

	private void jbInit() throws Throwable {

		jSplitPane1.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

		jSplitPane1.setAutoscrolls(true);

		JPanel panel = new JPanel();

		panel.setLayout(gridBagLayout1);

		wbuBerechnen.setText(LPMain.getTextRespectUISPr("angb.neuausstklmitformeln.aktualisieren"));

		getInternalFrame().addItemChangedListener(this);

		wbuAbbrechen.setText(LPMain.getTextRespectUISPr("lp.abbrechen"));

		wbuAngebotErstellen.setText(LPMain.getTextRespectUISPr("angb.neuausstklmitformeln.angeboterstellen"));

		wbuAngebotErstellen.addActionListener(this);
		wbuAngebotErstellen.setMnemonic('e');
		wbuAngebotErstellen.setEnabled(false);

		wbuBerechnen.addActionListener(this);
		wbuBerechnen.setMnemonic('a');

		wlaLosgroesse.setText(LPMain.getTextRespectUISPr("label.losgroesse"));

		setSize(900, 700);

		wbuAbbrechen.addActionListener(this);

		this.setLayout(gridBagLayout2);

		this.add(jSplitPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 700, 500));

		panel.add(jspScrollPane, new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));
		jSplitPane1.setDividerLocation(Defaults.sizeFactor(500));
		jSplitPane1.add(panel, JSplitPane.LEFT);

		jSplitPane1.add(new JPanel(), JSplitPane.RIGHT);

		stklparameterDto = DelegateFactory.getInstance().getStuecklisteDelegate()
				.stklparameterFindByStuecklisteIId(stuecklisteIId_formelstueckliste);

		JPanel panelWerte = new JPanel(new GridBagLayout());

		eingabefelder = new Component[stklparameterDto.length];
		artikelauswahlen = new WrapperIdentField[stklparameterDto.length];
		kundeauswehlen = new WrapperSelectField[stklparameterDto.length];

		int iZeile = 0;
		for (int i = 0; i < stklparameterDto.length; i++) {
			iZeile++;
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
				artikelauswahl = new WrapperIdentField(tpAG.getInternalFrame(), null);
				artikelauswahl.getWtfIdent()
						.setMandatoryField(Helper.short2boolean(stklparameterDto[i].getBMandatory()));
			} else if (stklparameterDto[i].getCTyp().trim().equals(StuecklisteFac.STKLPARAMETER_TYP_KUNDE_ID)) {
				kundenauswahl = new WrapperSelectField(WrapperSelectField.KUNDE, tpAG.getInternalFrame(),
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
				c.addKeyListener(this);
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

		iZeile++;
		panelWerte.add(wlaLosgroesse, new GridBagConstraints(1, iZeile, 1, 1, .5, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(20, 2, 0, 2), 0, 0));

		wnfLosgroesse.setMandatoryField(true);
		wnfLosgroesse.setBigDecimal(BigDecimal.ONE);
		wnfLosgroesse.addKeyListener(this);

		panelWerte.add(wnfLosgroesse, new GridBagConstraints(2, iZeile, 1, 1, .5, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(20, 2, 0, 2), 0, 0));

		// Kunde
		iZeile++;

		wsfKunde = new WrapperSelectField(WrapperSelectField.KUNDE, tpAG.getInternalFrame(), false);

		panelWerte.add(wsfKunde.getWrapperButton(), new GridBagConstraints(1, iZeile, 1, 1, .5, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 0, 2), 0, 0));
		panelWerte.add(wsfKunde.getWrapperTextField(), new GridBagConstraints(2, iZeile, 1, 1, .5, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 0, 2), 0, 0));

		jspScrollPane.getViewport().add(panelWerte, null);

		panel.add(wbuBerechnen, new GridBagConstraints(0, 4, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 80, 0));
		panel.add(wbuAbbrechen, new GridBagConstraints(1, 4, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 30, 0));

		panel.add(wbuAngebotErstellen, new GridBagConstraints(2, 4, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

	}

	public Map<String, Object> getParameter() {
		return parameterMap;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getSource().equals(wbuBerechnen) || e.getSource().equals(wbuAngebotErstellen)) {
			try {

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

						KundeId kundeid = null;

						if (kundeauswehlen[i].getIKey() != null) {

							kundeid = new KundeId(kundeauswehlen[i].getIKey());
						}

						parameterMapLocal.put(stklparameterDto[i].getCNr(), kundeid);
					}

					if (parameterMapLocal.get(stklparameterDto[i].getCNr()) == null
							&& Helper.short2boolean(stklparameterDto[i].getBMandatory()) == true) {

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
						return;

					}

				}

				parameterMap = parameterMapLocal;

				BigDecimal bdLosgroesse = BigDecimal.ONE;

				if (wnfLosgroesse.getBigDecimal() != null) {
					bdLosgroesse = wnfLosgroesse.getBigDecimal();
				}

				if (e.getSource().equals(wbuBerechnen)) {

					try {
						ReportvarianteDto varianteDto = DelegateFactory.getInstance().getDruckerDelegate()
								.reportvarianteFindByCReportnameCReportnameVariante(
										StuecklisteReportFac.REPORT_STUECKLISTE_GESAMTKALKULATION,
										"stk_gesamtkalkulation_formel_vk.jasper");
						LPMain.getTheClient().setReportvarianteIId(varianteDto.getIId());
					} catch (Exception e1) {
						// Keine Variante vorhanden // FEHLER
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
								LPMain.getTextRespectUISPr("angb.neuausstklmitformeln.variante_gk.fehlt"));
						setVisible(false);
					}

					JasperPrintLP print = DelegateFactory.getInstance().getStuecklisteReportDelegate()
							.printGesamtkalkulationKonfigurator(stuecklisteIId_formelstueckliste, bdLosgroesse, false,
									false, false, false,false, parameterMap, wsfKunde.getIKey(),false,null,null,null,null);

					Map m = print.getMapParameters();

					Object letzterZoomfaktor = null;

					

					panelReport = new PanelReport(tpAG.getInternalFrame(), "", print, false);

					panelReport.setPreferredSize(
							new Dimension(tpAG.getInternalFrame().getWidth(), tpAG.getInternalFrame().getHeight()));


					jSplitPane1.add(panelReport, JSplitPane.RIGHT);
					wbuAngebotErstellen.setEnabled(true);

				} else {

					if (wsfKunde.getIKey() == null) {

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
								LPMain.getTextRespectUISPr("angb.neuausstklmitformeln.angeboterstellen.kundefehlt"));
					} else {

						if(bNurPositionHinzufuegen) {
							angebotIId = DelegateFactory.getInstance().getStuecklisteDelegate()
									.createProduktstuecklisteAnhandFormelstuecklisteUndErzeugeAngebot(tpAG.getAngebotDto().getIId(),
											stuecklisteIId_formelstueckliste, bdLosgroesse, wsfKunde.getIKey(),
											parameterMap);
						}else {
							angebotIId = DelegateFactory.getInstance().getStuecklisteDelegate()
									.createProduktstuecklisteAnhandFormelstuecklisteUndErzeugeAngebot(null,
											stuecklisteIId_formelstueckliste, bdLosgroesse, wsfKunde.getIKey(),
											parameterMap);
						}
						

						tpAG.getPanelAuswahl().eventYouAreSelected(false);
						tpAG.getPanelAuswahl().setSelectedId(angebotIId);
						tpAG.getPanelAuswahl().eventYouAreSelected(false);
						getInternalFrame().closePanelDialog();
					}

				}

			} catch (Throwable e2) {
				tpAG.getAngebotAuswahl().handleException(e2, true);
			}

		} else if (e.getSource().equals(wbuAbbrechen)) {
			parameterMap = null;
			getInternalFrame().closePanelDialog();

		}
	}

	@Override
	protected void eventKeyTyped(KeyEvent e) throws Throwable {
		wbuAngebotErstellen.setEnabled(false);

	}

	@Override
	protected void eventKeyPressed(KeyEvent e) throws Throwable {

	}

	@Override
	protected void eventKeyReleased(KeyEvent e) throws Throwable {

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		} else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			int i = 0;
		}
	}

}
