package com.lp.client.auftrag;

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
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

/**
 * <p>
 * Report Auftragnachkalkulation.
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 12.07.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */
public class ReportTeilnehmer extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private ButtonGroup buttonGroupSortierung = new ButtonGroup();

	private JLabel wlaSortierung = new JLabel();

	private WrapperRadioButton wrbSortAuftrag = new WrapperRadioButton();
	private WrapperRadioButton wrbSortKunde = new WrapperRadioButton();
	private WrapperRadioButton wrbSortTeilnehmer = new WrapperRadioButton();

	private WrapperCheckBox wcbNurOffene = new WrapperCheckBox();

	public ReportTeilnehmer(InternalFrame internalFrame, String sAdd2Title) throws Throwable {
		super(internalFrame, sAdd2Title);
		jbInit();

	}

	public String getModul() {
		return AuftragReportFac.REPORT_MODUL;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		getInternalFrame().addItemChangedListener(this);

		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, Calendar.DECEMBER);
		c.set(Calendar.DAY_OF_MONTH, 31);

		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));

		wrbSortAuftrag.setText(LPMain.getTextRespectUISPr("report.auftrag.teilnehmer.sortauftrag"));
		wrbSortKunde.setText(LPMain.getTextRespectUISPr("report.auftrag.teilnehmer.sortkunde"));
		wrbSortTeilnehmer.setText(LPMain.getTextRespectUISPr("report.auftrag.teilnehmer.sorteilnehmer"));
		
		wcbNurOffene.setText(LPMain.getTextRespectUISPr("report.auftrag.teilnehmer.nuroffene"));

		
		
		
		buttonGroupSortierung.add(wrbSortAuftrag);
		buttonGroupSortierung.add(wrbSortKunde);
		buttonGroupSortierung.add(wrbSortTeilnehmer);

		wrbSortAuftrag.setSelected(true);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wrbSortAuftrag, new GridBagConstraints(1, iZeile, 1, 1, 1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		iZeile++;
		
		jpaWorkingOn.add(wrbSortKunde, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		iZeile++;
		jpaWorkingOn.add(wrbSortTeilnehmer, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wcbNurOffene, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	public String getReportname() {
		return AuftragReportFac.REPORT_TEILNEHMER;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		int iSortierung = AuftragReportFac.SORT_REPORT_TEILNEHMER_AUFTRAG;
		if (wrbSortKunde.isSelected()) {
			iSortierung = AuftragReportFac.SORT_REPORT_TEILNEHMER_KUNDE;
		} else if (wrbSortTeilnehmer.isSelected()) {
			iSortierung = AuftragReportFac.SORT_REPORT_TEILNEHMER_TEILNEHMER;
		}

		return DelegateFactory.getInstance().getAuftragReportDelegate().printAuftragteilnehmer(iSortierung,
				wcbNurOffene.isSelected());
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
