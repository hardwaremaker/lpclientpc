
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
package com.lp.client.angebotstkl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.EkaglieferantDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.util.Facade;


public class PanelEkaglieferant extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameAngebotstkl internalFramePersonal = null;

	private WrapperTextField wtfAnsprechpartner = null;
	private WrapperButton wbuAnsprechpartner = null;
	private PanelQueryFLR panelQueryFLRAnsprechpartner = null;

	public final static String ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT = "action_special_ansprechpartnerlieferant";

	private EkaglieferantDto ekaglieferantDto = null;

	public WrapperSelectField wsfLieferant = new WrapperSelectField(WrapperSelectField.LIEFERANT, getInternalFrame(),
			true);

	private WrapperLabel wlaImport = new WrapperLabel();
	private WrapperTimestampField wdfImport = new WrapperTimestampField();
	
	private WrapperLabel wlaVersand = new WrapperLabel();
	private WrapperTimestampField wdfVersand = new WrapperTimestampField();

	private WrapperLabel wlaAngebotsnummer = new WrapperLabel();
	private WrapperTextField wtfAngebotsnummer = new WrapperTextField();

	private WrapperLabel wlaWaehrung = new WrapperLabel();
	private WrapperComboBox wcoWaehrung = new WrapperComboBox();

	private WrapperLabel wlaAufschlag = new WrapperLabel();
	private WrapperNumberField wnfAufschlag=new WrapperNumberField();
	
	public PanelEkaglieferant(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFrameAngebotstkl) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() throws Throwable {
		
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wsfLieferant;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		ekaglieferantDto = new EkaglieferantDto();
		leereAlleFelder(this);
		
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (wsfLieferant.getIKey() != null) {
			dialogQueryAnsprechpartner();
		} else {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("er.error.zuerst_lieferant_waehlen"));
		}
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getAngebotstklDelegate().removeEkaglieferant(ekaglieferantDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == wsfLieferant.getPanelQueryFLR()) {
				Integer lieferantIId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(lieferantIId);

				wcoWaehrung.setKeyOfSelectedItem(lfDto.getWaehrungCNr());

			} else if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);
				wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatFixTitelName1Name2());
				ekaglieferantDto.setAnsprechpartnerIId(iIdAnsprechpartner);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				ekaglieferantDto.setAnsprechpartnerIId(null);
				wtfAnsprechpartner.setText("");

			}
		}
	}

	protected void components2Dto() throws ExceptionLP {
		ekaglieferantDto.setEinkaufsangebotIId(internalFramePersonal.getEinkaufsangebotDto().getIId());
		ekaglieferantDto.setLieferantIId(wsfLieferant.getIKey());
		ekaglieferantDto.setWaehrungCNr((String) wcoWaehrung.getKeyOfSelectedItem());
		ekaglieferantDto.setCAngebotsnummer(wtfAngebotsnummer.getText());
		ekaglieferantDto.setTImport(wdfImport.getTimestamp());
		ekaglieferantDto.setTVersand(wdfVersand.getTimestamp());
		ekaglieferantDto.setNAufschlag(wnfAufschlag.getBigDecimal());

	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		wsfLieferant.setKey(ekaglieferantDto.getLieferantIId());
		wtfAngebotsnummer.setText(ekaglieferantDto.getCAngebotsnummer());
		wdfImport.setTimestamp(ekaglieferantDto.getTImport());
		wdfVersand.setTimestamp(ekaglieferantDto.getTVersand());
		wcoWaehrung.setKeyOfSelectedItem(ekaglieferantDto.getWaehrungCNr());
		wnfAufschlag.setBigDecimal(ekaglieferantDto.getNAufschlag());
		if (ekaglieferantDto.getAnsprechpartnerIId() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(ekaglieferantDto.getAnsprechpartnerIId());
			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatFixTitelName1Name2());
		} else {
			wtfAnsprechpartner.setText(null);
		}

		
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();
			if (ekaglieferantDto.getIId() == null) {
				ekaglieferantDto.setIId(
						DelegateFactory.getInstance().getAngebotstklDelegate().createEkaglieferant(ekaglieferantDto));
				setKeyWhenDetailPanel(ekaglieferantDto.getIId());
			} else {
				DelegateFactory.getInstance().getAngebotstklDelegate().updateEkaglieferant(ekaglieferantDto);
			}

			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(internalFramePersonal.getEkgruppeDto().getIId() + "");
			}
			eventYouAreSelected(false);

		}

	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaAngebotsnummer.setText(LPMain.getTextRespectUISPr("anf.angebotnummer"));

		wlaImport.setText(LPMain.getTextRespectUISPr("agstkl.ekaglieferant.importzeitpunkt"));
		wlaVersand.setText(LPMain.getTextRespectUISPr("agstkl.ekaglieferant.versandzeitpunkt"));

		wlaWaehrung.setText(LPMain.getTextRespectUISPr("lp.waehrung"));
		
		wlaAufschlag.setText(LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.ekag.aufschlag"));
		wnfAufschlag.setMandatoryField(true);
		
		
		getInternalFrame().addItemChangedListener(this);
		wcoWaehrung.setMandatoryField(true);
		wcoWaehrung.setMap(DelegateFactory.getInstance().getLocaleDelegate().getAllWaehrungen());

		wbuAnsprechpartner = new WrapperButton();
		wbuAnsprechpartner.setText(LPMain.getTextRespectUISPr("button.ansprechpartner"));

		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner = new WrapperTextField();
		wtfAnsprechpartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfAnsprechpartner.setActivatable(false);
		wbuAnsprechpartner.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT);
		wsfLieferant.setMandatoryField(true);

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
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wsfLieferant.getWrapperGotoButton(), new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wsfLieferant.getWrapperTextField(), new GridBagConstraints(2, iZeile, 3, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		iZeile++;
		jpaWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(2, iZeile, 3, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		iZeile++;
		jpaWorkingOn.add(wlaWaehrung, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoWaehrung, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wlaAufschlag, new GridBagConstraints(3, iZeile, 1, 1, 2.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 150, 0));
		
		jpaWorkingOn.add(wnfAufschlag, new GridBagConstraints(4, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++;

		jpaWorkingOn.add(wlaVersand, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wdfVersand, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 200, 0));
		
		iZeile++;
		jpaWorkingOn.add(wlaAngebotsnummer, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfAngebotsnummer, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		iZeile++;

		jpaWorkingOn.add(wlaImport, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wdfImport, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 200, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	private void dialogQueryAnsprechpartner() throws Throwable {
		Integer ansprechpartnerIId = ekaglieferantDto.getAnsprechpartnerIId();

		LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
				.lieferantFindByPrimaryKey(wsfLieferant.getIKey());

		panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance().createPanelFLRAnsprechpartner(
				getInternalFrame(), lfDto.getPartnerIId(), ansprechpartnerIId, true, true);
		new DialogQuery(panelQueryFLRAnsprechpartner);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_EINKAUFSANGEBOT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
			wnfAufschlag.setBigDecimal(BigDecimal.ZERO);
		} else {
			ekaglieferantDto = DelegateFactory.getInstance().getAngebotstklDelegate()
					.ekaglieferantFindByPrimaryKey((Integer) key);
			dto2Components();

		}

	}
}
