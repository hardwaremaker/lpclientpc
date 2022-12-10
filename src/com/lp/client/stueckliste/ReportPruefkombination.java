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
package com.lp.client.stueckliste;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportPruefkombination extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private InternalFrameStueckliste internalFrameStueckliste = null;

	private WrapperSelectField wsfArtikelKontakt = new WrapperSelectField(
			WrapperSelectField.ARTIKEL_OHNE_ARBEISZEIT, getInternalFrame(),
			true);
	private WrapperSelectField wsfArtikelLitze = new WrapperSelectField(
			WrapperSelectField.ARTIKEL_OHNE_ARBEISZEIT, getInternalFrame(),
			true);

	private WrapperLabel textsucheLitze = new WrapperLabel();
	private WrapperTextField wtfTextsucheLitze = new WrapperTextField();

	public ReportPruefkombination(InternalFrameStueckliste internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getInstance().getTextRespectUISPr("stkl.stueckliste");
		internalFrameStueckliste = internalFrame;

		jbInit();
		initComponents();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	private void jbInit() throws Exception {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		internalFrameStueckliste.addItemChangedListener(this);

		wsfArtikelKontakt.getWrapperGotoButton().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"stk.pruefkombination.kontakt"));
		wsfArtikelLitze.getWrapperGotoButton().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"stk.pruefkombination.litze"));

		textsucheLitze.setText(LPMain.getInstance().getTextRespectUISPr(
				"stk.pruefkombination.textsuchelitze"));

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.add(wsfArtikelKontakt.getWrapperGotoButton(),
				new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfArtikelKontakt.getWrapperTextField(),
				new GridBagConstraints(1, 0, 2, 1, 1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn.add(wsfArtikelLitze.getWrapperGotoButton(),
				new GridBagConstraints(0, 1, 1, 1, 0.5, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfArtikelLitze.getWrapperTextField(),
				new GridBagConstraints(1, 1, 2, 1, 1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn.add(textsucheLitze, new GridBagConstraints(0, 2, 1, 1,
				0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfTextsucheLitze, new GridBagConstraints(1, 2, 2, 1,
				1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public String getModul() {
		return StuecklisteReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return StuecklisteReportFac.REPORT_STUECKLISTE_PRUEFKOMBINATION;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory
				.getInstance()
				.getStuecklisteReportDelegate()
				.printPruefkombinationen(wsfArtikelKontakt.getIKey(),
						wsfArtikelLitze.getIKey(), wtfTextsucheLitze.getText());
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
