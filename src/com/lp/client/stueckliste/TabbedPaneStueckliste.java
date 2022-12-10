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
package com.lp.client.stueckliste;

import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.angebotstkl.AngebotstklFilterFactory;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.DialogArtikelkopieren;
import com.lp.client.artikel.DialogNeueArtikelnummer;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.ICopyPaste;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.assistent.view.AssistentView;
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
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.open.CsvFile;
import com.lp.client.frame.filechooser.open.DirectoryFile;
import com.lp.client.frame.filechooser.open.FileOpenerFactory;
import com.lp.client.frame.filechooser.open.XlsFile;
import com.lp.client.frame.filechooser.open.XlsFileOpener;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.importassistent.StklImportController;
import com.lp.client.system.ReportEntitylog;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.PruefkombinationDto;
import com.lp.server.stueckliste.service.StrukturierterImportDto;
import com.lp.server.stueckliste.service.StrukturierterImportSiemensNXDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.HvOptional;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.service.POSDocument2POSDto;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.csv.LPCSVReader;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

public class TabbedPaneStueckliste extends TabbedPane implements ICopyPaste {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryStueckliste = null;
	private PanelStueckliste panelDetailStueckliste = null;

	private PanelQuery panelQueryPositionen = null;
	private PanelStuecklistepositionen panelBottomPositionen = null;
	private PanelSplit panelSplitPositionen = null;

	private PanelQuery panelQueryPositionenErsatz = null;
	private PanelStuecklistepositionenErsatz panelBottomPositionenErsatz = null;
	private PanelSplit panelSplitPositionenErsatz = null;

	private PanelQuery panelQueryEigenschaft = null;
	private PanelBasis panelBottomEigenschaft = null;
	private PanelSplit panelSplitEigenschaft = null;

	private PanelQuery panelQueryParameter = null;
	private PanelBasis panelBottomParameter = null;
	private PanelSplit panelSplitParameter = null;

	private PanelQueryFLR panelQueryFLRBevorzugterArtikel = null;

	private PanelQuery panelQueryArbeitsplan = null;

	public PanelQuery getPanelQueryArbeitsplan() {
		return panelQueryArbeitsplan;
	}

	private PanelBasis panelBottomArbeitsplan = null;
	private PanelSplit panelSplitArbeitsplan = null;

	private PanelQuery panelQueryPruefplan = null;
	private PanelStklpruefplan panelBottomPruefplan = null;
	private PanelSplit panelSplitPruefplan = null;

	private PanelQuery panelQueryAbbuchungslager = null;
	private PanelBasis panelBottomAbbuchungslager = null;
	private PanelSplit panelSplitAbbuchungslager = null;

	public static int IDX_PANEL_AUSWAHL = -1;
	public static int IDX_PANEL_DETAIL = -1;
	public static int IDX_PANEL_POSITIONEN = -1;
	public static int IDX_PANEL_PRUEFPLAN = -1;
	public static int IDX_PANEL_POSITIONENERSATZ = -1;
	public static int IDX_PANEL_ARBEITSPLAN = -1;
	public static int IDX_PANEL_ALTERNATIVMASCHINE = -1;
	public static int IDX_LAGERENTNAHME = -1;
	public static int IDX_PANEL_EIGENSCHAFTEN = -1;
	public static int IDX_PANEL_PARAMETER = -1;

	private PanelQuery panelQueryAlternativmaschine = null;
	private PanelAlternativmaschine panelBottomAlternativmaschine = null;
	private PanelSplit panelSplitAlternativmaschine = null;

	public boolean bStuecklistenfreigabe = false;

	boolean bDarfSollzeitenAendern = false;

	public boolean bReferenznummerInPositonen = false;

	private boolean bPositionen = true;

	private PanelDialogStuecklisteKommentar pdStuecklisteKommentar = null;

	private WrapperMenuBar wrapperManuBar = null;

	private JMenuItem menuItemKonfigurieren = null;

	private final String MENU_INFO_ARBEITSPLAN = "MENU_INFO_ARBEITSPLAN";
	private final String MENU_INFO_GESAMTKALKULATION = "MENU_INFO_GESAMTKALKULATION";
	private final String MENU_INFO_AENDERUNGEN = "MENU_INFO_AENDERUNGEN";
	private final String MENU_INFO_PRUEFPLAN = "MENU_INFO_PRUEFPLAN";
	private final String MENU_INFO_FREIGABE = "MENU_INFO_FREIGABE";
	private final String MENU_INFO_MINDERVERFUEGBARKEIT = "MENU_INFO_MINDERVERFUEGBARKEIT";
	private final String MENU_INFO_WAFFENREGISTER = "MENU_INFO_WAFFENREGISTER";

	private final String MENU_XLSIMPORT_ARBEITSPLAN = "MENU_XLSIMPORT_ARBEITSPLAN";
	private final String MENU_XLSIMPORT_MATERIAL = "MENU_XLSIMPORT_MATERIAL";

	private final String MENUE_INVENTUR_ACTION_STUECKLISTE = "MENUE_INVENTUR_ACTION_STUECKLISTE";
	private final String MENU_BEARBEITEN_KOMMENTAR = "MENU_BEARBEITEN_KOMMENTAR";
	private final String MENU_BEARBEITEN_LOSE_AKTUALISIEREN = "MENU_BEARBEITEN_LOSE_AKTUALISIEREN";
	private final String MENU_BEARBEITEN_STUECKLISTE_AUS_PROFIRST_AKTUALISIEREN = "MENU_BEARBEITEN_STUECKLISTE_AUS_PROFIRST_AKTUALISIEREN";
	private final String MENU_BEARBEITEN_KONFIGURIEREN = "MENU_BEARBEITEN_KONFIGURIEREN";
	private final String MENU_BEARBEITEN_UPDATE_SOLLZEITEN_ANHAND_ISTZEITEN = "MENU_BEARBEITEN_UPDATE_SOLLZEITEN_ANHAND_ISTZEITEN";

	private final String MENUE_JOURNAL_ACTION_REICHWEITE = "MENUE_JOURNAL_ACTION_REICHWEITE";
	private final String MENU_JOURNAL_VERSCHLEISSTEILVERWENDUNG = "MENU_JOURNAL_VERSCHLEISSTEILVERWENDUNG";

	private final String MENU_BEARBEITEN_ARTIKEL_ERSETZEN = "MENU_BEARBEITEN_ARTIKEL_ERSETZEN";
	private final String MENU_BEARBEITEN_BEVORZUGTEN_ARTIKEL_EINTRAGEN = "MENU_BEARBEITEN_BEVORZUGTEN_ARTIKEL_EINTRAGEN";
	private final String MENU_BEARBEITEN_MASCHINE_ERSETZEN = "MENU_BEARBEITEN_MASCHINE_ERSETZEN";
	private final String ACTION_SPECIAL_BAUMSICHT = PanelBasis.ALWAYSENABLED + "action_special_baumsicht";

	private final String ACTION_SPECIAL_PARAMETER_KOPIEREN = "ACTION_SPECIAL_PARAMETER_KOPIEREN";
	private final String BUTTON_PARAMETER_KOPIEREN = PanelBasis.ACTION_MY_OWN_NEW + ACTION_SPECIAL_PARAMETER_KOPIEREN;

	private final String BUTTON_SORTIERE_NACH_ARTIKELNUMMER = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_SORTIERE_NACH_ARTIKELNUMMER";

	private final String BUTTON_AG_NEU_NUMMERIEREN = PanelBasis.ACTION_MY_OWN_NEW + "ACTION_SPECIAL_AG_NEU_NUMMERIEREN";

	private static final String ACTION_SPECIAL_IMPORT = "action_special_import";
	private static final String ACTION_SPECIAL_ARBEITSPLAN = "action_special_import_arbeitsplan";
	private static final String ACTION_SPECIAL_IMPORT_AGSTKL = "action_special_import_agstkl";

	private static final String ACTION_SPECIAL_CSVIMPORT_POSITIONEN = "ACTION_SPECIAL_CSVIMPORT_POSITIONEN";
	private static final String ACTION_SPECIAL_CSVIMPORT_ARBEITSPLAN = "ACTION_SPECIAL_CSVIMPORT_ARBEITSPLAN";
	private final String MENUE_ACTION_CSVIMPORT = "MENUE_ACTION_CSVIMPORT";

	private final String BUTTON_IMPORTSTUECKLISTEPOSITIONEN_XLS = PanelBasis.ACTION_MY_OWN_NEW
			+ "IMPORTSTUECKLISTEPOSITIONEN_XLS";

	private static final String ACTION_PRUEFPLAN_ERZEUGEN = "action_special_pruefplanerzeguen";
	private final String BUTTON_PRUEFPLAN_ERZEUGEN = PanelBasis.LEAVEALONE + ACTION_PRUEFPLAN_ERZEUGEN;

	private final String MENUE_ACTION_CREO_IMPORT = "MENUE_ACTION_CREO_IMPORT";

	private final String BUTTON_IMPORTSTUECKLISTEPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW + ACTION_SPECIAL_IMPORT;
	private final String BUTTON_IMPORTAGSTKLPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW + ACTION_SPECIAL_IMPORT_AGSTKL;
	private final String BUTTON_IMPORTSTUECKLISTEARBEITSPLAN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_ARBEITSPLAN;
	private final String BUTTON_IMPORTCSV_STUECKLISTEPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_CSVIMPORT_POSITIONEN;
	private final String BUTTON_IMPORTCSV_STUECKLISTEARBEISTPLAN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_CSVIMPORT_ARBEITSPLAN;

	private static final String MENU_XLSIMPORT_PRUEFPLAN = "MENU_XLSIMPORT_PRUEFPLAN";

	private final String EXTRA_NEU_AUS_STUECKLISTE = "neu_aus_stueckliste";

	private final static String MENUE_ACTION_INFO_VERGLEICH_MIT_ANDERER_STUECKLISTE = "menu_action_vergleich_mit_anderer_stueckliste";

	private final String MENU_BEARBEITEN_HANDARTIKEL_UMANDELN = "MENU_BEARBEITEN_HANDARTIKEL_UMANDELN";

	private PanelQueryFLR panelAgstkl = null;
	private PanelQueryFLR panelStueckliste = null;

	private PanelQueryFLR panelStuecklisteFuerParameter = null;

	private PanelQueryFLR panelQueryFLRPruefkombination = null;

	private ISheetImportController sheetImportController = null;

	public int iStrukturierterStklImport = 0;

	String rechtVorher = getInternalFrame().getRechtModulweit();

