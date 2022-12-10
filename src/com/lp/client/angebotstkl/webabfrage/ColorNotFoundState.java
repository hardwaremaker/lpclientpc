package com.lp.client.angebotstkl.webabfrage;

import java.awt.Color;

import com.lp.client.frame.HelperClient;

public class ColorNotFoundState implements IColorState {

	private Color bgColor = new Color(255, 77, 77);
	private Color bgColorSelected =  new Color(204, 0, 0);

	@Override
	public Color getBackgroundColor() {
		return bgColor; //#ee4000
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
