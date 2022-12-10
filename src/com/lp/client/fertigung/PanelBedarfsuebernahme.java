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
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.PanelHandlagerbewegung;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.MultipleImageViewer;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperSnrChnrField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.Delegate;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.server.artikel.fastlanereader.generated.service.WwArtikellagerPK;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.fertigung.service.BedarfsuebernahmeDto;
import com.lp.server.fertigung.service.InternebestellungDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LoslagerentnahmeDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.GotoHelper;
import com.lp.util.Helper;

/**
 * <p>
 * Panel zum Bearbeiten der Internen Bestellung
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>03. 12. 2005</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.5 $
 */
public class PanelBedarfsuebernahme extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneBedarfsuebernahme tabbedPaneBedarfsuebernahme = null;

	private BedarfsuebernahmeDto bedarfsuebernahmeDto = null;

	private static final String ACTION_SPECIAL_Los = "action_special_los";
	private static final String ACTION_SPECIAL_ARTIKEL = "action_special_artikel";
	static final public String ACTION_SPECIAL_LAGER_FROM_LISTE = "action_lager_from_liste";

	private WrapperCheckBox wcbRueckgabe = new WrapperCheckBox();
	private WrapperCheckBox wcbZusaetzlich = new WrapperCheckBox();

	private PanelQueryFLR panelQueryFLRArtikel = null;
	private PanelQueryFLR panelQueryFLRLos = null;
	private PanelQueryFLR panelQueryFLRLager = null;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jPanelWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();
	private Border border1;

	private Integer lagerIId_Buchung = null;

	private WrapperButton wbuLager = new WrapperButton();
	private WrapperTextField wtfLager = new WrapperTextField();

	private WrapperLabel wlaLosnummerErfasst = new WrapperLabel();
	private WrapperTextField wtfLosnummerErfasst = new WrapperTextField();

	private WrapperTextField wtfLosnummer = new WrapperTextField();
	private WrapperGotoButton wbuLos = new WrapperGotoButton(
			GotoHelper.GOTO_FERTIGUNG_AUSWAHL);

	private WrapperGotoButton wbuArtikel = new WrapperGotoButton(
			GotoHelper.GOTO_ARTIKEL_AUSWAHL);
	private WrapperTextField wtfArtikel = new WrapperTextField();
	private WrapperTextField wtfArtikelNummerErfasst = new WrapperTextField();

	private WrapperLabel wlaLosinfo = new WrapperLabel();

	private WrapperLabel wlaWunschmenge = new WrapperLabel();
	private WrapperNumberField wnfWunschmenge = null;

	private MultipleImageViewer pi = new MultipleImageViewer(null);

	private WrapperLabel wlaLagerstand = new WrapperLabel();

	private WrapperLabel wlaGenehmigteMenge = new WrapperLabel();
	private WrapperNumberField wnfGenehmigteMenge = null;

	private WrapperLabel wlaEinheit = new WrapperLabel();

	private WrapperLabel wlaGebuchteMenge = new WrapperLabel();
	private WrapperNumberField wnfGebuchteMenge = null;

	WrapperSelectField wsfPerson = new WrapperSelectField(
			WrapperSelectField.PERSONAL, getInternalFrame(), false);

	private JLabel wlaZeitpunktErfasst = new JLabel();

	private WrapperLabel wlaArtikelErfasst = new WrapperLabel();
	private WrapperTextField wtfArtikelbezeichnungManuell = new WrapperTextField(
			80);

	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperTextField wtfKommentar = new WrapperTextField(80);

	private WrapperSnrChnrField wtfSeriennr = new WrapperSnrChnrField(
			getInternalFrame(), false);

	private WrapperLabel wlaWunschtermin = new WrapperLabel();
	private WrapperDateField wdfWunschtermin = new WrapperDateField();

	public PanelBedarfsuebernahme(InternalFrame internalFrame,
			String add2TitleI, Object key,
			TabbedPaneBedarfsuebernahme tabbedPaneBedarfsuebernahme)
			throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneBedarfsuebernahme = tabbedPaneBedarfsuebernahme;
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		this.enableToolsPanelButtons(aWhichButtonIUse);

		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setLayout(gridBagLayout1);
		wlaWunschmenge.setText(LPMain
				.getTextRespectUISPr("fert.bedarfsuebernahme.wunschmenge"));
		wlaGebuchteMenge.setText(LPMain
				.getTextRespectUISPr("fert.bedarfsuebernahme.buchungsmenge"));
		wlaGenehmigteMenge.setText(LPMain
				.getTextRespectUISPr("fert.bedarfsuebernahme.genehmigtemenge"));

		wlaWunschtermin.setText(LPMain
				.getTextRespectUISPr("fert.bedarfsuebernahme.wunschtermin"));

		wlaArtikelErfasst.setText(LPMain
				.getTextRespectUISPr("fert.bedarfsuebernahme.artikelerfasst"));

		wlaLosnummerErfasst
				.setText(LPMain
						.getTextRespectUISPr("fert.bedarfsuebernahme.losnummererfasst"));

		wlaKommentar.setText(LPMain.getTextRespectUISPr("lp.kommentar"));

		wlaLagerstand.setHorizontalAlignment(SwingConstants.LEFT);
		wlaLagerstand.setText("");

		wbuLager.setText(LPMain.getTextRespectUISPr("button.lager"));
		wbuLager.setActionCommand(ACTION_SPECIAL_LAGER_FROM_LISTE);
		wbuLager.addActionListener(this);

		wtfLager.setActivatable(false);
		wtfLager.setMandatoryField(true);

		wlaLosinfo.setHorizontalAlignment(SwingConstants.LEFT);

		wlaEinheit.setHorizontalAlignment(SwingConstants.LEFT);

		wtfArtikel.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfArtikel.setActivatable(false);
		wbuLos.setText(LPMain.getTextRespectUISPr("fert.tab.unten.los.title")
				+ "...");
		wbuLos.setToolTipText(LPMain
				.getTextRespectUISPr("fert.tab.unten.los.title") + "...");

		wcbRueckgabe.setText(LPMain
				.getTextRespectUISPr("fert.bedarfsuebernahme.rueckgabe"));
		wcbZusaetzlich.setText(LPMain
				.getTextRespectUISPr("fert.bedarfsuebernahme.zusaetzlich"));

		wtfLosnummer.setMandatoryField(true);
		wtfArtikel.setMandatoryField(true);

		wbuArtikel.setText(LPMain.getTextRespectUISPr("button.artikel"));
		wbuArtikel.setToolTipText(LPMain.getTextRespectUISPr("button.artikel"));
		jPanelWorkingOn.setBorder(border1);
		jPanelWorkingOn.setLayout(gridBagLayout3);

		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);

		wtfLosnummer.setActivatable(false);

		wsfPerson.setMandatoryField(true);

		wtfArtikelNummerErfasst.setActivatable(false);
		wtfLosnummerErfasst.setActivatable(false);
		wtfArtikelNummerErfasst.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfLosnummerErfasst.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfArtikelbezeichnungManuell.setActivatable(false);
		wtfArtikelbezeichnungManuell.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuLos.addActionListener(this);
		wbuLos.setActionCommand(ACTION_SPECIAL_Los);
		wbuArtikel.addActionListener(this);
		wbuArtikel.setActionCommand(ACTION_SPECIAL_ARTIKEL);

		wnfWunschmenge = new WrapperNumberField();
		wnfWunschmenge.setMinimumValue(0);

		wnfWunschmenge.setMandatoryFieldDB(true);
		wdfWunschtermin.setMandatoryField(true);

		wnfGebuchteMenge = new WrapperNumberField();
		wnfGebuchteMenge.setMinimumValue(0);
		wnfGebuchteMenge.setMandatoryField(true);

		wtfSeriennr.setWnfBelegMenge(wnfGebuchteMenge);

		wnfGenehmigteMenge = new WrapperNumberField();
		wnfGenehmigteMenge.setMinimumValue(0);
		wnfGenehmigteMenge.setMandatoryField(true);

		wnfWunschmenge.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());
		wnfGenehmigteMenge.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());
		wnfGebuchteMenge.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		// statusbarneu: 1 an den unteren rand des panels haengen
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;

		jPanelWorkingOn.add(wsfPerson.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wsfPerson.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaZeitpunktErfasst, new GridBagConstraints(3,
				iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaLosnummerErfasst, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfLosnummerErfasst, new GridBagConstraints(1,
				iZeile, 2, 1, 0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaKommentar, new GridBagConstraints(3, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wtfKommentar, new GridBagConstraints(4, iZeile, 1,
				1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wbuLos, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 100, 0));
		jPanelWorkingOn.add(wtfLosnummer, new GridBagConstraints(1, iZeile, 2,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaLosinfo, new GridBagConstraints(3, iZeile, 3, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaArtikelErfasst, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfArtikelNummerErfasst, new GridBagConstraints(1,
				iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn
				.add(new WrapperLabel(
						LPMain.getTextRespectUISPr("fert.bedarfsuebernahme.artikelbezerfasst")),
						new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wtfArtikelbezeichnungManuell,
				new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wbuArtikel, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfArtikel, new GridBagConstraints(1, iZeile, 4, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jPanelWorkingOn.add(wlaWunschmenge, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfWunschmenge, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), -50, 0));

		jPanelWorkingOn.add(wcbRueckgabe, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(pi, new GridBagConstraints(3, iZeile, 3, 8, 0.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaWunschtermin, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wdfWunschtermin, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wcbZusaetzlich, new GridBagConstraints(2, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaGenehmigteMenge, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfGenehmigteMenge, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), -50, 0));
		jPanelWorkingOn.add(wlaEinheit, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wtfSeriennr.getButtonSnrAuswahl(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfSeriennr, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaGebuchteMenge, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfGebuchteMenge, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wbuLager, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfLager, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaLagerstand, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		wtfSeriennr.getButtonSnrAuswahl().setVisible(false);
		wtfSeriennr.setVisible(false);

	}

	private void aktualisiereFelderSnrChnr(ArtikelDto artikelDto,
			boolean bEnableField,
			List<SeriennrChargennrMitMengeDto> alSeriennrChargennrMitMenge,
			Integer lagerIId) throws Throwable {
		if (Helper.short2boolean(artikelDto.getBChargennrtragend())
				|| Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
			wnfGebuchteMenge.setActivatable(false);
			wnfGebuchteMenge.setEditable(bEnableField);
			wtfSeriennr.setSeriennummern(alSeriennrChargennrMitMenge,
					artikelDto, lagerIId);
			wtfSeriennr.setVisible(true);
			wtfSeriennr.setMandatoryField(true);
			wtfSeriennr.getButtonSnrAuswahl().setVisible(true);

			jPanelWorkingOn.repaint();

		} else {
			wnfGebuchteMenge.setActivatable(true);
			wnfGebuchteMenge.setEditable(bEnableField);
			wtfSeriennr.setVisible(false);
			wtfSeriennr.setMandatoryField(false);
			wtfSeriennr.getButtonSnrAuswahl().setVisible(false);
			jPanelWorkingOn.repaint();

		}
		if (bEnableField) {
			wtfSeriennr.getButtonSnrAuswahl().setEnabled(bEnableField);
		}

	}

	protected PropertyVetoException eventActionVetoableChangeLP()
			throws Throwable {
		PropertyVetoException pve = super.eventActionVetoableChangeLP();
		DelegateFactory.getInstance().getFertigungDelegate()
				.removeLockDerInternenBestellungWennIchIhnSperre();
		return pve;
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (bedarfsuebernahmeDto.getIId() == null) {
				// speichern
				bedarfsuebernahmeDto.setIId(DelegateFactory.getInstance()
						.getFertigungDelegate()
						.createBedarfsuebernahme(bedarfsuebernahmeDto));
				this.setKeyWhenDetailPanel(bedarfsuebernahmeDto.getIId());

				bedarfsuebernahmeDto = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.bedarfsuebernahmeFindByPrimaryKey(
								bedarfsuebernahmeDto.getIId());

				dto2Components();
			} else {
				DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.verbucheBedarfsuebernahme(bedarfsuebernahmeDto,
								wnfGenehmigteMenge.getBigDecimal(),
								wnfGebuchteMenge.getBigDecimal(),
								wtfSeriennr.getSeriennummern(),
								lagerIId_Buchung);
			}

			super.eventActionSave(e, false);
			eventYouAreSelected(false);
		}
	}

	/**
	 * Die eingegebenen Daten in ein Dto schreiben
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {
		bedarfsuebernahmeDto.setNWunschmenge(wnfWunschmenge.getBigDecimal());
		bedarfsuebernahmeDto.setTWunschtermin(wdfWunschtermin.getTimestamp());
		bedarfsuebernahmeDto.setBAbgang(Helper.boolean2Short(!Helper
				.short2boolean(wcbRueckgabe.getShort())));
		bedarfsuebernahmeDto.setBzusaetzlich(wcbZusaetzlich.getShort());
		bedarfsuebernahmeDto.setCKommentar(wtfKommentar.getText());

		bedarfsuebernahmeDto.setPersonalIIdAnlegen(wsfPerson.getIKey());
		bedarfsuebernahmeDto.setPersonalIIdAendern(LPMain.getTheClient()
				.getIDPersonal());

	}

	/**
	 * Ein Dto-Objekt ins Panel uebertragen
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {

		wtfSeriennr.getButtonSnrAuswahl().setVisible(false);
		wtfSeriennr.setVisible(false);
		wtfSeriennr.setMandatoryField(false);

		if (bedarfsuebernahmeDto.getLosIId() != null) {
			LosDto losDto = DelegateFactory.getInstance()
					.getFertigungDelegate()
					.losFindByPrimaryKey(bedarfsuebernahmeDto.getLosIId());
			wtfLosnummer.setText(losDto.getCNr());

			String stueckliste = "";
			if (losDto.getStuecklisteIId() != null) {
				StuecklisteDto stklDto = DelegateFactory
						.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId());

				stueckliste = LPMain.getTextRespectUISPr("stkl.stueckliste")
						+ ": "
						+ stklDto.getArtikelDto()
								.formatArtikelbezeichnungMitZusatzbezeichnung();
			}

			wlaLosinfo.setText(LPMain.getTextRespectUISPr("lp.status") + ": "
					+ losDto.getStatusCNr().trim() + ", " + stueckliste);
			wbuLos.setOKey(bedarfsuebernahmeDto.getLosIId());
		} else {
			wtfLosnummer.setText(null);
			wlaLosinfo.setText("");
		}

		if (bedarfsuebernahmeDto.getArtikelIId() != null) {
			ArtikelDto artikelDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							bedarfsuebernahmeDto.getArtikelIId());

			wtfArtikel.setText(artikelDto.formatArtikelbezeichnung());
			wbuArtikel.setOKey(bedarfsuebernahmeDto.getArtikelIId());

			wlaEinheit.setText(artikelDto.getEinheitCNr().trim());

			aktualisiereFelderSnrChnr(artikelDto, false, null, lagerIId_Buchung);

		} else {
			wtfArtikel.setText(null);
		}

		wlaZeitpunktErfasst.setText(LPMain
				.getTextRespectUISPr("fert.bedarfsuebernahme.zeitpunkterfasst")
				+ Helper.formatTimestamp(bedarfsuebernahmeDto.getTAnlegen(),
						LPMain.getTheClient().getLocUi()));

		wsfPerson.setKey(bedarfsuebernahmeDto.getPersonalIIdAnlegen());

		wnfWunschmenge.setBigDecimal(bedarfsuebernahmeDto.getNWunschmenge());

		if (bedarfsuebernahmeDto.getLossollmaterialIId() != null) {
			LossollmaterialDto sollDto = DelegateFactory
					.getInstance()
					.getFertigungDelegate()
					.lossollmaterialFindByPrimaryKey(
							bedarfsuebernahmeDto.getLossollmaterialIId());

			wnfGebuchteMenge.setBigDecimal(DelegateFactory.getInstance()
					.getFertigungDelegate()
					.getAusgegebeneMenge(sollDto.getIId()));

			wnfGenehmigteMenge.setBigDecimal(sollDto.getNMenge());

			LosistmaterialDto[] istMat = DelegateFactory
					.getInstance()
					.getFertigungDelegate()
					.losistmaterialFindByLossollmaterialIId(
							bedarfsuebernahmeDto.getLossollmaterialIId());

			if (istMat != null && istMat.length > 0) {
				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(sollDto.getArtikelIId());
				LagerDto lDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerFindByPrimaryKey(istMat[0].getLagerIId());

				wtfLager.setText(lDto.getCNr());
				List<SeriennrChargennrMitMengeDto> snrchnrs = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getAllSeriennrchargennrEinerBelegartposition(
								LocaleFac.BELEGART_LOS, istMat[0].getIId());
				wtfSeriennr.setSeriennummern(snrchnrs, artikelDto,
						lDto.getIId());
			} else {
				lagerIId_Buchung = null;
				wtfLager.setText(null);
			}

		} else {
			wnfGebuchteMenge.setBigDecimal(null);
			wnfGenehmigteMenge.setBigDecimal(null);
			lagerIId_Buchung = null;
			wtfLager.setText(null);
		}

		wtfKommentar.setText(bedarfsuebernahmeDto.getcKommentar());

		wdfWunschtermin.setTimestamp(bedarfsuebernahmeDto.getTWunschtermin());

		wcbRueckgabe.setShort(Helper.boolean2Short(!Helper
				.short2boolean(bedarfsuebernahmeDto.getBAbgang())));
		wcbZusaetzlich.setShort(bedarfsuebernahmeDto.getBZusaetzlich());

		wtfLosnummerErfasst.setText(bedarfsuebernahmeDto.getCLosnummer());
		wtfArtikelNummerErfasst.setText(bedarfsuebernahmeDto
				.getCArtikelnummer());
		wtfArtikelbezeichnungManuell.setText(bedarfsuebernahmeDto
				.getCArtikelbezeichnung());

		pi.setImage(bedarfsuebernahmeDto.getOMedia());

		this.setStatusbarPersonalIIdAendern(bedarfsuebernahmeDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(bedarfsuebernahmeDto.getTAendern());

		this.setStatusbarPersonalIIdAnlegen(bedarfsuebernahmeDto
				.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(bedarfsuebernahmeDto.getTAnlegen());
	}

	public void eventActionNew(EventObject eventObject,
			boolean bChangeKeyLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		this.leereAlleFelder(this);
		bedarfsuebernahmeDto = new BedarfsuebernahmeDto();
		wlaLagerstand.setText("");
	}

	/**
	 * Stornieren einer Rechnung bzw Gutschrift
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bAdministrateLockKeyI
	 *            boolean
	 * @param bNeedNoDeleteI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (bedarfsuebernahmeDto.getStatusCNr().equals(
				LocaleFac.STATUS_VERBUCHT)) {
			DelegateFactory
					.getInstance()
					.getFertigungDelegate()
					.verbuchungBedarfsuebernahmeZuruecknehmen(
							bedarfsuebernahmeDto.getIId());
			super.eventActionDelete(e, false, false);
		} else {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("fert.bedarfsuebernahme.error.ruecknahme.nurimstatusverbucht"));
		}

	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_BEDARFSUEBERNAHME;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_Los)) {
			dialogQueryLos(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL)) {
			dialogQueryArtikel(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER_FROM_LISTE)) {

			if (bedarfsuebernahmeDto.getLosIId() != null) {
				dialogQueryLagerFromListe(e);
			} else {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("fert.bedarfsuebernahme.lagerauswahl.keinlos"));
			}

		}
	}

	/**
	 * eventItemchanged.
	 * 
	 * @param eI
	 *            EventObject
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLos) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LosDto losDto = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.losFindByPrimaryKey((Integer) key);
				wtfLosnummer.setText(losDto.getCNr());
				bedarfsuebernahmeDto.setLosIId(losDto.getIId());
				if (bedarfsuebernahmeDto.getCLosnummer() == null) {
					bedarfsuebernahmeDto.setCLosnummer(losDto.getCNr());
				}

				LoslagerentnahmeDto[] lagerDtos = DelegateFactory.getInstance()
						.getFertigungDelegate()
						.loslagerentnahmeFindByLosIId(losDto.getIId());
				if (lagerDtos != null && lagerDtos.length > 0) {
					LagerDto lDto = DelegateFactory.getInstance()
							.getLagerDelegate()
							.lagerFindByPrimaryKey(lagerDtos[0].getLagerIId());

					wtfLager.setText(lDto.getCNr());
					lagerIId_Buchung = lDto.getIId();

				}

				aktualisiereLagerstand();

			} else if (e.getSource() == panelQueryFLRArtikel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);

				wtfArtikel.setText(artikelDto.formatArtikelbezeichnung());
				wlaEinheit.setText(artikelDto.getEinheitCNr().trim());

				aktualisiereFelderSnrChnr(artikelDto, true, null, null);
				wtfSeriennr.setMandatoryField(false);
				wtfSeriennr.getButtonSnrAuswahl().setEnabled(false);
				bedarfsuebernahmeDto.setArtikelIId(artikelDto.getIId());
				if (bedarfsuebernahmeDto.getCArtikelnummer() == null) {
					bedarfsuebernahmeDto.setCArtikelnummer(artikelDto.getCNr());
				}

				aktualisiereLagerstand();

			} else if (e.getSource() == panelQueryFLRLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LoslagerentnahmeDto loslagerentnahmeDto = DelegateFactory
						.getInstance().getFertigungDelegate()
						.loslagerentnahmeFindByPrimaryKey((Integer) key);
				LagerDto lagerDto = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.lagerFindByPrimaryKey(
								loslagerentnahmeDto.getLagerIId());

				loslagerentnahmeDto.getLagerIId();

				wtfLager.setText(lagerDto.getCNr());
				lagerIId_Buchung = lagerDto.getIId();

				aktualisiereLagerstand();

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

	private void aktualisiereLagerstand() throws Throwable {

		if (bedarfsuebernahmeDto.getArtikelIId() != null
				&& lagerIId_Buchung != null) {

			ArtikelDto artikelDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							bedarfsuebernahmeDto.getArtikelIId());
			if (artikelDto.isLagerbewirtschaftet()) {
				BigDecimal lagerstand = DelegateFactory
						.getInstance()
						.getLagerDelegate()
						.getLagerstand(bedarfsuebernahmeDto.getArtikelIId(),
								lagerIId_Buchung);

				wlaLagerstand.setText(Helper.formatZahl(lagerstand, Defaults
						.getInstance().getIUINachkommastellenMenge(), LPMain
						.getInstance().getTheClient().getLocUi())
						+ " "
						+ LPMain.getInstance().getTextRespectUISPr(
								"artikel.handlagerbewegung.auflager"));
			} else {
				wlaLagerstand.setText("nicht lagerbew.");
			}
		}
	}

	/**
	 * Dialogfenster zur Auftragauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryLos(ActionEvent e) throws Throwable {
		panelQueryFLRLos = FertigungFilterFactory.getInstance()
				.createPanelFLRLose(getInternalFrame(), null, false);

		panelQueryFLRLos.setSelectedId(bedarfsuebernahmeDto.getLosIId());

		new DialogQuery(panelQueryFLRLos);
	}

	/**
	 * Dialogfenster zur Auftragauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryArtikel(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikel(getInternalFrame(), false);

		panelQueryFLRArtikel
				.setSelectedId(bedarfsuebernahmeDto.getArtikelIId());

		new DialogQuery(panelQueryFLRArtikel);
	}

	void dialogQueryLagerFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRLager = new PanelQueryFLR(null, FertigungFilterFactory
				.getInstance().createFKLagerentnahme(
						bedarfsuebernahmeDto.getLosIId()),
				QueryParameters.UC_ID_LOSLAGERENTNAHME, null,
				getInternalFrame(), LPMain.getTextRespectUISPr("label.lager"));
		panelQueryFLRLager
				.befuellePanelFilterkriterienDirektUndVersteckte(null, null,
						ArtikelFilterFactory.getInstance().createFKVLager());

		new DialogQuery(panelQueryFLRLager);

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			wcbRueckgabe.setSelected(false);

			wnfGebuchteMenge.setEditable(false);
			wnfGenehmigteMenge.setEditable(false);
			wbuLager.setEnabled(false);

			wnfGebuchteMenge.setMandatoryField(false);
			wnfGenehmigteMenge.setMandatoryField(false);
			wtfLager.setMandatoryField(false);
			wtfSeriennr.setMandatoryField(false);
			wtfSeriennr.getButtonSnrAuswahl().setVisible(false);
			wtfSeriennr.setVisible(false);

			wlaZeitpunktErfasst.setText("                                 ");

		} else {
			bedarfsuebernahmeDto = DelegateFactory.getInstance()
					.getFertigungDelegate()
					.bedarfsuebernahmeFindByPrimaryKey((Integer) key);

			wnfGebuchteMenge.setMandatoryField(true);
			wnfGenehmigteMenge.setMandatoryField(true);
			wtfLager.setMandatoryField(true);

			dto2Components();
		}
	}

	public void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {

		if (bedarfsuebernahmeDto.getStatusCNr().equals(LocaleFac.STATUS_OFFEN)) {
			super.eventActionUpdate(aE, bNeedNoUpdateI);
			wsfPerson.setEnabled(false);

			wtfKommentar.setEditable(false);
			wcbRueckgabe.setEnabled(false);
			wcbZusaetzlich.setEnabled(false);
			wnfWunschmenge.setEditable(false);
			wnfGebuchteMenge.setEditable(false);

			if (bedarfsuebernahmeDto.getLosIId() != null) {

				LoslagerentnahmeDto[] lagerDtos = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.loslagerentnahmeFindByLosIId(
								bedarfsuebernahmeDto.getLosIId());
				if (lagerDtos != null && lagerDtos.length > 0) {
					LagerDto lDto = DelegateFactory.getInstance()
							.getLagerDelegate()
							.lagerFindByPrimaryKey(lagerDtos[0].getLagerIId());

					wtfLager.setText(lDto.getCNr());
					lagerIId_Buchung = lDto.getIId();

				}

			}

			if (bedarfsuebernahmeDto.getArtikelIId() != null) {
				ArtikelDto artikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								bedarfsuebernahmeDto.getArtikelIId());
				aktualisiereFelderSnrChnr(artikelDto, true, null,
						lagerIId_Buchung);
			}
			aktualisiereLagerstand();
		} else {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("fert.bedarfsuebernahme.error.verbuchen.nurimstatusoffen"));
		}

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuLos;
	}

}
