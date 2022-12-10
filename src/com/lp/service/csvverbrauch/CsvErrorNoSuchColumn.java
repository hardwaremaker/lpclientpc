package com.lp.service.csvverbrauch;

import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.errors.NoSuchColumnException;

public class CsvErrorNoSuchColumn implements ICsvErrorAction {

	private NoSuchColumnException exception;
	
	public CsvErrorNoSuchColumn(NoSuchColumnException exception) {
		this.exception = exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr("fert.verbrauchsartikel.import.error.spaltenichtgefunden", 
				exception.getColumn());
	}

	@Override
	public Integer getLinenumber() {
		return exception.getLinenumber();
	}

}
