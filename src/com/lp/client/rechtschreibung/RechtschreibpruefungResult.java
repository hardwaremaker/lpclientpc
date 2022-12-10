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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stellt das Ergebnis der Rechtschreibpr&uuml;fung dar. <br>
 * Obwohl derzeit nur einzelne W&ouml;rter gepr&uuml;ft werden, hat diese Klasse
 * die m&ouml;glichkeit mehrere Fehler zu behandeln, alle Klassen, die ein
 * {@link RechtschreibpruefungResult} erhalten, sollten davon ausgehen, dass es
 * in der Zukunft wieder m&ouml;glich sein kann, mehrere Fehler gleichzeitig zu
 * erhalten. <br>
 * Der Fehler wird nur &uuml;ber eine Liste von falschen Bereichen im
 * gepr&uuml;ften Text &uuml;bergeben, der Text selbst ist nicht vorhanden.
 * Durch {@link RSWorkQueue#finished(RSWorkItem, RechtschreibpruefungResult)}
 * wird das Result immer auf den aktuellen Text angepasst.
 * 
 * @author Alexander Daum
 *
 */
public class RechtschreibpruefungResult {
	/**
	 * Liste aller Fehler, Jeder Fehler ist ein Bereich im gepr&uuml;ften Text. Die
	 * Fehler sind immer nach Startposition aufsteigend sortiert
	 */
	private final List<Span> fehler;
	/**
	 * Der Bereich, der von der Rechtschreibpr&uuml;fung gepr&uuml;ft wurde.
	 */
	private final Span checkedSpan;
	private final RechtschreibpruefungResultStatus status;

	/**
	 * Erzeugt ein neues {@link RechtschreibpruefungResult} ohne Fehler von dem
	 * gegebenen Bereich
	 * 
	 * @param checkedSpan
	 * @return
	 */
	public static RechtschreibpruefungResult emptyResult(Span checkedSpan) {
		return new RechtschreibpruefungResult(Collections.emptyList(), checkedSpan,
				RechtschreibpruefungResultStatus.SUCCESS);
	}

	/**
	 * Erzeugt ein neues {@link RechtschreibpruefungResult} mit einem Fehler, der
	 * den ganzen gegebenen Bereich gro&szlig; ist
	 * 
	 * @param checkedSpan
	 * @return
	 */
	public static RechtschreibpruefungResult asWrongWord(Span checkedSpan) {
		return new RechtschreibpruefungResult(singletonArrayList(checkedSpan), checkedSpan,
				RechtschreibpruefungResultStatus.SUCCESS);
	}

	public static RechtschreibpruefungResult asWord(boolean correct, Span checkedSpan) {
		return new RechtschreibpruefungResult(
				correct ? Collections.emptyList() : singletonArrayList(checkedSpan), checkedSpan,
				RechtschreibpruefungResultStatus.SUCCESS);
	}

	/**
	 * Erzeuge ein neues {@link RechtschreibpruefungResult} mit Status
	 * {@link RechtschreibpruefungResultStatus#FAIL}, keinen gefundenen Fehlern und
	 * keinem gepr&uuml;ften Bereich
	 * 
	 * @return
	 */
	public static RechtschreibpruefungResult failedResult() {
		return new RechtschreibpruefungResult(Collections.emptyList(), new Span(0, 0),
				RechtschreibpruefungResultStatus.FAIL);
	}

	/**
	 * Erzeugt ein neues {@link RechtschreibpruefungResult} aus einer Liste von
	 * Bereichen, die realtiv zu checkedSpan angegeben sind.
	 * 
	 * @param relativeSpans Bereiche mit Fehlern, positionen relativ zu checkedSpan
	 * @param checkedSpan   Der Bereich, in dem gepr&uuml;ft wurde
	 * @param status        Status der Pr&uuml;fung
	 * @return
	 */
	public static RechtschreibpruefungResult fromRelativeSpans(List<Span> relativeSpans, Span checkedSpan,
			RechtschreibpruefungResultStatus status) {
		List<Span> absoluteSpans = new ArrayList<>(relativeSpans.size());
		for (Span rel : relativeSpans) {
			Span absolute = new Span(checkedSpan.start + rel.start, checkedSpan.start + rel.end);
			absoluteSpans.add(absolute);
		}
		return fromAbsoluteSpans(absoluteSpans, checkedSpan, status);
	}

	/**
	 * Erzeugt ein neues {@link RechtschreibpruefungResult} aus einer Liste von
	 * Bereichen, die die absolute Position im Text anzeigen. Es m&uuml;ssen alle
	 * Bereiche innerhalb von checkedSpan liegen.
	 * 
	 * @param absoluteSpans Liste der Bereiche mit Fehlern
	 * @param checkedSpan   Der gepr&uuml;fte Bereich
	 * @param status        Status der Pru&uuml;fung
	 * @return
	 */
	public static RechtschreibpruefungResult fromAbsoluteSpans(List<Span> absoluteSpans, Span checkedSpan,
			RechtschreibpruefungResultStatus status) {
		return new RechtschreibpruefungResult(absoluteSpans, checkedSpan, status);
	}

	private RechtschreibpruefungResult(List<Span> absoluteSpans, Span checkedSpan,
			RechtschreibpruefungResultStatus status) {
		//Wenn die Liste nicht leer ist, dann sollte es eine ArrayList sein. Wenn nicht konvertieren
		if(!(absoluteSpans.isEmpty() || absoluteSpans instanceof ArrayList)) {
			absoluteSpans = new ArrayList<Span>(absoluteSpans);
		}
		this.fehler = absoluteSpans;
		fehler.sort((s1, s2) -> Integer.compare(s1.start, s2.start));
		this.checkedSpan = checkedSpan;
		this.status = status;
	}

	public RechtschreibpruefungResultStatus getStatus() {
		return status;
	}

	/**
	 * Gibt die Liste von Bereichen zur&uuml;ck, in denen Fehler gefunden wurden.
	 * Die Liste ist nach start der Bereiche sortiert
	 * 
	 * @return
	 */
	public List<Span> getFehler() {
		return fehler;
	}

	/**
	 * Gibt den Bereich zur&uuml;ck, der von der Rechtschreibpr&uuml;fung
	 * gepr&uuml;ft wurde
	 * 
	 * @return
	 */
	public Span getCheckedSpan() {
		return checkedSpan;
	}

	@Override
	public String toString() {
		return String.format("Range: %s, Results: %s", checkedSpan, fehler);
	}
	
	private static<T> List<T> singletonArrayList(T t) {
		ArrayList<T> list = new ArrayList<T>();
		list.add(t);
		return list;
	}

	/**
	 * Enum f&uuml;r den Status des Rechtschreibpr&uuml;fungResults
	 * 
	 * @author Alexander Daum
	 *
	 */
	public static enum RechtschreibpruefungResultStatus {
		/**
		 * Zeigt an, dass die Rechtschreibpr&uuml;fung erfolgreich durchgef&uuml;hrt
		 * wurde, dabei ist es egal, ob Fehler gefunden wurden, oder nicht
		 */
		SUCCESS,
		/**
		 * Zeigt an, dass die Rechtschreibpr&uuml;fung durch etwas abgebrochen wurde.
		 * Der Grund kann eine Exception sein
		 */
		FAIL
	}
}
