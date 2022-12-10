
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

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.lp.client.frame.*;
import com.lp.client.frame.component.*;
import com.lp.client.pc.*;
import com.lp.server.angebotstkl.service.EkgruppelieferantDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.service.*;
import com.lp.server.util.Facade;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.util.Helper;
import com.lp.client.frame.dialog.*;
import com.lp.client.partner.PartnerFilterFactory;

public class PanelEkgruppelieferant extends PanelBasis {

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

	private EkgruppelieferantDto ekgruppelieferantDto = null;

	public WrapperSelectField wsfLieferant = new WrapperSelectField(WrapperSelectField.LIEFERANT, getInternalFrame(),
			true);

	public PanelEkgruppelieferant(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
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
		ekgruppelieferantDto = new EkgruppelieferantDto();
		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT)) {
			if (wsfLieferant.getIKey() != null) {
				dialogQueryAnsprechpartner();
			} else {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("er.error.zuerst_lieferant_waehlen"));
			}
		}
	}

	private void dialogQueryAnsprechpartner() throws Throwable {
		Integer ansprechpartnerIId = ekgruppelieferantDto.getAnsprechpartnerIId();

		LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
				.lieferantFindByPrimaryKey(wsfLieferant.getIKey());

		panelQueryFLRAnsprechpartner = PartnerFilterFactory.getInstance().createPanelFLRAnsprechpartner(
				getInternalFrame(), lfDto.getPartnerIId(), ansprechpartnerIId, true, true);
		new DialogQuery(panelQueryFLRAnsprechpartner);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				Integer iIdAnsprechpartner = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartner);
				wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatFixTitelName1Name2());
				ekgruppelieferantDto.setAnsprechpartnerIId(iIdAnsprechpartner);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAnsprechpartner) {
				ekgruppelieferantDto.setAnsprechpartnerIId(null);
				wtfAnsprechpartner.setText("");

			}
		}
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getAngebotstklDelegate().removeEkgruppelieferant(ekgruppelieferantDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {
		ekgruppelieferantDto.setEkgruppeIId(internalFramePersonal.getEkgruppeDto().getIId());
		ekgruppelieferantDto.setLieferantIId(wsfLieferant.getIKey());

	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		wsfLieferant.setKey(ekgruppelieferantDto.getLieferantIId());
		if (ekgruppelieferantDto.getAnsprechpartnerIId() != null) {
			AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
					.ansprechpartnerFindByPrimaryKey(ekgruppelieferantDto.getAnsprechpartnerIId());
			wtfAnsprechpartner.setText(ansprechpartnerDto.getPartnerDto().formatFixTitelName1Name2());
		} else {
			wtfAnsprechpartner.setText(null);
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();
			if (ekgruppelieferantDto.getIId() == null) {
				ekgruppelieferantDto.setIId(DelegateFactory.getInstance().getAngebotstklDelegate()
						.createEkgruppelieferant(ekgruppelieferantDto));
				setKeyWhenDetailPanel(ekgruppelieferantDto.getIId());
			} else {
				DelegateFactory.getInstance().getAngebotstklDelegate().updateEkgruppelieferant(ekgruppelieferantDto);
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

		getInternalFrame().addItemChangedListener(this);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		wbuAnsprechpartner = new WrapperButton();
		wbuAnsprechpartner.setText(LPMain
				.getTextRespectUISPr("button.ansprechpartner"));

		wbuAnsprechpartner.addActionListener(this);

		wtfAnsprechpartner = new WrapperTextField();
		wtfAnsprechpartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfAnsprechpartner.setActivatable(false);
		wbuAnsprechpartner
				.setActionCommand(ACTION_SPECIAL_ANSPRECHPARTNER_LIEFERANT);
		
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
		jpaWorkingOn.add(wsfLieferant.getWrapperButton(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 0), 100, 0));
		jpaWorkingOn.add(wsfLieferant.getWrapperTextField(), new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 0), 100, 0));
		
		jpaWorkingOn.add(wbuAnsprechpartner, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 0), 0, 0));
		jpaWorkingOn.add(wtfAnsprechpartner, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 0), 0, 0));
		

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_EKGRUPPE;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();

		} else {
			ekgruppelieferantDto = DelegateFactory.getInstance().getAngebotstklDelegate()
					.ekgruppelieferantFindByPrimaryKey((Integer) key);
			dto2Components();

		}

	}
}
