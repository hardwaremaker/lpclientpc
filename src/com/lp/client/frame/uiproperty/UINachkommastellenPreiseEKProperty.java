package com.lp.client.frame.uiproperty;

import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

public class UINachkommastellenPreiseEKProperty extends HvUIPropertyInteger {

	@Override
	protected Integer load() throws Throwable {
		ParametermandantDto paramDto = holeMandantparameterAllgemein(
				ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_EK);
		return paramDto.asInteger();
	}

}