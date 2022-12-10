package com.lp.client.util.feature;

import java.io.Serializable;

public class FeatureState implements Serializable {
	private static final long serialVersionUID = 6882683745706950096L;

	private final FeatureToggle feature;
	private boolean enabled;
	
	public FeatureState(FeatureToggle feature) {
		this(feature, false);
	}
	
	public FeatureState(FeatureToggle feature, boolean enabled) {
		this.feature = feature;
		this.enabled = enabled;
	}
	
	public FeatureToggle getFeature() {
		return feature;
	}
	
	public void beEnabled() {
		this.enabled = true;
	}
	
	public void beDisabled() {
		this.enabled = false;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	/**
     * Creates a copy of this state object
     */
    public FeatureState copy() {
        FeatureState copy = new FeatureState(feature);
        copy.setEnabled(this.enabled);
/*
     	copy.setStrategyId(this.strategyId);
        for (Entry<String, String> entry : this.parameters.entrySet()) {
            copy.setParameter(entry.getKey(), entry.getValue());
        }
 */
        return copy;
    }
    
    /**
     * Returns a copy of a featureState, or <code>null</code> if the featureState is
     * <code>null</code>.
     */
    public static FeatureState copyOf(FeatureState featureState) {
        return featureState == null ? null : featureState.copy();
    }
}
