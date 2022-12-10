package com.lp.service.csvverbrauch;

import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.errors.NoSetArticlePositionsException;

public class CsvErrorNoSetArticlePositions implements ICsvErrorAction {

	private NoSetArticlePositionsException exception;
	
	public CsvErrorNoSetArticlePositions(NoSetArticlePositionsException exception) {
		this.exception = exception;
	}

	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr("fert.verbrauchsartikel.import.error.setartikelkeinepositionen", 
				exception.getPartlistnumber());
	}

	@Override
	public Integer getLinenumber() {
		return exception.getLinenumber();
	}

}
