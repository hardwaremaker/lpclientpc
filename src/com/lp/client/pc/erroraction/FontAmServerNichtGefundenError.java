package com.lp.client.pc.erroraction;

import com.lp.client.pc.LPMain;
import com.lp.util.FontNichtGefundenException;

public class FontAmServerNichtGefundenError extends FontNichtGefundenError {

	public FontAmServerNichtGefundenError() {
	}

	@Override
	protected void appendMainErrorMessage(StringBuilder msgBuilder, FontNichtGefundenException exc) {
		msgBuilder.append(LPMain.getMessageTextRespectUISPr("lp.report.error.fontnichtgefunden.server", exc.getFontName()));
	}
}
