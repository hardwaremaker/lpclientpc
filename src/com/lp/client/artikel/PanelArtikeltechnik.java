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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDynamischHelper;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.FarbcodeDto;
import com.lp.server.artikel.service.GeometrieDto;
import com.lp.server.artikel.service.LaseroberflaecheDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.MontageDto;
import com.lp.server.artikel.service.VerpackungDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelArtikeltechnik extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArtikelDto artikelDto = null;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private WrapperLabel wlaBauform = new WrapperLabel();
	private WrapperTextField wtfBauform = new WrapperTextField();
	private WrapperLabel wlaVerpackungsart = new WrapperLabel();
	private WrapperTextField wtfVerpackungsart = new WrapperTextField();
	private WrapperLabel wlaBreite = new WrapperLabel();
	WrapperLabel wlaProzentAufschlag = new WrapperLabel();
	WrapperLabel wlaAufschlagMandantenwhg = new WrapperLabel();

	private WrapperLabel wla5 = new WrapperLabel();
	private WrapperLabel wlaHoehe = new WrapperLabel();
	private WrapperLabel wlaTiefe = new WrapperLabel();
	private WrapperLabel wlaGewicht = new WrapperLabel();
	private WrapperLabel wla9 = new WrapperLabel();
	private WrapperLabel wlaRasterliegend = new WrapperLabel();
	private WrapperLabel wlaStromverbrauchtypisch = new WrapperLabel();
	private WrapperLabel wlaMaterialgewicht = new WrapperLabel();
	private WrapperButton wbuMaterial = new WrapperButton();
	private WrapperLabel wlaZuschlagEK = new WrapperLabel();
	private WrapperLabel wlaZuschlagVK = new WrapperLabel();
	private WrapperLabel wlaRasterstehend = new WrapperLabel();
	private WrapperLabel wlaStromverbrauchmaximal = new WrapperLabel();
	private WrapperTextField wtfMaterialkennung = new WrapperTextField();
	private WrapperLabel wla17 = new WrapperLabel();
	private WrapperLabel wla18 = new WrapperLabel();
	private WrapperLabel wlaAufschlag = new WrapperLabel();
	private WrapperLabel wlaMaterialgewichteinheit = new WrapperLabel();
	private WrapperTextField wtfMaterialbezeichnung = new WrapperTextField();
	private WrapperNumberField wnfMaterialgewicht = null;
	private WrapperNumberField wnfStromverbrauchtypisch = null;
	private WrapperNumberField wnfZuschlagEK = null;
	private WrapperNumberField wnfZuschlagVK = null;
	private WrapperNumberField wnfHoehe = null;
	private WrapperNumberField wnfBreite = null;
	private WrapperNumberField wnfTiefe = null;
	private WrapperNumberField wnfGewicht = null;
	private WrapperNumberField wnfAufschlagBetrag = new WrapperNumberField();
	private WrapperNumberField wnfAufschlagProzent = new WrapperNumberField();

	private WrapperNumberField wnfStromverbrauchmaximal = null;
	private WrapperNumberField wnfRasterliegend = null;
	private WrapperNumberField wnfRasterstehend = null;
	private WrapperTextField wtfTextbreite = new WrapperTextField();
	private WrapperCheckBox wcbHochstellen = new WrapperCheckBox();
	private WrapperCheckBox wcbHochsetzen = new WrapperCheckBox();
	private WrapperCheckBox wcbPolarisiert = new WrapperCheckBox();
	private WrapperCheckBox wcbAntistatic = new WrapperCheckBox();
	private PanelQueryFLR panelQueryFLRMaterial = null;
	private PanelQueryFLR panelQueryFLRFarbcode = null;
	static final public String ACTION_SPECIAL_MATERIAL_FROM_LISTE = "action_material_from_liste";
	static final public String ACTION_SPECIAL_FARBCODE_FROM_LISTE = "action_farbcode_from_liste";

	private PanelQueryFLR panelQueryFLRLaseroberflaeche = null;
	static final public String ACTION_SPECIAL_LASEROBERFLAECHE_FROM_LISTE = "action_laseroberlflaeche_from_liste";

	private WrapperLabel wla1 = new WrapperLabel();
	private WrapperLabel wlaZBez2 = new WrapperLabel();
	private WrapperLabel wlaZBez = new WrapperLabel();
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperLabel wlaArtikelnummer = new WrapperLabel();
	private WrapperTextField wtfZBez2 = new WrapperTextField();
	private WrapperTextField wtfZBez = new WrapperTextField();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperTextField wtfArtikelnummer = new WrapperTextField();
	private WrapperLabel wla2 = new WrapperLabel();
	private WrapperLabel wla3 = new WrapperLabel();
	private WrapperLabel wlaEinheitRasterliegend = new WrapperLabel();
	private WrapperLabel wlaEinheitRasterstehend = new WrapperLabel();
	private WrapperLabel wlaEinheitStromverbrauchtypisch = new WrapperLabel();
	private WrapperLabel wlaEinheitStromverbrauchMax = new WrapperLabel();
	private WrapperSelectField wsfReach = new WrapperSelectField(WrapperSelectField.REACH, getInternalFrame(), true);
	private WrapperSelectField wsfRohs = new WrapperSelectField(WrapperSelectField.ROHS, getInternalFrame(), true);
	private WrapperSelectField wsfAutomotive = new WrapperSelectField(WrapperSelectField.AUTOMOTIVE, getInternalFrame(),
			true);
	private WrapperSelectField wsfMedical = new WrapperSelectField(WrapperSelectField.MEDICAL, getInternalFrame(),
			true);
	private WrapperLabel wlaUL = new WrapperLabel();
	private WrapperTextField wtfUL = new WrapperTextField();

	private WrapperButton wbuFarbcodierung = new WrapperButton();
	private WrapperTextField wtfFarbcodierung = new WrapperTextField();

	private WrapperButton wbuLaseroberflaeche = new WrapperButton();
	private WrapperTextField wtfLaseroberflaeche = new WrapperTextField();

	private JScrollPane technikEigenschaften = new JScrollPane();

	private boolean bGrammStattKilo = false;

	PanelDynamischHelper phd = null;

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfTextbreite;
	}

	public PanelArtikeltechnik(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	void dialogQueryMaterialFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRMaterial = ArtikelFilterFactory.getInstance().createPanelFLRMaterial(getInternalFrame(),
				artikelDto.getMaterialIId(), true);

		new DialogQuery(panelQueryFLRMaterial);
	}

	void dialogQueryFarbcodeFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };

		panelQueryFLRFarbcode = new PanelQueryFLR(null, null, QueryParameters.UC_ID_FARBCODE, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("artikel.farbcode"));
		panelQueryFLRFarbcode.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDKennung(),
				SystemFilterFactory.getInstance().createFKDBezeichnung());
		panelQueryFLRFarbcode.setSelectedId(artikelDto.getFarbcodeIId());

		new DialogQuery(panelQueryFLRFarbcode);
	}

	void dialogQueryLaseroberflaecheFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };

		panelQueryFLRLaseroberflaeche = new PanelQueryFLR(null, null, QueryParameters.UC_ID_LASEROBERFLAECHE,
				aWhichButtonIUse, getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("artikel.laseroberflaeche"));
		panelQueryFLRLaseroberflaeche.befuellePanelFilterkriterienDirekt(
				SystemFilterFactory.getInstance().createFKDKennung(),
				SystemFilterFactory.getInstance().createFKDBezeichnung());
		panelQueryFLRLaseroberflaeche.setSelectedId(artikelDto.getLaseroberflaecheIId());

		new DialogQuery(panelQueryFLRLaseroberflaeche);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_MATERIAL_FROM_LISTE)) {
			dialogQueryMaterialFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FARBCODE_FROM_LISTE)) {
			dialogQueryFarbcodeFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LASEROBERFLAECHE_FROM_LISTE)) {
			dialogQueryLaseroberflaecheFromListe(e);
		}
	}

	protected void dto2Components() throws Throwable {
		// wtfMaterialkennung.setText(artikelDto.getMaterialCNr());
		wcbAntistatic.setShort(artikelDto.getBAntistatic());

		wnfGewicht.setDouble(artikelDto.getFGewichtkg());

		if (bGrammStattKilo) {

			if (artikelDto.getFGewichtkg() != null) {
				wnfGewicht.setDouble(artikelDto.getFGewichtkg() * 1000);
			}

		}

		if (artikelDto.getFMaterialgewicht() != null) {
			wnfMaterialgewicht.setBigDecimal(new BigDecimal(artikelDto.getFMaterialgewicht()));
		} else {
			wnfMaterialgewicht.setBigDecimal(null);
		}

		wnfStromverbrauchmaximal.setDouble(artikelDto.getFStromverbrauchmax());
		wnfStromverbrauchtypisch.setDouble(artikelDto.getFStromverbrauchtyp());

		wnfAufschlagProzent.setDouble(artikelDto.getFAufschlagProzent());
		wnfAufschlagBetrag.setBigDecimal(artikelDto.getNAufschlagBetrag());

		wlaMaterialgewichteinheit.setText("g/" + artikelDto.getEinheitCNr());

		if (bGrammStattKilo) {
			wla9.setText("g/" + artikelDto.getEinheitCNr());
		} else {
			wla9.setText("kg/" + artikelDto.getEinheitCNr());
		}

		wla17.setText(DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getInstance().getTheClient().getMandant()).getWaehrungCNr() + "/"
				+ artikelDto.getEinheitCNr().trim());
		wla18.setText(DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getInstance().getTheClient().getMandant()).getWaehrungCNr() + "/"
				+ artikelDto.getEinheitCNr().trim());
		if (artikelDto.getMaterialIId() != null) {

			wnfZuschlagEK.setBigDecimal(
					DelegateFactory.getInstance().getMaterialDelegate().getMaterialzuschlagEKInZielwaehrung(
							artikelDto.getIId(), null, new java.sql.Date(System.currentTimeMillis()),
							LPMain.getInstance().getTheClient().getSMandantenwaehrung()));

			wnfZuschlagVK.setBigDecimal(
					DelegateFactory.getInstance().getMaterialDelegate().getMaterialzuschlagVKInZielwaehrung(
							artikelDto.getIId(), null, new java.sql.Date(System.currentTimeMillis()),
							LPMain.getInstance().getTheClient().getSMandantenwaehrung()));

		}
		if (artikelDto.getMaterialIId() != null) {
			MaterialDto materialDto = DelegateFactory.getInstance().getMaterialDelegate()
					.materialFindByPrimaryKey(artikelDto.getMaterialIId());
			wtfMaterialkennung.setText(materialDto.getCNr());
			if (materialDto.getMaterialsprDto() != null) {
				wtfMaterialbezeichnung.setText(materialDto.getMaterialsprDto().getCBez());
			}
		} else {
			wtfMaterialkennung.setText(null);
			wtfMaterialbezeichnung.setText(null);
			wnfZuschlagEK.setBigDecimal(null);
		}

		if (artikelDto.getFarbcodeIId() != null) {
			FarbcodeDto farbcodeDto = DelegateFactory.getInstance().getArtikelDelegate()
					.farbcodeFindByPrimaryKey(artikelDto.getFarbcodeIId());
			wtfFarbcodierung.setText(farbcodeDto.getBezeichnung());
		} else {
			wtfFarbcodierung.setText(null);
		}

		if (artikelDto.getLaseroberflaecheIId() != null) {
			LaseroberflaecheDto laseroberflaecheDto = DelegateFactory.getInstance().getArtikelDelegate()
					.laseroberflaecheFindByPrimaryKey(artikelDto.getLaseroberflaecheIId());
			wtfLaseroberflaeche.setText(laseroberflaecheDto.getBezeichnung());
		} else {
			wtfLaseroberflaeche.setText(null);
		}

		if (artikelDto.getVerpackungDto() != null) {
			wtfVerpackungsart.setText(artikelDto.getVerpackungDto().getCVerpackungsart());
			wtfBauform.setText(artikelDto.getVerpackungDto().getCBauform());
		}

		if (artikelDto.getMontageDto() != null) {
			wcbHochsetzen.setShort(artikelDto.getMontageDto().getBHochsetzen());
			wcbHochstellen.setShort(artikelDto.getMontageDto().getBHochstellen());
			wcbPolarisiert.setShort(artikelDto.getMontageDto().getBPolarisiert());
			if (artikelDto.getMontageDto().getFRasterliegend() != null) {
				wnfRasterliegend.setBigDecimal(new BigDecimal(artikelDto.getMontageDto().getFRasterliegend()));

			} else {
				wnfRasterliegend.setBigDecimal(null);
			}

			if (artikelDto.getMontageDto().getFRasterstehend() != null) {
				wnfRasterstehend.setBigDecimal(new BigDecimal(artikelDto.getMontageDto().getFRasterstehend()));

			} else {
				wnfRasterstehend.setBigDecimal(null);
			}

		}
		if (artikelDto.getGeometrieDto() != null) {
			wnfHoehe.setDouble(artikelDto.getGeometrieDto().getFHoehe());
			wnfTiefe.setDouble(artikelDto.getGeometrieDto().getFTiefe());
			wnfBreite.setDouble(artikelDto.getGeometrieDto().getFBreite());
			wtfTextbreite.setText(artikelDto.getGeometrieDto().getCBreitetext());
		}
		wtfArtikelnummer.setText(artikelDto.getCNr());
		if (artikelDto.getArtikelsprDto() != null) {
			wtfBezeichnung.setText(artikelDto.getArtikelsprDto().getCBez());
			wtfZBez.setText(artikelDto.getArtikelsprDto().getCZbez());
			wtfZBez2.setText(artikelDto.getArtikelsprDto().getCZbez2());
		}

		wtfUL.setText(artikelDto.getCUL());
		wsfReach.setKey(artikelDto.getReachIId());
		wsfRohs.setKey(artikelDto.getRohsIId());
		wsfAutomotive.setKey(artikelDto.getAutomotiveIId());
		wsfMedical.setKey(artikelDto.getMedicalIId());

		this.setStatusbarPersonalIIdAendern(artikelDto.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(artikelDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(artikelDto.getTAnlegen());
		this.setStatusbarTAendern(artikelDto.getTAendern());

	}

	protected void components2Dto() throws ExceptionLP {
		artikelDto.setBAntistatic(wcbAntistatic.getShort());
		artikelDto.setFGewichtkg(wnfGewicht.getDouble());

		// PJ19853
		if (bGrammStattKilo && artikelDto.getFGewichtkg() != null) {
			artikelDto.setFGewichtkg(wnfGewicht.getDouble() / 1000);

		}

		artikelDto.setFMaterialgewicht(wnfMaterialgewicht.getDouble());
		artikelDto.setFAufschlagProzent(wnfAufschlagProzent.getDouble());
		artikelDto.setNAufschlagBetrag(wnfAufschlagBetrag.getBigDecimal());

		artikelDto.setFStromverbrauchmax(wnfStromverbrauchmaximal.getDouble());
		artikelDto.setFStromverbrauchtyp(wnfStromverbrauchtypisch.getDouble());

		artikelDto.setGeometrieDto(new GeometrieDto());
		artikelDto.getGeometrieDto().setFBreite(wnfBreite.getDouble());
		artikelDto.getGeometrieDto().setFHoehe(wnfHoehe.getDouble());
		artikelDto.getGeometrieDto().setFTiefe(wnfTiefe.getDouble());
		artikelDto.getGeometrieDto().setCBreitetext(wtfTextbreite.getText());

		artikelDto.setMontageDto(new MontageDto());
		artikelDto.getMontageDto().setBHochsetzen(wcbHochsetzen.getShort());
		artikelDto.getMontageDto().setBHochstellen(wcbHochstellen.getShort());
		artikelDto.getMontageDto().setBPolarisiert(wcbPolarisiert.getShort());
		artikelDto.getMontageDto().setFRasterliegend(wnfRasterliegend.getDouble());
		artikelDto.getMontageDto().setFRasterstehend(wnfRasterstehend.getDouble());

		artikelDto.setVerpackungDto(new VerpackungDto());
		artikelDto.getVerpackungDto().setCBauform(wtfBauform.getText());
		artikelDto.getVerpackungDto().setCVerpackungsart(wtfVerpackungsart.getText());
		artikelDto.setCUL(wtfUL.getText());
		artikelDto.setReachIId(wsfReach.getIKey());
		artikelDto.setRohsIId(wsfRohs.getIKey());
		artikelDto.setAutomotiveIId(wsfAutomotive.getIKey());
		artikelDto.setMedicalIId(wsfMedical.getIKey());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();
			DelegateFactory.getInstance().getArtikelDelegate().updateArtikel(artikelDto);

			if (DelegateFactory.getInstance().getPanelDelegate()
					.panelbeschreibungVorhanden(PanelFac.PANEL_ARTIKELTECHNIK)) {

				DelegateFactory.getInstance().getPanelDelegate()
						.createPaneldaten(phd.components2Dto(artikelDto.getIId() + ""));
			}

			((InternalFrameArtikel) getInternalFrame()).setArtikelDto(artikelDto);
			super.eventActionSave(e, true);
			eventYouAreSelected(false);
		}
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		InternalFrameArtikel internalFrameArtikel = (InternalFrameArtikel) getInternalFrame();

		if (internalFrameArtikel.getTabbedPaneArtikel().bArtikelfreigabe == true) {
			// Wenn Fregegeben, dann nicht mehr aenderbar
			if (internalFrameArtikel.getArtikelDto() != null
					&& internalFrameArtikel.getArtikelDto().getTFreigabe() != null) {
				// PJ22070
				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
						.getParameterDelegate()
						.getParametermandant(ParameterFac.PARAMETER_TECHNIK_BEI_ARTIKELFREIGABE_AENDERBAR,
								ParameterFac.KATEGORIE_ARTIKEL, LPMain.getTheClient().getMandant());
				boolean bTechnikDochAenderbar = (Boolean) parameter.getCWertAsObject();

				if (bTechnikDochAenderbar == false) {
					lockStateValue = new LockStateValue(PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
				}
			}
		}

		return lockStateValue;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRMaterial) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				MaterialDto materialDto = DelegateFactory.getInstance().getMaterialDelegate()
						.materialFindByPrimaryKey((Integer) key);
				wtfMaterialkennung.setText(materialDto.getCNr());
				if (materialDto.getMaterialsprDto() != null) {
					wtfMaterialbezeichnung.setText(materialDto.getMaterialsprDto().getCBez());
				}
				artikelDto.setMaterialIId(materialDto.getIId());
				if (artikelDto.getMaterialIId() != null) {

					BigDecimal bZuschlag = DelegateFactory.getInstance().getMaterialDelegate()
							.materialzuschlagFindAktuellenzuschlag(artikelDto.getMaterialIId());
					if (bZuschlag != null && wnfMaterialgewicht.getBigDecimal() != null) {
						wnfZuschlagEK.setBigDecimal(
								DelegateFactory.getInstance().getMaterialDelegate().getMaterialzuschlagEKInZielwaehrung(
										artikelDto.getIId(), null, new java.sql.Date(System.currentTimeMillis()),
										LPMain.getInstance().getTheClient().getSMandantenwaehrung()));
					}
					if (wnfMaterialgewicht.getBigDecimal() == null) {
						// SP3504
						if (LPMain.getInstance().getDesktop()
								.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MATERIALZUSCHLAG)) {

							DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.warning"), LPMain
									.getInstance().getTextRespectUISPr("artikel.warning.keinmaterialgewichtdefiniert"));
						}
					}
				}

			} else if (e.getSource() == panelQueryFLRFarbcode) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				FarbcodeDto farbcodeDto = DelegateFactory.getInstance().getArtikelDelegate()
						.farbcodeFindByPrimaryKey((Integer) key);
				wtfFarbcodierung.setText(farbcodeDto.getBezeichnung());
				artikelDto.setFarbcodeIId(farbcodeDto.getIId());
			} else if (e.getSource() == panelQueryFLRLaseroberflaeche) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				LaseroberflaecheDto laseroberflaecheDto = DelegateFactory.getInstance().getArtikelDelegate()
						.laseroberflaecheFindByPrimaryKey((Integer) key);

				wtfLaseroberflaeche.setText(laseroberflaecheDto.getBezeichnung());
				artikelDto.setLaseroberflaecheIId(laseroberflaecheDto.getIId());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRMaterial) {

				wtfMaterialkennung.setText(null);
				wtfMaterialbezeichnung.setText(null);
				artikelDto.setMaterialIId(null);
			} else if (e.getSource() == panelQueryFLRFarbcode) {

				wtfFarbcodierung.setText(null);
				artikelDto.setFarbcodeIId(null);
			} else if (e.getSource() == panelQueryFLRLaseroberflaeche) {
				wtfLaseroberflaeche.setText(null);
				artikelDto.setLaseroberflaecheIId(null);
			}
		}

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);
		leereAlleFelder(this);
		artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(((InternalFrameArtikel) getInternalFrame()).getArtikelDto().getIId());

		phd = new PanelDynamischHelper(PanelFac.PANEL_ARTIKELTECHNIK, artikelDto.getArtgruIId(), getInternalFrame());

		if (DelegateFactory.getInstance().getPanelDelegate()
				.panelbeschreibungVorhanden(PanelFac.PANEL_ARTIKELTECHNIK)) {
			technikEigenschaften.getViewport().removeAll();
			technikEigenschaften.getViewport().add(phd.panelErzeugen());
			enableAllComponents(technikEigenschaften, false);
			phd.dto2Components(artikelDto.getIId() + "");

		}

		dto2Components();
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
		enableAllComponents(technikEigenschaften, true);
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		enableAllComponents(technikEigenschaften, false);
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wnfMaterialgewicht = new WrapperNumberField();
		wnfMaterialgewicht.setFractionDigits(4);
		wnfStromverbrauchtypisch = new WrapperNumberField();
		wnfZuschlagEK = new WrapperNumberField();
		wnfZuschlagEK.setFractionDigits(6);
		wnfZuschlagVK = new WrapperNumberField();
		wnfZuschlagVK.setFractionDigits(6);
		wnfHoehe = new WrapperNumberField();
		wnfHoehe.setFractionDigits(Defaults.getInstance().getIUINachkommastellenDimensionen());

		wnfBreite = new WrapperNumberField();
		wnfBreite.setFractionDigits(Defaults.getInstance().getIUINachkommastellenDimensionen());

		wnfTiefe = new WrapperNumberField();
		wnfTiefe.setFractionDigits(Defaults.getInstance().getIUINachkommastellenDimensionen());

		wnfGewicht = new WrapperNumberField();
		wnfGewicht.setMinimumValue(new BigDecimal(0));
		wnfGewicht.setMaximumValue(new BigDecimal(100000));
		wnfGewicht.setFractionDigits(5);

		// PJ19853
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_ARTIKELGEWICHT_GRAMM_STATT_KILO,
						ParameterFac.KATEGORIE_ARTIKEL, LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null) {
			bGrammStattKilo = (Boolean) parameter.getCWertAsObject();
		}

		if (bGrammStattKilo) {
			wnfGewicht.setFractionDigits(2);
		}

		wnfStromverbrauchmaximal = new WrapperNumberField();
		wnfRasterliegend = new WrapperNumberField();

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_RASTERLIEGEND_NACHKOMMASTELLEN, ParameterFac.KATEGORIE_ARTIKEL,
				LPMain.getTheClient().getMandant());
		Integer iNk = (Integer) parameter.getCWertAsObject();

		wnfRasterliegend.setFractionDigits(iNk);

		wnfRasterstehend = new WrapperNumberField();

		wlaBauform.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.bauform"));
		wtfBauform.setColumnsMax(ArtikelFac.MAX_ARTIKEL_BAUFORM);
		wtfBauform.setText("");
		wlaVerpackungsart.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.verpackungsart"));

		wtfFarbcodierung.setActivatable(false);

		wtfLaseroberflaeche.setActivatable(false);

		wtfVerpackungsart.setColumnsMax(ArtikelFac.MAX_ARTIKEL_VERPACKUNGSART);
		wtfVerpackungsart.setText("");
		wlaBreite.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.breite"));
		wla5.setHorizontalAlignment(SwingConstants.LEFT);
		wla5.setText("mm");
		wlaHoehe.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.hoehe"));
		wlaTiefe.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.tiefe"));
		wlaGewicht.setText(LPMain.getInstance().getTextRespectUISPr("lp.gewicht"));

		wlaAufschlag.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.aufschlagmaterial"));

		wla9.setHorizontalAlignment(SwingConstants.LEFT);
		wla9.setHorizontalTextPosition(SwingConstants.TRAILING);
		wla9.setText("kg");
		wlaRasterliegend.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.rasterliegend"));
		wlaStromverbrauchtypisch
				.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.stromverbrauchtypisch"));
		wlaMaterialgewicht.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.materialgewicht"));
		wbuMaterial.setText(LPMain.getInstance().getTextRespectUISPr("lp.material") + "...");
		wbuMaterial.setActionCommand(PanelArtikeltechnik.ACTION_SPECIAL_MATERIAL_FROM_LISTE);
		wbuMaterial.addActionListener(this);

		wbuFarbcodierung.setText(LPMain.getInstance().getTextRespectUISPr("artikel.farbcode") + "...");

		wlaUL.setText(LPMain.getInstance().getTextRespectUISPr("artikel.ul"));

		wbuFarbcodierung.setActionCommand(PanelArtikeltechnik.ACTION_SPECIAL_FARBCODE_FROM_LISTE);
		wbuFarbcodierung.addActionListener(this);

		wbuLaseroberflaeche.setText(LPMain.getInstance().getTextRespectUISPr("artikel.laseroberflaeche") + "...");
		wbuLaseroberflaeche.setActionCommand(PanelArtikeltechnik.ACTION_SPECIAL_LASEROBERFLAECHE_FROM_LISTE);
		wbuLaseroberflaeche.addActionListener(this);

		wlaZuschlagEK.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.zuschlag.ek"));
		wlaZuschlagVK.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.zuschlag.vk"));
		wlaRasterstehend.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.rasterstehend"));
		wlaStromverbrauchmaximal
				.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.stromverbrauchmaximal"));
		wtfMaterialkennung.setEditable(false);
		wtfMaterialkennung.setText("");
		wtfMaterialkennung.setActivatable(false);
		wnfZuschlagEK.setEditable(false);
		wnfZuschlagEK.setActivatable(false);
		wnfZuschlagEK.setDependenceField(true);

		wnfZuschlagVK.setEditable(false);
		wnfZuschlagVK.setActivatable(false);
		wnfZuschlagVK.setDependenceField(true);
		wla17.setHorizontalAlignment(SwingConstants.LEFT);
		wla17.setText("WHG/EHT");
		wla18.setHorizontalAlignment(SwingConstants.LEFT);
		wla18.setText("WHG/EHT");
		wlaMaterialgewichteinheit.setHorizontalAlignment(SwingConstants.LEFT);
		wlaMaterialgewichteinheit.setText("g/EHT");
		wtfMaterialbezeichnung.setEditable(false);
		wtfMaterialbezeichnung.setText("");
		wtfMaterialbezeichnung.setActivatable(false);

		wtfTextbreite.setColumnsMax(ArtikelFac.MAX_ARTIKEL_TEXTBREITE);
		wtfTextbreite.setText("");
		wcbHochstellen.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.hochstellen"));
		wcbHochsetzen.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.hochsetzen"));
		wcbPolarisiert.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.polarisiert"));
		wcbAntistatic.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.antistatic"));
		wla1.setMinimumSize(new Dimension(10, 4));
		wla1.setPreferredSize(new Dimension(10, 4));
		wla1.setText("");
		wlaZBez2.setText(LPMain.getInstance().getTextRespectUISPr("lp.zusatzbez2"));
		wlaZBez.setText(LPMain.getInstance().getTextRespectUISPr("lp.zusatzbezeichnung"));
		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));
		wlaArtikelnummer.setText(LPMain.getInstance().getTextRespectUISPr("artikel.artikelnummer"));
		wlaArtikelnummer.setPreferredSize(new Dimension(150, 0));
		wtfZBez2.setText("");
		wtfZBez2.setActivatable(false);
		wtfZBez.setText("");
		wtfZBez.setActivatable(false);
		wtfBezeichnung.setText("");
		wtfBezeichnung.setActivatable(false);

		int iLaengenBezeichung = DelegateFactory.getInstance().getArtikelDelegate().getLaengeArtikelBezeichnungen();
		wtfBezeichnung.setColumnsMax(iLaengenBezeichung);
		wtfZBez.setColumnsMax(iLaengenBezeichung);
		wtfZBez2.setColumnsMax(iLaengenBezeichung);

		wtfArtikelnummer.setText("");
		wtfArtikelnummer.setActivatable(false);
		wla2.setHorizontalAlignment(SwingConstants.LEFT);
		wla2.setText("mm");
		wla3.setHorizontalAlignment(SwingConstants.LEFT);
		wla3.setText("mm");

		wlaEinheitRasterliegend.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitRasterliegend
				.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.einheit.rasterliegend"));
		wlaEinheitRasterstehend.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitRasterstehend
				.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.einheit.rasterstehend"));
		wlaEinheitStromverbrauchtypisch.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitStromverbrauchtypisch
				.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.einheit.stromverbrauchtypisch"));
		wlaEinheitStromverbrauchMax.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitStromverbrauchMax
				.setText(LPMain.getInstance().getTextRespectUISPr("artikel.technik.einheit.stromverbrauchmaximal"));

		jpaWorkingOn = new JPanel(new MigLayout("wrap 9", "[20%][10%][10%][10%][15%][10%][10%][10%][5%]"));
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaArtikelnummer, "growx");
		jpaWorkingOn.add(wtfArtikelnummer, "growx, span 4, wrap");

		jpaWorkingOn.add(wlaBezeichnung, "growx");
		jpaWorkingOn.add(wtfBezeichnung, "growx, span");

		jpaWorkingOn.add(wlaZBez, "growx");
		jpaWorkingOn.add(wtfZBez, "growx, span");

		jpaWorkingOn.add(wlaZBez2, "growx");
		jpaWorkingOn.add(wtfZBez2, "growx, span, wrap 20");

		jpaWorkingOn.add(wlaBreite, "growx");
		jpaWorkingOn.add(wtfTextbreite, "growx");
		jpaWorkingOn.add(wnfBreite, "growx");
		jpaWorkingOn.add(wla2, "growx");
		jpaWorkingOn.add(wlaBauform, "growx, span 2");
		jpaWorkingOn.add(wtfBauform, "growx, span");

		jpaWorkingOn.add(wlaHoehe, "growx");
		jpaWorkingOn.add(wnfHoehe, "growx, span 2");
		jpaWorkingOn.add(wla3, "growx");
		jpaWorkingOn.add(wlaVerpackungsart, "growx, span 2");
		jpaWorkingOn.add(wtfVerpackungsart, "growx, span");

		jpaWorkingOn.add(wlaTiefe, "growx");
		jpaWorkingOn.add(wnfTiefe, "growx, span 2");
		jpaWorkingOn.add(wla5, "growx");
		jpaWorkingOn.add(wcbHochsetzen, "growx, span 2");
		jpaWorkingOn.add(wcbHochstellen, "growx, span ");

		jpaWorkingOn.add(wlaGewicht, "growx");
		jpaWorkingOn.add(wnfGewicht, "growx, span 2");
		jpaWorkingOn.add(wla9, "growx");
		jpaWorkingOn.add(wcbPolarisiert, "growx, span 2");
		jpaWorkingOn.add(wcbAntistatic, "growx, span ");

		jpaWorkingOn.add(wlaRasterliegend, "growx");
		jpaWorkingOn.add(wnfRasterliegend, "growx, span 2");
		jpaWorkingOn.add(wlaEinheitRasterliegend, "growx");
		jpaWorkingOn.add(wsfReach.getWrapperButton(), "growx, span 2");
		jpaWorkingOn.add(wsfReach.getWrapperTextField(), "growx, span ");

		jpaWorkingOn.add(wlaRasterstehend, "growx");
		jpaWorkingOn.add(wnfRasterstehend, "growx, span 2");
		jpaWorkingOn.add(wlaEinheitRasterstehend, "growx");
		jpaWorkingOn.add(wsfRohs.getWrapperButton(), "growx, span 2");
		jpaWorkingOn.add(wsfRohs.getWrapperTextField(), "growx, span ");

		jpaWorkingOn.add(wlaStromverbrauchtypisch, "growx");
		jpaWorkingOn.add(wnfStromverbrauchtypisch, "growx, span 2");
		jpaWorkingOn.add(wlaEinheitStromverbrauchtypisch, "growx");
		jpaWorkingOn.add(wsfAutomotive.getWrapperButton(), "growx, span 2");
		jpaWorkingOn.add(wsfAutomotive.getWrapperTextField(), "growx, span ");

		jpaWorkingOn.add(wlaStromverbrauchmaximal, "growx");
		jpaWorkingOn.add(wnfStromverbrauchmaximal, "growx, span 2");
		jpaWorkingOn.add(wlaEinheitStromverbrauchMax, "growx");
		jpaWorkingOn.add(wsfMedical.getWrapperButton(), "growx, span 2");
		jpaWorkingOn.add(wsfMedical.getWrapperTextField(), "growx, span ");

		jpaWorkingOn.add(wlaUL, "growx, span 6");
		// jpaWorkingOn.add(new WrapperLabel(), "growx, span 2");
		// jpaWorkingOn.add(new WrapperLabel(), "growx");
		// jpaWorkingOn.add(new WrapperLabel(), "growx, span 2");
		jpaWorkingOn.add(wtfUL, "growx, span, wrap ");

		jpaWorkingOn.add(wlaMaterialgewicht, "growx");
		jpaWorkingOn.add(wnfMaterialgewicht, "growx, span 2");
		jpaWorkingOn.add(wlaMaterialgewichteinheit, "growx");
		jpaWorkingOn.add(wbuFarbcodierung, "growx, span 2");
		jpaWorkingOn.add(wtfFarbcodierung, "growx, span");

		jpaWorkingOn.add(wbuMaterial, "growx");
		jpaWorkingOn.add(wtfMaterialkennung, "growx, span 2");
		jpaWorkingOn.add(wtfMaterialbezeichnung, "skip, growx, span");

		jpaWorkingOn.add(wlaZuschlagEK, "growx");
		jpaWorkingOn.add(wnfZuschlagEK, "growx, span 2");
		jpaWorkingOn.add(wla17, "growx");
		jpaWorkingOn.add(wbuLaseroberflaeche, "growx, span 2");
		jpaWorkingOn.add(wtfLaseroberflaeche, "growx, span");

		jpaWorkingOn.add(wlaZuschlagVK, "growx");
		jpaWorkingOn.add(wnfZuschlagVK, "growx, span 2");

		jpaWorkingOn.add(wla18, "growx");
		jpaWorkingOn.add(wlaAufschlag, "growx");
		jpaWorkingOn.add(wnfAufschlagBetrag, "growx");
		wlaAufschlagMandantenwhg = new WrapperLabel(LPMain.getTheClient().getSMandantenwaehrung());
		wlaAufschlagMandantenwhg.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaAufschlagMandantenwhg, "growx");
		jpaWorkingOn.add(wnfAufschlagProzent, "growx");
		wlaProzentAufschlag.setText("%");
		wlaProzentAufschlag.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaProzentAufschlag, "growx, wrap");

		ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(((InternalFrameArtikel) getInternalFrame()).getArtikelDto().getIId());

		phd = new PanelDynamischHelper(PanelFac.PANEL_ARTIKELTECHNIK, aDto.getArtgruIId(), getInternalFrame());

		if (DelegateFactory.getInstance().getPanelDelegate()
				.panelbeschreibungVorhanden(PanelFac.PANEL_ARTIKELTECHNIK)) {
			technikEigenschaften.getViewport().add(phd.panelErzeugen());

			jpaWorkingOn.add(technikEigenschaften, "growx, span 10");
		}

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DISCARD, ACTION_PREVIOUS, ACTION_NEXT, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void setDefaults() {

		// SP5756
		if (getInternalFrame().bRechtDarfPreiseSehenEinkauf && getInternalFrame().bRechtDarfPreiseSehenVerkauf) {

		} else {

			wnfZuschlagEK.setVisible(false);
			wlaZuschlagEK.setVisible(false);

			wnfZuschlagVK.setVisible(false);
			wlaZuschlagVK.setVisible(false);
			wlaAufschlag.setVisible(false);
			wnfAufschlagBetrag.setVisible(false);
			wnfAufschlagProzent.setVisible(false);
			wlaProzentAufschlag.setVisible(false);
			wlaAufschlagMandantenwhg.setVisible(false);
			wla17.setVisible(false);
			wla18.setVisible(false);
		}

	}

	public void wnfZuschlag_actionPerformed(ActionEvent e) {

	}

}
