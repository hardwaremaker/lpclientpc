package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class SelectedFieldBackgroundColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookSelectedFieldBackground(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return null;
	}

	@Override
	protected Color getDefaultColor() {
		Color color = ClientConfiguration.getSelectedFieldBackground(getRegistrant().getColorVision());
		return color != null ? color : new Color(180, 195, 255);
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getSelectedFieldBackground(getRegistrant().getColorVision());
	}

}
