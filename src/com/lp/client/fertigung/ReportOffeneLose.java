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
package com.lp.client.fertigung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.Facade;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportOffeneLose extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	static final public String ACTION_SPECIAL_KOSTENSTELLE_FROM_LISTE = "ACTION_SPECIAL_KOSTENSTELLE_FROM_LISTE";
	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "ACTION_SPECIAL_KUNDE_FROM_LISTE";
	static final public String ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE = "ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE";

	private PanelQueryFLR panelQueryFLRKunde = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;
	private PanelQueryFLR panelQueryFLRFertigungsgruppe = null;

	private Integer kundeIId = null;
	private Integer fertigungsgruppeIId = null;
	private Integer kostenstelleIId = null;
	private WrapperButton wbuKunde = new WrapperButton();
	private WrapperTextField wtfKunde = new WrapperTextField();
	private WrapperButton wbuKostenstelle = new WrapperButton();
	private WrapperTextField wtfKostenstelle = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);

	private WrapperButton wbuFertigungsgruppe = new WrapperButton();
	private WrapperTextField wtfFertigungsgruppe = new WrapperTextField();

	private WrapperLabel wlaSortierung = new WrapperLabel();

	private WrapperDateField wdfStichtag = new WrapperDateField();

	private WrapperButton wbuBelegnrvon = new WrapperButton();
	private WrapperTextField wtfBelegnrvon = new WrapperTextField();
	private WrapperButton wbuBelegnrbis = new WrapperButton();
	private WrapperTextField wtfBelegnrbis = new WrapperTextField();

	private ButtonGroup buttonGroupSortierung = new ButtonGroup();
	private WrapperRadioButton wrbKunde = new WrapperRadioButton();
	private WrapperRadioButton wrbLosnummer = new WrapperRadioButton();
	private WrapperRadioButton wrbKostenstelle = new WrapperRadioButton();
	private WrapperRadioButton wrbBeginntermin = new WrapperRadioButton();
	private WrapperRadioButton wrbEndetermin = new WrapperRadioButton();
	private WrapperRadioButton wrbArtikel = new WrapperRadioButton();
	private WrapperRadioButton wrbFertigungsgruppe = new WrapperRadioButton();
	private WrapperRadioButton wrbKundeundGewaehlterTermin = new WrapperRadioButton();
	private WrapperRadioButton wrbLiefertermin = new WrapperRadioButton();

	private WrapperComboBox wcbOptionStichtag = new WrapperComboBox();
	private WrapperCheckBox wcbNurForecast = new WrapperCheckBox();

	private PanelQueryFLR panelQueryFLRLos_von = null;
	private PanelQueryFLR panelQueryFLRLos_bis = null;

	static final public String ACTION_SPECIAL_LOS_VON_FROM_LISTE = "ACTION_SPECIAL_LOS_VON_FROM_LISTE";
	static final public String ACTION_SPECIAL_LOS_BIS_FROM_LISTE = "ACTION_SPECIAL_LOS_BIS_FROM_LISTE";

	private Integer losIIdVon = null;
	private Integer losIIdBis = null;

	public ReportOffeneLose(InternalFrame internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getInstance().getTextRespectUISPr("stkl.stueckliste");
		jbInit();
		initComponents();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfStichtag;
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);

		wlaSortierung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.sortierung"));
		wlaSortierung.setHorizontalAlignment(SwingConstants.LEFT);

		wbuBelegnrvon.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.losnummer")
				+ " von");
		wbuBelegnrvon.addActionListener(this);
		wbuBelegnrvon.setActionCommand(this.ACTION_SPECIAL_LOS_VON_FROM_LISTE);
		wtfBelegnrvon.setActivatable(false);

		wbuBelegnrbis.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.bis"));
		wbuBelegnrbis.addActionListener(this);
		wbuBelegnrbis.setActionCommand(this.ACTION_SPECIAL_LOS_BIS_FROM_LISTE);
		wtfBelegnrbis.setActivatable(false);
		
		wcbNurForecast.setText(LPMain.getInstance()
				.getTextRespectUISPr("fert.offenelose.nurforecast"));
		

		wcbOptionStichtag.setMandatoryField(true);

		Map<Integer, String> m = new TreeMap<Integer, String>();
		m.put(FertigungReportFac.OFFENE_OPTION_STICHTAG_BEGINNDATUM,
				LPMain.getTextRespectUISPr("lp.beginn"));
		m.put(FertigungReportFac.OFFENE_OPTION_STICHTAG_ENDEDATUM,
				LPMain.getTextRespectUISPr("lp.ende"));
		m.put(FertigungReportFac.OFFENE_OPTION_STICHTAG_LIEFERTERMIN,
				LPMain.getTextRespectUISPr("label.liefertermin"));

		wcbOptionStichtag.setMap(m);

		wcbOptionStichtag
				.setKeyOfSelectedItem(FertigungReportFac.OFFENE_OPTION_STICHTAG_ENDEDATUM);

		wdfStichtag.setTimestamp(new java.sql.Timestamp(System
				.currentTimeMillis()));

		wbuFertigungsgruppe.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.fertigungsgruppe"));
		wbuFertigungsgruppe
				.setActionCommand(this.ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE);
		wbuFertigungsgruppe.addActionListener(this);

		wbuKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde"));
		wbuKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kostenstelle"));

		wtfKunde.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfKostenstelle.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfKunde.setEditable(false);
		wtfKostenstelle.setEditable(false);
		wtfFertigungsgruppe.setEditable(false);

		wrbBeginntermin.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.beginn"));
		wrbEndetermin.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.ende"));
		wrbKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.kostenstelle"));
		wrbKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kunde.modulname"));
		wrbLosnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.losnummer"));
		wrbArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelnummer"));
		wrbFertigungsgruppe.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.fertigungsgruppe"));
		wrbKundeundGewaehlterTermin.setText(LPMain.getInstance()
				.getTextRespectUISPr("fert.kundeundtermin"));
		wrbLiefertermin.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.liefertermin"));

		wrbLosnummer.setSelected(true);

		wbuKostenstelle
				.setActionCommand(this.ACTION_SPECIAL_KOSTENSTELLE_FROM_LISTE);
		wbuKostenstelle.addActionListener(this);
		wbuKunde.setActionCommand(this.ACTION_SPECIAL_KUNDE_FROM_LISTE);
		wbuKunde.addActionListener(this);
		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		int iZeile = 0;

		jpaWorkingOn.add(wbuKunde, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, iZeile, 1, 1,
				0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(2, iZeile, 2, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wbuKostenstelle, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelle, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbLosnummer, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbKunde, new GridBagConstraints(4, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 150, 0));

		iZeile++;
		jpaWorkingOn.add(wbuFertigungsgruppe, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFertigungsgruppe, new GridBagConstraints(1, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbKostenstelle, new GridBagConstraints(3, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbBeginntermin, new GridBagConstraints(4, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbOptionStichtag, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfStichtag, new GridBagConstraints(1, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbEndetermin, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbArtikel, new GridBagConstraints(4, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuBelegnrvon, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBelegnrvon, new GridBagConstraints(1, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbFertigungsgruppe, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbKundeundGewaehlterTermin, new GridBagConstraints(4,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wbuBelegnrbis, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBelegnrbis, new GridBagConstraints(1, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbLiefertermin, new GridBagConstraints(3, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FORECAST)) {
			jpaWorkingOn.add(wcbNurForecast, new GridBagConstraints(4,
					iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		}
		

		iZeile++;

		buttonGroupSortierung.add(wrbBeginntermin);
		buttonGroupSortierung.add(wrbEndetermin);
		buttonGroupSortierung.add(wrbKostenstelle);
		buttonGroupSortierung.add(wrbKunde);
		buttonGroupSortierung.add(wrbLosnummer);
		buttonGroupSortierung.add(wrbArtikel);
		buttonGroupSortierung.add(wrbFertigungsgruppe);
		buttonGroupSortierung.add(wrbKundeundGewaehlterTermin);
		buttonGroupSortierung.add(wrbLiefertermin);

	}

	public String getModul() {
		return FertigungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FertigungReportFac.REPORT_OFFENE;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRKostenstelle) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				KostenstelleDto kostenstelleDto = DelegateFactory.getInstance()
						.getSystemDelegate()
						.kostenstelleFindByPrimaryKey((Integer) key);
				wtfKostenstelle.setText(kostenstelleDto
						.formatKostenstellenbezeichnung());
				kostenstelleIId = kostenstelleDto.getIId();
			} else if (e.getSource() == panelQueryFLRKunde) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				KundeDto kundeDto = DelegateFactory.getInstance()
						.getKundeDelegate().kundeFindByPrimaryKey(key);
				kundeIId = kundeDto.getIId();
				wtfKunde.setText(kundeDto.getPartnerDto()
						.formatFixTitelName1Name2());
			} else if (e.getSource() == panelQueryFLRFertigungsgruppe) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory
						.getInstance().getStuecklisteDelegate()
						.fertigungsgruppeFindByPrimaryKey(key);
				fertigungsgruppeIId = fertigungsgruppeDto.getIId();
				wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());
			} else if (e.getSource() == panelQueryFLRLos_von) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				LosDto losDto = DelegateFactory.getInstance()
						.getFertigungDelegate().losFindByPrimaryKey(key);
				losIIdVon = key;
				wtfBelegnrvon.setText(losDto.getCNr());
			} else if (e.getSource() == panelQueryFLRLos_bis) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				LosDto losDto = DelegateFactory.getInstance()
						.getFertigungDelegate().losFindByPrimaryKey(key);
				losIIdBis = key;
				wtfBelegnrbis.setText(losDto.getCNr());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRKostenstelle) {
				wtfKostenstelle.setText(null);
				kostenstelleIId = null;
			} else if (e.getSource() == panelQueryFLRKunde) {
				wtfKunde.setText(null);
				kundeIId = null;
			} else if (e.getSource() == panelQueryFLRFertigungsgruppe) {
				wtfFertigungsgruppe.setText(null);
				fertigungsgruppeIId = null;
			} else if (e.getSource() == panelQueryFLRLos_von) {
				wtfBelegnrvon.setText(null);
				losIIdVon = null;
			} else if (e.getSource() == panelQueryFLRLos_bis) {
				wtfBelegnrbis.setText(null);
				losIIdBis = null;
			}

		}
	}

	private void dialogQueryLosVon(ActionEvent e) throws Throwable {
		panelQueryFLRLos_von = FertigungFilterFactory.getInstance()
				.createPanelFLRLose(getInternalFrame(), null, true);
		if (losIIdVon != null) {
			panelQueryFLRLos_von.setSelectedId(losIIdVon);
		}
		new DialogQuery(panelQueryFLRLos_von);
	}

	private void dialogQueryLosBis(ActionEvent e) throws Throwable {
		panelQueryFLRLos_bis = FertigungFilterFactory.getInstance()
				.createPanelFLRLose(getInternalFrame(), null, true);
		if (losIIdBis != null) {
			panelQueryFLRLos_bis.setSelectedId(losIIdBis);
		}
		new DialogQuery(panelQueryFLRLos_bis);
	}

	void dialogQueryKostenstelleFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance()
				.createPanelFLRKostenstelle(getInternalFrame(), false, true,
						kostenstelleIId);
		new DialogQuery(panelQueryFLRKostenstelle);
	}

	void dialogQueryKundeFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(getInternalFrame(), true, true, kundeIId);
		new DialogQuery(panelQueryFLRKunde);
	}

	void dialogQueryFertigungsgruppeFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRFertigungsgruppe = StuecklisteFilterFactory.getInstance()
				.createPanelFLRFertigungsgruppe(getInternalFrame(),
						fertigungsgruppeIId, true);
		new DialogQuery(panelQueryFLRFertigungsgruppe);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE_FROM_LISTE)) {
			dialogQueryKostenstelleFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_FROM_LISTE)) {
			dialogQueryKundeFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE)) {
			dialogQueryFertigungsgruppeFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_LOS_VON_FROM_LISTE)) {
			dialogQueryLosVon(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_LOS_BIS_FROM_LISTE)) {
			dialogQueryLosBis(e);
		}
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		int iOptionSortierung = -1;

		if (wrbBeginntermin.isSelected()) {
			iOptionSortierung = FertigungReportFac.OFFENE_OPTION_SORTIERUNG_BEGINN;
		} else if (wrbEndetermin.isSelected()) {
			iOptionSortierung = FertigungReportFac.OFFENE_OPTION_SORTIERUNG_ENDE;
		} else if (wrbKostenstelle.isSelected()) {
			iOptionSortierung = FertigungReportFac.OFFENE_OPTION_SORTIERUNG_KOSTENSTELLE;
		} else if (wrbKunde.isSelected()) {
			iOptionSortierung = FertigungReportFac.OFFENE_OPTION_SORTIERUNG_KUNDE;
		} else if (wrbLosnummer.isSelected()) {
			iOptionSortierung = FertigungReportFac.OFFENE_OPTION_SORTIERUNG_LOSNUMMER;
		} else if (wrbArtikel.isSelected()) {
			iOptionSortierung = FertigungReportFac.OFFENE_OPTION_SORTIERUNG_ARTIKEL;
		} else if (wrbFertigungsgruppe.isSelected()) {
			iOptionSortierung = FertigungReportFac.OFFENE_OPTION_SORTIERUNG_FERTIGUNGSGRUPPE;
		} else if (wrbKundeundGewaehlterTermin.isSelected()) {
			iOptionSortierung = FertigungReportFac.OFFENE_OPTION_SORTIERUNG_KUNDEUNDGEWAEHLTERTERMIN;
		} else if (wrbLiefertermin.isSelected()) {
			iOptionSortierung = FertigungReportFac.OFFENE_OPTION_SORTIERUNG_LIEFERTERMIN;
		}
		return DelegateFactory
				.getInstance()
				.getFertigungDelegate()
				.printOffene(wdfStichtag.getDate(),
						(Integer) wcbOptionStichtag.getKeyOfSelectedItem(),
						wtfBelegnrvon.getText(), wtfBelegnrbis.getText(),
						kundeIId, kostenstelleIId, fertigungsgruppeIId,
						iOptionSortierung,wcbNurForecast.isSelected());
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
