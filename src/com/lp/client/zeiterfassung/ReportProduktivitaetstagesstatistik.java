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

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class ReportProduktivitaetstagesstatistik extends ReportZeiterfassung
		implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperSelectField wsfPersonalgruppe = new WrapperSelectField(
			WrapperSelectField.PERSONALGRUPPE, getInternalFrame(), true);

	private WrapperCheckBox wcMonatsbetrachtung = new WrapperCheckBox();

	public ReportProduktivitaetstagesstatistik(
			InternalFrameZeiterfassung internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, internalFrame.getPersonalDto().getIId(), add2Title);
		jbInit();
		initComponents();
		wdrBereich.doClickDown();
		wdrBereich.doClickUp();

	}
	@Override
	protected boolean showSorting() {
		return false;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	private void jbInit() throws Throwable {
		wcMonatsbetrachtung
				.setText(LPMain
						.getTextRespectUISPr("report.produktivitaetstagesstatistik.monatsbetrachtung"));
		
		jpaWorkingOn.add(wcMonatsbetrachtung, "cell 1 5");
		jpaWorkingOn.add(wsfPersonalgruppe.getWrapperButton(), "span 2, split 2");
		jpaWorkingOn.add(wsfPersonalgruppe.getWrapperTextField());
		
		addZeitraumAuswahl();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
	}

	public String getModul() {
		return ZeiterfassungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ZeiterfassungReportFac.REPORT_PRODUKTIVITAETSTAGESSTATISTIK;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP jasperPrint = null;

		java.sql.Timestamp wdfBisTemp =Helper.addiereTageZuTimestamp(new java.sql.Timestamp(wdfBis
				.getTimestamp().getTime()),1);

		jasperPrint = DelegateFactory
				.getInstance()
				.getZeiterfassungReportDelegate()
				.printProduktivitaetstagesstatistik(getPersonalIId(),
						wdfVon.getTimestamp(), wdfBisTemp, getPersonAuswahl(),getKostenstelleIIdAbteilung(),
						mitVersteckten(), nurAnwesende(), wcMonatsbetrachtung.isSelected(),wsfPersonalgruppe.getIKey());

		return jasperPrint;

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
