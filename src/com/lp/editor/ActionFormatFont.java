package com.lp.editor;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;

import com.lp.client.pc.LPMain;
import com.lp.editor.ui.LpFontChooser;
import com.lp.editor.util.LpEditorMessages;

class ActionFormatFont extends AbstractAction {
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

	public ActionFormatFont(LpEditor lpEditor) {
		sName = LpEditorMessages.getInstance().getString(
				"Action.TextProperties");
		sShortDescription = sName;
		// kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.TextProperties");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_STYLE_FONT);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		// putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		int pos = lpEditor.getCaretPosition();
		if (lpEditor.getCaret().getMark() <= pos) {
			pos--;
		}
		AttributeSet attr = lpEditor.getStyledDocument()
				.getCharacterElement(pos).getAttributes();

		LpFontChooser fontChooser = new LpFontChooser(
//				lpEditor.getOwnerFrame(),
				null,
				lpEditor.getEditorMode(),
				lpEditor.getAsSystemFonts(),
				lpEditor.getAsAvailableFontSizes());

		fontChooser.setAttributes(attr, lpEditor.jTextPane.getSelectedText());
		fontChooser.setLocationRelativeTo(LPMain.getInstance().getDesktop());
		fontChooser.setVisible(true);
		if (fontChooser.getOption() == JOptionPane.OK_OPTION) {
			lpEditor.setCharacterAttributes(fontChooser.getAttributes(), false); // fontChooser.getSelectedFont()
//			lpEditor.getStyledDocument().setCharacterAttributes(pos, pos, fontChooser.getAttributes(), false); // fontChooser.getSelectedFont()
		}
		fontChooser.dispose();
		fontChooser = null;
		lpEditor.requestFocusInWindow();
	}
}