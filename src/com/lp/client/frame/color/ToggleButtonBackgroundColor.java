package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class ToggleButtonBackgroundColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookPanelToolbarButtonBackground(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return null;
	}

	@Override
	protected Color getDefaultColor() {
		return null;
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getPanelToolbarButtonBackground(getRegistrant().getColorVision());
	}

}
