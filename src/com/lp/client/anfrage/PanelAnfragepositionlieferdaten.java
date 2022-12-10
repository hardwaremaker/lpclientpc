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
package com.lp.client.anfrage;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.anfrage.service.AnfragepositionlieferdatenDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.util.Helper;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("static-access")
/*
 * <p>In diesem Fenster werden Anfragepositionlieferdaten erfasst. <p>Copyright
 * Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum 17.06.05</p>
 * <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.14 $
 */
public class PanelAnfragepositionlieferdaten extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAnfrage intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAnfrage tpAnfrage = null;

	private AnfragepositionlieferdatenDto anfragepositionlieferdatenDto = null;
	protected AnfragepositionDto anfragepositionDto = null;

	private JPanel jPanelWorkingOn = null;
	private Border innerBorder = null;

	private WrapperGotoButton wlaIdent = null;
	private WrapperLabel wlaArtikelbezeichnung = null;
	private WrapperLabel wlaArtikelzusatzbez = null;
	private WrapperLabel wlaArtikelbezeichnungbeimlieferanten = null;

	private WrapperLabel wlaAnlieferzeitinwochen = null;
	private WrapperLabel wlaWunschtermin = null;

	private WrapperLabel wlaAnliefermenge = null;
	private WrapperLabel wlaWunschmenge = null;
	private WrapperLabel wlaEinheitAnliefermenge = null;
	private WrapperLabel wlaEinheitWunschmenge = null;

	private WrapperLabel wlaAngeboteinzelpreis = null;
	private WrapperLabel wlaRichtpreis = null;
	private WrapperLabel wlaWaehrungAngeboteinzelpreis = null;
	private WrapperLabel wlaWaehrungRichtpreis = null;
	private WrapperLabel wlaArtikelnummerbeimlieferanten = null;

	private WrapperTextField wtfIdent = null;
	private WrapperTextField wtfArtikelbezeichnung = null;
	private WrapperTextField wtfArtikelzusatzbez = null;
	private WrapperTextField wtfArtikelbezeichnungbeimlieferanten = null;
	private WrapperTextField wtfArtikelnummerbeimlieferanten = null;

	private WrapperNumberField wnfAnlieferzeit = null;
	private WrapperNumberField wnfLief1WBZ = null;
	private WrapperDateField wdfWunschtermin = null;

	private WrapperLabel wlaPreisgueltigab = new WrapperLabel();
	private WrapperDateField wdfPreisgueltigab = new WrapperDateField();
	
	protected WrapperNumberField wnfAnliefermenge = null;
	protected WrapperNumberField wnfLief1Menge = null;
	private WrapperNumberField wnfWunschmenge = null;

	protected WrapperNumberField wnfAngeboteinzelpreis = null;
	protected WrapperNumberField wnfLief1Preis = null;
	private WrapperNumberField wnfRichtpreis = null;

	private WrapperLabel wlaStandardmenge = null;
	private WrapperLabel wlaMindestbestellmenge = null;
	private WrapperLabel wlaVerpackungseinheit = null;
	private WrapperNumberField wnfStandardmenge = null;
	private WrapperNumberField wnfMindestbestellmenge = null;
	private WrapperNumberField wnfVerpackungseinheit = null;

	private WrapperLabel wlaEinheitVerpackungseinheit = new WrapperLabel();
	private WrapperLabel wlaEinheitMindestbestellmenge = new WrapperLabel();
	private WrapperLabel wlaEinheitStandardmenge = new WrapperLabel();
	private WrapperSelectField wsfZertifikatart = null;

	private WrapperLabel wlaAnlieferzeitEinheit = new WrapperLabel();

	protected WrapperNumberField wnfGewichtbestellmenge = null;
	private WrapperLabel wlaGewichtEinheit = new WrapperLabel();
	protected WrapperNumberField wnfGewichtPreis = null;
	private WrapperLabel wlaGewichtWaehrung = null;
	private WrapperLabel wlaAltBestMengeneinheit = new WrapperLabel(
			LPMain.getInstance().getTextRespectUISPr("bes.alternative.bestellmengeneinheit"));

	public PanelAnfragepositionlieferdaten(InternalFrame internalFrame, String add2TitleI, Object key)
			throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameAnfrage) internalFrame;
		tpAnfrage = intFrame.getTabbedPaneAnfrage();

		jbInit();
		initComponents();
	}

	void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		setLayout(new GridBagLayout());

		innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(innerBorder);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		// Workingpanel

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		wsfZertifikatart = new WrapperSelectField(WrapperSelectField.ZERTIFIKATART, getInternalFrame(), true);

		wlaIdent = new WrapperGotoButton(LPMain.getInstance().getTextRespectUISPr("label.identnummer"),
				com.lp.util.GotoHelper.GOTO_ARTIKEL_AUSWAHL);
		wlaIdent.setActivatable(false);

		wlaArtikelbezeichnung = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));
		wlaArtikelzusatzbez = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.zusatzbezeichnung"));
		wlaArtikelbezeichnungbeimlieferanten = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr("anf.artikelbezeichnungbeimlieferanten"));
		wlaArtikelnummerbeimlieferanten = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr("anfr.artikelnrlieferant"));

		
		
		wlaPreisgueltigab.setText(LPMain.getTextRespectUISPr("anfrage.lieferdaten.preisgueltigab"));
		
		wdfPreisgueltigab.setMandatoryField(true);
		
		wlaAnlieferzeitEinheit.setHorizontalAlignment(SwingConstants.LEFT);

		wnfGewichtbestellmenge = new WrapperNumberField();
		wnfGewichtPreis = new WrapperNumberField();
		wnfGewichtPreis.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
		wlaGewichtEinheit.setHorizontalAlignment(SwingConstants.LEFT);

		wlaGewichtWaehrung = new WrapperLabel();
		wlaGewichtWaehrung.setHorizontalAlignment(SwingConstants.LEADING);

		wnfGewichtbestellmenge.addFocusListener(new FocusAdapterGewicht(this));
		wnfGewichtbestellmenge.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());

		wnfGewichtPreis.addFocusListener(new FocusAdapterGewichtPreis(this));

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT,
						ParameterFac.KATEGORIE_ARTIKEL, LPMain.getInstance().getTheClient().getMandant());

		if (parameter.getCWert() != null) {
			if (parameter.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
				wlaAnlieferzeitEinheit.setText(LPMain.getTextRespectUISPr("lp.kw"));
			} else if (parameter.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE)) {
				wlaAnlieferzeitEinheit.setText(LPMain.getTextRespectUISPr("lp.tage"));
			} else {
				wlaAnlieferzeitEinheit.setText("?");
			}
		}

		wlaAnlieferzeitinwochen = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr("artikel.artikellieferant.wiederbeschaffungszeit"));
		wlaWunschtermin = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("anf.anliefertermin"));

		wlaStandardmenge = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("artikel.standardmenge"));
		wlaVerpackungseinheit = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr("artikel.verpackungseinheit"));
		wlaMindestbestellmenge = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr("artikel.mindestbestellmenge"));

		wtfArtikelnummerbeimlieferanten = new WrapperTextField();

		wnfStandardmenge = new WrapperNumberField();
		wnfVerpackungseinheit = new WrapperNumberField();
		wnfMindestbestellmenge = new WrapperNumberField();

		wlaAnliefermenge = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("anf.anliefermenge"));
		HelperClient.setDefaultsToComponent(wlaAnliefermenge, 90);
		wlaWunschmenge = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.menge"));
		HelperClient.setDefaultsToComponent(wlaWunschmenge, 90);
		wlaAngeboteinzelpreis = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("anf.anlieferpreis"));
		wlaRichtpreis = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("anf.richtpreis"));

		wlaEinheitAnliefermenge = new WrapperLabel();
		wlaEinheitAnliefermenge.setHorizontalAlignment(SwingConstants.LEADING);
		HelperClient.setDefaultsToComponent(wlaEinheitAnliefermenge, 30);
		wlaEinheitWunschmenge = new WrapperLabel();
		wlaEinheitWunschmenge.setHorizontalAlignment(SwingConstants.LEADING);
		HelperClient.setDefaultsToComponent(wlaEinheitWunschmenge, 30);

		wlaEinheitStandardmenge.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitMindestbestellmenge.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitVerpackungseinheit.setHorizontalAlignment(SwingConstants.LEFT);

		wlaWaehrungAngeboteinzelpreis = new WrapperLabel();
		wlaWaehrungAngeboteinzelpreis.setHorizontalAlignment(SwingConstants.LEADING);
		wlaWaehrungRichtpreis = new WrapperLabel();
		wlaWaehrungRichtpreis.setHorizontalAlignment(SwingConstants.LEADING);

		wtfIdent = new WrapperTextField();
		wtfIdent.setActivatable(false);
		wtfArtikelbezeichnung = new WrapperTextField();
		wtfArtikelbezeichnung.setActivatable(false);
		wtfArtikelbezeichnung.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfArtikelzusatzbez = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
		wtfArtikelzusatzbez.setActivatable(false);
		wtfArtikelbezeichnungbeimlieferanten = new WrapperTextField();

		wnfAnlieferzeit = new WrapperNumberField();
		wnfAnlieferzeit = new WrapperNumberField(0, 9999);
		wnfAnlieferzeit.setFractionDigits(0);

		wnfLief1Preis = new WrapperNumberField();
		wnfLief1Preis.setActivatable(false);

		wnfLief1Menge = new WrapperNumberField();
		wnfLief1Menge.setActivatable(false);

		wnfLief1WBZ = new WrapperNumberField();
		wnfLief1WBZ.setActivatable(false);

		wdfWunschtermin = new WrapperDateField();
		wdfWunschtermin.setActivatable(false);

		wnfAnliefermenge = new WrapperNumberField();
		wnfAnliefermenge.setMandatoryField(true);
		wnfAnliefermenge.addFocusListener(new WnfGelieferteMengeFocusAdapter(this));

		wnfAnliefermenge.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());

		wnfWunschmenge = new WrapperNumberField();
		wnfWunschmenge.setActivatable(false);
		wnfWunschmenge.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());

		wnfAngeboteinzelpreis = new WrapperNumberField();
		wnfAngeboteinzelpreis.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
		wnfAngeboteinzelpreis.setMandatoryField(true);
		wnfAngeboteinzelpreis.addFocusListener(new WnfGelieferterPreisFocusAdapter(this));
		wnfRichtpreis = new WrapperNumberField();
		wnfRichtpreis.setActivatable(false);
		wnfRichtpreis.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());

		jPanelWorkingOn = new JPanel(new MigLayout("wrap 9", "[27%][10%][10%][7%][10%][7%][15%][15%][4%]"));
		add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jPanelWorkingOn.add(wlaIdent, "growx");
		jPanelWorkingOn.add(wtfIdent, "growx, span 2, wrap");

		jPanelWorkingOn.add(wlaArtikelbezeichnung, "growx");
		jPanelWorkingOn.add(wtfArtikelbezeichnung, "growx, span 5");
		jPanelWorkingOn.add(wlaStandardmenge, "growx");
		jPanelWorkingOn.add(wnfStandardmenge, "growx");
		jPanelWorkingOn.add(wlaEinheitStandardmenge, "growx, wrap");

		jPanelWorkingOn.add(wlaArtikelzusatzbez, "growx");
		jPanelWorkingOn.add(wtfArtikelzusatzbez, "growx, span 5");
		jPanelWorkingOn.add(wlaMindestbestellmenge, "growx");
		jPanelWorkingOn.add(wnfMindestbestellmenge, "growx");
		jPanelWorkingOn.add(wlaEinheitMindestbestellmenge, "growx, wrap");

		jPanelWorkingOn.add(wlaArtikelnummerbeimlieferanten, "growx");
		jPanelWorkingOn.add(wtfArtikelnummerbeimlieferanten, "growx, span 5");
		jPanelWorkingOn.add(wlaVerpackungseinheit, "growx");
		jPanelWorkingOn.add(wnfVerpackungseinheit, "growx");
		jPanelWorkingOn.add(wlaEinheitVerpackungseinheit, "growx, wrap");

		jPanelWorkingOn.add(wlaArtikelbezeichnungbeimlieferanten, "growx");
		jPanelWorkingOn.add(wtfArtikelbezeichnungbeimlieferanten, "growx, span 5, wrap");

		jPanelWorkingOn.add(new WrapperLabel("Lief1"), "growx, skip2, wrap");

		jPanelWorkingOn.add(wlaAnlieferzeitinwochen, "growx");
		jPanelWorkingOn.add(wnfAnlieferzeit, "growx");
		jPanelWorkingOn.add(wnfLief1WBZ, "growx");
		jPanelWorkingOn.add(wlaAnlieferzeitEinheit, "growx");
		jPanelWorkingOn.add(wlaAltBestMengeneinheit, "growx, span 2");
		jPanelWorkingOn.add(wlaWunschtermin, "growx");
		jPanelWorkingOn.add(wdfWunschtermin, "growx, span 2, wrap");

		jPanelWorkingOn.add(wlaAnliefermenge, "right, growx");
		jPanelWorkingOn.add(wnfAnliefermenge, "growx");
		jPanelWorkingOn.add(wnfLief1Menge, "growx");
		jPanelWorkingOn.add(wlaEinheitAnliefermenge, "growx");

		jPanelWorkingOn.add(wnfGewichtbestellmenge, "growx");
		jPanelWorkingOn.add(wlaGewichtEinheit, "growx");

		jPanelWorkingOn.add(wlaWunschmenge, "right, growx");
		jPanelWorkingOn.add(wnfWunschmenge, "growx");

		jPanelWorkingOn.add(wlaEinheitWunschmenge, "growx, wrap");

		jPanelWorkingOn.add(wlaAngeboteinzelpreis, "growx");
		jPanelWorkingOn.add(wnfAngeboteinzelpreis, "growx");
		jPanelWorkingOn.add(wnfLief1Preis, "growx");
		jPanelWorkingOn.add(wlaWaehrungAngeboteinzelpreis, "growx");
		jPanelWorkingOn.add(wnfGewichtPreis, "growx");
		jPanelWorkingOn.add(wlaGewichtWaehrung, "growx");
		jPanelWorkingOn.add(wlaRichtpreis, "growx");
		jPanelWorkingOn.add(wnfRichtpreis, "growx");
		jPanelWorkingOn.add(wlaWaehrungRichtpreis, "growx, wrap");

		
		jPanelWorkingOn.add(wlaPreisgueltigab, " growx");
		jPanelWorkingOn.add(wdfPreisgueltigab, "growx, span 2");
		
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZERTIFIKATART)) {
			jPanelWorkingOn.add(wsfZertifikatart.getWrapperButton(), "skip 3, growx");
			jPanelWorkingOn.add(wsfZertifikatart.getWrapperTextField(), "growx, span 2");
		}

	}

	private void setDefaults() throws Throwable {
		wlaWaehrungAngeboteinzelpreis.setText(tpAnfrage.getAnfrageDto().getWaehrungCNr());
		wlaWaehrungRichtpreis.setText(tpAnfrage.getAnfrageDto().getWaehrungCNr());
		wdfWunschtermin.setTimestamp(tpAnfrage.getAnfrageDto().getTAnliefertermin());
		wnfGewichtbestellmenge.setBigDecimal(null);
		wnfGewichtPreis.setBigDecimal(null);
	}

	private void setIdentVisible(boolean bVisible) {
		wlaIdent.setVisible(bVisible);
		wtfIdent.setVisible(bVisible);
	}

	private void setArtikelbezeichnungbeimlieferantenVisible(boolean bVisible) {
		wlaArtikelbezeichnungbeimlieferanten.setVisible(bVisible);
		wtfArtikelbezeichnungbeimlieferanten.setVisible(bVisible);
		wlaArtikelnummerbeimlieferanten.setVisible(bVisible);
		wtfArtikelnummerbeimlieferanten.setVisible(bVisible);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			DelegateFactory.getInstance().getAnfragepositionDelegate()
					.updateAnfragepositionlieferdaten(anfragepositionlieferdatenDto);

			// buttons schalten
			super.eventActionSave(e, true);

			eventYouAreSelected(false);
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		if (tpAnfrage.pruefeAktuelleAnfrage()) {
			if (tpAnfrage.istAktualisierenLieferdatenErlaubt()) {

				super.eventActionUpdate(aE, false);
				// Vorschlagswerte setzen, wenn die Position noch nicht erfasst
				// wurde
				if (!Helper.short2boolean(anfragepositionlieferdatenDto.getBErfasst())) {
					wnfAnliefermenge.setBigDecimal(anfragepositionDto.getNMenge());
					wnfAngeboteinzelpreis.setBigDecimal(anfragepositionDto.getNRichtpreis());

					
					
					//PJ22394
					if(tpAnfrage.getAnfrageDto().getTPreisgueltigab()!=null) {
						wdfPreisgueltigab.setDate(tpAnfrage.getAnfrageDto().getTPreisgueltigab());
					}else {
						//SP9451
						wdfPreisgueltigab.setDate(new java.util.Date());
					}
					
					
					
					if (anfragepositionDto.getArtikelIId() != null) {
						ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
								.artikelFindByPrimaryKey(anfragepositionDto.getArtikelIId());

						refreshBestellmengeneinheitUndPreis(artikelDto);
					}

				}

				if (!Helper.short2boolean(anfragepositionlieferdatenDto.getBErfasst())) {
					if (anfragepositionDto.getArtikelIId() != null) {

						ArtikellieferantDto alDto = DelegateFactory.getInstance().getArtikelDelegate()
								.getArtikelEinkaufspreis(anfragepositionDto.getArtikelIId(),
										tpAnfrage.getAnfrageDto().getLieferantIIdAnfrageadresse(),
										anfragepositionDto.getNMenge(), tpAnfrage.getAnfrageDto().getWaehrungCNr(),
										new java.sql.Date(tpAnfrage.getAnfrageDto().getTBelegdatum().getTime()));

						if (alDto != null) {
							wnfStandardmenge.setDouble(alDto.getFStandardmenge());
							wnfMindestbestellmenge.setDouble(alDto.getFMindestbestelmenge());
							wnfVerpackungseinheit.setBigDecimal(alDto.getNVerpackungseinheit());
							wtfArtikelnummerbeimlieferanten.setText(alDto.getCArtikelnrlieferant());
							wtfArtikelbezeichnungbeimlieferanten.setText(alDto.getCBezbeilieferant());
						}

					}

				}

			}
		}
	}

	private void resetPanel() throws Throwable {
		anfragepositionlieferdatenDto = new AnfragepositionlieferdatenDto();
		anfragepositionDto = new AnfragepositionDto();

		leereAlleFelder(this);
		setDefaults();
	}

	private void dto2Components() throws Throwable {
		String cArtikelbezeichnung = null;

		wtfArtikelzusatzbez.setText(null);

		ArtikelDto artikelDto = null;
		if (anfragepositionDto.getPositionsartCNr().equals(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)) {
			artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(anfragepositionDto.getArtikelIId());

			wlaEinheitMindestbestellmenge.setText(artikelDto.getEinheitCNr().trim());
			wlaEinheitStandardmenge.setText(artikelDto.getEinheitCNr().trim());
			wlaEinheitVerpackungseinheit.setText(artikelDto.getEinheitCNr().trim());

			wnfMindestbestellmenge.setBigDecimal(anfragepositionlieferdatenDto.getNMindestbestellmenge());
			wnfVerpackungseinheit.setBigDecimal(anfragepositionlieferdatenDto.getNVerpackungseinheit());
			wnfStandardmenge.setBigDecimal(anfragepositionlieferdatenDto.getNStandardmenge());
			
			wdfPreisgueltigab.setTimestamp(anfragepositionlieferdatenDto.getTPreisgueltigab());

			setIdentVisible(true);
			wtfIdent.setText(artikelDto.getCNr());

			wlaIdent.setOKey(artikelDto.getIId());

			wtfArtikelbezeichnungbeimlieferanten.setText(anfragepositionlieferdatenDto.getCBezbeilieferant());
			wtfArtikelnummerbeimlieferanten.setText(anfragepositionlieferdatenDto.getCArtikelnrlieferant());

			// die sprachabhaengig Artikelbezeichnung anzeigen
			cArtikelbezeichnung = DelegateFactory.getInstance().getArtikelDelegate()
					.formatArtikelbezeichnungEinzeiligOhneExc(anfragepositionDto.getArtikelIId());

			// die Artikelzusatzbezeichnung anzeigen
			ArtikelsprDto artikelsprDto = artikelDto.getArtikelsprDto();
			if (artikelsprDto != null) {
				if (artikelsprDto.getCZbez() != null) {
					wtfArtikelzusatzbez.setText(artikelsprDto.getCZbez());

				}

				if (artikelsprDto.getCBez() != null) {
					cArtikelbezeichnung = artikelsprDto.getCBez();
				}

			}

			setArtikelbezeichnungbeimlieferantenVisible(true);
		} else {
			setIdentVisible(false);

			if (anfragepositionDto.getCBez() != null) {
				cArtikelbezeichnung = anfragepositionDto.getCBez();
			}

			setArtikelbezeichnungbeimlieferantenVisible(false);
		}

		wtfArtikelbezeichnung.setText(cArtikelbezeichnung);
		if (anfragepositionDto.getCZusatzbez() != null) {
			wtfArtikelzusatzbez.setText(anfragepositionDto.getCZusatzbez());
		}

		wnfAnlieferzeit.setInteger(anfragepositionlieferdatenDto.getIAnlieferzeit());

		wsfZertifikatart.setKey(anfragepositionlieferdatenDto.getZertifikatartIId());

		if (Helper.short2boolean(anfragepositionlieferdatenDto.getBErfasst())) {
			wnfAnliefermenge.setBigDecimal(anfragepositionlieferdatenDto.getNAnliefermenge());
			wnfAngeboteinzelpreis.setBigDecimal(anfragepositionlieferdatenDto.getNNettogesamtpreis());
		} else {

			wnfAnliefermenge.setText("");
			wnfAngeboteinzelpreis.setText("");
		}

		wlaEinheitAnliefermenge.setText(anfragepositionDto.getEinheitCNr().trim());

		wnfWunschmenge.setBigDecimal(anfragepositionDto.getNMenge());
		wlaEinheitWunschmenge.setText(anfragepositionDto.getEinheitCNr().trim());
		wnfRichtpreis.setBigDecimal(anfragepositionDto.getNRichtpreis());

		// Lief1
		
		if (anfragepositionDto.getArtikelIId() != null) {

			ArtikellieferantDto alDto = DelegateFactory.getInstance().getArtikelDelegate().getArtikelEinkaufspreis(
					anfragepositionDto.getArtikelIId(), tpAnfrage.getAnfrageDto().getLieferantIIdAnfrageadresse(),
					anfragepositionDto.getNMenge(), tpAnfrage.getAnfrageDto().getWaehrungCNr(),
					new java.sql.Date(tpAnfrage.getAnfrageDto().getTBelegdatum().getTime()));

			if (alDto != null) {
				wnfLief1Menge.setBigDecimal(alDto.getNStaffelmenge());
				wnfLief1Preis.setBigDecimal(alDto.getNNettopreis());
				wnfLief1WBZ.setInteger(alDto.getIWiederbeschaffungszeit());
			}
		}

		refreshBestellmengeneinheitUndPreis(artikelDto);

	}

	public void refreshBestellmengeneinheitUndPreis(ArtikelDto artikelDto) throws ExceptionLP, Throwable {

		if (artikelDto != null && artikelDto.getEinheitCNrBestellung() != null) {
			wnfGewichtbestellmenge.setVisible(true);
			wnfGewichtPreis.setVisible(true);
			wlaAltBestMengeneinheit.setVisible(true);
			wlaGewichtWaehrung.setVisible(true);

			revalidate();
			if (wnfAnliefermenge.getBigDecimal() != null && artikelDto.getNUmrechnungsfaktor() != null) {
				if (Helper.short2boolean(artikelDto.getbBestellmengeneinheitInvers())) {
					wnfGewichtbestellmenge.setBigDecimal(wnfAnliefermenge.getBigDecimal().divide(
							artikelDto.getNUmrechnungsfaktor(), Defaults.getInstance().getIUINachkommastellenPreiseEK(),
							BigDecimal.ROUND_HALF_EVEN));
				} else {
					wnfGewichtbestellmenge.setBigDecimal(
							wnfAnliefermenge.getBigDecimal().multiply(artikelDto.getNUmrechnungsfaktor()));
				}
			}

			wlaGewichtEinheit.setText(artikelDto.getEinheitCNrBestellung().trim());
			wlaGewichtWaehrung.setText(
					tpAnfrage.getAnfrageDto().getWaehrungCNr() + "/" + artikelDto.getEinheitCNrBestellung().trim());
			if (wnfAngeboteinzelpreis.getBigDecimal() != null && artikelDto.getNUmrechnungsfaktor() != null) {
				BigDecimal nPreisPerEinheit = null;
				if (Helper.short2boolean(artikelDto.getbBestellmengeneinheitInvers())) {
					nPreisPerEinheit = (wnfAngeboteinzelpreis.getBigDecimal().multiply(artikelDto.getNUmrechnungsfaktor()));
				} else {
					nPreisPerEinheit = (wnfAngeboteinzelpreis.getBigDecimal().divide(artikelDto.getNUmrechnungsfaktor(),
							Defaults.getInstance().getIUINachkommastellenPreiseEK(), BigDecimal.ROUND_HALF_EVEN));
				}

				wnfGewichtPreis.setBigDecimal(nPreisPerEinheit);
			}
		} else {
			wlaGewichtEinheit.setText(null);

			wnfGewichtbestellmenge.setVisible(false);
			wnfGewichtPreis.setVisible(false);

			wlaGewichtWaehrung.setVisible(false);
			wlaAltBestMengeneinheit.setVisible(false);

			revalidate();

		}
	}

	private void components2Dto() throws Throwable {
		anfragepositionlieferdatenDto.setCBezbeilieferant(wtfArtikelbezeichnungbeimlieferanten.getText());
		anfragepositionlieferdatenDto.setCArtikelnrlieferant(wtfArtikelnummerbeimlieferanten.getText());

		anfragepositionlieferdatenDto.setNMindestbestellmenge(wnfMindestbestellmenge.getBigDecimal());
		anfragepositionlieferdatenDto.setNVerpackungseinheit(wnfVerpackungseinheit.getBigDecimal());
		anfragepositionlieferdatenDto.setNStandardmenge(wnfStandardmenge.getBigDecimal());

		anfragepositionlieferdatenDto.setIAnlieferzeit(wnfAnlieferzeit.getInteger());
		anfragepositionlieferdatenDto.setNAnliefermenge(wnfAnliefermenge.getBigDecimal());
		anfragepositionlieferdatenDto.setNNettogesamtpreis(wnfAngeboteinzelpreis.getBigDecimal());
		anfragepositionlieferdatenDto.setBErfasst(new Short((short) 0));

		anfragepositionlieferdatenDto.setZertifikatartIId(wsfZertifikatart.getIKey());
		anfragepositionlieferdatenDto.setTPreisgueltigab(wdfPreisgueltigab.getTimestamp());
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);

		wlaIdent.getWrapperButtonGoTo().setEnabled(true);

		
		wnfLief1Menge.setBigDecimal(null);
		wnfLief1Preis.setBigDecimal(null);
		wnfLief1WBZ.setInteger(null);
		
		// teilnehmer neu einlesen, ausloeser war ev. ein refresh
		Object oKey = getKeyWhenDetailPanel();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {
			anfragepositionlieferdatenDto = DelegateFactory.getInstance().getAnfragepositionDelegate()
					.anfragepositionlieferdatenFindByPrimaryKey((Integer) oKey);

			anfragepositionDto = DelegateFactory.getInstance().getAnfragepositionDelegate()
					.anfragepositionFindByPrimaryKey(anfragepositionlieferdatenDto.getAnfragepositionIId());

			dto2Components();

		}

		tpAnfrage.setTitleAnfrage(LPMain.getInstance().getTextRespectUISPr("anf.panel.positionlieferdaten"));

		// die Anfrage fuer die Statusbar neu einlesen
		if (anfragepositionDto != null) {
			tpAnfrage.setAnfrageDto(DelegateFactory.getInstance().getAnfrageDelegate()
					.anfrageFindByPrimaryKey(anfragepositionDto.getBelegIId()));
		}

		aktualisiereStatusbar();
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAnfrage.getAnfrageDto().getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAnfrage.getAnfrageDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAnfrage.getAnfrageDto().getPersonalIIdAendern());
		setStatusbarTAendern(tpAnfrage.getAnfrageDto().getTAendern());
		setStatusbarStatusCNr(tpAnfrage.getAnfrageStatus());
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ANFRAGE;
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (tpAnfrage.istAktualisierenLieferdatenErlaubt()) {
			DelegateFactory.getInstance().getAnfragepositionDelegate()
					.resetAnfragepositionlieferdaten(anfragepositionlieferdatenDto);
			super.eventActionDelete(e, false, false); // keyWasForLockMe nicht
			// ueberschreiben
		}
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		tpAnfrage.getWlaLieferdatenInfo().setText("");

		LockStateValue lsv = super.getLockedstateDetailMainKey();

		if (tpAnfrage.getAnfrageDto().getIId() != null) {
			if (tpAnfrage.getAnfrageDto().getStatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_STORNIERT)
					|| tpAnfrage.getAnfrageDto().getStatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT)
					|| tpAnfrage.getAnfrageDto().getStatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_ERLEDIGT)) {
				lsv = new LockStateValue(PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}

			if (tpAnfrage.getAnfrageDto().getStatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN)
					&& tpAnfrage.getAnfrageDto().getCAngebotnummer() == null) {
				lsv = new LockStateValue(PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);

				tpAnfrage.getWlaLieferdatenInfo().setForeground(Color.RED);
				tpAnfrage.getWlaLieferdatenInfo()
						.setText(LPMain.getInstance().getTextRespectUISPr("anf.lieferdaten.erfassung.info"));

			}

		}

		return lsv;
	}
}

