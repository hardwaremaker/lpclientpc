package com.lp.editor;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.PersonalDelegate;
import com.lp.client.pc.LPMain;
import com.lp.editor.util.LpEditorMessages;
import com.lp.server.system.service.TheClientDto;

class ActionInsertSignatur extends TextAction {
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

	public ActionInsertSignatur(LpEditor lpEditor) {
		super("InsertSignatur");
		sName = LpEditorMessages.getInstance().getString(
				"Action.InsertSignatur");
		sShortDescription = sName;

		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_INSERT_SIGNATUR);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);

		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	@SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent e) {
		JTextComponent target = getTextComponent(e);
		if ((target != null) && (e != null)) {
			if ((!target.isEditable()) || (!target.isEnabled())) {
				UIManager.getLookAndFeel().provideErrorFeedback(target);
				return;
			}
			String content = "";

			// Personal Kurzzeichen des Bearbeiters
			try {
				TheClientDto clientDto = LPMain.getInstance().getTheClient();
				Integer iPersonalID = clientDto.getIDPersonal();
				PersonalDelegate persDelegate = DelegateFactory.getInstance()
						.getPersonalDelegate();

				String locale = LPMain.getInstance().getTheClient()
						.getLocUiAsString();

				if (lpEditor.localeFuerSignatur != null) {
					locale = lpEditor.localeFuerSignatur;
				}

				String signatur = persDelegate.getSignatur(iPersonalID, locale);
				if (signatur != null) {
					content = signatur;
				}
			} catch (Throwable ex) {
			}

			if (content != null) {

				String text = target.getText();

				text = text + content;

				target.setText(text);
			}
		}

		lpEditor.requestFocusInWindow();
	}

	public KeyStroke getAcceleratorKey() {
		return kAcceleratorKey;
	}

}