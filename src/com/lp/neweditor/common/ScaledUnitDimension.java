package com.lp.neweditor.common;

import java.awt.Dimension;

/**
 * Klasse um Dimension mit Einheit, DPI und Zoomfaktor zu Pixel umzuwandeln
 * 
 * @author Alexander Daum
 *
 */
public class ScaledUnitDimension {
	private static final double INCH_MM = 25.4;
	private final double dot_per_mm;
	private EditorZoomFaktor zoomFaktor;
	private double pixWidth, pixHeight;

	/**
	 * Create a new UnitDimension with (0,0) and dpi
	 * 
	 * @param dpi
	 */
	public ScaledUnitDimension(double dpi) {
		this.dot_per_mm = dpi / INCH_MM;
		pixWidth = 0;
		pixHeight = 0;
		zoomFaktor = new EditorZoomFaktor(100);
	}

	public ScaledUnitDimension(double width, double height, SizeUnit unit, double dpi) {
		this(dpi);
		setWidth(width, unit);
		setHeight(height, unit);
	}

	/**
	 * get as Dimension in pixels
	 * 
	 * @return
	 */
	public Dimension getAsPixels() {
		return new Dimension(getWidth(), getHeight());
	}

	/**
	 * get as Dimension in mm, according to dpi
	 * 
	 * @return
	 */
	public Dimension getAsMM() {
		return new Dimension(getWidthMM(), getHeightMM());
	}

	/**
	 * Width in Pixel
	 * 
	 * @return
	 */
	public int getWidth() {
		return zoomFaktor.scaleInt((int) pixWidth);
	}

	/**
	 * Height in Pixel
	 * 
	 * @return
	 */
	public int getHeight() {
		return zoomFaktor.scaleInt((int) pixHeight);
	}

	/**
	 * Width in mm, according to DPI
	 * 
	 * @return
	 */
	public int getWidthMM() {
		return (int) Math.round(pixWidth / dot_per_mm);
	}

	/**
	 * Height in mm, according to DPI
	 * 
	 * @return
	 */
	public int getHeightMM() {
		return (int) Math.round(pixHeight / dot_per_mm);
	}

	public void setWidth(double width, SizeUnit unit) {
		if (unit == SizeUnit.PIXEL) {
			// Pixel is direct
			pixWidth = width;
		} else {
			double inMM = ((double) width) * unit.getInMM();
			pixWidth = (int) Math.round(inMM * dot_per_mm);
		}
	}

	public void setHeight(double height, SizeUnit unit) {
		if (unit == SizeUnit.PIXEL) {
			// Pixel is direct
			pixHeight = height;
		} else {
			double inMM = ((double) height) * unit.getInMM();
			pixHeight = (int) Math.round(inMM * dot_per_mm);
		}
	}

	public void setDimension(Dimension dimension, SizeUnit unit) {
		setWidth(dimension.getWidth(), unit);
		setHeight(dimension.getHeight(), unit);
	}

	public EditorZoomFaktor getZoomFaktor() {
		return zoomFaktor;
	}

	public void setZoomFaktor(EditorZoomFaktor zoomFaktor) {
		this.zoomFaktor = zoomFaktor;
	}
}
