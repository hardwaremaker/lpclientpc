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
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ReportAuslastungsvorschauDetailiert extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private WrapperLabel wlaStichtag = new WrapperLabel();
	private WrapperDateField wdfStichtag = new WrapperDateField();


	
	protected JPanel jpaWorkingOn = new JPanel();

	public ReportAuslastungsvorschauDetailiert(InternalFrame internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);
		jbInit();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfStichtag;
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);
		jpaWorkingOn.setLayout(new GridBagLayout());
		wlaStichtag.setText(LPMain.getInstance().getTextRespectUISPr("lp.stichtag"));
		wdfStichtag.setMandatoryField(true);
		
		//Stichtag ist mit uebernaechsten Sonntag vorbesetzt
		
		Calendar c=Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		
		c.add(Calendar.DATE, 7);
		c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);

		java.sql.Timestamp wdfBisTemp = new java.sql.Timestamp(c.getTimeInMillis());
		wdfStichtag.setTimestamp(wdfBisTemp);
		
	
		

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		int iZeile = 0;

		jpaWorkingOn.add(wlaStichtag, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfStichtag, new GridBagConstraints(1, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
	}

	public String getModul() {
		return FertigungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FertigungReportFac.REPORT_AUSLASTUNGSVORSCHAU_DETAILIERT;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		java.sql.Timestamp wdfBisTemp = Helper.addiereTageZuTimestamp(wdfStichtag
				.getTimestamp(),1);

		return DelegateFactory.getInstance().getFertigungDelegate()
				.printAuslastungsvorschauDetailliert(wdfBisTemp);

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
