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

import java.util.Comparator;

/**
 * Diese Klasse repr&auml;sentiert einen Bereich von <code>int</code> Werten.
 * Der Bereich geht von inklusive start bis exklusive end (rechtsoffenes
 * Intervall). <br>
 * Diese Klasse bietet zus&auml;tzliche Funktionen um Bereiche zu vergleichen
 * oder zu ver&auml;ndern. <br>
 * Die Klasse ist Immutable. Alle Funktionen, die einen Bereich ver&auml;ndern
 * (z.B. shift oder combine) geben eine neue Instanz von {@link Span}
 * zur&uuml;ck und modifizieren die Urspr&uuml;nglichen Objekte nicht
 * 
 * @author Alexander Daum
 *
 */
public class Span {
	public final int start, end;

	public Span(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public String toString() {
		return String.format("[%d, %d)", start, end);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + end;
		result = prime * result + start;
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
		Span other = (Span) obj;
		if (end != other.end)
			return false;
		if (start != other.start)
			return false;
		return true;
	}

	/**
	 * Pr&uuml;ft, ob dieser Bereich den anderen Bereich &uuml;berschneidet. Eine
	 * &Uuml;berschneidung ist vorhanden, wenn mindestens ein Wert in beiden
	 * Bereichen enthalten ist. <br>
	 * Beispiel: <br>
	 * [0,10) &uuml;berschneidet [8,25)? => Ja <br>
	 * [0,20) &uuml;berschneidet [4,5)? => Ja <br>
	 * [5,10) &uuml;berschneidet [10,15)? => Nein (Ende ist exklusive) <br>
	 * 
	 * <br>
	 * Um auch Bereiche, die zwar keine &Uuml;berschneidung haben, aber direkt
	 * nebeneinander liegen zu finden, kann {@link Span#intersectsOrAdjacent(Span)}
	 * benutzt werden
	 * 
	 * @param other
	 * @return
	 */
	public boolean intersects(Span other) {
		return ((other.end > start) && (other.start < end));
	}

	/**
	 * Pr&uuml;t ob von {@link Span#intersects(Span)} und
	 * {@link Span#isAdjacent(Span)} mindestens eine Bedingung erf&uuml;llt ist
	 * 
	 * @param other
	 * @return
	 */
	public boolean intersectsOrAdjacent(Span other) {
		return intersects(other) || isAdjacent(other);
	}

	/**
	 * Pr&uuml;ft, ob dieser Bereich den anderen Bereich komplett enth&auml;lt
	 * 
	 * 
	 * @param other
	 * @return
	 */
	public boolean contains(Span other) {
		return (other.end <= end) && other.start >= start;
	}

	/**
	 * Pr&uuml;ft, ob dieser Bereich an den anderen Bereich angrenzt, ohne dass sich
	 * beide Bereiche &uuml;berschneiden.
	 * 
	 * @param other
	 * @return
	 */
	public boolean isAdjacent(Span other) {
		return other.end == this.start || this.end == other.start;
	}

	/**
	 * Verschiebt den Bereich um eine bestimmte L&auml;nge. Start und Ende werden um
	 * den gleichen Wert verschoben
	 * 
	 * @param shift
	 * @return
	 */
	public Span shift(int shift) {
		return new Span(start + shift, end + shift);
	}

	/**
	 * Kombiniert diesen Bereich mit einem anderen. Gibt dazu einen neuen Bereich
	 * vom kleineren Startwert zum gr&ouml;&szlig;eren Endwert zur&uuml;ck. Diese
	 * Funktion liefert nur ein korrektes Ergebnis, wenn
	 * {@link Span#intersectsOrAdjacent(Span)} wahr ist. <br>
	 * Der neue Bereich enth&auml;lt garantiert beide Bereiche
	 * 
	 * @param other
	 * @return
	 */
	public Span combine(Span other) {
		return new Span(Math.min(start, other.start), Math.max(end, other.end));
	}

	/**
	 * Gibt die L&aouml;nge dieses Bereichs zur&uuml;ck.
	 * 
	 * @return
	 */
	public int getLength() {
		return end - start;
	}

	public static Comparator<Span> startComparator() {
		return (s1, s2) -> Integer.compare(s1.start, s2.start);
	}
}