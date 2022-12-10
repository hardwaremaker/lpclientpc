package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class ListBackgroundColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookListBackground(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return "List.background";
	}

	@Override
	protected Color getDefaultColor() {
		return new Color(255, 255, 255);
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getListBackground(getRegistrant().getColorVision());
	}

}
