package com.lp.neweditor.action;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import com.lp.client.frame.PersonalFormatter;
import com.lp.editor.LpEditorIconManager;
import com.lp.editor.util.LpEditorMessages;
import com.lp.neweditor.common.IconManagerSingleton;

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
	private String sName;
	private String sShortDescription; // Tooltip
	private KeyStroke kAcceleratorKey;
	private ImageIcon imgIcon;

	public ActionInsertDateTimeUserShortcut() {
		super("InsertDateTimeUserShortcut");
		sName = LpEditorMessages.getInstance().getString("Action.InsertDateTimeUserShortcut");
		sShortDescription = sName;
		// MR todo: Shortcut funktioniert nicht.
		// kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_I,
		// KeyEvent.CTRL_MASK);
		imgIcon = IconManagerSingleton.getIconManager().getIcon(LpEditorIconManager.ICON_EDIT_INSERT_DATE);

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

			PersonalFormatter personalFormatter = new PersonalFormatter();
			String content = personalFormatter.formatNowUserShortSign();

			if (content != null) {
				target.replaceSelection(content);
			} else {
				UIManager.getLookAndFeel().provideErrorFeedback(target);
			}
		}

	}

	public KeyStroke getAcceleratorKey() {
		return kAcceleratorKey;
	}

}