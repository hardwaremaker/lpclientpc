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
package com.lp.client.frame.component;

/**
 * WrapperLabel mit zus&auml;tzlicher Methode setFileSize, die eine Zahl in Byte
 * zur passenden Einheit (Basis 1024) umrechnet. Unterst&uuml;tzt werden kB, MB,
 * GB und TB. Alle Zahlen aus Byte werden mit 2 Dezimalstellen angegeben
 * 
 * @author Alex
 *
 */
public class WrapperFileSizeLabel extends WrapperLabel {
	private static final long serialVersionUID = 1L;

	private static final String[] postfixes = { "kB", "MB", "GB", "TB" };

	public void setFileSize(long bytes) {
		if (bytes < 1024) {
			// Keine kB daraus machen
			setText(String.format("%dB", bytes));
			return;
		}
		double dBytes = bytes;
		for (int i = 0; i < postfixes.length; i++) {
			dBytes /= 1024;
			if (dBytes < 1024) {
				setText(String.format("%.2f%s", dBytes, postfixes[i]));
				return;
			}
		}
	}

}
