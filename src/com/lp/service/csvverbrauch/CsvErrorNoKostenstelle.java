package com.lp.service.csvverbrauch;

import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.errors.NoKostenstelleException;

public class CsvErrorNoKostenstelle implements ICsvErrorAction {

	private NoKostenstelleException exception;
	
	public CsvErrorNoKostenstelle(NoKostenstelleException exception) {
		this.exception = exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getTextRespectUISPr("fert.verbrauchsartikel.import.error.keinekostenstelle");
	}

	@Override
	public Integer getLinenumber() {
		return exception.getLinenumber();
	}

}
