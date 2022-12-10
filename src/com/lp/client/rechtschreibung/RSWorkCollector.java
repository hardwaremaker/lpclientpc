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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Verwaltete mehrere {@link RSWorkQueue} instanzen. Die Queues sind intern als
 * {@link WeakReference} gespeichert und k&ouml;nnen dadurch automatisch
 * entfernt werden, wenn keine anderne Referenzen mehr bestehen. Eine
 * Implementierung von {@link RSWorkQueue} darf sich darauf allerdings nicht
 * verlassen, da dieses Verhalten vom GarbageCollector gesteuert wird und
 * implementierungsabh&aouml;ngig ist.<br>
 * Es sollte nur eine Instanz dieser Klasse pro WorkerThread existieren, da
 * {@link RSWorkQueue} keine concurrency unterst&uuml;tzt und bei zwei
 * gleichzeitigen Zugriffen aus verschiedenen Threads auf die gleiche Queue
 * zugegriffen werden kann
 * 
 * @author Alexander Daum
 *
 */
public class RSWorkCollector {
	private List<WeakReference<RSWorkQueue>> workQueues;

	public RSWorkCollector() {
		workQueues = new ArrayList<>();
	}

	/**
	 * Erstellt eine neue {@link RSWorkQueue} und verkn&uuml;pft diese mit diesem
	 * {@link RSWorkCollector}
	 * 
	 * @return
	 */
	public synchronized RSWorkQueue createWorkQueue() {
		RSWorkQueue queue = new RSWorkQueue();
		workQueues.add(new WeakReference<>(queue));
		return queue;
	}

	/**
	 * Hole arbeit von einer WorkQueue, gibt {@link Optional#empty()} zur&uuml;ck,
	 * wenn keine Arbeit verf&uuml;gbar ist. <br>
	 * Diese Methode hat keinen fairen Algorithmus um arbeit von mehreren queues zu
	 * verteilen.
	 * 
	 * @return
	 */
	public synchronized Optional<RSWorkItem> getWork() {
		Iterator<WeakReference<RSWorkQueue>> iter = workQueues.iterator();
		while (iter.hasNext()) {
			RSWorkQueue queue = iter.next().get();
			if (queue == null) {
				iter.remove();
				continue;
			}
			Optional<RSWorkItem> workItem = queue.getWork();
			if (workItem.isPresent()) {
				return workItem;
			}
		}
		return Optional.empty();
	}
}
