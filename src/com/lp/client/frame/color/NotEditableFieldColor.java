package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class NotEditableFieldColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookNotEditableColor(getRegistrant().getColorVision());
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getNotEditableColor(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return null;
	}

	@Override
	protected Color getDefaultColor() {
		return new Color(240, 240, 240);
	}

}
