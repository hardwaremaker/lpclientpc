package com.lp.neweditor.data.editor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import com.lp.client.pc.LPMain;
import com.lp.neweditor.common.Direction;
import com.lp.neweditor.common.InsertPosition;
import com.lp.neweditor.data.BlockPosition;
import com.lp.neweditor.data.SilenceableUndoableEditSupport;
import com.lp.neweditor.data.block.BlockFromDtoFactory;
import com.lp.neweditor.event.BlockEventListener;
import com.lp.neweditor.event.BlockEventSupport;
import com.lp.neweditor.ui.block.EditorBlock;
import com.lp.server.system.service.EditorBaseBlockDto;
import com.lp.server.system.service.EditorContentDto;
import com.lp.server.util.EditorContentIId;
import com.lp.util.Helper;

public class DefaultEditorModel implements EditorModel {

	/**
	 * ID des Datenobjekts am Server, ist ein invalid IId, wenn dieses Model nicht
	 * aus einem Dto erzeugt wurde
	 */
	private EditorContentIId idFromServer = new EditorContentIId(null);

	/**
	 * Liste von Zeilen von Bl&ouml;cken <br>
	 * Block = data.get(y).get(x)
	 */
	private List<List<EditorBlock<?>>> data;

	private Locale locale;

	private int prefWidth;

	private final PropertyChangeSupport changeSupport;
	private final SilenceableUndoableEditSupport editSupport;
	private final BlockEventSupport blockMoveSupport;

	/**
	 * Modification count um beim Iterator sicherstellen zu k&ouml;nnen, dass kein
	 * ConcurrentAccess auftritt
	 */
	private int modCount = 0;

	public DefaultEditorModel(Locale locale, int prefWidth) {
		this.locale = locale;
		this.prefWidth = prefWidth;
		data = new ArrayList<>();
		changeSupport = new PropertyChangeSupport(this);
		blockMoveSupport = new BlockEventSupport(this);
		editSupport = new SilenceableUndoableEditSupport(this);
	}

