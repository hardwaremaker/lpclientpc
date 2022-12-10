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
package com.lp.client.zeiterfassung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.SonderzeitenDto;
import com.lp.server.personal.service.TaetigkeitDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeitgutschriftDto;
import com.lp.util.Helper;

public class PanelZeitgutschrift extends PanelBasis {

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
	private InternalFrameZeiterfassung internalFrameZeiterfassung = null;
	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperDateField wdfDatum = new WrapperDateField();
	private ZeitgutschriftDto zeitgutschriftDto = null;

	private WrapperLabel wlaGutschriftGeht = new WrapperLabel();
	private WrapperTimeField wtfGutschriftGeht = new WrapperTimeField();
	private WrapperLabel wlaGutschriftKommt = new WrapperLabel();
	private WrapperTimeField wtfGutschriftKommt = new WrapperTimeField();

	private ZeiterfassungPruefer zeiterfassungPruefer = null;

	public PanelZeitgutschrift(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameZeiterfassung = (InternalFrameZeiterfassung) internalFrame;
		zeiterfassungPruefer = new ZeiterfassungPruefer(getInternalFrame());
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfDatum;
	}

	protected void setDefaults() {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		zeitgutschriftDto = new ZeitgutschriftDto();

		leereAlleFelder(this);

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (zeiterfassungPruefer.pruefeObBuchungMoeglich(zeitgutschriftDto
				.getTDatum(), internalFrameZeiterfassung.getPersonalDto()
				.getIId())) {
			super.eventActionUpdate(aE, bNeedNoUpdateI);
		} else {
			return;
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (zeiterfassungPruefer.pruefeObBuchungMoeglich(zeitgutschriftDto
				.getTDatum(), internalFrameZeiterfassung.getPersonalDto()
				.getIId())) {
			DelegateFactory.getInstance().getZeiterfassungDelegate()
					.removeZeitgutschrift(zeitgutschriftDto);
			this.setKeyWhenDetailPanel(null);
			super.eventActionDelete(e, false, false);
		}
	}

	protected void components2Dto() throws ExceptionLP {
		zeitgutschriftDto.setTDatum(wdfDatum.getTimestamp());
		zeitgutschriftDto.setUGutschriftGeht(wtfGutschriftGeht.getTime());
		zeitgutschriftDto.setUGutschriftKommt(wtfGutschriftKommt.getTime());

		zeitgutschriftDto.setPersonalIId(internalFrameZeiterfassung
				.getPersonalDto().getIId());

	}

	protected void dto2Components() throws ExceptionLP, Throwable {
		wdfDatum.setTimestamp(zeitgutschriftDto.getTDatum());
		wtfGutschriftKommt.setTime(zeitgutschriftDto.getUGutschriftKommt());
		wtfGutschriftGeht.setTime(zeitgutschriftDto.getUGutschriftGeht());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			if (zeitgutschriftDto.getIId() == null) {
				zeitgutschriftDto.setIId(DelegateFactory.getInstance()
						.getZeiterfassungDelegate()
						.createZeitgutschrift(zeitgutschriftDto));
				setKeyWhenDetailPanel(zeitgutschriftDto.getIId());

			} else {
				DelegateFactory.getInstance().getZeiterfassungDelegate()
						.updateZeitgutschrift(zeitgutschriftDto);
			}

			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameZeiterfassung.getPersonalDto().getIId()
								+ "");
			}
			eventYouAreSelected(false);

		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

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

		wlaDatum.setText(LPMain.getTextRespectUISPr("lp.datum"));
		wdfDatum.setMandatoryField(true);

		// wdfBis.setActivatable(false);
		getInternalFrame().addItemChangedListener(this);

		wlaGutschriftKommt.setText(LPMain
				.getTextRespectUISPr("pers.zeitmodell.gutschriftkommt"));
		wlaGutschriftGeht.setText(LPMain
				.getTextRespectUISPr("pers.zeitmodell.gutschriftgeht"));

		wtfGutschriftKommt.setMandatoryField(true);
		wtfGutschriftGeht.setMandatoryField(true);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

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
		jpaWorkingOn.add(wlaDatum, new GridBagConstraints(0, 0, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfDatum, new GridBagConstraints(1, 0, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaGutschriftKommt, new GridBagConstraints(0, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfGutschriftKommt, new GridBagConstraints(1, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 80, 0));
		
		WrapperLabel wlaUeberschrift1 = new WrapperLabel("hh:mm");
		wlaUeberschrift1.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaUeberschrift1, new GridBagConstraints(2, 1, 1,
				1, 0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wlaGutschriftGeht, new GridBagConstraints(0, 2, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfGutschriftGeht, new GridBagConstraints(1, 2, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 80, 0));
		
		WrapperLabel wlaUeberschrift2 = new WrapperLabel("hh:mm");
		wlaUeberschrift2.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaUeberschrift2, new GridBagConstraints(2, 2, 1,
				1, 0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ZEITGUTSCHRIFT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();

			if (key != null && (key.equals(LPMain.getLockMeForNew()))) {
				wdfDatum.setTimestamp(Helper
						.cutTimestamp(new java.sql.Timestamp(System
								.currentTimeMillis())));
			}

		} else {
			zeitgutschriftDto = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.zeitgutschriftFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

}
