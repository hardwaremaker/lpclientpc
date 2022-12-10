package com.lp.client.util.resolution;

import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.lp.client.pc.ActualJavaVersion;
import com.lp.client.pc.SystemProperties;
import com.lp.client.util.logger.LpLogger;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * Klasse f&uuml;r Funktionen, die die DPI des Bildschirms mittel JavaFX
 * ermitteln. <br>
 * 
 * <b> ACHTUNG: </b> wenn die Anwendung &uuml;ber Remote-Desktop ausgef&uuml;hrt
 * wird k&ouml;nnen die DPI 0 sein!
 * 
 * @author Alexander Daum
 *
 */
public class DPIUtils {
	private static boolean isJavaFXInitialized = false;
	private static final boolean isJava16;
	static {
		ActualJavaVersion version = new ActualJavaVersion();
		isJava16 = version.compare(16, 0) <= 0;
	}

	/**
	 * Hole die Dpi eines Bildschirms mithilfe von JavaFX. Beim ersten Aufruf wird
	 * eventuell JavaFX intialisiert.
	 * 
	 * @param bounds
	 * @return
	 */
	private static ScreenInfo getDpiWithJavaFX(GraphicsConfiguration gConf) {
		// JavaFX muss initialisiert sein, um die Dpi holen zu koennen.
		// Dazu kann einfach ein JFXPanel instanziert werden, wenn JavaFX noch nicht
		// initialisert wurde, passiert es dann.
		if (!isJavaFXInitialized) {
			new JFXPanel();
			isJavaFXInitialized = true;
		}
		Rectangle bounds = gConf.getBounds();
		if (!SystemProperties.isMacOs() && !isJava16) {
			bounds = calculateCorrectBounds(gConf);
		}
		DpiGetter getter = new DpiGetter(new Rectangle2D(bounds.x, bounds.y, 1, 1),
				gConf.getDefaultTransform().getScaleX());
		Platform.runLater(getter);
		getter.waitFor();
		return getter.getScreenInfo();
	}

	/**
	 * Hole die DPI des Bildschirms in dem sich ein Component befindet. <br>
	 * 
	 * <b> ACHTUNG: </b> wenn die Anwendung &uuml;ber Remote-Desktop ausgef&uuml;hrt
	 * wird k&ouml;nnen die DPI 0 sein!
	 * 
	 * @param component
	 * @return
	 */
	public static ScreenInfo getDpiOfComponent(Component component) {
		return getDpiOfScreen(component.getGraphicsConfiguration());
	}

	/**
	 * 
	 * <b> ACHTUNG: </b> wenn die Anwendung &uuml;ber Remote-Desktop ausgef&uuml;hrt
	 * wird k&ouml;nnen die DPI 0 sein!
	 * 
	 * @param graphicsConfiguration
	 * @return
	 */
	public static ScreenInfo getDpiOfScreen(GraphicsConfiguration graphicsConfiguration) {
		return getDpiWithJavaFX(graphicsConfiguration);
	}

	/**
	 * Notwendig wegen Bug JDK-8211999, betrifft Windows und Linux
	 * 
	 * <br>
	 * 
	 * Durch den Bug werden die Koordinaten des Bildschirm-Ursprungs mit dem
	 * Default-Transform skaliert, was zur Folge hat, dass es zu &Uuml;berlappungen
	 * bei Bildschirmen kommt. <br>
	 * 
	 * Diese Methode multipliziert die Position wieder mit dem Skalierungsfaktor,
	 * l&auml;sst aber die Gr&ouml;&szlig;e gleich, wenn der Bildschirm im positiven
	 * Bereich ist. <br>
	 * Bei Bildschirmkoordinaten im negativen bereich, darf die Breite nicht
	 * mitskaliert werden, also muss diese zuerst dazu addiert werden, dann
	 * multipliziert, dann wieder abgezogen. <br>
	 * Dieser Code nimmt an, dass der Bildschirm nicht mit einer Nulllinie überlappt
	 * 
	 * <b> Beispiel des Bugs </b> Zum Beispiel: Linker Monitor (Hauptmonitor) bei
	 * 0/0 mit 1920x1080 <br>
	 * Rechter Monitor bei 1920/0 mit 3840x2160. <br>
	 * Wenn rechter Monitor mit 200% skaliert wird, dann werden alle Koordinaten
	 * betroffen, und der Bildschirm wird zu Rechter Monitor bei 960/0 mit 1920x1080
	 * (200%) was nat&uuml;rlich zu Problemen f&uuml;rt, da die Positionen von 960
	 * bis 1920 jetzt in 2 Bildschirmen vorkommen!
	 * 
	 * 
	 * @param screenBounds
	 * @return
	 */
	private static Rectangle calculateCorrectBounds(GraphicsConfiguration gConf) {
		Rectangle bounds = gConf.getBounds();
		AffineTransform defaultTransform = gConf.getDefaultTransform();
		if (bounds.x >= 0) {
			bounds.x *= defaultTransform.getScaleX();
		} else {
			bounds.x += bounds.width;
			bounds.x *= defaultTransform.getScaleX();
			bounds.x -= bounds.width;
		}
		if (bounds.y >= 0) {
			bounds.y *= defaultTransform.getScaleY();
		} else {
			bounds.y += bounds.height;
			bounds.y *= defaultTransform.getScaleY();
			bounds.y -= bounds.height;
		}
		return bounds;
	}

