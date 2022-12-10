package com.lp.editor;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import com.lp.client.frame.PersonalFormatter;
import com.lp.editor.util.LpEditorMessages;

/**
 *
 * <p>
 * Action um das heutige Datum und Uhrzeit in client Locale und Kurzzeichen des
 * gerade eingeloggten Personal im Editor einzufuegen.
 * </p>
 *
 * <p>
 *
 * @author $Author: valentin $
 *         </p>
 *
 * @version not attributable Date $Date: 2008/08/11 10:46:03 $
 */
class ActionInsertDateTimeUserShortcut extends TextAction {
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

	public ActionInsertDateTimeUserShortcut(LpEditor lpEditor) {
		super("InsertDateTimeUserShortcut");
		sName = LpEditorMessages.getInstance().getString(
				"Action.InsertDateTimeUserShortcut");
		sShortDescription = sName;
		// MR todo: Shortcut funktioniert nicht.
		// kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_I,
		// KeyEvent.CTRL_MASK);
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_INSERT_DATE);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		// putValue(ACCELERATOR_KEY, kAcceleratorKey);
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

			PersonalFormatter personalFormatter = new PersonalFormatter();
			String content = personalFormatter.formatNowUserShortSign();
			
			if (content != null) {
				target.replaceSelection(content);
			} else {
				UIManager.getLookAndFeel().provideErrorFeedback(target);
			}
		}

		lpEditor.requestFocusInWindow();
	}

	public KeyStroke getAcceleratorKey() {
		return kAcceleratorKey;
	}

}