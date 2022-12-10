package com.lp.client.angebotstkl.webabfrage;

import java.awt.Color;

import com.lp.client.frame.HelperClient;

public class ColorStartState implements IColorState {

	private Color bgColor = new Color(153, 234, 255);

	@Override
	public Color getBackgroundColor() {
		return Color.white;
	}

	@Override
	public Color getBackgroundColorSelected() {
		return new Color(51, 153, 255);
	}

	@Override
	public Color getForegroundColor() {
		return HelperClient.getContrastYIQ(bgColor);
	}

	@Override
	public Color getForegroundColorSelected() {
		return Color.white;
	}
}
