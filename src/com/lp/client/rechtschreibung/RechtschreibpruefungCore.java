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

import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.log4j.Logger;
import org.wildfly.security.manager.action.CreateThreadAction;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.uiproperty.RechtschreibpruefungAktivierenProperty;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.logger.LpLogger;

import dk.dren.hunspell.Hunspell.Dictionary;

/**
 * Kern-Klasse der Rechtschreibpr&uuml;fung. Verwaltet den Core-Thread und das
 * starten der Rechtschreibpr&uuml;fung. Ist derzeit ein Singleton
 * <hr>
 * Dokumentation der Struktur der Rechtschreibpr&uuml;fung: <br>
 * <br>
 * 
 * Der {@link RechtschreibpruefungCore} verwaltet den {@link CoreThread} und ist
 * das Interface zur Steuerung der Rechtschreibpr&uuml;fung. <br>
 * <br>
 * 
 * Der {@link CoreThread} f&uuml;hrt die Rechtschreibpr&uuml;fung durch. Dazu
 * wird periodisch ein {@link RSWorkCollector} abgefragt, der die Vergabe der
 * Arbeit steuert. Der {@link CoreThread} (und nur darin verwendete
 * {@link LanguageToolSupplier}) ist die einzige Klasse mit Verweisen auf
 * externe Rechtschreibpr&uuml;fungs Bibliotheken Der {@link CoreThread} (und
 * nur darin verwendete {@link LanguageToolSupplier}) ist die einzige Klasse mit
 * Verweisen auf externe Rechtschreibpr&uuml;fungs Bibliotheken. Soll die
 * Bibliothek ausgetauscht werden, m&uuml;ssen nur diese Klassen angepasst
 * werden (au&szlig;er wortweise Pr&uuml;fung ist nicht mehr gew&uuml;nscht,
 * z.B. f&uuml;r Grammatik) <br>
 * 
 * Der {@link RSWorkCollector} verwaltet eine Liste mit Referenzen auf alle
 * {@link RSWorkQueue} Instanzen. Jede {@link RSWorkQueue} ist einem
 * Komponenten, der Rechtschreibpr&uuml;fung ben&uuml;tzt zugeorndet. <br>
 * <br>
 * 
 * Jede {@link RSWorkQueue} verwaltet den Text einer Komponente und gibt diese
 * als einzelne, zu &uuml;berpr&uuml;fende W&ouml;rter wieder zur&uuml;ck. <br>
 * Um eine neue {@link RSWorkQueue} zu erhalten, sollte
 * {@link RechtschreibpruefungCore#getWorkQueue()} verwendet werden, das eine
 * neue Instanz erzeugt und im {@link RSWorkCollector} registriert. <br>
 * <br>
 * 
 * Eine Komponente ist eine beliebige Klasse, die {@link IRechtschreibPruefbar}
 * implementiert und die Funktionalit&auml;t entweder selbst implementiert, oder
 * einen RechtschreibAdapter verwendet. <br>
 * <br>
 * 
 * Zur einfachen Rechtschreibpr&uuml;fungs Unterst&uuml;tzung existieren noch
 * zwei Arten von Klassen: RechtschreibAdapter, die von
 * {@link AbstractRechtschreibAdapter} erben und RechtschreibWorker, die von
 * {@link AbstractRechtschreibWorker} erben. <br>
 * Ein RechtschreibWorker verbindet eine Komponente mit einer
 * {@link RSWorkQueue}, gibt also &Auml;nderungen des verwalteten Textes als
 * {@link RSModification} zur {@link RSWorkQueue} und reagiert auf Ergebnisse
 * aus der Rechtschreibpr&uuml;fung. <br>
 * Ein RechtschreibAdapter erm&ouml;glicht eine leichtere Implementierung von
 * Rechtschreibung in Komponenten, da diese Klasse {@link IRechtschreibPruefbar}
 * implementiert und alle darin enthaltenen Aufrufe in einer Komponente an einen
 * Adapter &uuml;bergeben werden k&ouml;nnen.
 * 
 * @author Alexander Daum
 *
 */
public class RechtschreibpruefungCore {

	private RechtschreibpruefungAktivierenProperty arbeitsplatzParam = new RechtschreibpruefungAktivierenProperty();

	private static RechtschreibpruefungCore instance = new RechtschreibpruefungCore();

	/**
	 * Der Rechtschreibpr&uuml;fungs Thread. Ist null, wenn die
	 * Rechtschreibpr&uuml;fung noch nicht intialisiert wurde oder nicht aktiviert
	 * ist.
	 */
	private CoreThread thread;

	private void createThread() {
		thread = new CoreThread();
		thread.setName("RechtschreibpruefungsCoreThread");
	}

	/**
	 * Wird aufgerufen, wenn die eigenen W&ouml;rter neu vom Server geholt werden
	 * sollen
	 */
	public void reloadCustomWords() {
		if(thread != null) {
			thread.reloadCustomWords();
		}
	}

