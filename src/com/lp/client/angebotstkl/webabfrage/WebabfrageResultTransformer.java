package com.lp.client.angebotstkl.webabfrage;

import java.util.ArrayList;
import java.util.List;

import com.lp.client.frame.ExceptionLP;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;

public class WebabfrageResultTransformer {
	private FindChipsPartFactory findchipsfactory;

	public WebabfrageResultTransformer(WebabfrageBaseModel baseModel) {
		findchipsfactory = new FindChipsPartFactory(baseModel);
	}

	public List<EinkaufsangebotpositionDto> transformWebabfrageResult(EinkaufsangebotDto einkaufsangebotDto, IWebabfrageResult result) 
			throws ExceptionLP, Throwable {
		if (result.getSelectedPart() != null && result.getSelectedPart().isFindChipsPart()) {
			return findchipsfactory.transformWebabfrageResult(einkaufsangebotDto, result);
		}
		
		return new ArrayList<EinkaufsangebotpositionDto>();
	}
}
