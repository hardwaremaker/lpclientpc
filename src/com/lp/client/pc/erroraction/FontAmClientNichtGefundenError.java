package com.lp.client.pc.erroraction;

import com.lp.client.pc.LPMain;
import com.lp.util.FontNichtGefundenException;

public class FontAmClientNichtGefundenError extends FontNichtGefundenError {

	public FontAmClientNichtGefundenError() {
	}

	protected void appendMainErrorMessage(StringBuilder msgBuilder, FontNichtGefundenException exc) {
		msgBuilder.append(LPMain.getMessageTextRespectUISPr("lp.report.error.fontnichtgefunden.client", exc.getFontName()));
	}
	
}
