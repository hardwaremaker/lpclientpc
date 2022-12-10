package com.lp.neweditor.ui.menu;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JToggleButton;
import javax.swing.text.AttributeSet;

import com.lp.editor.ui.LpToolBarToggleButton;

public class EditorToggleMenuButtonManager {

	private Map<Object, JToggleButton> btnMap;

	public EditorToggleMenuButtonManager() {
		btnMap = new HashMap<>();
	}

	public JToggleButton createButton(Object property, Action action) {
		JToggleButton btn = new LpToolBarToggleButton();
		btn.setAction(action);
		btn.setFocusable(false);
		btnMap.put(property, btn);
		return btn;
	}

	public void removeButton(Object property) {
		btnMap.remove(property);
	}

	public void propertyChanged(Object property, boolean newValue) {
		JToggleButton btn = btnMap.get(property);
		if (btn != null) {
			btn.setSelected(newValue);
		}
	}

	public void propertiesChanged(AttributeSet attrSet) {
		for (Object key : btnMap.keySet()) {
			Object attribute = attrSet.getAttribute(key);
			if (attribute != null && attribute instanceof Boolean) {
				propertyChanged(key, (Boolean) attribute);
			} else if (attribute == null) {
				// Manche Attribute sind nicht vorhanden wenn nicht aktiv, statt false
				propertyChanged(key, false);
			}
		}
	}

}
