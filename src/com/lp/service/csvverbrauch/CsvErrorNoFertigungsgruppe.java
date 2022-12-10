package com.lp.service.csvverbrauch;

import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.errors.NoFertigungsgruppeException;

public class CsvErrorNoFertigungsgruppe implements ICsvErrorAction {

	private NoFertigungsgruppeException exception;
	
	public CsvErrorNoFertigungsgruppe(NoFertigungsgruppeException exception) {
		this.exception = exception;
	}
	
	@Override
	public String getMessage() {
		return LPMain.getTextRespectUISPr("fert.verbrauchsartikel.import.error.keinefertigungsgruppe");
	}

	@Override
	public Integer getLinenumber() {
		return exception.getLinenumber();
	}

}
