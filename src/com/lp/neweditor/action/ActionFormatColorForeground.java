package com.lp.neweditor.action;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TextAction;

import com.lp.editor.LpEditorIconManager;
import com.lp.editor.util.LpEditorMessages;
import com.lp.neweditor.common.IconManagerSingleton;

class ActionFormatColorForeground extends TextAction {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String sName;
	private String sShortDescription; // Tooltip
	private Integer iMnemonicKey;
	private ImageIcon imgIcon;

	public ActionFormatColorForeground() {
		super("FormatColorForeground");
		sName = LpEditorMessages.getInstance().getString("Action.ColorForeground");
		sShortDescription = sName;
		// kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic("Action.ColorForeground");
		imgIcon = IconManagerSingleton.getIconManager().getIcon(LpEditorIconManager.ICON_STYLE_COLOR_FOREGROUND);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		// putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);
	}

	public void actionPerformed(ActionEvent e) {
		Color colorOld, colorNew;
		JTextComponent textComp = getTextComponent(e);
		if (textComp instanceof JTextPane) {
			JTextPane editorPane = (JTextPane) textComp;
			AttributeSet attributeSetOld = editorPane.getCharacterAttributes();
			colorOld = editorPane.getStyledDocument().getForeground(attributeSetOld);
			colorNew = JColorChooser.showDialog(textComp, sName, colorOld);
			if (colorNew != null) {
				SimpleAttributeSet attributeSetNew = new SimpleAttributeSet();
				StyleConstants.setForeground(attributeSetNew, colorNew);
				editorPane.setCharacterAttributes(attributeSetNew, false);
			}
		}
	}
}
