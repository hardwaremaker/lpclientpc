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
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.server.personal.service.MaschinengruppeDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;

public class PanelMaschinengruppe extends PanelBasis {

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
	private WrapperLabel wlaKurzbezeichnung = new WrapperLabel();
	private WrapperTextField wtfKurzbezeichnung = new WrapperTextField();
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();

	private WrapperCheckBox wcbInAuslastungsanzeige = new WrapperCheckBox();

	private MaschinengruppeDto maschinengruppeDto = null;

	private WrapperTextField wtfFertigungsgruppe = null;
	private WrapperButton wbuFertigungsgruppe = null;

	static final public String ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE = "action_special_fertigungsgruppe_from_liste";

	private PanelQueryFLR panelQueryFLRFertigungsgruppe = null;

	public PanelMaschinengruppe(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		maschinengruppeDto = new MaschinengruppeDto();
		// getInternalFrame().setKeyWasForLockMe(LPMain.getLockMeForNew());

		leereAlleFelder(this);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE)) {
			dialogQueryFertigungsgruppeFromListe(e);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getZeiterfassungDelegate()
				.removeMaschinengruppe(maschinengruppeDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	private void dialogQueryFertigungsgruppeFromListe(ActionEvent e)
			throws Throwable {
		panelQueryFLRFertigungsgruppe = StuecklisteFilterFactory.getInstance()
				.createPanelFLRFertigungsgruppe(getInternalFrame(),
						maschinengruppeDto.getFertigungsgruppeIId(), false);
		new DialogQuery(panelQueryFLRFertigungsgruppe);
	}

	protected void components2Dto() {
		maschinengruppeDto.setCKbez(wtfKurzbezeichnung.getText());
		maschinengruppeDto.setCBez(wtfBezeichnung.getText());
		maschinengruppeDto.setBAuslastungsanzeige(wcbInAuslastungsanzeige
				.getShort());

	}

	protected void dto2Components() throws Throwable {
		wtfKurzbezeichnung.setText(maschinengruppeDto.getCKbez());
		wtfBezeichnung.setText(maschinengruppeDto.getCBez());
		wcbInAuslastungsanzeige.setShort(maschinengruppeDto
				.getBAuslastungsanzeige());

		FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory
				.getInstance()
				.getStuecklisteDelegate()
				.fertigungsgruppeFindByPrimaryKey(
						maschinengruppeDto.getFertigungsgruppeIId());
		wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (maschinengruppeDto.getIId() == null) {
				maschinengruppeDto.setIId(DelegateFactory.getInstance()
						.getZeiterfassungDelegate()
						.createMaschinengruppe(maschinengruppeDto));
				setKeyWhenDetailPanel(maschinengruppeDto.getIId());
			} else {
				DelegateFactory.getInstance().getZeiterfassungDelegate()
						.updateMaschinengruppe(maschinengruppeDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						maschinengruppeDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRFertigungsgruppe) {
				Object oKey = panelQueryFLRFertigungsgruppe.getSelectedId();

				FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory
						.getInstance().getStuecklisteDelegate()
						.fertigungsgruppeFindByPrimaryKey((Integer) oKey);
				wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());

				maschinengruppeDto.setFertigungsgruppeIId(fertigungsgruppeDto
						.getIId());

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

		wcbInAuslastungsanzeige
				.setText(LPMain
						.getTextRespectUISPr("pers.maschinengruppe.inauslastungsanzeige"));

		wbuFertigungsgruppe = new WrapperButton();
		wbuFertigungsgruppe.setText(LPMain
				.getTextRespectUISPr("stkl.fertigungsgruppe") + "...");

		wbuFertigungsgruppe
				.setActionCommand(ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE);
		wbuFertigungsgruppe.addActionListener(this);
		wtfFertigungsgruppe = new WrapperTextField();
		wtfFertigungsgruppe.setActivatable(false);
		wtfFertigungsgruppe.setMandatoryField(true);

		wlaKurzbezeichnung.setText(LPMain
				.getTextRespectUISPr("maschine.kurzbez"));
		wtfKurzbezeichnung.setColumnsMax(2);
		wtfKurzbezeichnung.setText("");
		wtfKurzbezeichnung.setMandatoryField(true);

		wlaBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wtfBezeichnung
				.setColumnsMax(PersonalFac.MAX_PENDLERPAUSCHALE_BEZEICHNUNG);
		wtfBezeichnung.setText("");
		wtfBezeichnung.setMandatoryField(true);
		getInternalFrame().addItemChangedListener(this);
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

		iZeile++;

		jpaWorkingOn.add(wlaKurzbezeichnung, new GridBagConstraints(0, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKurzbezeichnung, new GridBagConstraints(1, iZeile,
				1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 1,
				1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wbuFertigungsgruppe, new GridBagConstraints(0, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFertigungsgruppe, new GridBagConstraints(1, iZeile,
				1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wcbInAuslastungsanzeige, new GridBagConstraints(1,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MASCHINENGRUPPE;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();
		} else {
			maschinengruppeDto = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.maschinengruppeFindByPrimaryKey((Integer) key);

			dto2Components();
		}
	}
}
