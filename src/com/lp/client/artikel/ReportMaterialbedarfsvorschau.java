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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerReportFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ReportMaterialbedarfsvorschau extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperCheckBox wlaVon = new WrapperCheckBox();
	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();
	private WrapperDateRangeController wdrBereich = null;
	
	private WrapperCheckBox wcbNurVerrechnetePositionen = new WrapperCheckBox();

	private WrapperCheckBox wlaRealisierungstermin = new WrapperCheckBox();
	private WrapperDateField wdfRealisierungstermin = new WrapperDateField();

	private WrapperCheckBox wlaOffeneRahmenauftraege = new WrapperCheckBox();
	private WrapperDateField wdfOffeneRahmenauftraege = new WrapperDateField();
	private WrapperCheckBox wlaAngelegteAuftraege = new WrapperCheckBox();
	private WrapperDateField wdfAngelegteAuftraege = new WrapperDateField();

	private WrapperSelectField wsfArtikelgruppe = null;

	private WrapperSelectField wsfKunde = null;

	public ReportMaterialbedarfsvorschau(InternalFrameArtikel internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getInstance().getTextRespectUISPr(
				"artikel.report.artikelgruppen");
		jbInit();
		initComponents();

		Calendar c = Calendar.getInstance();

		int iJahr = c.get(Calendar.YEAR);

		c.set(c.DAY_OF_MONTH, c.get(c.DAY_OF_MONTH) - 1);
		c.set(c.MINUTE, 0);
		c.set(c.HOUR, 0);
		c.set(c.SECOND, 0);
		c.set(c.MILLISECOND, 0);
		wdfBis.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		c.set(c.YEAR, c.get(c.YEAR) - 1);

		wdfVon.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));

		Calendar cRealTermin = Calendar.getInstance();

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_MATERIALBEDARFSVORSCHAU_OFFSET_REALISIERUNGSTERMIN,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && parameter.getCWert().length() > 0) {
			cRealTermin.add(Calendar.MONTH,
					(Integer) parameter.getCWertAsObject());
		} else {
			cRealTermin.add(Calendar.MONTH, 3);
		}

		wdfRealisierungstermin.setDate(cRealTermin.getTime());

		Timestamp[] tsBeginnEnde = DelegateFactory.getInstance()
				.getBuchenDelegate().getDatumVonBisGeschaeftsjahr(iJahr);

		wdfAngelegteAuftraege.setTimestamp(tsBeginnEnde[1]);
		wdfOffeneRahmenauftraege.setTimestamp(tsBeginnEnde[1]);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		}

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);

		wlaVon.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.report.materialbedarfsvorschau.vergangenheit.von"));
		wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));

		wlaRealisierungstermin
				.setText(LPMain
						.getInstance()
						.getTextRespectUISPr(
								"artikel.report.materialbedarfsvorschau.realisierungstermin"));
		wlaOffeneRahmenauftraege.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"artikel.report.materialbedarfsvorschau.offenerahmen"));
		wlaAngelegteAuftraege.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.report.materialbedarfsvorschau.angelegteauftraege"));
		wcbNurVerrechnetePositionen.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.materiabedarfsvorschau.nurverrechnete"));
		wcbNurVerrechnetePositionen.setSelected(true);

		
		
		wsfArtikelgruppe = new WrapperSelectField(
				WrapperSelectField.ARTIKELGRUPPE, getInternalFrame(), true);
		HelperClient.setMinimumAndPreferredSize(wsfArtikelgruppe.getWrapperButton(), HelperClient.getSizeFactoredDimension(100));
		wsfKunde = new WrapperSelectField(WrapperSelectField.KUNDE,
				getInternalFrame(), true);

		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);
		wdfAngelegteAuftraege.setMandatoryField(true);
		wdfOffeneRahmenauftraege.setMandatoryField(true);
		wdfRealisierungstermin.setMandatoryField(true);

		//wlaAngelegteAuftraege.setSelected(true);
		wlaRealisierungstermin.setSelected(true);
		wlaVon.setSelected(true);
		//wlaOffeneRahmenauftraege.setSelected(true);

		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, 5, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 270, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(2, 5, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 80, 0));
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(3, 5, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 90, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(4, 5, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 200, 0));
		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(5, 5, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		
		
		jpaWorkingOn.add(wcbNurVerrechnetePositionen, new GridBagConstraints(2, 6, 3, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 80, 0));
		

		jpaWorkingOn.add(wlaRealisierungstermin, new GridBagConstraints(0, 8,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfRealisierungstermin, new GridBagConstraints(2, 8,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaOffeneRahmenauftraege, new GridBagConstraints(0, 9,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfOffeneRahmenauftraege, new GridBagConstraints(2, 9,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaAngelegteAuftraege, new GridBagConstraints(0, 10,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfAngelegteAuftraege, new GridBagConstraints(2, 10,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		
		jpaWorkingOn.add(wsfKunde.getWrapperButton(), new GridBagConstraints(3, 9, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfKunde.getWrapperTextField(), new GridBagConstraints(4, 9, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfArtikelgruppe.getWrapperButton(), new GridBagConstraints(3, 10, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfArtikelgruppe.getWrapperTextField(), new GridBagConstraints(4, 10, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		

	}

	public String getModul() {
		return LagerReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return LagerReportFac.REPORT_MATERIALBEDARFSVORSCHAU;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		return DelegateFactory
				.getInstance()
				.getLagerReportDelegate()
				.printMaterialbedarfsvorschau(
						wlaVon.isSelected() ? wdrBereich.getTimestampVon() : null,
						wlaVon.isSelected() ? wdrBereich.getTimestampBis(): null,
						wlaRealisierungstermin.isSelected() ? wdfRealisierungstermin
								.getTimestamp() : null,
						wlaOffeneRahmenauftraege.isSelected() ? wdfOffeneRahmenauftraege
								.getTimestamp() : null,
						wlaAngelegteAuftraege.isSelected() ? wdfAngelegteAuftraege
								.getTimestamp() : null, wsfKunde.getIKey(),
						wsfArtikelgruppe.getIKey(),wcbNurVerrechnetePositionen.isSelected());
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
