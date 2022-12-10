package com.lp.client.frame.uiproperty;

import com.lp.server.system.service.ArbeitsplatzparameterDto;

public abstract class HvUIPropertyDouble extends HvUIProperty<Double> {

	public HvUIPropertyDouble() {
	}

	protected Double holeArbeitsplatzparameterDouble(String param) throws Throwable {
		ArbeitsplatzparameterDto paramDto = holeArbeitsplatzparameter(param);
		return paramDto != null ? new Double(paramDto.getCWert()) : getDefaultValue();
	}
}
