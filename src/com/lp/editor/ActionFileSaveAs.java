package com.lp.editor;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import com.lp.editor.util.LpEditorMessages;

class ActionFileSaveAs extends AbstractAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	// KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionFileSaveAs(LpEditor lpEditor) {
		sName = LpEditorMessages.getInstance().getString("Action.SaveAs_");
		sShortDescription = sName;
		// kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.SaveAs_");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_FILE_SAVEAS);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		// putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		lpEditor.saveAs();
		lpEditor.requestFocusInWindow();
	}
}