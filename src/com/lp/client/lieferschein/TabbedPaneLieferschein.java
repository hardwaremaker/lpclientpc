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
package com.lp.client.lieferschein;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableModel;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.xml.sax.SAXException;

import com.lp.client.angebot.AngebotFilterFactory;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.auftrag.PanelDialogAuftragKommentar;
import com.lp.client.forecast.ForecastFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.DialogImportAllgemein;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.ICopyPaste;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ArtikelsetViewController;
import com.lp.client.frame.component.DialogGeaenderteKonditionenVK;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionenArtikelVerkauf;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.TabbedPaneBasis;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.open.CsvFile;
import com.lp.client.frame.filechooser.open.FileOpenerFactory;
import com.lp.client.frame.filechooser.open.XmlFile;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.DialogDatumseingabe;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.HelperTimestamp;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.ImportAmazonCsvDto;
import com.lp.server.auftrag.service.ImportShopifyCsvDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinServiceFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.lieferschein.service.LieferscheintextDto;
import com.lp.server.lieferschein.service.VerkettetDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.HvOptional;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/*
 * <p>TabbedPane fuer Modul Lieferschein.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Uli Walch; 28.10.04</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version $Revision: 1.49 $ Date $Date: 2013/02/08 08:35:50 $
 */
public class TabbedPaneLieferschein extends TabbedPaneBasis implements ICopyPaste {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery lsAuswahl = null; // FLR Liste
	private PanelBasis lsKopfdaten = null;
	private PanelBasis lsKonditionen = null;

	private PanelBasis lsVersandinfos = null;

	private PanelQuery lsPositionenTop = null; // FLR 1:n Liste
	private PanelLieferscheinPositionen lsPositionenBottom = null;
	private PanelSplit lsPositionen = null;

	private PanelQuery lsAuftraege = null; // FLR 1:n Liste

	private PanelQuery lsPositionenSichtAuftragTop = null; // FLR 1:n Liste
	private PanelLieferscheinPositionenSichtAuftrag lsPositionenSichtAuftragBottom = null;
	private PanelSplit lsPositionenSichtAuftrag = null;

	private PanelQuery lsVerkettetTop = null; // FLR 1:n Liste
	private PanelVerkettet lsVerkettetBottom = null;
	private PanelSplit lsVerkettetSplit = null;

	// ptclient: 0 Die Panels deklarieren
	private PanelDialogKriterienLieferscheinUebersicht pdKriterienLsUebersicht = null;
	private PanelTabelleLieferscheinUebersicht ptLieferscheinUebersicht = null;

	private PanelDialogKriterienLieferscheinUmsatz pdKriterienLsUmsatz = null;
	private boolean bKriterienLsUmsatzUeberMenueAufgerufen = false;
	private PanelTabelleLieferscheinUmsatz ptLieferscheinUmsatz = null;

	public static int IDX_PANEL_LIEFERSCHEINAUSWAHL = -1;
	public static int IDX_PANEL_LIEFERSCHEINKOPFDATEN = -1;
	public static int IDX_PANEL_LIEFERSCHEINPOSITIONEN = -1;
	private int IDX_PANEL_LIEFERSCHEINAUFTRAEGE = -1;
	private int IDX_PANEL_LIEFERSCHEINPOSITIONENSICHTAUFTRAG = -1;
	private int IDX_PANEL_LIEFERSCHEINKONDITIONEN = -1;
	private int IDX_PANEL_VERSANDINFOS = -1;
	private int IDX_PANEL_LIEFERSCHEINUMSATZ = -1;
	private int IDX_PANEL_VERKETTET = -1;

	// Bitmuster fuer das Enable der Panels
	private boolean[] aPanelsEnabled = new boolean[6];

	// dtos, die in mehr als einem panel benoetigt werden
	private LieferscheinDto lieferscheinDto = null;
	private KundeDto kundeLieferadresseDto = null;
	private LieferscheintextDto kopftextDto = null;
	private LieferscheintextDto fusstextDto = null;
	private AuftragDto auftragDtoSichtAuftrag = null;

	private PanelDialogLieferscheinKommentar pdLieferscheinInternerKommentar = null;
	
	private PanelQueryFLR panelQueryFLRMandant = null;
	private PanelQueryFLR panelQueryFLRRechnungsadresseauswahl = null;
	private PanelQueryFLR panelQueryFLRLieferschein = null;
	private PanelQueryFLR panelQueryFLRLieferscheinVerketten = null;
	private PanelQueryFLR panelQueryFLRForecastpositionen = null;

	private PanelQueryFLR panelQueryFLRLager_Amazon = null;
	private PanelQueryFLR panelQueryFLRLager_Shopify = null;

	private final String MENU_ACTION_DATEI_LIEFERSCHEIN = "MENU_ACTION_DATEI_LIEFERSCHEIN";
	private final String MENU_ACTION_DATEI_LIEFERSCHEINETIKETT = "MENU_ACTION_DATEI_LIEFERSCHEINETIKETT";
	private final String MENU_ACTION_DATEI_LIEFERSCHEINWAETIKETT = "MENU_ACTION_DATEI_LIEFERSCHEINWAETIKETT";
	private final String MENU_ACTION_DATEI_VERSANDETIKETTEN = "MENU_ACTION_DATEI_VERANDETIKETTEN";
	private final String MENU_ACTION_DATEI_IMPORT_EASYDATA_STOCK_MOVEMENT = "MENU_ACTION_DATEI_IMPORT_EASYDATA_STOCK_MOVEMENT";
	private final String MENU_ACTION_DATEI_PLC_VERSANDETIKETTEN = "MENU_ACTION_DATEI_PLC_VERSANDETIKETTEN";

	private final String MENU_ACTION_JOURNAL_ANGELEGT = "MENU_ACTION_JOURNAL_ANGELEGT";
	private final String MENU_ACTION_JOURNAL_OFFEN = "MENU_ACTION_JOURNAL_OFFEN";
	private final String MENU_ACTION_JOURNAL_OFFENE_RUECKGABEN = "MENU_ACTION_JOURNAL_OFFENE_RUECKGABEN";
	private final String MENU_ACTION_JOURNAL_ALLE = "MENU_ACTION_JOURNAL_ALLE";
	private final String MENU_ACTION_JOURNAL_UEBERSICHT = // ptclient: 2 Menue
			// Action definieren
			"MENU_ACTION_JOURNAL_UEBERSICHT";
	private final String MENU_ACTION_JOURNAL_UMSATZUEBERSICHT = "MENU_ACTION_JOURNAL_UMSATZUEBERSICHT";
	private final String MENU_ACTION_JOURNAL_PACKSTUECKE = "MENU_ACTION_JOURNAL_PACKSTUECKE";

	private final String MENU_BEARBEITEN_MANUELL_ERLEDIGEN = "MENU_BEARBEITEN_MANUELL_ERLEDIGEN";
	private final String MENU_BEARBEITEN_BEGRUENDUNG = "MENU_BEARBEITEN_BEGRUENDUNG";
	private final String MENU_BEARBEITEN_FUELLE_FEHLEMGEN_DES_ANDEREN_MANDANTEN_NACH = "MENU_BEARBEITEN_FUELLE_FEHLEMGEN_DES_ANDEREN_MANDANTEN_NACH";

	private final String MENU_BEARBEITEN_RE_ADRESSE_AENDERN = "MENU_BEARBEITEN_RE_ADRESSE_AENDERN";

	private final String MENU_BEARBEITEN_INTERNER_KOMMENTAR = "MENU_BEARBEITEN_INTERNER_KOMMENTAR";
	
	// diese Action sitzt auf dem extra Neu Knopf
	public final static String EXTRA_NEU_AUS_AUFTRAG = "aus_auftrag";
	public final static String EXTRA_NEU_AUS_LIEFERSCHEIN = "aus_lieferschein";
	public final static String EXTRA_NEU_AUS_ANGEBOT = "aus_angebot";
	public final static String EXTRA_NEU_ALLE_POSITIONEN_AUS_AUFTRAG_UEBERNEHMEN = "alle_positionen_aus_auftrag_uebernehmen";

	public final static String MY_OWN_NEW_AUS_LIEFERSCHEIN = PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_LIEFERSCHEIN;

	public final static String MY_OWN_NEW_LIEFERAVISO_BARCODE = PanelBasis.ACTION_MY_OWN_NEW + "lieferaviso_barcode";

	public final static String MY_OWN_NEW_AUS_AUFTRAG = PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_AUFTRAG;
	public final static String MY_OWN_NEW_AUS_ANGEBOT = PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_ANGEBOT;

	public final static String MY_OWN_NEW_POSITION_AUS_FORECAST = PanelBasis.ACTION_MY_OWN_NEW
			+ "POSITION_AUS_FORECAST";

	public final static String MY_OWN_NEW_ALLE_POSITIONEN_AUS_AUFTRAG_UEBERNEHMEN = PanelBasis.ACTION_MY_OWN_NEW_ENABLED_ON_MULTISELECT
			+ EXTRA_NEU_ALLE_POSITIONEN_AUS_AUFTRAG_UEBERNEHMEN;

	public final static String EXTRA_NEU_MEHRERE_LIEFERSCHEINE_VERKETTEN = "mehrere_lieferscheine_verketten";
	public final static String MY_OWN_NEW_MEHRERE_LIEFERSCHEINE_VERKETTEN = PanelBasis.ACTION_MY_OWN_NEW
			+ EXTRA_NEU_MEHRERE_LIEFERSCHEINE_VERKETTEN;

	public final static String MY_OWN_NEW_SNRBARCODEERFASSUNG = PanelBasis.ACTION_MY_OWN_NEW + "SNRBARCODEERFASSUNG";

	private static final String ACTION_SPECIAL_SORTIERE_LS_NACH_AUFTRAGSNUMMER = "ACTION_SPECIAL_SORTIERE_LS_NACH_AUFTRAGSNUMMER";
	private final String BUTTON_SORTIERE_LS_NACH_AUFTRAGSNUMMER = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_SORTIERE_LS_NACH_AUFTRAGSNUMMER;

	private final String BUTTON_SORTIERE_LS_NACH_ARTIKELNUMMER = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_SORTIERE_LS_NACH_ARTIKELNUMMER";

	private static final String ACTION_SPECIAL_SCHNELLERFASSUNG_LSPOSITIONEN = "ACTION_SPECIAL_SCHNELLERFASSUNG_LSPOSITIONEN";
	private final String BUTTON_SCHNELLERFASSUNG_LIEFERSCHEINPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_SCHNELLERFASSUNG_LSPOSITIONEN;

	private static final String ACTION_SPECIAL_PREISE_NEU_KALKULIEREN = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_PREISE_NEU_KALKULIEREN";

	private PanelQueryFLR panelQueryFLRAuftragauswahl = null;
	private PanelQueryFLR panelQueryFLRAuftragauswahlZusatz = null;
	private PanelQueryFLR panelQueryFLRAngebotauswahl = null;

	private PanelQueryFLR panelQueryFLRBegruendung = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner_Rechungsadresse = null;

	private final boolean bSammellieferschein;
	private Boolean bZusatzfunktion4Vending;

	private static final String ACTION_SPECIAL_CSVIMPORT_SHOPIFY = "ACTION_SPECIAL_CSVIMPORT_SHOPIFY";
	private static final String ACTION_SPECIAL_CSVIMPORT_AMAZON = "ACTION_SPECIAL_CSVIMPORT_AMAZON";

