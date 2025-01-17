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
package com.lp.client.rechnung;


import java.awt.GridBagConstraints;
import java.awt.Insets;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportJournalVerkauf;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.rechnung.service.ReportRechnungJournalKriterienDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportRechnungAlleGutschriften
    extends PanelReportJournalVerkauf implements PanelReportIfJRDS
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperCheckBox wcbNurOffene = null;
  private WrapperCheckBox wcbStatistikadresse = null;

  public ReportRechnungAlleGutschriften(InternalFrame internalFrame, String add2Title)
      throws Throwable {
    super(internalFrame, add2Title);
    jbInit();
    initComponents();
 }


 protected void jbInit() {
   wcbNurOffene = new WrapperCheckBox();
   wcbNurOffene.setText(LPMain.getInstance().getTextRespectUISPr("lp.nuroffene"));
   wcbStatistikadresse = new WrapperCheckBox();
   wcbStatistikadresse.setText(LPMain.getTextRespectUISPr("rech.statistikadresseverwenden"));
   HelperClient.setMinimumAndPreferredSize(wcbNurOffene, HelperClient.getSizeFactoredDimension(80));
   jpaWorkingOn.add(wcbNurOffene,
                    new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                           GridBagConstraints.WEST,
                                           GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                           0, 0));
   jpaWorkingOn.add(wcbStatistikadresse,
                    new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
                                           GridBagConstraints.WEST,
                                           GridBagConstraints.BOTH, new Insets(2, 2, 2, 2),
                                           0, 0));
 }


  public String getModul() {
    return RechnungReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return RechnungReportFac.REPORT_GUTSCHRIFTEN_ALLE;
  }


  public boolean getBErstelleReportSofort() {
    return false;
  }


  public MailtextDto getMailtextDto()
      throws Throwable {
    MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
    return mailtextDto;
  }


  public JasperPrintLP getReport(String sDrucktype)
      throws Throwable {
    ReportRechnungJournalKriterienDto krit = new ReportRechnungJournalKriterienDto();
    befuelleKriterien(krit);
    krit.setBNurOffene(wcbNurOffene.isSelected());
    krit.setBVerwendeStatistikAdresse(wcbStatistikadresse.isSelected());
    return DelegateFactory.getInstance().getRechnungDelegate().printGutschriftenAlle(krit);
  }
}
