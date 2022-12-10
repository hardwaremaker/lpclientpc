package com.lp.client.util.feature;

import com.lp.client.util.feature.manager.PropertyFileFeatureManager;

public class FeatureContext {

	private static FeatureManager cache;
	
	public static FeatureManager getManager() {
		FeatureManager m = getManagerOrNull();
		if(m != null) {
			return m;
		}
		
		throw new IllegalStateException("No FeatureManager available");
	}
	
	public static FeatureManager getManagerOrNull() {
		if(cache != null) return cache;
		
//		cache = new InMemoryFeatureManager();
		
		String usrHome = System.getProperty("user.home");
		if(usrHome == null) {
			usrHome = ".";
		}
		String propertyFileName = usrHome + "/.heliumv/feature.properties";
		cache = new PropertyFileFeatureManager(propertyFileName);
		
		return cache;
	}
	
	public static void clear() {
		cache = null;
	}
}
