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
package com.lp.client.frame.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.lp.client.anfrage.InternalFrameAnfrage;
import com.lp.client.bestellung.DialogGebindeUmrechnung;
import com.lp.client.bestellung.HelperBestellung;
import com.lp.client.bestellung.InternalFrameBestellung;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.artikel.service.GebindeDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;
import com.lp.util.KeyValue;

/*
 * <p><I>Basisklasse fuer Bestellvorschlag.</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>21.10.05</I></p>
 *
 * <p> </p>
 *
 * @author Josef Erlinger
 *
 * @version $Revision: 1.15 $
 */
public class PanelPositionenBestellvorschlag extends PanelPositionenPreiseingabe implements ListSelectionListener {

	private static final long serialVersionUID = -357479957188646788L;

	// eigenes Panel fuer die Felder ist wegen der Positionierung notwendig
	private JPanel jPanel = null;

	private WrapperLabel wlaLiefertermin = null;
	private WrapperDateField wdfLiefertermin = null;

	private WrapperLabel wlaMaterialzuschlagInfo = null;
	private WrapperNumberField wnfMaterialzuschlagInfo = null;
	private WrapperLabel wlaMaterialzuschlagWaehrung = null;

	private WrapperLabel wlaOffeneRahmenmenge = new WrapperLabel();
	private WrapperNumberField wnfOffeneRahmenmenge = new WrapperNumberField();

	private JButton buArtikellieferantUebernehmen = null;;

	private PanelQueryFLR panelQueryFLRLieferant = null;

	private WrapperSelectField wsfProjekt = new WrapperSelectField(WrapperSelectField.PROJEKT, getInternalFrame(),
			true);

	private WrapperCheckBox wcbVormerkliste = new WrapperCheckBox();

	private WrapperSelectField wsfLiefergruppe = new WrapperSelectField(WrapperSelectField.LIEFERGRUPPE,
			getInternalFrame(), true);

	static final private String ACTION_SPECIAL_LIEFERANT_AENDERN = "action_special_lieferantaendern";

	public final static String MY_OWN_NEW_TOGGLE_BEARBEITET = PanelBasis.ACTION_MY_OWN_NEW + "MY_OWN_NEW_BEARBEITET";

	static final private String ACTION_SPECIAL_ARTIKELLIEFERANTUEBERNEHMEN = "action_special_artikellieferant_uebernehmen";

	private BestellvorschlagDto bestellvorschlagDto = null;
	private LieferantDto lieferantDto = null;

	public WrapperButton wbuPreisauswahl = null;
	static final public String ACTION_SPECIAL_EK_PREIS_HOLEN = "action_special_ek_preis_holen";

	public WrapperEditorField wefText = null;

	private WrapperLabel wlaStandort = null;
	private WrapperComboBox wcbStandort = null;

	private WrapperButton buttonGebinde = new WrapperButton();
	public String ACTION_GEBINDE = "ACTION_GEBINDE";

	// JE: auf Staffelmengen entweder bei focusLost oder spaetestens beim
	// Speichern pruefen, aber nicht mehrfach
	private boolean checkMenge = true;

	private WrapperKeyValueField wkvArtikelbezLieferant = new WrapperKeyValueField(
			Defaults.getInstance().bySizeFactor(200));

	JList list = null;
	ArrayList<KeyValue> mArtikellieferanten = new ArrayList<KeyValue>();

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame    der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI       der default Titel des Panels
	 * @param key              PK der Position
	 * @param iSpaltenbreite1I die Breite der ersten Spalte
	 * @throws java.lang.Throwable Ausnahme
	 */
	public PanelPositionenBestellvorschlag(InternalFrame internalFrame, String add2TitleI, Object key,
			int iSpaltenbreite1I) throws Throwable {
		super(internalFrame, add2TitleI, key, HelperClient.LOCKME_BESTELLVORSCHLAG,
				internalFrame.bRechtDarfPreiseSehenEinkauf, internalFrame.bRechtDarfPreiseAendernEinkauf,
				iSpaltenbreite1I, null);
		jbInit();
		initComponents();
		initPanel();
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout und einen Rahmen nach
		// innen
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEmptyBorder(3, 3, 10, 3));

		// Zeile - die Toolbar
		add(getToolsPanel(), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// ohne Button new
		resetToolsPanel();

		// zusaetzliche Buttons setzen
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, ACTION_TEXT };

		wefText = new WrapperEditorFieldKommentar(getInternalFrame(), LPMain.getTextRespectUISPr("label.text"));

		wkvArtikelbezLieferant.setKey(LPMain.getTextRespectUISPr("bes.artikelbezeichnungbeimlieferanten"));

		buArtikellieferantUebernehmen = new JButton(
				LPMain.getTextRespectUISPr("bes.bestellvorschlag.artikellieferantuebernehmen"));
		buArtikellieferantUebernehmen.setActionCommand(ACTION_SPECIAL_ARTIKELLIEFERANTUEBERNEHMEN);
		buArtikellieferantUebernehmen.addActionListener(this);

		list = new JList();

		Color defaultCellForegroundColor = UIManager.getColor("Table.foreground");

		list.setSelectionForeground(defaultCellForegroundColor);
		// list.setFont(new java.awt.Font("monospaced", 0, 12));
		list.setFont(new java.awt.Font("monospaced", 0, 11));
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		// listScroller.setMinimumSize(new Dimension(250, 80));
		// listScroller.setPreferredSize(new Dimension(300, 80));
		list.addListSelectionListener(this);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		enableToolsPanelButtons(aWhichButtonIUse);

		// hier kommen die Felder drauf

		jPanel = new JPanel();
		jPanel.setLayout(new GridBagLayout());

		iZeile++;
		add(jPanel, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		iZeile++;
		add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// alle speziellen Felder
		wlaLiefertermin = new WrapperLabel(LPMain.getTextRespectUISPr("lp.liefertermin"));
		wdfLiefertermin = new WrapperDateField();

		wlaMaterialzuschlagInfo = new WrapperLabel(LPMain.getTextRespectUISPr("lp.materialzuschlag"));
		wlaMaterialzuschlagWaehrung = new WrapperLabel(LPMain.getTheClient().getSMandantenwaehrung());
		wlaMaterialzuschlagWaehrung.setHorizontalAlignment(SwingConstants.LEADING);
		wnfMaterialzuschlagInfo = new WrapperNumberField();
		wnfMaterialzuschlagInfo.setActivatable(false);

		wnfMaterialzuschlagInfo.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());

		// Zeile 1 + 2 ist der Artikelblock
		int iGridBagLayout = 0;
		addArtikelblock(jPanel, iGridBagLayout);

		iGridBagLayout++;
		iGridBagLayout++;
		addFormatierungszeileNettoeinzelpreis(jPanel, iGridBagLayout);

		iGridBagLayout++;

