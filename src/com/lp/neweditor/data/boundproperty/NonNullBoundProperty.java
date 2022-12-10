package com.lp.neweditor.data.boundproperty;

import java.beans.PropertyChangeSupport;
import java.util.Objects;

/**
 * BoundProperty, das nicht null sein darf. Wird jemals null als Wert gesetzt,
 * wird eine {@link NullPointerException} geworfen
 * 
 * @author Alexander Daum
 *
 * @param <T>
 */
public class NonNullBoundProperty<T> extends BoundProperty<T> {

	public NonNullBoundProperty(String propertyName, T defaultValue, PropertyChangeSupport propertyChangeSupport) {
		super(propertyName, Objects.requireNonNull(defaultValue), propertyChangeSupport);
	}

	@Override
	public void setValueWithoutEvent(T newValue) {
		super.setValueWithoutEvent(Objects.requireNonNull(newValue));
	}

}
