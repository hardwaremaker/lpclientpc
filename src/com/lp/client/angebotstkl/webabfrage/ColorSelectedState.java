package com.lp.client.angebotstkl.webabfrage;

import java.awt.Color;

import com.lp.client.frame.HelperClient;

public class ColorSelectedState implements IColorState {

	private Color bgColor = new Color(153, 234, 255);
	private Color bgColorSelected = new Color(0, 182, 230);

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
