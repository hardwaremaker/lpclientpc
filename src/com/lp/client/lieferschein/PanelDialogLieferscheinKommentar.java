package com.lp.client.lieferschein;

import java.awt.event.ActionEvent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKommentar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LockMeDto;

/**
 * <p>
 * <I>Dialog zur Eingabe des internen Kommentars fuer einen Auftrag.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>29.03.06</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class PanelDialogLieferscheinKommentar extends PanelDialogKommentar {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameLieferschein intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneLieferschein tpLieferschein = null;

	/** Wenn der Dialog von aussen geoeffnet wird, muss ein Lock gesetzt werden. */
	private LockMeDto lockMeLieferschein = null;

	/** Der Kommentar kann intern oder extern sein. */
	private boolean bInternerKommentar = false;

	public PanelDialogLieferscheinKommentar(InternalFrame internalFrame, String add2TitleI, boolean bInternerKommentarI)
			throws Throwable {
		super(internalFrame, add2TitleI);

		intFrame = (InternalFrameLieferschein) getInternalFrame();
		tpLieferschein = intFrame.getTabbedPaneLieferschein();
		bInternerKommentar = bInternerKommentarI;

		jbInit();
		initComponents();
		dto2Components();
	}

	private void jbInit() throws Throwable {
		if (tpLieferschein.getLieferscheinDto() != null) {
			lockMeLieferschein = new LockMeDto(HelperClient.LOCKME_LIEFERSCHEIN,
					tpLieferschein.getLieferscheinDto().getIId() + "", LPMain.getInstance().getCNrUser());
		}

		// explizit ausloesen, weil weder new noch update aufgerufen werden
		eventActionLock(null);
	}

	private void dto2Components() throws Throwable {
		if (bInternerKommentar) {
			getLpEditor().setText(tpLieferschein.getLieferscheinDto().getXInternerkommentar());
			setCBestehenderKommentar(tpLieferschein.getLieferscheinDto().getXInternerkommentar());
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (super.bHandleEventInSuperklasse) {
			// explizit aufrufen, weil weder save noch discard aufgerufen werden
			eventActionUnlock(null);
			tpLieferschein.refreshAktuellesPanel(); // den Titel des aktuellen Panels setzen
		}
	}

	/**
	 * Den Kommentar im Auftrag abspeichern.
	 * 
	 * @throws Throwable Ausnahme
	 */
	public void saveKommentar() throws Throwable {
		if (lockMeLieferschein != null) {
			checkLockedDlg(lockMeLieferschein);
		}

		if (this.bInternerKommentar) {
			tpLieferschein.getLieferscheinDto().setXInternerkommentar(getLpEditor().getText());
		}

		DelegateFactory.getInstance().getLsDelegate()
				.updateLieferscheinOhneWeitereAktion(tpLieferschein.getLieferscheinDto());
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_AUFTRAG;
	}

	protected void eventActionLock(ActionEvent e) throws Throwable {
		if (lockMeLieferschein != null) {
			// Zugehoeriges Auftrag locken.
			super.lock(lockMeLieferschein);
		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {
		if (lockMeLieferschein != null) {
			// Zugehoeriges Auftrag locken.
			super.unlock(lockMeLieferschein);
		}
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue lockstateValue = super.getLockedstateDetailMainKey();
		if (lockstateValue.getIState() == LOCK_IS_NOT_LOCKED && lockMeLieferschein != null) {
			int iLockstate = getLockedByWerWas(lockMeLieferschein);

			if (iLockstate == LOCK_IS_LOCKED_BY_ME) {
				iLockstate = LOCK_IS_LOCKED_BY_OTHER_USER; // anderes Modul hat gelock zB Partner
			}

			lockstateValue.setIState(iLockstate);
		}

		return lockstateValue;
	}
}
