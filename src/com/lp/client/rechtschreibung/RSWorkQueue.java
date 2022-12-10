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

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

import com.lp.client.rechtschreibung.RSModification.ModificationType;
import com.lp.client.rechtschreibung.RechtschreibpruefungResult.RechtschreibpruefungResultStatus;

/**
 * Diese Klasse verwaltet das Aufteilen eines Textes in zu pr&uuml;fende
 * W&ouml;rter und das &uuml;bergeben dieser W&ouml;rter an die
 * Rechtschreibpr&uuml;fung. <br>
 * Diese Klasse ist nur dazu geeignet, einen einzigen Text zu verwalten, es muss
 * also eine separate Instanz dieser Klasse pro
 * {@link AbstractRechtschreibWorker} vorhanden sein. <br>
 * Um mehrere Texte gleichzeitig pr&uuml;fen zu k&ouml;nnen wird ein
 * {@link RSWorkCollector} ben&ouml;tigt, der mehrere {@link RSWorkQueue}
 * Instanzen verwaltet <br>
 * 
 * F&uuml;r mehr Informationen zur Struktur der Rechtschreibpr&uuml;fung siehe
 * {@link RechtschreibpruefungCore}
 * 
 * @author Alexander Daum
 *
 */
public class RSWorkQueue {

	/**
	 * Zeit in ms, die nach Aenderung vergangen sein muss, bevor eine
	 * Rechtschreibpruefung verursacht wird.
	 */
	private static final int DELAY_BEFORE_CHECKING = 300;

	private ConcurrentLinkedDeque<RSModification> queue;
	private Consumer<RechtschreibpruefungResult> callback;
	private WordTokenizer wordTokenizer;

	private Optional<Locale> localeChange = Optional.empty();
	private Optional<String> localeChangeText = Optional.empty();

	public RSWorkQueue() {
		queue = new ConcurrentLinkedDeque<>();
	}

	public void putModification(RSModification modification) {
		queue.addLast(modification);
	}

	/**
	 * &Auml;ndert das locale. Dadurch wird der gesamte aktuelle Text neu
	 * gepr&uuml;ft (falls text vorhanden ist)
	 * 
	 * @param loc
	 */
	protected synchronized void changeLocale(Locale loc, String text) {
		localeChange = Optional.of(loc);
		localeChangeText = Optional.of(text);
	}

	/**
	 * Holt das n&auml;chste Wort, das von der Rechtschreibpr&uuml;fung gepr&uuml;ft
	 * werden soll. Falls kein Wort gepr&uuml;ft werden soll, wird
	 * {@link Optional#empty()} zur&uuml;ck gegeben
	 * 
	 * @return Ein Optional, das das n&auml;chste zu pr&uuml;fende Wort
	 *         enth&auml;lt, oder Optional.empty(), wenn kein Wort mehr zu
	 *         pr&uuml;fen ist.
	 */
	protected Optional<RSWorkItem> getWork() {
		synchronized(this) {
			if (localeChange.isPresent()) {
				wordTokenizer = new WordTokenizer(localeChange.get());
				wordTokenizer.tokenizeCompleteText(localeChangeText.orElse(""));
				localeChange = Optional.empty();
				localeChangeText = Optional.empty();
			}
		}
		
		//Sprache noch nicht intialisiert
		if(wordTokenizer == null) {
			return Optional.empty();
		}

		while (!queue.isEmpty()) {
			wordTokenizer.synchronizeWords(queue.poll());
		}

		long currentTime = System.currentTimeMillis();
		long minAge = currentTime - DELAY_BEFORE_CHECKING;
		Optional<MarkedWord> word = wordTokenizer.getWordMarkedBefore(minAge);
		if (word.isPresent()) {
			RSWorkItem workItem = new RSWorkItem(wordTokenizer.getText(), wordTokenizer.getLocale(), word.get(), this);
			word.get().setMarked(false);
			return Optional.of(workItem);
		}

		return Optional.empty();
	}

	/**
	 * Stelle Rechtschreibpr&uuml;fung fertig. Hier wird noch die Position der
	 * Markierungen im Resultat angepasst, falls inzwischen neue Modifikationen
	 * bekannt wurden. <br>
	 * Dieser Aufruf <b>muss</b> fuer ein gepr&uuml;ftes Wort aufgerufen werden,
	 * bevor das n&auml;chste mal {@link RSWorkQueue#getWork()} aufgerufen wird,
	 * ansonsten werden Modifikationen aus der Queue bereits entfernt und die
	 * Positionen des Resultats k&ouml;nnten falsch (veraltet) sein. <br>
	 * Sollte im gleichen Thread aufgerufen werden, der urspr&uuml;nglich
	 * {@link RSWorkQueue#getWork()} aufgerufen hat
	 * 
	 * @param work   Das {@link RSWorkItem}, das fertig gepr&uuml;ft wurde
	 * @param result Das Ergebnis der Rechtschreibpr&uuml;fung
	 */
	protected void finished(RSWorkItem work, RechtschreibpruefungResult result) {
		if (result.getStatus() != RechtschreibpruefungResultStatus.SUCCESS) {
			callback.accept(result);
			return;
		}
		
		int checkOffset = 0;
		for (RSModification scheduled : queue) {
			checkOffset += calculateResultModification(result, scheduled);
		}
		Span newCheckedSpan = result.getCheckedSpan().shift(checkOffset);
		callback.accept(
				RechtschreibpruefungResult.fromAbsoluteSpans(result.getFehler(), newCheckedSpan, result.getStatus()));
	}

	/**
	 * Berechnet &Auml;nderungen auf Ergebnisbereiche, die von neuem WorkItem (und
	 * der ausl&ouml;senden Modificatio) verursacht werden. Die Fehler Spans werden
	 * direkt in der Liste modifiziert, um den Range zu &auml;ndern, wird ein offset
	 * zur&uuml;ck gegeben, um den der Pr&uuml;fbereich verschoben werden muss.
	 * 
	 * @implNote neue Modifikationen, die &uuml;berschneidungen haben, m&uuml;ssen
	 *           nicht richtig ber&uuml;cksichtigt werden, da diese sowieso den
	 *           betroffenen Bereich neu pr&uuml;fen m&uuml;ssen
	 * 
	 * @param result
	 * @param workToConsider
	 * @return
	 */
	private int calculateResultModification(RechtschreibpruefungResult result, RSModification modification) {
		List<Span> spans = result.getFehler();
		int offset = modification.getEnd() - modification.getStart();
		int checkOffset = 0;
		if (modification.getType() == ModificationType.DELETE) {
			// Bei delete nach links verschieben
			offset = -offset;
		}
		if (modification.getType() == ModificationType.INSERT || modification.getType() == ModificationType.DELETE) {
			int idx = binarySearchSpan(spans, modification.getStart());
			if (idx < 0) {
				idx = (-idx) - 1;
			}
			for (; idx < spans.size(); idx++) {
				Span old = spans.get(idx);
				Span newSpan = old.shift(offset);
				spans.set(idx, newSpan);
			}
			if (modification.getStart() <= result.getCheckedSpan().start + checkOffset) {
				checkOffset = offset;
			}
		}
		return checkOffset;
	}

	protected void setCallback(Consumer<RechtschreibpruefungResult> callback) {
		this.callback = callback;
	}

	private int binarySearchSpan(List<Span> spans, int start) {
		return Collections.binarySearch(spans, new Span(start, 0), (s1, s2) -> Integer.compare(s1.start, s2.start));
	}

}
