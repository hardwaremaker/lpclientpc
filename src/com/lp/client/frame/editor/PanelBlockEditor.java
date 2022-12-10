package com.lp.client.frame.editor;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Optional;

import javax.swing.JComponent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.WrapperBlockEditorField;
import com.lp.neweditor.data.BlockPosition;
import com.lp.neweditor.ui.block.EditorBlock;
import com.lp.neweditor.ui.editor.HvBlockEditor;

public class PanelBlockEditor extends PanelDialog {
	private static final long serialVersionUID = 1L;
	private WrapperBlockEditorField targetComp;
	private HvBlockEditor editor;

	public PanelBlockEditor(InternalFrame internalFrame, String add2Title, WrapperBlockEditorField target) throws Throwable {
		super(internalFrame, add2Title);
		this.targetComp = target;
		editor = new HvBlockEditor();
		editor.fromDto(target.getContent(), false);
		editor.setEditable(true);
		editor.setMenuVisible(true);
		jbInit();
	}

	@Override
	protected void onEscOrClosePanelDialog() throws Throwable {
		super.onEscOrClosePanelDialog();
		targetComp.setContent(editor.toDto());
	}

	private void jbInit() {
		jpaWorkingOn.add(editor.getView().getUIComponent(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	@Override
	protected JComponent getFirstFocusableComponent() throws Exception {
		Optional<EditorBlock<?>> firstBlock = editor.getBlockAt(new BlockPosition(0, 0));
		if (firstBlock.isPresent()) {
			return firstBlock.get().getView().getUIComponent();
		} else {
			return editor.getView().getUIComponent();
		}
	}

}
