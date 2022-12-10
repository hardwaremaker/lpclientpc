package com.lp.neweditor.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

public class AutocompleteComboBox<E> extends JComboBox<E> {
	private static final long serialVersionUID = 2L;

//	private final KeyListener editorKeyListener;
	private final TreeMap<String, E> values;

	public AutocompleteComboBox(E[] data) {
		super(data);
		this.values = new TreeMap<>();
		for (E e : data) {
			values.put(e.toString().toLowerCase(), e);
		}
//		editorKeyListener = new EditorKeyListener();
		setEditable(true);
		if (editor != null) {
//			editor.getEditorComponent().addKeyListener(editorKeyListener);
			JTextComponent editorComp = (JTextComponent) editor.getEditorComponent();
			editorComp.setDocument(new AutocompleteDocument());
		}
	}

	@SuppressWarnings("unchecked")
	public AutocompleteComboBox(Collection<E> data) {
		this((E[]) data.toArray());
	}

	@Override
	public void setEditor(ComboBoxEditor anEditor) {
		super.setEditor(anEditor);
		JTextComponent editorComp = (JTextComponent) editor.getEditorComponent();
		editorComp.setDocument(new AutocompleteDocument());
	}

	private class EditorKeyListener extends KeyAdapter {
		@Override
		public void keyTyped(KeyEvent e) {
			Dimension size = getPreferredSize();
			Object selectedItem = getEditor().getItem();
			String typed = selectedItem.toString();
			E firstObject = values.ceilingEntry(typed.toLowerCase()).getValue();

			System.out.println("Setting selected: " + firstObject.toString());
			setSelectedItem(firstObject);
			getEditor().setItem(firstObject);

			setPreferredSize(size);
			Component comp = getEditor().getEditorComponent();
			if (comp instanceof JTextComponent) {
				JTextComponent textComp = (JTextComponent) comp;
//					textComp.setCaretPosition(textComp.getDocument().getLength());
				textComp.setSelectionStart(typed.length());
				textComp.setSelectionEnd(textComp.getDocument().getLength());
			}
//				showPopup();
		}
	}

	private class AutocompleteDocument extends PlainDocument {
		private static final long serialVersionUID = 1L;
		private boolean selecting = false;

		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if (selecting)
				return;
			super.insertString(offs, str, a);
			String content = getText(0, getLength());
			Object item = lookupItem(content);
			// Verhindere nested aufrufe
			selecting = true;
			setSelectedItem(item);
			selecting = false;

			//Setze ganzes Item als Text
			remove(0, getLength());
			super.insertString(0, item.toString(), a);

			//Markiere nicht vom Nutzer eingegebenen Teil
			JTextComponent textComp = (JTextComponent) editor.getEditorComponent();
			textComp.setSelectionStart(offs + str.length());
			textComp.setSelectionEnd(getLength());
		}

		@Override
		public void remove(int offs, int len) throws BadLocationException {
			if (!selecting) {
				super.remove(offs, len);
			}
		}

		@Override
		public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			if (!selecting) {
				super.replace(offset, length, text, attrs);
			}
		}

		private E lookupItem(String pattern) {
			Map.Entry<String, E> entry = values.ceilingEntry(pattern.toLowerCase());
			if (entry.getKey().startsWith(pattern.toLowerCase())) {
				return entry.getValue();
			}
			// no item starts with the pattern => return null
			return null;
		}
	}
}
