package com.lp.neweditor.common.text;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import javax.swing.text.BadLocationException;
import javax.swing.text.GlyphView;
import javax.swing.text.ParagraphView;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import javax.swing.text.View;

import com.lp.editor.ui.LpDecoratedTextPane;

class ScaledGlyphPainter extends GlyphView.GlyphPainter {
	static ScaledGlyphPainter instance = new ScaledGlyphPainter();

	public static ScaledGlyphPainter getInstance() {
		return instance;
	}

	/**
	 * Graphics von einem BufferedImage, f&uuml;r Font Metrics
	 */
	static Graphics2D painterGr;

	static {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		painterGr = (Graphics2D) img.getGraphics();

		RenderingHints hints = new RenderingHints(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		painterGr.setRenderingHints(hints);
		painterGr.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	}

	/**
	 * Determine the span the glyphs given a start location (for tab expansion).
	 */
	public float getSpan(GlyphView v, int p0, int p1, TabExpander e, float x) {
		sync(v);
		Segment text = LpSegmentCache.getText(v, p0, p1);
		int[] justificationData = getJustificationData(v);
		float width = getTabbedTextWidth(v, text, metrics, x, e, p0, justificationData);
		LpSegmentCache.releaseSharedSegment(text);
		return (float) (width * getZoomFactor(v));
	}

	public float getHeight(GlyphView v) {
		sync(v);
		return (float) (metrics.getHeight() * getZoomFactor(v));
	}

	/**
	 * Fetches the ascent above the baseline for the glyphs corresponding to the
	 * given range in the model.
	 */
	public float getAscent(GlyphView v) {
		sync(v);
		return (float) (metrics.getAscent() * getZoomFactor(v));
	}

	/**
	 * Fetches the descent below the baseline for the glyphs corresponding to the
	 * given range in the model.
	 */
	public float getDescent(GlyphView v) {
		sync(v);
		return (float) (metrics.getDescent() * getZoomFactor(v));
	}

	/**
	 * Paints the glyphs representing the given range.
	 */
	public void paint(GlyphView v, Graphics g, Shape a, int p0, int p1) {
		sync(v);
		Segment text;
		TabExpander expander = v.getTabExpander();
		Rectangle alloc = (a instanceof Rectangle) ? (Rectangle) a : a.getBounds();

		// determine the x coordinate to render the glyphs
		int x = alloc.x;
		int p = v.getStartOffset();
		int[] justificationData = getJustificationData(v);
		if (p != p0) {
			text = LpSegmentCache.getText(v, p, p0);
			float width = getTabbedTextWidth(v, text, metrics, x, expander, p, justificationData);
			LpSegmentCache.releaseSharedSegment(text);
			x += width;
		}

		// determine the y coordinate to render the glyphs
		int y = alloc.y + metrics.getHeight() - metrics.getDescent();

		// render the glyphs
		text = LpSegmentCache.getText(v, p0, p1);
		g.setFont(metrics.getFont());

		// Hier Zoom setzen
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		double zoom = getZoomFactor(v);
		AffineTransform oldTransform = g2d.getTransform();
		g2d.scale(zoom, zoom);
		drawTabbedText(v, text, x, y, g, expander, p0, justificationData);
		g2d.setTransform(oldTransform);

		LpSegmentCache.releaseSharedSegment(text);
	}

	public Shape modelToView(GlyphView v, int pos, Position.Bias bias, Shape a) throws BadLocationException {
		sync(v);
		Rectangle alloc = (a instanceof Rectangle) ? (Rectangle) a : a.getBounds();
		System.out.println(String.format("Model to View, Model Position: %d; in Rect: %d, %d, %dx%d", pos, alloc.x,
				alloc.y, alloc.width, alloc.height));
		int p0 = v.getStartOffset();
		int p1 = v.getEndOffset();
		TabExpander expander = v.getTabExpander();
		Segment text;

		if (pos == p1) {
			// The caller of this is left to right and borders a right to
			// left view, return our end location.
			return new Rectangle(alloc.x + alloc.width, alloc.y, 0, metrics.getHeight());
		}
		if ((pos >= p0) && (pos <= p1)) {
			// determine range to the left of the position
			text = LpSegmentCache.getText(v, p0, pos);
			int[] justificationData = getJustificationData(v);
			int width = (int) getTabbedTextWidth(v, text, metrics, alloc.x, expander, p0, justificationData);
			LpSegmentCache.releaseSharedSegment(text);
			width *= getZoomFactor(v);
			return new Rectangle(alloc.x + width, alloc.y, 0, metrics.getHeight());
		}
		throw new BadLocationException("modelToView - can't convert", p1);
	}

	/**
	 * Provides a mapping from the view coordinate space to the logical coordinate
	 * space of the model.
	 *
	 * @param v          the view containing the view coordinates
	 * @param x          the X coordinate
	 * @param y          the Y coordinate
	 * @param a          the allocated region to render into
	 * @param biasReturn always returns <code>Position.Bias.Forward</code> as the
	 *                   zero-th element of this array
	 * @return the location within the model that best represents the given point in
	 *         the view
	 * @see View#viewToModel
	 */
	public int viewToModel(GlyphView v, float x, float y, Shape a, Position.Bias[] biasReturn) {
		sync(v);
		Rectangle alloc = a.getBounds();
		divideRectByZoom(alloc, getZoomFactor(v));
		x /= getZoomFactor(v);
		y /= getZoomFactor(v);
		int p0 = v.getStartOffset();
		int p1 = v.getEndOffset();
		TabExpander expander = v.getTabExpander();
		Segment text = LpSegmentCache.getText(v, p0, p1);
		int[] justificationData = getJustificationData(v);
		int offs = getTabbedTextOffset(v, text, metrics, alloc.x, (int) x, expander, p0, true, justificationData);
		LpSegmentCache.releaseSharedSegment(text);
		int retValue = p0 + offs;
		if (retValue == p1) {
			// No need to return backward bias as GlyphPainter1 is used for
			// ltr text only.
			retValue--;
		}
		biasReturn[0] = Position.Bias.Forward;
		return retValue;
	}

	/**
	 * Determines the best location (in the model) to break the given view. This
	 * method attempts to break on a whitespace location. If a whitespace location
	 * can't be found, the nearest character location is returned.
	 *
	 * @param v   the view
	 * @param p0  the location in the model where the fragment should start its
	 *            representation >= 0
	 * @param x   the graphic location along the axis that the broken view would
	 *            occupy >= 0; this may be useful for things like tab calculations
	 * @param len specifies the distance into the view where a potential break is
	 *            desired >= 0
	 * @return the model location desired for a break
	 * @see View#breakView
	 */
	public int getBoundedPosition(GlyphView v, int p0, float x, float len) {
		sync(v);
		TabExpander expander = v.getTabExpander();
		Segment s = LpSegmentCache.getText(v, p0, v.getEndOffset());
		int[] justificationData = getJustificationData(v);
		int index = getTabbedTextOffset(v, s, metrics, (int) x, (int) (x + len), expander, p0, false,
				justificationData);
		int p1 = p0 + index;
		LpSegmentCache.releaseSharedSegment(s);
		return p1;
	}

	void sync(GlyphView v) {
		Font f = v.getFont();
		if ((metrics == null) || (!f.equals(metrics.getFont()))) {

			// fetch a new FontMetrics
			// Der "Trick" liegt darin, dass graphics2d.getFontMetrics() FontMetrics
			// mit float Werten zurueckliefert.
			metrics = painterGr.getFontMetrics(f);
		}
	}

	private int[] getJustificationData(GlyphView v) {
		View parent = v.getParent();
		int[] ret = null;

		// use reflection to get the data
		Class<?> pClass = parent.getClass();
		if (pClass.isAssignableFrom(ParagraphView.class.getDeclaredClasses()[0])) { // if (parent instanceof
																					// ParagraphView.Row) {
			try {
				Field f = pClass.getDeclaredField("justificationData");
				if (f != null) {
					f.setAccessible(true);
					ret = (int[]) f.get(parent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return ret;
	}

	// --- variables ---------------------------------------------

	FontMetrics metrics;
	static char[] SPACE_CHAR = new char[] { ' ' };

	float getTabbedTextWidth(View view, Segment s, FontMetrics metrics, float x, TabExpander e, int startOffset,
			int[] justificationData) {
		float nextX = x;
		char[] txt = s.array;
		String txtStr = new String(txt);
		int txtOffset = s.offset;
		int n = s.offset + s.count;
		int charCount = 0;
		int spaceAddon = 0;
		int spaceAddonLeftoverEnd = -1;
		int startJustifiableContent = 0;
		int endJustifiableContent = 0;
		if (justificationData != null) {
			int offset = -startOffset + txtOffset;
			View parent = null;
			if (view != null && (parent = view.getParent()) != null) {
				offset += parent.getStartOffset();
			}
			spaceAddon = justificationData[0];
			spaceAddonLeftoverEnd = justificationData[1] + offset;
			startJustifiableContent = justificationData[2] + offset;
			endJustifiableContent = justificationData[3] + offset;
		}

		for (int i = txtOffset; i < n; i++) {
			if (txt[i] == '\t' || ((spaceAddon != 0 || i <= spaceAddonLeftoverEnd) && (txt[i] == ' ')
					&& startJustifiableContent <= i && i <= endJustifiableContent)) {
				nextX += metrics.getStringBounds(txtStr, i - charCount, i, painterGr).getWidth();
				charCount = 0;
				if (txt[i] == '\t') {
					if (e != null) {
						nextX = e.nextTabStop((float) nextX, startOffset + i - txtOffset);
					} else {
						nextX += metrics.getStringBounds(SPACE_CHAR, 0, 1, painterGr).getWidth();
						nextX = (int) nextX;
					}
				} else if (txt[i] == ' ') {
					nextX += metrics.getStringBounds(SPACE_CHAR, 0, 1, painterGr).getWidth() + spaceAddon;
					nextX = (int) nextX;
					if (i <= spaceAddonLeftoverEnd) {
						nextX++;
					}
				}
			} else if (txt[i] == '\n') {
				// Ignore newlines, they take up space and we shouldn't be
				// counting them.
				nextX += (float) metrics.getStringBounds(txtStr, i - charCount, i, painterGr).getWidth();
				charCount = 0;
			} else {
				charCount++;
			}
		}

		nextX += metrics.getStringBounds(txtStr, n - charCount, n, painterGr).getWidth();
		return nextX - x;
	}

	float drawTabbedText(View view, Segment s, float x, float y, Graphics g, TabExpander e, int startOffset,
			int[] justificationData) {
		float nextX = x;
		char[] txt = s.array;
		String txtStr = new String(txt);
		int txtOffset = s.offset;
		int flushLen = 0;
		int flushIndex = s.offset;
		int spaceAddon = 0;
		int spaceAddonLeftoverEnd = -1;
		int startJustifiableContent = 0;
		int endJustifiableContent = 0;
		if (justificationData != null) {
			int offset = -startOffset + txtOffset;
			View parent = null;
			if (view != null && (parent = view.getParent()) != null) {
				offset += parent.getStartOffset();
			}
			spaceAddon = justificationData[0];
			spaceAddonLeftoverEnd = justificationData[1] + offset;
			startJustifiableContent = justificationData[2] + offset;
			endJustifiableContent = justificationData[3] + offset;
		}
		int n = s.offset + s.count;
		for (int i = txtOffset; i < n; i++) {
			if (txt[i] == '\t' || ((spaceAddon != 0 || i <= spaceAddonLeftoverEnd) && (txt[i] == ' ')
					&& startJustifiableContent <= i && i <= endJustifiableContent)) {
				if (flushLen > 0) {
					((Graphics2D) g).drawString(txtStr.substring(flushIndex, flushIndex + flushLen), x, y);
					// corrected position
					nextX += metrics.getStringBounds(txtStr, flushIndex, flushIndex + flushLen, painterGr).getWidth();
					flushLen = 0;
				}
				flushIndex = i + 1;
				if (txt[i] == '\t') {
					if (e != null) {
						nextX = e.nextTabStop((float) nextX, startOffset + i - txtOffset);
					} else {
						nextX += (float) metrics.getStringBounds(SPACE_CHAR, 0, 1, painterGr).getWidth();
						nextX = (int) nextX;
					}
				} else if (txt[i] == ' ') {
					nextX += (float) metrics.getStringBounds(SPACE_CHAR, 0, 1, painterGr).getWidth() + spaceAddon;
					if (i <= spaceAddonLeftoverEnd) {
						nextX++;
					}
				}
				x = nextX;
			} else if ((txt[i] == '\n') || (txt[i] == '\r')) {
				if (flushLen > 0) {
					((Graphics2D) g).drawString(txtStr.substring(flushIndex, flushIndex + flushLen), x, y);
					// corrected
					nextX += metrics.getStringBounds(txtStr, flushIndex, flushIndex + flushLen, painterGr).getWidth();
					flushLen = 0;
				}
				flushIndex = i + 1;
				x = nextX;
			} else {
				flushLen += 1;
			}
		}
		if (flushLen > 0) {
			((Graphics2D) g).drawString(txtStr.substring(flushIndex, flushIndex + flushLen), x, y);
			// corrected
			nextX += metrics.getStringBounds(txtStr, flushIndex, flushIndex + flushLen, painterGr).getWidth();
		}
		return nextX;
	}

	int getTabbedTextOffset(View view, Segment s, FontMetrics metrics, int x0, int x, TabExpander e, int startOffset,
			boolean round, int[] justificationData) {
		if (x0 >= x) {
			// x before x0, return.
			return 0;
		}
		float currX = x0;
		float nextX = currX;
		// s may be a shared segment, so it is copied prior to calling
		// the tab expander
		char[] txt = s.array;
		int txtOffset = s.offset;
		int txtCount = s.count;
		int spaceAddon = 0;
		int spaceAddonLeftoverEnd = -1;
		int startJustifiableContent = 0;
		int endJustifiableContent = 0;
		if (justificationData != null) {
			int offset = -startOffset + txtOffset;
			View parent = null;
			if (view != null && (parent = view.getParent()) != null) {
				offset += parent.getStartOffset();
			}
			spaceAddon = justificationData[0];
			spaceAddonLeftoverEnd = justificationData[1] + offset;
			startJustifiableContent = justificationData[2] + offset;
			endJustifiableContent = justificationData[3] + offset;
		}
		int n = s.offset + s.count;
		for (int i = s.offset; i < n; i++) {
			if (txt[i] == '\t' || ((spaceAddon != 0 || i <= spaceAddonLeftoverEnd) && (txt[i] == ' ')
					&& startJustifiableContent <= i && i <= endJustifiableContent)) {
				if (txt[i] == '\t') {
					if (e != null) {
						nextX = (int) e.nextTabStop((float) nextX, startOffset + i - txtOffset);
					} else {
						nextX += metrics.getStringBounds(SPACE_CHAR, 0, 1, painterGr).getWidth();
					}
				} else if (txt[i] == ' ') {
					nextX += metrics.getStringBounds(SPACE_CHAR, 0, 1, painterGr).getWidth() + spaceAddon;
					nextX = (int) nextX;

					if (i <= spaceAddonLeftoverEnd) {
						nextX++;
					}
				}
			} else {
				nextX += metrics.getStringBounds(txt, i, i + 1, painterGr).getWidth();
			}
			if ((x >= currX) && (x < nextX)) {
				// found the hit position... return the appropriate side
				if ((round == false) || ((x - currX) < (nextX - x))) {
					return i - txtOffset;
				} else {
					return i + 1 - txtOffset;
				}
			}
			currX = nextX;
		}

		// didn't find, return end offset
		return txtCount;
	}

	private double getZoomFactor(GlyphView v) {
		Object prop = v.getDocument().getProperty(LpDecoratedTextPane.ZoomfactorProperty);
		double zoom = 1.0;
		if (prop != null) {
			if (prop instanceof Number) {
				zoom = ((Number) prop).floatValue();
			}
		}
		return zoom;
	}

	private int multiplyByZoom(int x, double zoom) {
		return (int) Math.round(x * zoom);
	}

	private int divideByZoom(int x, double zoom) {
		return (int) Math.round(x / zoom);
	}

	private Rectangle zoomedMultRectangle(int x, int y, int width, int height, double zoom) {
		return new Rectangle(multiplyByZoom(x, zoom), multiplyByZoom(y, zoom), multiplyByZoom(width, zoom),
				multiplyByZoom(height, zoom));
	}

	/**
	 * Achtung: modifiziert Parameter
	 * 
	 * @param rect
	 * @param zoom
	 */
	private void divideRectByZoom(Rectangle rect, double zoom) {
		rect.x = divideByZoom(rect.x, zoom);
		rect.y = divideByZoom(rect.y, zoom);
		rect.width = divideByZoom(rect.width, zoom);
		rect.height = divideByZoom(rect.height, zoom);
	}
}