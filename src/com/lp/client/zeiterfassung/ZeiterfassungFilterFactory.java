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
package com.lp.client.zeiterfassung;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

public class ZeiterfassungFilterFactory {
	private static ZeiterfassungFilterFactory filterFactory = null;

	private ZeiterfassungFilterFactory() {
		// Singleton.
	}

	static public ZeiterfassungFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new ZeiterfassungFilterFactory();
		}

		return filterFactory;
	}

	public PanelQueryFLR createPanelFLRMaschinen(InternalFrame internalFrameI, Integer selectedId) throws Throwable {
		return createPanelFLRMaschinen(internalFrameI,selectedId,false);
	}
	
	public PanelQueryFLR createPanelFLRMaschinen(InternalFrame internalFrameI, Integer selectedId,
			boolean nurManuellBedienbar) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };

		FilterKriterium[] kriterien = null;

		if (nurManuellBedienbar) {
			kriterien = new FilterKriterium[2];
			kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'" + LPMain.getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
			kriterien[1] = new FilterKriterium("b_manuelle_bedienung", true, "1", FilterKriterium.OPERATOR_EQUAL,
					false);
		} else {
			kriterien = new FilterKriterium[1];
			kriterien[0] = new FilterKriterium("mandant_c_nr", true, "'" + LPMain.getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_EQUAL, false);
		}

		PanelQueryFLR panelQueryFLRMaschine = new PanelQueryFLR(null, kriterien, QueryParameters.UC_ID_MASCHINE,
				aWhichButtonIUse, internalFrameI, LPMain.getTextRespectUISPr("auft.title.panel.auswahl"),
				createFKVMaschine(),null);

		panelQueryFLRMaschine.addDirektFilter(SystemFilterFactory.getInstance().createFKDBezeichnung());

		if (selectedId != null) {
			panelQueryFLRMaschine.setSelectedId(selectedId);
		}

		return panelQueryFLRMaschine;
	}

	public PanelQueryFLR createPanelFLRAlternativmaschinen(InternalFrame internalFrameI, List<Integer> al,
			Integer selectedId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };

		String in = "";

		Iterator it = al.iterator();
		while (it.hasNext()) {

			in += "'" + it.next() + "'";

			if (it.hasNext()) {
				in += ",";
			}
		}

		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("i_id", true, "(" + in + ")", FilterKriterium.OPERATOR_IN, false);

		PanelQueryFLR panelQueryFLRMaschine = new PanelQueryFLR(null, kriterien, QueryParameters.UC_ID_MASCHINE,
				aWhichButtonIUse, internalFrameI, LPMain.getTextRespectUISPr("auft.title.panel.auswahl"));

		panelQueryFLRMaschine.befuellePanelFilterkriterienDirektUndVersteckte(
				SystemFilterFactory.getInstance().createFKDBezeichnung(), null, createFKVMaschine());

		if (selectedId != null) {
			panelQueryFLRMaschine.setSelectedId(selectedId);
		}

		return panelQueryFLRMaschine;
	}

	public PanelQueryFLR createPanelFLRBereitschaftsart(InternalFrame internalFrameI, Integer selectedId,
			boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton == true) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_FILTER };
		}

		PanelQueryFLR panelQueryFLR = new PanelQueryFLR(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
				QueryParameters.UC_ID_BEREITSCHAFTART, aWhichButtonIUse, internalFrameI,
				LPMain.getTextRespectUISPr("pers.bereitschaftsart"));

		panelQueryFLR.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(),
				null);

		if (selectedId != null) {
			panelQueryFLR.setSelectedId(selectedId);
		}

		return panelQueryFLR;
	}

	public FilterKriterium createFKVMaschine() {
		FilterKriterium fkVersteckt = new FilterKriterium(ZeiterfassungFac.FLR_MASCHINE_B_VERSTECKT, true, "(1)", // wenn
				FilterKriterium.OPERATOR_NOT_IN, false);

		return fkVersteckt;
	}

	public PanelQueryFLR createPanelFLRZeitdatenGutSchlecht(InternalFrame internalFrameI, java.sql.Date dDatum,
			Integer selectedId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		FilterKriterium[] kriterien = new FilterKriterium[2];
		kriterien[0] = new FilterKriterium("t_zeit", true, "'" + Helper.formatDateWithSlashes(dDatum) + "'",
				FilterKriterium.OPERATOR_GTE, false);

		java.sql.Date dMorgen = Helper.addiereTageZuDatum(new java.sql.Date(dDatum.getTime()), 1);

		kriterien[1] = new FilterKriterium("t_zeit", true, "'" + Helper.formatDateWithSlashes(dMorgen) + "'",
				FilterKriterium.OPERATOR_LT, false);

		PanelQueryFLR panelQueryFLRZeitdaten = new PanelQueryFLR(null, kriterien,
				QueryParameters.UC_ID_ZEITDATENGUTSCHLECHT, aWhichButtonIUse, internalFrameI,
				LPMain.getTextRespectUISPr("auft.title.panel.auswahl"));

		panelQueryFLRZeitdaten.befuellePanelFilterkriterienDirekt(createFKDZeitdatenGutSchlechtPerson(), null);

		if (selectedId != null) {
			panelQueryFLRZeitdaten.setSelectedId(selectedId);
		}

		return panelQueryFLRZeitdaten;
	}

	public PanelQueryFLR createPanelFLRMaschinenZeitdatenGutSchlecht(InternalFrame internalFrameI, java.sql.Date dDatum,
			Integer selectedId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		FilterKriterium[] kriterien = new FilterKriterium[2];
		kriterien[0] = new FilterKriterium("t_von", true, "'" + Helper.formatDateWithSlashes(dDatum) + "'",
				FilterKriterium.OPERATOR_GTE, false);

		java.sql.Date dMorgen = Helper.addiereTageZuDatum(new java.sql.Date(dDatum.getTime()), 1);

		kriterien[1] = new FilterKriterium("t_von", true, "'" + Helper.formatDateWithSlashes(dMorgen) + "'",
				FilterKriterium.OPERATOR_LT, false);

		PanelQueryFLR panelQueryFLRZeitdaten = new PanelQueryFLR(null, kriterien,
				QueryParameters.UC_ID_MASCHINENZEITDATENGUTSCHLECHT, aWhichButtonIUse, internalFrameI,
				LPMain.getTextRespectUISPr("auft.title.panel.auswahl"));

		if (selectedId != null) {
			panelQueryFLRZeitdaten.setSelectedId(selectedId);
		}

		return panelQueryFLRZeitdaten;
	}

	public QueryType[] createQTTaetigkeit() {

		QueryType[] types = new QueryType[2];

		FilterKriterium f1 = new FilterKriterium("c_nr", true, "", FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getTextRespectUISPr("lp.taetigkeit"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		FilterKriterium f2 = new FilterKriterium(ZeiterfassungFac.FLR_TAETIGKEIT_TAETIGKEITSPRSET + ".c_bez", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[1] = new QueryType(LPMain.getTextRespectUISPr("lp.bezeichnung"), f2,
				new String[] { FilterKriterium.OPERATOR_EQUAL }, true, true);

		return types;
	}

	public FilterKriterium[] createFKAlleNichtTagbuchbarentaetigkeiten() {
		FilterKriterium[] kriterien = new FilterKriterium[2];

		kriterien[0] = new FilterKriterium(ZeiterfassungFac.FLR_TAETIGKEIT_B_TAGBUCHBAR, true,
				Helper.boolean2Short(false).intValue() + "", FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[1] = new FilterKriterium(ZeiterfassungFac.FLR_TAETIGKEIT_TAETIGKEITART_C_NR, true,
				"'" + ZeiterfassungFac.TAETIGKEITART_SONDERTAETIGKEIT + "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKAlleTagbuchbarentaetigkeiten() throws Throwable {
		FilterKriterium[] kriterien = null;

		boolean bSonderzeitenCUD = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_SONDERZEITEN_CUD);

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_PERSONAL,
				ParameterFac.PARAMETER_URLAUBSANTRAG);
		boolean bUrlaubsantrag = (java.lang.Boolean) parameter.getCWertAsObject();

		if (bSonderzeitenCUD == true) {

			if (bUrlaubsantrag == true) {
				kriterien = new FilterKriterium[2];

				kriterien[0] = new FilterKriterium("c_nr", true,
						"('" + ZeiterfassungFac.TAETIGKEIT_ENDE + "','" + ZeiterfassungFac.TAETIGKEIT_GEHT + "','"
								+ ZeiterfassungFac.TAETIGKEIT_KOMMT + "','" + ZeiterfassungFac.TAETIGKEIT_UNTER + "')",
						FilterKriterium.OPERATOR_NOT_IN, false);
				kriterien[1] = new FilterKriterium("b_versteckt", true, "0", FilterKriterium.OPERATOR_IS, false);

			} else {
				kriterien = new FilterKriterium[3];

				kriterien[0] = new FilterKriterium("c_nr", true,
						"('" + ZeiterfassungFac.TAETIGKEIT_ENDE + "','" + ZeiterfassungFac.TAETIGKEIT_GEHT + "','"
								+ ZeiterfassungFac.TAETIGKEIT_KOMMT + "','" + ZeiterfassungFac.TAETIGKEIT_UNTER + "')",
						FilterKriterium.OPERATOR_NOT_IN, false);
				kriterien[1] = new FilterKriterium("b_versteckt", true, "0", FilterKriterium.OPERATOR_IS, false);
				kriterien[2] = new FilterKriterium("c_nr", true,
						"('" + ZeiterfassungFac.TAETIGKEIT_URLAUBSANTRAG + "','"
								+ ZeiterfassungFac.TAETIGKEIT_KRANKANTRAG + "','" + ZeiterfassungFac.TAETIGKEIT_ZAANTRAG
								+ "')",
						FilterKriterium.OPERATOR_NOT_IN, false);

			}

		} else {

			if (bUrlaubsantrag == true) {
				kriterien = new FilterKriterium[2];
				kriterien[0] = new FilterKriterium("c_nr", true,
						"('" + ZeiterfassungFac.TAETIGKEIT_ENDE + "','" + ZeiterfassungFac.TAETIGKEIT_GEHT + "','"
								+ ZeiterfassungFac.TAETIGKEIT_KOMMT + "','" + ZeiterfassungFac.TAETIGKEIT_URLAUB + "','"
								+ ZeiterfassungFac.TAETIGKEIT_UNTER + "')",
						FilterKriterium.OPERATOR_NOT_IN, false);
				kriterien[1] = new FilterKriterium(ZeiterfassungFac.FLR_TAETIGKEIT_B_DARF_SELBER_BUCHEN, true, "1",
						FilterKriterium.OPERATOR_EQUAL, false);

			} else {
				kriterien = new FilterKriterium[4];
				kriterien[0] = new FilterKriterium("c_nr", true,
						"('" + ZeiterfassungFac.TAETIGKEIT_ENDE + "','" + ZeiterfassungFac.TAETIGKEIT_GEHT + "','"
								+ ZeiterfassungFac.TAETIGKEIT_KOMMT + "','" + ZeiterfassungFac.TAETIGKEIT_UNTER + "')",
						FilterKriterium.OPERATOR_NOT_IN, false);
				kriterien[1] = new FilterKriterium("b_versteckt", true, "0", FilterKriterium.OPERATOR_IS, false);

				kriterien[2] = new FilterKriterium("c_nr", true,
						"('" + ZeiterfassungFac.TAETIGKEIT_URLAUBSANTRAG + "','"
								+ ZeiterfassungFac.TAETIGKEIT_KRANKANTRAG + "','" + ZeiterfassungFac.TAETIGKEIT_ZAANTRAG
								+ "')",
						FilterKriterium.OPERATOR_NOT_IN, false);
				kriterien[3] = new FilterKriterium(ZeiterfassungFac.FLR_TAETIGKEIT_B_DARF_SELBER_BUCHEN, true, "1",
						FilterKriterium.OPERATOR_EQUAL, false);
			}

		}

		return kriterien;
	}

	public FilterKriteriumDirekt createFKDZeitdatenArtikelnummer() throws Throwable {

		return new FilterKriteriumDirekt("zeitdaten." + ZeiterfassungFac.FLR_ZEITDATEN_FLRARTIKEL + ".c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("label.ident"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true, Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriterium[] createFKPersonalKey(Integer iId) throws Throwable {
		// Handartikel nicht anzeigen
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("i_id", true, iId + "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriteriumDirekt createFKDZeitdatenArtikelzusatzbezeichnung() throws Throwable {

		return new FilterKriteriumDirekt("aspr.c_zbez", "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("lp.zusatzbezeichnung"), FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createFKDTelefonzeitenPartner() throws Throwable {
		return new FilterKriteriumDirekt("flrpartner.c_name1nachnamefirmazeile1", "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("part.firma_nachname"), FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
																											// als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public FilterKriterium[] createFKReisespesen(Integer reiseIId) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("reise_i_id", true,
				reiseIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createcreateFKReisespesenWennNULL() {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("reise_i_id", true,
				"-3678924", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}
	
	public FilterKriteriumDirekt createFKDZeitdatenGutSchlechtPerson() throws Throwable {
		return new FilterKriteriumDirekt("flrpersonal.flrpartner.c_name1nachnamefirmazeile1", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("lp.nachname"),
				FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public FilterKriteriumDirekt createFKDTelefonzeitenKommentar() throws Throwable {
		return new FilterKriteriumDirekt(ArtikelFac.FLR_ARTIKELLISTE_C_VOLLTEXT, "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getTextRespectUISPr("lp.kommentar"), FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public FilterKriterium[] createFKAlleSondertaetigkeitenOhneKommtGehtEndeStop() {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = new FilterKriterium("c_nr", true, "('" + ZeiterfassungFac.TAETIGKEIT_GEHT + "','"
				+ ZeiterfassungFac.TAETIGKEIT_ENDE + "','" + ZeiterfassungFac.TAETIGKEIT_KOMMT + "','" + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return kriterien;
	}
	public FilterKriterium[] createFKLeistungsfaktor(Integer maschineIId) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("maschine_i_id", true,
				maschineIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKMaschinenkosten(Integer maschineIId) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium(ZeiterfassungFac.FLR_MASCHINENKOSTEN_FLRMASCHINE + ".i_id", true,
				maschineIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKMaschinemaschinezm(Integer maschineIId) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("maschine_i_id", true, maschineIId + "", FilterKriterium.OPERATOR_EQUAL,
				false);

		return kriterien;
	}

	public FilterKriterium[] createFKMaschinenzeitdaten(Integer maschineIId) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium(ZeiterfassungFac.FLR_MASCHINENZEITDATEN_FLRMASCHINE + ".i_id", true,
				maschineIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriteriumDirekt createFKDMaschinenzeitdatenPersonalname() throws Throwable {

		return new FilterKriteriumDirekt(
				"flrpersonal_gestartet." + PersonalFac.FLR_PERSONAL_FLRPARTNER + "."
						+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
				"", FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("lp.nachname"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true, Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriteriumDirekt createMaschinenzeitdatenFKDLosnummer() throws Throwable {
		return new FilterKriteriumDirekt("flrlossollarbeitsplan.flrlos." + FertigungFac.FLR_LOS_C_NR, "",
				FilterKriterium.OPERATOR_LIKE, "Los Nr.", FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung
																									// als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT);
	}

	public FilterKriterium[] createFKDiaetentagessatz(Integer diaetenIId) throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium(ZeiterfassungFac.FLR_DIAETENTAGESSATZ_FLRDIAETEN + ".i_id", true,
				diaetenIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKZusaetzlicheSondertaetigkeiten() {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = new FilterKriterium(ZeiterfassungFac.FLR_TAETIGKEIT_TAETIGKEITART_C_NR, true,
				"'" + ZeiterfassungFac.TAETIGKEITART_SONDERTAETIGKEIT + "'", FilterKriterium.OPERATOR_EQUAL, false);

		/*
		 * kriterien[1] = new FilterKriterium("c_nr", true, "('" +
		 * ZeiterfassungFac.TAETIGKEIT_KOMMT + "','" + ZeiterfassungFac.TAETIGKEIT_GEHT
		 * + "','" + ZeiterfassungFac.TAETIGKEIT_ENDE + "','" +
		 * ZeiterfassungFac.TAETIGKEIT_UNTER + "')", FilterKriterium.OPERATOR_NOT_IN,
		 * false);
		 */
		return kriterien;
	}

	public FilterKriterium[] createFKArbeitsgaenge() {
		FilterKriterium[] kriterien = new FilterKriterium[1];

		kriterien[0] = new FilterKriterium("taetigkeitart_c_nr", true,
				"'" + ZeiterfassungFac.TAETIGKEITART_SONDERTAETIGKEIT + "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

		return kriterien;
	}

	public FilterKriterium[] createFKZeitdatenZuPersonalUndDatum(Integer personalIId, java.sql.Date dDatum) {
		FilterKriterium[] kriterien = new FilterKriterium[3];
		FilterKriterium krit1 = new FilterKriterium("zeitdaten." + ZeiterfassungFac.FLR_ZEITDATEN_FLRPERSONAL + ".i_id",
				true, personalIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		FilterKriterium krit2 = new FilterKriterium("zeitdaten." + ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT, true,
				"'" + Helper.formatDateWithSlashes(dDatum) + "'", FilterKriterium.OPERATOR_GTE, false);

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(dDatum.getTime());
		c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);

		FilterKriterium krit3 = new FilterKriterium("zeitdaten." + ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT, true,
				"'" + Helper.formatDateWithSlashes(new java.sql.Date(c.getTimeInMillis())) + "'",
				FilterKriterium.OPERATOR_LT, false);

		kriterien[0] = krit1;
		kriterien[1] = krit2;
		kriterien[2] = krit3;
		return kriterien;
	}

	public FilterKriterium[] createFKSonderzeiten(Integer personalIId) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(ZeiterfassungFac.FLR_SONDERZEITEN_PERSONAL_I_ID, true,
				personalIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKZeitabschluss(Integer personalIId) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("personal_i_id", true, personalIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKReisezeiten(Integer personalIId) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID, true, personalIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public FilterKriterium[] createFKTelefonzeiten(Integer personalIId) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(ZeiterfassungFac.FLR_TELEFONZEITEN_PERSONAL_I_ID, true,
				personalIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	public QueryType[] createQTSonderzeiten() {

		QueryType[] types = new QueryType[1];

		FilterKriterium f1 = new FilterKriterium(ZeiterfassungFac.FLR_SONDERZEITEN_FLRTAETIGKEIT + ".c_nr", true, "",
				FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getTextRespectUISPr("lp.taetigkeit"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL, FilterKriterium.OPERATOR_NOT_EQUAL }, true, true);

		return types;
	}

	public FilterKriterium[] createFKZeitdatenpruefenZuPersonal(Integer personalIId) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("zeitdaten." + ZeiterfassungFac.FLR_ZEITDATEN_FLRPERSONAL + ".i_id",
				true, personalIId + "", FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[0] = krit1;
		return kriterien;
	}
}
