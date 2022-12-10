package com.lp.client.util.feature;

public enum HvFeatures implements FeatureToggle {
	OSXFileDialog("OSXFileDialog"),
	ContextParam("ContextParam"),
	BlockEditor("BlockEditor");

	HvFeatures(String value) {
		this.value = value;
	}
	
	public boolean isActive() {
		return FeatureContext.getManager().isActive(this);
	}

	public String getText() {
		return value ;
	}

	public static HvFeatures fromString(String text) {
		if(text != null) {
			for (HvFeatures status : HvFeatures.values()) {
				if(text.equalsIgnoreCase(status.value) || text.equals(status.toString())) {
					return status ;
				}
			}
		}
		
		throw new IllegalArgumentException("HvFeatures: No enum value '" + text + "'") ;
	}
	
	private String value;
}
