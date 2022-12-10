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
package com.lp.client.angebot;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.angebot.service.AkquisestatusDto;
import com.lp.server.fertigung.service.ZusatzstatusDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>
 * <I>Dialog zur Eingabe der Akquisedaten fuer ein Angebot.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>14.07.05</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.4 $
 */
public class PanelDialogAngebotAkquisedaten extends PanelDialog {

	private WrapperLabel wlaNachfasstermin = null;
	private WrapperLabel wlaRealisierungstermin = null;
	private WrapperLabel wlaAuftragswahrscheinlichkeit = null;
	private WrapperLabel wlaProzent = null;
	private WrapperLabel wlaAblageort = null;

	protected WrapperDateField wdfNachfasstermin = null;
	protected WrapperDateField wdfRealisierungstermin = null;
	protected WrapperNumberField wnfAuftragswahrscheinlichkeit = null;

	protected WrapperTextField wtfAblageort = null;

	private WrapperLabel wlaKommentarIntern = null;
	private WrapperLabel wlaKommentarExtern = null;
	private WrapperEditorField wefKommentarIntern = null;
	private WrapperEditorField wefKommentarExtern = null;

	private WrapperButton wbuVertreter = new WrapperButton();
	private WrapperTextField wtfVetreter = new WrapperTextField();

	private WrapperButton wbuAkquisestatus = new WrapperButton();
	private WrapperTextField wtfAkquisestatus = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRVertreter = null;
	static final public String ACTION_SPECIAL_VERTRETER_FROM_LISTE = "action_vertreter_liste";
	private PanelQueryFLR panelQueryFLRAkquisestatus = null;
	static final public String ACTION_SPECIAL_AKQUISESTATUS_FROM_LISTE = "action_akquisestatus_liste";

	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAngebot intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAngebot tpAngebot = null;

	static final public String ACTION_SPECIAL_SAVE = "action_" + ALWAYSENABLED + "save";

	/** Wenn der Dialog von aussen geoeffnet wird, muss ein Lock gesetzt werden. */
	private LockMeDto lockMeAngebot = null;

	public PanelDialogAngebotAkquisedaten(InternalFrame internalFrame, String add2TitleI) throws Throwable {
		super(internalFrame, add2TitleI);

		intFrame = (InternalFrameAngebot) getInternalFrame();
		tpAngebot = intFrame.getTabbedPaneAngebot();

		jbInit();
		setDefaults();
		initComponents();
		dto2Components();
	}

	private void dialogQueryVertreter(ActionEvent e) throws Throwable {
		panelQueryFLRVertreter = PersonalFilterFactory.getInstance().createPanelFLRPersonal(intFrame, true, false,
				tpAngebot.getAngebotDto().getPersonalIIdVertreter());

		new DialogQuery(panelQueryFLRVertreter);
	}

	private void dialogQueryAqkuisestatus(ActionEvent e) throws Throwable {
		
		String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };
		
		panelQueryFLRAkquisestatus = new PanelQueryFLR(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
				QueryParameters.UC_ID_AKQUISESTATUS, aWhichButtonIUse, getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("angb.akquisestatus"));

