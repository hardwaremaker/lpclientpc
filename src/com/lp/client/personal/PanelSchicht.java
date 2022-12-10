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

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.SchichtDto;

@SuppressWarnings("static-access")
public class PanelSchicht extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InternalFramePersonal internalFramePersonal = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();

	private WrapperCheckBox wcbPausenAbziehen = new WrapperCheckBox();
	private WrapperCheckBox wcbBegrenztAufTagessoll = new WrapperCheckBox();

	public InternalFramePersonal getInternalFramePersonal() {
		return internalFramePersonal;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}

	public PanelSchicht(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFramePersonal) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		// leereAlleFelder(this);
		if (!getKeyWhenDetailPanel().equals(LPMain.getLockMeForNew())) {

			dto2Components();

			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFramePersonal().getTabbedPaneSchicht()
							.getSchichtDto().getCBez());
		}
	}

	protected void dto2Components() throws Throwable {
		wtfBezeichnung.setText(getInternalFramePersonal()
				.getTabbedPaneSchicht().getSchichtDto().getCBez());
		wcbPausenAbziehen.setShort(getInternalFramePersonal()
				.getTabbedPaneSchicht().getSchichtDto().getBPausenabziehen());
		wcbBegrenztAufTagessoll.setShort(getInternalFramePersonal()
				.getTabbedPaneSchicht().getSchichtDto().getBBegrenztAufTagessoll());

	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
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

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));

		wcbPausenAbziehen.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.schichtzeit.pausenabziehen"));
		wcbBegrenztAufTagessoll.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.schicht.begrenzauftagessoll"));

		wtfBezeichnung.setText("");

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, 1, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, 1, 3, 1,
				0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbPausenAbziehen, new GridBagConstraints(1, 2, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbBegrenztAufTagessoll, new GridBagConstraints(1, 3, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		this.add(getPanelStatusbar(), new GridBagConstraints(0, 3, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		getInternalFramePersonal().getTabbedPaneSchicht().setSchichtDto(
				new SchichtDto());

		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_SCHICHT;
	}

	protected void setDefaults() throws Throwable {

	}

	protected void components2Dto() throws Throwable {

		getInternalFramePersonal().getTabbedPaneSchicht().getSchichtDto()
				.setCBez(wtfBezeichnung.getText());
		getInternalFramePersonal().getTabbedPaneSchicht().getSchichtDto()
				.setMandantCNr(LPMain.getTheClient().getMandant());
		getInternalFramePersonal().getTabbedPaneSchicht().getSchichtDto()
				.setBPausenabziehen(wcbPausenAbziehen.getShort());
		getInternalFramePersonal().getTabbedPaneSchicht().getSchichtDto()
		.setBBegrenztAufTagessoll(wcbBegrenztAufTagessoll.getShort());

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory
				.getInstance()
				.getSchichtDelegate()
				.removeSchicht(
						getInternalFramePersonal().getTabbedPaneSchicht()
								.getSchichtDto());
		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (getInternalFramePersonal().getTabbedPaneSchicht()
					.getSchichtDto().getIId() == null) {
				getInternalFramePersonal()
						.getTabbedPaneSchicht()
						.getSchichtDto()
						.setIId(DelegateFactory
								.getInstance()
								.getSchichtDelegate()
								.createSchicht(
										getInternalFramePersonal()
												.getTabbedPaneSchicht()
												.getSchichtDto()));
				setKeyWhenDetailPanel(getInternalFramePersonal()
						.getTabbedPaneSchicht().getSchichtDto().getIId());

			} else {
				DelegateFactory
						.getInstance()
						.getSchichtDelegate()
						.updateSchicht(
								getInternalFramePersonal()
										.getTabbedPaneSchicht().getSchichtDto());
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						getInternalFramePersonal().getTabbedPaneSchicht()
								.getSchichtDto().getIId().toString());
			}
			eventYouAreSelected(false);

			getInternalFramePersonal().getTabbedPaneSchicht().setSchichtDto(
					DelegateFactory
							.getInstance()
							.getSchichtDelegate()
							.schichtFindByPrimaryKey(
									getInternalFramePersonal()
											.getTabbedPaneSchicht()
											.getSchichtDto().getIId()));

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
	}
}
