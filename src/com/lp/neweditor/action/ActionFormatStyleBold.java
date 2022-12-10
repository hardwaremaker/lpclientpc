package com.lp.neweditor.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.text.StyledEditorKit;

import com.lp.editor.LpEditorIconManager;
import com.lp.editor.util.LpEditorMessages;
import com.lp.neweditor.common.IconManagerSingleton;

class ActionFormatStyleBold extends StyledEditorKit.BoldAction {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String sName;
	private String sShortDescription; // Tooltip
	private KeyStroke kAcceleratorKey;
	private Integer iMnemonicKey;
	private ImageIcon imgIcon;

	public ActionFormatStyleBold() {
		super();
		sName = LpEditorMessages.getInstance().getString("Action.Bold");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_F,
				KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK);
		iMnemonicKey = LpEditorMessages.getInstance()
				.getMnemonic("Action.Bold");
		imgIcon = IconManagerSingleton.getIconManager()
				.getIcon(LpEditorIconManager.ICON_STYLE_BOLD);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}
}