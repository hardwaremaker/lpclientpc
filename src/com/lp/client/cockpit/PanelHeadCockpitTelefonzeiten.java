package com.lp.client.cockpit;

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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTelefonField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.IPartnerDtoService;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.Facade;
import com.lp.util.Helper;

/*
 * <p><I>Diese Klasse kuemmert sich um das Panel Partner.</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>20.10.04</I></p>
 *
 * @author $Author: christian $
 *
 * @version $Revision: 1.22 $
 *
 * Date $Date: 2012/11/09 08:01:49 $
 */
public class PanelHeadCockpitTelefonzeiten extends PanelBasis {

	private static final long serialVersionUID = 8590199699076800120L;

	protected JPanel jpaWorkingOn = null;

	protected JPanel jpaButtonAction = null;
	protected WrapperLabel wcoAnrede = null;
	protected Border border = null;
	protected SortedMap<?, ?> tmAnreden = null;
	protected WrapperLabel wla1 = null;

	protected GridBagLayout gblAussen = null;
	protected GridBagLayout gblPartner = null;
	protected WrapperTextField wtfAnsprechpartner = null;
	protected WrapperLabel wlaAnsprechpartner = null;
	protected WrapperTextField wtfName1 = null;

	private WrapperTelefonField wtfDurchwahl = null;
	private WrapperTelefonField wtfHandy = null;
	
	private IPartnerDtoService partnerDtoService;
	InternalFrameCockpit internalFrameCockpit = null;

	public PanelHeadCockpitTelefonzeiten(InternalFrameCockpit internalFrame, String add2TitleI, Object key,
			IPartnerDtoService partnerDtoService) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.partnerDtoService = partnerDtoService;

		this.internalFrameCockpit = internalFrame;

		jbInit();

