package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class TextfieldBorderColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookTextfieldBorderColor(getRegistrant().getColorVision());
	}

	@Override
	protected Color createDefaultConfigColor() {
		return null;
	}

	@Override
	protected String getUIManagerProperty() {
		return null;
	}

	@Override
	protected Color getDefaultColor() {
		return ClientConfiguration.getTextfieldBorderColor(getRegistrant().getColorVision());
	}

}
