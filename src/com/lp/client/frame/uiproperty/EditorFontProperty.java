package com.lp.client.frame.uiproperty;

import com.lp.server.system.service.ParameterFac;

public class EditorFontProperty extends HvUIPropertyString {

	public EditorFontProperty() {
	}

	@Override
	protected String load() throws Throwable {
		return holeArbeitsplatzparameterString(
				ParameterFac.ARBEITSPLATZPARAMETER_EDITOR_SCHRIFTART);
	}

}
