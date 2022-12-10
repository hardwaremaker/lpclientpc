package com.lp.neweditor.data.editor;

import com.lp.neweditor.data.BlockPosition;
import com.lp.neweditor.ui.block.EditorBlock;

public class BlockWithPosition {
	public final EditorBlock<?> block;
	public final BlockPosition position;

	public BlockWithPosition(EditorBlock<?> block, BlockPosition position) {
		this.block = block;
		this.position = position;
	}

	public EditorBlock<?> getBlock() {
		return block;
	}

	public BlockPosition getPosition() {
		return position;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((block == null) ? 0 : block.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockWithPosition other = (BlockWithPosition) obj;
		if (block == null) {
			if (other.block != null)
				return false;
		} else if (!block.equals(other.block))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}
}
