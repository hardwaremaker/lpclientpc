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

import java.util.Locale;

/**
 * Interface, dass alle Klassen, die eine Rechtschreibpruefung unterstuetzen
 * implementieren. Zur einfachen Implementierung sollte die Klasse selbst noch
 * einen RechtschreibAdapter als Membervariable beistzen. Diese erben von
 * {@link AbstractRechtschreibAdapter} (z.B. {@link SwingRechtschreibAdapter}),
 * diese Klasse implementieren alle Funktionen dieses Interfaces und aufrufe
 * k&ouml;nnen an den Adapter weitergeleitet werden. <br>
 * 
 * F&uuml;r mehr Informationen zur Struktur der Rechtschreibpr&uuml;fung siehe
 * {@link RechtschreibpruefungCore}
 * 
 * @author Alexander Daum
 */
public interface IRechtschreibPruefbar {
	/**
	 * Aktiviert die Rechtschreibpr&uuml;fung
	 */
	void aktiviereRechtschreibpruefung();

	/**
	 * Deaktiviert die Rechtschreibpr&uuml;fung
	 */
	void deaktiviereRechtschreibpruefung();

	/**
	 * &Auml;ntert das Locale der Rechtschreibpr&uuml;fung. Verursacht eine neue
	 * pr&uuml;fung des gesamten Textes.
	 * 
	 * @param loc neues Locale
	 * 
	 */
	void setRechtschreibpruefungLocale(Locale loc);
}
