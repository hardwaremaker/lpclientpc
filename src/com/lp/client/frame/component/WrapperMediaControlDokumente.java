package com.lp.client.frame.component;

import java.util.Map;
import java.util.TreeMap;

import com.lp.server.system.service.MediaFac;

public class WrapperMediaControlDokumente extends WrapperMediaControl {
	private static final long serialVersionUID = -1399130537581890139L;

	public WrapperMediaControlDokumente(InternalFrame internalFrame,
			String addTitel) throws Throwable {
		super(internalFrame, addTitel);
	}

	public WrapperMediaControlDokumente(InternalFrame internalFrame,
			String addTitel, boolean bMitDefaultbildFeld) throws Throwable {
		super(internalFrame, addTitel, bMitDefaultbildFeld);
	}

	@Override
	public Map<String, String> getSelectableMimeTypes(boolean ohneText) {
		Map<String, String> tmArten = new TreeMap<String, String>();
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG,
				MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG,
				MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF,
				MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF,
				MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF,
				MediaFac.DATENFORMAT_MIMETYPE_APP_PDF);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT,
				MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_APP_JASPER,
				MediaFac.DATENFORMAT_MIMETYPE_APP_JASPER);
		return tmArten;
	}
}
