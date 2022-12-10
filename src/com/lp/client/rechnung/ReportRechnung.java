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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.RechnungDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.IDruckTypeReport;
import com.lp.client.frame.report.IZugferdBeleg;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportBeleg;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.rechnung.service.RechnungtextDto;
import com.lp.server.rechnung.service.ZugferdResult;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.HvOptional;
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
public class ReportRechnung extends ReportBeleg implements IZugferdBeleg, IDruckTypeReport {

	private static final long serialVersionUID = 1L;
	private RechnungDto rechnungDto = null;
	private KundeDto kundeDto = null;

	public ReportRechnung(InternalFrame internalFrame, PanelBasis panelToRefresh, RechnungDto rechnungDto,
			KundeDto kundeDto, String sAdd2Title) throws Throwable {
		// reporttitel: so gehts fuer Belege
		super(internalFrame, panelToRefresh, sAdd2Title, LocaleFac.BELEGART_RECHNUNG, rechnungDto.getIId(),
				rechnungDto.getKostenstelleIId());
		this.rechnungDto = rechnungDto;
		this.kundeDto = kundeDto;
		// vorbesetzen
		if (rechnungDto != null) {
			// super.wnfKopien.setInteger(kundeDto.getIDefaultrekopiendrucken());
			setKopien(kundeDto.getIDefaultrekopiendrucken());

		}

		if (kundeDto != null) {
			setEmpfaengerEmailAdresse(kundeDto.getCEmailRechnungsempfang());
		}
	}

	public String getModul() {
		return RechnungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return RechnungReportFac.REPORT_RECHNUNG;
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		Locale locKunde = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());
		// belegkopien: 5 den Report (Original + Kopien am Server erzeugen)
		JasperPrintLP[] prints = DelegateFactory.getInstance().getRechnungDelegate().printRechnung(rechnungDto.getIId(),
				locKunde, new Boolean(this.isBPrintLogo()),
				// wnfKopien.getInteger());
				getKopien(), sDrucktype);
		JasperPrintLP print = prints[0];
		for (int i = 1; i < prints.length; i++) {
			print = Helper.addReport2Report(print, prints[i].getPrint());
		}
		return print;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		// report_email: 2 Das Default-Mailtext-Dto holen
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		if (rechnungDto != null && kundeDto != null) {
			// report_email: 3 reportspezifische Parameter
			Locale locKunde = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());
			mailtextDto.setMailAnprechpartnerIId(rechnungDto.getAnsprechpartnerIId());
			PersonalDto personalDtoBearbeiter = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(rechnungDto.getPersonalIIdVertreter());
			mailtextDto.setMailVertreter(personalDtoBearbeiter);
			mailtextDto.setMailBelegdatum(new java.sql.Date(rechnungDto.getTBelegdatum().getTime()));
			mailtextDto.setMailBelegnummer(rechnungDto.getCNr());
			mailtextDto.setMailBezeichnung(LPMain.getTextRespectSpezifischesLocale("rech.mailbezeichnung", locKunde));
			mailtextDto.setMailFusstext(getRechnungFusstext());
			mailtextDto.setMailPartnerIId(kundeDto.getPartnerIId());
			mailtextDto.setMailText(null);
			mailtextDto.setParamLocale(locKunde);
			mailtextDto.setProjektIId(rechnungDto.getProjektIId());
			mailtextDto.setMailProjekt(rechnungDto.getCBez());
			mailtextDto.setKundenbestellnummer(rechnungDto.getCBestellnummer());
			mailtextDto.setRechnungsart(rechnungDto.getRechnungartCNr());
			mailtextDto.setMailKopftext(getRechnungKopftext());

			List<LieferscheinDto> alLs = new ArrayList<LieferscheinDto>();
			if (rechnungDto.getLieferscheinIId() != null) {
				alLs.add(DelegateFactory.getInstance().getLsDelegate()
						.lieferscheinFindByPrimaryKey(rechnungDto.getLieferscheinIId()));
			}

			RechnungPositionDto[] rePosDtos = DelegateFactory.getInstance().getRechnungDelegate()
					.rechnungPositionFindByRechnungIId(rechnungDto.getIId());

			for (RechnungPositionDto reposDto : rePosDtos) {
				if (reposDto.getLieferscheinIId() != null) {
					LieferscheinDto lsDto = DelegateFactory.getInstance().getLsDelegate()
							.lieferscheinFindByPrimaryKey(reposDto.getLieferscheinIId());
					if (!alLs.contains(lsDto)) {
						alLs.add(lsDto);
					}

				}
			}

