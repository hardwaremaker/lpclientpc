package com.lp.client.system;

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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.projekt.service.ProjektReportFac;
import com.lp.server.reklamation.service.ReklamationReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.SystemReportFac;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportStatistik extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();

	private JRadioButton wrbTaeglich = new JRadioButton();
	private JRadioButton wrbWoechentlich = new JRadioButton();
	private JRadioButton wrbMonatlich = new JRadioButton();
	private JRadioButton wrbQurtal = new JRadioButton();
	private JRadioButton wrbJaehrlich = new JRadioButton();
	protected ButtonGroup buttonGroupS = new ButtonGroup();

	
	private WrapperDateRangeController wdrBereich = null;
	
	public ReportStatistik(InternalFrame internalFrame, String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getInstance().getTextRespectUISPr("artikl.report.projekte");
		jbInit();
		initComponents();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}

	private void jbInit() throws Exception {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);

		wlaVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));

		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);
		
		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);
	    wdrBereich.doClickUp();
		
		wrbTaeglich.setText(LPMain.getInstance().getTextRespectUISPr("system.report.statistik.taeglich"));
		wrbWoechentlich.setText(LPMain.getInstance().getTextRespectUISPr("system.report.statistik.woechentlich"));
		wrbMonatlich.setText(LPMain.getInstance().getTextRespectUISPr("system.report.statistik.monatlich"));
		wrbQurtal.setText(LPMain.getInstance().getTextRespectUISPr("system.report.statistik.quartal"));
		wrbJaehrlich.setText(LPMain.getInstance().getTextRespectUISPr("system.report.statistik.jaehrlich"));
		buttonGroupS.add(wrbTaeglich);
		buttonGroupS.add(wrbWoechentlich);
		buttonGroupS.add(wrbMonatlich);
		buttonGroupS.add(wrbQurtal);
		buttonGroupS.add(wrbJaehrlich);

		wrbTaeglich.setSelected(true);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, 5, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, 5, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, 5, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(4, 5, 1, 1, 0.1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.VERTICAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbTaeglich, new GridBagConstraints(1, 6, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbWoechentlich, new GridBagConstraints(1, 7, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbMonatlich, new GridBagConstraints(1, 8, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbQurtal, new GridBagConstraints(2, 6, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbJaehrlich, new GridBagConstraints(2, 7, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public String getModul() {
		return SystemReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return SystemReportFac.REPORT_STATISTIK;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		String sOption = "";

		int iOption = -1;

		if (wrbTaeglich.isSelected()) {
			iOption = SystemReportFac.STATISTIK_OPTION_TAEGLICH;
			sOption = LPMain.getInstance().getTextRespectUISPr("system.report.statistik.taeglich");
		} else if (wrbWoechentlich.isSelected()) {
			iOption = SystemReportFac.STATISTIK_OPTION_WOECHENTLICH;
			sOption = LPMain.getInstance().getTextRespectUISPr("system.report.statistik.woechentlich");
		} else if (wrbMonatlich.isSelected()) {
			iOption = SystemReportFac.STATISTIK_OPTION_MONATLICH;
			sOption = LPMain.getInstance().getTextRespectUISPr("system.report.statistik.monatlich");
		} else if (wrbQurtal.isSelected()) {
			iOption = SystemReportFac.STATISTIK_OPTION_QUARTAL;
			sOption = LPMain.getInstance().getTextRespectUISPr("system.report.statistik.quartal");
		} else if (wrbJaehrlich.isSelected()) {
			iOption = SystemReportFac.STATISTIK_OPTION_JAEHRLICH;
			sOption = LPMain.getInstance().getTextRespectUISPr("system.report.statistik.jaehrlich");
		}

		return DelegateFactory.getInstance().getSystemReportDelegate()
				.printStatistik(new DatumsfilterVonBis(wdrBereich.getTimestampVon(), wdrBereich.getTimestampBis()), iOption, sOption);
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
