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
package com.lp.client.artikel;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

/**
 * <p>
 * &UUml;berschrift:
 * </p>
 * <p>
 * Beschreibung:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Organisation:
 * </p>
 * 
 * @author Christian Kollmann
 * @version $Revision: 1.18 $
 */
public class TabbedPaneArtikelgrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryArtikelklassen = null;
	private PanelBasis panelSplitArtikelklassen = null;
	private PanelBasis panelBottomArtikelklassen = null;

	private PanelQuery panelQueryArtikelgruppen = null;
	private PanelBasis panelSplitArtikelgruppen = null;
	private PanelBasis panelBottomArtikelgruppen = null;

	private PanelQuery panelQueryLager = null;
	private PanelBasis panelSplitLager = null;
	private PanelBasis panelBottomLager = null;

	private PanelQuery panelQueryHersteller = null;
	private PanelBasis panelSplitHersteller = null;
	public PanelBasis getPanelSplitHersteller() {
		return panelSplitHersteller;
	}

	private PanelBasis panelBottomHersteller = null;

	private PanelQuery panelQueryArtikelkommentarart = null;
	private PanelBasis panelSplitArtikelkommentarart = null;
	private PanelBasis panelBottomArtikelkommentarart = null;

	private PanelQuery panelQueryFarbcode = null;
	private PanelBasis panelSplitFarbcode = null;
	private PanelBasis panelBottomFarbcode = null;

	private PanelQuery panelQuerySperren = null;
	private PanelBasis panelSplitSperren = null;
	private PanelBasis panelBottomSperren = null;

	private PanelQuery panelQueryLagerplatz = null;
	private PanelBasis panelSplitLagerplatz = null;
	private PanelBasis panelBottomLagerplatz = null;

	private PanelQuery panelQueryVerleih = null;
	private PanelBasis panelSplitVerleih = null;
	private PanelBasis panelBottomVerleih = null;

	private PanelQuery panelQueryVorschlagstext = null;
	private PanelBasis panelSplitVorschlagstext = null;
	private PanelBasis panelBottomVorschlagstext = null;

	private PanelQuery panelQueryWebshop = null;
	private PanelBasis panelSplitWebshop = null;
	private PanelBasis panelBottomWebshop = null;

	private PanelQuery panelQueryReach = null;
	private PanelBasis panelSplitReach = null;
	private PanelBasis panelBottomReach = null;
	
	private PanelQuery panelQueryLaseroberflaeche = null;
	private PanelBasis panelSplitLaseroberflaeche = null;
	private PanelBasis panelBottomLaseroberflaeche = null;

	private PanelQuery panelQueryRohs = null;
	private PanelBasis panelSplitRohs = null;
	private PanelBasis panelBottomRohs = null;

	private PanelQuery panelQueryAutomotive = null;
	private PanelBasis panelSplitAutomotive = null;
	private PanelBasis panelBottomAutomotive = null;

	private PanelQuery panelQueryMedical = null;
	private PanelBasis panelSplitMedical = null;
	private PanelBasis panelBottomMedical = null;

	private PanelQuery panelQueryVorzug = null;
	private PanelBasis panelSplitVorzug = null;
	private PanelBasis panelBottomVorzug = null;

	private PanelQuery panelQueryAlergen = null;
	private PanelBasis panelSplitAlergen = null;
	private PanelBasis panelBottomAlergen = null;

	private PanelQuery panelQueryGebinde = null;
	private PanelBasis panelSplitGebinde = null;
	private PanelBasis panelBottomGebinde = null;

	private PanelQuery panelQueryVerpackungsmittel = null;
	private PanelBasis panelSplitVerpackungsmittel = null;
	private PanelBasis panelBottomVerpackungsmittel = null;

	private PanelQuery panelQueryDateiverweis = null;
	private PanelBasis panelSplitDateiverweis = null;
	private PanelBasis panelBottomDateiverweis = null;

	private PanelQuery panelQueryWaffenkaliber = null;
	private PanelBasis panelSplitWaffenkaliber = null;
	private PanelBasis panelBottomWaffenkaliber = null;

	private PanelQuery panelQueryWaffenausfuehrung = null;
	private PanelBasis panelSplitWaffenausfuehrung = null;
	private PanelBasis panelBottomWaffenausfuehrung = null;

	private PanelQuery panelQueryWaffentyp = null;
	private PanelBasis panelSplitWaffentyp = null;
	private PanelBasis panelBottomWaffentyp = null;

	private PanelQuery panelQueryWaffentypFein = null;
	private PanelBasis panelSplitWaffentypFein = null;
	private PanelBasis panelBottomWaffentypFein = null;

	private PanelQuery panelQueryWaffenkategorie = null;
	private PanelBasis panelSplitWaffenkategorie = null;
	private PanelBasis panelBottomWaffenkategorie = null;

	private PanelQuery panelQueryWaffenzusatz = null;
	private PanelBasis panelSplitWaffenzusatz = null;
	private PanelBasis panelBottomWaffenzusatz = null;

	private final String MENUE_ACTION_HERSTELLER_ZUSAMMENFUEHREN = "ACTION_HERSTELLER_ZUSAMMENFUEHREN";

	private int IDX_PANEL_ARTIKELKLASSEN = -1;
	private int IDX_PANEL_ARTIKELGRUPPEN = -1;
	private int IDX_PANEL_LAGER = -1;
	private int IDX_PANEL_HERSTELLER = -1;
	private int IDX_PANEL_ARTIKELKOMMENTARART = -1;
	private int IDX_PANEL_FARBCODE = -1;
	private int IDX_PANEL_SPERREN = -1;
	private int IDX_PANEL_LAGERPLATZ = -1;
	private int IDX_PANEL_VERLEIH = -1;
	private int IDX_PANEL_VORSCHLAGSTEXT = -1;
	private int IDX_PANEL_WEBSHOP = -1;
	private int IDX_PANEL_REACH = -1;
	private int IDX_PANEL_ROHS = -1;
	private int IDX_PANEL_AUTOMOTIVE = -1;
	private int IDX_PANEL_MEDICAL = -1;
	private int IDX_PANEL_VORZUG = -1;
	private int IDX_PANEL_ALERGEN = -1;
	private int IDX_PANEL_GEBINDE = -1;
	private int IDX_PANEL_VERPACKUNGSMITTEL = -1;
	private int IDX_PANEL_DATEIVERWEIS = -1;
	private int IDX_PANEL_WAFFENKALIBER = -1;
	private int IDX_PANEL_WAFFENAUSFUEHRUNG = -1;
	private int IDX_PANEL_WAFFENTYP = -1;
	private int IDX_PANEL_WAFFENTYPFEIN = -1;
	private int IDX_PANEL_WAFFENKATEGORIE = -1;
	private int IDX_PANEL_WAFFENZUSATZ = -1;
	private int IDX_PANEL_LASEROBERFLAECHE = -1;

	private WrapperMenuBar wrapperManuBar = null;

	public TabbedPaneArtikelgrunddaten(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("lp.grunddaten"));

		jbInit();
		initComponents();
	}

	public InternalFrameArtikel getInternalFrameArtikel() {
		return (InternalFrameArtikel) getInternalFrame();
	}

	private void createArtikelklassen() throws Throwable {
		if (panelSplitArtikelklassen == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER };

			panelQueryArtikelklassen = new PanelQuery(ArtikelFilterFactory.getInstance().createQTArtikelklasse(),
					ArtikelFilterFactory.getInstance().createFKArtklaMandantCNr(), QueryParameters.UC_ID_ARTIKELKLASSE,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.title.tab.artikelklassen"), true);

			panelQueryArtikelklassen.befuellePanelFilterkriterienDirekt(
					ArtikelFilterFactory.getInstance().createFKDArtikelklasseKennung(),
					SystemFilterFactory.getInstance()
							.createFKDSprTabelleBezeichnung(ArtikelFac.FLR_ARTIKELKLASSE_ARTIKELKLASSESPRSET));

			panelBottomArtikelklassen = new PanelArtikelklassen(getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.title.tab.artikelklassen"), null);
			panelSplitArtikelklassen = new PanelSplit(getInternalFrame(), panelBottomArtikelklassen,
					panelQueryArtikelklassen, 380);

			setComponentAt(IDX_PANEL_ARTIKELKLASSEN, panelSplitArtikelklassen);
		}
	}

	private void createArtikelkommentarart() throws Throwable {
		if (panelSplitArtikelkommentarart == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER };

			panelQueryArtikelkommentarart = new PanelQuery(
					ArtikelFilterFactory.getInstance().createQTArtikelkommentarart(), null,
					QueryParameters.UC_ID_ARTIKELKOMMENTARART, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kommentarart"), true);

			panelQueryArtikelkommentarart.befuellePanelFilterkriterienDirekt(
					ArtikelFilterFactory.getInstance().createFKDArtikelkommentarartKennung(),
					SystemFilterFactory.getInstance().createFKDSprTabelleBezeichnung(
							ArtikelkommentarFac.FLR_ARTIKELKOMMENTARART_ARTIKELKOMMENTARARTSPRSET));

			panelBottomArtikelkommentarart = new PanelArtikelkommentarart(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kommentarart"), null);
			panelSplitArtikelkommentarart = new PanelSplit(getInternalFrame(), panelBottomArtikelkommentarart,
					panelQueryArtikelkommentarart, 350);

			setComponentAt(IDX_PANEL_ARTIKELKOMMENTARART, panelSplitArtikelkommentarart);
		}
	}

	private void createFarbcode() throws Throwable {
		if (panelSplitFarbcode == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER };

			panelQueryFarbcode = new PanelQuery(ArtikelFilterFactory.getInstance().createQTArtikelkommentarart(), null,
					QueryParameters.UC_ID_FARBCODE, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.farbcode"), true);

			panelQueryFarbcode.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDKennung(),
					SystemFilterFactory.getInstance().createFKDBezeichnung());

			panelBottomFarbcode = new PanelFarbcode(getInternalFrame(), LPMain.getTextRespectUISPr("artikel.farbcode"),
					null);
			panelSplitFarbcode = new PanelSplit(getInternalFrame(), panelBottomFarbcode, panelQueryFarbcode, 380);

			setComponentAt(IDX_PANEL_FARBCODE, panelSplitFarbcode);
		}
	}

	private void createVorschlagstext() throws Throwable {
		if (panelSplitVorschlagstext == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryVorschlagstext = new PanelQuery(null, ArtikelFilterFactory.getInstance().createFKVorschlagstext(),
					QueryParameters.UC_ID_VORSCHLAGSTEXT, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.vorschlagstext"), true);

			panelQueryVorschlagstext
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

			panelBottomVorschlagstext = new PanelVorschlagstext(getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.vorschlagstext"), null);
			panelSplitVorschlagstext = new PanelSplit(getInternalFrame(), panelBottomVorschlagstext,
					panelQueryVorschlagstext, 380);

			setComponentAt(IDX_PANEL_VORSCHLAGSTEXT, panelSplitVorschlagstext);
		}
	}

	private void createReach() throws Throwable {
		if (panelSplitReach == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryReach = new PanelQuery(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_REACH, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.reach"), true);

			panelQueryReach.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			panelBottomReach = new PanelReach(getInternalFrame(), LPMain.getTextRespectUISPr("artikel.reach"), null);
			panelSplitReach = new PanelSplit(getInternalFrame(), panelBottomReach, panelQueryReach, 380);

			setComponentAt(IDX_PANEL_REACH, panelSplitReach);
		}
	}
	private void createLaseroberflaeche() throws Throwable {
		if (panelSplitLaseroberflaeche == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryLaseroberflaeche = new PanelQuery(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_LASEROBERFLAECHE, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.laseroberflaeche"), true);

			panelQueryLaseroberflaeche.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			panelBottomLaseroberflaeche = new PanelLaseroberflaeche(getInternalFrame(), LPMain.getTextRespectUISPr("artikel.laseroberflaeche"), null);
			panelSplitLaseroberflaeche = new PanelSplit(getInternalFrame(), panelBottomLaseroberflaeche, panelQueryLaseroberflaeche, 350);

			setComponentAt(IDX_PANEL_LASEROBERFLAECHE, panelSplitLaseroberflaeche);
		}
	}

	private void createVorzug() throws Throwable {
		if (panelSplitVorzug == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryVorzug = new PanelQuery(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_VORZUG, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.vorzug"), true);

			panelQueryVorzug
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

			panelBottomVorzug = new PanelVorzug(getInternalFrame(), LPMain.getTextRespectUISPr("artikel.vorzug"), null);
			panelSplitVorzug = new PanelSplit(getInternalFrame(), panelBottomVorzug, panelQueryVorzug, 350);

			setComponentAt(IDX_PANEL_VORZUG, panelSplitVorzug);
		}
	}

	private void createAlergen() throws Throwable {
		if (panelSplitAlergen == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1, PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			panelQueryAlergen = new PanelQuery(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_ALERGEN, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.allergen"), true);

			panelQueryAlergen
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

			panelBottomAlergen = new PanelAllergen(getInternalFrame(), LPMain.getTextRespectUISPr("artikel.allergen"),
					null);
			panelSplitAlergen = new PanelSplit(getInternalFrame(), panelBottomAlergen, panelQueryAlergen, 350);

			setComponentAt(IDX_PANEL_ALERGEN, panelSplitAlergen);
		}
	}

	private void createGebinde() throws Throwable {
		if (panelSplitGebinde == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryGebinde = new PanelQuery(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_GEBINDE, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.gebinde"), true);

			panelQueryGebinde
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

			panelBottomGebinde = new PanelGebinde(getInternalFrame(), LPMain.getTextRespectUISPr("artikel.gebinde"),
					null);
			panelSplitGebinde = new PanelSplit(getInternalFrame(), panelBottomGebinde, panelQueryGebinde, 350);

			setComponentAt(IDX_PANEL_GEBINDE, panelSplitGebinde);
		}
	}

	private void createVerpackungsmittel() throws Throwable {
		if (panelSplitVerpackungsmittel == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryVerpackungsmittel = new PanelQuery(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_VERPACKUNGSMITTEL, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.verpackungsmittel"), true);

			panelQueryVerpackungsmittel
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

			panelBottomVerpackungsmittel = new PanelVerpackungsmittel(getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.verpackungsmittel"), null);
			panelSplitVerpackungsmittel = new PanelSplit(getInternalFrame(), panelBottomVerpackungsmittel,
					panelQueryVerpackungsmittel, 320);

			setComponentAt(IDX_PANEL_VERPACKUNGSMITTEL, panelSplitVerpackungsmittel);
		}
	}

	private void createDateiverweis() throws Throwable {
		if (panelSplitDateiverweis == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryDateiverweis = new PanelQuery(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_DATEIVERWEIS, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.dateiverweisuebersetzung"), true);

			panelBottomDateiverweis = new PanelDateiverweis(getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.dateiverweisuebersetzung"), null);
			panelSplitDateiverweis = new PanelSplit(getInternalFrame(), panelBottomDateiverweis, panelQueryDateiverweis,
					320);

			setComponentAt(IDX_PANEL_DATEIVERWEIS, panelSplitDateiverweis);
		}
	}

	private void createRohs() throws Throwable {
		if (panelSplitRohs == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryRohs = new PanelQuery(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_ROHS, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.rohs"), true);

			panelQueryRohs.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			panelBottomRohs = new PanelRohs(getInternalFrame(), LPMain.getTextRespectUISPr("artikel.rohs"), null);
			panelSplitRohs = new PanelSplit(getInternalFrame(), panelBottomRohs, panelQueryRohs, 380);

			setComponentAt(IDX_PANEL_ROHS, panelSplitRohs);
		}
	}

	private void createAutomotive() throws Throwable {
		if (panelSplitAutomotive == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryAutomotive = new PanelQuery(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_AUTOMOTIVE, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.automotive"), true);

			panelQueryAutomotive
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

			panelBottomAutomotive = new PanelAutomotive(getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.automotive"), null);
			panelSplitAutomotive = new PanelSplit(getInternalFrame(), panelBottomAutomotive, panelQueryAutomotive, 380);

			setComponentAt(IDX_PANEL_AUTOMOTIVE, panelSplitAutomotive);
		}
	}

	private void createMedical() throws Throwable {
		if (panelSplitMedical == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryMedical = new PanelQuery(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_MEDICAL, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.medical"), true);

			panelQueryMedical
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

			panelBottomMedical = new PanelMedical(getInternalFrame(), LPMain.getTextRespectUISPr("artikel.medical"),
					null);
			panelSplitMedical = new PanelSplit(getInternalFrame(), panelBottomMedical, panelQueryMedical, 380);

			setComponentAt(IDX_PANEL_MEDICAL, panelSplitMedical);
		}
	}

	private void createWaffenkaliber() throws Throwable {
		if (panelSplitWaffenkaliber == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryWaffenkaliber = new PanelQuery(null, null, QueryParameters.UC_ID_WAFFENKALIBER, aWhichButtonIUse,
					getInternalFrame(), LPMain.getTextRespectUISPr("artikel.waffenkaliber"), true);

			panelQueryWaffenkaliber.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDKennung(),
					SystemFilterFactory.getInstance().createFKDBezeichnung());

			panelBottomWaffenkaliber = new PanelWaffenkaliber(getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.waffenkaliber"), null);
			panelSplitWaffenkaliber = new PanelSplit(getInternalFrame(), panelBottomWaffenkaliber,
					panelQueryWaffenkaliber, 350);

			setComponentAt(IDX_PANEL_WAFFENKALIBER, panelSplitWaffenkaliber);
		}
	}

	private void createWaffenausfuehrung() throws Throwable {
		if (panelSplitWaffenausfuehrung == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryWaffenausfuehrung = new PanelQuery(null, null, QueryParameters.UC_ID_WAFFENAUSFUEHRUNG,
					aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("artikel.waffenausfuehrung"),
					true);

			panelQueryWaffenausfuehrung.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDKennung(),
					SystemFilterFactory.getInstance().createFKDBezeichnung());

			panelBottomWaffenausfuehrung = new PanelWaffenausfuehrung(getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.waffenausfuehrung"), null);
			panelSplitWaffenausfuehrung = new PanelSplit(getInternalFrame(), panelBottomWaffenausfuehrung,
					panelQueryWaffenausfuehrung, 350);

			setComponentAt(IDX_PANEL_WAFFENAUSFUEHRUNG, panelSplitWaffenausfuehrung);
		}
	}

	private void createWaffentyp() throws Throwable {
		if (panelSplitWaffentyp == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryWaffentyp = new PanelQuery(null, null, QueryParameters.UC_ID_WAFFENTYP, aWhichButtonIUse,
					getInternalFrame(), LPMain.getTextRespectUISPr("artikel.waffentyp"), true);

			panelQueryWaffentyp.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDKennung(),
					SystemFilterFactory.getInstance().createFKDBezeichnung());

			panelBottomWaffentyp = new PanelWaffentyp(getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.waffentyp"), null);
			panelSplitWaffentyp = new PanelSplit(getInternalFrame(), panelBottomWaffentyp, panelQueryWaffentyp, 350);

			setComponentAt(IDX_PANEL_WAFFENTYP, panelSplitWaffentyp);
		}
	}

	private void createWaffentypFein() throws Throwable {
		if (panelSplitWaffentypFein == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryWaffentypFein = new PanelQuery(null, null, QueryParameters.UC_ID_WAFFENTYPFEIN, aWhichButtonIUse,
					getInternalFrame(), LPMain.getTextRespectUISPr("artikel.waffentypFein"), true);

			panelQueryWaffentypFein.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDKennung(),
					SystemFilterFactory.getInstance().createFKDBezeichnung());

			panelBottomWaffentypFein = new PanelWaffentypFein(getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.waffentypFein"), null);
			panelSplitWaffentypFein = new PanelSplit(getInternalFrame(), panelBottomWaffentypFein,
					panelQueryWaffentypFein, 350);

			setComponentAt(IDX_PANEL_WAFFENTYPFEIN, panelSplitWaffentypFein);
		}
	}

	private void createWaffenkategorie() throws Throwable {
		if (panelSplitWaffenkategorie == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryWaffenkategorie = new PanelQuery(null, null, QueryParameters.UC_ID_WAFFENKATEGORIE,
					aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("artikel.waffenkategorie"), true);

			panelQueryWaffenkategorie.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDKennung(),
					SystemFilterFactory.getInstance().createFKDBezeichnung());

			panelBottomWaffenkategorie = new PanelWaffenkategorie(getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.waffenkategorie"), null);
			panelSplitWaffenkategorie = new PanelSplit(getInternalFrame(), panelBottomWaffenkategorie,
					panelQueryWaffenkategorie, 350);

			setComponentAt(IDX_PANEL_WAFFENKATEGORIE, panelSplitWaffenkategorie);
		}
	}

	private void createWaffenzusatz() throws Throwable {
		if (panelSplitWaffenzusatz == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryWaffenzusatz = new PanelQuery(null, null, QueryParameters.UC_ID_WAFFENZUSATZ, aWhichButtonIUse,
					getInternalFrame(), LPMain.getTextRespectUISPr("artikel.waffenzusatz"), true);

			panelQueryWaffenzusatz.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDKennung(),
					SystemFilterFactory.getInstance().createFKDBezeichnung());

			panelBottomWaffenzusatz = new PanelWaffenzusatz(getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.waffenzusatz"), null);
			panelSplitWaffenzusatz = new PanelSplit(getInternalFrame(), panelBottomWaffenzusatz, panelQueryWaffenzusatz,
					350);

			setComponentAt(IDX_PANEL_WAFFENZUSATZ, panelSplitWaffenzusatz);
		}
	}

	private void createWebshop() throws Throwable {
		if (panelSplitWebshop == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, };

			panelQueryWebshop = new PanelQuery(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_WEBSHOP, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.webshop"), true);

			panelQueryWebshop
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

			panelBottomWebshop = new PanelWebshop(getInternalFrame(), LPMain.getTextRespectUISPr("lp.webshop"), null);
			panelSplitWebshop = new PanelSplit(getInternalFrame(), panelBottomWebshop, panelQueryWebshop, 250);

			setComponentAt(IDX_PANEL_WEBSHOP, panelSplitWebshop);
		}
	}

	private void createVerleih() throws Throwable {
		if (panelSplitVerleih == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER };

			panelQueryVerleih = new PanelQuery(null, null, QueryParameters.UC_ID_VERLEIH, aWhichButtonIUse,
					getInternalFrame(), LPMain.getTextRespectUISPr("artikel.verleih"), true);

			panelBottomVerleih = new PanelVerleih(getInternalFrame(), LPMain.getTextRespectUISPr("artikel.verleih"),
					null);
			panelSplitVerleih = new PanelSplit(getInternalFrame(), panelBottomVerleih, panelQueryVerleih, 330);

			setComponentAt(IDX_PANEL_VERLEIH, panelSplitVerleih);
		}
	}

	private void createLagerplatz() throws Throwable {
		if (panelSplitLagerplatz == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER };

			panelQueryLagerplatz = new PanelQuery(ArtikelFilterFactory.getInstance().createQTLagerplatz(),
					ArtikelFilterFactory.getInstance().createFKLagerplatz(), QueryParameters.UC_ID_LAGERPLATZ,
					aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("lp.lagerplatz"), true);

			panelQueryLagerplatz
					.befuellePanelFilterkriterienDirekt(ArtikelFilterFactory.getInstance().createFKDLagerplatz(), null);

			panelBottomLagerplatz = new PanelLagerplatz(getInternalFrame(), LPMain.getTextRespectUISPr("lp.lagerplatz"),
					null);
			panelSplitLagerplatz = new PanelSplit(getInternalFrame(), panelBottomLagerplatz, panelQueryLagerplatz, 330);

			setComponentAt(IDX_PANEL_LAGERPLATZ, panelSplitLagerplatz);
		}
	}

	private void createSperren() throws Throwable {
		if (panelSplitSperren == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQuerySperren = new PanelQuery(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_SPERREN, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.sperren"), true);

			panelQuerySperren
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

			panelBottomSperren = new PanelSperren(getInternalFrame(), LPMain.getTextRespectUISPr("lp.sperren"), null);
			panelSplitSperren = new PanelSplit(getInternalFrame(), panelBottomSperren, panelQuerySperren, 210);

			setComponentAt(IDX_PANEL_SPERREN, panelSplitSperren);
		}
	}

	private void createArtikelgruppen() throws Throwable {
		if (panelSplitArtikelgruppen == null) {
			String[] aWhichButtonIUse = null;
			if (zentralerArtikelstammUndNichtHauptmandant()) {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
			} else {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER };
			}

			panelQueryArtikelgruppen = new PanelQuery(ArtikelFilterFactory.getInstance().createQTArtikelgruppe(),
					ArtikelFilterFactory.getInstance().createFKArtgruMandantCNr(), QueryParameters.UC_ID_ARTIKELGRUPPE,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.title.tab.artikelgruppen"), true);

			panelQueryArtikelgruppen.befuellePanelFilterkriterienDirekt(
					ArtikelFilterFactory.getInstance().createFKDArtikelgruppeKennung(),
					SystemFilterFactory.getInstance()
							.createFKDSprTabelleBezeichnung(ArtikelFac.FLR_ARTIKELGRUPPE_ARTIKELGRUPPESPRSET));

			panelBottomArtikelgruppen = new PanelArtikelgruppen(getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.title.tab.artikelgruppen"), null);

			panelSplitArtikelgruppen = new PanelSplit(getInternalFrame(), panelBottomArtikelgruppen,
					panelQueryArtikelgruppen, 260);

			setComponentAt(IDX_PANEL_ARTIKELGRUPPEN, panelSplitArtikelgruppen);
		}
	}

	private void createHersteller() throws Throwable {
		if (panelSplitHersteller == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryHersteller = new PanelQuery(null, null, QueryParameters.UC_ID_ARTIKELHERSTELLER, aWhichButtonIUse,
					getInternalFrame(), LPMain.getTextRespectUISPr("artikel.title.tab.hersteller"), true);
			panelQueryHersteller.befuellePanelFilterkriterienDirekt(
					ArtikelFilterFactory.getInstance().createFKDHersteller(),
					ArtikelFilterFactory.getInstance().createFKDHerstellerPartner());

			panelBottomHersteller = new PanelHersteller(getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.title.tab.hersteller"), null);

			panelSplitHersteller = new PanelSplit(getInternalFrame(), panelBottomHersteller, panelQueryHersteller, 320);

			setComponentAt(IDX_PANEL_HERSTELLER, panelSplitHersteller);
		}
	}

	private void createLager() throws Throwable {
		if (panelSplitLager == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			panelQueryLager = new PanelQuery(ArtikelFilterFactory.getInstance().createQTLager(),
					ArtikelFilterFactory.getInstance().createFKLagerlisteMitVersteckten(),
					QueryParameters.UC_ID_LAGER_ALLE, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("button.lager"), true,
					ArtikelFilterFactory.getInstance().createFKVLagerGrunddaten(), null);
			panelQueryLager.befuellePanelFilterkriterienDirekt(
					ArtikelFilterFactory.getInstance().createFKDLagernameGrunddaten(),
					ArtikelFilterFactory.getInstance().createFKDLagerLagerartGrunddaten());

			panelBottomLager = new PanelLager(getInternalFrame(), LPMain.getTextRespectUISPr("button.lager"), null);

			panelSplitLager = new PanelSplit(getInternalFrame(), panelBottomLager, panelQueryLager, 290);

			setComponentAt(IDX_PANEL_LAGER, panelSplitLager);
		}
	}

	public boolean zentralerArtikelstammUndNichtHauptmandant() throws Throwable {
		boolean bZentralerArtikelstamm = LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM);

		boolean bIstHauptmandant = false;

		String hauptMandant = DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant()).getAnwenderDto()
				.getMandantCNrHauptmandant();
		if (hauptMandant.equals(LPMain.getTheClient().getMandant())) {
			bIstHauptmandant = true;
		}

		if (bZentralerArtikelstamm == true && bIstHauptmandant == false) {
			return true;
		} else {
			return false;
		}

	}

	private void jbInit() throws Throwable {

		boolean bZentralerArtikelstamm = LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM);
		boolean bGetrennteLager = LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER);

		int tabIndex = 0;

		boolean bIstHauptmandant = false;

		String hauptMandant = DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant()).getAnwenderDto()
				.getMandantCNrHauptmandant();
		if (hauptMandant.equals(LPMain.getTheClient().getMandant())) {
			bIstHauptmandant = true;
		}

		// SP3192
		if (bZentralerArtikelstamm && bGetrennteLager && bIstHauptmandant == false) {

			IDX_PANEL_ARTIKELGRUPPEN = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("artikel.title.tab.artikelgruppen"), null, null,
					LPMain.getTextRespectUISPr("artikel.title.tab.artikelgruppen"), IDX_PANEL_ARTIKELGRUPPEN);
			tabIndex++;

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MEHRLAGERVERWALTUNG)) {

				IDX_PANEL_LAGER = tabIndex;
				insertTab(LPMain.getTextRespectUISPr("label.lager"), null, null,
						LPMain.getTextRespectUISPr("label.lager"), IDX_PANEL_LAGER);
				tabIndex++;
				createLager();

			}

			IDX_PANEL_LAGERPLATZ = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("lp.lagerplatz"), null, null,
					LPMain.getTextRespectUISPr("lp.lagerplatz"), IDX_PANEL_LAGERPLATZ);

			tabIndex++;
			createLagerplatz();
		} else {

			IDX_PANEL_ARTIKELKLASSEN = tabIndex;
			// 1 tab oben: QP1 PartnerFLR; lazy loading
			insertTab(LPMain.getTextRespectUISPr("artikel.title.tab.artikelklassen"), null, null,
					LPMain.getTextRespectUISPr("artikel.title.tab.artikelklassen"), IDX_PANEL_ARTIKELKLASSEN);
			tabIndex++;
			IDX_PANEL_ARTIKELGRUPPEN = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("artikel.title.tab.artikelgruppen"), null, null,
					LPMain.getTextRespectUISPr("artikel.title.tab.artikelgruppen"), IDX_PANEL_ARTIKELGRUPPEN);

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MEHRLAGERVERWALTUNG)) {
				tabIndex++;
				IDX_PANEL_LAGER = tabIndex;
				insertTab(LPMain.getTextRespectUISPr("label.lager"), null, null,
						LPMain.getTextRespectUISPr("label.lager"), IDX_PANEL_LAGER);
			}
			tabIndex++;
			IDX_PANEL_HERSTELLER = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("artikel.title.tab.hersteller"), null, null,
					LPMain.getTextRespectUISPr("artikel.title.tab.hersteller"), IDX_PANEL_HERSTELLER);

			tabIndex++;
			IDX_PANEL_ARTIKELKOMMENTARART = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("lp.kommentarart"), null, null,
					LPMain.getTextRespectUISPr("lp.kommentarart"), IDX_PANEL_ARTIKELKOMMENTARART);

			tabIndex++;
			IDX_PANEL_FARBCODE = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("artikel.farbcode"), null, null,
					LPMain.getTextRespectUISPr("artikel.farbcode"), IDX_PANEL_FARBCODE);
			tabIndex++;
			IDX_PANEL_SPERREN = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("lp.sperren"), null, null, LPMain.getTextRespectUISPr("lp.sperren"),
					IDX_PANEL_SPERREN);
			tabIndex++;
			IDX_PANEL_LAGERPLATZ = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("lp.lagerplatz"), null, null,
					LPMain.getTextRespectUISPr("lp.lagerplatz"), IDX_PANEL_LAGERPLATZ);

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_VERLEIH)) {
				tabIndex++;
				IDX_PANEL_VERLEIH = tabIndex;
				insertTab(LPMain.getTextRespectUISPr("artikel.verleih"), null, null,
						LPMain.getTextRespectUISPr("artikel.verleih"), IDX_PANEL_VERLEIH);
			}

			tabIndex++;
			IDX_PANEL_VORSCHLAGSTEXT = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("artikel.vorschlagstext"), null, null,
					LPMain.getTextRespectUISPr("artikel.vorschlagstext"), IDX_PANEL_VORSCHLAGSTEXT);
			tabIndex++;
			IDX_PANEL_WEBSHOP = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("lp.webshop"), null, null, LPMain.getTextRespectUISPr("lp.webshop"),
					IDX_PANEL_WEBSHOP);

			tabIndex++;
			IDX_PANEL_REACH = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("artikel.reach"), null, null,
					LPMain.getTextRespectUISPr("artikel.reach"), IDX_PANEL_REACH);

			tabIndex++;
			IDX_PANEL_ROHS = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("artikel.rohs"), null, null,
					LPMain.getTextRespectUISPr("artikel.rohs"), IDX_PANEL_ROHS);

			tabIndex++;
			IDX_PANEL_AUTOMOTIVE = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("artikel.automotive"), null, null,
					LPMain.getTextRespectUISPr("artikel.automotive"), IDX_PANEL_AUTOMOTIVE);

			tabIndex++;
			IDX_PANEL_MEDICAL = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("artikel.medical"), null, null,
					LPMain.getTextRespectUISPr("artikel.medical"), IDX_PANEL_MEDICAL);
			tabIndex++;
			IDX_PANEL_VORZUG = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("artikel.vorzug"), null, null,
					LPMain.getTextRespectUISPr("artikel.vorzug"), IDX_PANEL_VORZUG);
			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_KUECHE)) {

				tabIndex++;
				IDX_PANEL_ALERGEN = tabIndex;
				insertTab(LPMain.getTextRespectUISPr("artikel.allergen"), null, null,
						LPMain.getTextRespectUISPr("artikel.allergen"), IDX_PANEL_ALERGEN);
			}

			tabIndex++;
			IDX_PANEL_GEBINDE = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("artikel.gebinde"), null, null,
					LPMain.getTextRespectUISPr("artikel.gebinde"), IDX_PANEL_GEBINDE);

			tabIndex++;
			IDX_PANEL_VERPACKUNGSMITTEL = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("artikel.verpackungsmittel"), null, null,
					LPMain.getTextRespectUISPr("artikel.verpackungsmittel"), IDX_PANEL_VERPACKUNGSMITTEL);

			tabIndex++;
			IDX_PANEL_DATEIVERWEIS = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("artikel.dateiverweisuebersetzung"), null, null,
					LPMain.getTextRespectUISPr("artikel.dateiverweisuebersetzung"), IDX_PANEL_DATEIVERWEIS);

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_WAFFENREGISTER)) {
				tabIndex++;
				IDX_PANEL_WAFFENKALIBER = tabIndex;
				insertTab(LPMain.getTextRespectUISPr("artikel.waffenkaliber"), null, null,
						LPMain.getTextRespectUISPr("artikel.waffenkaliber"), IDX_PANEL_WAFFENKALIBER);
				tabIndex++;
				IDX_PANEL_WAFFENTYP = tabIndex;
				insertTab(LPMain.getTextRespectUISPr("artikel.waffentyp"), null, null,
						LPMain.getTextRespectUISPr("artikel.waffentyp"), IDX_PANEL_WAFFENTYP);
				tabIndex++;
				IDX_PANEL_WAFFENTYPFEIN = tabIndex;
				insertTab(LPMain.getTextRespectUISPr("artikel.waffentypfein"), null, null,
						LPMain.getTextRespectUISPr("artikel.waffentypfein"), IDX_PANEL_WAFFENTYPFEIN);
				tabIndex++;
				IDX_PANEL_WAFFENZUSATZ = tabIndex;
				insertTab(LPMain.getTextRespectUISPr("artikel.waffenzusatz"), null, null,
						LPMain.getTextRespectUISPr("artikel.waffenzusatz"), IDX_PANEL_WAFFENZUSATZ);
				tabIndex++;
				IDX_PANEL_WAFFENKATEGORIE = tabIndex;
				insertTab(LPMain.getTextRespectUISPr("artikel.waffenkategorie"), null, null,
						LPMain.getTextRespectUISPr("artikel.waffenkategorie"), IDX_PANEL_WAFFENKATEGORIE);
				tabIndex++;
				IDX_PANEL_WAFFENAUSFUEHRUNG = tabIndex;
				insertTab(LPMain.getTextRespectUISPr("artikel.waffenausfuehrung"), null, null,
						LPMain.getTextRespectUISPr("artikel.waffenausfuehrung"), IDX_PANEL_WAFFENAUSFUEHRUNG);

			}

			
			tabIndex++;
			IDX_PANEL_LASEROBERFLAECHE = tabIndex;
			insertTab(LPMain.getTextRespectUISPr("artikel.laseroberflaeche"), null, null,
					LPMain.getTextRespectUISPr("artikel.laseroberflaeche"), IDX_PANEL_LASEROBERFLAECHE);

			
			createArtikelklassen();

		}
		// Itemevents an MEIN Detailpanel senden kann.
		this.addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryArtikelklassen) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomArtikelklassen.setKeyWhenDetailPanel(key);
				panelBottomArtikelklassen.eventYouAreSelected(false);
				panelQueryArtikelklassen.updateButtons();

			} else if (e.getSource() == panelQueryArtikelgruppen) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomArtikelgruppen.setKeyWhenDetailPanel(key);
				panelBottomArtikelgruppen.eventYouAreSelected(false);
				panelQueryArtikelgruppen.updateButtons();

			} else if (e.getSource() == panelQueryVerleih) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomVerleih.setKeyWhenDetailPanel(key);
				panelBottomVerleih.eventYouAreSelected(false);
				panelQueryVerleih.updateButtons();

			} else if (e.getSource() == panelQueryHersteller) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomHersteller.setKeyWhenDetailPanel(key);
				panelBottomHersteller.eventYouAreSelected(false);
				panelQueryHersteller.updateButtons();

			} else if (e.getSource() == panelQueryLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomLager.setKeyWhenDetailPanel(key);
				panelBottomLager.eventYouAreSelected(false);
				panelQueryLager.updateButtons();

			} else if (e.getSource() == panelQueryArtikelkommentarart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomArtikelkommentarart.setKeyWhenDetailPanel(key);
				panelBottomArtikelkommentarart.eventYouAreSelected(false);
				panelQueryArtikelkommentarart.updateButtons();

			} else if (e.getSource() == panelQueryFarbcode) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomFarbcode.setKeyWhenDetailPanel(key);
				panelBottomFarbcode.eventYouAreSelected(false);
				panelQueryFarbcode.updateButtons();

			} else if (e.getSource() == panelQueryLagerplatz) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomLagerplatz.setKeyWhenDetailPanel(key);
				panelBottomLagerplatz.eventYouAreSelected(false);
				panelQueryLagerplatz.updateButtons();

			} else if (e.getSource() == panelQuerySperren) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomSperren.setKeyWhenDetailPanel(key);
				panelBottomSperren.eventYouAreSelected(false);
				panelQuerySperren.updateButtons();

			} else if (e.getSource() == panelQueryVorschlagstext) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomVorschlagstext.setKeyWhenDetailPanel(key);
				panelBottomVorschlagstext.eventYouAreSelected(false);
				panelQueryVorschlagstext.updateButtons();

			} else if (e.getSource() == panelQueryWebshop) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomWebshop.setKeyWhenDetailPanel(key);
				panelBottomWebshop.eventYouAreSelected(false);
				panelQueryWebshop.updateButtons();

			}

			else if (e.getSource() == panelQueryReach) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomReach.setKeyWhenDetailPanel(key);
				panelBottomReach.eventYouAreSelected(false);
				panelQueryReach.updateButtons();

			}else if (e.getSource() == panelQueryLaseroberflaeche) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomLaseroberflaeche.setKeyWhenDetailPanel(key);
				panelBottomLaseroberflaeche.eventYouAreSelected(false);
				panelQueryLaseroberflaeche.updateButtons();

			} else if (e.getSource() == panelQueryRohs) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomRohs.setKeyWhenDetailPanel(key);
				panelBottomRohs.eventYouAreSelected(false);
				panelQueryRohs.updateButtons();

			} else if (e.getSource() == panelQueryAutomotive) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomAutomotive.setKeyWhenDetailPanel(key);
				panelBottomAutomotive.eventYouAreSelected(false);
				panelQueryAutomotive.updateButtons();

			} else if (e.getSource() == panelQueryMedical) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomMedical.setKeyWhenDetailPanel(key);
				panelBottomMedical.eventYouAreSelected(false);
				panelQueryMedical.updateButtons();

			} else if (e.getSource() == panelQueryVorzug) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomVorzug.setKeyWhenDetailPanel(key);
				panelBottomVorzug.eventYouAreSelected(false);
				panelQueryVorzug.updateButtons();

			} else if (e.getSource() == panelQueryAlergen) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomAlergen.setKeyWhenDetailPanel(key);
				panelBottomAlergen.eventYouAreSelected(false);
				panelQueryAlergen.updateButtons();

			} else if (e.getSource() == panelQueryGebinde) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomGebinde.setKeyWhenDetailPanel(key);
				panelBottomGebinde.eventYouAreSelected(false);
				panelQueryGebinde.updateButtons();

			} else if (e.getSource() == panelQueryVerpackungsmittel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomVerpackungsmittel.setKeyWhenDetailPanel(key);
				panelBottomVerpackungsmittel.eventYouAreSelected(false);
				panelQueryVerpackungsmittel.updateButtons();

			} else if (e.getSource() == panelQueryDateiverweis) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomDateiverweis.setKeyWhenDetailPanel(key);
				panelBottomDateiverweis.eventYouAreSelected(false);
				panelQueryDateiverweis.updateButtons();

			} else if (e.getSource() == panelQueryWaffenkaliber) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomWaffenkaliber.setKeyWhenDetailPanel(key);
				panelBottomWaffenkaliber.eventYouAreSelected(false);
				panelQueryWaffenkaliber.updateButtons();

			} else if (e.getSource() == panelQueryWaffenausfuehrung) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomWaffenausfuehrung.setKeyWhenDetailPanel(key);
				panelBottomWaffenausfuehrung.eventYouAreSelected(false);
				panelQueryWaffenausfuehrung.updateButtons();

			} else if (e.getSource() == panelQueryWaffentyp) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomWaffentyp.setKeyWhenDetailPanel(key);
				panelBottomWaffentyp.eventYouAreSelected(false);
				panelQueryWaffentyp.updateButtons();

			} else if (e.getSource() == panelQueryWaffentypFein) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomWaffentypFein.setKeyWhenDetailPanel(key);
				panelBottomWaffentypFein.eventYouAreSelected(false);
				panelQueryWaffentypFein.updateButtons();

			} else if (e.getSource() == panelQueryWaffenkategorie) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomWaffenkategorie.setKeyWhenDetailPanel(key);
				panelBottomWaffenkategorie.eventYouAreSelected(false);
				panelQueryWaffenkategorie.updateButtons();

			} else if (e.getSource() == panelQueryWaffenzusatz) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomWaffenzusatz.setKeyWhenDetailPanel(key);
				panelBottomWaffenzusatz.eventYouAreSelected(false);
				panelQueryWaffenzusatz.updateButtons();

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomArtikelklassen) {
				panelQueryArtikelklassen.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomArtikelgruppen) {
				panelQueryArtikelgruppen.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomHersteller) {
				panelQueryHersteller.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomVerleih) {
				panelQueryVerleih.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomLager) {
				panelQueryLager.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomArtikelkommentarart) {
				panelQueryArtikelkommentarart.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomFarbcode) {
				panelQueryFarbcode.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomSperren) {
				panelQuerySperren.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomLagerplatz) {
				panelQueryLagerplatz.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomVorschlagstext) {
				panelQueryVorschlagstext.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomWebshop) {
				panelQueryWebshop.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}

			else if (e.getSource() == panelBottomReach) {
				panelQueryReach.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}else if (e.getSource() == panelBottomLaseroberflaeche) {
				panelQueryLaseroberflaeche.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomRohs) {
				panelQueryRohs.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomAutomotive) {
				panelQueryAutomotive.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomMedical) {
				panelQueryMedical.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomVorzug) {
				panelQueryVorzug.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomAlergen) {
				panelQueryAlergen.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomGebinde) {
				panelQueryGebinde.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomVerpackungsmittel) {
				panelQueryVerpackungsmittel.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomDateiverweis) {
				panelQueryDateiverweis.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomWaffenkaliber) {
				panelQueryWaffenkaliber.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomWaffenausfuehrung) {
				panelQueryWaffenausfuehrung.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomWaffentyp) {
				panelQueryWaffentyp.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomWaffentypFein) {
				panelQueryWaffentypFein.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomWaffenkategorie) {
				panelQueryWaffenkategorie.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomWaffenzusatz) {
				panelQueryWaffenzusatz.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}

		}

		else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			panelSplitArtikelklassen.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryArtikelklassen) {
				panelBottomArtikelklassen.eventActionNew(e, true, false);
				panelBottomArtikelklassen.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryHersteller) {
				panelBottomHersteller.eventActionNew(e, true, false);
				panelBottomHersteller.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryArtikelgruppen) {
				panelBottomArtikelgruppen.eventActionNew(e, true, false);
				panelBottomArtikelgruppen.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryLager) {
				panelBottomLager.eventActionNew(e, true, false);
				panelBottomLager.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryArtikelkommentarart) {
				panelBottomArtikelkommentarart.eventActionNew(e, true, false);
				panelBottomArtikelkommentarart.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryFarbcode) {
				panelBottomFarbcode.eventActionNew(e, true, false);
				panelBottomFarbcode.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryLagerplatz) {
				panelBottomLagerplatz.eventActionNew(e, true, false);
				panelBottomLagerplatz.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryVerleih) {
				panelBottomVerleih.eventActionNew(e, true, false);
				panelBottomVerleih.eventYouAreSelected(false);
			} else if (e.getSource() == panelQuerySperren) {
				panelBottomSperren.eventActionNew(e, true, false);
				panelBottomSperren.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryVorschlagstext) {
				panelBottomVorschlagstext.eventActionNew(e, true, false);
				panelBottomVorschlagstext.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryWebshop) {
				panelBottomWebshop.eventActionNew(e, true, false);
				panelBottomWebshop.eventYouAreSelected(false);
			}

			else if (e.getSource() == panelQueryReach) {
				panelBottomReach.eventActionNew(e, true, false);
				panelBottomReach.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryLaseroberflaeche) {
				panelBottomLaseroberflaeche.eventActionNew(e, true, false);
				panelBottomLaseroberflaeche.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryRohs) {
				panelBottomRohs.eventActionNew(e, true, false);
				panelBottomRohs.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryAutomotive) {
				panelBottomAutomotive.eventActionNew(e, true, false);
				panelBottomAutomotive.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryMedical) {
				panelBottomMedical.eventActionNew(e, true, false);
				panelBottomMedical.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryVorzug) {
				panelBottomVorzug.eventActionNew(e, true, false);
				panelBottomVorzug.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryAlergen) {
				panelBottomAlergen.eventActionNew(e, true, false);
				panelBottomAlergen.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryGebinde) {
				panelBottomGebinde.eventActionNew(e, true, false);
				panelBottomGebinde.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryVerpackungsmittel) {
				panelBottomVerpackungsmittel.eventActionNew(e, true, false);
				panelBottomVerpackungsmittel.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryDateiverweis) {
				panelBottomDateiverweis.eventActionNew(e, true, false);
				panelBottomDateiverweis.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryWaffenkaliber) {
				panelBottomWaffenkaliber.eventActionNew(e, true, false);
				panelBottomWaffenkaliber.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryWaffenausfuehrung) {
				panelBottomWaffenausfuehrung.eventActionNew(e, true, false);
				panelBottomWaffenausfuehrung.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryWaffentyp) {
				panelBottomWaffentyp.eventActionNew(e, true, false);
				panelBottomWaffentyp.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryWaffentypFein) {
				panelBottomWaffentypFein.eventActionNew(e, true, false);
				panelBottomWaffentypFein.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryWaffenkategorie) {
				panelBottomWaffenkategorie.eventActionNew(e, true, false);
				panelBottomWaffenkategorie.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryWaffenzusatz) {
				panelBottomWaffenzusatz.eventActionNew(e, true, false);
				panelBottomWaffenzusatz.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomArtikelklassen) {
				panelSplitArtikelklassen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomArtikelgruppen) {
				panelSplitArtikelgruppen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomHersteller) {
				panelSplitHersteller.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLager) {
				panelSplitLager.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLagerplatz) {
				panelSplitLagerplatz.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomArtikelkommentarart) {
				panelSplitArtikelkommentarart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomFarbcode) {
				panelSplitFarbcode.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomVerleih) {
				panelSplitVerleih.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSperren) {
				panelSplitSperren.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomVorschlagstext) {
				panelSplitVorschlagstext.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWebshop) {
				panelSplitWebshop.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomReach) {
				panelSplitReach.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomRohs) {
				panelSplitRohs.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLaseroberflaeche) {
				panelSplitLaseroberflaeche.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomAutomotive) {
				panelSplitAutomotive.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomMedical) {
				panelSplitMedical.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomVorzug) {
				panelSplitVorzug.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomAlergen) {
				panelSplitAlergen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomGebinde) {
				panelSplitGebinde.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomVerpackungsmittel) {
				panelSplitVerpackungsmittel.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomDateiverweis) {
				panelSplitDateiverweis.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWaffenkaliber) {
				panelSplitWaffenkaliber.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWaffenausfuehrung) {
				panelSplitWaffenausfuehrung.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWaffentyp) {
				panelSplitWaffentyp.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWaffentypFein) {
				panelSplitWaffentypFein.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWaffenkategorie) {
				panelSplitWaffenkategorie.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWaffenzusatz) {
				panelSplitWaffenzusatz.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomArtikelklassen) {
				Object oKey = panelBottomArtikelklassen.getKeyWhenDetailPanel();
				panelQueryArtikelklassen.eventYouAreSelected(false);
				panelQueryArtikelklassen.setSelectedId(oKey);
				panelSplitArtikelklassen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomArtikelgruppen) {
				Object oKey = panelBottomArtikelgruppen.getKeyWhenDetailPanel();
				panelQueryArtikelgruppen.eventYouAreSelected(false);
				panelQueryArtikelgruppen.setSelectedId(oKey);
				panelSplitArtikelgruppen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomHersteller) {
				Object oKey = panelBottomHersteller.getKeyWhenDetailPanel();
				panelQueryHersteller.eventYouAreSelected(false);
				panelQueryHersteller.setSelectedId(oKey);
				panelSplitHersteller.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLager) {
				Object oKey = panelBottomLager.getKeyWhenDetailPanel();
				panelQueryLager.eventYouAreSelected(false);
				panelQueryLager.setSelectedId(oKey);
				panelSplitLager.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomVerleih) {
				Object oKey = panelBottomVerleih.getKeyWhenDetailPanel();
				panelQueryVerleih.eventYouAreSelected(false);
				panelQueryVerleih.setSelectedId(oKey);
				panelSplitVerleih.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomArtikelkommentarart) {
				Object oKey = panelBottomArtikelkommentarart.getKeyWhenDetailPanel();
				panelQueryArtikelkommentarart.eventYouAreSelected(false);
				panelQueryArtikelkommentarart.setSelectedId(oKey);
				panelSplitArtikelkommentarart.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomFarbcode) {
				Object oKey = panelBottomFarbcode.getKeyWhenDetailPanel();
				panelQueryFarbcode.eventYouAreSelected(false);
				panelQueryFarbcode.setSelectedId(oKey);
				panelSplitFarbcode.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomSperren) {
				Object oKey = panelBottomSperren.getKeyWhenDetailPanel();
				panelQuerySperren.eventYouAreSelected(false);
				panelQuerySperren.setSelectedId(oKey);
				panelSplitSperren.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomLagerplatz) {
				Object oKey = panelBottomLagerplatz.getKeyWhenDetailPanel();
				panelQueryLagerplatz.eventYouAreSelected(false);
				panelQueryLagerplatz.setSelectedId(oKey);
				panelSplitLagerplatz.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomVorschlagstext) {
				Object oKey = panelBottomVorschlagstext.getKeyWhenDetailPanel();
				panelQueryVorschlagstext.eventYouAreSelected(false);
				panelQueryVorschlagstext.setSelectedId(oKey);
				panelSplitVorschlagstext.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomWebshop) {
				Object oKey = panelBottomWebshop.getKeyWhenDetailPanel();
				panelQueryWebshop.eventYouAreSelected(false);
				panelQueryWebshop.setSelectedId(oKey);
				panelSplitWebshop.eventYouAreSelected(false);

			}

			else if (e.getSource() == panelBottomReach) {
				Object oKey = panelBottomReach.getKeyWhenDetailPanel();
				panelQueryReach.eventYouAreSelected(false);
				panelQueryReach.setSelectedId(oKey);
				panelSplitReach.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomLaseroberflaeche) {
				Object oKey = panelBottomLaseroberflaeche.getKeyWhenDetailPanel();
				panelQueryLaseroberflaeche.eventYouAreSelected(false);
				panelQueryLaseroberflaeche.setSelectedId(oKey);
				panelSplitLaseroberflaeche.eventYouAreSelected(false);

			}else if (e.getSource() == panelBottomRohs) {
				Object oKey = panelBottomRohs.getKeyWhenDetailPanel();
				panelQueryRohs.eventYouAreSelected(false);
				panelQueryRohs.setSelectedId(oKey);
				panelSplitRohs.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomAutomotive) {
				Object oKey = panelBottomAutomotive.getKeyWhenDetailPanel();
				panelQueryAutomotive.eventYouAreSelected(false);
				panelQueryAutomotive.setSelectedId(oKey);
				panelSplitAutomotive.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomMedical) {
				Object oKey = panelBottomMedical.getKeyWhenDetailPanel();
				panelQueryMedical.eventYouAreSelected(false);
				panelQueryMedical.setSelectedId(oKey);
				panelSplitMedical.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomVorzug) {
				Object oKey = panelBottomVorzug.getKeyWhenDetailPanel();
				panelQueryVorzug.eventYouAreSelected(false);
				panelQueryVorzug.setSelectedId(oKey);
				panelSplitVorzug.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomAlergen) {
				Object oKey = panelBottomAlergen.getKeyWhenDetailPanel();
				panelQueryAlergen.eventYouAreSelected(false);
				panelQueryAlergen.setSelectedId(oKey);
				panelSplitAlergen.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomGebinde) {
				Object oKey = panelBottomGebinde.getKeyWhenDetailPanel();
				panelQueryGebinde.eventYouAreSelected(false);
				panelQueryGebinde.setSelectedId(oKey);
				panelSplitGebinde.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomVerpackungsmittel) {
				Object oKey = panelBottomVerpackungsmittel.getKeyWhenDetailPanel();
				panelQueryVerpackungsmittel.eventYouAreSelected(false);
				panelQueryVerpackungsmittel.setSelectedId(oKey);
				panelSplitVerpackungsmittel.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomDateiverweis) {
				Object oKey = panelBottomDateiverweis.getKeyWhenDetailPanel();
				panelQueryDateiverweis.eventYouAreSelected(false);
				panelQueryDateiverweis.setSelectedId(oKey);
				panelSplitDateiverweis.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomWaffenkaliber) {
				Object oKey = panelBottomWaffenkaliber.getKeyWhenDetailPanel();
				panelQueryWaffenkaliber.eventYouAreSelected(false);
				panelQueryWaffenkaliber.setSelectedId(oKey);
				panelSplitWaffenkaliber.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomWaffenausfuehrung) {
				Object oKey = panelBottomWaffenausfuehrung.getKeyWhenDetailPanel();
				panelQueryWaffenausfuehrung.eventYouAreSelected(false);
				panelQueryWaffenausfuehrung.setSelectedId(oKey);
				panelSplitWaffenausfuehrung.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomWaffentyp) {
				Object oKey = panelBottomWaffentyp.getKeyWhenDetailPanel();
				panelQueryWaffentyp.eventYouAreSelected(false);
				panelQueryWaffentyp.setSelectedId(oKey);
				panelSplitWaffentyp.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomWaffentypFein) {
				Object oKey = panelBottomWaffentypFein.getKeyWhenDetailPanel();
				panelQueryWaffentypFein.eventYouAreSelected(false);
				panelQueryWaffentypFein.setSelectedId(oKey);
				panelSplitWaffentypFein.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomWaffenkategorie) {
				Object oKey = panelBottomWaffenkategorie.getKeyWhenDetailPanel();
				panelQueryWaffenkategorie.eventYouAreSelected(false);
				panelQueryWaffenkategorie.setSelectedId(oKey);
				panelSplitWaffenkategorie.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomWaffenzusatz) {
				Object oKey = panelBottomWaffenzusatz.getKeyWhenDetailPanel();
				panelQueryWaffenzusatz.eventYouAreSelected(false);
				panelQueryWaffenzusatz.setSelectedId(oKey);
				panelSplitWaffenzusatz.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomArtikelklassen) {
				Object oKey = panelQueryArtikelklassen.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomArtikelklassen.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArtikelklassen.getId2SelectAfterDelete();
					panelQueryArtikelklassen.setSelectedId(oNaechster);
				}
				panelSplitArtikelklassen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomArtikelgruppen) {
				Object oKey = panelQueryArtikelgruppen.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomArtikelgruppen.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArtikelgruppen.getId2SelectAfterDelete();
					panelQueryArtikelgruppen.setSelectedId(oNaechster);
				}
				panelSplitArtikelgruppen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomHersteller) {
				Object oKey = panelQueryHersteller.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomHersteller.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryHersteller.getId2SelectAfterDelete();
					panelQueryHersteller.setSelectedId(oNaechster);
				}
				panelSplitHersteller.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomVerleih) {
				Object oKey = panelQueryVerleih.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomVerleih.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryVerleih.getId2SelectAfterDelete();
					panelQueryVerleih.setSelectedId(oNaechster);
				}
				panelSplitVerleih.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLager) {
				Object oKey = panelQueryLager.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomLager.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLager.getId2SelectAfterDelete();
					panelQueryLager.setSelectedId(oNaechster);
				}
				panelSplitLager.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomArtikelkommentarart) {
				Object oKey = panelQueryArtikelkommentarart.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomArtikelkommentarart.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArtikelkommentarart.getId2SelectAfterDelete();
					panelQueryArtikelkommentarart.setSelectedId(oNaechster);
				}
				panelSplitArtikelkommentarart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomFarbcode) {
				Object oKey = panelQueryFarbcode.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomFarbcode.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryFarbcode.getId2SelectAfterDelete();
					panelQueryFarbcode.setSelectedId(oNaechster);
				}
				panelSplitFarbcode.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSperren) {
				Object oKey = panelQuerySperren.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomSperren.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQuerySperren.getId2SelectAfterDelete();
					panelQuerySperren.setSelectedId(oNaechster);
				}
				panelSplitSperren.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLagerplatz) {
				Object oKey = panelQueryLagerplatz.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomLagerplatz.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLagerplatz.getId2SelectAfterDelete();
					panelQueryLagerplatz.setSelectedId(oNaechster);
				}
				panelSplitLagerplatz.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomVorschlagstext) {
				Object oKey = panelQueryVorschlagstext.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomVorschlagstext.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryVorschlagstext.getId2SelectAfterDelete();
					panelQueryVorschlagstext.setSelectedId(oNaechster);
				}
				panelSplitVorschlagstext.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWebshop) {
				Object oKey = panelQueryWebshop.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomWebshop.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryWebshop.getId2SelectAfterDelete();
					panelQueryWebshop.setSelectedId(oNaechster);
				}
				panelSplitWebshop.eventYouAreSelected(false);
			}

			else if (e.getSource() == panelBottomReach) {
				Object oKey = panelQueryReach.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomReach.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryReach.getId2SelectAfterDelete();
					panelQueryReach.setSelectedId(oNaechster);
				}
				panelSplitReach.eventYouAreSelected(false);
			}else if (e.getSource() == panelBottomLaseroberflaeche) {
				Object oKey = panelQueryLaseroberflaeche.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomLaseroberflaeche.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLaseroberflaeche.getId2SelectAfterDelete();
					panelQueryLaseroberflaeche.setSelectedId(oNaechster);
				}
				panelSplitLaseroberflaeche.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomRohs) {
				Object oKey = panelQueryRohs.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomRohs.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryRohs.getId2SelectAfterDelete();
					panelQueryRohs.setSelectedId(oNaechster);
				}
				panelSplitRohs.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomAutomotive) {
				Object oKey = panelQueryAutomotive.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomAutomotive.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryAutomotive.getId2SelectAfterDelete();
					panelQueryAutomotive.setSelectedId(oNaechster);
				}
				panelSplitAutomotive.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomMedical) {
				Object oKey = panelQueryMedical.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomMedical.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryMedical.getId2SelectAfterDelete();
					panelQueryMedical.setSelectedId(oNaechster);
				}
				panelSplitMedical.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomVorzug) {
				Object oKey = panelQueryVorzug.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomVorzug.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryVorzug.getId2SelectAfterDelete();
					panelQueryVorzug.setSelectedId(oNaechster);
				}
				panelSplitVorzug.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomAlergen) {
				Object oKey = panelQueryAlergen.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomAlergen.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryAlergen.getId2SelectAfterDelete();
					panelQueryAlergen.setSelectedId(oNaechster);
				}
				panelSplitAlergen.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomGebinde) {
				Object oKey = panelQueryGebinde.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomGebinde.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryGebinde.getId2SelectAfterDelete();
					panelQueryGebinde.setSelectedId(oNaechster);
				}
				panelSplitGebinde.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomVerpackungsmittel) {
				Object oKey = panelQueryVerpackungsmittel.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomVerpackungsmittel.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryVerpackungsmittel.getId2SelectAfterDelete();
					panelQueryVerpackungsmittel.setSelectedId(oNaechster);
				}
				panelSplitVerpackungsmittel.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomDateiverweis) {
				Object oKey = panelQueryDateiverweis.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomDateiverweis.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryDateiverweis.getId2SelectAfterDelete();
					panelQueryDateiverweis.setSelectedId(oNaechster);
				}
				panelSplitDateiverweis.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWaffenkaliber) {
				Object oKey = panelQueryWaffenkaliber.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomWaffenkaliber.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryWaffenkaliber.getId2SelectAfterDelete();
					panelQueryWaffenkaliber.setSelectedId(oNaechster);
				}
				panelSplitWaffenkaliber.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWaffenausfuehrung) {
				Object oKey = panelQueryWaffenausfuehrung.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomWaffenausfuehrung.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryWaffenausfuehrung.getId2SelectAfterDelete();
					panelQueryWaffenausfuehrung.setSelectedId(oNaechster);
				}
				panelSplitWaffenausfuehrung.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWaffentyp) {
				Object oKey = panelQueryWaffentyp.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomWaffentyp.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryWaffentyp.getId2SelectAfterDelete();
					panelQueryWaffentyp.setSelectedId(oNaechster);
				}
				panelSplitWaffentyp.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWaffentypFein) {
				Object oKey = panelQueryWaffentypFein.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomWaffentypFein.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryWaffentypFein.getId2SelectAfterDelete();
					panelQueryWaffentypFein.setSelectedId(oNaechster);
				}
				panelSplitWaffentypFein.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWaffenkategorie) {
				Object oKey = panelQueryWaffenkategorie.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomWaffenkategorie.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryWaffenkategorie.getId2SelectAfterDelete();
					panelQueryWaffenkategorie.setSelectedId(oNaechster);
				}
				panelSplitWaffenzusatz.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomWaffenzusatz) {
				Object oKey = panelQueryWaffenzusatz.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomWaffenzusatz.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryWaffenzusatz.getId2SelectAfterDelete();
					panelQueryWaffenzusatz.setSelectedId(oNaechster);
				}
				panelSplitWaffenzusatz.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == panelQueryAlergen) {
				int iPos = panelQueryAlergen.getTable().getSelectedRow();
				if (iPos < panelQueryAlergen.getTable().getRowCount() - 1) {
					Integer idPos1 = (Integer) panelQueryAlergen.getSelectedId();
					Integer idPos2 = (Integer) panelQueryAlergen.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getArtikelDelegate().vertauscheAlergen(idPos1, idPos2);
					panelQueryAlergen.setSelectedId(idPos1);
				}
			} else if (e.getSource() == panelQueryLager) {
				int iPos = panelQueryLager.getTable().getSelectedRow();
				if (iPos < panelQueryLager.getTable().getRowCount() - 1) {
					Integer idPos1 = (Integer) panelQueryLager.getSelectedId();
					Integer idPos2 = (Integer) panelQueryLager.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getLagerDelegate().vertauscheLager(idPos1, idPos2);
					panelQueryLager.setSelectedId(idPos1);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == panelQueryAlergen) {
				int iPos = panelQueryAlergen.getTable().getSelectedRow();
				if (iPos > 0) {
					Integer idPos1 = (Integer) panelQueryAlergen.getSelectedId();
					Integer idPos2 = (Integer) panelQueryAlergen.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getArtikelDelegate().vertauscheAlergen(idPos1, idPos2);
					panelQueryAlergen.setSelectedId(idPos1);
				}
			} else if (e.getSource() == panelQueryLager) {
				int iPos = panelQueryLager.getTable().getSelectedRow();
				if (iPos > 0) {
					Integer idPos1 = (Integer) panelQueryLager.getSelectedId();
					Integer idPos2 = (Integer) panelQueryLager.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getLagerDelegate().vertauscheLager(idPos1, idPos2);
					panelQueryLager.setSelectedId(idPos1);
				}
			}
		}

	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getTextRespectUISPr("lp.grunddaten"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		getInternalFrame().setLpTitle(3, "");

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_ARTIKELKLASSEN) {
			createArtikelklassen();
			panelSplitArtikelklassen.eventYouAreSelected(false);
			panelQueryArtikelklassen.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_ARTIKELGRUPPEN) {
			createArtikelgruppen();
			panelSplitArtikelgruppen.eventYouAreSelected(false);
			panelQueryArtikelgruppen.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_HERSTELLER) {
			createHersteller();
			panelSplitHersteller.eventYouAreSelected(false);
			panelQueryHersteller.updateButtons();
		} else if (selectedIndex == IDX_PANEL_LAGER) {
			createLager();
			panelSplitLager.eventYouAreSelected(false);
			panelQueryLager.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ARTIKELKOMMENTARART) {
			createArtikelkommentarart();
			panelSplitArtikelkommentarart.eventYouAreSelected(false);
			panelQueryArtikelkommentarart.updateButtons();
		} else if (selectedIndex == IDX_PANEL_FARBCODE) {
			createFarbcode();
			panelSplitFarbcode.eventYouAreSelected(false);
			panelQueryFarbcode.updateButtons();
		} else if (selectedIndex == IDX_PANEL_SPERREN) {
			createSperren();
			panelSplitSperren.eventYouAreSelected(false);
			panelQuerySperren.updateButtons();
		} else if (selectedIndex == IDX_PANEL_LAGERPLATZ) {
			createLagerplatz();
			panelSplitLagerplatz.eventYouAreSelected(false);
			panelQueryLagerplatz.updateButtons();
		} else if (selectedIndex == IDX_PANEL_VERLEIH) {
			createVerleih();
			panelSplitVerleih.eventYouAreSelected(false);
			panelQueryVerleih.updateButtons();
		} else if (selectedIndex == IDX_PANEL_VORSCHLAGSTEXT) {
			createVorschlagstext();
			panelSplitVorschlagstext.eventYouAreSelected(false);
			panelQueryVorschlagstext.updateButtons();
		} else if (selectedIndex == IDX_PANEL_WEBSHOP) {
			createWebshop();
			panelSplitWebshop.eventYouAreSelected(false);
			panelQueryWebshop.updateButtons();
		} else if (selectedIndex == IDX_PANEL_REACH) {
			createReach();
			panelSplitReach.eventYouAreSelected(false);
			panelQueryReach.updateButtons();
		}  else if (selectedIndex == IDX_PANEL_LASEROBERFLAECHE) {
			createLaseroberflaeche();
			panelSplitLaseroberflaeche.eventYouAreSelected(false);
			panelQueryLaseroberflaeche.updateButtons();
		}else if (selectedIndex == IDX_PANEL_ROHS) {
			createRohs();
			panelSplitRohs.eventYouAreSelected(false);
			panelQueryRohs.updateButtons();
		} else if (selectedIndex == IDX_PANEL_AUTOMOTIVE) {
			createAutomotive();
			panelSplitAutomotive.eventYouAreSelected(false);
			panelQueryAutomotive.updateButtons();
		} else if (selectedIndex == IDX_PANEL_MEDICAL) {
			createMedical();
			panelSplitMedical.eventYouAreSelected(false);
			panelQueryMedical.updateButtons();
		} else if (selectedIndex == IDX_PANEL_VORZUG) {
			createVorzug();
			panelSplitVorzug.eventYouAreSelected(false);
			panelQueryVorzug.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ALERGEN) {
			createAlergen();
			panelSplitAlergen.eventYouAreSelected(false);
			panelQueryAlergen.updateButtons();
		} else if (selectedIndex == IDX_PANEL_GEBINDE) {
			createGebinde();
			panelSplitGebinde.eventYouAreSelected(false);
			panelQueryGebinde.updateButtons();
		} else if (selectedIndex == IDX_PANEL_VERPACKUNGSMITTEL) {
			createVerpackungsmittel();
			panelSplitVerpackungsmittel.eventYouAreSelected(false);
			panelQueryVerpackungsmittel.updateButtons();
		} else if (selectedIndex == IDX_PANEL_DATEIVERWEIS) {
			createDateiverweis();
			panelSplitDateiverweis.eventYouAreSelected(false);
			panelQueryDateiverweis.updateButtons();
		} else if (selectedIndex == IDX_PANEL_WAFFENKALIBER) {
			createWaffenkaliber();
			panelSplitWaffenkaliber.eventYouAreSelected(false);
			panelQueryWaffenkaliber.updateButtons();
		} else if (selectedIndex == IDX_PANEL_WAFFENTYP) {
			createWaffentyp();
			panelSplitWaffentyp.eventYouAreSelected(false);
			panelQueryWaffentyp.updateButtons();
		} else if (selectedIndex == IDX_PANEL_WAFFENTYPFEIN) {
			createWaffentypFein();
			panelSplitWaffentypFein.eventYouAreSelected(false);
			panelQueryWaffentypFein.updateButtons();
		} else if (selectedIndex == IDX_PANEL_WAFFENAUSFUEHRUNG) {
			createWaffenausfuehrung();
			panelSplitWaffenausfuehrung.eventYouAreSelected(false);
			panelQueryWaffenausfuehrung.updateButtons();
		} else if (selectedIndex == IDX_PANEL_WAFFENZUSATZ) {
			createWaffenzusatz();
			panelSplitWaffenzusatz.eventYouAreSelected(false);
			panelQueryWaffenzusatz.updateButtons();
		} else if (selectedIndex == IDX_PANEL_WAFFENKATEGORIE) {
			createWaffenkategorie();
			panelSplitWaffenkategorie.eventYouAreSelected(false);
			panelQueryWaffenkategorie.updateButtons();
		}

	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(MENUE_ACTION_HERSTELLER_ZUSAMMENFUEHREN)) {
			String add2Title = LPMain.getTextRespectUISPr("hersteller.zusammenfuehren");

			HerstellerDto hstDto = null;
			if (getSelectedIndex() == IDX_PANEL_HERSTELLER && panelQueryHersteller != null
					&& panelQueryHersteller.getSelectedId() != null) {
				hstDto = DelegateFactory.getInstance().getArtikelDelegate()
						.herstellerFindBdPrimaryKey((Integer) panelQueryHersteller.getSelectedId());
			}

			PanelDialogHerstellerZusammenfuehren d = new PanelDialogHerstellerZusammenfuehren(hstDto,
					getInternalFrameArtikel(), add2Title);
			getInternalFrame().showPanelDialog(d);
			d.setVisible(true);

		}

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperManuBar == null) {
			wrapperManuBar = new WrapperMenuBar(this);

			JMenu menuDatei = (JMenu) wrapperManuBar.getComponent(WrapperMenuBar.MENU_MODUL);

			JMenuItem menuItemZusammenfuehren = new JMenuItem(LPMain.getTextRespectUISPr("hersteller.zusammenfuehren"));
			menuItemZusammenfuehren.addActionListener(this);
			menuItemZusammenfuehren.setActionCommand(MENUE_ACTION_HERSTELLER_ZUSAMMENFUEHREN);
			HelperClient.setToolTipTextMitRechtToComponent(menuItemZusammenfuehren, RechteFac.RECHT_WW_ARTIKEL_CUD);
			menuDatei.add(menuItemZusammenfuehren, 0);

		}
		return wrapperManuBar;
	}

}
