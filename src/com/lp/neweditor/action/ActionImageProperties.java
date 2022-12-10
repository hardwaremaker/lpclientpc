package com.lp.neweditor.action;

import java.awt.event.ActionEvent;
import java.util.Optional;

import javax.swing.AbstractAction;

import com.lp.client.pc.LPMain;
import com.lp.editor.LpEditorIconManager;
import com.lp.editor.util.LpEditorMessages;
import com.lp.neweditor.common.IconManagerSingleton;
import com.lp.neweditor.data.block.ImageBlockModel;
import com.lp.neweditor.ui.block.EditorBlock;
import com.lp.neweditor.ui.block.ImageBlockController;
import com.lp.neweditor.ui.dialog.DialogInsertImage;
import com.lp.neweditor.ui.editor.HvBlockEditor;
import com.lp.server.system.service.HvImageDto;

public class ActionImageProperties extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private final HvBlockEditor editor;

	public ActionImageProperties(HvBlockEditor editor) {
		this.editor = editor;

		String name = LpEditorMessages.getInstance().getString("Action.EditImage");
		putValue(NAME, name);
		putValue(SHORT_DESCRIPTION, name);
		putValue(SMALL_ICON, IconManagerSingleton.getIconManager().getIcon(LpEditorIconManager.ICON_EDIT_IMAGE));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EditorBlock<?> controller = editor.getSelectedBlock();
		if (controller.getModel() instanceof ImageBlockModel) {
			ImageBlockModel model = (ImageBlockModel) controller.getModel();
			DialogInsertImage dialog = new DialogInsertImage(LPMain.getInstance().getDesktop(), false);
			dialog.setData(model.getImageDtoForDB(), model.getSize(), model.getAlignment());
			dialog.setVisible(true);
			if (dialog.getAccepted()) {
				model.beginCompoundUpdate();
				model.setSize(dialog.getImageSize());
				model.setAlignment(dialog.getAlignment());
				model.setImage(dialog.getSelectedImage().orElse(null));
				model.endCompoundUpdate();
			}
		}
	}
}
