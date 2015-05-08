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
 *******************************************************************************/
package com.lp.client.frame.editor;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.WrapperHtmlField;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.editor.util.TextBlockOverflowException;


public class PanelHtmlEditor extends PanelDialog {
	private static final long serialVersionUID = -8875580648783603955L;
	static final public String ACTION_SPECIAL_SAVE = "action_special_"
			+ ALWAYSENABLED + "accept";

	private WrapperHtmlField targetComponent = null;
	private WrapperHtmlField editField = null ;
	private LockStateValue lockstateValue = null;
	private String sLastSavedText = null;

	public PanelHtmlEditor(InternalFrame internalFrame,
			WrapperHtmlField targetComponent, String addTitleI, String text,
			int iLockState) throws Throwable {
		super(internalFrame, addTitleI) ;
		this.targetComponent = targetComponent;
		this.sLastSavedText = text;

		editField = new WrapperHtmlField(internalFrame, addTitleI, true) ;
		editField.setText(text) ;
		
		lockstateValue = new LockStateValue(null, null, iLockState);
		jbInitPanel();
		initComponents();

		updateButtons(lockstateValue);
		
		editField.startEditing();
	}

	private void jbInitPanel() throws Throwable {
		// Actionpanel von Oberklasse holen und anhaengen.
		createAndSaveButton(
				"/com/lp/client/res/disk_blue.png",
				LPMain.getTextRespectUISPr("lp.save"),
				ACTION_SPECIAL_SAVE,
				KeyStroke
						.getKeyStroke('S', java.awt.event.InputEvent.CTRL_MASK),
				null);
		if (lockstateValue.getIState() == LOCK_NO_LOCKING
				|| lockstateValue.getIState() == LOCK_IS_LOCKED_BY_ME) {
			String[] aWhichButtonIUse = { ACTION_SPECIAL_SAVE };
			enableToolsPanelButtons(aWhichButtonIUse);
		}
		jpaWorkingOn.add(editField, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
	}
	
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		String sNewText = null;
		try {
			sNewText = editField.getText();
		} catch (TextBlockOverflowException ex) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.error.editor.laengeueberschritten"));
			return;
		}

		// Event zum Schliessen schon hier fangen, falls der text nicht
		// gespeichert wurde
		if (e.getActionCommand().equals(PanelBasis.ESC)
				|| e.getActionCommand()
						.equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
			boolean bAskBeforeExit = false;
			boolean bHandleEventInSuperklasse = true;

			if (this.sLastSavedText == null && sNewText != null) {
				bAskBeforeExit = true;
			} else if (!sLastSavedText.equals(sNewText)) {
				bAskBeforeExit = true;
			}
			if (bAskBeforeExit) {
				int iAnswer = DialogFactory.showMeldung(
						LPMain.getTextRespectUISPr("lp.frage.textuebernehmen"),
						LPMain.getTextRespectUISPr("lp.frage"),
						JOptionPane.YES_NO_CANCEL_OPTION);
				switch (iAnswer) {
				case JOptionPane.YES_OPTION: {
					saveText(sNewText);
				}
					break;
				case JOptionPane.NO_OPTION: {
					// nothing here
				}
					break;
				case JOptionPane.CANCEL_OPTION: {
					bHandleEventInSuperklasse = false;
					// nothing here
				}
					break;
				default: {
					return;
				}
				}
			}
			// event nach oben zum schliessen
			if (bHandleEventInSuperklasse) {
				super.eventActionSpecial(e);
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SAVE)) {
			saveText(sNewText);
		}
	}

	private void saveText(String sNewText) {
		if (targetComponent != null) {
			targetComponent.setText(sNewText);
			this.sLastSavedText = sNewText;
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return editField ;
	}	
}