		initPanel();
	}

	protected PartnerDto getPartnerDto() {
		return partnerDtoService.getServicePartnerDto();
	}

	protected void setPartnerDto(PartnerDto partnerDto) {
		partnerDtoService.setServicePartnerDto(partnerDto);
	}

	public Integer getPartnerIId() {
		if (partnerDtoService.getServicePartnerDto() == null)
			return null;
		return partnerDtoService.getServicePartnerDto().getIId();
	}

	/**
	 * Fuelle die Helpercontrols, jene aus denen ausgewaehlt werden kann.
	 * 
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	private void initPanel() throws Throwable {
		Locale clientLocUi = LPMain.getTheClient().getLocUi();
		

	}

	private void jbInit() throws Throwable {

		// Buttons
		resetToolsPanel();

		// das aussenpanel hat immer das gridbaglayout.
		gblAussen = new GridBagLayout();
		setLayout(gblAussen);

		jpaWorkingOn = new JPanel();

		wcoAnrede = new WrapperLabel();

		// flrlokal: damit der event via internalframe direkt hierherkommt,
		// nicht ueber tabbedpanes.
		getInternalFrame().addItemChangedListener(this);

		// Workingpanel
		gblPartner = new GridBagLayout();
		jpaWorkingOn.setLayout(gblPartner);

		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		wtfDurchwahl = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);
		wtfDurchwahl.setActivatable(false);
		wtfHandy = new WrapperTelefonField(PartnerFac.MAX_KOMMART_INHALT);
		wtfHandy.setActivatable(false);
		
		wla1 = new WrapperLabel();
		wla1.setText("");

		wtfAnsprechpartner = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);

		wtfName1 = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
		wtfName1.setMandatoryFieldDB(true);

		wtfName1.setDependenceField(true);

		wlaAnsprechpartner = new WrapperLabel(LPMain.getTextRespectUISPr("label.ansprechpartner"));

		jpaWorkingOn.add(wcoAnrede, new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));

		jpaWorkingOn.add(wtfName1, new GridBagConstraints(1, iZeile, 1, 1, 0.8, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfDurchwahl, new GridBagConstraints(2, iZeile, 1, 1, 0.3, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jpaWorkingOn.add(wlaAnsprechpartner, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfHandy, new GridBagConstraints(2, iZeile, 1, 1, 0.3, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);

		if (!bNeedNoYouAreSelectedI) {
			Object key = getPartnerDto().getIId();
			if (key == null) {
				leereAlleFelder(this);
				clearStatusbar();
			} else {
				setPartnerDto(
						DelegateFactory.getInstance().getPartnerDelegate().partnerFindByPrimaryKey((Integer) key));

				getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
						getPartnerDto().formatFixTitelName1Name2());
				setStatusbar();

			}
			initPanel();
			// updateComponents();
			dto2Components();
		}
	}

	protected void setDefaults() throws Throwable {

		if (getPartnerDto() == null) {
			setPartnerDto(new PartnerDto());
		}

		getPartnerDto().setBVersteckt(Helper.boolean2Short(false));

		getPartnerDto().setLocaleCNrKommunikation(LPMain.getTheClient().getLocMandantAsString());

	}

	protected void setStatusbar() throws Throwable {
		setStatusbarModification(getPartnerDto());
	}

	/**
	 * Fuelle alle Panelfelder.
	 * 
	 * @throws Throwable Ausnahme
	 */
	protected void dto2Components() throws Throwable {

		PartnerDto partnerDto = getPartnerDto();

		wcoAnrede.setText(partnerDto.getAnredeCNr());

		wtfName1.setText(partnerDto.formatFixTitelName1Name2());
		
		
		Integer ansprechpartnerIId=(Integer) internalFrameCockpit.getTpCockpit().getPanelQueryAnsprechpartner().getSelectedId();
		
		if(ansprechpartnerIId!=null) {
			AnsprechpartnerDto ansprechpartnerDto=DelegateFactory.getInstance().getAnsprechpartnerDelegate().ansprechpartnerFindByPrimaryKey(ansprechpartnerIId);
			PartnerDto pDto=DelegateFactory.getInstance().getPartnerDelegate().partnerFindByPrimaryKey(ansprechpartnerDto.getPartnerIIdAnsprechpartner());
			
			wtfAnsprechpartner.setText(pDto.formatFixName1Name2());
			
			
			if (ansprechpartnerDto.getCTelefon() != null) {

				wtfDurchwahl.setPartnerKommunikationDto(partnerDto, ansprechpartnerDto.getCTelefon());

				wtfDurchwahl.setTextDurchwahl(DelegateFactory.getInstance().getPartnerDelegate().enrichNumber(
						partnerDto.getIId(), PartnerFac.KOMMUNIKATIONSART_TELEFON, ansprechpartnerDto.getCTelefon(), false));

				if (Helper.short2boolean(ansprechpartnerDto.getBDurchwahl())) {
					wtfDurchwahl.setIstAnsprechpartner(true);
				} else {
					wtfDurchwahl.setIstAnsprechpartner(false);
					wtfDurchwahl.setPartnerKommunikationDto(partnerDto,
							ansprechpartnerDto.getCTelefon());
				}

			} else {
				wtfDurchwahl.setPartnerKommunikationDto(partnerDto,
						partnerDto.getCTelefon());
			}
			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatTitelAnrede());

			if (ansprechpartnerDto.getCHandy() != null) {
				wtfHandy.setPartnerKommunikationDto(ansprechpartnerDto.getPartnerDto(), ansprechpartnerDto.getCHandy());
				// wtfDurchwahl.setIstAnsprechpartner(true);
			} else {
				wtfHandy.setPartnerKommunikationDto(null, null);
			}
			
		}else {
			wtfAnsprechpartner.setText(null);
		}
		
		
		
	
		
		
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.ACTION_NEW) {

		} else if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PARTNER;
	}

	// protected void wcoItemStateChanged(ItemEvent e) {
	// updateComponents();
	// }

	

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfName1;
	}

}