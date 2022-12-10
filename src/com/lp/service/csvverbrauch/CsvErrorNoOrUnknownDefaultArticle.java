package com.lp.service.csvverbrauch;

import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.errors.NoOrUnknownDefaultArticleException;

public class CsvErrorNoOrUnknownDefaultArticle implements ICsvErrorAction {

	private NoOrUnknownDefaultArticleException exception;
	
	public CsvErrorNoOrUnknownDefaultArticle(NoOrUnknownDefaultArticleException exception) {
		this.exception = exception;
	}

	@Override
	public String getMessage() {
		if (exception.getArticlenumber() != null) {
			return LPMain.getMessageTextRespectUISPr("fert.verbrauchsartikel.import.error.defaultartikel.unbekannt", 
					exception.getArticlenumber(), exception.getParametername());
		}

		return LPMain.getMessageTextRespectUISPr("fert.verbrauchsartikel.import.error.defaultartikel.nichtdefiniert", 
				exception.getParametername());
	}

	@Override
	public Integer getLinenumber() {
		return exception.getLinenumber();
	}

}
