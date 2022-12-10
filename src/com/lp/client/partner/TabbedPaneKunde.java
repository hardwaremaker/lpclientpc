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
package com.lp.client.partner;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.artikel.IVendidataExportController;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.ICopyPaste;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedEventDrop;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMapButton;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.dynamisch.PanelDynamisch;
import com.lp.client.media.DropPanelSplit;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.DialogStuecklisteStruktur;
import com.lp.client.system.ReportEntitylog;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.media.service.MediaEmailMetaDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.AnsprechpartnerISortValues;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.KundesokomengenstaffelDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerReportFac;
import com.lp.server.partner.service.VendidataPartnerExportResult;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;

/*
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004, 2005, 2006</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2013/02/01 08:56:38 $
 */
public class TabbedPaneKunde extends TabbedPane implements ICopyPaste, IPartnerDtoService {

	private static final long serialVersionUID = 1L;

	public static int IDX_PANE_KUNDE = -1;
	public static int IDX_PANE_KOPFDATEN = -1;
	public static int IDX_PANE_KONDITIONEN = -1;
	public static int IDX_PANE_SACHBEARBEITER = -1;
	public static int IDX_PANE_ANSPRECHPARTNER = -1;
	public static int IDX_PANE_BANKVERBINDUNG = -1;
	public static int IDX_PANE_RECHNUNGSADRESSE = -1;
	// private static final int IDX_PANE_LIEFERSTATISTIK_QP7 = 6;
	public static int IDX_PANE_UMSATZSTATISTIK = -1;
	public static int IDX_PANE_KURZBRIEF = -1;
	public static int IDX_PANE_KUNDESOKO = -1;
	public static int IDX_PANE_KUNDESOKOMENGENSTAFFEL = -1;
	public static int IDX_PANEL_KUNDENEIGENSCHAFTEN = -1;
	public static int IDX_PANEL_KONTAKT = -1;
	public static int IDX_PANEL_SELEKTION = -1;
	public static int IDX_PANEL_KOMMENTAR = -1;
	public static int IDX_PANEL_MATERIAL = -1;
	public static int IDX_PANEL_SPEDITEUR = -1;
	public static int IDX_PANEL_KUNDEKENNUNG = -1;
	public static int IDX_PANEL_KENNUNG = -1;
	public static int IDX_PANEL_PARTNEREIGENSCHAFTEN = -1;

	private String rechtModulweit = null;

	private KundeDto kundeDto = new KundeDto();

	private PanelQuery panelKundeQP1 = null;
	private PanelBasis panelKundekopfdatenD2 = null;
	private PanelBasis panelKonditionenD3 = null;
	private PanelQuery panelSachbearbeiterTopQP4 = null;
	private PanelBasis panelSachbearbeiterBottomD4 = null;
	private PanelBasis panelSachbearbeiterSP4 = null;
	private PanelBasis panelAnsprechpartnerSP5 = null;
	private PanelQuery panelAnsprechpartnerTopQP5 = null;
	// private PanelBasis panelAnsprechpartnerBottomD5 = null;
	private PanelKundeAnsprechpartner panelAnsprechpartnerBottomD5 = null;
	private PanelBasis panelREadresseBottomD6 = null;
	private PanelQuery panelLieferstatistikQP6 = null;
	private PanelQueryFLR panelPartner = null;

	private PanelQueryFLRProvisionsempaengerErsetzen panelQueryFLRProvisionsempaengerErsetzen = null;

	private PanelTabelleUmsatz panelUebersicht = null;
	private PanelDialogKriterienUmsatzKD panelKriterienUebersicht = null;

	private PanelQuery panelKundesokoQP10 = null;
	private PanelKundesoko panelKundesokoD10 = null;
	private PanelSplit panelKundesokoSP10 = null; // FLR 1:n Liste

	private PanelQuery panelKundesokomengenstaffelQP11 = null;
	private PanelKundesokomengenstaffel panelKundesokomengenstaffelD11 = null;
	private PanelSplit panelKundesokomengenstaffelSP11 = null; // FLR 1:n Liste

	private PanelQuery panelQueryKurzbrief = null;
	private PanelPartnerKurzbrief panelDetailKurzbrief = null;
	private PanelSplit panelSplitKurzbrief = null;

	private PanelQuery panelBankTopQP = null;
	private PanelBasis panelBankBottomD = null;
	private PanelSplit panelBankSP = null;

	private PanelBasis panelDetailpartnereigenschaft = null;
	private PanelBasis panelDetailKundeneigenschaft = null;

	protected PanelQuery panelQueryKontakt = null;
	private PanelBasis panelDetailKontakt = null;
	private PanelSplit panelSplitKontakt = null;

	private PanelQuery panelSelektionTopQP = null;
	private PanelPASelektionKunde panelSelektionBottomD = null;
	private PanelSplit panelSelektionSP = null;

	private PanelQuery panelMaterialTopQP = null;
	private PanelKundematerial panelMaterialBottomD = null;
	private PanelSplit panelMaterialSP = null;

	private PanelQuery panelSpediteurTopQP = null;
	private PanelKundespediteur panelSpediteurBottomD = null;
	private PanelSplit panelSpediteurSP = null;

	private PanelQuery panelKennungTopQP = null;
	private PanelKundeKennung panelKennungBottomD = null;
	private PanelSplit panelKennungSP = null;

	private PanelQuery panelKommentarTopQP = null;
	private PanelKundeLieferantKommentar panelKommentarBottomD = null;
	private PanelSplit panelKommentarSP = null;

	private WrapperMapButton mapButton = null;

	// das sind die extra Neu Buttons auf den QueryPanels dieses TabbedPane
	private final static String EXTRA_NEU_AUS_PARTNER = "neu_aus_partner";

	private final String MENUE_ACTION_ADRESSETIKETT = "MENUE_ACTION_ADRESSETIKETT";
	private final String MENUE_ACTION_LIEFERSTATISTIK = "MENUE_ACTION_LIEFERSTATISTIK";
	private final String MENUE_ACTION_STAMMBLATT = "MENUE_ACTION_STAMMBLATT";
	private final String MENUE_ACTION_PREISLISTE = "MENUE_ACTION_PREISLISTE";
	private final String MENUE_ACTION_LIEFERMENGEN = "MENUE_ACTION_LIEFERMENGEN";

	private final String MENUE_ACTION_UMSATZSTATISTIK = "MENUE_ACTION_UMSATZSTATISTIK";
	private final String MENUE_ACTION_KUNDENLISTE = "MENUE_ACTION_KUNDENLISTE";

	private final String MENU_INFO_AENDERUNGEN = "MENU_INFO_AENDERUNGEN";

	private final String MENUE_ACTION_KUNDENZUSAMMENFUEHREN = "MENUE_ACTION_KUNDENZUSAMMENFUEHREN";
	private final String MENUE_ACTION_EXPORT_4VENDING = "MENUE_ACTION_EXPORT_4VENDING";

	private static final String MENUE_ACTION_PFLEGE_PROFIRST_IMPORT = "MENUE_ACTION_PFLEGE_PROFIRST_IMPORT";
	private static final String MENUE_ACTION_PFLEGE_PROFIRST_IMPORT_VKPREIS = "MENUE_ACTION_PFLEGE_PROFIRST_IMPORT_VKPREIS";
	private static final String MENUE_ACTION_PFLEGE_PROVISIONSEMPFAENGER_ERSETZEN = "MENUE_ACTION_PFLEGE_PROVISIONSEMPFAENGER_ERSETZEN";
	private static final String MENUE_ACTION_PFLEGE_SOKO_IMPORT = "MENUE_ACTION_PFLEGE_SOKO_IMPORT";

	private final String BUTTON_IMPORT_KUNDESOKO = PanelBasis.ACTION_MY_OWN_NEW + "ACTION_SPECIAL_IMPORT_KUNDESOKO";

	private final String ACTION_SPECIAL_BAUMSICHT = "ACTION_SPECIAL_BAUMSICHT";
	private final String MENU_KUNDE_BAUMSICHT = PanelBasis.ACTION_MY_OWN_NEW + ACTION_SPECIAL_BAUMSICHT;

	private WrapperMenuBar menu = null;
	private Boolean bZusatzfunktion4Vending = null;
	private IVendidataExportController<VendidataPartnerExportResult> kundeexport4vendingController;

	private static final String ACTION_SPECIAL_NEW_EMAIL = PanelBasis.ACTION_MY_OWN_NEW + "new_email_entry";

	public TabbedPaneKunde(InternalFrame internalFrameI) throws Throwable {

		super(internalFrameI, LPMain.getTextRespectUISPr("label.kunde"));
		rechtModulweit = getInternalFrame().getRechtModulweit();
		jbInit();
		initComponents();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {

		// tab oben: QP KundeFLR; lazy loading
		// hier kommt alles drauf
		IDX_PANE_KUNDE =

				reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.auswahl"), null, null,
						LPMain.getTextRespectUISPr("lp.auswahl"));

		// tab oben; Kopfdaten ; D2; lazy loading

		IDX_PANE_KOPFDATEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.kopfdaten"), null, null,
				LPMain.getTextRespectUISPr("lp.kopfdaten"));

		// tab oben; Kundenkonditionen; lazy loading

