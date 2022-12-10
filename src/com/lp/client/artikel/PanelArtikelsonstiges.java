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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
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
import com.lp.client.frame.component.WrapperFormattedTextField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.SollverkaufDto;
import com.lp.server.finanz.service.WarenverkehrsnummerDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelArtikelsonstiges extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArtikelDto artikelDto = null;
	private InternalFrameArtikel internalFrameArtikel = null;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaMaxvertreterprovision = new WrapperLabel();
	private WrapperNumberField wnfMaxvertreterprovision = new WrapperNumberField();
	private WrapperLabel wla2 = new WrapperLabel();
	private WrapperLabel wlaGarantiezeit = new WrapperLabel();
	private WrapperNumberField wnfGarantiezeit = new WrapperNumberField();
	private WrapperLabel wlaInMonaten = new WrapperLabel();
	private WrapperLabel wlaEinheitVerpackungsmenge = new WrapperLabel();
	private WrapperLabel wlaMinutenfaktor1 = new WrapperLabel();
	private WrapperNumberField wrapperNumberFieldMinutenfaktor1 = new WrapperNumberField();
	private WrapperLabel wlaMinutenfaktor2 = new WrapperLabel();

	private WrapperLabel wlaMinutenfaktor1Einheit = new WrapperLabel();
	private WrapperLabel wlaMinutenfaktor2Einheit = new WrapperLabel();

	private WrapperCheckBox wcbVKPreispflichtig = new WrapperCheckBox();
	
	private WrapperNumberField wnfMinutenfaktor2 = new WrapperNumberField();
	private WrapperLabel wlaVerpackungsmenge = new WrapperLabel();
	private WrapperNumberField wnfVerpackungsmenge = new WrapperNumberField();
	private WrapperLabel wlaMindestverkaufsmenge = new WrapperLabel();
	private WrapperNumberField wnfMindestverkaufsmenge = new WrapperNumberField();

	private WrapperNumberField wnfMindestdeckungsbeitrag = new WrapperNumberField();
	private WrapperLabel wlaMindestdeckungsbeitrag = new WrapperLabel();
	private WrapperLabel wla8 = new WrapperLabel();
	private WrapperLabel wlaAufschlag = new WrapperLabel();
	private WrapperNumberField wnfAufschlag = new WrapperNumberField();
	private WrapperLabel wlaSoll = new WrapperLabel();
	private WrapperNumberField wnfSoll = new WrapperNumberField();
	private WrapperLabel wlaVerkaufsean = new WrapperLabel();
	private WrapperTextField wtfVerkaufsean = new WrapperTextField();
	private WrapperTextField wtfEccn = new WrapperTextField();
	private WrapperLabel wlaVerpackungsean = new WrapperLabel();
	private WrapperLabel wlaEccn = new WrapperLabel();
	private WrapperTextField wtfVerpackungsean = new WrapperTextField();
	private WrapperButton wbuWarenverkehrsnummer = new WrapperButton();
	private WrapperFormattedTextField wtfWarenverkehrsnummer = new WrapperFormattedTextField();
	private WrapperCheckBox wcoLagerbewertet = new WrapperCheckBox();
	private WrapperCheckBox wcoRabattierbar = new WrapperCheckBox();
	private WrapperCheckBox wcoRahmenartikel = new WrapperCheckBox();
	private WrapperCheckBox wcoKommissionieren = new WrapperCheckBox();
	private WrapperCheckBox wcoKeineLagerzubuchung = new WrapperCheckBox();
	private WrapperCheckBox wcoDokumentenpflicht = new WrapperCheckBox();
	private WrapperCheckBox wcoLagerbewirtschaftet = new WrapperCheckBox();
	private WrapperCheckBox wcoVerleih = new WrapperCheckBox();
	private WrapperCheckBox wcoWerbeabgabepflichtig = new WrapperCheckBox();
	private WrapperCheckBox wcoAZInABNachkalkulation = new WrapperCheckBox();

	private WrapperComboBox wcoSnrChnr = new WrapperComboBox();

	private WrapperNumberField wnfLaengeSnrMin = new WrapperNumberField();
	private WrapperNumberField wnfLaengeSnrMax = new WrapperNumberField();

	private WrapperLabel wlaLetzteWartung = new WrapperLabel();
	private WrapperDateField wdfLetzteWartung = new WrapperDateField();

	private WrapperLabel wlaWartungsintervall = new WrapperLabel();
	private WrapperNumberField wnfWartungsintervall = new WrapperNumberField();
	private WrapperLabel wlaSofortverbrauch = new WrapperLabel();
	private WrapperNumberField wnfSofortverbrauch = new WrapperNumberField();

	private WrapperGotoButton wbuZugehoerigerArtikel = new WrapperGotoButton(
			com.lp.util.GotoHelper.GOTO_ARTIKEL_AUSWAHL);

	private WrapperTextField wtfZugehoerigerArtikel = new WrapperTextField();
	private WrapperNumberField wnfZugehoerigerArtikelMultiplikator = new WrapperNumberField();
	private WrapperCheckBox wcoMultiplikatorEinsDurch = new WrapperCheckBox();
	private WrapperCheckBox wcoMultiplikatorAufrunden = new WrapperCheckBox();
	

	private WrapperButton wbuUrsprungsland = new WrapperButton();
	private WrapperTextField wtfUrsprungsland = new WrapperTextField();

	private WrapperGotoButton wbuErsatzArtikel = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_ARTIKEL_AUSWAHL);

	private WrapperTextField wtfErsatzArtikel = new WrapperTextField();

	private WrapperLabel wlaVerpackungsmittelMenge = new WrapperLabel();
	private WrapperNumberField wnfVerpackungsmittelMenge = new WrapperNumberField();
	private WrapperSelectField wsfVerpackungsmittel = new WrapperSelectField(WrapperSelectField.VERPACKUNGSMITTEL,
			getInternalFrame(), true);

	private WrapperCheckBox wcoBewilligungspflichtig = new WrapperCheckBox();
	private WrapperCheckBox wcoMeldepflichtig = new WrapperCheckBox();
	
	private PanelQueryFLR panelQueryFLRArtikel = null;
	private PanelQueryFLR panelQueryFLRErsatzArtikel = null;
	private PanelQueryFLR panelQueryFLRWarenverkehrsnummer = null;
	private PanelQueryFLR panelQueryFLRLand = null;
	static final public String ACTION_SPECIAL_ARTIKEL_FROM_LISTE = "action_artikel_from_liste";
	static final public String ACTION_SPECIAL_ARTIKELERSATZ_FROM_LISTE = "ACTION_SPECIAL_ARTIKELERSATZ_FROM_LISTE";
	static final public String ACTION_SPECIAL_WARENVERKEHRSNUMMER_FROM_LISTE = "ACTION_SPECIAL_WARENVERKEHRSNUMMER_FROM_LISTE";
	static final public String ACTION_SPECIAL_LAND_FROM_LISTE = "ACTION_SPECIAL_LAND_FROM_LISTE";
	static final private String ACTION_SPECIAL_GENERIERE_VERKAUFSEAN = "ACTION_SPECIAL_GENERIERE_VERKAUFSEAN";

	private WrapperLabel wlaZBez2 = new WrapperLabel();
	private WrapperLabel wlaZbez = new WrapperLabel();
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperLabel wlaArtikelnummer = new WrapperLabel();
	private WrapperTextField wftBezeichnungStd = new WrapperTextField();
	private WrapperTextField wtfZBezStd = new WrapperTextField();
	private WrapperTextField wtfZBez2Std = new WrapperTextField();
	private WrapperTextField wtfArtikelnummer = new WrapperTextField();
	private WrapperLabel wla1 = new WrapperLabel();
	private WrapperLabel wla3 = new WrapperLabel();
	private WrapperLabel wlaArtikeleinheit = new WrapperLabel();
	private WrapperButton wbuGeneriereVerkaufsean = new WrapperButton();
	private JPanel panelVerkaufsean = new JPanel();

	private WrapperLabel wlaPreisZugehoerigerartikel = new WrapperLabel();
	private WrapperNumberField wnfPreisZugehoerigerartikel = new WrapperNumberField();
	
	public PanelArtikelsonstiges(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfMaxvertreterprovision;
	}

	void dialogQueryArtikelFromListe(ActionEvent e) throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_LEEREN };
		panelQueryFLRArtikel = new PanelQueryFLR(null, ArtikelFilterFactory.getInstance().createFKArtikelliste(),
				com.lp.server.util.fastlanereader.service.query.QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("title.artikelauswahlliste"),
				ArtikelFilterFactory.getInstance().createFKVArtikel(), null);
		panelQueryFLRArtikel.setFilterComboBox(DelegateFactory.getInstance().getArtikelDelegate().getAllSprArtgru(),
				new FilterKriterium("ag.i_id", true, "" + "", FilterKriterium.OPERATOR_IN, false), false,
				LPMain.getTextRespectUISPr("lp.alle"), false);
		panelQueryFLRArtikel.befuellePanelFilterkriterienDirekt(
				ArtikelFilterFactory.getInstance().createFKDArtikelnummer(internalFrameArtikel),
				ArtikelFilterFactory.getInstance().createFKDVolltextsuche());
		panelQueryFLRArtikel.setSelectedId(artikelDto.getArtikelIIdZugehoerig());
		new DialogQuery(panelQueryFLRArtikel);
	}

	void dialogQueryErsatzArtikelFromListe(ActionEvent e) throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_LEEREN };
		panelQueryFLRErsatzArtikel = new PanelQueryFLR(null, ArtikelFilterFactory.getInstance().createFKArtikelliste(),
				com.lp.server.util.fastlanereader.service.query.QueryParameters.UC_ID_ARTIKELLISTE, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("title.artikelauswahlliste"),
				ArtikelFilterFactory.getInstance().createFKVArtikel(), null);
		panelQueryFLRErsatzArtikel.setFilterComboBox(
				DelegateFactory.getInstance().getArtikelDelegate().getAllSprArtgru(),
				new FilterKriterium("ag.i_id", true, "" + "", FilterKriterium.OPERATOR_IN, false), false,
				LPMain.getTextRespectUISPr("lp.alle"), false);
		panelQueryFLRErsatzArtikel.befuellePanelFilterkriterienDirekt(
				ArtikelFilterFactory.getInstance().createFKDArtikelnummer(internalFrameArtikel),
				ArtikelFilterFactory.getInstance().createFKDVolltextsuche());
		panelQueryFLRErsatzArtikel.setSelectedId(artikelDto.getArtikelIIdErsatz());
		new DialogQuery(panelQueryFLRErsatzArtikel);
	}

	void dialogQueryWarenverkehrsnummerFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRWarenverkehrsnummer = FinanzFilterFactory.getInstance()
				.createPanelFLRWarenverkehrsnummer(getInternalFrame(), wtfWarenverkehrsnummer.getFormattedText());
		new DialogQuery(panelQueryFLRWarenverkehrsnummer);

	}

	void dialogQueryLandFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };

		panelQueryFLRLand = new PanelQueryFLR(null, null, QueryParameters.UC_ID_LAND, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("title.landauswahlliste"));

		panelQueryFLRLand.setSelectedId(artikelDto.getLandIIdUrsprungsland());

		new DialogQuery(panelQueryFLRLand);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL_FROM_LISTE)) {
			dialogQueryArtikelFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELERSATZ_FROM_LISTE)) {
			dialogQueryErsatzArtikelFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_WARENVERKEHRSNUMMER_FROM_LISTE)) {
			dialogQueryWarenverkehrsnummerFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LAND_FROM_LISTE)) {
			dialogQueryLandFromListe(e);
		} else if (ACTION_SPECIAL_GENERIERE_VERKAUFSEAN.equals(e.getActionCommand())) {
			actionGeneriereVerkaufsean();
		}

	}

	private void actionGeneriereVerkaufsean() throws ExceptionLP, Throwable {
		String verkaufsean = DelegateFactory.getInstance().getArtikelDelegate()
				.generiereGTIN13Nummer(artikelDto.getIId());
		wtfVerkaufsean.setText(verkaufsean);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (artikelDto.getArtikelIIdZugehoerig() != null) {
				StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelDto.getIId());
				if (stklDto != null
						&& stklDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {
					DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"), LPMain
							.getInstance().getTextRespectUISPr("artikel.zugehoerigerartikel.undsetnichtmoeglich"));

					return;
				}
			}

			ArtikelDto artikelTempDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(artikelDto.getIId());

			if (Helper.short2boolean(artikelTempDto.getBLagerbewirtschaftet()) == true
					&& Helper.short2boolean(artikelDto.getBLagerbewirtschaftet()) == false) {

				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr("artikel.error.wechsel.lagerbewirtschaftet"),
						LPMain.getTextRespectUISPr("lp.warning"), JOptionPane.WARNING_MESSAGE, JOptionPane.NO_OPTION);

				if (b == false) {
					return;
				}

				Object[] aOptionen = new Object[2];
				aOptionen[0] = LPMain.getInstance()
						.getTextRespectUISPr("artikel.error.wechsel.lagerbewirtschaftet.nichtdurchfuehren");
				aOptionen[1] = LPMain.getInstance()
						.getTextRespectUISPr("artikel.error.wechsel.lagerbewirtschaftet.jasicher");

				int iAuswahl = DialogFactory.showModalDialog(getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr("artikel.error.wechsel.lagerbewirtschaftet2"),
						LPMain.getInstance().getTextRespectUISPr("lp.warning"), aOptionen, aOptionen[0]);

				if (iAuswahl != 1) {
					return;
				}

			}

			// PJ16141
			if (Helper.short2boolean(artikelTempDto.getBChargennrtragend()) != Helper
					.short2boolean(artikelDto.getBChargennrtragend())
					&& DelegateFactory.getInstance().getLagerDelegate()
							.sindBereitsLagerbewegungenVorhanden(artikelDto.getIId())) {
				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr("artikel.error.wechsel.chnrbehaftet"));
				if (b == true) {
					DelegateFactory.getInstance().getArtikelDelegate().updateArtikel(artikelDto);
				}

			} else {
				DelegateFactory.getInstance().getArtikelDelegate().updateArtikel(artikelDto);
			}

			artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(artikelDto.getIId());
			((InternalFrameArtikel) getInternalFrame()).setArtikelDto(artikelDto);
			super.eventActionSave(e, true);
		}
		eventYouAreSelected(false);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRArtikel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelTempDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				if (artikelTempDto.getIId().equals(internalFrameArtikel.getArtikelDto().getIId())) {
					DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr("artikel.error.kannnichtselbstzugeordnetwerden"));
				} else {
					wtfZugehoerigerArtikel.setText(artikelTempDto.formatArtikelbezeichnung());
					artikelDto.setArtikelIIdZugehoerig(artikelTempDto.getIId());
				}
			} else if (e.getSource() == panelQueryFLRErsatzArtikel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelTempDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				if (artikelTempDto.getIId().equals(internalFrameArtikel.getArtikelDto().getIId())) {
					DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr("artikel.error.kannnichtselbstzugeordnetwerden"));
				} else {
					wtfErsatzArtikel.setText(artikelTempDto.formatArtikelbezeichnung());
					artikelDto.setArtikelIIdErsatz(artikelTempDto.getIId());
				}
			} else if (e.getSource() == panelQueryFLRLand) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LandDto landDto = DelegateFactory.getInstance().getSystemDelegate().landFindByPrimaryKey((Integer) key);
				wtfUrsprungsland.setText(landDto.getCLkz());
				artikelDto.setLandIIdUrsprungsland(landDto.getIID());
			}

			else if (e.getSource() == panelQueryFLRWarenverkehrsnummer) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				wtfWarenverkehrsnummer.setText((String) key);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRArtikel) {

				wtfZugehoerigerArtikel.setText(null);
				artikelDto.setArtikelIIdZugehoerig(null);
			} else if (e.getSource() == panelQueryFLRErsatzArtikel) {

				wtfErsatzArtikel.setText(null);
				artikelDto.setArtikelIIdErsatz(null);
			} else if (e.getSource() == panelQueryFLRLand) {
				wtfUrsprungsland.setText(null);
				artikelDto.setLandIIdUrsprungsland(null);
			}

		}

	}

	protected void dto2Components() throws Throwable {

		wnfLaengeSnrMin.setInteger(artikelDto.getILaengeminSnrchnr());
		wnfLaengeSnrMax.setInteger(artikelDto.getILaengemaxSnrchnr());

		wcbVKPreispflichtig.setShort(artikelDto.getBVkpreispflichtig());
		
		wnfMaxvertreterprovision.setDouble(artikelDto.getFVertreterprovisionmax());
		wnfMindestdeckungsbeitrag.setDouble(artikelDto.getFMindestdeckungsbeitrag());
		wrapperNumberFieldMinutenfaktor1.setDouble(artikelDto.getFMinutenfaktor1());
		wnfMinutenfaktor2.setDouble(artikelDto.getFMinutenfaktor2());
		wnfVerpackungsmenge.setDouble(artikelDto.getFVerpackungsmenge());

		wnfMindestverkaufsmenge.setBigDecimal(artikelDto.getNMindestverkaufsmenge());

		wlaEinheitVerpackungsmenge.setText(artikelDto.getEinheitCNr().trim());
		wlaArtikeleinheit.setText(artikelDto.getEinheitCNr().trim());

		wtfEccn.setText(artikelDto.getCEccn());

		wnfZugehoerigerArtikelMultiplikator.setDouble(artikelDto.getFMultiplikatorZugehoerigerartikel());
		wcoMultiplikatorAufrunden.setShort(artikelDto.getBMultiplikatorAufrunden());
		wcoMultiplikatorEinsDurch.setShort(artikelDto.getBMultiplikatorInvers());
		
		wcoMeldepflichtig.setShort(artikelDto.getBMeldepflichtig());
		wcoBewilligungspflichtig.setShort(artikelDto.getBBewilligungspflichtig());
		
		wcoAZInABNachkalkulation.setShort(artikelDto.getBAzinabnachkalk());

		wtfVerkaufsean.setText(artikelDto.getCVerkaufseannr());
		wtfVerpackungsean.setText(artikelDto.getCVerpackungseannr());
		wtfWarenverkehrsnummer.setText(artikelDto.getCWarenverkehrsnummer());

		wnfWartungsintervall.setInteger(artikelDto.getIWartungsintervall());
		wnfSofortverbrauch.setInteger(artikelDto.getISofortverbrauch());

		wdfLetzteWartung.setTimestamp(artikelDto.getTLetztewartung());

		wnfVerpackungsmittelMenge.setBigDecimal(artikelDto.getNVerpackungsmittelmenge());
		wsfVerpackungsmittel.setKey(artikelDto.getVerpackungsmittelIId());

		wlaLetzteWartung.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.letztewartung"));

		if (artikelDto.getPersonalIIdLetztewartung() != null) {
			wlaLetzteWartung.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.letztewartung") + " ("
					+ DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey(artikelDto.getPersonalIIdLetztewartung()).getCKurzzeichen()
					+ ")");
		}

		wlaWartungsintervall.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.wartungsintervall"));
		if (wdfLetzteWartung.getTimestamp() != null && wnfWartungsintervall.getInteger() != null) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(wdfLetzteWartung.getTimestamp().getTime());
			c.add(Calendar.MONTH, wnfWartungsintervall.getInteger());

			String s = "<font color=\"#FF0000\">"
					+ LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.naechstewartung") + "</font>";

			if (c.getTime().before(new java.util.Date())) {
				s = "<font color=\"#FF0000\">"
						+ LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.naechstewartung") +  Helper.formatDatum(c.getTime(), LPMain.getTheClient().getLocUi()) + "</font>" +"&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp "+LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.wartungsintervall");
			} else {
				s = LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.naechstewartung") +  Helper.formatDatum(c.getTime(), LPMain.getTheClient().getLocUi()) + "&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp "+LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.wartungsintervall");
			
			}

			
			s="<html>"+s+"<body></body><2html>";
			
			wlaWartungsintervall.setText(s);

		}

		if (artikelDto.getCWarenverkehrsnummer() == null
				|| Helper.checkWarenverkehrsnummer(artikelDto.getCWarenverkehrsnummer().trim())
				|| artikelDto.getCWarenverkehrsnummer().length() == 0) {
			wtfWarenverkehrsnummer.setForeground(new WrapperTextField().getForeground());
		} else {
			wtfWarenverkehrsnummer.setForeground(Color.red);
		}

		if (artikelDto.getLandIIdUrsprungsland() != null) {
			LandDto landDto = DelegateFactory.getInstance().getSystemDelegate()
					.landFindByPrimaryKey(artikelDto.getLandIIdUrsprungsland());
			wtfUrsprungsland.setText(landDto.getCLkz());
		} else {
			wtfUrsprungsland.setText(null);
		}

		wcoRahmenartikel.setShort(artikelDto.getBRahmenartikel());
		wcoKeineLagerzubuchung.setShort(artikelDto.getBKeineLagerzubuchung());
		wcoKommissionieren.setShort(artikelDto.getBKommissionieren());

		if (artikelDto.isChargennrtragend()) {
			wcoSnrChnr.setKeyOfSelectedItem(ArtikelFac.SNRCHNR_CHNRBEHAFTET);
		} else if (artikelDto.isSeriennrtragend()) {
			wcoSnrChnr.setKeyOfSelectedItem(ArtikelFac.SNRCHNR_SNRBEHAFTET);
		} else {
			wcoSnrChnr.setKeyOfSelectedItem(ArtikelFac.SNRCHNR_OHNE);
		}

		wcoRabattierbar.setShort(artikelDto.getBRabattierbar());
		wcoDokumentenpflicht.setShort(artikelDto.getBDokumentenpflicht());
		wcoWerbeabgabepflichtig.setShort(artikelDto.getBWerbeabgabepflichtig());
		wcoLagerbewertet.setShort(artikelDto.getBLagerbewertet());
		wcoVerleih.setShort(artikelDto.getBVerleih());
		wcoLagerbewirtschaftet.setShort(artikelDto.getBLagerbewirtschaftet());
		wnfGarantiezeit.setInteger(artikelDto.getIGarantiezeit());
		if (artikelDto.getSollverkaufDto() != null) {
			wnfAufschlag.setDouble(artikelDto.getSollverkaufDto().getFAufschlag());
			wnfSoll.setDouble(artikelDto.getSollverkaufDto().getFSollverkauf());
		}
		if (artikelDto.getArtikelIIdZugehoerig() != null) {
			ArtikelDto artikelDtoTemp = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(artikelDto.getArtikelIIdZugehoerig());
			wtfZugehoerigerArtikel.setText(artikelDtoTemp.formatArtikelbezeichnung());
		} else {
			wtfZugehoerigerArtikel.setText(null);
		}

		wbuZugehoerigerArtikel.setOKey(artikelDto.getArtikelIIdZugehoerig());

		if (artikelDto.getArtikelIIdErsatz() != null) {
			ArtikelDto artikelDtoTemp = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(artikelDto.getArtikelIIdErsatz());
			wtfErsatzArtikel.setText(artikelDtoTemp.formatArtikelbezeichnung());

		} else {
			wtfErsatzArtikel.setText(null);
		}

		wnfPreisZugehoerigerartikel.setBigDecimal(artikelDto.getNPreisZugehoerigerartikel());
		
		ArrayList<String> s = DelegateFactory.getInstance().getArtikelDelegate()
				.getVorgaengerArtikel(artikelDto.getIId());

		StringBuffer str = new StringBuffer("Vorg\u00E4nger: ");
		for (int i = 0; i < s.size(); i++) {
			str.append(s.get(i));
			str.append(", ");
		}

		wbuErsatzArtikel.setToolTipText(str.toString());

		wbuErsatzArtikel.setOKey(artikelDto.getArtikelIIdErsatz());

		wtfArtikelnummer.setText(artikelDto.getCNr());
		if (artikelDto.getArtikelsprDto() != null) {
			wftBezeichnungStd.setText(artikelDto.getArtikelsprDto().getCBez());
			wtfZBezStd.setText(artikelDto.getArtikelsprDto().getCZbez());
			wtfZBez2Std.setText(artikelDto.getArtikelsprDto().getCZbez2());
		}
		this.setStatusbarPersonalIIdAendern(artikelDto.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(artikelDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(artikelDto.getTAnlegen());
		this.setStatusbarTAendern(artikelDto.getTAendern());

	}

	protected void components2Dto() throws Throwable {
		artikelDto.setFVertreterprovisionmax(wnfMaxvertreterprovision.getDouble());
		artikelDto.setFMindestdeckungsbeitrag(wnfMindestdeckungsbeitrag.getDouble());
		artikelDto.setFMinutenfaktor1(wrapperNumberFieldMinutenfaktor1.getDouble());
		artikelDto.setFMinutenfaktor2(wnfMinutenfaktor2.getDouble());
		artikelDto.setIGarantiezeit(wnfGarantiezeit.getInteger());
		artikelDto.setBRabattierbar(wcoRabattierbar.getShort());
		artikelDto.setBDokumentenpflicht(wcoDokumentenpflicht.getShort());
		artikelDto.setBWerbeabgabepflichtig(wcoWerbeabgabepflichtig.getShort());
		artikelDto.setBVkpreispflichtig(wcbVKPreispflichtig.getShort());

		if (wcoSnrChnr.getKeyOfSelectedItem().equals(ArtikelFac.SNRCHNR_CHNRBEHAFTET)) {
			artikelDto.setBChargennrtragend(Helper.boolean2Short(true));
			artikelDto.setBSeriennrtragend(Helper.boolean2Short(false));
		} else if (wcoSnrChnr.getKeyOfSelectedItem().equals(ArtikelFac.SNRCHNR_SNRBEHAFTET)) {
			artikelDto.setBChargennrtragend(Helper.boolean2Short(false));
			artikelDto.setBSeriennrtragend(Helper.boolean2Short(true));
		} else {
			artikelDto.setBChargennrtragend(Helper.boolean2Short(false));
			artikelDto.setBSeriennrtragend(Helper.boolean2Short(false));
		}

		artikelDto.setILaengeminSnrchnr(wnfLaengeSnrMin.getInteger());
		artikelDto.setILaengemaxSnrchnr(wnfLaengeSnrMax.getInteger());

		artikelDto.setBLagerbewertet(wcoLagerbewertet.getShort());
		artikelDto.setBVerleih(wcoVerleih.getShort());
		artikelDto.setBLagerbewirtschaftet(wcoLagerbewirtschaftet.getShort());
		artikelDto.setBRahmenartikel(wcoRahmenartikel.getShort());
		artikelDto.setBKommissionieren(wcoKommissionieren.getShort());
		artikelDto.setBKeineLagerzubuchung(wcoKeineLagerzubuchung.getShort());
		artikelDto.setBAzinabnachkalk(wcoAZInABNachkalkulation.getShort());
		
		artikelDto.setBMultiplikatorAufrunden(wcoMultiplikatorAufrunden.getShort());
		artikelDto.setBMultiplikatorInvers(wcoMultiplikatorEinsDurch.getShort());
		
		artikelDto.setBMeldepflichtig(wcoMeldepflichtig.getShort());
		artikelDto.setBBewilligungspflichtig(wcoBewilligungspflichtig.getShort());
		

		artikelDto.setFMultiplikatorZugehoerigerartikel(wnfZugehoerigerArtikelMultiplikator.getDouble());
		artikelDto.setIWartungsintervall(wnfWartungsintervall.getInteger());
		artikelDto.setTLetztewartung(wdfLetzteWartung.getTimestamp());
		artikelDto.setISofortverbrauch(wnfSofortverbrauch.getInteger());
		artikelDto.setNMindestverkaufsmenge(wnfMindestverkaufsmenge.getBigDecimal());
		artikelDto.setNVerpackungsmittelmenge(wnfVerpackungsmittelMenge.getBigDecimal());
		artikelDto.setVerpackungsmittelIId(wsfVerpackungsmittel.getIKey());

		String sWVN = wtfWarenverkehrsnummer.getFormattedText();
		if (sWVN != null && !sWVN.equals("")) {
			// MB: Format pruefen.
			if (Helper.checkWarenverkehrsnummer(wtfWarenverkehrsnummer.getFormattedText().trim())) {
				WarenverkehrsnummerDto dto = DelegateFactory.getInstance().getFinanzServiceDelegate()
						.warenverkehrsnummerFindByPrimaryKeyOhneExc(wtfWarenverkehrsnummer.getFormattedText().trim());
				if (dto == null) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
							LPMain.getTextRespectUISPr("artikel.error.warenverkehrsnummer"));
				}

				artikelDto.setCWarenverkehrsnummer(wtfWarenverkehrsnummer.getFormattedText());

			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
						LPMain.getTextRespectUISPr("artikel.error.warenverkehrsnummer.format"));
			}
		} else {
			artikelDto.setCWarenverkehrsnummer(null);
		}

		artikelDto.setCVerkaufseannr(wtfVerkaufsean.getText());
		artikelDto.setCVerpackungseannr(wtfVerpackungsean.getText());

		artikelDto.setCEccn(wtfEccn.getText());

		artikelDto.setSollverkaufDto(new SollverkaufDto());
		artikelDto.getSollverkaufDto().setFAufschlag(wnfAufschlag.getDouble());
		artikelDto.getSollverkaufDto().setFSollverkauf(wnfSoll.getDouble());
		artikelDto.setFVerpackungsmenge(wnfVerpackungsmenge.getDouble());
		artikelDto.setNPreisZugehoerigerartikel(wnfPreisZugehoerigerartikel.getBigDecimal());

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);

		wbuZugehoerigerArtikel.setVisible(true);
		wtfZugehoerigerArtikel.setVisible(true);
		wnfZugehoerigerArtikelMultiplikator.setVisible(true);

		leereAlleFelder(this);
		artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(((InternalFrameArtikel) getInternalFrame()).getArtikelDto().getIId());

		if (Helper.short2boolean(artikelDto.getBKalkulatorisch())) {
			wbuZugehoerigerArtikel.setVisible(false);
			wtfZugehoerigerArtikel.setVisible(false);
			wnfZugehoerigerArtikelMultiplikator.setVisible(false);

		}

		dto2Components();
		wcoSeriennummer_actionPerformed(null);
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		wlaMaxvertreterprovision
				.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.maxvertreterprovision"));
		wlaGarantiezeit.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.garantiezeit"));
		wla2.setHorizontalAlignment(SwingConstants.LEFT);
		wla2.setText("%");

		// PJ20380
		Map m = new LinkedHashMap();
		wcoSnrChnr.setMandatoryField(true);
		m.put(ArtikelFac.SNRCHNR_OHNE, LPMain.getTextRespectUISPr("artikel.snrchnr.ohne"));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_SERIENNUMMERN)) {

			m.put(ArtikelFac.SNRCHNR_SNRBEHAFTET, LPMain.getTextRespectUISPr("artikel.snrchnr.snr"));
		}

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_CHARGENNUMMERN)) {

			m.put(ArtikelFac.SNRCHNR_CHNRBEHAFTET, LPMain.getTextRespectUISPr("artikel.snrchnr.chnr"));
		}

		wcoSnrChnr.setMap(m, false);

		wnfLaengeSnrMin.setFractionDigits(0);
		wnfLaengeSnrMax.setFractionDigits(0);

		wnfLaengeSnrMin.setMinimumValue(0);
		wnfLaengeSnrMax.setMinimumValue(0);

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getInstance().getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
				ParameterFac.PARAMETER_ARTIKEL_MINDESTLAENGE_SERIENNUMMER);

		Integer iMinlaenge = (Integer) parameter.getCWertAsObject();

		wnfLaengeSnrMin
				.setToolTipText(LPMain.getMessageTextRespectUISPr("artikel.snrchnr.min.tooltip", iMinlaenge + ""));

		parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getInstance().getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
				ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_SERIENNUMMER);

		Integer iMaxlaenge = (Integer) parameter.getCWertAsObject();
		wnfLaengeSnrMax
				.setToolTipText(LPMain.getMessageTextRespectUISPr("artikel.snrchnr.max.tooltip", iMaxlaenge + ""));

		wlaInMonaten.setHorizontalAlignment(SwingConstants.LEFT);
		wlaMinutenfaktor1Einheit.setHorizontalAlignment(SwingConstants.LEFT);
		wlaMinutenfaktor2Einheit.setHorizontalAlignment(SwingConstants.LEFT);

		wlaPreisZugehoerigerartikel.setText(LPMain.getInstance().getTextRespectUISPr("artikel.preiszugehoerigerartikel"));
		wnfPreisZugehoerigerartikel.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		
		wlaInMonaten.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.inmonaten"));
		wlaMinutenfaktor1.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.minutenfaktor1"));
		wlaMinutenfaktor2.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.minutenfaktor2"));
		wlaMinutenfaktor1Einheit
				.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.minutenfaktor1einheit"));
		wlaMinutenfaktor2Einheit
				.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.minutenfaktor2einheit"));

		wlaVerpackungsmittelMenge.setText(LPMain.getInstance().getTextRespectUISPr("artikel.verpackungsmittelmenge"));

		wnfVerpackungsmittelMenge.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());

		wlaMindestverkaufsmenge.setText(LPMain.getInstance().getTextRespectUISPr("artikel.mindestverkaufsmenge"));
		wnfMindestverkaufsmenge.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());

		wlaVerpackungsmenge.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.verpackungsmenge"));
		wlaMindestdeckungsbeitrag
				.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.mindestdeckungsbeitrag"));
		wlaLetzteWartung.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.letztewartung"));
		wla8.setHorizontalAlignment(SwingConstants.LEFT);

		wlaArtikeleinheit.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitVerpackungsmenge.setHorizontalAlignment(SwingConstants.LEFT);

		wcbVKPreispflichtig.setText(LPMain.getTextRespectUISPr("artikel.vkpreisflichtig"));
		
		wla8.setText("%");
		wlaAufschlag.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.aufschlag"));
		wlaSoll.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.soll"));
		wlaEccn.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.eccn"));
		wlaVerpackungsean.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.verpackungsean"));
		wlaWartungsintervall.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.wartungsintervall"));
		wlaSofortverbrauch.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.sofortverbrauch"));
		wnfWartungsintervall.setFractionDigits(0);
		wnfSofortverbrauch.setFractionDigits(0);

		wtfVerkaufsean.setColumnsMax(ArtikelFac.MAX_ARTIKEL_VERKAUFEANNR);
		wtfEccn.setColumnsMax(ArtikelFac.MAX_ARTIKEL_ECCN);
		wtfVerpackungsean.setColumnsMax(ArtikelFac.MAX_ARTIKEL_VERKAUFEANNR);
		wtfVerkaufsean.setText("");
		wbuWarenverkehrsnummer.setToolTipText("");
		wbuWarenverkehrsnummer
				.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.warenverkehrsnummer") + "...");
		wtfWarenverkehrsnummer.setColumnsMax(ArtikelFac.MAX_ARTIKEL_WARENVERKEHRSNUMMER);
		wtfWarenverkehrsnummer.setFormat(ArtikelFac.PATTERN_WARENVERKEHRSNUMMER);
		wtfWarenverkehrsnummer.removeContent();
		wcoSnrChnr.addActionListener(new PanelArtikelsonstiges_wrapperCheckBoxSeriennummer_actionAdapter(this));

		wcoRabattierbar.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.rabattierbar"));

		wcoDokumentenpflicht.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.dokumentenpflicht"));

		wcoRahmenartikel.setText(LPMain.getInstance().getTextRespectUISPr("artikel.rahmenartikel"));
		wcoKommissionieren.setText(LPMain.getInstance().getTextRespectUISPr("artikel.kommissionieren"));
		wcoKeineLagerzubuchung.setText(LPMain.getInstance().getTextRespectUISPr("artikel.keinelagerzubuchung"));
		wcoAZInABNachkalkulation
				.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.azinabnachkalkulation"));

		wcoWerbeabgabepflichtig.setText(LPMain.getInstance().getTextRespectUISPr("artikel.werbeabgabepflichtig"));

		wcoLagerbewirtschaftet
				.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.lagerbewirtschaftet"));
		wcoLagerbewertet.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.lagerbewertet"));
		wcoVerleih.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.verleih"));
		wcoVerleih.addActionListener(new PanelArtikelsonstiges_wrapperCheckBoxVerleih_actionAdapter(this));
		wbuZugehoerigerArtikel
				.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.zugehoerigerartikel") + "...");
		wbuZugehoerigerArtikel.setActionCommand(PanelArtikelsonstiges.ACTION_SPECIAL_ARTIKEL_FROM_LISTE);
		wbuZugehoerigerArtikel.addActionListener(this);

		wnfZugehoerigerArtikelMultiplikator
				.setToolTipText(LPMain.getInstance().getTextRespectUISPr("artikel.zugehoerigerartikel.multiplikator"));

		wcoMultiplikatorEinsDurch.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.multiplikator.einsdurch"));
		wcoMultiplikatorAufrunden.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.multiplikator.aufrunden"));

		wbuWarenverkehrsnummer.setActionCommand(PanelArtikelsonstiges.ACTION_SPECIAL_WARENVERKEHRSNUMMER_FROM_LISTE);
		wbuWarenverkehrsnummer.addActionListener(this);

		wcoMeldepflichtig.setText(LPMain.getInstance().getTextRespectUISPr("artikel.meldepflichtig"));
		wcoBewilligungspflichtig.setText(LPMain.getInstance().getTextRespectUISPr("artikel.bewilligungspflichtig"));
		
		
		wbuUrsprungsland.setText(LPMain.getInstance().getTextRespectUISPr("artikel.ursprungsland") + "...");
		wbuUrsprungsland.setActionCommand(ACTION_SPECIAL_LAND_FROM_LISTE);
		wbuUrsprungsland.addActionListener(this);

		wbuErsatzArtikel.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.ersatzartikel") + "...");

		wbuErsatzArtikel.setActionCommand(PanelArtikelsonstiges.ACTION_SPECIAL_ARTIKELERSATZ_FROM_LISTE);
		wbuErsatzArtikel.addActionListener(this);
		wtfErsatzArtikel.setActivatable(false);
		wtfErsatzArtikel.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfZugehoerigerArtikel.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfZugehoerigerArtikel.setText("");
		wtfZugehoerigerArtikel.setActivatable(false);

		wlaZBez2.setRequestFocusEnabled(true);
		wlaZBez2.setText(LPMain.getInstance().getTextRespectUISPr("lp.zusatzbezeichnung") + " 2");
		wlaZbez.setText(LPMain.getInstance().getTextRespectUISPr("lp.zusatzbezeichnung"));
		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));
		wlaArtikelnummer.setText(LPMain.getInstance().getTextRespectUISPr("artikel.artikelnummer"));
		wftBezeichnungStd.setActivatable(false);
		wftBezeichnungStd.setText("");
		wtfZBezStd.setActivatable(false);
		wtfZBezStd.setText("");
		wtfZBez2Std.setActivatable(false);
		wtfZBez2Std.setText("");
		wtfArtikelnummer.setBackground(Color.white);
		wtfArtikelnummer.setActivatable(false);
		wtfArtikelnummer.setText("");
		wnfGarantiezeit.setFractionDigits(0);
		wnfAufschlag.setMinimumValue(new BigDecimal(0));
		wnfSoll.setMinimumValue(new BigDecimal(0));
		wnfMindestdeckungsbeitrag.setMinimumValue(new BigDecimal(0));
		wnfMindestdeckungsbeitrag.setMandatoryField(true);
		wnfMaxvertreterprovision.setMinimumValue(new BigDecimal(0));

		int iLaengenBezeichung = DelegateFactory.getInstance().getArtikelDelegate().getLaengeArtikelBezeichnungen();
		wftBezeichnungStd.setColumnsMax(iLaengenBezeichung);
		wtfZBezStd.setColumnsMax(iLaengenBezeichung);
		wtfZBez2Std.setColumnsMax(iLaengenBezeichung);
		
		wtfUrsprungsland.setActivatable(false);

		wla1.setRequestFocusEnabled(true);
		wla1.setHorizontalAlignment(SwingConstants.LEFT);
		wla1.setText("%");
		wla3.setRequestFocusEnabled(true);
		wla3.setHorizontalAlignment(SwingConstants.LEFT);
		wla3.setText("%");
		jbInitVerkaufseanPanel();

		jpaWorkingOn = new JPanel(
				new MigLayout("ins 0, wrap 6 ", "[fill, 25%|fill,20%|fill,5%|fill, 20%|fill, 10%|fill, 15%]","[]2[]"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaArtikelnummer);
		jpaWorkingOn.add(wtfArtikelnummer, " wrap");

		jpaWorkingOn.add(wlaBezeichnung);
		jpaWorkingOn.add(wftBezeichnungStd, "span");

		jpaWorkingOn.add(wlaZbez);
		jpaWorkingOn.add(wtfZBezStd, "span");

		jpaWorkingOn.add(wlaZBez2);
		jpaWorkingOn.add(wtfZBez2Std, "span");

		jpaWorkingOn.add(wlaMaxvertreterprovision);
		jpaWorkingOn.add(wnfMaxvertreterprovision);
		jpaWorkingOn.add(wla2);
		jpaWorkingOn.add(wlaGarantiezeit);
		jpaWorkingOn.add(wnfGarantiezeit);
		jpaWorkingOn.add(wlaInMonaten, "wrap");

		jpaWorkingOn.add(wlaMindestdeckungsbeitrag);
		jpaWorkingOn.add(wnfMindestdeckungsbeitrag);
		jpaWorkingOn.add(wla8);
		jpaWorkingOn.add(wlaSofortverbrauch);
		jpaWorkingOn.add(wnfSofortverbrauch);
		WrapperLabel wlaInTagen = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.sofortverbrauch.intagen"));
		wlaInTagen.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaInTagen, "wrap");

		jpaWorkingOn.add(wlaLetzteWartung);
		jpaWorkingOn.add(wdfLetzteWartung);

		jpaWorkingOn.add(wlaWartungsintervall, "span 2");
		jpaWorkingOn.add(wnfWartungsintervall);
		WrapperLabel wlaWartungsintervallEinheit = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.wartungsintervall.inmonaten"));
		wlaWartungsintervallEinheit.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaWartungsintervallEinheit, "wrap");

		jpaWorkingOn.add(wlaAufschlag);
		jpaWorkingOn.add(wnfAufschlag);
		jpaWorkingOn.add(wla1);
		jpaWorkingOn.add(wlaMinutenfaktor1);
		jpaWorkingOn.add(wrapperNumberFieldMinutenfaktor1);
		jpaWorkingOn.add(wlaMinutenfaktor1Einheit, "wrap");

		jpaWorkingOn.add(wlaSoll);
		jpaWorkingOn.add(wnfSoll);
		jpaWorkingOn.add(wla3);
		jpaWorkingOn.add(wlaMinutenfaktor2);
		jpaWorkingOn.add(wnfMinutenfaktor2);
		jpaWorkingOn.add(wlaMinutenfaktor2Einheit, "wrap");

		jpaWorkingOn.add(wsfVerpackungsmittel.getWrapperButton());
		jpaWorkingOn.add(wsfVerpackungsmittel.getWrapperTextField());

		jpaWorkingOn.add(wlaVerpackungsmittelMenge, "skip");
		jpaWorkingOn.add(wnfVerpackungsmittelMenge);
		jpaWorkingOn.add(wlaArtikeleinheit, "wrap");

		jpaWorkingOn.add(panelVerkaufsean);
		jpaWorkingOn.add(wtfVerkaufsean);
		jpaWorkingOn.add(wlaVerpackungsmenge, "skip");
		jpaWorkingOn.add(wnfVerpackungsmenge);
		jpaWorkingOn.add(wlaEinheitVerpackungsmenge, "wrap");

		jpaWorkingOn.add(wlaVerpackungsean, "skip 3");
		jpaWorkingOn.add(wtfVerpackungsean, "wrap");

		jpaWorkingOn.add(wlaEccn);
		jpaWorkingOn.add(wtfEccn);
		jpaWorkingOn.add(wlaMindestverkaufsmenge, "skip");
		jpaWorkingOn.add(wnfMindestverkaufsmenge, "wrap");

		jpaWorkingOn.add(wbuWarenverkehrsnummer);
		jpaWorkingOn.add(wtfWarenverkehrsnummer);
		jpaWorkingOn.add(wbuUrsprungsland, "skip");

		// PJ19598
		if (internalFrameArtikel.getTabbedPaneArtikel().isUrsprungslandIstPflichtfeld()) {
			wtfUrsprungsland.setMandatoryField(true);
		}

		jpaWorkingOn.add(wtfUrsprungsland, "wrap");

		jpaWorkingOn.add(wcoSnrChnr);

		jpaWorkingOn.add(wnfLaengeSnrMin, "split 3");
		jpaWorkingOn.add(new WrapperLabel("-"));
		jpaWorkingOn.add(wnfLaengeSnrMax);
		jpaWorkingOn.add(new WrapperLabel());
		jpaWorkingOn.add(wcoRahmenartikel);
		jpaWorkingOn.add(wcoKommissionieren, "span");

		jpaWorkingOn.add(wcoLagerbewirtschaftet, "newline");
		jpaWorkingOn.add(wcoLagerbewertet, "span 2");

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_VERLEIH)) {

			jpaWorkingOn.add(wcoVerleih);
			jpaWorkingOn.add(wcoWerbeabgabepflichtig,"span 2");
		} else {
			jpaWorkingOn.add(wcoAZInABNachkalkulation);
			jpaWorkingOn.add(wcoWerbeabgabepflichtig,"span 2");
		}

		jpaWorkingOn.add(wcoRabattierbar, "newline");
		jpaWorkingOn.add(wcoDokumentenpflicht, "span 2");
		jpaWorkingOn.add(wcbVKPreispflichtig);
		jpaWorkingOn.add(wcoKeineLagerzubuchung, "span");

		
		
		jpaWorkingOn.add(wcoMeldepflichtig);
		jpaWorkingOn.add(wcoBewilligungspflichtig);
		
		if (!LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_DUAL_USE)) {
			wcoMeldepflichtig.setVisible(false);
			wcoBewilligungspflichtig.setVisible(false);
		}
		
		jpaWorkingOn.add(wbuErsatzArtikel, "skip");
		jpaWorkingOn.add(wtfErsatzArtikel, "span");

		
		jpaWorkingOn.add(wbuZugehoerigerArtikel, "newline");

		jpaWorkingOn.add(wtfZugehoerigerArtikel);
		jpaWorkingOn.add(wnfZugehoerigerArtikelMultiplikator);
		jpaWorkingOn.add(wcoMultiplikatorEinsDurch);
		jpaWorkingOn.add(wcoMultiplikatorAufrunden,"span");
		
		jpaWorkingOn.add(wlaPreisZugehoerigerartikel, "newline");
		jpaWorkingOn.add(wnfPreisZugehoerigerartikel);
		jpaWorkingOn.add(new JLabel(LPMain.getInstance().getTheClient().getSMandantenwaehrung()));
		
		
		
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DISCARD, ACTION_PREVIOUS, ACTION_NEXT, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	private void jbInitVerkaufseanPanel() throws Throwable {
		wlaVerkaufsean.setText(LPMain.getInstance().getTextRespectUISPr("artikel.sonstiges.verkaufsean"));

		HelperClient.setMinimumAndPreferredSize(wlaVerkaufsean, HelperClient.getSizeFactoredDimension(120));

		wbuGeneriereVerkaufsean.setText("G");
		wbuGeneriereVerkaufsean.setToolTipText(LPMain.getTextRespectUISPr("artikel.generiereverkaufsean"));
		wbuGeneriereVerkaufsean.setPreferredSize(new Dimension(40, 23));
		wbuGeneriereVerkaufsean.setMinimumSize(new Dimension(40, 23));
		wbuGeneriereVerkaufsean.setEnabled(enableVerkaufseanButton());
		wbuGeneriereVerkaufsean.setActivatable(enableVerkaufseanButton());
		wbuGeneriereVerkaufsean.setActionCommand(ACTION_SPECIAL_GENERIERE_VERKAUFSEAN);
		wbuGeneriereVerkaufsean.addActionListener(this);

		HvLayout layoutVerkausean = HvLayoutFactory.create(panelVerkaufsean, "ins 0", "push[][]", "");
		layoutVerkausean.add(wlaVerkaufsean, "al right").add(wbuGeneriereVerkaufsean, "al right");
	}

	private boolean enableVerkaufseanButton() throws Throwable {
		String basisnummerGTIN = DelegateFactory.getInstance().getParameterDelegate().getGS1BasisnummerGTIN();
		return !Helper.isStringEmpty(basisnummerGTIN);
	}

	protected void setDefaults() {
		wcoLagerbewirtschaftet.setSelected(true);
		wcoLagerbewertet.setSelected(true);

		if (getInternalFrame().bRechtDarfPreiseSehenEinkauf && getInternalFrame().bRechtDarfPreiseSehenVerkauf) {

		} else {
			// SP5756
			wlaMindestdeckungsbeitrag.setVisible(false);
			wnfMindestdeckungsbeitrag.setVisible(false);
			wla8.setVisible(false);
		}

	}

	void wcoSeriennummer_actionPerformed(ActionEvent e) {
		wnfLaengeSnrMin.setVisible(false);
		wnfLaengeSnrMax.setVisible(false);

		if (wcoSnrChnr.getKeyOfSelectedItem().equals(ArtikelFac.SNRCHNR_SNRBEHAFTET)) {

			wcoLagerbewirtschaftet.setSelected(true);
			wnfLaengeSnrMin.setVisible(true);
			wnfLaengeSnrMax.setVisible(true);
		} else if (wcoSnrChnr.getKeyOfSelectedItem().equals(ArtikelFac.SNRCHNR_CHNRBEHAFTET)) {

			wcoLagerbewirtschaftet.setSelected(true);
		}
	}

	void wcoChargennummer_actionPerformed(ActionEvent e) {

	}

	void wcoVerleih_actionPerformed(ActionEvent e) {
		if (wcoVerleih.isSelected()) {
			wcoLagerbewirtschaftet.setSelected(true);
		}

	}

}

class PanelArtikelsonstiges_wrapperCheckBoxSeriennummer_actionAdapter implements java.awt.event.ActionListener {
	PanelArtikelsonstiges adaptee;

	PanelArtikelsonstiges_wrapperCheckBoxSeriennummer_actionAdapter(PanelArtikelsonstiges adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoSeriennummer_actionPerformed(e);
	}
}

class PanelArtikelsonstiges_wrapperCheckBoxVerleih_actionAdapter implements java.awt.event.ActionListener {
	PanelArtikelsonstiges adaptee;

	PanelArtikelsonstiges_wrapperCheckBoxVerleih_actionAdapter(PanelArtikelsonstiges adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoVerleih_actionPerformed(e);
	}
}
