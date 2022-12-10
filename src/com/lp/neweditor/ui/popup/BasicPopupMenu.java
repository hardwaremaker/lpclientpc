package com.lp.neweditor.ui.popup;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;

import com.lp.neweditor.action.BlockEditorActions;
import com.lp.neweditor.ui.block.EditorBlock;

/**
 * Klasse zum verwalten von Popup Menus f&uuml;r den neuen Editor. <br>
 * Diese Klasse wird nicht direkt verwendet, sondern bietet nur ein
 * Grundger&uuml;st, das spezifische Klassen f&uuml;r einen Typen von Editor
 * Block verwenden k&ouml;nnen <br>
 * Spezifische Klassen sollten diese Klasse &uuml;berschreiben und es sollte
 * meistens ausreichen, nur die {@link BasicPopupMenu#initPopup()} Methode zu
 * &uuml;berschreiben und die gew&uuml;nschten Elemente hinzuzuf&uuml;gen <br>
 * Diese Klasse hat auch einen {@link MouseListener}, der bei Rechtsklick das
 * Menu an dieser Stelle &ouml;ffnet
 * 
 * @author Alexander Daum
 *
 */
public abstract class BasicPopupMenu {
	private JPopupMenu popup;
	private MouseListener mouseListener;
	/**
	 * Alle Actions sollten wenn m&ouml;glich von hier bezogen werden. Dadurch
	 * k&ouml;nnen die Actions zentral verwaltet (enable/disable) werden
	 */
	protected final BlockEditorActions actions;

	public BasicPopupMenu(EditorBlock<?> controller) {
		this.actions = controller.getEditor().getActions();
		mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3 && controller.isEditable()) {
					getSwingPopup().show(e.getComponent(), e.getX(), e.getY());
					e.getComponent().requestFocusInWindow();
				}
			}
		};
	}

	/**
	 * Gibt das Swing {@link JPopupMenu} zur&uuml;ck. Das Popup Menu wird beim
	 * ersten Aufruf dieser Methode erstellt
	 * 
	 * @return
	 */
	public JPopupMenu getSwingPopup() {
		if (popup == null) {
			popup = initPopup();
		}
		return popup;
	}

	/**
	 * Erstellt das JPopupMenu. <br>
	 * Subklassen, die mehr Menuelemente hinzuf&uuml;gen, sollen diese Methode
	 * &uuml;berschreiben, dann die <code>super</code> Methode aufrufen um das
	 * JPopupMenu zu erhalten, zu dem mehr Eintr&auml;ge hinzugef&uuml;gt werden.
	 * <br>
	 * Diese Methode wird beim ersten Aufruf von
	 * {@link BasicPopupMenu#getSwingPopup()} aufgerufen
	 * 
	 * @return
	 */
	protected JPopupMenu initPopup() {
		JPopupMenu popupMenu = new JPopupMenu();

		popupMenu.add(actions.getUndoAction());
		popupMenu.add(actions.getRedoAction());

		return popupMenu;
	}

	/**
	 * Gibt den MouseListener zur&uuml;ck. Der MouseListener &ouml;ffnet das
	 * PopupMenu bei Rechtsklick auf dem Component, der geklickt wurde
	 * 
	 * @return
	 */
	public MouseListener getMouseListener() {
		return mouseListener;
	}
}
