package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class TextfieldInactiveForegroundColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookInactiveForegroundColor(getRegistrant().getColorVision());
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getInactiveForegroundColor(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return null;
	}

	@Override
	protected Color getDefaultColor() {
		return new Color(0, 0, 255);
	}

}