	@Override
	public Optional<EditorBlock<?>> getBlock(BlockPosition pos) {
		if (pos.y >= data.size()) {
			return Optional.empty();
		}
		List<EditorBlock<?>> line = data.get(pos.y);
		if (pos.x >= line.size()) {
			return Optional.empty();
		}
		return Optional.of(line.get(pos.x));
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	@Override
	public int getPreferredWidth() {
		return prefWidth;
	}

	@Override
	public BlockPosition addBlock(EditorBlock<?> block, BlockPosition pos, InsertPosition insert) {
		Objects.requireNonNull(block);
		Objects.requireNonNull(pos);
		Objects.requireNonNull(insert);
		boolean firstBlock = data.isEmpty();
		BlockPosition addedPosition = doAddBlock(block, pos, insert);
		if (!firstBlock) {
			AddBlockEdit edit = new AddBlockEdit(pos, insert, block, addedPosition);
			editSupport.postEdit(edit);
		}
		return addedPosition;
	}

	protected BlockPosition doAddBlock(EditorBlock<?> block, BlockPosition pos, InsertPosition insert) {
		switch (insert) {
		case BEFORE_HORIZONTAL:
			return addBlockToLine(block, pos);
		case AFTER_HORIZONTAL:
			return addBlockToLine(block, new BlockPosition(pos.x + 1, pos.y));
		case AFTER_VERTICAL:
			return addLine(block, pos.y + 1);
		case BEFORE_VERTICAL:
			return addLine(block, pos.y);
		default:
			return BlockPosition.INVALID;
		}
	}

	private BlockPosition addBlockToLine(EditorBlock<?> block, BlockPosition pos) {
		if(pos.y > data.size()) {
			throw new IllegalArgumentException("Cannot insert in Row " + pos.y);
		} else if(pos.y == data.size()) {
			//insert into new last line, that is allowed
			return addLine(block, pos.y);
		}
		List<EditorBlock<?>> line = data.get(pos.y);
		int xIns = Math.min(line.size(), pos.x);
		line.add(xIns, block);
		blockMoveSupport.fireBlockAdded(block, new BlockPosition(xIns, pos.y), Direction.HORIZONTAL);
		return new BlockPosition(xIns, pos.y);
	}

	private BlockPosition addLine(EditorBlock<?> block, int y) {
		int yIns = Math.min(y, data.size());
		List<EditorBlock<?>> newLine = new ArrayList<>(1);
		newLine.add(block);
		data.add(yIns, newLine);
		blockMoveSupport.fireBlockAdded(block, new BlockPosition(0, yIns), Direction.VERTICAL);
		return new BlockPosition(0, yIns);
	}

	@Override
	public boolean removeBlock(EditorBlock<?> block) {
		BlockPosition findBlock = findBlock(block);
		if (!findBlock.isValidPosition()) {
			return false;
		}
		removeBlock(findBlock);
		return true;
	}

	@Override
	public EditorBlock<?> removeBlock(BlockPosition pos) {
		Optional<EditorBlock<?>> block = getBlock(pos);
		block.orElseThrow(() -> new NoSuchElementException("Block an Position: " + pos + " existiert nicht"));
		Direction dir = doDelete(pos);
		DeleteBlockEdit edit = new DeleteBlockEdit(pos, block.get(), dir);
		editSupport.postEdit(edit);
		return block.get();
	}

	protected Direction doDelete(BlockPosition pos) {
		if (!pos.isValidPosition()) {
			throw new IllegalArgumentException("Ungueltige Blockposition");
		}
		List<EditorBlock<?>> line = data.get(pos.y);
		EditorBlock<?> block = line.remove(pos.x);

		if (line.isEmpty()) {
			// Zeile ist jetzt leer, ganze Zeile entfernen
			data.remove(pos.y);
			blockMoveSupport.fireBlockRemoved(block, pos, Direction.VERTICAL);
			return Direction.VERTICAL;
		} else {
			blockMoveSupport.fireBlockRemoved(block, pos, Direction.HORIZONTAL);
			return Direction.HORIZONTAL;
		}
	}

	@Override
	public BlockPosition findBlock(EditorBlock<?> block) {
		for (int y = 0; y < data.size(); y++) {
			List<EditorBlock<?>> line = data.get(y);
			for (int x = 0; x < line.size(); x++) {
				if (line.get(x).equals(block)) {
					return new BlockPosition(x, y);
				}
			}
		}
		return BlockPosition.INVALID;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	@Override
	public void addBlockMovedListener(BlockEventListener listener) {
		blockMoveSupport.addListener(listener);
	}

	@Override
	public void removeBlockMovedListener(BlockEventListener listener) {
		blockMoveSupport.removeListener(listener);
	}

	@Override
	public void setLocale(Locale locale) {
		Locale oldLocale = this.locale;
		this.locale = locale;
		changeSupport.firePropertyChange(EditorModel.propertyLocale, oldLocale, locale);
	}

	@Override
	public void addUndoableEditListener(UndoableEditListener listener) {
		editSupport.addUndoableEditListener(listener);
	}

	@Override
	public void removeUndoableEditListener(UndoableEditListener listener) {
		editSupport.removeUndoableEditListener(listener);
	}

	@Override
	public void beginCompoundUpdate() {
		editSupport.beginUpdate();
	}

	@Override
	public void endCompoundUpdate() {
		editSupport.endUpdate();
	}

	private class AddBlockEdit extends AbstractUndoableEdit {
		private BlockPosition addedPosition;
		private final BlockPosition position;
		private final InsertPosition insert;
		private final EditorBlock<?> block;

		public AddBlockEdit(BlockPosition position, InsertPosition insert, EditorBlock<?> block,
				BlockPosition addedPosition) {
			this.position = position;
			this.addedPosition = addedPosition;
			this.insert = insert;
			this.block = block;
		}

		@Override
		public void undo() throws CannotUndoException {
			super.undo();
			doDelete(addedPosition);
		}

		@Override
		public void redo() throws CannotRedoException {
			super.redo();
			addedPosition = doAddBlock(block, position, insert);
		}

	}

	private class DeleteBlockEdit extends AbstractUndoableEdit {
		private BlockPosition addedPosition;
		private BlockPosition deletedPosition;
		private final EditorBlock<?> block;
		private Direction deleteDirection;

		public DeleteBlockEdit(BlockPosition deletedPosition, EditorBlock<?> block, Direction deleteDirection) {
			this.deleteDirection = deleteDirection;
			this.deletedPosition = deletedPosition;
			this.block = block;
		}

		@Override
		public void undo() throws CannotUndoException {
			super.undo();
			InsertPosition insert = deleteDirection == Direction.HORIZONTAL ? InsertPosition.BEFORE_HORIZONTAL
					: InsertPosition.BEFORE_VERTICAL;
			addedPosition = doAddBlock(block, deletedPosition, insert);
		}

		@Override
		public void redo() throws CannotRedoException {
			super.redo();
			deleteDirection = doDelete(addedPosition);
			deletedPosition = addedPosition;
		}
	}

	@Override
	public Iterator<BlockWithPosition> iterator() {
		return new Itr();
	}

	private class Itr implements Iterator<BlockWithPosition> {

		private int x, y;
		private int expectedModCount = modCount;

		public Itr() {
			x = 0;
			y = 0;
		}

		private boolean isBlockValid() {
			if(data.size() <= y)
				return false;
			return data.get(y).size() > x;
		}

		private boolean hasMoreRows() {
			return data.size() > (y + 1);
		}

		@Override
		public boolean hasNext() {
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			return hasMoreRows() || isBlockValid();
		}

		@Override
		public BlockWithPosition next() {
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			if (!isBlockValid()) {
				if (hasMoreRows()) {
					x = 0;
					y++;
				} else {
					throw new NoSuchElementException(String.format("No Element at (%d, %d)", x, y));
				}
			}
			List<EditorBlock<?>> line = data.get(y);
			EditorBlock<?> obj = line.get(x);
			BlockPosition pos = new BlockPosition(x, y);
			x++;
			return new BlockWithPosition(obj, pos);
		}
	}

	@Override
	public EditorContentDto toDto() {
		EditorContentDto contentDto = new EditorContentDto();
		contentDto.setId(idFromServer);
		contentDto.setLocale(Helper.locale2String(locale));
		for (BlockWithPosition block : this) {
			EditorBaseBlockDto blockDto = block.getBlock().getModel().toDto();
			blockDto.setColumn(block.getPosition().x);
			blockDto.setRow(block.position.y);
			contentDto.addBlockDto(blockDto);
		}
		return contentDto;
	}

	@Override
	public void fromDto(EditorContentDto dto, BlockFromDtoFactory factory) {
		beginCompoundUpdate();
		removeAllBlocks();
		if(dto.getLocale() == null) {
			setLocale(LPMain.getInstance().getUISprLocale());
		} else {
			setLocale(Helper.string2Locale(dto.getLocale()));
		}
		idFromServer = dto.getId();
		// Bloecke muessen nachher in richtiger Reihenfolge eingefuegt werden
		SortedMap<BlockPosition, EditorBlock<?>> newBlocks = new TreeMap<>();
		for (EditorBaseBlockDto blockDto : dto.getBlockDtos()) {
			EditorBlock<?> newBlock = factory.createBlockForDto(blockDto);
			BlockPosition pos = new BlockPosition(blockDto.getColumn(), blockDto.getRow());
			newBlocks.put(pos, newBlock);
		}
		for (Map.Entry<BlockPosition, EditorBlock<?>> entry : newBlocks.entrySet()) {
			// TODO insert Mechanismus ueberarbeiten!
			addBlock(entry.getValue(), entry.getKey(), InsertPosition.AFTER_HORIZONTAL);
		}
		endCompoundUpdate();
	}

	@Override
	public void removeAllBlocks() {
		beginCompoundUpdate();
		// Rueckwaerts loeschen, damit aendern sich Positionen nicht
		for (int y = data.size() - 1; y >= 0; y--) {
			List<EditorBlock<?>> line = data.get(y);
			for (int x = line.size() - 1; x >= 0; x--) {
				removeBlock(new BlockPosition(x, y));
			}
		}
		endCompoundUpdate();
	}
	
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public void endIgnoreUpdate() {
		editSupport.setIgnoreUpdates(false);
	}

	@Override
	public void beginIgnoreUpdate() {
		editSupport.setIgnoreUpdates(true);
		
	}

}
