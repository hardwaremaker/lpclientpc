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
package com.lp.client.artikel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.MultipleImageViewer;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDokumentenablage;
import com.lp.client.frame.component.PanelFilterKriteriumDirekt;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.InternalFrameStueckliste;
import com.lp.editor.LpEditor;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.VorschlagstextDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.jcr.service.JCRRepoInfo;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.GotoHelper;
import com.lp.util.Helper;
import com.lp.util.SiWertParser;
import com.lp.util.siprefixparser.BigDecimalSI;

public class PanelArtikel extends PanelBasis {

	private static final long serialVersionUID = 1L;
	private InternalFrameArtikel internalFrameArtikel = null;
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();
	private JPanel jpaPanelWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private WrapperLabel wlaArtikelnummer = new WrapperLabel();
	private WrapperButton wbuKurzbezeichnung = new WrapperButton();
	private WrapperLabel wlaKurzbezeichnung = new WrapperLabel();
	private WrapperButton wbuBezeichnung = new WrapperButton();
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperButton wbuZusatzbez = new WrapperButton();
	private WrapperLabel wlaZusatzbez = new WrapperLabel();
	private WrapperButton wbuZusatzbez2 = new WrapperButton();
	private WrapperLabel wlaZusatzbez2 = new WrapperLabel();
	private WrapperLabel wlaEinheit = new WrapperLabel();
	private WrapperLabel wlaReferenznummer = new WrapperLabel();
	private WrapperTextField wtfArtikelnummer = new WrapperTextField();
	private WrapperTextField wtfKurzbezeichnung = new WrapperTextField();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperTextField wtfZusatzbez = new WrapperTextField();
	private WrapperTextField wtfZusatzbez2 = new WrapperTextField();
	private WrapperComboBox wcoEinheit = new WrapperComboBox();
	private WrapperTextField wtfReferenznummer = new WrapperTextField();
	private WrapperButton wbuArtikelklasse = new WrapperButton();
	private WrapperButton wbuArtikelgruppe = new WrapperButton();
	private WrapperTextField wtfArtikelklasse = new WrapperTextField();
	private WrapperTextField wtfArtikelgruppe = new WrapperTextField();
	private WrapperButton wbuGeneriereArtikelnummer = new WrapperButton();
	private WrapperTextField wtfHerstellerKuerzel = new WrapperTextField();

	private WrapperButton wbuUrsprungsland = new WrapperButton();
	private WrapperTextField wtfUrsprungsland = new WrapperTextField();

	private WrapperLabel wlaVorzugsteil = new WrapperLabel();
	private WrapperComboBox wcoVorzugsteil = new WrapperComboBox();

	private WrapperButton wbuHersteller = new WrapperButton();
	private WrapperTextField wtfHersteller = new WrapperTextField();
	private WrapperLabel wlaArtikelart = new WrapperLabel();
	private WrapperComboBox wcoArtikelart = new WrapperComboBox();
	private WrapperComboBox wcoMehrwertsteuer = new WrapperComboBox();
	private WrapperLabel wlaMehrwertsteuersatz = new WrapperLabel();

	private WrapperComboBox wcoLocale = new WrapperComboBox();

	private JLabel wlaFreigabe = new JLabel();

	private WrapperCheckBox wcbBestellmengeneinheitInvers = new WrapperCheckBox();
	private WrapperCheckBox wcbBevorzugt = new WrapperCheckBox();

	private WrapperLabel wlaDefaultbezeichnungen = new WrapperLabel();
	Integer defaultMwstsatz = null;
	private MultipleImageViewer pi = new MultipleImageViewer(null);

	private WrapperLabel wlaArtikelBezStd = new WrapperLabel();
	private WrapperTextField wtfArtikelBezStd = new WrapperTextField();
	private WrapperLabel wlaArtikelZBezStd = new WrapperLabel();
	private WrapperLabel wlaKurzbezeichnungStd = new WrapperLabel();
	private WrapperTextField wtfKurzbezeichnungStd = new WrapperTextField();
	private WrapperTextField wtfArtikelZBezStd = new WrapperTextField();
	private WrapperLabel wlaZBez2Std = new WrapperLabel();
	private WrapperTextField wtfArtikelZBez2Std = new WrapperTextField();

	private WrapperSelectField wsfShopgruppe = new WrapperSelectField(WrapperSelectField.SHOPGRUPPE, getInternalFrame(),
			true);

	private WrapperKeyValueField wkvfLagerstand = null;
	private WrapperKeyValueField wkvfReserviert = null;
	private WrapperKeyValueField wkvfFehlmenge = null;
	private WrapperKeyValueField wkvfVerfuegbar = null;

	private WrapperKeyValueField wkvfRahmenreserviert = null;
	private WrapperKeyValueField wkvfInfertigung = null;
	private WrapperKeyValueField wkvfBestellt = null;
	private WrapperKeyValueField wkvfRahmenbestellt = null;
	private WrapperKeyValueField wkvfRahmenbedarf = null;
	private WrapperKeyValueField wkvfPaternoster = null;

	private WrapperKeyValueField wkvfReserviertIntern = null;
	private WrapperKeyValueField wkvfBestelltIntern = null;
	private WrapperKeyValueField wkvfUnterwegs = null;

	protected WrapperGotoButton wbuStkl = null;

	private WrapperLabel wlaEinheitBestellung = new WrapperLabel();
	private WrapperComboBox wcoEinheitBestellung = new WrapperComboBox();
	private WrapperLabel wlaUmrechnungsfaktor = new WrapperLabel();
	private WrapperNumberField wnfUmrechnungsfaktor = new WrapperNumberField();

	private WrapperLabel wlaSperren = new WrapperLabel();

	private WrapperLabel wlaIndex = new WrapperLabel();
	private WrapperTextField wtfIndex = new WrapperTextField();
	private WrapperLabel wlaRevision = new WrapperLabel();
	private WrapperTextField wtfRevision = new WrapperTextField();

	private WrapperSelectField wsfLiefergruppe = new WrapperSelectField(WrapperSelectField.LIEFERGRUPPE,
			getInternalFrame(), true);

	private WrapperButton wbu4VendingId = new WrapperButton();
	private WrapperLabel wla4VendingId = new WrapperLabel();
	private JPanel panel4VendingId = new JPanel();

	private PanelQueryFLR panelQueryFLRHersteller = null;
	private PanelQueryFLR panelQueryFLRArtikelgruppe = null;
	private PanelQueryFLR panelQueryFLRArtikelklasse = null;

	private WrapperLabel wlaSiWert = new WrapperLabel();

	private boolean bPositionskontierung = false;

	JList list = null;
	ArrayList<String> verfuegbarkeitErsatztypen = new ArrayList<String>();

	private WrapperCheckBox wcbVersteckt = new WrapperCheckBox();
	private WrapperCheckBox wcbReineMannzeit = new WrapperCheckBox();
	private WrapperCheckBox wcbNurZurInfo = new WrapperCheckBox();

	private WrapperCheckBox wcbKalkulatorisch = new WrapperCheckBox();

	private boolean bKeinFocusLost = true;

	private boolean bPaternosterVerfuegbar = false;

	private boolean bZentralerArtikelstammUndGetrennteLaeger = false;

	private boolean bMehrsprachigkeit = false;

	static final public String ACTION_SPECIAL_HERSTELLER_FROM_LISTE = "action_hersteller_from_liste";
	static final public String ACTION_SPECIAL_ARTIKELGRUPPE_FROM_LISTE = "action_artikelgruppe_from_liste";
	static final public String ACTION_SPECIAL_ARTIKELKLASSE_FROM_LISTE = "action_artikelklasse_from_liste";
	static final public String ACTION_SPECIAL_GENERIERE_ARTIKELNUMMER = "ACTION_SPECIAL_GENERIERE_ARTIKELNUMMER";

	static final public String ACTION_SPECIAL_VORSCHLAGSTEXT_KBEZ = "ACTION_SPECIAL_VORSCHLAGSTEXT_KBEZ";
	static final public String ACTION_SPECIAL_VORSCHLAGSTEXT_BEZ = "ACTION_SPECIAL_VORSCHLAGSTEXT_BEZ";
	static final public String ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ = "ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ";
	static final public String ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ2 = "ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ2";

	static final public String ACTION_SPECIAL_STKL_NEU_ERZEUGEN = "ACTION_SPECIAL_STKL_NEU_ERZEUGEN";
	static final public String ACTION_SPECIAL_BUTTON_4VENDING_ID = "ACTION_special_BUTTON_4VENDING_ID";
	static final public String BUTTON_4VENDING_ID = ACTION_SPECIAL_BUTTON_4VENDING_ID;

	public final static String MY_OWN_NEW_TOGGLE_FREIGABE = PanelBasis.ACTION_MY_OWN_NEW + "MY_OWN_NEW_FREIGABE";

	static final public String ACTION_SPECIAL_LAND_FROM_LISTE = "ACTION_SPECIAL_LAND_FROM_LISTE";
	private PanelQueryFLR panelQueryFLRLand = null;

	private PanelQueryFLR panelQueryFLRVorschlagstextKBEZ = null;
	private PanelQueryFLR panelQueryFLRVorschlagstextBEZ = null;
	private PanelQueryFLR panelQueryFLRVorschlagstextZBEZ = null;
	private PanelQueryFLR panelQueryFLRVorschlagstextZBEZ2 = null;

	private SiWertParser siwertParser;
	// PJ18691
	private static final ImageIcon DOKUMENTE = HelperClient.createImageIcon("document_attachment_green16x16.png");
	private static final ImageIcon KEINE_DOKUMENTE = HelperClient.createImageIcon("document_attachment16x16.png");
	private JButton jbDokumente;
	public final static String MY_OWN_NEW_DOKUMENTENABLAGE = PanelBasis.LEAVEALONE + "DOKUMENTENABLAGE";
	public final static String MY_OWN_NEW_SPR_LOESCHEN = PanelBasis.ACTION_MY_OWN_NEW + "SPR_LOESCEHN";
	public final static String MY_OWN_NEW_GOTO_SOKO = PanelBasis.LEAVEALONE + "GOTO_SOKO";

	private JScrollPane jspKommentare = null;
	private LpEditor jtpKommentare = new LpEditor(null);

