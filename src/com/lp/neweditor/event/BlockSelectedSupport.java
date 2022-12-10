package com.lp.neweditor.event;

import com.lp.neweditor.ui.block.EditorBlock;

public class BlockSelectedSupport extends BaseEventSupport<BlockSelectedListener> {

	public BlockSelectedSupport(Object source) {
		super(source);
	}

	public void fireBlockSelected(EditorBlock<?> selected) {
		BlockSelectedEvent e = new BlockSelectedEvent(selected, source);
		forEachListener(lis -> lis.blockSelected(e));
	}

}
