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
package com.lp.client.fertigung;

import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.LosbereichDto;
import com.lp.server.fertigung.service.LosklasseDto;
import com.lp.server.fertigung.service.LosstatusDto;
import com.lp.server.fertigung.service.WiederholendeloseDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
/**
 *
 * <p>Diese Klasse kuemmert sich um das Modul Fertigung</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 21. 07. 2005</p>
 *
 * @author  Martin Bluehweis
 *
 * @version $Revision: 1.5 $
 */
public class InternalFrameFertigung extends InternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneLos tabbedPaneLos = null;
	private TabbedPaneInternebestellung tabbedPaneInternebestellung = null;
	private TabbedPaneKapazitaetsvorschau tabbedPaneKapazitaetsvorschau = null;
	private TabbedPaneFertigungGrunddaten tabbedPaneGrunddaten = null;
	private TabbedPaneWiederholendelose tabbedPaneWiederholendelose = null;
	private TabbedPaneOffeneAG tabbedPaneOffeneAG = null;
	private TabbedPaneMehrereLoseErledigenStornieren tabbedPaneMehrereLoseErledigenStornieren = null;
	private TabbedPaneBedarfsuebernahme tabbedPaneBedarfsuebernahme = null;
	private TabbedPaneTrumpf tabbedPaneTrumpf = null;


	public boolean bHatProFirst = false;

	public static int IDX_TABBED_PANE_LOS = 0;
	private int IDX_TABBED_PANE_OFFENE_AGS = -1;
	public int IDX_TABBED_PANE_INTERNEBESTELLUNG = -1;
	private int IDX_TABBED_PANE_KAPAZITAETSVORSCHAU = -1;
	private int IDX_TABBED_PANE_WIEDERHOLENDELOSE = -1;
	private int IDX_TABBED_PANE_MEHRERE_LOSE_ERLEDIGEN_STORNIEREN = -1;
	private int IDX_TABBED_PANE_GRUNDDATEN = -1;
	private int IDX_TABBED_PANE_BEDARFSUEBERNAHME = -1;
	public int IDX_TABBED_PANE_TRUMPF = -1;

	private LosstatusDto losstatusDto = null;
	private LosklasseDto losklasseDto = new LosklasseDto();
	private LosbereichDto losbereichDto = new LosbereichDto();

	public LosbereichDto getLosbereichDto() {
		return losbereichDto;
	}

	public void setLosbereichDto(LosbereichDto losbereichDto) {
		this.losbereichDto = losbereichDto;
	}

	private WiederholendeloseDto wiederholendeloseDto;

	public InternalFrameFertigung(String title, String belegartCNr,
			String sRechtModulweitI) throws Throwable {

		super(title, belegartCNr, sRechtModulweitI);
		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_STUECKLISTE,
						ParameterFac.PARAMETER_PRO_FIRST_DBURL);
		String dburl = parameter.getCWert();
		if (dburl != null && dburl.trim().length() > 0) {
			bHatProFirst = true;
		}

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		int tabIndex = 0;
		IDX_TABBED_PANE_LOS = tabIndex;
		// 1 tab unten: Lose
		tabbedPaneRoot.insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"fert.tab.unten.los.title"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"fert.tab.unten.los.tooltip"), IDX_TABBED_PANE_LOS);

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ZEITERFASSUNG)) {

			tabIndex++;
			IDX_TABBED_PANE_OFFENE_AGS = tabIndex;
			// 2 tab unten: Interne Bestellung
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr("fert.offeneags"),
					null, null,
					LPMain.getInstance().getTextRespectUISPr("fert.offeneags"),
					IDX_TABBED_PANE_OFFENE_AGS);
		}

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_FERT_LOS_CUD)) {
			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_HVMA_ZEITERFASSUNG)) {
				tabIndex++;
				IDX_TABBED_PANE_BEDARFSUEBERNAHME = tabIndex;
				// 2 tab unten: Interne Bestellung
				tabbedPaneRoot.insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"fert.bedarfsuebernahme"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"fert.bedarfsuebernahme"),
						IDX_TABBED_PANE_BEDARFSUEBERNAHME);
			}

			tabIndex++;
			IDX_TABBED_PANE_INTERNEBESTELLUNG = tabIndex;
			// 2 tab unten: Interne Bestellung
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"fert.tab.unten.internebestellung.title"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"fert.tab.unten.internebestellung.tooltip"),
					IDX_TABBED_PANE_INTERNEBESTELLUNG);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_KAPAZITAETSVORSCHAU)) {
				tabIndex++;
				IDX_TABBED_PANE_KAPAZITAETSVORSCHAU = tabIndex;
				// 3 tab unten: Kapazitaetsvorschau
				tabbedPaneRoot.insertTab(
						LPMain.getInstance().getTextRespectUISPr(
								"fert.tab.unten.kapazitaetsvorschau.title"),
						null,
						null,
						LPMain.getInstance().getTextRespectUISPr(
								"fert.tab.unten.kapazitaetsvorschau.tooltip"),
						IDX_TABBED_PANE_KAPAZITAETSVORSCHAU);
			}

			tabIndex++;
			IDX_TABBED_PANE_WIEDERHOLENDELOSE = tabIndex;
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"fert.wiederholendelose"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"fert.wiederholendelose"),
					IDX_TABBED_PANE_WIEDERHOLENDELOSE);
			tabIndex++;
			IDX_TABBED_PANE_MEHRERE_LOSE_ERLEDIGEN_STORNIEREN = tabIndex;
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"fert.los.mehrereerledigen"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"fert.los.mehrereerledigen"),
					IDX_TABBED_PANE_MEHRERE_LOSE_ERLEDIGEN_STORNIEREN);
		}
		
		
		//PJ22507
		if (LPMain.getInstance().getDesktop().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_TRUTOPS_BOOST)) {
			tabIndex++;
			IDX_TABBED_PANE_TRUMPF = tabIndex;
			tabbedPaneRoot.insertTab(LPMain.getInstance().getTextRespectUISPr("artikel.trumpf"), null, null,
					LPMain.getInstance().getTextRespectUISPr("artikel.trumpf"), IDX_TABBED_PANE_TRUMPF);
		}
		
		// 4 tab unten: Grunddaten
		// nur anzeigen wenn Benutzer Recht dazu hat
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_GRUNDDATEN_SEHEN)) {
			tabIndex++;
			IDX_TABBED_PANE_GRUNDDATEN = tabIndex;
			tabbedPaneRoot.insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"fert.tab.unten.grunddaten.title"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"fert.tab.unten.grunddaten.tooltip"),
					IDX_TABBED_PANE_GRUNDDATEN);
		}

		getTabbedPaneLos().lPEventObjectChanged(null);

		tabbedPaneRoot.setSelectedComponent(tabbedPaneLos);

		// ich selbst moechte informiert werden.
		addItemChangedListener(this);
		// awt: listener bin auch ich.
		registerChangeListeners();
		// das icon setzen.
		ImageIcon iicon = new javax.swing.ImageIcon(getClass().getResource(
				"/com/lp/client/res/factory16x16.png"));
		setFrameIcon(iicon);
	}

	

	private void createTabbedPaneTrumpf() throws Throwable { // vkpf:
		if (tabbedPaneTrumpf == null) {
			// lazy loading
			tabbedPaneTrumpf = new TabbedPaneTrumpf(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_TRUMPF, tabbedPaneTrumpf);
			initComponents();
		}
	}
	
	
	public void lPStateChanged(EventObject e) throws Throwable {
		int selectedCur = ((JTabbedPane) e.getSource()).getSelectedIndex();

		DelegateFactory.getInstance().getFertigungDelegate()
				.removeLockDerInternenBestellungWennIchIhnSperre();

		if (selectedCur == IDX_TABBED_PANE_LOS) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneLos().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_GRUNDDATEN) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneGrunddaten().lPEventObjectChanged(null);
		} else if (selectedCur == IDX_TABBED_PANE_INTERNEBESTELLUNG) {
			// Info an Tabbedpane, bist selektiert worden.
			try {
				DelegateFactory.getInstance().getFertigungDelegate()
						.pruefeBearbeitenDerInternenBestellungErlaubt();

				getTabbedPaneInternebestellung().lPEventObjectChanged(null);
				setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");

			} catch (ExceptionLP efc) {
				if (efc != null
						&& efc.getICode() == EJBExceptionLP.FEHLER_INTERNEBESTELLUNG_IST_GESPERRT) {
					tabbedPaneRoot.setSelectedComponent(tabbedPaneLos);
					// Benutzer der gerade sperrt finden
					String[] sHelper = efc.getSMsg().split(" ");
					String sLocker = DelegateFactory
							.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey(
									Integer.parseInt(sHelper[sHelper.length - 1]))
							.getCKurzzeichen();
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.warning"), LPMain
									.getInstance().getMsg(efc) + sLocker);
				} else {
					throw efc;
				}
			}

		} else if (selectedCur == IDX_TABBED_PANE_OFFENE_AGS) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneOffeneAG().lPEventObjectChanged(null);
			setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
		} else if (selectedCur == IDX_TABBED_PANE_BEDARFSUEBERNAHME) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneBedarfsuebernahme().lPEventObjectChanged(null);
			setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
		} else if (selectedCur == IDX_TABBED_PANE_KAPAZITAETSVORSCHAU) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneKapazitaetsvorschau().lPEventObjectChanged(null);
			setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
		} else if (selectedCur == IDX_TABBED_PANE_WIEDERHOLENDELOSE) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneWiederholendelose().lPEventObjectChanged(null);
			setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
		} else if (selectedCur == IDX_TABBED_PANE_MEHRERE_LOSE_ERLEDIGEN_STORNIEREN) {
			// Info an Tabbedpane, bist selektiert worden.
			getTabbedPaneMehrereLoseErledigen().lPEventObjectChanged(null);
			setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
		} else if (selectedCur == IDX_TABBED_PANE_TRUMPF) {
			createTabbedPaneTrumpf();
			// Info an Tabbedpane, bist selektiert worden.
			tabbedPaneTrumpf.lPEventObjectChanged(null);
		} 
	}

	public TabbedPaneLos getTabbedPaneLos() throws Throwable {
		if (tabbedPaneLos == null) {
			// lazy loading
			tabbedPaneLos = new TabbedPaneLos(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_LOS, tabbedPaneLos);
			// if (tabbedPaneLos.getSelectedIIdLos() == null) {
			// enableAllPanelsExcept(false);
			// //Grunddaten enablen.
			// tabbedPaneRoot.setEnabledAt(IDX_TABBED_PANE_GRUNDDATEN, true);
			// }
			initComponents();
		}
		return tabbedPaneLos;
	}

	private TabbedPaneInternebestellung getTabbedPaneInternebestellung()
			throws Throwable {
		if (tabbedPaneInternebestellung == null) {
			// lazy loading
			tabbedPaneInternebestellung = new TabbedPaneInternebestellung(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_INTERNEBESTELLUNG,
					tabbedPaneInternebestellung);
		}
		return tabbedPaneInternebestellung;
	}

	private TabbedPaneOffeneAG getTabbedPaneOffeneAG() throws Throwable {
		if (tabbedPaneOffeneAG == null) {
			// lazy loading
			tabbedPaneOffeneAG = new TabbedPaneOffeneAG(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_OFFENE_AGS,
					tabbedPaneOffeneAG);
		}
		return tabbedPaneOffeneAG;
	}
	private TabbedPaneBedarfsuebernahme getTabbedPaneBedarfsuebernahme() throws Throwable {
		if (tabbedPaneBedarfsuebernahme == null) {
			// lazy loading
			tabbedPaneBedarfsuebernahme = new TabbedPaneBedarfsuebernahme(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_BEDARFSUEBERNAHME,
					tabbedPaneBedarfsuebernahme);
		}
		return tabbedPaneBedarfsuebernahme;
	}

	private TabbedPaneMehrereLoseErledigenStornieren getTabbedPaneMehrereLoseErledigen()
			throws Throwable {
		if (tabbedPaneMehrereLoseErledigenStornieren == null) {
			// lazy loading
			tabbedPaneMehrereLoseErledigenStornieren = new TabbedPaneMehrereLoseErledigenStornieren(
					this);
			tabbedPaneRoot.setComponentAt(
					IDX_TABBED_PANE_MEHRERE_LOSE_ERLEDIGEN_STORNIEREN,
					tabbedPaneMehrereLoseErledigenStornieren);
		}
		return tabbedPaneMehrereLoseErledigenStornieren;
	}

	private TabbedPaneKapazitaetsvorschau getTabbedPaneKapazitaetsvorschau()
			throws Throwable {
		if (tabbedPaneKapazitaetsvorschau == null) {
			// lazy loading
			tabbedPaneKapazitaetsvorschau = new TabbedPaneKapazitaetsvorschau(
					this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_KAPAZITAETSVORSCHAU,
					tabbedPaneKapazitaetsvorschau);
			initComponents();
		}
		return tabbedPaneKapazitaetsvorschau;
	}

	private TabbedPaneFertigungGrunddaten getTabbedPaneGrunddaten()
			throws Throwable {
		if (tabbedPaneGrunddaten == null) {
			// lazy loading
			tabbedPaneGrunddaten = new TabbedPaneFertigungGrunddaten(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_GRUNDDATEN,
					tabbedPaneGrunddaten);
			HelperClient.setComponentNames(this);
		}
		return tabbedPaneGrunddaten;
	}

	private TabbedPaneWiederholendelose getTabbedPaneWiederholendelose()
			throws Throwable {
		if (tabbedPaneWiederholendelose == null) {
			// lazy loading
			tabbedPaneWiederholendelose = new TabbedPaneWiederholendelose(this);
			tabbedPaneRoot.setComponentAt(IDX_TABBED_PANE_WIEDERHOLENDELOSE,
					tabbedPaneWiederholendelose);
			HelperClient.setComponentNames(this);
		}
		return tabbedPaneWiederholendelose;
	}

	protected void lPEventItemChanged(ItemChangedEvent eI) {
		// nothing here.
	}

	public void setLosklasseDto(LosklasseDto losklasseDto) {
		this.losklasseDto = losklasseDto;
	}

	public void setLosstatusDto(LosstatusDto losstatusDto) {
		this.losstatusDto = losstatusDto;
	}

	public void setWiederholendeloseDto(
			WiederholendeloseDto wiederholendeloseDto) {
		this.wiederholendeloseDto = wiederholendeloseDto;
	}

	public LosklasseDto getLosklasseDto() {
		return losklasseDto;
	}

	public LosstatusDto getLosstatusDto() {
		return losstatusDto;
	}

	public WiederholendeloseDto getWiederholendeloseDto() {
		return wiederholendeloseDto;
	}
}
