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
package com.lp.client.system;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.automatik.IPanelAutoJobDetails;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.AutomatikjobDto;

public class PanelAutomatikDetails extends PanelBasis {
	private static final long serialVersionUID = 1L;

	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	TabbedPaneAutomatik tpAutomatik = null;
	private AutomatikjobDto automatikjobDto = null;
	
	private IPanelAutoJobDetails autoJobHeadPanel;
	private IPanelAutoJobDetails autoJobDetailsCtrl;
	
	public final static String MY_OWN_NEW_START = PanelBasis.ACTION_MY_OWN_NEW + "MY_OWN_NEW_START";
	
	public PanelAutomatikDetails(InternalFrame internalFrameI, String addTitleI)
			throws Throwable {
		super(internalFrameI, addTitleI);
	}

	public PanelAutomatikDetails(InternalFrame internalFrameI,
			String addTitleI, Object keyWhenDetailPanelI,
			TabbedPaneAutomatik tabbedPaneAutomatik,
			IPanelAutoJobDetails autoJobHeadPanel,
			IPanelAutoJobDetails autoJobDetailsCtrl)
			throws Throwable {
		super(internalFrameI, addTitleI, keyWhenDetailPanelI);
		this.tpAutomatik = tabbedPaneAutomatik;
		this.autoJobHeadPanel = autoJobHeadPanel;
		this.autoJobDetailsCtrl = autoJobDetailsCtrl;
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		panelButtonAction = getToolsPanel();
		this.setActionMap(null);

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 6, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile = autoJobHeadPanel.installComponents(jPanelWorkingOn, iZeile);
		iZeile = autoJobDetailsCtrl.installComponents(jPanelWorkingOn, iZeile);
		
		String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		///PJ22585
		//getToolBar().addButtonCenter("/com/lp/client/res/gears_run.png",
		//		LPMain.getTextRespectUISPr("lp.automatik.sofort.ausfuehren"), MY_OWN_NEW_START, null,
		//		null);
		
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_AUTOMATIK;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || key.equals(LPMain.getLockMeForNew())) {
			dto2Components();
			if (automatikjobDto != null) {
				automatikjobDto.setIId(null);
			}
//			wtfName.setText("");
//			wtfIntervall.setText("");
		} else {
			automatikjobDto = DelegateFactory.getInstance()
					.getAutomatikDelegate().automatikjobFindByPrimaryKey(
							(Integer) key);
			
			dto2Components();
		}
	}

	protected void dto2Components() throws Throwable {
		autoJobHeadPanel.loadDetails();
		autoJobDetailsCtrl.loadDetails();
	}

	protected void components2Dto() throws ExceptionLP {

	}
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MY_OWN_NEW_START)) {
			//autoJobHeadPanel.getJobType();
		}
	}
	
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		components2Dto();
		autoJobHeadPanel.saveDetails();
		autoJobDetailsCtrl.saveDetails();

		super.eventActionSave(e, true);
		eventYouAreSelected(false);
	}

	public PanelAutomatikDetails() {
		super();
	}

}
