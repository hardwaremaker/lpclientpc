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

import com.lp.client.util.logger.LpLogger;

public class ActualJavaVersion extends ApprovedJavaVersion {
	protected final LpLogger myLogger = (LpLogger) com.lp.client.util.logger.LpLogger
			.getInstance(this.getClass());

	private String theJavaVersion ;
	
	public ActualJavaVersion() {
		initializeFromSystemProperty() ;
	}
	
	protected void initializeFromSystemProperty() {
		theJavaVersion = System.getProperty("java.version");
	
		String[] javaVersion = theJavaVersion.split("\\.|_");

//		myLogger.warn("javaversion = '" + theJavaVersion + "'.");
//		for (String string : javaVersion) {
//			myLogger.warn("split value '" + string + "'.");
//		}
	
		int majorIndex = javaVersion.length > 1 ? 1 : 0;
		
		try {
			int firstNumber = Integer.parseInt(javaVersion[0]);
			if (firstNumber != 1) {
				//Java Versionen vor Java 9: Version 1.x.y
				//Ab Java 9: x.y
				//Wenn 1. Zahl nicht == 1, dann ist major index doch erster
				majorIndex = 0;
			}
			setMajor(Integer.parseInt(javaVersion[majorIndex]));

			// wenn kein update vorhanden dann setze auf 0
			int update = 0;
			if (javaVersion.length > 3) {
				update= Integer.parseInt(javaVersion[3]);
			}
			setUpdate(update);
		} catch (NumberFormatException e) {
			myLogger.error(
					"Java Version " + javaVersion + " konnte nicht sauber festgestellt werden!", e);
		}
	}
	
	public String getJavaVersionString() {
		return theJavaVersion ;
	}
}
