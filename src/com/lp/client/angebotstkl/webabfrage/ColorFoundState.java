package com.lp.client.angebotstkl.webabfrage;

import java.awt.Color;

import com.lp.client.frame.HelperClient;

public class ColorFoundState implements IColorState {

	private Color bgColor = new Color(255, 213, 153);
	private Color bgColorSelected = new Color(255, 150, 0);

	@Override
	public Color getBackgroundColor() {
		return bgColor;
	}

	@Override
	public Color getBackgroundColorSelected() {
		return bgColorSelected;
	}

	@Override
	public Color getForegroundColor() {
		return HelperClient.getContrastYIQ(bgColor);
	}

	@Override
	public Color getForegroundColorSelected() {
		return HelperClient.getContrastYIQ(bgColorSelected);
	}

}
