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

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.lp.client.frame.*;
import com.lp.client.frame.component.*;
import com.lp.client.pc.*;
import com.lp.server.personal.service.*;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.util.Helper;
import com.lp.client.frame.dialog.*;

public class PanelKollektivuestd50 extends PanelBasis {

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
	private InternalFramePersonal internalFramePersonal = null;

	private Kollektivuestd50Dto kollektivuestd50Dto = null;

	private WrapperLabel wlaTagesart = new WrapperLabel();
	private WrapperComboBox wcoTagesart = new WrapperComboBox();
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperTimeField wtfBis = new WrapperTimeField();
	private WrapperLabel wlaAb = new WrapperLabel();
	private WrapperTimeField wtfAb = new WrapperTimeField();
	private WrapperCheckBox wcbRestdestages = new WrapperCheckBox();
	private WrapperCheckBox wcbPausenIgnorieren = new WrapperCheckBox();

	private WrapperLabel wla0000 = new WrapperLabel();
	private WrapperLabel wla2400 = new WrapperLabel();

	public PanelKollektivuestd50(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFramePersonal) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() throws Throwable {
		wcoTagesart.setMap(DelegateFactory.getInstance()
				.getZeiterfassungDelegate().getAllSprTagesarten());
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcoTagesart;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		kollektivuestd50Dto = new Kollektivuestd50Dto();
		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getPersonalDelegate()
				.removeKollektivuestd(kollektivuestd50Dto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {
		kollektivuestd50Dto.setKollektivIId(internalFramePersonal
				.getKollektivDto().getIId());
		kollektivuestd50Dto.setTagesartIId((Integer) wcoTagesart
				.getKeyOfSelectedItem());
		kollektivuestd50Dto.setUBis(wtfBis.getTime());
		kollektivuestd50Dto.setUVon(wtfAb.getTime());
		kollektivuestd50Dto.setBRestdestages(wcbRestdestages.getShort());
		kollektivuestd50Dto.setBUnterignorieren(wcbPausenIgnorieren.getShort());
	}

	protected void dto2Components() throws ExceptionLP, Throwable {
		wcbRestdestages.setShort(kollektivuestd50Dto.getBRestdestages());
		wcbPausenIgnorieren.setShort(kollektivuestd50Dto.getBUnterignorieren());
		wtfBis.setTime(kollektivuestd50Dto.getUBis());
		wtfAb.setTime(kollektivuestd50Dto.getUVon());
		wcoTagesart.setKeyOfSelectedItem(kollektivuestd50Dto.getTagesartIId());

		if (Helper.short2Boolean(kollektivuestd50Dto.getBRestdestages()) == false) {
			wtfBis.setTime(kollektivuestd50Dto.getUBis());
			wtfBis.setActivatable(true);
		} else {
			wtfBis.setTime(null);
			wtfBis.setActivatable(false);
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			if (wtfBis.getTime().before(wtfAb.getTime())
					|| Helper.short2boolean(wcbRestdestages.getShort()) == true) {
				components2Dto();
				if (kollektivuestd50Dto.getIId() == null) {
					kollektivuestd50Dto.setIId(DelegateFactory.getInstance()
							.getPersonalDelegate()
							.createKollektivuestd50(kollektivuestd50Dto));
					setKeyWhenDetailPanel(kollektivuestd50Dto.getIId());
				} else {
					DelegateFactory.getInstance().getPersonalDelegate()
							.updateKollektivuestd50(kollektivuestd50Dto);
				}

				super.eventActionSave(e, true);

				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(
							internalFramePersonal.getZeitmodellDto().getIId()
									+ "");
				}
				eventYouAreSelected(false);
			} else {
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("lp.error.beginnvorende"));
			}
		}

	}

