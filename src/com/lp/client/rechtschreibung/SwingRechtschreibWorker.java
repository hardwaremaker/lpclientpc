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

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.JTextComponent;

import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;

/**
 * Implementierung von {@link AbstractRechtschreibWorker} f&uuml;r Swing
 * {@link JTextComponent}s. <br>
 * Verwendet einen {@link DocumentListener} um auf &Auml;nderungen im Text zu
 * reagieren und einen {@link PropertyChangeListener} um &auml;nderungen des
 * Documents des TextComponents zu unterst&uuml;tzen. <br>
 * F&uuml;r Sprachunterst&uuml;tzung wird kein PropertyChangeListener verwendet.
 * <br>
 * Rechtschreibfehler werden mit einem
 * {@link RechtschreibfehlerHighlightPainter} und Swing {@link Highlighter}
 * markiert <br>
 * 
 * Die Klasse sollte nicht direkt verwendet werden, stattdessen sollte ein
 * {@link SwingRechtschreibAdapter} verwendet werden, der das erstellen,
 * registrieren und entfernen des {@link SwingRechtschreibWorker}s verwaltet
 * 
 * @author Alexander Daum
 *
 */
public class SwingRechtschreibWorker extends AbstractRechtschreibWorker {
	private final JTextComponent textComponent;
	private final DocumentHandler handler;

	private RechtschreibfehlerHighlightPainter painter;
	private PropertyChangeListener docChangeListener;

	private boolean active = true;

	/**
	 * Erstellt einen neuen {@link SwingRechtschreibWorker} und registriert die
	 * Listener auf den {@link JTextComponent}. <br>
	 * Es wird auch direkt eine Rechtschreibpr&uuml;fung mit dem derzeit im
	 * textComponent enthaltenen Text gestartet
	 * 
	 * @param textComponent
	 * @param locale
	 * @param workQueue
	 */
	protected SwingRechtschreibWorker(JTextComponent textComponent, Locale locale, RSWorkQueue workQueue) {
		super(locale, workQueue);
		this.textComponent = textComponent;
		painter = new RechtschreibfehlerHighlightPainter(Color.red);
		handler = new DocumentHandler();
		textComponent.getDocument().addDocumentListener(handler);
		docChangeListener = this::onDocumentChange;
		textComponent.addPropertyChangeListener("document", docChangeListener);

		// Wenn erstellt wird gleich pruefen
		onCompleteTextChange(getText());
	}

	/**
	 * Methode die vom {@link PropertyChangeListener} f&uuml;r das "document"
	 * property aufgerufen wird. Entfernt den {@link DocumentListener} vom alten
	 * Document und f&uuml;gt diesen zum neuen Document hinzu, danach wird eine neue
	 * Pr&uuml;fung des ganzen Textes gestartet, da sich dieser mit dem Docuemnt
	 * ge&auml;ndert haben k&ouml;nnte
	 * 
	 * @param e
	 */
	private void onDocumentChange(PropertyChangeEvent e) {
		Document oldDoc = (Document) e.getOldValue();
		Document newDoc = (Document) e.getNewValue();

		oldDoc.removeDocumentListener(handler);
		newDoc.addDocumentListener(handler);

		onCompleteTextChange(getText());
	}

	protected final void updateUI(RechtschreibpruefungResult result) {
		if (!active) {
			return;
		}
		SwingUtilities.invokeLater(() -> {
			doUpdateUI(result);
		});
	}

	/**
	 * Aktualisiert die Markierungen f&uuml;r Fehler. <br>
	 * Hier werden zuerst alle Markierungen im gepr&uuml;ften Bereich, die vom
	 * painter dieses RechtschreibWorkers erstellt wurden, gel&ouml;scht, danach
	 * werden f&uuml;r alle Fehler neue Markierungen erstellt
	 * 
	 * @param result
	 */
	protected void doUpdateUI(RechtschreibpruefungResult result) {
		Highlighter highlighter = textComponent.getHighlighter();
		Span checkedSpan = result.getCheckedSpan();
		for (Highlight hl : highlighter.getHighlights()) {
			if (hl.getPainter().equals(painter)) {
				Span highlightSpan = new Span(hl.getStartOffset(), hl.getEndOffset());
				if (checkedSpan.intersects(highlightSpan)) {
					highlighter.removeHighlight(hl);
				}
			}
		}
		for (Span match : result.getFehler()) {
			if (match.start == match.end)
				continue;
			try {
				highlighter.addHighlight(match.start, match.end, painter);
			} catch (BadLocationException e) {
			}
		}
	}

	@Override
	protected void onFail(RechtschreibpruefungResult result, int failCount) {
		if (!active) {
			return;
		}
		// Bis zu 3 mal neu pruefen. Wenn dann immer noch Fehler passieren, aufgeben
		if (failCount <= 3) {
			onCompleteTextChange(getText());
		} else {
			// Bei 3 Fehlschl&auml;gen alle Markierungen l&ouml;schen und die
			// Rechtschreibpr&uuml;fung von diesem Worker deaktivieren
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("rechtschreibung.fehler"),
					LPMain.getTextRespectUISPr("rechtschreibung.fehler.text"));
			removeFromComponent();
			active = false;
		}
	}

	private void removeHighlights() {
		Highlighter highlighter = textComponent.getHighlighter();
		for (Highlight hl : highlighter.getHighlights()) {
			if (hl.getPainter().equals(painter)) {
				highlighter.removeHighlight(hl);
			}
		}
	}

	/**
	 * Entfernt alle Listener dieses {@link SwingRechtschreibWorker}s von dem
	 * TextComponent. Soll aufgerufen werden, wenn die Rechtschreibpr&uuml;fung
	 * deakiviert werden soll
	 */
	public void removeFromComponent() {
		textComponent.getDocument().removeDocumentListener(handler);
		textComponent.removePropertyChangeListener(docChangeListener);
		removeHighlights();
		active = false;
	}

	private String getText() {
		Document doc = textComponent.getDocument();
		try {
			return doc.getText(0, doc.getLength());
		} catch (BadLocationException e) {
			return "";
		}
	}

	/**
	 * DocumentListener implementierung f&uuml;r {@link SwingRechtschreibWorker}
	 * 
	 * @author Alexander Daum
	 *
	 */
	private class DocumentHandler implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			onInsert(getText(), e.getOffset(), e.getOffset() + e.getLength());
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			onDelete(getText(), e.getOffset(), e.getOffset() + e.getLength());
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			// Keine Textaenderung
		}
	}

}
