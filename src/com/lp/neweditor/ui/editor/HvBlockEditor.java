package com.lp.neweditor.ui.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;
import java.util.Optional;

import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.rechtschreibung.IRechtschreibPruefbar;
import com.lp.neweditor.action.BlockEditorActions;
import com.lp.neweditor.common.EditorDefaults;
import com.lp.neweditor.common.EditorZoomFaktor;
import com.lp.neweditor.common.InsertPosition;
import com.lp.neweditor.common.SizeUnit;
import com.lp.neweditor.data.BlockPosition;
import com.lp.neweditor.data.block.BlockFromDtoFactory;
import com.lp.neweditor.data.boundproperty.BoundPropertyBoolean;
import com.lp.neweditor.data.editor.BlockWithPosition;
import com.lp.neweditor.data.editor.DefaultEditorModel;
import com.lp.neweditor.data.editor.EditorModel;
import com.lp.neweditor.event.BlockEvent;
import com.lp.neweditor.event.BlockEventListener;
import com.lp.neweditor.event.BlockSelectedEvent;
import com.lp.neweditor.event.BlockSelectedListener;
import com.lp.neweditor.event.PropertyChangeSupplier;
import com.lp.neweditor.ui.block.BlockView;
import com.lp.neweditor.ui.block.EditorBlock;
import com.lp.server.system.service.EditorContentDto;

/**
 * Hauptklasse des neuen Editors TODO Beschreibung
 * 
 * @author Alexander Daum
 *
 */
public class HvBlockEditor implements PropertyChangeSupplier, IRechtschreibPruefbar {
	public static final String propertyZoomFaktor = "zoomFaktor";
	public static final String propertyEditable = "editable";
	public static final int DISPLAY_DPI = 72;
	/**
	 * Default Breite des Editors
	 */
	private static final int DEFAULT_PX_WIDTH = 600;
	private final UndoableEditSupport editSupport;
	private final PropertyChangeSupport propChangeSupport;
	private EditorModel model;
	private EditorView view;

	private final EventSupport eventSupport;

	private InternalFrame iFrame;

	private final UndoManager undoManager;

	private EditorZoomFaktor zoomFaktor = new EditorZoomFaktor(100);

	private final BlockEditorActions actions;

	private final BoundPropertyBoolean editable;

	public HvBlockEditor() {
		this.editSupport = new UndoableEditSupport(this);
		this.propChangeSupport = new PropertyChangeSupport(this);
		eventSupport = new EventSupport();
		undoManager = new UndoManager();
		actions = new BlockEditorActions(this);
		editable = new BoundPropertyBoolean(propertyEditable, true, propChangeSupport);

		zoomFaktor = EditorDefaults.getDefaultZoomFaktor();
		view = new EditorView();
		model = createDefaultModel();
		model.addBlockMovedListener(eventSupport);
		model.addUndoableEditListener(eventSupport);
	}

	/**
	 * Wird aufgerufen, wenn dieser Editor zu einem Helium InternalFrame
	 * hinzugef&uuml;gt wird.
	 * 
	 * @param internalFrame
	 */
	public void setInternalFrame(InternalFrame internalFrame) {
		this.iFrame = internalFrame;
	}

	/**
	 * Gibt den InternalFrame, der den Editor enth&auml;lt zur&uuml;ck. Wenn der
	 * Editor nie mit {@link HvBlockEditor#setInternalFrame(InternalFrame)} zu einem
	 * InternalFrame hinzugef&uuml;gt wurde, wird {@link Optional#empty()}
	 * zur&uuml;ck gegeben
	 * 
	 * @return
	 */
	public Optional<InternalFrame> getInternalFrame() {
		return Optional.ofNullable(iFrame);
	}

	public EditorModel getModel() {
		return model;
	}

	public EditorModel createDefaultModel() {
		DefaultEditorModel model = new DefaultEditorModel(Locale.getDefault(), DEFAULT_PX_WIDTH);
		return model;
	}

	public EditorView getView() {
		return view;
	}

	public void addBlock(EditorBlock<?> block, BlockPosition pos, InsertPosition insert) {
		model.addBlock(block, pos, insert);
	}

	public synchronized void updateView() {
		view.removeAllBlocks();
		int lastY = -1;
		int numBlocks = 0;
		for (BlockWithPosition block : model) {
			if (block.position.y > lastY) {
				view.addLine();
				lastY = block.position.y;
			}
			view.addBlock(block.block.getView());
			numBlocks++;
		}
		if (numBlocks <= 1) {
			actions.getActionDeleteBlock().setEnabled(false);
		} else {
			actions.getActionDeleteBlock().setEnabled(true);
		}
		SwingUtilities.invokeLater(() -> {
			view.getUIComponent().revalidate();
			view.getUIComponent().repaint();
		});
	}

	public UndoManager getUndoManager() {
		return undoManager;
	}

	public void addUndoableEditListener(UndoableEditListener listener) {
		editSupport.addUndoableEditListener(listener);
	}

