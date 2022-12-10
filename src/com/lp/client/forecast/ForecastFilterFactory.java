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

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.MaterialFac;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class ForecastFilterFactory {
	private static ForecastFilterFactory filterFactory = null;

	private ForecastFilterFactory() {
		// Singleton.
	}

	static public ForecastFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new ForecastFilterFactory();
		}

		return filterFactory;
	}

	public FilterKriterium[] createFKSchnellansichtForecast() throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium("status_c_nr", true, "('"
				+ LocaleFac.STATUS_ERLEDIGT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return filters;
	}

	public FilterKriterium[] createFKSchnellansichtForecastauftrag()
			throws Throwable {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium("status_c_nr", true, "('"
				+ LocaleFac.STATUS_ERLEDIGT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return filters;
	}

	public FilterKriterium[] createFKKundeMandantCNr() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("flrkunde.mandant_c_nr", true, "'"
				+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriterium[] createFKForecastauftraege(
			Integer fclieferadresseIId) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("flrfclieferadresse.i_id", true,
				fclieferadresseIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriterium[] createFKLieferadresse(Integer forecastIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("flrforecast.i_id", true,
				forecastIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriterium[] createFKForecastpositionen(
			Integer forecastauftragIId) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("flrforecastauftrag.i_id", true,
				forecastauftragIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriterium[] createFKLinienabruf(Integer forecastpositionIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("flrforecastposition.i_id", true,
				forecastpositionIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriteriumDirekt createFKDForecastnummer() throws Throwable {
		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("label.kennung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public FilterKriteriumDirekt createFKPositionenArtikelnummer() throws Throwable {
		return new FilterKriteriumDirekt("flrartikel.c_nr", "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("artikel.artikelnummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}
	
	
	public PanelQueryFLR createPanelFLRForecast(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton == true) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER };
		}

		
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("flrkunde.mandant_c_nr", true, "'"
				+ LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		
	
		
		PanelQueryFLR panelQueryFLRPg = new PanelQueryFLR(null,
				kriterien, QueryParameters.UC_ID_FORECAST,
				aWhichButtonIUse, internalFrameI, LPMain
						.getTextRespectUISPr("fc.forecast"));
		if (selectedId != null) {
			panelQueryFLRPg.setSelectedId(selectedId);
		}
		return panelQueryFLRPg;

	}

	
	public FilterKriteriumDirekt createFKPositionenBestellnummer() throws Throwable {
		return new FilterKriteriumDirekt("c_bestellnummer", "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("label.bestellnummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case
	}
	
	public FilterKriteriumDirekt createFKDTextSuchen() throws Throwable {

		return new FilterKriteriumDirekt("c_suche", "",
				FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("lp.textsuche"),
				FilterKriteriumDirekt.EXTENDED_SEARCH, false, true,
				Facade.MAX_UNBESCHRAENKT);

	}
	
	
	public QueryType[] buildQueryTypesAuswahl() {
		QueryType[] types = new QueryType[1];

		FilterKriterium f0 = new FilterKriterium("t_termin", true, "",
				FilterKriterium.OPERATOR_EQUAL, false);

		types[0] = new QueryType(
				LPMain.getTextRespectUISPr("label.termin"), f0,
				new String[] { FilterKriterium.OPERATOR_EQUAL,
						FilterKriterium.OPERATOR_GTE,
						FilterKriterium.OPERATOR_LTE }, true, // flrdate:
				// eingabeformat
				// 10.12.2004
				false, false);



		return types;
	}
	
	
	public PanelQueryFLR createPanelFLRForecastpositionen(
			InternalFrame internalFrameI, Integer forecastauftragIId,
			boolean bShowLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bShowLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER };
		}
		PanelQueryFLR panelQueryPositionen = new PanelQueryFLR(null,
				ForecastFilterFactory.getInstance().createFKForecastpositionen(
						forecastauftragIId),
				QueryParameters.UC_ID_FORECASTPOSITION, aWhichButtonIUse,
				internalFrameI,
				LPMain.getTextRespectUISPr("fc.forecastpositionen"));

		panelQueryPositionen
				.setFilterComboBox(
						DelegateFactory
								.getInstance()
								.getForecastDelegate()
								.getAllArtikelEinesForecastAuftrags(
										forecastauftragIId),
						new FilterKriterium("flrartikel.i_id", true, "'"
								+ LPMain.getTheClient().getMandant() + "'",
								FilterKriterium.OPERATOR_EQUAL, false), false);

		return panelQueryPositionen;

	}

}