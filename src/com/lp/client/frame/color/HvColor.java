package com.lp.client.frame.color;

import java.awt.Color;

import javax.swing.UIManager;

import com.lp.client.frame.Defaults;

public abstract class HvColor {
	
	private HvColorRegistrant registrant;
	private Color color;
	private Color osColor;

	public HvColor() {
	}

	public void setRegistrant(HvColorRegistrant registrant) {
		this.registrant = registrant;
	}
	
	protected HvColorRegistrant getRegistrant() {
		return registrant;
	}
	
	public Color get() {
		if (color == null) {
			color = setUIManagerColor(createColorImpl());
		}
		return color;
	}
	
	public void setOsColor() {
		if (osColor == null) {
			osColor = getUIManagerColor();
		}
	}
	
	protected Color createColorImpl() {
		Color newColor = Defaults.getInstance().isHeliumLookEnabled() ? createColor() : 
			(osColor != null ? osColor : createDefaultConfigColor());
		if (newColor != null) return newColor;
		
		newColor = getUIManagerColor();
		if (newColor != null) return newColor;
		
		return getDefaultColor();
	}
	
	private Color getUIManagerColor() {
		if (!hasUIManagerProperty()) return null;
		
		return UIManager.getColor(getUIManagerProperty());
	}
	
	private Color setUIManagerColor(Color colorToSet) {
		if (hasUIManagerProperty()) {
			UIManager.put(getUIManagerProperty(), colorToSet);
		}
		
		return colorToSet;
	}
	
	protected boolean hasUIManagerProperty() {
		return getUIManagerProperty() != null;
	}

	protected abstract Color createColor();
	
	protected abstract Color createDefaultConfigColor();
	
	protected abstract String getUIManagerProperty();
	
	protected abstract Color getDefaultColor();
	
	public void reset() {
		color = null;
	}
}
