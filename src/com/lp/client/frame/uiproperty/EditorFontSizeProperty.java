package com.lp.client.frame.uiproperty;

import com.lp.server.system.service.ParameterFac;

public class EditorFontSizeProperty extends HvUIPropertyInteger {

	public EditorFontSizeProperty() {
	}

	@Override
	protected Integer load() throws Throwable {
		return holeArbeitsplatzparameterInteger(
				ParameterFac.ARBEITSPLATZPARAMETER_EDITOR_SCHRIFT_GROESSE);
	}

}
