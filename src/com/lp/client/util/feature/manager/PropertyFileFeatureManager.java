package com.lp.client.util.feature.manager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.lp.client.util.feature.FeatureManager;
import com.lp.client.util.feature.FeatureState;
import com.lp.client.util.feature.FeatureToggle;
import com.lp.client.util.feature.HvFeatures;


public class PropertyFileFeatureManager implements FeatureManager {

	private final String propertyFilename;
	private final Properties properties;
	private final ConcurrentHashMap<String, FeatureState> toggles = new ConcurrentHashMap<String, FeatureState>();

	public PropertyFileFeatureManager(String propertyFilename) {
		this.propertyFilename = propertyFilename;
		this.properties = readFile();
	}
	
	private Properties readFile() {
		FileInputStream fis = null;
		Properties values = new Properties();
		try {
			fis = new FileInputStream(this.propertyFilename);
			Properties temp = new Properties();
			temp.load(fis);
			values = temp;
		} catch (FileNotFoundException e) {
			e.printStackTrace();			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fis != null) {
				try {
					fis.close();					
				} catch (IOException e) {
				}
			}
		}

		return values;
	}
	
	@Override
	public String getName() {
		return "PropertyFileFeatureManager";
	}

	@Override
	public Set<FeatureToggle> getFeatures() {
		Set<FeatureToggle> features = new HashSet<FeatureToggle>();
		for (Entry<String, FeatureState> entry : toggles.entrySet()) {
			features.add(entry.getValue().getFeature());
		}
		return features;
	}

	@Override
	public boolean isActive(FeatureToggle feature) {
		FeatureState fs = getFeatureState(feature);
		return fs == null ? false : fs.isEnabled();
	}

	@Override
	public FeatureState getFeatureState(FeatureToggle feature) {
		FeatureState fs = toggles.get(feature.name());
		if(fs == null) {
			String value = properties.getProperty(feature.name());
			if(value == null) {
				if (HvFeatures.OSXFileDialog.name().equals(feature.name())) {
					fs = new FeatureState(feature, true);
					toggles.put(feature.name(), fs);
					return fs;
				}
				
				return null;
			}

			value = value.trim().toLowerCase();
			boolean enabled =
					value.equals("true") || 
					value.equals("enable") ||
					value.equals("enabled") ||
					value.equals("1");
			fs = new FeatureState(feature, enabled);
			toggles.put(feature.name(), fs);
		}
		
		return fs;
	}

	@Override
	public void setFeatureState(FeatureState featureState) {
		toggles.put(featureState.getFeature().name(), featureState);
	}
}
