package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class TextValidColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookValidTextColor(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return null;
	}

	@Override
	protected Color getDefaultColor() {
		return new Color(0, 0x96, 0);
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getValidTextColor(getRegistrant().getColorVision());
	}
}
