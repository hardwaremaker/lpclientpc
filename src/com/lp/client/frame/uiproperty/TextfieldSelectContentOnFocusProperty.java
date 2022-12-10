package com.lp.client.frame.uiproperty;

import com.lp.server.system.service.ParameterFac;

public class TextfieldSelectContentOnFocusProperty extends HvUIPropertyBoolean {

	public TextfieldSelectContentOnFocusProperty() {
	}

	@Override
	protected Boolean load() throws Throwable {
		return holeArbeitsplatzparameterBoolean(
				ParameterFac.ARBEITSPLATZPARAMETER_TEXTFELD_SELEKTION_BEI_FOKUS);
	}

}
