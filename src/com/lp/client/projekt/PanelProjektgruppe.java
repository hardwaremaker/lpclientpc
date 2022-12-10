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
package com.lp.client.projekt;

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
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.projekt.service.ProjektgruppeDto;


/*
 * <p>Panel zum Bearbeiten der Klassen eines Loses</p> <p>Copyright Logistik Pur
 * Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum <I>24. 10. 2005</I></p>
 * <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.4 $
 */
public class PanelProjektgruppe extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneProjekt tabbedPaneProjekt = null;

	private ProjektgruppeDto projektgruppeDto = null;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();
	private Border border1;

	private WrapperSelectField wsfProjektVater = new WrapperSelectField(
			WrapperSelectField.PROJEKT, getInternalFrame(), false);
	private WrapperSelectField wsfProjektKind = new WrapperSelectField(
			WrapperSelectField.PROJEKT, getInternalFrame(), false);

	public PanelProjektgruppe(InternalFrame internalFrame, String add2TitleI,
			Object key, TabbedPaneProjekt tabbedPaneLos) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneProjekt = tabbedPaneLos;
		jbInit();
		initComponents();
	}

	private TabbedPaneProjekt getTabbedPaneProjekt() {
		return tabbedPaneProjekt;
	}

	private void jbInit() throws Throwable {
		String[] aWhichButtonIUse = null;

		aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };

		this.enableToolsPanelButtons(aWhichButtonIUse);

		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setLayout(gridBagLayout1);

		jpaWorkingOn.setBorder(border1);
		jpaWorkingOn.setLayout(gridBagLayout3);
		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);

		// controls

		wsfProjektVater.setMandatoryField(true);
		wsfProjektKind.setMandatoryField(true);

		wsfProjektVater.setText(LPMain
				.getTextRespectUISPr("proj.uebergeordnetesprojekt"));

		wsfProjektKind.setText(LPMain
				.getTextRespectUISPr("proj.untergeordnetesprojekt"));

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		// statusbarneu: 1 an den unteren rand des panels haengen
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfProjektVater.getWrapperGotoButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 150, 0));
		jpaWorkingOn.add(wsfProjektVater.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfProjektKind.getWrapperGotoButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfProjektKind.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (projektgruppeDto != null) {

				if (projektgruppeDto.getIId() == null) {
					projektgruppeDto.setIId(DelegateFactory.getInstance()
							.getProjektServiceDelegate()
							.createProjektgruppe(projektgruppeDto));
				} else {
					DelegateFactory.getInstance().getProjektServiceDelegate()
							.updateProjektgruppe(projektgruppeDto);
				}

				setKeyWhenDetailPanel(projektgruppeDto.getIId());
				super.eventActionSave(e, true);
				// jetz den anzeigen
				eventYouAreSelected(false);
			}
		}
	}

	/**
	 * Die eingegebenen Daten in ein Dto schreiben
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {
		projektgruppeDto.setProjektIIdKind(wsfProjektKind.getIKey());
		projektgruppeDto.setProjektIIdVater(wsfProjektVater.getIKey());
	}

	/**
	 * Ein Dto-Objekt ins Panel uebertragen
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		if (projektgruppeDto != null) {

			wsfProjektKind.setKey(projektgruppeDto.getProjektIIdKind());
			wsfProjektVater.setKey(projektgruppeDto.getProjektIIdVater());
		}
	}

	public void eventActionNew(EventObject eventObject,
			boolean bChangeKeyLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, false, false);
		projektgruppeDto = new ProjektgruppeDto();
		this.leereAlleFelder(this);
	}

	/**
	 * Stornieren einer Rechnung bzw Gutschrift
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bAdministrateLockKeyI
	 *            boolean
	 * @param bNeedNoDeleteI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (this.projektgruppeDto != null) {
			if (projektgruppeDto.getIId() != null) {
				if (!isLockedDlg()) {
					DelegateFactory.getInstance().getProjektServiceDelegate()
							.removeProjektgruppe(projektgruppeDto);
					this.projektgruppeDto = null;
					this.leereAlleFelder(this);
					super.eventActionDelete(e, false, false);
				}
			}
		}
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_PROJEKT;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	private InternalFrameProjekt getInternalFrameProjekt() {
		return (InternalFrameProjekt) getInternalFrame();
	}

	/**
	 * eventItemchanged.
	 * 
	 * @param eI
	 *            EventObject
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource().equals(wsfProjektVater.getPanelQueryFLR())) {
				wsfProjektKind.setKey(getTabbedPaneProjekt().getProjektDto()
						.getIId());
			} else if (e.getSource().equals(wsfProjektKind.getPanelQueryFLR())) {
				wsfProjektVater.setKey(getTabbedPaneProjekt().getProjektDto()
						.getIId());
			}

		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		if (!bNeedNoYouAreSelectedI) {
			Object key = getKeyWhenDetailPanel();
			if (key == null
					|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
				// einen neuen Eintrag anlegen oder die letzte Position wurde
				// geloescht.
				leereAlleFelder(this);
				clearStatusbar();
			} else {
				// einen alten Eintrag laden.
				projektgruppeDto = DelegateFactory.getInstance()
						.getProjektServiceDelegate()
						.projektgruppeFindByPrimaryKey((Integer) key);
				dto2Components();
			}
		}

		getTabbedPaneProjekt().setTitleProjekt(
				LPMain.getTextRespectUISPr("proj.verknuepfung"));

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
	}

	public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
		return false;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wsfProjektVater;
	}
}
