package com.lp.neweditor.event;

import java.util.EventObject;

public class ModelChangedEvent<T> extends EventObject {
	private static final long serialVersionUID = 1L;

	private final T dataBefore;
	private final T dataAfter;

	public ModelChangedEvent(T dataBefore, T dataAfter, Object source) {
		super(source);
		this.dataBefore = dataBefore;
		this.dataAfter = dataAfter;
	}

	public T getDataBefore() {
		return dataBefore;
	}

	public T getDataAfter() {
		return dataAfter;
	}
}
