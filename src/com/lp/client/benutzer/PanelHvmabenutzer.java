
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
package com.lp.client.benutzer;

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
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.RollerechtDto;
import com.lp.server.personal.service.HvmabenutzerDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelHvmabenutzer extends PanelBasis {

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
	private InternalFrameBenutzer internalFrameBenutzer = null;
	private WrapperButton wbuBenutzer = new WrapperButton();
	private HvmabenutzerDto hvmabenutzerDto = null;
	private WrapperTextField wtfBenutzer = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRBenutzer = null;

	static final public String ACTION_SPECIAL_BENUTZER_FROM_LISTE = "action_benutzer_from_liste";

	public PanelHvmabenutzer(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameBenutzer = (InternalFrameBenutzer) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuBenutzer;
	}

	protected void setDefaults() {

	}

	void dialogQueryBenutzerFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		panelQueryFLRBenutzer = new PanelQueryFLR(null, null, QueryParameters.UC_ID_BENUZTER, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr("title.benutzerauswahlliste"));

		panelQueryFLRBenutzer.befuellePanelFilterkriterienDirekt(
				BenutzerFilterFactory.getInstance().createFKDBenutzerkennung(),
				BenutzerFilterFactory.getInstance().createFKDNachname());

		panelQueryFLRBenutzer.setSelectedId(hvmabenutzerDto.getBenutzerIId());

		new DialogQuery(panelQueryFLRBenutzer);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		hvmabenutzerDto = new HvmabenutzerDto();
	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		BenutzerDto benutzerDto = DelegateFactory.getInstance().getBenutzerDelegate()
				.benutzerFindByPrimaryKey(hvmabenutzerDto.getBenutzerIId());
		wtfBenutzer.setText(benutzerDto.getCBenutzerkennung());

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			hvmabenutzerDto = DelegateFactory.getInstance().getHvmaDelegate()
					.hvmabenutzerFindByPrimaryKey((Integer) key);
			dto2Components();

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRBenutzer) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				BenutzerDto benutzerDto = DelegateFactory.getInstance().getBenutzerDelegate()
						.benutzerFindByPrimaryKey((Integer) key);

				wtfBenutzer.setText(benutzerDto.getCBenutzerkennung());
				hvmabenutzerDto.setBenutzerIId((Integer) key);
			}
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_BENUTZER_FROM_LISTE)) {
			dialogQueryBenutzerFromListe(e);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (hvmabenutzerDto.getIId() == null) {
				hvmabenutzerDto.setHvmalizenzIId(internalFrameBenutzer.getTabbedPaneHvma().getHvmalizenzDto().getIId());
				hvmabenutzerDto
						.setIId(DelegateFactory.getInstance().getHvmaDelegate().createHvmabenutzer(hvmabenutzerDto));
				setKeyWhenDetailPanel(hvmabenutzerDto.getIId());
			} else {
				DelegateFactory.getInstance().getHvmaDelegate().updateHvmabenutzer(hvmabenutzerDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameBenutzer.getTabbedPaneHvma().getHvmalizenzDto().getIId().toString());
			}
			eventYouAreSelected(false);
		}
	}

	protected void components2Dto() {
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getHvmaDelegate().removeHvmabenutzer(hvmabenutzerDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
		hvmabenutzerDto = new HvmabenutzerDto();
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

		wbuBenutzer.setText(LPMain.getInstance().getTextRespectUISPr("pers.hvmabenutzer") + "...");
		wbuBenutzer.setActionCommand(ACTION_SPECIAL_BENUTZER_FROM_LISTE);
		wbuBenutzer.addActionListener(this);

		getInternalFrame().addItemChangedListener(this);

		wtfBenutzer.setMandatoryField(true);
		wtfBenutzer.setActivatable(false);
		wtfBenutzer.setColumnsMax(100);
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wbuBenutzer, new GridBagConstraints(0, 0, 1, 1, 0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBenutzer, new GridBagConstraints(1, 0, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_HVMABENUTZER;
	}

}
