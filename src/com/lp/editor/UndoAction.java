package com.lp.editor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.undo.CannotUndoException;

import com.lp.client.util.logger.LpLogger;
import com.lp.editor.util.LpEditorMessages;

public class UndoAction extends AbstractAction {
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

	public UndoAction(LpEditor lpEditor) {
		sName = LpEditorMessages.getInstance().getString("Action.Undo");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance()
				.getMnemonic("Action.Undo");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_UNDO);

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
			lpEditor.undoManager.undo();
		} catch (CannotUndoException ex) {
			LpLogger.getInstance(UndoAction.class).warn(
					ex.getLocalizedMessage());
		}
		updateUndoState();
		lpEditor.actionEditRedo.updateRedoState();
		lpEditor.updateBufferStatus(null, -1, true);
		lpEditor.requestFocusInWindow();
	}

	protected void updateUndoState() {
		if (lpEditor.undoManager.canUndo()) {
			setEnabled(true);
			putValue(Action.NAME,
					lpEditor.undoManager.getUndoPresentationName());
		} else {
			setEnabled(false);
			putValue(Action.NAME, "Undo");
		}
	}
}
