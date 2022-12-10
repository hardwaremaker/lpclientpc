package com.lp.client.frame.uiproperty;

import com.lp.server.system.service.ParameterFac;

public class EditorZoomProperty extends HvUIPropertyString {

	public EditorZoomProperty() {
	}

	@Override
	protected String load() throws Throwable {
		return holeArbeitsplatzparameterString(
				ParameterFac.ARBEITSPLATZPARAMETER_EDITOR_ZOOM);
	}

}
