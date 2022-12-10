package com.lp.editor;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import com.lp.client.system.AuswahlTextbaustein;
import com.lp.editor.util.LpEditorMessages;

class ActionInsertTextbaustein extends TextAction {
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

	public ActionInsertTextbaustein(LpEditor lpEditor) {
		super("InsertTextbaustein");
		sName = LpEditorMessages.getInstance().getString(
				"Action.InsertTextbaustein");
		sShortDescription = sName;

		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_INSERT_TEXTBAUSTEIN);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);

		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		JTextComponent target = getTextComponent(e);
		if ((target != null) && (e != null)) {
			if ((!target.isEditable()) || (!target.isEnabled())) {
				UIManager.getLookAndFeel().provideErrorFeedback(target);
				return;
			}

			try {
				AuswahlTextbaustein auswahlText = new AuswahlTextbaustein(lpEditor.getInternalFrame());
				auswahlText.choose();
				String content = auswahlText.getTextbausteinText();

				if (content != null) {
					String text = target.getText();
					text = text + content;
					target.setText(text);
				}
			} catch (Throwable ex) {
			}
		}

		lpEditor.requestFocusInWindow();
	}

	public KeyStroke getAcceleratorKey() {
		return kAcceleratorKey;
	}

}