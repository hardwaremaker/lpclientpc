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
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.zutritt.DialogReadFingerprint;
import com.lp.server.personal.service.FingerartDto;
import com.lp.server.personal.service.PersonalfingerDto;
import com.lp.server.personal.service.PersonalterminalDto;
import com.lp.server.system.service.ArbeitsplatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelPersonalterminal extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private WrapperButton wbuArbeitsplatz = new WrapperButton();
	private WrapperTextField wtfArbeitsplatz = new WrapperTextField();
	private PanelQueryFLR panelQueryFLRArbeitsplatz = null;

	static final public String ACTION_SPECIAL_ARBEITSPLATZ_FROM_LISTE = "ACTION_SPECIAL_ARBEITSPLATZ_FROM_LISTE";

	private PersonalterminalDto personalterminalDto = null;

	public PanelPersonalterminal(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfArbeitsplatz;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		personalterminalDto = new PersonalterminalDto();
		leereAlleFelder(this);
	}

	/**
	 * Hier kommen die events meiner speziellen Buttons an.
	 * 
	 * @param e ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_ARBEITSPLATZ_FROM_LISTE)) {
			dialogQueryArbeitsplatzFromListe(e);
		}
	}

	void dialogQueryArbeitsplatzFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };

		 FilterKriterium[] filtersI=new FilterKriterium[] { new FilterKriterium("c_typ", true,
				"'"+ ParameterFac.ARBEITSPLATZ_TYP_WIN_TERMINAL + "'", FilterKriterium.OPERATOR_EQUAL, false)};
		
		
		
		panelQueryFLRArbeitsplatz = new PanelQueryFLR(null, filtersI, QueryParameters.UC_ID_ARBEITSPLATZ, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("system.pcname"));
		panelQueryFLRArbeitsplatz
				.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);
		panelQueryFLRArbeitsplatz.setSelectedId(personalterminalDto.getArbeitsplatzIId());

		new DialogQuery(panelQueryFLRArbeitsplatz);
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getPersonalDelegate().removePersonalterminal(personalterminalDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() {
		personalterminalDto.setPersonalIId(((InternalFramePersonal) getInternalFrame()).getPersonalDto().getIId());

	}

	protected void dto2Components() throws Throwable {

		ArbeitsplatzDto apDto = DelegateFactory.getInstance().getParameterDelegate()
				.arbeitsplatzFindByPrimaryKey(personalterminalDto.getArbeitsplatzIId());

		wtfArbeitsplatz.setText(apDto.getCPcname());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (personalterminalDto.getIId() == null) {
				personalterminalDto.setIId(DelegateFactory.getInstance().getPersonalDelegate()
						.createPersonalterminal(personalterminalDto));
				setKeyWhenDetailPanel(personalterminalDto.getIId());
			} else {
				DelegateFactory.getInstance().getPersonalDelegate().updatePersonalterminal(personalterminalDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(personalterminalDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRArbeitsplatz) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArbeitsplatzDto apDto = DelegateFactory.getInstance().getParameterDelegate()
						.arbeitsplatzFindByPrimaryKey((Integer) key);
				wtfArbeitsplatz.setText(apDto.getCPcname());
				personalterminalDto.setArbeitsplatzIId(apDto.getIId());
			}
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

		wbuArbeitsplatz.setText(LPMain.getInstance().getTextRespectUISPr("pers.personal.erlaubteterminals.arbeitsplatz") + "...");
		wbuArbeitsplatz.setActionCommand(ACTION_SPECIAL_ARBEITSPLATZ_FROM_LISTE);
		wbuArbeitsplatz.addActionListener(this);

		wtfArbeitsplatz.setMandatoryField(true);
		wtfArbeitsplatz.setActivatable(false);

		getInternalFrame().addItemChangedListener(this);
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

		jpaWorkingOn.add(wbuArbeitsplatz, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArbeitsplatz, new GridBagConstraints(1, 1, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PERSONAL;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();
		} else {
			personalterminalDto = DelegateFactory.getInstance().getPersonalDelegate()
					.personalterminalFindByPrimaryKey((Integer) key);

			dto2Components();

		}
	}

}
