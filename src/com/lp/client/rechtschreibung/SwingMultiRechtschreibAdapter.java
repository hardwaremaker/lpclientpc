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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.swing.text.JTextComponent;

/**
 * Eine Implementierung von {@link AbstractRechtschreibAdapter}, die mehrere
 * Swing Text Components verwaltet. Kann verwendet werden, wenn dynamisch
 * generierte Swing Text Componenten eine Rechtschreibpruefung brauchen, die
 * TextComponents selbst aber nicht {@link IRechtschreibPruefbar} sein sollen
 * oder k&ouml;nnen (z.B. Swing JTextField)
 * 
 * @author Alexander Daum
 *
 */
public class SwingMultiRechtschreibAdapter extends AbstractRechtschreibAdapter {
	private List<SwingRechtschreibAdapter> allAdapters;
	private boolean active;
	private Locale loc;

	public SwingMultiRechtschreibAdapter() {
		allAdapters = new ArrayList<>();
		active = false;
	}

	@Override
	public void aktiviereRechtschreibpruefung(Locale loc) {
		if(active) {
			return;
		}
		Objects.nonNull(loc);
		allAdapters.forEach(ad -> ad.aktiviereRechtschreibpruefung(loc));
		active = true;
		this.loc = loc;
	}

	@Override
	public void deaktiviereRechtschreibpruefung() {
		allAdapters.forEach(AbstractRechtschreibAdapter::deaktiviereRechtschreibpruefung);
		active = false;
	}

	@Override
	public void setRechtschreibpruefungLocale(Locale loc) {
		Objects.nonNull(loc);
		allAdapters.forEach(ad -> ad.setRechtschreibpruefungLocale(loc));
		this.loc = loc;
	}

	/**
	 * Fuegt einen neuen Component hinzu
	 * 
	 * @param component
	 */
	public void addComponent(JTextComponent component) {
		Objects.nonNull(component);
		SwingRechtschreibAdapter newAdapter = new SwingRechtschreibAdapter(component);
		if (active)
			newAdapter.aktiviereRechtschreibpruefung(loc);
		allAdapters.add(newAdapter);
	}

	/**
	 * Entfernt einen Component
	 * 
	 * @param component
	 */
	public void removeComponent(JTextComponent component) {
		Objects.nonNull(component);
		Iterator<SwingRechtschreibAdapter> iter = allAdapters.iterator();
		while (iter.hasNext()) {
			SwingRechtschreibAdapter ad = iter.next();
			if (ad.getTextComponent().equals(component)) {
				ad.deaktiviereRechtschreibpruefung();
				iter.remove();
			}
		}
	}

}
