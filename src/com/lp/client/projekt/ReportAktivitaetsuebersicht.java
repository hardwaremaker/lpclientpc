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
package com.lp.client.projekt;

import java.util.Calendar;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;

import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelDateRange;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.projekt.service.ProjektReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportAktivitaetsuebersicht extends PanelBasis implements
		PanelReportIfJRDS {
	private static final long serialVersionUID = 8220763679960938410L;

	private WrapperCheckBox wcbGesamtinfo = new WrapperCheckBox();
	protected ButtonGroup buttonGroupSortierung = new ButtonGroup();
	private JLabel wlaSortierung = new JLabel();

	protected WrapperRadioButton wrbSortierungBelegnummer = new WrapperRadioButton();
	protected WrapperRadioButton wrbSortierungPartner = new WrapperRadioButton();
	protected WrapperRadioButton wrbSortierungMitarbeiter = new WrapperRadioButton();

	private PanelDateRange panelDateRange = null;
	private HvLayout hvLayout = null ;

	public ReportAktivitaetsuebersicht(InternalFrameProjekt internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getTextRespectUISPr("artikel.report.ladenhueter");
		jbInit();
		initComponents();
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return panelDateRange.getWdfVon();
	}

	private void jbInit() throws Throwable {
		wcbGesamtinfo.setText(LPMain.getTextRespectUISPr(
				"proj.gesamtinfo"));

		wlaSortierung.setText(LPMain.getTextRespectUISPr(
				"lp.sortierung"));

		wrbSortierungBelegnummer.setText(LPMain.getTextRespectUISPr(
						"projekt.aktivitaetsuebersicht.sortierung.beleg"));
		wrbSortierungMitarbeiter
				.setText(LPMain.getTextRespectUISPr(
						"projekt.aktivitaetsuebersicht.sortierung.mitarbeiter"));
		wrbSortierungPartner.setText(LPMain.getTextRespectUISPr(
				"projekt.aktivitaetsuebersicht.sortierung.partner"));

		buttonGroupSortierung.add(wrbSortierungBelegnummer);
		buttonGroupSortierung.add(wrbSortierungMitarbeiter);
		buttonGroupSortierung.add(wrbSortierungPartner);
		wrbSortierungPartner.setSelected(true);

		getInternalFrame().addItemChangedListener(this);

		panelDateRange = new PanelDateRange();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.WEEK_OF_YEAR, c.get(Calendar.WEEK_OF_YEAR) - 1);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		panelDateRange.getWdfVon().setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		panelDateRange.getWdfBis().setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		hvLayout = HvLayoutFactory.create(this, "insets 10 82 10 0");

		hvLayout
			.add(panelDateRange).wrap()
			.add(wcbGesamtinfo, 250).wrap()
			.add(wlaSortierung).split(4)
			.add(wrbSortierungPartner)
			.add(wrbSortierungMitarbeiter)
			.add(wrbSortierungBelegnummer);
	}

	public String getModul() {
		return ProjektReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ProjektReportFac.REPORT_PROJEKT_JOURNAL_AKTIVITAETSUEBERSICHT;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		int iSort = ProjektReportFac.OPTION_SORTIERUNG_AKTIVITAETSUEBERSICHT_PARTNER;

		if (wrbSortierungMitarbeiter.isSelected()) {
			iSort = ProjektReportFac.OPTION_SORTIERUNG_AKTIVITAETSUEBERSICHT_MITARBEITER;
		} else if (wrbSortierungBelegnummer.isSelected()) {
			iSort = ProjektReportFac.OPTION_SORTIERUNG_AKTIVITAETSUEBERSICHT_BELEGART_BELEGNUMMER;
		}

		return DelegateFactory
				.getInstance()
				.getProjektDelegate()
				.printAktivitaetsuebersicht(panelDateRange.getWdrc().getDatumsfilterVonBis(),
						wcbGesamtinfo.isSelected(), iSort);
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
