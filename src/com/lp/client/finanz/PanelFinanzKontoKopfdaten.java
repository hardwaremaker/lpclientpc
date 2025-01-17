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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.EventObject;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.FinanzDelegate;
import com.lp.client.frame.delegate.FinanzServiceDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.ErgebnisgruppeDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.finanz.service.KontoartDto;
import com.lp.server.finanz.service.UvaartDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

import net.miginfocom.swing.MigLayout;

/**
 * <p>
 * <I>Panel zur Anzeige der Kontokopfdaten</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>01. 01. 2004</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Gernot Gebhart, Martin Bluehweis
 * @version $Revision: 1.34 $
 */
public class PanelFinanzKontoKopfdaten extends PanelBasis {
	private static final long serialVersionUID = -190307060415174759L;

	private TabbedPaneKonten tabbedPaneKonten = null;

	private Integer kontonummerMaxStellen = null;
	private boolean bIstModulKostenstelleInstalliert = false;

	private Boolean printKontoart = true;

	private KontoDtoSmall ustKontoDto = null;
	private KontoDtoSmall skontoKontoDto = null;

	private FinanzamtDto finanzamtDto = null;
	private PartnerDto partnerDto = null;
	private KostenstelleDto kostenstelleDto = null;
	private UvaartDto uvaartDto = null;
	private KontoartDto kontoartDto = null;
	private ErgebnisgruppeDto ergebnisgruppeDto = null;
	private ErgebnisgruppeDto bilanzgruppeDto_negativ = null;
//	private SteuerkategorieDto steuerkategorieDto = null;
//	private SteuerkategorieDto steuerkategorieReverseDto = null;
	private String steuerkategorieCnr = null;

	private PanelQueryFLR panelQueryFLRKontoart = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;
	private PanelQueryFLR panelQueryFLRUstKonto = null;
	private PanelQueryFLR panelQueryFLRSkontoKonto = null;

	private PanelQueryFLR panelQueryFLRUvaArt = null;
	private PanelQueryFLR panelQueryFLRFinanzamt = null;
	private PanelQueryFLR panelQueryFLRErgebnisgruppe = null;
//	private PanelQueryFLR panelQueryFLRSteuerkategorie = null;
//	private PanelQueryFLR panelQueryFLRSteuerkategorieReverse = null;
	private PanelQueryFLR panelQueryFLRBilanzgruppeNegativ = null;

	private static final String ACTION_SPECIAL_KONTOART = "action_special_konto_kontoart";
	private static final String ACTION_SPECIAL_KOSTENSTELLE = "action_special_konto_kostenstelle";
	private static final String ACTION_SPECIAL_USTKONTO = "action_special_konto_ustkonto";
	private static final String ACTION_SPECIAL_SKONTOKONTO = "action_special_konto_skontokonto";
	private static final String ACTION_SPECIAL_UVAART = "action_special_konto_uvaart";
	private static final String ACTION_SPECIAL_FINANZAMT = "action_special_konto_finanzamt";
	private static final String ACTION_SPECIAL_ERGEBNISGRUPPE = "action_special_konto_ergebnisgruppe";
	private static final String ACTION_SPECIAL_BILANZGRUPPE = "action_special_konto_bilanzgruppe";
	private static final String ACTION_SPECIAL_BILANZGRUPPE_NEGATIV = "action_special_konto_bilanzgruppe_negativ";
//	private static final String ACTION_SPECIAL_STEUERKATEGORIE_REVERSE = "action_special_konto_steuerkategorie_reverse";
//	private static final String ACTION_SPECIAL_STEUERKATEGORIE = "action_special_konto_steuerkategorie";
	private static final String ACTION_SPECIAL_WAEHRUNGDRUCK = "action_special_waehrungdruck";

	private GridBagLayout gridBagLayoutAll = new GridBagLayout();
	private WrapperLabel wlaKontoBezeichnung = new WrapperLabel();
	private WrapperButton wbuKostenstelle = new WrapperButton();
	private WrapperLabel wlaKontoNummer = new WrapperLabel();
	private WrapperButton wbuFinanzamt = new WrapperButton();
	private WrapperLabel wlaGueltigVon = new WrapperLabel();
	private WrapperCheckBox wcbAutomatikEroeffnung = new WrapperCheckBox();
	private WrapperCheckBox wcbohneUst = new WrapperCheckBox();
	private WrapperCheckBox wcbAllgemeinSichtbar = new WrapperCheckBox();
	private WrapperCheckBox wcbManuellbebuchbar = new WrapperCheckBox();
	private WrapperButton wbuUVAArt = new WrapperButton();
//	private WrapperButton wbuSteuerkategorie = new WrapperButton();
//	private WrapperButton wbuSteuerkategorieReverse = new WrapperButton();
	private WrapperTextField wtfUstKontoBezeichnung = new WrapperTextField();
	private WrapperButton wbuSkontoKonto = new WrapperButton();
	private WrapperTextField wtfUstKontoNummer = new WrapperTextField();
	private WrapperButton wbuUstKonto = new WrapperButton();
	private WrapperTextField wtfSkontoKontoBezeichnung = new WrapperTextField();
	private WrapperTextField wtfSkontoKontoNummer = new WrapperTextField();
	private WrapperButton wbuKontoart = new WrapperButton();
	private WrapperLabel wlaGueltigBis = new WrapperLabel();
	private WrapperTextNumberField wtfKontoNummer = null;
	private WrapperTextField wtfKostenstelleNummer = new WrapperTextField();
	private WrapperTextField wtfKostenstelleBezeichnung = new WrapperTextField();
	private WrapperTextField wtfKontoBezeichnung = new WrapperTextField();
	private WrapperTextField wtfKontoart = new WrapperTextField();
	private WrapperTextField wtfUVAArt = new WrapperTextField();
//	private WrapperTextField wtfSteuerkategorie = new WrapperTextField();
//	private WrapperTextField wtfSteuerkategorieReverse = new WrapperTextField();
	private WrapperTextField wtfFinanzamt = new WrapperTextField();
	private WrapperDateField wdfGueltigVon = new WrapperDateField();
	private WrapperDateField wdfGueltigBis = new WrapperDateField();
	private WrapperLabel wlaAbstand = new WrapperLabel();
	private WrapperLabel wlaBankInfo = new WrapperLabel();

	private WrapperLabel wlaUVAVariante = new WrapperLabel();
	private WrapperComboBox wcoUVAVariante = new WrapperComboBox();

	private WrapperButton wbuBilanzgruppe = new WrapperButton();
	private WrapperTextField wtfBilanzgruppe = new WrapperTextField();

	private WrapperButton wbuBilanzgruppeNegativ = new WrapperButton();
	private WrapperTextField wtfBilanzgruppeNegativ = new WrapperTextField();

	private WrapperComboBox wcoRechenregelUST = new WrapperComboBox();
	private WrapperComboBox wcoRechenregelSkonto = new WrapperComboBox();
	private WrapperComboBox wcoRechenregelBilanz = new WrapperComboBox();
	private WrapperLabel wlaStellen = new WrapperLabel();
	private WrapperButton wbuErgebnisgruppe = new WrapperButton();
	private WrapperTextField wtfErgebnisgruppe = new WrapperTextField();
	private WrapperCheckBox wcbVersteckt = null;
	private WrapperLabel wlaBemerkung = null;
	private WrapperEditorField wefBemerkung = null;

	protected WrapperGotoButton wbuPartner = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_PARTNER_AUSWAHL);

//	private WrapperCheckBox wcbReverseCharge = new WrapperCheckBox();
	private WrapperLabel wlaPlzOrt = new WrapperLabel();
	private WrapperLabel wlaUIDNr = new WrapperLabel();

	private WrapperComboBox wcoSortierung = new WrapperComboBox();
	private WrapperLabel wlaSortierung = new WrapperLabel();

	private WrapperLabel wlaWaehrung = new WrapperLabel();
	private WrapperComboBox wcoWaehrung = new WrapperComboBox();

	private WrapperTextField wtfEBGeschaeftsjahr = new WrapperTextField();
	private WrapperTextField wtfEBTAnlegen = new WrapperTextField();

	private WrapperRadioButton wrbSteuerartUst;
	private WrapperRadioButton wrbSteuerartVst;

	private JPanel jPanelWorkingOn;

