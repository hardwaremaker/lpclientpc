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
package com.lp.client.personal;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.LinkedHashMap;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.KollektivDto;
import com.lp.server.personal.service.PersonalFac;

@SuppressWarnings("static-access")
public class PanelKollektiv extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFramePersonal internalFramePersonal = null;
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private KollektivDto kollektivDto = null;

	private WrapperLabel wlaAbrechnungsart = new WrapperLabel();
	private WrapperComboBox wcoAbrechnungsart = new WrapperComboBox();

	private WrapperLabel wlaBerechnungsbasis = new WrapperLabel();
	private WrapperRadioButton wrbBerechnungsbasisUhrzeit = new WrapperRadioButton();
	private WrapperRadioButton wrbBerechnungsbasisSollzeitZeitmodell = new WrapperRadioButton();
	private WrapperRadioButton wrbBerechnungsbasisFesteStunden = new WrapperRadioButton();
	private ButtonGroup bgBerechnungsbasis = new ButtonGroup();

	private WrapperLabel wlaNormalstunden = new WrapperLabel();
	private WrapperNumberField wnfNormalstunden = new WrapperNumberField();

	private WrapperLabel wlaFaktorUest50 = new WrapperLabel();
	private WrapperNumberField wnfFaktorUest50 = new WrapperNumberField();
	private WrapperLabel wlaFaktorUest100 = new WrapperLabel();
	private WrapperNumberField wnfFaktorUest100 = new WrapperNumberField();
	
	private WrapperLabel wlaFaktorPassiveReisezeit = new WrapperLabel();
	private WrapperNumberField wnfFaktorPassiveReisezeit = new WrapperNumberField();

	private WrapperLabel wlaFaktorUest200 = new WrapperLabel();
	private WrapperNumberField wnfFaktorUest200 = new WrapperNumberField();

	private WrapperLabel wla200Prozentab = new WrapperLabel();
	private WrapperNumberField wnf200Prozentab = new WrapperNumberField();

	private WrapperLabel wlaFaktorMehrstd = new WrapperLabel();
	private WrapperNumberField wnfFaktorMehrstd = new WrapperNumberField();

	private WrapperLabel wlaBlockzeitBis = new WrapperLabel();
	private WrapperTimeField wtfBlockzeitBis = new WrapperTimeField();
	private WrapperLabel wlaBlockzeitAb = new WrapperLabel();
	private WrapperTimeField wtfBlockzeitAb = new WrapperTimeField();

	private WrapperCheckBox wcoVerbraucheUestd = new WrapperCheckBox();
	private WrapperCheckBox wcoUestdErstAbErbrachtenSollstunden = new WrapperCheckBox();
	private WrapperCheckBox wcoUestdVerteilen = new WrapperCheckBox();

	private WrapperCheckBox wcoFeiertagsueberstundenAbSoll = new WrapperCheckBox();

	private WrapperCheckBox wcoWochengesamtsicht = new WrapperCheckBox();

	public PanelKollektiv(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFramePersonal) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		kollektivDto = new KollektivDto();
		internalFramePersonal.setKollektivDto(kollektivDto);

		leereAlleFelder(this);
	}

	public void setSichtbarkeitAufgrundAbrechnungsart() {

		wlaBerechnungsbasis.setVisible(true);
		wrbBerechnungsbasisFesteStunden.setVisible(true);
		wrbBerechnungsbasisSollzeitZeitmodell.setVisible(true);
		wrbBerechnungsbasisUhrzeit.setVisible(true);
		wcoUestdErstAbErbrachtenSollstunden.setVisible(true);
		wcoUestdVerteilen.setVisible(true);
		wcoVerbraucheUestd.setVisible(true);
		wlaNormalstunden.setVisible(true);
		wnfNormalstunden.setVisible(true);
		wcoWochengesamtsicht.setVisible(true);
		wlaBlockzeitAb.setVisible(true);
		wlaBlockzeitBis.setVisible(true);
		wtfBlockzeitAb.setVisible(true);
		wtfBlockzeitBis.setVisible(true);
		wlaFaktorMehrstd.setVisible(true);
		wnfFaktorMehrstd.setVisible(true);
		wlaFaktorUest200.setVisible(true);
		wnfFaktorUest200.setVisible(true);
		wla200Prozentab.setVisible(true);
		wcoFeiertagsueberstundenAbSoll.setVisible(true);
		wnf200Prozentab.setVisible(true);

		if (wcoAbrechnungsart.getKeyOfSelectedItem()
				.equals(PersonalFac.KOLLEKTIV_ABRECHNUNGSART_BETRIEBSVEREINBARUNG_A)) {
			wlaBerechnungsbasis.setVisible(false);
			wrbBerechnungsbasisFesteStunden.setVisible(false);
			wrbBerechnungsbasisSollzeitZeitmodell.setVisible(false);
			wrbBerechnungsbasisUhrzeit.setVisible(false);
			wcoUestdErstAbErbrachtenSollstunden.setVisible(false);
			wcoUestdVerteilen.setVisible(false);
			wcoVerbraucheUestd.setVisible(false);
			wlaNormalstunden.setVisible(false);
			wnfNormalstunden.setVisible(false);
			wcoWochengesamtsicht.setVisible(false);
			wlaBlockzeitAb.setVisible(false);
			wlaBlockzeitBis.setVisible(false);
			wtfBlockzeitAb.setVisible(false);
			wtfBlockzeitBis.setVisible(false);
			wlaFaktorMehrstd.setVisible(false);
			wnfFaktorMehrstd.setVisible(false);
			wlaFaktorUest200.setVisible(false);
			wnfFaktorUest200.setVisible(false);
			wla200Prozentab.setVisible(false);
			wnf200Prozentab.setVisible(false);
			wcoFeiertagsueberstundenAbSoll.setVisible(false);
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getSource().equals(wcoAbrechnungsart)) {

			setSichtbarkeitAufgrundAbrechnungsart();

		}

	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getPersonalDelegate().removeKollektiv(kollektivDto.getIId());
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {
		kollektivDto.setCBez(wtfBezeichnung.getText());
		kollektivDto.setBVerbraucheuestd(wcoVerbraucheUestd.getShort());
		kollektivDto.setbWochengesamtsicht(wcoWochengesamtsicht.getShort());
		kollektivDto.setNNormalstunden(wnfNormalstunden.getBigDecimal());
		kollektivDto.setUBlockzeitab(wtfBlockzeitAb.getTime());
		kollektivDto.setUBlockzeitbis(wtfBlockzeitBis.getTime());
		kollektivDto.setNFaktoruestd100(wnfFaktorUest100.getBigDecimal());
		kollektivDto.setNFaktoruestd50(wnfFaktorUest50.getBigDecimal());
		kollektivDto.setNFaktormehrstd(wnfFaktorMehrstd.getBigDecimal());
		kollektivDto.setBUestdabsollstderbracht(wcoUestdErstAbErbrachtenSollstunden.getShort());
		kollektivDto.setBUestdverteilen(wcoUestdVerteilen.getShort());
		kollektivDto.setNFaktoruestd200(wnfFaktorUest200.getBigDecimal());
		kollektivDto.setN200prozentigeab(wnf200Prozentab.getBigDecimal());

		kollektivDto.setCAbrechungsart((String) wcoAbrechnungsart.getKeyOfSelectedItem());

		kollektivDto.setBFeiertagsueberstundenAbSoll(wcoFeiertagsueberstundenAbSoll.getShort());

		int i = PersonalFac.KOLLEKTIV_BERECHNUNGSBASIS_UHRZEIT;

		if (wrbBerechnungsbasisSollzeitZeitmodell.isSelected()) {
			i = PersonalFac.KOLLEKTIV_BERECHNUNGSBASIS_SOLLZEIT_ZEITMODEL;
		} else if (wrbBerechnungsbasisFesteStunden.isSelected()) {
			i = PersonalFac.KOLLEKTIV_BERECHNUNGSBASIS_FESTE_STUNDEN;
		}

		kollektivDto.setIBerechnungsbasis(i);
		
		kollektivDto.setIFaktorPassiveReisezeit(wnfFaktorPassiveReisezeit.getInteger());

	}

	protected void dto2Components() throws ExceptionLP {
		wtfBezeichnung.setText(kollektivDto.getCBez());
		wcoVerbraucheUestd.setShort(kollektivDto.getBVerbraucheuestd());
		wnfNormalstunden.setBigDecimal(kollektivDto.getNNormalstunden());
		wnfFaktorUest100.setBigDecimal(kollektivDto.getNFaktoruestd100());
		wnfFaktorUest50.setBigDecimal(kollektivDto.getNFaktoruestd50());
		wnfFaktorMehrstd.setBigDecimal(kollektivDto.getNFaktormehrstd());
		wtfBlockzeitAb.setTime(kollektivDto.getUBlockzeitab());
		wtfBlockzeitBis.setTime(kollektivDto.getUBlockzeitbis());
		wcoUestdErstAbErbrachtenSollstunden.setShort(kollektivDto.getBUestdabsollstderbracht());
		wcoUestdVerteilen.setShort(kollektivDto.getBUestdverteilen());
		wcoWochengesamtsicht.setShort(kollektivDto.getbWochengesamtsicht());
		wnfFaktorUest200.setBigDecimal(kollektivDto.getNFaktoruestd200());
		wnf200Prozentab.setBigDecimal(kollektivDto.getN200prozentigeab());

		wcoAbrechnungsart.setKeyOfSelectedItem(kollektivDto.getCAbrechungsart());

		wcoFeiertagsueberstundenAbSoll.setShort(kollektivDto.getBFeiertagsueberstundenAbSoll());

		if (kollektivDto.getIBerechnungsbasis() == PersonalFac.KOLLEKTIV_BERECHNUNGSBASIS_UHRZEIT) {
			wrbBerechnungsbasisUhrzeit.setSelected(true);
		} else if (kollektivDto.getIBerechnungsbasis() == PersonalFac.KOLLEKTIV_BERECHNUNGSBASIS_SOLLZEIT_ZEITMODEL) {
			wrbBerechnungsbasisSollzeitZeitmodell.setSelected(true);
		} else if (kollektivDto.getIBerechnungsbasis() == PersonalFac.KOLLEKTIV_BERECHNUNGSBASIS_FESTE_STUNDEN) {
			wrbBerechnungsbasisFesteStunden.setSelected(true);
		}
		wnfFaktorPassiveReisezeit.setInteger(kollektivDto.getIFaktorPassiveReisezeit());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			if (wtfBlockzeitAb.getTime().equals(wtfBlockzeitBis.getTime())
					|| wtfBlockzeitBis.getTime().before(wtfBlockzeitAb.getTime())) {

				components2Dto();
				if (kollektivDto.getIId() == null) {
					kollektivDto.setCBez(wtfBezeichnung.getText());
					kollektivDto
							.setIId(DelegateFactory.getInstance().getPersonalDelegate().createKollektiv(kollektivDto));
					setKeyWhenDetailPanel(kollektivDto.getIId());

				} else {
					DelegateFactory.getInstance().getPersonalDelegate().updateKollektiv(kollektivDto);

				}
				super.eventActionSave(e, true);
				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(kollektivDto.getIId() + "");
				}
				internalFramePersonal.setKollektivDto(kollektivDto);

				eventYouAreSelected(false);

			} else {
				DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
						LPMain.getInstance().getTextRespectUISPr("lp.error.beginnvorende"));
			}
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		eI = (ItemChangedEvent) eI;
	}

	private void jbInit() throws Throwable {
		// von hier ...
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));
		wlaAbrechnungsart.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.abrechnungsart"));

		LinkedHashMap<String, String> m = new LinkedHashMap<String, String>();
		m.put(PersonalFac.KOLLEKTIV_ABRECHNUNGSART_STANDARD, PersonalFac.KOLLEKTIV_ABRECHNUNGSART_STANDARD);
		m.put(PersonalFac.KOLLEKTIV_ABRECHNUNGSART_BETRIEBSVEREINBARUNG_A,
				PersonalFac.KOLLEKTIV_ABRECHNUNGSART_BETRIEBSVEREINBARUNG_A);
		wcoAbrechnungsart.setMandatoryField(true);
		wcoAbrechnungsart.setMap(m);
		wcoAbrechnungsart.addActionListener(this);

		wtfBezeichnung.setColumnsMax(PersonalFac.MAX_KOLLEKTIV_BEZEICHNUNG);
		wtfBezeichnung.setText("");
		wtfBezeichnung.setMandatoryField(true);

		wcoVerbraucheUestd.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.verbraucheuestd"));
		wcoUestdErstAbErbrachtenSollstunden
				.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.uestderstaberbrachtensollstd"));
		wcoUestdVerteilen.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.uestdverteilen"));

		wcoWochengesamtsicht.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.wochengesamtsicht"));

		wlaNormalstunden.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.normalstunden"));

		wlaFaktorUest200.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.faktoruest200"));
		wla200Prozentab.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.faktoruest200.abstunden"));

		wcoFeiertagsueberstundenAbSoll
				.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.feiertgasueberstundensbsoll"));

		wlaFaktorUest100.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.faktoruest100"));
		wlaFaktorUest50.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.faktoruest50"));
		wlaFaktorMehrstd.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.faktormehrstd"));
		wlaBlockzeitBis.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.blockzeitbis"));
		wlaBlockzeitAb.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.blockzeitab"));

		wlaFaktorPassiveReisezeit.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.faktor.passivereisezeit"));
		wnfFaktorPassiveReisezeit.setMandatoryField(true);
		wnfFaktorPassiveReisezeit.setFractionDigits(0);
		
		wlaBerechnungsbasis.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.berechnungsbasis"));
		wrbBerechnungsbasisUhrzeit
				.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.berechnungsbasis.uhrzeit"));
		wrbBerechnungsbasisSollzeitZeitmodell.setText(
				LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.berechnungsbasis.sollzeitzeitmodell"));
		wrbBerechnungsbasisFesteStunden
				.setText(LPMain.getInstance().getTextRespectUISPr("pers.kollektiv.berechnungsbasis.festestunden"));
		bgBerechnungsbasis = new ButtonGroup();

		bgBerechnungsbasis.add(wrbBerechnungsbasisUhrzeit);
		bgBerechnungsbasis.add(wrbBerechnungsbasisSollzeitZeitmodell);
		bgBerechnungsbasis.add(wrbBerechnungsbasisFesteStunden);
		wrbBerechnungsbasisUhrzeit.setSelected(true);

		wnfFaktorUest100.setMandatoryField(true);
		wnfFaktorUest50.setMandatoryField(true);
		wnfFaktorMehrstd.setMandatoryField(true);

		wnfFaktorUest200.setMandatoryField(true);
		wnf200Prozentab.setMandatoryField(true);

		HelperClient.setMinimumAndPreferredSize(wtfBlockzeitBis, HelperClient.getSizeFactoredDimension(80));
		HelperClient.setMinimumAndPreferredSize(wtfBlockzeitAb, HelperClient.getSizeFactoredDimension(80));

		wnfNormalstunden.setMinimumValue(0);
		getInternalFrame().addItemChangedListener(this);
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1, 1, 0.6, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaNormalstunden, new GridBagConstraints(2, iZeile, 1, 1, 0.6, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfNormalstunden, new GridBagConstraints(3, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), -46, 0));
		iZeile++;
		jpaWorkingOn.add(wlaAbrechnungsart, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoAbrechnungsart, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBerechnungsbasis, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbBerechnungsbasisUhrzeit, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcoWochengesamtsicht, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wrbBerechnungsbasisSollzeitZeitmodell, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbBerechnungsbasisFesteStunden, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcoVerbraucheUestd, new GridBagConstraints(0, iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcoUestdErstAbErbrachtenSollstunden, new GridBagConstraints(0, iZeile, 2, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcoUestdVerteilen, new GridBagConstraints(0, iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaFaktorUest200, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 46, 0));
		jpaWorkingOn.add(wnfFaktorUest200, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wla200Prozentab, new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnf200Prozentab, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), -46, 0));
		iZeile++;
		jpaWorkingOn.add(wlaFaktorUest100, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfFaktorUest100, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaFaktorUest50, new GridBagConstraints(2, iZeile, 1, 1, 0.6, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfFaktorUest50, new GridBagConstraints(3, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), -46, 0));
		iZeile++;
		jpaWorkingOn.add(wlaFaktorMehrstd, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfFaktorMehrstd, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcoFeiertagsueberstundenAbSoll, new GridBagConstraints(2, iZeile, 2, 1, 0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaFaktorPassiveReisezeit, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfFaktorPassiveReisezeit, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBlockzeitBis, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBlockzeitBis, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBlockzeitAb, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBlockzeitAb, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_KOLLEKTIV;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);
		Object key = internalFramePersonal.getKollektivDto().getIId();

		if (key != null) {
			kollektivDto = DelegateFactory.getInstance().getPersonalDelegate()
					.kollektivFindByPrimaryKey(internalFramePersonal.getKollektivDto().getIId());

			dto2Components();

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					internalFramePersonal.getKollektivDto().getCBez());

		} else {
			leereAlleFelder(this);
			wnfFaktorUest200.setInteger(2);
			wnf200Prozentab.setInteger(24);
			wnfFaktorUest100.setInteger(1);
			wnfFaktorUest50.setDouble(0.5);
			wnfFaktorMehrstd.setInteger(0);
			wrbBerechnungsbasisUhrzeit.setSelected(true);
			wnfFaktorPassiveReisezeit.setInteger(0);

		}
		internalFramePersonal.getTabbedPaneKollektiv().disableUest100();
		internalFramePersonal.getTabbedPaneKollektiv().disableBetriebsvereinbarung();

	}

}
