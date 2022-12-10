package com.lp.neweditor.ui.menu;

import javax.swing.JComponent;
import javax.swing.JToolBar;

import com.lp.neweditor.action.BlockEditorActions;
import com.lp.neweditor.data.block.ImageBlockModel;
import com.lp.neweditor.ui.block.EditorBlock;

public class ImageEditorMenu extends EditorMenu {

	public ImageEditorMenu(EditorBlock<? extends ImageBlockModel> controller) {
		super(controller);
	}

	@Override
	protected void populateToolBar(JComponent toolBar) {
		super.populateToolBar(toolBar);
		toolBar.add(createImageMenu());
	}
	
	private JToolBar createImageMenu() {
		BlockEditorActions actions = controller.getEditor().getActions();
		
		JToolBar imageToolBar = createNewToolbar();
		imageToolBar.add(actions.getActionImageProperties()).setFocusable(false);
		imageToolBar.add(actions.getActionInsertTextblock()).setFocusable(false);
		return imageToolBar;
	}

}