//	private ReversechargeartDto rcArtOhne ;
	private WrapperLabel wlaSteuerkategorie = new WrapperLabel();
	private WrapperComboBox wcoSteuerkategorie = new WrapperComboBox();
	private Map<String, String> steuerkategorieMap;
	private PanelSelectReversechargeart panelReversechargeart;

	public PanelFinanzKontoKopfdaten(InternalFrame internalFrame, String add2TitleI, Object key,
			TabbedPaneKonten tabbedPaneKonten) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneKonten = tabbedPaneKonten;
		jbInit();
		initPanel();
		setDefaults();
		initComponents();
	}

	private void initPanel() throws Throwable {
		// rechte
		bIstModulKostenstelleInstalliert = true;
		wbuKostenstelle.setVisible(bIstModulKostenstelleInstalliert);
		wtfKostenstelleBezeichnung.setVisible(bIstModulKostenstelleInstalliert);
		wtfKostenstelleNummer.setVisible(bIstModulKostenstelleInstalliert);
		kontonummerMaxStellen = DelegateFactory.getInstance().getFinanzDelegate()
				.getAnzahlStellenVonKontoNummer(getTabbedPaneKonten().getKontotyp());
		((WrapperTextNumberField) wtfKontoNummer).setMaximumDigits(kontonummerMaxStellen.intValue());
		wlaStellen.setText("(" + kontonummerMaxStellen + " " + LPMain.getTextRespectUISPr("lp.stellen") + ")");
		if (!wcoWaehrung.isMapSet()) {
			wcoWaehrung.setMap(DelegateFactory.getInstance().getLocaleDelegate().getAllWaehrungen());
		}
	}

	/**
	 * Die zugehoerige TabbedPane holen.
	 * 
	 * @return TabbedPaneKonten
	 */
	public TabbedPaneKonten getTabbedPaneKonten() {
		return tabbedPaneKonten;
	}

	private void jbInit() throws Throwable {
//		rcArtOhne = getFinanzServiceDelegate().reversechargeartFindOhne() ;

		// von hier ...
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		JPanel panelButtonAction = getToolsPanel();

		steuerkategorieMap = getFinanzServiceDelegate().getAllSteuerkategorieCnr();
		wlaSteuerkategorie.setText(LPMain.getTextRespectUISPr("fb.steuerkategorie"));
		wcoSteuerkategorie.setMandatoryFieldDB(false);
		wcoSteuerkategorie.setMandatoryField(false);
		wcoSteuerkategorie.setMap(steuerkategorieMap);
		wcoSteuerkategorie.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (FinanzServiceFac.KONTOTYP_SACHKONTO.equals(getTabbedPaneKonten().getKontotyp())) {
					String cnr = (String) wcoSteuerkategorie.getKeyOfSelectedItem();
					setVisibleBesteuerung(cnr != null);
				}
			}
		});
		panelReversechargeart = new PanelSelectReversechargeart(getInternalFrame(), "");

		wdfGueltigVon.setActivatable(true);
		wdfGueltigVon.setMandatoryField(true);
		wtfKontoNummer = new WrapperTextNumberField();
		wtfKontoBezeichnung.setColumnsMax(FinanzFac.MAX_KONTO_BEZEICHNUNG);
		wtfKontoart.setActivatable(false);
		wtfKontoart.setMandatoryField(true);
		wtfUVAArt.setMandatoryField(true);
		wtfUstKontoNummer.setMandatoryField(false);

		getInternalFrame().addItemChangedListener(this);

		wlaKontoBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wlaStellen.setHorizontalAlignment(SwingConstants.LEFT);
		wlaStellen.setMinimumSize(new Dimension(130, 23));
		wlaStellen.setPreferredSize(new Dimension(130, 23));

		wbuKostenstelle.setText(LPMain.getTextRespectUISPr("button.kostenstelle"));
		wbuKostenstelle.setToolTipText(LPMain.getTextRespectUISPr("button.kostenstelle.tooltip"));
		wbuKostenstelle.addActionListener(this);
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
		wlaKontoNummer.setText(LPMain.getTextRespectUISPr("lp.kontonr"));
		wbuFinanzamt.setText(LPMain.getTextRespectUISPr("button.finanzamt"));
		wbuFinanzamt.setToolTipText(LPMain.getTextRespectUISPr("button.finanzamt.tooltip"));
		wbuFinanzamt.addActionListener(this);
		wbuFinanzamt.setActionCommand(ACTION_SPECIAL_FINANZAMT);
		wlaGueltigVon.setRequestFocusEnabled(true);
		wlaGueltigVon.setText(LPMain.getTextRespectUISPr("lp.gueltigab"));
		wcbAutomatikEroeffnung.setText(LPMain.getTextRespectUISPr("finanz.label.automatischeeroeffnungsbuchung"));
		wcbohneUst.setText(LPMain.getTextRespectUISPr("fb.label.ohneust"));
		wcbAllgemeinSichtbar.setText(LPMain.getTextRespectUISPr("finanz.label.allgemeinsichtbar"));
		wcbManuellbebuchbar.setText(LPMain.getTextRespectUISPr("finanz.label.manuellbebuchbar"));
		wbuUVAArt.setText(LPMain.getTextRespectUISPr("button.uvaart"));
		wbuUVAArt.setToolTipText(LPMain.getTextRespectUISPr("button.uvaart.tooltip"));
		wbuUVAArt.addActionListener(this);
		wbuUVAArt.setActionCommand(ACTION_SPECIAL_UVAART);

//		wbuSteuerkategorie.setText(LPMain
//				.getTextRespectUISPr("fb.steuerkategorie"));
//		wbuSteuerkategorie.addActionListener(this);
//		wbuSteuerkategorie.setActionCommand(ACTION_SPECIAL_STEUERKATEGORIE);

//		wbuSteuerkategorieReverse.setText(LPMain
//				.getTextRespectUISPr("fb.steuerkategoriereverse"));
//		wbuSteuerkategorieReverse.addActionListener(this);
//		wbuSteuerkategorieReverse
//				.setActionCommand(ACTION_SPECIAL_STEUERKATEGORIE_REVERSE);

		wlaUVAVariante.setText(LPMain.getTextRespectUISPr("fb.konto.uvavariante"));

		wcoUVAVariante.setMap(DelegateFactory.getInstance().getMandantDelegate().mwstsatzFindAllByMandant(), true);

		wtfUstKontoBezeichnung.setActivatable(false);
		wbuSkontoKonto.setText(LPMain.getTextRespectUISPr("button.skontokonto"));
		wbuSkontoKonto.setToolTipText(LPMain.getTextRespectUISPr("button.skontokonto.tooltip"));
		wbuSkontoKonto.addActionListener(this);
		wbuSkontoKonto.setActionCommand(ACTION_SPECIAL_SKONTOKONTO);
		wtfUstKontoNummer.setActivatable(false);
		wbuUstKonto.setText(LPMain.getTextRespectUISPr("button.ustkonto"));
		wbuUstKonto.setToolTipText(LPMain.getTextRespectUISPr("button.ustkonto.tooltip"));
		wbuUstKonto.addActionListener(this);
		wbuUstKonto.setActionCommand(ACTION_SPECIAL_USTKONTO);
		wtfSkontoKontoBezeichnung.setActivatable(false);
		wtfSkontoKontoNummer.setActivatable(false);
		wbuKontoart.setText(LPMain.getTextRespectUISPr("button.kontoart"));
		wbuKontoart.setToolTipText(LPMain.getTextRespectUISPr("button.kontoart.tooltip"));
		wbuKontoart.addActionListener(this);
		wbuKontoart.setActionCommand(ACTION_SPECIAL_KONTOART);
		wlaGueltigBis.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wtfKontoNummer.setMandatoryField(true);
		wtfKontoNummer.setActivatable(true);
		wtfKostenstelleNummer.setActivatable(false);
		wtfKontoBezeichnung.setMandatoryField(true);
		wtfKontoBezeichnung.setActivatable(true);
		wtfUVAArt.setActivatable(false);
		wtfFinanzamt.setMandatoryField(true);
		wtfFinanzamt.setActivatable(false);
		wdfGueltigBis.setActivatable(true);
		wlaAbstand.setText("  ");
		wtfKostenstelleBezeichnung.setActivatable(false);
		wbuErgebnisgruppe.setText(LPMain.getTextRespectUISPr("button.ergebnisgruppe"));
		wbuErgebnisgruppe.setToolTipText(LPMain.getTextRespectUISPr("button.ergebnisgruppe.tooltip"));
		wbuErgebnisgruppe.addActionListener(this);
		wbuErgebnisgruppe.setActionCommand(ACTION_SPECIAL_ERGEBNISGRUPPE);
		wbuBilanzgruppe.setText(LPMain.getTextRespectUISPr("button.bilanzgruppe"));

		wbuBilanzgruppeNegativ.setText(LPMain.getTextRespectUISPr("button.bilanzgruppenegativ"));
		wbuBilanzgruppe.addActionListener(this);
		wbuBilanzgruppe.setActionCommand(ACTION_SPECIAL_BILANZGRUPPE);

		wtfErgebnisgruppe.setActivatable(false);
		wtfBilanzgruppe.setActivatable(false);

		wbuBilanzgruppeNegativ.addActionListener(this);
		wbuBilanzgruppeNegativ.setActionCommand(ACTION_SPECIAL_BILANZGRUPPE_NEGATIV);
		wtfBilanzgruppeNegativ.setActivatable(false);

		wcbVersteckt = new WrapperCheckBox();
		wcbVersteckt.setText(LPMain.getTextRespectUISPr("lp.versteckt"));

		wcoSortierung.setMinimumSize(new Dimension(100, Defaults.getInstance().getControlHeight()));
		wcoSortierung.setPreferredSize(new Dimension(100, Defaults.getInstance().getControlHeight()));

		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));

		wlaBemerkung = new WrapperLabel(LPMain.getTextRespectUISPr("lp.bemerkung"));
		wlaBemerkung.setVerticalAlignment(SwingConstants.NORTH);
		wefBemerkung = new WrapperEditorField(getInternalFrame(), LPMain.getTextRespectUISPr("lp.bemerkung"));

		wlaWaehrung.setText(LPMain.getTextRespectUISPr("finanz.label.waehrungdruck"));
		wcoWaehrung.addActionListener(this);
		wcoWaehrung.setActionCommand(ACTION_SPECIAL_WAEHRUNGDRUCK);

		wrbSteuerartUst = new WrapperRadioButton(LPMain.getTextRespectUISPr("fb.label.ust"));
		wrbSteuerartVst = new WrapperRadioButton(LPMain.getTextRespectUISPr("fb.label.vst"));

