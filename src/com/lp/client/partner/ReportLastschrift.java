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

import javax.swing.JComponent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.PanelReportLogoIfJRDS;
import com.lp.server.partner.service.PartnerReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p> Diese Klasse kuemmert sich den Kurzbrief.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 11.11.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2012/05/16 09:09:42 $
 */
public class ReportLastschrift extends PanelBasis implements PanelReportIfJRDS, PanelReportLogoIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer partnerbankIId = null;
	private boolean bPrintLogo = false;

	public ReportLastschrift(InternalFrame internalFrame, String add2Title,
			Integer partnerbankIId) throws Throwable {

		super(internalFrame, add2Title);

		this.partnerbankIId = partnerbankIId;
	}

	public String getModul() {
		return PartnerReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return PartnerReportFac.REPORT_PART_KURZBRIEF;
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);

		return mailtextDto;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		return DelegateFactory.getInstance().getKundeDelegate()
				.printLastschrift(partnerbankIId, bPrintLogo);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}

	public boolean isBPrintLogo() {
		return bPrintLogo;
	}

	public void setBPrintLogo(boolean bPrintLogo) {
		this.bPrintLogo = bPrintLogo;
	}

}