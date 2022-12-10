package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class MandatoryFieldBorderColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookMandatoryFieldBorderColor(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return null;
	}

	@Override
	protected Color getDefaultColor() {
		Color color = ClientConfiguration.getMandatoryFieldBorderColor(getRegistrant().getColorVision());
		return color != null ? color : new Color(207, 0, 104);
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getMandatoryFieldBorderColor(getRegistrant().getColorVision());
	}

}
