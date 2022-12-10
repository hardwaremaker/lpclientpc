package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class OptionPaneBackgroundColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookOptionPaneBackground(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return "OptionPane.background";
	}

	@Override
	protected Color getDefaultColor() {
		return new Color(240, 240, 240);
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getOptionPaneBackground(getRegistrant().getColorVision());
	}

}
