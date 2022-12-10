package com.lp.neweditor.common;

import com.lp.editor.LpEditorIconManager;

public class IconManagerSingleton {
	private static LpEditorIconManager iconManager = new LpEditorIconManager();

	public static LpEditorIconManager getIconManager() {
		return iconManager;
	}
}
