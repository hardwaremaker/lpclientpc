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
package com.lp.client.partner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.LagerReportFac;
import com.lp.server.artikel.service.VkPreisfindungFac;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportKundenpreisliste extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	static final private String ACTION_SPECIAL_FLR_ARTIKELGRUPPE = "ACTION_SPECIAL_FLR_ARTIKELGRUPPE";
	static final private String ACTION_SPECIAL_FLR_ARTIKELKLASSE = "ACTION_SPECIAL_FLR_ARTIKELKLASSE";

	private WrapperSelectField wsfShopgruppe = new WrapperSelectField(
			WrapperSelectField.SHOPGRUPPE, getInternalFrame(), true);
	
	private WrapperButton wbuArtikelgruppeFLR = null;
	private WrapperTextField wtfArtikelgruppe = null;
	private WrapperButton wbuArtikelklasseFLR = null;
	private WrapperTextField wtfArtikelklasse = null;

	private WrapperLabel wlaPreisgueltigkeit = new WrapperLabel();

	private WrapperCheckBox wcbMitGesperrte = null;
	private WrapperCheckBox wcbVersteckte = null;
	private WrapperCheckBox wcbNurSonderkonditionen = null;
	private WrapperCheckBox wcbMitMandantensprache = null;

	private Integer artikelIId_Von = null;
	private Integer artikelIId_Bis = null;

	private WrapperButton wbuArtikelnrVon = new WrapperButton();
	private WrapperDateField wdfPreisgueltigkeit = new WrapperDateField();
	private WrapperButton wbuArtikelnrBis = new WrapperButton();
	private WrapperTextField wtfArtikelnrVon = new WrapperTextField();
	private WrapperTextField wtfArtikelnrBis = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRArtikel_Von = null;
	private PanelQueryFLR panelQueryFLRArtikel_Bis = null;
	static final public String ACTION_SPECIAL_ARTIKELVON_FROM_LISTE = "ACTION_SPECIAL_ARTIKELVON_FROM_LISTE";
	static final public String ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE = "ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE";

	private PanelQueryFLR panelQueryFLRArtikelgruppe = null;
	private PanelQueryFLR panelQueryFLRArtikelklasse = null;
	Integer preislisteIId = null;
	Integer artikelgruppeIId = null;
	Integer artikelklasseIId = null;
	InternalFrameKunde internalFrameKunde=null;

	public ReportKundenpreisliste(InternalFrameKunde internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		internalFrameKunde=internalFrame;
		LPMain.getInstance().getTextRespectUISPr("artikel.report.preisliste");
		jbInit();
		initComponents();

	}

	void dialogQueryArtikelFromListe_Von(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel_Von = ArtikelFilterFactory
				.getInstance()
				.createPanelFLRArtikel(getInternalFrame(), artikelIId_Von, true);

		new DialogQuery(panelQueryFLRArtikel_Von);
	}

	void dialogQueryArtikelFromListe_Bis(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel_Bis = ArtikelFilterFactory
				.getInstance()
				.createPanelFLRArtikel(getInternalFrame(), artikelIId_Bis, true);

		new DialogQuery(panelQueryFLRArtikel_Bis);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_ARTIKELGRUPPE)) {
			panelQueryFLRArtikelgruppe = ArtikelFilterFactory.getInstance()
					.createPanelFLRArtikelgruppe(getInternalFrame(),
							artikelgruppeIId);
			new DialogQuery(panelQueryFLRArtikelgruppe);

		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_FLR_ARTIKELKLASSE)) {
			panelQueryFLRArtikelklasse = ArtikelFilterFactory.getInstance()
					.createPanelFLRArtikelklasse(getInternalFrame(),
							artikelklasseIId);
			new DialogQuery(panelQueryFLRArtikelklasse);

		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ARTIKELVON_FROM_LISTE)) {
			dialogQueryArtikelFromListe_Von(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE)) {
			dialogQueryArtikelFromListe_Bis(e);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRArtikelgruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtgruDto artgruDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artgruFindByPrimaryKey((Integer) key);
				wtfArtikelgruppe.setText(artgruDto.getCNr());
				artikelgruppeIId = (Integer) key;
			} else if (e.getSource() == panelQueryFLRArtikelklasse) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtklaDto artklaDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artklaFindByPrimaryKey((Integer) key);
				wtfArtikelklasse.setText(artklaDto.getCNr());
				artikelklasseIId = (Integer) key;

			} else if (e.getSource() == panelQueryFLRArtikel_Von) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				artikelIId_Von = artikelDto.getIId();
				wtfArtikelnrVon.setText(artikelDto.getCNr());
			} else if (e.getSource() == panelQueryFLRArtikel_Bis) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				artikelIId_Bis = artikelDto.getIId();
				wtfArtikelnrBis.setText(artikelDto.getCNr());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRArtikelgruppe) {
				artikelgruppeIId = null;
				wtfArtikelgruppe.setText("");
			} else if (e.getSource() == panelQueryFLRArtikelklasse) {
				artikelklasseIId = null;
				wtfArtikelklasse.setText("");
			} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
				if (e.getSource() == panelQueryFLRArtikel_Von) {
					wtfArtikelnrVon.setText(null);
					artikelIId_Von = null;
				} else if (e.getSource() == panelQueryFLRArtikel_Bis) {
					wtfArtikelnrBis.setText(null);
					artikelIId_Bis = null;
				}
			}

		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	private void jbInit() throws Throwable {

		wbuArtikelnrVon.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelnummer")
				+ " " + LPMain.getInstance().getTextRespectUISPr("lp.von"));

		wlaPreisgueltigkeit.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.preisliste.preisgueltigkeit"));

		wbuArtikelnrVon.setActionCommand(ACTION_SPECIAL_ARTIKELVON_FROM_LISTE);
		wbuArtikelnrVon.addActionListener(this);

		wbuArtikelnrBis.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelnummer")
				+ " " + LPMain.getInstance().getTextRespectUISPr("lp.bis"));

		wdfPreisgueltigkeit.setMandatoryField(true);
		wdfPreisgueltigkeit.setDate(new java.util.Date());

		wbuArtikelnrBis.setActionCommand(ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE);
		wbuArtikelnrBis.addActionListener(this);

		wbuArtikelgruppeFLR = new WrapperButton();
		wbuArtikelgruppeFLR.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.artikelgruppe"));
		wbuArtikelgruppeFLR.setMandatoryField(true);
		wbuArtikelgruppeFLR.setActionCommand(ACTION_SPECIAL_FLR_ARTIKELGRUPPE);
		wbuArtikelgruppeFLR.addActionListener(this);

		wcbVersteckte = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("lp.versteckte"));
		wcbMitGesperrte = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("lp.mitgesperrten"));
		wcbNurSonderkonditionen = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("kunde.preisliste.nursonderkonditionen"));
		wcbMitMandantensprache = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("kunde.preisliste.mitmandantensprache"));

		wbuArtikelklasseFLR = new WrapperButton();
		wbuArtikelklasseFLR.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.artikelklasse"));
		wbuArtikelklasseFLR.setMandatoryField(true);
		wbuArtikelklasseFLR.setActionCommand(ACTION_SPECIAL_FLR_ARTIKELKLASSE);
		wbuArtikelklasseFLR.addActionListener(this);

		wtfArtikelgruppe = new WrapperTextField(VkPreisfindungFac.MAX_CNR);
		wtfArtikelgruppe.setActivatable(false);
		wtfArtikelklasse = new WrapperTextField(VkPreisfindungFac.MAX_CNR);
		wtfArtikelklasse.setActivatable(false);

		getInternalFrame().addItemChangedListener(this);
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaPreisgueltigkeit, new GridBagConstraints(1, 0, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 80, 0));
		jpaWorkingOn.add(wdfPreisgueltigkeit, new GridBagConstraints(2, 0, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbMitGesperrte, new GridBagConstraints(3, 0, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wbuArtikelgruppeFLR, new GridBagConstraints(0, 1, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikelgruppe, new GridBagConstraints(1, 1, 2, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
			jpaWorkingOn.add(wcbVersteckte, new GridBagConstraints(3, 1, 2, 1,
					0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2), 0, 0));
		}

		jpaWorkingOn.add(wcbNurSonderkonditionen, new GridBagConstraints(3, 2,
				2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	

		jpaWorkingOn.add(wbuArtikelklasseFLR, new GridBagConstraints(0, 2, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikelklasse, new GridBagConstraints(1, 2, 2, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuArtikelnrVon, new GridBagConstraints(0, 3, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 80, 0));
		jpaWorkingOn.add(wtfArtikelnrVon, new GridBagConstraints(1, 3, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuArtikelnrBis, new GridBagConstraints(2, 3, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 80, 0));
		jpaWorkingOn.add(wtfArtikelnrBis, new GridBagConstraints(3, 3, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wcbMitMandantensprache, new GridBagConstraints(3, 4,
				2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		
		jpaWorkingOn.add(wsfShopgruppe.getWrapperButton(), new GridBagConstraints(0, 4, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfShopgruppe.getWrapperTextField(), new GridBagConstraints(1, 4, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		

	}

	public String getModul() {
		return KundeReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return KundeReportFac.REPORT_KUNDENPREISLISTE;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		return DelegateFactory
				.getInstance()
				.getKundeDelegate()
				.printKundenpreisliste(internalFrameKunde.getKundeDto().getIId(), artikelgruppeIId, artikelklasseIId,
						wcbMitGesperrte.isSelected(), wtfArtikelnrVon.getText(),
						wtfArtikelnrBis.getText(), wcbVersteckte.isSelected(),
						wdfPreisgueltigkeit.getDate(),
						wcbNurSonderkonditionen.isSelected(),wcbMitMandantensprache.isSelected(), wsfShopgruppe.getIKey());
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
