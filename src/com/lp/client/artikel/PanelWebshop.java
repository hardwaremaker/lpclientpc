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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.WebshopDto;

public class PanelWebshop extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InternalFrameArtikel internalFrameArtikel = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();

	private WrapperLabel wlaWebshopart = new WrapperLabel();
	private WrapperComboBox wcoWebshopart = new WrapperComboBox();
	private WrapperLabel wlaUrl = new WrapperLabel();
	private WrapperTextField wtfUrl = new WrapperTextField();
	private WrapperLabel wlaUser = new WrapperLabel();
	private WrapperTextField wtfUser = new WrapperTextField();
	private WrapperLabel wlaPassword = new WrapperLabel();
	private WrapperTextField wtfPassword = new WrapperTextField();
	
	private WebshopDto webshopDto = null;

	public InternalFrameArtikel getInternalFrameInstandhaltung() {
		return internalFrameArtikel;
	}

	public PanelWebshop(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;

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
			webshopDto = DelegateFactory.getInstance().getArtikelDelegate()
					.webshopFindByPrimaryKey((Integer) key);

			dto2Components();
		}
	}

	protected void dto2Components() throws Throwable {
		wtfBezeichnung.setText(webshopDto.getCBez());
		wtfUrl.setText(webshopDto.getCUrl());
		wtfUser.setText(webshopDto.getCUser());
		wtfPassword.setText(webshopDto.getCPassword());
		wcoWebshopart.setKeyOfSelectedItem(webshopDto.getWebshopartCnr());
		setConnectionUI(hasConnectionUI(webshopDto.getWebshopartCnr()));
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

		wlaBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wtfBezeichnung.setMandatoryField(true);

		wlaWebshopart.setText(LPMain.getTextRespectUISPr("webshop.webshopart"));
		wcoWebshopart.setMandatoryField(true);
		wcoWebshopart.addActionListener(this);
		wcoWebshopart.setToken("webshopart");
		
		wlaUrl.setText(LPMain.getTextRespectUISPr("webshop.url"));
		wlaUser.setText(LPMain.getTextRespectUISPr("webshop.user"));
		wlaPassword.setText(LPMain.getTextRespectUISPr("webshop.password"));
		
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
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 3,
				1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaWebshopart, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoWebshopart, new GridBagConstraints(1, iZeile, 3,
				1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++;
		jpaWorkingOn.add(wlaUrl, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfUrl, new GridBagConstraints(1, iZeile, 3,
				1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++;
		jpaWorkingOn.add(wlaUser, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfUser, new GridBagConstraints(1, iZeile, 3,
				1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++;
		jpaWorkingOn.add(wlaPassword, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPassword, new GridBagConstraints(1, iZeile, 3,
				1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, 
				ACTION_DELETE, ACTION_DISCARD };

		enableToolsPanelButtons(aWhichButtonIUse);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);

		webshopDto = new WebshopDto();
	}

	private void setConnectionUI(boolean enable) {
		wlaUrl.setVisible(enable);
		wtfUrl.setVisible(enable);
		wlaUser.setVisible(enable);
		wtfUser.setVisible(enable);
		wlaPassword.setVisible(enable);
		wtfPassword.setVisible(enable);		
	}
	
	private boolean hasConnectionUI(String webshopartCnr) {
		return !ArtikelFac.WEBSHOPART_NICHT_ZUTREFFEND
				.equals(webshopartCnr);		
	}
	
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource().equals(wcoWebshopart)) {
			setConnectionUI(hasConnectionUI(
					(String)wcoWebshopart.getKeyOfSelectedItem()));
		}
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_WEBSHOP;
	}

	protected void setDefaults() throws Throwable {
		Map<?, ?> m = DelegateFactory.getInstance().getArtikelDelegate()
				.getAllSprWebshoparten();
		m.remove(ArtikelFac.ARTIKELART_HANDARTIKEL);
		wcoWebshopart.setMap(m);
	}

	protected void components2Dto() throws Throwable {
		webshopDto.setCBez(wtfBezeichnung.getText());
		webshopDto.setMandantCNr(LPMain.getTheClient().getMandant());
		if(!hasConnectionUI((String)wcoWebshopart.getKeyOfSelectedItem())) {
			wtfUrl.setText(null);
			wtfPassword.setText(null);
			wtfUser.setText(null);			
		}
		webshopDto.setCUrl(wtfUrl.getText());
		webshopDto.setCPassword(wtfPassword.getText());
		webshopDto.setCUser(wtfUser.getText());
		webshopDto.setWebshopartCnr((String)wcoWebshopart.getKeyOfSelectedItem());
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getArtikelDelegate()
				.removeWebshop(webshopDto);
		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			if (webshopDto.getIId() == null) {
				webshopDto.setIId(DelegateFactory.getInstance()
						.getArtikelDelegate().createWebshop(webshopDto));
				setKeyWhenDetailPanel(webshopDto.getIId());

			} else {

				DelegateFactory.getInstance().getArtikelDelegate()
						.updateWebshop(webshopDto);

			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						webshopDto.getIId().toString());
			}
			eventYouAreSelected(false);

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
//		ItemChangedEvent e = (ItemChangedEvent) eI;
	}

}
