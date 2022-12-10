
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
package com.lp.client.rechnung;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKommentar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.ProjektzeitenDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.system.service.LockMeDto;

/**
 * <p>
 * <I>Dialog zur Eingabe des internen Kommentars fuer eins St&uuml;ckliste.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>30.09.05</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Christian Kollmann
 * @version $Revision: 1.3 $
 */
public class PanelDialogZeitinfoKommentar extends PanelDialogKommentar {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Cache for convenience. */
	private InternalFrameRechnung intFrame = null;

	/** Wenn der Dialog von aussen geoeffnet wird, muss ein Lock gesetzt werden. */
	private LockMeDto lockMeZeitdaten = null;

	/** Der Kommentar kann intern oder extern sein. */
	private boolean bInternerKommentar = false;

	private ProjektzeitenDto projektzeitenDto = null;

	private ZeitdatenDto zeitdatenDto = null;
	private TelefonzeitenDto telefonzeitenDto = null;

	public PanelDialogZeitinfoKommentar(InternalFrame internalFrame, ProjektzeitenDto projektzeitenDto,
			String add2TitleI, boolean bInternerKommentarI) throws Throwable {
		super(internalFrame, add2TitleI);
		this.projektzeitenDto = projektzeitenDto;
		intFrame = (InternalFrameRechnung) getInternalFrame();
		bInternerKommentar = bInternerKommentarI;

		jbInit();
		initComponents();
		dto2Components();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}

	private void jbInit() throws Throwable {
		if (projektzeitenDto != null) {

			if (projektzeitenDto.getZeitdatenIId() != null) {
				zeitdatenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.zeitdatenFindByPrimaryKey(projektzeitenDto.getZeitdatenIId());

				lockMeZeitdaten = new LockMeDto(HelperClient.LOCKME_ZEITDATEN, zeitdatenDto.getIId() + "",
						LPMain.getInstance().getCNrUser());
			} else if (projektzeitenDto.getTelefonzeitenIId() != null) {
				telefonzeitenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.telefonzeitenFindByPrimaryKey(projektzeitenDto.getTelefonzeitenIId());

				lockMeZeitdaten = new LockMeDto(HelperClient.LOCKME_TELEFONZEITEN, telefonzeitenDto.getIId() + "",
						LPMain.getInstance().getCNrUser());
			}
		}

		eventActionLock(null);
	}

	private void dto2Components() throws Throwable {

		if (this.bInternerKommentar) {

			if (zeitdatenDto != null) {
				getLpEditor().setText(zeitdatenDto.getXKommentarIntern());
				setCBestehenderKommentar(zeitdatenDto.getXKommentarIntern());
			} else if (telefonzeitenDto != null) {
				getLpEditor().setText(telefonzeitenDto.getXKommentarint());
				setCBestehenderKommentar(telefonzeitenDto.getXKommentarint());
			}
		} else {
			if (zeitdatenDto != null) {
				getLpEditor().setText(zeitdatenDto.getXKommentar());
				setCBestehenderKommentar(zeitdatenDto.getXKommentar());
			} else if (telefonzeitenDto != null) {
				getLpEditor().setText(telefonzeitenDto.getXKommentarext());
				setCBestehenderKommentar(telefonzeitenDto.getXKommentarext());
			}
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (super.bHandleEventInSuperklasse) {
			// explizit aufrufen, weil weder save noch discard aufgerufen werden
			eventActionUnlock(null);
		}
	}

	/**
	 * Den Kommentar im Angebot abspeichern.
	 * 
	 * @throws Throwable Ausnahme
	 */
	public void saveKommentar() throws Throwable {
		if (lockMeZeitdaten != null) {
			checkLockedDlg(lockMeZeitdaten);
		}

		if (this.bInternerKommentar) {

			if (zeitdatenDto != null) {
				zeitdatenDto.setXKommentarIntern(getLpEditor().getText());
			} else if (telefonzeitenDto != null) {
				telefonzeitenDto.setXKommentarint(getLpEditor().getText());
			}

		} else {
			if (zeitdatenDto != null) {
				zeitdatenDto.setXKommentar(getLpEditor().getText());
			} else if (telefonzeitenDto != null) {
				telefonzeitenDto.setXKommentarext(getLpEditor().getText());
			}
		}
		if (zeitdatenDto != null) {
			DelegateFactory.getInstance().getZeiterfassungDelegate().updateZeitdaten(zeitdatenDto);
		} else if (telefonzeitenDto != null) {
			DelegateFactory.getInstance().getZeiterfassungDelegate().updateTelefonzeiten(telefonzeitenDto);
		}
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_ZEITDATEN;
	}

	protected void eventActionLock(ActionEvent e) throws Throwable {
		if (lockMeZeitdaten != null) {
			// Zugehoeriges Angebot locken.
			super.lock(lockMeZeitdaten);
		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {
		if (lockMeZeitdaten != null) {
			// Zugehoeriges Angebot locken.
			super.unlock(lockMeZeitdaten);
		}
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue lockstateValue = super.getLockedstateDetailMainKey();
		if (lockstateValue.getIState() == LOCK_IS_NOT_LOCKED && lockMeZeitdaten != null) {
			int iLockstate = getLockedByWerWas(lockMeZeitdaten);

			if (iLockstate == LOCK_IS_LOCKED_BY_ME) {
				iLockstate = LOCK_IS_LOCKED_BY_OTHER_USER; // anderes Modul hat gelock zB Partner
			}

			lockstateValue.setIState(iLockstate);
		}

		return lockstateValue;
	}
}
