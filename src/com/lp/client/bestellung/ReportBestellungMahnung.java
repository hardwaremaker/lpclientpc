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
package com.lp.client.bestellung;

import java.sql.Timestamp;
import java.util.Locale;

import javax.swing.JComponent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.IDruckTypeReport;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportBeleg;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.service.BSMahnungDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um den Druck der Buchungen auf einem Konto
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 07.08.06
 * </p>
 * 
 * <p>
 * @author $Author: christian $
 * </p>
 * 
 * @version not attributable Date $Date: 2012/01/18 16:34:58 $
 */
public class ReportBestellungMahnung extends ReportBeleg implements
		PanelReportIfJRDS, IDruckTypeReport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer bsmahnungIId = null;
	private BestellungDto bestellungDto = null;
	private LieferantDto lieferantDto = null;

	public ReportBestellungMahnung(InternalFrame internalFrame,
			PanelBasis panelToRefresh, Integer bsmahnungIId, String sAdd2Title)
			throws Throwable {
		super(internalFrame, panelToRefresh, sAdd2Title, null, null, null);
		this.bsmahnungIId = bsmahnungIId;
		BSMahnungDto bsmahnungDto = DelegateFactory.getInstance().getBSMahnwesenDelegate().bsmahnungFindByPrimaryKey(bsmahnungIId);
		bestellungDto = DelegateFactory.getInstance().getBestellungDelegate().bestellungFindByPrimaryKey(bsmahnungDto.getBestellungIId());
		lieferantDto = DelegateFactory.getInstance().getLieferantDelegate().lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse());
		
		this.setVisible(false);
	}

	@Override
	protected String getLockMeWer() throws Exception {
		// lock ist hier egal
		return null;
	}
	
	public String getModul() {
		return BestellungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return BestellungReportFac.REPORT_BSMAHNUNG;
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory.getInstance().getBestellungDelegate()
				.printBSMahnungAusMahnlauf(bsmahnungIId, this.isBPrintLogo());
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		
		Locale locLieferant = Helper.string2Locale(lieferantDto.getPartnerDto().getLocaleCNrKommunikation());
		mailtextDto.setParamLocale(locLieferant);
		mailtextDto.setMailBetreff(LPMain.getTextRespectSpezifischesLocale("bs.einzelmahnung.mailbetreff", locLieferant)
				+ " " + bestellungDto.getCNr());
		mailtextDto.setMailPartnerIId(lieferantDto.getPartnerIId());
		mailtextDto.setMailAnprechpartnerIId(bestellungDto.getAnsprechpartnerIId());
		mailtextDto.setMailVertreter(null);
		mailtextDto.setMailBelegdatum(bestellungDto.getDBelegdatum());
		mailtextDto.setMailBelegnummer(bestellungDto.getCNr());
		mailtextDto.setMailProjekt(bestellungDto.getCBez());
		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
	}

	@Override
	protected Timestamp berechneBelegImpl() throws Throwable {
		DelegateFactory.getInstance().getBSMahnwesenDelegate()
		.mahneBSMahnlauf(bsmahnungIId);
		return null;
	}

	@Override
	protected void aktiviereBelegImpl(Timestamp t) throws Throwable {
		// nichts machen, wurde schon aktiviert
	}

}
