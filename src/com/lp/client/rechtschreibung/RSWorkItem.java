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
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Diese Klasse repr&auml;sentiert ein St&uuml;ck Text, das von der
 * Rechtschreibpr&uuml;fung gepr&uuml;ft werden kann. <br>
 * Ein WorkItem enth&auml;lt den kompletten Text, die Sprache, einen Bereich der
 * gepr&uuml;ft werden soll und eine Referenz auf die WorkQueue
 * 
 * @author Alexander Daum
 *
 */
public class RSWorkItem {
	private final String completeText;
	private final Locale locale;
	private final MarkedWord toCheck;
	private final RSWorkQueue queue;

	public RSWorkItem(String completeText, Locale locale, MarkedWord toCheck, RSWorkQueue queue) {
		this.completeText = completeText;
		this.locale = locale;
		this.toCheck = toCheck;
		this.queue = queue;
	}

	public String getCompleteText() {
		return completeText;
	}

	public int getStartPos() {
		return toCheck.start + (toCheck.canIgnoreFirst ? 1 : 0);
	}

	public int getEndPos() {
		return Math.max(toCheck.start, toCheck.end - (toCheck.canIgnoreLast ? 1 : 0));
	}

	public MarkedWord getToCheck() {
		return toCheck;
	}

	public Locale getLocale() {
		return locale;
	}

	/**
	 * Gibt den zu &uuml;berpr&uuml;fenden Teil des Texts zur&uuml;ck.
	 * 
	 * @return
	 */
	public String getStringToCheck() {
		return getCompleteText().substring(getStartPos(), getEndPos());
	}

	/**
	 * Gibt eine Liste mit allen m&ouml;glichen W&ouml;rtern zur&uuml;ck. Wenn
	 * Zeichen, die kein Buchstabe sind an das Wort angrenzen, sind diese
	 * zwischendurch auch Teil des Wortes,
	 * 
	 * @return
	 */
	public Collection<Span> getAllSpansToCheck() {
		List<Span> allStrings = new ArrayList<>();
		allStrings.add(new Span(getStartPos(), getEndPos()));
		if (toCheck.canIgnoreFirst) {
			allStrings.add(new Span(toCheck.start, getEndPos()));
		}
		if (toCheck.canIgnoreLast) {
			allStrings.add(new Span(getStartPos(), toCheck.end));
		}
		if (toCheck.canIgnoreFirst && toCheck.canIgnoreLast) {
			allStrings.add(new Span(toCheck.start, toCheck.end));
		}
		return allStrings;
	}

	/**
	 * Stellt die Rechtschreibpr&uuml;fung dieses WorkItems fertig und sendet das
	 * Ergebnis zur&uuml;ck an die {@link RSWorkQueue} und damit auch an den
	 * {@link AbstractRechtschreibWorker}, der die Pr&uuml;fung gestartet hat
	 * 
	 * @param result
	 */
	public void finished(RechtschreibpruefungResult result) {
		queue.finished(this, result);
	}

	@Override
	public String toString() {
		return String.format("Work on: <%s>, part of <%s>", getStringToCheck(), getCompleteText());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + ((completeText == null) ? 0 : completeText.hashCode());
		result = prime * result + ((queue == null) ? 0 : queue.hashCode());
		result = prime * result + ((toCheck == null) ? 0 : toCheck.hashCode());
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
		RSWorkItem other = (RSWorkItem) obj;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		if (completeText == null) {
			if (other.completeText != null)
				return false;
		} else if (!completeText.equals(other.completeText))
			return false;
		if (queue == null) {
			if (other.queue != null)
				return false;
		} else if (!queue.equals(other.queue))
			return false;
		if (toCheck == null) {
			if (other.toCheck != null)
				return false;
		} else if (!toCheck.equals(other.toCheck))
			return false;
		return true;
	}
	
}
