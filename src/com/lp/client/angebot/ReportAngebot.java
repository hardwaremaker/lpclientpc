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
package com.lp.client.angebot;

import java.sql.Timestamp;
import java.util.Locale;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.IDruckTypeReport;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportBeleg;
import com.lp.client.pc.LPMain;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotReportFac;
import com.lp.server.angebot.service.AngebottextDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.HvOptional;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;


/**
 * <p>Report Angebot.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 20.07.05</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.10 $
 */
public class ReportAngebot extends ReportBeleg implements IDruckTypeReport {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private AngebotDto angebotDto = null;
	private KundeDto kundeDto = null;

	public ReportAngebot(InternalFrame internalFrame,
			PanelBasis panelToRefresh, AngebotDto angebotDtoI, String add2Title)
			throws Throwable {
		super(internalFrame, panelToRefresh, add2Title,

		LocaleFac.BELEGART_ANGEBOT, angebotDtoI.getIId(), angebotDtoI
				.getKostenstelleIId());

		angebotDto = angebotDtoI;

		kundeDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(angebotDto.getKundeIIdAngebotsadresse());

		// UW 19.04.06 der Kunde definiert keine Kopienanzahl fuer das Angebot
	}
	@Override
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ANGEBOT;
	}
	public String getReportname() {
		return AngebotReportFac.REPORT_ANGEBOT;
	}

	public String getModul() {
		return AngebotReportFac.REPORT_MODUL;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP[] aJasperPrint = DelegateFactory
				.getInstance()
				.getAngebotReportDelegate()
				.printAngebot(angebotDto.getIId(),
//						wnfKopien.getInteger(),
						getKopien(),
						new Boolean(this.isBPrintLogo()), getReportname(),
						sDrucktype);
		// Original und Kopien hintereinanderhaengen
		JasperPrintLP jasperPrint = aJasperPrint[0];
		for (int i = 1; i < aJasperPrint.length; i++) {
			jasperPrint = Helper.addReport2Report(jasperPrint,
					aJasperPrint[i].getPrint());
		}
		return jasperPrint;
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		if (angebotDto != null) {
			Locale locKunde = Helper.string2Locale(kundeDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			mailtextDto.setMailPartnerIId(kundeDto.getPartnerIId());
			mailtextDto.setMailAnprechpartnerIId(angebotDto
					.getAnsprechpartnerIIdKunde());
			// MB 13.07.06 IMS 2104
			PersonalDto personalDtoBearbeiter = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							angebotDto.getPersonalIIdVertreter());
			mailtextDto.setMailVertreter(personalDtoBearbeiter);
			mailtextDto.setMailBelegdatum(Helper.extractDate(angebotDto
					.getTBelegdatum()));
			mailtextDto.setMailBelegnummer(angebotDto.getCNr());
			mailtextDto.setMailBezeichnung(LPMain
					.getTextRespectSpezifischesLocale("angb.mailbezeichnung",
							locKunde));

			// MB 13.07.06 IMS 2104
			mailtextDto.setMailProjekt(angebotDto.getCBez());
			mailtextDto.setMailFusstext(getAngebotFusstext());
			
			mailtextDto.setProjektIId(angebotDto.getProjektIId());
			mailtextDto.setAngebotAnfragenummer(angebotDto.getCKundenanfrage());
			/**
			 * @todo die restlichen Felder befuellen
			 */
			mailtextDto.setMailText(null); // UW: kommt noch
			mailtextDto.setParamLocale(locKunde);
			
			mailtextDto.setBelegVersion(angebotDto.getIVersion());
			mailtextDto.setMailKopftext(getAngebotKopftext());
		}
		return mailtextDto;
	}
	
	private String getAngebotKopftext() throws ExceptionLP, Throwable {
		HvOptional<String> kopftext = HvOptional.ofNullable(angebotDto.getXKopftextuebersteuert());
		if (kopftext.isPresent()) {
			return kopftext.get();
		}
		
		HvOptional<AngebottextDto> defaultKopftext = HvOptional.ofNullable(
				DelegateFactory.getInstance().getAngebotServiceDelegate().getAngebotkopfDefault(
						kundeDto.getPartnerDto().getLocaleCNrKommunikation()));
		return defaultKopftext.isPresent()
				? defaultKopftext.get().getXTextinhalt()
				: null;
	}
	
	private String getAngebotFusstext() throws ExceptionLP, Throwable {
		HvOptional<String> fusstext = HvOptional.ofNullable(angebotDto.getXFusstextuebersteuert());
		if (fusstext.isPresent()) {
			return fusstext.get();
		}
		
		HvOptional<AngebottextDto> defaultFusstext = HvOptional.ofNullable(
				DelegateFactory.getInstance().getAngebotServiceDelegate().getAngebotfussDefault(
						kundeDto.getPartnerDto().getLocaleCNrKommunikation()));
		return defaultFusstext.isPresent()
				? defaultFusstext.get().getXTextinhalt()
				: null;
	}

	@Override
	protected void aktiviereBelegImpl(Timestamp t) throws Throwable {
		DelegateFactory.getInstance().getAngebotDelegate().aktiviereBelegControlled(angebotDto.getIId(), t);
	}

	@Override
	protected Timestamp berechneBelegImpl() throws Throwable {
		return DelegateFactory.getInstance().getAngebotDelegate().berechneBelegControlled(angebotDto.getIId());
	}

}
