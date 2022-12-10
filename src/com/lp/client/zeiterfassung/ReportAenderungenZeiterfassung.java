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
package com.lp.client.zeiterfassung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportAenderungenZeiterfassung extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperLabel wlaZeitbuchungVon = new WrapperLabel();
	private WrapperDateField wdfZeitbuchungVon = new WrapperDateField();
	private WrapperLabel wlaZeitbuchungBis = new WrapperLabel();
	private WrapperDateField wdfZeitbuchungBis = new WrapperDateField();
	private WrapperLabel wlaAenderungVon = new WrapperLabel();
	private WrapperDateField wdfAenderungVon = new WrapperDateField();
	private WrapperLabel wlaAenderungBis = new WrapperLabel();
	private WrapperDateField wdfAenderungBis = new WrapperDateField();
	private WrapperLabel wbuPersonal = new WrapperLabel();
	
	private WrapperLabel wlaSortierung = new WrapperLabel();
	private WrapperComboBox wcbSortierung = new WrapperComboBox();

	private WrapperCheckBox wcbInserts = new WrapperCheckBox();
	private WrapperCheckBox wcbUpdates = new WrapperCheckBox();
	private WrapperCheckBox wcbDeletes = new WrapperCheckBox();

	private WrapperTextField wtfPersonal = new WrapperTextField();

	private Integer personalIId = null;
	private WrapperDateRangeController wdrZeitbuchungBereich = null;
	private WrapperDateRangeController wdrAenderungBereich = null;

	public ReportAenderungenZeiterfassung(
			InternalFrameZeiterfassung internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);
		
		jbInit();
		initComponents();

		wtfPersonal.setText(internalFrame.getPersonalDto().formatAnrede());
		personalIId = internalFrame.getPersonalDto().getIId();

		wdrZeitbuchungBereich.doClickDown();
		wdrZeitbuchungBereich.doClickUp();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfZeitbuchungVon;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		return DelegateFactory
				.getInstance()
				.getZeiterfassungReportDelegate()
				.printAenderungen(personalIId, wdrZeitbuchungBereich.getDatumsfilterVonBis(),
						wdrAenderungBereich.getDatumsfilterVonBis(), wcbInserts.isSelected(), 
						wcbUpdates.isSelected(), wcbDeletes.isSelected(), wcbSortierung.getSelectedIndex());
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);

		wlaZeitbuchungVon.setText(LPMain.getTextRespectUISPr("lp.zeitraum") + " "
				+ LPMain.getTextRespectUISPr("lp.von"));
		wlaZeitbuchungBis.setText(LPMain.getTextRespectUISPr("lp.bis"));
		
		wlaAenderungVon.setText(LPMain.getTextRespectUISPr("lp.report.aenderungen") + " "
				+ LPMain.getTextRespectUISPr("lp.von"));
		wlaAenderungBis.setText(LPMain.getTextRespectUISPr("lp.bis"));

		wbuPersonal
				.setText(LPMain
						.getTextRespectUISPr("zeiterfassung.report.monatsabrechnung.selektierteperson")
						+ ": ");

		wcbInserts.setText(LPMain
				.getTextRespectUISPr("pers.zeitdaten.aenderungen.inserts"));
		wcbUpdates.setText(LPMain
				.getTextRespectUISPr("pers.zeitdaten.aenderungen.updates"));
		wcbDeletes.setText(LPMain
				.getTextRespectUISPr("pers.zeitdaten.aenderungen.deletes"));

		wtfPersonal.setActivatable(false);
		wtfPersonal.setEditable(false);
		wtfPersonal.setSaveReportInformation(false);
		wdrZeitbuchungBereich = new WrapperDateRangeController(wdfZeitbuchungVon, wdfZeitbuchungBis);
		wdrAenderungBereich = new WrapperDateRangeController(wdfAenderungVon, wdfAenderungBis);
		wdfZeitbuchungVon.setMandatoryField(true);
		wdfZeitbuchungBis.setMandatoryField(true);
		wlaSortierung.setText("Sortieren nach");
		wcbSortierung = new WrapperComboBox(new String[] {
				LPMain.getTextRespectUISPr("lp.report.zeitbuchungen"), 
				LPMain.getTextRespectUISPr("pers.zeiterfassung.aenderungenreport.sort.aenderungszeitpunkt")});
		
		wcbSortierung.setSelectedIndex(0);
		
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaZeitbuchungVon, new GridBagConstraints(0, 5, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfZeitbuchungVon, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaZeitbuchungBis, new GridBagConstraints(2, 5, 1, 1, 0.05, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfZeitbuchungBis, new GridBagConstraints(3, 5, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
//		jpaWorkingOn.add(wdrZeitbuchungBereich, new GridBagConstraints(3, 5, 1, 1, 0.1,
//				0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
//				new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wlaAenderungVon, new GridBagConstraints(0, 6, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(1, 1, 1, 1), 0, 0));
		jpaWorkingOn.add(wdfAenderungVon, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaAenderungBis, new GridBagConstraints(2, 6, 1, 1, 0.05, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfAenderungBis, new GridBagConstraints(3, 6, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbInserts, new GridBagConstraints(1, 7, 1, 1, 0.05, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbUpdates, new GridBagConstraints(3, 7, 1, 1, 0.05, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbDeletes, new GridBagConstraints(5, 7, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(6, 7, 1, 1, 0.3, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbSortierung, new GridBagConstraints(7, 7, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfPersonal, new GridBagConstraints(1, 2, 5, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuPersonal, new GridBagConstraints(0, 2, 1, 1, 0.2,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		
		
	}

	public String getModul() {
		return ZeiterfassungFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ZeiterfassungReportFac.REPORT_ZEITERFASSUNG_AENDERUNGEN;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

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