		panelQueryFLRAkquisestatus
				.befuellePanelFilterkriterienDirekt(SystemFilterFactory.getInstance().createFKDBezeichnung(), null);
		panelQueryFLRAkquisestatus.setSelectedId(tpAngebot.getAngebotDto().getAkquisestatusIId());
		new DialogQuery(panelQueryFLRAkquisestatus);

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRVertreter) {
				Integer iIdPersonal = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				PersonalDto vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
						.personalFindByPrimaryKey(iIdPersonal);

				wtfVetreter.setText(vertreterDto.getPartnerDto().formatTitelAnrede());

				tpAngebot.getAngebotDto().setPersonalIIdVertreter(iIdPersonal);

			} else if (e.getSource() == panelQueryFLRAkquisestatus) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				AkquisestatusDto akquisestatusDto = DelegateFactory.getInstance().getAngebotServiceDelegate()
						.akquisestatusFindByPrimaryKey((Integer) key);

				wtfAkquisestatus.setText(akquisestatusDto.getCBez());
				tpAngebot.getAngebotDto().setAkquisestatusIId(akquisestatusDto.getIId());
			}
		}else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			// source ist MEIN Ansprechpartnerdialog
			if (e.getSource() == panelQueryFLRAkquisestatus) {
				tpAngebot.getAngebotDto().setAkquisestatusIId(null);
				wtfAkquisestatus.setText("");
			}
		}	
	}

	private void jbInit() throws Throwable {
		createAndSaveAndShowButton("/com/lp/client/res/disk_blue.png",
				LPMain.getInstance().getTextRespectUISPr("lp.tooltip.kriterienuebernehmen"), ACTION_SPECIAL_SAVE, null);

		String[] aWhichButtonIUse = { ACTION_SPECIAL_SAVE };

		enableButtonAction(aWhichButtonIUse);
		LockStateValue lockstateValue = new LockStateValue(null, null, PanelBasis.LOCK_NO_LOCKING);
		updateButtons(lockstateValue);

		if (tpAngebot.getAngebotDto() != null) {
			lockMeAngebot = new LockMeDto(HelperClient.LOCKME_ANGEBOT, tpAngebot.getAngebotDto().getIId() + "",
					LPMain.getInstance().getCNrUser());
		}

		// explizit ausloesen, weil weder new noch update aufgerufen werden
		eventActionLock(null);
	}

	protected void setDefaults() throws Throwable {
		getInternalFrame().addItemChangedListener(this);
		wlaNachfasstermin = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("angb.nachfasstermin"));
		HelperClient.setDefaultsToComponent(wlaNachfasstermin, 150);
		wlaRealisierungstermin = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("angb.realisierungstermin"));
		wlaAuftragswahrscheinlichkeit = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr("angb.auftragswahrscheinlichkeit"));
		wlaProzent = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("label.prozent"));
		HelperClient.setDefaultsToComponent(wlaProzent, 150);
		wlaProzent.setHorizontalAlignment(SwingConstants.LEADING);
		wlaAblageort = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("angb.ablageort"));
		wtfAblageort = new WrapperTextField();
		wtfAblageort.setColumnsMax(40);

		wlaKommentarIntern = new WrapperLabel(LPMain.getTextRespectUISPr("lp.internerkommentar"));
		wlaKommentarExtern = new WrapperLabel(LPMain.getTextRespectUISPr("lp.externerkommentar"));

		wefKommentarIntern = new WrapperEditorFieldKommentar(getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.kommentar"));
		wefKommentarExtern = new WrapperEditorFieldKommentar(getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.kommentar"));

		wbuVertreter.setMandatoryField(true);

		wdfNachfasstermin = new WrapperDateField();
		wdfNachfasstermin.setMandatoryField(true);
		wdfRealisierungstermin = new WrapperDateField();
		wnfAuftragswahrscheinlichkeit = new WrapperNumberField();
		wnfAuftragswahrscheinlichkeit.setMaximumValue(100);
		HelperClient.setDefaultsToComponent(wnfAuftragswahrscheinlichkeit, 100);
		wnfAuftragswahrscheinlichkeit.setMandatoryField(true);
		HelperClient.setMinimumAndPreferredSize(wlaAuftragswahrscheinlichkeit,
				HelperClient.getSizeFactoredDimension(160));

		wbuVertreter = new WrapperButton();
		wbuVertreter.setText(LPMain.getInstance().getTextRespectUISPr("button.vertreter"));
		wbuVertreter.setActionCommand(ACTION_SPECIAL_VERTRETER_FROM_LISTE);
		wbuVertreter.addActionListener(this);

		wtfVetreter = new WrapperTextField();
		wtfVetreter.setMandatoryField(true);
		wtfVetreter.setActivatable(false);
		wtfVetreter.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuAkquisestatus = new WrapperButton();
		wbuAkquisestatus.setText(LPMain.getInstance().getTextRespectUISPr("angb.akquisestatus")+"...");
		wbuAkquisestatus.setActionCommand(ACTION_SPECIAL_AKQUISESTATUS_FROM_LISTE);
		wbuAkquisestatus.addActionListener(this);

		wtfAkquisestatus = new WrapperTextField();
		wtfAkquisestatus.setActivatable(false);
		wtfAkquisestatus.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		int iZeileI = 0;

		jpaWorkingOn.add(wlaNachfasstermin, new GridBagConstraints(0, iZeileI, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfNachfasstermin, new GridBagConstraints(1, iZeileI, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeileI++;
		jpaWorkingOn.add(wlaRealisierungstermin, new GridBagConstraints(0, iZeileI, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfRealisierungstermin, new GridBagConstraints(1, iZeileI, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeileI++;
		jpaWorkingOn.add(wlaAuftragswahrscheinlichkeit, new GridBagConstraints(0, iZeileI, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfAuftragswahrscheinlichkeit, new GridBagConstraints(1, iZeileI, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaProzent, new GridBagConstraints(2, iZeileI, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuAkquisestatus, new GridBagConstraints(3, iZeileI, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAkquisestatus, new GridBagConstraints(4, iZeileI, 1, 1, 2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeileI++;
		jpaWorkingOn.add(wlaAblageort, new GridBagConstraints(0, iZeileI, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfAblageort, new GridBagConstraints(1, iZeileI, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeileI++;
		jpaWorkingOn.add(wbuVertreter, new GridBagConstraints(0, iZeileI, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfVetreter, new GridBagConstraints(1, iZeileI, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeileI++;
		jpaWorkingOn.add(wlaKommentarIntern, new GridBagConstraints(0, iZeileI, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wefKommentarIntern, new GridBagConstraints(1, iZeileI, 5, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeileI++;
		jpaWorkingOn.add(wlaKommentarExtern, new GridBagConstraints(0, iZeileI, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wefKommentarExtern, new GridBagConstraints(1, iZeileI, 5, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

	}

	private void dto2Components() throws Throwable {
		wdfNachfasstermin.setTimestamp(tpAngebot.getAngebotDto().getTNachfasstermin());
		wdfRealisierungstermin.setTimestamp(tpAngebot.getAngebotDto().getTRealisierungstermin());
		wnfAuftragswahrscheinlichkeit.setDouble(tpAngebot.getAngebotDto().getFAuftragswahrscheinlichkeit());
		wtfAblageort.setText(tpAngebot.getAngebotDto().getXAblageort());
		wefKommentarIntern.setText(tpAngebot.getAngebotDto().getXInternerkommentar());
		wefKommentarExtern.setText(tpAngebot.getAngebotDto().getXExternerkommentar());

		if (tpAngebot.getAngebotDto().getPersonalIIdVertreter() != null) {
			PersonalDto vertreterDto = DelegateFactory.getInstance().getPersonalDelegate()
					.personalFindByPrimaryKey(tpAngebot.getAngebotDto().getPersonalIIdVertreter());
			wtfVetreter.setText(vertreterDto.getPartnerDto().formatTitelAnrede());
		}

		if (tpAngebot.getAngebotDto().getAkquisestatusIId() != null) {

			AkquisestatusDto akquisestatusDto = DelegateFactory.getInstance().getAngebotServiceDelegate()
					.akquisestatusFindByPrimaryKey(tpAngebot.getAngebotDto().getAkquisestatusIId());

			wtfAkquisestatus.setText(akquisestatusDto.getCBez());

		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG) || e.getActionCommand().equals(ESC)) {
			// explizit aufrufen, weil weder save noch discard aufgerufen werden
			eventActionUnlock(null);
			getInternalFrame().closePanelDialog();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SAVE)) {
			if (lockMeAngebot != null) {
				checkLockedDlg(lockMeAngebot);
			}

			// die Akquisedaten ohne Statuswechsel abspeichern
			tpAngebot.getAngebotDto().setTNachfasstermin(wdfNachfasstermin.getTimestamp());
			tpAngebot.getAngebotDto().setTRealisierungstermin(wdfRealisierungstermin.getTimestamp());
			tpAngebot.getAngebotDto().setFAuftragswahrscheinlichkeit(wnfAuftragswahrscheinlichkeit.getDouble());
			tpAngebot.getAngebotDto().setXAblageort(wtfAblageort.getText());
			tpAngebot.getAngebotDto().setXInternerkommentar(wefKommentarIntern.getText());
			tpAngebot.getAngebotDto().setXExternerkommentar(wefKommentarExtern.getText());

			DelegateFactory.getInstance().getAngebotDelegate()
					.updateAngebotOhneWeitereAktion(tpAngebot.getAngebotDto());

			// explizit aufrufen, weil weder save noch discard aufgerufen werden
			eventActionUnlock(null);
			getInternalFrame().closePanelDialog();

			// sonst sind die eben gespeicherten Daten in den Konditionen noch
			// nicht sichtbar
			if (tpAngebot.getSelectedIndex() == TabbedPaneAngebot.IDX_PANEL_KONDITIONEN) {
				tpAngebot.refreshAktuellesPanel();
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_VERTRETER_FROM_LISTE)) {
			dialogQueryVertreter(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_AKQUISESTATUS_FROM_LISTE)) {
			dialogQueryAqkuisestatus(e);
		}
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_ANGEBOT;
	}

	protected void eventActionLock(ActionEvent e) throws Throwable {
		if (lockMeAngebot != null) {
			// Zugehoeriges Angebot locken.
			super.lock(lockMeAngebot);
		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {
		if (lockMeAngebot != null) {
			// Zugehoeriges Angebot locken.
			super.unlock(lockMeAngebot);
		}
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue lockstateValue = super.getLockedstateDetailMainKey();
		if (lockstateValue.getIState() == LOCK_IS_NOT_LOCKED && lockMeAngebot != null) {
			int iLockstate = getLockedByWerWas(lockMeAngebot);

			if (iLockstate == LOCK_IS_LOCKED_BY_ME) {
				iLockstate = LOCK_IS_LOCKED_BY_OTHER_USER; // anderes Modul hat
															// gelock zB Partner
			}

			lockstateValue.setIState(iLockstate);
		}

		return lockstateValue;
	}
}
