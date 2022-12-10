package com.lp.client.personal;

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

public class PanelKollektivUestdBVA extends PanelBasis {

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

	private KollektivUestdBVADto kollektivuestdBVADto = null;

	private WrapperLabel wlaTagesart = new WrapperLabel();
	private WrapperComboBox wcoTagesart = new WrapperComboBox();
	private WrapperLabel wla100Ende = new WrapperLabel();
	private WrapperTimeField wtf100Ende = new WrapperTimeField();

	private WrapperLabel wla50Ende = new WrapperLabel();
	private WrapperTimeField wtf50Ende = new WrapperTimeField();

	private WrapperTimeField wtf50Beginn = new WrapperTimeField();

	private WrapperTimeField wtf100Beginn = new WrapperTimeField();

	private WrapperTimeField wtfGleitzeitBis = new WrapperTimeField();

	private WrapperLabel wla0000 = new WrapperLabel();
	private WrapperLabel wla2400 = new WrapperLabel();

	public PanelKollektivUestdBVA(InternalFrame internalFrame,
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
		kollektivuestdBVADto = new KollektivUestdBVADto();
		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getPersonalDelegate()
				.removeKollektivuestdBVA(kollektivuestdBVADto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {
		kollektivuestdBVADto.setKollektivIId(internalFramePersonal
				.getKollektivDto().getIId());
		kollektivuestdBVADto.setTagesartIId((Integer) wcoTagesart
				.getKeyOfSelectedItem());
		kollektivuestdBVADto.setU100Ende(wtf100Ende.getTime());
		kollektivuestdBVADto.setU100Beginn(wtf100Beginn.getTime());
		kollektivuestdBVADto.setU50Beginn(wtf50Beginn.getTime());
		kollektivuestdBVADto.setU50Ende(wtf50Ende.getTime());
		kollektivuestdBVADto.setUGleitzeitBis(wtfGleitzeitBis.getTime());

	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		wtf100Ende.setTime(kollektivuestdBVADto.getU100Ende());
		wtf100Beginn.setTime(kollektivuestdBVADto.getU100Beginn());
		wtf50Beginn.setTime(kollektivuestdBVADto.getU50Beginn());
		wtf50Ende.setTime(kollektivuestdBVADto.getU50Ende());
		wtfGleitzeitBis.setTime(kollektivuestdBVADto.getUGleitzeitBis());
		wcoTagesart.setKeyOfSelectedItem(kollektivuestdBVADto.getTagesartIId());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();
			
			
			
			//Pruefen:
			if(kollektivuestdBVADto.getU50Ende().getTime()<kollektivuestdBVADto.getU100Ende().getTime()){
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), LPMain.getTextRespectUISPr("pers.uest.betriebsvereinbarung.a.error1"));
				return;
			}

			if(kollektivuestdBVADto.getU50Beginn().getTime()<kollektivuestdBVADto.getU50Ende().getTime()){
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), LPMain.getTextRespectUISPr("pers.uest.betriebsvereinbarung.a.error2"));
				return;
			}

			if(kollektivuestdBVADto.getU100Beginn().getTime()<kollektivuestdBVADto.getU50Beginn().getTime()){
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), LPMain.getTextRespectUISPr("pers.uest.betriebsvereinbarung.a.error3"));
				return;
			}


			
			
			if (kollektivuestdBVADto.getIId() == null) {
				kollektivuestdBVADto.setIId(DelegateFactory.getInstance()
						.getPersonalDelegate()
						.createKollektivuestdBVA(kollektivuestdBVADto));
				setKeyWhenDetailPanel(kollektivuestdBVADto.getIId());
			} else {
				DelegateFactory.getInstance().getPersonalDelegate()
						.updateKollektivUestdBVA(kollektivuestdBVADto);
			}

			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFramePersonal.getZeitmodellDto().getIId() + "");
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

		getInternalFrame().addItemChangedListener(this);
		wlaTagesart.setText(LPMain.getTextRespectUISPr("lp.tagesart"));
		wcoTagesart.setMandatoryField(true);
		wla100Ende.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wla50Ende.setText(LPMain.getTextRespectUISPr("lp.ab"));
		wtf100Beginn.setMandatoryField(true);
		wtf100Ende.setMandatoryField(true);
		wtf50Beginn.setMandatoryField(true);
		wtf50Ende.setMandatoryField(true);
		wtfGleitzeitBis.setMandatoryField(true);

		wla0000.setText("00:00 " + LPMain.getTextRespectUISPr("lp.uhr") + "-");
		wla2400.setText("-24:00 " + LPMain.getTextRespectUISPr("lp.uhr"));

		Dimension labelDimension = HelperClient.getSizeFactoredDimension(90);
		HelperClient.setMinimumAndPreferredSize(wcoTagesart, labelDimension);
		HelperClient.setMinimumAndPreferredSize(wla100Ende, labelDimension);
		HelperClient.setMinimumAndPreferredSize(wla50Ende, labelDimension);

		Dimension timeFieldDimension = HelperClient
				.getSizeFactoredDimension(75);
		// HelperClient.setMinimumAndPreferredSize(wtf100Ende,
		// timeFieldDimension);
		HelperClient.setMinimumAndPreferredSize(wtf100Beginn,
				timeFieldDimension);
		HelperClient
				.setMinimumAndPreferredSize(wtf50Beginn, timeFieldDimension);
		HelperClient.setMinimumAndPreferredSize(wtf50Ende, timeFieldDimension);
		HelperClient.setMinimumAndPreferredSize(wtfGleitzeitBis,
				timeFieldDimension);

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

		iZeile++;

		jpaWorkingOn.add(wlaTagesart, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wcoTagesart, new GridBagConstraints(2, iZeile, 5, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn
				.add(new WrapperLabel(
						LPMain.getTextRespectUISPr("pers.uest.betriebsvereinbarung.a.100ende")),
						new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(0, 0,
										0, 20), 100, 0));
		
		
		ImageIcon iicon = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/clock16x16.png"));
		WrapperLabel wlaUeberschrift3 = new WrapperLabel(iicon);
		wlaUeberschrift3.setHorizontalAlignment(SwingConstants.RIGHT);
		jpaWorkingOn.add(wlaUeberschrift3, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20, 0));
		

		jpaWorkingOn
				.add(new WrapperLabel(
						LPMain.getTextRespectUISPr("pers.uest.betriebsvereinbarung.a.50ende")),
						new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(0, 0,
										0, 20), 100, 0));

		
		
		WrapperLabel wlaUeberschrift4 = new WrapperLabel(iicon);
		wlaUeberschrift4.setHorizontalAlignment(SwingConstants.RIGHT);
		jpaWorkingOn.add(wlaUeberschrift4, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20, 0));
		
		jpaWorkingOn
				.add(new WrapperLabel(
						LPMain.getTextRespectUISPr("pers.uest.betriebsvereinbarung.a.gzbis")),
						new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(0, 0,
										0, 15), 100, 0));

		jpaWorkingOn
				.add(new WrapperLabel(
						LPMain.getTextRespectUISPr("pers.uest.betriebsvereinbarung.a.50beginn")),
						new GridBagConstraints(5, iZeile, 1, 1, 0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(0, 0,
										0, 20), 100, 0));
		
		WrapperLabel wlaUeberschrift5 = new WrapperLabel(iicon);
		wlaUeberschrift5.setHorizontalAlignment(SwingConstants.RIGHT);
		jpaWorkingOn.add(wlaUeberschrift5, new GridBagConstraints(5, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20, 0));
		

		jpaWorkingOn
				.add(new WrapperLabel(
						LPMain.getTextRespectUISPr("pers.uest.betriebsvereinbarung.a.100beginn")),
						new GridBagConstraints(6, iZeile, 1, 1, 0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(0, 0,
										0, 20), 100, 0));
		WrapperLabel wlaUeberschrift6 = new WrapperLabel(iicon);
		wlaUeberschrift6.setHorizontalAlignment(SwingConstants.RIGHT);
		jpaWorkingOn.add(wlaUeberschrift6, new GridBagConstraints(6, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20, 0));
		
		iZeile++;

		jpaWorkingOn.add(wla0000, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 50, 0));
		jpaWorkingOn.add(wtf100Ende, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));

		jpaWorkingOn.add(wtf50Ende, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));

		jpaWorkingOn.add(wtfGleitzeitBis, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 17, 0, 0), 0, 0));
		
		
		

		jpaWorkingOn.add(wtf50Beginn, new GridBagConstraints(5, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));

		jpaWorkingOn.add(wtf100Beginn, new GridBagConstraints(6, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));

		jpaWorkingOn.add(wla2400, new GridBagConstraints(7, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 50, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

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

		} else {
			kollektivuestdBVADto = DelegateFactory.getInstance()
					.getPersonalDelegate()
					.kollektivUestdBVAFindByPrimaryKey((Integer) key);
			dto2Components();

		}
		internalFramePersonal.getTabbedPaneKollektiv().disableUest100();
		internalFramePersonal.getTabbedPaneKollektiv().disableBetriebsvereinbarung();

	}
}
