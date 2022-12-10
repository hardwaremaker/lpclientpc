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
package com.lp.client.auftrag;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableModel;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.SAXException;

import com.lp.client.angebot.AngebotFilterFactory;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.DialogNeueArtikelnummer;
import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.ICopyPaste;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ArtikelsetViewController;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionenArtikelVerkauf;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.PanelSplit2;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.dynamisch.PanelDynamisch;
import com.lp.client.frame.filechooser.open.CsvFile;
import com.lp.client.frame.filechooser.open.FileOpenerFactory;
import com.lp.client.frame.filechooser.open.XlsxFile;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.stammdatencrud.PanelStammdatenCRUD;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.projekt.InternalFrameProjekt;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.client.rechnung.RechnungFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.HelperTimestamp;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.client.zeiterfassung.PanelAnwesenheitsbestaetigung;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragartDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragteilnehmerDto;
import com.lp.server.auftrag.service.AuftragtextDto;
import com.lp.server.auftrag.service.ImportShopifyCsvDto;
import com.lp.server.auftrag.service.ImportSonCsvDto;
import com.lp.server.auftrag.service.ImportVATXlsxDto;
import com.lp.server.auftrag.service.ImportWooCommerceCsvDto;
import com.lp.server.auftrag.service.LieferstatusDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.kueche.service.SpeiseplanDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/*
 * <p>Tabbed pane fuer Komponente Auftrag.</p> <p>Copyright Logistik Pur
 * Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum 2004-10-28</p> <p> </p>
 *
 * @author Uli Walch
 *
 * @version $Revision: 1.61 $
 */
public class TabbedPaneAuftrag extends TabbedPane implements ICopyPaste {

	private static final long serialVersionUID = 1L;

	private JLabel bestellungenSumme = null;
	private JLabel splittbetragSumme = null;
	private JLabel splittbetragSumme2 = null;
	private PanelQuery auftragAuswahl = null; // FLR Liste
	private PanelAuftragKopfdaten auftragKopfdaten = null;
	private PanelAuftragKonditionen2 auftragKonditionen = null;
	private PanelQuery auftragSichtLieferstatus = null;

	private PanelSplit2 lieferstatus = null;

	private PanelQuery auftragPositionenTop = null;
	private PanelAuftragPositionen2 auftragPositionenBottom = null;
	private PanelSplit auftragPositionen = null; // FLR 1:n Liste

	public PanelSplit getAuftragPositionenSplit() {
		return auftragPositionen;
	}

	private PanelQuery auftragTeilnehmerTop = null;
	private PanelBasis auftragTeilnehmerBottom = null;
	private PanelSplit auftragTeilnehmer = null; // FLR 1:n Liste

	private PanelQuery auftragKostenstelleTop = null;
	private PanelBasis auftragKostenstelleBottom = null;
	private PanelSplit auftragKostenstelle = null; // FLR 1:n Liste

	private PanelQuery auftragZeitplanTop = null;
	private PanelBasis auftragZeitplanBottom = null;
	private PanelSplit auftragZeitplan = null; // FLR 1:n Liste

	private PanelQuery panelQueryAnwesenheitsbestaetigung = null;
	private PanelSplit panelSplitAnwesenheitsbestaetigung = null;
	private PanelBasis panelBottomAnwesenheitsbestaetigung = null;

	private PanelQuery auftragZahlungsplanTop = null;

	private WrapperGotoButton wbuRechnung = new WrapperGotoButton(LPMain.getTextRespectUISPr("rechnung.modulname"),
			com.lp.util.GotoHelper.GOTO_RECHNUNG_AUSWAHL);
	private WrapperGotoButton wbuLieferschein = new WrapperGotoButton(LPMain.getTextRespectUISPr("ls.modulname"),
			com.lp.util.GotoHelper.GOTO_LIEFERSCHEIN_AUSWAHL);

	public PanelQuery getAuftragZahlungsplanTop() {
		return auftragZahlungsplanTop;
	}

	public PanelQuery getAuftragZeitplanTop() {
		return auftragZeitplanTop;
	}

	private PanelBasis auftragZahlungsplanBottom = null;
	private PanelSplit auftragZahlungsplan = null; // FLR 1:n Liste

	private JLabel auftragsSumme = new JLabel();

	private PanelQuery auftragZahlungsplanmeilensteinTop = null;

	public PanelQuery getAuftragZahlungsplanmeilensteinTop() {
		return auftragZahlungsplanmeilensteinTop;
	}

	private PanelBasis auftragZahlungsplanmeilensteinBottom = null;
	private PanelSplit auftragZahlungsplanmeilenstein = null; // FLR 1:n Liste

	private PanelQuery sichtAuftragAufAndereBelegartenTop = null;

	private PanelQuery auftragSichtRahmenTop = null;
	private PanelBasis auftragSichtRahmenBottom = null;
	private PanelSplit auftragSichtRahmen = null; // FLR 1:n Liste

	private PanelQuery panelAuftragsnrnrnTopQP;
	private PanelSplit panelAuftragsnrnrnSP;
	private PanelStammdatenCRUD panelAuftragsnrnrnBottomD;

	private PanelDialogKriterienAuftragUebersicht pdKriterienAuftragUebersicht = null;
	private boolean bKriterienAuftragUebersichtUeberMenueAufgerufen = false;
	private PanelTabelleAuftragUebersicht ptAuftragUebersicht = null;

	private PanelDialogKriterienAuftragzeiten pdKriterienAuftragzeiten = null;
	private boolean pdKriterienAuftragzeitenUeberMenueAufgerufen = false;
	private PanelTabelleAuftragzeiten ptAuftragzeiten = null;

	private PanelBasis panelDetailAuftragseigenschaft = null;

	private PanelDialogAuftragArtikelSchnellanlage panelDialogAuftragArtikelSchnellanlageArtikelAendern = null;

	private PanelTabelleSichtLSRE auftragLSRE = null;

	private PanelQuery auftragLose = null;
	private PanelQuery auftragBestellungen = null;
	private PanelQuery auftragEingangsrechnung = null;
	private PanelQueryFLR panelQueryFLRBegruendung = null;

	// PJ20821
	static final public String GOTO_BESTELLUNG = PanelBasis.ACTION_MY_OWN_NEW + "GOTO_BESTELLUNG";
	private WrapperGotoButton wbuGotoBestellung = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_BESTELLUNG_AUSWAHL);

	static final public String GOTO_EINGANGSRECHNUNG = PanelBasis.ACTION_MY_OWN_NEW + "GOTO_EINGANGSRECHNUNG";
	private WrapperGotoButton wbuGotoEingangsrechnung = new WrapperGotoButton(
			com.lp.util.GotoHelper.GOTO_EINGANGSRECHNUNG_AUSWAHL);

	// PJ19121
	static final public String GOTO_LOS = PanelBasis.ACTION_MY_OWN_NEW + "GOTO_LOS";
	WrapperGotoButton wbuGotoLos = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_FERTIGUNG_AUSWAHL);

	private Integer iIdAuftrag;

	public boolean bAuftragsfreigabe = false;

	public static int IDX_PANEL_AUFTRAGAUSWAHL = -1;
	public static int IDX_PANEL_AUFTRAGKOPFDATEN = -1;
	private int IDX_PANEL_AUFTRAGPOSITIONEN = -1;
	private int IDX_PANEL_AUFTRAGKONDITIONEN = -1;
	private int IDX_PANEL_AUFTRAGTEILNEHMER = -1;
	private int IDX_PANEL_SICHTLIEFERSTATUS = -1;
	private int IDX_PANEL_SICHTRAHMEN = -1;
	private int IDX_PANEL_AUFTRAGUEBERSICHT = -1;
	private int IDX_PANEL_AUFTRAGZEITEN = -1;
	private int IDX_PANEL_LSRE = -1;
	private int IDX_PANEL_LOSE = -1;
	private int IDX_PANEL_BESTELLUNGEN = -1;
	private int IDX_PANEL_EINGANGSRECHNUNGEN = -1;
	private int IDX_PANEL_AUFTRAGKOSTENSTELLEN = -1;
	private int IDX_PANEL_AUFTRAGEIGENSCHAFTEN = -1;
	private int IDX_PANEL_ZEITPLAN = -1;
	private int IDX_PANEL_ZAHLUNGSPLAN = -1;
	private int IDX_PANEL_ZAHLUNGSPLANMEILENSTEIN = -1;
	private int IDX_PANEL_ZEITBESTAETIGUNG = -1;

	// dtos, die in mehr als einem panel benoetigt werden
	private AuftragDto auftragDto = null;
	private KundeDto kundeAuftragDto = null;
	private AuftragtextDto kopftextDto = null; // belegartkonditionen: 1
	// Kopftext unabhaengig vom
	// PanelKontionen hinterlegen
	private AuftragtextDto fusstextDto = null; // belegartkonditionen: 2
	// Fusstext unabhaengig vom
	// PanelKontionen hinterlegen

	private final String MENU_ACTION_DATEI_NEU_AUS_AUFTRAG = "MENU_ACTION_DATEI_NEU_AUS_AUFTRAG";
	private final String MENU_ACTION_DATEI_AUFTRAGBESTAETIGUNG = "MENU_ACTION_DATEI_AUFTRAGBESTAETIGUNG";
	private final String MENU_ACTION_DATEI_VERSANDWEGBESTAETIGUNG = "MENU_ACTION_DATEI_VERSANDWEGBESTAETIGUNG";
	private final String MENU_ACTION_DATEI_PACKLISTE = "MENU_ACTION_DATEI_PACKLISTE";
	private final String MENU_ACTION_DATEI_KOMMISSIONIERUNG = "MENU_ACTION_DATEI_KOMMISSIONIERUNG";
	private final String MENU_ACTION_DATEI_ETIKETT = "MENU_ACTION_DATEI_ETIKETT";

	private final String MENU_ACTION_JOURNAL_OFFEN = "MENU_ACTION_JOURNAL_OFFEN";
	private final String MENU_ACTION_JOURNAL_OFFEN_DETAILS = "MENU_ACTION_JOURNAL_OFFEN_DETAILS";
	private final String MENU_ACTION_JOURNAL_OFFEN_POSITIONEN = "MENU_ACTION_JOURNAL_OFFEN_POSITIONEN";
	private final String MENU_ACTION_JOURNAL_UEBERSICHT = "MENU_ACTION_JOURNAL_UEBERSICHT";
	private final String MENU_ACTION_JOURNAL_UMSATZSTATISTIK = "MENU_ACTION_JOURNAL_UMSATZSTATISTIK";
	private final String MENU_ACTION_JOURNAL_STATISTIK = "MENU_ACTION_JOURNAL_STATISTIK";
	private final String MENU_ACTION_JOURNAL_ERFUELLUNGSGRAD = "MENU_ACTION_JOURNAL_ERFUELLUNGSGRAD";
	private final String MENU_ACTION_JOURNAL_WARTUNGSAUSWERTUNG = "MENU_ACTION_JOURNAL_WARTUNGSAUSWERTUNG";
	private final String MENU_ACTION_JOURNAL_ALLE_AUFTRAEGE = "MENU_ACTION_JOURNAL_ALLE_AUFTRAEGE";
	private final String MENU_ACTION_JOURNAL_ERLEDIGTE_AUFTRAEGE = "MENU_ACTION_JOURNAL_ERLEDIGTE_AUFTRAEGE";
	private final String MENU_ACTION_JOURNAL_TAETIGKEITSSTATISTIK = "MENU_ACTION_JOURNAL_TAETIGKEITSSTATISTIK";
	private final String MENU_ACTION_JOURNAL_LIEFERPLAN = "MENU_ACTION_JOURNAL_LIEFERPLAN";
	private final String MENU_ACTION_JOURNAL_AUSZULIEFERNDE_POSITIONEN = "MENU_ACTION_JOURNAL_AUSZULIEFERNDE_POSITIONEN";
	private final String MENU_ACTION_JOURNAL_TEILNEHMER = "MENU_ACTION_JOURNAL_TEILNEHMER";

	private final String MENU_BEARBEITEN_MANUELL_ERLEDIGEN = "MENU_BEARBEITEN_MANUELL_ERLEDIGEN";
	private final String MENU_BEARBEITEN_MANUELL_ERLEDIGEN_AUFHEBEN = "MENU_BEARBEITEN_MANUELL_ERLEDIGEN_AUFHEBEN";
	private final String MENU_BEARBEITEN_INTERNER_KOMMENTAR = "MENU_BEARBEITEN_INTERNER_KOMMENTAR";
	private final String MENU_BEARBEITEN_EXTERNER_KOMMENTAR = "MENU_BEARBEITEN_EXTERNER_KOMMENTAR";
	private final String MENU_BEARBEITEN_ANGEBOT_ZEITDATEN_UEBERNEHMEN = "MENU_BEARBEITEN_ANGEBOT_ZEITDATEN_UEBERNEHMEN";
	private final String MENU_BEARBEITEN_ERFUELLUNGSGRAD_PRAEMIE = "MENU_BEARBEITEN_ERFUELLUNGSGRAD_PRAEMIE";
	private final String MENUE_ACTION_BEARBEITEN_TERMINVERSCHIEBEN = "MENUE_ACTION_BEARBEITEN_TERMINVERSCHIEBEN";
	private final String MENU_BEARBEITEN_RECHNUNGSADRESSE_BESTELLNUMMER_PROJEKT = "MENU_BEARBEITEN_RECHNUNGSADRESSE_BESTELLNUMMER_PROJEKT";

	private String MY_OWN_NEW_TOGGLE_GESEHEN = PanelBasis.ACTION_MY_OWN_NEW + "TOGGLE_GESEHEN";
	private String MY_OWN_NEW_TOGGLE_HVMAUEBERTRAGEN = PanelBasis.ACTION_MY_OWN_NEW + "TOGGLE_HVMAUEBERTRAGEN";

	private final String MENU_ACTION_INFO_AUFTRAGZEITEN = "MENU_ACTION_INFO_AUFTRAGZEITEN";
	private final String MENU_ACTION_INFO_ZEITBESTAETIGUNG = "MENU_ACTION_INFO_ZEITBESTAETIGUNG";
	private final String MENU_ACTION_INFO_AUFTRAGSETIKETT = "MENU_ACTION_INFO_AUFTRAGSETIKETT";
	private final String MENU_ACTION_INFO_NACHKALKULATION = "MENU_ACTION_INFO_NACHKALKULATION";
	private final String MENUE_ACTION_INFO_WIEDERBESCHAFFUNG = "MENU_ACTION_INFO_WIEDERBESCHAFFUNG";
	private final String MENUE_ACTION_INFO_VERFUEGBARKEITSPRUEFUNG = "MENUE_ACTION_INFO_VERFUEGBARKEITSPRUEFUNG";
	private final String MENUE_ACTION_INFO_ROLLIERENDEPLANUNG = "MENUE_ACTION_INFO_ROLLIERENDEPLANUNG";
	private final String MENUE_ACTION_INFO_RAHMENERFUELLUNG = "MENUE_ACTION_INFO_RAHMENERFUELLUNG";
	private final String MENUE_ACTION_INFO_RAHMENUEBERSICHT = "MENUE_ACTION_INFO_RAHMENUEBERSICHT";
	private final String MENUE_ACTION_INFO_AUFTRAGSUEBERSICHT = "MENU_ACTION_INFO_AUFTRAGSUEBERSICHT";
	private final String MENUE_ACTION_INFO_PROJEKTBLATT = "MENU_ACTION_INFO_PROJEKTBLATT";
	private final String MENU_ACTION_JOURNAL_MEILENSTEINE = "MENU_ACTION_JOURNAL_MEILENSTEINE";
	private final String MENU_ACTION_JOURNAL_ERFOLGSSTATUS = "MENU_ACTION_JOURNAL_ERFOLGSSTATUS";
	private final String MENU_ACTION_JOURNAL_PLANSTUNDEN = "MENU_ACTION_JOURNAL_PLANSTUNDEN";
	private final String MENU_ACTION_JOURNAL_MATERIALBEDARFE = "MENU_ACTION_JOURNAL_MATERIALBEDARFE";

	private final String MENUE_ACTION_PFLEGE_INDEXANPASSUNG = "MENUE_ACTION_PFLEGE_INDEXANPASSUNG";
	private final String MENUE_ACTION_PFLEGE_WIEDERHOLENDE_PREISE_ZUR_PREISGUELTIGKEIT_ANPASSEN = "MENUE_ACTION_PFLEGE_WIEDERHOLENDE_PREISE_ZUR_PREISGUELTIGKEIT_ANPASSEN";

	private static final String ACTION_SPECIAL_CSVIMPORT_POSITIONEN = "ACTION_SPECIAL_CSVIMPORT_POSITIONEN";
	private static final String ACTION_SPECIAL_CSVIMPORT_SON = "ACTION_SPECIAL_CSVIMPORT_SON";
	private static final String ACTION_SPECIAL_CSVIMPORT_WOOCOMMERCE = "ACTION_SPECIAL_CSVIMPORT_WOOCOMMERCE";
	private static final String ACTION_SPECIAL_XSLXIMPORT_VAT = "ACTION_SPECIAL_XSLXIMPORT_VAT";
	private static final String ACTION_SPECIAL_SCHNELLERFASSUNG_POSITIONEN = "ACTION_SPECIAL_SCHNELLERFASSUNG_POSITIONEN";

	private final String BUTTON_SORTIERE_NACH_ARTIKELNUMMER = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_SORTIERE_NACH_ARTIKELNUMMER";

	private final String MENU_BEARBEITEN_HANDARTIKEL_UMANDELN = "MENU_BEARBEITEN_HANDARTIKEL_UMANDELN";

	private final String MENU_BEARBEITEN_HANDARTIKEL_IN_BESTEHENDEN_ARTIKEL_UMANDELN = "MENU_BEARBEITEN_HANDARTIKEL_IN_BESTEHENDEN_ARTIKEL_UMANDELN";

	private static final String ACTION_SPECIAL_NEU_AUS_PROJEKT = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_NEU_AUS_PROJEKT";

	private static final String ACTION_SPECIAL_PREISE_NEU_KALKULIEREN = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_PREISE_NEU_KALKULIEREN";

	private final String MENU_BEARBEITEN_PROJEKT_ANLEGEN = "MENU_BEARBEITEN_PROJEKT_ANLEGEN";

	public final static String EXTRA_NEU_EXTERNER_TEILNEHMER = "externer_teilnehmer";
	public final static String EXTRA_NEU_AUS_ANGEBOT = "aus_angebot";
	public final static String EXTRA_RAHMENAUFTRAG_NEU_AUS_ANGEBOT = "rahmenauftrag_aus_angebot";
	public final static String EXTRA_NEU_AUS_AUFTRAG = "aus_auftrag";
	public final static String EXTRA_ABRUF_ZU_RAHMEN = "extra_abruf_zu_rahmen";
	public final static String EXTRA_NEU_AUS_SCHNELLANLAGE = "aus_schnellanlage";
	public final static String EXTRA_ARTIKEL_AUS_SCHNELLANLAGE_BEARBEITEN = "aus_schnellanlage_bearbeiten";

	public final static String EXTRA_ZEITPLAN_IMPORTIEREN = "zeitplan_importieren";

	private final String MENU_BEARBEITEN_BEGRUENDUNG = "MENU_BEARBEITEN_BEGRUENDUNG";

	private String MY_OWN_NEW_NEU_EXTERNER_TEILNEHMER = PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_EXTERNER_TEILNEHMER;
	private String MY_OWN_NEW_RAHMENAUFTRAG_NEU_AUS_ANGEBOT = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_RAHMENAUFTRAG_NEU_AUS_ANGEBOT;

	private String MY_OWN_NEW_NEU_AUS_ANGEBOT = PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_ANGEBOT;

	private String MY_OWN_NEW_NEU_AUS_AUFTRAG = PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_AUFTRAG;

	private String MY_OWN_NEW_NEU_AUS_AUFTRAG_LIEFERTERMIN = PanelBasis.ACTION_MY_OWN_NEW + "aus_auftrag_liefertermin";

	private String MY_OWN_NEW_NEU_AUS_SCHNELLANLAGE = PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_SCHNELLANLAGE;

	private String MY_OWN_NEW_NEU_ARTIKEL_AUS_SCHNELLANLAGE_BEARBEITEN = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_ARTIKEL_AUS_SCHNELLANLAGE_BEARBEITEN;

	private String MY_OWN_NEW_EXTRA_ZEITPLAN_IMPORTIEREN = PanelBasis.ACTION_MY_OWN_NEW + EXTRA_ZEITPLAN_IMPORTIEREN;

	private String MY_OWN_NEW_TOGGLE_VERRECHENBAR = PanelBasis.ACTION_MY_OWN_NEW + "TOGGLE_VERRECHENBAR";

	private String MY_OWN_NEW_ABRUF_ZU_RAHMEN = PanelBasis.ACTION_MY_OWN_NEW + EXTRA_ABRUF_ZU_RAHMEN;
	private String MENUE_ACTION_ETIKETT_DRUCKEN = PanelBasis.ACTION_MY_OWN_NEW + "MENUE_ACTION_ETIKETT_DRUCKEN";
	private final String BUTTON_IMPORTCSV_AUFTRAGPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_CSVIMPORT_POSITIONEN;
	private final String BUTTON_SCHNELLERFASSUNG_AUFTRAGPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_SCHNELLERFASSUNG_POSITIONEN;

	public final static String LIEFERSCHEIN_PREISE_UPDATEN = "lp_preise_updaten";

	private String MY_OWN_NEW_LIEFERSCHEIN_PREISE_UPDATEN = PanelBasis.ACTION_MY_OWN_NEW + LIEFERSCHEIN_PREISE_UPDATEN;

	private PanelQueryFLR panelQueryFLRZeitplantyp = null;

	private PanelQueryFLR panelQueryFLRAuftragauswahl = null;
	private PanelQueryFLR panelQueryFLRAuftragauswahlLiefertermin = null;
	private PanelQueryFLR panelQueryFLRAngebotauswahl = null;
	private PanelQueryFLR panelQueryFLRAngebotauswahlFuerRahmenauftrag = null;

	private PanelQueryFLR panelQueryFLRProjekt = null;

	private PanelDialogAuftragKommentar pdAuftragInternerKommentar = null;
	private PanelDialogAuftragKommentar pdAuftragExternerKommentar = null;

	private PanelQueryFLR panelQueryFLRArtikel_handartikelumwandeln = null;

	private PanelQueryFLR panelQueryFLRKundeFuerVATImport = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartnerFuerVATImport = null;

	private boolean bAuftragTerminStundenMinuten = false;

	boolean bEsGibtRahmenauftraege = false;
	private WrapperMenuBar menuBar = null;

	public TabbedPaneAuftrag(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("auft.auftrag"));

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_AUFTRAGSFREIGABE, ParameterFac.KATEGORIE_AUFTRAG,
						LPMain.getTheClient().getMandant());

		bAuftragsfreigabe = (Boolean) parameter.getCWertAsObject();

		jbInit();
		initComponents();
	}

	public InternalFrameAuftrag getInternalFrameAuftrag() throws Throwable {
		return (InternalFrameAuftrag) getInternalFrame();
	}

	public PanelQuery getAuftragAuswahl() {
		return auftragAuswahl;
	}

	public PanelBasis getAuftragKopfdaten() {
		return auftragKopfdaten;
	}

	public PanelQuery getAuftragPositionenTop() { // isort: 0
		return auftragPositionenTop;
	}

	public PanelAuftragPositionen2 getAuftragPositionenBottom() {
		return auftragPositionenBottom;
	}

	public PanelQuery getAuftragTeilnehmerTop() {
		return auftragTeilnehmerTop;
	}

	public PanelQuery getAuftragKostenstelleTop() {
		return auftragKostenstelleTop;
	}

	public PanelBasis getAuftragTeilnehmerBottom() {
		return auftragTeilnehmerBottom;
	}

	public PanelBasis getAuftragKostenstelleBottom() {
		return auftragKostenstelleBottom;
	}

	public PanelQuery getSichtAuftragAufAndereBelegartenTop() {
		return sichtAuftragAufAndereBelegartenTop;
	}

	public PanelQuery getAuftragSichtRahmenTop() {
		return auftragSichtRahmenTop;
	}

	public PanelQuery getAuftragSichtLieferstatus() {
		return auftragSichtLieferstatus;
	}

	public AuftragDto getAuftragDto() {
		return auftragDto;
	}

	public void setAuftragDto(AuftragDto dto) {
		auftragDto = dto;
	}

	public KundeDto getKundeAuftragDto() {
		return kundeAuftragDto;
	}

	public void setKundeAuftragDto(KundeDto dto) {
		kundeAuftragDto = dto;
	}

	public AuftragtextDto getKopftextDto() {
		return kopftextDto;
	}

	public void setKopftextDto(AuftragtextDto kopftextDtoI) {
		kopftextDto = kopftextDtoI;
	}

	public AuftragtextDto getFusstextDto() {
		return fusstextDto;
	}

	public void setFusstextDto(AuftragtextDto fusstextDtoI) {
		fusstextDto = fusstextDtoI;
	}

	public boolean getBAuftragterminstudenminuten() {
		return bAuftragTerminStundenMinuten;
	}

	protected Integer getSelectedIdPositionen() throws Throwable {
		return (Integer) getAuftragPositionenTop().getSelectedId();
	}

	private void jbInit() throws Throwable {
		// dafuer sorgen, dass die Dtos != null sind
		resetDtos();
		// SK: Wenn es die Auftragart Rahmenauftrag nicht gibt soll der Button
		// nicht angezeigt werden
		AuftragartDto[] auftragArt = DelegateFactory.getInstance().getAuftragServiceDelegate().auftragartFindAll();

		for (int i = 0; i < auftragArt.length; i++) {
			if (AuftragServiceFac.AUFTRAGART_RAHMEN.equals(auftragArt[i].getCNr())) {
				bEsGibtRahmenauftraege = true;
			}
		}

		// die Liste der Auftraege aufbauen
		refreshAuftragAuswahl();

		// die Liste der Auftraege anzeigen
		// auftragAuswahl.eventYouAreSelected(false);

		// den aktuell gewaehlten Auftrag hinterlegen und den Lock setzen
		setKeyWasForLockMe();

		IDX_PANEL_AUFTRAGAUSWAHL = reiterHinzufuegen(LPMain.getTextRespectUISPr("auft.title.panel.auswahl"), null,
				auftragAuswahl, LPMain.getTextRespectUISPr("auft.title.tooltip.auswahl"));

		// die restlichen Panels erst bei Bedarf laden
		IDX_PANEL_AUFTRAGKOPFDATEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("auft.title.panel.kopfdaten"), null,
				null, LPMain.getTextRespectUISPr("auft.title.tooltip.kopfdaten"));

		IDX_PANEL_AUFTRAGPOSITIONEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("auft.title.panel.positionen"), null,
				null, LPMain.getTextRespectUISPr("auft.title.tooltip.positionen"));

		IDX_PANEL_AUFTRAGKONDITIONEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("auft.title.panel.konditionen"),
				null, null, LPMain.getTextRespectUISPr("auft.title.tooltip.konditionen"));

		IDX_PANEL_AUFTRAGTEILNEHMER = reiterHinzufuegen(LPMain.getTextRespectUISPr("auft.title.panel.teilnehmer"), null,
				null, LPMain.getTextRespectUISPr("auft.title.tooltip.teilnehmer"));

		IDX_PANEL_SICHTLIEFERSTATUS = reiterHinzufuegen(
				LPMain.getTextRespectUISPr("auft.title.panel.sichtlieferstatus"), null, null,
				LPMain.getTextRespectUISPr("auft.title.tooltip.sichtlieferstatus"));

		// if(bEsGibtRahmenauftraege){
		IDX_PANEL_SICHTRAHMEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.sichtrahmen"), null, null,
				LPMain.getTextRespectUISPr("lp.sichtrahmen"));
		// }
		IDX_PANEL_AUFTRAGUEBERSICHT = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.umsatzuebersicht"), null, null,
				LPMain.getTextRespectUISPr("lp.umsatzuebersicht"));

		IDX_PANEL_AUFTRAGZEITEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("auft.title.panel.auftragzeiten"), null,
				null, LPMain.getTextRespectUISPr("auft.title.tooltip.auftragzeiten"));

		IDX_PANEL_LSRE = reiterHinzufuegen(LPMain.getTextRespectUISPr("auft.title.panel.lsre"), null, null, "LSRE");

		IDX_PANEL_LOSE = reiterHinzufuegen(LPMain.getTextRespectUISPr("auft.title.panel.lose"), null, null,
				LPMain.getTextRespectUISPr("auft.title.tooltip.lose"));
		IDX_PANEL_BESTELLUNGEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("bes.bestellungen"), null, null,
				LPMain.getTextRespectUISPr("bes.bestellungen"));

		IDX_PANEL_EINGANGSRECHNUNGEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("er.modulname.tooltip"), null, null,
				LPMain.getTextRespectUISPr("er.modulname.tooltip"));

		// Wenn keine Panelbeschriebung vorhanden, dann ausblenden
		PanelbeschreibungDto[] dtos = DelegateFactory.getInstance().getPanelDelegate()
				.panelbeschreibungFindByPanelCNrMandantCNr(PanelFac.PANEL_AUFTRAGSEIGENSCHAFTEN, null);
		if (dtos != null && dtos.length > 0) {
			IDX_PANEL_AUFTRAGEIGENSCHAFTEN =

					reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.eigenschaften"), null, null,
							LPMain.getTextRespectUISPr("lp.eigenschaften"));
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_AUFTRAGUNTERKOSTENSTELLEN)) {

			IDX_PANEL_AUFTRAGKOSTENSTELLEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("auft.unterkostenstellen"),
					null, null, LPMain.getTextRespectUISPr("auft.unterkostenstellen"));

		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ERWEITERTE_PROJEKTSTEUERUNG)
				|| LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_KLEINE_PROJEKTSTEUERUNG)) {
			IDX_PANEL_ZEITPLAN = reiterHinzufuegen(LPMain.getTextRespectUISPr("auft.zeitplan"), null, null,
					LPMain.getTextRespectUISPr("auft.zeitplan"));

		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ERWEITERTE_PROJEKTSTEUERUNG)) {
			IDX_PANEL_ZAHLUNGSPLAN = reiterHinzufuegen(LPMain.getTextRespectUISPr("auft.zahlungsplan"), null, null,
					LPMain.getTextRespectUISPr("auft.zahlungsplan"));
			IDX_PANEL_ZAHLUNGSPLANMEILENSTEIN = reiterHinzufuegen(
					LPMain.getTextRespectUISPr("auft.zahlungsplanmeilenstein"), null, null,
					LPMain.getTextRespectUISPr("auft.zahlungsplanmeilenstein"));
		}

		if (LPMain.getInstance().getDesktop().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_HVMA2)) {

			IDX_PANEL_ZEITBESTAETIGUNG = reiterHinzufuegen(
					LPMain.getTextRespectUISPr("pers.title.tab.anwesenheitsbestaetigung"), null, null,
					LPMain.getTextRespectUISPr("pers.title.tab.anwesenheitsbestaetigung"));
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_AUFTRAGTERMIN_STUNDEN_MINUTEN)) {
			bAuftragTerminStundenMinuten = true;
		}

		addChangeListener(this);

		getInternalFrame().addItemChangedListener(this);
	}

	private void refreshAuftragAuswahl() throws Throwable {
		if (auftragAuswahl == null) {
			QueryType[] qtAuswahl = AuftragFilterFactory.getInstance().createQTPanelAuftragAuswahl();
			FilterKriterium[] filterAuswahl = SystemFilterFactory.getInstance().createFKMandantCNr(); // die
																										// Filterkriterien
																										// aendern sich
			// nicht

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER };

			auftragAuswahl = new PanelQuery(qtAuswahl, filterAuswahl, QueryParameters.UC_ID_AUFTRAG, aWhichButtonIUse,
					getInternalFrame(), LPMain.getTextRespectUISPr("auft.title.panel.auswahl"), true,
					AuftragFilterFactory.getInstance().createFKVAuftrag(), null); // liste
																					// refresh
																					// wenn
																					// lasche
																					// geklickt
																					// wurde

			// fkdirekt: 0 dem PanelQuery die direkten FilterKriterien setzen
			FilterKriteriumDirekt fkDirekt1 = AuftragFilterFactory.getInstance().createFKDAuftragnummer();

			FilterKriteriumDirekt fkDirekt2 = AuftragFilterFactory.getInstance().createFKDKundenname();

			auftragAuswahl.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);
			auftragAuswahl.addDirektFilter(AuftragFilterFactory.getInstance().createFKDProjekt());
			auftragAuswahl.addDirektFilter(AuftragFilterFactory.getInstance().createFKDTextSuchen());

			auftragAuswahl
					.befuelleFilterkriteriumSchnellansicht(AuftragFilterFactory.getInstance().createFKSchnellansicht());

			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ANGEBOT)
					&& (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_ANGB_ANGEBOT_R)
							|| DelegateFactory.getInstance().getTheJudgeDelegate()
									.hatRecht(RechteFac.RECHT_ANGB_ANGEBOT_CUD))) {

				auftragAuswahl.createAndSaveAndShowButton("/com/lp/client/res/presentation_chart16x16.png",
						LPMain.getTextRespectUISPr("lp.tooltip.datenausbestehendemangebot"), MY_OWN_NEW_NEU_AUS_ANGEBOT,
						RechteFac.RECHT_AUFT_AUFTRAG_CUD);
			}

			if (bEsGibtRahmenauftraege) {

				auftragAuswahl.createAndSaveAndShowButton("/com/lp/client/res/presentation.png",
						LPMain.getTextRespectUISPr("lp.tooltip.rahmenauftragausbestehendemangebot"),
						MY_OWN_NEW_RAHMENAUFTRAG_NEU_AUS_ANGEBOT, RechteFac.RECHT_AUFT_AUFTRAG_CUD);
			}

			auftragAuswahl.createAndSaveAndShowButton("/com/lp/client/res/auftrag16x16.png",
					LPMain.getTextRespectUISPr("lp.tooltip.datenausbestehendemauftrag"), MY_OWN_NEW_NEU_AUS_AUFTRAG,
					RechteFac.RECHT_AUFT_DARF_AUFTRAEGE_KOPIEREN);

			auftragAuswahl.createAndSaveAndShowButton("/com/lp/client/res/book_blue_add.png",
					LPMain.getTextRespectUISPr("lp.tooltip.datenausbestehendemauftrag.liefertermine"),
					MY_OWN_NEW_NEU_AUS_AUFTRAG_LIEFERTERMIN, RechteFac.RECHT_AUFT_DARF_AUFTRAEGE_KOPIEREN);

			ParametermandantDto parameterMandant = DelegateFactory.getInstance().getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
							ParameterFac.PARAMETER_ZUSATZSTATUS_VERRECHENBAR);

			boolean b = (Boolean) parameterMandant.getCWertAsObject();
			if (b == true) {
				auftragAuswahl.createAndSaveAndShowButton("/com/lp/client/res/calculator16x16.png",
						LPMain.getTextRespectUISPr("auft.kannabgerechnetwerden"), MY_OWN_NEW_TOGGLE_VERRECHENBAR,
						RechteFac.RECHT_AUFT_AUFTRAG_CUD);
			}

			if (bEsGibtRahmenauftraege) {
				auftragAuswahl.createAndSaveAndShowButton("/com/lp/client/res/abruf.png",
						LPMain.getTextRespectUISPr("auft.abrufzurahmen"), MY_OWN_NEW_ABRUF_ZU_RAHMEN,
						RechteFac.RECHT_AUFT_AUFTRAG_CUD);
			}

			ArbeitsplatzparameterDto parameter = DelegateFactory.getInstance().getParameterDelegate()
					.holeArbeitsplatzparameter(ParameterFac.ARBEITSPLATZPARAMETER_AUFT_SERIENNUMMERNETIKETTENDRUCK);
			if (parameter != null && parameter.getCWert() != null && parameter.getCWert().equals("1")) {
				auftragAuswahl.createAndSaveAndShowButton("/com/lp/client/res/printer216x16.png",
						LPMain.getTextRespectUISPr("auft.seriennummerdrucken") + " F11", MENUE_ACTION_ETIKETT_DRUCKEN,
						KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0), null);
			}

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {
				auftragAuswahl.createAndSaveAndShowButton("/com/lp/client/res/briefcase2_document16x16.png",
						LPMain.getTextRespectUISPr("angb.neuausprojekt"), ACTION_SPECIAL_NEU_AUS_PROJEKT,
						RechteFac.RECHT_AUFT_AUFTRAG_CUD);
			}

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_AUFTRAG_SCHNELLANLAGE)) {
				auftragAuswahl.createAndSaveAndShowButton("/com/lp/client/res/flash.png",
						LPMain.getTextRespectUISPr("auft.schnellanlage"), MY_OWN_NEW_NEU_AUS_SCHNELLANLAGE,
						RechteFac.RECHT_AUFT_AUFTRAG_CUD);
				auftragAuswahl.createAndSaveAndShowButton("/com/lp/client/res/nut_and_bolt24x24.png",
						LPMain.getTextRespectUISPr("auftrag.schnellanlage.artikel.bearbeiten"),
						MY_OWN_NEW_NEU_ARTIKEL_AUS_SCHNELLANLAGE_BEARBEITEN, RechteFac.RECHT_AUFT_AUFTRAG_CUD);
			}

		}
	}

	private PanelTabelleSichtLSRE getPanelTabelleLoszeiten() throws Throwable {

		auftragLSRE = new PanelTabelleSichtLSRE(QueryParameters.UC_ID_AUFTRAGSICHTLSRE,
				LPMain.getTextRespectUISPr("auft.title.panel.lsre"), getInternalFrame());

		// default Kriterium vorbelegen
		FilterKriterium[] aFilterKrit = new FilterKriterium[1];

		FilterKriterium krit1 = new FilterKriterium("auftrag_i_id", true, auftragDto.getIId().toString(),
				FilterKriterium.OPERATOR_EQUAL, false);

		aFilterKrit[0] = krit1;

		auftragLSRE.setDefaultFilter(aFilterKrit);

		setComponentAt(IDX_PANEL_LSRE, auftragLSRE);

		return auftragLSRE;
	}

	private void refreshLose() throws Throwable {
		FilterKriterium[] filtersRechnungen = FertigungFilterFactory.getInstance().createFKAuftrag(auftragDto.getIId());
		if (auftragLose == null) {
			FilterKriterium[] filterAuswahl = new FilterKriterium[1];
			filterAuswahl[0] = new FilterKriterium("flrlos.mandant_c_nr", true,
					"'" + LPMain.getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL, false);

			// nicht

			String[] aWhichButtonIUse = {};

			auftragLose = new PanelQuery(null, filterAuswahl, QueryParameters.UC_ID_LOS, aWhichButtonIUse,
					getInternalFrame(), LPMain.getTextRespectUISPr("auft.title.panel.lose"), true); // liste
			// refresh
			// wenn
			// lasche
			// geklickt
			// wurde
			FilterKriteriumDirekt fkDirekt1 = FertigungFilterFactory.getInstance().createFKDLosnummer();
			FilterKriteriumDirekt fkDirekt2 = FertigungFilterFactory.getInstance().createFKDArtikelnummer();
			auftragLose.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);
			FilterKriteriumDirekt fkDirektStatistikadresse = RechnungFilterFactory.getInstance()
					.createFKDKundestatistikadresse();
			auftragLose.addDirektFilter(fkDirektStatistikadresse);

			auftragLose.getToolBar().addButtonLeft("/com/lp/client/res/data_into.png",
					LPMain.getTextRespectUISPr("proj.projektverlauf.gehezubeleg"), GOTO_LOS,
					KeyStroke.getKeyStroke('G', java.awt.event.InputEvent.CTRL_MASK), null);
			auftragLose.enableToolsPanelButtons(true, GOTO_LOS);
			auftragLose.enableToolsPanelButtons(true, PanelBasis.ACTION_REFRESH);

			this.setComponentAt(IDX_PANEL_LOSE, auftragLose);
		}
		auftragLose.setDefaultFilter(filtersRechnungen);
	}

	private void refreshBestellungen() throws Throwable {
		FilterKriterium[] filtersBestellungen = BestellungFilterFactory.getInstance()
				.createFKAuftrag(auftragDto.getIId());
		if (auftragBestellungen == null) {
			String[] aWhichButtonIUse = {};

			auftragBestellungen = new PanelQuery(null, filtersBestellungen, QueryParameters.UC_ID_BESTELLUNG,
					aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("bes.bestellungen"), true); // liste
			bestellungenSumme = new JLabel();
			auftragBestellungen.getToolBar().getToolsPanelCenter().add(bestellungenSumme);
			// refresh
			// wenn
			// lasche
			// geklickt
			// wurde
			FilterKriteriumDirekt fkDirekt1 = BestellungFilterFactory.getInstance().getFilterkriteriumDirekt1();
			FilterKriteriumDirekt fkDirekt2 = BestellungFilterFactory.getInstance().createFKDProjekt();
			auftragBestellungen.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);

			auftragBestellungen.getToolBar().addButtonLeft("/com/lp/client/res/data_into.png",
					LPMain.getTextRespectUISPr("proj.projektverlauf.gehezubeleg"), GOTO_BESTELLUNG,
					KeyStroke.getKeyStroke('G', java.awt.event.InputEvent.CTRL_MASK), null);
			auftragBestellungen.enableToolsPanelButtons(true, GOTO_BESTELLUNG);
			auftragBestellungen.enableToolsPanelButtons(true, PanelBasis.ACTION_REFRESH);

			this.setComponentAt(IDX_PANEL_BESTELLUNGEN, auftragBestellungen);
		}

		auftragBestellungen.setDefaultFilter(filtersBestellungen);
