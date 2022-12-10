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
package com.lp.client.forecast;

import java.util.EventObject;

import javax.swing.ImageIcon;

import com.lp.client.auftrag.TabbedPaneAuftragGrunddaten;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragartDto;
import com.lp.server.forecast.service.ForecastartDto;
import com.lp.server.forecast.service.ImportdefDto;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse kuemmert sich um den Kunden.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum 16.11.04</p>
 *
 * @author $Author: christian $
 * @version $Revision: 1.4 $
 * Date $Date: 2012/08/13 13:17:29 $
 */
public class InternalFrameForecast extends InternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneForecast tpForecast = null;
	private TabbedPaneForecastGrunddaten tpForecastgrunddaten = null;

	private ForecastartDto forecastartDto = new ForecastartDto();
	
	public ForecastartDto getForecastartDto() {
		return forecastartDto;
	}

	public void setForecastartDto(ForecastartDto forecastartDto) {
		this.forecastartDto = forecastartDto;
	}

	private ImportdefDto importdefDto = new ImportdefDto();
	
	public ImportdefDto getImportdefDto() {
		return importdefDto;
	}

	public void setImportdefDto(ImportdefDto importdefDto) {
		this.importdefDto = importdefDto;
	}

	private String rechtModulweit = null;

	public static int IDX_PANE_FORECAST = 0;
	public static int IDX_PANE_GRUNDDATEN = 1;

	public InternalFrameForecast(String sAddTitleI, String belegartCNr,
			String sRechtModulweitI) throws Throwable {

		super(sAddTitleI, belegartCNr, sRechtModulweitI);
		rechtModulweit = sRechtModulweitI;
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {

		tabbedPaneRoot.insertTab(
				LPMain.getInstance().getTextRespectUISPr("fc.forecast"), null,
				tpForecast,
				LPMain.getInstance().getTextRespectUISPr("fc.forecast"),
				IDX_PANE_FORECAST);
		
		
		tabbedPaneRoot.insertTab(
				LPMain.getInstance().getTextRespectUISPr("lp.grunddaten"), null,
				tpForecastgrunddaten,
				LPMain.getInstance().getTextRespectUISPr("lp.grunddaten"),
				IDX_PANE_GRUNDDATEN);

		// set defaults
		// Defaulttabbedpane setzen.
		refreshKundeTP();
		lPStateChanged(null);
		// /
		tpForecast.lPEventObjectChanged(null);
		tabbedPaneRoot.setSelectedComponent(tpForecast);

		// ich selbst moechte informiert werden.
		addItemChangedListener(this);
		// awt: listener bin auch ich.
		registerChangeListeners();

		// dem frame das icon setzen
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/document_chart.png"));
		setFrameIcon(iicon);
	}

	private void refreshKundeTP() throws Throwable {
		if (tpForecast == null) {
			tpForecast = new TabbedPaneForecast(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_FORECAST, tpForecast);
			initComponents();
		}
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {
		if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {

		}
	}

	public void lPStateChanged(EventObject e) throws Throwable {
		setRechtModulweit(rechtModulweit);
		int selectedCur = tabbedPaneRoot.getSelectedIndex(); // ( (JTabbedPane)
																// e.getSource()).getSelectedIndex();

		if (selectedCur == IDX_PANE_FORECAST) {
			refreshKundeTP();
			tpForecast.lPEventObjectChanged(null);
			setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN, LPMain
					.getInstance().getTextRespectUISPr("label.kunde"));
		} else if (selectedCur == IDX_PANE_GRUNDDATEN) {
			refreshTabbedPaneGrunddaten();

			// Info an Tabbedpane, bist selektiert worden.
			tpForecastgrunddaten.lPEventObjectChanged(null);
		}

	}

	
	private void refreshTabbedPaneGrunddaten() throws Throwable {
		if (tpForecastgrunddaten == null) {
			tpForecastgrunddaten = new TabbedPaneForecastGrunddaten(this);
			tabbedPaneRoot.setComponentAt(IDX_PANE_GRUNDDATEN,
					tpForecastgrunddaten);
			initComponents();
		}
	}
	
	/*
	 * Ableiten, da der Kunde keine TP hat.
	 * 
	 * @return PropertyVetoException
	 * 
	 * @throws Throwable
	 */
	/*
	 * public PropertyVetoException vetoableChangeLP() throws Throwable {
	 * 
	 * PanelBasis pb = (PanelBasis) tabbedPaneRoot.getSelectedComponent();
	 * return pb.vetoableChangeLP(); }
	 */

	/**
	 * gotoAuswahl
	 */
	protected void gotoAuswahl() {
		tabbedPaneRoot.setSelectedIndex(IDX_PANE_FORECAST);
	}

	public TabbedPaneForecast getTpForecast() {
		return tpForecast;
	}

}
