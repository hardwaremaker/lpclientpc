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
package com.lp.client.angebotstkl;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class AngebotstklFilterFactory {
	private static AngebotstklFilterFactory filterFactory = null;

	private AngebotstklFilterFactory() {
		// Singleton.
	}

	static public AngebotstklFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new AngebotstklFilterFactory();
		}

		return filterFactory;
	}

	public QueryType[] createQTAgstkl() {

		QueryType[] types = new QueryType[2];

		FilterKriterium f1 = new FilterKriterium("agstkl.c_nr", true, "", FilterKriterium.OPERATOR_LIKE, false);

		types[0] = new QueryType(LPMain.getTextRespectUISPr("label.belegnummer"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL, FilterKriterium.OPERATOR_NOT_EQUAL }, true, true);

		FilterKriterium f2 = new FilterKriterium("agstkl.c_bez", true, "", FilterKriterium.OPERATOR_LIKE, true);

		types[1] = new QueryType(LPMain.getTextRespectUISPr("label.projekt"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL, FilterKriterium.OPERATOR_NOT_EQUAL }, false, false,
				true);

		return types;
	}

	public FilterKriteriumDirekt createFKDArtikelBezeichnung() throws Throwable {

		return new FilterKriteriumDirekt("aspr.c_bez", "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("lp.bezeichnung"), FilterKriteriumDirekt.EXTENDED_SEARCH, false, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDPositionlieferantArtikelnummer() throws Throwable {

		int iLaenge = ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER;

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER,
						ParameterFac.KATEGORIE_ARTIKEL, LPMain.getTheClient().getMandant());

		if (parameter.getCWertAsObject() != null) {
			iLaenge = ((Integer) parameter.getCWertAsObject()).intValue();
		}

		return new FilterKriteriumDirekt("flreinkaufsangebotposition.flrartikel.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("artikel.artikelnummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true, iLaenge);

	}

	public FilterKriteriumDirekt createFKDAgstklbelegnumer() throws Throwable {

		return new FilterKriteriumDirekt("c_nr", "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("label.belegnummer"), FilterKriteriumDirekt.PROZENT_LEADING, true, false,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDAgstklprojekt() throws Throwable {

		return new FilterKriteriumDirekt("agstkl.c_bez", "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("agstkl.projekt.projbezznrfilter"), FilterKriteriumDirekt.EXTENDED_SEARCH, false, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDEinkaufsangbotprojekt() throws Throwable {

		return new FilterKriteriumDirekt("c_projekt", "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("label.projekt"), FilterKriteriumDirekt.EXTENDED_SEARCH, false, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriterium[] createFKAgstklMandantCNr() throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("agstkl.mandant_c_nr", true, "'" + LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		return kriterien;
	}

	public FilterKriteriumDirekt createFKDAgstklAngebote() throws Throwable {

		return new FilterKriteriumDirekt("angebotspositionen.flrangebot.c_nr", "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("angb.angebotsnummer"), FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDEinakufsangebotbelegnumer() throws Throwable {

		return new FilterKriteriumDirekt("c_nr", "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("label.belegnummer"), FilterKriteriumDirekt.PROZENT_LEADING, true, false,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDKundeName() {
		FilterKriteriumDirekt fkDirekt1 = new FilterKriteriumDirekt(
				"agstkl." + AngebotstklFac.FLR_AGSTKL_FLRKUNDE + "." + KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
				"", FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("lp.kunde.modulname"),
				FilterKriteriumDirekt.AP_FIRMA_PROZENT_TRAILING, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case

		return fkDirekt1;
	}

	public FilterKriteriumDirekt createFKDEinkaufsangebotKundeName() {
		FilterKriteriumDirekt fkDirekt1 = new FilterKriteriumDirekt(
				AngebotstklFac.FLR_AGSTKL_FLRKUNDE + "." + KundeFac.FLR_PARTNER + "."
						+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
				"", FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("lp.kunde.modulname"),
				FilterKriteriumDirekt.AP_FIRMA_PROZENT_TRAILING, // Auswertung als 'XX%'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case

		return fkDirekt1;
	}

	public FilterKriterium[] createFKAgstklKey(Integer agstklIId) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("agstkl.i_id", true, agstklIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKAgstkl(Integer agstklIId) {

		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("agstkl_i_id", true, agstklIId + "", FilterKriterium.OPERATOR_EQUAL,
				false);
		kriterien[0] = krit1;
		return kriterien;

	}

	public FilterKriterium[] createFKEkgruppe(Integer ekgruppe_i_id) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("ekgruppe_i_id", true, ekgruppe_i_id + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKEinkaufsangebot(Integer agstklIId) {

		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("einkaufsangebot_i_id", true, agstklIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;

	}

	public FilterKriterium[] createFKPositionlieferant(Integer egaklieferantIId) {

		FilterKriterium[] kriterien = new FilterKriterium[1];

		FilterKriterium krit1 = new FilterKriterium("ekaglieferant_i_id", true, egaklieferantIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;

	}

	public FilterKriterium[] createFKWeblieferantEinkaufsangebot(Integer agstklIId) {

		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("flrEkweblieferant.einkaufsangebot_i_id", true, agstklIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;

	}

	public FilterKriterium createFKWebpartner() throws Throwable {

		FilterKriterium kriterium = new FilterKriterium(AngebotstklFac.FLR_WEBPARTNER_KRIT_MANDANT_MIT_LIEFERANT_NULL,
				true, LPMain.getTheClient().getMandant(), FilterKriterium.OPERATOR_EQUAL, false);

		return kriterium;
	}

	public FilterKriterium createFKWebpartnerLieferantNotNull() throws Throwable {

		FilterKriterium kriterium = new FilterKriterium(AngebotstklFac.FLR_WEBPARTNER_KRIT_MANDANT_OHNE_LIEFERANT_NULL,
				true, LPMain.getTheClient().getMandant(), FilterKriterium.OPERATOR_EQUAL, false);

		return kriterium;
	}

	/**
	 * Agstklauswahlliste mit Filter & Direktfilter.
	 * 
	 * @param internalFrameI    InternalFrame
	 * @param bShowFilterButton den New Button anzeigen
	 * @param bShowLeerenButton den Leeren Button anzeigen
	 * @return PanelQueryFLR die Auswahlliste
	 * @throws Throwable Ausnahme
	 */

	public PanelQueryFLR createPanelFLRAgstkl(InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton) throws Throwable {
		return createPanelFLRAgstkl(internalFrameI, bShowFilterButton, bShowLeerenButton, false);
	}

	public PanelQueryFLR createPanelFLRAgstkl(InternalFrame internalFrameI, boolean bShowFilterButton,
			boolean bShowLeerenButton, boolean bVorlagen) throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance().createButtonArray(bShowFilterButton,
				bShowLeerenButton);

		FilterKriterium[] kriterien = null;

		if (bVorlagen) {
			kriterien = new FilterKriterium[2];
			kriterien[0] = new FilterKriterium("agstkl.mandant_c_nr", true,
					"'" + LPMain.getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL, false);
			kriterien[1] = new FilterKriterium("agstkl.b_vorlage", true,
					"1", FilterKriterium.OPERATOR_EQUAL, false);
		} else {
			kriterien = new FilterKriterium[1];
			kriterien[0] = new FilterKriterium("agstkl.mandant_c_nr", true,
					"'" + LPMain.getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL, false);
		}

		PanelQueryFLR panelQueryFLRAgstkl = new PanelQueryFLR(createQTAgstkl(), kriterien, QueryParameters.UC_ID_AGSTKL,
				aWhichButtonIUse, internalFrameI, LPMain.getTextRespectUISPr("title.agstklauswahlliste"));

		panelQueryFLRAgstkl.befuellePanelFilterkriterienDirekt(createFKDAgstklbelegnumer(), createFKDKundeName());
		panelQueryFLRAgstkl.addDirektFilter(AngebotstklFilterFactory.getInstance().createFKDAgstklprojekt());

		panelQueryFLRAgstkl.addDirektFilter(AngebotstklFilterFactory.getInstance().createFKDAgstklAngebote());

		return panelQueryFLRAgstkl;
	}

	public PanelQueryFLR createPanelFLRWebpartner(InternalFrame internalFrame) throws Throwable {
		String[] aWhichButtonIUse = SystemFilterFactory.getInstance().createButtonArray(false, false);
		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = createFKWebpartnerLieferantNotNull();

		PanelQueryFLR panelQueryFLRWebpartner = new PanelQueryFLR(null, kriterien, QueryParameters.UC_ID_WEBFINDCHIPS,
				aWhichButtonIUse, internalFrame, LPMain.getTextRespectUISPr("title.webpartnerauswahlliste"));

		return panelQueryFLRWebpartner;
	}
}