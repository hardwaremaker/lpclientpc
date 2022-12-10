package com.lp.neweditor.action;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.lp.neweditor.common.NewEditorHelper;

/**
 * ItemListener zum Umstellen der FontSize eines JTextPanes. <br>
 * Nimmt an, dass ItemEvents mit Objekten des Typs {@link Integer} erhalten
 * werden
 * 
 * @author Alexander Daum
 *
 */
public class ComboBoxFontSizeListener implements ItemListener {
	private final JTextPane editor;

	public ComboBoxFontSizeListener(JTextPane editor) {
		this.editor = editor;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (editor.getSelectionStart() != editor.getSelectionEnd()) {
				Integer fontSize = (Integer) e.getItem();
				MutableAttributeSet attrSet = new SimpleAttributeSet();
				StyleConstants.setFontSize(attrSet, fontSize);
				editor.setCharacterAttributes(attrSet, false);
				NewEditorHelper.updateUIAndCaret(editor, true);
			}
		}
	}

}
