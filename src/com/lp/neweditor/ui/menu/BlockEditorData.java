package com.lp.neweditor.ui.menu;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.ImmutableList;
import com.lp.client.frame.delegate.DelegateFactory;

/**
 * Singleton Klasse zur verwaltung von Daten, die vom Editor ben&ouml;tigt
 * werden, z.B. Fonts, Fontgr&ouml;&szlig;en... <br>
 * Alle Daten, die vom Server geladen werden, sind lazy loading und k&ouml;nnen
 * mit {@link BlockEditorData#initDataFromServer()} asynchron vorgeladen werden
 * 
 * @author Alex
 *
 */
public class BlockEditorData {
	private static final BlockEditorData instance = new BlockEditorData();

	private List<String> systemFonts = null;
	private List<Integer> availableFontSizes = null;

	public BlockEditorData() {
		availableFontSizes = ImmutableList.of(8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72);
	}

	public List<String> getSystemFonts() {
		if (systemFonts == null) {
			// Falls concurrent Zugriffe auftreten, soll trotzdem nur ein Thread zum Server
			// gehen, daher hier locken. Wenn 2 Threads bis hier kommen, holt einer die
			// Daten von Server, der 2. muss warten. Wenn 1. Thread fertig ist, dann ist
			// systemFonts nicht mehr null und 2. Thread gibt die gleiche Liste zurueck
			synchronized (this) {
				if (systemFonts != null) {
					return systemFonts;
				}
				try {
					systemFonts = DelegateFactory.getInstance().getSystemDelegate().getServerSystemReportFonts();
					// Leere Strings entfernen
					systemFonts.removeIf(String::isEmpty);
					systemFonts = Collections.unmodifiableList(systemFonts);
				} catch (Throwable e) {
					systemFonts = Collections.emptyList();
				}
			}
		}
		return systemFonts;
	}

	public String[] getSystemFontsAsArray() {
		List<String> fonts = getSystemFonts();
		return fonts.toArray(new String[fonts.size()]);
	}

	private CompletableFuture<List<String>> getSystemFontsAsync() {
		if (systemFonts == null) {
			return CompletableFuture.supplyAsync(this::getSystemFonts);
		} else {
			return CompletableFuture.completedFuture(systemFonts);
		}
	}

	public List<Integer> getAvailableFontSizes() {
		return availableFontSizes;
	}

	public Integer[] getAvailableFontSizesAsArray() {
		List<Integer> sizes = getAvailableFontSizes();
		return sizes.toArray(new Integer[sizes.size()]);
	}

	public String[] getAvailableFontSizesAsStringArray() {
		return getAvailableFontSizes().stream().map(Object::toString).toArray(String[]::new);
	}

	public static BlockEditorData getInstance() {
		return instance;
	}

	/**
	 * Starte im Hintergrund Daten vom Server zu laden, dadurch wird der erste Start
	 * des Editors beschleunigt
	 */
	public void initDataFromServer() {
		if (systemFonts == null) {
			getSystemFontsAsync();
		}
	}

}
