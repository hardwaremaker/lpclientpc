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
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.service.PASelektionDto;
import com.lp.server.partner.service.SelektionDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p> Diese Klasse kuemmert sich Partnerselektion.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; dd.12.05</p>
 *
 * <p>@author $Author: sebastian $</p>
 *
 * @version not attributable Date $Date: 2009/03/06 14:17:54 $
 */
public class PanelPASelektion extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPanePartner tpPartner = null;

	private JPanel jpaWorking = null;
	private GridBagLayout gblWorkingPanel = null;
	private Border border = null;
	private GridBagLayout gblAussen = null;

	private WrapperButton wbuSelektion = null;
	static final private String ACTION_SPECIAL_FLR_SELEKTION = "action_special_flr_selektion";
	private PanelQueryFLR panelQueryFLRSelektionAuswahl = null;
	private WrapperTextField wtfSelektion = null;
	private WrapperLabel wlaBemerkung = null;
	private WrapperTextField wtfBemerkung = null;

	public PanelPASelektion(InternalFrame internalFrame, String add2TitleI,
			Object keyI, TabbedPanePartner tpPartner) throws Throwable {
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

		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DELETE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);

		wbuSelektion = new WrapperButton();
		wbuSelektion.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.button.selektion"));
		wbuSelektion.setActionCommand(ACTION_SPECIAL_FLR_SELEKTION);
		wbuSelektion.addActionListener(this);

		wtfSelektion = new WrapperTextField();
		wtfSelektion.setActivatable(false);
		wtfSelektion.setMandatoryFieldDB(true);

		wlaBemerkung = new WrapperLabel();
		wlaBemerkung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bemerkung"));
		wtfBemerkung = new WrapperTextField();

		// das aussenpanel hat immer das gridbaglayout.
		gblAussen = new GridBagLayout();
		setLayout(gblAussen);

		// actionpanel von Oberklasse holen und anhaengen.
		add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Workingpanel
		jpaWorking = new JPanel();
		gblWorkingPanel = new GridBagLayout();
		jpaWorking.setLayout(gblWorkingPanel);
		add(jpaWorking, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Ab hier einhaengen.
		// Zeile
		jpaWorking.add(wbuSelektion, new GridBagConstraints(0, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wtfSelektion, new GridBagConstraints(1, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorking.add(wlaBemerkung, new GridBagConstraints(0, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wtfBemerkung, new GridBagConstraints(1, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		String sT = null;

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
			// Neu.
			leereAlleFelder(this);
			clearStatusbar();
			sT = getTabbedPanePartner().getServicePartnerDto()
					.formatFixTitelName1Name2();
		} else {
			// Update.
			Integer pASelektionPK = (Integer) key;
			getTabbedPanePartner().setPASelektionDto(
					DelegateFactory
							.getInstance()
							.getPartnerDelegate()
							.pASelektionFindByPrimaryKey(pASelektionPK));
			dto2Components();

			

			String sB = getTabbedPanePartner().getPASelektionDto()
					.getCBemerkung();
			sT = getTabbedPanePartner().getServicePartnerDto()
					.formatFixTitelName1Name2()
					+ (sB == null ? "" : " | " + sB);
		}
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sT);
	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		wtfBemerkung.setText(getTabbedPanePartner().getPASelektionDto()
				.getCBemerkung());
		SelektionDto selektionDto = DelegateFactory
				.getInstance()
				.getPartnerServicesDelegate()
				.selektionFindByPrimaryKey(
						getTabbedPanePartner().getPASelektionDto()
								.getSelektionIId());
		wtfSelektion.setText(selektionDto != null ? selektionDto.getCNr()
				: null);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {

			checkLockedDlg();

			components2Dto();
			if (getTabbedPanePartner().getPASelektionDto().getIId()==null) {
				// Create.
				getTabbedPanePartner().getPASelektionDto().setIId(DelegateFactory
						.getInstance()
						.getPartnerDelegate()
						.createPASelektion(
								getTabbedPanePartner().getPASelektionDto()));

				setKeyWhenDetailPanel(getTabbedPanePartner().getPASelektionDto().getIId());
			} else {
				// Update.
				DelegateFactory
						.getInstance()
						.getPartnerDelegate()
						.updatePASelektion(
								getTabbedPanePartner().getPASelektionDto());
			}
			super.eventActionSave(e, false);
			eventYouAreSelected(false);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (!isLockedDlg()) {
			DelegateFactory
					.getInstance()
					.getPartnerDelegate()
					.removePASelektion(getTabbedPanePartner()
									.getPASelektionDto());
			getTabbedPanePartner().setPASelektionDto(new PASelektionDto());

			super.eventActionDelete(e, false, false);
		}
	}

	private void components2Dto() {
		getTabbedPanePartner().getPASelektionDto().setPartnerIId(
				getTabbedPanePartner().getServicePartnerDto().getIId());
		getTabbedPanePartner().getPASelektionDto().setCBemerkung(
				wtfBemerkung.getText());
	}

	private void initPanel() throws Throwable {
		// nothing here
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PARTNER;
	}

	private TabbedPanePartner getTabbedPanePartner() {
		return tpPartner;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		if (!bNeedNoNewI) {
			getTabbedPanePartner().setPASelektionDto(new PASelektionDto());
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

		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_SELEKTION)) {

			panelQueryFLRSelektionAuswahl = PartnerFilterFactory.getInstance()
					.createPanelFLRSelektion(
							getInternalFrame(),
							false,
							getTabbedPanePartner().getPASelektionDto()
									.getSelektionIId());
			new DialogQuery(panelQueryFLRSelektionAuswahl);
		}
	}

	protected void eventItemchanged(EventObject eI) throws ExceptionLP,
			Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRSelektionAuswahl) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				getTabbedPanePartner().getPASelektionDto().setSelektionIId(iId);

				SelektionDto selektionDto = null;
				if (iId != null) {
					selektionDto = DelegateFactory.getInstance()
							.getPartnerServicesDelegate()
							.selektionFindByPrimaryKey(iId);
					wtfSelektion.setText(selektionDto != null ? selektionDto
							.getCNr() : null);
				}
			}
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuSelektion;
	}
}
