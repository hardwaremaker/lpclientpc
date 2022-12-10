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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.angebotstkl.webabfrage.WebabfrageAssistentController;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.DialogNeueArtikelnummer;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
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
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.open.CsvFile;
import com.lp.client.frame.filechooser.open.DirectoryFile;
import com.lp.client.frame.filechooser.open.FileOpenerFactory;
import com.lp.client.frame.filechooser.open.XlsFileOpener;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.importassistent.StklImportController;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.editor.LpEditor;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.AngebotstklreportFac;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.angebotstkl.service.EkaglieferantDto;
import com.lp.server.angebotstkl.service.PositionlieferantDto;
import com.lp.server.angebotstkl.service.WebabfragepositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.service.POSDocument2POSDto;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.csv.LPCSVReader;

@SuppressWarnings("static-access")
public class TabbedPaneEinkaufsangebot extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryEinkaufsangebot = null;
	private PanelBasis panelDetailEinkaufsangebot = null;

	private PanelQuery panelQueryEinkaufsangebotpositionen = null;
	private PanelEinkaufsangebotpositionen panelDetailEinkaufsangebotpositionen = null;
	private PanelSplit panelSplitEinkaufsangebotpositionen = null; // FLR 1:n

	private PanelBasis panelDetailEinkaufsangebotKommentar = null;

	private PanelBasis panelLieferantenOptimieren = null;

	private PanelQuery panelQueryWeblieferant = null;
	private PanelEkweblieferant panelDetailWeblieferant = null;
	private PanelSplit panelSplitWeblieferant = null; // FLR 1:n

	private PanelQuery panelQueryEkagLieferant = null;
	private PanelEkaglieferant panelDetailEkaglieferant = null;
	private PanelSplit panelSplitEkaglieferant = null; // FLR 1:n

	private PanelQuery panelQueryPositionlieferant = null;
	private PanelPositionlieferant panelDetailPositionlieferant = null;
	private PanelSplit panelSplitPositionlieferant = null; // FLR 1:n

	private PanelQuery panelQueryPositionlieferantVergleich = null;
	private PanelPositionlieferantVergleich panelDetailPositionlieferantVergleich = null;
	private PanelSplit panelSplitPositionlieferantVergleich = null; // FLR 1:n

	private PanelQueryFLR panelQueryFLREkgruppe = null;

	private PanelQueryFLR panelQueryFLREkagPositionFuerPositionlieferant = null;

	private static final String ACTION_SPECIAL_VERDICHTEN = PanelBasis.ACTION_MY_OWN_NEW + "action_special_verdichten";

	private static final String ACTION_SPECIAL_CSVIMPORT_POSITIONEN = "ACTION_SPECIAL_CSVIMPORT_POSITIONEN";
	private static final String ACTION_SPECIAL_WEBABFRAGE = "action_special_webabfrage";

	private final String BUTTON_IMPORTCSV_EKAGSTKLPOSITIONEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_CSVIMPORT_POSITIONEN;
	private final String BUTTON_WEBABFRAGE = PanelBasis.LEAVEALONE + ACTION_SPECIAL_WEBABFRAGE;

	private static final String ACTION_SPECIAL_EKGRUPPE_HINZUFUEGEN = "ACTION_SPECIAL_EKGRUPPE_HINZUFUEGEN";
	private final String BUTTON_EKGRUPPE_HINZUFUEGEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_EKGRUPPE_HINZUFUEGEN;

	private static final String ACTION_SPECIAL_XLS_VERSENDEN = "ACTION_SPECIAL_XLS_VERSENDEN";
	private final String BUTTON_XLS_ERZEUGEN_UND_PER_MAIL_VERSENDEN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_XLS_VERSENDEN;

	private static final String ACTION_SPECIAL_XLS_ERZEUGEN = "ACTION_SPECIAL_XLS_ERZEUGEN";
	private final String BUTTON_XLS_ERZEUGEN = PanelBasis.ACTION_MY_OWN_NEW + ACTION_SPECIAL_XLS_ERZEUGEN;

	private static final String ACTION_SPECIAL_XLS_EINLESEN = "ACTION_SPECIAL_XLS_EINLESEN";
	private final String BUTTON_XLS_EINLESEN = PanelBasis.ACTION_MY_OWN_NEW + ACTION_SPECIAL_XLS_EINLESEN;

	public static int IDX_PANEL_AUSWAHL = -1;
	public static int IDX_PANEL_KOPFDATEN = -1;
	public static int IDX_PANEL_EKAGLIEFERANT = -1;
	public static int IDX_PANEL_POSITIONEN_LF = -1;
	public static int IDX_PANEL_POSITIONEN_LF_VERGLEICH = -1;
	public static int IDX_PANEL_EKAGLIEFERANT_OPTIMIEREN = -1;
	public static int IDX_PANEL_POSITIONEN = -1;
	public static int IDX_PANEL_KOMMENTAR = -1;
	public static int IDX_PANEL_WEBLIEFERANT = -1;

	private final String MENU_DATEI_DRUCKEN = "MENU_DATEI_DRUCKEN";
	private final String MENU_DATEI_VERGLEICH = "MENU_DATEI_VERGLEICH";
	private final String MENU_BEARBEITEN_HANDARTIKEL_UMWANDELN = "MENU_BEARBEITEN_HANDARTIKEL_UMWANDELN";
	private final String MENU_BEARBEITEN_HANDARTIKEL_IN_BESTEHENDEN_ARTIKEL_UMANDELN = "MENU_BEARBEITEN_HANDARTIKEL_IN_BESTEHENDEN_ARTIKEL_UMANDELN";

	private PanelQueryFLR panelQueryFLRArtikel_handartikelumwandeln = null;

	private WrapperMenuBar wrapperMenuBar = null;

	private String letzterPfad = null;

	public TabbedPaneEinkaufsangebot(InternalFrame internalFrameI) throws Throwable {

		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr("lp.einkaufsangebot"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryEinkaufsangebot() {
		return panelQueryEinkaufsangebot;
	}

	public PanelQuery getPanelQueryEinkaufsangebotpositionen() {
		return panelQueryEinkaufsangebotpositionen;
	}

	public PanelQuery getPanelQueryEkaglieferant() {
		return panelQueryEkagLieferant;
	}

	public PanelQuery getPanelQueryPositionlieferant() {
		return panelQueryPositionlieferant;
	}

	public PanelQuery getPanelQueryPositionlieferantVergleich() {
		return panelQueryPositionlieferantVergleich;
	}

	public PanelBasis getAngebotstklPositionenBottom() {
		return panelDetailEinkaufsangebotpositionen;
	}

	public PanelQuery getAngebotstklPositionenTop() {
		return panelQueryEinkaufsangebotpositionen;
	}

	private void refreshAuswahl() throws Throwable {
		if (panelQueryEinkaufsangebot == null) {

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER };
			panelQueryEinkaufsangebot = new PanelQuery(AngebotstklFilterFactory.getInstance().createQTAgstkl(),
					SystemFilterFactory.getInstance().createFKMandantCNr(), QueryParameters.UC_ID_EINKAUFSANGEBOT,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("auft.title.panel.auswahl"), true);

			panelQueryEinkaufsangebot.befuellePanelFilterkriterienDirekt(
					AngebotstklFilterFactory.getInstance().createFKDEinakufsangebotbelegnumer(),
					AngebotstklFilterFactory.getInstance().createFKDEinkaufsangebotKundeName());

			panelQueryEinkaufsangebot
					.addDirektFilter(AngebotstklFilterFactory.getInstance().createFKDEinkaufsangbotprojekt());

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryEinkaufsangebot);
		}
	}

	private void createKopfdaten(Integer key) throws Throwable {
		if (panelDetailEinkaufsangebot == null) {
			panelDetailEinkaufsangebot = new PanelEinkaufsangebot(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"), key);
			setComponentAt(IDX_PANEL_KOPFDATEN, panelDetailEinkaufsangebot);
		}
	}

	private void createLeferantenOptimieren(Integer key) throws Throwable {
		if (panelLieferantenOptimieren == null) {
			panelLieferantenOptimieren = new PanelEkaglieferantOptimieren(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren"), key);
			setComponentAt(IDX_PANEL_EKAGLIEFERANT_OPTIMIEREN, panelLieferantenOptimieren);
		}
	}

	private void createKommentar(Integer key) throws Throwable {
		if (panelDetailEinkaufsangebotKommentar == null) {
			panelDetailEinkaufsangebotKommentar = new PanelEinkaufsangebotpositionenKommentare(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.kommentar"), key);
			setComponentAt(IDX_PANEL_KOMMENTAR, panelDetailEinkaufsangebotKommentar);
		}
	}

	private PanelBasis getEinkaufsangebotKopfdaten() throws Throwable {
		Integer iIdAngebot = null;

		if (panelDetailEinkaufsangebot == null) {

			// Die Angebot hat einen Key vom Typ Integer
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdAngebot = new Integer(Integer.parseInt(getInternalFrame().getKeyWasForLockMe()));
			}

			panelDetailEinkaufsangebot = new PanelEinkaufsangebot(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("angb.panel.kopfdaten"), iIdAngebot); // empty bei leerer
																									// angebotsliste

			setComponentAt(IDX_PANEL_KOPFDATEN, panelDetailEinkaufsangebot);
		}

		return panelDetailEinkaufsangebot;
	}

	public void printEinkaufsangebot() throws Throwable {
		if (!getEinkaufsangebotKopfdaten().isLockedDlg()) {
			if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {
				// das Angebot drucken
				getInternalFrame().showReportKriterien(new ReportEinkaufsangebot(getInternalFrameAngebotstkl(),
						getInternalFrameAngebotstkl().getEinkaufsangebotDto().getCNr(),
						getInternalFrameAngebotstkl().getEinkaufsangebotDto()));
			} else {
				// es ist keine Angbotsstueckliste ausgewaehlt
				DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
						LPMain.getInstance().getTextRespectUISPr("as.warning.keineinkaufsangebot"));
			}
		}
	}

	public void printVergleich() throws Throwable {
		if (!getEinkaufsangebotKopfdaten().isLockedDlg()) {
			if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {
				// das Angebot drucken
				getInternalFrame().showReportKriterien(new ReportVergleich(getInternalFrameAngebotstkl(),
						getInternalFrameAngebotstkl().getEinkaufsangebotDto().getCNr(),
						getInternalFrameAngebotstkl().getEinkaufsangebotDto().getIId()));
			} else {
				// es ist keine Angbotsstueckliste ausgewaehlt
				DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
						LPMain.getInstance().getTextRespectUISPr("as.warning.keineinkaufsangebot"));
			}
		}
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {

		IDX_PANEL_AUSWAHL = 

		reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.auswahl"));

		
		IDX_PANEL_KOPFDATEN = 

		reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"), null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"));

		
		IDX_PANEL_POSITIONEN = 

		reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.positionen"), null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.positionen"));

		
		IDX_PANEL_KOMMENTAR = 

		reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.kommentar"), null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.kommentar"));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_WEB_BAUTEIL_ANFRAGE)) {

			
			IDX_PANEL_WEBLIEFERANT = 
			reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("agstkl.weblieferant"), null, null,
					LPMain.getInstance().getTextRespectUISPr("agstkl.weblieferant"));
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_EK_ANGEBOT_ANFRAGE)) {

			
			IDX_PANEL_EKAGLIEFERANT = 

			reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("agstkl.ekgruppelieferant"), null, null,
					LPMain.getInstance().getTextRespectUISPr("agstkl.ekgruppelieferant"));

			
			IDX_PANEL_POSITIONEN_LF = 

			reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("agstkl.lieferant.positionenlf"), null, null,
					LPMain.getInstance().getTextRespectUISPr("agstkl.lieferant.positionenlf"));
			
			IDX_PANEL_POSITIONEN_LF_VERGLEICH = 

			reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("agstkl.lieferant.positionenlfvergleich"), null, null,
					LPMain.getInstance().getTextRespectUISPr("agstkl.lieferant.positionenlfvergleich"));
			
			IDX_PANEL_EKAGLIEFERANT_OPTIMIEREN = 

			reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren"), null,
					null, LPMain.getInstance().getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren"));

		}

		refreshAuswahl();

		panelQueryEinkaufsangebot.eventYouAreSelected(false);

		if ((Integer) panelQueryEinkaufsangebot.getSelectedId() != null) {
			getInternalFrameAngebotstkl().setEinkaufsangebotDto(DelegateFactory.getInstance().getAngebotstklDelegate()
					.einkaufsangebotFindByPrimaryKey((Integer) panelQueryEinkaufsangebot.getSelectedId()));
		}

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryEinkaufsangebot, ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryEinkaufsangebot.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() throws Throwable {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("lp.einkaufsangebot"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

		if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {
			String sBezeichnung = "";
			if (getInternalFrameAngebotstkl().getEinkaufsangebotDto().getCProjekt() != null) {
				sBezeichnung = getInternalFrameAngebotstkl().getEinkaufsangebotDto().getCProjekt();
			}

			if (getSelectedIndex() > IDX_PANEL_EKAGLIEFERANT && panelQueryEkagLieferant != null) {
				if (panelQueryEkagLieferant.getSelectedId() != null) {

					EkaglieferantDto ekaglieferantDto = DelegateFactory.getInstance().getAngebotstklDelegate()
							.ekaglieferantFindByPrimaryKey((Integer) panelQueryEkagLieferant.getSelectedId());
					LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
							.lieferantFindByPrimaryKey(ekaglieferantDto.getLieferantIId());

					sBezeichnung += " " + LPMain.getInstance().getTextRespectUISPr("agstkl.ekgruppelieferant") + ": "
							+ lfDto.getPartnerDto().formatFixName1Name2();

				}
			}

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameAngebotstkl().getEinkaufsangebotDto().getCNr() + ", " + sBezeichnung);

		}
	}

	public InternalFrameAngebotstkl getInternalFrameAngebotstkl() {
		return (InternalFrameAngebotstkl) getInternalFrame();
	}

	public EinkaufsangebotDto getEinkaufsangebotDto() {
		return getInternalFrameAngebotstkl().getEinkaufsangebotDto();
	}

	private void wandleHandartikelInArtikelUm() throws Throwable {
		if (panelQueryEinkaufsangebotpositionen == null
				|| panelQueryEinkaufsangebotpositionen.getSelectedId() == null) {
			return;
		}

		EinkaufsangebotpositionDto einkaufsangebotpositionDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.einkaufsangebotpositionFindByPrimaryKey((Integer) panelQueryEinkaufsangebotpositionen.getSelectedId());

		if (einkaufsangebotpositionDto == null || !AngebotstklFac.POSITIONSART_AGSTKL_HANDEINGABE
				.equals(einkaufsangebotpositionDto.getPositionsartCNr())) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
					LPMain.getTextRespectUISPr("einkaufsangebot.bearbeiten.handartikelumwandeln.error"));
			return;
		}

		ArtikelDto artikelDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.findeArtikelZuEinkaufsangebotpositionHandeingabe(
						(Integer) panelQueryEinkaufsangebotpositionen.getSelectedId());
		DialogNeueArtikelnummer dialog = new DialogNeueArtikelnummer(getInternalFrame());
		dialog.setArtikelVorschlag(artikelDto);

		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(dialog);
		dialog.setVisible(true);

		if (dialog.getArtikelnummerNeu() != null) {
			Integer posIId = DelegateFactory.getInstance().getAngebotstklDelegate()
					.einkaufsangebotWandleHandartikelUmUndFasseZusammen(
							(Integer) panelQueryEinkaufsangebotpositionen.getSelectedId(),
							dialog.getArtikelnummerNeu());
			panelSplitEinkaufsangebotpositionen.eventYouAreSelected(false);
			panelQueryEinkaufsangebotpositionen.setSelectedId(posIId);
		}
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_DATEI_DRUCKEN)) {
			printEinkaufsangebot();
		} else if (e.getActionCommand().equals(MENU_DATEI_VERGLEICH)) {
			printVergleich();
		} else if (MENU_BEARBEITEN_HANDARTIKEL_UMWANDELN.equals(e.getActionCommand())) {
			wandleHandartikelInArtikelUm();
		} else if (e.getActionCommand().equals(MENU_BEARBEITEN_HANDARTIKEL_IN_BESTEHENDEN_ARTIKEL_UMANDELN)) {
			if (panelQueryEinkaufsangebotpositionen != null
					&& panelQueryEinkaufsangebotpositionen.getSelectedId() != null) {

				EinkaufsangebotpositionDto einkaufsangebotpositionDto = DelegateFactory.getInstance()
						.getAngebotstklDelegate().einkaufsangebotpositionFindByPrimaryKey(
								(Integer) panelQueryEinkaufsangebotpositionen.getSelectedId());

				ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(einkaufsangebotpositionDto.getArtikelIId());
				if (aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {

					panelQueryFLRArtikel_handartikelumwandeln = ArtikelFilterFactory.getInstance()
							.createPanelFLRArtikel(getInternalFrame(), false);
					new DialogQuery(panelQueryFLRArtikel_handartikelumwandeln);
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
							LPMain.getTextRespectUISPr("ekag.bearbeiten.handartikelinbestehendenumwandeln.error"));
				}

				panelSplitEinkaufsangebotpositionen.eventYouAreSelected(false);
				panelQueryEinkaufsangebotpositionen.setSelectedId(einkaufsangebotpositionDto.getIId());
			}
		}

	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelQueryEinkaufsangebot) {
				refreshAngebotstklPositionen();
				setSelectedComponent(panelSplitEinkaufsangebotpositionen);
				// panelDetailAgstkl.eventYouAreSelected(false);
			} else if (eI.getSource() == panelQueryFLREkgruppe) {

				Integer ekgruppeIId = (Integer) panelQueryFLREkgruppe.getSelectedId();

				DelegateFactory.getInstance().getAngebotstklDelegate()
						.kopiereEkgruppeInEkaglieferant(getEinkaufsangebotDto().getIId(), ekgruppeIId);
				panelSplitEkaglieferant.eventYouAreSelected(false);
			} else if (eI.getSource() == panelQueryFLREkagPositionFuerPositionlieferant) {

				Integer ekagposIId = (Integer) panelQueryFLREkagPositionFuerPositionlieferant.getSelectedId();

				panelDetailPositionlieferant.eventActionNew(eI, true, false, ekagposIId);
				panelDetailPositionlieferant.eventYouAreSelected(false);
				setSelectedComponent(panelSplitPositionlieferant); // ui

			} else if (eI.getSource() == panelQueryFLRArtikel_handartikelumwandeln) {
				Integer artikelIId = (Integer) ((ISourceEvent) eI.getSource()).getIdSelected();

				if (artikelIId != null) {
					ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByPrimaryKey(artikelIId);
					artikelDto = DelegateFactory.getInstance().getArtikelkommentarDelegate().pruefeArtikel(artikelDto,
							LocaleFac.BELEGART_AUFTRAG, getInternalFrame());

					if (artikelDto != null) {
						ArrayList<String> meldungen = DelegateFactory.getInstance().getArtikelDelegate()
								.wandleHandeingabeInBestehendenArtikelUm(
										(Integer) panelQueryEinkaufsangebotpositionen.getSelectedId(),
										ArtikelFac.HANDARTIKEL_UMWANDELN_EINKAUFSANGEBOTPOSITION, artikelDto.getIId());
						panelQueryEinkaufsangebotpositionen.eventYouAreSelected(false);

						for (int i = 0; i < meldungen.size(); i++) {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"), meldungen.get(i));
						}

					}
				}
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();
			if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				if (eI.getSource().equals(panelQueryEinkaufsangebotpositionen)) {
					einfuegenHV();
				}
			}

			else if (sAspectInfo.equals(PanelBasis.ACTION_KOPIEREN)) {
				if (eI.getSource().equals(panelQueryEinkaufsangebotpositionen)) {
					copyHV();
				}
			} else if (sAspectInfo.equals(BUTTON_EKGRUPPE_HINZUFUEGEN)) {

				String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

				panelQueryFLREkgruppe = new PanelQueryFLR(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
						QueryParameters.UC_ID_EKGRUPPE, aWhichButtonIUse, getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr("agstkl.ekgruppe"));

				new DialogQuery(panelQueryFLREkgruppe);

			} else if (sAspectInfo.equals(ACTION_SPECIAL_VERDICHTEN)) {

				EkaglieferantDto[] ekags = DelegateFactory.getInstance().getAngebotstklDelegate()
						.ekaglieferantFindByEinkaufsangebotIId(getEinkaufsangebotDto().getIId());

				if (ekags.length == 0) {

					DelegateFactory.getInstance().getAngebotstklDelegate()
							.verdichteEinkaufsangebotPositionen(getEinkaufsangebotDto().getIId());
					panelSplitEinkaufsangebotpositionen.eventYouAreSelected(false);
				} else {
					DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr("agstkl.einkaufsangebot.positionen.verdichten.error"));
				}
			} else if (sAspectInfo.equals(BUTTON_XLS_EINLESEN)) {

				if (panelQueryEkagLieferant.getSelectedId() != null) {
					XlsFileOpener xlsOpener = FileOpenerFactory
							.einkaufsangebotLieferantImportOpenXls(getInternalFrame());
					xlsOpener.doOpenDialog();

					if (!xlsOpener.hasFileChosen())
						return;

					EkaglieferantDto ekaglieferantDto = DelegateFactory.getInstance().getAngebotstklDelegate()
							.ekaglieferantFindByPrimaryKey((Integer) panelQueryEkagLieferant.getSelectedId());

					DelegateFactory.getInstance().getAngebotstklDelegate()
							.leseXLSEinesLieferantenEin(ekaglieferantDto.getIId(), xlsOpener.getFile().getBytes());
					panelSplitEkaglieferant.eventYouAreSelected(false);
				} else {

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("agstkl.ekaglieferant.import.error.keinlieferantselektiert"));

				}
			} else if (sAspectInfo.equals(BUTTON_XLS_ERZEUGEN_UND_PER_MAIL_VERSENDEN)) {

				int indexAlle = 0;
				int indexMarkierte = 1;
				int iAnzahlOptionen = 2;

				Object[] aOptionenVerdichten = new Object[iAnzahlOptionen];

				aOptionenVerdichten[indexAlle] = LPMain.getTextRespectUISPr("agstkl.lieferanten.xls.erzeugen.alle");
				aOptionenVerdichten[indexMarkierte] = LPMain
						.getTextRespectUISPr("agstkl.lieferanten.xls.erzeugen.markierte");

				JLabel lDatum = new JLabel(LPMain.getTextRespectUISPr("agstkl.lieferanten.xls.erzeugen.datumbis"));

				WrapperDateField wdf = new WrapperDateField();
				wdf.setMandatoryField(true);

				java.sql.Date datumbis = Helper.addiereTageZuDatum(new java.sql.Date(System.currentTimeMillis()), 3);
				wdf.setDate(datumbis);

				Object[] params = { LPMain.getTextRespectUISPr("agstkl.lieferanten.xls.erzeugen.frage"), lDatum, wdf };

				int iAuswahl = JOptionPane.showOptionDialog(getInternalFrame(), params,
						LPMain.getTextRespectUISPr("lp.frage"), JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, // Icon
						aOptionenVerdichten, aOptionenVerdichten[0]);

				int indexOK = 0;
				int indexAbbrechen = 1;
				int indexAnzahl = 2;
				
				
				datumbis=wdf.getDate();

				Object[] aOptionen2 = new Object[indexAnzahl];
				aOptionen2[indexOK] = LPMain.getTextRespectUISPr("button.ok");
				aOptionen2[indexAbbrechen] = LPMain.getTextRespectUISPr("lp.abbrechen");

				JLabel lEmailText = new JLabel(LPMain.getTextRespectUISPr("agstkl.lieferanten.xls.erzeugen.mailtext"));
				JLabel lEmailBetreff = new JLabel(LPMain.getTextRespectUISPr("label.betreff") + ":");

				LpEditor wefEmailText = new LpEditor(null);
				WrapperTextField wtfBetreff = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);

				wefEmailText.setMinimumSize(new java.awt.Dimension(550, 250));
				wefEmailText.setPreferredSize(new java.awt.Dimension(550, 250));
				wefEmailText.setMaximumSize(new java.awt.Dimension(550, 250));

				// Mailtext
				com.lp.server.system.service.MailtextDto m = new com.lp.server.system.service.MailtextDto();

				m.setParamXslFile(AngebotstklreportFac.REPORT_EKAGAMIL);
				m.setParamLocale(LPMain.getTheClient().getLocUi());
				m.setParamMandantCNr(LPMain.getTheClient().getMandant());
				m.setParamModul(AngebotstklreportFac.REPORT_MODUL);
				m.setMailBelegnummer(getEinkaufsangebotDto().getCNr());

				m.setXlsmailversand_abgabetermin(datumbis);

				KundeDto kdDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(getEinkaufsangebotDto().getKundeIId());

				m.setXlsmailversand_endkunde(kdDto.getPartnerDto().formatFixName1Name2());
				m.setXlsmailversand_geplanterfertigungstermin(getEinkaufsangebotDto().getTFertigungstermin());
				m.setXlsmailversand_projekt(getEinkaufsangebotDto().getCProjekt());

				String text = DelegateFactory.getInstance().getVersandDelegate().getDefaultTextForBelegEmail(m);

				String betreff = DelegateFactory.getInstance().getVersandDelegate().getDefaultBetreffForBelegEmail(m,
						LocaleFac.BELEGART_EINKAUFSANGEBOT, getEinkaufsangebotDto().getIId());

				wefEmailText.setText(text);
				wtfBetreff.setText(betreff);

				Object[] params2 = { lEmailBetreff, wtfBetreff, new JLabel(" "), lEmailText, wefEmailText };

				int iAuswahl2 = JOptionPane.showOptionDialog(getInternalFrame(), params2,
						LPMain.getTextRespectUISPr("agstkl.lieferanten.xls.erzeugen.mailtext"),
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, // Icon
						aOptionen2, aOptionen2[0]);

				if (iAuswahl2 == indexOK) {
					EkaglieferantDto[] ekagDtos = null;

					if (iAuswahl == indexAlle) {
						ekagDtos = DelegateFactory.getInstance().getAngebotstklDelegate()
								.ekaglieferantFindByEinkaufsangebotIId(getEinkaufsangebotDto().getIId());
					} else if (iAuswahl == indexMarkierte) {
						Object[] ids = panelQueryEkagLieferant.getSelectedIds();

						ekagDtos = new EkaglieferantDto[ids.length];

						for (int i = 0; i < ids.length; i++) {
							ekagDtos[i] = DelegateFactory.getInstance().getAngebotstklDelegate()
									.ekaglieferantFindByPrimaryKey((Integer) ids[i]);
						}
					} else {
						return;
					}

					PersonalDto personalDto_Angemeldet = DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());

					if (personalDto_Angemeldet.getCEmail() == null) {

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								personalDto_Angemeldet.getPartnerDto().formatFixName1Name2() + " / "
										+ personalDto_Angemeldet.getPartnerDto().formatFixName1Name2() + " "
										+ LPMain.getTextRespectUISPr("pers.sonderzeiten.urlaubsantrag.keinabsender"));
						return;
					} else {
						String absender = personalDto_Angemeldet.getCEmail();

						for (int i = 0; i < ekagDtos.length; i++) {

							LinkedHashMap<Integer, byte[]> lhm = DelegateFactory.getInstance().getAngebotstklDelegate()
									.erzeugeXLSFuerLieferanten(getEinkaufsangebotDto().getIId(),
											ekagDtos[i].getLieferantIId());

							Iterator itLf = lhm.keySet().iterator();

							if (itLf.hasNext()) {

								Integer lieferantIId = (Integer) itLf.next();

								LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
										.lieferantFindByPrimaryKey(lieferantIId);

								String email_empfaenger = null;
								if (ekagDtos[i].getAnsprechpartnerIId() != null) {
									AnsprechpartnerDto anspDto = DelegateFactory.getInstance()
											.getAnsprechpartnerDelegate()
											.ansprechpartnerFindByPrimaryKey(ekagDtos[i].getAnsprechpartnerIId());

									email_empfaenger = anspDto.getCEmail();

								}

								if (email_empfaenger == null) {
									email_empfaenger = lfDto.getPartnerDto().getCEmail();
								}

								if (email_empfaenger == null) {

									DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getMessageTextRespectUISPr(
													"agstkl.lieferanten.xls.versand.keinemailadresse",
													lfDto.getPartnerDto().formatFixName1Name2()));

								} else {

									byte[] datenXlsFile = lhm.get(lieferantIId);

									String filename = getEinkaufsangebotDto().getCNr().replaceAll("/", "_") + "_"
											+ lfDto.getIKreditorenkontoAsIntegerNotiId() + ".xls";

									VersandauftragDto versDto = new VersandauftragDto();
									versDto.setCEmpfaenger(email_empfaenger);
									versDto.setCAbsenderadresse(absender);
									String sBetreff = getEinkaufsangebotDto().getCNr();

									versDto.setCDateiname(filename);
									lfDto.getPartnerDto().getLocaleCNrKommunikation();
									versDto.setCBetreff(wtfBetreff.getText());
									versDto.setOInhalt(datenXlsFile);

									versDto.setCText(wefEmailText.getText());

									DelegateFactory.getInstance().getVersandDelegate().updateVersandauftrag(versDto,
											false);

									EkaglieferantDto ekAgDto = DelegateFactory.getInstance().getAngebotstklDelegate()
											.ekaglieferantFindByPrimaryKey(ekagDtos[i].getIId());

									ekAgDto.setTVersand(new java.sql.Timestamp(System.currentTimeMillis()));
									DelegateFactory.getInstance().getAngebotstklDelegate().updateEkaglieferant(ekAgDto);

								}
							}

						}
					}
				}

			} else if (sAspectInfo.equals(BUTTON_XLS_ERZEUGEN)) {

				HvOptional<DirectoryFile> df = HelperClient
						.saveDirectoryNew(this,FileChooserConfigToken.ExportLastDirectory);

				if (df.isPresent()) {
					if (panelQueryEkagLieferant.getSelectedId() != null) {

						EkaglieferantDto ekaglieferantDto = DelegateFactory.getInstance().getAngebotstklDelegate()
								.ekaglieferantFindByPrimaryKey((Integer) panelQueryEkagLieferant.getSelectedId());

						LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
								.lieferantFindByPrimaryKey(ekaglieferantDto.getLieferantIId());

						if (lfDto.getIKreditorenkontoAsIntegerNotiId() == null) {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getMessageTextRespectUISPr(
											"agstkl.lieferanten.xls.erzeugen.errordebitorennummer",
											lfDto.getPartnerDto().formatFixName1Name2()));
							return;
						}

						LinkedHashMap<Integer, byte[]> lhm = DelegateFactory.getInstance().getAngebotstklDelegate()
								.erzeugeXLSFuerLieferanten(getEinkaufsangebotDto().getIId(),
										ekaglieferantDto.getLieferantIId());

						Iterator itLf = lhm.keySet().iterator();

						if (itLf.hasNext()) {

							Integer lieferantIId = (Integer) itLf.next();

							byte[] datenXlsFile = lhm.get(lieferantIId);

							letzterPfad = df.get().getDirectory().getPath();

							MandantDto mandantDto = DelegateFactory.getInstance().getMandantDelegate()
									.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant());

							String filename = mandantDto.getCKbez() + "_Anfrage_"
									+ getEinkaufsangebotDto().getCNr().replaceAll("/", "_") + ".xls";

							try {
								File file = new File(letzterPfad, filename);

								FileOutputStream fo = new FileOutputStream(file);
								fo.write(datenXlsFile);
								fo.close();
							} catch (Exception e) {
								DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), e.getMessage());
							}

						}

					}
				}

			} else if (sAspectInfo.equals(BUTTON_IMPORTCSV_EKAGSTKLPOSITIONEN)) {
				if (LPMain.getInstance().getDesktop().darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_INTELLIGENTER_EINKAUFSAGSTKLIMPORT)) {
					if (LPMain.getInstance().getDesktop()
							.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_KUNDESONDERKONDITIONEN)) {
						AssistentView av = new AssistentView(getInternalFrameAngebotstkl(),
								LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.intelligentereinkaufsangebotimport"),
								new StklImportController(getInternalFrameAngebotstkl().getEinkaufsangebotDto().getIId(),
										StklImportSpezifikation.SpezifikationsTyp.EINKAUFSANGEBOTSSTKL_SPEZ,
										getInternalFrame()));
						getInternalFrame().showPanelDialog(av);
					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
								LPMain.getTextRespectUISPr("stkl.error.keinekundesokoberechtigung"));
					}
				} else {
					// PJ 14984
					HvOptional<CsvFile> csvFile = FileOpenerFactory.einkaufsangebotStklPositionenImportCsv(this);
					if(csvFile.isPresent()) {					
						// Tab-getrenntes File einlesen.
						LPCSVReader reader = new LPCSVReader(new FileReader(csvFile.get().getFile()), (char) KeyEvent.VK_SEMICOLON);

						if (sAspectInfo.equals(BUTTON_IMPORTCSV_EKAGSTKLPOSITIONEN)) {
							// CSV-Format fuer Arbeitsplan:
							// Spalte1: ARTIKELNUMMER
							// Spalte2: MENGE
							// PJ 13479: Damit man Handartikel importieren kann

							// Spalte3: BEZEICHNUNG-WENN HANDARTIKEL
							// Spalte4: EINHEIT-WENN HANDARTIKEL
							// Spalte5: C_POSITION
							// Spalte6: C_KOMMENTAR
							if (panelDetailEinkaufsangebotpositionen.defaultMontageartDto == null) {
								// DefaultMontageart==null
								return;
							}

							String[] sLine;
							int iZeile = 0;
							ArrayList<EinkaufsangebotpositionDto> list = new ArrayList<EinkaufsangebotpositionDto>();
							do {
								// zeilenweise einlesen.
								sLine = reader.readNext();
								iZeile++;
								if (sLine != null) {
									if (sLine.length < 6) {
										DialogFactory.showModalDialog(
												LPMain.getInstance().getTextRespectUISPr("lp.error"),
												"CSV-Datei muss genau 6 Spalten beinhalten.");
										return;
									}

									EinkaufsangebotpositionDto dto = new EinkaufsangebotpositionDto();
									dto.setBelegIId((Integer) panelQueryEinkaufsangebot.getSelectedId());
									dto.setBMitdrucken(Helper.boolean2Short(false));
									dto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
									ArtikelDto artikelDto = null;
									try {
										artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
												.artikelFindByCNr(sLine[0]);
										dto.setArtikelIId(artikelDto.getIId());
										dto.setEinheitCNr(artikelDto.getEinheitCNr());
										dto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
										dto.setCPosition(sLine[4]);
										dto.setCBemerkung(sLine[5]);
										try {
											dto.setNMenge(new BigDecimal(sLine[1]));
											list.add(dto);
										} catch (NumberFormatException ex) {
											DialogFactory.showModalDialog(
													LPMain.getInstance().getTextRespectUISPr("lp.error"),
													"Keine g\u00FCltige Zahl in Zeile " + iZeile
															+ ", Spalte 2. Zeile wird \u00FCbersprungen.");
										}
									} catch (ExceptionLP ex1) {
										if (ex1.getICode() == EJBExceptionLP.FEHLER_BEI_FIND) {

											if (sLine[2] != null && sLine[2].length() > 0) {
												// Handartikel anlegen
												dto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
												dto.setCBez(sLine[2]);
												try {
													dto.setNMenge(new BigDecimal(sLine[1]));
													dto.setCPosition(sLine[4]);
													dto.setCBemerkung(sLine[5]);

													try {
														DelegateFactory.getInstance().getSystemDelegate()
																.einheitFindByPrimaryKey(sLine[3]);
														dto.setEinheitCNr(sLine[3]);
														list.add(dto);
													} catch (ExceptionLP e) {
														if (ex1.getICode() == EJBExceptionLP.FEHLER_BEI_FIND) {
															DialogFactory.showModalDialog(
																	LPMain.getInstance()
																			.getTextRespectUISPr("lp.error"),
																	"Keine g\u00FCltige Einheit " + sLine[3]
																			+ " in Zeile " + iZeile
																			+ ", Spalte 4. Zeile wird \u00FCbersprungen.");
														} else {
															handleException(e, true);
														}
													}

												} catch (NumberFormatException ex) {
													DialogFactory.showModalDialog(
															LPMain.getInstance().getTextRespectUISPr("lp.error"),
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
								EinkaufsangebotpositionDto[] returnArray = new EinkaufsangebotpositionDto[list.size()];
								returnArray = (EinkaufsangebotpositionDto[]) list.toArray(returnArray);
								DelegateFactory.getInstance().getAngebotstklDelegate()
										.createEinkaufsangebotpositions(returnArray);
								panelSplitEinkaufsangebotpositionen.eventYouAreSelected(false);
							}
						}
					}
				}
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelDetailEinkaufsangebot) {
				panelQueryEinkaufsangebot.clearDirektFilter();
				Object oKey = panelDetailEinkaufsangebot.getKeyWhenDetailPanel();

				panelQueryEinkaufsangebot.setSelectedId(oKey);
			} else if (eI.getSource() == panelDetailEinkaufsangebotpositionen) {
				// den Key des Datensatzes merken
				Object oKey = panelDetailEinkaufsangebotpositionen.getKeyWhenDetailPanel();

				// wenn der neue Datensatz wirklich angelegt wurde (Abbruch
				// moeglich durch Pruefung auf Unterpreisigkeit)
				if (!oKey.equals(LPMain.getLockMeForNew())) {
					// die Liste neu aufbauen
					panelQueryEinkaufsangebotpositionen.eventYouAreSelected(false);

					// den Datensatz in der Liste selektieren
					panelQueryEinkaufsangebotpositionen.setSelectedId(oKey);
				}

				// im Detail den selektierten anzeigen
				panelSplitEinkaufsangebotpositionen.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailWeblieferant) {
				// den Key des Datensatzes merken
				Object oKey = panelDetailWeblieferant.getKeyWhenDetailPanel();

				// wenn der neue Datensatz wirklich angelegt wurde (Abbruch
				// moeglich durch Pruefung auf Unterpreisigkeit)
				if (!oKey.equals(LPMain.getLockMeForNew())) {
					// die Liste neu aufbauen
					panelQueryWeblieferant.eventYouAreSelected(false);

					// den Datensatz in der Liste selektieren
					panelQueryWeblieferant.setSelectedId(oKey);
				}

				// im Detail den selektierten anzeigen
				panelSplitWeblieferant.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailEkaglieferant) {
				// den Key des Datensatzes merken
				Object oKey = panelDetailEkaglieferant.getKeyWhenDetailPanel();

				panelQueryEkagLieferant.eventYouAreSelected(false);
				panelQueryEkagLieferant.setSelectedId(oKey);
				panelSplitEkaglieferant.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailPositionlieferant) {
				// den Key des Datensatzes merken
				Object oKey = panelDetailPositionlieferant.getKeyWhenDetailPanel();

				panelQueryPositionlieferant.eventYouAreSelected(false);
				panelQueryPositionlieferant.setSelectedId(oKey);
				panelSplitPositionlieferant.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailPositionlieferantVergleich) {
				// den Key des Datensatzes merken
				Object oKey = panelDetailPositionlieferantVergleich.getKeyWhenDetailPanel();

				panelQueryPositionlieferantVergleich.eventYouAreSelected(false);
				panelQueryPositionlieferantVergleich.setSelectedId(oKey);
				panelSplitPositionlieferantVergleich.eventYouAreSelected(false);
			}
		}
		if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (eI.getSource() == panelQueryEinkaufsangebotpositionen) {
				int iPos = panelQueryEinkaufsangebotpositionen.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryEinkaufsangebotpositionen.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryEinkaufsangebotpositionen.getTable()
							.getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getAngebotstklDelegate()
							.vertauscheEinkausangebotpositionen(iIdPosition, iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryEinkaufsangebotpositionen.setSelectedId(iIdPosition);
				}
			}
			if (eI.getSource() == panelQueryWeblieferant) {
				int iPos = panelQueryWeblieferant.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryWeblieferant.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryWeblieferant.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getAngebotstklDelegate().vertauscheEkweblieferant(iIdPosition,
							iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryWeblieferant.setSelectedId(iIdPosition);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (eI.getSource() == panelQueryEinkaufsangebotpositionen) {
				int iPos = panelQueryEinkaufsangebotpositionen.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryEinkaufsangebotpositionen.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryEinkaufsangebotpositionen.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryEinkaufsangebotpositionen.getTable()
							.getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getAngebotstklDelegate()
							.vertauscheEinkausangebotpositionen(iIdPosition, iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryEinkaufsangebotpositionen.setSelectedId(iIdPosition);
				}
			}
			if (eI.getSource() == panelQueryWeblieferant) {
				int iPos = panelQueryWeblieferant.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryWeblieferant.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryWeblieferant.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryWeblieferant.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getAngebotstklDelegate().vertauscheEkweblieferant(iIdPosition,
							iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryWeblieferant.setSelectedId(iIdPosition);
				}
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (eI.getSource() == panelQueryEinkaufsangebotpositionen) {
				panelDetailEinkaufsangebotpositionen.eventActionNew(eI, true, false);
				panelDetailEinkaufsangebotpositionen.eventYouAreSelected(false); // Buttons
				// schalten
			}
		}

		else if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelQueryEinkaufsangebot) {

				if (panelQueryEinkaufsangebot.getSelectedId() != null) {
					getInternalFrameAngebotstkl().setKeyWasForLockMe(panelQueryEinkaufsangebot.getSelectedId() + "");
					getInternalFrameAngebotstkl().setEinkaufsangebotDto(
							DelegateFactory.getInstance().getAngebotstklDelegate().einkaufsangebotFindByPrimaryKey(
									(Integer) panelQueryEinkaufsangebot.getSelectedId()));

					if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {

						String sBezeichnung = "";
						if (getInternalFrameAngebotstkl().getEinkaufsangebotDto().getCProjekt() != null) {
							sBezeichnung = getInternalFrameAngebotstkl().getEinkaufsangebotDto().getCProjekt();
						}

						getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
								getInternalFrameAngebotstkl().getEinkaufsangebotDto().getCNr() + ", " + sBezeichnung);
					}
				} else {

					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, (false));
					getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");

				}
			} else if (eI.getSource() == panelQueryEinkaufsangebotpositionen) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				panelDetailEinkaufsangebotpositionen.setKeyWhenDetailPanel(key);
				panelDetailEinkaufsangebotpositionen.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelQueryEinkaufsangebotpositionen
						.updateButtons(panelDetailEinkaufsangebotpositionen.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryWeblieferant) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				panelDetailWeblieferant.setKeyWhenDetailPanel(key);
				panelDetailWeblieferant.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelQueryWeblieferant.updateButtons(panelDetailWeblieferant.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryEkagLieferant) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				panelDetailEkaglieferant.setKeyWhenDetailPanel(key);
				panelDetailEkaglieferant.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelQueryEkagLieferant.updateButtons(panelDetailEkaglieferant.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryPositionlieferant) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				panelDetailPositionlieferant.setKeyWhenDetailPanel(key);
				panelDetailPositionlieferant.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelQueryPositionlieferant.updateButtons(panelDetailPositionlieferant.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryPositionlieferantVergleich) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				panelDetailPositionlieferantVergleich.setKeyWhenDetailPanel(key);
				panelDetailPositionlieferantVergleich.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelQueryPositionlieferantVergleich
						.updateButtons(panelDetailPositionlieferantVergleich.getLockedstateDetailMainKey());
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelDetailEinkaufsangebotpositionen) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryEinkaufsangebotpositionen.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			}
			if (eI.getSource() == panelDetailWeblieferant) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryWeblieferant.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));

			}
			if (eI.getSource() == panelDetailPositionlieferant) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryPositionlieferant.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));

			}
			if (eI.getSource() == panelDetailPositionlieferantVergleich) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryPositionlieferantVergleich.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));

			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (eI.getSource() == panelDetailEinkaufsangebot) {

				panelQueryEinkaufsangebot.eventYouAreSelected(false);
				getInternalFrameAngebotstkl().getEinkaufsangebotDto()
						.setIId((Integer) panelQueryEinkaufsangebot.getSelectedId());
				panelDetailEinkaufsangebot.setKeyWhenDetailPanel(panelQueryEinkaufsangebot.getSelectedId());
				this.setSelectedComponent(panelQueryEinkaufsangebot);
				if (panelQueryEinkaufsangebot.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
				}

			} else if (eI.getSource() == panelDetailEinkaufsangebotpositionen) {
				// bei einem Neu im 1:n Panel wurde der KeyForLockMe
				// ueberschrieben
				setKeyWasForLockMe();
				if (panelDetailEinkaufsangebotpositionen.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryEinkaufsangebotpositionen.getId2SelectAfterDelete();
					panelQueryEinkaufsangebotpositionen.setSelectedId(oNaechster);
				}
				panelSplitEinkaufsangebotpositionen.eventYouAreSelected(false); // refresh
				// auf
				// das
				// gesamte
				// 1
				// :
				// n
				// panel
			} else if (eI.getSource() == panelDetailWeblieferant) {

				setKeyWasForLockMe();
				if (panelDetailWeblieferant.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryWeblieferant.getId2SelectAfterDelete();
					panelQueryWeblieferant.setSelectedId(oNaechster);
				}
				panelSplitWeblieferant.eventYouAreSelected(false); // refresh

			} else if (eI.getSource() == panelDetailEkaglieferant) {

				setKeyWasForLockMe();
				if (panelDetailEkaglieferant.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryEkagLieferant.getId2SelectAfterDelete();
					panelQueryEkagLieferant.setSelectedId(oNaechster);
				}
				panelSplitEkaglieferant.eventYouAreSelected(false); // refresh

			} else if (eI.getSource() == panelDetailPositionlieferant) {

				setKeyWasForLockMe();
				if (panelDetailPositionlieferant.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPositionlieferant.getId2SelectAfterDelete();
					panelQueryPositionlieferant.setSelectedId(oNaechster);
				}
				panelSplitPositionlieferant.eventYouAreSelected(false); // refresh

			} else if (eI.getSource() == panelDetailPositionlieferantVergleich) {

				setKeyWasForLockMe();
				if (panelDetailPositionlieferantVergleich.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPositionlieferantVergleich.getId2SelectAfterDelete();
					panelQueryPositionlieferantVergleich.setSelectedId(oNaechster);
				}
				panelSplitPositionlieferantVergleich.eventYouAreSelected(false); // refresh

			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			if (eI.getSource() instanceof AssistentView) {
				getAktuellesPanel().eventYouAreSelected(false);
			}
			refreshTitle();
			super.lPEventItemChanged(eI);
		} else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelDetailEinkaufsangebotpositionen) {
				panelSplitEinkaufsangebotpositionen.eventYouAreSelected(false); // das
				// 1
				// :
				// n
				// Panel
				// neu
				// aufbauen
			}
			if (eI.getSource() == panelDetailWeblieferant) {
				panelSplitWeblieferant.eventYouAreSelected(false); // das
				// 1
				// :
				// n
				// Panel
				// neu
				// aufbauen
			}
			if (eI.getSource() == panelDetailEkaglieferant) {
				panelSplitEkaglieferant.eventYouAreSelected(false);
			}
			if (eI.getSource() == panelDetailPositionlieferant) {
				panelSplitPositionlieferant.eventYouAreSelected(false);
			}
			if (eI.getSource() == panelDetailPositionlieferantVergleich) {
				panelSplitPositionlieferantVergleich.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelQueryEinkaufsangebot) {
				createKopfdaten(null);
				// wenn es bisher keinen eintrag gibt, die restlichen
				// panels aktivieren
				if (panelQueryEinkaufsangebot.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, true);
				}

				panelDetailEinkaufsangebot.eventActionNew(eI, true, false);
				setSelectedComponent(panelDetailEinkaufsangebot);

			} else if (eI.getSource() == panelQueryEinkaufsangebotpositionen) {
				panelDetailEinkaufsangebotpositionen.eventActionNew(eI, true, false);
				panelDetailEinkaufsangebotpositionen.eventYouAreSelected(false);
				setSelectedComponent(panelSplitEinkaufsangebotpositionen); // ui
			} else if (eI.getSource() == panelQueryWeblieferant) {
				panelDetailWeblieferant.eventActionNew(eI, true, false);
				panelDetailWeblieferant.eventYouAreSelected(false);
				setSelectedComponent(panelSplitWeblieferant); // ui
			} else if (eI.getSource() == panelQueryEkagLieferant) {
				panelDetailEkaglieferant.eventActionNew(eI, true, false);
				panelDetailEkaglieferant.eventYouAreSelected(false);
				setSelectedComponent(panelSplitEkaglieferant); // ui
			} else if (eI.getSource() == panelQueryPositionlieferant) {

				// Zuerst Position auswaehlen

				String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

				panelQueryFLREkagPositionFuerPositionlieferant = new PanelQueryFLR(null,
						AngebotstklFilterFactory.getInstance().createFKEinkaufsangebot(
								getInternalFrameAngebotstkl().getEinkaufsangebotDto().getIId()),
						QueryParameters.UC_ID_EINKAUFSANGEBOTPOSITIONEN, aWhichButtonIUse, getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr("angb.panel.positionen"));

				new DialogQuery(panelQueryFLREkagPositionFuerPositionlieferant);

			} else if (eI.getSource() == panelQueryPositionlieferantVergleich) {
				panelDetailPositionlieferantVergleich.eventActionNew(eI, true, false);
				panelDetailPositionlieferantVergleich.eventYouAreSelected(false);
				setSelectedComponent(panelSplitPositionlieferantVergleich); // ui
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			if (eI.getSource().equals(panelQueryEinkaufsangebotpositionen)) {
				copyHV();
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();
			if (sAspectInfo.equals(BUTTON_WEBABFRAGE)) {
				if (LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_WEB_BAUTEIL_ANFRAGE)) {
					showPanelWebabfrageEinkaufsangebot();
				}
			}
		}

	}

	/**
	 * @throws Throwable
	 */
	private void showPanelWebabfrageEinkaufsangebot() throws Throwable {
		Object[] objectPosIds = getPanelQueryEinkaufsangebotpositionen().getSelectedIds();

		if (objectPosIds == null || objectPosIds.length < 1) {
			DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
					LPMain.getInstance().getTextRespectUISPr("agstkl.einkaufsangebotposition.keinepositionselektiert"));
			return;
		}

		EinkaufsangebotDto ekangebotDto = getInternalFrameAngebotstkl().getEinkaufsangebotDto();
		if ((ekangebotDto.getNMenge1() == null || BigDecimal.ZERO.compareTo(ekangebotDto.getNMenge1()) == 0)
				&& (ekangebotDto.getNMenge2() == null || BigDecimal.ZERO.compareTo(ekangebotDto.getNMenge2()) == 0)
				&& (ekangebotDto.getNMenge3() == null || BigDecimal.ZERO.compareTo(ekangebotDto.getNMenge3()) == 0)
				&& (ekangebotDto.getNMenge4() == null || BigDecimal.ZERO.compareTo(ekangebotDto.getNMenge4()) == 0)
				&& (ekangebotDto.getNMenge5() == null || BigDecimal.ZERO.compareTo(ekangebotDto.getNMenge5()) == 0)) {
			DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("agstkl.einkaufsangebotposition.keinemengenstaffelangegeben"));
			return;
		}

		Integer[] posIIds = Arrays.copyOf(objectPosIds, objectPosIds.length, Integer[].class);
		List<WebabfragepositionDto> webabfragepositionDtos = DelegateFactory.getInstance().getAngebotstklDelegate()
				.getWebabfragepositionenByEinkaufsangebotpositionen(Arrays.asList(posIIds));

		AssistentView view = new AssistentView(getInternalFrameAngebotstkl(),
				LPMain.getTextRespectUISPr("agstkl.webabfrage.button"),
				new WebabfrageAssistentController(getInternalFrameAngebotstkl(), webabfragepositionDtos));
		getInternalFrame().showPanelDialog(view);
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

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			refreshAuswahl();
			panelQueryEinkaufsangebot.eventYouAreSelected(false);
			panelQueryEinkaufsangebot.updateButtons();
			if (panelQueryEinkaufsangebot.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
			}

		} else if (selectedIndex == IDX_PANEL_KOPFDATEN) {
			Integer key = null;
			if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {
				key = getInternalFrameAngebotstkl().getEinkaufsangebotDto().getIId();
			}
			createKopfdaten(key);
			panelDetailEinkaufsangebot.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_KOMMENTAR) {
			Integer key = null;
			if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {
				key = getInternalFrameAngebotstkl().getEinkaufsangebotDto().getIId();
			}
			createKommentar(key);
			panelDetailEinkaufsangebotKommentar.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_EKAGLIEFERANT_OPTIMIEREN) {
			Integer key = null;
			if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {
				key = getInternalFrameAngebotstkl().getEinkaufsangebotDto().getIId();
			}
			createLeferantenOptimieren(key);
			panelLieferantenOptimieren.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_POSITIONEN) {
			refreshAngebotstklPositionen();

			Integer key = null;
			if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {
				key = getInternalFrameAngebotstkl().getEinkaufsangebotDto().getIId();

				FilterKriterium[] defaultfk = AngebotstklFilterFactory.getInstance().createFKEinkaufsangebot(key);
				panelQueryEinkaufsangebotpositionen.setDefaultFilter(defaultfk);
			}

			panelSplitEinkaufsangebotpositionen.eventYouAreSelected(false);

			panelQueryEinkaufsangebotpositionen
					.updateButtons(panelDetailEinkaufsangebotpositionen.getLockedstateDetailMainKey());
		} else if (selectedIndex == IDX_PANEL_EKAGLIEFERANT) {
			refreshEkaglieferant();

			Integer key = null;
			if (getInternalFrameAngebotstkl().getEinkaufsangebotDto() != null) {
				key = getInternalFrameAngebotstkl().getEinkaufsangebotDto().getIId();

				FilterKriterium[] defaultfk = AngebotstklFilterFactory.getInstance().createFKEinkaufsangebot(key);
				panelQueryEkagLieferant.setDefaultFilter(defaultfk);
			}

			panelSplitEkaglieferant.eventYouAreSelected(false);

			panelQueryEkagLieferant.updateButtons(panelDetailEkaglieferant.getLockedstateDetailMainKey());
		} else if (selectedIndex == IDX_PANEL_POSITIONEN_LF) {

			if (panelQueryEkagLieferant == null || panelQueryEkagLieferant.getSelectedId() == null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("agstkl.lieferanten.zuerstauswaehlen"));
				refreshEkaglieferant();
				setSelectedComponent(panelSplitEkaglieferant);
			} else {

				EkaglieferantDto ekagDto = DelegateFactory.getInstance().getAngebotstklDelegate()
						.ekaglieferantFindByPrimaryKey((Integer) panelQueryEkagLieferant.getSelectedId());

				if (ekagDto.getEinkaufsangebotIId().equals(getEinkaufsangebotDto().getIId())) {

					refreshPositionlieferant();

					FilterKriterium[] filtersPositionen = AngebotstklFilterFactory.getInstance()
							.createFKPositionlieferant((Integer) panelQueryEkagLieferant.getSelectedId());

					panelQueryPositionlieferant.setDefaultFilter(filtersPositionen);

					panelSplitPositionlieferant.eventYouAreSelected(false);

					panelQueryPositionlieferant
							.updateButtons(panelDetailPositionlieferant.getLockedstateDetailMainKey());
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("agstkl.lieferanten.zuerstauswaehlen"));
					refreshEkaglieferant();
					setSelectedComponent(panelSplitEkaglieferant);
				}
			}

		} else if (selectedIndex == IDX_PANEL_POSITIONEN_LF_VERGLEICH) {

			if (panelQueryEkagLieferant == null || panelQueryEkagLieferant.getSelectedId() == null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("agstkl.lieferanten.zuerstauswaehlen"));
				refreshEkaglieferant();
				setSelectedComponent(panelSplitEkaglieferant);
			} else {

				EkaglieferantDto ekagDto = DelegateFactory.getInstance().getAngebotstklDelegate()
						.ekaglieferantFindByPrimaryKey((Integer) panelQueryEkagLieferant.getSelectedId());

				if (ekagDto.getEinkaufsangebotIId().equals(getEinkaufsangebotDto().getIId())) {

					refreshPositionlieferantvergleich();

					FilterKriterium[] filtersPositionen = AngebotstklFilterFactory.getInstance()
							.createFKPositionlieferant((Integer) panelQueryEkagLieferant.getSelectedId());

					panelQueryPositionlieferantVergleich.setDefaultFilter(filtersPositionen);

					panelSplitPositionlieferantVergleich.eventYouAreSelected(false);

					panelQueryPositionlieferantVergleich
							.updateButtons(panelDetailPositionlieferantVergleich.getLockedstateDetailMainKey());

					Integer selId = (Integer) panelQueryPositionlieferantVergleich.getSelectedId();

					if (selId == null) {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
								LPMain.getTextRespectUISPr("agstkl.lieferant.positionenlfvergleich.keinezeilen"));
						refreshEkaglieferant();
						setSelectedComponent(panelSplitEkaglieferant);
					}

				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("agstkl.lieferanten.zuerstauswaehlen"));
					refreshEkaglieferant();
					setSelectedComponent(panelSplitEkaglieferant);
				}
			}

		} else if (selectedIndex == IDX_PANEL_WEBLIEFERANT) {
			refreshWeblieferant();

			Integer key = null;

			key = getInternalFrameAngebotstkl().getEinkaufsangebotDto().getIId();

			FilterKriterium[] defaultfk = AngebotstklFilterFactory.getInstance()
					.createFKWeblieferantEinkaufsangebot(key);
			panelQueryWeblieferant.setDefaultFilter(defaultfk);

			panelSplitWeblieferant.eventYouAreSelected(false);

			panelQueryWeblieferant.updateButtons(panelDetailWeblieferant.getLockedstateDetailMainKey());
		}
		refreshTitle();
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);

			// Menue Datei; ein neuer Eintrag wird immer oben im Menue
			// eingetragen
			JMenu jmModul = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_MODUL);
			jmModul.add(new JSeparator(), 0);

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_EK_ANGEBOT_ANFRAGE)) {

				JMenuItem menuItemDateiVergleich = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("agstkl.lieferant.positionenlfvergleich"));
				menuItemDateiVergleich.addActionListener(this);
				menuItemDateiVergleich.setActionCommand(MENU_DATEI_VERGLEICH);
				jmModul.add(menuItemDateiVergleich, 0);

			}

			JMenuItem menuItemDateiDrucken = new JMenuItem(LPMain.getInstance().getTextRespectUISPr("lp.menu.drucken"));
			menuItemDateiDrucken.addActionListener(this);
			menuItemDateiDrucken.setActionCommand(MENU_DATEI_DRUCKEN);
			jmModul.add(menuItemDateiDrucken, 0);

			JMenu jmBearbeiten = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

			JMenuItem menuItemHandartikelInBestehendenUmwandeln = new JMenuItem(
					LPMain.getTextRespectUISPr("auftrag.bearbeiten.handartikelinbestehdendenumwandeln"));
			menuItemHandartikelInBestehendenUmwandeln.addActionListener(this);
			menuItemHandartikelInBestehendenUmwandeln
					.setActionCommand(MENU_BEARBEITEN_HANDARTIKEL_IN_BESTEHENDEN_ARTIKEL_UMANDELN);
			jmBearbeiten.add(menuItemHandartikelInBestehendenUmwandeln, 0);

			JMenuItem menuItemHandartikelUmwandeln = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("angebot.bearbeiten.handartikelumwandeln"));
			menuItemHandartikelUmwandeln.addActionListener(this);
			menuItemHandartikelUmwandeln.setActionCommand(MENU_BEARBEITEN_HANDARTIKEL_UMWANDELN);
			jmBearbeiten.add(menuItemHandartikelUmwandeln, 0);

		}

		return wrapperMenuBar;
	}

	private PanelSplit refreshAngebotstklPositionen() throws Throwable {
		if (panelSplitEinkaufsangebotpositionen == null) {
			panelDetailEinkaufsangebotpositionen = new PanelEinkaufsangebotpositionen(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("angb.panel.positionen"), null); // eventuell
			// gibt
			// es
			// noch
			// keine
			// position

			QueryType[] qtPositionen = null;
			FilterKriterium[] filtersPositionen = AngebotstklFilterFactory.getInstance()
					.createFKEinkaufsangebot(getInternalFrameAngebotstkl().getEinkaufsangebotDto().getIId());

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1, PanelBasis.ACTION_POSITION_VORPOSITIONEINFUEGEN,
					PanelBasis.ACTION_KOPIEREN, PanelBasis.ACTION_EINFUEGEN_LIKE_NEW

			};

			panelQueryEinkaufsangebotpositionen = new PanelQuery(qtPositionen, filtersPositionen,
					QueryParameters.UC_ID_EINKAUFSANGEBOTPOSITIONEN, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("angb.panel.positionen"), true); // flag
			// ,
			// damit
			// flr
			// erst
			// beim
			// aufruf
			// des
			// panels
			// loslaeuft

			panelSplitEinkaufsangebotpositionen = new PanelSplit(getInternalFrame(),
					panelDetailEinkaufsangebotpositionen, panelQueryEinkaufsangebotpositionen, 130);

			boolean intelligenterStklImport = LPMain.getInstance().getDesktop().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_INTELLIGENTER_EINKAUFSAGSTKLIMPORT);

			panelQueryEinkaufsangebotpositionen.createAndSaveAndShowButton("/com/lp/client/res/document_into.png",
					LPMain.getInstance().getTextRespectUISPr(
							intelligenterStklImport ? "agstkl.einkaufsangebot.intelligentereinkaufsangebotimport" : "stkl.positionen.cvsimport"),
					BUTTON_IMPORTCSV_EKAGSTKLPOSITIONEN, null);

			panelQueryEinkaufsangebotpositionen.createAndSaveAndShowButton("/com/lp/client/res/branch.png",
					LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.positionen.verdichten"),
					ACTION_SPECIAL_VERDICHTEN, RechteFac.RECHT_AS_ANGEBOTSTKL_CUD);

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_WEB_BAUTEIL_ANFRAGE)) {
				panelQueryEinkaufsangebotpositionen.createAndSaveAndShowButton("/com/lp/client/res/earth_view.png",
						LPMain.getInstance().getTextRespectUISPr("agstkl.webabfrage.button"), BUTTON_WEBABFRAGE, null);
				panelQueryEinkaufsangebotpositionen.setHmButtonEnabled(BUTTON_WEBABFRAGE, true);
			}
			panelQueryEinkaufsangebotpositionen.setMultipleRowSelectionEnabled(true);

			setComponentAt(IDX_PANEL_POSITIONEN, panelSplitEinkaufsangebotpositionen);
		}

		return panelSplitEinkaufsangebotpositionen;
	}

	private PanelSplit refreshEkaglieferant() throws Throwable {
		if (panelSplitEkaglieferant == null) {
			panelDetailEkaglieferant = new PanelEkaglieferant(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("agstkl.ekgruppelieferant"), null); // eventuell

			QueryType[] qtPositionen = null;
			FilterKriterium[] filtersPositionen = AngebotstklFilterFactory.getInstance()
					.createFKEinkaufsangebot(getInternalFrameAngebotstkl().getEinkaufsangebotDto().getIId());

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW

			};

			panelQueryEkagLieferant = new PanelQuery(qtPositionen, filtersPositionen,
					QueryParameters.UC_ID_EKAGLIEFERANT, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("agstkl.ekgruppelieferant"), true); // flag
			panelQueryEkagLieferant.setMultipleRowSelectionEnabled(true);
			panelSplitEkaglieferant = new PanelSplit(getInternalFrame(), panelDetailEkaglieferant,
					panelQueryEkagLieferant, 270);

			panelQueryEkagLieferant.createAndSaveAndShowButton("/com/lp/client/res/goto.png",
					LPMain.getInstance().getTextRespectUISPr("agstkl.ekgruppe.hinzufuegen"),
					BUTTON_EKGRUPPE_HINZUFUEGEN, null);

			panelQueryEkagLieferant.createAndSaveAndShowButton("/com/lp/client/res/document_out.png",
					LPMain.getInstance().getTextRespectUISPr("agstkl.lieferanten.xls.erzeugen"), BUTTON_XLS_ERZEUGEN,
					null);

			panelQueryEkagLieferant.createAndSaveAndShowButton("/com/lp/client/res/mail.png",
					LPMain.getInstance().getTextRespectUISPr("agstkl.lieferanten.xls.erzeugenundversende"),
					BUTTON_XLS_ERZEUGEN_UND_PER_MAIL_VERSENDEN, null);

			panelQueryEkagLieferant.createAndSaveAndShowButton("/com/lp/client/res/document_into.png",
					LPMain.getInstance().getTextRespectUISPr("agstkl.lieferanten.xls.einlesen"), BUTTON_XLS_EINLESEN,
					null);

			setComponentAt(IDX_PANEL_EKAGLIEFERANT, panelSplitEkaglieferant);
		}

		return panelSplitEinkaufsangebotpositionen;
	}

	private PanelSplit refreshPositionlieferant() throws Throwable {
		if (panelSplitPositionlieferant == null) {
			panelDetailPositionlieferant = new PanelPositionlieferant(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("agstkl.lieferant.positionenlf"), null); // eventuell

			QueryType[] qtPositionen = null;
			FilterKriterium[] filtersPositionen = AngebotstklFilterFactory.getInstance()
					.createFKPositionlieferant((Integer) panelQueryEkagLieferant.getSelectedId());

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryPositionlieferant = new PanelQuery(qtPositionen, filtersPositionen,
					QueryParameters.UC_ID_POSITIONLIEFERANT, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("agstkl.lieferant.positionenlf"), true); // flag

			panelQueryPositionlieferant
					.addDirektFilter(AngebotstklFilterFactory.getInstance().createFKDPositionlieferantArtikelnummer());

			panelQueryPositionlieferant
					.addDirektFilter(AngebotstklFilterFactory.getInstance().createFKDArtikelBezeichnung());

			panelSplitPositionlieferant = new PanelSplit(getInternalFrame(), panelDetailPositionlieferant,
					panelQueryPositionlieferant, 160);

			setComponentAt(IDX_PANEL_POSITIONEN_LF, panelSplitPositionlieferant);
		}

		return panelSplitPositionlieferant;
	}

	private PanelSplit refreshPositionlieferantvergleich() throws Throwable {
		if (panelSplitPositionlieferantVergleich == null) {
			panelDetailPositionlieferantVergleich = new PanelPositionlieferantVergleich(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("agstkl.lieferant.positionenlfvergleich"), null); // eventuell

			QueryType[] qtPositionen = null;
			FilterKriterium[] filtersPositionen = AngebotstklFilterFactory.getInstance()
					.createFKPositionlieferant((Integer) panelQueryEkagLieferant.getSelectedId());

			String[] aWhichButtonIUse = {

			};

			panelQueryPositionlieferantVergleich = new PanelQuery(qtPositionen, filtersPositionen,
					QueryParameters.UC_ID_POSITIONLIEFERANT, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("agstkl.lieferant.positionenlfvergleich"), true); // flag

			panelQueryPositionlieferantVergleich
					.addDirektFilter(AngebotstklFilterFactory.getInstance().createFKDPositionlieferantArtikelnummer());

			panelQueryPositionlieferantVergleich
					.addDirektFilter(AngebotstklFilterFactory.getInstance().createFKDArtikelBezeichnung());

			panelSplitPositionlieferantVergleich = new PanelSplit(getInternalFrame(),
					panelDetailPositionlieferantVergleich, panelQueryPositionlieferantVergleich, 250);

			setComponentAt(IDX_PANEL_POSITIONEN_LF_VERGLEICH, panelSplitPositionlieferantVergleich);
		}

		return panelSplitPositionlieferantVergleich;
	}

	private PanelSplit refreshWeblieferant() throws Throwable {
		if (panelSplitWeblieferant == null) {
			panelDetailWeblieferant = new PanelEkweblieferant(getInternalFrameAngebotstkl(),
					LPMain.getInstance().getTextRespectUISPr("agstkl.weblieferant"), null); // eventuell

			QueryType[] qtPositionen = null;
			FilterKriterium[] filtersPositionen = AngebotstklFilterFactory.getInstance()
					.createFKWeblieferantEinkaufsangebot(
							getInternalFrameAngebotstkl().getEinkaufsangebotDto().getIId());

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1

			};

			panelQueryWeblieferant = new PanelQuery(qtPositionen, filtersPositionen,
					QueryParameters.UC_ID_EKWEBLIEFERANT, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("agstkl.weblieferant"), true); // flag

			panelSplitWeblieferant = new PanelSplit(getInternalFrame(), panelDetailWeblieferant, panelQueryWeblieferant,
					140);

			setComponentAt(IDX_PANEL_WEBLIEFERANT, panelSplitWeblieferant);
		}

		return panelSplitWeblieferant;
	}

	public void geheZuPositionsliefeferant(Integer positionlieferantIId) throws Throwable {

		PositionlieferantDto posLiefDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.positionlieferantFindByPrimaryKey(positionlieferantIId);

		refreshEkaglieferant();
		FilterKriterium[] defaultfk = AngebotstklFilterFactory.getInstance()
				.createFKEinkaufsangebot(getEinkaufsangebotDto().getIId());
		panelQueryEkagLieferant.setDefaultFilter(defaultfk);
		panelSplitEkaglieferant.eventYouAreSelected(false);
		panelQueryEkagLieferant.setSelectedId(posLiefDto.getEgaklieferantIId());
		panelSplitEkaglieferant.eventYouAreSelected(false);

		refreshPositionlieferant();
		setSelectedComponent(panelSplitPositionlieferant);
		FilterKriterium[] filtersPositionen = AngebotstklFilterFactory.getInstance()
				.createFKPositionlieferant(posLiefDto.getEgaklieferantIId());

		panelQueryPositionlieferant.setDefaultFilter(filtersPositionen);

		panelSplitPositionlieferant.eventYouAreSelected(false);

		panelQueryPositionlieferant.setSelectedId(positionlieferantIId);
		panelSplitPositionlieferant.eventYouAreSelected(false);

	}

	public void copyHV() throws ExceptionLP, Throwable {

		Object aoIIdPosition[] = panelQueryEinkaufsangebotpositionen.getSelectedIds();

		if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
			EinkaufsangebotpositionDto[] dtos = new EinkaufsangebotpositionDto[aoIIdPosition.length];
			Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
			for (int i = 0; i < aIIdPosition.length; i++) {
				aIIdPosition[i] = (Integer) aoIIdPosition[i];
				dtos[i] = DelegateFactory.getInstance().getAngebotstklDelegate()
						.einkaufsangebotpositionFindByPrimaryKey((Integer) aoIIdPosition[i]);
			}
			LPMain.getInstance().getPasteBuffer().writeObjectToPasteBuffer(dtos);
		}

	}

	public void einfuegenHV() throws IOException, ParserConfigurationException, SAXException, Throwable {

		Object o = LPMain.getInstance().getPasteBuffer().readObjectFromPasteBuffer();

		if (o instanceof BelegpositionDto[]) {

			EinkaufsangebotpositionDto[] positionDtos = DelegateFactory.getInstance()
					.getBelegpostionkonvertierungDelegate()
					.konvertiereNachEinkaufsangebotpositionDto((BelegpositionDto[]) o);

			if (positionDtos != null) {
				Integer iId = null;

				for (int i = 0; i < positionDtos.length; i++) {
					EinkaufsangebotpositionDto positionDto = positionDtos[i];
					try {
						positionDto.setIId(null);
						positionDto.setBelegIId((Integer) panelQueryEinkaufsangebot.getSelectedId());
						positionDto.setISort(null);
						
						//SP8821
						positionDto.setPositionlieferantIIdUebersteuertMenge1(null);
						positionDto.setPositionlieferantIIdUebersteuertMenge2(null);
						positionDto.setPositionlieferantIIdUebersteuertMenge3(null);
						positionDto.setPositionlieferantIIdUebersteuertMenge4(null);
						positionDto.setPositionlieferantIIdUebersteuertMenge5(null);

						
						// wir legen eine neue position an
						if (iId == null) {
							iId = DelegateFactory.getInstance().getAngebotstklDelegate()
									.createEinkaufsangebotposition(positionDto);
						} else {
							DelegateFactory.getInstance().getAngebotstklDelegate()
									.createEinkaufsangebotposition(positionDto);
						}
					} catch (Throwable t) {
						// nur loggen!
						myLogger.error(t.getMessage(), t);
					}
				}

				// die Liste neu aufbauen
				panelQueryEinkaufsangebotpositionen.eventYouAreSelected(false);

				// den Datensatz in der Liste selektieren
				panelQueryEinkaufsangebotpositionen.setSelectedId(iId);

				// im Detail den selektierten anzeigen
				panelQueryEinkaufsangebotpositionen.eventYouAreSelected(false);
			}

		}

	}

	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfBelegPosI) throws Throwable {
		AgstklpositionDto angStklPosDto = (AgstklpositionDto) belegposDtoI;

		String sPosArt = angStklPosDto.getPositionsartCNr();
		if (!LocaleFac.POSITIONSART_IDENT.startsWith(sPosArt)
				&& !LocaleFac.POSITIONSART_HANDEINGABE.startsWith(sPosArt)) {
			String sMsg = "LocaleFac.POSITIONSART_IDENT.startsWith(sPosArt) "
					+ "&& !LocaleFac.POSITIONSART_HANDEINGABE.startsWith(sPosArt)";
			throw new IllegalArgumentException(sMsg);
		}

		angStklPosDto.setBelegIId(getInternalFrameAngebotstkl().getAgstklDto().getIId());
		angStklPosDto.setISort(xalOfBelegPosI + 1000);

		if (angStklPosDto.getNGestehungspreis() == null) {
			// WH: 2007-01-15: Gestpreis aus Hauptlager des Mandanten
			// uebernehmen
			if (angStklPosDto.getArtikelIId() != null) {
				LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate().getHauptlagerDesMandanten();

				BigDecimal preis = DelegateFactory.getInstance().getLagerDelegate()
						.getGemittelterGestehungspreisEinesLagers(angStklPosDto.getArtikelIId(), lagerDto.getIId());
				angStklPosDto.setNGestehungspreis(preis);
			} else {
				angStklPosDto.setNGestehungspreis(new BigDecimal(0));
			}
		}
		if (angStklPosDto.getBDrucken() == null) {
			angStklPosDto.setBDrucken(Helper.boolean2Short(true));
		}
		if (angStklPosDto.getBRabattsatzuebersteuert() == null) {
			angStklPosDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
		}
		if (angStklPosDto.getNNettoeinzelpreis() == null) {
			angStklPosDto.setNNettoeinzelpreis(new BigDecimal(0));
		}
		if (angStklPosDto.getNNettogesamtpreis() == null) {
			angStklPosDto.setNNettogesamtpreis(new BigDecimal(0));
		}
		if (angStklPosDto.getFRabattsatz() == null) {
			angStklPosDto.setFRabattsatz(0.0);
		}
		if (angStklPosDto.getBArtikelbezeichnunguebersteuert() == null) {
			angStklPosDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
		}

	}

	public Integer saveBelegPosAsTextpos(POSDocument2POSDto pOSDocument2POSDtoI, int xalOfBelegPosI)
			throws ExceptionLP, Throwable {
		return null;
	}

}
