package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class EditableFieldColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookEditableColor(getRegistrant().getColorVision());
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getEditableColor(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return null;
	}

	@Override
	protected Color getDefaultColor() {
		return Color.WHITE;
	}

}