	public TabbedPaneLieferschein(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("ls.modulname"));
		// Sammellieferschein aktiviert?
		bSammellieferschein = LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_SAMMELLIEFERSCHEIN);
		jbInit();
		initComponents();
	}

	// PJ20146
	public boolean lieferscheinGehtAnEinenAnderenMandanten() throws Throwable {

		if (LPMain.getInstance().getDesktopController()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {

			Integer partnerIId_Lieferadresse = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(getLieferscheinDto().getKundeIIdLieferadresse()).getPartnerIId();

			MandantDto[] dtos = DelegateFactory.getInstance().getMandantDelegate().mandantFindAll();

			if (dtos != null && dtos.length > 0) {

				for (int i = 0; i < dtos.length; i++) {

					String mandantCNr = dtos[i].getCNr();

					if (!mandantCNr.equals(LPMain.getTheClient().getMandant())) {
						// SP7745
						if (dtos[i].getPartnerIId().equals(partnerIId_Lieferadresse)
								|| (dtos[i].getPartnerIIdLieferadresse() != null
										&& dtos[i].getPartnerIIdLieferadresse().equals(partnerIId_Lieferadresse))) {

							boolean b = DelegateFactory.getInstance().getMandantDelegate()
									.darfAnwenderAufZusatzfunktionZugreifen(
											MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, mandantCNr);

							if (b == true) {
								return true;
							}
						}

					}

				}
			}
		}
		return false;

	}

	private void dialogQueryLieferschein() throws Throwable {

		FilterKriterium[] filters = null;

		filters = new FilterKriterium[2];
		filters[0] = new FilterKriterium(LieferscheinFac.FLR_LIEFERSCHEIN_MANDANT_C_NR, true,
				"'" + LPMain.getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL, false);
		filters[1] = new FilterKriterium(LieferscheinFac.FLR_LIEFERSCHEIN_LIEFERSCHEINSTATUS_STATUS_C_NR, true,
				"('" + LieferscheinFac.LSSTATUS_STORNIERT + "','" + LieferscheinFac.LSSTATUS_ANGELEGT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		String sTitle = LPMain.getTextRespectUISPr("ls.title.tooltip.auswahl");
		panelQueryFLRLieferschein = LieferscheinFilterFactory.getInstance()
				.createPanelQueryFLRLieferschein(getInternalFrame(), filters, sTitle, null);
		new DialogQuery(panelQueryFLRLieferschein);
	}

	private void jbInit() throws Throwable {
		// dafuer sorgen, dass die Dtos != null sind
		resetDtos();
		initializePanelsEnabled();

		IDX_PANEL_LIEFERSCHEINAUSWAHL = reiterHinzufuegen(LPMain.getTextRespectUISPr("ls.title.panel.auswahl"), null,
				lsAuswahl, LPMain.getTextRespectUISPr("ls.title.tooltip.auswahl"));

		// die restlichen Panels erst bei Bedarf laden
		IDX_PANEL_LIEFERSCHEINKOPFDATEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("ls.title.panel.kopfdaten"),
				null, null, LPMain.getTextRespectUISPr("ls.title.tooltip.kopfdaten"));

		IDX_PANEL_LIEFERSCHEINPOSITIONEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("ls.title.panel.positionen"),
				null, null, LPMain.getTextRespectUISPr("ls.title.tooltip.positionen"));
		// optional die Auftraege
		if (bSammellieferschein) {

			IDX_PANEL_LIEFERSCHEINAUFTRAEGE = reiterHinzufuegen(LPMain.getTextRespectUISPr("ls.title.panel.auftraege"),
					null, null, LPMain.getTextRespectUISPr("ls.title.tooltip.auftraege"));

		}

		IDX_PANEL_LIEFERSCHEINPOSITIONENSICHTAUFTRAG = reiterHinzufuegen(
				LPMain.getTextRespectUISPr("ls.title.panel.sichtauftrag"), null, null,
				LPMain.getTextRespectUISPr("ls.title.tooltip.sichtauftrag"));

		IDX_PANEL_LIEFERSCHEINKONDITIONEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("ls.title.panel.konditionen"),
				null, null, LPMain.getTextRespectUISPr("ls.title.tooltip.konditionen"));

		IDX_PANEL_VERSANDINFOS = reiterHinzufuegen(LPMain.getTextRespectUISPr("ls.versandinfos"), null, null,
				LPMain.getTextRespectUISPr("ls.versandinfos"));

		IDX_PANEL_LIEFERSCHEINUMSATZ = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.umsatzuebersicht"), null, null,
				LPMain.getTextRespectUISPr("lp.umsatzuebersicht"));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_LIEFERSCHEINE_VERKETTEN)) {
			IDX_PANEL_VERKETTET = reiterHinzufuegen(LPMain.getTextRespectUISPr("ls.verkettet"), null, null,
					LPMain.getTextRespectUISPr("ls.verkettet"));

		}

		createLieferscheinAuswahl();
		// den aktuell gewaehlten Lieferschein im Titel anzeigen
		setTitleLieferschein(LPMain.getTextRespectUISPr("ls.title.panel.auswahl"));
		addChangeListener(this);

		getInternalFrame().addItemChangedListener(this);
	}

	private void initializePanelsEnabled() {
		for (int i = 0; i < aPanelsEnabled.length; i++) {
			aPanelsEnabled[i] = true;
		}
	}

	/**
	 * Die Auswahlliste der Lieferscheine ist das erste Panel im Modul.
	 * 
	 * @throws Throwable
	 */
	private void createLieferscheinAuswahl() throws Throwable {
		QueryType[] qtAuswahl = LieferscheinFilterFactory.getInstance().createQTPanelLieferscheinauswahl();
		FilterKriterium[] filtersAuswahl = SystemFilterFactory.getInstance().createFKMandantCNr();

		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER };

		lsAuswahl = new PanelQuery(qtAuswahl, filtersAuswahl, QueryParameters.UC_ID_LIEFERSCHEIN, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr("ls.title.panel.auswahl"), true); // liste
		// refresh
		// wenn
		// lasche
		// geklickt
		// wurde

		// dem PanelQuery die direkten FilterKriterien setzen
		FilterKriteriumDirekt fkDirekt1 = LieferscheinFilterFactory.getInstance().createFKDLieferscheinnummer();

		FilterKriteriumDirekt fkDirekt2 = LieferscheinFilterFactory.getInstance().createFKDKundeLieferadresse();

		lsAuswahl.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);

		lsAuswahl.addDirektFilter(LieferscheinFilterFactory.getInstance().createFKDProjekt());

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_LIEFERSCHEIN,
				ParameterFac.PARAMETER_RECHNUNGSADRESSE_IN_LIEFERSCHEINAUSWAHL);

		if ((Boolean) parameter.getCWertAsObject()) {
			lsAuswahl.addDirektFilter(LieferscheinFilterFactory.getInstance().createFKDKundeRechnungsadresse());
		}

		lsAuswahl.addDirektFilter(LieferscheinFilterFactory.getInstance().createFKDAuftragsnummmer());

		lsAuswahl.addDirektFilter(LieferscheinFilterFactory.getInstance().createFKDTextSuchen());

		lsAuswahl.befuelleFilterkriteriumSchnellansicht(
				LieferscheinFilterFactory.getInstance().createFKLieferscheineSchnellansicht());

		lsAuswahl.createAndSaveAndShowButton("/com/lp/client/res/presentation_chart16x16.png",
				LPMain.getTextRespectUISPr("lp.tooltip.datenausbestehendemangebot"), MY_OWN_NEW_AUS_ANGEBOT,
				RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);

		lsAuswahl.createAndSaveAndShowButton("/com/lp/client/res/auftrag16x16.png",
				LPMain.getTextRespectUISPr("lp.tooltip.datenausbestehendemauftrag"), MY_OWN_NEW_AUS_AUFTRAG,
				RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);

		lsAuswahl.createAndSaveAndShowButton("/com/lp/client/res/truck_red16x16.png",
				LPMain.getTextRespectUISPr("lp.tooltip.datenausbestehendemls"), MY_OWN_NEW_AUS_LIEFERSCHEIN,
				RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_VERSANDWEG)) {
			lsAuswahl.createAndSaveAndShowButton("/com/lp/client/res/scanner16x16.png",
					LPMain.getTextRespectUISPr("ls.lieferaviso"), MY_OWN_NEW_LIEFERAVISO_BARCODE,
					RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);
		}

		setComponentAt(IDX_PANEL_LIEFERSCHEINAUSWAHL, lsAuswahl);
	}

	// TODO-AGILCHANGES
	/**
	 * AGILPRO CHANGES Changed visiblity from protected to public
	 * 
	 * @author Lukas Lisowski
	 * @param e ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		Integer iIdLieferschein = getLieferscheinDto().getIId();

		// dtos hinterlegen
		initializeDtos(iIdLieferschein);

		int selectedIndex = getSelectedIndex();

		if (selectedIndex == IDX_PANEL_LIEFERSCHEINAUSWAHL) {
			getLieferscheinAuswahl().eventYouAreSelected(false);
			lsAuswahl.updateButtons();
			// SP855
			setAuftragDtoSichtAuftrag(new AuftragDto());
			// wenn es fuer das Default TabbedPane noch keinen eintrag gibt, die
			// restlichen Panels deaktivieren, die Grunddaten bleiben erreichbar
			if (getLieferscheinAuswahl().getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_LIEFERSCHEINAUSWAHL, false);
			}
		} else if (selectedIndex == IDX_PANEL_LIEFERSCHEINKOPFDATEN) {
			refreshLieferscheinKopfdaten().eventYouAreSelected(false); // sonst
			// werden
			// die
			// buttons
			// nicht
			// richtig
			// gesetzt
			// !
		} else if (selectedIndex == IDX_PANEL_LIEFERSCHEINPOSITIONEN) {
			refreshLieferscheinPositionen();

			if (getLieferscheinDto().getIId() != null) {
				// filter aktualisieren
				FilterKriterium[] filtersPositionen = LieferscheinFilterFactory.getInstance()
						.createFKFlrlieferscheiniid(getLieferscheinDto().getIId());
				lsPositionenTop.setDefaultFilter(filtersPositionen);
			}
			lsPositionen.eventYouAreSelected(false);

			lsPositionenTop.updateButtons(lsPositionenBottom.getLockedstateDetailMainKey());

			// SP20146
			neuButtonsDerPositionenDeaktivieren();

		} else if (selectedIndex == IDX_PANEL_VERKETTET) {
			refreshVerkettet();

			if (getLieferscheinDto().getIId() != null) {
				// filter aktualisieren
				FilterKriterium[] filtersPositionen = LieferscheinFilterFactory.getInstance()
						.createFKVerkettet(getLieferscheinDto().getIId());
				lsVerkettetTop.setDefaultFilter(filtersPositionen);
			}
			lsVerkettetSplit.eventYouAreSelected(false);

			lsVerkettetTop.updateButtons(lsVerkettetBottom.getLockedstateDetailMainKey());

			// Wenn ich bei einem anderen LS-Verkettet bin, dann muss ich auf
			// den Kopf springen
			VerkettetDto verkettetDto = DelegateFactory.getInstance().getLieferscheinServiceDelegate()
					.verkettetfindByLieferscheinIIdVerkettetOhneExc(getLieferscheinDto().getIId());

			if (verkettetDto != null) {
				getInternalFrame().geheZu(InternalFrameLieferschein.IDX_TABBED_PANE_LIEFERSCHEIN, IDX_PANEL_VERKETTET,
						verkettetDto.getLieferscheinIId(), null, LieferscheinFilterFactory.getInstance()
								.createFKLieferscheinKey((Integer) verkettetDto.getLieferscheinIId()));
			}

		} else if (selectedIndex == IDX_PANEL_LIEFERSCHEINPOSITIONENSICHTAUFTRAG) {
			if (getAuftragDtoSichtAuftrag().getIId() == null && bSammellieferschein) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
						LPMain.getTextRespectUISPr("ls.auftragwaehlen"));
				setSelectedComponent(getLieferscheinAuftraege());
			} else {
				refreshLieferscheinPositionenSichtAuftrag();
				lsPositionenSichtAuftragTop.setDefaultFilter(LieferscheinFilterFactory.getInstance()
						.createFKLieferscheinSichtAuftrag(getLieferscheinDto(), getAuftragDtoSichtAuftrag().getIId()));
				lsPositionenSichtAuftrag.eventYouAreSelected(false);

				lsPositionenSichtAuftragTop.updateButtons(lsPositionenSichtAuftragBottom.getLockedstateDetailMainKey());
			}
		} else if (selectedIndex == IDX_PANEL_LIEFERSCHEINKONDITIONEN) {
			getLieferscheinKonditionen().eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_VERSANDINFOS) {
			getLieferscheinVersandinfos().eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_LIEFERSCHEINUMSATZ) {
			if (!bKriterienLsUmsatzUeberMenueAufgerufen) {
				getKriterienLieferscheinUmsatz();
				getInternalFrame().showPanelDialog(pdKriterienLsUmsatz);
			}
		} else if (selectedIndex == IDX_PANEL_LIEFERSCHEINAUFTRAEGE) {
			getLieferscheinAuftraege().eventYouAreSelected(false);
			getLieferscheinAuftraege().updateButtons(new LockStateValue(null, null, PanelBasis.LOCK_IS_NOT_LOCKED));
		}
	}

	private void neuButtonsDerPositionenDeaktivieren() throws Throwable {
		// PJ20146

		if (lieferscheinGehtAnEinenAnderenMandanten()) {
			if (lsPositionenTop.getHmOfButtons().containsKey(PanelBasis.ACTION_NEW)) {
				lsPositionenTop.getHmOfButtons().get(PanelBasis.ACTION_NEW).getButton().setEnabled(false);
			}
			if (lsPositionenTop.getHmOfButtons().containsKey(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				lsPositionenTop.getHmOfButtons().get(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW).getButton()
						.setEnabled(false);
			}
			if (lsPositionenTop.getHmOfButtons().containsKey(MY_OWN_NEW_SNRBARCODEERFASSUNG)) {
				lsPositionenTop.getHmOfButtons().get(MY_OWN_NEW_SNRBARCODEERFASSUNG).getButton().setEnabled(false);
			}
			if (lsPositionenTop.getHmOfButtons().containsKey(MY_OWN_NEW_POSITION_AUS_FORECAST)) {
				lsPositionenTop.getHmOfButtons().get(MY_OWN_NEW_POSITION_AUS_FORECAST).getButton().setEnabled(false);
			}
			if (lsPositionenTop.getHmOfButtons().containsKey(PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN)) {
				lsPositionenTop.getHmOfButtons().get(PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN).getButton()
						.setEnabled(false);
			}
			if (lsPositionenTop.getHmOfButtons().containsKey(BUTTON_SCHNELLERFASSUNG_LIEFERSCHEINPOSITIONEN)) {
				lsPositionenTop.getHmOfButtons().get(BUTTON_SCHNELLERFASSUNG_LIEFERSCHEINPOSITIONEN).getButton()
						.setEnabled(false);
			}

		}
	}

	private void getKriterienLieferscheinUmsatz() throws Throwable {
		if (pdKriterienLsUmsatz == null) {
			pdKriterienLsUmsatz = new PanelDialogKriterienLieferscheinUmsatz(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kriterienumsatzuebersicht"));
		}
	}

	private LieferscheinpositionDto getSelectedLieferscheinpositionDto() throws ExceptionLP {
		if (lsPositionenTop.getSelectedId() == null)
			return null;

		return DelegateFactory.getInstance().getLieferscheinpositionDelegate()
				.lieferscheinpositionFindByPrimaryKey((Integer) lsPositionenTop.getSelectedId());
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		// Zeile in der Tabelle doppelgeklickt
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			handleGotoDetailPanel(e);
		}

		else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRBegruendung) {

				DelegateFactory.getInstance().getLsDelegate()
						.updateLieferscheinBegruendung(getLieferscheinDto().getIId(), null);
				if (lsKonditionen != null) {
					lsKonditionen.eventYouAreSelected(false);
				}

			}

		}

		else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) { // Zeile in
			// Tabelle
			// gewaehlt
			if (e.getSource() == lsAuswahl) {
				// gewaehlten lieferschein hinterlegen
				Integer pkLieferschein = (Integer) lsAuswahl.getSelectedId();
				initializeDtos(pkLieferschein);
				setKeyWasForLockMe();

				setTitleLieferschein(LPMain.getTextRespectUISPr("ls.title.panel.auswahl"));

				// lieferscheinpositionen
				/*
				 * refreshLieferscheinPositionen();
				 * lsPositionenTop.setDefaultFilter(LieferscheinFilterFactory
				 * .getInstance().createFKFlrlieferscheiniid( getLieferscheinDto().getIId()));
				 * lsPositionenBottom.setKeyWhenDetailPanel(pkLieferschein); // pk
				 */
				// fuer
				// erste
				// Ansicht
				// vorbelegen

				// lieferscheinpositionen sicht auftrag
				if (getLieferscheinDto().getIId() != null
						&& getLieferscheinDto().getLieferscheinartCNr().equals(LieferscheinFac.LSART_AUFTRAG)) {
					if (bSammellieferschein) {
						// Filter auf die Auftraege
						FilterKriterium[] filtersPos = LieferscheinFilterFactory.getInstance()
								.createFKAuftraegeEinesLieferscheins(getLieferscheinDto().getIId());
						getLieferscheinAuftraege().setDefaultFilter(filtersPos);
					}
					// Sicht Auftrag
					refreshLieferscheinPositionenSichtAuftrag();
					lsPositionenSichtAuftragTop
							.setDefaultFilter(LieferscheinFilterFactory.getInstance().createFKLieferscheinSichtAuftrag(
									getLieferscheinDto(), getAuftragDtoSichtAuftrag().getIId()));
					// @todo ist das richtig? fuer lock brauche ich die
					// auftragposition ... PJ 5259
					lsPositionenSichtAuftragBottom.setKeyWhenDetailPanel(pkLieferschein);
				}

				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_LIEFERSCHEINAUSWAHL,
						(pkLieferschein != null));

				enablePanels(getLieferscheinDto(), false);
			} else if (e.getSource() == lsPositionenTop) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				lsPositionenBottom.setKeyWhenDetailPanel(key);
				lsPositionenBottom.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				lsPositionenTop.updateButtons(lsPositionenBottom.getLockedstateDetailMainKey());

				// SP20146
				neuButtonsDerPositionenDeaktivieren();
			} else if (e.getSource() == lsVerkettetTop) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				lsVerkettetBottom.setKeyWhenDetailPanel(key);
				lsVerkettetBottom.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				lsVerkettetTop.updateButtons(lsVerkettetBottom.getLockedstateDetailMainKey());
			} else if (e.getSource() == lsPositionenSichtAuftragTop) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				lsPositionenSichtAuftragBottom.setKeyWhenDetailPanel(key);
				lsPositionenSichtAuftragBottom.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				lsPositionenSichtAuftragTop.updateButtons(lsPositionenSichtAuftragBottom.getLockedstateDetailMainKey());
			} else if (e.getSource() == lsAuftraege) {
				Integer iIdAuftrag = (Integer) lsAuftraege.getSelectedId();
				if (iIdAuftrag != null) {
					AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
							.auftragFindByPrimaryKey(iIdAuftrag);
					this.setAuftragDtoSichtAuftrag(auftragDto);
					if (null == lsPositionenSichtAuftragTop || null == lsPositionenSichtAuftrag) {
						refreshLieferscheinPositionenSichtAuftrag();
					}
					lsPositionenSichtAuftragTop
							.setDefaultFilter(LieferscheinFilterFactory.getInstance().createFKLieferscheinSichtAuftrag(
									getLieferscheinDto(), getAuftragDtoSichtAuftrag().getIId()));
				}
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (e.getSource() == lsPositionenBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				lsPositionenTop.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == lsVerkettetBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				lsVerkettetTop.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == lsPositionenSichtAuftragBottom) {
				// IMS 1646 wenn die Auftragposition inzwischen geloescht wurde
				if (lsPositionenSichtAuftragBottom.getAuftragpositionDto() == null) {
					lsPositionenSichtAuftrag.eventYouAreSelected(false);
					lsPositionenSichtAuftragTop.updateButtons();
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_LIEFERSCHEINPOSITIONENSICHTAUFTRAG,
							true);
				} else {
					// im QP die Buttons in den Zustand neu setzen.
					lsPositionenSichtAuftragTop.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

			if (e.getSource() == lsAuswahl) {
				if (sAspectInfo.equals(MY_OWN_NEW_AUS_AUFTRAG)) {
					// der Benutzer muss einen Auftrag auswaehlen
					dialogQueryAuftragFromListe(null);
				} else if (sAspectInfo.equals(MY_OWN_NEW_AUS_ANGEBOT)) {
					// der Benutzer muss ein Angebot auswaehlen
					dialogQueryAngebotFromListe(null);
				} else if (sAspectInfo.equals(MY_OWN_NEW_AUS_LIEFERSCHEIN)) {
					// der Benutzer muss einen Lieferschein auswaehlen
					dialogQueryLieferschein();
				} else if (sAspectInfo.equals(MY_OWN_NEW_LIEFERAVISO_BARCODE)) {

					dialogLieferaviso();
				}
			} else if (e.getSource() == lsVerkettetTop) {

				if (sAspectInfo.equals(MY_OWN_NEW_MEHRERE_LIEFERSCHEINE_VERKETTEN)) {

					FilterKriterium[] filters = null;

					filters = new FilterKriterium[7];
					filters[0] = new FilterKriterium(LieferscheinFac.FLR_LIEFERSCHEIN_MANDANT_C_NR, true,
							"'" + LPMain.getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL, false);
					filters[1] = new FilterKriterium(LieferscheinFac.FLR_LIEFERSCHEIN_KUNDE_I_ID_LIEFERADRESSE, true,
							getLieferscheinDto().getKundeIIdLieferadresse() + "", FilterKriterium.OPERATOR_EQUAL,
							false);
					filters[2] = new FilterKriterium(LieferscheinFac.FLR_LIEFERSCHEIN_KUNDE_I_ID_RECHNUNGSADRESSE, true,
							getLieferscheinDto().getKundeIIdRechnungsadresse() + "", FilterKriterium.OPERATOR_EQUAL,
							false);
					filters[3] = new FilterKriterium(
							LieferscheinFac.FLR_LIEFERSCHEIN_WAEHRUNG_C_NR_LIEFERSCHEINWAEHRUNG, true,
							"'" + getLieferscheinDto().getWaehrungCNr() + "'", FilterKriterium.OPERATOR_EQUAL, false);

					filters[4] = new FilterKriterium(LieferscheinFac.FLR_LIEFERSCHEIN_I_ID, true,
							getLieferscheinDto().getIId() + "", FilterKriterium.OPERATOR_NOT_EQUAL, false);

					// Bereits verkettete nicht mehr anzeigen

					filters[5] = new FilterKriterium(LieferscheinServiceFac.LS_HANDLER_OHNE_VERKETTETE, true, "",
							FilterKriterium.OPERATOR_NOT_EQUAL, false);
					filters[6] = new FilterKriterium(LieferscheinFac.FLR_LIEFERSCHEIN_LIEFERSCHEINSTATUS_STATUS_C_NR,
							true, "'" + LieferscheinFac.LSSTATUS_VERRECHNET + "'", FilterKriterium.OPERATOR_NOT_EQUAL,
							false);

					String sTitle = LPMain.getTextRespectUISPr("ls.title.tooltip.auswahl");

					panelQueryFLRLieferscheinVerketten = LieferscheinFilterFactory.getInstance()
							.createPanelQueryFLRLieferschein(getInternalFrame(), filters, sTitle, null);
					panelQueryFLRLieferscheinVerketten.setMultipleRowSelectionEnabled(true);
					panelQueryFLRLieferscheinVerketten.addButtonAuswahlBestaetigen(null);

					new DialogQuery(panelQueryFLRLieferscheinVerketten);
				}

			} else if (e.getSource() == lsPositionenTop) {

				if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
					if (istAktualisierenLieferscheinErlaubt()) {
						einfuegenHV();
					}
				} else if (sAspectInfo.equals(BUTTON_SCHNELLERFASSUNG_LIEFERSCHEINPOSITIONEN)) {

					if (istAktualisierenLieferscheinErlaubt()) {

						DialogPositionenSchnellerfassung d = new DialogPositionenSchnellerfassung(lsPositionen,
								getLieferscheinDto());
						LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
						d.setVisible(true);
					}
					lsPositionen.eventYouAreSelected(false); // refresh
				} else if (sAspectInfo.equals(BUTTON_SORTIERE_LS_NACH_AUFTRAGSNUMMER)) {

					// Vorher fragen
					if (istAktualisierenLieferscheinErlaubt()) {

						boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("ls.sortierenach.frage"));

						if (b == true) {

							DelegateFactory.getInstance().getLieferscheinpositionDelegate()
									.sortiereNachAuftragsnummer(getLieferscheinDto().getIId());

							lsPositionenTop.eventYouAreSelected(false);

						}
					}
				} else if (sAspectInfo.equals(BUTTON_SORTIERE_LS_NACH_ARTIKELNUMMER)) {

					// Vorher fragen
					if (istAktualisierenLieferscheinErlaubt()) {

						boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("ls.sortierenach.frage"));

						if (b == true) {

							DelegateFactory.getInstance().getLieferscheinpositionDelegate()
									.sortiereNachArtikelnummer(getLieferscheinDto().getIId());

							lsPositionenTop.eventYouAreSelected(false);

						}
					}
				} else {

					if (istAktualisierenLieferscheinErlaubt()) {
						if (sAspectInfo.equals(MY_OWN_NEW_SNRBARCODEERFASSUNG)) {
							dialogBarcodeerfassung(false);
							lsPositionenTop.eventYouAreSelected(false);

						}
					}
				}

				if (sAspectInfo.equals(MY_OWN_NEW_POSITION_AUS_FORECAST)) {

					panelQueryFLRForecastpositionen = ForecastFilterFactory.getInstance()
							.createPanelFLRForecastpositionen(getInternalFrame(),
									DelegateFactory.getInstance().getForecastDelegate()
											.getAktuellFreigegebenenForecastauftragEinerLieferadresse(
													getLieferscheinDto().getKundeIIdLieferadresse()),
									false);

					DialogQuery dialog = new DialogQuery(panelQueryFLRForecastpositionen);

				} else if (sAspectInfo.endsWith(ACTION_SPECIAL_PREISE_NEU_KALKULIEREN)) {

					if (getLieferscheinDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)) {

						//
						PanelPositionenArtikelVerkauf panelBottom = ((PanelPositionenArtikelVerkauf) lsPositionenBottom.panelArtikel);

						LieferscheinpositionDto[] dtos = DelegateFactory.getInstance().getLieferscheinpositionDelegate()
								.lieferscheinpositionFindByLieferscheinIId(getLieferscheinDto().getIId());

						for (int i = 0; i < dtos.length; i++) {
							if (dtos[i].getPositionsartCNr().equals(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT)) {
								lsPositionenTop.setSelectedId(dtos[i].getIId());
								lsPositionenTop.eventYouAreSelected(false);
								panelBottom.setKeyWhenDetailPanel(dtos[i].getIId());
								panelBottom.update();
								panelBottom.berechneVerkaufspreis(false);
								panelBottom.pruefeNettoPreis();
								lsPositionenBottom.eventActionSave(null, false);
							}
						}
					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("auft.error.neuberechnen"));
					}

				}

			} else if (e.getSource() == lsPositionenSichtAuftragTop) {
				if (sAspectInfo.equals(MY_OWN_NEW_ALLE_POSITIONEN_AUS_AUFTRAG_UEBERNEHMEN)) {
					if (lsPositionenSichtAuftragTop.getTable().getRowCount() > 0) {
						lsPositionenSichtAuftragBottom.eventActionNew(e, true, false);
						lsPositionenSichtAuftrag.eventYouAreSelected(false); // das
						// ganze
						// 1
						// :
						// n
						// Panel
						// aktualisieren
					}
				}

				if (istAktualisierenLieferscheinErlaubt()) {
					if (sAspectInfo.equals(MY_OWN_NEW_SNRBARCODEERFASSUNG)) {
						dialogBarcodeerfassung(true);
						lsPositionenSichtAuftragTop.eventYouAreSelected(false);

					}
				}
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == lsAuswahl) {
				// wenn es bisher keinen lieferschein gegeben hat
				if (lsAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_LIEFERSCHEINAUSWAHL, true);
				}

				refreshLieferscheinKopfdaten().eventActionNew(e, true, false);
				setSelectedComponent(lsKopfdaten);
			} else if (e.getSource() == lsPositionenTop) {
				lsPositionenBottom.eventActionNew(e, true, false);
				lsPositionenBottom.eventYouAreSelected(false);
				setSelectedComponent(lsPositionen); // ui
			} else if (e.getSource() == lsVerkettetTop) {
				lsVerkettetBottom.eventActionNew(e, true, false);
				lsVerkettetBottom.eventYouAreSelected(false);
				setSelectedComponent(lsVerkettetSplit); // ui
			} else if (e.getSource() == lsPositionenSichtAuftragTop) {
				lsPositionenSichtAuftragBottom.eventActionNew(e, true, false);
				lsPositionenSichtAuftragBottom.eventYouAreSelected(false);
				setSelectedComponent(lsPositionenSichtAuftrag); // ui
			} else if (e.getSource() == lsAuftraege) {

				// SP8559
				if (getLieferscheinDto().getAuftragIId() != null) {
					AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
							.auftragFindByPrimaryKey(getLieferscheinDto().getAuftragIId());

					for (RechnungDto re : DelegateFactory.getInstance().getRechnungDelegate()
							.rechnungFindByAuftragIId(auftragDto.getIId())) {

						if (re.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT))
							continue;
						if (re.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_ANZAHLUNG)) {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"),
									LPMain.getTextRespectUISPr("ls.sammellieferschein.anzahlungsrechnung.error"));
							return;
						}
					}

				}

				dialogQueryAuftragFromListeZusatz(null);
				getLieferscheinAuftraege().updateButtons();
			}
		} else

		if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == lsPositionenBottom) {
				setKeyWasForLockMe();
				if (lsPositionenBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = lsPositionenTop.getId2SelectAfterDelete();
					lsPositionenTop.setSelectedId(oNaechster);
				}
				lsPositionen.eventYouAreSelected(false);
			} else if (e.getSource() == lsVerkettetBottom) {
				setKeyWasForLockMe();
				if (lsVerkettetBottom.getKeyWhenDetailPanel() == null) {
					Object oNaechster = lsVerkettetTop.getId2SelectAfterDelete();
					lsVerkettetTop.setSelectedId(oNaechster);
				}
				lsVerkettetSplit.eventYouAreSelected(false);
			} else if (e.getSource() == lsPositionenSichtAuftragBottom) {
				setKeyWasForLockMe();

				lsPositionenSichtAuftrag.eventYouAreSelected(false);
			} else if (e.getSource() == lsKopfdaten) {
				// die Liste neu laden, falls sich etwas geaendert hat
				lsAuswahl.eventYouAreSelected(false);

				// nach einem Discard ist der aktuelle Key nicht mehr gesetzt
				setKeyWasForLockMe();

				// der Key der Kopfdaten steht noch auf new|...
				lsKopfdaten.setKeyWhenDetailPanel(lsAuswahl.getSelectedId());

				// auf die Auswahl schalten
				setSelectedComponent(lsAuswahl);

				// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
				// restlichen panel deaktivieren
				if (lsAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_LIEFERSCHEINAUSWAHL, false);
				}
			}
		} else

		// nostayinmypanel: 0 Das Event ACTION_STAY_IN_MY_PANEL wird ersetzt
		// durch
		// ACTION_SAVE und ACTION_DISCARD
		if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

			if (e.getSource() == lsKopfdaten) {
				Object pkLieferschein = lsKopfdaten.getKeyWhenDetailPanel();
				initializeDtos((Integer) pkLieferschein);
				getInternalFrame().setKeyWasForLockMe(pkLieferschein.toString());

				lsAuswahl.clearDirektFilter();
				lsAuswahl.eventYouAreSelected(false);
				lsAuswahl.setSelectedId(pkLieferschein);
				lsAuswahl.eventYouAreSelected(false);
			} else if (e.getSource() == lsPositionenBottom) {
				Object oKey = lsPositionenBottom.getKeyWhenDetailPanel();
				if (!oKey.equals(LPMain.getLockMeForNew())) {
					lsPositionenTop.eventYouAreSelected(false);
					lsPositionenTop.setSelectedId(oKey);
				}
				lsPositionen.eventYouAreSelected(false);
			} else if (e.getSource() == lsVerkettetBottom) {
				Object oKey = lsVerkettetBottom.getKeyWhenDetailPanel();
				if (!oKey.equals(LPMain.getLockMeForNew())) {
					lsVerkettetTop.eventYouAreSelected(false);
					lsVerkettetTop.setSelectedId(oKey);
				}
				lsVerkettetSplit.eventYouAreSelected(false);
			} else if (e.getSource() == lsPositionenSichtAuftragBottom) {
				Object oNaechster = lsPositionenSichtAuftragTop.getId2SelectAfterDelete();
				lsPositionenSichtAuftrag.eventYouAreSelected(false);

				lsPositionenSichtAuftragTop.setSelectedId(oNaechster);
				lsPositionenSichtAuftrag.eventYouAreSelected(false);
				// wenn die letzte Position uebernommen wurde, Wechsel auf
				// Positionen
				if (lsPositionenSichtAuftragBottom.getKeyWhenDetailPanel() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
					setSelectedComponent(refreshLieferscheinPositionen());
				}
			}
		} else

		// nostayinmypanel: 1 Das Event ACTION_STAY_IN_MY_PANEL wird ersetzt
		// durch
		// ACTION_SAVE und ACTION_DISCARD

		// wir landen hier bei der Abfolge Button Aendern -> xx -> Button
		// Discard
		if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == lsPositionenBottom) {
				lsPositionen.eventYouAreSelected(false); // refresh auf das
				// gesamte 1:n panel
			} else if (e.getSource() == lsVerkettetBottom) {
				lsVerkettetSplit.eventYouAreSelected(false); // refresh auf das
				// gesamte 1:n panel
			} else if (e.getSource() == lsPositionenSichtAuftragBottom) {
				lsPositionenSichtAuftrag.eventYouAreSelected(false); // refresh
				// auf
				// das
				// gesamte
				// 1:n
				// panel
			}
		} else

		// der OK Button in einem PanelDialog wurde gedrueckt
		if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {

			if (e.getSource() == pdKriterienLsUebersicht) { // ptclient: 5
				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = pdKriterienLsUebersicht.getAlleFilterKriterien();

				// die Kriterien dem PanelTabelle setzen
				// getLieferscheinUebersicht().setDefaultFilter(aAlleKriterien);

				// die Uebersicht aktualisieren @todo redundant, wenn man dann
				// ohnehin wechselt PJ 5260
				ptLieferscheinUebersicht.eventYouAreSelected(false);

				// man steht auf alle Faelle auf der Uebersicht
				setSelectedComponent(ptLieferscheinUebersicht);
				setTitleLieferscheinOhneLieferscheinnummer(LPMain.getTextRespectUISPr("lp.umsatzuebersicht"));
				ptLieferscheinUebersicht.updateButtons(new LockStateValue(null, null, PanelBasis.LOCK_IS_NOT_LOCKED));
			} else

			if (e.getSource() == pdKriterienLsUmsatz) {
				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = pdKriterienLsUmsatz.getAlleFilterKriterien();

				// die Kriterien dem PanelTabelle setzen
				getLieferscheinUmsatz().setDefaultFilter(aAlleKriterien);

				// die Uebersicht aktualisieren
				ptLieferscheinUmsatz.eventYouAreSelected(false);

				// man steht auf alle Faelle auf der Uebersicht
				setSelectedComponent(ptLieferscheinUmsatz);
				setTitleLieferscheinOhneLieferscheinnummer(LPMain.getTextRespectUISPr("lp.umsatzuebersicht"));
				bKriterienLsUmsatzUeberMenueAufgerufen = false;
				ptLieferscheinUmsatz.updateButtons(new LockStateValue(null, null, PanelBasis.LOCK_IS_NOT_LOCKED));
			}
		} else

		// Einer der Knoepfe zur Reihung der Positionen auf einem PanelQuery
		// wurde gedrueckt
		if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == lsPositionenTop) {
				if (getLieferscheinDto().getStatusCNr().equals(LieferscheinFac.LSSTATUS_ANGELEGT)) {
					int iPos = lsPositionenTop.getTable().getSelectedRow();

					// wenn die Position nicht die erste ist
					if (iPos > 0) {
						Integer iIdPosition = (Integer) lsPositionenTop.getSelectedId();

						TableModel tm = lsPositionenTop.getTable().getModel();

						LieferscheinpositionDto posDto = getSelectedLieferscheinpositionDto();
						if (LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_INTELLIGENTE_ZWISCHENSUMME
								.equals(posDto.getPositionsartCNr())) {
							Integer myPosNumber = DelegateFactory.getInstance().getLieferscheinpositionDelegate()
									.getLSPositionNummer(posDto.getIId());
							Integer previousIId = DelegateFactory.getInstance().getLieferscheinpositionDelegate()
									.getLSPositionIIdFromPositionNummer(getLieferscheinDto().getIId(), myPosNumber - 1);
							if (previousIId.equals(posDto.getZwsBisPosition())) {
								return;
							}
						}

						DelegateFactory.getInstance().getLieferscheinpositionDelegate()
								.vertauscheLieferscheinpositionMinus(iPos, tm);

						// die Liste neu anzeigen und den richtigen Datensatz
						// markieren
						lsPositionenTop.setSelectedId(iIdPosition);
					}
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("anf.error.positionenverschieben"));
				}
			}
		} else

		if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == lsPositionenTop) {
				if (getLieferscheinDto().getStatusCNr().equals(LieferscheinFac.LSSTATUS_ANGELEGT)) {
					int iPos = lsPositionenTop.getTable().getSelectedRow();

					// wenn die Position nicht die letzte ist
					if (iPos < lsPositionenTop.getTable().getRowCount() - 1) {
						Integer iIdPosition = (Integer) lsPositionenTop.getSelectedId();

						TableModel tm = lsPositionenTop.getTable().getModel();
						DelegateFactory.getInstance().getLieferscheinpositionDelegate()
								.vertauscheLieferscheinpositionPlus(iPos, tm);

						// Integer iIdPositionPlus1 = (Integer) lsPositionenTop
						// .getTable().getValueAt(iPos + 1, 0);
						// DelegateFactory
						// .getInstance()
						// .getLieferscheinpositionDelegate()
						// .vertauscheLieferscheinpositionen(iIdPosition,
						// iIdPositionPlus1);

						// die Liste neu anzeigen und den richtigen Datensatz
						// markieren
						lsPositionenTop.setSelectedId(iIdPosition);
					}
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("anf.error.positionenverschieben"));
				}
			}
		} else

		if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (e.getSource() == lsPositionenTop) {

				if (lsPositionenTop.getSelectedId() != null) {
					LieferscheinpositionDto lsPos = DelegateFactory.getInstance().getLieferscheinpositionDelegate()
							.lieferscheinpositionFindByPrimaryKey((Integer) lsPositionenTop.getSelectedId());

					lsPositionenBottom.setArtikeSetIIdForNewPosition(lsPos.getPositioniIdArtikelset());
					lsPositionenBottom.eventActionNew(e, true, false);
					lsPositionenBottom.eventYouAreSelected(false); // Buttons
																	// schalten
					// if (lsPos.getPositioniIdArtikelset() == null) {
					// lsPositionenBottom.eventActionNew(e, true, false);
					// lsPositionenBottom.eventYouAreSelected(false); // Buttons
					// // schalten
					// } else {
					// DialogFactory
					// .showModalDialog(
					// LPMain.getTextRespectUISPr("lp.error"),
					// LPMain.getTextRespectUISPr("lp.error.artikel.kannnichtzwischenseteingefuegtwerden"));
					// return;
					// }

				} else {
					lsPositionenBottom.eventActionNew(e, true, false);
					lsPositionenBottom.eventYouAreSelected(false); // Buttons
					// schalten
				}
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			copyHV();
		}

	}

	private void dialogLieferaviso() throws Throwable {
		DialogLieferavisoBarcode d = new DialogLieferavisoBarcode(lsAuswahl);
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
		d.setVisible(true);

		if (d.getLieferscheinDto() != null) {
			processLieferaviso(d.getLieferscheinDto());

			// PJ21441
			dialogLieferaviso();

		}
	}

	private void processLieferaviso(LieferscheinDto lsDto) throws Throwable {
		if (!LocaleFac.STATUS_GELIEFERT.equals(lsDto.getStatusCNr())) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("ls.dialog.barcodeaviso.statusfalsch.title"),
					LPMain.getMessageTextRespectUISPr("ls.dialog.barcodeaviso.statusfalsch.text", lsDto.getCNr(),
							lsDto.getStatusCNr().trim()));
			return;
		}

		if (!DelegateFactory.getInstance().getLsDelegate().hatLieferscheinVersandweg(lsDto)) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("ls.dialog.barcodeaviso.versandweg.title"),
					LPMain.getMessageTextRespectUISPr("ls.dialog.barcodeaviso.versandweg.text", lsDto.getCNr()));
			return;
		}

		boolean hasLieferaviso = lsDto.getTLieferaviso() != null;
		boolean enable = lsDto.getIId() != null && lsDto.getTLieferaviso() == null;
		if (hasLieferaviso) {
			enable = true;
		}

		if (enable) {
			DelegateFactory.getInstance().getLsDelegate().setzeAuslieferdatumAufJetzt(lsDto.getIId());

			if (lsDto.getTLieferaviso() == null) {
				dialogLieferavisoErzeugen(lsDto);
			} else {
				boolean yes = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("ls.bereitsavisiert"));
				if (yes) {
					DelegateFactory.getInstance().getLsDelegate().resetLieferscheinAviso(lsDto.getIId());
					dialogLieferavisoErzeugen(lsDto);
				}
			}
		}
	}

	private void dialogLieferavisoErzeugen(LieferscheinDto lsDto) throws Throwable {
		DialogLieferaviso dlgLieferaviso = new DialogLieferaviso() {

			@Override
			protected void processed(LieferscheinDto lsDto) throws Throwable {
			}

			@Override
			protected void processException(Throwable t) {
			}
		};

		dlgLieferaviso.showModal(LPMain.getInstance().getDesktop(), lsDto);

		if (dlgLieferaviso.getThrowable() != null) {
			throw dlgLieferaviso.getThrowable();
		}
	}

	public void dialogBarcodeerfassung(boolean bSichtAuftrag) throws Throwable, ExceptionLP {

		if (bSichtAuftrag == false) {

			if (getLieferscheinDto().getLieferscheinartCNr().equals(LieferscheinFac.LSART_AUFTRAG)) {
				// einmalige Warnung aussprechen
				if (!((InternalFrameLieferschein) getInternalFrame()).bWarnungAusgesprochen) {
					((InternalFrameLieferschein) getInternalFrame()).bWarnungAusgesprochen = true;
					if (DialogFactory.showMeldung(
							LPMain.getTextRespectUISPr("ls.warning.reduziertnichtdieoffenemengeimauftrag"),
							LPMain.getTextRespectUISPr("lp.warning"),
							javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.NO_OPTION) {

						return;
					}

				}
			}
		}

		DialogPositionenBarcodeerfassung d = new DialogPositionenBarcodeerfassung(getLieferscheinDto().getLagerIId(),
				getInternalFrame());
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);

		d.setVisible(true);
		List<SeriennrChargennrMitMengeDto> alSeriennummern = d.alSeriennummern;

		if (alSeriennummern.size() > 0 && d.artikelIId != null) {
			ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(d.artikelIId);

			LieferscheinpositionDto lsPos = new LieferscheinpositionDto();
			lsPos.setBelegIId(getLieferscheinDto().getIId());
			lsPos.setArtikelIId(d.artikelIId);
			lsPos.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);

			lsPos.setSeriennrChargennrMitMenge(alSeriennummern);

			lsPos.setBNettopreisuebersteuert(Helper.boolean2Short(false));
			lsPos.setNMenge(new BigDecimal(alSeriennummern.size()));
			lsPos.setEinheitCNr(artikelDto.getEinheitCNr());

			if (bSichtAuftrag == true && getAuftragDtoSichtAuftrag() != null
					&& getAuftragDtoSichtAuftrag().getIId() != null) {
				// Zuerst entsprechende Auftragspositionen suchen

				AuftragpositionDto[] auftragspositionDtos = DelegateFactory.getInstance().getAuftragpositionDelegate()
						.auftragpositionFindByAuftragOffeneMenge(getAuftragDtoSichtAuftrag().getIId());

				AuftragpositionDto auftragspositionDto = null;

				for (int i = 0; i < auftragspositionDtos.length; i++) {

					if (auftragspositionDtos[i].getArtikelIId() != null
							&& auftragspositionDtos[i].getArtikelIId().equals(lsPos.getArtikelIId())) {
						auftragspositionDto = auftragspositionDtos[i];
						break;
					}

				}

				if (auftragspositionDto != null) {

					lsPos.setBMwstsatzuebersteuert(auftragspositionDto.getBMwstsatzuebersteuert());
					lsPos.setBNettopreisuebersteuert(auftragspositionDto.getBNettopreisuebersteuert());
					lsPos.setBRabattsatzuebersteuert(auftragspositionDto.getBRabattsatzuebersteuert());

					lsPos.setEinheitCNr(auftragspositionDto.getEinheitCNr());
					lsPos.setFRabattsatz(auftragspositionDto.getFRabattsatz());
					lsPos.setFZusatzrabattsatz(auftragspositionDto.getFZusatzrabattsatz());

					lsPos.setNMaterialzuschlagKurs(auftragspositionDto.getNMaterialzuschlagKurs());
					lsPos.setTMaterialzuschlagDatum(auftragspositionDto.getTMaterialzuschlagDatum());

					lsPos.setAuftragpositionIId(auftragspositionDto.getIId());
					lsPos.setMwstsatzIId(auftragspositionDto.getMwstsatzIId());
					lsPos.setNBruttoeinzelpreis(auftragspositionDto.getNBruttoeinzelpreis());

					lsPos.setNMwstbetrag(auftragspositionDto.getNMwstbetrag());
					lsPos.setNEinzelpreis(auftragspositionDto.getNEinzelpreis());

					lsPos.setNMaterialzuschlag(auftragspositionDto.getNMaterialzuschlag());

					lsPos.setNNettoeinzelpreis(auftragspositionDto.getNNettoeinzelpreis());

					lsPos.setNRabattbetrag(auftragspositionDto.getNRabattbetrag());
					lsPos.setTypCNr(auftragspositionDto.getTypCNr());
					lsPos.setVerleihIId(auftragspositionDto.getVerleihIId());

				} else {

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("ls.error.barcodeerfassung.keineoffeneposition") + " "
									+ artikelDto.getCNr());
					return;
				}

			} else {

				KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(getLieferscheinDto().getKundeIIdRechnungsadresse());

				Timestamp belegDatum = HelperTimestamp.belegDatum(getLieferscheinDto());
				MwstsatzDto mwstsatzDtoAktuell = DelegateFactory.getInstance().getMandantDelegate()
						.mwstsatzFindZuDatum(kundeDto.getMwstsatzbezIId(), belegDatum);
				/*
				 * MwstsatzDto mwstsatzDtoAktuell =
				 * DelegateFactory.getInstance().getMandantDelegate()
				 * .mwstsatzFindByMwstsatzbezIIdAktuellster(kundeDto.getMwstsatzbezIId());
				 */
				VkpreisfindungDto vkpreisfindungDto = DelegateFactory.getInstance().getVkPreisfindungDelegate()
						.verkaufspreisfindung(artikelDto.getIId(), kundeDto.getIId(),
								new BigDecimal(alSeriennummern.size()), new java.sql.Date(System.currentTimeMillis()),
								kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(), mwstsatzDtoAktuell.getIId(),
								getLieferscheinDto().getWaehrungCNr());

				VerkaufspreisDto preisDtoInMandantenwaehrung = null;
				try {
					preisDtoInMandantenwaehrung = Helper.getVkpreisBerechnet(vkpreisfindungDto);
				} catch (Throwable t) {
					t.printStackTrace();
				}

				VerkaufspreisDto verkaufspreisDtoInZielwaehrung = null;
				if (preisDtoInMandantenwaehrung != null) {
					if (preisDtoInMandantenwaehrung.waehrungCNr != null
							&& preisDtoInMandantenwaehrung.waehrungCNr.equals(getLieferscheinDto().getWaehrungCNr())) {
						verkaufspreisDtoInZielwaehrung = preisDtoInMandantenwaehrung;
						// TODO: Runden auf wieviele Stellen
						verkaufspreisDtoInZielwaehrung.einzelpreis = Helper
								.rundeKaufmaennisch(verkaufspreisDtoInZielwaehrung.einzelpreis
										.multiply(verkaufspreisDtoInZielwaehrung.tempKurs), 4);
						verkaufspreisDtoInZielwaehrung.rabattsumme = verkaufspreisDtoInZielwaehrung.einzelpreis
								.subtract(verkaufspreisDtoInZielwaehrung.nettopreis);
						// TODO: Rabattsatz hat wieviele Stellen
						// ?
						verkaufspreisDtoInZielwaehrung.rabattsatz = new Double(
								Helper.getProzentsatz(verkaufspreisDtoInZielwaehrung.einzelpreis,
										verkaufspreisDtoInZielwaehrung.rabattsumme, 4));

					} else {
						verkaufspreisDtoInZielwaehrung = DelegateFactory.getInstance().getVkPreisfindungDelegate()
								.getPreisdetailsInFremdwaehrung(preisDtoInMandantenwaehrung,
										new BigDecimal(getLieferscheinDto()
												.getFWechselkursmandantwaehrungzubelegwaehrung().doubleValue()));
					}
				}
				lsPos.setMwstsatzIId(mwstsatzDtoAktuell.getIId());

				if (verkaufspreisDtoInZielwaehrung != null) {
					lsPos.setNEinzelpreis(verkaufspreisDtoInZielwaehrung.nettopreis);
					lsPos.setNNettoeinzelpreis(verkaufspreisDtoInZielwaehrung.nettopreis);
					lsPos.setNBruttoeinzelpreis(verkaufspreisDtoInZielwaehrung.bruttopreis);
					lsPos.setNMwstbetrag(verkaufspreisDtoInZielwaehrung.mwstsumme);
					lsPos.setMwstsatzIId(verkaufspreisDtoInZielwaehrung.mwstsatzIId);
				} else {
					lsPos.setNEinzelpreis(new BigDecimal(0));
					lsPos.setNNettoeinzelpreis(new BigDecimal(0));
					lsPos.setNBruttoeinzelpreis(new BigDecimal(0));
				}

				lsPos.setFRabattsatz(0D);
				lsPos.setFZusatzrabattsatz(0D);
			}
			DelegateFactory.getInstance().getLieferscheinpositionDelegate().createLieferscheinposition(lsPos, false);

		}
	}

	/**
	 * Verarbeitung von ItemChangedEvent.GOTO_DETAIL_PANEL.
	 * 
	 * @param e ItemChangedEvent
	 * @throws Throwable Ausnahme
	 */
	private void handleGotoDetailPanel(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == lsAuswahl) {
			Integer lieferscheinIId = (Integer) lsAuswahl.getSelectedId();
			initializeDtos(lieferscheinIId); // befuellt den Lieferschein und
			// den Kunden
			getInternalFrame().setKeyWasForLockMe(lieferscheinIId + "");

			if (lieferscheinIId != null) {
				setSelectedComponent(refreshLieferscheinPositionen()); // auf
				// die
				// Positionen
				// wechseln
			}
		} else if (e.getSource() == panelQueryFLRLieferscheinVerketten) {

			Object[] keys = panelQueryFLRLieferscheinVerketten.getSelectedIds();

			for (int i = 0; i < keys.length; i++) {
				Integer lieferscheinIIdVerkettet = (Integer) keys[i];

				VerkettetDto verkettetDto = new VerkettetDto();
				verkettetDto.setLieferscheinIId(getLieferscheinDto().getIId());
				verkettetDto.setLieferscheinIIdVerkettet(lieferscheinIIdVerkettet);

				verkettetDto.setIId(
						DelegateFactory.getInstance().getLieferscheinServiceDelegate().createVerkettet(verkettetDto));

			}

			if (panelQueryFLRLieferscheinVerketten.getDialog() != null) {
				panelQueryFLRLieferscheinVerketten.getDialog().setVisible(false);
			}

			lsVerkettetSplit.eventYouAreSelected(false);
		} else if (e.getSource() == panelQueryFLRAuftragauswahl) {
			Integer iIdAuftragBasis = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

			if (iIdAuftragBasis != null) {

				AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey(iIdAuftragBasis);

				boolean b = DelegateFactory.getInstance().getKundeDelegate()
						.pruefeKreditlimitMitJaNeinFrage(getInternalFrame(), auftragDto.getKundeIIdRechnungsadresse());

				if (b == true) {

					// SP4131
					AuftragDto aDto = DelegateFactory.getInstance().getAuftragDelegate()
							.auftragFindByPrimaryKey(iIdAuftragBasis);
					if (aDto.getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
						boolean bJa = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("ls.warnung.lsauswiederholendemauftrag"));

						if (bJa == false) {
							return;
						}
					}

					Integer lieferscheinIId = DelegateFactory.getInstance().getAuftragDelegate()
							.erzeugeLieferscheinAusAuftrag(iIdAuftragBasis, null, getInternalFrame());

					initializeDtos(lieferscheinIId); // befuellt den
														// Lieferschein
					// und den Kunden
					getInternalFrame().setKeyWasForLockMe(lieferscheinIId + "");
					lsAuswahl.setSelectedId(lieferscheinIId);

					// wenn es bisher keinen Lieferschein gegeben hat
					if (lsAuswahl.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_LIEFERSCHEINAUSWAHL, false);
					}
					if (bSammellieferschein) {
						// Filter auf die Auftraege
						FilterKriterium[] filtersPos = LieferscheinFilterFactory.getInstance()
								.createFKAuftraegeEinesLieferscheins(lieferscheinIId);
						getLieferscheinAuftraege().setDefaultFilter(filtersPos);
						// setSelectedComponent(lsAuftraege);
						setAuftragDtoSichtAuftrag(DelegateFactory.getInstance().getAuftragDelegate()
								.auftragFindByPrimaryKey(iIdAuftragBasis));
						setSelectedComponent(refreshLieferscheinPositionenSichtAuftrag());
					} else {
						setSelectedComponent(refreshLieferscheinPositionenSichtAuftrag());
					}
				}
			}
		} else if (e.getSource() == panelQueryFLRAuftragauswahlZusatz || e.getSource() == lsAuftraege) {
			Integer iIdAuftrag;
			if (e.getSource() == panelQueryFLRAuftragauswahlZusatz) {
				iIdAuftrag = (Integer) panelQueryFLRAuftragauswahlZusatz.getSelectedId();
			} else {
				iIdAuftrag = (Integer) lsAuftraege.getSelectedId();
			}
			if (iIdAuftrag != null) {
				AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey(iIdAuftrag);

				// PJ19958

				boolean bVerrechnbarAuftrag = Helper
						.short2boolean(DelegateFactory.getInstance().getAuftragServiceDelegate()
								.verrechenbarFindByPrimaryKey(auftragDto.getVerrechenbarIId()).getBVerrechenbar());
				boolean bVerrechnbarLieferschein = Helper.short2boolean(getLieferscheinDto().getBVerrechenbar());
				if (bVerrechnbarAuftrag != bVerrechnbarLieferschein) {
					// Hinweis
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
							LPMain.getTextRespectUISPr("auftrag.verrechenbar.ungleich.lieferschein"));

				}

				// PJ17887
				if (e.getSource() == panelQueryFLRAuftragauswahlZusatz) {
					DialogUnterschiedlicheKonditionen d = new DialogUnterschiedlicheKonditionen(auftragDto,
							getLieferscheinDto(), (InternalFrameLieferschein) getInternalFrame());
					LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
					if (d.bKonditionenUnterschiedlich == true) {
						d.setVisible(true);
						if (d.bAbgebrochen == true) {
							return;
						}
						setLieferscheinDto(DelegateFactory.getInstance().getLsDelegate()
								.lieferscheinFindByPrimaryKey(getLieferscheinDto().getIId()));
					}
				}

				this.setAuftragDtoSichtAuftrag(auftragDto);
				lsPositionenSichtAuftragTop.setDefaultFilter(LieferscheinFilterFactory.getInstance()
						.createFKLieferscheinSichtAuftrag(getLieferscheinDto(), getAuftragDtoSichtAuftrag().getIId()));
				setSelectedComponent(refreshLieferscheinPositionenSichtAuftrag());
			}
		} else if (e.getSource() == panelQueryFLRAngebotauswahl) {
			Integer iIdAngebotBasis = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

			if (iIdAngebotBasis != null) {

				AngebotDto aDto = DelegateFactory.getInstance().getAngebotDelegate()
						.angebotFindByPrimaryKey(iIdAngebotBasis);
				boolean b = DelegateFactory.getInstance().getKundeDelegate()
						.pruefeKreditlimitMitJaNeinFrage(getInternalFrame(), aDto.getKundeIIdAngebotsadresse());

				if (b == true) {

					Integer lieferscheinIId = DelegateFactory.getInstance().getAngebotDelegate()
							.erzeugeLieferscheinAusAngebot(iIdAngebotBasis, getInternalFrame());

					initializeDtos(lieferscheinIId); // befuellt den
														// Lieferschein
					// und den Kunden
					getInternalFrame().setKeyWasForLockMe(lieferscheinIId + "");
					lsAuswahl.setSelectedId(lieferscheinIId);

					// wenn es bisher keinen Lieferschein gegeben hat
					if (lsAuswahl.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_LIEFERSCHEINAUSWAHL, false);
					}

					setSelectedComponent(refreshLieferscheinPositionenSichtAuftrag());
				}
			}
		} else if (e.getSource() == panelQueryFLRBegruendung) {

			if (e.getSource() == panelQueryFLRBegruendung) {
				Integer key = (Integer) panelQueryFLRBegruendung.getSelectedId();
				DelegateFactory.getInstance().getLsDelegate()
						.updateLieferscheinBegruendung(getLieferscheinDto().getIId(), key);
				if (lsKonditionen != null) {
					lsKonditionen.eventYouAreSelected(false);
				}
			}
		} else if (e.getSource() == panelQueryFLRLager_Amazon) {

			if (e.getSource() == panelQueryFLRLager_Amazon) {
				Integer lagerIId = (Integer) panelQueryFLRLager_Amazon.getSelectedId();
				importCSV_Amazon(lagerIId);
			}
		} else if (e.getSource() == panelQueryFLRLager_Shopify) {

			if (e.getSource() == panelQueryFLRLager_Shopify) {
				Integer lagerIId = (Integer) panelQueryFLRLager_Shopify.getSelectedId();
				importCSV_Shopify(lagerIId);
			}
		}

		else if (e.getSource() == panelQueryFLRAnsprechpartner_Rechungsadresse) {
			Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

			getLieferscheinDto().setAnsprechpartnerIIdRechnungsadresse(iIdAnsprechpartner);

			DelegateFactory.getInstance().getLsDelegate().updateLieferscheinOhneWeitereAktion(getLieferscheinDto());
			if (lsKopfdaten != null) {
				lsKopfdaten.eventYouAreSelected(false);
			}

		}

		else if (e.getSource() == panelQueryFLRForecastpositionen) {

			Integer forecastpositionIId = (Integer) panelQueryFLRForecastpositionen.getSelectedId();

			lsPositionenBottom.eventActionNew(e, true, false);
			lsPositionenBottom.eventYouAreSelected(false);
			lsPositionenBottom.positionMitForecastdatenVorbesetzen(forecastpositionIId);
		}

		else if (e.getSource() == panelQueryFLRRechnungsadresseauswahl) {

			Integer key = (Integer) panelQueryFLRRechnungsadresseauswahl.getSelectedId();

			getLieferscheinDto().setKundeIIdRechnungsadresse(key);

			KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(key);

			DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(kundeDto.getIId(),
					LocaleFac.BELEGART_LIEFERSCHEIN, getInternalFrame());

			AnsprechpartnerDto[] anspDtos = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPartnerIIdOhneExc(kundeDto.getPartnerIId());

			if (anspDtos != null && anspDtos.length > 0) {
				panelQueryFLRAnsprechpartner_Rechungsadresse = PartnerFilterFactory.getInstance()
						.createPanelFLRAnsprechpartner(getInternalFrame(), kundeDto.getPartnerIId(), null, true, true);

				new DialogQuery(panelQueryFLRAnsprechpartner_Rechungsadresse);
			} else {
				getLieferscheinDto().setAnsprechpartnerIIdRechnungsadresse(null);
				DelegateFactory.getInstance().getLsDelegate().updateLieferscheinOhneWeitereAktion(getLieferscheinDto());
				if (lsKopfdaten != null) {
					lsKopfdaten.eventYouAreSelected(false);
				}
			}

		} else if (e.getSource() == panelQueryFLRMandant) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();

			String mandantCNr_Gewaehlt = key.toString();

			if (!mandantCNr_Gewaehlt.equals(LPMain.getTheClient().getMandant())) {

				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("ls.fuelle.fehlmengen.anderesmandanten.nach.frage"));
				mf.setLocale(LPMain.getTheClient().getLocUi());

				Object pattern[] = { mandantCNr_Gewaehlt, LPMain.getTheClient().getMandant() };
				String sMsg = mf.format(pattern);

				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(), sMsg);

				if (b == true) {

					// Stichtag abfragen

					DialogDatumseingabe d = new DialogDatumseingabe();
					d.setTitle(LPMain.getTextRespectUISPr("ls.fehlmengen.nachfuellen.stichtag"));

					Calendar c = Calendar.getInstance();
					c.add(Calendar.WEEK_OF_YEAR, 1);
					c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

					d.getWnfDatum().setTimestamp(new java.sql.Timestamp(c.getTime().getTime()));

					LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);

					if (d.datum != null) {

						TreeMap<String, BigDecimal> tmNichtUmgebucht = DelegateFactory.getInstance()
								.getFehlmengeDelegate().fuelleFehlmengenDesAnderenMandantenNach(mandantCNr_Gewaehlt,
										new java.sql.Timestamp(d.datum.getTime()));

						// Ev. Meldung anzeigen

						if (tmNichtUmgebucht.size() > 0) {

							StringBuffer sb = new StringBuffer();
							sb.append(LPMain
									.getTextRespectUISPr("ls.fuelle.fehlmengen.anderesmandanten.nach.error.snrchnr"));

							Iterator it = tmNichtUmgebucht.keySet().iterator();

							while (it.hasNext()) {

								String artikel = (String) it.next();

								BigDecimal bdMenge = tmNichtUmgebucht.get(artikel);

								sb.append("\n");
								sb.append(artikel + ":   "
										+ Helper.formatZahl(bdMenge,
												Defaults.getInstance().getIUINachkommastellenMenge(),
												LPMain.getTheClient().getLocUi()));
							}

							DialogFactory.showMessageMitScrollbar(LPMain.getTextRespectUISPr("lp.info"), sb.toString());

						}
					}
					getPanelAuswahl().eventYouAreSelected(false);
				}
			}

		} else if (e.getSource() == panelQueryFLRLieferschein) {
			Integer lieferscheinIId = (Integer) panelQueryFLRLieferschein.getSelectedId();

			// Vorher die Konditionen gegen den Kudnen pruefen
			LieferscheinDto lsDtoVorhanden = DelegateFactory.getInstance().getLsDelegate()
					.lieferscheinFindByPrimaryKey(lieferscheinIId);

			Integer lieferscheinIIdNeu = null;

			DialogGeaenderteKonditionenVK dialog = new DialogGeaenderteKonditionenVK(lsDtoVorhanden,
					lsDtoVorhanden.getKundeIIdLieferadresse(), getInternalFrame());
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(dialog);

			if (dialog.bKonditionenUnterschiedlich == true) {
				dialog.setVisible(true);

				if (dialog.bAbgebrochen == false) {

					lieferscheinIIdNeu = DelegateFactory.getInstance().getLieferscheinpositionDelegate()
							.createLieferscheinAusLieferschein(lieferscheinIId, dialog.bKundeSelektiert);

				}
			} else {
				lieferscheinIIdNeu = DelegateFactory.getInstance().getLieferscheinpositionDelegate()
						.createLieferscheinAusLieferschein(lieferscheinIId, false);
			}
			if (lieferscheinIIdNeu != null) {
				getLieferscheinAuswahl().clearDirektFilter();
				getLieferscheinAuswahl().eventYouAreSelected(false);
				getLieferscheinAuswahl().setSelectedId(lieferscheinIIdNeu);
				getLieferscheinAuswahl().eventYouAreSelected(false);
				this.setSelectedComponent(refreshLieferscheinKopfdaten());
			}
		}
	}

	public void dialogQueryAngebotFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRAngebotauswahl = AngebotFilterFactory.getInstance().createPanelFLRAngebot(getInternalFrame(), true,
				false, AngebotFilterFactory.getInstance().createFKAngebotNichtAngelegt()); // pro
																							// Angebot
		// 1 Auftrag
		// & 1
		// Lieferschein
		// -> FK
		// offene
		// Angebote
		// eines
		// Mandanten

		new DialogQuery(panelQueryFLRAngebotauswahl);
	}

	private PanelBasis refreshLieferscheinKopfdaten() throws Throwable {
		Integer iIdLieferschein = null;

		if (lsKopfdaten == null) {
			// Der Lieferschein hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdLieferschein = new Integer(Integer.parseInt(getInternalFrame().getKeyWasForLockMe()));
			}

			lsKopfdaten = new PanelLieferscheinKopfdaten(getInternalFrame(),
					LPMain.getTextRespectUISPr("ls.title.panel.kopfdaten"), iIdLieferschein); // empty
			// bei
			// leerer
			// Liste

			setComponentAt(IDX_PANEL_LIEFERSCHEINKOPFDATEN, lsKopfdaten);
		}

		return lsKopfdaten;
	}

	public PanelSplit refreshLieferscheinPositionen() throws Throwable {
		if (lsPositionen == null) {
			lsPositionenBottom = new PanelLieferscheinPositionen(getInternalFrame(),
					LPMain.getTextRespectUISPr("ls.title.panel.positionen"), null); // default
																					// belegung,
																					// eventuell
																					// gibt
																					// es
																					// noch
																					// keine
			// position

			QueryType[] qtPos = null;
			FilterKriterium[] filtersPos = LieferscheinFilterFactory.getInstance()
					.createFKFlrlieferscheiniid(getLieferscheinDto().getIId());

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1, PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					PanelBasis.ACTION_KOPIEREN, PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };

			lsPositionenTop = new PanelQuery(qtPos, filtersPos, QueryParameters.UC_ID_LIEFERSCHEINPOSITION,
					aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("ls.title.panel.positionen"),
					true);

			boolean bForecast = LPMain.getInstance().getDesktop()
					.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FORECAST);

			if (bForecast) {
				lsPositionenTop.createAndSaveAndShowButton("/com/lp/client/res/document_chart.png",
						LPMain.getTextRespectUISPr("ls.positionen.neuausforecastposition"),
						MY_OWN_NEW_POSITION_AUS_FORECAST, RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);
			}

			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_SERIENNUMMER_EINEINDEUTIG);

			if ((Boolean) parameter.getCWertAsObject()) {
				lsPositionenTop.createAndSaveAndShowButton("/com/lp/client/res/laserpointer.png",
						LPMain.getTextRespectUISPr("ls.positionen.barcodeerfassung"), MY_OWN_NEW_SNRBARCODEERFASSUNG,
						RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);
			}

			lsPositionenTop.createAndSaveAndShowButton("/com/lp/client/res/scanner16x16.png",
					LPMain.getTextRespectUISPr("auftrag.positionen.schnelleingabe"),
					BUTTON_SCHNELLERFASSUNG_LIEFERSCHEINPOSITIONEN, RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);

			lsPositionenTop.createAndSaveAndShowButton("/com/lp/client/res/down_plus.png",
					LPMain.getTextRespectUISPr("ls.sortierenachabnummer"), BUTTON_SORTIERE_LS_NACH_AUFTRAGSNUMMER,
					null);

			lsPositionenTop.createAndSaveAndShowButton("/com/lp/client/res/sort_az_descending.png",
					LPMain.getTextRespectUISPr("ls.sortierenachartikelnummer"), BUTTON_SORTIERE_LS_NACH_ARTIKELNUMMER,
					null);

			lsPositionenTop.createAndSaveAndShowButton("/com/lp/client/res/calculator16x16.png",
					LPMain.getTextRespectUISPr("auft.preise.neuberechnen"), ACTION_SPECIAL_PREISE_NEU_KALKULIEREN,
					RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);

			lsPositionen = new PanelSplit(getInternalFrame(), lsPositionenBottom, lsPositionenTop, 165);

			lsPositionenTop.setMultipleRowSelectionEnabled(true);

			setComponentAt(IDX_PANEL_LIEFERSCHEINPOSITIONEN, lsPositionen);
		}

		return lsPositionen;
	}

	public PanelSplit refreshVerkettet() throws Throwable {
		if (lsVerkettetSplit == null) {
			lsVerkettetBottom = new PanelVerkettet(getInternalFrame(), LPMain.getTextRespectUISPr("ls.verkettet"),
					null);
			// position

			QueryType[] qtPos = null;
			FilterKriterium[] filtersPos = LieferscheinFilterFactory.getInstance()
					.createFKVerkettet(getLieferscheinDto().getIId());

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			lsVerkettetTop = new PanelQuery(qtPos, filtersPos, QueryParameters.UC_ID_VERKETTET, aWhichButtonIUse,
					getInternalFrame(), LPMain.getTextRespectUISPr("ls.verkettet"), true);

			lsVerkettetSplit = new PanelSplit(getInternalFrame(), lsVerkettetBottom, lsVerkettetTop, 350);

			lsVerkettetTop.createAndSaveAndShowButton("/com/lp/client/res/truck_red16x16.png",
					LPMain.getTextRespectUISPr("ls.verketten.mehrere"), MY_OWN_NEW_MEHRERE_LIEFERSCHEINE_VERKETTEN,
					RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);

			setComponentAt(IDX_PANEL_VERKETTET, lsVerkettetSplit);
		}

		return lsPositionen;
	}

	public PanelQuery getLieferscheinAuftraege() throws Throwable {
		if (lsAuftraege == null) {
			QueryType[] qtPos = null;
			FilterKriterium[] filtersPos = LieferscheinFilterFactory.getInstance()
					.createFKAuftraegeEinesLieferscheins(getLieferscheinDto().getIId());

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			lsAuftraege = new PanelQuery(qtPos, filtersPos, QueryParameters.UC_ID_AUFTRAEGE_EINES_LIEFERSCHEINS,
					aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("ls.title.panel.auftraege"), true);
			setComponentAt(IDX_PANEL_LIEFERSCHEINAUFTRAEGE, lsAuftraege);
		} else {
			lsAuftraege.setDefaultFilter(LieferscheinFilterFactory.getInstance()
					.createFKAuftraegeEinesLieferscheins(getLieferscheinDto().getIId()));
		}
		this.setTitleLieferschein(LPMain.getTextRespectUISPr("ls.title.panel.auftraege"));

		return lsAuftraege;
	}

	private PanelBasis getLieferscheinKonditionen() throws Throwable {
		if (lsKonditionen == null) {
			lsKonditionen = new PanelLieferscheinKonditionen2(getInternalFrame(),
					LPMain.getTextRespectUISPr("ls.title.panel.konditionen"), getLieferscheinDto().getIId());
			setComponentAt(IDX_PANEL_LIEFERSCHEINKONDITIONEN, lsKonditionen);
		}

		return lsKonditionen;
	}

	private PanelBasis getLieferscheinVersandinfos() throws Throwable {
		if (lsVersandinfos == null) {
			lsVersandinfos = new PanelLieferscheinVersandinfos(getInternalFrame(),
					LPMain.getTextRespectUISPr("ls.versandinfos"), getLieferscheinDto().getIId());
			setComponentAt(IDX_PANEL_VERSANDINFOS, lsVersandinfos);
		}

		return lsVersandinfos;
	}

	/**
	 * Fuer lazy loading.
	 * 
	 * @return PanelBasis
	 * @throws Throwable
	 */
	public PanelSplit refreshLieferscheinPositionenSichtAuftrag() throws Throwable {
		if (lsPositionenSichtAuftrag == null) {
			lsPositionenSichtAuftragBottom = new PanelLieferscheinPositionenSichtAuftrag(getInternalFrame(),
					LPMain.getTextRespectUISPr("ls.title.panel.sichtauftrag"), null,
					getLieferscheinDto().getLagerIId());

			FilterKriterium[] filtersPos = LieferscheinFilterFactory.getInstance()
					.createFKLieferscheinSichtAuftrag(getLieferscheinDto(), getAuftragDtoSichtAuftrag().getIId());

			lsPositionenSichtAuftragTop = new PanelQuery(null, filtersPos,
					QueryParameters.UC_ID_LIEFERSCHEINPOSITIONSICHTAUFTRAG, null, getInternalFrame(),
					LPMain.getTextRespectUISPr("ls.title.panel.sichtauftrag"), true);

			lsPositionenSichtAuftragTop.befuelleFilterkriteriumSchnellansicht(
					LieferscheinFilterFactory.getInstance().createFKSichtAuftragOffeneAnzeigen());

			lsPositionenSichtAuftragTop.getCbSchnellansicht()
					.setText(LPMain.getTextRespectUISPr("ls.sichtauftrag.offeneanzeigen"));

			Dimension d = new Dimension(200, Defaults.getInstance().getControlHeight());

			lsPositionenSichtAuftragTop.getCbSchnellansicht().setPreferredSize(d);
			lsPositionenSichtAuftragTop.getCbSchnellansicht().setMaximumSize(d);

			lsPositionenSichtAuftragTop.setMultipleRowSelectionEnabled(true);
			lsPositionenSichtAuftragTop.createAndSaveAndShowButton("/com/lp/client/res/auftrag16x16.png",
					LPMain.getTextRespectUISPr("ls.allepositionenausauftrag"),
					MY_OWN_NEW_ALLE_POSITIONEN_AUS_AUFTRAG_UEBERNEHMEN, RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);

			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_SERIENNUMMER_EINEINDEUTIG);

			if ((Boolean) parameter.getCWertAsObject()) {
				lsPositionenSichtAuftragTop.createAndSaveAndShowButton("/com/lp/client/res/laserpointer.png",
						LPMain.getTextRespectUISPr("ls.positionen.barcodeerfassung"), MY_OWN_NEW_SNRBARCODEERFASSUNG,
						RechteFac.RECHT_LS_LIEFERSCHEIN_CUD);
			}

			FilterKriteriumDirekt fkDirekt1 = LieferscheinFilterFactory.getInstance().createFKDArtikelnummer();

			lsPositionenSichtAuftragTop.befuellePanelFilterkriterienDirekt(fkDirekt1, null);

			lsPositionenSichtAuftrag = new PanelSplit(getInternalFrame(), lsPositionenSichtAuftragBottom,
					lsPositionenSichtAuftragTop, 160);

			setComponentAt(IDX_PANEL_LIEFERSCHEINPOSITIONENSICHTAUFTRAG, lsPositionenSichtAuftrag);
		}

		return lsPositionenSichtAuftrag;
	}

	public LieferscheinDto getLieferscheinDto() {
		return lieferscheinDto;
	}

	public void setLieferscheinDto(LieferscheinDto dto) throws Throwable {
		lieferscheinDto = dto;
	}

	public KundeDto getKundeLieferadresseDto() {
		return kundeLieferadresseDto;
	}

	public void setKundeLieferadresseDto(KundeDto kundeDto) {
		kundeLieferadresseDto = kundeDto;
	}

	public LieferscheintextDto getKopftextDto() {
		return kopftextDto;
	}

	public void setKopftextDto(LieferscheintextDto kopftextDtoI) {
		kopftextDto = kopftextDtoI;
	}

	public LieferscheintextDto getFusstextDto() {
		return fusstextDto;
	}

	public void setFusstextDto(LieferscheintextDto fusstextDtoI) {
		fusstextDto = fusstextDtoI;
	}

	/**
	 * Alle dtos zuruecksetzen.
	 * 
	 * @throws Throwable
	 */
	public void resetDtos() throws Throwable {
		this.setLieferscheinDto(new LieferscheinDto());
		this.setAuftragDtoSichtAuftrag(new AuftragDto());
		this.kundeLieferadresseDto = new KundeDto();
	}

	/**
	 * Nachdem ein Lieferschein geaehlt wurde, jetzt alle abhaengigen Dtos setzen.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @throws java.lang.Throwable Ausnahme
	 */
	public void initializeDtos(Integer iIdLieferscheinI) throws Throwable {
		if (iIdLieferscheinI != null) {
			setLieferscheinDto(
					DelegateFactory.getInstance().getLsDelegate().lieferscheinFindByPrimaryKey(iIdLieferscheinI));

			kundeLieferadresseDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(getLieferscheinDto().getKundeIIdLieferadresse());

			if (getLieferscheinDto().getLieferscheintextIIdDefaultKopftext() == null
					&& getLieferscheinDto().getLieferscheintextIIdDefaultFusstext() == null) {
				initLieferscheintexte();
			}

			if (getLieferscheinDto().getLieferscheintextIIdDefaultKopftext() != null) {
				kopftextDto = DelegateFactory.getInstance().getLieferscheinServiceDelegate()
						.lieferscheintextFindByPrimaryKey(getLieferscheinDto().getLieferscheintextIIdDefaultKopftext());
			}

			if (getLieferscheinDto().getLieferscheintextIIdDefaultFusstext() != null) {
				fusstextDto = DelegateFactory.getInstance().getLieferscheinServiceDelegate()
						.lieferscheintextFindByPrimaryKey(getLieferscheinDto().getLieferscheintextIIdDefaultFusstext());
			}

			if (!bSammellieferschein) {
				if (getLieferscheinDto().getAuftragIId() != null) {
					setAuftragDtoSichtAuftrag(DelegateFactory.getInstance().getAuftragDelegate()
							.auftragFindByPrimaryKey(getLieferscheinDto().getAuftragIId()));
				} else {
					setAuftragDtoSichtAuftrag(new AuftragDto());
				}
			}
		}
	}

	/**
	 * Den Text der Titelleiste ueberschreiben.
	 * 
	 * @param panelTitle der Title des aktuellen panel
	 * @throws Throwable
	 */
	public void setTitleLieferschein(String panelTitle) throws Throwable {
		// aktuelle lieferscheinnummer bestimmen
		StringBuffer lsnummer = new StringBuffer("");
		if (getLieferscheinDto() == null || getLieferscheinDto().getIId() == null) {
			lsnummer.append(LPMain.getTextRespectUISPr("ls.title.neuerlieferschein"));
		} else {
			KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(getLieferscheinDto().getKundeIIdLieferadresse());

			lsnummer.append(LPMain.getTextRespectUISPr("ls.title.lieferschein")).append(" ")
					.append(getLieferscheinDto().getCNr()).append(" ")
					.append(kundeDto.getPartnerDto().formatFixTitelName1Name2());
		}

		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, panelTitle);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, lsnummer.toString());
	}

	public void setTitleLieferscheinOhneLieferscheinnummer(String panelTitle) throws Exception {
		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, panelTitle);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
	}

	/**
	 * Default Lieferscheinfusstext in einer bestimmten Sprache holen.
	 * 
	 * @param sLocAsString gewuenschtes Locale
	 * @return LieferscheintextDto
	 * @throws java.lang.Throwable Ausnahme
	 */
	public LieferscheintextDto getDefaultFusstext(String sLocAsString) throws Throwable {
		return DelegateFactory.getInstance().getLieferscheinServiceDelegate()
				.lieferscheintextFindByMandantLocaleCNr(sLocAsString, MediaFac.MEDIAART_FUSSTEXT);
	}

	/**
	 * Default Kopftext fuer diesen Lieferschein in der Sprache des Kunden holen.
	 * 
	 * @param sLocAsString gewuenschtes Locale
	 * @return LieferscheintextDto
	 * @throws java.lang.Throwable Ausnahme
	 */
	public LieferscheintextDto getDefaultKopftext(String sLocAsString) throws Throwable {
		return DelegateFactory.getInstance().getLieferscheinServiceDelegate()
				.lieferscheintextFindByMandantLocaleCNr(sLocAsString, MediaFac.MEDIAART_KOPFTEXT);
	}

	public PanelQuery getLieferscheinAuswahl() {
		return lsAuswahl;
	}

	public PanelQuery getLieferscheinPositionenTop() {
		return lsPositionenTop;
	}

	public PanelLieferscheinPositionen getLieferscheinPositionenBottom() {
		return lsPositionenBottom;
	}

	public PanelQuery getLieferscheinPositionenSichtAuftragTop() {
		return lsPositionenSichtAuftragTop;
	}

	protected Integer getSelectedIdPositionen() throws Throwable {
		return (Integer) getLieferscheinPositionenTop().getSelectedId();
	}

	public boolean pruefeAktuellenLieferschein() {
		boolean bIstGueltig = true;

		if (getLieferscheinDto() == null || getLieferscheinDto().getIId() == null) {
			bIstGueltig = false;
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("ls.warning.keinlieferschein"));
		}

		return bIstGueltig;
	}

	/**
	 * Diese Methode prueft, ob zum aktuellen Lieferschein Konditionen erfasst
	 * wurden. <br>
	 * Wenn der Benutzer aufgrund von KONDITIONEN_BESTAETIGEN die Konditionen nicht
	 * bestaetigen muss, muessen die Default Texte vorbelegt werden.
	 * 
	 * @return boolean true, wenn die Konditionen gueltig erfasst wurden
	 * @throws Throwable Ausnahme
	 */
	protected boolean pruefeKonditionen() throws Throwable {
		boolean bErfasst = true;

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_LIEFERSCHEIN,
				ParameterFac.PARAMETER_KONDITIONEN_DES_BELEGS_BESTAETIGEN);

		Short sValue = new Short(parameter.getCWert());

		if (pruefeAktuellenLieferschein()) {
			if (Helper.short2boolean(sValue)) {
				if (getLieferscheinDto().getLieferscheintextIIdDefaultKopftext() == null
						&& getLieferscheinDto().getLieferscheintextIIdDefaultFusstext() == null) {
					bErfasst = false;

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("lp.hint.konditionenerfassen"));
				}
			} else {
				// die Konditionen initialisieren
				initLieferscheinKonditionen();
			}
		}

		return bErfasst;
	}

	/**
	 * Je nach Mandantenparameter muss der Benutzer die Konditionen nicht erfassen.
	 * Damit der Lieferschein gedruckt werden kann, muessen die Konditionen aber
	 * initialisiert worden sein.
	 * 
	 * @throws Throwable Ausnahme
	 */
	public void initLieferscheinKonditionen() throws Throwable {
		initLieferscheintexte(); // Kopf- und Fusstext werden initialisiert

		getLieferscheinDto().setLieferscheintextIIdDefaultKopftext(kopftextDto.getIId());
		getLieferscheinDto().setLieferscheintextIIdDefaultFusstext(fusstextDto.getIId());

		DelegateFactory.getInstance().getLsDelegate().updateLieferscheinOhneWeitereAktion(getLieferscheinDto());
	}

	/**
	 * Kopf- und Fusstext vorbelegen.
	 * 
	 * @throws Throwable Ausnahme
	 */
	public void initLieferscheintexte() throws Throwable {

		try {
			kopftextDto = DelegateFactory.getInstance().getLieferscheinServiceDelegate()
					.lieferscheintextFindByMandantLocaleCNr(
							getKundeLieferadresseDto().getPartnerDto().getLocaleCNrKommunikation(),
							MediaFac.MEDIAART_KOPFTEXT);
		} catch (Throwable t) {
			// wenn es keinen Kopftext gibt
			String localeCNr = LPMain.getTheClient().getLocUiAsString();
			kopftextDto = DelegateFactory.getInstance().getLieferscheinServiceDelegate()
					.createDefaultLieferscheintext(MediaFac.MEDIAART_KOPFTEXT, localeCNr);
		}

		try {
			fusstextDto = DelegateFactory.getInstance().getLieferscheinServiceDelegate()
					.lieferscheintextFindByMandantLocaleCNr(
							getKundeLieferadresseDto().getPartnerDto().getLocaleCNrKommunikation(),
							MediaFac.MEDIAART_FUSSTEXT);
		} catch (Throwable t) {
			// wenn es keinen Fusstext gibt
			String localeCNr = LPMain.getTheClient().getLocUiAsString();
			fusstextDto = DelegateFactory.getInstance().getLieferscheinServiceDelegate()
					.createDefaultLieferscheintext(MediaFac.MEDIAART_FUSSTEXT, localeCNr);
		}

	}

	public boolean esGibtOffeneLieferscheine() throws Throwable {
		boolean bGibtOffene = true;

		if (DelegateFactory.getInstance().getLsDelegate().berechneAnzahlDerOffenenLieferscheine() <= 0) {
			bGibtOffene = false;

			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("ls.warning.keineoffenenlieferscheine"));
		}

		return bGibtOffene;
	}

	public boolean aktuellerLieferscheinHatPositionen() throws Throwable {
		boolean bGibtPositionen = true;

		if (DelegateFactory.getInstance().getLieferscheinpositionDelegate()
				.berechneAnzahlMengenbehaftetePositionen(getLieferscheinDto().getIId()) <= 0) {
			bGibtPositionen = false;
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("ls.warning.keinepositionen"));
		}

		return bGibtPositionen;
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(MENU_ACTION_DATEI_LIEFERSCHEIN)) {
			printLieferschein();
		} else if (e.getActionCommand().equals(MENU_ACTION_DATEI_LIEFERSCHEINETIKETT)) {
			printLieferscheinetikett();
		} else if (e.getActionCommand().equals(MENU_ACTION_DATEI_LIEFERSCHEINWAETIKETT)) {
			printLieferscheinwaetikett(null);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_CSVIMPORT_SHOPIFY)) {
			panelQueryFLRLager_Shopify = ArtikelFilterFactory.getInstance().createPanelFLRLager(getInternalFrame(),
					null);
			new DialogQuery(panelQueryFLRLager_Shopify);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_CSVIMPORT_AMAZON)) {

			panelQueryFLRLager_Amazon = ArtikelFilterFactory.getInstance().createPanelFLRLager(getInternalFrame(),
					null);
			new DialogQuery(panelQueryFLRLager_Amazon);
		} else if (e.getActionCommand().equals(MENU_ACTION_DATEI_VERSANDETIKETTEN)) {
			ReportVersandetiketten ve = new ReportVersandetiketten(getInternalFrame(), getLieferscheinDto(),
					getTitleDruck());
			ve.eventYouAreSelected(false);
			ve.setKeyWhenDetailPanel(getLieferscheinDto().getIId());
			// man kann unabhaengig vom Status beliebig oft drucken
			getInternalFrame().showReportKriterien(ve, getKundeLieferadresseDto().getPartnerDto(),
					getLieferscheinDto().getAnsprechpartnerIId(), false);
			ve.updateButtons();
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_ANGELEGT)) {
			getInternalFrame().showReportKriterien(
					new ReportLieferscheinAngelegte(getInternalFrame(),
							LPMain.getTextRespectUISPr("ls.print.listeangelegt")),
					getKundeLieferadresseDto().getPartnerDto(), getLieferscheinDto().getAnsprechpartnerIId(), false);
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_ALLE)) {
			getInternalFrame().showReportKriterien(
					new ReportLieferscheinAlleLieferscheine(getInternalFrame(),
							LPMain.getTextRespectUISPr("ls.menu.journal.alle")),
					getKundeLieferadresseDto().getPartnerDto(), getLieferscheinDto().getAnsprechpartnerIId(), false);
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_OFFEN)) {
			getInternalFrame().showReportKriterien(
					new ReportLieferscheinOffene(getInternalFrame(),
							LPMain.getTextRespectUISPr("ls.print.listeoffene")),
					getKundeLieferadresseDto().getPartnerDto(), getLieferscheinDto().getAnsprechpartnerIId(), false);
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_OFFENE_RUECKGABEN)) {
			getInternalFrame().showReportKriterien(
					new ReportLieferscheinOffeneRueckgaben(getInternalFrame(),
							LPMain.getTextRespectUISPr("ls.print.listeoffene.rueckgaben")),
					getKundeLieferadresseDto().getPartnerDto(), getLieferscheinDto().getAnsprechpartnerIId(), false);
		} else
		// ptclient: 6 Die Ubersicht wurde ueber das Menue gewaehlt
		if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_UEBERSICHT)) {
			getKriterienLieferscheinUmsatz();
			getInternalFrame().showPanelDialog(pdKriterienLsUebersicht);
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_UMSATZUEBERSICHT)) {
			getKriterienLieferscheinUmsatz();
			getInternalFrame().showPanelDialog(pdKriterienLsUmsatz);
			bKriterienLsUmsatzUeberMenueAufgerufen = true;
		} else if (e.getActionCommand().equals(MENU_ACTION_JOURNAL_PACKSTUECKE)) {
			String add2Title = LPMain.getTextRespectUISPr("ls.report.packstuecke");
			getInternalFrame().showReportKriterien(new ReportPackstuecke(getInternalFrame(), add2Title));
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_FUELLE_FEHLEMGEN_DES_ANDEREN_MANDANTEN_NACH)) {
			// Zuerst Mandant auswaehlen
			String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };

			FilterKriterium[] filtersI = new FilterKriterium[1];
			filtersI[0] = new FilterKriterium("c_nr", true, "'" + LPMain.getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_NOT_EQUAL, false);

			panelQueryFLRMandant = new PanelQueryFLR(null, filtersI, QueryParameters.UC_ID_MANDANT, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("ls.fuelle.fehlmengen.anderesmandanten.nach.zielmandant.auswaehlen"));

			new DialogQuery(panelQueryFLRMandant);

		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_BEGRUENDUNG)) {

			panelQueryFLRBegruendung = LieferscheinFilterFactory.getInstance()
					.createPanelFLRBegruendung(getInternalFrame(), null, true);
			new DialogQuery(panelQueryFLRBegruendung);

		}else if (e.getActionCommand().equals(MENU_BEARBEITEN_INTERNER_KOMMENTAR)) {
			if (pruefeAktuellenLieferschein()) {
				if (!refreshLieferscheinKopfdaten().isLockedDlg()) {
				

					pdLieferscheinInternerKommentar = new PanelDialogLieferscheinKommentar(getInternalFrame(),
							LPMain.getTextRespectUISPr("lp.internerkommentar"), true);
					
					getInternalFrame().showPanelDialog(pdLieferscheinInternerKommentar);
					setTitleLieferschein(LPMain.getTextRespectUISPr("lp.internerkommentar"));
				}
			}
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_MANUELL_ERLEDIGEN)) {
			if (pruefeAktuellenLieferschein()) {
				if (!refreshLieferscheinKopfdaten().isLockedDlg()) {

					// Statuswechsel 'Geliefert' -> 'Erledigt' : Ausgeloest
					// durch Menue
					if (getLieferscheinDto().getStatusCNr().equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
						if (DialogFactory.showMeldung(LPMain.getTextRespectUISPr("ls.status.auferledigtsetzen"),
								LPMain.getTextRespectUISPr("lp.hint"),
								javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {

							DelegateFactory.getInstance().getLsDelegate()
									.manuellErledigen(getLieferscheinDto().getIId());
							refreshAktuellesPanel();
						}
					} else {
						showStatusMessage("lp.warning", "ls.warning.kannnichterledigtwerden");
					}
				}
			}
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_RE_ADRESSE_AENDERN)) {

			// Vorher Warnung
			if (getLieferscheinDto() != null) {
				boolean bAendern = true;

				if (getLieferscheinDto().getStatusCNr().equals(LieferscheinFac.LSSTATUS_ANGELEGT)
						|| getLieferscheinDto().getStatusCNr().equals(LieferscheinFac.LSSTATUS_OFFEN)) {

					if (DelegateFactory.getInstance().getLieferscheinpositionDelegate()
							.berechneAnzahlMengenbehaftetePositionen(getLieferscheinDto().getIId()) > 0) {
						bAendern = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("ls.menu.bearbeiten.readresse.aendern.warnung"));
					}

					if (bAendern == true) {
						panelQueryFLRRechnungsadresseauswahl = PartnerFilterFactory.getInstance().createPanelFLRKunde(
								getInternalFrame(), true, false, getLieferscheinDto().getKundeIIdRechnungsadresse());
						new DialogQuery(panelQueryFLRRechnungsadresseauswahl);
					}
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("ls.menu.bearbeiten.readresse.aendern.error"));

				}
			}

		} else if (MENU_ACTION_DATEI_IMPORT_EASYDATA_STOCK_MOVEMENT.equals(e.getActionCommand())) {
			actionImportEasydataStockMovementsXML();
		} else if (MENU_ACTION_DATEI_PLC_VERSANDETIKETTEN.equals(e.getActionCommand())) {
			boolean plcVersand = DelegateFactory.getInstance().getLsDelegate()
					.isPaketEtikettErzeugbar(lieferscheinDto.getIId());
			if (!plcVersand)
				return;

			ReportPostVersandetiketten ve = new ReportPostVersandetiketten(getInternalFrame(), getLieferscheinDto(),
					getTitleDruck());
			ve.eventYouAreSelected(false);
			ve.setKeyWhenDetailPanel(getLieferscheinDto().getIId());

			getInternalFrame().showReportKriterien(ve, getKundeLieferadresseDto().getPartnerDto(),
					getLieferscheinDto().getAnsprechpartnerIId(), false);
			ve.updateButtons();
		}
	}

	@Override
	protected void initDtos() throws Throwable {
		initializeDtos(getLieferscheinDto().getIId());
	}

	protected boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;
		int code = exfc.getICode();

		switch (code) {
		case EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT:
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("auft.mandant.hauptlager_fehlt"));
			break;

		case EJBExceptionLP.FEHLER_LIEFERSCHEIN_KEINEOFFENENLIEFERSCHEINE:
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("ls.warning.keineoffenenlieferscheine"));
			break;

		case EJBExceptionLP.FEHLER_LIEFERSCHEIN_KEINEANGELEGTENLIEFERSCHEINE:
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("ls.warning.keineangelegtenlieferscheine"));
			break;

		case EJBExceptionLP.ARTIKEL_KEINE_LAGERBUCHUNG_VORHANDEN:
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("ls.error.lagerbuchung"));
			break;

		case EJBExceptionLP.FEHLER_FORMAT_NUMBER:
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.belegwerte"));
			break;

		case EJBExceptionLP.FEHLER_DLG_DONE_DO_NOTHING:

			// die laufende Aktion wurde abgebrochen
			break;

		default:
			bErrorErkannt = false;
		}

		return bErrorErkannt;
	}

	public void printLieferschein() throws Throwable {

		if (pruefeKonditionen()) {

			if (!refreshLieferscheinKopfdaten().isLockedDlg()) {
				getInternalFrame().showReportKriterien(
						new ReportLieferschein(getInternalFrame(), getAktuellesPanel(), getLieferscheinDto(),
								getTitleDruck()),
						getKundeLieferadresseDto().getPartnerDto(), getLieferscheinDto().getAnsprechpartnerIId(),
						false);
			}
		}

	}

	public void printLieferscheinetikett() throws Throwable {

		if (pruefeAktuellenLieferschein()) {

			boolean bHatVersandRecht = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_LS_LIEFERSCHEIN_VERSAND);
			ReportLieferscheinAdressetikett rla = new ReportLieferscheinAdressetikett(getInternalFrame(),
					getLieferscheinDto(), getTitleDruck());
			rla.eventYouAreSelected(false);
			rla.setKeyWhenDetailPanel(getLieferscheinDto().getIId());
			getInternalFrame().showReportKriterien(rla, getKundeLieferadresseDto().getPartnerDto(),
					getLieferscheinDto().getAnsprechpartnerIId(), false);
			rla.updateButtons(bHatVersandRecht);

		}
	}

	public void printLieferscheinwaetikett(Integer lieferscheinpositionIId) throws Throwable {

		if (pruefeAktuellenLieferschein()) {
			if (aktuellerLieferscheinHatPositionen()) {
				ReportLieferscheinWAEetikett rwae = new ReportLieferscheinWAEetikett(getInternalFrame(),
						getLieferscheinDto(), lieferscheinpositionIId, getTitleDruck());
				getInternalFrame().showReportKriterien(rwae, getKundeLieferadresseDto().getPartnerDto(),
						getLieferscheinDto().getAnsprechpartnerIId(), false);
			}
		}
	}

	private String getTitleDruck() {
		StringBuffer buff = new StringBuffer();

		buff.append(getLieferscheinDto().getCNr());
		buff.append(" ");
		buff.append(getKundeLieferadresseDto().getPartnerDto().getCName1nachnamefirmazeile1());

		return buff.toString();
	}

	public void dialogQueryAuftragFromListe(ActionEvent e) throws Throwable {
		FilterKriterium[] fk = LieferscheinFilterFactory.getInstance().createFKPanelQueryFLRAuftragAuswahl();
		panelQueryFLRAuftragauswahl = AuftragFilterFactory.getInstance().createPanelFLRAuftrag(getInternalFrame(), true,
				false, fk);
		new DialogQuery(panelQueryFLRAuftragauswahl);
	}

	public void dialogQueryAuftragFromListeZusatz(ActionEvent e) throws Throwable {
		// Der muss die gleichen Adressen haben wie der Hauptauftrag
		if (getLieferscheinDto().getAuftragIId() != null) {
			AuftragDto auftragDtoHA = DelegateFactory.getInstance().getAuftragDelegate()
					.auftragFindByPrimaryKey(getLieferscheinDto().getAuftragIId());
			// die Auftraege, die schon in der Tabelle stehen, sollen
			// rausgefiltert werden
			Integer[] auftragIIdNA = new Integer[getLieferscheinAuftraege().getTable().getRowCount() + 1];
			// Auch der Kopfauftrag
			auftragIIdNA[0] = getLieferscheinDto().getAuftragIId();
			// alle auftrag-ids aus der Tabelle
			for (int i = 0; i < getLieferscheinAuftraege().getTable().getRowCount(); i++) {
				auftragIIdNA[i + 1] = (Integer) getLieferscheinAuftraege().getTable().getValueAt(i, 0);
			}
			FilterKriterium[] fk = LieferscheinFilterFactory.getInstance().createFKPanelQueryFLRAuftragAuswahl(
					auftragDtoHA.getKundeIIdLieferadresse(), auftragDtoHA.getKundeIIdRechnungsadresse(),
					auftragDtoHA.getKundeIIdAuftragsadresse(), auftragIIdNA);
			panelQueryFLRAuftragauswahlZusatz = AuftragFilterFactory.getInstance()
					.createPanelFLRAuftrag(getInternalFrame(), true, false, fk);
			new DialogQuery(panelQueryFLRAuftragauswahlZusatz);
		}
	}

	public LagerDto getHauptlagerDesMandanten() throws Throwable {
		LagerDto oLagerDto = DelegateFactory.getInstance().getLagerDelegate().getHauptlagerDesMandanten();
		return oLagerDto;
	}

	public boolean istLieferscheinAuftragbezogen() throws Throwable {
		boolean bIstAuftragbezogen = false;

		if (pruefeAktuellenLieferschein()) {
			if (getLieferscheinDto().getLieferscheinartCNr().equals(LieferscheinFac.LSART_AUFTRAG)) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
						LPMain.getTextRespectUISPr("ls.warning.lsistauftragbezogen"));

				bIstAuftragbezogen = true;
			}
		}

		return bIstAuftragbezogen;
	}

	/**
	 * Diese Methode setzt des aktuellen Auftrag aus der Auswahlliste als den zu
	 * lockenden Auftrag.
	 * 
	 * @throws java.lang.Throwable Ausnahme
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = lsAuswahl.getSelectedId();

		if (oKey != null) {
			initializeDtos((Integer) oKey);
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	/*
	 * private PanelTabelle getLieferscheinUebersicht() { if
	 * (ptLieferscheinUebersicht == null) { ptLieferscheinUebersicht = new
	 * PanelTabelleLieferscheinUebersicht(
	 * QueryParameters.UC_ID_LIEFERSCHEINUEBERSICHT,
	 * LPMain.getInstance().getTextRespectUISPr( "ls.title.panel.uebersicht"),
	 * getInternalFrame()); setComponentAt(IDX_PANEL_LIEFERSCHEINUEBERSICHT,
	 * ptLieferscheinUebersicht); }
	 * 
	 * return ptLieferscheinUebersicht; }
	 */

	private PanelTabelle getLieferscheinUmsatz() throws Throwable {
		if (ptLieferscheinUmsatz == null) {
			ptLieferscheinUmsatz = new PanelTabelleLieferscheinUmsatz(QueryParameters.UC_ID_LIEFERSCHEINUMSATZ,
					LPMain.getTextRespectUISPr("lp.umsatzuebersicht"), getInternalFrame());
			setComponentAt(IDX_PANEL_LIEFERSCHEINUMSATZ, ptLieferscheinUmsatz);
		}

		return ptLieferscheinUmsatz;
	}

	/**
	 * Der Status des Lieferscheins kann in einigen Faellen ueber den Update Button
	 * geaendert werden.
	 * 
	 * @return boolean true, wenn der aktuelle Lieferschein geaendert werden darf
	 * @throws Throwable Ausnahme
	 */

	public boolean aktualisiereLieferscheinstatusDurchButtonUpdate() throws Throwable {
		boolean bIstAktualisierenErlaubt = false;

		if (pruefeAktuellenLieferschein()) {

			// SP7403
			RechnungDto reDto = DelegateFactory.getInstance().getRechnungDelegate()
					.istLieferscheinBereitsInProformarechnung(getLieferscheinDto().getIId());
			if (reDto != null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getMessageTextRespectUISPr("ls.hint.bereitsinproformarechnung", reDto.getCNr()));
				return false;
			}

			if (getLieferscheinDto().getStatusCNr().equals(LieferscheinFac.LSSTATUS_ANGELEGT)) {
				bIstAktualisierenErlaubt = true;
			}

			else if (getLieferscheinDto().getStatusCNr().equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
				bIstAktualisierenErlaubt = (DialogFactory.showMeldung(
						LPMain.getTextRespectUISPr("ls.hint.offennachangelegt"), LPMain.getTextRespectUISPr("lp.frage"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);

				if (bIstAktualisierenErlaubt == true) {
					DelegateFactory.getInstance().getLsDelegate().setzeStatusLieferschein(getLieferscheinDto().getIId(),
							LieferscheinFac.LSSTATUS_ANGELEGT);
				}

			}

			else if (getLieferscheinDto().getStatusCNr().equals(LieferscheinFac.LSSTATUS_STORNIERT)) {

				if (DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("ls.stornoaufheben"))) {
					DelegateFactory.getInstance().getLsDelegate().stornoAufheben(getLieferscheinDto().getIId());
					bIstAktualisierenErlaubt = true;
				}

			}

			else if (getLieferscheinDto().getStatusCNr().equals(LieferscheinFac.LSSTATUS_ERLEDIGT)) {
				if (DialogFactory.showMeldung(LPMain.getTextRespectUISPr("ls.hint.lieferscheinerledigt"),
						LPMain.getTextRespectUISPr("lp.hint"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
					DelegateFactory.getInstance().getLsDelegate().erledigungAufheben(getLieferscheinDto().getIId());
					refreshAktuellesPanel();
				}
			} else {
				showStatusMessage("lp.warning", "ls.warning.lskannnichtgeaendertwerden");
			}
		}

		return bIstAktualisierenErlaubt;
	}

	// public boolean aktualisiereLieferscheinstatusDurchButtonUpdate()
	// throws Throwable {
	// boolean bIstAktualisierenErlaubt = false;
	//
	// if (pruefeAktuellenLieferschein()) {
	//
	// // im Status 'Angelegt' duerfen die Kopfdaten geaendert werden
	// if (getLieferscheinDto().getStatusCNr().equals(
	// LieferscheinFac.LSSTATUS_ANGELEGT)) {
	// bIstAktualisierenErlaubt = true;
	// } else
	//
	// // im Status 'Offen' duerfen die Kopfdaten geaendert werden
	// if (getLieferscheinDto().getStatusCNr().equals(
	// LieferscheinFac.LSSTATUS_GELIEFERT)) {
	// bIstAktualisierenErlaubt = true;
	// } else
	//
	// // Statuswechsel 'Storniert' -> 'Geliefert' : Ausgeloest durch
	// // Button Update
	// if (getLieferscheinDto().getStatusCNr().equals(
	// LieferscheinFac.LSSTATUS_STORNIERT)) {
	// DialogFactory.showModalDialog(LPMain.getInstance()
	// .getTextRespectUISPr("lp.hint"), LPMain.getInstance()
	// .getTextRespectUISPr("ls.stornoaufheben"));
	// } else
	//
	// // Statuswechsel 'Erledigt' -> 'Geliefert' : Ausgeloest durch Button
	// // Update
	// if (getLieferscheinDto().getStatusCNr().equals(
	// LieferscheinFac.LSSTATUS_ERLEDIGT)) {
	// if (DialogFactory.showMeldung(LPMain.getInstance()
	// .getTextRespectUISPr("ls.hint.lieferscheinerledigt"),
	// LPMain.getInstance().getTextRespectUISPr("lp.hint"),
	// javax.swing.JOptionPane.YES_NO_OPTION) ==
	// javax.swing.JOptionPane.YES_OPTION) {
	// DelegateFactory.getInstance().getLsDelegate()
	// .erledigungAufheben(getLieferscheinDto().getIId());
	// refreshAktuellesPanel();
	// }
	// } else {
	// showStatusMessage("lp.warning",
	// "ls.warning.lskannnichtgeaendertwerden");
	// }
	// }
	//
	// return bIstAktualisierenErlaubt;
	// }

	/**
	 * Diese Methode prueft den Status des aktuellen Lieferscheins und legt fest, ob
	 * eine Aenderung in den Kopfdaten bzw. Konditionen erlaubt ist.
	 * 
	 * @return boolean true, wenn ein update erlaubt ist
	 * @throws java.lang.Throwable Ausnahme
	 */
	public boolean istAktualisierenLieferscheinErlaubt() throws Throwable {
		boolean bIstAenderungErlaubtO = false;

		if (pruefeAktuellenLieferschein()) {

			// SP7403
			RechnungDto reDto = DelegateFactory.getInstance().getRechnungDelegate()
					.istLieferscheinBereitsInProformarechnung(getLieferscheinDto().getIId());
			if (reDto != null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getMessageTextRespectUISPr("ls.hint.bereitsinproformarechnung", reDto.getCNr()));
				return false;
			}

			if (getLieferscheinDto().getStatusCNr().equals(LieferscheinFac.LSSTATUS_ANGELEGT)) {
				bIstAenderungErlaubtO = true;
			} else if (getLieferscheinDto().getStatusCNr().equals(LieferscheinFac.LSSTATUS_OFFEN)
					|| getLieferscheinDto().getStatusCNr().equals(LieferscheinFac.LSSTATUS_GELIEFERT)) {
				boolean bZuruecknehmen = (DialogFactory.showMeldung(
						LPMain.getTextRespectUISPr("ls.hint.offennachangelegt"), LPMain.getTextRespectUISPr("lp.frage"),
						javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
				if (bZuruecknehmen == true) {
					DelegateFactory.getInstance().getLsDelegate().setzeStatusLieferschein(getLieferscheinDto().getIId(),
							LieferscheinFac.LSSTATUS_ANGELEGT);
				}
				return bZuruecknehmen;
			}

			else {
				showStatusMessage("lp.warning", "ls.warning.lskannnichtgeaendertwerden");
			}
		}

		return bIstAenderungErlaubtO;
	}

	// public boolean istAktualisierenLieferscheinErlaubt() throws Throwable {
	// boolean bIstAenderungErlaubtO = false;
	//
	// if (pruefeAktuellenLieferschein()) {
	// if (getLieferscheinDto().getStatusCNr().equals(
	// LieferscheinFac.LSSTATUS_ANGELEGT)
	// || getLieferscheinDto().getStatusCNr().equals(
	// LieferscheinFac.LSSTATUS_OFFEN)
	// || getLieferscheinDto().getStatusCNr().equals(
	// LieferscheinFac.LSSTATUS_GELIEFERT)) {
	// bIstAenderungErlaubtO = true;
	// } else {
	// showStatusMessage("lp.warning",
	// "ls.warning.lskannnichtgeaendertwerden");
	// }
	// }
	//
	// return bIstAenderungErlaubtO;
	// }

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wrapperMenuBar = new WrapperMenuBar(this);

		// Menue Datei; ein neuer Eintrag wird immer oben im Menue eingetragen
		JMenu jmModul = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_MODUL);

		jmModul.add(new JSeparator(), 0);
		if (hasZusatzfunktion4Vending()) {
			JMenu menuDateiImport = new WrapperMenu("lp.import", this);
			JMenuItem menuItemDateiImportEasydata = new JMenuItem(
					LPMain.getTextRespectUISPr("ls.menu.datei.4vending.lagerumbuchung"));
			menuItemDateiImportEasydata.addActionListener(this);
			menuItemDateiImportEasydata.setActionCommand(MENU_ACTION_DATEI_IMPORT_EASYDATA_STOCK_MOVEMENT);
			menuDateiImport.add(menuItemDateiImportEasydata);
			jmModul.add(menuDateiImport, 0);
		}

		boolean bSchreibrecht = false;
		if (getInternalFrame().getRechtModulweit().equals(RechteFac.RECHT_MODULWEIT_UPDATE)) {
			bSchreibrecht = true;
		}

		if (bSchreibrecht) {
			JMenu abimport = new JMenu(LPMain.getTextRespectUISPr("lp.import"));
			jmModul.add(abimport, jmModul.getItemCount() - 2);

			JMenuItem menuItemImportShopify = new JMenuItem(LPMain.getTextRespectUISPr("auft.import.shopify"));
			menuItemImportShopify.addActionListener(this);
			menuItemImportShopify.setActionCommand(ACTION_SPECIAL_CSVIMPORT_SHOPIFY);
			abimport.add(menuItemImportShopify);

			/*
			 * JMenuItem menuItemImportAmazon = new
			 * JMenuItem(LPMain.getTextRespectUISPr("auft.import.amazon"));
			 * menuItemImportAmazon.addActionListener(this);
			 * menuItemImportAmazon.setActionCommand(ACTION_SPECIAL_CSVIMPORT_AMAZON);
			 * abimport.add(menuItemImportAmazon);
			 */
		}

		JMenuItem menuItemDateiVersandetikett = new JMenuItem(
				LPMain.getTextRespectUISPr("ls.versandetiketten") + "...");
		menuItemDateiVersandetikett.addActionListener(this);
		menuItemDateiVersandetikett.setActionCommand(MENU_ACTION_DATEI_VERSANDETIKETTEN);
		jmModul.add(menuItemDateiVersandetikett, 0);
		JMenuItem menuItemDateiLieferscheinWAEtikett = new JMenuItem(
				LPMain.getTextRespectUISPr("ls.menu.datei.lieferscheinwaetikett"));
		menuItemDateiLieferscheinWAEtikett.addActionListener(this);
		menuItemDateiLieferscheinWAEtikett.setActionCommand(MENU_ACTION_DATEI_LIEFERSCHEINWAETIKETT);
		jmModul.add(menuItemDateiLieferscheinWAEtikett, 0);

		JMenuItem menuItemDateiLieferscheinetikett = new JMenuItem(
				LPMain.getTextRespectUISPr("ls.menu.datei.lieferscheinetikett"));
		menuItemDateiLieferscheinetikett.addActionListener(this);
		menuItemDateiLieferscheinetikett.setActionCommand(MENU_ACTION_DATEI_LIEFERSCHEINETIKETT);
		jmModul.add(menuItemDateiLieferscheinetikett, 0);

		JMenuItem menuItemDateiLieferschein = new JMenuItem(LPMain

				.getTextRespectUISPr("ls.menu.datei.lieferschein"));
		menuItemDateiLieferschein.addActionListener(this);
		menuItemDateiLieferschein.setActionCommand(MENU_ACTION_DATEI_LIEFERSCHEIN);
		jmModul.add(menuItemDateiLieferschein, 0);

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_POST_PLC_VERSAND)) {
			JMenuItem menuItemDateiPlcVersandetikett = new JMenuItem(
					"PLC-" + LPMain.getTextRespectUISPr("ls.versandetiketten") + "...");
			menuItemDateiPlcVersandetikett.addActionListener(this);
			menuItemDateiPlcVersandetikett.setActionCommand(MENU_ACTION_DATEI_PLC_VERSANDETIKETTEN);
			jmModul.add(menuItemDateiPlcVersandetikett);
		}

		// Menue Bearbeiten
		JMenu jmBearbeiten = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

		JMenuItem menuItemBearbeitenManuellErledigen = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.menuellerledigen"));
		menuItemBearbeitenManuellErledigen.addActionListener(this);
		menuItemBearbeitenManuellErledigen.setActionCommand(MENU_BEARBEITEN_MANUELL_ERLEDIGEN);
		jmBearbeiten.add(menuItemBearbeitenManuellErledigen, 0);

		JMenuItem menuItemBearbeitenBegruendung = new JMenuItem(LPMain.getTextRespectUISPr("ls.begruendung"));
		menuItemBearbeitenBegruendung.addActionListener(this);
		menuItemBearbeitenBegruendung.setActionCommand(MENU_BEARBEITEN_BEGRUENDUNG);
		jmBearbeiten.add(menuItemBearbeitenBegruendung, 1);

		JMenuItem menuItemBearbeitenReAdresseAendern = new JMenuItem(
				LPMain.getTextRespectUISPr("ls.menu.bearbeiten.readresse.aendern"));
		menuItemBearbeitenReAdresseAendern.addActionListener(this);
		menuItemBearbeitenReAdresseAendern.setActionCommand(MENU_BEARBEITEN_RE_ADRESSE_AENDERN);
		jmBearbeiten.add(menuItemBearbeitenReAdresseAendern, 2);

		JMenuItem menuItemBearbeitenInternerKommentar = new JMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.internerkommentar"));
		menuItemBearbeitenInternerKommentar.addActionListener(this);
		menuItemBearbeitenInternerKommentar.setActionCommand(MENU_BEARBEITEN_INTERNER_KOMMENTAR);
		jmBearbeiten.add(menuItemBearbeitenInternerKommentar, 3);
		
		// SP2947
		MandantDto[] mandantDtos = DelegateFactory.getInstance().getMandantDelegate().mandantFindAll();
		if (mandantDtos.length > 1) {

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {

				JMenuItem menuItemBearbeitenFuelleNach = new JMenuItem(
						LPMain.getTextRespectUISPr("ls.fuelle.fehlmengen.anderesmandanten.nach"));
				menuItemBearbeitenFuelleNach.addActionListener(this);
				menuItemBearbeitenFuelleNach
						.setActionCommand(MENU_BEARBEITEN_FUELLE_FEHLEMGEN_DES_ANDEREN_MANDANTEN_NACH);
				jmBearbeiten.add(menuItemBearbeitenFuelleNach, 2);
			}
		}

		// Menue Journal
		JMenu jmJournal = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_JOURNAL);

		JMenuItem menuItemJournalAngelegt = new JMenuItem(LPMain.getTextRespectUISPr("ls.menu.journal.angelegt"));
		menuItemJournalAngelegt.addActionListener(this);
		menuItemJournalAngelegt.setActionCommand(MENU_ACTION_JOURNAL_ANGELEGT);
		jmJournal.add(menuItemJournalAngelegt);

		JMenuItem menuItemJournalAlle = new JMenuItem(LPMain.getTextRespectUISPr("ls.menu.journal.alle"));
		menuItemJournalAlle.addActionListener(this);
		menuItemJournalAlle.setActionCommand(MENU_ACTION_JOURNAL_ALLE);
		jmJournal.add(menuItemJournalAlle);

		JMenuItem menuItemJournalOffene = new JMenuItem(LPMain.getTextRespectUISPr("ls.menu.journal.offen"));
		menuItemJournalOffene.addActionListener(this);
		menuItemJournalOffene.setActionCommand(MENU_ACTION_JOURNAL_OFFEN);
		jmJournal.add(menuItemJournalOffene);
		JMenuItem menuItemJournalOffeneRueckgaben = new JMenuItem(
				LPMain.getTextRespectUISPr("ls.menu.journal.offen.rueckgaben"));
		menuItemJournalOffeneRueckgaben.addActionListener(this);
		menuItemJournalOffeneRueckgaben.setActionCommand(MENU_ACTION_JOURNAL_OFFENE_RUECKGABEN);
		jmJournal.add(menuItemJournalOffeneRueckgaben);

		/*
		 * jmJournal.add(new JSeparator()); JMenuItem menuItemJournalUebersicht = new
		 * JMenuItem(LPMain.getInstance(). getTextRespectUISPr(
		 * "ls.menu.journal.uebersicht"));
		 * menuItemJournalUebersicht.addActionListener(this); menuItemJournalUebersicht
		 * .setActionCommand(MENU_ACTION_JOURNAL_UEBERSICHT);
		 * jmJournal.add(menuItemJournalUebersicht);
		 */

		jmJournal.add(new JSeparator());

		WrapperMenuItem menuItemJournalUmsatzTag = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("lp.menu.umsatzuebersicht"), RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
		menuItemJournalUmsatzTag.addActionListener(this);
		menuItemJournalUmsatzTag.setActionCommand(MENU_ACTION_JOURNAL_UMSATZUEBERSICHT);
		jmJournal.add(menuItemJournalUmsatzTag);
		menuItemJournalUmsatzTag.setEnabled(bDarfPreiseSehen);

		jmJournal.add(new JSeparator());

		WrapperMenuItem menuItemPackstuecke = new WrapperMenuItem(LPMain.getTextRespectUISPr("ls.report.packstuecke"),
				null);
		menuItemPackstuecke.addActionListener(this);
		menuItemPackstuecke.setActionCommand(MENU_ACTION_JOURNAL_PACKSTUECKE);
		jmJournal.add(menuItemPackstuecke);

		return wrapperMenuBar;
	}

	public void gotoAuswahl() throws Throwable {
		setSelectedComponent(lsAuswahl);
		setTitleLieferschein(LPMain.getTextRespectUISPr("ls.title.panel.auswahl"));
	}

	public void gotoPositionenAusSichtAuftrag() throws Throwable {
		// wir kommen aus einem abgebrochenen eventActionNew
		getInternalFrame().setKeyWasForLockMe(getLieferscheinDto().getIId() + "");

		setEnabledAt(IDX_PANEL_LIEFERSCHEINPOSITIONEN, true);

		// die neuen Positionen einlesen
		setSelectedComponent(lsPositionen);
	}

	public void gotoPositionenSichtAuftrag() throws Throwable {
		// wir kommen aus einem abgebrochenen eventActionNew
		getInternalFrame().setKeyWasForLockMe(getLieferscheinDto().getIId() + "");

		setEnabledAt(IDX_PANEL_LIEFERSCHEINPOSITIONENSICHTAUFTRAG, true);

		// die neuen Positionen einlesen
		setSelectedComponent(lsPositionenSichtAuftrag);
	}

	public String getLieferscheinStatus() throws Throwable {
		return DelegateFactory.getInstance().getLocaleDelegate().getStatusCBez(getLieferscheinDto().getStatusCNr());
	}

	public void showStatusMessage(String lpTitle, String lpMessage) throws Throwable {
		MessageFormat mf = new MessageFormat(LPMain.getTextRespectUISPr(lpMessage));

		mf.setLocale(LPMain.getTheClient().getLocUi());

		Object pattern[] = { getLieferscheinStatus() };

		DialogFactory.showModalDialog(LPMain.getTextRespectUISPr(lpTitle), mf.format(pattern));
	}

	public void setBKriterienLsUmsatzUeberMenueAufgerufen(boolean bAufgerufenI) {
		bKriterienLsUmsatzUeberMenueAufgerufen = bAufgerufenI;
	}

	public PanelSplit getLsPositionenSichtAuftrag() {
		return lsPositionenSichtAuftrag;
	}

	// TODO-AGILCHANGES
	/**
	 * AGILPRO CHANGES BEGIN
	 * 
	 * @author Lukas Lisowski
	 */

	/**
	 * @return the lsAuswahl
	 */
	public PanelQuery getPanelAuswahl() {
		this.setSelectedIndex(IDX_PANEL_LIEFERSCHEINAUSWAHL);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.lsAuswahl;
	}

	/**
	 * @return the lsKonditionen
	 */
	public PanelBasis getPanelKonditionen() {
		this.setSelectedIndex(IDX_PANEL_LIEFERSCHEINKONDITIONEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.lsKonditionen;
	}

	public PanelBasis getPanelVersandinfos() {
		this.setSelectedIndex(IDX_PANEL_VERSANDINFOS);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.lsVersandinfos;
	}

	/**
	 * @return the lsKopfdaten
	 */
	public PanelBasis getPanelKopfdaten() {
		this.setSelectedIndex(IDX_PANEL_LIEFERSCHEINKOPFDATEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.lsKopfdaten;
	}

	/**
	 * @return the lsPositionen
	 */
	public PanelSplit getPanelPositionen() {
		this.setSelectedIndex(IDX_PANEL_LIEFERSCHEINPOSITIONEN);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.lsPositionen;
	}

	/**
	 * @return the lsPositionen
	 */
	public PanelSplit getPanelSichtAuftrag() {
		this.setSelectedIndex(IDX_PANEL_LIEFERSCHEINPOSITIONENSICHTAUFTRAG);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.lsPositionenSichtAuftrag;
	}

	public Object getDto() {
		return getLieferscheinDto();
	}

	public void enablePanels(LieferscheinDto lsDtoI, boolean bDisableOnlyI) throws Throwable {
		if (lsDtoI.getIId() != null) {
			String cNrStatus = lsDtoI.getStatusCNr();
			String cLsArt = lsDtoI.getLieferscheinartCNr();

			boolean enableI = true;
			if (cLsArt.equals(LieferscheinFac.LSART_LIEFERANT) || cLsArt.equals(LieferscheinFac.LSART_FREI)) {
				enableI = false;
			}
			if (!bDarfPreiseSehen)
				enablePanelLieferscheinUmsatz(false);

			if (bDisableOnlyI) {
				// disable mode: dh. nur die false sind "nachfalsen" -> noch
				// mehr abdrehen.
				if (!enableI) {
					enablePanelLieferscheinSichtAuftrag(enableI);
				}

			} else {
				enablePanelLieferscheinSichtAuftrag(enableI);

			}
		}
	}

	private void importCSV_Shopify(Integer lagerIId) throws Throwable {
		HvOptional<CsvFile> csvFile = FileOpenerFactory.auftragShopifyImportCsv(this);
		if (csvFile.isPresent()) {

			LinkedHashMap<String, ArrayList<ImportShopifyCsvDto>> hm = new LinkedHashMap<String, ArrayList<ImportShopifyCsvDto>>();

			InputStreamReader input = new InputStreamReader(new FileInputStream(csvFile.get().getFile()),
					StandardCharsets.UTF_8.name());
			CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(input);
			int iZeile = 1;
			for (CSVRecord record : csvParser) {
				try {
					iZeile++;
					String bestellnummer = record.get("Name");

					ArrayList<ImportShopifyCsvDto> alZeilenCsv = new ArrayList<ImportShopifyCsvDto>();
					if (hm.containsKey(bestellnummer)) {
						alZeilenCsv = hm.get(bestellnummer);
					}

					ImportShopifyCsvDto zeileDto = new ImportShopifyCsvDto();

					zeileDto.shopifyBestellnummer = bestellnummer;
					zeileDto.email = record.get("Email");
					zeileDto.artikelnummer = record.get("Lineitem sku");
					zeileDto.bruttopreis = record.get("Lineitem price");

					zeileDto.rabatt = record.get("Discount Amount");

					zeileDto.gesamterbelegbetrag = record.get("Total");

					zeileDto.versandkosten = record.get("Shipping");
					zeileDto.waehrung = record.get("Currency");
					zeileDto.menge = record.get("Lineitem quantity");
					zeileDto.belegdatum = record.get("Created at");

					zeileDto.lieferart = record.get("Shipping Method");

					zeileDto.rechnungsadresseFirma = record.get("Billing Company");
					zeileDto.rechnungsadresseName = record.get("Billing Name");
					zeileDto.rechnungsadresseAdresse1 = record.get("Billing Address1");
					zeileDto.rechnungsadresseAdresse2 = record.get("Billing Address2");
					zeileDto.rechnungsadresseLand = record.get("Billing Country");
					zeileDto.rechnungsadressePLZ = record.get("Billing Zip");
					zeileDto.rechnungsadresseOrt = record.get("Billing City");
					zeileDto.rechnungsadresseStrasse = record.get("Billing Street");

					zeileDto.lieferadresseFirma = record.get("Shipping Company");
					zeileDto.lieferadresseName = record.get("Shipping Name");
					zeileDto.lieferadresseAdresse1 = record.get("Shipping Address1");
					zeileDto.lieferadresseAdresse2 = record.get("Shipping Address2");
					zeileDto.lieferadresseLand = record.get("Shipping Country");
					zeileDto.lieferadressePLZ = record.get("Shipping Zip");
					zeileDto.lieferadresseOrt = record.get("Shipping City");
					zeileDto.lieferadresseStrasse = record.get("Shipping Street");

					zeileDto.zeile = iZeile;

					alZeilenCsv.add(zeileDto);

					hm.put(bestellnummer, alZeilenCsv);

				} catch (Exception e) {

					String field_1 = record.get("amazon-order-id");

				}
			}

			DialogImportShopify dialog = new DialogImportShopify(hm, lagerIId, this);
			dialog.setVisible(true);

		}
	}

	private void importCSV_Amazon(Integer lagerIId) throws Throwable {
		HvOptional<CsvFile> csvFile = FileOpenerFactory.auftragAmazonImportCsv(this);
		if (csvFile.isPresent()) {

			LinkedHashMap<String, ArrayList<ImportAmazonCsvDto>> hm = new LinkedHashMap<String, ArrayList<ImportAmazonCsvDto>>();

			InputStreamReader input = new InputStreamReader(new FileInputStream(csvFile.get().getFile()),
					StandardCharsets.UTF_8.name());
			CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(input);
			int iZeile = 1;
			for (CSVRecord record : csvParser) {

				iZeile++;
				try {

					String bestellnummer = record.get("amazon-order-id");

					ArrayList<ImportAmazonCsvDto> alZeilenCsv = new ArrayList<ImportAmazonCsvDto>();
					if (hm.containsKey(bestellnummer)) {
						alZeilenCsv = hm.get(bestellnummer);
					}

					ImportAmazonCsvDto zeileDto = new ImportAmazonCsvDto();

					zeileDto.amazonBestellnummer = bestellnummer;
					zeileDto.email = record.get("buyer-email");
					zeileDto.artikelnummer = record.get("sku");
					zeileDto.bruttopreis = record.get("item-price");
					zeileDto.versandkosten = record.get("shipping-price");
					zeileDto.waehrung = record.get("currency");
					zeileDto.menge = record.get("quantity-shipped");
					zeileDto.belegdatum = record.get("purchase-date");

					zeileDto.recipientName = record.get("recipient-name");

					zeileDto.lieferadresseName = record.get("ship-address-1");
					zeileDto.lieferadresseAdresse1 = record.get("ship-address-2");
					zeileDto.lieferadresseAdresse2 = record.get("ship-address-3");
					zeileDto.lieferadresseLand = record.get("ship-country");
					zeileDto.lieferadressePLZ = record.get("ship-postal-code");
					zeileDto.lieferadresseOrt = record.get("ship-city");

					zeileDto.rechnungsadresseName = record.get("bill-address-1");
					zeileDto.rechnungsadresseAdresse1 = record.get("bill-address-2");
					zeileDto.rechnungsadresseAdresse2 = record.get("bill-address-3");
					zeileDto.rechnungsadresseLand = record.get("bill-country");
					zeileDto.rechnungsadressePLZ = record.get("bill-postal-code");
					zeileDto.rechnungsadresseOrt = record.get("bill-city");

					zeileDto.rabatt = record.get("item-promotion-discount");
					zeileDto.versandrabatt = record.get("ship-promotion-discount");

					zeileDto.trackingNumber = record.get("tracking-number");
					zeileDto.zeile = iZeile;

					alZeilenCsv.add(zeileDto);

					hm.put(bestellnummer, alZeilenCsv);

				} catch (Exception e) {

					String field_1 = record.get("amazon-order-id");

				}
			}

			DialogImportAmazon dialog = new DialogImportAmazon(hm, lagerIId, this);
			dialog.setVisible(true);

		}
	}

	private void enablePanelLieferscheinSichtAuftrag(boolean enableI) {
		setEnabledAt(IDX_PANEL_LIEFERSCHEINPOSITIONENSICHTAUFTRAG, enableI);
		if (bSammellieferschein) {
			setEnabledAt(IDX_PANEL_LIEFERSCHEINAUFTRAEGE, enableI);
		}
	}

	private void enablePanelLieferscheinUmsatz(boolean enableI) {
		setEnabledAt(IDX_PANEL_LIEFERSCHEINUMSATZ, enableI);
	}

	protected AuftragDto getAuftragDtoSichtAuftrag() throws Throwable {
		return auftragDtoSichtAuftrag;
	}

	protected void setAuftragDtoSichtAuftrag(AuftragDto auftragDtoSichtAuftrag) {
		this.auftragDtoSichtAuftrag = auftragDtoSichtAuftrag;
	}

	public void copyHV() throws ExceptionLP, Throwable {
		Object aoIIdPosition[] = lsPositionenTop.getSelectedIds();

		if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
			LieferscheinpositionDto[] dtos = new LieferscheinpositionDto[aoIIdPosition.length];
			Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
			for (int i = 0; i < aIIdPosition.length; i++) {
				aIIdPosition[i] = (Integer) aoIIdPosition[i];
				dtos[i] = DelegateFactory.getInstance().getLieferscheinpositionDelegate()
						.lieferscheinpositionFindByPrimaryKey((Integer) aoIIdPosition[i]);
			}

			if (lsPositionenBottom.getArtikelsetViewController().validateCopyConstraintsUI(dtos)) {
				LPMain.getPasteBuffer().writeObjectToPasteBuffer(dtos);
			}
		}
	}

	public void einfuegenHV() throws IOException, ExceptionLP, ParserConfigurationException, Throwable, SAXException {
		Object o = LPMain.getPasteBuffer().readObjectFromPasteBuffer();

		if (!lsPositionenBottom.getArtikelsetViewController().validatePasteConstraintsUI(o)) {
			return;
		}

		if (o instanceof BelegpositionDto[]) {
			LieferscheinpositionDto[] lspositionDtos = DelegateFactory.getInstance()
					.getBelegpostionkonvertierungDelegate().konvertiereNachLieferscheinpositionDto(
							(BelegpositionDto[]) o, getLieferscheinDto().getKundeIIdLieferadresse(),
							getLieferscheinDto().getTBelegdatum(), true);

			int iInserted = 0;
			if (lspositionDtos != null) {
				Integer iId = null;
				Boolean b = positionAmEndeEinfuegen();
				if (b != null) {
					for (int i = 0; i < lspositionDtos.length; i++) {
						LieferscheinpositionDto positionDto = lspositionDtos[i];
						LieferscheinpositionDto fehlerMeldungDto = null;
						try {
							positionDto.setIId(null);
							// damits hinten angehaengt wird.

							if (b == false) {
								Integer iIdAktuellePosition = (Integer) getLieferscheinPositionenTop().getSelectedId();

								// die erste Position steht an der Stelle 1
								Integer iSortAktuellePosition = new Integer(1);

								if (iIdAktuellePosition != null) {
									LieferscheinpositionDto aktuellePositionDto = DelegateFactory.getInstance()
											.getLieferscheinpositionDelegate()
											.lieferscheinpositionFindByPrimaryKey(iIdAktuellePosition);
									iSortAktuellePosition = aktuellePositionDto.getISort();

									// Die bestehenden Positionen muessen
									// Platz fuer die neue schaffen
									DelegateFactory.getInstance().getLieferscheinpositionDelegate()
											.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
													getLieferscheinDto().getIId(), iSortAktuellePosition.intValue());

									lsPositionenBottom.getArtikelsetViewController().setArtikelSetIIdFuerNeuePosition(
											aktuellePositionDto.getPositioniIdArtikelset());
								} else {
									iSortAktuellePosition = null;
								}

								// Die neue Position wird an frei gemachte
								// Position gesetzt
								positionDto.setISort(iSortAktuellePosition);

							} else {
								positionDto.setISort(null);
							}
							positionDto.setBelegIId(getLieferscheinDto().getIId());

							if (positionDto.getLieferscheinpositionartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
								// Artikel pruefen:
								Integer artikelIID = positionDto.getArtikelIId();
								ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
										.artikelFindByPrimaryKey(artikelIID);

								// Seriennummernbehaftet
								if (artikelDto != null
										&& artikelDto.getBSeriennrtragend().equals(Helper.boolean2Short(true))) {
									// Menge auf 0 setzen, und neue
									// Texteingabeposition einfuegen, dass
									// Seriennummer manuell eingetragen werden
									// muss.
									BigDecimal bdPosKopierteMenge = positionDto.getNMenge();
									positionDto.setNMenge(new BigDecimal(0));
									// Seriennr. darf nicht mitkopiert werden.
									positionDto.setSeriennrChargennrMitMenge(null);

									fehlerMeldungDto = new LieferscheinpositionDto();
									fehlerMeldungDto.setLieferscheinIId(positionDto.getLieferscheinIId());
									fehlerMeldungDto.setPositionsartCNr(LocaleFac.POSITIONSART_TEXTEINGABE);
									fehlerMeldungDto.setBNettopreisuebersteuert(Helper.getShortFalse());
									Object pattern[] = { bdPosKopierteMenge };
									String sText = LPMain.getTextRespectUISPr("ls.paste.seriennummernbehaftet");
									String sTextFormattiert = MessageFormat.format(sText, pattern);
									fehlerMeldungDto.setXTextinhalt(sTextFormattiert);
								}
								// Chargennnummernbehaftet
								else if (artikelDto != null
										&& artikelDto.getBChargennrtragend().equals(Helper.boolean2Short(true))) {
									// Menge auf 0 setzen, und neue
									// Texteingabeposition einfuegen, dass
									// Chargennummer manuell eingetragen werden
									// muss.
									SeriennrChargennrAufLagerDto[] dtos = DelegateFactory.getInstance()
											.getLagerDelegate()
											.getAllSerienChargennrAufLager(positionDto.getArtikelIId(),
													this.getLieferscheinDto().getLagerIId());
									BigDecimal bdPosKopierteMenge = positionDto.getNMenge();

									// Menge auf 0 setzen, und neue
									// Texteingabeposition einfuegen, dass
									// Chargennummer manuell eingetragen
									// werden
									// muss.
									// ghp, 12.06.2015
									// BigDecimal bdPosKopierteMenge =
									// positionDto
									// .getNMenge();
									positionDto.setNMenge(new BigDecimal(0));
									fehlerMeldungDto = new LieferscheinpositionDto();
									fehlerMeldungDto.setLieferscheinIId(positionDto.getLieferscheinIId());
									fehlerMeldungDto.setPositionsartCNr(LocaleFac.POSITIONSART_TEXTEINGABE);
									fehlerMeldungDto.setBNettopreisuebersteuert(Helper.getShortFalse());

									Object pattern[] = { bdPosKopierteMenge };
									String sText = LPMain.getTextRespectUISPr("ls.paste.chargennummernbehaftet");
									String sTextFormattiert = MessageFormat.format(sText, pattern);
									fehlerMeldungDto.setXTextinhalt(sTextFormattiert);
									// }
								}
								// lagerbewirtschaftet: Lagerstand ausreichend
								else if (artikelDto != null
										&& artikelDto.getBLagerbewirtschaftet().equals(Helper.boolean2Short(true))) {
									// lagerstand ueberpruefen, damit diese
									// Position
									// nur angelegt wird, wenn genuegend
									// Menge in Lager vorhanden ist
									BigDecimal lagerstand = null;

									// PJ18290

									ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
											.getParameterDelegate().getParametermandant(
													ParameterFac.PARAMETER_LAGER_IMMER_AUSREICHEND_VERFUEGBAR,
													ParameterFac.KATEGORIE_ARTIKEL, LPMain.getTheClient().getMandant());

									int bImmerAusreichendVerfuegbar = (Integer) parameter.getCWertAsObject();

									if (bImmerAusreichendVerfuegbar == 0) {
										lagerstand = DelegateFactory.getInstance().getLagerDelegate()
												.getLagerstandAllerLagerEinesMandanten(artikelDto.getIId());

									} else {
										lagerstand = new BigDecimal(9999999);
									}

									BigDecimal bdLagerstandDiff = lagerstand.subtract(positionDto.getNMenge());
									if (bdLagerstandDiff.compareTo(new BigDecimal(0)) < 0) {
										// nur soviel abbuchen wie moeglich,
										// fuer
										// den Rest eine extra
										// Texteingabeposition
										// mit der Fehlermeldung
										// anlegen, dass nicht die ganze Menge
										// gebucht werden konnte.
										BigDecimal bdPosKopierteMenge = positionDto.getNMenge();
										positionDto.setNMenge(lagerstand);

										fehlerMeldungDto = new LieferscheinpositionDto();
										fehlerMeldungDto.setLieferscheinIId(positionDto.getLieferscheinIId());
										fehlerMeldungDto.setPositionsartCNr(LocaleFac.POSITIONSART_TEXTEINGABE);
										fehlerMeldungDto.setBNettopreisuebersteuert(Helper.getShortFalse());
										Object pattern[] = { positionDto.getNMenge(), bdPosKopierteMenge };
										String sText = LPMain.getTextRespectUISPr("ls.paste.lagerstandfuerartikelleer");
										String sTextFormattiert = MessageFormat.format(sText, pattern);
										fehlerMeldungDto.setXTextinhalt(sTextFormattiert);
									}

								}

							}

							// wir legen eine neue position an

							if (iId == null) {
								ArtikelsetViewController viewController = lsPositionenBottom
										.getArtikelsetViewController();
								boolean bDiePositionSpeichern = viewController
										.validateArtikelsetConstraints(positionDto);

								if (bDiePositionSpeichern) {
									positionDto.setPositioniIdArtikelset(
											viewController.getArtikelSetIIdFuerNeuePosition());

									List<SeriennrChargennrMitMengeDto> snrs = viewController
											.handleArtikelsetSeriennummern(getLieferscheinDto().getLagerIId(),
													positionDto);
									if (!viewController.isArtikelsetWithSnrsStoreable(positionDto, snrs)) {
										bDiePositionSpeichern = false;
									}

									if (bDiePositionSpeichern) {
										iId = DelegateFactory.getInstance().getLieferscheinpositionDelegate()
												.createLieferscheinposition(positionDto, snrs, true);
										lspositionDtos[i] = DelegateFactory.getInstance()
												.getLieferscheinpositionDelegate()
												.lieferscheinpositionFindByPrimaryKey(iId);
									}
								}
							} else {
								Integer newId = DelegateFactory.getInstance().getLieferscheinpositionDelegate()
										.createLieferscheinposition(positionDto, true);
								lspositionDtos[i] = DelegateFactory.getInstance().getLieferscheinpositionDelegate()
										.lieferscheinpositionFindByPrimaryKey(newId);
							}

							iInserted++;

							// Fehlermeldung als eigene Position anlegen.
							if (fehlerMeldungDto != null) {
								DelegateFactory.getInstance().getLieferscheinpositionDelegate()
										.createLieferscheinposition(fehlerMeldungDto, false);
							}
						} catch (ExceptionLP t) {
							// nur loggen!
							myLogger.error(t.getMessage(), t);
						}
					}
				}

				ZwsEinfuegenHVLieferscheinposition cpp = new ZwsEinfuegenHVLieferscheinposition();
				cpp.processZwsPositions(lspositionDtos, (BelegpositionDto[]) o);

				// die Liste neu aufbauen
				lsPositionenTop.eventYouAreSelected(false);
				// den Datensatz in der Liste selektieren
				this.lsPositionenTop.setSelectedId(iId);

				// im Detail den selektierten anzeigen
				lsPositionen.eventYouAreSelected(false);
			}
		}

	}

	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfBelegPosI) throws Throwable {
		LieferscheinpositionDto lieferscheinpositionDtoI = (LieferscheinpositionDto) belegposDtoI;
		lieferscheinpositionDtoI.setBelegIId(lieferscheinDto.getIId());
		lieferscheinpositionDtoI.setISort(xalOfBelegPosI + 1000);
		if (lieferscheinpositionDtoI.getBArtikelbezeichnunguebersteuert() == null) {
			lieferscheinpositionDtoI.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
		}
	}

	private boolean hasZusatzfunktion4Vending() {
		if (bZusatzfunktion4Vending == null) {
			bZusatzfunktion4Vending = LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_4VENDING_SCHNITTSTELLE);
		}
		return bZusatzfunktion4Vending;
	}

	private void actionImportEasydataStockMovementsXML() {
		XmlFile xmlFile = FileChooserBuilder
				.createOpenDialog(FileChooserConfigToken.ImportXmlEasydataStockMovements, this).addXmlFilter()
				.openSingle();
		if (!xmlFile.hasFile())
			return;

		EasydataStockMovementImportCtrl stmCtrl = new EasydataStockMovementImportCtrl(xmlFile);
		DialogEasydataStockMovementResult stmDialog = new DialogEasydataStockMovementResult(
				LPMain.getInstance().getDesktop(), stmCtrl);
		stmDialog.setVisible(true);
	}
}
