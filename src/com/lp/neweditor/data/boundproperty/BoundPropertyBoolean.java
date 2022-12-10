package com.lp.neweditor.data.boundproperty;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

/**
 * Klasse um Swing BoundProperties einfacher zu machen. <br>
 * Diese Klasse bietet eine get methode, die das Datenobjekt zur&uuml;ck gibt
 * und eine set methode, die das Datenobjekt setzt und ein PropertyChangeEvent
 * verursacht. <br>
 * F&uuml;r Java primitives gibt es spezielle overloads
 * 
 * @author Alexander Daum
 *
 */
public class BoundPropertyBoolean {
	private final PropertyChangeSupport propChange;
	private final String propertyName;
	private boolean value;

	/**
	 * Erzeuge ein neues BoundProperty
	 * 
	 * @param propertyName          name, der dem propertyChangeEvent mitgegeben
	 *                              wird
	 * @param defaultValue          Wert zu beginn
	 * @param propertyChangeSupport der {@link PropertyChangeSupport}, der verwendet
	 *                              wird um {@link PropertyChangeEvent}s
	 *                              auszul&ouml;sen
	 */
	public BoundPropertyBoolean(String propertyName, boolean defaultValue,
			PropertyChangeSupport propertyChangeSupport) {
		value = defaultValue;
		this.propChange = propertyChangeSupport;
		this.propertyName = propertyName;
	}

	/**
	 * Hole den Wert
	 * 
	 * @return
	 */
	public boolean getValue() {
		return value;
	}

	/**
	 * Setze den Wert und l&ouml;se PropertyChangeEvent aus
	 * 
	 * @param newValue
	 */
	public void setValue(boolean newValue) {
		boolean oldValue = value;
		value = newValue;
		propChange.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * Setze den Wert ohne ein {@link PropertyChangeEvent} auszul&ouml;sen. <br>
	 * 
	 * @param newValue
	 */
	public void setValueWithoutEvent(boolean newValue) {
		value = newValue;
	}
}
