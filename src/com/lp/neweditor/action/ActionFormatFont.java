package com.lp.neweditor.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import com.lp.client.pc.LPMain;
import com.lp.editor.LpEditorIconManager;
import com.lp.editor.ui.LpFontChooser;
import com.lp.editor.util.LpEditorMessages;
import com.lp.neweditor.common.IconManagerSingleton;
import com.lp.neweditor.ui.menu.BlockEditorData;

class ActionFormatFont extends TextAction {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final String sName;
	private final String sShortDescription; // Tooltip
	private final Integer iMnemonicKey;
	private final ImageIcon imgIcon;

	private final BlockEditorData menuData;

	public ActionFormatFont(BlockEditorData menuData) {
		super("FormatFont");
		sName = LpEditorMessages.getInstance().getString("Action.TextProperties");
		sShortDescription = sName;
		// kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic("Action.TextProperties");
		imgIcon = IconManagerSingleton.getIconManager().getIcon(LpEditorIconManager.ICON_STYLE_FONT);
		this.menuData = menuData;

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		// putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);
	}

	public void actionPerformed(ActionEvent e) {
		JTextComponent textComp = super.getTextComponent(e);
		if (!(textComp instanceof JTextPane))
			return;
		JTextPane editorPane = (JTextPane) textComp;
		int pos = textComp.getCaretPosition();
		if (textComp.getCaret().getMark() <= pos) {
			pos--;
		}
		AttributeSet attr = editorPane.getStyledDocument().getCharacterElement(pos).getAttributes();

		LpFontChooser fontChooser = new LpFontChooser(
				null, 1, menuData.getSystemFontsAsArray(), menuData.getAvailableFontSizesAsStringArray());

		fontChooser.setAttributes(attr, editorPane.getSelectedText());
		fontChooser.setLocationRelativeTo(LPMain.getInstance().getDesktop());
		fontChooser.setVisible(true);
		if (fontChooser.getOption() == JOptionPane.OK_OPTION) {
			editorPane.setCharacterAttributes(fontChooser.getAttributes(), false); // fontChooser.getSelectedFont()
			editorPane.updateUI();
		}
		fontChooser.dispose();
		fontChooser = null;
	}
}