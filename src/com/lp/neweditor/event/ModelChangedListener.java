package com.lp.neweditor.event;

@FunctionalInterface
public interface ModelChangedListener<T> {
	void dataChanged(ModelChangedEvent<T> e);
}
