/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.rechtschreibung;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.QuadCurve2D;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.View;

import javafx.scene.shape.QuadCurve;

/**
 * HighlightPainter, der eine gewellte rote Linie zeichnet.
 *
 */
class RechtschreibfehlerHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {

	RechtschreibfehlerHighlightPainter(Color color) {
		super(color);
	}

	@Override
	public Shape paintLayer(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c, View view) {
		Rectangle rect;

		if (offs0 == view.getStartOffset() && offs1 == view.getEndOffset()) {
			if (bounds instanceof Rectangle) {
				rect = (Rectangle) bounds;
			} else {
				rect = bounds.getBounds();
			}
		} else {
			try {
				Shape shape = view.modelToView(offs0, Position.Bias.Forward, offs1, Position.Bias.Backward, bounds);
				rect = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
			} catch (BadLocationException e) {
				rect = null;
			}
		}

		if (rect != null) {
			Color color = getColor();

			if (color == null) {
				g.setColor(c.getSelectionColor());
			} else {
				g.setColor(color);
			}

			rect.width = Math.max(rect.width, 1);

			int descent = c.getFontMetrics(c.getFont()).getDescent();

			drawCurvedLine(g, rect, descent);
		}

		return rect;
	}

	private void drawCurvedLine(Graphics g, Rectangle rect, int descent) {
		double x1 = rect.x;
		double xEnd = rect.x + rect.width;
		double height = 4;
		double yAvg = rect.getMaxY() - height/2;
		double phase = 4;

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setClip(rect);
		g2.setStroke(new BasicStroke(0.8f));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (double start = x1; start < xEnd; start += phase) {
			QuadCurve2D.Double upperCurve = new QuadCurve2D.Double(start, yAvg, start + phase / 4, yAvg + height / 2,
					start + phase / 2, yAvg);
			QuadCurve2D.Double lowerCurve = new QuadCurve2D.Double(start + phase / 2, yAvg, start + 3 * phase / 4,
					yAvg - height / 2, start + phase, yAvg);
			g2.draw(upperCurve);
			g2.draw(lowerCurve);
		}
		g2.dispose();
	}
}