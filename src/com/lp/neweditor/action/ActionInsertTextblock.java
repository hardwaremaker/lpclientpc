package com.lp.neweditor.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.lp.editor.LpEditorIconManager;
import com.lp.editor.util.LpEditorMessages;
import com.lp.neweditor.common.IconManagerSingleton;
import com.lp.neweditor.common.InsertPosition;
import com.lp.neweditor.data.BlockPosition;
import com.lp.neweditor.ui.block.TextBlockController;
import com.lp.neweditor.ui.editor.HvBlockEditor;

public class ActionInsertTextblock extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private final HvBlockEditor editor;

	public ActionInsertTextblock(HvBlockEditor editor) {
		this.editor = editor;

		String name = LpEditorMessages.getInstance().getString("Action.InsertTextBlock");
		putValue(NAME, name);
		putValue(SHORT_DESCRIPTION, name);
		putValue(SMALL_ICON, IconManagerSingleton.getIconManager().getIcon(LpEditorIconManager.ICON_INSERT_TEXTBLOCK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		BlockPosition pos = editor.getSelectedBlockPosition();
		TextBlockController block = new TextBlockController(editor);
		editor.addBlock(block, pos, InsertPosition.AFTER_VERTICAL);
		block.getView().getUIComponent().requestFocusInWindow();
	}

}
