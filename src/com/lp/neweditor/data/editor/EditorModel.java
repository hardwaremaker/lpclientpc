package com.lp.neweditor.data.editor;

import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.Optional;

import javax.swing.event.UndoableEditListener;

import com.lp.neweditor.common.InsertPosition;
import com.lp.neweditor.data.BlockPosition;
import com.lp.neweditor.data.block.BlockFromDtoFactory;
import com.lp.neweditor.event.BlockEventProvider;
import com.lp.neweditor.ui.block.EditorBlock;
import com.lp.server.system.service.EditorContentDto;

public interface EditorModel extends BlockEventProvider, Iterable<BlockWithPosition> {
	public static String propertyLocale = "locale";

	Optional<EditorBlock<?>> getBlock(BlockPosition pos);

	/**
	 * F&uuml;ge Block hinzu. Gibt die tats&auml;chliche Position des Blocks
	 * zur&uuml;ck. <br>
	 * Der Parameter insert bestimmt, wie der Block eingef&uuml;gt wird. Konstanten
	 * mit BEFORE bedeuten, dass der Block auf pos vorher nach hinten verschoben
	 * wird und der neue Block auf dieser Position eigef&uuml;gt wird. Konstanten
	 * mit AFTER f&uuml;gen den Block nach dem exisitierenden ein, falls ein Block
	 * vorhanden ist. HORIZONTAL f&uuml;gt einen weiteren Block in diese Zeile ein
	 * und verschiebt keine Zeilen, VERTICAL f&uuml;gt immer eine neue Zeile ein
	 * 
	 * @param block  Der Block
	 * @param pos    Zielposition
	 * @param insert Wo eingef&uuml;gt werden soll.
	 * @return
	 */
	BlockPosition addBlock(EditorBlock<?> block, BlockPosition pos, InsertPosition insert);

	boolean removeBlock(EditorBlock<?> block);

	EditorBlock<?> removeBlock(BlockPosition pos);
	
	void removeAllBlocks();
	
	boolean isEmpty();

	BlockPosition findBlock(EditorBlock<?> block);

	Locale getLocale();

	void setLocale(Locale locale);

	int getPreferredWidth();

	void addPropertyChangeListener(PropertyChangeListener listener);

	void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

	void removePropertyChangeListener(PropertyChangeListener listener);

	void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

	void addUndoableEditListener(UndoableEditListener listener);

	void removeUndoableEditListener(UndoableEditListener listener);

	void beginCompoundUpdate();

	void endCompoundUpdate();

	EditorContentDto toDto();

	/**
	 * Lade Daten des Models aus einem Dto, mithilfe einer Factory, die pro BlockDto die richtigen Block Objekte erzeugt. <br>
	 * Diese Methode kann auch auf nicht leere Models aufgerufen werden
	 * @param dto
	 * @param factory
	 */
	void fromDto(EditorContentDto dto, BlockFromDtoFactory factory);

	void endIgnoreUpdate();

	void beginIgnoreUpdate();
}
