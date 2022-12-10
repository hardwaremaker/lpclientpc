package com.lp.client.angebotstkl.webabfrage;

import java.util.List;

import com.lp.client.frame.ExceptionLP;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;

public interface IPartImportFactory {

	public List<EinkaufsangebotpositionDto> transformWebabfrageResult(
			EinkaufsangebotDto einkaufsangebotDto, IWebabfrageResult result) throws ExceptionLP, Throwable;
}
