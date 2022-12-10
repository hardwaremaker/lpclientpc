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
package com.lp.client.eingangsrechnung;

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
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um den Rechnungsdruck
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 15.06.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/05/16 09:09:42 $
 */
public class ReportEingangsrechungMitPositionen extends ReportBeleg implements IDruckTypeReport {

	private static final long serialVersionUID = 1L;
	private EingangsrechnungDto eingangsrechnungDto = null;
	private LieferantDto lieferantDto = null;

	public ReportEingangsrechungMitPositionen(InternalFrame internalFrame,
			PanelBasis panelToRefresh, EingangsrechnungDto eingangsrechnungDto,
			String sAdd2Title) throws Throwable {
		// reporttitel: so gehts fuer Belege
		super(internalFrame, panelToRefresh, sAdd2Title,
				LocaleFac.BELEGART_EINGANGSRECHNUNG, eingangsrechnungDto
						.getIId(), eingangsrechnungDto.getKostenstelleIId());
		this.eingangsrechnungDto = eingangsrechnungDto;

		LieferantDto lDto = DelegateFactory
				.getInstance()
				.getLieferantDelegate()
				.lieferantFindByPrimaryKey(
						eingangsrechnungDto.getLieferantIId());

		this.lieferantDto = lDto;
		initEmpfaengerEmailAdresse();
	}

	private void initEmpfaengerEmailAdresse() throws ExceptionLP, Throwable {
		if (lieferantDto == null) return;
		
		KundeDto kundeDto = DelegateFactory
				.getInstance()
				.getKundeDelegate()
				.kundeFindByiIdPartnercNrMandantOhneExc(
						lieferantDto.getPartnerIId(),
						LPMain.getTheClient().getMandant());
		if (kundeDto != null) {
			setEmpfaengerEmailAdresse(kundeDto.getCEmailRechnungsempfang());
		}
	}
	
	public String getModul() {
		return EingangsrechnungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return EingangsrechnungReportFac.REPORT_EINGANGSRECHNUNG_MIT_POSITIONEN;
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		Locale locDruck = Helper.string2Locale(lieferantDto.getPartnerDto()
				.getLocaleCNrKommunikation());
		// belegkopien: 5 den Report (Original + Kopien am Server erzeugen)
		JasperPrintLP[] prints = DelegateFactory
				.getInstance()
				.getEingangsrechnungDelegate()
				.printEingangsrechnungMitPositionen(
						eingangsrechnungDto.getIId(), locDruck,
						new Boolean(this.isBPrintLogo()),
						// wnfKopien.getInteger());
						getKopien());
		JasperPrintLP print = prints[0];
		for (int i = 1; i < prints.length; i++) {
			print = Helper.addReport2Report(print, prints[i].getPrint());
		}
		return print;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		// report_email: 2 Das Default-Mailtext-Dto holen
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		if (eingangsrechnungDto != null && lieferantDto != null) {
			// report_email: 3 reportspezifische Parameter
			Locale locKunde = Helper.string2Locale(lieferantDto.getPartnerDto()
					.getLocaleCNrKommunikation());

			PersonalDto personalDtoBearbeiter = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							eingangsrechnungDto.getPersonalIIdAnlegen());
			mailtextDto.setMailVertreter(personalDtoBearbeiter);
			mailtextDto.setMailBelegdatum(eingangsrechnungDto.getDBelegdatum());
			mailtextDto.setMailBelegnummer(eingangsrechnungDto.getCNr());
			mailtextDto.setMailBezeichnung(LPMain
					.getTextRespectSpezifischesLocale("rech.mailbezeichnung",
							locKunde));
			mailtextDto.setMailFusstext(eingangsrechnungDto
					.getCFusstextuebersteuert());
			mailtextDto.setMailPartnerIId(lieferantDto.getPartnerIId());
			mailtextDto.setMailProjekt(null);
			mailtextDto.setMailText(null);
			mailtextDto.setParamLocale(locKunde);
		}
		return mailtextDto;
	}

	@Override
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_EINGANGSRECHNUNG;
	}

	@Override
	protected void aktiviereBelegImpl(Timestamp t) throws Throwable {

		DelegateFactory.getInstance().getEingangsrechnungDelegate()
				.aktiviereBeleg(eingangsrechnungDto.getIId());

	}

	@Override
	protected Timestamp berechneBelegImpl() throws Throwable {
		/*
		 * return DelegateFactory.getInstance().getRechnungDelegate()
		 * .berechneBelegControlled(rechnungDto.getIId());
		 */
		return null;
	}
}
