package com.lp.editor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.text.StyledEditorKit;

import com.lp.editor.util.LpEditorMessages;

class ActionEditCopy extends StyledEditorKit.CopyAction {
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

	public ActionEditCopy(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString("Action.Copy");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_C,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance()
				.getMnemonic("Action.Copy");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_COPY);

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