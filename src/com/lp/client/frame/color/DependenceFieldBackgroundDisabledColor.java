package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class DependenceFieldBackgroundDisabledColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookDependenceFieldDisabledBackground(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return null;
	}

	@Override
	protected Color getDefaultColor() {
		Color color = ClientConfiguration.getDependenceFieldDisabledBackground(getRegistrant().getColorVision());
		return color != null ? color : new Color(230, 230, 255);
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getDependenceFieldDisabledBackground(getRegistrant().getColorVision());
	}

}
