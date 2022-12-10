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

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.lp.client.anfrage.ReportAnfragestatistik;
import com.lp.client.angebot.ReportAngebotsstatistik;
import com.lp.client.auftrag.ReportRahmenauftragReservierungsliste;
import com.lp.client.bestellung.ReportWepEtikett2;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.dynamisch.PanelDynamisch;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.filter.HvTaggedCsvFileFilter;
import com.lp.client.frame.filechooser.open.CsvFile;
import com.lp.client.frame.filechooser.open.FileOpenerFactory;
import com.lp.client.frame.filechooser.open.WrapperFile;
import com.lp.client.frame.filechooser.open.XlsFile;
import com.lp.client.frame.filechooser.open.XlsFileOpener;
import com.lp.client.partner.DialogLagerminsollImportXLS;
import com.lp.client.partner.PanelKundesokomengenstaffel;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.artikel.fastlanereader.generated.service.WwArtikellagerPK;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelImportDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.LumiQuoteArtikelDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.fertigung.service.VendidataArticleExportResult;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.DokumentenlinkDto;
import com.lp.server.system.service.GeaenderteChargennummernDto;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.csv.LPCSVReader;
import com.lp.util.csv.LPCSVWriter;

@SuppressWarnings("static-access")
/*
 * <p>Ueberschrift: </p> <p>Beschreibung: </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Organisation: </p>
 * 
 * @author Christian Kollmann
 * 
 * @version $Revision: 1.70 $
 */
