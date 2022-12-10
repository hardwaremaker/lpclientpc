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
package com.lp.client.finanz;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HvCreatingCachingProvider;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.open.CsvFile;
import com.lp.client.frame.filechooser.open.FileOpenerFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.finanz.service.ExportdatenDto;
import com.lp.server.finanz.service.ExportlaufDto;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeVersion;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.HvOptional;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.HVPDFExporter;
import com.lp.util.Helper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Panels fuer den Fibu-Export
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 30.01.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: robert $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/01/19 11:47:17 $
 */
public class TabbedPaneExport extends TabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryExportlauf = null;
	private PanelQuery panelQueryExportdaten = null;
	private PanelDialogKriterienExportlauf pdKriterienExportlauf = null;

	private ExportlaufDto exportlaufDto = null;

	private static final int IDX_EXPORTLAUF = 0;
	private static final int IDX_EXPORTDATEN = 1;

	private final static String MENU_ACTION_EXPORT = "menu_action_export";

	private final static String MENU_ACTION_IMPORT_OP = "menu_action_import_op";

	private final static String MENU_ACTION_EXPORT_SACHKONTEN = "menu_action_export_sachkonten";
	private final static String MENU_ACTION_EXPORT_DEBITORENKONTEN = "menu_action_export_debitorenkonten";
	private final static String MENU_ACTION_EXPORT_KREDITORENKONTEN = "menu_action_export_kreditorenkonten";

	private final static String ACTION_SPECIAL_REMOVE_EXPORTLAUF = PanelBasis.LEAVEALONE
			+ "_action_special_removeexportlauf";
	private final static String ACTION_SPECIAL_REMOVE_EXPORTBELEG_EINZELN = PanelBasis.ALWAYSENABLED
			+ "_action_special_removeexportbeleg_einzeln";

	public TabbedPaneExport(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain
				.getTextRespectUISPr("finanz.tab.unten.export.title"));
		jbInit();
		initComponents();
	}

	public ExportlaufDto getExportlaufDto() {
		return exportlaufDto;
	}

	public void setExportlaufDto(ExportlaufDto exportlaufDto) throws Throwable {
		this.exportlaufDto = exportlaufDto;
		String sTitle = null;
		if (getExportlaufDto() != null) {
			getPanelQueryExportdaten().setDefaultFilter(
					FinanzFilterFactory.getInstance().createFKExportdaten(
							getExportlaufDto().getIId()));
			sTitle = Helper.formatTimestamp(getExportlaufDto().getTAendern(),
					LPMain.getTheClient().getLocUi());
			LPButtonAction item1 = (LPButtonAction) getPanelQueryExportlauf()
					.getHmOfButtons().get(ACTION_SPECIAL_REMOVE_EXPORTLAUF);
			item1.getButton().setEnabled(true);
		} else {
			sTitle = "";
			LPButtonAction item1 = (LPButtonAction) getPanelQueryExportlauf()
					.getHmOfButtons().get(ACTION_SPECIAL_REMOVE_EXPORTLAUF);
			item1.getButton().setEnabled(false);
		}
		getInternalFrame()
				.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitle);
	}

	private void jbInit() throws Throwable {
		// Tab 1: Exportlaeufe
		insertTab(
				LPMain.getTextRespectUISPr("finanz.tab.oben.exportlauf.title"),
				null,
				null,
				LPMain.getTextRespectUISPr("finanz.tab.oben.exportlauf.tooltip"),
				IDX_EXPORTLAUF);
		insertTab(LPMain.getTextRespectUISPr("finanz.tab.oben.belege.title"),
				null, null,
				LPMain.getTextRespectUISPr("finanz.tab.oben.belege.title"),
				IDX_EXPORTDATEN);
		setSelectedComponent(getPanelQueryExportlauf());
		// refresh
		getPanelQueryExportlauf().eventYouAreSelected(false);
		// damit gleich einer selektiert ist
		ItemChangedEvent it = new ItemChangedEvent(getPanelQueryExportlauf(),
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);
		// Listener
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	/**
	 * 4 Verarbeite unsere eigenen Itemevents die von anderen Panels, Dialogen,
	 * ... kommen.
	 * 
	 * @param e
	 *            ItemChangedEvent
	 * @throws Throwable
	 */
	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryExportlauf) {
				Object key = panelQueryExportlauf.getSelectedId();
				// nur wechseln wenns auch einen gibt
				if (key != null) {
					ExportlaufDto exportlaufDto = DelegateFactory.getInstance()
							.getFibuExportDelegate()
							.exportlaufFindByPrimaryKey((Integer) key);
					setExportlaufDto(exportlaufDto);
					setSelectedComponent(getPanelQueryExportdaten());
					updatePanelQueryExportdaten();
				}
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryExportlauf) {
				Object key = panelQueryExportlauf.getSelectedId();
				if (key != null) {
					ExportlaufDto exportlaufDto = DelegateFactory.getInstance()
							.getFibuExportDelegate()
							.exportlaufFindByPrimaryKey((Integer) key);
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_EXPORTLAUF, true);
					setExportlaufDto(exportlaufDto);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_EXPORTLAUF, false);
					setExportlaufDto(null);
				}
				panelQueryExportlauf.updateButtons();
			} else if (e.getSource() == panelQueryExportdaten) {
				panelQueryExportdaten.updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryExportlauf) {
				getInternalFrame().showPanelDialog(getPdKriterienExportlauf());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_PRINT) {
			if (e.getSource() == getPanelQueryExportlauf()) {
				if (this.getExportlaufDto() != null) {

				}
			} else if (e.getSource() == getPanelQueryExportdaten()) {

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			if (e.getSource() == getPanelQueryExportlauf()) {
				if (getExportlaufDto() != null) {
					try {
						// Das Loeschen muss bestaetigt werden
						Object pattern[] = { Helper.formatDatum(
								getExportlaufDto().getTStichtag(), LPMain
										.getTheClient().getLocUi()) };
						MessageFormat mf = new MessageFormat(
								LPMain.getTextRespectUISPr("fb.frage.exportlaufloeschen"));
						boolean bLoeschen = DialogFactory
								.showModalJaNeinDialog(getInternalFrame(),
										mf.format(pattern),
										LPMain.getTextRespectUISPr("lp.frage"));
						// Wenn der Exportlauf aelter als 2 Wochen ist, dann
						// noch eine Meldung
						if (bLoeschen
								&& Helper.getDifferenzInTagen(
										new java.util.Date(System
												.currentTimeMillis()),
										getExportlaufDto().getTStichtag()) > 14) {
							bLoeschen = DialogFactory
									.showModalJaNeinDialog(
											getInternalFrame(),
											LPMain.getTextRespectUISPr("fb.frage.exportlaufloeschen2"),
											LPMain.getTextRespectUISPr("lp.frage"));
						}
						if (bLoeschen) {
							DelegateFactory
									.getInstance()
									.getFibuExportDelegate()
									.nehmeExportlaufZurueckUndLoescheIhn(
											getExportlaufDto().getIId());
							// nach dem Loeschen ein Refresh
							setExportlaufDto(null);
							getPanelQueryExportlauf()
									.eventYouAreSelected(false);
						}
					} catch (Throwable ex) {
						handleExportExceptions(ex);
					}
				}
			} else if (e.getSource() == getPanelQueryExportdaten()) {
				if (getPanelQueryExportdaten().getSelectedId() != null) {
					try {
						boolean bLoeschen = DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										LPMain.getTextRespectUISPr("fb.frage.diesenbelegausexportentfernen"),
										LPMain.getTextRespectUISPr("lp.frage"));
						if (bLoeschen) {
							DelegateFactory
									.getInstance()
									.getFibuExportDelegate()
									.removeExportdaten(
											(Integer) getPanelQueryExportdaten()
													.getSelectedId());
							getPanelQueryExportdaten().eventYouAreSelected(
									false);
						}
					} catch (Throwable ex) {
						handleExportExceptions(ex);
					}
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == getPdKriterienExportlauf()) {
				createExportlauf();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == pdKriterienExportlauf) {
				getPanelQueryExportlauf().eventYouAreSelected(false);
			}
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_EXPORTLAUF) {
			panelQueryExportlauf.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_EXPORTDATEN) {
			getPanelQueryExportdaten().eventYouAreSelected(false);
			updatePanelQueryExportdaten();
		}
	}

	private FilterKriterium[] buildFiltersExportlauf() throws Throwable {
		FilterKriterium[] filter = null;
		ExportlaufDto exportlaufDto = getExportlaufDto();
		if (exportlaufDto != null) {
			filter = FinanzFilterFactory.getInstance().createFKExportdaten(
					exportlaufDto.getIId());
		}
		return filter;
	}

	private void updatePanelQueryExportdaten() throws Throwable {
		getPanelQueryExportdaten().setDefaultFilter(buildFiltersExportlauf());
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_ACTION_EXPORT)) {
			getInternalFrame().showPanelDialog(getPdKriterienExportlauf());
		}
		if (e.getActionCommand().equals(MENU_ACTION_IMPORT_OP)) {
			importOffenePosten();
		}
		if (e.getActionCommand().equals(MENU_ACTION_EXPORT_DEBITORENKONTEN)) {
			JDialog dialog = new DialogKontoExport(new KontoExporterDebitoren(), getInternalFrame());
			dialog.setVisible(true);
		}
		if (e.getActionCommand().equals(MENU_ACTION_EXPORT_KREDITORENKONTEN)) {
			JDialog dialog = new DialogKontoExport(new KontoExporterKreditoren(), getInternalFrame());
			dialog.setVisible(true);
		}
		if (e.getActionCommand().equals(MENU_ACTION_EXPORT_SACHKONTEN)) {
			exportiereKonten(new KontoExporterSachkonten());
		}
	}

	private void importOffenePosten() throws Throwable {
		HvOptional<CsvFile> csvFile = FileOpenerFactory.finanzOffenePostenImportCsv(this);
		if (csvFile.isPresent()) {
			List<String[]> al = csvFile.get().read();
			if (al.size() > 0) {
				String err = DelegateFactory.getInstance()
						.getFibuExportDelegate().importiereOffenePosten(al);
				if (err.length() > 0) {
//					JTextArea textArea = new JTextArea(err) ;
//					JScrollPane scrollPane = new JScrollPane(textArea) ;
//					textArea.setLineWrap(true) ;
//					textArea.setWrapStyleWord(true) ;

					JList listArea = new JList(err.split("\n")) ;
					JScrollPane scrollPane = new JScrollPane(listArea) ;
					scrollPane.setPreferredSize(new Dimension(600, 300));
					JOptionPane.showMessageDialog(this, scrollPane, "Fehler beim Import", JOptionPane.ERROR_MESSAGE);
//					JOptionPane.showMessageDialog(this, err,
//							"Fehler beim Import", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this,
							"Daten erfolgreich importiert",
							"Offene Posten Import",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}

	private void createExportlauf() throws Throwable {
		try {
			// Kriterien aus dem Dialogpanel holen
			FibuExportKriterienDto exportKriterienDto = new FibuExportKriterienDto();
			// Stichtag
			exportKriterienDto.setDStichtag(getPdKriterienExportlauf()
					.getTStichtag());
			// Belege ausserhalb Gueltigheitszeitraum
			exportKriterienDto
					.setBAuchBelegeAusserhalbGueltigkeitszeitraum(getPdKriterienExportlauf()
							.getBExportiereBelegeAusserhalbGueltigkeitszeitraum());
			exportKriterienDto
					.setBBelegeAusserhalbGueltigkeitszeitraumAlsExportiertMarkieren(getPdKriterienExportlauf()
							.getBBelegeAusserhalbGueltigkeitszeitraumNurMarkieren());
			// Belegart
			exportKriterienDto.setSBelegartCNr(getPdKriterienExportlauf()
					.getSBelegartCNr());
			String data = DelegateFactory.getInstance().getFibuExportDelegate()
					.exportiereBelege(exportKriterienDto);
			saveExportFile(getBelegExporter(getPdKriterienExportlauf().getSBelegartCNr()), data);
		} catch (Throwable t) {
			handleExportExceptions(t);
		} finally {
			// immer refresh (damit auch nach einem aufgetretenen fehler)
			getPanelQueryExportlauf().eventYouAreSelected(false);
			// den letzten (=neuesten) selektieren
			Integer iIdLetzter = DelegateFactory.getInstance()
					.getFibuExportDelegate().exportlaufFindLetztenExportlauf();
			getPanelQueryExportlauf().setSelectedId(iIdLetzter);
			if (iIdLetzter != null) {
				ExportlaufDto exportlaufDto = DelegateFactory.getInstance()
						.getFibuExportDelegate()
						.exportlaufFindByPrimaryKey(iIdLetzter);
				setExportlaufDto(exportlaufDto);
			}
		}
	}

	private IBelegExporter getBelegExporter(String sBelegartCNr) throws Exception {
		if (sBelegartCNr.equals(LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
			return new EingangsrechnungExporter();
		} else if (sBelegartCNr.equals(LocaleFac.BELEGART_RECHNUNG)) {
			return new RechnungExporter();
		} else if (sBelegartCNr.equals(LocaleFac.BELEGART_GUTSCHRIFT)) {
			return new GutschriftExporter();
		}
		
		throw new Exception("Unbekannte Belegart '" + sBelegartCNr + "'");
	}

	private PanelQuery getPanelQueryExportlauf() throws Throwable {
		if (panelQueryExportlauf == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			FilterKriterium[] filters = SystemFilterFactory.getInstance()
					.createFKMandantCNr();

			panelQueryExportlauf = new PanelQuery(null, filters,
					QueryParameters.UC_ID_EXPORTLAUF, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.auswahl"), true);
			panelQueryExportlauf.createAndSaveAndShowButton(
					"/com/lp/client/res/delete2.png",
					LPMain.getTextRespectUISPr("fb.exportlaufloeschen"),
					ACTION_SPECIAL_REMOVE_EXPORTLAUF,
					RechteFac.RECHT_FB_DARF_EXPORT_ZURUECKNEHMEN);
			setComponentAt(IDX_EXPORTLAUF, panelQueryExportlauf);
		}
		return panelQueryExportlauf;
	}

	private PanelQuery getPanelQueryExportdaten() throws Throwable {
		if (panelQueryExportdaten == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };
			panelQueryExportdaten = new PanelQuery(null, null,
					QueryParameters.UC_ID_EXPORTDATEN, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getTextRespectUISPr("finanz.tab.oben.belege.title"),
					true);
			panelQueryExportdaten
					.createAndSaveAndShowButton(
							"/com/lp/client/res/delete2.png",
							LPMain.getTextRespectUISPr("fb.belegausexportlaufloeschen"),
							ACTION_SPECIAL_REMOVE_EXPORTBELEG_EINZELN,
							RechteFac.RECHT_FB_CHEFBUCHHALTER);
			setComponentAt(IDX_EXPORTDATEN, panelQueryExportdaten);
		}
		return panelQueryExportdaten;
	}

	protected JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wmb = new WrapperMenuBar(this);
		// ------------------------------------------------------------------------
		// Modul - Menu
		// ------------------------------------------------------------------------
		JMenu jmModul = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_MODUL);
		// Export
		WrapperMenuItem menueItemExport = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fb.menu.export"),
				RechteFac.RECHT_FB_FINANZ_CUD);
		menueItemExport.addActionListener(this);
		menueItemExport.setActionCommand(MENU_ACTION_EXPORT);
		jmModul.add(menueItemExport, 0);

		// Import
		WrapperMenuItem menueItemImportOp = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fb.menu.import.op"),
				RechteFac.RECHT_FB_FINANZ_CUD);
		menueItemImportOp.addActionListener(this);
		menueItemImportOp.setActionCommand(MENU_ACTION_IMPORT_OP);
		jmModul.add(menueItemImportOp, 1);

		jmModul.add(new JSeparator(), 2);

		// ------------------------------------------------------------------------
		// Journal - Menu
		// ------------------------------------------------------------------------
		JMenu jmJournal = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_JOURNAL);
		// Sachkonten - Export
		WrapperMenuItem menueItemExportSachkonten = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fb.menu.export.sachkonten"),
				RechteFac.RECHT_FB_FINANZ_CUD);
		menueItemExportSachkonten.addActionListener(this);
		menueItemExportSachkonten
				.setActionCommand(MENU_ACTION_EXPORT_SACHKONTEN);
		jmJournal.add(menueItemExportSachkonten, 0);
		// Debitorenkonten - Export
		WrapperMenuItem menueItemExportDebitorenkonten = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fb.menu.export.debitorenkonten"),
				RechteFac.RECHT_FB_FINANZ_CUD);
		menueItemExportDebitorenkonten.addActionListener(this);
		menueItemExportDebitorenkonten
				.setActionCommand(MENU_ACTION_EXPORT_DEBITORENKONTEN);
		jmJournal.add(menueItemExportDebitorenkonten, 1);
		// Kreditorenkonten - Export
		WrapperMenuItem menueItemExportKreditorenkonten = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("fb.menu.export.kreditorenkonten"),
				RechteFac.RECHT_FB_FINANZ_CUD);
		menueItemExportKreditorenkonten.addActionListener(this);
		menueItemExportKreditorenkonten
				.setActionCommand(MENU_ACTION_EXPORT_KREDITORENKONTEN);
		jmJournal.add(menueItemExportKreditorenkonten, 2);

		return wmb;
	}

	private void handleExportExceptions(Throwable t) throws Throwable {
		if (t instanceof ExceptionLP) {
			ExceptionLP e = (ExceptionLP) t;
			List<?> a = e.getAlInfoForTheClient();
			StringBuffer sb = new StringBuffer();
			if (a != null && !a.isEmpty()) {
				for (Iterator<?> iter = a.iterator(); iter.hasNext();) {
					String item = (String) iter.next();
					sb.append(item + "\n");
				}
			}
			String sToken = null;
			switch (e.getICode()) {
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_DEBITORENKONTO_NICHT_DEFINIERT: {
				sToken = "finanz.error.debitorenkontonichtdefiniert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KREDITORENKONTO_NICHT_DEFINIERT: {
				sToken = "finanz.error.kreditorenkontonichtdefiniert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_UST_KONTO_NICHT_DEFINIERT: {
				sToken = "finanz.error.ustkontonichtdefiniert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_EINGANGSRECHNUNG_IST_GELOCKT: {
				sToken = "finanz.error.ergesperrt";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_RECHNUNG_IST_GELOCKT: {
				sToken = "finanz.error.regesperrt";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_GUTSCHRIFT_IST_GELOCKT: {
				sToken = "finanz.error.gsgesperrt";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KOSTENSTELLE_HAT_KEIN_SACHKONTO: {
				sToken = "finanz.error.kostenstellehatkeinsachkonto";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_EINGANGSRECHNUNG_NICHT_VOLLSTAENDIG_KONTIERT: {
				sToken = "finanz.error.eristnichtvollstaendigkontiert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_ES_DARF_NUR_DER_LETZTE_GELOESCHT_WERDEN: {
				sToken = "finanz.error.esdarfnurderletzteexportlaufgeloeschtwerden";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KONTOLAENDERART_NICHT_DEFINIERT: {
				sToken = "finanz.error.kontolaenderartnichtdefiniert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_LAENDERART_NICHT_FESTSTELLBAR_FUER_PARTNER: {
				sToken = "finanz.error.laenderartkannnichtfestgestelltwerden";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_IST_NOCH_NICHT_AKTIVIERT: {
				sToken = "finanz.error.belegistnochnichtaktiviert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_AUSGANGSRECHNUNG_NICHT_VOLLSTAENDIG_KONTIERT: {
				sToken = "finanz.error.belegistnichtvollstaendigkontiert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_LIEFERANTENRECHNUNGSNUMMER_FEHLT: {
				sToken = "finanz.error.lieferantenrechnungsnummerfehlt";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_STICHTAG_NICHT_DEFINIERT: {
				sToken = "finanz.error.stichtagnichtdefiniert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_LIEGT_AUSSERHALB_GUELIGEM_EXPORTZEITRAUM: {
				sToken = "finanz.error.belegausserhalbgueltigemzeitraum";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_FINANZAMT_NICHT_VOLLSTAENDIG_DEFINIERT: {
				sToken = "finanz.error.finanzamtnichtvollstaendigdefiniert";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KONTO_HAT_KEIN_FINANZAMT: {
				sToken = "finanz.error.kontohatkeinfinanzamt";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_SALDO_UNGLEICH_NULL: {
				sToken = "finanz.error.saldoungleichnull";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_ARTIKEL_KEINE_ARTIKELGRUPPE: {
				sToken = "finanz.export.error.keineartikelgruppe";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_WAEHRUNG_NICHT_GEFUNDEN: {
				sToken = "finanz.error.waehrungnichtgefunden";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KORREKTURBUCHUNG_ZUHOCH: {
				sToken = "finanz.error.export.korrekturzuhoch";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEINKURS_ZUDATUM: {
				sToken = "finanz.error.keinkurs";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE: {
				sToken = "finanz.error.keinmwstcode";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_KONTO_FUER_ARTIKELGRUPPE: {
				sToken = "finanz.error.keinkontofuerartikelgruppe";
			}
				break;
			case EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_UST_KONTO_DEFINIERT: {
				sToken = "finanz.error.keinustkonto";
			}
				break;
			default: {
				throw e;
			}
			}
			String sText = LPMain.getTextRespectUISPr(sToken);
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					sText + "\n" + sb.toString());
		} else {
			throw t;
		}
	}

	private ParametermandantDto getParametermandantFinanz(String parameter) throws ExceptionLP, Throwable {
		return DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FINANZ, parameter);
	}
	private String getParameterWertFinanz(String parameter) throws ExceptionLP, Throwable {
		ParametermandantDto paramDto = getParametermandantFinanz(parameter);
		return paramDto != null ? paramDto.getCWert() : null;
	}
	
	private void saveExportFile(IBelegExporter belegExporter, String data) throws ExceptionLP, Throwable {
		if (data == null) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hint"),
					LPMain.getTextRespectUISPr("fb.export.keinebelegezuexportieren"));
			return;
		}
		
		// Id des erzeugten Exportlaufs holen
		Integer iIdLetzterExportlauf = DelegateFactory.getInstance()
				.getFibuExportDelegate().exportlaufFindLetztenExportlauf();
		ParametermandantDto pASCII = getParametermandantFinanz(
				ParameterFac.PARAMETER_FINANZ_EXPORT_ASCII);
		Boolean bASCII = false;
		if (pASCII != null) {
			bASCII = (Boolean) pASCII.getCWertAsObject();
		}

		boolean bSaved = LPMain.getInstance()
				.saveFile(getInternalFrame(), belegExporter.getExportPath(),
						data.getBytes("windows-1252"), bASCII);
		if (!bSaved) {
			// Falls nicht gespeichert werden konnte, wird der Exportlauf
			// zurueckgenommen
			DelegateFactory.getInstance().getFibuExportDelegate()
					.nehmeExportlaufZurueckUndLoescheIhn(
							iIdLetzterExportlauf);
			return;
		}
		
		saveExportDokumente(belegExporter, iIdLetzterExportlauf);

		DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
					LPMain.getTextRespectUISPr("fb.export.datensichern2"));
	}
	
	private boolean exportDokumente() {
		try {
			String paramMitBelegen = getParameterWertFinanz(ParameterFac.PARAMETER_FINANZ_EXPORT_MIT_BELEGEN);
			return "1".equals(paramMitBelegen);
		} catch (Throwable e) {
			myLogger.error(e);
		}
		return false;
	}
	
	private ArrayList<JCRDocDto> getJcrDocs(JCRDocDto jcr) {
		ArrayList<JCRDocDto> al = new ArrayList<JCRDocDto>();
		List<DocNodeBase> children;
		try {
			children = DelegateFactory.getInstance().getJCRDocDelegate()
					.getDocNodeChildrenFromNode(jcr.getDocPath());
			for (DocNodeBase docNode : children) {
				HvOptional<JCRDocDto> fetchedDoc = HvOptional.ofNullable(
						DelegateFactory.getInstance().getJCRDocDelegate()
						.getJCRDocDtoFromNode(jcr.getDocPath().getDeepCopy().add(docNode)));
				if (fetchedDoc.isPresent()) {
					al.add(fetchedDoc.get());
				}
			}
		} catch (Throwable e) {
			myLogger.error("Fehler beim Holen der Dokumente fuer Beleg " + jcr.getsBelegnummer(), e);
		}

		return al;
	}

	private JCRDocDto getLastJcrVersion(JCRDocDto jcr) throws ExceptionLP, Throwable {
		DocNodeVersion docVersion = DelegateFactory.getInstance().getJCRDocDelegate().getLastJcrDocVersion(jcr);
		return docVersion != null ? docVersion.getJCRDocDto() : null;
	}

	private void saveAsPdf(String exportDir, JCRDocDto jcrdocDto, String kennung) {
		try {
			String filename = kennung
					+ jcrdocDto.getsBelegnummer().replaceAll("[^a-zA-Z0-9]", "_") + ".pdf";
			File file = new File(exportDir, filename);
			saveAsPdf(file, jcrdocDto, kennung);
		} catch (Exception e) {
			myLogger.error("Fehler beim Speichern des Exportdokuments '" + kennung
					+ jcrdocDto.getsBelegnummer() + "'", e);
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					"Fehler beim Speichern des Exportdokuments " + kennung
							+ jcrdocDto.getsBelegnummer() + "\n"
							+ e.getMessage());
		}
	}
	
	private void saveAsPdf(File file, JCRDocDto jcrdocDto, String kennung) throws ClassNotFoundException, IOException, JRException {
		ByteArrayInputStream bStream = new ByteArrayInputStream(
				jcrdocDto.getbData());
		ObjectInputStream oStream = new ObjectInputStream(bStream);
		JasperPrint jPrint = (JasperPrint) oStream.readObject();
		HVPDFExporter exporter = new HVPDFExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
		exporter.exportReport();
	}

	private void saveAs(String exportDir, JCRDocDto jcrdocDto, String kennung) {
		try {
			String filename = kennung + jcrdocDto.getsBelegnummer().replaceAll("[^a-zA-Z0-9]", "_") 
					+ "_" + jcrdocDto.getsFilename();
			File file = new File(exportDir, filename);
			saveAs(file, jcrdocDto, kennung);
		} catch (Exception e) {
			myLogger.error("Fehler beim Speichern des Exportdokuments '" + kennung
					+ jcrdocDto.getsBelegnummer() + "'", e);
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					"Fehler beim Speichern des Exportdokuments " + kennung
							+ jcrdocDto.getsBelegnummer() + "\n"
							+ e.getMessage());
		}
	}

	private void saveAs(File file, JCRDocDto jcrdocDto, String kennung) throws IOException {
		FileOutputStream fo = new FileOutputStream(file);
		fo.write(jcrdocDto.getbData());
		fo.flush();
		fo.close();
	}

	private void exportiereKonten(KontoExporter kontoExporter) throws Throwable {
		kontoExporter.exportAndSaveKonten(getInternalFrame(), false);
	}

	private PanelDialogKriterienExportlauf getPdKriterienExportlauf()
			throws Throwable {
		if (pdKriterienExportlauf == null) {
			pdKriterienExportlauf = new PanelDialogKriterienExportlauf(
					getInternalFrame(),
					LPMain.getTextRespectUISPr("fb.kriterien.exportlauf"));
		}
		return pdKriterienExportlauf;
	}

	private ExportdatenDto[] holeExportdatenDtos(Integer exportlaufIId, String belegartCnr) throws ExceptionLP, Throwable {
			return DelegateFactory.getInstance().getFibuExportDelegate()
					.exportdatenFindByExportlaufIIdBelegartCNr(exportlaufIId, belegartCnr);
	}
	
	private PrintInfoDto holePrintInfo(Integer belegIId, Integer usecaseIId) throws ExceptionLP, Throwable {
		return DelegateFactory.getInstance().getJCRDocDelegate()
				.getPathAndPartnerAndTable(belegIId, usecaseIId);
	}
	
	private void saveExportDokumente(IBelegExporter belegExporter, Integer exportlaufIId) {
		if (!exportDokumente()) return;
		
		try {
			ExportdatenDto[] exportdaten = holeExportdatenDtos(exportlaufIId, belegExporter.getBelegartCNr());
			
			for (ExportdatenDto data : exportdaten) {
				saveExportDokumenteOfBeleg(belegExporter, data.getIBelegiid());
			}
		} catch (Throwable t) {
			myLogger.error(t);
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					"Fehler beim Exportieren der Dokumente der Belege mit Belegart " 
							+ belegExporter.getBelegartCNr());
		}
	}

	private void saveExportDokumenteOfBeleg(IBelegExporter belegExporter, Integer belegId) throws Throwable {
		List<JCRDocDto> jcrDocs = belegExporter.getJCRDocs(belegId);
		for (JCRDocDto doc : jcrDocs) {
			JCRDocDto docLatestVersion = getLastJcrVersion(doc);
			if (docLatestVersion == null) continue;
			
			docLatestVersion = DelegateFactory.getInstance().getJCRDocDelegate().getData(docLatestVersion);
			belegExporter.saveDoc(docLatestVersion);
		}
	}
	
	public JCRDocDto setupDefaultJCRDocDto(PrintInfoDto printInfo) {
		JCRDocDto doc = new JCRDocDto();
		doc.setDocPath(printInfo.getDocPath());
		doc.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		doc.setlPartner(printInfo.getiId());
		doc.setsTable(printInfo.getTable());
		return doc;
	}

	private interface IBelegExporter {
		String getBelegartCNr();
		String getExportPath() throws ExceptionLP, Throwable;
		void saveDoc(JCRDocDto doc) throws ExceptionLP, Throwable;
		Integer getUsecaseId();
		List<JCRDocDto> getJCRDocs(Integer belegIId) throws ExceptionLP, Throwable;
	}
	
	private class RechnungExporter implements IBelegExporter {

		public String getBelegartCNr() {
			return LocaleFac.BELEGART_RECHNUNG;
		}
		
		public String getExportDirPath() throws ExceptionLP, Throwable {
			return new File(getExportPath()).getParent();
		}

		@Override
		public String getExportPath() throws ExceptionLP, Throwable {
			return getParameterWertFinanz(ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_RECHNUNG);
		}

		public JCRDocDto setupJCRDocDto(PrintInfoDto printInfo) {
			JCRDocDto doc = setupDefaultJCRDocDto(printInfo);
			doc.setDocPath(printInfo.getDocPath().add(
					new DocNodeFile(RechnungReportFac.REPORT_RECHNUNG)));
			return doc;
		}

		@Override
		public void saveDoc(JCRDocDto doc) throws ExceptionLP, Throwable {
			if (doc == null || doc.getbData() == null) return;
			saveAsPdf(getExportDirPath(), doc, "RE");
		}

		@Override
		public Integer getUsecaseId() {
			return QueryParameters.UC_ID_RECHNUNG;
		}

		@Override
		public List<JCRDocDto> getJCRDocs(Integer belegIId) throws ExceptionLP, Throwable {
			PrintInfoDto printInfo = holePrintInfo(belegIId, getUsecaseId());
			List<JCRDocDto> docList = new ArrayList<JCRDocDto>();
			docList.add(setupJCRDocDto(printInfo));
			return docList;
		}
	}
	
	private class GutschriftExporter extends RechnungExporter {
		@Override
		public String getBelegartCNr() {
			return LocaleFac.BELEGART_GUTSCHRIFT;
		}
		
		@Override
		public String getExportPath() throws ExceptionLP, Throwable {
			return getParameterWertFinanz(ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_GUTSCHRIFT);
		}

		@Override
		public JCRDocDto setupJCRDocDto(PrintInfoDto printInfo) {
			JCRDocDto doc = super.setupJCRDocDto(printInfo);
			doc.setDocPath(printInfo.getDocPath().add(new DocNodeFile(RechnungReportFac.REPORT_GUTSCHRIFT)));
			return doc;
		}
		
		@Override
		public void saveDoc(JCRDocDto doc) throws ExceptionLP, Throwable {
			if (doc == null || doc.getbData() == null) return;
			saveAsPdf(getExportDirPath(), doc, "GS");
		}
		
		@Override
		public Integer getUsecaseId() {
			return QueryParameters.UC_ID_GUTSCHRIFT;
		};
	}
	
	private class EingangsrechnungExporter implements IBelegExporter {

		@Override
		public String getBelegartCNr() {
			return LocaleFac.BELEGART_EINGANGSRECHNUNG;
		}

		@Override
		public String getExportPath() throws ExceptionLP, Throwable {
			return getParameterWertFinanz(ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_EINGANGSRECHNUNG);
		}

		@Override
		public void saveDoc(JCRDocDto doc) throws ExceptionLP, Throwable {
			if (doc == null || doc.getbData() == null) return;
			
			if (isJRPrint(doc)) {
				saveAsPdf(new File(getExportPath()).getParent(), doc, "ER");
			} else {
				saveAs(new File(getExportPath()).getParent(), doc, "ER");
			}
		}

		@Override
		public Integer getUsecaseId() {
			return QueryParameters.UC_ID_EINGANGSRECHNUNG;
		}

		@Override
		public List<JCRDocDto> getJCRDocs(Integer belegIId) throws ExceptionLP, Throwable {
			PrintInfoDto printInfo = holePrintInfo(belegIId, getUsecaseId());
			return getJcrDocs(setupDefaultJCRDocDto(printInfo));
		}
		
	}
	
	private boolean isJRPrint(JCRDocDto doc) {
		return doc.getsMIME() != null && doc.getsMIME().toLowerCase().endsWith(".jrprint");
	}

	public void saveEingangsrechnungBelege(List<EingangsrechnungDto> eingangsrechnungen, String directory) throws Throwable {
		EingangsrechnungExporterModulER erExporter = new EingangsrechnungExporterModulER(directory);
		erExporter.export(eingangsrechnungen);
	}
	
	private class EingangsrechnungExporterModulER extends EingangsrechnungExporter {
		private String directory;
		private boolean shouldOverwrite = false;
		private DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		private EingangsrechnungDto currentEr = null;
		private Integer count = 0;
		private HvCreatingCachingProvider<Integer, LieferantDto> lieferantCache = new HvCreatingCachingProvider<Integer, LieferantDto>() {
			protected LieferantDto provideValue(Integer key, Integer transformedKey) throws ExceptionLP {
				try {
					LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate().lieferantFindByPrimaryKey(key);
					return lieferantDto;
				} catch (Throwable e) {
					throw new ExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
				}
			}
		};
		
		public EingangsrechnungExporterModulER(String directory) {
			this.directory = directory;
		}
		
		public void export(List<EingangsrechnungDto> eingangsrechnungen) throws Throwable {
			for (EingangsrechnungDto er : eingangsrechnungen) {
				currentEr = er;
				count = 0;
				saveExportDokumenteOfBeleg(this, er.getIId());
			}
		}

		@Override
		public String getExportPath() throws ExceptionLP, Throwable {
			return directory;
		}
		
		@Override
		public void saveDoc(JCRDocDto doc) throws ExceptionLP, Throwable {
			if (doc == null || doc.getbData() == null) return;
			
			String filename = createFilename();
			File file = new File(getExportPath(), filename);
			if (!(isJRPrint(doc) || isPdf(doc)))
				return;
			
			if (file.exists() && !shouldOverwrite) {
				if (!dialogShouldOverwrite(filename)) {
					return;
				}
			}
			
			if (isJRPrint(doc)) {
				saveAsPdf(file, doc, "ER");
				count++;
			} else if (isPdf(doc)){
				saveAs(file, doc, "ER");
				count++;
			}
		}
		
		private boolean isPdf(JCRDocDto doc) {
			return doc.getsMIME() != null && doc.getsMIME().toLowerCase().endsWith("pdf");
		}

		private String createFilename() throws ExceptionLP {
			LieferantDto lieferantDto = lieferantCache.getValueOfKey(currentEr.getLieferantIId());
			StringBuilder filename = new StringBuilder();
			filename.append(eliminateSpecialChars(lieferantDto.getPartnerDto().getCName1nachnamefirmazeile1()));
			
			HvOptional<String> liefReNr = HvOptional.ofNullable(currentEr.getCLieferantenrechnungsnummer());
			if (liefReNr.isPresent()) {
				filename.append("_")
					.append(eliminateSpecialChars(liefReNr.get()));
			}
			
			filename.append("_")
				.append(dateFormatter.format(currentEr.getDBelegdatum()))
				.append(count > 0 ? ("_" + count) : "")
				.append(".pdf");
			
			return filename.toString();
		}
		
		protected String eliminateSpecialChars(String value) {
			String[] charFrom = new String[] { "\u00E4", "\u00C4", "\u00F6",
					"\u00D6", "\u00FC", "\u00DC", "\u00DF" };
			String[] charTo = new String[] { "ae", "Ae", "oe", "Oe", "ue", "Ue",
					"ss" };

			for (int i = 0; i < charFrom.length; i++)
				value = value.replaceAll(charFrom[i], charTo[i]);

			return value.replaceAll("[^a-zA-Z0-9\\s-]", "-");
		}
		
		private boolean dialogShouldOverwrite(String filename) {
			Object[] options = { LPMain.getTextRespectUISPr("lp.ja"), LPMain.getTextRespectUISPr("er.export.dokumente.alleueberschreiben"),
					LPMain.getTextRespectUISPr("lp.nein") };
			int selected = JOptionPane.showOptionDialog(getInternalFrame(), 
					LPMain.getMessageTextRespectUISPr("lp.datei.existiertueberschreiben", filename), "", 
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, // don't use a custom Icon
					options, // the titles of buttons
					options[0]); // default button title
			if (selected == 1)
				shouldOverwrite = true;
			
			return selected != 2;
		}
	}
	
	private class BelegDocsCounter {
		
	}
}