	/**
	 * Initialisiert die Rechtschreibpruefung. Es werden hier 2 Parameter
	 * gepr&uuml;ft:
	 * <ol>
	 * <li>Der Arbeitsplatzparameter RECHTSCHREIBPRUEFUNG</li>
	 * <li>Der Startparameter enablerechtschreibpruefung</li>
	 * </ol>
	 * 
	 * Der Arbeitsplatzparameter und Startparameter sind <code> false </code>, falls
	 * sie nicht gesetzt sind. <br>
	 * Um die Rechtschreibpr&uuml;fung zu aktivieren, muss einer der Parameter
	 * <code> true </code> sein.
	 * 
	 * @param theClientDto
	 * @throws Throwable
	 * @throws ExceptionLP
	 */
	public void initialize(TheClientDto theClientDto) throws ExceptionLP, Throwable {
		boolean enabled = arbeitsplatzParam.get() || Defaults.getInstance().isRechtschreibpruefungEnabled();
		if (enabled) {
			createThread();
			thread.start();
		} else {
			thread = null;
		}
	}

	/**
	 * Der Core-Thread der Rechtschreibung. Dieser holt sich &uuml;ber einen
	 * {@link RSWorkCollector} regelm&auml;&szlig;ig die W&ouml;rter, die
	 * &uuml;berpr&uuml;ft werden m&uuml;ssen und f&uuml;hrt die
	 * Rechtschreibpr&uuml;fung durch.
	 * 
	 * @author Alexander Daum
	 *
	 */
	private static class CoreThread extends Thread {
		/**
		 * Delay zur Abfrage des {@link RSWorkCollector}. Nachdem ein Wort fertig
		 * gepr&uuml;ft wird, wird sofort versucht ein neues Wort zu holen. Ist keines
		 * vorhanden wird diese Zeit in ms gewartet, bevor erneut versucht wird
		 */
		private static final long QUEUE_POLL_DELAY = 300;

		private final RSWorkCollector workQueue;
		private final HunspellDictionaryHandler dictHandler;

		private Logger myLogger = LpLogger.getLogger("Rechtschreibpruefung");

		/**
		 * L&auml;dt die eigenen W&ouml;rter aus der Datenbank neu.
		 * 
		 * @implNote Derzeit werden dei W&ouml;rter erst beir n&auml;chsten
		 *           Rechtschreibpr&uuml;fung neu geladen, diese Funktion l&ouml;scht
		 *           nur die alten W&ouml;rter und ist dadurch schnell
		 * 
		 */
		public void reloadCustomWords() {
			dictHandler.reloadCustomWords();
		}

		public CoreThread() {
			this.workQueue = new RSWorkCollector();
			dictHandler = new HunspellDictionaryHandler();
		}

		@Override
		public void run() {
			while (true) {
				try {
					RSWorkItem nextWork = waitForWork();
					RechtschreibpruefungResult result = check(nextWork);
					nextWork.finished(result);
				} catch (Exception e) {
					myLogger.warn("Exception thrown in Rechtschreibpruefung", e);
				}
			}
		}

		/**
		 * Pr&uuml;ft einen Textteil auf Rechtschreibung und gibt ein
		 * RechtschreibpruefungResult zur&uuml;ck.
		 * 
		 * @param work
		 * @return
		 */
		private RechtschreibpruefungResult check(RSWorkItem work) {
			System.out.println("Checking with " + work.getLocale().toString());
			List<Dictionary> dicts = dictHandler.getDictsForLanguage(work.getLocale());
			if (dicts.isEmpty()) {
				return RechtschreibpruefungResult.emptyResult(work.getToCheck());
			}
			boolean correct = false;
			for (Span span : work.getAllSpansToCheck()) {
				String word = work.getCompleteText().substring(span.start, span.end);
				correct = doCheckWord(dicts, word);
				if (correct) {
					return RechtschreibpruefungResult.asWord(correct, span);
				}
			}
			return RechtschreibpruefungResult.asWord(correct, new Span(work.getStartPos(), work.getEndPos()));
		}

		private boolean doCheckWord(List<Dictionary> dicts, String word) {
			boolean correct = false;
			for (Dictionary dict : dicts) {
				if (!dict.misspelled(word)) {
					correct = true;
					break;
				}
			}
			if (!correct) {
				correct = checkForURL(word);
			}
			return correct;
		}

		/**
		 * Pr&uuml;fe, ob das Wort eine URL ist
		 * 
		 * @param word
		 * @return
		 */
		private boolean checkForURL(String word) {
			boolean isURL = UrlValidator.getInstance().isValid(word);
			isURL = isURL || InetAddressValidator.getInstance().isValid(word);
			return isURL;
		}


		/**
		 * Fragt den {@link RSWorkCollector} ab, ob Arbeit vorhanden ist. Wenn nicht,
		 * wartet diese Funktion und pr&uuml;ft alle
		 * {@link CoreThread#QUEUE_POLL_DELAY}ms wieder ob Arbeit verf&uuml;gbar ist.
		 * 
		 * @return Das neue {@link RSWorkItem}. Ist nie <code>null</code>
		 */
		private RSWorkItem waitForWork() {
			Optional<RSWorkItem> work = workQueue.getWork();
			while (!work.isPresent()) {
				try {
					Thread.sleep(QUEUE_POLL_DELAY);
				} catch (InterruptedException e) {
				}
				work = workQueue.getWork();
			}
			RSWorkItem workItem = work.get();
			return workItem;
		}
	}

	public static RechtschreibpruefungCore getInstance() {
		return instance;
	}

	/**
	 * Erzeugt eine neue {@link RSWorkQueue}, die mit dem {@link RSWorkCollector}
	 * des CoreThreads verkn&uuml;pft ist.
	 * 
	 * @return
	 */
	public RSWorkQueue getWorkQueue() {
		return thread.workQueue.createWorkQueue();
	}

}
