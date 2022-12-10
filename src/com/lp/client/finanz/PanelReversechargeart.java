/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2016 HELIUM V IT-Solutions GmbH
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
package com.lp.client.finanz;

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
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.finanz.service.ReversechargeartsprDto;

public class PanelReversechargeart extends PanelBasis {
	private static final long serialVersionUID = 1L;

	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperLabel wlaKennung = new WrapperLabel();
	private WrapperTextField wtfKennung = new WrapperTextField();
 	
	private final TabbedPaneFinanzamt tabbedPaneFinanzamt;
	private ReversechargeartDto reversechargeartDto = null ;
	
	public PanelReversechargeart(InternalFrame internalFrame, String add2TitleI,
			Object pk, TabbedPaneFinanzamt tabbedPaneFinanzamt) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		
		if(null == tabbedPaneFinanzamt) throw new IllegalArgumentException("tabbedPaneFinanzamt") ;		
		this.tabbedPaneFinanzamt = tabbedPaneFinanzamt;
		
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

		reversechargeartDto = new ReversechargeartDto() ;
		leereAlleFelder(this);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}
	
	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
	throws Throwable {
		if (bNeedNoUpdateI) {
			return;
		}
		int iLockstate = getLockedstateDetailMainKey().getIState();
		if (iLockstate == LOCK_IS_NOT_LOCKED
				||
				// in diesen Faellen ein echtes update zulassen
				iLockstate == LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY
				|| iLockstate == LOCK_ENABLE_REFRESHANDUPDATE_ONLY) {
			// MB: nocheinmal ein refresh. (der datensatz muss bereits gelockt
			// sein)
			// damit werden die aktuellen Daten angezeigt. Solange der Lock
			// besteht,
			// kann kein anderer User die Daten veraendern.
			eventYouAreSelected(false);
			/**
			 * @todo MB->MB eigentlich sollte erst nach dem Locken refresht
			 *       werden aber, dann funktionieren die PanelSplit-FLR's nicht
			 *       mehr richtig ... keine ahnung wieso
			 */

			eventActionLock(null);
			// Lockstate setzen und Buttons schalten.
			LockStateValue lockstateValue = getLockedstateDetailMainKey();
			lockstateValue.setIState(LOCK_IS_LOCKED_BY_ME);
			updateButtons(lockstateValue);
			setFirstFocusableComponentSaveOld();
		} else {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hint"),
					LPMain.getTextRespectUISPr("lp.system.locked.text"));

			LockStateValue lockstateValue = getLockedstateDetailMainKey();
			lockstateValue
			.setIState((iLockstate == LOCK_IS_LOCKED_BY_ME) ? LOCK_IS_LOCKED_BY_OTHER_USER
					: iLockstate);
			updateButtons(lockstateValue);
		}
		
		wtfKennung.setEditable(false);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
		} else {
			if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
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

		wtfBezeichnung.setMandatoryField(true);
		wtfBezeichnung.setColumnsMax(FinanzServiceFac.MAX_REVERSECHARGEART_BEZEICHNUNG);
		wtfBezeichnung.setToken("bezeichnung");
		wlaBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));

		wlaKennung.setText(LPMain.getTextRespectUISPr("label.kennung"));
		wtfKennung.setColumnsMax(15);
		wtfKennung.setToken("kennung");
		wtfKennung.setEditable(false);


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

		iZeile++;
		jpaWorkingOn.add(wlaKennung, new GridBagConstraints(0, iZeile, 1,
				1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, iZeile, 1,
				1, 2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, 
				ACTION_DELETE, ACTION_DISCARD };

		enableToolsPanelButtons(aWhichButtonIUse);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_REVERSECHARGEART;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getFinanzServiceDelegate()
				.removeReversechargeart(reversechargeartDto.getIId());
		setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable{
		reversechargeartDto.setCNr(wtfKennung.getText());
		reversechargeartDto.setMandantCNr(LPMain.getTheClient().getMandant());
		reversechargeartDto.setSprDto(new ReversechargeartsprDto());
		reversechargeartDto.getSprDto().setcBez(wtfBezeichnung.getText());
		
		setKeyWhenDetailPanel(reversechargeartDto.getIId());
	}

	protected void dto2Components() throws Throwable {
		wtfKennung.setText(reversechargeartDto.getCNr());
		wtfBezeichnung.setText(reversechargeartDto.getSprDto().getcBez());
		setKeyWhenDetailPanel(reversechargeartDto.getIId());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (reversechargeartDto.getIId() == null) {
				Integer newId = DelegateFactory.getInstance()
						.getFinanzServiceDelegate().createReversechargeart(reversechargeartDto);
				getFinanzController().setReversechargeartIId(newId);
				reversechargeartDto = getFinanzController().getReversechargeartDto() ;
//				reversechargeartDto.setIId(DelegateFactory.getInstance()
//						.getFinanzServiceDelegate().createReversechargeart(reversechargeartDto));
				setKeyWhenDetailPanel(reversechargeartDto.getIId());
			} else {
				DelegateFactory.getInstance().getFinanzServiceDelegate()
						.updateReversechargeart(reversechargeartDto);
			}
			super.eventActionSave(e, true);
			
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(reversechargeartDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}

	}

	private TabbedPaneFinanzamt getTabbedPaneFinanzamt() {
		return tabbedPaneFinanzamt;
	}
	
	private IFinanzamtController getFinanzController() {
		return getTabbedPaneFinanzamt().getFinanzController() ;
	}
	
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			reversechargeartDto = getFinanzController().getReversechargeartDto() ;
//			reversechargeartDto = DelegateFactory.getInstance().getFinanzServiceDelegate()
//					.reversechargeartFindByPrimaryKey((Integer) key);
			dto2Components();
			setKeyWhenDetailPanel(reversechargeartDto.getIId());
		}
		
		getInternalFrame().setLpTitle(
				InternalFrame.TITLE_IDX_AS_I_LIKE,
				getTabbedPaneFinanzamt().getFinanzamtTitle());	
	}
}
