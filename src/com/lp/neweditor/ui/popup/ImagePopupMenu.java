package com.lp.neweditor.ui.popup;

import javax.swing.JPopupMenu;

import com.lp.neweditor.data.block.ImageBlockModel;
import com.lp.neweditor.ui.block.EditorBlock;

public class ImagePopupMenu extends BasicPopupMenu{

	public ImagePopupMenu(EditorBlock<? extends ImageBlockModel> controller) {
		super(controller);
	}
	
	@Override
	protected JPopupMenu initPopup() {
		JPopupMenu popup = super.initPopup();
		popup.add(actions.getActionImageProperties());
		return popup;
	}

}
