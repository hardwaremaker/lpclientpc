
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerReportFac;
import com.lp.server.artikel.service.VkPreisfindungFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ReportJournalBewegungsvorschau extends PanelBasis implements PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperTextField wtfFilterVon = new WrapperTextField();
	private WrapperTextField wtfFilterBis = new WrapperTextField();

	private WrapperLabel wlaStandort = new WrapperLabel();
	private WrapperComboBox wcbStandort = new WrapperComboBox();
	private WrapperCheckBox wcbMitRahmen = new WrapperCheckBox();
	private WrapperCheckBox wcbArtikelOhneBewegungsvorschauAusblenden = new WrapperCheckBox();
	private WrapperCheckBox wcbVersteckte = null;

	Integer artikelgruppeIId = null;

	private PanelQueryFLR panelQueryFLRArtikelklasse = null;
	Integer artikelklasseIId = null;

	private WrapperButton wbuArtikelgruppe = new WrapperButton();
	private WrapperTextField wtfArtikelgruppe = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRArtikel_Von = null;
	private PanelQueryFLR panelQueryFLRArtikel_Bis = null;

	private WrapperButton wbuArtikelnrVon = new WrapperButton();
	private WrapperButton wbuArtikelnrBis = new WrapperButton();

	static final public String ACTION_SPECIAL_ARTIKELVON_FROM_LISTE = "ACTION_SPECIAL_ARTIKELVON_FROM_LISTE";
	static final public String ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE = "ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE";
	static final public String ACTION_SPECIAL_ARTIKELGRUPPE_FROM_LISTE = "action_artikelgruppe_from_liste";
	static final private String ACTION_SPECIAL_FLR_ARTIKELKLASSE = "ACTION_SPECIAL_FLR_ARTIKELKLASSE";

	private WrapperButton wbuArtikelklasseFLR = null;
	private WrapperTextField wtfArtikelklasse = null;

	private PanelQueryFLR panelQueryFLRArtikelgruppe = null;

	public ReportJournalBewegungsvorschau(InternalFrameArtikel internalFrame, String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getInstance().getTextRespectUISPr("bes.bewegungsvorschau");
		jbInit();
		initComponents();

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELVON_FROM_LISTE)) {
			dialogQueryArtikelFromListe_Von(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE)) {
			dialogQueryArtikelFromListe_Bis(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELGRUPPE_FROM_LISTE)) {
			dialogQueryArtikelgruppeFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_ARTIKELKLASSE)) {
			panelQueryFLRArtikelklasse = ArtikelFilterFactory.getInstance()
					.createPanelFLRArtikelklasse(getInternalFrame(), artikelklasseIId);
			new DialogQuery(panelQueryFLRArtikelklasse);

		}
	}

	private void dialogQueryArtikelgruppeFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRArtikelgruppe = ArtikelFilterFactory.getInstance().createPanelFLRArtikelgruppe(getInternalFrame(),
				artikelgruppeIId);
		new DialogQuery(panelQueryFLRArtikelgruppe);
	}

	void dialogQueryArtikelFromListe_Von(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel_Von = ArtikelFilterFactory.getInstance().createPanelFLRArtikel(getInternalFrame(), null,
				true);

		new DialogQuery(panelQueryFLRArtikel_Von);
	}

	void dialogQueryArtikelFromListe_Bis(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel_Bis = ArtikelFilterFactory.getInstance().createPanelFLRArtikel(getInternalFrame(), null,
				true);

		new DialogQuery(panelQueryFLRArtikel_Bis);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRArtikelgruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtgruDto artgruDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artgruFindByPrimaryKey((Integer) key);
				wtfArtikelgruppe.setText(artgruDto.getBezeichnung());
				artikelgruppeIId = artgruDto.getIId();
			} else if (e.getSource() == panelQueryFLRArtikelklasse) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtklaDto artklaDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artklaFindByPrimaryKey((Integer) key);
				wtfArtikelklasse.setText(artklaDto.getBezeichnung());
				artikelklasseIId = (Integer) key;

			} else if (e.getSource() == panelQueryFLRArtikel_Von) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				wtfFilterVon.setText(artikelDto.getCNr());
			} else if (e.getSource() == panelQueryFLRArtikel_Bis) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				wtfFilterBis.setText(artikelDto.getCNr());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRArtikelgruppe) {

				wtfArtikelgruppe.setText(null);

				artikelgruppeIId = null;
			} else if (e.getSource() == panelQueryFLRArtikel_Von) {

				wtfFilterVon.setText(null);
			} else if (e.getSource() == panelQueryFLRArtikel_Bis) {

				wtfFilterBis.setText(null);
			} else if (e.getSource() == panelQueryFLRArtikelklasse) {
				artikelklasseIId = null;
				wtfArtikelklasse.setText("");
			}
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfFilterVon;
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);

		wlaStandort = new WrapperLabel(LPMain.getTextRespectUISPr("system.standort"));
		wcbStandort.setMap(DelegateFactory.getInstance().getLagerDelegate().getAlleStandorte());

		wcbMitRahmen.setText(LPMain.getTextRespectUISPr("artikel.bewegungsvorschau.rahmenberuecksichtigen"));

		wcbArtikelOhneBewegungsvorschauAusblenden
				.setText(LPMain.getTextRespectUISPr("artikel.bewegungsvorschau.ohnebewegungausblenden"));
		wcbVersteckte = new WrapperCheckBox(LPMain.getTextRespectUISPr("lp.versteckte"));

		wbuArtikelgruppe.setText(LPMain.getTextRespectUISPr("lp.artikelgruppe") + "...");
		wbuArtikelgruppe.setActionCommand(PanelArtikel.ACTION_SPECIAL_ARTIKELGRUPPE_FROM_LISTE);
		wbuArtikelgruppe.addActionListener(this);
		wtfArtikelgruppe.setActivatable(false);

		wbuArtikelnrVon.setText(LPMain.getInstance().getTextRespectUISPr("artikel.artikelnummer") + " "
				+ LPMain.getInstance().getTextRespectUISPr("lp.von"));
		wbuArtikelnrBis.setText(LPMain.getInstance().getTextRespectUISPr("artikel.artikelnummer") + " "
				+ LPMain.getInstance().getTextRespectUISPr("lp.bis"));

		wbuArtikelnrVon.setActionCommand(this.ACTION_SPECIAL_ARTIKELVON_FROM_LISTE);
		wbuArtikelnrVon.addActionListener(this);

		wbuArtikelnrBis.setActionCommand(this.ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE);
		wbuArtikelnrBis.addActionListener(this);

		
		wbuArtikelklasseFLR = new WrapperButton();
		wbuArtikelklasseFLR.setText(LPMain.getInstance().getTextRespectUISPr("button.artikelklasse"));
		wbuArtikelklasseFLR.setMandatoryField(true);
		wbuArtikelklasseFLR.setActionCommand(ACTION_SPECIAL_FLR_ARTIKELKLASSE);
		wbuArtikelklasseFLR.addActionListener(this);

	wtfArtikelklasse = new WrapperTextField(VkPreisfindungFac.MAX_CNR);
		wtfArtikelklasse.setActivatable(false);
		
		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wbuArtikelgruppe, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfArtikelgruppe, new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		
		jpaWorkingOn.add(wbuArtikelklasseFLR, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfArtikelklasse, new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		
		if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
			jpaWorkingOn.add(wcbVersteckte, new GridBagConstraints(3, iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		}

		iZeile++;

		jpaWorkingOn.add(wbuArtikelnrVon, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFilterVon, new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuArtikelnrBis, new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFilterBis, new GridBagConstraints(4, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
				ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);
		boolean lagerminJeLager = ((Boolean) parameter.getCWertAsObject());

		if (lagerminJeLager) {
			jpaWorkingOn.add(wcbStandort, new GridBagConstraints(2, 3, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wlaStandort, new GridBagConstraints(0, 3, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		}
		iZeile++;
		jpaWorkingOn.add(wcbMitRahmen, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbArtikelOhneBewegungsvorschauAusblenden, new GridBagConstraints(3, 4, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public String getModul() {
		return ArtikelReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ArtikelReportFac.REPORT_BEWEGUNGSVORSCHAU_ALLE;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		return DelegateFactory.getInstance().getArtikelReportDelegate().printBewegungsvorschauAlle(artikelgruppeIId,
				artikelklasseIId, wtfFilterVon.getText(), wtfFilterBis.getText(),
				(Integer) wcbStandort.getKeyOfSelectedItem(), wcbMitRahmen.isSelected(),
				wcbArtikelOhneBewegungsvorschauAusblenden.isSelected(), wcbVersteckte.isSelected());
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
