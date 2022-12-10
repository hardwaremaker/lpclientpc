package com.lp.neweditor.event;

public class ModelChangedSupport<T> extends BaseEventSupport<ModelChangedListener<T>>{
	public ModelChangedSupport(Object source) {
		super(source);
	}

	public void fireModelChange(T oldData, T newData) {
		ModelChangedEvent<T> event = new ModelChangedEvent<>(oldData, newData, source);
		forEachListener(l -> l.dataChanged(event));
	}
}
