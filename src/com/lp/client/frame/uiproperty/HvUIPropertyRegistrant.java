package com.lp.client.frame.uiproperty;

import java.util.ArrayList;
import java.util.List;

public class HvUIPropertyRegistrant {

	private List<HvUIProperty<?>> registrants = new ArrayList<HvUIProperty<?>>();

	public HvUIPropertyRegistrant() {
	}

	public <T extends HvUIProperty<?>> T add(T uiProperty) {
		registrants.add(uiProperty);
		return uiProperty;
	}
	
	public void remove(HvUIProperty<?> uiProperty) {
		registrants.remove(uiProperty);
	}
	
	public void resetProperties() {
		for (HvUIProperty<?> uiProperty : registrants) {
			uiProperty.reset();
		}
	}
}
