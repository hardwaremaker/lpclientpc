package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class DependenceFieldBackgroundColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookDependenceFieldBackground(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return null;
	}

	@Override
	protected Color getDefaultColor() {
		Color color = ClientConfiguration.getDependenceFieldBackground(getRegistrant().getColorVision());
		return color != null ? color : new Color(220, 220, 255);
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getDependenceFieldBackground(getRegistrant().getColorVision());
	}

}
