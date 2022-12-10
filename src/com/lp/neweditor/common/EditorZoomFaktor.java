package com.lp.neweditor.common;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;

import org.apache.poi.util.Dimension2DDouble;

/**
 * Klasse um einen ZoomFaktor in % darzustellen. toString Methode gibt Wert in %
 * aus. <br>
 * Diese Klasse ist Immutable und implementiert {@link ImageScaling}
 * 
 * @author Alexander Daum
 *
 */
public class EditorZoomFaktor extends ImageScaling {
	private final int percentValue;

	public EditorZoomFaktor(int percentValue) {
		this.percentValue = percentValue;
	}

	public int getPercentValue() {
		return percentValue;
	}

	public double getFactor() {
		return (double) percentValue / 100.0;
	}

	public int scaleInt(int orig) {
		return (int) Math.round(orig * getFactor());
	}

	@Override
	public String toString() {
		return Integer.toString(percentValue) + '%';
	}

	public static EditorZoomFaktor parseZoomFaktor(String str) {
		if (str.endsWith("%")) {
			str = str.substring(0, str.length() - 1);
		}
		return new EditorZoomFaktor(Integer.parseInt(str));
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(percentValue);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EditorZoomFaktor other = (EditorZoomFaktor) obj;
		if (percentValue != other.percentValue)
			return false;
		return true;
	}

	@Override
	protected Dimension2D scale(Dimension2D originalSize) {
		return new Dimension2DDouble(getFactor() * originalSize.getWidth(), getFactor() * originalSize.getHeight());
	}
}
