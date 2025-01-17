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
package com.lp.client.instandhaltung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.instandhaltung.service.StandorttechnikerDto;

public class PanelStandorttechniker extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InternalFrameInstandhaltung internalFrameInstandhaltung = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private StandorttechnikerDto standorttechnikerDto = null;

	private WrapperSelectField wsfPersonal_Techniker = new WrapperSelectField(
			WrapperSelectField.PERSONAL, getInternalFrame(), false);

	private WrapperCheckBox wcbVerantwortlicher = new WrapperCheckBox();

	public InternalFrameInstandhaltung getInternalFrameInstandhaltung() {
		return internalFrameInstandhaltung;
	}

	public PanelStandorttechniker(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameInstandhaltung = (InternalFrameInstandhaltung) internalFrame;

		jbInit();
		setDefaults();
		initComponents();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		if (key == null || key.equals(LPMain.getLockMeForNew())) {

			clearStatusbar();

		} else {
			standorttechnikerDto = DelegateFactory.getInstance()
					.getInstandhaltungDelegate()
					.standorttechnikerFindByPrimaryKey((Integer) key);

			dto2Components();

		}

	}

	protected void dto2Components() throws Throwable {

		wsfPersonal_Techniker.setKey(standorttechnikerDto
				.getPersonalIId());
		wcbVerantwortlicher.setShort(standorttechnikerDto.getBVerantwortlicher());
	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(10, 10, 0, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);

	
		wcbVerantwortlicher.setText(LPMain
				.getTextRespectUISPr("is.standorttechniker.verantwortlich"));
	
		wsfPersonal_Techniker.setMandatoryField(true);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wsfPersonal_Techniker, new GridBagConstraints(0,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfPersonal_Techniker.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.3, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		

		iZeile++;
		jpaWorkingOn.add(wcbVerantwortlicher, new GridBagConstraints(1, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);

		standorttechnikerDto = new StandorttechnikerDto();

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_GERAETETHISTORIE;
	}

	protected void setDefaults() throws Throwable {

	}

	protected void components2Dto() throws Throwable {
		standorttechnikerDto.setStandortIId(internalFrameInstandhaltung
				.getTabbedPaneInstandhaltung().getStandortDto().getIId());
		standorttechnikerDto.setPersonalIId(wsfPersonal_Techniker
				.getIKey());
		standorttechnikerDto.setBVerantwortlicher(wcbVerantwortlicher.getShort());

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getInstandhaltungDelegate()
				.removeStandorttechniker(standorttechnikerDto);
		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			if (standorttechnikerDto.getIId() == null) {
				standorttechnikerDto.setIId(DelegateFactory.getInstance()
								.getInstandhaltungDelegate()
								.createStandorttechniker(standorttechnikerDto));
				setKeyWhenDetailPanel(standorttechnikerDto.getIId());

			} else {

				DelegateFactory.getInstance().getInstandhaltungDelegate()
						.updateStandorttechniker(standorttechnikerDto);

			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						standorttechnikerDto.getIId().toString());
			}
			eventYouAreSelected(false);

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

}
