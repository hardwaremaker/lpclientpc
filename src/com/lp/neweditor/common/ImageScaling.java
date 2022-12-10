package com.lp.neweditor.common;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;

import org.apache.poi.util.Dimension2DDouble;

import com.google.common.collect.ImmutableList;

import net.sf.cglib.proxy.NoOp;

/**
 * Klasse zum skalieren von Dimensions, wird verwendet um verschiedene Methoden
 * zur erfassung von Bildgr&ouml;&szlig;e zu implementieren <br>
 * Alle Implementierungen von {@link ImageScaling} sind Immutable<br>
 * Die skalierungen sind verkettbar mit
 * {@link ImageScaling#andThenDo(ImageScaling)} <br>
 * Alle Skalierungen rechnen intern mit <code>double</code> und es wird erst am
 * Ende auf eine {@link Dimension} mit <code>int</code> umgerechnet
 * 
 * @author Alexander Daum
 *
 */
public abstract class ImageScaling {

	/**
	 * No-op Skalierung, gibt die gleiche Dimension wieder zur&uuml;ck (falls die
	 * {@link Dimension2D} in scaleSize keine ganzzahligen Werte hat, werden diese
	 * gerundet) <br>
	 * Diese Konstante ist relativeScaling(1.0) vorzuziehen, wenn keine Skalierung
	 * gewollt ist
	 */
	public static final ImageScaling noScaling = new NoOpScaling();

	protected abstract Dimension2D scale(Dimension2D originalSize);

	/**
	 * Skaliert eine Dimension. Da {@link Dimension2D} auch double Werte haben kann,
	 * wird hier eventuell gerundet (auch wenn ein No-op scaling oder Skalierung um
	 * 100% passiert)
	 * 
	 * @param originalSize
	 * @return
	 */
	public Dimension scaleSize(Dimension2D originalSize) {
		Dimension2D scaled = scale(originalSize);
		return new Dimension((int) Math.round(scaled.getWidth()), (int) Math.round(scaled.getHeight()));
	}

	/**
	 * Skalierung auf bestimmte Gr&ouml;&szlig;e in Pixel
	 * 
	 * @param targetWidth
	 * @param targetHeight
	 * @return
	 */
	public static ImageScaling toPixelSize(int targetWidth, int targetHeight) {
		return new AbsoluteScaling(SizeUnit.PIXEL, 1, targetWidth, targetHeight);
	}

	/**
	 * Skaliere auf bestimmte Gr&ouml;&szlig;e in einer Einheit mit bestimmten DPI
	 * (Pixel Gr&ouml;&szlig;e = dpi * Gr&ouml;&szlig;e in Inch)
	 * 
	 * @param targetWidth
	 * @param targetHeight
	 * @param unit
	 * @param dpi
	 * @return
	 */
	public static ImageScaling toSizeWithDPI(double targetWidth, double targetHeight, SizeUnit unit, double dpi) {
		double dpmm = dpi / SizeUnit.INCH.getInMM();
		return new AbsoluteScaling(unit, dpmm, targetWidth, targetHeight);
	}

	/**
	 * Skaliere um Faktor in beide Richtungen
	 * 
	 * @param scalingFactor
	 * @return
	 */
	public static ImageScaling relativeScaling(double scalingFactor) {
		return new RelativeScaling(scalingFactor, scalingFactor);
	}

	/**
	 * Skaliere um getrennten Faktor in X / Y Richtung
	 * 
	 * @param scalingX
	 * @param scalingY
	 * @return
	 */
	public static ImageScaling relativeScaling(double scalingX, double scalingY) {
		return new RelativeScaling(scalingX, scalingY);
	}

	/**
	 * Verkettet diese Skalierung mit einer anderen und gibt ein neues
	 * {@link ImageScaling} Objekt zur&uuml;ck, das alle skalierungen
	 * durchf&uuml;hrt <br>
	 * Diese Methode gibt <b>nicht</b> garantiert ein neues Objekt zur&uuml;ck. Bei
	 * manchen Argumenten wird optimiert (wenn eines der Objekte noop ist, oder das
	 * Ziel eine fixe Gr&ouml;&szlig; ist, etc.)
	 * 
	 * @param other
	 * @return
	 */
	public ImageScaling andThenDo(ImageScaling other) {
		if (other == noScaling) {
			return this;
		}
		if (other.getClass() == AbsoluteScaling.class) {
			return other;
		}
		return new ConcatScaling(this, other);
	}

	private static final class NoOpScaling extends ImageScaling {
		@Override
		public Dimension2D scale(Dimension2D originalSize) {
			return originalSize;
		}

		@Override
		public ImageScaling andThenDo(ImageScaling other) {
			return other;
		}

