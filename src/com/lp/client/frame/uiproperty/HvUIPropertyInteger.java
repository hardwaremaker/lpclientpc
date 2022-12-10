package com.lp.client.frame.uiproperty;

import com.lp.client.frame.ExceptionLP;
import com.lp.server.system.service.ArbeitsplatzparameterDto;

public abstract class HvUIPropertyInteger extends HvUIProperty<Integer> {

	public HvUIPropertyInteger() {
	}

	protected Integer holeArbeitsplatzparameterInteger(String param) throws ExceptionLP, Throwable {
		ArbeitsplatzparameterDto paramDto = holeArbeitsplatzparameter(param);
		return paramDto != null ? new Integer(paramDto.getCWert()): getDefaultValue();
	}
}
