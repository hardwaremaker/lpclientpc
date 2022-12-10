package com.lp.neweditor.ui.editor;

import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class EditorScrollablePane extends JPanel {
	private static final long serialVersionUID = 1L;

	public EditorScrollablePane(EditorPagePane page) {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(Box.createHorizontalGlue());
		this.add(page);
		this.add(Box.createHorizontalGlue());
	}

	@Override
	public Insets getInsets() {
		return new Insets(10, 10, 5, 5);
	}
}
