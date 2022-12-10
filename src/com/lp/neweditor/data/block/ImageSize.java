package com.lp.neweditor.data.block;

import com.lp.neweditor.common.ImageScaling;
import com.lp.neweditor.common.SizeUnit;

/**
 * Klasse, die die Gr&ouml;&szlig;e eines Bildes repr&auml;sentiert. <br>
 * Groesse ist hier immer in Millimeter
 * 
 * @author Alexander Daum
 *
 */
public class ImageSize {
	public final float width;

	public final float height;

	public ImageSize(double width, double height, SizeUnit unit) {
		this.width = (float) (width * unit.getConversionFactor(SizeUnit.MM));
		this.height = (float) (height * unit.getConversionFactor(SizeUnit.MM));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(height);
		result = prime * result + Float.floatToIntBits(width);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageSize other = (ImageSize) obj;
		if (Float.floatToIntBits(height) != Float.floatToIntBits(other.height))
			return false;
		if (Float.floatToIntBits(width) != Float.floatToIntBits(other.width))
			return false;
		return true;
	}

	public ImageScaling toScaling(double dpi) {
		return ImageScaling.toSizeWithDPI(width, height, SizeUnit.MM, dpi);
	}

}