		wlaOffeneRahmenmenge.setText(LPMain.getTextRespectUISPr("bes.bv.offenerahmenmenge"));
		wnfOffeneRahmenmenge.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());
		wnfOffeneRahmenmenge.setActivatable(false);
		jPanel.add(wlaOffeneRahmenmenge, new GridBagConstraints(0, iGridBagLayout, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));
		jPanel.add(wnfOffeneRahmenmenge, new GridBagConstraints(1, iGridBagLayout, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		addZeileRabattsumme(jPanel, iGridBagLayout);

		iGridBagLayout++;
		addZeileMaterialzuschlag(jPanel, iGridBagLayout + 2);

		jPanel.add(wlaMaterialzuschlagInfo, new GridBagConstraints(3, iGridBagLayout + 2, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		jPanel.add(wnfMaterialzuschlagInfo, new GridBagConstraints(6, iGridBagLayout + 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jPanel.add(wlaMaterialzuschlagWaehrung, new GridBagConstraints(7, iGridBagLayout + 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		//
		wlaStandort = new WrapperLabel(LPMain.getTextRespectUISPr("system.standort"));

		wcbStandort = new WrapperComboBox();
		wcbStandort.setMandatoryField(true);
		wcbStandort.setMap(DelegateFactory.getInstance().getLagerDelegate().getAlleStandorte());

		iGridBagLayout++;
		addZeileNettogesamtpreis(jPanel, iGridBagLayout, false);

		wbuPreisauswahl = new WrapperButton();
		HelperClient.setDefaultsToComponent(wbuPreisauswahl, 70);

		wbuPreisauswahl.setActionCommand(ACTION_SPECIAL_EK_PREIS_HOLEN);
		wbuPreisauswahl.addActionListener(this);
		wbuPreisauswahl.setText(LPMain.getTextRespectUISPr("button.preis"));

		// darf Preis sehen Recht, keinen Button zeigen, wenn nicht erlaubt
		if (!bRechtDarfPreiseSehen) {
			wbuPreisauswahl.setVisible(false);
		} else {
			wlaEinzelpreis.setVisible(false);
		}

		jPanel.add(wbuPreisauswahl, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		wtfArtikel.setMandatoryField(true);
		wnfMenge.addFocusListener(new WnfMengeVorschlagFocusAdapter(this));
		wtfBezeichnung.setActivatable(false);
		wcoEinheit.setActivatable(false);

		// jetzt alle zusaetzlichen Felder
		iGridBagLayout++;
		jPanel.add(wlaLiefertermin, new GridBagConstraints(0, iGridBagLayout, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanel.add(wdfLiefertermin, new GridBagConstraints(1, iGridBagLayout, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		this.createAndSaveAndShowButton("/com/lp/client/res/address_book216x16.png",
				LPMain.getTextRespectUISPr("bes.bestellvorschlag.lieferant.aendern"), ACTION_SPECIAL_LIEFERANT_AENDERN,
				RechteFac.RECHT_BES_BESTELLUNG_CUD);

		this.createAndSaveAndShowButton("/com/lp/client/res/check2.png",
				LPMain.getTextRespectUISPr("bes.bestellvorschlag.bearbeitet"), MY_OWN_NEW_TOGGLE_BEARBEITET, null,
				RechteFac.RECHT_BES_BESTELLUNG_CUD);

		wkvArtikelbezLieferant.setMinimumSize(new Dimension(458, 23));
		wkvArtikelbezLieferant.setPreferredSize(new Dimension(458, 23));
		getToolBar().getToolsPanelRight().add(wkvArtikelbezLieferant);

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {
			iGridBagLayout++;
			jPanel.add(wsfProjekt.getWrapperGotoButton(), new GridBagConstraints(0, iGridBagLayout, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jPanel.add(wsfProjekt.getWrapperTextField(), new GridBagConstraints(1, iGridBagLayout, 5, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		wcbVormerkliste.setText(LPMain.getTextRespectUISPr("bes.vormerkung"));
		jPanel.add(wcbVormerkliste, new GridBagConstraints(6, iGridBagLayout, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 80, 0));

		jPanel.add(wsfLiefergruppe.getWrapperButton(), new GridBagConstraints(6, iGridBagLayout, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 90, 2, 2), 90, 0));

		jPanel.add(wsfLiefergruppe.getWrapperTextField(), new GridBagConstraints(7, iGridBagLayout, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iGridBagLayout++;

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
				ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);
		boolean lagerminJeLager = ((Boolean) parameter.getCWertAsObject());

		if (lagerminJeLager) {

			jPanel.add(wlaStandort, new GridBagConstraints(3, iGridBagLayout, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jPanel.add(wcbStandort, new GridBagConstraints(6, iGridBagLayout, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			iGridBagLayout++;
		}

		jPanel.add(buArtikellieferantUebernehmen, new GridBagConstraints(0, iGridBagLayout, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanel.add(listScroller, new GridBagConstraints(1, iGridBagLayout, 7, 1, 0.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 40));

		buttonGebinde.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/cookies.png")));
		buttonGebinde.setActionCommand(ACTION_GEBINDE);
		buttonGebinde.addActionListener(this);

		jPanel.add(buttonGebinde, new GridBagConstraints(0, iZeileMenge, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 10, 0));

	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			list.setSelectedIndex(list.getSelectedIndex());
			buArtikellieferantUebernehmen.setEnabled(true);
			int i = list.getSelectedIndex();

			if (i >= 0) {

				KeyValue map = mArtikellieferanten.get(i);
				try {
					ArtikellieferantDto artikellieferantDto = (ArtikellieferantDto) map.getOKey();
					if (artikellieferantDto != null) {
						if (!artikellieferantDto.getLieferantDto().getMandantCNr()
								.equals(LPMain.getTheClient().getMandant())) {
							buArtikellieferantUebernehmen.setEnabled(false);
						}
					}
				} catch (Throwable e1) {
					handleException(e1, true);
				}
			}

		}
	}

	private void initPanel() throws Throwable {
		setWaehrungCNr(LPMain.getTheClient().getSMandantenwaehrung());

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);

		if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERANT_AENDERN)) {
			panelQueryFLRLieferant = PartnerFilterFactory.getInstance().createPanelFLRLieferant(getInternalFrame(),
					(lieferantDto != null) ? lieferantDto.getIId() : null, true, false);
			new DialogQuery(panelQueryFLRLieferant);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_EK_PREIS_HOLEN)) {
			wnfMengeVorschlagFocusLost(null);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELLIEFERANTUEBERNEHMEN)) {
			int i = list.getSelectedIndex();

			if (i >= 0) {

				KeyValue kv = mArtikellieferanten.get(i);

				ArtikellieferantDto artikellieferantDto = (ArtikellieferantDto) kv.getOKey();
				if (artikellieferantDto != null && artikellieferantDto.getLieferantDto().getMandantCNr()
						.equals(LPMain.getTheClient().getMandant())) {

					bestellvorschlagDto.setILieferantId(artikellieferantDto.getLieferantIId());
					bestellvorschlagDto.setIWiederbeschaffungszeit(artikellieferantDto.getIWiederbeschaffungszeit());

					if (artikellieferantDto.getNEinzelpreis() != null) {

						bestellvorschlagDto.setNNettoeinzelpreis(artikellieferantDto.getNEinzelpreis());
						bestellvorschlagDto.setDRabattsatz(artikellieferantDto.getFRabatt());

						BigDecimal nettopreis = artikellieferantDto.getNNettopreis();

						if (wnfMaterialzuschlag != null && wnfMaterialzuschlag.getBigDecimal() != null) {
							nettopreis = nettopreis.add(wnfMaterialzuschlag.getBigDecimal());
						}
						bestellvorschlagDto.setNNettogesamtpreis(nettopreis);

						bestellvorschlagDto.setBNettopreisuebersteuert(
								Helper.boolean2Short(!Helper.short2boolean(artikellieferantDto.getBRabattbehalten())));

						if (artikellieferantDto.getNNettopreis() != null
								&& artikellieferantDto.getNEinzelpreis() != null) {

							bestellvorschlagDto.setNRabattbetrag(artikellieferantDto.getNEinzelpreis()
									.subtract(artikellieferantDto.getNNettopreis()));

						} else {
							bestellvorschlagDto.setNRabattbetrag(BigDecimal.ZERO);
						}
					}
					DelegateFactory.getInstance().getBestellvorschlagDelegate()
							.updateBestellvorschlag(bestellvorschlagDto);

					if (getInternalFrame() instanceof InternalFrameBestellung) {
						((InternalFrameBestellung) getInternalFrame()).getTabbedPaneBestellvorschlag()
								.getPanelBestellungVorschlagSP1().eventYouAreSelected(false);
					} else if (getInternalFrame() instanceof InternalFrameAnfrage) {
						((InternalFrameAnfrage) getInternalFrame()).getTabbedPaneAnfragevorschlag()
								.getPanelAnfragevorschlagSP1().eventYouAreSelected(false);
					}

				}
			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
						LPMain.getTextRespectUISPr("bes.bestellvorschlag.artikellieferantuebernehmen.keineintrag"));
			}
		} else if (e.getActionCommand().equals(ACTION_GEBINDE)) {
			positionsmengeueberGebindeBerechnen();
		} else if (e.getActionCommand().equals(MY_OWN_NEW_TOGGLE_BEARBEITET)) {

			ArrayList<Integer> ids = null;

			if (getInternalFrame() instanceof InternalFrameBestellung) {
				ids = ((InternalFrameBestellung) getInternalFrame()).getTabbedPaneBestellvorschlag()
						.getPanelBestellungVorschlagSP1().getPanelQuery().getSelectedIdsAsInteger();
			} else if (getInternalFrame() instanceof InternalFrameAnfrage) {
				ids = ((InternalFrameAnfrage) getInternalFrame()).getTabbedPaneAnfragevorschlag()
						.getPanelAnfragevorschlagSP1().getPanelQuery().getSelectedIdsAsInteger();
			}

			if (ids != null) {
				for (int i = 0; i < ids.size(); i++) {
					DelegateFactory.getInstance().getBestellvorschlagDelegate().toggleBearbeitet(ids.get(i));
				}
			}

			if (getInternalFrame() instanceof InternalFrameBestellung) {
				((InternalFrameBestellung) getInternalFrame()).getTabbedPaneBestellvorschlag()
						.getPanelBestellungVorschlagSP1().eventYouAreSelected(false);
			} else if (getInternalFrame() instanceof InternalFrameAnfrage) {
				((InternalFrameAnfrage) getInternalFrame()).getTabbedPaneAnfragevorschlag()
						.getPanelAnfragevorschlagSP1().eventYouAreSelected(false);
			}

		}
	}

	protected void setDefaults() throws Throwable {

		resetEditorButton();

		leereAlleFelder(this);

		super.setDefaults();

		bestellvorschlagDto = new BestellvorschlagDto();
		lieferantDto = new LieferantDto();

		wdfLiefertermin.setTimestamp(new Timestamp(System.currentTimeMillis()));
		wnfRabattsumme.getWrbFixNumber().setSelected(true);
		wefText.getLpEditor().setText(null);
		wcbVormerkliste.setSelected(true);
		wcbVormerkliste.setText(LPMain.getTextRespectUISPr("bes.vormerkung"));
	}

	protected void components2Dto() throws Throwable {

		bestellvorschlagDto.setCMandantCNr(LPMain.getTheClient().getMandant());
		bestellvorschlagDto.setIArtikelId(getArtikelDto().getIId());
		bestellvorschlagDto.setNZubestellendeMenge(wnfMenge.getBigDecimal());
		bestellvorschlagDto.setTLiefertermin(wdfLiefertermin.getTimestamp());
		// bestellvorschlagDto.setCBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
		// bestellvorschlagDto.setIBelegartId(null);
		bestellvorschlagDto.setILieferantId(lieferantDto.getIId());

		// Bestellvorschlag in Mandantenwaehrung == UI-Anzeige
		bestellvorschlagDto.setNNettoeinzelpreis(wnfEinzelpreis.getBigDecimal());
		bestellvorschlagDto.setDRabattsatz(getWnfRabattsatz().getDouble());
		bestellvorschlagDto.setNRabattbetrag(wnfRabattsumme.getBigDecimal());
		// UW->JE Nettogesamtpreisminusrabatte -> Spalte entfernen?
		bestellvorschlagDto
				.setNNettogesamtpreis(wnfEinzelpreis.getBigDecimal().subtract(wnfRabattsumme.getBigDecimal()));

		bestellvorschlagDto
				.setBNettopreisuebersteuert(Helper.boolean2Short(wnfNettopreis.getWrbFixNumber().isSelected()));
		bestellvorschlagDto.setProjektIId(wsfProjekt.getIKey());

		bestellvorschlagDto.setXTextinhalt(wefText.getText());
		bestellvorschlagDto.setBVormerkung(wcbVormerkliste.getShort());
		bestellvorschlagDto.setPartnerIIdStandort((Integer) wcbStandort.getKeyOfSelectedItem());

		bestellvorschlagDto.setPersonalIIdBearbeitet(LPMain.getTheClient().getIDPersonal());
		bestellvorschlagDto.setTBearbeitet(new Timestamp(System.currentTimeMillis()));

		// SP3530 Nur bei Bestellvorschlag rueckpflegen
		if (getInternalFrame() instanceof InternalFrameBestellung) {
			// @todo UW->JE koennte man am Server machen? JE ja PJ 5038
			checkArtikellieferantAndCreateOrUpdate();
		}

		// PJ21761
		ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(getArtikelDto().getIId());
		artikelDto.setLfliefergruppeIId(wsfLiefergruppe.getIKey());
		DelegateFactory.getInstance().getArtikelDelegate().updateArtikel(artikelDto);

	}

	private void positionsmengeueberGebindeBerechnen() throws Throwable {

		if (bestellvorschlagDto.getILieferantId() != null) {
			// Gebindeauswahl
			ArrayList<GebindeDto> alGebinde = DelegateFactory.getInstance().getArtikelDelegate()
					.getGebindeEinesArtikelsUndEinesLieferanten(getArtikelDto().getIId(),
							bestellvorschlagDto.getILieferantId(),
							new java.sql.Date(bestellvorschlagDto.getTLiefertermin().getTime()));

			if (alGebinde.size() > 0) {

				DialogGebindeUmrechnung dGebinde = new DialogGebindeUmrechnung(getArtikelDto().getIId(),
						bestellvorschlagDto.getILieferantId(), alGebinde, wnfMenge.getBigDecimal(),
						bestellvorschlagDto.getGebindeIId());
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(dGebinde);
				dGebinde.setVisible(true);

				if (dGebinde.getBdNeuePositionsmenge() != null && dGebinde.getGebindeDtoSelektiert() != null
						|| dGebinde.isKeinGebinde()) {

					if (dGebinde.isKeinGebinde()) {
						// SP3724
						bestellvorschlagDto.setGebindeIId(null);
						bestellvorschlagDto.setNAnzahlgebinde(null);
					} else {
						// Menge setzen
						wnfMenge.setBigDecimal(dGebinde.getBdNeuePositionsmenge());

						bestellvorschlagDto.setGebindeIId(dGebinde.getGebindeDtoSelektiert().getIId());
						bestellvorschlagDto.setNAnzahlgebinde(dGebinde.getBdAnzahlGebinde());
					}
				}
			}
		}
	}

	protected void dto2Components() throws Throwable {
		artikelDto2components();

		buttonGebinde.setVisible(false);

		if (bestellvorschlagDto.getILieferantId() != null) {

			ArrayList<GebindeDto> alGebinde = DelegateFactory.getInstance().getArtikelDelegate()
					.getGebindeEinesArtikelsUndEinesLieferanten(getArtikelDto().getIId(),
							bestellvorschlagDto.getILieferantId(),
							new java.sql.Date(bestellvorschlagDto.getTLiefertermin().getTime()));

			if (alGebinde.size() > 0) {
				buttonGebinde.setVisible(true);

				Integer gebindeIId = bestellvorschlagDto.getGebindeIId();
				BigDecimal bgAnzahlGebinde = bestellvorschlagDto.getNAnzahlgebinde();

				String toolTip = "";

				if (gebindeIId != null && bgAnzahlGebinde != null && bgAnzahlGebinde.doubleValue() != 0) {
					String gebinde = DelegateFactory.getInstance().getArtikelDelegate()
							.gebindeFindByPrimaryKey(gebindeIId).getCBez();

					BigDecimal bdGebindemenge = bestellvorschlagDto.getNZubestellendeMenge().divide(bgAnzahlGebinde, 6,
							BigDecimal.ROUND_HALF_EVEN);

					toolTip = gebinde + " ("
							+ Helper.formatZahl(bdGebindemenge, Defaults.getInstance().getIUINachkommastellenMenge(),
									LPMain.getTheClient().getLocUi())
							+ " " + getArtikelDto().getEinheitCNr().trim() + ")";

				}

				buttonGebinde.setToolTipText(toolTip);
			}
		}

		// die Bezeichnung kann durch die Positionen uebersteuert werden
		String cBezeichnungUebersteuert = null;

		if (bestellvorschlagDto.getIBelegartId() != null) {
			if (bestellvorschlagDto.getCBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)) {
				AuftragpositionDto auftragpositionDto = DelegateFactory.getInstance().getAuftragpositionDelegate()
						.auftragpositionFindByPrimaryKeyOhneExc(bestellvorschlagDto.getIBelegartpositionid());

				if (auftragpositionDto != null && auftragpositionDto.getCBez() != null
						&& auftragpositionDto.getCBez().length() > 0) {
					cBezeichnungUebersteuert = auftragpositionDto.getCBez();
				}
			} else if (bestellvorschlagDto.getCBelegartCNr().equals(LocaleFac.BELEGART_BESTELLUNG)) {
				BestellpositionDto bestellpositionDto = DelegateFactory.getInstance().getBestellungDelegate()
						.bestellpositionFindByPrimaryKeyOhneExc(bestellvorschlagDto.getIBelegartpositionid());

				if (bestellpositionDto != null && bestellpositionDto.getCBez() != null
						&& bestellpositionDto.getCBez().length() > 0) {
					cBezeichnungUebersteuert = bestellpositionDto.getCBez();
				}
			}
		}

		if (cBezeichnungUebersteuert != null) {
			wtfBezeichnung.setText(cBezeichnungUebersteuert);
		}

		mArtikellieferanten = DelegateFactory.getInstance().getArtikelDelegate().getListeDerArtikellieferanten(
				bestellvorschlagDto.getIId(), bestellvorschlagDto.getNZubestellendeMenge());
		list.removeAll();

		Object[] tempZeilen = new Object[mArtikellieferanten.size()];

		for (int i = 0; i < mArtikellieferanten.size(); i++) {
			KeyValue m = mArtikellieferanten.get(i);
			tempZeilen[i] = m.getOValue();
		}

		list.setListData(tempZeilen);

		wefText.setText(bestellvorschlagDto.getXTextinhalt());

		wnfMenge.setBigDecimal(bestellvorschlagDto.getNZubestellendeMenge());
		wdfLiefertermin.setTimestamp(bestellvorschlagDto.getTLiefertermin());

		if (lieferantDto.getIId() != null) {
			String cArtikelBez = null;

			ArtikellieferantDto artikellieferantDto = DelegateFactory.getInstance().getArtikelDelegate()
					.getArtikelEinkaufspreis(getArtikelDto().getIId(), lieferantDto.getIId(), BigDecimal.ONE,
							LPMain.getTheClient().getSMandantenwaehrung(),
							new java.sql.Date(System.currentTimeMillis()));

			if (artikellieferantDto != null) {
				cArtikelBez = artikellieferantDto.getCBezbeilieferant(); // nur
				// Anzeige
			}

			wkvArtikelbezLieferant.setValue(cArtikelBez);
		} else {

			wkvArtikelbezLieferant.setValue("");
		}

		// PJ18006 Materialzuschlag nur als Info anzeigen
		setVisibleZeileMaterialzuschlag(false);
		wnfMaterialzuschlag.setBigDecimal(null);
		// der Bestellvorschlag wird in Mandantenwaehrung abgespeichert und
		// angezeigt UW->JE
		if (Helper.short2boolean(bestellvorschlagDto.getBNettopreisuebersteuert())) {
			wnfNettopreis.getWrbFixNumber().setSelected(true);
		} else {
			wnfRabattsumme.getWrbFixNumber().setSelected(true);
		}

		Hashtable<?, ?> ht = DelegateFactory.getInstance().getArtikelbestelltDelegate()
				.getAnzahlRahmenbestellt(getArtikelDto().getIId());
		if (ht.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
			BigDecimal rahmenbestellt = (BigDecimal) ht.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
			wnfOffeneRahmenmenge.setBigDecimal(rahmenbestellt);
		} else {
			wnfOffeneRahmenmenge.setBigDecimal(null);
		}

		wnfEinzelpreis.setBigDecimal(bestellvorschlagDto.getNNettoeinzelpreis());
		getWnfRabattsatz().setDouble(bestellvorschlagDto.getDRabattsatz());
		wnfRabattsumme.setBigDecimal(bestellvorschlagDto.getNRabattbetrag());
		wnfNettopreis.setBigDecimal(bestellvorschlagDto.getNNettogesamtpreis());
		// UW->JE Nettogesamtpreisminusrabatte -> Spalte entfernen?
		wnfMaterialzuschlagInfo.setVisible(false);
		wlaMaterialzuschlagInfo.setVisible(false);
		wlaMaterialzuschlagWaehrung.setVisible(false);
		wnfMaterialzuschlagInfo.setBigDecimal(new BigDecimal(0));
		wcbVormerkliste.setShort(bestellvorschlagDto.getBVormerkung());

		if (bestellvorschlagDto.getTVormerkung() != null && bestellvorschlagDto.getPersonalIIdVormerkung() != null) {

			PersonalDto personalDtoVerrechnen = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(bestellvorschlagDto.getPersonalIIdVormerkung());

			wcbVormerkliste.setText(LPMain.getTextRespectUISPr("bes.vormerkung") + " ("

					+ Helper.formatDatum(bestellvorschlagDto.getTVormerkung(), LPMain.getTheClient().getLocUi()) + ", "
					+ personalDtoVerrechnen.formatAnrede() + ")");

		}

		if (getArtikelDto().getMaterialIId() != null && lieferantDto.getIId() != null) {

			wnfMaterialzuschlagInfo.setVisible(true);
			wlaMaterialzuschlagInfo.setVisible(true);
			wlaMaterialzuschlagWaehrung.setVisible(true);

			BigDecimal zuschlag = DelegateFactory.getInstance().getMaterialDelegate()
					.getMaterialzuschlagEKInZielwaehrung(getArtikelDto().getIId(), lieferantDto.getIId(),
							new java.sql.Date(System.currentTimeMillis()),
							LPMain.getTheClient().getSMandantenwaehrung());

			wnfMaterialzuschlagInfo.setBigDecimal(zuschlag);
		}

		wsfProjekt.setKey(bestellvorschlagDto.getProjektIId());
		wcbStandort.setKeyOfSelectedItem(bestellvorschlagDto.getPartnerIIdStandort());

		wsfLiefergruppe.setKey(wifArtikelauswahl.getArtikelDto().getLfliefergruppeIId());

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged((ItemChangedEvent) eI);

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLieferant) {
				Integer iIdLieferant = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(iIdLieferant);
				bestellvorschlagDto.setILieferantId(lieferantDto.getIId());

				if (bestellvorschlagDto.getIArtikelId() != null) {
					// MB 17.05.06 mit WH ausgemacht:
					// 1. schaun, obs einen artikellieferant-eintrag zu
					// diesem lieferanten gibt

					ArtikellieferantDto alDto = DelegateFactory.getInstance().getArtikelDelegate()
							.getArtikelEinkaufspreis(bestellvorschlagDto.getIArtikelId(), iIdLieferant, BigDecimal.ONE,
									LPMain.getTheClient().getSMandantenwaehrung(),
									new java.sql.Date(System.currentTimeMillis()));

					// 2. wenns einen gibt, dann gleiches Verhalten wie bei
					// Artikellieferant-Auswahl
					if (alDto != null && alDto.getNEinzelpreis() != null) {
						// artikellieferantDto in Mandantenwaehrung holen
						ArtikellieferantDto artikellieferantDto = DelegateFactory.getInstance().getArtikelDelegate()
								.artikellieferantFindByArtikellIIdLieferantIIdInWunschwaehrung(
										bestellvorschlagDto.getIArtikelId(), iIdLieferant,
										LPMain.getTheClient().getSMandantenwaehrung());
						if (artikellieferantDto != null) {
							wkvArtikelbezLieferant.setValue(artikellieferantDto.getCBezbeilieferant());

							bestellvorschlagDto
									.setIWiederbeschaffungszeit(artikellieferantDto.getIWiederbeschaffungszeit());

							bestellvorschlagDto.setNNettoeinzelpreis(artikellieferantDto.getNEinzelpreis());
							bestellvorschlagDto.setDRabattsatz(artikellieferantDto.getFRabatt());

							wnfEinzelpreis.setBigDecimal(artikellieferantDto.getNEinzelpreis());

							BigDecimal nettopreis = artikellieferantDto.getNNettopreis();

							if (wnfMaterialzuschlag != null && wnfMaterialzuschlag.getBigDecimal() != null) {
								nettopreis = nettopreis.add(wnfMaterialzuschlag.getBigDecimal());
							}
							bestellvorschlagDto.setNNettogesamtpreis(nettopreis);

							bestellvorschlagDto.setBNettopreisuebersteuert(Helper
									.boolean2Short(!Helper.short2boolean(artikellieferantDto.getBRabattbehalten())));

							wnfNettopreis.setBigDecimal(nettopreis);

							if (artikellieferantDto.getNNettopreis() != null
									&& artikellieferantDto.getNEinzelpreis() != null) {

								bestellvorschlagDto.setNRabattbetrag(artikellieferantDto.getNEinzelpreis()
										.subtract(artikellieferantDto.getNNettopreis()));

							} else {
								bestellvorschlagDto.setNRabattbetrag(BigDecimal.ZERO);
							}
						}
					} else {
						// SP3019 Fragen, ob Artikellieferant nachgetragen
						// werden soll

						int indexNichtEintragen = 0;
						int indexEintragen = 1;
						int indexAlsBevorzugtEintragen = 2;
						int iAnzahlOptionen = 3;

						Object[] aOptionen = new Object[iAnzahlOptionen];
						aOptionen[indexNichtEintragen] = LPMain.getTextRespectUISPr(
								"bes.bestellvorschlag.lieferantaendern.artikellieferant.eintragen.nicht");
						aOptionen[indexEintragen] = LPMain.getTextRespectUISPr(
								"bes.bestellvorschlag.lieferantaendern.artikellieferant.eintragen.eintragen");
						aOptionen[indexAlsBevorzugtEintragen] = LPMain.getTextRespectUISPr(
								"bes.bestellvorschlag.lieferantaendern.artikellieferant.eintragen.alsbevorzugteintragen");

						int iAuswahl = DialogFactory.showModalDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr(
										"bes.bestellvorschlag.lieferantaendern.artikellieferant.eintragen.meldung"),
								LPMain.getTextRespectUISPr("lp.frage"), aOptionen, aOptionen[0]);

						if (iAuswahl == indexEintragen || iAuswahl == indexAlsBevorzugtEintragen) {

							ArtikellieferantDto artikellieferantDto = new ArtikellieferantDto();

							artikellieferantDto.setLieferantIId(lieferantDto.getIId());
							artikellieferantDto.setArtikelIId(bestellvorschlagDto.getIArtikelId());
							artikellieferantDto
									.setTPreisgueltigab(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())));

							artikellieferantDto.setBRabattbehalten(wnfRabattsumme.getWrbFixNumber().getShort());

							if (wnfEinzelpreis.getBigDecimal() != null && wnfRabattsatz.getDouble() != null
									&& wnfNettopreis.getBigDecimal() != null) {
								artikellieferantDto.setNEinzelpreis(wnfEinzelpreis.getBigDecimal());
								artikellieferantDto.setFRabatt(wnfRabattsatz.getDouble());
								artikellieferantDto.setNNettopreis(wnfNettopreis.getBigDecimal());
							}

							Integer artikellieferantIId = DelegateFactory.getInstance().getArtikelDelegate()
									.createArtikellieferant(artikellieferantDto);

							if (iAuswahl == indexAlsBevorzugtEintragen) {
								DelegateFactory.getInstance().getArtikelDelegate().artikellieferantAlsErstesReihen(
										bestellvorschlagDto.getIArtikelId(), artikellieferantIId);
							}
						}
					}
				}

				DelegateFactory.getInstance().getBestellvorschlagDelegate().updateBestellvorschlag(bestellvorschlagDto);

				if (getInternalFrame() instanceof InternalFrameBestellung) {

					((InternalFrameBestellung) getInternalFrame()).getTabbedPaneBestellvorschlag()
							.getPanelBestellungVorschlagSP1().eventYouAreSelected(false);
				}

				if (getInternalFrame() instanceof InternalFrameAnfrage) {

					((InternalFrameAnfrage) getInternalFrame()).getTabbedPaneAnfragevorschlag()
							.getPanelAnfragevorschlagSP1().eventYouAreSelected(false);
				}

			} else if (e.getSource() == wifArtikelauswahl.getPanelQueryFLRArtikel()) {

				Integer artikelIId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				ArtikellieferantDto[] dtos = DelegateFactory.getInstance().getArtikelDelegate()
						.artikellieferantFindByArtikelIId(artikelIId);

				if (dtos.length > 0) {
					holeArtikellieferant(dtos[0].getIId());
				}
			}
		}
	}

	private void holeArtikellieferant(Integer iIdArtikelLieferant) throws ExceptionLP, Throwable {

		ArtikellieferantDto alDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikellieferantFindByPrimaryKey(iIdArtikelLieferant);
		// nun den Preis in Mandantenwaehrung holen
		ArtikellieferantDto artikellieferantDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikellieferantFindByIIdInWunschwaehrung(alDto.getIId(),
						LPMain.getTheClient().getSMandantenwaehrung());
		if (artikellieferantDto != null) {
			lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
					.lieferantFindByPrimaryKey(artikellieferantDto.getLieferantIId());

			bestellvorschlagDto.setILieferantId(lieferantDto.getIId());
			bestellvorschlagDto.setIWiederbeschaffungszeit(artikellieferantDto.getIWiederbeschaffungszeit());

			wkvArtikelbezLieferant.setValue(artikellieferantDto.getCBezbeilieferant());

			wnfEinzelpreis.setBigDecimal(artikellieferantDto.getNEinzelpreis());
			getWnfRabattsatz().setDouble(artikellieferantDto.getFRabatt());

			BigDecimal nettopreis = artikellieferantDto.getNNettopreis();

			if (nettopreis == null) {
				nettopreis = BigDecimal.ZERO;
			}

			if (wnfMaterialzuschlag != null && wnfMaterialzuschlag.getBigDecimal() != null) {
				nettopreis = nettopreis.add(wnfMaterialzuschlag.getBigDecimal());
			}

			wnfNettopreis.setBigDecimal(nettopreis);

			if (artikellieferantDto.getNNettopreis() != null && artikellieferantDto.getNEinzelpreis() != null) {
				wnfRabattsumme.setBigDecimal(
						artikellieferantDto.getNEinzelpreis().subtract(artikellieferantDto.getNNettopreis()));
			}
		}
		// @todo UW->JE hier muessen auch die hinterlegten Preise angezeigt
		// werden! PJ 5039
	}

	protected PropertyVetoException eventActionVetoableChangeLP() throws Throwable {
		PropertyVetoException pve = super.eventActionVetoableChangeLP();
		DelegateFactory.getInstance().getBestellvorschlagDelegate().removeLockDesBestellvorschlagesWennIchIhnSperre();
		return pve;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		buttonGebinde.setVisible(false);
		// zuerst alles zuruecksetzen, Ausloeser war ev. Button Discard
		setDefaults();

		// Position neu einlesen, ausloeser war ev. Button Refresh oder Discard
		Object key = getKeyWhenDetailPanel();

		if (key != null && !key.equals(LPMain.getLockMeForNew())) {

			buArtikellieferantUebernehmen.setEnabled(true);
			bestellvorschlagDto = DelegateFactory.getInstance().getBestellvorschlagDelegate()
					.bestellvorschlagFindByPrimaryKey((Integer) key);

			if (bestellvorschlagDto.getILieferantId() != null) {
				lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(bestellvorschlagDto.getILieferantId());
			}

			if (bestellvorschlagDto.getIArtikelId() != null) {
				wnfMenge.setBigDecimal(bestellvorschlagDto.getNZubestellendeMenge());
				setArtikelDto(DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(bestellvorschlagDto.getIArtikelId()));
			}
			setEditorButtonColor();
			dto2Components();

			/**
			 * @todo Titel und Statusbar PJ 5040
			 */
		} else {
			buArtikellieferantUebernehmen.setEnabled(false);
			list.setListData(new Object[0]);
		}
	}

	/**
	 * Fuer einen Lieferanten pruefen, ob er fuer einen gewaehlten Artikel
	 * Artikellieferant ist. Wenn er kein Artikellieferant ist, wird er neu
	 * angelegt. Die Preise muessen dabei in der Waehrung des Lieferanten
	 * abgespeichert werden. Wenn der Artikellieferant bereits existiert, muessen
	 * die Preis aktualisiert werden.
	 * 
	 * @throws Throwable Ausnahme
	 */
	private void checkArtikellieferantAndCreateOrUpdate() throws Throwable {
		if (lieferantDto.getIId() != null && getArtikelDto().getIId() != null) {
			// Suchen, ob ein Lieferant schon Artikellieferant ist
			ArtikellieferantDto artikellieferantDto = DelegateFactory.getInstance().getArtikelDelegate()
					.getArtikelEinkaufspreisMitOptionGebinde(getArtikelDto().getIId(), lieferantDto.getIId(),
							BigDecimal.ONE, LPMain.getTheClient().getSMandantenwaehrung(),
							new java.sql.Date(System.currentTimeMillis()), bestellvorschlagDto.getGebindeIId());

			// Wenn der Lieferant noch kein Artikellieferant ist, neu anlegen
			if (artikellieferantDto == null) {
				artikellieferantDto = new ArtikellieferantDto();
				artikellieferantDto.setArtikelIId(getArtikelDto().getIId());
				artikellieferantDto.setLieferantIId(lieferantDto.getIId());
				artikellieferantDto.setMandantCNr(LPMain.getTheClient().getMandant());
				artikellieferantDto.setTPreisgueltigab(wdfLiefertermin.getTimestamp());
				artikellieferantDto.setBRabattbehalten(wnfRabattsumme.getWrbFixNumber().getShort());
				artikellieferantDto.setGebindeIId(bestellvorschlagDto.getGebindeIId());

				if (bestellvorschlagDto.getGebindeIId() != null && bestellvorschlagDto.getNAnzahlgebinde() != null
						&& bestellvorschlagDto.getNAnzahlgebinde().doubleValue() != 0) {

					BigDecimal bdGebindemenge = bestellvorschlagDto.getNZubestellendeMenge().divide(
							bestellvorschlagDto.getNAnzahlgebinde(),
							Defaults.getInstance().getIUINachkommastellenMenge(), BigDecimal.ROUND_HALF_EVEN);

					artikellieferantDto.setNGebindemenge(bdGebindemenge);
				}

				// die Anzeige ist in Mandantenwaehrung
				/**
				 * @todo Nachkommastellen nicht hart codiert
				 */
				artikellieferantDto.setNEinzelpreis(
						wnfEinzelpreis.getBigDecimal().divide(getWechselKursVonMandantNachLieferantWaehrung(),
								Defaults.getInstance().getIUINachkommastellenPreiseEK(), BigDecimal.ROUND_HALF_EVEN));
				artikellieferantDto.setNNettopreis(
						wnfNettopreis.getBigDecimal().divide(getWechselKursVonMandantNachLieferantWaehrung(),
								Defaults.getInstance().getIUINachkommastellenPreiseEK(), BigDecimal.ROUND_HALF_EVEN));
				artikellieferantDto.setFRabatt(getWnfRabattsatz().getDouble());
			} else {
				// update Artikellieferant wegen Preisen, vielleicht gab es
				// aufgrund der Staffelmengen eine Aenderung
				artikellieferantDto.setNEinzelpreis(Helper.rundeKaufmaennisch(
						wnfEinzelpreis.getBigDecimal().multiply(getWechselKursVonMandantNachLieferantWaehrung()),
						Defaults.getInstance().getIUINachkommastellenPreiseEK()));
				artikellieferantDto.setNNettopreis(Helper.rundeKaufmaennisch(
						wnfNettopreis.getBigDecimal().multiply(getWechselKursVonMandantNachLieferantWaehrung()),
						Defaults.getInstance().getIUINachkommastellenPreiseEK()));
				artikellieferantDto.setFRabatt(getWnfRabattsatz().getDouble());

				DelegateFactory.getInstance().getArtikelDelegate().updateArtikellieferant(artikellieferantDto);

				// Staffelpreise nachpflegen
				ArtikellieferantstaffelDto[] artliefstaffelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikellieferantstaffelFindByArtikellieferantIId(artikellieferantDto.getIId());

				for (int i = 0; i < artliefstaffelDto.length; i++) {
					if (artliefstaffelDto[i].getFRabatt() == null) {
						artliefstaffelDto[i].setFRabatt(0D);
					}
					BigDecimal differenz = artikellieferantDto.getNEinzelpreis()
							.multiply(new BigDecimal(artliefstaffelDto[i].getFRabatt().doubleValue()).movePointLeft(2));
					artliefstaffelDto[i].setNNettopreis(artikellieferantDto.getNEinzelpreis().subtract(differenz));
					DelegateFactory.getInstance().getArtikelDelegate()
							.updateArtikellieferantstaffel(artliefstaffelDto[i]);
				}

			}
		}
	}

	private BigDecimal getWechselKursVonMandantNachLieferantWaehrung() throws Throwable {
		BigDecimal bdWechselkurs = null;
		// Die originale lieferantDto ueberschreibt die Modulweit geltende
		// lieferantDto (ghp)
		// LieferantDto lieferantDto = null;

		LieferantDto helperLieferantDto = null;

		// if (bestellvorschlagDto != null
		// && bestellvorschlagDto.getILieferantId() != null) {
		// helperLieferantDto = DelegateFactory
		// .getInstance()
		// .getLieferantDelegate()
		// .lieferantFindByPrimaryKey(
		// bestellvorschlagDto.getILieferantId());
		// } else {
		// helperLieferantDto =
		// DelegateFactory.getInstance().getLieferantDelegate()
		// .lieferantFindByPrimaryKey(lieferantDto.getIId());
		// }

		Integer lieferantId;
		if (bestellvorschlagDto != null && bestellvorschlagDto.getILieferantId() != null) {
			lieferantId = bestellvorschlagDto.getILieferantId();
		} else {
			lieferantId = lieferantDto.getIId();
		}

		helperLieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
				.lieferantFindByPrimaryKey(lieferantId);

		String sMandantenwaehrung = LPMain.getTheClient().getSMandantenwaehrung();
		if (!helperLieferantDto.getWaehrungCNr().equals(sMandantenwaehrung)) {
			bdWechselkurs = DelegateFactory.getInstance().getLocaleDelegate().getWechselkurs2(sMandantenwaehrung,
					helperLieferantDto.getWaehrungCNr());
		} else {
			bdWechselkurs = new BigDecimal(1);
		}

		return bdWechselkurs;
	}

	/*
	 * private Float getWechselKursVonLieferantNachMandantWaehrung() throws
	 * Throwable { Float wechselkurs = null; LieferantDto lieferantDto = null;
	 * 
	 * if (bestellvorschlagDto != null && bestellvorschlagDto.getILieferantId() !=
	 * null) { lieferantDto = getInternalFrame().getLieferantDelegate().
	 * lieferantFindByPrimaryKey(bestellvorschlagDto. getILieferantId()); } else {
	 * lieferantDto = getInternalFrame().getLieferantDelegate().
	 * lieferantFindByPrimaryKey(lieferantDto.getIId()); }
	 * 
	 * MandantDto mandantDto = getInternalFrame().getMandantDelegate().
	 * mandantFindByPrimaryKey (LPMain.getInstance().getTheClient().getMandant());
	 * 
	 * if (!lieferantDto.getWaehrungCNr().equals(mandantDto.getWaehrungCNr())) { //
	 * UW->JE != geht nicht wechselkurs = getInternalFrame().getLocaleDelegate().
	 * getWechselkurs(lieferantDto.getWaehrungCNr(), mandantDto.getWaehrungCNr()); }
	 * else { wechselkurs = new Float(1); }
	 * 
	 * return wechselkurs; }
	 */

	/**
	 * Jedes Mal, wenn die Menge geaendert wird, muss gepreuft werden, ob es
	 * hinterlegte Staffelpreise gibt, wenn ja in Mandantenwaehrung anzeigen.
	 * 
	 * @param e FocusEvent
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	protected void wnfMengeVorschlagFocusLost(FocusEvent e) throws ExceptionLP, Throwable {
		// diese Logik wird in mehreren Panels verwendet!

		// waehrung mitgeben damit in Methode
		// HelperBestellung.setzePreisfelder() die Wahrung in
		// Mandantwaehrung umgerechnet wird
		MandantDto mandantDto = DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant());

		checkMenge = HelperBestellung.checkStaffelpreiseUndMengen(getArtikelDto(), getInternalFrame(), checkMenge,
				wnfMenge, wnfNettopreis, getWnfRabattsatz(), wnfRabattsumme, wnfEinzelpreis, lieferantDto,
				mandantDto.getWaehrungCNr(), true, bestellvorschlagDto.getGebindeIId());

		// Preise muessen durch HelperBestellung in Lieferantenwaehrung
		// angezeigt worden sein!
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false); // LockMeForNew setzen

		setDefaults();
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

		setArtikelEingabefelderEditable(false);
		checkMenge = true;
		buArtikellieferantUebernehmen.setEnabled(true);
	}

	public void eventActionText(ActionEvent e) throws Throwable {
		super.eventActionText(e);
		if (getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			// Editor auf Read Only schalten

		}

		getInternalFrame().showPanelEditor(wefText, this.getAdd2Title(), wefText.getLpEditor().getText(),
				getLockedstateDetailMainKey().getIState());
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, false);

		setArtikelEingabefelderEditable(true);
		buArtikellieferantUebernehmen.setEnabled(false);

		// wenn es noch keine Preise gibt, die Preisfelder mit 0.0 vorbelegen
		if (bestellvorschlagDto.getNNettoeinzelpreis() == null) {
			BigDecimal nDefault = new BigDecimal(0);

			wnfEinzelpreis.setBigDecimal(nDefault);
			getWnfRabattsatz().setBigDecimal(nDefault);
			wnfRabattsumme.setBigDecimal(nDefault);
			wnfNettopreis.setBigDecimal(nDefault);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			// checken ob mindestbestellmenge unterschritten wurde nur wenn
			// checkmenge false
			if (checkMenge) {
				boolean answer = HelperBestellung.checkMindestbestellmenge(getInternalFrame(), getArtikelDto(),
						wnfMenge, wnfNettopreis, getWnfRabattsatz(), wnfRabattsumme, wnfEinzelpreis, lieferantDto);

				if (!answer) {
					return;
				}
			}
			checkMenge = true;
			components2Dto();

			if (bestellvorschlagDto.getIId() == null) {
				Integer bestellvorschlagIId = DelegateFactory.getInstance().getBestellvorschlagDelegate()
						.createBestellvorschlag(bestellvorschlagDto);

				bestellvorschlagDto = DelegateFactory.getInstance().getBestellvorschlagDelegate()
						.bestellvorschlagFindByPrimaryKey(bestellvorschlagIId);

				setKeyWhenDetailPanel(bestellvorschlagIId);
			} else {
				DelegateFactory.getInstance().getBestellvorschlagDelegate().updateBestellvorschlag(bestellvorschlagDto);
			}

			super.eventActionSave(e, true);

			if (wdfLiefertermin.getDate() != null) {

				ParametermandantDto pmGuenstiger = DelegateFactory.getInstance().getParameterDelegate()
						.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_BESTELLUNG,
								ParameterFac.PARAMETER_GUENSTIGER_MELDUNG_ANZEIGEN);
				boolean bGuenstigerMeldung = (Boolean) pmGuenstiger.getCWertAsObject();

				if (bGuenstigerMeldung == true) {

					ArtikellieferantDto artLiefDtoBilliger = DelegateFactory.getInstance().getArtikelDelegate()
							.getGuenstigstenEKPreis(getArtikelDto().getIId(), wnfMenge.getBigDecimal(),
									wdfLiefertermin.getDate(), LPMain.getTheClient().getSMandantenwaehrung(),
									bestellvorschlagDto.getILieferantId());

					BigDecimal nettopreisMitMaterialzuschlag = wnfNettopreis.getBigDecimal();
					if (wnfMaterialzuschlagInfo.getBigDecimal() != null) {
						nettopreisMitMaterialzuschlag = nettopreisMitMaterialzuschlag
								.add(wnfMaterialzuschlagInfo.getBigDecimal());
					}

					if (artLiefDtoBilliger != null && artLiefDtoBilliger.getLief1Preis() != null
							&& artLiefDtoBilliger.getLieferantIId() != null && artLiefDtoBilliger.getLief1Preis()
									.doubleValue() < nettopreisMitMaterialzuschlag.doubleValue()) {

						LieferantDto liefBilliger = DelegateFactory.getInstance().getLieferantDelegate()
								.lieferantFindByPrimaryKey(artLiefDtoBilliger.getLieferantIId());

						StringBuffer sb = new StringBuffer();
						MessageFormat mf = new MessageFormat(LPMain.getTextRespectUISPr("bes.lief.billiger"));
						mf.setLocale(LPMain.getTheClient().getLocUi());
						Object pattern[] = { liefBilliger.getPartnerDto().formatFixTitelName1Name2(),
								Helper.formatZahl(artLiefDtoBilliger.getLief1Preis(),
										Defaults.getInstance().getIUINachkommastellenPreiseEK(),
										LPMain.getTheClient().getLocUi()) + " "
										+ LPMain.getTheClient().getSMandantenwaehrung() };
						sb.append(mf.format(pattern));

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"), sb.toString());
					}
				}
			}
			eventYouAreSelected(true);
		}
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (getInternalFrame() instanceof InternalFrameBestellung)
			((InternalFrameBestellung) getInternalFrame()).getTabbedPaneBestellvorschlag().deleteAuswahl();
		if (getInternalFrame() instanceof InternalFrameAnfrage)
			((InternalFrameAnfrage) getInternalFrame()).getTabbedPaneAnfragevorschlag().deleteAuswahl();
	}

	public BestellvorschlagDto getBestellvorschlagDto() {
		return bestellvorschlagDto;
	}

	private void setEditorButtonColor() {
		getHmOfButtons().get(ACTION_TEXT).getButton()
				.setIcon(bestellvorschlagDto.getXTextinhalt() != null
						&& bestellvorschlagDto.getXTextinhalt().length() > 0 ? IconFactory.getCommentExist()
								: IconFactory.getEditorEdit());
	}

	private void resetEditorButton() {
		getHmOfButtons().get(ACTION_TEXT).getButton().setIcon(IconFactory.getEditorEdit());
	}

}

class WnfMengeVorschlagFocusAdapter extends java.awt.event.FocusAdapter {
	PanelPositionenBestellvorschlag adaptee;

	WnfMengeVorschlagFocusAdapter(PanelPositionenBestellvorschlag adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfMengeVorschlagFocusLost(e);
		} catch (Throwable t) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.preisberechnungfehlgeschlagen"));
		}
	}
}