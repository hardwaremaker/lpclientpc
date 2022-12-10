package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class ViewportBackgroundColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookViewportBackground(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return "Viewport.background";
	}

	@Override
	protected Color getDefaultColor() {
		return new Color(240, 240, 240);
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getViewportBackground(getRegistrant().getColorVision());
	}

}
