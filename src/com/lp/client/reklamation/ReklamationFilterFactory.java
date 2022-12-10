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
package com.lp.client.reklamation;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse ist ein Singleton und kuemmert sich um alle Filter im
 * Auftrag.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2007</p>
 * 
 * <p>Erstellung: 26. 04. 2005</p>
 * 
 * <p>@author Uli Walch</p>
 * 
 * @version $Revision: 1.10 $ Date $Date: 2012/01/19 10:34:06 $
 */
public class ReklamationFilterFactory {
	private static ReklamationFilterFactory filterFactory = null;

	private ReklamationFilterFactory() {
		// Singleton.
	}

	/**
	 * Hole das Singelton AuftragFilterFactory.
	 * 
	 * @return LPMain
	 */
	static public ReklamationFilterFactory getInstance() {
		if (filterFactory == null) {
			filterFactory = new ReklamationFilterFactory();
		}

		return filterFactory;
	}

	/**
	 * Direktes Filter Kriterium Auftragnummer fuer das PanelAuftragAuswahl.
	 * 
	 * @return FilterKriteriumDirekt
	 * @throws Throwable
	 */
	public FilterKriteriumDirekt createFKDReklamationsnummer() throws Throwable {
		return new FilterKriteriumDirekt("c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("rekla.nummer"),
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public FilterKriteriumDirekt createFKDReklamationArtikelnummer()
			throws Throwable {
		return new FilterKriteriumDirekt("flrartikel.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("artikel.artikelnummer"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	
	public FilterKriteriumDirekt createFKDReklamationKundeLieferant()
			throws Throwable {
		return new FilterKriteriumDirekt("KUNDE_LIEFERANT", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("rekla.filter.kundelieferant"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	
	public FilterKriteriumDirekt createFKDReklamationKdReklaNr()
			throws Throwable {
		return new FilterKriteriumDirekt("c_kdreklanr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("rekla.filter.kdreklanrkdlsnr"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	protected QueryType[] createQTReklamationauswahl() {

		QueryType[] types = new QueryType[1];

		FilterKriterium f1 = new FilterKriterium("flrfehlerangabe.c_bez", true,
				"", FilterKriterium.OPERATOR_LIKE, true);

		types[0] = new QueryType(LPMain.getInstance().getTextRespectUISPr(
				"rekla.fehlerangabe"), f1,
				new String[] { FilterKriterium.OPERATOR_EQUAL, FilterKriterium.OPERATOR_NOT_EQUAL }, true, true);
		return types;
	}

	public FilterKriterium[] createFKReklamationKey(Integer reklamationIId)
			throws Throwable {
		
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("i_id", true, reklamationIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		return kriterien;
	}
	
	public FilterKriteriumDirekt createFKDProjekt() throws Throwable {
		return new FilterKriteriumDirekt("c_projekt", "",
				FilterKriterium.OPERATOR_LIKE, 
				LPMain.getTextRespectUISPr("label.projekt"),
				FilterKriteriumDirekt.EXTENDED_SEARCH, 
				false, // wrapWithSingleQuotes
				true, // ignore case
				Facade.MAX_UNBESCHRAENKT);
	}
	
	public FilterKriteriumDirekt createFKDGrund() throws Throwable {
		return new FilterKriteriumDirekt("c_grund", "",
				FilterKriterium.OPERATOR_LIKE, 
				LPMain.getTextRespectUISPr("rekla.grund"),
				FilterKriteriumDirekt.EXTENDED_SEARCH, 
				false, // wrapWithSingleQuotes
				true, // ignore case
				Facade.MAX_UNBESCHRAENKT);
	}
	
	public FilterKriteriumDirekt createFKDGrundKommentar() throws Throwable {
		return new FilterKriteriumDirekt("x_grund_lang", "",
				FilterKriterium.OPERATOR_LIKE, 
				LPMain.getTextRespectUISPr("rekla.grundkommentar"),
				FilterKriteriumDirekt.EXTENDED_SEARCH, 
				false, // wrapWithSingleQuotes
				true, // ignore case
				Facade.MAX_UNBESCHRAENKT);
	}
	
	public FilterKriteriumDirekt createFKDMaschinengruppe() throws Throwable {
		return new FilterKriteriumDirekt(
				"flrmaschine.flrmaschinengruppe.c_bez", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("pers.maschinengruppe"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, true, Facade.MAX_UNBESCHRAENKT); // ignore case
	}

	public PanelQueryFLR createPanelFLRMassnahme(InternalFrame internalFrameI,
			Integer selectedId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		PanelQueryFLR panelQueryFLRMassnahme = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_MASSNAHME, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"title.massnahmeauswahlliste"));
		panelQueryFLRMassnahme.befuellePanelFilterkriterienDirekt(
				ReklamationFilterFactory.getInstance()
				.createFKDBezeichnungMitAlias("massnahme"), null);
		if (selectedId != null) {
			panelQueryFLRMassnahme.setSelectedId(selectedId);
		}
		return panelQueryFLRMassnahme;

	}

	public PanelQueryFLR createPanelFLRReklamation(InternalFrame internalFrameI,
			Integer selectedId) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		PanelQueryFLR panelQueryReklamation = new PanelQueryFLR(ReklamationFilterFactory
				.getInstance().createQTReklamationauswahl(), SystemFilterFactory.getInstance().createFKMandantCNr(),
				QueryParameters.UC_ID_REKLAMATION, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"auft.title.panel.auswahl"));
		panelQueryReklamation.befuellePanelFilterkriterienDirekt(
				ReklamationFilterFactory.getInstance()
						.createFKDReklamationsnummer(),
				ReklamationFilterFactory.getInstance()
						.createFKDReklamationArtikelnummer());
		panelQueryReklamation.addDirektFilter(ReklamationFilterFactory
				.getInstance().createFKDReklamationKdReklaNr());
		panelQueryReklamation.addDirektFilter(ReklamationFilterFactory
				.getInstance().createFKDMaschinengruppe());
		
	
		
		if (selectedId != null) {
			panelQueryReklamation.setSelectedId(selectedId);
		}
		return panelQueryReklamation;

	}

	public FilterKriterium[] createFKReklamationSchnellansicht() {
		FilterKriterium[] filters = new FilterKriterium[1];
		filters[0] = new FilterKriterium(
				ReklamationFac.FLR_REKLAMATION_STATUS_C_NR, true, "('"
						+ LocaleFac.STATUS_ERLEDIGT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		return filters;
	}

	public FilterKriteriumDirekt createFKDBezeichnungMitAlias(String alias)
			throws Throwable {

		return new FilterKriteriumDirekt(alias + ".c_bez", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("lp.bezeichnung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
				Facade.MAX_UNBESCHRAENKT);

	}

	public FilterKriterium[] createFKReklamation(Integer reklamationIId)
			throws Throwable {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("reklamation_i_id", true,
				reklamationIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		return kriterien;
	}

	public FilterKriteriumDirekt createFKDBehandlungKennung() {
		return new FilterKriteriumDirekt("behandlung.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.kennung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	
	public FilterKriteriumDirekt createFKDSchwereKennung() {
		return new FilterKriteriumDirekt("schwere.c_nr", "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
						.getTextRespectUISPr("label.kennung"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
				true, true, Facade.MAX_UNBESCHRAENKT); // wrapWithSingleQuotes
	}

	
	public PanelQueryFLR createPanelFLRBehandlung(InternalFrame internalFrameI,
			Integer selectedId, boolean bMitLeerenButton) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		PanelQueryFLR panelQueryFLRBeurteilung = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_BEHANDLUNG, aWhichButtonIUse,
				internalFrameI, LPMain.getInstance().getTextRespectUISPr(
						"rekla.behandlung"));
		panelQueryFLRBeurteilung.befuellePanelFilterkriterienDirekt(
				createFKDBehandlungKennung(), null);
		if (selectedId != null) {
			panelQueryFLRBeurteilung.setSelectedId(selectedId);
		}
		return panelQueryFLRBeurteilung;

	}

}
