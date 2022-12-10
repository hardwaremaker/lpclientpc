package com.lp.neweditor.action;

import java.awt.event.ActionEvent;
import java.util.Optional;

import javax.swing.AbstractAction;

import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.editor.LpEditorIconManager;
import com.lp.editor.util.LpEditorMessages;
import com.lp.neweditor.common.IconManagerSingleton;
import com.lp.neweditor.ui.block.ImageBlockController;
import com.lp.neweditor.ui.dialog.DialogInsertImage;
import com.lp.neweditor.ui.editor.HvBlockEditor;
import com.lp.server.system.service.HvImageDto;

public class ActionInsertImageBlock extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private final HvBlockEditor editor;

	public ActionInsertImageBlock(HvBlockEditor editor) {
		this.editor = editor;

		String name = LpEditorMessages.getInstance().getString("Action.InsertImage");
		putValue(NAME, name);
		putValue(SHORT_DESCRIPTION, name);
		putValue(SMALL_ICON, IconManagerSingleton.getIconManager().getIcon(LpEditorIconManager.ICON_INSERT_IMAGE));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DialogInsertImage dialog = new DialogInsertImage(LPMain.getInstance().getDesktop(), true);
		dialog.setVisible(true);
		if(dialog.getAccepted()) {
			ImageBlockController block = new ImageBlockController(editor);
			block.getModel().beginIgnoreUpdate();
			Optional<HvImageDto> imageDto = dialog.getSelectedImage();
			if(!imageDto.isPresent())
				return;
			block.setImage(imageDto.get());
			block.setSize(dialog.getImageSize());
			block.getModel().setAlignment(dialog.getAlignment());
			block.getModel().endIgnoreUpdate();
			editor.addBlock(block, editor.getSelectedBlockPosition(), dialog.getInsertPosition());
		}
	}

}
