package com.lp.neweditor.common;


import org.apache.log4j.Logger;

import com.lp.client.frame.Defaults;
import com.lp.server.util.logger.LpLogger;

public class EditorDefaults {
	private static Logger logger = LpLogger.getLogger(EditorDefaults.class);
	public static String getDefaultFont() {
		String font = "Arial";
		try {
			font = Defaults.getInstance().getEditorFont();
		} catch (Throwable t) {
			logger.warn("Error while getting Default editor Font", t);
		}
		if (font == null) {
			font = "Arial";
		}
		return font;
	}

	public static int getDefaultFontSize() {
		int fontSize = 12;
		try {
			Integer size = Defaults.getInstance().getEditorFontSize();
			fontSize = size == null ? 12 : size;
		} catch (Throwable t) {
			logger.warn("Error while getting Default editor Font size", t);
		}
		return fontSize;
	}

	public static EditorZoomFaktor getDefaultZoomFaktor() {
		EditorZoomFaktor zf = new EditorZoomFaktor(100);
		try {
			String zoomStr = Defaults.getInstance().getEditorZoom();
			if (zoomStr != null) {
				zf = EditorZoomFaktor.parseZoomFaktor(zoomStr);
			}
		} catch (Throwable e) {
			logger.warn("Error while getting Default editor ZoomFaktor", e);
		}
		return zf;
	}
}
