package com.lp.client.auftrag;

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

import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayout.Align;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelDateRange;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.LagerReportFac;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportAuftragUmsatzstatistik extends PanelBasis implements PanelReportIfJRDS {

	private static final long serialVersionUID = 5603367408558454859L;

	protected JPanel jpaWorkingOn = new JPanel();
	private WrapperLabel wlaOffenerAuftragsstandZum = new WrapperLabel();
	private WrapperDateField wdfVon = new WrapperDateField();

	private JLabel wlaKundengruppierung = new JLabel();

	private ButtonGroup buttonGroupKundengruppierung = new ButtonGroup();
	private JRadioButton wrbKundGruppBranche = new JRadioButton();
	private JRadioButton wrbKundGruppPartnerklasse = new JRadioButton();
	private JRadioButton wrbKundGruppKeine = new JRadioButton();

	private WrapperCheckBox wcbNurHauptgruppeKlasse = new WrapperCheckBox();

	private ButtonGroup buttonGroupSortierung = new ButtonGroup();
	private JRadioButton wrbSortName = new JRadioButton();
	private JRadioButton wrbSortUmsatz = new JRadioButton();
	private JRadioButton wrbSortLkz = new JRadioButton();
	private ButtonGroup buttonGroupPreis = new ButtonGroup();

	private ButtonGroup buttonGroupGruppierung = new ButtonGroup();
	private JRadioButton wrbGruppierungArtikelklassen = new JRadioButton();
	private JRadioButton wrbGruppierungArtikelgruppen = new JRadioButton();
	private JRadioButton wrbGruppierungFertigungsgruppen = new JRadioButton();

	private WrapperComboBox wcoArt;
	private WrapperComboBox wcoArtUnverbindlich;

	private WrapperCheckBox wcbMitAngelegten = null;

	private JLabel wlaSortierung = new JLabel();
	private JLabel wlaGruppierung = new JLabel();

	public ReportAuftragUmsatzstatistik(InternalFrame internalFrame, String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getTextRespectUISPr("part.kund.umsatzstatistik");

		jbInit();
		initComponents();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		// return wdfVon;
		return wdfVon;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		wcbNurHauptgruppeKlasse.setVisible(false);

		if (wrbGruppierungArtikelgruppen.isSelected() || wrbGruppierungArtikelklassen.isSelected()) {
			wcbNurHauptgruppeKlasse.setVisible(true);
		}

	}

	private void jbInit() throws Throwable {

		wlaOffenerAuftragsstandZum
				.setText(LPMain.getTextRespectUISPr("auft.menu.umsatzstatistik.offenerauftragsstand"));

		wrbKundGruppBranche.setText(LPMain.getTextRespectUISPr("lp.branche"));
		wrbKundGruppPartnerklasse.setText(LPMain.getTextRespectUISPr("lp.partnerklasse"));
		wrbKundGruppKeine.setText(LPMain.getTextRespectUISPr("label.kundengruppierung.keine"));

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);
		wdfVon.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
		wdfVon.setMandatoryField(true);

		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));
		wlaGruppierung.setText(LPMain.getTextRespectUISPr("label.gruppierung"));
		buttonGroupSortierung.add(wrbSortName);
		buttonGroupSortierung.add(wrbSortUmsatz);
		buttonGroupSortierung.add(wrbSortLkz);
		buttonGroupGruppierung.add(wrbGruppierungArtikelklassen);
		buttonGroupGruppierung.add(wrbGruppierungArtikelgruppen);
		buttonGroupGruppierung.add(wrbGruppierungFertigungsgruppen);

		buttonGroupKundengruppierung.add(wrbKundGruppBranche);
		buttonGroupKundengruppierung.add(wrbKundGruppPartnerklasse);
		buttonGroupKundengruppierung.add(wrbKundGruppKeine);

		wlaKundengruppierung.setText(LPMain.getTextRespectUISPr("label.kundengruppierung"));

		wcbNurHauptgruppeKlasse.setText(LPMain.getTextRespectUISPr("kunde.umsatzstatistik.nurhauptgruppeklasse"));

		LinkedHashMap<Integer, String> hm = new LinkedHashMap<Integer, String>();
		hm.put(1, LPMain.getTextRespectUISPr("auft.journal.ohnerahmenauftraege")); // ohne
																					// Rahmenauftraege,
																					// d.h.
																					// Abrufe
																					// und
																					// freie
																					// Auftraege
		hm.put(2, LPMain.getTextRespectUISPr("auft.journal.nurrahmenauftraege")); // nur
																					// Rahmenauftraege
		wcoArt = new WrapperComboBox();
		wcoArt.setMandatoryField(true);
		wcoArt.setKeyOfSelectedItem(0);
		wcoArt.setMap(hm);

		LinkedHashMap<Integer, String> hmUnverbindlich = new LinkedHashMap<Integer, String>();
		hmUnverbindlich.put(AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_ALLE,
				LPMain.getTextRespectUISPr("auftrag.report.offene.unverbindliche.option1"));
		hmUnverbindlich.put(AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_NUR_UNVERBINDLICHE,
				LPMain.getTextRespectUISPr("auftrag.report.offene.unverbindliche.option2"));
		hmUnverbindlich.put(AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_OHNE_UNVERBINDLICHE,
				LPMain.getTextRespectUISPr("auftrag.report.offene.unverbindliche.option3"));

		wcoArtUnverbindlich = new WrapperComboBox();
		wcoArtUnverbindlich.setMandatoryField(true);
		wcoArtUnverbindlich.setKeyOfSelectedItem(0);
		wcoArtUnverbindlich.setMap(hmUnverbindlich);
		HelperClient.setMinimumAndPreferredSize(wcoArt, HelperClient.getSizeFactoredDimension(130));
		HelperClient.setMinimumAndPreferredSize(wcoArtUnverbindlich, HelperClient.getSizeFactoredDimension(230));

		wrbGruppierungFertigungsgruppen.setText(LPMain.getTextRespectUISPr("stkl.fertigungsgruppe"));
		wrbGruppierungFertigungsgruppen.addActionListener(this);
		wrbGruppierungArtikelgruppen.setText(LPMain.getTextRespectUISPr("lp.artikelgruppe"));
		wrbGruppierungArtikelgruppen.addActionListener(this);
		wrbGruppierungArtikelklassen.setText(LPMain.getTextRespectUISPr("lp.artikelklasse"));
		wrbGruppierungArtikelklassen.addActionListener(this);
		wrbGruppierungArtikelklassen.setSelected(true);

		wrbSortName.setText(LPMain.getTextRespectUISPr("lp.name"));
		wrbSortName.addActionListener(this);
		wrbSortUmsatz.setText(LPMain.getTextRespectUISPr("lp.umsatz"));
		wrbSortUmsatz.addActionListener(this);
		wrbSortUmsatz.setSelected(true);

		wrbSortLkz.setText(LPMain.getTextRespectUISPr("part.kund.umsatzstatistik.sortierung.lkz"));
		wrbSortLkz.addActionListener(this);

		wrbKundGruppKeine.setSelected(true);

		wcbMitAngelegten = new WrapperCheckBox();
		wcbMitAngelegten.setText(LPMain.getInstance().getTextRespectUISPr("auft.offenedetails.mitangelegten"));

		HvLayout hvLayout = HvLayoutFactory.create(this);

		hvLayout.addEmptyColumn().addAligned(Align.RIGHT, wlaOffenerAuftragsstandZum, 200).add(wdfVon, 160).wrap();

		hvLayout.addAligned(Align.RIGHT, wlaGruppierung).add(wrbGruppierungArtikelklassen)
				.add(wrbGruppierungArtikelgruppen).add(wrbGruppierungFertigungsgruppen).wrap();

		hvLayout.addEmptyColumn().add(wcbNurHauptgruppeKlasse, 160).span(3).wrap();

		hvLayout.addAligned(Align.RIGHT, wlaSortierung).add(wrbSortUmsatz).add(wrbSortName).add(wrbSortLkz).wrap();

		hvLayout.addAligned(Align.RIGHT, wlaKundengruppierung).add(wrbKundGruppBranche).add(wrbKundGruppPartnerklasse)
				.add(wrbKundGruppKeine).wrap();

		hvLayout.add(wcoArt).add(wcoArtUnverbindlich).add(wcbMitAngelegten, 150);

	}

	public String getModul() {
		return LagerReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		Integer iOptionGruppierung = null;
		Integer iOptionSortierung = null;
		Integer iOptionKundeGruppierung = null;

		if (wrbGruppierungArtikelgruppen.isSelected()) {
			iOptionGruppierung = new Integer(
					LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELGRUPPE);
		} else if (wrbGruppierungArtikelklassen.isSelected()) {
			iOptionGruppierung = new Integer(
					LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_ARTIKELKLASSE);
		} else if (wrbGruppierungFertigungsgruppen.isSelected()) {
			iOptionGruppierung = new Integer(
					LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_FERTIGUNGSGRUPPE);
		}

		if (wrbSortName.isSelected()) {
			iOptionSortierung = new Integer(LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_SORTIERUNG_FIRMANNAME);
		} else if (wrbSortUmsatz.isSelected()) {
			iOptionSortierung = new Integer(LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_SORTIERUNG_UMSATZ);
		} else if (wrbSortLkz.isSelected()) {
			iOptionSortierung = new Integer(LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_SORTIERUNG_LKZ);
		}

		if (wrbKundGruppBranche.isSelected()) {
			iOptionKundeGruppierung = new Integer(
					LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_BRANCHE);
		} else if (wrbKundGruppPartnerklasse.isSelected()) {
			iOptionKundeGruppierung = new Integer(
					LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_PARTNERKLASSE);
		} else if (wrbKundGruppKeine.isSelected()) {
			iOptionKundeGruppierung = new Integer(
					LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_KUNDENGRUPPIERUNG_KEINE);
		}

		return DelegateFactory.getInstance().getAuftragReportDelegate().printAuftragUmsatzstatistik(wdfVon.getDate(),
				(Integer) wcoArt.getKeyOfSelectedItem(), (Integer) wcoArtUnverbindlich.getKeyOfSelectedItem(),
				iOptionGruppierung, wcbNurHauptgruppeKlasse.isSelected(), iOptionKundeGruppierung, iOptionSortierung,
				wcbMitAngelegten.isSelected());

	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
