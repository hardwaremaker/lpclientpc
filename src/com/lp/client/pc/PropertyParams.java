package com.lp.client.pc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import com.lp.client.frame.Defaults;

public class PropertyParams {
	public final static String defaultContext = "default.";
	
	private final Properties properties;
	private static final String propertyFile = SystemProperties.homeDir() + "/.heliumv/parameter.properties";
	
	public PropertyParams(Properties p) {
		this.properties = p;
	}

	/**
	 * Die Parameter vom Filesystem laden.</br>
	 * <p> Die Property-Datei muss existieren.</p>
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static PropertyParams load() throws FileNotFoundException, IOException {
		try(FileInputStream fis = new FileInputStream(propertyFile)) {
			Properties p = new Properties();
			p.load(fis);
			return new PropertyParams(p);
		}	
	}
	
	/**
	 * Die Parameter vom Filesystem laden</br>
	 * <p>Sollte die Property-Datei nicht existieren wird ein leerer Satz
	 * an Properties zur Verf&uuml;gung gestellt.</p>
	 * <p>Aktuell gehen wir davon aus, dass sich im betreffenden Verzeichnis
	 * tats&auml;chlich keine Verzeichnisse befinden, die den Namen der 
	 * Property-Datei haben. Und nat&uuml;rlich haben wir auch Leserechte.</p>
	 * @return
	 * @throws IOException
	 */
	public static PropertyParams loadOrEmpty() throws IOException {
		try {
			return load();
		} catch(FileNotFoundException e) {
			return new PropertyParams(new Properties());
		}
	}

	public void apply(String prefix) {
		Defaults defaults = Defaults.getInstance();
		Enumeration<?> objKeys = properties.propertyNames();
		while(objKeys.hasMoreElements()) {
			String key = objKeys.nextElement().toString();
			if(!key.startsWith(prefix)) continue;
			
			if(applySystemKey(key, SystemProperties.factoryClass)) continue;
			if(applySystemKey(key, SystemProperties.providerUrl)) continue;
			
			applyUserSettings(defaults, key, prefix);
		}			
	}
	
	private void applyUserSettings(Defaults defaults, String key, String prefix) {
		if(key.equals(prefix + "maximized")) {
			defaults.setMaximized(valueAsBoolean(key));
			return;
		}
		if(key.equals(prefix + "mandant")) {
			defaults.setUebersteuerterMandant(valueAsString(key));
			return;
		}
		
		if(key.equals(prefix + "username")) {
			defaults.usernameAusBatch = valueAsString(key);
			return;
		}

		if(key.equals(prefix + "password")) {
			defaults.passwordAusBatch = valueAsString(key);
			return;
		}

		if(key.equals(prefix + "locale")) {
			defaults.localeAusBatch = valueAsString(key);
			return;
		}

		if(key.equals(prefix + "background")) {
			defaults.setBackground(valueAsBoolean(key));
			return;
		}

		if(key.equals(prefix + "checkresolution")) {
			defaults.setCheckResolution(valueAsBoolean(key));
			return;
		}

		if(key.equals(prefix + "direkthilfe")) {
			defaults.setDirekthilfeEnabled(valueAsBoolean(key));
			return;
		}

		if(key.equals(prefix + "laf-system")) {
			defaults.setDefaultLookAndFeel(
					javax.swing.UIManager.getSystemLookAndFeelClassName());
			return;
		}

		if(key.equals(prefix + "laf-java")) {
			defaults.setDefaultLookAndFeel(
					javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
			return;
		}

		if(key.equals(prefix + "laf-kunststoff")) {
			defaults.setDefaultLookAndFeel(Desktop.LAF_CLASS_NAME_KUNSTSTOFF);
			return;
		}

		if(key.equals(prefix + "layout")) {
			defaults.setLoadLayoutOnLogon(valueAsBoolean(key));
			return;
		}

		if(key.equals(prefix + "refreshtitle")) {
			defaults.setRefreshTitle(valueAsBoolean(key));
			return;
		}

		if(key.equals(prefix + "showiids")) {
			defaults.setShowIIdColumn(valueAsBoolean(key));
			return;
		}
		
		if(key.equals(prefix + "usewaitcursor")) {
			defaults.setUseWaitCursor(valueAsBoolean(key));
			return;
		}
		
		if(key.equals(prefix + "enablerechtschreibpruefung")) {
			defaults.setRechtschreibpruefungEnabled(valueAsBoolean(key));
			return;
		}
	}
	
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
	
	public void setProviderUrl(String value) {
		setProviderUrl(defaultContext, value);
	}
	
	public void setProviderUrl(String prefix, String value) {
		setProperty(prefix + SystemProperties.providerUrl, value);
	}
	
	public void setFactoryClass(String value) {
		setFactoryClass(defaultContext, value);
	}
	
	public void setFactoryClass(String prefix, String value) {
		setProperty(prefix + SystemProperties.factoryClass, value);
	}
	
	public void setFactoryClass(String prefix, boolean wildflyAS) {
		setProperty(prefix + SystemProperties.factoryClass, wildflyAS ?
				SystemProperties.factoryClassWildfly : SystemProperties.factoryClassJBoss);
	}

	public void store() throws FileNotFoundException, IOException {
		try(FileOutputStream fos = new FileOutputStream(propertyFile)) {
			properties.store(fos,"");
		}
	}

	private boolean applySystemKey(String propertyKey, String systemProperty) {
		if(propertyKey.endsWith(systemProperty)) {
			System.setProperty(systemProperty, properties.getProperty(propertyKey));
			return true;
		}
		
		return false;
	}

	private boolean valueAsBoolean(String key) {
		String value = properties.getProperty(key);
		if(value == null) {
			return false;
		}
		
		value = value.trim().toLowerCase();
		if("true".equals(value) || 
				"enable".equals(value) || 
				"enabled".equals(value) ||
				"1".equals(value)) {
			return true;
		}
		
		return false;
	}
	
	private String valueAsString(String key) {
		return properties.getProperty(key);
	}
	
	private String valueAsString(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
}