package com.lp.editor.util;

import java.awt.Component;

import javax.swing.JFrame;

public class EditorSwingUtils {
	public static JFrame getFrameOfComponent(Component comp) {
		Component frame = comp;
		while (frame != null && !(frame instanceof JFrame)) {
			frame = frame.getParent();
		}
		return (JFrame) frame;
	}
}
