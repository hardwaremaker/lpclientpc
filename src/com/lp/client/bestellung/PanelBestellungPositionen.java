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
package com.lp.client.bestellung;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import com.lp.client.artikel.ReportArtikelstatistik;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.HvActionEvent;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionen2;
import com.lp.client.frame.component.PanelPositionenArtikelEinkauf;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.pc.PasteBuffer;
import com.lp.client.util.IconFactory;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellpositionServiceDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.inserat.service.InseratDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

/**
 * 
 * <p>
 * Diese Klasse kuemmert sich um BesPos.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Josef Ornetsmueller; dd.mm.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/02/05 09:15:20 $
 */
public class PanelBestellungPositionen extends PanelPositionen2 {
	private static final long serialVersionUID = 1543377035804752974L;

	private final TabbedPaneBestellung tPBes;
	private BestellpositionDto besPosDto = null;

	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "action_los_from_liste";
	static final private String ACTION_SPECIAL_MONATSSTATISTIK = "action_special_monatsstatikstik";
	static final private String ACTION_SPECIAL_SPLIT = "action_special_split";
	static final private String ACTION_SPECIAL_STAFFELPREIS_VERWENDEN = "ACTION_SPECIAL_STAFFELPREIS_VERWENDEN";

	static final private String ACTION_SPECIAL_ETIKETTDRUCKEN = "action_special_etikettdrucken";

	private PanelQueryFLR panelQueryFLRLos = null;
	private PanelQueryFLR panelQueryFLRLossollmaterial = null;
	// Wenn eine freie Bestellposition bei einer Abrufbestellung zu einem Rahmen
	// erfasst wird, den Benutzer einmalig warnen
	/**
	 * @todo MB hab ich mal hier her verschoben - schaut schraeg aus
	 */
	// private boolean bWarnungAusgesprochen = false;
	private WrapperDateField wdfUebersteuerterLiefertermin = null;
	private WrapperLabel wlaUebersteuerterLiefertermin = null;

	protected WrapperLabel wlaVerpackungseinheit = null;
	protected WrapperNumberField wnfVerpackungseinheit = null;

	protected WrapperLabel wlaMindestbestellmenge = null;
	protected WrapperNumberField wnfMindestbestellmenge = null;
	private WrapperLabel wlaMindestbestellmengeEinheit = null;

	private WrapperLabel wlaFixkosten = null;
	private WrapperNumberField wnfFixkosten = null;
	private WrapperLabel wlaFixkostenEinheit = null;

	private WrapperCheckBox wcoArtikelnummerBezInStaffelRueckpflegen = new WrapperCheckBox();

	private WrapperCheckBox wcoWEPInfoAnAnforderer = new WrapperCheckBox();
	private WrapperCheckBox wcoWEPInfoAnAnfordererHandeingabe = new WrapperCheckBox();

	private JPanel jPanelStaffel = null;

	private WrapperLabel wlaLieferantArtikelBezeichnung = null;
	public WrapperTextField wtfLieferantArtikelBezeichnung = null;

	private WrapperLabel wlaLieferantArtikelNummer = null;
	public WrapperTextField wtfLieferantArtikelNummer = null;

	private WrapperLabel wlaLieferantArtikelBezeichnungHandeingabe = null;
	public WrapperTextField wtfLieferantArtikelBezeichnungHandeingabe = null;

	private WrapperLabel wlaLieferantArtikelNummerHandeingabe = null;
	public WrapperTextField wtfLieferantArtikelNummerHandeingabe = null;

	private WrapperRadioButton wrbPreiseNichtNachpflegen = null;
	private WrapperRadioButton wcbEinzelpreisNachpflegen = null;
	private WrapperRadioButton wcbPreisstaffelNachpflegen = null;
	private ButtonGroup jbgNachpflegen = null;

	private WrapperComboBox wcoStaffelRueckpflege = null;
	protected WrapperButton wbuStaffelVerwenden = null;

	private WrapperLabel wlaPreisnachpflege = null;
	private JPanel jPanelPreisnachpflege = null;

	private long tBefore = 0;

	/**
	 * @todo hier kein ArtikelDto - bitte wie in den anderen Modulen behandeln.
	 */
	protected ArtikelDto oArtikelDto = null;

	protected ArtikellieferantDto artLiefDto = null;

	// TODO internal frame wird schon in PanelBasis gespeichert!
	private InternalFrameBestellung intFrame = null;

	private WrapperLabel wlaAngebotnummer = null;
	private WrapperTextField wtfAngebotnummer = null;

	private WrapperButton wbuLossollarbeitsplan = new WrapperButton();
	private WrapperTextField wtfLossollarbeitsplan = new WrapperTextField();

	private WrapperSelectField wsfKunde = new WrapperSelectField(WrapperSelectField.KUNDE, getInternalFrame(), false);
	private WrapperSelectField wsfKundeHandeingabe = new WrapperSelectField(WrapperSelectField.KUNDE,
			getInternalFrame(), false);

	private final FocusAdapterPreis preisFocusAdapter;

	public PanelBestellungPositionen(InternalFrame internalFrameI, String add2TitleI, Object keyI) throws Throwable {
		super(internalFrameI, add2TitleI, keyI, PanelPositionen2.TYP_PANELPREISEINGABE_ARTIKELBESTELLUNG);
		tPBes = getInternalFrameBes().getTabbedPaneBestellung();
		preisFocusAdapter = new FocusAdapterPreis(this);
		jbInitPanel();
		initComponents();
		initPanel();

		revalidate();
	}

	private TabbedPaneBestellung getTPBes() {
		return tPBes;
	}

	private InternalFrameBestellung getInternalFrameBes() {
		if (intFrame == null) {
			intFrame = (InternalFrameBestellung) getInternalFrame();
		}
		return intFrame;
	}

