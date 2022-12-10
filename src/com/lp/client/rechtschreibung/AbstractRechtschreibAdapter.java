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
package com.lp.client.rechtschreibung;

import java.awt.TextComponent;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.lp.client.pc.LPMain;
import com.lp.server.util.logger.LpLogger;

/**
 * Superklasse f&uuml;r alle RechtschreibAdapter. <br>
 * Ein RechtschreibAdapter bietet einfache Unterst&uuml;tzung von
 * Rechtschreibpr&uuml;fungsfunktionen f&uuml;r eine Anwendung (z.B.
 * {@link SwingRechtschreibAdapter} f&uuml;r Swing {@link TextComponent}s). <br>
 * Durch Verwendung eines RechtschreibAdapters kann die Rechtschreibpr&uuml;fung
 * einfach aktiviert, deaktiviert und die Sprache ge&auml;ndert werden. Diese
 * Klasse implementiert alle Funktionen aus {@link IRechtschreibPruefbar}. Eine
 * Klasse, die Rechtschreibung unterst&uuml;tzen soll, kann auch dieses
 * Interface Implementieren und alle Funktionen an einen RechtschreibAdapter
 * weiter leiten. <br>
 * 
 * F&uuml;r mehr Informationen zur Struktur der Rechtschreibpr&uuml;fung siehe
 * {@link RechtschreibpruefungCore}
 * 
 * @author Alexander Daum
 *
 */
public abstract class AbstractRechtschreibAdapter implements IRechtschreibPruefbar {
	protected Logger myLogger = LpLogger.getLogger(getClass().toString());

	/**
	 * Aktiviert die Rechtschreibpr&uuml;fung mit dem UI Locale
	 */
	public void aktiviereRechtschreibpruefung() {
		try {
			Locale locUi = LPMain.getTheClient().getLocUi();
			aktiviereRechtschreibpruefung(locUi);
		} catch (Throwable e) {

		}
	}

	/**
	 * Aktiviert die Rechtschreibpr&uuml;fung mit spezifischem Locale
	 * 
	 * @param loc
	 */
	public abstract void aktiviereRechtschreibpruefung(Locale loc);
}
