package com.lp.service.csvverbrauch;

import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.errors.NumberOfColumnsException;

public class CsvErrorNumberOfColumns implements ICsvErrorAction {

	private NumberOfColumnsException exception;
	
	public CsvErrorNumberOfColumns(NumberOfColumnsException exception) {
		this.exception = exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr("fert.verbrauchsartikel.import.error.zuwenigspalten", 
				exception.getColumns(), exception.getIndex());
	}

	@Override
	public Integer getLinenumber() {
		return exception.getLinenumber();
	}

}