		IDX_PANE_KONDITIONEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("auft.title.panel.konditionen"), null, null,
				LPMain.getTextRespectUISPr("auft.title.panel.konditionen"));

		// tab oben; Splitpane Sachbearbeiter

		IDX_PANE_SACHBEARBEITER = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.sachbearbeiter"), null, null,
				LPMain.getTextRespectUISPr("lp.sachbearbeiter"));

		// tab oben; Splitpane Ansprechpartner

		IDX_PANE_ANSPRECHPARTNER = reiterHinzufuegen(LPMain.getTextRespectUISPr("label.ansprechpartner"), null, null,
				LPMain.getTextRespectUISPr("label.ansprechpartner"));

		// tab oben; Splitpane Bankverbindung

		IDX_PANE_BANKVERBINDUNG = reiterHinzufuegen(LPMain.getTextRespectUISPr("part.kund.bankverbindung"), null, null,
				LPMain.getTextRespectUISPr("part.kund.bankverbindung"));

		// tab oben; Rechnungsadresse;lazy loading

		IDX_PANE_RECHNUNGSADRESSE = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.rechnungsadresse"), null, null,
				LPMain.getTextRespectUISPr("lp.rechnungsadresse"));

		/*
		 * //tab oben; Statistik reiterHinzufuegen(
		 * LPMain.getInstance().getTextRespectUISPr ("part.kunde.lieferstatistik"),
		 * null, null, LPMain.getInstance().getTextRespectUISPr
		 * ("part.kunde.lieferstatistik"), IDX_PANE_LIEFERSTATISTIK_QP7);
		 */

		boolean bDarfUmsaetzeSehen = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PART_KUNDE_UMSAETZE_R);
		if (bDarfUmsaetzeSehen == true) {
			// tab oben; Umsatzuebersicht

			IDX_PANE_UMSATZSTATISTIK = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.umsatzuebersicht"), null, null,
					LPMain.getTextRespectUISPr("lp.umsatzuebersicht"));
		}

		IDX_PANE_KURZBRIEF = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.kurzbrief"), null, null,
				LPMain.getTextRespectUISPr("lp.kurzbrief"));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_KUNDESONDERKONDITIONEN)) {
			if (getInternalFrame().bRechtDarfPreiseSehenVerkauf) {

				IDX_PANE_KUNDESOKO = reiterHinzufuegen(LPMain.getTextRespectUISPr("part.kundesoko"), null, null,
						LPMain.getTextRespectUISPr("part.kundesoko"));

				// PJ19363
				ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate()
						.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
								ParameterFac.PARAMETER_VK_MENGENSTAFFEL_ANSTATT_SOKO_MENGESTAFFEL);
				boolean b = (java.lang.Boolean) parameter.getCWertAsObject();

				if (b == false) {

					IDX_PANE_KUNDESOKOMENGENSTAFFEL =

							reiterHinzufuegen(LPMain.getTextRespectUISPr("part.kundesokomengenstaffel"), null, null,
									LPMain.getTextRespectUISPr("part.kundesokomengenstaffel"));
				}
			}
		}

		// Wenn keine Panelbeschriebung vorhanden, dann ausblenden
		if (DelegateFactory.getInstance().getPanelDelegate()
				.panelbeschreibungVorhanden(PanelFac.PANEL_KUNDENEIGENSCHAFTEN)) {

			IDX_PANEL_KUNDENEIGENSCHAFTEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.eigenschaften"), null,
					null, LPMain.getTextRespectUISPr("lp.eigenschaften"));
		}

		if (DelegateFactory.getInstance().getPanelDelegate()
				.panelbeschreibungVorhanden(PanelFac.PANEL_PARTNEREIGENSCHAFTEN)) {
			IDX_PANEL_PARTNEREIGENSCHAFTEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.eigenschaften.partner"),
					null, null, LPMain.getTextRespectUISPr("lp.eigenschaften.partner"));
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_KONTAKTMANAGMENT)) {

			IDX_PANEL_KONTAKT = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.kontakt"), null, null,
					LPMain.getTextRespectUISPr("lp.kontakt"));

		}

		IDX_PANEL_SELEKTION = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.selektion"), null, null,
				LPMain.getTextRespectUISPr("lp.selektion"));
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_METALLNOTIERUNG_DETAILLIERT)) {

			IDX_PANEL_MATERIAL = reiterHinzufuegen(LPMain.getTextRespectUISPr("part.materialnotierung"), null, null,
					LPMain.getTextRespectUISPr("part.materialnotierung"));
		}

		IDX_PANEL_KOMMENTAR = reiterHinzufuegen(LPMain.getTextRespectUISPr("part.kommentar"), null, null,
				LPMain.getTextRespectUISPr("part.kommentar"));

		IDX_PANEL_SPEDITEUR = reiterHinzufuegen(LPMain.getTextRespectUISPr("kunde.kundespediteur"), null, null,
				LPMain.getTextRespectUISPr("kunde.kundespediteur"));

		IDX_PANEL_KENNUNG = reiterHinzufuegen(LPMain.getTextRespectUISPr("kunde.kundekennung"), null, null,
				LPMain.getTextRespectUISPr("kunde.kundekennung"));

		// QP1 ist default.
		setSelectedComponent(panelKundeQP1);
		this.refreshKundeQP1();

		if (panelKundeQP1.getSelectedId() == null) {
			// leere tabelle: restlichen panels deaktivieren.
			getInternalFrame().enableAllPanelsExcept(false);
		}
		getInternalFrameKunde().getKundeDto().setIId((Integer) panelKundeQP1.getSelectedId());

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelKundeQP1, ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getTextRespectUISPr("label.kunde"));
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (menu == null) {
			menu = new WrapperMenuBar(this);
			JMenu menuDatei = (JMenu) menu.getComponent(WrapperMenuBar.MENU_MODUL);
			menuDatei.add(new JSeparator(), 0);
			// Hier Menuebar hinzufuegen
			JMenu menuInfo = new WrapperMenu("lp.info", this);
			menu.addJMenuItem(menuInfo);
			JMenuItem menuItemadressetikett = new JMenuItem(LPMain.getTextRespectUISPr("part.report.adressetikett"));
			menuItemadressetikett.addActionListener(this);
			menuItemadressetikett.setActionCommand(MENUE_ACTION_ADRESSETIKETT);
			menuInfo.add(menuItemadressetikett);

			JMenuItem menuItemlieferstatistik = new JMenuItem(LPMain.getTextRespectUISPr("part.kunde.lieferstatistik"));
			menuItemlieferstatistik.addActionListener(this);
			menuItemlieferstatistik.setActionCommand(MENUE_ACTION_LIEFERSTATISTIK);
			menuInfo.add(menuItemlieferstatistik);
			boolean bDarfUmsaetzeSehen = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PART_KUNDE_UMSAETZE_R);
			if (bDarfUmsaetzeSehen == true) {
				// Kundenstammblatt
				JMenuItem menuItemStammblatt = new JMenuItem(LPMain.getTextRespectUISPr("lp.stammblatt"));
				menuItemStammblatt.addActionListener(this);
				menuItemStammblatt.setActionCommand(MENUE_ACTION_STAMMBLATT);
				menuInfo.add(menuItemStammblatt);
			}
			if (getInternalFrame().bRechtDarfPreiseSehenVerkauf) {
				JMenuItem menuItemPreisliste = new JMenuItem(
						LPMain.getTextRespectUISPr("kunde.report.kundenpreisliste"));
				menuItemPreisliste.addActionListener(this);
				menuItemPreisliste.setActionCommand(MENUE_ACTION_PREISLISTE);
				menuInfo.add(menuItemPreisliste);
			}

			if (DelegateFactory.getInstance().getMandantDelegate()
					.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FORECAST)) {
				JMenuItem menuItemLiefermengen = new JMenuItem(LPMain.getTextRespectUISPr("kunde.report.liefermengen"));
				menuItemLiefermengen.addActionListener(this);
				menuItemLiefermengen.setActionCommand(MENUE_ACTION_LIEFERMENGEN);
				menuInfo.add(menuItemLiefermengen);
			}

			JMenuItem menuItemAenderungen = new JMenuItem(LPMain.getTextRespectUISPr("lp.report.aenderungen"));
			menuItemAenderungen.addActionListener(this);
			menuItemAenderungen.setActionCommand(MENU_INFO_AENDERUNGEN);
			menuInfo.add(menuItemAenderungen);

			WrapperMenuItem menuItemUmsatzstatistik = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("part.kund.umsatzstatistik"), RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
			menuItemUmsatzstatistik.addActionListener(this);
			menuItemUmsatzstatistik.setActionCommand(MENUE_ACTION_UMSATZSTATISTIK);
			JMenu journal = (JMenu) menu.getComponent(WrapperMenuBar.MENU_JOURNAL);

			if (bDarfUmsaetzeSehen == true) {
				journal.add(menuItemUmsatzstatistik);
			}

			WrapperMenuItem menuItemKundenliste = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("part.kunde.kundenliste"), RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);
			menuItemKundenliste.addActionListener(this);
			menuItemKundenliste.setActionCommand(MENUE_ACTION_KUNDENLISTE);
			journal.add(menuItemKundenliste);

			if (hatZusatzfunkton4Vending()) {
				WrapperMenuItem menuItemExport4Vending = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("kunde.export.menu.4vendingkunden"), null);
				menuItemExport4Vending.addActionListener(this);
				menuItemExport4Vending.setActionCommand(MENUE_ACTION_EXPORT_4VENDING);
				menuDatei.add(menuItemExport4Vending, 0);
			}

			/* Kunden zusammenfuehren */
			boolean bDarfKundenZusammenfuehren = false;
			try {
				bDarfKundenZusammenfuehren = DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PART_KUNDE_ZUSAMMENFUEHREN_ERLAUBT);
			} catch (Throwable ex) {
				handleException(ex, true);
			}
			if (bDarfKundenZusammenfuehren) {
				JMenuItem menuItemKundenZusammenfuehren = new JMenuItem(
						LPMain.getTextRespectUISPr("part.kundenzusammenfuehren"));
				menuItemKundenZusammenfuehren.addActionListener(this);
				menuItemKundenZusammenfuehren.setActionCommand(MENUE_ACTION_KUNDENZUSAMMENFUEHREN);
				HelperClient.setToolTipTextMitRechtToComponent(menuItemKundenZusammenfuehren,
						RechteFac.RECHT_PART_KUNDE_ZUSAMMENFUEHREN_ERLAUBT);
				menuDatei.add(menuItemKundenZusammenfuehren, 0);
			}

			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_PRO_FIRST_DBURL);
			String dburl = parameter.getCWert();

			JMenu menuPflege = new WrapperMenu("lp.pflege", this);

			if (dburl != null && dburl.trim().length() > 0) {

				JMenuItem menuItemImportProFirst = new JMenuItem(LPMain.getTextRespectUISPr("stk.profistimport"));
				menuItemImportProFirst.addActionListener(this);
				menuItemImportProFirst.setActionCommand(MENUE_ACTION_PFLEGE_PROFIRST_IMPORT);
				menuPflege.add(menuItemImportProFirst);

				JMenuItem menuItemImportProFirstVKPReis = new JMenuItem(
						LPMain.getTextRespectUISPr("stk.profistimport.nurkvpreise"));
				menuItemImportProFirstVKPReis.addActionListener(this);
				menuItemImportProFirstVKPReis.setActionCommand(MENUE_ACTION_PFLEGE_PROFIRST_IMPORT_VKPREIS);
				menuPflege.add(menuItemImportProFirstVKPReis);

			}

			if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_PART_KUNDE_CUD)) {
				JMenuItem menuItemProvisionsempfaengerErsetzen = new JMenuItem(
						LPMain.getTextRespectUISPr("kunde.pflege.provisionsempfaenger"));
				menuItemProvisionsempfaengerErsetzen.addActionListener(this);
				menuItemProvisionsempfaengerErsetzen
						.setActionCommand(MENUE_ACTION_PFLEGE_PROVISIONSEMPFAENGER_ERSETZEN);
				menuPflege.add(menuItemProvisionsempfaengerErsetzen);

				JMenuItem menuItemSokoImport = new JMenuItem(LPMain.getTextRespectUISPr("kunde.pflege.sokoimport"));
				menuItemSokoImport.addActionListener(this);
				menuItemSokoImport.setActionCommand(MENUE_ACTION_PFLEGE_SOKO_IMPORT);
				menuPflege.add(menuItemSokoImport);

			}

			if (menuPflege.getItemCount() > 0) {
				menu.addJMenuItem(menuPflege);
			}

		}
		return menu;
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {

		super.lPEventObjectChanged(e);
		getInternalFrame().setRechtModulweit(rechtModulweit);
		int selectedCur = this.getSelectedIndex();
		if (selectedCur == IDX_PANE_KUNDE) {
			// gehe zu QP1.
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					LPMain.getTextRespectUISPr("lp.auswahl"));
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
			refreshKundeQP1();

			panelKundeQP1.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelKundeQP1.updateButtons();
		}

		else if (selectedCur == IDX_PANE_KOPFDATEN) {
			// gehe zu D2.
			Integer key = (Integer) getInternalFrameKunde().getKundeDto().getIId();
			try {
				getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
						LPMain.getTextRespectUISPr("anf.panel.kopfdaten"));
				refreshKundePartnerD2(key);
				// Info an Panel: bist selektiert worden.
				panelKundekopfdatenD2.eventYouAreSelected(false);
			} catch (ExceptionLP efc) {
				if (efc != null && efc.getICode() == EJBExceptionLP.FEHLER_DLG_DONE_DO_NOTHING) {
					getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, "");
					panelKundeQP1.eventYouAreSelected(false);
					setEnabledAt(IDX_PANE_KUNDE, true);
					setSelectedComponent(panelKundeQP1);
				}
			}
		}

		else if (selectedCur == IDX_PANE_KONDITIONEN) {
			// gehe zu D3.
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					LPMain.getTextRespectUISPr("anf.panel.konditionen"));
			Integer key = (Integer) getInternalFrameKunde().getKundeDto().getIId();
			refreshKonditionenD3(key);
			// Info an Panel: bist selektiert worden.
			panelKonditionenD3.eventYouAreSelected(false);
		}

		else if (selectedCur == IDX_PANE_RECHNUNGSADRESSE) {
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					LPMain.getTextRespectUISPr("lp.rechnungsadresse"));
			Integer key = (Integer) getInternalFrameKunde().getKundeDto().getIId();
			refreshRechnungsadresseDX(key);
			panelREadresseBottomD6.eventYouAreSelected(false);
		}

		else if (selectedCur == IDX_PANE_SACHBEARBEITER) {
			// gehe zu SP4
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					LPMain.getTextRespectUISPr("lp.sachbearbeiter"));
			Integer iIdKunde = (Integer) getInternalFrameKunde().getKundeDto().getIId();
			refreshSachbearbeiterSP4(iIdKunde);
			panelSachbearbeiterSP4.eventYouAreSelected(false);
			panelSachbearbeiterTopQP4.updateButtons(panelSachbearbeiterBottomD4.getLockedstateDetailMainKey());
		}

		else if (selectedCur == IDX_PANE_ANSPRECHPARTNER) {

			// PJ21637
			boolean hatRechtAnsprtechpartnerCUD = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PART_ANSPRECHPARTNER_CUD);
			if (hatRechtAnsprtechpartnerCUD == true) {
				getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_UPDATE);
			}

			// gehe zu SP5
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					LPMain.getTextRespectUISPr("label.ansprechpartner"));
			Integer iIdPartner = getInternalFrameKunde().getKundeDto().getPartnerIId();
			refreshAnsprechpartnerSP5(iIdPartner);
			panelAnsprechpartnerSP5.eventYouAreSelected(false);
			panelAnsprechpartnerTopQP5.updateButtons(panelAnsprechpartnerBottomD5.getLockedstateDetailMainKey());
		} else if (selectedCur == IDX_PANE_BANKVERBINDUNG) {
			// gehe zu SP5
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					LPMain.getTextRespectUISPr("finanz.bankverbindung"));
			Integer iIdPartner = getInternalFrameKunde().getKundeDto().getPartnerIId();
			refreshBankverbindung(iIdPartner);
			panelBankSP.eventYouAreSelected(false);
			panelBankTopQP.updateButtons(panelBankBottomD.getLockedstateDetailMainKey());
		}

		/*
		 * else if (selectedCur == IDX_PANE_LIEFERSTATISTIK_QP7) { //gehe zu QP6
		 * getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, LPMain
		 * .getInstance().getTextRespectUISPr("part.kunde.lieferstatistik"));
		 * getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
		 * getInternalFrameKunde
		 * ().getKundeDto().getPartnerDto().formatFixTitelName1Name2());
		 * refreshLieferstatistikQP6();
		 * panelLieferstatistikQP6.eventYouAreSelected(false); }
		 */

		else if (selectedCur == IDX_PANE_UMSATZSTATISTIK) {
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					LPMain.getTextRespectUISPr("lp.umsatzuebersicht"));
			refreshUmsatzPT7();
			// bevor man an die Uebersicht kommt, muss man die Kriterien waehlen
			getInternalFrame().showPanelDialog(getPanelKriterienUmsatz());
		}

		else if (selectedCur == IDX_PANEL_SELEKTION) {
			// gehe zu SP6
			Integer iIdPartner = getKundeDto().getPartnerIId();
			refreshSelektionSP6(iIdPartner);
			panelSelektionSP.eventYouAreSelected(false);
			// im QP die Buttons setzen.
			LockStateValue d = panelSelektionBottomD.getLockedstateDetailMainKey();
			panelSelektionTopQP.updateButtons(d);
		} else if (selectedCur == IDX_PANEL_MATERIAL) {
			// gehe zu SP6
			refreshMaterialSP(getKundeDto().getIId());
			panelMaterialSP.eventYouAreSelected(false);
			// im QP die Buttons setzen.
			LockStateValue d = panelMaterialBottomD.getLockedstateDetailMainKey();
			panelMaterialTopQP.updateButtons(d);
		} else if (selectedCur == IDX_PANEL_SPEDITEUR) {
			// gehe zu SP6
			refreshSpediteurSP(getKundeDto().getIId());
			panelSpediteurSP.eventYouAreSelected(false);
			// im QP die Buttons setzen.
			LockStateValue d = panelSpediteurBottomD.getLockedstateDetailMainKey();
			panelSpediteurTopQP.updateButtons(d);
		} else if (selectedCur == IDX_PANEL_SPEDITEUR) {
			// gehe zu SP6
			refreshSpediteurSP(getKundeDto().getIId());
			panelSpediteurSP.eventYouAreSelected(false);
			// im QP die Buttons setzen.
			LockStateValue d = panelSpediteurBottomD.getLockedstateDetailMainKey();
			panelSpediteurTopQP.updateButtons(d);
		} else if (selectedCur == IDX_PANEL_KENNUNG) {
			// gehe zu SP6
			refreshKennungSP(getKundeDto().getIId());
			panelKennungSP.eventYouAreSelected(false);
			// im QP die Buttons setzen.
			LockStateValue d = panelKennungBottomD.getLockedstateDetailMainKey();
			panelKennungTopQP.updateButtons(d);
		} else if (selectedCur == IDX_PANEL_KOMMENTAR) {
			// gehe zu SP6
			Integer iIdPartner = getKundeDto().getPartnerIId();
			refreshKommentar(iIdPartner);
			panelKommentarSP.eventYouAreSelected(false);
			// im QP die Buttons setzen.
			LockStateValue d = panelKommentarBottomD.getLockedstateDetailMainKey();
			panelKommentarTopQP.updateButtons(d);
		}

		else if (selectedCur == IDX_PANE_KUNDESOKO) {
			// gehe zu SP9
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					LPMain.getTextRespectUISPr("part.kundesoko"));
			refreshKundesokoSP10(getInternalFrameKunde().getKundeDto().getIId());
			panelKundesokoSP10.eventYouAreSelected(false);
			// im QP die Buttons setzen.
			LockStateValue d = panelKundesokoD10.getLockedstateDetailMainKey();
			panelKundesokoQP10.updateButtons(d);
		}

		else if (selectedCur == IDX_PANE_KURZBRIEF) {
			boolean hatRechtKurzbriefCUD = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PART_KURZBRIEF_CUD);
			if (hatRechtKurzbriefCUD == true) {
				getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_UPDATE);
			}
			// gehe zu SP9
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					LPMain.getTextRespectUISPr("lp.kurzbrief"));
			refreshKurzbrief(getInternalFrameKunde().getKundeDto().getPartnerIId());
			panelSplitKurzbrief.eventYouAreSelected(false);
			// im QP die Buttons setzen.
			LockStateValue d = panelDetailKurzbrief.getLockedstateDetailMainKey();
			panelQueryKurzbrief.updateButtons(d);

		} else if (selectedCur == IDX_PANEL_KONTAKT) {
			// gehe zu SP9
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					LPMain.getTextRespectUISPr("lp.kontakt"));
			refreshKontakt(getInternalFrameKunde().getKundeDto().getPartnerIId());
			panelSplitKontakt.eventYouAreSelected(false);
			// im QP die Buttons setzen.
			LockStateValue d = panelDetailKontakt.getLockedstateDetailMainKey();
			panelQueryKontakt.updateButtons(d);

		} else if (selectedCur == IDX_PANEL_KUNDENEIGENSCHAFTEN) {
			refreshEigenschaften(getInternalFrameKunde().getKundeDto().getIId());
			panelDetailKundeneigenschaft.eventYouAreSelected(false);
		} else if (selectedCur == IDX_PANEL_PARTNEREIGENSCHAFTEN) {
			refreshPartnerEigenschaften(getInternalFrameKunde().getKundeDto().getPartnerIId());
			panelDetailpartnereigenschaft.eventYouAreSelected(false);
		} else if (selectedCur == IDX_PANE_KUNDESOKOMENGENSTAFFEL) {
			// die anzeigte Liste in diesem Panel ist abhaengig von der momentan
			// selektierten Zeile im Panel IDX_PANE_KUNDESOKO_SP10
			refreshKundesokoSP10(getInternalFrameKunde().getKundeDto().getIId()); // falls
			// das
			// Panel
			// noch
			// nicht
			// exitistiert
			panelKundesokoSP10.eventYouAreSelected(false);

			int iSelektierteZeile = panelKundesokoQP10.getTable().getSelectedRow();

			if (iSelektierteZeile > -1) {
				getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
						LPMain.getTextRespectUISPr("part.kundesokomengenstaffel"));
				refreshKundesokomengenstaffelSP11((Integer) panelKundesokoD10.getKeyWhenDetailPanel());
				panelKundesokomengenstaffelSP11.eventYouAreSelected(false);
				// im QP die Buttons setzen.
				LockStateValue d = panelKundesokomengenstaffelD11.getLockedstateDetailMainKey();
				panelKundesokomengenstaffelQP11.updateButtons(d);
			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("part.hint.keinekundesoko"));

				setSelectedComponent(panelKundesokoSP10);
			}
		}

	}

	/**
	 * Behandle ActionEvent; zB Menue-Klick oben.
	 * 
	 * @param e ActionEvent
	 * @throws Throwable
	 * @todo Implement this com.lp.client.frame.component.TabbedPane method
	 */
	protected void lPActionEvent(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(MENUE_ACTION_ADRESSETIKETT)) {
			String add2Title = LPMain.getTextRespectUISPr("part.report.adressetikett");
			getInternalFrame().showReportKriterien(
					new ReportAdressetikett(getInternalFrame(), getInternalFrameKunde().getKundeDto().getPartnerDto(),
							PartnerReportFac.REPORT_ADRESSETIKETT_OPTION_KUNDE, add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_PREISLISTE)) {
			String add2Title = LPMain.getTextRespectUISPr("kunde.report.kundenpreisliste");
			getInternalFrame().showReportKriterien(new ReportKundenpreisliste(getInternalFrameKunde(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_LIEFERMENGEN)) {
			String add2Title = LPMain.getTextRespectUISPr("kunde.report.liefermengen");
			getInternalFrame().showReportKriterien(new ReportLiefermengen(getInternalFrameKunde(), add2Title));
		} else if (e.getActionCommand().equals(MENUE_ACTION_LIEFERSTATISTIK)) {
			if (getInternalFrameKunde().getKundeDto() != null) {
				getInternalFrame().showReportKriterien(new ReportKundeLieferstatistik(getInternalFrameKunde(),
						getInternalFrameKunde().getKundeDto().getPartnerDto().formatFixTitelName1Name2()));
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_UMSATZSTATISTIK)) {
			if (getInternalFrameKunde().getKundeDto() != null) {
				getInternalFrame().showReportKriterien(new ReportKundeumsatzstatistik(getInternalFrameKunde(),
						getInternalFrameKunde().getKundeDto().getPartnerDto().formatFixTitelName1Name2()));
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_STAMMBLATT)) {
			if (getInternalFrameKunde().getKundeDto() != null) {
				getInternalFrame().showReportKriterien(new ReportKundenstammblatt(getInternalFrameKunde(),
						getInternalFrameKunde().getKundeDto().getIId(),
						getInternalFrameKunde().getKundeDto().getPartnerDto().formatFixTitelName1Name2()));
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_KUNDENLISTE)) {
			if (getInternalFrameKunde().getKundeDto() != null) {
				String add2Title = LPMain.getTextRespectUISPr("part.kunde.kundenliste");
				getInternalFrame().showReportKriterien(new ReportKundenliste(getInternalFrameKunde(), add2Title));
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_KUNDENZUSAMMENFUEHREN)) {
			String add2Title = LPMain.getTextRespectUISPr("part.kundenzusammenfuehren");

			PanelDialogKundenZusammenfuehren d = new PanelDialogKundenZusammenfuehren(getKundeDto(), getInternalFrame(),
					add2Title);
			getInternalFrame().showPanelDialog(d);
			d.setVisible(true);
		} else if (MENUE_ACTION_EXPORT_4VENDING.equals(e.getActionCommand())) {
			exportiere4VendingKunden();
		} else if (MENUE_ACTION_PFLEGE_PROVISIONSEMPFAENGER_ERSETZEN.equals(e.getActionCommand())) {
			panelQueryFLRProvisionsempaengerErsetzen = new PanelQueryFLRProvisionsempaengerErsetzen(null,
					SystemFilterFactory.getInstance().createFKMandantCNr(), QueryParameters.UC_ID_PROVISIONSEMPFAENGER,
					null, getInternalFrame(), LPMain.getTextRespectUISPr("kunde.pflege.provisionsempfaenger"));
			panelQueryFLRProvisionsempaengerErsetzen.setMultipleRowSelectionEnabled(true);
			new DialogQuery(panelQueryFLRProvisionsempaengerErsetzen);

		} else if (MENUE_ACTION_PFLEGE_SOKO_IMPORT.equals(e.getActionCommand())) {
			DialogKundesokoimportXLS d = new DialogKundesokoimportXLS(this, null);
		} else if (e.getActionCommand().equals(MENUE_ACTION_PFLEGE_PROFIRST_IMPORT)
				|| e.getActionCommand().equals(MENUE_ACTION_PFLEGE_PROFIRST_IMPORT_VKPREIS)) {

			if (DelegateFactory.getInstance().getStuecklisteDelegate()
					.wirdKundeVonProFirstIgnoriert(getKundeDto().getPartnerDto().getCKbez())) {

				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("kunde.profirstimport.ignorevorhanden"));

				if (b == true) {
					DelegateFactory.getInstance().getStuecklisteDelegate()
							.removeProFirstIgnore(getKundeDto().getPartnerDto().getCKbez());
				} else {
					return;
				}

			}

			// Alle Stueckliste eines Kunden importieren

			if (e.getActionCommand().equals(MENUE_ACTION_PFLEGE_PROFIRST_IMPORT_VKPREIS)) {
				DelegateFactory.getInstance().getStuecklisteDelegate().pruefeUndImportiereProFirst(null,
						getKundeDto().getPartnerDto().getCKbez(), true);
			} else {
				DelegateFactory.getInstance().getStuecklisteDelegate().pruefeUndImportiereProFirst(null,
						getKundeDto().getPartnerDto().getCKbez());
			}

		} else if (e.getActionCommand().equals(MENU_INFO_AENDERUNGEN)) {

			String add2Title = LPMain.getTextRespectUISPr("lp.report.aenderungen");
			getInternalFrame().showReportKriterien(new ReportEntitylog(HvDtoLogClass.KUNDE, getKundeDto().getIId() + "",
					getInternalFrameKunde(), add2Title, getKundeDto().getPartnerDto().formatFixName1Name2()));
		}

	}

	private void exportiere4VendingKunden() {
		kundeexport4vendingController = new VendidataKundenExportController(getInternalFrameKunde());
		Dialog4VendingPartnerExport dialog = new Dialog4VendingKundenExport(LPMain.getInstance().getDesktop(),
				LPMain.getTextRespectUISPr("kunde.export.menu.4vendingkunden"), true, kundeexport4vendingController);
		dialog.setVisible(true);
	}

	public KundeDto getKundeDto() {
		return kundeDto;
	}

	public void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto;
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (eI.getSource() == panelKundekopfdatenD2) {
				// ... QP1 nach selektieren.
				panelKundeQP1.eventYouAreSelected(false);
				getInternalFrameKunde().getKundeDto().setIId((Integer) panelKundeQP1.getSelectedId());
				getInternalFrame().setKeyWasForLockMe(panelKundeQP1.getSelectedId() + "");
				if (panelKundeQP1.getSelectedId() == null) {
					// leer: die restlichen panels deaktivieren
					// damit enableAllPanelsExcept den richtigen enabled!
					setSelectedComponent(panelKundeQP1);
					getInternalFrame().enableAllPanelsExcept(false);
				}
				setSelectedComponent(panelKundeQP1);
			} else if (eI.getSource() == panelKonditionenD3) {
				panelKundeQP1.eventYouAreSelected(false);
				setSelectedComponent(panelKundeQP1);
			} else if (eI.getSource() == panelSelektionBottomD) {
				Integer iId = (Integer) panelSelektionTopQP.getSelectedId();
				if (iId != null) {
					refreshSelektionSP6((Integer) getInternalFrameKunde().getKundeDto().getPartnerIId());
					getInternalFrame().setKeyWasForLockMe(iId.toString());
				}
				panelSelektionSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelSachbearbeiterBottomD4) {
				// ...SP4 refreshen; zb. nach save, loeschen.
				Integer iIdKunde = (Integer) getInternalFrameKunde().getKundeDto().getIId();
				refreshSachbearbeiterSP4(iIdKunde);
				getInternalFrame().setKeyWasForLockMe(iIdKunde + "");
				panelSachbearbeiterSP4.eventYouAreSelected(false);
			} else if (eI.getSource() == panelMaterialBottomD) {
				// ...SP4 refreshen; zb. nach save, loeschen.
				Integer iIdKunde = (Integer) getInternalFrameKunde().getKundeDto().getIId();
				refreshMaterialSP(iIdKunde);
				getInternalFrame().setKeyWasForLockMe(iIdKunde + "");
				panelMaterialSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelSpediteurBottomD) {
				// ...SP4 refreshen; zb. nach save, loeschen.
				Integer iIdKunde = (Integer) getInternalFrameKunde().getKundeDto().getIId();
				refreshSpediteurSP(iIdKunde);
				getInternalFrame().setKeyWasForLockMe(iIdKunde + "");
				panelSpediteurSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKennungBottomD) {
				// ...SP4 refreshen; zb. nach save, loeschen.
				Integer iIdKunde = (Integer) getInternalFrameKunde().getKundeDto().getIId();
				refreshKennungSP(iIdKunde);
				getInternalFrame().setKeyWasForLockMe(iIdKunde + "");
				panelKennungSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKommentarBottomD) {
				// ...SP4 refreshen; zb. nach save, loeschen.
				Object key = panelKommentarTopQP.getSelectedId();
				refreshKommentar(getInternalFrameKunde().getKundeDto().getPartnerIId());
				getInternalFrame().setKeyWasForLockMe(key + "");
				panelKommentarSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelREadresseBottomD6) {
				refreshKonditionenD3((Integer) panelKundeQP1.getSelectedId());
				panelREadresseBottomD6.setKeyWhenDetailPanel(panelKundeQP1.getSelectedId());
				getInternalFrameKunde().getKundeDto().setIId((Integer) panelKundeQP1.getSelectedId());
				panelREadresseBottomD6.eventYouAreSelected(false);
				setSelectedComponent(panelREadresseBottomD6);
			} else if (eI.getSource() == panelAnsprechpartnerBottomD5) {
				Integer iIdPartner = (Integer) DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey((Integer) getInternalFrameKunde().getKundeDto().getIId())
						.getPartnerIId();

				refreshAnsprechpartnerSP5(iIdPartner);
				getInternalFrame().setKeyWasForLockMe(iIdPartner + "");
				panelAnsprechpartnerSP5.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBankBottomD) {
				Integer iIdPartner = (Integer) DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey((Integer) getInternalFrameKunde().getKundeDto().getIId())
						.getPartnerIId();

				refreshBankverbindung(iIdPartner);
				getInternalFrame().setKeyWasForLockMe(iIdPartner + "");
				panelBankSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKundesokoD10) {
				// ...SP10 refreshen; zb. nach save, loeschen.
				refreshKundesokoSP10(getInternalFrameKunde().getKundeDto().getIId());
				getInternalFrame().setKeyWasForLockMe(getInternalFrameKunde().getKundeDto().getIId() + "");
				panelKundesokoSP10.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKundesokomengenstaffelD11) {
				// ...SP11 refreshen; zb. nach save, loeschen.
				refreshKundesokomengenstaffelSP11((Integer) panelKundesokoD10.getKeyWhenDetailPanel());
				getInternalFrame().setKeyWasForLockMe((Integer) panelKundesokoD10.getKeyWhenDetailPanel() + "");
				panelKundesokomengenstaffelSP11.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailKurzbrief) {
				Integer kundeIId = (Integer) panelQueryKurzbrief.getSelectedId();
				refreshKurzbrief(getInternalFrameKunde().getKundeDto().getPartnerIId());
				// getInternalFrame().setKeyWasForLockMe(
				// panelDetailKurzbrief.getKeyWhenDetailPanel() + "");
				getInternalFrame().setKeyWasForLockMe(kundeIId + "");
				panelSplitKurzbrief.eventYouAreSelected(false);

			} else if (eI.getSource() == panelDetailKontakt) {
				refreshKontakt(getInternalFrameKunde().getKundeDto().getPartnerIId());
				getInternalFrame().setKeyWasForLockMe(panelDetailKontakt.getKeyWhenDetailPanel() + "");
				panelSplitKontakt.eventYouAreSelected(false);

			}

		}

		else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelAnsprechpartnerBottomD5) {
				// im QP die Buttons in den Zustand neu setzen.
				panelAnsprechpartnerTopQP5.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelSachbearbeiterBottomD4) {
				panelSachbearbeiterTopQP4.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelBankBottomD) {
				panelBankTopQP.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelKundesokoD10) {
				panelKundesokoQP10.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelSelektionBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelSelektionTopQP.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelMaterialBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelMaterialTopQP.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelSpediteurBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelSpediteurTopQP.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelKennungBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelKennungTopQP.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelKommentarBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelKommentarTopQP.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelKundesokomengenstaffelD11) {
				panelKundesokomengenstaffelQP11.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelDetailKurzbrief) {
				panelQueryKurzbrief.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				panelDetailKurzbrief.beEditMode(true);
			} else if (eI.getSource() == panelDetailKontakt) {
				panelQueryKontakt.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}

		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelSachbearbeiterBottomD4) {
				// QP4 Top refresehen
				Object oKey = panelSachbearbeiterBottomD4.getKeyWhenDetailPanel();
				panelSachbearbeiterTopQP4.eventYouAreSelected(false);
				panelSachbearbeiterTopQP4.setSelectedId(oKey);
				panelSachbearbeiterSP4.eventYouAreSelected(false);
			} else if (eI.getSource() == panelAnsprechpartnerBottomD5) {
				Object oKey = panelAnsprechpartnerBottomD5.getKeyWhenDetailPanel();

				Integer iIdKunde = getKundeDto().getPartnerIId();

				ArrayList<AnsprechpartnerDto> allAnsprechpartner = (ArrayList<AnsprechpartnerDto>) DelegateFactory
						.getInstance().getAnsprechpartnerDelegate().getAllAnsprechpartner(iIdKunde);

				Integer iIdPosition1 = null;
				Integer iSort = null;
				for (int i = 0; i < allAnsprechpartner.size(); i++) {
					if (oKey.equals(allAnsprechpartner.get(i).getIId())) {
						iIdPosition1 = allAnsprechpartner.get(i).getIId();
						iSort = allAnsprechpartner.get(i).getISort();
						break;
					}
				}

				boolean iSortVorhanden = false;
				Integer iIdPosition2 = null;
				PartnerDto partner = null;
				for (int i = 0; i < allAnsprechpartner.size(); i++) {
					if (!oKey.equals(allAnsprechpartner.get(i).getIId())
							&& allAnsprechpartner.get(i).getISort().equals(iSort)) {
						iSortVorhanden = true;
						iIdPosition2 = allAnsprechpartner.get(i).getIId();
						partner = DelegateFactory.getInstance().getPartnerDelegate()
								.partnerFindByPrimaryKey(allAnsprechpartner.get(i).getPartnerIIdAnsprechpartner());
						break;
					}
				}

				if (iSortVorhanden) {
					Object[] options = { LPMain.getTextRespectUISPr("lp.vorher"),
							LPMain.getTextRespectUISPr("lp.nachher"), LPMain.getTextRespectUISPr("lp.abbrechen") };

					int iOption = DialogFactory.showModalDialog(
							getInternalFrame(), LPMain
									.getMessageTextRespectUISPr("part.ansprechpartner.sortierung.vorhanden", iSort,
											partner.getAnredeCNr().trim() + " " + partner.getCName1nachnamefirmazeile1()
													+ " " + partner.getCName2vornamefirmazeile2()),
							"", options, options[1]);
					if (iOption == JOptionPane.YES_OPTION) { // vorher
						DelegateFactory.getInstance().getAnsprechpartnerDelegate().vertauscheAnsprechpartner(iSort,
								iSort - AnsprechpartnerISortValues.ANSPRECHPARTNER_ISORT_STEP / 2, iIdPosition1,
								iIdPosition2);
					} else if (iOption == JOptionPane.NO_OPTION) { // nachher
						DelegateFactory.getInstance().getAnsprechpartnerDelegate().vertauscheAnsprechpartner(iSort,
								iSort + AnsprechpartnerISortValues.ANSPRECHPARTNER_ISORT_STEP / 2, iIdPosition1,
								iIdPosition2);
					} else if (iOption == JOptionPane.CANCEL_OPTION) {

					} else if (iOption == JOptionPane.CLOSED_OPTION) {

					}
				}

				DelegateFactory.getInstance().getAnsprechpartnerDelegate().renumberISortAnsprechpartner(iIdKunde);

				panelAnsprechpartnerTopQP5.eventYouAreSelected(false);
				panelAnsprechpartnerTopQP5.setSelectedId(oKey);
				panelAnsprechpartnerSP5.eventYouAreSelected(false);

			} else if (eI.getSource() == panelBankBottomD) {
				Object oKey = panelBankBottomD.getKeyWhenDetailPanel();
				panelBankTopQP.eventYouAreSelected(false);
				panelBankTopQP.setSelectedId(oKey);
				panelBankSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKundesokoD10) {
				Object oKey = panelKundesokoD10.getKeyWhenDetailPanel();
				panelKundesokoQP10.eventYouAreSelected(false);
				panelKundesokoQP10.setSelectedId(oKey);
				panelKundesokoSP10.eventYouAreSelected(false);
			} else if (eI.getSource() == panelSelektionBottomD) {
				Object oKey = panelSelektionBottomD.getKeyWhenDetailPanel();
				panelSelektionTopQP.eventYouAreSelected(false);
				panelSelektionTopQP.setSelectedId(oKey);
				panelSelektionSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelMaterialBottomD) {
				Object oKey = panelMaterialBottomD.getKeyWhenDetailPanel();
				panelMaterialTopQP.eventYouAreSelected(false);
				panelMaterialTopQP.setSelectedId(oKey);
				panelMaterialSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelSpediteurBottomD) {
				Object oKey = panelSpediteurBottomD.getKeyWhenDetailPanel();
				panelSpediteurTopQP.eventYouAreSelected(false);
				panelSpediteurTopQP.setSelectedId(oKey);
				panelSpediteurSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKennungBottomD) {
				Object oKey = panelKennungBottomD.getKeyWhenDetailPanel();
				panelKennungTopQP.eventYouAreSelected(false);
				panelKennungTopQP.setSelectedId(oKey);
				panelKennungSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKommentarBottomD) {
				Object oKey = panelKommentarBottomD.getKeyWhenDetailPanel();
				panelKommentarTopQP.eventYouAreSelected(false);
				panelKommentarTopQP.setSelectedId(oKey);
				panelKommentarSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKundesokomengenstaffelD11) {
				Object oKey = panelKundesokomengenstaffelD11.getKeyWhenDetailPanel();
				panelKundesokomengenstaffelQP11.eventYouAreSelected(false);
				panelKundesokomengenstaffelQP11.setSelectedId(oKey);
				panelKundesokomengenstaffelSP11.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailKurzbrief) {
				Object oKey = panelDetailKurzbrief.getKeyWhenDetailPanel();
				panelQueryKurzbrief.eventYouAreSelected(false);
				panelQueryKurzbrief.setSelectedId(oKey);
				panelSplitKurzbrief.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailKontakt) {
				Object oKey = panelDetailKontakt.getKeyWhenDetailPanel();
				panelQueryKontakt.eventYouAreSelected(false);
				panelQueryKontakt.setSelectedId(oKey);
				panelSplitKontakt.eventYouAreSelected(false);
			}

			else if (eI.getSource() == panelKundekopfdatenD2) {
				// MB 04.05.06 IMS 1676
				panelKundeQP1.clearDirektFilter();
				if (panelKundeQP1.getSelectedId() == null || // wenns der erste
																// ist ... oder:
						(panelKundeQP1.getSelectedId() != null && !(panelKundeQP1.getSelectedId(). // wenn ein falscher
						// selektiert ist
								equals(panelKundekopfdatenD2.getKeyWhenDetailPanel())))) {
					Object key = panelKundekopfdatenD2.getKeyWhenDetailPanel();
					panelKundeQP1.eventYouAreSelected(false);
					panelKundeQP1.setSelectedId(key);
					panelKundeQP1.eventYouAreSelected(false);
				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelKundekopfdatenD2) {
				panelKundekopfdatenD2.eventYouAreSelected(false); // refresh auf
				// das
				// gesamte
				// 1:n panel
			} else if (eI.getSource() == panelKonditionenD3) {
				panelKonditionenD3.eventYouAreSelected(false);
			} else if (eI.getSource() == panelSachbearbeiterBottomD4) {
				panelSachbearbeiterSP4.eventYouAreSelected(false);
			} else if (eI.getSource() == panelAnsprechpartnerBottomD5) {
				panelAnsprechpartnerSP5.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBankBottomD) {
				panelBankSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelREadresseBottomD6) {
				panelREadresseBottomD6.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKundesokoD10) {
				panelKundesokoSP10.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKundesokomengenstaffelD11) {
				panelKundesokomengenstaffelSP11.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailKurzbrief) {
				panelSplitKurzbrief.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailKontakt) {
				panelSplitKontakt.eventYouAreSelected(false);
			} else if (eI.getSource() == panelSelektionBottomD) {
				panelSelektionSP.eventYouAreSelected(false); // refresh auf das
				// gesamte 1:n
				// panel
			} else if (eI.getSource() == panelMaterialBottomD) {
				panelMaterialSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelSpediteurBottomD) {
				panelSpediteurSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKennungBottomD) {
				panelKennungSP.eventYouAreSelected(false);
			} else if (eI.getSource() == panelKommentarBottomD) {
				panelKommentarSP.eventYouAreSelected(false);
			}

		}

		else if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelKundeQP1) {
				if (panelKundeQP1.getSelectedId() != null) {
					Object iId = ((ISourceEvent) eI.getSource()).getIdSelected();
					getInternalFrameKunde().getKundeDto().setIId((Integer) iId);

					if (iId != null) {
						setKundeDto(
								DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey((Integer) iId));
					}

					getInternalFrame().setKeyWasForLockMe(iId + "");
					if (panelKundekopfdatenD2 != null) {
						panelKundekopfdatenD2.setKeyWhenDetailPanel(panelKundeQP1.getSelectedId());
					}
					if (iId == null) {
						getInternalFrame().enableAllMyKidPanelsExceptMe(IDX_PANE_KUNDE, false);
					} else {
						// kunden auch lesen
						getInternalFrameKunde().setKundeDto(
								DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey((Integer) iId));
						getInternalFrame().enableAllMyKidPanelsExceptMe(IDX_PANE_KUNDE, true);
					}

					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANE_KUNDE, true);
					mapButton.setPartnerDto(kundeDto.getPartnerDto());
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANE_KUNDE, false);
					mapButton.setPartnerDto(null);
				}

			} else if (eI.getSource() == panelSachbearbeiterTopQP4) {
				Integer iIdSachb = (Integer) panelSachbearbeiterTopQP4.getSelectedId();
				Integer iIdKunde = (Integer) getInternalFrameKunde().getKundeDto().getIId();
				refreshSachbearbeiterSP4(iIdKunde);
				panelSachbearbeiterBottomD4.setKeyWhenDetailPanel(iIdSachb);
				panelSachbearbeiterBottomD4.eventYouAreSelected(false);
				panelSachbearbeiterTopQP4.updateButtons(panelSachbearbeiterBottomD4.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelSelektionTopQP) {
				Object pASelektionPK = panelSelektionTopQP.getSelectedId();
				panelSelektionBottomD.setKeyWhenDetailPanel(pASelektionPK);
				panelSelektionBottomD.eventYouAreSelected(false);
				panelSelektionTopQP.updateButtons();
			} else if (eI.getSource() == panelMaterialTopQP) {
				Object pASelektionPK = panelMaterialTopQP.getSelectedId();
				panelMaterialBottomD.setKeyWhenDetailPanel(pASelektionPK);
				panelMaterialBottomD.eventYouAreSelected(false);
				panelMaterialTopQP.updateButtons();
			} else if (eI.getSource() == panelSpediteurTopQP) {
				Object pASelektionPK = panelSpediteurTopQP.getSelectedId();
				panelSpediteurBottomD.setKeyWhenDetailPanel(pASelektionPK);
				panelSpediteurBottomD.eventYouAreSelected(false);
				panelSpediteurTopQP.updateButtons();
			} else if (eI.getSource() == panelKennungTopQP) {
				Object pASelektionPK = panelKennungTopQP.getSelectedId();
				panelKennungBottomD.setKeyWhenDetailPanel(pASelektionPK);
				panelKennungBottomD.eventYouAreSelected(false);
				panelKennungTopQP.updateButtons();
			} else if (eI.getSource() == panelKommentarTopQP) {
				Object key = panelKommentarTopQP.getSelectedId();
				panelKommentarBottomD.setKeyWhenDetailPanel(key);
				panelKommentarBottomD.eventYouAreSelected(false);
				panelKommentarTopQP.updateButtons();
			} else if (eI.getSource() == panelAnsprechpartnerTopQP5) {
				getInternalFrameKunde().setKundeDto(DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(getInternalFrameKunde().getKundeDto().getIId()));
				Integer iIdAnsprechpartner = (Integer) panelAnsprechpartnerTopQP5.getSelectedId();
				Integer iIdPartner = getInternalFrameKunde().getKundeDto().getPartnerIId();
				refreshAnsprechpartnerSP5(iIdPartner);
				panelAnsprechpartnerBottomD5.setKeyWhenDetailPanel(iIdAnsprechpartner);
				panelAnsprechpartnerBottomD5.eventYouAreSelected(false);
				panelAnsprechpartnerTopQP5.updateButtons(panelAnsprechpartnerBottomD5.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelBankTopQP) {
				getInternalFrameKunde().setKundeDto(DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(getInternalFrameKunde().getKundeDto().getIId()));
				Integer iIdAnsprechpartner = (Integer) panelBankTopQP.getSelectedId();
				Integer iIdPartner = getInternalFrameKunde().getKundeDto().getPartnerIId();
				refreshBankverbindung(iIdPartner);
				panelBankBottomD.setKeyWhenDetailPanel(iIdAnsprechpartner);
				panelBankBottomD.eventYouAreSelected(false);
				panelBankTopQP.updateButtons(panelBankBottomD.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelLieferstatistikQP6) {
				panelLieferstatistikQP6.updateButtons();
			} else if (eI.getSource() == panelKundesokoQP10) {
				refreshKundesokoSP10(getInternalFrameKunde().getKundeDto().getIId());
				Integer iId = (Integer) panelKundesokoQP10.getSelectedId();
				panelKundesokoD10.setKeyWhenDetailPanel(iId);
				panelKundesokoD10.eventYouAreSelected(false);
				panelKundesokoQP10.updateButtons(panelKundesokoD10.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelKundesokomengenstaffelQP11) {
				refreshKundesokomengenstaffelSP11((Integer) panelKundesokoD10.getKeyWhenDetailPanel());
				Integer iId = (Integer) panelKundesokomengenstaffelQP11.getSelectedId();
				panelKundesokomengenstaffelD11.setKeyWhenDetailPanel(iId);
				panelKundesokomengenstaffelD11.eventYouAreSelected(false);
				panelKundesokomengenstaffelQP11
						.updateButtons(panelKundesokomengenstaffelD11.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryKurzbrief) {
				Integer iId = (Integer) panelQueryKurzbrief.getSelectedId();
				panelDetailKurzbrief.setKeyWhenDetailPanel(iId);
				panelDetailKurzbrief.eventYouAreSelected(false);
				panelQueryKurzbrief.updateButtons();
			} else if (eI.getSource() == panelQueryKontakt) {
				Integer iId = (Integer) panelQueryKontakt.getSelectedId();
				panelDetailKontakt.setKeyWhenDetailPanel(iId);
				panelDetailKontakt.eventYouAreSelected(false);
				panelQueryKontakt.updateButtons();
			}
		}

		else if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelKundeQP1) {
				Integer key = (Integer) getInternalFrameKunde().getKundeDto().getIId();
				// wenn noch nicht erzeugt, dann jetzt erzeugen; wegen is lazy.
				refreshKundePartnerD2(key);
				// jetzt ab zu D2.
				setSelectedComponent(panelKundekopfdatenD2);
			} else if (eI.getSource() == panelPartner) {
				// Neu aus Partner.
				Integer iIdPartner = (Integer) panelPartner.getSelectedId();
				refreshKundePartnerD2(null);
				getInternalFrameKunde().setKundeDto(new KundeDto());
				getInternalFrameKunde().getKundeDto().setPartnerIId(iIdPartner);
				panelKundekopfdatenD2.eventActionNew(eI, true, false);
				PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
						.partnerFindByPrimaryKey(iIdPartner);
				if (partnerDto != null) {
					if (partnerDto.getLandplzortDto() != null) {
						setSelectedComponent(panelKundekopfdatenD2);
						PanelKundekopfdaten Panelkundekopfdaten = (PanelKundekopfdaten) panelKundekopfdatenD2;
						Panelkundekopfdaten.setDefaultMWSTforLand(partnerDto.getLandplzortDto().getLandDto());
					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("part.error.csvimport.keinort"));
					}
				}
			} else if (panelQueryFLRProvisionsempaengerErsetzen != null
					&& eI.getSource() == panelQueryFLRProvisionsempaengerErsetzen.getPanelQueryFLRPersonal()) {
				panelQueryFLRProvisionsempaengerErsetzen.ersetzen(
						(Integer) panelQueryFLRProvisionsempaengerErsetzen.getPanelQueryFLRPersonal().getSelectedId());

			}
		}

		else if ((eI.getID() == ItemChangedEvent.ACTION_NEW) || (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW)) {
			// btnnew: einen neuen machen.
			if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
				String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();
				if (eI.getSource() == panelKundesokoQP10 && sAspectInfo.endsWith(BUTTON_IMPORT_KUNDESOKO)) {
					DialogKundesokoimportXLS d = new DialogKundesokoimportXLS(this, getKundeDto().getIId());
					panelKundesokoSP10.eventYouAreSelected(false);
					return;
				}

				if (sAspectInfo.endsWith(ACTION_SPECIAL_BAUMSICHT)) {
					DialogKundeStruktur d = new DialogKundeStruktur(panelKundeQP1,
							panelKundeQP1.getWcbVersteckteFelderAnzeigen().isSelected());
					LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);
					return;
				}

				if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
					// copypaste
					einfuegenHV();
					panelKundesokoSP10.eventYouAreSelected(false);
					return;
				}

			}

			if (eI.getSource() == panelKundeQP1) {
				// im QP1 auf new gedrueckt.
				// btnnew: wenn es bisher keinen eintrag gibt, die restlichen
				// panels aktivieren
				if (panelKundeQP1.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}

				if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
					refreshKundePartnerD2(null);
					getInternalFrameKunde().setKundeDto(new KundeDto());
					// jetzt in's richtige panel mit new.
					panelKundekopfdatenD2.eventActionNew(eI, true, false);
					// auch ui maessig, fuehrt zu eventYouAreSelected
					setSelectedComponent(panelKundekopfdatenD2);
				} else {
					// Neu aus Partner.
					dialogPartner(eI);
				}
			} else if (eI.getSource() == panelSachbearbeiterTopQP4) {
				// im QP4 auf new gedrueckt.
				// jetzt in's richtige panel mit new.
				panelSachbearbeiterBottomD4.eventActionNew(eI, true, false);
				// jetzt eventYouAreSelected ausloesen wegen naechster zeile.
				panelSachbearbeiterBottomD4.eventYouAreSelected(false);
				// auch ui maessig, fuehrt NICHT zu eventYouAreSelected
				setSelectedComponent(panelSachbearbeiterSP4);
			} else if (eI.getSource() == panelAnsprechpartnerTopQP5) {
				// im QP5 auf new gedrueckt.
				// jetzt in's richtige panel mit new.
				panelAnsprechpartnerBottomD5.eventActionNew(eI, true, false);
				// jetzt eventYouAreSelected ausloesen wegen naechster zeile.
				panelAnsprechpartnerBottomD5.eventYouAreSelected(false);
				// auch ui maessig, fuehrt NICHT zu eventYouAreSelected
				setSelectedComponent(panelAnsprechpartnerSP5);
			} else if (eI.getSource() == panelSelektionTopQP) {
				panelSelektionBottomD.eventActionNew(eI, true, false);
				panelSelektionBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelSelektionSP);
			} else if (eI.getSource() == panelMaterialTopQP) {
				panelMaterialBottomD.eventActionNew(eI, true, false);
				panelMaterialBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelMaterialSP);
			} else if (eI.getSource() == panelSpediteurTopQP) {
				panelSpediteurBottomD.eventActionNew(eI, true, false);
				panelSpediteurBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelSpediteurSP);
			} else if (eI.getSource() == panelKennungTopQP) {
				panelKennungBottomD.eventActionNew(eI, true, false);
				panelKennungBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelKennungSP);
			} else if (eI.getSource() == panelKommentarTopQP) {
				panelKommentarBottomD.eventActionNew(eI, true, false);
				panelKommentarBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelKommentarSP);
			} else if (eI.getSource() == panelBankTopQP) {
				// im QP5 auf new gedrueckt.
				// jetzt in's richtige panel mit new.
				panelBankBottomD.eventActionNew(eI, true, false);
				// jetzt eventYouAreSelected ausloesen wegen naechster zeile.
				panelBankBottomD.eventYouAreSelected(false);
				// auch ui maessig, fuehrt NICHT zu eventYouAreSelected
				setSelectedComponent(panelBankSP);
			} else if (eI.getSource() == panelKundesokoQP10) {
				panelKundesokoD10.eventActionNew(eI, true, false);
				panelKundesokoD10.eventYouAreSelected(false);
				setSelectedComponent(panelKundesokoSP10);
			} else if (eI.getSource() == panelKundesokomengenstaffelQP11) {
				panelKundesokomengenstaffelD11.eventActionNew(eI, true, false);
				panelKundesokomengenstaffelD11.eventYouAreSelected(false);
				setSelectedComponent(panelKundesokomengenstaffelSP11);
			} else if (eI.getSource() == panelQueryKurzbrief) {
				panelDetailKurzbrief.bePlain();
				panelDetailKurzbrief.eventActionNew(eI, true, false);
				panelDetailKurzbrief.eventYouAreSelected(false);
				setSelectedComponent(panelSplitKurzbrief);
			} else if (eI.getSource() == panelQueryKontakt) {
				panelDetailKontakt.eventActionNew(eI, true, false);
				panelDetailKontakt.eventYouAreSelected(false);
				setSelectedComponent(panelSplitKontakt);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_PRINT) {
			myLogger.error("KD>Lieferstatistik 1: " + 0, null);
			Defaults.lUhrQuickDirtyBS = System.currentTimeMillis();
			if (getInternalFrameKunde().getKundeDto() != null) {
				getInternalFrame().showReportKriterien(new ReportKundeLieferstatistik(getInternalFrameKunde(),
						getInternalFrameKunde().getKundeDto().getPartnerDto().formatFixTitelName1Name2()));
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (eI.getSource() == getPanelKriterienUmsatz()) {

				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = getPanelKriterienUmsatz().getAlleFilterKriterien();

				// die Kriterien dem PanelTabelle setzen
				refreshUmsatzPT7().setDefaultFilter(aAlleKriterien);

				// die Uebersicht aktualisieren redundant, wenn man dann ohnehin
				// wechselt
				setComponentAt(IDX_PANE_UMSATZSTATISTIK, panelUebersicht);
				refreshUmsatzPT7().eventYouAreSelected(false);
				refreshUmsatzPT7().updateButtons(new LockStateValue(null, null, PanelBasis.LOCK_IS_NOT_LOCKED));

				// man steht auf alle Faelle auf der Uebersicht
				// setSelectedComponent(getPanelUebersicht());
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			// Einer der Knoepfe zur Reihung der Positionen auf einem PanelQuery
			// wurde gedrueckt
			if (eI.getSource() == panelAnsprechpartnerTopQP5) {
				int iPos = panelAnsprechpartnerTopQP5.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelAnsprechpartnerTopQP5.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelAnsprechpartnerTopQP5.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getAnsprechpartnerDelegate().vertauscheAnsprechpartner(iIdPosition,
							iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelAnsprechpartnerTopQP5.setSelectedId(iIdPosition);
					panelAnsprechpartnerSP5.eventYouAreSelected(true);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (eI.getSource() == panelAnsprechpartnerTopQP5) {
				int iPos = panelAnsprechpartnerTopQP5.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelAnsprechpartnerTopQP5.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelAnsprechpartnerTopQP5.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelAnsprechpartnerTopQP5.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getAnsprechpartnerDelegate().vertauscheAnsprechpartner(iIdPosition,
							iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelAnsprechpartnerTopQP5.setSelectedId(iIdPosition);
					panelAnsprechpartnerSP5.eventYouAreSelected(true);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (eI.getSource() == panelAnsprechpartnerTopQP5) {
				panelAnsprechpartnerBottomD5.eventActionNew(eI, true, false);
				panelAnsprechpartnerBottomD5.eventYouAreSelected(false); // Buttons
				// schalten

				Integer iIdAnsprechpartner = (Integer) panelAnsprechpartnerTopQP5.getSelectedId();
				Integer iIdKunde = getKundeDto().getPartnerIId();
				refreshAnsprechpartnerSP5(iIdKunde);
				Integer iSort = null;
				if (iIdAnsprechpartner != null) {
					iSort = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner).getISort();
				}

				panelAnsprechpartnerBottomD5.setDefaults(iSort);

			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			if (eI.getSource() instanceof PanelQuery) {
				PanelQuery pq = (PanelQuery) eI.getSource();
				if (pq.getAspect().equals(ACTION_SPECIAL_NEW_EMAIL)) {
					// panelDetailKurzbrief.beHtml();
					panelDetailKurzbrief.eventActionNew(eI, true, false);
					panelDetailKurzbrief.eventYouAreSelected(false);
					setSelectedComponent(panelSplitKurzbrief);
					panelDetailKurzbrief.beHtml();
					panelDetailKurzbrief.beEditMode(true);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_DROP) {
			myLogger.info("Drop Changed on " + eI.getSource());
			if (eI.getSource() == panelDetailKurzbrief) {
				if (eI instanceof ItemChangedEventDrop) {
					ItemChangedEventDrop dropEvent = (ItemChangedEventDrop) eI;
					Integer kbId = DelegateFactory.getInstance().getEmailMediaDelegate().createKurzbriefFromEmail(
							getKundeDto().getPartnerDto().getIId(), LocaleFac.BELEGART_KUNDE,
							(MediaEmailMetaDto) dropEvent.getDropData());
					panelQueryKurzbrief.setSelectedId(kbId);
					panelSplitKurzbrief.eventYouAreSelected(false);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			copyHV();
		}
	}

	private void refreshKurzbrief(Integer iIdPartnerI) throws Throwable {

		if (panelSplitKurzbrief == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance().createFKKurzbriefpartner(iIdPartnerI,
					LocaleFac.BELEGART_KUNDE);

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_EMAIL_CLIENT)) {
				aWhichStandardButtonIUse = new String[] { PanelBasis.ACTION_NEW, ACTION_SPECIAL_NEW_EMAIL };
			}

			panelQueryKurzbrief = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_PARTNERKURZBRIEF,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("lp.kurzbrief"), true);

			if (aWhichStandardButtonIUse.length > 1) {
				panelQueryKurzbrief.createAndSaveAndShowButton("/com/lp/client/res/documentHtml.png",
						LPMain.getTextRespectUISPr("lp.newHtml"), ACTION_SPECIAL_NEW_EMAIL,
						KeyStroke.getKeyStroke('N', InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK), null);
			}

			panelDetailKurzbrief = new PanelPartnerKurzbrief(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kurzbrief"), null, LocaleFac.BELEGART_KUNDE);

			panelSplitKurzbrief = new DropPanelSplit(getInternalFrame(), panelDetailKurzbrief, panelQueryKurzbrief,
					150);

			setComponentAt(IDX_PANE_KURZBRIEF, panelSplitKurzbrief);
		} else {
			// filter refreshen.
			panelQueryKurzbrief.setDefaultFilter(
					PartnerFilterFactory.getInstance().createFKKurzbriefpartner(iIdPartnerI, LocaleFac.BELEGART_KUNDE));
		}
	}

	private void refreshSachbearbeiterSP4(Object key) throws Exception, Throwable {
		if (panelSachbearbeiterSP4 == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance().createFKKunde((Integer) key);

			panelSachbearbeiterTopQP4 = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_SACHBERABEITER,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("lp.sachbearbeiter"),
					true);

			panelSachbearbeiterBottomD4 = new PanelSachbearbeiter(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.sachbearbeiter"), null);

			panelSachbearbeiterSP4 = new PanelSplit(getInternalFrame(), panelSachbearbeiterBottomD4,
					panelSachbearbeiterTopQP4, 200);
			setComponentAt(IDX_PANE_SACHBEARBEITER, panelSachbearbeiterSP4);
		} else {
			// filter refreshen.
			panelSachbearbeiterTopQP4.setDefaultFilter(PartnerFilterFactory.getInstance().createFKKunde((Integer) key));
		}
	}

	private void refreshAnsprechpartnerSP5(Integer iIdPartnerI) throws Throwable {

		if (panelAnsprechpartnerSP5 == null) {

			// die zusaetzlichen Buttons am PanelQuery anbringen
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1, PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance().createFKAnsprechpartner(iIdPartnerI);

			// panelAnsprechpartnerTopQP5 = new PanelQuery(querytypes, filters,
			// QueryParameters.UC_ID_ANSPRECHPARTNER, aWhichButtonIUse,
			// getInternalFrame(),
			// LPMain.getTextRespectUISPr("label.ansprechpartner"), true);

			panelAnsprechpartnerTopQP5 = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_ANSPRECHPARTNER,
					aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("label.ansprechpartner"), true,
					PartnerFilterFactory.getInstance().createFKVAnsprechpartner(), null);

			panelAnsprechpartnerBottomD5 = new PanelKundeAnsprechpartner(getInternalFrame(),
					LPMain.getTextRespectUISPr("label.ansprechpartner"), null);

			panelAnsprechpartnerSP5 = new PanelSplit(getInternalFrame(), panelAnsprechpartnerBottomD5,
					panelAnsprechpartnerTopQP5, 150);

			// panelAnsprechpartnerTopQP5
			// .befuellePanelFilterkriterienDirektUndVersteckte(
			// PartnerFilterFactory.getInstance().createFKDAnsprechpartnerPartnerName(),
			// null,
			// PartnerFilterFactory.getInstance().createFKVAnsprechpartner());

			panelAnsprechpartnerTopQP5
					.addDirektFilter(PartnerFilterFactory.getInstance().createFKDAnsprechpartnerPartnerName());

			setComponentAt(IDX_PANE_ANSPRECHPARTNER, panelAnsprechpartnerSP5);
		} else {
			// filter refreshen.
			panelAnsprechpartnerTopQP5
					.setDefaultFilter(PartnerFilterFactory.getInstance().createFKAnsprechpartner(iIdPartnerI));
		}
	}

	private void refreshEigenschaften(Integer key) throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD, };

		panelDetailKundeneigenschaft = new PanelDynamisch(getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.eigenschaften"), panelKundeQP1, PanelFac.PANEL_KUNDENEIGENSCHAFTEN,
				HelperClient.LOCKME_KUNDE, aWhichButtonIUse);
		setComponentAt(IDX_PANEL_KUNDENEIGENSCHAFTEN, panelDetailKundeneigenschaft);

	}

	private void refreshPartnerEigenschaften(Integer key) throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD, };

		panelDetailpartnereigenschaft = new PanelDynamisch(getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.eigenschaften"), panelKundeQP1, PanelFac.PANEL_PARTNEREIGENSCHAFTEN,
				HelperClient.LOCKME_PARTNER, aWhichButtonIUse, null, getKundeDto().getPartnerIId() + "");
		setComponentAt(IDX_PANEL_PARTNEREIGENSCHAFTEN, panelDetailpartnereigenschaft);

	}

	private void refreshSelektionSP6(Integer iIdPartnerI) throws Throwable {

		if (panelSelektionTopQP == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance().createFKPASelektion(iIdPartnerI);

			panelSelektionTopQP = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_PARTNERSELEKTION,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("lp.uebersicht.detail"),
					true);

			panelSelektionBottomD = new PanelPASelektionKunde(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.selektion"), null, this);

			panelSelektionSP = new PanelSplit(getInternalFrame(), panelSelektionBottomD, panelSelektionTopQP, 200);
			setComponentAt(IDX_PANEL_SELEKTION, panelSelektionSP);
		} else {
			// filter refreshen.
			panelSelektionTopQP.setDefaultFilter(PartnerFilterFactory.getInstance().createFKPASelektion(iIdPartnerI));
		}
	}

	private void refreshMaterialSP(Integer kundeIId) throws Throwable {

		if (panelMaterialTopQP == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance().createFKKunde(kundeIId);

			panelMaterialTopQP = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_KUNDEMATERIAL,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("part.materialnotierung"),
					true);

			panelMaterialBottomD = new PanelKundematerial(getInternalFrame(),
					LPMain.getTextRespectUISPr("part.materialnotierung"), null, this);

			panelMaterialSP = new PanelSplit(getInternalFrame(), panelMaterialBottomD, panelMaterialTopQP, 270);
			setComponentAt(IDX_PANEL_MATERIAL, panelMaterialSP);
		} else {
			// filter refreshen.
			panelMaterialTopQP.setDefaultFilter(PartnerFilterFactory.getInstance().createFKKunde(kundeIId));
		}
	}

	private void refreshSpediteurSP(Integer kundeIId) throws Throwable {

		if (panelSpediteurTopQP == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance().createFKKunde(kundeIId);

			panelSpediteurTopQP = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_KUNDESPEDITEUR,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("kunde.kundespediteur"),
					true);

			panelSpediteurBottomD = new PanelKundespediteur(getInternalFrame(),
					LPMain.getTextRespectUISPr("kunde.kundespediteur"), null, this);

			panelSpediteurSP = new PanelSplit(getInternalFrame(), panelSpediteurBottomD, panelSpediteurTopQP, 270);
			setComponentAt(IDX_PANEL_SPEDITEUR, panelSpediteurSP);
		} else {
			// filter refreshen.
			panelSpediteurTopQP.setDefaultFilter(PartnerFilterFactory.getInstance().createFKKunde(kundeIId));
		}
	}

	private void refreshKennungSP(Integer kundeIId) throws Throwable {

		if (panelKennungTopQP == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance().createFKKunde(kundeIId);

			panelKennungTopQP = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_KUNDEKENNUNG,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("kunde.kundekennung"),
					true);

			panelKennungTopQP
					.addDirektFilter(new FilterKriteriumDirekt("flrkennung.c_nr", "", FilterKriterium.OPERATOR_LIKE,
							LPMain.getTextRespectUISPr("label.kennung"), FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
																													// als
							// 'XX%'
							true, true, Facade.MAX_UNBESCHRAENKT));
			panelKennungTopQP.addDirektFilter(new FilterKriteriumDirekt("c_wert", "", FilterKriterium.OPERATOR_LIKE,
					LPMain.getTextRespectUISPr("lp.wert"), FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
																									// als
					// 'XX%'
					true, true, Facade.MAX_UNBESCHRAENKT));

			panelKennungBottomD = new PanelKundeKennung(getInternalFrame(),
					LPMain.getTextRespectUISPr("kunde.kundekennung"), this);

			panelKennungSP = new PanelSplit(getInternalFrame(), panelKennungBottomD, panelKennungTopQP, 270);
			setComponentAt(IDX_PANEL_KENNUNG, panelKennungSP);
		} else {
			// filter refreshen.
			panelKennungTopQP.setDefaultFilter(PartnerFilterFactory.getInstance().createFKKunde(kundeIId));
		}
	}

	private void refreshKommentar(Integer iIdPartnerI) throws Throwable {

		if (panelKommentarTopQP == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance().createFKPartnerkommentar(iIdPartnerI, true);

			panelKommentarTopQP = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_PARTNERKOMMENTAR,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("part.kommentar"), true);

			panelKommentarBottomD = new PanelKundeLieferantKommentar(getInternalFrame(),
					LPMain.getTextRespectUISPr("part.kommentar"), null, true);

			panelKommentarSP = new PanelSplit(getInternalFrame(), panelKommentarBottomD, panelKommentarTopQP, 160);
			setComponentAt(IDX_PANEL_KOMMENTAR, panelKommentarSP);
		} else {
			// filter refreshen.
			panelKommentarTopQP
					.setDefaultFilter(PartnerFilterFactory.getInstance().createFKPartnerkommentar(iIdPartnerI, true));
		}
	}

	private void refreshKundePartnerD2(Object key) throws Throwable {
		if (panelKundekopfdatenD2 == null) {
			// panelKundekopfdatenD2 = new
			// PanelKundekopfdaten(getInternalFrame(),
			// LPMain.getTextRespectUISPr("lp.kopfdaten"),
			// key);
			panelKundekopfdatenD2 = new PanelKundekopfdaten(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kopfdaten"), key, this);
			setComponentAt(IDX_PANE_KOPFDATEN, panelKundekopfdatenD2);
		}
	}

	private void refreshKonditionenD3(Object key) throws Throwable {
		if (panelKonditionenD3 == null) {
			panelKonditionenD3 = new PanelKundeKonditionen(getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.konditionen"), key);
			setComponentAt(IDX_PANE_KONDITIONEN, panelKonditionenD3);
		}
	}

	Integer getSelectedKundeIId() {
		return (Integer) panelKundeQP1.getSelectedId();
	}

	private PanelTabelle refreshUmsatzPT7() throws Throwable {
		if (panelUebersicht == null) {

			KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(getSelectedKundeIId());

			panelUebersicht = new PanelTabelleUmsatz(QueryParameters.UC_ID_KUNDE_UMSATZSTATISTIK,
					LPMain.getTextRespectUISPr("lp.umsatzuebersicht"), getInternalFrame(),
					kundeDto.getPartnerDto().formatAnrede());
		}
		panelUebersicht.setAdd2Title(LPMain.getTextRespectUISPr("lp.umsatzuebersicht") + " "
				+ getInternalFrameKunde().getKundeDto().getPartnerDto().formatAnrede());

		panelUebersicht.setAnrede(getInternalFrameKunde().getKundeDto().getPartnerDto().formatAnrede());
		return panelUebersicht;
	}

	private void refreshRechnungsadresseDX(Object key) throws Throwable {
		if (panelREadresseBottomD6 == null) {
			// panelREadresseBottomD6 = new PanelKundeRechnungsadresse(
			// getInternalFrame(), LPMain
			// .getTextRespectUISPr("lp.rechnungsadresse"), key);
			// panelREadresseBottomD6 = new PanelKundeRechnungsadresse(
			// getInternalFrame(), LPMain
			// .getTextRespectUISPr("lp.rechnungsadresse"), key, this);

			panelREadresseBottomD6 = new PanelKundeRechnungsadresse(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.rechnungsadresse"), key, new IPartnerDtoService() {

						@Override
						public void setServicePartnerDto(PartnerDto partnerDto) {
							getInternalFrameKunde().getKundeDto().setPartnerRechnungsadresseDto(partnerDto);
						}

						@Override
						public PartnerDto getServicePartnerDto() {
							return getInternalFrameKunde().getKundeDto().getPartnerRechnungsadresseDto();
						}
					});

			setComponentAt(IDX_PANE_RECHNUNGSADRESSE, panelREadresseBottomD6);
		}
	}

	private void refreshBankverbindung(Integer iIdPartnerI) throws Throwable {

		if (panelBankTopQP == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance().createFKPartnerbank(iIdPartnerI);

			panelBankTopQP = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_PARTNERBANK,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("finanz.bankverbindung"),
					true);

			panelBankBottomD = new PanelKundepartnerbank(getInternalFrame(),
					LPMain.getTextRespectUISPr("finanz.bankverbindung"), null);

			panelBankSP = new PanelSplit(getInternalFrame(), panelBankBottomD, panelBankTopQP, 200);
			setComponentAt(IDX_PANE_BANKVERBINDUNG, panelBankSP);
		} else {
			// filter refreshen.
			panelBankTopQP.setDefaultFilter(PartnerFilterFactory.getInstance().createFKPartnerbank(iIdPartnerI));
		}
	}

	private void refreshKontakt(Integer iIdPartnerI) throws Throwable {

		if (panelSplitKontakt == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance().createFKKontakt(iIdPartnerI);
			;

			panelQueryKontakt = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_KONTAKT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("lp.kontakt"), true);

			panelDetailKontakt = new PanelKundeKontakt(getInternalFrame(), LPMain.getTextRespectUISPr("lp.kontakt"),
					null);

			panelSplitKontakt = new PanelSplit(getInternalFrame(), panelDetailKontakt, panelQueryKontakt, 150);

			setComponentAt(IDX_PANEL_KONTAKT, panelSplitKontakt);
		} else {
			// filter refreshen.
			panelQueryKontakt.setDefaultFilter(PartnerFilterFactory.getInstance().createFKKontakt(iIdPartnerI));
		}
	}

	private void refreshKundeQP1() throws Throwable {

		if (panelKundeQP1 == null) {
			// welche buttons brauchen wir?
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER };

			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_KUNDENAUSWAHL_STRUKTURIERT);
			boolean b = (java.lang.Boolean) parameter.getCWertAsObject();

			if (b == true) {

				panelKundeQP1 = new PanelQuery(PartnerFilterFactory.getInstance().createQTKundeABCDebitoren(),
						PartnerFilterFactory.getInstance().createFKKundeMandantPartner(),
						QueryParameters.UC_ID_KUNDE_STRUKTUR, aWhichButtonIUse, getInternalFrame(),
						LPMain.getTextRespectUISPr("lp.auswahl"), true,
						PartnerFilterFactory.getInstance().createFKVKunde(), null);
			} else {
				panelKundeQP1 = new PanelQuery(PartnerFilterFactory.getInstance().createQTKundeABCDebitoren(),
						PartnerFilterFactory.getInstance().createFKKundeMandantPartner(), QueryParameters.UC_ID_KUNDE2,
						aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("lp.auswahl"), true,
						PartnerFilterFactory.getInstance().createFKVKunde(), null);
			}

			panelKundeQP1.setFilterComboBox(PartnerFilterFactory.getInstance().getMapKundeInteressent(),
					new FilterKriterium("KUNDE_INTERESSENT", true, "" + "", FilterKriterium.OPERATOR_EQUAL, false),
					true);

			panelKundeQP1
					.setFilterComboBox2(DelegateFactory.getInstance().getPartnerDelegate().getAllBranche(),
							new FilterKriterium("flrpartner.branche_i_id", true, "" + "",
									FilterKriterium.OPERATOR_EQUAL, false),
							false, LPMain.getTextRespectUISPr("lp.alle"), true);

			parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_DEFAULT_KUNDENAUSWAHL);
			Integer iAuswahl = (java.lang.Integer) parameter.getCWertAsObject();

			panelKundeQP1.setKeyOfFilterComboBox(iAuswahl);

			// Hier den zusaetzlichen Button aufs Panel bringen
			panelKundeQP1.createAndSaveAndShowButton("/com/lp/client/res/businessmen16x16.png",
					LPMain.getTextRespectUISPr("part.tooltip.new_kunde_from_partner"),
					PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_PARTNER, null);
			// PJ21278
			// panelKundeQP1.createAndSaveAndShowButton("/com/lp/client/res/branch_view.png",
			// LPMain.getTextRespectUISPr("stkl.baumansicht"), MENU_KUNDE_BAUMSICHT, null);

			panelKundeQP1.addDirektFilter(PartnerFilterFactory.getInstance()
					.createFKDKundePartnerName(LPMain.getTextRespectUISPr("lp.firma")));
			panelKundeQP1.addDirektFilter(PartnerFilterFactory.getInstance().createFKDLandPLZOrt());
			panelKundeQP1.addDirektFilter(PartnerFilterFactory.getInstance().createFKDPartnerErweiterteSuche());

			parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
			boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter.getCWertAsObject();

			if (!bSuchenInklusiveKbez) {

				panelKundeQP1
						.addDirektFilter(PartnerFilterFactory.getInstance().createFKDKundePartnerKurzbezeichnung());
			}

			parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_KUNDEN,
					ParameterFac.PARAMETER_KUNDE_MIT_NUMMER);

			int bKundeMitNummer = (java.lang.Integer) parameter.getCWertAsObject();

			if (bKundeMitNummer > 0) {
				panelKundeQP1.addDirektFilter(PartnerFilterFactory.getInstance().createFKDKundeKundennummer());
			} else
				panelKundeQP1.addDirektFilter(PartnerFilterFactory.getInstance().createFKDDebitorennummer());

			mapButton = new WrapperMapButton(kundeDto.getPartnerDto());
			panelKundeQP1.addButtonToToolpanel(mapButton);

			setComponentAt(IDX_PANE_KUNDE, panelKundeQP1);
		}
	}

	/*
	 * private void refreshLieferstatistikQP6() throws Throwable {
	 * 
	 * if (panelLieferstatistikQP6 == null) { //welche buttons brauchen wir?
	 * String[] aWhichButtonIUse = { PanelBasis.ACTION_PRINT};
	 * 
	 * panelLieferstatistikQP6 = new PanelQuery( null, null,
	 * QueryParameters.UC_ID_KUNDE_LIEFERSTATISTIK, aWhichButtonIUse, this, "",
	 * true); setComponentAt(IDX_PANE_LIEFERSTATISTIK_QP7, panelLieferstatistikQP6);
	 * } if (kundeDto != null) { panelLieferstatistikQP6
	 * .setDefaultFilter(PartnerFilterFactory.getInstance().
	 * createFKKundeLieferstatistik(kundeDto. getIId())); } }
	 */

	private void refreshKundesokoSP10(Object key) throws Exception, Throwable {
		if (panelKundesokoSP10 == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance().createFKKundesoko((Integer) key);

			panelKundesokoQP10 = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_KUNDESOKO,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("part.kundesoko"), true);
			panelKundesokoQP10.setMultipleRowSelectionEnabled(true);
			panelKundesokoQP10.createAndSaveAndShowButton("/com/lp/client/res/document_into.png",
					LPMain.getTextRespectUISPr("part.kundesoko.xlsimport"), BUTTON_IMPORT_KUNDESOKO, null);

			panelKundesokoD10 = new PanelKundesoko(getInternalFrame(), LPMain.getTextRespectUISPr("part.kundesoko"),
					null);

			panelKundesokoSP10 = new PanelSplit(getInternalFrame(), panelKundesokoD10, panelKundesokoQP10, 140);

			panelKundesokoQP10.befuellePanelFilterkriterienDirekt(
					new FilterKriteriumDirekt("flrartikel.c_nr", "", FilterKriterium.OPERATOR_LIKE,
							LPMain.getTextRespectUISPr("kundesoko.filter.artikelnummerartikelgruppe"),
							FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
																	// als
																	// '%XX'
							true, // wrapWithSingleQuotes
							true, Facade.MAX_UNBESCHRAENKT),
					new FilterKriteriumDirekt("c_kundeartikelnummer", "", FilterKriterium.OPERATOR_LIKE,
							LPMain.getTextRespectUISPr("kunde.soko.kundeartikelnummer"),
							FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
																	// als
																	// '%XX'
							true, // wrapWithSingleQuotes
							true, Facade.MAX_UNBESCHRAENKT));

			setComponentAt(IDX_PANE_KUNDESOKO, panelKundesokoSP10);
		} else {
			// filter refreshen.
			panelKundesokoQP10.setDefaultFilter(PartnerFilterFactory.getInstance().createFKKundesoko((Integer) key));
		}
	}

	private void refreshKundesokomengenstaffelSP11(Object key) throws Exception, Throwable {
		if (panelKundesokomengenstaffelSP11 == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance()
					.createFKKundesokomengenstaffel((Integer) key);

			panelKundesokomengenstaffelQP11 = new PanelQuery(querytypes, filters,
					QueryParameters.UC_ID_KUNDESOKOMENGENSTAFFEL, aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("part.kundesokomengenstaffel"), true);

			panelKundesokomengenstaffelD11 = new PanelKundesokomengenstaffel(getInternalFrame(),
					LPMain.getTextRespectUISPr("part.kundesokomengenstaffel"), null);

			panelKundesokomengenstaffelSP11 = new PanelSplit(getInternalFrame(), panelKundesokomengenstaffelD11,
					panelKundesokomengenstaffelQP11, 180);

			setComponentAt(IDX_PANE_KUNDESOKOMENGENSTAFFEL, panelKundesokomengenstaffelSP11);
		} else {
			// filter refreshen.
			panelKundesokomengenstaffelQP11
					.setDefaultFilter(PartnerFilterFactory.getInstance().createFKKundesokomengenstaffel((Integer) key));
		}
	}

	private InternalFrameKunde getInternalFrameKunde() {
		return ((InternalFrameKunde) getInternalFrame());
	}

	private void dialogPartner(ItemChangedEvent e) throws Throwable {
		panelPartner = PartnerFilterFactory.getInstance().createPanelFLRPartner(getInternalFrame());
		DialogQuery dialogQueryPartner = new DialogQuery(panelPartner);
	}

	public PanelKundesoko getPanelKundesoko() {
		return panelKundesokoD10;
	}

	public PanelQuery getPanelKundeQP1() {
		this.setSelectedIndex(IDX_PANE_KUNDE);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.panelKundeQP1;
	}

	public PanelQuery getOnlyPanelKundeQP1() {
		return this.panelKundeQP1;
	}

	public PanelQuery getPanelKundesokomengenstaffelQP11() {
		return panelKundesokomengenstaffelQP11;
	}

	private PanelDialogKriterien getPanelKriterienUmsatz() throws Throwable {
		FilterKriterium fkKD = null;
		fkKD = PartnerFilterFactory.getInstance().createFKKriteriumIId(getInternalFrameKunde().getKundeDto().getIId());
		if (panelKriterienUebersicht == null) {
			panelKriterienUebersicht = new PanelDialogKriterienUmsatzKD(getInternalFrameKunde(),
					LPMain.getTextRespectUISPr("lp.kriterienumsatzuebersicht"), fkKD);
		}
		panelKriterienUebersicht.setAdd2Title(LPMain.getTextRespectUISPr("lp.kriterienumsatzuebersicht") + " "
				+ getInternalFrameKunde().getKundeDto().getPartnerDto().formatAnrede());
		panelKriterienUebersicht.setFkKDLF(fkKD);
		return panelKriterienUebersicht;
	}

	@Override
	public void setServicePartnerDto(PartnerDto partnerDto) {
		getInternalFrameKunde().getKundeDto().setPartnerDto(partnerDto);

	}

	@Override
	public PartnerDto getServicePartnerDto() {
		return getInternalFrameKunde().getKundeDto().getPartnerDto();
	}

	private boolean hatZusatzfunkton4Vending() {
		if (bZusatzfunktion4Vending == null) {
			bZusatzfunktion4Vending = LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_4VENDING_SCHNITTSTELLE);
		}
		return bZusatzfunktion4Vending;
	}

	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfBelegPosI) throws Throwable {

	}

	public void copyHV() throws ExceptionLP, Throwable {
		Object iids[] = this.panelKundesokoQP10.getSelectedIds();

		if (iids != null && (iids.length > 0)) {

			KundesokoDto[] dtos = new KundesokoDto[iids.length];
			Integer aIIdPosition[] = new Integer[iids.length];
			for (int i = 0; i < aIIdPosition.length; i++) {
				aIIdPosition[i] = (Integer) iids[i];
				dtos[i] = DelegateFactory.getInstance().getKundesokoDelegate()
						.kundesokoFindByPrimaryKey((Integer) aIIdPosition[i]);
			}

			LPMain.getPasteBuffer().writeObjectToPasteBuffer(dtos);

		}

	}

	public void einfuegenHV() throws IOException, ExceptionLP, ParserConfigurationException, Throwable, SAXException {

		Object o = LPMain.getPasteBuffer().readObjectFromPasteBuffer();

		if (o instanceof KundesokoDto[]) {

			KundesokoDto[] dtos = (KundesokoDto[]) o;
			if (dtos != null && dtos.length > 0) {

				Integer kundeIIdAlt = dtos[0].getKundeIId();

				for (int i = 0; i < dtos.length; i++) {
					KundesokoDto sokoDto = dtos[i];
					sokoDto.setKundeIId(getKundeDto().getIId());

					KundesokomengenstaffelDto[] staffelnDto = DelegateFactory.getInstance().getKundesokoDelegate()
							.kundesokomengenstaffelFindByKundesokoIIdInZielWaehrung(sokoDto.getIId(),
									new java.sql.Date(System.currentTimeMillis()), getKundeDto().getCWaehrung());

					for (int j = 0; j < staffelnDto.length; j++) {
						try {
							DelegateFactory.getInstance().getKundesokoDelegate().createKundesoko(dtos[i],
									staffelnDto[j]);
						} catch (ExceptionLP e) {

							if (e.getICode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
								// Wenn es zu einen Artikel/Artgru bereits eine Soko/Staffel gibt -> Auslassen
							} else {
								throw e;
							}

						}
					}
				}

			}

		}
	}

}
