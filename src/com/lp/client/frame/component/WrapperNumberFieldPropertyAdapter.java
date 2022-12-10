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
package com.lp.client.frame.component;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.lp.client.frame.ExceptionLP;

/**
 * Eine Klasse um ein {@link WrapperNumberField} zu ergaenzen und den BigDecimal
 * als bound property verfuegbar zu machen. Name des Bound Property = menge
 * 
 * @author Alexander Daum
 *
 */
public class WrapperNumberFieldPropertyAdapter {
	private final WrapperNumberField wnf;
	private BigDecimal nMenge;

	private final PropertyChangeSupport eventSupport;

	public WrapperNumberFieldPropertyAdapter(WrapperNumberField wnf) {
		eventSupport = new PropertyChangeSupport(this);
		this.wnf = wnf;
		try {
			getWnfMenge();
		} catch (ExceptionLP e) {
			nMenge = BigDecimal.ZERO;
		}
		this.wnf.getDocument().addDocumentListener(new DocListener());

	}

	/**
	 * Erstelle neuen {@link WrapperNumberFieldPropertyAdapter} und setze menge des
	 * {@link WrapperNumberField}
	 * 
	 * @param wnf
	 * @param nMenge
	 */
	public WrapperNumberFieldPropertyAdapter(WrapperNumberField wnf, BigDecimal nMenge) {
		eventSupport = new PropertyChangeSupport(this);
		this.wnf = wnf;
		this.nMenge = nMenge;
		try {
			setWnfMenge();
		} catch (ExceptionLP e) {
			// Ignore
		}
		this.wnf.getDocument().addDocumentListener(new DocListener());
	}

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public void setNMenge(BigDecimal nMenge) throws ExceptionLP {
		this.nMenge = nMenge;
		setWnfMenge();
	}

	public WrapperNumberField getWrapperNumberField() {
		return wnf;
	}

	/*
	 * WrapperNumberField getter/setter. Zur sicherheit synchronized auf wnf,
	 * eventuell zugriff aus UI-Thread (DocumentListener) und app-thread
	 */
	private void getWnfMenge() throws ExceptionLP {
		synchronized (wnf) {
			nMenge = wnf.getBigDecimal();
		}
	}

	private void setWnfMenge() throws ExceptionLP {
		synchronized (wnf) {
			wnf.setBigDecimal(nMenge);
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		eventSupport.addPropertyChangeListener(l);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener l) {
		eventSupport.addPropertyChangeListener(propertyName, l);
	}

	private class DocListener implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			docUpdate(e);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			docUpdate(e);
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			docUpdate(e);
		}

		private void docUpdate(DocumentEvent e) {
			BigDecimal oldVal, newVal;
			synchronized (wnf) {
				oldVal = nMenge;
				try {
					getWnfMenge();
				} catch (ExceptionLP e1) {
					nMenge = BigDecimal.ZERO;
					try {
						setWnfMenge();
					} catch (ExceptionLP e2) {
					}
				}
				newVal = nMenge;
			}
			eventSupport.firePropertyChange("menge", oldVal, newVal);
		}

	}
}