//		refreshBestellungWert();
	}

	private void refreshBestellungWert() throws Throwable {
		BigDecimal bestellwert = DelegateFactory.getInstance().getAuftragDelegate()
				.berechneBestellwertAuftrag(iIdAuftrag);

		bestellungenSumme.setText(LPMain.getTextRespectUISPr("auft.bestellungen.summe") + " = "
				+ Helper.formatZahl(bestellwert, 2, LPMain.getTheClient().getLocUi()));
	}

	private void refreshEingangsrechnungen() throws Throwable {
		FilterKriterium[] filtersRechnungen = AuftragFilterFactory.getInstance()
				.createFKAuftragEingansrechnungen(auftragDto.getIId());
		if (auftragEingangsrechnung == null) {

			String[] aWhichButtonIUse = {};

			auftragEingangsrechnung = new PanelQuery(null, filtersRechnungen,
					QueryParameters.UC_ID_AUFTRAGEINGANGSRECHNUNGEN, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("er.modulname.tooltip"), true);

			splittbetragSumme = new JLabel();

			auftragEingangsrechnung.getToolBar().getToolsPanelCenter().add(splittbetragSumme, new GridBagConstraints(0,
					0, 1, 1, 1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			splittbetragSumme2 = new JLabel();
			auftragEingangsrechnung.getToolBar().getToolsPanelCenter().add(splittbetragSumme2, new GridBagConstraints(0,
					1, 1, 1, 1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			auftragEingangsrechnung.getToolBar().addButtonLeft("/com/lp/client/res/data_into.png",
					LPMain.getTextRespectUISPr("proj.projektverlauf.gehezubeleg"), GOTO_EINGANGSRECHNUNG,
					KeyStroke.getKeyStroke('G', java.awt.event.InputEvent.CTRL_MASK), null);
			auftragEingangsrechnung.enableToolsPanelButtons(true, GOTO_EINGANGSRECHNUNG);
			auftragEingangsrechnung.enableToolsPanelButtons(true, PanelBasis.ACTION_REFRESH);

			this.setComponentAt(IDX_PANEL_EINGANGSRECHNUNGEN, auftragEingangsrechnung);
		}

		String[] splittbetrag = DelegateFactory.getInstance().getAuftragDelegate()
				.berechneSummeSplittbetrag(auftragDto.getIId());

		splittbetragSumme.setText(splittbetrag[0]);
		splittbetragSumme2.setText(splittbetrag[1]);

		auftragEingangsrechnung.setDefaultFilter(filtersRechnungen);
	}

	public void dialogQueryAuftragFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRAuftragauswahl = AuftragFilterFactory.getInstance().createPanelFLRAuftrag(getInternalFrame(), true,
				false, null);

		new DialogQuery(panelQueryFLRAuftragauswahl);
	}

	public void dialogQueryAuftragLieferterminFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRAuftragauswahlLiefertermin = AuftragFilterFactory.getInstance()
				.createPanelFLRAuftrag(getInternalFrame(), true, false, null);

		new DialogQuery(panelQueryFLRAuftragauswahlLiefertermin);
	}

	public void dialogQueryAngebotFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRAngebotauswahl = AngebotFilterFactory.getInstance()
				.createPanelFLRAngebotErledigteVersteckt(getInternalFrame(), true, false); // pro Angebot 1 Auftrag ->
																							// FK offene
		// Angebote eines Mandanten

		new DialogQuery(panelQueryFLRAngebotauswahl);
	}

	public void dialogQueryAngebotFuerRahmenauftragFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRAngebotauswahlFuerRahmenauftrag = AngebotFilterFactory.getInstance()
				.createPanelFLRAngebotErledigteVersteckt(getInternalFrame(), true, false); // pro Angebot 1
																							// Auftrag -> FK
																							// offene
		// Angebote eines Mandanten

		new DialogQuery(panelQueryFLRAngebotauswahlFuerRahmenauftrag);
	}

	private void refreshSichtLieferstatusUnten() throws Throwable {

		int iSelektierteZeile = auftragSichtLieferstatus.getTable().getSelectedRow();

		Integer iIdPosition = -999;

		FilterKriterium[] fk = null;
		if (iSelektierteZeile > -1) {
			// in der selektierten Zeile ist column 0 die iIdPosition
			// und column 1
			// die Belegart @todo etwas weniger hardcoded ... PJ 5170
			iIdPosition = (Integer) auftragSichtLieferstatus.getTable().getValueAt(iSelektierteZeile, 0);
			String sBelegartkuerzel = (String) auftragSichtLieferstatus.getTable().getValueAt(iSelektierteZeile, 1);

			if (sBelegartkuerzel.equals(DelegateFactory.getInstance().getLocaleDelegate()
					.belegartFindByCNr(LocaleFac.BELEGART_AUFTRAG).getCKurzbezeichnung())) {

				fk = AuftragFilterFactory.getInstance().createFKSichtAuftragpositionAufLieferscheine(iIdPosition);

			} else {
				// bei Lieferscheinposition kann dieses Panel nicht
				// angewaehlt werden
				// -> Default ist eine entsprechende Meldung und der
				// Wechsel auf das logische
				// Vorgaenger Panel SichtLieferstatus
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("auft.hint.auftragpositionwaehlen"));

			}

		}

		fk = AuftragFilterFactory.getInstance().createFKSichtAuftragpositionAufLieferscheine(iIdPosition);

		if (sichtAuftragAufAndereBelegartenTop == null) {

			HelperClient.setMinimumAndPreferredSize(wbuLieferschein,
					new Dimension(Defaults.sizeFactor(100), HelperClient.getToolsPanelButtonDimension().height));
			HelperClient.setMinimumAndPreferredSize(wbuRechnung,
					new Dimension(Defaults.sizeFactor(100), HelperClient.getToolsPanelButtonDimension().height));

			wbuLieferschein.setEnabled(false);
			wbuLieferschein.setActivatable(false);

			wbuRechnung.setEnabled(false);
			wbuRechnung.setActivatable(false);

			sichtAuftragAufAndereBelegartenTop = new PanelQuery(null, fk,
					QueryParameters.UC_ID_AUFTRAGPOSITIONINLIEFERSCHEIN, null, getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.auftragpositioninlieferschein"), true);
			sichtAuftragAufAndereBelegartenTop.getToolBar().getToolsPanelLeft().add(wbuLieferschein);
			sichtAuftragAufAndereBelegartenTop.getToolBar().getToolsPanelLeft().add(wbuRechnung);

		}

		sichtAuftragAufAndereBelegartenTop.setDefaultFilter(fk);

		sichtAuftragAufAndereBelegartenTop.eventYouAreSelected(false);

		wbuRechnung.setOKey(null);
		wbuRechnung.setToolTipText(null);
		wbuLieferschein.setOKey(null);
		wbuLieferschein.setToolTipText(null);

		if (sichtAuftragAufAndereBelegartenTop.getSelectedId() != null) {

			aktualisiereGotoButtons();
		}

	}

	private void aktualisiereGotoButtons() throws ExceptionLP, Throwable {
		wbuRechnung.setOKey(null);
		wbuRechnung.setToolTipText(null);
		wbuLieferschein.setOKey(null);
		wbuLieferschein.setToolTipText(null);
		if (sichtAuftragAufAndereBelegartenTop.getSelectedId() != null) {
			LieferstatusDto lieferstatusDto = DelegateFactory.getInstance().getAuftragDelegate()
					.lieferstatusFindByPrimaryKey((Integer) sichtAuftragAufAndereBelegartenTop.getSelectedId());

			if (lieferstatusDto.getLieferscheinpositionIId() != null) {

				LieferscheinpositionDto oLieferscheinpositionDto = DelegateFactory.getInstance()
						.getLieferscheinpositionDelegate()
						.lieferscheinpositionFindByPrimaryKey(lieferstatusDto.getLieferscheinpositionIId());

				LieferscheinDto oLieferscheinDto = DelegateFactory.getInstance().getLsDelegate()
						.lieferscheinFindByPrimaryKey(oLieferscheinpositionDto.getLieferscheinIId());

				if (oLieferscheinDto.getRechnungIId() != null) {
					RechnungDto oRechnungDto = DelegateFactory.getInstance().getRechnungDelegate()
							.rechnungFindByPrimaryKey(oLieferscheinDto.getRechnungIId());
					wbuRechnung.setOKey(oLieferscheinDto.getRechnungIId());

					wbuRechnung.setToolTipText(oRechnungDto.getCNr() + " " + oRechnungDto.getStatusCNr());

				} else {
					wbuRechnung.setOKey(null);
					wbuRechnung.setToolTipText(null);

				}

				wbuLieferschein.setOKey(sichtAuftragAufAndereBelegartenTop.getSelectedId());

				wbuLieferschein.setOKey(oLieferscheinDto.getIId());
				wbuLieferschein.setToolTipText(oLieferscheinDto.getCNr() + " " + oLieferscheinDto.getStatusCNr());
			} else if (lieferstatusDto.getRechnungpositionIId() != null) {
				RechnungPositionDto reposDto = DelegateFactory.getInstance().getRechnungDelegate()
						.rechnungPositionFindByPrimaryKey(lieferstatusDto.getRechnungpositionIId());

				RechnungDto oRechnungDto = DelegateFactory.getInstance().getRechnungDelegate()
						.rechnungFindByPrimaryKey(reposDto.getRechnungIId());
				wbuRechnung.setOKey(reposDto.getRechnungIId());

				wbuRechnung.setToolTipText(oRechnungDto.getCNr() + " " + oRechnungDto.getStatusCNr());

			}
		}
	}

	/**
	 * AGILPRO CHANGES Changed visiblity from protected to public
	 * 
	 * @author Lukas Lisowski
	 * @param e ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		iIdAuftrag = auftragDto.getIId();
		// dtos hinterlegen
		initializeDtos(iIdAuftrag);

		int selectedIndex = getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUFTRAGAUSWAHL) {
			setTitleAuftrag(LPMain.getTextRespectUISPr("auft.title.panel.auswahl"));
			refreshAuftragAuswahl();
			auftragAuswahl.eventYouAreSelected(false);
			auftragAuswahl.updateButtons();
			// SK Damit nach Auswahl der Auftragliste die Meldung des Sicht
			// Lieferstatus wieder kommt
			if (auftragSichtLieferstatus != null) {
				auftragSichtLieferstatus = null;
			}

			// emptytable: 0 wenn es fuer das Default TabbedPane noch keinen
			// Eintrag gibt, die
			// restlichen Panels deaktivieren, die Grunddaten bleiben erreichbar
			if (getAuftragAuswahl().getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUFTRAGAUSWAHL, false);
			}
		} else if (selectedIndex == IDX_PANEL_AUFTRAGKOPFDATEN) {

			refreshAuftragKopfdaten();
			auftragKopfdaten.eventYouAreSelected(false); // sonst werden die
			// buttons nicht
			// richtig gesetzt!

		} else if (selectedIndex == IDX_PANEL_AUFTRAGPOSITIONEN) {

			refreshAuftragPositionen();

			LPButtonAction item = (LPButtonAction) auftragPositionenTop.getHmOfButtons().get(MY_OWN_NEW_TOGGLE_GESEHEN);

			item.getButton().setVisible(false);

			LPButtonAction itemHVMA = (LPButtonAction) auftragPositionenTop.getHmOfButtons()
					.get(MY_OWN_NEW_TOGGLE_HVMAUEBERTRAGEN);

			itemHVMA.getButton().setVisible(false);

			auftragPositionen.eventYouAreSelected(false);
			auftragPositionenTop.updateButtons(auftragPositionenBottom.getLockedstateDetailMainKey());

			boolean b = DelegateFactory.getInstance().getAuftragDelegate().hatAuftragVersandweg(getAuftragDto());
			if (b == true) {
				item.getButton().setVisible(true);
			}

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_HVMA2)) {
				itemHVMA.getButton().setVisible(true);
			}

		} else if (selectedIndex == IDX_PANEL_AUFTRAGKONDITIONEN) {

			refreshAuftragKonditionen();
			auftragKonditionen.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_AUFTRAGTEILNEHMER) {

			refreshAuftragTeilnehmer();

			// pqaddbutton: 2 Nachdem das PanelSplit erzeugt wurde, muss
			// eventYouAreSelected
			// auf jeden Fall noch einmal aufgerufen werden, wegen dem Status
			// der zusaetzlichen Buttons
			auftragTeilnehmer.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			auftragTeilnehmerTop.updateButtons(auftragTeilnehmerBottom.getLockedstateDetailMainKey());

		} else if (selectedIndex == IDX_PANEL_ZEITBESTAETIGUNG) {

			refreshZeitbestaetigung();

			// pqaddbutton: 2 Nachdem das PanelSplit erzeugt wurde, muss
			// eventYouAreSelected
			// auf jeden Fall noch einmal aufgerufen werden, wegen dem Status
			// der zusaetzlichen Buttons
			panelSplitAnwesenheitsbestaetigung.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelQueryAnwesenheitsbestaetigung
					.updateButtons(panelBottomAnwesenheitsbestaetigung.getLockedstateDetailMainKey());

		} else if (selectedIndex == IDX_PANEL_AUFTRAGKOSTENSTELLEN) {

			refreshAuftragKostenstelle();
			auftragKostenstelle.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			auftragKostenstelleTop.updateButtons(auftragKostenstelleBottom.getLockedstateDetailMainKey());

		} else if (selectedIndex == IDX_PANEL_ZEITPLAN) {

			refreshZeitplan();
			auftragZeitplan.eventYouAreSelected(false);
			auftragZeitplanTop.updateButtons(auftragZeitplanBottom.getLockedstateDetailMainKey());

		} else if (selectedIndex == IDX_PANEL_ZAHLUNGSPLAN) {

			refreshZahlungsplan();
			auftragZahlungsplan.eventYouAreSelected(false);
			auftragZahlungsplanTop.updateButtons(auftragZahlungsplanBottom.getLockedstateDetailMainKey());

		} else if (selectedIndex == IDX_PANEL_ZAHLUNGSPLANMEILENSTEIN) {
			refreshZahlungsplan();

			auftragZahlungsplanTop.eventYouAreSelected(false);
			auftragZahlungsplanBottom.setKeyWhenDetailPanel(auftragZahlungsplanTop.getSelectedId());
			auftragZahlungsplanBottom.eventYouAreSelected(false);

			if (auftragZahlungsplanTop.getSelectedId() != null) {
				refreshZahlungsplanmeilenstein();
				auftragZahlungsplanmeilenstein.eventYouAreSelected(false);
				auftragZahlungsplanmeilensteinTop
						.updateButtons(auftragZahlungsplanmeilensteinBottom.getLockedstateDetailMainKey());
			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
						LPMain.getTextRespectUISPr("auft.zahlungsplanmeilenstein.keinzahlungsplanausgewaehlt"));

				setSelectedIndex(IDX_PANEL_ZAHLUNGSPLAN);
				lPEventObjectChanged(e);
			}

		} else if (selectedIndex == IDX_PANEL_SICHTLIEFERSTATUS) {

			setTitleAuftrag(LPMain.getTextRespectUISPr("auft.title.panel.sichtlieferstatus"));
			refreshSichtLieferstatus();
			auftragSichtLieferstatus.eventYouAreSelected(false);
			auftragSichtLieferstatus.updateButtons();

		} else if (selectedIndex == IDX_PANEL_SICHTRAHMEN) {

			setTitleAuftrag(LPMain.getTextRespectUISPr("lp.sichtrahmen"));
			refreshAuftragSichtRahmen();
			auftragSichtRahmen.eventYouAreSelected(false);
			auftragSichtRahmenTop.updateButtons(auftragSichtRahmenBottom.getLockedstateDetailMainKey());
		} else if (selectedIndex == IDX_PANEL_AUFTRAGUEBERSICHT) {

			if (!bKriterienAuftragUebersichtUeberMenueAufgerufen) {
				getKriterienAuftragUebersicht();
				getInternalFrame().showPanelDialog(pdKriterienAuftragUebersicht);
			}
		} else if (selectedIndex == IDX_PANEL_AUFTRAGZEITEN) {

			if (!pdKriterienAuftragzeitenUeberMenueAufgerufen) {
				getKriterienAuftragzeiten();
				getInternalFrame().showPanelDialog(pdKriterienAuftragzeiten);
			}

		} else if (selectedIndex == IDX_PANEL_LSRE) {

			getPanelTabelleLoszeiten().eventYouAreSelected(false);

			setSelectedComponent(auftragLSRE);

			auftragLSRE.updateButtons(new LockStateValue(null, null, PanelBasis.LOCK_IS_NOT_LOCKED));
		} else if (selectedIndex == IDX_PANEL_LOSE) {

			refreshLose();
			auftragLose.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_BESTELLUNGEN) {

			refreshBestellungen();
			auftragBestellungen.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_EINGANGSRECHNUNGEN) {

			refreshEingangsrechnungen();
			auftragEingangsrechnung.eventYouAreSelected(false);

		} else if (selectedIndex == IDX_PANEL_AUFTRAGEIGENSCHAFTEN) {

			refreshEigenschaften(getAuftragDto().getIId());
			panelDetailAuftragseigenschaft.eventYouAreSelected(false);
			if (getAuftragDto() != null) {
				LPButtonAction item = null;
				if (getAuftragDto().getStatusCNr() != null) {
					if (getAuftragDto().getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)) {
						item = (LPButtonAction) panelDetailAuftragseigenschaft.getHmOfButtons()
								.get(PanelBasis.ACTION_UPDATE);
						item.getButton().setEnabled(false);
					}
				}
			}

		}

		setTitleAuftrag("");
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRBegruendung) {

				DelegateFactory.getInstance().getAuftragDelegate().updateAuftragBegruendung(getAuftragDto().getIId(),
						null);
				if (auftragKonditionen != null) {
					auftragKonditionen.eventYouAreSelected(false);
				}

			}

		}

		else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			handleItemChanged(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_ESCAPE) {
			// goto PanelQuery
			// if (e.getSource() == auftragPositionen) {
			// auftragPositionenBottom.isl
			setSelectedComponent(getAuftragAuswahl());
			// }
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (e.getSource() == auftragPositionenBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				auftragPositionenTop.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == auftragTeilnehmerBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				auftragTeilnehmerTop.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelBottomAnwesenheitsbestaetigung) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryAnwesenheitsbestaetigung.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == auftragKostenstelleBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				auftragKostenstelleTop.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == auftragZeitplanBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				auftragZeitplanTop.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == auftragZahlungsplanBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				auftragZahlungsplanTop.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == auftragZahlungsplanmeilensteinBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				auftragZahlungsplanmeilensteinTop.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == auftragSichtRahmenBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				auftragSichtRahmenTop.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelAuftragsnrnrnBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelAuftragsnrnrnTopQP.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else
		// eine Zeile in einer Tabelle, bei der ich Listener bin, wurde
		// doppelgeklickt
		if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			setTitleAuftrag("");
			// usemenuebar: Hier super.eventItemchanged(e); aufrufen, damit die
			// Menuebar, wenn auf ein "unteres Ohrwaschl"
			// geklickt wird, angezeigt wird.
			super.lPEventItemChanged(e);
		}

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			handleGotoDetailPanel(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			// pqaddbutton: 3 Es wurde der spezielle Neu Button gedrueckt
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

			if (sAspectInfo.equals(MY_OWN_NEW_NEU_EXTERNER_TEILNEHMER)) {
				if (e.getSource() == auftragTeilnehmerTop) {
					auftragTeilnehmerBottom.eventActionNew(e, true, false);
					auftragTeilnehmerBottom.eventYouAreSelected(false);
					setSelectedComponent(auftragTeilnehmer); // ui
				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_NEU_AUS_ANGEBOT)) {
				if (e.getSource() == auftragAuswahl) {
					dialogQueryAngebotFromListe(null);
				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_RAHMENAUFTRAG_NEU_AUS_ANGEBOT)) {
				if (e.getSource() == auftragAuswahl) {
					dialogQueryAngebotFuerRahmenauftragFromListe(null);
				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_NEU_AUS_AUFTRAG)) {
				if (e.getSource() == auftragAuswahl) {
					dialogQueryAuftragFromListe(null);
				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_NEU_AUS_AUFTRAG_LIEFERTERMIN)) {
				if (e.getSource() == auftragAuswahl) {
					dialogQueryAuftragLieferterminFromListe(null);
				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_EXTRA_ZEITPLAN_IMPORTIEREN)) {
				if (e.getSource() == auftragZeitplanTop) {
					panelQueryFLRZeitplantyp = new PanelQueryFLR(null,
							SystemFilterFactory.getInstance().createFKMandantCNr(), QueryParameters.UC_ID_ZEITPLANTYP,
							null, getInternalFrame(), LPMain.getTextRespectUISPr("auft.zeitplantyp"));
					new DialogQuery(panelQueryFLRZeitplantyp);
				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_NEU_ARTIKEL_AUS_SCHNELLANLAGE_BEARBEITEN)) {
				if (getAuftragDto() != null && getAuftragDto().getIId() != null) {
					AuftragpositionDto[] aufposDtos = DelegateFactory.getInstance().getAuftragpositionDelegate()
							.auftragpositionFindByAuftrag(getAuftragDto().getIId());

					if (aufposDtos != null && aufposDtos.length > 0 && aufposDtos[0].getArtikelIId() != null) {
						panelDialogAuftragArtikelSchnellanlageArtikelAendern = new PanelDialogAuftragArtikelSchnellanlage(
								getInternalFrame(), aufposDtos[0].getArtikelIId());
						getInternalFrame().showPanelDialog(panelDialogAuftragArtikelSchnellanlageArtikelAendern);
					}

				}

			} else if (sAspectInfo.equals(BUTTON_SORTIERE_NACH_ARTIKELNUMMER)) {

				// Vorher fragen
				if (aktualisiereAuftragstatus()) {

					boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getTextRespectUISPr("lp.sortierenach.frage"));

					if (b == true) {

						DelegateFactory.getInstance().getAuftragpositionDelegate()
								.sortiereNachArtikelnummer(getAuftragDto().getIId());

						auftragPositionenTop.eventYouAreSelected(false);

					}
				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_NEU_AUS_SCHNELLANLAGE)) {
				// In KopfdatenWechseln
				// um mit Auftraegen arbeiten zu koennen, muss das Hauptlager
				// des Mandanten definiert sein
				LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate().getHauptlagerDesMandanten();

				// emptytable: 1 wenn es bisher keinen eintrag gibt, die
				// restlichen
				// panels aktivieren
				if (auftragAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUFTRAGAUSWAHL, true);
				}

				// SP5351
				ItemChangedEvent evNew = new ItemChangedEvent(e.getSource(), ItemChangedEvent.ACTION_NEW);

				ParametermandantDto parameterMandant = DelegateFactory.getInstance().getParameterDelegate()
						.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
								ParameterFac.PARAMETER_ETIKETTENDRUCK_NACH_SCHNELLANLAGE);

				int i = (Integer) parameterMandant.getCWertAsObject();

				if (i == 0 || i == 2) {

					//
					// In KopfdatenWechseln
					// um mit Auftraegen arbeiten zu koennen, muss das
					// Hauptlager
					// des Mandanten definiert sein
					DelegateFactory.getInstance().getLagerDelegate().getHauptlagerDesMandanten();

					// emptytable: 1 wenn es bisher keinen eintrag gibt, die
					// restlichen
					// panels aktivieren
					if (auftragAuswahl.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUFTRAGAUSWAHL, true);
					}

					refreshAuftragKopfdaten().eventActionNew(evNew, true, false);
					auftragKopfdaten.setSchnellanlage(true);
					setSelectedComponent(auftragKopfdaten);
					// PJ20262
					auftragKopfdaten.dialogQueryKunde(null);

				} else {

					MandantDto mDto = DelegateFactory.getInstance().getMandantDelegate()
							.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant());

					KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
							.kundeFindByiIdPartnercNrMandantOhneExc(mDto.getPartnerIId(), mDto.getCNr());

					if (kdDto != null) {

						refreshAuftragKopfdaten().eventActionNew(evNew, true, false);
						auftragKopfdaten.eventYouAreSelected(false);
						auftragKopfdaten.components2auftragDto();

						// Kunde bin ich selbst
						getAuftragDto().setAuftragartCNr(AuftragServiceFac.AUFTRAGART_FREI);

						getAuftragDto().setKundeIIdAuftragsadresse(kdDto.getIId());
						setKundeAuftragDto(kdDto);
						getAuftragDto().setKundeIIdLieferadresse(kdDto.getIId());
						getAuftragDto().setKundeIIdRechnungsadresse(kdDto.getIId());
						getAuftragDto().setLagerIIdAbbuchungslager(lagerDto.getIId());
						getAuftragDto().setKostIId(kdDto.getKostenstelleIId());

						auftragKopfdaten.initKonditionen();

						Integer auftragIId = DelegateFactory.getInstance().getAuftragDelegate()
								.createAuftrag(getAuftragDto());

						auftragKopfdaten.setSchnellanlage(true);
						setSelectedComponent(auftragKopfdaten);
						getInternalFrame().setKeyWasForLockMe(auftragIId.toString());

						auftragAuswahl.clearDirektFilter();
						auftragAuswahl.eventYouAreSelected(false);
						auftragAuswahl.setSelectedId(auftragIId);
						auftragAuswahl.eventYouAreSelected(false);
						auftragKopfdaten.eventYouAreSelected(false);

						auftragKopfdaten.eventActionUpdate(null, false);
						auftragKopfdaten.setSchnellanlage(true);

						auftragKopfdaten.updateButtons(auftragKopfdaten.getLockedstateDetailMainKey());

						if (i == 1) {
							// Etikett Drucken

							PanelReportKriterien krit = new PanelReportKriterien(getInternalFrame(),
									new ReportAuftragsetikett(getInternalFrame(), auftragIId, ""), "",
									kdDto.getPartnerDto(), getAuftragDto().getAnsprechparnterIId(), false, false,
									false); // jetzt das tatsaechliche
											// Drucken
							krit.druckeArchiviereUndSetzeVersandstatusEinesBelegs();
						}
					} else {
						// Meldung
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("auft.schnellanlage.mandantnichtalskundedfiniert"));
						return;
					}
				}

			} else if (sAspectInfo.equals(MY_OWN_NEW_TOGGLE_VERRECHENBAR)) {
				if (e.getSource() == auftragAuswahl && getAuftragDto() != null && getAuftragDto().getIId() != null) {
					DelegateFactory.getInstance().getAuftragDelegate().toggleVerrechenbar(getAuftragDto().getIId());
					auftragAuswahl.eventYouAreSelected(false);
				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_TOGGLE_GESEHEN)) {
				if (e.getSource() == auftragPositionenTop) {

					ArrayList<Integer> al = auftragPositionenTop.getSelectedIdsAsInteger();

					for (int i = 0; i < al.size(); i++) {
						DelegateFactory.getInstance().getAuftragpositionDelegate().toggleGesehen(al.get(i));
					}

					auftragPositionen.eventYouAreSelected(false);
				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_TOGGLE_HVMAUEBERTRAGEN)) {
				if (e.getSource() == auftragPositionenTop) {

					ArrayList<Integer> al = auftragPositionenTop.getSelectedIdsAsInteger();

					for (int i = 0; i < al.size(); i++) {
						DelegateFactory.getInstance().getAuftragpositionDelegate().toggleHvmauebertragen(al.get(i));
					}

					auftragPositionen.eventYouAreSelected(false);
				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_LIEFERSCHEIN_PREISE_UPDATEN)) {
				if (e.getSource() == auftragPositionenTop) {
					ArrayList<LieferscheinpositionDto> lsposAusgelassenDto = DelegateFactory.getInstance()
							.getLieferscheinpositionDelegate()
							.preiseAusAuftragspositionenUebernehmen(getAuftragDto().getIId());

					if (lsposAusgelassenDto != null && lsposAusgelassenDto.size() > 0) {

						String crlf = new String(new byte[] { 13, 10 });

						String message_ausgelassen = LPMain
								.getTextRespectUISPr("auft.preise.inls.rueckpflegen.error.title") + crlf + crlf;

						for (int i = 0; i < lsposAusgelassenDto.size(); i++) {
							LieferscheinDto lsDto = DelegateFactory.getInstance().getLsDelegate()
									.lieferscheinFindByPrimaryKey(lsposAusgelassenDto.get(i).getLieferscheinIId());

							message_ausgelassen += lsDto.getCNr() + " Pos. "
									+ DelegateFactory.getInstance().getLieferscheinpositionDelegate()
											.getPositionNummer(lsposAusgelassenDto.get(i).getIId())
									+ crlf;

						}

						DialogFactory.showMessageMitScrollbar("Info", message_ausgelassen, true);

					}

				}
			} else if (sAspectInfo.equals(GOTO_LOS)) {
				if (e.getSource() == auftragLose) {

					Object key = auftragLose.getSelectedId();

					if (key != null) {
						wbuGotoLos.setOKey((Integer) key);
						wbuGotoLos.actionPerformed(new ActionEvent(wbuGotoLos, 0, WrapperGotoButton.ACTION_GOTO));
					}

				}
			} else if (sAspectInfo.equals(GOTO_EINGANGSRECHNUNG)) {
				if (e.getSource() == auftragEingangsrechnung) {

					Object key = auftragEingangsrechnung.getSelectedId();

					if (key != null) {
						wbuGotoEingangsrechnung.setOKey((Integer) key);
						wbuGotoEingangsrechnung.actionPerformed(
								new ActionEvent(wbuGotoEingangsrechnung, 0, WrapperGotoButton.ACTION_GOTO));
					}

				}
			} else if (sAspectInfo.equals(GOTO_BESTELLUNG)) {
				if (e.getSource() == auftragBestellungen) {

					Object key = auftragBestellungen.getSelectedId();

					if (key != null) {
						wbuGotoBestellung.setOKey((Integer) key);
						wbuGotoBestellung
								.actionPerformed(new ActionEvent(wbuGotoBestellung, 0, WrapperGotoButton.ACTION_GOTO));
					}

				}
			} else if (sAspectInfo.equals(MY_OWN_NEW_ABRUF_ZU_RAHMEN)) {
				if (e.getSource() == auftragAuswahl) {
					// ein Abrufauftrag kann nur aus einem Rahmenauftrag
					// generiert werden
					if (pruefeAktuellenAuftrag()) {
						if (getAuftragDto().getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
							if (getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
									|| getAuftragDto().getStatusCNr()
											.equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)) {
								refreshAuftragKopfdaten();

								// jetzt dafuer sorgen, dass die Buttons richtig
								// geschalten werden
								getInternalFrame().setKeyWasForLockMe(LPMain.getLockMeForNew());

								// ZUERST wechseln, denn hier wird die aktuelle
								// Bestellung neu festgelegt
								setSelectedComponent(auftragKopfdaten);

								auftragKopfdaten.eventActionNew(e, true, false);

								// an dieser Stelle sind die Panels nach
								// Bitmuster geschalten
								getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUFTRAGKOPFDATEN,
										false);
							} else {
								showStatusMessage("lp.hint", "auft.keinabrufzudiesemrahmen");
							}
						} else {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("auft.rahmenauftragwaehlen"));
						}
					}
				}
			} else if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				if (istAktualisierenAuftragErlaubt()) {
					// copypaste
					einfuegenHV();
				}
			} else if (sAspectInfo.endsWith(ACTION_SPECIAL_PREISE_NEU_KALKULIEREN)) {

				if (getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {

					//
					PanelPositionenArtikelVerkauf panelBottom = ((PanelPositionenArtikelVerkauf) auftragPositionenBottom.panelArtikel);

					AuftragpositionDto[] dtos = DelegateFactory.getInstance().getAuftragpositionDelegate()
							.auftragpositionFindByAuftrag(getAuftragDto().getIId());

					for (int i = 0; i < dtos.length; i++) {
						if (dtos[i].getPositionsartCNr().equals(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {
							auftragPositionenTop.setSelectedId(dtos[i].getIId());
							auftragPositionenTop.eventYouAreSelected(false);
							panelBottom.setKeyWhenDetailPanel(dtos[i].getIId());
							panelBottom.update();
							panelBottom.berechneVerkaufspreis(false);
							panelBottom.pruefeNettoPreis();
							auftragPositionenBottom.eventActionSave(null, false);
						}
					}
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("auft.error.neuberechnen"));
				}

			} else if (sAspectInfo.equals(BUTTON_IMPORTCSV_AUFTRAGPOSITIONEN)) {
				// SP4966
				if (getAuftragDto().getBestellungIIdAndererMandant() != null) {

					BestellungDto bsDto = DelegateFactory.getInstance().getBestellungDelegate()
							.bestellungFindByPrimaryKey(getAuftragDto().getBestellungIIdAndererMandant());

					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("auft.error.besinanderemmandanten.nurmehrmengeaenderbar"));
					Object[] pattern = new Object[] { bsDto.getCNr(), bsDto.getMandantCNr() };

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"), mf.format(pattern));
					return;

				}

				importCSVAuftragPositionen();
				auftragPositionen.eventYouAreSelected(false); // refresh
			} else if (sAspectInfo.equals(BUTTON_SCHNELLERFASSUNG_AUFTRAGPOSITIONEN)) {

				if (istAktualisierenAuftragErlaubt()) {

					// SP4966
					if (getAuftragDto().getBestellungIIdAndererMandant() != null) {

						BestellungDto bsDto = DelegateFactory.getInstance().getBestellungDelegate()
								.bestellungFindByPrimaryKey(getAuftragDto().getBestellungIIdAndererMandant());

						MessageFormat mf = new MessageFormat(
								LPMain.getTextRespectUISPr("auft.error.besinanderemmandanten.nurmehrmengeaenderbar"));
						Object[] pattern = new Object[] { bsDto.getCNr(), bsDto.getMandantCNr() };

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"), mf.format(pattern));
						return;

					}

					DialogAuftragspositionenSchnellerfassung d = new DialogAuftragspositionenSchnellerfassung(
							auftragPositionen, getAuftragDto());
					LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);
				}
				auftragPositionen.eventYouAreSelected(false); // refresh
			} else if (sAspectInfo.endsWith(ACTION_SPECIAL_NEU_AUS_PROJEKT)) {
				dialogQueryProjektFromListe(null);
			} else if (sAspectInfo.equals(MENUE_ACTION_ETIKETT_DRUCKEN)) {
				String auftragsnummer = (String) JOptionPane.showInputDialog(this,
						LPMain.getTextRespectUISPr("label.auftragnummer"), LPMain.getTextRespectUISPr("lp.frage"),
						JOptionPane.QUESTION_MESSAGE);

				if (auftragsnummer != null && auftragsnummer.length() > 1 && auftragsnummer.startsWith("$A")) {
					auftragsnummer = auftragsnummer.substring(2);
					AuftragpositionDto auftragposDto = null;
					// Zuerst auftrag auswaehlen und erste Position holen
					AuftragDto auftragDto = null;
					try {
						auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
								.auftragFindByMandantCNrCNr(LPMain.getTheClient().getMandant(), auftragsnummer);
					} catch (ExceptionLP ex) {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
								LPMain.getTextRespectUISPr("auft.auftragnichtvorhanden"));
						return;
					}
					try {
						Integer iMinISort = DelegateFactory.getInstance().getAuftragpositionDelegate()
								.getMinISort(auftragDto.getIId());
						auftragposDto = DelegateFactory.getInstance().getAuftragpositionDelegate()
								.auftragpositionFindByAuftragISort(auftragDto.getIId(), iMinISort);
					} catch (ExceptionLP ex) {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
								LPMain.getTextRespectUISPr("auft.hatkeinepositionen"));
						return;
					}
					// Die erste Position muss von der Art Position sein
					if (AuftragServiceFac.AUFTRAGPOSITIONART_POSITION.equals(auftragposDto.getPositionsartCNr())) {
						// Wenn die Position korrekt war die naechste Pos holen
						// und drucken
						try {
							auftragposDto = DelegateFactory.getInstance().getAuftragpositionDelegate()
									.auftragpositionFindByAuftragISort(auftragposDto.getBelegIId(),
											auftragposDto.getISort() + 1);
						} catch (ExceptionLP ex) {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("auft.keinepositionzudrucken"));
							return;
						}
						if (auftragposDto != null) {
							ReportAuftragSrnEtikett reportEtikett = new ReportAuftragSrnEtikett(getInternalFrame(),
									auftragposDto.getBelegIId(), auftragposDto.getIId(), auftragposDto.getArtikelIId(),
									"");
							getInternalFrame().showReportKriterienZweiDrucker(reportEtikett, null, null, false, false,
									false, true);
							reportEtikett.eventYouAreSelected(false);
						}
					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
								LPMain.getTextRespectUISPr("auft.falschepositionsart"));
						return;
					}
				} else {
					if (auftragsnummer != null) {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								"Keine g\u00FCltige Autragsnummer " + auftragsnummer);
						return;
					}
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == auftragAuswahl) {
				// um mit Auftraegen arbeiten zu koennen, muss das Hauptlager
				// des Mandanten definiert sein
				DelegateFactory.getInstance().getLagerDelegate().getHauptlagerDesMandanten();

				// emptytable: 1 wenn es bisher keinen eintrag gibt, die
				// restlichen
				// panels aktivieren
				if (auftragAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUFTRAGAUSWAHL, true);
				}

				refreshAuftragKopfdaten().eventActionNew(e, true, false);
				auftragKopfdaten.setSchnellanlage(false);

				setSelectedComponent(auftragKopfdaten);
			} else if (e.getSource() == auftragPositionenTop) {
				// pqnewnotallowed: 0 das Anlegen einer neuen Position nur
				// ausloesen, wenn es erlaubt ist
				if (istAktualisierenAuftragErlaubt()) {
					auftragPositionenBottom.eventActionNew(e, true, false);
					auftragPositionenBottom.eventYouAreSelected(false);
					setSelectedComponent(auftragPositionen); // ui
					// VF IMS 2180
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUFTRAGPOSITIONEN, false);
				} else {
					auftragPositionen.eventYouAreSelected(false);
				}
			} else if (e.getSource() == auftragTeilnehmerTop) {
				auftragTeilnehmerBottom.eventActionNew(e, true, false);
				auftragTeilnehmerBottom.eventYouAreSelected(false);
				setSelectedComponent(auftragTeilnehmer); // ui
			} else if (e.getSource() == panelQueryAnwesenheitsbestaetigung) {
				panelBottomAnwesenheitsbestaetigung.eventActionNew(e, true, false);
				panelBottomAnwesenheitsbestaetigung.eventYouAreSelected(false);
				setSelectedComponent(panelSplitAnwesenheitsbestaetigung); // ui
			} else if (e.getSource() == auftragKostenstelleTop) {
				auftragKostenstelleBottom.eventActionNew(e, true, false);
				auftragKostenstelleBottom.eventYouAreSelected(false);
				setSelectedComponent(auftragKostenstelle); // ui
			} else if (e.getSource() == auftragZeitplanTop) {
				auftragZeitplanBottom.eventActionNew(e, true, false);
				auftragZeitplanBottom.eventYouAreSelected(false);
				setSelectedComponent(auftragZeitplan); // ui
			} else if (e.getSource() == auftragZahlungsplanTop) {
				auftragZahlungsplanBottom.eventActionNew(e, true, false);
				auftragZahlungsplanBottom.eventYouAreSelected(false);
				setSelectedComponent(auftragZahlungsplan); // ui
			} else if (e.getSource() == auftragZahlungsplanmeilensteinTop) {
				auftragZahlungsplanmeilensteinBottom.eventActionNew(e, true, false);
				auftragZahlungsplanmeilensteinBottom.eventYouAreSelected(false);
				setSelectedComponent(auftragZahlungsplanmeilenstein); // ui
			} else if (e.getSource() == panelAuftragsnrnrnTopQP) {
				panelAuftragsnrnrnBottomD.eventActionNew(e, true, false);
				panelAuftragsnrnrnBottomD.eventYouAreSelected(false);
			}
		} else

		// wir landen hier bei der Abfolge Button Aendern -> xx -> Button
		// Discard
		if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == auftragKopfdaten) {
				// aenderewaehrung: 6 die Kopfdaten muessen mit den
				// urspruenglichen Daten befuellt werden
				auftragKopfdaten.eventYouAreSelected(false);
				auftragKopfdaten.updateButtons(auftragKopfdaten.getLockedstateDetailMainKey());
			} else if (e.getSource() == auftragPositionenBottom) {
				auftragPositionen.eventYouAreSelected(false); // das 1:n Panel
				// neu aufbauen
			} else if (e.getSource() == auftragTeilnehmerBottom) {
				auftragTeilnehmer.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomAnwesenheitsbestaetigung) {
				panelSplitAnwesenheitsbestaetigung.eventYouAreSelected(false);
			} else if (e.getSource() == auftragKostenstelleBottom) {
				auftragKostenstelle.eventYouAreSelected(false);
			} else if (e.getSource() == auftragZeitplanBottom) {
				auftragZeitplan.eventYouAreSelected(false);
			} else if (e.getSource() == auftragZahlungsplanBottom) {
				auftragZahlungsplan.eventYouAreSelected(false);
			} else if (e.getSource() == auftragZahlungsplanmeilensteinBottom) {
				auftragZahlungsplanmeilenstein.eventYouAreSelected(false);
			} else if (e.getSource() == auftragSichtRahmenBottom) {
				auftragSichtRahmen.eventYouAreSelected(false);
			} else if (e.getSource() == panelAuftragsnrnrnBottomD) {
				panelAuftragsnrnrnSP.eventYouAreSelected(false);
			} else
				return;
		} else
		// Wir landen hier nach Button Save
		if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			handleActionSave(e);
		} else
		// wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
		if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == auftragPositionenBottom) {
				// btndiscard: 1 bei einem Neu im 1:n Panel wurde der
				// KeyForLockMe ueberschrieben
				setKeyWasForLockMe();
				if (auftragPositionenBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = auftragPositionenTop.getId2SelectAfterDelete();
					auftragPositionenTop.setSelectedId(oNaechster);
				}
				auftragPositionen.eventYouAreSelected(false); // refresh auf das
				// gesamte 1:n
				// panel
			} else if (e.getSource() == auftragTeilnehmerBottom) {
				setKeyWasForLockMe();
				if (auftragTeilnehmerBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = auftragTeilnehmerTop.getId2SelectAfterDelete();
					auftragTeilnehmerTop.setSelectedId(oNaechster);
				}
				auftragTeilnehmer.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomAnwesenheitsbestaetigung) {
				setKeyWasForLockMe();
				if (panelBottomAnwesenheitsbestaetigung.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryAnwesenheitsbestaetigung.getId2SelectAfterDelete();
					panelQueryAnwesenheitsbestaetigung.setSelectedId(oNaechster);
				}
				panelSplitAnwesenheitsbestaetigung.eventYouAreSelected(false);
			} else if (e.getSource() == auftragKostenstelleBottom) {
				setKeyWasForLockMe();
				if (auftragKostenstelleBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = auftragKostenstelleTop.getId2SelectAfterDelete();
					auftragKostenstelleTop.setSelectedId(oNaechster);
				}
				auftragKostenstelle.eventYouAreSelected(false);
			} else if (e.getSource() == auftragZeitplanBottom) {
				setKeyWasForLockMe();
				if (auftragZeitplanBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = auftragZeitplanTop.getId2SelectAfterDelete();
					auftragZeitplanTop.setSelectedId(oNaechster);
				}
				auftragZeitplan.eventYouAreSelected(false);
			} else if (e.getSource() == auftragZahlungsplanBottom) {
				setKeyWasForLockMe();
				if (auftragZahlungsplanBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = auftragZahlungsplanTop.getId2SelectAfterDelete();
					auftragZahlungsplanTop.setSelectedId(oNaechster);
				}
				auftragZahlungsplan.eventYouAreSelected(false);
			} else if (e.getSource() == auftragZahlungsplanmeilensteinBottom) {
				setKeyWasForLockMe();
				if (auftragZahlungsplanmeilensteinBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = auftragZahlungsplanmeilensteinTop.getId2SelectAfterDelete();
					auftragZahlungsplanmeilensteinTop.setSelectedId(oNaechster);
				}
				auftragZahlungsplanmeilenstein.eventYouAreSelected(false);
			} else if (e.getSource() == auftragSichtRahmenBottom) {
				setKeyWasForLockMe();
				auftragSichtRahmen.eventYouAreSelected(false);
			} else if (e.getSource() == auftragKopfdaten) {
				// btndiscard: 2 die Liste neu laden, falls sich etwas geaendert
				// hat
				auftragAuswahl.eventYouAreSelected(false);

				// btndiscard: 3 nach einem Discard ist der aktuelle Key nicht
				// mehr gesetzt
				setKeyWasForLockMe();

				// btndiscard: 4 der Key der Kopfdaten steht noch auf new|...
				auftragKopfdaten.setKeyWhenDetailPanel(auftragAuswahl.getSelectedId());

				// btndiscard: 5 auf die Auswahl schalten
				setSelectedComponent(auftragAuswahl);

				// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
				// restlichen panel deaktivieren
				if (auftragAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUFTRAGAUSWAHL, false);
				}

				auftragKopfdaten.updateButtons(auftragKopfdaten.getLockedstateDetailMainKey());
			}
		} else
		// der OK Button in einem PanelDialog wurde gedrueckt
		if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == pdKriterienAuftragUebersicht) {
				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = pdKriterienAuftragUebersicht.getAlleFilterKriterien();

				// die Kriterien dem PanelTabelle setzen
				refreshAuftragUebersicht();
				ptAuftragUebersicht.setDefaultFilter(aAlleKriterien);

				// die Uebersicht aktualisieren
				ptAuftragUebersicht.eventYouAreSelected(false);

				// man steht auf alle Faelle auf der Uebersicht
				setSelectedComponent(ptAuftragUebersicht);
				setTitleAuftragOhneAuftragnummer(LPMain.getTextRespectUISPr("lp.umsatzuebersicht"));
				bKriterienAuftragUebersichtUeberMenueAufgerufen = false;
				ptAuftragUebersicht.updateButtons(new LockStateValue(null, null, PanelBasis.LOCK_IS_NOT_LOCKED));
			} else if (e.getSource() == pdKriterienAuftragzeiten) {
				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = pdKriterienAuftragzeiten.getAlleFilterKriterien();

				// die Kriterien dem PanelTabelle setzen
				refreshAuftragzeiten();
				ptAuftragzeiten.setDefaultFilter(aAlleKriterien);

				// die Auftragzeiten aktualisieren
				ptAuftragzeiten.eventYouAreSelected(false);

				// man steht auf alle Faelle in den Auftragzeiten
				setSelectedComponent(ptAuftragzeiten);
				setTitleAuftrag(LPMain.getTextRespectUISPr("auft.title.panel.auftragzeiten"));
				pdKriterienAuftragzeitenUeberMenueAufgerufen = false;
				ptAuftragzeiten.updateButtons(new LockStateValue(null, null, PanelBasis.LOCK_IS_NOT_LOCKED));
			} else if (e.getSource() == panelDialogAuftragArtikelSchnellanlageArtikelAendern) {
				// Artikel updaten
				ArtikelDto aDto = panelDialogAuftragArtikelSchnellanlageArtikelAendern
						.getArtikelDtoVorhandenMitNeuenBezeichnungen();

				DelegateFactory.getInstance().getArtikelDelegate().updateArtikel(aDto);

				PaneldatenDto[] paneldatenDtos = panelDialogAuftragArtikelSchnellanlageArtikelAendern
						.getPaneldatenDtos();

				for (int i = 0; i < paneldatenDtos.length; i++) {
					paneldatenDtos[i].setCKey(aDto.getIId() + "");
				}

				DelegateFactory.getInstance().getPanelDelegate().createPaneldaten(paneldatenDtos);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == auftragPositionenTop) {
				if (getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
					int iPos = auftragPositionenTop.getTable().getSelectedRow();

					// wenn die Position nicht die erste ist
					if (iPos > 0) {
						Integer iIdPosition = (Integer) auftragPositionenTop.getSelectedId();

						// Integer iIdPositionMinus1 = (Integer)
						// auftragPositionenTop
						// .getTable().getValueAt(iPos - 1, 0);
						//
						// DelegateFactory
						// .getInstance()
						// .getAuftragpositionDelegate()
						// .vertauscheAuftragpositionen(iIdPosition,
						// iIdPositionMinus1);

						TableModel tm = auftragPositionenTop.getTable().getModel();
						DelegateFactory.getInstance().getAuftragpositionDelegate().vertauscheAuftragpositionMinus(iPos,
								tm);

						// die Liste neu anzeigen und den richtigen Datensatz
						// markieren
						auftragPositionenTop.setSelectedId(iIdPosition);
					}
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("anf.error.positionenverschieben"));
				}
			} else

			if (e.getSource() == auftragTeilnehmerTop) {
				int iPos = auftragTeilnehmerTop.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) auftragTeilnehmerTop.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) auftragTeilnehmerTop.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getAuftragteilnehmerDelegate()
							.vertauscheAuftragteilnehmer(iIdPosition, iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					auftragTeilnehmerTop.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == auftragZahlungsplanmeilensteinTop) {
				int iPos = auftragZahlungsplanmeilensteinTop.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) auftragZahlungsplanmeilensteinTop.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) auftragZahlungsplanmeilensteinTop.getTable()
							.getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getAuftragServiceDelegate()
							.vertauscheZahlungsplanmeilenstein(iIdPosition, iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					auftragZahlungsplanmeilensteinTop.setSelectedId(iIdPosition);
				}
			}
		} else

		if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == auftragPositionenTop) {
				if (getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {
					int iPos = auftragPositionenTop.getTable().getSelectedRow();

					// wenn die Position nicht die letzte ist
					if (iPos < auftragPositionenTop.getTable().getRowCount() - 1) {
						Integer iIdPosition = (Integer) auftragPositionenTop.getSelectedId();

						// Integer iIdPositionPlus1 = (Integer)
						// auftragPositionenTop
						// .getTable().getValueAt(iPos + 1, 0);
						//
						// DelegateFactory
						// .getInstance()
						// .getAuftragpositionDelegate()
						// .vertauscheAuftragpositionen(iIdPosition,
						// iIdPositionPlus1);

						TableModel tm = auftragPositionenTop.getTable().getModel();
						DelegateFactory.getInstance().getAuftragpositionDelegate().vertauscheAuftragpositionPlus(iPos,
								tm);

						// die Liste neu anzeigen und den richtigen Datensatz
						// markieren
						auftragPositionenTop.setSelectedId(iIdPosition);
					}
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("anf.error.positionenverschieben"));
				}
			} else

			if (e.getSource() == auftragTeilnehmerTop) {
				int iPos = auftragTeilnehmerTop.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < auftragTeilnehmerTop.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) auftragTeilnehmerTop.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) auftragTeilnehmerTop.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getAuftragteilnehmerDelegate()
							.vertauscheAuftragteilnehmer(iIdPosition, iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					auftragTeilnehmerTop.setSelectedId(iIdPosition);
				}
			} else

			if (e.getSource() == auftragZahlungsplanmeilensteinTop) {
				int iPos = auftragZahlungsplanmeilensteinTop.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < auftragZahlungsplanmeilensteinTop.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) auftragZahlungsplanmeilensteinTop.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) auftragZahlungsplanmeilensteinTop.getTable()
							.getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getAuftragServiceDelegate()
							.vertauscheZahlungsplanmeilenstein(iIdPosition, iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					auftragZahlungsplanmeilensteinTop.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (e.getSource() == auftragPositionenTop) {

				auftragPositionenBottom.setArtikeSetIIdForNewPosition(
						auftragPositionenBottom.getAuftragpositionDto().getPositioniIdArtikelset());

				auftragPositionenBottom.eventActionNew(e, true, false);
				auftragPositionenBottom.eventYouAreSelected(false); // Buttons
				// schalten
			} else if (e.getSource() == auftragTeilnehmerTop) {
				auftragTeilnehmerBottom.eventActionNew(e, true, false);
				auftragTeilnehmerBottom.eventYouAreSelected(false); // Buttons
				// schalten
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			copyHV();
		}
	}

	public void dialogQueryProjektFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRProjekt = ProjektFilterFactory.getInstance().createPanelFLRProjekt(getInternalFrame());

		new DialogQuery(panelQueryFLRProjekt);
	}

	private void handleActionSave(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == auftragKopfdaten) {
			Object pkAuftrag = auftragKopfdaten.getKeyWhenDetailPanel();
			initializeDtos((Integer) pkAuftrag);
			getInternalFrame().setKeyWasForLockMe(pkAuftrag.toString());

			auftragAuswahl.clearDirektFilter();
			auftragAuswahl.eventYouAreSelected(false);
			auftragAuswahl.setSelectedId(pkAuftrag);
			auftragAuswahl.eventYouAreSelected(false);

			// wenn ein Abrufauftrag angelegt wurde und es noch keine Positionen
			// dazu gibt...
			if (getAuftragDto().getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_ABRUF)
					&& DelegateFactory.getInstance().getAuftragpositionDelegate()
							.getAnzahlMengenbehafteteAuftragpositionen(getAuftragDto().getIId()) == 0) {
				// showPanelDialogDivisor();
			}
		} else if (e.getSource() == auftragPositionenBottom) {
			// den Key des Datensatzes merken
			Object oKey = auftragPositionenBottom.getKeyWhenDetailPanel();

			// wenn der neue Datensatz wirklich angelegt wurde (Abbruch moeglich
			// durch Pruefung auf Unterpreisigkeit)
			if (!oKey.equals(LPMain.getLockMeForNew())) {
				// die Liste neu aufbauen
				auftragPositionenTop.eventYouAreSelected(false);

				// den Datensatz in der Liste selektieren
				auftragPositionenTop.setSelectedId(oKey);
			}

			// im Detail den selektierten anzeigen
			auftragPositionen.eventYouAreSelected(false);
		} else if (e.getSource() == auftragTeilnehmerBottom) {
			Object oKey = auftragTeilnehmerBottom.getKeyWhenDetailPanel();
			auftragTeilnehmerTop.eventYouAreSelected(false);
			auftragTeilnehmerTop.setSelectedId(oKey);
			auftragTeilnehmer.eventYouAreSelected(false);
		} else if (e.getSource() == panelBottomAnwesenheitsbestaetigung) {
			Object oKey = panelBottomAnwesenheitsbestaetigung.getKeyWhenDetailPanel();
			panelQueryAnwesenheitsbestaetigung.eventYouAreSelected(false);
			panelQueryAnwesenheitsbestaetigung.setSelectedId(oKey);
			panelSplitAnwesenheitsbestaetigung.eventYouAreSelected(false);
		} else if (e.getSource() == auftragKostenstelleBottom) {
			Object oKey = auftragKostenstelleBottom.getKeyWhenDetailPanel();
			auftragKostenstelleTop.eventYouAreSelected(false);
			auftragKostenstelleTop.setSelectedId(oKey);
			auftragKostenstelle.eventYouAreSelected(false);
		} else if (e.getSource() == auftragZeitplanBottom) {
			Object oKey = auftragZeitplanBottom.getKeyWhenDetailPanel();
			auftragZeitplanTop.eventYouAreSelected(false);
			auftragZeitplanTop.setSelectedId(oKey);
			auftragZeitplan.eventYouAreSelected(false);
		} else if (e.getSource() == auftragZahlungsplanBottom) {
			Object oKey = auftragZahlungsplanBottom.getKeyWhenDetailPanel();
			auftragZahlungsplanTop.eventYouAreSelected(false);
			auftragZahlungsplanTop.setSelectedId(oKey);
			auftragZahlungsplan.eventYouAreSelected(false);
		} else if (e.getSource() == auftragZahlungsplanmeilensteinBottom) {
			Object oKey = auftragZahlungsplanmeilensteinBottom.getKeyWhenDetailPanel();
			auftragZahlungsplanmeilensteinTop.eventYouAreSelected(false);
			auftragZahlungsplanmeilensteinTop.setSelectedId(oKey);
			auftragZahlungsplanmeilenstein.eventYouAreSelected(false);
		} else if (e.getSource() == auftragSichtRahmenBottom) {
			auftragSichtRahmen.eventYouAreSelected(false);

			// wenn die letzte Position uebernommen wurde, Wechsel auf
			// Positionen
			if (auftragSichtRahmenBottom.getKeyWhenDetailPanel() == null) {
				getInternalFrame().enableAllPanelsExcept(true);
				refreshAuftragPositionen();
				setSelectedComponent(auftragPositionen);
			}
		} else if (e.getSource() == panelAuftragsnrnrnBottomD) {
			Object oKey = panelAuftragsnrnrnBottomD.getKeyWhenDetailPanel();
			panelAuftragsnrnrnTopQP.eventYouAreSelected(false);
			panelAuftragsnrnrnTopQP.setSelectedId(oKey);
			panelAuftragsnrnrnSP.eventYouAreSelected(false);
		}

	}

	public void erstelleAuftragAusProjekt(Integer projektIId) throws Throwable {

		ItemChangedEvent e = new ItemChangedEvent(this, -99);

		getAuftragAuswahl().eventActionNew(e, true, false);
		auftragKopfdaten.eventYouAreSelected(false);
		// Nun noch Kunde/Ansprechpartner/Vertreter/Projekt/ProjektBezeichnung
		// setzen
		refreshAuftragKopfdaten().setDefaultsAusProjekt(projektIId);
	}

	/**
	 * Verarbeitung von ItemChangedEvent.GOTO_DETAIL_PANEL.
	 * 
	 * @param e ItemChangedEvent
	 * @throws Throwable Ausnahme
	 */
	private void handleGotoDetailPanel(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == auftragAuswahl) {
			Integer auftragIId = (Integer) auftragAuswahl.getSelectedId();
			initializeDtos(auftragIId); // befuellt den Auftrag und den Kunden
			getInternalFrame().setKeyWasForLockMe(auftragIId + "");

			if (auftragIId != null) {
				setSelectedComponent(refreshAuftragPositionen()); // auf die
				// Positionen
				// wechseln
			}
		} else if (e.getSource() == panelQueryFLRProjekt) {
			Integer projektIId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

			if (projektIId != null) {
				erstelleAuftragAusProjekt(projektIId);
			}
		} else if (e.getSource() == panelQueryFLRArtikel_handartikelumwandeln) {
			Integer artikelIId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

			if (artikelIId != null) {
				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(artikelIId);
				artikelDto = DelegateFactory.getInstance().getArtikelkommentarDelegate().pruefeArtikel(artikelDto,
						LocaleFac.BELEGART_AUFTRAG, getInternalFrame());

				if (artikelDto != null) {
					ArrayList<String> meldungen = DelegateFactory.getInstance().getArtikelDelegate()
							.wandleHandeingabeInBestehendenArtikelUm((Integer) auftragPositionenTop.getSelectedId(),
									ArtikelFac.HANDARTIKEL_UMWANDELN_AUFTRAG, artikelDto.getIId());
					auftragPositionenTop.eventYouAreSelected(false);

					for (int i = 0; i < meldungen.size(); i++) {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"), meldungen.get(i));
					}

				}
			}
		} else if (e.getSource() == panelQueryFLRZeitplantyp) {
			Integer zeitplantypIId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

			if (zeitplantypIId != null) {
				DelegateFactory.getInstance().getAuftragServiceDelegate()
						.importierteZeitplanAusZeitplantyp(zeitplantypIId, getAuftragDto().getIId());
				auftragZeitplanTop.eventYouAreSelected(false);
			}
		} else if (e.getSource() == panelQueryFLRAuftragauswahl
				|| e.getSource() == panelQueryFLRAuftragauswahlLiefertermin) {
			Integer iIdAuftragBasis = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

			if (iIdAuftragBasis != null) {

				AuftragDto auftragDtoBasis = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey(iIdAuftragBasis);

				if (auftragDtoBasis.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_ABRUF)
						&& auftragDtoBasis.getAuftragIIdRahmenauftrag() != null) {

					AuftragDto auftragDtoRahmen = DelegateFactory.getInstance().getAuftragDelegate()
							.auftragFindByPrimaryKey(auftragDtoBasis.getAuftragIIdRahmenauftrag());

					if (auftragDtoRahmen.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
						boolean bFreienAuftragAnlegen = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("auft.auftragkopieren.rahmen.erledigt"));
						if (bFreienAuftragAnlegen == false) {
							return;
						}

					}

				}

				Integer auftragIId = null;
				if (e.getSource() == panelQueryFLRAuftragauswahlLiefertermin) {
					auftragIId = DelegateFactory.getInstance().getAuftragDelegate()
							.erzeugeAuftragAusAuftragMitNeuemLiefertermin(iIdAuftragBasis, getInternalFrame());
				} else {
					auftragIId = DelegateFactory.getInstance().getAuftragDelegate()
							.erzeugeAuftragAusAuftrag(iIdAuftragBasis, getInternalFrame());

				}

				if (auftragIId != null) {
					initializeDtos(auftragIId); // befuellt den Auftrag und den
					// Kunden
					getInternalFrame().setKeyWasForLockMe(auftragIId + "");
					auftragAuswahl.setSelectedId(auftragIId);

					// wenn es bisher keinen Auftrag gegeben hat
					if (auftragAuswahl.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUFTRAGAUSWAHL, false);
					}

					setSelectedComponent(refreshAuftragPositionen()); // auf die

					// Positionen
					// wechseln
				}
			}
		} else if (e.getSource() == panelQueryFLRAngebotauswahl) {
			Integer iIdAngebotBasis = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

			if (iIdAngebotBasis != null) {

				boolean bMitZeitDaten = false;

				boolean bZeitdatenVorhanden = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.sindBelegzeitenVorhanden(LocaleFac.BELEGART_ANGEBOT, iIdAngebotBasis);

				if (bZeitdatenVorhanden == true) {
					bMitZeitDaten = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getTextRespectUISPr("auft.hint.zeitdatenvonangebot") + "?",
							LPMain.getTextRespectUISPr("lp.frage"));
				}

				// PJ19633
				AngebotpositionDto[] posDtos = DelegateFactory.getInstance().getAngebotpositionDelegate()
						.angebotpositionFindByAngebotIId(iIdAngebotBasis);

				boolean bAlternativPositionenEnthalten = false;
				ArrayList<String> agstklOhneStkl = new ArrayList();

				for (int i = 0; i < posDtos.length; i++) {
					if (posDtos[i].getBAlternative() != null
							&& Helper.short2boolean(posDtos[i].getBAlternative()) == true) {
						bAlternativPositionenEnthalten = true;
					}

					if (posDtos[i].getAgstklIId() != null) {
						AgstklDto agstklDto = DelegateFactory.getInstance().getAngebotstklDelegate()
								.agstklFindByPrimaryKey(posDtos[i].getAgstklIId());

						if (agstklDto.getStuecklisteIId() == null) {
							agstklOhneStkl.add(agstklDto.getCNr());
						}

					}

				}

				if (agstklOhneStkl.size() > 0) {
					Iterator it = agstklOhneStkl.iterator();
					String agstkls = "";
					while (it.hasNext()) {
						agstkls += it.next();
						if (it.hasNext()) {
							agstkls += ", ";
						}
					}

					boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getMessageTextRespectUISPr("angb.agstklohnestkl", agstkls));

					if (b == false) {
						return;
					}

				}

				boolean bAlternativPositionenUebernehmen = true;
				if (bAlternativPositionenEnthalten == true) {
					bAlternativPositionenUebernehmen = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getTextRespectUISPr("auf.angebot.alternativpositionen.uebernehmen"));
				}

				Integer auftragIId = DelegateFactory.getInstance().getAngebotDelegate().erzeugeAuftragAusAngebot(
						iIdAngebotBasis, bMitZeitDaten, false, bAlternativPositionenUebernehmen, getInternalFrame());

				// MB 080.05.06 IMS 1964
				auftragKopfdaten.setKeyWhenDetailPanel(auftragIId);

				initializeDtos(auftragIId); // befuellt den Auftrag und den
				// Kunden
				getInternalFrame().setKeyWasForLockMe(auftragIId + "");
				auftragAuswahl.setSelectedId(auftragIId);

				// wenn es bisher keinen Auftrag gegeben hat
				if (auftragAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUFTRAGAUSWAHL, false);
				}

				setSelectedComponent(refreshAuftragKopfdaten()); // auf die
				// Kopfdaten
				// wechseln
			}
		} else if (e.getSource() == panelQueryFLRAngebotauswahlFuerRahmenauftrag) {
			Integer iIdAngebotBasis = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

			if (iIdAngebotBasis != null) {

				boolean bMitZeitDaten = false;

				boolean bZeitdatenVorhanden = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.sindBelegzeitenVorhanden(LocaleFac.BELEGART_ANGEBOT, iIdAngebotBasis);

				if (bZeitdatenVorhanden == true) {
					bMitZeitDaten = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getTextRespectUISPr("auft.hint.zeitdatenvonangebot") + "?",
							LPMain.getTextRespectUISPr("lp.frage"));
				}

				// PJ19633
				AngebotpositionDto[] posDtos = DelegateFactory.getInstance().getAngebotpositionDelegate()
						.angebotpositionFindByAngebotIId(iIdAngebotBasis);

				boolean bAlternativPositionenEnthalten = false;

				for (int i = 0; i < posDtos.length; i++) {
					if (posDtos[i].getBAlternative() != null
							&& Helper.short2boolean(posDtos[i].getBAlternative()) == true) {
						bAlternativPositionenEnthalten = true;
					}
				}

				boolean bAlternativPositionenUebernehmen = true;
				if (bAlternativPositionenEnthalten == true) {
					bAlternativPositionenUebernehmen = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getTextRespectUISPr("auf.angebot.alternativpositionen.uebernehmen"));
				}

				Integer auftragIId = DelegateFactory.getInstance().getAngebotDelegate().erzeugeAuftragAusAngebot(
						iIdAngebotBasis, bMitZeitDaten, true, bAlternativPositionenUebernehmen, getInternalFrame());
				auftragKopfdaten.setKeyWhenDetailPanel(auftragIId);

				initializeDtos(auftragIId); // befuellt den Auftrag und den
				// Kunden
				getInternalFrame().setKeyWasForLockMe(auftragIId + "");
				auftragAuswahl.setSelectedId(auftragIId);

				// wenn es bisher keinen Auftrag gegeben hat
				if (auftragAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUFTRAGAUSWAHL, false);
				}

				setSelectedComponent(refreshAuftragKopfdaten()); // auf die
				// Kopfdaten
				// wechseln
			}
		} else if (e.getSource() == panelQueryFLRBegruendung) {

			if (e.getSource() == panelQueryFLRBegruendung) {
				Integer key = (Integer) panelQueryFLRBegruendung.getSelectedId();
				DelegateFactory.getInstance().getAuftragDelegate().updateAuftragBegruendung(getAuftragDto().getIId(),
						key);
				if (auftragKonditionen != null) {
					auftragKonditionen.eventYouAreSelected(false);
				}
			}
		} else if (e.getSource() == panelQueryFLRKundeFuerVATImport) {
			Integer iIdKunde = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

			DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(iIdKunde, LocaleFac.BELEGART_AUFTRAG,
					getInternalFrame());

			KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(iIdKunde);

			panelQueryFLRAnsprechpartnerFuerVATImport = PartnerFilterFactory.getInstance()
					.createPanelFLRAnsprechpartner(getInternalFrame(), kdDto.getPartnerDto().getIId(), null, true,
							true);

			new DialogQuery(panelQueryFLRAnsprechpartnerFuerVATImport);

		} else if (e.getSource() == panelQueryFLRAnsprechpartnerFuerVATImport) {
			Integer ansprechpartnerIId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

			importXLSX_VAT((Integer) panelQueryFLRKundeFuerVATImport.getSelectedId(), ansprechpartnerIId);

		}

	}

	/**
	 * Ein ItemChangedEvent mit code ITEM_CHANGED ist eingelangt.
	 * 
	 * @param e das ItemChangedEvent
	 * @throws Throwable
	 */
	private void handleItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == auftragAuswahl) {
			Integer iIdAuftrag = (Integer) auftragAuswahl.getSelectedId();
			initializeDtos(iIdAuftrag);
			// updateMenuItemDateiAuftragVersand() ;

			setKeyWasForLockMe();
			refreshAuftragKopfdaten().setKeyWhenDetailPanel(iIdAuftrag);

			setTitleAuftrag(LPMain.getTextRespectUISPr("auft.title.panel.auswahl"));

			getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUFTRAGAUSWAHL, iIdAuftrag != null);
		} else if (e.getSource() == auftragPositionenTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			auftragPositionenBottom.setKeyWhenDetailPanel(key);
			auftragPositionenBottom.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			auftragPositionenTop.updateButtons(auftragPositionenBottom.getLockedstateDetailMainKey());

			LPButtonAction item = (LPButtonAction) auftragPositionenTop.getHmOfButtons().get(MY_OWN_NEW_TOGGLE_GESEHEN);
			item.getButton().setEnabled(true);

			LPButtonAction item2 = (LPButtonAction) auftragPositionenTop.getHmOfButtons()
					.get(MY_OWN_NEW_TOGGLE_HVMAUEBERTRAGEN);
			item2.getButton().setEnabled(true);

		} else if (e.getSource() == auftragTeilnehmerTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			auftragTeilnehmerBottom.setKeyWhenDetailPanel(key);
			auftragTeilnehmerBottom.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			auftragTeilnehmerTop.updateButtons();
		} else if (e.getSource() == panelQueryAnwesenheitsbestaetigung) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			panelBottomAnwesenheitsbestaetigung.setKeyWhenDetailPanel(key);
			panelBottomAnwesenheitsbestaetigung.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			panelQueryAnwesenheitsbestaetigung.updateButtons();
		} else if (e.getSource() == auftragKostenstelleTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			auftragKostenstelleBottom.setKeyWhenDetailPanel(key);
			auftragKostenstelleBottom.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			auftragKostenstelleTop.updateButtons();
		} else if (e.getSource() == auftragZeitplanTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			auftragZeitplanBottom.setKeyWhenDetailPanel(key);
			auftragZeitplanBottom.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			auftragZeitplanTop.updateButtons();
		} else if (e.getSource() == auftragZahlungsplanTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			auftragZahlungsplanBottom.setKeyWhenDetailPanel(key);
			auftragZahlungsplanBottom.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			auftragZahlungsplanTop.updateButtons();
		} else if (e.getSource() == auftragZahlungsplanmeilensteinTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			auftragZahlungsplanmeilensteinBottom.setKeyWhenDetailPanel(key);
			auftragZahlungsplanmeilensteinBottom.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			auftragZahlungsplanmeilensteinTop.updateButtons();
		} else if (e.getSource() == auftragSichtRahmenTop) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			auftragSichtRahmenBottom.setKeyWhenDetailPanel(key);
			auftragSichtRahmenBottom.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			auftragSichtRahmenTop.updateButtons();
		} else if (e.getSource() == panelAuftragsnrnrnTopQP) {
			Integer iId = (Integer) panelAuftragsnrnrnTopQP.getSelectedId();
			// getInternalFrame().setKeyWasForLockMe(iId + "");
			panelAuftragsnrnrnBottomD.setKeyWhenDetailPanel(iId);
			panelAuftragsnrnrnBottomD.eventYouAreSelected(false);

			// im QP die Buttons in den Zustand nolocking/save setzen.
			panelAuftragsnrnrnTopQP.updateButtons(panelAuftragsnrnrnBottomD.getLockedstateDetailMainKey());
		} else if (e.getSource() == auftragSichtLieferstatus) {
			Integer iId = (Integer) auftragSichtLieferstatus.getSelectedId();

			refreshSichtLieferstatusUnten();

			sichtAuftragAufAndereBelegartenTop.updateButtons();
		} else if (e.getSource() == sichtAuftragAufAndereBelegartenTop) {
			Integer iId = (Integer) sichtAuftragAufAndereBelegartenTop.getSelectedId();

			aktualisiereGotoButtons();
		} else if (e.getSource() == auftragBestellungen) {
			myLogger.info("Auftrag-Bestellung refreshed?");
			refreshBestellungWert();
			myLogger.info("Auftrag-BestellungWert aktualisiert");
		}
	}

	@Override
	protected void initDtos() throws Throwable {
		initializeDtos(auftragDto.getIId());
	}

	public void initializeDtos(Integer iIdAuftragI) throws Throwable {
		if (iIdAuftragI != null) {
			auftragDto = DelegateFactory.getInstance().getAuftragDelegate().auftragFindByPrimaryKey(iIdAuftragI);

			kundeAuftragDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse());

			// belegartkonditionen: 2a Kopftext und Fusstext hinterlegen
			if (auftragDto.getAuftragIIdKopftext() == null && auftragDto.getAuftragIIdFusstext() == null) {
				initAuftragtexte();
			}

			if (auftragDto.getAuftragIIdKopftext() != null) {
				kopftextDto = DelegateFactory.getInstance().getAuftragServiceDelegate()
						.auftragtextFindByPrimaryKey(auftragDto.getAuftragIIdKopftext());
			}

			if (auftragDto.getAuftragIIdFusstext() != null) {
				fusstextDto = DelegateFactory.getInstance().getAuftragServiceDelegate()
						.auftragtextFindByPrimaryKey(auftragDto.getAuftragIIdFusstext());
			}
		}
	}

	private PanelSplit refreshAuftragSichtRahmen() throws Throwable {
		QueryType[] qtPos = null;
		FilterKriterium[] filtersPos = null;

		if (getAuftragDto() != null) {
			if (getAuftragDto().getAuftragartCNr() != null
					&& getAuftragDto().getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {
				filtersPos = AuftragFilterFactory.getInstance()
						.createFKAuftragpositionSichtRahmen(getAuftragDto().getIId());
			} else if (getAuftragDto().getAuftragartCNr() != null
					&& getAuftragDto().getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
				filtersPos = AuftragFilterFactory.getInstance()
						.createFKAuftragpositionSichtRahmenAusAbruf(getAuftragDto().getAuftragIIdRahmenauftrag());
			}
		}

		if (auftragSichtRahmen == null) {
			auftragSichtRahmenBottom = new PanelAuftragpositionSichtRahmen(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.sichtrahmen"), null);

			auftragSichtRahmenTop = new PanelQuery(qtPos, filtersPos, QueryParameters.UC_ID_AUFTRAGPOSITIONSICHTRAHMEN,
					null, getInternalFrame(), LPMain.getTextRespectUISPr("lp.sichtrahmen"), true);

			auftragSichtRahmenTop.addDirektFilter(new FilterKriteriumDirekt("flrartikel.c_nr", "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("artikel.artikelnummer"),
					FilterKriteriumDirekt.PROZENT_BOTH, true, true, Facade.MAX_UNBESCHRAENKT));

			auftragSichtRahmenTop.addDirektFilter(new FilterKriteriumDirekt("textsuche", "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("label.text"),
					FilterKriteriumDirekt.PROZENT_BOTH, true, true, Facade.MAX_UNBESCHRAENKT));

			auftragSichtRahmen = new PanelSplit(getInternalFrame(), auftragSichtRahmenBottom, auftragSichtRahmenTop,
					160);

			setComponentAt(IDX_PANEL_SICHTRAHMEN, auftragSichtRahmen);
		}

		auftragSichtRahmenTop.setDefaultFilter(filtersPos);

		return auftragSichtRahmen;
	}

	private PanelSplit refreshAuftragPositionen() throws Throwable {
		QueryType[] qtPositionen = null;
		FilterKriterium[] filtersPositionen = AuftragFilterFactory.getInstance()
				.createFKFlrauftragiid(auftragDto.getIId());

		if (auftragPositionen == null) {

			auftragPositionenBottom = new PanelAuftragPositionen2(getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.positionen"), null); // emptytable:
			// 2
			// eventuell
			// gibt es
			// noch
			// keine
			// position

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1, PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					PanelBasis.ACTION_KOPIEREN, PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };

			auftragPositionenTop = new PanelQuery(qtPositionen, filtersPositionen,
					QueryParameters.UC_ID_AUFTRAGPOSITION, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.positionen"), true); // flag,
			// damit flr
			// erst beim
			// aufruf
			// des
			// panels
			// loslaeuft

			auftragPositionenTop.createAndSaveAndShowButton("/com/lp/client/res/selection_replace.png",
					LPMain.getTextRespectUISPr("auft.lieferscheinpreise.update"),
					MY_OWN_NEW_LIEFERSCHEIN_PREISE_UPDATEN, RechteFac.RECHT_AUFT_AUFTRAG_CUD);

			auftragPositionenTop.createAndSaveAndShowButton("/com/lp/client/res/document_into.png",
					LPMain.getTextRespectUISPr("stkl.positionen.cvsimport"), BUTTON_IMPORTCSV_AUFTRAGPOSITIONEN, null);

			auftragPositionenTop.createAndSaveAndShowButton("/com/lp/client/res/scanner16x16.png",
					LPMain.getTextRespectUISPr("auftrag.positionen.schnelleingabe"),
					BUTTON_SCHNELLERFASSUNG_AUFTRAGPOSITIONEN, RechteFac.RECHT_AUFT_AUFTRAG_CUD);

			auftragPositionenTop.createAndSaveAndShowButton("/com/lp/client/res/sort_az_descending.png",
					LPMain.getTextRespectUISPr("ls.sortierenachartikelnummer"), BUTTON_SORTIERE_NACH_ARTIKELNUMMER,
					null);

			auftragPositionenTop.createAndSaveAndShowButton("/com/lp/client/res/calculator16x16.png",
					LPMain.getTextRespectUISPr("auft.preise.neuberechnen"), ACTION_SPECIAL_PREISE_NEU_KALKULIEREN,
					RechteFac.RECHT_AUFT_AUFTRAG_CUD);

			auftragPositionenTop.createAndSaveAndShowButton("/com/lp/client/res/data_ok.png",
					LPMain.getTextRespectUISPr("auft.positionen.toggle.gesehen"), MY_OWN_NEW_TOGGLE_GESEHEN,
					RechteFac.RECHT_AUFT_AUFTRAG_CUD);

			auftragPositionenTop.createAndSaveAndShowButton("/com/lp/client/res/document_ok.png",
					LPMain.getTextRespectUISPr("auft.positionen.toggle.hvma"), MY_OWN_NEW_TOGGLE_HVMAUEBERTRAGEN,
					RechteFac.RECHT_AUFT_AUFTRAG_CUD);

			auftragPositionen = new PanelSplit(getInternalFrame(), auftragPositionenBottom, auftragPositionenTop, 160);

			auftragPositionenTop.setMultipleRowSelectionEnabled(true);

			setComponentAt(IDX_PANEL_AUFTRAGPOSITIONEN, auftragPositionen);
		}

		auftragPositionenTop.setDefaultFilter(filtersPositionen);

		return auftragPositionen;
	}

	private PanelAuftragKopfdaten refreshAuftragKopfdaten() throws Throwable {
		Integer iIdAuftrag = null;

		if (auftragKopfdaten == null) {
			// typkeyfordetail: Der Auftrag hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdAuftrag = new Integer(Integer.parseInt(getInternalFrame().getKeyWasForLockMe()));
			}

			auftragKopfdaten = new PanelAuftragKopfdaten(getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.kopfdaten"), iIdAuftrag); // empty
			// bei
			// leerer
			// Liste

			setComponentAt(IDX_PANEL_AUFTRAGKOPFDATEN, auftragKopfdaten);
		}

		return auftragKopfdaten;
	}

	private PanelAuftragKonditionen2 refreshAuftragKonditionen() throws Throwable {
		if (auftragKonditionen == null) {

			auftragKonditionen = new PanelAuftragKonditionen2(getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.konditionen"), auftragDto.getIId());

			setComponentAt(IDX_PANEL_AUFTRAGKONDITIONEN, auftragKonditionen);
		}

		return auftragKonditionen;
	}

	private void refreshSichtLieferstatus() throws Throwable {
		QueryType[] qtPositionen = null;
		FilterKriterium[] filtersPositionen = AuftragFilterFactory.getInstance()
				.createFKFlrauftragiid(auftragDto.getIId());

		if (auftragSichtLieferstatus == null) {

			auftragSichtLieferstatus = new PanelQuery(qtPositionen, filtersPositionen,
					QueryParameters.UC_ID_SICHTLIEFERSTATUS, null, getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.sichtlieferstatus"), true);

			auftragSichtLieferstatus.eventYouAreSelected(false);
		}

		auftragSichtLieferstatus.setDefaultFilter(filtersPositionen);

		refreshSichtLieferstatusUnten();

		lieferstatus = new PanelSplit2(getInternalFrame(), auftragSichtLieferstatus, sichtAuftragAufAndereBelegartenTop,
				280);

		setComponentAt(IDX_PANEL_SICHTLIEFERSTATUS, lieferstatus);

	}

	private void refreshAuftragUebersicht() throws Throwable {
		if (ptAuftragUebersicht == null) {

			ptAuftragUebersicht = new PanelTabelleAuftragUebersicht(QueryParameters.UC_ID_AUFTRAGUEBERSICHT,
					LPMain.getTextRespectUISPr("lp.umsatzuebersicht"), getInternalFrame());
			setComponentAt(IDX_PANEL_AUFTRAGUEBERSICHT, ptAuftragUebersicht);
		}
	}

	private void refreshAuftragzeiten() throws Throwable {
		if (ptAuftragzeiten == null) {

			ptAuftragzeiten = new PanelTabelleAuftragzeiten(QueryParameters.UC_ID_AUFTRAGZEITEN,
					LPMain.getTextRespectUISPr("auft.title.panel.auftragzeiten"), getInternalFrame());

			// default Kriterium vorbelegen
			FilterKriterium[] aFilterKrit = new FilterKriterium[2];

			FilterKriterium krit1 = new FilterKriterium(AuftragFac.KRIT_PERSONAL, true, "true",
					FilterKriterium.OPERATOR_EQUAL, false);

			FilterKriterium krit2 = new FilterKriterium(AuftragFac.KRIT_AUFTRAG_I_ID, true,
					auftragDto.getIId().toString(), FilterKriterium.OPERATOR_EQUAL, false);

			aFilterKrit[0] = krit1;
			aFilterKrit[1] = krit2;

			ptAuftragzeiten.setDefaultFilter(aFilterKrit);

			setComponentAt(IDX_PANEL_AUFTRAGZEITEN, ptAuftragzeiten);
		}
	}

	public PanelSplit refreshPanelAuftragsrnnrn(Integer iIdPosition) throws Throwable {
		String[] aWhichStandardButtonIUse = null;
		FilterKriterium[] filtersPositionen = AuftragFilterFactory.getInstance()
				.createFKAuftragpositioniid(iIdPosition);

		panelAuftragsnrnrnTopQP = new PanelQuery(null, filtersPositionen, QueryParameters.UC_ID_AUFTRAGSERIENNRN,
				aWhichStandardButtonIUse, getInternalFrame(),
				LPMain.getTextRespectUISPr("artikel.report.seriennummern"), true);

		panelAuftragsnrnrnBottomD = new PanelStammdatenCRUD(getInternalFrame(),
				LPMain.getTextRespectUISPr("artikel.report.seriennummern"), null,
				HelperClient.SCRUD_AUFTRAGSERIENNUMMER_FILE, getInternalFrameAuftrag(),
				HelperClient.LOCKME_AUFTRAGSERIENNUMMERN);

		panelAuftragsnrnrnSP = new PanelSplit(getInternalFrame(), panelAuftragsnrnrnBottomD, panelAuftragsnrnrnTopQP,
				400 - ((PanelStammdatenCRUD) panelAuftragsnrnrnBottomD).getAnzahlControls() * 30);

		// }
		panelAuftragsnrnrnTopQP.eventYouAreSelected(false);
		return panelAuftragsnrnrnSP;
	}

	private PanelSplit refreshAuftragTeilnehmer() throws Throwable {
		QueryType[] qtTeilnehmer = null;
		FilterKriterium[] filtersTeilnehmer = AuftragFilterFactory.getInstance()
				.createFKFlrauftragiid(auftragDto.getIId());

		if (auftragTeilnehmer == null) {

			auftragTeilnehmerBottom = new PanelAuftragTeilnehmer(getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.teilnehmer"), null); // eventuell
			// gibt es
			// noch
			// keinen
			// Teilnehmer

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1, PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					PanelBasis.ACTION_KOPIEREN, PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };

			auftragTeilnehmerTop = new PanelQuery(qtTeilnehmer, filtersTeilnehmer,
					QueryParameters.UC_ID_AUFTRAGTEILNEHMER, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.teilnehmer"), true);

			// pqaddbutton: 1 Hier den zusaetzlichen Button aufs Panel bringen
			auftragTeilnehmerTop.createAndSaveAndShowButton("/com/lp/client/res/new_from_externer_teilnehmer.png",
					LPMain.getTextRespectUISPr("auft.tooltip.externerteilnehmer"), MY_OWN_NEW_NEU_EXTERNER_TEILNEHMER,
					RechteFac.RECHT_AUFT_AUFTRAG_CUD);

			auftragTeilnehmer = new PanelSplit(getInternalFrame(), auftragTeilnehmerBottom, auftragTeilnehmerTop, 200);
			auftragTeilnehmerTop.setMultipleRowSelectionEnabled(true);

			setComponentAt(IDX_PANEL_AUFTRAGTEILNEHMER, auftragTeilnehmer);
		}

		auftragTeilnehmerTop.setDefaultFilter(filtersTeilnehmer);

		return auftragTeilnehmer;
	}

	private PanelSplit refreshZeitbestaetigung() throws Throwable {
		QueryType[] qtTeilnehmer = null;
		FilterKriterium[] filtersTeilnehmer = AuftragFilterFactory.getInstance()
				.createFKFlrauftragiid(auftragDto.getIId());

		if (panelSplitAnwesenheitsbestaetigung == null) {

			panelBottomAnwesenheitsbestaetigung = new PanelAnwesenheitsbestaetigung(getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.title.tab.anwesenheitsbestaetigung"), null); // eventuell
			// gibt es
			// noch
			// keinen
			// Teilnehmer

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryAnwesenheitsbestaetigung = new PanelQuery(qtTeilnehmer, filtersTeilnehmer,
					QueryParameters.UC_ID_ANWESENHEITSBESTAETIGUNG, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.title.tab.anwesenheitsbestaetigung"), true);

			panelSplitAnwesenheitsbestaetigung = new PanelSplit(getInternalFrame(), panelBottomAnwesenheitsbestaetigung,
					panelQueryAnwesenheitsbestaetigung, 200);

			setComponentAt(IDX_PANEL_ZEITBESTAETIGUNG, panelSplitAnwesenheitsbestaetigung);
		}

		panelQueryAnwesenheitsbestaetigung.setDefaultFilter(filtersTeilnehmer);

		return panelSplitAnwesenheitsbestaetigung;
	}

	private PanelSplit refreshAuftragKostenstelle() throws Throwable {
		QueryType[] qtTeilnehmer = null;
		FilterKriterium[] filters = AuftragFilterFactory.getInstance().createFKFlrauftragiid(auftragDto.getIId());

		if (auftragKostenstelle == null) {

			auftragKostenstelleBottom = new PanelAuftragKostenstelle(getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.kostenstellen"), null); // eventuell
			// gibt es
			// noch
			// keinen
			// Teilnehmer

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			auftragKostenstelleTop = new PanelQuery(qtTeilnehmer, filters, QueryParameters.UC_ID_AUFTRAGKOSTENSTELLE,
					aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("auft.kostenstellen"), true);

			auftragKostenstelle = new PanelSplit(getInternalFrame(), auftragKostenstelleBottom, auftragKostenstelleTop,
					200);

			setComponentAt(IDX_PANEL_AUFTRAGKOSTENSTELLEN, auftragKostenstelle);
		}

		auftragKostenstelleTop.setDefaultFilter(filters);

		return auftragKostenstelle;
	}

	private PanelSplit refreshZeitplan() throws Throwable {

		FilterKriterium[] filtersTeilnehmer = AuftragFilterFactory.getInstance()
				.createFKFlrauftragiid(auftragDto.getIId());

		if (auftragZeitplan == null) {

			auftragZeitplanBottom = new PanelZeitplan(getInternalFrame(), LPMain.getTextRespectUISPr("auft.zeitplan"),
					null); // eventuell

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			auftragZeitplanTop = new PanelQuery(null, filtersTeilnehmer, QueryParameters.UC_ID_ZEITPLAN,
					aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("auft.zeitplan"), true);

			auftragZeitplanTop.createAndSaveAndShowButton("/com/lp/client/res/document_into.png",
					LPMain.getTextRespectUISPr("auft.zeitplan.importieren"), MY_OWN_NEW_EXTRA_ZEITPLAN_IMPORTIEREN,
					RechteFac.RECHT_AUFT_AUFTRAG_CUD);

			auftragZeitplan = new PanelSplit(getInternalFrame(), auftragZeitplanBottom, auftragZeitplanTop, 200);

			setComponentAt(IDX_PANEL_ZEITPLAN, auftragZeitplan);
		}

		auftragZeitplanTop.setDefaultFilter(filtersTeilnehmer);

		return auftragZeitplan;
	}

	private PanelSplit refreshZahlungsplan() throws Throwable {
		FilterKriterium[] filtersTeilnehmer = AuftragFilterFactory.getInstance()
				.createFKFlrauftragiid(auftragDto.getIId());

		if (auftragZahlungsplan == null) {

			auftragZahlungsplanBottom = new PanelZahlungsplan(getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.zahlungsplan"), null); // eventuell

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			auftragZahlungsplanTop = new PanelQuery(null, filtersTeilnehmer, QueryParameters.UC_ID_ZAHLUNGSPLAN,
					aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("auft.zahlungsplan"), true);

			auftragZahlungsplanTop.getToolBar().getToolsPanelCenter().add(auftragsSumme);

			auftragZahlungsplan = new PanelSplit(getInternalFrame(), auftragZahlungsplanBottom, auftragZahlungsplanTop,
					170);

			setComponentAt(IDX_PANEL_ZAHLUNGSPLAN, auftragZahlungsplan);
		}

		auftragZahlungsplanTop.setDefaultFilter(filtersTeilnehmer);

		// PJ19104
		BigDecimal bdAuftragwertInMandantenwaehrung = getAuftragDto().getNGesamtauftragswertInAuftragswaehrung();

		if (bdAuftragwertInMandantenwaehrung == null
				|| getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
			bdAuftragwertInMandantenwaehrung = DelegateFactory.getInstance().getAuftragDelegate()
					.berechneGesamtwertAuftrag(getAuftragDto().getIId());

			if (getAuftragDto().getNKorrekturbetrag() != null) {
				bdAuftragwertInMandantenwaehrung = bdAuftragwertInMandantenwaehrung
						.add(getAuftragDto().getNKorrekturbetrag());
			}

		}

		bdAuftragwertInMandantenwaehrung = DelegateFactory.getInstance().getLocaleDelegate()
				.rechneUmInAndereWaehrungGerundetZuDatum(bdAuftragwertInMandantenwaehrung,
						getAuftragDto().getCAuftragswaehrung(), LPMain.getTheClient().getSMandantenwaehrung(),
						new java.sql.Date(getAuftragDto().getDLiefertermin().getTime()));

		auftragsSumme.setText(LPMain.getTextRespectUISPr("kond.label.auftragswert") + " = "
				+ Helper.formatZahl(bdAuftragwertInMandantenwaehrung, 2, LPMain.getTheClient().getLocUi()) + " "
				+ LPMain.getTheClient().getSMandantenwaehrung());

		return auftragZahlungsplan;
	}

	private PanelSplit refreshZahlungsplanmeilenstein() throws Throwable {
		FilterKriterium[] filtersTeilnehmer = AuftragFilterFactory.getInstance()
				.createFKZahlungsplanmeilenstein((Integer) auftragZahlungsplanTop.getSelectedId());

		if (auftragZahlungsplanmeilenstein == null) {

			auftragZahlungsplanmeilensteinBottom = new PanelZahlungsplanmeilenstein(getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.zahlungsplanmeilenstein"), null); // eventuell

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			auftragZahlungsplanmeilensteinTop = new PanelQuery(null, filtersTeilnehmer,
					QueryParameters.UC_ID_ZAHLUNGSPLANMEILENSTEIN, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.zahlungsplanmeilenstein"), true);

			auftragZahlungsplanmeilenstein = new PanelSplit(getInternalFrame(), auftragZahlungsplanmeilensteinBottom,
					auftragZahlungsplanmeilensteinTop, 200);

			setComponentAt(IDX_PANEL_ZAHLUNGSPLANMEILENSTEIN, auftragZahlungsplanmeilenstein);
		}

		auftragZahlungsplanmeilensteinTop.setDefaultFilter(filtersTeilnehmer);

		return auftragZahlungsplanmeilenstein;
	}

	private void refreshEigenschaften(Integer key) throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD, };

		panelDetailAuftragseigenschaft = new PanelDynamisch(getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.eigenschaften"), auftragAuswahl, PanelFac.PANEL_AUFTRAGSEIGENSCHAFTEN,
				HelperClient.LOCKME_ARTIKEL, aWhichButtonIUse);
		setComponentAt(IDX_PANEL_AUFTRAGEIGENSCHAFTEN, panelDetailAuftragseigenschaft);

	}

	/**
	 * Den Text der Titelleiste ueberschreiben.
	 * 
	 * @param panelTitle der Title des aktuellen panel
	 * @throws Exception
	 */
	public void setTitleAuftrag(String panelTitle) throws Throwable {
		// aktuelle auftragnummer bestimmen
		StringBuffer auftragnummer = new StringBuffer("");
		if (auftragDto == null || auftragDto.getIId() == null) {
			auftragnummer.append(LPMain.getTextRespectUISPr("auft.title.neuerauftrag"));
		} else {

			KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse());

			auftragnummer.append(LPMain.getTextRespectUISPr("auft.auftrag")).append(" ").append(auftragDto.getCNr())
					.append(" ").append(kundeDto.getPartnerDto().formatFixTitelName1Name2());

		}

		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, panelTitle);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, auftragnummer.toString());
	}

	public void setTitleAuftragOhneAuftragnummer(String panelTitle) throws Exception {
		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, panelTitle);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
	}

	public void resetDtos() {
		auftragDto = new AuftragDto();
		kundeAuftragDto = new KundeDto();
		kopftextDto = new AuftragtextDto();
		fusstextDto = new AuftragtextDto();
	}

	public boolean pruefeAktuellenAuftrag() {
		boolean bIstGueltig = true;

		if (auftragDto == null || auftragDto.getIId() == null) {
			bIstGueltig = false;
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("auft.warning.keinauftrag"));
		}

		return bIstGueltig;
	}

	public boolean aktuellerAuftragHatPositionen() throws Throwable {
		boolean bHatPositionen = true;

		if (DelegateFactory.getInstance().getAuftragpositionDelegate()
				.getAnzahlMengenbehafteteAuftragpositionen(auftragDto.getIId()) <= 0) {
			bHatPositionen = false;
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("auft.warning.keinepositionen"));
		}

		return bHatPositionen;
	}

	protected boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;
		int code = exfc.getICode();

		switch (code) {
		case EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT:
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("auft.mandant.hauptlager_fehlt"));
			break;

		default:
			bErrorErkannt = false;
			break;
		}

		return bErrorErkannt;
	}

	/**
	 * Pruefen, ob zu dem aktuellen Auftrag Artikelpositionen erfasst sind.
	 * 
	 * @return boolean true, wenn es Artikelpositionen zu diesem Auftrag gibt
	 * @throws java.lang.Throwable
	 */
	public boolean aktuellerAuftragHatArtikelpositionen() throws Throwable {
		boolean bHatArtikelpositionen = true;

		if (DelegateFactory.getInstance().getAuftragpositionDelegate()
				.berechneAnzahlArtikelpositionenMitStatus(auftragDto.getIId(), null) <= 0) {
			bHatArtikelpositionen = false;
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("auft.warning.keineartikelpositionen"));
		}

		return bHatArtikelpositionen;
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_ACTION_DATEI_NEU_AUS_AUFTRAG)) {
			// der Benutzer muss einen Auftrag auswaehlen
			dialogQueryAuftragFromListe(null);
		} else if (e.getActionCommand().equals(MENU_ACTION_DATEI_AUFTRAGBESTAETIGUNG)) {
			printAuftragbestaetigung();
		} else if (e.getActionCommand().equals(MENU_ACTION_DATEI_PACKLISTE)) {
			printPackliste();
		} else if (e.getActionCommand().equals(MENU_ACTION_DATEI_KOMMISSIONIERUNG)) {
			printKommissionierung();
		} else if (e.getActionCommand().equals(MENU_ACTION_DATEI_ETIKETT)) {
			printEtikett();
		} else if (e.getActionCommand().equals(GOTO_LOS)) {
			int z = 0;
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_CSVIMPORT_SON)) {
			importCSV_SON();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_CSVIMPORT_WOOCOMMERCE)) {
			importCSV_WooCommerce();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_XSLXIMPORT_VAT)) {

			panelQueryFLRKundeFuerVATImport = PartnerFilterFactory.getInstance().createPanelFLRKunde(getInternalFrame(),
					true, false, null);
			new DialogQuery(panelQueryFLRKundeFuerVATImport);

		} else if (e.getActionCommand().equals(MENU_ACTION_INFO_AUFTRAGZEITEN)) {
			if (pruefeAktuellenAuftrag()) {
				getKriterienAuftragzeiten();
				getInternalFrame().showPanelDialog(pdKriterienAuftragzeiten);
				pdKriterienAuftragzeitenUeberMenueAufgerufen = true;
			}
		} else if (e.getActionCommand().equals(MENU_ACTION_INFO_ZEITBESTAETIGUNG)) {
			if (pruefeAktuellenAuftrag()) {
				getInternalFrame().showReportKriterien(
						new ReportZeitbestaetigung(getInternalFrame(), getAuftragDto().getIId(), getTitleDruck()),
						getKundeAuftragDto().getPartnerDto(), getAuftragDto().getAnsprechparnterIId(), false);
			}
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_PROJEKT_ANLEGEN)) {
			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PROJEKT)) {

				if (getAuftragDto().getProjektIId() != null) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("auftrag.bearbeiten.projekterzeugen.projektbereitszugeordnet"));
				}

				InternalFrameProjekt ifPJ = (InternalFrameProjekt) LPMain.getInstance().getDesktop()
						.holeModul(LocaleFac.BELEGART_PROJEKT);
				ifPJ.getTabbedPaneProjekt().erstelleProjektAusAuftrag(getAuftragDto().getIId());
			}
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_BEGRUENDUNG)) {

			panelQueryFLRBegruendung = AuftragFilterFactory.getInstance().createPanelFLRBegruendung(getInternalFrame(),
					null, true);
			new DialogQuery(panelQueryFLRBegruendung);

		} else if (e.getActionCommand().equals(MENU_ACTION_INFO_NACHKALKULATION)) {
			if (auftragDto != null && auftragDto.getIId() != null) {
				if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
					showStatusMessage("lp.warning", "auft.storno.wurdestorniert");
				} else {
					printNachkalkulation();
				}
			}
		} else if (e.getActionCommand().equals(MENU_ACTION_INFO_AUFTRAGSETIKETT)) {
			if (auftragDto != null && auftragDto.getIId() != null) {
				getInternalFrame().showReportKriterien(
						new ReportAuftragsetikett(getInternalFrame(), getAuftragDto().getIId(), getTitleDruck()),
						getKundeAuftragDto().getPartnerDto(), getAuftragDto().getAnsprechparnterIId(), false);
			}
		}

		else if (e.getActionCommand().equals(MENU_BEARBEITEN_MANUELL_ERLEDIGEN)) {
			if (pruefeAktuellenAuftrag()) {
				if (!refreshAuftragKopfdaten().isLockedDlg()) {

					// Statuswechsel 'Offen' -> 'Erledigt' : Ausgeloest durch
					// Menue
					if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
							|| auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)) {
						if (DialogFactory.showMeldung(LPMain.getTextRespectUISPr("auft.auftragstatus.erledigtsetzen"),
								LPMain.getTextRespectUISPr("lp.hint"),
								javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {

							// PJ19165 vorher pruefen ob Anzahlung bzw.
							// Schlussrechnung vorhanden
							boolean bAnzahlungenVorhanden = false;
							boolean bSchlussrechnungVorhanden = false;
							RechnungDto[] reDtos = DelegateFactory.getInstance().getRechnungDelegate()
									.rechnungFindByAuftragIId(auftragDto.getIId());
							for (int i = 0; i < reDtos.length; i++) {
								if (!reDtos[i].getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {

									if (reDtos[i].getRechnungartCNr().equals(RechnungFac.RECHNUNGART_ANZAHLUNG)) {
										bAnzahlungenVorhanden = true;
									}
									if (reDtos[i].getRechnungartCNr().equals(RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG)) {
										bSchlussrechnungVorhanden = true;
									}

								}
							}

							if (bAnzahlungenVorhanden == true && bSchlussrechnungVorhanden == false) {

								if (DelegateFactory.getInstance().getTheJudgeDelegate()
										.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER)) {

									boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
											LPMain.getTextRespectUISPr(
													"auft.manuellerledigen.error.anzahlungohneschlussrechnung.trotzdem"));

									if (b == true) {

										DelegateFactory.getInstance().getAuftragDelegate()
												.manuellErledigen(auftragDto.getIId());
									}

								} else {
									DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getTextRespectUISPr(
													"auft.manuellerledigen.error.anzahlungohneschlussrechnung"));
								}

							} else {
								DelegateFactory.getInstance().getAuftragDelegate()
										.manuellErledigen(auftragDto.getIId());
							}

							refreshAktuellesPanel();
						}
					} else {
						showStatusMessage("lp.warning", "auft.warning.auftragkannnichterledigtwerden");
					}
				}
			}
		}

		else if (e.getActionCommand().equals(MENUE_ACTION_BEARBEITEN_TERMINVERSCHIEBEN)) {
			if (auftragDto != null && auftragDto.getIId() != null) {

				// SP4966
				if (auftragDto.getBestellungIIdAndererMandant() != null) {

					BestellungDto bsDto = DelegateFactory.getInstance().getBestellungDelegate()
							.bestellungFindByPrimaryKey(auftragDto.getBestellungIIdAndererMandant());

					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("auft.error.besinanderemmandanten.nurmehrmengeaenderbar"));
					Object[] pattern = new Object[] { bsDto.getCNr(), bsDto.getMandantCNr() };

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"), mf.format(pattern));
					return;

				}

				refreshAuftragKopfdaten();
				getPanelKopfdaten().eventActionLock(null);
				DialogTerminverschiebenAuftrag d = new DialogTerminverschiebenAuftrag(this);
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);
				getPanelKopfdaten().eventActionUnlock(null);
				getPanelKopfdaten().eventYouAreSelected(false);
			}

		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_MANUELL_ERLEDIGEN_AUFHEBEN)) {
			// nur, wenn auch eine Bestellung ausgewaehlt ist.
			if (auftragDto != null && auftragDto.getIId() != null) {
				// Statuswechsel 'Erledigt' -> 'Offen' : Ausgeloest durch Menue
				if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT)) {
					if (DialogFactory.showMeldung(LPMain.getTextRespectUISPr("auft.auftragstatus.offensetzen"),
							LPMain.getTextRespectUISPr("lp.hint"),
							javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
						DelegateFactory.getInstance().getAuftragDelegate().erledigungAufheben(auftragDto.getIId());
						getAuftragKopfdaten().eventYouAreSelected(false);
					}
				} else {

					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("bes.warning.kannnichtunerledigtwerden"));

					mf.setLocale(LPMain.getTheClient().getLocUi());

					Object pattern[] = { auftragDto.getStatusCNr().trim() };

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
							mf.format(pattern) + ". " + LPMain.getTextRespectUISPr("auf.erledigung.aufheben.hinweis"));
				}
			}
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_ANGEBOT_ZEITDATEN_UEBERNEHMEN)) {
			if (auftragDto.getAngebotIId() != null) {
				DelegateFactory.getInstance().getZeiterfassungDelegate()
						.konvertiereAngebotszeitenNachAuftragzeiten(auftragDto.getAngebotIId(), auftragDto.getIId());
			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("auft.error.angebotnichtdefiniert"));
			}
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_ERFUELLUNGSGRAD_PRAEMIE)) {
			if (auftragDto.getIId() != null) {
				Double fErfuellungsgrad = null;
				if (auftragDto.getFErfuellungsgrad() != null) {
					fErfuellungsgrad = auftragDto.getFErfuellungsgrad();
				}

				BigDecimal bdPraemie = null;
				if (auftragDto.getNPraemie() != null) {
					bdPraemie = auftragDto.getNPraemie();
				}

				DialogErfuellungsgrad d = new DialogErfuellungsgrad(auftragDto, fErfuellungsgrad, bdPraemie);
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);
				auftragKopfdaten.eventYouAreSelected(false);
				if (auftragKonditionen != null) {
					auftragKonditionen.eventYouAreSelected(false);
				}

			}
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_RECHNUNGSADRESSE_BESTELLNUMMER_PROJEKT)) {
			if (auftragDto.getIId() != null) {

				if (auftragDto.getBestellungIIdAndererMandant() != null) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
							LPMain.getTextRespectUISPr("auft.error.anderermandant.kopfdaten.nichtmehraenderbar"));
					return;
				}

				DialogRechnungsadresseBestellnummerProjekt d = new DialogRechnungsadresseBestellnummerProjekt(
						getInternalFrame(), auftragDto);
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);
				auftragKopfdaten.eventYouAreSelected(false);

			}
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_INTERNER_KOMMENTAR)) {
			if (pruefeAktuellenAuftrag()) {
				if (!getAuftragKopfdaten().isLockedDlg()) {
					refreshPdAuftragInternerKommentar();
					getInternalFrame().showPanelDialog(pdAuftragInternerKommentar);
					setTitleAuftrag(LPMain.getTextRespectUISPr("lp.internerkommentar"));
				}
			}
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_EXTERNER_KOMMENTAR)) {
			if (pruefeAktuellenAuftrag()) {
				if (!getAuftragKopfdaten().isLockedDlg()) {
					refreshPdAuftragExternerKommentar();
					getInternalFrame().showPanelDialog(pdAuftragExternerKommentar);
					setTitleAuftrag(LPMain.getTextRespectUISPr("lp.externerkommentar"));
				}
			}
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_OFFEN)) {
			getInternalFrame().showReportKriterien(
					new ReportAuftragOffene2(getInternalFrame(),
							LPMain.getTextRespectUISPr("auft.print.listeoffeneauftraege")),
					getKundeAuftragDto().getPartnerDto(), getAuftragDto().getAnsprechparnterIId(), false);
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_OFFEN_DETAILS)) {
			getInternalFrame().showReportKriterien(
					new ReportAuftragOffeneDetails(getInternalFrame(),
							LPMain.getTextRespectUISPr("auft.print.listeoffenedetails")),
					getKundeAuftragDto().getPartnerDto(), getAuftragDto().getAnsprechparnterIId(), false);
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_OFFEN_POSITIONEN)) {
			getInternalFrame().showReportKriterien(
					new ReportAuftragOffenePositionen(getInternalFrame(),
							LPMain.getTextRespectUISPr("auft.print.listeoffenedetails")),
					getKundeAuftragDto().getPartnerDto(), getAuftragDto().getAnsprechparnterIId(), false);
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_UEBERSICHT)) {
			// wenn man diese Auswahl zuliesse, waeren danach die Panels
			// verfuegbar
			if (pruefeAktuellenAuftrag()) {
				getKriterienAuftragUebersicht();
				getInternalFrame().showPanelDialog(pdKriterienAuftragUebersicht);
				bKriterienAuftragUebersichtUeberMenueAufgerufen = true;
			}
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_STATISTIK)) {

			String add2Title = LPMain.getTextRespectUISPr("auft.menu.journal.statistik");
			getInternalFrame().showReportKriterien(new ReportAuftragstatistik(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_UMSATZSTATISTIK)) {

			String add2Title = LPMain.getTextRespectUISPr("auft.menu.umsatzstatistik");
			getInternalFrame().showReportKriterien(new ReportAuftragUmsatzstatistik(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_ERFUELLUNGSGRAD)) {

			String add2Title = LPMain.getTextRespectUISPr("auft.menu.journal.erfuellungsgrad");
			getInternalFrame().showReportKriterien(new ReportErfuellungsgrad(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_WARTUNGSAUSWERTUNG)) {

			String add2Title = LPMain.getTextRespectUISPr("kunde.report.wartungsauswertung");
			getInternalFrame().showReportKriterien(new ReportWartungsauswertung(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_ALLE_AUFTRAEGE)) {

			String add2Title = LPMain.getTextRespectUISPr("auft.menu.journal.alle");
			getInternalFrame().showReportKriterien(new ReportAuftragAlle(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_ERLEDIGTE_AUFTRAEGE)) {

			String add2Title = LPMain.getTextRespectUISPr("auft.menu.journal.erledigte");
			getInternalFrame().showReportKriterien(new ReportAuftragErledigt(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_TAETIGKEITSSTATISTIK)) {

			String add2Title = LPMain.getTextRespectUISPr("auftrag.report.taetigkeitsstatistik");
			getInternalFrame().showReportKriterien(new ReportTaetigkeitsstatistik(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_INFO_RAHMENERFUELLUNG)) {

			String add2Title = LPMain.getTextRespectUISPr("auftrag.report.erfuellungsjournal");
			getInternalFrame().showReportKriterien(new ReportErfuellungsjournal(getInternalFrameAuftrag(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_INFO_AUFTRAGSUEBERSICHT)) {

			String add2Title = LPMain.getTextRespectUISPr("auft.auftrag.info.auftragsuebersicht");
			getInternalFrame().showReportKriterien(new ReportAuftragsuebersicht(getInternalFrameAuftrag(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_INFO_PROJEKTBLATT)) {

			String add2Title = LPMain.getTextRespectUISPr("auft.report.projektblatt");
			getInternalFrame().showReportKriterien(
					new ReportProjektblatt(getInternalFrameAuftrag(), getAuftragDto().getIId(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_PFLEGE_INDEXANPASSUNG)) {

			boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.pflege.indexanpassung.warning"));

			if (b == true) {
				ArrayList<Integer> alErhoeht = DelegateFactory.getInstance().getAuftragDelegate()
						.wiederholendeAuftraegeUmIndexAnpassen();

				if (alErhoeht != null) {

					String msg = LPMain.getTextRespectUISPr("auft.pflege.indexanpassung.durchgefuehrt");

					for (int i = 0; i < alErhoeht.size(); i++) {
						AuftragDto aDto = DelegateFactory.getInstance().getAuftragDelegate()
								.auftragFindByPrimaryKey(alErhoeht.get(i));
						msg += aDto.getCNr();

						if (i != alErhoeht.size() - 1) {
							msg += ", ";
						}

					}

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"), msg);

				}

			}

		} else if (e.getActionCommand()
				.equals(MENUE_ACTION_PFLEGE_WIEDERHOLENDE_PREISE_ZUR_PREISGUELTIGKEIT_ANPASSEN)) {

			DialogWiederholendeAuftraegeAktualsisieren d = new DialogWiederholendeAuftraegeAktualsisieren();
			d.setTitle(LPMain.getTextRespectUISPr("lp.frage"));
			d.getWnfDatum().setDate(new java.sql.Date(System.currentTimeMillis()));
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
			d.setVisible(true);

			if (d.datum != null) {
				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getMessageTextRespectUISPr(
								"auft.pflege.wiederholende.zurpreisgueltigkeit.aktualisieren.warning",
								Helper.formatDatum(d.getWnfDatum().getDate(), LPMain.getTheClient().getLocUi())));

				if (b == true) {
					String s = DelegateFactory.getInstance().getAuftragDelegate()
							.wiederholendeAuftraegeMitPreisgueltigkeitAnpassen(
									new Timestamp(d.getWnfDatum().getTimestamp().getTime()),
									d.getWnfMaxAbweichung().getDouble());

					if (s != null) {

						String msg = LPMain
								.getTextRespectUISPr("auft.pflege.wiederholende.zurpreisgueltigkeit.durchgefuehrt");

						JTextPane textArea = new JTextPane();
						textArea.setContentType("text/html");

						textArea.setText(s);
						JScrollPane scrollPane = new JScrollPane(textArea);

						scrollPane.setPreferredSize(new Dimension(600, 500));
						JOptionPane.showMessageDialog(null, scrollPane, LPMain.getTextRespectUISPr("lp.info"),
								JOptionPane.INFORMATION_MESSAGE);

					}

				}
			}

		}

		else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_MEILENSTEINE)) {

			String add2Title = LPMain.getTextRespectUISPr("auft.report.meilensteine");
			getInternalFrame().showReportKriterien(
					new ReportMeilensteine(getInternalFrameAuftrag(), getAuftragDto().getIId(), add2Title));
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_ERFOLGSSTATUS)) {

			String add2Title = LPMain.getTextRespectUISPr("auftrag.erfolgsstatus");
			getInternalFrame().showReportKriterien(new ReportErfolgsstatus(getInternalFrameAuftrag(), add2Title));
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_LIEFERPLAN)) {

			String add2Title = LPMain.getTextRespectUISPr("auftrag.lieferplan");
			getInternalFrame().showReportKriterien(new ReportLieferplan(getInternalFrameAuftrag(), add2Title));
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_TEILNEHMER)) {

			String add2Title = LPMain.getTextRespectUISPr("report.auftrag.teilnehmer");
			getInternalFrame().showReportKriterien(new ReportTeilnehmer(getInternalFrameAuftrag(), add2Title));
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_AUSZULIEFERNDE_POSITIONEN)) {

			String add2Title = LPMain.getTextRespectUISPr("auft.report.auszulieferndepositionen");
			getInternalFrame()
					.showReportKriterien(new ReportAuszulieferndePositionen(getInternalFrameAuftrag(), add2Title));
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_PLANSTUNDEN)) {

			String add2Title = LPMain.getTextRespectUISPr("auft.report.planstunden");
			getInternalFrame().showReportKriterien(
					new ReportPlanstunden(getInternalFrameAuftrag(), getAuftragDto().getIId(), add2Title));
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_MATERIALBEDARFE)) {

			String add2Title = LPMain.getTextRespectUISPr("auft.report.materialbedarfe");
			getInternalFrame().showReportKriterien(
					new ReportMaterialbedarfe(getInternalFrameAuftrag(), getAuftragDto().getIId(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_INFO_RAHMENUEBERSICHT)) {

			String add2Title = LPMain.getTextRespectUISPr("auftrag.report.rahmenuebersicht");
			if (getAuftragDto().getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_RAHMEN)) {

				getInternalFrame().showReportKriterien(
						new ReportRahmenuebersicht(getInternalFrameAuftrag(), getAuftragDto().getIId(), add2Title));

			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
						LPMain.getTextRespectUISPr("auft.rahmenauftragwaehlen"));
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_INFO_WIEDERBESCHAFFUNG)) {
			getInternalFrame().showReportKriterien(
					new ReportAuftragWiederbeschaffung(getInternalFrame(), getAuftragDto().getIId(),
							LPMain.getTextRespectUISPr("auft.menu.info.wiederbeschaffung")),
					getKundeAuftragDto().getPartnerDto(), getAuftragDto().getAnsprechparnterIId(), false);
		} else if (e.getActionCommand().equals(MENUE_ACTION_INFO_ROLLIERENDEPLANUNG)) {
			getInternalFrame().showReportKriterien(
					new ReportRollierendePlanung(getInternalFrame(), getAuftragDto().getIId(),
							LPMain.getTextRespectUISPr("auft.rollierendeplanung")),
					getKundeAuftragDto().getPartnerDto(), getAuftragDto().getAnsprechparnterIId(), false);
		} else if (e.getActionCommand().equals(MENUE_ACTION_INFO_VERFUEGBARKEITSPRUEFUNG)) {
			if (auftragDto != null && auftragDto.getIId() != null) {
				if (pruefeAktuellenAuftrag()) {
					if (this.aktuellerAuftragHatArtikelpositionen()) {
						getInternalFrame().showReportKriterien(
								new ReportVerfuegbarkeitspruefung(getInternalFrame(), getAuftragDto(), getTitleDruck()),
								getKundeAuftragDto().getPartnerDto(), getAuftragDto().getAnsprechparnterIId(), false);
					}
				}
			}
		} else if (e.getActionCommand().equals(MENU_ACTION_DATEI_VERSANDWEGBESTAETIGUNG)) {
			printAuftragbestaetigungClevercure();
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_HANDARTIKEL_IN_BESTEHENDEN_ARTIKEL_UMANDELN)) {
			if (auftragPositionenTop != null && auftragPositionenTop.getSelectedId() != null) {

				AuftragpositionDto posDto = DelegateFactory.getInstance().getAuftragpositionDelegate()
						.auftragpositionFindByPrimaryKey((Integer) auftragPositionenTop.getSelectedId());

				ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(posDto.getArtikelIId());
				if (getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)
						&& aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {

					panelQueryFLRArtikel_handartikelumwandeln = ArtikelFilterFactory.getInstance()
							.createPanelFLRArtikel(getInternalFrame(), false);
					new DialogQuery(panelQueryFLRArtikel_handartikelumwandeln);
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
							LPMain.getTextRespectUISPr("auftrag.bearbeiten.handartikelinbestehendenumwandeln.error"));
				}

				auftragPositionen.eventYouAreSelected(false);
				auftragPositionenTop.setSelectedId(posDto.getIId());
			}
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_HANDARTIKEL_UMANDELN)) {
			if (auftragPositionenTop != null && auftragPositionenTop.getSelectedId() != null) {

				AuftragpositionDto posDto = DelegateFactory.getInstance().getAuftragpositionDelegate()
						.auftragpositionFindByPrimaryKey((Integer) auftragPositionenTop.getSelectedId());

				if (posDto.getArtikelIId() != null && auftragDto != null
						&& posDto.getAuftragpositionstatusCNr().equals(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN)) {

					ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByPrimaryKey(posDto.getArtikelIId());
					if (aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
						DialogNeueArtikelnummer d = new DialogNeueArtikelnummer(getInternalFrame());
						LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
						d.setVisible(true);
						if (d.getArtikelnummerNeu() != null) {

							DelegateFactory.getInstance().getArtikelDelegate().wandleHandeingabeInArtikelUm(
									posDto.getIId(), ArtikelFac.HANDARTIKEL_UMWANDELN_AUFTRAG, d.getArtikelnummerNeu());
						}

					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
								LPMain.getTextRespectUISPr("auftrag.bearbeiten.handartikelumwandeln.error2"));
					}

				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
							LPMain.getTextRespectUISPr("auftrag.bearbeiten.handartikelumwandeln.error"));
				}
				auftragPositionen.eventYouAreSelected(false);
				auftragPositionenTop.setSelectedId(posDto.getIId());
			}

		}
	}

	public void eventActionUnlock() throws Throwable {
		getPanelKopfdaten().eventActionUnlock(null);
		refreshAuftragKopfdaten().eventActionRefresh(null, false);
	}

	private void printAuftragbestaetigungClevercure() throws Throwable {
		JTextArea msgLabel;
		JProgressBar progressBar;
		final int MAXIMUM = 100;
		JPanel panel;

		progressBar = new JProgressBar(0, MAXIMUM);
		progressBar.setIndeterminate(true);
		msgLabel = new JTextArea("An Clevercure \u00FCbermitteln");
		msgLabel.setEditable(false);

		panel = new JPanel(new BorderLayout(5, 5));
		panel.add(msgLabel, BorderLayout.PAGE_START);
		panel.add(progressBar, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(11, 11, 11, 11));

		final JDialog dialog = new JDialog();
		dialog.getContentPane().add(panel);
		dialog.setResizable(false);
		dialog.pack();
		dialog.setSize(500, dialog.getHeight());
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setAlwaysOnTop(false);
		dialog.setVisible(true);
		msgLabel.setBackground(panel.getBackground());

		SwingWorker<String, Object> worker = new SwingWorker<String, Object>() {
			@Override
			protected void done() {
				dialog.dispose();
			}

			@Override
			protected String doInBackground() throws Exception {
				String result = "";
				try {
					result = DelegateFactory.getInstance().getAuftragDelegate()
							.createOrderResponsePost(getAuftragDto());
					publish(result);
				} catch (Throwable t) {
					handleException(t, false);
				}

				return result;
			}
		};

		worker.execute();
	}

	private void refreshPdAuftragInternerKommentar() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken des
		// Auftrags nicht richtig
		pdAuftragInternerKommentar = new PanelDialogAuftragKommentar(getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.internerkommentar"), true);
	}

	private void refreshPdAuftragExternerKommentar() throws Throwable {
		// das Panel immer neu anlegen, sonst funktioniert das Locken des
		// Auftrags nicht richtig
		pdAuftragExternerKommentar = new PanelDialogAuftragKommentar(getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.externerkommentar"), false);
	}

	public boolean aktualisiereAuftragstatus() throws Throwable {
		boolean bIstAktualisierenErlaubt = false;

		if (pruefeAktuellenAuftrag()) {

			// PJ19485
			if (auftragDto.getBestellungIIdAndererMandant() != null) {
				BestellungDto bsDto = DelegateFactory.getInstance().getBestellungDelegate()
						.bestellungFindByPrimaryKey(auftragDto.getBestellungIIdAndererMandant());

				int nWEs = DelegateFactory.getInstance().getWareneingangDelegate().getAnzahlWE(bsDto.getIId());

				if (bsDto.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_TEILERLEDIGT)
						|| bsDto.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_GELIEFERT)
						|| bsDto.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_ERLEDIGT) || nWEs > 0) {

					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("auft.error.besinanderemmandanten.zumindestteilerledigt"));
					Object[] pattern = new Object[] { bsDto.getCNr(), bsDto.getMandantCNr(),
							bsDto.getStatusCNr().trim() };

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"), mf.format(pattern));
					return false;

				}

				if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)) {
					// SP6288
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("auft.error.besinanderemmandanten.offen.nichtmehrgeaendert"));
					return false;
				}

			}

			if (bAuftragsfreigabe && auftragDto.getTAuftragsfreigabe() != null
					&& !(auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT))) {
				// Wenn der Auftrag bereits freigegeben ist und keine
				// Berechtigung vorhanden

				boolean b = DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_AUFT_DARF_AUFTRAEGE_FREIGEBEN);

				if (b == false) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("auft.freigeben.ruecknahme.error"));
					return false;
				} else {
					if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
							|| auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)
							|| auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {

						if (DialogFactory.showMeldung(

								LPMain.getTextRespectUISPr("auft.freigeben.ruecknahme.frage"),
								LPMain.getTextRespectUISPr("lp.frage"),
								javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
							DelegateFactory.getInstance().getAuftragDelegate().aendereAuftragstatus(auftragDto.getIId(),
									AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
							setAuftragDto(DelegateFactory.getInstance().getAuftragDelegate()
									.auftragFindByPrimaryKey(auftragDto.getIId()));
							return true;
						} else {
							return false;
						}

					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("auft.freigeben.ruecknahme.nurbeioffen"));

						return false;
					}

				}

			}

			// im Status 'Angelegt' duerfen die Kopfdaten geaendert werden
			if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {

				bIstAktualisierenErlaubt = true;

			} else if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)) {

				Object[] options = { LPMain.getTextRespectUISPr("lp.ja"), LPMain.getTextRespectUISPr("lp.nein"),
						LPMain.getTextRespectUISPr("auft.aenderungsauftrag") };
				int iOption = DialogFactory.showModalDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("lp.hint.offennachangelegt"), "", options, options[1]);

				if (iOption == JOptionPane.NO_OPTION) {
					bIstAktualisierenErlaubt = false;

				} else if (iOption == JOptionPane.YES_OPTION) {
					bIstAktualisierenErlaubt = true;

				} else if (iOption == JOptionPane.CANCEL_OPTION) {
					// 19236
					// auftragDto.setTAenderungsauftrag(new Timestamp(System
					// .currentTimeMillis()));
					// DelegateFactory
					// .getInstance()
					// .getAuftragDelegate()
					// .setAuftragstatus(auftragDto,
					// AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
					AuftragDto aenderungsauftrag = DelegateFactory.getInstance().getAuftragDelegate()
							.erzeugeAenderungsauftrag(auftragDto.getIId());
					setAuftragDto(aenderungsauftrag);
					bIstAktualisierenErlaubt = true;
				}

			} else if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
				if (DialogFactory.showMeldung(LPMain.getTextRespectUISPr("auft.auftragstatus.offensetzen"),
						LPMain.getTextRespectUISPr("lp.hint"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {

					DelegateFactory.getInstance().getAuftragDelegate().aendereAuftragstatus(auftragDto.getIId(),
							AuftragServiceFac.AUFTRAGSTATUS_OFFEN);

					// DialogFactory.showModalDialog(
					// LPMain.getTextRespectUISPr("lp.hint"),
					// LPMain.getTextRespectUISPr(
					// "auft.auftragstatus.offen"));

					// den geaenderten Status anzeigen
					setAuftragDto(DelegateFactory.getInstance().getAuftragDelegate()
							.auftragFindByPrimaryKey(auftragDto.getIId()));
					auftragKopfdaten.eventYouAreSelected(false);
				}
			} else if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)) {
				if (DialogFactory.showMeldung(LPMain.getTextRespectUISPr("auft.auftragstatus.offensetzen"),
						LPMain.getTextRespectUISPr("lp.hint"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {

					DelegateFactory.getInstance().getAuftragDelegate().aendereAuftragstatus(auftragDto.getIId(),
							AuftragServiceFac.AUFTRAGSTATUS_OFFEN);

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("auft.auftragstatus.offen"));

					// den geaenderten Status anzeigen
					setAuftragDto(DelegateFactory.getInstance().getAuftragDelegate()
							.auftragFindByPrimaryKey(auftragDto.getIId()));
					auftragKopfdaten.eventYouAreSelected(false);
				}
			} else if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {

				// SP4010
				ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate()
						.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
								ParameterFac.PARAMETER_AUFTRAG_AUTOMATISCH_VOLLSTAENDIG_ERLEDIGEN);
				int iAutomatischErledigen = (Integer) parameter.getCWertAsObject();

				if (iAutomatischErledigen == 0 && !DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_AUFT_DARF_AUFTRAG_ERLEDIGEN)) {

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("auft.hint.erledigungzuruecknehmen.error"));
					return false;
				}

				if (DialogFactory.showMeldung(LPMain.getTextRespectUISPr("auft.hint.auftragerledigt"),
						LPMain.getTextRespectUISPr("lp.hint"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
					DelegateFactory.getInstance().getAuftragDelegate().erledigungAufheben(auftragDto.getIId());
					initializeDtos(auftragDto.getIId());
					refreshAktuellesPanel();
				}
			}
		}

		return bIstAktualisierenErlaubt;
	}

	/**
	 * Diese Methode prueft den Status des aktuellen Auftrags und legt fest, ob eine
	 * Aenderung in den Kopfdaten bzw. Konditionen erlaubt ist.
	 * 
	 * @return boolean true, wenn ein update erlaubt ist
	 * @throws java.lang.Throwable Ausnahme
	 */
	public boolean istAktualisierenAuftragErlaubt() throws Throwable {
		boolean bIstAenderungErlaubtO = false;

		// PJ19485
		if (auftragDto.getBestellungIIdAndererMandant() != null) {
			BestellungDto bsDto = DelegateFactory.getInstance().getBestellungDelegate()
					.bestellungFindByPrimaryKey(auftragDto.getBestellungIIdAndererMandant());

			int nWEs = DelegateFactory.getInstance().getWareneingangDelegate().getAnzahlWE(bsDto.getIId());

			if (bsDto.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_TEILERLEDIGT)
					|| bsDto.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_GELIEFERT)
					|| bsDto.getStatusCNr().equals(BestellungFac.BESTELLSTATUS_ERLEDIGT) || nWEs > 0) {

				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("auft.error.besinanderemmandanten.zumindestteilerledigt"));
				Object[] pattern = new Object[] { bsDto.getCNr(), bsDto.getMandantCNr(), bsDto.getStatusCNr().trim() };

				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"), mf.format(pattern));
				return false;

			}

			if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)) {
				// SP6288
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("auft.error.besinanderemmandanten.offen.nichtmehrgeaendert"));
				return false;
			}

		}

		if (pruefeAktuellenAuftrag()) {

			if (bAuftragsfreigabe && auftragDto.getTAuftragsfreigabe() != null) {
				// Wenn der Auftrag bereits freigegeben ist und keine
				// Berechtigung vorhanden

				boolean b = DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_AUFT_DARF_AUFTRAEGE_FREIGEBEN);

				if (b == false) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("auft.freigeben.ruecknahme.error"));
					return false;
				} else {
					if ((auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
							|| auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT))) {

						if (DialogFactory.showMeldung(

								LPMain.getTextRespectUISPr("auft.freigeben.ruecknahme.frage"),
								LPMain.getTextRespectUISPr("lp.frage"),
								javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
							auftragDto.setPersonalIIdAuftragsfreigabe(null);
							auftragDto.setTAuftragsfreigabe(null);
							DelegateFactory.getInstance().getAuftragDelegate().setAuftragstatus(auftragDto,
									AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
							return true;
						} else {
							return false;
						}

					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("auft.freigeben.ruecknahme.nurbeioffen"));

						return false;
					}

				}

			}
			if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {

				bIstAenderungErlaubtO = true;

			} else if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)) {

				Object[] options = { LPMain.getTextRespectUISPr("lp.ja"), LPMain.getTextRespectUISPr("lp.nein"),
						LPMain.getTextRespectUISPr("auft.aenderungsauftrag") };
				int iOption = DialogFactory.showModalDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("lp.hint.offennachangelegt"), "", options, options[1]);

				if (iOption == JOptionPane.NO_OPTION) {
					return false;
				} else if (iOption == JOptionPane.YES_OPTION) {
					DelegateFactory.getInstance().getAuftragDelegate().setAuftragstatus(auftragDto,
							AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
					return true;
				} else if (iOption == JOptionPane.CANCEL_OPTION) {
					// // 19236
					// auftragDto.setTAenderungsauftrag(new Timestamp(System
					// .currentTimeMillis()));
					//
					// DelegateFactory
					// .getInstance()
					// .getAuftragDelegate()
					// .setAuftragstatus(auftragDto,
					// AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
					AuftragDto aenderungsauftrag = DelegateFactory.getInstance().getAuftragDelegate()
							.erzeugeAenderungsauftrag(auftragDto.getIId());
					setAuftragDto(aenderungsauftrag);
					return true;
				}

				return false;

			} else if (auftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)) {
				/*
				 * SP2553 if (AuftragServiceFac.AUFTRAGART_RAHMEN.equals(auftragDto
				 * .getAuftragartCNr())) { bIstAenderungErlaubtO = true; } else {
				 */
				showStatusMessage("lp.warning", "auft.warning.auftragkannnichtgeaendertwerden");
				// }
			} else {
				showStatusMessage("lp.warning", "auft.warning.auftragkannnichtgeaendertwerden");
			}
		}

		return bIstAenderungErlaubtO;
	}

	/*
	 * private void printListeOffenerAuftraege(ReportJournalKriterienDto
	 * reportJournalKriterienDtoI) throws Throwable { try { JasperPrint print =
	 * DelegateFactory.getInstance().getAuftragReportDelegate().
	 * printAuftragOffene(reportJournalKriterienDtoI);
	 * 
	 * if (print == null) { throw new ExceptionLP(EJBExceptionLP.FEHLER, new
	 * Exception("print == null")); }
	 * 
	 * getInternalFrame().showReport( print, LPMain.getTextRespectUISPr
	 * ("auft.print.listeoffeneauftraege")); } catch (ExceptionLP ex) {
	 * 
	 * int code = ex.getICode();
	 * 
	 * switch (code) { case EJBExceptionLP.FEHLER_MUSS_GROESSER_0_SEIN:
	 * DialogFactory.showModalDialog( LPMain.getTextRespectUISPr("lp.error"),
	 * LPMain.getTextRespectUISPr( "auft.warning.keineoffenenauftraege")); } } }
	 */

	private void printNachkalkulation() throws Throwable {
		if (pruefeAktuellenAuftrag()) {
			if (DelegateFactory.getInstance().getAuftragDelegate()
					.berechneAnzahlBelegeZuAuftrag(auftragDto.getIId()) == 0) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("auft.hint.keinebelege"));
			} else {
				getInternalFrame().showReportKriterien(
						new ReportAuftragnachkalkulation(getInternalFrame(), getAuftragDto().getIId(), getTitleDruck()),
						getKundeAuftragDto().getPartnerDto(), getAuftragDto().getAnsprechparnterIId(), false);
			}
		}
	}

	/**
	 * Packliste zum aktuellen Auftrag drucken.
	 * 
	 * @throws Throwable Ausnahme
	 */
	private void printPackliste() throws Throwable {
		if (auftragDto != null && auftragDto.getIId() != null) {
			if (pruefeAktuellenAuftrag()) {
				if (this.aktuellerAuftragHatArtikelpositionen()) {
					getInternalFrame().showReportKriterien(
							new ReportAuftragPackliste(getInternalFrame(), getAuftragDto().getIId(), getTitleDruck()),
							getKundeAuftragDto().getPartnerDto(), getAuftragDto().getAnsprechparnterIId(), false);
				}
			}
		}
	}

	private void printKommissionierung() throws Throwable {
		if (auftragDto != null && auftragDto.getIId() != null) {
			if (pruefeAktuellenAuftrag()) {
				if (this.aktuellerAuftragHatArtikelpositionen()) {
					getInternalFrame().showReportKriterien(
							new ReportKommissionierung(getInternalFrame(), getAuftragDto().getIId(), getTitleDruck()),
							getKundeAuftragDto().getPartnerDto(), getAuftragDto().getAnsprechparnterIId(), false);
				}
			}
		}
	}

	private void printEtikett() throws Throwable {

	}

	/**
	 * Auftragbestaetigung + Vorkalkulation zum aktuellen Auftrag drucken.
	 * 
	 * @throws Throwable
	 */
	public void printAuftragbestaetigung() throws Throwable {
		if (auftragDto != null && auftragDto.getIId() != null) {
			// belegartkonditionen: 7 Pruefen, ob die Konitionen erfasst wurden
			if (pruefeKonditionen()) {
				if (aktuellerAuftragHatPositionen()) {
					if (!refreshAuftragKopfdaten().isLockedDlg()) {
						DelegateFactory.getInstance().getLagerDelegate().getHauptlagerDesMandanten();
						// pruefen, beginn und ende
						boolean ok = DelegateFactory.getInstance().getAuftragDelegate()
								.checkPositionFormat(auftragDto.getIId());
						if (ok) {
							getInternalFrame().showReportKriterien(
									new ReportAuftrag(getInternalFrame(), getAktuellesPanel(), getAuftragDto(),
											getTitleDruck()),
									getKundeAuftragDto().getPartnerDto(), getAuftragDto().getAnsprechparnterIId(),
									false);
						} else {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("auft.hint.beginnohneende"));
						}
					}
				}
			}
		}
	}

	/**
	 * belegartkonditionen: 3 Diese Methode prueft, ob zum aktuellen Auftrag
	 * Konditionen erfasst wurden. <br>
	 * Wenn der Benutzer aufgrund von KONDITIONEN_BESTAETIGEN die Konditionen nicht
	 * bestaetigen muss, muessen die Default Texte vorbelegt werden.
	 * 
	 * @return boolean true, wenn die Konditionen gueltig erfasst wurden
	 * @throws Throwable Ausnahme
	 */
	protected boolean pruefeKonditionen() throws Throwable {
		boolean bErfasst = true;

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
				ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN);

		Short sValue = new Short(parameter.getCWert());

		if (pruefeAktuellenAuftrag()) {
			if (Helper.short2boolean(sValue)) {
				if (auftragDto.getAuftragIIdKopftext() == null && auftragDto.getAuftragIIdFusstext() == null) {
					bErfasst = false;

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("lp.hint.konditionenerfassen"));
				}
			} else {
				// belegartkonditionen: 4 die Auftragkonditionen initialisieren
				initAuftragKonditionen();
			}
		}

		return bErfasst;
	}

	/**
	 * belegartkonditionen: 5 Je nach Mandantenparameter muss der Benutzer die
	 * Konditionen nicht erfassen. Damit der Auftrag gedruckt werden kann, muessen
	 * die Konditionen aber initialisiert worden sein.
	 * 
	 * @throws Throwable Ausnahme
	 */
	public void initAuftragKonditionen() throws Throwable {
		initAuftragtexte(); // Kopf- und Fusstext werden initialisiert

		if (getAuftragDto().getAuftragIIdKopftext() == null || getAuftragDto().getAuftragIIdFusstext() == null) {
			getAuftragDto().setAuftragtextIIdKopftext(kopftextDto.getIId());
			getAuftragDto().setAuftragtextIIdFusstext(fusstextDto.getIId());

			DelegateFactory.getInstance().getAuftragDelegate().updateAuftragOhneWeitereAktion(getAuftragDto());
		}
	}

	/**
	 * Diese Methode setzt des aktuellen Auftrag aus der Auswahlliste als den zu
	 * lockenden Auftrag.
	 * 
	 * @throws java.lang.Throwable Ausnahme
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = auftragAuswahl.getSelectedId();

		if (oKey != null) {
			initializeDtos((Integer) oKey);

			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void getKriterienAuftragUebersicht() throws Throwable {
		if (pdKriterienAuftragUebersicht == null) {

			pdKriterienAuftragUebersicht = new PanelDialogKriterienAuftragUebersicht(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kriterienumsatzuebersicht"));
		}
	}

	private void getKriterienAuftragzeiten() throws Throwable {
		if (pdKriterienAuftragzeiten == null) {

			pdKriterienAuftragzeiten = new PanelDialogKriterienAuftragzeiten(getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.kriterienauftragzeiten"));
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		// if(menuBar == null) {
		// menuBar = new WrapperMenuBar(this) ;
		// }
		menuBar = new WrapperMenuBar(this);
		// Menue Datei; ein neuer Eintrag wird immer oben im Menue eingetragen
		JMenu jmModul = (JMenu) menuBar.getComponent(WrapperMenuBar.MENU_MODUL);

		jmModul.add(new JSeparator(), 0);

		JMenuItem menuItemDateiEtikett = new JMenuItem(LPMain.getTextRespectUISPr("auft.menu.datei.auftragetikett"));
		menuItemDateiEtikett.addActionListener(this);
		menuItemDateiEtikett.setActionCommand(MENU_ACTION_DATEI_ETIKETT);
		// jmModul.add(menuItemDateiEtikett, 0);
		JMenuItem menuItemEtikett = new JMenuItem(LPMain.getTextRespectUISPr("auft.auftrag.info.auftragsetikett"));
		menuItemEtikett.addActionListener(this);
		menuItemEtikett.setActionCommand(MENU_ACTION_INFO_AUFTRAGSETIKETT);
		jmModul.add(menuItemEtikett, 0);

		JMenuItem menuItemDateiKommissionierung = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.datei.kommissionierung"));
		menuItemDateiKommissionierung.addActionListener(this);
		menuItemDateiKommissionierung.setActionCommand(MENU_ACTION_DATEI_KOMMISSIONIERUNG);
		jmModul.add(menuItemDateiKommissionierung, 0);
		JMenuItem menuItemDateiPackliste = new JMenuItem(LPMain.getTextRespectUISPr("auft.menu.datei.packliste"));
		menuItemDateiPackliste.addActionListener(this);
		menuItemDateiPackliste.setActionCommand(MENU_ACTION_DATEI_PACKLISTE);
		jmModul.add(menuItemDateiPackliste, 0);

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF)) {

			JMenuItem menuItemDateiAuftragbestaetigung = new JMenuItem(LPMain.getTextRespectUISPr("lp.menu.drucken"));
			menuItemDateiAuftragbestaetigung.addActionListener(this);
			menuItemDateiAuftragbestaetigung.setActionCommand(MENU_ACTION_DATEI_AUFTRAGBESTAETIGUNG);
			jmModul.add(menuItemDateiAuftragbestaetigung, 0);

			// updateMenuItemDateiAuftragVersand();
		}

		// Menue Bearbeiten

		boolean bSchreibrecht = false;

		boolean bDarfAuftraegeFreigeben = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_AUFT_DARF_AUFTRAEGE_FREIGEBEN);

		if (getInternalFrame().getRechtModulweit().equals(RechteFac.RECHT_MODULWEIT_UPDATE)) {
			bSchreibrecht = true;
		}

		if (bSchreibrecht) {
			JMenu modul = (JMenu) menuBar.getComponent(WrapperMenuBar.MENU_MODUL);
			JMenu abimport = new JMenu(LPMain.getTextRespectUISPr("lp.import"));
			modul.add(abimport, modul.getItemCount() - 2);

			JMenuItem menuItemImportSON = new JMenuItem(LPMain.getTextRespectUISPr("auft.import.son"));
			menuItemImportSON.addActionListener(this);
			menuItemImportSON.setActionCommand(ACTION_SPECIAL_CSVIMPORT_SON);
			abimport.add(menuItemImportSON);

			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAGS_VAT_IMPORT);
			boolean bVatImport = (Boolean) parameter.getCWertAsObject();
			if (bVatImport) {
				JMenuItem menuItemImportVAT = new JMenuItem(LPMain.getTextRespectUISPr("auft.import.vat"));
				menuItemImportVAT.addActionListener(this);
				menuItemImportVAT.setActionCommand(ACTION_SPECIAL_XSLXIMPORT_VAT);
				abimport.add(menuItemImportVAT);
			}

			JMenuItem menuItemImportWoocommerce = new JMenuItem(LPMain.getTextRespectUISPr("auft.import.woocommerce"));
			menuItemImportWoocommerce.addActionListener(this);
			menuItemImportWoocommerce.setActionCommand(ACTION_SPECIAL_CSVIMPORT_WOOCOMMERCE);
			abimport.add(menuItemImportWoocommerce);

		}

		JMenu jmBearbeiten = (JMenu) menuBar.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

		if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_FERT_TERMINE_VERSCHIEBEN)) {
			JMenuItem menuItemTerminverschieben = new JMenuItem(
					LPMain.getTextRespectUISPr("auft.menu.termineverschieben"));
			menuItemTerminverschieben.addActionListener(this);
			menuItemTerminverschieben.setActionCommand(MENUE_ACTION_BEARBEITEN_TERMINVERSCHIEBEN);

			if (bAuftragsfreigabe) {
				if (bDarfAuftraegeFreigeben) {
					jmBearbeiten.add(menuItemTerminverschieben);
				}
			} else {
				jmBearbeiten.add(menuItemTerminverschieben);
			}

		}

		JMenuItem menuItemHandartikelUmwandeln = new JMenuItem(
				LPMain.getTextRespectUISPr("angebot.bearbeiten.handartikelumwandeln"));
		menuItemHandartikelUmwandeln.addActionListener(this);
		menuItemHandartikelUmwandeln.setActionCommand(MENU_BEARBEITEN_HANDARTIKEL_UMANDELN);

		JMenuItem menuItemHandartikelInBestehendenUmwandeln = new JMenuItem(
				LPMain.getTextRespectUISPr("auftrag.bearbeiten.handartikelinbestehdendenumwandeln"));
		menuItemHandartikelInBestehendenUmwandeln.addActionListener(this);
		menuItemHandartikelInBestehendenUmwandeln
				.setActionCommand(MENU_BEARBEITEN_HANDARTIKEL_IN_BESTEHENDEN_ARTIKEL_UMANDELN);

		if (bAuftragsfreigabe) {
			if (bDarfAuftraegeFreigeben) {
				jmBearbeiten.add(menuItemHandartikelUmwandeln);
				jmBearbeiten.add(menuItemHandartikelInBestehendenUmwandeln);
			}
		} else {
			jmBearbeiten.add(menuItemHandartikelUmwandeln);
			jmBearbeiten.add(menuItemHandartikelInBestehendenUmwandeln);
		}
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {
			JMenuItem menuItemProjektErstellen = new JMenuItem(
					LPMain.getTextRespectUISPr("angebot.bearbeiten.projekterzeugen"));
			menuItemProjektErstellen.addActionListener(this);
			menuItemProjektErstellen.setActionCommand(MENU_BEARBEITEN_PROJEKT_ANLEGEN);
			jmBearbeiten.add(new JSeparator());
			jmBearbeiten.add(menuItemProjektErstellen);

		}
		jmBearbeiten.add(new JSeparator(), 0);

		JMenuItem menuItemBearbeitenExternerKommentar = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.externerkommentar"));
		menuItemBearbeitenExternerKommentar.addActionListener(this);
		menuItemBearbeitenExternerKommentar.setActionCommand(MENU_BEARBEITEN_EXTERNER_KOMMENTAR);
		jmBearbeiten.add(menuItemBearbeitenExternerKommentar, 0);

		JMenuItem menuItemBearbeitenInternerKommentar = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.internerkommentar"));
		menuItemBearbeitenInternerKommentar.addActionListener(this);
		menuItemBearbeitenInternerKommentar.setActionCommand(MENU_BEARBEITEN_INTERNER_KOMMENTAR);
		jmBearbeiten.add(menuItemBearbeitenInternerKommentar, 0);

		jmBearbeiten.add(new JSeparator(), 0);

		JMenuItem menuItemBearbeitenBegruendung = new JMenuItem(LPMain.getTextRespectUISPr("ls.begruendung"));
		menuItemBearbeitenBegruendung.addActionListener(this);
		menuItemBearbeitenBegruendung.setActionCommand(MENU_BEARBEITEN_BEGRUENDUNG);
		jmBearbeiten.add(menuItemBearbeitenBegruendung, 0);

		if (bSchreibrecht) {

			// PJ19372
			JMenuItem menuItemBearbeitenRechnungsadresse = new JMenuItem(
					LPMain.getTextRespectUISPr("auft.menu.bearbeiten.rechnungsadressebestellnummerprojekt"));

			menuItemBearbeitenRechnungsadresse.addActionListener(this);
			menuItemBearbeitenRechnungsadresse.setActionCommand(MENU_BEARBEITEN_RECHNUNGSADRESSE_BESTELLNUMMER_PROJEKT);

			if (bAuftragsfreigabe) {
				if (bDarfAuftraegeFreigeben) {
					jmBearbeiten.add(menuItemBearbeitenRechnungsadresse, 0);
				}
			} else {
				jmBearbeiten.add(menuItemBearbeitenRechnungsadresse, 0);
			}

			// PJ18288
			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_AUFTRAG_AUTOMATISCH_VOLLSTAENDIG_ERLEDIGEN);
			int iAutomatischErledigen = (Integer) parameter.getCWertAsObject();

			String recht = null;
			if (iAutomatischErledigen == 0) {
				recht = RechteFac.RECHT_AUFT_DARF_AUFTRAG_ERLEDIGEN;
			}

			WrapperMenuItem menuItemBearbeitenManuellErledigen = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("lp.menu.menuellerledigen"), recht);
			menuItemBearbeitenManuellErledigen.addActionListener(this);
			menuItemBearbeitenManuellErledigen.setActionCommand(MENU_BEARBEITEN_MANUELL_ERLEDIGEN);
			jmBearbeiten.add(menuItemBearbeitenManuellErledigen, 0);

			WrapperMenuItem menuItemBearbeitenManuellErledigenAufheben = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("bes.menu.bearbeiten.erledigungaufheben"), recht);

			menuItemBearbeitenManuellErledigenAufheben.addActionListener(this);
			menuItemBearbeitenManuellErledigenAufheben.setActionCommand(MENU_BEARBEITEN_MANUELL_ERLEDIGEN_AUFHEBEN);

			if (bAuftragsfreigabe) {
				if (bDarfAuftraegeFreigeben) {
					jmBearbeiten.add(menuItemBearbeitenManuellErledigenAufheben, 0);
				}
			} else {
				jmBearbeiten.add(menuItemBearbeitenManuellErledigenAufheben, 0);
			}

			JMenuItem menuItemBearbeitenAngebotZeitDaten = new JMenuItem(
					LPMain.getTextRespectUISPr("auft.hint.zeitdatenvonangebot"));

			menuItemBearbeitenAngebotZeitDaten.addActionListener(this);
			menuItemBearbeitenAngebotZeitDaten.setActionCommand(MENU_BEARBEITEN_ANGEBOT_ZEITDATEN_UEBERNEHMEN);
			jmBearbeiten.add(menuItemBearbeitenAngebotZeitDaten, 0);

			JMenuItem menuItemBearbeitenErfuellungsgrad = new JMenuItem(
					LPMain.getTextRespectUISPr("auft.menu.bearbeiten.erfuellungsgrad"));

			menuItemBearbeitenErfuellungsgrad.addActionListener(this);
			menuItemBearbeitenErfuellungsgrad.setActionCommand(MENU_BEARBEITEN_ERFUELLUNGSGRAD_PRAEMIE);

			jmBearbeiten.add(menuItemBearbeitenErfuellungsgrad, 0);

		}
		// Menue Journal
		JMenu jmJournal = (JMenu) menuBar.getComponent(WrapperMenuBar.MENU_JOURNAL);

		JMenuItem menuItemAlleAuftraege = new JMenuItem(LPMain.getTextRespectUISPr("auft.menu.journal.alle"));
		menuItemAlleAuftraege.addActionListener(this);
		menuItemAlleAuftraege.setActionCommand(MENU_ACTION_JOURNAL_ALLE_AUFTRAEGE);
		jmJournal.add(menuItemAlleAuftraege);

		JMenuItem menuItemErledigteAuftraege = new JMenuItem(LPMain.getTextRespectUISPr("auft.menu.journal.erledigte"));
		menuItemErledigteAuftraege.addActionListener(this);
		menuItemErledigteAuftraege.setActionCommand(MENU_ACTION_JOURNAL_ERLEDIGTE_AUFTRAEGE);
		jmJournal.add(menuItemErledigteAuftraege);

		JMenuItem menuItemJournalOffene = new JMenuItem(LPMain.getTextRespectUISPr("auft.menu.journal.offen"));
		menuItemJournalOffene.addActionListener(this);
		menuItemJournalOffene.setActionCommand(MENU_ACTION_JOURNAL_OFFEN);
		jmJournal.add(menuItemJournalOffene);

		JMenuItem menuItemJournalOffeneDetails = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.journal.offendetails"));
		menuItemJournalOffeneDetails.addActionListener(this);
		menuItemJournalOffeneDetails.setActionCommand(MENU_ACTION_JOURNAL_OFFEN_DETAILS);
		jmJournal.add(menuItemJournalOffeneDetails);

		JMenuItem menuItemJournalOffenePositionen = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.journal.offenpositionen"));
		menuItemJournalOffenePositionen.addActionListener(this);
		menuItemJournalOffenePositionen.setActionCommand(MENU_ACTION_JOURNAL_OFFEN_POSITIONEN);
		jmJournal.add(menuItemJournalOffenePositionen);

		jmJournal.add(new JSeparator());

		if (getInternalFrame().bRechtDarfPreiseSehenVerkauf) {

			JMenuItem menuItemJournalUebersicht = new JMenuItem(LPMain.getTextRespectUISPr("lp.menu.umsatzuebersicht"));
			menuItemJournalUebersicht.addActionListener(this);
			menuItemJournalUebersicht.setActionCommand(MENU_ACTION_JOURNAL_UEBERSICHT);
			jmJournal.add(menuItemJournalUebersicht);

			JMenuItem menuItemJournalUmsatzstatzistik = new JMenuItem(
					LPMain.getTextRespectUISPr("auft.menu.umsatzstatistik"));
			menuItemJournalUmsatzstatzistik.addActionListener(this);
			menuItemJournalUmsatzstatzistik.setActionCommand(MENU_ACTION_JOURNAL_UMSATZSTATISTIK);
			jmJournal.add(menuItemJournalUmsatzstatzistik);

			WrapperMenuItem menuItemJournalStatistik = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("auft.menu.journal.statistik"), RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
			menuItemJournalStatistik.addActionListener(this);
			menuItemJournalStatistik.setActionCommand(MENU_ACTION_JOURNAL_STATISTIK);
			jmJournal.add(menuItemJournalStatistik);
		}

		WrapperMenuItem menuItemerfuellungsgrad = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.journal.erfuellungsgrad"),
				RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
		menuItemerfuellungsgrad.addActionListener(this);
		menuItemerfuellungsgrad.setActionCommand(MENU_ACTION_JOURNAL_ERFUELLUNGSGRAD);
		jmJournal.add(menuItemerfuellungsgrad);

		JMenuItem menuItemWartungsauswertung = new JMenuItem(
				LPMain.getTextRespectUISPr("kunde.report.wartungsauswertung"));
		menuItemWartungsauswertung.addActionListener(this);
		menuItemWartungsauswertung.setActionCommand(MENU_ACTION_JOURNAL_WARTUNGSAUSWERTUNG);
		jmJournal.add(menuItemWartungsauswertung);

		JMenuItem menuItemTaetigkeitsjournal = new JMenuItem(
				LPMain.getTextRespectUISPr("auftrag.report.taetigkeitsstatistik"));
		menuItemTaetigkeitsjournal.addActionListener(this);
		menuItemTaetigkeitsjournal.setActionCommand(MENU_ACTION_JOURNAL_TAETIGKEITSSTATISTIK);
		jmJournal.add(menuItemTaetigkeitsjournal);

		WrapperMenuItem menuItemErfolgsstatus = new WrapperMenuItem(LPMain.getTextRespectUISPr("auftrag.erfolgsstatus"),
				RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
		menuItemErfolgsstatus.addActionListener(this);
		menuItemErfolgsstatus.setActionCommand(MENU_ACTION_JOURNAL_ERFOLGSSTATUS);
		jmJournal.add(menuItemErfolgsstatus);

		JMenuItem menuItemLieferplan = new JMenuItem(LPMain.getTextRespectUISPr("auftrag.lieferplan"));
		menuItemLieferplan.addActionListener(this);
		menuItemLieferplan.setActionCommand(MENU_ACTION_JOURNAL_LIEFERPLAN);
		jmJournal.add(menuItemLieferplan);

		JMenuItem menuItemTeilnehmer = new JMenuItem(LPMain.getTextRespectUISPr("report.auftrag.teilnehmer"));
		menuItemTeilnehmer.addActionListener(this);
		menuItemTeilnehmer.setActionCommand(MENU_ACTION_JOURNAL_TEILNEHMER);
		jmJournal.add(menuItemTeilnehmer);

		JMenuItem menuItemAuszulieferndePositionen = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.report.auszulieferndepositionen"));
		menuItemAuszulieferndePositionen.addActionListener(this);
		menuItemAuszulieferndePositionen.setActionCommand(MENU_ACTION_JOURNAL_AUSZULIEFERNDE_POSITIONEN);
		jmJournal.add(menuItemAuszulieferndePositionen);

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ERWEITERTE_PROJEKTSTEUERUNG)
				|| LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_KLEINE_PROJEKTSTEUERUNG)) {
			jmJournal.add(new JSeparator());

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ERWEITERTE_PROJEKTSTEUERUNG)) {

				JMenuItem menuItemMeilensteine = new JMenuItem(LPMain.getTextRespectUISPr("auft.report.meilensteine"));
				menuItemMeilensteine.addActionListener(this);
				menuItemMeilensteine.setActionCommand(MENU_ACTION_JOURNAL_MEILENSTEINE);
				jmJournal.add(menuItemMeilensteine);
			}

			JMenuItem menuItemPlanstunden = new JMenuItem(LPMain.getTextRespectUISPr("auft.report.planstunden"));
			menuItemPlanstunden.addActionListener(this);
			menuItemPlanstunden.setActionCommand(MENU_ACTION_JOURNAL_PLANSTUNDEN);
			jmJournal.add(menuItemPlanstunden);

			JMenuItem menuItemMaterialbedarfe = new JMenuItem(
					LPMain.getTextRespectUISPr("auft.report.materialbedarfe"));
			menuItemMaterialbedarfe.addActionListener(this);
			menuItemMaterialbedarfe.setActionCommand(MENU_ACTION_JOURNAL_MATERIALBEDARFE);
			jmJournal.add(menuItemMaterialbedarfe);

		}

		// Menue Info
		JMenu menuInfo = new WrapperMenu("lp.info", this);

		JMenuItem menuItemAuftragzeiten = new JMenuItem(LPMain.getTextRespectUISPr("auft.menu.datei.auftragzeiten"));
		menuItemAuftragzeiten.addActionListener(this);
		menuItemAuftragzeiten.setActionCommand(MENU_ACTION_INFO_AUFTRAGZEITEN);
		menuInfo.add(menuItemAuftragzeiten);

		if (LPMain.getInstance().getDesktop().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_HVMA2)) {

			JMenuItem menuItemZeitbestaetigung = new JMenuItem(
					LPMain.getTextRespectUISPr("auft.report.zeitbestaetigung"));
			menuItemZeitbestaetigung.addActionListener(this);
			menuItemZeitbestaetigung.setActionCommand(MENU_ACTION_INFO_ZEITBESTAETIGUNG);
			menuInfo.add(menuItemZeitbestaetigung);
		}

		if (getInternalFrame().bRechtDarfPreiseSehenVerkauf) {

			WrapperMenuItem menuItemNachkalkulation = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("auft.menu.datei.nachkalkulation"),
					RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
			menuItemNachkalkulation.addActionListener(this);
			menuItemNachkalkulation.setActionCommand(MENU_ACTION_INFO_NACHKALKULATION);
			menuInfo.add(menuItemNachkalkulation);
		}

		JMenuItem menuItemWiederbeschaffung = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.info.wiederbeschaffung"));
		menuItemWiederbeschaffung.addActionListener(this);
		menuItemWiederbeschaffung.setActionCommand(MENUE_ACTION_INFO_WIEDERBESCHAFFUNG);
		menuInfo.add(menuItemWiederbeschaffung);

		JMenuItem menuItemRollierendePlanung = new JMenuItem(LPMain.getTextRespectUISPr("auft.rollierendeplanung"));
		menuItemRollierendePlanung.addActionListener(this);
		menuItemRollierendePlanung.setActionCommand(MENUE_ACTION_INFO_ROLLIERENDEPLANUNG);
		menuInfo.add(menuItemRollierendePlanung);

		JMenuItem menuItemVerfuegbarkeitspruefung = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.menu.info.verfuegbarkeitspruefung"));
		menuItemVerfuegbarkeitspruefung.addActionListener(this);
		menuItemVerfuegbarkeitspruefung.setActionCommand(MENUE_ACTION_INFO_VERFUEGBARKEITSPRUEFUNG);
		menuInfo.add(menuItemVerfuegbarkeitspruefung);

		JMenuItem menuItemRahmenerfuelleung = new JMenuItem(
				LPMain.getTextRespectUISPr("auftrag.report.erfuellungsjournal"));
		menuItemRahmenerfuelleung.addActionListener(this);
		menuItemRahmenerfuelleung.setActionCommand(MENUE_ACTION_INFO_RAHMENERFUELLUNG);
		menuInfo.add(menuItemRahmenerfuelleung);

		JMenuItem menuItemRahmenuebersicht = new JMenuItem(
				LPMain.getTextRespectUISPr("auftrag.report.rahmenuebersicht"));
		menuItemRahmenuebersicht.addActionListener(this);
		menuItemRahmenuebersicht.setActionCommand(MENUE_ACTION_INFO_RAHMENUEBERSICHT);
		menuInfo.add(menuItemRahmenuebersicht);
		// 18766
		JMenuItem menuItemAuftragsubersicht = new JMenuItem(
				LPMain.getTextRespectUISPr("auft.auftrag.info.auftragsuebersicht"));
		menuItemAuftragsubersicht.addActionListener(this);
		menuItemAuftragsubersicht.setActionCommand(MENUE_ACTION_INFO_AUFTRAGSUEBERSICHT);
		menuInfo.add(menuItemAuftragsubersicht);

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ERWEITERTE_PROJEKTSTEUERUNG)
				|| LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_KLEINE_PROJEKTSTEUERUNG)) {
			JMenuItem menuItemProjektblatt = new JMenuItem(LPMain.getTextRespectUISPr("auft.report.projektblatt"));
			menuItemProjektblatt.addActionListener(this);
			menuItemProjektblatt.setActionCommand(MENUE_ACTION_INFO_PROJEKTBLATT);
			menuInfo.add(menuItemProjektblatt);
		}

		menuBar.addJMenuItem(menuInfo);

		if (bSchreibrecht) {

			if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1)
					&& DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER)
					&& DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_AUFT_AKTIVIEREN)) {
				JMenu menuPflege = new WrapperMenu("lp.pflege", this);

				JMenuItem menuItemWiederholendeErhoehen = new JMenuItem(
						LPMain.getTextRespectUISPr("auft.pflege.indexanpassung"));
				menuItemWiederholendeErhoehen.addActionListener(this);
				menuItemWiederholendeErhoehen.setActionCommand(MENUE_ACTION_PFLEGE_INDEXANPASSUNG);

				menuPflege.add(menuItemWiederholendeErhoehen);

				JMenuItem menuItemPreisgueltigkeit = new JMenuItem(
						LPMain.getTextRespectUISPr("auft.pflege.wiederholende.zurpreisgueltigkeit.aktualisieren"));
				menuItemPreisgueltigkeit.addActionListener(this);
				menuItemPreisgueltigkeit
						.setActionCommand(MENUE_ACTION_PFLEGE_WIEDERHOLENDE_PREISE_ZUR_PREISGUELTIGKEIT_ANPASSEN);

				menuPflege.add(menuItemPreisgueltigkeit);

				menuBar.addJMenuItem(menuPflege);
			}

		}

		return menuBar;
	}

	public void showStatusMessage(String lpTitle, String lpMessage) throws Throwable {

		MessageFormat mf = new MessageFormat(LPMain.getTextRespectUISPr(lpMessage));

		mf.setLocale(LPMain.getTheClient().getLocUi());

		Object pattern[] = { getAuftragStatus() };

		DialogFactory.showModalDialog(LPMain.getTextRespectUISPr(lpTitle), mf.format(pattern));
	}

	public void gotoAuswahl() throws Throwable {
		setSelectedComponent(auftragAuswahl);
		setTitleAuftrag(LPMain.getTextRespectUISPr("auft.title.panel.auswahl"));
	}

	public String getAuftragStatus() throws Throwable {
		return DelegateFactory.getInstance().getLocaleDelegate().getStatusCBez(getAuftragDto().getStatusCNr());
	}

	public boolean getLockStateEnableRefreshOnly() {
		boolean bLockStateEnableRefreshOnly = false;

		if (getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)
				|| getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)
				|| getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
			bLockStateEnableRefreshOnly = true;
		}

		return bLockStateEnableRefreshOnly;
	}

	private String getTitleDruck() {
		StringBuffer buff = new StringBuffer();

		buff.append(getAuftragDto().getCNr());
		buff.append(" ");
		buff.append(getKundeAuftragDto().getPartnerDto().getCName1nachnamefirmazeile1());

		return buff.toString();
	}

	public void setBKriterienAuftragUebersichtUeberMenueAufgerufen(boolean bAufgerufenI) {
		bKriterienAuftragUebersichtUeberMenueAufgerufen = bAufgerufenI;
	}

	private void enableTabSichtLieferstatus(boolean disableOnly) {
		if (!auftragExists())
			return;

		boolean enableTab = !AuftragServiceFac.AUFTRAGART_RAHMEN.equals(auftragDto.getAuftragartCNr());
		if (disableOnly && enableTab)
			return;

		setEnabledAt(IDX_PANEL_SICHTLIEFERSTATUS, enableTab);
	}

	private void enableTabSichtRahmen(boolean disableOnly) {
		if (!auftragExists())
			return;

		boolean enableTab = Helper.isOneOf(auftragDto.getAuftragartCNr(), AuftragServiceFac.AUFTRAGART_RAHMEN,
				AuftragServiceFac.AUFTRAGART_ABRUF);
		if (disableOnly && enableTab)
			return;

		setEnabledAt(IDX_PANEL_SICHTRAHMEN, enableTab);
	}

	private void enableTabAuftragKostenstellen(boolean disableOnly) {
		if (!auftragExists() || IDX_PANEL_AUFTRAGKOSTENSTELLEN < 0)
			return;

		boolean enableTab = Helper.isOneOf(auftragDto.getAuftragartCNr(), AuftragServiceFac.AUFTRAGART_FREI,
				AuftragServiceFac.AUFTRAGART_WIEDERHOLEND);
		if (disableOnly && enableTab)
			return;

		setEnabledAt(IDX_PANEL_AUFTRAGKOSTENSTELLEN, enableTab);
	}

	private void enableTabAuftraguebersicht(boolean disableOnly) {
		if (!auftragExists())
			return;

		boolean enableTab = bDarfPreiseSehen;
		if (disableOnly && enableTab)
			return;

		setEnabledAt(IDX_PANEL_AUFTRAGUEBERSICHT, enableTab);
	}

	private boolean auftragExists() {
		return auftragDto != null && auftragDto.getIId() != null;
	}

	public void enableTabs(boolean disableOnly) {
		enableTabSichtLieferstatus(disableOnly);
		enableTabSichtRahmen(disableOnly);
		enableTabAuftragKostenstellen(disableOnly);
		enableTabAuftraguebersicht(disableOnly);
	}

	public void disableOnlyTabs() {
		enableTabs(true);
	}

	/*
	 * public void showPanelDialogDivisor() throws Throwable { if (pdDivisor ==
	 * null) { pdDivisor = new PanelDialogAuftragDivisor( getInternalFrame(),
	 * LPMain.getTextRespectUISPr("bes.divisor")); }
	 * 
	 * this.setTitleAuftrag("bes.divisor");
	 * getInternalFrame().showPanelDialog(pdDivisor); }
	 */
	public void gotoSichtRahmen() throws Throwable {
		setSelectedComponent(refreshAuftragSichtRahmen());
		setEnabledAt(IDX_PANEL_SICHTRAHMEN, true);
		setTitleAuftrag(LPMain.getTextRespectUISPr("lp.sichtrahmen"));

		// alles richtig schalten, auch wenn die Liste der Abrufpositionen
		// vorher leer war
		auftragSichtRahmen.eventYouAreSelected(false); // refresh auf das
		// gesamte 1:n panel
	}

	public PanelAuftragKonditionen2 getAuftragKonditionen() {
		return auftragKonditionen;
	}

	/**
	 * belegartkonditionen: 6 Kopf- und Fusstext vorbelegen.
	 * 
	 * @throws Throwable Ausnahme
	 */
	public void initAuftragtexte() throws Throwable {

		kopftextDto = DelegateFactory.getInstance().getAuftragServiceDelegate()
				.getAufragkopfDefault(getKundeAuftragDto().getPartnerDto().getLocaleCNrKommunikation());
		fusstextDto = DelegateFactory.getInstance().getAuftragServiceDelegate()
				.getAufragfussDefault(getKundeAuftragDto().getPartnerDto().getLocaleCNrKommunikation());

	}

	// TODO-AGILCHANGES8
	/**
	 * AGILPRO CHANGES BEGIN
	 * 
	 * @author Lukas Lisowski
	 */

	/**
	 * @return the auftragPositionen
	 */
	public PanelSplit getPanelPositionen() {
		this.setSelectedIndex(IDX_PANEL_AUFTRAGPOSITIONEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.auftragPositionen;
	}

	/**
	 * @return the auftragSichtRahmen
	 */
	public PanelSplit getPanelSichtRahmen() {
		this.setSelectedIndex(IDX_PANEL_SICHTRAHMEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.auftragSichtRahmen;
	}

	/**
	 * @return the auftragTeilnehmer
	 */
	public PanelSplit getPanelTeilnehmer() {
		this.setSelectedIndex(IDX_PANEL_AUFTRAGTEILNEHMER);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.auftragTeilnehmer;
	}

	/**
	 * 
	 * @return com.lp.client.frame.component.PanelQuery
	 */
	public PanelQuery getPanelAuswahl() {
		this.setSelectedIndex(IDX_PANEL_AUFTRAGAUSWAHL);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return auftragAuswahl;
	}

	/**
	 * 
	 * @return com.lp.client.frame.component.PanelBasis
	 */
	public PanelAuftragKopfdaten getPanelKopfdaten() {
		this.setSelectedIndex(IDX_PANEL_AUFTRAGKOPFDATEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return auftragKopfdaten;
	}

	/**
	 * 
	 * @return com.lp.client.frame.component.PanelBasis
	 */
	public PanelBasis getPanelKonditionen() {
		this.setSelectedIndex(IDX_PANEL_AUFTRAGKONDITIONEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return auftragKonditionen;
	}

	/**
	 * AGILPRO CHANGES END
	 * 
	 * @return Object
	 */
	public Object getDto() {
		return auftragDto;
	}

	public void copyHV() throws ExceptionLP, Throwable {
		int selectedPanelIndex = this.getSelectedIndex();

		if (selectedPanelIndex == IDX_PANEL_AUFTRAGPOSITIONEN) {

			Object aoIIdPosition[] = this.auftragPositionenTop.getSelectedIds();

			if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
				AuftragpositionDto[] dtos = new AuftragpositionDto[aoIIdPosition.length];
				Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
				for (int i = 0; i < aIIdPosition.length; i++) {
					aIIdPosition[i] = (Integer) aoIIdPosition[i];
					dtos[i] = DelegateFactory.getInstance().getAuftragpositionDelegate()
							.auftragpositionFindByPrimaryKey((Integer) aoIIdPosition[i]);
				}

				if (getAuftragPositionenBottom().getArtikelsetViewController().validateCopyConstraintsUI(dtos)) {
					LPMain.getPasteBuffer().writeObjectToPasteBuffer(dtos);
				}
			}
		} else if (selectedPanelIndex == IDX_PANEL_AUFTRAGTEILNEHMER) {

			Object aoIIdPosition[] = this.auftragTeilnehmerTop.getSelectedIds();

			if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
				AuftragteilnehmerDto[] dtos = new AuftragteilnehmerDto[aoIIdPosition.length];
				Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
				for (int i = 0; i < aIIdPosition.length; i++) {
					aIIdPosition[i] = (Integer) aoIIdPosition[i];
					dtos[i] = DelegateFactory.getInstance().getAuftragteilnehmerDelegate()
							.auftragteilnehmerFindByPrimaryKey((Integer) aoIIdPosition[i]);
				}
				LPMain.getPasteBuffer().writeObjectToPasteBuffer(dtos);
			}
		}

	}

	public boolean warnungAuftragspositionOhneRahmenbezug(AuftragpositionDto auftragpositionDto) throws Throwable {
		if (auftragpositionDto.getIId() == null) {
			if (auftragpositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)
					|| auftragpositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
				if (getAuftragDto().getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_ABRUF)) {
					// einmalige Warnung aussprechen
					if (!getInternalFrameAuftrag().bWarnungAusgesprochen) {
						getInternalFrameAuftrag().bWarnungAusgesprochen = true;
						if (DialogFactory.showMeldung(
								LPMain.getTextRespectUISPr("lp.warning.reduziertnichtdieoffenemengeimrahmen"),
								LPMain.getTextRespectUISPr("lp.warning"),
								javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.NO_OPTION) {

							return false;
						}

					}
				}
			}

		}
		return true;
	}

	public void einfuegenHV() throws IOException, ExceptionLP, ParserConfigurationException, Throwable, SAXException {

		Object o = LPMain.getPasteBuffer().readObjectFromPasteBuffer();

		int selectedPanelIndex = this.getSelectedIndex();

		if (selectedPanelIndex == IDX_PANEL_AUFTRAGPOSITIONEN) {
			if (!getAuftragPositionenBottom().getArtikelsetViewController().validatePasteConstraintsUI(o)) {
				return;
			}

			if (o instanceof BelegpositionDto[]) {

				// SP4966
				if (getAuftragDto().getBestellungIIdAndererMandant() != null) {

					BestellungDto bsDto = DelegateFactory.getInstance().getBestellungDelegate()
							.bestellungFindByPrimaryKey(getAuftragDto().getBestellungIIdAndererMandant());

					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("auft.error.besinanderemmandanten.nurmehrmengeaenderbar"));
					Object[] pattern = new Object[] { bsDto.getCNr(), bsDto.getMandantCNr() };

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"), mf.format(pattern));
					return;

				}

				AuftragpositionDto[] positionDtos = DelegateFactory.getInstance().getBelegpostionkonvertierungDelegate()
						.konvertiereNachAuftragpositionDto((BelegpositionDto[]) o,
								getAuftragDto().getKundeIIdRechnungsadresse(), getAuftragDto().getTBelegdatum());

				if (positionDtos != null) {
					Integer iId = null;
					Boolean b = positionAmEndeEinfuegen();
					if (b != null) {
						for (int i = 0; i < positionDtos.length; i++) {
							AuftragpositionDto positionDto = positionDtos[i];
							try {
								positionDto.setIId(null);

								boolean bSpeichern = warnungAuftragspositionOhneRahmenbezug(positionDto);
								if (bSpeichern == false) {
									break;
								}

								// damits hinten angehaengt wird.
								if (b == false) {
									Integer iIdAktuellePosition = (Integer) getAuftragPositionenTop().getSelectedId();

									// die erste Position steht an der Stelle 1
									Integer iSortAktuellePosition = new Integer(1);

									if (iIdAktuellePosition != null) {
										AuftragpositionDto aktuellePositionDto = DelegateFactory.getInstance()
												.getAuftragpositionDelegate()
												.auftragpositionFindByPrimaryKey(iIdAktuellePosition);
										iSortAktuellePosition = aktuellePositionDto.getISort();

										// iSortAktuellePosition = DelegateFactory
										// .getInstance()
										// .getAuftragpositionDelegate()
										// .auftragpositionFindByPrimaryKey(
										// iIdAktuellePosition)
										// .getISort();

										// poseinfuegen: 4 Die bestehenden
										// Positionen
										// muessen Platz fuer die neue schaffen
										DelegateFactory.getInstance().getAuftragpositionDelegate()
												.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
														getAuftragDto().getIId(), iSortAktuellePosition.intValue());

										getAuftragPositionenBottom().getArtikelsetViewController()
												.setArtikelSetIIdFuerNeuePosition(
														aktuellePositionDto.getPositioniIdArtikelset());
										// poseinfuegen: 5 Die neue Position wird an
										// frei
										// gemachte Position gesetzt
										positionDto.setISort(iSortAktuellePosition);
									} else {
										positionDto.setISort(null);
									}

								} else {
									positionDto.setISort(null);
								}

								positionDto.setBelegIId(this.getAuftragDto().getIId());

								// Positionstermin auf Liefertermin setzen, wenn sie
								// nicht gleich sind.
								if (positionDto.getTUebersteuerbarerLiefertermin() == null) {
									positionDto
											.setTUebersteuerbarerLiefertermin(this.getAuftragDto().getDLiefertermin());
								}

								// wir legen eine neue position an
								ArtikelsetViewController viewController = getAuftragPositionenBottom()
										.getArtikelsetViewController();

								boolean bDiePositionSpeichern = viewController
										.validateArtikelsetConstraints(positionDto);

								if (bDiePositionSpeichern) {
									positionDto.setPositioniIdArtikelset(
											viewController.getArtikelSetIIdFuerNeuePosition());
									Integer newIId = DelegateFactory.getInstance().getAuftragpositionDelegate()
											.createAuftragposition(positionDto);
									positionDto.setIId(newIId);
									if (iId == null) {
										iId = newIId;
									}
								}
							} catch (Throwable t) {
								// nur loggen!
								myLogger.error(t.getMessage(), t);
							}
						}
					}

					ZwsEinfuegenHVAuftragposition cpp = new ZwsEinfuegenHVAuftragposition();
					cpp.processZwsPositions(positionDtos, (BelegpositionDto[]) o);
					// processZwsPositions(positionDtos, (BelegpositionDto[]) o) ;

					// die Liste neu aufbauen
					auftragPositionenTop.eventYouAreSelected(false);
					// den Datensatz in der Liste selektieren
					auftragPositionenTop.setSelectedId(iId);
					// im Detail den selektierten anzeigen
					auftragPositionenBottom.eventYouAreSelected(false);
				}

			}
		} else if (selectedPanelIndex == IDX_PANEL_AUFTRAGTEILNEHMER) {
			// Es koennen nur SpeiseplanDtos verarbeitet werden
			if (o instanceof AuftragteilnehmerDto[]) {
				AuftragteilnehmerDto[] positionen = (AuftragteilnehmerDto[]) o;
				for (int i = 0; i < positionen.length; i++) {

					AuftragteilnehmerDto atDto = positionen[i];

					if(! DelegateFactory.getInstance().getAuftragteilnehmerDelegate()
							.istPartnerEinAuftragteilnehmer(atDto.getPartnerIIdAuftragteilnehmer(), getAuftragDto().getIId())) {
						atDto.setIId(null);
						atDto.setAuftragIId(getAuftragDto().getIId());

						DelegateFactory.getInstance().getAuftragteilnehmerDelegate().createAuftragteilnehmer(atDto);
					}

			
				
				}

				auftragTeilnehmerTop.eventYouAreSelected(false);

			}
		}
	}

	// private void processZwsPositions(AuftragpositionDto[] positionsDto,
	// BelegpositionDto[] source) throws ExceptionLP {
	// for(int i = 0; i < source.length; i++) {
	// if(!AuftragServiceFac
	// .AUFTRAGPOSITIONART_INTELLIGENTE_ZWISCHENSUMME.equals(source[i].getPositionsartCNr()))
	// continue ;
	// Integer vonId = ((BelegpositionVerkaufDto)source[i]).getZwsVonPosition()
	// ;
	// if(vonId == null) continue ;
	// Integer vonIndex = findArrayIndexFor(vonId, source) ;
	// if(vonIndex == null) continue ;
	//
	// positionsDto[i].setZwsVonPosition(positionsDto[vonIndex].getIId()) ;
	//
	// Integer bisId = ((BelegpositionVerkaufDto)source[i]).getZwsBisPosition()
	// ;
	// if(bisId == null) continue ;
	// Integer bisIndex = findArrayIndexFor(bisId, source) ;
	// if(bisIndex == null) continue ;
	//
	// positionsDto[i].setZwsBisPosition(positionsDto[bisIndex].getIId()) ;
	//
	// DelegateFactory.getInstance().getAuftragpositionDelegate().updateAuftragpositionOhneWeitereAktion(positionsDto[i])
	// ;
	// }
	// }
	//
	// private Integer findArrayIndexFor(Integer iid, BelegpositionDto[] source)
	// {
	// for(int i = 0; i < source.length; i++) {
	// if(source[i].getIId().equals(iid)) return i ;
	// }
	// return null ;
	// }

	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfBelegPosI) throws Throwable {
		AuftragpositionDto abPosDto = (AuftragpositionDto) belegposDtoI;

		abPosDto.setBelegIId(auftragDto.getIId());
		abPosDto.setISort(xalOfBelegPosI + 1000);
	}

	private void importCSV_SON() throws Throwable {
		HvOptional<CsvFile> csvFile = FileOpenerFactory.auftragSONImportCsv(this);
		if (csvFile.isPresent()) {

			List<String[]> al = csvFile.get().read();
			if (al.size() > 1) {
				LinkedHashMap<String, ArrayList<ImportSonCsvDto>> hm = new LinkedHashMap<String, ArrayList<ImportSonCsvDto>>();

				for (int i = 1; i < al.size(); i++) {

					String[] felder = al.get(i);

					if (felder.length > 18) {

						String bestellnummer = felder[11];

						if (bestellnummer == null) {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("auft.import.son.error.keine.bestellnummer"));
							break;
						}

						ArrayList<ImportSonCsvDto> alZeilenCsv = new ArrayList<ImportSonCsvDto>();
						if (hm.containsKey(bestellnummer)) {
							alZeilenCsv = hm.get(bestellnummer);
						}

						// Kunde Bestellnummer und Liefertermin
						ImportSonCsvDto zeileDto = new ImportSonCsvDto();

						zeileDto.setAbladestelle(felder[8]);

						zeileDto.setBestelltyp(felder[4]);

						zeileDto.setArtikelnummer(felder[13]);

						zeileDto.setBezeichnung(felder[15]);
						zeileDto.setLiefertermin(felder[17]);
						zeileDto.setMenge(felder[18]);

						alZeilenCsv.add(zeileDto);

						hm.put(bestellnummer, alZeilenCsv);
					} else {

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("auft.import.son.error.anzahl.spalten"));
						break;
					}

				}

				DelegateFactory.getInstance().getAuftragDelegate().importiereSON_CSV(hm);

				getPanelAuswahl().eventYouAreSelected(false);

			}
		}
	}

	private void importCSV_WooCommerce() throws Throwable {
		HvOptional<CsvFile> csvFile = FileOpenerFactory.auftragWooCommerceImportCsv(this);
		if (csvFile.isPresent()) {

			InputStreamReader input = new InputStreamReader(new FileInputStream(csvFile.get().getFile()),
					StandardCharsets.UTF_8.name());
			CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(input);
			int iZeile = 1;

			LinkedHashMap<String, ArrayList<ImportWooCommerceCsvDto>> hm = new LinkedHashMap<String, ArrayList<ImportWooCommerceCsvDto>>();

			for (CSVRecord record : csvParser) {

				iZeile++;
				String bestellnummer;
				boolean bEnglisch = true;
				try {
					bestellnummer = record.get("Order Number");
				} catch (IllegalArgumentException e) {
					bestellnummer = record.get("Bestellnummer");
					bEnglisch = false;
				}

				if (bestellnummer == null) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("auft.import.woocommerce.error.keine.bestellnummer"));
					break;
				}

				ArrayList<ImportWooCommerceCsvDto> alZeilenCsv = new ArrayList<ImportWooCommerceCsvDto>();
				if (hm.containsKey(bestellnummer)) {
					alZeilenCsv = hm.get(bestellnummer);
				}

				ImportWooCommerceCsvDto zeileDto = new ImportWooCommerceCsvDto();

				zeileDto.bestellnummer = bestellnummer;

				if (bEnglisch) {
					zeileDto.email = record.get("Email (Billing)");
					zeileDto.artikelnummer = record.get("SKU");
					zeileDto.bruttopreis = record.get("Order Total Amount").replaceAll(",", ".");

					zeileDto.artikelpreis = record.get("Item Cost").replaceAll(",", ".");

					zeileDto.gesamterbelegbetrag = record.get("Order Total Amount");

					zeileDto.versandkosten = record.get("Order Shipping Amount").replaceAll(",", ".");

					zeileDto.menge = record.get("Quantity");
					zeileDto.belegdatum = record.get("Order Date");

					zeileDto.kommentar = record.get("Customer Note");

					zeileDto.lieferart = record.get("Shipping Method Title");
					zeileDto.zahlungsziel = record.get("Payment Method Title");

					String anrede = record.get("Title");

					if (anrede != null && anrede.equals("1")) {
						anrede = PartnerFac.PARTNER_ANREDE_HERR;
					} else if (anrede != null && anrede.equals("2")) {
						anrede = PartnerFac.PARTNER_ANREDE_FRAU;
					} else {
						anrede = null;
					}

					zeileDto.anrede = anrede;

					zeileDto.rechnungsadresseFirma = record.get("Company (Billing)");
					zeileDto.rechnungsadresseName1 = record.get("Last Name (Billing)");
					zeileDto.rechnungsadresseName2 = record.get("First Name (Billing)");
					zeileDto.rechnungsadresseStrasse = record.get("Address 1&2 (Billing)");
					zeileDto.rechnungsadresseLand = record.get("Country Code (Billing)");
					zeileDto.rechnungsadressePLZ = record.get("Postcode (Billing)");
					zeileDto.rechnungsadresseOrt = record.get("City (Billing)");

					zeileDto.rechnungsadresseTelefon = record.get("Phone (Billing)");

					zeileDto.lieferadresseName1 = record.get("Last Name (Shipping)");
					zeileDto.lieferadresseName2 = record.get("First Name (Shipping)");
					zeileDto.lieferadresseStrasse = record.get("Address 1&2 (Shipping)");
					zeileDto.lieferadresseLand = record.get("Country Code (Shipping)");
					zeileDto.lieferadressePLZ = record.get("Postcode (Shipping)");
					zeileDto.lieferadresseOrt = record.get("City (Shipping)");
				} else {
					zeileDto.email = record.get("E-Mail (Abrechnung)");
					zeileDto.artikelnummer = record.get("Artikelnummer");
					zeileDto.bruttopreis = record.get("Gesamtbetrag der Bestellung").replaceAll(",", ".");

					zeileDto.artikelpreis = record.get("Gegenstandskosten").replaceAll(",", ".");

					zeileDto.gesamterbelegbetrag = record.get("Gesamtbetrag der Bestellung");

					zeileDto.versandkosten = record.get("Versandbetrag bestellen").replaceAll(",", ".");

					zeileDto.menge = record.get("Anzahl");
					zeileDto.belegdatum = record.get("Auftragsdatum");

					zeileDto.kommentar = record.get("Kundenhinweis");

					zeileDto.lieferart = record.get("Versandart Titel");
					zeileDto.zahlungsziel = record.get("Zahlungsmethode Titel");

					String anrede = record.get("Anrede");

					if (anrede != null && anrede.equals("1")) {
						anrede = PartnerFac.PARTNER_ANREDE_HERR;
					}
					if (anrede != null && anrede.equals("2")) {
						anrede = PartnerFac.PARTNER_ANREDE_HERR;
					} else {
						anrede = null;
					}

					zeileDto.anrede = anrede;

					zeileDto.rechnungsadresseFirma = record.get("Firma (Fakturierung)");
					zeileDto.rechnungsadresseName1 = record.get("Vorname (Abrechnung)");
					zeileDto.rechnungsadresseName2 = record.get("Nachname (Abrechnung)");
					zeileDto.rechnungsadresseStrasse = record.get("Adresse 1 & 2 (Abrechnung)");
					zeileDto.rechnungsadresseLand = record.get("L\u00E4ndercode (Abrechnung)");
					zeileDto.rechnungsadressePLZ = record.get("Postleitzahl (Abrechnung)");
					zeileDto.rechnungsadresseOrt = record.get("Stadt (Abrechnung)");

					zeileDto.rechnungsadresseTelefon = record.get("Telefon (Abrechnung)");

					zeileDto.lieferadresseName1 = record.get("Vorname (Versand)");
					zeileDto.lieferadresseName2 = record.get("Nachname (Versand)");
					zeileDto.lieferadresseStrasse = record.get("Adresse 1 & 2 (Versand)");
					zeileDto.lieferadresseLand = record.get("L\u00E4ndercode (Versand)");
					zeileDto.lieferadressePLZ = record.get("Postleitzahl (Versand)");
					zeileDto.lieferadresseOrt = record.get("Stadt (Versand)");
				}

				zeileDto.zeile = iZeile;

				alZeilenCsv.add(zeileDto);

				hm.put(bestellnummer, alZeilenCsv);

			}

			DelegateFactory.getInstance().getAuftragDelegate().importiereWooCommerce_CSV(hm);

			getPanelAuswahl().eventYouAreSelected(false);

		}
	}

	private void importXLSX_VAT(Integer kundeIId, Integer ansprechpartnerIId) throws Throwable {

		HvOptional<XlsxFile> xlsxFile = FileOpenerFactory.auftragVATImportXlsx(this);
		if (xlsxFile.isPresent()) {

			final int BESTELLNUMMER = 0;
			final int ARTIKELNUMMER = 4;
			final int LIEFERTERMIN = 11;
			final int WAEHRUNG = 7;
			final int MENGE = 10;
			final int PREIS = 6;
			final int LIEFERADRESSE = 35;
			final int BESTELLNUMMER_TEIL2 = 12;
			final int ZUSATZBEZEICHNUNG = 5;
			final int BEST_TEXT = 13;

			LinkedHashMap<String, ArrayList<ImportVATXlsxDto>> hm = new LinkedHashMap<String, ArrayList<ImportVATXlsxDto>>();

			// Finds the workbook instance for XLSX file
			XSSFWorkbook myWorkBook = xlsxFile.get().createWorkbook();

			// Return first sheet from the XLSX workbook
			XSSFSheet mySheet = myWorkBook.getSheetAt(0);

			// Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = mySheet.iterator();

			// Traversing over each row of XLSX file

			int iZeile = 0;

			// Ueberschrift ueberspringen
			if (rowIterator.hasNext()) {
				iZeile++;
				rowIterator.next();
			}

			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();

				iZeile++;

				// For each row, iterate through each columns

				String bestellnummer = null;
				String artikelnummer = null;
				String waehrung = null;
				String lieferadresse = null;
				String bestellnummer_teil2 = null;
				String zusatzbezeichnung = null;

				java.sql.Timestamp liefertermin = null;
				BigDecimal bdPreis = null;
				BigDecimal bdMenge = null;
				String bestText = null;

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					if (cell.getColumnIndex() == BESTELLNUMMER && cell.getCellType() == CellType.STRING) {
						bestellnummer = cell.getStringCellValue();
					} else if (cell.getColumnIndex() == ARTIKELNUMMER && cell.getCellType() == CellType.STRING) {
						artikelnummer = cell.getStringCellValue();
					} else if (cell.getColumnIndex() == WAEHRUNG && cell.getCellType() == CellType.STRING) {
						waehrung = cell.getStringCellValue();
					} else if (cell.getColumnIndex() == LIEFERADRESSE && cell.getCellType() == CellType.STRING) {
						lieferadresse = cell.getStringCellValue();
					} else if (cell.getColumnIndex() == BEST_TEXT && cell.getCellType() == CellType.STRING) {
						bestText = cell.getStringCellValue();
					} else if (cell.getColumnIndex() == BESTELLNUMMER_TEIL2 && cell.getCellType() == CellType.STRING) {
						bestellnummer_teil2 = cell.getStringCellValue();
					} else if (cell.getColumnIndex() == ZUSATZBEZEICHNUNG && cell.getCellType() == CellType.STRING) {
						zusatzbezeichnung = cell.getStringCellValue();
					}

					else if (cell.getColumnIndex() == LIEFERTERMIN && cell.getCellType() == CellType.NUMERIC
							&& HSSFDateUtil.isCellDateFormatted(cell)) {
						liefertermin = new Timestamp(cell.getDateCellValue().getTime());
					} else if (cell.getColumnIndex() == MENGE && cell.getCellType() == CellType.NUMERIC) {
						bdMenge = new BigDecimal(cell.getNumericCellValue());
					} else if (cell.getColumnIndex() == PREIS && cell.getCellType() == CellType.NUMERIC) {
						bdPreis = new BigDecimal(cell.getNumericCellValue());
					}

				}

				ArrayList<ImportVATXlsxDto> alZeilenXlsx = new ArrayList<ImportVATXlsxDto>();
				if (hm.containsKey(bestellnummer)) {
					alZeilenXlsx = hm.get(bestellnummer);
				}

				ImportVATXlsxDto zeileDto = new ImportVATXlsxDto();
				zeileDto.setZeile(iZeile);
				zeileDto.setArtikelnummer(artikelnummer);
				zeileDto.setBest_text(bestText);
				zeileDto.setLiefertermin(liefertermin);
				zeileDto.setLieferadresse(lieferadresse);

				zeileDto.setPreis(bdPreis);
				zeileDto.setMenge(bdMenge);

				zeileDto.setWaehrung(waehrung);
				zeileDto.setBestellnummer(bestellnummer + "/" + bestellnummer_teil2);
				zeileDto.setZusatzbezeichnung(zusatzbezeichnung);

				alZeilenXlsx.add(zeileDto);

				hm.put(bestellnummer, alZeilenXlsx);

			}
			DelegateFactory.getInstance().getAuftragDelegate().importiereVAT_XLSX(kundeIId, ansprechpartnerIId, hm);
			getPanelAuswahl().eventYouAreSelected(false);
		}
	}

	private void importCSVAuftragPositionen() throws Throwable {
		HvOptional<CsvFile> csvFile = FileOpenerFactory.auftragPositionImportCsv(this);
		if (csvFile.isPresent()) {
			List<String[]> al = csvFile.get().read();
			if (al.size() > 0) {
				StringBuffer err = new StringBuffer();

				Timestamp belegDatum = HelperTimestamp.belegDatum(auftragDto);
				MwstsatzDto mwstsatzDto = DelegateFactory.getInstance().getMandantDelegate()
						.mwstsatzFindZuDatum(kundeAuftragDto.getMwstsatzbezIId(), belegDatum);

				for (int i = 1; i < al.size(); i++) {
					try {
						AuftragpositionDto apo = new AuftragpositionDto();
						apo.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);
						apo.setBelegIId(auftragDto.getIId());
						String[] as = al.get(i);
						ArtikelDto artikelDto = null;
						try {
							artikelDto = DelegateFactory.getInstance().getArtikelDelegate().artikelFindByCNr(as[0]);
						} catch (ExceptionLP e) {
							err.append("Zeile " + i + ": " + "Artikel " + as[0] + " nicht gefunden.\r\n");
						}
						if (artikelDto != null) {
							apo.setArtikelIId(artikelDto.getIId());
							apo.setEinheitCNr(artikelDto.getEinheitCNr());

							/*
							 * MwstsatzDto mwstsatzDto = DelegateFactory.getInstance().getMandantDelegate()
							 * .mwstsatzFindByMwstsatzbezIIdAktuellster(kundeAuftragDto.getMwstsatzbezIId())
							 * ;
							 */
							apo.setMwstsatzIId(mwstsatzDto.getIId());
							apo.setNMenge(new BigDecimal(as[1]));
							apo.setNOffeneMenge(new BigDecimal(as[1]));
							apo.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);

							apo.setTUebersteuerbarerLiefertermin(auftragDto.getDLiefertermin());

							if (as.length > 1 && as[2] != null && as[2].length() > 0) {
								// Datum
								try {
									SimpleDateFormat sdfToDate = new SimpleDateFormat("dd.MM.yyyy");
									java.util.Date date1 = sdfToDate.parse(as[2]);
									apo.setTUebersteuerbarerLiefertermin(new Timestamp(date1.getTime()));
								} catch (ParseException ex2) {
									err.append("Zeile " + i + ": " + "Datum " + as[1]
											+ " muss Format dd.MM.yyyy haben.\r\n");
								}

							}
							apo.setNMenge(new BigDecimal(as[1]));

							PanelPositionenArtikelVerkauf ppav = (PanelPositionenArtikelVerkauf) this.auftragPositionenBottom.panelArtikel;
							ppav.setArtikelDto(artikelDto);
							ppav.berechneVerkaufspreis(mwstsatzDto.getIId(), new Double(1), false);
							// VkpreisfindungDto vkpfDto =
							// ppav.getVkpreisfindungDto();
							VerkaufspreisDto vkDto = ppav.verkaufspreisDtoInZielwaehrung;
							apo.setNNettoeinzelpreis(vkDto.nettopreis);
							apo.setNBruttoeinzelpreis(vkDto.bruttopreis);
							apo.setNEinzelpreis(vkDto.einzelpreis);
							apo.setFRabattsatz(vkDto.rabattsatz);
							apo.setNMaterialzuschlag(vkDto.bdMaterialzuschlag);

							apo.setNMwstbetrag(vkDto.mwstsumme);
							apo.setNRabattbetrag(vkDto.rabattsumme);

							apo.setFZusatzrabattsatz(vkDto.getDdZusatzrabattsatz());
							apo.setBNettopreisuebersteuert(Helper.boolean2Short(false));
							apo.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
							apo.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));

							DelegateFactory.getInstance().getAuftragpositionDelegate().createAuftragposition(apo);
						}
					} catch (ExceptionLP e1) {
						err.append("Zeile " + i + ": " + e1.getSMsg() + " / " + e1.getMessage() + "\n\r");
					} catch (Exception e2) {
						err.append("Zeile " + i + ": " + e2.getMessage() + "\n\r");
					}
				}
				if (err.length() > 0) {
					JOptionPane.showMessageDialog(this, err, "Fehler beim Import", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, "Daten erfolgreich importiert", "Auftragsposition-Import",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}
}
