package com.lp.client.nachrichten;

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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.BenutzermandantsystemrolleDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.benutzer.service.SystemrolleDto;
import com.lp.server.personal.service.NachrichtenaboDto;
import com.lp.server.personal.service.NachrichtengruppeDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class PanelNachrichtenabo extends PanelBasis {

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
	private InternalFrameNachrichten internalFrameNachrichten = null;
	private NachrichtenaboDto nachrichtenaboDto = null;

	private WrapperButton wbuNachrichtengruppe = new WrapperButton();

	private WrapperTextField wtfNachrichtengruppe = new WrapperTextField();

	private WrapperButton wbuPersonal = new WrapperButton();
	private WrapperTextField wtfPersonal = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRNachrichtengruppe = null;
	private PanelQueryFLR panelQueryFLRPersonal = null;

	static final public String ACTION_SPECIAL_SYSTEMROLLE_FROM_LISTE = "action_systemrolle_from_liste";
	static final public String ACTION_SPECIAL_PERSONAL_FROM_LISTE = "action_personal_from_liste";

	public PanelNachrichtenabo(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameNachrichten = (InternalFrameNachrichten) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuNachrichtengruppe;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		nachrichtenaboDto = new NachrichtenaboDto();
		leereAlleFelder(this);
	}

	void dialogQueryPersonalFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRPersonal = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonal(getInternalFrame(), true, true);

		new DialogQuery(panelQueryFLRPersonal);

	}

	void dialogQueryGruppeFromListe(ActionEvent e, boolean alternativeRolle)
			throws Throwable {
		String[] aWhichButtonIUse = alternativeRolle ? new String[] {
				PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN }
				: new String[] { PanelBasis.ACTION_REFRESH };

		panelQueryFLRNachrichtengruppe = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_NACHRICHTENGRUPPE, aWhichButtonIUse,
				internalFrameNachrichten,
				LPMain.getTextRespectUISPr("pers.nachrichtengruppen"));

		panelQueryFLRNachrichtengruppe.setSelectedId(nachrichtenaboDto
				.getNachrichtengruppeIId());
		new DialogQuery(panelQueryFLRNachrichtengruppe);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_SYSTEMROLLE_FROM_LISTE)) {
			dialogQueryGruppeFromListe(e, false);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_PERSONAL_FROM_LISTE)) {

			dialogQueryPersonalFromListe(e);

		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		DelegateFactory.getInstance().getNachrichtenDelegate()
				.removeNachrichtenabo(nachrichtenaboDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() {
		nachrichtenaboDto.setNachrichtenartIId(internalFrameNachrichten
				.getNachrichtenartDto().getIId());
	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		if (nachrichtenaboDto.getPersonalIId() != null) {
			wtfPersonal.setText(DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							nachrichtenaboDto.getPersonalIId()).formatAnrede());
		} else {
			wtfPersonal.setText("");
		}

		if (nachrichtenaboDto.getNachrichtengruppeIId() != null) {

			wtfNachrichtengruppe.setText(DelegateFactory
					.getInstance()
					.getNachrichtenDelegate()
					.nachrichtengruppeFindByPrimaryKey(
							nachrichtenaboDto.getNachrichtengruppeIId()).getCBez());
		} else {
			wtfNachrichtengruppe.setText("");
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (nachrichtenaboDto.getIId() == null) {
				nachrichtenaboDto.setIId(DelegateFactory.getInstance()
						.getNachrichtenDelegate()
						.createNachrichtenabo(nachrichtenaboDto));
				setKeyWhenDetailPanel(nachrichtenaboDto.getIId());
			} else {
				DelegateFactory.getInstance().getNachrichtenDelegate()
						.updateNachrichtenabo(nachrichtenaboDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						nachrichtenaboDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRNachrichtengruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				NachrichtengruppeDto nachrichtengruppeDto = DelegateFactory
						.getInstance().getNachrichtenDelegate()
						.nachrichtengruppeFindByPrimaryKey((Integer) key);
				wtfNachrichtengruppe.setText(nachrichtengruppeDto.getCBez());
				nachrichtenaboDto.setNachrichtengruppeIId(nachrichtengruppeDto
						.getIId());
			} else if (e.getSource() == panelQueryFLRPersonal) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);
					wtfPersonal.setText(personalDto.formatAnrede());
					nachrichtenaboDto.setPersonalIId((Integer) key);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
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

		wbuPersonal.addActionListener(this);

		wbuPersonal.setActionCommand(ACTION_SPECIAL_PERSONAL_FROM_LISTE);

		wbuNachrichtengruppe.setText(LPMain
				.getTextRespectUISPr("pers.nachrichtengruppe") + "...");
		wbuPersonal.setText(LPMain.getTextRespectUISPr("lp.personal"));

		wbuNachrichtengruppe
				.setActionCommand(ACTION_SPECIAL_SYSTEMROLLE_FROM_LISTE);
		wbuNachrichtengruppe.addActionListener(this);

		wtfPersonal.setActivatable(false);
		// wtfPersonal.setMandatoryField(true);

		wtfNachrichtengruppe.setActivatable(false);
		// wtfNachrichtengruppe.setMandatoryField(true);
		wtfNachrichtengruppe.setText("");

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jpaWorkingOn = new JPanel(new MigLayout("insets 0, wrap 2",
		// "[25%, fill][75%, fill]", "")) ;
		jpaWorkingOn = new JPanel();
		HvLayout layout = HvLayoutFactory.create(jpaWorkingOn,
				"insets 0, wrap 2", "[25%, fill][75%, fill]", "");

		layout.add(wbuPersonal).add(wtfPersonal);
		layout.add(wbuNachrichtengruppe).add(wtfNachrichtengruppe);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_NACHRICHTENART;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			nachrichtenaboDto = DelegateFactory.getInstance()
					.getNachrichtenDelegate()
					.nachrichtenaboFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

}
