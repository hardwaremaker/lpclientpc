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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.LinkedHashMap;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse kuemmert sich um den Druck der Aufloesbaren Fehlmengen
 * Liste</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Martin Bluehweis; 19.01.06</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2010/02/15 14:00:14 $
 */
public class ReportFehlmengenAllerLose extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jpaWorkingOn = null;

	private ArrayList<Integer> losIId = new ArrayList<Integer>();

	private Integer fertigungsgruppenIId = null;

	private WrapperComboBox wcoArt;
	private WrapperCheckBox wcbOhneBestellteArtikel = new WrapperCheckBox();
	private WrapperCheckBox wcbNurDringendeArtikel = new WrapperCheckBox();

	private WrapperLabel wlaSortierung = new WrapperLabel();
	private WrapperRadioButton wrbSortArtikelnr = new WrapperRadioButton();
	private WrapperRadioButton wrbSortBelegnummer = new WrapperRadioButton();
	private WrapperRadioButton wrbSortBeginntermin = new WrapperRadioButton();

	private WrapperCheckBox wcbAlleAusserEigengefertigteArtikel = null;
	
	private WrapperButton wbuFertigungsgruppe = null;
	private WrapperTextField wtfFertigungsgruppe = null;

	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "action_auftrag_los_liste";

	static final public String ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE = "action_special_fertigungsgruppe_from_liste";

	private ButtonGroup buttonGroupSortierung = new ButtonGroup();

	private WrapperButton wbuLos = new WrapperButton();
	private WrapperTextField wtfLos = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRLos = null;

	private PanelQueryFLR panelQueryFLRFertigungsgruppe = null;

	public ReportFehlmengenAllerLose(InternalFrame internalFrame, String sAdd2Title) throws Throwable {
		super(internalFrame, sAdd2Title);
		jbInit();
		setDefaults();
		initComponents();
	}

	private void dialogQueryFertigungsgruppeFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRFertigungsgruppe = StuecklisteFilterFactory.getInstance()
				.createPanelFLRFertigungsgruppe(getInternalFrame(), null, true);

		new DialogQuery(panelQueryFLRFertigungsgruppe);
	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLos = FertigungFilterFactory.getInstance().createPanelFLRBebuchbareLose(getInternalFrame(), false,
				true, true, null, true);
		panelQueryFLRLos.setMultipleRowSelectionEnabled(true);
		panelQueryFLRLos.addButtonAuswahlBestaetigen(null);
		new DialogQuery(panelQueryFLRLos);

	}

	public void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLos) {
				Object[] o = panelQueryFLRLos.getSelectedIds();

				String lose = "";

				if (wtfLos.getText() != null) {
					lose = wtfLos.getText();
				}
				for (int i = 0; i < o.length; i++) {
					LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
							.losFindByPrimaryKey((Integer) o[i]);
					lose += losDto.getCNr() + ", ";

					losIId.add(losDto.getIId());

				}
				wtfLos.setText(lose);
				if (panelQueryFLRLos.dialog != null) {
					panelQueryFLRLos.dialog.setVisible(false);
				}
			} else if (e.getSource() == panelQueryFLRFertigungsgruppe) {
				Object o = panelQueryFLRFertigungsgruppe.getSelectedId();

				FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory.getInstance().getStuecklisteDelegate()
						.fertigungsgruppeFindByPrimaryKey((Integer) o);

				fertigungsgruppenIId=fertigungsgruppeDto.getIId();
				
				wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRLos) {
				losIId = new ArrayList<Integer>();
				wtfLos.setText("");
			} else if (e.getSource() == panelQueryFLRFertigungsgruppe) {
				fertigungsgruppenIId = null;
				wtfFertigungsgruppe.setText(null);
			}
		}
	}

	private void jbInit() {
		jpaWorkingOn = new JPanel(new GridBagLayout());

		LinkedHashMap<Integer, String> hm = new LinkedHashMap<Integer, String>();
		hm.put(FertigungReportFac.FEHLMENGEN_ALLER_LOSE_OPTION_STUECKLISTEN_ALLE,
				LPMain.getTextRespectUISPr("fert.report.fehlmengenallerlose.art.option.alle")); // Alle
		hm.put(FertigungReportFac.FEHLMENGEN_ALLER_LOSE_OPTION_STUECKLISTEN_NUR_EIGENGEFERTIGTE,
				LPMain.getTextRespectUISPr("fert.report.fehlmengenallerlose.art.option.nureigengefertigte"));
		hm.put(FertigungReportFac.FEHLMENGEN_ALLER_LOSE_OPTION_STUECKLISTEN_NUR_FREMDGEFERTIGTE,
				LPMain.getTextRespectUISPr("fert.report.fehlmengenallerlose.art.option.nurfremdgefertigte"));
		wcoArt = new WrapperComboBox();
		wcoArt.setMandatoryField(true);
		wcoArt.setKeyOfSelectedItem(0);
		wcoArt.setMap(hm);

		wcbAlleAusserEigengefertigteArtikel = new WrapperCheckBox();
		wcbAlleAusserEigengefertigteArtikel
				.setText(LPMain
						.getInstance()
						.getTextRespectUISPr(
								"fert.report.fehlmengenallerlose.alleaussereigengefertigte"));

		wcbAlleAusserEigengefertigteArtikel.setSelected(true);

		
		wbuLos.setText(LPMain.getInstance().getTextRespectUISPr("auft.title.panel.lose") + "...");

		wbuLos.setActionCommand(ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLos.addActionListener(this);
		wtfLos.setActivatable(false);
		wtfLos.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);

		wcbOhneBestellteArtikel.setText(LPMain.getTextRespectUISPr("fert.fehlmengenallerlose.ohnebestellte"));
		wrbSortArtikelnr.setText(LPMain.getTextRespectUISPr("fert.fehlmengenallerlose.sort.artikel"));
		wrbSortArtikelnr.addActionListener(this);
		wrbSortBeginntermin.setText(LPMain.getTextRespectUISPr("fert.fehlmengenallerlose.sort.beginntermin"));
		wrbSortBeginntermin.addActionListener(this);
		wrbSortBelegnummer.setText(LPMain.getTextRespectUISPr("fert.fehlmengenallerlose.sort.belegnummer"));
		wrbSortArtikelnr.addActionListener(this);

		wcbNurDringendeArtikel.setText(LPMain.getTextRespectUISPr("fert.los.fehlmengenallerlose.nurdringende"));

		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));

		buttonGroupSortierung.add(wrbSortArtikelnr);
		buttonGroupSortierung.add(wrbSortBeginntermin);
		buttonGroupSortierung.add(wrbSortBelegnummer);
		wrbSortArtikelnr.setSelected(true);

		
		wbuFertigungsgruppe = new WrapperButton();
		wbuFertigungsgruppe.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.fertigungsgruppe")
				+ "...");
		wbuFertigungsgruppe.setActionCommand(this.ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE);
		wbuFertigungsgruppe.addActionListener(this);
		wtfFertigungsgruppe = new WrapperTextField();
		wtfFertigungsgruppe.setActivatable(false);

		getInternalFrame().addItemChangedListener(this);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 150, 0));

		jpaWorkingOn.add(wrbSortArtikelnr, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 200, 0));

		jpaWorkingOn.add(wcbAlleAusserEigengefertigteArtikel, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 300, 0));

		iZeile++;

		jpaWorkingOn.add(wcoArt, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbSortBeginntermin, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbOhneBestellteArtikel, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbSortBelegnummer, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		
		jpaWorkingOn.add(wbuFertigungsgruppe, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFertigungsgruppe, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbNurDringendeArtikel, new GridBagConstraints(2, iZeile, 1,
		  1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new
		  Insets(2, 2, 2, 2), 0, 0));
		 

		iZeile++;
		jpaWorkingOn.add(wbuLos, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfLos, new GridBagConstraints(1, iZeile, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_LOS_FROM_LISTE)) {
			dialogQueryLosFromListe(e);
		}
		if (e.getActionCommand().equals(ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE)) {
			dialogQueryFertigungsgruppeFromListe(e);
		}

	}

	private void setDefaults() {
		// Default Sortierung nach Ident

	}

	public String getModul() {
		return FertigungReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FertigungReportFac.REPORT_FEHLMENGEN_ALLER_LOSE;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		int iSortierung = FertigungReportFac.FEHLMENGEN_ALLER_LOSE_OPTION_SORTIERUNG_ARTIKELNUMMER;

		if (wrbSortBeginntermin.isSelected()) {
			iSortierung = FertigungReportFac.FEHLMENGEN_ALLER_LOSE_OPTION_SORTIERUNG_BEGINNTERMIN;
		} else if (wrbSortBelegnummer.isSelected()) {
			iSortierung = FertigungReportFac.FEHLMENGEN_ALLER_LOSE_OPTION_SORTIERUNG_BELEGNUMMER;
		}

		return DelegateFactory.getInstance().getFertigungDelegate().printFehlmengenAllerLose(wcbAlleAusserEigengefertigteArtikel.isSelected(),
				(int) wcoArt.getKeyOfSelectedItem(), wcbOhneBestellteArtikel.isSelected(), losIId, iSortierung,
				wcbNurDringendeArtikel.isSelected(), fertigungsgruppenIId);
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}
}
