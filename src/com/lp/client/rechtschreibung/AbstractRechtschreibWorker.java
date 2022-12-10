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

import com.lp.client.rechtschreibung.RSModification.ModificationType;
import com.lp.client.rechtschreibung.RechtschreibpruefungResult.RechtschreibpruefungResultStatus;

/**
 * Superklasse aller RechtschreibWorker. Verwaltet das senden von Modifikationen
 * zur {@link RSWorkQueue}. Pro {@link RSWorkQueue} darf nur ein
 * {@link AbstractRechtschreibWorker} exisiteren, ansonsten kann es zu seltsamen
 * Fehlern kommen, da die Texte gemischt werden! Es sollte bei erstellen eines
 * RechtschreibWorkers deshalb immer eine neue {@link RSWorkQueue} erstellt
 * werden.
 * 
 * F&uuml;r mehr Informationen zur Struktur der Rechtschreibpr&uuml;fung siehe
 * {@link RechtschreibpruefungCore}
 * 
 * @author Alexander Daum
 *
 */
public abstract class AbstractRechtschreibWorker {
	private final RSWorkQueue workQueue;
	private int failCount = 0;

	protected AbstractRechtschreibWorker(Locale locale, RSWorkQueue queue) {
		this.workQueue = queue;
		workQueue.changeLocale(locale, "");
		queue.setCallback(this::updateUIIfSuccess);
	}

	/**
	 * Sendet einen neuen Text zur Rechtschreibpruefung. Der komplette Text wird neu
	 * geprueft, bei Fertigstellung wird updateUI aufgerufen. <br>
	 * Der Text wird in einem anderen Thread geprueft und eventuell mit verzoegerung
	 * gestartet. Wird diese Methode erneut aufgerufen, bevor der vorherige Text
	 * fertig geprueft wurde, wird die alte Pruefung unter umstaenden abgebrochen.
	 * <br>
	 * Die pruefung beginnt erst nach einem kurzen delay um nicht eine neue
	 * Rechtschreibpruefung pro getipptem Buchstaben zu starten
	 * 
	 * @param newText Der text, der geprueft werden soll. <br>
	 *                Bei null wird die rechtschreibpruefung abgebrochen
	 */
	protected final void onCompleteTextChange(String newText) {
		if (newText == null) {
			return;
		}
		onInsert(newText, 0, newText.length());
	}

	/**
	 * Sendet eine neue INSERT Modifikation an die Rechtschreibpr&uuml;fung. <br>
	 * Neue und eventuell Modifizierte W&ouml;rter werden gepr&uuml;ft.
	 * 
	 * @param completeText Der neue, ganze Text
	 * @param startSection Start des Einf&uuml;gens
	 * @param endSection   Ende des Einf&uuml;gens
	 */
	protected final void onInsert(String completeText, int startSection, int endSection) {
		workQueue.putModification(new RSModification(completeText, startSection, endSection, ModificationType.INSERT));
	}

	/**
	 * Sendet eine neue DELETE Modifikation an die Recthschreibpr&uuml;fung <br>
	 * Eventuell modifizierte W&ouml;rter werden dadurch gepr&uuml;ft
	 * 
	 * @param complString  Der neue, ganze Text
	 * @param startSection Start des L&ouml;schens, ist auch die Position im neuen
	 *                     Text, an der der gel&ouml;schte Bereich war
	 * @param endSection   Ende des L&ouml;schens im alten Text, bzw. Start +
	 *                     L&aouml;nge des l&ouml;schens
	 */
	protected final void onDelete(String complString, int startSection, int endSection) {
		workQueue.putModification(new RSModification(complString, startSection, endSection, ModificationType.DELETE));
	}

	/**
	 * &Auml;ndert die Sprache der Rechtschreibpr&uuml;fung. Dadurch wird der
	 * gesamte Text neu gepr&uuml;ft
	 * 
	 * @param locale       Die neue Sprache
	 * @param completeText Der gesamte Text
	 */
	public void setLocale(Locale locale, String completeText) {
		workQueue.changeLocale(locale, completeText);
	}

	/**
	 * Wird von der {@link RSWorkQueue} aufgerufen, wenn eine
	 * Rechtschreibpr&uuml;fung fertig ist.
	 * 
	 * @param result
	 */
	private void updateUIIfSuccess(RechtschreibpruefungResult result) {
		if (result.getStatus() == RechtschreibpruefungResultStatus.SUCCESS) {
			failCount = 0;
			updateUI(result);
		} else {
			onFail(result, ++failCount);
		}
	}

	/**
	 * Wird aufgerufen, wenn die Rechtschreibpr&uuml;fung fehlgeschlagen ist.
	 * failCount ist die Anzahl aufeinanderfolgender Fehlschl&auml;ge.
	 * 
	 * @param result
	 * @param failCount
	 */
	protected void onFail(RechtschreibpruefungResult result, int failCount) {

	}

	/**
	 * Wird aufgerufen, wenn eine Rechtschreibpruefung fertig ist.
	 * 
	 * @param result
	 */
	protected abstract void updateUI(RechtschreibpruefungResult result);
}