class FocusAdapterGewicht implements FocusListener {
	private PanelAnfragepositionlieferdaten adaptee = null;

	FocusAdapterGewicht(PanelAnfragepositionlieferdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void focusGained(FocusEvent e) {
		// nothing here
	}

	public void focusLost(FocusEvent e) {
		try {

			if (adaptee.wnfGewichtbestellmenge.getBigDecimal() != null && adaptee.anfragepositionDto != null
					&& adaptee.anfragepositionDto.getArtikelIId() != null) {

				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(adaptee.anfragepositionDto.getArtikelIId());

				if (artikelDto.getNUmrechnungsfaktor() != null) {
					BigDecimal umrechnung = new BigDecimal(0);
					BigDecimal gewicht = adaptee.wnfGewichtbestellmenge.getBigDecimal();
					BigDecimal faktor = artikelDto.getNUmrechnungsfaktor();
					if (gewicht != null || faktor != null) {

						if (Helper.short2boolean(artikelDto.getbBestellmengeneinheitInvers())) {
							umrechnung = gewicht.multiply(faktor);
						} else {
							umrechnung = gewicht.divide(faktor, Defaults.getInstance().getIUINachkommastellenPreiseEK(),
									BigDecimal.ROUND_HALF_EVEN);
						}

						adaptee.wnfAnliefermenge.setBigDecimal(umrechnung);
					}
				}
			}
		} catch (Throwable tDummy) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}

class FocusAdapterGewichtPreis implements FocusListener {
	private PanelAnfragepositionlieferdaten adaptee = null;

	FocusAdapterGewichtPreis(PanelAnfragepositionlieferdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void focusGained(FocusEvent e) {
		// nothing here
	}

	public void focusLost(FocusEvent e) {
		try {
			if (adaptee.wnfGewichtPreis.getBigDecimal() != null && adaptee.anfragepositionDto != null
					&& adaptee.anfragepositionDto.getArtikelIId() != null) {

				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(adaptee.anfragepositionDto.getArtikelIId());

				if (artikelDto.getNUmrechnungsfaktor() != null) {
					BigDecimal umrechnung = new BigDecimal(0);
					BigDecimal preis = adaptee.wnfGewichtPreis.getBigDecimal();
					BigDecimal faktor = artikelDto.getNUmrechnungsfaktor();
					if (preis != null || faktor != null) {
						if (Helper.short2boolean(artikelDto.getbBestellmengeneinheitInvers())) {
							umrechnung = preis.divide(faktor, Defaults.getInstance().getIUINachkommastellenPreiseEK(),
									BigDecimal.ROUND_HALF_EVEN);
						} else {
							umrechnung = preis.multiply(faktor);
						}
						adaptee.wnfAngeboteinzelpreis.setBigDecimal(umrechnung);
					}
				}
			}

		} catch (Throwable tDummy) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}

class WnfGelieferteMengeFocusAdapter extends java.awt.event.FocusAdapter {
	private PanelAnfragepositionlieferdaten adaptee;

	WnfGelieferteMengeFocusAdapter(PanelAnfragepositionlieferdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {

		if (adaptee.anfragepositionDto != null && adaptee.anfragepositionDto.getArtikelIId() != null) {
			try {
				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(adaptee.anfragepositionDto.getArtikelIId());

				if (artikelDto != null && artikelDto.getNUmrechnungsfaktor() != null) {

					if (adaptee.wnfAnliefermenge.getBigDecimal() != null) {
						if (Helper.short2boolean(artikelDto.getbBestellmengeneinheitInvers())) {

							adaptee.wnfGewichtbestellmenge.setBigDecimal(
									adaptee.wnfAnliefermenge.getBigDecimal().divide(artikelDto.getNUmrechnungsfaktor(),
											Defaults.getInstance().getIUINachkommastellenPreiseEK(),
											BigDecimal.ROUND_HALF_EVEN));
						} else {
							adaptee.wnfGewichtbestellmenge.setBigDecimal(adaptee.wnfAnliefermenge.getBigDecimal()
									.multiply(artikelDto.getNUmrechnungsfaktor()));
						}

					}

				}
			} catch (Throwable tDummy) {
				LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
			}
		}

	}
}

class WnfGelieferterPreisFocusAdapter extends java.awt.event.FocusAdapter {
	private PanelAnfragepositionlieferdaten adaptee;

	WnfGelieferterPreisFocusAdapter(PanelAnfragepositionlieferdaten adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			if (adaptee.wnfAngeboteinzelpreis.getBigDecimal() != null && adaptee.anfragepositionDto != null
					&& adaptee.anfragepositionDto.getArtikelIId() != null) {

				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(adaptee.anfragepositionDto.getArtikelIId());

				if (artikelDto.getNUmrechnungsfaktor() != null) {

					BigDecimal nPreisPerEinheit = null;
					if (Helper.short2boolean(artikelDto.getbBestellmengeneinheitInvers())) {
						nPreisPerEinheit = (adaptee.wnfAngeboteinzelpreis.getBigDecimal()
								.multiply(artikelDto.getNUmrechnungsfaktor()));
					} else {
						nPreisPerEinheit = (adaptee.wnfAngeboteinzelpreis.getBigDecimal().divide(
								artikelDto.getNUmrechnungsfaktor(),
								Defaults.getInstance().getIUINachkommastellenPreiseEK(), BigDecimal.ROUND_HALF_EVEN));
					}

					adaptee.wnfGewichtPreis.setBigDecimal(nPreisPerEinheit);
				}

			}

		} catch (Throwable tDummy) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}
