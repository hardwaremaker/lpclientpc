package com.lp.neweditor.event;

import java.awt.Component;
import java.awt.event.FocusAdapter;

import com.lp.neweditor.ui.block.EditorBlock;

public class BlockSelectedFocusAdapter extends FocusAdapter{
	private EditorBlock<?> controller;

	public BlockSelectedFocusAdapter(EditorBlock<?> controller) {
		this.controller = controller;
	}
	
	
}
