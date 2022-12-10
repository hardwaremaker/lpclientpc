

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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.lieferschein.service.LieferscheinReportFac;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
/*
 * <p>Report ReportWepEtikett <p>Copyright Logistik Pur Software GmbH (c)
 * 2004-2008</p> <p>Erstellungsdatum 28.11.07</p> <p> </p>
 * 
 * @author Victor Finder
 * 
 * @version $Revision: 1.3 $
 */
public class ReportPackstuecke extends PanelBasis implements
		PanelReportIfJRDS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	protected JPanel jpaWorkingOn = new JPanel();
	private WrapperLabel wlaPackstueck = null;
	protected WrapperTextField wtfPackstueck = null;

	public ReportPackstuecke(InternalFrame internalFrame,
			 String sAdd2Title) throws Throwable {
		super(internalFrame, sAdd2Title);
	
		jbInit();
		setDefaults();
	}

	private void jbInit() throws Throwable {

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		jpaWorkingOn.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);
		wlaPackstueck = new WrapperLabel();
		wlaPackstueck.setText(LPMain.getInstance().getTextRespectUISPr(
				"ls.report.packstuecke.filter"));
		HelperClient.setMinimumAndPreferredSize(wlaPackstueck, HelperClient.getSizeFactoredDimension(130));
		wtfPackstueck = new WrapperTextField();
		wtfPackstueck.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wtfPackstueck.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		
		

		


		this.setLayout(new GridBagLayout());
		jpaWorkingOn.add(wlaPackstueck, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfPackstueck, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 200, 0));
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(2, iZeile,
				1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		this.add(jpaWorkingOn, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

	}

	private void setDefaults() throws Throwable {
		
	}

	public String getModul() {
		return LieferscheinReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return LieferscheinReportFac.REPORT_LIEFERSCHEIN_PACKSTUECKE;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory
				.getInstance()
				.getLieferscheinReportDelegate().printPackstuecke(wtfPackstueck.getText());
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
	}
}
