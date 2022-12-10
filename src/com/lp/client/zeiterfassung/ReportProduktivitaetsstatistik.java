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
import java.sql.Timestamp;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class ReportProduktivitaetsstatistik extends ReportZeiterfassung implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton wbuGestern = null;

	private WrapperCheckBox wcbVerdichtet = new WrapperCheckBox();
	private WrapperCheckBox wcbMonatsstatstik = new WrapperCheckBox();
	private WrapperCheckBox wcbProjekteZusammengefasst = new WrapperCheckBox();

	public ReportProduktivitaetsstatistik(InternalFrameZeiterfassung internalFrame, String add2Title) throws Throwable {
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
		wcbVerdichtet.setText(LPMain.getTextRespectUISPr("lp.verdichtet"));
		wcbMonatsstatstik.setText(LPMain.getTextRespectUISPr("pers.report.produktivitaetsstatistik.monatsbetrachtung"));
		wcbProjekteZusammengefasst.setText(LPMain.getTextRespectUISPr("pers.report.produktivitaetsstatistik.projektezusammengefasst"));

		
		
		wbuGestern = new JButton(new ImageIcon(getClass().getResource("/com/lp/client/res/table_selection_cell.png")));
		wbuGestern.setToolTipText(
				LPMain.getTextRespectUISPr("pers.zeiterfassung.produktivitaetsstatistik.datummitgesternvorbesetzen"));
		wbuGestern.addActionListener(this);

		wcbVerdichtet.setSelected(true);

		jpaWorkingOn.add(wcbVerdichtet, "cell 1 5");
		jpaWorkingOn.add(wcbMonatsstatstik, "cell 2 5");
		jpaWorkingOn.add(wcbProjekteZusammengefasst, "cell 3 5, wrap");

		addZeitraumAuswahl();

		jpaWorkingOn.add(wbuGestern, "growy");

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getSource().equals(wbuGestern)) {
			wdfVon.setTimestamp(
					Helper.cutTimestamp(Helper.addiereTageZuTimestamp(new Timestamp(System.currentTimeMillis()), -1)));
			wdfBis.setTimestamp(
					Helper.cutTimestamp(Helper.addiereTageZuTimestamp(new Timestamp(System.currentTimeMillis()), -1)));
		}
	}

	public String getModul() {
		return ZeiterfassungFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ZeiterfassungFac.REPORT_PRODUKTIVITAETSSTATISTIK;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP jasperPrint = null;

		java.sql.Timestamp wdfBisTemp = Helper.addiereTageZuTimestamp(wdfBis.getTimestamp(), 1);

		jasperPrint = DelegateFactory.getInstance().getZeiterfassungDelegate().printProduktivitaetsstatistik(
				getPersonalIId(), wdfVon.getTimestamp(), wdfBisTemp, mitVersteckten(), nurAnwesende(),
				wcbVerdichtet.isSelected(), wcbMonatsstatstik.isSelected(), wcbProjekteZusammengefasst.isSelected(),
				getPersonAuswahl(),getKostenstelleIIdAbteilung());

		return jasperPrint;

	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
