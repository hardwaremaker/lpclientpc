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

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;

import com.lp.client.fertigung.TabbedPaneLos.StatusFilterKeys;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.personal.service.ZeitverteilungDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.csv.LPCSVWriter;

import net.sf.jasperreports.engine.JRException;

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
 * @version $Revision: 1.5 $
 */
public class TabbedPaneOffeneAG extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryOffeneAGs = null;
	private PanelQuery panelQueryProduzieren = null;
	private PanelQuery panelQueryReihen = null;

	private PanelQueryFLR panelQueryFLRPersonal = null;
	private Integer personalIId_ZuletztAusgewaehlt = null;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_PRODUZIEREN = 1;
	private final static int IDX_PANEL_REIHEN = 2;

	private final static String EXTRA_GOTO_AG = "goto_ag";
	private final static String EXTRA_PROFIRST_EXPORT = "profirst_export";

	private final static String EXTRA_GOTO_LOS = "goto_los";

	private static final String ACTION_SPECIAL_VERDICHTEN = PanelBasis.ALWAYSENABLED + "action_special_verdichten";

	private JLabel summeSollmenge;

	private WrapperMenuBar wrapperMenuBar = null;

	private WrapperCheckBox wcbReihen = new WrapperCheckBox();

	private static final String MENUE_ACTION_MASCHINE_MATERIAL = "MENUE_ACTION_MASCHINE_MATERIAL";
	private static final String MENUE_ACTION_TAETIGKEIT_AGBEGINN = "MENUE_ACTION_TAETIGKEIT_AGBEGINN";

	static final public String ACTION_SPECIAL_TOGGLE_AG_FERTIG = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_toggle_ag_fertig";

	static final public String ACTION_SPECIAL_FERTIGUNG_STARTEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_toggle_fertigung_starten";

	static final public String ACTION_SPECIAL_MASCHINE_REIHEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "action_special_maschine_reihen";

	private final String ACTION_SPECIAL_AUSGEBEN = PanelBasis.ALWAYSENABLED + "action_special_ausgeben";

	public TabbedPaneOffeneAG(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr("fert.offeneags"));
		jbInit();
		initComponents();
		plusMinusButtonsSchalten(false);
	}

	public PanelQuery getPanelQueryWiederholende() {
		return panelQueryOffeneAGs;
	}

	private void jbInit() throws Throwable {

		insertTab(LPMain.getInstance().getTextRespectUISPr("fert.offeneags"), null, null,
				LPMain.getInstance().getTextRespectUISPr("fert.offeneags"), IDX_PANEL_AUSWAHL);

		if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_FERT_LOS_CUD)) {

			insertTab(LPMain.getInstance().getTextRespectUISPr("fert.produzieren"), null, null,
					LPMain.getInstance().getTextRespectUISPr("fert.produzieren"), IDX_PANEL_PRODUZIEREN);

			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MASCHINENZEITERFASSUNG)) {
				insertTab(LPMain.getInstance().getTextRespectUISPr("fert.reihen"), null, null,
						LPMain.getInstance().getTextRespectUISPr("fert.reihen"), IDX_PANEL_REIHEN);
			}

		}

		createAuswahl();

		panelQueryOffeneAGs.eventYouAreSelected(false);

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryOffeneAGs, ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	private void createAuswahl() throws Throwable {
		if (panelQueryOffeneAGs == null) {

			String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			// Filter
			FilterKriterium[] kriterien = new FilterKriterium[1];
			kriterien[0] = new FilterKriterium("flroffeneags.mandant_c_nr", true,
					"'" + LPMain.getInstance().getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL,
					false);

			FilterKriterium[] filters = new FilterKriterium[1];
			filters[0] = new FilterKriterium("flroffeneags.i_anzahlzeitdaten", true, "0", FilterKriterium.OPERATOR_GT,
					false);

			panelQueryOffeneAGs = new PanelQuery(null, kriterien, QueryParameters.UC_ID_OFFENE_AGS, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("fert.offeneags"), true);
			plusMinusButtonsSchalten(false);

			panelQueryOffeneAGs.befuelleFilterkriteriumSchnellansicht(filters, false,
					LPMain.getTextRespectUISPr("fert.offeneags.eintraege.mit.zeitdaten"));

			panelQueryOffeneAGs.createAndSaveAndShowButton("/com/lp/client/res/data_into.png",
					LPMain.getInstance().getTextRespectUISPr("fert.offeneags.gotoag"),
					PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_AG, null);

			if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_FERT_LOS_CUD)) {

				panelQueryOffeneAGs.createAndSaveAndShowButton("/com/lp/client/res/check2.png",
						LPMain.getTextRespectUISPr("fert.arbeitsplan.togglefertig"), ACTION_SPECIAL_TOGGLE_AG_FERTIG,
						null, null);
			}

			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_ZEITERFASSUNG)) {
				panelQueryOffeneAGs.getToolBar().addButtonLeft("/com/lp/client/res/gear_run.png",
						LPMain.getTextRespectUISPr("fert.offeneag.fertigungstarten"), ACTION_SPECIAL_FERTIGUNG_STARTEN,
						null, null);

			}

			panelQueryOffeneAGs.setFilterComboBox(
					DelegateFactory.getInstance().getFertigungDelegate().getAllMaschinenInOffeAGs(),
					new FilterKriterium("MASCHINE_GRUPPE", true, "" + "", FilterKriterium.OPERATOR_EQUAL, false), false,
					LPMain.getTextRespectUISPr("lp.alle"), false);

			panelQueryOffeneAGs.befuellePanelFilterkriterienDirekt(
					new FilterKriteriumDirekt("sollmaterial.c_nr", "", FilterKriterium.OPERATOR_LIKE,
							LPMain.getTextRespectUISPr("fert.offeneags.filter.material"),
							FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
																	// als
																	// '%XX'
							true, // wrapWithSingleQuotes
							true, Facade.MAX_UNBESCHRAENKT),
					new FilterKriteriumDirekt("taetigkeit.c_nr", "", FilterKriterium.OPERATOR_LIKE,
							LPMain.getTextRespectUISPr("fert.offeneags.filter.taetigkeit"),
							FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
																	// als
																	// '%XX'
							true, // wrapWithSingleQuotes
							true, Facade.MAX_UNBESCHRAENKT));
			panelQueryOffeneAGs.addDirektFilter(new FilterKriteriumDirekt("flrkunde.flrpartner.c_kbez", "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("fert.offeneags.filter.kunde"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
															// '%XX'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT));

			panelQueryOffeneAGs.setMultipleRowSelectionEnabled(true);
			FilterKriteriumDirekt fkDirekt = FertigungFilterFactory.getInstance().createFKDLosnummerOffeneAGs();
			panelQueryOffeneAGs.addDirektFilter(fkDirekt);

			panelQueryOffeneAGs.addDirektFilter(new FilterKriteriumDirekt("stueckliste.c_nr", "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("fert.offeneags.filter.stkl"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
															// als
															// '%XX'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT));

			panelQueryOffeneAGs.addDirektFilter(new FilterKriteriumDirekt("sollarbeitsplan.i_unterarbeitsgang", "",
					FilterKriterium.OPERATOR_IS + " " + FilterKriterium.OPERATOR_NULL,
					LPMain.getTextRespectUISPr("fert.offeneags.nurhauptags"), FilterKriteriumDirekt.PROZENT_NONE, true,
					false, Facade.MAX_UNBESCHRAENKT, FilterKriteriumDirekt.TYP_BOOLEAN));

			Map<String, String> mComboBox = new LinkedHashMap<String, String>();
			mComboBox.put(StatusFilterKeys.Alle, LPMain.getTextRespectUISPr("fert.losauswahl.krit.alle"));
			mComboBox.put(StatusFilterKeys.ZuProduzierende,
					LPMain.getTextRespectUISPr("fert.losauswahl.krit.zuproduzierende"));
			mComboBox.put(StatusFilterKeys.Offene, LPMain.getTextRespectUISPr("fert.losauswahl.krit.offene"));

			panelQueryOffeneAGs.addDirektFilter(
					new FilterKriteriumDirekt("flrlos.status_c_nr", "", FilterKriterium.OPERATOR_NOT_IN,
							LPMain.getTextRespectUISPr("fert.offeneags.status"), FilterKriteriumDirekt.PROZENT_NONE,
							false, false, Facade.MAX_UNBESCHRAENKT, FilterKriteriumDirekt.TYP_COMBOBOX, mComboBox));

			wcbReihen.setText(LPMain.getTextRespectUISPr("fert.offeneags.reihen"));
			wcbReihen.setToolTipText(LPMain.getTextRespectUISPr("fert.offeneags.reihen.tooltip"));

			wcbReihen.addActionListener(this);
			wcbReihen.setMinimumSize(new Dimension(200, Defaults.getInstance().getControlHeight()));
			wcbReihen.setPreferredSize(new Dimension(200, Defaults.getInstance().getControlHeight()));

			if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_FERT_LOS_CUD)) {
				panelQueryOffeneAGs.getToolBar().getToolsPanelCenter().add(wcbReihen);
			}
			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryOffeneAGs);

		}
	}

	private void createReihen() throws Throwable {
		if (panelQueryReihen == null) {

			String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			// Filter
			FilterKriterium[] kriterien = new FilterKriterium[1];
			kriterien[0] = new FilterKriterium("flroffeneags.mandant_c_nr", true,
					"'" + LPMain.getInstance().getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL,
					false);

			panelQueryReihen = new PanelQuery(null, kriterien, QueryParameters.UC_ID_OFFENE_AGS, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("fert.offeneags"), true);

			panelQueryReihen.createAndSaveAndShowButton("/com/lp/client/res/data_into.png",
					LPMain.getInstance().getTextRespectUISPr("fert.offeneags.gotoag"),
					PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_AG, null);

			if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_FERT_LOS_CUD)) {

				panelQueryReihen.createAndSaveAndShowButton("/com/lp/client/res/check2.png",
						LPMain.getTextRespectUISPr("fert.arbeitsplan.togglefertig"), ACTION_SPECIAL_TOGGLE_AG_FERTIG,
						null, null);

				panelQueryReihen.getToolBar().addButtonLeft("/com/lp/client/res/sort_az_descending.png",
						LPMain.getTextRespectUISPr("fert.agsdurchnummerieren"), ACTION_SPECIAL_MASCHINE_REIHEN, null,
						null);

			}

			panelQueryReihen.setFilterComboBox(
					DelegateFactory.getInstance().getFertigungDelegate().getAllMaschinenOhneMaschinengruppenInOffeAGs(),
					new FilterKriterium("flroffeneags.flrmaschine.i_id", true, "" + "", FilterKriterium.OPERATOR_EQUAL,
							false),
					true);

			panelQueryReihen.setMultipleRowSelectionEnabled(true);
			setComponentAt(IDX_PANEL_REIHEN, panelQueryReihen);
		}

		switchPlusMinusButtons(panelQueryReihen, true);
	}

	private void createProduzieren() throws Throwable {
		if (panelQueryProduzieren == null) {

			// Filter
			FilterKriterium[] kriterien = new FilterKriterium[2];
			kriterien[0] = new FilterKriterium("flrProduzieren.flrlos.mandant_c_nr", true,
					"'" + LPMain.getInstance().getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL,
					false);

			kriterien[1] = new FilterKriterium("flrProduzieren.flrlos.status_c_nr", true,
					"'" + LocaleFac.STATUS_ANGELEGT + "'", FilterKriterium.OPERATOR_EQUAL, false);

			panelQueryProduzieren = new PanelQuery(null, kriterien, QueryParameters.UC_ID_PRODUZIEREN, null,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("fert.produzieren"), true);

			panelQueryProduzieren.createAndSaveAndShowButton("/com/lp/client/res/data_into.png",
					LPMain.getInstance().getTextRespectUISPr("fert.offeneags.gotolos"),
					PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_LOS, null);

			// ProFirst

			if (getInternalFrameFertigung().bHatProFirst == true) {

				panelQueryProduzieren.createAndSaveAndShowButton("/com/lp/client/res/p1.png",
						LPMain.getInstance().getTextRespectUISPr("fert.offeneag.profirstexport"),
						PanelBasis.ACTION_MY_OWN_NEW + EXTRA_PROFIRST_EXPORT, null);

			}
			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PRUEFPLAN1) == false) {
				panelQueryProduzieren.addDirektFilter(new FilterKriteriumDirekt("artikel_sollmaterial.c_nr", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("fert.offeneags.filter.material"),
						FilterKriteriumDirekt.PROZENT_TRAILING, true, // wrapWithSingleQuotes
						true, Facade.MAX_UNBESCHRAENKT));
			}

			panelQueryProduzieren.setFilterComboBox(
					DelegateFactory.getInstance().getZeiterfassungDelegate().getAllMaschinen(),
					new FilterKriterium("MASCHINE_GRUPPE", true, "" + "", FilterKriterium.OPERATOR_EQUAL, false), false,
					LPMain.getTextRespectUISPr("lp.alle"), false);

			summeSollmenge = new JLabel();

			panelQueryProduzieren.getToolBar().getToolsPanelCenter().add(summeSollmenge);

			panelQueryProduzieren.addDirektFilter(new FilterKriteriumDirekt("flrartikel_sollarbeitsplan.c_nr", "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("fert.offeneags.filter.taetigkeit"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
															// als
															// '%XX'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT));
			panelQueryProduzieren.addDirektFilter(new FilterKriteriumDirekt("kunde.flrpartner.c_kbez", "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("fert.offeneags.filter.kunde"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als
															// '%XX'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT));

			FilterKriteriumDirekt fkDirekt = FertigungFilterFactory.getInstance().createFKDLosnummerOffeneAGs();
			panelQueryProduzieren.addDirektFilter(fkDirekt);

			panelQueryProduzieren.addDirektFilter(new FilterKriteriumDirekt("flrlos.flrstueckliste.flrartikel.c_nr", "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("fert.offeneags.filter.stkl"),
					FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung
															// als
															// '%XX'
					true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT));

			panelQueryProduzieren.addDirektFilter(new FilterKriteriumDirekt(FertigungFac.ZUPRODUZIEREN_TEXTSUCHE, "",
					FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("fert.produzieren.textsuche"),
					FilterKriteriumDirekt.EXTENDED_SEARCH, false, true, Facade.MAX_UNBESCHRAENKT));

			panelQueryProduzieren.setMultipleRowSelectionEnabled(true);

			panelQueryProduzieren.createAndSaveAndShowButton("/com/lp/client/res/branch.png",
					LPMain.getTextRespectUISPr("fert.produzieren.verdichten"), ACTION_SPECIAL_VERDICHTEN,
					RechteFac.RECHT_FERT_LOS_CUD);

			panelQueryProduzieren.createAndSaveAndShowButton("/com/lp/client/res/data_next.png",
					LPMain.getTextRespectUISPr("fert.produzieren.loseausgeben"), ACTION_SPECIAL_AUSGEBEN,
					RechteFac.RECHT_FERT_LOS_CUD);

			setComponentAt(IDX_PANEL_PRODUZIEREN, panelQueryProduzieren);

		}
	}

	public InternalFrameFertigung getInternalFrameFertigung() {
		return (InternalFrameFertigung) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryOffeneAGs.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryOffeneAGs) {
				if (panelQueryOffeneAGs.getSelectedId() != null) {
					getInternalFrameFertigung().setKeyWasForLockMe(panelQueryOffeneAGs.getSelectedId() + "");

					if (panelQueryOffeneAGs.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
					} else {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, true);
					}
					if (panelQueryOffeneAGs.getHmOfButtons()
							.containsKey(PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_AG)) {
						panelQueryOffeneAGs.getHmOfButtons().get(PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_AG)
								.getButton().setEnabled(true);
					}

				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, true);
				}

			} else if (e.getSource() == panelQueryProduzieren) {

				AbstractButton refreshBtn = panelQueryProduzieren.getHmOfButtons().get(PanelBasis.ACTION_REFRESH)
						.getButton();
				refreshBtn.setOpaque(true);
				refreshBtn.setBackground(UIManager.getColor("Button.background"));

				if (panelQueryProduzieren.getSelectedIds() != null
						&& panelQueryProduzieren.getSelectedIds().length > 0) {

					ArrayList<Integer> losIIs = new ArrayList<Integer>();

					for (int i = 0; i < panelQueryProduzieren.getSelectedIds().length; i++) {
						losIIs.add((Integer) panelQueryProduzieren.getSelectedIds()[i]);
					}

					BigDecimal bd[] = DelegateFactory.getInstance().getFertigungDelegate()
							.getSummeMengeSollmaterialUndDauerFuerZuProduzieren(losIIs);

					summeSollmenge.setText("   " + LPMain.getTextRespectUISPr("fert.produzieren.summematerial")
							+ Helper.formatZahl(bd[0], LPMain.getInstance().getTheClient().getLocUi())
							+ LPMain.getTextRespectUISPr("fert.produzieren.summedauer")
							+ Helper.formatZahl(bd[1], LPMain.getInstance().getTheClient().getLocUi()));
				} else {
					summeSollmenge.setText("");
				}

			} else if (e.getSource() == panelQueryReihen) {
				if (panelQueryReihen.getSelectedId() != null) {
					getInternalFrameFertigung().setKeyWasForLockMe(panelQueryReihen.getSelectedId() + "");

					if (panelQueryReihen.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
					} else {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, true);
					}
					if (panelQueryReihen.getHmOfButtons().containsKey(PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_AG)) {
						panelQueryReihen.getHmOfButtons().get(PanelBasis.ACTION_MY_OWN_NEW + EXTRA_GOTO_AG).getButton()
								.setEnabled(true);
					}

				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, true);
				}

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
		} else if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRPersonal) {
				Integer personalIId = (Integer) panelQueryFLRPersonal.getSelectedId();
				personalIId_ZuletztAusgewaehlt = personalIId;
				Timestamp tZeit = new Timestamp(System.currentTimeMillis());
				for (int i = 0; i < panelQueryOffeneAGs.getSelectedIds().length; i++) {

					Integer arbeitsgangIId = (Integer) panelQueryOffeneAGs.getSelectedIds()[i];

					LossollarbeitsplanDto saDto = DelegateFactory.getInstance().getFertigungDelegate()
							.lossollarbeitsplanFindByPrimaryKey(arbeitsgangIId);

					ZeitverteilungDto zeitverteilungDto = new ZeitverteilungDto();
					zeitverteilungDto.setLosIId(saDto.getLosIId());
					zeitverteilungDto.setTZeit(new Timestamp(tZeit.getTime() + i * 1000));
					zeitverteilungDto.setPersonalIId(personalIId);

					zeitverteilungDto.setArtikelIId(saDto.getArtikelIIdTaetigkeit());

					zeitverteilungDto.setLossollarbeitsplanIId(arbeitsgangIId);
					zeitverteilungDto.setMaschineIId(saDto.getMaschineIId());

					try {
						DelegateFactory.getInstance().getZeiterfassungDelegate()
								.createZeitverteilung(zeitverteilungDto);
					} catch (ExceptionLP e1) {
						if (e1.getICode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {

							LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
									.losFindByPrimaryKey(saDto.getLosIId());

							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getMessageTextRespectUISPr("fert.gemeinsamefertigungstarten.error",
											losDto.getCNr()));
						} else {
							throw e1;
						}

					}

				}

			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

			if (e.getSource().equals(panelQueryProduzieren)) {

				if (sAspectInfo.equals(ACTION_SPECIAL_VERDICHTEN)) {
					if (panelQueryProduzieren.getSelectedIds() != null
							&& panelQueryProduzieren.getSelectedIds().length > 0) {

						ArrayList<Integer> losIIs = new ArrayList<Integer>();

						for (int i = 0; i < panelQueryProduzieren.getSelectedIds().length; i++) {
							losIIs.add((Integer) panelQueryProduzieren.getSelectedIds()[i]);
						}

						DelegateFactory.getInstance().getFertigungDelegate().angelegteLoseVerdichten(losIIs);

						panelQueryProduzieren.eventYouAreSelected(false);

					}

				}

				if (sAspectInfo.equals(ACTION_SPECIAL_AUSGEBEN)) {

					JCheckBox nurproduzierbare = new JCheckBox(
							LPMain.getTextRespectUISPr("fert.produzieren.nurproduzierbare"));
					JCheckBox direktDrucken = new JCheckBox(
							LPMain.getTextRespectUISPr("fert.produzieren.direktdrucken"));

					Object[] params = { nurproduzierbare, direktDrucken };
					int nAnswer = JOptionPane.showConfirmDialog(this, params, LPMain.getTextRespectUISPr("lp.frage"),
							JOptionPane.OK_CANCEL_OPTION);
					nurproduzierbare.isSelected();

					if (nAnswer == JOptionPane.OK_OPTION) {

						Object[] losIIdsSelektiert = panelQueryProduzieren.getSelectedIds();

						if (nurproduzierbare.isSelected()) {

							// filtern
							losIIdsSelektiert = DelegateFactory.getInstance().getFertigungDelegate()
									.nurProduzierbareLose(losIIdsSelektiert);

						}

						ArrayList<Integer> losIIds = DelegateFactory.getInstance().getFertigungDelegate()
								.gebeSelektierteLoseAus(losIIdsSelektiert);
						panelQueryProduzieren.eventYouAreSelected(false);

						// PJ19803 Fertigungsbegleitscheine drucken

						if (losIIds != null) {

							for (int i = losIIds.size() - 1; i >= 0; i--) {

								LosDto lDto = DelegateFactory.getInstance().getFertigungDelegate()
										.losFindByPrimaryKey(losIIds.get(i));

								if (direktDrucken.isSelected()) {

									// Aktivieren
									PanelReportKriterien krit = null;
									try {
										// DruckPanel instantiieren
										krit = new PanelReportKriterien(getInternalFrame(),
												new ReportFertigungsbegleitschein(getInternalFrame(), lDto.getIId(),
														""),
												"", null, null, false, false, false);
										// jetzt das tatsaechliche Drucken
										krit.druckeArchiviereUndSetzeVersandstatusEinesBelegs();
									} catch (JRException ex) {
										int iChoice = JOptionPane.YES_OPTION;
										myLogger.error(ex.getLocalizedMessage(), ex);
										String sMsg = LPMain.getTextRespectUISPr(
												"lp.drucken.reportkonntenichtgedrucktwerden") + " " + lDto.getCNr();
										Object[] options = { LPMain.getTextRespectUISPr("lp.druckerror.wiederholen"),
												LPMain.getTextRespectUISPr("lp.druckerror.\u00FCberspringen"),
												LPMain.getTextRespectUISPr("lp.abbrechen"), };
										iChoice = DialogFactory.showModalDialog(this.getInternalFrame(), sMsg,
												LPMain.getTextRespectUISPr("lp.error"), options, options[0]);
										if (iChoice == 0) {
											Thread.sleep(5000);
											krit.druckeArchiviereUndSetzeVersandstatusEinesBelegs();
										} else if (iChoice == 2) {

											break;
										}
									} catch (Throwable ex) {
										//
									}

								} else {

									getInternalFrameFertigung().getTabbedPaneLos().printFertigungsbegleitschein(lDto);
								}
							}
						}
					}

				}
			}

		} else if ((e.getID() == ItemChangedEvent.ACTION_NEW) || (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW)) {
			// btnnew: einen neuen machen.

			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

			if (e.getSource() == panelQueryOffeneAGs) {
				// goto AG

				if (sAspectInfo.endsWith(EXTRA_GOTO_AG)) {

					if (panelQueryOffeneAGs.getSelectedId() != null) {

						LossollarbeitsplanDto saDto = DelegateFactory.getInstance().getFertigungDelegate()
								.lossollarbeitsplanFindByPrimaryKey((Integer) panelQueryOffeneAGs.getSelectedId());

						FilterKriterium[] kriterien = new FilterKriterium[1];
						kriterien[0] = new FilterKriterium("flrlos.i_id", true, saDto.getLosIId() + "",
								FilterKriterium.OPERATOR_EQUAL, false);

						getInternalFrameFertigung().geheZu(InternalFrameFertigung.IDX_TABBED_PANE_LOS,
								getInternalFrameFertigung().getTabbedPaneLos().IDX_ARBEITSPLAN, saDto.getLosIId(),
								saDto.getIId(), kriterien);

					}
				} else if (sAspectInfo.endsWith(ACTION_SPECIAL_TOGGLE_AG_FERTIG)) {

					if (panelQueryOffeneAGs.getSelectedIds() != null) {

						for (int i = 0; i < panelQueryOffeneAGs.getSelectedIds().length; i++) {
							DelegateFactory.getInstance().getFertigungDelegate()
									.toggleArbeitsplanFertig((Integer) panelQueryOffeneAGs.getSelectedIds()[i]);

						}
						panelQueryOffeneAGs.eventYouAreSelected(false);

					}

				} else if (sAspectInfo.endsWith(ACTION_SPECIAL_FERTIGUNG_STARTEN)) {

					if (panelQueryOffeneAGs.getSelectedIds() != null) {

						panelQueryFLRPersonal = PersonalFilterFactory.getInstance()
								.createPanelFLRPersonalMitKostenstelleSichtbarkeit(getInternalFrame(), true, false,
										personalIId_ZuletztAusgewaehlt != null ? personalIId_ZuletztAusgewaehlt
												: LPMain.getTheClient().getIDPersonal());
						new DialogQuery(panelQueryFLRPersonal);

					}

				}

			} else if (e.getSource() == panelQueryProduzieren) {
				if (sAspectInfo.endsWith(EXTRA_PROFIRST_EXPORT)) {
					if (panelQueryProduzieren.getSelectedIds().length > 0) {

						// Parameter fuer Pfad

						ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate()
								.getMandantparameter(LPMain.getInstance().getTheClient().getMandant(),
										ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PRO_FIRST_VERZEICHNIS);
						String proFirstPfad = parameter.getCWert();
						if (proFirstPfad != null && proFirstPfad.trim().length() > 0) {

							File file = new File(proFirstPfad + "/" + "OAG_"
									+ Helper.formatTimestamp(new Timestamp(System.currentTimeMillis()),
											LPMain.getTheClient().getLocUi()).replace('.', '-').replace(' ', '_')
											.replace(':', '-')
									+ ".csv");

							if (file.exists()) {
								int erg = JOptionPane.showConfirmDialog(LPMain.getInstance().getDesktop(),
										"Die Datei " + file.getName() + " existiert bereits.\n"
												+ "Wollen Sie sie \u00FCberschreiben?",
										"Editor: Warnung", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
								if (erg == JOptionPane.NO_OPTION)
									return;
							}

							LPCSVWriter writer;
							try {
								writer = new LPCSVWriter(new FileWriter(file), LPCSVWriter.DEFAULT_SEPARATOR,
										LPCSVWriter.NO_QUOTE_CHARACTER);

								ArrayList<String[]> al = DelegateFactory.getInstance().getFertigungDelegate()
										.getSelektierteAGsForProfirst(panelQueryProduzieren.getSelectedIds());

								for (int i = 0; i < al.size(); i++) {
									writer.writeNext(al.get(i));
								}

								writer.close();

								DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"),
										LPMain.getTextRespectUISPr("system.extraliste.gespeichert") + " ("
												+ file.getAbsolutePath() + ") ");

							} catch (java.io.FileNotFoundException ex) {
								// ev. Datei gerade verwendet?
								DialogFactory.showModalDialog("Fehler", "Die angegeben Datei '" + file.getAbsolutePath()
										+ "' konnte nicht erstellt werden. M\u00F6glicherweise wird sie durch ein anderes Programm verwendet.");

							}
						} else {
							// Pfad nicht definiert
							DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("artikel.profistverzeichnis.nichtdefiniert"));
						}

					}
				} else if (sAspectInfo.endsWith(EXTRA_GOTO_LOS)) {

					if (panelQueryProduzieren.getSelectedId() != null) {

						LosDto lDto = DelegateFactory.getInstance().getFertigungDelegate()
								.losFindByPrimaryKey((Integer) panelQueryProduzieren.getSelectedId());

						FilterKriterium[] kriterien = new FilterKriterium[1];
						kriterien[0] = new FilterKriterium("flrlos.i_id", true, lDto.getIId() + "",
								FilterKriterium.OPERATOR_EQUAL, false);

						getInternalFrameFertigung().geheZu(InternalFrameFertigung.IDX_TABBED_PANE_LOS,
								getInternalFrameFertigung().getTabbedPaneLos().IDX_AUSWAHL, lDto.getIId(), null,
								kriterien);

					}
				}
			} else if (e.getSource() == panelQueryReihen) {
				if (sAspectInfo.endsWith(ACTION_SPECIAL_MASCHINE_REIHEN)) {

					DelegateFactory.getInstance().getFertigungDelegate().offenAgsNachAGBeginnReihen(
							(Integer) panelQueryReihen.getWcoFilter().getKeyOfSelectedItem());

					panelQueryReihen.eventYouAreSelected(false);

				}
				
				if (sAspectInfo.endsWith(EXTRA_GOTO_AG)) {

					if (panelQueryReihen.getSelectedId() != null) {

						LossollarbeitsplanDto saDto = DelegateFactory.getInstance().getFertigungDelegate()
								.lossollarbeitsplanFindByPrimaryKey((Integer) panelQueryReihen.getSelectedId());

						FilterKriterium[] kriterien = new FilterKriterium[1];
						kriterien[0] = new FilterKriterium("flrlos.i_id", true, saDto.getLosIId() + "",
								FilterKriterium.OPERATOR_EQUAL, false);

						getInternalFrameFertigung().geheZu(InternalFrameFertigung.IDX_TABBED_PANE_LOS,
								getInternalFrameFertigung().getTabbedPaneLos().IDX_ARBEITSPLAN, saDto.getLosIId(),
								saDto.getIId(), kriterien);

					}
				}  else if (sAspectInfo.endsWith(ACTION_SPECIAL_TOGGLE_AG_FERTIG)) {

					if (panelQueryReihen.getSelectedIds() != null) {

						for (int i = 0; i < panelQueryReihen.getSelectedIds().length; i++) {
							DelegateFactory.getInstance().getFertigungDelegate()
									.toggleArbeitsplanFertig((Integer) panelQueryReihen.getSelectedIds()[i]);

						}
						panelQueryReihen.eventYouAreSelected(false);

					}

				}
				
			}

		}

		else if (e.getID() == ItemChangedEvent.ACTION_TABLE_SELECTION_CHANGED) {
			if (e.getSource() == panelQueryOffeneAGs) {
				if (wcbReihen.isSelected()) {
					if (panelQueryOffeneAGs.getCurrentSortierKriterien() != null
							&& vergleicheSortReihung(panelQueryOffeneAGs.getCurrentSortierKriterien(),
									getAlSortFuerReihung()) == true) {

					} else {
						wcbReihen.setSelected(false);
						plusMinusButtonsSchalten(false);
					}
				}
			} else if (e.getSource() == panelQueryProduzieren) {

				//

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1
				|| e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == panelQueryOffeneAGs) {
				int iPos = panelQueryOffeneAGs.getTable().getSelectedRow();

				Integer iIdPosition = (Integer) panelQueryOffeneAGs.getSelectedId();

				if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
					DelegateFactory.getInstance().getFertigungDelegate().offenAgsUmreihen(iIdPosition, false);
				} else {
					DelegateFactory.getInstance().getFertigungDelegate().offenAgsUmreihen(iIdPosition, true);
				}

				// die Liste neu anzeigen und den richtigen Datensatz
				// markieren

				panelQueryOffeneAGs.setSelectedId(iIdPosition);

				panelQueryOffeneAGs.setCurrentSortierKriterien(getAlSortFuerReihung());
				panelQueryOffeneAGs.eventYouAreSelected(false);

			} else if (e.getSource() == panelQueryReihen) {

				if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
					int iPos = panelQueryReihen.getTable().getSelectedRow();

					// wenn die Position nicht die erste ist
					if (iPos > 0) {
						Integer iIdPosition = (Integer) panelQueryReihen.getSelectedId();

						if ("control".equals(panelQueryReihen.getAspect())) {
							DelegateFactory.getInstance().getFertigungDelegate()
								.offenenAgAlsErstenReihen((Integer) panelQueryReihen.getWcoFilter().getKeyOfSelectedItem(),
										iIdPosition);							
						} else {
						
							Integer iIdPositionMinus1 = (Integer) panelQueryReihen.getTable().getValueAt(iPos - 1, 0);
							DelegateFactory.getInstance().getFertigungDelegate()
									.vertauscheLossollarbeitsplanReihung(iIdPosition, iIdPositionMinus1);
						}
						
						// die Liste neu anzeigen und den richtigen Datensatz
						// markieren
						panelQueryReihen.setSelectedId(iIdPosition);
					}
				} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
					int iPos = panelQueryReihen.getTable().getSelectedRow();

					// wenn die Position nicht die letzte ist
					if (iPos < panelQueryReihen.getTable().getRowCount() - 1) {
						Integer iIdPosition = (Integer) panelQueryReihen.getSelectedId();

						Integer iIdPositionPlus1 = (Integer) panelQueryReihen.getTable().getValueAt(iPos + 1, 0);
						DelegateFactory.getInstance().getFertigungDelegate()
								.vertauscheLossollarbeitsplanReihung(iIdPosition, iIdPositionPlus1);

						// die Liste neu anzeigen und den richtigen Datensatz
						// markieren
						panelQueryReihen.setSelectedId(iIdPosition);
					}
				}

			}
		}

	}

	private boolean vergleicheSortReihung(ArrayList<SortierKriterium> al1, ArrayList<SortierKriterium> al2) {

		if (al1.size() == al2.size()) {

			for (int i = 0; i < al1.size(); i++) {

				if (!al1.get(i).kritName.equals(al2.get(i).kritName)) {
					return false;
				}

			}
			return true;

		} else {
			return false;
		}

	}

	private void refreshTitle() {
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("fert.offeneags"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryOffeneAGs.eventYouAreSelected(false);

			panelQueryOffeneAGs.updateButtons();
		} else if (selectedIndex == IDX_PANEL_PRODUZIEREN) {
			createProduzieren();

			// panelQueryProduzieren.eventYouAreSelected(false);

			panelQueryProduzieren.updateButtons();

			AbstractButton refreshBtn = panelQueryProduzieren.getHmOfButtons().get(PanelBasis.ACTION_REFRESH)
					.getButton();
			refreshBtn.setOpaque(true);
			refreshBtn.setBackground(Color.blue);

		} else if (selectedIndex == IDX_PANEL_REIHEN) {
			createReihen();

			panelQueryReihen.setSortierbar(false);

			panelQueryReihen.setCurrentSortierKriterien(getAlSortFuerMaschinenReihung());

			panelQueryReihen.eventYouAreSelected(false);

			panelQueryReihen.updateButtons();

		}

		refreshTitle();
	}

	private String sortReihung1 = "flrmaschine.c_identifikationsnr";
	private String sortReihung2 = "flroffeneags.t_agbeginn";
	private String sortReihung3 = "flroffeneags.i_maschinenversatz_ms";
	private String sortReihung4 = "flroffeneags.flrlos.c_nr";

	private ArrayList<SortierKriterium> getAlSortFuerReihung() {

		ArrayList<SortierKriterium> alSortFuerReihung = new ArrayList<SortierKriterium>();

		SortierKriterium sk1 = new SortierKriterium(sortReihung1, true, "ASC");
		alSortFuerReihung.add(sk1);
		SortierKriterium sk2 = new SortierKriterium(sortReihung2, true, "ASC");
		alSortFuerReihung.add(sk2);
		SortierKriterium sk3 = new SortierKriterium(sortReihung3, true, "ASC");
		alSortFuerReihung.add(sk3);
		SortierKriterium sk4 = new SortierKriterium(sortReihung4, true, "ASC");
		alSortFuerReihung.add(sk4);

		return alSortFuerReihung;
	}

	private String sortMaschinenReihung1 = "flroffeneags.flrlossollarbeitsplan.i_reihung";
	private String sortMaschinenReihung2 = "flroffeneags.t_agbeginn";
	private String sortMaschinenReihung3 = "flroffeneags.i_maschinenversatz_ms";
	private String sortMaschinenReihung4 = "flroffeneags.flrlos.c_nr";

	private ArrayList<SortierKriterium> getAlSortFuerMaschinenReihung() {

		ArrayList<SortierKriterium> alSortFuerReihung = new ArrayList<SortierKriterium>();

		SortierKriterium sk1 = new SortierKriterium(sortMaschinenReihung1, true, "ASC");
		alSortFuerReihung.add(sk1);
		SortierKriterium sk2 = new SortierKriterium(sortMaschinenReihung2, true, "ASC");
		alSortFuerReihung.add(sk2);
		SortierKriterium sk3 = new SortierKriterium(sortMaschinenReihung3, true, "ASC");
		alSortFuerReihung.add(sk3);
		SortierKriterium sk4 = new SortierKriterium(sortMaschinenReihung4, true, "ASC");
		alSortFuerReihung.add(sk4);

		return alSortFuerReihung;
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_ACTION_MASCHINE_MATERIAL)) {
			String add2Title = "";
			getInternalFrame()
					.showReportKriterien(new ReportMaschineUndMaterial(getInternalFrameFertigung(), add2Title));

		} else if (e.getActionCommand().equals(MENUE_ACTION_TAETIGKEIT_AGBEGINN)) {
			String add2Title = "";
			getInternalFrame()
					.showReportKriterien(new ReportTaetigkeitAGBeginn(getInternalFrameFertigung(), add2Title));

		} else if (e.getSource().equals(wcbReihen)) {

			if (wcbReihen.isSelected()) {
				// PJ18715 Nach Maschine/AG-Beginn/VersatzMS/Losnr sortieren und
				// Pfeil auf/ab anzeigen

				panelQueryOffeneAGs.setCurrentSortierKriterien(getAlSortFuerReihung());
				panelQueryOffeneAGs.eventYouAreSelected(false);
				plusMinusButtonsSchalten(true);

			} else {
				plusMinusButtonsSchalten(false);
			}

		}

	}

	public void plusMinusButtonsSchalten(boolean bEnabled) {
		switchPlusMinusButtons(panelQueryOffeneAGs, bEnabled);
/*		
		LPButtonAction minus = panelQueryOffeneAGs.getHmOfButtons().get(PanelBasis.ACTION_POSITION_VONNNACHNMINUS1);
		LPButtonAction plus = panelQueryOffeneAGs.getHmOfButtons().get(PanelBasis.ACTION_POSITION_VONNNACHNPLUS1);
		minus.getButton().setEnabled(bEnabled);
		plus.getButton().setEnabled(bEnabled);
		minus.getButton().setVisible(bEnabled);
		plus.getButton().setVisible(bEnabled);
*/
	}

	private void switchPlusMinusButtons(PanelQuery panel, boolean beEnabled) {
		myLogger.warn("switchPlusMinusButtons queryPanel, " + beEnabled);
		switchButtonAction(panel.getHmOfButtons()
				.get(PanelBasis.ACTION_POSITION_VONNNACHNMINUS1), beEnabled);
		switchButtonAction(panel.getHmOfButtons()
				.get(PanelBasis.ACTION_POSITION_VONNNACHNPLUS1), beEnabled);
	}
	
	private void switchButtonAction(LPButtonAction hvButton, boolean beEnabled) {
		hvButton.getButton().setEnabled(beEnabled);
		hvButton.getButton().setVisible(beEnabled);
	}
	
	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		setName("TabbedPaneOffeneAG");
		if (wrapperMenuBar == null) {

			wrapperMenuBar = new WrapperMenuBar(this);

			JMenuItem menuItemMaschineMaterial = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("fert.report.maschineundmaterial"));

			menuItemMaschineMaterial.addActionListener(this);

			menuItemMaschineMaterial.setActionCommand(MENUE_ACTION_MASCHINE_MATERIAL);
			JMenu jmJournal = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_JOURNAL);
			jmJournal.add(menuItemMaschineMaterial);

			JMenuItem menuItemTaetigkeitAGBeginn = new JMenuItem(
					LPMain.getInstance().getTextRespectUISPr("fert.report.taetigkeittplusagbeginn"));

			menuItemTaetigkeitAGBeginn.addActionListener(this);

			menuItemTaetigkeitAGBeginn.setActionCommand(MENUE_ACTION_TAETIGKEIT_AGBEGINN);

			jmJournal.add(menuItemTaetigkeitAGBeginn);
		}

		return wrapperMenuBar;

	}

}
