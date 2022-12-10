package com.lp.neweditor.ui.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.swing.border.Border;

public class EditorPageBorder implements Border {

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2d = (Graphics2D) g;

		Color oldColor = g2d.getColor();
		g2d.setColor(Color.black);

		Shape outer = new Rectangle2D.Float(x, y, width, height);
		Shape inner = new Rectangle2D.Float(x + 1, y + 1, width - 3, height - 3);
		Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
		path.append(outer, false);
		path.append(inner, false);
		g2d.fill(path);
		g2d.setColor(oldColor);
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(1, 1, 2, 2);
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}

}
