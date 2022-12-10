package com.lp.neweditor.ui.popup;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import com.lp.editor.util.LpEditorMessages;
import com.lp.neweditor.data.block.TextBlockModel;
import com.lp.neweditor.ui.block.EditorBlock;

/**
 * Popup Menu Klasse f&uuml;r Text
 * @author Alexander Daum
 *
 */
public class TextBlockPopupMenu extends BasicPopupMenu {

	public TextBlockPopupMenu(EditorBlock<? extends TextBlockModel> controller) {
		super(controller);
	}

	@Override
	protected JPopupMenu initPopup() {
		JPopupMenu baseMenu = super.initPopup();
		baseMenu.addSeparator();
		baseMenu.add(actions.getActionCut());
		baseMenu.add(actions.getActionCopy());
		baseMenu.add(actions.getActionPaste());
		baseMenu.addSeparator();
		baseMenu.add(createFormatMenu());

		return baseMenu;
	}

	private JMenu createFormatMenu() {
		JMenu formatMenu = new JMenu();
		formatMenu.setText(LpEditorMessages.getInstance().getString("Menu.Format"));
		formatMenu.setMnemonic(LpEditorMessages.getInstance().getMnemonic("Menu.Format"));
		formatMenu.add(createFormatStyleMenu());
		formatMenu.addSeparator();
		formatMenu.add(actions.getActionFontSelector());
		formatMenu.add(createFormatColorMenu());
		return formatMenu;
	}

	private JMenu createFormatStyleMenu() {
		JMenu formatStyle = new JMenu();
		formatStyle.setText(LpEditorMessages.getInstance().getString("Menu.Style"));
		formatStyle.setMnemonic(LpEditorMessages.getInstance().getMnemonic("Menu.Style"));

		formatStyle.add(actions.getActionBold());
		formatStyle.add(actions.getActionItalic());
		formatStyle.add(actions.getActionUnderline());
		formatStyle.add(actions.getActionStrikethrough());
		return formatStyle;
	}

	private JMenu createFormatColorMenu() {
		JMenu formatColor = new JMenu();
		formatColor.setText(LpEditorMessages.getInstance().getString("Menu.Color"));
		formatColor.setMnemonic(LpEditorMessages.getInstance().getMnemonic("Menu.Color"));
		formatColor.add(actions.getActionColorForeground());
		formatColor.add(actions.getActionColorBackground());

		return formatColor;
	}

}
