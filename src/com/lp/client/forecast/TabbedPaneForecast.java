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
package com.lp.client.forecast;

import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.xml.sax.SAXException;

import com.lp.client.artikel.DialogArtikelXLSImport;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.assistent.view.AssistentView;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.open.CsvFile;
import com.lp.client.frame.filechooser.open.FileOpenerFactory;
import com.lp.client.frame.filechooser.open.XlsFileOpener;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.ImportWooCommerceCsvDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.forecast.service.ForecastImportZeissDto;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.HvOptional;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class TabbedPaneForecast extends TabbedPane {

	public String sRechtModulweit = null;

	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryForecast = null;

	public PanelQuery getPanelQueryForecast() {
		return panelQueryForecast;
	}

	private PanelForecast panelDetailForecast = null;

	public PanelQuery getPanelQueryForecastauftrag() {
		return panelQueryAuftrag;
	}

	private int vorhererSelektierterTab = -1;

	private PanelQuery panelQueryAuftrag = null;
	private PanelBasis panelSplitAuftrag = null;
	private PanelForecastauftrag panelBottomAuftrag = null;

	private PanelQuery panelQueryLieferadresse = null;

	public PanelQuery getPanelQueryLieferadresse() {
		return panelQueryLieferadresse;
	}

	public void geheZuForecastposition(Integer forecastpositionIId) throws Throwable {

		ForecastpositionDto fpDto = DelegateFactory.getInstance().getForecastDelegate()
				.forecastpositionFindByPrimaryKey(forecastpositionIId);

		ForecastauftragDto fcaDto = DelegateFactory.getInstance().getForecastDelegate()
				.forecastauftragFindByPrimaryKey(fpDto.getForecastauftragIId());

		FclieferadresseDto fclDto = DelegateFactory.getInstance().getForecastDelegate()
				.fclieferadresseFindByPrimaryKey(fcaDto.getFclieferadresseIId());

		ForecastDto fcDto = DelegateFactory.getInstance().getForecastDelegate()
				.forecastFindByPrimaryKey(fclDto.getForecastIId());
		refreshAuswahl();
		setzePanelQueryKey(panelQueryForecast, fcDto.getIId());

		setSelectedIndex(IDX_PANEL_AUSWAHL);
		createLieferadresse();
		setzePanelQueryKey(panelQueryLieferadresse, fclDto.getiId());

		setSelectedIndex(IDX_PANEL_LIEFERADRESSE);
		createAuftraege();
		setzePanelQueryKey(panelQueryAuftrag, fcaDto.getIId());
		setSelectedIndex(IDX_PANEL_FORECASTAUFTRAG);
		createPositionen(fcaDto.getIId());
		setzePanelQueryKey(panelQueryPositionen, forecastpositionIId);
		setSelectedIndex(IDX_PANEL_FORECASTPOSITION);

	}

	private void setzePanelQueryKey(PanelQuery pq, Integer key) throws Throwable {

		if (pq.isFilterActive()) {
			// Wenn in den Filtern was drinsteht, kann der
			// Datensatz evtl. nicht selektiert werden.
			// -> Filter leeren und refresh
			pq.clearAllFilters();
			pq.eventYouAreSelected(false);
		}

		// PJ18469 fkcombobox und schnellansicht leeren
		if (pq.getCbSchnellansicht() != null) {
			pq.getCbSchnellansicht().setSelected(false);
		}
		pq.setKeyOfFilterComboBox(null);

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(com.lp.server.benutzer.service.RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
			if (pq.getWcbVersteckteFelderAnzeigen() != null) {
				// Wenn Recht DARF_VERSTECKTE_SEHEN, dann

				pq.getWcbVersteckteFelderAnzeigen().setSelected(true);
			}
		}

		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("i_id", true, key + "", FilterKriterium.OPERATOR_EQUAL, false);

		pq.befuelleFilterkriteriumKey(kriterien);

		pq.eventYouAreSelected(false);
		pq.befuelleFilterkriteriumKey(null);
	}

	public String formatKunde(KundeDto kundeDto) {
		String s = kundeDto.getPartnerDto().formatTitelAnrede();

		if (kundeDto.getPartnerDto().getLandplzortDto() != null) {
			s += "|" + kundeDto.getPartnerDto().getCKbez() + "|" + kundeDto.getPartnerDto().formatLKZPLZOrt();

		}

		if (kundeDto.getPartnerDto().getCStrasse() != null) {
			s += "|" + kundeDto.getPartnerDto().getCStrasse();

		}
		return s;

	}

	private PanelBasis panelSplitLieferadresse = null;
	private PanelFclieferadresse panelBottomLieferadresse = null;

	private PanelQuery panelQueryPositionen = null;

	public PanelQuery getPanelQueryPositionen() {
		return panelQueryPositionen;
	}

	private PanelBasis panelSplitPositionen = null;
	private PanelForcastposition panelBottomPositionen = null;

	private PanelQuery panelQueryLinienabruf = null;
	private PanelBasis panelSplitLinienabruf = null;
	private PanelLinienabruf panelBottomLinienabruf = null;

	private ForecastDto forecastDto;

	public static int IDX_PANEL_AUSWAHL = -1;
	private static int IDX_PANEL_KOPFDATEN = -1;
	public static int IDX_PANEL_LIEFERADRESSE = -1;
	public static int IDX_PANEL_FORECASTAUFTRAG = -1;
	public static int IDX_PANEL_FORECASTPOSITION = -1;
	public static int IDX_PANEL_LINIENABRUF = -1;

	private final String MENU_BEARBEITEN_ERLEDIGEN = "MENU_BEARBEITEN_ERLEDIGEN";

	private final String MENU_JOURNAL_BESCHAFFUNG = "MENU_JOURNAL_BESCHAFFUNG";
	private final String MENU_JOURNAL_UEBERSICHT = "MENU_JOURNAL_UEBERSICHT";
	private final String MENU_JOURNAL_GEPLANTERUMSATZ = "MENU_JOURNAL_GEPLANTERUMSATZ";

	private final String MENU_MODUL_IMPORT_VMI = "MENU_MODUL_IMPORT_VMI";

	private final String MENUE_ACTION_POSITIONEN_XLSIMPORT = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_POSITIONEN_XLSIMPORT";

	private final String MENUE_ACTION_PRINT_DELTALISTE = PanelBasis.ACTION_MY_OWN_NEW + "MENUE_ACTION_PRINT_DELTALISTE";

	private final String MENUE_ACTION_PRINT_LIEFERSITUATION = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_ACTION_PRINT_LIEFERSITUATION";

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneForecast(InternalFrame internalFrameI) throws Throwable {

		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr("fc.forecast"));

		jbInit();

		sRechtModulweit = internalFrameI.getRechtModulweit();

		initComponents();
	}

	public PanelQuery getPanelQueryPersonal() {
		return panelQueryForecast;
	}

	private void refreshAuswahl() throws Throwable {
		if (panelQueryForecast == null) {

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryForecast = new PanelQuery(null, ForecastFilterFactory.getInstance().createFKKundeMandantCNr(),
					QueryParameters.UC_ID_FORECAST, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("fc.forecast"), true);
			panelQueryForecast.addDirektFilter(ForecastFilterFactory.getInstance().createFKDForecastnummer());

			panelQueryForecast.befuelleFilterkriteriumSchnellansicht(
					ForecastFilterFactory.getInstance().createFKSchnellansichtForecast());

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryForecast);
		}
	}

	private void createAuftraege() throws Throwable {
		if (panelSplitAuftrag == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryAuftrag = new PanelQuery(null,
					ForecastFilterFactory.getInstance()
							.createFKForecastauftraege((Integer) panelQueryLieferadresse.getSelectedId()),
					QueryParameters.UC_ID_FORECASTAUFTRAG, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("fc.forecastauftraege"), true);

			panelQueryAuftrag.befuelleFilterkriteriumSchnellansicht(
					ForecastFilterFactory.getInstance().createFKSchnellansichtForecastauftrag());

			panelQueryAuftrag.createAndSaveAndShowButton("/com/lp/client/res/documents_gear.png",
					LPMain.getInstance().getTextRespectUISPr("fc.position.deltaliste"), MENUE_ACTION_PRINT_DELTALISTE,
					null);
			panelQueryAuftrag.createAndSaveAndShowButton("/com/lp/client/res/printer.png",
					LPMain.getInstance().getTextRespectUISPr("fc.report.liefersituation"),
					MENUE_ACTION_PRINT_LIEFERSITUATION, null);

			panelBottomAuftrag = new PanelForecastauftrag(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("fc.forecastauftraege"),
					panelQueryAuftrag.getSelectedId());
			panelSplitAuftrag = new PanelSplit(getInternalFrame(), panelBottomAuftrag, panelQueryAuftrag, 210);

			setComponentAt(IDX_PANEL_FORECASTAUFTRAG, panelSplitAuftrag);

		} else { // filter refreshen.
			panelQueryAuftrag.setDefaultFilter(ForecastFilterFactory.getInstance()
					.createFKForecastauftraege((Integer) panelQueryLieferadresse.getSelectedId()));
		}

	}

	private void createLieferadresse() throws Throwable {
		if (panelSplitLieferadresse == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryLieferadresse = new PanelQuery(null,
					ForecastFilterFactory.getInstance()
							.createFKLieferadresse((Integer) panelQueryForecast.getSelectedId()),
					QueryParameters.UC_ID_FCLIEFERADRESSE, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("fc.lieferadressen"), true);

			panelBottomLieferadresse = new PanelFclieferadresse(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("fc.lieferadressen"),
					panelQueryLieferadresse.getSelectedId(), this);
			panelSplitLieferadresse = new PanelSplit(getInternalFrame(), panelBottomLieferadresse,
					panelQueryLieferadresse, 210);

			setComponentAt(IDX_PANEL_LIEFERADRESSE, panelSplitLieferadresse);

		} else { // filter refreshen.
			panelQueryLieferadresse.setDefaultFilter(ForecastFilterFactory.getInstance()
					.createFKLieferadresse((Integer) panelQueryForecast.getSelectedId()));
		}

	}

	private void createKopfdaten(Integer key) throws Throwable {
		if (panelDetailForecast == null) {
			panelDetailForecast = new PanelForecast(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"), key);
			setComponentAt(IDX_PANEL_KOPFDATEN, panelDetailForecast);
		}
	}

	private void createPositionen(Integer forecastauftragIId) throws Throwable {
		if (panelSplitPositionen == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER };
			panelQueryPositionen = new PanelQuery(ForecastFilterFactory.getInstance().buildQueryTypesAuswahl(),
					ForecastFilterFactory.getInstance().createFKForecastpositionen(forecastauftragIId),
					QueryParameters.UC_ID_FORECASTPOSITION, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("fc.forecastpositionen"), true);

			panelQueryPositionen.befuellePanelFilterkriterienDirekt(
					ForecastFilterFactory.getInstance().createFKPositionenArtikelnummer(),
					ForecastFilterFactory.getInstance().createFKPositionenBestellnummer());

			panelQueryPositionen.addDirektFilter(ForecastFilterFactory.getInstance().createFKDTextSuchen());

			panelQueryPositionen.createAndSaveAndShowButton("/com/lp/client/res/document_into.png",
					LPMain.getInstance().getTextRespectUISPr("fc.position.xlsimport"),
					MENUE_ACTION_POSITIONEN_XLSIMPORT, null);
			panelQueryPositionen.setMultipleRowSelectionEnabled(true);

			panelBottomPositionen = new PanelForcastposition(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("fc.forecastpositionen"),
					panelQueryPositionen.getSelectedId(), this);
			panelSplitPositionen = new PanelSplit(getInternalFrame(), panelBottomPositionen, panelQueryPositionen, 250);

			setComponentAt(IDX_PANEL_FORECASTPOSITION, panelSplitPositionen);

		} else { // filter refreshen.
			panelQueryPositionen.setDefaultFilter(
					ForecastFilterFactory.getInstance().createFKForecastpositionen(forecastauftragIId));

		}

	}

	private void createLinienabruf(Integer forecastpositionIId) throws Throwable {
		if (panelSplitLinienabruf == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryLinienabruf = new PanelQuery(null,
					ForecastFilterFactory.getInstance().createFKLinienabruf(forecastpositionIId),
					QueryParameters.UC_ID_LINIENABRUF, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("fc.linienabrufe"), true);

			panelBottomLinienabruf = new PanelLinienabruf(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("fc.linienabrufe"), panelQueryLinienabruf.getSelectedId(),
					this);
			panelSplitLinienabruf = new PanelSplit(getInternalFrame(), panelBottomLinienabruf, panelQueryLinienabruf,
					120);

			setComponentAt(IDX_PANEL_LINIENABRUF, panelSplitLinienabruf);

		} else { // filter refreshen.
			panelQueryLinienabruf
					.setDefaultFilter(ForecastFilterFactory.getInstance().createFKLinienabruf(forecastpositionIId));
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

		IDX_PANEL_LIEFERADRESSE =

				reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("fc.lieferadressen"), null, null,
						LPMain.getInstance().getTextRespectUISPr("fc.lieferadressen"));

		IDX_PANEL_FORECASTAUFTRAG =

				reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("fc.forecastauftraege"), null, null,
						LPMain.getInstance().getTextRespectUISPr("fc.forecastauftraege"));

		IDX_PANEL_FORECASTPOSITION =

				reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("fc.forecastpositionen"), null, null,
						LPMain.getInstance().getTextRespectUISPr("fc.forecastpositionen"));

		IDX_PANEL_LINIENABRUF =

				reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("fc.linienabrufe"), null, null,
						LPMain.getInstance().getTextRespectUISPr("fc.linienabrufe"));

		refreshAuswahl();

		panelQueryForecast.eventYouAreSelected(false);

		if ((Integer) panelQueryForecast.getSelectedId() != null) {
			setForecastDto(DelegateFactory.getInstance().getForecastDelegate()
					.forecastFindByPrimaryKey((Integer) panelQueryForecast.getSelectedId()));
		}

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryForecast, ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryForecast.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void refreshTitle(boolean bMitArtikel) throws Throwable {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("fc.forecast"));
		if (this.getSelectedComponent() != null) {
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		}

		if (getForecastDto() != null) {

			String auftrag = "";
			if (panelQueryAuftrag != null && panelQueryAuftrag.getSelectedId() != null) {
				ForecastauftragDto fcaDto = DelegateFactory.getInstance().getForecastDelegate()
						.forecastauftragFindByPrimaryKey((Integer) panelQueryAuftrag.getSelectedId());

				auftrag = Helper.formatDatum(fcaDto.getTAnlegen(), LPMain.getTheClient().getLocUi());

			}

			if (bMitArtikel && panelQueryPositionen != null & panelQueryPositionen.getSelectedId() != null) {

				Integer artikelIId = DelegateFactory.getInstance().getForecastDelegate()
						.forecastpositionFindByPrimaryKey((Integer) panelQueryPositionen.getSelectedId())
						.getArtikelIId();

				auftrag += " / " + DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(artikelIId).formatArtikelbezeichnung();
			}

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, getForecastDto().getCNr() + " " + auftrag);
		}
	}

	public InternalFrameForecast getInternalFrameForecast() {
		return (InternalFrameForecast) getInternalFrame();
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_BEARBEITEN_ERLEDIGEN)) {

			if (getForecastDto() != null) {

				if (getForecastDto().getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)) {

					if (DialogFactory.showMeldung(LPMain.getTextRespectUISPr("fc.forecast.angelegtsetzen"),
							LPMain.getTextRespectUISPr("lp.hint"),
							javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {

						DelegateFactory.getInstance().getForecastDelegate()
								.toggleForecastErledigt(getForecastDto().getIId());

					}
				} else {
					if (DialogFactory.showMeldung(LPMain.getTextRespectUISPr("fc.forecast.erledigtsetzen"),
							LPMain.getTextRespectUISPr("lp.hint"),
							javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {

						DelegateFactory.getInstance().getForecastDelegate()
								.toggleForecastErledigt(getForecastDto().getIId());

					}
				}

				setForecastDto(DelegateFactory.getInstance().getForecastDelegate()
						.forecastFindByPrimaryKey(getForecastDto().getIId()));

				panelQueryForecast.eventYouAreSelected(false);

			}

		} else if (e.getActionCommand().equals(MENU_JOURNAL_BESCHAFFUNG)) {
			String add2Title = LPMain.getTextRespectUISPr("fc.beschaffung");
			getInternalFrame().showReportKriterien(new ReportBeschaffung(getInternalFrame(), null, add2Title));
		} else if (e.getActionCommand().equals(MENU_JOURNAL_GEPLANTERUMSATZ)) {
			String add2Title = LPMain.getTextRespectUISPr("fc.report.geplanterumsatz");
			getInternalFrame().showReportKriterien(new ReportGeplanterUmsatz(getInternalFrame(), null, add2Title));
		} else if (e.getActionCommand().equals(MENU_JOURNAL_UEBERSICHT)) {
			String add2Title = LPMain.getTextRespectUISPr("fc.report.uebersicht");
			getInternalFrame().showReportKriterien(new ReportForecastUebersicht(getInternalFrame(), add2Title));
			// } else if
			// (MENU_MODUL_CODAILY_XLS_IMPORT.equals(e.getActionCommand())) {
			// importCalloffDailyXlsElectrolux();
			// } else if
			// (MENU_MODUL_COWEEKLY_XLS_IMPORT.equals(e.getActionCommand())) {
			// importCalloffWeeklyXlsElectrolux();
			// } else if (MENU_MODUL_LINIENABRUF_ELUXDE_IMPORT.equals(e
			// .getActionCommand())) {
			// importLinienabrufTxtElectroluxDE();
			// } else if
			// (MENU_MODUL_EDIFACT_IMPORT.equals(e.getActionCommand())) {
			// importCalloffEdifact();
		} else if (e.getActionCommand().equals(MENU_MODUL_IMPORT_VMI)) {
			XlsFileOpener xlsOpener = FileOpenerFactory.stklMaterialImportOpenXls(getInternalFrame());
			xlsOpener.doOpenDialog();

			if (!xlsOpener.hasFileChosen())
				return;

			DelegateFactory.getInstance().getForecastDelegate().importiereVMI_XLS(xlsOpener.getFile().getBytes());

		}
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelQueryForecast) {
				// refreshAngebotstklPositionen();
				// setSelectedComponent(panelSplitAngStk);
				// panelDetailAgstkl.eventYouAreSelected(false);
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();
			if (sAspectInfo.equals(MENUE_ACTION_POSITIONEN_XLSIMPORT)) {

				Object[] options = { "Standard", "Epsilon", "Rollierende Planung", "VMI-CC", "VAT", "Zeiss",
						LPMain.getTextRespectUISPr("lp.abbrechen") };

				int selected = JOptionPane.showOptionDialog(null, LPMain.getTextRespectUISPr("fc.import.format"),
						"Format", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
						options[0]);

				if (selected == 0) {
					DialogForecastpositionImportXLS d = new DialogForecastpositionImportXLS(this,
							(Integer) panelQueryAuftrag.getSelectedId());

				} else if (selected == 1 || selected == 2) {
					XlsFileOpener xlsOpener = FileOpenerFactory.stklMaterialImportOpenXls(getInternalFrame());
					xlsOpener.doOpenDialog();

					if (!xlsOpener.hasFileChosen())
						return;

					if (selected == 1) {
						DelegateFactory.getInstance().getForecastDelegate().importiereEpsilon_XLS(
								xlsOpener.getFile().getBytes(), (Integer) panelQueryAuftrag.getSelectedId());
					} else if (selected == 2) {

						Object[] optionsArtikelnummer = { "Artikelnummer", "Referenznummer",
								LPMain.getTextRespectUISPr("lp.abbrechen") };

						int selectedArtikelnummer = JOptionPane.showOptionDialog(null,
								LPMain.getTextRespectUISPr("fc.import.rollierendeplanung.artikelnumer"),
								LPMain.getTextRespectUISPr("lp.frage"), JOptionPane.DEFAULT_OPTION,
								JOptionPane.INFORMATION_MESSAGE, null, optionsArtikelnummer, optionsArtikelnummer[0]);

						if (selectedArtikelnummer == 0) {

							DialogRollierendePlanungImportXLS d = new DialogRollierendePlanungImportXLS(
									xlsOpener.getFile().getBytes(), this, (Integer) panelQueryAuftrag.getSelectedId(),
									false);
							d.setSize(500, 500);
							LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
							d.setVisible(true);

						} else if (selectedArtikelnummer == 1) {
							DialogRollierendePlanungImportXLS d = new DialogRollierendePlanungImportXLS(
									xlsOpener.getFile().getBytes(), this, (Integer) panelQueryAuftrag.getSelectedId(),
									true);
							d.setSize(500, 500);
							LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
							d.setVisible(true);
						}

					}

				} else if (selected == 3) {
					XlsFileOpener xlsOpener = FileOpenerFactory
							.forecastVMICCstklMaterialImportOpenXls(getInternalFrame());
					xlsOpener.doOpenDialog();

					if (!xlsOpener.hasFileChosen())
						return;

					DelegateFactory.getInstance().getForecastDelegate().importiereVMI_CC_XLS(
							xlsOpener.getFile().getBytes(), (Integer) panelQueryAuftrag.getSelectedId());
				} else if (selected == 4) {
					DialogForecastpositionenImportVAT_XLS d = new DialogForecastpositionenImportVAT_XLS(this,
							(Integer) panelQueryAuftrag.getSelectedId());

				} else if (selected == 5) {
					importCSV_Zeiss();

				}

				panelSplitPositionen.eventYouAreSelected(false);
			} else if (sAspectInfo.equals(MENUE_ACTION_PRINT_DELTALISTE)) {
				String add2Title = LPMain.getTextRespectUISPr("fc.position.deltaliste");
				getInternalFrame().showReportKriterien(new ReportForecastDeltaliste(getInternalFrame(),
						(Integer) panelQueryLieferadresse.getSelectedId(), add2Title));

			} else if (sAspectInfo.equals(MENUE_ACTION_PRINT_LIEFERSITUATION)) {
				String add2Title = LPMain.getTextRespectUISPr("fc.report.liefersituation");
				getInternalFrame().showReportKriterien(new ReportLiefersituation(getInternalFrame(),
						(Integer) panelQueryAuftrag.getSelectedId(), add2Title));

			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelDetailForecast) {
				panelQueryForecast.clearDirektFilter();
				Object oKey = panelDetailForecast.getKeyWhenDetailPanel();

				panelQueryForecast.refreshMe(oKey);
			} else if (eI.getSource() == panelBottomAuftrag) {
				Object oKey = panelBottomAuftrag.getKeyWhenDetailPanel();
				panelQueryAuftrag.eventYouAreSelected(false);
				panelQueryAuftrag.setSelectedId(oKey);
				panelSplitAuftrag.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomLieferadresse) {
				Object oKey = panelBottomLieferadresse.getKeyWhenDetailPanel();
				panelQueryLieferadresse.eventYouAreSelected(false);
				panelQueryLieferadresse.setSelectedId(oKey);
				panelSplitLieferadresse.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomPositionen) {
				Object oKey = panelBottomPositionen.getKeyWhenDetailPanel();
				panelQueryPositionen.eventYouAreSelected(false);
				panelQueryPositionen.setSelectedId(oKey);
				panelSplitPositionen.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomLinienabruf) {
				Object oKey = panelBottomLinienabruf.getKeyWhenDetailPanel();
				panelQueryLinienabruf.eventYouAreSelected(false);
				panelQueryLinienabruf.setSelectedId(oKey);
				panelSplitLinienabruf.eventYouAreSelected(false);
			}
		}
		if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {

		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {

		} else if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelQueryForecast) {

				if (panelQueryForecast.getSelectedId() != null) {
					getInternalFrameForecast().setKeyWasForLockMe(panelQueryForecast.getSelectedId() + "");
					setForecastDto(DelegateFactory.getInstance().getForecastDelegate()
							.forecastFindByPrimaryKey((Integer) panelQueryForecast.getSelectedId()));

					if (getForecastDto() != null) {

						getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, getForecastDto().getCNr());
					}
				} else {

					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, (false));
					getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");

				}
			} else if (eI.getSource() == panelQueryAuftrag) {

				Integer iId = (Integer) panelQueryAuftrag.getSelectedId();
				panelBottomAuftrag.setKeyWhenDetailPanel(iId);
				panelBottomAuftrag.eventYouAreSelected(false);
				panelQueryAuftrag.updateButtons();

			} else if (eI.getSource() == panelQueryLieferadresse) {

				Integer iId = (Integer) panelQueryLieferadresse.getSelectedId();
				panelBottomLieferadresse.setKeyWhenDetailPanel(iId);
				panelBottomLieferadresse.eventYouAreSelected(false);
				panelQueryLieferadresse.updateButtons();

			} else if (eI.getSource() == panelQueryPositionen) {
				Integer iId = (Integer) panelQueryPositionen.getSelectedId();
				panelBottomPositionen.setKeyWhenDetailPanel(iId);
				panelBottomPositionen.eventYouAreSelected(false);
				panelQueryPositionen.updateButtons();
			} else if (eI.getSource() == panelQueryLinienabruf) {
				Integer iId = (Integer) panelQueryLinienabruf.getSelectedId();
				panelBottomLinienabruf.setKeyWhenDetailPanel(iId);
				panelBottomLinienabruf.eventYouAreSelected(false);
				panelQueryLinienabruf.updateButtons();
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (eI.getSource() == panelBottomPositionen) {
				panelQueryPositionen.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (eI.getSource() == panelDetailForecast) {

				panelQueryForecast.eventYouAreSelected(false);
				getForecastDto().setIId((Integer) panelQueryForecast.getSelectedId());
				panelDetailForecast.setKeyWhenDetailPanel(panelQueryForecast.getSelectedId());
				this.setSelectedComponent(panelQueryForecast);
				if (panelQueryForecast.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
				}

			} else if (eI.getSource() == panelBottomAuftrag) {
				setKeyWasForLockMe();
				if (panelBottomAuftrag.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryAuftrag.getId2SelectAfterDelete();
					panelQueryAuftrag.setSelectedId(oNaechster);
				}
				panelSplitAuftrag.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomLieferadresse) {
				setKeyWasForLockMe();
				if (panelBottomLieferadresse.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLieferadresse.getId2SelectAfterDelete();
					panelQueryLieferadresse.setSelectedId(oNaechster);
				}
				panelSplitLieferadresse.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomLinienabruf) {
				setKeyWasForLockMe();
				if (panelBottomLinienabruf.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLinienabruf.getId2SelectAfterDelete();
					panelQueryLinienabruf.setSelectedId(oNaechster);
				}
				panelSplitLinienabruf.eventYouAreSelected(false);
			} else if (eI.getSource() == panelBottomPositionen) {
				Object oKey = panelQueryPositionen.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomPositionen.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPositionen.getId2SelectAfterDelete();
					panelQueryPositionen.setSelectedId(oNaechster);
				}
				panelSplitPositionen.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			if (eI.getSource() instanceof AssistentView) {
				getAktuellesPanel().eventYouAreSelected(false);
				refreshTitle(false);
			} else if (eI.getSource() == panelQueryPositionen) {
				panelSplitPositionen.eventYouAreSelected(false);
				refreshTitle(false);
			} else if (eI.getSource() == panelQueryLinienabruf) {
				panelSplitLinienabruf.eventYouAreSelected(false);
				refreshTitle(true);
			} else if (eI.getSource() == panelQueryAuftrag) {
				panelSplitAuftrag.eventYouAreSelected(false);
				refreshTitle(true);
			} else if (eI.getSource() == panelQueryLieferadresse) {
				panelSplitLieferadresse.eventYouAreSelected(false);
				refreshTitle(true);
			}

			super.lPEventItemChanged(eI);
		} else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD || eI.getID() == ItemChangedEvent.ACTION_DELETE) {
			if (eI.getSource() == panelBottomPositionen) {
				panelSplitPositionen.eventYouAreSelected(false);
			}
			if (eI.getSource() == panelBottomLinienabruf) {
				panelSplitLinienabruf.eventYouAreSelected(false);
			}
			if (eI.getSource() == panelBottomAuftrag) {
				panelSplitAuftrag.eventYouAreSelected(false);
			}
			if (eI.getSource() == panelBottomLieferadresse) {
				panelSplitLieferadresse.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelQueryForecast) {
				createKopfdaten(null);
				// wenn es bisher keinen eintrag gibt, die restlichen
				// panels aktivieren
				if (panelQueryForecast.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, true);
				}

				panelDetailForecast.eventActionNew(eI, true, false);
				setSelectedComponent(panelDetailForecast);

			} else if (eI.getSource() == panelQueryAuftrag) {
				panelBottomAuftrag.eventActionNew(eI, true, false);
				panelBottomAuftrag.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitAuftrag);
			} else if (eI.getSource() == panelQueryLieferadresse) {
				panelBottomLieferadresse.eventActionNew(eI, true, false);
				panelBottomLieferadresse.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitLieferadresse);
			} else if (eI.getSource() == panelQueryPositionen) {
				panelBottomPositionen.eventActionNew(eI, true, false);
				panelBottomPositionen.eventYouAreSelected(false);
			} else if (eI.getSource() == panelQueryLinienabruf) {
				panelBottomLinienabruf.eventActionNew(eI, true, false);
				panelBottomLinienabruf.eventYouAreSelected(false);
			}

		}
	}

	/**
	 * einfuegenHV
	 * 
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws Throwable
	 */
	public void einfuegenHV() throws IOException, ParserConfigurationException, SAXException, Throwable {

	}

	/**
	 * Behandle ChangeEvent; zB Tabwechsel oben.
	 * 
	 * @param e ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {

		super.lPEventObjectChanged(e);

		getInternalFrameForecast().setRechtModulweit(sRechtModulweit);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			refreshAuswahl();
			panelQueryForecast.eventYouAreSelected(false);
			panelQueryForecast.updateButtons();
			if (panelQueryForecast.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
			}
			refreshTitle(false);
		} else if (selectedIndex == IDX_PANEL_KOPFDATEN) {
			Integer key = null;
			if (getForecastDto() != null) {
				key = getForecastDto().getIId();
			}
			createKopfdaten(key);
			panelDetailForecast.eventYouAreSelected(false);
			refreshTitle(false);
		} else if (selectedIndex == IDX_PANEL_FORECASTAUFTRAG) {
			createLieferadresse();
			panelSplitLieferadresse.eventYouAreSelected(false);
			if (panelQueryLieferadresse.getSelectedId() == null) {
				DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.info"),
						LPMain.getInstance().getTextRespectUISPr("fc.zuerstlieferadressewaehlen"));

				setSelectedIndex(IDX_PANEL_LIEFERADRESSE);
				lPEventObjectChanged(e);
			}

			createAuftraege();
			panelSplitAuftrag.eventYouAreSelected(false);
			panelQueryAuftrag.updateButtons();
			refreshTitle(false);
		} else if (selectedIndex == IDX_PANEL_LIEFERADRESSE) {
			createLieferadresse();
			panelSplitLieferadresse.eventYouAreSelected(false);
			panelQueryLieferadresse.updateButtons();
			refreshTitle(false);
		} else if (selectedIndex == IDX_PANEL_FORECASTPOSITION) {
			createLieferadresse();
			panelSplitLieferadresse.eventYouAreSelected(false);
			if (panelQueryLieferadresse.getSelectedId() == null) {
				DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.info"),
						LPMain.getInstance().getTextRespectUISPr("fc.zuerstlieferadressewaehlen"));

				setSelectedIndex(IDX_PANEL_LIEFERADRESSE);
				lPEventObjectChanged(e);
				return;
			}

			createAuftraege();

			panelSplitAuftrag.eventYouAreSelected(false);
			if (getPanelQueryForecastauftrag().getSelectedId() != null) {

				if (panelQueryPositionen != null && vorhererSelektierterTab > IDX_PANEL_FORECASTAUFTRAG) {

				} else {
					createPositionen((Integer) getPanelQueryForecastauftrag().getSelectedId());
				}

				// Wenn der Forecastauftrag Freigegeben ist, dann keine
				// aenderungen mehr moeglich
				ForecastauftragDto fcaDto = DelegateFactory.getInstance().getForecastDelegate()
						.forecastauftragFindByPrimaryKey((Integer) getPanelQueryForecastauftrag().getSelectedId());

				if (fcaDto.getStatusCNr().equals(LocaleFac.STATUS_FREIGEGEBEN)) {
					getInternalFrameForecast().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
				}

				panelSplitPositionen.eventYouAreSelected(false);
				panelQueryPositionen.updateButtons();
			} else {
				DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.info"),
						LPMain.getInstance().getTextRespectUISPr("fc.zuerstauftragwaehlen"));

				setSelectedIndex(IDX_PANEL_FORECASTAUFTRAG);
				lPEventObjectChanged(e);
			}
			refreshTitle(false);
		} else if (selectedIndex == IDX_PANEL_LINIENABRUF) {
			createLieferadresse();
			panelSplitLieferadresse.eventYouAreSelected(false);
			createAuftraege();
			panelSplitAuftrag.eventYouAreSelected(false);

			if (getPanelQueryForecastauftrag().getSelectedId() != null) {
				createPositionen((Integer) getPanelQueryForecastauftrag().getSelectedId());
				if (panelQueryPositionen != null && panelQueryPositionen.getSelectedId() != null) {

				} else {

					if (vorhererSelektierterTab >= IDX_PANEL_FORECASTAUFTRAG) {
						createPositionen((Integer) getPanelQueryForecastauftrag().getSelectedId());
					}
				}

				panelQueryPositionen.eventYouAreSelected(false);

				if (panelQueryPositionen.getSelectedId() != null) {

					ForecastauftragDto fcaDto = DelegateFactory.getInstance().getForecastDelegate()
							.forecastauftragFindByPrimaryKey((Integer) getPanelQueryForecastauftrag().getSelectedId());

					if (fcaDto.getStatusCNr().equals(LocaleFac.STATUS_FREIGEGEBEN)) {
						getInternalFrameForecast().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
					}

					createLinienabruf((Integer) panelQueryPositionen.getSelectedId());
					panelSplitLinienabruf.eventYouAreSelected(false);
					panelQueryLinienabruf.updateButtons();
				} else {
					DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.info"),
							LPMain.getInstance().getTextRespectUISPr("fc.zuerstpositionwaehlen"));

					setSelectedIndex(IDX_PANEL_FORECASTPOSITION);
					lPEventObjectChanged(e);
				}

			} else {
				DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.info"),
						LPMain.getInstance().getTextRespectUISPr("fc.zuerstauftragwaehlen"));

				setSelectedIndex(IDX_PANEL_FORECASTAUFTRAG);
				lPEventObjectChanged(e);
			}
			refreshTitle(true);
		}

		vorhererSelektierterTab = getSelectedIndex();

	}

	private void importCSV_Zeiss() throws Throwable {
		HvOptional<CsvFile> csvFile = FileOpenerFactory.forecastZeissImportCsv(this);
		if (csvFile.isPresent()) {

			InputStreamReader input = new InputStreamReader(new FileInputStream(csvFile.get().getFile()),
					StandardCharsets.UTF_8.name());
			CSVParser csvParser = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader()
					.withAllowMissingColumnNames().parse(input);
			int iZeile = 1;

			ArrayList<ForecastImportZeissDto> alZeilenCsv = new ArrayList<ForecastImportZeissDto>();
			TreeMap<Integer, java.util.Date> hmDatumsspalten = new TreeMap<Integer, java.util.Date>();

			try {
			List l = csvParser.getHeaderNames();

			int iSpalteKategorieBezeichnung = l.indexOf("Kategorie Bezeichnung");

			for (int i = iSpalteKategorieBezeichnung + 2; i < l.size(); i++) {

				String woche_monat = (String) l.get(i);

				if (woche_monat.startsWith("W")) {

					String[] teile = woche_monat.split(" ");

					String[] kw_jahr = teile[1].split("\\.");

					Calendar c = Calendar.getInstance();
					c = Helper.cutCalendar(c);
					c.set(Calendar.YEAR, new Integer(kw_jahr[1]));
					c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					c.set(Calendar.WEEK_OF_YEAR, new Integer(kw_jahr[0]));

					hmDatumsspalten.put(i, c.getTime());

				} else if (woche_monat.startsWith("M")) {
					String[] teile = woche_monat.split(" ");

					String[] monat_jahr = teile[1].split("\\.");

					Calendar c = Calendar.getInstance();
					c = Helper.cutCalendar(c);

					c.set(Calendar.YEAR, new Integer(monat_jahr[1]));
					c.set(Calendar.MONTH, new Integer(monat_jahr[0]) - 1);
					c.set(Calendar.DAY_OF_MONTH, 1);

					hmDatumsspalten.put(i, c.getTime());
				}

			}

			for (CSVRecord record : csvParser) {

				iZeile++;

				if (record.size() > 9) {

						String referenznummer = record.get("Material");

						String kategorie = record.get("Kategorie Bezeichnung");

						if (kategorie != null && kategorie.equals("Forecast")) {

							ForecastImportZeissDto zeileDto = new ForecastImportZeissDto(referenznummer, iZeile);

							Iterator it = hmDatumsspalten.keySet().iterator();
							while (it.hasNext()) {

								Integer iSpalte = (Integer) it.next();

								Date datum = hmDatumsspalten.get(iSpalte);

								String menge = record.get(iSpalte);

								if (menge != null && menge.length() > 0) {
									menge = menge.trim().replaceFirst(",", ".");
									zeileDto.addDatumMenge(datum, new BigDecimal(menge));
								}

							}

							alZeilenCsv.add(zeileDto);
						}

					

				}
			}

			DelegateFactory.getInstance().getForecastDelegate().importiereForecastpositionZEISS_CSV(alZeilenCsv,
					(Integer) getPanelQueryForecastauftrag().getSelectedId());
			panelSplitPositionen.eventYouAreSelected(false);
			} catch (IllegalArgumentException e) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("fc.forecast.zeiss.import.fehlende.spalten"));
			}

		}
	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);

			JMenu modul = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_MODUL);

			boolean bSchreibrecht = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_FC_FORECAST_CUD);

			if (bSchreibrecht) {
				JMenu menuImport = new WrapperMenu("lp.import", this);
				JMenuItem menuitemVMIImport = new JMenuItem(LPMain.getTextRespectUISPr("fc.import.vmi"));
				menuitemVMIImport.addActionListener(this);
				menuitemVMIImport.setActionCommand(MENU_MODUL_IMPORT_VMI);

				menuImport.add(menuitemVMIImport);
				modul.add(new JSeparator(), 0);
				modul.add(menuImport, 0);

			}

			JMenu jmBearbeiten = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_BEARBEITEN);
			WrapperMenuItem menuItemBearbeitenManuellErledigen = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("fc.menu.forecast.manuellerledigen"), RechteFac.RECHT_FC_FORECAST_CUD);
			menuItemBearbeitenManuellErledigen.addActionListener(this);
			menuItemBearbeitenManuellErledigen.setActionCommand(MENU_BEARBEITEN_ERLEDIGEN);
			jmBearbeiten.add(menuItemBearbeitenManuellErledigen, 0);

			JMenu journal = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_JOURNAL);

			WrapperMenuItem menuItemGeplanterUmsatz = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("fc.report.geplanterumsatz"), null);
			menuItemGeplanterUmsatz.addActionListener(this);
			menuItemGeplanterUmsatz.setActionCommand(MENU_JOURNAL_GEPLANTERUMSATZ);
			journal.add(menuItemGeplanterUmsatz, 0);

			WrapperMenuItem menuItemUebersicht = new WrapperMenuItem(LPMain.getTextRespectUISPr("fc.report.uebersicht"),
					null);
			menuItemUebersicht.addActionListener(this);
			menuItemUebersicht.setActionCommand(MENU_JOURNAL_UEBERSICHT);
			journal.add(menuItemUebersicht, 0);

			WrapperMenuItem menuItemBeschaffung = new WrapperMenuItem(LPMain.getTextRespectUISPr("fc.beschaffung"),
					null);
			menuItemBeschaffung.addActionListener(this);
			menuItemBeschaffung.setActionCommand(MENU_JOURNAL_BESCHAFFUNG);
			journal.add(menuItemBeschaffung, 0);

		}

		return wrapperMenuBar;
	}

	public ForecastDto getForecastDto() {
		return forecastDto;
	}

	public void setForecastDto(ForecastDto forecastDto) {
		this.forecastDto = forecastDto;
	}

}
