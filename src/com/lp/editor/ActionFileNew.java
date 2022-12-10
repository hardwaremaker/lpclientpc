package com.lp.editor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.lp.editor.util.LpEditorMessages;

/**
 *
 * <p>
 * <I>Actions fuer den LpEditor</I>.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>August 2003</I>
 * </p>
 * <p>
 * </p>
 *
 * @author Kajetan Fuchsberger
 * @author Sascha Zelzer
 * @version $Revision: 1.4 $
 */

class ActionFileNew extends AbstractAction {
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

	public ActionFileNew(LpEditor lpEditor) {
		sName = LpEditorMessages.getInstance().getString("Action.New");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_N,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic("Action.New");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_FILE_NEW);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		lpEditor.openBlank();
		lpEditor.requestFocusInWindow();
	}
}