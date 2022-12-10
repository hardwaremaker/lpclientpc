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
package com.lp.client.zeiterfassung;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.PanelArtikellieferantstaffelpreise;
import com.lp.client.auftrag.ReportAuftragszeitstatistik;
import com.lp.client.fertigung.FertigungFilterFactory;
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
import com.lp.client.frame.filechooser.open.FileOpenerFactory;
import com.lp.client.frame.filechooser.open.XlsFile;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.DialogUrlaubsantrag;
import com.lp.client.personal.PanelBereitschaft;
import com.lp.client.personal.PanelReisespesen;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.personal.ReportUrlaubsantrag;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.personal.service.HvmaLizenzEnum;
import com.lp.server.personal.service.HvmalizenzDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.SonderzeitenAntragEmailDto;
import com.lp.server.personal.service.SonderzeitenDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungFavoritenDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.HvOptional;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class TabbedPaneZeiterfassung extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PanelQuery panelQueryPersonal = null;

	private PanelQuery panelQueryZeitdaten = null;
	private PanelZeitdaten panelBottomZeitdaten = null;

	public PanelZeitdaten getPanelBottomZeitdaten() {
		return panelBottomZeitdaten;
	}

	private PanelSplit panelSplitZeitdaten = null;

	private PanelQuery panelQuerySonderzeiten = null;

	public PanelQuery getPanelQuerySonderzeiten() {
		return panelQuerySonderzeiten;
	}

	public PanelQuery getPanelQueryReisezeiten() {
		return panelQueryReisezeiten;
	}

	private PanelBasis panelBottomSonderzeiten = null;
	private PanelSplit panelSplitSonderzeiten = null;

	private PanelQuery panelQueryZeitgutschrift = null;
	private PanelBasis panelBottomZeitgutschrift = null;
	private PanelSplit panelSplitZeitgutschrift = null;

	private PanelQuery panelQueryZeitabschluss = null;
	private PanelBasis panelBottomZeitabschluss = null;
	private PanelSplit panelSplitZeitabschluss = null;

	private PanelQuery panelQueryBereitschaft = null;
	private PanelBasis panelBottomBereitschaft = null;
	private PanelSplit panelSplitBereitschaft = null;

	private PanelQuery panelQueryReisezeiten = null;
	private PanelBasis panelBottomReisezeiten = null;
	private PanelSplit panelSplitReisezeiten = null;

	private PanelQuery panelQueryReisespesen = null;
	private PanelBasis panelBottomReisespesen = null;
	private PanelSplit panelSplitReisespesen = null;

	private PanelQuery panelQueryTelefonzeiten = null;
	private PanelTelefonzeiten panelBottomTelefonzeiten = null;
	private PanelSplit panelSplitTelefonzeiten = null;

	private PanelQueryFLR panelQueryFLRLosQuelle = null;
	private PanelQueryFLR panelQueryFLRLosZiel = null;
	Integer losIId_Quelle = null;

	private PanelQuery panelQueryQueue = null;

	private PanelQueryFLR panelQueryFLRFavoriten = null;

	private PanelQuery panelQueryZeitdatenpruefen = null;
	private PanelZeitdatenpruefen panelBottomZeitdatenpruefen = null;
	private PanelSplit panelSplitZeitdatenpruefen = null;

	private PanelDialogAutomatikbuchungen pdKriterienAutomatikbuchungen = null;
	private PanelDialogSchichtzeitmodelle pdKriterienSchichtzeitmodelle = null;
	private PanelDialogNegativstundenInUrlaubUmwandeln pdNegativstundenInUrlaubUmwandeln = null;

	public static int IDX_PANEL_AUSWAHL = -1;
	public static int IDX_PANEL_ZEITDATEN = -1;
	public static int IDX_PANEL_SONDERZEITEN = -1;
	public static int IDX_PANEL_ZEITGUTSCHRIFT = -1;
	public static int IDX_PANEL_BEREITSCHAFT = -1;
	public static int IDX_PANEL_REISEZEITEN = -1;
	public static int IDX_PANEL_REISESPESEN = -1;
	public static int IDX_PANEL_TELEFONZEITEN = -1;
	public static int IDX_PANEL_PROJEKTQUEUE = -1;
	public static int IDX_PANEL_ZEITABSCHLUSS = -1;
	public static int IDX_PANEL_ZEITDATENPRUEFEN = -1;

	private final static String MENUE_ACTION_ZEITDATEN = "MENUE_ACTION_ZEITDATRN";
	private final static String MENUE_ACTION_MONATSABRECHNUNG = "MENUE_ACTION_MONATSABRECHNUNG";
	private final static String MENUE_ACTION_WOCHENABRECHNUNG = "MENUE_ACTION_WOCHENABRECHNUNG";
	private final static String MENUE_ACTION_WOCHENJOURNAL = "MENUE_ACTION_WOCHENJOURNAL";
	private final static String MENUE_ACTION_ANWESENHEITSLISTE = "MENUE_ACTION_ANWESENHEITSLISTE";
	private final static String MENUE_ACTION_SONDERZEITENLISTE = "MENUE_ACTION_SONDERZEITENLISTE";
	private final static String MENUE_ACTION_PRODUKTIVITAETSSTATISTIK = "MENUE_ACTION_PRODUKTIVITAETSSTATISTIK";
	private final static String MENUE_ACTION_PRODUKTIVITAETSTAGESSTATISTIK = "MENUE_ACTION_PRODUKTIVITAETSTAGESSTATISTIK";
	private final static String MENUE_ACTION_AUFTRAGSZEITSTATISTIK = "MENUE_ACTION_AUFTRAGSZEITSTATISTIK";
	private final static String MENUE_ACTION_LETZTE_SYNCHRONISATION = "MENUE_LETZTE_SYNCHRONISATION";
	private final static String MENUE_ACTION_ARBEITSZEITSTATISTIK = "MENUE_ACTION_ARBEITSZEITSTATISTIK";
	private final String MENU_ACTION_PFLEGE_AUTOMATIKBUCHUNGEN = "MENU_ACTION_PFLEGE_AUTOMATIKBUCHUNGEN";
	private final String MENU_ACTION_PFLEGE_NEGATIVE_STUNDEN_IN_URLAUB_WANDELN = "MENU_ACTION_PFLEGE_NEGATIVE_STUNDEN_IN_URLAUB_WANDELN";
	private final String MENU_ACTION_PFLEGE_SCHICHTZEITMODELLE = "MENU_ACTION_PFLEGE_SCHICHTZEITMODELLE";
	private final static String MENU_ACTION_PFLEGE_LOSZEITEN_VERSCHIEBEN = "MENU_ACTION_PFLEGE_LOSZEITEN_VERSCHIEBEN";
	private final static String MENUE_ACTION_REISEZEITEN = "MENUE_ACTION_REISEZEITEN";
	private final static String MENUE_ACTION_FAHRTENBUCH = "MENUE_ACTION_FAHRTENBUCH";
	private final static String MENUE_ACTION_MITARBEITERUEBERSICHT = "MENUE_ACTION_MITARBEITERUEBERSICHT";
	private final static String MENUE_ACTION_TELEFONZEITERFASSUNG = "MENUE_ACTION_TELEFONZEITERFASSUNG";
	private final static String MENUE_ACTION_ZEITSALDO = "MENUE_ACTION_ZEITSALDO";
	private final static String MENUE_ACTION_MITARBEITEREINTEILUNG = "MENUE_ACTION_MITARBEITEREINTEILUNG";
	private final static String MENUE_ACTION_MASCHINENERFOLG = "MENUE_ACTION_MASCHINENERFOLG";
	private final static String MENUE_ACTION_LOHNDATENEXPORT = "MENUE_ACTION_LOHNDATENEXPORT";
	private final static String MENUE_ACTION_SONDERZEITEN_IMPORT = "MENUE_ACTION_SONDERZEITEN_IMPORT";
	private final static String MENUE_ACTION_ABGESCHLOSSENE_ZEITEN = "MENUE_ACTION_ABGESCHLOSSENE_ZEITEN";
	private final static String MENUE_ACTION_WOCHENABSCHLUSS = "MENUE_ACTION_WOCHENABSCHLUSS";

	private final String MENU_INFO_AENDERUNGEN = "MENU_INFO_AENDERUNGEN";

	public final static String AUS_QUEUE_ENTFERNEN = "aus_queue_entfernen";
	public final static String MY_OWN_NEW_AUS_QUEUE_ENTFERNEN = PanelBasis.ACTION_MY_OWN_NEW + AUS_QUEUE_ENTFERNEN;

	public final static String FAVORITELISTE = "favoritenliste";
	public final static String MY_OWN_NEW_FAVORITENLISTE = PanelBasis.ACTION_MY_OWN_NEW + FAVORITELISTE;

	public final static String URLAUBSANTRAG = "urlaubsantrag";
	public final static String MY_OWN_NEW_URLAUBSANTRAG = PanelBasis.ACTION_MY_OWN_NEW + URLAUBSANTRAG;

	public final static String LEAVEALONE_WANDLE_URLAUBSANTRAG_UM = PanelBasis.LEAVEALONE + "_action_special_umwandeln";

	public final static String MY_OWN_NEW_FAVORIT_UEBERNEHMEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_FAVORIT_UEBERNEHMEN";
	public final static String MY_OWN_NEW_FAVORIT_SOFORT_ANLEGEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_FAVORIT_SOFORT_ANLEGEN";

	private boolean bVonBisErfassung = false;
	private boolean bKommtGehtBuchen = false;

	public boolean isBKommtGehtBuchen() {
		return bKommtGehtBuchen;
	}

	public boolean isBVonBisErfassung() {
		return bVonBisErfassung;
	}

	public PanelQuery getPanelQueryZeitdaten() {
		return panelQueryZeitdaten;
	}

	public PanelSplit getPanelSplitZeitdaten() {
		return panelSplitZeitdaten;
	}

	public PanelSplit getPanelSplitTelefonzeiten() {
		return panelSplitTelefonzeiten;
	}

	public PanelSplit getPanelSplitZeitdatenpruefen() {
		return panelSplitZeitdatenpruefen;
	}

	public PanelQuery getPanelQueryPersonal() {
		return panelQueryPersonal;
	}

	public PanelQuery getPanelQueryZeitdatenpruefen() {
		return panelQueryZeitdatenpruefen;
	}

	public PanelZeitdatenpruefen getPanelBottomZeitdatenpruefen() {
		return panelBottomZeitdatenpruefen;
	}

	public TabbedPaneZeiterfassung(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("zeiterfassung.modulname"));
		IDX_PANEL_ZEITDATENPRUEFEN = -1;
		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_PERSONAL,
				ParameterFac.PARAMETER_VON_BIS_ERFASSUNG);
		bVonBisErfassung = (java.lang.Boolean) parameter.getCWertAsObject();

		parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_PERSONAL,
				ParameterFac.PARAMETER_VON_BIS_ERFASSUNG_KOMMT_GEHT_BUCHEN);
		bKommtGehtBuchen = (java.lang.Boolean) parameter.getCWertAsObject();

		jbInit();
		initComponents();
	}

	public void gotoAuswahl() throws Throwable {
		setSelectedComponent(panelQueryPersonal);

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryPersonal.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void createAuswahl() throws Throwable {
		if (panelQueryPersonal == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_FILTER };
			panelQueryPersonal = new PanelQuery(PersonalFilterFactory.getInstance().createQTPersonal(),
					PersonalFilterFactory.getInstance().createFKDPersonalauswahl(getInternalFrame()),
					QueryParameters.UC_ID_PERSONAL_ZEITERFASSUNG, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("auft.title.panel.auswahl"), true);

			panelQueryPersonal.befuellePanelFilterkriterienDirektUndVersteckte(
					PersonalFilterFactory.getInstance().createFKDPersonalname(),
					PersonalFilterFactory.getInstance().createFKDPersonalnummer(),
					PersonalFilterFactory.getInstance().createFKVPersonal());
			panelQueryPersonal.addDirektFilter(PersonalFilterFactory.getInstance().createFKDAusweis());
			panelQueryPersonal.eventYouAreSelected(false);
			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryPersonal);
			try {
				panelQueryPersonal.setSelectedId(LPMain.getTheClient().getIDPersonal());
			} catch (Throwable ex) {
				// nothing here
			}
		}
	}

	public void copyHV() throws ExceptionLP, Throwable {
		int selectedPanelIndex = this.getSelectedIndex();

		if (selectedPanelIndex == IDX_PANEL_ZEITDATEN) {
			ArrayList<Integer> aoIIdPosition = panelQueryZeitdaten.getSelectedIdsAsInteger();

			if (aoIIdPosition != null && (aoIIdPosition.size() > 0)) {
				ZeitdatenDto[] dtos = new ZeitdatenDto[aoIIdPosition.size()];
				for (int i = 0; i < aoIIdPosition.size(); i++) {
					dtos[i] = DelegateFactory.getInstance().getZeiterfassungDelegate()
							.zeitdatenFindByPrimaryKey(aoIIdPosition.get(i));
				}
				LPMain.getPasteBuffer().writeObjectToPasteBuffer(dtos);
			}
		}
	}

	public void einfuegenHV() throws IOException, ParserConfigurationException, SAXException, Throwable {

		Object o = LPMain.getPasteBuffer().readObjectFromPasteBuffer();

		int selectedPanelIndex = this.getSelectedIndex();
		if (o instanceof ZeitdatenDto[]) {
			if (selectedPanelIndex == IDX_PANEL_ZEITDATEN) {

				ZeitdatenDto[] positionDtos = (ZeitdatenDto[]) o;

				int iInserted = 0;
				if (positionDtos != null && positionDtos.length > 0) {

					Integer telefonIId = DelegateFactory.getInstance().getZeiterfassungDelegate()
							.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_TELEFON).getIId();

					ZeiterfassungPruefer zeiterfassungPruefer = new ZeiterfassungPruefer(getInternalFrame());

					Integer personalIId = getInternalFrameZeiterfassung().getPersonalDto().getIId();

					if (zeiterfassungPruefer.pruefeObBuchungMoeglich(positionDtos[0].getTZeit(), personalIId)) {

						Timestamp tUnterschriebenBis = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.sindZeitenBereitsUnterschrieben(personalIId, positionDtos[0].getTZeit());

						if (tUnterschriebenBis != null) {
							boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
									LPMain.getMessageTextRespectUISPr("pers.zeitdaten.unterschriebenbis", Helper
											.formatTimestamp(tUnterschriebenBis, LPMain.getTheClient().getLocUi())));
							if (b == false) {
								return;
							}
						}

						Integer iId = null;

						for (int i = 0; i < positionDtos.length; i++) {
							ZeitdatenDto zeitdatenDto = positionDtos[i];
							try {

								if (zeitdatenDto.getTaetigkeitIId() != null
										&& zeitdatenDto.getTaetigkeitIId().equals(telefonIId)) {
									continue;
								}

								if (Helper.short2boolean(zeitdatenDto.getBAutomatikbuchung()) == true) {
									continue;
								}

								zeitdatenDto.setIId(null);
								zeitdatenDto.setPersonalIId(personalIId);
								// Zeit

								Calendar cNeu = Calendar.getInstance();
								cNeu.setTimeInMillis(zeitdatenDto.getTZeit().getTime());

								Timestamp tDatumAusPanel = panelBottomZeitdaten.getWdfDatum().getTimestamp();

								Calendar cDatumAusPanel = Calendar.getInstance();
								cDatumAusPanel.setTimeInMillis(tDatumAusPanel.getTime());

								cNeu.set(Calendar.YEAR, cDatumAusPanel.get(Calendar.YEAR));
								cNeu.set(Calendar.MONTH, cDatumAusPanel.get(Calendar.MONTH));
								cNeu.set(Calendar.DATE, cDatumAusPanel.get(Calendar.DATE));
								zeitdatenDto.setTZeit(new Timestamp(cNeu.getTimeInMillis()));

								// SP8245
								zeitdatenDto.setCWowurdegebucht("Client: " + Helper.getPCName());

								zeitdatenDto.setTErledigt(null);
								zeitdatenDto.setPersonalIIdErledigt(null);

								zeitdatenDto.setBTaetigkeitgeaendert(Helper.boolean2Short(false));

								// wir legen eine neue position an
								iId = DelegateFactory.getInstance().getZeiterfassungDelegate()
										.createZeitdaten(zeitdatenDto);
								iInserted++;
							}  catch (ExceptionLP ex) {
								// if (ex.getICode() ==
								// EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE)
								// {
								if (ex.getICode() == EJBExceptionLP.FEHLER_ZEITBUCHUNG_AUF_FERTIGEN_ARBEITSGANG_NICHT_MOEGLICH) {
									throw ex;
								}else {
									// nur loggen!
									myLogger.error(ex.getMessage(), ex);
								}
								
							}  catch (Throwable t) {
								myLogger.error(t.getMessage(), t);
							}
						}
						// den Datensatz in der Liste selektieren
						panelQueryZeitdaten.setSelectedId(iId);

						// die Liste neu aufbauen
						panelQueryZeitdaten.eventYouAreSelected(false);

						// im Detail den selektierten anzeigen
						panelSplitZeitdaten.eventYouAreSelected(false);
					}

				}

			}
		}

	}

	private void createZeitdaten(Integer iIdPersonaIl, Date dateI) throws Throwable {

		if (panelQueryZeitdaten == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_PREVIOUS,
					PanelBasis.ACTION_NEXT, };

			boolean darfKommtGehtAendern = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PERS_DARF_KOMMT_GEHT_AENDERN);

			boolean bCopyPaste = false;

			if (getInternalFrameZeiterfassung().bRechtNurBuchen == false && darfKommtGehtAendern
					&& isBVonBisErfassung() == false) {
				aWhichStandardButtonIUse = new String[] { PanelBasis.ACTION_NEW, PanelBasis.ACTION_KOPIEREN,
						PanelBasis.ACTION_EINFUEGEN_LIKE_NEW, PanelBasis.ACTION_PREVIOUS, PanelBasis.ACTION_NEXT };
				bCopyPaste = true;
			}

			if (isBVonBisErfassung() == true && isBKommtGehtBuchen() == false) {
				// Keine Ueberpruefung
			} else {
				java.sql.Timestamp t = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.pruefeObAmLetztenBuchungstagKommtUndGehtGebuchtWurde(iIdPersonaIl);
				if (t != null) {

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("zeiterfassung.error.buchungenunvollstaendig") + " "
									+ Helper.formatDatum(t, LPMain.getTheClient().getLocUi()));

					dateI = Helper.cutDate(new Date(t.getTime()));

				}
			}

			int ucId = QueryParameters.UC_ID_ZEITDATEN;

			if (bVonBisErfassung) {
				ucId = QueryParameters.UC_ID_ZEITDATEN_VON_BIS;
			}
			panelQueryZeitdaten = new PanelQuery(null,
					ZeiterfassungFilterFactory.getInstance().createFKZeitdatenZuPersonalUndDatum(iIdPersonaIl, dateI),
					ucId, aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("zeiterfassung.title.tab.zeitdaten"), true);

			if (bCopyPaste == true) {
				panelQueryZeitdaten.setMultipleRowSelectionEnabled(true);
			}
			panelQueryZeitdaten.befuellePanelFilterkriterienDirekt(
					ZeiterfassungFilterFactory.getInstance().createFKDZeitdatenArtikelnummer(),
					ZeiterfassungFilterFactory.getInstance().createFKDZeitdatenArtikelzusatzbezeichnung());
			panelBottomZeitdaten = new PanelZeitdaten(getInternalFrame(),
					LPMain.getTextRespectUISPr("zeiterfassung.title.tab.zeitdaten"), null);

			panelSplitZeitdaten = new PanelSplit(getInternalFrame(), panelBottomZeitdaten, panelQueryZeitdaten, 190);

			panelQueryZeitdaten.createAndSaveAndShowButton("/com/lp/client/res/index_view.png",
					LPMain.getTextRespectUISPr("pers.zeiterfassung.favoritenliste"), MY_OWN_NEW_FAVORITENLISTE,
					RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD);

			setComponentAt(IDX_PANEL_ZEITDATEN, panelSplitZeitdaten);
		} else {
			// filter refreshen.
			panelQueryZeitdaten.setDefaultFilter(
					ZeiterfassungFilterFactory.getInstance().createFKZeitdatenZuPersonalUndDatum(iIdPersonaIl, dateI));
		}
	}

	private void createZeitdatenpruefen(Integer personalId) throws Throwable {
		if (panelQueryZeitdatenpruefen == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_PREVIOUS, PanelBasis.ACTION_NEXT, };

			int ucId = QueryParameters.UC_ID_ZEITDATENPRUEFEN;
			panelQueryZeitdatenpruefen = new PanelQuery(null,
					ZeiterfassungFilterFactory.getInstance().createFKZeitdatenpruefenZuPersonal(personalId), ucId,
					aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("zeiterfassung.title.tab.zeitdaten"), true);

			panelQueryZeitdatenpruefen.befuellePanelFilterkriterienDirekt(
					ZeiterfassungFilterFactory.getInstance().createFKDZeitdatenArtikelnummer(),
					ZeiterfassungFilterFactory.getInstance().createFKDZeitdatenArtikelzusatzbezeichnung());
			panelBottomZeitdatenpruefen = new PanelZeitdatenpruefen(getInternalFrame(),
					LPMain.getTextRespectUISPr("zeiterfassung.title.tab.zeitdaten"), null);

			panelSplitZeitdatenpruefen = new PanelSplit(getInternalFrame(), panelBottomZeitdatenpruefen,
					panelQueryZeitdatenpruefen, 190);

			panelQueryZeitdatenpruefen.createAndSaveAndShowButton("/com/lp/client/res/index_view.png",
					LPMain.getTextRespectUISPr("pers.zeiterfassung.favoritenliste"), MY_OWN_NEW_FAVORITENLISTE,
					RechteFac.RECHT_PERS_ZEITEREFASSUNG_CUD);

			setComponentAt(IDX_PANEL_ZEITDATENPRUEFEN, panelSplitZeitdatenpruefen);
		} else {
			// filter refreshen.
			panelQueryZeitdatenpruefen.setDefaultFilter(
					ZeiterfassungFilterFactory.getInstance().createFKZeitdatenpruefenZuPersonal(personalId));
		}
	}

	private void createReisezeiten(Integer key) throws Throwable {

		if (panelQueryReisezeiten == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_PREVIOUS,
					PanelBasis.ACTION_NEXT, };
			panelQueryReisezeiten = new PanelQuery(null,
					ZeiterfassungFilterFactory.getInstance().createFKReisezeiten(key), QueryParameters.UC_ID_REISE,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("pers.reisezeiten"), true);
			panelBottomReisezeiten = new PanelReisezeiten(getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.reisezeiten"), null);

			panelSplitReisezeiten = new PanelSplit(getInternalFrame(), panelBottomReisezeiten, panelQueryReisezeiten,
					230);

			setComponentAt(IDX_PANEL_REISEZEITEN, panelSplitReisezeiten);
		} else {
			// filter refreshen.
			panelQueryReisezeiten.setDefaultFilter(ZeiterfassungFilterFactory.getInstance().createFKReisezeiten(key));
		}
	}

	private void createReisespesen(Integer key) throws Throwable {
		if (panelQueryReisespesen == null) {
			String[] aWhichStandardButtonIUse = new String[] { PanelBasis.ACTION_NEW };

			panelQueryReisespesen = new PanelQuery(null,
					ZeiterfassungFilterFactory.getInstance().createFKReisespesen(key),
					QueryParameters.UC_ID_REISESPESEN, aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.reisespesen"), true);

			panelBottomReisespesen = new PanelReisespesen(getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.reisespesen"), null);
			if (panelQueryReisezeiten == null) {
				createReisezeiten(key);
				panelQueryReisezeiten.eventYouAreSelected(false);
			}

			panelSplitReisespesen = new PanelSplit(getInternalFrame(), panelBottomReisespesen, panelQueryReisespesen,
					260);

			setComponentAt(IDX_PANEL_REISESPESEN, panelSplitReisespesen);

		} else {
			// filter refreshen.
			if (key != null) {

				panelQueryReisespesen
						.setDefaultFilter(ZeiterfassungFilterFactory.getInstance().createFKReisespesen(key));
			} else {
				panelQueryReisespesen
						.setDefaultFilter(ZeiterfassungFilterFactory.getInstance().createcreateFKReisespesenWennNULL());
			}

		}
	}

	private void createTelefonzeiten(Integer key) throws Throwable {

		if (panelQueryTelefonzeiten == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_PREVIOUS,
					PanelBasis.ACTION_NEXT, };
			panelQueryTelefonzeiten = new PanelQuery(null,
					ZeiterfassungFilterFactory.getInstance().createFKReisezeiten(key),
					QueryParameters.UC_ID_TELEFONZEITEN, aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.telefonzeiten"), true);

			panelQueryTelefonzeiten.befuellePanelFilterkriterienDirekt(
					ZeiterfassungFilterFactory.getInstance().createFKDTelefonzeitenPartner(),
					ZeiterfassungFilterFactory.getInstance().createFKDTelefonzeitenKommentar());

			panelBottomTelefonzeiten = new PanelTelefonzeiten(getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.telefonzeiten"), null);

			panelSplitTelefonzeiten = new PanelSplit(getInternalFrame(), panelBottomTelefonzeiten,
					panelQueryTelefonzeiten, 180);

			setComponentAt(IDX_PANEL_TELEFONZEITEN, panelSplitTelefonzeiten);
		} else {
			// filter refreshen.
			panelQueryTelefonzeiten
					.setDefaultFilter(ZeiterfassungFilterFactory.getInstance().createFKTelefonzeiten(key));
		}
	}

	private PanelQuery createProjektQueue(Integer personalIId) throws Throwable {

		FilterKriterium[] filters = ProjektFilterFactory.getInstance().createFKProjektQueue(personalIId);

		if (panelQueryQueue == null) {
			String[] aWhichButtonIUseHistory = { PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			panelQueryQueue = new PanelQuery(null, filters, QueryParameters.UC_ID_PROJEKT_QUEUE,
					aWhichButtonIUseHistory, getInternalFrame(), "", true);

			panelQueryQueue.createAndSaveAndShowButton("/com/lp/client/res/navigate_left.png",
					LPMain.getTextRespectUISPr("proj.ausqueueentfernen"), MY_OWN_NEW_AUS_QUEUE_ENTFERNEN,
					RechteFac.RECHT_PROJ_PROJEKT_CUD);

			setComponentAt(IDX_PANEL_PROJEKTQUEUE, panelQueryQueue);
		}
		panelQueryQueue.setDefaultFilter(filters);
		panelQueryQueue.setMultipleRowSelectionEnabled(true);
		panelQueryQueue.addStatusBar();

		return panelQueryQueue;
	}

	private void createSonderzeiten(Integer key) throws Throwable {

		if (panelQuerySonderzeiten == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_PREVIOUS, PanelBasis.ACTION_NEXT, };
			panelQuerySonderzeiten = new PanelQuery(ZeiterfassungFilterFactory.getInstance().createQTSonderzeiten(),
					PersonalFilterFactory.getInstance().createFKPersonal(key), QueryParameters.UC_ID_SONDERZEITEN,
					aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("zeiterfassung.title.tab.sonderzeiten"), true);
			panelQuerySonderzeiten.setMultipleRowSelectionEnabled(true);
			panelBottomSonderzeiten = new PanelSonderzeiten(getInternalFrame(),
					LPMain.getTextRespectUISPr("zeiterfassung.title.tab.sonderzeiten"), null);

			panelSplitSonderzeiten = new PanelSplit(getInternalFrame(), panelBottomSonderzeiten, panelQuerySonderzeiten,
					300);

			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_URLAUBSANTRAG);
			boolean bUrlaubsantrag = (java.lang.Boolean) parameter.getCWertAsObject();

			if (bUrlaubsantrag == true) {
				panelQuerySonderzeiten.setMultipleRowSelectionEnabled(true);
				panelQuerySonderzeiten.createAndSaveAndShowButton("/com/lp/client/res/step_new.png",
						LPMain.getTextRespectUISPr("pers.sonderzeiten.urlaubsantrag"), MY_OWN_NEW_URLAUBSANTRAG, null);

				PersonalDto personalDto_Angemeldet = DelegateFactory.getInstance().getPersonalDelegate()
						.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());

				// SP3537
				if (personalDto_Angemeldet.getPersonalfunktionCNr() != null && (personalDto_Angemeldet
						.getPersonalfunktionCNr().equals(PersonalFac.PERSONALFUNKTION_GESCHAEFTSFUEHRER)
						|| personalDto_Angemeldet.getPersonalfunktionCNr()
								.equals(PersonalFac.PERSONALFUNKTION_LOHNVERRECHNUNG)
						|| personalDto_Angemeldet.getPersonalfunktionCNr()
								.equals(PersonalFac.PERSONALFUNKTION_ABTEILUNGSLEITER))) {

					panelQuerySonderzeiten.createAndSaveAndShowButton("/com/lp/client/res/step.png",
							LPMain.getTextRespectUISPr("pers.sonderzeiten.urlaubsantragumwandeln"),
							LEAVEALONE_WANDLE_URLAUBSANTRAG_UM, null);

					panelQuerySonderzeiten.getHmOfButtons().get(LEAVEALONE_WANDLE_URLAUBSANTRAG_UM).getButton()
							.setEnabled(true);

				}

			}

			setComponentAt(IDX_PANEL_SONDERZEITEN, panelSplitSonderzeiten);
		} else {
			// filter refreshen.
			panelQuerySonderzeiten.setDefaultFilter(ZeiterfassungFilterFactory.getInstance().createFKSonderzeiten(key));
		}
	}

	private void createZeitgutschrift(Integer key) throws Throwable {

		if (panelQueryZeitgutschrift == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_PREVIOUS, PanelBasis.ACTION_NEXT, };
			panelQueryZeitgutschrift = new PanelQuery(null, PersonalFilterFactory.getInstance().createFKPersonal(key),
					QueryParameters.UC_ID_ZEITGUTSCHRIFT, aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.zeitgutschrift"), true);
			panelBottomZeitgutschrift = new PanelZeitgutschrift(getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.zeitgutschrift"), null);

			panelSplitZeitgutschrift = new PanelSplit(getInternalFrame(), panelBottomZeitgutschrift,
					panelQueryZeitgutschrift, 300);

			setComponentAt(IDX_PANEL_ZEITGUTSCHRIFT, panelSplitZeitgutschrift);
		} else {
			// filter refreshen.
			panelQueryZeitgutschrift
					.setDefaultFilter(ZeiterfassungFilterFactory.getInstance().createFKSonderzeiten(key));
		}
	}

	private void createZeitabschluss(Integer key) throws Throwable {

		if (panelQueryZeitabschluss == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_PREVIOUS,
					PanelBasis.ACTION_NEXT, };
			panelQueryZeitabschluss = new PanelQuery(null, PersonalFilterFactory.getInstance().createFKPersonal(key),
					QueryParameters.UC_ID_ZEITABSCHLUSS, aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("zeiterfassung.title.tab.zeitabschluss"), true);
			panelBottomZeitabschluss = new PanelZeitabschluss(getInternalFrame(),
					LPMain.getTextRespectUISPr("zeiterfassung.title.tab.zeitabschluss"), null);

			panelSplitZeitabschluss = new PanelSplit(getInternalFrame(), panelBottomZeitabschluss,
					panelQueryZeitabschluss, 350);

			setComponentAt(IDX_PANEL_ZEITABSCHLUSS, panelSplitZeitabschluss);
		} else {
			// filter refreshen.
			panelQueryZeitabschluss
					.setDefaultFilter(ZeiterfassungFilterFactory.getInstance().createFKZeitabschluss(key));
		}
	}

	private void createBereitschaft(Integer key) throws Throwable {

		if (panelQueryBereitschaft == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_PREVIOUS, PanelBasis.ACTION_NEXT, };
			panelQueryBereitschaft = new PanelQuery(null,
					PersonalFilterFactory.getInstance().createFKBereitschaftPersonal(key),
					QueryParameters.UC_ID_BEREITSCHAFT, aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.bereitschaft"), true);
			panelBottomBereitschaft = new PanelBereitschaft(getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.bereitschaft"), null);

			panelSplitBereitschaft = new PanelSplit(getInternalFrame(), panelBottomBereitschaft, panelQueryBereitschaft,
					300);

			setComponentAt(IDX_PANEL_BEREITSCHAFT, panelSplitBereitschaft);
		} else {
			// filter refreshen.
			panelQueryBereitschaft
					.setDefaultFilter(PersonalFilterFactory.getInstance().createFKBereitschaftPersonal(key));
		}
	}

	private void jbInit() throws Throwable {
		IDX_PANEL_AUSWAHL = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.auswahl"), null, null,
				LPMain.getTextRespectUISPr("lp.auswahl"));
		IDX_PANEL_ZEITDATEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("zeiterfassung.title.tab.zeitdaten"), null,
				null, LPMain.getTextRespectUISPr("zeiterfassung.title.tab.zeitdaten"));

		if (getInternalFrameZeiterfassung().bRechtNurBuchen == false) {

			IDX_PANEL_SONDERZEITEN = reiterHinzufuegen(
					LPMain.getTextRespectUISPr("zeiterfassung.title.tab.sonderzeiten"), null, null,
					LPMain.getTextRespectUISPr("zeiterfassung.title.tab.sonderzeiten"));

			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_PERSONAL,
					ParameterFac.PARAMETER_ZEITERFASSUNG_MIT_ZEITGUTSCHRIFT);
			boolean bZeitgutschrift = (java.lang.Boolean) parameter.getCWertAsObject();

			if (bZeitgutschrift) {
				// SP8196
				boolean bSonderzeitenCUD = DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_SONDERZEITEN_CUD);
				if (bSonderzeitenCUD) {
					IDX_PANEL_ZEITGUTSCHRIFT = reiterHinzufuegen(LPMain.getTextRespectUISPr("pers.zeitgutschrift"),
							null, null, LPMain.getTextRespectUISPr("pers.zeitgutschrift"));
				}
			}

		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZEITEN_ABSCHLIESSEN)) {

			IDX_PANEL_ZEITABSCHLUSS = reiterHinzufuegen(
					LPMain.getTextRespectUISPr("zeiterfassung.title.tab.zeitabschluss"), null, null,
					LPMain.getTextRespectUISPr("zeiterfassung.title.tab.zeitabschluss"));
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_BEREITSCHAFT)) {
			IDX_PANEL_BEREITSCHAFT = reiterHinzufuegen(LPMain.getTextRespectUISPr("pers.bereitschaft"), null, null,
					LPMain.getTextRespectUISPr("pers.bereitschaft"));
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_REISEZEITEN)) {
			IDX_PANEL_REISEZEITEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("pers.reisezeiten"), null, null,
					LPMain.getTextRespectUISPr("pers.reisezeiten"));
			IDX_PANEL_REISESPESEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("pers.reisespesen"), null, null,
					LPMain.getTextRespectUISPr("pers.reisespesen"));
		}
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_TELEFONZEITERFASSUNG)) {
			IDX_PANEL_TELEFONZEITEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("pers.telefonzeiten"), null, null,
					LPMain.getTextRespectUISPr("pers.telefonzeiten"));
		}

		if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PROJEKT)) {

			if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_PROJ_PROJEKT_CUD)
					|| DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_PROJ_PROJEKT_R)) {
				IDX_PANEL_PROJEKTQUEUE = reiterHinzufuegen(LPMain.getTextRespectUISPr("proj.projekt.queue"), null, null,
						LPMain.getTextRespectUISPr("proj.projekt.queue"));

			}

		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_HVMA_ZEITERFASSUNG)
				&& !DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_ZEITEINGABE_NUR_BUCHEN)) {
			IDX_PANEL_ZEITDATENPRUEFEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("pers.zeitdatenpruefen"), null,
					null, LPMain.getTextRespectUISPr("pers.zeitdatenpruefen.tooltip"));
		}

		createAuswahl();

		// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
		// restlichen panel deaktivieren
		if (panelQueryPersonal.getSelectedId() == null) {
			getInternalFrame().enableAllPanelsExcept(false);
		}
		if ((Integer) panelQueryPersonal.getSelectedId() != null) {
			getInternalFrameZeiterfassung().setPersonalDto(DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey((Integer) panelQueryPersonal.getSelectedId()));
		}

		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		this.addChangeListener(this);

		// Damit D2 einen Aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryPersonal, ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

	}

	public void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getTextRespectUISPr("zeiterfassung.modulname"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		if (getInternalFrameZeiterfassung().getPersonalDto() != null) {
			String sNachname = getInternalFrameZeiterfassung().getPersonalDto().getPartnerDto()
					.getCName1nachnamefirmazeile1();
			if (sNachname == null) {
				sNachname = "";
			}
			String sVorname = getInternalFrameZeiterfassung().getPersonalDto().getPartnerDto()
					.getCName2vornamefirmazeile2();
			if (sVorname == null) {
				sVorname = "";
			}
			if (getInternalFrameZeiterfassung().getPersonalDto() != null) {
				getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
						getInternalFrameZeiterfassung().getPersonalDto().getCPersonalnr() + ", " + sVorname + " "
								+ sNachname);
			}
		}
	}

	public InternalFrameZeiterfassung getInternalFrameZeiterfassung() {
		return (InternalFrameZeiterfassung) getInternalFrame();
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {

		boolean bDarfNurVorschauSehen = true;

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_ZEITERFASSUNG_JOURNALE_DRUCKEN)) {
			bDarfNurVorschauSehen = false;
		}

		if (e.getActionCommand().equals(MENUE_ACTION_ZEITDATEN)) {
			String add2Title = LPMain.getTextRespectUISPr("zeiterfassung.report.zeitdatenjournal");
			getInternalFrame().showReportKriterien(
					new ReportZeiterfassungZeitdaten(getInternalFrameZeiterfassung(), add2Title),
					bDarfNurVorschauSehen);

		} else if (e.getActionCommand().equals(MENUE_ACTION_MONATSABRECHNUNG)) {
			boolean bDarfMonatsabrechnungDrucken = false;
			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PERS_ZEITERFASSUNG_MONATSABRECHNUNG_DRUCKEN)) {
				bDarfMonatsabrechnungDrucken = true;
			}
			String add2Title = LPMain.getTextRespectUISPr("zeiterfassung.report.monatsabrechnung");
			getInternalFrame().showReportKriterien(
					new ReportMonatsabrechnung(getInternalFrameZeiterfassung(), add2Title),
					!bDarfMonatsabrechnungDrucken);

		} else if (e.getActionCommand().equals(MENUE_ACTION_LOHNDATENEXPORT)) {

			String add2Title = LPMain.getTextRespectUISPr("pers.zeitdaten.export");
			getInternalFrame()
					.showReportKriterien(new ReportLohndatenexport(getInternalFrameZeiterfassung(), add2Title));

		} else if (e.getActionCommand().equals(MENUE_ACTION_ZEITSALDO)) {
			boolean bDarfMonatsabrechnungDrucken = false;
			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PERS_ZEITERFASSUNG_MONATSABRECHNUNG_DRUCKEN)) {
				bDarfMonatsabrechnungDrucken = true;
			}
			String add2Title = LPMain.getTextRespectUISPr("zeiterfassung.report.zeitsaldo");
			getInternalFrame().showReportKriterien(new ReportZeitsaldo(getInternalFrameZeiterfassung(), add2Title),
					!bDarfMonatsabrechnungDrucken);

		} else if (e.getActionCommand().equals(MENUE_ACTION_MITARBEITERUEBERSICHT)) {
			String add2Title = LPMain.getTextRespectUISPr("zeiterfassung.report.mitarbeiteruebersicht");
			getInternalFrame().showReportKriterien(
					new ReportMitarbeiteruebersicht(getInternalFrameZeiterfassung(), add2Title), bDarfNurVorschauSehen);

		} else if (e.getActionCommand().equals(MENUE_ACTION_MITARBEITEREINTEILUNG)) {
			String add2Title = LPMain.getTextRespectUISPr("pers.zeiterfassung.report.mitarbeitereinteilung");
			getInternalFrame().showReportKriterien(
					new ReportMitarbeitereinteilung(getInternalFrameZeiterfassung(), add2Title), bDarfNurVorschauSehen);

		} else if (e.getActionCommand().equals(MENUE_ACTION_MASCHINENERFOLG)) {
			String add2Title = LPMain.getTextRespectUISPr("pers.zeiterfassung.report.maschinenerfolg");
			getInternalFrame().showReportKriterien(
					new ReportMaschinenerfolg(getInternalFrameZeiterfassung(), add2Title), bDarfNurVorschauSehen);

		} else if (e.getActionCommand().equals(MENUE_ACTION_WOCHENABRECHNUNG)) {
			boolean bDarfMonatsabrechnungDrucken = false;
			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PERS_ZEITERFASSUNG_MONATSABRECHNUNG_DRUCKEN)) {
				bDarfMonatsabrechnungDrucken = true;
			}
			String add2Title = LPMain.getTextRespectUISPr("zeiterfassung.report.wochenabrechnung");
			getInternalFrame().showReportKriterien(
					new ReportWochenabrechnung(getInternalFrameZeiterfassung(), add2Title),
					!bDarfMonatsabrechnungDrucken);

		} else if (e.getActionCommand().equals(MENUE_ACTION_WOCHENJOURNAL)) {
			boolean bDarfMonatsabrechnungDrucken = false;
			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PERS_ZEITERFASSUNG_MONATSABRECHNUNG_DRUCKEN)) {
				bDarfMonatsabrechnungDrucken = true;
			}
			String add2Title = LPMain.getTextRespectUISPr("zeiterfassung.report.wochenjournal");
			getInternalFrame().showReportKriterien(new ReportWochenjournal(getInternalFrameZeiterfassung(), add2Title),
					!bDarfMonatsabrechnungDrucken);

		} else if (e.getActionCommand().equals(MENUE_ACTION_SONDERZEITEN_IMPORT)) {
			HvOptional<XlsFile> xlsFile = FileOpenerFactory.sonderzeitenImportXls(this);
			if (!xlsFile.isPresent())
				return;

			// Eigentliche Datenaufbereitung
			java.sql.Date[] datum = DialogFactory
					.showDatumseingabeVonBis(LPMain.getTextRespectUISPr("pers.sondertaetigkeit.import.datumvonbis"));

			SonderzeitenImportXls importXls = new SonderzeitenImportXls();
			importXls.importFile(xlsFile.get().getFile(), datum[0], datum[1]);

			DelegateFactory.getInstance().getZeiterfassungDelegate().importiereSonderzeiten(importXls.getStartDate(),
					importXls.getEndDate(), importXls.getImportData());

		} else if (e.getActionCommand().equals(MENUE_ACTION_ANWESENHEITSLISTE)) {
			String add2Title = LPMain.getTextRespectUISPr("zeiterfassung.report.anwesenheitsliste");
			getInternalFrame().showReportKriterien(
					new ReportAnwesenheitsliste(getInternalFrameZeiterfassung(), add2Title), bDarfNurVorschauSehen);

		} else if (e.getActionCommand().equals(MENUE_ACTION_ABGESCHLOSSENE_ZEITEN)) {
			String add2Title = LPMain.getTextRespectUISPr("zeiterfassung.report.abgeschlossenezeitbuchungen");
			getInternalFrame().showReportKriterien(
					new ReportAbgeschlosseneZeiten(getInternalFrameZeiterfassung(), add2Title), bDarfNurVorschauSehen);

		} else if (e.getActionCommand().equals(MENUE_ACTION_LETZTE_SYNCHRONISATION)) {
			String add2Title = LPMain.getTextRespectUISPr("personal.report.letztesynchronisation");
			getInternalFrame().showReportKriterien(
					new ReportLetzteSynchronisation(getInternalFrameZeiterfassung(), add2Title), bDarfNurVorschauSehen);

		} else if (e.getActionCommand().equals(MENUE_ACTION_SONDERZEITENLISTE)) {
			String add2Title = LPMain.getTextRespectUISPr("zeiterfassung.report.sonderzeitenliste");
			getInternalFrame().showReportKriterien(
					new ReportSonderzeitenliste(getInternalFrameZeiterfassung(), add2Title), bDarfNurVorschauSehen);

		} else if (e.getActionCommand().equals(MENUE_ACTION_AUFTRAGSZEITSTATISTIK)) {
			String add2Title = LPMain.getTextRespectUISPr("personal.report.auftragszeitstatistik");
			getInternalFrame().showReportKriterien(
					new ReportAuftragszeitstatistik(getInternalFrameZeiterfassung(), add2Title), bDarfNurVorschauSehen);

		} else if (e.getActionCommand().equals(MENUE_ACTION_WOCHENABSCHLUSS)) {
			String add2Title = LPMain.getTextRespectUISPr("pers.report.wochenabschluss");
			getInternalFrame().showReportKriterien(
					new ReportWochenabschluss(getInternalFrameZeiterfassung(), add2Title), bDarfNurVorschauSehen);

		} else if (e.getActionCommand().equals(MENUE_ACTION_PRODUKTIVITAETSSTATISTIK)) {
			String add2Title = LPMain.getTextRespectUISPr("zeiterfassung.report.produktivitaetsstatistik");
			getInternalFrame().showReportKriterien(
					new ReportProduktivitaetsstatistik(getInternalFrameZeiterfassung(), add2Title),
					bDarfNurVorschauSehen);

		} else if (e.getActionCommand().equals(MENUE_ACTION_PRODUKTIVITAETSTAGESSTATISTIK)) {
			String add2Title = LPMain.getTextRespectUISPr("zeiterfassung.report.produktivitaetstagesstatistik");
			getInternalFrame().showReportKriterien(
					new ReportProduktivitaetstagesstatistik(getInternalFrameZeiterfassung(), add2Title),
					bDarfNurVorschauSehen);

		} else if (e.getActionCommand().equals(MENUE_ACTION_ARBEITSZEITSTATISTIK)) {
			String add2Title = LPMain.getTextRespectUISPr("zeiterfassung.report.arbeitszeitstatistik");
			getInternalFrame().showReportKriterien(
					new ReportArbeitszeitstatistik(getInternalFrameZeiterfassung(), add2Title), bDarfNurVorschauSehen);

		} else if (e.getActionCommand().equals(MENUE_ACTION_REISEZEITEN)) {
			String add2Title = LPMain.getTextRespectUISPr("pers.reisezeiten");
			getInternalFrame().showReportKriterien(new ReportReisezeiten(getInternalFrameZeiterfassung(), add2Title),
					bDarfNurVorschauSehen);

		} else if (e.getActionCommand().equals(MENUE_ACTION_FAHRTENBUCH)) {
			String add2Title = LPMain.getTextRespectUISPr("pers.reisezeiten.fahrtenbuch");
			getInternalFrame().showReportKriterien(new ReportFahrtenbuch(getInternalFrameZeiterfassung(), add2Title),
					bDarfNurVorschauSehen);

		} else if (e.getActionCommand().equals(MENUE_ACTION_TELEFONZEITERFASSUNG)) {
			String add2Title = LPMain.getTextRespectUISPr("pers.telefonzeiten");
			getInternalFrame().showReportKriterien(new ReportTelefonzeiten(getInternalFrameZeiterfassung(), add2Title),
					bDarfNurVorschauSehen);

		}

		else if (e.getActionCommand().equals(MENU_ACTION_PFLEGE_AUTOMATIKBUCHUNGEN)) {

			java.util.Date dDatum = new java.sql.Date(System.currentTimeMillis());
			if (getPanelBottomZeitdaten() != null && getPanelBottomZeitdaten().getWdfDatum() != null
					&& getPanelBottomZeitdaten().getWdfDatum().getDate() != null) {
				dDatum = getPanelBottomZeitdaten().getWdfDatum().getDate();
			}

			pdKriterienAutomatikbuchungen = new PanelDialogAutomatikbuchungen(getInternalFrame(), dDatum,
					LPMain.getTextRespectUISPr("zeiterfassung.automatikbuchungen"));

			getInternalFrame().showPanelDialog(pdKriterienAutomatikbuchungen);
		} else if (e.getActionCommand().equals(MENU_ACTION_PFLEGE_NEGATIVE_STUNDEN_IN_URLAUB_WANDELN)) {

			pdNegativstundenInUrlaubUmwandeln = new PanelDialogNegativstundenInUrlaubUmwandeln(getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.zeiterfassung.negativestunden.inurlaubwandeln"));

			getInternalFrame().showPanelDialog(pdNegativstundenInUrlaubUmwandeln);
		} else if (e.getActionCommand().equals(MENU_ACTION_PFLEGE_SCHICHTZEITMODELLE)) {
			pdKriterienSchichtzeitmodelle = new PanelDialogSchichtzeitmodelle(getInternalFrame(),
					LPMain.getTextRespectUISPr("zeiterfassung.schichtzeitmodelle"));

			getInternalFrame().showPanelDialog(pdKriterienSchichtzeitmodelle);
		} else if (e.getActionCommand().equals(MENU_ACTION_PFLEGE_LOSZEITEN_VERSCHIEBEN)) {

			boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					LPMain.getTextRespectUISPr("fert.pflege.loszeitenverschieben.info"));

			if (b == true) {

				panelQueryFLRLosQuelle = FertigungFilterFactory.getInstance().createPanelFLRLose(getInternalFrame(),
						null, true);
				panelQueryFLRLosQuelle
						.setAdd2Title(LPMain.getTextRespectUISPr("fert.pflege.loszeitenverschieben.quelle"));
				new DialogQuery(panelQueryFLRLosQuelle);
			}
		} else if (e.getActionCommand().equals(MENU_INFO_AENDERUNGEN)) {

			String add2Title = LPMain.getTextRespectUISPr("lp.report.aenderungen");
			getInternalFrame().showReportKriterien(
					new ReportAenderungenZeiterfassung(getInternalFrameZeiterfassung(), add2Title));
		}
	}

	/**
	 * changed
	 * 
	 * @param e ItemChangedEvent
	 * @throws Throwable
	 */
	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryPersonal) {
				Integer iId = (Integer) panelQueryPersonal.getSelectedId();
				createZeitdaten(iId, new java.sql.Date(System.currentTimeMillis()));
				if (iId != null) {
					setSelectedComponent(panelSplitZeitdaten);
				}
			} else if (e.getSource() == panelQueryFLRLosQuelle) {

				losIId_Quelle = (Integer) panelQueryFLRLosQuelle.getSelectedId();

				panelQueryFLRLosZiel = FertigungFilterFactory.getInstance().createPanelFLRLose(getInternalFrame(), null,
						true);
				panelQueryFLRLosZiel.setAdd2Title(LPMain.getTextRespectUISPr("fert.pflege.loszeitenverschieben.ziel"));
				new DialogQuery(panelQueryFLRLosZiel);
			} else if (e.getSource() == panelQueryFLRLosZiel) {

				DelegateFactory.getInstance().getZeiterfassungDelegate().loszeitenVerschieben(losIId_Quelle,
						(Integer) panelQueryFLRLosZiel.getSelectedId());

				losIId_Quelle = null;

			} else if (e.getSource() == panelQueryFLRFavoriten) {
				erstelleZeitdatenAusVorschlag((ZeiterfassungFavoritenDto) panelQueryFLRFavoriten.getSelectedId());

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomZeitdaten) {
				panelSplitZeitdaten.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSonderzeiten) {
				panelSplitSonderzeiten.eventYouAreSelected(false);
				if (panelQuerySonderzeiten.getHmOfButtons().get(LEAVEALONE_WANDLE_URLAUBSANTRAG_UM) != null) {
					panelQuerySonderzeiten.getHmOfButtons().get(LEAVEALONE_WANDLE_URLAUBSANTRAG_UM).getButton()
							.setEnabled(true);
				}

			} else if (e.getSource() == panelBottomBereitschaft) {
				panelSplitBereitschaft.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZeitgutschrift) {
				panelSplitZeitgutschrift.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomReisezeiten) {
				panelSplitReisezeiten.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomTelefonzeiten) {
				panelSplitTelefonzeiten.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZeitabschluss) {
				panelSplitZeitabschluss.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomReisespesen) {
				panelSplitReisespesen.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomSonderzeiten) {
				panelQuerySonderzeiten.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				if (panelQuerySonderzeiten.getHmOfButtons().get(LEAVEALONE_WANDLE_URLAUBSANTRAG_UM) != null) {
					panelQuerySonderzeiten.getHmOfButtons().get(LEAVEALONE_WANDLE_URLAUBSANTRAG_UM).getButton()
							.setEnabled(false);
				}

			} else if (e.getSource() == panelBottomZeitdaten) {
				panelQueryZeitdaten.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomBereitschaft) {
				panelQueryBereitschaft.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomZeitgutschrift) {
				panelQueryZeitgutschrift.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomReisezeiten) {
				panelQueryReisezeiten.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomTelefonzeiten) {
				panelQueryTelefonzeiten.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomZeitabschluss) {
				panelQueryZeitabschluss.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomReisespesen) {
				panelQueryReisespesen.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomZeitdaten) {
				Object oKey = panelBottomZeitdaten.getKeyWhenDetailPanel();
				panelQueryZeitdaten.eventYouAreSelected(false);
				panelQueryZeitdaten.setSelectedId(oKey);
				panelSplitZeitdaten.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSonderzeiten) {
				Object oKey = panelBottomSonderzeiten.getKeyWhenDetailPanel();
				panelQuerySonderzeiten.eventYouAreSelected(false);
				panelQuerySonderzeiten.setSelectedId(oKey);
				panelSplitSonderzeiten.eventYouAreSelected(false);
				if (panelQuerySonderzeiten.getHmOfButtons().get(LEAVEALONE_WANDLE_URLAUBSANTRAG_UM) != null) {
					panelQuerySonderzeiten.getHmOfButtons().get(LEAVEALONE_WANDLE_URLAUBSANTRAG_UM).getButton()
							.setEnabled(true);
				}

			} else if (e.getSource() == panelBottomBereitschaft) {
				Object oKey = panelBottomBereitschaft.getKeyWhenDetailPanel();
				panelQueryBereitschaft.eventYouAreSelected(false);
				panelQueryBereitschaft.setSelectedId(oKey);
				panelSplitBereitschaft.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZeitgutschrift) {
				Object oKey = panelBottomZeitgutschrift.getKeyWhenDetailPanel();
				panelQueryZeitgutschrift.eventYouAreSelected(false);
				panelQueryZeitgutschrift.setSelectedId(oKey);
				panelSplitZeitgutschrift.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomReisezeiten) {
				Object oKey = panelBottomReisezeiten.getKeyWhenDetailPanel();
				panelQueryReisezeiten.eventYouAreSelected(false);
				panelQueryReisezeiten.setSelectedId(oKey);
				panelSplitReisezeiten.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomTelefonzeiten) {
				Object oKey = panelBottomTelefonzeiten.getKeyWhenDetailPanel();
				panelQueryTelefonzeiten.eventYouAreSelected(false);
				panelQueryTelefonzeiten.setSelectedId(oKey);
				panelSplitTelefonzeiten.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZeitabschluss) {
				Object oKey = panelBottomZeitabschluss.getKeyWhenDetailPanel();
				panelQueryZeitabschluss.eventYouAreSelected(false);
				panelQueryZeitabschluss.setSelectedId(oKey);
				panelSplitZeitabschluss.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomReisespesen) {
				Object oKey = panelBottomReisespesen.getKeyWhenDetailPanel();
				panelQueryReisespesen.eventYouAreSelected(false);
				panelQueryReisespesen.setSelectedId(oKey);
				panelSplitReisespesen.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryPersonal) {
				if (panelQueryPersonal.getSelectedId() != null) {
					getInternalFrameZeiterfassung().setKeyWasForLockMe(panelQueryPersonal.getSelectedId() + "");

					// Dto-setzen

					getInternalFrameZeiterfassung().setPersonalDto(DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) panelQueryPersonal.getSelectedId()));
					refreshTitle();
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL,
							((ISourceEvent) e.getSource()).getIdSelected() != null);

					if (IDX_PANEL_ZEITDATENPRUEFEN != -1) {
						boolean hasZeitdatenpruefen = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.hasZeitdatenpruefen((Integer) panelQueryPersonal.getSelectedId());
						setEnabledAt(IDX_PANEL_ZEITDATENPRUEFEN, hasZeitdatenpruefen);
					}
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
				}

			} else if (e.getSource() == panelQueryZeitdaten) {
				Integer iId = (Integer) panelQueryZeitdaten.getSelectedId();
				panelBottomZeitdaten.setKeyWhenDetailPanel(iId);
				panelBottomZeitdaten.eventYouAreSelected(false);
				panelQueryZeitdaten.updateButtons();
			} else if (e.getSource() == panelQuerySonderzeiten) {
				Integer iId = (Integer) panelQuerySonderzeiten.getSelectedId();
				panelBottomSonderzeiten.setKeyWhenDetailPanel(iId);
				panelBottomSonderzeiten.eventYouAreSelected(false);
				panelQuerySonderzeiten.updateButtons();
				if (panelQuerySonderzeiten.getHmOfButtons().get(LEAVEALONE_WANDLE_URLAUBSANTRAG_UM) != null) {
					panelQuerySonderzeiten.getHmOfButtons().get(LEAVEALONE_WANDLE_URLAUBSANTRAG_UM).getButton()
							.setEnabled(true);
				}

			} else if (e.getSource() == panelQueryBereitschaft) {
				Integer iId = (Integer) panelQueryBereitschaft.getSelectedId();
				panelBottomBereitschaft.setKeyWhenDetailPanel(iId);
				panelBottomBereitschaft.eventYouAreSelected(false);
				panelQueryBereitschaft.updateButtons();
			} else if (e.getSource() == panelQueryZeitgutschrift) {
				Integer iId = (Integer) panelQueryZeitgutschrift.getSelectedId();
				panelBottomZeitgutschrift.setKeyWhenDetailPanel(iId);
				panelBottomZeitgutschrift.eventYouAreSelected(false);
				panelQueryZeitgutschrift.updateButtons();
			} else if (e.getSource() == panelQueryReisezeiten) {
				Integer iId = (Integer) panelQueryReisezeiten.getSelectedId();
				panelBottomReisezeiten.setKeyWhenDetailPanel(iId);
				panelBottomReisezeiten.eventYouAreSelected(false);
				panelQueryReisezeiten.updateButtons();
			} else if (e.getSource() == panelQueryTelefonzeiten) {
				Integer iId = (Integer) panelQueryTelefonzeiten.getSelectedId();
				panelBottomTelefonzeiten.setKeyWhenDetailPanel(iId);
				panelBottomTelefonzeiten.eventYouAreSelected(false);
				panelQueryTelefonzeiten.updateButtons();
			} else if (e.getSource() == panelQueryZeitabschluss) {
				Integer iId = (Integer) panelQueryZeitabschluss.getSelectedId();
				panelBottomZeitabschluss.setKeyWhenDetailPanel(iId);
				panelBottomZeitabschluss.eventYouAreSelected(false);
				panelQueryZeitabschluss.updateButtons();
			} else if (e.getSource() == panelQueryQueue) {
				if (panelQueryPersonal.getSelectedId() != null) {
					Double dGesamt = DelegateFactory.getInstance().getProjektDelegate()
							.berechneGesamtSchaetzung((Integer) panelQueryPersonal.getSelectedId());
					panelQueryQueue.setStatusbarSpalte6(
							LPMain.getTextRespectUISPr("proj.queue.schaetzung") + " " + dGesamt, true);
				}
			} else if (e.getSource() == panelQueryZeitdatenpruefen) {
				Integer iId = (Integer) panelQueryZeitdatenpruefen.getSelectedId();
				panelBottomZeitdatenpruefen.setKeyWhenDetailPanel(iId);
				panelBottomZeitdatenpruefen.eventYouAreSelected(false);
				panelQueryZeitdatenpruefen.updateButtons();
			} else if (e.getSource() == panelQueryReisespesen) {
				Integer iId = (Integer) panelQueryReisespesen.getSelectedId();
				panelBottomReisespesen.setKeyWhenDetailPanel(iId);
				panelBottomReisespesen.eventYouAreSelected(false);
				panelQueryReisespesen.updateButtons();
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelBottomZeitdaten) {
				setKeyWasForLockMe();
				if (panelBottomZeitdaten.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryZeitdaten.getId2SelectAfterDelete();

					if (!isBVonBisErfassung()) {
						panelQueryZeitdaten.setSelectedId(oNaechster);
					}

				}
				panelSplitZeitdaten.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSonderzeiten) {
				setKeyWasForLockMe();
				if (panelBottomSonderzeiten.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQuerySonderzeiten.getId2SelectAfterDelete();
					panelQuerySonderzeiten.setSelectedId(oNaechster);
				}
				panelSplitSonderzeiten.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomBereitschaft) {
				setKeyWasForLockMe();
				if (panelBottomBereitschaft.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryBereitschaft.getId2SelectAfterDelete();
					panelQueryBereitschaft.setSelectedId(oNaechster);
				}
				panelSplitBereitschaft.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZeitgutschrift) {
				setKeyWasForLockMe();
				if (panelBottomZeitgutschrift.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryZeitgutschrift.getId2SelectAfterDelete();
					panelQueryZeitgutschrift.setSelectedId(oNaechster);
				}
				panelSplitZeitgutschrift.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomReisezeiten) {
				setKeyWasForLockMe();
				if (panelBottomReisezeiten.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryReisezeiten.getId2SelectAfterDelete();
					panelQueryReisezeiten.setSelectedId(oNaechster);
				}
				panelSplitReisezeiten.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomTelefonzeiten) {
				setKeyWasForLockMe();
				if (panelBottomTelefonzeiten.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryTelefonzeiten.getId2SelectAfterDelete();
					panelQueryTelefonzeiten.setSelectedId(oNaechster);
				}
				panelSplitTelefonzeiten.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZeitabschluss) {
				setKeyWasForLockMe();
				if (panelBottomZeitabschluss.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryZeitabschluss.getId2SelectAfterDelete();
					panelQueryZeitabschluss.setSelectedId(oNaechster);
				}
				panelSplitZeitabschluss.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomReisespesen) {
				setKeyWasForLockMe();
				if (panelBottomReisespesen.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryReisespesen.getId2SelectAfterDelete();
					panelQueryReisespesen.setSelectedId(oNaechster);
				}
				panelSplitReisespesen.eventYouAreSelected(false);
			}
			if (e.getSource() == panelBottomZeitdatenpruefen) {
				setKeyWasForLockMe();
				if (panelBottomZeitdatenpruefen.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryZeitdatenpruefen.getId2SelectAfterDelete();

					if (!isBVonBisErfassung()) {
						panelQueryZeitdatenpruefen.setSelectedId(oNaechster);
					}

				}
				panelSplitZeitdatenpruefen.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			super.lPEventItemChanged(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryZeitdaten) {

				panelBottomZeitdaten.eventActionNew(e, true, false);
				panelBottomZeitdaten.eventYouAreSelected(false);
				panelBottomZeitdaten.getWdfDatum().setEnabled(false);
				this.setSelectedComponent(panelSplitZeitdaten);
			} else if (e.getSource() == panelQuerySonderzeiten) {
				panelBottomSonderzeiten.eventActionNew(e, true, false);
				panelBottomSonderzeiten.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitSonderzeiten);
				if (panelQuerySonderzeiten.getHmOfButtons().get(LEAVEALONE_WANDLE_URLAUBSANTRAG_UM) != null) {
					panelQuerySonderzeiten.getHmOfButtons().get(LEAVEALONE_WANDLE_URLAUBSANTRAG_UM).getButton()
							.setEnabled(false);
				}

			} else if (e.getSource() == panelQueryBereitschaft) {
				panelBottomBereitschaft.eventActionNew(e, true, false);
				panelBottomBereitschaft.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitBereitschaft);
			} else if (e.getSource() == panelQueryZeitgutschrift) {
				panelBottomZeitgutschrift.eventActionNew(e, true, false);
				panelBottomZeitgutschrift.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitZeitgutschrift);
			} else if (e.getSource() == panelQueryReisezeiten) {
				panelBottomReisezeiten.eventActionNew(e, true, false);
				panelBottomReisezeiten.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitReisezeiten);
			} else if (e.getSource() == panelQueryTelefonzeiten) {
				panelBottomTelefonzeiten.eventActionNew(e, true, false);
				panelBottomTelefonzeiten.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitTelefonzeiten);
			} else if (e.getSource() == panelQueryZeitabschluss) {
				panelBottomZeitabschluss.eventActionNew(e, true, false);
				panelBottomZeitabschluss.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitZeitabschluss);
			} else if (e.getSource() == panelQueryReisespesen) {
				panelBottomReisespesen.eventActionNew(e, true, false);
				panelBottomReisespesen.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitReisespesen);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {

			if (e.getSource() == pdKriterienAutomatikbuchungen) {

				Integer personlIId = null;
				boolean bLoeschen = false;

				if (!pdKriterienAutomatikbuchungen.isAlleSelected()) {
					personlIId = pdKriterienAutomatikbuchungen.getPersonlIId();
				}
				if (pdKriterienAutomatikbuchungen.isLoeschen()) {
					bLoeschen = true;
				}

				if (pdKriterienAutomatikbuchungen.pruefeObBuchungMoeglich()) {
					getInternalFrame().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
					try {
						com.lp.client.frame.delegate.DelegateFactory.getInstance().getZeiterfassungDelegate()
								.automatikbuchungenAufrollen(personlIId, pdKriterienAutomatikbuchungen.getTVon(),
										pdKriterienAutomatikbuchungen.getTBis(), bLoeschen);
					} catch (ExceptionLP e1) {

						handleException(e1, false);

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("pers.automatikbuchungen.aufrollen.error"));

					}

					getInternalFrame().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
				}

				pdKriterienAutomatikbuchungen.eventYouAreSelected(false);
				setSelectedComponent(panelQueryPersonal);
				// pdKriterienAutomatikbuchungen.updateButtons(new
				// LockStateValue(null, null,
				// PanelBasis.LOCK_IS_NOT_LOCKED));
			} else if (e.getSource() == pdKriterienSchichtzeitmodelle) {

				Integer personlIId = null;

				if (!pdKriterienSchichtzeitmodelle.isAlleSelected()) {
					personlIId = pdKriterienSchichtzeitmodelle.getPersonlIId();
				}

				if (pdKriterienSchichtzeitmodelle.pruefeObBuchungMoeglich()) {
					getInternalFrame().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
					try {
						com.lp.client.frame.delegate.DelegateFactory.getInstance().getZeiterfassungDelegate()
								.schichtzeitmodelleAufrollen(personlIId, pdKriterienSchichtzeitmodelle.getTVon(),
										pdKriterienSchichtzeitmodelle.getTBis());
					} catch (ExceptionLP e1) {

						handleException(e1, false);

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("pers.schichtzeitmodelle.aufrollen.error"));

					}

					getInternalFrame().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
				}

				pdKriterienSchichtzeitmodelle.eventYouAreSelected(false);
				setSelectedComponent(panelQueryPersonal);
				// pdKriterienAutomatikbuchungen.updateButtons(new
				// LockStateValue(null, null,
				// PanelBasis.LOCK_IS_NOT_LOCKED));
			} else if (e.getSource() == pdNegativstundenInUrlaubUmwandeln) {

				Integer personlIId = null;

				if (!pdNegativstundenInUrlaubUmwandeln.isAlleSelected()) {
					personlIId = pdNegativstundenInUrlaubUmwandeln.getPersonlIId();
				}

				if (pdNegativstundenInUrlaubUmwandeln.pruefeObBuchungMoeglich()) {
					getInternalFrame().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
					try {
						com.lp.client.frame.delegate.DelegateFactory.getInstance().getZeiterfassungDelegate()
								.negativstundenInUrlaubUmwandeln(pdNegativstundenInUrlaubUmwandeln.getJahr(),
										pdNegativstundenInUrlaubUmwandeln.getMonat(), personlIId);
					} catch (ExceptionLP e1) {

						boolean bErrorBekannt = handleException(e1, false);

						if (bErrorBekannt == false) {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("pers.schichtzeitmodelle.aufrollen.error"));
						}

					}

					getInternalFrame().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
				}

				pdNegativstundenInUrlaubUmwandeln.eventYouAreSelected(false);
				setSelectedComponent(panelQueryPersonal);
				// pdKriterienAutomatikbuchungen.updateButtons(new
				// LockStateValue(null, null,
				// PanelBasis.LOCK_IS_NOT_LOCKED));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (e.getSource() == panelQueryQueue) {
				if (sAspectInfo.equals(MY_OWN_NEW_AUS_QUEUE_ENTFERNEN)) {
					if (panelQueryQueue.getTable().getRowCount() > 0) {
						Integer iIdPosition = (Integer) panelQueryQueue.getSelectedId();
						DelegateFactory.getInstance().getProjektDelegate().ausQueueEntfernen(iIdPosition);
						panelQueryQueue.eventYouAreSelected(false);
					}
				}
			} else if (e.getSource() == panelQueryZeitdaten) {
				if (sAspectInfo.equals(MY_OWN_NEW_FAVORITENLISTE)) {

					dialogQueryFavoritenliste(null);

				} else if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
					einfuegenHV();
				}
			} else if (e.getSource() == panelQuerySonderzeiten) {

				if (sAspectInfo.equals(MY_OWN_NEW_URLAUBSANTRAG)) {

					PersonalDto personalDto_Selektiert = DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) getPanelQueryPersonal().getSelectedId());

					PersonalDto personalDto_Angemeldet = DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());

					// SP3571
					if (personalDto_Selektiert.getCEmail() == null) {

						// SP3844

						if (personalDto_Angemeldet.getCEmail() == null) {

							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), personalDto_Selektiert
									.getPartnerDto().formatFixName1Name2() + " / "
									+ personalDto_Angemeldet.getPartnerDto().formatFixName1Name2() + " "
									+ LPMain.getTextRespectUISPr("pers.sonderzeiten.urlaubsantrag.keinabsender"));
							return;
						}

					}

					DialogUrlaubsantrag d = new DialogUrlaubsantrag();
					d.setTitle(LPMain.getTextRespectUISPr("pers.sonderzeiten.urlaubsantrag.vonbis"));
					LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);

					if (d.datumVon != null && d.datumBis != null) {
						SonderzeitenDto szDto = new SonderzeitenDto();
						szDto.setPersonalIId((Integer) getPanelQueryPersonal().getSelectedId());

						if (d.isUrlaubsantrag()) {
							szDto.setTaetigkeitIId(DelegateFactory.getInstance().getZeiterfassungDelegate()
									.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_URLAUBSANTRAG).getIId());
						} else if (d.isKrankantrag()) {
							szDto.setTaetigkeitIId(DelegateFactory.getInstance().getZeiterfassungDelegate()
									.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KRANKANTRAG).getIId());
						} else {
							szDto.setTaetigkeitIId(DelegateFactory.getInstance().getZeiterfassungDelegate()
									.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_ZAANTRAG).getIId());
						}

						if (d.wcbHalbtag.isSelected()) {
							szDto.setBTag(Helper.boolean2Short(false));
							szDto.setBHalbtag(Helper.boolean2Short(true));
						} else {

							szDto.setBTag(Helper.boolean2Short(true));
							szDto.setBHalbtag(Helper.boolean2Short(false));
						}

						// Aufgrund von SP7233 auskommentiert
						// java.sql.Timestamp[] buchungenVorhanden = DelegateFactory.getInstance()
						// .getZeiterfassungDelegate().sindIstZeitenVorhandenWennUrlaubGebuchtWird(szDto,
						// new java.sql.Timestamp(d.datumVon.getTime()),
						// new java.sql.Timestamp(d.datumBis.getTime()));

						DelegateFactory.getInstance().getZeiterfassungDelegate().createSonderzeitenVonBis(szDto,
								new java.sql.Timestamp(d.datumVon.getTime()),
								new java.sql.Timestamp(d.datumBis.getTime()), null);

						panelQuerySonderzeiten.eventYouAreSelected(false);

						sendEmail(personalDto_Selektiert, personalDto_Angemeldet, d);
					}
				}

			} else if (e.getSource() == panelQueryFLRFavoriten) {

				if (panelQueryFLRFavoriten.getSelectedId() != null) {
					if (sAspectInfo.equals(MY_OWN_NEW_FAVORIT_UEBERNEHMEN)) {

						erstelleZeitdatenAusVorschlag(
								(ZeiterfassungFavoritenDto) panelQueryFLRFavoriten.getSelectedId());

					} else if (sAspectInfo.equals(MY_OWN_NEW_FAVORIT_SOFORT_ANLEGEN)) {

						ZeiterfassungFavoritenDto zfDto = (ZeiterfassungFavoritenDto) panelQueryFLRFavoriten
								.getSelectedId();

						ZeitdatenDto zDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.zeitdatenFindByPrimaryKey(zfDto.getZeitdatenIId());
						zDto.setTZeit(new Timestamp(System.currentTimeMillis()));
						zDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
						zDto.setTAendern(new Timestamp(System.currentTimeMillis()));
						zDto.setIId(null);
						if (zfDto.getArtikelIId() != null) {
							zDto.setArtikelIId(zfDto.getArtikelIId());
						}

						zDto.setCWowurdegebucht("Client: " + Helper.getPCName());

						Integer iIdNeu = DelegateFactory.getInstance().getZeiterfassungDelegate().createZeitdaten(zDto);

						panelBottomZeitdaten.getWdfDatum().setTimestamp(new Timestamp(System.currentTimeMillis()));

						panelSplitZeitdaten.eventYouAreSelected(false);
						panelQueryZeitdaten.setSelectedId(iIdNeu);
						panelSplitZeitdaten.eventYouAreSelected(false);

					}
					panelQueryFLRFavoriten.getDialog().setVisible(false);

				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			copyHV();
		} else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {

			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (e.getSource() == panelQuerySonderzeiten) {
				if (sAspectInfo.equals(LEAVEALONE_WANDLE_URLAUBSANTRAG_UM)) {

					PersonalDto personalDto_Angemeldet = DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());

					boolean bDarfUmwandeln = false;
					// Wenn ich GF oder Lohnverrechnung bin, dann darf immer
					// umgewandelt werden
					if (personalDto_Angemeldet.getPersonalfunktionCNr() != null && (personalDto_Angemeldet
							.getPersonalfunktionCNr().equals(PersonalFac.PERSONALFUNKTION_GESCHAEFTSFUEHRER)
							|| personalDto_Angemeldet.getPersonalfunktionCNr()
									.equals(PersonalFac.PERSONALFUNKTION_LOHNVERRECHNUNG))) {

						bDarfUmwandeln = true;
					} else {

						if (personalDto_Angemeldet.getPersonalfunktionCNr() != null && (personalDto_Angemeldet
								.getPersonalfunktionCNr().equals(PersonalFac.PERSONALFUNKTION_ABTEILUNGSLEITER))) {

							if (personalDto_Angemeldet.getIId()
									.equals(getInternalFrameZeiterfassung().getPersonalDto().getIId())) {
								// Der eigene Urlaubsantrag darf nur vom GF
								// umgewandelt werden
								DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
										LPMain.getTextRespectUISPr(
												"pers.sonderzeiten.urlaubsantragumwandeln.error.keinrecht2"));

							} else {
								// Wenn die Person in der Abteilung des
								// Angemeldeten Benutzers ist, dann darf
								// umgewandelt werden

								if (personalDto_Angemeldet.getKostenstelleDto_Abteilung() != null
										&& personalDto_Angemeldet.getKostenstelleDto_Abteilung()
												.equals(getInternalFrameZeiterfassung().getPersonalDto()
														.getKostenstelleDto_Abteilung())) {
									bDarfUmwandeln = true;
								} else {
									DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
											LPMain.getTextRespectUISPr(
													"pers.sonderzeiten.urlaubsantragumwandeln.error.keinrecht3"));
								}

							}

						} else {
							// Wenn kein Abteilungsleiter, dann darf er auch
							// nicht umwandeln

							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"), LPMain
									.getTextRespectUISPr("pers.sonderzeiten.urlaubsantragumwandeln.error.keinrecht"));

						}

					}

					if (bDarfUmwandeln) {
						Object[] ids = panelQuerySonderzeiten.getSelectedIds();
						if (ids != null) {
							Integer[] integerIIds = new Integer[ids.length];
							for (int i = 0; i < ids.length; i++) {
								integerIIds[i] = (Integer) ids[i];
							}

							// Report oeffnen

							DialogUrlaubsantragGenehmigen d = new DialogUrlaubsantragGenehmigen();
							LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
							d.setVisible(true);

							if (d.isbAbbruch() == false) {

								// SP4191
								boolean bBestehendeVorhanden = false;
								if (d.bGenehmigt == false) {
									// Wenn bestehende Urlaube/ZA vorhanden,
									// dann nachfragen ob diese ebenfalls
									// geloescht werden sollen

									for (int i = 0; i < integerIIds.length; i++) {

										String taetigkeitCNr = DelegateFactory.getInstance().getZeiterfassungDelegate()
												.sonderzeitenFindByPrimaryKey(integerIIds[i]).getTaetigkeitDto()
												.getCNr();

										if (taetigkeitCNr.equals(ZeiterfassungFac.TAETIGKEIT_URLAUB)
												|| taetigkeitCNr.equals(ZeiterfassungFac.TAETIGKEIT_ZEITAUSGLEICH)) {
											bBestehendeVorhanden = true;
										}

									}

								}

								boolean bBestehendeLoeschen = false;
								if (bBestehendeVorhanden == true) {
									bBestehendeLoeschen = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
											LPMain.getTextRespectUISPr("pers.antrag.umwandeln.bestehende.loeschen"));
								}

								// PJ22007
								Integer personalIId = (Integer) getPanelQueryPersonal().getSelectedId();

								PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
										.personalFindByPrimaryKey(personalIId);

								String add2Title = LPMain.getTextRespectUISPr("pers.sonderzeiten.urlaubsantrag");
								getInternalFrame().showReportKriterien(
										new ReportUrlaubsantrag(getInternalFrame(), personalIId, integerIIds,
												d.getSVoraussetzung(), d.bGenehmigt, add2Title),
										personalDto.getPartnerDto(), null);

								DelegateFactory.getInstance().getZeiterfassungDelegate()
										.wandleUrlaubsantragInUrlaubUm(integerIIds, d.bGenehmigt, bBestehendeLoeschen);
								panelSplitSonderzeiten.eventYouAreSelected(false);

							}
						}

					}
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == panelQueryQueue) {
				int iPos = panelQueryQueue.getTable().getSelectedRow();
				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryQueue.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryQueue.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getProjektDelegate().vertauscheProjekte(iIdPosition,
							iIdPositionMinus1, -3);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryQueue.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == panelQueryQueue) {
				int iPos = panelQueryQueue.getTable().getSelectedRow();
				// wenn die Position nicht die erste ist
				if (iPos < panelQueryQueue.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryQueue.getSelectedId();
					// wenn die Position nicht die letzte ist
					Integer iIdPositionPlus1 = (Integer) panelQueryQueue.getTable().getValueAt(iPos + 1, 0);
					DelegateFactory.getInstance().getProjektDelegate().vertauscheProjekte(iIdPosition, iIdPositionPlus1,
							-3);
					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryQueue.setSelectedId(iIdPosition);
				}
			}
		}

	}

	private void sendEmail(PersonalDto personalDto_Selektiert, PersonalDto personalDto_Angemeldet,
			DialogUrlaubsantrag d) throws Throwable {
		SonderzeitenAntragEmailDto szDto = new SonderzeitenAntragEmailDto(d.isUrlaubsantrag(), d.isKrankantrag(),
				d.datumVon, d.datumBis, personalDto_Angemeldet.getIId());

		DelegateFactory.getInstance().getZeiterfassungDelegate().createSonderzeitenEmail(szDto);
		/*
		 * // PJ18441 Mail an Abteilungsleiter bzw. GF String cEmpfaenger = null;
		 * 
		 * if (personalDto_Selektiert.getPersonalfunktionCNr() != null &&
		 * (personalDto_Selektiert .getPersonalfunktionCNr()
		 * .equals(PersonalFac.PERSONALFUNKTION_ABTEILUNGSLEITER) ||
		 * personalDto_Selektiert .getPersonalfunktionCNr()
		 * .equals(PersonalFac.PERSONALFUNKTION_GESCHAEFTSFUEHRER))) {
		 * 
		 * // Wenn ich Abteilungsleiter/GF bin, dann Mail an GF
		 * 
		 * PersonalDto[] aGeschaeftsfDto = DelegateFactory .getInstance()
		 * .getPersonalDelegate() .personalFindByMandantCNrPersonalfunktionCNr(
		 * LPMain.getTheClient().getMandant(),
		 * PersonalFac.PERSONALFUNKTION_GESCHAEFTSFUEHRER);
		 * 
		 * if (aGeschaeftsfDto != null && aGeschaeftsfDto.length > 0) { if
		 * (aGeschaeftsfDto[0] != null) { cEmpfaenger = aGeschaeftsfDto[0] .getCEmail();
		 * } }
		 * 
		 * } else { // Mail an Abteilungsleiter, wenn vorhanden
		 * 
		 * PersonalDto[] aAbteilungsleiterDto = DelegateFactory .getInstance()
		 * .getPersonalDelegate() .personalFindByMandantCNrPersonalfunktionCNr(
		 * LPMain.getTheClient().getMandant(),
		 * PersonalFac.PERSONALFUNKTION_ABTEILUNGSLEITER);
		 * 
		 * if (aAbteilungsleiterDto != null && aAbteilungsleiterDto.length > 0) {
		 * 
		 * for (int i = 0; i < aAbteilungsleiterDto.length; i++) {
		 * 
		 * if (aAbteilungsleiterDto[i] .getKostenstelleIIdAbteilung() != null) {
		 * 
		 * if (aAbteilungsleiterDto[i] .getKostenstelleIIdAbteilung()
		 * .equals(personalDto_Selektiert .getKostenstelleIIdAbteilung())) { cEmpfaenger
		 * = aAbteilungsleiterDto[i] .getCEmail(); break; }
		 * 
		 * }
		 * 
		 * }
		 * 
		 * }
		 * 
		 * // SP2782 Wenn kein Abteilungsleiter gefunden, dann // an GF if (cEmpfaenger
		 * == null) { PersonalDto[] aGeschaeftsfDto = DelegateFactory .getInstance()
		 * .getPersonalDelegate() .personalFindByMandantCNrPersonalfunktionCNr(
		 * LPMain.getTheClient() .getMandant(),
		 * PersonalFac.PERSONALFUNKTION_GESCHAEFTSFUEHRER);
		 * 
		 * if (aGeschaeftsfDto != null && aGeschaeftsfDto.length > 0) {
		 * 
		 * for (int i = 0; i < aGeschaeftsfDto.length; i++) { if (aGeschaeftsfDto[i] !=
		 * null) { if (aGeschaeftsfDto[i].getCEmail() != null) { cEmpfaenger =
		 * aGeschaeftsfDto[i] .getCEmail(); } } }
		 * 
		 * } }
		 * 
		 * }
		 * 
		 * if (cEmpfaenger != null) { VersandauftragDto dto = new VersandauftragDto();
		 * dto.setCEmpfaenger(cEmpfaenger);
		 * 
		 * if (personalDto_Selektiert.getCEmail() != null) {
		 * dto.setCAbsenderadresse(personalDto_Selektiert .getCEmail()); } else {
		 * dto.setCAbsenderadresse(personalDto_Angemeldet .getCEmail()); }
		 * 
		 * String sBetreff =null; if (d.isUrlaubsantrag()) { sBetreff =
		 * "Neuer Urlaubsantrag von " + personalDto_Selektiert.getPartnerDto()
		 * .formatFixName1Name2(); }else if (d.isKrankantrag()) { sBetreff =
		 * "Neuer Krank-Antrag von " + personalDto_Selektiert.getPartnerDto()
		 * .formatFixName1Name2(); }else { sBetreff = "Neuer Zeitausgleich-Antrag von "
		 * + personalDto_Selektiert.getPartnerDto() .formatFixName1Name2(); }
		 * 
		 * 
		 * dto.setCBetreff(sBetreff);
		 * 
		 * String text = "Zeitraum: " + Helper.formatDatum(d.datumVon, LPMain
		 * .getTheClient().getLocUi()) + "-" + Helper.formatDatum(d.datumBis, LPMain
		 * .getTheClient().getLocUi());
		 * 
		 * dto.setCText(text);
		 * 
		 * // PJ20605 bei Krankantrag auch an Lohnverrechnung // schicken if
		 * (d.isKrankantrag()) {
		 * 
		 * PersonalDto[] aLohnverrechnungDto = DelegateFactory .getInstance()
		 * .getPersonalDelegate() .personalFindByMandantCNrPersonalfunktionCNr(
		 * LPMain.getTheClient() .getMandant(),
		 * PersonalFac.PERSONALFUNKTION_LOHNVERRECHNUNG);
		 * 
		 * if (aLohnverrechnungDto != null && aLohnverrechnungDto.length > 0) { String
		 * cEmpfaengerCC=""; for (int i = 0; i < aLohnverrechnungDto.length; i++) { if
		 * (aLohnverrechnungDto[i].getCEmail() != null) {
		 * cEmpfaengerCC+=aLohnverrechnungDto[i].getCEmail()+";"; } }
		 * 
		 * dto.setCCcempfaenger(cEmpfaengerCC);
		 * 
		 * } }
		 * 
		 * dto = DelegateFactory.getInstance() .getVersandDelegate()
		 * .updateVersandauftrag(dto, false);
		 * 
		 * } else { DialogFactory .showModalDialog(
		 * LPMain.getTextRespectUISPr("lp.error"),
		 * LPMain.getTextRespectUISPr("pers.urlaubsantrag.fehlender.empfaenger")); }
		 */
	}

	public void erstelleZeitdatenAusVorschlag(ZeiterfassungFavoritenDto zfDto) throws Throwable {

		panelBottomZeitdaten.eventActionNew(null, true, false);
		panelBottomZeitdaten.eventYouAreSelected(false);
		panelBottomZeitdaten.getWdfDatum().setEnabled(false);
		panelBottomZeitdaten.erstelleVorschlagFuerZeitbuchung(zfDto);
		this.setSelectedComponent(panelSplitZeitdaten);

	}

	void dialogQueryFavoritenliste(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		FilterKriterium[] kriterien = null;

		kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID, true,
				panelQueryPersonal.getSelectedId() + "", FilterKriterium.OPERATOR_EQUAL, false);

		kriterien[0] = krit1;

		panelQueryFLRFavoriten = new PanelQueryFLR(null, kriterien, QueryParameters.UC_ID_ZEITERFASSUNG_FAVORITEN,
				aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("pers.zeiterfassung.favoritenliste"));

		panelQueryFLRFavoriten.createAndSaveAndShowButton("/com/lp/client/res/document_time.png",
				LPMain.getTextRespectUISPr("pers.zeiterfassung.favoritenliste.vorbesetzten"),
				MY_OWN_NEW_FAVORIT_UEBERNEHMEN, null);

		if (!isBVonBisErfassung()) {

			panelQueryFLRFavoriten.createAndSaveAndShowButton("/com/lp/client/res/document_ok.png",
					LPMain.getTextRespectUISPr("pers.zeiterfassung.favoritenliste.uebernehmen"),
					MY_OWN_NEW_FAVORIT_SOFORT_ANLEGEN, KeyStroke.getKeyStroke('S', java.awt.event.InputEvent.CTRL_MASK),
					null);
		}

		new DialogQuery(panelQueryFLRFavoriten);

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
			createAuswahl();
			panelQueryPersonal.eventYouAreSelected(false);
			panelQueryPersonal.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ZEITDATEN) {
			createZeitdaten((Integer) panelQueryPersonal.getSelectedId(),
					new java.sql.Date(System.currentTimeMillis()));
			panelBottomZeitdaten.aktualisiereDaten();
			panelSplitZeitdaten.eventYouAreSelected(false);
			panelQueryZeitdaten.updateButtons();
		} else if (selectedIndex == IDX_PANEL_SONDERZEITEN) {
			createSonderzeiten((Integer) panelQueryPersonal.getSelectedId());
			panelSplitSonderzeiten.eventYouAreSelected(false);
			panelQuerySonderzeiten.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ZEITGUTSCHRIFT) {
			createZeitgutschrift((Integer) panelQueryPersonal.getSelectedId());
			panelSplitZeitgutschrift.eventYouAreSelected(false);
			panelQueryZeitgutschrift.updateButtons();
		} else if (selectedIndex == IDX_PANEL_BEREITSCHAFT) {
			createBereitschaft((Integer) panelQueryPersonal.getSelectedId());
			panelSplitBereitschaft.eventYouAreSelected(false);
			panelQueryBereitschaft.updateButtons();
		} else if (selectedIndex == IDX_PANEL_REISEZEITEN) {
			createReisezeiten((Integer) panelQueryPersonal.getSelectedId());
			panelSplitReisezeiten.eventYouAreSelected(false);
			panelQueryReisezeiten.updateButtons();
		} else if (selectedIndex == IDX_PANEL_REISESPESEN) {
			createReisezeiten((Integer) panelQueryPersonal.getSelectedId());
			panelQueryReisezeiten.eventYouAreSelected(false);
			panelBottomReisezeiten.setKeyWhenDetailPanel(panelQueryReisezeiten.getSelectedId());
			panelBottomReisezeiten.eventYouAreSelected(false);

			if (panelQueryReisezeiten.getSelectedId() != null) {

				ReiseDto reiseDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.reiseFindByPrimaryKey((Integer) panelQueryReisezeiten.getSelectedId());

				if (Helper.short2boolean(reiseDto.getBBeginn())) {
					createReisespesen((Integer) panelQueryReisezeiten.getSelectedId());
					panelSplitReisespesen.eventYouAreSelected(false);
					panelQueryReisespesen.updateButtons();
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
							LPMain.getTextRespectUISPr("personal.zusspesen.nuraufbeginnmoeglich"));

					setSelectedIndex(IDX_PANEL_REISEZEITEN);
					lPEventObjectChanged(e);
				}

			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
						LPMain.getTextRespectUISPr("personal.keinereisezeitausgewaehlt"));

				setSelectedIndex(IDX_PANEL_REISEZEITEN);
				lPEventObjectChanged(e);
			}
		} else if (selectedIndex == IDX_PANEL_TELEFONZEITEN) {
			createTelefonzeiten((Integer) panelQueryPersonal.getSelectedId());
			panelSplitTelefonzeiten.eventYouAreSelected(false);
			panelQueryTelefonzeiten.updateButtons();
		} else if (selectedIndex == IDX_PANEL_PROJEKTQUEUE) {
			createProjektQueue((Integer) panelQueryPersonal.getSelectedId());
			panelQueryQueue.eventYouAreSelected(false);
			panelQueryQueue.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ZEITABSCHLUSS) {
			createZeitabschluss((Integer) panelQueryPersonal.getSelectedId());
			panelQueryZeitabschluss.eventYouAreSelected(false);
			panelQueryZeitabschluss.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ZEITDATENPRUEFEN) {
			createZeitdatenpruefen((Integer) panelQueryPersonal.getSelectedId());
			panelSplitZeitdatenpruefen.eventYouAreSelected(false);
			panelQueryZeitdatenpruefen.updateButtons();
		}

		refreshTitle();
	}

	protected JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar menuBarZeiterfassung = new WrapperMenuBar(this);

		JMenu jmModul = (JMenu) menuBarZeiterfassung.getComponent(WrapperMenuBar.MENU_MODUL);

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_LOHNDATENEXPORT)) {
			JMenuItem menuItemExport = new JMenuItem(LPMain.getTextRespectUISPr("pers.zeitdaten.export"));
			menuItemExport.addActionListener(this);
			menuItemExport.setActionCommand(MENUE_ACTION_LOHNDATENEXPORT);

			jmModul.add(new JSeparator(), 0);

			jmModul.add(menuItemExport, 0);
		}

		if (getInternalFrame().getRechtModulweit().equals(RechteFac.RECHT_MODULWEIT_UPDATE)) {

			JMenuItem menuItemSonderzeitenImport = new JMenuItem(LPMain.getTextRespectUISPr("pers.sonderzeitimport"));
			menuItemSonderzeitenImport.addActionListener(this);
			menuItemSonderzeitenImport.setActionCommand(MENUE_ACTION_SONDERZEITEN_IMPORT);
			jmModul.add(menuItemSonderzeitenImport, 0);
		}

		JMenu menuPflege = new WrapperMenu("lp.pflege", this);
		JMenu menuInfo = new WrapperMenu("lp.info", this);
		JMenuItem menuItemZeitdaten = new JMenuItem(
				LPMain.getTextRespectUISPr("zeiterfassung.report.zeitdatenjournal"));
		menuItemZeitdaten.addActionListener(this);

		menuItemZeitdaten.setActionCommand(MENUE_ACTION_ZEITDATEN);

		JMenuItem menuItemSonderzeitenliste = new JMenuItem(
				LPMain.getTextRespectUISPr("zeiterfassung.report.sonderzeitenliste"));

		menuItemSonderzeitenliste.addActionListener(this);

		menuItemSonderzeitenliste.setActionCommand(MENUE_ACTION_SONDERZEITENLISTE);

		JMenuItem menuItemProduktivitaetsstatistik = new JMenuItem(
				LPMain.getTextRespectUISPr("zeiterfassung.report.produktivitaetsstatistik"));

		menuItemProduktivitaetsstatistik.addActionListener(this);

		menuItemProduktivitaetsstatistik.setActionCommand(MENUE_ACTION_PRODUKTIVITAETSSTATISTIK);

		WrapperMenuItem menuItemAuftragzeitstatistik = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("personal.report.auftragszeitstatistik"),
				RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);

		menuItemAuftragzeitstatistik.addActionListener(this);

		menuItemAuftragzeitstatistik.setActionCommand(MENUE_ACTION_AUFTRAGSZEITSTATISTIK);

		WrapperMenuItem menuItemArbeitszeitstatistik = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("zeiterfassung.report.arbeitszeitstatistik"),
				RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1);

		menuItemArbeitszeitstatistik.addActionListener(this);

		menuItemArbeitszeitstatistik.setActionCommand(MENUE_ACTION_ARBEITSZEITSTATISTIK);

		boolean bDarfLagerprueffunktionensehen = false;

		try {
			bDarfLagerprueffunktionensehen = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_WW_DARF_LAGERPRUEFFUNKTIONEN_SEHEN);
		} catch (Throwable ex) {
			handleException(ex, true);
		}

		if (bDarfLagerprueffunktionensehen) {

			JMenuItem menuItemAutomatikbuchungen = new JMenuItem(
					LPMain.getTextRespectUISPr("zeiterfassung.automatikbuchungen"));

			menuItemAutomatikbuchungen.addActionListener(this);

			menuItemAutomatikbuchungen.setActionCommand(MENU_ACTION_PFLEGE_AUTOMATIKBUCHUNGEN);
			menuPflege.add(menuItemAutomatikbuchungen);

			JMenuItem menuItemSchichtzeitmodelle = new JMenuItem(
					LPMain.getTextRespectUISPr("zeiterfassung.schichtzeitmodelle"));

			menuItemSchichtzeitmodelle.addActionListener(this);

			menuItemSchichtzeitmodelle.setActionCommand(MENU_ACTION_PFLEGE_SCHICHTZEITMODELLE);
			menuPflege.add(menuItemSchichtzeitmodelle);

			JMenuItem menuItemNegativeStundenInUrlaubWandeln = new JMenuItem(
					LPMain.getTextRespectUISPr("pers.zeiterfassung.negativestunden.inurlaubwandeln"));

			menuItemNegativeStundenInUrlaubWandeln.addActionListener(this);

			menuItemNegativeStundenInUrlaubWandeln
					.setActionCommand(MENU_ACTION_PFLEGE_NEGATIVE_STUNDEN_IN_URLAUB_WANDELN);
			menuPflege.add(menuItemNegativeStundenInUrlaubWandeln);

		}

		if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_FERT_LOS_CUD)) {

			JMenuItem menuItemLoszeitenVerschieben = new JMenuItem(
					LPMain.getTextRespectUISPr("fert.pflege.loszeitenverschieben"));

			menuItemLoszeitenVerschieben.addActionListener(this);

			menuItemLoszeitenVerschieben.setActionCommand(MENU_ACTION_PFLEGE_LOSZEITEN_VERSCHIEBEN);
			menuPflege.add(menuItemLoszeitenVerschieben);
		}

		JMenuItem menuItemMonatsabrechnung = new JMenuItem(
				LPMain.getTextRespectUISPr("zeiterfassung.report.monatsabrechnung"));

		JMenuItem menuItemAnwesenheitsListe = new JMenuItem(
				LPMain.getTextRespectUISPr("zeiterfassung.report.anwesenheitsliste"));
		menuItemAnwesenheitsListe.addActionListener(this);
		menuItemAnwesenheitsListe.setActionCommand(MENUE_ACTION_ANWESENHEITSLISTE);

		menuInfo.add(menuItemZeitdaten);

		JMenuItem menuItemWochenjournal = new JMenuItem(
				LPMain.getTextRespectUISPr("zeiterfassung.report.wochenjournal"));
		menuItemWochenjournal.addActionListener(this);
		menuItemWochenjournal.setActionCommand(MENUE_ACTION_WOCHENJOURNAL);
		menuInfo.add(menuItemWochenjournal);

		menuInfo.add(menuItemSonderzeitenliste);

		menuItemMonatsabrechnung.addActionListener(this);
		menuItemMonatsabrechnung.setActionCommand(MENUE_ACTION_MONATSABRECHNUNG);
		menuInfo.add(menuItemMonatsabrechnung);

		JMenuItem menuItemWochenabrechnung = new JMenuItem(
				LPMain.getTextRespectUISPr("zeiterfassung.report.wochenabrechnung"));
		menuItemWochenabrechnung.addActionListener(this);
		menuItemWochenabrechnung.setActionCommand(MENUE_ACTION_WOCHENABRECHNUNG);
		menuInfo.add(menuItemWochenabrechnung);

		menuInfo.add(menuItemProduktivitaetsstatistik);

		JMenuItem menuItemProduktivitaetstagesstatistik = new JMenuItem(
				LPMain.getTextRespectUISPr("zeiterfassung.report.produktivitaetstagesstatistik"));

		menuItemProduktivitaetstagesstatistik.addActionListener(this);

		menuItemProduktivitaetstagesstatistik.setActionCommand(MENUE_ACTION_PRODUKTIVITAETSTAGESSTATISTIK);
		menuInfo.add(menuItemProduktivitaetstagesstatistik);
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZEITEN_ABSCHLIESSEN)) {
			JMenuItem menuItemWochenabschluss = new JMenuItem(
					LPMain.getTextRespectUISPr("pers.report.wochenabschluss"));

			menuItemWochenabschluss.addActionListener(this);

			menuItemWochenabschluss.setActionCommand(MENUE_ACTION_WOCHENABSCHLUSS);
			menuInfo.add(menuItemWochenabschluss);
		}
		menuInfo.addSeparator();

		JMenuItem menuItemMitarbeiteruebersicht = new JMenuItem(
				LPMain.getTextRespectUISPr("zeiterfassung.report.mitarbeiteruebersicht"));
		menuItemMitarbeiteruebersicht.addActionListener(this);
		menuItemMitarbeiteruebersicht.setActionCommand(MENUE_ACTION_MITARBEITERUEBERSICHT);
		menuInfo.add(menuItemMitarbeiteruebersicht);

		JMenuItem menuItemZeitsaldo = new JMenuItem(LPMain.getTextRespectUISPr("zeiterfassung.report.zeitsaldo"));
		menuItemZeitsaldo.addActionListener(this);
		menuItemZeitsaldo.setActionCommand(MENUE_ACTION_ZEITSALDO);
		menuInfo.add(menuItemZeitsaldo);

		JMenuItem menuItemMitarbeitereinteilung = new JMenuItem(
				LPMain.getTextRespectUISPr("pers.zeiterfassung.report.mitarbeitereinteilung"));
		menuItemMitarbeitereinteilung.addActionListener(this);
		menuItemMitarbeitereinteilung.setActionCommand(MENUE_ACTION_MITARBEITEREINTEILUNG);
		menuInfo.add(menuItemMitarbeitereinteilung);

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MASCHINENZEITERFASSUNG)) {
			JMenuItem menuItemMaschinenerfolg = new JMenuItem(
					LPMain.getTextRespectUISPr("pers.zeiterfassung.report.maschinenerfolg"));
			menuItemMaschinenerfolg.addActionListener(this);
			menuItemMaschinenerfolg.setActionCommand(MENUE_ACTION_MASCHINENERFOLG);
			menuInfo.add(menuItemMaschinenerfolg);
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_REISEZEITEN)) {
			menuInfo.addSeparator();
			JMenuItem menuItemReisezeiten = new JMenuItem(LPMain.getTextRespectUISPr("pers.reisezeiten"));
			menuItemReisezeiten.addActionListener(this);
			menuItemReisezeiten.setActionCommand(MENUE_ACTION_REISEZEITEN);
			menuInfo.add(menuItemReisezeiten);

			JMenuItem menuItemFahrtenbuch = new JMenuItem(LPMain.getTextRespectUISPr("pers.reisezeiten.fahrtenbuch"));
			menuItemFahrtenbuch.addActionListener(this);
			menuItemFahrtenbuch.setActionCommand(MENUE_ACTION_FAHRTENBUCH);
			menuInfo.add(menuItemFahrtenbuch);
		}
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_TELEFONZEITERFASSUNG)) {
			menuInfo.addSeparator();
			JMenuItem menuItemTelefonzeiten = new JMenuItem(LPMain.getTextRespectUISPr("pers.telefonzeiten"));
			menuItemTelefonzeiten.addActionListener(this);
			menuItemTelefonzeiten.setActionCommand(MENUE_ACTION_TELEFONZEITERFASSUNG);
			menuInfo.add(menuItemTelefonzeiten);
		}

		JMenuItem menuItemAenderungen = new JMenuItem(LPMain.getTextRespectUISPr("lp.report.aenderungen"));
		menuItemAenderungen.addActionListener(this);
		menuItemAenderungen.setActionCommand(MENU_INFO_AENDERUNGEN);
		menuInfo.add(menuItemAenderungen);

		menuBarZeiterfassung.addJMenuItem(menuPflege);

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_PERS_ZEITERFASSUNG_REPORTS_SEHEN)) {
			menuBarZeiterfassung.addJMenuItem(menuInfo);
		} else {
			menuBarZeiterfassung.add(menuItemZeitdaten);
		}

		if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_PERS_SICHTBARKEIT_ALLE)
				|| DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_PERS_SICHTBARKEIT_ABTEILUNG)) {

			JMenu modulJournal = (JMenu) menuBarZeiterfassung.getComponent(WrapperMenuBar.MENU_JOURNAL);
			modulJournal.add(menuItemAnwesenheitsListe);

			if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_PERS_SICHTBARKEIT_ALLE)) {

				modulJournal.add(menuItemArbeitszeitstatistik);
				modulJournal.add(menuItemAuftragzeitstatistik);

				if (LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZEITEN_ABSCHLIESSEN)) {

					JMenuItem menuItemAbgenschlosseneZeiten = new JMenuItem(
							LPMain.getTextRespectUISPr("zeiterfassung.report.abgeschlossenezeitbuchungen"));
					menuItemAbgenschlosseneZeiten.addActionListener(this);
					menuItemAbgenschlosseneZeiten.setActionCommand(MENUE_ACTION_ABGESCHLOSSENE_ZEITEN);
					modulJournal.add(menuItemAbgenschlosseneZeiten);
				}

				if (LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_HVMA2)) {

					HvmalizenzDto lizenzDto = DelegateFactory.getInstance().getHvmaDelegate()
							.hvmalizenzFindByEnum(HvmaLizenzEnum.Offline);
					if (lizenzDto != null && lizenzDto.getIMaxUser() > 0) {

						WrapperMenuItem menuItemLetzteSynchronisation = new WrapperMenuItem(
								LPMain.getTextRespectUISPr("personal.report.letztesynchronisation"), null);

						menuItemLetzteSynchronisation.addActionListener(this);

						menuItemLetzteSynchronisation.setActionCommand(MENUE_ACTION_LETZTE_SYNCHRONISATION);
						modulJournal.add(menuItemLetzteSynchronisation);
					}
				}
			}

		}

		return menuBarZeiterfassung;
	}

	public void refreshQPZeitdaten(Integer iIdPersonal, Date dateI, boolean isEditModeI) throws Throwable {

		if (isEditModeI) {
			panelQueryZeitdaten.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
		} else {
			createZeitdaten(iIdPersonal, dateI);
			panelQueryZeitdaten.eventYouAreSelected(false);
			panelQueryZeitdaten.updateButtons(new LockStateValue(PanelBasis.LOCK_IS_NOT_LOCKED));
		}

	}

	public void telefonzeitStarten(Integer partnerIId, Integer ansprechpartnerIId, Integer projektIId,
			String nummerGewaehlt) throws Throwable {
		createTelefonzeiten(LPMain.getTheClient().getIDPersonal());
		setSelectedComponent(panelSplitTelefonzeiten);
		panelQueryTelefonzeiten.eventActionNew(null, true, false);

		panelBottomTelefonzeiten.setzeAusWrapperTelefonField(partnerIId, ansprechpartnerIId, projektIId,
				nummerGewaehlt);

	}

}
