package com.lp.service.csvverbrauch;

import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.errors.NoSuchArticleException;

public class CsvErrorNoSuchArticle implements ICsvErrorAction {

	private NoSuchArticleException exception;
	
	public CsvErrorNoSuchArticle(NoSuchArticleException exception) {
		this.exception = exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr("fert.verbrauchsartikel.import.error.unbekannterartikel", 
				exception.getArticlenumber());
	}

	@Override
	public Integer getLinenumber() {
		return exception.getLinenumber();
	}

}
