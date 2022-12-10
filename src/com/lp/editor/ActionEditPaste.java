package com.lp.editor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.text.StyledEditorKit;

import com.lp.editor.util.LpEditorMessages;

class ActionEditPaste extends StyledEditorKit.PasteAction {
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

	public ActionEditPaste(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString("Action.Paste");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_V,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.Paste");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_PASTE);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		boolean bCellEditing = false;
		if (lpEditor.currentTable != null && lpEditor.currentTable.isEditing()) {
			lpEditor.currentTable.doRowHeightUpdate(false);
			bCellEditing = true;
		}

		super.actionPerformed(e);

		if (bCellEditing) {
			lpEditor.currentTable.doRowHeightUpdate(true);
			lpEditor.currentTable.updateRowHeight(lpEditor.currentTable
					.getEditingRow());
		}

		lpEditor.requestFocusInWindow();
	}

	public KeyStroke getAcceleratorKey() {
		return kAcceleratorKey;
	}

}