public class TabbedPaneArtikel extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryArtikel = null;
	private PanelQuery panelQueryArtikellieferant = null;
	private PanelQuery panelQueryStaffelpreise = null;

	private PanelQuery panelQueryArtikellager = null;
	private PanelBasis panelSplitArtikellager = null;
	private PanelBasis panelDetailArtikellager = null;

	private PanelQuery panelQuerySoko = null;
	private PanelBasis panelSplitSoko = null;
	private PanelBasis panelDetailSoko = null;

	private PanelQuery panelQuerySokomengenstaffel = null;
	private PanelBasis panelSplitSokomengenstaffel = null;
	private PanelBasis panelDetailSokomengenstaffel = null;

	private PanelQuery panelQueryLagerplatz = null;
	private PanelBasis panelDetailArtikel = null;
	private PanelBasis panelDetailArtikelbestelldaten = null;
	private PanelBasis panelDetailArtikeltechnik = null;
	private PanelBasis panelDetailArtikelsonstiges = null;
	private PanelExterneDokumente panelDetailExterneDokumente = null;
	private PanelBasis panelDetailArtikellieferant = null;
	private PanelBasis panelDetailLagerplatz = null;
	private PanelArtikellieferantstaffelpreise panelDetailStaffelpeise = null;
	private PanelBasis panelSplitArtikellieferant = null;

	private PanelQuery panelQueryKatalog = null;
	private PanelBasis panelDetailKatalog = null;
	private PanelBasis panelSplitKatalog = null;

	private PanelBasis panelSplitStaffelpreise = null;
	private PanelBasis panelSplitLagerplatz = null;
	private PanelBasis panelDetailArtikelpreise = null;

	private PanelBasis panelSplitVkpfStaffelmenge = null;
	private PanelQuery panelQueryVkpfStaffelmenge = null;
	private PanelVkpfStaffelmenge panelDetailVkpfStaffelmenge = null;

	private PanelQuery panelQueryArtikelkommentar = null;
	private PanelBasis panelSplitArtikelkommentar = null;
	private PanelBasis panelDetailArtikelkommentar = null;

	private PanelQuery panelQuerySperren = null;
	private PanelBasis panelDetailSperren = null;
	private PanelBasis panelSplitSperren = null;

	private PanelQuery panelQueryShopgruppe = null;
	private PanelBasis panelDetailShopgruppe = null;
	private PanelBasis panelSplitShopgruppe = null;

	private PanelQuery panelQueryErsatztypen = null;
	private PanelBasis panelDetailErsatztypen = null;
	private PanelBasis panelSplitErsatztypen = null;

	private PanelQuery panelQueryAllergen = null;
	private PanelBasis panelDetailAllergen = null;
	private PanelBasis panelSplitAllergen = null;

	private PanelQuery panelQueryZugehoerige = null;
	private PanelBasis panelDetailZugehoerige = null;
	private PanelBasis panelSplitZugehoerige = null;

	private PanelQuery panelQueryEinkaufsean = null;
	private PanelBasis panelDetailEinkaufsean = null;
	private PanelBasis panelSplitEinkaufsean = null;

	private PanelBasis panelDetailArtikeleigenschaft = null;

	private PanelBasis panelDetailWaffenregister = null;

	private PanelArtikelTrumpf panelDetailTrumpf = null;

	private PanelQueryFLR panelQueryFLRPaternoster = null;

	private PanelQueryFLR panelQueryFLRKommentarart = null;

	private PanelQueryFLR panelQueryFLRLieferant = null;

	private PanelQueryFLR panelQueryFLRArtikelKommentarKopieren = null;

	private static final String ACTION_SPECIAL_PROFIRST_IMPORT = "action_special_profirst_import";
	private static final String ACTION_SPECIAL_WARENZUGAENGE_FUER_PROFIRST = "action_special_warenzugaenge_fuer_profirst";
	private static final String ACTION_SPECIAL_CHARGENNUMMER_ANHAND_WEP_AKTUALISIEREN = "action_special_chargennummern_anhand_wep_aktualisieren";

	private final String MENUE_ACTION_STATISTIK = "MENUE_ACTION_STATISTIK";
	private final String MENUE_ACTION_RESERVIERUNGEN = "MENUE_ACTION_RESERVIERUNGEN";
	private final String MENUE_ACTION_LIEFERANTENPREISVERGLEICH = "MENUE_ACTION_LIEFERANTENPREISVERGLEICH";

	private final String MENUE_ACTION_FEHLMENGEN = "MENUE_ACTION_FEHLMENGEN";
	private final String MENUE_ACTION_FORECASTPOSITIONEN = "MENUE_ACTION_FORECASTPOSITIONEN";
	private final String MENUE_ACTION_VKPREISENTWICKLUNG = "MENUE_ACTION_VKPREISENTWICKLUNG";

	private final String MENUE_ACTION_BESTELLTLISTE = "MENUE_ACTION_BESTELLTLISTE";
	private final String MENUE_ACTION_ANFRAGESTATISTIK = "MENUE_ACTION_ANFRAGESTATISTIK";
	private final String MENUE_ACTION_ANGEBOTSSTATISTIK = "NENUE_ACTION_ANGEBOTSSTATISTIK";
	private final String MENUE_ACTION_RAHMENRESERVIERUNGEN = "MENUE_ACTION_RAHMENRESERVIERUNGEN";
	private final String MENUE_ACTION_DETAILBEDARFE = "MENUE_ACTION_DETAILBEDARFE";
	private final String MENUE_ACTION_VERWENDUNGSNACHWEIS = "MENUE_ACTION_VERWENDUNGSNACHWEIS";

	private final String MENUE_ACTION_VK_STAFFELPREISE_ERHOEHEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_ACTION_VK_STAFFELPREISE_ERHOEHEN";
	private final String MENUE_ACTION_EK_STAFFELPREISE_ERHOEHEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_ACTION_EK_STAFFELPREISE_ERHOEHEN";

	private final String MENUE_ACTION_KOMMENTARE_AUS_ANDEREM_ARTIKEL_KOPIEREN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_ACTION_KOMMENTARE_AUS_ANDEREM_ARTIKEL_KOPIEREN";

	private final String MENUE_ACTION_ETIKETT = PanelBasis.LEAVEALONE + "_MENUE_ACTION_ETIKETT";
	private final String MENUE_ACTION_STAMMBLATT = "MENUE_ACTION_STAMMBLATT";
	private final String MENUE_ACTION_LOSSTATUS = "MENUE_ACTION_LOSSTATUS";
	private final String MENUE_ACTION_FREIINFERTIGUNG = "MENUE_ACTION_FREIINFERTIGUNG";
	private final String MENUE_ACTION_BEWEGUNSVORSCHAU = "MENUE_ACTION_BEWEGUNSVORSCHAU";
	private final String MENUE_ACTION_AUFTRAGSSERIENNUMMERN = "MENUE_ACTION_AUFTRAGSSERIENNUMMERN";
	private final String MENUE_ACTION_KUNDENSOKOS = "MENUE_ACTION_KUNDENSOKOS";
	private final String MENUE_ACTION_SNRSTAMMBLATT = "MENUE_ACTION_SNRSTAMMBLATT";

	private final String MENUE_JOURNAL_ACTION_ALERGENE = "MENUE_ACTION_ALERGENE";

	private final String MENUE_JOURNAL_ACTION_LAGERSTANDSLISTE = "MENUE_JOURNAL_ACTION_LAGERSTANDSLISTE";
	private final String MENUE_JOURNAL_ACTION_SERIENNUMMERN = "MENUE_JOURNAL_ACTION_SERIENNUMMERN";
	private final String MENUE_JOURNAL_ACTION_LADENHUETER = "MENUE_JOURNAL_ACTION_LADENHUETER";
	private final String MENUE_JOURNAL_ACTION_HITLISTE = "MENUE_JOURNAL_ACTION_HITLISTE";
	private final String MENUE_JOURNAL_ACTION_MINDESTHALTBARKEIT = "MENUE_JOURNAL_ACTION_MINDESTHALTBARKEIT";
	private final String MENUE_JOURNAL_ACTION_PREISLISTE = "MENUE_JOURNAL_ACTION_PREISLISTE";
	private final String MENUE_JOURNAL_ACTION_MAKEORBUY = "MENUE_JOURNAL_ACTION_MAKEORBUY";
	private final String MENUE_JOURNAL_ACTION_WARENBENEGUNSJOURNAL = "MENUE_JOURNAL_ACTION_WARENBENEGUNSJOURNAL";
	private final String MENUE_JOURNAL_ACTION_REKLAMATIONEN = "MENUE_JOURNAL_ACTION_REKLAMATIONEN";
	private final String MENUE_JOURNAL_ACTION_PROJEKTE = "MENUE_JOURNAL_ACTION_PROJEKTE";
	private final String MENUE_JOURNAL_ACTION_GESTPREISUEBERMINVK = "MENUE_JOURNAL_ACTION_GESTPREISUEBERMINVK";
	private final String MENUE_JOURNAL_ACTION_WARENENTNAHMESTATISTIK = "MENUE_JOURNAL_ACTION_WARENENTNAHMESTATISTIK";
	private final String MENUE_JOURNAL_ACTION_MINDESTLAGERSTAENDE = "MENUE_JOURNAL_ACTION_MINDESTLAGERSTAENDE";
	private final String MENUE_JOURNAL_ACTION_ARTIKELGRUPPEN = "MENUE_JOURNAL_ACTION_ARTIKELGRUPPEN";
	private final String MENUE_JOURNAL_ACTION_MATERIALBEDARFSVORSCHAU = "MENUE_JOURNAL_ACTION_MATERIALBEDARFSVORSCHAU";
	private final String MENUE_JOURNAL_ACTION_SHOPGRUPPEN = "MENUE_JOURNAL_ACTION_SHOPGRUPPEN";
	private final String MENUE_JOURNAL_ACTION_NAECHSTE_WARTUNGEN = "MENUE_JOURNAL_ACTION_NAECHSTE_WARTUNGEN";
	private final String MENUE_JOURNAL_ACTION_INDIREKTE_WARENEINSATZSTATISTIK = "MENUE_JOURNAL_ACTION_INDIREKTE_WARENEINSATZSTATISTIK";
	private final String MENUE_JOURNAL_ACTION_ARTIKEL_OHNE_STKL_VERWENDUNG = "MENUE_JOURNAL_ACTION_ARTIKEL_OHNE_STKL_VERWENDUNG";
	private final String MENUE_JOURNAL_ACTION_AUFTRAGSWERTE = "MENUE_JOURNAL_ACTION_AUFTRAGSWERTE";
	private final String MENUE_JOURNAL_ACTION_MEHRERE_ETIKETTEN = "MENUE_JOURNAL_MEHRERE_ETIKETTEN";
	private final String MENUE_JOURNAL_ACTION_BEWEGUNSVORSCHAU = "JOURNAL_ACTION_BEWEGUNSVORSCHAU";
	private final String MENUE_JOURNAL_ACTION_KUNDESONDERKONDITIONEN = "MENUE_JOURNAL_ACTION_KUNDESONDERKONDITIONEN";

	private final String MENUE_ACTION_RAHMENBESTELLTLISTE = "MENUE_JOURNAL_ACTION_RAHMENBESTELLTLISTE";
	private final String MENUE_ACTION_AENDERUNGEN = "MENUE_ACTION_AENDERUNGEN";

	private final String MENUE_ACTION_CSVIMPORT = "MENUE_ACTION_CSVIMPORT";
	private final String MENUE_ACTION_XLSIMPORT = "MENUE_ACTION_XLSIMPORT";
	private final String MENUE_ACTION_ZUSAMMENFUEHREN = "ACTION_ZUSAMMENFUEHREN";
	private final String MENUE_ACTION_LAGERSOLL_MIN_XLSIMPORT = "MENUE_ACTION_LAGERSOLL_MIN_XLSIMPORT";
	private final String MENUE_ACTION_PREISPFLEGE_EXPORT = "MENUE_ACTION_PREISPFLEGE_EXPORT";
	private final String MENUE_ACTION_PREISPFLEGE_IMPORT = "MENUE_ACTION_PREISPFLEGE_IMPORT";
	private final String MENUE_ACTION_WBZ_AKTUALISIEREN = "MENUE_ACTION_WBZ_AKTUALISIEREN";
	private final String MENUE_ACTION_LUMIQUOTE_CSV_EXPORT = "LUMIQUOTE_CSV_EXPORT";

	private final String MENUE_JOURNAL_ACTION_OFFENERAHMENBEDARFE = "MENUE_JOURNAL_ACTION_OFFENERAHMENBEDARFE";

	private final String MENUE_PFLEGE_VKPREISE = "MENUE_PFLEGE_VKPREISE";
	private final String MENUE_PFLEGE_PATERNOSTER = "MENUE_PFLEGE_PATERNOSTER";
	private final String MENUE_PFLEGE_AENDERE_SNRCHNR = "MENUE_PFLEGE_AENDERE_SNRCHNR";
	private final String MENUE_PFLEGE_ALLERGENE = "MENUE_PFLEGE_ALLERGENE";
	private final String MENUE_PFLEGE_4VENDING_ARTIKEL_EXPORT = "MENUE_PFLEGE_4VENDING_ARTIKEL_EXPORT";
	private final String MENUE_PFLEGE_VKSTAFFEL_XLS_IMPORT = "MENUE_PFLEGE_VKSTAFFEL_XLS_IMPORT";
	private final String MENUE_PFLEGE_EIGENSCHAFTEN_XLS_IMPORT = "MENUE_PFLEGE_EIGENSCHAFTEN_XLS_IMPORT";

	public boolean bArtikelfreigabe = false;

	private final String EXTRA_NEU_AUS_ARTIKEL = "neu_aus_artikel";
	private final String EXTRA_LAGERORT_BEARBEITEN = "EXTRA_LAGERORT_BEARBEITEN";
	private final String EXTRA_KOMMENTAR_SUCHEN = "kommentar_suchen";

	public static int IDX_PANEL_AUSWAHL = -1;
	public static int IDX_PANEL_DETAIL = -1;
	public static int IDX_PANEL_PREISE = -1;
	public static int IDX_PANEL_VKPFSTAFFELMENGE = -1;
	public static int IDX_PANEL_LIEFERANT = -1;
	public int IDX_PANEL_EK_STAFFELPREISE = -1;
	public int IDX_PANEL_LAGER = -1;
	public int IDX_PANEL_LAGERPLATZ = -1;
	public int IDX_PANEL_TECHNIK = -1;
	public int IDX_PANEL_BESTELLDATEN = -1;
	public int IDX_PANEL_SONSTIGES = -1;
	public int IDX_PANEL_ARTIKELKOMMENTAR = -1;
	public int IDX_PANEL_KATALOG = -1;
	public int IDX_PANEL_SPERREN = -1;
	public int IDX_PANEL_SHOPGRUPPE = -1;
	public int IDX_PANEL_ZUGEHOERIGE = -1;
	public int IDX_PANEL_ARTIKELEIGENSCHAFTEN = -1;
	public int IDX_PANEL_EINKAUFSEAN = -1;
	public int IDX_PANEL_EXTERNEDOKUMENTE = -1;
	public int IDX_PANEL_TRUMPF = -1;
	public int IDX_PANEL_SOKO = -1;
	public int IDX_PANEL_SOKO_MENGENSTAFFEL = -1;
	public int IDX_PANEL_ARTIKELALERGEN = -1;
	public int IDX_PANEL_ERSATZTYPEN = -1;
	public int IDX_PANEL_WAFFENREGISTER = -1;
	// usemenubar 2: Variable deklarieren, wegen lazy loading
	private WrapperMenuBar wrapperManuBar = null;
	boolean bKurzbezeichnungStattVerpackungsart = false;
	boolean bReferenznummerInPositonen = false;
	boolean bUrsprungslandIstPflichtfeld = false;

	public String sRechtModulweit = null;

	private IVendidataExportController<VendidataArticleExportResult> artikelexport4vendingController;

	public PanelQuery getPanelQueryArtikel() {
		return panelQueryArtikel;
	}

	public PanelQuery getPanelQuerySoko() {
		return panelQuerySoko;
	}

	public PanelQuery getPanelQuerySokomengenstaffel() {
		return panelQuerySokomengenstaffel;
	}

	public TabbedPaneArtikel(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr("lp.artikel"));
		jbInit();

		sRechtModulweit = internalFrameI.getRechtModulweit();

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getInstance().getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
				ParameterFac.PARAMETER_STATUSZEILE_KBEZ_STATT_VERPACKUNGSART);
		bKurzbezeichnungStattVerpackungsart = (java.lang.Boolean) parameter.getCWertAsObject();

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_URSPRUNGSLAND_IST_PFLICHTFELD, ParameterFac.KATEGORIE_ARTIKEL,
				LPMain.getTheClient().getMandant());
		bUrsprungslandIstPflichtfeld = (Boolean) parameter.getCWertAsObject();

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_REFERENZNUMMER_IN_POSITIONEN, ParameterFac.KATEGORIE_ALLGEMEIN,
				LPMain.getTheClient().getMandant());
		bReferenznummerInPositonen = (Boolean) parameter.getCWertAsObject();

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ARTIKELFREIGABE)) {
			bArtikelfreigabe = true;
		}

		initComponents();
	}

	public boolean isUrsprungslandIstPflichtfeld() {
		return bUrsprungslandIstPflichtfeld;
	}

	private void createDetail(Integer key) throws Throwable {
		if (panelDetailArtikel == null) {
			panelDetailArtikel = new PanelArtikel(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.detail"), key);
			setComponentAt(IDX_PANEL_DETAIL, panelDetailArtikel);
		}
	}

	private void createPreise(Integer key) throws Throwable {
		if (panelDetailArtikelpreise == null) {
			panelDetailArtikelpreise = new PanelVkpfPreise(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.preise"), key);
			setComponentAt(IDX_PANEL_PREISE, panelDetailArtikelpreise);
		}
	}

	private void createBestelldaten(Integer key) throws Throwable {
		if (panelDetailArtikelbestelldaten == null) {
			panelDetailArtikelbestelldaten = new PanelArtikelbestelldaten(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.bestelldaten"), key);
			setComponentAt(IDX_PANEL_BESTELLDATEN, panelDetailArtikelbestelldaten);
		}
	}

	private void createWaffenregister(Integer key) throws Throwable {
		if (panelDetailWaffenregister == null) {
			panelDetailWaffenregister = new PanelArtikelWaffenregister(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.waffenregister"), key);
			setComponentAt(IDX_PANEL_WAFFENREGISTER, panelDetailWaffenregister);
		}
	}

	private void createSonstiges(Integer key) throws Throwable {
		if (panelDetailArtikelsonstiges == null) {
			panelDetailArtikelsonstiges = new PanelArtikelsonstiges(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.sonstiges"), key);
			setComponentAt(IDX_PANEL_SONSTIGES, panelDetailArtikelsonstiges);
		}
	}

	private void createExterneDokumente(Integer key) throws Throwable {

		panelDetailExterneDokumente = new PanelExterneDokumente(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("lp.externedokumente"), key);
		setComponentAt(IDX_PANEL_EXTERNEDOKUMENTE, panelDetailExterneDokumente);

	}

	private void createTrumpf(Integer key) throws Throwable {

		panelDetailTrumpf = new PanelArtikelTrumpf(getInternalFrame(), this,
				LPMain.getInstance().getTextRespectUISPr("artikel.trumpf"), key);
		setComponentAt(IDX_PANEL_TRUMPF, panelDetailTrumpf);

	}

	private void createTechnik(Integer key) throws Throwable {
		if (panelDetailArtikeltechnik == null) {
			panelDetailArtikeltechnik = new PanelArtikeltechnik(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.technik"), key);
			setComponentAt(IDX_PANEL_TECHNIK, panelDetailArtikeltechnik);
		}
	}

	private void createEigenschaften(Integer key) throws Throwable {
		if (!((Integer) panelQueryArtikel.getSelectedId()).equals(key)) {
			System.out.println("catched different keys");
		}

		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD, };

		panelDetailArtikeleigenschaft = new PanelDynamisch(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("lp.eigenschaften"), panelQueryArtikel,
				PanelFac.PANEL_ARTIKELEIGENSCHAFTEN, HelperClient.LOCKME_ARTIKEL, aWhichButtonIUse);
		setComponentAt(IDX_PANEL_ARTIKELEIGENSCHAFTEN, panelDetailArtikeleigenschaft);

	}

	private void createAuswahl() throws Throwable {
		if (panelQueryArtikel == null) {
			// Artikelauswahlliste
			String[] aWhichButtonIUse = null;

			QueryType[] qtAuswahl = ArtikelFilterFactory.getInstance().createQTArtikelauswahl();

			boolean bBenutzerIstInMandantensprechAngemeldet = false;
			if (LPMain.getInstance().getTheClient().getLocMandantAsString()
					.equals(LPMain.getInstance().getTheClient().getLocUiAsString())) {
				bBenutzerIstInMandantensprechAngemeldet = true;
			}

			if (bBenutzerIstInMandantensprechAngemeldet) {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER };
			} else {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_FILTER };
			}

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_SI_WERT)) {

				SortierKriterium krit = new SortierKriterium("aspr.c_siwert", true, "ASC");

				panelQueryArtikel = new PanelQuery(qtAuswahl, ArtikelFilterFactory.getInstance().createFKArtikelliste(),
						QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse, getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), true,
						ArtikelFilterFactory.getInstance().createFKVArtikel(), null, krit,
						LPMain.getTextRespectUISPr("artikel.auswahl.sortbysiwert"));

			} else {
				panelQueryArtikel = new PanelQuery(qtAuswahl, ArtikelFilterFactory.getInstance().createFKArtikelliste(),
						QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse, getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), true,
						ArtikelFilterFactory.getInstance().createFKVArtikel(), null, null, null);
			}

			panelQueryArtikel.createAndSaveAndShowButton("/com/lp/client/res/text_find.png",
					LPMain.getInstance().getTextRespectUISPr("artikel.kommentarsuche"),
					PanelBasis.ACTION_MY_OWN_NEW + EXTRA_KOMMENTAR_SUCHEN, null);

			if (bBenutzerIstInMandantensprechAngemeldet) {
				// Hier den zusaetzlichen Button aufs Panel bringen
				panelQueryArtikel.createAndSaveAndShowButton("/com/lp/client/res/nut_and_bolt16x16.png",
						LPMain.getInstance().getTextRespectUISPr("artikel.artikel_kopieren"),
						PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_ARTIKEL, RechteFac.RECHT_WW_ARTIKEL_CUD);
			}

			// PJ18579
			panelQueryArtikel.createAndSaveAndShowButton("/com/lp/client/res/printer216x16.png",
					LPMain.getInstance().getTextRespectUISPr("artikel.report.etikett.shortcut"), MENUE_ACTION_ETIKETT,
					KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), null);
			// SP5386
			panelQueryArtikel.enableToolsPanelButtons(true, MENUE_ACTION_ETIKETT);

			ArbeitsplatzparameterDto aparameter = DelegateFactory.getInstance().getParameterDelegate()
					.holeArbeitsplatzparameter(ParameterFac.ARBEITSPLATZPARAMETER_LAGERPLATZ_DIREKT_ERFASSEN);

			if (aparameter != null) {

				panelQueryArtikel.createAndSaveAndShowButton("/com/lp/client/res/table_sql.png",
						LPMain.getInstance().getTextRespectUISPr("artikel.lagerortbearbeiten") + " F12",
						PanelBasis.ACTION_MY_OWN_NEW + EXTRA_LAGERORT_BEARBEITEN,
						KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0), null);

			}

			panelQueryArtikel.befuellePanelFilterkriterienDirekt(
					ArtikelFilterFactory.getInstance().createFKDArtikelnummer(getInternalFrame()),
					ArtikelFilterFactory.getInstance().createFKDVolltextsuche());

			panelQueryArtikel.addDirektFilter(ArtikelFilterFactory.getInstance().createFKDLieferantennrBezeichnung());

			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getInstance().getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DIREKTFILTER_GRUPPE_KLASSE_STATT_REFERENZNUMMER);
			boolean bDirektfilterAGAKStattReferenznummer = (java.lang.Boolean) parameter.getCWertAsObject();

			if (bDirektfilterAGAKStattReferenznummer) {
				panelQueryArtikel.addDirektFilter(ArtikelFilterFactory.getInstance().createFKDAKAG());
			} else {
				panelQueryArtikel.addDirektFilter(ArtikelFilterFactory.getInstance().createFKDReferenznr());
			}

			panelQueryArtikel.setFilterComboBox(DelegateFactory.getInstance().getArtikelDelegate().getAllSprArtgru(),
					new FilterKriterium("ag.i_id", true, "" + "", FilterKriterium.OPERATOR_IN, false),
					DelegateFactory.getInstance().getArtikelDelegate().sindArtikelgruppenEingeschraenkt(),
					LPMain.getTextRespectUISPr("lp.alle"), false);

			panelQueryArtikel.addStatusBar();

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryArtikel);

		}
	}

	private void createArtikellieferant(Integer key) throws Throwable {

		if (panelQueryArtikellieferant == null) {
			String[] aWhichButtonIUse = null;

			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF)) {

				aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
						PanelBasis.ACTION_POSITION_VONNNACHNPLUS1, PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
						PanelBasis.ACTION_PREVIOUS, PanelBasis.ACTION_NEXT };

			} else {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_PREVIOUS, PanelBasis.ACTION_NEXT };
			}

			QueryType[] querytypes = null;
			FilterKriterium[] filters = ArtikelFilterFactory.getInstance().createFKArtikellieferant(key);

			panelQueryArtikellieferant = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_ARTIKELLIEFERANT,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.artikellieferant"), true);

			panelDetailArtikellieferant = new PanelArtikellieferant(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.artikellieferant"), null);

			panelSplitArtikellieferant = new PanelSplit(getInternalFrame(), panelDetailArtikellieferant,
					panelQueryArtikellieferant, 160);
			setComponentAt(IDX_PANEL_LIEFERANT, panelSplitArtikellieferant);
		} else {
			// filter refreshen.
			panelQueryArtikellieferant
					.setDefaultFilter(ArtikelFilterFactory.getInstance().createFKArtikellieferant(key));
		}
	}

	private void createSoko(Integer key) throws Throwable {

		if (panelQuerySoko == null) {
			String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW, PanelBasis.ACTION_PREVIOUS,
					PanelBasis.ACTION_NEXT };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = ArtikelFilterFactory.getInstance().createFKArtikelsoko(key);

			panelQuerySoko = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_ARTIKELSOKO, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("part.kundesoko"), true,
					new FilterKriterium("flrkunde." + KundeFac.FLR_PARTNER + "." + PartnerFac.FLR_PARTNER_VERSTECKT,
							true, "(1)", // wenn
											// das
											// Kriterium
											// verwendet
											// wird,
											// sollen
											// die
											// versteckten
											// nicht
											// mitangezeigt
											// werden
							FilterKriterium.OPERATOR_NOT_IN, false),
					null);

			panelQuerySoko.addDirektFilter(
					new FilterKriteriumDirekt("flrkunde." + KundeFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
							FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("lp.firma"),
							FilterKriteriumDirekt.PROZENT_BOTH, true, true, Facade.MAX_UNBESCHRAENKT));

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {

				FilterKriterium[] kriterien = new FilterKriterium[1];

				kriterien[0] = new FilterKriterium("flrkunde.mandant_c_nr", true,
						"'" + LPMain.getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL, false);

				panelQuerySoko.befuelleFilterkriteriumSchnellansicht(kriterien);
				panelQuerySoko.getCbSchnellansicht().setText(LPMain.getTextRespectUISPr("artikel.soko.schnellansicht"));

			}

			panelDetailSoko = new PanelArtikelSoko(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("part.kundesoko"), null);

			panelSplitSoko = new PanelSplit(getInternalFrame(), panelDetailSoko, panelQuerySoko, 140);
			setComponentAt(IDX_PANEL_SOKO, panelSplitSoko);
		} else {
			// filter refreshen.
			panelQuerySoko.setDefaultFilter(ArtikelFilterFactory.getInstance().createFKArtikelsoko(key));
		}
	}

	private void createSokomengenstaffel(Integer kundesokoIId) throws Throwable {

		if (panelQuerySokomengenstaffel == null) {
			String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW, PanelBasis.ACTION_PREVIOUS,
					PanelBasis.ACTION_NEXT };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = ArtikelFilterFactory.getInstance()
					.createFKArtikelsokomengenstaffel(kundesokoIId);

			panelQuerySokomengenstaffel = new PanelQuery(querytypes, filters,
					QueryParameters.UC_ID_ARTIKELSOKOMENGENSTAFFEL, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("part.kundesoko"), true);

			panelDetailSokomengenstaffel = new PanelKundesokomengenstaffel(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("part.kundesoko"), null);

			panelSplitSokomengenstaffel = new PanelSplit(getInternalFrame(), panelDetailSokomengenstaffel,
					panelQuerySokomengenstaffel, 140);
			setComponentAt(IDX_PANEL_SOKO_MENGENSTAFFEL, panelSplitSokomengenstaffel);
		} else {
			// filter refreshen.
			panelQuerySokomengenstaffel.setDefaultFilter(
					ArtikelFilterFactory.getInstance().createFKArtikelsokomengenstaffel(kundesokoIId));
		}
	}

	private void createVkpfStaffelmenge(Integer key) throws Throwable {
		if (panelQueryVkpfStaffelmenge == null) {

			String[] aWhichButtonIUse = null;
			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_AENDERN_VERKAUF)) {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW };

			} else {
				aWhichButtonIUse = new String[] {};
			}

			QueryType[] querytypes = null;
			FilterKriterium[] filters = ArtikelFilterFactory.getInstance().createFKVkpfStaffelmenge(key);

			panelQueryVkpfStaffelmenge = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_VKPFSTAFFELMENGE,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.vkpfstaffelmenge"), true);

			panelDetailVkpfStaffelmenge = new PanelVkpfStaffelmenge(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.vkpfstaffelmenge"), null);

			panelSplitVkpfStaffelmenge = new PanelSplit(getInternalFrame(), panelDetailVkpfStaffelmenge,
					panelQueryVkpfStaffelmenge, 180);

			panelQueryVkpfStaffelmenge.createAndSaveAndShowButton("/com/lp/client/res/percent.png",
					LPMain.getInstance().getTextRespectUISPr("artikel.staffelpreise.erhoehen"),
					MENUE_ACTION_VK_STAFFELPREISE_ERHOEHEN, RechteFac.RECHT_LP_DARF_PREISE_AENDERN_VERKAUF);

			setComponentAt(IDX_PANEL_VKPFSTAFFELMENGE, panelSplitVkpfStaffelmenge);
		} else {
			// filter refreshen.
			panelQueryVkpfStaffelmenge
					.setDefaultFilter(ArtikelFilterFactory.getInstance().createFKVkpfStaffelmenge(key));
		}
	}

	private void createArtikelkommentar(Integer key) throws Throwable {

		if (panelQueryArtikelkommentar == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = ArtikelFilterFactory.getInstance().createFKArtikelkommentar(key);

			panelQueryArtikelkommentar = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_ARTIKELKOMMENTAR,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("lp.kommentar"),
					true);

			panelDetailArtikelkommentar = new PanelArtikelkommentar(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.kommentar"), null);

			panelSplitArtikelkommentar = new PanelSplit(getInternalFrame(), panelDetailArtikelkommentar,
					panelQueryArtikelkommentar, 180);

			panelQueryArtikelkommentar.createAndSaveAndShowButton("/com/lp/client/res/goto.png",
					LPMain.getInstance().getTextRespectUISPr("artikel.kommentare.kopieren"),
					MENUE_ACTION_KOMMENTARE_AUS_ANDEREM_ARTIKEL_KOPIEREN, RechteFac.RECHT_WW_ARTIKEL_CUD);

			setComponentAt(IDX_PANEL_ARTIKELKOMMENTAR, panelSplitArtikelkommentar);
		} else {
			// filter refreshen.
			panelQueryArtikelkommentar
					.setDefaultFilter(ArtikelFilterFactory.getInstance().createFKArtikelkommentar(key));
		}
	}

	private void createStaffelpreise(Integer key) throws Throwable {
		if (panelQueryStaffelpreise == null) {
			String[] aWhichStandardButtonIUse = null;

			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF)) {

				aWhichStandardButtonIUse = new String[] { PanelBasis.ACTION_NEW,
						PanelBasis.ACTION_POSITION_VONNNACHNMINUS1, PanelBasis.ACTION_POSITION_VONNNACHNPLUS1,
						PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN };

			} else {
				aWhichStandardButtonIUse = new String[] {};
			}

			panelQueryStaffelpreise = new PanelQuery(null,
					ArtikelFilterFactory.getInstance().createFKArtikellieferantstaffel(key),
					QueryParameters.UC_ID_ARTIKELLIEFERANTSTAFFEL, aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.staffelpreise"), true);

			panelDetailStaffelpeise = new PanelArtikellieferantstaffelpreise(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.staffelpreise"), null);
			if (panelQueryArtikellieferant == null) {
				createArtikellieferant(key);
				panelQueryArtikellieferant.eventYouAreSelected(false);
			}

			if (panelQueryArtikellieferant.getSelectedId() != null) {
				panelDetailStaffelpeise.artikellieferantDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikellieferantFindByPrimaryKey((Integer) panelQueryArtikellieferant.getSelectedId());
			} else {
				panelDetailStaffelpeise.artikellieferantDto = null;
			}

			panelQueryStaffelpreise.createAndSaveAndShowButton("/com/lp/client/res/percent.png",
					LPMain.getInstance().getTextRespectUISPr("artikel.staffelpreise.erhoehen"),
					MENUE_ACTION_EK_STAFFELPREISE_ERHOEHEN, RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF);

			panelSplitStaffelpreise = new PanelSplit(getInternalFrame(), panelDetailStaffelpeise,
					panelQueryStaffelpreise, 160);

			if (getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
				setComponentAt(IDX_PANEL_EK_STAFFELPREISE, panelSplitStaffelpreise);
			}
		} else {
			// filter refreshen.
			if (key != null) {
				panelQueryStaffelpreise
						.setDefaultFilter(ArtikelFilterFactory.getInstance().createFKArtikellieferantstaffel(key));
			} else {
				panelQueryStaffelpreise
						.setDefaultFilter(ArtikelFilterFactory.getInstance().createFKArtikellieferantstaffelWennNULL());
			}

		}
	}

	private void createLagerplatz(Integer key) throws Throwable {

		if (panelQueryLagerplatz == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1, };

			panelQueryLagerplatz = new PanelQuery(null,
					ArtikelFilterFactory.getInstance().createFKArtikellagerplaetze(key),
					QueryParameters.UC_ID_ARTIKELLAGERPLAETZE, aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.lagerplatz"), true);

			panelDetailLagerplatz = new PanelArtikellagerplaetze(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.lagerplatz"), null);

			panelSplitLagerplatz = new PanelSplit(getInternalFrame(), panelDetailLagerplatz, panelQueryLagerplatz, 320);
			setComponentAt(IDX_PANEL_LAGERPLATZ, panelSplitLagerplatz);
		} else {
			// filter refreshen.
			panelQueryLagerplatz.setDefaultFilter(ArtikelFilterFactory.getInstance().createFKArtikellagerplaetze(key));
		}
	}

	private void createKatalog(Integer key) throws Throwable {

		if (panelQueryKatalog == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = ArtikelFilterFactory.getInstance().createFKKatalog(key);

			panelQueryKatalog = new PanelQuery(null, filters, QueryParameters.UC_ID_KATALOG, aWhichStandardButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("lp.katalog"), true);

			panelDetailKatalog = new PanelKatalogseite(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.katalog"), null);

			panelSplitKatalog = new PanelSplit(getInternalFrame(), panelDetailKatalog, panelQueryKatalog, 400);
			setComponentAt(IDX_PANEL_KATALOG, panelSplitKatalog);
		} else {
			// filter refreshen.
			panelQueryKatalog.setDefaultFilter(ArtikelFilterFactory.getInstance().createFKKatalog(key));
		}
	}

	private void createEinkaufsean(Integer key) throws Throwable {

		if (panelQueryEinkaufsean == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = ArtikelFilterFactory.getInstance().createFKEinkaufsean(key);

			panelQueryEinkaufsean = new PanelQuery(null, filters, QueryParameters.UC_ID_EINKAUFSEAN,
					aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.einkaufsean"), true);

			panelDetailEinkaufsean = new PanelEinkaufsean(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.einkaufsean"), null);

			panelSplitEinkaufsean = new PanelSplit(getInternalFrame(), panelDetailEinkaufsean, panelQueryEinkaufsean,
					350);
			setComponentAt(IDX_PANEL_EINKAUFSEAN, panelSplitEinkaufsean);
		} else {
			// filter refreshen.
			panelQueryEinkaufsean.setDefaultFilter(ArtikelFilterFactory.getInstance().createFKEinkaufsean(key));
		}
	}

	private void createSperren(Integer key) throws Throwable {

		if (panelQuerySperren == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			FilterKriterium[] filters = ArtikelFilterFactory.getInstance().createFKArtikelsperren(key);

			panelQuerySperren = new PanelQuery(null, filters, QueryParameters.UC_ID_ARTIKELSPERREN,
					aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.sperren"), true);

			panelDetailSperren = new PanelArtikelsperren(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.sperren"), null);

			panelSplitSperren = new PanelSplit(getInternalFrame(), panelDetailSperren, panelQuerySperren, 360);
			setComponentAt(IDX_PANEL_SPERREN, panelSplitSperren);
		} else {
			// filter refreshen.
			panelQuerySperren.setDefaultFilter(ArtikelFilterFactory.getInstance().createFKArtikelsperren(key));
		}
	}

	private void createShopgruppe(Integer key) throws Throwable {

		if (panelQueryShopgruppe == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = ArtikelFilterFactory.getInstance().createFKArtikelshopgruppen(key);

			panelQueryShopgruppe = new PanelQuery(null, filters, QueryParameters.UC_ID_ARTIEKLSHOPGRUPPE,
					aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.tab.shopgruppen"), true);

			panelDetailShopgruppe = new PanelArtikelshopgruppe(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.tab.shopgruppen"), null);

			panelSplitShopgruppe = new PanelSplit(getInternalFrame(), panelDetailShopgruppe, panelQueryShopgruppe, 360);
			setComponentAt(IDX_PANEL_SHOPGRUPPE, panelSplitShopgruppe);
		} else {
			// filter refreshen.
			panelQueryShopgruppe.setDefaultFilter(ArtikelFilterFactory.getInstance().createFKArtikelshopgruppen(key));
		}
	}

	private void createErsatztypen(Integer key) throws Throwable {

		if (panelQueryErsatztypen == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			FilterKriterium[] filters = ArtikelFilterFactory.getInstance().createFKErsatztypen(key);

			panelQueryErsatztypen = new PanelQuery(null, filters, QueryParameters.UC_ID_ERSATZTYPEN,
					aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.tab.ersatztypen"), true);

			panelDetailErsatztypen = new PanelErsatztypen(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.tab.ersatztypen"), null);

			panelSplitErsatztypen = new PanelSplit(getInternalFrame(), panelDetailErsatztypen, panelQueryErsatztypen,
					360);
			setComponentAt(IDX_PANEL_ERSATZTYPEN, panelSplitErsatztypen);
		} else {
			// filter refreshen.
			panelQueryErsatztypen.setDefaultFilter(ArtikelFilterFactory.getInstance().createFKErsatztypen(key));
		}
	}

	private void createAlergen(Integer key) throws Throwable {

		if (panelQueryAllergen == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = ArtikelFilterFactory.getInstance().createFKArtikelalergen(key);

			panelQueryAllergen = new PanelQuery(null, filters, QueryParameters.UC_ID_ARTIKELALERGEN,
					aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.allergen"), true);

			panelDetailAllergen = new PanelArtikelallergen(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("artikel.allergen"), null);

			panelSplitAllergen = new PanelSplit(getInternalFrame(), panelDetailAllergen, panelQueryAllergen, 360);
			setComponentAt(IDX_PANEL_ARTIKELALERGEN, panelSplitAllergen);
		} else {
			// filter refreshen.
			panelQueryAllergen.setDefaultFilter(ArtikelFilterFactory.getInstance().createFKArtikelalergen(key));
		}
	}

	private void createZugehoerige(Integer key) throws Throwable {

		if (panelQueryZugehoerige == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = ArtikelFilterFactory.getInstance().createFKZugehoerige(key);

			panelQueryZugehoerige = new PanelQuery(null, filters, QueryParameters.UC_ID_ZUGEHOERIGE,
					aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.zugehoerige"), true);

			panelDetailZugehoerige = new PanelZugehoerige(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.zugehoerige"), null);

			panelSplitZugehoerige = new PanelSplit(getInternalFrame(), panelDetailZugehoerige, panelQueryZugehoerige,
					380);
			setComponentAt(IDX_PANEL_ZUGEHOERIGE, panelSplitZugehoerige);
		} else {
			// filter refreshen.
			panelQueryZugehoerige.setDefaultFilter(ArtikelFilterFactory.getInstance().createFKZugehoerige(key));
		}
	}

	private void createArtikellager(Integer key) throws Throwable {

		if (panelQueryArtikellager == null) {

			boolean bDarfGestpreiseaendern = false;
			try {
				bDarfGestpreiseaendern = DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_WW_ARTIKEL_GESTPREISE_CU);
			} catch (Throwable ex) {
				handleException(ex, true);
			}
			String[] aWhichButtonIUse = null;
			if (bDarfGestpreiseaendern == true) {
				aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW };
			} else {
				aWhichButtonIUse = new String[] {};
			}

			panelQueryArtikellager = new PanelQuery(null, ArtikelFilterFactory.getInstance().createFKArtikellager(key),
					QueryParameters.UC_ID_ARTIKELLAGER, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("label.lager"), true);

			panelQueryArtikellager.befuellePanelFilterkriterienDirektUndVersteckte(null, null,
					ArtikelFilterFactory.getInstance().createFKVLager());

			panelDetailArtikellager = new PanelArtikellager(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("label.lager"), null);

			panelSplitArtikellager = new PanelSplit(getInternalFrame(), panelDetailArtikellager, panelQueryArtikellager,
					200);
			setComponentAt(IDX_PANEL_LAGER, panelSplitArtikellager);

		} else {
			// filter refreshen.
			panelQueryArtikellager.setDefaultFilter(ArtikelFilterFactory.getInstance().createFKArtikellager(key));
		}
	}

	private void jbInit() throws Throwable {
		IDX_PANEL_AUSWAHL = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.auswahl"));

		IDX_PANEL_DETAIL = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.detail"), null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.detail"));

		// Darf Preise sehen: Tab nicht anzeigen, wenn kein Benutzerrecht:
		// LP_DARF_PREISE_SEHEN_VERKAUF
		if (getInternalFrame().bRechtDarfPreiseSehenVerkauf) {
			IDX_PANEL_PREISE = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.preise"),
					null, null, LPMain.getInstance().getTextRespectUISPr("artikel.title.tooltip.preise"));

		}

		// Darf Preise sehen: Tab nicht anzeigen, wenn kein Benutzerrecht:
		// LP_DARF_PREISE_SEHEN_VERKAUF
		if (getInternalFrame().bRechtDarfPreiseSehenVerkauf) {
			IDX_PANEL_VKPFSTAFFELMENGE = reiterHinzufuegen(
					LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.vkpfstaffelmenge"), null, null,
					LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.vkpfstaffelmenge"));

		}

		IDX_PANEL_LIEFERANT = reiterHinzufuegen(
				LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.artikellieferant"), null, null,
				LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.artikellieferant"));

		// Darf Preise sehen: Tab nicht anzeigen, wenn kein Benutzerrecht:
		// LP_DARF_PREISE_SEHEN_EINKAUF
		if (getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			IDX_PANEL_EK_STAFFELPREISE = reiterHinzufuegen(
					LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.staffelpreise"), null, null,
					LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.staffelpreise"));

		}
		IDX_PANEL_LAGER = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("label.lager"), null, null,
				LPMain.getInstance().getTextRespectUISPr("label.lager"));

		IDX_PANEL_LAGERPLATZ = reiterHinzufuegen(
				LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.lagerplatz"), null, null,
				LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.lagerplatz"));

		IDX_PANEL_TECHNIK = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.technik"), null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.technik"));

		IDX_PANEL_BESTELLDATEN = reiterHinzufuegen(
				LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.bestelldaten"), null, null,
				LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.bestelldaten"));

		IDX_PANEL_SONSTIGES = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.sonstiges"), null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.sonstiges"));

		IDX_PANEL_ARTIKELKOMMENTAR = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.kommentar"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.kommentar"));

		IDX_PANEL_KATALOG = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.katalog"), null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.katalog"));

		IDX_PANEL_SPERREN = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.sperren"), null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.sperren"));

		IDX_PANEL_SHOPGRUPPE = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("artikel.tab.webshopgruppen"),
				null, null, LPMain.getInstance().getTextRespectUISPr(

						"artikel.tab.webshopgruppen"));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ERSATZTYPENVERWALTUNG)) {

			IDX_PANEL_ERSATZTYPEN = reiterHinzufuegen(
					LPMain.getInstance().getTextRespectUISPr("artikel.tab.ersatztypen"), null, null,
					LPMain.getInstance().getTextRespectUISPr("artikel.tab.ersatztypen"));
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZUSAMMENGEHOERIGE_ARTIKEL)) {

			IDX_PANEL_ZUGEHOERIGE = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.zugehoerige"), null,
					null, LPMain.getInstance().getTextRespectUISPr("lp.zugehoerige"));
		}

		// Wenn keine Panelbeschriebung vorhanden, dann ausblenden
		if (DelegateFactory.getInstance().getPanelDelegate()
				.panelbeschreibungVorhanden(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN)) {

			IDX_PANEL_ARTIKELEIGENSCHAFTEN = reiterHinzufuegen(
					LPMain.getInstance().getTextRespectUISPr("lp.eigenschaften"), null, null,
					LPMain.getInstance().getTextRespectUISPr("lp.eigenschaften"));
		}

		if (hatBelegartKueche() || hatZusatzfunktionEinkaufsEAN()) {

			IDX_PANEL_EINKAUFSEAN = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("artikel.einkaufsean"),
					null, null, LPMain.getInstance().getTextRespectUISPr("artikel.einkaufsean"));
		}

		if (hatBelegartKueche()) {

			IDX_PANEL_ARTIKELALERGEN = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("artikel.allergen"),
					null, null, LPMain.getInstance().getTextRespectUISPr("artikel.allergen"));
		}

		DokumentenlinkDto[] dtosDoku = DelegateFactory.getInstance().getMandantDelegate()
				.dokumentenlinkFindByBelegartCNrMandantCNrBPfadabsolut(LocaleFac.BELEGART_ARTIKEL, true);
		if (dtosDoku != null && dtosDoku.length > 0) {

			IDX_PANEL_EXTERNEDOKUMENTE = reiterHinzufuegen(
					LPMain.getInstance().getTextRespectUISPr("lp.externedokumente"), null, null,
					LPMain.getInstance().getTextRespectUISPr("lp.externedokumente"));
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_KUNDESONDERKONDITIONEN)) {
			if (getInternalFrame().bRechtDarfPreiseSehenVerkauf) {

				IDX_PANEL_SOKO = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("part.kundesoko"), null,
						null, LPMain.getInstance().getTextRespectUISPr("part.kundesoko"));
				IDX_PANEL_SOKO_MENGENSTAFFEL = reiterHinzufuegen(
						LPMain.getInstance().getTextRespectUISPr("part.kundesokomengenstaffel"), null, null,
						LPMain.getInstance().getTextRespectUISPr("part.kundesokomengenstaffel"));
			}
		}
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_WAFFENREGISTER)) {

			IDX_PANEL_WAFFENREGISTER = reiterHinzufuegen(
					LPMain.getInstance().getTextRespectUISPr("artikel.waffenregister"), null, null,
					LPMain.getInstance().getTextRespectUISPr("artikel.waffenregister"));
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_TRUTOPS_BOOST)) {
			IDX_PANEL_TRUMPF = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("artikel.trumpf"), null, null,
					LPMain.getInstance().getTextRespectUISPr("artikel.trumpf"));
		}

		// QP1 ist default.
		createAuswahl();

		if ((Integer) panelQueryArtikel.getSelectedId() != null) {
			getInternalFrameArtikel().setArtikelDto(DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey((Integer) panelQueryArtikel.getSelectedId()));
		}
		// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
		// restlichen panel deaktivieren

		if (panelQueryArtikel.getSelectedId() == null) {
			getInternalFrame().enableAllMyKidPanelsExceptMe(0, false);
		}

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryArtikel, ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);
	}

	public InternalFrameArtikel getInternalFrameArtikel() {
		return (InternalFrameArtikel) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryArtikel.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		// AD: System.out.println(e.toString() + ", ID:" + e.getID() + ", " +
		// e.getSource().toString());
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryArtikel) {
				Integer angebotIId = (Integer) panelQueryArtikel.getSelectedId();
				if (angebotIId != null) {
					setSelectedComponent(panelDetailArtikel);
				}

			} else if (e.getSource() == panelQueryFLRKommentarart) {

				Integer artikelIId = (Integer) panelQueryFLRKommentarart.getSelectedId();
				panelQueryArtikel.clearAllFilters();
				panelQueryArtikel.eventYouAreSelected(false);
				panelQueryArtikel.setSelectedId(artikelIId);

			} else if (e.getSource() == panelQueryFLRArtikelKommentarKopieren) {

				Integer artikelIId_Quelle = (Integer) panelQueryFLRArtikelKommentarKopieren.getSelectedId();

				Object[] aOptionen = new Object[2];
				aOptionen[0] = LPMain.getInstance()
						.getTextRespectUISPr("artikel.kommentare.kopieren.option.nurclientlocale");
				aOptionen[1] = LPMain.getInstance()
						.getTextRespectUISPr("artikel.kommentare.kopieren.option.allelocales");

				int iAuswahl = DialogFactory.showModalDialog(getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr("artikel.kommentare.kopieren.title"),
						LPMain.getInstance().getTextRespectUISPr("lp.frage"), aOptionen, aOptionen[0]);

				if (iAuswahl != -1) {
					boolean nurClientlocale = true;
					if (iAuswahl == 1) {
						nurClientlocale = false;
					}

					DelegateFactory.getInstance().getArtikelkommentarDelegate().kopiereArtikelkommentar(
							artikelIId_Quelle, getInternalFrameArtikel().getArtikelDto().getIId(), nurClientlocale);
					panelQueryArtikelkommentar.eventYouAreSelected(false);

				}

			} else if (e.getSource() == panelQueryFLRLieferant) {

				Integer lieferantIId = (Integer) panelQueryFLRLieferant.getSelectedId();

				HvOptional<XlsFile> xlsFile = FileOpenerFactory.allergeneImportOpenXls(this);
				if (!xlsFile.isPresent())
					return;

				byte[] xlsContent = xlsFile.get().getBytes();

				DelegateFactory.getInstance().getArtikelDelegate().importiereAlergeneXLS(xlsContent, lieferantIId);
			} else if (e.getSource() == panelQueryFLRPaternoster) {
				Integer paternosterIId = (Integer) panelQueryFLRPaternoster.getSelectedId();

				DelegateFactory.getInstance().getAutoPaternosterDelegate().paternosterAddArtikelAll(paternosterIId);

			}

		}

		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD || e.getID() == ItemChangedEvent.ACTION_DELETE) {
			if (e.getSource() == panelDetailArtikellieferant) {
				panelSplitArtikellieferant.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailKatalog) {
				panelSplitKatalog.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailSperren) {
				panelSplitSperren.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailZugehoerige) {
				panelSplitZugehoerige.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailStaffelpeise) {
				panelSplitStaffelpreise.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailLagerplatz) {
				panelSplitLagerplatz.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArtikelpreise) {
				panelDetailArtikelpreise.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArtikelkommentar) {
				panelDetailArtikelkommentar.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailVkpfStaffelmenge) {
				panelSplitVkpfStaffelmenge.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArtikellager) {
				panelSplitArtikellager.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailEinkaufsean) {
				panelSplitEinkaufsean.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailShopgruppe) {
				panelSplitShopgruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailAllergen) {
				panelSplitAllergen.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailErsatztypen) {
				panelSplitErsatztypen.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailSoko) {
				panelSplitSoko.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailSokomengenstaffel) {
				panelSplitSokomengenstaffel.eventYouAreSelected(false);
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelDetailArtikellieferant) {
				panelQueryArtikellieferant.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelDetailKatalog) {
				panelQueryKatalog.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelDetailSperren) {
				panelQuerySperren.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelDetailZugehoerige) {
				panelQueryZugehoerige.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelDetailLagerplatz) {
				panelQueryLagerplatz.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelDetailStaffelpeise) {
				panelQueryStaffelpreise.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelDetailArtikelkommentar) {
				panelQueryArtikelkommentar.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelDetailVkpfStaffelmenge) {
				panelQueryVkpfStaffelmenge.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelDetailArtikellager) {
				panelQueryArtikellager.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelDetailEinkaufsean) {
				panelQueryEinkaufsean.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelDetailShopgruppe) {
				panelQueryShopgruppe.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelDetailAllergen) {
				panelQueryAllergen.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelDetailErsatztypen) {
				panelQueryErsatztypen.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelDetailSoko) {
				panelQuerySoko.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelDetailSokomengenstaffel) {
				panelQuerySokomengenstaffel.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelDetailArtikel) {
				panelQueryArtikel.clearDirektFilter();
				Object oKey = panelDetailArtikel.getKeyWhenDetailPanel();

				panelQueryArtikel.refreshMe(oKey);
				// panelQueryArtikel.setSelectedId(oKey);
			}

			if (e.getSource() == panelDetailArtikellieferant) {
				Object oKey = panelDetailArtikellieferant.getKeyWhenDetailPanel();
				panelQueryArtikellieferant.eventYouAreSelected(false);
				panelQueryArtikellieferant.setSelectedId(oKey);
				panelSplitArtikellieferant.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailKatalog) {
				Object oKey = panelDetailKatalog.getKeyWhenDetailPanel();
				panelQueryKatalog.eventYouAreSelected(false);
				panelQueryKatalog.setSelectedId(oKey);
				panelSplitKatalog.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailEinkaufsean) {
				Object oKey = panelDetailEinkaufsean.getKeyWhenDetailPanel();
				panelQueryEinkaufsean.eventYouAreSelected(false);
				panelQueryEinkaufsean.setSelectedId(oKey);
				panelSplitEinkaufsean.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailSperren) {
				Object oKey = panelDetailSperren.getKeyWhenDetailPanel();
				panelQuerySperren.eventYouAreSelected(false);
				panelQuerySperren.setSelectedId(oKey);
				panelSplitSperren.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailShopgruppe) {
				Object oKey = panelDetailShopgruppe.getKeyWhenDetailPanel();
				panelQueryShopgruppe.eventYouAreSelected(false);
				panelQueryShopgruppe.setSelectedId(oKey);
				panelSplitShopgruppe.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailErsatztypen) {
				Object oKey = panelDetailErsatztypen.getKeyWhenDetailPanel();
				panelQueryErsatztypen.eventYouAreSelected(false);
				panelQueryErsatztypen.setSelectedId(oKey);
				panelSplitErsatztypen.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailAllergen) {
				Object oKey = panelDetailAllergen.getKeyWhenDetailPanel();
				panelQueryAllergen.eventYouAreSelected(false);
				panelQueryAllergen.setSelectedId(oKey);
				panelSplitAllergen.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailZugehoerige) {
				Object oKey = panelDetailZugehoerige.getKeyWhenDetailPanel();
				panelQueryZugehoerige.eventYouAreSelected(false);
				panelQueryZugehoerige.setSelectedId(oKey);
				panelSplitZugehoerige.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailStaffelpeise) {
				Object oKey = panelDetailStaffelpeise.getKeyWhenDetailPanel();
				panelQueryStaffelpreise.eventYouAreSelected(false);
				panelQueryStaffelpreise.setSelectedId(oKey);
				panelSplitStaffelpreise.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailLagerplatz) {
				Object oKey = panelDetailLagerplatz.getKeyWhenDetailPanel();
				panelQueryLagerplatz.eventYouAreSelected(false);
				panelQueryLagerplatz.setSelectedId(oKey);
				panelSplitLagerplatz.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailArtikelkommentar) {
				Object oKey = panelDetailArtikelkommentar.getKeyWhenDetailPanel();
				panelQueryArtikelkommentar.eventYouAreSelected(false);
				panelQueryArtikelkommentar.setSelectedId(oKey);
				panelSplitArtikelkommentar.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArtikellager) {
				Object oKey = panelDetailArtikellager.getKeyWhenDetailPanel();
				panelQueryArtikellager.eventYouAreSelected(false);
				panelQueryArtikellager.setSelectedId(oKey);
				panelSplitArtikellager.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailVkpfStaffelmenge) {
				Object oKey = panelDetailVkpfStaffelmenge.getKeyWhenDetailPanel();
				panelQueryVkpfStaffelmenge.eventYouAreSelected(false);
				panelQueryVkpfStaffelmenge.setSelectedId(oKey);
				panelSplitVkpfStaffelmenge.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailSoko) {
				Object oKey = panelDetailSoko.getKeyWhenDetailPanel();
				panelQuerySoko.eventYouAreSelected(false);
				panelQuerySoko.setSelectedId(oKey);
				panelSplitSoko.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailSokomengenstaffel) {
				Object oKey = panelDetailSokomengenstaffel.getKeyWhenDetailPanel();
				panelQuerySokomengenstaffel.eventYouAreSelected(false);
				panelQuerySokomengenstaffel.setSelectedId(oKey);
				panelSplitSokomengenstaffel.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryArtikel) {
				if (panelQueryArtikel.getSelectedId() != null) {
					getInternalFrameArtikel().setKeyWasForLockMe(panelQueryArtikel.getSelectedId() + "");
					createDetail((Integer) panelQueryArtikel.getSelectedId());
					panelDetailArtikel.setKeyWhenDetailPanel(panelQueryArtikel.getSelectedId());

					// Dto-setzen
					getInternalFrameArtikel().setArtikelDto(DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByPrimaryKey((Integer) panelQueryArtikel.getSelectedId()));

					if (bArtikelfreigabe == true && getInternalFrameArtikel().getArtikelDto() != null
							&& getInternalFrameArtikel().getArtikelDto().getTFreigabe() == null) {
						boolean hatRecht = DelegateFactory.getInstance().getTheJudgeDelegate()
								.hatRecht(RechteFac.RECHT_WW_ARTIKEL_CUD);
						if (hatRecht) {
							getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_UPDATE);
						}
					}

					// createArtikellieferant((Integer) panelQueryArtikel
					// .getSelectedId());
					// TODO: ghp, sollte doch eigentlich panelQueryArtikel sein?
					// panelQueryArtikellieferant.eventYouAreSelected(false);
					// panelQueryArtikel.eventYouAreSelected(false);
					panelQueryArtikel.updateLpTitle();

					// SP5502
					if (panelDetailExterneDokumente != null) {
						panelDetailExterneDokumente.setBelegartIId((Integer) panelQueryArtikel.getSelectedId());
					}

					String sBezeichnung = "";
					if (getInternalFrameArtikel().getArtikelDto().getArtikelsprDto() != null) {
						sBezeichnung = getInternalFrameArtikel().getArtikelDto().getArtikelsprDto().getCBez();
					}
					if (sBezeichnung == null) {
						sBezeichnung = "";
					}

					refreshTitle();
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, true);

					if (bKurzbezeichnungStattVerpackungsart) {
						// Statusbar setzen
						if (getInternalFrameArtikel().getArtikelDto() != null
								&& getInternalFrameArtikel().getArtikelDto().getArtikelsprDto() != null
								&& getInternalFrameArtikel().getArtikelDto().getArtikelsprDto().getCKbez() != null) {

							panelQueryArtikel.setStatusbarSpalte4(
									LPMain.getInstance().getTextRespectUISPr("artikel.kurzbez") + ": "
											+ getInternalFrameArtikel().getArtikelDto().getArtikelsprDto().getCKbez());
						} else {
							panelQueryArtikel.setStatusbarSpalte4("");

						}

						if (getInternalFrameArtikel().getArtikelDto() != null
								&& getInternalFrameArtikel().getArtikelDto().getCIndex() != null) {
							panelQueryArtikel
									.setStatusbarSpalte5(LPMain.getInstance().getTextRespectUISPr("artikel.index")
											+ ": " + getInternalFrameArtikel().getArtikelDto().getCIndex(), true);
						} else {
							panelQueryArtikel.setStatusbarSpalte5("", true);

						}
					} else {

						// Statusbar setzen
						Integer materialIId = getInternalFrameArtikel().getArtikelDto().getMaterialIId();
						if (materialIId != null) {
							MaterialDto materialDto = DelegateFactory.getInstance().getMaterialDelegate()
									.materialFindByPrimaryKey(materialIId);

							panelQueryArtikel.setStatusbarSpalte4(
									LPMain.getInstance().getTextRespectUISPr("fert.tab.oben.material.title") + ": "
											+ materialDto.getBezeichnung());
						} else {
							panelQueryArtikel.setStatusbarSpalte4("");

						}

						if (getInternalFrameArtikel().getArtikelDto().getVerpackungDto() != null
								&& getInternalFrameArtikel().getArtikelDto().getVerpackungDto()
										.getCVerpackungsart() != null) {
							panelQueryArtikel.setStatusbarSpalte5(LPMain.getInstance()
									.getTextRespectUISPr("artikel.technik.verpackungsart") + ": "
									+ getInternalFrameArtikel().getArtikelDto().getVerpackungDto().getCVerpackungsart(),
									true);
						} else {
							panelQueryArtikel.setStatusbarSpalte5("", true);

						}

						if (getInternalFrameArtikel().getArtikelDto().getVerpackungDto() != null
								&& getInternalFrameArtikel().getArtikelDto().getVerpackungDto().getCBauform() != null) {
							panelQueryArtikel.setStatusbarSpalte6(LPMain.getInstance()
									.getTextRespectUISPr("artikel.technik.bauform") + ": "
									+ getInternalFrameArtikel().getArtikelDto().getVerpackungDto().getCBauform(), true);
						} else {
							panelQueryArtikel.setStatusbarSpalte6("", true);

						}
					}
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
				}

				panelQueryArtikel.updateButtons();
			} else if (e.getSource() == panelQueryArtikellieferant) {

				Integer iId = (Integer) panelQueryArtikellieferant.getSelectedId();

				createStaffelpreise(iId);
				if (panelQueryArtikellieferant.getSelectedId() != null) {
					panelDetailStaffelpeise.artikellieferantDto = DelegateFactory.getInstance().getArtikelDelegate()
							.artikellieferantFindByPrimaryKey((Integer) panelQueryArtikellieferant.getSelectedId());
				} else {
					panelDetailStaffelpeise.artikellieferantDto = null;
				}
				if (getInternalFrameArtikel().getArtikelDto() != null
						&& getInternalFrameArtikel().getArtikelDto().getIId() != null) {
					panelDetailArtikellieferant.setKeyWhenDetailPanel(iId);
					panelDetailArtikellieferant.eventYouAreSelected(false);
					panelQueryArtikellieferant.updateButtons();

				}
			} else if (e.getSource() == panelQueryKatalog) {

				Integer iId = (Integer) panelQueryKatalog.getSelectedId();
				panelDetailKatalog.setKeyWhenDetailPanel(iId);
				panelDetailKatalog.eventYouAreSelected(false);
				panelQueryKatalog.updateButtons();
			} else if (e.getSource() == panelQueryEinkaufsean) {

				Integer iId = (Integer) panelQueryEinkaufsean.getSelectedId();
				panelDetailEinkaufsean.setKeyWhenDetailPanel(iId);
				panelDetailEinkaufsean.eventYouAreSelected(false);
				panelQueryEinkaufsean.updateButtons();
			} else if (e.getSource() == panelQuerySperren) {

				Integer iId = (Integer) panelQuerySperren.getSelectedId();
				panelDetailSperren.setKeyWhenDetailPanel(iId);
				panelDetailSperren.eventYouAreSelected(false);
				panelQuerySperren.updateButtons();
			} else if (e.getSource() == panelQueryShopgruppe) {

				Integer iId = (Integer) panelQueryShopgruppe.getSelectedId();
				panelDetailShopgruppe.setKeyWhenDetailPanel(iId);
				panelDetailShopgruppe.eventYouAreSelected(false);
				panelQueryShopgruppe.updateButtons();
			} else if (e.getSource() == panelQueryErsatztypen) {

				Integer iId = (Integer) panelQueryErsatztypen.getSelectedId();
				panelDetailErsatztypen.setKeyWhenDetailPanel(iId);
				panelDetailErsatztypen.eventYouAreSelected(false);
				panelQueryErsatztypen.updateButtons();
			} else if (e.getSource() == panelQueryAllergen) {

				Integer iId = (Integer) panelQueryAllergen.getSelectedId();
				panelDetailAllergen.setKeyWhenDetailPanel(iId);
				panelDetailAllergen.eventYouAreSelected(false);
				panelQueryAllergen.updateButtons();
			} else if (e.getSource() == panelQueryZugehoerige) {

				Integer iId = (Integer) panelQueryZugehoerige.getSelectedId();
				panelDetailZugehoerige.setKeyWhenDetailPanel(iId);
				panelDetailZugehoerige.eventYouAreSelected(false);
				panelQueryZugehoerige.updateButtons();
			} else if (e.getSource() == panelQueryArtikellager) {

				WwArtikellagerPK iId = (WwArtikellagerPK) panelQueryArtikellager.getSelectedId();
				panelDetailArtikellager.setKeyWhenDetailPanel(iId);
				panelDetailArtikellager.eventYouAreSelected(false);
				panelQueryArtikellager.updateButtons();

			} else if (e.getSource() == panelQueryStaffelpreise) {

				Integer iId = (Integer) panelQueryStaffelpreise.getSelectedId();
				panelDetailStaffelpeise.setKeyWhenDetailPanel(iId);
				panelDetailStaffelpeise.eventYouAreSelected(false);
				panelQueryStaffelpreise.updateButtons();
			} else if (e.getSource() == panelQueryLagerplatz) {
				Integer iId = (Integer) panelQueryLagerplatz.getSelectedId();
				panelDetailLagerplatz.setKeyWhenDetailPanel(iId);
				panelDetailLagerplatz.eventYouAreSelected(false);
				panelQueryLagerplatz.updateButtons();

			} else if (e.getSource() == panelQueryArtikelkommentar) {
				Integer iId = (Integer) panelQueryArtikelkommentar.getSelectedId();
				panelDetailArtikelkommentar.setKeyWhenDetailPanel(iId);
				panelDetailArtikelkommentar.eventYouAreSelected(false);
				panelQueryArtikelkommentar.updateButtons();
			} else if (e.getSource() == panelQueryVkpfStaffelmenge) {
				Integer iId = (Integer) panelQueryVkpfStaffelmenge.getSelectedId();
				panelDetailVkpfStaffelmenge.setKeyWhenDetailPanel(iId);
				panelDetailVkpfStaffelmenge.eventYouAreSelected(false);
				panelQueryVkpfStaffelmenge.updateButtons();
			} else if (e.getSource() == panelQuerySoko) {
				Integer iId = (Integer) panelQuerySoko.getSelectedId();
				panelDetailSoko.setKeyWhenDetailPanel(iId);
				panelDetailSoko.eventYouAreSelected(false);
				panelQuerySoko.updateButtons();
			} else if (e.getSource() == panelQuerySokomengenstaffel) {
				Integer iId = (Integer) panelQuerySokomengenstaffel.getSelectedId();
				panelDetailSokomengenstaffel.setKeyWhenDetailPanel(iId);
				panelDetailSokomengenstaffel.eventYouAreSelected(false);
				panelQuerySokomengenstaffel.updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailArtikel) {
				this.setSelectedComponent(panelQueryArtikel);
				setKeyWasForLockMe();
				panelQueryArtikel.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArtikellieferant) {
				setKeyWasForLockMe();
				if (panelDetailArtikellieferant.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArtikellieferant.getId2SelectAfterDelete();
					panelQueryArtikellieferant.setSelectedId(oNaechster);
				}
				panelSplitArtikellieferant.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailKatalog) {
				setKeyWasForLockMe();
				if (panelDetailKatalog.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryKatalog.getId2SelectAfterDelete();
					panelQueryKatalog.setSelectedId(oNaechster);
				}
				panelSplitKatalog.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailSperren) {
				setKeyWasForLockMe();
				if (panelDetailSperren.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQuerySperren.getId2SelectAfterDelete();
					panelQuerySperren.setSelectedId(oNaechster);
				}
				panelSplitSperren.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailShopgruppe) {
				setKeyWasForLockMe();
				if (panelDetailShopgruppe.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryShopgruppe.getId2SelectAfterDelete();
					panelQueryShopgruppe.setSelectedId(oNaechster);
				}
				panelSplitShopgruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailErsatztypen) {
				setKeyWasForLockMe();
				if (panelDetailErsatztypen.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryErsatztypen.getId2SelectAfterDelete();
					panelQueryErsatztypen.setSelectedId(oNaechster);
				}
				panelSplitErsatztypen.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailAllergen) {
				setKeyWasForLockMe();
				if (panelDetailAllergen.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryAllergen.getId2SelectAfterDelete();
					panelQueryAllergen.setSelectedId(oNaechster);
				}
				panelSplitAllergen.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailZugehoerige) {
				setKeyWasForLockMe();
				if (panelDetailZugehoerige.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryZugehoerige.getId2SelectAfterDelete();
					panelQueryZugehoerige.setSelectedId(oNaechster);
				}
				panelSplitZugehoerige.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailStaffelpeise) {
				setKeyWasForLockMe();
				if (panelDetailStaffelpeise.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryStaffelpreise.getId2SelectAfterDelete();
					panelQueryStaffelpreise.setSelectedId(oNaechster);
				}
				panelSplitStaffelpreise.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailEinkaufsean) {
				setKeyWasForLockMe();
				if (panelDetailEinkaufsean.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryEinkaufsean.getId2SelectAfterDelete();
					panelQueryEinkaufsean.setSelectedId(oNaechster);
				}
				panelSplitEinkaufsean.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailLagerplatz) {
				setKeyWasForLockMe();
				if (panelDetailLagerplatz.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLagerplatz.getId2SelectAfterDelete();
					panelQueryLagerplatz.setSelectedId(oNaechster);
				}
				panelSplitLagerplatz.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArtikelkommentar) {
				setKeyWasForLockMe();
				if (panelDetailArtikelkommentar.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArtikelkommentar.getId2SelectAfterDelete();
					panelQueryArtikelkommentar.setSelectedId(oNaechster);
				}
				panelSplitArtikelkommentar.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailArtikellager) {
				setKeyWasForLockMe();
				if (panelDetailArtikellager.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArtikellager.getId2SelectAfterDelete();
					panelQueryArtikellager.setSelectedId(oNaechster);
				}
				panelSplitArtikellager.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailVkpfStaffelmenge) {
				setKeyWasForLockMe();
				if (panelDetailVkpfStaffelmenge.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryVkpfStaffelmenge.getId2SelectAfterDelete();
					panelQueryVkpfStaffelmenge.setSelectedId(oNaechster);
				}
				panelQueryVkpfStaffelmenge.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailSoko) {
				setKeyWasForLockMe();
				if (panelDetailSoko.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQuerySoko.getId2SelectAfterDelete();
					panelQuerySoko.setSelectedId(oNaechster);
				}
				panelQuerySoko.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailSokomengenstaffel) {
				setKeyWasForLockMe();
				if (panelDetailSokomengenstaffel.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQuerySokomengenstaffel.getId2SelectAfterDelete();
					panelQuerySokomengenstaffel.setSelectedId(oNaechster);
				}
				panelQuerySokomengenstaffel.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			// usemenuebar: Hier super.eventItemchanged(e); aufrufen, damit die
			// Menuebar, wenn auf ein "unteres Ohrwaschl"
			// geklickt wird, angezeigt wird.
			super.lPEventItemChanged(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (e.getSource() == panelQueryArtikel && sAspectInfo.endsWith(EXTRA_NEU_AUS_ARTIKEL)) {
				createDetail((Integer) panelQueryArtikel.getSelectedId());

				if (panelQueryArtikel.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				// Neu aus Artikel.
				Integer iIdArtikel = (Integer) panelQueryArtikel.getSelectedId();

				if (iIdArtikel != null) {

					DialogArtikelkopieren d = new DialogArtikelkopieren(
							DelegateFactory.getInstance().getArtikelDelegate().artikelFindByPrimaryKey(iIdArtikel),
							getInternalFrameArtikel());
					LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);
					if (d.getArtikelnummerNeu() != null) {
						Object[] o = DelegateFactory.getInstance().getArtikelDelegate().kopiereArtikel(iIdArtikel,
								d.getArtikelnummerNeu(), d.getZuKopierendeFelder(), d.getHerstellerIIdNeu(), null);

						Integer artikelId_Neu = (Integer) o[0];

						panelQueryArtikel.setSelectedId(artikelId_Neu);
						panelQueryArtikel.eventYouAreSelected(false);

						boolean bAndereSprachenKopiert = (Boolean) o[1];

						if (bAndereSprachenKopiert == true) {

							DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.info"),
									LPMain.getInstance().getTextRespectUISPr("artikel.info.mehreresprachenkopiert"));
						}

						boolean bEsSindZukuenftigePreisevorhanden = (Boolean) o[2];
						if (bEsSindZukuenftigePreisevorhanden == true) {

							DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.info"),
									LPMain.getInstance().getTextRespectUISPr("artikel.info.zukuenftigepreisekopiert"));
						}
					}
				}

			} else if (e.getSource() == panelQueryArtikel && sAspectInfo.endsWith(EXTRA_KOMMENTAR_SUCHEN)) {

				String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };

				panelQueryFLRKommentarart = new PanelQueryFLR(null,
						ArtikelFilterFactory.getInstance().createFKArtikelkommentarSuche(),
						QueryParameters.UC_ID_ARTIKELKOMMENTARSUCHE, aWhichButtonIUse, getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr("artikel.kommentarsuche"),
						new FilterKriterium("locale.c_nr", true, "'" + LPMain.getTheClient().getLocUiAsString() + "'", // wenn
								// das
								// Kriterium
								// verwendet
								// wird,
								// sollen
								// die
								// versteckten
								// nicht
								// mitangezeigt
								// werden
								FilterKriterium.OPERATOR_EQUAL, false),
						LPMain.getInstance().getTextRespectUISPr("artikel.kommentarsuche.allesprachen"));

				panelQueryFLRKommentarart.befuellePanelFilterkriterienDirekt(new FilterKriteriumDirekt("x_kommentar",
						"", FilterKriterium.OPERATOR_LIKE, LPMain.getInstance().getTextRespectUISPr("lp.kommentar"),
						FilterKriteriumDirekt.EXTENDED_SEARCH, false, true, Facade.MAX_UNBESCHRAENKT), null);

				new DialogQuery(panelQueryFLRKommentarart);

			} else if (e.getSource() == panelQueryArtikel && sAspectInfo.endsWith(EXTRA_LAGERORT_BEARBEITEN)) {

				ArbeitsplatzparameterDto aparameter = DelegateFactory.getInstance().getParameterDelegate()
						.holeArbeitsplatzparameter(ParameterFac.ARBEITSPLATZPARAMETER_LAGERPLATZ_DIREKT_ERFASSEN);

				DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) panelQueryArtikel.getSelectedId());

				DialogLagerortBearbeiten d = new DialogLagerortBearbeiten(new Integer(aparameter.getCWert()),
						panelQueryArtikel);

				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);

				d.setVisible(true);

			} else if (e.getSource() == panelQueryArtikel && sAspectInfo.endsWith(MENUE_ACTION_ETIKETT)) {
				String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.etikett.shortcut");
				getInternalFrame().showReportKriterien(new ReportArtikeletikett(getInternalFrameArtikel(), add2Title));
			} else if (e.getSource() == panelQueryArtikelkommentar
					&& sAspectInfo.endsWith(MENUE_ACTION_KOMMENTARE_AUS_ANDEREM_ARTIKEL_KOPIEREN)) {
				panelQueryFLRArtikelKommentarKopieren = ArtikelFilterFactory.getInstance()
						.createPanelFLRArtikel(getInternalFrame(), false);
				new DialogQuery(panelQueryFLRArtikelKommentarKopieren);
			} else if (e.getSource() == panelQueryVkpfStaffelmenge
					&& sAspectInfo.endsWith(MENUE_ACTION_VK_STAFFELPREISE_ERHOEHEN)) {
				DialogStaffelpreiseErhoehen d = new DialogStaffelpreiseErhoehen(getInternalFrameArtikel(),
						getInternalFrameArtikel().getArtikelDto().getIId(), null);

				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);

				d.setVisible(true);
				panelSplitVkpfStaffelmenge.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryStaffelpreise
					&& sAspectInfo.endsWith(MENUE_ACTION_EK_STAFFELPREISE_ERHOEHEN)) {
				DialogStaffelpreiseErhoehen d = new DialogStaffelpreiseErhoehen(getInternalFrameArtikel(), null,
						(Integer) panelQueryArtikellieferant.getSelectedId());
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);
				panelSplitStaffelpreise.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryArtikel) {
				createDetail((Integer) panelQueryArtikel.getSelectedId());

				if (panelQueryArtikel.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailArtikel.eventActionNew(null, true, false);
				setSelectedComponent(panelDetailArtikel);
			} else if (e.getSource() == panelQueryArtikellieferant) {
				panelDetailArtikellieferant.eventActionNew(e, true, false);
				panelDetailArtikellieferant.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitArtikellieferant);
			} else if (e.getSource() == panelQueryArtikellager) {
				panelDetailArtikellager.eventActionNew(e, true, false);
				panelDetailArtikellager.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitArtikellager);
			} else if (e.getSource() == panelQueryKatalog) {
				panelDetailKatalog.eventActionNew(e, true, false);
				panelDetailKatalog.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitKatalog);
			} else if (e.getSource() == panelQueryEinkaufsean) {
				panelDetailEinkaufsean.eventActionNew(e, true, false);
				panelDetailEinkaufsean.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitEinkaufsean);
			} else if (e.getSource() == panelQuerySperren) {
				panelDetailSperren.eventActionNew(e, true, false);
				panelDetailSperren.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitSperren);
			} else if (e.getSource() == panelQueryShopgruppe) {
				panelDetailShopgruppe.eventActionNew(e, true, false);
				panelDetailShopgruppe.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitShopgruppe);
			} else if (e.getSource() == panelQueryErsatztypen) {
				panelDetailErsatztypen.eventActionNew(e, true, false);
				panelDetailErsatztypen.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitErsatztypen);
			} else if (e.getSource() == panelQueryAllergen) {
				panelDetailAllergen.eventActionNew(e, true, false);
				panelDetailAllergen.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitAllergen);
			} else if (e.getSource() == panelQueryZugehoerige) {
				panelDetailZugehoerige.eventActionNew(e, true, false);
				panelDetailZugehoerige.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitZugehoerige);
			} else if (e.getSource() == panelQueryLagerplatz) {
				panelDetailLagerplatz.eventActionNew(e, true, false);
				panelDetailLagerplatz.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitLagerplatz);
			} else if (e.getSource() == panelQueryStaffelpreise) {
				panelDetailStaffelpeise.eventActionNew(e, true, false);
				panelDetailStaffelpeise.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitStaffelpreise);
			} else if (e.getSource() == panelQueryArtikelkommentar) {
				panelDetailArtikelkommentar.eventActionNew(e, true, false);
				panelDetailArtikelkommentar.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitArtikelkommentar);
			} else if (e.getSource() == panelQueryVkpfStaffelmenge) {
				panelDetailVkpfStaffelmenge.eventActionNew(e, true, false);
				panelDetailVkpfStaffelmenge.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitVkpfStaffelmenge);
			} else if (e.getSource() == panelQuerySoko) {
				panelDetailSoko.eventActionNew(e, true, false);
				panelDetailSoko.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitSoko);
			} else if (e.getSource() == panelQuerySokomengenstaffel) {
				panelDetailSokomengenstaffel.eventActionNew(e, true, false);
				panelDetailSokomengenstaffel.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitSokomengenstaffel);
			}
		}
		if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == panelQueryArtikellieferant) {
				int iPos = panelQueryArtikellieferant.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryArtikellieferant.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryArtikellieferant.getTable().getValueAt(iPos - 1, 0);
					DelegateFactory.getInstance().getArtikelDelegate().vertauscheArtikellieferanten(iIdPosition,
							iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren

					panelQueryArtikellieferant.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryArtikelkommentar) {
				int iPos = panelQueryArtikelkommentar.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryArtikelkommentar.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryArtikelkommentar.getTable().getValueAt(iPos - 1, 0);
					DelegateFactory.getInstance().getArtikelkommentarDelegate().vertauscheArtikelkommentar(iIdPosition,
							iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren

					panelQueryArtikelkommentar.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryLagerplatz) {
				int iPos = panelQueryLagerplatz.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryLagerplatz.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryLagerplatz.getTable().getValueAt(iPos - 1, 0);
					DelegateFactory.getInstance().getLagerDelegate().vertauscheArtikellagerplaetze(iIdPosition,
							iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren

					panelQueryLagerplatz.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQuerySperren) {
				int iPos = panelQuerySperren.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQuerySperren.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQuerySperren.getTable().getValueAt(iPos - 1, 0);
					DelegateFactory.getInstance().getArtikelDelegate().vertauscheArtikelsperren(iIdPosition,
							iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren

					panelQuerySperren.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryErsatztypen) {
				int iPos = panelQueryErsatztypen.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryErsatztypen.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryErsatztypen.getTable().getValueAt(iPos - 1, 0);
					DelegateFactory.getInstance().getArtikelDelegate().vertauscheErsatztypen(iIdPosition,
							iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren

					panelQueryErsatztypen.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == panelQueryArtikellieferant) {
				int iPos = panelQueryArtikellieferant.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryArtikellieferant.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryArtikellieferant.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryArtikellieferant.getTable().getValueAt(iPos + 1, 0);
					DelegateFactory.getInstance().getArtikelDelegate().vertauscheArtikellieferanten(iIdPosition,
							iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryArtikellieferant.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryArtikelkommentar) {
				int iPos = panelQueryArtikelkommentar.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryArtikelkommentar.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryArtikelkommentar.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryArtikelkommentar.getTable().getValueAt(iPos + 1, 0);
					DelegateFactory.getInstance().getArtikelkommentarDelegate().vertauscheArtikelkommentar(iIdPosition,
							iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryArtikelkommentar.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryLagerplatz) {
				int iPos = panelQueryLagerplatz.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryLagerplatz.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryLagerplatz.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryLagerplatz.getTable().getValueAt(iPos + 1, 0);
					DelegateFactory.getInstance().getLagerDelegate().vertauscheArtikellagerplaetze(iIdPosition,
							iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryLagerplatz.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQuerySperren) {
				int iPos = panelQuerySperren.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQuerySperren.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQuerySperren.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQuerySperren.getTable().getValueAt(iPos + 1, 0);
					DelegateFactory.getInstance().getArtikelDelegate().vertauscheArtikelsperren(iIdPosition,
							iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQuerySperren.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryErsatztypen) {
				int iPos = panelQueryErsatztypen.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryErsatztypen.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryErsatztypen.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryErsatztypen.getTable().getValueAt(iPos + 1, 0);
					DelegateFactory.getInstance().getArtikelDelegate().vertauscheErsatztypen(iIdPosition,
							iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryErsatztypen.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (e.getSource() == panelQueryArtikellieferant) {
				panelDetailArtikellieferant.eventActionNew(e, true, false);
				panelDetailArtikellieferant.eventYouAreSelected(false); // Buttons
				// schalten
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEAVE_ME_ALONE_BUTTON) {
			if (e.getSource() == panelQueryArtikel) {
				String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.etikett");
				getInternalFrame().showReportKriterien(new ReportArtikeletikett(getInternalFrameArtikel(), add2Title));
			}
		}
	}

	private void dialogPaternoster() throws Throwable {
		panelQueryFLRPaternoster = ArtikelFilterFactory.getInstance().createPanelFLRPaternoster(getInternalFrame(),
				null, null);
		new DialogQuery(panelQueryFLRPaternoster);
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		// usemenubar 1: super-Methode muss in jedem TabbedPane vorhanden sein
		/*
		 * AD: if (e != null) System.out.println(e.toString() + ", " +
		 * e.getSource().toString());
		 */
		super.lPEventObjectChanged(e);

		getInternalFrameArtikel().setRechtModulweit(sRechtModulweit);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryArtikel.eventYouAreSelected(false);
			if (panelQueryArtikel.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
			}
			panelQueryArtikel.updateButtons();

		} else if (selectedIndex == IDX_PANEL_DETAIL) {
			createDetail((Integer) panelQueryArtikel.getSelectedId());
			panelDetailArtikel.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_PREISE) {

			Defaults.lUhrQuickDirtyART = System.currentTimeMillis();
			myLogger.error("ART>VK-Preise 1 : " + (System.currentTimeMillis() - Defaults.lUhrQuickDirtyART), null);
			Defaults.lUhrQuickDirtyART = System.currentTimeMillis();

			if (DelegateFactory.getInstance().getVkPreisfindungDelegate()
					.vkpfartikelpreislisteFindByMandantCNr() == null
					|| DelegateFactory.getInstance().getVkPreisfindungDelegate()
							.vkpfartikelpreislisteFindByMandantCNr().length == 0) {
				DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.hint"),
						LPMain.getInstance().getTextRespectUISPr("vkpf.hint.keinepreisliste"));
				setSelectedComponent(panelQueryArtikel);
			} else {
				DelegateFactory.getInstance().getLagerDelegate().getHauptlagerDesMandanten();

				createPreise(getInternalFrameArtikel().getArtikelDto().getIId());
				panelDetailArtikelpreise.eventYouAreSelected(false);
			}

			myLogger.error("ART>VK-Preise 2 : " + (System.currentTimeMillis() - Defaults.lUhrQuickDirtyART), null);
			Defaults.lUhrQuickDirtyART = System.currentTimeMillis();
		} else if (selectedIndex == IDX_PANEL_VKPFSTAFFELMENGE) {
			createVkpfStaffelmenge(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitVkpfStaffelmenge.eventYouAreSelected(false);
			panelQueryVkpfStaffelmenge.updateButtons();
		} else if (selectedIndex == IDX_PANEL_BESTELLDATEN) {
			createBestelldaten(getInternalFrameArtikel().getArtikelDto().getIId());
			panelDetailArtikelbestelldaten.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_WAFFENREGISTER) {
			createWaffenregister(getInternalFrameArtikel().getArtikelDto().getIId());
			panelDetailWaffenregister.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_LIEFERANT) {
			createArtikellieferant(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitArtikellieferant.eventYouAreSelected(false);
			panelQueryArtikellieferant.updateButtons();
		} else if (selectedIndex == IDX_PANEL_EK_STAFFELPREISE) {
			createArtikellieferant(getInternalFrameArtikel().getArtikelDto().getIId());
			panelQueryArtikellieferant.eventYouAreSelected(false);
			panelDetailArtikellieferant.setKeyWhenDetailPanel(panelQueryArtikellieferant.getSelectedId());
			panelDetailArtikellieferant.eventYouAreSelected(false);

			if (panelQueryArtikellieferant.getSelectedId() != null) {
				// SP9009
				ArtikellieferantDto artikellieferantDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikellieferantFindByPrimaryKey((Integer) panelQueryArtikellieferant.getSelectedId());

				if (artikellieferantDto != null && artikellieferantDto.getLieferantDto() != null && !artikellieferantDto
						.getLieferantDto().getMandantCNr().equals(LPMain.getTheClient().getMandant())) {

					if (!DelegateFactory.getInstance().getBenutzerServicesDelegate().hatRechtInZielmandant(
							RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF,
							artikellieferantDto.getLieferantDto().getMandantCNr())) {
						getInternalFrameArtikel().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
					}
				}

				createStaffelpreise((Integer) panelQueryArtikellieferant.getSelectedId());
				panelSplitStaffelpreise.eventYouAreSelected(false);
				panelQueryStaffelpreise.updateButtons();
			} else {
				DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.info"),
						LPMain.getInstance().getTextRespectUISPr("artikel.keinlieferantausgewaehlt"));

				setSelectedIndex(IDX_PANEL_LIEFERANT);
				lPEventObjectChanged(e);
			}
		} else if (selectedIndex == IDX_PANEL_TECHNIK) {
			createTechnik(getInternalFrameArtikel().getArtikelDto().getIId());
			panelDetailArtikeltechnik.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_SONSTIGES) {
			createSonstiges(getInternalFrameArtikel().getArtikelDto().getIId());
			panelDetailArtikelsonstiges.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_EXTERNEDOKUMENTE) {
			createExterneDokumente(getInternalFrameArtikel().getArtikelDto().getIId());
			panelDetailExterneDokumente.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_TRUMPF) {
			createTrumpf(getInternalFrameArtikel().getArtikelDto().getIId());
			panelDetailTrumpf.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_LAGER) {
			createArtikellager(getInternalFrameArtikel().getArtikelDto().getIId());

			panelSplitArtikellager.eventYouAreSelected(false);
			panelQueryArtikellager.updateButtons();

		} else if (selectedIndex == IDX_PANEL_KATALOG) {
			createKatalog(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitKatalog.eventYouAreSelected(false);
			panelQueryKatalog.updateButtons();
		} else if (selectedIndex == IDX_PANEL_EINKAUFSEAN) {
			createEinkaufsean(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitEinkaufsean.eventYouAreSelected(false);
			panelQueryEinkaufsean.updateButtons();
		} else if (selectedIndex == IDX_PANEL_SPERREN) {
			createSperren(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitSperren.eventYouAreSelected(false);
			panelQuerySperren.updateButtons();
		} else if (selectedIndex == IDX_PANEL_SHOPGRUPPE) {
			createShopgruppe(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitShopgruppe.eventYouAreSelected(false);
			panelQueryShopgruppe.updateButtons();
		} else if (selectedIndex == IDX_PANEL_SOKO) {

			if (!getInternalFrame().bRechtDarfPreiseAendernVerkauf) {
				getInternalFrameArtikel().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
			}

			createSoko(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitSoko.eventYouAreSelected(false);
			panelQuerySoko.updateButtons();
		} else if (selectedIndex == IDX_PANEL_SOKO_MENGENSTAFFEL) {

			if (!getInternalFrame().bRechtDarfPreiseAendernVerkauf) {
				getInternalFrameArtikel().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
			}

			createSoko(getInternalFrameArtikel().getArtikelDto().getIId());
			panelQuerySoko.eventYouAreSelected(false);
			panelDetailSoko.setKeyWhenDetailPanel(panelQuerySoko.getSelectedId());
			panelDetailSoko.eventYouAreSelected(false);

			if (panelQuerySoko.getSelectedId() != null) {

				createSokomengenstaffel((Integer) panelQuerySoko.getSelectedId());
				panelSplitSokomengenstaffel.eventYouAreSelected(false);
				panelQuerySokomengenstaffel.updateButtons();
			} else {
				DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.info"),
						LPMain.getInstance().getTextRespectUISPr("artikel.keinesokoausgewaehlt"));

				setSelectedIndex(IDX_PANEL_SOKO);
				lPEventObjectChanged(e);
			}

		} else if (selectedIndex == IDX_PANEL_ERSATZTYPEN) {

			if (bArtikelfreigabe == true && getInternalFrameArtikel().getArtikelDto() != null
					&& getInternalFrameArtikel().getArtikelDto().getTFreigabe() != null) {
				getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
			}

			createErsatztypen(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitErsatztypen.eventYouAreSelected(false);
			panelQueryErsatztypen.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ARTIKELALERGEN) {
			createAlergen(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitAllergen.eventYouAreSelected(false);
			panelQueryAllergen.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ZUGEHOERIGE) {
			createZugehoerige(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitZugehoerige.eventYouAreSelected(false);
			panelQueryZugehoerige.updateButtons();
		} else if (selectedIndex == IDX_PANEL_LAGERPLATZ) {

			boolean hatRecht = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_WW_ARTIKEL_LAGERPLATZ_CUD);
			if (hatRecht == true) {
				getInternalFrameArtikel().setRechtModulweit(RechteFac.RECHT_MODULWEIT_UPDATE);
			} else {
				getInternalFrameArtikel().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);

			}

			createLagerplatz(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitLagerplatz.eventYouAreSelected(false);
			panelQueryLagerplatz.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ARTIKELKOMMENTAR) {

			if (bArtikelfreigabe == true && getInternalFrameArtikel().getArtikelDto() != null
					&& getInternalFrameArtikel().getArtikelDto().getTFreigabe() != null) {

				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
						.getParameterDelegate()
						.getParametermandant(ParameterFac.PARAMETER_KOMMENTAR_BEI_ARTIKELFREIGABE_AENDERBAR,
								ParameterFac.KATEGORIE_ARTIKEL, LPMain.getTheClient().getMandant());
				boolean b = (Boolean) parameter.getCWertAsObject();
				if (b == false) {

					// PJ21640
					boolean hatRecht = DelegateFactory.getInstance().getTheJudgeDelegate()
							.hatRecht(RechteFac.RECHT_WW_ARTIKELKOMMENTAR_FREIGEGEBENER_ARTIKEL_CUD);
					if (!hatRecht) {
						getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
					}

				}

			}

			createArtikelkommentar(getInternalFrameArtikel().getArtikelDto().getIId());
			panelSplitArtikelkommentar.eventYouAreSelected(false);
			panelQueryArtikelkommentar.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ARTIKELEIGENSCHAFTEN) {
			createEigenschaften(getInternalFrameArtikel().getArtikelDto().getIId());
			panelDetailArtikeleigenschaft.eventYouAreSelected(false);
		}
		refreshTitle();
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("lp.artikel"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		String sBezeichnung = "";
		if (getInternalFrameArtikel().getArtikelDto() != null) {
			if (getInternalFrameArtikel().getArtikelDto().getArtikelsprDto() != null) {
				sBezeichnung = getInternalFrameArtikel().getArtikelDto().getArtikelsprDto().getCBez();
			}
			if (sBezeichnung == null) {
				sBezeichnung = "";
			}
			if (getInternalFrameArtikel().getArtikelDto().getCNr() == null) {
				getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");

			} else {
				String referenznummer = "";
				if (bReferenznummerInPositonen && getInternalFrameArtikel().getArtikelDto().getCReferenznr() != null) {
					referenznummer = ", " + getInternalFrameArtikel().getArtikelDto().getCReferenznr();
				}

				getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
						getInternalFrameArtikel().getArtikelDto().getCNr() + referenznummer + ", " + sBezeichnung);
			}
		}
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {

		// usemenuebar: Vergleichen, welcher Button gedrueckt wurde
		if (e.getActionCommand().equals(MENUE_ACTION_STATISTIK)) {

			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.artikelstatistik");
			if (getInternalFrameArtikel().getArtikelDto() != null) {
				getInternalFrame().showReportKriterien(new ReportArtikelstatistik(getInternalFrameArtikel(),
						getInternalFrameArtikel().getArtikelDto().getIId(), false, add2Title));
			}

		} else if (e.getActionCommand().equals(MENUE_ACTION_RESERVIERUNGEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.artikelreservierung");
			getInternalFrame()
					.showReportKriterien(new ReportArtikelreservierungen(getInternalFrameArtikel(), add2Title));

		} else if (e.getActionCommand().equals(MENUE_ACTION_LIEFERANTENPREISVERGLEICH)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.lieferantenpreisvergleich");
			getInternalFrame()
					.showReportKriterien(new ReportLieferantenpreisvergleich(getInternalFrameArtikel(), add2Title));

		} else if (e.getActionCommand().equals(MENUE_ACTION_BESTELLTLISTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.artikelbestellt");
			getInternalFrame().showReportKriterien(new ReportArtikelbestellt(getInternalFrameArtikel(),
					getInternalFrameArtikel().getArtikelDto(), add2Title));

		} else if (e.getActionCommand().equals(MENUE_ACTION_FEHLMENGEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.fehlmengen");
			getInternalFrame().showReportKriterien(new ReportArtikelfehlmengen(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_VKPREISENTWICKLUNG)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.vkpreisentwicklung");
			getInternalFrame().showReportKriterien(new ReportVkpreisentwicklung(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_ANFRAGESTATISTIK)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("anf.anfragestatistik");
			getInternalFrame().showReportKriterien(new ReportAnfragestatistik(getInternalFrameArtikel(), add2Title));

		} else if (e.getActionCommand().equals(MENUE_ACTION_ANGEBOTSSTATISTIK)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("angb.angebotsstatistik");
			getInternalFrame().showReportKriterien(new ReportAngebotsstatistik(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_RAHMENRESERVIERUNGEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("auft.rahmenreservierungen");
			getInternalFrame().showReportKriterien(
					new ReportRahmenauftragReservierungsliste(getInternalFrameArtikel(), add2Title));

		} else if (e.getActionCommand().equals(MENUE_ACTION_FORECASTPOSITIONEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.forecastpositionen");
			getInternalFrame().showReportKriterien(new ReportForecastpositionen(getInternalFrameArtikel(), add2Title));

		} else if (e.getActionCommand().equals(MENUE_ACTION_VERWENDUNGSNACHWEIS)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.verwendungsnachweis");
			getInternalFrame().showReportKriterien(new ReportVerwendungsnachweis(getInternalFrameArtikel(), add2Title));

		} else if (e.getActionCommand().equals(MENUE_PFLEGE_VKPREISE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.preise");
			getInternalFrame().showPanelDialog(new PanelDialogPreispflege(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_PFLEGE_VKSTAFFEL_XLS_IMPORT)) {

			DialogVkMengenstaffelImportXLS d = new DialogVkMengenstaffelImportXLS(this);

		} else if (e.getActionCommand().equals(MENUE_PFLEGE_ALLERGENE)) {
			panelQueryFLRLieferant = PartnerFilterFactory.getInstance().createPanelFLRLieferant(getInternalFrame(),
					null, true, false);
			new DialogQuery(panelQueryFLRLieferant);
		} else if (e.getActionCommand().equals(MENUE_PFLEGE_EIGENSCHAFTEN_XLS_IMPORT)) {
			DialogEigenschaftenImportXLS d = new DialogEigenschaftenImportXLS(this,
					PanelFac.PANEL_ARTIKELEIGENSCHAFTEN);
		} else if (e.getActionCommand().equals(MENUE_PFLEGE_PATERNOSTER)) {
			dialogPaternoster();
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_LAGERSTANDSLISTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.lagerstandsliste");
			getInternalFrame().showReportKriterien(new ReportLagerstandliste(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_ALERGENE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.allergen");
			getInternalFrame().showReportKriterien(new ReportAllergene(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_ARTIKELGRUPPEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.artikelgruppen");
			getInternalFrame().showReportKriterien(new ReportArtikelgruppen(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_MATERIALBEDARFSVORSCHAU)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.materialbedarfsvorschau");
			getInternalFrame()
					.showReportKriterien(new ReportMaterialbedarfsvorschau(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_INDIREKTE_WARENEINSATZSTATISTIK)) {
			String add2Title = LPMain.getInstance()
					.getTextRespectUISPr("artikel.report.indirektewareneinsatzstatistik");
			getInternalFrame()
					.showReportKriterien(new ReportIndirekteWarneinsatzstatistik(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_ARTIKEL_OHNE_STKL_VERWENDUNG)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.artikelohnestklverwendung");
			getInternalFrame()
					.showReportKriterien(new ReportArtikelOhneStklVerwendung(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_AUFTRAGSWERTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.auftragswerte");
			getInternalFrame().showReportKriterien(new ReportAuftragswerte(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_SHOPGRUPPEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.shopgruppen");
			getInternalFrame().showReportKriterien(new ReportShopgruppen(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_NAECHSTE_WARTUNGEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.naechstewartungen");
			getInternalFrame().showReportKriterien(new ReportNaechsteWartungen(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_SERIENNUMMERN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.seriennummern");
			getInternalFrame().showReportKriterien(new ReportSeriennummern(getInternalFrameArtikel(), add2Title,
					(Integer) panelQueryArtikel.getSelectedId()));
		} else if (e.getActionCommand().contentEquals(MENUE_JOURNAL_ACTION_MEHRERE_ETIKETTEN)) {
			String add2Title = LPMain.getTextRespectUISPr("artikel.report.multietikett");
			getInternalFrame()
					.showReportKriterien(new ReportMehrereArtikeletiketten(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_ETIKETT)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.etikett");
			getInternalFrame().showReportKriterien(new ReportArtikeletikett(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_STAMMBLATT)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.stammblatt");
			getInternalFrame().showReportKriterien(new ReportArtikelstammblatt(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_LOSSTATUS)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.losstatus");
			getInternalFrame().showReportKriterien(new ReportLosstatus(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_DETAILBEDARFE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.detailbedarf");
			getInternalFrame().showReportKriterien(new ReportRahmenbedarfe(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_FREIINFERTIGUNG)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.freiinfertigung");
			getInternalFrame().showReportKriterien(new ReportFreiInFertigung(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_KUNDENSOKOS)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.kundesokos");
			getInternalFrame().showReportKriterien(new ReportKundensokos(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_SNRSTAMMBLATT)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.snrstammblatt");
			getInternalFrame().showReportKriterien(new ReportSnrStammblatt(getInternalFrameArtikel(), add2Title,
					(Integer) panelQueryArtikel.getSelectedId()));
		} else if (e.getActionCommand().equals(MENUE_ACTION_BEWEGUNSVORSCHAU)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("bes.bewegungsvorschau");
			getInternalFrame().showReportKriterien(new ReportBewegungsvorschau(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_BEWEGUNSVORSCHAU)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("bes.bewegungsvorschau");
			getInternalFrame()
					.showReportKriterien(new ReportJournalBewegungsvorschau(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_AUFTRAGSSERIENNUMMERN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.auftragsseriennummern");
			getInternalFrame()
					.showReportKriterien(new ReportAuftragsseriennummern(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_WARENBENEGUNSJOURNAL)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.warenbewegungsjournal");
			getInternalFrame().showReportKriterien(new ReportWarenbewegung(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_REKLAMATIONEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikl.report.reklamationen");
			getInternalFrame().showReportKriterien(
					new ReportOffeneReklamationenEinesArtikels(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_PROJEKTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikl.report.projekte");
			getInternalFrame()
					.showReportKriterien(new ReportProjekteEinesArtikels(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_GESTPREISUEBERMINVK)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.gestpreisueberminvk");
			getInternalFrame().showReportKriterien(new ReportGestpreisUeberMinVK(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_MINDESTLAGERSTAENDE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.mindestlagerstaende");
			getInternalFrame().showReportKriterien(new ReportMindestlagerstaende(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_WARENENTNAHMESTATISTIK)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.warenentnahmestatistik");
			getInternalFrame()
					.showReportKriterien(new ReportWarenentnahmestatistik(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_LADENHUETER)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.ladenhueter");
			getInternalFrame().showReportKriterien(new ReportLadenhueter(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_HITLISTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.hitliste");
			getInternalFrame().showReportKriterien(new ReportHitliste(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_MINDESTHALTBARKEIT)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.journal.mindesthaltbarkeit");
			getInternalFrame().showReportKriterien(new ReportMindesthalbarkeit(getInternalFrameArtikel(), add2Title));
		}

		else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_PREISLISTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.preisliste");
			getInternalFrame().showReportKriterien(new ReportPreisliste(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_MAKEORBUY)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.makeorbuy");
			getInternalFrame().showReportKriterien(new ReportMakeOrBuy(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_KUNDESONDERKONDITIONEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.kundesokos");
			getInternalFrame()
					.showReportKriterien(new ReportKundesonderkonditionen(getInternalFrameArtikel(), add2Title));
		}

		else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_OFFENERAHMENBEDARFE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.offenerahmendetailbedarfe");
			getInternalFrame().showReportKriterien(new ReportAlleRahmenbedarfe(getInternalFrameArtikel(), add2Title));
		}

		else if (e.getActionCommand().equals(MENUE_ACTION_RAHMENBESTELLTLISTE)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.report.artikelrahmenbestelltliste");
			getInternalFrame()
					.showReportKriterien(new ReportArtikelRahmenbestellungsliste(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_AENDERUNGEN)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr("artikel.info.report.aenderungen");
			getInternalFrame().showReportKriterien(new ReportAenderungen(getInternalFrameArtikel(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_PFLEGE_AENDERE_SNRCHNR)) {

			if (getInternalFrameArtikel().getArtikelDto() != null
					&& (Helper.short2boolean(getInternalFrameArtikel().getArtikelDto().getBSeriennrtragend()) || Helper
							.short2boolean(getInternalFrameArtikel().getArtikelDto().getBChargennrtragend()))) {
				getInternalFrame().showPanelDialog(new PanelDialogSnrChnrAendern(getInternalFrameArtikel(),
						LPMain.getInstance().getTextRespectUISPr("artikel.pflege.snrchnraendern")));
			} else {
				DialogFactory.showModalDialog("Fehler", "Artikel ist nicht Seriennumern-/Chargennumernbehaftet");
				return;
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_LAGERSOLL_MIN_XLSIMPORT)) {
			DialogLagerminsollImportXLS d = new DialogLagerminsollImportXLS(this);
		} else if (e.getActionCommand().equals(MENUE_ACTION_XLSIMPORT)) {
			onActionXLSImport();

		} else if (e.getActionCommand().equals(MENUE_ACTION_LUMIQUOTE_CSV_EXPORT)) {
			// Dateiauswahldialog
			HvOptional<WrapperFile> csv = HelperClient.showSaveDialog(null, FileChooserConfigToken.ExportCsvLumiquote,
					null, new HvTaggedCsvFileFilter());
			if (csv.isPresent()) {

				ArrayList<LumiQuoteArtikelDto> alDaten = DelegateFactory.getInstance().getArtikelServiceDelegate()
						.getArtikelDatenForLumiquote();

				if (alDaten.size() > 0) {

					
			        byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF }; 
					
					File file = csv.get().getFile();

					OutputStream os = new FileOutputStream(file);
					os.write(bom);
                    OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                    LPCSVWriter writerCsv = new LPCSVWriter(osw, ',',
                            LPCSVWriter.DEFAULT_QUOTE_CHARACTER);
                    
					writerCsv.writeNext(alDaten.get(0).getColumnNames());

					for (int i = 0; i < alDaten.size(); i++) {

						writerCsv.writeNext(alDaten.get(i).zeileToStringArray());

					}

					writerCsv.close();

					

				}
			}

		} else if (e.getActionCommand().equals(MENUE_ACTION_CSVIMPORT)) {

			// Dateiauswahldialog
			HvOptional<CsvFile> csvFile = FileOpenerFactory.artikelImportOpenCsv(this);
			if (csvFile.isPresent()) {
				LPCSVReader reader = new LPCSVReader(new FileReader(csvFile.get().getFile()), ';');

				String[] sLine;
				// Erste Zeile Auslassen (Ueberschrift)
				sLine = reader.readNext();
				// zeilenweise einlesen.
				sLine = reader.readNext();
				ArrayList<ArtikelImportDto> alDaten = new ArrayList<ArtikelImportDto>();

				do {

					ArtikelImportDto dtoTemp = new ArtikelImportDto();
					try {
						dtoTemp.setArtikelnummer(sLine[0]);
						dtoTemp.setBezeichnung(sLine[1]);
						dtoTemp.setKurzbezeichnung(sLine[2]);
						dtoTemp.setZusatzbezeichnung(sLine[3]);
						dtoTemp.setZusatzbezeichnung2(sLine[4]);
						dtoTemp.setArtikelart(sLine[5]);
						dtoTemp.setEinheit(sLine[6]);
						dtoTemp.setArtikelgruppe(sLine[7]);
						dtoTemp.setArtikelklasse(sLine[8]);
						dtoTemp.setReferenznummer(sLine[9]);
						dtoTemp.setMwstsatz(sLine[10]);

						// 14520
						dtoTemp.setVkpreisbasis(sLine[11]);
						dtoTemp.setVkpreisbasisgueltigab(sLine[12]);

						dtoTemp.setFixpreispreisliste1(sLine[13]);
						dtoTemp.setRabattsatzpreisliste1(sLine[14]);
						dtoTemp.setGueltigabpreisliste1(sLine[15]);

						dtoTemp.setFixpreispreisliste2(sLine[16]);
						dtoTemp.setRabattsatzpreisliste2(sLine[17]);
						dtoTemp.setGueltigabpreisliste2(sLine[18]);

						dtoTemp.setFixpreispreisliste3(sLine[19]);
						dtoTemp.setRabattsatzpreisliste3(sLine[20]);
						dtoTemp.setGueltigabpreisliste3(sLine[21]);

						// PJ17708
						// otional REVISION / INDEX / CHARGENGEFUEHRT
						// //SNRBEHAFTET
						if (sLine.length > 22) {
							dtoTemp.setRevision(sLine[22]);
						}
						if (sLine.length > 23) {
							dtoTemp.setIndex(sLine[23]);
						}
						if (sLine.length > 24) {
							dtoTemp.setChargenbehaftet(sLine[24]);
						}
						if (sLine.length > 25) {
							dtoTemp.setSnrbehaftet(sLine[25]);
						}

						alDaten.add(dtoTemp);
					} catch (java.lang.ArrayIndexOutOfBoundsException e1) {
						// dann wurde der Rest mir Leer aufgefuellt
						alDaten.add(dtoTemp);
					}

					sLine = reader.readNext();
				} while (sLine != null);

				ArtikelImportDto[] dtos = new ArtikelImportDto[alDaten.size()];

				dtos = (ArtikelImportDto[]) alDaten.toArray(dtos);

				DialogArtikelImport d = new DialogArtikelImport(dtos, this);
				d.setSize(500, 500);
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);

			}

		} else if (e.getActionCommand().equals(MENUE_ACTION_PREISPFLEGE_EXPORT)) {
			DialogArtikelExport d = new DialogArtikelExport(getInternalFrameArtikel());
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);
		} else if (e.getActionCommand().equals(MENUE_ACTION_WBZ_AKTUALISIEREN)) {
			DialogWBZAktualisieren d = new DialogWBZAktualisieren(getInternalFrameArtikel());
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);
		} else if (e.getActionCommand().equals(MENUE_ACTION_PREISPFLEGE_IMPORT)) {
			HvOptional<XlsFile> xlsFile = FileOpenerFactory.artikelImportPreispflegeOpenXls(this);
			if (!xlsFile.isPresent())
				return;

			byte[] xlsContent = xlsFile.get().getBytes();

			ParametermandantDto p = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_BEGRUENDUNG_BEI_VKPREISAENDERUNG);

			int begruendungAngeben = (Integer) p.getCWertAsObject();

			String bemerkung = null;
			if (begruendungAngeben > 0) {
				bemerkung = JOptionPane.showInputDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("artikel.vkpreis.bemerkung"), LPMain.getTextRespectUISPr("lp.frage"),
						JOptionPane.QUESTION_MESSAGE);
			}

			DelegateFactory.getInstance().getArtikelDelegate().preiseXLSForPreispflege(xlsContent, bemerkung);

			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"),
					LPMain.getTextRespectUISPr("artikel.preispflege.import.erfolgreich"));

		} else if (MENUE_PFLEGE_4VENDING_ARTIKEL_EXPORT.equals(e.getActionCommand())) {
			exportiere4VendingArtikel();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PROFIRST_IMPORT)) {
			try {
				DelegateFactory.getInstance().getStuecklisteDelegate().pruefeUndImportiereProFirst(null, null);
			} catch (ExceptionLP e1) {

				if (e1.getICode() == EJBExceptionLP.FEHLER_PRO_FIRST_IMPORT_KUNDE_NICHT_VORHANDEN) {
					String sMsg = LPMain.getTextRespectUISPr("stk.profistimport.error.keinkunde");

					if (e1.getAlInfoForTheClient().size() > 0) {
						sMsg = sMsg + "\n" + e1.getAlInfoForTheClient().get(0);

						int indexWaehrungAbbrechen = 0;
						int indexWaehrungIgnorieren = 1;
						int iAnzahlOptionen = 2;

						Object[] aOptionen = new Object[iAnzahlOptionen];
						aOptionen[indexWaehrungAbbrechen] = LPMain.getInstance().getTextRespectUISPr("lp.abbrechen");
						aOptionen[indexWaehrungIgnorieren] = LPMain.getInstance()
								.getTextRespectUISPr("stkl.profirstimport.kundeinzukunftignorieren");

						int iAuswahl = DialogFactory.showModalDialog(getInternalFrame(), sMsg,
								LPMain.getInstance().getTextRespectUISPr("lp.frage"), aOptionen, aOptionen[0]);

						if (iAuswahl == indexWaehrungIgnorieren) {
							DelegateFactory.getInstance().getStuecklisteDelegate()
									.ignoriereKundeBeiProfirstImport((String) e1.getAlInfoForTheClient().get(0));
						}

					}

				} else {
					throw e1;
				}

			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_CHARGENNUMMER_ANHAND_WEP_AKTUALISIEREN)) {

			DialogChargennummernAnhandWEPAktualisieren d = new DialogChargennummernAnhandWEPAktualisieren(
					getInternalFrame());
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);

			if (d.getGeaenderteChnrs() != null && d.getGeaenderteChnrs().size() > 0) {

				for (int i = 0; i < d.getGeaenderteChnrs().size(); i++) {

					GeaenderteChargennummernDto gDto = d.getGeaenderteChnrs().get(i);

					WareneingangspositionDto wepDto = DelegateFactory.getInstance().getWareneingangDelegate()
							.wareneingangspositionFindByPrimaryKey(gDto.getWepIId());
					BestellpositionDto bsposDto = DelegateFactory.getInstance().getBestellungDelegate()
							.bestellpositionFindByPrimaryKey(gDto.getBestellpositionIId());
					WareneingangDto weDto = DelegateFactory.getInstance().getWareneingangDelegate()
							.wareneingangFindByPrimaryKey(wepDto.getWareneingangIId());
					ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByPrimaryKey(bsposDto.getArtikelIId());

					ReportWepEtikett2 reportEtikett = new ReportWepEtikett2(getInternalFrame(), weDto, wepDto, bsposDto,
							aDto, null, aDto.formatArtikelbezeichnung(), null, null);
					reportEtikett.eventYouAreSelected(false);
					getInternalFrame().showReportKriterien(reportEtikett);

					reportEtikett.setKeyWhenDetailPanel(wepDto.getIId());

				}

				getInternalFrame().showReportKriterien(new ReportGeaenderteChargen(getInternalFrameArtikel(),
						LPMain.getTextRespectUISPr("artikel.pflege.chargennummern.anhandwepaktualisieren"),
						d.getGeaenderteChnrs()));

			}

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_WARENZUGAENGE_FUER_PROFIRST)) {

			// Parameter fuer Pfad

			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getInstance().getTheClient().getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_PRO_FIRST_VERZEICHNIS);
			String proFirstPfad = parameter.getCWert();
			if (proFirstPfad != null && proFirstPfad.trim().length() > 0) {

				Timestamp t = new Timestamp(0);

				KeyvalueDto[] dtos = DelegateFactory.getInstance().getSystemDelegate()
						.keyvalueFindyByCGruppe(SystemServicesFac.KEYVALUE_WEP_EXPORT_PRO_FIRST_LETZTER_ZEITPUNKT);

				if (dtos != null && dtos.length > 0) {
					t = new Timestamp(new Long(dtos[0].getCValue()));
				}

				LPCSVWriter writer;
				File file = null;
				try {

					ArrayList<String[]> al = DelegateFactory.getInstance().getLagerDelegate()
							.getAlleWarenzugaengeFuerProFirst();

					if (al.size() < 2) {
						// Wenn Kein Inhalt, dann keine Datei erzeugen
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"),
								LPMain.getTextRespectUISPr("artikel.profirstexport.keininhalt"));

					} else {
						file = new File(proFirstPfad + "/" + "WE_AB_"
								+ Helper.formatTimestamp(t, LPMain.getTheClient().getLocUi()).replace('.', '-')
										.replace(' ', '_').replace(':', '-')
								+ ".csv");

						if (file.exists()) {
							int erg = JOptionPane.showConfirmDialog(LPMain.getInstance().getDesktop(),
									"Die Datei " + file.getName() + " existiert bereits.\n"
											+ "Wollen Sie sie \u00FCberschreiben?",
									"Editor: Warnung", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if (erg == JOptionPane.NO_OPTION)
								return;
						}
						writer = new LPCSVWriter(new FileWriter(file), LPCSVWriter.DEFAULT_SEPARATOR,
								LPCSVWriter.DEFAULT_QUOTE_CHARACTER);
						for (int i = 0; i < al.size(); i++) {
							writer.writeNext(al.get(i));
						}

						writer.close();

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"),
								LPMain.getTextRespectUISPr("system.extraliste.gespeichert") + " ("
										+ file.getAbsolutePath() + ") ");
					}

				} catch (java.io.FileNotFoundException ex) {
					// ev. Datei gerade verwendet?
					DialogFactory.showModalDialog("Fehler", "Die angegeben Datei '" + file.getAbsolutePath()
							+ "' konnte nicht erstellt werden. M\u00F6glicherweise wird sie durch ein anderes Programm verwendet.");

				}
			} else {
				// Pfad nicht definiert
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("artikel.profistverzeichnis.nichtdefiniert"));
			}

		} else if (e.getActionCommand().equals(MENUE_ACTION_ZUSAMMENFUEHREN)) {
			String add2Title = LPMain.getTextRespectUISPr("artikel.zusammenfuehren");
			PanelDialogArtikelZusammenfuehren d = new PanelDialogArtikelZusammenfuehren(
					getInternalFrameArtikel().getArtikelDto(), getInternalFrameArtikel(), add2Title);
			getInternalFrame().showPanelDialog(d);
			d.setVisible(true);

		}

	}

	private void onActionXLSImport() throws IOException, Throwable {
		XlsFileOpener xlsOpener = FileOpenerFactory.artikelImportOpenXls(getInternalFrame());
		xlsOpener.doOpenDialog();

		if (!xlsOpener.hasFileChosen())
			return;

		DialogArtikelXLSImport d = new DialogArtikelXLSImport(xlsOpener.getFile().getBytes(), this);
		d.setSize(500, 500);
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
		d.setVisible(true);
	}

	private void exportiere4VendingArtikel() throws Throwable {
		artikelexport4vendingController = new VendidataArtikelExportController(getInternalFrameArtikel());
		Dialog4VendingArtikelExport dialog = new Dialog4VendingArtikelExport(LPMain.getInstance().getDesktop(),
				LPMain.getTextRespectUISPr("artikel.export.menu.4vendingartikel"), true,
				artikelexport4vendingController);
		dialog.setVisible(true);
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperManuBar == null) {

			boolean bSchreibrecht = false;
			if (getInternalFrame().getRechtModulweit().equals(RechteFac.RECHT_MODULWEIT_UPDATE)) {
				bSchreibrecht = true;
			}

			// usemenubar 3: Nachfolgende Zeile erzeugt Standard- Symbolleiste
			wrapperManuBar = new WrapperMenuBar(this);
			JMenu menuDatei = (JMenu) wrapperManuBar.getComponent(WrapperMenuBar.MENU_MODUL);
			menuDatei.add(new JSeparator(), 0);

			//
			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_EK_ANGEBOT_ANFRAGE)) {
				JMenuItem menuItemImport = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.lumiquote.csvexport"));
				menuItemImport.addActionListener(this);
				menuItemImport.setActionCommand(MENUE_ACTION_LUMIQUOTE_CSV_EXPORT);
				//menuDatei.add(menuItemImport, 0);
			}

			// XLS-Import
			JMenuItem menuItemImport = new JMenuItem(LPMain.getInstance().getTextRespectUISPr("part.csvimport"));
			menuItemImport.addActionListener(this);
			menuItemImport.setActionCommand(MENUE_ACTION_CSVIMPORT);

			if (bSchreibrecht) {

				menuDatei.add(menuItemImport, 0);
			}
			// XLS-Import
			ParametermandantDto parameterLM = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_LAGERMIN_JE_LAGER, ParameterFac.KATEGORIE_ARTIKEL,
							LPMain.getTheClient().getMandant());
			boolean bLagerminJeLager = (Boolean) parameterLM.getCWertAsObject();
			if (bLagerminJeLager && bSchreibrecht) {
				JMenuItem menuItemMinSollImport = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.minsoll.xlsimport"));
				menuItemMinSollImport.addActionListener(this);
				menuItemMinSollImport.setActionCommand(MENUE_ACTION_LAGERSOLL_MIN_XLSIMPORT);
				menuDatei.add(menuItemMinSollImport, 0);

			}
			// CSV-Import
			JMenuItem menuItemXLSImport = new JMenuItem(LPMain.getInstance().getTextRespectUISPr("part.xlsimport"));
			menuItemXLSImport.addActionListener(this);
			menuItemXLSImport.setActionCommand(MENUE_ACTION_XLSIMPORT);
			if (bSchreibrecht) {
				menuDatei.add(menuItemXLSImport, 0);
			}

			boolean bDarfZusammenfuehren = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_WW_ARTIKEL_ZUSAMMENFUEHREN);

			if (bDarfZusammenfuehren && bSchreibrecht) {
				JMenuItem menuItemZusammenfuehren = new JMenuItem(
						LPMain.getTextRespectUISPr("artikel.zusammenfuehren"));
				menuItemZusammenfuehren.addActionListener(this);
				menuItemZusammenfuehren.setActionCommand(MENUE_ACTION_ZUSAMMENFUEHREN);
				HelperClient.setToolTipTextMitRechtToComponent(menuItemZusammenfuehren,
						RechteFac.RECHT_WW_ARTIKEL_ZUSAMMENFUEHREN);
				menuDatei.add(menuItemZusammenfuehren, 0);
			}

			// usemenubar 4: Hier Artikelspezifische Menues hinzufuegen
			JMenu menuPflege = new WrapperMenu("lp.pflege", this);
			JMenu menuInfo = new WrapperMenu("lp.info", this);

			JMenuItem menuItemArtikelstatistik = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("lp.statistik"));

			JMenuItem menuItemReservierungen = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("artikel.report.artikelreservierung"));
			JMenuItem menuItemFehlmenge = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("artikel.report.fehlmengen"));
			JMenuItem menuItemBestelltliste = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("artikel.report.artikelbestellt"));
			// Rahmenbestelltliste
			JMenuItem menuItemRahmenbestelltliste = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("artikel.report.artikelrahmenbestelltliste"));
			JMenuItem menuItemAnfragestatistik = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("anf.anfragestatistik"));
			JMenuItem menuItemAngebotsstatistik = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("angb.angebotsstatistik"));
			JMenuItem menuItemVerwendungsnachweis = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("artikel.verwendungsnachweis"));

			// usemenubar 5: Actionlistener setzen
			menuItemArtikelstatistik.addActionListener(this);
			// usemenubar 6: und ActionCommand setzten
			// Druck der Statistik nur wenn DarfPreiseSehen Rechte gesetzt
			// sind.
			menuItemArtikelstatistik.setActionCommand(MENUE_ACTION_STATISTIK);
			menuInfo.add(menuItemArtikelstatistik);

			menuItemReservierungen.addActionListener(this);
			menuItemReservierungen.setActionCommand(MENUE_ACTION_RESERVIERUNGEN);
			menuInfo.add(menuItemReservierungen);

			menuItemFehlmenge.addActionListener(this);
			menuItemFehlmenge.setActionCommand(MENUE_ACTION_FEHLMENGEN);
			menuInfo.add(menuItemFehlmenge);

			menuItemBestelltliste.addActionListener(this);
			menuItemBestelltliste.setActionCommand(MENUE_ACTION_BESTELLTLISTE);
			menuInfo.add(menuItemBestelltliste);

			menuItemRahmenbestelltliste.addActionListener(this);
			menuItemRahmenbestelltliste.setActionCommand(MENUE_ACTION_RAHMENBESTELLTLISTE);
			menuInfo.add(menuItemRahmenbestelltliste);

			if (getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
				menuItemAnfragestatistik.addActionListener(this);
				menuItemAnfragestatistik.setActionCommand(MENUE_ACTION_ANFRAGESTATISTIK);
				menuInfo.add(menuItemAnfragestatistik);
			}

			if (getInternalFrame().bRechtDarfPreiseSehenVerkauf) {
				menuItemAngebotsstatistik.addActionListener(this);
				menuItemAngebotsstatistik.setActionCommand(MENUE_ACTION_ANGEBOTSSTATISTIK);
				menuInfo.add(menuItemAngebotsstatistik);
			}

			JMenuItem menuItemRahmenreservierungen = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("auft.rahmenreservierungen"));

			menuItemRahmenreservierungen.addActionListener(this);
			menuItemRahmenreservierungen.setActionCommand(MENUE_ACTION_RAHMENRESERVIERUNGEN);
			menuInfo.add(menuItemRahmenreservierungen);

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_RAHMENDETAILBEDARFE)) {

				JMenuItem menuItemDetailbedarfe = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.detailbedarf"));

				menuItemDetailbedarfe.addActionListener(this);
				menuItemDetailbedarfe.setActionCommand(MENUE_ACTION_DETAILBEDARFE);
				menuInfo.add(menuItemDetailbedarfe);
			}

			JMenuItem menuItemForecastpositionen = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("artikel.report.forecastpositionen"));

			menuItemForecastpositionen.addActionListener(this);
			menuItemForecastpositionen.setActionCommand(MENUE_ACTION_FORECASTPOSITIONEN);

			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FORECAST)) {

				menuInfo.add(menuItemForecastpositionen);
			}

			menuItemVerwendungsnachweis.addActionListener(this);
			menuItemVerwendungsnachweis.setActionCommand(MENUE_ACTION_VERWENDUNGSNACHWEIS);
			menuInfo.add(menuItemVerwendungsnachweis);

			JMenuItem menuItemEtikett = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("artikel.report.etikett"));
			menuItemEtikett.addActionListener(this);
			menuItemEtikett.setActionCommand(MENUE_ACTION_ETIKETT);
			menuInfo.add(menuItemEtikett);

			JMenuItem menuItemStammblatt = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("artikel.report.stammblatt"));
			menuItemStammblatt.addActionListener(this);
			menuItemStammblatt.setActionCommand(MENUE_ACTION_STAMMBLATT);
			menuInfo.add(menuItemStammblatt);

			JMenuItem menuItemLosstatus = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("artikel.report.losstatus"));
			menuItemLosstatus.addActionListener(this);
			menuItemLosstatus.setActionCommand(MENUE_ACTION_LOSSTATUS);
			menuInfo.add(menuItemLosstatus);

			JMenuItem menuItemFreiinfertigung = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("artikel.report.freiinfertigung"));
			menuItemFreiinfertigung.addActionListener(this);
			menuItemFreiinfertigung.setActionCommand(MENUE_ACTION_FREIINFERTIGUNG);
			menuInfo.add(menuItemFreiinfertigung);
			JMenuItem menuItemBewegungsvorschau = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("bes.bewegungsvorschau"));
			menuItemBewegungsvorschau.addActionListener(this);
			menuItemBewegungsvorschau.setActionCommand(MENUE_ACTION_BEWEGUNSVORSCHAU);
			menuInfo.add(menuItemBewegungsvorschau);

			JMenuItem menuItemWarenbewegungsjournal = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("artikel.report.warenbewegungsjournal"));
			menuItemWarenbewegungsjournal.addActionListener(this);
			menuItemWarenbewegungsjournal.setActionCommand(MENUE_JOURNAL_ACTION_WARENBENEGUNSJOURNAL);
			menuInfo.add(menuItemWarenbewegungsjournal);

			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_REKLAMATION)) {
				JMenuItem menuItemReklamationen = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikl.report.reklamationen"));
				menuItemReklamationen.addActionListener(this);
				menuItemReklamationen.setActionCommand(MENUE_JOURNAL_ACTION_REKLAMATIONEN);
				menuInfo.add(menuItemReklamationen);
			}

			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PROJEKT)) {
				JMenuItem menuItemProjekt = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikl.report.projekte"));
				menuItemProjekt.addActionListener(this);
				menuItemProjekt.setActionCommand(MENUE_JOURNAL_ACTION_PROJEKTE);
				menuInfo.add(menuItemProjekt);
			}

			// PJ14139
			if (getInternalFrame().bRechtDarfPreiseSehenVerkauf == true) {
				JMenuItem menuItemVkpreisentwicklung = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.vkpreisentwicklung"));
				menuItemVkpreisentwicklung.addActionListener(this);
				menuItemVkpreisentwicklung.setActionCommand(MENUE_ACTION_VKPREISENTWICKLUNG);
				menuInfo.add(menuItemVkpreisentwicklung);
			}

			ParametermandantDto parameterABSNR = (ParametermandantDto) DelegateFactory.getInstance()
					.getParameterDelegate().getParametermandant(ParameterFac.PARAMETER_AUFTRAG_SERIENNUMMERN,
							ParameterFac.KATEGORIE_AUFTRAG, LPMain.getTheClient().getMandant());
			boolean bAuftragSnr = (Boolean) parameterABSNR.getCWertAsObject();

			if (bAuftragSnr) {
				JMenuItem menuItemAuftragsseriennummern = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.auftragsseriennummern"));
				menuItemAuftragsseriennummern.addActionListener(this);
				menuItemAuftragsseriennummern.setActionCommand(MENUE_ACTION_AUFTRAGSSERIENNUMMERN);
				menuInfo.add(menuItemAuftragsseriennummern);
			}

			JMenuItem menuItemAenderungen = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("artikel.info.report.aenderungen"));
			menuItemAenderungen.addActionListener(this);
			menuItemAenderungen.setActionCommand(MENUE_ACTION_AENDERUNGEN);
			menuInfo.add(menuItemAenderungen);

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_KUNDESONDERKONDITIONEN)) {

				JMenuItem menuItemKundensoksos = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.kundesokos"));
				menuItemKundensoksos.addActionListener(this);
				menuItemKundensoksos.setActionCommand(MENUE_ACTION_KUNDENSOKOS);
				menuInfo.add(menuItemKundensoksos);
			}

			JMenuItem menuItemSnrStammblatt = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("artikel.report.snrstammblatt"));
			menuItemSnrStammblatt.addActionListener(this);
			menuItemSnrStammblatt.setActionCommand(MENUE_ACTION_SNRSTAMMBLATT);
			menuInfo.add(menuItemSnrStammblatt);

			JMenuItem menuItemLieferantenpreisvergleich = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("artikel.report.lieferantenpreisvergleich"));
			menuItemLieferantenpreisvergleich.addActionListener(this);
			menuItemLieferantenpreisvergleich.setActionCommand(MENUE_ACTION_LIEFERANTENPREISVERGLEICH);
			menuInfo.add(menuItemLieferantenpreisvergleich);

			// usemenubar 7: Unbedingt diese Methode verwenden, um Menues dem
			// MenueBar
			// hinzuzufuegen:
			wrapperManuBar.addJMenuItem(menuInfo);

			// PJ14139
			// Mit WH besprochen: wenn eins der beiden Rechte felht, dann ist
			// der
			// Punkt Journal weg!
			if (getInternalFrame().bRechtDarfPreiseSehenEinkauf == true
					&& getInternalFrame().bRechtDarfPreiseSehenVerkauf == true) {

				// usemenubar 8: Wenn man in ein bestehendes System-Menue einen
				// Eintrag hinzufuegen
				// will, muss man diese Methode verwenden:
				JMenu journal = (JMenu) wrapperManuBar.getComponent(WrapperMenuBar.MENU_JOURNAL);
				JMenuItem menuItemLagerstandsListe = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.lagerstandsliste"));
				menuItemLagerstandsListe.addActionListener(this);
				menuItemLagerstandsListe.setActionCommand(MENUE_JOURNAL_ACTION_LAGERSTANDSLISTE);

				JMenuItem menuItemSeriennummernListe = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.seriennummern"));
				menuItemSeriennummernListe.addActionListener(this);
				menuItemSeriennummernListe.setActionCommand(MENUE_JOURNAL_ACTION_SERIENNUMMERN);

				journal.add(menuItemLagerstandsListe);

				WrapperMenuItem menuItemArtikelgruppen = new WrapperMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.artikelgruppen"),
						RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
				menuItemArtikelgruppen.addActionListener(this);
				menuItemArtikelgruppen.setActionCommand(MENUE_JOURNAL_ACTION_ARTIKELGRUPPEN);
				journal.add(menuItemArtikelgruppen);

				WrapperMenuItem menuItemShopgruppen = new WrapperMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.shopgruppen"),
						RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
				menuItemShopgruppen.addActionListener(this);
				menuItemShopgruppen.setActionCommand(MENUE_JOURNAL_ACTION_SHOPGRUPPEN);
				journal.add(menuItemShopgruppen);

				if (LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_CHARGENNUMMERN)
						|| LPMain.getInstance().getDesktop()
								.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_SERIENNUMMERN)) {

					journal.add(menuItemSeriennummernListe);
				}
				WrapperMenuItem menuItemHitliste = new WrapperMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.hitliste"),
						RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
				menuItemHitliste.addActionListener(this);
				menuItemHitliste.setActionCommand(MENUE_JOURNAL_ACTION_HITLISTE);

				journal.add(menuItemHitliste);

				ParametermandantDto parameter = null;
				try {
					parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
							.getParametermandant(
									ParameterFac.PARAMETER_CHARGENNUMMER_BEINHALTET_MINDESTHALTBARKEITSDATUM,
									ParameterFac.KATEGORIE_ARTIKEL, LPMain.getInstance().getTheClient().getMandant());
				} catch (Throwable ex1) {
					handleException(ex1, true);
				}

				if (parameter.getCWert() != null && !parameter.getCWert().equals("0")) {
					JMenuItem menuItemMindesthaltbarkeit = new JMenuItem(
							LPMain.getInstance().getTextRespectUISPr("artikel.journal.mindesthaltbarkeit"));
					menuItemMindesthaltbarkeit.addActionListener(this);
					menuItemMindesthaltbarkeit.setActionCommand(MENUE_JOURNAL_ACTION_MINDESTHALTBARKEIT);

					journal.add(menuItemMindesthaltbarkeit);

				}

				if (LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_RAHMENDETAILBEDARFE)) {

					// Rahmenbedarfe
					JMenuItem menuItemRahmendetailbedarfe = new JMenuItem(
							LPMain.getInstance().getTextRespectUISPr("artikel.report.offenerahmendetailbedarfe"));
					menuItemRahmendetailbedarfe.addActionListener(this);
					menuItemRahmendetailbedarfe.setActionCommand(MENUE_JOURNAL_ACTION_OFFENERAHMENBEDARFE);
					journal.add(menuItemRahmendetailbedarfe);
				}

				// Preisliste
				JMenuItem menuItemPreisliste = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.preisliste"));
				menuItemPreisliste.addActionListener(this);
				menuItemPreisliste.setActionCommand(MENUE_JOURNAL_ACTION_PREISLISTE);
				journal.add(menuItemPreisliste);

				JMenuItem menuItemLadenhueter = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.ladenhueter"));
				menuItemLadenhueter.addActionListener(this);
				menuItemLadenhueter.setActionCommand(MENUE_JOURNAL_ACTION_LADENHUETER);
				journal.add(menuItemLadenhueter);

				WrapperMenuItem menuItemGestpreisUnterMinVK = new WrapperMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.gestpreisueberminvk"),
						RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
				menuItemGestpreisUnterMinVK.addActionListener(this);
				menuItemGestpreisUnterMinVK.setActionCommand(MENUE_JOURNAL_ACTION_GESTPREISUEBERMINVK);
				journal.add(menuItemGestpreisUnterMinVK);

				JMenuItem menuItemWarenentnahmestatistik = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.warenentnahmestatistik"));
				menuItemWarenentnahmestatistik.addActionListener(this);
				menuItemWarenentnahmestatistik.setActionCommand(MENUE_JOURNAL_ACTION_WARENENTNAHMESTATISTIK);
				journal.add(menuItemWarenentnahmestatistik);

				JMenuItem menuItemMindestlagerstaende = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.mindestlagerstaende"));
				menuItemMindestlagerstaende.addActionListener(this);
				menuItemMindestlagerstaende.setActionCommand(MENUE_JOURNAL_ACTION_MINDESTLAGERSTAENDE);
				journal.add(menuItemMindestlagerstaende);

				JMenuItem menuItemNaechsteWartungen = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.naechstewartungen"));
				menuItemNaechsteWartungen.addActionListener(this);
				menuItemNaechsteWartungen.setActionCommand(MENUE_JOURNAL_ACTION_NAECHSTE_WARTUNGEN);
				journal.add(menuItemNaechsteWartungen);

				JMenuItem menuItemIndirektewareneinsatzstatistik = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.indirektewareneinsatzstatistik"));
				menuItemIndirektewareneinsatzstatistik.addActionListener(this);
				menuItemIndirektewareneinsatzstatistik
						.setActionCommand(MENUE_JOURNAL_ACTION_INDIREKTE_WARENEINSATZSTATISTIK);
				journal.add(menuItemIndirektewareneinsatzstatistik);

				JMenuItem menuItemArtikelOhneStklVerwendung = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.artikelohnestklverwendung"));
				menuItemArtikelOhneStklVerwendung.addActionListener(this);
				menuItemArtikelOhneStklVerwendung.setActionCommand(MENUE_JOURNAL_ACTION_ARTIKEL_OHNE_STKL_VERWENDUNG);
				journal.add(menuItemArtikelOhneStklVerwendung);

				JMenuItem menuItemAuftragswerte = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.auftragswerte"));
				menuItemAuftragswerte.addActionListener(this);
				menuItemAuftragswerte.setActionCommand(MENUE_JOURNAL_ACTION_AUFTRAGSWERTE);
				journal.add(menuItemAuftragswerte);

				// PJ20195
				WrapperMenuItem menuItemMaterialbedarfsvorschau = new WrapperMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.materialbedarfsvorschau"), null);
				menuItemMaterialbedarfsvorschau.addActionListener(this);
				menuItemMaterialbedarfsvorschau.setActionCommand(MENUE_JOURNAL_ACTION_MATERIALBEDARFSVORSCHAU);
				journal.add(menuItemMaterialbedarfsvorschau);

				if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_KUECHE)) {
					JMenuItem menuItemAlergene = new JMenuItem(
							LPMain.getInstance().getTextRespectUISPr("artikel.allergen"));
					menuItemAlergene.addActionListener(this);
					menuItemAlergene.setActionCommand(MENUE_JOURNAL_ACTION_ALERGENE);
					journal.add(menuItemAlergene);
				}
				JMenuItem menuItemMehrereEtiketten = new JMenuItem(
						LPMain.getTextRespectUISPr("artikel.report.multietikett"));
				menuItemMehrereEtiketten.addActionListener(this);
				menuItemMehrereEtiketten.setActionCommand(MENUE_JOURNAL_ACTION_MEHRERE_ETIKETTEN);
				journal.add(menuItemMehrereEtiketten);

				JMenuItem menuItemJournalBewegungsvorschau = new JMenuItem(
						LPMain.getTextRespectUISPr("bes.bewegungsvorschau"));
				menuItemJournalBewegungsvorschau.addActionListener(this);
				menuItemJournalBewegungsvorschau.setActionCommand(MENUE_JOURNAL_ACTION_BEWEGUNSVORSCHAU);
				journal.add(menuItemJournalBewegungsvorschau);

				JMenuItem menuItemMakeOrBuy = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.makeorbuy"));
				menuItemMakeOrBuy.addActionListener(this);
				menuItemMakeOrBuy.setActionCommand(MENUE_JOURNAL_ACTION_MAKEORBUY);
				journal.add(menuItemMakeOrBuy);

				if (LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_KUNDESONDERKONDITIONEN)) {

					JMenuItem menuItemKundensoksos = new JMenuItem(
							LPMain.getInstance().getTextRespectUISPr("artikel.report.kundesokos"));
					menuItemKundensoksos.addActionListener(this);
					menuItemKundensoksos.setActionCommand(MENUE_JOURNAL_ACTION_KUNDESONDERKONDITIONEN);
					journal.add(menuItemKundensoksos);
				}

			} else {
				// PJ20029
				JMenu journal = (JMenu) wrapperManuBar.getComponent(WrapperMenuBar.MENU_JOURNAL);
				JMenuItem menuItemSeriennummernListe = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.report.seriennummern"));
				menuItemSeriennummernListe.addActionListener(this);
				menuItemSeriennummernListe.setActionCommand(MENUE_JOURNAL_ACTION_SERIENNUMMERN);
				if (LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_CHARGENNUMMERN)
						|| LPMain.getInstance().getDesktop()
								.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_SERIENNUMMERN)) {

					journal.add(menuItemSeriennummernListe);
				}
			}
			boolean bDarfLagerprueffunktionensehen = false;

			try {
				bDarfLagerprueffunktionensehen = DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_WW_DARF_LAGERPRUEFFUNKTIONEN_SEHEN);
			} catch (Throwable ex) {
				handleException(ex, true);
			}

			if (bDarfLagerprueffunktionensehen) {

				JMenuItem menuItemPflegeVkpreise = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.title.panel.preise"));
				menuItemPflegeVkpreise.addActionListener(this);
				menuItemPflegeVkpreise.setActionCommand(MENUE_PFLEGE_VKPREISE);
				menuPflege.add(menuItemPflegeVkpreise);

				JMenuItem menuItemPruefeAendereSnrChnr = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("artikel.pflege.snrchnraendern"));
				menuItemPruefeAendereSnrChnr.addActionListener(this);
				menuItemPruefeAendereSnrChnr.setActionCommand(MENUE_PFLEGE_AENDERE_SNRCHNR);
				menuPflege.add(menuItemPruefeAendereSnrChnr);

				JMenuItem menuItemPaternoster = new JMenuItem("Paternoster hinzuf\u00FCgen");
				menuItemPaternoster.addActionListener(this);
				menuItemPaternoster.setActionCommand(MENUE_PFLEGE_PATERNOSTER);
				menuPflege.add(menuItemPaternoster);

				if (getInternalFrame().bRechtDarfPreiseSehenVerkauf == true) {
					JMenuItem menuItemExport = new JMenuItem(
							LPMain.getInstance().getTextRespectUISPr("artikel.preispflege.export"));
					menuItemExport.addActionListener(this);
					menuItemExport.setActionCommand(MENUE_ACTION_PREISPFLEGE_EXPORT);
					menuPflege.add(menuItemExport);
				}

				if (getInternalFrame().bRechtDarfPreiseAendernVerkauf == true) {
					JMenuItem menuItemExport = new JMenuItem(
							LPMain.getInstance().getTextRespectUISPr("artikel.preispflege.import"));
					menuItemExport.addActionListener(this);
					menuItemExport.setActionCommand(MENUE_ACTION_PREISPFLEGE_IMPORT);
					menuPflege.add(menuItemExport);

					JMenuItem menuItemPflegeVkStaffelImport = new JMenuItem(
							LPMain.getInstance().getTextRespectUISPr("artikel.vkmengenstaffeln.import"));
					menuItemPflegeVkStaffelImport.addActionListener(this);
					menuItemPflegeVkStaffelImport.setActionCommand(MENUE_PFLEGE_VKSTAFFEL_XLS_IMPORT);
					menuPflege.add(menuItemPflegeVkStaffelImport);

				}

				if (getInternalFrame().bRechtDarfPreiseAendernEinkauf == true) {
					JMenuItem menuItem = new JMenuItem(
							LPMain.getInstance().getTextRespectUISPr("artikel.wbzaktualisieren"));
					menuItem.addActionListener(this);
					menuItem.setActionCommand(MENUE_ACTION_WBZ_AKTUALISIEREN);
					menuPflege.add(menuItem);
				}

				if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_KUECHE)) {
					JMenuItem menuItemImportAllergene = new JMenuItem(
							LPMain.getInstance().getTextRespectUISPr("artikel.import.allergen"));
					menuItemImportAllergene.addActionListener(this);
					menuItemImportAllergene.setActionCommand(MENUE_PFLEGE_ALLERGENE);
					menuPflege.add(menuItemImportAllergene);
				}

				if (LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_4VENDING_SCHNITTSTELLE)) {
					JMenuItem menuItemExport4VendingArtikel = new JMenuItem(
							LPMain.getInstance().getTextRespectUISPr("artikel.export.menu.4vendingartikel"));
					menuItemExport4VendingArtikel.addActionListener(this);
					menuItemExport4VendingArtikel.setActionCommand(MENUE_PFLEGE_4VENDING_ARTIKEL_EXPORT);
					menuPflege.add(menuItemExport4VendingArtikel);
				}

				if (DelegateFactory.getInstance().getPanelDelegate()
						.panelbeschreibungVorhanden(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN)) {
					JMenuItem menuItemImportEigenschaften = new JMenuItem(
							LPMain.getInstance().getTextRespectUISPr("artikel.pflege.import.eigenschaften"));
					menuItemImportEigenschaften.addActionListener(this);
					menuItemImportEigenschaften.setActionCommand(MENUE_PFLEGE_EIGENSCHAFTEN_XLS_IMPORT);
					menuPflege.add(menuItemImportEigenschaften);
				}

				// ProFirst

				ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate()
						.getMandantparameter(LPMain.getInstance().getTheClient().getMandant(),
								ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_PRO_FIRST_DBURL);
				String dburl = parameter.getCWert();
				if (dburl != null && dburl.trim().length() > 0) {
					JMenuItem menuItemImportProFirst = new JMenuItem(
							LPMain.getInstance().getTextRespectUISPr("stk.profistimport"));
					menuItemImportProFirst.addActionListener(this);
					menuItemImportProFirst.setActionCommand(ACTION_SPECIAL_PROFIRST_IMPORT);
					menuPflege.add(menuItemImportProFirst);

					JMenuItem menuItemWEProFirst = new JMenuItem(
							LPMain.getInstance().getTextRespectUISPr("artikel.warenzugaengefuerprofirst"));
					menuItemWEProFirst.addActionListener(this);
					menuItemWEProFirst.setActionCommand(ACTION_SPECIAL_WARENZUGAENGE_FUER_PROFIRST);
					menuPflege.add(menuItemWEProFirst);
				}

				// PJ20640

				ParametermandantDto parameterChrnWepos = (ParametermandantDto) DelegateFactory.getInstance()
						.getParameterDelegate()
						.getParametermandant(ParameterFac.PARAMETER_AUTOMATISCHE_CHARGENNUMMER_BEI_WEP,
								ParameterFac.KATEGORIE_BESTELLUNG, LPMain.getInstance().getTheClient().getMandant());

				int automatischeChargennummerBeiWEPOS = (Integer) parameterChrnWepos.getCWertAsObject();

				if (automatischeChargennummerBeiWEPOS == 1) {
					JMenuItem menuItemAuto = new JMenuItem(LPMain.getInstance()
							.getTextRespectUISPr("artikel.pflege.chargennummern.anhandwepaktualisieren"));
					menuItemAuto.addActionListener(this);
					menuItemAuto.setActionCommand(ACTION_SPECIAL_CHARGENNUMMER_ANHAND_WEP_AKTUALISIEREN);
					menuPflege.add(menuItemAuto);
				}

				wrapperManuBar.addJMenuItem(menuPflege);

			}

			// dann bekommt man das gewuenschte Menue zurueck und kann diesem
			// einen
			// Eintrag hinzufuegen. Um das Menueitem nicht an letzter Stelle
			// hunzuzufuegen, kann man bei
			// journal.add(..) auch die Stelle mitangeben
		}
		return wrapperManuBar;
	}

	protected boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;
		int code = exfc.getICode();

		switch (code) {
		case EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT:
			DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
					LPMain.getInstance().getTextRespectUISPr("auft.mandant.hauptlager_fehlt"));
			break;

		default:
			bErrorErkannt = false;
			break;
		}

		return bErrorErkannt;
	}

	private boolean hatZusatzfunktionEinkaufsEAN() {
		return LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_EINKAUFS_EAN);
	}

	private boolean hatBelegartKueche() {
		return LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_KUECHE);
	}

	protected PanelQuery getPanelQueryArtikellieferant() throws Throwable {
		createArtikellieferant(getInternalFrameArtikel().getArtikelDto().getIId());
		return panelQueryArtikellieferant;
	}
}
