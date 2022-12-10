package com.lp.neweditor.ui.block;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.lp.neweditor.common.EditorZoomFaktor;
import com.lp.neweditor.data.block.BaseBlockModel;
import com.lp.neweditor.event.BlockSelectedListener;
import com.lp.neweditor.event.BlockSelectedSupport;
import com.lp.neweditor.event.ModelChangedListener;
import com.lp.neweditor.event.ModelChangedSupport;
import com.lp.neweditor.ui.editor.HvBlockEditor;
import com.lp.server.system.service.EditorBaseBlockDto;
import com.lp.server.util.logger.LpLogger;

public abstract class BasicBlockController<ModelType extends BaseBlockModel, ViewType extends BlockView>
		implements EditorBlock<ModelType> {
	private BlockSelectedSupport selectSupport;
	private ModelChangedSupport<ModelType> modelChangeSupport;

	private ModelType model;
	private ViewType view;

	private EditorZoomFaktor zoomFaktor;

	private boolean editable;
	
	protected final HvBlockEditor editor;
	protected final BlockEventSupport eventSupport;

	protected final Logger logger = LpLogger.getLogger(getClass().getName());

	/**
	 * Erzeuge ein neues View
	 * 
	 * @return
	 */
	protected abstract ViewType createView();

	public BasicBlockController(HvBlockEditor editor) {
		this.editor = editor;
		zoomFaktor = editor.getZoom();
		selectSupport = new BlockSelectedSupport(this);
		modelChangeSupport = new ModelChangedSupport<>(this);
		eventSupport = Objects.requireNonNull(createEventSupport());
		view = Objects.requireNonNull(createView());
		view.addFocusListener(new BlockFocusAdapter());
		setEditable(editor.isEditable());
		editor.addPropertyChangeListener(HvBlockEditor.propertyEditable,
				evt -> setEditable((Boolean) evt.getNewValue()));
		editor.addPropertyChangeListener(HvBlockEditor.propertyZoomFaktor, this::onZoomChange);
		setModel(Objects.requireNonNull(createDefaultModel()));
	}

	@Override
	public void addSelectedListener(BlockSelectedListener listener) {
		selectSupport.addListener(listener);
	}

	@Override
	public void removeSelectedListener(BlockSelectedListener listener) {
		selectSupport.removeListener(listener);
	}

	@Override
	public ViewType getView() {
		return view;
	}

	protected class BlockFocusAdapter extends FocusAdapter {
		@Override
		public void focusGained(FocusEvent e) {
			selectSupport.fireBlockSelected(BasicBlockController.this);
		}
	}

	@Override
	public HvBlockEditor getEditor() {
		return editor;
	}

	private void onZoomChange(PropertyChangeEvent e) {
		this.zoomFaktor = (EditorZoomFaktor) e.getNewValue();
		setZoom(zoomFaktor);
	}

	protected EditorZoomFaktor getZoom() {
		return zoomFaktor;
	}

	protected abstract void setZoom(EditorZoomFaktor zoom);

	@Override
	public void addModelChangeListener(ModelChangedListener<ModelType> listener) {
		modelChangeSupport.addListener(listener);
	}

	@Override
	public void removeModelChangeListener(ModelChangedListener<ModelType> listener) {
		modelChangeSupport.removeListener(listener);
	}

	@Override
	public ModelType getModel() {
		return model;
	}

	public void setModel(ModelType model) {
		ModelType oldModel = this.model;
		if (oldModel != null) {
			detachFromModel(oldModel);
		}
		this.model = model;
		attachToModel(model);
		modelChangeSupport.fireModelChange(oldModel, model);
	};

	protected void attachToModel(ModelType model) {
		model.addPropertyChangeListener(eventSupport);
	}

	protected void detachFromModel(ModelType model) {
		model.removePropertyChangeListener(eventSupport);
	}

	protected abstract BlockEventSupport createEventSupport();

	/**
	 * Basisklassen f&uuml;r Block EventSupport. Jeder Block sollte eine eigene
	 * EventSupport Klasse besitzen, die ebenfalls protected ist (damit k&ouml;nnen
	 * Klassen, die diese extenden auch darauf zugreifen) <br>
	 * Grund f&uuml;r diese Klasse zum behandeln von Events ist, dass der Controller
	 * selbst kein Listener sein soll, weil dieser von au&szlig;en sichtbar ist, das
	 * EventSupport Objekt normalerweise aber nicht (protected). <br>
	 * Normalerweise k&ouml;nnten Java8 Lambdas / Method references benutzt werden,
	 * und jede Subklasse besitzt mehrere Membervariablen als Handler f&uuml;r
	 * einzelne Events. Allerdings wird bei BlockControllern bereits im
	 * Superkonstruktor attackToModel aufgerufen, was Zugriff auf die Listener
	 * braucht, die aber erst im Unterklassenkonstruktor initialisiert werden
	 * k&ouml;nnen
	 * 
	 * @author Alexander Daum
	 *
	 */
	protected abstract class BlockEventSupport implements PropertyChangeListener {
		/**
		 * Versuche alle Properties zu behandeln die dieser Klasse bekannt sind. Wenn
		 * Event verarbeitet werden konnte, wird true zur&uuml;ck gegeben, sonst
		 * false<br>
		 * Bei Vererbung sollte die Subklasse zuerst super.tryHandlePropertyChange
		 * aufrufen und nur wenn diese Methode false zur&uuml;ck gibt auf eigene
		 * properties pr&uuml;fen. Wenn bereits vorhandene Properties anders behandelt
		 * werden sollen, sollte die Superklasse daf&uuml;r entsprechende Methoden zur
		 * verf&uuml;gung stellen
		 * 
		 * @param evt
		 * @return
		 */
		protected abstract boolean tryHandlePropertyChange(PropertyChangeEvent evt);

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			boolean handled = tryHandlePropertyChange(evt);
			if (!handled) {
				logger.warn(String.format(
						"PropertyChangeEvent fuer '%s' wurde von EventHandler Klasse '%s' nicht behandelt",
						evt.getPropertyName(), getClass().getName()));
			}
		}
	}

	@Override
	public void setEditable(boolean editable) {
		view.setEditable(editable);
		this.editable = editable;
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

}
