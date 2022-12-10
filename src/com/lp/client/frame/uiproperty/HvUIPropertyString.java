package com.lp.client.frame.uiproperty;

import com.lp.client.frame.ExceptionLP;
import com.lp.server.system.service.ArbeitsplatzparameterDto;

public abstract class HvUIPropertyString extends HvUIProperty<String> {

	public HvUIPropertyString() {
	}

	protected String holeArbeitsplatzparameterString(String param) throws ExceptionLP, Throwable {
		ArbeitsplatzparameterDto paramDto = holeArbeitsplatzparameter(param);
		return paramDto != null ? paramDto.getCWert() : getDefaultValue(); 
	}
}
