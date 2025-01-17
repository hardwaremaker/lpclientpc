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
package com.lp.client.auftrag;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.EventObject;
import java.util.LinkedHashMap;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportJournalVerkauf;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportAuftragOffene2 extends PanelReportJournalVerkauf implements
		PanelReportIfJRDS {
	public enum OffeneReportType {
		OFFENE,
		OFFENE_OHNE_DETAIL;
	}
	private static final long serialVersionUID = 1L;
	/**
	 * @todo den Vertreter in die Superklasse. warum bauen wir immer alles 17
	 *       mal????
	 */
	private final static String ACTION_SPECIAL_SORTIERUNG_VERTRETER = "action_special_sortierung_vertreter";
	private final static String ACTION_SPECIAL_VERTRETER_AUSWAHL = "action_special_vertreter_auswahl";
	private final static String ACTION_SPECIAL_VERTRETER_ALLE = "action_special_vertreter_alle";
	private final static String ACTION_SPECIAL_VERTRETER_EINER = "action_special_vertreter_einer";

	private WrapperLabel wlaStichtag = new WrapperLabel();
	private WrapperDateField wdfStichtag = new WrapperDateField();

	private WrapperCheckBox wcbInternenKommentarDrucken = new WrapperCheckBox();
	private WrapperCheckBox wcbMitDetails = new WrapperCheckBox();
	private WrapperCheckBox wcbSortiereNachLiefertermin = new WrapperCheckBox();
	private WrapperRadioButton wrbSortierungVertreter = new WrapperRadioButton();
	private WrapperButton wbuVertreter = null;
	private WrapperTextField wtfVertreter = null;
	private ButtonGroup jbgVertreter = new ButtonGroup();
	private WrapperRadioButton wrbVertreterAlle = new WrapperRadioButton();
	private WrapperRadioButton wrbVertreterEiner = new WrapperRadioButton();
	private WrapperCheckBox wcbMitAngelegten = null;

	private JPanel jPanelVertreter = null;
	private PanelQueryFLR panelQueryFLRVertreter = null;
	private PersonalDto vertreterDto = null;

	private WrapperComboBox wcoArt;
	private WrapperComboBox wcoArtUnverbindlich;

	private ButtonGroup jbgStichtag = new ButtonGroup();
	private WrapperRadioButton wrbStichtagErledigt = new WrapperRadioButton();
	private WrapperRadioButton wrbStichtagLiefertermin = new WrapperRadioButton();

	public ReportAuftragOffene2(InternalFrame internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);
		jbInit();
		setDefaults();
		initComponents();
	}

	protected void jbInit() throws Exception {

		wcbMitAngelegten = new WrapperCheckBox();
		wcbMitAngelegten.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.offenedetails.mitangelegten"));

		wrbSortierungVertreter = new WrapperRadioButton(LPMain.getInstance()
				.getTextRespectUISPr("label.vertreter"));
		wrbSortierungVertreter
				.setActionCommand(ACTION_SPECIAL_SORTIERUNG_VERTRETER);
		wrbSortierungVertreter.addActionListener(this);
		buttonGroupSortierung.add(wrbSortierungVertreter);

		wrbVertreterAlle.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.alle"));
		wrbVertreterAlle.setActionCommand(ACTION_SPECIAL_VERTRETER_ALLE);
		wrbVertreterAlle.addActionListener(this);

		wrbVertreterEiner.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.einer"));
		wrbVertreterEiner.setActionCommand(ACTION_SPECIAL_VERTRETER_EINER);
		wrbVertreterEiner.addActionListener(this);

		jbgVertreter = new ButtonGroup();
		jbgVertreter.add(wrbVertreterAlle);
		jbgVertreter.add(wrbVertreterEiner);

		wbuVertreter = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("button.vertreter"));
		wbuVertreter.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.vertreter.tooltip"));
		wbuVertreter.setActionCommand(ACTION_SPECIAL_VERTRETER_AUSWAHL);
		wbuVertreter.addActionListener(this);

		wbuVertreter.setMinimumSize(new Dimension(BREITE_BUTTONS, Defaults
				.getInstance().getControlHeight()));
		wbuVertreter.setPreferredSize(new Dimension(BREITE_BUTTONS, Defaults
				.getInstance().getControlHeight()));
		wtfVertreter = new WrapperTextField();
		wtfVertreter.setEditable(false);
		wtfVertreter.setMinimumSize(new Dimension(50, Defaults.getInstance()
				.getControlHeight()));
		wtfVertreter.setPreferredSize(new Dimension(50, Defaults.getInstance()
				.getControlHeight()));

		wlaStichtag.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.stichtag"));
		wdfStichtag.setMandatoryField(true);
		wdfStichtag.setDate(new Date(System.currentTimeMillis()));
		wcbInternenKommentarDrucken.setText(LPMain.getInstance()
				.getTextRespectUISPr("auft.internenkommentarmitdrucken"));
		wcbMitDetails.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.offenemitdetaildrucken"));
		wcbMitDetails.addActionListener(this);
		wcbSortiereNachLiefertermin.setText(LPMain.getInstance()
				.getTextRespectUISPr("auft.sortierungnachliefertermin"));

		
		wrbStichtagErledigt.setText(LPMain.getInstance()
				.getTextRespectUISPr("auft.report.offene.stichtag.erledigungsdatum"));
		wrbStichtagLiefertermin.setText(LPMain.getInstance()
				.getTextRespectUISPr("auft.report.offene.stichtag.liefertermin"));
		
		jbgStichtag.add(wrbStichtagErledigt);
		jbgStichtag.add(wrbStichtagLiefertermin);
		//PJ21042
		wrbStichtagLiefertermin.setSelected(true);

		LinkedHashMap<Integer, String> hm = new LinkedHashMap<Integer, String>();
		hm.put(1,
				LPMain.getTextRespectUISPr("auft.journal.ohnerahmenauftraege")); // ohne
																					// Rahmenauftraege,
																					// d.h.
																					// Abrufe
																					// und
																					// freie
																					// Auftraege
		hm.put(2, LPMain.getTextRespectUISPr("auft.journal.nurrahmenauftraege")); // nur
																					// Rahmenauftraege
		wcoArt = new WrapperComboBox();
		wcoArt.setMandatoryField(true);
		wcoArt.setKeyOfSelectedItem(0);
		wcoArt.setMap(hm);

		LinkedHashMap<Integer, String> hmUnverbindlich = new LinkedHashMap<Integer, String>();
		hmUnverbindlich
				.put(AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_ALLE,
						LPMain.getTextRespectUISPr("auftrag.report.offene.unverbindliche.option1"));
		hmUnverbindlich
				.put(AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_NUR_UNVERBINDLICHE,
						LPMain.getTextRespectUISPr("auftrag.report.offene.unverbindliche.option2"));
		hmUnverbindlich
				.put(AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_OHNE_UNVERBINDLICHE,
						LPMain.getTextRespectUISPr("auftrag.report.offene.unverbindliche.option3"));

		wcoArtUnverbindlich = new WrapperComboBox();
		wcoArtUnverbindlich.setMandatoryField(true);
		wcoArtUnverbindlich.setKeyOfSelectedItem(0);
		wcoArtUnverbindlich.setMap(hmUnverbindlich);
		
		HelperClient.setMinimumAndPreferredSize(wcbMitAngelegten, HelperClient.getSizeFactoredDimension(200));
		HelperClient.setMinimumAndPreferredSize(wcoArtUnverbindlich, HelperClient.getSizeFactoredDimension(230));

		iZeile++;
		jpaWorkingOn.add(wrbSortierungVertreter, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbVertreterAlle, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbVertreterEiner, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(getPanelVertreter(), new GridBagConstraints(2, iZeile,
				4, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaStichtag, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfStichtag, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 4, 0));
		jpaWorkingOn.add(wrbStichtagErledigt, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbStichtagLiefertermin, new GridBagConstraints(3, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbInternenKommentarDrucken, new GridBagConstraints(0,
				iZeile, 6, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbMitDetails, new GridBagConstraints(0, iZeile, 6, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbSortiereNachLiefertermin, new GridBagConstraints(0,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbMitAngelegten, new GridBagConstraints(2, iZeile, 2,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcoArt, new GridBagConstraints(0, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoArtUnverbindlich, new GridBagConstraints(2, iZeile,
				2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

		this.setEinschraenkungDatumBelegnummerSichtbar(false);
		wrbSortierungLieferadresse.setVisible(true);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);

		if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER_EINER)) {
			// wenn noch keiner gewaehlt ist, dann geht der Dialog auf
			if (vertreterDto.getIId() == null && isInitialized()) {
				wbuVertreter.doClick();
			}

			wbuVertreter.setVisible(true);
			wtfVertreter.setVisible(true);
			wtfVertreter.setMandatoryField(true);
		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_VERTRETER_AUSWAHL)) {
			dialogQueryVertreter(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER_ALLE)) {
			wbuVertreter.setVisible(false);
			wtfVertreter.setVisible(false);
			wtfVertreter.setMandatoryField(false);

			vertreterDto = new PersonalDto();
		}

		// SP2939
		if (e.getSource().equals(wcbMitDetails)) {

			if (getParent() instanceof JPanel) {
				JPanel jpa = (JPanel) getParent();

				if (jpa.getParent() instanceof JSplitPane) {
					JSplitPane jsp = (JSplitPane) jpa.getParent();
					if (jsp.getParent() instanceof JPanel) {
						JPanel jpaworkingon = (JPanel) jsp.getParent();
						if (jpaworkingon.getParent() instanceof PanelReportKriterien) {
							PanelReportKriterien panelReportKriterien = (PanelReportKriterien) jpaworkingon
									.getParent();

							wcbMitDetails.removeActionListener(this);

							panelReportKriterien.refreshVarianten();

							wcbMitDetails.addActionListener(this);

						}
					}

				}

			}
		}

	}

	private void setDefaults() {
		vertreterDto = new PersonalDto();

		// alle Vertreter
		wrbVertreterAlle.setSelected(true);
		wbuVertreter.setVisible(false);
		wtfVertreter.setVisible(false);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRVertreter) {

				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				if (key != null) {
					vertreterDto = DelegateFactory.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);

					wtfVertreter.setText(vertreterDto.getPartnerDto()
							.formatFixName2Name1());
				}
			}
		}
	}

	private void dialogQueryVertreter(ActionEvent e) throws Throwable {
		panelQueryFLRVertreter = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonal(getInternalFrame(), true, false,
						vertreterDto.getIId());

		new DialogQuery(panelQueryFLRVertreter);
	}

	private JPanel getPanelVertreter() {
		if (jPanelVertreter == null) {
			jPanelVertreter = new JPanel(new GridBagLayout());

			jPanelVertreter.add(wbuVertreter, new GridBagConstraints(0, 0, 1,
					1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jPanelVertreter.add(wtfVertreter, new GridBagConstraints(1, 0, 1,
					1, 1.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		return jPanelVertreter;
	}

	public String getModul() {
		return AuftragReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		if (wcbMitDetails.isSelected()) {
			return AuftragReportFac.REPORT_AUFTRAG_OFFENE;
		} else {
			return AuftragReportFac.REPORT_AUFTRAG_OFFENE_OHNE_DETAILS;
		}
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected ReportJournalKriterienDto getKriterien() {
		ReportJournalKriterienDto krit = new ReportJournalKriterienDto();
		befuelleKriterien(krit);
		if (wrbSortierungVertreter.isSelected()) {
			krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER;

		}

		if (wrbVertreterAlle.isSelected()) {
			krit.vertreterIId = null;
		} else {
			krit.vertreterIId = vertreterDto.getIId();
		}

		return krit;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		if (wcbMitDetails.isSelected()) {
			return DelegateFactory
					.getInstance()
					.getAuftragReportDelegate()
					.printAuftragOffene(
							getKriterien(),
							wdfStichtag.getDate(),
							new Boolean(wcbInternenKommentarDrucken
									.isSelected()),
							(Integer) wcoArt.getKeyOfSelectedItem(),
							(Integer) wcoArtUnverbindlich
									.getKeyOfSelectedItem(),
							wcbMitAngelegten.isSelected(),
							wrbStichtagLiefertermin.isSelected());
		} else {
			return DelegateFactory
					.getInstance()
					.getAuftragReportDelegate()
					.printAuftragOffeneOhneDetail(
							getKriterien(),
							wdfStichtag.getDate(),
							new Boolean(wcbSortiereNachLiefertermin
									.isSelected()),
							new Boolean(wcbInternenKommentarDrucken
									.isSelected()),
							(Integer) wcoArt.getKeyOfSelectedItem(),
							(Integer) wcoArtUnverbindlich
									.getKeyOfSelectedItem(),
							wcbMitAngelegten.isSelected(),
							wrbStichtagLiefertermin.isSelected());
		}

	}
}
