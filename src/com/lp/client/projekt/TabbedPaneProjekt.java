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
package com.lp.client.projekt;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.Map;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.event.ChangeEvent;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.DialogEigenschaftenImportXLS;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.HvDropTarget;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedEventDrop;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.dynamisch.PanelDynamisch;
import com.lp.client.media.DropPanelSplit;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.media.service.MediaEmailMetaDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.projekt.service.HistoryDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjekttaetigkeitDto;
import com.lp.server.projekt.service.ProjekttechnikerDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse kuemmert sich ...</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 * 
 * <p>@author $Author: Gerold $</p>
 * 
 * @version not attributable Date $Date: 2012/07/05 09:25:28 $
 */
public class TabbedPaneProjekt extends TabbedPane implements IProjektDtoService {

	private static final long serialVersionUID = 1L;
	public static int IDX_PANEL_PROJEKTAUSWAHL = -1;
	public static int IDX_PANEL_PROJEKTKOPFDATEN = -1;
	public int IDX_PANEL_COCKPIT = -1;
	public int IDX_PANEL_PROJEKTHISTORY = -1;
	public int IDX_PANEL_PROJEKTQUEUE = -1;
	public int IDX_PANEL_ZEITDATEN = -1;
	public int IDX_PANEL_TELEFONZEITEN = -1;
	public int IDX_PANEL_PROJEKTVERLAUF = -1;
	public int IDX_PANEL_TECHNIKER = -1;
	public int IDX_PANEL_TAETIGKEITEN = -1;
	public int IDX_PANEL_PROJEKTEIGENSCHAFTEN = -1;
	public int IDX_PANEL_GRUPPEN = -1;
	public int IDX_PANEL_ZEITBUCHUNGEN = -1;
	public int IDX_PANEL_HISTORYEIGENSCHAFTEN = -1;

	private PanelSplit panelSplitHistory = null;
	private PanelProjektHistory panelHistory = null;
	private PanelQuery panelQueryHistory = null;

	// private PanelSplit panelSplitQueue = null;
	// private PanelProjektQueue panelQueue = null;
	private PanelQuery panelQueryQueue = null;
	private PanelQuery panelQueryTelefonzeiten = null;

	private PanelQuery projektAuswahl = null;
	private PanelProjektKopfdaten projektKopfdaten = null;
	private PanelCockpit projektCockpit = null;

	private PanelSplit panelSplitTechniker = null;
	private PanelQuery panelQueryTechniker = null;
	private PanelProjekttechniker panelDetailTechniker = null;

	private PanelSplit panelSplitGruppen = null;
	private PanelQuery panelQueryGruppen = null;
	private PanelProjektgruppe panelDetailGruppen = null;

	private PanelSplit panelSplitTaetigkeiten = null;
	private PanelQuery panelQueryTaetigkeiten = null;
	private PanelProjekttaetigkeiten panelDetailTaetigkeiten = null;

	private PanelSplit panelSplitZeitbuchungen = null;
	private PanelQuery panelQueryZeitbuchungen = null;
	private PanelBasis panelDetailZeitbuchungen = null;

	private ProjektDto projektDto = null;
	private PartnerDto partnerDto = null;
	private AnsprechpartnerDto ansprechpartnerDto = null;
	private PersonalDto personalZugewiesenerDto = null;
	private PersonalDto personalErzeugerDto = null;

	private PanelQueryFLR panelQueryFLRTechniker = null;
	private PanelQueryFLR panelQueryFLRArtikel = null;

	public final static String MENU_ANSICHT_PROJEKT_ALLE = "MENU_ANSICHT_PROJEKT_ALLE";
	private final static String MENU_ANSICHT_PROJEKT_ALLE_OFFENE = "MENU_ANSICHT_PROJEKT_ALLE_OFFENE";
	private final static String MENU_ANSICHT_PROJEKT_MEINE = "MENU_ANSICHT_PROJEKT_MEINE";
	private final static String MENU_ANSICHT_PROJEKT_MEINE_OFFENE = "MENU_ANSICHT_PROJEKT_MEINE_OFFENE";
	private final static String MENU_ANSICHT_PROJEKT_ALLE_OHNE_STORNIERT = "MENU_ANSICHT_PROJEKT_ALLE_OHNE_STORNIERT";

	private final static String MENU_JOURNAL_PROJEKT_ALLE = "MENU_JOURNAL_PROJEKT_ALLE";
	private final static String MENU_JOURNAL_PROJEKT_ALLE_OFFENE = "MENU_JOURNAL_PROJEKT_ALLE_OFFENE";
	private final static String MENU_JOURNAL_PROJEKT_ERLEDIGT = "MENU_JOURNAL_PROJEKT_ERLEDIGT";
	private final static String MENU_JOURNAL_PROJEKT_AUSWAHLLISTE = "MENU_JOURNAL_PROJEKT_AUSWAHLLISTE";
	private final static String MENU_JOURNAL_PROJEKT_ALLE_DETAILLIERT = "MENU_JOURNAL_PROJEKT_ALLE_DETAILLIERT";

	private final static String MENU_JOURNAL_ATIVITAETSUEBERISCHT = "MENU_JOURNAL_ATIVITAETSUEBERISCHT";
	private final static String MENU_JOURNAL_PROJEKTFORECAST = "MENU_JOURNAL_PROJEKTFORECAST";

	private final static String MENU_INFO_STATISTIK = "MENU_INFO_STATISTIK";
	private final static String MENU_AENDERUNGEN_EIGENSCHAFTEN = "MENU_AENDERUNGEN_EIGENSCHAFTEN";

	public final static String MENU_ACTION_DATEI_PROJEKT = "MENU_ACTION_DATEI_PROJEKT";

	public final static String IN_QUEUE_AUFNEHMEN = "in_queue_aufnehmen";
	public final static String MY_OWN_NEW_IN_QUEUE_AUFNEHMEN = PanelBasis.ACTION_MY_OWN_NEW + IN_QUEUE_AUFNEHMEN;

	public final static String MY_OWN_NEW_MEHRERE_TECHNIKER_HINZUFUEGEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "_mehrere_techniker_hinzufuegen";
	public final static String MY_OWN_NEW_MEHRERE_TAETIGKEITEN_HINZUFUEGEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "_mehrere_taetigkeiten_hinzufuegen";

	public final static String AUS_QUEUE_ENTFERNEN = "aus_queue_entfernen";
	public final static String MY_OWN_NEW_AUS_QUEUE_ENTFERNEN = PanelBasis.ACTION_MY_OWN_NEW + AUS_QUEUE_ENTFERNEN;
	private static final String ACTION_SPECIAL_NEW_EMAIL = PanelBasis.ACTION_MY_OWN_NEW + "new_email_entry";
	// private static final String ACTION_SPECIAL_NEW_EMAIL = "action_special_"
	// + PanelBasis.ALWAYSENABLED + "new_email_entry";

	private static final String ACTION_SPECIAL_GOTO_TELEFONZEITEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_GOTO_TELEFONZEITEN";
	WrapperGotoButton wbuGotoTelefonzeiten = new WrapperGotoButton(
			com.lp.util.GotoHelper.GOTO_ZEITERFASSUNG_TELEFONZEITEN);

	private final String MENUE_PFLEGE_PROJEKTEIGENSCHAFTEN_XLS_IMPORT = "MENUE_PFLEGE_PROJEKTEIGENSCHAFTEN_XLS_IMPORT";
	private final String MENUE_PFLEGE_HISTORYEIGENSCHAFTEN_XLS_IMPORT = "MENUE_PFLEGE_HISTORYEIGENSCHAFTEN_XLS_IMPORT";

	private PanelDialogKriterienProjektzeiten panelDialogKriterienProjektzeiten = null;
	private boolean pdKriterienLoszeitenUeberMenueAufgerufen = false;
	private PanelTabelleProjektzeiten panelTabelleProjektzeiten = null;

	public PanelProjektverlauf projektverlauf = null;

	private WrapperMenuBar wrapperMenuBar = null;
	private JCheckBoxMenuItem menuItemAlle = null;
	private JCheckBoxMenuItem menuItemAlleOffene = null;
	private JCheckBoxMenuItem menuItemMeine = null;
	private JCheckBoxMenuItem menuItemMeineOffenen = null;
	private JCheckBoxMenuItem menuItemAlleOhneStorniert = null;
	private JMenu menuAnsicht = null;

	private PanelBasis panelDetailProjekteigenschaft = null;
	private PanelBasis panelDetailHistoryeigenschaft = null;

	public String sRechtModulweit = null;

	boolean bProjektMitUmsatz = false;

