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

/**
 * Klasse um eine &Auml;nderung im Text zu repr&auml;sentieren. Eine
 * &Auml;nderung ist entweder ein INSERT oder ein DELETE. In beiden F&auml;llen
 * ist {@link RSModification#completeText} der gesamte Text nach der
 * &Auml;nderung. <br>
 * Die Span range bezeichnet immer den ver&auml;nderten Bereich, ist allerdings
 * f&uuml;r INSERT und DELETE leicht unterschiedlich. <br>
 * F&uuml;r mehr Information zum Bereich, siehe
 * {@link RSModification#getCompleteText()}
 * 
 * 
 * @author Alexander Daum
 *
 */
public class RSModification {
	private final String completeText;
	private final Span range;
	private final ModificationType type;

	public RSModification(String completeText, Span range, ModificationType type) {
		this.completeText = completeText;
		this.range = range;
		this.type = type;
	}

	public RSModification(String completeText, int start, int end, ModificationType type) {
		this(completeText, new Span(start, end), type);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCompleteText() == null) ? 0 : getCompleteText().hashCode());
		result = prime * result + ((getRange() == null) ? 0 : getRange().hashCode());
		result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RSModification other = (RSModification) obj;
		if (getCompleteText() == null) {
			if (other.getCompleteText() != null)
				return false;
		} else if (!getCompleteText().equals(other.getCompleteText()))
			return false;
		if (getRange() == null) {
			if (other.getRange() != null)
				return false;
		} else if (!getRange().equals(other.getRange()))
			return false;
		if (getType() != other.getType())
			return false;
		return true;
	}

	/**
	 * Gibt den Start (inklusiv) des modifizierten Bereichs zur&uuml;ck. Der Bereich
	 * ist inklusive dieser Startposition
	 * 
	 * @return
	 */
	public int getStart() {
		return getRange().start;
	}

	/**
	 * Gibt das Ende (exklusiv) des modifizierten Bereichs zur&uuml;ck. Der Bereich
	 * enth&auml;lt diese Position nicht mehr
	 * 
	 * @return
	 */
	public int getEnd() {
		return getRange().end;
	}

	/**
	 * Gibt den Text nach dieser Modifikation zur&uuml;ck
	 * 
	 * @return
	 */
	public String getCompleteText() {
		return completeText;
	}

	/**
	 * Gibt den Bereich der Modifikation zur&uuml;ck. Der Bereich ist inklusive des
	 * Beginns, exklusive dem Ende, wie {@link String#substring(int, int)}.
	 * <hr>
	 * 
	 * Bei INSERT das der Bereich, der im neuen Text neu eingef&uuml;gt wurde, so
	 * dass <code>completeText.substring(range.start,range.end)</code> den
	 * eingef&uuml;gten Text zur&uuml;ck gibt <br>
	 * Bei DELETE das der Bereich, der aus dem alten Text gel&ouml;scht wurde. Der
	 * Beginn des Bereichs ist die Stelle im neuen Text wo dieser vorher war, das
	 * Ende hat im neuen Text keine Bedeutung
	 * 
	 * @return
	 */
	public Span getRange() {
		return range;
	}

	/**
	 * Gibt den Typ der Modifikation zur&uuml;ck
	 * 
	 * @return
	 */
	public ModificationType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return type.toString() + " " + range.toString();
	}

	/**
	 * Enum f&uuml;r M&ouml;gliche Modifikationstypen. INSERT und DELETE
	 *
	 */
	public static enum ModificationType {
		INSERT, DELETE
	}
}
