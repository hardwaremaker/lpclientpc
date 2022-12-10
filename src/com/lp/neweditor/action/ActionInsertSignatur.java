package com.lp.neweditor.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.PersonalDelegate;
import com.lp.client.pc.LPMain;
import com.lp.editor.LpEditorIconManager;
import com.lp.editor.util.LpEditorMessages;
import com.lp.neweditor.common.IconManagerSingleton;
import com.lp.neweditor.ui.editor.HvBlockEditor;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.Helper;

class ActionInsertSignatur extends TextAction {
	/**
	*
	*/
	private static final long serialVersionUID = 1L;
	private String sName;
	private String sShortDescription; // Tooltip
	private KeyStroke kAcceleratorKey;
	private ImageIcon imgIcon;
	
	private final HvBlockEditor editor;

	public ActionInsertSignatur(HvBlockEditor editor) {
		super("InsertSignatur");
		sName = LpEditorMessages.getInstance().getString("Action.InsertSignatur");
		sShortDescription = sName;

		imgIcon = IconManagerSingleton.getIconManager().getIcon(LpEditorIconManager.ICON_EDIT_INSERT_SIGNATUR);

		this.editor = editor;
		
		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);

		putValue(SMALL_ICON, imgIcon);
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
				PersonalDelegate persDelegate = DelegateFactory.getInstance().getPersonalDelegate();

				String locale = LPMain.getInstance().getTheClient().getLocUiAsString();

				if (editor.getModel().getLocale() != null) {
					locale = Helper.locale2String(editor.getModel().getLocale());
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

	}

	public KeyStroke getAcceleratorKey() {
		return kAcceleratorKey;
	}

}