	public TabbedPaneProjekt(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("proj.projekt"));
		sRechtModulweit = getInternalFrame().getRechtModulweit();

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_PROJEKT,
				ParameterFac.PARAMETER_PROJEKT_MIT_UMSATZ);
		bProjektMitUmsatz = (Boolean) parameter.getCWertAsObject();

		jbInit();
		initComponents();

	}

	private void jbInit() throws Throwable {
		// dafuer sorgen, dass die Dtos != null sind
		resetDtos();
		// die Liste der Projekt aufbauen
		refreshProjektAuswahl();

		IDX_PANEL_PROJEKTAUSWAHL =

				reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.auswahl"), null, projektAuswahl,
						LPMain.getTextRespectUISPr("lp.auswahl"));

		IDX_PANEL_PROJEKTKOPFDATEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.kopfdaten"), null,
				projektKopfdaten, LPMain.getTextRespectUISPr("lp.kopfdaten"));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {

			IDX_PANEL_COCKPIT = reiterHinzufuegen(LPMain.getTextRespectUISPr("proj.cockpit"), null, null,
					LPMain.getTextRespectUISPr("proj.cockpit"));
		}

		IDX_PANEL_PROJEKTHISTORY = reiterHinzufuegen(LPMain.getTextRespectUISPr("proj.projekt.details"), null, null,
				LPMain.getTextRespectUISPr("proj.projekt.details"));

		// Wenn keine Panelbeschreibung vorhanden, dann ausblenden
		PanelbeschreibungDto[] dtosHistory = DelegateFactory.getInstance().getPanelDelegate()
				.panelbeschreibungFindByPanelCNrMandantCNr(PanelFac.PANEL_PROJEKTHISTORY, null);
		if (dtosHistory != null && dtosHistory.length > 0) {

			IDX_PANEL_HISTORYEIGENSCHAFTEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("proj.history.eigenschaften"),
					null, null, LPMain.getTextRespectUISPr("proj.history.eigenschaften"));
		}

		IDX_PANEL_PROJEKTQUEUE = reiterHinzufuegen(LPMain.getTextRespectUISPr("proj.projekt.queue"), null, null,
				LPMain.getTextRespectUISPr("proj.projekt.queue"));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)
				&& LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_TELEFONZEITERFASSUNG)) {

			IDX_PANEL_TELEFONZEITEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("pers.telefonzeiten"), null, null,
					LPMain.getTextRespectUISPr("pers.telefonzeiten"));
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTZEITERFASSUNG)) {

			IDX_PANEL_ZEITDATEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("fert.tab.oben.istzeitdaten.title"),
					null, null, LPMain.getTextRespectUISPr("fert.tab.oben.istzeitdaten.tooltip"));

			IDX_PANEL_ZEITBUCHUNGEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("projekt.zeitbuchungen"), null, null,
					LPMain.getTextRespectUISPr("projekt.zeitbuchungen"));
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {

			IDX_PANEL_PROJEKTVERLAUF = reiterHinzufuegen(LPMain.getTextRespectUISPr("proj.projektverlauf"), null, null,
					LPMain.getTextRespectUISPr("proj.projektverlauf"));
		}

		IDX_PANEL_TECHNIKER = reiterHinzufuegen(LPMain.getTextRespectUISPr("proj.projekttechniker"), null, null,
				LPMain.getTextRespectUISPr("proj.projekttechniker"));

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_PROJEKT,
				ParameterFac.PARAMETER_PROJEKT_MIT_TAETIGKEIT);
		boolean bProjektMitTaetigkeiten = ((Boolean) parameter.getCWertAsObject());
		if (bProjektMitTaetigkeiten == true) {

			IDX_PANEL_TAETIGKEITEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("proj.projekttaetigkeiten"), null,
					null, LPMain.getTextRespectUISPr("proj.projekttaetigkeiten"));
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_UNTERPROJEKTE)) {
			// PJ18536 Gruppe

			IDX_PANEL_GRUPPEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("proj.verknuepfung"), null, null,
					LPMain.getTextRespectUISPr("proj.verknuepfung"));
		}

		// Wenn keine Panelbeschreibung vorhanden, dann ausblenden
		PanelbeschreibungDto[] dtos = DelegateFactory.getInstance().getPanelDelegate()
				.panelbeschreibungFindByPanelCNrMandantCNr(PanelFac.PANEL_PROJEKTEIGENSCHAFTEN, null);
		if (dtos != null && dtos.length > 0) {

			IDX_PANEL_PROJEKTEIGENSCHAFTEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.eigenschaften"), null,
					null, LPMain.getTextRespectUISPr("lp.eigenschaften"));
		}

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getTextRespectUISPr("proj.projekt"));

	}

	public Integer getBereichIId() {
		return (Integer) projektAuswahl.getKeyOfFilterComboBox();
	}

	public PanelQuery getPanelQueryProjekttechniker(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryTechniker == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUsePositionen = { PanelBasis.ACTION_NEW };
			panelQueryTechniker = new PanelQuery(null, null, QueryParameters.UC_ID_PROJEKTTECHNIKER,
					aWhichButtonIUsePositionen, getInternalFrame(), LPMain.getTextRespectUISPr("proj.projekttechniker"),
					true);

			panelQueryTechniker.createAndSaveAndShowButton("/com/lp/client/res/documents.png",
					LPMain.getTextRespectUISPr("proj.mehreretechniker"), MY_OWN_NEW_MEHRERE_TECHNIKER_HINZUFUEGEN,
					RechteFac.RECHT_PROJ_PROJEKT_CUD);

		}
		return panelQueryTechniker;
	}

	public void erstelleProjektAusAngebot(Integer angebotIId) throws Throwable {

		ItemChangedEvent e = new ItemChangedEvent(this, -99);
		getProjektAuswahl().eventActionNew(e, true, false);
		projektKopfdaten.eventYouAreSelected(false);

		projektKopfdaten.setDefaultsAusAngebot(angebotIId);
	}

	public void erstelleProjektAusAuftrag(Integer auftragIId) throws Throwable {

		ItemChangedEvent e = new ItemChangedEvent(this, -99);
		getProjektAuswahl().eventActionNew(e, true, false);
		projektKopfdaten.eventYouAreSelected(false);

		projektKopfdaten.setDefaultsAusAuftrag(auftragIId);
	}

	private PanelProjekttechniker getPanelDetailProjekttechniker(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailTechniker == null && bNeedInstantiationIfNull) {
			panelDetailTechniker = new PanelProjekttechniker(getInternalFrame(),
					LPMain.getTextRespectUISPr("proj.projekttechniker"), null, this);
		}
		return panelDetailTechniker;
	}

	private PanelSplit getPanelSplitProjekttechniker(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitTechniker == null && bNeedInstantiationIfNull) {
			panelSplitTechniker = new PanelSplit(getInternalFrame(), getPanelDetailProjekttechniker(true),
					getPanelQueryProjekttechniker(true), 200);
			setComponentAt(IDX_PANEL_TECHNIKER, panelSplitTechniker);
		}
		return panelSplitTechniker;
	}

	public PanelQuery getPanelQueryProjektgruppe(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryGruppen == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUsePositionen = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_PRINT };
			panelQueryGruppen = new PanelQuery(null, null, QueryParameters.UC_ID_PROJEKTGRUPPE,
					aWhichButtonIUsePositionen, getInternalFrame(), LPMain.getTextRespectUISPr("proj.verknuepfung"),
					true);

		}
		return panelQueryGruppen;
	}

	private PanelProjektgruppe getPanelDetailProjektgruppe(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelDetailGruppen == null && bNeedInstantiationIfNull) {
			panelDetailGruppen = new PanelProjektgruppe(getInternalFrame(),
					LPMain.getTextRespectUISPr("proj.verknuepfung"), null, this);
		}
		return panelDetailGruppen;
	}

	private PanelSplit getPanelSplitProjektgruppe(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitGruppen == null && bNeedInstantiationIfNull) {
			panelSplitGruppen = new PanelSplit(getInternalFrame(), getPanelDetailProjektgruppe(true),
					getPanelQueryProjektgruppe(true), 200);
			setComponentAt(IDX_PANEL_GRUPPEN, panelSplitGruppen);
		}
		return panelSplitGruppen;
	}

	public PanelQuery getPanelQueryProjekttaetigkeiten(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryTaetigkeiten == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUsePositionen = { PanelBasis.ACTION_NEW };
			panelQueryTaetigkeiten = new PanelQuery(null, null, QueryParameters.UC_ID_PROJEKTTAETIGKEIT,
					aWhichButtonIUsePositionen, getInternalFrame(),
					LPMain.getTextRespectUISPr("proj.projekttaetigkeiten"), true);

			panelQueryTaetigkeiten.createAndSaveAndShowButton("/com/lp/client/res/documents.png",
					LPMain.getTextRespectUISPr("proj.mehreretaetigkeiten"), MY_OWN_NEW_MEHRERE_TAETIGKEITEN_HINZUFUEGEN,
					RechteFac.RECHT_PROJ_PROJEKT_CUD);

		}
		return panelQueryTaetigkeiten;
	}

	private PanelProjekttaetigkeiten getPanelDetailProjekttaetigkeiten(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelDetailTaetigkeiten == null && bNeedInstantiationIfNull) {
			panelDetailTaetigkeiten = new PanelProjekttaetigkeiten(getInternalFrame(),
					LPMain.getTextRespectUISPr("proj.projekttaetigkeiten"), null, this);
		}
		return panelDetailTaetigkeiten;
	}

	private PanelSplit getPanelSplitProjekttaetigkeiten(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitTaetigkeiten == null && bNeedInstantiationIfNull) {
			panelSplitTaetigkeiten = new PanelSplit(getInternalFrame(), getPanelDetailProjekttaetigkeiten(true),
					getPanelQueryProjekttaetigkeiten(true), 200);
			setComponentAt(IDX_PANEL_TAETIGKEITEN, panelSplitTaetigkeiten);
		}
		return panelSplitTaetigkeiten;
	}

	public void refreshFilterTechniker() throws Throwable {
		if (getProjektDto() != null) {
			if (getPanelQueryProjekttechniker(false) != null) {
				FilterKriterium[] krit = ProjektFilterFactory.getInstance()
						.createFKProjekttechniker(getProjektDto().getIId());
				getPanelQueryProjekttechniker(true).setDefaultFilter(krit);
			}
		}
	}

	public void refreshFilterGruppen() throws Throwable {
		if (getProjektDto() != null) {
			if (getPanelQueryProjektgruppe(false) != null) {
				FilterKriterium[] krit = ProjektFilterFactory.getInstance()
						.createFKProjektgruppe(getProjektDto().getIId());
				getPanelQueryProjektgruppe(true).setDefaultFilter(krit);
			}
		}
	}

	public void refreshFilterTaetigkeiten() throws Throwable {
		if (getProjektDto() != null) {
			if (getPanelQueryProjekttaetigkeiten(false) != null) {
				FilterKriterium[] krit = ProjektFilterFactory.getInstance()
						.createFKProjekttaetigkeiten(getProjektDto().getIId());
				getPanelQueryProjekttaetigkeiten(true).setDefaultFilter(krit);
			}
		}
	}

	public PanelProjektverlauf getPanelProjektverlauf() throws Throwable {

		projektverlauf = new PanelProjektverlauf(QueryParameters.UC_ID_PROJEKTVERLAUF,
				LPMain.getTextRespectUISPr("proj.projektverlauf"), getInternalFrameProjekt());

		// default Kriterium vorbelegen
		FilterKriterium[] aFilterKrit = new FilterKriterium[1];

		FilterKriterium krit1 = new FilterKriterium("projekt_i_id", true, projektDto.getIId().toString(),
				FilterKriterium.OPERATOR_EQUAL, false);

		aFilterKrit[0] = krit1;

		projektverlauf.setDefaultFilter(aFilterKrit);

		setComponentAt(IDX_PANEL_PROJEKTVERLAUF, projektverlauf);

		return projektverlauf;
	}

	private void refreshProjektAuswahl() throws Throwable {

		if (projektAuswahl == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_PRINT, PanelBasis.ACTION_FILTER, };

			

			FilterKriterium[] filterAuswahl =  ProjektFilterFactory.getInstance().createFKProjektauswahl();

			ArbeitsplatzparameterDto paramDto = DelegateFactory.getInstance().getParameterDelegate()
					.holeArbeitsplatzparameter(ParameterFac.ARBEITSPLATZPARAMETER_PROJEKTAUSWAHL_SCHNELL);

			boolean bProjektauswahlSchnell=false;
			int UC_ID = QueryParameters.UC_ID_PROJEKT;
			if (paramDto != null && paramDto.asBoolean()) {
				UC_ID = QueryParameters.UC_ID_PROJEKT_SCHNELL;
				bProjektauswahlSchnell=true;
			}
			
			QueryType[] querytypes = ProjektFilterFactory.getInstance().createQTPanelProjektAuswahl(bProjektauswahlSchnell);
			

			projektAuswahl = new PanelQuery(querytypes, filterAuswahl, UC_ID, aWhichButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.auswahl"), true);
			HvDropTarget dragDrop = new HvDropTarget(projektAuswahl);
			dragDrop.addDropListener(new ProjektEmailDropListener(this, projektAuswahl));

			Map m = DelegateFactory.getInstance().getProjektServiceDelegate().getAllBereich();
			Object defaultId = null;
			if (m.size() > 0) {
				defaultId = m.keySet().iterator().next();
			}

			projektAuswahl.setFilterComboBox(m,
					new FilterKriterium("projekt." + ProjektFac.FLR_PROJEKT_BEREICH_I_ID, true, "" + "",
							FilterKriterium.OPERATOR_EQUAL, false),
					false, LPMain.getTextRespectUISPr("lp.alle"), false, defaultId);

			if (bProjektMitUmsatz) {
				projektAuswahl.setFilterComboBox2(
						DelegateFactory.getInstance().getProjektServiceDelegate().getAllLeadstatus(),
						new FilterKriterium("projekt.flrvkfortschritt.flrleadstatus.i_id", true, "" + "",
								FilterKriterium.OPERATOR_EQUAL, false),
						false, LPMain.getTextRespectUISPr("lp.alle"), false);
				projektAuswahl.getWcoFilter2().setToolTipText(LPMain.getTextRespectUISPr("proj.leadstatus"));
			}

			projektAuswahl.createAndSaveAndShowButton("/com/lp/client/res/navigate_right.png",
					LPMain.getTextRespectUISPr("proj.inqueueaufnehmen"), MY_OWN_NEW_IN_QUEUE_AUFNEHMEN,
					RechteFac.RECHT_PROJ_PROJEKT_CUD);

			FilterKriteriumDirekt fkDirekt1 = ProjektFilterFactory.getInstance().createFKDPartnerIId();

			FilterKriteriumDirekt fkDirekt2 = ProjektFilterFactory.getInstance().createFKDTitelVolltextsuche();

			projektAuswahl.befuellePanelFilterkriterienDirekt(
					ProjektFilterFactory.getInstance().createFKDProjektnummer(), fkDirekt1);
			projektAuswahl.addDirektFilter(fkDirekt2);
			projektAuswahl.addDirektFilter(ProjektFilterFactory.getInstance().createFKDTextSuchen());

			if (DelegateFactory.getInstance().getProjektServiceDelegate().esGibtMindestensEinenBereichMitBetreiber()) {
				projektAuswahl.addDirektFilter(ProjektFilterFactory.getInstance().createFKDBetreiber());
			}

			if (DelegateFactory.getInstance().getProjektServiceDelegate().esGibtMindestensEinenBereichMitArtikel()) {
				projektAuswahl.addDirektFilter(ProjektFilterFactory.getInstance().createFKDArtikel());
			}

			projektAuswahl.addStatusBar();

		}

	}

	private PanelBasis refreshProjektKopfdaten() throws Throwable {
		Integer iIdProjekt = null;
		if (projektKopfdaten == null) {
			if (getInternalFrame().getKeyWasForLockMe() != null) {
				iIdProjekt = new Integer(Integer.parseInt(getInternalFrame().getKeyWasForLockMe()));
			}
			projektKopfdaten = new PanelProjektKopfdaten(getInternalFrame(), LPMain.getTextRespectUISPr("lp.kopfdaten"),
					iIdProjekt); // empty
									// bei
									// leerer
									// Liste

			setComponentAt(IDX_PANEL_PROJEKTKOPFDATEN, projektKopfdaten);
		}
		return projektKopfdaten;
	}

	private void refreshEigenschaften(Integer key) throws Throwable {
		projektAuswahl.setSelectedId(key);
		// if (panelDetailProjekteigenschaft == null) {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD, };

		panelDetailProjekteigenschaft = new PanelDynamisch(getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.eigenschaften"), projektAuswahl, PanelFac.PANEL_PROJEKTEIGENSCHAFTEN,
				HelperClient.LOCKME_ARTIKEL, aWhichButtonIUse);
		setComponentAt(IDX_PANEL_PROJEKTEIGENSCHAFTEN, panelDetailProjekteigenschaft);
		// }
	}

	private void refreshHistoryEigenschaften() throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD, };
		panelDetailHistoryeigenschaft = new PanelDynamisch(getInternalFrame(),
				LPMain.getTextRespectUISPr("proj.history.eigenschaften"), panelQueryHistory,
				PanelFac.PANEL_PROJEKTHISTORY, HelperClient.LOCKME_PROJEKT, aWhichButtonIUse);
		setComponentAt(IDX_PANEL_HISTORYEIGENSCHAFTEN, panelDetailHistoryeigenschaft);

	}

	private PanelSplit refreshProjektHistory() throws Throwable {

		FilterKriterium[] filters = ProjektFilterFactory.getInstance().createFKProjekt(getProjektDto().getIId());

		if (panelSplitHistory == null) {
			boolean hasEmailFeature = LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_EMAIL_CLIENT);

			panelHistory = new PanelProjektHistory(getInternalFrame(), this,
					LPMain.getTextRespectUISPr("proj.projekt.history"), null); // eventuell
																				// gibt
																				// es
																				// noch
																				// keine
																				// position

			String[] aWhichButtonIUseHistory = { PanelBasis.ACTION_NEW };
			if (hasEmailFeature) {
				aWhichButtonIUseHistory = new String[] { PanelBasis.ACTION_NEW, ACTION_SPECIAL_NEW_EMAIL };
			}

			panelQueryHistory = new PanelQuery(null, filters, QueryParameters.UC_ID_HISTORY, aWhichButtonIUseHistory,
					getInternalFrame(), "", true);
			HvDropTarget dropTarget = new HvDropTarget(panelQueryHistory);
			dropTarget.addDropListener(new ProjektDetailEmailDropListener(panelQueryHistory, panelHistory));
			if (hasEmailFeature) {
				panelQueryHistory.createAndSaveAndShowButton("/com/lp/client/res/documentHtml.png",
						LPMain.getInstance().getTextRespectUISPr("lp.newHtml"), ACTION_SPECIAL_NEW_EMAIL,
						KeyStroke.getKeyStroke('N', InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK), null);
			}

			panelSplitHistory = new DropPanelSplit(getInternalFrame(), panelHistory, panelQueryHistory, 200);
			panelSplitHistory.beOneTouchExpandable();
			setComponentAt(IDX_PANEL_PROJEKTHISTORY, panelSplitHistory);
		}
		panelQueryHistory.setDefaultFilter(filters);
		panelQueryHistory.setMultipleRowSelectionEnabled(true);

		return panelSplitHistory;
	}

	private PanelSplit refreshProjektZeitbuchungen() throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[2];
		kriterien[0] = new FilterKriterium("zeitdaten.c_belegartnr", true, "'" + LocaleFac.BELEGART_PROJEKT + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[1] = new FilterKriterium("zeitdaten.i_belegartid", true, getProjektDto().getIId() + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		if (panelSplitZeitbuchungen == null) {

			panelDetailZeitbuchungen = new PanelProjektZeitbuchungen(getInternalFrame(), this,
					LPMain.getTextRespectUISPr("projekt.zeitbuchungen"), null);

			panelQueryZeitbuchungen = new PanelQuery(null, kriterien, QueryParameters.UC_ID_ZEITBUCHUNGEN, null,
					getInternalFrame(), "", true);

			panelSplitZeitbuchungen = new PanelSplit(getInternalFrame(), panelDetailZeitbuchungen,
					panelQueryZeitbuchungen, 200);
			panelSplitZeitbuchungen.beOneTouchExpandable();
			setComponentAt(IDX_PANEL_ZEITBUCHUNGEN, panelSplitZeitbuchungen);
		}
		panelQueryZeitbuchungen.setDefaultFilter(kriterien);
		return panelSplitZeitbuchungen;
	}

	private PanelQuery refreshProjektQueue() throws Throwable {

		FilterKriterium[] filters = ProjektFilterFactory.getInstance()
				.createFKProjektQueue(LPMain.getInstance().getTheClient().getIDPersonal());

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

	private PanelQuery refreshProjektTelefonzeiten() throws Throwable {

		FilterKriterium[] filters = ProjektFilterFactory.getInstance()
				.createFKProjektTelefonzeiten(projektDto.getIId());

		if (panelQueryTelefonzeiten == null) {

			panelQueryTelefonzeiten = new PanelQuery(null, filters, QueryParameters.UC_ID_PROJEKTTELEFONZEITEN, null,
					getInternalFrame(), "", true);
			panelQueryTelefonzeiten.createAndSaveAndShowButton("/com/lp/client/res/data_into.png",
					LPMain.getTextRespectUISPr("proj.telefonzeiten.goto"), ACTION_SPECIAL_GOTO_TELEFONZEITEN,
					KeyStroke.getKeyStroke('G', java.awt.event.InputEvent.CTRL_MASK), null);
			setComponentAt(IDX_PANEL_TELEFONZEITEN, panelQueryTelefonzeiten);
		}
		panelQueryTelefonzeiten.setDefaultFilter(filters);
		panelQueryTelefonzeiten.addStatusBar();

		return panelQueryTelefonzeiten;
	}

	private void deselektAllMenueBoxes() {
		menuItemAlle.setSelected(false);
		menuItemAlleOffene.setSelected(false);
		menuItemMeineOffenen.setSelected(false);
		menuItemMeine.setSelected(false);
		menuItemAlleOhneStorniert.setSelected(false);
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		wrapperMenuBar = new WrapperMenuBar(this);

		// Menue Datei; ein neuer Eintrag wird immer oben im Menue eingetragen
		JMenu jmModul = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_MODUL);

		JMenuItem menuItemDateiLieferschein = new JMenuItem(LPMain.getTextRespectUISPr("ls.menu.datei.lieferschein"));
		menuItemDateiLieferschein.addActionListener(this);
		menuItemDateiLieferschein.setActionCommand(MENU_ACTION_DATEI_PROJEKT);
		jmModul.add(menuItemDateiLieferschein, 0);

		// Menue Journal
		JMenu jmJournal = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_JOURNAL);

		JMenuItem menuItemJournalAlle = new JMenuItem(LPMain.getTextRespectUISPr("proj.alle") + "...");
		menuItemJournalAlle.addActionListener(this);
		menuItemJournalAlle.setActionCommand(MENU_JOURNAL_PROJEKT_ALLE);
		jmJournal.add(menuItemJournalAlle);

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {
			JMenuItem menuItemJournalAlleDetailliert = new JMenuItem(
					LPMain.getTextRespectUISPr("proj.alle.detailliert") + "...");
			menuItemJournalAlleDetailliert.addActionListener(this);
			menuItemJournalAlleDetailliert.setActionCommand(MENU_JOURNAL_PROJEKT_ALLE_DETAILLIERT);
			jmJournal.add(menuItemJournalAlleDetailliert);
		}

		JMenuItem menuItemJournalOffene = new JMenuItem(LPMain.getTextRespectUISPr("proj.alle.offene") + "...");
		menuItemJournalOffene.addActionListener(this);
		menuItemJournalOffene.setActionCommand(MENU_JOURNAL_PROJEKT_ALLE_OFFENE);
		jmJournal.add(menuItemJournalOffene);

		JMenuItem menuItemJournalErledigt = new JMenuItem(LPMain.getTextRespectUISPr("proj.journal.erledigt") + "...");
		menuItemJournalErledigt.addActionListener(this);
		menuItemJournalErledigt.setActionCommand(MENU_JOURNAL_PROJEKT_ERLEDIGT);
		jmJournal.add(menuItemJournalErledigt);

		JMenuItem menuItemJournalAuswahlliste = new JMenuItem(
				LPMain.getTextRespectUISPr("proj.journal.auswahlliste") + "...");
		menuItemJournalAuswahlliste.addActionListener(this);
		menuItemJournalAuswahlliste.setActionCommand(MENU_JOURNAL_PROJEKT_AUSWAHLLISTE);
		jmJournal.add(menuItemJournalAuswahlliste);

		JMenuItem menuItemJournalAktivitaetsuebersicht = new JMenuItem(
				LPMain.getTextRespectUISPr("proj.journal.aktivitaetsuebersicht"));
		menuItemJournalAktivitaetsuebersicht.addActionListener(this);
		menuItemJournalAktivitaetsuebersicht.setActionCommand(MENU_JOURNAL_ATIVITAETSUEBERISCHT);
		jmJournal.add(menuItemJournalAktivitaetsuebersicht);
		if (bProjektMitUmsatz == true) {
			JMenuItem menuItemJournalProjektforecast = new JMenuItem(
					LPMain.getTextRespectUISPr("proj.journal.projektforecast"));
			menuItemJournalProjektforecast.addActionListener(this);
			menuItemJournalProjektforecast.setActionCommand(MENU_JOURNAL_PROJEKTFORECAST);
			jmJournal.add(menuItemJournalProjektforecast);
		}
		JMenu menuInfo = new WrapperMenu("lp.info", this);
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {

			JMenuItem menuItemProjektstatistik = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("lp.statistik"));
			menuItemProjektstatistik.addActionListener(this);
			menuItemProjektstatistik.setActionCommand(MENU_INFO_STATISTIK);
			menuInfo.add(menuItemProjektstatistik);

		}

		// Wenn keine Panelbeschreibung vorhanden, dann ausblenden
		PanelbeschreibungDto[] dtos = DelegateFactory.getInstance().getPanelDelegate()
				.panelbeschreibungFindByPanelCNrMandantCNr(PanelFac.PANEL_PROJEKTEIGENSCHAFTEN, null);
		if (dtos != null && dtos.length > 0) {
			JMenuItem menuItemAenderungenEigenschaften = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("projekt.report.aenderungeneigenschaften"));
			menuItemAenderungenEigenschaften.addActionListener(this);
			menuItemAenderungenEigenschaften.setActionCommand(MENU_AENDERUNGEN_EIGENSCHAFTEN);
			menuInfo.add(menuItemAenderungenEigenschaften);
		}

		if (menuInfo.getPopupMenu() != null && menuInfo.getPopupMenu().getComponents() != null
				&& menuInfo.getPopupMenu().getComponents().length > 0) {
			wrapperMenuBar.addJMenuItem(menuInfo);
		}

		// zusaetzliches Menue Ansicht, das nur sichtbar ist, wenn man auf dem
		// Panel Auswahl steht
		if (menuAnsicht == null) {
			menuAnsicht = new WrapperMenu("lp.ansicht", this);
			menuItemAlle = new JCheckBoxMenuItem(LPMain.getTextRespectUISPr("proj.alle"));
			menuItemAlle.addActionListener(this);
			menuItemAlle.setActionCommand(MENU_ANSICHT_PROJEKT_ALLE);
			menuAnsicht.add(menuItemAlle);

			menuItemAlleOffene = new JCheckBoxMenuItem(LPMain.getTextRespectUISPr("proj.alle.offene"));
			menuItemAlleOffene.addActionListener(this);
			menuItemAlleOffene.setActionCommand(MENU_ANSICHT_PROJEKT_ALLE_OFFENE);
			menuAnsicht.add(menuItemAlleOffene);

			menuItemMeine = new JCheckBoxMenuItem(LPMain.getTextRespectUISPr("proj.meine"));
			menuItemMeine.addActionListener(this);
			menuItemMeine.setActionCommand(MENU_ANSICHT_PROJEKT_MEINE);
			menuAnsicht.add(menuItemMeine);

			menuItemMeineOffenen = new JCheckBoxMenuItem(LPMain.getTextRespectUISPr("proj.meine.offene"));
			menuItemMeineOffenen.addActionListener(this);
			menuItemMeineOffenen.setActionCommand(MENU_ANSICHT_PROJEKT_MEINE_OFFENE);
			menuAnsicht.add(menuItemMeineOffenen);

			menuItemAlleOhneStorniert = new JCheckBoxMenuItem(LPMain.getTextRespectUISPr("proj.alle.ohne.storniert"));
			menuItemAlleOhneStorniert.addActionListener(this);
			menuItemAlleOhneStorniert.setActionCommand(MENU_ANSICHT_PROJEKT_ALLE_OHNE_STORNIERT);
			menuAnsicht.add(menuItemAlleOhneStorniert);
		}
		wrapperMenuBar.addJMenuItem(menuAnsicht);
		JMenu menuPflege = new WrapperMenu("lp.pflege", this);

		if (DelegateFactory.getInstance().getPanelDelegate()
				.panelbeschreibungVorhanden(PanelFac.PANEL_PROJEKTEIGENSCHAFTEN)) {

			if (getInternalFrame().getRechtModulweit().equals(RechteFac.RECHT_MODULWEIT_UPDATE)) {

				JMenuItem menuItemImportEigenschaften = new JMenuItem(
						LPMain.getInstance().getTextRespectUISPr("projekt.pflege.import.projekteigenschaften"));
				menuItemImportEigenschaften.addActionListener(this);
				menuItemImportEigenschaften.setActionCommand(MENUE_PFLEGE_PROJEKTEIGENSCHAFTEN_XLS_IMPORT);
				menuPflege.add(menuItemImportEigenschaften);
			}

		}
		if (DelegateFactory.getInstance().getPanelDelegate()
				.panelbeschreibungVorhanden(PanelFac.PANEL_PROJEKTHISTORY)) {

			boolean hatRecht = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PROJ_DETAILS_CUD);
			if (hatRecht) {
				JMenuItem menuItemImportEigenschaften = new JMenuItem(LPMain.getInstance()
						.getTextRespectUISPr("projekt.pflege.import.projektdetailundeigenschaften"));
				menuItemImportEigenschaften.addActionListener(this);
				menuItemImportEigenschaften.setActionCommand(MENUE_PFLEGE_HISTORYEIGENSCHAFTEN_XLS_IMPORT);
				menuPflege.add(menuItemImportEigenschaften);
			}

		}

		if (menuPflege.getPopupMenu().getComponentCount() > 0) {

			wrapperMenuBar.addJMenuItem(menuPflege);
		}

		return wrapperMenuBar;
	}

	protected void refreshMenuAnsicht(boolean flag) {
		if (menuAnsicht != null) {
			MenuElement[] menuElement = menuAnsicht.getSubElements();
			JPopupMenu ansicht = (JPopupMenu) menuElement[0];
			java.awt.Component[] components = ansicht.getComponents();
			for (int i = 0; i < components.length; ++i) {
				if (components[i] instanceof JCheckBoxMenuItem) {
					((JCheckBoxMenuItem) components[i]).setEnabled(flag);
				}
			}
		}
	}

	public void lPActionEvent(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(MENU_ACTION_DATEI_PROJEKT)) {
			printProjekt();
		} else if (e.getActionCommand().equals(MENU_ANSICHT_PROJEKT_ALLE)) {
			FilterKriterium[] fk = ProjektFilterFactory.getInstance().createFKProjektauswahl();
			projektAuswahl.setDefaultFilter(fk);
			projektAuswahl.eventYouAreSelected(false);
			if (projektAuswahl.getSelectedId() != null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_PROJEKTAUSWAHL, true);
			} else {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_PROJEKTAUSWAHL, false);
			}
			deselektAllMenueBoxes();
			menuItemAlle.setSelected(true);
			projektAuswahl.setStatusbarSpalte6(LPMain.getTextRespectUISPr("proj.alle"), true);
		} else if (e.getActionCommand().equals(MENU_ANSICHT_PROJEKT_ALLE_OFFENE)) {
			FilterKriterium[] fk = ProjektFilterFactory.getInstance().createFKAlleOffene();
			projektAuswahl.setDefaultFilter(fk);
			projektAuswahl.eventYouAreSelected(false);
			deselektAllMenueBoxes();
			menuItemAlleOffene.setSelected(true);
			projektAuswahl.setStatusbarSpalte6(LPMain.getTextRespectUISPr("proj.alle.offene"), true);
		} else if (e.getActionCommand().equals(MENU_ANSICHT_PROJEKT_MEINE)) {
			FilterKriterium[] fk = ProjektFilterFactory.getInstance().createFKPersonalIId();
			projektAuswahl.setDefaultFilter(fk);
			projektAuswahl.eventYouAreSelected(false);
			deselektAllMenueBoxes();
			menuItemMeine.setSelected(true);
			projektAuswahl.setStatusbarSpalte6(LPMain.getTextRespectUISPr("proj.meine"), true);
		} else if (e.getActionCommand().equals(MENU_ANSICHT_PROJEKT_MEINE_OFFENE)) {
			FilterKriterium[] fk = ProjektFilterFactory.getInstance().createFKMeineOffene();
			projektAuswahl.setDefaultFilter(fk);
			projektAuswahl.eventYouAreSelected(false);
			deselektAllMenueBoxes();
			menuItemMeineOffenen.setSelected(true);
			projektAuswahl.setStatusbarSpalte6(LPMain.getTextRespectUISPr("proj.meine.offene"), true);
		} else if (e.getActionCommand().equals(MENU_ANSICHT_PROJEKT_ALLE_OHNE_STORNIERT)) {
			FilterKriterium[] fk = ProjektFilterFactory.getInstance().createFKAlleOhneStorniert();
			projektAuswahl.setDefaultFilter(fk);
			projektAuswahl.eventYouAreSelected(false);
			deselektAllMenueBoxes();
			menuItemAlleOhneStorniert.setSelected(true);
			projektAuswahl.setStatusbarSpalte6(LPMain.getTextRespectUISPr("proj.alle.ohne.storniert"), true);
		} else if (e.getActionCommand().equals(MENU_JOURNAL_PROJEKT_ALLE_OFFENE)) {
			getInternalFrame().showReportKriterien(
					new ReportProjektOffene(getInternalFrameProjekt(), LPMain.getTextRespectUISPr("proj.alle.offene")));
		} else if (e.getActionCommand().equals(MENU_JOURNAL_PROJEKT_ALLE)) {
			getInternalFrame().showReportKriterien(
					new ReportProjektAlle(getInternalFrameProjekt(), LPMain.getTextRespectUISPr("proj.alle")));
		} else if (e.getActionCommand().equals(MENU_JOURNAL_PROJEKT_ALLE_DETAILLIERT)) {
			getInternalFrame().showReportKriterien(new ReportProjektAlleDetailliert(getInternalFrameProjekt(),
					LPMain.getTextRespectUISPr("proj.alle.detailliert")));
		} else if (e.getActionCommand().equals(MENU_JOURNAL_PROJEKT_ERLEDIGT)) {
			getInternalFrame().showReportKriterien(
					new ReportProjektErledigt(getInternalFrameProjekt(), LPMain.getTextRespectUISPr("proj.erledigt")));
		} else if (e.getActionCommand().equals(MENU_JOURNAL_PROJEKT_AUSWAHLLISTE)) {
			getInternalFrame().showReportKriterien(new ReportProjektAuswahlliste(getInternalFrameProjekt(),
					LPMain.getTextRespectUISPr("proj.journal.auswahlliste")));
		} else if (e.getActionCommand().equals(MENU_JOURNAL_ATIVITAETSUEBERISCHT)) {
			getInternalFrame().showReportKriterien(new ReportAktivitaetsuebersicht(getInternalFrameProjekt(),
					LPMain.getTextRespectUISPr("proj.journal.aktivitaetsuebersicht")));
		} else if (e.getActionCommand().equals(MENU_JOURNAL_PROJEKTFORECAST)) {
			getInternalFrame().showReportKriterien(new ReportProjektforecast(getInternalFrameProjekt(),
					LPMain.getTextRespectUISPr("proj.journal.projektforecast")));
		} else if (e.getActionCommand().equals(MENU_INFO_STATISTIK)) {
			if (getProjektDto() != null && getProjektDto().getIId() != null) {
				getInternalFrame().showReportKriterien(new ReportProjektstatistik(getInternalFrameProjekt(),
						getProjektDto().getIId(), LPMain.getTextRespectUISPr("lp.statistik")));
			}
		} else if (e.getActionCommand().equals(MENU_AENDERUNGEN_EIGENSCHAFTEN)) {
			if (getProjektDto() != null && getProjektDto().getIId() != null) {
				getInternalFrame().showReportKriterien(
						new ReportAenderungenEigenschaften(getProjektDto().getIId(), getInternalFrameProjekt(),
								LPMain.getTextRespectUISPr("projekt.report.aenderungeneigenschaften")));
			}
		} else if (e.getActionCommand().equals(MENUE_PFLEGE_PROJEKTEIGENSCHAFTEN_XLS_IMPORT)) {
			DialogEigenschaftenImportXLS d = new DialogEigenschaftenImportXLS(this,
					PanelFac.PANEL_PROJEKTEIGENSCHAFTEN);
		} else if (e.getActionCommand().equals(MENUE_PFLEGE_HISTORYEIGENSCHAFTEN_XLS_IMPORT)) {
			DialogEigenschaftenImportXLS d = new DialogEigenschaftenImportXLS(this, PanelFac.PANEL_PROJEKTHISTORY);
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		getInternalFrame().setRechtModulweit(sRechtModulweit);

		Integer iIdProjekt = getProjektDto().getIId();

		// dtos hinterlegen
		initializeDtos(iIdProjekt);

		int selectedCur = this.getSelectedIndex();

		if (selectedCur == IDX_PANEL_PROJEKTAUSWAHL) {
			// gehe zu QP1.
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, "");
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
			refreshProjektAuswahl();
			projektAuswahl.eventYouAreSelected(false);
			// im QP die Buttons setzen.
			projektAuswahl.updateButtons();
			refreshMenuAnsicht(true);
			// alle offene
			if (iIdProjekt == null) {

				PersonalDto persDto = DelegateFactory.getInstance().getPersonalDelegate()
						.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());

				if (Helper.short2boolean(persDto.getBStartMitMeinenOffenenProjekten())) {
					this.actionPerformed(
							new ActionEvent(this, ActionEvent.ACTION_PERFORMED, MENU_ANSICHT_PROJEKT_MEINE_OFFENE));
				} else {
					this.actionPerformed(
							new ActionEvent(this, ActionEvent.ACTION_PERFORMED, MENU_ANSICHT_PROJEKT_ALLE_OFFENE));
				}

			}
		} else if (selectedCur == IDX_PANEL_PROJEKTKOPFDATEN) {
			refreshProjektKopfdaten();
			projektKopfdaten.eventYouAreSelected(false); // sonst werden die
															// buttons nicht
															// richtig gesetzt!
		} else if (selectedCur == IDX_PANEL_COCKPIT) {
			projektCockpit = new PanelCockpit(getInternalFrame(), this);
			setComponentAt(IDX_PANEL_COCKPIT, projektCockpit);

		} else if (selectedCur == IDX_PANEL_PROJEKTHISTORY) {

			boolean hatRecht = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PROJ_DETAILS_CUD);
			if (hatRecht == true && DelegateFactory.getInstance().getProjektServiceDelegate()
					.istMeinProjekt(getProjektDto().getIId(), LPMain.getTheClient().getIDPersonal())) {

				getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_UPDATE);
			}

			refreshProjektHistory();
			panelQueryHistory.eventYouAreSelected(false);
			panelQueryHistory.updateButtons();
			panelHistory.eventYouAreSelected(false); // sonst werden die buttons
														// nicht richtig
														// gesetzt!
		} else if (selectedCur == IDX_PANEL_ZEITDATEN) {
			if (!pdKriterienLoszeitenUeberMenueAufgerufen) {
				getInternalFrame().showPanelDialog(getKriterienProjektzeiten(true));
			}
			setTitleProjekt(LPMain.getTextRespectUISPr("fert.tab.oben.istzeitdaten.title"));
		} else if (selectedCur == IDX_PANEL_PROJEKTQUEUE) {
			refreshProjektQueue();
			panelQueryQueue.eventYouAreSelected(false);
			panelQueryQueue.updateButtons();
		} else if (selectedCur == IDX_PANEL_TELEFONZEITEN) {
			refreshProjektTelefonzeiten();
			panelQueryTelefonzeiten.eventYouAreSelected(false);
			panelQueryTelefonzeiten.updateButtons();
		} else if (selectedCur == IDX_PANEL_ZEITBUCHUNGEN) {
			refreshProjektZeitbuchungen();
			panelQueryZeitbuchungen.eventYouAreSelected(false);
			panelQueryZeitbuchungen.updateButtons();
		} else if (selectedCur == IDX_PANEL_PROJEKTVERLAUF) {
			getPanelProjektverlauf().eventYouAreSelected(false);
			setSelectedComponent(projektverlauf);
			projektverlauf.updateButtons(new LockStateValue(null, null, PanelBasis.LOCK_IS_NOT_LOCKED));
			setTitleProjekt(LPMain.getTextRespectUISPr("proj.projektverlauf"));
		} else if (selectedCur == IDX_PANEL_TECHNIKER) {
			getPanelSplitProjekttechniker(true).eventYouAreSelected(false);
			refreshFilterTechniker();
			panelQueryTechniker.eventYouAreSelected(false);
			panelQueryTechniker.updateButtons();
			panelDetailTechniker.eventYouAreSelected(false);
		} else if (selectedCur == IDX_PANEL_GRUPPEN) {
			getPanelSplitProjektgruppe(true).eventYouAreSelected(false);
			refreshFilterGruppen();
			panelQueryGruppen.eventYouAreSelected(false);
			panelQueryGruppen.updateButtons();
			panelDetailGruppen.eventYouAreSelected(false);
		} else if (selectedCur == IDX_PANEL_TAETIGKEITEN) {
			getPanelSplitProjekttaetigkeiten(true).eventYouAreSelected(false);
			refreshFilterTaetigkeiten();
			panelQueryTaetigkeiten.eventYouAreSelected(false);
			panelQueryTaetigkeiten.updateButtons();
			panelDetailTaetigkeiten.eventYouAreSelected(false);
		} else if (selectedCur == IDX_PANEL_PROJEKTEIGENSCHAFTEN) {
			refreshEigenschaften(iIdProjekt);
			panelDetailProjekteigenschaft.eventYouAreSelected(false);
		} else if (selectedCur == IDX_PANEL_HISTORYEIGENSCHAFTEN) {

			refreshProjektHistory();

			panelQueryHistory.eventYouAreSelected(false);

			if (panelQueryHistory.getSelectedId() == null) {
				DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.info"),
						LPMain.getInstance().getTextRespectUISPr("proj.history.eigenschaften.keindetailausgewaehlt"));

				setSelectedIndex(IDX_PANEL_PROJEKTHISTORY);
				lPEventObjectChanged(e);
			} else {
				refreshHistoryEigenschaften();
				panelDetailHistoryeigenschaft.eventYouAreSelected(false);
			}

		}
	}

	private PanelDialogKriterienProjektzeiten getKriterienProjektzeiten(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelDialogKriterienProjektzeiten == null && bNeedInstantiationIfNull) {
			panelDialogKriterienProjektzeiten = new PanelDialogKriterienProjektzeiten(getInternalFrame(), this,
					LPMain.getTextRespectUISPr("fert.title.kriterienloszeiten"));

		}
		return panelDialogKriterienProjektzeiten;
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		super.lPEventItemChanged(e);
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			handleItemChanged(e);
		} else
		// eine Zeile in einer Tabelle, bei der ich Listener bin, wurde
		// doppelgeklickt
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			handleGotoDetailPanel(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			handleActionNew(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_PRINT) {
			handleActionPrint(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			handleActionSave(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			handleActionDiscard(e);
		} else
		// wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
		if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {

			if (e.getSource() == projektKopfdaten) {
				// btndiscard: 2 die Liste neu laden, falls sich etwas geaendert
				// hat
				projektAuswahl.eventYouAreSelected(false);
				// btndiscard: 3 nach einem Discard ist der aktuelle Key nicht
				// mehr gesetzt
				setKeyWasForLockMe();
				// btndiscard: 4 der Key der Kopfdaten steht noch auf new|...
				projektKopfdaten.setKeyWhenDetailPanel(projektAuswahl.getSelectedId());
				// btndiscard: 5 auf die Auswahl schalten
				setSelectedComponent(projektAuswahl);
				// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
				// restlichen panel deaktivieren
				if (projektAuswahl.getSelectedId() == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_PROJEKTAUSWAHL, false);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_PROJEKTAUSWAHL, true);
				}
				projektKopfdaten.updateButtons(projektKopfdaten.getLockedstateDetailMainKey());
			} else if (e.getSource() == panelHistory) {
				panelQueryHistory.updateButtons();
				panelHistory.eventYouAreSelected(false);
				panelQueryHistory.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailTechniker) {
				setKeyWasForLockMe();
				getPanelSplitProjekttechniker(true).eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailGruppen) {
				setKeyWasForLockMe();
				getPanelSplitProjektgruppe(true).eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailTaetigkeiten) {
				setKeyWasForLockMe();
				getPanelSplitProjekttaetigkeiten(true).eventYouAreSelected(false);
			}
		} // der OK Button in einem PanelDialog wurde gedrueckt
		else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == getKriterienProjektzeiten(false)) {
				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = getKriterienProjektzeiten(true).getAlleFilterKriterien();

				// die Kriterien dem PanelTabelle setzen
				getPanelTabelleProjektzeiten().setDefaultFilter(aAlleKriterien);

				// die Auftragzeiten aktualisieren
				getPanelTabelleProjektzeiten().eventYouAreSelected(false);

				// man steht auf alle Faelle in den Auftragzeiten
				setSelectedComponent(getPanelTabelleProjektzeiten());
				pdKriterienLoszeitenUeberMenueAufgerufen = false;
				getPanelTabelleProjektzeiten()
						.updateButtons(new LockStateValue(null, null, PanelBasis.LOCK_IS_NOT_LOCKED));
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
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (e.getSource() == projektAuswahl) {
				if (sAspectInfo.equals(MY_OWN_NEW_IN_QUEUE_AUFNEHMEN)) {
					if (projektAuswahl.getTable().getRowCount() > 0) {
						Integer iIdPosition = (Integer) projektAuswahl.getSelectedId();
						ProjektDto projektDto = DelegateFactory.getInstance().getProjektDelegate()
								.projektFindByPrimaryKey(iIdPosition);
						if (projektDto.getISort() == null) {
							DelegateFactory.getInstance().getProjektDelegate().inQueueAufnehmen(iIdPosition);

						} else {
							DelegateFactory.getInstance().getProjektDelegate().ausQueueEntfernen(iIdPosition);
						}
						projektAuswahl.eventYouAreSelected(false);
					}
				}
			} else if (e.getSource() == panelQueryQueue) {
				if (sAspectInfo.equals(MY_OWN_NEW_AUS_QUEUE_ENTFERNEN)) {
					if (panelQueryQueue.getTable().getRowCount() > 0) {
						Integer iIdPosition = (Integer) panelQueryQueue.getSelectedId();
						DelegateFactory.getInstance().getProjektDelegate().ausQueueEntfernen(iIdPosition);
						panelQueryQueue.eventYouAreSelected(false);
					}
				}
			} else if (e.getSource() == panelQueryTechniker) {
				if (sAspectInfo.equals(MY_OWN_NEW_MEHRERE_TECHNIKER_HINZUFUEGEN)) {

					panelQueryFLRTechniker = PersonalFilterFactory.getInstance()
							.createPanelFLRPersonalMitKostenstelle(getInternalFrame(), true, true, null);
					panelQueryFLRTechniker.setMultipleRowSelectionEnabled(true);
					panelQueryFLRTechniker.addButtonAuswahlBestaetigen(null);
					new DialogQuery(panelQueryFLRTechniker);

				}
			} else if (e.getSource() == panelQueryTaetigkeiten) {
				if (sAspectInfo.equals(MY_OWN_NEW_MEHRERE_TAETIGKEITEN_HINZUFUEGEN)) {
					String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };
					panelQueryFLRArtikel = new PanelQueryFLR(null,
							ArtikelFilterFactory.getInstance().createFKArtikellisteNurArbeitszeit(),
							QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse, getInternalFrame(),
							LPMain.getTextRespectUISPr("title.artikelauswahlliste"));

					FilterKriteriumDirekt fkDirekt1 = ArtikelFilterFactory.getInstance()
							.createFKDArtikelnummer(getInternalFrame());
					FilterKriteriumDirekt fkDirekt2 = ArtikelFilterFactory.getInstance().createFKDVolltextsuche();
					panelQueryFLRArtikel.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);

					panelQueryFLRArtikel.setMultipleRowSelectionEnabled(true);
					panelQueryFLRArtikel.addButtonAuswahlBestaetigen(null);

					new DialogQuery(panelQueryFLRArtikel);

				}
			} else if (e.getSource() == panelQueryHistory) {
				if (sAspectInfo.equals(ACTION_SPECIAL_NEW_EMAIL)) {
					// panelQueryHistory.updateButtons();
					panelQueryHistory.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
					panelHistory.eventActionNew(e, true, false);
					panelHistory.eventYouAreSelected(false);
					setSelectedComponent(panelSplitHistory);
					panelHistory.beHtml();
					panelHistory.beEditMode(true);
				}
			} else if (e.getSource() == panelQueryTelefonzeiten) {
				if (sAspectInfo.equals(ACTION_SPECIAL_GOTO_TELEFONZEITEN)) {

					if (panelQueryTelefonzeiten.getSelectedId() != null) {
						wbuGotoTelefonzeiten.setOKey(panelQueryTelefonzeiten.getSelectedId());
						wbuGotoTelefonzeiten.actionPerformed(
								new ActionEvent(wbuGotoTelefonzeiten, 0, WrapperGotoButton.ACTION_GOTO));
					}
				}
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_DROP) {
			myLogger.info("Drop Changed on " + e.getSource());
			if (e.getSource() == panelHistory) {
				if (e instanceof ItemChangedEventDrop) {
					ItemChangedEventDrop dropEvent = (ItemChangedEventDrop) e;

					DelegateFactory.getInstance().getEmailMediaDelegate().createHistoryFromEmail(
							getProjektDto().getIId(), (MediaEmailMetaDto) dropEvent.getDropData());

					// createNewHistoryFromEmail((MediaEmailMetaDto)
					// dropEvent.getDropData());
					panelSplitHistory.eventYouAreSelected(true);
				}
			}
			// } else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			// if (e.getSource() instanceof PanelQuery) {
			// PanelQuery pq = (PanelQuery) e.getSource();
			// if (pq.getAspect().equals(ACTION_SPECIAL_NEW_EMAIL)) {
			// panelHistory.eventActionNew(e, true, false);
			// panelHistory.eventYouAreSelected(false);
			// setSelectedComponent(panelSplitHistory);
			// panelHistory.beHtml();
			// panelHistory.beEditMode(true);
			// }
			// }
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelHistory) {
				panelQueryHistory.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				panelHistory.beEditMode(true);
			}
		}
	}

	private void createNewHistoryFromEmail(MediaEmailMetaDto emailDto) throws Throwable {

		HistoryDto historyDto = new HistoryDto();
		historyDto.setProjektIId(getProjektDto().getIId());
		historyDto.setXText(emailDto.getXContent());
		historyDto.setTBelegDatum(emailDto.getTEmailDate());
		historyDto.setCTitel(emailDto.getCSubject());
		historyDto.setBHtml(emailDto.getBHtml());
		DelegateFactory.getInstance().getProjektDelegate().createHistory(historyDto);
	}

	private PanelTabelleProjektzeiten getPanelTabelleProjektzeiten() throws Throwable {
		if (panelTabelleProjektzeiten == null) {
			panelTabelleProjektzeiten = new PanelTabelleProjektzeiten(QueryParameters.UC_ID_PROJEKTZEITEN,
					LPMain.getTextRespectUISPr("fert.tab.oben.istzeitdaten.title"), getInternalFrameProjekt());

			// default Kriterium vorbelegen
			FilterKriterium[] aFilterKrit = new FilterKriterium[2];

			FilterKriterium krit1 = new FilterKriterium(ProjektFac.KRIT_PROJEKTZEITEN_SORTIERUNG, true,
					ZeiterfassungFac.SORTIERUNG_ZEITDATEN_PERSONAL + "", FilterKriterium.OPERATOR_EQUAL, false);

			FilterKriterium krit2 = new FilterKriterium(ProjektFac.KRIT_PROJEKT_I_ID, true,
					getProjektDto().getIId().toString(), FilterKriterium.OPERATOR_EQUAL, false);
			aFilterKrit[0] = krit1;
			aFilterKrit[1] = krit2;

			panelTabelleProjektzeiten.setDefaultFilter(aFilterKrit);

			setComponentAt(IDX_PANEL_ZEITDATEN, panelTabelleProjektzeiten);
		}
		return panelTabelleProjektzeiten;
	}

	/**
	 * handleActionDiscard
	 * 
	 * @param e ItemChangedEvent
	 * @throws Throwable
	 */
	private void handleActionDiscard(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == projektKopfdaten) {
				// aenderewaehrung: 6 die Kopfdaten muessen mit den
				// urspruenglichen Daten befuellt werden
				projektKopfdaten.eventYouAreSelected(false);
				projektKopfdaten.updateButtons(projektKopfdaten.getLockedstateDetailMainKey());
			} else if (e.getSource() == panelDetailTechniker) {
				panelSplitTechniker.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailGruppen) {
				panelSplitGruppen.eventYouAreSelected(false);
			} else if (e.getSource() == panelHistory) {
				panelSplitHistory.eventYouAreSelected(false);
			}
		}
	}

	/**
	 * Verarbeitung von ItemChangedEvent.GOTO_DETAIL_PANEL.
	 * 
	 * @param e ItemChangedEvent
	 * @throws Throwable Ausnahme
	 */
	private void handleGotoDetailPanel(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == projektAuswahl) {
			Integer projektIId = (Integer) projektAuswahl.getSelectedId();
			initializeDtos(projektIId);
			getInternalFrame().setKeyWasForLockMe(projektIId + "");
			if (projektIId != null) {
				setSelectedComponent(projektKopfdaten);
				projektKopfdaten.eventYouAreSelected(false);
			}
		} else if (e.getSource() == panelQueryQueue) {
			Integer projektIId = (Integer) panelQueryQueue.getSelectedId();
			initializeDtos(projektIId);
			getInternalFrame().setKeyWasForLockMe(projektIId + "");
			if (projektIId != null) {
				setSelectedComponent(projektKopfdaten);
				panelQueryQueue.eventYouAreSelected(false);
			}
		} else if (e.getSource() == panelQueryFLRTechniker) {
			Object[] personalIId = panelQueryFLRTechniker.getSelectedIds();
			if (panelQueryFLRTechniker.getDialog() != null) {
				panelQueryFLRTechniker.getDialog().setVisible(false);
			}
			for (int i = 0; i < personalIId.length; i++) {

				ProjekttechnikerDto projekttechnikerDto = new ProjekttechnikerDto();
				projekttechnikerDto.setProjektIId(getProjektDto().getIId());
				projekttechnikerDto.setPersonalIId((Integer) personalIId[i]);
				DelegateFactory.getInstance().getProjektServiceDelegate().createProjekttechniker(projekttechnikerDto);

			}

			panelSplitTechniker.eventYouAreSelected(false);

		} else if (e.getSource() == panelQueryFLRArtikel) {
			Object[] artikelIId = panelQueryFLRArtikel.getSelectedIds();
			if (panelQueryFLRArtikel.getDialog() != null) {
				panelQueryFLRArtikel.getDialog().setVisible(false);
			}
			for (int i = 0; i < artikelIId.length; i++) {

				ProjekttaetigkeitDto projekttaetigkeitDto = new ProjekttaetigkeitDto();
				projekttaetigkeitDto.setProjektIId(getProjektDto().getIId());
				projekttaetigkeitDto.setArtikelIId((Integer) artikelIId[i]);
				DelegateFactory.getInstance().getProjektServiceDelegate().createProjekttaetigkeit(projekttaetigkeitDto);

			}

			panelSplitTaetigkeiten.eventYouAreSelected(false);

		}
	}

	/**
	 * handleActionPrint
	 * 
	 * @throws Throwable
	 */

	private void handleActionPrint(ItemChangedEvent e) throws Throwable {

		if (e.getSource() == panelQueryGruppen) {

			if (getProjektDto() != null && getProjektDto().getIId() != null) {
				getInternalFrame()
						.showReportKriterien(new ReportProjektbaum(getInternalFrameProjekt(), getTitleDruck()));
			}

		} else {
			printProjekt();
			refreshProjektAuswahl();
		}

	}

	/**
	 * handleActionNew
	 * 
	 * @param e ItemChangedEvent
	 * @throws Throwable
	 */
	private void handleActionNew(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == projektAuswahl) {
			refreshProjektKopfdaten().eventActionNew(e, true, false);
			setSelectedComponent(projektKopfdaten);
		}
		if (e.getSource() == panelQueryHistory) {
			panelHistory.bePlain();
			panelHistory.eventActionNew(e, true, false);
			panelHistory.eventYouAreSelected(false);
			setSelectedComponent(panelSplitHistory);
		} else if (e.getSource() == panelQueryTechniker) {
			panelDetailTechniker.eventActionNew(e, true, false);
			panelDetailTechniker.eventYouAreSelected(false);
			setSelectedComponent(panelSplitTechniker);
		} else if (e.getSource() == panelQueryGruppen) {
			panelDetailGruppen.eventActionNew(e, true, false);
			panelDetailGruppen.eventYouAreSelected(false);
			setSelectedComponent(panelSplitGruppen);
		} else if (e.getSource() == panelQueryTaetigkeiten) {
			panelDetailTaetigkeiten.eventActionNew(e, true, false);
			panelDetailTaetigkeiten.eventYouAreSelected(false);
			setSelectedComponent(panelSplitTaetigkeiten);
		}
	}

	/**
	 * handleActionNew
	 * 
	 * @param e ItemChangedEvent
	 * @throws Throwable
	 */
	private void handleActionSave(ItemChangedEvent e) throws Throwable {
		if (e.getSource() == panelHistory) {
			Object oKey = panelHistory.getKeyWhenDetailPanel();
			panelQueryHistory.eventYouAreSelected(false);
			panelQueryHistory.setSelectedId(oKey);
			panelSplitHistory.eventYouAreSelected(false);
		} else if (e.getSource() == panelDetailTechniker) {
			Object oKey = panelDetailTechniker.getKeyWhenDetailPanel();
			panelQueryTechniker.eventYouAreSelected(false);
			panelQueryTechniker.setSelectedId(oKey);
			panelSplitTechniker.eventYouAreSelected(false);
		} else if (e.getSource() == panelDetailGruppen) {
			Object oKey = panelDetailGruppen.getKeyWhenDetailPanel();
			panelQueryGruppen.eventYouAreSelected(false);
			panelQueryGruppen.setSelectedId(oKey);
			panelSplitGruppen.eventYouAreSelected(false);
		} else if (e.getSource() == panelDetailTaetigkeiten) {
			Object oKey = panelDetailTaetigkeiten.getKeyWhenDetailPanel();
			panelQueryTaetigkeiten.eventYouAreSelected(false);
			panelQueryTaetigkeiten.setSelectedId(oKey);
			panelSplitTaetigkeiten.eventYouAreSelected(false);
		}
	}

	private void handleItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getSource() == panelQueryHistory) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			// key 1; IF
			getInternalFrame().setKeyWasForLockMe(key + "");
			panelHistory.setKeyWhenDetailPanel(key);
			panelHistory.eventYouAreSelected(false);
			panelQueryHistory.updateButtons();
		} else if (e.getSource() == panelQueryTechniker) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			// key 1; IF
			getInternalFrame().setKeyWasForLockMe(key + "");
			panelDetailTechniker.setKeyWhenDetailPanel(key);
			panelDetailTechniker.eventYouAreSelected(false);
			panelQueryTechniker.updateButtons();
		} else if (e.getSource() == panelQueryGruppen) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			// key 1; IF
			getInternalFrame().setKeyWasForLockMe(key + "");
			panelDetailGruppen.setKeyWhenDetailPanel(key);
			panelDetailGruppen.eventYouAreSelected(false);
			panelQueryGruppen.updateButtons();
		} else if (e.getSource() == panelQueryZeitbuchungen) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			// key 1; IF
			getInternalFrame().setKeyWasForLockMe(key + "");
			panelDetailZeitbuchungen.setKeyWhenDetailPanel(key);
			panelDetailZeitbuchungen.eventYouAreSelected(false);
			panelQueryZeitbuchungen.updateButtons();
		} else if (e.getSource() == panelQueryTaetigkeiten) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			// key 1; IF
			getInternalFrame().setKeyWasForLockMe(key + "");
			panelDetailTaetigkeiten.setKeyWhenDetailPanel(key);
			panelDetailTaetigkeiten.eventYouAreSelected(false);
			panelQueryTaetigkeiten.updateButtons();
		} else if (e.getSource() == projektAuswahl) {
			Integer iIdProjekt = (Integer) projektAuswahl.getSelectedId();
			// Anzahl
			int count = projektAuswahl.getTable().getRowCount();
			projektAuswahl.setStatusbarSpalte5(LPMain.getTextRespectUISPr("proj.anzahl") + " " + count, true);
			initializeDtos(iIdProjekt);
			setKeyWasForLockMe();
			refreshProjektKopfdaten().setKeyWhenDetailPanel(iIdProjekt);
			setTitleProjekt(LPMain.getTextRespectUISPr("lp.auswahl"));
			if (iIdProjekt == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_PROJEKTAUSWAHL, false);
			} else {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_PROJEKTAUSWAHL, true);
			}
		} else if (e.getSource() == panelQueryQueue) {
			Object key = ((ISourceEvent) e.getSource()).getIdSelected();
			Double dGesamt = DelegateFactory.getInstance().getProjektDelegate()
					.berechneGesamtSchaetzung(LPMain.getInstance().getTheClient().getIDPersonal());
			// Gesamt Schaetzung
			panelQueryQueue.setStatusbarSpalte6(LPMain.getTextRespectUISPr("proj.queue.schaetzung") + " " + dGesamt,
					true);
			// key 1; IF
			getInternalFrame().setKeyWasForLockMe(key + "");
			panelQueryQueue.updateButtons();
		}
	}

	public void resetDtos() {
		setProjektDto(new ProjektDto());
		personalZugewiesenerDto = new PersonalDto();
		personalErzeugerDto = new PersonalDto();
		partnerDto = new PartnerDto();
		ansprechpartnerDto = new AnsprechpartnerDto();
	}

	public void printProjekt() throws Throwable {
		if (!refreshProjektKopfdaten().isLockedDlg()) {
			if (getProjektDto() != null && getProjektDto().getIId() != null) {
				// das aktuelle Panel wegen Statusanzeige aktualisieren
				// aktuellesPanelAktualisieren();

				PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
						.partnerFindByPrimaryKey(projektDto.getPartnerIId());

				getInternalFrame().showReportKriterien(
						new ReportProjekt(getInternalFrame(), getProjektDto(), getTitleDruck()), partnerDto,
						projektDto.getAnsprechpartnerIId());
			}
		}
	}

	private String getTitleDruck() {
		StringBuffer buff = new StringBuffer();

		buff.append(getProjektDto().getCTitel());
		buff.append(" ");

		return buff.toString();
	}

	protected void gotoAuswahl() {
		this.setSelectedIndex(IDX_PANEL_PROJEKTAUSWAHL);
	}

	/**
	 * Den Text der Titelleiste ueberschreiben.
	 * 
	 * @param panelTitle der Title des aktuellen panel
	 * @throws Throwable
	 */
	public void setTitleProjekt(String panelTitle) throws Throwable {

		StringBuffer projektTitel = new StringBuffer("");
		if (getProjektDto() == null || getProjektDto().getIId() == null) {
			projektTitel.append(LPMain.getTextRespectUISPr("proj.projekt.neu"));
		} else {
			partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
					.partnerFindByPrimaryKey(getProjektDto().getPartnerIId());
			projektTitel
					.append(DelegateFactory.getInstance().getProjektServiceDelegate()
							.bereichFindByPrimaryKey(getProjektDto().getBereichIId()).getCBez())
					.append(" ").append(getProjektDto().getCNr() + "|" + partnerDto.getCName1nachnamefirmazeile1() + "|"
							+ getProjektDto().getCTitel());
		}

		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, panelTitle);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, projektTitel.toString());
	}

	/**
	 * Diese Methode setzt des aktuellen Projekt aus der Auswahlliste als den zu
	 * lockenden Projekt.
	 * 
	 * @throws java.lang.Throwable Ausnahme
	 */
	public void setKeyWasForLockMe() throws Throwable {
		Object oKey = projektAuswahl.getSelectedId();

		if (oKey != null) {
			initializeDtos((Integer) oKey);
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void initializeDtos(Integer iIdProjektI) throws Throwable {
		this.resetDtos();
		if (iIdProjektI != null) {
			setProjektDto(DelegateFactory.getInstance().getProjektDelegate().projektFindByPrimaryKey(iIdProjektI));
			if (getProjektDto().getPersonalIIdZugewiesener() != null) {
				personalZugewiesenerDto = DelegateFactory.getInstance().getPersonalDelegate()
						.personalFindByPrimaryKey(getProjektDto().getPersonalIIdZugewiesener());
			}
			personalErzeugerDto = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(getProjektDto().getPersonalIIdErzeuger());

			partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
					.partnerFindByPrimaryKey(getProjektDto().getPartnerIId());
			if (getProjektDto().getAnsprechpartnerIId() != null) {
				ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(getProjektDto().getAnsprechpartnerIId());
			}
		}
	}

	public InternalFrameProjekt getInternalFrameProjekt() throws Throwable {
		return (InternalFrameProjekt) getInternalFrame();
	}

	public PanelBasis getProjektKopfdaten() {
		return projektKopfdaten;
	}

	public PanelQuery getProjektAuswahl() {
		return projektAuswahl;
	}

	public ProjektDto getProjektDto() {
		return projektDto;
	}

	public void setProjektDto(ProjektDto dto) {
		this.projektDto = dto;
	}

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public void setPartnerDto(PartnerDto dto) {
		this.partnerDto = dto;
	}

	public AnsprechpartnerDto getAnsprechpartnerDto() {
		return ansprechpartnerDto;
	}

	public void setAnsprechpartnerDto(AnsprechpartnerDto dto) {
		this.ansprechpartnerDto = dto;
	}

	public PersonalDto getPersonalZugewiesenerDto() {
		return personalZugewiesenerDto;
	}

	public PersonalDto getPersonalErzeugerDto() {
		return personalErzeugerDto;
	}

	public void setPersonalErzeugerDto(PersonalDto personalDto) {
		this.personalErzeugerDto = personalDto;
	}

	public void setPersonalZugewiesenerDto(PersonalDto personalDto) {
		this.personalZugewiesenerDto = personalDto;
	}

	public PanelQuery getPanelQueryHistory() {
		return panelQueryHistory;
	}

}
