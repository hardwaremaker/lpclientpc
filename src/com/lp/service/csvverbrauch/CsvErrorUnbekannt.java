package com.lp.service.csvverbrauch;

import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.errors.ImportException;

public class CsvErrorUnbekannt implements ICsvErrorAction {

	private ImportException exception;

	public CsvErrorUnbekannt(ImportException exception) {
		this.exception = exception;
	}

	@Override
	public String getMessage() {
		return LPMain.getMessageTextRespectUISPr("fert.verbrauchsartikel.import.error.unbekannt", 
				exception.getMessage());
	}

	@Override
	public Integer getLinenumber() {
		return exception.getLinenumber();
	}

}
