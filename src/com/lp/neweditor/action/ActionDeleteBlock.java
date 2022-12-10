package com.lp.neweditor.action;

import java.awt.event.ActionEvent;
import java.util.Optional;

import javax.swing.AbstractAction;

import com.lp.editor.LpEditorIconManager;
import com.lp.editor.util.LpEditorMessages;
import com.lp.neweditor.common.IconManagerSingleton;
import com.lp.neweditor.data.BlockPosition;
import com.lp.neweditor.ui.block.EditorBlock;
import com.lp.neweditor.ui.editor.HvBlockEditor;

public class ActionDeleteBlock extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private final HvBlockEditor editor;

	public ActionDeleteBlock(HvBlockEditor editor) {
		this.editor = editor;

		String name = LpEditorMessages.getInstance().getString("Action.DeleteBlock");
		putValue(NAME, name);
		putValue(SHORT_DESCRIPTION, name);
		putValue(SMALL_ICON, IconManagerSingleton.getIconManager().getIcon(LpEditorIconManager.ICON_DELETE_BLOCK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		BlockPosition pos = editor.getSelectedBlockPosition();
		if (pos.isValidPosition()) {
			editor.deleteBlock(pos);
			Optional<EditorBlock<?>> newOnPos = editor.getBlockAt(pos);
			if (!newOnPos.isPresent()) {
				newOnPos = editor.getBlockAt(new BlockPosition(0, pos.y - 1));
			}
			if (newOnPos.isPresent()) {
				newOnPos.get().getView().getUIComponent().requestFocusInWindow();
			}
		}
	}

}