			String versandnummer1 = null;
			String versandnummer2 = null;
			Iterator itLS = alLs.iterator();
			while (itLS.hasNext()) {

				LieferscheinDto lieferscheinDto = (LieferscheinDto) itLS.next();

				if (lieferscheinDto.getCVersandnummer() != null) {
					if (versandnummer1 != null) {
						versandnummer1 += ", " + lieferscheinDto.getCVersandnummer();
					} else {
						versandnummer1 = lieferscheinDto.getCVersandnummer();
					}
				}

				if (lieferscheinDto.getCVersandnummer2() != null) {

					if (versandnummer2 != null) {
						versandnummer2 += ", " + lieferscheinDto.getCVersandnummer2();
					} else {
						versandnummer2 = lieferscheinDto.getCVersandnummer2();
					}
				}

				mailtextDto = befuelleSpediteur(lieferscheinDto.getSpediteurIId(), mailtextDto);
				
			}

			mailtextDto.setLs_versandnummer(versandnummer1);
			mailtextDto.setLs_versandnummer(versandnummer2);

			
			
			
		}
		return mailtextDto;

	}

	@Override
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_RECHNUNG;
	}

	@Override
	protected void aktiviereBelegImpl(Timestamp t) throws Throwable {
		String statusCNrOld = rechnungDto.getStatusCNr();

		// PJ19290 Vor dem aktivieren pruefen, ob Mandatsreferenz abgelaufen ist
		// und Info anzeigen

		DelegateFactory.getInstance().getRechnungDelegate().prufeSEPAMandatsreferenz(rechnungDto);

		DelegateFactory.getInstance().getRechnungDelegate().aktiviereBelegControlled(rechnungDto.getIId(), t);
		// SP 2013/001327
		rechnungDto = DelegateFactory.getInstance().getRechnungDelegate()
				.rechnungFindByPrimaryKey(rechnungDto.getIId());

		((InternalFrameRechnung) getInternalFrame()).getTabbedPaneRechnung().setRechnungDto(rechnungDto);

		if (!rechnungDto.getStatusCNr().equals(statusCNrOld)
				&& rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_OFFEN)
				&& rechnungDto.getNWert().signum() == 0) {
			DialogFactory.showMeldung(LPMain.getTextRespectUISPr("rech.belegwert0"),
					LPMain.getTextRespectUISPr("lp.achtung"), JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
		}
	}

	private RechnungDelegate rechnungDelegate() throws Throwable {
		return DelegateFactory.getInstance().getRechnungDelegate();
	}

	@Override
	protected Timestamp berechneBelegImpl() throws Throwable {
		return rechnungDelegate().berechneBelegControlled(rechnungDto.getIId());
	}

	@Override
	public ZugferdResult createZugferdResult() throws Throwable {
		Locale locKunde = Helper.string2Locale(kundeDto.getPartnerDto().getLocaleCNrKommunikation());
		return rechnungDelegate().createZugferdRechnung(getIIdBeleg(), locKunde, new Boolean(this.isBPrintLogo()),
				JasperPrintLP.DRUCKTYP_MAIL);
	}

	@Override
	public boolean isZugferdPartner() throws Throwable {
		return rechnungDelegate().isZugferdPartner(getIIdBeleg());
	}

	private String getRechnungKopftext() throws Throwable {
		HvOptional<String> kopftext = HvOptional.ofNullable(rechnungDto.getCKopftextuebersteuert());
		if (kopftext.isPresent()) {
			return kopftext.get();
		}

		return defaultRechnungtextByMediaart(MediaFac.MEDIAART_KOPFTEXT);
	}

	private String getRechnungFusstext() throws Throwable {
		HvOptional<String> fusstext = HvOptional.ofNullable(rechnungDto.getCFusstextuebersteuert());
		if (fusstext.isPresent()) {
			return fusstext.get();
		}

		return defaultRechnungtextByMediaart(MediaFac.MEDIAART_FUSSTEXT);
	}

	private String defaultRechnungtextByMediaart(String mediaart) throws ExceptionLP, Throwable {
		HvOptional<RechnungtextDto> defaultText = HvOptional.ofNullable(
				DelegateFactory.getInstance().getRechnungServiceDelegate().rechnungtextFindByMandantLocaleCNr(
						kundeDto.getPartnerDto().getLocaleCNrKommunikation(), mediaart));
		return defaultText.isPresent() ? defaultText.get().getCTextinhalt() : null;
	}
}
