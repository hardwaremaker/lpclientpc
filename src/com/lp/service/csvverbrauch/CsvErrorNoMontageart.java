package com.lp.service.csvverbrauch;

import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.errors.NoMontageartException;

public class CsvErrorNoMontageart implements ICsvErrorAction {

	private NoMontageartException exception;
	
	public CsvErrorNoMontageart(NoMontageartException exception) {
		this.exception = exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getTextRespectUISPr("fert.verbrauchsartikel.import.error.keinemontageart");
	}

	@Override
	public Integer getLinenumber() {
		return exception.getLinenumber();
	}

}
