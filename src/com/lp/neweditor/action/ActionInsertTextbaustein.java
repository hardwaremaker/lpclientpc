package com.lp.neweditor.action;

import java.awt.event.ActionEvent;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.system.AuswahlTextbaustein;
import com.lp.editor.LpEditorIconManager;
import com.lp.editor.util.LpEditorMessages;
import com.lp.neweditor.common.IconManagerSingleton;
import com.lp.neweditor.ui.editor.HvBlockEditor;

class ActionInsertTextbaustein extends TextAction {
	/**
	*
	*/
	private static final long serialVersionUID = 1L;
	private String sName;
	private String sShortDescription; // Tooltip
	private KeyStroke kAcceleratorKey;
	private ImageIcon imgIcon;
	private final HvBlockEditor editor;

	public ActionInsertTextbaustein(HvBlockEditor editor) {
		super("InsertTextbaustein");
		sName = LpEditorMessages.getInstance().getString("Action.InsertTextbaustein");
		sShortDescription = sName;

		imgIcon = IconManagerSingleton.getIconManager().getIcon(LpEditorIconManager.ICON_EDIT_INSERT_TEXTBAUSTEIN);

		this.editor = editor;

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);

		putValue(SMALL_ICON, imgIcon);
	}

	public void actionPerformed(ActionEvent e) {
		JTextComponent target = getTextComponent(e);
		if ((target != null) && (e != null)) {
			if ((!target.isEditable()) || (!target.isEnabled())) {
				UIManager.getLookAndFeel().provideErrorFeedback(target);
				return;
			}

			try {
				Optional<InternalFrame> frame = editor.getInternalFrame();
				if (frame.isPresent()) {
					AuswahlTextbaustein auswahlText = new AuswahlTextbaustein(frame.get());
					auswahlText.choose();
					String content = auswahlText.getTextbausteinText();

					if (content != null) {
						String text = target.getText();
						text = text + content;
						target.setText(text);
					}
				}
			} catch (Throwable ex) {
			}
		}

	}

	public KeyStroke getAcceleratorKey() {
		return kAcceleratorKey;
	}

}