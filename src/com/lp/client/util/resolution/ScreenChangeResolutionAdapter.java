package com.lp.client.util.resolution;

import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.CompletableFuture;

import com.lp.client.util.resolution.DPIUtils.ScreenInfo;

/**
 * Eine Klasse, die auf einem Component installiert werden kann und die danach
 * die DPI des Bildschirms als PropertyChangeEvent zur verfuegung stellt
 * 
 * @author Alexander Daum
 *
 */
public class ScreenChangeResolutionAdapter {
	/**
	 * Diese Propert wird bei allen &Auml;nderungen der Bildschriminformationen dpi,
	 * Aufl&ouml;sung und Skalierungsfaktor verursacht <br>
	 * Typ: {@link ScreenInfo}
	 */
	public static final String PROPERTY_SCREEN_INFO = "screen_info";

	public static final String PROPERTY_DPI = "dpi";
	public static final String PROPERTY_WIDTH = "width";
	public static final String PROPERTY_HEIGHT = "height";
	public static final String PROPERTY_SCALING = "scaling_factor";

	private PropertyChangeSupport changeSupport;

	private CachedValue lastValue = new CachedValue(new ScreenInfo(0, 0, 0, 0), 0, 0, 0);

	public ScreenChangeResolutionAdapter() {
		changeSupport = new PropertyChangeSupport(this);
	}

	public static ScreenChangeResolutionAdapter createForComponent(Component comp) {
		ScreenChangeResolutionAdapter resAdapter = new ScreenChangeResolutionAdapter();
		comp.addHierarchyBoundsListener(resAdapter.new CompListener());
		comp.addHierarchyListener(resAdapter.new CompListener());
		return resAdapter;
	}

	public void addPropertyChangeListener(String prop, PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(prop, listener);
	}

	private void computeNewValues(Component comp) {
		GraphicsConfiguration graphicsConfiguration = comp.getGraphicsConfiguration();
		if (graphicsConfiguration == null) {
			return;
		}
		Rectangle screenBounds = graphicsConfiguration.getBounds();
		CompletableFuture<ScreenInfo> infoFuture = CompletableFuture.supplyAsync(() -> DPIUtils.getDpiOfComponent(comp));
		infoFuture.thenAccept(info -> {
			synchronized (ScreenChangeResolutionAdapter.this) {
				CachedValue newValue = new CachedValue(info, System.currentTimeMillis(), screenBounds.x,
						screenBounds.y);
				if (lastValue.isDataDifferent(info)) {
					fireChangeEvents(newValue);
				}
				lastValue = newValue;
			}
		});
	}

	private void fireChangeEvents(CachedValue newValue) {
		changeSupport.firePropertyChange(PROPERTY_SCREEN_INFO, lastValue.screenInfo, newValue.screenInfo);
		if (newValue.screenInfo.dpi != lastValue.screenInfo.dpi) {
			changeSupport.firePropertyChange(PROPERTY_DPI, lastValue.screenInfo.dpi, newValue.screenInfo.dpi);
		}
		if (newValue.screenInfo.height != lastValue.screenInfo.height) {
			changeSupport.firePropertyChange(PROPERTY_HEIGHT, lastValue.screenInfo.height, newValue.screenInfo.height);
		}
		if (newValue.screenInfo.width != lastValue.screenInfo.width) {
			changeSupport.firePropertyChange(PROPERTY_WIDTH, lastValue.screenInfo.width, newValue.screenInfo.width);
		}
		if (newValue.screenInfo.scalingFactor != lastValue.screenInfo.scalingFactor) {
			changeSupport.firePropertyChange(PROPERTY_SCALING, lastValue.screenInfo.scalingFactor,
					newValue.screenInfo.scalingFactor);
		}
	}

	private class CompListener implements HierarchyListener, HierarchyBoundsListener {

		@Override
		public void ancestorMoved(java.awt.event.HierarchyEvent e) {
			Component comp = e.getChanged();
			ancestorMovedOrResized(comp);
		}

		@Override
		public void hierarchyChanged(HierarchyEvent e) {
			if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0) {
				// Parent changed, get the Frame and then compute new values
				Component comp = e.getComponent();
				if (comp != null) {
					computeNewValues(comp);
				}
			}
		}

		@Override
		public void ancestorResized(HierarchyEvent e) {
			Component comp = e.getChanged();
			ancestorMovedOrResized(comp);
		};

		private void ancestorMovedOrResized(Component ancestor) {
			if (!(ancestor instanceof Window)) {
				return;
			}
			Rectangle screenBounds = ancestor.getGraphicsConfiguration().getBounds();
			if (!lastValue.isValid(screenBounds.x, screenBounds.y)) {
				computeNewValues(ancestor);
			}
		}
	}

	private static class CachedValue {
		/**
		 * Wie lange ein gecachter Wert gueltig bleibt.
		 * 
		 * Darf nicht zu lange sein, sonst wird die DPI bei aenderungen der Bildschirme
		 * nicht korrekt berechnet. Wenn die Zeit zu kurz ist, wird die aktuelle DPI
		 * Zahl zu oft geholt und das verschieben des Fensters kann ruckeln
		 */
		private static final long CACHE_VALID_MS = 10_000;
		// The time of creation
		private final long timestamp;

		// The position of the Screen
		private final int x, y;

		// Values start here
		private final ScreenInfo screenInfo;

		public CachedValue(ScreenInfo info, long timestamp, int x, int y) {
			this.x = x;
			this.y = y;
			this.screenInfo = info;
			this.timestamp = timestamp;
		}

		/**
		 * Pruefe ob diese gecachten Werte fuer den Bildschirm mit Koordinaten x und y
		 * noch gueltig sind
		 */
		public boolean isValid(int x, int y) {
			return this.x == x && this.y == y && System.currentTimeMillis() <= (timestamp + CACHE_VALID_MS);
		}

		public boolean isDataDifferent(ScreenInfo otherData) {
			return !screenInfo.equals(otherData);
		}

	}
}
