
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
import java.awt.event.ActionEvent;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastReportFac;
import com.lp.server.personal.service.FahrzeugDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
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
public class ReportGeplanterUmsatz extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	
	  
	  private WrapperLabel wlaZeitraum = new WrapperLabel();
	  private WrapperLabel wlaVon = new WrapperLabel();
	  private WrapperDateField wdfVon = new WrapperDateField();
	  private WrapperLabel wlaBis = new WrapperLabel();
	  private WrapperDateField wdfBis = new WrapperDateField();
	
	private PanelQueryFLR panelQueryFLRArtikel_Von = null;
	private PanelQueryFLR panelQueryFLRArtikel_Bis = null;

	private WrapperButton wbuArtikelnrVon = new WrapperButton();
	private WrapperButton wbuArtikelnrBis = new WrapperButton();

	private WrapperTextField wtfFilterVon = new WrapperTextField();
	private WrapperTextField wtfFilterBis = new WrapperTextField();

	static final public String ACTION_SPECIAL_ARTIKELVON_FROM_LISTE = "ACTION_SPECIAL_ARTIKELVON_FROM_LISTE";
	static final public String ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE = "ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE";

	private Integer forecastIId;

	private WrapperLabel wlaSortierung = new WrapperLabel();

	static final public String ACTION_SPECIAL_FORECAST_FROM_LISTE = "action_forecast_from_liste";

	private WrapperButton wbuForecast = new WrapperButton();
	private WrapperTextField wtfForecast = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);

	private PanelQueryFLR panelQueryFLRForecast = null;

	private WrapperRadioButton wrbSortStklAuftragUndStklStruktur = new WrapperRadioButton();
	private WrapperRadioButton wrbArtikelgruppe = new WrapperRadioButton();

	private ButtonGroup buttonGroupSortierung = new ButtonGroup();

	public ReportGeplanterUmsatz(InternalFrame internalFrame, Integer forecastauftragIId, String sAdd2Title)
			throws Throwable {
		super(internalFrame, sAdd2Title);

		jbInit();
		initComponents();

		
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		
		wdfVon.setDate(cal.getTime());


		cal.add(Calendar.MONTH, 12);
		
		wdfBis.setDate(cal.getTime());

	}

	public String getModul() {
		return ForecastReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ForecastReportFac.REPORT_BESCHAFFUNG;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		int iOption = ForecastReportFac.GEPLANTERUMSATZ_OPTION_ARTIKEL;

		if (wrbArtikelgruppe.isSelected()) {
			iOption = ForecastReportFac.GEPLANTERUMSATZ_OPTION_ARTIKELGRUPPE;
		}

		return DelegateFactory.getInstance().getForecastReportDelegate().printGeplanterUmsatz(forecastIId, iOption,
				wdfVon.getTimestamp(),
				wdfBis.getTimestamp(), wtfFilterVon.getText(),
				wtfFilterBis.getText());
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_FORECAST_FROM_LISTE)) {
			panelQueryFLRForecast = ForecastFilterFactory.getInstance().createPanelFLRForecast(getInternalFrame(),
					forecastIId, true);
			new DialogQuery(panelQueryFLRForecast);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELVON_FROM_LISTE)) {
			dialogQueryArtikelFromListe_Von(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE)) {
			dialogQueryArtikelFromListe_Bis(e);
		}
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);

		jpaWorkingOn.setLayout(gridBagLayout2);

		wbuForecast.setText(LPMain.getTextRespectUISPr("fc.forecast"));
		wtfForecast.setEditable(false);

		getInternalFrame().addItemChangedListener(this);

		wbuForecast.addActionListener(this);
		wbuForecast.setActionCommand(ACTION_SPECIAL_FORECAST_FROM_LISTE);

		 wlaZeitraum.setText(LPMain.getTextRespectUISPr("lp.zeitraum") + ":");
		    wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
		    wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));
		    
		    wdfVon.setMandatoryField(true);
		    wdfBis.setMandatoryField(true);

		wbuArtikelnrVon.setText(LPMain.getTextRespectUISPr("artikel.artikelnummer") + " "
				+ LPMain.getTextRespectUISPr("lp.von"));
		wbuArtikelnrBis.setText(LPMain.getTextRespectUISPr("artikel.artikelnummer") + " "
				+ LPMain.getTextRespectUISPr("lp.bis"));
		wbuArtikelnrVon.setActionCommand(this.ACTION_SPECIAL_ARTIKELVON_FROM_LISTE);
		wbuArtikelnrVon.addActionListener(this);

		wbuArtikelnrBis.setActionCommand(this.ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE);
		wbuArtikelnrBis.addActionListener(this);

		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));

		wrbSortStklAuftragUndStklStruktur.setSelected(true);
		wrbSortStklAuftragUndStklStruktur
				.setText(LPMain.getTextRespectUISPr("fc.report.geplanterumsatz.option.artikel"));

		wrbArtikelgruppe.setText(LPMain.getTextRespectUISPr("fc.report.geplanterumsatz.option.artikelgruppe"));

		buttonGroupSortierung.add(wrbSortStklAuftragUndStklStruktur);
		buttonGroupSortierung.add(wrbArtikelgruppe);

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		int iZeile = 0;
		jpaWorkingOn.add(wbuForecast, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 120, 0));
		jpaWorkingOn.add(wtfForecast, new GridBagConstraints(1, iZeile, 3, 1, 0, 0.4, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 300, 0));

		iZeile++;

		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortStklAuftragUndStklStruktur, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 60, 0));

		jpaWorkingOn.add(wrbArtikelgruppe, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		iZeile++;

		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 150, 0));

		iZeile++;
		jpaWorkingOn.add(wbuArtikelnrVon, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFilterVon, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuArtikelnrBis, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFilterBis, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

	}

	void dialogQueryArtikelFromListe_Von(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel_Von = ArtikelFilterFactory.getInstance().createPanelFLRArtikel(getInternalFrame(), null,
				true);

		new DialogQuery(panelQueryFLRArtikel_Von);
	}

	void dialogQueryArtikelFromListe_Bis(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel_Bis = ArtikelFilterFactory.getInstance().createPanelFLRArtikel(getInternalFrame(), null,
				true);

		new DialogQuery(panelQueryFLRArtikel_Bis);
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRForecast) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ForecastDto fcDto = DelegateFactory.getInstance().getForecastDelegate()
						.forecastFindByPrimaryKey((Integer) key);
				wtfForecast.setText(fcDto.getCNr());
				forecastIId = fcDto.getIId();
			}

			else if (e.getSource() == panelQueryFLRArtikel_Von) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				wtfFilterVon.setText(artikelDto.getCNr());
			} else if (e.getSource() == panelQueryFLRArtikel_Bis) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				wtfFilterBis.setText(artikelDto.getCNr());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRForecast) {
				forecastIId = null;
				wtfForecast.setText(null);
			}
			else if (e.getSource() == panelQueryFLRArtikel_Von) {
				wtfFilterVon.setText(null);
			}
			else if (e.getSource() == panelQueryFLRArtikel_Bis) {
				wtfFilterBis.setText(null);
			}
		}
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
