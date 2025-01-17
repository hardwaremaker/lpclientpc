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
package com.lp.client.anfrage;

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
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfrageReportFac;
import com.lp.server.anfrage.service.AnfragetextDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

/**
 * <p>
 * Report Anfrage.
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 29.06.05
 * </p>
 * <p>
 * </p>
 *
 * @author Uli Walch
 * @version $Revision: 1.9 $
 */
public class ReportAnfrage extends ReportBeleg implements IDruckTypeReport {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private AnfrageDto anfrageDto = null;
	private LieferantDto lieferantDto = null;

	public ReportAnfrage(InternalFrame internalFrame,
			PanelBasis panelToRefresh, AnfrageDto anfrageDtoI, String add2Title)
			throws Throwable {
		super(internalFrame, panelToRefresh, add2Title,
				LocaleFac.BELEGART_ANFRAGE, anfrageDtoI.getIId(), anfrageDtoI
						.getKostenstelleIId());

		anfrageDto = anfrageDtoI;

		if (anfrageDto.getLieferantIIdAnfrageadresse() != null) {
			lieferantDto = DelegateFactory
					.getInstance()
					.getLieferantDelegate()
					.lieferantFindByPrimaryKey(
							anfrageDto.getLieferantIIdAnfrageadresse());
		}

		// UW 19.04.06 der Lieferant definiert keine Kopienanzahl fuer die
		// Anfrage
	}

	@Override
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ANFRAGE;
	}

	public String getModul() {
		return AnfrageReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return AnfrageReportFac.REPORT_ANFRAGE;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		JasperPrintLP jasperPrint = null;

		JasperPrintLP[] aJasperPrint = DelegateFactory
				.getInstance()
				.getAnfrageReportDelegate()
				.printAnfrage(anfrageDto.getIId(),
//						wnfKopien.getInteger(),
						getKopien(),
						new Boolean(this.isBPrintLogo()));

		// Original und Kopien hintereinanderhaengen
		jasperPrint = aJasperPrint[0];

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
		if (anfrageDto != null && lieferantDto != null) {
			Locale locKunde = Helper.string2Locale(lieferantDto.getPartnerDto()
					.getLocaleCNrKommunikation());
			mailtextDto.setMailPartnerIId(lieferantDto.getPartnerIId());
			mailtextDto.setMailAnprechpartnerIId(anfrageDto
					.getAnsprechpartnerIIdLieferant());
			mailtextDto.setMailVertreter(null);
			mailtextDto.setMailBelegdatum(Helper.extractDate(anfrageDto
					.getTBelegdatum()));
			mailtextDto.setMailBelegnummer(anfrageDto.getCNr());
			mailtextDto.setMailBezeichnung(LPMain
					.getTextRespectSpezifischesLocale("anf.mailbezeichnung",
							locKunde));
			mailtextDto.setMailProjekt(anfrageDto.getCBez());
			mailtextDto.setProjektIId(anfrageDto.getProjektIId());
			mailtextDto.setMailFusstext(getAnfrageFusstext());
			mailtextDto.setMailKopftext(getAnfrageKopftext());
			/**
			 * @todo die restlichen Felder befuellen
			 */
			mailtextDto.setMailText(null); // UW: kommt noch
			mailtextDto.setParamLocale(locKunde);
		}
		return mailtextDto;
	}

	@Override
	protected void aktiviereBelegImpl(Timestamp t) throws Throwable {
		DelegateFactory.getInstance().getAnfrageDelegate().aktiviereBelegControlled(getIIdBeleg(), t);
	}

	@Override
	protected Timestamp berechneBelegImpl() throws Throwable {
		return DelegateFactory.getInstance().getAnfrageDelegate().berechneBelegControlled(getIIdBeleg());
	}

	private String getAnfrageKopftext() throws ExceptionLP, Throwable {
		String uebersteuert = anfrageDto.getXKopftextuebersteuert();
		if (uebersteuert != null) {
			return uebersteuert;
		}
		// Hole default wert
		AnfragetextDto defaultKopftext = DelegateFactory.getInstance().getAnfrageServiceDelegate()
				.anfragetextFindByMandantLocaleCNr(lieferantDto.getPartnerDto().getLocaleCNrKommunikation(),
						MediaFac.MEDIAART_KOPFTEXT);

		return defaultKopftext == null ? null : defaultKopftext.getXTextinhalt();

	}

	private String getAnfrageFusstext() throws ExceptionLP, Throwable {
		String uebersteuert = anfrageDto.getXFusstextuebersteuert();
		if (uebersteuert != null) {
			return uebersteuert;
		}
		// Hole default wert
		AnfragetextDto defaultFusstext = DelegateFactory.getInstance().getAnfrageServiceDelegate()
				.anfragetextFindByMandantLocaleCNr(lieferantDto.getPartnerDto().getLocaleCNrKommunikation(),
						MediaFac.MEDIAART_FUSSTEXT);

		return defaultFusstext == null ? null : defaultFusstext.getXTextinhalt();

	}
}
