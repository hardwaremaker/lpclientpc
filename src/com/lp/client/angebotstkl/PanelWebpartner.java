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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.IWebpartnerDto;

public abstract class PanelWebpartner extends PanelBasis {

	private static final long serialVersionUID = 3499948152289495645L;
	
	private GridBagLayout gridBagLayoutAll;
	private GridBagLayout gridBagLayoutWorkingPanel;
	private Border border;
	private JPanel jpaWorkingOn;
	private JPanel jpaButtonAction;
	private WrapperSelectField wsfLieferant;
	
	private IWebpartnerDto webpartnerDto = null;

	public PanelWebpartner(InternalFrame internalFrameI, String addTitleI,
			Object keyWhenDetailPanelI) throws Throwable {
		super(internalFrameI, addTitleI, keyWhenDetailPanelI);
		
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	public IWebpartnerDto getWebpartnerDto() {
		return webpartnerDto;
	}

	public void setWebpartnerDto(IWebpartnerDto webpartnerDto) {
		this.webpartnerDto = webpartnerDto;
	}

	abstract protected IWebpartnerDto getNewWebpartnerDto();
	
	private void setDefaults() {
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
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, 
				new Insets(0,	0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		int zeile = 0;
		Component[][] components = getDetailComponents();
		if (components != null) {
			for (int i = 0; i < components.length; i++) {
				jpaWorkingOn.add(components[i][0],
						new GridBagConstraints(0, zeile, 1, 1, 0.1, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
								0, 0));
				jpaWorkingOn.add(components[i][1],
						new GridBagConstraints(1, zeile, 1, 1, 0.3, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
								0, 0));
				zeile++;
			}
		}
		wsfLieferant = new WrapperSelectField(WrapperSelectField.LIEFERANT, getInternalFrame(), false);
		jpaWorkingOn.add(wsfLieferant.getWrapperGotoButton(),
				new GridBagConstraints(0, zeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfLieferant.getWrapperTextField(),
				new GridBagConstraints(1, zeile, 1, 1, 0.3, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DISCARD };

		enableToolsPanelButtons(aWhichButtonIUse);
	}
	
	abstract protected Component[][] getDetailComponents();

	protected String getLockMeWer() throws Exception {
		return getLockMeWerImpl();
	}

	abstract protected String getLockMeWerImpl();

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wsfLieferant.getWrapperGotoButton();
	}

	protected void dto2Components() throws Throwable {
		wsfLieferant.setKey(webpartnerDto.getLieferantIId());
	}
	
	protected void components2Dto() throws Throwable {
		webpartnerDto.setLieferantIId(wsfLieferant.getIKey());
	}
	
	@Override
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getAngebotstklDelegate().removeWebpartner(webpartnerDto.getIId());
		this.setKeyWhenDetailPanel(null);
		
		super.eventActionDelete(e, false, false);
	}

	@Override
	public void eventActionNew(EventObject eventObject,
			boolean bAdministrateLockKeyI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		webpartnerDto = getNewWebpartnerDto();
		leereAlleFelder(this);
	}

	@Override
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (webpartnerDto.getIId() == null) {
				webpartnerDto.setIId(DelegateFactory.getInstance()
						.getAngebotstklDelegate().createWebpartner(webpartnerDto));
				setKeyWhenDetailPanel(webpartnerDto.getIId());
			} else {
				DelegateFactory.getInstance().getAngebotstklDelegate().updateWebpartner(webpartnerDto);
			}
			super.eventActionSave(e, true);
			
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						webpartnerDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
		
	}

	@Override
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		
		Object key = getKeyWhenDetailPanel();
		
		if (key == null || key.equals(LPMain.getLockMeForNew())) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			webpartnerDto = DelegateFactory.getInstance()
					.getAngebotstklDelegate().webpartnerFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

}
