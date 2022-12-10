package com.lp.neweditor.data;

import javax.swing.undo.UndoableEditSupport;

public class SilenceableUndoableEditSupport extends UndoableEditSupport {
	private boolean ignoreUpdates = false;

	public void setIgnoreUpdates(boolean ignoreUpdates) {
		this.ignoreUpdates = ignoreUpdates;
	}

	public SilenceableUndoableEditSupport(Object r) {
		super(r);
	}

	public synchronized void postEdit(javax.swing.undo.UndoableEdit e) {
		if (!ignoreUpdates) {
			super.postEdit(e);
		}
	}
}