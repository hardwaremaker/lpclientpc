package com.lp.client.rechtschreibung;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.lp.client.rechtschreibung.RSModification.ModificationType;

public class WordTokenizer {

	private static final Pattern patternWhitespace = Pattern.compile("\\S+");

	private TokenizedText data;
	private boolean markedWordsPresent = true;
	private final Locale locale;

	public WordTokenizer(Locale locale) {
		this.locale = locale;
	}

	public String getText() {
		return data.currentText;
	}

	public Locale getLocale() {
		return locale;
	}

	public Optional<MarkedWord> getWordMarkedBefore(long maxTimestamp) {
		if (!markedWordsPresent) {
			return Optional.empty();
		}
		markedWordsPresent = false;
		for (MarkedWord word : data.words.asMapOfRanges().values()) {
			if (word.isMarked()) {
				markedWordsPresent = true;
				if (word.getMarkedTime() <= maxTimestamp) {
					return Optional.of(word);
				}
			}
		}
		return Optional.empty();
	}

	/**
	 * Zerteilt den Text completeText in W&ouml;rter und gibt eine neue instanz von
	 * {@link TokenizedText} zur&uuml;ck. Jedes Wort ist als dirty markiert
	 * 
	 * @param completeText Der Text
	 * @return Einen neuen {@link TokenizedText}
	 */
	public TokenizedText tokenizeCompleteText(String completeText) {
		markedWordsPresent = true;
		TreeRangeMap<Integer, MarkedWord> newWords = TreeRangeMap.create();
		Matcher matcher = patternWhitespace.matcher(completeText);
		while (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			boolean canIgnoreFirstChar = !Character.isAlphabetic(completeText.codePointAt(matcher.start()));
			boolean canIgnoreLastChar = !Character.isAlphabetic(completeText.codePointAt(matcher.end() - 1));
			MarkedWord newSpan = new MarkedWord(start, end, canIgnoreFirstChar, canIgnoreLastChar);
			newSpan.setMarked(true);
			newWords.put(Range.closedOpen(start, end), newSpan);
		}
		return new TokenizedText(completeText, newWords, locale);
	}

