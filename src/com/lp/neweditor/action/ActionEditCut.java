package com.lp.neweditor.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.text.StyledEditorKit;

import com.lp.editor.LpEditorIconManager;
import com.lp.editor.RedoAction;
import com.lp.editor.util.LpEditorMessages;
import com.lp.neweditor.common.IconManagerSingleton;

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
	private String sName;
	private String sShortDescription; // Tooltip
	private KeyStroke kAcceleratorKey;
	private Integer iMnemonicKey;
	private ImageIcon imgIcon;

	public ActionEditCut() {
		super();
		sName = LpEditorMessages.getInstance().getString("Action.Cut");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic("Action.Cut");
		imgIcon = IconManagerSingleton.getIconManager().getIcon(LpEditorIconManager.ICON_EDIT_CUT);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		getTextComponent(e).requestFocus();
	}

	public KeyStroke getAcceleratorKey() {
		return kAcceleratorKey;
	}

}