package com.lp.neweditor.event;

import java.util.EventObject;

import com.lp.neweditor.ui.block.EditorBlock;

public class BlockSelectedEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private final EditorBlock<?> selectedBlock;

	public BlockSelectedEvent(EditorBlock<?> selectedBlock, Object source) {
		super(source);
		this.selectedBlock = selectedBlock;
	}

	public EditorBlock<?> getSelectedBlock() {
		return selectedBlock;
	}
}