		@Override
		public String toString() {
			return "No Scaling";
		}

		@Override
		public boolean equals(Object obj) {
			if(obj == null) {
				return false;
			}
			return obj.getClass() == NoOpScaling.class;
		}
	}

	/**
	 * Implementierung zur Skalierung auf eine bestimmte Gr&ouml;&szlig;e, entweder
	 * Pixel oder Abmessung mit DPI
	 * 
	 * @author Alexander Daum
	 *
	 */
	public static class AbsoluteScaling extends ImageScaling {
		private final double targetWidth, targetHeight;

		public AbsoluteScaling(SizeUnit unit, double dpmm, double targetWidth, double targetHeight) {
			if (unit == SizeUnit.PIXEL) {
				this.targetWidth = targetWidth;
				this.targetHeight = targetHeight;
			} else {
				this.targetWidth = targetWidth * unit.getInMM() * dpmm;
				this.targetHeight = targetHeight * unit.getInMM() * dpmm;
			}
		}

		@Override
		public Dimension2D scale(Dimension2D originalSize) {
			return new Dimension2DDouble(targetWidth, targetHeight);
		}

		@Override
		public String toString() {
			return String.format("Absolute Scaling to (%.0fpx x %.0fpx)", targetWidth, targetHeight);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			long temp;
			temp = Double.doubleToLongBits(targetHeight);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(targetWidth);
			result = prime * result + (int) (temp ^ (temp >>> 32));
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
			AbsoluteScaling other = (AbsoluteScaling) obj;
			if (Double.doubleToLongBits(targetHeight) != Double.doubleToLongBits(other.targetHeight))
				return false;
			if (Double.doubleToLongBits(targetWidth) != Double.doubleToLongBits(other.targetWidth))
				return false;
			return true;
		}

	}

	/**
	 * Implementierung zur Skalierung um einen Faktor in X und Y richtung
	 * 
	 * @author Alexander Daum
	 *
	 */
	public static class RelativeScaling extends ImageScaling {
		private final double scaleX, scaleY;

		@Override
		public Dimension2D scale(Dimension2D originalSize) {
			return new Dimension2DDouble(scaleX * originalSize.getWidth(), scaleY * originalSize.getHeight());
		}

		public RelativeScaling(double scaleX, double scaleY) {
			this.scaleX = scaleX;
			this.scaleY = scaleY;
		}

		@Override
		public String toString() {
			return String.format("Relative Scaling by (%.0f%%, %.0f%%)", 100 * scaleX, 100 * scaleY);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			long temp;
			temp = Double.doubleToLongBits(scaleX);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(scaleY);
			result = prime * result + (int) (temp ^ (temp >>> 32));
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
			RelativeScaling other = (RelativeScaling) obj;
			if (Double.doubleToLongBits(scaleX) != Double.doubleToLongBits(other.scaleX))
				return false;
			if (Double.doubleToLongBits(scaleY) != Double.doubleToLongBits(other.scaleY))
				return false;
			return true;
		}

	}

	/**
	 * Implementierung zur verkettung mehrerer Skalierungsoperationen. Ruft alle
	 * skalierungen der Reihe nach auf
	 * 
	 * @author Alexander Daum
	 *
	 */
	public static class ConcatScaling extends ImageScaling {
		private final ImmutableList<ImageScaling> scalings;

		/**
		 * Erstellt ein neues ConcatScaling.
		 * 
		 * @param scalings
		 */
		public ConcatScaling(ImageScaling... scalings) {
			ImmutableList.Builder<ImageScaling> builder = ImmutableList.builder();
			for (ImageScaling sc : scalings) {
				// Optimierung fuer lange verkettungen, so wird verschachtelung reduziert.
				// ImageScalings sind Immutable, also ist das auch kein Problem
				if (sc.getClass() == ConcatScaling.class) {
					builder.addAll(((ConcatScaling) sc).scalings);
				} else {
					builder.add(sc);
				}
			}
			this.scalings = builder.build();
		}

		@Override
		protected Dimension2D scale(Dimension2D originalSize) {
			Dimension2D working = originalSize;
			for (ImageScaling scaler : scalings) {
				working = scaler.scale(working);
			}
			return working;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(scalings.size());
			sb.append(" concatenated Scalings: [");
			boolean first = true;
			for (ImageScaling sc : scalings) {
				if (!first) {
					sb.append(", ");
				}
				first = false;
				sb.append(sc.toString());
			}
			sb.append("]");
			return sb.toString();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((scalings == null) ? 0 : scalings.hashCode());
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
			ConcatScaling other = (ConcatScaling) obj;
			if (scalings == null) {
				if (other.scalings != null)
					return false;
			} else if (!scalings.equals(other.scalings))
				return false;
			return true;
		}

	}
}
