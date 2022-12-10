package com.lp.neweditor.ui.menu;

import java.awt.Color;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import com.lp.editor.util.LpEditorMessages;
import com.lp.neweditor.data.block.TextBlockModel;
import com.lp.neweditor.event.ModelChangedEvent;
import com.lp.neweditor.event.ModelChangedListener;
import com.lp.neweditor.excep.EditorError;
import com.lp.neweditor.ui.block.TextBlockController;

public class TextEditorStatusBar extends EditorStatusBar
		implements DocumentListener, PropertyChangeListener, ModelChangedListener<TextBlockModel> {

	private static final long serialVersionUID = 1L;

	private BoundedRangeModel bufferModel;
	private int realValue = 0;
	private boolean overflowed = false;

	private JLabel labelFuellstand;

	private JProgressBar bufferStatus;
	private Color oldColor;

	public TextEditorStatusBar(TextBlockController controller) {
		this();
		install(controller);
	}

	public TextEditorStatusBar() {
		bufferModel = new DefaultBoundedRangeModel();
		bufferStatus = new JProgressBar(bufferModel);
		bufferStatus.setStringPainted(true);

		labelFuellstand = new JLabel(LpEditorMessages.getInstance().getString("Status.BufferInfo"));

		setLayout(new FlowLayout(FlowLayout.RIGHT));
		add(labelFuellstand);
		add(bufferStatus);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
		case TextBlockModel.propertyDocument:
			Document oldDoc = (Document) evt.getOldValue();
			Document newDoc = (Document) evt.getNewValue();

			if (oldDoc != null) {
				oldDoc.removeDocumentListener(this);
			}
			newDoc.addDocumentListener(this);
			realValue = newDoc.getLength();
			bufferModel.setValue(realValue);
			checkForOverflow();
			break;
		case TextBlockModel.propertyMaxLength:
			Integer newMaxLength = (Integer) evt.getNewValue();
			bufferModel.setMaximum(newMaxLength);
			checkForOverflow();
			break;
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		realValue += e.getLength();
		bufferModel.setValue(realValue);
		checkForOverflow();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		realValue -= e.getLength();
		bufferModel.setValue(realValue);
		checkForOverflow();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	@Override
	public void dataChanged(ModelChangedEvent<TextBlockModel> e) {
		TextBlockModel oldModel = e.getDataBefore();
		TextBlockModel newModel = e.getDataAfter();
		if (oldModel != null) {
			oldModel.removeDocumentListener(this);
			oldModel.removePropertyChangeListener(this);
		}
		setModel(newModel);
	}

	public void install(TextBlockController controller) {
		TextBlockModel model = controller.getModel();
		if (model != null) {
			setModel(model);
		}
		controller.addModelChangeListener(this);
	}

	private void setModel(TextBlockModel model) {
		model.addPropertyChangeListener(this);
		model.addDocumentListener(this);
		Document doc = model.getDocument();
		int maximumLength = model.getMaximumLength();
		bufferModel.setMaximum(maximumLength);
		realValue = doc.getLength();
		bufferModel.setValue(realValue);
		checkForOverflow();
	}

	private void checkForOverflow() {
		if (realValue != bufferModel.getValue()) {
			bufferModel.setValue(realValue);
		}
		if (realValue > bufferModel.getMaximum()) {
			if (!overflowed) {
				overflowed = true;
				oldColor = bufferStatus.getForeground();
				bufferStatus.setForeground(Color.red);
				labelFuellstand.setText(LpEditorMessages.getInstance().getString("Status.BufferOverflow"));
			}
		} else if (realValue < 0) {
			throw new EditorError("Internal Error: negative String length is impossible");
		} else {
			//No overflow
			if(overflowed) {
				overflowed = false;
				bufferStatus.setForeground(oldColor);
				labelFuellstand.setText(LpEditorMessages.getInstance().getString("Status.BufferInfo"));
			}
		}

	}

}
