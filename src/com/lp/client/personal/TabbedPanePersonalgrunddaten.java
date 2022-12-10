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
package com.lp.client.personal;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>
 * &UUml;berschrift:
 * </p>
 * <p>
 * Beschreibung:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Organisation:
 * </p>
 * 
 * @author Christian Kollmann
 * @version $Revision: 1.6 $
 */
public class TabbedPanePersonalgrunddaten extends TabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryBeruf = null;
	private PanelBasis panelSplitBeruf = null;
	private PanelBasis panelBottomBeruf = null;

	private PanelQuery panelQueryPendlerpauschale = null;
	private PanelBasis panelSplitPendlerpauschale = null;
	private PanelBasis panelBottomPendlerpauschale = null;

	private PanelQuery panelQueryLohngruppe = null;
	private PanelBasis panelSplitLohngruppe = null;
	private PanelBasis panelBottomLohngruppe = null;

	private PanelQuery panelQueryReligion = null;
	private PanelBasis panelSplitReligion = null;
	private PanelBasis panelBottomReligion = null;

	private PanelQuery panelQueryTagesart = null;
	private PanelBasis panelSplitTagesart = null;
	private PanelBasis panelBottomTagesart = null;

	private PanelQuery panelQueryZulage = null;
	private PanelBasis panelSplitZulage = null;
	private PanelBasis panelBottomZulage = null;

	private PanelQuery panelQuerySchichtzuschlag = null;
	private PanelBasis panelSplitSchichtzuschlag = null;
	private PanelBasis panelBottomSchichtzuschlag = null;

	private PanelQuery panelQueryIdentifikation = null;
	private PanelBasis panelSplitIdentifikation = null;
	private PanelBasis panelBottomIdentifikation = null;

	private PanelQuery panelQueryDSGVOKategorie = null;
	private PanelBasis panelSplitDSGVOKategorie = null;
	private PanelBasis panelBottomDSGVOKategorie = null;

	private PanelQuery panelQueryDSGVOText = null;
	private PanelBasis panelSplitDSGVOText = null;
	private PanelBasis panelBottomDSGVOText = null;

	private PanelQuery panelQueryAbwesenheitsart = null;
	private PanelBasis panelSplitAbwesenheitsart = null;
	private PanelBasis panelBottomAbwesenheitsart = null;

	private PanelQuery panelQueryLohnart = null;
	private PanelBasis panelSplitLohnart = null;
	private PanelBasis panelBottomLohnart = null;

	private PanelQuery panelQueryLohnartStundenfaktor = null;
	private PanelBasis panelSplitLohnartStundenfaktor = null;
	private PanelBasis panelBottomLohnartStundenfaktor = null;

	private PanelQuery panelQueryZahltag = null;
	private PanelBasis panelSplitZahltag = null;
	private PanelBasis panelBottomZahltag = null;

	private final static int IDX_PANEL_BERUF = 0;
	private final static int IDX_PANEL_PENDLERPAUSCHALE = 1;
	private final static int IDX_PANEL_LOHNGRUPPE = 2;
	private final static int IDX_PANEL_RELIGION = 3;
	private final static int IDX_PANEL_TAGESART = 4;
	private final static int IDX_PANEL_ZULAGE = 5;
	private final static int IDX_PANEL_LOHNART = 6;
	private final static int IDX_PANEL_LOHNARTSTUNDENFAKTOR = 7;
	private final static int IDX_PANEL_ZAHLTAG = 8;
	private final static int IDX_PANEL_SCHICHTZUSCHLAG = 9;
	private final static int IDX_PANEL_ABWESENHEITSART = 10;
	private final static int IDX_PANEL_IDENTIFIKATION = 11;
	private final static int IDX_PANEL_DSGVO_KATEGORIE = 12;
	private final static int IDX_PANEL_DSGVO_TEXT = 13;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPanePersonalgrunddaten(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr("pers.title.tab.grunddaten"));

		jbInit();
		initComponents();
	}

	public InternalFramePersonal getInternalFramePersonal() {
		return (InternalFramePersonal) getInternalFrame();
	}

	private void createZulage() throws Throwable {
		if (panelSplitZulage == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryZulage = new PanelQuery(null, null, QueryParameters.UC_ID_ZULAGE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("pers.zulage"), true);
			panelQueryZulage
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

			panelBottomZulage = new PanelZulage(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.zulage"), null);
			panelSplitZulage = new PanelSplit(getInternalFrame(), panelBottomZulage, panelQueryZulage, 400);

			setComponentAt(IDX_PANEL_ZULAGE, panelSplitZulage);
		}
	}

	private void createSchichtzuschlag() throws Throwable {
		if (panelSplitSchichtzuschlag == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQuerySchichtzuschlag = new PanelQuery(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_SCHICHTZUSCHLAG, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.schichtzuschlag"), true);
			panelQuerySchichtzuschlag
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

			panelBottomSchichtzuschlag = new PanelSchichtzuschlag(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.schichtzuschlag"), null);
			panelSplitSchichtzuschlag = new PanelSplit(getInternalFrame(), panelBottomSchichtzuschlag,
					panelQuerySchichtzuschlag, 370);

			setComponentAt(IDX_PANEL_SCHICHTZUSCHLAG, panelSplitSchichtzuschlag);
		}
	}

	private void createLohnart() throws Throwable {
		if (panelSplitLohnart == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryLohnart = new PanelQuery(null, null, QueryParameters.UC_ID_LOHNART, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("pers.zulage"), true);
			panelQueryLohnart
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

			panelBottomLohnart = new PanelLohnart(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.lohnart"), null);
			panelSplitLohnart = new PanelSplit(getInternalFrame(), panelBottomLohnart, panelQueryLohnart, 300);

			setComponentAt(IDX_PANEL_LOHNART, panelSplitLohnart);
		}
	}

	private void createZahltag() throws Throwable {
		if (panelSplitZahltag == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryZahltag = new PanelQuery(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_ZAHLTAG, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.zahltag"), true);

			panelBottomZahltag = new PanelZahltag(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.zahltag"), null);
			panelSplitZahltag = new PanelSplit(getInternalFrame(), panelBottomZahltag, panelQueryZahltag, 300);

			setComponentAt(IDX_PANEL_ZAHLTAG, panelSplitZahltag);
		}
	}

	private void createLohnartStundenfaktor() throws Throwable {
		if (panelSplitLohnartStundenfaktor == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryLohnartStundenfaktor = new PanelQuery(null, null, QueryParameters.UC_ID_LOHNARTSTUNDENFAKTOR,
					aWhichButtonIUse, getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("pers.zulage"),
					true);
			panelQueryLohnartStundenfaktor
					.befuellePanelFilterkriterienDirekt(PersonalFilterFactory.getInstance().createFKDLohnart(), null);

			panelBottomLohnartStundenfaktor = new PanelLohnartstundenfaktor(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.lohnartstunden"), null);
			panelSplitLohnartStundenfaktor = new PanelSplit(getInternalFrame(), panelBottomLohnartStundenfaktor,
					panelQueryLohnartStundenfaktor, 300);

			setComponentAt(IDX_PANEL_LOHNARTSTUNDENFAKTOR, panelSplitLohnartStundenfaktor);
		}
	}

	private void createIdentifikation() throws Throwable {
		if (panelSplitIdentifikation == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryIdentifikation = new PanelQuery(null, null, QueryParameters.UC_ID_IDENTIFIKATION,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.identifikation"), true);
			panelQueryIdentifikation
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDKennung(), null);

			panelBottomIdentifikation = new PanelIdentifikation(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.identifikation"), null);
			panelSplitIdentifikation = new PanelSplit(getInternalFrame(), panelBottomIdentifikation,
					panelQueryIdentifikation, 280);

			setComponentAt(IDX_PANEL_IDENTIFIKATION, panelSplitIdentifikation);
		}
	}

	private void createAbwesenheitsart() throws Throwable {
		if (panelSplitAbwesenheitsart == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			panelQueryAbwesenheitsart = new PanelQuery(null, null, QueryParameters.UC_ID_ABWESENHEITSART,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.dsgvo.abwesenheitsart"), true);

			panelBottomAbwesenheitsart = new PanelAbwesenheitsart(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.dsgvo.abwesenheitsart"), null);
			panelSplitAbwesenheitsart = new PanelSplit(getInternalFrame(), panelBottomAbwesenheitsart,
					panelQueryAbwesenheitsart, 320);

			setComponentAt(IDX_PANEL_ABWESENHEITSART, panelSplitAbwesenheitsart);
		}
	}

	private void createDSGVOText() throws Throwable {
		if (panelSplitDSGVOText == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			panelQueryDSGVOText = new PanelQuery(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_DSGVOTEXT, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("ers.dsgvo.kopffusstext"), true);

			panelBottomDSGVOText = new PanelDSGVOText(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("ers.dsgvo.kopffusstext"), null);
			panelSplitDSGVOText = new PanelSplit(getInternalFrame(), panelBottomDSGVOText, panelQueryDSGVOText, 280);

			setComponentAt(IDX_PANEL_DSGVO_TEXT, panelSplitDSGVOText);
		}
	}

	private void createDSGVOKategorie() throws Throwable {
		if (panelSplitDSGVOKategorie == null) {
			String[] aWhichButtonIUse = {};

			panelQueryDSGVOKategorie = new PanelQuery(null, null, QueryParameters.UC_ID_DSGVOKATEGORIE,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.dsgvo.kategorie"), true);
			panelQueryDSGVOKategorie
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDKennung(), null);

			panelBottomDSGVOKategorie = new PanelDSGVOKategorie(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.dsgvo.kategorie"), null);
			panelSplitDSGVOKategorie = new PanelSplit(getInternalFrame(), panelBottomDSGVOKategorie,
					panelQueryDSGVOKategorie, 280);

			setComponentAt(IDX_PANEL_DSGVO_KATEGORIE, panelSplitDSGVOKategorie);
		}
	}

	private void createTagesart() throws Throwable {
		if (panelSplitTagesart == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryTagesart = new PanelQuery(null,
					PersonalFilterFactory.getInstance().createFKZusaetzlicheTagesarten(),
					QueryParameters.UC_ID_TAGESART, aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.title.tab.tagesart"), true);

			panelQueryTagesart.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDKennung(),
					SystemFilterFactory.getInstance()
							.createFKDSprTabelleBezeichnung(PersonalFac.FLR_TAGESART_TAGESARTSPRSET));

			panelBottomTagesart = new PanelTagesart(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.title.tab.tagesart"), null);

			panelSplitTagesart = new PanelSplit(getInternalFrame(), panelBottomTagesart, panelQueryTagesart, 380);

			setComponentAt(IDX_PANEL_TAGESART, panelSplitTagesart);
		}
	}

	private void createReligion() throws Throwable {
		if (panelSplitReligion == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryReligion = new PanelQuery(null, null, QueryParameters.UC_ID_RELIGION, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("pers.title.tab.religion"), true);

			panelQueryReligion.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDKennung(),
					SystemFilterFactory.getInstance()
							.createFKDSprTabelleBezeichnung(PersonalFac.FLR_RELIGION_RELIGIONSPRSET));

			panelBottomReligion = new PanelReligion(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.title.tab.religion"), null);

			panelSplitReligion = new PanelSplit(getInternalFrame(), panelBottomReligion, panelQueryReligion, 400);

			setComponentAt(IDX_PANEL_RELIGION, panelSplitReligion);
		}
	}

	private void createBeruf() throws Throwable {
		if (panelSplitBeruf == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryBeruf = new PanelQuery(null, null, QueryParameters.UC_ID_BERUF, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("pers.title.tab.beruf"), true);
			panelQueryBeruf.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			panelBottomBeruf = new PanelBeruf(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.title.tab.beruf"), null);

			panelSplitBeruf = new PanelSplit(getInternalFrame(), panelBottomBeruf, panelQueryBeruf, 400);

			setComponentAt(IDX_PANEL_BERUF, panelSplitBeruf);
		}
	}

	private void createLohngruppe() throws Throwable {
		if (panelSplitLohngruppe == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryLohngruppe = new PanelQuery(null, null, QueryParameters.UC_ID_LOHNGRUPPE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("pers.title.tab.lohngruppe"), true);
			panelQueryLohngruppe
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

			panelBottomLohngruppe = new PanelLohngruppe(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.title.tab.lohngruppe"), null);

			panelSplitLohngruppe = new PanelSplit(getInternalFrame(), panelBottomLohngruppe, panelQueryLohngruppe, 400);

			setComponentAt(IDX_PANEL_LOHNGRUPPE, panelSplitLohngruppe);
		}
	}

	private void createPendlerpauschale() throws Throwable {
		if (panelSplitPendlerpauschale == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			panelQueryPendlerpauschale = new PanelQuery(null, null, QueryParameters.UC_ID_PENDLERPAUSCHALE,
					aWhichButtonIUse, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.title.tab.pendlerpauschale"), true);
			panelQueryPendlerpauschale
					.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);

			panelBottomPendlerpauschale = new PanelPendlerpauschale(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("pers.title.tab.pendlerpauschale"), null);

			panelSplitPendlerpauschale = new PanelSplit(getInternalFrame(), panelBottomPendlerpauschale,
					panelQueryPendlerpauschale, 400);

			setComponentAt(IDX_PANEL_PENDLERPAUSCHALE, panelSplitPendlerpauschale);
		}
	}

	private void jbInit() throws Throwable {
		// Kollektiv

		// 1 tab oben: QP1 PartnerFLR; lazy loading

		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.title.tab.beruf"), null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.title.tab.beruf"), IDX_PANEL_BERUF);
		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.title.tab.pendlerpauschale"), null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.title.tab.pendlerpauschale"),
				IDX_PANEL_PENDLERPAUSCHALE);
		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.title.tab.lohngruppe"), null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.title.tab.lohngruppe"), IDX_PANEL_LOHNGRUPPE);
		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.title.tab.religion"), null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.title.tab.religion"), IDX_PANEL_RELIGION);

		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.title.tab.tagesart"), null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.title.tab.tagesart"), IDX_PANEL_TAGESART);

		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.zulage"), null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.zulage"), IDX_PANEL_ZULAGE);

		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.lohnart"), null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.lohnart"), IDX_PANEL_LOHNART);
		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.lohnartstunden"), null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.lohnartstunden"), IDX_PANEL_LOHNARTSTUNDENFAKTOR);
		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.zahltag"), null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.zahltag"), IDX_PANEL_ZAHLTAG);
		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.schichtzuschlag"), null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.schichtzuschlag"), IDX_PANEL_SCHICHTZUSCHLAG);
		//SP6867
		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.dsgvo.abwesenheitsart"), null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.dsgvo.abwesenheitsart"), IDX_PANEL_ABWESENHEITSART);
		if (LPMain.getInstance().getDesktop().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_DSGVO)) {
			insertTab(LPMain.getInstance().getTextRespectUISPr("pers.identifikation"), null, null,
					LPMain.getInstance().getTextRespectUISPr("pers.identifikation"), IDX_PANEL_IDENTIFIKATION);
			insertTab(LPMain.getInstance().getTextRespectUISPr("pers.dsgvo.kategorie"), null, null,
					LPMain.getInstance().getTextRespectUISPr("pers.dsgvo.kategorie"), IDX_PANEL_DSGVO_KATEGORIE);
			insertTab(LPMain.getInstance().getTextRespectUISPr("pers.dsgvo.kopffusstext"), null, null,
					LPMain.getInstance().getTextRespectUISPr("pers.dsgvo.kopffusstext"), IDX_PANEL_DSGVO_TEXT);
		}

		createBeruf();

		// Itemevents an MEIN Detailpanel senden kann.
		this.addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryBeruf) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomBeruf.setKeyWhenDetailPanel(key);
				panelBottomBeruf.eventYouAreSelected(false);
				panelQueryBeruf.updateButtons();

			} else if (e.getSource() == panelQueryLohngruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomLohngruppe.setKeyWhenDetailPanel(key);
				panelBottomLohngruppe.eventYouAreSelected(false);
				panelQueryLohngruppe.updateButtons();

			} else if (e.getSource() == panelQueryPendlerpauschale) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomPendlerpauschale.setKeyWhenDetailPanel(key);
				panelBottomPendlerpauschale.eventYouAreSelected(false);
				panelQueryPendlerpauschale.updateButtons();

			} else if (e.getSource() == panelQueryReligion) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomReligion.setKeyWhenDetailPanel(key);
				panelBottomReligion.eventYouAreSelected(false);
				panelQueryReligion.updateButtons();

			} else if (e.getSource() == panelQueryTagesart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomTagesart.setKeyWhenDetailPanel(key);
				panelBottomTagesart.eventYouAreSelected(false);
				panelQueryTagesart.updateButtons();

			} else if (e.getSource() == panelQueryZulage) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomZulage.setKeyWhenDetailPanel(key);
				panelBottomZulage.eventYouAreSelected(false);
				panelQueryZulage.updateButtons();

			} else if (e.getSource() == panelQuerySchichtzuschlag) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomSchichtzuschlag.setKeyWhenDetailPanel(key);
				panelBottomSchichtzuschlag.eventYouAreSelected(false);
				panelQuerySchichtzuschlag.updateButtons();

			} else if (e.getSource() == panelQueryLohnart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomLohnart.setKeyWhenDetailPanel(key);
				panelBottomLohnart.eventYouAreSelected(false);
				panelQueryLohnart.updateButtons();

			} else if (e.getSource() == panelQueryZahltag) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomZahltag.setKeyWhenDetailPanel(key);
				panelBottomZahltag.eventYouAreSelected(false);
				panelQueryZahltag.updateButtons();

			} else if (e.getSource() == panelQueryLohnartStundenfaktor) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomLohnartStundenfaktor.setKeyWhenDetailPanel(key);
				panelBottomLohnartStundenfaktor.eventYouAreSelected(false);
				panelQueryLohnartStundenfaktor.updateButtons();

			} else if (e.getSource() == panelQueryIdentifikation) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomIdentifikation.setKeyWhenDetailPanel(key);
				panelBottomIdentifikation.eventYouAreSelected(false);
				panelQueryIdentifikation.updateButtons();

			} else if (e.getSource() == panelQueryDSGVOKategorie) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomDSGVOKategorie.setKeyWhenDetailPanel(key);
				panelBottomDSGVOKategorie.eventYouAreSelected(false);
				panelQueryDSGVOKategorie.updateButtons();

			} else if (e.getSource() == panelQueryDSGVOText) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomDSGVOText.setKeyWhenDetailPanel(key);
				panelBottomDSGVOText.eventYouAreSelected(false);
				panelQueryDSGVOText.updateButtons();

			} else if (e.getSource() == panelQueryAbwesenheitsart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelBottomAbwesenheitsart.setKeyWhenDetailPanel(key);
				panelBottomAbwesenheitsart.eventYouAreSelected(false);
				panelQueryAbwesenheitsart.updateButtons();

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == panelQueryDSGVOText) {
				int iPos = panelQueryDSGVOText.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryDSGVOText.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryDSGVOText.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getPartnerServicesDelegate().vertauscheDsgvotext(iIdPosition,
							iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryDSGVOText.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryAbwesenheitsart) {
				int iPos = panelQueryAbwesenheitsart.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryAbwesenheitsart.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryAbwesenheitsart.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getPersonalDelegate().vertauscheAbwesenheitsart(iIdPosition,
							iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryAbwesenheitsart.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == panelQueryDSGVOText) {
				int iPos = panelQueryDSGVOText.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryDSGVOText.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryDSGVOText.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryDSGVOText.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getPartnerServicesDelegate().vertauscheDsgvotext(iIdPosition,
							iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryDSGVOText.setSelectedId(iIdPosition);
				}
			} else if (e.getSource() == panelQueryAbwesenheitsart) {
				int iPos = panelQueryAbwesenheitsart.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryAbwesenheitsart.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryAbwesenheitsart.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryAbwesenheitsart.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getPersonalDelegate().vertauscheAbwesenheitsart(iIdPosition,
							iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryAbwesenheitsart.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			panelSplitBeruf.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryLohngruppe) {
				panelBottomLohngruppe.eventActionNew(e, true, false);
				panelBottomLohngruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryBeruf) {
				panelBottomBeruf.eventActionNew(e, true, false);
				panelBottomBeruf.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryPendlerpauschale) {
				panelBottomPendlerpauschale.eventActionNew(e, true, false);
				panelBottomPendlerpauschale.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryReligion) {
				panelBottomReligion.eventActionNew(e, true, false);
				panelBottomReligion.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryTagesart) {
				panelBottomTagesart.eventActionNew(e, true, false);
				panelBottomTagesart.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryZulage) {
				panelBottomZulage.eventActionNew(e, true, false);
				panelBottomZulage.eventYouAreSelected(false);
			} else if (e.getSource() == panelQuerySchichtzuschlag) {
				panelBottomSchichtzuschlag.eventActionNew(e, true, false);
				panelBottomSchichtzuschlag.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryLohnart) {
				panelBottomLohnart.eventActionNew(e, true, false);
				panelBottomLohnart.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryZahltag) {
				panelBottomZahltag.eventActionNew(e, true, false);
				panelBottomZahltag.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryLohnartStundenfaktor) {
				panelBottomLohnartStundenfaktor.eventActionNew(e, true, false);
				panelBottomLohnartStundenfaktor.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryIdentifikation) {
				panelBottomIdentifikation.eventActionNew(e, true, false);
				panelBottomIdentifikation.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryDSGVOKategorie) {
				panelBottomDSGVOKategorie.eventActionNew(e, true, false);
				panelBottomDSGVOKategorie.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryDSGVOText) {
				panelBottomDSGVOText.eventActionNew(e, true, false);
				panelBottomDSGVOText.eventYouAreSelected(false);
			} else if (e.getSource() == panelQueryAbwesenheitsart) {
				panelBottomAbwesenheitsart.eventActionNew(e, true, false);
				panelBottomAbwesenheitsart.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomBeruf) {
				panelQueryBeruf.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomLohngruppe) {
				panelQueryLohngruppe.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomPendlerpauschale) {
				panelQueryPendlerpauschale.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (e.getSource() == panelBottomReligion) {
				panelQueryReligion.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomTagesart) {
				panelQueryTagesart.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomZulage) {
				panelQueryZulage.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomSchichtzuschlag) {
				panelQuerySchichtzuschlag.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomLohnart) {
				panelQueryLohnart.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomZahltag) {
				panelQueryZahltag.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomLohnartStundenfaktor) {
				panelQueryLohnartStundenfaktor.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomIdentifikation) {
				panelQueryIdentifikation.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomDSGVOKategorie) {
				panelQueryDSGVOKategorie.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomDSGVOKategorie) {
				panelQueryDSGVOText.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelBottomAbwesenheitsart) {
				panelQueryAbwesenheitsart.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomBeruf) {
				panelSplitBeruf.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohngruppe) {
				panelSplitLohngruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPendlerpauschale) {
				panelSplitPendlerpauschale.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomReligion) {
				panelSplitReligion.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomTagesart) {
				panelSplitTagesart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZulage) {
				panelSplitZulage.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSchichtzuschlag) {
				panelSplitSchichtzuschlag.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohnart) {
				panelSplitLohnart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZahltag) {
				panelSplitZahltag.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohnartStundenfaktor) {
				panelSplitLohnartStundenfaktor.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomIdentifikation) {
				panelSplitIdentifikation.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomDSGVOKategorie) {
				panelSplitDSGVOKategorie.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomDSGVOText) {
				panelSplitDSGVOText.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomAbwesenheitsart) {
				panelSplitAbwesenheitsart.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomBeruf) {
				Object oKey = panelBottomBeruf.getKeyWhenDetailPanel();
				panelQueryBeruf.eventYouAreSelected(false);
				panelQueryBeruf.setSelectedId(oKey);
				panelSplitBeruf.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohngruppe) {
				Object oKey = panelBottomLohngruppe.getKeyWhenDetailPanel();
				panelQueryLohngruppe.eventYouAreSelected(false);
				panelQueryLohngruppe.setSelectedId(oKey);
				panelSplitLohngruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPendlerpauschale) {
				Object oKey = panelBottomPendlerpauschale.getKeyWhenDetailPanel();
				panelQueryPendlerpauschale.eventYouAreSelected(false);
				panelQueryPendlerpauschale.setSelectedId(oKey);
				panelSplitPendlerpauschale.eventYouAreSelected(false);

			} else if (e.getSource() == panelBottomReligion) {
				Object oKey = panelBottomReligion.getKeyWhenDetailPanel();
				panelQueryReligion.eventYouAreSelected(false);
				panelQueryReligion.setSelectedId(oKey);
				panelSplitReligion.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomTagesart) {
				Object oKey = panelBottomTagesart.getKeyWhenDetailPanel();
				panelQueryTagesart.eventYouAreSelected(false);
				panelQueryTagesart.setSelectedId(oKey);
				panelSplitTagesart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZulage) {
				Object oKey = panelBottomZulage.getKeyWhenDetailPanel();
				panelQueryZulage.eventYouAreSelected(false);
				panelQueryZulage.setSelectedId(oKey);
				panelSplitZulage.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSchichtzuschlag) {
				Object oKey = panelBottomSchichtzuschlag.getKeyWhenDetailPanel();
				panelQuerySchichtzuschlag.eventYouAreSelected(false);
				panelQuerySchichtzuschlag.setSelectedId(oKey);
				panelSplitSchichtzuschlag.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohnart) {
				Object oKey = panelBottomLohnart.getKeyWhenDetailPanel();
				panelQueryLohnart.eventYouAreSelected(false);
				panelQueryLohnart.setSelectedId(oKey);
				panelSplitLohnart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZahltag) {
				Object oKey = panelBottomZahltag.getKeyWhenDetailPanel();
				panelQueryZahltag.eventYouAreSelected(false);
				panelQueryZahltag.setSelectedId(oKey);
				panelSplitZahltag.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohnartStundenfaktor) {
				Object oKey = panelBottomLohnartStundenfaktor.getKeyWhenDetailPanel();
				panelQueryLohnartStundenfaktor.eventYouAreSelected(false);
				panelQueryLohnartStundenfaktor.setSelectedId(oKey);
				panelSplitLohnartStundenfaktor.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomIdentifikation) {
				Object oKey = panelBottomIdentifikation.getKeyWhenDetailPanel();
				panelQueryIdentifikation.eventYouAreSelected(false);
				panelQueryIdentifikation.setSelectedId(oKey);
				panelSplitIdentifikation.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomDSGVOKategorie) {
				Object oKey = panelBottomDSGVOKategorie.getKeyWhenDetailPanel();
				panelQueryDSGVOKategorie.eventYouAreSelected(false);
				panelQueryDSGVOKategorie.setSelectedId(oKey);
				panelSplitDSGVOKategorie.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomAbwesenheitsart) {
				Object oKey = panelBottomAbwesenheitsart.getKeyWhenDetailPanel();
				panelQueryAbwesenheitsart.eventYouAreSelected(false);
				panelQueryAbwesenheitsart.setSelectedId(oKey);
				panelSplitAbwesenheitsart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomDSGVOText) {
				Object oKey = panelBottomDSGVOText.getKeyWhenDetailPanel();
				panelQueryDSGVOText.eventYouAreSelected(false);
				panelQueryDSGVOText.setSelectedId(oKey);
				panelSplitDSGVOText.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomBeruf) {
				Object oKey = panelQueryBeruf.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomBeruf.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryBeruf.getId2SelectAfterDelete();
					panelQueryBeruf.setSelectedId(oNaechster);
				}
				panelSplitBeruf.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohngruppe) {
				Object oKey = panelQueryLohngruppe.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomLohngruppe.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLohngruppe.getId2SelectAfterDelete();
					panelQueryLohngruppe.setSelectedId(oNaechster);
				}
				panelSplitLohngruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomPendlerpauschale) {
				Object oKey = panelQueryPendlerpauschale.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomPendlerpauschale.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPendlerpauschale.getId2SelectAfterDelete();
					panelQueryPendlerpauschale.setSelectedId(oNaechster);
				}
				panelSplitPendlerpauschale.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomReligion) {
				Object oKey = panelQueryReligion.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomReligion.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryReligion.getId2SelectAfterDelete();
					panelQueryReligion.setSelectedId(oNaechster);
				}
				panelSplitReligion.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomTagesart) {
				Object oKey = panelQueryTagesart.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomTagesart.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryTagesart.getId2SelectAfterDelete();
					panelQueryTagesart.setSelectedId(oNaechster);
				}
				panelSplitTagesart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZulage) {
				Object oKey = panelQueryZulage.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomZulage.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryZulage.getId2SelectAfterDelete();
					panelQueryZulage.setSelectedId(oNaechster);
				}
				panelSplitZulage.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomSchichtzuschlag) {
				Object oKey = panelQuerySchichtzuschlag.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomSchichtzuschlag.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQuerySchichtzuschlag.getId2SelectAfterDelete();
					panelQuerySchichtzuschlag.setSelectedId(oNaechster);
				}
				panelSplitSchichtzuschlag.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohnart) {
				Object oKey = panelQueryLohnart.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomLohnart.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLohnart.getId2SelectAfterDelete();
					panelQueryLohnart.setSelectedId(oNaechster);
				}
				panelSplitLohnart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomZahltag) {
				Object oKey = panelQueryZahltag.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomZahltag.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryZahltag.getId2SelectAfterDelete();
					panelQueryZahltag.setSelectedId(oNaechster);
				}
				panelSplitZahltag.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomLohnartStundenfaktor) {
				Object oKey = panelQueryLohnartStundenfaktor.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomLohnartStundenfaktor.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryLohnartStundenfaktor.getId2SelectAfterDelete();
					panelQueryLohnartStundenfaktor.setSelectedId(oNaechster);
				}
				panelSplitLohnartStundenfaktor.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomIdentifikation) {
				Object oKey = panelQueryIdentifikation.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomIdentifikation.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryIdentifikation.getId2SelectAfterDelete();
					panelQueryIdentifikation.setSelectedId(oNaechster);
				}
				panelSplitIdentifikation.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomDSGVOKategorie) {
				Object oKey = panelQueryDSGVOKategorie.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomDSGVOKategorie.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryDSGVOKategorie.getId2SelectAfterDelete();
					panelQueryDSGVOKategorie.setSelectedId(oNaechster);
				}
				panelSplitDSGVOKategorie.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomAbwesenheitsart) {
				Object oKey = panelQueryAbwesenheitsart.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomAbwesenheitsart.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryAbwesenheitsart.getId2SelectAfterDelete();
					panelQueryAbwesenheitsart.setSelectedId(oNaechster);
				}
				panelSplitAbwesenheitsart.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomDSGVOText) {
				Object oKey = panelQueryDSGVOText.getSelectedId();
				if (oKey != null) {
					getInternalFrame().setKeyWasForLockMe(oKey.toString());
				} else {
					getInternalFrame().setKeyWasForLockMe(null);
				}
				if (panelBottomDSGVOText.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryDSGVOText.getId2SelectAfterDelete();
					panelQueryDSGVOText.setSelectedId(oNaechster);
				}
				panelSplitDSGVOText.eventYouAreSelected(false);
			}
		}

	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("pers.title.tab.grunddaten"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		getInternalFrame().setLpTitle(3, "");

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_BERUF) {
			createBeruf();
			panelSplitBeruf.eventYouAreSelected(false);
			panelQueryBeruf.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_LOHNGRUPPE) {
			createLohngruppe();
			panelSplitLohngruppe.eventYouAreSelected(false);
			panelQueryLohngruppe.updateButtons();
		} else if (selectedIndex == IDX_PANEL_PENDLERPAUSCHALE) {
			createPendlerpauschale();
			panelSplitPendlerpauschale.eventYouAreSelected(false);
			panelQueryPendlerpauschale.updateButtons();
		} else if (selectedIndex == IDX_PANEL_RELIGION) {
			createReligion();
			panelSplitReligion.eventYouAreSelected(false);
			panelQueryReligion.updateButtons();
		} else if (selectedIndex == IDX_PANEL_TAGESART) {
			createTagesart();
			panelSplitTagesart.eventYouAreSelected(false);
			panelQueryTagesart.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ZULAGE) {
			createZulage();
			panelSplitZulage.eventYouAreSelected(false);
			panelQueryZulage.updateButtons();
		} else if (selectedIndex == IDX_PANEL_SCHICHTZUSCHLAG) {
			createSchichtzuschlag();
			panelSplitSchichtzuschlag.eventYouAreSelected(false);
			panelQuerySchichtzuschlag.updateButtons();
		} else if (selectedIndex == IDX_PANEL_LOHNART) {
			createLohnart();
			panelSplitLohnart.eventYouAreSelected(false);
			panelQueryLohnart.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ZAHLTAG) {
			createZahltag();
			panelSplitZahltag.eventYouAreSelected(false);
			panelQueryZahltag.updateButtons();
		} else if (selectedIndex == IDX_PANEL_LOHNARTSTUNDENFAKTOR) {
			createLohnartStundenfaktor();
			panelSplitLohnartStundenfaktor.eventYouAreSelected(false);
			panelQueryLohnartStundenfaktor.updateButtons();
		} else if (selectedIndex == IDX_PANEL_IDENTIFIKATION) {
			createIdentifikation();
			panelSplitIdentifikation.eventYouAreSelected(false);
			panelQueryIdentifikation.updateButtons();
		} else if (selectedIndex == IDX_PANEL_DSGVO_KATEGORIE) {
			createDSGVOKategorie();
			panelSplitDSGVOKategorie.eventYouAreSelected(false);
			panelQueryDSGVOKategorie.updateButtons();
		} else if (selectedIndex == IDX_PANEL_DSGVO_TEXT) {
			createDSGVOText();
			panelSplitDSGVOText.eventYouAreSelected(false);
			panelQueryDSGVOText.updateButtons();
		} else if (selectedIndex == IDX_PANEL_ABWESENHEITSART) {
			createAbwesenheitsart();
			panelSplitAbwesenheitsart.eventYouAreSelected(false);
			panelQueryAbwesenheitsart.updateButtons();
		}

	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);
		}
		return wrapperMenuBar;
	}

}
