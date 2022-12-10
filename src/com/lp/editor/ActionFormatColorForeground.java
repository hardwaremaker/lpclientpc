package com.lp.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.lp.editor.util.LpEditorMessages;

class ActionFormatColorForeground extends AbstractAction {
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

	public ActionFormatColorForeground(LpEditor lpEditor) {
		sName = LpEditorMessages.getInstance().getString(
				"Action.ColorForeground");
		sShortDescription = sName;
		// kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.ColorForeground");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_STYLE_COLOR_FOREGROUND);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		// putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		Color colorOld, colorNew;
		AttributeSet attributeSetOld = lpEditor.getCharacterAttributes();
		colorOld = lpEditor.getStyledDocument().getForeground(attributeSetOld);
		colorNew = JColorChooser.showDialog(lpEditor, sName, colorOld);
		if (colorNew != null) {
			SimpleAttributeSet attributeSetNew = new SimpleAttributeSet();
			StyleConstants.setForeground(attributeSetNew, colorNew);
			lpEditor.setCharacterAttributes(attributeSetNew, false);
//			lpEditor.getStyledDocument().setCharacterAttributes(iMnemonicKey, iMnemonicKey, attributeSetNew, false); // fontChooser.getSelectedFont()
		}
		lpEditor.requestFocusInWindow();
	}
}
