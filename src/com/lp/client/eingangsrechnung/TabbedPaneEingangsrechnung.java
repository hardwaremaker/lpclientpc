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
package com.lp.client.eingangsrechnung;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogBelegDocExport;
import com.lp.client.frame.component.DialogErDocExport;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.PanelQueryFLRGoto;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.WareneingangDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.dialog.DialogQRCodeLesen;
import com.lp.client.frame.filechooser.open.FileOpenerFactory;
import com.lp.client.frame.filechooser.open.XlsFileOpener;
import com.lp.client.frame.filechooser.open.XmlFile;
import com.lp.client.inserat.InseratFilterFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.DialogEingabeWEPreise;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.HelperTimestamp;
import com.lp.client.util.VendidataImportController;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.EinstandspreiseEinesWareneingangsDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDto;
import com.lp.server.eingangsrechnung.service.VendidataImporterResult;
import com.lp.server.eingangsrechnung.service.ZusatzkostenAnlegenResult;
import com.lp.server.eingangsrechnung.service.ZusatzkostenAnlegenWarningEntry;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.inserat.service.InseratDto;
import com.lp.server.inserat.service.InseratartikelDto;
import com.lp.server.inserat.service.InseraterDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.system.service.JxlImportErgebnis;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Panels der Eingangsrechnungsverwaltung
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.45 $
 */
