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
package com.lp.client.lieferschein;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.AusliefervorschlagFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;


public class ReportAusliefervorschlag extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WrapperLabel wlaSortierung = new WrapperLabel();
	private WrapperRadioButton wrbSortKundeLieferadresse = new WrapperRadioButton();
	private WrapperRadioButton wrbSortArtikel = new WrapperRadioButton();
	private ButtonGroup buttonGroupSortierung = new ButtonGroup();

	private WrapperSelectField wsfKunde = null;
	private WrapperSelectField wsfLieferadresse = null;
	protected JPanel jpaWorkingOn = new JPanel();

	public ReportAusliefervorschlag(InternalFrame internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		jbInit();

	}

	protected void jbInit() throws Throwable {

		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));

		wsfKunde = new WrapperSelectField(WrapperSelectField.KUNDE,
				getInternalFrame(), true);
		wsfLieferadresse = new WrapperSelectField(WrapperSelectField.KUNDE,
				getInternalFrame(), true);
		
		wsfLieferadresse.setText(LPMain
				.getTextRespectUISPr("label.sortierungnachlieferadresse")+"...");

		wrbSortArtikel
				.setText(LPMain
						.getTextRespectUISPr("ls.ausliefervorschlag.report.sort.artikel"));
		wrbSortKundeLieferadresse
				.setText(LPMain
						.getTextRespectUISPr("ls.ausliefervorschlag.report.sort.kundelieferadresse"));

		buttonGroupSortierung.add(wrbSortArtikel);
		buttonGroupSortierung.add(wrbSortKundeLieferadresse);

		wrbSortArtikel.setSelected(true);

		jpaWorkingOn.setLayout(new GridBagLayout());
		this.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);

		jpaWorkingOn.add(wsfKunde.getWrapperButton(), new GridBagConstraints(0,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 150, 0));
		jpaWorkingOn.add(wsfKunde.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 200, 0));
		iZeile++;

		jpaWorkingOn.add(wsfLieferadresse.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wsfLieferadresse.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wrbSortArtikel, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wrbSortKundeLieferadresse, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		this.add(jpaWorkingOn);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);

		ItemChangedEvent e = (ItemChangedEvent) eI;

	}

	public String getModul() {
		return AusliefervorschlagFac.REPORT_MODUL;
	}

	public String getReportname() {
		return AusliefervorschlagFac.REPORT_AUSLIEFERVORSCHLAG;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		int iSort = AusliefervorschlagFac.SORT_AUSLIEFERVORSCHLAG_ARTIKEL;

		if (wrbSortKundeLieferadresse.isSelected()) {
			iSort = AusliefervorschlagFac.SORT_AUSLIEFERVORSCHLAG_KUNDE_LIEFERADRESSE;
		}

		return DelegateFactory
				.getInstance()
				.getAusliefervorschlagDelegate()
				.printAusliefervorschlag(wsfKunde.getIKey(),
						wsfLieferadresse.getIKey(), iSort);
	}
}