//		wtfSteuerkategorie.setActivatable(false);
//		wtfSteuerkategorieReverse.setActivatable(false);

		ButtonGroup steuerart = new ButtonGroup();
		steuerart.add(wrbSteuerartUst);
		steuerart.add(wrbSteuerartVst);

		wtfEBGeschaeftsjahr.setActivatable(false);
		wtfEBTAnlegen.setActivatable(false);
		if (getTabbedPaneKonten().getKontotyp().equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
			wbuPartner = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_KUNDE_AUSWAHL);

		} else if (getTabbedPaneKonten().getKontotyp().equals(FinanzServiceFac.KONTOTYP_KREDITOR)) {
			wbuPartner = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_LIEFERANT_AUSWAHL);
		}

		jPanelWorkingOn = new JPanel(new MigLayout("wrap 4, hidemode 3", "[25%,fill|25%,fill|30%,fill|20%,fill]"));
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0)); // sonstige

		jPanelWorkingOn.add(wlaKontoNummer);
		jPanelWorkingOn.add(wtfKontoNummer);
		jPanelWorkingOn.add(wlaStellen);
		jPanelWorkingOn.add(wtfEBGeschaeftsjahr);

		if (FinanzServiceFac.KONTOTYP_SACHKONTO.equals(getTabbedPaneKonten().getKontotyp())) {
			jPanelWorkingOn.add(wlaKontoBezeichnung);
		} else {
			jPanelWorkingOn.add(wlaKontoBezeichnung, "split 2, grow");
			jPanelWorkingOn.add(wbuPartner.getWrapperButtonGoTo(), "grow 10");
		}
		jPanelWorkingOn.add(wtfKontoBezeichnung, "span, wrap");

		jPanelWorkingOn.add(wbuKontoart);
		jPanelWorkingOn.add(wtfKontoart, "span 2");
		jPanelWorkingOn.add(wlaBankInfo, "wrap");

		jPanelWorkingOn.add(wbuKostenstelle);
		jPanelWorkingOn.add(wtfKostenstelleNummer);
		jPanelWorkingOn.add(wtfKostenstelleBezeichnung, "span");

		jPanelWorkingOn.add(wbuUstKonto);
		jPanelWorkingOn.add(wtfUstKontoNummer);
		jPanelWorkingOn.add(wtfUstKontoBezeichnung);
		jPanelWorkingOn.add(wcoRechenregelUST);

		jPanelWorkingOn.add(wbuSkontoKonto);
		jPanelWorkingOn.add(wtfSkontoKontoNummer);
		jPanelWorkingOn.add(wtfSkontoKontoBezeichnung);
		jPanelWorkingOn.add(wcoRechenregelSkonto);

		jPanelWorkingOn.add(wbuErgebnisgruppe);
		jPanelWorkingOn.add(wtfErgebnisgruppe, "span");

		jPanelWorkingOn.add(wbuBilanzgruppe);
		jPanelWorkingOn.add(wtfBilanzgruppe);
		jPanelWorkingOn.add(wbuBilanzgruppeNegativ);
		jPanelWorkingOn.add(wtfBilanzgruppeNegativ);

		KontoDto kontoDto = getTabbedPaneKonten().getKontoDto();
		if (kontoDto != null && (kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_DEBITOR)
				|| kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_KREDITOR))) {
			wlaUIDNr.setHorizontalAlignment(SwingConstants.LEFT);

			jPanelWorkingOn.add(wlaPlzOrt);
			jPanelWorkingOn.add(wlaUIDNr);

//			wcbReverseCharge.setText(LPMain
//					.getTextRespectUISPr("lp.reversecharge"));
//			wcbReverseCharge.setActivatable(false);
//			jPanelWorkingOn.add(wcbReverseCharge, "span");
			jPanelWorkingOn.add(panelReversechargeart.getWlaReversechargeart(), "growx");
			jPanelWorkingOn.add(panelReversechargeart.getWcoReversechargeart(), "growx, span");
		}

		jPanelWorkingOn.add(wbuFinanzamt);
		jPanelWorkingOn.add(wtfFinanzamt, "span");

		jPanelWorkingOn.add(wbuUVAArt);
		jPanelWorkingOn.add(wtfUVAArt, "span 2");

		jPanelWorkingOn.add(wlaUVAVariante, "split2");
		jPanelWorkingOn.add(wcoUVAVariante, " w 200!");

		jPanelWorkingOn.add(wlaSteuerkategorie);
		jPanelWorkingOn.add(wcoSteuerkategorie, "span, split");
//		jPanelWorkingOn.add(wbuSteuerkategorie);
//		jPanelWorkingOn.add(wtfSteuerkategorie, "span, split");

//		jPanelWorkingOn.add(wlaBesteuerung, "skip, split");
		jPanelWorkingOn.add(wrbSteuerartVst);
		jPanelWorkingOn.add(wrbSteuerartUst, "wrap");

//		jPanelWorkingOn.add(wbuSteuerkategorieReverse, "newline");
//		jPanelWorkingOn.add(wtfSteuerkategorieReverse, "span");

		jPanelWorkingOn.add(wlaGueltigVon, "newline");
		jPanelWorkingOn.add(wdfGueltigVon);
		jPanelWorkingOn.add(wcbAutomatikEroeffnung);
		jPanelWorkingOn.add(wcbohneUst);

		jPanelWorkingOn.add(wlaGueltigBis, "newline");
		jPanelWorkingOn.add(wdfGueltigBis);
		jPanelWorkingOn.add(wcbAllgemeinSichtbar, "span");

		jPanelWorkingOn.add(wlaSortierung);
		jPanelWorkingOn.add(wcoSortierung);
		jPanelWorkingOn.add(wcbManuellbebuchbar, "span");

		jPanelWorkingOn.add(wlaWaehrung);
		jPanelWorkingOn.add(wcoWaehrung);
		jPanelWorkingOn.add(wcbVersteckt, "span, wrap");

		jPanelWorkingOn.add(wlaBemerkung, "top");
		jPanelWorkingOn.add(wefBemerkung, "span, pushy");

	}

	private void dto2ComponentsEB(KontoDto kontoDto) throws Throwable {
		if (kontoDto.getiGeschaeftsjahrEB() != null && kontoDto.gettEBAnlegen() != null) {
			String myInfo = kontoDto.getiGeschaeftsjahrEB().toString() + "/";
			myInfo += Helper.formatTimestamp(kontoDto.gettEBAnlegen(), DateFormat.MEDIUM, DateFormat.MEDIUM,
					Defaults.getInstance().getLocUI());
			String totalInfo = LPMain.getMessageTextRespectUISPr("lp.konto.automatischeeb", new Object[] { myInfo });
			wtfEBGeschaeftsjahr.setText(totalInfo);
		} else {
			wtfEBGeschaeftsjahr.setText("");
		}
		// if(kontoDto.getiGeschaeftsjahrEB() != null) {
		// wtfEBGeschaeftsjahr.setText(kontoDto.getiGeschaeftsjahrEB().toString())
		// ;
		// } else {
		// wtfEBGeschaeftsjahr.setText("") ;
		// }
		//
		// if(kontoDto.gettEBAnlegen() != null) {
		// wtfEBTAnlegen.setText(
		// Helper.formatDatum(kontoDto.gettEBAnlegen(),
		// Defaults.getInstance().getLocUI())) ;
		// } else {
		// wtfEBTAnlegen.setText("") ;
		// }
	}

	private void dto2ComponentsFinanzamt() throws Throwable {
		if (finanzamtDto != null) {
			// Partner holen
			partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
					.partnerFindByPrimaryKey(finanzamtDto.getPartnerIId());
			wtfFinanzamt.setText(partnerDto.formatFixTitelName1Name2());
			wcoSteuerkategorie.setActivatable(true);
			wcoSteuerkategorie.setEnabled(isLockedByMeOrForNew());

//			wbuSteuerkategorie.setActivatable(true);
//			wbuSteuerkategorie.setEnabled(isLocked());
//			wbuSteuerkategorieReverse.setActivatable(true);
//			wbuSteuerkategorieReverse.setEnabled(isLocked());
		} else {
			wtfFinanzamt.setText(null);
			wcoSteuerkategorie.setActivatable(false);
			wcoSteuerkategorie.setEnabled(false);
			;
//			wbuSteuerkategorie.setActivatable(false);
//			wbuSteuerkategorie.setEnabled(false);
//			wbuSteuerkategorieReverse.setActivatable(false);
//			wbuSteuerkategorieReverse.setEnabled(false);
		}
	}

	/**
	 * Traegt die Daten des UST Kontos ein.
	 */
	private void dto2ComponentsUSTKonto() throws Throwable {
		if (ustKontoDto != null) {
			wtfUstKontoNummer.setText(ustKontoDto.getCNr());
			wtfUstKontoBezeichnung.setText(ustKontoDto.getCBez());
		} else {
			wtfUstKontoNummer.setText(null);
			wtfUstKontoBezeichnung.setText(null);
		}
		// Rechenregel auswaehlbar
		LockStateValue lockstateValue = getLockedstateDetailMainKey();
		int iLockstate = lockstateValue.getIState();
		if (iLockstate == LOCK_FOR_NEW || iLockstate == LOCK_IS_LOCKED_BY_ME
				|| iLockstate == LOCK_IS_LOCKED_BY_OTHER_USER) {
			wcoRechenregelUST.setEnabled(ustKontoDto != null);
		}
	}

	/**
	 * Traegt die Daten fuer das Skontokonto ein.
	 */
	private void dto2ComponentsSkontoKonto() throws Throwable {
		if (skontoKontoDto != null) {
			wtfSkontoKontoNummer.setText(skontoKontoDto.getCNr());
			wtfSkontoKontoBezeichnung.setText(skontoKontoDto.getCBez());
		} else {
			wtfSkontoKontoNummer.setText(null);
			wtfSkontoKontoBezeichnung.setText(null);
		}
		// Rechenregel auswaehlbar
		LockStateValue lockstateValue = getLockedstateDetailMainKey();
		int iLockstate = lockstateValue.getIState();
		if (iLockstate == LOCK_FOR_NEW || iLockstate == LOCK_IS_LOCKED_BY_ME
				|| iLockstate == LOCK_IS_LOCKED_BY_OTHER_USER) {
			wcoRechenregelSkonto.setEnabled(skontoKontoDto != null);
		}
	}

	/**
	 * Traegt die Daten fuer die Kostenstelle ein.
	 */
	private void dto2ComponentsKostenstelle() {
		if (kostenstelleDto != null) {
			wtfKostenstelleNummer.setText(kostenstelleDto.getCNr());
			wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
		} else {
			wtfKostenstelleNummer.setText(null);
			wtfKostenstelleBezeichnung.setText(null);
		}
	}

	private void dto2ComponentsErgebnisgruppe() {
		wtfBilanzgruppeNegativ.setMandatoryField(false);
		if (ergebnisgruppeDto != null) {

			if (Helper.short2boolean(ergebnisgruppeDto.getBBilanzgruppe())) {
				wtfBilanzgruppe.setText(ergebnisgruppeDto.getCBez());
				wtfErgebnisgruppe.setText(null);

				if (ergebnisgruppeDto.getITyp() == FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_POSITIV) {
					wtfBilanzgruppeNegativ.setMandatoryField(true);
				}

			} else {
				wtfErgebnisgruppe.setText(ergebnisgruppeDto.getCBez());
				wtfBilanzgruppe.setText(null);
			}

		} else {
			wtfErgebnisgruppe.setText(null);
			wtfBilanzgruppe.setText(null);
		}
	}

	/**
	 * Traegt die Daten fuer die UVA Art ein.
	 * 
	 * @throws Throwable
	 */
	private void dto2ComponentsUVAArt() throws Throwable {
		if (uvaartDto != null) {
			String text = getFinanzServiceDelegate().uebersetzeUvaartOptimal(uvaartDto.getIId());
			wtfUVAArt.setText(text);
		} else {
			wtfUVAArt.setText(null);
		}
	}

	private void dto2ComponentsSteuerkategorie() throws Throwable {
		dto2ComponentsSteuerkategorie(getTabbedPaneKonten().getKontoDto());
	}
	
	private void dto2ComponentsSteuerkategorie(KontoDto kontoDto) throws Throwable {
		steuerkategorieCnr = kontoDto != null ? kontoDto.getSteuerkategorieCnr() : null;
		wcoSteuerkategorie.setKeyOfSelectedItem(steuerkategorieCnr);
		if (steuerkategorieCnr != null) {
//			wtfSteuerkategorie.setText(steuerkategorieCnr) ;
			boolean showSteuerart = FinanzServiceFac.KONTOTYP_SACHKONTO
					.equals(kontoDto.getKontotypCNr());
			setVisibleBesteuerung(showSteuerart);
		} else {
//			wtfSteuerkategorie.setText(null);
			setVisibleBesteuerung(false);
		}

//		if (steuerkategorieDto != null) {
//			wtfSteuerkategorie.setText(steuerkategorieDto.getCBez());
//			boolean showSteuerart = FinanzServiceFac.KONTOTYP_SACHKONTO
//					.equals(getTabbedPaneKonten().getKontoDto().getKontotypCNr());
//			setVisibleBesteuerung(showSteuerart);
//		} else {
//			wtfSteuerkategorie.setText(null);
//			setVisibleBesteuerung(false);
//		}
	}

	private void setVisibleBesteuerung(boolean visible) {
		wrbSteuerartUst.setVisible(visible);
		wrbSteuerartVst.setVisible(visible);
	}

