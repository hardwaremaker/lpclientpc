package com.lp.editor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.text.StyledEditorKit;

import com.lp.editor.util.LpEditorMessages;

/**
 * Action - Klasse fuer das Bearbeiten der Undo - Events. Haelt unter anderem
 * den Undo - Manager auf dem neuesten Stand und Stellt Methoden zum
 * entsprechenden Aktivieren / Deaktivieren der Action bereit.
 *
 * @see com.lp.lpeditor.LpEditor.LpUndoableEditListener
 * @see RedoAction
 */

class ActionEditCut extends StyledEditorKit.CutAction {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionEditCut(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString("Action.Cut");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_X,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic("Action.Cut");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_CUT);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		lpEditor.requestFocusInWindow();
	}

	public KeyStroke getAcceleratorKey() {
		return kAcceleratorKey;
	}

}