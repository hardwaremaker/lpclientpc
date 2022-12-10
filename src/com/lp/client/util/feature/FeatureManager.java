package com.lp.client.util.feature;

import java.util.Set;

public interface FeatureManager {
	/**
	 * Name of this Manager
	 * @return name of this Manager
	 */
	String getName();
	
	Set<FeatureToggle> getFeatures();
	
	boolean isActive(FeatureToggle feature);
	
	FeatureState getFeatureState(FeatureToggle feature);
	
	void setFeatureState(FeatureState featureState);
}