	public void removeUndoableEditListener(UndoableEditListener listener) {
		editSupport.removeUndoableEditListener(listener);
	}

	public EditorZoomFaktor getZoom() {
		return zoomFaktor;
	}

	private class EventSupport implements UndoableEditListener, BlockEventListener, BlockSelectedListener {
		private EditorBlock<?> previouslySelected = null;

		protected final PropertyChangeListener zoomListener;

		public EventSupport() {
			zoomListener = this::onZoomChange;
		}

		@Override
		public void onBlockEvent(BlockEvent event) {
			EditorBlock<?> block = event.getBlock();
			switch (event.getType()) {
			case INSERT:
				block.getModel().addUndoableEditListener(eventSupport);
				block.addSelectedListener(eventSupport);
				block.getView().getMenuForBlock().addPropertyChangeListener(propertyZoomFaktor,
						eventSupport.zoomListener);
				block.getView().getUIComponent().requestFocusInWindow();
				break;
			case REMOVE:
				block.removeSelectedListener(eventSupport);
				block.getView().getMenuForBlock().removePropertyChangeListener(propertyZoomFaktor,
						eventSupport.zoomListener);
				block.getModel().removeUndoableEditListener(eventSupport);
				break;
			default:
				break;
			}
			updateView();
		}

		@Override
		public void undoableEditHappened(UndoableEditEvent e) {
			undoManager.addEdit(e.getEdit());
			editSupport.postEdit(e.getEdit());
		}

		@Override
		public void blockSelected(BlockSelectedEvent e) {
			if (!e.getSelectedBlock().equals(previouslySelected)) {
				getView().setMenu(e.getSelectedBlock().getView().getMenuForBlock());
				getView().setStatusBar(e.getSelectedBlock().getView().getStatusBar());
				previouslySelected = e.getSelectedBlock();
			}
		}

		private void onZoomChange(PropertyChangeEvent e) {
			EditorZoomFaktor newZoom = (EditorZoomFaktor) e.getNewValue();
			if (newZoom.equals(zoomFaktor)) {
				return;
			}
			EditorZoomFaktor oldZoom = zoomFaktor;
			zoomFaktor = newZoom;
			propChangeSupport.firePropertyChange(propertyZoomFaktor, oldZoom, newZoom);
			view.getContent().setZoomFaktor(newZoom);
			for (BlockView bv : view.getContent()) {
				bv.getMenuForBlock().setZoom(newZoom);
			}
		}

	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propChangeSupport.addPropertyChangeListener(listener);
	}

	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propChangeSupport.removePropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propChangeSupport.removePropertyChangeListener(propertyName, listener);
	}

	/**
	 * Setzt breite des Editors
	 * 
	 * @param width
	 */
	public void setPageWidth(int width, SizeUnit unit) {
		view.setWidth(width, unit);
	}

	public void setMenuVisible(boolean b) {
		view.setMenuVisible(b);
	}

	public BlockEditorActions getActions() {
		return actions;
	}

	@Override
	public void aktiviereRechtschreibpruefung() {
		for (BlockWithPosition block : model) {
			if (block.block instanceof IRechtschreibPruefbar) {
				((IRechtschreibPruefbar) block.block).aktiviereRechtschreibpruefung();
			}
		}
	}

	@Override
	public void deaktiviereRechtschreibpruefung() {
		for (BlockWithPosition block : model) {
			if (block.block instanceof IRechtschreibPruefbar) {
				((IRechtschreibPruefbar) block.block).deaktiviereRechtschreibpruefung();
			}
		}
	}

	@Override
	public void setRechtschreibpruefungLocale(Locale loc) {
		model.setLocale(loc);
		for (BlockWithPosition block : model) {
			if (block.block instanceof IRechtschreibPruefbar) {
				((IRechtschreibPruefbar) block.block).setRechtschreibpruefungLocale(loc);
			}
		}
	}

	public EditorBlock<?> getSelectedBlock() {
		return eventSupport.previouslySelected;
	}

	public BlockPosition getSelectedBlockPosition() {
		return getPositionOfBlock(getSelectedBlock());
	}

	public BlockPosition getPositionOfBlock(EditorBlock<?> block) {
		return model.findBlock(block);
	}

	public void deleteBlock(BlockPosition pos) {
		getModel().removeBlock(pos);
	}

	public Optional<EditorBlock<?>> getBlockAt(BlockPosition pos) {
		return model.getBlock(pos);
	}

	public void fromDto(EditorContentDto dto, boolean undoable) {
		if(!undoable) {
			model.beginIgnoreUpdate();
		}
		model.fromDto(dto, new BlockFromDtoFactory(this));
		if(!undoable) {
			model.endIgnoreUpdate();
		}
	}

	public EditorContentDto toDto() {
		return model.toDto();
	}

	public void setEditable(boolean b) {
		editable.setValue(b);
	}
	
	public boolean isEditable() {
		return editable.getValue();
	}
}