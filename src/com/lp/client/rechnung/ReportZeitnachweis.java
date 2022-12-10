
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

import java.util.ArrayList;
import java.util.Locale;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportLogoIfJRDS;
import com.lp.client.pc.LPMain;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerReportFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ProjektzeitenDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.rechnung.service.ZeitinfoTransferDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um einen Serienbrief.
 * </p>
 *
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 *
 * <p>
 * Erstellung: Josef Ornetsmueller; 02.12.05
 * </p>
 *
 * <p>
 * 
 * @author $Author: sebastian $
 *         </p>
 *
 * @version not attributable Date $Date: 2009/08/25 13:19:50 $
 */
public class ReportZeitnachweis extends PanelBasis implements PanelReportIfJRDS, PanelReportLogoIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Integer> zeitdatenIIds = null;
	private boolean bPrintLogo = false;
	private java.sql.Date dateVon = null;
	private java.sql.Date dateBis = null;

	public ReportZeitnachweis(InternalFrame internalFrame, ArrayList<Integer> zeitdatenIIds,java.sql.Date dateVon, java.sql.Date dateBis,
			String add2Title) throws Throwable {

		super(internalFrame, add2Title);

		this.zeitdatenIIds = zeitdatenIIds;
		this.dateVon = dateVon;
		this.dateBis = dateBis;
	}

	public String getModul() {
		return RechnungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return RechnungReportFac.REPORT_ZEITNACHWEIS;
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		if (zeitdatenIIds != null && zeitdatenIIds.size() > 0) {

			ProjektzeitenDto pzDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.projektzeitenFindByPrimaryKey(zeitdatenIIds.get(0));

			Integer projektIId = null;
			if (pzDto.getZeitdatenIId() != null) {
				ZeitdatenDto zDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.zeitdatenFindByPrimaryKey(pzDto.getZeitdatenIId());
				projektIId = zDto.getIBelegartid();
			} else if (pzDto.getTelefonzeitenIId() != null) {
				TelefonzeitenDto tzDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.telefonzeitenFindByPrimaryKey(pzDto.getTelefonzeitenIId());
				projektIId = tzDto.getProjektIId();
			}

			ProjektDto projektDto = DelegateFactory.getInstance().getProjektDelegate()
					.projektFindByPrimaryKey(projektIId);

			PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
					.partnerFindByPrimaryKey(projektDto.getPartnerIId());
			mailtextDto.setMailPartnerIId(projektDto.getPartnerIId());
			mailtextDto.setMailAnprechpartnerIId(projektDto.getAnsprechpartnerIId());
			mailtextDto.setParamLocale(Helper.string2Locale(partnerDto.getLocaleCNrKommunikation()));

			mailtextDto.setMailBelegnummer(projektDto.getCNr());
			mailtextDto.setMailBezeichnung(LPMain.getTextRespectSpezifischesLocale("rech.zeitnachweis.mailbezeichnung",
					Helper.string2Locale(partnerDto.getLocaleCNrKommunikation())));

			PersonalDto personalDtoBearbeiter = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());
			mailtextDto.setMailVertreter(personalDtoBearbeiter);
			mailtextDto.setBisDatum(dateBis);

		}

		return mailtextDto;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		ArrayList<ZeitinfoTransferDto> prints = DelegateFactory.getInstance().getRechnungDelegate()
				.printZeitnachweis(zeitdatenIIds, dateVon, dateBis);

		JasperPrintLP jasperPrint = null;

		for (int i = 0; i < prints.size(); i++) {

			if (i == 0) {
				jasperPrint = prints.get(i).getPrint();
			} else {
				jasperPrint = Helper.addReport2Report(jasperPrint, prints.get(i).getPrint());
			}

		}

		return jasperPrint;
	}

	public boolean isBPrintLogo() {
		return bPrintLogo;
	}

	public void setBPrintLogo(boolean bPrintLogo) {
		this.bPrintLogo = bPrintLogo;
	}

}
