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
package com.lp.client.fertigung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.EventObject;

import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;

import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.IAktiviereBelegReport;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.Facade;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class ReportVerpackungsetiketten extends PanelBasis implements PanelReportIfJRDS, IAktiviereBelegReport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperTextField wtfKommentar = new WrapperTextField();
	private Integer iLosIId;

	protected JPanel jpaWorkingOn = new JPanel();

	private WrapperLabel wlaAnzahlDerEtiketten = null;
	protected WrapperNumberField wnfAnzahlDerEtiketten = null;

	private WrapperLabel wlaVerpackungsmittelmenge = null;
	protected WrapperNumberField wnfVerpackungsmittelmenge = null;

	private WrapperLabel wlaLosgroesse = new WrapperLabel();
	protected WrapperNumberField wnfLosgroesse = new WrapperNumberField();

	private WrapperLabel wlaVPEtikettengedrucktAm = new WrapperLabel();

	public ReportVerpackungsetiketten(InternalFrame internalFrame, String title, Integer iLosIId) throws Throwable {
		super(internalFrame, title);
		this.iLosIId = iLosIId;
		jbInit();
		initComponents();

	}

	private void jbInit() throws Throwable {

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		jpaWorkingOn.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);
		wlaAnzahlDerEtiketten = new WrapperLabel();
		wlaAnzahlDerEtiketten.setText(LPMain.getTextRespectUISPr("fert.vpetiketten.anzahletiketten"));

		wlaVerpackungsmittelmenge = new WrapperLabel();
		wlaVerpackungsmittelmenge.setText(LPMain.getTextRespectUISPr("artikel.verpackungsmittelmenge"));
		wnfVerpackungsmittelmenge = new WrapperNumberField();
		wlaLosgroesse.setText(LPMain.getTextRespectUISPr("label.losgroesse"));

		HelperClient.setMinimumAndPreferredSize(wlaAnzahlDerEtiketten, HelperClient.getSizeFactoredDimension(190));
		wnfAnzahlDerEtiketten = new WrapperNumberField();
		wnfAnzahlDerEtiketten.setMinimumSize(new Dimension(30, Defaults.getInstance().getControlHeight()));
		wnfAnzahlDerEtiketten.setPreferredSize(new Dimension(30, Defaults.getInstance().getControlHeight()));
		wnfAnzahlDerEtiketten.setFractionDigits(0);
		wnfAnzahlDerEtiketten.setMandatoryField(true);

		wnfAnzahlDerEtiketten.setActivatable(false);
		wnfVerpackungsmittelmenge.setActivatable(false);
		wnfLosgroesse.setActivatable(false);

		wnfAnzahlDerEtiketten.setBigDecimal(BigDecimal.ONE);

		// PJ17954
		LosDto lDto = DelegateFactory.getInstance().getFertigungDelegate().losFindByPrimaryKey(iLosIId);

		if (lDto.getTVpEtikettengedruckt() != null) {

			String ausg = LPMain.getTextRespectUISPr("fert.vpetiketten.gedrucktam") + ": ";

			ausg += Helper.formatDatum(lDto.getTVpEtikettengedruckt(), LPMain.getTheClient().getLocUi());

			if (lDto.getPersonalIIdVpEtikettengedruckt() != null) {
				PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
						.personalFindByPrimaryKey(lDto.getPersonalIIdVpEtikettengedruckt());
				ausg += " (" + personalDto.getCKurzzeichen() + ")";
			}

			wlaVPEtikettengedrucktAm.setText(ausg);

		}

		wnfLosgroesse.setBigDecimal(lDto.getNLosgroesse());
		wnfLosgroesse.setFractionDigits(Defaults.getInstance().getIUINachkommastellenLosgroesse());
		if (lDto.getStuecklisteIId() != null) {
			StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
					.stuecklisteFindByPrimaryKey(lDto.getStuecklisteIId());
			ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(stklDto.getArtikelIId());

			if (aDto.getNVerpackungsmittelmenge() != null && aDto.getNVerpackungsmittelmenge().doubleValue() != 0) {
				wnfVerpackungsmittelmenge.setBigDecimal(aDto.getNVerpackungsmittelmenge());
				double d = lDto.getNLosgroesse().doubleValue() / aDto.getNVerpackungsmittelmenge().doubleValue();
				int iExemplare = (int) Math.ceil(d);

				if (iExemplare >= 1) {
					wnfAnzahlDerEtiketten.setInteger(iExemplare);
				}
			}
		}

		wlaKommentar.setText(LPMain.getTextRespectUISPr("fertigung.report.losetikett.Kommentar") + ": ");
		wtfKommentar.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		jpaWorkingOn.add(wlaVerpackungsmittelmenge, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfVerpackungsmittelmenge, new GridBagConstraints(1, iZeile, 2, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		iZeile++;
		jpaWorkingOn.add(wlaLosgroesse, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfLosgroesse, new GridBagConstraints(1, iZeile, 2, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		iZeile++;
		jpaWorkingOn.add(wlaAnzahlDerEtiketten, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 200, 0));
		jpaWorkingOn.add(wnfAnzahlDerEtiketten, new GridBagConstraints(1, iZeile, 2, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		iZeile++;
		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfKommentar, new GridBagConstraints(1, iZeile, 2, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		iZeile++;
		jpaWorkingOn.add(wlaVPEtikettengedrucktAm, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		iZeile++;

		this.add(jpaWorkingOn, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	}

	@Override
	protected void eventItemchanged(EventObject eI) throws Throwable {
	}

	public String getReportname() {
		return FertigungReportFac.REPORT_VERPACKUNGSETIKETT;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	public String getModul() {
		return FertigungReportFac.REPORT_MODUL;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory.getInstance().getFertigungDelegate().printLosVerpackungsetiketten(iLosIId,
				wtfKommentar.getText(), wnfAnzahlDerEtiketten.getInteger());
	}

	@Override
	public void aktiviereBeleg(Timestamp t) throws Throwable {
		// TODO Auto-generated method stub
		DelegateFactory.getInstance().getFertigungDelegate().setzeVPEtikettGedruckt(iLosIId);
	}

	@Override
	public Timestamp berechneBeleg() throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refreshPanelInBackground() throws Throwable {
		// TODO Auto-generated method stub

	}

}
