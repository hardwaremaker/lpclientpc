package com.lp.client.forecast;

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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastReportFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p>
 * Diese Klasse kuemmert sich um den Druck der Verfuegbarkeit
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004 - 2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 23.07.07
 * </p>
 * 
 * <p>
 * 
 * @author $Author: robert $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/10/19 13:19:03 $
 */
public class ReportBeschaffung extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	Integer forecastauftragIId;

	private WrapperLabel wlaSortierung = new WrapperLabel();

	private WrapperLabel wlaForecastart = new WrapperLabel();
	private WrapperComboBox wcbForecastart = new WrapperComboBox();
	
	private WrapperLabel wlaStatus = new WrapperLabel();
	private WrapperComboBox wcbStatus = new WrapperComboBox();
	

	private WrapperRadioButton wrbSortStklAuftragUndStklStruktur = new WrapperRadioButton();
	private WrapperRadioButton wrbSortLieferant = new WrapperRadioButton();

	private ButtonGroup buttonGroupSortierung = new ButtonGroup();

	public ReportBeschaffung(InternalFrame internalFrame,
			Integer forecastauftragIId, String sAdd2Title) throws Throwable {
		super(internalFrame, sAdd2Title);

		this.forecastauftragIId = forecastauftragIId;
		jbInit();
		initComponents();
	}

	public String getModul() {
		return ForecastReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ForecastReportFac.REPORT_BESCHAFFUNG;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		int iSort = ForecastReportFac.BESCHAFFUNG_SORTIERUNG_AUFTRAG_STRUKTUR;

		if (wrbSortLieferant.isSelected()) {
			iSort = ForecastReportFac.BESCHAFFUNG_SORTIERUNG_LIEFERANT;
		}

		return DelegateFactory
				.getInstance()
				.getForecastReportDelegate()
				.printBeschaffung(forecastauftragIId,
						(String) wcbForecastart.getKeyOfSelectedItem(),(String) wcbStatus.getKeyOfSelectedItem(), iSort);
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);

		jpaWorkingOn.setLayout(gridBagLayout2);

		wlaForecastart.setText(LPMain.getTextRespectUISPr("fc.forecastart"));
		

		wlaStatus.setText(LPMain.getTextRespectUISPr("lp.status"));
		
		
		
		wcbForecastart.setMandatoryField(true);
		wcbStatus.setMandatoryField(true);
		
		Map mFC = new LinkedHashMap();

		mFC.put(ForecastFac.FORECASTART_CALL_OFF_TAG,
				DelegateFactory
						.getInstance()
						.getForecastDelegate()
						.forecastartFindByPrimaryKey(
								ForecastFac.FORECASTART_CALL_OFF_TAG)
						.getBezeichnung());
		mFC.put(ForecastFac.FORECASTART_CALL_OFF_WOCHE,
				DelegateFactory
						.getInstance()
						.getForecastDelegate()
						.forecastartFindByPrimaryKey(
								ForecastFac.FORECASTART_CALL_OFF_WOCHE)
						.getBezeichnung());
		mFC.put(ForecastFac.FORECASTART_FORECASTAUFTRAG,
				DelegateFactory
						.getInstance()
						.getForecastDelegate()
						.forecastartFindByPrimaryKey(
								ForecastFac.FORECASTART_FORECASTAUFTRAG)
						.getBezeichnung());
		wcbForecastart.setMap(mFC);
		
		
		Map mStatus = new LinkedHashMap();

		mStatus.put(LocaleFac.STATUS_ANGELEGT,
				LocaleFac.STATUS_ANGELEGT);
		
		mStatus.put(LocaleFac.STATUS_FREIGEGEBEN,
				LocaleFac.STATUS_FREIGEGEBEN);
		wcbStatus.setMap(mStatus);
		

		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));

		wrbSortStklAuftragUndStklStruktur.setSelected(true);
		wrbSortStklAuftragUndStklStruktur.setText(LPMain
				.getTextRespectUISPr("fc.beschaffung.sort.strukt"));

		wrbSortLieferant.setText(LPMain
				.getTextRespectUISPr("fc.beschaffung.sort.lieferant"));

		buttonGroupSortierung.add(wrbSortStklAuftragUndStklStruktur);
		buttonGroupSortierung.add(wrbSortLieferant);

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		int iZeile = 0;
		jpaWorkingOn.add(wlaForecastart, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wcbForecastart,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		if(forecastauftragIId==null){
		
		jpaWorkingOn.add(wlaStatus, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wcbStatus,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		}
		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wrbSortStklAuftragUndStklStruktur,
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbSortLieferant, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	protected void eventItemchanged(EventObject eI) {

	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
