/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JRExporterParameter;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.HVPDFExporter;
import com.lp.util.Helper;

public class ReportProjektblatt extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer auftragIId = null;

	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JButton jbFreigabeUndDokumentenblage = new JButton();

	private String ACTION_SPEICHERN = "ACTION_SPEICHERN";

	public ReportProjektblatt(InternalFrame internalFrame, Integer iIdAuftragI,
			String sAdd2Title) throws Throwable {
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

		jbFreigabeUndDokumentenblage
				.setText(LPMain
						.getTextRespectUISPr("auft.projektblatt.freigabeunddokumentenablage"));
		jbFreigabeUndDokumentenblage.setActionCommand(ACTION_SPEICHERN);
		jbFreigabeUndDokumentenblage.addActionListener(this);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		jpaWorkingOn.add(jbFreigabeUndDokumentenblage);

		iZeile++;

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPEICHERN)) {
			JasperPrintLP print = DelegateFactory.getInstance()
					.getAuftragReportDelegate().printProjektblatt(auftragIId);

			// und in Dokumentenablage speichern
			PrintInfoDto oInfo = DelegateFactory
					.getInstance()
					.getJCRDocDelegate()
					.getPathAndPartnerAndTable(auftragIId,
							QueryParameters.UC_ID_AUFTRAG);

			DocPath docPath = null;
			if (oInfo != null) {
				docPath = oInfo.getDocPath();
			}
			if (docPath != null) {
				// Nur archivieren wenn nicht in dieser Tabelle vorhanden

				String sName = "auft_projektblatt";

				JCRDocDto jcrDocDto = new JCRDocDto();

				Integer iPartnerIId = null;
				MandantDto mandantDto = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mandantFindByPrimaryKey(
								LPMain.getTheClient().getMandant());
				iPartnerIId = mandantDto.getPartnerIId();

				Helper.setJcrDocBinaryData(jcrDocDto, print.getPrint());

				jcrDocDto.setDocPath(docPath.add(new DocNodeFile(sName)));
				jcrDocDto.setlPartner(iPartnerIId);
				jcrDocDto.setsBelegnummer("");
				jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
				jcrDocDto.setlAnleger(LPMain.getTheClient().getIDPersonal());
				jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
				jcrDocDto.setsSchlagworte(" ");
				jcrDocDto.setsName(sName);
				jcrDocDto.setsFilename(sName + ".jrprint");
				if (oInfo.getTable() != null) {
					jcrDocDto.setsTable(oInfo.getTable());
				} else {
					jcrDocDto.setsTable(" ");
				}
				jcrDocDto.setsRow(" ");

				jcrDocDto.setsMIME(".jrprint");
				jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
				jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
				jcrDocDto.setbVersteckt(false);
				jcrDocDto.setlVersion(DelegateFactory.getInstance()
						.getJCRDocDelegate().getNextVersionNumer(jcrDocDto));
				DelegateFactory.getInstance().getJCRDocDelegate()
						.addNewDocumentOrNewVersionOfDocument(jcrDocDto);
			}

			DelegateFactory.getInstance().getAuftragDelegate()
					.auftragFreigeben(auftragIId);

		}

	}

	public String getReportname() {
		return AuftragReportFac.REPORT_AUFTRAG_PROJEKTBLATT;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		return DelegateFactory.getInstance().getAuftragReportDelegate()
				.printProjektblatt(auftragIId);
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
