package com.lp.neweditor.event;

public interface BlockEventProvider {
	void addBlockMovedListener(BlockEventListener listener);
	void removeBlockMovedListener(BlockEventListener listener);
}
