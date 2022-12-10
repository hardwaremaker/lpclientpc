package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class PrinterPropertiesSavedBorderColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration
				.getHeliumLookPrinterPropertiesSavedBorderColor(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return null;
	}

	@Override
	protected Color getDefaultColor() {
		Color color = ClientConfiguration
				.getPrinterPropertiesSavedBorderColor(getRegistrant().getColorVision());
		return color != null ? color : new Color(0, 200, 0);
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration
				.getPrinterPropertiesSavedBorderColor(getRegistrant().getColorVision());
	}

}
