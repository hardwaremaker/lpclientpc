package com.lp.neweditor.event;

import com.lp.neweditor.common.Direction;
import com.lp.neweditor.data.BlockPosition;
import com.lp.neweditor.event.BlockEvent.Type;
import com.lp.neweditor.ui.block.EditorBlock;

public class BlockEventSupport extends BaseEventSupport<BlockEventListener> {

	public BlockEventSupport(Object source) {
		super(source);
	}

	public void fireBlockAdded(EditorBlock<?> block, BlockPosition pos, Direction dir) {
		BlockEvent event = new BlockEvent(block, pos, Type.INSERT, dir, source);
		super.forEachListener(lis -> lis.onBlockEvent(event));
	}

	public void fireBlockRemoved(EditorBlock<?> block, BlockPosition pos, Direction dir) {
		BlockEvent event = new BlockEvent(block, pos, Type.REMOVE, dir, source);
		super.forEachListener(lis -> lis.onBlockEvent(event));
	}

}
