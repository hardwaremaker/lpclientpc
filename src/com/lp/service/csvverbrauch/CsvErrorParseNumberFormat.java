package com.lp.service.csvverbrauch;

import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.errors.ParseNumberFormatException;

public class CsvErrorParseNumberFormat implements ICsvErrorAction {

	private ParseNumberFormatException exception;
	
	public CsvErrorParseNumberFormat(ParseNumberFormatException exception) {
		this.exception = exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr("fert.verbrauchsartikel.import.error.zahlenformat", 
				exception.getValue(), exception.getColumn());
	}

	@Override
	public Integer getLinenumber() {
		return exception.getLinenumber();
	}

}
