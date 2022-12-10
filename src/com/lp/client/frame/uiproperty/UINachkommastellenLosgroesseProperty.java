package com.lp.client.frame.uiproperty;

import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

public class UINachkommastellenLosgroesseProperty extends HvUIPropertyInteger {

	@Override
	protected Integer load() throws Throwable {
		ParametermandantDto paramDto = holeMandantparameterFertigung(
				ParameterFac.PARAMETER_NACHKOMMASTELLEN_LOSGROESSE);
		return paramDto.asInteger();
	}

}