//	private void dto2ComponentsSteuerkategorieReverse() throws Throwable {
//		if (steuerkategorieReverseDto != null) {
//			wtfSteuerkategorieReverse.setText(steuerkategorieReverseDto
//					.getCBez());
//		} else {
//			wtfSteuerkategorieReverse.setText(null);
//		}
//	}

	/**
	 * Traegt die Daten fuer die UVA Art ein.
	 * 
	 * @throws Throwable
	 */
	private void dto2ComponentsKontoart() throws Throwable {
		if (kontoartDto != null) {
			String text = getFinanzServiceDelegate().uebersetzeKontoartOptimal(kontoartDto.getCNr());
			wtfKontoart.setText(text);
		} else {
			wtfKontoart.setText(null);
		}
	}

	/**
	 * Loesche das Konto.
	 * 
	 * @param e                     ActionEvent
	 * @param bAdministrateLockKeyI boolean
	 * @param bNeedNoDeleteI        boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		KontoDto kontoDto = getTabbedPaneKonten().getKontoDto();
		if (kontoDto != null) {
			getFinanzDelegate().removeKonto(kontoDto);
			super.eventActionDelete(e, true, true);
		}
	}

	/**
	 * Drucken des Kontoblattes
	 * 
	 * @param e ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionPrint(ActionEvent e) throws Throwable {
		if (getTabbedPaneKonten().getKontoDto() != null) {
			String sTitle = getTabbedPaneKonten().getKontoDto().getCNr() + " "
					+ LPMain.getTextRespectUISPr("finanz.buchungen");
			getInternalFrame().showReportKriterien(
					new ReportBuchungenAufKonto(getInternalFrame(), getTabbedPaneKonten().getKontoDto(), sTitle));
		}
	}

	private boolean isSameSteuerkategorie() {
		KontoDto kontoDto = getTabbedPaneKonten().getKontoDto();
		if (steuerkategorieCnr == null) {
			return kontoDto.getSteuerkategorieCnr() == null;
		} else {
			return steuerkategorieCnr.equals(kontoDto.getSteuerkategorieCnr());
		}
	}

	/**
	 * Speichere Daten des Panels
	 * 
	 * @param e            ActionEvent
	 * @param bNeedNoSaveI boolean
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			if (null != getTabbedPaneKonten().getKontoDto()) {
				boolean finanzamtGeaendert = (finanzamtDto == null
						&& getTabbedPaneKonten().getKontoDto().getFinanzamtIId() != null)
						|| (finanzamtDto != null && getTabbedPaneKonten().getKontoDto().getFinanzamtIId() == null)
						|| (finanzamtDto != null && finanzamtDto.getPartnerIId()
								.compareTo(getTabbedPaneKonten().getKontoDto().getFinanzamtIId()) != 0);
				if (finanzamtGeaendert) {
					if (DelegateFactory.getInstance().getBuchenDelegate()
							.hatKontoBuchungen(getTabbedPaneKonten().getKontoDto().getIId())) {
						// wenn Buchungen am Konto dann ist das Aendern des
						// Finanzamt nicht erlaubt
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
								LPMain.getTextRespectUISPr("finanz.error.finanzamtaendern.nichterlaubt"));
						return;
					}
				}
				if (!getTabbedPaneKonten().getKontoDto().getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) {
//					boolean steuerkategorieGeaendert = (steuerkategorieDto == null && getTabbedPaneKonten()
//							.getKontoDto().getSteuerkategorieIId() != null)
//							|| (steuerkategorieDto != null && getTabbedPaneKonten()
//									.getKontoDto().getSteuerkategorieIId() == null)
//							|| (steuerkategorieDto != null && steuerkategorieDto.getIId().compareTo(
//									getTabbedPaneKonten().getKontoDto()
//											.getSteuerkategorieIId()) != 0);

					// reverse Kategorie nur geaendert wenn vorher nicht
					// null drinnen war
//					boolean steuerkategorieReverseGeaendert = (steuerkategorieReverseDto == null && getTabbedPaneKonten()
//							.getKontoDto().getSteuerkategorieIIdReverse() != null)
//							|| (steuerkategorieReverseDto != null && getTabbedPaneKonten()
//									.getKontoDto()
//									.getSteuerkategorieIIdReverse() == null)
//							|| (steuerkategorieReverseDto != null && steuerkategorieReverseDto.getIId().compareTo(
//									getTabbedPaneKonten().getKontoDto()
//											.getSteuerkategorieIIdReverse()) != 0);
//					steuerkategorieReverseGeaendert = getTabbedPaneKonten()
//							.getKontoDto().getSteuerkategorieIIdReverse() != null
//							&& steuerkategorieReverseGeaendert;

					if (!isSameSteuerkategorie()) {
						if (DelegateFactory.getInstance().getBuchenDelegate()
								.hatKontoBuchungen(getTabbedPaneKonten().getKontoDto().getIId())) {
							// wenn Buchungen am Konto dann ist das Aendern der
							// Steuerkategorie nicht erlaubt
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("finanz.error.steuerkategorieaendern.nichterlaubt"));
							return;
						}
					}
				}
			}

			if (wtfKontoNummer.getText().length() == kontonummerMaxStellen.intValue()) {
				components2Dto();
				KontoDto kontoDto = getTabbedPaneKonten().getKontoDto();
				kontoDto = getFinanzDelegate().updateKonto(kontoDto);
				setKeyWhenDetailPanel(kontoDto.getIId());
				super.eventActionSave(e, true);
				if (getInternalFrame().getKeyWasForLockMe() == null) {
					// der erste 1:n eintrag wurde angelegt den hauptkey locken.
					getInternalFrame().setKeyWasForLockMe(kontoDto.getIId().toString());
				}
				getTabbedPaneKonten().setKontoDto(kontoDto);
				eventYouAreSelected(false);
			} else {
				String sMsg = LPMain.getTextRespectUISPr("finanz.error.kontonummerstellen");
				Object[] pattern = { kontonummerMaxStellen };
				sMsg = MessageFormat.format(sMsg, pattern);
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"), sMsg);
			}
		}
	}

	private void components2Dto() throws Throwable {
		KontoDto kontoDto = getTabbedPaneKonten().getKontoDto();
		if (kontoDto == null) {
			kontoDto = new KontoDto();
			kontoDto.setMandantCNr(LPMain.getTheClient().getMandant());
		}
		if (wcoSortierung.getSelectedIndex() != 0) {
			kontoDto.setCsortierung(wcoSortierung.getSelectedItem().toString());
		} else {
			kontoDto.setCsortierung(null);
		}
		kontoDto.setCNr(wtfKontoNummer.getText());
		kontoDto.setKontotypCNr(getTabbedPaneKonten().getKontotyp());
		kontoDto.setCBez(wtfKontoBezeichnung.getText());

		kontoDto.setxBemerkung(wefBemerkung.getText());

		kontoDto.setMwstsatzIId((Integer) wcoUVAVariante.getKeyOfSelectedItem());

		if (finanzamtDto != null) {
			kontoDto.setFinanzamtIId(finanzamtDto.getPartnerIId());
		} else {
			kontoDto.setFinanzamtIId(null);
		}
		if (ustKontoDto != null) {
			kontoDto.setKontoIIdWeiterfuehrendUst(ustKontoDto.getIId());
		} else {
			kontoDto.setKontoIIdWeiterfuehrendUst(null);
		}
		if (skontoKontoDto != null) {
			kontoDto.setKontoIIdWeiterfuehrendSkonto(skontoKontoDto.getIId());
		} else {
			kontoDto.setKontoIIdWeiterfuehrendSkonto(null);
		}
		if (kontoartDto != null) {
			kontoDto.setKontoartCNr(kontoartDto.getCNr());
		} else {
			kontoDto.setKontoartCNr(null);
		}
		if (uvaartDto != null) {
			kontoDto.setUvaartIId(uvaartDto.getIId());
		} else {
			kontoDto.setUvaartIId(null);
		}
		if (kostenstelleDto != null) {
			kontoDto.setKostenstelleIId(kostenstelleDto.getIId());
		} else {
			kontoDto.setKostenstelleIId(null);
		}
		if (ergebnisgruppeDto != null) {
			kontoDto.setErgebnisgruppeIId(ergebnisgruppeDto.getIId());
		} else {
			kontoDto.setErgebnisgruppeIId(null);
		}

		if (bilanzgruppeDto_negativ != null) {
			kontoDto.setErgebnisgruppeIId_negativ(bilanzgruppeDto_negativ.getIId());
		} else {
			kontoDto.setErgebnisgruppeIId_negativ(null);
		}

//		if (steuerkategorieReverseDto != null) {
//			kontoDto.setSteuerkategorieIIdReverse(steuerkategorieReverseDto
//					.getIId());
//		} else {
//			kontoDto.setSteuerkategorieIIdReverse(null);
//		}
		kontoDto.setSteuerkategorieIIdReverse(null);
		kontoDto.setSteuerkategorieIId(null);

		steuerkategorieCnr = (String) wcoSteuerkategorie.getKeyOfSelectedItem();
		if (steuerkategorieCnr != null) {
			kontoDto.setcSteuerart(wrbSteuerartUst.isVisible()
					? (wrbSteuerartUst.isSelected() ? FinanzServiceFac.STEUERART_UST : FinanzServiceFac.STEUERART_VST)
					: null);
			kontoDto.setSteuerkategorieCnr(steuerkategorieCnr);
		} else {
			kontoDto.setcSteuerart(null);
			kontoDto.setSteuerkategorieCnr(null);
		}
		kontoDto.setRechenregelCNrWeiterfuehrendBilanz((String) wcoRechenregelBilanz.getKeyOfSelectedItem());
		kontoDto.setRechenregelCNrWeiterfuehrendSkonto((String) wcoRechenregelSkonto.getKeyOfSelectedItem());
		kontoDto.setRechenregelCNrWeiterfuehrendUst((String) wcoRechenregelUST.getKeyOfSelectedItem());

		kontoDto.setDGueltigvon(wdfGueltigVon.getDate());
		kontoDto.setDGueltigbis(wdfGueltigBis.getDate());
		kontoDto.setBAllgemeinsichtbar(Helper.boolean2Short(wcbAllgemeinSichtbar.isSelected()));
		kontoDto.setBAutomeroeffnungsbuchung(Helper.boolean2Short(wcbAutomatikEroeffnung.isSelected()));
		kontoDto.setBOhneUst(Helper.boolean2Short(wcbohneUst.isSelected()));
		kontoDto.setBVersteckt(Helper.boolean2Short(wcbVersteckt.isSelected()));
		kontoDto.setBManuellbebuchbar(Helper.boolean2Short(wcbManuellbebuchbar.isSelected()));
		if (wcoWaehrung.getSelectedItem().equals(wcoWaehrung.emptyEntry))
			kontoDto.setWaehrungCNrDruck(null);
		else
			kontoDto.setWaehrungCNrDruck((String) wcoWaehrung.getSelectedItem());
		getTabbedPaneKonten().setKontoDto(kontoDto);
	}

	/**
	 * Neu
	 * 
	 * @param eventObject ActionEvent
	 * @param bLockMeI    boolean
	 * @param bNeedNoNewI boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		getTabbedPaneKonten().setKontoDto(null);
		leereAlleFelder(this);
		this.ustKontoDto = null;
		this.skontoKontoDto = null;
		this.kontoartDto = null;
		this.kostenstelleDto = null;
		this.steuerkategorieCnr = null;

		setDefaultsForThisPanel();
		dto2ComponentsFinanzamt();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		wtfBilanzgruppeNegativ.setMandatoryField(false);
		if (!bNeedNoYouAreSelectedI) {
			KontoDto kontoDto = getTabbedPaneKonten().getKontoDto();
			if (kontoDto != null) {
				getTabbedPaneKonten().setKontoDto(getFinanzDelegate().kontoFindByPrimaryKey(kontoDto.getIId()));
				dto2Components();
			} else {
				// Neu
				// zuerst muss ein konto gewaehlt werden.
				this.wcoRechenregelBilanz.setEnabled(false);
				this.wcoRechenregelSkonto.setEnabled(false);
				this.wcoRechenregelUST.setEnabled(false);
			}
		}
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI) throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);
		this.eventYouAreSelected(false);
	}

	private void setDefaults() throws Throwable {
		// Berechtigungen
		boolean bVollversion = ((InternalFrameFinanz) getInternalFrame()).getBVollversion();

		boolean bChefbuchhalter = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER);

		String[] aWhichButtonIUse = null;
		if (bVollversion) {

			if (bChefbuchhalter) {
				aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD,
						ACTION_PRINT };
			} else {
				aWhichButtonIUse = new String[] { ACTION_PRINT };
			}
		} else {
			if (bChefbuchhalter) {
				aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD };
			} else {
				aWhichButtonIUse = new String[] {};
			}
		}

		TreeMap<String, String> tmSortierung = new TreeMap<String, String>();
		tmSortierung.put(FinanzFac.KONTO_SORTIERUNG_DATUM, LPMain.getTextRespectUISPr("lp.datum"));
		tmSortierung.put(FinanzFac.KONTO_SORTIERUNG_AUSZUG, LPMain.getTextRespectUISPr("label.auszug"));
		tmSortierung.put(FinanzFac.KONTO_SORTIERUNG_BELEG, LPMain.getTextRespectUISPr("lp.beleg"));

		wcoSortierung.setMap(tmSortierung);

		enableToolsPanelButtons(aWhichButtonIUse);
		// Sichtbarkeit der controls
		if (!bVollversion) {
			// wbuKontoart.setVisible(false);
			// wtfKontoart.setVisible(false);
			wbuKostenstelle.setVisible(false);
			wtfKostenstelleNummer.setVisible(false);
			wtfKostenstelleBezeichnung.setVisible(false);
			// wbuUstKonto.setVisible(false);
			// wtfUstKontoNummer.setVisible(false);
			// wtfUstKontoBezeichnung.setVisible(false);
			wcoRechenregelUST.setVisible(false);
			wbuSkontoKonto.setVisible(false);
			wtfSkontoKontoNummer.setVisible(false);
			wtfSkontoKontoBezeichnung.setVisible(false);
			wcoRechenregelSkonto.setVisible(false);
			wcoRechenregelBilanz.setVisible(false);
			wbuErgebnisgruppe.setVisible(false);
			wtfErgebnisgruppe.setVisible(false);
			wbuBilanzgruppe.setVisible(false);
			wtfBilanzgruppe.setVisible(false);
			wbuBilanzgruppeNegativ.setVisible(false);
			wtfBilanzgruppeNegativ.setVisible(false);
			wtfErgebnisgruppe.setVisible(false);
			wbuUVAArt.setVisible(false);
			wtfUVAArt.setVisible(false);

			wlaUVAVariante.setVisible(false);
			wcoUVAVariante.setVisible(false);

			wcbohneUst.setVisible(false);
			// wbuFinanzamt.setVisible(false);
			// wtfFinanzamt.setVisible(false);
			wlaGueltigVon.setVisible(false);
			wlaGueltigBis.setVisible(false);
			wdfGueltigVon.setVisible(false);
			wdfGueltigBis.setVisible(false);
			wdfGueltigVon.setMandatoryField(false);
			wcbAllgemeinSichtbar.setVisible(false);
			wcbAutomatikEroeffnung.setVisible(false);
			wcbManuellbebuchbar.setVisible(false);
			wtfUVAArt.setMandatoryField(false);
			wlaSortierung.setVisible(false);
			wcoSortierung.setVisible(false);
			// wtfFinanzamt.setMandatoryField(false);
		}
//		boolean bFibumodul = false;
//		try {
//			bFibumodul = DelegateFactory
//					.getInstance()
//					.getMandantDelegate()
//					.darfAnwenderAufModulZugreifen(
//							LocaleFac.BELEGART_FINANZBUCHHALTUNG);
//		} catch (Throwable e) {
//			//
//		}

		if (bVollversion) {
			wbuUstKonto.setVisible(false);
			wtfUstKontoNummer.setVisible(false);
			wtfUstKontoBezeichnung.setVisible(false);
			wcoRechenregelUST.setVisible(false);

			wbuSkontoKonto.setVisible(false);
			wtfSkontoKontoNummer.setVisible(false);
			wtfSkontoKontoBezeichnung.setVisible(false);
			wcoRechenregelSkonto.setVisible(false);
			wcoRechenregelBilanz.setVisible(false);
		} else {
			wbuUstKonto.setVisible(true);
			wtfUstKontoNummer.setVisible(true);
			wtfUstKontoBezeichnung.setVisible(true);
			wcoRechenregelUST.setVisible(true);

			wbuSkontoKonto.setVisible(true);
			wtfSkontoKontoNummer.setVisible(true);
			wtfSkontoKontoBezeichnung.setVisible(true);
			wcoRechenregelSkonto.setVisible(true);
			wcoRechenregelBilanz.setVisible(true);
		}

		uvaartDto = getDefaultUvaartDto();
		dto2ComponentsUVAArt();
	}

	private UvaartDto getDefaultUvaartDto() throws Throwable {
		return getFinanzServiceDelegate().uvaartFindByCnr(FinanzServiceFac.UVAART_NICHT_ZUTREFFEND);
	}

	private void setDefaultsForThisPanel() throws com.lp.client.frame.ExceptionLP {

		wcoSteuerkategorie.setSelectedIndex(0);
		Map<?, ?> maRechenregeln = getFinanzDelegate().getAllRechenregel();
		wcoRechenregelBilanz.setMap(maRechenregeln);
		wcoRechenregelSkonto.setMap(maRechenregeln);
		wcoRechenregelUST.setMap(maRechenregeln);
		wcbAllgemeinSichtbar.setSelected(true);
		wcbAutomatikEroeffnung.setSelected(true);
		wcbohneUst.setSelected(false);
		try {
			uvaartDto = getDefaultUvaartDto();
			dto2ComponentsUVAArt();
		} catch (Throwable e) {
			// TODO: ana: ist hier ein catch fuer die UVA-Art notwendig?
		}

		setDefaultsGueltigkeit();
		if (finanzamtDto != null)
			wtfFinanzamt.setText(finanzamtDto.getPartnerDto().formatName());
		if (printKontoart != true) {
			wbuKontoart.setVisible(false);
			wtfKontoart.setVisible(false);
			wtfKontoart.setMandatoryField(false);
		}

//		boolean bFibumodul = false;
//		try {
//			bFibumodul = DelegateFactory
//					.getInstance()
//					.getMandantDelegate()
//					.darfAnwenderAufModulZugreifen(
//							LocaleFac.BELEGART_FINANZBUCHHALTUNG);
//		} catch (Throwable e) {
//			//
//		}

		boolean bFibuModul = ((InternalFrameFinanz) getInternalFrame()).getBVollversion();
		if (bFibuModul) {
			wbuUstKonto.setVisible(false);
			wtfUstKontoNummer.setVisible(false);
			wtfUstKontoBezeichnung.setVisible(false);
			wcoRechenregelUST.setVisible(false);

			wbuSkontoKonto.setVisible(false);
			wtfSkontoKontoNummer.setVisible(false);
			wtfSkontoKontoBezeichnung.setVisible(false);
			wcoRechenregelSkonto.setVisible(false);
			wcoRechenregelBilanz.setVisible(false);
		} else {
			wbuUstKonto.setVisible(true);
			wtfUstKontoNummer.setVisible(true);
			wtfUstKontoBezeichnung.setVisible(true);
			wcoRechenregelUST.setVisible(true);

			wbuSkontoKonto.setVisible(true);
			wtfSkontoKontoNummer.setVisible(true);
			wtfSkontoKontoBezeichnung.setVisible(true);
			wcoRechenregelSkonto.setVisible(true);
			wcoRechenregelBilanz.setVisible(true);
		}
	}

	void dialogQueryKontoart(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, };
		QueryType[] qt = null;
		FilterKriterium[] filters = null;
		panelQueryFLRKontoart = new PanelQueryFLR(qt, filters, QueryParameters.UC_ID_KONTOART, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr("finanz.liste.kontoarten"));
		if (this.kontoartDto != null) {
			panelQueryFLRKontoart.setSelectedId(kontoartDto.getCNr());
		}
		new DialogQuery(panelQueryFLRKontoart);
	}

	void dialogQueryUstKonto(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };
		QueryType[] qt = null;
		// nur Sachkonten dieses Mandanten
		FilterKriterium[] filters = FinanzFilterFactory.getInstance().createFKSachkonten();
		panelQueryFLRUstKonto = new PanelQueryFLR(qt, filters, QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr("finanz.liste.sachkonten"));
		FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().createFKDKontonummer();
		FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().createFKDKontobezeichnung();
		panelQueryFLRUstKonto.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);
		if (ustKontoDto != null) {
			panelQueryFLRUstKonto.setSelectedId(ustKontoDto.getIId());
		}
		new DialogQuery(panelQueryFLRUstKonto);
	}

	void dialogQuerySkontoKonto(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN, };
		QueryType[] qt = null;
		// nur Sachkonten dieses Mandanten
		FilterKriterium[] filters = FinanzFilterFactory.getInstance().createFKSachkonten();
		panelQueryFLRSkontoKonto = new PanelQueryFLR(qt, filters, QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr("finanz.liste.sachkonten"));
		FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().createFKDKontonummer();
		FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().createFKDKontobezeichnung();
		panelQueryFLRSkontoKonto.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);
		if (skontoKontoDto != null) {
			panelQueryFLRSkontoKonto.setSelectedId(skontoKontoDto.getIId());
		}
		new DialogQuery(panelQueryFLRSkontoKonto);
	}

	void dialogQueryUvaArt(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, };
		QueryType[] qt = null;
		FilterKriterium[] fkneu = new FilterKriterium[1];

		fkneu[0] = new FilterKriterium("mandantCNr", true, "'" + LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		panelQueryFLRUvaArt = new PanelQueryFLR(qt, fkneu, QueryParameters.UC_ID_UVAART, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr("finanz.liste.uvaarten"));
		if (this.uvaartDto != null) {
			panelQueryFLRUvaArt.setSelectedId(uvaartDto.getCNr());
		}
		new DialogQuery(panelQueryFLRUvaArt);
	}

//	void dialogQuerySteuerkategorie(ActionEvent e, boolean reverseCharge)
//			throws Throwable {
//		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,  PanelBasis.ACTION_LEEREN};
//
//		FilterKriterium[] fkneu = new FilterKriterium[] {
//				new FilterKriterium("mandant_c_nr", true, "'"
//						+ LPMain.getTheClient().getMandant() + "'",
//						FilterKriterium.OPERATOR_EQUAL, false),
//				new FilterKriterium("finanzamt_i_id", true, ""
//						+ finanzamtDto.getPartnerIId().intValue(),
//						FilterKriterium.OPERATOR_EQUAL, false),
//				new FilterKriterium("reversechargeart_i_id", true, 
//						"" + rcArtOhne.getIId(), FilterKriterium.OPERATOR_EQUAL, false)
//		};
//
//		PanelQueryFLR queryFlr = new PanelQueryFLR(null, fkneu,
//				QueryParameters.UC_ID_STEUERKATEGORIE, aWhichButtonIUse,
//				getInternalFrame(), LPMain.getTextRespectUISPr(
//						"fb.steuerkategorie"));
//		panelQueryFLRSteuerkategorie = queryFlr;
//		if (steuerkategorieDto != null) {
//			panelQueryFLRSteuerkategorie.setSelectedId(steuerkategorieDto
//					.getIId());
//		}
////		if (reverseCharge) {
////			panelQueryFLRSteuerkategorieReverse = queryFlr;
////			if (steuerkategorieReverseDto != null)
////				panelQueryFLRSteuerkategorieReverse
////						.setSelectedId(steuerkategorieReverseDto.getIId());
////		} else {
////			panelQueryFLRSteuerkategorie = queryFlr;
////			if (steuerkategorieDto != null)
////				panelQueryFLRSteuerkategorie.setSelectedId(steuerkategorieDto
////						.getIId());
////		}
//		new DialogQuery(queryFlr);
//	}

	void dialogQueryFinanzamt(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, };
		QueryType[] qt = null;
		// Filter nach Mandant
		FilterKriterium[] filters = SystemFilterFactory.getInstance().createFKMandantCNr();

		panelQueryFLRFinanzamt = new PanelQueryFLR(qt, filters, QueryParameters.UC_ID_FINANZAMT, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr("finanz.liste.finanzaemter"));
		if (this.finanzamtDto != null) {
			panelQueryFLRFinanzamt.setSelectedId(finanzamtDto.getPartnerIId());
		}
		new DialogQuery(panelQueryFLRFinanzamt);
	}

	void dialogQueryErgebnisgruppe(ActionEvent e, boolean bBilanzgruppe) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN, };
		QueryType[] qt = null;
		// Filter nach Mandant
		FilterKriterium[] filters = FinanzFilterFactory.getInstance().createFKErgebnisgruppe(bBilanzgruppe);

		String title = LPMain
				.getTextRespectUISPr(bBilanzgruppe ? "finanz.liste.bilanzgruppen" : "finanz.liste.ergebnisgruppen");

		panelQueryFLRErgebnisgruppe = new PanelQueryFLR(qt, filters, QueryParameters.UC_ID_ERGEBNISGRUPPE,
				aWhichButtonIUse, getInternalFrame(), title);
		if (ergebnisgruppeDto != null) {
			panelQueryFLRErgebnisgruppe.setSelectedId(ergebnisgruppeDto.getIId());
		}
		new DialogQuery(panelQueryFLRErgebnisgruppe);
	}

	void dialogQueryBilanzgruppeNegativ(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN, };
		QueryType[] qt = null;
		// Filter nach Mandant
		FilterKriterium[] filters = FinanzFilterFactory.getInstance().createFKBilanzgruppenNegativ();

		String title = null;

		title = LPMain.getTextRespectUISPr("finanz.liste.bilanzgruppen");

		panelQueryFLRBilanzgruppeNegativ = new PanelQueryFLR(qt, filters, QueryParameters.UC_ID_ERGEBNISGRUPPE,
				aWhichButtonIUse, getInternalFrame(), title);

		new DialogQuery(panelQueryFLRBilanzgruppeNegativ);
	}

	void dialogQueryKostenstelle(ActionEvent e) throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance().createPanelFLRKostenstelle(getInternalFrame(),
				false, false);
		if (kostenstelleDto != null) {
			panelQueryFLRKostenstelle.setSelectedId(kostenstelleDto.getIId());
		}
		new DialogQuery(panelQueryFLRKostenstelle);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_KONTOART)) {
			dialogQueryKontoart(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_USTKONTO)) {
			dialogQueryUstKonto(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SKONTOKONTO)) {
			dialogQuerySkontoKonto(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_BILANZGRUPPE)) {
			dialogQueryErgebnisgruppe(e, true);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_BILANZGRUPPE_NEGATIV)) {

			if (ergebnisgruppeDto == null
					|| ergebnisgruppeDto.getITyp() != FinanzFac.ERGEBNISGRUPPE_TYP_BILANZGRUPPE_POSITIV) {
				// Warnung anzeigen
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("fb.negativebilanzgruppeauswaehlen.error"));
			} else {
				dialogQueryBilanzgruppeNegativ(e);
			}

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_UVAART)) {
			dialogQueryUvaArt(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FINANZAMT)) {
			dialogQueryFinanzamt(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ERGEBNISGRUPPE)) {
			dialogQueryErgebnisgruppe(e, false);
//		} else if (e.getActionCommand().equals(ACTION_SPECIAL_STEUERKATEGORIE)) {
//			dialogQuerySteuerkategorie(e, false);
//		} else if (e.getActionCommand().equals(
//				ACTION_SPECIAL_STEUERKATEGORIE_REVERSE)) {
//			dialogQuerySteuerkategorie(e, true);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRFinanzamt) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					finanzamtDto = getFinanzDelegate().finanzamtFindByPrimaryKey((Integer) key,
							LPMain.getTheClient().getMandant());
				} else {
					finanzamtDto = null;
				}
				this.dto2ComponentsFinanzamt();
				steuerkategorieAnpassenAnFinanzamt(finanzamtDto);
			} else if (e.getSource() == panelQueryFLRKontoart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					kontoartDto = getFinanzServiceDelegate().kontoartFindByPrimaryKey((String) key);
				} else {
					kontoartDto = null;
				}
				dto2ComponentsKontoart();
			} else if (e.getSource() == panelQueryFLRKostenstelle) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
							.kostenstelleFindByPrimaryKey((Integer) key);
				} else {
					kostenstelleDto = null;
				}
				dto2ComponentsKostenstelle();
			} else if (e.getSource() == panelQueryFLRSkontoKonto) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					skontoKontoDto = getFinanzDelegate().kontoFindByPrimaryKeySmall((Integer) key);
				} else {
					skontoKontoDto = null;
				}
				dto2ComponentsSkontoKonto();
			} else if (e.getSource() == panelQueryFLRUstKonto) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					ustKontoDto = getFinanzDelegate().kontoFindByPrimaryKeySmall((Integer) key);
				} else {
					ustKontoDto = null;
				}
				dto2ComponentsUSTKonto();
			} else if (e.getSource() == panelQueryFLRUvaArt) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					uvaartDto = getFinanzServiceDelegate().uvaartFindByPrimaryKey((Integer) key);
				} else {
					uvaartDto = null;
				}
				dto2ComponentsUVAArt();
//			} else if (e.getSource() == panelQueryFLRSteuerkategorie) {
//				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
//				if (key != null) {
//					steuerkategorieDto = getFinanzServiceDelegate()
//							.steuerkategorieFindByPrimaryKey((Integer) key);
//				} else {
//					steuerkategorieDto = null;
//				}
//				dto2ComponentsSteuerkategorie();
//			} else if (e.getSource() == panelQueryFLRSteuerkategorieReverse) {
//				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
//				if (key != null) {
//					steuerkategorieReverseDto = getFinanzServiceDelegate()
//							.steuerkategorieFindByPrimaryKey((Integer) key);
//				} else {
//					steuerkategorieReverseDto = null;
//				}
//				dto2ComponentsSteuerkategorieReverse();
			} else if (e.getSource() == panelQueryFLRErgebnisgruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					ergebnisgruppeDto = getFinanzDelegate().ergebnisgruppeFindByPrimaryKey((Integer) key);
				} else {
					ergebnisgruppeDto = null;
				}
				dto2ComponentsErgebnisgruppe();

				wtfBilanzgruppeNegativ.setText(null);
				bilanzgruppeDto_negativ = null;

			} else if (e.getSource() == panelQueryFLRBilanzgruppeNegativ) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				bilanzgruppeDto_negativ = getFinanzDelegate().ergebnisgruppeFindByPrimaryKey((Integer) key);
				wtfBilanzgruppeNegativ.setText(bilanzgruppeDto_negativ.getCBez());

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRKostenstelle) {
				kostenstelleDto = null;
				dto2ComponentsKostenstelle();
			} else if (e.getSource() == panelQueryFLRSkontoKonto) {
				skontoKontoDto = null;
				dto2ComponentsSkontoKonto();
			} else if (e.getSource() == panelQueryFLRUstKonto) {
				ustKontoDto = null;
				dto2ComponentsUSTKonto();
			} else if (e.getSource() == panelQueryFLRErgebnisgruppe) {
				ergebnisgruppeDto = null;
				dto2ComponentsErgebnisgruppe();
//			} else if (e.getSource() == panelQueryFLRSteuerkategorie) {
//				steuerkategorieDto = null;
//				dto2ComponentsSteuerkategorie();
			}
		}
	}

	private void steuerkategorieAnpassenAnFinanzamt(FinanzamtDto finanzamtDtoI) throws Throwable {
		if (finanzamtDtoI == null) {
			steuerkategorieCnr = null;
		} else {
//			if (steuerkategorieDto != null) {
//				if (!steuerkategorieDto.getFinanzamtIId().equals(
//						finanzamtDtoI.getPartnerIId())
//						|| !(steuerkategorieDto.getMandantCNr()
//								.equals(finanzamtDtoI.getMandantCNr()))) {
//					try {
//						steuerkategorieDto = DelegateFactory
//								.getInstance()
//								.getFinanzServiceDelegate()
//								.steuerkategorieFindByCNrFinanzamtIId(
//										steuerkategorieDto.getCNr(),
//										finanzamtDtoI.getPartnerIId());
//					} catch (Throwable e) {
//						steuerkategorieDto = null;
//					}
//				}
//			}

//			if (steuerkategorieReverseDto != null) {
//				if (!steuerkategorieReverseDto.getFinanzamtIId().equals(
//						finanzamtDtoI.getPartnerIId())
//						|| !(steuerkategorieReverseDto.getMandantCNr()
//								.equals(finanzamtDtoI.getMandantCNr()))) {
//					try {
//						steuerkategorieReverseDto = DelegateFactory
//								.getInstance()
//								.getFinanzServiceDelegate()
//								.steuerkategorieFindByCNrFinanzamtIId(
//										steuerkategorieReverseDto.getCNr(),
//										finanzamtDtoI.getPartnerIId());
//					} catch (Throwable e) {
//						steuerkategorieReverseDto = null;
//					}
//				}
//			}
		}
		dto2ComponentsSteuerkategorie();
//		dto2ComponentsSteuerkategorieReverse();
	}

	public String getLockMeWer() {
		return HelperClient.LOCKME_FINANZ_KONTO;
	}

	private void dto2Components() throws Throwable {
		KontoDto kontoDto = getTabbedPaneKonten().getKontoDto();
		dto2Components(kontoDto);
	}
	
	private void dto2Components(KontoDto kontoDto) throws Throwable {
		wbuPartner.setOKey(null);
		if (kontoDto != null) {
			wefBemerkung.setText(kontoDto.getxBemerkung());
			wdfGueltigBis.setDate(kontoDto.getDGueltigbis());
			wdfGueltigVon.setDate(kontoDto.getDGueltigvon());

			dto2ComponentsEB(kontoDto);

			if (kontoDto.getCsortierung() != null) {
				wcoSortierung.setSelectedItem(kontoDto.getCsortierung());
			} else {
				wcoSortierung.setSelectedIndex(0);
			}

			// Finanzamt
			if (kontoDto.getFinanzamtIId() != null) {
				finanzamtDto = getFinanzDelegate().finanzamtFindByPrimaryKey(kontoDto.getFinanzamtIId(),
						kontoDto.getMandantCNr());
			} else {
				finanzamtDto = null;
			}
			dto2ComponentsFinanzamt();
			// UST-Konto
			if (kontoDto.getKontoIIdWeiterfuehrendUst() != null) {
				ustKontoDto = getFinanzDelegate().kontoFindByPrimaryKeySmall(kontoDto.getKontoIIdWeiterfuehrendUst());
			} else {
				ustKontoDto = null;
			}
			dto2ComponentsUSTKonto();
			// Skontokonto
			if (kontoDto.getKontoIIdWeiterfuehrendSkonto() != null) {
				skontoKontoDto = getFinanzDelegate()
						.kontoFindByPrimaryKeySmall(kontoDto.getKontoIIdWeiterfuehrendSkonto());
			} else {
				skontoKontoDto = null;
			}
			dto2ComponentsSkontoKonto();

			if (kontoDto.getErgebnisgruppeIId_negativ() != null) {
				bilanzgruppeDto_negativ = getFinanzDelegate()
						.ergebnisgruppeFindByPrimaryKey(kontoDto.getErgebnisgruppeIId_negativ());
				wtfBilanzgruppeNegativ.setText(bilanzgruppeDto_negativ.getCBez());
			} else {
				bilanzgruppeDto_negativ = null;
				wtfBilanzgruppeNegativ.setText(null);
			}

			// UVA Art
			if (kontoDto.getUvaartIId() != null) {
				uvaartDto = getFinanzServiceDelegate().uvaartFindByPrimaryKey(kontoDto.getUvaartIId());
			} else {
				uvaartDto = null;
			}
			dto2ComponentsUVAArt();
			// Kontoart
			if (kontoDto.getKontoartCNr() != null) {
				kontoartDto = getFinanzServiceDelegate().kontoartFindByPrimaryKey(kontoDto.getKontoartCNr());
			} else {
				kontoartDto = null;
			}
			dto2ComponentsKontoart();
			// Kostenstelle
			if (kontoDto.getKostenstelleIId() != null) {
				kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
						.kostenstelleFindByPrimaryKey(kontoDto.getKostenstelleIId());
			} else {
				kostenstelleDto = null;
			}
			dto2ComponentsKostenstelle();

			boolean isUst = FinanzServiceFac.STEUERART_UST.equals(kontoDto.getcSteuerart());
			if (isUst)
				wrbSteuerartUst.setSelected(true);
			else
				wrbSteuerartVst.setSelected(true);

			if (kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
				KundeDto kundeDto[] = DelegateFactory.getInstance().getKundeDelegate()
						.kundefindByKontoIIdDebitorenkonto(kontoDto.getIId());

				if (kundeDto != null && kundeDto.length > 0) {

					wbuPartner.setOKey(kundeDto[0].getIId());

					PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
							.partnerFindByPrimaryKey(kundeDto[0].getPartnerIId());

					if (partnerDto.getCUid() != null) {
						wlaUIDNr.setText(LPMain.getTextRespectUISPr("lp.uid") + ": " + partnerDto.getCUid());
					} else {
						wlaUIDNr.setText(LPMain.getTextRespectUISPr("lp.uid") + ": ");
					}

					String sammelkonto = "";

					if (kundeDto.length > 1) {
						sammelkonto = LPMain.getTextRespectUISPr("fb.konto.sammelkonto") + " ";

					}

					if (partnerDto.getLandplzortDto() != null) {
						wlaPlzOrt.setText(sammelkonto + partnerDto.getLandplzortDto().getLandDto().getCLkz() + " "
								+ partnerDto.getLandplzortDto().getCPlz() + " "
								+ partnerDto.getLandplzortDto().getOrtDto().getCName());
					} else {
						wlaPlzOrt.setText(sammelkonto);
					}

//					wcbReverseCharge.setShort(kundeDto[0].getBReversecharge());
					panelReversechargeart.getWcoReversechargeart()
							.setKeyOfSelectedItem(kundeDto[0].getReversechargeartId());
				} else {
					wlaPlzOrt.setText(null);
					wlaUIDNr.setText(null);
//					wcbReverseCharge.setSelected(false);
					panelReversechargeart.getWcoReversechargeart().setSelectedIndex(0);
				}
			}
			if (kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_KREDITOR)) {
				LieferantDto[] lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantfindByKontoIIdKreditorenkonto(kontoDto.getIId());

				if (lieferantDto != null && lieferantDto.length > 0) {

					PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
							.partnerFindByPrimaryKey(lieferantDto[0].getPartnerIId());

					wbuPartner.setOKey(lieferantDto[0].getIId());

					if (partnerDto.getCUid() != null) {
						wlaUIDNr.setText(LPMain.getTextRespectUISPr("lp.uid") + ": " + partnerDto.getCUid());
					} else {
						wlaUIDNr.setText(LPMain.getTextRespectUISPr("lp.uid") + ": ");
					}

					String sammelkonto = "";

					if (lieferantDto.length > 1) {
						sammelkonto = LPMain.getTextRespectUISPr("fb.konto.sammelkonto") + " ";

					}

					if (partnerDto.getLandplzortDto() != null) {
						wlaPlzOrt.setText(sammelkonto + partnerDto.getLandplzortDto().getLandDto().getCLkz() + " "
								+ partnerDto.getLandplzortDto().getCPlz() + " "
								+ partnerDto.getLandplzortDto().getOrtDto().getCName());
					} else {
						wlaPlzOrt.setText(sammelkonto);
					}

//					wcbReverseCharge
//							.setSelected(Helper.short2boolean(lieferantDto[0]
//									.getBReversecharge()));
					panelReversechargeart.getWcoReversechargeart()
							.setKeyOfSelectedItem(lieferantDto[0].getReversechargeartId());
// TODO: ghp, 29.01.2015 Das ist so Bloedsinn. Reversechargeart hat mit der Steuerkategorie nichts zu tun 					
//					wcoSteuerkategorie.setKeyOfSelectedItem(
//							lieferantDto[0].getReversechargeartId());
				} else {
					wlaPlzOrt.setText(null);
					wlaUIDNr.setText(null);
//					wcbReverseCharge.setSelected(false);
					panelReversechargeart.getWcoReversechargeart().setSelectedIndex(0);
				}
			}
			if (kontoDto.getWaehrungCNrDruck() == null)
				wcoWaehrung.setSelectedItem(wcoWaehrung.emptyEntry);
			else
				wcoWaehrung.setSelectedItem(kontoDto.getWaehrungCNrDruck());

			wlaUIDNr.setText(wlaUIDNr.getText() + " " + LPMain.getTextRespectUISPr("lp.kurzbez") + ": "
					+ kontoDto.getPartnerKurzbezeichnung());

			// Ergebnisgruppe
			if (kontoDto.getErgebnisgruppeIId() != null) {
				ergebnisgruppeDto = DelegateFactory.getInstance().getFinanzDelegate()
						.ergebnisgruppeFindByPrimaryKey(kontoDto.getErgebnisgruppeIId());
			} else {
				ergebnisgruppeDto = null;
			}
			dto2ComponentsErgebnisgruppe();
			// Steuerkategorie
			steuerkategorieCnr = kontoDto.getSteuerkategorieCnr();

//			if (kontoDto.getSteuerkategorieIId() != null) {
//				steuerkategorieDto = DelegateFactory
//						.getInstance()
//						.getFinanzServiceDelegate()
//						.steuerkategorieFindByPrimaryKey(
//								kontoDto.getSteuerkategorieIId());
//			} else {
//				steuerkategorieDto = null;
//			}

//			if (kontoDto.getSteuerkategorieIIdReverse() != null) {
//				steuerkategorieReverseDto = DelegateFactory
//						.getInstance()
//						.getFinanzServiceDelegate()
//						.steuerkategorieFindByPrimaryKey(
//								kontoDto.getSteuerkategorieIIdReverse());
//			} else {
//				steuerkategorieReverseDto = null;
//			}
//			dto2ComponentsSteuerkategorieReverse();
			// Allgemein sichtbar?
			wcbAllgemeinSichtbar.setSelected(Helper.short2boolean(kontoDto.getBAllgemeinsichtbar()));
			wcbAutomatikEroeffnung.setSelected(Helper.short2boolean(kontoDto.getBAutomeroeffnungsbuchung()));
			wcbohneUst.setSelected(Helper.short2boolean(kontoDto.getBOhneUst()));
			wcbManuellbebuchbar.setSelected(Helper.short2boolean(kontoDto.getBManuellbebuchbar()));
			wcbVersteckt.setSelected(Helper.short2boolean(kontoDto.getBVersteckt()));

			wcoUVAVariante.setKeyOfSelectedItem(kontoDto.getMwstsatzIId());
			wtfKontoNummer.setText(kontoDto.getCNr());

			wtfKontoBezeichnung.setText(kontoDto.getCBez());

			if (kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) {
				BankverbindungDto bvDto = getFinanzDelegate().bankverbindungFindByKontoIIdOhneExc(kontoDto.getIId());
				String kontoBezeichnung = "";
				if (bvDto != null) {
					kontoBezeichnung += LPMain.getTextRespectUISPr("part.kund.banken") + ": "
							+ DelegateFactory.getInstance().getPartnerbankDelegate()
									.bankFindByPrimaryKey(bvDto.getBankIId()).getPartnerDto()
									.getCName1nachnamefirmazeile1();
				}

				wlaBankInfo.setText(kontoBezeichnung);

			} else {
				wlaBankInfo.setText("");
			}

			Map<?, ?> maRechenregeln = getFinanzDelegate().getAllRechenregel();

			wcoRechenregelBilanz.setMap(maRechenregeln);
			wcoRechenregelSkonto.setMap(maRechenregeln);
			wcoRechenregelUST.setMap(maRechenregeln);
			wcoRechenregelBilanz.setKeyOfSelectedItem(kontoDto.getRechenregelCNrWeiterfuehrendBilanz());
			wcoRechenregelSkonto.setKeyOfSelectedItem(kontoDto.getRechenregelCNrWeiterfuehrendSkonto());
			wcoRechenregelUST.setKeyOfSelectedItem(kontoDto.getRechenregelCNrWeiterfuehrendUst());
			setStatusbarModification(kontoDto);

			if (kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_DEBITOR)
					|| kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_KREDITOR)) {
				wtfKostenstelleBezeichnung.setVisible(false);
				wtfKostenstelleBezeichnung.setVisible(false);
				wtfKostenstelleNummer.setVisible(false);
				wtfUstKontoBezeichnung.setVisible(false);
				wtfUstKontoNummer.setVisible(false);
				wtfSkontoKontoBezeichnung.setVisible(false);
				wtfSkontoKontoNummer.setVisible(false);
				wtfUVAArt.setVisible(false);
				wtfUstKontoBezeichnung.setVisible(false);
				wtfUstKontoNummer.setVisible(false);
				wbuKostenstelle.setVisible(false);
				wbuSkontoKonto.setVisible(false);
				wbuUstKonto.setVisible(false);
				wbuUVAArt.setVisible(false);
				wcoRechenregelBilanz.setVisible(false);
				wcoRechenregelSkonto.setVisible(false);
				wcoRechenregelUST.setVisible(false);
				wbuErgebnisgruppe.setVisible(false);
				wtfErgebnisgruppe.setVisible(false);
				wbuBilanzgruppe.setVisible(false);
				wtfBilanzgruppe.setVisible(false);

				wbuBilanzgruppeNegativ.setVisible(false);
				wtfBilanzgruppeNegativ.setVisible(false);

				wtfErgebnisgruppe.setVisible(false);
				wcoSteuerkategorie.setMandatoryField(true);
				wcoSteuerkategorie.setMap(steuerkategorieMap);
//				wtfSteuerkategorie.setMandatoryField(true);
//				wtfSteuerkategorieReverse.setMandatoryField(true);
//				wtfSteuerkategorieReverse.setVisible(true);
				wtfUVAArt.setMandatoryField(false);
				wtfKontoart.setVisible(false);
				wtfKontoart.setMandatoryField(false);
				wbuKontoart.setVisible(false);
				wcbohneUst.setVisible(false);

				wlaUVAVariante.setVisible(false);
				wcoUVAVariante.setVisible(false);

			} else {
				wcoSteuerkategorie.setMandatoryField(false);
				wcoSteuerkategorie.setMap(steuerkategorieMap);

//				wbuSteuerkategorieReverse.setVisible(false);
//				wtfSteuerkategorieReverse.setVisible(false);
				wtfKontoart.setVisible(true);
				wtfKontoart.setMandatoryField(true);

				wbuKontoart.setVisible(true);
				// TODO: CG UST + Skonto bei Fibu
			}
			dto2ComponentsSteuerkategorie(kontoDto);
			dto2ComponentsUVAArt();
		}
	}

	private FinanzDelegate getFinanzDelegate() throws ExceptionLP {
		return DelegateFactory.getInstance().getFinanzDelegate();
	}

	private FinanzServiceDelegate getFinanzServiceDelegate() throws Throwable {
		return DelegateFactory.getInstance().getFinanzServiceDelegate();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfKontoNummer;
	}

	public boolean handleOwnException(ExceptionLP ex) throws Throwable {
		// if (ex.getICode() == EJBExceptionLP.FEHLER_FINANZ_ZYKEL_BILANZ) {
		// dto2ComponentsBilanzKonto();
		// }
		return false;
	}

	public void setPrintKontoart(Boolean printKontoartnow) {
		printKontoart = printKontoartnow;
	}

	private void setDefaultsGueltigkeit() {
		try {
			int geschaeftsjahr = DelegateFactory.getInstance().getParameterDelegate().getGeschaeftsjahr();
			GeschaeftsjahrMandantDto gjDto = DelegateFactory.getInstance().getSystemDelegate()
					.geschaeftsjahrFindByPrimaryKey(geschaeftsjahr);
			wdfGueltigVon.setDate(new java.sql.Date(gjDto.getDBeginndatum().getTime()));
		} catch (Throwable e) {
			wdfGueltigVon.setDate(new java.sql.Date(System.currentTimeMillis()));
		}
		wdfGueltigBis.setDate(null);
	}
	
	public void setDefaultsAusKonto(KontoDto sourceKontoDto) throws Throwable {
		dto2Components(sourceKontoDto);
		setDefaultsGueltigkeit();
	}
}