	public PanelArtikel(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;

		siwertParser = new SiWertParser(getSiOhneEinheitenFromParam(), getSiEinheitenFromParam());

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)
				&& LPMain.getInstance().getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER)) {
			bZentralerArtikelstammUndGetrennteLaeger = true;
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MEHRSPRACHIGKEIT)) {
			bMehrsprachigkeit = true;
		}

		jbInit();
		setDefaults();
		initComponents();
	}

	private void dialogQueryHerstellerFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };

		panelQueryFLRHersteller = new PanelQueryFLR(null, null, QueryParameters.UC_ID_ARTIKELHERSTELLER,
				aWhichButtonIUse, internalFrameArtikel, LPMain.getTextRespectUISPr("title.herstellerauswahlliste"));
		panelQueryFLRHersteller.befuellePanelFilterkriterienDirekt(
				ArtikelFilterFactory.getInstance().createFKDHersteller(),
				ArtikelFilterFactory.getInstance().createFKDHerstellerPartner());
		panelQueryFLRHersteller.setSelectedId(internalFrameArtikel.getArtikelDto().getHerstellerIId());
		new DialogQuery(panelQueryFLRHersteller);
	}

	private void dialogQueryArtikelgruppeFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRArtikelgruppe = ArtikelFilterFactory.getInstance().createPanelFLRArtikelgruppe(getInternalFrame(),
				internalFrameArtikel.getArtikelDto().getArtgruIId());
		new DialogQuery(panelQueryFLRArtikelgruppe);
	}

	private void dialogQueryArtikelklasseFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRArtikelklasse = ArtikelFilterFactory.getInstance().createPanelFLRArtikelklasse(getInternalFrame(),
				internalFrameArtikel.getArtikelDto().getArtklaIId());
		new DialogQuery(panelQueryFLRArtikelklasse);
	}

	void dialogQueryLandFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };

		panelQueryFLRLand = new PanelQueryFLR(null, null, QueryParameters.UC_ID_LAND, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr("title.landauswahlliste"));

		panelQueryFLRLand.setSelectedId(internalFrameArtikel.getArtikelDto().getLandIIdUrsprungsland());

		new DialogQuery(panelQueryFLRLand);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
		wcoLocale.setEnabled(false);

		// SPR aus
		sprachabhaengigeBezeichnungAnhandComboBoxSetzen(true);

		// SP7673
		jtpKommentare.setEditable(false);
		jtpKommentare.showToolBar(false);
		jtpKommentare.showStatusBar(false);
		jtpKommentare.showTableItems(false);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_HERSTELLER_FROM_LISTE)) {
			dialogQueryHerstellerFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELGRUPPE_FROM_LISTE)) {
			dialogQueryArtikelgruppeFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LAND_FROM_LISTE)) {
			dialogQueryLandFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELKLASSE_FROM_LISTE)) {
			dialogQueryArtikelklasseFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_GENERIERE_ARTIKELNUMMER)) {
			wtfArtikelnummer.setText(DelegateFactory.getInstance().getArtikelDelegate()
					.generiereNeueArtikelnummer(wtfArtikelnummer.getText()));
		} else if (e.getActionCommand().equals(MY_OWN_NEW_GOTO_SOKO)) {

			FilterKriterium[] fk = new FilterKriterium[] { new FilterKriterium("kundesoko.flrartikel.i_id", false,
					internalFrameArtikel.getArtikelDto().getIId() + "", FilterKriterium.OPERATOR_EQUAL, false) };

			getInternalFrame().geheZu(InternalFrameArtikel.IDX_TABBED_PANE_KUNDENARTIKELNUMMERN,
					TabbedPaneKundenartikelnummern.IDX_PANEL_KUNDENARTIKELNUMMERN, null, null, fk);

			if (internalFrameArtikel.getTabbedPaneKundenartikelnummern() != null) {
				LinkedHashMap<Integer, PanelFilterKriteriumDirekt> hm = internalFrameArtikel
						.getTabbedPaneKundenartikelnummern().preislistennameTop.getHmDirektFilter();
				Iterator<Integer> it = hm.keySet().iterator();
				while (it.hasNext()) {
					PanelFilterKriteriumDirekt fdk = hm.get(it.next());
					if (fdk.fkd.equals(internalFrameArtikel.getTabbedPaneKundenartikelnummern().fkdArtikelnummer))
						fdk.wtfFkdirektValue1.setText(internalFrameArtikel.getArtikelDto().getCNr());
				}
				internalFrameArtikel.getTabbedPaneKundenartikelnummern().preislistennameTop.eventYouAreSelected(false);
			}

		} else if (e.getActionCommand().equals(MY_OWN_NEW_DOKUMENTENABLAGE)) {
			PrintInfoDto values = DelegateFactory.getInstance().getJCRDocDelegate().getPathAndPartnerAndTable(
					internalFrameArtikel.getArtikelDto().getIId(), QueryParameters.UC_ID_ARTIKELLISTE);

			DocPath docPath = values.getDocPath();
			Integer iPartnerIId = values.getiId();
			String sTable = values.getTable();
			if (docPath != null) {
				PanelDokumentenablage panelDokumentenverwaltung = new PanelDokumentenablage(getInternalFrame(),
						internalFrameArtikel.getArtikelDto().getIId().toString(), docPath, sTable,
						internalFrameArtikel.getArtikelDto().getIId().toString(), true, iPartnerIId);
				getInternalFrame().showPanelDialog(panelDokumentenverwaltung);
				getInternalFrame().addItemChangedListener(panelDokumentenverwaltung);
			} else {
				// Show Dialog
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
						LPMain.getTextRespectUISPr("jcr.hinweis.keinpfad"));
			}
		}

		else if (e.getActionCommand().equals(ACTION_SPECIAL_VORSCHLAGSTEXT_BEZ)) {
			panelQueryFLRVorschlagstextBEZ = ArtikelFilterFactory.getInstance()
					.createPanelFLRVorschlagstext(getInternalFrame());
			new DialogQuery(panelQueryFLRVorschlagstextBEZ);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VORSCHLAGSTEXT_KBEZ)) {
			panelQueryFLRVorschlagstextKBEZ = ArtikelFilterFactory.getInstance()
					.createPanelFLRVorschlagstext(getInternalFrame());
			new DialogQuery(panelQueryFLRVorschlagstextKBEZ);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ)) {
			panelQueryFLRVorschlagstextZBEZ = ArtikelFilterFactory.getInstance()
					.createPanelFLRVorschlagstext(getInternalFrame());
			new DialogQuery(panelQueryFLRVorschlagstextZBEZ);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ2)) {
			panelQueryFLRVorschlagstextZBEZ2 = ArtikelFilterFactory.getInstance()
					.createPanelFLRVorschlagstext(getInternalFrame());
			new DialogQuery(panelQueryFLRVorschlagstextZBEZ2);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_STKL_NEU_ERZEUGEN)) {
			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(com.lp.server.benutzer.service.RechteFac.RECHT_STK_STUECKLISTE_CUD)) {

				InternalFrameStueckliste ifStueckliste = (InternalFrameStueckliste) LPMain.getInstance().getDesktop()
						.holeModul(LocaleFac.BELEGART_STUECKLISTE);
				ifStueckliste.getTabbedPaneStueckliste()
						.erstelleStuecklisteAusArtikel(internalFrameArtikel.getArtikelDto().getIId());

			}
		} else if (ACTION_SPECIAL_BUTTON_4VENDING_ID.equals(e.getActionCommand())) {
			action4VendingButtonPressed();
		} else if (MY_OWN_NEW_SPR_LOESCHEN.equals(e.getActionCommand())) {
			DelegateFactory.getInstance().getArtikelDelegate().removeArtikelspr(
					internalFrameArtikel.getArtikelDto().getIId(), (String) wcoLocale.getKeyOfSelectedItem());

			eventYouAreSelected(false);
		}

		if (e.getSource().equals(wcoArtikelart)) {
			if (wcoArtikelart.getKeyOfSelectedItem() != null
					&& wcoArtikelart.getKeyOfSelectedItem().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
				wcbNurZurInfo.setVisible(true);
				wcbReineMannzeit.setVisible(true);
			} else {
				wcbNurZurInfo.setVisible(false);
				wcbReineMannzeit.setVisible(false);
			}
			jpaPanelWorkingOn.validate();
		}

		if (e.getSource().equals(wcoLocale)) {

			sprachabhaengigeBezeichnungAnhandComboBoxSetzen(false);

		}

		if (e.getActionCommand().equals(MY_OWN_NEW_TOGGLE_FREIGABE)) {
			// PJ 17558
			if (internalFrameArtikel.getArtikelDto().getIId() != null) {

				String grundRuecknahme = null;
				if (internalFrameArtikel.getArtikelDto().getTFreigabe() != null) {

					String s = (String) JOptionPane.showInputDialog(internalFrameArtikel,
							LPMain.getTextRespectUISPr("ww.freigeben.ruecknahmegrund"),
							LPMain.getTextRespectUISPr("lp.frage"), JOptionPane.PLAIN_MESSAGE);

					// If a string was returned, say so.
					if ((s != null) && (s.length() > 0)) {

						if (s.length() > 299) {
							s = s.substring(0, 299);
						}
						grundRuecknahme = s;
					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("ww.freigeben.ruecknahmegrund.error"));

						return;
					}

					Integer stuecklisteIId = DelegateFactory.getInstance().getStuecklisteDelegate()
							.wirdArtikelInFreigegebenerStuecklisteVerwendet(
									internalFrameArtikel.getArtikelDto().getIId(), false);
					if (stuecklisteIId != null) {

						if (DelegateFactory.getInstance().getTheJudgeDelegate()
								.hatRecht(RechteFac.RECHT_STK_FREIGABE_CUD)) {
							StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
									.stuecklisteFindByPrimaryKey(stuecklisteIId);

							boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
									LPMain.getMessageTextRespectUISPr("stk.freigabe.zuruecknehmen.wirdverwendet",
											"(" + stklDto.getArtikelDto().formatArtikelbezeichnung() + ")"));
							if (b == false) {
								return;
							}else {
								DelegateFactory.getInstance().getStuecklisteDelegate()
								.wirdArtikelInFreigegebenerStuecklisteVerwendet(
										internalFrameArtikel.getArtikelDto().getIId(), true);
							}
							
							
							
						} else {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("ww.freigabe.zuruecknehmen.stkl.keinrecht"));
							return;
						}

					}

				}

				DelegateFactory.getInstance().getArtikelDelegate()
						.toggleFreigabe(internalFrameArtikel.getArtikelDto().getIId(), grundRuecknahme);
				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(internalFrameArtikel.getArtikelDto().getIId());
				internalFrameArtikel.setArtikelDto(artikelDto);

				if (artikelDto.getTFreigabe() == null) {
					getInternalFrame().setRechtModulweit(RechteFac.RECHT_MODULWEIT_UPDATE);
				}

				eventYouAreSelected(false);
			}
		}

	}

	private void sprachabhaengigeBezeichnungAnhandComboBoxSetzen(
			boolean bAnhandMandantenlocaleWennNullAusComboboxLocale) throws ExceptionLP, Throwable {
		if (internalFrameArtikel.getArtikelDto() != null && internalFrameArtikel.getArtikelDto().getIId() != null) {

			ArtikelsprDto artikelsprDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelsprFindByArtikelIIdLocaleCNrOhneExc(internalFrameArtikel.getArtikelDto().getIId(),
							(String) wcoLocale.getKeyOfSelectedItem());

			if (artikelsprDto == null && bAnhandMandantenlocaleWennNullAusComboboxLocale) {
				artikelsprDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelsprFindByArtikelIIdLocaleCNrOhneExc(internalFrameArtikel.getArtikelDto().getIId(),
								LPMain.getTheClient().getLocMandantAsString());
			}

			if (artikelsprDto != null) {
				wtfBezeichnung.setText(artikelsprDto.getCBez());
				wtfKurzbezeichnung.setText(artikelsprDto.getCKbez());
				wtfZusatzbez.setText(artikelsprDto.getCZbez());
				wtfZusatzbez2.setText(artikelsprDto.getCZbez2());

			} else {
				wtfBezeichnung.setText(null);
				wtfKurzbezeichnung.setText(null);
				wtfZusatzbez.setText(null);
				wtfZusatzbez2.setText(null);
			}
			if (getHmOfButtons().containsKey(MY_OWN_NEW_SPR_LOESCHEN)) {

				LPButtonAction lpba = getHmOfButtons().get(MY_OWN_NEW_SPR_LOESCHEN);

				if (wcoLocale.getKeyOfSelectedItem() != null
						&& wcoLocale.getKeyOfSelectedItem().equals(LPMain.getTheClient().getLocMandantAsString())) {

					lpba.getButton().setEnabled(false);
				} else {
					lpba.getButton().setEnabled(true);
				}
			}

		}
	}

	private void action4VendingButtonPressed() throws ExceptionLP, Throwable {
		boolean has4VendingId = wla4VendingId.getText() != null && !wla4VendingId.getText().isEmpty();

		if (has4VendingId) {
			boolean delete = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					LPMain.getTextRespectUISPr("artikel.vendidata.dialog.wirklichloeschen"));
			if (delete) {
				DelegateFactory.getInstance().getArtikelDelegate()
						.delete4VendingId(internalFrameArtikel.getArtikelDto().getIId());
				init4VendingComponents(null);
			}
		} else {
			Integer new4VendingId = DelegateFactory.getInstance().getArtikelDelegate()
					.generiere4VendingId(internalFrameArtikel.getArtikelDto().getIId());
			init4VendingComponents(new4VendingId != null ? new4VendingId.toString() : null);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRHersteller) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				HerstellerDto herstellerDto = DelegateFactory.getInstance().getArtikelDelegate()
						.herstellerFindBdPrimaryKey((Integer) key);
				wtfHersteller.setText(herstellerDto.getBezeichnung());
				wtfHerstellerKuerzel.setText(herstellerDto.getCNr());
				internalFrameArtikel.getArtikelDto().setHerstellerIId(herstellerDto.getIId());
			} else if (e.getSource() == panelQueryFLRArtikelklasse) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtklaDto artklaDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artklaFindByPrimaryKey((Integer) key);
				wtfArtikelklasse.setText(artklaDto.getBezeichnung());
				internalFrameArtikel.getArtikelDto().setArtklaIId(artklaDto.getIId());

			} else if (e.getSource() == panelQueryFLRArtikelgruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtgruDto artgruDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artgruFindByPrimaryKey((Integer) key);
				wtfArtikelgruppe.setText(artgruDto.getBezeichnung());
				internalFrameArtikel.getArtikelDto().setArtgruIId(artgruDto.getIId());
			} else if (e.getSource() == panelQueryFLRLand) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LandDto landDto = DelegateFactory.getInstance().getSystemDelegate().landFindByPrimaryKey((Integer) key);
				wtfUrsprungsland.setText(landDto.getCLkz());
				internalFrameArtikel.getArtikelDto().setLandIIdUrsprungsland(landDto.getIID());
			} else if (e.getSource() == panelQueryFLRVorschlagstextBEZ) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				VorschlagstextDto vorschlagstextDto = DelegateFactory.getInstance().getArtikelDelegate()
						.vorschlagstextFindByPrimaryKey((Integer) key);
				wtfBezeichnung.setText(vorschlagstextDto.getCBez());
			} else if (e.getSource() == panelQueryFLRVorschlagstextKBEZ) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				VorschlagstextDto vorschlagstextDto = DelegateFactory.getInstance().getArtikelDelegate()
						.vorschlagstextFindByPrimaryKey((Integer) key);
				wtfKurzbezeichnung.setText(vorschlagstextDto.getCBez());
			} else if (e.getSource() == panelQueryFLRVorschlagstextZBEZ) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				VorschlagstextDto vorschlagstextDto = DelegateFactory.getInstance().getArtikelDelegate()
						.vorschlagstextFindByPrimaryKey((Integer) key);
				wtfZusatzbez.setText(vorschlagstextDto.getCBez());
			} else if (e.getSource() == panelQueryFLRVorschlagstextZBEZ2) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				VorschlagstextDto vorschlagstextDto = DelegateFactory.getInstance().getArtikelDelegate()
						.vorschlagstextFindByPrimaryKey((Integer) key);
				wtfZusatzbez2.setText(vorschlagstextDto.getCBez());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRArtikelgruppe) {

				wtfArtikelgruppe.setText(null);

				internalFrameArtikel.getArtikelDto().setArtgruIId(null);
				internalFrameArtikel.getArtikelDto().setArtgruDto(null);
			}
			if (e.getSource() == panelQueryFLRArtikelklasse) {

				wtfArtikelklasse.setText(null);

				internalFrameArtikel.getArtikelDto().setArtklaDto(null);
				internalFrameArtikel.getArtikelDto().setArtklaIId(null);
			}
			if (e.getSource() == panelQueryFLRHersteller) {

				wtfHersteller.setText(null);
				internalFrameArtikel.getArtikelDto().setHerstellerIId(null);
				wtfHerstellerKuerzel.setText(null);
			}
			if (e.getSource() == panelQueryFLRLand) {
				wtfUrsprungsland.setText(null);
				internalFrameArtikel.getArtikelDto().setLandIIdUrsprungsland(null);
			}
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		Object key = null;

		if (internalFrameArtikel.getArtikelDto() != null) {
			key = internalFrameArtikel.getArtikelDto().getIId();
		}
		wsfShopgruppe.setAusgeschlosseneIds(null);
		wlaSiWert.setText("");
		jtpKommentare.setText("");
		if (key != null) {
			wbuStkl.setVisible(true);
			internalFrameArtikel.setArtikelDto(DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(internalFrameArtikel.getArtikelDto().getIId()));
			wsfShopgruppe.setAusgeschlosseneIds(DelegateFactory.getInstance().getArtikelDelegate()
					.getBereitsVerwendeteShopgruppen(internalFrameArtikel.getArtikelDto().getIId()));
			if (bPositionskontierung == true) {
				if (defaultMwstsatz == null) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("artikel.error.positionkoniertungkeindefaultmwstsatz"));
					LPMain.getInstance().getDesktop().closeFrame(getInternalFrame());
				} else {

					if (internalFrameArtikel.getArtikelDto().getMwstsatzbezIId() == null) {

						internalFrameArtikel.getArtikelDto().setMwstsatzbezIId(defaultMwstsatz);

						DelegateFactory.getInstance().getArtikelDelegate()
								.updateArtikel(internalFrameArtikel.getArtikelDto());
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
								LPMain.getTextRespectUISPr("artikel.error.positionkoniertungmwstsatzupgedated"));
					}

				}
			}

			dto2Components();
			String sBezeichnung = "";
			if (internalFrameArtikel.getArtikelDto().getArtikelsprDto() != null) {
				sBezeichnung = internalFrameArtikel.getArtikelDto().getArtikelsprDto().getCBez();
			}
			if (sBezeichnung == null) {
				sBezeichnung = "";
			}

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					internalFrameArtikel.getArtikelDto().getCNr() + ", " + sBezeichnung);
			wcoLocale.setEnabled(true);

			LPButtonAction toggleFreigabe = getHmOfButtons().get(MY_OWN_NEW_TOGGLE_FREIGABE);
			// Wenn vorhanden
			if (toggleFreigabe != null) {
				
				//SP9836
				if(super.getLockedstateDetailMainKey().getIState() != LOCK_IS_LOCKED_BY_ME) {
					toggleFreigabe.getButton().setEnabled(true);
				}
				
				
			}

		} else {
			list.setListData(new Object[0]);

			pi.setImage(null);

			leereAlleFelder(this);
			clearStatusbar();

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");

			// Default MWST-Satz setzen
			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_DEFAULT_ARTIKEL_MWSTSATZ,
							ParameterFac.KATEGORIE_ARTIKEL, LPMain.getTheClient().getMandant());

			if (defaultMwstsatz != null) {
				defaultMwstsatz = (Integer) parameter.getCWertAsObject();
				wcoMehrwertsteuer.setKeyOfSelectedItem(defaultMwstsatz);
			}
			wcoLocale.setKeyOfSelectedItem(LPMain.getTheClient().getLocUiAsString());
			wcoLocale.setEnabled(false);

		}

		jtpKommentare.setEditable(false);
		jtpKommentare.showToolBar(false);
		jtpKommentare.showStatusBar(false);
		jtpKommentare.showTableItems(false);

	}

	private void jbInit() throws Throwable {

		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		wlaArtikelnummer.setText(LPMain.getTextRespectUISPr("artikel.artikelnummer"));
		wtfArtikelnummer.setToken("artikelnummer");

		wbuKurzbezeichnung.setText(LPMain.getTextRespectUISPr("artikel.kurzbez"));
		wtfKurzbezeichnung.setToken("kurzbez");
		wbuKurzbezeichnung.setActionCommand(ACTION_SPECIAL_VORSCHLAGSTEXT_KBEZ);
		wbuKurzbezeichnung.addActionListener(this);

		wlaKurzbezeichnung.setText(LPMain.getTextRespectUISPr("artikel.kurzbez"));

		wbuBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wbuBezeichnung.setActionCommand(ACTION_SPECIAL_VORSCHLAGSTEXT_BEZ);
		wbuBezeichnung.addActionListener(this);

		wlaBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wtfBezeichnung.setToken("bezeichnung");

		wbuZusatzbez.setText(LPMain.getTextRespectUISPr("lp.zusatzbezeichnung"));
		wbuZusatzbez.setActionCommand(ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ);
		wbuZusatzbez.addActionListener(this);

		wlaZusatzbez.setText(LPMain.getTextRespectUISPr("lp.zusatzbezeichnung"));
		wtfZusatzbez.setToken("zusatzbezeichnung");

		wbuZusatzbez2.setText(LPMain.getTextRespectUISPr("lp.zusatzbezeichnung2"));

		wbuZusatzbez2.setActionCommand(ACTION_SPECIAL_VORSCHLAGSTEXT_ZBEZ2);
		wbuZusatzbez2.addActionListener(this);

		wlaZusatzbez2.setText(LPMain.getTextRespectUISPr("lp.zusatzbezeichnung2"));
		wtfZusatzbez2.setToken("zusatzbezeichnung2");

		wlaEinheit.setText(LPMain.getTextRespectUISPr("lp.einheit"));
		wcoEinheit.setToken("einheit");
		wlaEinheitBestellung.setText(LPMain.getTextRespectUISPr("artikel.bestelleinheit"));
		wcoEinheitBestellung.setToken("bestelleinheit");
		wlaUmrechnungsfaktor.setText(LPMain.getTextRespectUISPr("artikel.umrechnungsfaktor"));
		wnfUmrechnungsfaktor.setToken("umrechnungsfaktor");

		wlaReferenznummer.setText(LPMain.getTextRespectUISPr("lp.referenznummer"));
		wtfReferenznummer.setToken("referenznummer");

		wcbVersteckt.setText(LPMain.getTextRespectUISPr("lp.versteckt"));
		wcbVersteckt.setToken("versteckt");

		wcbKalkulatorisch.setText(LPMain.getTextRespectUISPr("artikel.kalkulatorisch"));
		wcbKalkulatorisch.setToken("kalkulatorisch");

		wcbNurZurInfo.setText(LPMain.getTextRespectUISPr("artikel.nurzurinfo"));
		wcbNurZurInfo.setToken("nurzurinfo");

		wbuUrsprungsland.setText(LPMain.getTextRespectUISPr("artikel.ursprungsland") + "...");
		wbuUrsprungsland.setActionCommand(ACTION_SPECIAL_LAND_FROM_LISTE);
		wbuUrsprungsland.addActionListener(this);
		wtfUrsprungsland.setActivatable(false);

		wcbReineMannzeit.setText(LPMain.getTextRespectUISPr("artikel.reinemannzeit"));
		wcbReineMannzeit.setToken("reinemannzeit");

		wtfArtikelnummer.setColumnsMax(ArtikelFac.MAX_ARTIKEL_ARTIKELNUMMER);
		wnfUmrechnungsfaktor.setFractionDigits(6);
		wcoEinheitBestellung.addActionListener(new PanelArtikel_wcoEinheitBestellung_actionAdapter(this));
		Map<?, ?> m = DelegateFactory.getInstance().getLocaleDelegate().getAllLocales(LPMain.getTheClient().getLocUi());
		wcoLocale.setMandatoryField(true);
		wcoLocale.setMap(m);

		wcoLocale.addActionListener(this);
		wcoLocale.setKeyOfSelectedItem(LPMain.getTheClient().getLocUiAsString());

		// Default MWST-Satz setzen
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_DEFAULT_ARTIKEL_MWSTSATZ, ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && parameter.getCWert().length() > 0) {
			defaultMwstsatz = (Integer) parameter.getCWertAsObject();
		}

		wtfHerstellerKuerzel.setActivatable(false);

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER, ParameterFac.KATEGORIE_ARTIKEL,
				LPMain.getTheClient().getMandant());

		if (parameter.getCWertAsObject() != null) {
			wtfArtikelnummer.setColumnsMax(((Integer) parameter.getCWertAsObject()).intValue());
		}

		wtfArtikelnummer.setMandatoryField(true);
		wtfArtikelnummer.setUppercaseField(true);
		wtfArtikelnummer.addFocusListener(new PanelArtikel_wtfArtikelnummer_focusAdapter(this));
		wtfKurzbezeichnung.setColumnsMax(ArtikelFac.MAX_ARTIKEL_KURZBEZEICHNUNG);

		int iLaengenBezeichung = DelegateFactory.getInstance().getArtikelDelegate().getLaengeArtikelBezeichnungen();

		wtfBezeichnung.setColumnsMax(iLaengenBezeichung);
		wtfZusatzbez.setColumnsMax(iLaengenBezeichung);
		wtfZusatzbez2.setColumnsMax(iLaengenBezeichung);
		wtfReferenznummer.setColumnsMax(ArtikelFac.MAX_ARTIKEL_REFERENZNUMMER);

		wtfArtikelBezStd.setColumnsMax(iLaengenBezeichung);
		wtfArtikelZBez2Std.setColumnsMax(iLaengenBezeichung);
		wtfArtikelZBezStd.setColumnsMax(iLaengenBezeichung);

		installSiKeyListeners();

		wbuArtikelklasse.setText(LPMain.getTextRespectUISPr("lp.artikelklasse") + "...");
		wbuArtikelklasse.setActionCommand(PanelArtikel.ACTION_SPECIAL_ARTIKELKLASSE_FROM_LISTE);
		wbuArtikelklasse.addActionListener(this);
		wbuArtikelklasse.setToken("artikelklasse");
		wbuArtikelgruppe.setText(LPMain.getTextRespectUISPr("lp.artikelgruppe") + "...");
		wbuArtikelgruppe.setActionCommand(PanelArtikel.ACTION_SPECIAL_ARTIKELGRUPPE_FROM_LISTE);
		wbuArtikelgruppe.addActionListener(this);
		wbuArtikelgruppe.setToken("artikelgruppe");

		wtfArtikelklasse.setActivatable(false);
		wbuHersteller.setText(LPMain.getTextRespectUISPr("lp.hersteller") + "...");
		wbuHersteller.setActionCommand(PanelArtikel.ACTION_SPECIAL_HERSTELLER_FROM_LISTE);
		wbuHersteller.addActionListener(this);
		wbuHersteller.setToken("hersteller");
		wtfHersteller.setText("");
		wtfHersteller.setActivatable(false);
		wlaArtikelart.setText(LPMain.getTextRespectUISPr("lp.artikelart"));

		wlaIndex.setText(LPMain.getTextRespectUISPr("artikel.index"));
		wtfIndex.setColumnsMax(15);
		wtfIndex.setToken("index");
		wlaRevision.setText(LPMain.getTextRespectUISPr("artikel.revision"));
		wtfRevision.setColumnsMax(ArtikelFac.MAX_ARTIKEL_REVISION);
		wtfRevision.setToken("revision");

		wlaMehrwertsteuersatz.setText(LPMain.getTextRespectUISPr("lp.mwst"));
		wcoMehrwertsteuer.setToken("mwst");
		wlaDefaultbezeichnungen.setText(LPMain.getTextRespectUISPr("artikel.defaultbezeichnungen") + ":");
		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_ARTIKELGRUPPE_IST_PFLICHTFELD, ParameterFac.KATEGORIE_ARTIKEL,
				LPMain.getTheClient().getMandant());
		wtfArtikelgruppe.setMandatoryField((Boolean) parameter.getCWertAsObject());
		wtfArtikelgruppe.setActivatable(false);
		
		
		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_ARTIKELKLASSE_IST_PFLICHTFELD, ParameterFac.KATEGORIE_ARTIKEL,
				LPMain.getTheClient().getMandant());
		wtfArtikelklasse.setMandatoryField((Boolean) parameter.getCWertAsObject());
		

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG, ParameterFac.KATEGORIE_KUNDEN,
				LPMain.getTheClient().getMandant());
		bPositionskontierung = ((Boolean) parameter.getCWertAsObject());
		wcoMehrwertsteuer.setMandatoryField(bPositionskontierung);

		bPaternosterVerfuegbar = DelegateFactory.getInstance().getAutoPaternosterDelegate().isPaternosterVerfuegbar();

		wcoArtikelart.setMandatoryField(true);
		wcoArtikelart.addActionListener(this);
		wcoArtikelart.setToken("artikelart");
		wcoEinheit.setMandatoryField(true);
		wbuGeneriereArtikelnummer.setText("G");
		wbuGeneriereArtikelnummer.setToolTipText(LPMain.getTextRespectUISPr("artikel.generiereartikelnummer"));
		wbuGeneriereArtikelnummer.setActionCommand(PanelArtikel.ACTION_SPECIAL_GENERIERE_ARTIKELNUMMER);
		wbuGeneriereArtikelnummer.addActionListener(this);
		wbuGeneriereArtikelnummer.setToken("generiereartikelnummer");

		wlaArtikelBezStd.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wtfArtikelBezStd.setText("");
		wtfArtikelBezStd.setActivatable(false);
		wlaArtikelZBezStd.setText(LPMain.getTextRespectUISPr("lp.zusatzbezeichnung"));
		wlaKurzbezeichnungStd.setText(LPMain.getTextRespectUISPr("label.kurzbezeichnung"));
		wtfKurzbezeichnungStd.setText("");
		wtfKurzbezeichnungStd.setActivatable(false);
		wtfArtikelZBezStd.setText("");
		wtfArtikelZBezStd.setActivatable(false);
		wlaZBez2Std.setRequestFocusEnabled(true);
		wlaZBez2Std.setText(LPMain.getTextRespectUISPr("lp.zusatzbez2"));
		wtfArtikelZBez2Std.setText("");
		wtfArtikelZBez2Std.setActivatable(false);
		wlaSperren.setText("");
		wlaSperren.setForeground(Color.RED);

		final int iBreite = Defaults.getInstance().bySizeFactor(120);
		wkvfLagerstand = new WrapperKeyValueField(iBreite);
		wkvfLagerstand.setKey(LPMain.getTextRespectUISPr("lp.lagerstand") + ": ");
		wkvfLagerstand.setToken("lagerstand");

		wcbBestellmengeneinheitInvers.setText(LPMain.getTextRespectUISPr("artikel.bestellmengeneinheit.invers"));

		wkvfReserviert = new WrapperKeyValueField(iBreite);
		wkvfReserviert.setKey("- " + LPMain.getTextRespectUISPr("lp.reserviert") + ": ");
		wkvfReserviert.setToken("reseviert");

		wkvfReserviertIntern = new WrapperKeyValueField(iBreite);
		wkvfReserviertIntern.setKey(LPMain.getTextRespectUISPr("lp.reserviert.intern") + ": ");
		wkvfReserviertIntern.getWlaKey().setForeground(Color.GRAY);

		wkvfFehlmenge = new WrapperKeyValueField(iBreite);
		wkvfFehlmenge.setKey("- " + LPMain.getTextRespectUISPr("label.fehlmenge") + ": ");
		wkvfFehlmenge.setToken("fehlmenge");

		wkvfVerfuegbar = new WrapperKeyValueField(iBreite);
		wkvfVerfuegbar.setKey("= " + LPMain.getTextRespectUISPr("lp.verfuegbar") + ": ");
		wkvfVerfuegbar.setToken("verfuegbar");

		wkvfInfertigung = new WrapperKeyValueField(iBreite);
		wkvfInfertigung.setKey(LPMain.getTextRespectUISPr("lp.infertigung") + ": ");
		wkvfInfertigung.setToken("infertigung");

		wkvfRahmenreserviert = new WrapperKeyValueField(iBreite);
		wkvfRahmenreserviert.setKey(LPMain.getTextRespectUISPr("lp.rahmenreserviert") + ": ");
		wkvfRahmenreserviert.setToken("rahmenreserviert");

		wkvfRahmenbestellt = new WrapperKeyValueField(iBreite);
		wkvfRahmenbestellt.setKey(LPMain.getTextRespectUISPr("lp.rahmenbestellt") + ": ");
		wkvfRahmenbestellt.setToken("rahmenbestellt");

		wkvfBestellt = new WrapperKeyValueField(iBreite);
		wkvfBestellt.setKey(LPMain.getTextRespectUISPr("lp.bestellt") + ": ");
		wkvfBestellt.setToken("bestellt");

		wkvfBestelltIntern = new WrapperKeyValueField(iBreite);
		wkvfBestelltIntern.setKey(LPMain.getTextRespectUISPr("lp.bestellt.intern") + ": ");
		wkvfBestelltIntern.getWlaKey().setForeground(Color.GRAY);

		wkvfUnterwegs = new WrapperKeyValueField(iBreite);
		wkvfUnterwegs.setKey(LPMain.getTextRespectUISPr("lp.bestellt.unterwegs") + ": ");
		wkvfUnterwegs.getWlaKey().setForeground(Color.GRAY);

		wlaVorzugsteil.setText(LPMain.getTextRespectUISPr("artikel.vorzug"));

		wcbBevorzugt.setToolTipText(LPMain.getTextRespectUISPr("artikel.bevorzugt"));

		wkvfRahmenbedarf = new WrapperKeyValueField(iBreite);
		wkvfRahmenbedarf.setKey(LPMain.getTextRespectUISPr("artikel.rahmenbedarf") + ": ");
		wkvfRahmenbedarf.setToken("rahmenbedarf");

		wkvfPaternoster = new WrapperKeyValueField(iBreite);
		wkvfPaternoster.setKey(LPMain.getTextRespectUISPr("artikel.paternoster") + ": ");
		wkvfPaternoster.setToken("paternoster");

		wlaSperren.setHorizontalAlignment(SwingConstants.LEFT);

		if (LPMain.getInstance().getDesktopController()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {
			wbuStkl = new WrapperGotoButton(GotoHelper.GOTO_STUECKLISTE_ANDERER_MANDANT_AUSWAHL);
		} else {
			wbuStkl = new WrapperGotoButton(GotoHelper.GOTO_STUECKLISTE_DETAIL);
		}

		wbuStkl.setActionCommand(ACTION_SPECIAL_STKL_NEU_ERZEUGEN);
		wbuStkl.addActionListener(this);
		wbuStkl.setActivatable(false);
		wbuStkl.setText(LPMain.getTextRespectUISPr("artikel.detail.stkl"));

		jpaPanelWorkingOn = new JPanel(new MigLayout("wrap 7",
				"[22.5%,fill][15%,fill][10%,fill][8.75%,fill][10%,fill][17.5%,fill][17.5%,fill]"));
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		iZeile = 0;

		jpaPanelWorkingOn.add(wlaArtikelnummer, "growx 75, split 2");
		jpaPanelWorkingOn.add(wbuGeneriereArtikelnummer, "growx 25");

		// jpaPanelWorkingOn.add(wlaArtikelnummer,
		// new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
		// GridBagConstraints.CENTER,
		// GridBagConstraints.HORIZONTAL,
		// new Insets(10, 2, 2, 52), 100, 0));
		// jpaPanelWorkingOn.add(wbuGeneriereArtikelnummer,
		// new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
		// GridBagConstraints.EAST, GridBagConstraints.NONE,
		// new Insets(10, 0, 0, 0), 40, 0));

		jpaPanelWorkingOn.add(wtfArtikelnummer);

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG)) {
			jpaPanelWorkingOn.add(wtfHerstellerKuerzel);
		} else {
			jpaPanelWorkingOn.add(new WrapperLabel());
		}

		if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_STUECKLISTE)) {
			jpaPanelWorkingOn.add(wbuStkl);
		} else {
			jpaPanelWorkingOn.add(new WrapperLabel());
		}

		jpaPanelWorkingOn.add(wbuHersteller);
		jpaPanelWorkingOn.add(wtfHersteller, "span");

		// PJ 16665

		boolean bVorschlagstexteVorhanden = DelegateFactory.getInstance().getArtikelDelegate()
				.sindVorschlagstexteVorhanden();

		jpaPanelWorkingOn.add(bVorschlagstexteVorhanden ? wbuKurzbezeichnung : wlaKurzbezeichnung);
		jpaPanelWorkingOn.add(wtfKurzbezeichnung);
		jpaPanelWorkingOn.add(wlaRevision);
		jpaPanelWorkingOn.add(wtfRevision, "span 2");
		jpaPanelWorkingOn.add(wlaArtikelart);
		jpaPanelWorkingOn.add(wcoArtikelart, "wrap");

		jpaPanelWorkingOn.add(bVorschlagstexteVorhanden ? wbuBezeichnung : wlaBezeichnung);
		jpaPanelWorkingOn.add(wtfBezeichnung, "span 4");
		if (hasZusatzFunktionSiWert()) {
			wlaSiWert.setHorizontalAlignment(SwingConstants.LEFT);
			jpaPanelWorkingOn.add(wlaSiWert);
		} else {
			jpaPanelWorkingOn.add(new WrapperLabel());
		}

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(com.lp.server.benutzer.service.RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
			jpaPanelWorkingOn.add(wcbVersteckt, "wrap");
		} else {
			jpaPanelWorkingOn.add(new WrapperLabel(), "wrap");
		}

		jpaPanelWorkingOn.add(bVorschlagstexteVorhanden ? wbuZusatzbez : wlaZusatzbez);
		jpaPanelWorkingOn.add(wtfZusatzbez, "span 4");
		jpaPanelWorkingOn.add(wcbReineMannzeit);
		jpaPanelWorkingOn.add(wlaSperren, "wrap");

		jpaPanelWorkingOn.add(bVorschlagstexteVorhanden ? wbuZusatzbez2 : wlaZusatzbez2);
		jpaPanelWorkingOn.add(wtfZusatzbez2, "span 4");
		jpaPanelWorkingOn.add(wcbNurZurInfo);
		jpaPanelWorkingOn.add(wcbKalkulatorisch, "wrap");

		jpaPanelWorkingOn.add(wlaEinheit);
		jpaPanelWorkingOn.add(wcoEinheit);
		jpaPanelWorkingOn.add(new WrapperLabel("="));
		jpaPanelWorkingOn.add(wnfUmrechnungsfaktor, "span 2");
		jpaPanelWorkingOn.add(wcoEinheitBestellung);
		jpaPanelWorkingOn.add(wcbBestellmengeneinheitInvers, "wrap");

		jpaPanelWorkingOn.add(wlaReferenznummer);
		jpaPanelWorkingOn.add(wtfReferenznummer);
		jpaPanelWorkingOn.add(wlaIndex);
		jpaPanelWorkingOn.add(wtfIndex, "span 2");
		jpaPanelWorkingOn.add(wlaMehrwertsteuersatz);
		jpaPanelWorkingOn.add(wcoMehrwertsteuer, "wrap");

		jpaPanelWorkingOn.add(wbuArtikelklasse);
		jpaPanelWorkingOn.add(wtfArtikelklasse, "span 4");
		jpaPanelWorkingOn.add(wsfLiefergruppe.getWrapperButton());
		jpaPanelWorkingOn.add(wsfLiefergruppe.getWrapperTextField(), "wrap");

		jpaPanelWorkingOn.add(wbuArtikelgruppe);
		jpaPanelWorkingOn.add(wtfArtikelgruppe, "span 4");

		jpaPanelWorkingOn.add(wsfShopgruppe.getWrapperButton());
		jpaPanelWorkingOn.add(wsfShopgruppe.getWrapperTextField(), "wrap");

		// PJ19598
		if (internalFrameArtikel.getTabbedPaneArtikel().isUrsprungslandIstPflichtfeld()) {

			wtfUrsprungsland.setMandatoryField(true);
			jpaPanelWorkingOn.add(wbuUrsprungsland);
			jpaPanelWorkingOn.add(wtfUrsprungsland, "span 4");

		} else {
			jpaPanelWorkingOn.add(new WrapperLabel(""), "span 5");
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ERSATZTYPENVERWALTUNG)) {
			jpaPanelWorkingOn.add(wcbBevorzugt, "split 2,  w 20");
		}

		jpaPanelWorkingOn.add(wlaVorzugsteil, " grow");
		jpaPanelWorkingOn.add(wcoVorzugsteil, "wrap");

		if (!LPMain.getTheClient().getLocKonzern().equals(LPMain.getTheClient().getLocUi())) {

			jpaPanelWorkingOn.add(wlaDefaultbezeichnungen, "wrap");
			jpaPanelWorkingOn.add(wlaKurzbezeichnungStd);
			jpaPanelWorkingOn.add(wtfKurzbezeichnungStd, "span 4, wrap");
			jpaPanelWorkingOn.add(wlaArtikelBezStd);
			jpaPanelWorkingOn.add(wtfArtikelBezStd, "span 4, wrap");
			jpaPanelWorkingOn.add(wlaArtikelZBezStd);
			jpaPanelWorkingOn.add(wtfArtikelZBezStd, "span 4, wrap");
			jpaPanelWorkingOn.add(wlaZBez2Std, "growx");
			jpaPanelWorkingOn.add(wtfArtikelZBez2Std, "span 4, wrap");
		} else {
			if (bPaternosterVerfuegbar) {
				jpaPanelWorkingOn.add(wkvfPaternoster, "gapright 14px, span 3, split 2");
				jpaPanelWorkingOn.add(new JLabel());
			}
			// default-Bild:

			jpaPanelWorkingOn.add(pi,
					(bPaternosterVerfuegbar ? "" : "skip 3,") + "w min:0, height 0:400:400, span 5 10, wrap");

			jpaPanelWorkingOn.add(wkvfLagerstand, "span 3, split 2");
			jpaPanelWorkingOn.add(wkvfInfertigung, "wrap");
			jpaPanelWorkingOn.add(wkvfReserviert, "span 3, split 2");
			jpaPanelWorkingOn.add(wkvfBestellt, "wrap");

			// PJ19760

			if (bZentralerArtikelstammUndGetrennteLaeger) {
				jpaPanelWorkingOn.add(wkvfReserviertIntern, "span 3, split 2");
				jpaPanelWorkingOn.add(wkvfBestelltIntern, "wrap");

				jpaPanelWorkingOn.add(new WrapperLabel(""), "span 3, split 2");
				jpaPanelWorkingOn.add(wkvfUnterwegs, "wrap");

			}

			jpaPanelWorkingOn.add(wkvfFehlmenge, "span 3, split 2");
			jpaPanelWorkingOn.add(wkvfRahmenreserviert, "wrap");
			jpaPanelWorkingOn.add(wkvfVerfuegbar, "top, span 3, split 2");
			jpaPanelWorkingOn.add(wkvfRahmenbestellt, "top, wrap");
			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_RAHMENDETAILBEDARFE)) {
				jpaPanelWorkingOn.add(wkvfRahmenbedarf, "span 2, wrap");
			}
		}

		list = new JList();

		Color defaultCellForegroundColor = UIManager.getColor("Table.foreground");

		list.setSelectionForeground(defaultCellForegroundColor);
		// list.setFont(new java.awt.Font("monospaced", 0, 12));
		list.setFont(new java.awt.Font("monospaced", 0, 11));
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jtpKommentare.setEnabled(false);

		jspKommentare = new JScrollPane(jtpKommentare);

		// PJ21394
		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_KOMMENTAR_HAT_VORRANG_IM_ARTIKELDETAIL, ParameterFac.KATEGORIE_ARTIKEL,
				LPMain.getTheClient().getMandant());
		boolean bKommentarHatVorrang = ((Boolean) parameter.getCWertAsObject());

		if (hasZusatzErsatztypenverwaltung() && bKommentarHatVorrang == false) {
			jpaPanelWorkingOn.add(listScroller, "span 3, split 2, height 200");

		} else {
			jpaPanelWorkingOn.add(jspKommentare, "span 3, split 2, grow,w 0:400:800, height 0:400:400");
		}

		// xcvb: Zusaetzliche Buttons fuer Detail-Panel angeben
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, ACTION_PREVIOUS,
				ACTION_NEXT, };

		enableToolsPanelButtons(aWhichButtonIUse);

		// PJ18881
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_KUNDESONDERKONDITIONEN)
				&& DelegateFactory.getInstance().getTheJudgeDelegate()
						.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF)) {

			getToolBar().addButtonCenter("/com/lp/client/res/data_into.png",
					LPMain.getTextRespectUISPr("artikel.goto.kundesosko"), MY_OWN_NEW_GOTO_SOKO, null, null);
			enableToolsPanelButtons(true, MY_OWN_NEW_GOTO_SOKO);
		}
		getToolBar().addButtonRight("/com/lp/client/res/document_attachment16x16.png",
				LPMain.getTextRespectUISPr("lp.dokumentablage"), MY_OWN_NEW_DOKUMENTENABLAGE, null, null);
		jbDokumente = getHmOfButtons().get(MY_OWN_NEW_DOKUMENTENABLAGE).getButton();

		if (bMehrsprachigkeit) {
			HelperClient.setMinimumAndPreferredSize(wcoLocale, HelperClient.getSizeFactoredDimension(165, 23));
			getToolBar().getToolsPanelRight().add(wcoLocale);

			getToolBar().addButtonRight("/com/lp/client/res/delete2.png",
					LPMain.getTextRespectUISPr("artikel.spr.inlocale.loeschen"), MY_OWN_NEW_SPR_LOESCHEN, null, null);

		}
		if (internalFrameArtikel.getTabbedPaneArtikel().bArtikelfreigabe == true) {

			boolean hatRecht = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_WW_FREIGABE_CUD);

			if (hatRecht) {

				getToolBar().addButtonCenter("/com/lp/client/res/check2.png",
						LPMain.getTextRespectUISPr("ww.freigeben"), MY_OWN_NEW_TOGGLE_FREIGABE, null,
						RechteFac.RECHT_STK_FREIGABE_CUD);
			}
			getToolBar().getToolsPanelCenter().add(wlaFreigabe);
		}

		// 4Vending
		if (hasZusatzFunktion4Vending()) {
			wbu4VendingId.setPreferredSize(new Dimension(140, 23));
			wbu4VendingId.setMinimumSize(new Dimension(140, 23));
			wbu4VendingId.setEnabled(true);
			wbu4VendingId.setActivatable(true);
			wbu4VendingId.setActionCommand(BUTTON_4VENDING_ID);
			wbu4VendingId.addActionListener(this);

			wla4VendingId.setPreferredSize(new Dimension(120, 23));
			wla4VendingId.setMinimumSize(new Dimension(120, 23));

			panel4VendingId.add(wbu4VendingId);
			panel4VendingId.add(wla4VendingId);
			panel4VendingId.setEnabled(true);
			getToolBar().getHmOfButtons().put(BUTTON_4VENDING_ID, new LPButtonAction(wbu4VendingId, null));
			getToolBar().getToolsPanelRight().add(panel4VendingId);
		}

	}

	private boolean hasZusatzFunktion4Vending() {
		return LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_4VENDING_SCHNITTSTELLE);
	}

	private boolean hasZusatzErsatztypenverwaltung() {
		return LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ERSATZTYPENVERWALTUNG);
	}

	private boolean hasZusatzFunktionSiWert() {
		return LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_SI_WERT);
	}

	private void installSiKeyListeners() {
		if (!hasZusatzFunktionSiWert())
			return;

		KeyListener siListener = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					siWertBerechnenAusTextfeldern();
				} catch (Throwable t) {
					getInternalFrame().handleException(t, false);
				}
			}
		};

		wtfBezeichnung.addKeyListener(siListener);
		wtfZusatzbez.addKeyListener(siListener);
		wtfZusatzbez2.addKeyListener(siListener);
	}

	protected void setDefaults() throws Throwable {
		Map<?, ?> m = DelegateFactory.getInstance().getArtikelDelegate().getAllSprArtikelarten();
		m.remove(ArtikelFac.ARTIKELART_HANDARTIKEL);
		wcoArtikelart.setMap(m);
		wcoMehrwertsteuer.setMap(DelegateFactory.getInstance().getMandantDelegate()
				.getAllMwstsatzbez(LPMain.getTheClient().getMandant()));
		wcoEinheit.setMap(DelegateFactory.getInstance().getSystemDelegate().getAllEinheiten());
		wcoEinheitBestellung.setMap(DelegateFactory.getInstance().getSystemDelegate().getAllEinheiten());

		wcoVorzugsteil.setMap(DelegateFactory.getInstance().getArtikelDelegate().getAllVorzug());

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT, ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null) {

			wcoEinheit.setKeyOfSelectedItem(Helper.fitString2Length(parameter.getCWert(), 15, ' '));
		}
		// Default-
		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_DEFAULT_ARTIKEL_ARTIKELART, ParameterFac.KATEGORIE_ARTIKEL,
				LPMain.getTheClient().getMandant());

		wcoArtikelart.setKeyOfSelectedItem(parameter.getCWert());
		wlaSperren.setText("");

	}

	protected void components2Dto() throws Throwable {

		wtfArtikelnummer.setText(wtfArtikelnummer.getText().trim());

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG)) {

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER,
							ParameterFac.KATEGORIE_ARTIKEL, LPMain.getTheClient().getMandant());

			int maxLaenge = ((Integer) parameter.getCWertAsObject()).intValue();

			if (wtfHerstellerKuerzel.getText() != null) {

				if (wtfArtikelnummer.getText().length() < maxLaenge) {
					internalFrameArtikel.getArtikelDto()
							.setCNr(Helper.fitString2Length(wtfArtikelnummer.getText(), maxLaenge, ' ')
									+ wtfHerstellerKuerzel.getText());
				} else {
					internalFrameArtikel.getArtikelDto()
							.setCNr(wtfArtikelnummer.getText() + wtfHerstellerKuerzel.getText());
				}
			} else {
				internalFrameArtikel.getArtikelDto().setCNr(wtfArtikelnummer.getText());
			}
		} else {
			internalFrameArtikel.getArtikelDto().setCNr(wtfArtikelnummer.getText());
		}

		internalFrameArtikel.getArtikelDto().setLfliefergruppeIId(wsfLiefergruppe.getIKey());

		internalFrameArtikel.getArtikelDto().setShopgruppeIId(wsfShopgruppe.getIKey());

		internalFrameArtikel.getArtikelDto().setArtikelartCNr((String) wcoArtikelart.getKeyOfSelectedItem());

		internalFrameArtikel.getArtikelDto().setBVersteckt(wcbVersteckt.getShort());
		internalFrameArtikel.getArtikelDto().setBKalkulatorisch(wcbKalkulatorisch.getShort());
		internalFrameArtikel.getArtikelDto().setBBevorzugt(wcbBevorzugt.getShort());

		internalFrameArtikel.getArtikelDto().setbBestellmengeneinheitInvers(wcbBestellmengeneinheitInvers.getShort());

		internalFrameArtikel.getArtikelDto().setbNurzurinfo(wcbNurZurInfo.getShort());
		internalFrameArtikel.getArtikelDto().setbReinemannzeit(wcbReineMannzeit.getShort());

		// Wenn Arbeitszeitartikel, dann immer Einheit= Stunden(h) (->WH
		// 09.11.05)
		if (((String) wcoArtikelart.getKeyOfSelectedItem()).equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
			internalFrameArtikel.getArtikelDto().setEinheitCNr(SystemFac.EINHEIT_STUNDE);
		} else {
			internalFrameArtikel.getArtikelDto().setEinheitCNr((String) wcoEinheit.getKeyOfSelectedItem());
		}

		if (wnfUmrechnungsfaktor.getBigDecimal() != null && wnfUmrechnungsfaktor.getBigDecimal().doubleValue() == 0) {
			internalFrameArtikel.getArtikelDto().setNUmrechnungsfaktor(null);
			internalFrameArtikel.getArtikelDto().setEinheitCNrBestellung(null);

		} else {
			internalFrameArtikel.getArtikelDto().setNUmrechnungsfaktor(wnfUmrechnungsfaktor.getBigDecimal());

		}

		internalFrameArtikel.getArtikelDto()
				.setEinheitCNrBestellung((String) wcoEinheitBestellung.getKeyOfSelectedItem());

		internalFrameArtikel.getArtikelDto().setMwstsatzbezIId((Integer) wcoMehrwertsteuer.getKeyOfSelectedItem());

		internalFrameArtikel.getArtikelDto().setVorzugIId((Integer) wcoVorzugsteil.getKeyOfSelectedItem());

		internalFrameArtikel.getArtikelDto().setCReferenznr(wtfReferenznummer.getText());

		if (internalFrameArtikel.getArtikelDto().getArtikelsprDto() == null) {
			internalFrameArtikel.getArtikelDto().setArtikelsprDto(new ArtikelsprDto());
		}

		internalFrameArtikel.getArtikelDto().setCRevision(wtfRevision.getText());
		internalFrameArtikel.getArtikelDto().setCIndex(wtfIndex.getText());

		internalFrameArtikel.getArtikelDto().getArtikelsprDto().setCBez(wtfBezeichnung.getText());
		internalFrameArtikel.getArtikelDto().getArtikelsprDto().setCKbez(wtfKurzbezeichnung.getText());
		internalFrameArtikel.getArtikelDto().getArtikelsprDto().setCZbez(wtfZusatzbez.getText());
		internalFrameArtikel.getArtikelDto().getArtikelsprDto().setCZbez2(wtfZusatzbez2.getText());

		// PJ19566
		if (bMehrsprachigkeit == false) {
			internalFrameArtikel.getArtikelDto().getArtikelsprDto()
					.setLocaleCNr(LPMain.getTheClient().getLocUiAsString());
		} else {
			internalFrameArtikel.getArtikelDto().getArtikelsprDto()
					.setLocaleCNr((String) wcoLocale.getKeyOfSelectedItem());
		}

	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (internalFrameArtikel.getTabbedPaneArtikel().bArtikelfreigabe == true) {
			// Wenn Fregegeben, dann nicht mehr aenderbar
			if (internalFrameArtikel.getArtikelDto() != null
					&& internalFrameArtikel.getArtikelDto().getTFreigabe() != null) {
				lockStateValue = new LockStateValue(PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lockStateValue;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfArtikelnummer;
	}

	protected void dto2Components() throws Throwable {
		wcbVersteckt.setShort(internalFrameArtikel.getArtikelDto().getBVersteckt());

		wcbKalkulatorisch.setShort(internalFrameArtikel.getArtikelDto().getBKalkulatorisch());
		wcbBevorzugt.setShort(internalFrameArtikel.getArtikelDto().getBBevorzugt());

		wcbReineMannzeit.setShort(internalFrameArtikel.getArtikelDto().getbReinemannzeit());
		wcbNurZurInfo.setShort(internalFrameArtikel.getArtikelDto().getbNurzurinfo());
		wcbBestellmengeneinheitInvers.setShort(internalFrameArtikel.getArtikelDto().getbBestellmengeneinheitInvers());

		String sperren = DelegateFactory.getInstance().getArtikelDelegate()
				.getArtikelsperrenText(internalFrameArtikel.getArtikelDto().getIId());
		if (sperren != null) {
			wlaSperren.setText(LPMain.getTextRespectUISPr("lp.sperren") + ": " + sperren);
		} else {
			wlaSperren.setText("");
		}
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_HERSTELLERKOPPLUNG)) {

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER,
							ParameterFac.KATEGORIE_ARTIKEL, LPMain.getTheClient().getMandant());

			int maxLaenge = ((Integer) parameter.getCWertAsObject()).intValue();

			if (internalFrameArtikel.getArtikelDto().getCNr().length() > maxLaenge) {
				wtfArtikelnummer.setText(internalFrameArtikel.getArtikelDto().getCNr().substring(0, maxLaenge));
				wtfHerstellerKuerzel.setText(internalFrameArtikel.getArtikelDto().getCNr().substring(maxLaenge));
			} else {
				wtfArtikelnummer.setText(internalFrameArtikel.getArtikelDto().getCNr());
				wtfHerstellerKuerzel.setText(null);
			}

		} else {
			wtfArtikelnummer.setText(internalFrameArtikel.getArtikelDto().getCNr());
		}
		wtfReferenznummer.setText(internalFrameArtikel.getArtikelDto().getCReferenznr());
		wsfLiefergruppe.setKey(internalFrameArtikel.getArtikelDto().getLfliefergruppeIId());
		wsfShopgruppe.setKey(internalFrameArtikel.getArtikelDto().getShopgruppeIId());

		if (internalFrameArtikel.getArtikelDto().getArtgruIId() != null) {
			internalFrameArtikel.getArtikelDto().setArtgruDto(DelegateFactory.getInstance().getArtikelDelegate()
					.artgruFindByPrimaryKey(internalFrameArtikel.getArtikelDto().getArtgruIId()));
			if (internalFrameArtikel.getArtikelDto().getArtgruDto().getArtgrusprDto() != null
					&& internalFrameArtikel.getArtikelDto().getArtgruDto().getArtgrusprDto().getCBez() != null) {
				wtfArtikelgruppe
						.setText(internalFrameArtikel.getArtikelDto().getArtgruDto().getArtgrusprDto().getCBez());
			} else {
				wtfArtikelgruppe.setText(internalFrameArtikel.getArtikelDto().getArtgruDto().getCNr());
			}

		} else {
			wtfArtikelgruppe.setText("");

		}
		if (internalFrameArtikel.getArtikelDto().getArtklaIId() != null) {
			internalFrameArtikel.getArtikelDto().setArtklaDto(DelegateFactory.getInstance().getArtikelDelegate()
					.artklaFindByPrimaryKey(internalFrameArtikel.getArtikelDto().getArtklaIId()));
			wtfArtikelklasse.setText(internalFrameArtikel.getArtikelDto().getArtklaDto().getBezeichnung());
		} else {
			wtfArtikelklasse.setText("");

		}
		if (internalFrameArtikel.getArtikelDto().getHerstellerIId() != null) {
			internalFrameArtikel.getArtikelDto().setHerstellerDto(DelegateFactory.getInstance().getArtikelDelegate()
					.herstellerFindBdPrimaryKey(internalFrameArtikel.getArtikelDto().getHerstellerIId()));
			wtfHersteller.setText(
					internalFrameArtikel.getArtikelDto().getHerstellerDto().getBezeichnung());
		} else {
			wtfHersteller.setText("");
		}
		wcoArtikelart.setKeyOfSelectedItem(internalFrameArtikel.getArtikelDto().getArtikelartCNr());
		wcoEinheit.setKeyOfSelectedItem(internalFrameArtikel.getArtikelDto().getEinheitCNr());
		wcoEinheitBestellung.setKeyOfSelectedItem(internalFrameArtikel.getArtikelDto().getEinheitCNrBestellung());
		wcoMehrwertsteuer.setKeyOfSelectedItem(internalFrameArtikel.getArtikelDto().getMwstsatzbezIId());

		wcoVorzugsteil.setKeyOfSelectedItem(internalFrameArtikel.getArtikelDto().getVorzugIId());

		wnfUmrechnungsfaktor.setBigDecimal(internalFrameArtikel.getArtikelDto().getNUmrechnungsfaktor());

		wtfIndex.setText(internalFrameArtikel.getArtikelDto().getCIndex());
		wtfRevision.setText(internalFrameArtikel.getArtikelDto().getCRevision());

		if (internalFrameArtikel.getArtikelDto().getLandIIdUrsprungsland() != null) {
			LandDto landDto = DelegateFactory.getInstance().getSystemDelegate()
					.landFindByPrimaryKey(internalFrameArtikel.getArtikelDto().getLandIIdUrsprungsland());
			wtfUrsprungsland.setText(landDto.getCLkz());
		} else {
			wtfUrsprungsland.setText(null);
		}

		if (internalFrameArtikel.getArtikelDto().getNUmrechnungsfaktor() == null) {
			wcoEinheitBestellung.setKeyOfSelectedItem(null);
			wcbBestellmengeneinheitInvers.setShort(Helper.boolean2Short(false));
		}

		if (internalFrameArtikel.getArtikelDto().getArtikelsprDto() != null) {

			if (internalFrameArtikel.getArtikelDto().getArtikelsprDto().getNSiwert() != null) {
				BigDecimalSI bdSI = new BigDecimalSI(
						internalFrameArtikel.getArtikelDto().getArtikelsprDto().getNSiwert());
				wlaSiWert.setText(bdSI.toSIString());
			} else {
				wlaSiWert.setText(null);
			}

			wtfKurzbezeichnung.setText(internalFrameArtikel.getArtikelDto().getArtikelsprDto().getCKbez());
			wtfBezeichnung.setText(internalFrameArtikel.getArtikelDto().getArtikelsprDto().getCBez());
			wtfZusatzbez.setText(internalFrameArtikel.getArtikelDto().getArtikelsprDto().getCZbez());
			wtfZusatzbez2.setText(internalFrameArtikel.getArtikelDto().getArtikelsprDto().getCZbez2());
			if (!internalFrameArtikel.getArtikelDto().getArtikelsprDto().getLocaleCNr()
					.equals(LPMain.getTheClient().getLocKonzernAsString())) {

			} else {

				BigDecimal lagerstand = DelegateFactory.getInstance().getLagerDelegate()
						.getLagerstandAllerLagerEinesMandanten(internalFrameArtikel.getArtikelDto().getIId(), false);

				BigDecimal fehlmengen = DelegateFactory.getInstance().getFehlmengeDelegate()
						.getAnzahlFehlmengeEinesArtikels(internalFrameArtikel.getArtikelDto().getIId());
				BigDecimal reservierungen = DelegateFactory.getInstance().getReservierungDelegate()
						.getAnzahlReservierungen(internalFrameArtikel.getArtikelDto().getIId());

				BigDecimal reservierungenIntern = BigDecimal.ZERO;
				if (bZentralerArtikelstammUndGetrennteLaeger) {
					reservierungenIntern = DelegateFactory.getInstance().getReservierungDelegate()
							.getAnzahlInterneReservierungen(internalFrameArtikel.getArtikelDto().getIId());
					wkvfReserviertIntern
							.setValue(Helper.formatZahl(reservierungenIntern, 2, LPMain.getTheClient().getLocUi()));

				}

				BigDecimal paternoster = DelegateFactory.getInstance().getLagerDelegate()
						.getPaternosterLagerstand(internalFrameArtikel.getArtikelDto().getIId());

				wkvfPaternoster.setValue(Helper.formatZahl(paternoster, 2, LPMain.getTheClient().getLocUi()));

				wkvfLagerstand.setValue(Helper.formatZahl(lagerstand, 2, LPMain.getTheClient().getLocUi()));

				if (reservierungen.doubleValue() < 0) {
					wkvfReserviert.getWlaValue().setForeground(Color.RED);
				} else {
					wkvfReserviert.getWlaValue().setForeground(Color.BLACK);
				}

				wkvfReserviert.setValue(Helper.formatZahl(reservierungen, 2, LPMain.getTheClient().getLocUi()));

				if (fehlmengen.doubleValue() < 0) {
					wkvfFehlmenge.getWlaValue().setForeground(Color.RED);
				} else {
					wkvfFehlmenge.getWlaValue().setForeground(Color.BLACK);
				}

				wkvfFehlmenge.setValue(Helper.formatZahl(fehlmengen, 2, LPMain.getTheClient().getLocUi()));

				BigDecimal verfuegbar = lagerstand.subtract(reservierungen).subtract(fehlmengen);

				if (verfuegbar.doubleValue() < 0) {
					wkvfVerfuegbar.getWlaValue().setForeground(Color.RED);
				} else {
					wkvfVerfuegbar.getWlaValue().setForeground(Color.BLACK);
				}

				wkvfVerfuegbar.setValue(Helper.formatZahl(verfuegbar, 2, LPMain.getTheClient().getLocUi()));
				BigDecimal infertigung = DelegateFactory.getInstance().getFertigungDelegate()
						.getAnzahlInFertigung(internalFrameArtikel.getArtikelDto().getIId());
				wkvfInfertigung.setValue(Helper.formatZahl(infertigung, 2, LPMain.getTheClient().getLocUi()));

				BigDecimal bestellt = DelegateFactory.getInstance().getArtikelbestelltDelegate()
						.getAnzahlBestellt(internalFrameArtikel.getArtikelDto().getIId());
				wkvfBestellt.setValue(Helper.formatZahl(bestellt, 2, LPMain.getTheClient().getLocUi()));
				BigDecimal bestelltIntern = BigDecimal.ZERO;
				BigDecimal unterwegs = BigDecimal.ZERO;
				if (bZentralerArtikelstammUndGetrennteLaeger) {
					bestelltIntern = DelegateFactory.getInstance().getArtikelbestelltDelegate()
							.getAnzahlInternBestellt(internalFrameArtikel.getArtikelDto().getIId());
					wkvfBestelltIntern.setValue(Helper.formatZahl(bestelltIntern, 2, LPMain.getTheClient().getLocUi()));
					unterwegs = DelegateFactory.getInstance().getArtikelbestelltDelegate()
							.getWareUnterwegsEinesArtikels(internalFrameArtikel.getArtikelDto().getIId());
					wkvfUnterwegs.setValue(Helper.formatZahl(unterwegs, 2, LPMain.getTheClient().getLocUi()));
				}

				BigDecimal rahmenres = DelegateFactory.getInstance().getReservierungDelegate()
						.getAnzahlRahmenreservierungen(internalFrameArtikel.getArtikelDto().getIId());
				wkvfRahmenreserviert.setValue(Helper.formatZahl(rahmenres, 2, LPMain.getTheClient().getLocUi()));

				BigDecimal rahmenbestellt = null;
				Hashtable<?, ?> htAnzahlRahmenbestellt = DelegateFactory.getInstance().getArtikelbestelltDelegate()
						.getAnzahlRahmenbestellt(internalFrameArtikel.getArtikelDto().getIId());
				if (htAnzahlRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
					rahmenbestellt = (BigDecimal) htAnzahlRahmenbestellt
							.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
					wkvfRahmenbestellt.setValue(Helper.formatZahl(rahmenbestellt, 2, LPMain.getTheClient().getLocUi()));
				}

				BigDecimal rahmenbedarf = DelegateFactory.getInstance().getRahmenbedarfeDelegate()
						.getSummeAllerRahmenbedarfeEinesArtikels(internalFrameArtikel.getArtikelDto().getIId());
				wkvfRahmenbedarf.setValue(Helper.formatZahl(rahmenbedarf, 2, LPMain.getTheClient().getLocUi()));

			}
			if (Helper.short2boolean(internalFrameArtikel.getArtikelDto().getBLagerbewirtschaftet()) == false) {

				wkvfVerfuegbar.setValue("----");
				wkvfLagerstand.setValue("----");
			}
		}

		ArtikelsprDto defaultSprDto = DelegateFactory.getInstance().getArtikelDelegate()
				.getDefaultArtikelbezeichnungen(internalFrameArtikel.getArtikelDto().getIId());

		if (defaultSprDto != null) {
			wtfKurzbezeichnungStd.setText(defaultSprDto.getCKbez());
			wtfArtikelBezStd.setText(defaultSprDto.getCBez());
			wtfArtikelZBezStd.setText(defaultSprDto.getCZbez());
			wtfArtikelZBez2Std.setText(defaultSprDto.getCZbez2());
		}

		// DefaultBild- Holen
		ArrayList<byte[]> b = DelegateFactory.getInstance().getArtikelkommentarDelegate()
				.getArtikelBilder(internalFrameArtikel.getArtikelDto().getIId());
		pi.setImages(b);

		pi.setTextPDFVorhandenVisible(DelegateFactory.getInstance().getArtikelkommentarDelegate()
				.sindTexteOderPDFsVorhanden(internalFrameArtikel.getArtikelDto().getIId()));

		// Goto Stkl

		if (LPMain.getInstance().getDesktopController()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {
			StuecklisteDto[] stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
					.stuecklisteFindByArtikelIId(internalFrameArtikel.getArtikelDto().getIId());

			if (stklDto.length > 0) {
				wbuStkl.setActivatable(false);
				wbuStkl.setText(LPMain.getTextRespectUISPr("artikel.detail.stkl"));
				wbuStkl.getWrapperButtonGoTo().setVisible(true);
				wbuStkl.setOKey(stklDto[0].getArtikelIId());

			} else {
				wbuStkl.setActivatable(true);
				wbuStkl.setEnabled(true);
				wbuStkl.setText(LPMain.getTextRespectUISPr("artikel.detail.stkl.erz"));
				wbuStkl.getWrapperButtonGoTo().setVisible(false);
				wbuStkl.setOKey(null);
			}
		} else {
			StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(internalFrameArtikel.getArtikelDto().getIId());

			if (stklDto != null && stklDto.getIId() != null) {
				wbuStkl.setActivatable(false);
				wbuStkl.setText(LPMain.getTextRespectUISPr("artikel.detail.stkl"));
				wbuStkl.getWrapperButtonGoTo().setVisible(true);
				wbuStkl.setOKey(stklDto.getIId());

			} else {
				wbuStkl.setActivatable(true);
				wbuStkl.setEnabled(true);
				wbuStkl.setText(LPMain.getTextRespectUISPr("artikel.detail.stkl.erz"));
				wbuStkl.getWrapperButtonGoTo().setVisible(false);
				wbuStkl.setOKey(null);
			}
		}

		PrintInfoDto values = DelegateFactory.getInstance().getJCRDocDelegate().getPathAndPartnerAndTable(
				internalFrameArtikel.getArtikelDto().getIId(), QueryParameters.UC_ID_ARTIKELLISTE);

		JCRRepoInfo repoInfo = new JCRRepoInfo();
		// boolean hasFiles = false;
		if (values != null && values.getDocPath() != null) {
			repoInfo = DelegateFactory.getInstance().getJCRDocDelegate().checkIfNodeExists(values.getDocPath());
			enableToolsPanelButtons(repoInfo.isOnline(), MY_OWN_NEW_DOKUMENTENABLAGE);
			// boolean online = DelegateFactory.getInstance()
			// .getJCRDocDelegate().isOnline();
			// enableToolsPanelButtons(online, MY_OWN_NEW_DOKUMENTENABLAGE);
			// if (online) {
			// hasFiles = DelegateFactory.getInstance()
			// .getJCRDocDelegate()
			// .checkIfNodeExists(values.getDocPath());
			// }
			// }
		}
		jbDokumente.setIcon(repoInfo.isExists() ? DOKUMENTE : KEINE_DOKUMENTE);

		if (hasZusatzErsatztypenverwaltung()) {

			verfuegbarkeitErsatztypen = DelegateFactory.getInstance().getArtikelDelegate()
					.getVerfuegbarkeitErsatztypen(internalFrameArtikel.getArtikelDto().getIId());
			list.removeAll();
			Object[] tempZeilen = new Object[verfuegbarkeitErsatztypen.size()];
			for (int i = 0; i < verfuegbarkeitErsatztypen.size(); i++) {
				String s = verfuegbarkeitErsatztypen.get(i);
				tempZeilen[i] = s;
			}
			list.setListData(tempZeilen);
		}

		String text = "";

		if (internalFrameArtikel.getArtikelDto().getTFreigabe() != null) {
			text = LPMain.getTextRespectUISPr("ww.freigegebenam") + " " + Helper.formatDatumZeit(
					internalFrameArtikel.getArtikelDto().getTFreigabe(), LPMain.getTheClient().getLocUi());
		}
		if (internalFrameArtikel.getArtikelDto().getPersonalIIdFreigabe() != null) {
			text += "(" + DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(internalFrameArtikel.getArtikelDto().getPersonalIIdFreigabe())
					.getCKurzzeichen() + ")";
		}

		wlaFreigabe.setText(text);

		this.setStatusbarPersonalIIdAendern(internalFrameArtikel.getArtikelDto().getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(internalFrameArtikel.getArtikelDto().getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(internalFrameArtikel.getArtikelDto().getTAnlegen());
		this.setStatusbarTAendern(internalFrameArtikel.getArtikelDto().getTAendern());

		// 4Vending
		init4VendingComponents(internalFrameArtikel.getArtikelDto().getCUL());

		sprachabhaengigeBezeichnungAnhandComboBoxSetzen(false);

		// PJ21102
		String kommentare = DelegateFactory.getInstance().getArtikelkommentarDelegate()
				.getArtikelkommentarFuerDetail(internalFrameArtikel.getArtikelDto().getIId());
		jtpKommentare.setText(kommentare);

	}

	/**
	 * 
	 */
	private void init4VendingComponents(String fourVendingId) {
		if (fourVendingId == null) {
			wla4VendingId.setText("");
			wbu4VendingId.setText(LPMain.getTextRespectUISPr("artikel.vendidata.idgenerieren"));
		} else {
			wla4VendingId.setText(LPMain.getMessageTextRespectUISPr("artikel.vendidata.idgleich", fourVendingId));
			wbu4VendingId.setText(LPMain.getTextRespectUISPr("artikel.vendidata.idloeschen"));
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			String einheitVorher = internalFrameArtikel.getArtikelDto().getEinheitCNr();
			components2Dto();
			try {
				if (internalFrameArtikel.getArtikelDto().getIId() == null) {
					internalFrameArtikel.getArtikelDto().setIId(DelegateFactory.getInstance().getArtikelDelegate()
							.createArtikel(internalFrameArtikel.getArtikelDto()));
					setKeyWhenDetailPanel(internalFrameArtikel.getArtikelDto().getIId());
					internalFrameArtikel.setArtikelDto(internalFrameArtikel.getArtikelDto());
				} else {

					ArtikelDto artikelDtoVorher = DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByPrimaryKey(internalFrameArtikel.getArtikelDto().getIId());

					// PJ 16901
					boolean bArtGruGeaendert = false;
					if (internalFrameArtikel.getArtikelDto().getArtgruIId() == null
							&& artikelDtoVorher.getArtgruIId() != null) {
						bArtGruGeaendert = true;
					}
					if (internalFrameArtikel.getArtikelDto().getArtgruIId() != null
							&& artikelDtoVorher.getArtgruIId() == null) {
						bArtGruGeaendert = true;
					}

					if (internalFrameArtikel.getArtikelDto().getArtgruIId() != null
							&& artikelDtoVorher.getArtgruIId() != null && !internalFrameArtikel.getArtikelDto()
									.getArtgruIId().equals(artikelDtoVorher.getArtgruIId())) {
						bArtGruGeaendert = true;
					}

					if (bArtGruGeaendert == true) {

						PaneldatenDto[] eigs = DelegateFactory.getInstance().getPanelDelegate()
								.paneldatenFindByPanelCNrCKey(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN,
										internalFrameArtikel.getArtikelDto().getIId() + "");

						if (eigs != null && eigs.length > 0) {

							boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
									LPMain.getTextRespectUISPr("artikel.artgru.geaendert"));

							if (b == false) {

								return;
							}
						}
					}

					if (Helper.short2boolean(internalFrameArtikel.getArtikelDto().getBBevorzugt())) {
						Integer artikelIId = DelegateFactory.getInstance().getArtikelDelegate()
								.gibtEsBereitsEinenBevorzugtenArtikel(internalFrameArtikel.getArtikelDto().getCNr());
						if (artikelIId != null && !artikelIId.equals(internalFrameArtikel.getArtikelDto().getIId())) {

							int indexVorhanden = 0;
							int indexAktuell = 1;
							int iAnzahlOptionen = 2;

							Object[] aOptionenVerdichten = new Object[iAnzahlOptionen];

							ArtikelDto aDtoVorhanden = DelegateFactory.getInstance().getArtikelDelegate()
									.artikelFindByPrimaryKey(artikelIId);

							aOptionenVerdichten[indexVorhanden] = aDtoVorhanden.getCNr();
							aOptionenVerdichten[indexAktuell] = internalFrameArtikel.getArtikelDto().getCNr();

							Object[] params = { LPMain.getMessageTextRespectUISPr("artikel.bevorzugt.bereitsvorhanden",
									aDtoVorhanden.getCNr()) };

							// PJ20358
							int iAuswahl = JOptionPane.showOptionDialog(getInternalFrame(), params,
									LPMain.getTextRespectUISPr("lp.frage"), JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE, null, // Icon
									aOptionenVerdichten, aOptionenVerdichten[0]);

							if (iAuswahl == indexVorhanden) {
								internalFrameArtikel.getArtikelDto().setBBevorzugt(Helper.getShortFalse());
							} else if (iAuswahl == indexAktuell) {
								aDtoVorhanden.setBBevorzugt(Helper.getShortFalse());
								DelegateFactory.getInstance().getArtikelDelegate().updateArtikel(aDtoVorhanden);
							} else {
								return;
							}

						}

					}

					DelegateFactory.getInstance().getArtikelDelegate()
							.updateArtikel(internalFrameArtikel.getArtikelDto());

					// PJ 13562
					if (internalFrameArtikel.getArtikelDto().getIId() != null && einheitVorher != null) {
						if (!einheitVorher.equals((String) wcoEinheit.getKeyOfSelectedItem())) {
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
									LPMain.getTextRespectUISPr("artikel.error.einheitgeaendert"));
						}
					}

				}

				super.eventActionSave(e, true);
				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(internalFrameArtikel.getArtikelDto().getIId().toString());
				}
				eventYouAreSelected(false);
				internalFrameArtikel.setArtikelDto(DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(internalFrameArtikel.getArtikelDto().getIId()));

			} catch (ExceptionLP ex) {
				if (ex.getICode() == EJBExceptionLP.FEHLER_ARTIKEL_ZEICHEN_IN_ARTIKELNUMMER_NICHT_ERLAUBT) {
					String msg = LPMain.getTextRespectUISPr("lp.error.zeicheninartikelnummernichterlaubt");
					if (ex.getAlInfoForTheClient() != null) {
						msg = msg + " (" + ex.getAlInfoForTheClient().get(0) + ")";
					}

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), msg);

				} else if (ex.getICode() == EJBExceptionLP.FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_KURZ) {
					String msg = LPMain.getTextRespectUISPr("lp.error.artikelnrzukurz");
					if (ex.getAlInfoForTheClient() != null) {
						msg = msg + " (Mindestens " + ex.getAlInfoForTheClient().get(0) + " Stellen)";
					}

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), msg);

				} else if (ex.getICode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
					bKeinFocusLost = true;

					handleException(ex, false);
				} else {
					handleException(ex, false);
				}
			}

		}
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

		if (getPanelQueryFLRForCallback() != null) {
			getPanelQueryFLRForCallback().getDialog().setVisible(true);
			setPanelQueryFLRForCallback(null);

			LPMain.getInstance().getDesktop().closeFrame(getInternalFrame());
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		internalFrameArtikel.setArtikelDto(new ArtikelDto());
		leereAlleFelder(this);
		setDefaults();
		wbuStkl.setVisible(false);

	}

	public void siWertBerechnenAusTextfeldern() throws Throwable {
		siWertBerechnen(wtfBezeichnung.getText(), wtfZusatzbez.getText(), wtfZusatzbez2.getText());
	}

	private Boolean getSiOhneEinheitenFromParam() throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_SI_OHNE_EINHEIT, ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		return (Boolean) parameter.getCWertAsObject();
	}

	private String getSiEinheitenFromParam() throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_SI_EINHEITEN, ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		return parameter.getCWert();
	}

	private void siWertBerechnen(String cBez, String cZbez, String cZbez2) throws Throwable {
		if (!hasZusatzFunktionSiWert())
			return;

		// PJ18155
		try {
			BigDecimalSI bdSi = siwertParser.berechneSiWertAusBezeichnung(cBez, cZbez, cZbez2);
			// BigDecimalSI bdSiO = Helper.berechneSiWertAusBezeichnung(
			// getSiOhneEinheitenFromParam(), getSiEinheitenFromParam(), cBez,
			// cZbez, cZbez2);
			wlaSiWert.setText(bdSi != null ? bdSi.toSIString() : null);
		} catch (IllegalArgumentException iae) {
			wlaSiWert.setText(null);
			myLogger.error("Mandantenparameter SI_OHNE_EINHEIT und SI_EINHEITEN stehen in Konflikt!", iae);
		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getArtikelDelegate().removeArtikel(internalFrameArtikel.getArtikelDto().getIId());
		super.eventActionDelete(e, true, true);
	}

	public void wtfArtikelnummer_focusLost(FocusEvent e) {
		if (bKeinFocusLost == false && wtfArtikelnummer.getText() != null
				&& internalFrameArtikel.getArtikelDto() != null
				&& internalFrameArtikel.getArtikelDto().getIId() == null) {

			try {

				ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByCNr(wtfArtikelnummer.getText());

				String text = LPMain.getTextRespectUISPr("artikel.existiertbereits.teil1") + "'" + aDto.getCNr() + "'.";

				if (Helper.short2boolean(aDto.getBVersteckt())) {
					text += " " + LPMain.getTextRespectUISPr("artikel.existiertbereits.teil2");
				}

				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), text);
			} catch (Throwable ex1) {
				// OK ->Artikel noch nicht vorhanden
			}

		} else {
			bKeinFocusLost = false;
		}

	}

	public void wcoEinheitBestellung_actionPerformed(ActionEvent e) {
		String key = (String) wcoEinheitBestellung.getKeyOfSelectedItem();
		if (key == null) {
			wnfUmrechnungsfaktor.setInteger(null);
			wnfUmrechnungsfaktor.setMandatoryField(false);
		} else {
			wnfUmrechnungsfaktor.setMandatoryField(true);
		}
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
		// SP4510 remove (multiple)image viewer references in the layout manager
		jpaPanelWorkingOn.removeAll();
		jpaPanelWorkingOn.setLayout(null);
		jpaPanelWorkingOn = null;
		pi.cleanup();
		pi = null;
	}

}

class PanelArtikel_wcoEinheitBestellung_actionAdapter implements ActionListener {
	private PanelArtikel adaptee;

	PanelArtikel_wcoEinheitBestellung_actionAdapter(PanelArtikel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoEinheitBestellung_actionPerformed(e);
	}
}

class PanelArtikel_wtfArtikelnummer_focusAdapter extends FocusAdapter {
	private PanelArtikel adaptee;

	PanelArtikel_wtfArtikelnummer_focusAdapter(PanelArtikel adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wtfArtikelnummer_focusLost(e);
	}
}
