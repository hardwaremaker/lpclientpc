package com.lp.neweditor.ui.block;

import java.awt.event.FocusListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.lp.neweditor.event.PropertyChangeSupplier;
import com.lp.neweditor.ui.EditorUIComponent;
import com.lp.neweditor.ui.menu.EditorMenu;
import com.lp.neweditor.ui.menu.EditorStatusBar;
import com.lp.neweditor.ui.popup.BasicPopupMenu;

public abstract class BlockView implements EditorUIComponent, PropertyChangeSupplier {
	private final EditorBlock<?> controller;
	protected final PropertyChangeSupport propSupport;
	protected final BasicPopupMenu popupMenu;

	public BlockView(EditorBlock<?> controller) {
		this.controller = controller;
		this.propSupport = new PropertyChangeSupport(this);
		this.popupMenu = createPopup();
	}

	public EditorBlock<?> getController() {
		return controller;
	}

	public abstract EditorMenu getMenuForBlock();
	
	public abstract EditorStatusBar getStatusBar();

	protected abstract BasicPopupMenu createPopup();

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propSupport.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propSupport.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propSupport.removePropertyChangeListener(propertyName, listener);
	}

	public abstract void getFocusClickBehind();
	
	public abstract void setEditable(boolean editable);
	
	public abstract void addFocusListener(FocusListener listener);
	
}
