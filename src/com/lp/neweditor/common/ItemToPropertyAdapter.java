package com.lp.neweditor.common;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeSupport;

/**
 * Helferklasse um Komponenten, die ItemEvents feuern (z.B. ComboBox) zu
 * PropertyChangeEvents umzubauen. <br>
 * Diese Klasse h&ouml;rt auf die ItemEvents und feuert bei select event ein
 * neues PropertyChange Event mit dem PropertyName und PropertyChangeSupport,
 * die im Konstruktor gesetzt wurden.
 * 
 * @author Alexander Daum
 *
 */
public class ItemToPropertyAdapter implements ItemListener {
	private final String propertyName;
	private Object old = null;

	private final PropertyChangeSupport changeSupport;

	/**
	 * Erzeuge einen neuen {@link ItemToPropertyAdapter}.
	 * 
	 * @param propertyName
	 * @param changeSupport
	 */
	public ItemToPropertyAdapter(String propertyName, PropertyChangeSupport changeSupport) {
		this.propertyName = propertyName;
		this.changeSupport = changeSupport;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			changeSupport.firePropertyChange(propertyName, old, e.getItem());
		}
		old = e.getItem();
	}

}
