package com.lp.neweditor.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.lp.client.util.logger.LpLogger;
import com.lp.editor.LpEditorIconManager;
import com.lp.editor.util.LpEditorMessages;
import com.lp.neweditor.common.IconManagerSingleton;

class UndoRedoAction implements UndoableEditListener {

	private final ActionEditRedo redo;
	private final ActionEditUndo undo;
	private final UndoManager undoManager;

	public UndoRedoAction(UndoManager undoManager) {
		this.undoManager = undoManager;
		redo = new ActionEditRedo();
		undo = new ActionEditUndo();
	}

	public AbstractAction getUndoAction() {
		return undo;
	}

	public AbstractAction getRedoAction() {
		return redo;
	}

	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		updateState();
	}

	protected void updateState() {
		redo.updateRedoState();
		undo.updateUndoState();
	}

	protected class ActionEditUndo extends TextAction {
		private static final long serialVersionUID = 1L;
		String sName;
		String sShortDescription; // Tooltip
		KeyStroke kAcceleratorKey;
		Integer iMnemonicKey;
		ImageIcon imgIcon;

		public ActionEditUndo() {
			super("EditUndo");
			sName = LpEditorMessages.getInstance().getString("Action.Undo");
			sShortDescription = sName;
			kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK);
			iMnemonicKey = LpEditorMessages.getInstance().getMnemonic("Action.Undo");
			imgIcon = IconManagerSingleton.getIconManager().getIcon(LpEditorIconManager.ICON_EDIT_UNDO);

			putValue(NAME, sName);
			putValue(SHORT_DESCRIPTION, sShortDescription);
			putValue(ACCELERATOR_KEY, kAcceleratorKey);
			putValue(MNEMONIC_KEY, iMnemonicKey);
			putValue(SMALL_ICON, imgIcon);

			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undoManager.undo();
			} catch (CannotUndoException ex) {
				LpLogger.getInstance(ActionEditUndo.class).warn(ex.getLocalizedMessage());
			}
			updateState();
		}

		protected void updateUndoState() {
			if (undoManager.canUndo()) {
				setEnabled(true);
				putValue(Action.NAME, undoManager.getUndoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Undo");
			}
		}
	}

	protected class ActionEditRedo extends TextAction {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		private String sName;
		private String sShortDescription; // Tooltip
		private KeyStroke kAcceleratorKey;
		private Integer iMnemonicKey;
		private ImageIcon imgIcon;

		public ActionEditRedo() {
			super("EditRedo");
			sName = LpEditorMessages.getInstance().getString("Action.Redo");
			sShortDescription = sName;
			kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK);
			iMnemonicKey = LpEditorMessages.getInstance().getMnemonic("Action.Redo");
			imgIcon = IconManagerSingleton.getIconManager().getIcon(LpEditorIconManager.ICON_EDIT_REDO);

			putValue(NAME, sName);
			putValue(SHORT_DESCRIPTION, sShortDescription);
			putValue(ACCELERATOR_KEY, kAcceleratorKey);
			putValue(MNEMONIC_KEY, iMnemonicKey);
			putValue(SMALL_ICON, imgIcon);

			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undoManager.redo();
			} catch (CannotRedoException ex) {
				LpLogger.getInstance(ActionEditRedo.class).warn(ex.getLocalizedMessage());
			}
			updateState();
		}

		protected void updateRedoState() {
			if (undoManager.canRedo()) {
				setEnabled(true);
				putValue(Action.NAME, undoManager.getRedoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Redo");
			}
		}
	}
}
