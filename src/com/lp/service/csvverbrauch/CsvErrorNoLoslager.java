package com.lp.service.csvverbrauch;

import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.errors.NoLoslagerException;

public class CsvErrorNoLoslager implements ICsvErrorAction {

	private NoLoslagerException exception;
	
	public CsvErrorNoLoslager(NoLoslagerException exception) {
		this.exception = exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getTextRespectUISPr("fert.verbrauchsartikel.import.error.keinloslager");
	}

	@Override
	public Integer getLinenumber() {
		return exception.getLinenumber();
	}

}
