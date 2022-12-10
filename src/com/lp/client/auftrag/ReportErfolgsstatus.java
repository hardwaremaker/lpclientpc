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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportErfolgsstatus extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperDateField wdfVon = new WrapperDateField();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();

	private WrapperLabel wlaSortierung = new WrapperLabel();
	private WrapperRadioButton wrbAuftragsnummer = new WrapperRadioButton();
	private WrapperRadioButton wrbKunde = new WrapperRadioButton();
	private WrapperRadioButton wrbProjektleiter = new WrapperRadioButton();
	private ButtonGroup buttonGroupSortierung = new ButtonGroup();

	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperRadioButton wrbAuftragdatum = new WrapperRadioButton();
	private WrapperRadioButton wrbErledigtDatum = new WrapperRadioButton();
	private WrapperRadioButton wrbLieferterminBisZum = new WrapperRadioButton();
	private ButtonGroup buttonGroupDatum = new ButtonGroup();

	private WrapperLabel wlaProjekt = new WrapperLabel();
	private WrapperButton wbuKunde = new WrapperButton();
	private WrapperTextField wtfKunde = new WrapperTextField();
	private WrapperTextField wtfProjekt = new WrapperTextField();
	private WrapperButton wbuProjekt = null;

	private WrapperButton wbuAuftrag = new WrapperButton();
	private WrapperTextField wtfAuftrag = new WrapperTextField();
	
	protected JPanel jpaWorkingOn = new JPanel();

	private WrapperDateRangeController wdrBereich = null;

	private Integer kundeIId = null;
	private PanelQueryFLR panelQueryFLRProjekt = null;
	private Integer projektIId = null;

	static final public String ACTION_SPECIAL_KUNDE_FROM_LISTE = "ACTION_SPECIAL_KUNDE_FROM_LISTE";
	private final static String ACTION_SPECIAL_PROJEKT = "action_special_projekt";
	static final public String ACTION_SPECIAL_AUFTRAG_FROM_LISTE = "action_auftrag_auftrag_liste";

	private PanelQueryFLR panelQueryFLRKunde = null;
	private PanelQueryFLR panelQueryFLRPartnerliste = null;
	private PanelQueryFLR panelQueryFLRAuftrag = null;
	
	private ArrayList<Integer> auftragIId = new ArrayList<Integer>();
	

	public ReportErfolgsstatus(InternalFrame internalFrame, String add2Title)
			throws Throwable {
		super(internalFrame, add2Title);
		jbInit();
		wdrBereich.doClickDown();
		wdrBereich.doClickUp();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	void dialogQueryProjektFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRProjekt = ProjektFilterFactory.getInstance()
				.createPanelFLRProjekt(getInternalFrame(), null, true);

		new DialogQuery(panelQueryFLRProjekt);

	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);
		jpaWorkingOn.setLayout(new GridBagLayout());
		wlaVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));
		wlaSortierung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.sortierung"));
		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);
		wdrBereich = new WrapperDateRangeController(wdfVon, wdfBis);

		boolean bProjektklammer = LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER);

		wlaDatum.setText(LPMain.getInstance().getTextRespectUISPr("lp.datum"));
		wrbAuftragdatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.belegdatum"));
		wrbErledigtDatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.erledigungsdatum"));
		wrbLieferterminBisZum.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.auftragstatstik.lieferterminbiszum"));
		buttonGroupDatum.add(wrbAuftragdatum);
		buttonGroupDatum.add(wrbErledigtDatum);
		buttonGroupDatum.add(wrbLieferterminBisZum);

		wrbAuftragdatum.addActionListener(this);
		wrbErledigtDatum.addActionListener(this);
		wrbLieferterminBisZum.addActionListener(this);

		wrbAuftragdatum.setSelected(true);

		wlaProjekt.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.projekt"));
		wbuKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde"));
		wrbAuftragsnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.auftragnummer"));

		wrbProjektleiter.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.auftragstatstik.sortierung.projektleiter"));
		wrbKunde.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kunde.modulname"));

		wbuKunde.setActionCommand(ACTION_SPECIAL_KUNDE_FROM_LISTE);
		wbuKunde.addActionListener(this);

		buttonGroupSortierung.add(wrbAuftragsnummer);
		buttonGroupSortierung.add(wrbKunde);
		buttonGroupSortierung.add(wrbProjektleiter);

		wtfKunde.setActivatable(false);

		wrbAuftragsnummer.setSelected(true);

		wbuProjekt = new WrapperButton(
				LPMain.getTextRespectUISPr("auft.report.projekte"));
		wbuProjekt.setActionCommand(ACTION_SPECIAL_PROJEKT);
		wbuProjekt.addActionListener(this);

		wbuAuftrag.setText(LPMain.getInstance().getTextRespectUISPr(
				"auft.modulname.tooltip")
				+ "...");

		wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG_FROM_LISTE);
		wbuAuftrag.addActionListener(this);
	
		wtfAuftrag.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wtfAuftrag.setActivatable(false);
		
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		int iZeile = 0;

		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaDatum, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbAuftragdatum, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbErledigtDatum, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbLieferterminBisZum, new GridBagConstraints(3,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbAuftragsnummer, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		if (bProjektklammer == true) {
			wtfProjekt.setActivatable(false);
			jpaWorkingOn.add(wbuProjekt,
					new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		} else {
			jpaWorkingOn.add(wlaProjekt,
					new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}

		jpaWorkingOn.add(wtfProjekt, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbKunde, new GridBagConstraints(3, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuKunde, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbProjektleiter, new GridBagConstraints(3, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuAuftrag, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAuftrag, new GridBagConstraints(1, iZeile, 3, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_FROM_LISTE)) {
			dialogQueryKunde(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PROJEKT)) {
			dialogQueryProjektFromListe(e);
		}else if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG_FROM_LISTE)) {
			FilterKriterium[] fk = LieferscheinFilterFactory.getInstance()
					.createFKPanelQueryFLRAuftragAuswahl(false);
			panelQueryFLRAuftrag = AuftragFilterFactory.getInstance()
					.createPanelFLRAuftrag(getInternalFrame(), true, true, fk);
			panelQueryFLRAuftrag.setMultipleRowSelectionEnabled(true);
			panelQueryFLRAuftrag.addButtonAuswahlBestaetigen(null);
			new DialogQuery(panelQueryFLRAuftrag);

		} 

		if (e.getSource().equals(wrbAuftragdatum)
				|| e.getSource().equals(wrbErledigtDatum)
				|| e.getSource().equals(wrbLieferterminBisZum)) {
			if (wrbLieferterminBisZum.isSelected()) {
				wdfVon.setEnabled(false);
			} else {
				wdfVon.setEnabled(true);
			}
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKunde) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				KundeDto kundeDto = DelegateFactory.getInstance()
						.getKundeDelegate().kundeFindByPrimaryKey(iId);
				kundeIId = kundeDto.getIId();
				wtfKunde.setText(kundeDto.getPartnerDto()
						.formatFixTitelName1Name2());
			} else if (e.getSource() == panelQueryFLRProjekt) {
				Integer key = (Integer) panelQueryFLRProjekt.getSelectedId();
				if (key != null) {

					ProjektDto pDto = DelegateFactory.getInstance()
							.getProjektDelegate().projektFindByPrimaryKey(key);

					projektIId = key;
					wtfProjekt.setText(pDto.getCNr());
				}
			}else if (e.getSource() == panelQueryFLRAuftrag) {
				Object[] o = panelQueryFLRAuftrag.getSelectedIds();

				String auftraege = "";

				if (wtfAuftrag.getText() != null) {
					auftraege = wtfAuftrag.getText();
				}
				for (int i = 0; i < o.length; i++) {
					AuftragDto losDto = DelegateFactory.getInstance()
							.getAuftragDelegate()
							.auftragFindByPrimaryKey((Integer) o[i]);
					auftraege += losDto.getCNr() + ", ";

					auftragIId.add(losDto.getIId());

				}
				wtfAuftrag.setText(auftraege);
				if(panelQueryFLRAuftrag.getDialog()!=null){
					panelQueryFLRAuftrag.getDialog().setVisible(false);
				}
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRKunde) {
				kundeIId = null;
				wtfKunde.setText(null);
			} else if (e.getSource() == panelQueryFLRProjekt) {
				kundeIId = null;
				wtfProjekt.setText(null);
			}if (e.getSource() == panelQueryFLRAuftrag) {
				auftragIId = new ArrayList<Integer>();
				wtfAuftrag.setText("");
			}
		}
	}

	private void dialogQueryKunde(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance()
				.createPanelFLRKunde(getInternalFrame(), false, true, kundeIId);

		new DialogQuery(panelQueryFLRKunde);
	}

	public String getModul() {
		return AuftragReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return AuftragReportFac.REPORT_ERFOLGSSTATUS;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		JasperPrintLP jasperPrint = null;

		int iOptionsortierung = -1;
		if (wrbKunde.isSelected()) {
			iOptionsortierung = AuftragReportFac.REPORT_ERFOLGSSTATUS_SORTIERUNG_KUNDE;
		} else if (wrbAuftragsnummer.isSelected()) {
			iOptionsortierung = AuftragReportFac.REPORT_ERFOLGSSTATUS_SORTIERUNG_AUFTRAG;
		} else if (wrbProjektleiter.isSelected()) {
			iOptionsortierung = AuftragReportFac.REPORT_ERFOLGSSTATUS_SORTIERUNG_PROJEKTLEITER;
		}
		int iOptionDatum = 1;
		if (wrbAuftragdatum.isSelected()) {
			iOptionDatum = AuftragReportFac.REPORT_AUFTRAGSTATISTIK_OPTION_DATUM_BELEGDATUM;
		} else if (wrbErledigtDatum.isSelected()) {
			iOptionDatum = AuftragReportFac.REPORT_AUFTRAGSTATISTIK_OPTION_DATUM_ERLEDIGUNGSDATUM;
		} else if (wrbLieferterminBisZum.isSelected()) {
			iOptionDatum = AuftragReportFac.REPORT_AUFTRAGSTATISTIK_OPTION_DATUM_LIEFERTERMIN_BIS_ZUM;
		}

		jasperPrint = DelegateFactory
				.getInstance()
				.getAuftragReportDelegate()
				.printErfolgsstatus(wdrBereich.getDatumsfilterVonBis(),
						iOptionDatum, kundeIId, projektIId, iOptionsortierung,auftragIId);

		return jasperPrint;

	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
