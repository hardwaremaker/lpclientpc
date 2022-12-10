package com.lp.client.util.feature.manager;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.lp.client.util.feature.FeatureManager;
import com.lp.client.util.feature.FeatureState;
import com.lp.client.util.feature.FeatureToggle;

public class InMemoryFeatureManager implements FeatureManager {

	private Map<String, FeatureState> toggles = new ConcurrentHashMap<String, FeatureState>();
	
	@Override
	public String getName() {
		return "InMemoryFeatureManager";
	}

	@Override
	public Set<FeatureToggle> getFeatures() {
		Set<FeatureToggle> keys = new HashSet<FeatureToggle>();
		for (Entry<String, FeatureState> entry : toggles.entrySet()) {
			keys.add(entry.getValue().getFeature());
		}
		return keys;
	}

	@Override
	public boolean isActive(FeatureToggle feature) {
		FeatureState state = getFeatureState(feature);
		return state == null ? false : state.isEnabled();
	}

	@Override
	public FeatureState getFeatureState(FeatureToggle feature) {
		return toggles.get(feature.name());
	}

	@Override
	public void setFeatureState(FeatureState featureState) {
        toggles.put(featureState.getFeature().name(), FeatureState.copyOf(featureState));
	}
}
