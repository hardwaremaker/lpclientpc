package com.lp.neweditor.data.boundproperty;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;

import com.lp.client.pc.LPMain;

/**
 * Klasse um Swing BoundProperties einfacher zu machen. <br>
 * Diese Klasse bietet eine get methode, die das Datenobjekt zur&uuml;ck gibt
 * und eine set methode, die das Datenobjekt setzt und ein PropertyChangeEvent
 * verursacht. <br>
 * F&uuml;r Java primitives gibt es spezielle overloads
 * 
 * @author Alexander Daum
 *
 * @param <T> Typ des Objekts
 */
public class BoundProperty<T> {
	private final PropertyChangeSupport propChange;
	private final String propertyName;
	private T value;

	private UndoableEditSupport editSupport = null;

	/**
	 * Erzeuge ein neues BoundProperty
	 * 
	 * @param propertyName          name, der dem propertyChangeEvent mitgegeben
	 *                              wird
	 * @param defaultValue          Wert zu beginn, darf <code>null</code> sein
	 * @param propertyChangeSupport der {@link PropertyChangeSupport}, der verwendet
	 *                              wird um {@link PropertyChangeEvent}s
	 *                              auszul&ouml;sen
	 */
	public BoundProperty(String propertyName, T defaultValue, PropertyChangeSupport propertyChangeSupport) {
		value = defaultValue;
		this.propChange = propertyChangeSupport;
		this.propertyName = propertyName;
	}

	/**
	 * F&uuml;gt unterst&uuml;tzung f&uuml;r {@link UndoableEdit} hinzu. Bei jedem
	 * aufruf von set wird dadurch eine Undoable Edit zum editSupport
	 * hinzugef&uuml;gt. <br>
	 * 
	 * @param editSupport
	 * @param chainingKey
	 */
	public void addUndoableEditSupport(UndoableEditSupport editSupport) {
		if (this.editSupport != null) {
			throw new IllegalStateException("Can only add UndoableEditSupport once");
		}
		this.editSupport = editSupport;
	}

	/**
	 * Hole den Wert
	 * 
	 * @return
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Setze den Wert und l&ouml;se PropertyChangeEvent aus
	 * 
	 * @param newValue
	 * 
	 * @implNote Ruft zum setzen {@link BoundProperty#setValueWithoutEvent(Object)}
	 *           auf
	 */
	public void setValue(T newValue) {
		T oldValue = value;
		setValueWithoutEvent(newValue);
		propChange.firePropertyChange(propertyName, oldValue, newValue);
		if (editSupport != null) {
			PropertyEdit edit = new PropertyEdit(oldValue, newValue);
			editSupport.postEdit(edit);
		}
	}

	public void setValueWithoutEdit(T newValue) {
		T oldValue = value;
		setValueWithoutEvent(newValue);
		propChange.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * Setze den Wert ohne ein {@link PropertyChangeEvent} auszul&ouml;sen. <br>
	 * 
	 * @param newValue
	 */
	public void setValueWithoutEvent(T newValue) {
		value = newValue;
	}

	private class PropertyEdit extends AbstractUndoableEdit {
		private static final long serialVersionUID = 1L;
		private final T oldVal, newVal;

		public PropertyEdit(T oldValue, T newValue) {
			this.oldVal = oldValue;
			this.newVal = newValue;
		}

		@Override
		public void undo() throws CannotUndoException {
			super.undo();
			setValueWithoutEdit(oldVal);
		}

		@Override
		public void redo() throws CannotRedoException {
			super.redo();
			setValueWithoutEdit(newVal);
		}

		@Override
		public String getPresentationName() {
			return LPMain.getMessageTextRespectUISPr("neweditor.boundproperty.message", propertyName);
		}

	}
}