	private void jbInitPanel() throws Throwable {

		resetToolsPanel();

		panelArtikel.wnfMenge.addFocusListener(new FocusAdapterMenge(this));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = null;

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF)) {
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT,
					ACTION_TEXT, ACTION_MEDIA };
		} else {
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, ACTION_TEXT, ACTION_MEDIA };
		}

		enableToolsPanelButtons(aWhichButtonIUse);

		wlaLieferantArtikelBezeichnung = new WrapperLabel(
				LPMain.getTextRespectUISPr("bes.artikelbezeichnungbeimlieferanten"));
		wlaLieferantArtikelNummer = new WrapperLabel(LPMain.getTextRespectUISPr("bes.lieferantartikelnummer"));

		wcoWEPInfoAnAnforderer.setText(LPMain.getTextRespectUISPr("artikel.wepinfo"));
		wcoWEPInfoAnAnforderer.setToolTipText(LPMain.getTextRespectUISPr("artikel.wepinfo.ananforderer"));

		wcoArtikelnummerBezInStaffelRueckpflegen
				.setText(LPMain.getTextRespectUISPr("bes.position.staffelrueckpflegen"));
		wcoArtikelnummerBezInStaffelRueckpflegen
				.setToolTipText(LPMain.getTextRespectUISPr("bes.position.staffelrueckpflegen.tooltip"));

		wcoWEPInfoAnAnfordererHandeingabe.setText(LPMain.getTextRespectUISPr("artikel.wepinfo"));
		wcoWEPInfoAnAnfordererHandeingabe.setToolTipText(LPMain.getTextRespectUISPr("artikel.wepinfo.ananforderer"));

		wlaLieferantArtikelBezeichnungHandeingabe = new WrapperLabel(
				LPMain.getTextRespectUISPr("bes.artikelbezeichnungbeimlieferanten"));
		wlaLieferantArtikelNummerHandeingabe = new WrapperLabel(
				LPMain.getTextRespectUISPr("bes.lieferantartikelnummer"));

		wtfLieferantArtikelBezeichnung = new WrapperTextField();
		wtfLieferantArtikelBezeichnung.setActivatable(true);
		wtfLieferantArtikelBezeichnung.setColumnsMax(80);
		wtfLieferantArtikelNummer = new WrapperTextField();
		wtfLieferantArtikelNummer.setActivatable(true);

		wtfLieferantArtikelBezeichnungHandeingabe = new WrapperTextField();
		wtfLieferantArtikelBezeichnungHandeingabe.setActivatable(true);
		wtfLieferantArtikelBezeichnungHandeingabe.setColumnsMax(80);
		wtfLieferantArtikelNummerHandeingabe = new WrapperTextField();
		wtfLieferantArtikelNummerHandeingabe.setActivatable(true);

		wlaUebersteuerterLiefertermin = new WrapperLabel(LPMain.getTextRespectUISPr("bes.bestellpositiontermin"));
		wdfUebersteuerterLiefertermin = new WrapperDateField();
		wdfUebersteuerterLiefertermin.setActivatable(true);

		panelHandeingabe.setVisibleZeileZusatzrabattsumme(false);
		panelHandeingabe.setVisibleZeileMwstsumme(false);
		panelHandeingabe.setVisibleZeileBruttogesamtpreis(false);

		wtfLossollarbeitsplan.setActivatable(false);
		wbuLossollarbeitsplan.setText(LPMain.getTextRespectUISPr("fert.tab.unten.los.title") + "...");
		wbuLossollarbeitsplan.setActionCommand(ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLossollarbeitsplan.addActionListener(this);

		refreshAktuellesArtikelLFPanel();

		refreshPreisnachpflegePanel();

		// jPanelPreisnachpflege.setVisible(false);

		wlaAngebotnummer = new WrapperLabel(LPMain.getTextRespectUISPr("anf.angebotnummer"));
		wtfAngebotnummer = new WrapperTextField();
		getInternalFrame().addItemChangedListener(this);
		// ab hier einhaengen.

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_KUNDE_JE_BESTELLPOSITION)) {
			wsfKunde.setMandatoryField(true);
			panelArtikel.add(wsfKunde, new GridBagConstraints(0, panelArtikel.iZeileNettopreis, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			panelArtikel.add(wsfKunde.getWrapperTextField(), new GridBagConstraints(1, panelArtikel.iZeileNettopreis, 3,
					1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			wsfKundeHandeingabe.setMandatoryField(true);
			panelHandeingabe.add(wsfKundeHandeingabe, new GridBagConstraints(0, panelHandeingabe.iZeileNettopreis, 1, 1,
					0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			panelHandeingabe.add(wsfKundeHandeingabe.getWrapperTextField(),
					new GridBagConstraints(1, panelHandeingabe.iZeileNettopreis, 3, 1, 0.0, 0.0,
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		}

		int iZeile = panelHandeingabe.iZeileNettopreis + 1;
		panelHandeingabe.add(wlaLieferantArtikelNummerHandeingabe, new GridBagConstraints(2, iZeile, 2, 1, 0.1, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		panelHandeingabe.add(wtfLieferantArtikelNummerHandeingabe, new GridBagConstraints(4, iZeile, 3, 1, 0.1, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		iZeile++;
		panelHandeingabe.add(wlaLieferantArtikelBezeichnungHandeingabe, new GridBagConstraints(2, iZeile, 2, 1, 0.1,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		panelHandeingabe.add(wtfLieferantArtikelBezeichnungHandeingabe, new GridBagConstraints(4, iZeile, 3, 1, 0.1,
				0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		panelHandeingabe.add(wcoWEPInfoAnAnfordererHandeingabe, new GridBagConstraints(7, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 80, 0));

		iZeile++;

		panelArtikel.add(wlaUebersteuerterLiefertermin, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panelArtikel.add(wdfUebersteuerterLiefertermin, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		panelArtikel.add(wlaLieferantArtikelNummer, new GridBagConstraints(2, iZeile, 2, 1, 0.1, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		panelArtikel.add(wtfLieferantArtikelNummer, new GridBagConstraints(4, iZeile, 3, 1, 0.1, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		panelArtikel.add(wcoArtikelnummerBezInStaffelRueckpflegen, new GridBagConstraints(7, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		iZeile++;

		panelArtikel.add(wlaAngebotnummer, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panelArtikel.add(wtfAngebotnummer, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		panelArtikel.add(wlaLieferantArtikelBezeichnung, new GridBagConstraints(2, iZeile, 2, 1, 0.1, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		panelArtikel.add(wtfLieferantArtikelBezeichnung, new GridBagConstraints(4, iZeile, 3, 1, 0.1, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		panelArtikel.add(wcoWEPInfoAnAnforderer, new GridBagConstraints(7, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		iZeile++;
		panelArtikel.add(jPanelPreisnachpflege, new GridBagConstraints(0, iZeile, 5, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		panelArtikel.add(jPanelStaffel, new GridBagConstraints(6, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(7, 0, 0, 0), 0, 0));

		// PJ 16215
		iZeile++;
		panelArtikel.add(wbuLossollarbeitsplan, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panelArtikel.add(wtfLossollarbeitsplan, new GridBagConstraints(1, iZeile, 6, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		iZeile++;
		add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		this.createAndSaveAndShowButton("/com/lp/client/res/chart.png",
				LPMain.getTextRespectUISPr("lp.statistik.monate"), ACTION_SPECIAL_MONATSSTATISTIK, null);

		this.createAndSaveAndShowButton("/com/lp/client/res/documents.png",
				LPMain.getTextRespectUISPr("bes.bestellposition.split"), ACTION_SPECIAL_SPLIT, null);

		this.createAndSaveAndShowButton("/com/lp/client/res/printer216x16.png",
				LPMain.getTextRespectUISPr("artikel.report.etikett.shortcut"), ACTION_SPECIAL_ETIKETTDRUCKEN,
				KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
				RechteFac.RECHT_BES_BESTELLUNG_CUD);

	}

	public BestellpositionDto getBestellpositionDto() {
		return besPosDto;
	}

	/**
	 * refreshStaffel
	 * 
	 * @throws Throwable
	 */
	private void refreshAktuellesArtikelLFPanel() throws Throwable {

		if (jPanelStaffel == null) {
			jPanelStaffel = new JPanel(new GridBagLayout());
			wlaVerpackungseinheit = new WrapperLabel(LPMain.getTextRespectUISPr("bes.verpackungseinheit.short"));
			wlaVerpackungseinheit.addMouseListener(new MouseAdapterChangeMenge(this));
			wnfVerpackungseinheit = new WrapperNumberField();
			wnfVerpackungseinheit.setActivatable(false);

			wlaMindestbestellmenge = new WrapperLabel(LPMain.getTextRespectUISPr("bes.mindestbestellmenge"));
			wlaMindestbestellmenge.addMouseListener(new MouseAdapterChangeMenge(this));
			/*
			 * wlaMindestbestellmenge.setToolTipText(
			 * LPMain.getInstance().getTextRespectUISPr("bes.menge.tooltip"));
			 */
			wnfMindestbestellmenge = new WrapperNumberField();
			wnfMindestbestellmenge.setActivatable(false);
			wlaMindestbestellmengeEinheit = new WrapperLabel();
			wlaMindestbestellmengeEinheit.setHorizontalAlignment(SwingConstants.LEFT);

			wlaFixkosten = new WrapperLabel(LPMain.getTextRespectUISPr("lp.fixkosten"));
			wnfFixkosten = new WrapperNumberField();
			wnfFixkosten.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());

			wlaFixkostenEinheit = new WrapperLabel();
			wlaFixkostenEinheit.setHorizontalAlignment(SwingConstants.LEFT);

			final int iNFSmaller = -80;
			// 1. zeile
			jPanelStaffel.add(wlaFixkosten, new GridBagConstraints(1, 0, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

			jPanelStaffel.add(wnfFixkosten, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), iNFSmaller, 0));

			jPanelStaffel.add(wlaFixkostenEinheit, new GridBagConstraints(3, 0, 1, 1, 0.1, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

			jPanelStaffel.add(wlaMindestbestellmenge, new GridBagConstraints(1, 1, 1, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
			jPanelStaffel.add(wnfMindestbestellmenge, new GridBagConstraints(2, 1, 1, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), iNFSmaller, 0));

			jPanelStaffel.add(wlaMindestbestellmengeEinheit, new GridBagConstraints(3, 1, 1, 1, 0.1, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

			jPanelStaffel.add(wlaVerpackungseinheit, new GridBagConstraints(1, 2, 1, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

			jPanelStaffel.add(wnfVerpackungseinheit, new GridBagConstraints(2, 2, 1, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), iNFSmaller, 0));

		}
	}

	public void refreshPreisnachpflegePanel() throws Throwable {
		if (jPanelPreisnachpflege == null) {
			wrbPreiseNichtNachpflegen = new WrapperRadioButton(
					LPMain.getTextRespectUISPr("bes.preise_nicht_nachpflegen"));
			wcbEinzelpreisNachpflegen = new WrapperRadioButton(LPMain.getTextRespectUISPr("bes.ezpreis_nachpflegen"));
			wcbPreisstaffelNachpflegen = new WrapperRadioButton(
					LPMain.getTextRespectUISPr("bes.staffelpreis_nachpflegen"));

			wcbEinzelpreisNachpflegen.addActionListener(this);
			wcbPreisstaffelNachpflegen.addActionListener(this);
			wrbPreiseNichtNachpflegen.addActionListener(this);

			jbgNachpflegen = new ButtonGroup();
			jbgNachpflegen.add(wrbPreiseNichtNachpflegen);
			jbgNachpflegen.add(wcbEinzelpreisNachpflegen);
			jbgNachpflegen.add(wcbPreisstaffelNachpflegen);

			wbuStaffelVerwenden = new WrapperButton();
			wcoStaffelRueckpflege = new WrapperComboBox();
			if (getInternalFrame().bRechtDarfPreiseAendernEinkauf == false) {
				wrbPreiseNichtNachpflegen.setActivatable(false);
				wcbEinzelpreisNachpflegen.setActivatable(false);
				wcbPreisstaffelNachpflegen.setActivatable(false);
				wcoStaffelRueckpflege.setActivatable(false);
				wcoStaffelRueckpflege.setActivatable(false);
			}

			// sei default
			wrbPreiseNichtNachpflegen.setSelected(true);

			wlaPreisnachpflege = new WrapperLabel(LPMain.getTextRespectUISPr("bes.nachpflegen"));

			wlaPreisnachpflege.setHorizontalAlignment(SwingConstants.LEFT);
			jPanelPreisnachpflege = new JPanel(new GridBagLayout());

			jPanelPreisnachpflege.add(wlaPreisnachpflege, new GridBagConstraints(0, 0, 1, 1, 0.2, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelPreisnachpflege.add(wrbPreiseNichtNachpflegen, new GridBagConstraints(1, 0, 1, 1, 0.03, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelPreisnachpflege.add(wcbEinzelpreisNachpflegen, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelPreisnachpflege.add(wcbPreisstaffelNachpflegen, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			jPanelPreisnachpflege.add(wcoStaffelRueckpflege, new GridBagConstraints(2, 2, 1, 1, 0.25, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			wbuStaffelVerwenden.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/document_into.png")));
			wbuStaffelVerwenden
					.setToolTipText(LPMain.getTextRespectUISPr("bes.bestellposition.staffelpreis.verwenden.tooltip"));

			wbuStaffelVerwenden.setActionCommand(ACTION_SPECIAL_STAFFELPREIS_VERWENDEN);
			wbuStaffelVerwenden.addActionListener(this);

			jPanelPreisnachpflege.add(wbuStaffelVerwenden, new GridBagConstraints(3, 2, 1, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 20, 0));

		}

		if (artLiefDto != null && artLiefDto.getIId() != null) {
			wcoStaffelRueckpflege.setMap(DelegateFactory.getInstance().getArtikelDelegate()
					.getAlleGueltigenStaffelneinesLieferanten(artLiefDto.getIId(), tPBes.getBesDto().getWaehrungCNr(),
							tPBes.getBesDto().getDBelegdatum()));
		} else {
			wcoStaffelRueckpflege.setMap(new TreeMap());
		}
		wcoStaffelRueckpflege.setEmptyEntry("Positionsmenge");

		try {
			if (getInternalFrame().bRechtDarfPreiseAendernEinkauf == true) {
				ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate()
						.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_BESTELLUNG,
								ParameterFac.PARAMETER_DEFAULT_EINKAUFSPREIS_ZURUECKPFLEGEN);
				Integer b = (Integer) parameter.getCWertAsObject();
				if (b == 0) {
					wrbPreiseNichtNachpflegen.setSelected(true);
				} else if (b == 1) {
					wcbPreisstaffelNachpflegen.setSelected(true);
				} else if (b == 2) {
					wcbEinzelpreisNachpflegen.setSelected(true);
				}
			}
		} catch (Throwable e) {
			handleException(e, true);
		}
	}

	private BestellungDto getBestellungDto() {
		return getTPBes().getBesDto();
	}

	/**
	 * Eine Position loeschen. <br>
	 * Tag buttondelete:
	 * 
	 * @param e                     Ereignis
	 * @param bAdministrateLockKeyI flag
	 * @param bNeedNoDeleteI        boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		this.wtfLieferantArtikelBezeichnung.setText(null);
		this.wtfLieferantArtikelNummer.setText(null);

		if (getTPBes().istAktualisierenBestellungErlaubt(false)) {
			// Es darf nur geloescht werden wenn noch keine WEs vorhanden

			Object[] o = intFrame.getTabbedPaneBestellung().getPanelBestellungPositionenTopQP3().getSelectedIds();
			if (o != null) {
				for (int i = 0; i < o.length; i++) {

					BestellpositionServiceDto besPosDtoToRemove = DelegateFactory.getInstance().getBestellungDelegate()
							.bestellpositionFindByPrimaryKey((Integer) o[i], true);
					WareneingangspositionDto[] wePos = DelegateFactory.getInstance().getWareneingangDelegate()
							.wareneingangspositionFindByBestellpositionIId(besPosDtoToRemove.getPositionDto().getIId());
					if (wePos.length > 0) {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
								LPMain.getTextRespectUISPr("bestellung.delete.esgibtnochwes"));
					} else {

						// Rahmenpositionen duerfen nur veraendert oder geloescht
						// werden,
						// wenn es
						// noch keine Abrufe dazu gibt
						if (DelegateFactory.getInstance().getBestellungDelegate()
								.bestellpositionFindByBestellpositionIIdRahmenposition(
										besPosDtoToRemove.getPositionDto().getIId()).length > 0) {

							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
									LPMain.getTextRespectUISPr("bes.rahmenpositionnichtloeschen"));
						} else {
							if (getTPBes().getBesDto().getBestellungartCNr()
									.equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
								if (besPosDtoToRemove.getPositionDto().getIBestellpositionIIdRahmenposition() != null) {
									DelegateFactory.getInstance().getBestellungDelegate()
											.removeAbrufbestellposition(besPosDtoToRemove.getPositionDto());
								} else {
									DelegateFactory.getInstance().getBestellungDelegate()
											.removeBestellposition(besPosDtoToRemove.getPositionDto());
									this.setKeyWhenDetailPanel(null);
								}
							} else {

								// PJ 17873 Auf Inserate pruefen
								InseratDto inseratDto = DelegateFactory.getInstance().getInseratDelegate()
										.istIseratAufBestellpositionVorhanden(besPosDto.getIId());
								if (inseratDto != null) {

									if (inseratDto.getStatusCNr().equals(LocaleFac.STATUS_ERSCHIENEN)
											|| inseratDto.getStatusCNr().equals(LocaleFac.STATUS_VERRECHENBAR)
											|| inseratDto.getStatusCNr().equals(LocaleFac.STATUS_GESTOPPT)
											|| inseratDto.getStatusCNr().equals(LocaleFac.STATUS_VERRECHNET)
											|| inseratDto.getStatusCNr().equals(LocaleFac.STATUS_TEILBEZAHLT)
											|| inseratDto.getStatusCNr().equals(LocaleFac.STATUS_BEZAHLT)) {
										// Loeschen nicht mehr moeglich

										DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
												LPMain.getTextRespectUISPr(
														"iv.bestellposition.ab.erschienen.nichtloeschbar") + " "
														+ inseratDto.getCNr());
										return;

									}

									// Wenn Status Bestellt, dann Verknuepfungen
									// aufloesen und loeschen

									DelegateFactory.getInstance().getInseratDelegate()
											.beziehungZuBestellpositionAufloesenUndBestellpositionenLoeschen(
													besPosDtoToRemove.getPositionDto().getIId());
									this.setKeyWhenDetailPanel(null);

								} else {
									DelegateFactory.getInstance().getBestellungDelegate()
											.removeBestellposition(besPosDtoToRemove.getPositionDto());
									this.setKeyWhenDetailPanel(null);
								}
							}

						}
					}

				}
			}
			super.eventActionDelete(e, false, false); // keyWasForLockMe
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {

		refreshMyComponents();

		if (getTPBes().istAktualisierenBestellungErlaubt(true)) {
			if (canChangeBestellpositionBeiNew()) {

				// Rahmenpositionen duerfen nur veraendert oder geloescht
				// werden, wenn es
				// noch keine Abrufe dazu gibt
				if (DelegateFactory.getInstance().getBestellungDelegate()
						.bestellpositionFindByBestellpositionIIdRahmenposition(besPosDto.getIId()).length > 0) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("bes.rahmenpositionnichtaendern"));
				}

				if (!isAmountValid(besPosDto.getNMenge(), getBestellungDto())) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
							LPMain.getTextRespectUISPr("bestellung.warning.nullmengebeistatusoffennichterlaubt"));
					return;
				}

				super.eventActionUpdate(aE, false);
				panelArtikel.setArtikelEingabefelderEditable(true);

				AuftragDto aDto = DelegateFactory.getInstance().getAuftragDelegate()
						.istAuftragBeiAnderemMandantenHinterlegt(besPosDto.getBestellungIId());

				if (aDto != null) {
					panelArtikel.setArtikelEingabefelderEditable(false);

					panelArtikel.wifArtikelauswahl.getWbuArtikel().setEnabled(false);
					panelArtikel.wifArtikelauswahl.getWtfIdent().setEditable(false);
					if (panelArtikel instanceof PanelPositionenArtikelEinkauf) {
						((PanelPositionenArtikelEinkauf) panelArtikel).wbuPreisauswahl.setEnabled(false);
					}

					panelArtikel.wnfMenge.setEditable(true);
					wtfAngebotnummer.setEditable(false);
					wtfLieferantArtikelBezeichnung.setEditable(false);
					wtfLieferantArtikelNummer.setEditable(false);
				}

			}

			try {
				ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate()
						.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_BESTELLUNG,
								ParameterFac.PARAMETER_DEFAULT_EINKAUFSPREIS_ZURUECKPFLEGEN);
				Integer b = (Integer) parameter.getCWertAsObject();
				if (b == 0) {
					wrbPreiseNichtNachpflegen.setSelected(true);
				} else if (b == 1) {
					wcbPreisstaffelNachpflegen.setSelected(true);
					wnfVerpackungseinheit.setEditable(true);
					wnfFixkosten.setEditable(false);
					wnfMindestbestellmenge.setEditable(false);
				} else if (b == 2) {
					wcbEinzelpreisNachpflegen.setSelected(true);
					wnfVerpackungseinheit.setEditable(true);
					wnfFixkosten.setEditable(true);
					wnfMindestbestellmenge.setEditable(true);

				}
				setzePositionsartAenderbar(besPosDto);
			} catch (Throwable e) {
				handleException(e, true);
			}
		}
	}

	private void initPanel() throws ExceptionLP, Throwable {
		// combobox Positionen in der UI Sprache des Benutzers fuellen
		setPositionsarten(DelegateFactory.getInstance().getBestellungServiceDelegate()
				.getBestellpositionart(LPMain.getTheClient().getLocUi()));
		((PanelPositionenArtikelEinkauf) panelArtikel).setLieferantDto(getTPBes().getLieferantDto());

		((PanelPositionenArtikelEinkauf) panelArtikel).wbuPreisauswahl.addActionListener(this);

		// Darf Preise sehen
		if (!getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			// Frage fuer Lieferantenpreise nachpflegen verstecken
			jPanelPreisnachpflege.setVisible(false);

			// Staffelpreise verstecken
			jPanelStaffel.setVisible(false);
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LOS_FROM_LISTE)) {
			dialogQueryLosFromListe(e);
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_MONATSSTATISTIK)) {
			if (besPosDto.getArtikelIId() != null) {
				ReportArtikelstatistik reportEtikett = new ReportArtikelstatistik(getInternalFrame(),
						besPosDto.getArtikelIId(), true, "");
				getInternalFrame().showReportKriterien(reportEtikett, false);
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SPLIT)) {
			if (besPosDto.getNMenge() != null && getTPBes().istAktualisierenBestellungErlaubt(true)) {

				if (!besPosDto.getBestellpositionstatusCNr()
						.equals(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT)) {

					DialogPositionSplitten d = new DialogPositionSplitten(besPosDto.getIId(), getInternalFrame());
					LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
					d.setVisible(true);

					if (d.getBestellpositionIIdNeu() != null) {
						getTPBes().getBestellungPositionenTop().eventActionRefresh(e, false);
						getTPBes().getPanelBestellungPositionenSP3().eventYouAreSelected(false);

						getTPBes().getBestellungPositionenTop().setSelectedId(d.getBestellpositionIIdNeu());
						getTPBes().getBestellungPositionenBottom().setKeyWhenDetailPanel(d.getBestellpositionIIdNeu());
						getTPBes().getBestellungPositionenBottom().eventYouAreSelected(false);

					}
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
							LPMain.getTextRespectUISPr("bes.bestellposition.split.error.erledigt"));

				}
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_STAFFELPREIS_VERWENDEN)) {
			Integer artikellieferantStaffelIId = (Integer) wcoStaffelRueckpflege.getKeyOfSelectedItem();
			if (artikellieferantStaffelIId != null) {
				setPreis(artikellieferantStaffelIId);
			}

		} else if (e.getActionCommand().equals(PanelPositionenArtikelEinkauf.ACTION_SPECIAL_EK_PREIS_HOLEN)) {

			actionPerformedPreisChanged();

			PanelPositionenArtikelBestellung panelArtikelBest = (PanelPositionenArtikelBestellung) panelArtikel;

			if (oArtikelDto.getNUmrechnungsfaktor() != null && oArtikelDto.getNUmrechnungsfaktor().doubleValue() != 0) {

				if (Helper.short2boolean(oArtikelDto.getbBestellmengeneinheitInvers())) {
					panelArtikelBest.wnfGewichtbestellmenge.setBigDecimal(panelArtikelBest.wnfMenge.getBigDecimal()
							.divide(oArtikelDto.getNUmrechnungsfaktor(), 2, BigDecimal.ROUND_HALF_EVEN));
				} else {
					panelArtikelBest.wnfGewichtbestellmenge.setBigDecimal(
							panelArtikelBest.wnfMenge.getBigDecimal().multiply(oArtikelDto.getNUmrechnungsfaktor()));
				}
			}

			if (oArtikelDto.getEinheitCNrBestellung() != null) {
				panelArtikelBest.wlaGewichtmengeEinheit.setText(oArtikelDto.getEinheitCNrBestellung().trim() + "  ??");
			}

			if (panelArtikelBest.wnfMenge.getDouble() > 1) {
				setPreis((Integer) null);
			} else {
				setPreis(artLiefDto);
			}
			panelArtikelBest.berechnePreisdaten(true);

		} else if (e.getSource().equals(wcbEinzelpreisNachpflegen)) {
			if (getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME
					|| getLockedstateDetailMainKey().getIState() == LOCK_FOR_NEW) {
				if (wcbEinzelpreisNachpflegen.isSelected()) {
					wnfFixkosten.setEditable(true);
					wnfVerpackungseinheit.setEditable(true);
					wnfMindestbestellmenge.setEditable(true);
				}
			}
		} else if (e.getSource().equals(wcbPreisstaffelNachpflegen)) {
			if (getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME
					|| getLockedstateDetailMainKey().getIState() == LOCK_FOR_NEW) {
				if (wcbPreisstaffelNachpflegen.isSelected()) {
					wnfFixkosten.setEditable(false);
					wnfVerpackungseinheit.setEditable(true);
					wnfMindestbestellmenge.setEditable(false);
				}
			}
		} else if (e.getSource().equals(wrbPreiseNichtNachpflegen)) {
			if (getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME
					|| getLockedstateDetailMainKey().getIState() == LOCK_FOR_NEW) {
				if (wrbPreiseNichtNachpflegen.isSelected()) {
					wnfFixkosten.setEditable(false);
					wnfVerpackungseinheit.setEditable(false);
					wnfMindestbestellmenge.setEditable(false);
				}
			}
		}

		if (e.getActionCommand().equals(ACTION_SPECIAL_ETIKETTDRUCKEN)) {

			getInternalFrame().showReportKriterien(new ReportBestelletiketten(intFrame, null));

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLos) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					dialogQueryLossollmaterialFromListe(key);
				}

			} else if (e.getSource() == getPanelArtikel().wifArtikelauswahl) {
				actionPerformedPreisChanged();
				refreshPreisnachpflegePanel();
			} else if (e.getSource() == panelQueryFLRLossollmaterial) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				LossollmaterialDto sollmaterialDto = DelegateFactory.getInstance().getFertigungDelegate()
						.lossollmaterialFindByPrimaryKey(key);

				LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
						.losFindByPrimaryKey(sollmaterialDto.getLosIId());
				besPosDto.setLossollmaterialIId(key);

				String s = losDto.getCNr();
				if (losDto.getCProjekt() != null) {
					s += ", " + losDto.getCProjekt();
				}

				wtfLossollarbeitsplan.setText(s);

				// PJ 16484
				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(sollmaterialDto.getArtikelIId());
				getPanelArtikel().wifArtikelauswahl.setArtikelDto(artikelDto);
				getPanelArtikel().setArtikelDto(artikelDto);
				getPanelArtikel().wnfMenge.setBigDecimal(sollmaterialDto.getNMenge());
				getPanelArtikel().wifArtikelauswahl.getWtfIdent().requestFocus();
				getPanelArtikel().artikelDto2components();

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRLossollmaterial || e.getSource() == panelQueryFLRLos) {

				wtfLossollarbeitsplan.setText(null);
				besPosDto.setLossollmaterialIId(null);
			}
		}
	}

	void dialogQueryLossollmaterialFromListe(Integer selectedLosIId) throws Throwable {
		if (selectedLosIId != null) {
			panelQueryFLRLossollmaterial = FertigungFilterFactory.getInstance().createPanelFLRLossollmaterial(
					getInternalFrame(), selectedLosIId, besPosDto.getLossollmaterialIId(), true);
			new DialogQuery(panelQueryFLRLossollmaterial);
		}

	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRLos = FertigungFilterFactory.getInstance().createPanelFLRBebuchbareLose(getInternalFrame(), true,
				true, true, null, true);
		new DialogQuery(panelQueryFLRLos);

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		setTBelegdatumMwstsatz(tPBes.getBesDto().getDBelegdatum());
		// den gesamten Inhalt zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();
		// position neu einlesen, ausloeser war ev. ein refresh
		Object pkPosition = getKeyWhenDetailPanel();

		if (pkPosition != null && !pkPosition.equals(LPMain.getLockMeForNew())) {
			// Bestehende Position anzeigen.
			BestellpositionServiceDto serviceDto = DelegateFactory.getInstance().getBestellungDelegate()
					.bestellpositionFindByPrimaryKey((Integer) pkPosition, true);
			besPosDto = serviceDto.getPositionDto();
			oArtikelDto = serviceDto.getArtikelDto();
			// besPosDto = DelegateFactory.getInstance().getBestellungDelegate()
			// .bestellpositionFindByPrimaryKey((Integer) pkPosition);
			// if (besPosDto.getArtikelIId() != null) {
			// oArtikelDto = DelegateFactory.getInstance()
			// .getArtikelDelegate()
			// .artikelFindByPrimaryKey(besPosDto.getArtikelIId());
			// }

			dto2Components();

			actionPerformedPreisChanged();
			refreshPreisnachpflegePanel();

			artLiefData2Components(oArtikelDto, artLiefDto, false);

		}

		else {
			panelArtikel.setLetzteArtikelCNr();
		}

		// wenn die Bestellung gerade gelockt ist, die Artikeleingabefelder
		// freischalten
		if (getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			panelArtikel.setArtikelEingabefelderEditable(true);
		}
		getTPBes().enablePanels(getTPBes().getBesDto(), true);

		refreshMyComponents();

		setEditorButtonColor();

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_BESTELLUNG,
				ParameterFac.PARAMETER_DEFAULT_STAFFELPREIS_RUECKPFLEGEN);
		if ((Boolean) parameter.getCWertAsObject()) {
			wcoArtikelnummerBezInStaffelRueckpflegen.setSelected(true);
		}

		if (pkPosition != null && pkPosition.equals(LPMain.getLockMeForNew())) {
			ParametermandantDto parameterWEPInfo = DelegateFactory.getInstance().getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_BESTELLUNG,
							ParameterFac.PARAMETER_DEFAULT_WEP_INFO);
			if ((Boolean) parameterWEPInfo.getCWertAsObject()) {
				wcoWEPInfoAnAnforderer.setSelected(true);
				wcoWEPInfoAnAnfordererHandeingabe.setSelected(true);
			}
		}

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_BESTELLUNG;
	}

	/**
	 * Fuegt den PreisFocusAdapter dieses Panels nur zu dem JComponent hinzu, wenn
	 * noch nicht drauf, verhindert dass der gleiche FocusListener mehrere male
	 * registriert ist
	 * 
	 * @param component
	 */
	private void addPreisFocusListenerIfNotPresent(JComponent component) {
		for (FocusListener lis : component.getFocusListeners()) {
			if (lis.equals(preisFocusAdapter))
				return;
		}
		component.addFocusListener(preisFocusAdapter);
	}

	protected void setDefaults() throws Throwable {

		besPosDto = new BestellpositionDto();
		oArtikelDto = new ArtikelDto();
		artLiefDto = new ArtikellieferantDto();
		besPosDto.setBNettopreisuebersteuert(new Short((short) 0));
		leereAlleFelder(this);

		wnfFixkosten.setForeground(Color.BLACK);

		super.setDefaults();

		PanelPositionenArtikelEinkauf panelArtikelEinkauf = null;

		if (panelArtikel.getArtikelDto() != null) {
			panelArtikelEinkauf = (PanelPositionenArtikelEinkauf) panelArtikel;

			if (artLiefDto != null) {
				wtfLieferantArtikelBezeichnung.setText(artLiefDto.getCBezbeilieferant());
				wtfLieferantArtikelNummer.setText(artLiefDto.getCArtikelnrlieferant());
			}
		}
		Date tLiefertermin = new Date(getTPBes().getBesDto().getDLiefertermin().getTime());
		wdfUebersteuerterLiefertermin.setDate(tLiefertermin);
		// default positionsart ist ident
		wcoPositionsart.setKeyOfSelectedItem(BestellpositionFac.BESTELLPOSITIONART_IDENT);
		jPanelPreisnachpflege.setVisible(true);

		BestellungDto besDto = getTPBes().getBesDto();
		if (besDto != null && besDto.getIId() != null) {

			panelArtikelEinkauf = (PanelPositionenArtikelEinkauf) panelArtikel;
			panelArtikelEinkauf.setLieferantDto(getTPBes().getLieferantDto());

			Double dWechselkursMandant2BS = getTPBes().getBesDto().getFWechselkursmandantwaehrungzubelegwaehrung();

			if (dWechselkursMandant2BS != null) {
				panelArtikelEinkauf.setBdWechselkurs(new BigDecimal(dWechselkursMandant2BS.doubleValue()));
			}

			// alle bestellungsabhaengigen Defaults in den verschiedenen
			// PanelPositionen setzen
			panelHandeingabe.setWaehrungCNr(besDto.getWaehrungCNr());
			panelArtikel.setWaehrungCNr(besDto.getWaehrungCNr());

			panelArtikel.wnfEinzelpreis.setMinimumValue(new BigDecimal(BestellpositionFac.MIN_D_EINZELPREIS));
			panelArtikel.wnfEinzelpreis.setMaximumValue(new BigDecimal(BestellpositionFac.MAX_D_EINZELPREIS));

			addPreisFocusListenerIfNotPresent(panelArtikel.wnfEinzelpreis);

			// wnfFixkosten.addFocusListener(new FocusAdapterPreis(this));
			// wtfLieferantArtikelNummer.addFocusListener(new FocusAdapterPreis(this));
			// wtfLieferantArtikelBezeichnung.addFocusListener(new FocusAdapterPreis(this));

			addPreisFocusListenerIfNotPresent(panelArtikel.wnfRabattsatz);
			addPreisFocusListenerIfNotPresent(panelArtikel.wnfNettopreis.getWrapperWrapperNumberField());

			panelArtikel.wnfMenge.setMinimumValue(new BigDecimal(BestellpositionFac.MIN_D_MENGE));
			panelArtikel.wnfMenge.setMaximumValue(new BigDecimal(BestellpositionFac.MAX_D_MENGE));
			panelArtikel.getWnfRabattsatz().setMinimumValue(new BigDecimal(BestellpositionFac.MIN_D_RABATTSATZ));
			panelArtikel.getWnfRabattsatz().setMaximumValue(new BigDecimal(BestellpositionFac.MAX_D_RABATTSATZ));

			panelHandeingabe.wnfEinzelpreis.setMinimumValue(new BigDecimal(BestellpositionFac.MIN_D_EINZELPREIS));
			panelHandeingabe.wnfEinzelpreis.setMaximumValue(new BigDecimal(BestellpositionFac.MAX_D_EINZELPREIS));

			panelHandeingabe.wnfMenge.setMinimumValue(new BigDecimal(BestellpositionFac.MIN_D_MENGE));
			panelHandeingabe.wnfMenge.setMaximumValue(new BigDecimal(BestellpositionFac.MAX_D_MENGE));

			panelHandeingabe.getWnfRabattsatz().setMinimumValue(new BigDecimal(BestellpositionFac.MIN_D_RABATTSATZ));
			panelHandeingabe.getWnfRabattsatz().setMaximumValue(new BigDecimal(BestellpositionFac.MAX_D_RABATTSATZ));
		}
	}

	private InternalFrameBestellung getInternalFrameBestellung() {
		return (InternalFrameBestellung) getInternalFrame();
	}

	private boolean canChangeBestellpositionBeiNew() throws Throwable {

		boolean goAheadbeiNew = true;
		boolean hatWEP = false;

		BestellpositionDto aBPOSDTOs[] = null;
		WareneingangspositionDto wepDto[] = null;

		Integer nBPOS = DelegateFactory.getInstance().getBestellungDelegate()
				.getAnzahlBestellpositionen(getTPBes().getBesDto().getIId());

		Integer nWE = DelegateFactory.getInstance().getWareneingangDelegate()
				.getAnzahlWE(getTPBes().getBesDto().getIId());

		if (nBPOS.compareTo(new Integer(0)) > 0 && nWE.compareTo(new Integer(0)) > 0) {
			// --BPOS und WE wurden gemacht
			aBPOSDTOs = DelegateFactory.getInstance().getBestellungDelegate()
					.bestellpositionFindByBestellung(getTPBes().getBesDto().getIId());

			for (int i = 0; i < nBPOS.intValue(); i++) {
				wepDto = DelegateFactory.getInstance().getWareneingangDelegate()
						.wareneingangspositionFindByBestellpositionIId(aBPOSDTOs[i].getIId());
				if (wepDto.length != 0) {
					// WEP gefunden
					hatWEP = true;
					break;
				}
			}
		} else {
			goAheadbeiNew = true;
		}

		if (hatWEP) {
			boolean answer = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					LPMain.getTextRespectUISPr("bes.frage.best.trotz.wepos.aendern"),
					LPMain.getTextRespectUISPr("lp.frage"));
			if (!answer) {
				goAheadbeiNew = false;
			}
		}
		return goAheadbeiNew;
	}

	/**
	 * Behandle Ereignis Neu.
	 * 
	 * @param eventObject Ereignis
	 * @param bLockMeI    legt fest, ob keyForLockMe ueberschreiben
	 * @param bNeedNoNewI boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {

		resetEditorButton();

		Object objectKey = getKeyWhenDetailPanel();

		if (getTPBes().istAktualisierenBestellungErlaubt(false)) {
			wtfLieferantArtikelBezeichnung.setText(null);
			wtfLieferantArtikelNummer.setText(null);

			if (canChangeBestellpositionBeiNew()) {
				super.eventActionNew(eventObject, true, true); // LockMeForNew
				// setzen
				panelArtikel.setArtikelEingabefelderEditable(true);
			} else {

			}

			setDefaults();

			// die neue Position soll vor der aktuell selektierten eingefuegt
			// werden
			if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
				// Dieses Flag gibt an, ob die neue Position vor der aktuellen
				// eingefuegt werden soll
				bFuegeNeuePositionVorDerSelektiertenEin = true;
			}

		} else {
			setKeyWhenDetailPanel(objectKey);

			ItemChangedEvent ice = (ItemChangedEvent) eventObject;

			ice.setId(ItemChangedEvent.ACTION_JOB_DONE);
		}
	}

	private void components2Dto() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.components2Dto(besPosDto, tPBes.getLieferantDto().getPartnerDto().getLocaleCNrKommunikation(),
				getTPBes().getBesDto().getIId());
		// 2. Weiter mit den anderen.
		String positionsart = getPositionsartCNr();

		// bei einer neuen Bestellposition einige Felder initialisieren
		if (besPosDto.getIId() == null) {
			besPosDto.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
		}

		if (positionsart.equals(LocaleFac.POSITIONSART_IDENT)) {
			besPosDto.setKundeIId(wsfKunde.getIKey());
			if (besPosDto.getIId() == null) {
				besPosDto.setNRabattbetrag(panelArtikel.getWnfRabattsatz().getBigDecimal());
			}
			if (wdfUebersteuerterLiefertermin.getDate() != null) {
				besPosDto.setTUebersteuerterLiefertermin(
						new Timestamp(wdfUebersteuerterLiefertermin.getDate().getTime()));
			} else {
				besPosDto.setTUebersteuerterLiefertermin(tPBes.getBesDto().getDLiefertermin());
			}

			besPosDto.setDRabattsatz(panelArtikel.getWnfRabattsatz().getDouble());
			besPosDto.setCAngebotnummer(wtfAngebotnummer.getText());

			besPosDto.setnVerpackungseinheit_NOT_IN_DB(wnfVerpackungseinheit.getBigDecimal());
			besPosDto.setfMindestbestellmenge_NOT_IN_DB(wnfMindestbestellmenge.getDouble());

			/**
			 * @todo gh wurde der Rabattsatz aus der Einkaufspreisfindung ubersteuert? PJ
			 *       4878
			 */
			besPosDto.setNNettoeinzelpreis(panelArtikel.wnfEinzelpreis.getBigDecimal());
			besPosDto.setNMaterialzuschlag(panelArtikel.wnfMaterialzuschlag.getBigDecimal());

			if (panelArtikel.getArtikelDto() != null && panelArtikel.wlaMaterialzuschlag != null) {

				if (panelArtikel.getArtikelDto().getMaterialIId() != null) {
					MaterialDto mDto = DelegateFactory.getInstance().getMaterialDelegate()
							.materialFindByPrimaryKey(panelArtikel.getArtikelDto().getMaterialIId());
					panelArtikel.wlaMaterialzuschlag.setText(mDto.getBezeichnung());
				} else {
					panelArtikel.wlaMaterialzuschlag.setText(LPMain.getTextRespectUISPr("lp.materialzuschlag"));
				}
			}

			besPosDto.setNRabattbetrag(panelArtikel.wnfRabattsumme.getBigDecimal());
			besPosDto.setNNettogesamtpreis(panelArtikel.wnfNettopreis.getBigDecimal());
			besPosDto.setNFixkosten(wnfFixkosten.getBigDecimal());

			besPosDto.setBWepinfoAnAnforderer(wcoWEPInfoAnAnforderer.getShort());

			// components2ArtLiefDto();
		} else if (positionsart.equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
			besPosDto.setDRabattsatz(panelHandeingabe.getWnfRabattsatz().getDouble());
			besPosDto.setKundeIId(wsfKundeHandeingabe.getIKey());
			besPosDto.setBRabattsatzUebersteuert(new Short((short) 0));
			besPosDto.setMwstsatzIId((Integer) panelHandeingabe.wcoMwstsatz.getKeyOfSelectedItem());
			besPosDto.setBMwstsatzUebersteuert(new Short((short) 0));
			besPosDto.setNNettoeinzelpreis(panelHandeingabe.wnfEinzelpreis.getBigDecimal());
			besPosDto.setNMaterialzuschlag(new BigDecimal(0));
			besPosDto.setNRabattbetrag(panelHandeingabe.wnfRabattsumme.getBigDecimal());
			besPosDto.setNNettogesamtpreis(panelHandeingabe.wnfNettopreis.getBigDecimal());
			if (panelHandeingabe.wdfLieferterminPosition.getTimestamp() != null) {
				besPosDto.setTUebersteuerterLiefertermin(panelHandeingabe.wdfLieferterminPosition.getTimestamp());
			} else {
				besPosDto.setTUebersteuerterLiefertermin(
						new Timestamp(getTPBes().getBesDto().getDLiefertermin().getTime()));
			}

			if (artLiefDto != null) {
				artLiefDto.setCBezbeilieferant(wtfLieferantArtikelBezeichnungHandeingabe.getText());
				artLiefDto.setCArtikelnrlieferant(wtfLieferantArtikelNummerHandeingabe.getText());
			}

			besPosDto.setBWepinfoAnAnforderer(wcoWEPInfoAnAnfordererHandeingabe.getShort());

		}
	}

	private void dto2Components() throws Throwable {
		// 1. Behandlung der trivialen Positionsarten.
		super.dto2Components(besPosDto, tPBes.getLieferantDto().getPartnerDto().getLocaleCNrKommunikation());

		// 2. Weiter mit den anderen.
		String positionsart = besPosDto.getPositionsartCNr();
		wdfUebersteuerterLiefertermin.setDate(new Date(besPosDto.getTUebersteuerterLiefertermin().getTime()));
		wnfFixkosten.setBigDecimal(besPosDto.getNFixkosten());

		wsfKunde.setKey(besPosDto.getKundeIId());
		wsfKundeHandeingabe.setKey(besPosDto.getKundeIId());

		wcoWEPInfoAnAnforderer.setShort(besPosDto.getBWepinfoAnAnforderer());
		wcoWEPInfoAnAnfordererHandeingabe.setShort(besPosDto.getBWepinfoAnAnforderer());

		if (positionsart.equalsIgnoreCase(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {

			panelArtikel.getWnfRabattsatz().setDouble(besPosDto.getDRabattsatz());
			panelArtikel.wnfEinzelpreis.setBigDecimal(besPosDto.getNNettoeinzelpreis());
			panelArtikel.wnfRabattsumme.setBigDecimal(besPosDto.getNRabattbetrag());
			panelArtikel.wnfMaterialzuschlag.setBigDecimal(besPosDto.getNMaterialzuschlag());
			panelArtikel.wnfNettopreis.setBigDecimal(besPosDto.getNNettogesamtpreis());
			panelArtikel.setArtikelDto(oArtikelDto);
			if (oArtikelDto.getIId() != null) {

				artLiefDto = HelperBestellung.getArtikellieferantDtoEinkaufspreis(oArtikelDto.getIId(),
						tPBes.getLieferantDto().getIId(), besPosDto.getNMenge(),
						tPBes.getLieferantDto().getWaehrungCNr(), tPBes.getBesDto().getDBelegdatum(),
						besPosDto.getGebindeIId());
			}

			if (artLiefDto != null) {
				wtfLieferantArtikelBezeichnung.setText(artLiefDto.getCBezbeilieferant());
				wtfLieferantArtikelNummer.setText(artLiefDto.getCArtikelnrlieferant());
				if (artLiefDto.getNVerpackungseinheit() != null) {
					panelArtikel.pa.setVerpackungsmenge(artLiefDto.getNVerpackungseinheit().doubleValue());
				}

			} else {
				wtfLieferantArtikelBezeichnung.setText(null);
				wtfLieferantArtikelNummer.setText(null);
			}

			BigDecimal nNettoBetrag = panelArtikel.wnfEinzelpreis.getBigDecimal()
					.subtract(panelArtikel.wnfRabattsumme.getBigDecimal());

			wtfAngebotnummer.setText(besPosDto.getCAngebotnummer());

			if ((panelArtikel.wnfNettopreis.getBigDecimal()).compareTo(nNettoBetrag) != 0) {
				panelArtikel.wnfNettopreis.getWrbFixNumber().setSelected(true);
			}

			if (besPosDto.getLossollmaterialIId() != null) {
				LossollmaterialDto sollmaterialDto = DelegateFactory.getInstance().getFertigungDelegate()
						.lossollmaterialFindByPrimaryKey(besPosDto.getLossollmaterialIId());

				LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
						.losFindByPrimaryKey(sollmaterialDto.getLosIId());
				String s = losDto.getCNr();
				if (losDto.getCProjekt() != null) {
					s += ", " + losDto.getCProjekt();
				}

				wtfLossollarbeitsplan.setText(s);
			} else {
				wtfLossollarbeitsplan.setText(null);
			}

		} else if (positionsart.equalsIgnoreCase(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
			panelHandeingabe.getWnfRabattsatz().setDouble(besPosDto.getDRabattsatz());
			panelHandeingabe.wnfEinzelpreis.setBigDecimal(besPosDto.getNNettoeinzelpreis());
			panelHandeingabe.wnfRabattsumme.setBigDecimal(besPosDto.getNRabattbetrag());
			panelHandeingabe.wnfNettopreis.setBigDecimal(besPosDto.getNNettogesamtpreis());
			panelHandeingabe.wdfLieferterminPosition.setDate(besPosDto.getTUebersteuerterLiefertermin());

			if (besPosDto.getArtikelIId() != null) {
				artLiefDto = DelegateFactory.getInstance().getArtikelDelegate().getArtikelEinkaufspreis(
						oArtikelDto.getIId(), tPBes.getLieferantDto().getIId(), besPosDto.getNMenge(),
						tPBes.getLieferantDto().getWaehrungCNr(), tPBes.getBesDto().getDBelegdatum());
			}

			if (artLiefDto != null) {
				wtfLieferantArtikelBezeichnungHandeingabe.setText(artLiefDto.getCBezbeilieferant());
				wtfLieferantArtikelNummerHandeingabe.setText(artLiefDto.getCArtikelnrlieferant());
			} else {
				wtfLieferantArtikelBezeichnungHandeingabe.setText(null);
				wtfLieferantArtikelNummerHandeingabe.setText(null);
			}

		}

		// PJ17730

		BigDecimal bdBestellwertinbestellwaehrung = tPBes.getBesDto().getNBestellwert();

		if (bdBestellwertinbestellwaehrung == null) {
			bdBestellwertinbestellwaehrung = DelegateFactory.getInstance().getBestellungDelegate()
					.berechneNettowertGesamt(tPBes.getBesDto().getIId());
		}

		String sGesamtbetrag = Helper.formatZahl(bdBestellwertinbestellwaehrung, 2,
				LPMain.getInstance().getUISprLocale());
		sGesamtbetrag = sGesamtbetrag + " " + tPBes.getBesDto().getWaehrungCNr();
		if (getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			setStatusbarSpalte5(sGesamtbetrag);
		}

		if (tPBes.getLieferantDto().getNMindestbestellwert() != null) {

			BigDecimal bdMindestbestellwertinBestellwaehrung = DelegateFactory.getInstance().getLocaleDelegate()
					.rechneUmInAndereWaehrungGerundetZuDatum(tPBes.getLieferantDto().getNMindestbestellwert(),
							tPBes.getLieferantDto().getWaehrungCNr(), tPBes.getBesDto().getWaehrungCNr(),
							tPBes.getBesDto().getDBelegdatum());
			if (bdMindestbestellwertinBestellwaehrung.doubleValue() > bdBestellwertinbestellwaehrung.doubleValue()) {
				setStatusbarSpalte5Color(Color.RED);
			}
		}

	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI) throws Throwable {

		super.eventActionRefresh(e, bNeedNoRefreshI);

		getTPBes().enablePanels(getTPBes().getBesDto(), true);

		refreshMyComponents();
	}

	public final boolean allMandatoryFieldsSetDlg() throws Throwable {
		boolean bAS = super.allMandatoryFieldsSetDlg();
		if (bAS) {
			bAS = checkMengeDlg();
		}
		return bAS;
	}

	/**
	 * checkMengeDlg
	 * 
	 * @return boolean
	 * @throws ExceptionLP
	 */
	private boolean checkMengeDlg() throws ExceptionLP {
		boolean bSpeichern = true;

		if (artLiefDto != null) {
			if (artLiefDto.getNVerpackungseinheit() != null) {
				int iRest = 0;
				if (artLiefDto.getNVerpackungseinheit().intValue() != 0) {
					iRest = panelArtikel.wnfMenge.getBigDecimal().intValue()
							% artLiefDto.getNVerpackungseinheit().intValue();
				}
				String sMsg = null;
				if (iRest > 0) {
					sMsg = LPMain.getTextRespectUISPr("bes.verpackungseinheit2");
				} else {
					sMsg = "";
				}
				if (artLiefDto.getFMindestbestelmenge() != null) {
					if (panelArtikel.wnfMenge.getBigDecimal().doubleValue() < artLiefDto.getFMindestbestelmenge()
							.doubleValue()) {
						sMsg += LPMain.getTextRespectUISPr("bes.mindestmenge");
					}
				}
				if (sMsg != null && !sMsg.equals("")) {
					sMsg += LPMain.getTextRespectUISPr("bes.speichern");
					bSpeichern = DialogFactory.showModalJaNeinDialog(getInternalFrame(), sMsg,
							LPMain.getTextRespectUISPr("lp.hinweis"));
				}
			}
		}
		return bSpeichern;
	}

	private boolean isAmountValid(BigDecimal bestellMenge, BestellungDto bestellungDto) {
		String positionsart = besPosDto.getPositionsartCNr();
		if (LocaleFac.POSITIONSART_IDENT.equals(positionsart)
				|| LocaleFac.POSITIONSART_HANDEINGABE.equals(positionsart)) {
			if (null == bestellMenge)
				return false;
			if (bestellMenge.signum() == 0) {

				String status = bestellungDto.getStatusCNr();
				if (BestellungFac.BESTELLSTATUS_ANGELEGT.equals(status)
						|| BestellungFac.BESTELLSTATUS_OFFEN.equals(status))
					return false;
			}
		}

		return true;
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {

		try {
			calculateFields();
			if (allMandatoryFieldsSetDlg()) {

				components2Dto();
				checkAndSetGelPreisDlg();

				if (!isAmountValid(besPosDto.getNMenge(), getBestellungDto())) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
							LPMain.getTextRespectUISPr("bestellung.warning.nullmengebeistatusoffennichterlaubt"));
					return;
				}

				// PJ20372
				if (besPosDto.getLossollmaterialIId() != null && besPosDto.getArtikelIId() != null) {
					LossollmaterialDto sollmaterialDto = DelegateFactory.getInstance().getFertigungDelegate()
							.lossollmaterialFindByPrimaryKey(besPosDto.getLossollmaterialIId());

					if (!besPosDto.getArtikelIId().equals(sollmaterialDto.getArtikelIId())) {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
								LPMain.getTextRespectUISPr(
										"bes.bestellpositiion.warning.stimmtmitsollarbeitsplan.nichtzusammen"));

					}

				}

				// PJ 15052
				if (besPosDto.getTUebersteuerterLiefertermin() != null
						&& Helper.cutTimestamp(besPosDto.getTUebersteuerterLiefertermin())
								.before(Helper.cutDate(getTPBes().getBesDto().getDBelegdatum()))) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
							LPMain.getTextRespectUISPr("auft.hint.lieferterminvorbelegtermin"));
				}

				// PJ 16966

				boolean bZertifiziert = pruefeObZertifiziert(panelArtikel.getArtikelDto(), tPBes.getLieferantDto());

				if (bZertifiziert == false) {
					boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							LPMain.getTextRespectUISPr("part.lieferant.nichtzertifiziert.trotzdem"));

					if (b == false) {
						return;
					}
				}

				if (besPosDto.getIId() == null) {
					// create

					// wenn AbrufBes und ein neue BesPos kommt
					// checkAbrufDlg();

					// Soll die neue Position vor der aktuell selektierten
					// stehen?
					if (bFuegeNeuePositionVorDerSelektiertenEin) {
						Integer iIdAktuellePosition = (Integer) getTPBes().getBestellungPositionenTop().getSelectedId();

						// die erste Position steht auf 1
						Integer iSortAktuellePosition = new Integer(1);

						if (iIdAktuellePosition != null) {
							iSortAktuellePosition = DelegateFactory.getInstance().getBestellungDelegate()
									.bestellpositionFindByPrimaryKey(iIdAktuellePosition).getISort();

							// Die bestehenden Positionen muessen Platz fuer die
							// neue schaffen
							DelegateFactory.getInstance().getBestellungDelegate()
									.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
											getTPBes().getBesDto().getIId(), iSortAktuellePosition.intValue());
						}

						// Die neue Position wird an frei gemachte Position
						// gesetzt
						besPosDto.setISort(iSortAktuellePosition);
					}

					BestellungDto bestDto = getTPBes().getBesDto();

					// die offene Menge wird nur bei neuen mengenbehafteten
					// Rahmenpositionen gesetzt

					BestellungDto actualBestDTO = DelegateFactory.getInstance().getBestellungDelegate()
							.bestellungFindByPrimaryKey(bestDto.getIId());
					if (actualBestDTO.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR)) {
						if (besPosDto.getNMenge() != null) {
							besPosDto.setNOffeneMenge(besPosDto.getNMenge());
						}
					}
					if (actualBestDTO.getBestellungartCNr().equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
						// Wenn eine Abrufposition und der Artikel im Rahmen
						// vorhanden dann Warnung dass Menge nicht vom Rahmen
						// abgezogen wird.
						BestellpositionDto[] rahmenbestDto = DelegateFactory.getInstance().getBestellungDelegate()
								.bestellpositionFindByBestellung(bestDto.getIBestellungIIdRahmenbestellung());
						int i = 0;
						boolean bBereitsgezeigt = false;
						while (i < rahmenbestDto.length && !bBereitsgezeigt) {
							if (besPosDto.getArtikelIId() != null) {
								if (besPosDto.getArtikelIId().equals(rahmenbestDto[i].getArtikelIId())) {
									DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
											LPMain.getTextRespectUISPr("bestellung.hinweis.keinerahmenzuordnung"));
									bBereitsgezeigt = true;
								}
							}
							i++;
						}
					}

					besPosDto
							.setPositioniIdArtikelset(getArtikelsetViewController().getArtikelSetIIdFuerNeuePosition());

					// SP8144
					frageNachpflegenFallsKeinArtikelLieferant();

					Integer iId = DelegateFactory.getInstance().getBestellungDelegate().createBestellposition(besPosDto,
							getSPreispflege(), (Integer) wcoStaffelRueckpflege.getKeyOfSelectedItem());

					besPosDto = DelegateFactory.getInstance().getBestellungDelegate()
							.bestellpositionFindByPrimaryKey(iId);

					if (artLiefDto == null || besPosDto.getPositionsartCNr()
							.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
						artLiefDto = DelegateFactory.getInstance().getArtikelDelegate().getArtikelEinkaufspreis(
								besPosDto.getArtikelIId(), getTPBes().getBesDto().getLieferantIIdBestelladresse(),
								new BigDecimal(1), getTPBes().getBesDto().getWaehrungCNr(),
								getTPBes().getBesDto().getDBelegdatum());

						if (besPosDto.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)
								&& artLiefDto == null) {
							artLiefDto = DelegateFactory.getInstance().getArtikelDelegate()
									.artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
											besPosDto.getArtikelIId(),
											getTPBes().getBesDto().getLieferantIIdBestelladresse(),
											getTPBes().getBesDto().getDBelegdatum(), null);
						}

						if (artLiefDto != null) {

							if (besPosDto.getPositionsartCNr()
									.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {

								artLiefDto.setCBezbeilieferant(wtfLieferantArtikelBezeichnungHandeingabe.getText());
								artLiefDto.setCArtikelnrlieferant(wtfLieferantArtikelNummerHandeingabe.getText());
							} else {
								artLiefDto.setCBezbeilieferant(wtfLieferantArtikelBezeichnung.getText());
								artLiefDto.setCArtikelnrlieferant(wtfLieferantArtikelNummer.getText());
							}
							DelegateFactory.getInstance().getArtikelDelegate().updateArtikellieferant(artLiefDto);
						}
					} else {
						if (artLiefDto.getArtikelIId() != null) {

							if (wcoArtikelnummerBezInStaffelRueckpflegen.isSelected()
									&& artLiefDto.getArtikellieferantstaffelIId() != null) {
								ArtikellieferantstaffelDto staffelDto = DelegateFactory.getInstance()
										.getArtikelDelegate().artikellieferantstaffelFindByPrimaryKey(
												artLiefDto.getArtikellieferantstaffelIId());
								staffelDto.setCBezbeilieferant(wtfLieferantArtikelBezeichnung.getText());
								staffelDto.setCArtikelnrlieferant(wtfLieferantArtikelNummer.getText());
								DelegateFactory.getInstance().getArtikelDelegate()
										.updateArtikellieferantstaffel(staffelDto);
							} else {
								artLiefDto = DelegateFactory.getInstance().getArtikelDelegate()
										.artikellieferantFindByPrimaryKey(artLiefDto.getIId());
								artLiefDto.setCBezbeilieferant(wtfLieferantArtikelBezeichnung.getText());
								artLiefDto.setCArtikelnrlieferant(wtfLieferantArtikelNummer.getText());
								DelegateFactory.getInstance().getArtikelDelegate().updateArtikellieferant(artLiefDto);
							}

						}
					}
					setKeyWhenDetailPanel(iId);
				} else {
					// update
					WareneingangspositionDto[] wePos = DelegateFactory.getInstance().getWareneingangDelegate()
							.wareneingangspositionFindByBestellpositionIId(besPosDto.getIId());
					BigDecimal bdGelieferteMenge = null;
					for (int i = 0; i < wePos.length; i++) {
						if (bdGelieferteMenge != null) {
							bdGelieferteMenge = bdGelieferteMenge.add(wePos[i].getNGeliefertemenge());
						} else {
							bdGelieferteMenge = wePos[i].getNGeliefertemenge();
						}
					}
					boolean bMengenKorrekt = true;
					if (bdGelieferteMenge != null) { // Wenn gelieferte Menge
						// NULL dann noch keine
						// WEs oder nicht
						// Mengenbehaftete Pos
						if (bdGelieferteMenge.compareTo(besPosDto.getNMenge()) > 0) {
							bMengenKorrekt = false;
						}
					}
					if (bMengenKorrekt) {
						if (getTPBes().getBesDto().getBestellungartCNr()
								.equals(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
							DelegateFactory.getInstance().getBestellungDelegate().updateAbrufbestellposition(besPosDto,
									getSPreispflege(), (Integer) wcoStaffelRueckpflege.getKeyOfSelectedItem());
						} else {

							frageNachpflegenFallsKeinArtikelLieferant();

							DelegateFactory.getInstance().getBestellungDelegate().updateBestellposition(besPosDto,
									getSPreispflege(), (Integer) wcoStaffelRueckpflege.getKeyOfSelectedItem());

							if (artLiefDto == null) {
								artLiefDto = DelegateFactory.getInstance().getArtikelDelegate().getArtikelEinkaufspreis(
										besPosDto.getArtikelIId(),
										getTPBes().getBesDto().getLieferantIIdBestelladresse(), new BigDecimal(1),
										getTPBes().getBesDto().getWaehrungCNr(),
										getTPBes().getBesDto().getDBelegdatum());
							}

							if (artLiefDto != null && artLiefDto.getIId() != null) {

								artLiefDto.setNFixkosten(wnfFixkosten.getBigDecimal());
								artLiefDto.setBHerstellerbez(Helper.boolean2Short(false));

								if (besPosDto.getPositionsartCNr()
										.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {

									artLiefDto.setCBezbeilieferant(wtfLieferantArtikelBezeichnungHandeingabe.getText());
									artLiefDto.setCArtikelnrlieferant(wtfLieferantArtikelNummerHandeingabe.getText());

									DelegateFactory.getInstance().getArtikelDelegate()
											.updateArtikellieferant(artLiefDto);

								} else {

									if (wcoArtikelnummerBezInStaffelRueckpflegen.isSelected()
											&& artLiefDto.getArtikellieferantstaffelIId() != null) {
										ArtikellieferantstaffelDto staffelDto = DelegateFactory.getInstance()
												.getArtikelDelegate().artikellieferantstaffelFindByPrimaryKey(
														artLiefDto.getArtikellieferantstaffelIId());
										staffelDto.setCBezbeilieferant(wtfLieferantArtikelBezeichnung.getText());
										staffelDto.setCArtikelnrlieferant(wtfLieferantArtikelNummer.getText());
										DelegateFactory.getInstance().getArtikelDelegate()
												.updateArtikellieferantstaffel(staffelDto);
									} else {

										artLiefDto = DelegateFactory.getInstance().getArtikelDelegate()
												.artikellieferantFindByPrimaryKey(artLiefDto.getIId());

										artLiefDto.setCBezbeilieferant(wtfLieferantArtikelBezeichnung.getText());
										artLiefDto.setCArtikelnrlieferant(wtfLieferantArtikelNummer.getText());

									}

									DelegateFactory.getInstance().getArtikelDelegate()
											.updateArtikellieferant(artLiefDto);
								}

							}
						}
					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
								LPMain.getTextRespectUISPr("bestellung.update.kleineralswe"));
					}
				}

				// PJ18750
				// preispflegeDerHandeingabe();

				super.eventActionSave(e, false);

				eventYouAreSelected(false);

				bFuegeNeuePositionVorDerSelektiertenEin = false;
				getArtikelsetViewController().resetArtikelSetIIdFuerNeuePosition();

			}
		} finally {
			// per Default wird eine neue Position ans Ende der Liste gesetzt
			// bFuegeNeuePositionVorDerSelektiertenEin = false;
		}
	}

	/**
	 * Ueberprueft ob Artikellieferant null ist und Bestellart IDENT ist, dann wird
	 * gefragt ob Artikellieferant nachgepflegt werden soll
	 */
	private void frageNachpflegenFallsKeinArtikelLieferant() {
		if (artLiefDto == null && besPosDto.getPositionsartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)
				&& wrbPreiseNichtNachpflegen.isSelected()) {
			if (wtfLieferantArtikelBezeichnung.getText() != null || wtfLieferantArtikelNummer.getText() != null) {
				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(), LPMain
						.getTextRespectUISPr("bes.bestellposition.keinartikellieferant.artikelnummernachpflegen"));

				if (b == true) {
					wcbEinzelpreisNachpflegen.setSelected(true);
				}

			}

		}
	}

	// SK Wird schon in Update erledigt hat hier nicht funktioniert.
	// private void checkAbrufDlg() {
	// if (getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)
	// || getPositionsartCNr().equals(
	// LocaleFac.POSITIONSART_HANDEINGABE)) {
	// if (getTPBes().getBesDto().getBestellungartCNr().equals(
	// BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR)) {
	//
	// if (bWarnungAusgesprochen) {
	// // einmalige Warnung aussprechen
	// DialogFactory
	// .showModalDialog(
	// LPMain.getTextRespectUISPr("lp.warning"),
	// LPMain
	// .getTextRespectUISPr("lp.warning.reduziertnichtdieoffenemengeimrahmen"));
	// bWarnungAusgesprochen = true;
	// }
	// }
	// }
	// }

	/**
	 * getSPreispflege
	 * 
	 * @return String
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	private String getSPreispflege() throws ExceptionLP, Throwable {

		String sPreispflege = BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT;

		if (wcbEinzelpreisNachpflegen.isSelected()) {
			// einzelpreis
			sPreispflege = BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_EINZELPREIS_RUECKPFLEGEN;
		}

		else if (wcbPreisstaffelNachpflegen.isSelected()) {
			// staffel aendern
			sPreispflege = BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_STAFFELPREIS_RUECKPFLEGEN;
		}
		return sPreispflege;
	}

	/**
	 * Drucke Bestellung.
	 * 
	 * @param e Ereignis
	 * @throws Throwable
	 */
	protected void eventActionPrint(HvActionEvent e) throws Throwable {

		if (e.isMouseEvent() && e.isRightButtonPressed()) {

			boolean bStatusAngelegt = getTPBes().getBesDto().getStatusCNr()
					.equals(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT);
			boolean bKonditionen = getTPBes().pruefeKonditionen();

			boolean bRecht = getCachedRights().getValueOfKey(RechteFac.RECHT_BES_BESTELLUNG_CUD);

			if (bStatusAngelegt && bKonditionen && bRecht) {
				DelegateFactory.getInstance().getBestellungDelegate()
						.berechneAktiviereBelegControlled(getTPBes().getBesDto().getIId());

				BestellungDto bestellungDto = DelegateFactory.getInstance().getBestellungDelegate()
						.bestellungFindByPrimaryKey(getTPBes().getBesDto().getIId());

				((InternalFrameBestellung) getInternalFrame()).getTabbedPaneBestellung()
						.setBestellungDto(bestellungDto);
				getTPBes().getPanelBestellungPositionenTopQP3().eventYouAreSelected(false);
				eventActionRefresh(e, false);

			} else if (!bStatusAngelegt) {
				DialogFactory.showModalDialog("Status", LPMain.getMessageTextRespectUISPr("status.zustand",
						LPMain.getTextRespectUISPr("bes.bestellung"), getTPBes().getBestellungStatus().trim()));
			}

		} else {

			getTPBes().printBestellung();

		}

		eventYouAreSelected(false);

		getTPBes().enablePanels(getTPBes().getBesDto(), true);

	}

	/**
	 * 
	 * @throws Throwable
	 */
	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(getTPBes().getBesDto().getPersonalIIdAnlegen());
		setStatusbarTAnlegen(getTPBes().getBesDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(getTPBes().getBesDto().getPersonalIIdAendern());
		setStatusbarTAendern(getTPBes().getBesDto().getTAendern());
		setStatusbarStatusCNr(getTPBes().getBesDto().getStatusCNr());
	}

	/**
	 * btngeliefert: 0
	 * 
	 * @return LockStateValue
	 * @throws Throwable
	 */
	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue lsv = null;
		if (getBestellungDto().getStatusCNr().equals(BestellungFac.BESTELLSTATUS_GELIEFERT)
				|| getBestellungDto().getStatusCNr().equals(BestellungFac.BESTELLSTATUS_ERLEDIGT)) {
			lsv = new LockStateValue(PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
		} else {
			lsv = super.getLockedstateDetailMainKey();
		}

		return lsv;
	}

	private void refreshMyComponents() throws Throwable {

		aktualisiereStatusbar();

		panelArtikel.wcoEinheit.setEnabled(false);

		int lockstate = getLockedstateDetailMainKey().getIState();
		wcoPositionsart.setEnabled(lockstate == LOCK_FOR_NEW);

		// LPButtonAction item = null;
		// String cNrStatus = getTPBes().getBesDto().getStatusCNr();
		// boolean bEnable =
		// !cNrStatus.equals(BestellungFac.BESTELLSTATUS_ERLEDIGT)
		// && !cNrStatus.equals(BestellungFac.BESTELLSTATUS_GELIEFERT)
		// && lockstate != LOCK_FOR_EMPTY && lockstate != LOCK_FOR_NEW;
		/*
		 * && ((tPBes.getWareneingangDto() == null) || (tPBes.getWareneingangDto() !=
		 * null && tPBes .getWareneingangDto().getIId() == null) || (tPBes
		 * .getWareneingangDto() != null && tPBes.getWareneingangDto().getIId() != null
		 * && (DelegateFactory .getInstance().getWareneingangDelegate().getAnzahlWE(
		 * getBestellungDto().getIId()).compareTo( new Integer(0)) == 0));
		 */

		// item = (LPButtonAction)
		// getHmOfButtons().get(PanelBasis.ACTION_DELETE);
		// // item.getButton().setEnabled(bEnable);
		// item.shouldBeEnabledTo(bEnable);
		//
		// item = (LPButtonAction)
		// getHmOfButtons().get(PanelBasis.ACTION_UPDATE);
		// // item.getButton().setEnabled(bEnable);
		// item.shouldBeEnabledTo(bEnable);

		// copypaste: 1 jetzt copy einfuegen buttons enable/disablen
		boolean bEnableEinfuegen = ((LPMain.getPasteBuffer()
				.existsPasteBuffer(PasteBuffer.FILE_PASTEBUFFER_POSITIONS)) != null);
		((LPButtonAction) (getHmOfButtons().get(ACTION_EINFUEGEN_LIKE_NEW))).getButton().setEnabled(bEnableEinfuegen);
		// titel
		getInternalFrameBestellung().getTabbedPaneBestellung().setTitle();
	}

	/**
	 * actionPerformedEinzelpreis TODO die Methode ist wahrscheinlich gar nicht
	 * n�tig!!!
	 * 
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	public void actionPerformedPreisChanged() throws Throwable {

		/**
		 * @todo JO q&d methode wird zu oft aufgerufen - hier muss man suchen! PJ 4888
		 */

		long tNow = System.currentTimeMillis();
		long tDiff = tNow - tBefore;
		if (tDiff < 150) {
			System.out.println(tDiff + "ms actionPerformedPreisChanged NICHT ausgefuehrt!");
			return;
		}
		tBefore = tNow;
		System.out.println(tDiff + "ms actionPerformedPreisChanged ausgefuehrt!");

		if (panelArtikel.getArtikelDto().getIId() == null) {
			/**
			 * @todo JO->WH PJ 4888
			 */
			// zb wenn neu und ein alter artikelname wurde vorgeschlagen und
			// benutzer
			// klickt mit maus auf feld! artikel wird nie geladen?
			// WH ist zz. so!
			return;
		}

		if (!getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			// Wenn man keine Einkaufspreise sehen darf, duerfen auch keine
			// Preise nachgepflegt werden.
			return;
		}

		// PJ21210
		BigDecimal bdMengeVorhanden = BigDecimal.ZERO;

		if (Helper.short2boolean(panelArtikel.getArtikelDto().getBSummeInBestellung())) {
			bdMengeVorhanden = DelegateFactory.getInstance().getBestellungDelegate()
					.getSummeMengeGleicherArtikelInBestellung(
							getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getIId(),
							getInternalFrameBestellung().getTabbedPaneBestellung()
									.getPanelBestellungPositionenBottomD3().getBestellpositionDto().getIId(),
							panelArtikel.getArtikelDto().getIId());
		}

		try {
			artLiefDto = HelperBestellung.getArtikellieferantDtoEinkaufspreis(panelArtikel.getArtikelDto().getIId(),
					((PanelPositionenArtikelEinkauf) panelArtikel).getIIdLieferant(),
					panelArtikel.wnfMenge.getBigDecimal().add(bdMengeVorhanden),
					getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getWaehrungCNr(),
					getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getDBelegdatum(),
					getInternalFrameBestellung().getTabbedPaneBestellung().getPanelBestellungPositionenBottomD3()
							.getBestellpositionDto().getGebindeIId());
		} catch (Throwable exDummy) {
			// nothing here
		}

		if (artLiefDto != null && besPosDto.getIId() == null) {
			wtfAngebotnummer.setText(artLiefDto.getCAngebotnummer());
			wtfLieferantArtikelNummer.setText(artLiefDto.getCArtikelnrlieferant());
			wtfLieferantArtikelBezeichnung.setText(artLiefDto.getCBezbeilieferant());
		}

	}

	public void artLiefData2Components(ArtikelDto artDtoI, ArtikellieferantDto artLiefDtoI,
			boolean bArtikelNeuAusgewaehlt) throws Throwable {

		oArtikelDto = artDtoI;
		artLiefDto = artLiefDtoI;

		String sArtEinheit = "";
		if (artDtoI != null && artDtoI.getEinheitCNr() != null) {
			sArtEinheit = artDtoI.getEinheitCNr().trim();
		}
		wlaMindestbestellmengeEinheit.setText(sArtEinheit);

		if (artLiefDto != null) {
			// PJ21437
			if (bArtikelNeuAusgewaehlt) {
				wnfFixkosten.setBigDecimal(artLiefDto.getNFixkosten());
			}

			if (bArtikelNeuAusgewaehlt == false) {
				if ((wnfFixkosten.getBigDecimal() == null && artLiefDto.getNFixkosten() != null)
						|| (wnfFixkosten.getBigDecimal() != null && artLiefDto.getNFixkosten() == null)
						|| (wnfFixkosten.getBigDecimal() != null && artLiefDto.getNFixkosten() != null
								&& !Helper.rundeKaufmaennisch(wnfFixkosten.getBigDecimal(), 2)
										.equals(Helper.rundeKaufmaennisch(artLiefDto.getNFixkosten(), 2)))) {
					wnfFixkosten.setForeground(Color.RED);
				}
			}

			wnfMindestbestellmenge.setDouble(artLiefDto.getFMindestbestelmenge());
			wnfVerpackungseinheit.setBigDecimal(artLiefDto.getNVerpackungseinheit());
			wtfLieferantArtikelBezeichnung.setText(artLiefDto.getCBezbeilieferant());
			wtfLieferantArtikelNummer.setText(artLiefDto.getCArtikelnrlieferant());

			wtfLieferantArtikelBezeichnungHandeingabe.setText(artLiefDto.getCBezbeilieferant());
			wtfLieferantArtikelNummerHandeingabe.setText(artLiefDto.getCArtikelnrlieferant());

			if (besPosDto.getIId() == null) {
				wtfAngebotnummer.setText(artLiefDto.getCAngebotnummer());
			}

		}

		wlaFixkostenEinheit.setText(panelArtikel.getWaehrungCNr());

	}

	public void artLiefData2ComponentsWennKeinEinzelpreisDefiniert(ArtikelDto artDtoI, ArtikellieferantDto artLiefDtoI,
			boolean bArtikelNeuAusgewaehlt) throws Throwable {

		String sArtEinheit = "";
		if (artDtoI != null && artDtoI.getEinheitCNr() != null) {
			sArtEinheit = artDtoI.getEinheitCNr().trim();
		}
		wlaMindestbestellmengeEinheit.setText(sArtEinheit);

		if (artLiefDtoI != null) {
			// PJ21437
			if (bArtikelNeuAusgewaehlt) {
				wnfFixkosten.setBigDecimal(artLiefDtoI.getNFixkosten());
			}
			wnfMindestbestellmenge.setDouble(artLiefDtoI.getFMindestbestelmenge());
			wnfVerpackungseinheit.setBigDecimal(artLiefDtoI.getNVerpackungseinheit());
			wtfLieferantArtikelBezeichnung.setText(artLiefDtoI.getCBezbeilieferant());
			wtfLieferantArtikelNummer.setText(artLiefDtoI.getCArtikelnrlieferant());

			if (besPosDto.getIId() == null) {
				wtfAngebotnummer.setText(artLiefDtoI.getCAngebotnummer());
			}

		}

		wlaFixkostenEinheit.setText(panelArtikel.getWaehrungCNr());

	}

	/**
	 * mouseClickedVerpackungseinheit
	 * 
	 * @param bdMengeI MouseEvent
	 * @throws ExceptionLP
	 */
	protected void setMenge(BigDecimal bdMengeI) throws ExceptionLP {
		if (bdMengeI != null) {
			panelArtikel.wnfMenge.setBigDecimal(bdMengeI);
		}
	}

	protected void setPreis(Integer artikellieferantstaffelIId) throws ExceptionLP, Throwable {

		if (artikellieferantstaffelIId == null && panelArtikel.wnfMenge.getBigDecimal() != null && artLiefDto != null
				&& artLiefDto.getIId() != null) {

			// Passende Staffel suchen

			BigDecimal menge = panelArtikel.wnfMenge.getBigDecimal();

			if (Helper.short2boolean(panelArtikel.getArtikelDto().getBSummeInBestellung())) {

				menge = menge.add(
						DelegateFactory.getInstance().getBestellungDelegate().getSummeMengeGleicherArtikelInBestellung(
								getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getIId(),
								getInternalFrameBestellung().getTabbedPaneBestellung()
										.getPanelBestellungPositionenBottomD3().getBestellpositionDto().getIId(),
								panelArtikel.getArtikelDto().getIId()));
			}

			Map m = DelegateFactory.getInstance().getArtikelDelegate().getAlleGueltigenStaffelneinesLieferanten(
					artLiefDto.getIId(), tPBes.getBesDto().getWaehrungCNr(), tPBes.getBesDto().getDBelegdatum());

			java.util.Iterator it = m.keySet().iterator();
			while (it.hasNext()) {
				Integer key = (Integer) it.next();

				ArtikellieferantstaffelDto staffelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikellieferantstaffelFindByPrimaryKey(key);

				if (staffelDto.getNMenge().doubleValue() == menge.doubleValue()) {
					wcoStaffelRueckpflege.setKeyOfSelectedItem(staffelDto.getIId());
				}

				if (staffelDto.getNMenge().doubleValue() > menge.doubleValue()) {

					break;
				}

				artikellieferantstaffelIId = staffelDto.getIId();
			}

		}

		if (artikellieferantstaffelIId != null) {
			ArtikellieferantstaffelDto staffelDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikellieferantstaffelFindByPrimaryKey(artikellieferantstaffelIId);

			if (staffelDto.getNNettopreis() != null) {
				BigDecimal nettopreis = staffelDto.getNNettopreis();
				if (panelArtikel.wnfMaterialzuschlag != null
						&& panelArtikel.wnfMaterialzuschlag.getBigDecimal() != null) {
					nettopreis = nettopreis.add(panelArtikel.wnfMaterialzuschlag.getBigDecimal());
				}
				panelArtikel.wnfNettopreis.setBigDecimal(nettopreis);
			}
			if (staffelDto.getFRabatt() != null) {
				panelArtikel.wnfRabattsatz.setDouble(staffelDto.getFRabatt());
			}

			if (Helper.short2boolean(staffelDto.getBRabattbehalten()) == true) {
				panelArtikel.wnfRabattsumme.getWrbFixNumber().setSelected(true);
			} else {
				panelArtikel.wnfNettopreis.getWrbFixNumber().setSelected(true);
			}

			panelArtikel.wnfRabattsatz_focusLost(null);
		}

	}

	/**
	 * setPreis
	 * 
	 * @param artLiefDto ArtikelDto
	 * @throws ExceptionLP
	 */
	protected void setPreis(ArtikellieferantDto artLiefDto) throws ExceptionLP {

		if (artLiefDto != null) {
			if (artLiefDto.getNNettopreis() != null) {
				BigDecimal nettopreis = artLiefDto.getNNettopreis();
				if (panelArtikel.wnfMaterialzuschlag != null
						&& panelArtikel.wnfMaterialzuschlag.getBigDecimal() != null) {
					nettopreis = nettopreis.add(panelArtikel.wnfMaterialzuschlag.getBigDecimal());
				}
				panelArtikel.wnfNettopreis.setBigDecimal(nettopreis);

			}
			if (artLiefDto.getFRabatt() != null) {
				panelArtikel.wnfRabattsatz.setDouble(artLiefDto.getFRabatt());
			}

			if (artLiefDto.getNEinzelpreis() != null) {
				panelArtikel.wnfEinzelpreis.setBigDecimal(artLiefDto.getNEinzelpreis());
			}

			if (Helper.short2boolean(artLiefDto.getBRabattbehalten()) == true) {
				panelArtikel.wnfRabattsumme.getWrbFixNumber().setSelected(true);
			} else {
				panelArtikel.wnfNettopreis.getWrbFixNumber().setSelected(true);
			}

		}
	}

	protected void checkAndSetGelPreisDlg() throws ExceptionLP, Throwable {
		if (besPosDto.getBestellpositionartCNr().equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)) {
			if (panelArtikel.wnfNettopreis.getBigDecimal() != null) {
				BigDecimal gestpreis = DelegateFactory.getInstance().getLagerDelegate()
						.getGemittelterGestehungspreisEinesLagers(besPosDto.getArtikelIId(),
								DelegateFactory.getInstance().getLagerDelegate().getHauptlagerDesMandanten().getIId());
				if ((gestpreis.compareTo(new BigDecimal(0))) != 0) {
					BigDecimal bdEKAbweichung = Helper
							.calculateRatioInDecimal(panelArtikel.wnfNettopreis.getBigDecimal(), gestpreis);
					ParametermandantDto pmEKPreisabweichung = DelegateFactory.getInstance().getParameterDelegate()
							.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
									ParameterFac.PARAMETER_EINKAUSPREIS_ABWEICHUNG);
					BigDecimal bdEKPreisabweichung = new BigDecimal(pmEKPreisabweichung.getCWert());
					if ((bdEKPreisabweichung.compareTo(bdEKAbweichung)) < 0) {
						MessageFormat mf = new MessageFormat(LPMain.getTextRespectUISPr("bes.preisspanne.gestpreis"));
						mf.setLocale(LPMain.getTheClient().getLocUi());
						Object pattern[] = { pmEKPreisabweichung.getCWert(), bdEKAbweichung.toString() };
						String sMsg = mf.format(pattern);
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"), sMsg);
					}
				}

				ParametermandantDto pmGuenstiger = DelegateFactory.getInstance().getParameterDelegate()
						.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_BESTELLUNG,
								ParameterFac.PARAMETER_GUENSTIGER_MELDUNG_ANZEIGEN);
				boolean bGuenstigerMeldung = (Boolean) pmGuenstiger.getCWertAsObject();

				if (bGuenstigerMeldung == true) {
					ArtikellieferantDto artLiefDtoBilliger = DelegateFactory.getInstance().getArtikelDelegate()
							.getGuenstigstenEKPreis(panelArtikel.getArtikelDto().getIId(),
									panelArtikel.wnfMenge.getBigDecimal(),
									getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getDBelegdatum(),
									getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getWaehrungCNr(),
									getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto()
											.getLieferantIIdBestelladresse());

					// SP2911 / PJ19683 es wird beidseitig immter mit Zuschlag
					// verglichen
					BigDecimal vergleichspreis = panelArtikel.wnfNettopreis.getBigDecimal();

					if (artLiefDtoBilliger != null && artLiefDtoBilliger.getLief1Preis() != null
							&& artLiefDtoBilliger.getLieferantIId() != null
							&& artLiefDtoBilliger.getLief1Preis().doubleValue() < vergleichspreis.doubleValue()) {

						LieferantDto liefBilliger = DelegateFactory.getInstance().getLieferantDelegate()
								.lieferantFindByPrimaryKey(artLiefDtoBilliger.getLieferantIId());

						String message = "";
						if (DelegateFactory.getInstance().getTheJudgeDelegate()
								.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_EINKAUF)) {
							message = LPMain.getMessageTextRespectUISPr("bes.lief.billiger",
									liefBilliger.getPartnerDto().formatFixTitelName1Name2(),
									Helper.formatZahl(artLiefDtoBilliger.getLief1Preis(),
											Defaults.getInstance().getIUINachkommastellenPreiseEK(),
											LPMain.getTheClient().getLocUi()) + " "
											+ getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto()
													.getWaehrungCNr());
						} else {
							message = LPMain.getMessageTextRespectUISPr("bes.lief.billiger.ohne.preis",
									liefBilliger.getPartnerDto().formatFixTitelName1Name2());
						}

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"), message);
					}
				}

			}
		}
	}

	private void setEditorButtonColor() {
		getHmOfButtons().get(ACTION_TEXT).getButton()
				.setIcon(besPosDto.getXTextinhalt() != null && besPosDto.getXTextinhalt().length() > 0
						? IconFactory.getCommentExist()
						: IconFactory.getEditorEdit());
	}

	private void resetEditorButton() {
		getHmOfButtons().get(ACTION_TEXT).getButton().setIcon(IconFactory.getEditorEdit());
	}

}

/**
 * 
 * <p>
 * Diese Klasse kuemmert sich um Aenderungen des Einzelpreises.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Jsef Ornetsmueller; 17.11.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/02/05 09:15:20 $
 */

class FocusAdapterPreis implements FocusListener {
	private PanelBestellungPositionen adaptee = null;

	FocusAdapterPreis(PanelBestellungPositionen adaptee) {
		this.adaptee = adaptee;
	}

	public void focusGained(FocusEvent e) {
		// nothing here
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.actionPerformedPreisChanged();
		} catch (Throwable tDummy) {
			// nothing here
		}
	}
}

/**
 * 
 * <p>
 * Diese Klasse kuemmert sich um FocusLost des Feldes nMenge.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung:Josef Erlinger; 26.07.05
 * </p>
 * 
 * @version not attributable Date $Date: 2013/02/05 09:15:20 $
 */
class FocusAdapterMenge extends java.awt.event.FocusAdapter {
	private PanelBestellungPositionen adaptee;

	FocusAdapterMenge(PanelBestellungPositionen adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			if (adaptee.getBestellpositionDto().getIId() == null) {
				adaptee.actionPerformedPreisChanged();
				adaptee.refreshPreisnachpflegePanel();

				PanelPositionenArtikelBestellung panelArtikel = (PanelPositionenArtikelBestellung) adaptee.panelArtikel;

				if (adaptee.oArtikelDto.getNUmrechnungsfaktor() != null) {

					if (Helper.short2boolean(adaptee.oArtikelDto.getbBestellmengeneinheitInvers())) {
						panelArtikel.wnfGewichtbestellmenge.setBigDecimal(adaptee.panelArtikel.wnfMenge.getBigDecimal()
								.divide(adaptee.oArtikelDto.getNUmrechnungsfaktor(),
										Defaults.getInstance().getIUINachkommastellenPreiseEK(),
										BigDecimal.ROUND_HALF_UP));
					} else {
						panelArtikel.wnfGewichtbestellmenge.setBigDecimal(adaptee.panelArtikel.wnfMenge.getBigDecimal()
								.multiply(adaptee.oArtikelDto.getNUmrechnungsfaktor()));
					}

				}

				if (adaptee.oArtikelDto.getEinheitCNrBestellung() != null) {
					panelArtikel.wlaGewichtmengeEinheit
							.setText(adaptee.oArtikelDto.getEinheitCNrBestellung().trim() + "  ??");
				}

				if (adaptee.panelArtikel.wnfMenge.getDouble() > 1) {
					adaptee.setPreis((Integer) null);
				} else {
					adaptee.setPreis(adaptee.artLiefDto);
				}

			}
		} catch (Throwable t) {
			t.printStackTrace();
			if (t instanceof ExceptionLP) {
				adaptee.handleException(t, false);
			} else {
				LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
			}
		}
	}
}

class MouseAdapterChangeMenge extends MouseAdapter {
	private PanelBestellungPositionen adaptee;

	MouseAdapterChangeMenge(PanelBestellungPositionen adaptee) throws Exception {
		if (adaptee == null) {
			throw new Exception("adaptee == null");
		}
		this.adaptee = adaptee;
	}

	/**
	 * refresh je nach MouseEvent.source die Menge und den Preis
	 * 
	 * @param eI MouseEvent
	 * @return boolean
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	private boolean refreshPreisMenge(MouseEvent eI) throws Throwable {
		boolean bRefreshedMengePreis = true;
		if (eI.getSource() == adaptee.wlaVerpackungseinheit) {
			adaptee.setMenge(adaptee.wnfVerpackungseinheit.getBigDecimal());
		} else if (eI.getSource() == adaptee.wlaMindestbestellmenge) {
			adaptee.setMenge(adaptee.wnfMindestbestellmenge.getBigDecimal());
		} else {
			bRefreshedMengePreis = false;
		}
		return bRefreshedMengePreis;
	}

	public void mouseClicked(MouseEvent eI) {
		try {
			int lockstate = adaptee.getLockedstateDetailMainKey().getIState();
			if (lockstate == PanelBasis.LOCK_IS_LOCKED_BY_ME || lockstate == PanelBasis.LOCK_FOR_NEW) {
				if (refreshPreisMenge(eI)) {
					adaptee.actionPerformedPreisChanged();
				}
			}
		} catch (Throwable exDummy) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}