	public TabbedPaneStueckliste(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("stkl.stueckliste"));

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_STRUKTURIERTER_STKLIMPORT,
						ParameterFac.KATEGORIE_STUECKLISTE, LPMain.getTheClient().getMandant());

		if (parameter.getCWertAsObject() != null) {
			iStrukturierterStklImport = (Integer) parameter.getCWertAsObject();
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_STUECKLISTENFREIGABE)) {
			bStuecklistenfreigabe = true;
		}

		boolean bDarfSollzeitenAendern = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_STK_SOLLZEITEN_FREIGEGEBENE_STK_CUD);

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_REFERENZNUMMER_IN_POSITIONEN, ParameterFac.KATEGORIE_ALLGEMEIN,
				LPMain.getTheClient().getMandant());
		bReferenznummerInPositonen = (Boolean) parameter.getCWertAsObject();

		jbInit();
		initComponents();

		// TODO: Sollte besser uebergeben werden
		setSheetImportController(new SheetImportController());
	}

	public PanelQuery getPanelQueryPersonal() {
		return panelQueryStueckliste;
	}

	public PanelQuery getPanelQueryPositionen() {
		return panelQueryPositionen;
	}

	public ISheetImportController getSheetImportController() {
		return sheetImportController;
	}

	public void setSheetImportController(ISheetImportController sheetImportController) {
		this.sheetImportController = sheetImportController;
	}

	private void createAuswahl() throws Throwable {
		if (panelQueryStueckliste == null) {

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryStueckliste = new PanelQuery(null,
					StuecklisteFilterFactory.getInstance().createStuecklisteMandantCNr(),
					QueryParameters.UC_ID_STUECKLISTE, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.auswahl"), true,
					StuecklisteFilterFactory.getInstance().createFKVStuecklisteArtikel(), null);
			panelQueryStueckliste.befuellePanelFilterkriterienDirekt(
					StuecklisteFilterFactory.getInstance().createFKDArtikelnummer(),
					StuecklisteFilterFactory.getInstance().createFKDTextSucheStkl());

			panelQueryStueckliste.addDirektFilter(StuecklisteFilterFactory.getInstance().createFKDKundeName());
			panelQueryStueckliste.addDirektFilter(StuecklisteFilterFactory.getInstance().createFKDTextSuchen());
			panelQueryStueckliste.setFilterComboBox(
					DelegateFactory.getInstance().getStuecklisteDelegate().getAllFertigungsgrupe(),
					new FilterKriterium("stueckliste.fertigungsgruppe_i_id", true, "" + "",
							FilterKriterium.OPERATOR_EQUAL, false),
					false, LPMain.getTextRespectUISPr("lp.alle"), false);
			panelQueryStueckliste.getWcoFilter().setToolTipText(LPMain.getTextRespectUISPr("stkl.fertigungsgruppe"));

			panelQueryStueckliste.setFilterComboBox2(
					DelegateFactory.getInstance().getArtikelDelegate().getAllSprArtgru(),
					new FilterKriterium("stueckliste.flrartikel.flrartikelgruppe.i_id", true, "" + "",
							FilterKriterium.OPERATOR_IN, false),
					false, textFromToken("lp.alle"), false);
			panelQueryStueckliste.getWcoFilter2().setToolTipText(LPMain.getTextRespectUISPr("lp.artikelgruppe"));

			panelQueryStueckliste.createAndSaveAndShowButton("/com/lp/client/res/branch_view.png",
					textFromToken("part.kunde.struktur"), ACTION_SPECIAL_BAUMSICHT, null);

			boolean bBenutzerIstInMandantensprechAngemeldet = false;
			if (LPMain.getTheClient().getLocMandantAsString().equals(LPMain.getTheClient().getLocUiAsString())) {
				bBenutzerIstInMandantensprechAngemeldet = true;
			}
			if (bBenutzerIstInMandantensprechAngemeldet) {
				// Hier den zusaetzlichen Button aufs Panel bringen
				panelQueryStueckliste.createAndSaveAndShowButton("/com/lp/client/res/text_code_colored16x16.png",
						textFromToken("stkl.stkl_kopieren"), PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_STUECKLISTE,
						null);
			}

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryStueckliste);
		}
	}

	private void createDetail(Integer key) throws Throwable {
		if (panelDetailStueckliste == null) {
			panelDetailStueckliste = new PanelStueckliste(getInternalFrame(), textFromToken("lp.kopfdaten"), key);
			setComponentAt(IDX_PANEL_DETAIL, panelDetailStueckliste);
		}
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperManuBar == null) {

			boolean bSchreibrecht = false;
			if (getInternalFrame().getRechtModulweit().equals(RechteFac.RECHT_MODULWEIT_UPDATE)) {
				bSchreibrecht = true;
			}

			wrapperManuBar = new WrapperMenuBar(this);

			JMenuItem menuItemModul = new JMenuItem(textFromToken("lp.drucken") + "...");

			JMenuItem menuItemArbeitsplan = new JMenuItem(textFromToken("stkl.arbeitsplan"));
			menuItemArbeitsplan.addActionListener(this);
			menuItemArbeitsplan.setActionCommand(MENU_INFO_ARBEITSPLAN);

			menuItemModul.addActionListener(this);
			menuItemModul.setActionCommand(MENUE_INVENTUR_ACTION_STUECKLISTE);

			JMenu stkimport = new JMenu(textFromToken("lp.import"));

			JMenu modul = (JMenu) wrapperManuBar.getComponent(WrapperMenuBar.MENU_MODUL);
			modul.add(new JSeparator(), 0);
			modul.add(menuItemModul, 0);
			modul.add(menuItemArbeitsplan, 1);
			if (bSchreibrecht) {
				modul.add(stkimport, 2);
			}

			JMenuItem menuItemImportMaterial = new JMenuItem(textFromToken("stkl.title.panel.positionen"));
			menuItemImportMaterial.addActionListener(this);
			menuItemImportMaterial.setActionCommand(MENU_XLSIMPORT_MATERIAL);
			stkimport.add(menuItemImportMaterial);

			JMenuItem menuItemImportArbeitsplan = new JMenuItem(textFromToken("stkl.arbeitsplan"));
			menuItemImportArbeitsplan.addActionListener(this);
			menuItemImportArbeitsplan.setActionCommand(MENU_XLSIMPORT_ARBEITSPLAN);
			stkimport.add(menuItemImportArbeitsplan);

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PRUEFPLAN1)) {
				JMenuItem menuItemImportPruefplan = new JMenuItem(textFromToken("stk.pruefplan"));
				menuItemImportPruefplan.addActionListener(this);
				menuItemImportPruefplan.setActionCommand(MENU_XLSIMPORT_PRUEFPLAN);
				stkimport.add(menuItemImportPruefplan);
			}

			if (iStrukturierterStklImport != 0) {
				// CSV-Import

				String text = textFromToken("lp.import");

				if (iStrukturierterStklImport == 1) {
					text = "Solid-Works- " + text;
				}
				if (iStrukturierterStklImport == 2) {
					text = "Siemens NX- " + text;
				}
				if (iStrukturierterStklImport == 3) {
					text = "INFRA- " + text;
				}

				JMenuItem menuItemCsvImport = new JMenuItem(text);
				menuItemCsvImport.addActionListener(this);
				menuItemCsvImport.setActionCommand(MENUE_ACTION_CSVIMPORT);
				stkimport.add(menuItemCsvImport);
			}

			JMenuItem menuItemImportCreo = new JMenuItem(LPMain.getInstance().getTextRespectUISPr("stkl.import.creo"));
			menuItemImportCreo.addActionListener(this);
			menuItemImportCreo.setActionCommand(MENUE_ACTION_CREO_IMPORT);
			stkimport.add(menuItemImportCreo);

			JMenu jmBearbeiten = (JMenu) wrapperManuBar.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

			// Produktkonfigurator

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_STUECKLISTE_MIT_FORMELN)) {
				menuItemKonfigurieren = new JMenuItem(textFromToken("stkl.menu.konfigurieren"));
				menuItemKonfigurieren.addActionListener(this);
				menuItemKonfigurieren.setActionCommand(MENU_BEARBEITEN_KONFIGURIEREN);
				jmBearbeiten.add(menuItemKonfigurieren, 0);

			}
			// ProFirst

			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_PRO_FIRST_DBURL);
			String dburl = parameter.getCWert();
			if (dburl != null && dburl.trim().length() > 0) {
				JMenuItem menuItemImportProFirst = new JMenuItem(textFromToken("stkl.menu.profirstaktualisieren"));
				menuItemImportProFirst.addActionListener(this);
				menuItemImportProFirst.setActionCommand(MENU_BEARBEITEN_STUECKLISTE_AUS_PROFIRST_AKTUALISIEREN);
				jmBearbeiten.add(menuItemImportProFirst, 0);

			}

			JMenuItem menuItemBearbeitenLoseAktualisieren = new JMenuItem(
					textFromToken("stkl.bearbeiten.loseaktualisieren"));
			menuItemBearbeitenLoseAktualisieren.addActionListener(this);
			menuItemBearbeitenLoseAktualisieren.setActionCommand(MENU_BEARBEITEN_LOSE_AKTUALISIEREN);
			jmBearbeiten.add(menuItemBearbeitenLoseAktualisieren, 0);
			if (bSchreibrecht) {

				JMenuItem menuItemBearbeitenSollzeitenAktualisieren = new JMenuItem(
						textFromToken("stkl.bearbeiten.sollzeitenanhand.losistzeitenaktialisieren"));
				menuItemBearbeitenSollzeitenAktualisieren.addActionListener(this);
				menuItemBearbeitenSollzeitenAktualisieren
						.setActionCommand(MENU_BEARBEITEN_UPDATE_SOLLZEITEN_ANHAND_ISTZEITEN);
				jmBearbeiten.add(menuItemBearbeitenSollzeitenAktualisieren, 0);

				JMenuItem menuItemHandartikelUmwandeln = new JMenuItem(
						textFromToken("angebot.bearbeiten.handartikelumwandeln"));
				menuItemHandartikelUmwandeln.addActionListener(this);
				menuItemHandartikelUmwandeln.setActionCommand(MENU_BEARBEITEN_HANDARTIKEL_UMANDELN);
				jmBearbeiten.add(menuItemHandartikelUmwandeln, 0);

				if (LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MASCHINENZEITERFASSUNG)) {
					JMenuItem menuItemBearbeitenMaschineErsetzen = new JMenuItem(
							textFromToken("stkl.maschine.ersetzendurch"));
					menuItemBearbeitenMaschineErsetzen.addActionListener(this);
					menuItemBearbeitenMaschineErsetzen.setActionCommand(MENU_BEARBEITEN_MASCHINE_ERSETZEN);
					jmBearbeiten.add(menuItemBearbeitenMaschineErsetzen, 0);
				}

				if (LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ERSATZTYPENVERWALTUNG)) {

					JMenuItem menuItemBearbeitenBevorzugtenArtikelEintragen = new JMenuItem(
							textFromToken("stkl.bevorzugten.artikel.eintragen"));
					menuItemBearbeitenBevorzugtenArtikelEintragen.addActionListener(this);
					menuItemBearbeitenBevorzugtenArtikelEintragen
							.setActionCommand(MENU_BEARBEITEN_BEVORZUGTEN_ARTIKEL_EINTRAGEN);
					jmBearbeiten.add(menuItemBearbeitenBevorzugtenArtikelEintragen, 0);
				}

				JMenuItem menuItemBearbeitenArtikelErsetzen = new JMenuItem(
						textFromToken("stkl.artikel.ersetztendurch"));
				menuItemBearbeitenArtikelErsetzen.addActionListener(this);
				menuItemBearbeitenArtikelErsetzen.setActionCommand(MENU_BEARBEITEN_ARTIKEL_ERSETZEN);
				jmBearbeiten.add(menuItemBearbeitenArtikelErsetzen, 0);

			}

			JMenuItem menuItemBearbeitenInternerKommentar = new JMenuItem(textFromToken("lp.kommentar"));
			menuItemBearbeitenInternerKommentar.addActionListener(this);
			menuItemBearbeitenInternerKommentar.setActionCommand(MENU_BEARBEITEN_KOMMENTAR);
			jmBearbeiten.add(menuItemBearbeitenInternerKommentar, 0);

			jmBearbeiten.add(new JSeparator(), 0);

			JMenuItem menuItemGesamtkalkulation = new JMenuItem(textFromToken("stkl.gesamtkalkulation"));
			menuItemGesamtkalkulation.addActionListener(this);
			menuItemGesamtkalkulation.setActionCommand(MENU_INFO_GESAMTKALKULATION);

			JMenu menuInfo = new WrapperMenu("lp.info", this);

			menuInfo.add(menuItemGesamtkalkulation);

			JMenuItem menuItemMinderverfuegbarkeit = new JMenuItem(textFromToken("stkl.minderverfuegbarkeit"));
			menuItemMinderverfuegbarkeit.addActionListener(this);
			menuItemMinderverfuegbarkeit.setActionCommand(MENU_INFO_MINDERVERFUEGBARKEIT);
			menuInfo.add(menuItemMinderverfuegbarkeit);

			JMenuItem menuItemInfoVergleich = new JMenuItem(
					LPMain.getTextRespectUISPr("stkl.report.vergleichmitandererstueckliste"));
			menuItemInfoVergleich.addActionListener(this);
			menuItemInfoVergleich.setActionCommand(MENUE_ACTION_INFO_VERGLEICH_MIT_ANDERER_STUECKLISTE);
			menuInfo.add(menuItemInfoVergleich);

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_WAFFENREGISTER)) {

				JMenuItem menuItemInfoWaffenregister = new JMenuItem(
						LPMain.getTextRespectUISPr("artikel.waffenregister"));
				menuItemInfoWaffenregister.addActionListener(this);
				menuItemInfoWaffenregister.setActionCommand(MENU_INFO_WAFFENREGISTER);
				menuInfo.add(menuItemInfoWaffenregister);
			}

			JMenuItem menuItemAenderungen = new JMenuItem(textFromToken("lp.report.aenderungen"));
			menuItemAenderungen.addActionListener(this);
			menuItemAenderungen.setActionCommand(MENU_INFO_AENDERUNGEN);
			menuInfo.add(menuItemAenderungen);

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PRUEFPLAN1)) {

				JMenuItem menuItemPruefplan = new JMenuItem(textFromToken("stk.pruefplan"));
				menuItemPruefplan.addActionListener(this);
				menuItemPruefplan.setActionCommand(MENU_INFO_PRUEFPLAN);
				menuInfo.add(menuItemPruefplan);
			}
			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ARTIKELFREIGABE)) {

				JMenuItem menuItemFreigabe = new JMenuItem(textFromToken("stk.report.freigabe"));
				menuItemFreigabe.addActionListener(this);
				menuItemFreigabe.setActionCommand(MENU_INFO_FREIGABE);
				menuInfo.add(menuItemFreigabe);
			}
			wrapperManuBar.addJMenuItem(menuInfo);

			JMenu journal = (JMenu) wrapperManuBar.getComponent(WrapperMenuBar.MENU_JOURNAL);
			JMenuItem menuItemReichweite = new JMenuItem(textFromToken("stkl.report.reichweite"));
			menuItemReichweite.addActionListener(this);
			menuItemReichweite.setActionCommand(MENUE_JOURNAL_ACTION_REICHWEITE);
			journal.add(menuItemReichweite);

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PRUEFPLAN1)) {

				JMenuItem menuItemVerschleissteilverwendung = new JMenuItem(
						textFromToken("stk.verschleissteilverwendung"));
				menuItemVerschleissteilverwendung.addActionListener(this);
				menuItemVerschleissteilverwendung.setActionCommand(MENU_JOURNAL_VERSCHLEISSTEILVERWENDUNG);
				journal.add(menuItemVerschleissteilverwendung);
			}

		}
		return wrapperManuBar;

	}

	private void refreshPositionen(Integer iIdI) throws Throwable {

		if (panelQueryPositionen == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1, PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					PanelBasis.ACTION_KOPIEREN, PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };

			panelQueryPositionen = new PanelQuery(null,
					StuecklisteFilterFactory.getInstance().createFKStueckliste(iIdI),
					QueryParameters.UC_ID_STUECKLISTEPOSITION, aWhichStandardButtonIUse, getInternalFrame(),
					textFromToken("stkl.title.panel.positionen"), true);
			panelBottomPositionen = new PanelStuecklistepositionen(getInternalFrame(),
					textFromToken("stkl.title.panel.positionen"), null);

			panelQueryPositionen.createAndSaveAndShowButton("/com/lp/client/res/text_code_colored16x16.png",
					textFromToken("stkl.positionen.importausandererstueckliste"), BUTTON_IMPORTSTUECKLISTEPOSITIONEN,
					null);

			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AGSTUECKLISTE)) {

				panelQueryPositionen.createAndSaveAndShowButton("/com/lp/client/res/note_add16x16.png",
						textFromToken("as.positionen.importausandererstueckliste"), BUTTON_IMPORTAGSTKLPOSITIONEN,
						null);
			}

			boolean intelligenterStklImport = LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_INTELLIGENTER_STUECKLISTENIMPORT);

			panelQueryPositionen.createAndSaveAndShowButton("/com/lp/client/res/document_into.png",
					textFromToken(
							intelligenterStklImport ? "stkl.intelligenterstklimport" : "stkl.positionen.cvsimport"),
					BUTTON_IMPORTCSV_STUECKLISTEPOSITIONEN, null);

			ArbeitsplatzparameterDto parameter = DelegateFactory.getInstance().getParameterDelegate()
					.holeArbeitsplatzparameter(
							ParameterFac.ARBEITSPLATZPARAMETER_DATEI_AUTOMATISCHE_STUECKLISTENERZEUGUNG);

			if (parameter != null && parameter.getCWert() != null && parameter.getCWert().trim().length() > 0) {

				panelQueryPositionen.createAndSaveAndShowButton("/com/lp/client/res/document_gear.png", "XLS_IMPORT",
						BUTTON_IMPORTSTUECKLISTEPOSITIONEN_XLS, null);
			}

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PRUEFPLAN1)) {
				panelQueryPositionen.createAndSaveAndShowButton("/com/lp/client/res/document_gear.png",
						"Pruefplan erzeugen", BUTTON_PRUEFPLAN_ERZEUGEN, null);
			}

			panelQueryPositionen.createAndSaveAndShowButton("/com/lp/client/res/sort_az_descending.png",
					LPMain.getTextRespectUISPr("ls.sortierenachartikelnummer"), BUTTON_SORTIERE_NACH_ARTIKELNUMMER,
					null);

			panelQueryPositionen.setMultipleRowSelectionEnabled(true);

			panelSplitPositionen = new PanelSplit(getInternalFrame(), panelBottomPositionen, panelQueryPositionen, 210);

			setComponentAt(IDX_PANEL_POSITIONEN, panelSplitPositionen);
		} else {
			// filter refreshen.
			panelQueryPositionen.setDefaultFilter(StuecklisteFilterFactory.getInstance().createFKStueckliste(iIdI));
		}
	}

	private void refreshPositionenErsatz(Integer iIdI) throws Throwable {

		if (panelQueryPositionenErsatz == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1, PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN };

			panelQueryPositionenErsatz = new PanelQuery(null,
					StuecklisteFilterFactory.getInstance().createFKStuecklistePosersatz(iIdI),
					QueryParameters.UC_ID_POSERSATZ, aWhichStandardButtonIUse, getInternalFrame(),
					textFromToken("stkl.positionen.ersatz"), true);
			panelBottomPositionenErsatz = new PanelStuecklistepositionenErsatz(getInternalFrame(),
					textFromToken("stkl.positionen.ersatz"), null);

			panelSplitPositionenErsatz = new PanelSplit(getInternalFrame(), panelBottomPositionenErsatz,
					panelQueryPositionenErsatz, 240);

			setComponentAt(IDX_PANEL_POSITIONENERSATZ, panelSplitPositionenErsatz);
		} else {
			// filter refreshen.
			panelQueryPositionenErsatz
					.setDefaultFilter(StuecklisteFilterFactory.getInstance().createFKStuecklistePosersatz(iIdI));
		}
	}

	private void refreshAlternativmaschine(Integer iIdI) throws Throwable {

		if (panelQueryAlternativmaschine == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			panelQueryAlternativmaschine = new PanelQuery(null,
					StuecklisteFilterFactory.getInstance().createFKAlternativmaschine(iIdI),
					QueryParameters.UC_ID_ALTERNATIVMASCHINE, aWhichStandardButtonIUse, getInternalFrame(),
					textFromToken("stkl.alternativmaschine"), true);
			panelBottomAlternativmaschine = new PanelAlternativmaschine(getInternalFrame(),
					textFromToken("stkl.alternativmaschine"), null);

			panelSplitAlternativmaschine = new PanelSplit(getInternalFrame(), panelBottomAlternativmaschine,
					panelQueryAlternativmaschine, 240);

			setComponentAt(IDX_PANEL_ALTERNATIVMASCHINE, panelSplitAlternativmaschine);
		} else {
			// filter refreshen.
			panelQueryAlternativmaschine
					.setDefaultFilter(StuecklisteFilterFactory.getInstance().createFKAlternativmaschine(iIdI));
		}
	}

	private void refreshPruefplan(Integer stuecklisteIId) throws Throwable {

		if (panelQueryPruefplan == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			panelQueryPruefplan = new PanelQuery(null,
					StuecklisteFilterFactory.getInstance().createFKPruefplan(stuecklisteIId),
					QueryParameters.UC_ID_STKLPRUEFPLAN, aWhichStandardButtonIUse, getInternalFrame(),
					textFromToken("stk.pruefplan"), true);
			panelBottomPruefplan = new PanelStklpruefplan(getInternalFrame(), textFromToken("stk.pruefplan"), null);

			panelSplitPruefplan = new PanelSplit(getInternalFrame(), panelBottomPruefplan, panelQueryPruefplan, 240);

			setComponentAt(IDX_PANEL_PRUEFPLAN, panelSplitPruefplan);
		} else {
			// filter refreshen.
			panelQueryPruefplan
					.setDefaultFilter(StuecklisteFilterFactory.getInstance().createFKPruefplan(stuecklisteIId));
		}
	}

	private void refreshArbeitsplan(Integer key) throws Throwable {

		if (panelQueryArbeitsplan == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };
			panelQueryArbeitsplan = new PanelQuery(null,
					StuecklisteFilterFactory.getInstance().createFKStueckliste(key),
					QueryParameters.UC_ID_STUECKLISTEARBEITSPLAN, aWhichStandardButtonIUse, getInternalFrame(),
					textFromToken("stkl.arbeitsplan"), true);

			panelQueryArbeitsplan.createAndSaveAndShowButton("/com/lp/client/res/text_code_colored16x16.png",
					textFromToken("stkl.positionen.arbeitsplanimportausandererstueckliste"),
					BUTTON_IMPORTSTUECKLISTEARBEITSPLAN, null);

			panelQueryArbeitsplan.createAndSaveAndShowButton("/com/lp/client/res/document_into.png",
					textFromToken("stkl.positionen.cvsimport"), BUTTON_IMPORTCSV_STUECKLISTEARBEISTPLAN, null);

			panelQueryArbeitsplan.createAndSaveAndShowButton("/com/lp/client/res/sort_az_descending.png",
					textFromToken("stkl.arbeitsplan.neunummerieren"), BUTTON_AG_NEU_NUMMERIEREN, null);

			panelBottomArbeitsplan = new PanelStuecklistearbeitsplan(getInternalFrame(),
					textFromToken("stkl.arbeitsplan"), null);
			panelQueryArbeitsplan.setMultipleRowSelectionEnabled(true);
			panelSplitArbeitsplan = new PanelSplit(getInternalFrame(), panelBottomArbeitsplan, panelQueryArbeitsplan,
					180);

			setComponentAt(IDX_PANEL_ARBEITSPLAN, panelSplitArbeitsplan);
		} else {
			// filter refreshen.
			panelQueryArbeitsplan.setDefaultFilter(StuecklisteFilterFactory.getInstance().createFKStueckliste(key));
		}
	}

	private void refreshAbbuchungslager(Integer key) throws Throwable {

		if (panelQueryAbbuchungslager == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };
			panelQueryAbbuchungslager = new PanelQuery(null,
					StuecklisteFilterFactory.getInstance().createFKStuecklisteAbbuchungslager(key),
					QueryParameters.UC_ID_STKLAGERENTNAHME, aWhichStandardButtonIUse, getInternalFrame(),
					textFromToken("stk.tab.oben.abbuchungslager"), true);

			panelBottomAbbuchungslager = new PanelStklagerentnahme(getInternalFrame(),
					textFromToken("stk.tab.oben.abbuchungslager"), null, this);

			panelSplitAbbuchungslager = new PanelSplit(getInternalFrame(), panelBottomAbbuchungslager,
					panelQueryAbbuchungslager, 180);

			setComponentAt(IDX_LAGERENTNAHME, panelSplitAbbuchungslager);
		} else {
			// filter refreshen.
			panelQueryAbbuchungslager
					.setDefaultFilter(StuecklisteFilterFactory.getInstance().createFKStuecklisteAbbuchungslager(key));
		}
	}

	private void dialogAgstkl(ItemChangedEvent e) throws Throwable {
		panelAgstkl = AngebotstklFilterFactory.getInstance().createPanelFLRAgstkl(getInternalFrame(), true, false);
		new DialogQuery(panelAgstkl);
	}

	private void createEigenschaft(Integer key) throws Throwable {

		if (panelQueryEigenschaft == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryEigenschaft = new PanelQuery(null,
					StuecklisteFilterFactory.getInstance().createFKStueckliste(key),
					QueryParameters.UC_ID_STUECKLISTEEIGENSCHAFT, aWhichStandardButtonIUse, getInternalFrame(),
					textFromToken("lp.eigenschaft"), true);
			panelBottomEigenschaft = new PanelStuecklisteeigenschaft(getInternalFrame(),
					textFromToken("lp.eigenschaft"), null);

			panelSplitEigenschaft = new PanelSplit(getInternalFrame(), panelBottomEigenschaft, panelQueryEigenschaft,
					350);

			setComponentAt(IDX_PANEL_EIGENSCHAFTEN, panelSplitEigenschaft);
		} else {
			// filter refreshen.
			panelQueryEigenschaft.setDefaultFilter(StuecklisteFilterFactory.getInstance().createFKStueckliste(key));
		}
	}

	private void createParameter(Integer key) throws Throwable {

		if (panelQueryParameter == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1, };
			panelQueryParameter = new PanelQuery(null,
					StuecklisteFilterFactory.getInstance().createFKStklparameter(key),
					QueryParameters.UC_ID_STKLPARAMETER, aWhichStandardButtonIUse, getInternalFrame(),
					textFromToken("lp.parameter"), true);

			panelQueryParameter.createAndSaveAndShowButton("/com/lp/client/res/goto.png",
					textFromToken("stk.parameter.fehlendeparameternachtragen"), BUTTON_PARAMETER_KOPIEREN, null);

			panelBottomParameter = new PanelStuecklisteParameter(getInternalFrame(), textFromToken("lp.parameter"),
					null);

			panelSplitParameter = new PanelSplit(getInternalFrame(), panelBottomParameter, panelQueryParameter, 230);

			setComponentAt(IDX_PANEL_PARAMETER, panelSplitParameter);
		} else {
			// filter refreshen.
			panelQueryParameter.setDefaultFilter(StuecklisteFilterFactory.getInstance().createFKStklparameter(key));
		}
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		IDX_PANEL_AUSWAHL = reiterHinzufuegen(textFromToken("lp.auswahl"), null, null, textFromToken("lp.auswahl"));

		IDX_PANEL_DETAIL = reiterHinzufuegen(textFromToken("lp.kopfdaten"), null, null, textFromToken("lp.kopfdaten"));
		IDX_PANEL_POSITIONEN = reiterHinzufuegen(textFromToken("stkl.title.panel.positionen"), null, null,
				textFromToken("stkl.title.panel.positionen"));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ERSATZTYPENVERWALTUNG)) {
			IDX_PANEL_POSITIONENERSATZ = reiterHinzufuegen(textFromToken("stkl.positionen.ersatz"), null, null,
					textFromToken("stkl.positionen.ersatz"));
		}
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PRUEFPLAN1)) {
			IDX_PANEL_PRUEFPLAN = reiterHinzufuegen(textFromToken("stk.pruefplan"), null, null,
					textFromToken("stk.pruefplan"));
		}
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_STUECKLISTE_ARBEITSPLAN)) {
			IDX_PANEL_ARBEITSPLAN = reiterHinzufuegen(textFromToken("stkl.arbeitsplan"), null, null,
					textFromToken("stkl.arbeitsplan"));

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ALTERNATIV_MASCHINEN)) {
				IDX_PANEL_ALTERNATIVMASCHINE = reiterHinzufuegen(textFromToken("stkl.alternativmaschine"), null, null,
						textFromToken("stkl.alternativmaschine"));
			}

		}

		IDX_LAGERENTNAHME = reiterHinzufuegen(LPMain.getTextRespectUISPr("stk.tab.oben.abbuchungslager"), null, null,
				textFromToken("stk.tab.oben.abbuchungslager"));

		IDX_PANEL_EIGENSCHAFTEN = reiterHinzufuegen(textFromToken("lp.eigenschaften"), null, null,
				textFromToken("lp.eigenschaften"));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_STUECKLISTE_MIT_FORMELN)) {

			IDX_PANEL_PARAMETER = reiterHinzufuegen(textFromToken("stk.parameter"), null, null,
					textFromToken("stk.parameter"));
		}

		createAuswahl();
		// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
		// restlichen panel deaktivieren
		if (panelQueryStueckliste.getSelectedId() == null) {
			getInternalFrame().enableAllMyKidPanelsExceptMe(0, false);
		}

		if ((Integer) panelQueryStueckliste.getSelectedId() != null) {
			getInternalFrameStueckliste().setStuecklisteDto(DelegateFactory.getInstance().getStuecklisteDelegate()
					.stuecklisteFindByPrimaryKey((Integer) panelQueryStueckliste.getSelectedId()));
		}

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryStueckliste, ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryStueckliste.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN, textFromToken("stkl.stueckliste"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

		if (getInternalFrameStueckliste().getStuecklisteDto() != null) {
			String sLosgroesse = "0";
			if (getInternalFrameStueckliste().getStuecklisteDto().getNLosgroesse() != null) {
				sLosgroesse = getInternalFrameStueckliste().getStuecklisteDto().getNLosgroesse().intValue() + "";
			}
			if (getInternalFrameStueckliste().getStuecklisteDto().getArtikelDto() != null) {

				if (bReferenznummerInPositonen) {
					getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
							getInternalFrameStueckliste().getStuecklisteDto().getArtikelDto()
									.formatArtikelbezeichnungMitZusatzbezeichnungUndReferenznummer() + ", LGR: "
									+ sLosgroesse);
				} else {
					getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
							getInternalFrameStueckliste().getStuecklisteDto().getArtikelDto()
									.formatArtikelbezeichnungMitZusatzbezeichnung() + ", LGR: " + sLosgroesse);
				}

			}
		}
	}

	public void erstelleStuecklisteAusArtikel(Integer artikelIId) throws ExceptionLP, Throwable {

		// SP5023
		if (getInternalFrame().getStack().size() > 1) {

			boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.goto.reportgeoeffnet"));

			if (b == true) {

				getInternalFrame().enableAllAndRemoveComponent(false);
			} else {
				return;
			}

		}

		createDetail(null);
		panelDetailStueckliste.eventActionNew(null, true, false);
		setSelectedComponent(panelDetailStueckliste);
		
		panelDetailStueckliste.eventYouAreSelected(false);
		
		panelDetailStueckliste.setMyComponents(artikelIId);
		panelDetailStueckliste.updateButtons();

	}

	public InternalFrameStueckliste getInternalFrameStueckliste() {
		return (InternalFrameStueckliste) getInternalFrame();
	}

	private PanelBasis getStuecklisteKopfdaten() throws Throwable {
		Integer iIdAngebot = null;

		if (panelDetailStueckliste == null) {

			// Die Angebot hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdAngebot = new Integer(Integer.parseInt(getInternalFrame().getKeyWasForLockMe()));
			}

			panelDetailStueckliste = new PanelStueckliste(getInternalFrame(), textFromToken("lp.kopfdaten"),
					iIdAngebot); // empty bei leerer
									// angebotsliste

			setComponentAt(IDX_PANEL_DETAIL, panelDetailStueckliste);
		}

		return panelDetailStueckliste;
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(MENU_XLSIMPORT_PRUEFPLAN)) {
			DialogPruefplanXLSImport d = new DialogPruefplanXLSImport(this);

		} else if (e.getActionCommand().equals(MENU_XLSIMPORT_ARBEITSPLAN)) {
			onActionXlsImportArbeitsplan();

		} else if (e.getActionCommand().equals(MENU_XLSIMPORT_MATERIAL)) {
			onActionXlsImportMaterial();
		} else if (e.getActionCommand().equals(MENUE_ACTION_CREO_IMPORT)) {

			XlsFileOpener xlsOpener = FileOpenerFactory.stklCreoImportOpenXls(getInternalFrame());
			xlsOpener.doOpenDialog();

			if (!xlsOpener.hasFileChosen()) {

				return;
			}

			DialogStuecklisteCreoImportXLS d = new DialogStuecklisteCreoImportXLS(xlsOpener.getFile().getBytes(), this);
			d.setSize(500, 500);
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);

		} else if (e.getActionCommand().equals(MENUE_ACTION_CSVIMPORT)) {

			if (iStrukturierterStklImport == 1) {
				HvOptional<XlsFile> xlsFile = FileOpenerFactory.stklStrukturiertImportXls(this);
				if (!xlsFile.isPresent())
					return;

				File f = xlsFile.get().getFile();
				File[] fileArray = new File(f.getParent()).listFiles();

				Workbook workbook = Workbook.getWorkbook(f);
				Sheet sheet = workbook.getSheet(0);

				try {
					ArrayList<StrukturierterImportDto> gesamtliste = getSheetImportController().importSheet(sheet,
							fileArray);
					ArrayList alAngelegteArtikel = DelegateFactory.getInstance().getStuecklisteDelegate()
							.importiereStuecklistenstruktur(gesamtliste, false, null);

					if (alAngelegteArtikel.size() > 0) {
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i < alAngelegteArtikel.size(); i++) {
							sb.append("\n");
							sb.append(alAngelegteArtikel.get(i));
						}

						DialogFactory.showMessageMitScrollbar(textFromToken("stkl.import.typ1.artikel.neuangelegt"),
								sb.toString());

					}

					DialogFactory.showModalDialog(textFromToken("lp.info"),
							textFromToken("stkl.import.erfolgreich.info"));

				} catch (IOException ex) {
					handleException(ex, true);
				} finally {
					workbook.close();
				}
			} else if (iStrukturierterStklImport == 2) {
				// Die Einheiten mm und mm?? muessen vorhanden sein, sonst
				// return

				try {
					DelegateFactory.getInstance().getSystemDelegate()
							.einheitFindByPrimaryKey(SystemFac.EINHEIT_MILLIMETER);
				} catch (Exception e1) {
					DialogFactory.showModalDialog(textFromToken("lp.error"), "Einheit 'mm' im System nicht vorhanden");
					return;
				}
				try {
					DelegateFactory.getInstance().getSystemDelegate()
							.einheitFindByPrimaryKey(SystemFac.EINHEIT_QUADRATMILLIMETER);
				} catch (Exception e1) {
					DialogFactory.showModalDialog(textFromToken("lp.error"),
							"Einheit 'mm??' im System nicht vorhanden");
					return;
				}

				// Dateiauswahldialog
				HvOptional<CsvFile> csvFile = FileOpenerFactory.stklStrukturiertImportSiemensCsv(this);
				if (!csvFile.isPresent())
					return;

				List<String[]> al = csvFile.get().read();
				StrukturierterImportSiemensXS xs = new StrukturierterImportSiemensXS();
				ArrayList<StrukturierterImportSiemensNXDto> alTemp = new ArrayList<StrukturierterImportSiemensNXDto>();

				boolean bErsterGueltigerArtikelGefunden = false;
				for (int i = 1; i < al.size(); i++) {

					// Kopfstueckliste muss vorhanden sein

					StrukturierterImportSiemensNXDto dto = xs.setupRow(al.get(i));

					if (bErsterGueltigerArtikelGefunden == false && dto.getArtikelIId() != null) {
						bErsterGueltigerArtikelGefunden = true;

						if (dto.getIEbene() != 1) {
							// Erster Artikel muss in Ebene 1 sein
							DialogFactory.showModalDialog(textFromToken("lp.error"), "Der erste g\u00FCltige Artikel ("
									+ dto.getArtikelnummer() + ") muss sich in Ebene 1 befinden.");
							return;

						}

					}

					if (bErsterGueltigerArtikelGefunden == true) {

						if (al.get(i).length >= 10) {

							// Nur Artikel mit 'm' in Spalte 10 importieren
							if (al.get(i)[9].trim().equals("m")) {

								if (dto.getArtikelIId() != null) {
									alTemp.add(dto);

									// Wenn in Spalte 6 (Material) anhand der
									// ersten Stellen bis zum '_' ein Artikel
									// gefunden
									// werden kann, dann wird eine Stueckliste +
									// Position angelegt

									StrukturierterImportSiemensNXDto dtoHS = xs.setupRowRohmaterial(al.get(i));

									if (dtoHS.getArtikelIId() != null) {

										// Rohmaterial muss in Helium in m oder
										// m?? definiert sein, sonst
										// Fehlermeldung
										// SystemFac.EINHEIT_METER oder
										// SystemFac.EINHEIT_QUADRATMETER

										ArtikelDto artikelDtoRohmaterial = DelegateFactory.getInstance()
												.getArtikelDelegate().artikelFindByPrimaryKey(dtoHS.getArtikelIId());

										if (artikelDtoRohmaterial.getEinheitCNr().equals(SystemFac.EINHEIT_METER)
												|| artikelDtoRohmaterial.getEinheitCNr()
														.equals(SystemFac.EINHEIT_QUADRATMETER)) {
											alTemp.add(dtoHS);
										} else {

											DialogFactory.showModalDialog(textFromToken("lp.error"), "Der Artikel '"
													+ dtoHS.getArtikelnummer()
													+ "' muss in HeliumV die Einheit m oder m?? hinterlegt haben. Der Import wird abgebrochen.");

											return;
										}

									}
								}
							}
						}
					}
				}

				ArrayList<StrukturierterImportSiemensNXDto> gesamtliste = xs.importSheet(alTemp);

				if (gesamtliste != null && gesamtliste.size() > 0) {
					try {

						ArrayList<ArtikelDto> alNichtimportiert = DelegateFactory.getInstance().getStuecklisteDelegate()
								.importiereStuecklistenstrukturSiemensNX(gesamtliste, alTemp, null);

						if (alNichtimportiert != null && alNichtimportiert.size() > 0) {

							String msg = "Folgende St\u00FCcklisten sind bereits in HeliumV vorhanden und wurden daher nicht importiert:\n\n";
							for (int i = 0; i < alNichtimportiert.size(); i++) {
								msg += alNichtimportiert.get(i).formatArtikelbezeichnung() + "\n";
							}

							DialogFactory.showModalDialog(textFromToken("lp.error"), msg);
						}
					} catch (IOException ex) {
						handleException(ex, true);
					}
				}

			} else if (iStrukturierterStklImport == 3) {
				// PJ18568
				HvOptional<DirectoryFile> df = HelperClient.chooseDirectoryNew(this,
						FileChooserConfigToken.ImportLastDirectory);
				if (df.isPresent()) {
					File pfad = df.get().getDirectory();

					File test = new File(pfad.getAbsolutePath());

					// Alle Verzeichnis im "test" auflisten...

					String[] DIR = test.list();

					HashMap<String, HashMap<String, byte[]>> hmDateien = new HashMap<String, HashMap<String, byte[]>>();

					for (int i = 0; i < DIR.length; i++) {

						File einzlneDatei = new File(DIR[i]);

						if (einzlneDatei.getAbsolutePath().toLowerCase().endsWith(".xls")) {

							ByteArrayOutputStream ous = null;
							InputStream ios = null;
							try {
								byte[] buffer = new byte[4096];
								ous = new ByteArrayOutputStream();
								ios = new FileInputStream(pfad.getAbsolutePath() + System.getProperty("file.separator")
										+ einzlneDatei.getName());
								int read = 0;
								while ((read = ios.read(buffer)) != -1) {
									ous.write(buffer, 0, read);
								}
							} finally {
								try {
									if (ous != null)
										ous.close();
								} catch (IOException ex) {
								}

								try {
									if (ios != null)
										ios.close();
								} catch (IOException ex) {
								}
							}

							String stueklistenr = einzlneDatei.getName();

							stueklistenr = stueklistenr.substring(0, stueklistenr.length() - 4);

							HashMap<String, byte[]> hmDateienTemp = new HashMap<String, byte[]>();
							hmDateienTemp.put("XLS", ous.toByteArray());

							hmDateien.put(stueklistenr, hmDateienTemp);
						}

						System.out.println(DIR[i]);

					}
					for (int i = 0; i < DIR.length; i++) {

						File einzlneDatei = new File(DIR[i]);

						if (einzlneDatei.getAbsolutePath().toLowerCase().endsWith(".pdf")) {

							ByteArrayOutputStream ous = null;
							InputStream ios = null;
							try {
								byte[] buffer = new byte[4096];
								ous = new ByteArrayOutputStream();
								ios = new FileInputStream(pfad.getAbsolutePath() + System.getProperty("file.separator")
										+ einzlneDatei.getName());
								int read = 0;
								while ((read = ios.read(buffer)) != -1) {
									ous.write(buffer, 0, read);
								}
							} finally {
								try {
									if (ous != null)
										ous.close();
								} catch (IOException ex) {
								}

								try {
									if (ios != null)
										ios.close();
								} catch (IOException ex) {
								}
							}

							String stueklistenr = einzlneDatei.getName();

							stueklistenr = stueklistenr.substring(0, stueklistenr.length() - 4);

							if (hmDateien.containsKey(stueklistenr)) {

								HashMap<String, byte[]> hmDateienTemp = hmDateien.get(stueklistenr);

								hmDateienTemp.put(einzlneDatei.getName(), ous.toByteArray());

								hmDateien.put(stueklistenr, hmDateienTemp);

							}

						}

					}

					DelegateFactory.getInstance().getStuecklisteDelegate().importiereStuecklistenINFRA(hmDateien);

				}

			}
		} else {
			if (getInternalFrameStueckliste().getStuecklisteDto() != null) {
				if (e.getActionCommand().equals(MENUE_INVENTUR_ACTION_STUECKLISTE)) {
					String add2Title = textFromToken("stkl.stueckliste");
					getInternalFrame().showReportKriterien(new ReportStueckliste(getInternalFrameStueckliste(),
							add2Title, getInternalFrameStueckliste().getStuecklisteDto().getIId()));
				} else if (e.getActionCommand().equals(MENU_BEARBEITEN_KONFIGURIEREN)) {
					DialogParameterErfassen d = new DialogParameterErfassen(this);
					d.setVisible(true);

					if (d.getParameter() != null) {

						if (d.isbProduktstuecklisteErzeugen()) {

							Integer stuecklisteIIdNeu = DelegateFactory.getInstance().getStuecklisteDelegate()
									.createProduktstuecklisteAnhandFormelstueckliste(
											getInternalFrameStueckliste().getStuecklisteDto().getIId(), BigDecimal.ONE,
											d.getParameter(), null);

							panelQueryStueckliste.eventYouAreSelected(false);

						} else {

							String add2Title = textFromToken("stkl.gesamtkalkulation");
							getInternalFrame().showReportKriterien(new ReportStuecklistegesamtkalkulation(
									getInternalFrameStueckliste(), add2Title,
									getInternalFrameStueckliste().getStuecklisteDto().getIId(), d.getParameter()));
						}
					}

				} else if (e.getActionCommand().equals(MENU_BEARBEITEN_STUECKLISTE_AUS_PROFIRST_AKTUALISIEREN)) {
					String s = DelegateFactory.getInstance().getStuecklisteDelegate().pruefeUndImportiereProFirst(
							getInternalFrameStueckliste().getStuecklisteDto().getIId(), null);

					if (s != null && s.length() > 0) {
						DialogFactory.showModalDialog(textFromToken("lp.error"), s);
					}

				} else if (e.getActionCommand().equals(MENU_BEARBEITEN_UPDATE_SOLLZEITEN_ANHAND_ISTZEITEN)) {

					Calendar c = Calendar.getInstance();

					java.sql.Date dBis = new java.sql.Date(c.getTime().getTime());

					c.add(Calendar.MONTH, -6);
					java.sql.Date dVon = new java.sql.Date(c.getTime().getTime());
					

					java.sql.Date[] datum = DialogFactory.showDatumseingabeVonBis(
							LPMain.getTextRespectUISPr("stkl.bearbeiten.sollzeitenanhand.losistzeitenaktialisieren.zeitraum"), dVon,
							dBis);
					
					if (datum[0] != null && datum[1] != null) {
						DelegateFactory.getInstance().getStuecklisteDelegate()
								.sollzeitenAnhandLosistzeitenAktualisieren(
										getInternalFrameStueckliste().getStuecklisteDto().getIId(), datum[0], datum[1]);
					}

				} else if (e.getActionCommand().equals(MENU_BEARBEITEN_LOSE_AKTUALISIEREN)) {

					// PJ18094
					ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
							.getParameterDelegate().getParametermandant(ParameterFac.PARAMETER_LOSE_AKTUALISIEREN,
									ParameterFac.KATEGORIE_STUECKLISTE, LPMain.getTheClient().getMandant());

					int iOption = (Integer) parameter.getCWertAsObject();

					Object[] options = null;
					int n = -1;
					TreeMap<String, Object[]> tm = null;

					JCheckBox check = new JCheckBox(
							textFromToken("fert.los.stuecklisteaktualisieren.incl.ausgegebenundinproduktion"));

					Integer stuecklisteIId = null;

					if (iOption == 0) {
						options = new Object[] { textFromToken("stkl.loseaktualisieren.selektiertestueckliste"),
								textFromToken("lp.abbrechen") };
						n = JOptionPane.showOptionDialog(getInternalFrame(), check,
								textFromToken("stkl.lose.aktualisieren.frage"), JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

						if (n == JOptionPane.YES_OPTION) {
							tm = DelegateFactory.getInstance().getFertigungDelegate().aktualisiereLoseAusStueckliste(
									getInternalFrameStueckliste().getStuecklisteDto().getIId(), check.isSelected());
							stuecklisteIId = getInternalFrameStueckliste().getStuecklisteDto().getIId();
						} else {
							return;
						}

					} else {
						options = new Object[] { textFromToken("stkl.loseaktualisieren.selektiertestueckliste"),
								textFromToken("stkl.loseaktualisieren.allestuecklisten"),
								textFromToken("lp.abbrechen") };
						n = JOptionPane.showOptionDialog(getInternalFrame(), check,
								textFromToken("stkl.lose.aktualisieren.frage"), JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

						if (n == JOptionPane.YES_OPTION) {
							tm = DelegateFactory.getInstance().getFertigungDelegate().aktualisiereLoseAusStueckliste(
									getInternalFrameStueckliste().getStuecklisteDto().getIId(), check.isSelected());
							stuecklisteIId = getInternalFrameStueckliste().getStuecklisteDto().getIId();
						} else if (n == JOptionPane.NO_OPTION) {
							tm = DelegateFactory.getInstance().getFertigungDelegate()
									.aktualisiereLoseAusStueckliste(null, check.isSelected());
						} else {
							return;
						}
					}

					getInternalFrame().showReportKriterien(
							new ReportLoseAktualisiert(getInternalFrame(), stuecklisteIId, check.isSelected(), tm));

				} else if (e.getActionCommand().equals(MENU_BEARBEITEN_KOMMENTAR)) {

					if (!getStuecklisteKopfdaten().isLockedDlg()) {
						refreshPdKommentar();
						getInternalFrame().showPanelDialog(pdStuecklisteKommentar);

					}
				} else if (e.getActionCommand().equals(MENU_BEARBEITEN_ARTIKEL_ERSETZEN)) {

					DialogArtikelErsetzen d = new DialogArtikelErsetzen(getInternalFrameStueckliste());
					LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);

				} else if (e.getActionCommand().equals(MENU_BEARBEITEN_BEVORZUGTEN_ARTIKEL_EINTRAGEN)) {

					panelQueryFLRBevorzugterArtikel = ArtikelFilterFactory.getInstance()
							.createPanelFLRArtikel(getInternalFrame(), null, false, false, true);

					new DialogQuery(panelQueryFLRBevorzugterArtikel);

				} else if (e.getActionCommand().equals(MENU_BEARBEITEN_MASCHINE_ERSETZEN)) {

					DialogMaschineErsetzen d = new DialogMaschineErsetzen(getInternalFrameStueckliste());
					LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);

				} else if (e.getActionCommand().equals(MENU_INFO_ARBEITSPLAN)) {

					String add2Title = textFromToken("stkl.arbeitsplan");
					getInternalFrame().showReportKriterien(new ReportArbeitsplan(getInternalFrameStueckliste(),
							add2Title, getInternalFrameStueckliste().getStuecklisteDto().getIId()));
				} else if (e.getActionCommand().equals(MENUE_JOURNAL_ACTION_REICHWEITE)) {

					String add2Title = textFromToken("stkl.report.reichweite");
					getInternalFrame()
							.showReportKriterien(new ReportReichweite(getInternalFrameStueckliste(), add2Title));
				} else if (e.getActionCommand().equals(MENU_INFO_GESAMTKALKULATION)) {

					String add2Title = textFromToken("stkl.gesamtkalkulation");
					getInternalFrame()
							.showReportKriterien(new ReportStuecklistegesamtkalkulation(getInternalFrameStueckliste(),
									add2Title, getInternalFrameStueckliste().getStuecklisteDto().getIId(), null));
				} else if (e.getActionCommand().equals(MENU_INFO_MINDERVERFUEGBARKEIT)) {

					String add2Title = textFromToken("stkl.minderverfuegbarkeit");
					getInternalFrame().showReportKriterien(new ReportMinderverfuegbarkeit(getInternalFrameStueckliste(),
							add2Title, getInternalFrameStueckliste().getStuecklisteDto().getIId()));
				} else if (e.getActionCommand().equals(MENU_INFO_WAFFENREGISTER)) {

					String add2Title = textFromToken("artikel.waffenregister");
					getInternalFrame().showReportKriterien(new ReportWaffenregister(getInternalFrameStueckliste(),
							add2Title, getInternalFrameStueckliste().getStuecklisteDto().getIId()));
				} else if (e.getActionCommand().equals(MENU_INFO_AENDERUNGEN)) {

					String add2Title = textFromToken("lp.report.aenderungen");
					getInternalFrame().showReportKriterien(new ReportEntitylog(HvDtoLogClass.STUECKLISTE,
							getInternalFrameStueckliste().getStuecklisteDto().getIId() + "",
							getInternalFrameStueckliste(), add2Title, getInternalFrameStueckliste().getStuecklisteDto()
									.getArtikelDto().formatArtikelbezeichnung()));
				} else if (e.getActionCommand().equals(MENU_INFO_PRUEFPLAN)) {

					String add2Title = textFromToken("stk.pruefplan");
					getInternalFrame().showReportKriterien(new ReportStklPruefplan(getInternalFrameStueckliste(),
							add2Title, getInternalFrameStueckliste().getStuecklisteDto().getIId()));
				} else if (e.getActionCommand().equals(MENU_INFO_FREIGABE)) {

					String add2Title = textFromToken("stk.report.freigabe");
					getInternalFrame().showReportKriterien(new ReportFreigabe(getInternalFrameStueckliste(), add2Title,
							getInternalFrameStueckliste().getStuecklisteDto().getIId()));
				} else if (e.getActionCommand().equals(MENU_JOURNAL_VERSCHLEISSTEILVERWENDUNG)) {

					String add2Title = textFromToken("stk.verschleissteilverwendung");
					getInternalFrame().showReportKriterien(
							new ReportVerschleissteilverwendung(getInternalFrameStueckliste(), add2Title));
				} else if (e.getActionCommand().equals(MENUE_ACTION_INFO_VERGLEICH_MIT_ANDERER_STUECKLISTE)) {

					String add2Title = textFromToken("stkl.report.vergleichmitandererstueckliste");
					getInternalFrame().showReportKriterien(
							new ReportVergleichMitAndererStueckliste(getInternalFrameStueckliste(), add2Title));
				} else if (e.getActionCommand().equals(MENU_BEARBEITEN_HANDARTIKEL_UMANDELN)) {
					if (panelQueryPositionen != null && panelQueryPositionen.getSelectedId() != null) {

						StuecklistepositionDto posDto = DelegateFactory.getInstance().getStuecklisteDelegate()
								.stuecklistepositionFindByPrimaryKey((Integer) panelQueryPositionen.getSelectedId());

						if (posDto.getArtikelIId() != null) {

							ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
									.artikelFindByPrimaryKey(posDto.getArtikelIId());
							if (aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
								DialogNeueArtikelnummer d = new DialogNeueArtikelnummer(getInternalFrame());
								LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
								d.setVisible(true);
								if (d.getArtikelnummerNeu() != null) {

									DelegateFactory.getInstance().getArtikelDelegate().wandleHandeingabeInArtikelUm(
											posDto.getIId(), ArtikelFac.HANDARTIKEL_UMWANDELN_STUECKLISTEPOSITION,
											d.getArtikelnummerNeu());
								}

							} else {
								DialogFactory.showModalDialog(textFromToken("lp.info"),
										textFromToken("angebot.bearbeiten.handartikelumwandeln.error"));
							}

						}
						panelSplitPositionen.eventYouAreSelected(false);
						panelQueryPositionen.setSelectedId(posDto.getIId());
					}

				}

			} else {

				DialogFactory.showModalDialog(textFromToken("lp.error"),
						"Bitte zuerst eine St\u00FCckliste ausw\u00E4hlen");

			}
		}
	}

	private void onActionXlsImportMaterial() throws IOException, Throwable {
		XlsFileOpener xlsOpener = FileOpenerFactory.stklMaterialImportOpenXls(getInternalFrame());
		xlsOpener.doOpenDialog();

		if (!xlsOpener.hasFileChosen())
			return;

		DialogStuecklisteMaterialXLSImport d = new DialogStuecklisteMaterialXLSImport(xlsOpener.getFile().getBytes(),
				this);
		d.setSize(500, 500);
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
		d.setVisible(true);
	}

	private void onActionXlsImportArbeitsplan() throws IOException, Throwable {
		XlsFileOpener xlsOpener = FileOpenerFactory.stklArbeitsplanImportOpenXls(getInternalFrame());
		xlsOpener.doOpenDialog();

		if (!xlsOpener.hasFileChosen())
			return;

		DialogArbeitsplanXLSImport d = new DialogArbeitsplanXLSImport(xlsOpener.getFile().getBytes(), this);
		d.setSize(500, 500);
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
		d.setVisible(true);
	}

	private void refreshPdKommentar() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken des
		// Angebots nicht richtig
		pdStuecklisteKommentar = new PanelDialogStuecklisteKommentar(getInternalFrame(), textFromToken("lp.kommentar"),
				true);
	}

	private void dialogStueckliste(ItemChangedEvent e) throws Throwable {
		panelStueckliste = StuecklisteFilterFactory.getInstance().createPanelFLRStueckliste(getInternalFrame(), null,
				false);
		new DialogQuery(panelStueckliste);
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelQueryStueckliste) {

				Integer iId = (Integer) panelQueryStueckliste.getSelectedId();
				if (iId != null) {
					refreshPositionen(iId);
					setSelectedComponent(panelSplitPositionen);
				}
			} else if (eI.getSource() == panelAgstkl) {
				DelegateFactory.getInstance().getStuecklisteDelegate().kopiereAusAgstkl(
						(Integer) panelAgstkl.getSelectedId(),
						getInternalFrameStueckliste().getStuecklisteDto().getIId());
				panelSplitPositionen.eventYouAreSelected(false);
			} else if (eI.getSource() == panelQueryFLRBevorzugterArtikel) {
				Integer artikelIId_Bevorzugt = (Integer) panelQueryFLRBevorzugterArtikel.getSelectedId();
				DelegateFactory.getInstance().getStuecklisteDelegate()
						.bevorzugtenArtikelEintragen(artikelIId_Bevorzugt);

			} else if (eI.getSource() == panelQueryFLRPruefkombination) {
				Integer pruefkombinationIId = (Integer) panelQueryFLRPruefkombination.getSelectedId();

				PruefkombinationDto pkDto = DelegateFactory.getInstance().getStuecklisteDelegate()
						.pruefkombinationFindByPrimaryKey(pruefkombinationIId);

				if (panelQueryPositionen.getSelectedIds() != null) {

					StuecklistepositionDto sktlposDtoKontakt = null;
					StuecklistepositionDto sktlposDtoLitze = null;

					if (panelQueryPositionen.getSelectedIds().length == 1) {
						sktlposDtoKontakt = DelegateFactory.getInstance().getStuecklisteDelegate()
								.stuecklistepositionFindByPrimaryKey(
										(Integer) panelQueryPositionen.getSelectedIds()[0]);
					} else if (panelQueryPositionen.getSelectedIds().length == 2) {
						{

							StuecklistepositionDto sktlposDto1 = DelegateFactory.getInstance().getStuecklisteDelegate()
									.stuecklistepositionFindByPrimaryKey(
											(Integer) panelQueryPositionen.getSelectedIds()[0]);

							StuecklistepositionDto sktlposDto2 = DelegateFactory.getInstance().getStuecklisteDelegate()
									.stuecklistepositionFindByPrimaryKey(
											(Integer) panelQueryPositionen.getSelectedIds()[1]);

							if (pkDto.getArtikelIIdKontakt().equals(sktlposDto1.getArtikelIId())) {
								sktlposDtoKontakt = sktlposDto1;
								sktlposDtoLitze = sktlposDto2;
							} else {
								sktlposDtoKontakt = sktlposDto2;
								sktlposDtoLitze = sktlposDto1;
							}

						}

					}
					// Ins Neu des Pruefplans wechseln und Kontakt/Litze
					// vorbesetzen

					refreshPruefplan(getInternalFrameStueckliste().getStuecklisteDto().getIId());

					this.setSelectedComponent(panelSplitPruefplan);

					panelBottomPruefplan.eventActionNew(null, true, false);

					panelBottomPruefplan.updateButtons();
					panelBottomPruefplan.pruefplanVorbesetzen(sktlposDtoKontakt, sktlposDtoLitze, pkDto);
				}

			} else if (eI.getSource() == panelStuecklisteFuerParameter) {

				Integer stuecklisteIIdQuelle = (Integer) panelStuecklisteFuerParameter.getSelectedId();
				DelegateFactory.getInstance().getStuecklisteDelegate().kopiereParameterEinerStueckliste(
						stuecklisteIIdQuelle, getInternalFrameStueckliste().getStuecklisteDto().getIId());
				panelSplitParameter.eventYouAreSelected(false);
			} else if (eI.getSource() == panelStueckliste) {

				if (bPositionen == true) {

					DelegateFactory.getInstance().getStuecklisteDelegate().kopiereStuecklistenPositionen(
							(Integer) panelStueckliste.getSelectedId(),
							getInternalFrameStueckliste().getStuecklisteDto().getIId());
					panelSplitPositionen.eventYouAreSelected(false);
				} else {
					DelegateFactory.getInstance().getStuecklisteDelegate().kopiereStuecklisteArbeitsplan(
							(Integer) panelStueckliste.getSelectedId(),
							getInternalFrameStueckliste().getStuecklisteDto().getIId());
					panelSplitArbeitsplan.eventYouAreSelected(false);

				}

			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();
			if (sAspectInfo.endsWith(ACTION_SPECIAL_IMPORT_AGSTKL)) {
				dialogAgstkl(eI);
			} else if (sAspectInfo.endsWith(ACTION_SPECIAL_IMPORT)) {
				bPositionen = true;
				dialogStueckliste(eI);
			} else if (sAspectInfo.endsWith(ACTION_SPECIAL_PARAMETER_KOPIEREN)) {

				HashMap hm = DelegateFactory.getInstance().getStuecklisteDelegate()
						.getAlleStuecklistenIIdsFuerVerwendungsnachweis(
								getInternalFrameStueckliste().getStuecklisteDto().getArtikelIId());

				String in = "(";

				if (hm.size() == 0) {
					in = "(-999";
				} else {

					// Liste anzeigen

					Iterator it = hm.keySet().iterator();

					while (it.hasNext()) {
						Integer key = (Integer) it.next();

						in += key;

						if (it.hasNext()) {
							in += ",";
						}

					}
				}
				in += ")";

				FilterKriterium[] kriterien = new FilterKriterium[1];
				kriterien[0] = new FilterKriterium("stueckliste." + StuecklisteFac.FLR_STUECKLISTE_B_MIT_FORMELN, true,
						"(1)", // wenn
						// das
						// Kriterium
						// verwendet
						// wird
						// ,
						// sollen
						// die
						// versteckten
						// nicht
						// mitangezeigt
						// werden
						FilterKriterium.OPERATOR_IN, false);

				Integer use_case_id = QueryParameters.UC_ID_STUECKLISTE;

				if (LPMain.getInstance().getDesktopController()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {
					use_case_id = QueryParameters.UC_ID_STUECKLISTE_MIT_MANDANT;
				}

				panelStuecklisteFuerParameter = new PanelQueryFLR(null, kriterien, use_case_id, null,
						getInternalFrameStueckliste(),
						LPMain.getTextRespectUISPr("stk.parameter.kopieren.stklauswaehlen"));

				panelStuecklisteFuerParameter.befuellePanelFilterkriterienDirektUndVersteckte(
						StuecklisteFilterFactory.getInstance().createFKDArtikelnummer(),
						StuecklisteFilterFactory.getInstance().createFKDTextSuchen(),
						new FilterKriterium("stueckliste.i_id", true, in, FilterKriterium.OPERATOR_IN, false),
						LPMain.getTextRespectUISPr("stk.parameter.kopieren.alle"));
				new DialogQuery(panelStuecklisteFuerParameter);

			} else if (sAspectInfo.equals(BUTTON_SORTIERE_NACH_ARTIKELNUMMER)) {

				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("lp.sortierenach.frage"));

				if (b == true) {

					DelegateFactory.getInstance().getStuecklisteDelegate()
							.sortiereNachArtikelnummer(getInternalFrameStueckliste().getStuecklisteDto().getIId());

					panelQueryPositionen.eventYouAreSelected(false);

				}

			} else if (sAspectInfo.equals(BUTTON_AG_NEU_NUMMERIEREN)) {

				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("stkl.arbeitsplan.neunummerieren.frage"));

				if (b == true) {

					DelegateFactory.getInstance().getStuecklisteDelegate()
							.arbeitsgaengeNeuNummerieren(getInternalFrameStueckliste().getStuecklisteDto().getIId());

					panelQueryArbeitsplan.eventYouAreSelected(false);

				}

			} else if (sAspectInfo.endsWith(BUTTON_IMPORTSTUECKLISTEPOSITIONEN_XLS)) {
				bPositionen = true;

				ArbeitsplatzparameterDto parameter = DelegateFactory.getInstance().getParameterDelegate()
						.holeArbeitsplatzparameter(
								ParameterFac.ARBEITSPLATZPARAMETER_DATEI_AUTOMATISCHE_STUECKLISTENERZEUGUNG);

				if (parameter != null && parameter.getCWert() != null && parameter.getCWert().trim().length() > 0) {

					String sPfad = parameter.getCWert();

					File f = new File(sPfad);

					if (f.exists() == false) {
						DialogFactory.showModalDialog("Fehler",
								"Die angegebene Datei '" + sPfad + "' existiert nicht.");
						return;
					}

					Workbook workbook = Workbook.getWorkbook(f);
					Sheet sheet = workbook.getSheet("HV_Materialpositionen");

					if (sheet == null) {
						DialogFactory.showModalDialog("Info",
								"Die Datei enth\u00E4lt kein Tabellenblatt mit dem Namen 'HV_Materialpositionen'");
						return;
					}

					MontageartDto[] stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
							.montageartFindByMandantCNr();

					for (int i = 1; i < sheet.getRows(); i++) {
						Cell[] cells = sheet.getRow(i);

						if (cells.length < 6) {
							continue;
						}
						String artikelnummer = cells[0].getContents();
						ArtikelDto aDto = null;
						try {
							aDto = DelegateFactory.getInstance().getArtikelDelegate().artikelFindByCNr(artikelnummer);
						} catch (Exception e) {
						}

						String einheitCnr = cells[6].getContents();
						EinheitDto einheitDto = DelegateFactory.getInstance().getSystemDelegate()
								.einheitFindByPrimaryKey(cells[6].getContents());
						BigDecimal menge = null;

						Float fDimension1 = null;
						Float fDimension2 = null;
						Float fDimension3 = null;

						if (cells[1].getType() == CellType.NUMBER || cells[1].getType() == CellType.NUMBER_FORMULA) {
							menge = new BigDecimal(((NumberCell) cells[1]).getValue());
						} else {
							continue;
						}

						if (cells[2].getType() == CellType.NUMBER || cells[2].getType() == CellType.NUMBER_FORMULA) {
							fDimension1 = new Float(((NumberCell) cells[2]).getValue());
						}
						if (cells[3].getType() == CellType.NUMBER || cells[3].getType() == CellType.NUMBER_FORMULA) {
							fDimension2 = new Float(((NumberCell) cells[3]).getValue());
						}
						if (cells[4].getType() == CellType.NUMBER || cells[4].getType() == CellType.NUMBER_FORMULA) {
							fDimension3 = new Float(((NumberCell) cells[4]).getValue());
						}

						StuecklistepositionDto posDto = new StuecklistepositionDto();
						if (aDto != null) {
							posDto.setArtikelIId(aDto.getIId());
							posDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
						} else {
							posDto.setSHandeingabe(cells[5].getContents());
							posDto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
						}

						posDto.setEinheitCNr(einheitCnr);
						posDto.setNMenge(menge);
						posDto.setMontageartIId(stklDto[0].getIId());
						posDto.setFDimension1(fDimension2);
						posDto.setFDimension2(fDimension3);
						posDto.setFDimension3(fDimension1);
						posDto.setStuecklisteIId(getInternalFrameStueckliste().getStuecklisteDto().getIId());

						if (cells.length > 7 && cells[7].getContents() != null) {

							if (cells[7].getContents().length() > 39) {
								posDto.setCPosition(cells[7].getContents().substring(0, 38));
							} else {
								posDto.setCPosition(cells[7].getContents());
							}
						}

						if (cells.length > 8 && cells[8].getContents() != null) {

							if (cells[8].getContents().length() > 79) {
								posDto.setCKommentar(cells[8].getContents().substring(0, 78));
							} else {
								posDto.setCKommentar(cells[8].getContents());
							}
						}

						if (cells.length > 9 && cells[9].getContents() != null && cells[9].getContents().equals("1")) {
							posDto.setBMitdrucken(new Short((short) 1));
						} else {
							posDto.setBMitdrucken(new Short((short) 0));
						}

						DelegateFactory.getInstance().getStuecklisteDelegate().createStuecklisteposition(posDto);

					}
					sheet = workbook.getSheet("HV_Arbeitsplan");

					if (sheet == null) {
						DialogFactory.showModalDialog("Info",
								"Die Datei enth\u00E4lt kein Tabellenblatt mit dem Namen 'HV_Arbeitsplan'");
						return;
					}

					for (int i = 1; i < sheet.getRows(); i++) {
						Cell[] cells = sheet.getRow(i);

						if (cells.length < 5) {
							continue;
						}

						String artikelnummer = cells[0].getContents();

						ArtikelDto aDto = null;
						try {
							aDto = DelegateFactory.getInstance().getArtikelDelegate().artikelFindByCNr(artikelnummer);
						} catch (Exception e) {
							continue;
						}

						Integer iArbeitsgang = null;
						Integer iUnterArbeitsgang = null;

						Long stueckzeit = null;
						Long ruestzeit = null;

						if (cells[1].getType() == CellType.NUMBER || cells[1].getType() == CellType.NUMBER_FORMULA) {
							iArbeitsgang = new Integer((int) ((NumberCell) cells[1]).getValue());
						}

						if (iArbeitsgang == null) {
							iArbeitsgang = DelegateFactory.getInstance().getStuecklisteDelegate()
									.getNextArbeitsgang(getInternalFrameStueckliste().getStuecklisteDto().getIId());

						}

						if (cells[2].getType() == CellType.NUMBER || cells[2].getType() == CellType.NUMBER_FORMULA) {
							iUnterArbeitsgang = new Integer((int) ((NumberCell) cells[2]).getValue());
						}
						if (cells[3].getType() == CellType.NUMBER || cells[3].getType() == CellType.NUMBER_FORMULA) {
							stueckzeit = new Long((long) ((NumberCell) cells[3]).getValue());
						}
						if (cells[4].getType() == CellType.NUMBER || cells[4].getType() == CellType.NUMBER_FORMULA) {
							ruestzeit = new Long((long) ((NumberCell) cells[4]).getValue());
						}

						StuecklistearbeitsplanDto posDto = new StuecklistearbeitsplanDto();
						posDto.setArtikelIId(aDto.getIId());

						posDto.setIArbeitsgang(iArbeitsgang);
						posDto.setIUnterarbeitsgang(iUnterArbeitsgang);
						posDto.setLRuestzeit(ruestzeit);
						posDto.setLStueckzeit(stueckzeit);
						posDto.setStuecklisteIId(getInternalFrameStueckliste().getStuecklisteDto().getIId());
						posDto.setBNurmaschinenzeit(new Short((short) 0));

						if (cells.length > 5 && cells[5].getContents() != null) {
							MaschineDto mDto;
							try {
								mDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
										.maschineFindByCIdentifikationsnr(cells[5].getContents());
								posDto.setMaschineIId(mDto.getIId());
							} catch (Exception e) {
							}
						}

						if (cells.length > 6 && cells[6].getContents() != null) {

							if (cells[6].getContents().length() > 79) {
								posDto.setCKommentar(cells[6].getContents().substring(0, 78));
							} else {
								posDto.setCKommentar(cells[6].getContents());
							}
						}

						if (cells.length > 7) {
							posDto.setXLangtext(cells[6].getContents());
						}

						DelegateFactory.getInstance().getStuecklisteDelegate().createStuecklistearbeitsplan(posDto);

					}

					panelQueryPositionen.eventYouAreSelected(false);
				}
			} else if (sAspectInfo.endsWith(ACTION_SPECIAL_ARBEITSPLAN)) {
				bPositionen = false;
				dialogStueckliste(eI);
			} else if (sAspectInfo.endsWith(ACTION_PRUEFPLAN_ERZEUGEN)) {

				int iAzahlSelektiert = panelQueryPositionen.getSelectedIds().length;

				panelQueryPositionen.getSelectedId();
				panelQueryPositionen.getSelectedIds();

			} else if (sAspectInfo.equals(BUTTON_IMPORTCSV_STUECKLISTEPOSITIONEN)
					|| sAspectInfo.equals(BUTTON_IMPORTCSV_STUECKLISTEARBEISTPLAN)) {
				if (LPMain.getInstance().getDesktop().darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_INTELLIGENTER_STUECKLISTENIMPORT)
						&& sAspectInfo.equals(BUTTON_IMPORTCSV_STUECKLISTEPOSITIONEN)) {
					AssistentView av = new AssistentView(getInternalFrameStueckliste(),
							LPMain.getTextRespectUISPr("stkl.intelligenterstklimport"),
							new StklImportController(getInternalFrameStueckliste().getStuecklisteDto().getIId(),
									StklImportSpezifikation.SpezifikationsTyp.FERTIGUNGSSTKL_SPEZ, getInternalFrame()));
					getInternalFrame().showPanelDialog(av);
				} else {
					// PJ 14984
					HvOptional<CsvFile> csvFile = FileOpenerFactory.stklPositionenImportCsv(this);
					if (csvFile.isPresent()) {
						// AD: Smooks Test
						/*
						 * String messageIn = SmooksHelper.readInputMessage(file. getAbsolutePath());
						 * try { List<?> smlist = SmooksHelper
						 * .runSmooksTransform("c:/stueckliste-smooks-config.xml" , messageIn);
						 * System.out.println(smlist.toString()); } catch (Exception e1) { // TODO
						 * Auto-generated catch block e1.printStackTrace(); }
						 */

						// Tab-getrenntes File einlesen.
						LPCSVReader reader = csvFile.get().createLPCSVReader();

						if (sAspectInfo.equals(BUTTON_IMPORTCSV_STUECKLISTEPOSITIONEN)) {
							// CSV-Format fuer Arbeitsplan:
							// Spalte1: ARTIKELNUMMER
							// Spalte2: MENGE
							// PJ 13479: Damit man Handartikel importieren kann

							// Spalte3: BEZEICHNUNG-WENN HANDARTIKEL
							// Spalte4: EINHEIT-WENN HANDARTIKEL
							// Spalte5: C_POSITION
							// Spalte6: C_KOMMENTAR
							// Spalte7: B_MITDRUCKEN (PJ 14580)
							if (panelBottomPositionen.defaultMontageartDto == null) {
								// DefaultMontageart==null
								return;
							}

							// PJ18091 Abfrage, ob Positionen vorher geloescht
							// werden sollen

							boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
									textFromToken("stk.positionen.csvimport.positionenvorherloeschen"));
							if (b == true) {
								DelegateFactory.getInstance().getStuecklisteDelegate().removeAlleStuecklistenpositionen(
										getInternalFrameStueckliste().getStuecklisteDto().getIId());
							}

							String[] sLine;
							int iZeile = 0;
							ArrayList<StuecklistepositionDto> list = new ArrayList<StuecklistepositionDto>();
							do {
								// zeilenweise einlesen.
								sLine = reader.readNext();
								iZeile++;
								if (sLine != null) {
									if (sLine.length < 7) {
										DialogFactory.showModalDialog(textFromToken("lp.error"),
												"CSV-Datei muss genau 7 Spalten beinhalten.");
										return;
									}

									StuecklistepositionDto dto = new StuecklistepositionDto();
									dto.setBMitdrucken(Helper.boolean2Short(true));
									dto.setMontageartIId(panelBottomPositionen.defaultMontageartDto.getIId());
									dto.setStuecklisteIId(getInternalFrameStueckliste().getStuecklisteDto().getIId());

									ArtikelDto artikelDto = null;
									try {
										artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
												.artikelFindByCNr(sLine[0]);
										dto.setArtikelIId(artikelDto.getIId());
										dto.setEinheitCNr(artikelDto.getEinheitCNr());
										dto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
										dto.setCPosition(sLine[4]);
										dto.setCKommentar(sLine[5]);
										try {
											dto.setNMenge(new BigDecimal(sLine[1]));

											try {

												Short s = new Short(sLine[6]);

												if (s != 0 && s != 1) {
													throw new NumberFormatException();
												}

												dto.setBMitdrucken(s);
												list.add(dto);
											} catch (NumberFormatException ex) {
												DialogFactory.showModalDialog(textFromToken("lp.error"),
														"Keine g\u00FCltiger Wert f\u00FCr 'Mitdrucken' (entweder 0 oder 1) in Zeile "
																+ iZeile + ", Spalte 7. Zeile wird \u00FCbersprungen.");
											}
										} catch (NumberFormatException ex) {
											DialogFactory.showModalDialog(textFromToken("lp.error"),
													"Keine g\u00FCltige Zahl in Zeile " + iZeile
															+ ", Spalte 2. Zeile wird \u00FCbersprungen.");
										}

									} catch (ExceptionLP ex1) {
										if (ex1.getICode() == EJBExceptionLP.FEHLER_BEI_FIND) {

											if (sLine[2] != null && sLine[2].length() > 0) {
												// Handartikel anlegen
												dto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
												dto.setSHandeingabe(sLine[2]);
												try {
													dto.setNMenge(new BigDecimal(sLine[1]));
													dto.setCPosition(sLine[4]);
													dto.setCKommentar(sLine[5]);

													try {
														DelegateFactory.getInstance().getSystemDelegate()
																.einheitFindByPrimaryKey(sLine[3]);
														dto.setEinheitCNr(sLine[3]);

														try {

															Short s = new Short(sLine[6]);

															if (s != 0 && s != 1) {
																throw new NumberFormatException();
															}

															dto.setBMitdrucken(s);
															list.add(dto);
														} catch (NumberFormatException ex) {
															DialogFactory.showModalDialog(textFromToken("lp.error"),
																	"Keine g\u00FCltiger Wert f\u00FCr 'Mitdrucken' (entweder 0 oder 1) in Zeile "
																			+ iZeile
																			+ ", Spalte 7. Zeile wird \u00FCbersprungen.");
														}

													} catch (ExceptionLP e) {
														if (ex1.getICode() == EJBExceptionLP.FEHLER_BEI_FIND) {
															DialogFactory.showModalDialog(textFromToken("lp.error"),
																	"Keine g\u00FCltige Einheit " + sLine[3]
																			+ " in Zeile " + iZeile
																			+ ", Spalte 4. Zeile wird \u00FCbersprungen.");
														} else {
															handleException(e, true);
														}
													}

												} catch (NumberFormatException ex) {
													DialogFactory.showModalDialog(textFromToken("lp.error"),
															"Keine g\u00FCltige Zahl in Zeile " + iZeile
																	+ ", Spalte 2. Zeile wird \u00FCbersprungen.");
												}
											}

										} else {
											handleException(ex1, true);
										}
									}
								}
							} while (sLine != null);

							if (list.size() > 0) {
								StuecklistepositionDto[] returnArray = new StuecklistepositionDto[list.size()];
								returnArray = (StuecklistepositionDto[]) list.toArray(returnArray);
								DelegateFactory.getInstance().getStuecklisteDelegate()
										.createStuecklistepositions(returnArray);
								panelSplitPositionen.eventYouAreSelected(false);
							}
						} else if (sAspectInfo.equals(BUTTON_IMPORTCSV_STUECKLISTEARBEISTPLAN)) {
							// CSV-Format fuer Arbeitsplan:
							// Spalte1: ARTIKELNUMMER
							// Spalte2: STUECKZEIT IN MILLISEKUNDEN
							// Spalte3: RUESTZEIT IN MILLISEKUNDEN

							String[] sLine;
							int iZeile = 0;
							ArrayList<StuecklistearbeitsplanDto> list = new ArrayList<StuecklistearbeitsplanDto>();
							do {
								// zeilenweise einlesen.
								sLine = reader.readNext();
								iZeile++;
								if (sLine != null) {
									if (sLine.length < 3) {
										DialogFactory.showModalDialog(textFromToken("lp.error"),
												"CSV-Datei muss genau 3 Spalten beinhalten.");
										return;

									}
									StuecklistearbeitsplanDto dto = new StuecklistearbeitsplanDto();
									dto.setBNurmaschinenzeit(Helper.boolean2Short(false));
									dto.setStuecklisteIId(getInternalFrameStueckliste().getStuecklisteDto().getIId());

									Integer i = DelegateFactory.getInstance().getStuecklisteDelegate()
											.getNextArbeitsgang(
													getInternalFrameStueckliste().getStuecklisteDto().getIId());

									if (i != null) {
										dto.setIArbeitsgang(i);
									} else {
										dto.setIArbeitsgang(new Integer(10));
									}

									ArtikelDto artikelDto = null;
									try {
										artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
												.artikelFindByCNr(sLine[0]);
										dto.setArtikelIId(artikelDto.getIId());
										try {
											dto.setLStueckzeit(new Long(sLine[1]));
											dto.setLRuestzeit(new Long(sLine[2]));

											if (artikelDto.getArtikelartCNr()
													.equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {

												list.add(dto);
											} else {
												DialogFactory.showModalDialog(textFromToken("lp.error"), "Artikel '"
														+ sLine[0] + "' in Zeile " + iZeile
														+ ", Spalte 1 ist kein Arbeitszeitartikel. Zeile wird \u00FCbersprungen.");

											}
										} catch (NumberFormatException ex) {
											DialogFactory.showModalDialog(textFromToken("lp.error"),
													"Keine g\u00FCltige Zahl in Zeile " + iZeile
															+ ", Spalte 2/3. Zeile wird \u00FCbersprungen.");
										}
									} catch (ExceptionLP ex1) {
										if (ex1.getICode() == EJBExceptionLP.FEHLER_BEI_FIND) {
											DialogFactory.showModalDialog(textFromToken("lp.error"), "Artikel '"
													+ sLine[0] + "' in Zeile " + iZeile
													+ ", Spalte 1 nicht gefunden. Zeile wird \u00FCbersprungen.");
										} else {
											handleException(ex1, true);
										}
									}
								}
							} while (sLine != null);

							if (list.size() > 0) {
								StuecklistearbeitsplanDto[] returnArray = new StuecklistearbeitsplanDto[list.size()];
								returnArray = (StuecklistearbeitsplanDto[]) list.toArray(returnArray);
								DelegateFactory.getInstance().getStuecklisteDelegate()
										.createStuecklistearbeitsplans(returnArray);
								panelSplitArbeitsplan.eventYouAreSelected(false);
							}
						}
					} else {
						// keine auswahl
					}
				}
			} else if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				einfuegenHV();
			} else if (eI.getSource() == panelQueryStueckliste && sAspectInfo.endsWith(EXTRA_NEU_AUS_STUECKLISTE)) {
				createDetail((Integer) panelQueryStueckliste.getSelectedId());

				if (panelQueryStueckliste.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				// Neu aus Artikel.
				Integer stuecklisteIId = (Integer) panelQueryStueckliste.getSelectedId();

				if (stuecklisteIId != null) {

					StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
							.stuecklisteFindByPrimaryKey(stuecklisteIId);

					DialogArtikelkopieren d = new DialogArtikelkopieren(DelegateFactory.getInstance()
							.getArtikelDelegate().artikelFindByPrimaryKey(stklDto.getArtikelIId()),
							getInternalFrameStueckliste());
					LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);
					if (d.getArtikelnummerNeu() != null) {
						Object[] o = DelegateFactory.getInstance().getStuecklisteDelegate().kopiereStueckliste(
								stklDto.getIId(), d.getArtikelnummerNeu(), d.getZuKopierendeFelder(),
								d.getHerstellerIIdNeu(), null);

						Integer steucklisteIId_Neu = (Integer) o[0];

						panelQueryStueckliste.setSelectedId(steucklisteIId_Neu);
						panelQueryStueckliste.eventYouAreSelected(false);

						boolean bAndereSprachenKopiert = (Boolean) o[1];

						if (bAndereSprachenKopiert == true) {

							DialogFactory.showModalDialog(textFromToken("lp.info"),
									textFromToken("artikel.info.mehreresprachenkopiert"));
						}

						boolean bEsSindZukuenftigePreisevorhanden = (Boolean) o[2];
						if (bEsSindZukuenftigePreisevorhanden == true) {

							DialogFactory.showModalDialog(textFromToken("lp.info"),
									textFromToken("artikel.info.zukuenftigepreisekopiert"));
						}
					}
				}

			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelDetailStueckliste) {
				panelQueryStueckliste.clearDirektFilter();
				Object oKey = panelDetailStueckliste.getKeyWhenDetailPanel();
				panelQueryStueckliste.setSelectedId(oKey);
			} else if (eI.getSource() == panelBottomPositionen) {
				Object oKey = panelBottomPositionen.getKeyWhenDetailPanel();
				panelQueryPositionen.eventYouAreSelected(false);
				panelQueryPositionen.setSelectedId(oKey);
				panelSplitPositionen.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomPositionenErsatz) {
				Object oKey = panelBottomPositionenErsatz.getKeyWhenDetailPanel();
				panelQueryPositionenErsatz.eventYouAreSelected(false);
				panelQueryPositionenErsatz.setSelectedId(oKey);
				panelSplitPositionenErsatz.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomAlternativmaschine) {
				Object oKey = panelBottomAlternativmaschine.getKeyWhenDetailPanel();
				panelQueryAlternativmaschine.eventYouAreSelected(false);
				panelQueryAlternativmaschine.setSelectedId(oKey);
				panelSplitAlternativmaschine.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomEigenschaft) {
				Object oKey = panelBottomEigenschaft.getKeyWhenDetailPanel();
				panelQueryEigenschaft.eventYouAreSelected(false);
				panelQueryEigenschaft.setSelectedId(oKey);
				panelSplitEigenschaft.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomParameter) {
				Object oKey = panelBottomParameter.getKeyWhenDetailPanel();
				panelQueryParameter.eventYouAreSelected(false);
				panelQueryParameter.setSelectedId(oKey);
				panelSplitParameter.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomArbeitsplan) {
				Object oKey = panelBottomArbeitsplan.getKeyWhenDetailPanel();
				panelQueryArbeitsplan.eventYouAreSelected(false);
				panelQueryArbeitsplan.setSelectedId(oKey);
				panelSplitArbeitsplan.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomAbbuchungslager) {
				Object oKey = panelBottomAbbuchungslager.getKeyWhenDetailPanel();
				panelQueryAbbuchungslager.eventYouAreSelected(false);
				panelQueryAbbuchungslager.setSelectedId(oKey);
				panelSplitAbbuchungslager.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomPruefplan) {
				Object oKey = panelBottomPruefplan.getKeyWhenDetailPanel();
				panelQueryPruefplan.eventYouAreSelected(false);
				panelQueryPruefplan.setSelectedId(oKey);
				panelSplitPruefplan.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelQueryStueckliste) {
				if (panelQueryStueckliste.getSelectedId() != null) {
					getInternalFrameStueckliste().setKeyWasForLockMe(panelQueryStueckliste.getSelectedId() + "");

					// Dto-setzen
					createDetail((Integer) panelQueryStueckliste.getSelectedId());
					panelDetailStueckliste.setKeyWhenDetailPanel(panelQueryStueckliste.getSelectedId());
					getInternalFrameStueckliste()
							.setStuecklisteDto(DelegateFactory.getInstance().getStuecklisteDelegate()
									.stuecklisteFindByPrimaryKey((Integer) panelQueryStueckliste.getSelectedId()));

					if (getInternalFrameStueckliste().getStuecklisteDto() != null) {

						String sLosgroesse = "0";
						if (getInternalFrameStueckliste().getStuecklisteDto().getNLosgroesse() != null) {
							sLosgroesse = getInternalFrameStueckliste().getStuecklisteDto().getNLosgroesse().intValue()
									+ "";
						}

						// PJ18550
						if (bStuecklistenfreigabe == true && getInternalFrameStueckliste().getStuecklisteDto() != null
								&& getInternalFrameStueckliste().getStuecklisteDto().getTFreigabe() == null) {
							boolean hatRecht = DelegateFactory.getInstance().getTheJudgeDelegate()
									.hatRecht(RechteFac.RECHT_STK_STUECKLISTE_CUD);
							if (hatRecht) {
								getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_UPDATE);
							}
						}

						refreshTitle();
					}

					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL,
							((ISourceEvent) eI.getSource()).getIdSelected() != null);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
				}
			} else if (eI.getSource() == panelQueryPositionen) {
				Integer iId = (Integer) panelQueryPositionen.getSelectedId();
				panelBottomPositionen.setKeyWhenDetailPanel(iId);
				panelBottomPositionen.eventYouAreSelected(false);
				panelQueryPositionen.updateButtons();

				if (panelQueryPositionen.getSelectedIds() != null
						&& (panelQueryPositionen.getSelectedIds().length == 2
								|| panelQueryPositionen.getSelectedIds().length == 1)
						&& panelQueryPositionen.getHmOfButtons().get(BUTTON_PRUEFPLAN_ERZEUGEN) != null) {

					panelQueryPositionen.getHmOfButtons().get(BUTTON_PRUEFPLAN_ERZEUGEN).shouldBeEnabled();
				} else {
					if (panelQueryPositionen.getHmOfButtons().get(BUTTON_PRUEFPLAN_ERZEUGEN) != null) {
						panelQueryPositionen.getHmOfButtons().get(BUTTON_PRUEFPLAN_ERZEUGEN).getButton()
								.setEnabled(false);
					}
				}

				//

			} else if (eI.getSource() == panelQueryPositionenErsatz) {
				Integer iId = (Integer) panelQueryPositionenErsatz.getSelectedId();
				panelBottomPositionenErsatz.setKeyWhenDetailPanel(iId);
				panelBottomPositionenErsatz.eventYouAreSelected(false);
				panelQueryPositionenErsatz.updateButtons();
			} else if (eI.getSource() == panelQueryAlternativmaschine) {
				Integer iId = (Integer) panelQueryAlternativmaschine.getSelectedId();
				panelBottomAlternativmaschine.setKeyWhenDetailPanel(iId);
				panelBottomAlternativmaschine.eventYouAreSelected(false);
				panelQueryAlternativmaschine.updateButtons();
			} else if (eI.getSource() == panelQueryEigenschaft) {
				Integer iId = (Integer) panelQueryEigenschaft.getSelectedId();
				panelBottomEigenschaft.setKeyWhenDetailPanel(iId);
				panelBottomEigenschaft.eventYouAreSelected(false);
				panelQueryEigenschaft.updateButtons();
			} else if (eI.getSource() == panelQueryParameter) {
				Integer iId = (Integer) panelQueryParameter.getSelectedId();
				panelBottomParameter.setKeyWhenDetailPanel(iId);
				panelBottomParameter.eventYouAreSelected(false);
				panelQueryParameter.updateButtons();
			} else if (eI.getSource() == panelQueryArbeitsplan) {
				Integer iId = (Integer) panelQueryArbeitsplan.getSelectedId();
				panelBottomArbeitsplan.setKeyWhenDetailPanel(iId);
				panelBottomArbeitsplan.eventYouAreSelected(false);
				panelQueryArbeitsplan.updateButtons();
			} else if (eI.getSource() == panelQueryAbbuchungslager) {
				Integer iId = (Integer) panelQueryAbbuchungslager.getSelectedId();
				panelBottomAbbuchungslager.setKeyWhenDetailPanel(iId);
				panelBottomAbbuchungslager.eventYouAreSelected(false);
				panelQueryAbbuchungslager.updateButtons();
			} else if (eI.getSource() == panelQueryPruefplan) {
				Integer iId = (Integer) panelQueryPruefplan.getSelectedId();
				panelBottomPruefplan.setKeyWhenDetailPanel(iId);
				panelBottomPruefplan.eventYouAreSelected(false);
				panelQueryPruefplan.updateButtons();
			}

		}

		else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (eI.getSource() == panelBottomPositionen) {
				panelQueryPositionen.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBottomArbeitsplan) {
				panelQueryArbeitsplan.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBottomAbbuchungslager) {
				panelQueryAbbuchungslager.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBottomEigenschaft) {
				panelQueryEigenschaft.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBottomParameter) {
				panelQueryParameter.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBottomPositionenErsatz) {
				panelQueryPositionenErsatz.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBottomAlternativmaschine) {
				panelQueryAlternativmaschine.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBottomPruefplan) {
				panelQueryPruefplan.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (eI.getSource() == panelQueryPositionen) {
				int iPos = panelQueryPositionen.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryPositionen.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryPositionen.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getStuecklisteDelegate().vertauscheStuecklisteposition(iIdPosition,
							iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryPositionen.setSelectedId(iIdPosition);
				}
			} else if (eI.getSource() == panelQueryPositionenErsatz) {
				int iPos = panelQueryPositionenErsatz.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryPositionenErsatz.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryPositionenErsatz.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getStuecklisteDelegate().vertauschePosersatz(iIdPosition,
							iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryPositionenErsatz.setSelectedId(iIdPosition);
				}
			} else if (eI.getSource() == panelQueryParameter) {
				int iPos = panelQueryParameter.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryParameter.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryParameter.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getStuecklisteDelegate().vertauscheStklparameter(iIdPosition,
							iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryParameter.setSelectedId(iIdPosition);
				}
			} else if (eI.getSource() == panelQueryAlternativmaschine) {
				int iPos = panelQueryAlternativmaschine.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryAlternativmaschine.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryAlternativmaschine.getTable().getValueAt(iPos - 1,
							0);

					DelegateFactory.getInstance().getStuecklisteDelegate().vertauscheAlternativmaschine(iIdPosition,
							iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryAlternativmaschine.setSelectedId(iIdPosition);
				}
			} else if (eI.getSource() == panelQueryPruefplan) {
				int iPos = panelQueryPruefplan.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryPruefplan.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryPruefplan.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getStuecklisteDelegate().vertauscheStklpruefplan(iIdPosition,
							iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryPruefplan.setSelectedId(iIdPosition);
				}
			} else if (eI.getSource() == panelQueryAbbuchungslager) {
				int iPos = panelQueryAbbuchungslager.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryAbbuchungslager.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryAbbuchungslager.getTable().getValueAt(iPos - 1, 0);
					DelegateFactory.getInstance().getStuecklisteDelegate().vertauscheStklagerentnahme(iIdPosition,
							iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren

					panelQueryAbbuchungslager.setSelectedId(iIdPosition);
				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (eI.getSource() == panelQueryPositionen) {
				int iPos = panelQueryPositionen.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryPositionen.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryPositionen.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryPositionen.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getStuecklisteDelegate().vertauscheStuecklisteposition(iIdPosition,
							iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryPositionen.setSelectedId(iIdPosition);
				}
			} else if (eI.getSource() == panelQueryPositionenErsatz) {
				int iPos = panelQueryPositionenErsatz.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryPositionenErsatz.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryPositionenErsatz.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryPositionenErsatz.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getStuecklisteDelegate().vertauschePosersatz(iIdPosition,
							iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryPositionenErsatz.setSelectedId(iIdPosition);
				}
			} else if (eI.getSource() == panelQueryParameter) {
				int iPos = panelQueryParameter.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryParameter.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryParameter.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryParameter.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getStuecklisteDelegate().vertauscheStklparameter(iIdPosition,
							iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryParameter.setSelectedId(iIdPosition);
				}
			} else if (eI.getSource() == panelQueryAlternativmaschine) {
				int iPos = panelQueryAlternativmaschine.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryAlternativmaschine.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryAlternativmaschine.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryAlternativmaschine.getTable().getValueAt(iPos + 1,
							0);

					DelegateFactory.getInstance().getStuecklisteDelegate().vertauscheAlternativmaschine(iIdPosition,
							iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryAlternativmaschine.setSelectedId(iIdPosition);
				}
			} else if (eI.getSource() == panelQueryPruefplan) {
				int iPos = panelQueryPruefplan.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryPruefplan.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryPruefplan.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryPruefplan.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getStuecklisteDelegate().vertauscheStklpruefplan(iIdPosition,
							iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryPruefplan.setSelectedId(iIdPosition);
				}
			} else if (eI.getSource() == panelQueryAbbuchungslager) {
				int iPos = panelQueryAbbuchungslager.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryAbbuchungslager.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryAbbuchungslager.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryAbbuchungslager.getTable().getValueAt(iPos + 1, 0);
					DelegateFactory.getInstance().getStuecklisteDelegate().vertauscheStklagerentnahme(iIdPosition,
							iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryAbbuchungslager.setSelectedId(iIdPosition);
				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (eI.getSource() == panelQueryPositionen) {
				panelBottomPositionen.eventActionNew(eI, true, false);
				panelBottomPositionen.eventYouAreSelected(false); // Buttons
				// schalten
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (eI.getSource() == panelDetailStueckliste) {
				this.setSelectedComponent(panelQueryStueckliste);
				setKeyWasForLockMe();
				panelQueryStueckliste.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomPositionen) {
				setKeyWasForLockMe();
				if (panelBottomPositionen.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPositionen.getId2SelectAfterDelete();
					panelQueryPositionen.setSelectedId(oNaechster);
				}
				panelSplitPositionen.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomPositionenErsatz) {
				setKeyWasForLockMe();
				if (panelBottomPositionenErsatz.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPositionenErsatz.getId2SelectAfterDelete();
					panelQueryPositionenErsatz.setSelectedId(oNaechster);
				}
				panelSplitPositionenErsatz.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomAlternativmaschine) {
				setKeyWasForLockMe();
				if (panelBottomAlternativmaschine.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryAlternativmaschine.getId2SelectAfterDelete();
					panelQueryAlternativmaschine.setSelectedId(oNaechster);
				}
				panelSplitAlternativmaschine.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomEigenschaft) {
				setKeyWasForLockMe();
				if (panelBottomEigenschaft.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryEigenschaft.getId2SelectAfterDelete();
					panelQueryEigenschaft.setSelectedId(oNaechster);
				}
				panelSplitEigenschaft.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomParameter) {
				setKeyWasForLockMe();
				if (panelBottomParameter.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryParameter.getId2SelectAfterDelete();
					panelQueryParameter.setSelectedId(oNaechster);
				}
				panelSplitParameter.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomArbeitsplan) {
				setKeyWasForLockMe();
				if (panelBottomArbeitsplan.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryArbeitsplan.getId2SelectAfterDelete();
					panelQueryArbeitsplan.setSelectedId(oNaechster);
				}
				panelSplitArbeitsplan.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomAbbuchungslager) {
				setKeyWasForLockMe();
				if (panelBottomAbbuchungslager.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryAbbuchungslager.getId2SelectAfterDelete();
					panelQueryAbbuchungslager.setSelectedId(oNaechster);
				}
				panelSplitAbbuchungslager.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomPruefplan) {
				setKeyWasForLockMe();
				if (panelBottomPruefplan.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPruefplan.getId2SelectAfterDelete();
					panelQueryPruefplan.setSelectedId(oNaechster);
				}
				panelSplitPruefplan.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			if (eI.getSource() instanceof AssistentView)
				getAktuellesPanel().eventYouAreSelected(false);
			refreshTitle();
			super.lPEventItemChanged(eI);
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelBottomPositionen) {
				panelSplitPositionen.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomEigenschaft) {
				panelSplitEigenschaft.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomArbeitsplan) {
				panelSplitArbeitsplan.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomAbbuchungslager) {
				panelSplitAbbuchungslager.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomPositionenErsatz) {
				panelSplitPositionenErsatz.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomPruefplan) {
				panelSplitPruefplan.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomAlternativmaschine) {
				panelSplitAlternativmaschine.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomParameter) {
				panelSplitParameter.eventYouAreSelected(false);
			}

		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelQueryStueckliste) {
				createDetail(null);
				if (panelQueryStueckliste.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailStueckliste.eventActionNew(eI, true, false);
				setSelectedComponent(panelDetailStueckliste);

			} else if (eI.getSource() == panelQueryPositionen) {

				panelBottomPositionen.eventActionNew(eI, true, false);
				panelBottomPositionen.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitPositionen);

			} else if (eI.getSource() == panelQueryPositionenErsatz) {

				panelBottomPositionenErsatz.eventActionNew(eI, true, false);
				panelBottomPositionenErsatz.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitPositionenErsatz);

			} else if (eI.getSource() == panelQueryAlternativmaschine) {

				panelBottomAlternativmaschine.eventActionNew(eI, true, false);
				panelBottomAlternativmaschine.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitAlternativmaschine);

			} else if (eI.getSource() == panelQueryEigenschaft) {

				panelBottomEigenschaft.eventActionNew(eI, true, false);
				panelBottomEigenschaft.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitEigenschaft);

			} else if (eI.getSource() == panelQueryParameter) {

				panelBottomParameter.eventActionNew(eI, true, false);
				panelBottomParameter.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitParameter);

			} else if (eI.getSource() == panelQueryArbeitsplan) {

				panelBottomArbeitsplan.eventActionNew(eI, true, false);
				panelBottomArbeitsplan.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitArbeitsplan);

			} else if (eI.getSource() == panelQueryAbbuchungslager) {

				panelBottomAbbuchungslager.eventActionNew(eI, true, false);
				panelBottomAbbuchungslager.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitAbbuchungslager);

			} else if (eI.getSource() == panelQueryPruefplan) {

				panelBottomPruefplan.eventActionNew(eI, true, false);
				panelBottomPruefplan.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitPruefplan);

			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			copyHV();
		} else if (eI.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			if (eI.getSource() == panelQueryPositionen) {
				String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();
				if (sAspectInfo.endsWith(BUTTON_PRUEFPLAN_ERZEUGEN)) {

					if (panelQueryPositionen.getSelectedIds() != null
							&& (panelQueryPositionen.getSelectedIds().length == 1
									|| panelQueryPositionen.getSelectedIds().length == 2)) {

						// Wenn Nur eine Position selektiert worden ist, dann
						// alle Pruefarten ausser Crimpen anzeigen, wo
						// artikel_i_id_kontakt gleich wie artikel_i_is aus
						// stk-Position ist
						StuecklistepositionDto sktlposDtoKontakt = DelegateFactory.getInstance()
								.getStuecklisteDelegate().stuecklistepositionFindByPrimaryKey(
										(Integer) panelQueryPositionen.getSelectedIds()[0]);

						FilterKriterium[] kriterien = null;

						if (panelQueryPositionen.getSelectedIds().length == 1) {

							kriterien = new FilterKriterium[1];
							StuecklistepositionDto sktlposDto = DelegateFactory.getInstance().getStuecklisteDelegate()
									.stuecklistepositionFindByPrimaryKey(
											(Integer) panelQueryPositionen.getSelectedIds()[0]);

							FilterKriterium krit1 = new FilterKriterium("BEIDE_RICHTUNGEN", true,
									sktlposDto.getArtikelIId() + "", FilterKriterium.OPERATOR_IN, false);

							kriterien[0] = krit1;

						} else {
							kriterien = new FilterKriterium[1];
							StuecklistepositionDto sktlposDtoLitze = DelegateFactory.getInstance()
									.getStuecklisteDelegate().stuecklistepositionFindByPrimaryKey(
											(Integer) panelQueryPositionen.getSelectedIds()[1]);

							FilterKriterium krit1 = new FilterKriterium("BEIDE_RICHTUNGEN", true,
									sktlposDtoKontakt.getArtikelIId() + " " + sktlposDtoLitze.getArtikelIId(),
									FilterKriterium.OPERATOR_IN, false);

							kriterien[0] = krit1;

						}

						String[] aWhichButtonIUse = null;

						panelQueryFLRPruefkombination = new PanelQueryFLR(null, kriterien,
								QueryParameters.UC_ID_PRUEFKOMBINATION, aWhichButtonIUse, getInternalFrame(),
								LPMain.getTextRespectUISPr("stk.pruefkombination"));

						new DialogQuery(panelQueryFLRPruefkombination);

					}
				}
			} else if (eI.getSource() == panelQueryStueckliste) {
				String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();

				if (sAspectInfo.endsWith(ACTION_SPECIAL_BAUMSICHT)) {
					DialogStuecklisteStruktur d = new DialogStuecklisteStruktur(getInternalFrameStueckliste(),
							panelQueryStueckliste.getWcbVersteckteFelderAnzeigen().isSelected());
					LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);
				}
			}
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_STUECKLISTE_MIT_FORMELN)) {

			if (getInternalFrameStueckliste().getStuecklisteDto() != null
					&& Helper.short2boolean(getInternalFrameStueckliste().getStuecklisteDto().getBMitFormeln()) == false
					&& getInternalFrameStueckliste().getStuecklisteDto().getStuecklisteIIdFormelstueckliste() == null) {

				setEnabledAt(IDX_PANEL_PARAMETER, false);

			}

			if (menuItemKonfigurieren != null) {
				if (getInternalFrameStueckliste().getStuecklisteDto() != null && Helper
						.short2boolean(getInternalFrameStueckliste().getStuecklisteDto().getBMitFormeln()) == true) {
					menuItemKonfigurieren.setEnabled(true);
				} else {
					menuItemKonfigurieren.setEnabled(false);
				}
			}

		}

	}

	public void copyHV() throws ExceptionLP, Throwable {
		int selectedPanelIndex = this.getSelectedIndex();

		if (selectedPanelIndex == IDX_PANEL_POSITIONEN) {
			ArrayList<Integer> aoIIdPosition = panelQueryPositionen.getSelectedIdsAsInteger();

			if (aoIIdPosition != null && (aoIIdPosition.size() > 0)) {
				StuecklistepositionDto[] dtos = new StuecklistepositionDto[aoIIdPosition.size()];
				for (int i = 0; i < aoIIdPosition.size(); i++) {
					dtos[i] = DelegateFactory.getInstance().getStuecklisteDelegate()
							.stuecklistepositionFindByPrimaryKey(aoIIdPosition.get(i));
				}
				LPMain.getPasteBuffer().writeObjectToPasteBuffer(dtos);
			}
		} else if (selectedPanelIndex == IDX_PANEL_ARBEITSPLAN) {
			
			ArrayList<Integer> aoIIdPosition = panelQueryArbeitsplan.getSelectedIdsAsInteger();

			if (aoIIdPosition != null && (aoIIdPosition.size() > 0)) {
				StuecklistearbeitsplanDto[] dtos = new StuecklistearbeitsplanDto[aoIIdPosition.size()];
				for (int i = 0; i < aoIIdPosition.size(); i++) {
					dtos[i] = DelegateFactory.getInstance().getStuecklisteDelegate()
							.stuecklistearbeitsplanFindByPrimaryKey(aoIIdPosition.get(i));
				}
				LPMain.getPasteBuffer().writeObjectToPasteBuffer(dtos);
			}
		}
	}

	public void einfuegenHV() throws IOException, ParserConfigurationException, SAXException, Throwable {

		Object o = LPMain.getPasteBuffer().readObjectFromPasteBuffer();

		int selectedPanelIndex = this.getSelectedIndex();
		if (o instanceof BelegpositionDto[]) {
			if (selectedPanelIndex == IDX_PANEL_POSITIONEN) {

				StuecklistepositionDto[] positionDtos = DelegateFactory.getInstance()
						.getBelegpostionkonvertierungDelegate().konvertiereNachStklpositionDto((BelegpositionDto[]) o);

				int iInserted = 0;
				if (positionDtos != null) {
					Integer iId = null;
					Boolean b = positionAmEndeEinfuegen();
					if (b != null) {

						for (int i = 0; i < positionDtos.length; i++) {
							StuecklistepositionDto positionDto = positionDtos[i];
							try {
								positionDto.setIId(null);
								positionDto.setBelegIId(getInternalFrameStueckliste().getStuecklisteDto().getIId());

								if (b == false) {
									Integer iIdAktuellePosition = (Integer) getPanelQueryPositionen().getSelectedId();

									// erstepos: 0 die erste Position steht an
									// der Stelle 1
									Integer iSortAktuellePosition = null;

									// erstepos: 1 die erste Position steht an
									// der Stelle 1
									if (iIdAktuellePosition != null) {
										iSortAktuellePosition = new Integer(1);
										iSortAktuellePosition = DelegateFactory.getInstance().getStuecklisteDelegate()
												.stuecklistepositionFindByPrimaryKey(iIdAktuellePosition).getISort();

										// Die bestehenden Positionen muessen
										// Platz fuer die neue schaffen
										DelegateFactory.getInstance().getStuecklisteDelegate()
												.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
														getInternalFrameStueckliste().getStuecklisteDto().getIId(),
														iSortAktuellePosition.intValue());
									}
									// Die neue Position wird an frei gemachte
									// Position gesetzt
									positionDto.setISort(iSortAktuellePosition);
								} else {
									positionDto.setISort(null);
								}

								// Wenn Handeingabe ArtikelID nicht mitkopieren
								if (positionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
									if (positionDto.getArtikelDto() != null) {
										positionDto.setSHandeingabe(positionDto.getArtikelDto().formatBezeichnung());
										positionDto.setArtikelDto(null);
										positionDto.setArtikelIId(null);
									}
								}

								// SP5751
								if (!Helper.short2boolean(
										getInternalFrameStueckliste().getStuecklisteDto().getBMitFormeln())) {
									positionDto.setXFormel(null);
								}

								// wir legen eine neue position an
								iId = DelegateFactory.getInstance().getStuecklisteDelegate()
										.createStuecklisteposition(positionDto);
								iInserted++;
							} catch (Throwable t) {
								// nur loggen!
								myLogger.error(t.getMessage(), t);
							}
						}
					}
					// den Datensatz in der Liste selektieren
					panelQueryPositionen.setSelectedId(iId);

					// die Liste neu aufbauen
					panelQueryPositionen.eventYouAreSelected(false);

					// im Detail den selektierten anzeigen
					panelSplitPositionen.eventYouAreSelected(false);
				}

			} else if (selectedPanelIndex == IDX_PANEL_ARBEITSPLAN) {

				StuecklistearbeitsplanDto[] positionDtos = DelegateFactory.getInstance()
						.getBelegpostionkonvertierungDelegate()
						.konvertiereNachStklarbeitsplanDto((BelegpositionDto[]) o);

				int iInserted = 0;
				if (positionDtos != null) {
					Integer iId = null;
					for (int i = 0; i < positionDtos.length; i++) {
						StuecklistearbeitsplanDto positionDto = positionDtos[i];
						try {
							positionDto.setIId(null);
							positionDto.setStuecklisteIId(getInternalFrameStueckliste().getStuecklisteDto().getIId());
							positionDto.setISort(null);

							// SP6517
							ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
									.getParameterDelegate()
									.getParametermandant(ParameterFac.PARAMETER_ARBEITSGANG_NUMMERIERUNG_BELASSEN,
											ParameterFac.KATEGORIE_STUECKLISTE, LPMain.getTheClient().getMandant());

							boolean bAGNrBelassen = (Boolean) parameter.getCWertAsObject();

							if (bAGNrBelassen == false) {
								// SP6203

								Integer iAG = DelegateFactory.getInstance().getStuecklisteDelegate()
										.getNextArbeitsgang(getInternalFrameStueckliste().getStuecklisteDto().getIId());
								if (iAG != null) {
									positionDto.setIArbeitsgang(iAG);
								}
							}

							// SP5751
							if (!Helper.short2boolean(
									getInternalFrameStueckliste().getStuecklisteDto().getBMitFormeln())) {
								positionDto.setXFormel(null);
							}

							// wir legen eine neue position an
							iId = DelegateFactory.getInstance().getStuecklisteDelegate()
									.createStuecklistearbeitsplan(positionDto);
							iInserted++;
						} catch (Throwable t) {
							// nur loggen!
							myLogger.error(t.getMessage(), t);
						}
					}

					// den Datensatz in der Liste selektieren
					panelQueryArbeitsplan.setSelectedId(iId);

					// die Liste neu aufbauen
					panelQueryArbeitsplan.eventYouAreSelected(false);

					// im Detail den selektierten anzeigen
					panelSplitArbeitsplan.eventYouAreSelected(false);
				}
			}
		}

	}

	/**
	 * save bestellpositionDto von xalOfBelegPosI als textpos.
	 * 
	 * @param pOSDocument2POSDtoI BestellpositionDto
	 * @param xalOfBelegPosI      int
	 * @return Integer
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	public Integer saveBelegPosAsTextpos(POSDocument2POSDto pOSDocument2POSDtoI, int xalOfBelegPosI)
			throws ExceptionLP, Throwable {

		if (true) {
			throw new Exception("saveBSPOSAsTextpos not implemented yet!");
		}

		return null;
	}

	/**
	 * fuelle stuecklistenpositionDtoI von xalOfStuecklistenPOSI mit den
	 * mussfeldern.
	 * 
	 * @param belegposDtoI  StuecklistepositionDto
	 * @param xalOfStklPosI int
	 * @throws Throwable
	 */
	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfStklPosI) throws Throwable {

		StuecklistepositionDto stklPosDtoI = (StuecklistepositionDto) belegposDtoI;

		String sPosArt = stklPosDtoI.getPositionsartCNr();
		if (!LocaleFac.POSITIONSART_IDENT.startsWith(sPosArt)
				&& !LocaleFac.POSITIONSART_HANDEINGABE.startsWith(sPosArt)) {
			String sMsg = "LocaleFac.POSITIONSART_IDENT.startsWith(sPosArt) "
					+ "&& !LocaleFac.POSITIONSART_HANDEINGABE.startsWith(sPosArt)";
			throw new IllegalArgumentException(sMsg);
		}

		stklPosDtoI.setBelegIId(getInternalFrameStueckliste().getStuecklisteDto().getIId());

		stklPosDtoI.setISort(xalOfStklPosI + 1000);

		if (stklPosDtoI.getNMenge() == null) {
			stklPosDtoI.setNMenge(new BigDecimal(0));
		}

		if (stklPosDtoI.getBMitdrucken() == null) {
			stklPosDtoI.setBMitdrucken(Helper.boolean2Short(false));
		}

		if (stklPosDtoI.getMontageartIId() == null) {
			MontageartDto[] stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
					.montageartFindByMandantCNr();
			// es muss mindestens 1 art geben!
			stklPosDtoI.setMontageartIId(stklDto[0].getIId());
		}
	}

	/**
	 * Behandle ChangeEvent; zB Tabwechsel oben.
	 * 
	 * @param e ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {

		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		// PJ18550
		if (bStuecklistenfreigabe == true && getInternalFrameStueckliste().getStuecklisteDto() != null
				&& getInternalFrameStueckliste().getStuecklisteDto().getTFreigabe() != null) {
			getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
		}

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			getInternalFrame().setRechtModulweit(rechtVorher);
			createAuswahl();
			panelQueryStueckliste.eventYouAreSelected(false);
			if (panelQueryStueckliste.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
			}
			panelQueryStueckliste.updateButtons();
		} else if (selectedIndex == IDX_PANEL_DETAIL) {
			Integer key = null;
			if (getInternalFrameStueckliste().getStuecklisteDto() != null) {
				key = getInternalFrameStueckliste().getStuecklisteDto().getIId();
			}
			createDetail(key);
			panelDetailStueckliste.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_POSITIONEN) {
			refreshPositionen(getInternalFrameStueckliste().getStuecklisteDto().getIId());
			panelSplitPositionen.eventYouAreSelected(false);
			panelQueryPositionen.updateButtons();
		} else if (selectedIndex == IDX_PANEL_PRUEFPLAN) {
			refreshPruefplan(getInternalFrameStueckliste().getStuecklisteDto().getIId());
			panelSplitPruefplan.eventYouAreSelected(false);
			panelQueryPruefplan.updateButtons();
		} else if (selectedIndex == IDX_PANEL_POSITIONENERSATZ) {

			refreshPositionen(getInternalFrameStueckliste().getStuecklisteDto().getIId());

			if (getPanelQueryPositionen().getSelectedId() != null) {
				refreshPositionenErsatz((Integer) getPanelQueryPositionen().getSelectedId());
				panelSplitPositionenErsatz.eventYouAreSelected(false);
				panelQueryPositionenErsatz.updateButtons();
			} else {
				DialogFactory.showModalDialog(textFromToken("lp.hinweis"),
						textFromToken("stkl.positionen.ersatz.error"));
				setSelectedComponent(panelSplitPositionen);
			}

		} else if (selectedIndex == IDX_PANEL_ALTERNATIVMASCHINE) {

			refreshArbeitsplan(getInternalFrameStueckliste().getStuecklisteDto().getIId());

			if (panelQueryArbeitsplan.getSelectedId() != null) {
				refreshAlternativmaschine((Integer) panelQueryArbeitsplan.getSelectedId());
				panelSplitAlternativmaschine.eventYouAreSelected(false);
				panelQueryAlternativmaschine.updateButtons();
			} else {
				DialogFactory.showModalDialog(textFromToken("lp.hinweis"),
						textFromToken("stkl.positionen.alternativmaschine.error"));
				setSelectedComponent(panelSplitArbeitsplan);
			}

		} else if (selectedIndex == IDX_PANEL_ARBEITSPLAN) {

			if (bStuecklistenfreigabe == true && getInternalFrameStueckliste().getStuecklisteDto() != null
					&& getInternalFrameStueckliste().getStuecklisteDto().getTFreigabe() != null) {

				bDarfSollzeitenAendern = DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_STK_SOLLZEITEN_FREIGEGEBENE_STK_CUD);

				if (bDarfSollzeitenAendern) {
					getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_UPDATE);
				} else {

					getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
				}
			}

			refreshArbeitsplan(getInternalFrameStueckliste().getStuecklisteDto().getIId());
			panelSplitArbeitsplan.eventYouAreSelected(false);
			panelQueryArbeitsplan.updateButtons();

		} else if (selectedIndex == IDX_LAGERENTNAHME) {
			refreshAbbuchungslager(getInternalFrameStueckliste().getStuecklisteDto().getIId());
			panelSplitAbbuchungslager.eventYouAreSelected(false);
			panelQueryAbbuchungslager.updateButtons();

		} else if (selectedIndex == IDX_PANEL_EIGENSCHAFTEN) {
			createEigenschaft(getInternalFrameStueckliste().getStuecklisteDto().getIId());
			panelSplitEigenschaft.eventYouAreSelected(false);
			panelQueryEigenschaft.updateButtons();

		} else if (selectedIndex == IDX_PANEL_PARAMETER) {
			createParameter(getInternalFrameStueckliste().getStuecklisteDto().getIId());
			panelSplitParameter.eventYouAreSelected(false);
			panelQueryParameter.updateButtons();

		}
		refreshTitle();

	}

}
