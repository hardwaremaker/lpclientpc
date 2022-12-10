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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelTabelle;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.fertigung.service.InternebestellungFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

public class PanelTabelleBewegungsvorschau extends PanelTabelle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArtikelDto artikelDto = null;

	// das soll in der optionalen zweiten Zeile stehen
	private WrapperLabel wlaEmpty = null;
	private WrapperLabel wlaIstBestand = null;
	private WrapperLabel wlaSollBestand = null;
	private WrapperLabel wlaMindestBestand = null;
	private WrapperLabel wlaUeberproduktion = null;

	private WrapperLabel wlaKritAuswertung = null;

	private WrapperLabel wlaStandort = null;
	private WrapperComboBox wcbStandort = null;
	private JCheckBox wcbMitRahmen = null;

	boolean lagerminjelager = false;

	/**
	 * PanelTabelle.
	 * 
	 * @param iUsecaseIdI
	 *            die eindeutige UseCase ID
	 * @param sTitelTabbedPaneI
	 *            der Titel des aktuellen TabbedPane
	 * @param oInternalFrameI
	 *            der uebergeordente InternalFrame
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public PanelTabelleBewegungsvorschau(int iUsecaseIdI,
			String sTitelTabbedPaneI, InternalFrame oInternalFrameI)
			throws Throwable {
		super(iUsecaseIdI, sTitelTabbedPaneI, oInternalFrameI);
		jbInitPanel();
		initComponents();
	}

	public void aktiviereStandort() {
		wcbStandort.setEnabled(true);
		wcbMitRahmen.setEnabled(true);
	}

	/**
	 * Initialisiere alle Komponenten; braucht der JBX-Designer;
	 * 
	 * @throws Exception
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	private void jbInitPanel() throws Throwable {

		wlaKritAuswertung = new WrapperLabel();

		wlaEmpty = new WrapperLabel();
		wlaEmpty.setMaximumSize(new Dimension(SPALTENBREITE_ZEILENHEADER,
				Defaults.getInstance().getControlHeight()));
		wlaEmpty.setMinimumSize(new Dimension(SPALTENBREITE_ZEILENHEADER,
				Defaults.getInstance().getControlHeight()));
		wlaEmpty.setPreferredSize(new Dimension(SPALTENBREITE_ZEILENHEADER,
				Defaults.getInstance().getControlHeight()));

		wlaIstBestand = new WrapperLabel();
		wlaSollBestand = new WrapperLabel();
		wlaMindestBestand = new WrapperLabel();
		wlaUeberproduktion = new WrapperLabel();

		wlaStandort = new WrapperLabel(
				LPMain.getTextRespectUISPr("system.standort"));

		
		wcbMitRahmen = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("artikel.bewegungsvorschau.rahmenberuecksichtigen"));

		wcbStandort = new WrapperComboBox();
		wcbStandort.setMap(DelegateFactory.getInstance().getLagerDelegate()
				.getAlleStandorte());
		wcbStandort.addActionListener(this);

		ParametermandantDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);
		lagerminjelager = (Boolean) parameter.getCWertAsObject();
		if (lagerminjelager) {
			getPanelOptionaleZweiteZeile().add(
					wlaStandort,
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.0,
							GridBagConstraints.WEST, GridBagConstraints.BOTH,
							new Insets(2, 2, 2, 2), 0, 0));
			getPanelOptionaleZweiteZeile().add(
					wcbStandort,
					new GridBagConstraints(4, 0, 1, 1, 0.1, 0.0,
							GridBagConstraints.WEST, GridBagConstraints.BOTH,
							new Insets(2, 2, 2, 2), 0, 0));
		}

		getPanelOptionaleZweiteZeile().add(
				wlaIstBestand,
				new GridBagConstraints(1, 1, 1, 1, 0.1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		getPanelOptionaleZweiteZeile().add(
				wlaSollBestand,
				new GridBagConstraints(2, 1, 1, 1, 0.1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		getPanelOptionaleZweiteZeile().add(
				wlaMindestBestand,
				new GridBagConstraints(3, 1, 1, 1, 0.1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		getPanelOptionaleZweiteZeile().add(
				wlaUeberproduktion,
				new GridBagConstraints(4, 1, 1, 1, 0.1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		setFirstColumnVisible(false);
		
		HelperClient.setMinimumAndPreferredSize(wcbMitRahmen, HelperClient.getSizeFactoredDimension(180));
		wcbMitRahmen.setEnabled(true);
		getToolBar().getToolsPanelLeft()
		.add(wcbMitRahmen);
		
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		artikelDto = DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.artikelFindByPrimaryKey(
						Integer.valueOf(getDefaultFilter()[0].value));
		// PJ18849
		
		boolean bInternebestellung=true;
		
		if(getDefaultFilter().length>2){
			if(getDefaultFilter()[2].kritName.equals(InternebestellungFac.KRIT_INTERNEBESTELLUNG)){
				bInternebestellung=new Boolean( getDefaultFilter()[2].value);
			}
		}
		
		if(getDefaultFilter().length>2){
			if(getDefaultFilter()[2].kritName.equals(InternebestellungFac.KRIT_INTERNEBESTELLUNG)){
				bInternebestellung=new Boolean( getDefaultFilter()[2].value);
			}
		}
		if(getDefaultFilter().length>2){
			if(getDefaultFilter()[3].kritName.equals(InternebestellungFac.KRIT_MIT_RAHMEN)){
				getDefaultFilter()[3].value =new Boolean(wcbMitRahmen.isSelected()).toString();
			}
		}
		
		if (lagerminjelager) {
			setDefaultFilter(FertigungFilterFactory.getInstance()
					.createFKBewegungsvorschau(artikelDto.getIId(), bInternebestellung,wcbMitRahmen.isSelected(),
							(Integer) wcbStandort.getKeyOfSelectedItem()));
		}

		super.eventActionRefresh(e, bNeedNoRefreshI);

		wlaKritAuswertung.setText(LPMain.getTextRespectUISPr("lp.artikel")
				+ ": " + artikelDto.formatArtikelbezeichnung());
		wlaKritAuswertung.setMaximumSize(new Dimension(350, Defaults
				.getInstance().getControlHeight()));
		wlaKritAuswertung.setMinimumSize(new Dimension(350, Defaults
				.getInstance().getControlHeight()));
		wlaKritAuswertung.setPreferredSize(new Dimension(350, Defaults
				.getInstance().getControlHeight()));
		wlaKritAuswertung.setHorizontalAlignment(SwingConstants.LEADING);

		getPanelFilterKriterien().add(wlaKritAuswertung);

		java.math.BigDecimal lagerstand = null;
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)
				&& !LPMain
						.getInstance()
						.getDesktop()
						.darfAnwenderAufZusatzfunktionZugreifen(
								MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER)) {

			lagerstand = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.getLagerstandAllerLagerAllerMandanten(artikelDto.getIId(),
							true);

		} else {

			lagerstand = DelegateFactory.getInstance().getLagerDelegate()
					.getLagerstandAllerLagerEinesMandanten(artikelDto.getIId());
		}

		wlaIstBestand.setText(LPMain.getTextRespectUISPr("lp.lagerstand")
				+ ": "
				+ Helper.formatZahl(lagerstand, 2,LPMain.getTheClient()
						.getLocUi()));
		wlaIstBestand.setHorizontalAlignment(SwingConstants.CENTER);

		if (lagerminjelager == false) {
			wlaSollBestand.setText(LPMain
					.getTextRespectUISPr("artikel.lagersollstand")
					+ ":"
					+ " "
					+ ((artikelDto.getFLagersoll() != null) ? Helper
							.formatZahl(artikelDto.getFLagersoll(),2, LPMain
									.getTheClient().getLocUi()) : "-"));

			wlaMindestBestand.setText(LPMain
					.getTextRespectUISPr("artikel.lagermindeststand")
					+ ":"
					+ " "
					+ ((artikelDto.getFLagermindest() != null) ? Helper
							.formatZahl(artikelDto.getFLagermindest(),2, LPMain
									.getTheClient().getLocUi()) : "-"));
		} else {

			BigDecimal[] bd = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.getSummeLagermindesUndLagerSollstandEinesStandorts(
							artikelDto.getIId(),
							(Integer) wcbStandort.getKeyOfSelectedItem());

			wlaSollBestand.setText(LPMain
					.getTextRespectUISPr("artikel.lagersollstand")
					+ ":"
					+ " "
					+   Helper
					.formatZahl(bd[1],2, LPMain
							.getTheClient().getLocUi()));

			wlaMindestBestand.setText(LPMain
					.getTextRespectUISPr("artikel.lagermindeststand")
					+ ":"
					+ " " +  Helper
					.formatZahl(bd[0],2, LPMain
							.getTheClient().getLocUi()));

			if ((Integer) wcbStandort.getKeyOfSelectedItem() != null) {

				BigDecimal lagerstandeinesStandorts = BigDecimal.ZERO;

				LagerDto[] lagerDtosEinesStandorts = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.lagerFindByPartnerIIdStandortMandantCNr(
								(Integer) wcbStandort.getKeyOfSelectedItem(), true);
				for (int i = 0; i < lagerDtosEinesStandorts.length; i++) {
					BigDecimal lagerstandTemp = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getLagerstand(artikelDto.getIId(),
									lagerDtosEinesStandorts[i].getIId());
					lagerstandeinesStandorts = lagerstandeinesStandorts
							.add(lagerstandTemp);

				}

				wlaIstBestand.setText(LPMain
						.getTextRespectUISPr("lp.lagerstand")
						+ ": "
						+ Helper.formatZahl(lagerstandeinesStandorts, 2,LPMain.getTheClient()
								.getLocUi()));

			}

		}

		wlaSollBestand.setHorizontalAlignment(SwingConstants.CENTER);
		wlaMindestBestand.setHorizontalAlignment(SwingConstants.CENTER);

		wlaUeberproduktion.setText(LPMain
				.getTextRespectUISPr("artikel.ueberproduktion")
				+ ":"
				+ " "
				+ ((artikelDto.getFUeberproduktion() != null) ? Helper
						.formatZahl(artikelDto.getFUeberproduktion(),2, LPMain
								.getTheClient().getLocUi())
						+ "%" : "-"));
		
	}
}
