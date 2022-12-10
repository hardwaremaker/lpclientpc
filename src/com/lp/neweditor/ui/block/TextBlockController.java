package com.lp.neweditor.ui.block;

import java.beans.PropertyChangeEvent;
import java.util.Locale;

import javax.swing.text.Document;

import com.lp.client.rechtschreibung.IRechtschreibPruefbar;
import com.lp.client.rechtschreibung.SwingRechtschreibAdapter;
import com.lp.editor.ui.LpDecoratedTextPane;
import com.lp.neweditor.common.EditorZoomFaktor;
import com.lp.neweditor.data.block.TextBlockModel;
import com.lp.neweditor.data.block.TextBlockModel;
import com.lp.neweditor.ui.editor.HvBlockEditor;

public class TextBlockController extends BasicBlockController<TextBlockModel, TextBlockView>
		implements IRechtschreibPruefbar {

	private static final int MAXIMUM_CHAR_COUNT = 3000;

	private SwingRechtschreibAdapter rechtschreibAdapter;

	public TextBlockController(HvBlockEditor editor) {
		super(editor);
		rechtschreibAdapter = new SwingRechtschreibAdapter(getView().getUIComponent());
	}

	@Override
	public boolean canBeDragged() {
		// Textblock kann nie gezogen werden -> immer Text markieren
		return false;
	}

	@Override
	protected TextBlockView createView() {
		return new TextBlockView(this);
	}

	/**
	 * Wird bei einem PropertyChange event der MaximumLengthProperty des Models
	 * aufgerufen
	 * 
	 * @param event
	 */
	protected void onMaximumLengthChange(PropertyChangeEvent event) {
		// TODO
	}

	@Override
	public TextBlockModel createDefaultModel() {
		Document defDoc = getView().getUIComponent().getEditorKit().createDefaultDocument();
		defDoc.putProperty(LpDecoratedTextPane.ZoomfactorProperty, getZoom().getFactor());
		TextBlockModel newModel = new TextBlockModel(MAXIMUM_CHAR_COUNT, defDoc);
		return newModel;
	}

	@Override
	protected void attachToModel(TextBlockModel model) {
		super.attachToModel(model);
		model.getDocument().putProperty(LpDecoratedTextPane.ZoomfactorProperty, getZoom().getFactor());
	}

	public void setText(String text) {
		getModel().setText(text);
	}

	protected void onDocChange(Document newDoc) {
		newDoc.putProperty(LpDecoratedTextPane.ZoomfactorProperty, getZoom().getFactor());
	}

	@Override
	protected void setZoom(EditorZoomFaktor zoom) {
		getModel().getDocument().putProperty(LpDecoratedTextPane.ZoomfactorProperty, zoom.getFactor());
		getView().getUIComponent().updateUI();
	}

	@Override
	public void aktiviereRechtschreibpruefung() {
		rechtschreibAdapter.aktiviereRechtschreibpruefung();
	}

	@Override
	public void deaktiviereRechtschreibpruefung() {
		rechtschreibAdapter.deaktiviereRechtschreibpruefung();
	}

	@Override
	public void setRechtschreibpruefungLocale(Locale loc) {
		rechtschreibAdapter.setRechtschreibpruefungLocale(loc);
	}

	@Override
	protected BlockEventSupport createEventSupport() {
		return new TextEventSupport();
	}

	protected class TextEventSupport extends BlockEventSupport {

		@Override
		protected boolean tryHandlePropertyChange(PropertyChangeEvent evt) {
			switch (evt.getPropertyName()) {
			case TextBlockModel.propertyDocument:
				onDocChange((Document) evt.getNewValue());
				return true;
			case TextBlockModel.propertyMaxLength:
				onMaximumLengthChange(evt);
				return true;
			default:
				return false;
			}
		}

	}


}
