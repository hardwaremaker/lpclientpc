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
package com.lp.client.auftrag;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.swing.JPanel;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p>Report Auftragnachkalkulation.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 12.07.05</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.9 $
 */
public class ReportAuftragnachkalkulation extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Integer auftragIId = null;
	private WrapperCheckBox wcbArbeitszeitDetailliert = new WrapperCheckBox();

	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperLabel wlaStichtag = new WrapperLabel();
	private WrapperDateField wdfStichtag = new WrapperDateField();

	public ReportAuftragnachkalkulation(InternalFrame internalFrame,
			Integer iIdAuftragI, String sAdd2Title) throws Throwable {
		super(internalFrame, sAdd2Title);

		auftragIId = iIdAuftragI;
		jbInit();
	}

	public String getModul() {
		return AuftragReportFac.REPORT_MODUL;
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		wlaStichtag.setText(LPMain.getTextRespectUISPr("lp.stichtag"));
		wcbArbeitszeitDetailliert.setText(LPMain.getTextRespectUISPr("auft.nachkalkulation.zeitendetailliert"));
		wcbArbeitszeitDetailliert.setSelected(false);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbArbeitszeitDetailliert, new GridBagConstraints(1,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaStichtag, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 150, 0));
		jpaWorkingOn.add(wdfStichtag, new GridBagConstraints(1, iZeile, 1, 1,
				1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public String getReportname() {
		return AuftragReportFac.REPORT_AUFTRAGSTATISTIK;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2099);

		AuftragDto auftragDto = DelegateFactory.getInstance()
				.getAuftragDelegate().auftragFindByPrimaryKey(auftragIId);

		return DelegateFactory
				.getInstance()
				.getAuftragReportDelegate()
				.printAuftragstatistik(
						new Timestamp(0),
						new Timestamp(c.getTimeInMillis()),
						true,
						null,
						null,
						auftragIId,
						null,
						null,
						AuftragReportFac.REPORT_AUFTRAGSTATISTIK_SORTIERUNG_AUFTRAG,
						!wcbArbeitszeitDetailliert.isSelected(),
						wdfStichtag.getTimestamp(), null);
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