	/**
	 * Eine Klasse, die in der run Methode die Dpi eines Bildschirms holt. Die run
	 * Methode der Klasse darf nur im JavaFX Event Thread aufgerufen werden, dazu
	 * kann {@link Platform#runLater(Runnable)} verwendet werden.
	 * 
	 * @author Alexander Daum
	 *
	 */
	private static class DpiGetter implements Runnable {
		private double dpi;
		private Rectangle2D resolution;
		private double scalingFactor;

		private final Rectangle2D r2d;

		private Logger logger;
		private boolean finished = false;

		private Logger getLogger() {
			if (logger == null) {
				logger = LpLogger.getLogger(DpiGetter.class.getCanonicalName());
			}
			return logger;
		}

		/**
		 * 
		 * @param screenArea
		 * @param scalingFactor Der Skalierungsfaktor des Bildschirms. Muss hier mit
		 *                      uebergeben werden, weil die Java 8 API von Screen den
		 *                      Skalierungsfaktor noch nicht liefert, dieser aber im
		 *                      Resultat benoetigt wird
		 */
		public DpiGetter(Rectangle2D screenArea, double scalingFactor) {
			r2d = screenArea;
			this.scalingFactor = scalingFactor;
		}

		@Override
		public void run() {
			try {
				ObservableList<Screen> screens = Screen.getScreensForRectangle(r2d);

				if (screens.size() > 1) {
					getLogger().warn(
							"Warnung, mehrere Bildschirme fuer einen Bereich gefunden, verwende den ersten, DPI koennten falsch sein");
				} else if (screens.isEmpty()) {
					getLogger().warn("Warnung, keine Bildschirme mit JavaFX gefunden, benutze default DPI");
					dpi = 0;
					resolution = new Rectangle2D(0, 0, 0, 0);
					scalingFactor = 1;
					return;
				}
				Screen usedScreen = screens.get(0);
				dpi = usedScreen.getDpi();
				resolution = usedScreen.getBounds();
			} finally {
				synchronized (this) {
					finished = true;
					this.notifyAll();
				}
			}
		}

		public ScreenInfo getScreenInfo() {
			return new ScreenInfo(dpi, scalingFactor, resolution.getWidth(), resolution.getHeight());
		}

		public void waitFor() {
			synchronized (this) {
				while (!finished) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static class ScreenInfo {
		/**
		 * Die DPI des Bildschirms. <br>
		 * <b> ACHTUNG: </b> wenn die Anwendung &uuml;ber Remote-Desktop ausgef&uuml;hrt
		 * wird kann dieser Wert 0 sein!
		 */
		public final double dpi;
		public final double scalingFactor;

		/**
		 * Gr&ouml;&szlig;e des Bildschirms in logischen Pixeln. Um echte
		 * Gr&ouml;&szlig;e zu erhalten muss noch durch scalingFactor dividiert werden.
		 */
		public final double width;
		public final double height;

		public ScreenInfo(double dpi, double scalingFactor, double width, double height) {
			super();
			this.dpi = dpi;
			this.scalingFactor = scalingFactor;
			this.width = width;
			this.height = height;
		}

		@Override
		public int hashCode() {
			return Objects.hash(dpi, height, scalingFactor, width);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ScreenInfo other = (ScreenInfo) obj;
			return Double.doubleToLongBits(dpi) == Double.doubleToLongBits(other.dpi)
					&& Double.doubleToLongBits(height) == Double.doubleToLongBits(other.height)
					&& Double.doubleToLongBits(scalingFactor) == Double.doubleToLongBits(other.scalingFactor)
					&& Double.doubleToLongBits(width) == Double.doubleToLongBits(other.width);
		}

	}

}
