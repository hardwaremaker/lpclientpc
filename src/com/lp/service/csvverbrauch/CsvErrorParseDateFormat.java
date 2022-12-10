package com.lp.service.csvverbrauch;

import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.errors.ParseDateFormatException;

public class CsvErrorParseDateFormat implements ICsvErrorAction {

	private ParseDateFormatException exception;
	
	public CsvErrorParseDateFormat(ParseDateFormatException exception) {
		this.exception = exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr("fert.verbrauchsartikel.import.error.datumsformat", 
				exception.getValue(), exception.getColumn(), exception.getFormat());
	}

	@Override
	public Integer getLinenumber() {
		return exception.getLinenumber();
	}

}
