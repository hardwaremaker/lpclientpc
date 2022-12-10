package com.lp.editor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.lp.editor.util.LpEditorMessages;

class ActionInsertRowBelow extends AbstractAction {
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

	public ActionInsertRowBelow(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString(
				"Action.RowInsertBelow");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.RowInsertBelow");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_INSERT_ROW_AFTER);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		lpEditor.insertTableRow(false);
	}
}