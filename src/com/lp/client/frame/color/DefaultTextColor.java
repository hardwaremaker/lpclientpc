package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class DefaultTextColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookDefaultTextColor(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return null;
	}

	@Override
	protected Color getDefaultColor() {
		return new Color(0, 0, 0);
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getDefaultTextColor(getRegistrant().getColorVision());
	}

}
