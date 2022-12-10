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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;

import com.lp.client.artikel.ReportChargeneigenschaften;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
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
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagExportResult;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagFac;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagkriterienDto;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlaglaufDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBSepaExportExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um den Zahlungsvorschlag.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>06.01.05</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.5 $
 */

public class TabbedPaneZahlungsvorschlag extends TabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryZVLauf = null;
	private PanelQuery panelQueryZV = null;
	private PanelZahlungsvorschlag panelDetailZV = null;
	private PanelSplit panelSplitZV = null;
	private boolean bZusatzfunktionSepa = false;

	private PanelDialogKriterienZahlungsvorschlag panelDialogKriterienZV = null;

	private final static int IDX_0_ZVLAUF = 0;
	private final static int IDX_1_ZV = 1;

	private ZahlungsvorschlaglaufDto zvlaufDto = null;

	private final static String ACTION_SPECIAL_REMOVE_ZV = PanelBasis.ALWAYSENABLED
			+ "_action_special_remove_zahlungsvorschlag";
	private final static String ACTION_SPECIAL_EXPORTIERE_ZV_CSV = PanelBasis.ALWAYSENABLED
			+ "_action_special_exportiere_zahlungsvorschlag_csv";
	private final static String ACTION_SPECIAL_EXPORTIERE_ZV_SEPA = PanelBasis.ALWAYSENABLED
			+ "_action_special_exportiere_zahlungsvorschlag_sepa";
	private static final String ACTION_SPECIAL_FREIGABE = PanelBasis.ALWAYSENABLED + "action_special_zv_freigabe";

	private final static String ACTION_SPECIAL_PRINT_UEBERWEISUNGSLISTE = PanelBasis.ALWAYSENABLED
			+ "_action_special_print_ueberweisungsliste";

	private final static String MENU_ACTION_SEPA_EXPORT = "menu_action_sepa_export";
	private final static String MENU_ACTION_CSV_EXPORT = "menu_action_csv_export";

	public TabbedPaneZahlungsvorschlag(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("finanz.tab.unten.zahlungsvorschlag.title"));
		bZusatzfunktionSepa = LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_SEPA);
		jbInit();
		initComponents();
	}

	public ZahlungsvorschlaglaufDto getZVlaufDto() {
		return zvlaufDto;
	}

	public void setZVlaufDto(ZahlungsvorschlaglaufDto zvlaufDto) throws Throwable {
		this.zvlaufDto = zvlaufDto;
		getPanelQueryZV().setDefaultFilter(FinanzFilterFactory.getInstance().createFKZahlungsvorschlag(getZVlaufDto()));
		String sTitle = null;
		if (getZVlaufDto() != null) {
			sTitle = Helper.formatTimestamp(getZVlaufDto().getTAnlegen(), LPMain.getTheClient().getLocUi());
			LPButtonAction item1 = (LPButtonAction) getPanelQueryZVlauf().getHmOfButtons()
					.get(ACTION_SPECIAL_REMOVE_ZV);
			item1.getButton().setEnabled(true);
			LPButtonAction item2 = (LPButtonAction) getPanelQueryZVlauf().getHmOfButtons()
					.get(ACTION_SPECIAL_EXPORTIERE_ZV_CSV);
			item2.getButton().setEnabled(true);
			if (bZusatzfunktionSepa) {
				LPButtonAction item3 = (LPButtonAction) getPanelQueryZVlauf().getHmOfButtons()
						.get(ACTION_SPECIAL_EXPORTIERE_ZV_SEPA);
				item3.getButton().setEnabled(true);
			}
		} else {
			LPButtonAction item1 = (LPButtonAction) getPanelQueryZVlauf().getHmOfButtons()
					.get(ACTION_SPECIAL_REMOVE_ZV);
			item1.getButton().setEnabled(false);
			LPButtonAction item2 = (LPButtonAction) getPanelQueryZVlauf().getHmOfButtons()
					.get(ACTION_SPECIAL_EXPORTIERE_ZV_CSV);
			item2.getButton().setEnabled(false);
			if (bZusatzfunktionSepa) {
				LPButtonAction item3 = (LPButtonAction) getPanelQueryZVlauf().getHmOfButtons()
						.get(ACTION_SPECIAL_EXPORTIERE_ZV_SEPA);
				item3.getButton().setEnabled(false);
			}
			sTitle = "";
		}
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitle);
	}

	/**
	 * jbInit.
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		// 1 tab oben: ZV Laeufe; lazy loading
		insertTab(LPMain.getTextRespectUISPr("finanz.tab.oben.zvlaeufe.title"), null, null,
				LPMain.getTextRespectUISPr("finanz.tab.oben.zvlaeufe.tooltip"), IDX_0_ZVLAUF);
		// 2 tab oben: ER's; lazy loading
		insertTab(LPMain.getTextRespectUISPr("finanz.tab.oben.offeneposten.title"), null, null,
				LPMain.getTextRespectUISPr("finanz.tab.oben.offeneposten.tooltip"), IDX_1_ZV);
		// default selektierung
		setSelectedComponent(getPanelQueryZVlauf());
		// refresh
		getPanelQueryZVlauf().eventYouAreSelected(false);
		// Listener
		// damit gleich eine selektiert ist

		ItemChangedEvent it = new ItemChangedEvent(getPanelQueryZVlauf(), ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	private PanelSplit getPanelSplitZV() throws Throwable {
		if (panelSplitZV == null) {
			panelSplitZV = new PanelSplit(getInternalFrame(), getPanelDetailZV(), getPanelQueryZV(), 250);
			setComponentAt(IDX_1_ZV, panelSplitZV);
		}
		return panelSplitZV;
	}

	private PanelQuery getPanelQueryZVlauf() throws Throwable {
		if (panelQueryZVLauf == null) {
			String[] aWhichButtonIUseZVlauf = { PanelBasis.ACTION_NEW };
			FilterKriterium[] filtersZVlauf = SystemFilterFactory.getInstance().createFKMandantCNr();

			panelQueryZVLauf = new PanelQuery(null, filtersZVlauf, QueryParameters.UC_ID_ZAHLUNGSVORSCHLAGLAUF,
					aWhichButtonIUseZVlauf, getInternalFrame(), LPMain.getTextRespectUISPr("finanz.zahlungsvorschlag"),
					true);
			panelQueryZVLauf.createAndSaveAndShowButton("/com/lp/client/res/delete2.png",
					"Zahlungsvorschlag l\u00F6schen", ACTION_SPECIAL_REMOVE_ZV, RechteFac.RECHT_FB_FINANZ_CUD);
			panelQueryZVLauf.createAndSaveAndShowButton("/com/lp/client/res/document_into.png",
					"Zahlungsvorschlag exportieren CSV", ACTION_SPECIAL_EXPORTIERE_ZV_CSV,
					RechteFac.RECHT_FB_FINANZ_CUD);
			if (bZusatzfunktionSepa) {
				panelQueryZVLauf.createAndSaveAndShowButton("/com/lp/client/res/sepa16x16.png",
						"Zahlungsvorschlag exportieren SEPA", ACTION_SPECIAL_EXPORTIERE_ZV_SEPA,
						RechteFac.RECHT_FB_FINANZ_CUD);
			}

			panelQueryZVLauf.createAndSaveAndShowButton("/com/lp/client/res/printer.png",
					LPMain.getTextRespectUISPr("finanz.ueberweisungsliste.drucken"),
					ACTION_SPECIAL_PRINT_UEBERWEISUNGSLISTE, null);

			setComponentAt(IDX_0_ZVLAUF, panelQueryZVLauf);
		}
		return panelQueryZVLauf;
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {
		if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == getPanelQueryZVlauf()) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				holeZVlaufDto(key);
				if (key == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_0_ZVLAUF, false);
					getPanelQueryZVlauf().updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_EMPTY));
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_0_ZVLAUF, true);
					getPanelQueryZVlauf().updateButtons(new LockStateValue((PanelBasis.LOCK_IS_NOT_LOCKED)));
				}
			} else if (eI.getSource() == getPanelQueryZV()) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				getPanelDetailZV().setKeyWhenDetailPanel(key);
				getPanelDetailZV().eventYouAreSelected(false);
				getPanelQueryZV().updateButtons();
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == getPanelQueryZVlauf()) {
				actionNewZahlungsvorschlaglauf();
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();
			if (eI.getSource() == getPanelQueryZVlauf()) {
				if (sAspectInfo.equals(ACTION_SPECIAL_REMOVE_ZV)) {
					if (getZVlaufDto() != null) {

						if (getZVlaufDto().getTGespeichert() != null) {

							boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
									LPMain.getTextRespectUISPr("er.zahlungsvorschlag.gespeichert.warning.loeschen"));
							if (b == false) {
								return;
							}

						}

						DelegateFactory.getInstance().getEingangsrechnungDelegate()
								.removeZahlungsvorschlaglauf(getZVlaufDto().getIId());
						setZVlaufDto(null);
					}
					getPanelQueryZVlauf().eventYouAreSelected(false);
				} else if (sAspectInfo.equals(ACTION_SPECIAL_EXPORTIERE_ZV_CSV)) {
					exportiereZahlungsvorschlag(ZahlungsvorschlagFac.FORMAT_CSV);
				} else if (sAspectInfo.equals(ACTION_SPECIAL_EXPORTIERE_ZV_SEPA)) {
					exportiereZahlungsvorschlag(ZahlungsvorschlagFac.FORMAT_SEPA);
				} else if (sAspectInfo.equals(ACTION_SPECIAL_PRINT_UEBERWEISUNGSLISTE)) {
					if (panelQueryZVLauf.getSelectedId() != null) {
						String add2Title = LPMain.getTextRespectUISPr("finanz.ueberweisungsliste.drucken");
						getInternalFrame().showReportKriterien(new ReportUeberweisungsliste(getInternalFrame(),
								(Integer) panelQueryZVLauf.getSelectedId(), add2Title));
					}
				}

			} else if (eI.getSource() == getPanelQueryZV()) {
				if (sAspectInfo.equals(ACTION_SPECIAL_FREIGABE)) {
					setzeUeberweisungFreigabe();
				}
			}
		} else if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == getPanelQueryZVlauf()) {
				setSelectedComponent(panelSplitZV);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == getPanelDetailZV()) {
				setKeyWasForLockMe();
				getPanelSplitZV().eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == getPanelDetailZV()) {
				getPanelSplitZV().eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelDetailZV) {
				Object oKey = panelDetailZV.getKeyWhenDetailPanel();
				panelQueryZV.eventYouAreSelected(false);
				panelQueryZV.setSelectedId(oKey);
				panelSplitZV.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (eI.getSource() == getPanelDialogKriterienZV()) {
				// ZV durchfuehren
				ZahlungsvorschlagkriterienDto krit = getPanelDialogKriterienZV().getKriterienDto();
				if (krit != null) {
					Integer iId = DelegateFactory.getInstance().getEingangsrechnungDelegate()
							.createZahlungsvorschlag(krit);
					getPanelQueryZVlauf().eventYouAreSelected(false);
					getPanelQueryZVlauf().setSelectedId(iId);
					if (iId != null) {
						setZVlaufDto(DelegateFactory.getInstance().getEingangsrechnungDelegate()
								.zahlungsvorschlaglaufFindByPrimaryKey(iId));
						// Filter am 2. panel aktualisieren
						getPanelQueryZV().setDefaultFilter(
								FinanzFilterFactory.getInstance().createFKZahlungsvorschlag(getZVlaufDto()));
						// und aus 2. panel umschalten und dieses aktualisieren
						setSelectedComponent(getPanelSplitZV());
						getPanelSplitZV().eventYouAreSelected(false);
					} else {
						// Es wurden keine offenen ERs gefunden
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
								LPMain.getTextRespectUISPr("fb.zahlungsvorschlag.keineoffeneners"));
					}
				} else {
					getPanelQueryZVlauf().eventYouAreSelected(false);
				}
			}
		}
	}

	private void actionNewZahlungsvorschlaglauf() throws Throwable {
		if (!DelegateFactory.getInstance().getEingangsrechnungDelegate()
				.darfNeuerZahlungsvorschlaglaufErstelltWerden()) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("fb.zahlungsvorschlag.neuerlauf.error.offenevorhanden"));
			return;
		}

		getInternalFrame().showPanelDialog(getPanelDialogKriterienZV());
	}

	private void setzeUeberweisungFreigabe() throws Throwable {
		List<Integer> selectedIds = new ArrayList<Integer>();
		for (Object o : getPanelQueryZV().getSelectedIds()) {
			selectedIds.add((Integer) o);
		}

		DelegateFactory.getInstance().getEingangsrechnungDelegate()
				.updateZahlungsvorschlagBBezahlenMultiSelect(selectedIds);

		refreshPanelQueryZV();
		getPanelDetailZV().updateGesamtwert();
	}

	/**
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	private void exportiereZahlungsvorschlag(Integer iExportTyp) throws ExceptionLP, Throwable {
		if (getZVlaufDto() == null)
			return;

		if (getZVlaufDto().getTGespeichert() != null) {
			if (!DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					LPMain.getTextRespectUISPr("er.zahlungsvorschlag.gespeichert.warning.nochmal"))) {
				return;
			}

		}

		boolean bNegativeVorhanden = DelegateFactory.getInstance().getEingangsrechnungDelegate()
				.sindNegativeZuExportierendeZahlungenVorhanden(getZVlaufDto().getIId());

		if (bNegativeVorhanden) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("finanz.zahlungsvorschlag.error.negative"));
			return;
		}

		// Exportdaten
		try {
			if (!checkExportFile(iExportTyp)) {
				return;
			}

			ZahlungsvorschlagExportResult result = DelegateFactory.getInstance().getEingangsrechnungDelegate()
					.exportiereZahlungsvorschlaglauf(getZVlaufDto().getIId(), iExportTyp);

			if (!result.hasFailed() && result.getDaten() != null) {
				saveExportFile(iExportTyp, result.getDaten());
			}

			if (ZahlungsvorschlagFac.FORMAT_CSV == iExportTyp && result.hasMessages()) {
				showDialogCsvExportResult(result);
			} else if (result.getDaten() == null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("fb.export.keinebelegezuexportieren"));
			}

		} catch (ExceptionLP e) {
			handleExportException(e);
		}

		getPanelQueryZVlauf().eventYouAreSelected(false);
	}

	private void showDialogCsvExportResult(ZahlungsvorschlagExportResult result) {
		DialogCsvExportZVResult dialog = new DialogCsvExportZVResult(LPMain.getInstance().getDesktop(),
				result.getMessages());
		dialog.setVisible(true);
	}

	/**
	 * @param iExportTyp
	 * @param sExport
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	private void saveExportFile(Integer iExportTyp, String sExport) throws ExceptionLP, Throwable {
		String filename = getExportFilename(iExportTyp);
		LPMain.getInstance().saveFile(getInternalFrame(), filename, sExport.getBytes("UTF-8"), false);

		if (ZahlungsvorschlagFac.FORMAT_SEPA == iExportTyp) {
			DelegateFactory.getInstance().getEingangsrechnungDelegate()
					.archiviereSepaZahlungsvorschlag(getZVlaufDto().getIId(), sExport);
		}
	}

	private String getExportFilename(Integer iExportTyp) throws ExceptionLP, Throwable {
		if (ZahlungsvorschlagFac.FORMAT_SEPA == iExportTyp) {
			String sFilename = DelegateFactory.getInstance().getEingangsrechnungDelegate()
					.getZahlungsvorschlagSepaExportFilename(getZVlaufDto().getBankverbindungIId());
			File dir = new File(sFilename, ZahlungsvorschlagFac.ZV_EXPORT_SEPA_ORDNER);
			File file = new File(dir, ZahlungsvorschlagFac.ZV_EXPORT_SEPA_FILENAME);
			return file.getCanonicalPath();
		}

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
				ParameterFac.PARAMETER_ZAHLUNGSVORSCHLAG_EXPORTZIEL);
		return parameter.getCWert();
	}

	private boolean checkExportFile(Integer iExportTyp) throws ExceptionLP, Throwable {
		File file = new File(getExportFilename(iExportTyp));

		boolean canWrite = false;
		try {
			if (!file.exists()) {
				if (file.getParent() != null)
					file.getParentFile().mkdirs();
				canWrite = file.createNewFile();
				if (canWrite)
					file.delete();
			} else {
				File tmpFile = File.createTempFile("temp", ".tmp", file.getParentFile());
				tmpFile.delete();
				canWrite = true;
			}
		} catch (Throwable e) {
			myLogger.warn("Datei kann nicht geschrieben werden", e);
			canWrite = false;
		}

		if (!canWrite) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.dateikannnichterzeugtwerden") + "\n"
							+ file.getCanonicalPath());
		}
		return canWrite;
	}

	private void handleExportException(ExceptionLP ex) throws ExceptionLP {

		if (EJBExceptionLP.FEHLER_SEPAEXPORT_EXPORT_FEHLGESCHLAGEN == ex.getICode()) {
			List<EJBSepaExportExceptionLP> messages = new ArrayList<EJBSepaExportExceptionLP>();
			List<?> info = ex.getAlInfoForTheClient();
			if (info == null || info.size() == 0 || !(info.get(0) instanceof List<?>)) {
				return;
			}

			List<?> errors = (List<?>) ex.getAlInfoForTheClient().get(0);
			for (Object error : errors) {
				EJBSepaExportExceptionLP sepaError = (EJBSepaExportExceptionLP) error;
				messages.add(sepaError);
			}

			if (!messages.isEmpty()) {
				DialogSepaExportResult dialog = new DialogSepaExportZVResult(LPMain.getInstance().getDesktop(),
						messages);
				dialog.setVisible(true);
			}

		} else if (EJBExceptionLP.FEHLER_SEPAEXPORT_KEIN_SEPA_VERZEICHNIS_VORHANDEN == ex.getICode()) {
			List<?> objects = ex.getAlInfoForTheClient();
			if (objects != null && !objects.isEmpty()) {
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("er.zahlungsvorschlag.sepaexportfehlgeschlagen"),
						LPMain.getMessageTextRespectUISPr("er.zahlungsvorschlag.sepaexport.keinsepaverzeichnis",
								objects));
			}

		} else if (EJBExceptionLP.FEHLER_SEPAEXPORT_KEINE_EURO_BELEGE == ex.getICode()) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("er.zahlungsvorschlag.sepaexportfehlgeschlagen"),
					LPMain.getTextRespectUISPr("er.zahlungsvorschlag.sepaexport.keineeurobelege"));

		} else if (EJBExceptionLP.FEHLER_SEPAEXPORT_KEINE_BELEGE == ex.getICode()) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("er.zahlungsvorschlag.sepaexportfehlgeschlagen"),
					LPMain.getTextRespectUISPr("er.zahlungsvorschlag.sepaexport.keinebelege"));

		} else {
			throw ex;
		}

	}

	/**
	 * getPanelQueryZV.
	 * 
	 * @return PanelQuery
	 * @throws Throwable
	 */
	private PanelQuery getPanelQueryZV() throws Throwable {
		if (panelQueryZV == null) {
			String[] aWhichButtonIUseZV = {};
			FilterKriterium[] filtersZV = FinanzFilterFactory.getInstance().createFKZahlungsvorschlag(getZVlaufDto());

			panelQueryZV = new PanelQuery(null, filtersZV, QueryParameters.UC_ID_ZAHLUNGSVORSCHLAG, aWhichButtonIUseZV,
					getInternalFrame(), LPMain.getTextRespectUISPr("finanz.zahlungsvorschlag"), true);
			panelQueryZV.createAndSaveAndShowButton("/com/lp/client/res/check2.png",
					LPMain.getTextRespectUISPr("fb.tooltip.bezahlen"), ACTION_SPECIAL_FREIGABE,
					RechteFac.RECHT_FB_FINANZ_CUD);

			panelQueryZV.setMultipleRowSelectionEnabled(true);
		}
		return panelQueryZV;
	}

	private PanelZahlungsvorschlag getPanelDetailZV() throws Throwable {
		if (panelDetailZV == null) {
			panelDetailZV = new PanelZahlungsvorschlag(getInternalFrame(),
					LPMain.getTextRespectUISPr("finanz.zahlungsvorschlag"), null, this);
		}
		return panelDetailZV;
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();
		if (selectedIndex == IDX_0_ZVLAUF) {
			getPanelQueryZVlauf().eventYouAreSelected(false);
		} else if (selectedIndex == IDX_1_ZV) {
			getPanelSplitZV().eventYouAreSelected(false);
			// Gesamtwert aktualisieren
			getPanelDetailZV().updateGesamtwert();
		}
	}

	protected void refreshPanelQueryZV() throws Throwable {
		getPanelQueryZV().eventYouAreSelected(false);
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (MENU_ACTION_CSV_EXPORT.equals(e.getActionCommand())) {
			exportiereZahlungsvorschlag(ZahlungsvorschlagFac.FORMAT_CSV);
		} else if (MENU_ACTION_SEPA_EXPORT.equals(e.getActionCommand())) {
			exportiereZahlungsvorschlag(ZahlungsvorschlagFac.FORMAT_SEPA);
		}
	}

	public Integer getSelectedIIdZVlauf() {
		return (Integer) panelQueryZVLauf.getSelectedId();
	}

	/**
	 * Einen ausgewaehlten ZV lauf holen und die Panels aktualisieren
	 * 
	 * @param key Object
	 * @throws Throwable
	 */
	private void holeZVlaufDto(Object key) throws Throwable {
		if (key != null) {
			ZahlungsvorschlaglaufDto dto = DelegateFactory.getInstance().getEingangsrechnungDelegate()
					.zahlungsvorschlaglaufFindByPrimaryKey((Integer) key);
			setZVlaufDto(dto);
			getInternalFrame().setKeyWasForLockMe(key.toString());
		} else {
			setZVlaufDto(null);
		}
	}

	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = getPanelQueryZVlauf().getSelectedId();
		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wmb = new WrapperMenuBar(this);

		JMenu jmModul = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_MODUL);
		jmModul.add(new JSeparator(), 0);

		if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_FB_FINANZ_CUD)) {
			JMenu menuExport = new WrapperMenu("fb.menu.export", this);

			if (bZusatzfunktionSepa) {
				WrapperMenuItem menuItemSepaExport = new WrapperMenuItem(
						LPMain.getTextRespectUISPr("fb.menu.export.zv.sepa"), null);
				menuItemSepaExport.addActionListener(this);
				menuItemSepaExport.setActionCommand(MENU_ACTION_SEPA_EXPORT);
				menuExport.add(menuItemSepaExport, 0);
			}

			WrapperMenuItem menuItemCSVExport = new WrapperMenuItem(LPMain.getTextRespectUISPr("fb.menu.export.zv.csv"),
					null);
			menuItemCSVExport.addActionListener(this);
			menuItemCSVExport.setActionCommand(MENU_ACTION_CSV_EXPORT);
			menuExport.add(menuItemCSVExport, 0);

			jmModul.add(menuExport, 0);
		}

		return wmb;
	}

	private PanelDialogKriterienZahlungsvorschlag getPanelDialogKriterienZV() throws Throwable {
		if (panelDialogKriterienZV == null) {
			panelDialogKriterienZV = new PanelDialogKriterienZahlungsvorschlag(getInternalFrame(), "");
		}
		return panelDialogKriterienZV;
	}

	public Object getDto() {
		return zvlaufDto;
	}

//	protected void eventActionSpecial(ActionEvent e) throws Throwable {
//			if (zvDto != null) {
//				DelegateFactory.getInstance().getEingangsrechnungDelegate()
//						.toggleZahlungsvorschlagBBezahlen(zvDto.getIId());
//				// oberes Panel aktualisieren
//				tabbedPaneZV.refreshPanelQueryZV();
//				// Gesamtwert neu berechnen
//				updateGesamtwert();
//			}
//		}
//	}

}