	private void visibleAnhandBerechnungsbasis() {
		if (internalFramePersonal.getKollektivDto().getIBerechnungsbasis() == PersonalFac.KOLLEKTIV_BERECHNUNGSBASIS_UHRZEIT) {
			wcbPausenIgnorieren.setVisible(true);
			wcbRestdestages.setVisible(true);
			wlaBis.setVisible(true);
			wtfBis.setVisible(true);
			wla2400.setVisible(true);

			wla0000.setVisible(true);
			wla2400.setVisible(true);
			wlaAb.setVisible(true);
			wtfAb.setVisible(true);

		} else if (internalFramePersonal.getKollektivDto()
				.getIBerechnungsbasis() == PersonalFac.KOLLEKTIV_BERECHNUNGSBASIS_SOLLZEIT_ZEITMODEL) {
			wcbPausenIgnorieren.setVisible(false);
			wcbRestdestages.setVisible(false);
			wlaBis.setVisible(false);
			wtfBis.setVisible(false);
			wla2400.setVisible(false);

			wla0000.setVisible(false);
			wla2400.setVisible(false);

			wtfAb.setVisible(false);
			wlaAb.setVisible(false);

		} else {
			wcbPausenIgnorieren.setVisible(false);
			wcbRestdestages.setVisible(false);
			wlaBis.setVisible(false);
			wtfBis.setVisible(false);
			wla2400.setVisible(false);

			wla0000.setVisible(false);
			wla2400.setVisible(false);

			wlaAb.setVisible(true);
			wtfAb.setVisible(true);

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

		getInternalFrame().addItemChangedListener(this);
		wlaTagesart.setText(LPMain.getTextRespectUISPr("lp.tagesart"));
		wcoTagesart.setMandatoryField(true);
		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wlaAb.setText(LPMain.getTextRespectUISPr("lp.ab"));
		wtfAb.setMandatoryField(true);
		wtfBis.setMandatoryField(true);

		wla0000.setText("00:00 " + LPMain.getTextRespectUISPr("lp.uhr") + " -");
		wla2400.setText(" - 24:00 " + LPMain.getTextRespectUISPr("lp.uhr"));

		Dimension labelDimension = HelperClient.getSizeFactoredDimension(90);
	    HelperClient.setMinimumAndPreferredSize(wcoTagesart, labelDimension);
	    HelperClient.setMinimumAndPreferredSize(wla0000, labelDimension);
	    HelperClient.setMinimumAndPreferredSize(wla2400, labelDimension);
	    HelperClient.setMinimumAndPreferredSize(wlaBis, labelDimension);
	    HelperClient.setMinimumAndPreferredSize(wlaAb, labelDimension);
	    HelperClient.setMinimumAndPreferredSize(wcbRestdestages, labelDimension);
	    HelperClient.setMinimumAndPreferredSize(wcbPausenIgnorieren,HelperClient.getSizeFactoredDimension(120));
	    
	    Dimension timeFieldDimension = HelperClient.getSizeFactoredDimension(75);
	    HelperClient.setMinimumAndPreferredSize(wtfBis, timeFieldDimension);
	    HelperClient.setMinimumAndPreferredSize(wtfAb, timeFieldDimension);
	    
		wcbRestdestages.setText(LPMain
				.getTextRespectUISPr("pers.zutritt.restdestages"));
		wcbPausenIgnorieren.setText(LPMain
				.getTextRespectUISPr("pers.kollektivuestd.unterignorieren"));
		wcbRestdestages.setMnemonic('R');
		wcbRestdestages
				.addActionListener(new PanelKollektivuestd50_wcbRestdestages_actionAdapter(
						this));

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
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaTagesart, new GridBagConstraints(1, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wcoTagesart, new GridBagConstraints(2, 0, 5, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wla0000, new GridBagConstraints(0, 1, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaBis, new GridBagConstraints(1, 1, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wtfBis, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 2, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaAb, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wtfAb, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 2, 0, 0), 0, 0));

		jpaWorkingOn.add(wcbRestdestages, new GridBagConstraints(5, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wla2400, new GridBagConstraints(6, 1, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wcbPausenIgnorieren, new GridBagConstraints(5, 0, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	public void wcbRestdestages_actionPerformed(ActionEvent e) {
		if (wcbRestdestages.isSelected()) {
			wtfBis.setEnabled(false);
		} else {
			wtfBis.setEnabled(true);

		}

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_KOLLEKTIV;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
			if (wcbRestdestages.isSelected()) {
				wtfBis.setEnabled(false);
			} else {
				if (key != null) {
					wtfBis.setEnabled(true);
				}
			}
		} else {
			kollektivuestd50Dto = DelegateFactory.getInstance()
					.getPersonalDelegate()
					.kollektivuestd50FindByPrimaryKey((Integer) key);
			dto2Components();

		}
		internalFramePersonal.getTabbedPaneKollektiv().disableUest100();
		internalFramePersonal.getTabbedPaneKollektiv().disableBetriebsvereinbarung();
		visibleAnhandBerechnungsbasis();
	}
}

class PanelKollektivuestd50_wcbRestdestages_actionAdapter implements
		ActionListener {
	private PanelKollektivuestd50 adaptee;

	PanelKollektivuestd50_wcbRestdestages_actionAdapter(
			PanelKollektivuestd50 adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcbRestdestages_actionPerformed(e);
	}
}
