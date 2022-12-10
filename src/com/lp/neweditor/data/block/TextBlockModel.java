package com.lp.neweditor.data.block;

import java.io.IOException;
import java.io.StringWriter;

import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

import com.lp.editor.text.LpJasperGenerator;
import com.lp.editor.text.LpJasperParser;
import com.lp.neweditor.data.boundproperty.BoundProperty;
import com.lp.neweditor.data.boundproperty.BoundPropertyInt;
import com.lp.server.system.service.EditorBaseBlockDto;
import com.lp.server.system.service.EditorTextBlockDto;

public class TextBlockModel extends BaseBlockModel {
	/**
	 * Property Maximale L&auml;nge des Textes. <br>
	 * Type: {@link Integer}
	 */
	public static final String propertyMaxLength = "maximumLength";
	/**
	 * Property Document, das den Text darstellt. <br>
	 * Type: {@link Document}
	 */
	public static final String propertyDocument = "document";

	private BoundPropertyInt maximumLength;

	private BoundProperty<Document> document;

	private final UndoableEditListener editListener;

	public TextBlockModel(int maximumLength, Document document) {
		this();
		this.maximumLength = new BoundPropertyInt(propertyMaxLength, maximumLength, propChangeSupport);
		this.document = new BoundProperty<Document>(propertyDocument, document, propChangeSupport);
		document.addUndoableEditListener(editListener);
	}

	public TextBlockModel() {
		editListener = ev -> getUndoSupport().postEdit(ev.getEdit());
	}

	public String getText() {
		StyledDocument doc = (StyledDocument)document.getValue();
		return LpJasperGenerator.getTextBlockContent(doc, 0, doc.getLength());
	}

	public int getMaximumLength() {
		return maximumLength.getValue();
	}

	public void setMaximumLength(int maximumLength) {
		this.maximumLength.setValue(maximumLength);
	}

	public int getLength() {
		return document.getValue().getLength();
	}

	public void setText(String newText) {
		try {
			StyledDocument doc = (StyledDocument) getDocument();
			doc.remove(0, doc.getLength());
			LpJasperParser.parseString(newText, doc, 0);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void addDocumentListener(DocumentListener docListener) {
		document.getValue().addDocumentListener(docListener);
	}

	public void removeDocumentListener(DocumentListener docListener) {
		document.getValue().removeDocumentListener(docListener);
	}

	public Document getDocument() {
		return document.getValue();
	}

	public void setDocument(Document doc) {
		Document oldDoc = this.document.getValue();
		if (oldDoc != null) {
			oldDoc.removeUndoableEditListener(editListener);
		}
		this.document.setValue(doc);
		doc.addUndoableEditListener(editListener);
	}

	@Override
	protected EditorTextBlockDto createEmptyDto() {
		return new EditorTextBlockDto();
	}

	@Override
	protected void populateDto(EditorBaseBlockDto dto) {
		super.populateDto(dto);
		EditorTextBlockDto textDto = (EditorTextBlockDto) dto;
		textDto.setText(getText());
	}

	@Override
	public void fromDto(EditorBaseBlockDto dto) {
		super.fromDto(dto);
		EditorTextBlockDto textDto = (EditorTextBlockDto) dto;
		setText(textDto.getText());
	}

}
