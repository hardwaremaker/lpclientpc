package com.lp.editor;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

import com.lp.editor.util.LpEditorMessages;

class ActionFormatAlignment extends StyledEditorKit.AlignmentAction {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	String sShortDescription; // Tooltip
	// KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionFormatAlignment(LpEditor lpEditor, String resKey, int align) {
		super(LpEditorMessages.getInstance().getString(resKey), align);

		sShortDescription = LpEditorMessages.getInstance().getString(resKey);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(resKey);
		switch (align) {
		case StyleConstants.ALIGN_LEFT:
			imgIcon = lpEditor.iconManager
					.getIcon(LpEditorIconManager.ICON_STYLE_ALIGN_LEFT);
			break;
		case StyleConstants.ALIGN_RIGHT:
			imgIcon = lpEditor.iconManager
					.getIcon(LpEditorIconManager.ICON_STYLE_ALIGN_RIGHT);
			break;
		case StyleConstants.ALIGN_CENTER:
			imgIcon = lpEditor.iconManager
					.getIcon(LpEditorIconManager.ICON_STYLE_ALIGN_CENTER);
			break;
		case StyleConstants.ALIGN_JUSTIFIED:
			imgIcon = lpEditor.iconManager
					.getIcon(LpEditorIconManager.ICON_STYLE_ALIGN_JUSTIFIED);
			break;
		}

		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		lpEditor.requestFocusInWindow();
	}
}