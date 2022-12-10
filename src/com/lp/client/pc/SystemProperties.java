/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.pc;

import com.lp.server.util.HvOptional;
import com.lp.server.util.Validator;

public class SystemProperties {
	public static final String providerUrl  = "java.naming.provider.url";
	public static final String factoryClass = "java.naming.factory.initial";
	public static final String factoryClassJBoss = "org.jnp.interfaces.NamingContextFactory";
	public static final String factoryClassWildfly = "org.wildfly.naming.client.WildFlyInitialContextFactory";
	public static final String featureContextParam = "hv.feature.contextparam";
	
	private static final Boolean cachedMacOs = System.getProperty("os.name")
			.toLowerCase().startsWith("mac os x");
	private static final Boolean cachedWinOs = System.getProperty("os.name")
			.toLowerCase().indexOf("win") > -1;

	public SystemProperties() {
	}

	/**
	 * Handelt es sich um ein Mac System?
	 * 
	 * @return true wenn es ein Mac OSX Betriebssystem ist, ansonsten false
	 */
	public static boolean isMacOs() {
		return cachedMacOs;
	}
	
	/**
	 * Handelt es sich um ein Windows System?
	 * 
	 * @return true wenn es ein Windows Betriebssystem ist, ansonsten false
	 */
	public static boolean isWinOs() {
		return cachedWinOs;
	}
	
	/**
	 * Das Home-Verzeichnis des am Betriebssystem angemeldeten Benutzers</br>
	 * <p>Es wird der Wert aus "user.home" verwendet. Sollte dieser null sein, 
	 * wird das "aktuelle" Verzeichnis verwendet.</p>
	 * <p>Das Home-Verzeichnis hat keinen abschliessenden "/" oder "\\"</p>
	 * 
	 * @return das Home-Verzeichnis des aktuell angemeldeten Benutzers
	 */
	public static String homeDir() {
		String homeDir = System.getProperty("user.home");
		if(homeDir == null) {
			homeDir = ".";
		}
		while(homeDir.endsWith("/") || homeDir.endsWith("\\")) {
			homeDir = homeDir.substring(0, homeDir.length());
		}
		
		return homeDir;
	}
	
	/**
	 * Der Wert der Systemproperty "java.naming.provider.url".</br>
	 * <p>Diese wird &uuml;blicherweise f&uuml;r die Server-Url
	 * (Applikationsserver) verwendet.</p>
	 * 
	 * <em>Es geht hier darum, eine einzige Stelle zu haben, mit
	 * der auf diesen Wert zugegriffen wird. Bitte benutzen.</em>
	 * 
	 * @return Wert der Systemproperty
	 */
	public static HvOptional<String> providerUrl() {
		return HvOptional.ofNullable(System.getProperty(providerUrl));
	}
	
	public static void providerUrl(String newUrl) {
		Validator.notEmpty(newUrl, "newUrl");
		System.setProperty(providerUrl, newUrl);
	}
	
	/**
	 * Der Wert der Systemproperty "java.naming.factory.initial"</br>
	 * <p>Diese wird &uuml;blicherweise verwendet, um das Protokoll
	 * mit dem der Applikationsserver angesprochen wird, zu definieren.</p>
	 * 
	 * <em>Es geht hier darum, eine einzige Stelle zu haben, mit
	 * der auf diesen Wert zugegriffen wird. Bitte benutzen.</em>
	 * 
	 * @return Wert der Systemproperty
	 */
	public static HvOptional<String> factoryClass() {
		return HvOptional.ofNullable(System.getProperty(factoryClass));
	}
	
	public static void factoryClass(String newClass) {
		Validator.notEmpty(newClass, "newClass");
		System.setProperty(factoryClass, newClass);
	}
	
	public static void factoryClass(boolean wildflyAS) {
		System.setProperty(factoryClass, wildflyAS ? 
				factoryClassWildfly : factoryClassJBoss);
	}
	
	public static HvOptional<String> featureContextParam() {
		try {
			return HvOptional.ofNullable(
				System.getProperty(featureContextParam));
		} catch(IllegalArgumentException e) {
			return HvOptional.empty();
		}
	}
}
