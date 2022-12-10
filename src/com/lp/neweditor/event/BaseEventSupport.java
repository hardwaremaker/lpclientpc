package com.lp.neweditor.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Basisklasse f&uuml;r Event Support.
 * 
 * @author Alexander Daum
 *
 * @param <ListenerType>
 */
public abstract class BaseEventSupport<ListenerType> {
	private List<ListenerType> listeners;
	protected final Object source;

	public BaseEventSupport(Object source) {
		this.source = source;
		listeners = new ArrayList<>();
	}

	public void addListener(ListenerType listener) {
		listeners.add(listener);
	}

	public void removeListener(ListenerType listener) {
		listeners.remove(listener);
	}

	protected void forEachListener(Consumer<ListenerType> action) {
		listeners.forEach(action);
	}
}
