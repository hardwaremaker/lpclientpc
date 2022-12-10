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
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportAuszulieferndePositionen extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaAuszuliefernBis = new WrapperLabel();
	private WrapperDateField wdfAuszuliefernBis = new WrapperDateField();

	private WrapperLabel wlaSortierung = new WrapperLabel();
	private WrapperRadioButton wrbArtikelAusliefertermin = new WrapperRadioButton();
	private WrapperRadioButton wrbKundeArtikelAusliefertermin = new WrapperRadioButton();
	private WrapperRadioButton wrbKundeAuslieferterminArtikel = new WrapperRadioButton();
	private WrapperRadioButton wrbKundeAuslieferterminAuftragArtikel = new WrapperRadioButton();
	private ButtonGroup buttonGroupSortierung = new ButtonGroup();

	private WrapperButton wbuKunde = new WrapperButton();
	private WrapperTextField wtfKunde = new WrapperTextField();

	protected JPanel jpaWorkingOn = new JPanel();

	private Integer kundeIId = null;

	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "ACTION_SPECIAL_KUNDE_FROM_LISTE";

	private PanelQueryFLR panelQueryFLRKunde = null;

	public ReportAuszulieferndePositionen(InternalFrame internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		jbInit();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfAuszuliefernBis;
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);
		jpaWorkingOn.setLayout(new GridBagLayout());
		wlaAuszuliefernBis.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.report.auszulieferndepositionen.auszuliefernbis"));
		wlaSortierung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.sortierung"));
		wdfAuszuliefernBis.setMandatoryField(true);

		wbuKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde"));
		wrbArtikelAusliefertermin.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.report.auszulieferndepositionen.sort.artikelausliefertermin"));

		wrbKundeArtikelAusliefertermin.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.report.auszulieferndepositionen.sort.kundeartikelausliefertermin"));
		
		wrbKundeAuslieferterminArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.report.auszulieferndepositionen.sort.kundeauslieferterminartikel"));

		wrbKundeAuslieferterminAuftragArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.report.auszulieferndepositionen.sort.kundeauslieferterminauftragartikel"));
		
		
		wbuKunde.setActionCommand(ACTION_SPECIAL_KUNDE_FROM_LISTE);
		wbuKunde.addActionListener(this);

		buttonGroupSortierung.add(wrbArtikelAusliefertermin);
		buttonGroupSortierung.add(wrbKundeArtikelAusliefertermin);
		buttonGroupSortierung.add(wrbKundeAuslieferterminArtikel);
		buttonGroupSortierung.add(wrbKundeAuslieferterminAuftragArtikel);

		wtfKunde.setActivatable(false);

		wrbArtikelAusliefertermin.setSelected(true);

		Long weeks = (long)6 * 7 * 24
				* 60 * 60 * 1000;
		wdfAuszuliefernBis.setDate(new Date(System.currentTimeMillis() + weeks));
		
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		int iZeile = 0;

		jpaWorkingOn.add(wlaAuszuliefernBis, new GridBagConstraints(0, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfAuszuliefernBis, new GridBagConstraints(1, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuKunde, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbArtikelAusliefertermin, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wrbKundeAuslieferterminArtikel, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wrbKundeArtikelAusliefertermin, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wrbKundeAuslieferterminAuftragArtikel, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_FROM_LISTE)) {
			dialogQueryKunde(e);
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKunde) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				KundeDto kundeDto = DelegateFactory.getInstance()
						.getKundeDelegate().kundeFindByPrimaryKey(iId);
				kundeIId = kundeDto.getIId();
				wtfKunde.setText(kundeDto.getPartnerDto()
						.formatFixTitelName1Name2());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRKunde) {
				kundeIId = null;
				wtfKunde.setText(null);
			}
		}
	}

	private void dialogQueryKunde(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(getInternalFrame(), false, true, kundeIId);

		new DialogQuery(panelQueryFLRKunde);
	}

	public String getModul() {
		return AuftragReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return AuftragReportFac.REPORT_AUSZULIEFERNDE_POSITIONEN;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP jasperPrint = null;

		int iOptionsortierung = -1;
		if (wrbArtikelAusliefertermin.isSelected()) {
			iOptionsortierung = AuftragReportFac.REPORT_AUSZULIEFERNDE_POSITIONEN_SORTIERUNG_ARTIKEL_AUSLIEFERTERMIN;
		} else if (wrbKundeArtikelAusliefertermin.isSelected()) {
			iOptionsortierung = AuftragReportFac.REPORT_AUSZULIEFERNDE_POSITIONEN_SORTIERUNG_KUNDE_ARTIKEL_AUSLIEFERTERMIN;
		}else if (wrbKundeAuslieferterminArtikel.isSelected()) {
			iOptionsortierung = AuftragReportFac.REPORT_AUSZULIEFERNDE_POSITIONEN_SORTIERUNG_KUNDE_AUSLIEFERTEMRIN_ARTIKEL;
		}else if(wrbKundeAuslieferterminAuftragArtikel.isSelected()) {
			iOptionsortierung = AuftragReportFac.REPORT_AUSZULIEFERNDE_POSITIONEN_SORTIERUNG_KUNDE_AUSLIEFERTERMIN_AUFTRAG_ARTIKEL;
		}
		
		jasperPrint = DelegateFactory
				.getInstance()
				.getAuftragReportDelegate()
				.printAuszulieferndePositionen(wdfAuszuliefernBis.getDate(),
						kundeIId, iOptionsortierung);

		return jasperPrint;

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
