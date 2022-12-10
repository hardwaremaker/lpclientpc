package com.lp.client.frame.color;

import java.util.ArrayList;
import java.util.List;

public class HvColorRegistrant {

	private List<HvColor> registrants = new ArrayList<HvColor>();
	private String colorVision;
	
	public HvColorRegistrant() {
	}

	public HvColor add(HvColor color) {
		registrants.add(color);
		color.setRegistrant(this);
		
		return color;
	}
	
	public void remove(HvColor color) {
		registrants.remove(color);
	}
	
	public String getColorVision() {
		return colorVision;
	}
	
	public void setColorVision(String colorVision) {
		this.colorVision = colorVision;
		resetColors();
	}
	
	public void resetColors() {
		for (HvColor color : registrants) {
			color.reset();
			color.get();
		}
	}
	
	public void setOsColors() {
		for (HvColor color : registrants) {
			color.setOsColor();
		}
	}
}
