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
package com.lp.client.eingangsrechnung;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportJournalEinkauf;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportEingangsrechnungAlleEingangsrechnungen extends
		PanelReportJournalEinkauf implements PanelReportIfJRDS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung = null;
	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperRadioButton wrbFreigabedatum = new WrapperRadioButton();
	private WrapperRadioButton wrbRechnungsdatum = new WrapperRadioButton();
	private ButtonGroup buttonGroupDatum = new ButtonGroup();

	public ReportEingangsrechnungAlleEingangsrechnungen(
			TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung,
			String add2Title) throws Throwable {
		super(tabbedPaneEingangsrechnung.getInternalFrame(), add2Title);
		this.tabbedPaneEingangsrechnung = tabbedPaneEingangsrechnung;
		jbInit();
	}

	public String getModul() {
		return EingangsrechnungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_ALLE;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
	}

	public void jbInit() {

		wlaDatum.setText(LPMain
				.getTextRespectUISPr("er.journal.alle.auswertungnach"));
		wlaDatum.setHorizontalAlignment(SwingConstants.LEFT);
		wrbFreigabedatum.setText(LPMain
				.getTextRespectUISPr("er.journal.alle.freigabedatum"));
		wrbRechnungsdatum.setText(LPMain
				.getTextRespectUISPr("er.journal.alle.er.datum"));
		wrbRechnungsdatum.setSelected(true);
		buttonGroupDatum.add(wrbFreigabedatum);
		buttonGroupDatum.add(wrbRechnungsdatum);
		
		HelperClient.setMinimumAndPreferredSize(wlaDatum, HelperClient.getSizeFactoredDimension(100));

		iZeile++;
		jpaWorkingOn.add(wlaDatum, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbRechnungsdatum, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbFreigabedatum, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		ReportJournalKriterienDto krit = new ReportJournalKriterienDto();
		befuelleKriterien(krit);
		return DelegateFactory
				.getInstance()
				.getEingangsrechnungDelegate()
				.printAlle(krit, tabbedPaneEingangsrechnung.isBZusatzkosten(),
						wrbFreigabedatum.isSelected());
	}
}
