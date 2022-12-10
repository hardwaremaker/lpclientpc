package com.lp.editor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.text.StyledEditorKit;

import com.lp.editor.util.LpEditorMessages;

class ActionFormatStyleBold extends StyledEditorKit.BoldAction {
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

	public ActionFormatStyleBold(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString("Action.Bold");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_F,
				KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK);
		iMnemonicKey = LpEditorMessages.getInstance()
				.getMnemonic("Action.Bold");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_STYLE_BOLD);

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
}