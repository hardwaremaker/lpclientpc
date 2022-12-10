package com.lp.neweditor.data.block;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEditSupport;

import com.lp.neweditor.data.SilenceableUndoableEditSupport;
import com.lp.server.system.service.EditorBaseBlockDto;
import com.lp.server.util.EditorBlockIId;

/**
 * Helferklasse, die Funktionen, die alle BlockModels brauchen, implementiert
 * 
 * @author Alexander Daum
 *
 */
public abstract class BaseBlockModel {
	/**
	 * Id des Blocks ins Datenbank
	 */
	private EditorBlockIId idInDatabase = new EditorBlockIId(null);
	
	protected final PropertyChangeSupport propChangeSupport = new PropertyChangeSupport(this);
	private final SilenceableUndoableEditSupport undoSupport = new SilenceableUndoableEditSupport(this);

	public BaseBlockModel() {
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propChangeSupport.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propChangeSupport.removePropertyChangeListener(propertyName, listener);
	}

	public void addUndoableEditListener(UndoableEditListener listener) {
		undoSupport.addUndoableEditListener(listener);
	}

	public void removeUndoableEditListener(UndoableEditListener listener) {
		undoSupport.removeUndoableEditListener(listener);
	}

	protected UndoableEditSupport getUndoSupport() {
		return undoSupport;
	}

	public void beginCompoundUpdate() {
		undoSupport.beginUpdate();
	}

	public void endCompoundUpdate() {
		undoSupport.endUpdate();
	}

	public void beginIgnoreUpdate() {
		undoSupport.setIgnoreUpdates(true);
	}

	public void endIgnoreUpdate() {
		undoSupport.setIgnoreUpdates(false);
	}

	protected abstract EditorBaseBlockDto createEmptyDto();

	/**
	 * Bef&uuml;lle das Dto mit Daten, es sollte hier auch immer die super Methode
	 * aufgerufen werden, damit k&ouml;nnen zus&auml;tzliche Datenfelder
	 * hinzugef&uuml;gt werden ohne alle Subklassen anzupassen. <br>
	 * Das Dto wird von createEmptyDto erzeugt.
	 * 
	 * @param dto ein Dto Objekt, das bef&uuml;llt werden muss
	 */
	protected void populateDto(EditorBaseBlockDto dto) {
		dto.setId(idInDatabase);
	}

	/**
	 * Lade Daten des Blocks aus dem Dto. <br>
	 * Es sollte hier immer super.fromDto aufgerufen werden
	 * 
	 * @param dto
	 */
	public void fromDto(EditorBaseBlockDto dto) {
		idInDatabase = dto.getId();
	}
	
	public EditorBaseBlockDto toDto() {
		EditorBaseBlockDto dto = createEmptyDto();
		populateDto(dto);
		return dto;
	}

}
