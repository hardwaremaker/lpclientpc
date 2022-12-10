package com.lp.client.frame.uiproperty;

import com.lp.client.frame.ExceptionLP;
import com.lp.server.system.service.ArbeitsplatzparameterDto;

public abstract class HvUIPropertyBoolean extends HvUIProperty<Boolean> {
	
	public HvUIPropertyBoolean() {
	}
	
	@Override
	protected Boolean getDefaultValue() {
		return Boolean.FALSE;
	}
	
	protected Boolean holeArbeitsplatzparameterBoolean(String param) throws ExceptionLP, Throwable {
		ArbeitsplatzparameterDto paramDto = holeArbeitsplatzparameter(param);
		return paramDto != null ? paramDto.asBoolean() : getDefaultValue();
	}
}
