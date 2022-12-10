package com.lp.neweditor.ui.menu;

import java.awt.Component;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.JTextComponent;

import com.lp.neweditor.common.NewEditorHelper;

/**
 * PopupMenuListener implementierung, die
 * {@link Component#requestFocusInWindow()} auf einen {@link Component} aufruft,
 * wenn das Popup geschlossen wird
 * 
 * @author Alexander Daum
 *
 */
public class GetFocusOnPopupClose implements PopupMenuListener {
	private final Component focusComponent;

	/**
	 * 
	 * @param focusComponent Der Component, der den Fokus bekommt, wenn das Popup
	 *                       geschlossen wird
	 */
	public GetFocusOnPopupClose(Component focusComponent) {
		this.focusComponent = focusComponent;
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		if (focusComponent instanceof JTextComponent) {
			NewEditorHelper.getFocusKeepSelection((JTextComponent) focusComponent);
		} else {
			focusComponent.requestFocusInWindow();
		}
	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {

	}
}
