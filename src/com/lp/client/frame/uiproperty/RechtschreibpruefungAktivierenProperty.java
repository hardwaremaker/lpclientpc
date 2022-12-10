package com.lp.client.frame.uiproperty;

import com.lp.server.system.service.ParameterFac;

public class RechtschreibpruefungAktivierenProperty extends HvUIPropertyBoolean {

	@Override
	protected Boolean load() throws Throwable {
		return holeArbeitsplatzparameterBoolean(ParameterFac.ARBEITSPLATZPARAMETER_RECHTSCHREIBPRUEFUNG);
	}

	@Override
	protected Boolean getDefaultValue() {
		return Boolean.FALSE;
	}

}
