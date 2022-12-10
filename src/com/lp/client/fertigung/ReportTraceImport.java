package com.lp.client.fertigung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.TraceImportDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

public class ReportTraceImport extends PanelBasis implements PanelReportIfJRDS {
	private static final long serialVersionUID = 8220763679960938410L;

	private WrapperButton wbuDurchfuehren = new WrapperButton();

	public JPanel jpaWorkingOn = new JPanel(new GridBagLayout());
	private ArrayList<String[]> alDaten = null;
	private File file = null;

	private ArrayList<TraceImportDto> alZuBuchen = null;
	private InternalFrameFertigung internalFrameFertigung = null;

	public ReportTraceImport(InternalFrameFertigung internalFrame, String add2Title, File file,
			ArrayList<String[]> alDaten) throws Throwable {
		super(internalFrame, add2Title);
		this.alDaten = alDaten;
		this.file = file;
		this.internalFrameFertigung = internalFrame;
		LPMain.getTextRespectUISPr("fert.los.traceimport");
		jbInit();
		initComponents();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getSource() != null && e.getSource().equals(wbuDurchfuehren)) {

			getInternalFrame().showPanelDialog(new PanelDialogTraceImport(internalFrameFertigung, alZuBuchen,
					LPMain.getTextRespectUISPr("fert.neuanhandauftrag"),file));

		
			/*
			 * if (b == true) { DelegateFactory.getInstance().getFertigungDelegate()
			 * .printTraceImport(filename, alDaten, true);
			 * wbuDurchfuehren.setEnabled(false);
			 * 
			 * DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
			 * LPMain.getTextRespectUISPr("fert.traceimport.durchfuehren.durchgefuehrt"));
			 * 
			 * getInternalFrame().closePanelDialog();
			 * 
			 * }
			 */
		}
	}

//	protected void eventItemchanged(EventObject eI) throws Throwable {
//	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	private void jbInit() throws Throwable {

		this.setLayout(new GridBagLayout());

		getInternalFrame().addItemChangedListener(this);

		wbuDurchfuehren.setText(LPMain.getTextRespectUISPr("fert.traceimport.durchfuehren"));

		wbuDurchfuehren.addActionListener(this);
		wbuDurchfuehren.setEnabled(false);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wbuDurchfuehren, new GridBagConstraints(2, 2, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

	}

	public String getModul() {
		return FertigungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FertigungReportFac.REPORT_TRACEIMPORT;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP print = DelegateFactory.getInstance().getFertigungDelegate().printTraceImport(file.getName(), alDaten);

		String allgemeineFehler = (String) print.getMapParameters().get("P_FEHLER_ALLGEMEIN");
		
		alZuBuchen = (ArrayList<TraceImportDto>) print.getMapParameters().get("P_DATENSAETZE_ZU_BUCHEN");

		if(allgemeineFehler == null || allgemeineFehler.length()==0) {
			wbuDurchfuehren.setEnabled(true);
		}
		
		return print;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