public class TabbedPaneEingangsrechnung extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WareneingangDto wareneingangDto = null;

	public Object[] inseratIIds = null;

	public Object[] getInseratIIds() {
		return inseratIIds;
	}

	boolean bDarfKontieren = false;
	boolean bDarfZahlungenErfassen = false;

	public void setInseratIIds(Object[] inseratIIds) {
		this.inseratIIds = inseratIIds;
	}

	static final public String GOTO_WE = PanelBasis.ACTION_MY_OWN_NEW + "GOTO_WE";
	WrapperGotoButton wbuGotoWe = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_BESTELLUNG_WARENEINGANG);

	private PanelQuery panelQueryEingangsrechnung = null;
	private PanelEingangsrechnungKopfdaten panelEingangsrechnungKopfdaten = null;
	private PanelEingangsrechnungKopfdatenZuordnung panelEingangsrechnungKopfdatenZuordnung = null;
	private PanelSplit panelSplitAuftrag = null;
	private PanelQuery panelQueryAuftrag = null;
	private PanelEingangsrechnungAuftragszuordnung panelAuftrag = null;
	private PanelSplit panelSplitInserat = null;
	private PanelQuery panelQueryInserat = null;
	private PanelEingangsrechnungInseratzuordnung panelInserat = null;
	private PanelSplit panelSplitKontierung = null;
	private PanelQuery panelQueryKontierung = null;
	private PanelEingangsrechnungKontierung panelKontierung = null;
	private PanelBasis panelSplitZahlung = null;
	private PanelQuery panelQueryZahlung = null;
	private PanelEingangsrechnungKopfFusstext panelEingangsrechnungKopfFusstext = null;

	private PanelQueryFLR panelQueryFLRLieferant = null;

	private PanelEingangsrechnungZahlung panelZahlung = null;

	private PanelQuery panelQueryWareneingaenge = null;

	private PanelQuery panelQueryReisezeiten = null;

	private PanelQueryFLR panelQueryFLREingangsrechnungenZuErledigen = null;

	public String sRechtModulweit = null;

	private JLabel lblweDiff = new JLabel();
	private JLabel lblweDiffPanelQueryFLRWEPsMehrererWEs = new JLabel();

	private PanelTabelleEingangsrechnungUebersicht panelUebersicht = null;
	private PanelDialogKriterienEingangsrechnungUebersicht panelKriterienUebersicht = null;

	private IVendidataImportController<VendidataImporterResult> vendidataImportController = null;
	private ReversechargeartDto rcartOhneDto = null;
	public int IDX_EINGANGSRECHNUNGEN = -1;
	public int iDX_KOPFDATEN = -1;
	public int iDX_ZUORDNUNG = -1;
	public int iDX_AUFTRAGSZUORDNUNG = -1;
	public int iDX_INSERATZUORDNUNG = -1;
	public int iDX_KONTIERUNG = -1;
	public int iDX_ZAHLUNGEN = -1;
	public int iDX_WARENEINGAENGE = -1;
	public int iDX_KOPFFUSSTEXT = -1;
	public int iDX_UEBERSICHT = -1;
	public int iDX_REISEZEITEN = -1;

	private final static String MENU_ACTION_ALLE = "menu_action_er_alle";
	private final static String MENU_ACTION_FEHLENDE_ZOLLPAPIERE = "menu_action_fehlende_zollpapiere";
	private final static String MENU_ACTION_ERFASSTE_ZOLLPAPIERE = "menu_action_erfasste_zollpapiere";
	private final static String MENU_ACTION_OFFENE = "menu_action_er_offene";
	private final static String MENU_ACTION_ZAHLUNG = "menu_action_er_zahlung";
	private final static String MENU_ACTION_KONTIERUNG = "menu_action_er_kontierung";
	private final static String MENU_ACTION_NICHT_ABGERCHNETE_AZ = "menu_action_nichtabgerechnete_az";
	private final static String MENUE_ACTION_BEARBEITEN_MANUELL_ERLEDIGEN = "MENU_ACTION_BEARBEITEN_MANUELL_ERLEDIGEN";
	private final static String MENUE_ACTION_FREIGABEDATUM_AENDERN = "MENUE_ACTION_FREIGABEDATUM_AENDERN";
	private final static String MENUE_ACTION_DRUCKEN = "menu_action_er_drucken";
	private final static String MENUE_ACTION_DOKUMENTE_DRUCKEN = "menu_action_er_dokumente_drucken";
	private final static String MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME = "MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME";
	private final static String MENUE_ACTION_BEARBEITEN_MAHNDATEN = "MENUE_ACTION_BEARBEITEN_MAHNDATEN";
	private final static String MENUE_ACTION_BEARBEITEN_WIEDERHOLENDE_ZUSATZKOSTEN_ANLEGEN = "MENUE_ACTION_WIEDERHOLENDE_ZUSATZKOSTEN_ANLEGEN";
	private final static String MENU_ACTION_IMPORT_4VENDING = "menu_action_er_import_4vending";
	private final static String MENU_ACTION_IMPORT_XLS = "menu_action_er_import_xls";

	private final static String MENU_ACTION_ER_AUS_QR = PanelBasis.ACTION_MY_OWN_NEW + "menu_action_er_aus_qr";

	private final String MENUE_ACTION_ZUORDNUNG_ER_LOESCHEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_ACTION_ZUORDNUNG_ER_LOESCHEN";

	private final static String MENUE_ACTION_WEP_PREIS_ERFASST = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_ACTION_WEP_PREIS_ERFASST";

	private final static String MENUE_ACTION_WEP_PREIS_ERFASST_WIE_ERSTE_MARKIERTE_ZEILE = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_ACTION_WEP_PREIS_ERFASST_WIE_ERSTE_MARKIERTE_ZEILE";

	private final static String MENUE_ACTION_WEP_PREIS_EINGEBEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_ACTION_WEP_PREIS_EINGEBEN";

	private final static String MENUE_ACTION_EXPORT_DOKUMENTE = "menu_action_export_er_dokumente";

	private JLabel inseraterSumme = new JLabel();

	private BigDecimal bdBetragAusQrCode = null;
	private String waehrungAusQRCode = null;

	private EingangsrechnungDto eingangsrechnungDto = null;
	private LieferantDto lieferantDto = null;

	public Integer iERPruefen = 0;

	// private static final String ACTION_SPECIAL_SCAN = "action_special_scan";

	// private final static String BUTTON_SCAN = PanelBasis.ACTION_MY_OWN_NEW
	// + ACTION_SPECIAL_SCAN;
	public final static String EXTRA_NEU_AUS_BESTELLUNG = "aus_bestellung";

	public final static String MY_OWN_NEW_AUS_BESTELLUNG = PanelBasis.ACTION_MY_OWN_NEW + EXTRA_NEU_AUS_BESTELLUNG;

	private PanelQueryFLR panelQueryFLRBestellungauswahl = null;

	private PanelQuery panelQueryWEsEinerRechnungsadresse = null;

	private DialogWEsEinerRechnungsadresse dDialogWEsEinerRechnungsadresse = null;

	private PanelQueryFLR panelQueryFLRWEPsMehrererWEs = null;

	private boolean bZusatzkosten = false;
	private boolean bZusatzfunktion4Vending = false;

	public boolean isBZusatzkosten() {
		return bZusatzkosten;
	}

	public boolean isBDarfKontieren() {
		return bDarfKontieren;
	}

	public TabbedPaneEingangsrechnung(InternalFrame internalFrameI, boolean bZusatzkosten, String title)
			throws Throwable {
		super(internalFrameI, title);
		sRechtModulweit = getInternalFrame().getRechtModulweit();
		this.bZusatzkosten = bZusatzkosten;

		bDarfKontieren = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_ER_DARF_KONTIEREN);

		bDarfZahlungenErfassen = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_ER_DARF_ZAHLUNGEN_ERFASSEN);

		bZusatzfunktion4Vending = LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_4VENDING_SCHNITTSTELLE);
		vendidataImportController = new VendidataImportController();

		ParametermandantDto parametermandantRCDto = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
						ParameterFac.PARAMETER_EINGANGSRECHNUNG_PRUEFEN);
		iERPruefen = (Integer) parametermandantRCDto.getCWertAsObject();

		jbInit();
		initComponents();
	}

	public EingangsrechnungDto getEingangsrechnungDto() {
		return eingangsrechnungDto;
	}

	public LieferantDto getLieferantDto() {
		return lieferantDto;
	}

	protected Integer getSelectedIIdER() throws Throwable {
		return (Integer) getPanelQueryEingangsrechnung(true).getSelectedId();
	}

	private void setTitleIdxAsILike() throws Throwable {
		StringBuffer sTitle = new StringBuffer();
		if (getEingangsrechnungDto().getCNr() != null) {
			sTitle.append(getEingangsrechnungDto().getCNr());
			sTitle.append(" ");
		}
		sTitle.append(getLieferantDto().getPartnerDto().formatFixTitelName1Name2());
		if (getSelectedIndex() == iDX_ZAHLUNGEN) {
			if (getLieferantDto() != null && getLieferantDto().getKontoIIdKreditorenkonto() != null) {
				KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
						.kontoFindByPrimaryKey(getLieferantDto().getKontoIIdKreditorenkonto());
				sTitle.append(LPMain.getMessageTextRespectUISPr("er.zahlungen.kreditorennr", kontoDto.getCNr()));
			}
		}
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitle.toString());
	}

	protected void setEingangsrechnungDto(EingangsrechnungDto eingangsrechnungDto) throws Throwable {
		this.eingangsrechnungDto = eingangsrechnungDto;
		refreshFilterKontierung();
		refreshFilterZahlung();
		if (bZusatzkosten == false) {
			refreshFilterAuftragszuordnung();
			refreshFilterWareneingaenge();
			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_REISEZEITEN)) {
				refreshFilterReisezeiten();
			}

		}

		if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_INSERAT)) {
			refreshFilterInseratzuordnung();
		}

		// Lieferant holen
		if (getEingangsrechnungDto() != null) {
			// aber nur dann, wenn ich den nicht schon hab
			if (getLieferantDto() == null
					|| !getLieferantDto().getIId().equals(eingangsrechnungDto.getLieferantIId())) {
				setLieferantDto(DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(eingangsrechnungDto.getLieferantIId()));
			}
			setTitleIdxAsILike();
		} else {
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
		}
		enablePanels();
	}

	protected void setLieferantDto(LieferantDto lieferantDto) {
		this.lieferantDto = lieferantDto;
	}

	private void jbInit() throws Throwable {

		rcartOhneDto = DelegateFactory.getInstance().getFinanzServiceDelegate().reversechargeartFindOhne();

		// Tab 1: Kassenbuecher
		IDX_EINGANGSRECHNUNGEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.auswahl"), null, null,
				LPMain.getTextRespectUISPr("lp.auswahl"));
		// Tab 2: Kopfdaten

		iDX_KOPFDATEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.kopfdaten"), null, null,
				LPMain.getTextRespectUISPr("lp.kopfdaten"));
		// Tab 2: Auftragszuordnung
		if (bZusatzkosten == false) {
			if (bDarfKontieren) {

				iDX_ZUORDNUNG = reiterHinzufuegen(LPMain.getTextRespectUISPr("er.zuordnung"), null, null,
						LPMain.getTextRespectUISPr("er.zuordnung"));
			}
			// Tab 2: Auftragszuordnung
			// SP1120 -> wenn kein Auftrag, dann abschalten
			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_AUFTRAG)) {

				iDX_AUFTRAGSZUORDNUNG = reiterHinzufuegen(
						LPMain.getTextRespectUISPr("er.tab.oben.auftragszuordnung.title"), null, null,
						LPMain.getTextRespectUISPr("er.tab.oben.auftragszuordnung.tooltip"));
			}
		}

		if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_INSERAT)) {

			iDX_INSERATZUORDNUNG = reiterHinzufuegen(LPMain.getTextRespectUISPr("er.tab.oben.inseratzuordnung.title"),
					null, null, LPMain.getTextRespectUISPr("er.tab.oben.inseratzuordnung.title"));
		}

		if (bDarfKontieren) {
			// Tab 3: Kontierung

			iDX_KONTIERUNG = reiterHinzufuegen(LPMain.getTextRespectUISPr("er.tab.oben.kontierung.title"), null, null,
					LPMain.getTextRespectUISPr("er.tab.oben.kontierung.tooltip"));
		}
		// Tab 4: Zahlungen

		iDX_ZAHLUNGEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("er.tab.oben.zahlungen.title"), null, null,
				LPMain.getTextRespectUISPr("er.tab.oben.zahlungen.tooltip"));
		if (bZusatzkosten == false) {
			// Tab 5: Wareneingaenge
			iDX_WARENEINGAENGE = reiterHinzufuegen(LPMain.getTextRespectUISPr("er.tab.oben.wareneingaenge.title"), null,
					null, LPMain.getTextRespectUISPr("er.tab.oben.wareneingaenge.tooltip"));

		}
		if (bZusatzkosten == false) {
			iDX_KOPFFUSSTEXT = reiterHinzufuegen(LPMain.getTextRespectUISPr("er.kopffusstext"), null, null,
					LPMain.getTextRespectUISPr("er.kopffusstext"));
		}

		// Tab 6: Uebersicht
		iDX_UEBERSICHT = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.umsatzuebersicht"), null, null,
				LPMain.getTextRespectUISPr("lp.umsatzuebersicht"));

		if (bZusatzkosten == false && LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_REISEZEITEN)) {

			iDX_REISEZEITEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("pers.reisezeiten"), null, null,
					LPMain.getTextRespectUISPr("pers.reisezeiten"));

		}

		setSelectedComponent(getPanelQueryEingangsrechnung(true));
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	/**
	 * getPanelQueryEingangsrechnung mit extrem lazy loading.
	 * 
	 * @return PanelQuery
	 * @param bNeedInstantiationIfNull boolean
	 * @throws Throwable
	 */
	protected PanelQuery getPanelQueryEingangsrechnung(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryEingangsrechnung == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseEingangsrechnung = { PanelBasis.ACTION_NEW };
			FilterKriterium[] filtersEingangsrechnung = EingangsrechnungFilterFactory.getInstance()
					.createFKEingangsrechnungAuswahl(bZusatzkosten);

			if (bZusatzkosten == true) {
				panelQueryEingangsrechnung = new PanelQuery(null, filtersEingangsrechnung,
						QueryParameters.UC_ID_ZUSATZKOSTEN, aWhichButtonIUseEingangsrechnung, getInternalFrame(),
						LPMain.getTextRespectUISPr("lp.auswahl"), true);
			} else {
				panelQueryEingangsrechnung = new PanelQuery(null, filtersEingangsrechnung,
						QueryParameters.UC_ID_EINGANGSRECHNUNG, aWhichButtonIUseEingangsrechnung, getInternalFrame(),
						LPMain.getTextRespectUISPr("lp.auswahl"), true);
			}

			FilterKriteriumDirekt fkDirekt1 = EingangsrechnungFilterFactory.getInstance()
					.createFKDEingangsrechnungnummer();
			FilterKriteriumDirekt fkDirekt2 = EingangsrechnungFilterFactory.getInstance().createFKDLieferantname();
			panelQueryEingangsrechnung.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);
			panelQueryEingangsrechnung
					.addDirektFilter(EingangsrechnungFilterFactory.getInstance().createFKDTextSuchen());

			ParametermandantDto parametermandantRCDto = DelegateFactory.getInstance().getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_EINGANGSRECHNUNG,
							ParameterFac.PARAMETER_EINGANGSRECHNUNG_AUS_QR_CODE);
			boolean bErAusQR = (Boolean) parametermandantRCDto.getCWertAsObject();
			if (bZusatzkosten == false && bErAusQR) {
				panelQueryEingangsrechnung.createAndSaveAndShowButton("/com/lp/client/res/scanner16x16.png",
						LPMain.getTextRespectUISPr("er.erausqr"), MENU_ACTION_ER_AUS_QR,
						RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD);
			}

			panelQueryEingangsrechnung.befuelleFilterkriteriumSchnellansicht(
					EingangsrechnungFilterFactory.getInstance().createFKEingangsrechnungSchnellansicht());
			panelQueryEingangsrechnung.addStatusBar();
			setComponentAt(IDX_EINGANGSRECHNUNGEN, panelQueryEingangsrechnung);
			// Button Eingangsrechnung aus Bestellung anlegen
			// Falsch verstandenes Projekt... vll mal zu verwenden (SK)
			/*
			 * panelQueryEingangsrechnung.createAndSaveAndShowButton(
			 * "/com/lp/client/res/shoppingcart_full16x16.png", LPMain.getTextRespectUISPr
			 * ("lp.tooltip.datenausbestehenderbestellung"), MY_OWN_NEW_AUS_BESTELLUNG
			 * ,RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD);
			 */

		}
		return panelQueryEingangsrechnung;
	}

	/**
	 * getPanelQueryAuftrag mit extrem lazy loading.
	 * 
	 * @return PanelQuery
	 * @param bNeedInstantiationIfNull boolean
	 * @throws Throwable
	 */
	private PanelQuery getPanelQueryAuftrag(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryAuftrag == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseAuftrag = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };
			panelQueryAuftrag = new PanelQuery(null, null, QueryParameters.UC_ID_EINGANGSRECHNUNG_AUFTRAGSZUORDNUNG,
					aWhichButtonIUseAuftrag, getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.auftragszuordnung.title"), true);
			panelQueryAuftrag.setMultipleRowSelectionEnabled(true);
		}
		return panelQueryAuftrag;
	}

	private PanelQuery getPanelQueryInserat(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryInserat == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseAuftrag = { PanelBasis.ACTION_NEW };
			panelQueryInserat = new PanelQuery(null, null, QueryParameters.UC_ID_INSERATER, aWhichButtonIUseAuftrag,
					getInternalFrame(), LPMain.getTextRespectUISPr("er.tab.oben.inseratzuordnung.title"), true);

			panelQueryInserat.getToolBar().getToolsPanelCenter().add(inseraterSumme);

		}
		if (getEingangsrechnungDto() != null && getEingangsrechnungDto().getNBetrag() != null
				&& getEingangsrechnungDto().getNUstBetrag() != null) {
			BigDecimal erBetragNetto = getEingangsrechnungDto().getNBetrag();

			if (!isReversecharge()) {
				erBetragNetto = erBetragNetto.subtract(getEingangsrechnungDto().getNUstBetrag());
			}

			BigDecimal bdwertInserate = new BigDecimal(0);
			InseraterDto[] inseraterDtos = DelegateFactory.getInstance().getInseratDelegate()
					.inseraterFindByEingangsrechnungIId(getEingangsrechnungDto().getIId());
			for (int i = 0; i < inseraterDtos.length; i++) {
				bdwertInserate = bdwertInserate.add(inseraterDtos[i].getNBetrag());
				bdwertInserate = bdwertInserate
						.add(DelegateFactory.getInstance().getInseratDelegate().berechneWerbeabgabeLFEinesInserates(
								inseraterDtos[i].getInseratIId(), inseraterDtos[i].getNBetrag()));

			}

			inseraterSumme
					.setText(LPMain.getTextRespectUISPr("iv.inserate.summe") + " = " + Helper.formatZahl(bdwertInserate,
							Defaults.getInstance().getIUINachkommastellenPreiseEK(), LPMain.getTheClient().getLocUi()));

			// SP3347
			bdwertInserate = Helper.rundeKaufmaennisch(bdwertInserate, 2);

			if (erBetragNetto.doubleValue() != bdwertInserate.doubleValue()) {
				inseraterSumme.setForeground(Color.RED);
			} else {
				inseraterSumme.setForeground(Color.BLACK);
			}
		}

		return panelQueryInserat;
	}

	/**
	 * getPanelQueryAuftrag mit extrem lazy loading.
	 * 
	 * @return PanelQuery
	 * @param bNeedInstantiationIfNull boolean
	 * @throws Throwable
	 */
	private PanelQuery getPanelQueryKontierung(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryKontierung == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseKontierung = { PanelBasis.ACTION_NEW };
			panelQueryKontierung = new PanelQuery(null, null, QueryParameters.UC_ID_EINGANGSRECHNUNG_KONTIERUNG,
					aWhichButtonIUseKontierung, getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.kontierung.title"), true);
		}
		return panelQueryKontierung;
	}

	protected PanelBasis getAktuellesPanel() {
		if (getSelectedComponent() instanceof PanelBasis)
			return (PanelBasis) getSelectedComponent();
		return null;
	}

	/**
	 * getPanelQueryZahlung mit extrem lazy loading.
	 * 
	 * @return PanelQuery
	 * @param bNeedInstantiationIfNull boolean
	 * @throws Throwable
	 */
	private PanelQuery getPanelQueryZahlung(boolean bNeedInstantiationIfNull) throws Throwable {
		return getPanelQueryZahlung(bNeedInstantiationIfNull, false);
	}
	
	private PanelQuery getPanelQueryZahlung(boolean bNeedInstantiationIfNull, boolean bkommtVonNew) throws Throwable {
		if (panelQueryZahlung == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseZahlung = new String[] { PanelBasis.ACTION_NEW };
			// if
			// (eingangsrechnungDto.getEingangsrechnungartCNr().equals(EingangsrechnungFac.
			// EINGANGSRECHNUNGART_GUTSCHRIFT)) {
			// aWhichButtonIUseZahlung = new String[]{};
			// }
			panelQueryZahlung = new PanelQuery(null, null, QueryParameters.UC_ID_EINGANGSRECHNUNG_ZAHLUNG,
					aWhichButtonIUseZahlung, getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.zahlungen.title"), true);
		}
		if (eingangsrechnungDto != null) {
			if (EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT
					.equals(eingangsrechnungDto.getEingangsrechnungartCNr())) {
				LPButtonAction item = (LPButtonAction) panelQueryZahlung.getHmOfButtons().get(PanelBasis.ACTION_NEW);
				item.getButton().setEnabled(false);
			} else {
				LPButtonAction item = (LPButtonAction) panelQueryZahlung.getHmOfButtons().get(PanelBasis.ACTION_NEW);
				if (bDarfZahlungenErfassen && bkommtVonNew == false) {

					// PJ22160
					boolean bVollstaendigKontiert = true;
					if (this.getEingangsrechnungDto() != null && this.getEingangsrechnungDto().getIId() != null) {
						
						boolean bMehrfach = this.getEingangsrechnungDto().getKontoIId() == null
								|| this.getEingangsrechnungDto().getKostenstelleIId() == null;

						if (bMehrfach) {
							BigDecimal bdNochNichtKontiert = DelegateFactory.getInstance().getEingangsrechnungDelegate()
									.getWertNochNichtKontiert(this.getEingangsrechnungDto().getIId());
							if (bdNochNichtKontiert.doubleValue() > 0) {
								bVollstaendigKontiert = false;
							}
						}
					}

					if (bVollstaendigKontiert) {
						item.getButton().setEnabled(true);
					}
				}
			}
		}
		return panelQueryZahlung;
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// da will jemand die Buchungen ansehen
			if (e.getSource() == getPanelQueryEingangsrechnung(false)) {
				Object key = getPanelQueryEingangsrechnung(true).getSelectedId();
				holeEingangsrechnungDto(key);
				getInternalFrame().setKeyWasForLockMe(key + "");
				// nur wechseln wenns auch einen gibt
				if (key != null) {
					setSelectedComponent(getPanelEingangsrechnungKopfdaten(true));
					getPanelEingangsrechnungKopfdaten(true).eventYouAreSelected(false);
				}
			}
			if (e.getSource() == panelQueryFLRBestellungauswahl) {
				Integer iIDBestellungChoosen = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				if (iIDBestellungChoosen != null) {
					Integer eingangsrechnungIId = DelegateFactory.getInstance().getBestellungDelegate()
							.erzeugeEingangsrechnungAusBestellung(iIDBestellungChoosen, null);
					getInternalFrame().setKeyWasForLockMe(eingangsrechnungIId + "");
					PanelQuery pQHelper = getPanelQueryEingangsrechnung(true);
					holeEingangsrechnungDto(eingangsrechnungIId);
					pQHelper.setSelectedId(eingangsrechnungIId);
					PanelEingangsrechnungKopfdaten pEKHelper = getPanelEingangsrechnungKopfdaten(false);
					setSelectedComponent(pEKHelper);
				}
			}

			if (e.getSource() == panelQueryFLREingangsrechnungenZuErledigen) {
				Object[] rechnungIIds = panelQueryFLREingangsrechnungenZuErledigen.getSelectedIds();

				for (int i = 0; i < rechnungIIds.length; i++) {

					DelegateFactory.getInstance().getEingangsrechnungDelegate()
							.manuellErledigen((Integer) rechnungIIds[i]);

				}

				if (panelQueryFLREingangsrechnungenZuErledigen.getDialog() != null) {
					panelQueryFLREingangsrechnungenZuErledigen.getDialog().setVisible(false);
				}
				getPanelQueryEingangsrechnung(false).eventYouAreSelected(false);

			}

			if (e.getSource() == panelQueryWEsEinerRechnungsadresse) {
				if (dDialogWEsEinerRechnungsadresse != null) {
					dDialogWEsEinerRechnungsadresse.setVisible(false);
				}
				Object[] ids = panelQueryWEsEinerRechnungsadresse.getSelectedIds();
				if (ids != null && ids.length > 0) {

					String krit = "(";

					for (int i = 0; i < ids.length; i++) {

						Integer wareneingangIId = (Integer) ids[i];

						WareneingangDto weDto = DelegateFactory.getInstance().getWareneingangDelegate()
								.wareneingangFindByPrimaryKey(wareneingangIId);
						weDto.setEingangsrechnungIId(getEingangsrechnungDto().getIId());
						DelegateFactory.getInstance().getWareneingangDelegate().updateWareneingang(weDto);

						krit += ids[i];

						if (i < ids.length - 1) {
							krit += ",";
						}
					}
					krit += ")";

					FilterKriterium[] kriterien = new FilterKriterium[1];
					kriterien[0] = new FilterKriterium("wareneingang_i_id", true, krit + "",
							FilterKriterium.OPERATOR_IN, false);

					panelQueryFLRWEPsMehrererWEs = new PanelQueryFLR(null, kriterien,
							QueryParameters.UC_ID_WEP_EINER_RECHNUNGSADRESSE, null, getInternalFrame(),
							LPMain.getTextRespectUISPr("er.weseinerrechnungadresse.zuordnen.wep.preiseingeben"));

					panelQueryFLRWEPsMehrererWEs.addButtonAuswahlBestaetigen(null, "/com/lp/client/res/door.png",
							LPMain.getTextRespectUISPr("er.weseinerrechnungadresse.zuordnen.fertig"));

					panelQueryFLRWEPsMehrererWEs.createAndSaveAndShowButton("/com/lp/client/res/text_ok16x16.png",
							LPMain.getTextRespectUISPr("er.weseinerrechnungadresse.zuordnen.wep.preiserfasst"),
							MENUE_ACTION_WEP_PREIS_ERFASST, RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF);
					panelQueryFLRWEPsMehrererWEs.createAndSaveAndShowButton("/com/lp/client/res/document_check.png",
							LPMain.getTextRespectUISPr(
									"er.weseinerrechnungadresse.zuordnen.wep.preiserfasst.wieerstezeile"),
							MENUE_ACTION_WEP_PREIS_ERFASST_WIE_ERSTE_MARKIERTE_ZEILE,
							RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF);
					panelQueryFLRWEPsMehrererWEs.getToolBar().getToolsPanelCenter()
							.add(lblweDiffPanelQueryFLRWEPsMehrererWEs);
					panelQueryFLRWEPsMehrererWEs.createAndSaveAndShowButton("/com/lp/client/res/edit.png",
							LPMain.getTextRespectUISPr("er.weseinerrechnungadresse.zuordnen.wep.preiseingeben"),
							MENUE_ACTION_WEP_PREIS_EINGEBEN,
							KeyStroke.getKeyStroke('U', java.awt.event.InputEvent.CTRL_MASK),
							RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF);
					panelQueryFLRWEPsMehrererWEs.setMultipleRowSelectionEnabled(true);
					new DialogQuery(panelQueryFLRWEPsMehrererWEs);

				}
			}

			if (e.getSource() == panelQueryWEsEinerRechnungsadresse) {
				panelQueryWareneingaenge.eventYouAreSelected(false);
			}

			if (e.getSource() == panelQueryFLRWEPsMehrererWEs) {
				if (panelQueryFLRWEPsMehrererWEs.getDialog() != null) {
					panelQueryFLRWEPsMehrererWEs.getDialog().setVisible(false);
				}
				panelQueryWareneingaenge.eventYouAreSelected(false);
			}

			if (e.getSource() == panelQueryFLRLieferant) {
				Integer lieferantIId = (Integer) panelQueryFLRLieferant.getSelectedId();
				erstelleEingangsrechnungausQRCode(lieferantIId, bdBetragAusQrCode, waehrungAusQRCode, null, null);
			}

		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == getPanelQueryEingangsrechnung(false)) {
				Object key = getPanelQueryEingangsrechnung(true).getSelectedId();
				if (key == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_EINGANGSRECHNUNGEN, false);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_EINGANGSRECHNUNGEN, true);
				}
				holeEingangsrechnungDto(key);
				getPanelQueryEingangsrechnung(true).updateButtons();
			} else if (e.getSource() == getPanelQueryAuftrag(false)) {
				// hole key
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelAuftrag(true).setKeyWhenDetailPanel(key);
				getPanelAuftrag(true).eventYouAreSelected(false);
				getPanelQueryAuftrag(true).updateButtons();
			} else if (e.getSource() == getPanelQueryInserat(false)) {
				// hole key
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelInserat(true).setKeyWhenDetailPanel(key);
				getPanelInserat(true).eventYouAreSelected(false);
				getPanelQueryInserat(true).updateButtons();
			} else if (e.getSource() == getPanelQueryKontierung(false)) {
				// hole key
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelKontierung(true).setKeyWhenDetailPanel(key);
				getPanelKontierung(true).eventYouAreSelected(false);
				getPanelQueryKontierung(true).updateButtons();
			} else if (e.getSource() == getPanelQueryZahlung(false)) {
				// hole key
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				getPanelZahlung(true).setKeyWhenDetailPanel(key);
				getPanelZahlung(true).eventYouAreSelected(false);
				getPanelQueryZahlung(true).updateButtons();
			} else if (e.getSource() == getPanelQueryWareneingaenge(false)) {
				getPanelQueryWareneingaenge(true).updateButtons();

				// PJ 16469

				lblweDiff.setText("");

				WareneingangDto[] weDtos = DelegateFactory.getInstance().getWareneingangDelegate()
						.wareneingangFindByEingangsrechnungIId(getEingangsrechnungDto().getIId());
				BigDecimal wertWE = new BigDecimal(0);
				for (int i = 0; i < weDtos.length; i++) {
					wertWE = wertWE.add(DelegateFactory.getInstance().getWareneingangDelegate()
							.berechneWertDesWareneingangsInBestellungswaehrung(weDtos[i].getIId()));

				}
				BigDecimal nettowertER = getNettowertER();

				lblweDiff.setText(LPMain.getMessageTextRespectUISPr("er.nettowert",
						Helper.formatZahl(nettowertER, 2, LPMain.getTheClient().getLocUi()),
						Helper.formatZahl(wertWE, 2, LPMain.getTheClient().getLocUi())));

				if (nettowertER.doubleValue() != Helper.rundeKaufmaennisch(wertWE, 2).doubleValue()) {
					lblweDiff.setForeground(Color.RED);
				} else {
					lblweDiff.setForeground(Color.BLACK);
				}

			} else if (e.getSource() == panelQueryWEsEinerRechnungsadresse) {
				if (dDialogWEsEinerRechnungsadresse != null) {
					dDialogWEsEinerRechnungsadresse.aktualisiereWerte();
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == getPanelAuftrag(true)) {
				getPanelQueryAuftrag(true).updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == getPanelInserat(true)) {
				getPanelQueryInserat(true).updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelKontierung) {
				getPanelQueryKontierung(true).updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == getPanelZahlung(false)) {
				getPanelQueryZahlung(true).updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() instanceof PanelQueryFLRGoto) {
				Integer iIdEingangsrechnung = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				this.setSelectedIndex(1);
				getPanelQueryEingangsrechnung(true).setSelectedId(iIdEingangsrechnung);
				getPanelQueryEingangsrechnung(true).eventYouAreSelected(false);
				getPanelEingangsrechnungKopfdaten(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == getPanelQueryEingangsrechnung(false)) {
				if (getPanelQueryEingangsrechnung(true).getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				getPanelEingangsrechnungKopfdaten(true).eventActionNew(null, true, false);
				setSelectedComponent(getPanelEingangsrechnungKopfdaten(true));
			} else if (e.getSource() == getPanelQueryAuftrag(false)) {
				getPanelAuftrag(true).eventActionNew(e, true, false);
				getPanelAuftrag(true).eventYouAreSelected(false);
				// locknew: 2 den Panels den richtigen lockstatus geben

				LockStateValue lockstateValue = new LockStateValue(null, null, PanelBasis.LOCK_FOR_NEW);
				getPanelAuftrag(true).updateButtons(lockstateValue);
				getPanelQueryAuftrag(true).updateButtons(lockstateValue);
			} else if (e.getSource() == getPanelQueryWareneingaenge(false)) {

				dDialogWEsEinerRechnungsadresse = new DialogWEsEinerRechnungsadresse(getEingangsrechnungDto(),
						getInternalFrame(), this);

				panelQueryWEsEinerRechnungsadresse = dDialogWEsEinerRechnungsadresse.getPanelQueryWEs();

				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(dDialogWEsEinerRechnungsadresse);
				dDialogWEsEinerRechnungsadresse.setVisible(true);
				panelQueryWareneingaenge.updateButtons(new LockStateValue(PanelBasis.LOCK_IS_NOT_LOCKED));

			} else if (e.getSource() == getPanelQueryInserat(false)) {
				getPanelInserat(true).eventActionNew(e, true, false);
				getPanelInserat(true).eventYouAreSelected(false);
				// locknew: 2 den Panels den richtigen lockstatus geben

				LockStateValue lockstateValue = new LockStateValue(null, null, PanelBasis.LOCK_FOR_NEW);
				getPanelInserat(true).updateButtons(lockstateValue);
				getPanelQueryInserat(true).updateButtons();
			} else if (e.getSource() == getPanelQueryKontierung(false)) {
				EingangsrechnungDto reDto = getEingangsrechnungDto();

				if (reDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_TEILBEZAHLT)
						|| reDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_ERLEDIGT)) {
					boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getTextRespectUISPr("er.eingangsrechnungistbereitserledigt.wirklichaendern"));

					if (b == false) {
						return;
					}
				}

				getPanelKontierung(true).eventActionNew(e, true, false);

				if (reDto.getTGedruckt() == null) {

					getPanelKontierung(true).eventYouAreSelected(false);

					LockStateValue lockstateValue = new LockStateValue(null, null, PanelBasis.LOCK_FOR_NEW);
					getPanelKontierung(true).updateButtons(lockstateValue);
					getPanelQueryAuftrag(true).updateButtons();
				}

			} else if (e.getSource() == getPanelQueryZahlung(false,true)) {
				EingangsrechnungDto reDto = getEingangsrechnungDto();
				if (reDto.getStatusCNr().equalsIgnoreCase(EingangsrechnungFac.STATUS_STORNIERT)) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("er.hint.eingangsrechnungstorniertkeinezahlungen"));
					gotoAuswahl();
					return;
				}
				if (reDto.getStatusCNr().equalsIgnoreCase(EingangsrechnungFac.STATUS_ERLEDIGT)) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("er.hint.eingangsrechnungerledigtkeinezahlungen"));
					gotoAuswahl();
					return;
				}
				if (reDto.getEingangsrechnungartCNr()
						.equalsIgnoreCase(EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT)) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("er.hint.eingangsgutschriftkeinezahlung"));
					gotoAuswahl();
					return;
				}
				try {
					getPanelZahlung(true).eventActionNew(e, true, false);
					getPanelZahlung(true).eventYouAreSelected(false);
					LockStateValue lockstateValue = new LockStateValue(null, null, PanelBasis.LOCK_FOR_NEW);
					getPanelZahlung(true).updateButtons(lockstateValue);
				} catch (Exception ex) {
					if (ex.getCause() instanceof EntityExistsException) {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"),
								LPMain.getTextRespectUISPr("finanz.error.ergesperrt"));
						System.out.println("");
					} else {
						throw ex;
					}
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == getPanelEingangsrechnungKopfdaten(false)) {
				// nix
			} else if (e.getSource() == getPanelAuftrag(false)) {
				getPanelSplitAuftrag(true).eventYouAreSelected(false);
				reloadEingangsrechnungDto();
			} else if (e.getSource() == getPanelInserat(false)) {
				getPanelSplitInserat(true).eventYouAreSelected(false);
				reloadEingangsrechnungDto();
			} else if (e.getSource() == panelKontierung) {
				// getPanelSplitKontierung(true).eventYouAreSelected(false);
				panelSplitKontierung.eventYouAreSelected(false);
				reloadEingangsrechnungDto();
			} else if (e.getSource() == getPanelZahlung(false)) {
				getPanelSplitZahlung(true).eventYouAreSelected(false);
				reloadEingangsrechnungDto();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == getPanelAuftrag(false)) {
				Object key = getPanelAuftrag(true).getKeyWhenDetailPanel();
				getPanelSplitAuftrag(true).eventYouAreSelected(false);
				getPanelQueryAuftrag(true).setSelectedId(key);
				getPanelSplitAuftrag(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelInserat(false)) {
				Object key = getPanelInserat(true).getKeyWhenDetailPanel();
				getPanelSplitInserat(true).eventYouAreSelected(false);
				getPanelQueryInserat(true).setSelectedId(key);
				getPanelSplitInserat(true).eventYouAreSelected(false);
			} else if (e.getSource() == panelKontierung) {
				Object key = getPanelKontierung(true).getKeyWhenDetailPanel();
				// panelSplitKontierung.eventYouAreSelected(false);
				// getPanelQueryKontierung(true).setSelectedId(key);
				// panelSplitKontierung.eventYouAreSelected(false);
				getPanelQueryKontierung(true).setSelectedId(key);
				panelSplitKontierung.eventYouAreSelected(false);
			} else if (e.getSource() == getPanelZahlung(false)) {
				Object key = getPanelZahlung(true).getKeyWhenDetailPanel();
				getPanelSplitZahlung(true).eventYouAreSelected(false);
				getPanelQueryZahlung(true).setSelectedId(key);
				getPanelSplitZahlung(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelEingangsrechnungKopfdaten(false)) {
				getPanelQueryEingangsrechnung(true).clearDirektFilter();
				Object key = getPanelEingangsrechnungKopfdaten(true).getKeyWhenDetailPanel();
				getPanelQueryEingangsrechnung(true).eventYouAreSelected(false);
				getPanelQueryEingangsrechnung(true).setSelectedId(key);
				getPanelQueryEingangsrechnung(true).eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == getPanelEingangsrechnungKopfdaten(false)) {
				setEingangsrechnungDto(null);
				panelQueryEingangsrechnung.eventYouAreSelected(false);
				this.setSelectedComponent(getPanelQueryEingangsrechnung(true));
			} else if (e.getSource() == getPanelAuftrag(false)) {
				setKeyWasForLockMe();
				// selectafterdelete: wenn der key null ist, den logisch
				// naechsten selektieren
				if (getPanelAuftrag(true).getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryAuftrag(true).getId2SelectAfterDelete();
					getPanelQueryAuftrag(true).setSelectedId(oNaechster);
				}
				getPanelSplitAuftrag(true).eventYouAreSelected(false);
			} else if (e.getSource() == getPanelInserat(false)) {
				setKeyWasForLockMe();
				// selectafterdelete: wenn der key null ist, den logisch
				// naechsten selektieren
				if (getPanelInserat(true).getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryInserat(true).getId2SelectAfterDelete();
					getPanelQueryInserat(true).setSelectedId(oNaechster);
				}
				getPanelSplitInserat(true).eventYouAreSelected(false);
			} else if (e.getSource() == panelKontierung) {
				setKeyWasForLockMe();
				if (getPanelKontierung(true).getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryKontierung(true).getId2SelectAfterDelete();
					getPanelQueryKontierung(true).setSelectedId(oNaechster);
				}
				panelSplitKontierung.eventYouAreSelected(false);
			} else if (e.getSource() == getPanelZahlung(false)) {
				setKeyWasForLockMe();
				if (getPanelZahlung(true).getKeyWhenDetailPanel() == null) {
					Object oNaechster = getPanelQueryZahlung(true).getId2SelectAfterDelete();
					getPanelQueryZahlung(true).setSelectedId(oNaechster);
				}
				getPanelSplitZahlung(true).eventYouAreSelected(false);
			}

		}
		// der OK Button in einem PanelDialog wurde gedrueckt
		else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {
			if (e.getSource() == getPanelKriterienUebersicht()) {
				// die Kriterien fuer PanelTabelle abholen
				FilterKriterium[] aAlleKriterien = getPanelKriterienUebersicht().getAlleFilterKriterien();

				// die Kriterien dem PanelTabelle setzen
				getPanelUebersicht().setDefaultFilter(aAlleKriterien);

				setComponentAt(iDX_UEBERSICHT, getPanelUebersicht());
				// die Uebersicht aktualisieren @todo redundant, wenn man dann
				// ohnehin wechselt PJ 5228
				getPanelUebersicht().eventYouAreSelected(false);
				getPanelUebersicht().updateButtons(new LockStateValue(null, null, PanelBasis.LOCK_IS_NOT_LOCKED));

				// man steht auf alle Faelle auf der Uebersicht
				// setSelectedComponent(getPanelUebersicht());
			}
		}
		if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {

		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {

		} else if (e.getID() == ItemChangedEvent.ACTION_TABLE_SELECTION_CHANGED) {
			if (panelQueryFLRWEPsMehrererWEs != null && e.getSource() == panelQueryFLRWEPsMehrererWEs.getTable()) {
				lblweDiffPanelQueryFLRWEPsMehrererWEs.setText("");

				WareneingangDto[] weDtos = DelegateFactory.getInstance().getWareneingangDelegate()
						.wareneingangFindByEingangsrechnungIId(getEingangsrechnungDto().getIId());
				BigDecimal wertAllerWEPosMitPreisErfasst = new BigDecimal(0);
				for (int i = 0; i < weDtos.length; i++) {

					WareneingangspositionDto weposDtos[] = DelegateFactory.getInstance().getWareneingangDelegate()
							.wareneingangspositionFindByWareneingangIId(weDtos[i].getIId());

					EinstandspreiseEinesWareneingangsDto preiseDto = DelegateFactory.getInstance()
							.getWareneingangDelegate()
							.getBerechnetenEinstandspreisEinerWareneingangsposition(weDtos[i].getIId());

					for (int j = 0; j < weposDtos.length; j++) {
						if (weposDtos[j].getBPreiseErfasst()) {

							if (preiseDto.getHmEinstandpreiseAllerPositionen().containsKey(weposDtos[j].getIId())) {

								BigDecimal bdEinstandspreis = preiseDto.getHmEinstandpreiseAllerPositionen()
										.get(weposDtos[j].getIId());

								wertAllerWEPosMitPreisErfasst = wertAllerWEPosMitPreisErfasst
										.add(bdEinstandspreis.multiply(weposDtos[j].getNGeliefertemenge()));

							}

						}

					}

				}
				BigDecimal nettowertER = getNettowertER();

				lblweDiffPanelQueryFLRWEPsMehrererWEs
						.setText(LPMain.getMessageTextRespectUISPr("er.nettowert.wepmitpreiserfasst",
								Helper.formatZahl(nettowertER, 2, LPMain.getTheClient().getLocUi()),
								Helper.formatZahl(wertAllerWEPosMitPreisErfasst, 2, LPMain.getTheClient().getLocUi())));

				if (nettowertER.doubleValue() != Helper.rundeKaufmaennisch(wertAllerWEPosMitPreisErfasst, 2)
						.doubleValue()) {
					lblweDiffPanelQueryFLRWEPsMehrererWEs.setForeground(Color.RED);
				} else {
					lblweDiffPanelQueryFLRWEPsMehrererWEs.setForeground(Color.BLACK);
				}
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {

		}

		else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (sAspectInfo.endsWith(MY_OWN_NEW_AUS_BESTELLUNG)) {
				// Der Benutzer muss eine Bestellung auswaehlen
				dialogQueryBestellungFromListe(null);
			} else if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				einfuegenHV();
			} else if (sAspectInfo.equals(GOTO_WE)) {
				if (e.getSource() == panelQueryWareneingaenge) {

					Object key = panelQueryWareneingaenge.getSelectedId();

					if (key != null) {
						wbuGotoWe.setOKey((Integer) key);
						wbuGotoWe.actionPerformed(new ActionEvent(wbuGotoWe, 0, WrapperGotoButton.ACTION_GOTO));
					}

				}
			} else if (e.getSource() == panelQueryWareneingaenge
					&& sAspectInfo.endsWith(MENUE_ACTION_ZUORDNUNG_ER_LOESCHEN)) {
				Integer wareneingangIId = (Integer) panelQueryWareneingaenge.getSelectedId();

				if (wareneingangIId != null) {
					WareneingangDto weDto = DelegateFactory.getInstance().getWareneingangDelegate()
							.wareneingangFindByPrimaryKey(wareneingangIId);
					weDto.setEingangsrechnungIId(null);
					DelegateFactory.getInstance().getWareneingangDelegate().updateWareneingang(weDto);
				}
				panelQueryWareneingaenge.eventYouAreSelected(false);

			} else if (e.getSource() == panelQueryFLRWEPsMehrererWEs
					&& (sAspectInfo.endsWith(MENUE_ACTION_WEP_PREIS_ERFASST)
							|| sAspectInfo.endsWith(MENUE_ACTION_WEP_PREIS_EINGEBEN))) {
				Integer wareneingangpositionIId = (Integer) panelQueryFLRWEPsMehrererWEs.getSelectedId();

				if (wareneingangpositionIId != null) {

					WareneingangspositionDto wepDto = DelegateFactory.getInstance().getWareneingangDelegate()
							.wareneingangspositionFindByPrimaryKey(wareneingangpositionIId);

					if (sAspectInfo.endsWith(MENUE_ACTION_WEP_PREIS_ERFASST)) {
						wepDto.setBPreiseErfasst(!wepDto.getBPreiseErfasst());
					} else {

						DialogEingabeWEPreise d = new DialogEingabeWEPreise(getEingangsrechnungDto(),
								wepDto.getNGelieferterpreis(), wepDto.getNGeliefertemenge(), getInternalFrame());
						d.setTitle(LPMain.getTextRespectUISPr("er.weseinerrechnungadresse.zuordnen.wep.preiseingeben"));
						LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);

						d.setVisible(true);

						if (d.bdPreis == null) {
							return;
						}
						wepDto.setNGelieferterpreis(d.bdPreis);
					}

					DelegateFactory.getInstance().getWareneingangDelegate().updateWareneingangsposition(wepDto);
				}
				panelQueryFLRWEPsMehrererWEs.eventYouAreSelected(false);
				panelQueryFLRWEPsMehrererWEs.setSelectedId(wareneingangpositionIId);

			} else if (e.getSource() == panelQueryFLRWEPsMehrererWEs
					&& (sAspectInfo.endsWith(MENUE_ACTION_WEP_PREIS_ERFASST_WIE_ERSTE_MARKIERTE_ZEILE))) {
				Object[] wareneingangpositionIId = panelQueryFLRWEPsMehrererWEs.getSelectedIds();

				if (wareneingangpositionIId != null && wareneingangpositionIId.length > 1) {

					WareneingangspositionDto wepDtoErste = DelegateFactory.getInstance().getWareneingangDelegate()
							.wareneingangspositionFindByPrimaryKey((Integer) wareneingangpositionIId[0]);

					// SP5280

					Boolean neuerWert = !wepDtoErste.getBPreiseErfasst().booleanValue();

					wepDtoErste.setBPreiseErfasst(neuerWert);
					DelegateFactory.getInstance().getWareneingangDelegate().updateWareneingangsposition(wepDtoErste);

					for (int i = 1; i < wareneingangpositionIId.length; i++) {
						WareneingangspositionDto wepDto = DelegateFactory.getInstance().getWareneingangDelegate()
								.wareneingangspositionFindByPrimaryKey((Integer) wareneingangpositionIId[i]);

						wepDto.setBPreiseErfasst(neuerWert);

						DelegateFactory.getInstance().getWareneingangDelegate().updateWareneingangsposition(wepDto);
					}

				}
				panelQueryFLRWEPsMehrererWEs.eventYouAreSelected(false);
				panelQueryFLRWEPsMehrererWEs.setSelectedId(panelQueryFLRWEPsMehrererWEs.getSelectedId());

			} else if (e.getSource() == panelQueryEingangsrechnung && (sAspectInfo.endsWith(MENU_ACTION_ER_AUS_QR))) {

				bdBetragAusQrCode = null;
				waehrungAusQRCode = null;

				DialogQRCodeLesen qr = new DialogQRCodeLesen(getInternalFrame());
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(qr);

				qr.setVisible(true);

				if (qr.getBarCode() != null && qr.getBarCode().length() > 0) {
					String barcode = qr.getBarCode();

					if (barcode.endsWith(">")) {
						// PJ21916

						// Bank anhand Teilnehmernummer suchen

						String waehrung = null;

						boolean bMitBetrag = true;
						if (barcode.substring(0, 2).equals("01")) {
							waehrung = "CHF";
						} else if (barcode.substring(0, 2).equals("04")) {
							waehrung = "CHF";
							bMitBetrag = false;
						} else if (barcode.substring(0, 2).equals("21")) {
							waehrung = "EUR";
						} else if (barcode.substring(0, 2).equals("31")) {
							waehrung = "EUR";
							bMitBetrag = false;
						} else {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("lp.error.esr.waehrung"));
							return;
						}
						barcode = barcode.substring(2);

						// Die naechsten 10 Stellen sind der Betrag
						BigDecimal nBetrag = BigDecimal.ZERO;

						if (bMitBetrag == true) {
							String betrag = barcode.substring(0, 10);
							nBetrag = new BigDecimal(betrag);
							nBetrag = nBetrag.movePointLeft(2);
							barcode = barcode.substring(12);
						} else {
							barcode = barcode.substring(2);
						}

						// Die naechsten Stellen bis zum + sind die Kontonummer und die Referenznummer

						int i = barcode.indexOf("+");

						String kontoUndReferenz = barcode.substring(0, i);

						String esrTeilnehmer = null;
						String referenz = null;
						if (bMitBetrag == true) {
							esrTeilnehmer = kontoUndReferenz.substring(0, 6);
							referenz = kontoUndReferenz.substring(6, kontoUndReferenz.length());
						} else {
							esrTeilnehmer = kontoUndReferenz.substring(0, 6);
							referenz = kontoUndReferenz.substring(6, kontoUndReferenz.length());
						}

						barcode = barcode.substring(i + 2);
						String konto = barcode.substring(0, barcode.length() - 1);

						// Anhand ESR-Nummer und Konto Bankverbindung suchen

						List<PartnerbankDto> bank = DelegateFactory.getInstance().getPartnerbankDelegate()
								.partnerbankFindByESRUndKontonummer(esrTeilnehmer, konto);

						if (bank != null && bank.size() > 0) {

							PartnerbankDto pbDto = bank.get(0);

							LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
									.lieferantFindByiIdPartnercNrMandantOhneExc(pbDto.getPartnerIId(),
											LPMain.getTheClient().getMandant());

							if (lfDto != null) {

								erstelleEingangsrechnungausQRCode(lfDto.getIId(), nBetrag, waehrung, referenz,
										referenz);

							} else {

								PartnerDto pDto = DelegateFactory.getInstance().getPartnerDelegate()
										.partnerFindByPrimaryKey(pbDto.getPartnerIId());

								DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getMessageTextRespectUISPr("lp.error.esr.partnerkeinlieferant",
												pDto.formatFixName1Name2()));
							}

						} else {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getMessageTextRespectUISPr("lp.error.esr.keine.bankverbindung",
											esrTeilnehmer, konto));
							return;
						}

					} else {

						int iPositionTyp = 0;
						int iPositionIBAN = 6;

						int iPositionLF_Name = 10;
						int iPositionLF_Strasse = 12;
						int iPositionLF_Hausnummer = 14;
						int iPositionLF_PLZ = 16;
						int iPositionLF_ORT = 18;
						int iPositionLF_Land = 20;

						int iPositionBetrag = 36;
						int iPositionWaehrung = 38;

						int iPositionLieferantenrechnungsnummer = -1;
						int iPositionZahlungsreferenz = -1;

						String[] teile = barcode.split("\n");

						if (teile.length == 30 || teile.length == 29) {
							// PayEye
							iPositionIBAN = 3;

							iPositionLF_Name = 5;
							iPositionLF_Strasse = 6;
							iPositionLF_Hausnummer = 7;
							iPositionLF_PLZ = 8;
							iPositionLF_ORT = 9;
							iPositionLF_Land = 10;

							iPositionBetrag = 18;
							iPositionWaehrung = 19;
							iPositionZahlungsreferenz = 28;
							if (teile.length == 30) {
								iPositionLieferantenrechnungsnummer = 29;
							}

						}

						if (teile.length > iPositionWaehrung) {

							String iban = teile[iPositionIBAN];

							String typ = teile[iPositionTyp];

							String lieferantenrechnungnummer = null;
							if (iPositionLieferantenrechnungsnummer > 0) {
								lieferantenrechnungnummer = teile[iPositionLieferantenrechnungsnummer];
							}

							String zahlungsreferenz = null;
							if (iPositionZahlungsreferenz > 0) {
								zahlungsreferenz = teile[iPositionZahlungsreferenz];
							}

							if (typ.equals("SPC")) {

								String betrag = teile[iPositionBetrag];
								if (betrag != null) {
									try {
										bdBetragAusQrCode = new BigDecimal(betrag);
									} catch (Exception e1) {
										DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), LPMain
												.getMessageTextRespectUISPr("er.erausqr.scan.betragungueltig", betrag));
										return;
									}
								}
								waehrungAusQRCode = teile[iPositionWaehrung];

								WaehrungDto waehrungDto = DelegateFactory.getInstance().getLocaleDelegate()
										.waehrungFindByPrimaryKeyWithNull(waehrungAusQRCode);

								if (waehrungDto != null) {
									ArrayList<Integer> alLieferanten = DelegateFactory.getInstance()
											.getLieferantDelegate().lieferantFindByIBAN(iban);

									if (alLieferanten.size() == 0) {

										String name = teile[iPositionLF_Name];
										String strasse = teile[iPositionLF_Strasse] + " "
												+ teile[iPositionLF_Hausnummer];

										String plz = teile[iPositionLF_PLZ];
										String ort = teile[iPositionLF_ORT];

										String land = teile[iPositionLF_Land];

										LandDto landDto = DelegateFactory.getInstance().getSystemDelegate()
												.landFindByLkz(land);

										if (landDto != null) {
											// Lieferant anlegen

											Integer lieferantIId = DelegateFactory.getInstance().getLieferantDelegate()
													.createLieferantFuerEingangsrechnungAusQRCode(
															name + "0123456789012345678901234567890", land, plz, ort,
															strasse, waehrungAusQRCode, iban);

											erstelleEingangsrechnungausQRCode(lieferantIId, bdBetragAusQrCode,
													waehrungAusQRCode, lieferantenrechnungnummer, zahlungsreferenz);
										} else {
											DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
													LPMain.getMessageTextRespectUISPr("er.landausqr.scan.nichtgefunden",
															land));
										}

									} else if (alLieferanten.size() == 1) {
										erstelleEingangsrechnungausQRCode(alLieferanten.get(0), bdBetragAusQrCode,
												waehrungAusQRCode, lieferantenrechnungnummer, zahlungsreferenz);
									} else if (alLieferanten.size() > 0) {
										String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
												.createButtonArray(false, false);

										FilterKriterium[] fk = InseratFilterFactory.getInstance()
												.createFKLieferantenOhneBestellungen(alLieferanten);
										panelQueryFLRLieferant = new PanelQueryFLRGoto(null, fk,
												QueryParameters.UC_ID_LIEFERANTEN, aWhichButtonIUse, getInternalFrame(),
												LocaleFac.BELEGART_LIEFERANT,
												LPMain.getTextRespectUISPr("title.lieferantenauswahlliste"), null);
										new DialogQuery(panelQueryFLRLieferant);
									}
								} else {
									DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getMessageTextRespectUISPr("er.erausqr.scan.waehrungnichtgefunden",
													waehrungAusQRCode));

								}
							} else {
								DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("er.erausqr.scan.falschertyp"));
							}

						}
					}
				}

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			copyHV();
		}
	}

	/**
	 * @return
	 */
	public BigDecimal getNettowertER() throws Throwable {
		if (isIGErwerbOrReversecharge()) {
			return Helper.rundeKaufmaennisch(getEingangsrechnungDto().getNBetragfw(), 2);
		}

		// SP8448 Wenn Mehrfach/Splittbuchung und noch keine Eintrag in Kontierung, dann
		// kommt der MWST-Betrag aus dem Lieferanten

		BigDecimal ustbetrag = getEingangsrechnungDto().getNUstBetragfw();
		EingangsrechnungKontierungDto[] kontierungDtos = DelegateFactory.getInstance().getEingangsrechnungDelegate()
				.eingangsrechnungKontierungFindByEingangsrechnungIId(getEingangsrechnungDto().getIId());

		if (getEingangsrechnungDto().getMwstsatzIId() == null && kontierungDtos.length == 0) {

			LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
					.lieferantFindByPrimaryKey(getEingangsrechnungDto().getLieferantIId());

			MwstsatzDto mwst = DelegateFactory.getInstance().getMandantDelegate().mwstsatzFindZuDatum(
					lieferantDto.getMwstsatzbezIId(),
					new Timestamp(getEingangsrechnungDto().getDBelegdatum().getTime()));
			ustbetrag = Helper.getMehrwertsteuerBetrag(getEingangsrechnungDto().getNBetragfw(), mwst.getFMwstsatz());

		} else {
			if (kontierungDtos.length > 0) {

				ustbetrag = BigDecimal.ZERO;

				for (int i = 0; i < kontierungDtos.length; i++) {

					ustbetrag = ustbetrag.add(kontierungDtos[i].getNBetragUst());
				}
			}
		}

		return Helper.rundeKaufmaennisch(getEingangsrechnungDto().getNBetragfw().subtract(ustbetrag), 2);
	}

	private void dialogQueryBestellungFromListe(ActionEvent e) throws Throwable {
		// Filterkriterien fuer Bestellungen einstellen
		FilterKriterium fk[] = new FilterKriterium[5];
		// Nur gelieferte und bestaetigte Bestellungen sollen angezeigt werden.
		FilterKriterium fkAbgerufen = new FilterKriterium(BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, true,
				"'" + BestellungFac.BESTELLSTATUS_ABGERUFEN + "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

		FilterKriterium fkAngelegt = new FilterKriterium(BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, true,
				"'" + BestellungFac.BESTELLSTATUS_ANGELEGT + "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

		FilterKriterium fkErledigt = new FilterKriterium(BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, true,
				"'" + BestellungFac.BESTELLSTATUS_ERLEDIGT + "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

		FilterKriterium fkOffen = new FilterKriterium(BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, true,
				"'" + BestellungFac.BESTELLSTATUS_OFFEN + "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

		FilterKriterium fkStorniert = new FilterKriterium(BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, true,
				"'" + BestellungFac.BESTELLSTATUS_STORNIERT + "'", FilterKriterium.OPERATOR_NOT_EQUAL, false);

		fk[0] = fkAbgerufen;
		fk[1] = fkAngelegt;
		fk[2] = fkErledigt;
		fk[3] = fkOffen;
		fk[4] = fkStorniert;
		// erstellen und anzeigen des Bestellungspanels
		panelQueryFLRBestellungauswahl = BestellungFilterFactory.getInstance()
				.createPanelFLRBestellung(getInternalFrame(), true, false, fk, null);
		new DialogQuery(panelQueryFLRBestellungauswahl);

	}

	/**
	 * Dto updaten, va.a um den status zu aktualisieren laden
	 * 
	 * @throws Throwable
	 */
	protected void refreshEingangsrechnungDto() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			holeEingangsrechnungDto(getEingangsrechnungDto().getIId());
		}
	}

	/**
	 * hole EingangsrechnungDto.
	 * 
	 * @param key Object
	 * @throws Throwable
	 */
	private void holeEingangsrechnungDto(Object key) throws Throwable {
		if (key != null) {
			setEingangsrechnungDto(DelegateFactory.getInstance().getEingangsrechnungDelegate()
					.eingangsrechnungFindByPrimaryKey((Integer) key));

			LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
					.lieferantFindByPrimaryKey(getEingangsrechnungDto().getLieferantIId());
			setLieferantDto(lieferantDto);

			if (getEingangsrechnungDto().getTGedruckt() != null) {
				getPanelQueryEingangsrechnung(true)
						.setStatusbarSpalte4(LPMain.getTextRespectUISPr("er.gedruckt") + ":" + Helper.formatDatum(
								getEingangsrechnungDto().getTGedruckt(), LPMain.getTheClient().getLocUi()));
			} else {
				getPanelQueryEingangsrechnung(true).setStatusbarSpalte4("");
			}

			getInternalFrame().setKeyWasForLockMe(key.toString());
			if (getPanelEingangsrechnungKopfdaten(false) != null) {
				getPanelEingangsrechnungKopfdaten(true).setKeyWhenDetailPanel(key);
			}
		}
	}

	protected void reloadEingangsrechnungDto() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			holeEingangsrechnungDto(getEingangsrechnungDto().getIId());
		}
	}

	public String getRechtModulweit() {
		return sRechtModulweit;
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int index = this.getSelectedIndex();

		getInternalFrame().setRechtModulweit(sRechtModulweit);

		if (index == IDX_EINGANGSRECHNUNGEN) {
			getPanelQueryEingangsrechnung(true).eventYouAreSelected(false);
		} else if (index == iDX_KOPFDATEN) {
			if (this.getEingangsrechnungDto() != null) {
				getPanelEingangsrechnungKopfdaten(true).setKeyWhenDetailPanel(this.getEingangsrechnungDto().getIId());

				if (this.getEingangsrechnungDto().getIId() != null && bDarfKontieren == false) {
					// PJ20436
					if (this.getEingangsrechnungDto().getKontoIId() != null) {
						getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
					} else {
						EingangsrechnungKontierungDto[] erKontierungDtos = DelegateFactory.getInstance()
								.getEingangsrechnungDelegate().eingangsrechnungKontierungFindByEingangsrechnungIId(
										this.getEingangsrechnungDto().getIId());
						if (erKontierungDtos != null && erKontierungDtos.length > 0) {
							getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
						}
					}

				}

			}
			getPanelEingangsrechnungKopfdaten(true).eventYouAreSelected(false);

		} else if (index == iDX_ZUORDNUNG) {
			getPanelEingangsrechnungKopfdatenZuordnung(true)
					.setKeyWhenDetailPanel(this.getEingangsrechnungDto().getIId());
			getPanelEingangsrechnungKopfdatenZuordnung(true).eventYouAreSelected(false);
		} else if (index == iDX_KOPFFUSSTEXT) {

			if (Helper.short2boolean(this.getEingangsrechnungDto().getBMitpositionen()) == true
					&& this.getEingangsrechnungDto().getTGedruckt() != null) {
				getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
			}
			getPanelEingangsrechnungKopfFusstext(true).setKeyWhenDetailPanel(this.getEingangsrechnungDto().getIId());
			getPanelEingangsrechnungKopfFusstext(true).eventYouAreSelected(false);
		} else if (index == iDX_AUFTRAGSZUORDNUNG) {
			getPanelSplitAuftrag(true).eventYouAreSelected(false);
			// wenn ich aus der Uebersicht komme, verlier ich den Titel
			this.setEingangsrechnungDto(this.getEingangsrechnungDto());
		} else if (index == iDX_INSERATZUORDNUNG) {
			getPanelSplitInserat(true).eventYouAreSelected(false);
			// wenn ich aus der Uebersicht komme, verlier ich den Titel
			this.setEingangsrechnungDto(this.getEingangsrechnungDto());
		} else if (index == iDX_KONTIERUNG) {

			if (Helper.short2boolean(this.getEingangsrechnungDto().getBMitpositionen()) == true
					&& this.getEingangsrechnungDto().getTGedruckt() != null) {
				getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
			}

			getPanelQueryKontierung(true).eventYouAreSelected(false);
			getPanelQueryKontierung(true).updateButtons();
			getPanelKontierung(true).eventYouAreSelected(false); // sonst werden
																	// die
																	// buttons
																	// nicht
																	// richtig
																	// gesetzt!
			refreshPanelSplitKontierung();
			this.setEingangsrechnungDto(this.getEingangsrechnungDto());
		} else if (index == iDX_ZAHLUNGEN) {

			// PJ20436
			if (bDarfZahlungenErfassen == false) {
				getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
			}

			boolean bMehrfach = this.getEingangsrechnungDto().getKontoIId() == null
					|| this.getEingangsrechnungDto().getKostenstelleIId() == null;

			if (bMehrfach) {
				BigDecimal bdNochNichtKontiert = DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.getWertNochNichtKontiert(this.getEingangsrechnungDto().getIId());
				if (bdNochNichtKontiert.doubleValue() > 0) {
					getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_READ);
				}
			}

			getPanelSplitZahlung(true).eventYouAreSelected(false);
			getPanelQueryZahlung(true).updateButtons();
			// wenn ich aus der Uebersicht komme, verlier ich den Titel
			this.setEingangsrechnungDto(this.getEingangsrechnungDto());
		} else if (index == iDX_WARENEINGAENGE) {
			getPanelQueryWareneingaenge(true).eventYouAreSelected(false);
			// wenn ich aus der Uebersicht komme, verlier ich den Titel
			this.setEingangsrechnungDto(this.getEingangsrechnungDto());
		} else if (index == iDX_UEBERSICHT) {
			getPanelUebersicht();
			// bevor man an die Uebersicht kommt, muss man die Kriterien waehlen
			getInternalFrame().showPanelDialog(getPanelKriterienUebersicht());
		} else if (index == iDX_REISEZEITEN) {
			getPanelQueryReisezeiten(true).eventYouAreSelected(false);
			// wenn ich aus der Uebersicht komme, verlier ich den Titel
			this.setEingangsrechnungDto(this.getEingangsrechnungDto());
		}

	}

	/**
	 * getPanelEingangsrechnungKopfdaten mit extrem lazy loading.
	 * 
	 * @param bNeedInstantiationIfNull boolean
	 * @return PanelEingangsrechnungKopfdaten
	 * @throws Throwable
	 */
	protected PanelEingangsrechnungKopfdaten getPanelEingangsrechnungKopfdaten(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelEingangsrechnungKopfdaten == null && bNeedInstantiationIfNull) {
			panelEingangsrechnungKopfdaten = new PanelEingangsrechnungKopfdaten(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kopfdaten"), null, this);
			this.setComponentAt(iDX_KOPFDATEN, panelEingangsrechnungKopfdaten);
		}
		return panelEingangsrechnungKopfdaten;
	}

	protected PanelEingangsrechnungKopfdatenZuordnung getPanelEingangsrechnungKopfdatenZuordnung(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelEingangsrechnungKopfdatenZuordnung == null && bNeedInstantiationIfNull) {
			panelEingangsrechnungKopfdatenZuordnung = new PanelEingangsrechnungKopfdatenZuordnung(getInternalFrame(),
					LPMain.getTextRespectUISPr("er.zuordnung"), null, this);
			this.setComponentAt(iDX_ZUORDNUNG, panelEingangsrechnungKopfdatenZuordnung);
		}
		return panelEingangsrechnungKopfdatenZuordnung;
	}

	protected PanelEingangsrechnungKopfFusstext getPanelEingangsrechnungKopfFusstext(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelEingangsrechnungKopfFusstext == null && bNeedInstantiationIfNull) {
			panelEingangsrechnungKopfFusstext = new PanelEingangsrechnungKopfFusstext(getInternalFrame(),
					LPMain.getTextRespectUISPr("er.kopffusstext"), null, this);
			this.setComponentAt(iDX_KOPFFUSSTEXT, panelEingangsrechnungKopfFusstext);
		}
		return panelEingangsrechnungKopfFusstext;
	}

	/**
	 * getPanelAuftrag mit extrem lazy loading.
	 * 
	 * @param bNeedInstantiationIfNull boolean
	 * @return PanelEingangsrechnungAuftragszuordnung
	 * @throws Throwable
	 */
	private PanelEingangsrechnungAuftragszuordnung getPanelAuftrag(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelAuftrag == null && bNeedInstantiationIfNull) {
			panelAuftrag = new PanelEingangsrechnungAuftragszuordnung(getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.auftragszuordnung.title"), null, this);
		}
		return panelAuftrag;
	}

	private PanelEingangsrechnungInseratzuordnung getPanelInserat(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelInserat == null && bNeedInstantiationIfNull) {
			panelInserat = new PanelEingangsrechnungInseratzuordnung(getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.inseratzuordnung.title"), null, this);
		}
		return panelInserat;
	}

	/**
	 * getPanelKontierung mit extrem lazy loading.
	 * 
	 * @param bNeedInstantiationIfNull boolean
	 * @return PanelEingangsrechnungAuftragszuordnung
	 * @throws Throwable
	 */

	private PanelEingangsrechnungKontierung getPanelKontierung(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelKontierung == null && bNeedInstantiationIfNull) {
			panelKontierung = new PanelEingangsrechnungKontierung(getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.kontierung.title"), null, this);
		}
		return panelKontierung;
	}

	/**
	 * getPanelZahlungen mit extrem lazy loading.
	 * 
	 * @param bNeedInstantiationIfNull boolean
	 * @return PanelEingangsrechnungAuftragszuordnung
	 * @throws Throwable
	 */
	private PanelEingangsrechnungZahlung getPanelZahlung(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelZahlung == null && bNeedInstantiationIfNull) {
			panelZahlung = new PanelEingangsrechnungZahlung(getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.zahlungen.title"), this);
		}
		return panelZahlung;
	}

	private PanelBasis getPanelSplitZahlung(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitZahlung == null && bNeedInstantiationIfNull) {
			// if
			// (eingangsrechnungDto.getEingangsrechnungartCNr().equals(EingangsrechnungFac.
			// EINGANGSRECHNUNGART_GUTSCHRIFT)) {
			// panelSplitZahlung = getPanelQueryZahlung(true);
			// }
			// else {
			panelSplitZahlung = new PanelSplit(getInternalFrame(), getPanelZahlung(true), getPanelQueryZahlung(true),
					165);
			// }
			this.setComponentAt(iDX_ZAHLUNGEN, panelSplitZahlung);
		}
		return panelSplitZahlung;
	}

	private PanelSplit getPanelSplitAuftrag(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitAuftrag == null && bNeedInstantiationIfNull) {
			panelSplitAuftrag = new PanelSplit(getInternalFrame(), getPanelAuftrag(true), getPanelQueryAuftrag(true),
					200);
			this.setComponentAt(iDX_AUFTRAGSZUORDNUNG, panelSplitAuftrag);
		}
		return panelSplitAuftrag;
	}

	private PanelSplit getPanelSplitInserat(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelSplitInserat == null && bNeedInstantiationIfNull) {
			panelSplitInserat = new PanelSplit(getInternalFrame(), getPanelInserat(true), getPanelQueryInserat(true),
					200);
			this.setComponentAt(iDX_INSERATZUORDNUNG, panelSplitInserat);
		}
		return panelSplitInserat;
	}

	/*
	 * private PanelSplit getPanelSplitKontierung(boolean bNeedInstantiationIfNull)
	 * throws Throwable { if (panelSplitKontierung == null &&
	 * bNeedInstantiationIfNull) { panelSplitKontierung = new
	 * PanelSplit(getInternalFrame(), getPanelKontierung(true),
	 * getPanelQueryKontierung(true), 200); this.setComponentAt(iDX_KONTIERUNG,
	 * panelSplitKontierung); } return panelSplitKontierung; }
	 */
	private PanelSplit refreshPanelSplitKontierung() throws Throwable {

		if (panelSplitKontierung == null) {
			panelKontierung = getPanelKontierung(true);
			panelQueryKontierung = getPanelQueryKontierung(true);
			panelSplitKontierung = new PanelSplit(getInternalFrame(), panelKontierung, panelQueryKontierung, 130);
			setComponentAt(iDX_KONTIERUNG, panelSplitKontierung);
		}

		return panelSplitKontierung;
	}

	private PanelTabelle getPanelUebersicht() throws Throwable {
		if (panelUebersicht == null) {
			panelUebersicht = new PanelTabelleEingangsrechnungUebersicht(QueryParameters.UC_ID_EINGANGSRECHNUNG_UMSATZ,
					LPMain.getTextRespectUISPr("lp.umsatzuebersicht"), getInternalFrame());
		}
		return panelUebersicht;
	}

	private PanelDialogKriterien getPanelKriterienUebersicht() throws HeadlessException, Throwable {
		if (panelKriterienUebersicht == null) {
			panelKriterienUebersicht = new PanelDialogKriterienEingangsrechnungUebersicht(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.kriterienumsatzuebersicht"), this);
		}
		return panelKriterienUebersicht;
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENU_ACTION_ALLE)) {
			try {
				getInternalFrame().showReportKriterien(
						new ReportEingangsrechnungAlleEingangsrechnungen(this, "Alle Eingangsrechnungen"));
			} catch (Throwable ex) {
				throw new ExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex);
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_DRUCKEN)) {
			print();
		} else if (e.getActionCommand().equals(MENUE_ACTION_DOKUMENTE_DRUCKEN)) {
			getInternalFrame().showReportKriterien(
					new ReportEingangsrechnungDokumente(this,
							LPMain.getTextRespectUISPr("er.eingangsrechnung.dokumentendruck")),
					getLieferantDto().getPartnerDto(), null, false);
		} else if (e.getActionCommand().equals(MENU_ACTION_OFFENE)) {
			getInternalFrame().showReportKriterien(new ReportEingangsrechnungOffene(this, "Offene Eingangsrechnungen"));
		} else if (e.getActionCommand().equals(MENU_ACTION_FEHLENDE_ZOLLPAPIERE)) {

			getInternalFrame().showReportKriterien(
					new ReportFehlendeZollpapiere(this, LPMain.getTextRespectUISPr("er.journal.fehlendezollpapiere")));
		} else if (e.getActionCommand().equals(MENU_ACTION_NICHT_ABGERCHNETE_AZ)) {

			getInternalFrame().showReportKriterien(
					new ReportNichtAbgerechneteAZ_ER(this, LPMain.getTextRespectUISPr("er.menu.nichtabgerechnete")));
		} else if (e.getActionCommand().equals(MENU_ACTION_ERFASSTE_ZOLLPAPIERE)) {

			getInternalFrame().showReportKriterien(new ReportErfassteZollimportpapiere(this,
					LPMain.getTextRespectUISPr("er.report.erfasste.zollimportpapiere")));
		} else if (e.getActionCommand().equals(MENU_ACTION_ZAHLUNG)) {
			getInternalFrame().showReportKriterien(new ReportEingangsrechnungZahlungsjournal(this, "Zahlungsjournal"));
		} else if (e.getActionCommand().equals(MENU_ACTION_KONTIERUNG)) {
			getInternalFrame().showReportKriterien(new ReportEingangsrechnungKontierung(this, "Kontierungsjournal"));
		} else if (e.getActionCommand().equals(MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME)) {

			if (this.getEingangsrechnungDto() != null && this.getEingangsrechnungDto().getIId() != null) {
				DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.verbucheEingangsrechnungNeu(this.getEingangsrechnungDto().getIId());
			}

		} else if (e.getActionCommand().equals(MENUE_ACTION_FREIGABEDATUM_AENDERN)) {

			if (this.getEingangsrechnungDto() != null && this.getEingangsrechnungDto().getIId() != null) {
				java.sql.Date datum = DialogFactory.showDatumseingabe(
						LPMain.getTextRespectUISPr("er.bearbeiten.freigabedatum.aendern"),
						getEingangsrechnungDto().getDFreigabedatum());
				if (datum != null) {
					this.getEingangsrechnungDto().setDFreigabedatum(datum);
					DelegateFactory.getInstance().getEingangsrechnungDelegate()
							.updateEingangsrechnungFreigabedatum(this.getEingangsrechnungDto().getIId(), datum);
				}
			}

		} else if (e.getActionCommand().equals(MENUE_ACTION_BEARBEITEN_MANUELL_ERLEDIGEN)) {
			if (!getPanelEingangsrechnungKopfdaten(true).isLockedDlg()) {
				reloadEingangsrechnungDto();
				if (getEingangsrechnungDto() != null) {
					if (getEingangsrechnungDto().getStatusCNr().equals(EingangsrechnungFac.STATUS_ANGELEGT)
							|| getEingangsrechnungDto().getStatusCNr().equals(EingangsrechnungFac.STATUS_TEILBEZAHLT)) {

						int indexJa = 0;
						int indexNein = 1;
						int indexMehrere = 2;
						int iAnzahlOptionen = 3;

						Object[] aOptionen = new Object[iAnzahlOptionen];
						aOptionen[indexJa] = LPMain.getTextRespectUISPr("lp.ja");
						aOptionen[indexNein] = LPMain.getTextRespectUISPr("lp.nein");
						aOptionen[indexMehrere] = LPMain.getTextRespectUISPr("rech.erledigen.frage.mehrere");

						int iAuswahl = DialogFactory.showModalDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("er.hint.erledigtstatusauferledigt"),
								LPMain.getTextRespectUISPr("lp.frage"), aOptionen, aOptionen[1]);

						if (iAuswahl == indexJa) {

							DelegateFactory.getInstance().getEingangsrechnungDelegate()
									.manuellErledigen(getEingangsrechnungDto().getIId());

							getEingangsrechnungDto().setStatusCNr(EingangsrechnungFac.STATUS_ERLEDIGT);
							// Panel aktualisieren
							this.lPEventObjectChanged(null);
						}
						if (iAuswahl == indexMehrere) {

							panelQueryFLREingangsrechnungenZuErledigen = EingangsrechnungFilterFactory.getInstance()
									.createPanelFLREingangsrechnungOffene(getInternalFrame(),
											getEingangsrechnungDto().getIId(), false, false);
							panelQueryFLREingangsrechnungenZuErledigen.setMultipleRowSelectionEnabled(true);
							panelQueryFLREingangsrechnungenZuErledigen.addButtonAuswahlBestaetigen(null);
							new DialogQuery(panelQueryFLREingangsrechnungenZuErledigen);
						}

					} else if (getEingangsrechnungDto().getStatusCNr().equals(EingangsrechnungFac.STATUS_ERLEDIGT)) {
						if (DialogFactory.showMeldung(LPMain.getTextRespectUISPr("er.hint.erledigtstatuszuruecknehmen"),
								LPMain.getTextRespectUISPr("lp.hint"),
								javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
							DelegateFactory.getInstance().getEingangsrechnungDelegate()
									.manuellErledigen(getEingangsrechnungDto().getIId());
							// Panel aktualisieren
							this.lPEventObjectChanged(null);
						}
					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
								LPMain.getTextRespectUISPr("er.hint.erkannnichtmanuellerledigtwerden"));
					}
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("er.hint.keineeingangsrechnunggewaehlt"));
				}
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_BEARBEITEN_WIEDERHOLENDE_ZUSATZKOSTEN_ANLEGEN)) {
			ZusatzkostenAnlegenResult result = DelegateFactory.er().wiederholendeZusatzkostenAnlegen();
			if (result.hasWarnings()) {
				StringBuffer sbWarnings = new StringBuffer();
				for (ZusatzkostenAnlegenWarningEntry entry : result.warnings()) {
					if (sbWarnings.length() > 0) {
						sbWarnings.append(", ");
					}
					sbWarnings.append(entry.cnr());
				}
				DialogFactory.showModalInfoDialog(LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getMessageTextRespectUISPr("er.hint.wiederholende.warnung", result.total(),
								sbWarnings.toString()));
			} else {
				DialogFactory.showModalInfoDialog(LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getMessageTextRespectUISPr("er.hint.wiederholende.erfolgreich", result.total()));
			}
		} else if (e.getActionCommand().equals(MENUE_ACTION_BEARBEITEN_MAHNDATEN)) {

			if (getEingangsrechnungDto() != null && getEingangsrechnungDto().getIId() != null) {
				DialogMahnstufeDatum d = new DialogMahnstufeDatum(getEingangsrechnungDto().getIId(), this);
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);
			}

		} else if (MENU_ACTION_IMPORT_4VENDING.equals(e.getActionCommand())) {
			import4VendingXML();

		} else if (MENU_ACTION_IMPORT_XLS.equals(e.getActionCommand())) {
			importXLS();

		} else if (MENUE_ACTION_EXPORT_DOKUMENTE.equals(e.getActionCommand())) {
			DialogBelegDocExport dialogExport = new DialogErDocExport(LPMain.getInstance().getDesktop(),
					getInternalFrame());
			dialogExport.addDirectory().addReceiptStartEndDate().setVisible(true);
		}

	}

	private void import4VendingXML() {
		HvOptional<XmlFile> xmlFile = FileOpenerFactory.eingangsrechnungImport4VendingXml(this);
		if (!xmlFile.isPresent())
			return;

		try {
			vendidataImportController.setImportFile(xmlFile.get().getFile());
			DialogVendidataResult dialog = new DialogVendidataResult(LPMain.getInstance().getDesktop(),
					LPMain.getTextRespectUISPr("er.vendidata.import.title"), true, vendidataImportController);
			dialog.setVisible(true);
		} catch (IOException ioE) {
			handleException(ioE, false);
		} catch (Throwable t) {
			handleException(t, false);
		}
	}

	private void importXLS() {

		XlsFileOpener xlsOpener = FileOpenerFactory.eingangsrechnungImportOpenXls(getInternalFrame());
		xlsOpener.doOpenDialog();

		if (!xlsOpener.hasFileChosen())
			return;

		try {
			JxlImportErgebnis jxlImportErgebnis = DelegateFactory.getInstance().getEingangsrechnungDelegate()
					.importiereEingangsrechnungXLS(xlsOpener.getFile().getBytes());

			String sFehler = "";
			if (jxlImportErgebnis.getFehler() != null) {

				sFehler += jxlImportErgebnis.getAlleFehlerAlsString();

			}

			if (jxlImportErgebnis.getExceptions() != null) {

				byte[] CRLFAscii = { 13, 10 };

				Iterator it = jxlImportErgebnis.getExceptions().keySet().iterator();

				while (it.hasNext()) {

					Integer iZeile = (Integer) it.next();

					Throwable ex = jxlImportErgebnis.getExceptions().get(iZeile);

					try {
						DelegateFactory.getInstance().getEingangsrechnungDelegate().handleThrowable(ex);
					} catch (ExceptionLP ex2) {

						String msg = LPMain.getInstance().getMsg(ex2);

						if (msg != null) {
							sFehler += msg + " Zeile " + (iZeile + 1) + new String(CRLFAscii);
						} else {
							sFehler += LPMain.getMessageTextRespectUISPr("xlsimport.unerwarteter_fehler", iZeile + 1,
									ex.toString()) + new String(CRLFAscii);
						}

					}

				}

			}

			if (sFehler.equals("")) {
				sFehler = "KEINE";
			}

			DialogFactory.showMessageMitScrollbar(LPMain.getMessageTextRespectUISPr("lp.info"),
					LPMain.getMessageTextRespectUISPr("er.import.xls.alleimportiert", sFehler,
							jxlImportErgebnis.getiAnzahlErfolgreichImportiert() + ""),
					true);

		} catch (IOException ioE) {
			handleException(ioE, false);
		} catch (Throwable t) {
			handleException(t, false);
		}
	}

	public void print() throws Throwable {

		if (Helper.short2boolean(getEingangsrechnungDto().getBMitpositionen())) {
			getInternalFrame().showReportKriterien(
					new ReportEingangsrechungMitPositionen(getInternalFrame(), getAktuellesPanel(),
							getEingangsrechnungDto(), LPMain.getTextRespectUISPr("er.eingangsrechnung")),
					getLieferantDto().getPartnerDto(), null, false);
		} else {
			getInternalFrame().showReportKriterien(
					new ReportEingangsrechnung(getInternalFrame(), getEingangsrechnungDto(),
							LPMain.getTextRespectUISPr("er.eingangsrechnung")),
					getLieferantDto().getPartnerDto(), null, false);
		}

	}

	private void refreshFilterAuftragszuordnung() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			FilterKriterium[] krit = EingangsrechnungFilterFactory.getInstance()
					.createFKAuftragszuordnung(getEingangsrechnungDto().getIId());
			getPanelQueryAuftrag(true).setDefaultFilter(krit);
		}
	}

	private void refreshFilterInseratzuordnung() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			FilterKriterium[] krit = EingangsrechnungFilterFactory.getInstance()
					.createFKInseratzuordnung(getEingangsrechnungDto().getIId());
			getPanelQueryInserat(true).setDefaultFilter(krit);
		}
	}

	private void refreshFilterKontierung() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			FilterKriterium[] krit = EingangsrechnungFilterFactory.getInstance()
					.createFKKontierung(getEingangsrechnungDto().getIId());
			getPanelQueryKontierung(true).setDefaultFilter(krit);
		}
	}

	protected void enablePanels() throws Throwable {
		setVisibilityKontierung();
		setVisibilityZuordnung();

		if (getEingangsrechnungDto() != null && iDX_WARENEINGAENGE != -1) {
			// SP7864
			if (eingangsrechnungDto.getEingangsrechnungartCNr()
					.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)) {
				setEnabledAt(iDX_WARENEINGAENGE, false);
			} else {
				setEnabledAt(iDX_WARENEINGAENGE, true);
			}

		}

	}

	private void setVisibilityKontierung() throws Throwable {
		if (getEingangsrechnungDto() != null && iDX_KONTIERUNG != -1) {
			// Wenn eine ER nicht auf Mehrfachkontierung gesetzt ist, wird der
			// Tab ausgegraut
			PanelBasis p = (PanelBasis) this.getSelectedComponent();
			Object key;
			if (p == null) {
				key = null;
			} else {
				key = p.getKeyWhenDetailPanel();
			}
			if (key == null || !key.equals(LPMain.getLockMeForNew())) {
				// kontierung evtl. ausgrauen
				if (eingangsrechnungDto.getKostenstelleIId() == null) {
					this.setEnabledAt(iDX_KONTIERUNG, true);
				} else {
					this.setEnabledAt(iDX_KONTIERUNG, false);
				}
			}
		}
	}

	private void setVisibilityZuordnung() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			// Wenn eine ER 'mit Positionen' hat, wird der
			// Tab Zuordnung ausgegraut
			PanelBasis p = (PanelBasis) this.getSelectedComponent();
			Object key;
			if (p == null) {
				key = null;
			} else {
				key = p.getKeyWhenDetailPanel();
			}
			if (key == null || !key.equals(LPMain.getLockMeForNew())) {
				boolean mitPositionen = Helper.short2boolean(eingangsrechnungDto.getBMitpositionen()) == true;

				// Zum Zeitpunkt dieses Aufrufs, kann es sein, dass die Panels
				// dieser Reiter noch instantiert sind, deshalb ist auch der
				// Index noch nicht gesetz
				if (iDX_ZUORDNUNG != -1) {
					// SP7864
					if (eingangsrechnungDto.getEingangsrechnungartCNr()
							.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)) {
						setEnabledAt(iDX_ZUORDNUNG, false);
					} else {
						setEnabledAt(iDX_ZUORDNUNG, !mitPositionen);
					}

				}
				if (iDX_KOPFFUSSTEXT != -1) {
					setEnabledAt(iDX_KOPFFUSSTEXT, mitPositionen);
				}
				// kontierung evtl. ausgrauen
				// if (Helper.short2boolean(eingangsrechnungDto
				// .getBMitpositionen()) == true) {
				// this.setEnabledAt(iDX_ZUORDNUNG, false);
				// this.setEnabledAt(iDX_KOPFFUSSTEXT, true);
				// } else {
				// this.setEnabledAt(iDX_ZUORDNUNG, true);
				// this.setEnabledAt(iDX_KOPFFUSSTEXT, false);
				// }
			}
		}
	}

	private void refreshFilterZahlung() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			FilterKriterium[] krit = EingangsrechnungFilterFactory.getInstance()
					.createFKZahlungen(getEingangsrechnungDto().getIId());
			getPanelQueryZahlung(true, true).setDefaultFilter(krit);
		}
	}

	private void refreshFilterWareneingaenge() throws Throwable {
		if (getEingangsrechnungDto() != null) {
			FilterKriterium[] krit = BestellungFilterFactory.getInstance()
					.createFKWareneingangER(getEingangsrechnungDto().getIId());
			getPanelQueryWareneingaenge(true).setDefaultFilter(krit);
		}
	}

	private void refreshFilterReisezeiten() throws Throwable {
		if (getEingangsrechnungDto() != null) {

			FilterKriterium[] filters = new FilterKriterium[1];
			filters[0] = new FilterKriterium("eingangsrechnung_i_id", true, "" + getEingangsrechnungDto().getIId(),
					FilterKriterium.OPERATOR_EQUAL, false);

			getPanelQueryReisezeiten(true).setDefaultFilter(filters);
		}
	}

	/**
	 * Diese Methode setzt der aktuellen ER aus der Auswahlliste als den zu
	 * lockenden Auftrag.
	 * 
	 * @throws Throwable
	 */
	private void setKeyWasForLockMe() throws Throwable {
		Object oKey = getPanelQueryEingangsrechnung(true).getSelectedId();

		if (oKey != null) {
			holeEingangsrechnungDto(oKey);

			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected void setSelectedEingangsrechnungIId() throws Throwable {
		if (this.getEingangsrechnungDto() != null && this.getEingangsrechnungDto().getIId() != null) {
			getPanelQueryEingangsrechnung(true).setSelectedId(getEingangsrechnungDto().getIId());
		}
	}

	protected void gotoAuswahl() throws Throwable {
		// tabelletitel: zur auswahl
		this.setSelectedIndex(IDX_EINGANGSRECHNUNGEN);
		getPanelQueryEingangsrechnung(true).eventYouAreSelected(false);
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		WrapperMenuBar wmb = new WrapperMenuBar(this);

		// Menue Datei
		JMenu jmModul = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_MODUL);

		jmModul.add(new JSeparator(), 0);

		JMenu menuDateiImport = new WrapperMenu("lp.import", this);

		if (bZusatzfunktion4Vending) {

			JMenuItem menuItemDateiImport4Vending = new JMenuItem(
					LPMain.getTextRespectUISPr("er.menu.import.4vending"));
			menuItemDateiImport4Vending.addActionListener(this);
			menuItemDateiImport4Vending.setActionCommand(MENU_ACTION_IMPORT_4VENDING);
			menuDateiImport.add(menuItemDateiImport4Vending);

		}

		if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD)) {
			JMenuItem menuItemDateiImportXLS = new JMenuItem(LPMain.getTextRespectUISPr("er.menu.import.xls"));
			menuItemDateiImportXLS.addActionListener(this);
			menuItemDateiImportXLS.setActionCommand(MENU_ACTION_IMPORT_XLS);
			menuDateiImport.add(menuItemDateiImportXLS);
		}

		if (menuDateiImport.getItemCount() > 0) {
			jmModul.add(menuDateiImport, 0);
		}

		if (bDarfKontieren) {

			JMenuItem menuItemDateiDokumente = new JMenuItem(
					LPMain.getTextRespectUISPr("er.eingangsrechnung.dokumentendruck"));
			menuItemDateiDokumente.addActionListener(this);
			menuItemDateiDokumente.setActionCommand(MENUE_ACTION_DOKUMENTE_DRUCKEN);
			jmModul.add(menuItemDateiDokumente, 0);
		}

		if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_LP_FINANCIAL_INFO_TYP_1)) {
			JMenu menuDateiExport = new WrapperMenu("er.export", this);
			JMenuItem menuItemExportDokumente = new JMenuItem(LPMain.getTextRespectUISPr("er.export.dokumente"));
			menuItemExportDokumente.addActionListener(this);
			menuItemExportDokumente.setActionCommand(MENUE_ACTION_EXPORT_DOKUMENTE);
			menuDateiExport.add(menuItemExportDokumente);

			jmModul.add(menuDateiExport, 0);
		}

		JMenuItem menuItemDateiBestellung = new JMenuItem(LPMain.getTextRespectUISPr("bes.menu.datei.bestellung"));
		menuItemDateiBestellung.addActionListener(this);
		menuItemDateiBestellung.setActionCommand(MENUE_ACTION_DRUCKEN);
		jmModul.add(menuItemDateiBestellung, 0);

		// Menue Bearbeiten
		JMenu jmBearbeiten = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_BEARBEITEN);

		WrapperMenuItem menuItemBearbeitenFreigabedatum = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("er.bearbeiten.freigabedatum.aendern"),
				RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD);
		menuItemBearbeitenFreigabedatum.addActionListener(this);
		menuItemBearbeitenFreigabedatum.setActionCommand(MENUE_ACTION_FREIGABEDATUM_AENDERN);
		jmBearbeiten.add(menuItemBearbeitenFreigabedatum, 0);

		if (!isBZusatzkosten()) {

			WrapperMenuItem menuItemBearbeitenMahndaten = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("lp.mahnstufe"), RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD);
			menuItemBearbeitenMahndaten.addActionListener(this);
			menuItemBearbeitenMahndaten.setActionCommand(MENUE_ACTION_BEARBEITEN_MAHNDATEN);
			jmBearbeiten.add(menuItemBearbeitenMahndaten, 0);

		}

		if (isBZusatzkosten()) {

			WrapperMenuItem menuItemBearbeitenZusatzkostenAnlegen = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("er.zusatzkosten.wiederholendeanlegen"),
					RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD);
			menuItemBearbeitenZusatzkostenAnlegen.addActionListener(this);
			menuItemBearbeitenZusatzkostenAnlegen
					.setActionCommand(MENUE_ACTION_BEARBEITEN_WIEDERHOLENDE_ZUSATZKOSTEN_ANLEGEN);
			jmBearbeiten.add(menuItemBearbeitenZusatzkostenAnlegen, 0);
		}

		WrapperMenuItem menueItemBeleguebernahme = null;
		menueItemBeleguebernahme = new WrapperMenuItem(LPMain.getTextRespectUISPr("finanz.beleguebernahme"),
				RechteFac.RECHT_FB_CHEFBUCHHALTER);
		menueItemBeleguebernahme.addActionListener(this);
		menueItemBeleguebernahme.setActionCommand(MENUE_ACTION_BEARBEITEN_BELEGUEBERNAHME);
		jmBearbeiten.add(menueItemBeleguebernahme, 0);
		if (!LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {

			WrapperMenuItem menuItemBearbeitenManuellErledigen = new WrapperMenuItem(
					LPMain.getTextRespectUISPr("lp.menu.menuellerledigen"), RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD);
			menuItemBearbeitenManuellErledigen.addActionListener(this);
			menuItemBearbeitenManuellErledigen.setActionCommand(MENUE_ACTION_BEARBEITEN_MANUELL_ERLEDIGEN);
			jmBearbeiten.add(menuItemBearbeitenManuellErledigen, 0);
		}

		JMenu journal = (JMenu) wmb.getComponent(WrapperMenuBar.MENU_JOURNAL);
		WrapperMenuItem menuItemAlle = new WrapperMenuItem(LPMain.getTextRespectUISPr("er.menu.alle"), null);
		WrapperMenuItem menuItemOffene = new WrapperMenuItem(LPMain.getTextRespectUISPr("er.menu.offenezumstichtag"),
				null);
		WrapperMenuItem menuItemZahlung = new WrapperMenuItem(LPMain.getTextRespectUISPr("er.menu.zahlungsausgang"),
				null);
		WrapperMenuItem menuItemKontierung = new WrapperMenuItem(LPMain.getTextRespectUISPr("er.menu.kontierung"),
				null);

		WrapperMenuItem menuItemFehlendeZollpapiere = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("er.journal.fehlendezollpapiere"), null);

		menuItemAlle.addActionListener(this);
		menuItemAlle.setActionCommand(MENU_ACTION_ALLE);

		menuItemFehlendeZollpapiere.addActionListener(this);
		menuItemFehlendeZollpapiere.setActionCommand(MENU_ACTION_FEHLENDE_ZOLLPAPIERE);

		WrapperMenuItem menuItemErfassteZollpapiere = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("er.report.erfasste.zollimportpapiere"), null);
		menuItemErfassteZollpapiere.addActionListener(this);
		menuItemErfassteZollpapiere.setActionCommand(MENU_ACTION_ERFASSTE_ZOLLPAPIERE);

		menuItemOffene.addActionListener(this);
		menuItemOffene.setActionCommand(MENU_ACTION_OFFENE);
		menuItemZahlung.addActionListener(this);
		menuItemZahlung.setActionCommand(MENU_ACTION_ZAHLUNG);
		menuItemKontierung.addActionListener(this);
		menuItemKontierung.setActionCommand(MENU_ACTION_KONTIERUNG);
		journal.add(menuItemAlle);
		journal.add(menuItemOffene);
		journal.add(menuItemZahlung);
		journal.add(menuItemKontierung);

		WrapperMenuItem menuItemNichtAbgerechnet = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("er.menu.nichtabgerechnete"), null);
		menuItemNichtAbgerechnet.addActionListener(this);
		menuItemNichtAbgerechnet.setActionCommand(MENU_ACTION_NICHT_ABGERCHNETE_AZ);
		journal.add(menuItemNichtAbgerechnet);

		if (bZusatzkosten == false) {
			journal.add(menuItemFehlendeZollpapiere);
			journal.add(menuItemErfassteZollpapiere);
		}

		return wmb;
	}

	public Object getDto() {
		return eingangsrechnungDto;
	}

	/**
	 * getPanelQueryAuftrag mit extrem lazy loading.
	 * 
	 * @return PanelQuery
	 * @param bNeedInstantiationIfNull boolean
	 * @throws Throwable
	 */
	private PanelQuery getPanelQueryWareneingaenge(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryWareneingaenge == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseAuftrag = { PanelBasis.ACTION_NEW };
			panelQueryWareneingaenge = new PanelQuery(null, null, QueryParameters.UC_ID_EINGANGSRECHNUNG_WARENEINGANG,
					aWhichButtonIUseAuftrag, getInternalFrame(),
					LPMain.getTextRespectUISPr("er.tab.oben.wareneingaenge.title"), true);

			panelQueryWareneingaenge.getToolBar().addButtonLeft("/com/lp/client/res/data_into.png",
					LPMain.getTextRespectUISPr("proj.projektverlauf.gehezubeleg"), GOTO_WE,
					KeyStroke.getKeyStroke('G', java.awt.event.InputEvent.CTRL_MASK), null);
			panelQueryWareneingaenge.enableToolsPanelButtons(true, GOTO_WE);

			panelQueryWareneingaenge.createAndSaveAndShowButton("/com/lp/client/res/delete2.png",
					LPMain.getTextRespectUISPr("er.weseinerrechnungadresse.zuordnungloeschen"),
					MENUE_ACTION_ZUORDNUNG_ER_LOESCHEN, RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF);

			panelQueryWareneingaenge.getToolBar().getToolsPanelCenter().add(lblweDiff);

			panelQueryWareneingaenge
					.addDirektFilter(new FilterKriteriumDirekt("flrbestellung.c_nr", "", FilterKriterium.OPERATOR_LIKE,
							LPMain.getTextRespectUISPr("label.bestellnummer"), FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung
																														// als
																														// '%XX'
							true, // wrapWithSingleQuotes
							false, Facade.MAX_UNBESCHRAENKT)); // ignore case);
			panelQueryWareneingaenge.addDirektFilter(new FilterKriteriumDirekt("c_lieferscheinnr", "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("bes.lieferscheinnummer"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
															// '%XX'
					true, // wrapWithSingleQuotes
					false, Facade.MAX_UNBESCHRAENKT)); // ignore case);

			setComponentAt(iDX_WARENEINGAENGE, panelQueryWareneingaenge);
		}
		return panelQueryWareneingaenge;
	}

	private PanelQuery getPanelQueryReisezeiten(boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelQueryReisezeiten == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseAuftrag = {};
			panelQueryReisezeiten = new PanelQuery(null, null, QueryParameters.UC_ID_EINGANGSRECHNUNG_REISEZEITEN,
					aWhichButtonIUseAuftrag, getInternalFrame(), LPMain.getTextRespectUISPr("pers.reisezeiten"), true);

			setComponentAt(iDX_REISEZEITEN, panelQueryReisezeiten);
		}
		return panelQueryReisezeiten;
	}

	public WareneingangDto getWareneingangDto() {
		return this.wareneingangDto;
	}

	public void setWareneingangDto(WareneingangDto wareneingangDto) {
		this.wareneingangDto = wareneingangDto;
	}

	public void erstelleEingangsrechnungausInseraten(Object[] inseratIIds) throws ExceptionLP, Throwable {

		if (inseratIIds != null && inseratIIds.length > 0) {

			this.inseratIIds = inseratIIds;

			EingangsrechnungDto eingangsrechnungDto = new EingangsrechnungDto();

			InseratDto inseratDtoErstes = DelegateFactory.getInstance().getInseratDelegate()
					.inseratFindByPrimaryKey((Integer) inseratIIds[0]);

			LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
					.lieferantFindByPrimaryKey(inseratDtoErstes.getLieferantIId());

			MwstsatzDto mwst = DelegateFactory.getInstance().getMandantDelegate()
					.mwstsatzFindZuDatum(lieferantDto.getMwstsatzbezIId(), HelperTimestamp.cut());
			/*
			 * MwstsatzDto mwst = DelegateFactory.getInstance().getMandantDelegate()
			 * .mwstsatzFindByMwstsatzbezIIdAktuellster(lieferantDto.getMwstsatzbezIId());
			 */
			if (mwst == null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("lp.error.mwstsatzlieferant"));
			} else {
				Calendar cal = Calendar.getInstance();
				eingangsrechnungDto.setMwstsatzIId(mwst.getIId());
				eingangsrechnungDto.setMandantCNr(lieferantDto.getMandantCNr());

				eingangsrechnungDto.setLieferantIId(lieferantDto.getIId());

				eingangsrechnungDto.setDBelegdatum(Helper.cutDate(new java.sql.Date(cal.getTimeInMillis())));
				eingangsrechnungDto.setWaehrungCNr(LPMain.getTheClient().getSMandantenwaehrung());

				eingangsrechnungDto.setNKurs(new BigDecimal(1));

				BigDecimal bdBetrag = new BigDecimal(0);
				for (int i = 0; i < inseratIIds.length; i++) {

					InseratDto inseratDto = DelegateFactory.getInstance().getInseratDelegate()
							.inseratFindByPrimaryKey((Integer) inseratIIds[i]);

					BigDecimal bdBetraggEinesInserates = inseratDto.getNNettoeinzelpreisEk()
							.multiply(inseratDto.getNMenge());

					double dRabattsatz = 100.0;
					if (inseratDto.getFKdRabatt() != null) {
						dRabattsatz = dRabattsatz - inseratDto.getFLFRabatt();
					}
					if (inseratDto.getFKdZusatzrabatt() != null) {
						double dFaktor2 = 100.0 - inseratDto.getFLfZusatzrabatt();
						dRabattsatz = dRabattsatz * dFaktor2 / 100.0;
					}
					if (inseratDto.getFKdNachlass() != null) {
						double dFaktor3 = 100.0 - inseratDto.getFLfNachlass();
						dRabattsatz = dRabattsatz * dFaktor3 / 100.0;
					}

					Double fRabattsatz = new Double(100.0 - dRabattsatz);
					BigDecimal bdRabattbetrag = Helper.getProzentWert(bdBetraggEinesInserates,
							new BigDecimal(fRabattsatz), 2);

					bdBetrag = bdBetrag.add(bdBetraggEinesInserates.subtract(bdRabattbetrag));

					InseratartikelDto[] inseratartikelDto = DelegateFactory.getInstance().getInseratDelegate()
							.inseratartikelFindByInseratIId(inseratDto.getIId());
					for (int j = 0; j < inseratartikelDto.length; j++) {
						bdBetrag = bdBetrag.add(inseratartikelDto[j].getNMenge()
								.multiply(inseratartikelDto[j].getNNettoeinzelpreisEk()));
					}

				}

				bdBetrag = bdBetrag.add(Helper.getProzentWert(bdBetrag, new BigDecimal(mwst.getFMwstsatz()), 4));

				eingangsrechnungDto.setNBetragfw(bdBetrag);
				eingangsrechnungDto.setNBetrag(bdBetrag.multiply(eingangsrechnungDto.getNKurs()));
				eingangsrechnungDto.setMwstsatzIId(lieferantDto.getMwstsatzbezIId());
				eingangsrechnungDto.setNUstBetragfw(Helper.getMehrwertsteuerBetrag(bdBetrag, mwst.getFMwstsatz()));
				eingangsrechnungDto
						.setNUstBetrag(eingangsrechnungDto.getNUstBetragfw().multiply(eingangsrechnungDto.getNKurs()));
				eingangsrechnungDto.setEingangsrechnungartCNr(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG);
				eingangsrechnungDto.setDFreigabedatum(Helper.cutDate(new java.sql.Date(cal.getTimeInMillis())));
				eingangsrechnungDto.setZahlungszielIId(lieferantDto.getZahlungszielIId());
				eingangsrechnungDto.setKontoIId(lieferantDto.getKontoIIdWarenkonto());
				eingangsrechnungDto.setKostenstelleIId(lieferantDto.getIIdKostenstelle());
				if (DelegateFactory.getInstance().getMandantDelegate()
						.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
					eingangsrechnungDto.setBIgErwerb(Helper.boolean2Short(DelegateFactory.getInstance()
							.getFinanzServiceDelegate().istIgErwerb(lieferantDto.getKontoIIdKreditorenkonto())));
				}

				PanelEingangsrechnungKopfdaten pErKHelper = getPanelEingangsrechnungKopfdaten(true);
				pErKHelper.eventActionNew(null, true, false);
				setSelectedComponent(pErKHelper);
				pErKHelper.setMyComponents(eingangsrechnungDto);
				pErKHelper.eventActionRefresh(null, false);
				pErKHelper.wlaWaehrung1.setText(eingangsrechnungDto.getWaehrungCNr());
				pErKHelper.wlaWaehrung2.setText(eingangsrechnungDto.getWaehrungCNr());
			}
		}
	}

	public void erstelleEingangsrechnungausBestellung(Integer iBestellungIId, WareneingangDto wareneingangDto)
			throws ExceptionLP, Throwable {
		this.wareneingangDto = wareneingangDto;
		EingangsrechnungDto eingangsrechnungDto = new EingangsrechnungDto();
		BestellungDto bestellungDto = DelegateFactory.getInstance().getBestellungDelegate()
				.bestellungFindByPrimaryKey(iBestellungIId);
		LieferantDto lieferantDto = null;
		if (bestellungDto.getLieferantIIdRechnungsadresse() != null) {
			lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
					.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdRechnungsadresse());
		} else {
			lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
					.lieferantFindByPrimaryKey(bestellungDto.getLieferantIIdBestelladresse());
		}

		MwstsatzDto mwst = DelegateFactory.getInstance().getMandantDelegate()
				.mwstsatzFindZuDatum(lieferantDto.getMwstsatzbezIId(), HelperTimestamp.cut());
		/*
		 * MwstsatzDto mwst = DelegateFactory.getInstance().getMandantDelegate()
		 * .mwstsatzFindByMwstsatzbezIIdAktuellster(lieferantDto.getMwstsatzbezIId());
		 */
		if (mwst == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.mwstsatzlieferant"));
		} else {
			Calendar cal = Calendar.getInstance();
			eingangsrechnungDto.setMwstsatzIId(mwst.getIId());
			eingangsrechnungDto.setMandantCNr(bestellungDto.getMandantCNr());
			if (bestellungDto.getLieferantIIdRechnungsadresse() != null) {
				eingangsrechnungDto.setLieferantIId(bestellungDto.getLieferantIIdRechnungsadresse());
			} else {
				eingangsrechnungDto.setLieferantIId(bestellungDto.getLieferantIIdBestelladresse());
			}

			if (eingangsrechnungDto.getLieferantIId() != null) {
				DelegateFactory.getInstance().getLieferantDelegate().pruefeLieferant(
						eingangsrechnungDto.getLieferantIId(), LocaleFac.BELEGART_EINGANGSRECHNUNG, getInternalFrame());
			}

			eingangsrechnungDto.setDBelegdatum(Helper.cutDate(new java.sql.Date(cal.getTimeInMillis())));
			eingangsrechnungDto.setWaehrungCNr(bestellungDto.getWaehrungCNr());
			/*
			 * AD: ge&auml;ndert auf Kurs zu Datum eingangsrechnungDto
			 * .setNKurs(Helper.getKehrwert(new BigDecimal( bestellungDto
			 * .getDWechselkursMandantwaehrungBestellungswaehrung())));
			 */
			WechselkursDto kursDto = DelegateFactory.getInstance().getLocaleDelegate().getKursZuDatum(
					eingangsrechnungDto.getWaehrungCNr(), LPMain.getTheClient().getSMandantenwaehrung(),
					eingangsrechnungDto.getDBelegdatum());
			eingangsrechnungDto.setNKurs(kursDto.getNKurs());

			WareneingangspositionDto[] wareneingangsposDto = new WareneingangDelegate()
					.wareneingangspositionFindByWareneingangIId(wareneingangDto.getIId());
			BigDecimal bdBetrag = BigDecimal.ZERO;

			EinstandspreiseEinesWareneingangsDto preiseDtos = DelegateFactory.getInstance().getWareneingangDelegate()
					.getBerechnetenEinstandspreisEinerWareneingangsposition(wareneingangDto.getIId());

			for (int i = 0; i < wareneingangsposDto.length; i++) {
				// PJ 17308
				BestellpositionDto bestposDto = DelegateFactory.getInstance().getBestellungDelegate()
						.bestellpositionFindByPrimaryKey(wareneingangsposDto[i].getBestellpositionIId());

				if (preiseDtos.getHmEinstandpreiseAllerPositionen().containsKey(wareneingangsposDto[i].getIId())) {

					BigDecimal bdEinstandspreisUngerundet = preiseDtos.getHmEinstandpreiseAllerPositionen()
							.get(wareneingangsposDto[i].getIId());

					if (bdEinstandspreisUngerundet != null) {
						bdBetrag = bdBetrag
								.add(bdEinstandspreisUngerundet.multiply(wareneingangsposDto[i].getNGeliefertemenge()));
					} else {
						bdBetrag = bdBetrag.add(wareneingangsposDto[i].getNGelieferterpreis()
								.multiply(wareneingangsposDto[i].getNGeliefertemenge()));
						// SP2514 Fixkosten duerfen nur hinzugefuegt werden,
						// wenn es nur einen geliefert-Preis gibt, wenns bereits
						// einen Einstandspreis gibt, dann ist dieser bereits
						// eingerechnet
						if (bestposDto.getNFixkostengeliefert() != null) {
							bdBetrag = bdBetrag.add(bestposDto.getNFixkostengeliefert());
						}

					}
				}

			}

			// SP 2239: Wird jetzt vom PanelEingangsrechnungKopfdaten selbst
			// gerechnet
			// gilt nicht mehr: // PJ 15885 MWST hinzufuegen:
			// bdBetrag = bdBetrag.add(Helper.getProzentWert(bdBetrag,
			// new BigDecimal(mwst.getFMwstsatz()), 4));
			eingangsrechnungDto.setNBetragfw(bdBetrag);
			eingangsrechnungDto.setNBetrag(bdBetrag.multiply(eingangsrechnungDto.getNKurs()));
			eingangsrechnungDto.setMwstsatzIId(mwst.getIId());
			eingangsrechnungDto.setNUstBetragfw(
					Helper.getMehrwertsteuerBetragFuerNetto(bdBetrag, BigDecimal.valueOf(mwst.getFMwstsatz())));
			eingangsrechnungDto
					.setNUstBetrag(eingangsrechnungDto.getNUstBetragfw().multiply(eingangsrechnungDto.getNKurs()));
			eingangsrechnungDto.setEingangsrechnungartCNr(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG);
			eingangsrechnungDto.setDFreigabedatum(Helper.cutDate(new java.sql.Date(cal.getTimeInMillis())));
			eingangsrechnungDto.setZahlungszielIId(lieferantDto.getZahlungszielIId());
			eingangsrechnungDto.setKontoIId(lieferantDto.getKontoIIdWarenkonto());
			eingangsrechnungDto.setKostenstelleIId(lieferantDto.getIIdKostenstelle());
			if (DelegateFactory.getInstance().getMandantDelegate()
					.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
				eingangsrechnungDto.setBIgErwerb(Helper.boolean2Short(DelegateFactory.getInstance()
						.getFinanzServiceDelegate().istIgErwerb(lieferantDto.getKontoIIdKreditorenkonto())));
			} else {
				eingangsrechnungDto.setBIgErwerb(lieferantDto.getBIgErwerb());
			}

			PanelEingangsrechnungKopfdaten pErKHelper = getPanelEingangsrechnungKopfdaten(true);
			pErKHelper.eventActionNew(null, true, false);
			setSelectedComponent(pErKHelper);
			pErKHelper.setMyComponents(eingangsrechnungDto);
			pErKHelper.eventActionRefresh(null, false);
			pErKHelper.wlaWaehrung1.setText(eingangsrechnungDto.getWaehrungCNr());
			pErKHelper.wlaWaehrung2.setText(eingangsrechnungDto.getWaehrungCNr());
			pErKHelper.setNettoBetrag(bdBetrag, eingangsrechnungDto.getMwstsatzIId());
			pErKHelper.setOriginalNettoBetrag(bdBetrag, eingangsrechnungDto.getNUstBetragfw());
		}
	}

	public void erstelleEingangsrechnungausQRCode(Integer lieferantIId, BigDecimal wertBrutto, String waehrung,
			String lieferantenrechnungsnummer, String zahlungsreferenz) throws ExceptionLP, Throwable {

		EingangsrechnungDto eingangsrechnungDto = new EingangsrechnungDto();

		LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
				.lieferantFindByPrimaryKey(lieferantIId);

		MwstsatzDto mwst = DelegateFactory.getInstance().getMandantDelegate()
				.mwstsatzFindZuDatum(lieferantDto.getMwstsatzbezIId(), HelperTimestamp.cut());

		/*
		 * MwstsatzDto mwst = DelegateFactory.getInstance().getMandantDelegate()
		 * .mwstsatzFindByMwstsatzbezIIdAktuellster(lieferantDto.getMwstsatzbezIId());
		 */
		if (mwst == null) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.mwstsatzlieferant"));
		} else {
			Calendar cal = Calendar.getInstance();
			eingangsrechnungDto.setMwstsatzIId(mwst.getIId());
			eingangsrechnungDto.setMandantCNr(lieferantDto.getMandantCNr());
			eingangsrechnungDto.setLieferantIId(lieferantDto.getIId());

			if (eingangsrechnungDto.getLieferantIId() != null) {
				DelegateFactory.getInstance().getLieferantDelegate().pruefeLieferant(
						eingangsrechnungDto.getLieferantIId(), LocaleFac.BELEGART_EINGANGSRECHNUNG, getInternalFrame());
			}

			eingangsrechnungDto.setDBelegdatum(Helper.cutDate(new java.sql.Date(cal.getTimeInMillis())));
			eingangsrechnungDto.setWaehrungCNr(waehrung);
			/*
			 * AD: ge&auml;ndert auf Kurs zu Datum eingangsrechnungDto
			 * .setNKurs(Helper.getKehrwert(new BigDecimal( bestellungDto
			 * .getDWechselkursMandantwaehrungBestellungswaehrung())));
			 */
			WechselkursDto kursDto = DelegateFactory.getInstance().getLocaleDelegate().getKursZuDatum(
					eingangsrechnungDto.getWaehrungCNr(), LPMain.getTheClient().getSMandantenwaehrung(),
					eingangsrechnungDto.getDBelegdatum());
			eingangsrechnungDto.setNKurs(kursDto.getNKurs());

			BigDecimal mwstBetrag = Helper.getMehrwertsteuerBetrag(wertBrutto, mwst.getFMwstsatz());
			BigDecimal betragNetto = wertBrutto.subtract(mwstBetrag);

			eingangsrechnungDto.setNBetragfw(betragNetto);

			eingangsrechnungDto.setNBetrag(betragNetto.multiply(eingangsrechnungDto.getNKurs()));
			eingangsrechnungDto.setMwstsatzIId(mwst.getIId());
			eingangsrechnungDto.setNUstBetragfw(mwstBetrag);
			eingangsrechnungDto.setNUstBetrag(mwstBetrag.multiply(eingangsrechnungDto.getNKurs()));
			eingangsrechnungDto.setEingangsrechnungartCNr(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG);
			eingangsrechnungDto.setDFreigabedatum(Helper.cutDate(new java.sql.Date(cal.getTimeInMillis())));
			eingangsrechnungDto.setZahlungszielIId(lieferantDto.getZahlungszielIId());
			eingangsrechnungDto.setKontoIId(lieferantDto.getKontoIIdWarenkonto());
			eingangsrechnungDto.setKostenstelleIId(lieferantDto.getIIdKostenstelle());

			// PJ21916 Erste Ziffer suchen
			if (lieferantenrechnungsnummer != null) {

				if (lieferantenrechnungsnummer.length() > 40) {
					eingangsrechnungDto.setCText(lieferantenrechnungsnummer.substring(0, 40));
				} else {
					eingangsrechnungDto.setCText(lieferantenrechnungsnummer);
				}

				char[] cArray = lieferantenrechnungsnummer.toCharArray();
				int iErsteZiffer = 0;
				for (int i = 0; i < cArray.length; i++) {

					if (cArray[i] != '0' && cArray[i] != '1' && cArray[i] != '2' && cArray[i] != '3' && cArray[i] != '4'
							&& cArray[i] != '5' && cArray[i] != '6' && cArray[i] != '7' && cArray[i] != '8'
							&& cArray[i] != '9') {
					} else {
						iErsteZiffer = i;
						break;
					}
				}

				// Erste Ziffer
				lieferantenrechnungsnummer = lieferantenrechnungsnummer.substring(iErsteZiffer,
						lieferantenrechnungsnummer.length());

				if (lieferantenrechnungsnummer.length() > 20) {
					lieferantenrechnungsnummer = lieferantenrechnungsnummer.substring(0, 20);
				}

			}

			eingangsrechnungDto.setCLieferantenrechnungsnummer(lieferantenrechnungsnummer);

			if (zahlungsreferenz != null) {
				if (zahlungsreferenz.length() > 40) {
					eingangsrechnungDto.setCKundendaten(zahlungsreferenz.substring(0, 40));
				} else {
					eingangsrechnungDto.setCKundendaten(zahlungsreferenz);
				}
			}

			if (DelegateFactory.getInstance().getMandantDelegate()
					.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {
				eingangsrechnungDto.setBIgErwerb(Helper.boolean2Short(DelegateFactory.getInstance()
						.getFinanzServiceDelegate().istIgErwerb(lieferantDto.getKontoIIdKreditorenkonto())));
			} else {
				eingangsrechnungDto.setBIgErwerb(lieferantDto.getBIgErwerb());
			}

			PanelEingangsrechnungKopfdaten pErKHelper = getPanelEingangsrechnungKopfdaten(true);
			pErKHelper.eventActionNew(null, true, false);
			setSelectedComponent(pErKHelper);
			pErKHelper.setMyComponents(eingangsrechnungDto);
			pErKHelper.eventActionRefresh(null, false);
			pErKHelper.wlaWaehrung1.setText(eingangsrechnungDto.getWaehrungCNr());
			pErKHelper.wlaWaehrung2.setText(eingangsrechnungDto.getWaehrungCNr());
			pErKHelper.setNettoBetrag(betragNetto, eingangsrechnungDto.getMwstsatzIId());

		}
	}

	public boolean isIGErwerbOrReversecharge() {
		EingangsrechnungDto erDto = getEingangsrechnungDto();
		if (erDto == null) {
			myLogger.error("EingangsrechnungDto ist nicht gesetzt, trotzdem wird isIGErwerbOrReversecharge abgefragt");
			return false;
		}

		return Helper.short2boolean(erDto.getBIgErwerb())
				|| !rcartOhneDto.getIId().equals(erDto.getReversechargeartId());
	}

	public boolean isReversecharge() {
		EingangsrechnungDto erDto = getEingangsrechnungDto();
		if (erDto == null) {
			myLogger.error("EingangsrechnungDto ist nicht gesetzt, trotzdem wird isReversecharge abgefragt");
			return false;
		}

		return !rcartOhneDto.getIId().equals(erDto.getReversechargeartId());
	}

	public void copyHV() throws ExceptionLP, Throwable {
		int selectedPanelIndex = this.getSelectedIndex();

		if (selectedPanelIndex == iDX_AUFTRAGSZUORDNUNG) {
			Object aoIIdPosition[] = panelQueryAuftrag.getSelectedIds();

			if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
				EingangsrechnungAuftragszuordnungDto[] dtos = new EingangsrechnungAuftragszuordnungDto[aoIIdPosition.length];
				Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
				for (int i = 0; i < aIIdPosition.length; i++) {
					aIIdPosition[i] = (Integer) aoIIdPosition[i];
					dtos[i] = DelegateFactory.getInstance().getEingangsrechnungDelegate()
							.eingangsrechnungAuftragszuordnungFindByPrimaryKey((Integer) aoIIdPosition[i]);
				}
				LPMain.getPasteBuffer().writeObjectToPasteBuffer(dtos);
			}
		}
	}

	public void einfuegenHV() throws IOException, ParserConfigurationException, SAXException, Throwable {

		Object o = LPMain.getPasteBuffer().readObjectFromPasteBuffer();

		int selectedPanelIndex = this.getSelectedIndex();
		if (selectedPanelIndex == iDX_AUFTRAGSZUORDNUNG) {
			if (o instanceof EingangsrechnungAuftragszuordnungDto[]) {
				EingangsrechnungAuftragszuordnungDto[] eaDtos = (EingangsrechnungAuftragszuordnungDto[]) o;

				ButtonGroup group = new javax.swing.ButtonGroup();
				String msgString1 = LPMain.getTextRespectUISPr("er.auftragszurdnung.kopieren.frage");
				JRadioButton opt1 = new javax.swing.JRadioButton(
						LPMain.getTextRespectUISPr("er.auftragszurdnung.kopieren.frage.exakt"));
				JRadioButton opt2 = new javax.swing.JRadioButton(
						LPMain.getTextRespectUISPr("er.auftragszurdnung.kopieren.frage.anteilig"));

				opt1.setSelected(true);

				group.add(opt1);
				group.add(opt2);

				Object[] array = { msgString1, opt1, opt2 };
				int sel = javax.swing.JOptionPane.showConfirmDialog(null, array, "", JOptionPane.DEFAULT_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (sel == 0) {
					if (opt2.isSelected()) {

						DelegateFactory.getInstance().getEingangsrechnungDelegate()
								.eingangsrechnungAuftragszuordnungAnteilsmaessigKopieren(eaDtos,
										getEingangsrechnungDto().getIId());

					} else {
						DelegateFactory.getInstance().getEingangsrechnungDelegate()
								.eingangsrechnungAuftragszuordnungExaktKopieren(eaDtos,
										getEingangsrechnungDto().getIId());
					}
				}

				panelSplitAuftrag.eventYouAreSelected(false);

			}
		}
	}

}
