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
package com.lp.client.partner;

import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayout.Align;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelDateRange;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.LagerReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportKundeumsatzstatistik extends PanelBasis implements PanelReportIfJRDS {

	private static final long serialVersionUID = 5603367408558454859L;

	protected JPanel jpaWorkingOn = new JPanel();
	// private GridBagLayout gridBagLayout2 = new GridBagLayout();
	// private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperLabel wlaVon = new WrapperLabel();
	// private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	// private WrapperDateField wdfBis = new WrapperDateField();
	// private WrapperDateRangeController wdrBereich = null;

	private PanelDateRange panelDateRange = new PanelDateRange();

	// private WrapperLabel wlaKundengruppierung = new WrapperLabel();
	private JLabel wlaKundengruppierung = new JLabel();

	private ButtonGroup buttonGroupKundengruppierung = new ButtonGroup();
	private JRadioButton wrbKundGruppBranche = new JRadioButton();
	private JRadioButton wrbKundGruppPartnerklasse = new JRadioButton();
	private JRadioButton wrbKundGruppKeine = new JRadioButton();

	private WrapperCheckBox wcbAuftragsadresse = new WrapperCheckBox();
	private WrapperCheckBox wcbStatistikadresse = new WrapperCheckBox();
	private WrapperCheckBox wcbNurHauptgruppeKlasse = new WrapperCheckBox();
	private WrapperCheckBox wcbMitNichtLagerbewertetenArtikeln = new WrapperCheckBox();
	private WrapperComboBox wcoOptionSortierungJahre = new WrapperComboBox();
	private WrapperCheckBox wcbOhneDeckungsbeitrag = new WrapperCheckBox();
	private WrapperCheckBox wcbSortiertNachProvisionsempfaenger = new WrapperCheckBox();

	private ButtonGroup buttonGroupSortierung = new ButtonGroup();
	private JRadioButton wrbSortName = new JRadioButton();
	private JRadioButton wrbSortUmsatz = new JRadioButton();
	private JRadioButton wrbSortLkz = new JRadioButton();
	private JRadioButton wrbPreisDB = new JRadioButton();
	private JRadioButton wrbPreisUmsatz = new JRadioButton();
	private ButtonGroup buttonGroupPreis = new ButtonGroup();

	private ButtonGroup buttonGroupGruppierung = new ButtonGroup();
	private JRadioButton wrbGruppierungArtikelklassen = new JRadioButton();
	private JRadioButton wrbGruppierungArtikelgruppen = new JRadioButton();
	private JRadioButton wrbGruppierungFertigungsgruppen = new JRadioButton();
	private JRadioButton wrbGruppierungJahresstatistik = new JRadioButton();
	private JRadioButton wrbGruppierungMonate = new JRadioButton();

	// private WrapperLabel wlaSortierung = new WrapperLabel();
	private JLabel wlaSortierung = new JLabel();
	// private WrapperLabel wlaGruppierung = new WrapperLabel();
	private JLabel wlaGruppierung = new JLabel();

	public ReportKundeumsatzstatistik(InternalFrame internalFrame, String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getTextRespectUISPr("part.kund.umsatzstatistik");

		jbInit();
		initComponents();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		// return wdfVon;
		return panelDateRange.getWdfVon();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		wcoOptionSortierungJahre.setMandatoryField(false);
		wcoOptionSortierungJahre.setVisible(false);
		wcbNurHauptgruppeKlasse.setVisible(false);
		if (e.getSource().equals(wrbGruppierungJahresstatistik)) {
			if (wrbGruppierungJahresstatistik.isSelected()) {
				// wdfBis.setDate(new
				// java.sql.Date(System.currentTimeMillis()));
				panelDateRange.getWdfBis().setDate(new java.sql.Date(System.currentTimeMillis()));
				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 10);
				c.set(Calendar.DAY_OF_YEAR, 1);
				// wdfVon.setTimestamp(new Timestamp(c.getTimeInMillis()));
				panelDateRange.getWdfVon().setTimestamp(new Timestamp(c.getTimeInMillis()));
			}
			if (wrbSortUmsatz.isSelected()) {
				wcoOptionSortierungJahre.setMandatoryField(true);
				wcoOptionSortierungJahre.setVisible(true);
			}
		}

		if (e.getSource().equals(wrbSortUmsatz)) {
			if (wrbGruppierungJahresstatistik.isSelected()) {
				wcoOptionSortierungJahre.setMandatoryField(true);
				wcoOptionSortierungJahre.setVisible(true);
			}
		}
		if (e.getSource().equals(wrbGruppierungMonate)) {
			if (wrbGruppierungMonate.isSelected()) {

				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
				c.set(Calendar.DAY_OF_YEAR, 1);

				// wdfVon.setTimestamp(new Timestamp(c.getTimeInMillis()));
				panelDateRange.getWdfVon().setTimestamp(new Timestamp(c.getTimeInMillis()));
				c.set(Calendar.MONTH, Calendar.DECEMBER);
				c.set(Calendar.DAY_OF_MONTH, 31);
				// wdfBis.setTimestamp(new Timestamp(c.getTimeInMillis()));
				panelDateRange.getWdfBis().setTimestamp(new Timestamp(c.getTimeInMillis()));
			}
		}
		if (wrbGruppierungArtikelgruppen.isSelected() || wrbGruppierungArtikelklassen.isSelected()) {
			wcbNurHauptgruppeKlasse.setVisible(true);
		}

	}

	// private void jbInit() throws Throwable {
	// this.setLayout(gridBagLayout1);
	// jpaWorkingOn.setLayout(gridBagLayout2);
	// wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
	// wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));
	// wrbPreisUmsatz.setText(LPMain.getTextRespectUISPr("lp.umsatz"));
	// wrbPreisDB.setText(LPMain.getTextRespectUISPr("lp.deckungsbeitrag"));
	// wrbPreisUmsatz.setSelected(true);
	//
	// wrbKundGruppBranche.setText(LPMain.getTextRespectUISPr("lp.branche"));
	// wrbKundGruppPartnerklasse.setText(LPMain
	// .getTextRespectUISPr("lp.partnerklasse"));
	// wrbKundGruppKeine.setText(LPMain
	// .getTextRespectUISPr("label.kundengruppierung.keine"));
	//
	// wcbMitNichtLagerbewertetenArtikeln
	// .setText(LPMain
	// .getTextRespectUISPr("report.kundeumsatzstatistik.mitnichtlagerbewertetenartikeln"));
	//
	// wcoOptionSortierungJahre.setMandatoryField(true);
	// Map<Integer, String> m = new TreeMap<Integer, String>();
	//
	// m.put(LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHRE_SORTIERBASIS_UMSATZ_GESAMT,
	// LPMain.getTextRespectUISPr("report.kundeumsatzstatistik.sortierbasis.gesamt"));
	// m.put(LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHRE_SORTIERBASIS_UMSATZ_AKTUELLESJAHR,
	// LPMain.getTextRespectUISPr("report.kundeumsatzstatistik.sortierbasis.aktuelles"));
	// m.put(LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHRE_SORTIERBASIS_UMSATZ_VORJAHR,
	// LPMain.getTextRespectUISPr("report.kundeumsatzstatistik.sortierbasis.vorjahr"));
	//
	// wcoOptionSortierungJahre.setMap(m);
	// wcoOptionSortierungJahre.setVisible(false);
	//
	// Calendar c = Calendar.getInstance();
	// c.set(Calendar.MONTH, 0);
	// c.set(Calendar.DATE, 1);
	// // wdfVon.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
	// panelDateRange.getWdfVon().setTimestamp(new
	// java.sql.Timestamp(c.getTimeInMillis()));
	// c.set(Calendar.MONTH, 11);
	// c.set(Calendar.DATE, 31);
	// // wdfBis.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
	// panelDateRange.getWdfBis().setTimestamp(new
	// java.sql.Timestamp(c.getTimeInMillis()));
	//
	// buttonGroupPreis.add(wrbPreisUmsatz);
	// buttonGroupPreis.add(wrbPreisDB);
	//
	// wcbStatistikadresse = new WrapperCheckBox();
	// wcbStatistikadresse.setText(LPMain
	// .getTextRespectUISPr("rech.statistikadresseverwenden"));
	//
	// wcbOhneDeckungsbeitrag.setText(LPMain
	// .getTextRespectUISPr("report.kundeumsatzstatistik.ohnedeckunsbeitrag"));
	//
	// // wdfVon.setMandatoryField(true);
	// // wdfBis.setMandatoryField(true);
	//
	// wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));
	// wlaGruppierung.setText(LPMain.getTextRespectUISPr("label.gruppierung"));
	// buttonGroupSortierung.add(wrbSortName);
	// buttonGroupSortierung.add(wrbSortUmsatz);
	// buttonGroupSortierung.add(wrbSortLkz);
	// buttonGroupGruppierung.add(wrbGruppierungArtikelklassen);
	// buttonGroupGruppierung.add(wrbGruppierungArtikelgruppen);
	// buttonGroupGruppierung.add(wrbGruppierungFertigungsgruppen);
	// buttonGroupGruppierung.add(wrbGruppierungJahresstatistik);
	// buttonGroupGruppierung.add(wrbGruppierungMonate);
	//
	// buttonGroupKundengruppierung.add(wrbKundGruppBranche);
	// buttonGroupKundengruppierung.add(wrbKundGruppPartnerklasse);
	// buttonGroupKundengruppierung.add(wrbKundGruppKeine);
	//
	// // wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);
	//
	// wlaKundengruppierung.setText(LPMain
	// .getTextRespectUISPr("label.kundengruppierung"));
	//
	// wrbGruppierungJahresstatistik.setText(LPMain
	// .getTextRespectUISPr("kunde.umsatzstatistik.jahresstatistik"));
	// wrbGruppierungJahresstatistik.addActionListener(this);
	//
	// wrbGruppierungMonate.setText(LPMain
	// .getTextRespectUISPr("kunde.umsatzstatistik.monatsstatistik"));
	// wrbGruppierungMonate.addActionListener(this);
	//
	// wrbGruppierungFertigungsgruppen.setText(LPMain
	// .getTextRespectUISPr("stkl.fertigungsgruppe"));
	// wrbGruppierungFertigungsgruppen.addActionListener(this);
	// wrbGruppierungArtikelgruppen.setText(LPMain
	// .getTextRespectUISPr("lp.artikelgruppe"));
	// wrbGruppierungArtikelgruppen.addActionListener(this);
	// wrbGruppierungArtikelklassen.setText(LPMain
	// .getTextRespectUISPr("lp.artikelklasse"));
	// wrbGruppierungArtikelklassen.addActionListener(this);
	// wrbGruppierungArtikelklassen.setSelected(true);
	//
	// wrbSortName.setText(LPMain.getTextRespectUISPr("lp.name"));
	// wrbSortName.addActionListener(this);
	// wrbSortUmsatz.setText(LPMain.getTextRespectUISPr("lp.umsatz"));
	// wrbSortUmsatz.addActionListener(this);
	// wrbSortUmsatz.setSelected(true);
	//
	// wrbSortLkz
	// .setText(LPMain
	// .getTextRespectUISPr("part.kund.umsatzstatistik.sortierung.lkz"));
	// wrbSortLkz.addActionListener(this);
	//
	// wrbKundGruppKeine.setSelected(true);
	//
	// this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
	// GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
	// 0, 0, 0), 0, 0));
	//
	// jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, 0, 1, 1, 3, 0.0,
	// GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	// new Insets(2, 2, 2, 2), 0, 0));
	// // jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, 0, 1, 1, 2, 0.0,
	// jpaWorkingOn.add(panelDateRange.getWdfVon(), new GridBagConstraints(1, 0,
	// 1, 1, 2, 0.0,
	// GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	// new Insets(2, 2, 2, 2), 0, 0));
	//
	// jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, 0, 1, 1, 1, 0.0,
	// GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	// new Insets(2, 2, 2, 2), 0, 0));
	// // jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, 0, 1, 1, 1, 0.0,
	// jpaWorkingOn.add(panelDateRange.getWdfBis(), new GridBagConstraints(3, 0,
	// 1, 1, 1, 0.0,
	// GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	// new Insets(2, 2, 2, 2), 0, 0));
	// // jpaWorkingOn.add(wdrBereich, new GridBagConstraints(4, 0, 1, 1, 1,
	// 0.0,
	// jpaWorkingOn.add(panelDateRange.getWdrc(), new GridBagConstraints(4, 0,
	// 1, 1, 1, 0.0,
	// GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,
	// 2, 2, 2), 0, 0));
	// jpaWorkingOn.add(wcbOhneDeckungsbeitrag,
	// new GridBagConstraints(5, 0, 2, 1, 0.0, 0.0,
	// GridBagConstraints.CENTER,
	// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
	// 0, 0));
	// jpaWorkingOn.add(wrbPreisUmsatz, new GridBagConstraints(1, 1, 1, 1, 0,
	// 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	// new Insets(2, 2, 2, 2), 0, 0));
	//
	// jpaWorkingOn.add(wrbPreisDB, new GridBagConstraints(2, 1, 1, 1, 0, 0.0,
	// GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	// new Insets(2, 2, 2, 2), 0, 0));
	// jpaWorkingOn.add(wcbStatistikadresse, new GridBagConstraints(3, 1, 2,
	// 1, 0.0, 0.0, GridBagConstraints.CENTER,
	// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	// jpaWorkingOn.add(wcbMitNichtLagerbewertetenArtikeln,
	// new GridBagConstraints(5, 1, 2, 1, 0.0, 0.0,
	// GridBagConstraints.CENTER,
	// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
	// 150, 0));
	// jpaWorkingOn.add(wlaGruppierung, new GridBagConstraints(0, 2, 1, 1,
	// 0.0, 0.0, GridBagConstraints.CENTER,
	// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	// jpaWorkingOn.add(wrbGruppierungArtikelklassen, new GridBagConstraints(
	// 1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
	// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	// jpaWorkingOn.add(wrbGruppierungArtikelgruppen, new GridBagConstraints(
	// 2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
	// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	// jpaWorkingOn.add(wrbGruppierungFertigungsgruppen,
	// new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
	// GridBagConstraints.CENTER,
	// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
	// 0, 0));
	// jpaWorkingOn.add(wrbGruppierungJahresstatistik, new GridBagConstraints(
	// 5, 2, 1, 1, 1, 0.0, GridBagConstraints.WEST,
	// GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	// jpaWorkingOn.add(wrbGruppierungMonate, new GridBagConstraints(6, 2, 1,
	// 1, 1, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
	// new Insets(2, 2, 2, 2), 0, 0));
	//
	// jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, 3, 1, 1, 0.0,
	// 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	// new Insets(2, 2, 2, 2), 0, 0));
	// jpaWorkingOn.add(wrbSortUmsatz, new GridBagConstraints(1, 3, 1, 1, 0.0,
	// 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	// new Insets(2, 2, 2, 2), 0, 0));
	// jpaWorkingOn.add(wrbSortName, new GridBagConstraints(2, 3, 1, 1, 0.0,
	// 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	// new Insets(2, 2, 2, 2), 0, 0));
	// jpaWorkingOn.add(wrbSortLkz, new GridBagConstraints(3, 3, 1, 1, 0.0,
	// 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
	// new Insets(2, 2, 2, 2), 0, 0));
	// jpaWorkingOn.add(wcoOptionSortierungJahre, new GridBagConstraints(5, 3,
	// 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
	// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	//
	// jpaWorkingOn.add(wlaKundengruppierung, new GridBagConstraints(0, 4, 1,
	// 1, 0.0, 0.0, GridBagConstraints.CENTER,
	// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
	// jpaWorkingOn.add(wrbKundGruppBranche, new GridBagConstraints(1, 4, 1,
	// 1, 0.0, 0.0, GridBagConstraints.CENTER,
	// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	// jpaWorkingOn.add(wrbKundGruppPartnerklasse, new GridBagConstraints(2,
	// 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
	// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	// jpaWorkingOn.add(wrbKundGruppKeine, new GridBagConstraints(3, 4, 1, 1,
	// 0.0, 0.0, GridBagConstraints.CENTER,
	// GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	// }

	private void jbInit() throws Throwable {
		// this.setLayout(gridBagLayout1);
		// jpaWorkingOn.setLayout(gridBagLayout2);
		wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wrbPreisUmsatz.setText(LPMain.getTextRespectUISPr("lp.umsatz"));
		wrbPreisDB.setText(LPMain.getTextRespectUISPr("lp.deckungsbeitrag"));
		wrbPreisUmsatz.setSelected(true);

		wrbKundGruppBranche.setText(LPMain.getTextRespectUISPr("lp.branche"));
		wrbKundGruppPartnerklasse.setText(LPMain.getTextRespectUISPr("lp.partnerklasse"));
		wrbKundGruppKeine.setText(LPMain.getTextRespectUISPr("label.kundengruppierung.keine"));

		wcbMitNichtLagerbewertetenArtikeln
				.setText(LPMain.getTextRespectUISPr("report.kundeumsatzstatistik.mitnichtlagerbewertetenartikeln"));

		wcbSortiertNachProvisionsempfaenger
				.setText(LPMain.getTextRespectUISPr("report.kundeumsatzstatistik.sortiertnach.provisionsempfaenger"));

		wcoOptionSortierungJahre.setMandatoryField(true);
		Map<Integer, String> m = new TreeMap<Integer, String>();

		m.put(LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHRE_SORTIERBASIS_UMSATZ_GESAMT,
				LPMain.getTextRespectUISPr("report.kundeumsatzstatistik.sortierbasis.gesamt"));
		m.put(LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHRE_SORTIERBASIS_UMSATZ_AKTUELLESJAHR,
				LPMain.getTextRespectUISPr("report.kundeumsatzstatistik.sortierbasis.aktuelles"));
		m.put(LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHRE_SORTIERBASIS_UMSATZ_VORJAHR,
				LPMain.getTextRespectUISPr("report.kundeumsatzstatistik.sortierbasis.vorjahr"));

		wcoOptionSortierungJahre.setMap(m);
		wcoOptionSortierungJahre.setVisible(false);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DATE, 1);
		panelDateRange.getWdfVon().setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
		c.set(Calendar.MONTH, 11);
		c.set(Calendar.DATE, 31);
		panelDateRange.getWdfBis().setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		buttonGroupPreis.add(wrbPreisUmsatz);
		buttonGroupPreis.add(wrbPreisDB);

		wcbStatistikadresse = new WrapperCheckBox();
		wcbStatistikadresse.setText(LPMain.getTextRespectUISPr("rech.statistikadresseverwenden"));
		wcbAuftragsadresse.setText(LPMain.getTextRespectUISPr("part.kundeumsatzstatistik.auftragsadresseverwenden"));

		wcbOhneDeckungsbeitrag.setText(LPMain.getTextRespectUISPr("report.kundeumsatzstatistik.ohnedeckunsbeitrag"));

		// wdfVon.setMandatoryField(true);
		// wdfBis.setMandatoryField(true);

		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));
		wlaGruppierung.setText(LPMain.getTextRespectUISPr("label.gruppierung"));
		buttonGroupSortierung.add(wrbSortName);
		buttonGroupSortierung.add(wrbSortUmsatz);
		buttonGroupSortierung.add(wrbSortLkz);
		buttonGroupGruppierung.add(wrbGruppierungArtikelklassen);
		buttonGroupGruppierung.add(wrbGruppierungArtikelgruppen);
		buttonGroupGruppierung.add(wrbGruppierungFertigungsgruppen);
		buttonGroupGruppierung.add(wrbGruppierungJahresstatistik);
		buttonGroupGruppierung.add(wrbGruppierungMonate);

		buttonGroupKundengruppierung.add(wrbKundGruppBranche);
		buttonGroupKundengruppierung.add(wrbKundGruppPartnerklasse);
		buttonGroupKundengruppierung.add(wrbKundGruppKeine);

		// wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

		wlaKundengruppierung.setText(LPMain.getTextRespectUISPr("label.kundengruppierung"));

		wrbGruppierungJahresstatistik.setText(LPMain.getTextRespectUISPr("kunde.umsatzstatistik.jahresstatistik"));
		wrbGruppierungJahresstatistik.addActionListener(this);

		wrbGruppierungMonate.setText(LPMain.getTextRespectUISPr("kunde.umsatzstatistik.monatsstatistik"));
		wrbGruppierungMonate.addActionListener(this);
		wcbNurHauptgruppeKlasse.setText(LPMain.getTextRespectUISPr("kunde.umsatzstatistik.nurhauptgruppeklasse"));

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

		HvLayout hvLayout = HvLayoutFactory.create(this);

		hvLayout.addEmptyColumn().add(panelDateRange).span(3).add(wcbOhneDeckungsbeitrag, 200).wrap();

		hvLayout.addEmptyColumn().add(wrbPreisUmsatz).add(wrbPreisDB).add(wcbAuftragsadresse, Defaults.sizeFactor(160))
				.add(wcbStatistikadresse, Defaults.sizeFactor(200)).wrap();

		hvLayout.addAligned(Align.RIGHT, wlaGruppierung).add(wrbGruppierungArtikelklassen)
				.add(wrbGruppierungArtikelgruppen).add(wrbGruppierungFertigungsgruppen)
				.add(wrbGruppierungJahresstatistik).split(2).add(wrbGruppierungMonate).wrap();

		hvLayout.addEmptyColumn().add(wcbNurHauptgruppeKlasse, 160).span(2).add(wcoOptionSortierungJahre, 90).wrap();

		hvLayout.addAligned(Align.RIGHT, wlaSortierung).add(wrbSortUmsatz).add(wrbSortName).add(wrbSortLkz).add(wcbSortiertNachProvisionsempfaenger,230).wrap();

		hvLayout.addAligned(Align.RIGHT, wlaKundengruppierung).add(wrbKundGruppBranche).add(wrbKundGruppPartnerklasse)
				.add(wrbKundGruppKeine).add(wcbMitNichtLagerbewertetenArtikeln, Defaults.sizeFactor(200));
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
		} else if (wrbGruppierungJahresstatistik.isSelected()) {
			iOptionGruppierung = new Integer(LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_JAHR);
		} else if (wrbGruppierungMonate.isSelected()) {
			iOptionGruppierung = new Integer(LagerReportFac.REPORT_KUNDEUMSATZSTATISTIK_OPTION_GRUPPIERUNG_MONAT);
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

		if (wcoOptionSortierungJahre.isVisible()) {
			return DelegateFactory.getInstance().getLagerReportDelegate().printKundeUmsatzstatistik(
					// wdfVon.getTimestamp(),
					panelDateRange.getWdfVon().getTimestamp(),
					// wdfBis.getTimestamp(),
					panelDateRange.getWdfBis().getTimestamp(), iOptionKundeGruppierung, wrbPreisUmsatz.isSelected(),
					iOptionGruppierung, wcbNurHauptgruppeKlasse.isSelected(), iOptionSortierung,
					(Integer) wcoOptionSortierungJahre.getKeyOfSelectedItem(), wcbAuftragsadresse.isSelected(),
					wcbStatistikadresse.isSelected(), wcbMitNichtLagerbewertetenArtikeln.isSelected(),
					wcbOhneDeckungsbeitrag.isSelected(), wcbSortiertNachProvisionsempfaenger.isSelected());
		} else {
			return DelegateFactory.getInstance().getLagerReportDelegate()
					// .printKundeUmsatzstatistik(wdfVon.getTimestamp(),
					.printKundeUmsatzstatistik(panelDateRange.getWdfVon().getTimestamp(),
							// wdfBis.getTimestamp(), iOptionKundeGruppierung,
							panelDateRange.getWdfBis().getTimestamp(), iOptionKundeGruppierung,
							wrbPreisUmsatz.isSelected(), iOptionGruppierung, wcbNurHauptgruppeKlasse.isSelected(),
							iOptionSortierung, null, wcbAuftragsadresse.isSelected(), wcbStatistikadresse.isSelected(),
							wcbMitNichtLagerbewertetenArtikeln.isSelected(), wcbOhneDeckungsbeitrag.isSelected(),
							wcbSortiertNachProvisionsempfaenger.isSelected());
		}

	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
