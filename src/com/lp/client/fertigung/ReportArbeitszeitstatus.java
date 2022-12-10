package com.lp.client.fertigung;

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
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelDateRange;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.projekt.service.ProjektReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class ReportArbeitszeitstatus extends PanelBasis implements
		PanelReportIfJRDS {
	private static final long serialVersionUID = 8220763679960938410L;

	
	protected ButtonGroup buttonGroupSortierung = new ButtonGroup();
	
	private JButton wbuGestern = null;
	
	
	private PanelDateRange panelDateRange = null;
	private HvLayout hvLayout = null ;

	public ReportArbeitszeitstatus(InternalFrameFertigung internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getTextRespectUISPr("fert.los.report.arbeitszeitstatus");
		jbInit();
		initComponents();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource().equals(wbuGestern)) {
			panelDateRange.getWdfVon().setTimestamp( Helper.cutTimestamp(Helper.addiereTageZuTimestamp(new Timestamp(System
					.currentTimeMillis()),-1)));
			panelDateRange.getWdfBis().setTimestamp(Helper.cutTimestamp(Helper.addiereTageZuTimestamp(new Timestamp(System
					.currentTimeMillis()),-1)));
		}
	}
	
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return panelDateRange.getWdfVon();
	}

	private void jbInit() throws Throwable {
		

		wbuGestern = new JButton(new ImageIcon(getClass().getResource(
				"/com/lp/client/res/table_selection_cell.png")));
		wbuGestern
				.setToolTipText(LPMain
						.getTextRespectUISPr("pers.zeiterfassung.produktivitaetsstatistik.datummitgesternvorbesetzen"));
		wbuGestern.addActionListener(this);


		getInternalFrame().addItemChangedListener(this);

		panelDateRange = new PanelDateRange();

		panelDateRange.getWdrc().doClickDown();
		

		hvLayout = HvLayoutFactory.create(this, "insets 10 82 10 0");

		hvLayout
			.add(panelDateRange,300).add(wbuGestern);
	}

	public String getModul() {
		return FertigungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FertigungReportFac.REPORT_ARBEITSZEITSTATUS;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		

		return DelegateFactory
				.getInstance()
				.getFertigungDelegate().printArbeitszeitstatus(panelDateRange.getWdrc().getDatumsfilterVonBis());
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
