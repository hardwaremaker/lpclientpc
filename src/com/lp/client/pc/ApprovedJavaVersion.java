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

public class ApprovedJavaVersion {
	private int major;
	private int update;

	public ApprovedJavaVersion() {
	}

	public ApprovedJavaVersion(int major) {
		this(major, 0);
	}

	public ApprovedJavaVersion(int major, int update) {
		this.setMajor(major);
		this.setUpdate(update);
	}

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public int getUpdate() {
		return update;
	}

	public void setUpdate(int update) {
		this.update = update;
	}

	public String toString() {
		// Java Versionen vor Java 9: 1.major.0_update 
		// Java Versionen ab Java 9: major.minor.update (es wird nur major.minor hier
		// zurueckgegeben, weil nur 2 Versionen gespeichert werden)
		if (major >= 9) {
			return major + "." + update;
		} else {
			return "1." + major + ".0_" + update;
		}
	}

	/**
	 * Die angegebene Major/Update Version mit meiner vergleichen
	 * 
	 * @param checkMajor  das Majorrelase (6, 7, 8, ...)
	 * @param checkUpdate die Updateversion (0, 5, 20, ...)
	 * @return -1 wenn checkMajor/checkUpdate < als ich sind, 0 wenn gleich, oder 1
	 *         wenn gr&ouml;&szlig;
	 */
	public int compare(int checkMajor, int checkUpdate) {
		if (checkMajor < major)
			return -1;
		if (checkMajor > major)
			return 1;
		if (checkUpdate < update)
			return -1;
		if (checkUpdate > update)
			return 1;

		return 0;
	}

	public int compare(ApprovedJavaVersion other) {
		if (other.getMajor() < major)
			return -1;
		if (other.getMajor() > major)
			return 1;
		if (other.getUpdate() < update)
			return -1;
		if (other.getUpdate() > update)
			return 1;

		return 0;
	}
}