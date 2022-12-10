package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class ScrollPaneBackgroundColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookScrollPaneBackground(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return "ScrollPane.background";
	}

	@Override
	protected Color getDefaultColor() {
		return new Color(240, 240, 240);
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getScrollPaneBackground(getRegistrant().getColorVision());
	}

}
