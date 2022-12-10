package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class TextfieldOnFocusColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookColorOnFocus(getRegistrant().getColorVision());
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getColorOnFocus(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return null;
	}

	@Override
	protected Color getDefaultColor() {
		return new Color(255, 255, 255);
	}

}