	/**
	 * Synchronisiert die W&ouml;rterteilung im {@link TokenizedText} prevData mit
	 * einer Modifikation und gibt einen neuen {@link TokenizedText} zur&uuml;ck.
	 * prevData wird nicht modifiziert. <br>
	 * Der text in dem neuen {@link TokenizedText} entspricht dem Text nach der
	 * Modifikation und die W&ouml;rtereinteilung und hat das gleiche Locale wie
	 * prevData. Falls eine &Auml;nderung des Locales durchgef&uuml;hrt wird, muss
	 * stattdessen {@link RSWorkQueue#tokenizeCompleteText(Locale, String)}
	 * aufgerufen werden. <br>
	 * Alle W&ouml;rter, die nicht von der Modifikation betroffen sind, behalten
	 * ihren vorherigen Zustand, alle betroffenen werden als dirty markiert. <br>
	 * Ein Betroffenes Wort, ist ein Wort, das sich durch die Modifikation
	 * ge&auml;ndert haben k&ouml;nnte. <br>
	 * <hr>
	 * Beispiele: <br>
	 * Insert: <br>
	 * vorher: Das ist ein Beispieltext <br>
	 * nachher: Das ist kein Beispieltext <br>
	 * Modifikation = INSERT mit Bereich (8,9). <br>
	 * Hier werden die W&ouml;rter Das und ist unver&auml;ndert &Uuml;bernommen, ein
	 * wird entfernt und stattdessen wird kein hinzugef&uuml;gt und als dirty
	 * markiert. Beispieltext bleibt ebenfalls erhalten, allerdings wird die
	 * Position dieses Wortes ge&auml;ndert. (Impl. Note: In der aktuellen
	 * implementierung von {@link TokenizedText} besteht ein Wort nur aus der dirty
	 * markierung und einem Anfangs- und Endbereich, die positionen sind Immutable,
	 * deshalb ist ein verschieben eines Wortes das gleiche wie das erstellen eines
	 * neuen Wortes mit den richtigen Positionen und setzen des Dirty Flags auf den
	 * gleichen Zustand) <br>
	 * <br>
	 * Delete: <br>
	 * vorher: Das ist ein Beispieltext <br>
	 * nachher: Das ist einBeispieltext <br>
	 * Modifikation = DELETE mit Bereich (12,13) <br>
	 * Bei einer DELETE Modifikation m&uuml;ssen nur die W&ouml;rter beachtet
	 * werden, die an den Startpunkt im neuen Text angrenzen, alle anderen sind
	 * nicht modifiziert oder komplett gel&ouml;scht. Da in der aktuellen
	 * Implementierung immer alle W&ouml;rter nach start der Modifikation neu
	 * gesucht werden m&uuml;ssen, passiert das automatisch. <br>
	 * In diesem Fall werden die W&ouml;rter ein und Beispieltext gel&ouml;scht und
	 * ein Wort einBeispieltext eingef&uuml;gt
	 * 
	 * 
	 * 
	 * @param modification
	 * @param prevData
	 * @return
	 */
	public void synchronizeWords(RSModification modification) {
		TreeRangeMap<Integer, MarkedWord> newWords = TreeRangeMap.create();
		String currentText = modification.getCompleteText();

		int startInText = 0;
		if (data != null) {
			Span startWord = data.words.get(modification.getStart() - 1);
			if (startWord == null) {
				startInText = modification.getStart();
			} else {
				startInText = startWord.start;
			}
			newWords.putAll(data.words.subRangeMap(Range.lessThan(startInText)));
		}
		String newText = currentText.substring(startInText);
		Matcher matcher = patternWhitespace.matcher(newText);
		int offset = modification.getRange().getLength();
		if (modification.getType() == ModificationType.DELETE) {
			offset = -offset;
		}
		while (matcher.find()) {
			int realStart = matcher.start() + startInText;
			int realEnd = matcher.end() + startInText;
			boolean isNewOrModified = false;
			switch (modification.getType()) {
			case INSERT:
				isNewOrModified = realStart <= modification.getEnd();
				break;
			case DELETE:
				isNewOrModified = realStart <= modification.getStart();
				break;
			}
			MarkedWord newSpan;
			if (!isNewOrModified) {
				int startBefore = realStart - offset;
				MarkedWord oldWord = data.words.get(startBefore);
				newSpan = new MarkedWord(realStart, realEnd, oldWord.canIgnoreFirst, oldWord.canIgnoreLast);
				newSpan.copyMarkingFrom(oldWord);
			} else {
				markedWordsPresent = true;
				// Neues Wort, oder wort mit delete drin
				boolean canIgnoreFirstChar = !Character.isAlphabetic(newText.codePointAt(matcher.start()));
				int size = matcher.end() - matcher.start();
				boolean canIgnoreLastChar = (size > 1) && !Character.isAlphabetic(newText.codePointAt(matcher.end() - 1));
				newSpan = new MarkedWord(realStart, realEnd, canIgnoreFirstChar, canIgnoreLastChar);
				newSpan.setMarked(true);
			}
			newWords.put(Range.closedOpen(realStart, realEnd), newSpan);
		}
		this.data = new TokenizedText(currentText, newWords, locale);
	}

	/**
	 * Klasse, die alle Daten eines in W&ouml;rter geteilten Textes enth&auml;lt.
	 * Ist immutable
	 *
	 */
	public static class TokenizedText implements Cloneable {
		public final String currentText;
		public final ImmutableRangeMap<Integer, MarkedWord> words;

		public TokenizedText(String currentText, RangeMap<Integer, MarkedWord> words, Locale locale) {
			this.currentText = currentText;
			this.words = ImmutableRangeMap.copyOf(words);
		}
	}
}
