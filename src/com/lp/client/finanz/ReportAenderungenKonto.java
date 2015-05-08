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
package com.lp.client.finanz;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportAenderungenKonto extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperLabel wlaKonto = new WrapperLabel();

	private WrapperTextField wtfKonto = new WrapperTextField();

	private Integer kontoIId = null;

	public ReportAenderungenKonto(InternalFrameFinanz internalFrame,
			Integer kontoIId, String add2Title) throws Throwable {
		super(internalFrame, add2Title);

		jbInit();
		initComponents();

		KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
				.kontoFindByPrimaryKey(kontoIId);
		wtfKonto.setText(kontoDto.getKontonrBezeichnung());
		this.kontoIId = kontoIId;

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		return DelegateFactory.getInstance().getFinanzReportDelegate()
				.printAenderungenKonto(kontoIId);
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);

		wlaKonto.setText(LPMain
				.getTextRespectUISPr("lp.konto")
				+ ": ");

		wtfKonto.setActivatable(false);
		wtfKonto.setEditable(false);
		wtfKonto.setSaveReportInformation(false);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaKonto, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKonto, new GridBagConstraints(1, 0, 1, 1, 0.3, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		

	}

	public String getModul() {
		return FinanzReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FinanzReportFac.REPORT_AENDERUNGEN_KONTO;
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