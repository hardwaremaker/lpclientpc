package com.lp.neweditor.common;

public enum SizeUnit {
	MM("mm", 1), CM("cm", 10), INCH("Inch", 25.4), PIXEL("Pixel", Double.NaN);

	private final double inMM;
	private final String stringRepr;

	private SizeUnit(String name, double convF) {
		inMM = convF;
		stringRepr = name;
	}

	public double getInMM() {
		return inMM;
	}

	public double getPixelsPerUnit(double dpi) {
		// DPI = px / inch
		// px/unit = px/in * in/mm * mm/unit
		// in/mm = 1/(mm/in)
		return dpi * getInMM() / INCH.getInMM();
	}

	public double getConversionFactor(SizeUnit toUnit) {
		if (toUnit == PIXEL || this == PIXEL) {
			throw new IllegalArgumentException("Kann nicht Groesse zu Pixel umrechnen, verwende getPixelsPerUnit");
		}
		return inMM / toUnit.inMM;
	}

	@Override
	public String toString() {
		return stringRepr;
	}
}
