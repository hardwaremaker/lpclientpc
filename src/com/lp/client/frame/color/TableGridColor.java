package com.lp.client.frame.color;

import java.awt.Color;

import com.lp.client.util.ClientConfiguration;

public class TableGridColor extends HvColor {

	@Override
	protected Color createColor() {
		return ClientConfiguration.getHeliumLookTableGridColor(getRegistrant().getColorVision());
	}

	@Override
	protected String getUIManagerProperty() {
		return "Table.gridColor";
	}

	@Override
	protected Color getDefaultColor() {
		return new Color(240, 240, 240);
	}

	@Override
	protected Color createDefaultConfigColor() {
		return ClientConfiguration.getTableGridColor(getRegistrant().getColorVision());
	}

}
