
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
package com.lp.client.partner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
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
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.partner.service.KundespediteurDto;
import com.lp.server.partner.service.PASelektionDto;
import com.lp.server.partner.service.SelektionDto;
import com.lp.server.system.service.SpediteurDto;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse kuemmert sich Partnerselektion.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Josef Ornetsmueller; dd.12.05</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2009/07/13 09:42:41 $
 */
public class PanelKundespediteur extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneKunde tpPartner = null;

	private JPanel jpaWorking = null;
	private GridBagLayout gblWorkingPanel = null;
	private Border border = null;
	private GridBagLayout gblAussen = null;

	private WrapperButton wbuSpedituer = null;
	static final private String ACTION_SPECIAL_FLR_SPEDITEUR = "action_special_flr_spedituer";
	private PanelQueryFLR panelQueryFLRSpediteur = null;
	private WrapperTextField wtfSpediteur = null;
	private WrapperLabel wlaAccounting = null;
	private WrapperTextField wtfAccounting = null;

	private WrapperLabel wlaGewichtinkg = null;
	private WrapperNumberField wnfGewichtinkg = null;

	private KundespediteurDto kundespediteurDto = new KundespediteurDto();

	public PanelKundespediteur(InternalFrame internalFrame, String add2TitleI, Object keyI, TabbedPaneKunde tpPartner)
			throws Throwable {
		super(internalFrame, add2TitleI, keyI);
		this.tpPartner = tpPartner;
		jbInit();
		initComponents();
		initPanel();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		// buttons.
		resetToolsPanel();

		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);

		wbuSpedituer = new WrapperButton();
		wbuSpedituer.setText(LPMain.getInstance().getTextRespectUISPr("button.spediteur"));
		wbuSpedituer.setActionCommand(ACTION_SPECIAL_FLR_SPEDITEUR);
		wbuSpedituer.addActionListener(this);

		wtfSpediteur = new WrapperTextField();
		wtfSpediteur.setActivatable(false);
		wtfSpediteur.setMandatoryFieldDB(true);

		wlaAccounting = new WrapperLabel();
		wlaAccounting.setText(LPMain.getInstance().getTextRespectUISPr("kunde.kundespediteur.accounting"));
		wtfAccounting = new WrapperTextField();
		wtfAccounting.setMandatoryField(true);

		wlaGewichtinkg = new WrapperLabel();
		
		wnfGewichtinkg = new WrapperNumberField();
		wnfGewichtinkg.setMandatoryField(true);
		wlaGewichtinkg.setText(LPMain.getInstance().getTextRespectUISPr("kunde.kundespediteur.gewichtinkg"));

		// das aussenpanel hat immer das gridbaglayout.
		gblAussen = new GridBagLayout();
		setLayout(gblAussen);

		// actionpanel von Oberklasse holen und anhaengen.
		add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// Workingpanel
		jpaWorking = new JPanel();
		gblWorkingPanel = new GridBagLayout();
		jpaWorking.setLayout(gblWorkingPanel);
		add(jpaWorking, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		// Ab hier einhaengen.
		// Zeile
		jpaWorking.add(wbuSpedituer, new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wtfSpediteur, new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorking.add(wlaGewichtinkg, new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wnfGewichtinkg, new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorking.add(wlaAccounting, new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wtfAccounting, new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		String sT = null;

		if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {
			// Neu.
			leereAlleFelder(this);
			clearStatusbar();

			sT = getTabbedPaneKunde().getKundeDto().getPartnerDto().formatFixTitelName1Name2();
		} else {
			// Update.
			Integer iid = (Integer) key;
			kundespediteurDto = DelegateFactory.getInstance().getKundeDelegate().kundespediteurFindByPrimaryKey(iid);
			dto2Components();

			sT = getTabbedPaneKunde().getKundeDto().getPartnerDto().formatFixTitelName1Name2();
		}
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sT);
	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		wtfAccounting.setText(kundespediteurDto.getCAccounting());

		SpediteurDto s = DelegateFactory.getInstance().getMandantDelegate()
				.spediteurFindByPrimaryKey(kundespediteurDto.getSpediteurIId());

		wtfSpediteur.setText(s.getCNamedesspediteurs());

		wnfGewichtinkg.setBigDecimal(kundespediteurDto.getNGewichtinkg());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {

		if (allMandatoryFieldsSetDlg()) {

			checkLockedDlg();

			components2Dto();
			if (kundespediteurDto.getIId() == null) {
				// Create.
				kundespediteurDto.setIId(
						DelegateFactory.getInstance().getKundeDelegate().createKundespediteur(kundespediteurDto));

				setKeyWhenDetailPanel(kundespediteurDto.getIId());
			} else {
				// Update.
				DelegateFactory.getInstance().getKundeDelegate().updateKundespediteur(kundespediteurDto);
			}
			super.eventActionSave(e, false);
			eventYouAreSelected(false);
		}
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (!isLockedDlg()) {
			DelegateFactory.getInstance().getKundeDelegate().removeKundespediteur(kundespediteurDto);
			kundespediteurDto = new KundespediteurDto();

			super.eventActionDelete(e, false, false);
		}
	}

	private void components2Dto() throws Throwable{
		kundespediteurDto.setKundeIId(getTabbedPaneKunde().getKundeDto().getIId());
		kundespediteurDto.setCAccounting(wtfAccounting.getText());
		kundespediteurDto.setNGewichtinkg(wnfGewichtinkg.getBigDecimal());
	}

	private void initPanel() throws Throwable {
		// nothing here
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_KUNDE;
	}

	private TabbedPaneKunde getTabbedPaneKunde() {
		return tpPartner;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		if (!bNeedNoNewI) {
			kundespediteurDto = new KundespediteurDto();
			setDefaults();
		}
	}

	/**
	 * setDefaults
	 * 
	 * @throws Throwable
	 */
	private void setDefaults() throws Throwable {
		// nothing here
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_SPEDITEUR)) {

			panelQueryFLRSpediteur = SystemFilterFactory.getInstance().createPanelFLRSpediteur(getInternalFrame(),
					kundespediteurDto.getSpediteurIId());
			new DialogQuery(panelQueryFLRSpediteur);
		}

	}

	protected void eventItemchanged(EventObject eI) throws ExceptionLP, Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRSpediteur) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				if (iId != null) {
					kundespediteurDto.setSpediteurIId(iId);
					SpediteurDto s = DelegateFactory.getInstance().getMandantDelegate().spediteurFindByPrimaryKey(iId);

					wtfSpediteur.setText(s.getCNamedesspediteurs());
				}
			}
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuSpedituer;
	}
}