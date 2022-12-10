package com.lp.editor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.undo.CannotRedoException;

import com.lp.client.util.logger.LpLogger;
import com.lp.editor.util.LpEditorMessages;

public class RedoAction extends AbstractAction {
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

	public RedoAction(LpEditor lpEditor) {
		sName = LpEditorMessages.getInstance().getString("Action.Redo");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_R,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance()
				.getMnemonic("Action.Redo");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_REDO);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
		setEnabled(false);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			lpEditor.undoManager.redo();
		} catch (CannotRedoException ex) {
			LpLogger.getInstance(RedoAction.class).warn(
					ex.getLocalizedMessage());
		}
		updateRedoState();
		lpEditor.actionEditUndo.updateUndoState();
		lpEditor.updateBufferStatus(null, -1, true);
		lpEditor.requestFocusInWindow();
	}

	protected void updateRedoState() {
		if (lpEditor.undoManager.canRedo()) {
			setEnabled(true);
			putValue(Action.NAME,
					lpEditor.undoManager.getRedoPresentationName());
		} else {
			setEnabled(false);
			putValue(Action.NAME, "Redo");
		}
	}
}
